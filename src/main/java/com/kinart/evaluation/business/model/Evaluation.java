package com.kinart.evaluation.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;


/**
 * cmbassi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "Evaluation")
@Table(name = "evaluation")
public class Evaluation  extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "codeeval", length = 10)
    private String codeeval;

    @Column(name = "nmat", length = 10)
    private String nmat;

    @Column(name = "libelleeval", length = 100)
     private String libelleeval;

    @Column(name = "datedebut")
     private Date datedebut;

    @Column(name = "datefin")
     private Date datefin;

    @Column(name = "dateeval")
     private Date dateeval;

    @Column(name = "notece", length = 10)
     private String notece;

    @Column(name = "notelettreevaluat", length = 10)
     private String notelettreevaluat;

    @Column(name = "notechifevaluat", length = 10)
     private String notechifevaluat;

    @Column(name = "notedg", length = 10)
     private String notedg;

    @Column(name = "numevaluat", length = 10)
     private String numevaluat;

    @Column(name = "codemodel", length = 10)
     private String codemodel;

    @Column(name = "evaluateur", length = 10)
     private String evaluateur;

    @Column(name = "etat", length = 10)
     private String etat;

    @Column(name = "statutworkflow", length = 10)
     private String statutworkflow;

    @Column(name = "statutsaper", length = 10)
     private String statutsaper;

}
