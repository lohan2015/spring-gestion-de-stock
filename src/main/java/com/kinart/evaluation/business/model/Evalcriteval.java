package com.kinart.evaluation.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/** @author cmbassi */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "Evalcriteval")
@Table(name = "evalcriteval")
public class Evalcriteval extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "codecrit", length = 10)
    private String codecrit;

    @Column(name = "codeobj", length = 10)
    private String codeobj;

    @Column(name = "codeetape", length = 10)
    private String codeetape;

    @Column(name = "nmat", length = 10)
    private String nmat;

    @Column(name = "codephase", length = 10)
    private String codephase;

    @Column(name = "codeeval", length = 10)
    private String codeeval;

    @Column(name = "dadeb")
    private Date dadeb;

    @Column(name = "dafin")
    private Date dafin;

    @Column(name = "notece")
    private BigDecimal notece;

    @Column(name = "notecoefce")
    private BigDecimal notecoefce;

    @Column(name = "notelettreevaluat", length = 10)
    private String notelettreevaluat;

    @Column(name = "notecoeflettreevaluat", length = 10)
    private String notecoeflettreevaluat;

    @Column(name = "notechifevaluat")
    private BigDecimal notechifevaluat;

    @Column(name = "notecoefchifevaluat", length = 10)
    private String notecoefchifevaluat;

    @Column(name = "coefcrit", length = 10)
    private String coefcrit;

    @Column(name = "sanction", length = 10)
    private String sanction;

    @Column(name = "typenomenc", length = 10)
    private String typenomenc;

    @Column(name = "budgetreel", length = 10)
    private String budgetreel;

    @Column(name = "qualreel", length = 10)
    private String qualreel;

    @Column(name = "satreel", length = 10)
    private String satreel;

    @Column(name = "distreel", length = 10)
    private String distreel;

    @Column(name = "realise", length = 10)
    private String realise;

    @Column(name = "duree", length = 10)
    private String duree;

    @Column(name = "statenomenc", length = 10)
    private String statenomenc;

    @Column(name = "total")
    private BigDecimal total;

    @Column(name = "totala1")
    private BigDecimal totala1;

    @Column(name = "totalce")
    private BigDecimal totalce;

    @Column(name = "typeobj", length = 10)
    private String typeobj;

    @Column(name = "libelle", length = 200)
    private String libelle;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "realisation", length = 100)
    private String realisation;

    @Column(name = "critreussite", length = 10)
    private String critreussite;

}
