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
@Table(name = "enqbilancompatt")
public class EnqBilanCompAtt extends AbstractEntity {

    @Column(name = "nmat", length = 6, nullable=false)
    private String nmat;

    @Column(name = "annee", length = 4, nullable=false)
    private int annee;

    @Column(name = "competence", length = 100)
    private String competence;

    @Column(name = "evaluation", length = 10)
    private String evaluation;

    @Column(name = "commentaire", length = 1000)
    private String commentaire;

    @Column(name = "action", length = 1000)
    private String action;
}
