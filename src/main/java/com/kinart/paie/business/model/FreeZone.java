package com.kinart.paie.business.model;

import java.math.BigDecimal;
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
@Entity(name = "FreeZone")
@Table(name = "freezone")
public class FreeZone extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "nmat", length = 10)
    private String nmat;

    @Column(name = "numerozl")
    private Integer numerozl;

    @Column(name = "vallzl", length = 200)
    private String vallzl;

    @Column(name = "valmzl")
    private Long valmzl;

    @Column(name = "valtzl")
    private BigDecimal valtzl;

    @Column(name = "valdzl")
    private Date valdzl;

}
