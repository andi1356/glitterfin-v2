package arobu.glitterfinv2.service;

import arobu.glitterfinv2.model.dto.ExpenseEntryCrudDTO;
import arobu.glitterfinv2.model.dto.ExpenseEntryPostDTO;
import arobu.glitterfinv2.model.dto.ExpenseFrontendDTO;
import arobu.glitterfinv2.model.dto.LocationData;
import arobu.glitterfinv2.model.entity.ExpenseEntry;
import arobu.glitterfinv2.model.entity.ExpenseOwner;
import arobu.glitterfinv2.model.entity.Location;
import arobu.glitterfinv2.model.repository.ExpenseEntryRepository;
import arobu.glitterfinv2.service.exception.OwnerNotFoundException;
import arobu.glitterfinv2.service.mapper.ExpenseEntryMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Comparator.comparing;

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

    public ExpenseEntry saveExpense(final ExpenseEntryPostDTO expenseEntryPostDTO) throws OwnerNotFoundException {
        ExpenseOwner owner = expenseOwnerService.getExpenseOwnerEntityById(
                SecurityContextHolder.getContext().getAuthentication().getName());
        Location location = locationService.getOrSaveLocationEntity(expenseEntryPostDTO.getLocationData());

        ExpenseEntry entity = ExpenseEntryMapper.toEntity(expenseEntryPostDTO, owner, location);
        LOGGER.info("Persisting expense entry: {}", entity);
        return expenseEntryRepository.save(entity);
    }

    public ExpenseEntry saveExpense(final ExpenseEntryCrudDTO dto) throws OwnerNotFoundException {
        ExpenseOwner owner = expenseOwnerService.getExpenseOwnerEntityById(
                SecurityContextHolder.getContext().getAuthentication().getName());
        LocationData locationData = new LocationData()
                .setLatitude(dto.getLatitude())
                .setLongitude(dto.getLongitude());
        Location location = locationService.getOrSaveLocationEntity(locationData);

        ExpenseEntry entity = dto.getId() != null ?
                expenseEntryRepository.findById(dto.getId()).orElse(new ExpenseEntry()) : new ExpenseEntry();

        entity.setOwner(owner)
                .setAmount(dto.getAmount())
                .setTimestamp(java.time.ZonedDateTime.parse(dto.getTimestamp()))
                .setTimezone(java.time.ZonedDateTime.parse(dto.getTimestamp()).getZone().getId())
                .setSource(dto.getSource())
                .setMerchant(dto.getMerchant())
                .setCategory(dto.getCategory())
                .setLocation(location);

        LOGGER.info("Persisting expense entry: {}", entity);
        return expenseEntryRepository.save(entity);
    }

    public ExpenseEntryCrudDTO getExpense(Integer id) {
        ExpenseEntry expense = expenseEntryRepository.findById(id).orElseThrow();
        return new ExpenseEntryCrudDTO()
                .setId(expense.getId())
                .setAmount(expense.getAmount())
                .setTimestamp(expense.getTimestamp().toString())
                .setSource(expense.getSource())
                .setMerchant(expense.getMerchant())
                .setCategory(expense.getCategory())
                .setLatitude(expense.getLocation().getLatitude().toString())
                .setLongitude(expense.getLocation().getLongitude().toString());
    }

    public void deleteExpense(Integer id) {
        expenseEntryRepository.deleteById(id);
    }

    public List<ExpenseEntry> getAllExpenses() {
        return expenseEntryRepository.findAll();
    }

    public List<ExpenseFrontendDTO> getExpensesForCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<ExpenseEntry> expenses = expenseEntryRepository.findAllByOwner_Username(username);
        return expenses.stream()
                .map(ExpenseEntryMapper::toFrontend)
                .sorted(comparing(ExpenseFrontendDTO::getTimestamp).reversed())
                .toList();
    }
}
