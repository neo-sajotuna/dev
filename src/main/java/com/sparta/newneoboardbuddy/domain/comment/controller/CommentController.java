package com.sparta.newneoboardbuddy.domain.comment.controller;

import com.sparta.newneoboardbuddy.common.dto.AuthUser;
import com.sparta.newneoboardbuddy.common.dto.response.CommonResponseDto;
import com.sparta.newneoboardbuddy.domain.comment.dto.request.CommentSaveRequestDto;
import com.sparta.newneoboardbuddy.domain.comment.dto.request.CommentUpdateRequestDto;
import com.sparta.newneoboardbuddy.domain.comment.dto.response.CommentSaveResponseDto;
import com.sparta.newneoboardbuddy.domain.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글을 작성하는 Controller 메서드
     * @param authUser Filter에서 인증된 유저 정보
     * @param commentSaveRequestDto 댓글 작성에 필요한 Request 객체
     * @return 댓글 작성 성공시 : 200 OK + 댓글 내용 / 그 외 : ErrorCode + Description
     */
    @PostMapping
    public CommonResponseDto<CommentSaveResponseDto> saveComment(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody CommentSaveRequestDto commentSaveRequestDto){
        return CommonResponseDto.success(
                commentService.saveComment(authUser, commentSaveRequestDto)
        );
    }

    /**
     * 댓글을 수정하는 Controller 메서드
     * @param authUser Filter에서 인증된 유저 정보
     * @param commentId 수정할 댓글 Id
     * @param commentUpdateRequestDto 댓글 수정에 필요한 Request 객체
     * @return 댓글 수정 성공시 : 200 OK + 댓글 내용 / 그 외 : ErrorCode + Description
     */
    @PatchMapping("/{commentId}")
    public CommonResponseDto<?> saveComment(@AuthenticationPrincipal AuthUser authUser, @PathVariable("commentId") Long commentId, @Valid @RequestBody CommentUpdateRequestDto commentUpdateRequestDto){
        return CommonResponseDto.success(
                commentService.updateComment(authUser, commentId, commentUpdateRequestDto)
        );
    }

    /**
     * 댓글을 삭제하는 Controller 메서드
     * @param authUser Filter에서 인증된 유저 정보
     * @param commentId 삭제할 댓글 Id
     * @return 댓글 삭제 성공시 : 200 OK / 그 외 : ErrorCode + Description
     */
    @DeleteMapping("/{commentId}")
    public CommonResponseDto<?> saveComment(@AuthenticationPrincipal AuthUser authUser, @PathVariable("commentId") Long commentId){
        commentService.deleteComment(authUser, commentId);
        return CommonResponseDto.success(null);
    }
}
