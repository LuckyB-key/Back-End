package com.luckyb.domain.coupon.enums;

/**
 * 사용자 쿠폰 상태 enum
 */
public enum UserCouponStatus {
    ISSUED("발급됨"),
    USED("사용됨"),
    EXPIRED("만료됨"),
    CANCELLED("취소됨");

    private final String description;

    UserCouponStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
} 