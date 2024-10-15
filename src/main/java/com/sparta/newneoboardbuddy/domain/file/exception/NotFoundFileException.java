package com.sparta.newneoboardbuddy.domain.file.exception;

import com.sparta.newneoboardbuddy.config.CommonException;
import org.springframework.http.HttpStatus;

public class NotFoundFileException extends CommonException {
    public NotFoundFileException(HttpStatus httpStatus) {
        super(httpStatus, "파일이 존재하지 않습니다.");
    }
}
