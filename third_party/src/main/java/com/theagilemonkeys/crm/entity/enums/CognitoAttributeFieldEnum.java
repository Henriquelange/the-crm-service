package com.theagilemonkeys.crm.entity.enums;

import lombok.Getter;

@Getter
public enum CognitoAttributeFieldEnum {
  EMAIL("email", ""),
  PHONE_NUMBER("phone_number", ""),
  EMAIL_VERIFIED("email_verified", "false"),
  PHONE_NUMBER_VERIFIED("phone_number_verified", "false");

  private final String name;
  private final String defaultValue;

  CognitoAttributeFieldEnum(String name, String defaultValue) {
    this.name = name;
    this.defaultValue = defaultValue;
  }
}
