package com.kinart.organisation.business.services.impl;

import com.kinart.api.organisation.dto.NiveauDto;
import com.kinart.api.organisation.dto.NiveauemploitypeDto;
import com.kinart.organisation.business.repository.NiveauOrganigrammeRepository;
import com.kinart.organisation.business.services.NiveauEmploitypeService;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.ErrorCodes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NiveauEmploitypeImpl implements NiveauEmploitypeService {

    private NiveauOrganigrammeRepository repository;

    @Autowired
    public NiveauEmploitypeImpl(NiveauOrganigrammeRepository repository){
        this.repository = repository;
    }

    @Override
    public NiveauemploitypeDto save(NiveauemploitypeDto dto) {
        return NiveauemploitypeDto.fromEntity(
                repository.save(
                        NiveauemploitypeDto.toEntity(dto)
                )
        );
    }

    @Override
    public NiveauemploitypeDto findById(Integer id) {
        return repository.findById(id).map(NiveauemploitypeDto::fromEntity).orElseThrow(() ->
                new EntityNotFoundException(
                        "Aucun niveau avec l'ID = " + id + " n'a été trouvé dans la BDD",
                        ErrorCodes.ARTICLE_NOT_FOUND));
    }

    @Override
    public List<NiveauemploitypeDto> findAll() {
        return repository.findAll().stream()
                .map(NiveauemploitypeDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Niveau ID is null");
            return;
        }
        repository.deleteById(id);
    }
}
