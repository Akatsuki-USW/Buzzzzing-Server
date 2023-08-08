package bokjak.bokjakserver.domain.comment.repository;

import bokjak.bokjakserver.domain.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom{

    List<Comment> findAllByParent(Comment parentComment);

}
