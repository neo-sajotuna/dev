package com.sparta.newneoboardbuddy.domain.board.repository;

import com.sparta.newneoboardbuddy.domain.board.entity.Board;
import com.sparta.newneoboardbuddy.domain.workspace.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    /**
     * 해당 id를 가진 Board를 workspace까지 fetch Join 하여 반환하는 메서드
     * @param boardId 조회할 board Id
     * @return workspace까지 fetch join한 board 객체
     */
    @Query("select b from Board b join fetch b.workspace w " +
            "where b.boardId = :boardId")
    Optional<Board> findByBoardWithJoinFetchToWorkspace(Long boardId);

    Optional<Board> findByBoardIdAndWorkspace(Long boardId, Workspace workspace);
}
