package arobu.glitterfinv2.service;

import arobu.glitterfinv2.model.dto.ExpenseEntryApiPostDTO;
import arobu.glitterfinv2.model.dto.ExpenseEntryUpdateDTO;
import arobu.glitterfinv2.model.entity.ExpenseEntry;
import arobu.glitterfinv2.model.entity.ExpenseOwner;
import arobu.glitterfinv2.model.entity.Location;
import arobu.glitterfinv2.model.mapper.ExpenseEntryMapper;
import arobu.glitterfinv2.model.repository.ExpenseEntryRepository;
import arobu.glitterfinv2.service.exception.ExpenseNotFoundException;
import arobu.glitterfinv2.service.exception.OwnerNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseEntryService {

    Logger LOGGER = LogManager.getLogger(ExpenseEntryService.class);

    private final ExpenseEntryRepository expenseEntryRepository;
    private final ExpenseOwnerService expenseOwnerService;
    private final LocationService locationService;

    public ExpenseEntryService(ExpenseEntryRepository expenseEntryRepository, ExpenseOwnerService expenseOwnerService, LocationService locationService) {
        this.expenseEntryRepository = expenseEntryRepository;
        this.expenseOwnerService = expenseOwnerService;
        this.locationService = locationService;
    }

    public ExpenseEntry saveExpense(final ExpenseEntryApiPostDTO expenseEntryApiPostDTO, final String username) throws OwnerNotFoundException {
        ExpenseOwner owner = expenseOwnerService.getExpenseOwnerEntityByUsername(username);
        Location location = locationService.getOrSaveLocationEntity(expenseEntryApiPostDTO.getLocationData());

        ExpenseEntry entity = ExpenseEntryMapper.toEntity(expenseEntryApiPostDTO, owner, location);
        LOGGER.info("Persisting expense entry: {}", entity);
        return expenseEntryRepository.save(entity);
    }

    public List<ExpenseEntry> getExpensesForAuthenticatedUser() {
        String username = getCurrentUsername();
        return expenseEntryRepository.findAllByOwner_Username(username);
    }

    public ExpenseEntry getExpenseForAuthenticatedUser(Integer id) {
        String username = getCurrentUsername();
        return expenseEntryRepository.findById(id)
                .filter(expense -> expense.getOwner() != null && username.equals(expense.getOwner().getUsername()))
                .orElseThrow(() -> new ExpenseNotFoundException(id));
    }

    public ExpenseEntry updateExpense(Integer id, ExpenseEntryUpdateDTO updateDTO) {
        ExpenseEntry expense = getExpenseForAuthenticatedUser(id);

        if (updateDTO.getAmount() != null) {
            expense.setAmount(updateDTO.getAmount());
        }
        expense.setCategory(updateDTO.getCategory());
        expense.setMerchant(updateDTO.getMerchant());
        expense.setSource(updateDTO.getSource());
        expense.setDescription(updateDTO.getDescription());
        expense.setDetails(updateDTO.getDetails());
        if (updateDTO.getShared() != null) {
            expense.setShared(updateDTO.getShared());
        }
        if (updateDTO.getOutlier() != null) {
            expense.setOutlier(updateDTO.getOutlier());
        }

        ExpenseEntry saved = expenseEntryRepository.save(expense);
        LOGGER.info("Updated expense entry {}", saved.getId());
        return saved;
    }

    public void deleteExpense(Integer id) {
        ExpenseEntry expense = getExpenseForAuthenticatedUser(id);
        expenseEntryRepository.delete(expense);
        LOGGER.info("Deleted expense entry {}", id);
    }

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }
        return principal.toString();
    }
}
