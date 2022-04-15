package com.cmbassi.gestiondepaie.services;

import com.cmbassi.gestiondepaie.dto.LigneEcheancierDto;
import com.cmbassi.gestiondepaie.dto.PretExterneEnteteDto;

import java.util.List;
import java.util.Optional;

public interface PretExterneService {

    public List<LigneEcheancierDto> loadEchancier(String numPret);

    public List<PretExterneEnteteDto> findPretEntetePretByFilter(Optional<String> matricule, Optional<String> nprt, Optional<String> crub);

    public PretExterneEnteteDto save(PretExterneEnteteDto dto, List<LigneEcheancierDto> echeancier);

    public List<LigneEcheancierDto> generateEcheancier(PretExterneEnteteDto dto);
}
