package bokjak.bokjakserver.domain.notification.model;

import bokjak.bokjakserver.domain.spot.model.Spot;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum NotificationType {

    /**
     * 내 게시글에 답글
     */
    CREATE_SPOT_COMMENT(ReceiverType.AUTHOR, Object.class),

    /**
     * 내 답글에 답글(대댓글)
     */
    CREATE_SPOT_COMMENT_COMMENT(ReceiverType.AUTHOR, Spot.class);

    private enum ReceiverType {
        AUTHOR,USER
    }

    private final ReceiverType receiverType;
    private final Class<?> redirectTargetClass;
}
