package com.kinart.paie.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "congefic")
public class CongeFictif extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "nmat", length = 10)
    private String nmat;

    @Column(name = "aamm", length = 6)
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

    @Column(name = "argu", length = 10)
    private String argu;

    @Column(name = "clas", length = 10)
    private String clas;

    @Column(name = "trtb", length = 10)
    private String trtb;

}
