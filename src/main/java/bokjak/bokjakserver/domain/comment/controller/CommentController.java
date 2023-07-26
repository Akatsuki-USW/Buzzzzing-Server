package bokjak.bokjakserver.domain.comment.controller;

import bokjak.bokjakserver.common.constant.SwaggerConstants;
import bokjak.bokjakserver.common.dto.ApiResponse;
import bokjak.bokjakserver.common.dto.PageResponse;
import bokjak.bokjakserver.config.security.PrincipalDetails;
import bokjak.bokjakserver.domain.comment.dto.CommentDto.CommentCardResponse;
import bokjak.bokjakserver.domain.comment.service.CommentService;
import bokjak.bokjakserver.domain.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static bokjak.bokjakserver.common.dto.ApiResponse.success;

@Slf4j
@RestController
@RequestMapping("/locations/spots")
@RequiredArgsConstructor
@SecurityRequirement(name = SwaggerConstants.SECURITY_SCHEME_NAME)
@Tag(name = SwaggerConstants.TAG_COMMENT, description = SwaggerConstants.TAG_COMMENT_DESCRIPTION)
public class CommentController {
    private final CommentService commentService;
    private final AuthService authService;

    // 스팟별 댓글 리스트 조회
    @GetMapping("/{spotId}/comments")
    @Operation(summary = SwaggerConstants.COMMENT_GET_ALL, description = SwaggerConstants.COMMENT_GET_ALL_DESCRIPTION)
    public ApiResponse<PageResponse<CommentCardResponse>> getSpotComments(
            @PathVariable Long spotId,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) Long cursorId
    ) {
        PageResponse<CommentCardResponse> pageResponse = commentService.getSpotComments(principalDetails.getUserId(), pageable, cursorId, spotId);
        return success(pageResponse);
    }

    // 스팟 댓글 생성
    // 스팟 댓글 수정
    // 스팟 댓글 삭제

}
