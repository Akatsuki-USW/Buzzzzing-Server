package bokjak.bokjakserver.domain.ban.repository;

import bokjak.bokjakserver.domain.ban.model.Ban;
import bokjak.bokjakserver.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BanRepository extends JpaRepository<Ban, Long>, BanRepositoryCustom {

    Optional<Ban> findByUserAndIsBannedIsTrue(User user);
}
