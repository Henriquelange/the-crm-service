package com.theagilemonkeys.crm.exception;

public enum PersistenceExceptionEnum {

  S3_INTEGRATION_ERROR(500, "Error while uploading file to S3: %s"),
  SAVE_ON_DATABASE_ERROR(500, "Error while saving on DynamoDB: %s"),
  DELETE_FROM_DATABASE_ERROR(500, "Error while deleting from DynamoDB: %s"),
  QUERY_DATABASE_ERROR(500, "Error while reading from DynamoDB: %s");

  private final int httpCode;

  private final String message;

  PersistenceExceptionEnum(final int httpCode, final String message) {
    this.httpCode = httpCode;
    this.message = message;
  }

  public PersistenceException exception(final String exceptionMessage) {
    return new PersistenceException(this.httpCode, String.format(this.message, exceptionMessage));
  }

}
