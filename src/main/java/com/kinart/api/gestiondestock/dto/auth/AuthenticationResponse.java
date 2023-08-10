package com.kinart.api.gestiondestock.dto.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResponse {

  private String accessToken;

  private String secret2FA;

  public AuthenticationResponse() {
  }

  public AuthenticationResponse(String accessToken, String secret2FA) {

    this.accessToken = accessToken;
    this.secret2FA = secret2FA;

  }
}
