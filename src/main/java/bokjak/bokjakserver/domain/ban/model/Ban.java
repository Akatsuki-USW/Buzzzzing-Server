package bokjak.bokjakserver.domain.ban.model;


import bokjak.bokjakserver.common.model.BaseEntity;
import bokjak.bokjakserver.domain.report.model.Report;
import bokjak.bokjakserver.domain.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ban extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ban_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "banned_user_id", nullable = false)
    private User user;
    @OneToOne(mappedBy = "banId")       //n+1 잘 생각해봐야할 듯
    private Report report;
    @Column(length = 50)
    private String title;
    @Column(length = 300)
    private String content;
    private LocalDate banStartedAt;
    private LocalDate banEndedAt;
}
