package com.sparta.newneoboardbuddy.domain.workspace.exception;

import com.sparta.newneoboardbuddy.config.CommonException;
import org.springframework.http.HttpStatus;

public class WorkspaceNotFoundException extends CommonException {
    public WorkspaceNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}