package com.cmbassi.gestiondepaie.model;

import java.math.BigDecimal;

import com.cmbassi.gestiondestock.model.AbstractEntity;
import lombok.*;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "virementsalarie")
public class VirementSalarie extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "nmat", length = 10)
    private String nmat;

    @Column(name = "bqag", length = 10)
    private String bqag;

    @Column(name = "guic", length = 10)
    private String guic;

    @Column(name = "comp", length = 20)
    private String comp;

    @Column(name = "cle", length = 10)
    private String cle;

    @Column(name = "bqso", length = 10)
    private String bqso;

    @Column(name = "pourc")
    private Integer pourc;

    @Column(name = "mont")
    private BigDecimal mont;

    @Column(name = "dvd", length = 10)
    private String dvd;

    @Column(name = "txchg")
    private BigDecimal txchg;

    @Column(name = "mntdb")
    private BigDecimal mntdb;

    @Column(name = "mntdvd")
    private BigDecimal mntdvd;

    @Column(name = "aamm", length = 6)
    private String aamm;

    @Column(name = "princ", length = 1)
    private String princ;

    @Column(name = "swift", length = 20)
    private String swift;

    @Column(name = "titu", length = 15)
    private String titu;

    @ManyToOne
    @JoinColumn(name = "idsalarie")
    private Salarie salarie;
}
