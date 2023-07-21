package bokjak.bokjakserver.domain.spot.model;


import bokjak.bokjakserver.common.constant.ConstraintConstants;
import bokjak.bokjakserver.common.model.BaseEntity;
import bokjak.bokjakserver.domain.bookmark.model.SpotBookmark;
import bokjak.bokjakserver.domain.category.model.SpotCategory;
import bokjak.bokjakserver.domain.comment.model.Comment;
import bokjak.bokjakserver.domain.location.model.Location;
import bokjak.bokjakserver.domain.user.model.User;
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
public class Spot extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spot_id")
    private Long id;

    @NotNull
    @Size(max = ConstraintConstants.SPOT_TITLE_MAX_LENGTH)
    private String title;

    @Size(max = ConstraintConstants.SPOT_ADDRESS_MAX_LENGTH)
    private String address;

    private String roadNameAddress; // deprecated : 클라이언트측에서 도로명 입력 안 함

    @NotNull
    @Size(max = ConstraintConstants.SPOT_CONTENT_MAX_LENGTH)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "spot_category_id")
    private SpotCategory spotCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "spot", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SpotBookmark> spotBookmarkList = new ArrayList<>();

    @OneToMany(mappedBy = "spot", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "spot", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SpotImage> spotImageList = new ArrayList<>();

    /* 연관관계 메서드 */
    public void addSpotImage(SpotImage spotImage) {
        spotImageList.add(spotImage);
    }

    public void addSpotImages(List<SpotImage> spotImages) {
        spotImageList.addAll(spotImages);
    }
}
