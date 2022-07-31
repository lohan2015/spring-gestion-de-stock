package com.kinart.organisation.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;


/** @author c.mbassi */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "Organigramme")
@Table(name = "organigramme")
public class Organigramme extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "codeorganigramme", length = 20)
    private String codeorganigramme;

    @Column(name = "libelle", length = 100)
    private String libelle;

    @Column(name = "codeniveau", length = 10)
    private String codeniveau;

    @Column(name = "datecree")
    private Date datecree;

    @Column(name = "estvalide", length = 1)
    private String estvalide;

    @Column(name = "accepteexterne", length = 1)
    private String accepteexterne;

    @Column(name = "codeposte", length = 10)
    private String codeposte;

    @Column(name = "codepere", length = 20)
    private String codepere;

    @Column(name = "codematricule", length = 10)
    private String codematricule;

    @Column(name = "bprestataire", length = 1)
    private String bprestataire;

    @Column(name = "bcasefictive", length = 1)
    private String bcasefictive;

    @Column(name = "codesite", length = 10)
    private String codesite;

    @Column(name = "nivfonction", length = 10)
    private String nivfonction;

    @Column(name = "libellecourt", length = 50)
    private String libellecourt;
}
