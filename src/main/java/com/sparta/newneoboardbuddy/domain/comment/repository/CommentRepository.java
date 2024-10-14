package com.sparta.newneoboardbuddy.domain.comment.repository;

import com.sparta.newneoboardbuddy.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
