package arobu.glitterfinv2.model.repository;

import arobu.glitterfinv2.model.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {
    Optional<Location> findLocationByGeocodePlaceId(Integer geocodePlaceId);
}
