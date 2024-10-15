package com.sparta.newneoboardbuddy.domain.workspace.repository;

import com.sparta.newneoboardbuddy.domain.member.entity.Member;
import com.sparta.newneoboardbuddy.domain.workspace.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {

    @Query("select w from Workspace w join fetch w.boards b  " +
            "where w.spaceId = :spaceId AND b.boardId = :boardId")
    Optional<Workspace> findBySpaceIdWithJoinFetchBoard(Long spaceId, Long boardId);
}
