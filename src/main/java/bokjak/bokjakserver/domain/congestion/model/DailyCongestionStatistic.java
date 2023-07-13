package bokjak.bokjakserver.domain.congestion.model;

import bokjak.bokjakserver.common.model.BaseEntity;
import bokjak.bokjakserver.domain.location.model.Location;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyCongestionStatistic extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "congestion_statistic_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    @Builder.Default
    private Map<String, ArrayList<Map<String, Integer>>> content = new HashMap<>();  // 시간별 혼잡도 (09시 ~ 24시)
}
