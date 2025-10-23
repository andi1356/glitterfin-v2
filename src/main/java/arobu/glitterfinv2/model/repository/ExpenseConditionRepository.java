package arobu.glitterfinv2.model.repository;

import arobu.glitterfinv2.model.entity.ExpenseCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseConditionRepository extends JpaRepository<ExpenseCondition, Integer> {
}
