package bokjak.bokjakserver.domain.category.model;

import bokjak.bokjakserver.domain.location.model.Location;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Location> locationList;

}
