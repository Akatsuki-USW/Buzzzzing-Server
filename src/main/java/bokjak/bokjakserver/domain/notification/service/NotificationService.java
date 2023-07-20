package bokjak.bokjakserver.domain.notification.service;

import bokjak.bokjakserver.common.exception.StatusCode;
import bokjak.bokjakserver.domain.notification.dto.NotificationDto.NotificationListResponse;
import bokjak.bokjakserver.domain.notification.dto.NotificationDto.NotificationResponse;
import bokjak.bokjakserver.domain.notification.dto.NotificationDto.NotifyParams;
import bokjak.bokjakserver.domain.notification.exception.NotificationException;
import bokjak.bokjakserver.domain.notification.model.Notification;
import bokjak.bokjakserver.domain.notification.repository.NotificationRepository;
import bokjak.bokjakserver.domain.user.model.User;
import bokjak.bokjakserver.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class NotificationService {

    private final FcmService fcmService;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    @Transactional
    public void pushMessage(NotifyParams params) {
        User receiver = userRepository.findById(params.receiver().getId())
                .orElseThrow(() -> new NotificationException(StatusCode.NOT_FOUND_USER));

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

    public NotificationListResponse getMyNotifications(User user) {
        User loginUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new NotificationException(StatusCode.NOT_FOUND_USER));

        return NotificationListResponse.of(loginUser.getNotificationList().stream()
                .map(NotificationResponse::of)
                .sorted((n1,n2) -> n2.createdAt().compareTo(n1.createdAt()))
                .limit(30)
                .toList()
        );
    }

    public NotificationResponse readNotification(Long notificationId, User user) {
        Notification notification = user.getNotificationList().stream().filter(n -> n.getId().equals(notificationId))
                .findFirst()
                .orElseThrow(() -> new NotificationException(StatusCode.NOT_FOUND_NOTIFICATION));
        notification.read();
        return NotificationResponse.of(notification);
    }

}
