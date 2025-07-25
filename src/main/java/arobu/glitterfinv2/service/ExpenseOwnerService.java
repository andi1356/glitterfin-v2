package arobu.glitterfinv2.service;

import arobu.glitterfinv2.model.entity.ExpenseOwner;
import arobu.glitterfinv2.model.repository.ExpenseOwnerRepository;
import arobu.glitterfinv2.service.exception.OwnerNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ExpenseOwnerService {
    ExpenseOwnerRepository expenseOwnerRepository;

    public ExpenseOwnerService(ExpenseOwnerRepository expenseOwnerRepository) {
        this.expenseOwnerRepository = expenseOwnerRepository;
    }

    public ExpenseOwner getExpenseOwnerEntity(String id) throws OwnerNotFoundException {
        return expenseOwnerRepository.findById(id)
                .orElseThrow(() -> new OwnerNotFoundException(id));
    }
}
