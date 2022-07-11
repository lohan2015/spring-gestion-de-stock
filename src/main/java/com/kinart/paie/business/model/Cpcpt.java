package com.kinart.paie.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "Cpcpt")
@Table(name = "cpcpt")
public class Cpcpt extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "ncpt", length = 50)
    private String ncpt;

    @Column(name = "intl", length = 50)
    private String intl;

    @Column(name = "intr", length = 50)
    private String intr;

    @Column(name = "prof", length = 50)
    private String prof;

    @Column(name = "susp", length = 50)
    private String susp;

    @Column(name = "ctie", length = 50)
    private String ctie;

    @Column(name = "cana", length = 10)
    private String cana;

    @Column(name = "cpta", length = 10)
    private String cpta;

    @Column(name = "seca", length = 10)
    private String seca;

    @Column(name = "seca2", length = 10)
    private String seca2;

    @Column(name = "cled", length = 10)
    private String cled;

    @Column(name = "clec", length = 10)
    private String clec;

    @Column(name = "ccen", length = 10)
    private String ccen;

    @Column(name = "cran", length = 10)
    private String cran;

    @Column(name = "spgl", length = 10)
    private String spgl;

    @Column(name = "csta", length = 10)
    private String csta;

    @Column(name = "snco", length = 10)
    private String snco;

    @Column(name = "dval", length = 10)
    private String dval;

    @Column(name = "dech", length = 10)
    private String dech;

    @Column(name = "deud", length = 10)
    private String deud;

    @Column(name = "cdsu", length = 10)
    private String cdsu;

    @Column(name = "cjau1", length = 10)
    private String cjau1;

    @Column(name = "cjau2", length = 10)
    private String cjau2;

    @Column(name = "cjau3", length = 10)
    private String cjau3;

    @Column(name = "cjau4", length = 10)
    private String cjau4;

    @Column(name = "cjau5", length = 10)
    private String cjau5;

    @Column(name = "cjau6", length = 10)
    private String cjau6;

    @Column(name = "cjau7", length = 10)
    private String cjau7;

    @Column(name = "cjau8", length = 10)
    private String cjau8;

    @Column(name = "dclt", length = 10)
    private String dclt;

    @Column(name = "sold")
    private BigDecimal sold;

    @Column(name = "adr1", length = 10)
    private String adr1;

    @Column(name = "adr2", length = 10)
    private String adr2;

    @Column(name = "adr3", length = 10)
    private String adr3;

    @Column(name = "adr4", length = 10)
    private String adr4;

    @Column(name = "cpst", length = 10)
    private String cpst;

    @Column(name = "bdis", length = 10)
    private String bdis;

    @Column(name = "pays", length = 10)
    private String pays;

    @Column(name = "tel1", length = 10)
    private String tel1;

    @Column(name = "tel2", length = 10)
    private String tel2;

    @Column(name = "tlcp", length = 10)
    private String tlcp;

    @Column(name = "tlex", length = 10)
    private String tlex;

    @Column(name = "tste", length = 10)
    private String tste;

    @Column(name = "dcst", length = 10)
    private Date dcst;

    @Column(name = "cact", length = 10)
    private String cact;

    @Column(name = "dire", length = 10)
    private String dire;

    @Column(name = "cial", length = 10)
    private String cial;

    @Column(name = "tial", length = 10)
    private String tial;

    @Column(name = "comp", length = 10)
    private String comp;

    @Column(name = "tcpt", length = 10)
    private String tcpt;

    @Column(name = "bqbq", length = 10)
    private String bqbq;

    @Column(name = "bqgu", length = 10)
    private String bqgu;

    @Column(name = "bqcp", length = 10)
    private String bqcp;

    @Column(name = "bqcl", length = 10)
    private String bqcl;

    @Column(name = "bqdm", length = 10)
    private String bqdm;

    @Column(name = "bqtl", length = 10)
    private String bqtl;

    @Column(name = "mpfr", length = 10)
    private String mpfr;

    @Column(name = "mpnr", length = 10)
    private String mpnr;

    @Column(name = "mpjf", length = 10)
    private String mpjf;

    @Column(name = "mpnj")
    private Integer mpnj;

    @Column(name = "mpje")
    private Integer mpje;

    @Column(name = "limc")
    private BigDecimal limc;

    @Column(name = "dtdf")
    private Date dtdf;

    @Column(name = "dtdr")
    private Date dtdr;

    @Column(name = "nddc")
    private Integer nddc;

    @Column(name = "dcco")
    private Date dcco;

    @Column(name = "dtlt")
    private Date dtlt;

    @Column(name = "ecran", length = 10)
    private String ecran;

    @Column(name = "lett", length = 10)
    private String lett;

    @Column(name = "lets", length = 10)
    private String lets;

    @Column(name = "letb", length = 10)
    private String letb;

    @Column(name = "rela", length = 10)
    private String rela;

    @Column(name = "saets", length = 10)
    private String saets;

}
