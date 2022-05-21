package com.kinart.paie.business.model;


import java.math.BigDecimal;
import java.util.Date;

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
@Entity(name = "ElementVariableConge")
@Table(name = "elementvariableconge")
public class ElementVariableConge extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "aamm", length = 6)
    private String aamm;

    @Column(name = "nmat", length = 10)
    private String nmat;

    @Column(name = "nbul")
    private Integer nbul;

    @Column(name = "ddeb")
    private Date ddeb;

    @Column(name = "dfin")
    private Date dfin;

    @Column(name = "nbjc")
    private BigDecimal nbjc;

    @Column(name = "nbja")
    private BigDecimal nbja;

    @Column(name = "motf", length = 10)
    private String motf;

    @Column(name = "mont")
    private BigDecimal mont;

    @Column(name = "cuti", length = 30)
    private String cuti;
}
