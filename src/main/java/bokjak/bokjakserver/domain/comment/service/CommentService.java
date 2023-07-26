package bokjak.bokjakserver.domain.comment.service;

import bokjak.bokjakserver.common.dto.PageResponse;
import bokjak.bokjakserver.common.exception.StatusCode;
import bokjak.bokjakserver.domain.comment.dto.CommentDto.CommentCardResponse;
import bokjak.bokjakserver.domain.comment.dto.CommentDto.CreateSpotCommentRequest;
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
    public PageResponse<CommentCardResponse> getSpotComments(Long currentUserId, Pageable pageable, Long cursorId, Long spotId) {
        Page<Comment> comments = commentRepository.findAllBySpotExceptBlockedAuthors(pageable, cursorId, spotId, currentUserId);

        Page<CommentCardResponse> resultPage = comments
                .map(it -> CommentCardResponse.of(it, it.getUser().getId().equals(currentUserId))); // 작성자 여부
        return PageResponse.of(resultPage);
    }

    @Transactional
    public CommentCardResponse createSpotComment(Long currentUserId, Long spotId, CreateSpotCommentRequest createSpotCommentRequest) {
        User user = userService.getUser(currentUserId);
        Spot spot = spotRepository.findById(spotId)
                .orElseThrow(() -> new SpotException(StatusCode.NOT_FOUND_SPOT));

        Comment comment = commentRepository.save(createSpotCommentRequest.toEntity(user, spot));
        boolean isAuthor = comment.getUser().equals(user);  // 작성자 여부(always true)

        return CommentCardResponse.of(comment, isAuthor);
    }

    // 스팟 댓글 생성
    // 스팟 댓글 수정
    // 스팟 댓글 삭제

}
