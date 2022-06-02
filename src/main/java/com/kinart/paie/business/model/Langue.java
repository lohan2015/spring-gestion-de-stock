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
@Entity(name = "Langue")
@Table(name = "langue")
public class Langue extends AbstractEntity {

    @Column(name = "clang", length = 10)
    private String clang;

    @Column(name = "llang", length = 100)
    private String llang;
}
