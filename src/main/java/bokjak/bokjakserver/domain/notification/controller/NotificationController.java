package bokjak.bokjakserver.domain.notification.controller;

import bokjak.bokjakserver.common.constant.SwaggerConstants;
import bokjak.bokjakserver.common.dto.ApiResponse;
import bokjak.bokjakserver.config.security.PrincipalDetails;
import bokjak.bokjakserver.domain.notification.dto.NotificationDto;
import bokjak.bokjakserver.domain.notification.dto.NotificationDto.NotificationResponse;
import bokjak.bokjakserver.domain.notification.service.NotificationService;
import bokjak.bokjakserver.domain.user.model.User;
import bokjak.bokjakserver.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static bokjak.bokjakserver.common.constant.SwaggerConstants.*;
import static bokjak.bokjakserver.common.dto.ApiResponse.*;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = SwaggerConstants.SECURITY_SCHEME_NAME)
@RequestMapping("/notification")
@Tag(name = TAG_NOTIFICATION, description = TAG_NOTIFICATION_DESCRIPTION)
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = NOTIFICATION_ME, description = NOTIFICATION_ME_DESCRIPTION)
    @GetMapping("/users/me")
    public ApiResponse<NotificationDto.NotificationListResponse> getMyNotifications(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(notificationService.getMyNotifications(principalDetails.getUserId()));
    }

    @Operation(summary = NOTIFICATION_READ, description = NOTIFICATION_READ_DESCRIPTION)
    @PutMapping("/{notificationId}/read")
    public ApiResponse<NotificationResponse> readNotification(@PathVariable Long notificationId,
                                                              @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(notificationService.readNotification(notificationId, principalDetails.getUserId()));
    }
}
