package com.kinart.api.portail.dto;

import com.kinart.portail.business.model.DemandePret;
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
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

/** @author c.mbassi */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Model des demandes de prêt")
public class DemandePretDto implements Serializable {

    private Integer id;
    private Instant creationDate;
    private Instant lastModifiedDate;

    @NotNull(message = "L'id entreprise ne doit pas etre null")
    private Integer idEntreprise = 1;

    private Utilisateur userDemPret = new Utilisateur();

    private BigDecimal montantPret;

    private Integer dureePret;

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
    private String scePersonnel;

    @NotNull(message = "Le validateur 2 ne doit pas etre vide")
    @NotEmpty(message = "Le validateur 2 ne doit pas etre vide")
    @NotBlank(message = "Le validateur 2 ne doit pas etre vide")
    private String drhl;

    private String dga;

    private String dg;

    private EnumStatusType status1;

    private EnumStatusType status2;

    private EnumStatusType status3;

    private EnumStatusType status4;

    public static DemandePretDto fromEntity(DemandePret demandePret) {
        if (demandePret == null) {
            return null;
        }

        DemandePretDto dto = new DemandePretDto();
        BeanUtils.copyProperties(demandePret, dto);

        return dto;
    }

    public static DemandePret toEntity(DemandePretDto dto) {
        if (dto == null) {
            return null;
        }

        DemandePret entity = new DemandePret();
        BeanUtils.copyProperties(dto, entity);

        return entity;
    }

}
