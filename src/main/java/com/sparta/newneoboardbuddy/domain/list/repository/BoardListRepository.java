package com.sparta.newneoboardbuddy.domain.list.repository;

import com.sparta.newneoboardbuddy.domain.board.entity.Board;
import com.sparta.newneoboardbuddy.domain.list.entity.BoardList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardListRepository extends JpaRepository<BoardList, Long> {
    Optional<BoardList> findByBoardAndListIndex(Board board, Long listIndex);
}
