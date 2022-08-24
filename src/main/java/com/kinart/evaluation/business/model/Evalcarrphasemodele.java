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
@Entity(name = "Evalcarrphasemodele")
@Table(name = "evalcarrphasemodele")
public class Evalcarrphasemodele extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "numerophase", length = 10)
    private String numerophase;

    @Column(name = "codemodele", length = 10)
    private String codemodele;

    @Column(name = "libelle", length = 100)
    private String libelle;

}
