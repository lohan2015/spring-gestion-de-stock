package com.kinart.paie.business.services.impl;

import com.kinart.paie.business.model.CongeFictif;
import com.kinart.paie.business.repository.CongeFictifRepository;
import com.kinart.paie.business.services.CongeFictifService;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.ErrorCodes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CongeFictifServiceImpl implements CongeFictifService {

    private CongeFictifRepository congeFictifRepository;

    @Autowired
    public CongeFictifServiceImpl(CongeFictifRepository congeFictifRepository) {
        this.congeFictifRepository = congeFictifRepository;
    }

    @Override
    public boolean save(CongeFictif entity) {
//        List<String> errors = CalculPaieValidator.validate(dto);
//        if (!errors.isEmpty()) {
//            log.error("Calcul non valide {}", dto);
//            throw new InvalidEntityException("Le calcul n'est pas valide", ErrorCodes.ARTICLE_NOT_VALID, errors);
//        }

//        return CalculPaieDto.fromEntity(
//                calculPaieRepository.save(
//                        CalculPaieDto.toEntity(dto)
//                )
//        );
        congeFictifRepository.save(entity);
        return true;
    }

    @Override
    public CongeFictif findById(Integer id) {
        if (id == null) {
            log.error("Calcul ID is null");
            return null;
        }

        //return congeFictifRepository.findById(id);
        return congeFictifRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(
                        "Aucun calcul avec l'ID = " + id + " n'a été trouvé dans la BDD",
                        ErrorCodes.ARTICLE_NOT_FOUND)
        );
    }

    @Override
    public List<CongeFictif> findAll() {
        return congeFictifRepository.findAll().stream()
                .collect(Collectors.toList());
    }

    @Override
    public List<CongeFictif> findByMatriculeAndPeriod(String nmat, String aamm, Integer nbul) {
        if (nmat == null) {
            log.error("Matricule salarié est null");
            return null;
        }

        return congeFictifRepository.findByMatriculeAndPeriod(nmat, aamm, nbul).stream()
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Salarie ID is null");
            return;
        }
        congeFictifRepository.deleteById(id);
    }

}
