package arobu.glitterfinv2.model.repository;

import arobu.glitterfinv2.model.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestEntityRepo extends JpaRepository<TestEntity, Long> {
}
