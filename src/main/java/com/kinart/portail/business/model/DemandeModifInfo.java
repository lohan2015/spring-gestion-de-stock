package com.kinart.portail.business.model;

import com.kinart.portail.business.utils.EnumStatusType;
import com.kinart.stock.business.model.AbstractEntity;
import com.kinart.stock.business.model.Utilisateur;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

/** @author c.mbassi */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "DemandeModifInfo")
@Table(name = "demandemodifinfo")
public class DemandeModifInfo  extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private Utilisateur userDemModInfo;

    @Column(name = "champconcerne", length = 200)
    private String champConcerne;

    @Column(name = "valeursouhaitee", length = 1000)
    private String valeurSouhaitee;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "file_size")
    private Long fileSize;

    @Lob
    @Column(name = "file_data")
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] fileData;

    @Column(name = "valid", length = 100)
    private String valid;

    @Column(name = "status", length = 20)
    @Enumerated(EnumType.STRING)
    private EnumStatusType status = EnumStatusType.ATTENTE_VALIDATION;

    @Column(name = "comment_user", length = 1000)
    private String commentUser;

    @Column(name = "comment_drhl", length = 1000)
    private String commentDrhl;

    @Column(name = "est_enf", length = 1)
    private String estEnfant;

    @Column(name = "est_conf", length = 1)
    private String estConjoint;

    @Column(name = "nom", length = 100)
    private String nom;

    @Column(name = "njf", length = 100)
    private String njf;

    @Column(name = "pren", length = 100)
    private String pren;

    @Column(name = "prof", length = 100)
    private String prof;

    @Column(name = "empl", length = 100)
    private String empl;

    @Column(name = "adrc", length = 100)
    private String adrc;

    @Column(name = "sexe", length = 10)
    private String sexe;

    @Column(name = "pays", length = 10)
    private String pays;

    @Column(name = "achg", length = 1)
    private String achg;

    @Column(name = "scol", length = 1)
    private String scol;

    @Column(name = "dte_evt")
    private Date dteEvt;
}
