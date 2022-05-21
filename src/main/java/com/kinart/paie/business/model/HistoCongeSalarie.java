package com.kinart.paie.business.model;

import java.math.BigDecimal;
import java.util.Date;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "HistoCongeSalarie")
@Table(name = "histocongesalarie")
public class HistoCongeSalarie extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "aamm", length = 10)
    private String nmat;

    @Column(name = "ddcg")
    private Date ddcg;

    @Column(name = "dfcg")
    private Date dfcg;

    @Column(name = "nbja")
    private BigDecimal nbja;

    @Column(name = "nbjc")
    private BigDecimal nbjc;

    @Column(name = "cmcg", length = 10)
    private String cmcg;

    @Column(name = "mtcg")
    private BigDecimal mtcg;

    @Column(name = "typa", length = 10)
    private String typa;

    @Column(name = "cclo", length = 10)
    private String cclo;

    @Column(name = "dclo")
    private Date dclo;

    @Column(name = "deff")
    private Date deff;

    @Column(name = "cuticre", length = 20)
    private String cuticre;

    @Column(name = "cutimod", length = 20)
    private String cutimod;

    @Column(name = "typcg", length = 10)
    private String typcg;

    @Column(name = "ref", length = 20)
    private String ref;

}
