package com.luckyb.domain.shelter.enums;

/**
 * 쉼터 상태 enum
 */
public enum ShelterStatus {
    ACTIVE("활성"),
    INACTIVE("비활성"),
    MAINTENANCE("점검 중"),
    CLOSED("폐쇄");

    private final String description;

    ShelterStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
} 