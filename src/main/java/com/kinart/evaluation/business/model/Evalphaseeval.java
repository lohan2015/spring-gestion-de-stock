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
@Entity(name = "Evalphaseeval")
@Table(name = "evalphaseeval")
public class Evalphaseeval extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "codephase", length = 10)
    private String codephase;

    @Column(name = "codeeval", length = 10)
    private String codeeval;

    @Column(name = "nmat", length = 10)
    private String nmat;

    @Column(name = "codeparam", length = 10)
    private String codeparam;

}
