package arobu.glitterfinv2.service;

import arobu.glitterfinv2.model.entity.ExpenseCondition;
import arobu.glitterfinv2.model.entity.ExpenseRule;
import arobu.glitterfinv2.model.entity.Owner;
import arobu.glitterfinv2.model.form.ExpenseRuleForm;
import arobu.glitterfinv2.model.repository.ExpenseConditionRepository;
import arobu.glitterfinv2.model.repository.ExpenseRuleRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    public List<ExpenseRule> getRules(Owner owner) {
        Sort sort = Sort.by(
                Sort.Order.asc("condition"),
                Sort.Order.asc("priority")
        );
        return ruleRepository.findAllByOwner(owner, sort);
    }

    public Optional<ExpenseRule> getRule(@AuthenticationPrincipal Owner owner,
                                         Integer id) {
        return ruleRepository.findByIdAndOwner(id, owner);
    }

    public String createRule(Owner owner, ExpenseRuleForm form) {
        Optional<ExpenseCondition> condition = conditionRepository.findByIdAndOwner(form.getConditionId(), owner);
        if (condition.isEmpty()) {
            return "Unable to create the rule. Condition was null";
        } else {
            Optional<ExpenseRule> existingDuplicateExpenseRule = ruleRepository
                    .findByConditionAndPopulatingFieldAndPriority(condition.get(), form.getPopulatingField(), form.getPriority());
            if (existingDuplicateExpenseRule.isPresent()) {
                final var expenseRule = existingDuplicateExpenseRule.get();
                return join("Rule uniqueness restriction hit. Check fields; ",
                                    " Condition: ", expenseRule.getCondition().prettyPrint(),
                                    ", Populating Field: ", expenseRule.getPopulatingField().toString(),
                                    ", Priority: ", expenseRule.getPriority().toString());
            } else {
                final var rule = new ExpenseRule()
                        .setOwner(owner)
                        .setCondition(condition.get())
                        .setPopulatingField(form.getPopulatingField())
                        .setValue(normalize(form.getValue()))
                        .setPriority(form.getPriority());
                ruleRepository.save(rule);
                return "Expense Rule created.";
            }
        }
    }

    public Optional<ExpenseRule> updateRule(Integer id, Owner owner, ExpenseRuleForm form) {
        if (form.getConditionId() == null) {
            return Optional.empty();
        }
        return ruleRepository.findByIdAndOwner(id, owner)
                .flatMap(existing -> conditionRepository.findByIdAndOwner(form.getConditionId(), owner)
                        .map(condition -> {
                            existing
                                    .setCondition(condition)
                                    .setPopulatingField(form.getPopulatingField())
                                    .setValue(normalize(form.getValue()))
                                    .setPriority(form.getPriority());
                            return ruleRepository.save(existing);
                        }));
    }

    public boolean deleteRule(Integer id, Owner owner) {
        if (!ruleRepository.existsByIdAndOwner(id, owner)) {
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
