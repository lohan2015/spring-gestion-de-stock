package com.kinart.paie.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "ConjointSalarie")
@Table(name = "conjointsalarie")
public class ConjointSalarie extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "nmat", length = 10)
    private String nmat;

    @Column(name = "nucj")
    private Integer nucj;

    @Column(name = "nom", length = 100)
    private String nom;

    @Column(name = "njf", length = 100)
    private String njf;

    @Column(name = "pren", length = 100)
    private String pren;

    @Column(name = "adrc", length = 10)
    private String adrc;

    @Column(name = "dmar")
    private Date dmar;

    @Column(name = "ddiv")
    private Date ddiv;

    @Column(name = "ddcd")
    private Date ddcd;

    @Column(name = "nbef")
    private Long nbef;

    @Column(name = "prof", length = 100)
    private String prof;

    @Column(name = "empl", length = 100)
    private String empl;

    @Column(name = "adre", length = 100)
    private String adre;

    @ManyToOne
    @JoinColumn(name = "idsalarie")
    private Salarie salarie;

}
