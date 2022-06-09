package com.theagilemonkeys.crm.exception;

public enum BusinessExceptionEnum {

  EMPTY_FILE_ERROR(500, "Uploaded file is empty"),
  INVALID_FILE_MIME_TYPE_ERROR(500, "Uploaded file has an invalid mime type: %s"),
  FILE_IO_ERROR(500, "Error processing file for upload: %s"),
  APPLICATION_ERROR(500, "Application error: %s");

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
