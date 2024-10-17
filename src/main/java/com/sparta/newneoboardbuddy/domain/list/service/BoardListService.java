package com.sparta.newneoboardbuddy.domain.list.service;

import com.sparta.newneoboardbuddy.common.dto.AuthUser;
import com.sparta.newneoboardbuddy.config.HierarchyUtil;
import com.sparta.newneoboardbuddy.domain.auth.exception.AuthException;
import com.sparta.newneoboardbuddy.domain.board.entity.Board;
import com.sparta.newneoboardbuddy.domain.board.service.BoardService;
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

    private final BoardService boardService;
    private final MemberService memberService;
    private final WorkspaceService workspaceService;
    private final BoardListRepository boardListRepository;
    private final HierarchyUtil hierarchyUtil;

    @Transactional
    public BoardListResponse createList(AuthUser authUser, BoardListRequest boardListRequest) {
        // 해당 유저가 워크스페이스 유저인지 확인
        Member member = getMemberInWorkspace(authUser, boardListRequest.getWorkspaceId());
        Board board = boardService.getBoardFetchJoinToWorkspace(boardListRequest.getBoardId());

        if (!hierarchyUtil.isBoardInWorkspace(boardListRequest.getWorkspaceId(), board)) {
            throw new IllegalArgumentException("Workspace에 존재하지 않는 Board입니다");
        }

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
        BoardList findBoardList = getBoardListInWorkspace(listId, boardListUpdateRequest.getWorkspaceId());

        if (!findBoardList.getListIndex().equals(boardListUpdateRequest.getIndex())) {
           BoardList findOtherList = boardListRepository.findByBoardAndListIndex(findBoardList.getBoard(), boardListUpdateRequest.getIndex())
                   .orElseThrow(()-> new NoSuchElementException("빈 공간에는 List를 배치할 수 없습니다."));

            swapIndices(findBoardList, findOtherList);
        }

        findBoardList.update(boardListUpdateRequest.getTitle());

        return new BoardListResponse(boardListRepository.save(findBoardList));
    }

    @Transactional
    public BoardListDeleteResponse deleteList(AuthUser authUser, Long listId, BoardListDeleteRequest boardListDeleteRequest) {
        Member member = memberService.memberInWorkspaceFetchWorkspace(authUser, boardListDeleteRequest.getWorkspaceId());
        BoardList boardList = getBoardListInWorkspace(listId, boardListDeleteRequest.getWorkspaceId());

        boardListRepository.delete(boardList);
        return new BoardListDeleteResponse(listId);
    }

    private Member getMemberInWorkspace(AuthUser authUser, Long workspaceId) {
        Member member = memberService.memberInWorkspaceFetchWorkspace(authUser, workspaceId);

        if (member.getMemberRole().equals(MemberRole.READ_ONLY_MEMBER)) {
            throw new AuthException("해당 유저는 읽기 권한 상태 입니다.");
        }

        return member;
    }

    /**
     * 두 BoardList의 인덱스를 교체하는 메서드
     * @param a 바꿀 객체 A
     * @param b 상대 객체 B
     */
    private void swapIndices(BoardList a, BoardList b) {
        Long tmp = a.getListIndex();

        a.swapIndex(b.getListIndex());
        b.swapIndex(tmp);
    }

    /**
     * 해당 BoardList가 Workspace에 속할 경우 반환하는 메서드
     * @param boardlistId 찾고자 하는 BoardList
     * @param workspaceId 속하는지 확인할 workspace Id
     * @return Workspace에 속하고 있는 boardList 객체
     */
    private BoardList getBoardListInWorkspace(Long boardlistId, Long workspaceId) {
        BoardList boardList = boardListRepository.findByIdWithJoinFetchToWorkspace(boardlistId)
                .orElseThrow(()-> new NoSuchElementException("존재하지 않는 List입니다."));

        if (hierarchyUtil.isListInWorkspace(workspaceId, boardList)) {
            throw new IllegalArgumentException("Workspace에 존재하지 않는 List입니다");
        }

        return boardList;
    }
}
