package arobu.glitterfinv2.service;

import arobu.glitterfinv2.model.entity.ExpenseCondition;
import arobu.glitterfinv2.model.entity.ExpenseEntry;
import arobu.glitterfinv2.model.entity.ExpenseRulesetAudit;
import arobu.glitterfinv2.model.repository.ExpenseConditionRepository;
import arobu.glitterfinv2.model.repository.ExpenseRuleRepository;
import arobu.glitterfinv2.model.repository.ExpenseRulesetAuditRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

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
                .forEach(rule -> {
                    LOG.info("Applying ruleset: {}", rule);
                    expenseCopy.set(rule.getPopulatingField(), rule.getValue());
                    auditRepository.save(new ExpenseRulesetAudit(rule.getCondition(), rule, expenseCopy));
                });

        return expenseCopy;
    }

    public boolean matches(ExpenseCondition condition, ExpenseEntry expense) {
        String expenseFieldValue = expense.get(condition.getExpenseField()).toLowerCase();
        return switch (condition.getPredicate()) {
            case IS -> expenseFieldValue.equals(condition.getValue());
            case CONTAINS -> expenseFieldValue.contains(condition.getValue());
            case STARTS_WITH -> expenseFieldValue.startsWith(condition.getValue());
            case ENDS_WITH -> expenseFieldValue.endsWith(condition.getValue());
            case REGEX -> expenseFieldValue.matches(condition.getValue());
        };
    }
}
