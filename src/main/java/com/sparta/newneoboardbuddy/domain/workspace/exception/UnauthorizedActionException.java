package com.sparta.newneoboardbuddy.domain.workspace.exception;

import com.sparta.newneoboardbuddy.config.CommonException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UnauthorizedActionException extends CommonException {
    public UnauthorizedActionException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}