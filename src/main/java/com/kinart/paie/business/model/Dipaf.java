package com.kinart.paie.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "Dipaf")
@Table(name = "dipaf")
public class Dipaf extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "vild", length = 10)
    private String vild;

    @Column(name = "nume")
    private Integer nume;

    @Column(name = "lett", length = 10)
    private String lett;

    @Column(name = "annee")
    private BigDecimal annee;

    @Column(name = "indi", length = 10)
    private String indi;

    @Column(name = "cuticre", length = 100)
    private String cuticre;

    @Column(name = "cutimod", length = 100)
    private String cutimod;
}
