package arobu.glitterfinv2.service;

import arobu.glitterfinv2.model.entity.ExpenseOwner;
import arobu.glitterfinv2.model.repository.ExpenseOwnerRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExpenseOwnerService {
    private final ExpenseOwnerRepository expenseOwnerRepository;
    private final PasswordEncoder passwordEncoder;

    public ExpenseOwnerService(ExpenseOwnerRepository expenseOwnerRepository, PasswordEncoder passwordEncoder) {
        this.expenseOwnerRepository = expenseOwnerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ExpenseOwner getExpenseOwnerEntityByUsername(String username) {
        return expenseOwnerRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public Optional<ExpenseOwner> getExpenseOwner(String userAgentId, String apiKey) {
        return expenseOwnerRepository
                .getExpenseOwnerByUserAgentId(userAgentId)
                .filter(owner -> passwordEncoder.matches(apiKey, owner.getApiToken()));
    }
}
