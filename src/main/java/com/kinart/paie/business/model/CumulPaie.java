package com.kinart.paie.business.model;

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
@Entity
@Table(name = "cumulpaie")
public class CumulPaie extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "nmat", length = 10)
    private String nmat;

    @Column(name = "aamm", length = 10)
    private String aamm;

    @Column(name = "rubq", length = 10)
    private String rubq;

    @Column(name = "nbul")
    private Integer nbul;

    @Column(name = "basc")
    private BigDecimal basc;

    @Column(name = "basp")
    private BigDecimal basp;

    @Column(name = "taux")
    private BigDecimal taux;

    @Column(name = "mont")
    private BigDecimal mont;

}
