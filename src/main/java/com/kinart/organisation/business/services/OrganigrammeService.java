package com.kinart.organisation.business.services;

import com.kinart.api.organisation.dto.OrganigrammeDto;
import com.kinart.api.organisation.dto.RechercheListeOrganigrammeDto;

import java.util.List;

public interface OrganigrammeService {

    OrganigrammeDto save(OrganigrammeDto dto);

    OrganigrammeDto findById(Integer id);

    List<OrganigrammeDto> findAll(RechercheListeOrganigrammeDto search);

    void delete(String codeOrganigramme);

    boolean isCelluleExist(String codeOrganigramme);

    String getPossibilites(String codePere);
}
