package com.cmbassi.gestiondepaie.services.impl;

import com.cmbassi.gestiondepaie.dto.*;
import com.cmbassi.gestiondepaie.repository.*;
import com.cmbassi.gestiondepaie.services.SalarieService;
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
public class SalarieServiceImpl implements SalarieService {

    private SalarieRepository salarieRepository;
    private ElementFixeSalaireRepository elementFixeSalaireRepository;
    private CaisseMutuelleSalarieRepository caisseMutuelleSalarieRepository;
    private VirementSalarieRepository virementSalarieRepository;
    private PretInterneRepository pretInterneRepository;

    @Autowired
    public SalarieServiceImpl(SalarieRepository salarieRepository, ElementFixeSalaireRepository elementFixeSalaireRepository, CaisseMutuelleSalarieRepository caisseMutuelleSalarieRepository, VirementSalarieRepository virementSalarieRepository, PretInterneRepository pretInterneRepository) {
        this.salarieRepository = salarieRepository;
        this.elementFixeSalaireRepository = elementFixeSalaireRepository;
        this.caisseMutuelleSalarieRepository = caisseMutuelleSalarieRepository;
        this.virementSalarieRepository = virementSalarieRepository;
        this.pretInterneRepository = pretInterneRepository;
    }

    @Override
    public SalarieDto save(SalarieDto dto) {
        List<String> errors = SalarieValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Salarié non valide {}", dto);
            throw new InvalidEntityException("Le salarié n'est pas valide", ErrorCodes.ARTICLE_NOT_VALID, errors);
        }

        // Caisses et mutuelles
        salarieRepository.deleteCaisseMutuelleByMatricule(dto.getNmat());
        if(!dto.getCaisseMutuelleSalarie().isEmpty()){
            for(CaisseMutuelleSalarieDto caisseMutDto : dto.getCaisseMutuelleSalarie()){
                    caisseMutuelleSalarieRepository.save(CaisseMutuelleSalarieDto.toEntity(caisseMutDto));
            }
        }


        // Eléments fixes
        salarieRepository.deleteElementFixeByMatricule(dto.getNmat());
        if(!dto.getElementFixeSalaire().isEmpty()){
            for(ElementFixeSalaireDto elementFixeSalaireDto : dto.getElementFixeSalaire()){
                elementFixeSalaireRepository.save(ElementFixeSalaireDto.toEntity(elementFixeSalaireDto));
            }
        }

        // Prêts internes
        salarieRepository.deletePretInterneByMatricule(dto.getNmat());
        if(!dto.getPretInterne().isEmpty()){
            for(PretInterneDto pretInterneDto : dto.getPretInterne()){
                pretInterneRepository.save(PretInterneDto.toEntity(pretInterneDto));
            }
        }

        // Banques
        salarieRepository.deleteBanqueByMatricule(dto.getNmat());
        if(!dto.getVirementSalarie().isEmpty()){
            for(VirementSalarieDto virementSalarieDto : dto.getVirementSalarie()){
                virementSalarieRepository.save(VirementSalarieDto.toEntity(virementSalarieDto));
            }
        }

        return SalarieDto.fromEntity(
                salarieRepository.save(
                        SalarieDto.toEntity(dto)
                )
        );
    }

    @Override
    public SalarieDto findById(Integer id) {
        if (id == null) {
            log.error("Salarie ID is null");
            return null;
        }

        return salarieRepository.findById(id).map(SalarieDto::fromEntity).orElseThrow(() ->
                new EntityNotFoundException(
                        "Aucun salarié avec l'ID = " + id + " n'a été trouvé dans la BDD",
                        ErrorCodes.ARTICLE_NOT_FOUND)
        );
    }

    @Override
    public List<SalarieDto> findAll() {
        return salarieRepository.findAll().stream()
                .map(SalarieDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<SalarieDto> findByMatricule(String nmat) {
        if (nmat == null) {
            log.error("Matricule salarié est null");
            return null;
        }

        return salarieRepository.findByMatricule("%"+nmat+"%").stream()
                .map(SalarieDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public List<SalarieDto> findByName(String nom) {
        return salarieRepository.findByName("%"+nom+"%").stream()
                .map(SalarieDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public List<SalarieDto> findByMatriculeInactif(String nmat) {
        if (nmat == null) {
            log.error("Matricule salarié est null");
            return null;
        }

        return salarieRepository.findByMatricule("%"+nmat+"%").stream()
                .map(SalarieDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Salarie ID is null");
            return;
        }

        // Caisses et mutuelles
        salarieRepository.deleteCaisseMutuelleByIdSalarie(id);

        // Eléments fixes
        salarieRepository.deleteElementFixeByIdSalarie(id);

        // Prêts internes
        salarieRepository.deletePretInterneByIdSalarie(id);

        // Banques
        salarieRepository.deleteBanqueByIdSalarie(id);

        salarieRepository.deleteById(id);
    }
}