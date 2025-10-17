package arobu.glitterfinv2.service;

import arobu.glitterfinv2.model.entity.ExpenseCondition;
import arobu.glitterfinv2.model.entity.ExpenseEntry;
import arobu.glitterfinv2.model.entity.ExpenseRuleset;
import arobu.glitterfinv2.model.entity.Location;
import arobu.glitterfinv2.model.entity.ExpenseOwner;
import arobu.glitterfinv2.model.entity.meta.ExpenseField;
import arobu.glitterfinv2.model.entity.meta.Predicate;
import arobu.glitterfinv2.model.repository.ExpenseRulesetRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class ExpenseRulesetService {

    private static final Logger LOGGER = LogManager.getLogger(ExpenseRulesetService.class);

    private final ExpenseRulesetRepository expenseRulesetRepository;

    public ExpenseRulesetService(ExpenseRulesetRepository expenseRulesetRepository) {
        this.expenseRulesetRepository = expenseRulesetRepository;
    }

    public Mono<ExpenseEntry> applyRules(ExpenseEntry expenseEntry) {
        if (expenseEntry == null) {
            return Mono.empty();
        }

        return Flux.defer(() -> Flux.fromIterable(getRulesets()))
                .filter(ruleset -> matchesCondition(expenseEntry, ruleset.getCondition()))
                .doOnNext(ruleset -> applyRule(expenseEntry, ruleset))
                .then(Mono.just(expenseEntry));
    }

    private List<ExpenseRuleset> getRulesets() {
        List<ExpenseRuleset> rulesets = expenseRulesetRepository.findAll();
        return rulesets != null ? rulesets : List.of();
    }

    private boolean matchesCondition(ExpenseEntry expenseEntry, ExpenseCondition condition) {
        if (condition == null || condition.getExpenseField() == null || condition.getPredicate() == null) {
            return false;
        }

        Optional<String> candidateValue = extractFieldValue(expenseEntry, condition.getExpenseField());
        if (candidateValue.isEmpty() || condition.getValue() == null) {
            return false;
        }

        String value = candidateValue.get().trim();
        String conditionValue = condition.getValue().trim();

        if (value.isEmpty() || conditionValue.isEmpty()) {
            return false;
        }

        String normalizedValue = value.toLowerCase(Locale.ENGLISH);
        String normalizedCondition = conditionValue.toLowerCase(Locale.ENGLISH);

        Predicate predicate = condition.getPredicate();
        return switch (predicate) {
            case IS -> normalizedValue.equals(normalizedCondition);
            case CONTAINS -> normalizedValue.contains(normalizedCondition);
            case STARTS_WITH -> normalizedValue.startsWith(normalizedCondition);
            case ENDS_WITH -> normalizedValue.endsWith(normalizedCondition);
        };
    }

    private Optional<String> extractFieldValue(ExpenseEntry expenseEntry, ExpenseField field) {
        return switch (field) {
            case ID -> Optional.ofNullable(expenseEntry.getId()).map(String::valueOf);
            case OWNER -> Optional.ofNullable(expenseEntry.getOwner()).map(ExpenseOwner::getUsername);
            case AMOUNT -> Optional.ofNullable(expenseEntry.getAmount()).map(BigDecimal::toPlainString);
            case TIMESTAMP -> Optional.ofNullable(expenseEntry.getTimestamp()).map(ZonedDateTime::toString);
            case TIMEZONE -> Optional.ofNullable(expenseEntry.getTimezone());
            case SOURCE -> Optional.ofNullable(expenseEntry.getSource());
            case MERCHANT -> Optional.ofNullable(expenseEntry.getMerchant());
            case LOCATION -> Optional.ofNullable(expenseEntry.getLocation()).map(Location::getDisplayName);
            case CATEGORY -> Optional.ofNullable(expenseEntry.getCategory());
            case RECEIPT_DATA -> Optional.ofNullable(expenseEntry.getReceiptData());
            case DESCRIPTION -> Optional.ofNullable(expenseEntry.getDescription());
            case DETAILS -> Optional.ofNullable(expenseEntry.getDetails());
            case IS_SHARED -> Optional.ofNullable(expenseEntry.getShared()).map(String::valueOf);
            case IS_OUTLIER -> Optional.ofNullable(expenseEntry.getOutlier()).map(String::valueOf);
        };
    }

    private void applyRule(ExpenseEntry expenseEntry, ExpenseRuleset ruleset) {
        ExpenseField targetField = ruleset.getPopulatingField();
        if (targetField == null) {
            LOGGER.warn("Ruleset {} is missing a target field", ruleset.getId());
            return;
        }

        String value = ruleset.getValue();
        LOGGER.debug("Applying ruleset {} to field {} with value '{}'", ruleset.getId(), targetField, value);

        switch (targetField) {
            case CATEGORY -> expenseEntry.setCategory(value);
            case SOURCE -> expenseEntry.setSource(value);
            case MERCHANT -> expenseEntry.setMerchant(value);
            case RECEIPT_DATA -> expenseEntry.setReceiptData(value);
            case DESCRIPTION -> expenseEntry.setDescription(value);
            case DETAILS -> expenseEntry.setDetails(value);
            case TIMEZONE -> {
                if (value != null && !value.isBlank()) {
                    expenseEntry.setTimezone(value);
                }
            }
            case IS_SHARED -> {
                if (value != null) {
                    expenseEntry.setShared(Boolean.parseBoolean(value));
                }
            }
            case IS_OUTLIER -> {
                if (value != null) {
                    expenseEntry.setOutlier(Boolean.parseBoolean(value));
                }
            }
            case AMOUNT -> {
                if (value == null || value.isBlank()) {
                    expenseEntry.setAmount(null);
                } else {
                    try {
                        expenseEntry.setAmount(new BigDecimal(value));
                    } catch (NumberFormatException ex) {
                        LOGGER.warn("Unable to parse amount '{}' for ruleset {}", value, ruleset.getId(), ex);
                    }
                }
            }
            case TIMESTAMP -> {
                if (value != null && !value.isBlank()) {
                    try {
                        ZonedDateTime parsedTimestamp = ZonedDateTime.parse(value);
                        expenseEntry.setTimestamp(parsedTimestamp);
                        expenseEntry.setTimezone(parsedTimestamp.getZone().getId());
                    } catch (DateTimeParseException ex) {
                        LOGGER.warn("Unable to parse timestamp '{}' for ruleset {}", value, ruleset.getId(), ex);
                    }
                }
            }
            default -> LOGGER.debug("Skipping unsupported target field {} for ruleset {}", targetField, ruleset.getId());
        }
    }
}
