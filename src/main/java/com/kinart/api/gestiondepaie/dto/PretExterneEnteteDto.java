package com.kinart.api.gestiondepaie.dto;

import com.kinart.paie.business.model.PretExterneEntete;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kinart.paie.business.services.utils.HibernateConnexionService;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Data
@Builder
@ApiModel(description = "Model de gestion des entêtes des prêts externes")
public class PretExterneEnteteDto implements Serializable {
    private Integer id;
    private Integer idEntreprise;
    private String nmat;
    private String nprt;
    private String crub;
    private String com1;
    private String com2;
    private Date dmep;
    private Date dpec;
    private Date per1;
    private BigDecimal mntp;
    private BigDecimal resr;
    private Integer nbec;
    private Integer nber;
    private BigDecimal mtec;
    private BigDecimal tint;
    private BigDecimal ttax;
    private String pact;
    private String etpr;
    private Date dcrp;
    private String ncpt;
    private String codenatpret;
    private String etatsolde;
    private String codedevise;
    @JsonIgnore
    private SalarieDto salarie;
    @JsonIgnore
    private List<PretExterneDetailDto> pretExterneDetail;
    private String nomsalarie;
    private String librubrique;

    private String libnature;

    private String dtMiseEnPlace;

    private String dtPremEch;

    private String mntMontantPret;

    private String mntResteARemb;

    private String mntEch;

    private String intEchReste;

    private String intNbreEch;

    private String bgTauxInt;

    private String bgTauxTaxe;

    private String strnom;

    private String modesaisie;

    @JsonIgnore
    @Autowired
    HibernateConnexionService service;

    public String strFormatDate;

    @JsonIgnore
    public SimpleDateFormat dateFormat;

    public String strSeparateurDeMillier;

    public String strSeparateurDeDecimal;

    public String strDevise;

    public int intNombreDeDecimales;

    public boolean boolPartieDecimalAdmise;

    public boolean boolDeviseAGaucheOuADroite;

    public boolean regenerer = false;

    public String aamm;

    public PretExterneEnteteDto() {
    }

    public PretExterneEnteteDto(Integer id, Integer idEntreprise, String nmat, String nprt, String crub, String com1, String com2, Date dmep, Date dpec, Date per1, BigDecimal mntp, BigDecimal resr, Integer nbec, Integer nber, BigDecimal mtec, BigDecimal tint, BigDecimal ttax, String pact, String etpr, Date dcrp, String ncpt, String codenatpret, String etatsolde, String codedevise, SalarieDto salarie, List<PretExterneDetailDto> pretExterneDetail, String nomsalarie, String librubrique, String libnature, String dtMiseEnPlace, String dtPremEch, String mntMontantPret, String mntResteARemb, String mntEch, String intEchReste, String intNbreEch, String bgTauxInt, String bgTauxTaxe, String strnom, String modesaisie, HibernateConnexionService service, String strFormatDate, SimpleDateFormat dateFormat, String strSeparateurDeMillier, String strSeparateurDeDecimal, String strDevise, int intNombreDeDecimales, boolean boolPartieDecimalAdmise, boolean boolDeviseAGaucheOuADroite, boolean regenerer, String aamm) {
        this.id = id;
        this.idEntreprise = idEntreprise;
        this.nmat = nmat;
        this.nprt = nprt;
        this.crub = crub;
        this.com1 = com1;
        this.com2 = com2;
        this.dmep = dmep;
        this.dpec = dpec;
        this.per1 = per1;
        this.mntp = mntp;
        this.resr = resr;
        this.nbec = nbec;
        this.nber = nber;
        this.mtec = mtec;
        this.tint = tint;
        this.ttax = ttax;
        this.pact = pact;
        this.etpr = etpr;
        this.dcrp = dcrp;
        this.ncpt = ncpt;
        this.codenatpret = codenatpret;
        this.etatsolde = etatsolde;
        this.codedevise = codedevise;
        this.salarie = salarie;
        this.pretExterneDetail = pretExterneDetail;
        this.nomsalarie = nomsalarie;
        this.librubrique = librubrique;
        this.libnature = libnature;
        this.dtMiseEnPlace = dtMiseEnPlace;
        this.dtPremEch = dtPremEch;
        this.mntMontantPret = mntMontantPret;
        this.mntResteARemb = mntResteARemb;
        this.mntEch = mntEch;
        this.intEchReste = intEchReste;
        this.intNbreEch = intNbreEch;
        this.bgTauxInt = bgTauxInt;
        this.bgTauxTaxe = bgTauxTaxe;
        this.strnom = strnom;
        this.modesaisie = modesaisie;
        this.service = service;
        this.strFormatDate = strFormatDate;
        this.dateFormat = dateFormat;
        this.strSeparateurDeMillier = strSeparateurDeMillier;
        this.strSeparateurDeDecimal = strSeparateurDeDecimal;
        this.strDevise = strDevise;
        this.intNombreDeDecimales = intNombreDeDecimales;
        this.boolPartieDecimalAdmise = boolPartieDecimalAdmise;
        this.boolDeviseAGaucheOuADroite = boolDeviseAGaucheOuADroite;
        this.regenerer = regenerer;
        this.aamm = aamm;
    }

    public static PretExterneEnteteDto fromEntity(PretExterneEntete pretExterneEntete) {
        if (pretExterneEntete == null) {
            return null;
        }
        PretExterneEnteteDto dto = new  PretExterneEnteteDto();
        BeanUtils.copyProperties(pretExterneEntete, dto);
        return dto;
    }

    public static PretExterneEntete toEntity(PretExterneEnteteDto dto) {
        if (dto == null) {
            return null;
        }
        PretExterneEntete pretInterne = new PretExterneEntete();
        BeanUtils.copyProperties(dto, pretInterne);
        return pretInterne;
    }
}
