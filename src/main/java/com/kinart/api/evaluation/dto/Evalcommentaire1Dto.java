package com.kinart.api.evaluation.dto;

import com.kinart.evaluation.business.model.Evalcodificationnote;
import com.kinart.evaluation.business.model.Evalcommentaire1;
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
@ApiModel(description = "Model de commentaire")
public class Evalcommentaire1Dto {
    private Integer id;
    private Integer idEntreprise;
    private String commentid;
    private String coeetape;
    private String codephase;
    private String codeeval;
    private String nmat;
    private String codemodele;
    private String commentaire;
    private String visaacceptation;
    private String titre;
    private String delais;
    private String critreussite;

    public static Evalcommentaire1Dto fromEntity(Evalcommentaire1 value) {
        if (value == null) {
            return null;
        }

        Evalcommentaire1Dto dto = new  Evalcommentaire1Dto();
        BeanUtils.copyProperties(value, dto);
        return dto;
    }

    public static Evalcommentaire1 toEntity(Evalcommentaire1Dto dto) {
        if (dto == null) {
            return null;
        }

        Evalcommentaire1 value = new Evalcommentaire1();
        BeanUtils.copyProperties(dto, value);
        return value;
    }
}
