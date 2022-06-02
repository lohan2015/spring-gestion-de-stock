package com.kinart.paie.business.model;

import java.io.Serializable;
import java.util.Date;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/** @author Hibernate CodeGenerator */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "CpAbr")
@Table(name = "cpabr")
public class CpAbr extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "codabr", length = 10)
    private String codabr;

    @Column(name = "libabr", length = 200)
    private String libabr;

    @Column(name = "coduti", length = 50)
    private String coduti;

}
