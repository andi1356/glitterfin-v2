package arobu.glitterfinv2.service;

import arobu.glitterfinv2.model.dto.ExpenseEntryApiPostDTO;
import arobu.glitterfinv2.model.dto.ExpenseEntryUpdateForm;
import arobu.glitterfinv2.model.entity.ExpenseEntry;
import arobu.glitterfinv2.model.entity.ExpenseOwner;
import arobu.glitterfinv2.model.entity.Location;
import arobu.glitterfinv2.model.mapper.ExpenseEntryMapper;
import arobu.glitterfinv2.model.repository.ExpenseEntryRepository;
import arobu.glitterfinv2.service.exception.OwnerNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Collections;
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

    public ExpenseEntry saveExpense(final ExpenseEntryApiPostDTO expenseEntryApiPostDTO, final String username) throws OwnerNotFoundException {
        ExpenseOwner owner = expenseOwnerService.getExpenseOwnerEntityByUsername(username);
        Location location = locationService.getOrSaveLocationEntity(expenseEntryApiPostDTO.getLocationData());

        ExpenseEntry entity = ExpenseEntryMapper.toEntity(expenseEntryApiPostDTO, owner, location);
        LOGGER.info("Persisting expense entry: {}", entity);
        return expenseEntryRepository.save(entity);
    }

    public List<ExpenseEntry> getExpensesForUser(final String username) {
        if (username == null || username.isBlank()) {
            LOGGER.debug("Requested expenses for empty username");
            return Collections.emptyList();
        }

        LOGGER.debug("Fetching expenses for user: {}", username);
        return expenseEntryRepository.findAllByOwner_Username(username);
    }

    public Optional<ExpenseEntry> getExpenseForUser(final Integer expenseId, final String username) {
        if (expenseId == null || username == null || username.isBlank()) {
            LOGGER.debug("Attempted to fetch expense with invalid data. id: {}, username: {}", expenseId, username);
            return Optional.empty();
        }

        LOGGER.debug("Fetching expense {} for user {}", expenseId, username);
        return expenseEntryRepository.findByIdAndOwner_Username(expenseId, username);
    }

    public Optional<ExpenseEntry> updateExpenseForUser(final Integer expenseId, final String username, final ExpenseEntryUpdateForm form) {
        if (form == null) {
            LOGGER.warn("Attempted to update expense {} for user {} with empty form", expenseId, username);
            return Optional.empty();
        }

        return getExpenseForUser(expenseId, username).map(expenseEntry -> {
            expenseEntry.setDescription(form.getDescription());
            expenseEntry.setCategory(form.getCategory());
            expenseEntry.setMerchant(form.getMerchant());
            expenseEntry.setAmount(form.getAmount());

            LOGGER.info("Updating expense {} for user {}", expenseId, username);
            return expenseEntryRepository.save(expenseEntry);
        });
    }

    public boolean deleteExpenseForUser(final Integer expenseId, final String username) {
        Optional<ExpenseEntry> expenseEntry = getExpenseForUser(expenseId, username);

        if (expenseEntry.isEmpty()) {
            LOGGER.warn("Attempted to delete missing expense {} for user {}", expenseId, username);
            return false;
        }

        LOGGER.info("Deleting expense {} for user {}", expenseId, username);
        expenseEntryRepository.delete(expenseEntry.get());
        return true;
    }
}
