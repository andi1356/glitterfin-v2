package arobu.glitterfinv2.configuration.security.api;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.AuthenticationEntryPoint;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@Configuration
public class ApiSecurityConfiguration {

    @Bean
    public FilterRegistrationBean<ApiTokenFilter> apiTokenFilterRegistration(ApiTokenFilter filter) {
        FilterRegistrationBean<ApiTokenFilter> reg = new FilterRegistrationBean<>(filter);
        reg.setEnabled(false);
        return reg;
    }

    @Bean
    public AuthenticationEntryPoint apiEntryPoint() {
        return (req, res, ex) -> {
            res.setStatus(SC_UNAUTHORIZED);
            res.setContentType("application/json");
            res.getWriter().write("{\"error\":\"unauthorized\",\"message\":\"API key missing or invalid\"}");
        };
    }
}
