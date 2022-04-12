package com.cmbassi.gestiondepaie.validator;

import com.cmbassi.gestiondepaie.dto.ParamColumnDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ParamColumnValidator {

  public static List<String> validate(ParamColumnDto dto) {
    List<String> errors = new ArrayList<>();

    if (dto == null) {
      errors.add("Veuillez renseigner le numéro de la table");
      errors.add("Veuillez renseigner le libellé");
      errors.add("Veuillez renseigner le type de la colonne");
      errors.add("Veuillez renseigner le numéro de la ligne");
      return errors;
    }

    if (dto.getCtab()==null) {
      errors.add("Veuillez renseigner le numéro de la table");
    }
    if (!StringUtils.hasLength(dto.getLibe())) {
      errors.add("Veuillez renseigner le libellé");
    }
    if (!StringUtils.hasLength(dto.getCtyp())) {
      errors.add("Veuillez renseigner le type de la colonne");
    }
    if (dto.getNume()==null) {
      errors.add("Veuillez renseigner le numéro de la ligne");
    }
    return errors;
  }

}
