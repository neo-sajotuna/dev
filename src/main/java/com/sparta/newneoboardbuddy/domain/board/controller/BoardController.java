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

    /**
     * 보드를 생성할 Controller 메서드
     * @param authUser Filter에서 인증된 유저 정보
     * @param boardRequest 보드 생성에 필요한 Request
     * @return 보드 생성에 성공시 : 200 OK + 생성된 보드 정보 / 그 외 : ErrorCode + Description
     */
    @PostMapping("/board")
    public CommonResponseDto<BoardResponse> createBoard (@AuthenticationPrincipal AuthUser authUser,
                                                         @RequestBody BoardRequest boardRequest) {
        return CommonResponseDto.success(boardService.createBoard(authUser, boardRequest));
    }

    /**
     * 보드를 수정할 Controller 메서드
     * @param authUser Filter에서 인증된 유저 정보
     * @param boardId 수정할 boardId
     * @param boardRequest 보드 생성에 필요한 Request
     * @return 보드 수정에 성공시 : 200 OK + 생성된 보드 정보 / 그 외 : ErrorCode + Description
     */
    @PutMapping("/board/{boardId}")
    public CommonResponseDto<BoardResponse> updateBoard(@AuthenticationPrincipal AuthUser authUser,
                                                        @PathVariable Long boardId,
                                                        @RequestBody BoardRequest boardRequest) {
        return CommonResponseDto.success(boardService.updateBoard(authUser, boardId, boardRequest));
    }


    /**
     * 보드를 조회할 Controller 메서드
     * @param authUser Filter에서 인증된 유저 정보
     * @param boardId 조회할 boardId
     * @param spaceId 해당 user와 board가 속해 있는지 확인할 space Id
     * @return 보드 조회에 성공시 : 200 OK + 조회된 보드 정보 / 그 외 : ErrorCode + Description
     */
    @GetMapping("/board/{boardId}")
    public CommonResponseDto<GetBoardResponse> getBoard (@AuthenticationPrincipal AuthUser authUser,
                                                         @PathVariable Long boardId,
                                                         @RequestParam Long spaceId) {
        return CommonResponseDto.success(boardService.getBoard(authUser,spaceId, boardId));
    }


    /**
     * 보드를 삭제할 Controller 메서드
     * @param authUser Filter에서 인증된 유저 정보
     * @param boardId 삭제할 boardId
     * @param spaceId 해당 user와 board가 속해 있는지 확인할 space Id
     * @return 삭제 성공시 : 200 OK + 메시지 / 그 외 : ErrorCode + Description
     */
    @DeleteMapping("/board/{boardId}")
    public CommonResponseDto<String> deleteBoard(@AuthenticationPrincipal AuthUser authUser,
                                                 @PathVariable Long boardId,
                                                 @RequestParam Long spaceId) {
        boardService.deleteBoard(authUser,spaceId, boardId);
        return CommonResponseDto.success("해당 보드가 삭제되었습니다.");
    }


}
