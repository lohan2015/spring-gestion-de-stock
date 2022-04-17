package com.kinart.api.gestiondestock.controller;


import com.kinart.api.gestiondestock.controller.api.AuthenticationApi;
import com.kinart.api.gestiondestock.dto.auth.AuthenticationRequest;
import com.kinart.api.gestiondestock.dto.auth.AuthenticationResponse;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.model.auth.ExtendedUser;
import com.kinart.stock.business.services.auth.ApplicationUserDetailsService;
import com.kinart.stock.business.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController implements AuthenticationApi {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private ApplicationUserDetailsService userDetailsService;

  @Autowired
  private JwtUtil jwtUtil;

  @Override
  public ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getLogin(),
            request.getPassword()
        )
    );

    try {
      final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getLogin());
      final String jwt = jwtUtil.generateToken((ExtendedUser) userDetails);
      return ResponseEntity.ok(AuthenticationResponse.builder().accessToken(jwt).build());
    } catch (UsernameNotFoundException e){
      return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }  catch (EntityNotFoundException e){
      return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

}
