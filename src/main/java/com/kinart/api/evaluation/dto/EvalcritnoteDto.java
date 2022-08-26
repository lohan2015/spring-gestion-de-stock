package com.kinart.api.evaluation.dto;

import com.kinart.evaluation.business.model.Evalcommentaire1;
import com.kinart.evaluation.business.model.Evalcritnote;
import com.kinart.stock.business.model.AbstractEntity;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;


/** @author cmbassi */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Model de note")
public class EvalcritnoteDto {
    private Integer id;
    private Integer idEntreprise;
    private String nmat;
    private String codeeval;
    private String codecrit;
    private BigDecimal notechf;
    private String notelet;
    private String comm;
    private BigDecimal notegl;
    private BigDecimal notece;
    private BigDecimal poids;
    private BigDecimal notechfcol;
    private String noteletcol;
    private BigDecimal notechfevl1;
    private String noteletevl1;
    private BigDecimal notechfevl2;
    private String noteletevl2;
    private String descr;

    public static EvalcritnoteDto fromEntity(Evalcritnote value) {
        if (value == null) {
            return null;
        }

        EvalcritnoteDto dto = new  EvalcritnoteDto();
        BeanUtils.copyProperties(value, dto);
        return dto;
    }

    public static Evalcritnote toEntity(EvalcritnoteDto dto) {
        if (dto == null) {
            return null;
        }

        Evalcritnote value = new Evalcritnote();
        BeanUtils.copyProperties(dto, value);
        return value;
    }

}
