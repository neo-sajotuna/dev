package com.sparta.newneoboardbuddy.domain.list.service;

import com.sparta.newneoboardbuddy.common.dto.AuthUser;
import com.sparta.newneoboardbuddy.config.HierarchyUtil;
import com.sparta.newneoboardbuddy.domain.auth.exception.AuthException;
import com.sparta.newneoboardbuddy.domain.board.entity.Board;
import com.sparta.newneoboardbuddy.domain.list.dto.request.BoardListDeleteRequest;
import com.sparta.newneoboardbuddy.domain.list.dto.request.BoardListRequest;
import com.sparta.newneoboardbuddy.domain.list.dto.request.BoardListUpdateRequest;
import com.sparta.newneoboardbuddy.domain.list.dto.response.BoardListDeleteResponse;
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

import java.util.NoSuchElementException;

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
        Member member = getMemberInWorkspace(authUser, boardListRequest.getWorkspaceId());
        Board board = hierarchyUtil.getBoardListUseMember(member, boardListRequest.getBoardId());

        BoardList boardList = new BoardList(
                0L,
                boardListRequest.getTitle(),
                (long) board.getBoardLists().size(),
                board,
                null);

        return new BoardListResponse(boardListRepository.save(boardList));
    }

    @Transactional
    public BoardListResponse updateList(AuthUser authUser, Long listId, BoardListUpdateRequest boardListUpdateRequest) {
        // 해당 유저가 워크스페이스 유저인지 확인
        Member member = getMemberInWorkspace(authUser, boardListUpdateRequest.getWorkspaceId());
        BoardList findBoardList = boardListRepository.findById(listId)
                .orElseThrow(()-> new NoSuchElementException("존재하지 않는 List입니다."));

        if (!findBoardList.getListIndex().equals(boardListUpdateRequest.getIndex())) {
           BoardList findOtherList = boardListRepository.findByBoardAndListIndex(findBoardList.getBoard(), boardListUpdateRequest.getIndex())
                   .orElseThrow(()-> new NoSuchElementException("빈 공간에는 List를 배치할 수 없습니다."));

            swapIndices(findBoardList, findOtherList);
        }

        findBoardList.update(boardListUpdateRequest.getTitle());

        return new BoardListResponse(boardListRepository.save(findBoardList));
    }

    public BoardListDeleteResponse deleteList(AuthUser authUser, Long listId, BoardListDeleteRequest boardListDeleteRequest) {
        Member member = memberService.memberInWorkspaceFetchWorkspace(authUser, boardListDeleteRequest.getWorkspaceId());

        return null;
    }

    private Member getMemberInWorkspace(AuthUser authUser, Long workspaceId) {
        Member member = memberService.memberInWorkspaceFetchWorkspace(authUser, workspaceId);

        if (member.getMemberRole().equals(MemberRole.READ_ONLY_MEMBER)) {
            throw new AuthException("해당 유저는 읽기 권한 상태 입니다.");
        }

        return member;
    }

    private void swapIndices(BoardList a, BoardList b) {
        Long tmp = a.getListIndex();

        a.swapIndex(b.getListIndex());
        b.swapIndex(tmp);
    }
}
