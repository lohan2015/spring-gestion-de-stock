package com.kinart.paie.business.services.impl;

import com.kinart.api.gestiondepaie.dto.DossierPaieDto;
import com.kinart.paie.business.model.DossierPaie;
import com.kinart.paie.business.repository.DossierPaieRepository;
import com.kinart.paie.business.services.DossierPaieService;
import com.kinart.paie.business.services.utils.ClsDate;
import com.kinart.paie.business.validator.DossierPaieValidator;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.ErrorCodes;
import com.kinart.stock.business.exception.InvalidEntityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DossierPaieImpl implements DossierPaieService {

    private DossierPaieRepository dossierPaieRepository;

    @Autowired
    public DossierPaieImpl(DossierPaieRepository dossierPaieRepository) {
        this.dossierPaieRepository = dossierPaieRepository;
    }

    @Override
    public DossierPaieDto save(DossierPaieDto dto) {
        List<String> errors = DossierPaieValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Dossier non valide {}", dto);
            throw new InvalidEntityException("Le dossier n'est pas valide", ErrorCodes.ARTICLE_NOT_VALID, errors);
        }

        return DossierPaieDto.fromEntity(
                dossierPaieRepository.save(
                        DossierPaieDto.toEntity(dto)
                )
        );
    }

    @Override
    public DossierPaieDto findById(Integer id) {
        if (id == null) {
            log.error("Dossier ID is null");
            return null;
        }

        return dossierPaieRepository.findById(id).map(DossierPaieDto::fromEntity).orElseThrow(() ->
                new EntityNotFoundException(
                        "Aucun dossier avec l'ID = " + id + " n'a été trouvé dans la BDD",
                        ErrorCodes.ARTICLE_NOT_FOUND)
        );
    }

    @Override
    public List<DossierPaieDto> findAll() {
        return dossierPaieRepository.findAll().stream()
                .map(DossierPaieDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Dossier ID is null");
            return;
        }
        dossierPaieRepository.deleteById(id);
    }

    @Override
    public Date getMoisDePaieCourant(Integer idEntreprise) {
        //DossierPaie dossierPaie = dossierPaieRepository.findByIdEntreprise(idEntreprise);
        //if(dossierPaie != null) return dossierPaie.getDdmp();
        return new ClsDate("01/01/2024").getDate();
    }

    @Override
    public Integer getNumeroBulletinPaie(Integer idEntreprise) {
        //DossierPaie dossierPaie = dossierPaieRepository.findByIdEntreprise(idEntreprise);
        //if(dossierPaie != null) return dossierPaie.getDnbu();
        return 9;
    }
}
