package com.luckyb.domain.coupon.enums;

/**
 * 사용자 쿠폰 상태 enum
 */
public enum UserCouponStatus {
  ACTIVE("활성"),
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

  public static UserCouponStatus fromValue(String value) {
    for (UserCouponStatus status : UserCouponStatus.values()) {
      if (status.name().equals(value)) {
        return status;
      }
    }
    throw new IllegalArgumentException("Unknown user coupon status: " + value);
  }
} 