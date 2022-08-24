package com.kinart.evaluation.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;


/**
 * cmbassi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "Evalnotedet")
@Table(name = "evalnotedet")
public class Evalnotedet extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "nmat", length = 10)
    private String nmat;

    @Column(name = "codeeval", length = 10)
    private String codeeval;

    @Column(name = "ttobjobjproduction")
     private BigDecimal ttobjobjproduction;

    @Column(name = "myobjobjproduction")
     private BigDecimal myobjobjproduction;

    @Column(name = "maxobjobjproduction")
     private BigDecimal maxobjobjproduction;

    @Column(name = "ttobjobjcontrole")
     private BigDecimal ttobjobjcontrole;

    @Column(name = "myobjobjcontrole")
     private BigDecimal myobjobjcontrole;

    @Column(name = "maxobjobjcontrole")
     private BigDecimal maxobjobjcontrole;

    @Column(name = "ttcomptechnique")
     private BigDecimal ttcomptechnique;

    @Column(name = "mycomptechnique")
     private BigDecimal mycomptechnique;

    @Column(name = "maxcomptechnique")
     private BigDecimal maxcomptechnique;

    @Column(name = "ttcompmanageriale")
     private BigDecimal ttcompmanageriale;

    @Column(name = "mycompmanageriale")
     private BigDecimal mycompmanageriale;

    @Column(name = "maxcompmanageriale")
     private BigDecimal maxcompmanageriale;

    @Column(name = "ttcomprespect")
     private BigDecimal ttcomprespect;

    @Column(name = "mycomprespect")
     private BigDecimal mycomprespect;

    @Column(name = "maxcomprespect")
     private BigDecimal maxcomprespect;

    @Column(name = "ttcompattitude")
     private BigDecimal ttcompattitude;

    @Column(name = "mycompattitude")
     private BigDecimal mycompattitude;

    @Column(name = "maxcompattitude")
     private BigDecimal maxcompattitude;

    @Column(name = "ttobjobjprodfa")
     private BigDecimal ttobjobjprodfa;

    @Column(name = "myobjobjprodfa")
     private BigDecimal myobjobjprodfa;

    @Column(name = "maxobjobjprodfa")
     private BigDecimal maxobjobjprodfa;

    @Column(name = "ttobjobjcontrfa")
     private BigDecimal ttobjobjcontrfa;

    @Column(name = "myobjobjcontrfa")
     private BigDecimal myobjobjcontrfa;

    @Column(name = "maxobjobjcontrfa")
     private BigDecimal maxobjobjcontrfa;

    @Column(name = "totalc")
     private BigDecimal totalc;

    @Column(name = "totald")
     private BigDecimal totald;

    @Column(name = "totalt")
     private BigDecimal totalt;

    @Column(name = "notefinalemp")
     private BigDecimal notefinalemp;

    @Column(name = "notefinalefa")
     private BigDecimal notefinalefa;

}
