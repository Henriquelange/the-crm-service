package com.theagilemonkeys.crm.controller;

import com.theagilemonkeys.crm.dto.ErrorResponseDTO;
import com.theagilemonkeys.crm.exception.PersistenceException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
@Slf4j
public class ErrorHandler {

  @ExceptionHandler({MethodArgumentNotValidException.class,
      MethodArgumentTypeMismatchException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ErrorResponseDTO methodArgumentNotValidHandler(final MethodArgumentNotValidException ex) {
    List<String> errors = new ArrayList<>();

    ex.getBindingResult().getAllErrors().forEach(error -> {
      String errorMessage = error.getDefaultMessage();
      errors.add(errorMessage);
    });

    log.error("Invalid incoming request. Errors: {}", errors, ex);
    return ErrorResponseDTO.builder().errors(errors).build();
  }

  @ExceptionHandler(PersistenceException.class)
  @ResponseBody
  public ResponseEntity<ErrorResponseDTO> persistenceExceptionHandler(
      final PersistenceException ex) {
    ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
        .errors(List.of(ex.getMessage())).build();
    return ResponseEntity.status(ex.getHttpCode()).body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public ErrorResponseDTO genericExceptionHandler(final Exception ex) {
    log.error("Generic exception: ", ex);
    return ErrorResponseDTO.builder().errors(Arrays.asList(ex.getMessage())).build();
  }

}
