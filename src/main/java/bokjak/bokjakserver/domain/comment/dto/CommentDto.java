package bokjak.bokjakserver.domain.comment.dto;

import bokjak.bokjakserver.domain.comment.model.Comment;
import lombok.Builder;

import java.time.LocalDateTime;

public class CommentDto {
    /* Request */

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
