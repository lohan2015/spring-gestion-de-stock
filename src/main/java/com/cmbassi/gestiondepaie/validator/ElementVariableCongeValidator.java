package com.cmbassi.gestiondepaie.validator;

import com.cmbassi.gestiondepaie.dto.ElementVariableCongeDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ElementVariableCongeValidator {

  public static List<String> validate(ElementVariableCongeDto dto) {
    List<String> errors = new ArrayList<>();

    if (dto == null) {
      errors.add("Veuillez renseigner la période");
      errors.add("Veuillez renseigner le matricule");
      errors.add("Veuillez renseigner la date de début");
      errors.add("Veuillez renseigner la date de fin");
      errors.add("Veuillez renseigner le nombre de jour d'absence");
      errors.add("Veuillez renseigner le nombre de jour de congé");
      errors.add("Veuillez renseigner le numéro de bulletin");
      return errors;
    }

    if (!StringUtils.hasLength(dto.getAamm())) {
      errors.add("Veuillez renseigner la période");
    }
    if (!StringUtils.hasLength(dto.getNmat())) {
      errors.add("Veuillez renseigner le matricule");
    }
    if (dto.getDdeb()==null) {
      errors.add("Veuillez renseigner la date de début");
    }
    if (dto.getDfin()==null) {
      errors.add("Veuillez renseigner la date de fin");
    }
    if (dto.getNbja()==null) {
      errors.add("Veuillez renseigner le nombre de jour d'absence");
    }
    if (dto.getNbjc()==null) {
      errors.add("Veuillez renseigner le nombre de jour de congé");
    }
    if (dto.getNbul()==0) {
      errors.add("Veuillez renseigner le numéro de bulletin");
    }

    return errors;
  }

}
