package bokjak.bokjakserver.domain.congestion.model;

import bokjak.bokjakserver.common.constant.ConstraintConstants;
import bokjak.bokjakserver.common.model.BaseEntity;
import bokjak.bokjakserver.domain.location.model.Location;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Congestion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "congestion_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "location_id")
    private Location location;

    @NotNull
    @Max(ConstraintConstants.CONGESTION_LEVEL_MAX_VALUE)
    private int congestionLevel;   //range : 1 ~ 3 (여유, 보통, 혼잡)

    private LocalDateTime observedAt;
}
