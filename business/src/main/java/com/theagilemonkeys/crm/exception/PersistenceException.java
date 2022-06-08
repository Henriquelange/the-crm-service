package com.theagilemonkeys.crm.exception;

import lombok.Getter;

@Getter
public class PersistenceException extends Exception {

  private final int httpCode;

  public PersistenceException(final int httpCode, final String message) {
    super(message);
    this.httpCode = httpCode;
  }

}
