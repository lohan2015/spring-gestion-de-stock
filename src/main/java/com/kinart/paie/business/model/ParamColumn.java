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
@Entity
@Table(name = "paramcolumn")
public class ParamColumn extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "ctab")
    private Integer ctab;

    @Column(name = "ctyp", length = 10)
    private String ctyp;

    @Column(name = "nume")
    private Integer nume;

    @Column(name = "libe", length = 100)
    private String libe;

    @Column(name = "duti", length = 20)
    private String duti;

    @Column(name = "numeLien")
    private Integer numeLien;

    @Column(name = "ctabLien")
    private Integer ctabLien;

    @Column(name = "codeProfil", length = 10)
    private String codeProfil;

    @Column(name = "ctypLien", length = 10)
    private String ctypLien;

    @Column(name = "obligatoire", length = 1)
    private String obligatoire;
}
