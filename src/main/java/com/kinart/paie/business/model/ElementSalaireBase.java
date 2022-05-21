package com.kinart.paie.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "ElementSalaireBase")
@Table(name = "elementsalairebase")
public class ElementSalaireBase extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "crub", length = 10)
    private String crub;

    @Column(name = "nume")
    private Integer nume;

    @Column(name = "sign", length = 1)
    private String sign;

    @Column(name = "rubk", length = 10)
    private String rubk;

    @ManyToOne
    @JoinColumn(name = "idelementsalaire")
    private ElementSalaire elementSalaire;
}
