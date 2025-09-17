package arobu.glitterfinv2.service;

import arobu.glitterfinv2.model.dto.ExpenseEntryPostDTO;
import arobu.glitterfinv2.model.dto.ExpenseEntryQuickShowDTO;
import arobu.glitterfinv2.model.entity.ExpenseEntry;
import arobu.glitterfinv2.model.entity.ExpenseOwner;
import arobu.glitterfinv2.model.entity.Location;
import arobu.glitterfinv2.model.mapper.ExpenseEntryMapper;
import arobu.glitterfinv2.model.repository.ExpenseEntryRepository;
import arobu.glitterfinv2.service.exception.OwnerNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.time.ZonedDateTime.parse;
import static java.util.stream.Collectors.toList;

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

    public ExpenseEntry saveExpense(final ExpenseEntryPostDTO expenseEntryPostDTO, final String username) throws OwnerNotFoundException {
        ExpenseOwner owner = expenseOwnerService.getExpenseOwnerEntityByUsername(username);
        Location location = locationService.getOrSaveLocationEntity(expenseEntryPostDTO.getLocationData());

        ExpenseEntry entity = ExpenseEntryMapper.toEntity(expenseEntryPostDTO, owner, location);
        LOGGER.info("Persisting expense entry: {}", entity);
        return expenseEntryRepository.save(entity);
    }

    public List<ExpenseEntryQuickShowDTO> getExpensesForUser(final String username) {
        return expenseEntryRepository.findAllByOwner_Username(username)
                .stream()
                .map(ExpenseEntryMapper::toQuickShowDTO)
                .collect(toList());
    }

    public ExpenseEntry getExpense(final Integer id) {
        return expenseEntryRepository.findById(id).orElse(null);
    }

    public ExpenseEntry updateExpense(final Integer id, final ExpenseEntryPostDTO dto, final String username) throws OwnerNotFoundException {
        ExpenseEntry existing = getExpense(id);
        if (existing == null) {
            return null;
        }
        ExpenseOwner owner = expenseOwnerService.getExpenseOwnerEntityByUsername(username);
        Location location = locationService.getOrSaveLocationEntity(dto.getLocationData());

        existing.setOwner(owner)
                .setAmount(dto.getAmount())
                .setTimestamp(parse(dto.getTimestamp()))
                .setTimezone(parse(dto.getTimestamp()).getZone().getId())
                .setSource(dto.getSource())
                .setMerchant(dto.getMerchant())
                .setLocation(location)
                .setReceiptData(dto.getReceiptData());

        if (dto.getShared() != null) {
            existing.setShared(dto.getShared());
        }
        if (dto.getOutlier() != null) {
            existing.setOutlier(dto.getOutlier());
        }
        LOGGER.info("Updating expense entry: {}", existing);
        return expenseEntryRepository.save(existing);
    }

    public void deleteExpense(final Integer id) {
        expenseEntryRepository.deleteById(id);
    }
}
