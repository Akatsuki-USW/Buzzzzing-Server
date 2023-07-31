package bokjak.bokjakserver.domain.comment.model;

import bokjak.bokjakserver.common.constant.ConstraintConstants;
import bokjak.bokjakserver.common.model.BaseEntity;
import bokjak.bokjakserver.domain.spot.model.Spot;
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
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "spot_id")
    private Spot spot;

    @NotNull
    @Size(max = ConstraintConstants.COMMENT_CONTENT_MAX_LENGTH)
    private String content;

    private boolean presence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parent; // 댓글, 대댓글 2가지로 나뉨. 무한 대댓글 아님

    @Builder.Default
    @OneToMany(mappedBy = "parent")
    List<Comment> childList = new ArrayList<>();

    // PrePersist : presence의 디폴트 값을 true로
    @PrePersist
    public void setDefaultPresence() {
        this.presence = true;
    }

    /* 편의 메서드 */
    public void update(String content) {
        this.content = content;
    }

    public void addChild(Comment child) {
        child.parent = this;
        this.childList.add(child);
    }

    public void softDelete() {  // 댓글 삭제시 대댓글은 삭제하지 않음
        this.presence = false;
    }

    public boolean isParent() {
        return this.parent == null;
    }
}
