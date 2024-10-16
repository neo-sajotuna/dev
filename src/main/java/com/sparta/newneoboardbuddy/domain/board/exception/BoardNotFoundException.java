package com.sparta.newneoboardbuddy.domain.board.exception;

import com.sparta.newneoboardbuddy.config.CommonException;
import org.springframework.http.HttpStatus;

public class BoardNotFoundException extends CommonException {
    public BoardNotFoundException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
