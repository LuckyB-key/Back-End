package com.luckyb.global.exception;

import lombok.Getter;

@Getter
public class AdvertisementNotFoundException extends RuntimeException {

  private final ErrorCode errorCode;

  public AdvertisementNotFoundException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

}
