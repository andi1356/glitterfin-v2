package arobu.glitterfinv2.service;

import arobu.glitterfinv2.model.dto.ExpenseEntryApiPostDTO;
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

    public List<ExpenseEntry> getExpensesForUser(final String username) {
        return expenseEntryRepository.findAllByOwner_UsernameOrderByTimestampDesc(username);
    }

    public ExpenseEntry getExpenseForUser(final String username, final Integer expenseId) throws ExpenseNotFoundException {
        return expenseEntryRepository.findByIdAndOwner_Username(expenseId, username)
                .orElseThrow(() -> new ExpenseNotFoundException(expenseId));
    }

    public ExpenseEntry updateExpenseForUser(final String username,
                                             final Integer expenseId,
                                             final java.util.function.Consumer<ExpenseEntry> mutator)
            throws ExpenseNotFoundException {
        ExpenseEntry expense = getExpenseForUser(username, expenseId);
        mutator.accept(expense);
        return expenseEntryRepository.save(expense);
    }

    public void deleteExpenseForUser(final String username, final Integer expenseId) throws ExpenseNotFoundException {
        ExpenseEntry expense = getExpenseForUser(username, expenseId);
        expenseEntryRepository.delete(expense);
    }
}
