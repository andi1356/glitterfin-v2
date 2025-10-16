package arobu.glitterfinv2.model.entity;

import arobu.glitterfinv2.model.entity.meta.ExpenseField;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Objects;

@Entity
public class ExpenseRuleset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ExpenseCondition condition;

    @Enumerated(EnumType.STRING)
    private ExpenseField populatingField;

    private String value;

    public Integer getId() {
        return id;
    }

    public ExpenseRuleset setId(Integer id) {
        this.id = id;
        return this;
    }

    public ExpenseCondition getCondition() {
        return condition;
    }

    public ExpenseRuleset setCondition(ExpenseCondition condition) {
        this.condition = condition;
        return this;
    }

    public ExpenseField getPopulatingField() {
        return populatingField;
    }

    public ExpenseRuleset setPopulatingField(ExpenseField populatingField) {
        this.populatingField = populatingField;
        return this;
    }

    public String getValue() {
        return value;
    }

    public ExpenseRuleset setValue(String value) {
        this.value = value;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ExpenseRuleset that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(condition, that.condition) && populatingField == that.populatingField && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, condition, populatingField, value);
    }
}
