package bokjak.bokjakserver.domain.report.model;

import bokjak.bokjakserver.common.model.BaseEntity;
import bokjak.bokjakserver.domain.ban.model.Ban;
import bokjak.bokjakserver.domain.user.model.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_user_id")
    private User reporter;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_user_id")
    private User reportedUser;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ban_id")
    private Ban banId;
    @Enumerated(EnumType.STRING)
    private ReportTarget reportTarget;
    private Long targetId;
    @Column(length = 300)
    private String content;
}
