package com.kinart.paie.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "HistoElementSalaire")
@Table(name = "histoelementsalaire")
public class HistoElementSalaire extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "crub", length = 10)
    private String crub;

    @Column(name = "lrub", length = 200)
    private String lrub;

    @Column(name = "calc", length = 1)
    private String calc;

    @Column(name = "pror", length = 1)
    private String pror;

    @Column(name = "ppcg", length = 10)
    private String ppcg;

    @Column(name = "prac", length = 10)
    private String prac;

    @Column(name = "prhr", length = 10)
    private String prhr;

    @Column(name = "prtb", length = 10)
    private String prtb;

    @Column(name = "prcl", length = 10)
    private String prcl;

    @Column(name = "prtm", length = 10)
    private String prtm;

    @Column(name = "prno")
    private Long prno;

    @Column(name = "ppas", length = 10)
    private String ppas;

    @Column(name = "moi1")
    private Long moi1;

    @Column(name = "moi2")
    private Long moi2;

    @Column(name = "moi3")
    private Long moi3;

    @Column(name = "moi4")
    private Long moi4;

    @Column(name = "bul1")
    private long bul1;

    @Column(name = "bul2")
    private long bul2;

    @Column(name = "bul3")
    private long bul3;

    @Column(name = "bul4")
    private long bul4;

    @Column(name = "apcf", length = 10)
    private String apcf;

    @Column(name = "cabf", length = 10)
    private String cabf;

    @Column(name = "prbul")
    private long prbul;

    @Column(name = "cbulf", length = 10)
    private String cbulf;

    @Column(name = "ednul", length = 10)
    private String ednul;

    @Column(name = "edcum", length = 10)
    private String edcum;

    @Column(name = "edbbu", length = 10)
    private String edbbu;

    @Column(name = "epbul")
    private Long epbul;

    @Column(name = "ajus", length = 10)
    private String ajus;

    @Column(name = "ajnu")
    private Long ajnu;

    @Column(name = "snet", length = 10)
    private String snet;

    @Column(name = "ecar", length = 10)
    private String ecar;

    @Column(name = "typr", length = 10)
    private String typr;

    @Column(name = "esat", length = 10)
    private String esat;

    @Column(name = "rreg", length = 10)
    private String rreg;

    @Column(name = "rman", length = 10)
    private String rman;

    @Column(name = "perc", length = 10)
    private String perc;

    @Column(name = "freq", length = 10)
    private String freq;

    @Column(name = "addf", length = 10)
    private String addf;

    @Column(name = "rcon", length = 10)
    private String rcon;

    @Column(name = "eddf", length = 10)
    private String eddf;

    @Column(name = "basc", length = 10)
    private String basc;

    @Column(name = "trtc", length = 10)
    private String trtc;

    @Column(name = "trve")
    private String trve;

    @Column(name = "exo", length = 10)
    private String exo;

    @Column(name = "val1", length = 10)
    private String val1;

    @Column(name = "val2", length = 10)
    private String val2;

    @Column(name = "val3", length = 10)
    private String val3;

    @Column(name = "mopa", length = 10)
    private String mopa;

    @Column(name = "lbtm", length = 10)
    private String lbtm;

    @Column(name = "opfi", length = 10)
    private String opfi;

    @Column(name = "algo", length = 10)
    private long algo;

    @Column(name = "cle1", length = 10)
    private String cle1;

    @Column(name = "cle2", length = 10)
    private String cle2;

    @Column(name = "tabl", length = 10)
    private String tabl;

    @Column(name = "toum", length = 10)
    private String toum;

    @Column(name = "nutm")
    private Long nutm;

    @Column(name = "arro", length = 10)
    private String arro;

    @Column(name = "resl", length = 10)
    private String resl;

    @Column(name = "sup1", length = 10)
    private String sup1;

    @Column(name = "sups", length = 10)
    private String sups;

    @Column(name = "sup2", length = 10)
    private String sup2;

    @Column(name = "inf1", length = 10)
    private String inf1;

    @Column(name = "infs", length = 10)
    private String infs;

    @Column(name = "inf2", length = 10)
    private String inf2;

    @Column(name = "egu1", length = 10)
    private String egu1;

    @Column(name = "egus", length = 10)
    private String egus;

    @Column(name = "egu2", length = 10)
    private String egu2;

    @Column(name = "cs1", length = 10)
    private String cs1;

    @Column(name = "cs2", length = 10)
    private String cs2;

    @Column(name = "cs3", length = 10)
    private String cs3;

    @Column(name = "sexe", length = 10)
    private String sexe;

    @Column(name = "age1")
    private Long age1;

    @Column(name = "age2")
    private Long age2;

    @Column(name = "sit1", length = 10)
    private String sit1;

    @Column(name = "sit2", length = 10)
    private String sit2;

    @Column(name = "sit3", length = 10)
    private String sit3;

    @Column(name = "sit4", length = 10)
    private String sit4;

    @Column(name = "nbe1")
    private Long nbe1;

    @Column(name = "nbe2")
    private Long nbe2;

    @Column(name = "nat1", length = 10)
    private String nat1;

    @Column(name = "nat2", length = 10)
    private String nat2;

    @Column(name = "zca1", length = 10)
    private String zca1;

    @Column(name = "zca2", length = 10)
    private String zca2;

    @Column(name = "zca3", length = 10)
    private String zca3;

    @Column(name = "zca4", length = 10)
    private String zca4;

    @Column(name = "cat1", length = 10)
    private String cat1;

    @Column(name = "cat2", length = 10)
    private String cat2;

    @Column(name = "tyc1", length = 10)
    private String tyc1;

    @Column(name = "tyc2", length = 10)
    private String tyc2;

    @Column(name = "tyc3", length = 10)
    private String tyc3;

    @Column(name = "tyc4", length = 10)
    private String tyc4;

    @Column(name = "tyc5", length = 10)
    private String tyc5;

    @Column(name = "tyc6", length = 10)
    private String tyc6;

    @Column(name = "tyc7", length = 10)
    private String tyc7;

    @Column(name = "tyc8", length = 10)
    private String tyc8;

    @Column(name = "gra1", length = 10)
    private String gra1;

    @Column(name = "gra2", length = 10)
    private String gra2;

    @Column(name = "gra3", length = 10)
    private String gra3;

    @Column(name = "gra4", length = 10)
    private String gra4;

    @Column(name = "gra5", length = 10)
    private String gra5;

    @Column(name = "gra6", length = 10)
    private String gra6;

    @Column(name = "gra7", length = 10)
    private String gra7;

    @Column(name = "gra8", length = 10)
    private String gra8;

    @Column(name = "avn", length = 10)
    private String avn;

    @Column(name = "niv11", length = 10)
    private String niv11;

    @Column(name = "niv12", length = 10)
    private String niv12;

    @Column(name = "niv13", length = 10)
    private String niv13;

    @Column(name = "niv14", length = 10)
    private String niv14;

    @Column(name = "niv21", length = 10)
    private String niv21;

    @Column(name = "niv22", length = 10)
    private String niv22;

    @Column(name = "niv23", length = 10)
    private String niv23;

    @Column(name = "niv24", length = 10)
    private String niv24;

    @Column(name = "niv31", length = 10)
    private String niv31;

    @Column(name = "niv32", length = 10)
    private String niv32;

    @Column(name = "niv33", length = 10)
    private String niv33;

    @Column(name = "niv34", length = 10)
    private String niv34;

    @Column(name = "synd", length = 10)
    private String synd;

    @Column(name = "reg1", length = 10)
    private String reg1;

    @Column(name = "reg2", length = 10)
    private String reg2;

    @Column(name = "reg3", length = 10)
    private String reg3;

    @Column(name = "reg4", length = 10)
    private String reg4;

    @Column(name = "reg5", length = 10)
    private String reg5;

    @Column(name = "reg6", length = 10)
    private String reg6;

    @Column(name = "reg7", length = 10)
    private String reg7;

    @Column(name = "reg8", length = 10)
    private String reg8;

    @Column(name = "clas1", length = 10)
    private String clas1;

    @Column(name = "clas2", length = 10)
    private String clas2;

    @Column(name = "clas3", length = 10)
    private String clas3;

    @Column(name = "clas4", length = 10)
    private String clas4;

    @Column(name = "cfon", length = 10)
    private String cfon;

    @Column(name = "hif1", length = 10)
    private String hif1;

    @Column(name = "hif2", length = 10)
    private String hif2;

    @Column(name = "hif3", length = 10)
    private String hif3;

    @Column(name = "hif4", length = 10)
    private String hif4;

    @Column(name = "fon1", length = 10)
    private String fon1;

    @Column(name = "fon2", length = 10)
    private String fon2;

    @Column(name = "fon3", length = 10)
    private String fon3;

    @Column(name = "fon4", length = 10)
    private String fon4;

    @Column(name = "fon5", length = 10)
    private String fon5;

    @Column(name = "fon6", length = 10)
    private String fon6;

    @Column(name = "fon7", length = 10)
    private String fon7;

    @Column(name = "fon8", length = 10)
    private String fon8;

    @Column(name = "zl11", length = 10)
    private String zl11;

    @Column(name = "zl12", length = 10)
    private String zl12;

    @Column(name = "zl21", length = 10)
    private String zl21;

    @Column(name = "zl22", length = 10)
    private String zl22;

    @Column(name = "cais", length = 10)
    private String cais;

    @Column(name = "dnbp", length = 10)
    private String dnbp;

    @Column(name = "txmt", length = 10)
    private String txmt;

    @Column(name = "trcu", length = 10)
    private String trcu;

    @Column(name = "basp", length = 10)
    private String basp;

    @Column(name = "abat", length = 10)
    private String abat;

    @Column(name = "abmx", length = 10)
    private String abmx;

    @Column(name = "pmin", length = 10)
    private String pmin;

    @Column(name = "pmax", length = 10)
    private String pmax;

    @Column(name = "pcab")
    private BigDecimal pcab;

    @Column(name = "pdap", length = 10)
    private String pdap;

    @Column(name = "comp", length = 10)
    private String comp;

    @Column(name = "cper", length = 10)
    private String cper;

    @Column(name = "de01", length = 15)
    private String de01;

    @Column(name = "de02", length = 15)
    private String de02;

    @Column(name = "de03", length = 15)
    private String de03;

    @Column(name = "de04", length = 15)
    private String de04;

    @Column(name = "de05", length = 15)
    private String de05;

    @Column(name = "de06", length = 15)
    private String de06;

    @Column(name = "de07", length = 15)
    private String de07;

    @Column(name = "de08", length = 15)
    private String de08;

    @Column(name = "de09", length = 15)
    private String de09;

    @Column(name = "de10", length = 15)
    private String de10;

    @Column(name = "de11", length = 15)
    private String de11;

    @Column(name = "de12", length = 15)
    private String de12;

    @Column(name = "de13", length = 15)
    private String de13;

    @Column(name = "de14", length = 15)
    private String de14;

    @Column(name = "de15", length = 15)
    private String de15;

    @Column(name = "de16", length = 15)
    private String de16;

    @Column(name = "de17", length = 15)
    private String de17;

    @Column(name = "de18", length = 15)
    private String de18;

    @Column(name = "de19", length = 15)
    private String de19;

    @Column(name = "de20", length = 15)
    private String de20;

    @Column(name = "cr01", length = 15)
    private String cr01;

    @Column(name = "cr02", length = 15)
    private String cr02;

    @Column(name = "cr03", length = 15)
    private String cr03;

    @Column(name = "cr04", length = 15)
    private String cr04;

    @Column(name = "cr05", length = 15)
    private String cr05;

    @Column(name = "fbas", length = 15)
    private String fbas;

    @Column(name = "tbas", length = 10)
    private String tbas;

    @Column(name = "note", length = 200)
    private String note;

    @Column(name = "formule", length = 200)
    private String formule;

    @Column(name = "aamm", length = 6)
    private String aamm;

    @Column(name = "nbul")
    private Integer nbul;
}
