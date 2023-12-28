package com.kinart.portail.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Builder
@NoArgsConstructor
//@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "enqbienetre")
public class EnqBienEtre extends AbstractEntity {

    @Column(name = "nmat", length = 6, nullable=false)
    private String nmat;

    @Column(name = "annee", length = 4, nullable=false)
    private int annee;

    @Column(name = "nume", length = 1, nullable=false)
    private int nume;

    @Column(name = "question", length = 1000)
    private String question;

    @Column(name = "reponse", length = 1000)
    private String reponse;

    public EnqBienEtre(String nmat, int annee, int nume, String question, String reponse){
        this.nmat = nmat;
        this.annee = annee;
        this.nume = nume;
        this.question = question;
        this.reponse = reponse;
    }
}
