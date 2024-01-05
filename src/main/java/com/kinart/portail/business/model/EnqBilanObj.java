package com.kinart.portail.business.model;

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
@Table(name = "enqbilanobj")
public class EnqBilanObj extends AbstractEntity {

    @Column(name = "nmat", length = 100, nullable=false)
    private String nmat;

    @Column(name = "annee", length = 4, nullable=false)
    private int annee;

    @Column(name = "objectif", length = 500)
    private String objectif;

    @Column(name = "nivatt", length = 20)
    private String nivAtt;

    @Column(name = "moyen", length = 1000)
    private String moyen;

    @Column(name = "commentaire", length = 1000)
    private String commentaire;

    public EnqBilanObj(String nmat, int annee){
        this.nmat = nmat;
        this.annee = annee;
    }
}
