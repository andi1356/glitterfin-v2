package arobu.glitterfinv2.model.repository;

import arobu.glitterfinv2.model.entity.ExpenseCondition;
import arobu.glitterfinv2.model.entity.Owner;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseConditionRepository extends JpaRepository<ExpenseCondition, Integer> {
    List<ExpenseCondition> findAllByOwner(Owner owner);
    List<ExpenseCondition> findAllByOwner(Owner owner, Sort sort);

    Optional<ExpenseCondition> findByIdAndOwner(Integer id, Owner owner);

    boolean existsByIdAndOwner(Integer id, Owner owner);
}
