package com.luckyb.global.exception;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {
    
    private final ErrorCode errorCode;
    
    public UserNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    
    public UserNotFoundException(ErrorCode errorCode, String additionalMessage) {
        super(errorCode.getMessage() + ": " + additionalMessage);
        this.errorCode = errorCode;
    }
} 