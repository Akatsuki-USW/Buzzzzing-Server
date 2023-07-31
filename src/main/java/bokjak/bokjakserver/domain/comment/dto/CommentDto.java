package bokjak.bokjakserver.domain.comment.dto;

import bokjak.bokjakserver.domain.comment.model.Comment;
import bokjak.bokjakserver.domain.spot.model.Spot;
import bokjak.bokjakserver.domain.user.model.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDateTime;

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

        public Comment toEntity(User user, Spot spot, Comment parent) { // 대댓글
            return Comment.builder()
                    .user(user)
                    .spot(spot)
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
    public record ParentCommentCardResponse(
            Long id,
            String content,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            Long userId,
            String userNickname,
            String userProfileImageUrl,
            boolean isAuthor
    ) {
        public static ParentCommentCardResponse of(Comment comment, Long userId) {
            User author = comment.getUser();

            // TODO presence에 따라 분기처리
            if (comment.isPresence()) {
                return ParentCommentCardResponse.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .updatedAt(comment.getUpdatedAt())
                        .userId(author.getId())
                        .userNickname(author.getNickname())
                        .userProfileImageUrl(author.getProfileImageUrl())
                        .isAuthor(author.getId().equals(userId))
                        .build();

            } else {
                return ParentCommentCardResponse.builder()
                        .id(comment.getId())
                        .isAuthor(author.getId().equals(userId))
                        .build();
            }
        }
    }

    @Builder
    public record ChildCommentCardResponse(
            Long parentId,
            Long id,
            String content,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            Long userId,
            String userNickname,
            String userProfileImageUrl,
            boolean isAuthor
    ) {
        public static ChildCommentCardResponse of(Comment comment, Long userId) {
            return ChildCommentCardResponse.builder()
                    .id(comment.getId())
                    .parentId(comment.getParent().getId())
                    .content(comment.getContent())
                    .createdAt(comment.getCreatedAt())
                    .updatedAt(comment.getUpdatedAt())
                    .userId(comment.getUser().getId())
                    .userNickname(comment.getUser().getNickname())
                    .userProfileImageUrl(comment.getUser().getProfileImageUrl())
                    .isAuthor(comment.getUser().getId().equals(userId))
                    .build();
        }
    }

    public record CommentMessage(
            boolean result
    ) {
    }
}
