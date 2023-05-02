package bokjak.bokjakserver.domain.user.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_black_user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserBlackUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_black_user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_user_id", nullable = false)
    private User subjectUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blacked_user_id", nullable = false)
    private User blackedUser;
}
