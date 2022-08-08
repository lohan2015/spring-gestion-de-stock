package com.kinart.organisation.business.validator;

import com.kinart.api.organisation.dto.PosteDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class PosteValidator {

  public static List<String> validate(PosteDto adresseDto) {
    List<String> errors = new ArrayList<>();

    if (adresseDto == null) {
      errors.add("Veuillez renseigner le code poste");
      errors.add("Veuillez renseigner l'emploi type");
      errors.add("Veuillez renseigner le libellé");
      errors.add("Veuillez renseigner la direction");
      errors.add("Veuillez renseigner le département");
      errors.add("Veuillez renseigner le service");
      errors.add("Veuillez renseigner la classe");
      errors.add("Veuillez renseigner la filière");
      errors.add("Veuillez renseigner la spécialité");
      errors.add("Veuillez renseigner le type de formation");
      errors.add("Veuillez renseigner le niveau de formation");
      errors.add("Veuillez renseigner la catégorie");
      errors.add("Veuillez renseigner l'échelon");
      return errors;
    }
    if (!StringUtils.hasLength(adresseDto.getCodeposte())) {
      errors.add("Veuillez renseigner le code poste");
    }
    if (!StringUtils.hasLength(adresseDto.getFonc())) {
      errors.add("Veuillez renseigner l'emploi type");
    }
    if (!StringUtils.hasLength(adresseDto.getLibelle())) {
      errors.add("Veuillez renseigner le libellé");
    }
    if (!StringUtils.hasLength(adresseDto.getNiv1())) {
      errors.add("Veuillez renseigner la direction");
    }
    if (!StringUtils.hasLength(adresseDto.getNiv2())) {
      errors.add("Veuillez renseigner le département");
    }
    if (!StringUtils.hasLength(adresseDto.getNiv1())) {
      errors.add("Veuillez renseigner le service");
    }
    if (!StringUtils.hasLength(adresseDto.getNiv1())) {
      errors.add("Veuillez renseigner la classe");
    }
    if (!StringUtils.hasLength(adresseDto.getNiv1())) {
      errors.add("Veuillez renseigner la filière");
    }
    if (!StringUtils.hasLength(adresseDto.getNiv1())) {
      errors.add("Veuillez renseigner la spécialité");
    }
    if (!StringUtils.hasLength(adresseDto.getNiv1())) {
      errors.add("Veuillez renseigner le type de formation");
    }
    if (!StringUtils.hasLength(adresseDto.getNiv1())) {
      errors.add("Veuillez renseigner le niveau de formation");
    }
    if (!StringUtils.hasLength(adresseDto.getNiv1())) {
      errors.add("Veuillez renseigner la catégorie");
    }
    if (!StringUtils.hasLength(adresseDto.getNiv1())) {
      errors.add("Veuillez renseigner l'échelon");
    }
    return errors;
  }

}
