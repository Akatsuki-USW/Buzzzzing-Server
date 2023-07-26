package bokjak.bokjakserver.domain.comment.dto;

import bokjak.bokjakserver.domain.category.model.SpotCategory;
import bokjak.bokjakserver.domain.comment.model.Comment;
import bokjak.bokjakserver.domain.location.model.Location;
import bokjak.bokjakserver.domain.spot.model.Spot;
import bokjak.bokjakserver.domain.user.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

import static bokjak.bokjakserver.common.constant.ConstraintConstants.*;
import static bokjak.bokjakserver.common.constant.ConstraintConstants.SPOT_IMAGE_MAX_SIZE;

public class CommentDto {
    /* Request */
    @Builder
    public record CreateSpotCommentRequest(
            @NotNull @Size(max = COMMENT_CONTENT_MAX_LENGTH)
            String content
    ) {
        public Comment toEntity(User user, Spot spot) {
            return Comment.builder()
                    .user(user)
                    .spot(spot)
                    .content(content)
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
            Long id,
            String content,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            Long userId,
            String userNickname,
            String userProfileImageUrl,
            boolean isAuthor
    ) {
        public static CommentCardResponse of(Comment comment, boolean isAuthor) {
            return CommentCardResponse.builder()
                    .id(comment.getId())
                    .content(comment.getContent())
                    .createdAt(comment.getCreatedAt())
                    .updatedAt(comment.getUpdatedAt())
                    .userId(comment.getUser().getId())
                    .userNickname(comment.getUser().getNickname())
                    .userProfileImageUrl(comment.getUser().getProfileImageUrl())
                    .isAuthor(isAuthor)
                    .build();
        }
    }

}
