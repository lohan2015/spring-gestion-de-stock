package com.kinart.organisation.business.services;

import com.kinart.api.organisation.dto.NiveauemploitypeDto;

import java.util.List;

public interface NiveauEmploitypeService {

    NiveauemploitypeDto save(NiveauemploitypeDto dto);

    NiveauemploitypeDto findById(Integer id);

    List<NiveauemploitypeDto> findAll();

    void delete(Integer id);
}
