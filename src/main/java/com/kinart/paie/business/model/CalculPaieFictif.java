package com.kinart.paie.business.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "CalculPaieFictif")
@Table(name = "calculpaiefictif")
public class CalculPaieFictif  extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "nmat", length = 10)
    private String nmat;

    @Column(name = "aamm", length = 10)
    private String aamm;

    @Column(name = "nbul")
    private Integer nbul;

    @Column(name = "nlig")
    private Integer nlig;

    @Column(name = "rubq", length = 10)
    private String rubq;

    @Column(name = "basc")
    private BigDecimal basc;

    @Column(name = "basp")
    private BigDecimal basp;

    @Column(name = "taux")
    private BigDecimal taux;

    @Column(name = "mont")
    private BigDecimal mont;

    @Column(name = "nprt", length = 10)
    private String nprt;

    @Column(name = "ruba", length = 10)
    private String ruba;

    @Column(name = "argu", length = 30)
    private String argu;

    @Column(name = "clas", length = 10)
    private String clas;

    @Column(name = "trtb", length = 1)
    private String trtb;

}
