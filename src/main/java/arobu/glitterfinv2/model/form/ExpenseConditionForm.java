package arobu.glitterfinv2.model.form;

import arobu.glitterfinv2.model.entity.ExpenseCondition;
import arobu.glitterfinv2.model.entity.meta.ExpenseField;
import arobu.glitterfinv2.model.entity.meta.Predicate;

public class ExpenseConditionForm {

    private ExpenseField expenseField;
    private Predicate predicate;
    private String value;

    public ExpenseField getExpenseField() {
        return expenseField;
    }

    public ExpenseConditionForm setExpenseField(ExpenseField expenseField) {
        this.expenseField = expenseField;
        return this;
    }

    public Predicate getPredicate() {
        return predicate;
    }

    public ExpenseConditionForm setPredicate(Predicate predicate) {
        this.predicate = predicate;
        return this;
    }

    public String getValue() {
        return value;
    }

    public ExpenseConditionForm setValue(String value) {
        this.value = value;
        return this;
    }

    public static ExpenseConditionForm fromEntity(ExpenseCondition condition) {
        return new ExpenseConditionForm()
                .setExpenseField(condition.getExpenseField())
                .setPredicate(condition.getPredicate())
                .setValue(condition.getValue());
    }
}
