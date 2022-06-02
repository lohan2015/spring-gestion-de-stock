package com.kinart.paie.business.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "Salarie")
@Table(name = "salarie")
public class Salarie extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer identreprise;

    @Size(max=6, message = "Le matricule du salarié devra avoir une taille maximale égale à 6")
    @Column(name = "nmat", length = 6)
    private String nmat;

    @Size(max=10, message = "Le niv1 devra avoir une taille maximale égale à 10")
    @Column(name = "niv1", length = 10)
    private String niv1;

    @Size(max=10, message = "Le niv2 devra avoir une taille maximale égale à 10")
    @Column(name = "niv2", length = 10)
    private String niv2;

    @Size(max=10, message = "Le niv3 devra avoir une taille maximale égale à 10")
    @Column(name = "niv3", length = 10)
    private String niv3;

    @Column(name = "cals", length = 10)
    private String cals;

    @Column(name = "clas", length = 10)
    private String clas;

    @Column(name = "nom", length = 100)
    private String nom;

    @Column(name = "pren", length = 100)
    private String pren;

    @Column(name = "sexe", length = 1)
    private String sexe;

    @Column(name = "dtna")
    private Date dtna;

    @Column(name = "nato", length = 10)
    private String nato;

    @Column(name = "sitf", length = 10)
    private String sitf;

    @Column(name = "nbcj")
    private Integer nbcj;

    @Column(name = "nbef")
    private Integer nbef;

    @Column(name = "nbec")
    private Integer nbec;

    @Column(name = "nbfe")
    private Integer nbfe;

    @Column(name = "nbpt")
    private BigDecimal nbpt;

    @Column(name = "modp", length = 10)
    private String modp;

    @Column(name = "banq", length = 20)
    private String banq;

    @Column(name = "guic", length = 10)
    private String guic;

    @Column(name = "comp", length = 50)
    private String comp;

    @Column(name = "cle", length = 10)
    private String cle;

    @Column(name = "ccpt", length = 20)
    private String ccpt;

    @Column(name = "bqso", length = 10)
    private String bqso;

    @Column(name = "vild", length = 10)
    private String vild;

    @Column(name = "cat", length = 10)
    private String cat;

    @Column(name = "ech", length = 10)
    private String ech;

    @Column(name = "grad", length = 10)
    private String grad;

    @Column(name = "fonc", length = 10)
    private String fonc;

    @Column(name = "afec", length = 10)
    private String afec;

    @Column(name = "codf", length = 10)
    private String codf;

    @Column(name = "indi")
    private Integer indi;

    @Column(name = "ctat", length = 10)
    private String ctat;

    @Column(name = "tinp")
    private BigDecimal tinp;

    @Column(name = "synd", length = 10)
    private String synd;

    @Column(name = "hifo", length = 10)
    private String hifo;

    @Column(name = "zli1", length = 10)
    private String zli1;

    @Column(name = "zli2", length = 10)
    private String zli2;

    @Column(name = "dtes")
    private Date dtes;

    @Column(name = "ddca")
    private Date ddca;

    @Column(name = "typc", length = 10)
    private String typc;

    @Column(name = "avn1", length = 10)
    private String avn1;

    @Column(name = "avn2", length = 10)
    private String avn2;

    @Column(name = "avn3", length = 10)
    private String avn3;

    @Column(name = "avn4", length = 10)
    private String avn4;

    @Column(name = "avn5", length = 10)
    private String avn5;

    @Column(name = "avn6", length = 10)
    private String avn6;

    @Column(name = "avn7", length = 10)
    private String avn7;

    @Column(name = "regi", length = 10)
    private String regi;

    @Column(name = "zres", length = 10)
    private String zres;

    @Column(name = "dmst", length = 100)
    private String dmst;

    @Column(name = "npie")
    private Integer npie;

    @Column(name = "mrrx", length = 10)
    private String mrrx;

    @Column(name = "dmrr")
    private Date dmrr;

    @Column(name = "mtfr", length = 10)
    private String mtfr;

    @Column(name = "lieu", length = 100)
    private String lieu;

    @Column(name = "cods", length = 10)
    private String cods;

    @Column(name = "pnet", length = 10)
    private String pnet;

    @Column(name = "snet")
    private BigDecimal snet;

    @Column(name = "devp", length = 10)
    private String devp;

    @Column(name = "equi", length = 10)
    private String equi;

    @Column(name = "dels", length = 10)
    private String dels;

    @Column(name = "tits", length = 10)
    private String tits;

    @Column(name = "dtit")
    private Date dtit;

    @Column(name = "depr")
    private Date depr;

    @Column(name = "decc")
    private Date decc;

    @Column(name = "japa")
    private BigDecimal japa;

    @Column(name = "dapa")
    private BigDecimal dapa;

    @Column(name = "japec")
    private BigDecimal japec;

    @Column(name = "dapec")
    private BigDecimal dapec;

    @Column(name = "jded")
    private BigDecimal jded;

    @Column(name = "dded")
    private BigDecimal dded;

    @Column(name = "jrla")
    private BigDecimal jrla;

    @Column(name = "jrlec")
    private BigDecimal jrlec;

    @Column(name = "nbjcf")
    private BigDecimal nbjcf;

    @Column(name = "nbjaf")
    private BigDecimal nbjaf;

    @Column(name = "ddcf")
    private Date ddcf;

    @Column(name = "dfcf")
    private Date dfcf;

    @Column(name = "mtcf")
    private BigDecimal mtcf;

    @Column(name = "pmcf", length = 10)
    private String pmcf;

    @Column(name = "nbjse")
    private BigDecimal nbjse;

    @Column(name = "nbjsa")
    private BigDecimal nbjsa;

    @Column(name = "nmjf", length = 10)
    private String nmjf;

    @Column(name = "adr1", length = 100)
    private String adr1;

    @Column(name = "adr2", length = 100)
    private String adr2;

    @Column(name = "adr3", length = 100)
    private String adr3;

    @Column(name = "adr4", length = 100)
    private String adr4;

    @Column(name = "bpos", length = 30)
    private String bpos;

    @Column(name = "ntel", length = 50)
    private String ntel;

    @Column(name = "pnai", length = 10)
    private String pnai;

    @Column(name = "comm", length = 100)
    private String comm;

    @Column(name = "pbpe", length = 100)
    private String pbpe;

    @Column(name = "dchg")
    private Date dchg;

    @Column(name = "mchg", length = 10)
    private String mchg;

    @Column(name = "dfes")
    private Date dfes;

    @Column(name = "stor", length = 100)
    private String stor;

    @Column(name = "nbjtr")
    private BigDecimal nbjtr;

    @Column(name = "drtcg")
    private Date drtcg;

    @Column(name = "ddenv")
    private Date ddenv;

    @Column(name = "drenv")
    private Date drenv;

    @Column(name = "noss", length = 20)
    private String noss;

    @Column(name = "cont", length = 20)
    private String cont;

    @Column(name = "nbjsm")
    private BigDecimal nbjsm;

    @Column(name = "sana", length = 10)
    private String sana;

    @Column(name = "tyfo1", length = 10)
    private String tyfo1;

    @Column(name = "tyfo2", length = 10)
    private String tyfo2;

    @Column(name = "nifo", length = 10)
    private String nifo;

    @Column(name = "dchf")
    private Date dchf;

    @Column(name = "note", length = 100)
    private String note;

    @Column(name = "codeposte", length = 10)
    private String codeposte;

    @Column(name = "codesite", length = 10)
    private String codesite;

    @Column(name = "zli3", length = 10)
    private String zli3;

    @Column(name = "zli4", length = 10)
    private String zli4;

    @Column(name = "zli5", length = 10)
    private String zli5;

    @Column(name = "zli6", length = 10)
    private String zli6;

    @Column(name = "zli7", length = 10)
    private String zli7;

    @Column(name = "zli8", length = 10)
    private String zli8;

    @Column(name = "zli9", length = 10)
    private String zli9;

    @Column(name = "zli10", length = 10)
    private String zli10;

    @Column(name = "lnai", length = 100)
    private String lnai;

    @Column(name = "lemb", length = 100)
    private String lemb;

    @Column(name = "photo")
    private String photo;

    @JsonIgnore
    @OneToMany(mappedBy = "salarie")
    private List<ElementFixeSalaire> elementFixeSalaire;

    @JsonIgnore
    @OneToMany(mappedBy = "salarie")
    private List<CaisseMutuelleSalarie> caisseMutuelleSalarie;

    @JsonIgnore
    @OneToMany(mappedBy = "salarie")
    private List<VirementSalarie> virementSalarie;

    @JsonIgnore
    @OneToMany(mappedBy = "salarie")
    private List<PretInterne> pretInterne;

    @JsonIgnore
    @OneToMany(mappedBy = "salarie")
    private List<PretExterneEntete> pretExterneEntete;
}
