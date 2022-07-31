package com.kinart.organisation.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/** @author c.mbassi */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "Orgniveau")
@Table(name = "orgniveau")
public class Orgniveau extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "codeniveau", length = 10)
    private String codeniveau;

    @Column(name = "libelle", length = 100)
    private String libelle;

    @Column(name = "codecouleur", length = 10)
    private String codecouleur;

    @Column(name = "priseencomptecouleur", length = 1)
    private String priseencomptecouleur;

}
