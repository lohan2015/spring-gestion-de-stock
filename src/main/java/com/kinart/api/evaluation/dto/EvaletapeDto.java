package com.kinart.api.evaluation.dto;

import com.kinart.evaluation.business.model.Evalcritnote;
import com.kinart.evaluation.business.model.Evaletape;
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
@ApiModel(description = "Model d'étape")
public class EvaletapeDto {
    private Integer id;
    private Integer idEntreprise;
    private String code;
    private String codephase;
    private String libelle;
    private String bcommentaire;
    private String bobjstatique;
    private String bnote;
    private String bintercodification;
    private String bcoefficient;
    private String bsanction;
    private String bdelais;
    private String bnotechiffre;
    private String bnotelettre;
    private String bdesription;
    private String etapesuiv;
    private String bderniereetape;
    private String butilise;
    private String bnotece;
    private String bvisa;
    private String btotala1;
    private String btotal;
    private String btotalce;
    private String bnotea1;
    private String coef;
    private String coefce;
    private String coefev;
    private String bobjdynamique;
    private String bresume;
    private String brealisation;
    private String bobjcommenta1;

    public static EvaletapeDto fromEntity(Evaletape value) {
        if (value == null) {
            return null;
        }

        EvaletapeDto dto = new  EvaletapeDto();
        BeanUtils.copyProperties(value, dto);
        return dto;
    }

    public static Evaletape toEntity(EvaletapeDto dto) {
        if (dto == null) {
            return null;
        }

        Evaletape value = new Evaletape();
        BeanUtils.copyProperties(dto, value);
        return value;
    }
}
