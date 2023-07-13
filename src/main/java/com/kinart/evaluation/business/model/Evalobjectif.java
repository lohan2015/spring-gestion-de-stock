package com.kinart.evaluation.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;


/** @author cmbassi */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "Evalobjectif")
@Table(name = "evalobjectif")
public class Evalobjectif extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "nmat", length = 10)
    private String nmat;

    @Column(name = "codeeval", length = 10)
    private String codeeval;

    @Column(name = "ordre")
    private Integer ordre;

    @Column(name = "nature", length = 100)
    private String nature;

    @Column(name = "description", length = 200)
    private String desc;

    @Column(name = "commentaire", length = 500)
    private String comment;

    @Column(name = "codeniveau", length = 10)
    private String moy;

    @Column(name = "etat", length = 10)
    private String etat;

    @Column(name = "poids")
    private BigDecimal poids;

    @Column(name = "note")
    private BigDecimal note;

    @Column(name = "notep")
    private BigDecimal notep;

    @Column(name = "txatt")
    private BigDecimal txatt;

    @Column(name = "ot1")
    private BigDecimal ot1;

    @Column(name = "ot2")
    private BigDecimal ot2;

    @Column(name = "ot3")
    private BigDecimal ot3;

    @Column(name = "ot4")
    private BigDecimal ot4;

    @Column(name = "rt1")
    private BigDecimal rt1;

    @Column(name = "rt2")
    private BigDecimal rt2;

    @Column(name = "rt3")
    private BigDecimal rt3;

    @Column(name = "rt4")
    private BigDecimal rt4;

    @Column(name = "codeobj", length = 10)
    private String codeobj;
    
    @Column(name = "notecol", length = 10)
    private String notecol;

    @Column(name = "noteevl1", length = 10)
    private String noteevl1;

    @Column(name = "noteevl2", length = 10)
    private String noteevl2;

}
