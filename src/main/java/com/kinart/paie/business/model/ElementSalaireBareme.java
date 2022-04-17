package com.kinart.paie.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "elementsalairebareme")
public class ElementSalaireBareme extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "crub", length = 10)
    private String crub;

    @Column(name = "nume")
    private Integer nume;

    @Column(name = "val1", length = 20)
    private String val1;

    @Column(name = "val2", length = 20)
    private String val2;

    @Column(name = "taux")
    private BigDecimal taux;

    @Column(name = "mont")
    private BigDecimal mont;

    @ManyToOne
    @JoinColumn(name = "idelementsalaire")
    private ElementSalaire elementSalaire;
}
