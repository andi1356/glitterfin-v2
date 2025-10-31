package arobu.glitterfinv2.service;

import arobu.glitterfinv2.model.entity.ExpenseCondition;
import arobu.glitterfinv2.model.entity.ExpenseRule;
import arobu.glitterfinv2.model.form.ExpenseRuleForm;
import arobu.glitterfinv2.model.repository.ExpenseConditionRepository;
import arobu.glitterfinv2.model.repository.ExpenseRuleRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.lang.String.join;

@Service
public class ExpenseRuleService {

    private final ExpenseRuleRepository ruleRepository;
    private final ExpenseConditionRepository conditionRepository;

    public ExpenseRuleService(ExpenseRuleRepository ruleRepository,
                              ExpenseConditionRepository conditionRepository) {
        this.ruleRepository = ruleRepository;
        this.conditionRepository = conditionRepository;
    }

    public List<ExpenseRule> getRules() {
        Sort sort = Sort.by(
                Sort.Order.asc("priority"),
                Sort.Order.asc("id")
        );
        return ruleRepository.findAll(sort);
    }

    public Optional<ExpenseRule> getRule(Integer id) {
        return ruleRepository.findById(id);
    }

    public String createRule(ExpenseRuleForm form) {
        conditionRepository.findById(form.getConditionId())
                .map(condition -> ruleRepository.findByConditionAndPopulatingFieldAndPriority(condition, form.getPopulatingField(), form.getPriority())
                        .map(rule ->
                                join("Rule uniqueness restriction hit. Recommendation is to increment priority.",
                                        " Condition: ", rule.getCondition().toString(),
                                        ", Populating Field: ", rule.getPopulatingField().toString(),
                                        ", Priority: ", rule.getPriority().toString())));

        Optional<ExpenseCondition> condition = conditionRepository.findById(form.getConditionId());
        if (condition.isEmpty()) {
            return "Unable to create the rule. Condition was null";
        } else {
            return ruleRepository
                    .findByConditionAndPopulatingFieldAndPriority(condition.get(), form.getPopulatingField(), form.getPriority())
                    .map(existingRule ->
                            join("Rule uniqueness restriction hit. Check fields; ",
                                    " Condition: ", existingRule.getCondition().toString(),
                                    ", Populating Field: ", existingRule.getPopulatingField().toString(),
                                    ", Priority: ", existingRule.getPriority().toString()))
                    .orElse("Expense Rule created.");
        }
    }

    public Optional<ExpenseRule> updateRule(Integer id, ExpenseRuleForm form) {
        if (form.getConditionId() == null) {
            return Optional.empty();
        }
        return ruleRepository.findById(id)
                .flatMap(existing -> conditionRepository.findById(form.getConditionId())
                        .map(condition -> {
                            existing
                                    .setCondition(condition)
                                    .setPopulatingField(form.getPopulatingField())
                                    .setValue(normalize(form.getValue()))
                                    .setPriority(form.getPriority());
                            return ruleRepository.save(existing);
                        }));
    }

    public boolean deleteRule(Integer id) {
        if (!ruleRepository.existsById(id)) {
            return false;
        }
        ruleRepository.deleteById(id);
        return true;
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        return value.trim();
    }
}
