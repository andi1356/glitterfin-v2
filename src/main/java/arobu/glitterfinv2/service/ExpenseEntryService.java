package arobu.glitterfinv2.service;

import arobu.glitterfinv2.model.dto.ExpenseEntryApiPostDTO;
import arobu.glitterfinv2.model.dto.LocationData;
import arobu.glitterfinv2.model.entity.ExpenseEntry;
import arobu.glitterfinv2.model.entity.ExpenseOwner;
import arobu.glitterfinv2.model.entity.Location;
import arobu.glitterfinv2.model.form.ExpenseEntryForm;
import arobu.glitterfinv2.model.mapper.ExpenseEntryMapper;
import arobu.glitterfinv2.model.repository.ExpenseEntryRepository;
import arobu.glitterfinv2.service.exception.OwnerNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.nonNull;

@Service
public class ExpenseEntryService {

    @Value("${receipt-folder-path}")
    private String RECEIPT_FOLDER_PATH;

    Logger LOGGER = LogManager.getLogger(ExpenseEntryService.class);

    private final ExpenseEntryRepository expenseEntryRepository;
    private final ExpenseOwnerService expenseOwnerService;
    private final LocationService locationService;
    private final ExpenseRulesetService expenseRulesetService;

    public ExpenseEntryService(ExpenseEntryRepository expenseEntryRepository, ExpenseOwnerService expenseOwnerService, LocationService locationService, ExpenseRulesetService expenseRulesetService) {
        this.expenseEntryRepository = expenseEntryRepository;
        this.expenseOwnerService = expenseOwnerService;
        this.locationService = locationService;
        this.expenseRulesetService = expenseRulesetService;
    }

    public ExpenseEntry saveExpense(final ExpenseEntryApiPostDTO expenseEntryApiPostDTO, final String username) throws OwnerNotFoundException {
        ExpenseOwner owner = expenseOwnerService.getExpenseOwnerEntityByUsername(username);
        Location location = locationService.getOrSaveLocationEntity(expenseEntryApiPostDTO.getLocationData());

        ExpenseEntry entity = ExpenseEntryMapper.toEntity(expenseEntryApiPostDTO, owner, location);

        decodeReceiptDataIfPresent(entity);

        ExpenseEntry savedEntity = expenseEntryRepository.save(entity);

        ExpenseEntry enrichedExpense = expenseRulesetService.applyRulesets(savedEntity);

        return expenseEntryRepository.save(enrichedExpense);
    }

    private void decodeReceiptDataIfPresent(ExpenseEntry entity) {
        if (nonNull(entity.getReceiptData())) {
            byte[] data = Base64.getDecoder().decode(entity.getReceiptData());
            try {
                String filename = entity.getTimestamp().toString() + ".jpg";
                Files.write(Path.of(RECEIPT_FOLDER_PATH, filename), data);
                entity.setReceiptData(filename);
            } catch (IOException e) {
                LOGGER.error("Receipt file not saved for expense entry with timestamp: {} due to error {}",
                        entity.getTimestamp(), e);
            }
        }
    }

    public Optional<ExpenseEntry> createExpense(final String username, final ExpenseEntryForm form) {
        if (form == null) {
            LOGGER.warn("Attempted to create expense for user {} with empty form", username);
            return Optional.empty();
        }
        ExpenseEntry newExpense = new ExpenseEntry();

        ExpenseOwner owner  = expenseOwnerService.getExpenseOwnerEntityByUsername(username);
        if (nonNull(form.getLatitude()) && nonNull(form.getLongitude())) {
            newExpense.setLocation(locationService.getOrSaveLocationEntity(
                    new LocationData(form.getLatitude().toString(), form.getLongitude().toString())));
        } else {
            newExpense.setLocation(locationService.getEmptyLocation());
        }

        newExpense
                .setOwner(owner)
                .setDescription(form.getDescription())
                .setCategory(form.getCategory())
                .setMerchant(form.getMerchant())
                .setAmount(form.getAmount())
                .setSource(form.getSource())
                .setReceiptData(form.getReceiptData())
                .setDetails(form.getDetails())
                .setShared(Boolean.TRUE.equals(form.getShared()))
                .setOutlier(Boolean.TRUE.equals(form.getOutlier()));

        applyTimestampUpdates(newExpense, form);

        if (newExpense.getAmount() == null
                || newExpense.getTimestamp() == null
                || newExpense.getTimezone() == null
                || newExpense.getSource() == null || newExpense.getSource().isBlank()
                || newExpense.getMerchant() == null || newExpense.getMerchant().isBlank()) {
            LOGGER.warn("Missing required fields when creating expense for user {}", username);
            return Optional.empty();
        }

        LOGGER.info("Creating expense for user {}", username);
        return Optional.of(expenseEntryRepository.save(newExpense));
    }

    public List<ExpenseEntry> getExpenses(final String username) {
        LOGGER.info("Fetching all expenses for user: {}", username);
        return expenseEntryRepository.findAllByOwner_Username(username);
    }

    public Optional<ExpenseEntry> getExpense(final Integer expenseId, final String username) {
        LOGGER.info("Fetching expense {} for user {}", expenseId, username);
        return expenseEntryRepository.findByIdAndOwner_Username(expenseId, username);
    }

    public Optional<ExpenseEntry> updateExpense(final Integer expenseId, final String username, final ExpenseEntryForm form) {
        if (form == null) {
            LOGGER.warn("Attempted to update expense {} for user {} with empty form", expenseId, username);
            return Optional.empty();
        }

        return getExpense(expenseId, username).map(expenseEntry -> {
            expenseEntry.setDescription(form.getDescription());
            expenseEntry.setCategory(form.getCategory());
            expenseEntry.setMerchant(form.getMerchant());
            expenseEntry.setAmount(form.getAmount() != null ? form.getAmount() : null);
            expenseEntry.setSource(form.getSource());
            expenseEntry.setReceiptData(form.getReceiptData());
            expenseEntry.setDetails(form.getDetails());
            if (!Objects.equals(form.getLatitude(), expenseEntry.getLocation().getLatitude()) ||
                    !Objects.equals(form.getLongitude(), expenseEntry.getLocation().getLongitude())) {
                expenseEntry.setLocation(locationService.getOrSaveLocationEntity(
                        new LocationData(form.getLatitude().toString(), form.getLongitude().toString())));
            }

            if (form.getShared() != null) {
                expenseEntry.setShared(form.getShared());
            }

            if (form.getOutlier() != null) {
                expenseEntry.setOutlier(form.getOutlier());
            }

            applyTimestampUpdates(expenseEntry, form);

            LOGGER.info("Updating expense {} for user {}", expenseId, username);
            return expenseEntryRepository.save(expenseEntry);
        });
    }

    public boolean deleteExpense(final Integer expenseId, final String username) {
        Optional<ExpenseEntry> expenseEntry = getExpense(expenseId, username);

        if (expenseEntry.isEmpty()) {
            LOGGER.warn("Attempted to delete missing expense {} for user {}", expenseId, username);
            return false;
        }

        LOGGER.info("Deleting expense {} for user {}", expenseId, username);
        expenseEntryRepository.delete(expenseEntry.get());
        return true;
    }

    private void applyTimestampUpdates(ExpenseEntry expenseEntry, ExpenseEntryForm form) {
        String timestampValue = form.getTimestamp();
        String timezoneValue = form.getTimezone();

        ZoneId targetZone = resolveTargetZone(expenseEntry, timezoneValue);

        if (timestampValue != null && !timestampValue.isBlank()) {
            try {
                LocalDateTime localDateTime = LocalDateTime.parse(timestampValue);
                ZonedDateTime updatedTimestamp = localDateTime.atZone(targetZone);
                expenseEntry.setTimestamp(updatedTimestamp);
                expenseEntry.setTimezone(updatedTimestamp.getZone().getId());
                return;
            } catch (DateTimeParseException e) {
                LOGGER.warn("Failed to parse timestamp '{}' for expense {}", timestampValue, expenseEntry.getId(), e);
            }
        }

        if (timezoneValue != null && !timezoneValue.isBlank()) {
            if (expenseEntry.getTimestamp() != null) {
                expenseEntry.setTimestamp(expenseEntry.getTimestamp().withZoneSameInstant(targetZone));
            }
            expenseEntry.setTimezone(targetZone.getId());
        }
    }

    private ZoneId resolveTargetZone(ExpenseEntry expenseEntry, String requestedZone) {
        if (requestedZone != null && !requestedZone.isBlank()) {
            try {
                return ZoneId.of(requestedZone);
            } catch (DateTimeException e) {
                LOGGER.warn("Invalid timezone '{}' provided for expense {}", requestedZone, expenseEntry.getId(), e);
            }
        }

        if (expenseEntry.getTimezone() != null && !expenseEntry.getTimezone().isBlank()) {
            try {
                return ZoneId.of(expenseEntry.getTimezone());
            } catch (DateTimeException e) {
                LOGGER.warn("Stored timezone '{}' for expense {} is invalid", expenseEntry.getTimezone(), expenseEntry.getId(), e);
            }
        }

        return expenseEntry.getTimestamp() != null
                ? expenseEntry.getTimestamp().getZone()
                : ZoneId.systemDefault();
    }
}
