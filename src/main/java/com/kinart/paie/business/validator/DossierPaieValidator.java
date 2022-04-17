package com.kinart.paie.business.validator;

import com.kinart.api.gestiondepaie.dto.DossierPaieDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class DossierPaieValidator {

  public static List<String> validate(DossierPaieDto dto) {
    List<String> errors = new ArrayList<>();

    if (dto == null) {
      errors.add("Veuillez renseigner le nom réduit");
      errors.add("Veuillez renseigner la description");
      errors.add("Veuillez renseigner le dernier mois de paie");
      errors.add("Veuillez renseigner la date de début d'exerice");
      errors.add("Veuillez renseigner la date de fin d'exerice");
      errors.add("Veuillez renseigner la dernier bulletin calculé");
      return errors;
    }

    if (!StringUtils.hasLength(dto.getNred())) {
      errors.add("Veuillez renseigner le nom réduit");
    }
    if (!StringUtils.hasLength(dto.getDrsc())) {
      errors.add("Veuillez renseigner la description");
    }
    if (dto.getDdmp()==null) {
      errors.add("Veuillez renseigner le dernier mois de paie");
    }
    if (dto.getDdex()==null) {
      errors.add("Veuillez renseigner la date de début d'exerice");
    }
    if (dto.getDfex()==null) {
      errors.add("Veuillez renseigner la date de fin d'exerice");
    }
    if (dto.getDnbu()==null) {
      errors.add("Veuillez renseigner la dernier bulletin calculé");
    }

    return errors;
  }

}
