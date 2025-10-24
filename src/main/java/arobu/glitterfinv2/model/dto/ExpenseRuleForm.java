package arobu.glitterfinv2.model.dto;

import arobu.glitterfinv2.model.entity.ExpenseRule;
import arobu.glitterfinv2.model.entity.meta.ExpenseRulesetUpdatableField;

public class ExpenseRuleForm {

    private Integer conditionId;
    private ExpenseRulesetUpdatableField populatingField;
    private String value;
    private Integer priority;

    public Integer getConditionId() {
        return conditionId;
    }

    public ExpenseRuleForm setConditionId(Integer conditionId) {
        this.conditionId = conditionId;
        return this;
    }

    public ExpenseRulesetUpdatableField getPopulatingField() {
        return populatingField;
    }

    public ExpenseRuleForm setPopulatingField(ExpenseRulesetUpdatableField populatingField) {
        this.populatingField = populatingField;
        return this;
    }

    public String getValue() {
        return value;
    }

    public ExpenseRuleForm setValue(String value) {
        this.value = value;
        return this;
    }

    public Integer getPriority() {
        return priority;
    }

    public ExpenseRuleForm setPriority(Integer priority) {
        this.priority = priority;
        return this;
    }

    public static ExpenseRuleForm fromEntity(ExpenseRule rule) {
        return new ExpenseRuleForm()
                .setConditionId(rule.getCondition() != null ? rule.getCondition().getId() : null)
                .setPopulatingField(rule.getPopulatingField())
                .setValue(rule.getValue())
                .setPriority(rule.getPriority());
    }
}
