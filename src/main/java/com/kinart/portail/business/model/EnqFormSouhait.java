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
@Table(name = "enqformsouhait")
public class EnqFormSouhait extends AbstractEntity {

    @Column(name = "nmat", length = 100, nullable=false)
    private String nmat;

    @Column(name = "annee", length = 4, nullable=false)
    private int annee;

    @Column(name = "intitule", length = 200)
    private String intitule;

    @Column(name = "objectif", length = 1000)
    private String objectif;

    public EnqFormSouhait(String nmat, int annee){
        this.nmat = nmat;
        this.annee = annee;
    }
}
