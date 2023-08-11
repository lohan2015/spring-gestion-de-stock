package com.kinart.api.portail.dto;

import com.kinart.portail.business.model.DemandeAbsenceConge;
import com.kinart.portail.business.utils.EnumStatusType;
import com.kinart.stock.business.model.Utilisateur;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

/** @author c.mbassi */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Model des demande absences / congé")
public class DemandeAbsenceCongeDto implements Serializable {

    private Integer id;
    private Instant creationDate;
    private Instant lastModifiedDate;

    @NotNull(message = "L'id entreprise ne doit pas etre null")
    private Integer idEntreprise = 1;

    private Utilisateur userDemAbsCg = new Utilisateur();

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dteDebut;

     @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dteFin;

    @NotNull(message = "Le motif ne doit pas etre vide")
    @NotEmpty(message = "Le motif ne doit pas etre vide")
    @NotBlank(message = "Le motif ne doit pas etre vide")
    private String motif;

    private String raison;

    @NotNull(message = "Le validateur 1 ne doit pas etre vide")
    @NotEmpty(message = "Le validateur 1 ne doit pas etre vide")
    @NotBlank(message = "Le validateur 1 ne doit pas etre vide")
    private String valid1;

    @NotNull(message = "Le validateur 2 ne doit pas etre vide")
    @NotEmpty(message = "Le validateur 2 ne doit pas etre vide")
    @NotBlank(message = "Le validateur 2 ne doit pas etre vide")
    private String valid2;

    private String valid3;

    private String valid4;

    private EnumStatusType status1;

    private EnumStatusType status2;

    private EnumStatusType status3;

    private EnumStatusType status4;

    private String commentN1;

    private String commentN2;

    private String commentN3;

    private String commentN4;

    private BigDecimal nbAbs;

    private BigDecimal nbCg;

    public static DemandeAbsenceCongeDto fromEntity(DemandeAbsenceConge absenceConge) {
        if (absenceConge == null) {
            return null;
        }

        DemandeAbsenceCongeDto dto = new DemandeAbsenceCongeDto();
        BeanUtils.copyProperties(absenceConge, dto);

        return dto;
    }

    public static DemandeAbsenceConge toEntity(DemandeAbsenceCongeDto dto) {
        if (dto == null) {
            return null;
        }

        DemandeAbsenceConge entity = new DemandeAbsenceConge();
        BeanUtils.copyProperties(dto, entity);

        return entity;
    }

}
