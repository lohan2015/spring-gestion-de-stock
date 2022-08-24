package com.kinart.organisation.business.services;

import com.kinart.api.organisation.dto.NiveauDto;

import java.util.List;

public interface NiveauService {

    NiveauDto save(NiveauDto dto);

    NiveauDto findById(Integer id);

    List<NiveauDto> findAll();

    void delete(Integer id);
}
