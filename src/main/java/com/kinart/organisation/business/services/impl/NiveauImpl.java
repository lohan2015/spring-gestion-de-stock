package com.kinart.organisation.business.services.impl;

import com.kinart.api.organisation.dto.NiveauDto;
import com.kinart.organisation.business.repository.NiveauRepository;
import com.kinart.organisation.business.services.NiveauService;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.ErrorCodes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NiveauImpl implements NiveauService {

    private NiveauRepository repository;

    @Autowired
    public NiveauImpl(NiveauRepository repository){
        this.repository = repository;
    }

    @Override
    public NiveauDto save(NiveauDto dto) {
        return NiveauDto.fromEntity(
                repository.save(
                        NiveauDto.toEntity(dto)
                )
        );
    }

    @Override
    public NiveauDto findById(Integer id) {
        return repository.findById(id).map(NiveauDto::fromEntity).orElseThrow(() ->
                new EntityNotFoundException(
                        "Aucun niveau avec l'ID = " + id + " n'a été trouvé dans la BDD",
                        ErrorCodes.ARTICLE_NOT_FOUND));
    }

    @Override
    public List<NiveauDto> findAll() {
        return repository.findAll().stream()
                .map(NiveauDto::fromEntity)
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
