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
@Entity(name = "ElementSalaireNet")
@Table(name = "elementsalairenet")
public class ElementSalaireNet extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer identreprise;

    @Column(name = "session_id")
    private BigDecimal session_id;

    @Column(name = "crub", length = 10)
    private String crub;

    @Column(name = "ajnu")
    private Integer ajnu;

    @Column(name = "mont")
    private BigDecimal mont;
}
