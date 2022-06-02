package com.kinart.paie.business.model;

import java.util.Date;

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
@Entity(name = "CpDevise")
@Table(name = "cpdevise")
public class CpDevise extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "numaxe")
    private Integer numaxe;

    @Column(name = "coddes", length = 50)
    private String coddes;

    @Column(name = "libdes", length = 100)
    private String libdes;

    @Column(name = "etat", length = 10)
    private String etat;

    @Column(name = "typdes", length = 10)
    private String typdes;

    @Column(name = "debsai")
    private Date debsai;

    @Column(name = "finsai")
    private Date finsai;

    @Column(name = "coduti", length = 50)
    private String coduti;
}
