package com.sparta.newneoboardbuddy.domain.comment.exception;

import com.sparta.newneoboardbuddy.config.CommonException;
import org.springframework.http.HttpStatus;

public class CommentNotFoundException extends CommonException {
    public CommentNotFoundException(HttpStatus status) {
        super(status, "존재하지 않는 댓글입니다.");
    }
}
