package com.luckyb.global.exception;

public class CouponNotFoundException extends RuntimeException {

  private final ErrorCode errorCode;
  private final String couponId;

  public CouponNotFoundException(ErrorCode errorCode, String couponId) {
    super(errorCode.getMessage() + " (couponId: " + couponId + ")");
    this.errorCode = errorCode;
    this.couponId = couponId;
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }

  public String getCouponId() {
    return couponId;
  }
} 