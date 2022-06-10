package com.theagilemonkeys.crm.repository;

import com.theagilemonkeys.crm.entity.Customer;
import com.theagilemonkeys.crm.exception.PersistenceException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {

  Optional<Customer> findById(UUID id) throws PersistenceException;

  List<Customer> findAll() throws PersistenceException;

  Customer save(Customer customer) throws PersistenceException;

  void delete(UUID id) throws PersistenceException;

  void deleteAll() throws PersistenceException;

}
