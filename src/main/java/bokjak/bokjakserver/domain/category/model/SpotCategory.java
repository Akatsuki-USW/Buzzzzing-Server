package bokjak.bokjakserver.domain.category.model;

import bokjak.bokjakserver.common.constant.ConstraintConstants;
import bokjak.bokjakserver.common.model.BaseEntity;
import bokjak.bokjakserver.domain.spot.model.Spot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SpotCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spot_category_id")
    private Long id;

    @NotNull
    @Size(max = ConstraintConstants.SPOT_CATEGORY_NAME_MAX_LENGTH)
    private String name;

    @OneToMany(mappedBy = "spotCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Spot> spotList = new ArrayList<>();
}
