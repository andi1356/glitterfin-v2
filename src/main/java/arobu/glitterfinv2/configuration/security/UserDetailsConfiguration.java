package arobu.glitterfinv2.configuration.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
public class UserDetailsConfiguration {

    Logger LOGGER = LogManager.getLogger(UserDetailsConfiguration.class);

    @Value("${security.username}")
    private String USERNAME;

    @Value("${security.password}")
    private String PASSWORD;

    @Bean
    public UserDetailsService userDetailsService() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10);

        UserDetails user =
                User.withUsername(USERNAME)
                        .password("{bcrypt}%s".formatted(bCryptPasswordEncoder.encode(PASSWORD)))
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            LOGGER.warn("Login failed for user: {} from IP: {}",
                    request.getParameter("username"), request.getRemoteAddr());
            response.sendRedirect("/login?error");
        };
    }
}
