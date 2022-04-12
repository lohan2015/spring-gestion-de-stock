package com.cmbassi.gestiondestock.controller;


import com.cmbassi.gestiondestock.controller.api.AuthenticationApi;
import com.cmbassi.gestiondestock.dto.auth.AuthenticationRequest;
import com.cmbassi.gestiondestock.dto.auth.AuthenticationResponse;
import com.cmbassi.gestiondestock.exception.EntityNotFoundException;
import com.cmbassi.gestiondestock.model.auth.ExtendedUser;
import com.cmbassi.gestiondestock.services.auth.ApplicationUserDetailsService;
import com.cmbassi.gestiondestock.utils.JwtUtil;
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
