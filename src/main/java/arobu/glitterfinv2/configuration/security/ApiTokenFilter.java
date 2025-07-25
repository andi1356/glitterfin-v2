package arobu.glitterfinv2.configuration.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static java.util.Collections.singletonList;
import static java.util.Objects.isNull;

@Component
public class ApiTokenFilter extends OncePerRequestFilter {

    Logger LOGGER = LogManager.getLogger(ApiTokenFilter.class);

    private static final String API_KEY_HEADER_NAME = "X-API-KEY";
    @Value("${security.api-token}")
    private String API_TOKEN_VALID_VALUE;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String apiKey = request.getHeader(API_KEY_HEADER_NAME);

        if (apiKey == null) {
            filterChain.doFilter(request, response);
        } else {
            if (!apiKey.equals(API_TOKEN_VALID_VALUE)) {
                LOGGER.warn("Invalid API key attempt from IP: {} with User-Agent: {}",
                        request.getRemoteAddr(), request.getHeader("User-Agent"));

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid API key");
                return;
            }
            String userAgentId = getUserAgentId(request);
            PreAuthenticatedAuthenticationToken apiUser = new PreAuthenticatedAuthenticationToken(
                    "api-client-id:" + userAgentId,
                    apiKey,
                    singletonList(new SimpleGrantedAuthority("ROLE_API_USER")));
            apiUser.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(apiUser);

            LOGGER.info("Successfully authenticated api user: {}", userAgentId);

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
