package bokjak.bokjakserver.domain.notification.service;

import bokjak.bokjakserver.common.exception.StatusCode;
import bokjak.bokjakserver.domain.notification.dto.NotificationDto.NotificationListResponse;
import bokjak.bokjakserver.domain.notification.dto.NotificationDto.NotificationResponse;
import bokjak.bokjakserver.domain.notification.dto.NotificationDto.NotifyParams;
import bokjak.bokjakserver.domain.notification.exception.NotificationException;
import bokjak.bokjakserver.domain.notification.model.Notification;
import bokjak.bokjakserver.domain.notification.repository.NotificationRepository;
import bokjak.bokjakserver.domain.user.model.User;
import bokjak.bokjakserver.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class NotificationService {

    private final FcmService fcmService;
    private final NotificationRepository notificationRepository;
    private final UserService userService;

    @Transactional
    public void pushMessage(NotifyParams params) {
        User receiver = userService.getUser(params.receiver().getId());

        fcmService.sendPushMessage(receiver.getFcmToken(), params);
        Notification notification = Notification.builder()
                .user(receiver)
                .title(params.title())
                .content(params.content())
                .isRead(false)
                .type(params.type())
                .redirectTargetId(params.redirectTargetId())
                .build();

        notificationRepository.save(notification);

        receiver.addNotification(notification);
    }

    public NotificationListResponse getMyNotifications(Long userId) {
        userService.getUser(userId);

        List<Notification> limitCountAndSortByRecent = notificationRepository.findLimitCountAndSortByRecent(userId);
        return NotificationListResponse.of(limitCountAndSortByRecent.stream()
                .map(NotificationResponse::of)
                .toList());
    }

    @Transactional
    public NotificationResponse readNotification(Long notificationId, Long userId) {
        userService.getUser(userId);

        Optional<Notification> notificationOptional = notificationRepository.findByNotificationIdAndUserId(notificationId, userId);
        if (notificationOptional.isEmpty()) throw new NotificationException(StatusCode.NOT_FOUND_NOTIFICATION);

        notificationOptional.get().read();

        return NotificationResponse.of(notificationOptional.get());
    }

}
