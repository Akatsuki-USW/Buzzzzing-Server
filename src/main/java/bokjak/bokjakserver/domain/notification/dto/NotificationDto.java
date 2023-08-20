package bokjak.bokjakserver.domain.notification.dto;

import bokjak.bokjakserver.common.constant.GlobalConstants;
import bokjak.bokjakserver.domain.comment.model.Comment;
import bokjak.bokjakserver.domain.notification.model.Notification;
import bokjak.bokjakserver.domain.notification.model.NotificationType;
import bokjak.bokjakserver.domain.spot.model.Spot;
import bokjak.bokjakserver.domain.user.model.User;
import lombok.Builder;

import java.util.List;

import static bokjak.bokjakserver.util.CustomDateUtils.customDateFormat;

public class NotificationDto {
    public record NotifyParams(
            User receiver, NotificationType type, Long redirectTargetId, String title, String content
    ) {
        @Builder
        public NotifyParams{}

        public static NotifyParams ofCreateSpotComment(
                User spotAuthor,
                Spot spot,
                Comment comment
        ) {
            String content = """
                    %s님이 %s님의 게시글에 댓글을 남겼어요 : %s
                    """.formatted(
                            comment.getUser().getNickname(),
                    spotAuthor.getNickname(),
                    comment.getContent()
            );
            return NotifyParams.builder()
                    .receiver(spotAuthor)
                    .type(NotificationType.CREATE_SPOT_COMMENT)
                    .redirectTargetId(spot.getId())
                    .title(spot.getTitle())
                    .content(content)
                    .build();
        }

        public static NotifyParams ofCreateSpotCommentComment(
                Comment parentComment,
                Spot spot,
                Comment childComment
        ) {
            String content = """
                    %s님이 %s님의 댓글에 대댓글을 남겼어요 : %s
                    """.formatted(
                    childComment.getUser().getNickname(),
                    parentComment.getUser().getNickname(),
                    childComment.getContent()
            );
            return NotifyParams.builder()
                    .receiver(parentComment.getUser())
                    .type(NotificationType.CREATE_SPOT_COMMENT_COMMENT)
                    .redirectTargetId(spot.getId())
                    .title(spot.getTitle())
                    .content(content)
                    .build();
        }

        public static NotifyParams ofCreateSpotCommentComment(
                User notifyTarget,
                Spot spot,
                Comment childComment
        ) {
            String content = """
                    %s님이 대댓글을 남겼어요 : %s
                    """.formatted(
                    childComment.getUser().getNickname(),
                    childComment.getContent()
            );
            return NotifyParams.builder()
                    .receiver(notifyTarget)
                    .type(NotificationType.CREATE_SPOT_COMMENT_COMMENT)
                    .redirectTargetId(spot.getId())
                    .title(spot.getTitle())
                    .content(content)
                    .build();
        }
    }
    public record NotificationResponse(
            Long notificationId,
            NotificationType notificationType,
            String targetEntity,
            Long redirectTargetId,
            String title,
            String body,
            String createdAt,
            boolean isRead
    ) {
        @Builder
        public NotificationResponse {}

        public static NotificationResponse of(Notification notification) {
            NotificationType notificationType = notification.getType();
            String targetClassName = notificationType.getRedirectTargetClass().getSimpleName();
            return NotificationResponse.builder()
                    .notificationId(notification.getId())
                    .notificationType(notificationType)
                    .targetEntity(targetClassName)
                    .redirectTargetId(notification.getRedirectTargetId())
                    .title(notification.getTitle())
                    .body(notification.getContent())
                    .createdAt(customDateFormat(notification.getCreatedAt(), GlobalConstants.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS))
                    .isRead(notification.isRead())
                    .build();
        }

    }

    public record NotificationListResponse(List<NotificationResponse> notifications) {
        @Builder
        public NotificationListResponse {}

        public static NotificationListResponse of(List<NotificationResponse> notifications) {
            return NotificationListResponse.builder()
                    .notifications(notifications)
                    .build();
        }
    }
}
