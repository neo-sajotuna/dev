package com.sparta.newneoboardbuddy.domain.list.controller;

import com.sparta.newneoboardbuddy.common.dto.AuthUser;
import com.sparta.newneoboardbuddy.domain.list.dto.request.BoardListRequest;
import com.sparta.newneoboardbuddy.domain.list.dto.response.BoardListResponse;
import com.sparta.newneoboardbuddy.domain.list.service.BoardListService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class BoardListController {

    private final BoardListService boardListService;

    @PostMapping("/list")
    public BoardListResponse creatList(@AuthenticationPrincipal AuthUser authUser, @RequestBody BoardListRequest boardListRequest) {
        BoardListResponse response = boardListService.createList(authUser, boardListRequest);
        return response;
    }
}
