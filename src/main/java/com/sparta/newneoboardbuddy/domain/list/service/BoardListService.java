package com.sparta.newneoboardbuddy.domain.list.service;

import com.sparta.newneoboardbuddy.common.dto.AuthUser;
import com.sparta.newneoboardbuddy.config.HierarchyUtil;
import com.sparta.newneoboardbuddy.domain.auth.exception.AuthException;
import com.sparta.newneoboardbuddy.domain.board.entity.Board;
import com.sparta.newneoboardbuddy.domain.list.dto.request.BoardListRequest;
import com.sparta.newneoboardbuddy.domain.list.dto.response.BoardListResponse;
import com.sparta.newneoboardbuddy.domain.list.entity.BoardList;
import com.sparta.newneoboardbuddy.domain.list.repository.BoardListRepository;
import com.sparta.newneoboardbuddy.domain.member.entity.Member;
import com.sparta.newneoboardbuddy.domain.member.enums.MemberRole;
import com.sparta.newneoboardbuddy.domain.member.service.MemberService;
import com.sparta.newneoboardbuddy.domain.workspace.service.WorkspaceService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardListService {

    private final MemberService memberService;
    private final WorkspaceService workspaceService;
    private final BoardListRepository boardListRepository;
    private final HierarchyUtil hierarchyUtil;

    @Transactional
    public BoardListResponse createList(AuthUser authUser, BoardListRequest boardListRequest) {
        // 해당 유저가 워크스페이스 유저인지 확인
        Member member = memberService.memberInWorkspaceFetchWorkspace(authUser, boardListRequest.getWorkspaceId());
     
        if (member.getMemberRole().equals(MemberRole.READ_ONLY_MEMBER)) {
            throw new AuthException("해당 유저는 쓰기 권한이 없습니다.");
        }

        Board board = hierarchyUtil.getBoardListUseMember(member, boardListRequest.getBoardId());
        BoardList boardList = new BoardList(
                0L,
                boardListRequest.getTitle(),
                (long) board.getBoardLists().size(),
                board,
                null);

        return new BoardListResponse(boardListRepository.save(boardList));
    }
}
