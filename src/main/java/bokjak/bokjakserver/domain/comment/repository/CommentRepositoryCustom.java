package bokjak.bokjakserver.domain.comment.repository;

import bokjak.bokjakserver.domain.comment.model.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CommentRepositoryCustom {
    Slice<Comment> findAllParentBySpotExceptBlockedAuthors(Pageable pageable, Long cursorId, Long spotId, Long userId);

    Long countAllParentBySpotExceptBlockedAuthors(Long spotId, Long userId);

    Slice<Comment> findAllChildrenByParentExceptBlockedAuthors(Pageable pageable, Long cursorId, Long parentId, Long userId);

    Long countAllChildrenByParentExceptBlockedAuthors(Long parentId, Long userId);
}
