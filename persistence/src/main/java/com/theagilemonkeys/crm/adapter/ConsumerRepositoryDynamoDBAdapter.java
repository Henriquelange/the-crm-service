package com.theagilemonkeys.crm.adapter;

import com.theagilemonkeys.crm.entity.Consumer;
import com.theagilemonkeys.crm.entity.ConsumerPersistence;
import com.theagilemonkeys.crm.exception.PersistenceException;
import com.theagilemonkeys.crm.exception.PersistenceExceptionEnum;
import com.theagilemonkeys.crm.mapper.PersistenceMapper;
import com.theagilemonkeys.crm.repository.ConsumerDynamoDBRepository;
import com.theagilemonkeys.crm.repository.ConsumerRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ConsumerRepositoryDynamoDBAdapter implements ConsumerRepository {

  @Autowired
  protected ConsumerDynamoDBRepository consumerRepository;

  @Autowired
  protected PersistenceMapper persistenceMapper;

  @Override
  public Optional<Consumer> findById(final UUID id) throws PersistenceException {
    try {
      Optional<ConsumerPersistence> fetchedConsumer = consumerRepository.findById(id.toString());
      return fetchedConsumer.map(consumer -> persistenceMapper.consumerPersistenceToConsumerBusinessEntity(consumer));
    } catch (Exception e) {
      log.error("Error while finding consumer with id {}: ", id, e);
      throw PersistenceExceptionEnum.QUERY_DATABASE_ERROR.exception(e.getMessage());
    }
  }

  @Override
  public List<Consumer> findAll() throws PersistenceException {
    try {
      final var values = consumerRepository.findAll();

      return StreamSupport.stream(values.spliterator(), false)
          .map(value -> persistenceMapper.consumerPersistenceToConsumerBusinessEntity(value))
          .collect(Collectors.toList());
    } catch (Exception e) {
      log.error("Error while finding all consumers: ", e);
      throw PersistenceExceptionEnum.QUERY_DATABASE_ERROR.exception(e.getMessage());
    }
  }

  @Override
  public Consumer save(final Consumer consumer) throws PersistenceException {
    try {
      ConsumerPersistence fetchedConsumer = consumerRepository.save(persistenceMapper.consumerBusinessToConsumerPersistenceEntity(consumer));
      return persistenceMapper.consumerPersistenceToConsumerBusinessEntity(fetchedConsumer);
    } catch (Exception e) {
      log.error("Error while saving consumer {}:", consumer, e);
      throw PersistenceExceptionEnum.SAVE_ON_DATABASE_ERROR.exception(e.getMessage());
    }
  }

  @Override
  public void delete(final UUID id) throws PersistenceException {
    try {
      consumerRepository.deleteById(id.toString());
    } catch (Exception e) {
      log.error("Error while deleting consumer with id {}: ", id, e);
      throw PersistenceExceptionEnum.DELETE_FROM_DATABASE_ERROR.exception(e.getMessage());
    }
  }
}
