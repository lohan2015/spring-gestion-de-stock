package com.kinart.paie.business.model;

import java.math.BigDecimal;

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
@Table(name = "elementsalaireajus")
public class ElementSalaireAjus extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "sessionId")
    private BigDecimal sessionId;

    @Column(name = "crub", length = 10)
    private String crub;

    @Column(name = "ajnu")
    private Integer ajnu;

    @Column(name = "mont")
    private BigDecimal mont;

}
