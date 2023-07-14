package bokjak.bokjakserver.domain.congestion.model;

import bokjak.bokjakserver.domain.location.model.Location;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyCongestionStatistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daily_congestion_statistic_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "location_id")
    private Location location;

    @CreatedDate
    private LocalDateTime createdAt;    // 혼잡도 통계 더미 데이터의 created_at을 임의 설정하기 위함. deprecated.

    // TODO refactor: Map<String, Integer> -> 클래스
    @Type(JsonType.class)
    @Column(columnDefinition = "json", nullable = false)
    @Builder.Default
    private Map<String, ArrayList<Map<String, Integer>>> content = new HashMap<>();  // 시간별 혼잡도 (09시 ~ 24시)
}
