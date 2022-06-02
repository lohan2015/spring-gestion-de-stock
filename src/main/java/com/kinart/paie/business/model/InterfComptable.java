package com.kinart.paie.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;


/** @author Hibernate CodeGenerator */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "InterfComptable")
@Table(name = "interfcomptable")
public class InterfComptable extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "codets", length = 10)
    private String codets;

    @Column(name = "codjou", length = 10)
    private String codjou;

    @Column(name = "numcpt", length = 50)
    private String numcpt;

    @Column(name = "numtie", length = 50)
    private String numtie;

    @Column(name = "numpce", length = 20)
    private String numpce;

    @Column(name = "datcpt")
    private Date datcpt;

    @Column(name = "datpce")
    private Date datpce;

    @Column(name = "datech")
    private Date datech;

    @Column(name = "devpce", length = 20)
    private String devpce;

    @Column(name = "quantite")
    private BigDecimal quantite;

    @Column(name = "sens", length = 1)
    private String sens;

    @Column(name = "pceMt")
    private BigDecimal pceMt;

    @Column(name = "reflet", length = 20)
    private String reflet;

    @Column(name = "coddes1", length = 20)
    private String coddes1;

    @Column(name = "coddes2", length = 20)
    private String coddes2;

    @Column(name = "coddes3", length = 20)
    private String coddes3;

    @Column(name = "coddes4", length = 20)
    private String coddes4;

    @Column(name = "coddes5", length = 20)
    private String coddes5;

    @Column(name = "coddes6", length = 20)
    private String coddes6;

    @Column(name = "coddes7", length = 20)
    private String coddes7;

    @Column(name = "coddes8", length = 20)
    private String coddes8;

    @Column(name = "coddes9", length = 20)
    private String coddes9;

    @Column(name = "libecr", length = 500)
    private String libecr;

    @Column(name = "codtre", length = 20)
    private String codtre;

    @Column(name = "codabr", length = 20)
    private String codabr;

    @Column(name = "coderr", length = 20)
    private String coderr;

    @Column(name = "liberr", length = 200)
    private String liberr;

    @Column(name = "coduti", length = 50)
    private String coduti;

}
