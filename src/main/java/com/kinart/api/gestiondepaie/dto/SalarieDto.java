package com.kinart.api.gestiondepaie.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kinart.paie.business.model.Salarie;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Model de gestion des salariés")
public class SalarieDto implements Serializable {
    private Integer id;
    private Integer identreprise;

    private String nmat;

    private String niv1;

    private String niv2;

    private String niv3;

    private String cals;

    private String clas;

    private String nom;

    private String pren;

    private String sexe;

    private Date dtna;

    private String nato;

    private String sitf;

    private Integer nbcj;

    private Integer nbef;

    private Integer nbec;

    private Integer nbfe;

    private BigDecimal nbpt;

    private String modp;

    private String banq;

    private String guic;

    private String comp;

    private String cle;

    private String ccpt;

    private String bqso;

    private String vild;

    private String cat;

    private String ech;

    private String grad;

    private String fonc;

    private String afec;

    private String codf;

    private Integer indi;

    private String ctat;

    private BigDecimal tinp;

    private String synd;

    private String hifo;

    private String zli1;

    private String zli2;

    private Date dtes;

    private Date ddca;

    private String typc;

    private String avn1;

    private String avn2;

    private String avn3;

    private String avn4;

    private String avn5;

    private String avn6;

    private String avn7;

    private String regi;

    private String zres;

    private String dmst;

    private Integer npie;

    private String mrrx;

    private Date dmrr;

    private String mtfr;

    private String lieu;

    private String cods;

    private String pnet;

    private BigDecimal snet;

    private String devp;

    private String equi;

    private String dels;

    private String tits;

    private Date dtit;

    private Date depr;

    private Date decc;

    private BigDecimal japa;

    private BigDecimal dapa;

    private BigDecimal japec;

    private BigDecimal dapec;

    private BigDecimal jded;

    private BigDecimal dded;

    private BigDecimal jrla;

    private BigDecimal jrlec;

    private BigDecimal nbjcf;

    private BigDecimal nbjaf;

    private Date ddcf;

    private Date dfcf;

    private BigDecimal mtcf;

    private String pmcf;

    private BigDecimal nbjse;

    private BigDecimal nbjsa;

    private String nmjf;

    private String adr1;

    private String adr2;

    private String adr3;

    private String adr4;

    private String bpos;

    private String ntel;

    private String pnai;

    private String comm;

    private String pbpe;

    private Date dchg;

    private String mchg;

    private Date dfes;

    private String stor;

    private BigDecimal nbjtr;

    private Date drtcg;

    private Date ddenv;

    private Date drenv;

    private String noss;

    private String cont;

    private BigDecimal nbjsm;

    private String sana;

    private String tyfo1;

    private String tyfo2;

    private String nifo;

    private Date dchf;

    private String note;

    private String codeposte;

    private String codesite;

    private String zli3;

    private String zli4;

    private String zli5;

    private String zli6;

    private String zli7;

    private String zli8;

    private String zli9;

    private String zli10;

    private String lnai;

    private String lemb;

    private String photo;

    @JsonIgnore
    private String libniv1;
    @JsonIgnore
    private String libniv2;
    @JsonIgnore
    private String libniv3;
    @JsonIgnore
    private String libfonction;
    @JsonIgnore
    private String libcategorie;
    @JsonIgnore
    private String libechelon;
    @JsonIgnore
    private String libagence;
    @JsonIgnore
    private String libgrade;
    @JsonIgnore
    private String libsexe;
    @JsonIgnore
    private String libsitfam;

    @JsonIgnore
    private List<ElementFixeSalaireDto> elementFixeSalaire;

    @JsonIgnore
    private List<CaisseMutuelleSalarieDto> caisseMutuelleSalarie;

    @JsonIgnore
    private List<VirementSalarieDto> virementSalarie;

    @JsonIgnore
    private List<PretInterneDto> pretInterne;

    public static SalarieDto fromEntity(Salarie salarie) {
        if (salarie == null) {
            return null;
        }
        SalarieDto dto = new  SalarieDto();
        BeanUtils.copyProperties(salarie, dto);
        return dto;
    }

    public static Salarie toEntity(SalarieDto dto) {
        if (dto == null) {
            return null;
        }
        Salarie sal = new Salarie();
        BeanUtils.copyProperties(dto, sal, "elementFixeSalaire", "caisseMutuelleSalarie", "virementSalarie", "pretInterne");
        return sal;
    }

}
