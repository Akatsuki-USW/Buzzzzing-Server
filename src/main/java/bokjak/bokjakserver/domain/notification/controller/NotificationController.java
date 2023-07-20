package bokjak.bokjakserver.domain.notification.controller;

import bokjak.bokjakserver.common.dto.ApiResponse;
import bokjak.bokjakserver.domain.notification.dto.NotificationDto;
import bokjak.bokjakserver.domain.notification.dto.NotificationDto.NotificationResponse;
import bokjak.bokjakserver.domain.notification.service.NotificationService;
import bokjak.bokjakserver.domain.user.model.User;
import bokjak.bokjakserver.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static bokjak.bokjakserver.common.dto.ApiResponse.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    @GetMapping("/users/me")
    public ApiResponse<NotificationDto.NotificationListResponse> getMyNotifications() {
        User currentUser = userService.getCurrentUser();
        return success(notificationService.getMyNotifications(currentUser));
    }

    @PutMapping("/{notificationId}/read")
    public ApiResponse<NotificationResponse> readNotification(@PathVariable Long notificationId) {
        User currentUser = userService.getCurrentUser();
        return success(notificationService.readNotification(notificationId, currentUser));
    }
}
