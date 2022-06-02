package com.kinart.paie.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


/** @author Hibernate CodeGenerator */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "Etablissement")
@Table(name = "etablissement")
public class Etablissement extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "codets", length = 10)
    private String codets;

    @Column(name = "libets", length = 100)
    private String libets;

    @Column(name = "adresse1", length = 100)
    private String adresse1;

    @Column(name = "adresse2", length = 100)
    private String adresse2;

    @Column(name = "adresse3", length = 100)
    private String adresse3;

    @Column(name = "cpostal", length = 10)
    private String cpostal;

    @Column(name = "localite", length = 100)
    private String localite;

    @Column(name = "pays", length = 100)
    private String pays;

    @Column(name = "coduti", length = 50)
    private String coduti;

    @Column(name = "coddev", length = 10)
    private String coddev;

    @Column(name = "numsite")
    private Integer numsite;

}
