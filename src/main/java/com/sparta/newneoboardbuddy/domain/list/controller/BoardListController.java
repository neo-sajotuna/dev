package com.sparta.newneoboardbuddy.domain.list.controller;

import com.sparta.newneoboardbuddy.common.dto.AuthUser;
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

    @PostMapping("/list")
    public BoardListResponse creatList(@AuthenticationPrincipal AuthUser authUser, @RequestBody BoardListRequest boardListRequest) {
        return boardListService.createList(authUser, boardListRequest);
    }

    @PutMapping("/list/{listId}")
    public BoardListResponse updateList(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long listId, @RequestBody BoardListUpdateRequest boardListUpdateRequest) {
        return boardListService.updateList(authUser, listId, boardListUpdateRequest);
    }

    @DeleteMapping("/list/{listId}")
    public BoardListDeleteResponse deleteList(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long listId, @RequestBody BoardListDeleteRequest boardListDeleteRequest) {
        return boardListService.deleteList(authUser, listId, boardListDeleteRequest);
    }
}
