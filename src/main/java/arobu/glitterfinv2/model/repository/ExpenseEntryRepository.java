package arobu.glitterfinv2.model.repository;

import arobu.glitterfinv2.model.entity.ExpenseEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseEntryRepository extends JpaRepository<ExpenseEntry, Integer> {

    List<ExpenseEntry> findAllByOwner_Username(String ownerUsername);

    Optional<ExpenseEntry> findByIdAndOwner_Username(Integer id, String ownerUsername);
}
