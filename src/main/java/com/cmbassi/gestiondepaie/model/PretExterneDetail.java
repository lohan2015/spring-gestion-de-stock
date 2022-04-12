package com.cmbassi.gestiondepaie.model;


import java.math.BigDecimal;
import java.util.Date;

import com.cmbassi.gestiondestock.model.AbstractEntity;
import lombok.*;

import javax.persistence.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "pretexternedetail")
public class PretExterneDetail extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "nprt", length = 10)
    private String nprt;

    @Column(name = "perb")
    private Date perb;

    @Column(name = "echo")
    private BigDecimal echo;

    @Column(name = "echr")
    private BigDecimal echr;

    @Column(name = "inte")
    private BigDecimal inte;

    @Column(name = "taxe")
    private BigDecimal taxe;

    @Column(name = "nbul")
    private Integer nbul;

    @ManyToOne
    @JoinColumn(name = "idpretexterneentete")
    private PretExterneEntete entetePretExterne;
}
