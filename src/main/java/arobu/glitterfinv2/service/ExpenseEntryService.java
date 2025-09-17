package arobu.glitterfinv2.service;

import arobu.glitterfinv2.model.dto.ExpenseEntryApiPostDTO;
import arobu.glitterfinv2.model.dto.ExpenseEntryForm;
import arobu.glitterfinv2.model.entity.ExpenseEntry;
import arobu.glitterfinv2.model.entity.ExpenseOwner;
import arobu.glitterfinv2.model.entity.Location;
import arobu.glitterfinv2.model.mapper.ExpenseEntryMapper;
import arobu.glitterfinv2.model.repository.ExpenseEntryRepository;
import arobu.glitterfinv2.service.exception.ExpenseNotFoundException;
import arobu.glitterfinv2.service.exception.OwnerNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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
        String username = getCurrentUsername();
        return expenseEntryRepository.findAllByOwner_Username(username);
    }

    public ExpenseEntry getExpenseForCurrentUser(Integer expenseId) {
        String username = getCurrentUsername();
        return expenseEntryRepository.findByIdAndOwner_Username(expenseId, username)
                .orElseThrow(() -> new ExpenseNotFoundException(expenseId));
    }

    public ExpenseEntry updateExpense(Integer expenseId, ExpenseEntryForm form) {
        ExpenseEntry expenseEntry = getExpenseForCurrentUser(expenseId);

        if (Objects.nonNull(form.getAmount())) {
            expenseEntry.setAmount(form.getAmount());
        }
        expenseEntry.setSource(form.getSource());
        expenseEntry.setMerchant(form.getMerchant());
        expenseEntry.setCategory(form.getCategory());
        expenseEntry.setDescription(form.getDescription());
        expenseEntry.setDetails(form.getDetails());
        expenseEntry.setShared(Boolean.TRUE.equals(form.getShared()));
        expenseEntry.setOutlier(Boolean.TRUE.equals(form.getOutlier()));

        LOGGER.info("Updating expense entry {}", expenseEntry);

        return expenseEntryRepository.save(expenseEntry);
    }

    public void deleteExpense(Integer expenseId) {
        ExpenseEntry expenseEntry = getExpenseForCurrentUser(expenseId);
        expenseEntryRepository.delete(expenseEntry);
        LOGGER.info("Deleted expense entry with id {}", expenseId);
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            throw new IllegalStateException("No authenticated user available");
        }
        return authentication.getName();
    }
}
