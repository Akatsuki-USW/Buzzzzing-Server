package bokjak.bokjakserver.domain.comment.controller;

import bokjak.bokjakserver.common.constant.SwaggerConstants;
import bokjak.bokjakserver.common.dto.ApiResponse;
import bokjak.bokjakserver.common.dto.PageResponse;
import bokjak.bokjakserver.config.security.PrincipalDetails;
import bokjak.bokjakserver.domain.comment.dto.CommentDto.CommentCardResponse;
import bokjak.bokjakserver.domain.comment.dto.CommentDto.CreateSpotCommentRequest;
import bokjak.bokjakserver.domain.comment.dto.CommentDto.UpdateSpotCommentRequest;
import bokjak.bokjakserver.domain.comment.service.CommentService;
import bokjak.bokjakserver.domain.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    @PostMapping("/{spotId}/comments")
    @Operation(summary = SwaggerConstants.COMMENT_CREATE, description = SwaggerConstants.COMMENT_CREATE_DESCRIPTION)
    public ApiResponse<CommentCardResponse> createSpotComment(
            @PathVariable Long spotId,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Valid @RequestBody CreateSpotCommentRequest createSpotCommentRequest
    ) {
        authService.checkIsBannedUser(principalDetails.getUser());
        CommentCardResponse response = commentService.createSpotComment(principalDetails.getUserId(), spotId, createSpotCommentRequest);
        return success(response);
    }

    // 스팟 댓글 수정
    @PutMapping("/comments/{commentId}")
    @Operation(summary = SwaggerConstants.COMMENT_UPDATE, description = SwaggerConstants.COMMENT_UPDATE_DESCRIPTION)
    public ApiResponse<CommentCardResponse> updateSpotComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Valid @RequestBody UpdateSpotCommentRequest updateSpotCommentRequest
    ) {
        authService.checkIsBannedUser(principalDetails.getUser());
        CommentCardResponse response = commentService.updateSpotComment(principalDetails.getUserId(), commentId, updateSpotCommentRequest);
        return success(response);
    }
    // 스팟 댓글 삭제

}
