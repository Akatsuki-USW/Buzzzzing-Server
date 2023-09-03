package bokjak.bokjakserver.domain.notification.model;

import bokjak.bokjakserver.common.model.BaseEntity;
import bokjak.bokjakserver.domain.user.model.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    private String title;
    private String content;
    private boolean isRead;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private Long redirectTargetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void read() {
        this.isRead = true;
    }
}
