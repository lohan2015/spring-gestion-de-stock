package com.kinart.paie.business.model;

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
@Entity
@Table(name = "calendrierpaie")
public class CalendrierPaie extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "jour")
    private Date jour;

    @Column(name = "jsem", length = 1)
    private String jsem;

    @Column(name = "ouvr", length = 1)
    private String ouvr;

    @Column(name = "fer", length = 1)
    private String fer;

    @Column(name = "nsem")
    private Long nsem;

    @Column(name = "anne")
    private Long anne;

    @Column(name = "trav", length = 1)
    private String trav;
}
