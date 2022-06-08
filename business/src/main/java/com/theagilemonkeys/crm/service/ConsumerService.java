package com.theagilemonkeys.crm.service;

import com.theagilemonkeys.crm.entity.Consumer;
import com.theagilemonkeys.crm.exception.PersistenceException;
import com.theagilemonkeys.crm.repository.ConsumerRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class ConsumerService {

  private final ConsumerRepository consumerRepository;

  public Consumer saveConsumer(Consumer consumer) throws PersistenceException {
    return consumerRepository.save(consumer);
  }

  public Optional<Consumer> findConsumerById(UUID consumerId) throws PersistenceException {
    return consumerRepository.findById(consumerId);
  }

  public List<Consumer> findAllConsumers() throws PersistenceException {
    return consumerRepository.findAll();
  }

}
