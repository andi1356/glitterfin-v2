package arobu.glitterfinv2.model.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.springframework.data.annotation.CreatedDate;

import java.time.ZonedDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

public class ExpenseRulesetAudit {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    Integer id;

    ExpenseEntry expense;
    ExpenseCondition condition;
    ExpenseRuleset ruleset;

    @CreatedDate
    ZonedDateTime timestamp;
}
