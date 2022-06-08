package com.theagilemonkeys.crm.mapper;

import com.theagilemonkeys.crm.entity.Customer;
import com.theagilemonkeys.crm.entity.CustomerPersistence;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    imports = {UUID.class})
public abstract class PersistenceMapper {

  public UUID stringToUuid(String id) {
    return id != null ? UUID.fromString(id) : null;
  }

  public String uuidToString(UUID id) {
    return id != null ? id.toString() : null;
  }

  public abstract Customer customerPersistenceToCustomerBusinessEntity(
      CustomerPersistence customerPersistence);

  public abstract CustomerPersistence customerBusinessToCustomerPersistenceEntity(Customer customer);

}
