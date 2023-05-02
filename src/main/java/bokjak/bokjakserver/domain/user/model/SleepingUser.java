package bokjak.bokjakserver.domain.user.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_black_user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SleepingUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sleeping_user_id")
    private Long id;

    private Long userId;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String email;
    @Enumerated(EnumType.STRING)
    private Socialtype socialtype;
    private String socail_uuid;
    private String refresh_token;
    private String nickname;
    private LocalDateTime lastVisitedAt;
    private LocalDateTime quitedAt;

}
