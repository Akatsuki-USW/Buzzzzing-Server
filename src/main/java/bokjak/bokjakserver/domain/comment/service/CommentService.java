package bokjak.bokjakserver.domain.comment.service;

import bokjak.bokjakserver.common.dto.PageResponse;
import bokjak.bokjakserver.common.exception.StatusCode;
import bokjak.bokjakserver.domain.comment.dto.CommentDto.*;
import bokjak.bokjakserver.domain.comment.exception.CommentException;
import bokjak.bokjakserver.domain.comment.model.Comment;
import bokjak.bokjakserver.domain.comment.repository.CommentRepository;
import bokjak.bokjakserver.domain.spot.exception.SpotException;
import bokjak.bokjakserver.domain.spot.model.Spot;
import bokjak.bokjakserver.domain.spot.repository.SpotRepository;
import bokjak.bokjakserver.domain.user.model.User;
import bokjak.bokjakserver.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final UserService userService;
    private final SpotRepository spotRepository;
    private final CommentRepository commentRepository;

    // 스팟별 댓글 리스트 조회
    public PageResponse<ParentCommentCardResponse> getParentComments(Long currentUserId, Pageable pageable, Long cursorId, Long spotId) {
        Page<Comment> comments = commentRepository.findAllParentBySpotExceptBlockedAuthors(pageable, cursorId, spotId, currentUserId);

        Page<ParentCommentCardResponse> resultPage = comments
                .map(it -> ParentCommentCardResponse.of(it, currentUserId)); // 작성자 여부
        return PageResponse.of(resultPage);
    }

    // 대댓글 리스트 조회
    public PageResponse<ChildCommentCardResponse> getChildComments(Long currentUserId, Pageable pageable, Long cursorId, Long parentId) {
        Page<Comment> comments = commentRepository.findAllChildrenByParentExceptBlockedAuthors(pageable, cursorId, parentId, currentUserId);

        Page<ChildCommentCardResponse> resultPage = comments.map(it -> ChildCommentCardResponse.of(it, currentUserId));
        return PageResponse.of(resultPage);
    }

    // 스팟 댓글 생성
    @Transactional
    public ParentCommentCardResponse createParentComment(Long currentUserId, Long spotId, CreateSpotCommentRequest createSpotCommentRequest) {
        User user = userService.getUser(currentUserId);
        Spot spot = spotRepository.findById(spotId)
                .orElseThrow(() -> new SpotException(StatusCode.NOT_FOUND_SPOT));

        Comment comment = commentRepository.save(createSpotCommentRequest.toEntity(user, spot));

        return ParentCommentCardResponse.of(comment, currentUserId);
    }

    // 대댓글 생성
    @Transactional
    public ChildCommentCardResponse createChildComment(Long currentUserId, Long parentId, CreateSpotCommentRequest createSpotCommentRequest) {
        User user = userService.getUser(currentUserId);
        Comment parent = commentRepository.findById(parentId)
                .orElseThrow(() -> new CommentException(StatusCode.NOT_FOUND_COMMENT));
        // 부모 댓글이 삭제된 경우 (논리적 에러)
        if (!parent.isPresence()) throw new CommentException(StatusCode.NOT_FOUND_COMMENT);

        Comment comment = commentRepository.save(createSpotCommentRequest.toEntity(user, parent));

        return ChildCommentCardResponse.of(comment, currentUserId);
    }

    // 스팟 댓글 수정
    @Transactional
    public ParentCommentCardResponse updateSpotComment(Long currentUserId, Long commentId, UpdateSpotCommentRequest updateSpotCommentRequest) {
        User user = userService.getUser(currentUserId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(StatusCode.NOT_FOUND_COMMENT));

        comment.update(updateSpotCommentRequest.content());

        return ParentCommentCardResponse.of(comment, currentUserId);
    }

    // 스팟 댓글 삭제
    @Transactional
    public CommentMessage deleteSpotComment(Long currentUserId, Long commentId) {
        User user = userService.getUser(currentUserId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(StatusCode.NOT_FOUND_COMMENT));

        checkIsAuthor(user, comment);

        // TODO CASCADE 방지 해보기. parentId = null로 바꾸고
        commentRepository.delete(comment);

        return new CommentMessage(true);
    }

    private static void checkIsAuthor(User user, Comment comment) {// 작성자인지 확인: 수정, 삭제는 작성자만 권한을 가짐
        if (!comment.getUser().equals(user)) {
            throw new CommentException(StatusCode.NOT_AUTHOR);
        }
    }
}
