package com.cmbassi.gestiondepaie.model;

import com.cmbassi.gestiondestock.model.AbstractEntity;
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
@Table(name = "sequenceauto")
public class SequenceAuto extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "codecompteur", length = 10)
    private String codecompteur;

    @Column(name = "libelle", length = 100)
    private String libelle;

    @Column(name = "incrementinitial", length = 5)
    private String incrementinitial;

    @Column(name = "longueurincrement", length = 5)
    private String longueurincrement;

    @Column(name = "prochainincrement", length = 5)
    private String prochainincrement;

    @Column(name = "ajoutersouche", length = 5)
    private String ajoutersouche;

    @Column(name = "ajouterdossier", length = 5)
    private String ajouterdossier;

    @Column(name = "ordredossier", length = 5)
    private String ordredossier;

    @Column(name = "ajouterannee", length = 5)
    private String ajouterannee;

    @Column(name = "longueurannee", length = 5)
    private String longueurannee;

    @Column(name = "ordreannee", length = 5)
    private String ordreannee;

    @Column(name = "ajoutermois", length = 5)
    private String ajoutermois;

    @Column(name = "ordremois", length = 5)
    private String ordremois;

    @Column(name = "ajouterautre", length = 5)
    private String ajouterautre;

    @Column(name = "ordreautre", length = 5)
    private String ordreautre;

    @Column(name = "cutilcree", length = 10)
    private String cutilcree;

    @Column(name = "cutilmod", length = 20)
    private String cutilmod;

    @Column(name = "valeurautre", length = 10)
    private String valeurautre;

    @Column(name = "ajouterjour", length = 5)
    private String ajouterjour;

    @Column(name = "ordrejour", length = 5)
    private String ordrejour;

}
