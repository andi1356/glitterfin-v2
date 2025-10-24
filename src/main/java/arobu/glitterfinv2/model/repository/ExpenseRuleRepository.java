package arobu.glitterfinv2.model.repository;

import arobu.glitterfinv2.model.entity.ExpenseRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ExpenseRuleRepository extends JpaRepository<ExpenseRule, Integer> {
    List<ExpenseRule> findAllByConditionIdIn(Collection<Integer> conditionIds);
    boolean existsByConditionId(Integer conditionId);
}
