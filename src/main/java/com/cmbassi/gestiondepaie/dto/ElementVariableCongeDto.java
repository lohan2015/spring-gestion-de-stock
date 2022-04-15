package com.cmbassi.gestiondepaie.dto;

import com.cmbassi.gestiondepaie.model.ElementVariableConge;
import com.cmbassi.gestiondepaie.model.ParamData;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class ElementVariableCongeDto implements Serializable {
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

    public ElementVariableCongeDto() {
    }

    public ElementVariableCongeDto(Integer idEntreprise, String aamm, String nmat, Integer nbul, Date ddeb, Date dfin, BigDecimal nbjc, BigDecimal nbja, String motf, BigDecimal mont, String cuti) {
        this.idEntreprise = idEntreprise;
        this.aamm = aamm;
        this.nmat = nmat;
        this.nbul = nbul;
        this.ddeb = ddeb;
        this.dfin = dfin;
        this.nbjc = nbjc;
        this.nbja = nbja;
        this.motf = motf;
        this.mont = mont;
        this.cuti = cuti;
    }

    public ElementVariableCongeDto(Integer id, Integer idEntreprise, String aamm, String nmat, Integer nbul, Date ddeb, Date dfin, BigDecimal nbjc, BigDecimal nbja, String motf, BigDecimal mont, String cuti, String startdate, String enddate, String nomsalarie, String libmotif) {
        this.id = id;
        this.idEntreprise = idEntreprise;
        this.aamm = aamm;
        this.nmat = nmat;
        this.nbul = nbul;
        this.ddeb = ddeb;
        this.dfin = dfin;
        this.nbjc = nbjc;
        this.nbja = nbja;
        this.motf = motf;
        this.mont = mont;
        this.cuti = cuti;
        this.startdate = startdate;
        this.enddate = enddate;
        this.nomsalarie = nomsalarie;
        this.libmotif = libmotif;
    }

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
