package com.kinart.paie.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;


/** @author Hibernate CodeGenerator */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "Produit")
@Table(name = "produit")
public class Produit  extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "codpro", length = 10)
    private String codpro;

    @Column(name = "libpro", length = 200)
    private String libpro;

    @Column(name = "sens", length = 1)
    private String sens;

    @Column(name = "natcpt", length = 10)
    private String natcpt;

    @Column(name = "codcen", length = 10)
    private String codcen;

    @Column(name = "codran", length = 10)
    private String codran;

    @Column(name = "gesana", length = 10)
    private String gesana;

    @Column(name = "gestre", length = 10)
    private String gestre;

    @Column(name = "geslet", length = 10)
    private String geslet;

    @Column(name = "gesval", length = 10)
    private String gesval;

    @Column(name = "coduti", length = 50)
    private String coduti;

}
