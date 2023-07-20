package bokjak.bokjakserver.domain.notification.dto;

import bokjak.bokjakserver.domain.comment.model.Comment;
import bokjak.bokjakserver.domain.notification.model.Notification;
import bokjak.bokjakserver.domain.notification.model.NotificationType;
import bokjak.bokjakserver.domain.spot.model.Spot;
import bokjak.bokjakserver.domain.user.model.User;
import lombok.Builder;

import java.util.List;

public class NotificationDto {
    public record NotifyParams(
            User receiver, NotificationType type, Long redirectTargetId, String title, String content
    ) {
        @Builder
        public NotifyParams{}

        public static NotifyParams ofCreateSpotComment(
                User spotAuthor,
                User commentAuthor,
                Spot spot,
                Comment comment
        ) {
            String content = """
                    '%s'님이 '%s'님의 게시글에 댓글을 남겼어요 : %s
                    """.formatted(
                            commentAuthor.getNickname(),
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

        public static NotifyParams ofCreateFeedCommentComment(
                User commentAuthor,
                Spot spot,
                Comment parentComment,
                Comment comment
        ) {
            String content = """
                    '%s'님이 '%s'님의 댓글에 대댓글을 남겼어요 : %s
                    """.formatted(
                            commentAuthor.getNickname(),
                    parentComment.getUser().getNickname(),
                    comment.getUser().getNickname(),
                    comment.getContent()
            );
            return NotifyParams.builder()
                    .receiver(commentAuthor)
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
                    .createdAt(notification.getCreatedAt())
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
