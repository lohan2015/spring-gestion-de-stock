package com.kinart.stock.business.services;

import com.kinart.api.gestiondestock.dto.EntrepriseDto;
import com.kinart.stock.business.exception.InvalidEntityException;

import java.util.List;

public interface EntrepriseService {

  EntrepriseDto save(EntrepriseDto dto) throws InvalidEntityException;

  EntrepriseDto findById(Integer id);

  List<EntrepriseDto> findAll();

  void delete(Integer id);

}
