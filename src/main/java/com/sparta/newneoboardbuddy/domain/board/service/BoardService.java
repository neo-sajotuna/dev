package com.sparta.newneoboardbuddy.domain.board.service;

import com.sparta.newneoboardbuddy.common.dto.AuthUser;
import com.sparta.newneoboardbuddy.domain.board.dto.request.BoardRequest;
import com.sparta.newneoboardbuddy.domain.board.dto.request.BoardResponse;
import com.sparta.newneoboardbuddy.domain.board.dto.response.GetBoardListResponse;
import com.sparta.newneoboardbuddy.domain.board.dto.response.GetBoardResponse;
import com.sparta.newneoboardbuddy.domain.board.dto.response.GetCardResponse;
import com.sparta.newneoboardbuddy.domain.board.entity.Board;
import com.sparta.newneoboardbuddy.domain.board.exception.BlankTitleException;
import com.sparta.newneoboardbuddy.domain.board.exception.BoardNotFoundException;
import com.sparta.newneoboardbuddy.domain.board.repository.BoardRepository;
import com.sparta.newneoboardbuddy.domain.card.entity.Card;
import com.sparta.newneoboardbuddy.domain.list.entity.BoardList;
import com.sparta.newneoboardbuddy.domain.member.entity.Member;
import com.sparta.newneoboardbuddy.domain.member.enums.MemberRole;
import com.sparta.newneoboardbuddy.domain.member.rpository.MemberRepository;
import com.sparta.newneoboardbuddy.domain.user.entity.User;
import com.sparta.newneoboardbuddy.domain.workspace.entity.Workspace;
import com.sparta.newneoboardbuddy.domain.workspace.exception.UnauthorizedActionException;
import com.sparta.newneoboardbuddy.domain.workspace.exception.WorkspaceNotFoundException;
import com.sparta.newneoboardbuddy.domain.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final WorkspaceRepository workspaceRepository;
    private final MemberRepository memberRepository;

    // 보드 생성
    @Transactional
    public BoardResponse createBoard(AuthUser authUser, BoardRequest boardRequest) {
        // 로그인하지 않은 멤버가 보드를 생성하려는 경우
        if (authUser == null) {
            throw new UnauthorizedActionException("로그인하지 않은 사용자는 보드를 생성할 수 없습니다.");
        }

        User user = User.fromAuthUser(authUser);

        // 해당 워크스페이스 아이디의 워크스페이스가 없을 경우
        Workspace workspace = workspaceRepository.findById(boardRequest.getSpaceId())
                .orElseThrow(() -> new WorkspaceNotFoundException("해당 워크스페이스가 없습니다"));

        // 제목이 비어 있는 경우
        if(boardRequest.getBoardTitle() == null || boardRequest.getBoardTitle().isEmpty()) {
            throw new BlankTitleException("보드의 제목을 입력해주세요.");
        }

        // 워크스페이스와 유저가 연결된 멤버 정보를 조회
        Member member = memberRepository.findByUserAndWorkspace(user, workspace)
                .orElseThrow(() -> new UnauthorizedActionException("해당 워크스페이스에 대한 권한이 없습니다."));

        // 읽기 전용 역할을 가진 멤버가 보드를 생성/수정하려는 경우
        if (member.getMemberRole() == MemberRole.READ_ONLY_MEMBER) {
            throw new UnauthorizedActionException("읽기 전용 멤버라 보드를 생성할 수 없습니다.");
        }

        Board board = new Board(boardRequest, workspace);
        boardRepository.save(board);
        return new BoardResponse(board);
    }


    // 보드 수정
    @Transactional
    public BoardResponse updateBoard(AuthUser authUser, Long boardId, BoardRequest boardRequest) {
        // 로그인하지 않은 멤버가 보드를 생성하려는 경우
        if (authUser == null) {
            throw new UnauthorizedActionException("로그인하지 않은 사용자는 보드를 생성할 수 없습니다.");
        }

        User user = User.fromAuthUser(authUser);

        Workspace workspace = workspaceRepository.findById(boardRequest.getSpaceId())
                .orElseThrow(() -> new WorkspaceNotFoundException("해당 워크스페이스가 없습니다."));

        Board board = boardRepository.findByBoardIdAndWorkspace(boardId, workspace)
                .orElseThrow(() -> new BoardNotFoundException("해당 보드가 존재하지 않습니다."));

        // 제목이 비어 있는 경우
        if(boardRequest.getBoardTitle()==null || boardRequest.getBoardTitle().isEmpty()) {
            throw new BlankTitleException("보드의 제목을 입력해주세요.");
        }

        // 워크스페이스와 유저가 연결된 멤버 정보를 조회
        Member member = memberRepository.findByUserAndWorkspace(user, workspace)
                .orElseThrow(() -> new UnauthorizedActionException("해당 워크스페이스에 대한 권한이 없습니다."));

        // 읽기 전용 역할을 가진 멤버가 보드를 생성/수정하려는 경우
        if (member.getMemberRole() == MemberRole.READ_ONLY_MEMBER) {
            throw new UnauthorizedActionException("읽기 전용 멤버라 보드를 수정할 수 없습니다.");
        }

        board.updateBoard(boardRequest.getBoardTitle(), boardRequest.getBackground());
        return new BoardResponse(board);

    }


    // 보드 단건 조회
    @Transactional
    public GetBoardResponse getBoard(AuthUser authUser,Long spaceId, Long boardId) {
        User user = User.fromAuthUser(authUser);

        // 워크스페이스 확인
        Workspace workspace = workspaceRepository.findById(spaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException("해당 워크스페이스가 없습니다."));

        // 보드와 워크스페이스가 연결된 보드 조회
        Board board = boardRepository.findByBoardIdAndWorkspace(boardId, workspace)
                .orElseThrow(() -> new BoardNotFoundException("해당 보드가 존재하지 않습니다."));


        // 해당 보드의 리스트와 각 리스트의 카드도 함께 조회
        List<BoardList> boardLists = board.getBoardLists();

        // 각 리스트에서 카드를 조회하여 GetBoardListResponse 로 변환
        List<GetBoardListResponse> boardListResponses = new ArrayList<>();

        for (BoardList list : boardLists) {
            List<GetCardResponse> cardResponses = new ArrayList<>();

            for (Card card : list.getCards()) {
                GetCardResponse cardResponse = new GetCardResponse(card.getCardId(), card.getCardTitle(), card.getCardContent());
                cardResponses.add(cardResponse);
            }

            GetBoardListResponse boardListResponse = new GetBoardListResponse(list.getListId(), list.getTitle(), cardResponses);
            boardListResponses.add(boardListResponse);
        }

        // GetBoardResponse 로 변환 및 반환
        return new GetBoardResponse(board.getBoardId(), board.getBoardTitle(), board.getBackground(), boardListResponses);

    }


    // 보드 삭제
    // 삭제 시 보드 내의 모든 리스트와 데이터도 삭제됩니다.
    @Transactional
    public void deleteBoard(AuthUser authUser,Long spaceId, Long boardId) {
        User user = User.fromAuthUser(authUser);
        // 워크스페이스 확인
        Workspace workspace = workspaceRepository.findById(spaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException("해당 워크스페이스가 없습니다."));

        // 보드와 워크스페이스가 연결된 보드 조회
        Board board = boardRepository.findByBoardIdAndWorkspace(boardId, workspace)
                .orElseThrow(() -> new BoardNotFoundException("해당 보드가 존재하지 않습니다."));

        // 워크스페이스와 유저가 연결된 멤버 정보를 조회
        Member member = memberRepository.findByUserAndWorkspace(user, workspace)
                .orElseThrow(() -> new UnauthorizedActionException("해당 보드에 대한 권한이 없습니다."));

        // 읽기 전용 역할을 가진 멤버가 보드를 삭제하려는 경우
        if (member.getMemberRole() == MemberRole.READ_ONLY_MEMBER) {
            throw new UnauthorizedActionException("읽기 전용 멤버라 보드를 삭제할 수 없습니다.");
        }

        boardRepository.delete(board);

    }

    public Board getBoardFetchJoinToWorkspace(Long boardId) {
        return boardRepository.findByBoardWithJoinFetchToWorkspace(boardId).orElseThrow(
                ()->new NoSuchElementException("해당 보드가 존재하지 않습니다.")
        );
    }

}
