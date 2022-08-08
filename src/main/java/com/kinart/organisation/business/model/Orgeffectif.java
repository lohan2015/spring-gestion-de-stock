package com.kinart.organisation.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


/** @author c.mbassi */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "Orgeffectif")
@Table(name = "orgeffectif")
public class Orgeffectif extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "codeorganigramme", length = 20)
    private String codeorganigramme;

    @Column(name = "codeeffectif", length = 20)
    private String codeeffectif;

    @Column(name = "effectiprevisionnel")
    private Integer effectiprevisionnel;

    @Column(name = "dateprevision")
    private Date dateprevision;

    @Column(name = "libelle", length = 200)
    private String libelle;

}
