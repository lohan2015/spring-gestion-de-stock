package com.kinart.paie.business.model;

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
@Entity(name = "ParamTable")
@Table(name = "paramtable")
public class ParamTable extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "ctab")
    private Integer ctab;

    @Column(name = "libe", length = 100)
    private String libe;

    @Column(name = "nccl")
    private int nccl;

    @Column(name = "typc", length = 10)
    private String typc;

    @Column(name = "duti", length = 20)
    private String duti;

    @Column(name = "profil", length = 10)
    private String profil;
}
