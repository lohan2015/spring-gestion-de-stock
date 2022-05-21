package com.kinart.paie.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "Message")
@Table(name = "message")
public class Message extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "cdmes", length = 10)
    private String cdmes;

    @Column(name = "clang", length = 5)
    private String clang;

    @Column(name = "lbmes", length = 200)
    private String lbmes;
}
