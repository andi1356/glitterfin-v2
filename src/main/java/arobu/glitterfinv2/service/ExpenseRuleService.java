package arobu.glitterfinv2.service;

import arobu.glitterfinv2.model.dto.ExpenseRuleForm;
import arobu.glitterfinv2.model.entity.ExpenseRule;
import arobu.glitterfinv2.model.repository.ExpenseConditionRepository;
import arobu.glitterfinv2.model.repository.ExpenseRuleRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
                Sort.Order.asc("priority").with(Sort.NullHandling.NULLS_LAST),
                Sort.Order.asc("id")
        );
        return ruleRepository.findAll(sort);
    }

    public Optional<ExpenseRule> getRule(Integer id) {
        return ruleRepository.findById(id);
    }

    public Optional<ExpenseRule> createRule(ExpenseRuleForm form) {
        if (form.getConditionId() == null) {
            return Optional.empty();
        }
        return conditionRepository.findById(form.getConditionId())
                .map(condition -> {
                    ExpenseRule rule = new ExpenseRule()
                            .setCondition(condition)
                            .setPopulatingField(form.getPopulatingField())
                            .setValue(normalize(form.getValue()))
                            .setPriority(form.getPriority());
                    return ruleRepository.save(rule);
                });
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
