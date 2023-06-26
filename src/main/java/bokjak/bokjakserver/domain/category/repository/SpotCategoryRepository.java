package bokjak.bokjakserver.domain.category.repository;

import bokjak.bokjakserver.domain.category.model.SpotCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpotCategoryRepository extends JpaRepository<SpotCategory, Long> {
}
