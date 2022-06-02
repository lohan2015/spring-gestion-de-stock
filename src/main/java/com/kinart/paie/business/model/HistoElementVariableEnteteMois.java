package com.kinart.paie.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "HistoElementVariableEnteteMois")
@Table(name = "histoelementvariableentetemois")
public class HistoElementVariableEnteteMois extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "aamm", length = 6)
    private String aamm;

    @Column(name = "nmat", length = 10)
    private String nmat;

    @Column(name = "nbul")
    private Integer nbul;

    @Column(name = "ddpa")
    private Date ddpa;

    @Column(name = "dfpa")
    private Date dfpa;

    @Column(name = "bcmo", length = 10)
    private String bcmo;
}
