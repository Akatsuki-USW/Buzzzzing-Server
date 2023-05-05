package bokjak.bokjakserver.domain.congestion.model;

import bokjak.bokjakserver.common.model.BaseEntity;
import bokjak.bokjakserver.domain.location.model.Location;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    private String congestion_level;
    private LocalDate observedAt;
}
