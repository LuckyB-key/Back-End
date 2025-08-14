package com.luckyb.global.exception;

import lombok.Getter;

@Getter
public class InvalidTokenException extends RuntimeException {
    
    private final ErrorCode errorCode;
    
    public InvalidTokenException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    
    public InvalidTokenException(ErrorCode errorCode, String additionalMessage) {
        super(errorCode.getMessage() + ": " + additionalMessage);
        this.errorCode = errorCode;
    }
} 