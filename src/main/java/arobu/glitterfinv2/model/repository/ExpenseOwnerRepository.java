package arobu.glitterfinv2.model.repository;

import arobu.glitterfinv2.model.entity.ExpenseOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseOwnerRepository extends JpaRepository<ExpenseOwner, String> {

}
