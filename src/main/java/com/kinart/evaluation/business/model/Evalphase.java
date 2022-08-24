package com.kinart.evaluation.business.model;

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
@Entity(name = "Evalphase")
@Table(name = "evalphase")
public class Evalphase  extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "code", length = 10)
    private String code;

    @Column(name = "libelle", length = 500)
    private String libelle;

    @Column(name = "butilise", length = 10)
    private String butilise;

    @Column(name = "bvisa", length = 10)
    private String bvisa;
}
