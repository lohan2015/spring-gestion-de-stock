package com.kinart.paie.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "ParamData")
@Table(name = "paramdata")
public class ParamData extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "ctab")
    private Integer ctab;

    @Column(name = "cacc", length = 20)
    private String cacc;

    @Column(name = "nume")
    private Integer nume;

    @Column(name = "vall", length = 2000)
    private String vall;

    @Column(name = "valm")
    private Long valm;

    @Column(name = "valt")
    private BigDecimal valt;

    @Column(name = "duti", length = 20)
    private String duti;

    @Column(name = "vald")
    private Date vald;
}
