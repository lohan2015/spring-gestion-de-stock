package com.cmbassi.gestiondepaie.model;

import com.cmbassi.gestiondestock.model.AbstractEntity;
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
