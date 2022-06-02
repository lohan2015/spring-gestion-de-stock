package com.kinart.paie.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "HistoElementFixeSalaire")
@Table(name = "histoelementfixesalaire")
public class HistoElementFixeSalaire extends AbstractEntity {

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

    @Column(name = "aamm", length = 6)
    private String aamm;

    @Column(name = "nbul")
    private Integer nbul;
}
