package com.kinart.api.gestiondepaie.dto;

import com.kinart.paie.business.model.ElementVariableConge;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ElementVariableCongeDto implements Serializable {
    private String dateFormat;
    private String typeBD;

    private Integer id;
    private Integer idEntreprise;
    private String aamm;
    private String nmat;
    private Integer nbul;
    private Date ddeb;
    private Date dfin;
    private BigDecimal nbjc;
    private BigDecimal nbja;
    private String motf;
    private BigDecimal mont;
    private String cuti;

    private String startdate;
    private String enddate;
    private String nomsalarie;
    private String libmotif;

    public static ElementVariableCongeDto fromEntity(ElementVariableConge elementVariableConge) {
        if (elementVariableConge == null) {
            return null;
        }
        ElementVariableCongeDto dto = new  ElementVariableCongeDto();
        BeanUtils.copyProperties(elementVariableConge, dto);
        return dto;
    }

    public static ElementVariableConge toEntity(ElementVariableCongeDto dto) {
        if (dto == null) {
            return null;
        }

        ElementVariableConge sal = new ElementVariableConge();
        BeanUtils.copyProperties(dto, sal, "startdate", "enddate");
        return sal;
    }
}
