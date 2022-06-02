package com.kinart.paie.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/** @author Hibernate CodeGenerator */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "FreeZoneParam")
@Table(name = "freezoneparam")
public class FreeZoneParam extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "numerozl")
    private Integer numerozl;

    @Column(name = "intitulezl", length = 100)
    private String intitulezl;

    @Column(name = "typezl", length = 10)
    private String typezl;

    @Column(name = "tablezl")
    private Integer tablezl;

    @Column(name = "actif", length = 1)
    private String actif;

}
