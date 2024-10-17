package com.sparta.newneoboardbuddy.domain.workspace.repository;

import com.sparta.newneoboardbuddy.domain.workspace.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {

    /** 수정 예정
     * SpaceId에 해당하는 workSpace를 Board까지 FetchJoin한 상태로 반환하는 메서드
     * @param spaceId 찾을 spaceId
     * @return 해당 Id를 가진 Optional이 적용된 workspace 객체
     */
    @Query("select w from Workspace w join fetch w.boards b  " +
            "where w.spaceId = :spaceId AND b.boardId = :boardId")
    Optional<Workspace> findBySpaceIdWithJoinFetchBoard(Long spaceId, Long boardId);
}
