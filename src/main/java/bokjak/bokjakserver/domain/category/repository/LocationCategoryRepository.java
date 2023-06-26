package bokjak.bokjakserver.domain.category.repository;

import bokjak.bokjakserver.domain.category.model.LocationCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationCategoryRepository extends JpaRepository<LocationCategory, Long> {
}
