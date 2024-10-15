package com.sparta.newneoboardbuddy.domain.comment.exception;

import com.sparta.newneoboardbuddy.config.CommonException;
import org.springframework.http.HttpStatus;

public class NotAuthorException extends CommonException {
    public NotAuthorException(HttpStatus httpStatus) {
        super(httpStatus, "댓글의 작성자만 수정 및 삭제가 가능합니다.");
    }
}
