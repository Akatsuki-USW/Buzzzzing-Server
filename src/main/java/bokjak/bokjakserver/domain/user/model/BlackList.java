package bokjak.bokjakserver.domain.user.model;

import bokjak.bokjakserver.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
public class BlackList extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "black_list_id")
    private Long id;

    private String socialEmail;
    private LocalDateTime banStartedAt;
    private LocalDateTime banEndedAt;
}
