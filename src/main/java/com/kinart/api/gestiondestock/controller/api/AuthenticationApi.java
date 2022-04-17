package com.kinart.api.gestiondestock.controller.api;

import static com.kinart.stock.business.utils.Constants.AUTHENTICATION_ENDPOINT;

import com.kinart.api.gestiondestock.dto.auth.AuthenticationRequest;
import com.kinart.api.gestiondestock.dto.auth.AuthenticationResponse;
import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Api("authentication")
public interface AuthenticationApi {

  @PostMapping(value=AUTHENTICATION_ENDPOINT + "/authenticate", produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
  public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request);

}
