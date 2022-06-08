package com.theagilemonkeys.crm.repository;

import com.theagilemonkeys.crm.entity.Consumer;
import com.theagilemonkeys.crm.exception.PersistenceException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConsumerRepository {

  Optional<Consumer> findById(UUID id) throws PersistenceException;

  List<Consumer> findAll() throws PersistenceException;

  Consumer save(Consumer consumer) throws PersistenceException;

  void delete(UUID id) throws PersistenceException;

}
