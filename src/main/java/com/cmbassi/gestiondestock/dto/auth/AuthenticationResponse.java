package com.cmbassi.gestiondestock.dto.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResponse {

  private String accessToken;

  public AuthenticationResponse() {
  }

  public AuthenticationResponse(String accessToken) {
    this.accessToken = accessToken;
  }
}
