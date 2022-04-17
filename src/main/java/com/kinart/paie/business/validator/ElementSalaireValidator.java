package com.kinart.paie.business.validator;

import com.kinart.api.gestiondepaie.dto.ElementSalaireDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ElementSalaireValidator {

  public static List<String> validate(ElementSalaireDto dto) {
    List<String> errors = new ArrayList<>();

    if (dto == null) {
      errors.add("Veuillez renseigner le code de la rubrique");
      errors.add("Veuillez renseigner le libellé de la rubrique");
      errors.add("Veuillez renseigner l'algorithme");
      return errors;
    }

    if (!StringUtils.hasLength(dto.getCrub())) {
      errors.add("Veuillez renseigner le code de la rubrique");
    }
    if (!StringUtils.hasLength(dto.getLrub())) {
      errors.add("Veuillez renseigner le libellé de la rubrique");
    }
    if (dto.getAlgo()==0) {
      errors.add("Veuillez renseigner l'algorithme'");
    }

    return errors;
  }

}
