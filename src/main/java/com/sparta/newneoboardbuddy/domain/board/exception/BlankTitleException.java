package com.sparta.newneoboardbuddy.domain.board.exception;

import com.sparta.newneoboardbuddy.config.CommonException;
import org.springframework.http.HttpStatus;

public class BlankTitleException extends CommonException {
    public BlankTitleException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
