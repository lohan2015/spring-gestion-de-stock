package com.kinart.api.portail.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElementVarCongeDto {

    @NotNull(message = "Le dossier ne doit pas etre vide")
    @NotEmpty(message = "Le dossier ne doit pas etre vide")
    @NotBlank(message = "Le dossier ne doit pas etre vide")
    private String cdos;

    @NotNull(message = "La période ne doit pas etre vide")
    @NotEmpty(message = "La période ne doit pas etre vide")
    @NotBlank(message = "La période ne doit pas etre vide")
    private String aamm;

    @NotNull(message = "Le matricule ne doit pas etre vide")
    @NotEmpty(message = "Le matricule ne doit pas etre vide")
    @NotBlank(message = "Le matricule ne doit pas etre vide")
    private String nmat;

    @NotNull(message = "Le numéro de bulletin ne doit pas etre vide")
    private Integer nbul;

    @NotNull(message = "La date de début ne doit pas etre vide")
    private Date ddeb;

    @NotNull(message = "La date de fin ne doit pas etre vide")
    private Date dfin;

    @NotNull(message = "Le nombre de jour de congé ne doit pas etre vide")
    private BigDecimal nbjc;

    @NotNull(message = "Le nombre de jour d'absence ne doit pas etre vide")
    private BigDecimal nbja;

    @NotNull(message = "Le motif ne doit pas etre vide")
    @NotEmpty(message = "Le motif ne doit pas etre vide")
    @NotBlank(message = "Le motif ne doit pas etre vide")
    private String motf;

    private BigDecimal mont;

    @NotNull(message = "Le code utilisateur ne doit pas etre vide")
    @NotEmpty(message = "Le code utilisateur ne doit pas etre vide")
    @NotBlank(message = "Le code utilisateur ne doit pas etre vide")
    private String cuti;

    private String estUnCOnge = "N";
    private BigDecimal nbjcTotal;
    private BigDecimal nbjaTotal;
    private Date minDeb;
    private Date maxFin;
    private Date firstDay;
    private Date lastDay;

}
