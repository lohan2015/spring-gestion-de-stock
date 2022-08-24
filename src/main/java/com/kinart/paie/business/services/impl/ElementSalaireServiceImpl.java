package com.kinart.paie.business.services.impl;

import com.kinart.api.gestiondepaie.dto.ElementSalaireBaremeDto;
import com.kinart.api.gestiondepaie.dto.ElementSalaireBaseDto;
import com.kinart.api.gestiondepaie.dto.ElementSalaireDto;
import com.kinart.paie.business.model.ElementSalaire;
import com.kinart.paie.business.repository.ElementSalaireBaremeRepository;
import com.kinart.paie.business.repository.ElementSalaireBaseRepository;
import com.kinart.paie.business.repository.ElementSalaireRepository;
import com.kinart.paie.business.services.ElementSalaireService;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.kinart.paie.business.validator.ElementSalaireValidator;
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
public class ElementSalaireServiceImpl implements ElementSalaireService {

    private ElementSalaireBaseRepository elementSalaireBaseRepository;
    private ElementSalaireBaremeRepository elementSalaireBaremeRepository;
    private ElementSalaireRepository elementSalaireRepository;
    private GeneriqueConnexionService generiqueConnexionService;

    @Autowired
    public ElementSalaireServiceImpl(GeneriqueConnexionService generiqueConnexionService, ElementSalaireBaseRepository elementSalaireBaseRepository, ElementSalaireBaremeRepository elementSalaireBaremeRepository, ElementSalaireRepository elementSalaireRepository) {
        this.elementSalaireBaseRepository = elementSalaireBaseRepository;
        this.elementSalaireBaremeRepository = elementSalaireBaremeRepository;
        this.elementSalaireRepository = elementSalaireRepository;
        this.generiqueConnexionService = generiqueConnexionService;
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
        if(dto!=null && dto.getElementSalaireBase()!=null && !dto.getElementSalaireBase().isEmpty()){
            for(ElementSalaireBaseDto elementSalaireBaseDto : dto.getElementSalaireBase()){
                elementSalaireBaseRepository.save(ElementSalaireBaseDto.toEntity(elementSalaireBaseDto));
            }
        }

        // Barème
        elementSalaireRepository.deleteBaremeByCodeElement(dto.getCrub());
        if(dto!=null && dto.getElementSalaireBareme()!=null && !dto.getElementSalaireBareme().isEmpty()){
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

    @Override
    public List<ElementSalaireDto> findDataByKeyWord(String keyword) {
        List<ElementSalaire> result = generiqueConnexionService.find("FROM ElementSalaire WHERE (upper(crub) like upper('%"+keyword+"%') OR upper(lrub) like UPPER('%"+keyword+"%'))");
        return result.stream().map(ElementSalaireDto::fromEntity).collect(Collectors.toList());
    }
}
