package com.sparta.newneoboardbuddy.domain.list.controller;

import com.sparta.newneoboardbuddy.common.dto.AuthUser;
import com.sparta.newneoboardbuddy.common.dto.response.CommonResponseDto;
import com.sparta.newneoboardbuddy.domain.list.dto.request.BoardListDeleteRequest;
import com.sparta.newneoboardbuddy.domain.list.dto.request.BoardListRequest;
import com.sparta.newneoboardbuddy.domain.list.dto.request.BoardListUpdateRequest;
import com.sparta.newneoboardbuddy.domain.list.dto.response.BoardListDeleteResponse;
import com.sparta.newneoboardbuddy.domain.list.dto.response.BoardListResponse;
import com.sparta.newneoboardbuddy.domain.list.service.BoardListService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class BoardListController {

    private final BoardListService boardListService;

    /**
     * List 생성하는 Controller 메서드
     * @param authUser Filter에서 인증된 유저 정보
     * @param boardListRequest List 생성에 필요한 정보가 담긴 Request 객체
     * @return 리스트 생성에 성공시 : 200 OK + 생성된 List 정보 / 그 외 : ErrorCode + Description
     */
    @PostMapping("/list")
    public CommonResponseDto<BoardListResponse> creatList(@AuthenticationPrincipal AuthUser authUser, @RequestBody BoardListRequest boardListRequest) {
        return CommonResponseDto.success(boardListService.createList(authUser, boardListRequest));
    }

    /**
     * List를 수정하는 Controller 메서드
     * @param authUser Filter에서 인증된 유저 정보
     * @param listId 수정할 List Id
     * @param boardListUpdateRequest List 수정에 필요한 정보가 담긴 Request 객체
     * @return List 수정에 성공시 : 200 OK + 수정된 List 정보 / 그 외 : ErrorCode + Description
     */
    @PutMapping("/list/{listId}")
    public CommonResponseDto<BoardListResponse> updateList(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long listId, @RequestBody BoardListUpdateRequest boardListUpdateRequest) {
        return CommonResponseDto.success(boardListService.updateList(authUser, listId, boardListUpdateRequest));
    }

    /**
     * List를 삭제하는 Controller 메서드
     * @param authUser Filter에서 인증된 유저 정보
     * @param listId 삭제할 List Id
     * @param boardListDeleteRequest List 삭제에 필요한 정보가 담긴 Request 객체
     * @return List 삭제에 성공시 : 200 OK + 삭제된 List Id / 그 외 : ErrorCode + Description
     */
    @DeleteMapping("/list/{listId}")
    public CommonResponseDto<BoardListDeleteResponse> deleteList(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long listId, @RequestBody BoardListDeleteRequest boardListDeleteRequest) {
        return CommonResponseDto.success(boardListService.deleteList(authUser, listId, boardListDeleteRequest));
    }
}
