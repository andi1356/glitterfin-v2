package arobu.glitterfinv2.model.repository;

import arobu.glitterfinv2.model.entity.ExpenseCondition;
import arobu.glitterfinv2.model.entity.ExpenseOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseConditionRepository extends JpaRepository<ExpenseCondition, Integer> {
    List<ExpenseCondition> findAllByOwner(ExpenseOwner owner);
    List<ExpenseCondition> findAllByOwner_Username(String ownerUsername);
    List<ExpenseCondition> findAllByOwner_UsernameOrderByExpenseFieldPredicate(String ownerUsername);
}
