package com.kinart.paie.business.validator;

import com.kinart.api.gestiondepaie.dto.ElementVariableDetailMoisDto;
import com.kinart.api.gestiondepaie.dto.ElementVariableEnteteMoisDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ElementVariableValidator {

  public static List<String> validateEntete(ElementVariableEnteteMoisDto entDto) {
    List<String> errors = new ArrayList<>();

    if (entDto == null) {
      errors.add("Veuillez renseigner le matricule");
      errors.add("Veuillez renseigner la période");
      errors.add("Veuillez renseigner le numéro du bulletin");
      return errors;
    }

    if (!StringUtils.hasLength(entDto.getAamm())) {
      errors.add("Veuillez renseigner la période");
    }
    if (!StringUtils.hasLength(entDto.getNmat())) {
      errors.add("Veuillez renseigner le matricule");
    }
    if (entDto.getNbul()==null) {
      errors.add("Veuillez renseigner le numéro du bulletin");
    }

    return errors;
  }

  public static List<String> validateDetail(ElementVariableDetailMoisDto detDto) {
    List<String> errors = new ArrayList<>();

    if (detDto == null) {
      errors.add("Veuillez renseigner le matricule");
      errors.add("Veuillez renseigner la période");
      errors.add("Veuillez renseigner le numéro du bulletin");
      errors.add("Veuillez renseigner le montant");
      return errors;
    }

    if (!StringUtils.hasLength(detDto.getAamm())) {
      errors.add("Veuillez renseigner la période");
    }
    if (!StringUtils.hasLength(detDto.getNmat())) {
      errors.add("Veuillez renseigner le matricule");
    }
    if (detDto.getNbul()==null) {
      errors.add("Veuillez renseigner le numéro du bulletin");
    }
    if (detDto.getMont()==null) {
      errors.add("Veuillez renseigner le montant");
    }

    return errors;
  }

}
