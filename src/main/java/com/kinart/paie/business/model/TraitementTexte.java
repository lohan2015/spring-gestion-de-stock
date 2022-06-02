package com.kinart.paie.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "TraitementTexte")
@Table(name = "traitementtexte")
public class TraitementTexte extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "sess", length = 20)
    private String sess;

    @Column(name = "nlig")
    private Integer nlig;

    @Column(name = "texte", length = 2000)
    private String texte;

}
