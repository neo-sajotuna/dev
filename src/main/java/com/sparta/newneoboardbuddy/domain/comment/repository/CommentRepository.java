package com.sparta.newneoboardbuddy.domain.comment.repository;

import com.sparta.newneoboardbuddy.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByCommentId(Long CommentId);

    /**
     * commentId를 기준으로 검색하되,
     * workspace ~ card까지 join fetch 한 후 반환하는 메서드
     * @param commentId 찾고자 하는 commentId
     * @return Optional로 감싸진 cardId를 갖고 있는 Comment객체
     */
    @Query("select c from Comment c " +
            "join fetch c.card ca " +
            "join fetch ca.boardList bl " +
            "join fetch bl.board b " +
            "join fetch b.workspace  " +
            "where c.commentId = :commentId")
    Optional<Comment> findByIdWithJoinFetchToWorkspace(Long commentId);
}
