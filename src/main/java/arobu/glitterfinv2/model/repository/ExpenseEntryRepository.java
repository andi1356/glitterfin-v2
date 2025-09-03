package arobu.glitterfinv2.model.repository;

import arobu.glitterfinv2.model.entity.ExpenseEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseEntryRepository extends JpaRepository<ExpenseEntry, Integer> {

    List<ExpenseEntry> findAllByOwner_Username(String ownerUsername);
}
