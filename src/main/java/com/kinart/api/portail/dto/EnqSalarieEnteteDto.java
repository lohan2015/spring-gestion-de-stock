package com.kinart.api.portail.dto;

import com.kinart.portail.business.model.*;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/** @author c.mbassi */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Model des enquêtes")
public class EnqSalarieEnteteDto  implements Serializable {

    private Integer id;
    private Instant creationDate;
    private Instant lastModifiedDate;

    private String nmat;
    private int annee;
    private String apprDrhl;

    List<EnqBienEtre> listeBienEtre = new ArrayList<>();
    List<EnqBilanObj> listeBilanObjectif = new ArrayList<>();
    List<EnqBilanAct> listeBilanActivites = new ArrayList<>();
    List<EnqBilanCompPoste> listeBilanCompPoste = new ArrayList<>();
    List<EnqBilanCompAtt> listeBilanCompAttendu = new ArrayList<>();
    List<EnqFormSuivie> listeFormationSuivie = new ArrayList<>();
    List<EnqFormSouhait> listeFormationSouhaitee = new ArrayList<>();
    List<EnqCondition> listeCondition = new ArrayList<>();

    public static EnqSalarieEnteteDto fromEntity(EnqSalarieEntete enqSalarieEntete) {
        if (enqSalarieEntete == null) {
            return null;
        }

        EnqSalarieEnteteDto dto = new EnqSalarieEnteteDto();
        BeanUtils.copyProperties(enqSalarieEntete, dto);

        return dto;
    }

    public static EnqSalarieEntete toEntity(EnqSalarieEnteteDto dto) {
        if (dto == null) {
            return null;
        }

        EnqSalarieEntete entity = new EnqSalarieEntete();
        BeanUtils.copyProperties(dto, entity);

        return entity;
    }

}
