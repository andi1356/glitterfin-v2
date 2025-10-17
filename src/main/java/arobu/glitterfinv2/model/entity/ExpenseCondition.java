package arobu.glitterfinv2.model.entity;

import arobu.glitterfinv2.model.entity.meta.ExpenseField;
import arobu.glitterfinv2.model.entity.meta.Predicate;
import jakarta.persistence.*;

import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class ExpenseCondition {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    Integer id;

    @Enumerated(EnumType.STRING)
    ExpenseField expenseField;

    @Enumerated(EnumType.STRING)
    Predicate predicate;

    String value;

    Integer priority;

    public Integer getId() {
        return id;
    }

    public ExpenseCondition setId(Integer id) {
        this.id = id;
        return this;
    }

    public ExpenseField getExpenseField() {
        return expenseField;
    }

    public ExpenseCondition setExpenseField(ExpenseField field) {
        this.expenseField = field;
        return this;
    }

    public Predicate getPredicate() {
        return predicate;
    }

    public ExpenseCondition setPredicate(Predicate predicate) {
        this.predicate = predicate;
        return this;
    }

    public String getValue() {
        return value;
    }

    public ExpenseCondition setValue(String value) {
        this.value = value;
        return this;
    }

    public Integer getPriority() {
        return priority;
    }

    public ExpenseCondition setPriority(Integer priority) {
        this.priority = priority;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ExpenseCondition that)) return false;
        return Objects.equals(id, that.id) && expenseField == that.expenseField && predicate == that.predicate && Objects.equals(value, that.value) && Objects.equals(priority, that.priority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, expenseField, predicate, value, priority);
    }
}
