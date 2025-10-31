package arobu.glitterfinv2.service;

import arobu.glitterfinv2.model.entity.ExpenseCondition;
import arobu.glitterfinv2.model.entity.meta.Predicate;
import arobu.glitterfinv2.model.form.ExpenseConditionForm;
import arobu.glitterfinv2.model.repository.ExpenseConditionRepository;
import arobu.glitterfinv2.model.repository.ExpenseRuleRepository;
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
    private final ExpenseRuleRepository ruleRepository;

    public ExpenseConditionService(ExpenseConditionRepository conditionRepository,
                                   ExpenseRuleRepository ruleRepository) {
        this.conditionRepository = conditionRepository;
        this.ruleRepository = ruleRepository;
    }

    public List<ExpenseCondition> getConditions(String ownerId) {
        return conditionRepository.findAllByOwner_UsernameOrderByExpenseFieldAscPredicateAsc(ownerId);
    }

    public Optional<ExpenseCondition> getCondition(Integer id) {
        return conditionRepository.findById(id);
    }

    public String createCondition(String ownerId, ExpenseConditionForm form) {

        ExpenseCondition condition = new ExpenseCondition()
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

    public Optional<ExpenseCondition> updateCondition(Integer id, ExpenseConditionForm form) {
        return conditionRepository.findById(id)
                .map(existing -> {
                    existing
                            .setExpenseField(form.getExpenseField())
                            .setPredicate(form.getPredicate())
                            .setValue(normalize(form.getPredicate(), form.getValue()));
                    return conditionRepository.save(existing);
                });
    }

    public boolean deleteCondition(Integer id) {
        if (!conditionRepository.existsById(id)) {
            return false;
        }
        if (ruleRepository.existsByConditionId(id)) {
            return false;
        }
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
