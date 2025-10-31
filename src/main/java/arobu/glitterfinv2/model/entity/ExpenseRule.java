package arobu.glitterfinv2.model.entity;

import arobu.glitterfinv2.model.entity.meta.ExpenseRulesetUpdatableField;
import jakarta.persistence.*;

import java.util.Objects;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class ExpenseRule {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    @ManyToOne
    private Owner owner;
    @ManyToOne
    private ExpenseCondition condition;
    @Enumerated(STRING)
    private ExpenseRulesetUpdatableField populatingField;
    private String value;
    private Integer priority;

    public Integer getId() {
        return id;
    }

    public Owner getOwner() {
        return owner;
    }

    public ExpenseRule setOwner(Owner owner) {
        this.owner = owner;
        return this;
    }

    public ExpenseCondition getCondition() {
        return condition;
    }

    public ExpenseRule setCondition(ExpenseCondition condition) {
        this.condition = condition;
        return this;
    }

    public ExpenseRulesetUpdatableField getPopulatingField() {
        return populatingField;
    }

    public ExpenseRule setPopulatingField(ExpenseRulesetUpdatableField populatingField) {
        this.populatingField = populatingField;
        return this;
    }

    public String getValue() {
        return value;
    }

    public ExpenseRule setValue(String value) {
        this.value = value;
        return this;
    }

    public Integer getPriority() {
        return priority;
    }

    public ExpenseRule setPriority(Integer priority) {
        this.priority = priority;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ExpenseRule that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(owner, that.owner) && Objects.equals(condition, that.condition) && populatingField == that.populatingField && Objects.equals(value, that.value) && Objects.equals(priority, that.priority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, owner, condition, populatingField, value, priority);
    }

    @Override
    public String toString() {
        return "ExpenseRule{" +
                "id=" + id +
                ", owner=" + owner +
                ", condition=" + condition +
                ", populatingField=" + populatingField +
                ", value='" + value + '\'' +
                ", priority=" + priority +
                '}';
    }
}
