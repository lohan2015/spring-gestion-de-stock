package com.kinart.paie.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "SuspensionPaie")
@Table(name = "suspensionpaie")
public class SuspensionPaie extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "nmat", length = 10)
    private String nmat;

    @Column(name = "ddar")
    private Date ddar;

    @Column(name = "dfar")
    private Date dfar;

    @Column(name = "mtar", length = 10)
    private String mtar;
}
