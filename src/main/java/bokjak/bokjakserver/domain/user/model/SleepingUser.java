package bokjak.bokjakserver.domain.user.model;

import bokjak.bokjakserver.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SleepingUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sleeping_user_id")
    private Long id;

    private Long originalId;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;
    private String email;
    private String socialEmail;
    private String password;
    @Enumerated(EnumType.STRING)
    private SocialType socialType;
    private String profileImageUrl;
    private String nickname;
    private LocalDateTime lastLoginDate;



}
