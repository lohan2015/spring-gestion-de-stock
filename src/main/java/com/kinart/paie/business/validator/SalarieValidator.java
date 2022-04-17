package com.kinart.paie.business.validator;

import com.kinart.api.gestiondepaie.dto.SalarieDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SalarieValidator {

  public static List<String> validate(SalarieDto dto) {
    List<String> errors = new ArrayList<>();

    if (dto == null) {
      errors.add("Veuillez renseigner le matricule du salarié");
      errors.add("Veuillez renseigner le nom / prénom du salarié");
      errors.add("Veuillez renseigner la direction du salarié");
      errors.add("Veuillez renseigner le département du salarié");
      errors.add("Veuillez renseigner le service du salarié");
      return errors;
    }

    if (!StringUtils.hasLength(dto.getNmat())) {
      errors.add("Veuillez renseigner le matricule du salarié");
    }
    if (!StringUtils.hasLength(dto.getNom())) {
      errors.add("Veuillez renseigner le nom / prénom du salarié");
    }
    if (!StringUtils.hasLength(dto.getNiv1())) {
      errors.add("Veuillez renseigner la direction du salarié");
    }
    if (!StringUtils.hasLength(dto.getNiv2())) {
      errors.add("Veuillez renseigner le département du salarié");
    }
    if (!StringUtils.hasLength(dto.getNiv3())) {
      errors.add("Veuillez renseigner le service du salarié");
    }
    return errors;
  }

}
