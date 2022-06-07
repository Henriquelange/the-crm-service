package com.theagilemonkeys.crm.config.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

  private final ObjectMapper mapper = new ObjectMapper();

  private static final String UNAUTHORIZED_MESSAGE = "Unauthorized";

  @Override
  public void commence(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse, AuthenticationException e)
      throws IOException {

    httpServletResponse.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
    httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
    httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
    httpServletResponse.getWriter().println(getUnauthorizedError());

  }

  private String getUnauthorizedError() {
    List<String> errors = List.of(UNAUTHORIZED_MESSAGE);
    try {
      return mapper.writeValueAsString(errors);
    } catch (JsonProcessingException e) {
      return UNAUTHORIZED_MESSAGE;
    }
  }
}
