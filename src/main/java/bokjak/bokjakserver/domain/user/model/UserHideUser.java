package bokjak.bokjakserver.domain.user.model;

import bokjak.bokjakserver.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_hide_user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserHideUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_hide_user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hider_user_id", nullable = false)
    private User hiderUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hided_user_id", nullable = false)
    private User hidedUserId;
}
