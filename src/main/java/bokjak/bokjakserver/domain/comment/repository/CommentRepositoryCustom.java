package bokjak.bokjakserver.domain.comment.repository;

import bokjak.bokjakserver.domain.comment.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepositoryCustom {
    Page<Comment> findAllBySpotExceptBlockedAuthors(Pageable pageable, Long cursorId, Long spotId, Long userId);
}
