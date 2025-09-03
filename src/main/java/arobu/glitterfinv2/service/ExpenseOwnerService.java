package arobu.glitterfinv2.service;

import arobu.glitterfinv2.model.entity.ExpenseOwner;
import arobu.glitterfinv2.model.repository.ExpenseOwnerRepository;
import arobu.glitterfinv2.service.exception.OwnerNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ExpenseOwnerService {
    ExpenseOwnerRepository expenseOwnerRepository;

    public ExpenseOwnerService(ExpenseOwnerRepository expenseOwnerRepository) {
        this.expenseOwnerRepository = expenseOwnerRepository;
    }

    public ExpenseOwner getExpenseOwnerEntityById(String id) throws OwnerNotFoundException {
        return expenseOwnerRepository.findById(id)
                .orElseThrow(() -> new OwnerNotFoundException(id));
    }

    public ExpenseOwner getExpenseOwnerEntityByUsername(String username) {
        return expenseOwnerRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public boolean validate(String userAgentId, String apiKey) {
        String encodedApiToken = expenseOwnerRepository
                .getExpenseOwnerByUserAgentId(userAgentId)
                .getApiToken();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        if (passwordEncoder.matches(apiKey, encodedApiToken)) {
            return expenseOwnerRepository.
                existsExpenseOwnerByUserAgentIdAndApiToken(userAgentId, encodedApiToken);
        }
        return false;
    }
}
