package arobu.glitterfinv2.service;

import arobu.glitterfinv2.model.dto.ExpenseEntryApiPostDTO;
import arobu.glitterfinv2.model.dto.ExpenseForm;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    public ExpenseEntry saveExpense(final ExpenseEntryApiPostDTO expenseEntryApiPostDTO) throws OwnerNotFoundException {
        ExpenseOwner owner = expenseOwnerService.getExpenseOwnerEntityByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName());
        Location location = locationService.getOrSaveLocationEntity(expenseEntryApiPostDTO.getLocationData());

        ExpenseEntry entity = ExpenseEntryMapper.toEntity(expenseEntryApiPostDTO, owner, location);
        LOGGER.info("Persisting expense entry: {}", entity);
        return expenseEntryRepository.save(entity);
    }

    public List<ExpenseEntry> getExpensesForCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return expenseEntryRepository.findAllByOwner_Username(username);
    }

    public ExpenseEntry getExpenseForCurrentUser(Integer expenseId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<ExpenseEntry> optionalExpense = expenseEntryRepository.findById(expenseId);
        return optionalExpense
                .filter(expense -> expense.getOwner() != null && username.equals(expense.getOwner().getUsername()))
                .orElseThrow(() -> new ExpenseNotFoundException(expenseId));
    }

    @Transactional
    public ExpenseEntry updateExpense(Integer expenseId, ExpenseForm form) {
        ExpenseEntry expense = getExpenseForCurrentUser(expenseId);

        expense.setAmount(form.getAmount());
        expense.setCategory(form.getCategory());
        expense.setDescription(form.getDescription());
        expense.setMerchant(form.getMerchant());
        expense.setSource(form.getSource());
        expense.setShared(Boolean.TRUE.equals(form.getShared()));
        expense.setOutlier(Boolean.TRUE.equals(form.getOutlier()));

        return expenseEntryRepository.save(expense);
    }

    @Transactional
    public void deleteExpense(Integer expenseId) {
        ExpenseEntry expense = getExpenseForCurrentUser(expenseId);
        expenseEntryRepository.delete(expense);
    }
}
