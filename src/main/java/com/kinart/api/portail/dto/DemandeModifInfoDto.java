package com.kinart.api.portail.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kinart.portail.business.model.DemandeModifInfo;
import com.kinart.portail.business.utils.EnumStatusType;
import com.kinart.stock.business.model.Utilisateur;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
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
@ApiModel(description = "Model des demandes de modif info")
public class DemandeModifInfoDto implements Serializable {

    private Integer id;
    private Instant creationDate;
    private Instant lastModifiedDate;

    @NotNull(message = "L'id entreprise ne doit pas etre null")
    private Integer idEntreprise = 1;

    private Utilisateur userDemModInfo = new Utilisateur();

    @NotNull(message = "Le champ concerné ne doit pas etre vide")
    @NotEmpty(message = "Le champ concerné ne doit pas etre vide")
    @NotBlank(message = "Le champ concerné ne doit pas etre vide")
    private String champConcerne;

    @NotNull(message = "La valeur concernée ne doit pas etre vide")
    @NotEmpty(message = "La valeur concernée ne doit pas etre vide")
    @NotBlank(message = "La valeur concernée ne doit pas etre vide")
    private String valeurSouhaitee;

    @NotNull(message = "Le nom du fichier ne doit pas etre vide")
    @NotEmpty(message = "Le nom du fichier ne doit pas etre vide")
    @NotBlank(message = "Le nom du fichier ne doit pas etre vide")
    private String fileName;

    @NotNull(message = "Le type du fichier ne doit pas etre vide")
    @NotEmpty(message = "Le type du fichier ne doit pas etre vide")
    @NotBlank(message = "Le type du fichier ne doit pas etre vide")
    private String fileType;

    private Long fileSize;

    @JsonIgnore
    private byte[] fileData;

    @NotNull(message = "Le validateur ne doit pas etre vide")
    @NotEmpty(message = "Le validateur ne doit pas etre vide")
    @NotBlank(message = "Le validateur ne doit pas etre vide")
    private String valid;

    private EnumStatusType status;

    private String commentDrhl;

    private String commentUser;

    private String estEnfant;

    private String estConjoint;

    private String nom;

    private String njf;

    private String pren;

    private String prof;

    private String empl;

    private String sexe;

    private String libsexe;

    private String pays;

    private String libpays;

    private String achg;

    private String scol;

    private Date dteEvt;

    private String adrc;

    MultipartFile file;

    public static DemandeModifInfoDto fromEntity(DemandeModifInfo modifInfo) {
        if (modifInfo == null) {
            return null;
        }

        DemandeModifInfoDto dto = new DemandeModifInfoDto();
        BeanUtils.copyProperties(modifInfo, dto);

        return dto;
    }

    public static DemandeModifInfo toEntity(DemandeModifInfoDto dto) {
        if (dto == null) {
            return null;
        }

        DemandeModifInfo entity = new DemandeModifInfo();
        BeanUtils.copyProperties(dto, entity);

        return entity;
    }

}
