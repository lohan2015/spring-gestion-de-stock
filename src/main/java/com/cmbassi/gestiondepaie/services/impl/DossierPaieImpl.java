package com.cmbassi.gestiondepaie.services.impl;

import com.cmbassi.gestiondepaie.dto.DossierPaieDto;
import com.cmbassi.gestiondepaie.model.DossierPaie;
import com.cmbassi.gestiondepaie.repository.DossierPaieRepository;
import com.cmbassi.gestiondepaie.services.DossierPaieService;
import com.cmbassi.gestiondepaie.services.utils.ClsDate;
import com.cmbassi.gestiondepaie.validator.DossierPaieValidator;
import com.cmbassi.gestiondestock.exception.EntityNotFoundException;
import com.cmbassi.gestiondestock.exception.ErrorCodes;
import com.cmbassi.gestiondestock.exception.InvalidEntityException;
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
        DossierPaie dossierPaie = dossierPaieRepository.findByIdEntreprise(idEntreprise);
        if(dossierPaie != null) return dossierPaie.getDdmp();
        return null;
    }

    @Override
    public Integer getNumeroBulletinPaie(Integer idEntreprise) {
        DossierPaie dossierPaie = dossierPaieRepository.findByIdEntreprise(idEntreprise);
        if(dossierPaie != null) return dossierPaie.getDnbu();
        return null;
    }
}
