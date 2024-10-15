package com.sparta.newneoboardbuddy.domain.workspace.exception;

import com.sparta.newneoboardbuddy.config.CommonException;
import org.springframework.http.HttpStatus;

public class UserAlreadyMemberException extends CommonException {
    public UserAlreadyMemberException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
