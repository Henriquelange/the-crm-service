package com.theagilemonkeys.crm.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGenerateStrategy;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedTimestamp;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import java.util.Calendar;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@DynamoDBTable(tableName = "CrmCustomer")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerPersistence {

  public static final String TABLE_NAME = "CrmCustomer";

  @Id
  @DynamoDBHashKey(attributeName = "id")
  private String id;

  @DynamoDBAttribute(attributeName = "firstName")
  private String firstName;

  @DynamoDBAttribute(attributeName = "lastName")
  private String lastName;

  @DynamoDBAttribute(attributeName = "photoUrl")
  private String photoUrl;

  @DynamoDBAttribute(attributeName = "createdDate")
  @DynamoDBAutoGeneratedTimestamp(strategy = DynamoDBAutoGenerateStrategy.CREATE)
  private Calendar createdDate;

  @DynamoDBAttribute(attributeName = "lastUpdatedDate")
  @DynamoDBAutoGeneratedTimestamp(strategy = DynamoDBAutoGenerateStrategy.ALWAYS)
  private Calendar lastUpdatedDate;

  @DynamoDBAttribute(attributeName = "lastModifiedBy")
  private String lastModifiedBy;

}