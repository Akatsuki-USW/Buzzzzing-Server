package bokjak.bokjakserver.domain.user.model;

import bokjak.bokjakserver.common.model.BaseEntity;
import bokjak.bokjakserver.domain.ban.model.Ban;
import bokjak.bokjakserver.domain.bookmark.model.LocationBookmark;
import bokjak.bokjakserver.domain.bookmark.model.SpotBookmark;
import bokjak.bokjakserver.domain.comment.model.Comment;
import bokjak.bokjakserver.domain.report.model.Report;
import bokjak.bokjakserver.domain.spot.model.Spot;
import jakarta.persistence.*;
import lombok.*;

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
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;
    private String email;
    private String socialUuid;
    @Enumerated(EnumType.STRING)
    private SocialType socialType;
    @Column(length = 500)
    private String profileImageUrl;
    private String refreshToken;
    @Column(length = 20)
    private String nickname;
    private LocalDateTime lastLoginDate;


    //회원(1) - 게시글 북마크 (다)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SpotBookmark> spotBookmarkList;

    //회원(1) - 장소 북마크(다)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocationBookmark> locationBookmarkList;

    //회원(1) - 차단을 원하는 유저(다)
    @OneToMany(mappedBy = "hiderUserId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserHideUser> hiderUserList;

    //회원(1) - 차단당한 유저(다)
    @OneToMany(mappedBy = "hidedUserId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserHideUser> hidedUserList;

    //회원(1) - 신고한 유저(다)
    @OneToMany(mappedBy = "reporterUserId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reporterUserList;

    //회원(1) - 신고당한 유저(다)
    @OneToMany(mappedBy = "reportedUserId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reportedUserList;

    //회원(1) - 정지 유저(다)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ban> banList;

    //회원(1) - 게시글(다)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Spot> spotList;

    //회원(1) - 댓글(다)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList;
}
