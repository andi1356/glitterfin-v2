package arobu.glitterfinv2.configuration.security.web;

import arobu.glitterfinv2.service.ExpenseOwnerService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class ExpenseOwnerDetailsService implements UserDetailsService {

    private final ExpenseOwnerService expenseOwnerService;

    public ExpenseOwnerDetailsService(ExpenseOwnerService expenseOwnerService) {
        this.expenseOwnerService = expenseOwnerService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return expenseOwnerService.getExpenseOwnerEntityByUsername(username);
    }
}
