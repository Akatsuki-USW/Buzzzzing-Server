package bokjak.bokjakserver.domain.user.model;

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
public class RevokeUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "revoke_user_id")
    private Long id;

    private String socialEmail;
    private LocalDateTime revokedAt;

    public void deleteRevokeUser() {
        this.socialEmail=null;
        this.revokedAt=null;
    }
}
