package com.kinart.api.portail.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kinart.portail.business.model.DemandeHabilitation;
import com.kinart.portail.business.utils.EnumStatusType;
import com.kinart.stock.business.model.Utilisateur;
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
@AllArgsConstructor
@NoArgsConstructor
public class DemandeHabilitationDto implements Serializable {

    private Integer id;
    private Instant creationDate;
    private Instant lastModifiedDate;

    @NotNull(message = "L'id entreprise ne doit pas etre null")
    private Integer idEntreprise;

    private Utilisateur userDemHabil;

    @NotNull(message = "Le nom du fichier ne doit pas etre vide")
    @NotEmpty(message = "Le nom du fichier ne doit pas etre vide")
    @NotBlank(message = "Le tnom du fichier ne doit pas etre vide")
    private String fileName;

    @NotNull(message = "Le type du fichier ne doit pas etre vide")
    @NotEmpty(message = "Le type du fichier ne doit pas etre vide")
    @NotBlank(message = "Le type du fichier ne doit pas etre vide")
    private String fileType;

    private long fileSize;

    @JsonIgnore
    private byte[] data;

    @NotNull(message = "Le validateur ne doit pas etre vide")
    @NotEmpty(message = "Le validateur ne doit pas etre vide")
    @NotBlank(message = "Le validateur ne doit pas etre vide")
    private String valid;

    private EnumStatusType status;

    MultipartFile file;

    public static DemandeHabilitationDto fromEntity(DemandeHabilitation habilitation) {
        if (habilitation == null) {
            return null;
        }

        DemandeHabilitationDto dto = new DemandeHabilitationDto();
        BeanUtils.copyProperties(habilitation, dto);

        return dto;
    }

    public static DemandeHabilitation toEntity(DemandeHabilitationDto dto) {
        if (dto == null) {
            return null;
        }

        DemandeHabilitation entity = new DemandeHabilitation();
        BeanUtils.copyProperties(dto, entity);

        return entity;
    }

}
