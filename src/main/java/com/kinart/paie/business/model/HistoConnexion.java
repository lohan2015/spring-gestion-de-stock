package com.kinart.paie.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "HistoConnexion")
@Table(name = "histoconnexion")
public class HistoConnexion extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "cuti", length = 20)
    private String cuti;

    @Column(name = "datc")
    private Date datc;

    @Column(name = "typeop", length = 20)
    private String typeop;

    @Column(name = "ligne", length = 2000)
    private String ligne;

}
