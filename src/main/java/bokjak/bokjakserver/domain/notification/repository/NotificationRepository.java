package bokjak.bokjakserver.domain.notification.repository;

import bokjak.bokjakserver.domain.notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long>, NotificationRepositoryCustom {

}
