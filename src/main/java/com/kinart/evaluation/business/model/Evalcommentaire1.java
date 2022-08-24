package com.kinart.evaluation.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/** @author cmbassi */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "Evalcommentaire1")
@Table(name = "evalcommentaire1")
public class Evalcommentaire1 extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "commentid", length = 10)
    private String commentid;

    @Column(name = "coeetape", length = 10)
    private String coeetape;

    @Column(name = "codephase", length = 10)
    private String codephase;

    @Column(name = "codeeval", length = 10)
    private String codeeval;

    @Column(name = "nmat", length = 10)
    private String nmat;

    @Column(name = "codemodele", length = 10)
    private String codemodele;

    @Column(name = "commentaire", length = 200)
    private String commentaire;

    @Column(name = "visaacceptation", length = 10)
    private String visaacceptation;

    @Column(name = "titre", length = 200)
    private String titre;

    @Column(name = "delais", length = 10)
    private String delais;

    @Column(name = "critreussite", length = 100)
    private String critreussite;
}
