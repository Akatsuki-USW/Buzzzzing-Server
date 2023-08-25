package bokjak.bokjakserver.domain.notification.repository;

import bokjak.bokjakserver.domain.notification.model.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationRepositoryCustom {
    List<Notification> findLimitCountAndSortByRecent(Long userId);
    Optional<Notification> findByNotificationIdAndUserId(Long notificationId, Long userId);
}
