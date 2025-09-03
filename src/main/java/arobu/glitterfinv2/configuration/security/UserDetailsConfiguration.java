package arobu.glitterfinv2.configuration.security;

import arobu.glitterfinv2.model.entity.ExpenseOwner;
import arobu.glitterfinv2.service.ExpenseOwnerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class UserDetailsConfiguration {

    Logger LOGGER = LogManager.getLogger(UserDetailsConfiguration.class);

    private final ExpenseOwnerService expenseOwnerService;

    public UserDetailsConfiguration(ExpenseOwnerService expenseOwnerService) {
        this.expenseOwnerService = expenseOwnerService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            ExpenseOwner user = expenseOwnerService.getExpenseOwnerEntityByUsername(username);
            return User.withUsername(user.getUsername())
                    .password(user.getPassword())
                    .roles("USER")
                    .build();
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider(
            UserDetailsService uds, PasswordEncoder encoder) {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider(uds);
        p.setPasswordEncoder(encoder);
        return p;
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, exception) -> {
            LOGGER.info("Login successful for user: {} from IP: {}",
                    request.getParameter("username"), request.getRemoteAddr());
            response.sendRedirect("/finances");
        };
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
