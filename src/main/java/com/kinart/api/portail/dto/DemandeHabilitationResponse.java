package com.kinart.api.portail.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kinart.portail.business.model.DemandeHabilitation;
import com.kinart.portail.business.utils.EnumStatusType;
import com.kinart.stock.business.model.Utilisateur;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;

/** @author c.mbassi */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@JsonDeserialize(builder = DemandeHabilitationDto.class)
//@JsonPOJOBuilder(buildMethodName = "buildFile", withPrefix="")
@ApiModel(description = "Model réponse des habilitations")
public class DemandeHabilitationResponse implements Serializable {

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

    @NotNull(message = "Le validateur ne doit pas etre vide")
    @NotEmpty(message = "Le validateur ne doit pas etre vide")
    @NotBlank(message = "Le validateur ne doit pas etre vide")
    private String valid;

    private String status;

    private String author;

    private String demandid;
    private String valueDate;

    public DemandeHabilitationResponse(Integer id, Instant creationDate, Integer idEntreprise, String valid, Utilisateur user){
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
    public static DemandeHabilitationResponse fromDto(DemandeHabilitationDto dto){
        DemandeHabilitationResponse reponse = new DemandeHabilitationResponse();
        BeanUtils.copyProperties(dto, reponse);
        reponse.setStatus(dto.getStatus().getCode());

        return reponse;
    }

    public static DemandeHabilitationDto toDto(DemandeHabilitationResponse dto){
        DemandeHabilitationDto reponse = new DemandeHabilitationDto();
        BeanUtils.copyProperties(dto, reponse);

        return reponse;
    }

}
