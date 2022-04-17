package com.kinart.api.gestiondepaie.dto;

import com.kinart.paie.business.model.CalendrierPaie;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Data
@Builder
@ApiModel(description = "Model de gestion des calendriers")
public class CalendrierPaieDto {
    private Integer id;
    private Integer idEntreprise;

    private Date jour;

    private String jsem;

    private String ouvr;

    private String fer;

    private Long nsem;

    private Long anne;

    private String trav;

    public CalendrierPaieDto() {
    }

    public CalendrierPaieDto(Integer id, Integer idEntreprise, Date jour, String jsem, String ouvr, String fer, Long nsem, Long anne, String trav) {
        this.id = id;
        this.idEntreprise = idEntreprise;
        this.jour = jour;
        this.jsem = jsem;
        this.ouvr = ouvr;
        this.fer = fer;
        this.nsem = nsem;
        this.anne = anne;
        this.trav = trav;
    }

    public static CalendrierPaieDto fromEntity(CalendrierPaie calendrierPaie) {
        if (calendrierPaie == null) {
            return null;
        }

        CalendrierPaieDto dto = new  CalendrierPaieDto();
        BeanUtils.copyProperties(calendrierPaie, dto);
        return dto;
    }

    public static CalendrierPaie toEntity(CalendrierPaieDto calendrierPaieDto) {
        if (calendrierPaieDto == null) {
            return null;
        }

        CalendrierPaie calendrierPaie = new CalendrierPaie();
        BeanUtils.copyProperties(calendrierPaieDto, calendrierPaie);
        return calendrierPaie;
    }
}
