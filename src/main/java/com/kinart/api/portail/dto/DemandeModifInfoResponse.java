package com.kinart.api.portail.dto;

import com.kinart.stock.business.model.Utilisateur;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

/** @author c.mbassi */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@JsonDeserialize(builder = DemandeHabilitationDto.class)
//@JsonPOJOBuilder(buildMethodName = "buildFile", withPrefix="")
@ApiModel(description = "Model réponse des modifs fiche salarié")
public class DemandeModifInfoResponse implements Serializable {

    private Integer id;

    private Instant creationDate;

    private Instant lastModifiedDate;

    @NotNull(message = "L'id entreprise ne doit pas etre null")
    private Integer idEntreprise = 1;

    private Integer userId;

    private String email;

    @NotNull(message = "Le nom du fichier ne doit pas etre vide")
    @NotEmpty(message = "Le nom du fichier ne doit pas etre vide")
    @NotBlank(message = "Le tnom du fichier ne doit pas etre vide")
    private String fileName;

    @NotNull(message = "Le type du fichier ne doit pas etre vide")
    @NotEmpty(message = "Le type du fichier ne doit pas etre vide")
    @NotBlank(message = "Le type du fichier ne doit pas etre vide")
    private String fileType;

    private Long fileSize;

    private String champConcerne;

    private String valeurSouhaitee;

    @NotNull(message = "Le validateur ne doit pas etre vide")
    @NotEmpty(message = "Le validateur ne doit pas etre vide")
    @NotBlank(message = "Le validateur ne doit pas etre vide")
    private String valid;

    private String status;

    private String author;

    private String demandid;
    private String valueDate;
    private String valueChamp;
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

    public DemandeModifInfoResponse(Integer id, Instant creationDate, Integer idEntreprise, String valid, Utilisateur user){
            setId(id);
            setValid(valid);
            setIdEntreprise(idEntreprise);
            setCreationDate(creationDate);
            setUserId(user.getId());
    }

    /**
     *
     * @param dto
     * @return
     */
    public static DemandeModifInfoResponse fromDto(DemandeModifInfoDto dto){
        DemandeModifInfoResponse reponse = new DemandeModifInfoResponse();
        BeanUtils.copyProperties(dto, reponse);
        reponse.setStatus(dto.getStatus().getCode());

        return reponse;
    }

    public static DemandeModifInfoDto toDto(DemandeModifInfoResponse dto){
        DemandeModifInfoDto reponse = new DemandeModifInfoDto();
        BeanUtils.copyProperties(dto, reponse);

        return reponse;
    }

}
