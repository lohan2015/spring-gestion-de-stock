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
@Entity(name = "JournalPaie")
@Table(name = "journalPaie")
public class JournalPaie  extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "sessionid", length = 20)
    private String sessionid;

    @Column(name = "niv1", length = 10)
    private String niv1;

    @Column(name = "niv2", length = 10)
    private String niv2;

    @Column(name = "niv3", length = 10)
    private String niv3;

    @Column(name = "nmat", length = 10)
    private String nmat;

    @Column(name = "nume")
    private Integer nume;

    @Column(name = "col1", length = 20)
    private String col1;

    @Column(name = "col2", length = 20)
    private String col2;

    @Column(name = "col3", length = 20)
    private String col3;

    @Column(name = "col4", length = 20)
    private String col4;

    @Column(name = "col5", length = 20)
    private String col5;

    @Column(name = "col6", length = 20)
    private String col6;

    @Column(name = "col7", length = 20)
    private String col7;

    @Column(name = "col8", length = 20)
    private String col8;

    @Column(name = "col9", length = 20)
    private String col9;

}
