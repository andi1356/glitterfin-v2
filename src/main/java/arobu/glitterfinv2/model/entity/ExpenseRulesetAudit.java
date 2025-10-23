package arobu.glitterfinv2.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

import java.time.ZonedDateTime;
import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class ExpenseRulesetAudit {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    @OneToOne
    private ExpenseCondition condition;
    @OneToOne
    private ExpenseRule rule;
    private ZonedDateTime appliedAt;

    public ExpenseRulesetAudit() {
    }

    public ExpenseRulesetAudit(ExpenseCondition condition, ExpenseRule rule) {
        this.condition = condition;
        this.rule = rule;
        this.appliedAt = ZonedDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public ExpenseCondition getCondition() {
        return condition;
    }

    public ExpenseRulesetAudit setCondition(ExpenseCondition condition) {
        this.condition = condition;
        return this;
    }

    public ExpenseRule getRule() {
        return rule;
    }

    public ExpenseRulesetAudit setRule(ExpenseRule rule) {
        this.rule = rule;
        return this;
    }

    public ZonedDateTime getAppliedAt() {
        return appliedAt;
    }

    public ExpenseRulesetAudit setAppliedAt(ZonedDateTime appliedAt) {
        this.appliedAt = appliedAt;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ExpenseRulesetAudit that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(condition, that.condition) && Objects.equals(rule, that.rule) && Objects.equals(appliedAt, that.appliedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, condition, rule, appliedAt);
    }

    @Override
    public String toString() {
        return "ExpenseRulesetAudit{" +
                "id=" + id +
                ", condition=" + condition +
                ", rule=" + rule +
                ", appliedAt=" + appliedAt +
                '}';
    }
}
