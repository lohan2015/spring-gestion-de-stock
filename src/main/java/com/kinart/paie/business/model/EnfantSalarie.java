package com.kinart.paie.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "EnfantSalarie")
@Table(name = "enfantsalarie")
public class EnfantSalarie extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "nmat", length = 10)
    private String nmat;

    @Column(name = "nsequence")
    private Integer nsequence;

    @Column(name = "nucj")
    private Long nucj;

    @Column(name = "nom", length = 500)
    private String nom;

    @Column(name = "sexe", length = 10)
    private String sexe;

    @Column(name = "dtna")
    private Date dtna;

    @Column(name = "pnai", length = 10)
    private String pnai;

    @Column(name = "ddcd")
    private Date ddcd;

    @Column(name = "scol", length = 1)
    private String scol;

    @Column(name = "achg", length = 1)
    private String achg;

}
