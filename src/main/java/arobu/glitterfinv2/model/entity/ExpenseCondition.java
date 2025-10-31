package arobu.glitterfinv2.model.entity;

import arobu.glitterfinv2.model.entity.meta.ExpenseField;
import arobu.glitterfinv2.model.entity.meta.Predicate;
import jakarta.persistence.*;

import java.util.Objects;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class ExpenseCondition {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    Integer id;
    @ManyToOne
    private ExpenseOwner owner;
    @Enumerated(STRING)
    ExpenseField expenseField;
    @Enumerated(STRING)
    Predicate predicate;
    String value;

    public Integer getId() {
        return id;
    }

    public ExpenseOwner getOwner() {
        return owner;
    }

    public ExpenseCondition setOwner(ExpenseOwner owner) {
        this.owner = owner;
        return this;
    }

    public ExpenseField getExpenseField() {
        return expenseField;
    }

    public ExpenseCondition setExpenseField(ExpenseField expenseField) {
        this.expenseField = expenseField;
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
        return Objects.equals(id, that.id) && Objects.equals(owner, that.owner) && expenseField == that.expenseField && predicate == that.predicate && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, owner, expenseField, predicate, value);
    }

    @Override
    public String toString() {
        return "ExpenseCondition{" +
                "id=" + id +
                ", owner=" + owner +
                ", expenseField=" + expenseField +
                ", predicate=" + predicate +
                ", value='" + value + '\'' +
                '}';
    }
}
