package com.kinart.portail.business.model;

import com.kinart.portail.business.utils.EnumStatusType;
import com.kinart.stock.business.model.AbstractEntity;
import com.kinart.stock.business.model.Utilisateur;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/** @author c.mbassi */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "DemandeHabilitation")
@Table(name = "demandehabilitation")
public class DemandeHabilitation   extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private Utilisateur userDemHabil;

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
}
