package com.luckyb.global.exception;

import lombok.Getter;

@Getter
public class ShelterNotFoundException extends RuntimeException {
    
    private final ErrorCode errorCode;
    
    public ShelterNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    
    public ShelterNotFoundException(ErrorCode errorCode, String additionalMessage) {
        super(errorCode.getMessage() + ": " + additionalMessage);
        this.errorCode = errorCode;
    }
} 