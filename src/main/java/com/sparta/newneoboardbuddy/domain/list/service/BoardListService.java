package com.sparta.newneoboardbuddy.domain.list.service;

import com.sparta.newneoboardbuddy.common.dto.AuthUser;
import com.sparta.newneoboardbuddy.domain.list.dto.request.BoardListRequest;
import com.sparta.newneoboardbuddy.domain.list.dto.response.BoardListResponse;
import com.sparta.newneoboardbuddy.domain.list.entity.BoardList;
import org.springframework.stereotype.Service;

@Service
public class BoardListService {

    public BoardListResponse createList(AuthUser authUser, BoardListRequest boardListRequest) {
        return new BoardListResponse();



    }
}
