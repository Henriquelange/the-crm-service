package com.theagilemonkeys.crm.exception;

public enum BusinessExceptionEnum {

  APPLICATION_ERROR(500, "Application error");

  private final int httpCode;

  private final String message;

  BusinessExceptionEnum(int httpCode, String message) {
    this.httpCode = httpCode;
    this.message = message;
  }

  public BusinessException exception() {
    return new BusinessException(this.httpCode, this.message);
  }

  public BusinessException exception(Object[] parameters) {
    return new BusinessException(this.httpCode, String.format(this.message, parameters));
  }

  public BusinessException exception(String parameter) {
    return this.exception(new Object[] {parameter});
  }
}
