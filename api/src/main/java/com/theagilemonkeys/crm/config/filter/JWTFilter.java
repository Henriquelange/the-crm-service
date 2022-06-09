package com.theagilemonkeys.crm.config.filter;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.theagilemonkeys.crm.dto.AuthenticatedUserDTO;
import com.theagilemonkeys.crm.entity.enums.CognitoAttributeFieldEnum;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
        boolean adminUser = false;
        List<GrantedAuthority> authorities = new ArrayList<>();

        JSONArray userGroups = (JSONArray) claimsSet.getClaim(CognitoAttributeFieldEnum.GROUPS.getName());
        if (userGroups != null) {
          for (var group : userGroups) {
            if (group.toString().equalsIgnoreCase("admin")) {
              authorities.add(new SimpleGrantedAuthority("admin"));
              adminUser = true;
              break;
            }
          }
        }

        AuthenticatedUserDTO authenticatedUser = AuthenticatedUserDTO.builder()
            .email(claimsSet.getStringClaim(CognitoAttributeFieldEnum.USERNAME.getName()))
            .admin(adminUser)
            .build();

        log.debug("Logged in user - User {}", authenticatedUser.getEmail());

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
            new UsernamePasswordAuthenticationToken(
                authenticatedUser, null, authorities);

        usernamePasswordAuthenticationToken
            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
      }

    } catch (BadJOSEException | ParseException | JOSEException | RuntimeException e) {
      log.error("Error to get authenticated user - Error:", e);
    }

    filterChain.doFilter(request, response);

  }
}
