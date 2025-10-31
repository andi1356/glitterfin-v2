package arobu.glitterfinv2.service;

import arobu.glitterfinv2.model.entity.ExpenseCondition;
import arobu.glitterfinv2.model.entity.ExpenseEntry;
import arobu.glitterfinv2.model.entity.ExpenseRule;
import arobu.glitterfinv2.model.entity.ExpenseRulesetAudit;
import arobu.glitterfinv2.model.repository.ExpenseConditionRepository;
import arobu.glitterfinv2.model.repository.ExpenseRuleRepository;
import arobu.glitterfinv2.model.repository.ExpenseRulesetAuditRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

import static java.util.Objects.nonNull;

@Service
public class ExpenseRulesetService {

    private static final Logger LOG = LoggerFactory.getLogger(ExpenseRulesetService.class);

    private final ExpenseRuleRepository ruleRepository;
    private final ExpenseConditionRepository conditionRepository;
    private final ExpenseRulesetAuditRepository auditRepository;

    public ExpenseRulesetService(ExpenseRuleRepository ruleRepository, ExpenseConditionRepository conditionRepository, ExpenseRulesetAuditRepository auditRepository) {
        this.ruleRepository = ruleRepository;
        this.conditionRepository = conditionRepository;
        this.auditRepository = auditRepository;
    }

    public ExpenseEntry applyRulesets(final ExpenseEntry expense) {
        ExpenseEntry expenseCopy = expense.copy();

        List<Integer> matchingConditionIds = conditionRepository.findAll().stream()
                .filter(condition -> matches(condition, expense))
                .map(ExpenseCondition::getId)
                .toList();

        ruleRepository.findAllByConditionIdIn(matchingConditionIds)
                .stream()
                .sorted(Comparator.comparing(ExpenseRule::getPriority))
                .forEach(rule -> {
                    LOG.info("Applying ruleset: {}", rule);
                    expenseCopy.set(rule.getPopulatingField(), rule.getValue());
                    auditRepository.save(new ExpenseRulesetAudit(rule.getCondition(), rule, expenseCopy));
                });

        return expenseCopy;
    }

    public boolean matches(ExpenseCondition condition, ExpenseEntry expense) {
        final var expenseFieldValue = expense.get(condition.getExpenseField());
        if (nonNull(expenseFieldValue)) {
            final var lowerCaseFieldValue = expenseFieldValue.toString().toLowerCase();
            return switch (condition.getPredicate()) {
                case IS -> lowerCaseFieldValue.equals(condition.getValue());
                case CONTAINS -> lowerCaseFieldValue.contains(condition.getValue());
                case STARTS_WITH -> lowerCaseFieldValue.startsWith(condition.getValue());
                case ENDS_WITH -> lowerCaseFieldValue.endsWith(condition.getValue());
                case REGEX -> expenseFieldValue.toString().matches(condition.getValue());
            };
        } else {
            return false;
        }
    }
}
