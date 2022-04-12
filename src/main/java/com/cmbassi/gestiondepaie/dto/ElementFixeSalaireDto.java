package com.cmbassi.gestiondepaie.dto;

import com.cmbassi.gestiondepaie.model.ElementFixeSalaire;
import com.cmbassi.gestiondepaie.model.ElementSalaire;
import com.cmbassi.gestiondepaie.model.Salarie;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Date;


@Data
@Builder
public class ElementFixeSalaireDto {

    private Integer idEntreprise;

    private String nmat;

    private String codp;

    private BigDecimal monp;

    private Date ddeb;

    private Date dfin;

    @JsonIgnore
    private Salarie salarie;

    public ElementFixeSalaireDto() {
    }

    public ElementFixeSalaireDto(Integer idEntreprise, String nmat, String codp, BigDecimal monp, Date ddeb, Date dfin, Salarie salarie) {
        this.idEntreprise = idEntreprise;
        this.nmat = nmat;
        this.codp = codp;
        this.monp = monp;
        this.ddeb = ddeb;
        this.dfin = dfin;
        this.salarie = salarie;
    }

    public static ElementSalaireDto fromEntity(ElementSalaire elementSalaire) {
        if (elementSalaire == null) {
            return null;
        }

        ElementSalaireDto dto = new  ElementSalaireDto();
        BeanUtils.copyProperties(elementSalaire, dto);
        return dto;
    }

    public static ElementFixeSalaire toEntity(ElementFixeSalaireDto elementFixeSalaireDto) {
        if (elementFixeSalaireDto == null) {
            return null;
        }

        ElementFixeSalaire elementFixeSalaire = new ElementFixeSalaire();
        BeanUtils.copyProperties(elementFixeSalaireDto, elementFixeSalaire);
        return elementFixeSalaire;
    }
}
