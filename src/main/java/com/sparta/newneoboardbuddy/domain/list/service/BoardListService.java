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
    private final BoardListRepository boardListRepository;
    private final HierarchyUtil hierarchyUtil;

    /**
     * 해당 유저가 속해 있는 Board에 List를 생성해주는 메서드
     * @param authUser Filter에서 인증된 유저 정보
     * @param boardListRequest Board생성에 필요한 내용이 담긴 Request
     * @return 생성된 List에 대한 정보가 담긴 Dto
     */
    @Transactional
    public BoardListResponse createList(AuthUser authUser, BoardListRequest boardListRequest) {
        // 해당 유저가 워크스페이스 유저인지 확인
        Member member = getMemberInWorkspace(authUser, boardListRequest.getWorkspaceId());
        Board board = boardService.getBoardFetchJoinToWorkspace(boardListRequest.getBoardId());

        // 해당 보드가 워크 스페이스 내에 있는지 확인
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

    /**
     * List를 수정( 이동 포함 ) 시켜주는 메서드
     * @param authUser Filter에서 인증된 유저 정보
     * @param listId 수정할 List Id
     * @param boardListUpdateRequest 수정에 필요한 정보가 담긴 Request Dto
     * @return 변경된 List 정보가 담긴 Dto객체
     */
    @Transactional
    public BoardListResponse updateList(AuthUser authUser, Long listId, BoardListUpdateRequest boardListUpdateRequest) {
        // 해당 유저가 워크스페이스 유저인지 확인
        Member member = getMemberInWorkspace(authUser, boardListUpdateRequest.getWorkspaceId());
        BoardList findBoardList = getBoardListInWorkspace(listId, boardListUpdateRequest.getWorkspaceId());

        // 만약 위치가 이동된 경우
        if (!findBoardList.getListIndex().equals(boardListUpdateRequest.getIndex())) {
            BoardList findOtherList = boardListRepository.findByBoardAndListIndex(findBoardList.getBoard(), boardListUpdateRequest.getIndex())
                   .orElseThrow(()-> new NoSuchElementException("빈 공간에는 List를 배치할 수 없습니다."));

            // 두 List의 위치를 변환한다.
            swapIndices(findBoardList, findOtherList);
        }
        findBoardList.update(boardListUpdateRequest.getTitle());

        return new BoardListResponse(boardListRepository.save(findBoardList));
    }

    /**
     * Board에서 List를 삭제시켜주는 메서드
     * @param authUser Filter에서 로그인된 유저 정보
     * @param listId 삭제할 List Id
     * @param boardListDeleteRequest 해당 List를 삭제하는데 필요한 정보가 담긴 Dto
     * @return 삭제된 List Id가 담긴 Dto 객체
     */
    @Transactional
    public BoardListDeleteResponse deleteList(AuthUser authUser, Long listId, BoardListDeleteRequest boardListDeleteRequest) {
        Member member = memberService.memberInWorkspaceFetchWorkspace(authUser, boardListDeleteRequest.getWorkspaceId());
        BoardList boardList = getBoardListInWorkspace(listId, boardListDeleteRequest.getWorkspaceId());

        boardListRepository.delete(boardList);
        return new BoardListDeleteResponse(listId);
    }

    /**
     * 해당 유저가 다음과 같은 조건을 만족하는지 확인 후 반환하는 메서드
     *  1. 유저가 해당 workspace 소속이어야 한다.
     *  2. 해당 유저는 읽기 권한이 아니여야 한다.
     * @param authUser Member정보를 탐색할 때 사용할 로그인된 유저 정보
     * @param workspaceId 해당 workspace 소속인지 확인할 workspace Id
     * @return Workspace까지 fetch Join이 완료된 로그인된 유저와 관련된 Member 객체
     */
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
     * 해당 Id를 가진 List가 Workspace에 속할 경우, 찾아 반환하는 메서드
     * @param boardlistId 찾고자 하는 BoardList
     * @param workspaceId 속하는지 확인할 workspace Id
     * @return Workspace에 속해 있는 Workspace까지 fetch join이 된 boardList 객체
     */
    private BoardList getBoardListInWorkspace(Long boardlistId, Long workspaceId) {
        BoardList boardList = boardListRepository.findByIdWithJoinFetchToWorkspace(boardlistId)
                .orElseThrow(()-> new NoSuchElementException("존재하지 않는 List입니다."));

        if (!hierarchyUtil.isListInWorkspace(workspaceId, boardList)) {
            throw new IllegalArgumentException("Workspace에 존재하지 않는 List입니다");
        }

        return boardList;
    }
}
