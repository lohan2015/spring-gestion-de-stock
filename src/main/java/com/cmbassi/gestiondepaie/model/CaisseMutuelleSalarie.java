package com.cmbassi.gestiondepaie.model;

import java.io.Serializable;
import java.util.Date;

import com.cmbassi.gestiondestock.model.AbstractEntity;
import lombok.*;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "caissemutuellesalarie")
public class CaisseMutuelleSalarie extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "nmat", length = 10)
    private String nmat;

    @Column(name = "rscm", length = 10)
    private String rscm;

    @Column(name = "rpcm", length = 10)
    private String rpcm;

    @Column(name = "nadh", length = 15)
    private String nadh;

    @Column(name = "dtad")
    private Date dtad;

    @Column(name = "dtrd")
    private Date dtrd;

    @ManyToOne
    @JoinColumn(name = "idsalairie")
    private Salarie salarie;
}
