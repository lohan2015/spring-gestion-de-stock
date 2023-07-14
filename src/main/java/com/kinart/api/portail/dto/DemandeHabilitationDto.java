package com.kinart.api.portail.dto;

import com.kinart.portail.business.model.DemandeAbsenceConge;
import com.kinart.portail.business.model.DemandeAttestation;
import com.kinart.portail.business.utils.EnumStatusType;
import com.kinart.stock.business.model.Utilisateur;
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
public class DemandeAttestationDto implements Serializable {

    private Integer id;
    private Instant creationDate;
    private Instant lastModifiedDate;

    @NotNull(message = "L'id entreprise ne doit pas etre null")
    private Integer idEntreprise;

    private Utilisateur userDemAttest;

    @NotNull(message = "Le type de document ne doit pas etre vide")
    @NotEmpty(message = "Le type de document ne doit pas etre vide")
    @NotBlank(message = "Le type de document ne doit pas etre vide")
    private String typeDoc;

    @NotNull(message = "Le validateur 1 ne doit pas etre vide")
    @NotEmpty(message = "Le validateur 1 ne doit pas etre vide")
    @NotBlank(message = "Le validateur 1 ne doit pas etre vide")
    private String scePersonnel;

    private EnumStatusType status;

    public static DemandeAttestationDto fromEntity(DemandeAttestation attestation) {
        if (attestation == null) {
            return null;
        }

        DemandeAttestationDto dto = new DemandeAttestationDto();
        BeanUtils.copyProperties(attestation, dto);

        return dto;
    }

    public static DemandeAttestation toEntity(DemandeAttestationDto dto) {
        if (dto == null) {
            return null;
        }

        DemandeAttestation entity = new DemandeAttestation();
        BeanUtils.copyProperties(dto, entity);

        return entity;
    }

}