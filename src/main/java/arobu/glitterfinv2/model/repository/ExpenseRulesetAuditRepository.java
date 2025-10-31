package arobu.glitterfinv2.model.repository;

import arobu.glitterfinv2.model.entity.ExpenseRulesetAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRulesetAuditRepository extends JpaRepository<ExpenseRulesetAudit, Integer> {
}
