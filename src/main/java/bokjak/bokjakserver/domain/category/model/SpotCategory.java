package bokjak.bokjakserver.domain.category.model;

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
public class SpotCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spot_category_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "spotCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Spot> spotList = new ArrayList<>();
}
