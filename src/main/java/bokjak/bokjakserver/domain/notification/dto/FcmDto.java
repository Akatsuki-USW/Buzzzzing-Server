package bokjak.bokjakserver.domain.notification.dto;

public class FcmDto {
    public record FcmMessage(
            Boolean validate_only,
            Message message
    ) {}

    public record Message(
            String token,
            Notification data
    ) {}

    public record Notification(
            String title,
            String body,
            String redirectTargetId,
            String type
    ) {}

    public record PushMessage(
            Long receiverId,
            String title,
            String body
    ) {}
}
