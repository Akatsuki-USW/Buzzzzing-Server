package bokjak.bokjakserver.domain.location.repository;

import bokjak.bokjakserver.domain.location.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long>, LocationRepositoryCustom {
}
