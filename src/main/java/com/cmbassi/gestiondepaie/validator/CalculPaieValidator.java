package com.cmbassi.gestiondepaie.validator;

import com.cmbassi.gestiondepaie.dto.CalculPaieDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CalculPaieValidator {

  public static List<String> validate(CalculPaieDto dto) {
    List<String> errors = new ArrayList<>();

    if (dto == null) {
      errors.add("Veuillez renseigner le code de la rubrique");
      errors.add("Veuillez renseigner la période");
      errors.add("Veuillez renseigner le montant");
      return errors;
    }

    if (!StringUtils.hasLength(dto.getRubq())) {
      errors.add("Veuillez renseigner le code de la rubrique");
    }
    if (!StringUtils.hasLength(dto.getAamm())) {
      errors.add("Veuillez renseigner la période");
    }
    if (dto.getMont()==null) {
      errors.add("Veuillez renseigner le montant");
    }

    return errors;
  }

}
