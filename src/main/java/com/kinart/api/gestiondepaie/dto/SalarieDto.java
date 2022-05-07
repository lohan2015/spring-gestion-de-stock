package com.kinart.api.gestiondepaie.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.kinart.paie.business.model.Salarie;
import com.kinart.stock.business.utils.NoUTCInstant;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Data
@Builder
public class SalarieDto implements Serializable {
    private Integer id;
    private Integer idEntreprise;

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
    private List<ElementFixeSalaireDto> elementFixeSalaire;

    @JsonIgnore
    private List<CaisseMutuelleSalarieDto> caisseMutuelleSalarie;

    @JsonIgnore
    private List<VirementSalarieDto> virementSalarie;

    @JsonIgnore
    private List<PretInterneDto> pretInterne;

    public SalarieDto() {
    }

    public SalarieDto(Integer idEntreprise, String nmat, String niv1, String niv2, String niv3, String cals, String clas, String nom, String pren, String sexe, Date dtna, String nato, String sitf, Integer nbcj, Integer nbef, Integer nbec, Integer nbfe, BigDecimal nbpt, String modp, String banq, String guic, String comp, String cle, String ccpt, String bqso, String vild, String cat, String ech, String grad, String fonc, String afec, String codf, Integer indi, String ctat, BigDecimal tinp, String synd, String hifo, String zli1, String zli2, Date dtes, Date ddca, String typc, String avn1, String avn2, String avn3, String avn4, String avn5, String avn6, String avn7, String regi, String zres, String dmst, Integer npie, String mrrx, Date dmrr, String mtfr, String lieu, String cods, String pnet, BigDecimal snet, String devp, String equi, String dels, String tits, Date dtit, Date depr, Date decc, BigDecimal japa, BigDecimal dapa, BigDecimal japec, BigDecimal dapec, BigDecimal jded, BigDecimal dded, BigDecimal jrla, BigDecimal jrlec, BigDecimal nbjcf, BigDecimal nbjaf, Date ddcf, Date dfcf, BigDecimal mtcf, String pmcf, BigDecimal nbjse, BigDecimal nbjsa, String nmjf, String adr1, String adr2, String adr3, String adr4, String bpos, String ntel, String pnai, String comm, String pbpe, Date dchg, String mchg, Date dfes, String stor, BigDecimal nbjtr, Date drtcg, Date ddenv, Date drenv, String noss, String cont, BigDecimal nbjsm, String sana, String tyfo1, String tyfo2, String nifo, Date dchf, String note, String codeposte, String codesite, String zli3, String zli4, String zli5, String zli6, String zli7, String zli8, String zli9, String zli10, String lnai, String lemb) {
        this.idEntreprise = idEntreprise;
        this.nmat = nmat;
        this.niv1 = niv1;
        this.niv2 = niv2;
        this.niv3 = niv3;
        this.cals = cals;
        this.clas = clas;
        this.nom = nom;
        this.pren = pren;
        this.sexe = sexe;
        this.dtna = dtna;
        this.nato = nato;
        this.sitf = sitf;
        this.nbcj = nbcj;
        this.nbef = nbef;
        this.nbec = nbec;
        this.nbfe = nbfe;
        this.nbpt = nbpt;
        this.modp = modp;
        this.banq = banq;
        this.guic = guic;
        this.comp = comp;
        this.cle = cle;
        this.ccpt = ccpt;
        this.bqso = bqso;
        this.vild = vild;
        this.cat = cat;
        this.ech = ech;
        this.grad = grad;
        this.fonc = fonc;
        this.afec = afec;
        this.codf = codf;
        this.indi = indi;
        this.ctat = ctat;
        this.tinp = tinp;
        this.synd = synd;
        this.hifo = hifo;
        this.zli1 = zli1;
        this.zli2 = zli2;
        this.dtes = dtes;
        this.ddca = ddca;
        this.typc = typc;
        this.avn1 = avn1;
        this.avn2 = avn2;
        this.avn3 = avn3;
        this.avn4 = avn4;
        this.avn5 = avn5;
        this.avn6 = avn6;
        this.avn7 = avn7;
        this.regi = regi;
        this.zres = zres;
        this.dmst = dmst;
        this.npie = npie;
        this.mrrx = mrrx;
        this.dmrr = dmrr;
        this.mtfr = mtfr;
        this.lieu = lieu;
        this.cods = cods;
        this.pnet = pnet;
        this.snet = snet;
        this.devp = devp;
        this.equi = equi;
        this.dels = dels;
        this.tits = tits;
        this.dtit = dtit;
        this.depr = depr;
        this.decc = decc;
        this.japa = japa;
        this.dapa = dapa;
        this.japec = japec;
        this.dapec = dapec;
        this.jded = jded;
        this.dded = dded;
        this.jrla = jrla;
        this.jrlec = jrlec;
        this.nbjcf = nbjcf;
        this.nbjaf = nbjaf;
        this.ddcf = ddcf;
        this.dfcf = dfcf;
        this.mtcf = mtcf;
        this.pmcf = pmcf;
        this.nbjse = nbjse;
        this.nbjsa = nbjsa;
        this.nmjf = nmjf;
        this.adr1 = adr1;
        this.adr2 = adr2;
        this.adr3 = adr3;
        this.adr4 = adr4;
        this.bpos = bpos;
        this.ntel = ntel;
        this.pnai = pnai;
        this.comm = comm;
        this.pbpe = pbpe;
        this.dchg = dchg;
        this.mchg = mchg;
        this.dfes = dfes;
        this.stor = stor;
        this.nbjtr = nbjtr;
        this.drtcg = drtcg;
        this.ddenv = ddenv;
        this.drenv = drenv;
        this.noss = noss;
        this.cont = cont;
        this.nbjsm = nbjsm;
        this.sana = sana;
        this.tyfo1 = tyfo1;
        this.tyfo2 = tyfo2;
        this.nifo = nifo;
        this.dchf = dchf;
        this.note = note;
        this.codeposte = codeposte;
        this.codesite = codesite;
        this.zli3 = zli3;
        this.zli4 = zli4;
        this.zli5 = zli5;
        this.zli6 = zli6;
        this.zli7 = zli7;
        this.zli8 = zli8;
        this.zli9 = zli9;
        this.zli10 = zli10;
        this.lnai = lnai;
        this.lemb = lemb;
    }

    public SalarieDto(Integer id, Integer idEntreprise, String nmat, String niv1, String niv2, String niv3, String cals, String clas, String nom, String pren, String sexe, Date dtna, String nato, String sitf, Integer nbcj, Integer nbef, Integer nbec, Integer nbfe, BigDecimal nbpt, String modp, String banq, String guic, String comp, String cle, String ccpt, String bqso, String vild, String cat, String ech, String grad, String fonc, String afec, String codf, Integer indi, String ctat, BigDecimal tinp, String synd, String hifo, String zli1, String zli2, Date dtes, Date ddca, String typc, String avn1, String avn2, String avn3, String avn4, String avn5, String avn6, String avn7, String regi, String zres, String dmst, Integer npie, String mrrx, Date dmrr, String mtfr, String lieu, String cods, String pnet, BigDecimal snet, String devp, String equi, String dels, String tits, Date dtit, Date depr, Date decc, BigDecimal japa, BigDecimal dapa, BigDecimal japec, BigDecimal dapec, BigDecimal jded, BigDecimal dded, BigDecimal jrla, BigDecimal jrlec, BigDecimal nbjcf, BigDecimal nbjaf, Date ddcf, Date dfcf, BigDecimal mtcf, String pmcf, BigDecimal nbjse, BigDecimal nbjsa, String nmjf, String adr1, String adr2, String adr3, String adr4, String bpos, String ntel, String pnai, String comm, String pbpe, Date dchg, String mchg, Date dfes, String stor, BigDecimal nbjtr, Date drtcg, Date ddenv, Date drenv, String noss, String cont, BigDecimal nbjsm, String sana, String tyfo1, String tyfo2, String nifo, Date dchf, String note, String codeposte, String codesite, String zli3, String zli4, String zli5, String zli6, String zli7, String zli8, String zli9, String zli10, String lnai, String lemb, String photo, List<ElementFixeSalaireDto> elementFixeSalaire, List<CaisseMutuelleSalarieDto> caisseMutuelleSalarie, List<VirementSalarieDto> virementSalarie, List<PretInterneDto> pretInterne) {
        this.id = id;
        this.idEntreprise = idEntreprise;
        this.nmat = nmat;
        this.niv1 = niv1;
        this.niv2 = niv2;
        this.niv3 = niv3;
        this.cals = cals;
        this.clas = clas;
        this.nom = nom;
        this.pren = pren;
        this.sexe = sexe;
        this.dtna = dtna;
        this.nato = nato;
        this.sitf = sitf;
        this.nbcj = nbcj;
        this.nbef = nbef;
        this.nbec = nbec;
        this.nbfe = nbfe;
        this.nbpt = nbpt;
        this.modp = modp;
        this.banq = banq;
        this.guic = guic;
        this.comp = comp;
        this.cle = cle;
        this.ccpt = ccpt;
        this.bqso = bqso;
        this.vild = vild;
        this.cat = cat;
        this.ech = ech;
        this.grad = grad;
        this.fonc = fonc;
        this.afec = afec;
        this.codf = codf;
        this.indi = indi;
        this.ctat = ctat;
        this.tinp = tinp;
        this.synd = synd;
        this.hifo = hifo;
        this.zli1 = zli1;
        this.zli2 = zli2;
        this.dtes = dtes;
        this.ddca = ddca;
        this.typc = typc;
        this.avn1 = avn1;
        this.avn2 = avn2;
        this.avn3 = avn3;
        this.avn4 = avn4;
        this.avn5 = avn5;
        this.avn6 = avn6;
        this.avn7 = avn7;
        this.regi = regi;
        this.zres = zres;
        this.dmst = dmst;
        this.npie = npie;
        this.mrrx = mrrx;
        this.dmrr = dmrr;
        this.mtfr = mtfr;
        this.lieu = lieu;
        this.cods = cods;
        this.pnet = pnet;
        this.snet = snet;
        this.devp = devp;
        this.equi = equi;
        this.dels = dels;
        this.tits = tits;
        this.dtit = dtit;
        this.depr = depr;
        this.decc = decc;
        this.japa = japa;
        this.dapa = dapa;
        this.japec = japec;
        this.dapec = dapec;
        this.jded = jded;
        this.dded = dded;
        this.jrla = jrla;
        this.jrlec = jrlec;
        this.nbjcf = nbjcf;
        this.nbjaf = nbjaf;
        this.ddcf = ddcf;
        this.dfcf = dfcf;
        this.mtcf = mtcf;
        this.pmcf = pmcf;
        this.nbjse = nbjse;
        this.nbjsa = nbjsa;
        this.nmjf = nmjf;
        this.adr1 = adr1;
        this.adr2 = adr2;
        this.adr3 = adr3;
        this.adr4 = adr4;
        this.bpos = bpos;
        this.ntel = ntel;
        this.pnai = pnai;
        this.comm = comm;
        this.pbpe = pbpe;
        this.dchg = dchg;
        this.mchg = mchg;
        this.dfes = dfes;
        this.stor = stor;
        this.nbjtr = nbjtr;
        this.drtcg = drtcg;
        this.ddenv = ddenv;
        this.drenv = drenv;
        this.noss = noss;
        this.cont = cont;
        this.nbjsm = nbjsm;
        this.sana = sana;
        this.tyfo1 = tyfo1;
        this.tyfo2 = tyfo2;
        this.nifo = nifo;
        this.dchf = dchf;
        this.note = note;
        this.codeposte = codeposte;
        this.codesite = codesite;
        this.zli3 = zli3;
        this.zli4 = zli4;
        this.zli5 = zli5;
        this.zli6 = zli6;
        this.zli7 = zli7;
        this.zli8 = zli8;
        this.zli9 = zli9;
        this.zli10 = zli10;
        this.lnai = lnai;
        this.lemb = lemb;
        this.photo = photo;
        this.elementFixeSalaire = elementFixeSalaire;
        this.caisseMutuelleSalarie = caisseMutuelleSalarie;
        this.virementSalarie = virementSalarie;
        this.pretInterne = pretInterne;
    }

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
