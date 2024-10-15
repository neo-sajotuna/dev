package com.sparta.newneoboardbuddy.domain.comment.controller;

import com.sparta.newneoboardbuddy.common.dto.AuthUser;
import com.sparta.newneoboardbuddy.common.dto.response.CommonResponseDto;
import com.sparta.newneoboardbuddy.domain.comment.dto.request.CommentUpdateRequestDto;
import com.sparta.newneoboardbuddy.domain.comment.dto.request.CommentSaveRequestDto;
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

    @PostMapping
    public CommonResponseDto<CommentSaveResponseDto> saveComment(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody CommentSaveRequestDto commentSaveRequestDto){
        return CommonResponseDto.success(
                commentService.saveComment(authUser, commentSaveRequestDto)
        );
    }

    @PatchMapping("/{commentId}")
    public CommonResponseDto<?> saveComment(@PathVariable("commentId") Long commentId, @Valid @RequestBody CommentUpdateRequestDto commentUpdateRequestDto){
        return CommonResponseDto.success(
                commentService.updateComment(commentId, commentUpdateRequestDto)
        );
    }

    @DeleteMapping("/{commentId}")
    public CommonResponseDto<?> saveComment(@PathVariable("commentId") Long commentId){
        commentService.deleteComment(commentId);
        return CommonResponseDto.success(null);
    }
}
