package com.kinart.paie.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


/** @author Hibernate CodeGenerator */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "Journal")
@Table(name = "journal")
public class Journal extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "codjou", length = 10)
    private String codjou;

    @Column(name = "libjou", length = 200)
    private String libjou;

    @Column(name = "etat", length = 10)
    private String etat;

    @Column(name = "typjou", length = 10)
    private String typjou;

    @Column(name = "natjou", length = 20)
    private String natjou;

    @Column(name = "edicen", length = 20)
    private String edicen;

    @Column(name = "debsai")
    private Date debsai;

    @Column(name = "finsai")
    private Date finsai;

    @Column(name = "gesnum", length = 20)
    private String gesnum;

    @Column(name = "typRegfac", length = 20)
    private String typRegfac;

    @Column(name = "ctpauto", length = 20)
    private String ctpauto;

    @Column(name = "ctpjou", length = 20)
    private String ctpjou;

    @Column(name = "coduti", length = 50)
    private String coduti;

    @Column(name = "codran")
    private Integer codran;

}
