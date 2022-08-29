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
@Entity(name = "Evalchampagent")
@Table(name = "evalchampagent")
public class Evalchampagent extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "codechamp", length = 10)
    private String codechamp;

    @Column(name = "nmat", length = 10)
    private String nmat;

}
