package com.cmbassi.gestiondepaie.validator;

import com.cmbassi.gestiondepaie.dto.ParamDataDto;

import java.util.ArrayList;
import java.util.List;

public class ParamDataValidator {

  public static List<String> validate(ParamDataDto dto) {
    List<String> errors = new ArrayList<>();

    if (dto == null) {
      errors.add("Veuillez renseigner le numéro de la table");
      errors.add("Veuillez renseigner le numéro de la ligne");
      return errors;
    }

    if (dto.getCtab()==null) {
      errors.add("Veuillez renseigner le numéro de la table");
    }
    if (dto.getNume()==null) {
      errors.add("Veuillez renseigner le numéro de la ligne");
    }
    return errors;
  }

}
