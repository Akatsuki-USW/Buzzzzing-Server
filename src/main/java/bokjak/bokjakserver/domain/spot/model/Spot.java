package bokjak.bokjakserver.domain.spot.model;


import bokjak.bokjakserver.domain.bookmark.model.SpotBookmark;
import bokjak.bokjakserver.domain.category.model.SpotCategory;
import bokjak.bokjakserver.domain.comment.model.Comment;
import bokjak.bokjakserver.domain.location.model.Location;
import bokjak.bokjakserver.domain.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Spot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spot_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spot_category_id", nullable = false)
    private SpotCategory spotCategory;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(length = 50)
    private String title;
    @Column(length = 50)
    private String spot_name;
    private String address;
    private String roadNameAddress;
    @Column(length = 1500)
    private String content;
    @Column(length = 500)
    private String thumbnailImageUrl;

    @OneToMany(mappedBy = "spot", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SpotBookmark> spotBookmarkList;
    @OneToMany(mappedBy = "spot", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList;
    @OneToMany(mappedBy = "spot", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SpotImage> spotImageList;
}
