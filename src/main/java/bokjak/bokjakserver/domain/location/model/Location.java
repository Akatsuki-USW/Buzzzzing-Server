package bokjak.bokjakserver.domain.location.model;

import bokjak.bokjakserver.common.model.BaseEntity;
import bokjak.bokjakserver.domain.bookmark.model.LocationBookmark;
import bokjak.bokjakserver.domain.category.model.LocationCategory;
import bokjak.bokjakserver.domain.congestion.model.Congestion;
import bokjak.bokjakserver.domain.congestion.model.DailyCongestionStatistic;
import bokjak.bokjakserver.domain.congestion.model.WeeklyCongestionStatistic;
import bokjak.bokjakserver.domain.spot.model.Spot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Location extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long id;

    @NotNull
    private String name;

    @Column(name = "realtime_congestion_level")
    private Integer realtimeCongestionLevel;
    private int apiId;  // 혼잡도 API(SK, 서울시)상의 장소 PK값

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "location_category_id")
    private LocationCategory locationCategory;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Congestion> congestionList = new ArrayList<>();

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DailyCongestionStatistic> dailyCongestionStatisticList = new ArrayList<>();

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<WeeklyCongestionStatistic> weeklyCongestionStatisticList = new ArrayList<>();

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Spot> spotList = new ArrayList<>();

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<LocationBookmark> locationBookmarkList = new ArrayList<>();
}
