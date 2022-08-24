package com.kinart.evaluation.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;import java.math.BigDecimal;


/** @author cmbassi */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "Evalcritnote")
@Table(name = "evalcritnote")
public class Evalcritnote extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "nmat", length = 10)
    private String nmat;

    @Column(name = "codeeval", length = 10)
    private String codeeval;

    @Column(name = "codecrit", length = 10)
    private String codecrit;

    @Column(name = "notechf")
    private BigDecimal notechf;

    @Column(name = "notelet", length = 10)
    private String notelet;

    @Column(name = "comm", length = 200)
    private String comm;

    @Column(name = "notegl")
    private BigDecimal notegl;

    @Column(name = "notece")
    private BigDecimal notece;

    @Column(name = "poids")
    private BigDecimal poids;
    
    @Column(name = "notechfcol")
    private BigDecimal notechfcol;

    @Column(name = "noteletcol", length = 10)
    private String noteletcol;

    @Column(name = "notechfevl1")
    private BigDecimal notechfevl1;

    @Column(name = "noteletevl1", length = 10)
    private String noteletevl1;

    @Column(name = "notechfevl2")
    private BigDecimal notechfevl2;

    @Column(name = "noteletevl2", length = 10)
    private String noteletevl2;

    @Column(name = "descr", length = 200)
    private String descr;

}
