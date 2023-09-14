package bokjak.bokjakserver.domain.test;

import bokjak.bokjakserver.common.dto.ApiResponse;
import bokjak.bokjakserver.domain.notification.dto.NotificationDto;
import bokjak.bokjakserver.domain.notification.dto.NotificationDto.NotifyParams;
import bokjak.bokjakserver.domain.notification.model.NotificationType;
import bokjak.bokjakserver.domain.notification.service.NotificationService;
import bokjak.bokjakserver.domain.user.model.User;
import bokjak.bokjakserver.domain.user.service.SleepingUserService;
import bokjak.bokjakserver.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    private final UserService userService;
    private final NotificationService notificationService;

    private final SleepingUserService sleepingUserService;

    @PostMapping("/push/self")
    public ApiResponse<NotificationDto.NotificationListResponse> testCreateNotificationMySelf() {
        User currentUser = userService.getCurrentUser();
        NotifyParams params = NotifyParams.builder()
                .receiver(currentUser)
                .type(NotificationType.TEST_USER_ITSELF)
                .redirectTargetId(currentUser.getId())
                .title("test")
                .content("자기 자신의 아이디를 리턴")
                .build();
        notificationService.pushMessage(params);
        return ApiResponse.success(notificationService.getMyNotifications(currentUser.getId()));
    }

    @GetMapping("/send/email")
    public String testSendEmailMySelf() {
        User currentUser = userService.getCurrentUser();
        sleepingUserService.sendMail(currentUser.getEmail());
        return "good";
    }
}
