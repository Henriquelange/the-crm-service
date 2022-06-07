package com.theagilemonkeys.crm.config.filter;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.theagilemonkeys.crm.dto.LoggedInUserDTO;
import com.theagilemonkeys.crm.entity.enums.CognitoAttributeFieldEnum;
import java.io.IOException;
import java.text.ParseException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Profile("!test")
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

  @Autowired
  private ConfigurableJWTProcessor<SecurityContext> jwtProcessorConfig;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
    try {
      final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

      if (!StringUtils.isEmpty(authorizationHeader) && authorizationHeader.startsWith("Bearer")) {

        final String token = authorizationHeader.split(" ")[1].trim();
        JWTClaimsSet claimsSet = jwtProcessorConfig.process(token, null);

        LoggedInUserDTO loggedInUser = LoggedInUserDTO.builder()
            .email(claimsSet.getStringClaim(CognitoAttributeFieldEnum.EMAIL.getName()))
            .build();

        log.debug("Logged user - User {}", loggedInUser.getEmail());

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
            new UsernamePasswordAuthenticationToken(
                loggedInUser, null, null);

        usernamePasswordAuthenticationToken
            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
      }

    } catch (BadJOSEException | ParseException | JOSEException | RuntimeException e) {
      log.error("Error get logged in user - Error:", e);
    }

    filterChain.doFilter(request, response);

  }
}
