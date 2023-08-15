package bokjak.bokjakserver.domain.user.repository;

import bokjak.bokjakserver.domain.user.model.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlackListRepository extends JpaRepository<BlackList, Long> {
}
