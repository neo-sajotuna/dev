package com.sparta.newneoboardbuddy.domain.file.exception;

import com.sparta.newneoboardbuddy.config.CommonException;
import org.springframework.http.HttpStatus;

public class ImpossibilityExtensionException extends CommonException {
    public ImpossibilityExtensionException(HttpStatus httpStatus) {
        super(httpStatus, "허용하지 않는 파일확장자 입니다.");
    }
}
