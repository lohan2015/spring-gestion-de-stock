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
@Entity(name = "LogElementSalaire")
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
