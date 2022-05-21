package com.kinart.paie.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "DistinctionSalarie")
@Table(name = "distinctionsalarie")
public class DistinctionSalarie extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "nmat", length = 10)
    private String nmat;

    @Column(name = "nlig")
    private Integer nlig;

    @Column(name = "ddis")
    private Date ddis;

    @Column(name = "cdis", length = 10)
    private String cdis;

    @Column(name = "montant")
    private BigDecimal montant;

    @Column(name = "etatpaiement", length = 1)
    private String etatpaiement;

    @Column(name = "datereglement")
    private Date datereglement;

    @Column(name = "cuticre", length = 50)
    private String cuticre;

    @Column(name = "cutimod", length = 1)
    private String cutimod;

}
