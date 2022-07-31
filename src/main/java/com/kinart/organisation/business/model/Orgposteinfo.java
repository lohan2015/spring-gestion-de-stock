package com.kinart.organisation.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;


/** 
 *  Compétences Métier ou Postes
 *     
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "Orgposteinfo")
@Table(name = "orgposteinfo")
public class Orgposteinfo extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "codeposte", length = 10)
    private String codeposte;

    @Column(name = "typeinfo", length = 20)
    private String typeinfo;

    @Column(name = "codeinfo1", length = 10)
    private String codeinfo1;

    @Column(name = "codeinfo2", length = 10)
    private String codeinfo2;

    @Column(name = "codeinfo3", length = 10)
    private String codeinfo3;

    @Column(name = "valminfo1")
    private BigDecimal valminfo1;

    @Column(name = "coeff", length = 10)
    private String coeff;

}
