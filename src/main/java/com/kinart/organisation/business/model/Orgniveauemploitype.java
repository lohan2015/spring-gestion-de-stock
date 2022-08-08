package com.kinart.organisation.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;


/** @author Hibernate CodeGenerator */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "Orgniveauemploitype")
@Table(name = "orgniveauemploitype")
public class Orgniveauemploitype extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "codeniveau", length = 10)
    private String codeniveau;

    @Column(name = "codeemploitype", length = 10)
    private String codeemploitype;
}
