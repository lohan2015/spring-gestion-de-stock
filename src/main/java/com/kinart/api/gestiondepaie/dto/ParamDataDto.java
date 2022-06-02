package com.kinart.api.gestiondepaie.dto;

import com.kinart.paie.business.model.ParamData;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParamDataDto {
    private Integer id;
    private Integer idEntreprise;

    private Integer ctab;

    private String cacc;

    private Integer nume;

    private String vall;

    private Long valm;

    private BigDecimal valt;

    private String duti;

    private Date vald;

    private String libcolonne;

    public static ParamDataDto fromEntity(ParamData paramData) {
        if (paramData == null) {
            return null;
        }
        ParamDataDto dto = new  ParamDataDto();
        BeanUtils.copyProperties(paramData, dto);
        return dto;
    }

    public static ParamData toEntity(ParamDataDto dto) {
        if (dto == null) {
            return null;
        }

        ParamData sal = new ParamData();
        BeanUtils.copyProperties(dto, sal);
        return sal;
    }
}
