package com.kinart.api.evaluation.dto;

import com.kinart.evaluation.business.model.Evalcarrphasemodele;
import com.kinart.evaluation.business.model.Evalcodificationnote;
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
@ApiModel(description = "Model de codification des notes d'évaluation")
public class EvalcodificationnoteDto {
    private Integer id;
    private Integer idEntreprise;
    private String code;
    private String libelle;
    private String intervalle;
    private String abbreviation;
    private String information;
    private String bjustifier;
    private String raison;
    private String min;
    private String max;

    public static EvalcodificationnoteDto fromEntity(Evalcodificationnote value) {
        if (value == null) {
            return null;
        }

        EvalcodificationnoteDto dto = new  EvalcodificationnoteDto();
        BeanUtils.copyProperties(value, dto);
        return dto;
    }

    public static Evalcodificationnote toEntity(EvalcodificationnoteDto dto) {
        if (dto == null) {
            return null;
        }

        Evalcodificationnote value = new Evalcodificationnote();
        BeanUtils.copyProperties(dto, value);
        return value;
    }

}
