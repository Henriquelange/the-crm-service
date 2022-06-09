package com.theagilemonkeys.crm.controller;

import com.theagilemonkeys.crm.dto.AuthenticatedUserDTO;
import com.theagilemonkeys.crm.dto.CreateCustomerDTO;
import com.theagilemonkeys.crm.entity.Customer;
import com.theagilemonkeys.crm.exception.PersistenceException;
import com.theagilemonkeys.crm.mapper.CustomerMapper;
import com.theagilemonkeys.crm.service.CustomerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

  @GetMapping("/customers")
  public ResponseEntity<List<Customer>> listAllCustomers(
      @AuthenticationPrincipal AuthenticatedUserDTO authenticatedUser) throws PersistenceException {

    List<Customer> customerList = customerService.findAllCustomers();

    return ResponseEntity.status(HttpStatus.OK).body(customerList);
  }

}
