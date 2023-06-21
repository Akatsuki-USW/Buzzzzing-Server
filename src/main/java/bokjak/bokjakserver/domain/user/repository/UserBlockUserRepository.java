package bokjak.bokjakserver.domain.user.repository;

import bokjak.bokjakserver.domain.user.model.User;
import bokjak.bokjakserver.domain.user.model.UserBlockUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserBlockUserRepository extends JpaRepository<UserBlockUser, Long> {
    boolean existsByBlockerUserAndAndBlockedUser(User blockerUser, User blockedUser);

}
