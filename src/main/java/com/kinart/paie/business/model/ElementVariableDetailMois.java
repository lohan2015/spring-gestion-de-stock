package com.kinart.paie.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "ElementVariableDetailMois")
@Table(name = "elementvariabledetailmois")
public class ElementVariableDetailMois extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "nlig")
    private Integer nlig;

    @Column(name = "nmat", length = 10)
    private String nmat;

    @Column(name = "nbul")
    private Integer nbul;

    @Column(name = "aamm", length = 6)
    private String aamm;

    @Column(name = "rubq", length = 10)
    private String rubq;

    @Column(name = "argu", length = 100)
    private String argu;

    @Column(name = "nprt", length = 10)
    private String nprt;

    @Column(name = "ruba", length = 10)
    private String ruba;

    @Column(name = "mont")
    private BigDecimal mont;

    @Column(name = "cuti", length = 20)
    private String cuti;

    @ManyToOne
    @JoinColumn(name = "idelementvariableentetemois")
    private ElementVariableEnteteMois enteteEltVar;
}
