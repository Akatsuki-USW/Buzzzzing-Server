package bokjak.bokjakserver.domain.comment.dto;

import bokjak.bokjakserver.common.constant.GlobalConstants;
import bokjak.bokjakserver.domain.comment.model.Comment;
import bokjak.bokjakserver.domain.spot.model.Spot;
import bokjak.bokjakserver.domain.user.model.User;
import bokjak.bokjakserver.util.CustomDateUtils;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import static bokjak.bokjakserver.common.constant.ConstraintConstants.COMMENT_CONTENT_MAX_LENGTH;

public class CommentDto {
    /* Request */
    @Builder
    public record CreateSpotCommentRequest(
            @NotNull @Size(max = COMMENT_CONTENT_MAX_LENGTH)
            String content
    ) {
        public Comment toEntity(User user, Spot spot) { // 댓글
            return Comment.builder()
                    .user(user)
                    .spot(spot)
                    .content(this.content)
                    .build();
        }

        public Comment toEntity(User user, Comment parent) { // 대댓글
            return Comment.builder()
                    .user(user)
                    .spot(parent.getSpot())
                    .parent(parent)
                    .content(this.content)
                    .build();
        }
    }

    @Builder
    public record UpdateSpotCommentRequest(
            @NotNull @Size(max = COMMENT_CONTENT_MAX_LENGTH)
            String content
    ) {
    }

    /* Response */
    @Builder
    public record CommentCardResponse(
            boolean presence,
            Long parentId,
            int childCount,
            Long id,
            String content,
            String createdAt,
            String updatedAt,
            Long userId,
            String userNickname,
            String userProfileImageUrl,
            boolean isAuthor
    ) {
        public static CommentCardResponse of(Comment comment, Long userId) {
                if (comment.isPresence()) {// 존재 여부에 따라 분기처리
                    User author = comment.getUser();

                    return CommentCardResponse.builder()
                            .presence(comment.isPresence())
                            .parentId(comment.isParent() ? null : comment.getParent().getId())
                            .childCount(comment.isParent() ? comment.getChildList().size() : 0) // 대댓글의 경우 항상 0
                            .id(comment.getId())
                            .content(comment.getContent())
                            .createdAt(CustomDateUtils.customDateFormat(comment.getCreatedAt(), GlobalConstants.DATE_FORMAT_YYYY_MM_DD_HH_MM))
                            .updatedAt(CustomDateUtils.customDateFormat(comment.getUpdatedAt(), GlobalConstants.DATE_FORMAT_YYYY_MM_DD_HH_MM))
                            .userId(author.getId())
                            .userNickname(author.getNickname())
                            .userProfileImageUrl(author.getProfileImageUrl())
                            .isAuthor(author.getId().equals(userId))
                            .build();
                } else {
                    return CommentCardResponse.builder()
                            .parentId(comment.isParent() ? null : comment.getParent().getId())
                            .childCount(comment.isParent() ? comment.getChildList().size() : 0) // 대댓글의 경우 항상 0
                            .id(comment.getId())
                            .presence(comment.isPresence())
                            .build();
                }
            }
        }

    public record CommentMessage(
            boolean result
    ) {
    }
}
