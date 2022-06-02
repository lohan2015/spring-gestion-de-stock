package com.kinart.paie.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/** @author Hibernate CodeGenerator */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "Compte")
@Table(name = "compte")
public class Compte extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "numcpt", length = 50)
    private String numcpt;

    @Column(name = "intcpt", length = 200)
    private String intcpt;

    @Column(name = "etat", length = 10)
    private String etat;

    @Column(name = "typcpt", length = 20)
    private String typcpt;

    @Column(name = "raccpt", length = 20)
    private String raccpt;

    @Column(name = "codpro", length = 20)
    private String codpro;

    @Column(name = "cptrep", length = 20)
    private String cptrep;

    @Column(name = "coddev", length = 10)
    private String coddev;

    @Column(name = "gesech", length = 20)
    private String gesech;

    @Column(name = "sautpg", length = 20)
    private String sautpg;

    @Column(name = "codnat", length = 20)
    private String codnat;

    @Column(name = "numlet")
    private Integer numlet;

    @Column(name = "comm", length = 200)
    private String comm;

    @Column(name = "coduti", length = 50)
    private String coduti;

    @Column(name = "codets", length = 10)
    private String codets;

}
