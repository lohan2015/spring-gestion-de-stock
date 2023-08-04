package com.kinart.api.portail.dto;

import com.kinart.portail.business.model.DemandeAbsenceConge;
import com.kinart.portail.business.utils.EnumStatusType;
import com.kinart.stock.business.model.Utilisateur;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

/** @author c.mbassi */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Model des demande absences / congé response")
public class DemandeAbsenceCongeResponse implements Serializable {

    private Integer id;
    private Instant creationDate;
    private Instant lastModifiedDate;

    @NotNull(message = "L'id entreprise ne doit pas etre null")
    private Integer idEntreprise = 1;

    private Integer userId;

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

    private String status1;

    private String status2;

    private String status3;

    private String status4;

    private String author;
    private String demandid;
    private String valueDate;
    private String email;
    private String libmotif;
    private String strDateDebut;
    private String strDateFin;
}
