package arobu.glitterfinv2.service;

import arobu.glitterfinv2.model.entity.ExpenseCondition;
import arobu.glitterfinv2.model.entity.meta.Predicate;
import arobu.glitterfinv2.model.form.ExpenseConditionForm;
import arobu.glitterfinv2.model.repository.ExpenseConditionRepository;
import arobu.glitterfinv2.model.repository.ExpenseRuleRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public List<ExpenseCondition> getConditions() {
        Sort sort = Sort.by(
                Sort.Order.asc("expenseField"),
                Sort.Order.asc("predicate"),
                Sort.Order.asc("id")
        );
        return conditionRepository.findAll(sort);
    }

    public Optional<ExpenseCondition> getCondition(Integer id) {
        return conditionRepository.findById(id);
    }

    public void createCondition(ExpenseConditionForm form) {
        ExpenseCondition condition = new ExpenseCondition()
                .setExpenseField(form.getExpenseField())
                .setPredicate(form.getPredicate())
                .setValue(normalize(form.getPredicate(), form.getValue()));
        conditionRepository.save(condition);
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
