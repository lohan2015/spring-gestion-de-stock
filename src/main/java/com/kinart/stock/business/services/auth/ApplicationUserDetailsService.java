package com.kinart.stock.business.services.auth;

import com.kinart.api.gestiondestock.dto.UtilisateurDto;
import com.kinart.stock.business.model.auth.ExtendedUser;
import com.kinart.stock.business.services.UtilisateurService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserDetailsService implements UserDetailsService {

  @Autowired
  private UtilisateurService service;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    System.out.println("CHARGEMENT USER 1..................."+email);
    UtilisateurDto utilisation = service.findByEmail(email);

    System.out.println("NOMBRE ROLE.................."+utilisation.getRoles().size());
    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
    utilisation.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getRoleName().getCode())));

    return new ExtendedUser(utilisation.getEmail(), utilisation.getMoteDePasse(), utilisation.getEntreprise().getId()
            , authorities);
  }
}
