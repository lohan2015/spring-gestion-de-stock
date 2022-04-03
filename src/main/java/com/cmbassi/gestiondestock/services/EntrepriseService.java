package com.cmbassi.gestiondestock.services;

import com.cmbassi.gestiondestock.dto.EntrepriseDto;
import java.util.List;

public interface EntrepriseService {

  EntrepriseDto save(EntrepriseDto dto) throws Exception;

  EntrepriseDto findById(Integer id) throws Exception;

  List<EntrepriseDto> findAll() throws Exception;

  void delete(Integer id) throws Exception;

}
