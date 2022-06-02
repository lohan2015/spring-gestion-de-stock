package com.kinart.paie.business.model;

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
@Entity(name = "ElementSalaireCompte")
@Table(name = "elementsalairecompte")
public class ElementSalaireCompte extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "crub", length = 10)
    private String crub;

    @Column(name = "nume")
    private Integer nume;

    @Column(name = "sens", length = 1)
    private String sens;

    @Column(name = "critere1", length = 50)
    private String critere1;

    @Column(name = "critere2", length = 50)
    private String critere2;

    @Column(name = "critere3", length = 50)
    private String critere3;

    @Column(name = "compte", length = 20)
    private String compte;

}
