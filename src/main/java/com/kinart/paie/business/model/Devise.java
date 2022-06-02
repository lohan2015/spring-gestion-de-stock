package com.kinart.paie.business.model;

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
@Entity(name = "Devise")
@Table(name = "devise")
public class Devise extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "cdev", length = 10)
    private String cdev;

    @Column(name = "ldev", length = 50)
    private String ldev;

    @Column(name = "ndcm", length = 20)
    private String ndcm;

}
