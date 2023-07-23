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

/** @author c.mbassi */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@JsonDeserialize(builder = DemandeHabilitationDto.class)
//@JsonPOJOBuilder(buildMethodName = "buildFile", withPrefix="")
@ApiModel(description = "Model réponse des attestations")
public class DemandeAttestationResponse implements Serializable {

    private Integer id;

    private Instant creationDate;

    private Instant lastModifiedDate;

    @NotNull(message = "L'id entreprise ne doit pas etre null")
    private Integer idEntreprise = 1;

    private Integer userId;

    private String email;

    private String typeDoc;

    @NotNull(message = "Le validateur ne doit pas etre vide")
    @NotEmpty(message = "Le validateur ne doit pas etre vide")
    @NotBlank(message = "Le validateur ne doit pas etre vide")
    private String scePersonnel;

    private String status;

    private String author;

    private String demandid;
    private String valueDate;

    public DemandeAttestationResponse(Integer id, Instant creationDate, Integer idEntreprise, String scePersonnel, Utilisateur user){
            setId(id);
            setScePersonnel(scePersonnel);
            setIdEntreprise(idEntreprise);
            setCreationDate(creationDate);
            setUserId(user.getId());
    }

    /**
     *
     * @param dto
     * @return
     */
    public static DemandeAttestationResponse fromDto(DemandeHabilitationDto dto){
        DemandeAttestationResponse reponse = new DemandeAttestationResponse();
        BeanUtils.copyProperties(dto, reponse);
        reponse.setStatus(dto.getStatus().getCode());

        return reponse;
    }

    public static DemandeHabilitationDto toDto(DemandeAttestationResponse dto){
        DemandeHabilitationDto reponse = new DemandeHabilitationDto();
        BeanUtils.copyProperties(dto, reponse);

        return reponse;
    }

}
