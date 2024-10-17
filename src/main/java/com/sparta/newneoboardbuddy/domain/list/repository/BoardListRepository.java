package com.sparta.newneoboardbuddy.domain.list.repository;

import com.sparta.newneoboardbuddy.domain.board.entity.Board;
import com.sparta.newneoboardbuddy.domain.list.entity.BoardList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BoardListRepository extends JpaRepository<BoardList, Long> {
    Optional<BoardList> findByBoardAndListIndex(Board board, Long listIndex);

    /**
     * 해당 BoardList Id를 가지고 있는 데이터를 Workspace까지 Fetch Join 하여 반환해주는 Query 메서드
     * @param boardListId 찾고자 하는 BoardList Id
     * @return workspace까지 fetch Join 한 boardList 객체
     */
    @Query("select bl from BoardList bl join fetch bl.board b join fetch b.workspace " +
            "where bl.listId = :boardListId")
    Optional<BoardList> findByIdWithJoinFetchToWorkspace(Long boardListId);

    /**
     * 모든 BoardList를 workspace까지 Fetch Join 하여 반환해주는 Query 메서드
     * @return workspace까지 fetch Join 한 모든 BoardList 객체
     */
    @Query("select bl from BoardList bl join fetch bl.board b join fetch b.workspace")
    List<BoardList> findAllJoinFetchToWorkspace();
}
