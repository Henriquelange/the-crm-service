package com.theagilemonkeys.crm.mapper;

import com.theagilemonkeys.crm.entity.Consumer;
import com.theagilemonkeys.crm.entity.ConsumerPersistence;
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

  public abstract Consumer consumerPersistenceToConsumerBusinessEntity(ConsumerPersistence consumerPersistence);

  public abstract ConsumerPersistence consumerBusinessToConsumerPersistenceEntity(Consumer consumer);

}
