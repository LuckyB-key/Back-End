package com.luckyb.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // 사용자 관련 에러
    USER_NOT_FOUND("U001", "사용자를 찾을 수 없습니다"),
    INVALID_TOKEN("U002", "유효하지 않은 토큰입니다"),
    DUPLICATE_EMAIL("U003", "이미 사용 중인 이메일입니다"),
    DUPLICATE_NICKNAME("U004", "이미 사용 중인 닉네임입니다"),
    
    // 인증 관련 에러
    AUTHENTICATION_FAILED("A001", "인증에 실패했습니다"),
    TOKEN_EXPIRED("A002", "토큰이 만료되었습니다"),
    INVALID_CREDENTIALS("A003", "잘못된 인증 정보입니다"),
    ACCESS_DENIED("A004","접근이 거부되었습니다."),
    
    // 쉼터 관련 에러
    SHELTER_NOT_FOUND("SH001", "쉼터를 찾을 수 없습니다"),
    INVALID_COORDINATES("SH002", "유효하지 않은 좌표입니다"),
    INVALID_SHELTER_TYPE("SH003", "유효하지 않은 쉼터 타입입니다"),
    SHELTER_CREATION_FAILED("SH004", "쉼터 생성에 실패했습니다"),
    SHELTER_UPDATE_FAILED("SH005", "쉼터 수정에 실패했습니다"),
    SHELTER_DELETE_FAILED("SH006", "쉼터 삭제에 실패했습니다"),

    // 광고 관련 에러
    ADVERTISEMENT_NOT_FOUND("AD001", "광고를 찾을 수 없습니다"),
    ADVERTISEMENT_CREATION_FAILED("AD002", "광고 생성에 실패했습니다"),
    ADVERTISEMENT_UPDATE_FAILED("AD003", "광고 수정에 실패했습니다"),
    ADVERTISEMENT_DELETE_FAILED("AD004", "광고 삭제에 실패했습니다"),
    INVALID_ADVERTISEMENT_INPUT("AD005", "광고 입력값이 올바르지 않습니다"),

    
    // 시스템 에러
    INTERNAL_SERVER_ERROR("S001", "서버 내부 오류가 발생했습니다"),
    INVALID_INPUT("S002", "입력값이 올바르지 않습니다");
    
    private final String code;
    private final String message;
    
    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
} 