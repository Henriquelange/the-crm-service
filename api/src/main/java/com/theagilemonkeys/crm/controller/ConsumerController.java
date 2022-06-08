package com.theagilemonkeys.crm.controller;

import com.theagilemonkeys.crm.dto.CreateConsumerDTO;
import com.theagilemonkeys.crm.dto.AuthenticatedUserDTO;
import com.theagilemonkeys.crm.entity.Consumer;
import com.theagilemonkeys.crm.exception.PersistenceException;
import com.theagilemonkeys.crm.mapper.ConsumerMapper;
import com.theagilemonkeys.crm.service.ConsumerService;
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

@Tag(name = "Consumer")
@RestController
@RequestMapping("/api/crm")
@Slf4j
public class ConsumerController {

  @Autowired
  private ConsumerService consumerService;

  @Autowired
  private ConsumerMapper consumerMapper;

  @PostMapping("/consumer")
  public ResponseEntity createConsumer(
      @AuthenticationPrincipal AuthenticatedUserDTO authenticatedUser, @Valid @RequestBody
      CreateConsumerDTO createConsumerDTO) throws PersistenceException {

    Consumer consumer = consumerService.saveConsumer(
        consumerMapper.createConsumerDTOToConsumerBusinessEntity(createConsumerDTO,
            authenticatedUser));
    log.info("Created consumer: {}", consumer);

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @GetMapping("/consumers")
  public ResponseEntity<List<Consumer>> listAllConsumers(
      @AuthenticationPrincipal AuthenticatedUserDTO authenticatedUser) throws PersistenceException {

    List<Consumer> consumerList = consumerService.findAllConsumers();

    return ResponseEntity.status(HttpStatus.OK).body(consumerList);
  }

}
