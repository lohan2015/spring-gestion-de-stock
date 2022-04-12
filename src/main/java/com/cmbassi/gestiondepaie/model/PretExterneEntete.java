package com.cmbassi.gestiondepaie.model;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.cmbassi.gestiondestock.model.AbstractEntity;
import lombok.*;

import javax.persistence.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "pretexterneentete")
public class PretExterneEntete extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "nmat", length = 10)
    private String nmat;

    @Column(name = "nprt", length = 10)
    private String nprt;

    @Column(name = "crub", length = 10)
    private String crub;

    @Column(name = "com1", length = 100)
    private String com1;

    @Column(name = "com2", length = 100)
    private String com2;

    @Column(name = "dmep")
    private Date dmep;

    @Column(name = "dpec")
    private Date dpec;

    @Column(name = "per1")
    private Date per1;

    @Column(name = "mntp")
    private BigDecimal mntp;

    @Column(name = "resr")
    private BigDecimal resr;

    @Column(name = "nbec")
    private Integer nbec;

    @Column(name = "nber")
    private Integer nber;

    @Column(name = "mtec")
    private BigDecimal mtec;

    @Column(name = "tint")
    private BigDecimal tint;

    @Column(name = "ttax")
    private BigDecimal ttax;

    @Column(name = "pact")
    private String pact;

    @Column(name = "etpr", length = 10)
    private String etpr;

    @Column(name = "dcrp")
    private Date dcrp;

    @Column(name = "ncpt", length = 15)
    private String ncpt;

    @Column(name = "codenatpret", length = 10)
    private String codenatpret;

    @Column(name = "etatsolde", length = 10)
    private String etatsolde;

    @Column(name = "codedevise", length = 10)
    private String codedevise;

    @ManyToOne
    @JoinColumn(name = "idsalarie")
    private Salarie salarie;

    @OneToMany(mappedBy = "entetePretExterne")
    private List<PretExterneDetail> pretExterneDetail;
}
