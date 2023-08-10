package com.kinart.api.gestiondestock.controller;


import com.kinart.api.gestiondestock.controller.api.AuthenticationApi;
import com.kinart.api.gestiondestock.dto.auth.AuthenticationRequest;
import com.kinart.api.gestiondestock.dto.auth.AuthenticationResponse;
import com.kinart.api.mail.EmailDetails;
import com.kinart.api.mail.service.EmailService;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RestController
public class AuthenticationController implements AuthenticationApi {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private ApplicationUserDetailsService userDetailsService;

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private EmailService emailService;

  @CrossOrigin
  @Override
  public ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest request) {
    //System.out.println("AUTHENTIFICATION 1...................");
        authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getLogin(),
            request.getPassword()
        )
    );
    //System.out.println("END FIXATION PARAM...................");
    try {
      //System.out.println("LOAD USER...................");
      final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getLogin());
      //System.out.println("GENERATE TOCKEN...................");
      final String jwt = jwtUtil.generateToken((ExtendedUser) userDetails);
      //System.out.println("NEW TOCKEN="+jwt);

      // Generate secret 2 factor authentification en send mail
      final String secret2FA = jwtUtil.generateSecret2FA();
      // Envoi notification
      EmailDetails paramMail = new EmailDetails();
      paramMail.setMsgBody("Code secret d'acces au portail salarié: "+secret2FA);
      paramMail.setRecipient(request.getLogin());
      paramMail.setSubject("Accès au portail salarié SONIBANK");
      try {
        emailService.sendSimpleMail(paramMail);
      } catch (Exception e){
        e.printStackTrace();
      }

      return ResponseEntity.ok(AuthenticationResponse.builder().accessToken(jwt).secret2FA(secret2FA).build());
    } catch (UsernameNotFoundException e){
      return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }  catch (EntityNotFoundException e){
      return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (NoSuchAlgorithmException e){
      return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

}
