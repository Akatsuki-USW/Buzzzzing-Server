package bokjak.bokjakserver.domain.comment.service;

import bokjak.bokjakserver.common.dto.PageResponse;
import bokjak.bokjakserver.common.exception.StatusCode;
import bokjak.bokjakserver.domain.comment.dto.CommentDto.CommentCardResponse;
import bokjak.bokjakserver.domain.comment.dto.CommentDto.CommentMessage;
import bokjak.bokjakserver.domain.comment.dto.CommentDto.CreateSpotCommentRequest;
import bokjak.bokjakserver.domain.comment.dto.CommentDto.UpdateSpotCommentRequest;
import bokjak.bokjakserver.domain.comment.exception.CommentException;
import bokjak.bokjakserver.domain.comment.model.Comment;
import bokjak.bokjakserver.domain.comment.repository.CommentRepository;
import bokjak.bokjakserver.domain.notification.dto.NotificationDto.NotifyParams;
import bokjak.bokjakserver.domain.notification.service.NotificationService;
import bokjak.bokjakserver.domain.spot.exception.SpotException;
import bokjak.bokjakserver.domain.spot.model.Spot;
import bokjak.bokjakserver.domain.spot.repository.SpotRepository;
import bokjak.bokjakserver.domain.user.model.User;
import bokjak.bokjakserver.domain.user.repository.UserRepository;
import bokjak.bokjakserver.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final UserService userService;
    private final SpotRepository spotRepository;
    private final CommentRepository commentRepository;
    private final NotificationService notificationService;

    // 스팟별 댓글 리스트 조회
    public PageResponse<CommentCardResponse> getParentComments(Long currentUserId, Pageable pageable, Long cursorId, Long spotId) {
        Slice<Comment> comments = commentRepository.findAllParentBySpotExceptBlockedAuthors(pageable, cursorId, spotId, currentUserId);
        Long total = commentRepository.countAllParentBySpotExceptBlockedAuthors(spotId, currentUserId);

        Slice<CommentCardResponse> result = comments.map(it -> CommentCardResponse.of(it, currentUserId));

        return PageResponse.of(result, total);
    }

    // 대댓글 리스트 조회
    public PageResponse<CommentCardResponse> getChildComments(Long currentUserId, Pageable pageable, Long cursorId, Long parentId) {
        Slice<Comment> comments = commentRepository.findAllChildrenByParentExceptBlockedAuthors(pageable, cursorId, parentId, currentUserId);
        Long total = commentRepository.countAllChildrenByParentExceptBlockedAuthors(parentId, currentUserId);

        Slice<CommentCardResponse> result = comments.map(it -> CommentCardResponse.of(it, currentUserId));

        return PageResponse.of(result, total);
    }

    // 스팟 댓글 생성
    @Transactional
    public CommentCardResponse createParentComment(Long currentUserId, Long spotId, CreateSpotCommentRequest createSpotCommentRequest) {
        User commentAuthor = userService.getUser(currentUserId);
        Spot spot = spotRepository.findById(spotId)
                .orElseThrow(() -> new SpotException(StatusCode.NOT_FOUND_SPOT));

        Comment comment = commentRepository.save(createSpotCommentRequest.toEntity(commentAuthor, spot));

        User spotAuthor = spot.getUser();
        boolean equals = checkIsSameUser(spotAuthor, commentAuthor);
        if (!equals) {
            notificationService.pushMessage(NotifyParams.ofCreateSpotComment(spotAuthor,spot,comment));
        }

        return CommentCardResponse.of(comment, currentUserId);
    }

    // 대댓글 생성
    @Transactional
    public CommentCardResponse createChildComment(Long currentUserId, Long parentId, CreateSpotCommentRequest createSpotCommentRequest) {
        User user = userService.getUser(currentUserId);
        Comment parent = commentRepository.findById(parentId)
                .orElseThrow(() -> new CommentException(StatusCode.NOT_FOUND_COMMENT));
        // 부모 댓글이 삭제된 경우 (논리적 에러)
        if (!parent.isPresence()) throw new CommentException(StatusCode.NOT_FOUND_COMMENT);

        Comment comment = commentRepository.save(createSpotCommentRequest.toEntity(user, parent));


        Spot spot = parent.getSpot();

        boolean equals = checkIsSameUser(parent.getUser(), user);
        if (!equals) {
            notificationService.pushMessage(NotifyParams.ofCreateSpotCommentComment(parent,spot,comment));
        }
        else {
            List<User> userList = commentRepository.
                    findAllByparentCommentAndDistinctExceptParentAuthor(parentId, parent.getUser().getId());

            userList.forEach(u -> {
                notificationService.pushMessage(NotifyParams.ofCreateSpotCommentComment(
                        u,spot,comment));
                    }
            );
        }
        return CommentCardResponse.of(comment, currentUserId);
    }

    // 스팟 댓글 수정
    @Transactional
    public CommentCardResponse updateSpotComment(Long currentUserId, Long commentId, UpdateSpotCommentRequest updateSpotCommentRequest) {
        User user = userService.getUser(currentUserId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(StatusCode.NOT_FOUND_COMMENT));
        checkIsAuthor(user, comment);

        comment.update(updateSpotCommentRequest.content());

        return CommentCardResponse.of(comment, currentUserId);
    }

    // 스팟 댓글 삭제
    @Transactional
    public CommentMessage deleteSpotComment(Long currentUserId, Long commentId) {
        User user = userService.getUser(currentUserId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(StatusCode.NOT_FOUND_COMMENT));

        checkIsAuthor(user, comment);

        comment.logicallyDelete();

        return new CommentMessage(true);
    }

    private static void checkIsAuthor(User user, Comment comment) {// 작성자인지 확인: 수정, 삭제는 작성자만 권한을 가짐
        if (!comment.getUser().equals(user)) {
            throw new CommentException(StatusCode.NOT_AUTHOR);
        }
    }

    private static boolean checkIsSameUser(User author, User user) {
        return author.getId().equals(user.getId());
    }
}
