package com.kinart.evaluation.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/** @author cmbassi */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "Evaletapeeval")
@Table(name = "evaletapeeval")
public class Evaletapeeval extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "codephase", length = 10)
    private String codephase;

    @Column(name = "codeetape", length = 10)
    private String codeetape;

    @Column(name = "nmat", length = 10)
    private String nmat;

    @Column(name = "codeeval", length = 10)
    private String codeeval;

    @Column(name = "codeparam", length = 10)
    private String codeparam;

    @Column(name = "notece")
    private BigDecimal notece;

    @Column(name = "notelettreevaluat", length = 10)
    private String notelettreevaluat;

    @Column(name = "notechifevaluat")
    private BigDecimal notechifevaluat;

    @Column(name = "justificsn", length = 100)
    private String justificsn;

}
