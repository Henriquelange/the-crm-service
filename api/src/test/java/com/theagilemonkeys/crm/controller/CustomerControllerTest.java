package com.theagilemonkeys.crm.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theagilemonkeys.crm.dto.AuthenticatedUserDTO;
import com.theagilemonkeys.crm.dto.CreateCustomerDTO;
import com.theagilemonkeys.crm.dto.UpdateCustomerDTO;
import com.theagilemonkeys.crm.entity.Customer;
import com.theagilemonkeys.crm.exception.PersistenceException;
import com.theagilemonkeys.crm.factory.CustomerTestFactory;
import com.theagilemonkeys.crm.mapper.CustomerMapper;
import com.theagilemonkeys.crm.service.CustomerService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.MimeTypeUtils;

@Slf4j
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest
class CustomerControllerTest {

  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  private CustomerMapper customerMapper;

  @Autowired
  private CustomerService customerService;

  private ObjectMapper mapper;

  CustomerControllerTest() {
    mapper = new ObjectMapper();
    mapper.findAndRegisterModules();
  }

  @BeforeEach
  public void setup() throws PersistenceException {
    AuthenticatedUserDTO authenticatedUserDTO =
        AuthenticatedUserDTO.builder()
            .email("test@test.com")
            .build();
    Authentication authentication = Mockito.mock(Authentication.class);
    Mockito.when(authentication.getPrincipal()).thenReturn(authenticatedUserDTO);
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    customerService.deleteAllCustomers();
  }

  @Test
  void shouldSuccessfullyCreateCustomer() {
    try {
      CreateCustomerDTO postData = CustomerTestFactory.generateCreateCustomerDTO();

      mockMvc.perform(MockMvcRequestBuilders
              .post("/api/crm/customer")
              .content(mapper.writeValueAsString(postData))
              .contentType(MediaType.APPLICATION_JSON)
              .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isCreated())
          .andReturn();

    } catch (Exception e) {
      log.error(e.toString());
      Assertions.fail(e);
    }
  }

  @Test
  void shouldSuccessfullyUpdateCustomer() {
    try {
      Customer customer = CustomerTestFactory.generateCustomer();
      customerService.saveCustomer(customer);

      UpdateCustomerDTO updateCustomerDTO = CustomerTestFactory.generateUpdateCustomerDTO();

      mockMvc.perform(MockMvcRequestBuilders
              .put(String.format("/api/crm/customer/%s", customer.getId()))
              .content(mapper.writeValueAsString(updateCustomerDTO))
              .contentType(MediaType.APPLICATION_JSON)
              .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andReturn();

      Customer updatedCustomer = customerService.findCustomerById(customer.getId()).get();
      assertThat(updatedCustomer.getFirstName()).isEqualTo(updateCustomerDTO.getFirstName());
      assertThat(updatedCustomer.getLastName()).isEqualTo(updateCustomerDTO.getLastName());
      assertThat(updatedCustomer.getPhotoUrl()).isEqualTo(updateCustomerDTO.getPhotoUrl());

    } catch (Exception e) {
      log.error(e.toString());
      Assertions.fail(e);
    }
  }

  @Test
  void shouldSuccessfullyGetCustomerDetails() {
    try {
      Customer customer = CustomerTestFactory.generateCustomer();
      customerService.saveCustomer(customer);

      mockMvc.perform(MockMvcRequestBuilders
              .get(String.format("/api/crm/customer/%s", customer.getId()))
              .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(result -> {
            Customer response =
                mapper.readValue(result.getResponse().getContentAsString(),
                    Customer.class);
            assertThat(response.getFirstName()).isEqualTo(customer.getFirstName());
            assertThat(response.getLastName()).isEqualTo(customer.getLastName());
            assertThat(response.getPhotoUrl()).isEqualTo(customer.getPhotoUrl());
          })
          .andReturn();

    } catch (Exception e) {
      log.error(e.toString());
      Assertions.fail(e);
    }
  }

  @Test
  void shouldSuccessfullyListCustomers() {
    try {
      Customer customer1 = CustomerTestFactory.generateCustomer();
      Customer customer2 = CustomerTestFactory.generateCustomer();
      Customer customer3 = CustomerTestFactory.generateCustomer();
      customerService.saveCustomer(customer1);
      customerService.saveCustomer(customer2);
      customerService.saveCustomer(customer3);

      mockMvc.perform(MockMvcRequestBuilders
              .get("/api/crm/customers")
              .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(result -> {
            List<Customer> response =
                mapper.readValue(result.getResponse().getContentAsString(),
                    new TypeReference<>() {});
            assertThat(response.size()).isEqualTo(3);
            assertThat(response.contains(customer1));
            assertThat(response.contains(customer2));
            assertThat(response.contains(customer3));
          })
          .andReturn();

    } catch (Exception e) {
      log.error(e.toString());
      Assertions.fail(e);
    }
  }

  @Test
  void shouldSuccessfullyDeleteCustomer() {
    try {
      Customer customer = CustomerTestFactory.generateCustomer();
      customerService.saveCustomer(customer);

      mockMvc.perform(MockMvcRequestBuilders
              .delete(String.format("/api/crm/customer/%s", customer.getId()))
              .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andReturn();

      Optional<Customer> deletedCustomer = customerService.findCustomerById(customer.getId());
      assertThat(deletedCustomer.isEmpty());

    } catch (Exception e) {
      log.error(e.toString());
      Assertions.fail(e);
    }
  }

  @Test
  void shouldReturn404OnCustomerNotFound() {
    try {
      mockMvc.perform(MockMvcRequestBuilders
              .get(String.format("/api/crm/customer/%s", UUID.randomUUID()))
              .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isNotFound())
          .andReturn();
    } catch (Exception e) {
      log.error(e.toString());
      Assertions.fail(e);
    }
  }

  @Test
  void shouldSucessfullyUploadPhotoToS3() {
    try {
      Customer customer = CustomerTestFactory.generateCustomer();
      customerService.saveCustomer(customer);

      MockMultipartFile file = new MockMultipartFile(
          "file",
          "photo.jpg",
          MimeTypeUtils.IMAGE_JPEG_VALUE,
          "Hello, World!".getBytes()
      );

      mockMvc.perform(MockMvcRequestBuilders
              .multipart(String.format("/api/crm/customer/%s/photo", customer.getId()))
              .file(file)
              .param("name", "photo.jpg"))
          .andExpect(status().isOk())
          .andReturn();
    } catch (Exception e) {
      log.error(e.toString());
      Assertions.fail(e);
    }
  }

  @Test
  void shouldFailToUploadPhotoWithWrongMimeType() {
    try {
      Customer customer = CustomerTestFactory.generateCustomer();
      customerService.saveCustomer(customer);

      MockMultipartFile file = new MockMultipartFile(
          "file",
          "text.txt",
          MimeTypeUtils.TEXT_PLAIN_VALUE,
          "Hello, World!".getBytes()
      );

      mockMvc.perform(MockMvcRequestBuilders
              .multipart(String.format("/api/crm/customer/%s/photo", customer.getId()))
              .file(file)
              .param("name", "photo.jpg"))
          .andExpect(status().isBadRequest())
          .andReturn();
    } catch (Exception e) {
      log.error(e.toString());
      Assertions.fail(e);
    }
  }

}
