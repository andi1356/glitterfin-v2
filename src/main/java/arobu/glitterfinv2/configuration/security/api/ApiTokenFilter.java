package arobu.glitterfinv2.configuration.security.api;

import arobu.glitterfinv2.service.OwnerService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static java.util.Collections.singletonList;
import static java.util.Objects.isNull;

@Component
public class ApiTokenFilter extends OncePerRequestFilter {

    Logger LOGGER = LoggerFactory.getLogger(ApiTokenFilter.class);

    private final OwnerService ownerService;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    public ApiTokenFilter(OwnerService ownerService,
                          @Qualifier("apiEntryPoint") AuthenticationEntryPoint authenticationEntryPoint) {
        this.ownerService = ownerService;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String apiKey = request.getHeader("X-API-KEY");
        String userAgentId = getUserAgentId(request);

        if (isNull(apiKey) || apiKey.isBlank()) {
            LOGGER.error("Missing X-API-KEY Header in request from IP: {} with User-Agent: {}",
                    request.getRemoteAddr(), request.getHeader("user-agent"));
            authenticationEntryPoint.commence(
                    request,
                    response,
                    new AuthenticationCredentialsNotFoundException("Missing API key")
            );
            return;
        } else {
            final var ownerOpt = ownerService.getOwner(userAgentId, apiKey);
            if (ownerOpt.isEmpty()) {
                LOGGER.error("Invalid API use attempt from IP: {} with User-Agent: {}",
                                request.getRemoteAddr(), userAgentId);
                authenticationEntryPoint.commence(
                        request,
                        response,
                        new BadCredentialsException("Invalid API key"));
                return;
            }
            ownerOpt.ifPresent(owner -> {
                OwnerAuthenticationToken roleApiUser = new OwnerAuthenticationToken(
                        owner,
                        apiKey,
                        singletonList(new SimpleGrantedAuthority("ROLE_API_USER"))
                );
                SecurityContextHolder.getContext().setAuthentication(roleApiUser);

                LOGGER.info("Successfully authenticated api user: {} with User-Agent: {}",
                        owner.getUsername(), owner.getUserAgentId());
                    });
            filterChain.doFilter(request, response);
        }
    }

    private static String getUserAgentId(HttpServletRequest request) {
        String userAgentHeader = request.getHeader("user-agent");
        if (isNull(userAgentHeader)) {
            return "null-user-agent";
        } else {
            String shortUserAgentHeaderId = userAgentHeader.split(" ")[0];
            if (isNull(shortUserAgentHeaderId)) {
                return userAgentHeader;
            } else {
                return shortUserAgentHeaderId;
            }
        }
    }
}
