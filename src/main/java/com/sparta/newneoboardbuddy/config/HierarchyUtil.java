package com.sparta.newneoboardbuddy.config;

import com.sparta.newneoboardbuddy.domain.board.entity.Board;
import com.sparta.newneoboardbuddy.domain.card.entity.Card;
import com.sparta.newneoboardbuddy.domain.comment.entity.Comment;
import com.sparta.newneoboardbuddy.domain.list.entity.BoardList;
import com.sparta.newneoboardbuddy.domain.workspace.entity.Workspace;
import org.springframework.stereotype.Component;

@Component
public class HierarchyUtil {
    /*
        해당 함수들을 호출하시기 전에 각 workspace까지 fetch Join이 완료된 상태인지 확인해주세요.

        Workspace -> Board -> BoardList -> Card -> Comment의 계층 구조로 이뤄진 이상
        Board ~ Comment까지 전체 Id 대조해 볼 필요 없이 WorkSpace만 일치한다면,
        나머지는 이전 단계 삽입하는 과정에서 연결 / 체크 되어 있기에 문제 없습니다.
     */

    /**
     * 해당 board가 workspaceId에 속해 있는지 판별하는 메서드
     * @param workspaceId 확인할 workspaceId
     * @param board 확인할 board 객체
     * @return True : board는 해당 workspace에 속해있다 / False 속하지 않았다.
     */
    public boolean isBoardInWorkspace(Long workspaceId, Board board) {
        Workspace workspace = board.getWorkspace();

        return workspace.getSpaceId().equals(workspaceId);
    }

    /**
     * 해당 boardList가 workspaceId에 속해 있는지 판별하는 메서드
     * @param workspaceId 확인할 workspaceId
     * @param boardList 확인할 boardList 객체
     * @return True : boardList는 해당 workspace에 속해있다 / False 속하지 않았다.
     */
    public boolean isListInWorkspace(Long workspaceId, BoardList boardList) {
        Board board = boardList.getBoard();

        return isBoardInWorkspace(workspaceId, board);
    }

    /**
     * 해당 card가 workspaceId에 속해 있는지 판별하는 메서드
     * @param workspaceId 확인할 workspaceId
     * @param card 확인할 card 객체
     * @return True : card는 해당 workspace에 속해있다 / False 속하지 않았다.
     */
    public boolean isCardInWorkspace(Long workspaceId, Card card) {
        BoardList boardList = card.getBoardList();

        return isListInWorkspace(workspaceId, boardList);
    }

    /**
     * 해당 comment가 workspaceId에 속해 있는지 판별하는 메서드
     * @param workspaceId 확인할 workspaceId
     * @param comment 확인할 comment 객체
     * @return True : comment는 해당 workspace에 속해있다 / False 속하지 않았다.
     */
    public boolean isCommentInWorkspace(Long workspaceId, Comment comment) {
        Card card = comment.getCard();

        return isCardInWorkspace(workspaceId, card);
    }
}
