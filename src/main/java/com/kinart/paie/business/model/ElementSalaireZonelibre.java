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
@Entity(name = "ElementSalaireZonelibre")
@Table(name = "elementsalairezonelibre")
public class ElementSalaireZonelibre extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer identreprise;

    @Column(name = "crub", length = 10)
    private String crub;

    @Column(name = "czli")
    private Integer czli;

    @Column(name = "zli1", length = 10)
    private String zli1;

    @Column(name = "zli2", length = 10)
    private String zli2;

}
