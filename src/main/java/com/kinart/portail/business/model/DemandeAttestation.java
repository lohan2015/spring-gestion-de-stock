package com.kinart.portail.business.model;

import com.kinart.portail.business.utils.EnumStatusType;
import com.kinart.stock.business.model.AbstractEntity;
import com.kinart.stock.business.model.Utilisateur;
import lombok.*;

import javax.persistence.*;

/** @author c.mbassi */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "DemandeAttestation")
@Table(name = "demandeattestation")
public class DemandeAttestation extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private Utilisateur userDemAttest;

    @Column(name = "typedoc", length = 200)
    private String typeDoc;

    @Column(name = "scepersonnel", length = 100)
    private String scePersonnel;

    @Column(name = "status", length = 20)
    @Enumerated(EnumType.STRING)
    private EnumStatusType status = EnumStatusType.EATTENTE_VALIDATION;
}
