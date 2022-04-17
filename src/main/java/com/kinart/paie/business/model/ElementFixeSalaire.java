package com.kinart.paie.business.model;

import java.math.BigDecimal;
import java.util.Date;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "elementfixesalaire")
public class ElementFixeSalaire extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "nmat", length = 10)
    private String nmat;

    @Column(name = "codp", length = 10)
    private String codp;

    @Column(name = "monp")
    private BigDecimal monp;

    @Column(name = "ddeb")
    private Date ddeb;

    @Column(name = "dfin")
    private Date dfin;

    @ManyToOne
    @JoinColumn(name = "idsalarie")
    private Salarie salarie;
}
