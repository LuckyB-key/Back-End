package com.luckyb.global.enums;

/**
 * 사용자 역할 enum
 */
public enum UserRole {
    USER("일반 사용자"),
    BUSINESS("비즈니스 사용자"),
    ADMIN("관리자");

    private final String description;

    UserRole(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
} 