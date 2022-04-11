package com.cmbassi.gestiondestock.services;

import com.cmbassi.gestiondestock.dto.EntrepriseDto;
import com.cmbassi.gestiondestock.exception.InvalidEntityException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EntrepriseService {

  EntrepriseDto save(EntrepriseDto dto) throws InvalidEntityException;

  EntrepriseDto findById(Integer id);

  List<EntrepriseDto> findAll();

  void delete(Integer id);

}
