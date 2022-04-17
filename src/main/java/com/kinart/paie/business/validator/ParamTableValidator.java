package com.kinart.paie.business.validator;

import com.kinart.api.gestiondepaie.dto.ParamTableDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ParamTableValidator {

  public static List<String> validate(ParamTableDto dto) {
    List<String> errors = new ArrayList<>();

    if (dto == null) {
      errors.add("Veuillez renseigner le numéro de la table");
      errors.add("Veuillez renseigner la taille de la clé");
      errors.add("Veuillez renseigner le libellé");
      errors.add("Veuillez renseigner le type");
      return errors;
    }

    if (dto.getCtab()==null) {
      errors.add("Veuillez renseigner le numéro de la table");
    }
    if (dto.getNccl()==0) {
      errors.add("Veuillez renseigner la taille de la clé");
    }
    if (!StringUtils.hasLength(dto.getLibe())) {
      errors.add("Veuillez renseigner le libellé");
    }
    if (!StringUtils.hasLength(dto.getTypc())) {
      errors.add("Veuillez renseigner le type");
    }
    return errors;
  }

}
