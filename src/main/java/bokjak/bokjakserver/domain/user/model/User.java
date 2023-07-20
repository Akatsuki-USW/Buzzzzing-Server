package bokjak.bokjakserver.domain.user.model;

import bokjak.bokjakserver.common.model.BaseEntity;
import bokjak.bokjakserver.domain.ban.model.Ban;
import bokjak.bokjakserver.domain.bookmark.model.LocationBookmark;
import bokjak.bokjakserver.domain.bookmark.model.SpotBookmark;
import bokjak.bokjakserver.domain.comment.model.Comment;
import bokjak.bokjakserver.domain.notification.model.Notification;
import bokjak.bokjakserver.domain.report.model.Report;
import bokjak.bokjakserver.domain.spot.model.Spot;
import bokjak.bokjakserver.domain.user.dto.UserDto;
import bokjak.bokjakserver.domain.user.dto.UserDto.UpdateUserInfoRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private String password;    //다시 알아보기
    private String socialEmail;
    @Enumerated(EnumType.STRING)
    private SocialType socialType;
    @Column(length = 500)
    private String profileImageUrl;
    @Column(length = 20)
    private String nickname;
    private LocalDateTime lastLoginDate;
    private String fcmToken;


    //회원(1) - 게시글 북마크 (다)
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SpotBookmark> spotBookmarkList = new ArrayList<>();

    //회원(1) - 장소 북마크(다)
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocationBookmark> locationBookmarkList = new ArrayList<>();

    //회원(1) - 다른 유저에게 차단당한 유저(다)
    @Builder.Default
    @OneToMany(mappedBy = "blockerUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserBlockUser> blockerUserList = new ArrayList<>();

    //회원(1) - 차단한 유저(다)
    @Builder.Default
    @OneToMany(mappedBy = "blockedUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserBlockUser> blockedUserList = new ArrayList<>();

    //회원(1) - 다른 유저에게 신고당한 유저(다)
    @Builder.Default
    @OneToMany(mappedBy = "reporterUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reporterUserList = new ArrayList<>();

    //회원(1) - 신고한 유저(다)
    @Builder.Default
    @OneToMany(mappedBy = "reportedUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reportedUserList = new ArrayList<>();

    //회원(1) - 정지 유저(다)
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ban> banList = new ArrayList<>();

    //회원(1) - 게시글(다)
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Spot> spotList = new ArrayList<>();

    //회원(1) - 댓글(다)
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notificationList = new ArrayList<>();

    public void deletedUser() {
        this.userStatus = UserStatus.DELETED;
        this.email = null;
        this.password = null;
        this.nickname = null;
        this.socialEmail = null;
        this.socialType = null;
        this.profileImageUrl = null;
    }

    public void updateUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public void addBlockerUser(UserBlockUser userBlockUser) {
        if (blockerUserList == null) blockerUserList = new ArrayList<>();
        blockerUserList.add(userBlockUser);

    }
    public void addBlockedUser(UserBlockUser userBlockUser) {
        if (blockedUserList == null) blockedUserList = new ArrayList<>();
        blockedUserList.add(userBlockUser);
    }

    public void addReportedUser(Report report) {
        if (reportedUserList == null) reportedUserList = new ArrayList<>();
        reportedUserList.add(report);
    }

    public void addNotification(Notification notification) {
        if (notificationList == null) notificationList = new ArrayList<>();
        notificationList.add(notification);
    }

    public void removeBlockerUser(UserBlockUser userBlockUser) {
        blockerUserList.remove(userBlockUser);
    }

    public void removeBlockedUser(UserBlockUser userBlockUser) {
        blockedUserList.remove(userBlockUser);
    }

    public void changeSleepingUser() {
        this.userStatus = UserStatus.SLEEP;
        this.email = null;
        this.password = null;
        this.socialEmail = null;
        this.socialType = null;
        this.lastLoginDate = null;
    }

    public void changeWakeUpUser(SleepingUser sleepingUser) {
        this.userStatus = sleepingUser.getUserStatus();
        this.email = sleepingUser.getEmail();
        this.password = sleepingUser.getPassword();
        this.socialEmail = sleepingUser.getSocialEmail();
        this.socialType = sleepingUser.getSocialType();
    }

    public void updateLastLoginDate() {
        this.lastLoginDate = LocalDateTime.now();
    }

    public void updateFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
    public void updateUserInfo(UpdateUserInfoRequest updateUserInfoRequest) {
        this.email = updateUserInfoRequest.email();
        this.nickname = updateUserInfoRequest.nickname();
        this.profileImageUrl = updateUserInfoRequest.profileImageUrl();
    }
}
