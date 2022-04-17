package com.kinart.paie.business.model;


import java.math.BigDecimal;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "pretinterne")
public class PretInterne extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "lg")
    private Integer lg;

    @Column(name = "nmat", length = 10)
    private String nmat;

    @Column(name = "crub")
    private String crub;

    @Column(name = "mtpr")
    private BigDecimal mtpr;

    @Column(name = "nbmens")
    private BigDecimal nbmens;

    @Column(name = "mtmens")
    private BigDecimal mtmens;

    @Column(name = "premrb", length = 10)
    private String premrb;

    @Column(name = "mtremb")
    private BigDecimal mtremb;

    @Column(name = "etatpr", length = 1)
    private String etatpr;

    @ManyToOne
    @JoinColumn(name = "idsalarie")
    private Salarie salarie;
}
