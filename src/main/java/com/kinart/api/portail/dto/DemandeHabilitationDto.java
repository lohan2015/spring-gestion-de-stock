package com.kinart.api.portail.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBase;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBuilder;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.BeanPropertyMap;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.kinart.portail.business.model.DemandeHabilitation;
import com.kinart.portail.business.utils.EnumStatusType;
import com.kinart.stock.business.model.Utilisateur;
import io.swagger.annotations.ApiModel;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.InputStreamSource;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.time.Instant;
import java.util.Map;
import java.util.Set;

/** @author c.mbassi */
@Data
@Builder
//@NoArgsConstructor
@AllArgsConstructor
//@JsonDeserialize(builder = DemandeHabilitationDto.class)
//@JsonPOJOBuilder(buildMethodName = "buildFile", withPrefix="")
@ApiModel(description = "Model des habilitations")
public class DemandeHabilitationDto implements Serializable {

    private Integer id;

    private Instant creationDate;

    private Instant lastModifiedDate;

    @NotNull(message = "L'id entreprise ne doit pas etre null")
    private Integer idEntreprise = 1;

    private Utilisateur userDemHabil = new Utilisateur();

    @NotNull(message = "Le nom du fichier ne doit pas etre vide")
    @NotEmpty(message = "Le nom du fichier ne doit pas etre vide")
    @NotBlank(message = "Le tnom du fichier ne doit pas etre vide")
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

    private String demandType;

    private String demandCommande;

    private String libDemandType;

    MultipartFile file;

    public DemandeHabilitationDto() {

    }

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
