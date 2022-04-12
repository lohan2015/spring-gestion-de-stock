package com.cmbassi.gestiondepaie.model;

import com.cmbassi.gestiondestock.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "logelementsalaire")
public class LogElementSalaire extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "cuti", length = 20)
    private String cuti;

    @Column(name = "oper", length = 100)
    private String oper;

    @Column(name = "ligne")
    private String ligne;

}
