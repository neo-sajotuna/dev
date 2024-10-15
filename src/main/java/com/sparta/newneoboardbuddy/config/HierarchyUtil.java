package com.sparta.newneoboardbuddy.config;

import com.sparta.newneoboardbuddy.domain.board.entity.Board;
import com.sparta.newneoboardbuddy.domain.card.entity.Card;
import com.sparta.newneoboardbuddy.domain.list.entity.BoardList;
import com.sparta.newneoboardbuddy.domain.member.entity.Member;
import com.sparta.newneoboardbuddy.domain.workspace.entity.Workspace;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
public class HierarchyUtil {
    public Board getBoardUseWorkspace(Workspace workspace, Long boardId) {
        for (Board board : workspace.getBoards()) {
            if (board.getBoardId().equals(boardId)) {
                return board;
            }
        }

        throw new NoSuchElementException("해당 board는 Workspace 소속이 아닙니다.");
    }

    public BoardList getBoardListUseBoard(Board board, Long boardListId) {
        for (BoardList boardList : board.getBoardLists()) {
            if (boardList.getListId().equals(boardListId)) {
                return boardList;
            }
        }

        throw new NoSuchElementException("해당 List는 Board 소속이 아닙니다.");
    }

    public Board getBoardListUseMember(Member member, Long boardId) {
        Workspace workspace = member.getWorkspace();
        return getBoardUseWorkspace(workspace, boardId);
    }
}
