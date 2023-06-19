package bokjak.bokjakserver.domain.location.model;

import bokjak.bokjakserver.common.model.BaseEntity;
import bokjak.bokjakserver.domain.bookmark.model.LocationBookmark;
import bokjak.bokjakserver.domain.category.model.LocationCategory;
import bokjak.bokjakserver.domain.congestion.model.Congestion;
import bokjak.bokjakserver.domain.spot.model.Spot;
import jakarta.persistence.*;
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

    private int apiId;  // 혼잡도 API(SK, 서울시)상의 장소 PK값

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_category_id", nullable = false)
    private LocationCategory locationCategory;

    private String name;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Congestion> congestionList = new ArrayList<>();

    // TODO : new ArrayList
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Spot> spotList;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocationBookmark> locationBookmarkList;
}
