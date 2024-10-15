package com.sparta.newneoboardbuddy.domain.file.exception;

import com.sparta.newneoboardbuddy.config.CommonException;
import org.springframework.http.HttpStatus;

public class FileSizeExceededException extends CommonException {
    public FileSizeExceededException(HttpStatus httpStatus) {
        super(httpStatus, "파일 크기는 5MB 이하로 가능합니다.");
    }
}
