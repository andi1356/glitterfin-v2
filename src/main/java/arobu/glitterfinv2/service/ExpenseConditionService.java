package arobu.glitterfinv2.service;

import arobu.glitterfinv2.model.entity.ExpenseCondition;
import arobu.glitterfinv2.model.entity.Owner;
import arobu.glitterfinv2.model.entity.meta.Predicate;
import arobu.glitterfinv2.model.form.ExpenseConditionForm;
import arobu.glitterfinv2.model.repository.ExpenseConditionRepository;
import arobu.glitterfinv2.model.repository.ExpenseRuleRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static arobu.glitterfinv2.model.entity.meta.Predicate.REGEX;
import static java.util.Objects.nonNull;

@Service
public class ExpenseConditionService {

    private final ExpenseConditionRepository conditionRepository;
    private final ExpenseRuleRepository expenseRuleRepository;

    public ExpenseConditionService(ExpenseConditionRepository conditionRepository, ExpenseRuleRepository expenseRuleRepository) {
        this.conditionRepository = conditionRepository;
        this.expenseRuleRepository = expenseRuleRepository;
    }

    public List<ExpenseCondition> getConditions(Owner owner) {
        Sort sort = Sort.by(
                Sort.Order.asc("expenseField"),
                Sort.Order.asc("predicate")
        );
        return conditionRepository.findAllByOwner(owner, sort);
    }

    public Optional<ExpenseCondition> getCondition(Integer id, Owner owner) {
        return conditionRepository.findByIdAndOwner(id, owner);
    }

    public String createCondition(Owner owner, ExpenseConditionForm form) {

        ExpenseCondition condition = new ExpenseCondition()
                .setOwner(owner)
                .setExpenseField(form.getExpenseField())
                .setPredicate(form.getPredicate())
                .setValue(normalize(form.getPredicate(), form.getValue()));
        if (condition.getPredicate()==REGEX) {
            try {
                Pattern.compile(condition.getValue());
            } catch (PatternSyntaxException e) {
                return "Regex invalid: " + e.getDescription();
            }
        }
        conditionRepository.save(condition);
        return "Condition created successfully.";
    }

    public Optional<ExpenseCondition> updateCondition(Integer id, Owner owner, ExpenseConditionForm form) {
        return conditionRepository.findByIdAndOwner(id, owner)
                .map(existing -> {
                    existing
                            .setExpenseField(form.getExpenseField())
                            .setPredicate(form.getPredicate())
                            .setValue(normalize(form.getPredicate(), form.getValue()));
                    return conditionRepository.save(existing);
                });
    }

    public boolean deleteCondition(Owner owner, Integer id) {
        if (!conditionRepository.existsByIdAndOwner(id, owner)) {
            return false;
        }
        expenseRuleRepository.findAllByConditionIdIn(List.of(id))
                        .forEach(expenseRule -> expenseRule.setCondition(null));
        conditionRepository.deleteById(id);
        return true;
    }

    private String normalize(Predicate predicate, String value) {
        if (value == null) {
            return "";
        }
        String trimmed = value.trim();
        if (nonNull(predicate) && predicate != REGEX) {
            return trimmed.toLowerCase();
        }
        return trimmed;
    }
}
