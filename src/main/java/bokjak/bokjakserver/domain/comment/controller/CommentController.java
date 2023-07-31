package bokjak.bokjakserver.domain.comment.controller;

import bokjak.bokjakserver.common.constant.SwaggerConstants;
import bokjak.bokjakserver.common.dto.ApiResponse;
import bokjak.bokjakserver.common.dto.PageResponse;
import bokjak.bokjakserver.config.security.PrincipalDetails;
import bokjak.bokjakserver.domain.comment.dto.CommentDto.*;
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
    @Operation(summary = SwaggerConstants.COMMENT_GET_ALL_CHILD, description = SwaggerConstants.COMMENT_GET_ALL_PARENT_DESCRIPTION)
    public ApiResponse<PageResponse<ParentCommentCardResponse>> getParentComments(
            @PathVariable Long spotId,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) Long cursorId
    ) {
        PageResponse<ParentCommentCardResponse> pageResponse = commentService.getParentComments(principalDetails.getUserId(), pageable, cursorId, spotId);
        return success(pageResponse);
    }

    // 대댓글 리스트 조회
    @GetMapping("/comments/{parentId}")
    @Operation(summary = SwaggerConstants.COMMENT_GET_ALL_CHILD, description = SwaggerConstants.COMMENT_GET_ALL_CHILD_DESCRIPTION)
    public ApiResponse<PageResponse<ChildCommentCardResponse>> getChildComments(
            @PathVariable Long parentId,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) Long cursorId
    ) {
        PageResponse<ChildCommentCardResponse> pageResponse = commentService.getChildComments(principalDetails.getUserId(), pageable, cursorId, parentId);
        return success(pageResponse);
    }

    // 스팟 댓글 생성
    @PostMapping("/{spotId}/comments")
    @Operation(summary = SwaggerConstants.COMMENT_CREATE, description = SwaggerConstants.COMMENT_CREATE_DESCRIPTION)
    public ApiResponse<ParentCommentCardResponse> createSpotComment(
            @PathVariable Long spotId,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Valid @RequestBody CreateSpotCommentRequest createSpotCommentRequest
    ) {
        authService.checkIsBannedUser(principalDetails.getUser());
        ParentCommentCardResponse response = commentService.createSpotComment(principalDetails.getUserId(), spotId, createSpotCommentRequest);
        return success(response);
    }

    // 스팟 댓글 수정
    @PutMapping("/comments/{commentId}")
    @Operation(summary = SwaggerConstants.COMMENT_UPDATE, description = SwaggerConstants.COMMENT_UPDATE_DESCRIPTION)
    public ApiResponse<ParentCommentCardResponse> updateSpotComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Valid @RequestBody UpdateSpotCommentRequest updateSpotCommentRequest
    ) {
        authService.checkIsBannedUser(principalDetails.getUser());
        ParentCommentCardResponse response = commentService.updateSpotComment(principalDetails.getUserId(), commentId, updateSpotCommentRequest);
        return success(response);
    }

    // 스팟 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    @Operation(summary = SwaggerConstants.COMMENT_DELETE, description = SwaggerConstants.COMMENT_DELETE_DESCRIPTION)
    public ApiResponse<CommentMessage> deleteSpotComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        authService.checkIsBannedUser(principalDetails.getUser());
        CommentMessage message = commentService.deleteSpotComment(principalDetails.getUserId(), commentId);
        return success(message);
    }
}
