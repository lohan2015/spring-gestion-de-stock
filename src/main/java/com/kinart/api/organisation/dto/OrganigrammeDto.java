package com.kinart.api.organisation.dto;

import com.kinart.organisation.business.model.Organigramme;
import com.kinart.organisation.business.model.Orgposte;
import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;


/** @author c.mbassi */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrganigrammeDto {
    private Integer id;
    private Integer idEntreprise;
    private String codeorganigramme;
    private String libelle;
    private String codeniveau;
    private Date datecree;
    private String estvalide;
    private String accepteexterne;
    private String codeposte;
    private String codepere;
    private String codematricule;
    private String bprestataire;
    private String bcasefictive;
    private String codesite;
    private String nivfonction;
    private String libellecourt;

    private String libelleorganigramme;
    private String libelleniveau;
    private String libellesite;
    private String nmat;
    private String nomagent;
    private int nombrefils = 0;
    private int nombrefilstotal = 0;

    public static OrganigrammeDto fromEntity(Organigramme organigramme) {
        if (organigramme == null) {
            return null;
        }

        OrganigrammeDto dto = new  OrganigrammeDto();
        BeanUtils.copyProperties(organigramme, dto);
        return dto;
    }

    public static Organigramme toEntity(OrganigrammeDto dto) {
        if (dto == null) {
            return null;
        }

        Organigramme organigramme = new Organigramme();
        BeanUtils.copyProperties(dto, organigramme);
        return organigramme;
    }
}
