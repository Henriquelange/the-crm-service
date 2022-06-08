package com.theagilemonkeys.crm.service;

import com.theagilemonkeys.crm.entity.Customer;
import com.theagilemonkeys.crm.exception.PersistenceException;
import com.theagilemonkeys.crm.repository.CustomerRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerService {

  private final CustomerRepository customerRepository;

  public Customer saveCustomer(Customer customer) throws PersistenceException {
    return customerRepository.save(customer);
  }

  public Optional<Customer> findCustomerById(UUID customerId) throws PersistenceException {
    return customerRepository.findById(customerId);
  }

  public List<Customer> findAllCustomers() throws PersistenceException {
    return customerRepository.findAll();
  }

}
