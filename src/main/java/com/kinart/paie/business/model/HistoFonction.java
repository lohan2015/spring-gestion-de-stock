package com.kinart.paie.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/** @author Hibernate CodeGenerator */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "HistoFonction")
@Table(name = "histofonction")
public class HistoFonction  extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "nmat", length = 10)
    private String nmat;

    @Column(name = "dchg")
    private Date dchg;

    @Column(name = "niv1", length = 10)
    private String niv1;

    @Column(name = "niv2", length = 10)
    private String niv2;

    @Column(name = "niv3", length = 10)
    private String niv3;

    @Column(name = "fonc", length = 10)
    private String fonc;

    @Column(name = "dtfin")
    private Date dtfin;
}
