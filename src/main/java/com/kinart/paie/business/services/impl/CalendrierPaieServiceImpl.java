package com.kinart.paie.business.services.impl;

import com.kinart.api.gestiondepaie.dto.CalendrierPaieDto;
import com.kinart.paie.business.repository.CalendrierPaieRepository;
import com.kinart.paie.business.services.CalendrierPaieService;
import com.kinart.paie.business.validator.CalendrierPaieValidator;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.ErrorCodes;
import com.kinart.stock.business.exception.InvalidEntityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CalendrierPaieServiceImpl implements CalendrierPaieService {

    private CalendrierPaieRepository calendrierPaieRepository;

    @Autowired
    public CalendrierPaieServiceImpl(CalendrierPaieRepository calendrierPaieRepository) {
        this.calendrierPaieRepository = calendrierPaieRepository;
    }

    @Override
    public CalendrierPaieDto save(CalendrierPaieDto dto) {
        List<String> errors = CalendrierPaieValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Calendrier non valide {}", dto);
            throw new InvalidEntityException("Le calendrier n'est pas valide", ErrorCodes.ARTICLE_NOT_VALID, errors);
        }

        return CalendrierPaieDto.fromEntity(
                calendrierPaieRepository.save(
                        CalendrierPaieDto.toEntity(dto)
                )
        );
    }

    @Override
    public CalendrierPaieDto findById(Integer id) {
        if (id == null) {
            log.error("Calendrier ID is null");
            return null;
        }

        return calendrierPaieRepository.findById(id).map(CalendrierPaieDto::fromEntity).orElseThrow(() ->
                new EntityNotFoundException(
                        "Aucun calendrier avec l'ID = " + id + " n'a été trouvé dans la BDD",
                        ErrorCodes.ARTICLE_NOT_FOUND)
        );
    }

    @Override
    public List<CalendrierPaieDto> findAll() {
        return calendrierPaieRepository.findAll().stream()
                .map(CalendrierPaieDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Calendrier ID is null");
            return;
        }
        calendrierPaieRepository.deleteById(id);
    }
}
