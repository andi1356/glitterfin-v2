package arobu.glitterfinv2.model.repository;

import arobu.glitterfinv2.model.entity.ExpenseEntry;
import arobu.glitterfinv2.model.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseEntryRepository extends JpaRepository<ExpenseEntry, Integer> {
    List<ExpenseEntry> findAllByOwnerOrderByTimestampDesc(Owner owner);

    Optional<ExpenseEntry> findByIdAndOwner(Integer id, Owner owner);

    boolean existsByIdAndOwner(Integer id, Owner owner);
}
