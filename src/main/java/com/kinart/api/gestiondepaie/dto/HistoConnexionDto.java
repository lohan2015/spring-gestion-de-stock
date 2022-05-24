package com.kinart.api.gestiondepaie.dto;

import com.kinart.paie.business.model.HistoConnexion;
import com.kinart.paie.business.model.LogMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoConnexionDto {

    private Integer idEntreprise;

    private String cuti;

    private Date datc;

    private String ligne;

    private String typeop;

    public static HistoConnexionDto fromEntity(HistoConnexion histoConnexion) {
        if (histoConnexion == null) {
            return null;
        }
        HistoConnexionDto dto = new HistoConnexionDto();
        BeanUtils.copyProperties(histoConnexion, dto);
        return dto;
    }

    public static HistoConnexion toEntity(HistoConnexionDto dto) {
        if (dto == null) {
            return null;
        }

        HistoConnexion sal = new HistoConnexion();
        BeanUtils.copyProperties(dto, sal);
        return sal;
    }
}
