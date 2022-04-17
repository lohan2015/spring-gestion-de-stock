package com.kinart.api.gestiondepaie.dto;

import com.kinart.paie.business.model.SequenceAuto;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Data
@Builder
public class SequenceAutoDto implements Serializable {
    private Integer id;
    private Integer idEntreprise;
    private String codecompteur;
    private String libelle;
    private String incrementinitial;
    private String longueurincrement;
    private String prochainincrement;
    private String ajoutersouche;
    private String ajouterdossier;
    private String ordredossier;
    private String ajouterannee;
    private String longueurannee;
    private String ordreannee;
    private String ajoutermois;
    private String ordremois;
    private String ajouterautre;
    private String ordreautre;
    private String cutilcree;
    private String cutilmod;
    private String valeurautre;
    private String ajouterjour;
    private String ordrejour;

    public SequenceAutoDto() {
    }

    public SequenceAutoDto(Integer id, Integer idEntreprise, String codecompteur, String libelle, String incrementinitial, String longueurincrement, String prochainincrement, String ajoutersouche, String ajouterdossier, String ordredossier, String ajouterannee, String longueurannee, String ordreannee, String ajoutermois, String ordremois, String ajouterautre, String ordreautre, String cutilcree, String cutilmod, String valeurautre, String ajouterjour, String ordrejour) {
        this.id = id;
        this.idEntreprise = idEntreprise;
        this.codecompteur = codecompteur;
        this.libelle = libelle;
        this.incrementinitial = incrementinitial;
        this.longueurincrement = longueurincrement;
        this.prochainincrement = prochainincrement;
        this.ajoutersouche = ajoutersouche;
        this.ajouterdossier = ajouterdossier;
        this.ordredossier = ordredossier;
        this.ajouterannee = ajouterannee;
        this.longueurannee = longueurannee;
        this.ordreannee = ordreannee;
        this.ajoutermois = ajoutermois;
        this.ordremois = ordremois;
        this.ajouterautre = ajouterautre;
        this.ordreautre = ordreautre;
        this.cutilcree = cutilcree;
        this.cutilmod = cutilmod;
        this.valeurautre = valeurautre;
        this.ajouterjour = ajouterjour;
        this.ordrejour = ordrejour;
    }

    public static SequenceAutoDto fromEntity(SequenceAuto sequenceAuto) {
        if (sequenceAuto == null) {
            return null;
        }
        SequenceAutoDto dto = new  SequenceAutoDto();
        BeanUtils.copyProperties(sequenceAuto, dto);
        return dto;
    }

    public static SequenceAuto toEntity(SequenceAutoDto dto) {
        if (dto == null) {
            return null;
        }
        SequenceAuto pretExterneDetail = new SequenceAuto();
        BeanUtils.copyProperties(dto, pretExterneDetail);
        return pretExterneDetail;
    }
}
