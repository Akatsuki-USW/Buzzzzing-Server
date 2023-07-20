package bokjak.bokjakserver.domain.congestion.model;

import bokjak.bokjakserver.domain.location.model.Location;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WeeklyCongestionStatistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weekly_congestion_statistic_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "location_id")
    private Location location;

    @NotNull
    private Float averageCongestionLevel;    // 일주일 혼잡도 평균

    @CreatedDate
    private LocalDateTime createdAt;    // 혼잡도 통계 더미 데이터의 created_at을 임의 설정하기 위함. deprecated.
}
