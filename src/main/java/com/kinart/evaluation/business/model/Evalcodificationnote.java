package com.kinart.evaluation.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/** @author cmbassi */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "Evalcodificationnote")
@Table(name = "evalcodificationnote")
public class Evalcodificationnote extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "code", length = 10)
    private String code;

    @Column(name = "libelle", length = 100)
    private String libelle;

    @Column(name = "intervalle", length = 10)
    private String intervalle;

    @Column(name = "abbreviation", length = 10)
    private String abbreviation;

    @Column(name = "information", length = 10)
    private String information;

    @Column(name = "bjustifier", length = 1)
    private String bjustifier;

    @Column(name = "raison", length = 100)
    private String raison;

    @Column(name = "min", length = 10)
    private String min;

    @Column(name = "max", length = 10)
    private String max;

}
