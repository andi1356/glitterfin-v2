package arobu.glitterfinv2.model.entity;

import arobu.glitterfinv2.model.entity.meta.ExpenseField;
import arobu.glitterfinv2.model.entity.meta.Predicate;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Objects;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class ExpenseCondition {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    Integer id;
    @Enumerated(STRING)
    ExpenseField expenseField;
    @Enumerated(STRING)
    Predicate predicate;
    String value;

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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ExpenseCondition that)) return false;
        return Objects.equals(id, that.id) && expenseField == that.expenseField && predicate == that.predicate && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, expenseField, predicate, value);
    }

    @Override
    public String toString() {
        return "ExpenseCondition{" +
                "id=" + id +
                ", expenseField=" + expenseField +
                ", predicate=" + predicate +
                ", value='" + value + '\'' +
                '}';
    }
}
