package com.kinart.evaluation.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/** @author c.mbassi */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "Evalanal")
@Table(name = "evalanal")
public class Evalanal  extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "nmat", length = 10)
    private String nmat;

    @Column(name = "codeeval", length = 10)
    private String codeeval;

    @Column(name = "bprom", length = 1)
    private String bprom;

    @Column(name = "bmut", length = 1)
    private String bmut;

    @Column(name = "bform", length = 1)
    private String bform;

    @Column(name = "baffec", length = 1)
    private String baffec;

    @Column(name = "breconv", length = 1)
    private String breconv;

    @Column(name = "bautr", length = 1)
    private String bautr;

    @Column(name = "cprom", length = 200)
    private String cprom;

    @Column(name = "cmut", length = 200)
    private String cmut;

    @Column(name = "cform", length = 200)
    private String cform;

    @Column(name = "caffec", length = 200)
    private String caffec;

    @Column(name = "creconv", length = 200)
    private String creconv;

    @Column(name = "cautr", length = 200)
    private String cautr;

    @Column(name = "cobs", length = 200)
    private String cobs;

    @Column(name = "bav1", length = 10)
    private String bav1;

    @Column(name = "bav2", length = 1)
    private String bav2;

    @Column(name = "bav3", length = 1)
    private String bav3;

    @Column(name = "bform1", length = 1)
    private String bform1;

    @Column(name = "bform2", length = 1)
    private String bform2;

    @Column(name = "cobs1", length = 200)
    private String cobs1;

    @Column(name = "cobs2", length = 200)
    private String cobs2;

    @Column(name = "cav1", length = 200)
    private String cav1;

    @Column(name = "cav2", length = 200)
    private String cav2;

    @Column(name = "cform1", length = 200)
    private String cform1;

    @Column(name = "cform2", length = 200)
    private String cform2;

    @Column(name = "cav3", length = 200)
    private String cav3;

    @Column(name = "bform3", length = 1)
    private String bform3;

    @Column(name = "cform3", length = 200)
    private String cform3;

    @Column(name = "bavdrh", length = 1)
    private String bavdrh;

    @Column(name = "bavdg", length = 1)
    private String bavdg;

    @Column(name = "cavdrh", length = 200)
    private String cavdrh;

    @Column(name = "cavdg", length = 200)
    private String cavdg;

    @Column(name = "cobs3", length = 200)
    private String cobs3;

}
