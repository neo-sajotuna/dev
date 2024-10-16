package com.sparta.newneoboardbuddy.common.exception;

import org.springframework.dao.OptimisticLockingFailureException;

public class CommonOptimisticLockingFailureException extends OptimisticLockingFailureException {
    public CommonOptimisticLockingFailureException(String message) { super(message); }
}
