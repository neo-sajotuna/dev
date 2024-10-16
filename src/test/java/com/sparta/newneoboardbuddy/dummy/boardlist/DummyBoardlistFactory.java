package com.sparta.newneoboardbuddy.dummy.boardlist;

import com.github.javafaker.Faker;
import com.sparta.newneoboardbuddy.domain.board.entity.Board;
import com.sparta.newneoboardbuddy.domain.list.entity.BoardList;
import com.sparta.newneoboardbuddy.domain.user.entity.User;
import com.sparta.newneoboardbuddy.domain.user.enums.UserRole;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DummyBoardlistFactory {
    /**
     * Random한 User 데이터를 만들어주는 메서드
     * @param size DummyData 개수
     * @return 완성된 User dummyData
     */
    public List<BoardList> createDummyBoardlist(Faker faker, int size, List<Board> boards) {
        List<BoardList> boardLists = new ArrayList<BoardList>();

        for (int i = 0; i < size; i++) {
            int index = faker.random().nextInt(boards.size());

            BoardList boardList = new BoardList(
                    0L,
                    faker.book().title(),
                    (long)boards.get(index).getBoardLists().size(),
                    boards.get(index),
                    null);

            boardLists.add(boardList);
        }

        return boardLists;
    }
}
