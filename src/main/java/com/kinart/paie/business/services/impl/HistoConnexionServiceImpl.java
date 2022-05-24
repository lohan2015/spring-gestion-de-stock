package com.kinart.paie.business.services.impl;

import com.kinart.api.gestiondepaie.dto.HistoConnexionDto;
import com.kinart.paie.business.repository.HistoConnexionRepository;
import com.kinart.paie.business.services.HistoConnexionService;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.ErrorCodes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HistoConnexionServiceImpl implements HistoConnexionService {

    private HistoConnexionRepository histoConnexionRepository;

    @Autowired
    public HistoConnexionServiceImpl(HistoConnexionRepository histoConnexionRepository) {
        this.histoConnexionRepository = histoConnexionRepository;
    }

    @Override
    public HistoConnexionDto save(HistoConnexionDto dto) {
        return HistoConnexionDto.fromEntity(
                histoConnexionRepository.save(
                        HistoConnexionDto.toEntity(dto)
                )
        );
    }

    @Override
    public HistoConnexionDto findById(Integer id) {
        if (id == null) {
            log.error("Dossier ID is null");
            return null;
        }

        return histoConnexionRepository.findById(id).map(HistoConnexionDto::fromEntity).orElseThrow(() ->
                new EntityNotFoundException(
                        "Aucun message avec l'ID = " + id + " n'a été trouvé dans la BDD",
                        ErrorCodes.ARTICLE_NOT_FOUND)
        );
    }

    @Override
    public List<HistoConnexionDto> findAll() {
        return histoConnexionRepository.findAll().stream()
                .map(HistoConnexionDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Dossier ID is null");
            return;
        }
        histoConnexionRepository.deleteById(id);
    }

    @Override
    public List<HistoConnexionDto> findByUser(String user) {
        return histoConnexionRepository.findByUser(user).stream()
                .map(HistoConnexionDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<HistoConnexionDto> findByUserAndOperation(String user, String optype) {
        return histoConnexionRepository.findByUserAndOperation(user, "%"+optype+"%").stream()
                .map(HistoConnexionDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<HistoConnexionDto> findByUserAndNoOperation(String user, String optype) {
        return histoConnexionRepository.findByUserAndOperation(user, "%"+optype+"%").stream()
                .map(HistoConnexionDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<HistoConnexionDto> findByOperation(String optype) {
        return histoConnexionRepository.findByOperation("%"+optype+"%").stream()
                .map(HistoConnexionDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<HistoConnexionDto> findByNoOperation(String optype) {
        return histoConnexionRepository.findByNoOperation("%"+optype+"%").stream()
                .map(HistoConnexionDto::fromEntity)
                .collect(Collectors.toList());
    }
}
