package arobu.glitterfinv2.model.repository;

import arobu.glitterfinv2.model.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, String> {
    Optional<Owner> findByUsername(String username);

    Optional<Owner> getOwnerByUserAgentId(String userAgentId);
}
