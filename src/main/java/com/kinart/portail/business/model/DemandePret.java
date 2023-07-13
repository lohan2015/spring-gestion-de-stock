package com.kinart.portail.business.model;

import com.kinart.portail.business.utils.EnumStatusType;
import com.kinart.stock.business.model.AbstractEntity;
import com.kinart.stock.business.model.Utilisateur;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

/** @author c.mbassi */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "DemandePret")
@Table(name = "demandepret")
public class DemandePret  extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private Utilisateur userDemPret;

    @Column(name = "typepret", length = 10)
    private String typePret;

    @Column(name = "montantpret")
    private BigDecimal montantPret;

    @Column(name = "dureepret")
    private Integer dureePret;

    @Column(name = "scepersonnel", length = 100)
    private String scePersonnel;

    @Column(name = "drhl", length = 100)
    private String drhl;

    @Column(name = "dga", length = 100)
    private String dga;

    @Column(name = "dg", length = 100)
    private String dg;

    @Column(name = "status1", length = 20)
    private EnumStatusType status1;

    @Column(name = "status2", length = 20)
    private EnumStatusType status2;

    @Column(name = "status3", length = 20)
    private EnumStatusType status3;

    @Column(name = "status4", length = 20)
    private EnumStatusType status4;

}
