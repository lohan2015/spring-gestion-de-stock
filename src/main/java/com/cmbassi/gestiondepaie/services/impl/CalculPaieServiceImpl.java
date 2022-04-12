package com.cmbassi.gestiondepaie.services.impl;

import com.cmbassi.gestiondepaie.dto.CalculPaieDto;
import com.cmbassi.gestiondepaie.dto.SalarieDto;
import com.cmbassi.gestiondepaie.repository.*;
import com.cmbassi.gestiondepaie.services.CalculPaieService;
import com.cmbassi.gestiondepaie.validator.CalculPaieValidator;
import com.cmbassi.gestiondepaie.validator.SalarieValidator;
import com.cmbassi.gestiondestock.exception.EntityNotFoundException;
import com.cmbassi.gestiondestock.exception.ErrorCodes;
import com.cmbassi.gestiondestock.exception.InvalidEntityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CalculPaieServiceImpl implements CalculPaieService {

    private CalculPaieRepository calculPaieRepository;

    @Autowired
    public CalculPaieServiceImpl(CalculPaieRepository calculPaieRepository) {
        this.calculPaieRepository = calculPaieRepository;
    }

    @Override
    public CalculPaieDto save(CalculPaieDto dto) {
        List<String> errors = CalculPaieValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Calcul non valide {}", dto);
            throw new InvalidEntityException("Le calcul n'est pas valide", ErrorCodes.ARTICLE_NOT_VALID, errors);
        }

        return CalculPaieDto.fromEntity(
                calculPaieRepository.save(
                        CalculPaieDto.toEntity(dto)
                )
        );
    }

    @Override
    public CalculPaieDto findById(Integer id) {
        if (id == null) {
            log.error("Calcul ID is null");
            return null;
        }

        return calculPaieRepository.findById(id).map(CalculPaieDto::fromEntity).orElseThrow(() ->
                new EntityNotFoundException(
                        "Aucun calcul avec l'ID = " + id + " n'a été trouvé dans la BDD",
                        ErrorCodes.ARTICLE_NOT_FOUND)
        );
    }

    @Override
    public List<CalculPaieDto> findAll() {
        return calculPaieRepository.findAll().stream()
                .map(CalculPaieDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<CalculPaieDto> findByMatriculeAndPeriod(String nmat, String aamm, Integer nbul) {
        if (nmat == null) {
            log.error("Matricule salarié est null");
            return null;
        }

        return calculPaieRepository.findByMatriculeAndPeriod(nmat, aamm, nbul).stream()
                .map(CalculPaieDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Salarie ID is null");
            return;
        }
        calculPaieRepository.deleteById(id);
    }

    @Override
    public boolean calculPaieSalarie(String nmat, String aamm, Integer nbul) {
        // TODO


        return false;
    }
}
