package arobu.glitterfinv2.service;

import arobu.glitterfinv2.model.entity.ExpenseOwner;
import arobu.glitterfinv2.model.repository.ExpenseOwnerRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExpenseOwnerService {
    ExpenseOwnerRepository expenseOwnerRepository;

    public ExpenseOwnerService(ExpenseOwnerRepository expenseOwnerRepository) {
        this.expenseOwnerRepository = expenseOwnerRepository;
    }

    public ExpenseOwner getExpenseOwnerEntityByUsername(String username) {
        return expenseOwnerRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public Optional<ExpenseOwner> getExpenseOwner(String userAgentId, String apiKey) {
        ExpenseOwner expenseOwner = expenseOwnerRepository
                .getExpenseOwnerByUserAgentId(userAgentId);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        if (passwordEncoder.matches(apiKey, expenseOwner.getApiToken())) {
            return Optional.of(expenseOwner);
        } else {
            return Optional.empty();
        }
    }
}
