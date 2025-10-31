package arobu.glitterfinv2.model.repository;

import arobu.glitterfinv2.model.entity.ExpenseCondition;
import arobu.glitterfinv2.model.entity.ExpenseRule;
import arobu.glitterfinv2.model.entity.Owner;
import arobu.glitterfinv2.model.entity.meta.ExpenseRulesetUpdatableField;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRuleRepository extends JpaRepository<ExpenseRule, Integer> {
    List<ExpenseRule> findAllByOwner(Owner owner, Sort sort);

    Optional<ExpenseRule> findByIdAndOwner(Integer id, Owner owner);

    List<ExpenseRule> findAllByConditionIdIn(Collection<Integer> conditionIds);

    Optional<ExpenseRule> findByConditionAndPopulatingFieldAndPriority(ExpenseCondition condition, ExpenseRulesetUpdatableField populatingField, Integer priority);

    boolean existsByIdAndOwner(Integer id, Owner owner);
}
