package com.cmbassi.gestiondepaie.validator;

import com.cmbassi.gestiondepaie.dto.CalendrierPaieDto;

import java.util.ArrayList;
import java.util.List;

public class CalendrierPaieValidator {

  public static List<String> validate(CalendrierPaieDto dto) {
    List<String> errors = new ArrayList<>();

    if (dto == null) {
      errors.add("Veuillez renseigner le jour");
      errors.add("Veuillez renseigner la semaine");
      return errors;
    }

    if (dto.getJour()==null) {
      errors.add("Veuillez renseigner le jour");
    }
    if (dto.getNsem()==null) {
      errors.add("Veuillez renseigner la semaine");
    }

    return errors;
  }

}
