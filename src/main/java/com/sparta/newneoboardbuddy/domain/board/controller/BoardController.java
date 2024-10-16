package com.sparta.newneoboardbuddy.domain.board.controller;

import com.sparta.newneoboardbuddy.common.dto.AuthUser;
import com.sparta.newneoboardbuddy.common.dto.response.CommonResponseDto;
import com.sparta.newneoboardbuddy.domain.board.dto.request.BoardRequest;
import com.sparta.newneoboardbuddy.domain.board.dto.request.BoardResponse;
import com.sparta.newneoboardbuddy.domain.board.dto.response.GetBoardResponse;
import com.sparta.newneoboardbuddy.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 보드 생성
    @PostMapping("/board")
    public CommonResponseDto<BoardResponse> createBoard (@AuthenticationPrincipal AuthUser authUser,
                                                         @RequestBody BoardRequest boardRequest) {
        return CommonResponseDto.success(boardService.createBoard(authUser, boardRequest));
    }

    // 보드 수정
    @PutMapping("/board/{boardId}")
    public CommonResponseDto<BoardResponse> updateBoard(@AuthenticationPrincipal AuthUser authUser,
                                                        @PathVariable Long boardId,
                                                        @RequestBody BoardRequest boardRequest) {
        return CommonResponseDto.success(boardService.updateBoard(authUser, boardId, boardRequest));
    }


    // 보드 단건 조회
    @GetMapping("/board/{boardId}")
    public CommonResponseDto<GetBoardResponse> getBoard (@AuthenticationPrincipal AuthUser authUser,
                                                         @PathVariable Long boardId,
                                                         @RequestParam Long spaceId) {
        return CommonResponseDto.success(boardService.getBoard(authUser,spaceId, boardId));
    }


    // 보드 삭제
    // 삭제 시 보드 내의 모든 리스트와 데이터도 삭제됩니다.
    @DeleteMapping("/board/{boardId}")
    public CommonResponseDto<String> deleteBoard(@AuthenticationPrincipal AuthUser authUser,
                                                 @PathVariable Long boardId,
                                                 @RequestParam Long spaceId) {
        boardService.deleteBoard(authUser,spaceId, boardId);
        return CommonResponseDto.success("해당 보드가 삭제되었습니다.");
    }


}
