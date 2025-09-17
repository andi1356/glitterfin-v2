package arobu.glitterfinv2.service;

import arobu.glitterfinv2.model.dto.ExpenseEntryApiPostDTO;
import arobu.glitterfinv2.model.dto.ExpenseFormData;
import arobu.glitterfinv2.model.entity.ExpenseEntry;
import arobu.glitterfinv2.model.entity.ExpenseOwner;
import arobu.glitterfinv2.model.entity.Location;
import arobu.glitterfinv2.model.mapper.ExpenseEntryMapper;
import arobu.glitterfinv2.model.repository.ExpenseEntryRepository;
import arobu.glitterfinv2.service.exception.ExpenseNotFoundException;
import arobu.glitterfinv2.service.exception.OwnerNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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

    public List<ExpenseEntry> getExpensesForOwner(final String username) {
        return expenseEntryRepository.findAllByOwner_UsernameOrderByTimestampDesc(username);
    }

    public List<ExpenseEntry> getExpensesForCurrentUser() {
        return getExpensesForOwner(resolveCurrentUsername());
    }

    public ExpenseEntry getExpenseForOwner(final Integer id, final String username) throws ExpenseNotFoundException {
        return expenseEntryRepository.findByIdAndOwner_Username(id, username)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense " + id + " was not found for user " + username));
    }

    public ExpenseEntry updateExpense(final Integer id, final ExpenseFormData formData, final String username) throws ExpenseNotFoundException {
        ExpenseEntry expenseEntry = getExpenseForOwner(id, username);

        if (formData.getAmount() != null) {
            expenseEntry.setAmount(formData.getAmount());
        }
        expenseEntry.setSource(trimToNull(formData.getSource()));
        expenseEntry.setMerchant(trimToNull(formData.getMerchant()));
        expenseEntry.setCategory(trimToNull(formData.getCategory()));
        expenseEntry.setDescription(trimToNull(formData.getDescription()));
        expenseEntry.setShared(formData.isShared());
        expenseEntry.setOutlier(formData.isOutlier());

        if (formData.getOccurredAt() != null) {
            ZoneId zoneId = resolveZoneId(expenseEntry);
            ZonedDateTime updatedTimestamp = formData.getOccurredAt().atZone(zoneId);
            expenseEntry.setTimestamp(updatedTimestamp);
            expenseEntry.setTimezone(zoneId.getId());
        }

        LOGGER.info("Updating expense entry {} for user {}", id, username);
        return expenseEntryRepository.save(expenseEntry);
    }

    public void deleteExpense(final Integer id, final String username) throws ExpenseNotFoundException {
        ExpenseEntry expenseEntry = getExpenseForOwner(id, username);
        LOGGER.info("Deleting expense entry {} for user {}", id, username);
        expenseEntryRepository.delete(expenseEntry);
    }

    private ZoneId resolveZoneId(ExpenseEntry entry) {
        if (entry.getTimezone() != null) {
            try {
                return ZoneId.of(entry.getTimezone());
            } catch (DateTimeException ex) {
                LOGGER.warn("Failed to parse timezone '{}' for expense {}", entry.getTimezone(), entry.getId(), ex);
            }
        }
        if (entry.getTimestamp() != null) {
            return entry.getTimestamp().getZone();
        }
        return ZoneId.systemDefault();
    }

    private String resolveCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalStateException("No authenticated user available");
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }
        return String.valueOf(principal);
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
