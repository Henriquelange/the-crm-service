package com.theagilemonkeys.crm.adapter;

import com.theagilemonkeys.crm.entity.Customer;
import com.theagilemonkeys.crm.entity.CustomerPersistence;
import com.theagilemonkeys.crm.exception.PersistenceException;
import com.theagilemonkeys.crm.exception.PersistenceExceptionEnum;
import com.theagilemonkeys.crm.mapper.PersistenceMapper;
import com.theagilemonkeys.crm.repository.CustomerDynamoDBRepository;
import com.theagilemonkeys.crm.repository.CustomerRepository;
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
public class CustomerRepositoryDynamoDBAdapter implements CustomerRepository {

  @Autowired
  protected CustomerDynamoDBRepository customerRepository;

  @Autowired
  protected PersistenceMapper persistenceMapper;

  @Override
  public Optional<Customer> findById(final UUID id) throws PersistenceException {
    try {
      Optional<CustomerPersistence> fetchedCustomer = customerRepository.findById(id.toString());
      return fetchedCustomer.map(customer -> persistenceMapper.customerPersistenceToCustomerBusinessEntity(customer));
    } catch (Exception e) {
      log.error("Error while finding customer with id {}: ", id, e);
      throw PersistenceExceptionEnum.QUERY_DATABASE_ERROR.exception(e.getMessage());
    }
  }

  @Override
  public List<Customer> findAll() throws PersistenceException {
    try {
      final var values = customerRepository.findAll();

      return StreamSupport.stream(values.spliterator(), false)
          .map(value -> persistenceMapper.customerPersistenceToCustomerBusinessEntity(value))
          .collect(Collectors.toList());
    } catch (Exception e) {
      log.error("Error while finding all customers: ", e);
      throw PersistenceExceptionEnum.QUERY_DATABASE_ERROR.exception(e.getMessage());
    }
  }

  @Override
  public Customer save(final Customer customer) throws PersistenceException {
    try {
      CustomerPersistence fetchedCustomer = customerRepository.save(persistenceMapper.customerBusinessToCustomerPersistenceEntity(customer));
      return persistenceMapper.customerPersistenceToCustomerBusinessEntity(fetchedCustomer);
    } catch (Exception e) {
      log.error("Error while saving customer {}:", customer, e);
      throw PersistenceExceptionEnum.SAVE_ON_DATABASE_ERROR.exception(e.getMessage());
    }
  }

  @Override
  public void delete(final UUID id) throws PersistenceException {
    try {
      customerRepository.deleteById(id.toString());
    } catch (Exception e) {
      log.error("Error while deleting customer with id {}: ", id, e);
      throw PersistenceExceptionEnum.DELETE_FROM_DATABASE_ERROR.exception(e.getMessage());
    }
  }
}
