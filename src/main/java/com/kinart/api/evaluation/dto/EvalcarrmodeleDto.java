package com.kinart.api.evaluation.dto;

import com.kinart.api.organisation.dto.NiveauDto;
import com.kinart.evaluation.business.model.Evalcarrmodele;
import com.kinart.organisation.business.model.Orgniveau;
import com.kinart.stock.business.model.AbstractEntity;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/** @author cmbassi */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Model des modèles d'évaluation")
public class EvalcarrmodeleDto {
    private Integer id;
    private Integer idEntreprise;
    private String codemodele;
    private String libelle;
    private String classe;

    public static EvalcarrmodeleDto fromEntity(Evalcarrmodele value) {
        if (value == null) {
            return null;
        }

        EvalcarrmodeleDto dto = new  EvalcarrmodeleDto();
        BeanUtils.copyProperties(value, dto);
        return dto;
    }

    public static Evalcarrmodele toEntity(EvalcarrmodeleDto dto) {
        if (dto == null) {
            return null;
        }

        Evalcarrmodele value = new Evalcarrmodele();
        BeanUtils.copyProperties(dto, value);
        return value;
    }
}
