package com.kinart.organisation.business.services;

import com.kinart.api.organisation.dto.PosteDto;

import java.util.List;

public interface PosteService {

    PosteDto save(PosteDto dto);

    PosteDto findById(Integer id);

    List<PosteDto> findAllPoste(String codeDossier);

    List<PosteDto> findAllMetier(String codeDossier);

    void delete(Integer id);
}
