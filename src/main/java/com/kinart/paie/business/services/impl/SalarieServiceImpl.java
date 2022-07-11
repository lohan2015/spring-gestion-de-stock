package com.kinart.paie.business.services.impl;

import com.kinart.api.gestiondepaie.dto.*;
import com.kinart.paie.business.model.Salarie;
import com.kinart.paie.business.repository.*;
import com.kinart.paie.business.services.SalarieService;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.kinart.paie.business.validator.SalarieValidator;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.ErrorCodes;
import com.kinart.stock.business.exception.InvalidEntityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SalarieServiceImpl implements SalarieService {

    private SalarieRepository salarieRepository;
    private ElementFixeSalaireRepository elementFixeSalaireRepository;
    private CaisseMutuelleSalarieRepository caisseMutuelleSalarieRepository;
    private VirementSalaireRepository virementSalarieRepository;
    private PretInterneRepository pretInterneRepository;
    private GeneriqueConnexionService generiqueConnexionService;

    @Autowired
    public SalarieServiceImpl(GeneriqueConnexionService generiqueConnexionService, SalarieRepository salarieRepository, ElementFixeSalaireRepository elementFixeSalaireRepository, CaisseMutuelleSalarieRepository caisseMutuelleSalarieRepository, VirementSalaireRepository virementSalarieRepository, PretInterneRepository pretInterneRepository) {
        this.salarieRepository = salarieRepository;
        this.elementFixeSalaireRepository = elementFixeSalaireRepository;
        this.caisseMutuelleSalarieRepository = caisseMutuelleSalarieRepository;
        this.virementSalarieRepository = virementSalarieRepository;
        this.pretInterneRepository = pretInterneRepository;
        this.generiqueConnexionService = generiqueConnexionService;
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
        if(dto!=null && dto.getCaisseMutuelleSalarie()!=null && !dto.getCaisseMutuelleSalarie().isEmpty()){
            for(CaisseMutuelleSalarieDto caisseMutDto : dto.getCaisseMutuelleSalarie()){
                    caisseMutuelleSalarieRepository.save(CaisseMutuelleSalarieDto.toEntity(caisseMutDto));
            }
        }


        // Eléments fixes
        salarieRepository.deleteElementFixeByMatricule(dto.getNmat());
        if(dto!=null && dto.getElementFixeSalaire()!=null && !dto.getElementFixeSalaire().isEmpty()){
            for(ElementFixeSalaireDto elementFixeSalaireDto : dto.getElementFixeSalaire()){
                elementFixeSalaireDto.setNmat(dto.getNmat());
                elementFixeSalaireRepository.save(ElementFixeSalaireDto.toEntity(elementFixeSalaireDto));
            }
        }

        // Prêts internes
        salarieRepository.deletePretInterneByMatricule(dto.getNmat());
        if(dto!=null && dto.getPretInterne()!=null && !dto.getPretInterne().isEmpty()){
            for(PretInterneDto pretInterneDto : dto.getPretInterne()){
                pretInterneDto.setNmat(dto.getNmat());
                pretInterneRepository.save(PretInterneDto.toEntity(pretInterneDto));
            }
        }

        // Banques
        salarieRepository.deleteBanqueByMatricule(dto.getNmat());
        if(dto!=null && dto.getVirementSalarie()!=null && !dto.getVirementSalarie().isEmpty()){
            for(VirementSalarieDto virementSalarieDto : dto.getVirementSalarie()){
                virementSalarieDto.setNmat(dto.getNmat());
                virementSalarieDto.setTxchg(BigDecimal.ONE);
                virementSalarieDto.setDvd("N");
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

        List<Salarie> result = generiqueConnexionService.find("FROM Salarie WHERE (nmat like UPPER('%"+nmat+"%') OR nom like UPPER('%"+nmat+"%') OR pren like UPPER('%"+nmat+"%'))");
        return result.stream().map(SalarieDto::fromEntity).collect(Collectors.toList());

        //return salarieRepository.findByMatricule("%"+nmat+"%").stream().map(SalarieDto::fromEntity).collect(Collectors.toList());
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
