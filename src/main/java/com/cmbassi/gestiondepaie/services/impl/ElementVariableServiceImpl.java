package com.cmbassi.gestiondepaie.services.impl;

import com.cmbassi.gestiondepaie.dto.ElementVariableDetailMoisDto;
import com.cmbassi.gestiondepaie.model.ElementVariableEnteteMois;
import com.cmbassi.gestiondepaie.repository.ElementVariableDetailMoisRepository;
import com.cmbassi.gestiondepaie.repository.ElementVariableEnteteMoisRepository;
import com.cmbassi.gestiondepaie.services.ElementVariableService;
import com.cmbassi.gestiondepaie.validator.ElementVariableValidator;
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
public class ElementVariableServiceImpl implements ElementVariableService {

    private ElementVariableDetailMoisRepository elementVariableDetailMoisRepository;
    private ElementVariableEnteteMoisRepository elementVariableEnteteMoisRepository;

    @Autowired
    public ElementVariableServiceImpl(ElementVariableDetailMoisRepository elementVariableDetailMoisRepository, ElementVariableEnteteMoisRepository elementVariableEnteteMoisRepository) {
        this.elementVariableDetailMoisRepository = elementVariableDetailMoisRepository;
        this.elementVariableEnteteMoisRepository = elementVariableEnteteMoisRepository;
    }

    @Override
    public ElementVariableDetailMoisDto save(ElementVariableDetailMoisDto dto) {
        List<String> errors = ElementVariableValidator.validateDetail(dto);
        if (!errors.isEmpty()) {
            log.error("EV non valide {}", dto);
            throw new InvalidEntityException("L'EV n'est pas valide", ErrorCodes.ARTICLE_NOT_VALID, errors);
        }

        ElementVariableEnteteMois elementVariableEnteteMois
                = elementVariableEnteteMoisRepository.findEntEVByMatricule(dto.getAamm(), dto.getNmat(), dto.getNbul());
        if(elementVariableEnteteMois == null) {
            elementVariableEnteteMois = new ElementVariableEnteteMois();
            elementVariableEnteteMois.setAamm(dto.getAamm());
            elementVariableEnteteMois.setIdEntreprise(dto.getIdEntreprise());
            elementVariableEnteteMois.setNbul(dto.getNbul());
            elementVariableEnteteMois.setNmat(dto.getNmat());
            elementVariableEnteteMoisRepository.save(elementVariableEnteteMois);
        }

        return ElementVariableDetailMoisDto.fromEntity(
                elementVariableDetailMoisRepository.save(
                        ElementVariableDetailMoisDto.toEntity(dto)
                )
        );
    }

    @Override
    public ElementVariableDetailMoisDto findDetailById(Integer id) {
        if (id == null) {
            log.error("EV ID is null");
            return null;
        }

        return elementVariableDetailMoisRepository.findById(id).map(ElementVariableDetailMoisDto::fromEntity).orElseThrow(() ->
                new EntityNotFoundException(
                        "Aucun EV avec l'ID = " + id + " n'a été trouvé dans la BDD",
                        ErrorCodes.ARTICLE_NOT_FOUND)
        );
    }

    @Override
    public List<ElementVariableDetailMoisDto> findDetailAll() {
        return elementVariableDetailMoisRepository.findAll().stream()
                .map(ElementVariableDetailMoisDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("EV ID is null");
            return;
        }
        elementVariableDetailMoisRepository.deleteById(id);
    }
}
