package com.kinart.api.evaluation.dto;

import com.kinart.evaluation.business.model.Evalcarrphasemodele;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.springframework.beans.BeanUtils;


/** @author cmbassi */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Model des phases du modèle d'évaluation")
public class EvalcarrphasemodeleDto {
    private Integer id;
    private Integer idEntreprise;
    private String numerophase;
    private String codemodele;
    private String libelle;

    public static EvalcarrphasemodeleDto fromEntity(Evalcarrphasemodele value) {
        if (value == null) {
            return null;
        }

        EvalcarrphasemodeleDto dto = new  EvalcarrphasemodeleDto();
        BeanUtils.copyProperties(value, dto);
        return dto;
    }

    public static Evalcarrphasemodele toEntity(EvalcarrphasemodeleDto dto) {
        if (dto == null) {
            return null;
        }

        Evalcarrphasemodele value = new Evalcarrphasemodele();
        BeanUtils.copyProperties(dto, value);
        return value;
    }

}
