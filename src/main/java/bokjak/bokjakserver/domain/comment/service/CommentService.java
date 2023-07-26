package bokjak.bokjakserver.domain.comment.service;

import bokjak.bokjakserver.common.dto.PageResponse;
import bokjak.bokjakserver.domain.comment.dto.CommentDto.CommentCardResponse;
import bokjak.bokjakserver.domain.comment.model.Comment;
import bokjak.bokjakserver.domain.comment.repository.CommentRepository;
import bokjak.bokjakserver.domain.spot.dto.SpotDto;
import bokjak.bokjakserver.domain.spot.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final SpotRepository spotRepository;
    private final CommentRepository commentRepository;

    // 스팟별 댓글 리스트 조회
    public PageResponse<CommentCardResponse> getSpotComments(Long currentUserId, Pageable pageable, Long cursorId, Long spotId) {
        Page<Comment> comments = commentRepository.findAllBySpotExceptBlockedAuthors(pageable, cursorId, spotId, currentUserId);

        Page<CommentCardResponse> resultPage = comments
                .map(it -> CommentCardResponse.of(it, it.getUser().getId().equals(currentUserId))); // 작성자 여부
        return PageResponse.of(resultPage);
    }

    // 스팟 댓글 생성
    // 스팟 댓글 수정
    // 스팟 댓글 삭제

}
