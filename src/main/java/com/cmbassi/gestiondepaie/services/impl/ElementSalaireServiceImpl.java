package com.cmbassi.gestiondepaie.services.impl;

import com.cmbassi.gestiondepaie.dto.*;
import com.cmbassi.gestiondepaie.repository.ElementSalaireBaremeRepository;
import com.cmbassi.gestiondepaie.repository.ElementSalaireBaseRepository;
import com.cmbassi.gestiondepaie.repository.ElementSalaireRepository;
import com.cmbassi.gestiondepaie.services.ElementSalaireService;
import com.cmbassi.gestiondepaie.validator.ElementSalaireValidator;
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
public class ElementSalaireServiceImpl implements ElementSalaireService {

    private ElementSalaireBaseRepository elementSalaireBaseRepository;
    private ElementSalaireBaremeRepository elementSalaireBaremeRepository;
    private ElementSalaireRepository elementSalaireRepository;

    @Autowired
    public ElementSalaireServiceImpl(ElementSalaireBaseRepository elementSalaireBaseRepository, ElementSalaireBaremeRepository elementSalaireBaremeRepository, ElementSalaireRepository elementSalaireRepository) {
        this.elementSalaireBaseRepository = elementSalaireBaseRepository;
        this.elementSalaireBaremeRepository = elementSalaireBaremeRepository;
        this.elementSalaireRepository = elementSalaireRepository;
    }

    @Override
    public ElementSalaireDto save(ElementSalaireDto dto) {
        List<String> errors = ElementSalaireValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Element non valide {}", dto);
            throw new InvalidEntityException("L'élément n'est pas valide", ErrorCodes.ARTICLE_NOT_VALID, errors);
        }

        // Base de calcul
        elementSalaireRepository.deleteBaseCalculByCodeElement(dto.getCrub());
        if(!dto.getElementSalaireBase().isEmpty()){
            for(ElementSalaireBaseDto elementSalaireBaseDto : dto.getElementSalaireBase()){
                elementSalaireBaseRepository.save(ElementSalaireBaseDto.toEntity(elementSalaireBaseDto));
            }
        }

        // Barème
        elementSalaireRepository.deleteBaremeByCodeElement(dto.getCrub());
        if(!dto.getElementSalaireBareme().isEmpty()){
            for(ElementSalaireBaremeDto elementSalaireBaremeDto : dto.getElementSalaireBareme()){
                elementSalaireBaremeRepository.save(ElementSalaireBaremeDto.toEntity(elementSalaireBaremeDto));
            }
        }

        return ElementSalaireDto.fromEntity(
                elementSalaireRepository.save(
                        ElementSalaireDto.toEntity(dto)
                )
        );
    }

    @Override
    public ElementSalaireDto findById(Integer id) {
        if (id == null) {
            log.error("Element ID is null");
            return null;
        }

        return elementSalaireRepository.findById(id).map(ElementSalaireDto::fromEntity).orElseThrow(() ->
                new EntityNotFoundException(
                        "Aucun élément avec l'ID = " + id + " n'a été trouvé dans la BDD",
                        ErrorCodes.ARTICLE_NOT_FOUND)
        );
    }

    @Override
    public List<ElementSalaireDto> findAll() {
        return elementSalaireRepository.findAll().stream()
                .map(ElementSalaireDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ElementSalaireDto> findByCode(String code) {
        if (code == null) {
            log.error("Code élément null");
            return null;
        }

        return elementSalaireRepository.findByCodeRubrique("%"+code+"%").stream()
                .map(ElementSalaireDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public List<ElementSalaireDto> findByLibelle(String libelle) {
        if (libelle == null) {
            log.error("Libellé élément null");
            return null;
        }

        return elementSalaireRepository.findByLibelleRubrique("%"+libelle+"%").stream()
                .map(ElementSalaireDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Element ID is null");
            return;
        }

        // Base de calcul
        elementSalaireRepository.deleteBaseCalculByIdElement(id);

        // Barème
        elementSalaireRepository.deleteBaremeByIdElement(id);

        elementSalaireRepository.deleteById(id);
    }
}
