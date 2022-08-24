package com.kinart.evaluation.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/** @author cmbassi */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "Evalmodeletapecoment")
@Table(name = "evalmodeletapecoment")
public class Evalmodeletapecoment extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "code", length = 10)
    private String code;

    @Column(name = "codephase", length = 10)
    private String codephase;

    @Column(name = "codeetape", length = 10)
    private String codeetape;

    @Column(name = "codemodel", length = 10)
    private String codemodel;

    @Column(name = "libelle", length = 100)
    private String libelle;

    @Column(name = "ordre", length = 10)
    private String ordre;

}
