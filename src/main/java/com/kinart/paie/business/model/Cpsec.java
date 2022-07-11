package com.kinart.paie.business.model;

import java.io.Serializable;
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
@Entity(name = "Cpsec")
@Table(name = "cpsec")
public class Cpsec extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "naxa", length = 20)
    private String naxa;

    @Column(name = "nsec", length = 20)
    private String nsec;

    @Column(name = "intl", length = 20)
    private String intl;

    @Column(name = "intr", length = 20)
    private String intr;

    @Column(name = "cnsa", length = 20)
    private String cnsa;

    @Column(name = "dato")
    private Date dato;

    @Column(name = "datf")
    private Date datf;

    @Column(name = "unit", length = 20)
    private String unit;

}
