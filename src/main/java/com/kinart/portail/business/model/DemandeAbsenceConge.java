package com.kinart.portail.business.model;

import com.kinart.portail.business.utils.EnumStatusType;
import com.kinart.stock.business.model.AbstractEntity;
import com.kinart.stock.business.model.Utilisateur;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

/** @author c.mbassi */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "DemandeAbsenceConge")
@Table(name = "demandeabsenceconge")
public class DemandeAbsenceConge  extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private Utilisateur userDemAbsCg;

    @Column(name = "dtedebut")
    private Date dteDebut;

    @Column(name = "dtefin")
    private Date dteFin;

    @Column(name = "motif", length = 200)
    private String motif;

    @Column(name = "raison", length = 1000)
    private String raison;

    @Column(name = "valid1", length = 100)
    private String valid1;

    @Column(name = "valid2", length = 100)
    private String valid2;

    @Column(name = "valid3", length = 100)
    private String valid3;

    @Column(name = "valid4", length = 100)
    private String valid4;

    @Column(name = "status1", length = 20)
    @Enumerated(EnumType.STRING)
    private EnumStatusType status1 = EnumStatusType.EATTENTE_VALIDATION;

    @Column(name = "status2", length = 20)
    @Enumerated(EnumType.STRING)
    private EnumStatusType status2 = EnumStatusType.NONE;

    @Column(name = "status3", length = 20)
    @Enumerated(EnumType.STRING)
    private EnumStatusType status3 = EnumStatusType.NONE;

    @Column(name = "status4", length = 20)
    @Enumerated(EnumType.STRING)
    private EnumStatusType status4 = EnumStatusType.NONE;

}
