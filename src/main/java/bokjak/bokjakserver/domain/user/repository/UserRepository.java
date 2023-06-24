package bokjak.bokjakserver.domain.user.repository;

import bokjak.bokjakserver.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByNickname(String nickname);
    Optional<User> findBySocialEmail(String socialEmail);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
}
