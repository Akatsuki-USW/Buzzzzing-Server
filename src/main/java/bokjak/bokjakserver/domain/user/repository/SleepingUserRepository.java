package bokjak.bokjakserver.domain.user.repository;

import bokjak.bokjakserver.domain.user.model.SleepingUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SleepingUserRepository extends JpaRepository<SleepingUser, Long> {

    Optional<SleepingUser> findBySocialEmail(String socialEmail);
}
