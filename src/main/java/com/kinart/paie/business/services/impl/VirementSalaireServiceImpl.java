package com.kinart.paie.business.services.impl;

import com.kinart.api.gestiondepaie.dto.LogMessageDto;
import com.kinart.api.gestiondepaie.dto.VirementSalarieDto;
import com.kinart.paie.business.repository.VirementSalaireRepository;
import com.kinart.paie.business.services.VirementSalaireService;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.ErrorCodes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VirementSalaireServiceImpl implements VirementSalaireService {

    private VirementSalaireRepository virementSalaireRepository;

    @Autowired
    public VirementSalaireServiceImpl(VirementSalaireRepository virementSalaireRepository) {
        this.virementSalaireRepository = virementSalaireRepository;
    }

    @Override
    public VirementSalarieDto save(VirementSalarieDto dto) {
        return VirementSalarieDto.fromEntity(
                virementSalaireRepository.save(
                        VirementSalarieDto.toEntity(dto)
                )
        );
    }

    @Override
    public VirementSalarieDto findById(Integer id) {
        if (id == null) {
            log.error("Virement ID is null");
            return null;
        }

        return virementSalaireRepository.findById(id).map(VirementSalarieDto::fromEntity).orElseThrow(() ->
                new EntityNotFoundException(
                        "Aucun virement avec l'ID = " + id + " n'a été trouvé dans la BDD",
                        ErrorCodes.ARTICLE_NOT_FOUND)
        );
    }

    @Override
    public VirementSalarieDto findByMatricule(String matricule) {
        return VirementSalarieDto.fromEntity(virementSalaireRepository.findBySalarie(matricule));
    }

    @Override
    public List<VirementSalarieDto> findAll() {
        return virementSalaireRepository.findAll().stream()
                .map(VirementSalarieDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Dossier ID is null");
            return;
        }
        virementSalaireRepository.deleteById(id);
    }
}
