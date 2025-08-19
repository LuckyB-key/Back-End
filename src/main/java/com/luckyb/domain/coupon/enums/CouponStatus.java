package com.luckyb.domain.coupon.enums;

/**
 * 쿠폰 상태 enum
 */
public enum CouponStatus {
    ACTIVE("활성"),
    INACTIVE("비활성"),
    EXPIRED("만료"),
    DEPLETED("소진");

    private final String description;

    CouponStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
} 