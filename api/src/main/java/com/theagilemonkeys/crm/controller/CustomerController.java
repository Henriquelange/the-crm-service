package com.theagilemonkeys.crm.controller;

import com.theagilemonkeys.crm.dto.AuthenticatedUserDTO;
import com.theagilemonkeys.crm.dto.CreateCustomerDTO;
import com.theagilemonkeys.crm.dto.UpdateCustomerDTO;
import com.theagilemonkeys.crm.entity.Customer;
import com.theagilemonkeys.crm.exception.PersistenceException;
import com.theagilemonkeys.crm.mapper.CustomerMapper;
import com.theagilemonkeys.crm.service.CustomerService;
import com.theagilemonkeys.crm.utils.EntityUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Customer")
@RestController
@RequestMapping("/api/crm")
@Slf4j
public class CustomerController {

  @Autowired
  private CustomerService customerService;

  @Autowired
  private CustomerMapper customerMapper;

  @PostMapping("/customer")
  public ResponseEntity createCustomer(
      @AuthenticationPrincipal AuthenticatedUserDTO authenticatedUser, @Valid @RequestBody
      CreateCustomerDTO createCustomerDTO) throws PersistenceException {

    Customer customer = customerService.saveCustomer(
        customerMapper.createCustomerDTOToCustomerBusinessEntity(createCustomerDTO,
            authenticatedUser));
    log.info("Created customer: {}", customer);

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PutMapping("/customer/{customerId}")
  public ResponseEntity updateCustomer(
      @AuthenticationPrincipal AuthenticatedUserDTO authenticatedUser, @Valid @RequestBody
      UpdateCustomerDTO updateCustomerDTO, @PathVariable UUID customerId)
      throws PersistenceException {

    Optional<Customer> fetchedCustomer = customerService.findCustomerById(customerId);
    if (fetchedCustomer.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    Customer customer = fetchedCustomer.get();

    BeanUtils.copyProperties(updateCustomerDTO, customer,
        EntityUtils.getNullPropertyNames(updateCustomerDTO));
    customer.setLastModifiedBy(authenticatedUser.getEmail());
    customerService.saveCustomer(customer);
    log.info("Updated customer: {}", customer);

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @GetMapping("/customers")
  public ResponseEntity<List<Customer>> listAllCustomers() throws PersistenceException {
    List<Customer> customerList = customerService.findAllCustomers();

    return ResponseEntity.status(HttpStatus.OK).body(customerList);
  }

  @GetMapping("/customer/{customerId}")
  public ResponseEntity<Customer> getCustomerDetails(@PathVariable UUID customerId)
      throws PersistenceException {
    Optional<Customer> customer = customerService.findCustomerById(customerId);
    if (customer.isPresent()) {
      return ResponseEntity.status(HttpStatus.OK).body(customer.get());
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @DeleteMapping("/customer/{customerId}")
  public ResponseEntity deleteCustomer(@PathVariable UUID customerId) throws PersistenceException {
    Optional<Customer> customer = customerService.findCustomerById(customerId);
    if (customer.isPresent()) {
      customerService.deleteCustomer(customerId);
      log.info("Deleted customer: {}", customer);
      return ResponseEntity.status(HttpStatus.OK).build();
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

}
