package bokjak.bokjakserver.domain.comment.repository;

import bokjak.bokjakserver.domain.comment.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom{
}
