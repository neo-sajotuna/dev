package com.sparta.newneoboardbuddy.domain.list.repository;

import com.sparta.newneoboardbuddy.domain.board.entity.Board;
import com.sparta.newneoboardbuddy.domain.list.entity.BoardList;
import com.sparta.newneoboardbuddy.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BoardListRepository extends JpaRepository<BoardList, Long> {
    Optional<BoardList> findByBoardAndListIndex(Board board, Long listIndex);

    @Query("select bl from BoardList bl join fetch bl.board b join fetch b.workspace " +
            "where bl.listId = :boardListId")
    Optional<BoardList> findByIdWithJoinFetchToWorkspace(Long boardListId);

    @Query("select bl from BoardList bl join fetch bl.board b join fetch b.workspace")
    List<BoardList> findAllJoinFetchToWorkspace();
}
