package com.sparta.newneoboardbuddy.dummy.board;

import com.github.javafaker.Faker;
import com.sparta.newneoboardbuddy.domain.board.dto.request.BoardRequest;
import com.sparta.newneoboardbuddy.domain.board.entity.Board;
import com.sparta.newneoboardbuddy.domain.workspace.entity.Workspace;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DummyBoardFactory {
    /**
     * Random한 Board 데이터를 만들어주는 메서드
     * @param size DummyData 개수
     * @param workspaces 해당 Board와 연결할 workspace
     * @return 완성된 dummyData
     */
    public List<Board> createDummyBoards(Faker faker, int size, List<Workspace> workspaces) {
        List<Board> boards = new ArrayList<Board>();

        for (int i = 0; i < size; i++) {

            int next = faker.random().nextInt(workspaces.size());
            Workspace workspace = workspaces.get(next);

            BoardRequest boardRequest = new BoardRequest(
                    workspace.getSpaceId(),
                    faker.book().title(),
                    faker.color().name()
            );
            Board board = new Board(boardRequest, workspace);
            boards.add(board);
        }

        return boards;
    }
}
