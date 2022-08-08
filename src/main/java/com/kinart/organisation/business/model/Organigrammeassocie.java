package com.kinart.organisation.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/** @author c.mbassi */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "Organigrammeassocie")
@Table(name = "organigrammeassocie")
public class Organigrammeassocie extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "codeorganigramme", length = 20)
    private String codeorganigramme;

    @Column(name = "codeexterne", length = 20)
    private String codeexterne;

    @Column(name = "nomexterne", length = 200)
    private String nomexterne;

    @Column(name = "libelleorganigramme", length = 200)
    private String libelleorganigramme;

}
