package bokjak.bokjakserver.domain.category.model;

import bokjak.bokjakserver.domain.location.model.Location;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LocationCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_category_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    private String iconImageUrl;

    @OneToMany(mappedBy = "locationCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Location> locationList = new ArrayList<>();

}
