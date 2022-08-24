package com.kinart.evaluation.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/** @author cmbassi */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "Evaletape")
@Table(name = "evaletape")
public class Evaletape extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "code", length = 10)
    private String code;

    @Column(name = "codephase", length = 10)
    private String codephase;

    @Column(name = "libelle", length = 500)
    private String libelle;

    @Column(name = "bcommentaire", length = 500)
    private String bcommentaire;

    @Column(name = "bobjstatique", length = 10)
    private String bobjstatique;

    @Column(name = "bnote", length = 10)
    private String bnote;

    @Column(name = "bintercodification", length = 10)
    private String bintercodification;

    @Column(name = "bcoefficient", length = 10)
    private String bcoefficient;

    @Column(name = "bsanction", length = 10)
    private String bsanction;

    @Column(name = "bdelais", length = 10)
    private String bdelais;

    @Column(name = "bnotechiffre", length = 10)
    private String bnotechiffre;

    @Column(name = "bnotelettre", length = 10)
    private String bnotelettre;

    @Column(name = "bdesription", length = 100)
    private String bdesription;

    @Column(name = "codeniveau", length = 10)
    private String etapesuiv;

    @Column(name = "bderniereetape", length = 10)
    private String bderniereetape;

    @Column(name = "butilise", length = 10)
    private String butilise;

    @Column(name = "bnotece", length = 10)
    private String bnotece;

    @Column(name = "bvisa", length = 10)
    private String bvisa;

    @Column(name = "btotala1", length = 10)
    private String btotala1;

    @Column(name = "btotal", length = 10)
    private String btotal;

    @Column(name = "btotalce", length = 10)
    private String btotalce;

    @Column(name = "bnotea1", length = 10)
    private String bnotea1;

    @Column(name = "coef", length = 10)
    private String coef;

    @Column(name = "coefce", length = 10)
    private String coefce;

    @Column(name = "coefev", length = 10)
    private String coefev;

    @Column(name = "bobjdynamique", length = 10)
    private String bobjdynamique;

    @Column(name = "bresume", length = 10)
    private String bresume;

    @Column(name = "brealisation", length = 10)
    private String brealisation;

    @Column(name = "bobjcommenta1", length = 10)
    private String bobjcommenta1;
}
