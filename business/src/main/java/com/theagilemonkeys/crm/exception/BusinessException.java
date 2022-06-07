package com.theagilemonkeys.crm.exception;

import lombok.Getter;

@Getter
public class BusinessException extends Exception {

  private final int httpCode;

  public BusinessException(int httpCode, String message) {
    super(message);
    this.httpCode = httpCode;
  }

}
