package com.luckyb.domain.coupon.enums;

/**
 * 쿠폰 상태 enum
 */
public enum CouponStatus {
    ACTIVE("활성"),
    USED("사용됨"),
    EXPIRED("만료"),
    CANCELLED("취소됨");

    private final String description;

    CouponStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static CouponStatus fromValue(String value) {
        for (CouponStatus status : CouponStatus.values()) {
            if (status.name().equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown coupon status: " + value);
    }
} 