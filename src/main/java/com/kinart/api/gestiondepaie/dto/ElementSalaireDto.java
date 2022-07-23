package com.kinart.api.gestiondepaie.dto;

import com.kinart.paie.business.model.ElementSalaire;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ElementSalaireDto {
    private Integer id;
    private Integer idEntreprise;

    private String crub;

    private String lrub;

    private String calc;

    private String pror;

    private String ppcg;

    private String prac;

    private String prhr;

    private String prtb;

    private String prcl;

    private String prtm;

    private Long prno;

    private String ppas;

    private Long moi1;

    private Long moi2;

    private Long moi3;

    private Long moi4;

    private long bul1;

    private long bul2;

    private long bul3;

    private long bul4;

    private String apcf;

    private String cabf;

    private long prbul;

    private String cbulf;

    private String ednul;

    private String edcum;

    private String edbbu;

    private Long epbul;

    private String ajus;

    private Long ajnu;

    private String snet;

    private String ecar;

    private String typr;

    private String esat;

    private String rreg;

    private String rman;

    private String perc;

    private String freq;

    private String addf;

    private String rcon;

    private String eddf;

    private String basc;

    private String trtc;

    private String trve;

    private String exo;

    private String val1;

    private String val2;

    private String val3;

    private String mopa;

    private String lbtm;

    private String opfi;

    private long algo;

    private String cle1;

    private String cle2;

    private String tabl;

    private String toum;

    private Long nutm;

    private String arro;

    private String resl;

    private String sup1;

    private String sups;

    private String sup2;

    private String inf1;

    private String infs;

    private String inf2;

    private String egu1;

    private String egus;

    private String egu2;

    private String cs1;

    private String cs2;

    private String cs3;

    private String sexe;

    private Long age1;

    private Long age2;

    private String sit1;

    private String sit2;

    private String sit3;

    private String sit4;

    private Long nbe1;

    private Long nbe2;

    private String nat1;

    private String nat2;

    private String zca1;

    private String zca2;

    private String zca3;

    private String zca4;

    private String cat1;

    private String cat2;

    private String tyc1;

    private String tyc2;

    private String tyc3;

    private String tyc4;

    private String tyc5;

    private String tyc6;

    private String tyc7;

    private String tyc8;

    private String gra1;

    private String gra2;

    private String gra3;

    private String gra4;

    private String gra5;

    private String gra6;

    private String gra7;

    private String gra8;

    private String avn;

    private String niv11;

    private String niv12;

    private String niv13;

    private String niv14;

    private String niv21;

    private String niv22;

    private String niv23;

    private String niv24;

    private String niv31;

    private String niv32;

    private String niv33;

    private String niv34;

    private String synd;

    private String reg1;

    private String reg2;

    private String reg3;

    private String reg4;

    private String reg5;

    private String reg6;

    private String reg7;

    private String reg8;

    private String clas1;

    private String clas2;

    private String clas3;

    private String clas4;

    private String cfon;

    private String hif1;

    private String hif2;

    private String hif3;

    private String hif4;

    private String fon1;

    private String fon2;

    private String fon3;

    private String fon4;

    private String fon5;

    private String fon6;

    private String fon7;

    private String fon8;

    private String zl11;

    private String zl12;

    private String zl21;

    private String zl22;

    private String cais;

    private String dnbp;

    private String txmt;

    private String trcu;

    private String basp;

    private String abat;

    private String abmx;

    private String pmin;

    private String pmax;

    private BigDecimal pcab;

    private String pdap;

    private String comp;

    private String cper;

    private String de01;

    private String de02;

    private String de03;

    private String de04;

    private String de05;

    private String de06;

    private String de07;

    private String de08;

    private String de09;

    private String de10;

    private String de11;

    private String de12;

    private String de13;

    private String de14;

    private String de15;

    private String de16;

    private String de17;

    private String de18;

    private String de19;

    private String de20;

    private String cr01;

    private String cr02;

    private String cr03;

    private String cr04;

    private String cr05;

    private String fbas;

    private String tbas;

    private String note;

    private String formule;

    private String val31;
    private String val32;
    private String val33;

    @JsonIgnore
    private List<ElementSalaireBaseDto> elementSalaireBase;

    @JsonIgnore
    private List<ElementSalaireBaremeDto> elementSalaireBareme;

    public static ElementSalaireDto fromEntity(ElementSalaire eltSalaire) {
        if (eltSalaire == null) {
            return null;
        }

        ElementSalaireDto dto = new  ElementSalaireDto();
        BeanUtils.copyProperties(eltSalaire, dto);
        return dto;
    }

    public static ElementSalaire toEntity(ElementSalaireDto dto) {
        if (dto == null) {
            return null;
        }
        ElementSalaire eltSal = new ElementSalaire();
        BeanUtils.copyProperties(dto, eltSal, "elementSalaireBase", "elementSalaireBareme");
        return eltSal;
    }

}
