package bokjak.bokjakserver.domain.user.model;

import bokjak.bokjakserver.common.model.BaseEntity;
import bokjak.bokjakserver.domain.bookmark.model.Bookmark;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Role role;
    private String email;
    @Enumerated(EnumType.STRING)
    private Socialtype socialtype;
    private String social_uuid;
    private String refresh_token;
    private String nickname;
    private LocalDateTime lastVisitedAt;
    private LocalDateTime quitedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bookmark> bookmarkList;

    @OneToMany(mappedBy = "subjectUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserBlackUser> subjectUserList;

    @OneToMany(mappedBy = "blackedUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserBlackUser> blackUserList;
}
