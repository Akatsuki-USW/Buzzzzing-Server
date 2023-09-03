package bokjak.bokjakserver.domain.user.repository;

import bokjak.bokjakserver.domain.user.model.RevokeUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RevokeUserRepository extends JpaRepository<RevokeUser, Long> {

    Optional<RevokeUser> findBySocialEmail(String socialEmail);

}
