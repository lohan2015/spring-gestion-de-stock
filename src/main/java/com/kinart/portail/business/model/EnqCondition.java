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
@Table(name = "enqcondition")
public class EnqCondition extends AbstractEntity {

    @Column(name = "nmat", length = 100, nullable=false)
    private String nmat;

    @Column(name = "annee", length = 4, nullable=false)
    private int annee;

    @Column(name = "titre", length = 500)
    private String titre;

    @Column(name = "reponse", length = 1000)
    private String reponse;

    public EnqCondition(String nmat, int annee, String titre){
        this.nmat = nmat;
        this.annee = annee;
        this.titre = titre;
    }
}
