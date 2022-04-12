package com.cmbassi.gestiondepaie.dto;

import com.cmbassi.gestiondepaie.model.ElementSalaire;
import com.cmbassi.gestiondepaie.model.ElementSalaireBareme;
import com.cmbassi.gestiondepaie.model.ElementSalaireBase;
import com.cmbassi.gestiondepaie.model.Salarie;
import com.cmbassi.gestiondestock.model.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.List;


@Data
@Builder
public class ElementSalaireDto {

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

    @JsonIgnore
    private List<ElementSalaireBaseDto> elementSalaireBase;

    @JsonIgnore
    private List<ElementSalaireBaremeDto> elementSalaireBareme;

    public ElementSalaireDto() {
    }

    public ElementSalaireDto(Integer idEntreprise, String crub, String lrub, String calc, String pror, String ppcg, String prac, String prhr, String prtb, String prcl, String prtm, Long prno, String ppas, Long moi1, Long moi2, Long moi3, Long moi4, long bul1, long bul2, long bul3, long bul4, String apcf, String cabf, long prbul, String cbulf, String ednul, String edcum, String edbbu, Long epbul, String ajus, Long ajnu, String snet, String ecar, String typr, String esat, String rreg, String rman, String perc, String freq, String addf, String rcon, String eddf, String basc, String trtc, String trve, String exo, String val1, String val2, String val3, String mopa, String lbtm, String opfi, long algo, String cle1, String cle2, String tabl, String toum, Long nutm, String arro, String resl, String sup1, String sups, String sup2, String inf1, String infs, String inf2, String egu1, String egus, String egu2, String cs1, String cs2, String cs3, String sexe, Long age1, Long age2, String sit1, String sit2, String sit3, String sit4, Long nbe1, Long nbe2, String nat1, String nat2, String zca1, String zca2, String zca3, String zca4, String cat1, String cat2, String tyc1, String tyc2, String tyc3, String tyc4, String tyc5, String tyc6, String tyc7, String tyc8, String gra1, String gra2, String gra3, String gra4, String gra5, String gra6, String gra7, String gra8, String avn, String niv11, String niv12, String niv13, String niv14, String niv21, String niv22, String niv23, String niv24, String niv31, String niv32, String niv33, String niv34, String synd, String reg1, String reg2, String reg3, String reg4, String reg5, String reg6, String reg7, String reg8, String clas1, String clas2, String clas3, String clas4, String cfon, String hif1, String hif2, String hif3, String hif4, String fon1, String fon2, String fon3, String fon4, String fon5, String fon6, String fon7, String fon8, String zl11, String zl12, String zl21, String zl22, String cais, String dnbp, String txmt, String trcu, String basp, String abat, String abmx, String pmin, String pmax, BigDecimal pcab, String pdap, String comp, String cper, String de01, String de02, String de03, String de04, String de05, String de06, String de07, String de08, String de09, String de10, String de11, String de12, String de13, String de14, String de15, String de16, String de17, String de18, String de19, String de20, String cr01, String cr02, String cr03, String cr04, String cr05, String fbas, String tbas, String note, String formule) {
        this.idEntreprise = idEntreprise;
        this.crub = crub;
        this.lrub = lrub;
        this.calc = calc;
        this.pror = pror;
        this.ppcg = ppcg;
        this.prac = prac;
        this.prhr = prhr;
        this.prtb = prtb;
        this.prcl = prcl;
        this.prtm = prtm;
        this.prno = prno;
        this.ppas = ppas;
        this.moi1 = moi1;
        this.moi2 = moi2;
        this.moi3 = moi3;
        this.moi4 = moi4;
        this.bul1 = bul1;
        this.bul2 = bul2;
        this.bul3 = bul3;
        this.bul4 = bul4;
        this.apcf = apcf;
        this.cabf = cabf;
        this.prbul = prbul;
        this.cbulf = cbulf;
        this.ednul = ednul;
        this.edcum = edcum;
        this.edbbu = edbbu;
        this.epbul = epbul;
        this.ajus = ajus;
        this.ajnu = ajnu;
        this.snet = snet;
        this.ecar = ecar;
        this.typr = typr;
        this.esat = esat;
        this.rreg = rreg;
        this.rman = rman;
        this.perc = perc;
        this.freq = freq;
        this.addf = addf;
        this.rcon = rcon;
        this.eddf = eddf;
        this.basc = basc;
        this.trtc = trtc;
        this.trve = trve;
        this.exo = exo;
        this.val1 = val1;
        this.val2 = val2;
        this.val3 = val3;
        this.mopa = mopa;
        this.lbtm = lbtm;
        this.opfi = opfi;
        this.algo = algo;
        this.cle1 = cle1;
        this.cle2 = cle2;
        this.tabl = tabl;
        this.toum = toum;
        this.nutm = nutm;
        this.arro = arro;
        this.resl = resl;
        this.sup1 = sup1;
        this.sups = sups;
        this.sup2 = sup2;
        this.inf1 = inf1;
        this.infs = infs;
        this.inf2 = inf2;
        this.egu1 = egu1;
        this.egus = egus;
        this.egu2 = egu2;
        this.cs1 = cs1;
        this.cs2 = cs2;
        this.cs3 = cs3;
        this.sexe = sexe;
        this.age1 = age1;
        this.age2 = age2;
        this.sit1 = sit1;
        this.sit2 = sit2;
        this.sit3 = sit3;
        this.sit4 = sit4;
        this.nbe1 = nbe1;
        this.nbe2 = nbe2;
        this.nat1 = nat1;
        this.nat2 = nat2;
        this.zca1 = zca1;
        this.zca2 = zca2;
        this.zca3 = zca3;
        this.zca4 = zca4;
        this.cat1 = cat1;
        this.cat2 = cat2;
        this.tyc1 = tyc1;
        this.tyc2 = tyc2;
        this.tyc3 = tyc3;
        this.tyc4 = tyc4;
        this.tyc5 = tyc5;
        this.tyc6 = tyc6;
        this.tyc7 = tyc7;
        this.tyc8 = tyc8;
        this.gra1 = gra1;
        this.gra2 = gra2;
        this.gra3 = gra3;
        this.gra4 = gra4;
        this.gra5 = gra5;
        this.gra6 = gra6;
        this.gra7 = gra7;
        this.gra8 = gra8;
        this.avn = avn;
        this.niv11 = niv11;
        this.niv12 = niv12;
        this.niv13 = niv13;
        this.niv14 = niv14;
        this.niv21 = niv21;
        this.niv22 = niv22;
        this.niv23 = niv23;
        this.niv24 = niv24;
        this.niv31 = niv31;
        this.niv32 = niv32;
        this.niv33 = niv33;
        this.niv34 = niv34;
        this.synd = synd;
        this.reg1 = reg1;
        this.reg2 = reg2;
        this.reg3 = reg3;
        this.reg4 = reg4;
        this.reg5 = reg5;
        this.reg6 = reg6;
        this.reg7 = reg7;
        this.reg8 = reg8;
        this.clas1 = clas1;
        this.clas2 = clas2;
        this.clas3 = clas3;
        this.clas4 = clas4;
        this.cfon = cfon;
        this.hif1 = hif1;
        this.hif2 = hif2;
        this.hif3 = hif3;
        this.hif4 = hif4;
        this.fon1 = fon1;
        this.fon2 = fon2;
        this.fon3 = fon3;
        this.fon4 = fon4;
        this.fon5 = fon5;
        this.fon6 = fon6;
        this.fon7 = fon7;
        this.fon8 = fon8;
        this.zl11 = zl11;
        this.zl12 = zl12;
        this.zl21 = zl21;
        this.zl22 = zl22;
        this.cais = cais;
        this.dnbp = dnbp;
        this.txmt = txmt;
        this.trcu = trcu;
        this.basp = basp;
        this.abat = abat;
        this.abmx = abmx;
        this.pmin = pmin;
        this.pmax = pmax;
        this.pcab = pcab;
        this.pdap = pdap;
        this.comp = comp;
        this.cper = cper;
        this.de01 = de01;
        this.de02 = de02;
        this.de03 = de03;
        this.de04 = de04;
        this.de05 = de05;
        this.de06 = de06;
        this.de07 = de07;
        this.de08 = de08;
        this.de09 = de09;
        this.de10 = de10;
        this.de11 = de11;
        this.de12 = de12;
        this.de13 = de13;
        this.de14 = de14;
        this.de15 = de15;
        this.de16 = de16;
        this.de17 = de17;
        this.de18 = de18;
        this.de19 = de19;
        this.de20 = de20;
        this.cr01 = cr01;
        this.cr02 = cr02;
        this.cr03 = cr03;
        this.cr04 = cr04;
        this.cr05 = cr05;
        this.fbas = fbas;
        this.tbas = tbas;
        this.note = note;
        this.formule = formule;
    }

    public ElementSalaireDto(Integer idEntreprise, String crub, String lrub, String calc, String pror, String ppcg, String prac, String prhr, String prtb, String prcl, String prtm, Long prno, String ppas, Long moi1, Long moi2, Long moi3, Long moi4, long bul1, long bul2, long bul3, long bul4, String apcf, String cabf, long prbul, String cbulf, String ednul, String edcum, String edbbu, Long epbul, String ajus, Long ajnu, String snet, String ecar, String typr, String esat, String rreg, String rman, String perc, String freq, String addf, String rcon, String eddf, String basc, String trtc, String trve, String exo, String val1, String val2, String val3, String mopa, String lbtm, String opfi, long algo, String cle1, String cle2, String tabl, String toum, Long nutm, String arro, String resl, String sup1, String sups, String sup2, String inf1, String infs, String inf2, String egu1, String egus, String egu2, String cs1, String cs2, String cs3, String sexe, Long age1, Long age2, String sit1, String sit2, String sit3, String sit4, Long nbe1, Long nbe2, String nat1, String nat2, String zca1, String zca2, String zca3, String zca4, String cat1, String cat2, String tyc1, String tyc2, String tyc3, String tyc4, String tyc5, String tyc6, String tyc7, String tyc8, String gra1, String gra2, String gra3, String gra4, String gra5, String gra6, String gra7, String gra8, String avn, String niv11, String niv12, String niv13, String niv14, String niv21, String niv22, String niv23, String niv24, String niv31, String niv32, String niv33, String niv34, String synd, String reg1, String reg2, String reg3, String reg4, String reg5, String reg6, String reg7, String reg8, String clas1, String clas2, String clas3, String clas4, String cfon, String hif1, String hif2, String hif3, String hif4, String fon1, String fon2, String fon3, String fon4, String fon5, String fon6, String fon7, String fon8, String zl11, String zl12, String zl21, String zl22, String cais, String dnbp, String txmt, String trcu, String basp, String abat, String abmx, String pmin, String pmax, BigDecimal pcab, String pdap, String comp, String cper, String de01, String de02, String de03, String de04, String de05, String de06, String de07, String de08, String de09, String de10, String de11, String de12, String de13, String de14, String de15, String de16, String de17, String de18, String de19, String de20, String cr01, String cr02, String cr03, String cr04, String cr05, String fbas, String tbas, String note, String formule, List<ElementSalaireBaseDto> elementSalaireBase, List<ElementSalaireBaremeDto> elementSalaireBareme) {
        this.idEntreprise = idEntreprise;
        this.crub = crub;
        this.lrub = lrub;
        this.calc = calc;
        this.pror = pror;
        this.ppcg = ppcg;
        this.prac = prac;
        this.prhr = prhr;
        this.prtb = prtb;
        this.prcl = prcl;
        this.prtm = prtm;
        this.prno = prno;
        this.ppas = ppas;
        this.moi1 = moi1;
        this.moi2 = moi2;
        this.moi3 = moi3;
        this.moi4 = moi4;
        this.bul1 = bul1;
        this.bul2 = bul2;
        this.bul3 = bul3;
        this.bul4 = bul4;
        this.apcf = apcf;
        this.cabf = cabf;
        this.prbul = prbul;
        this.cbulf = cbulf;
        this.ednul = ednul;
        this.edcum = edcum;
        this.edbbu = edbbu;
        this.epbul = epbul;
        this.ajus = ajus;
        this.ajnu = ajnu;
        this.snet = snet;
        this.ecar = ecar;
        this.typr = typr;
        this.esat = esat;
        this.rreg = rreg;
        this.rman = rman;
        this.perc = perc;
        this.freq = freq;
        this.addf = addf;
        this.rcon = rcon;
        this.eddf = eddf;
        this.basc = basc;
        this.trtc = trtc;
        this.trve = trve;
        this.exo = exo;
        this.val1 = val1;
        this.val2 = val2;
        this.val3 = val3;
        this.mopa = mopa;
        this.lbtm = lbtm;
        this.opfi = opfi;
        this.algo = algo;
        this.cle1 = cle1;
        this.cle2 = cle2;
        this.tabl = tabl;
        this.toum = toum;
        this.nutm = nutm;
        this.arro = arro;
        this.resl = resl;
        this.sup1 = sup1;
        this.sups = sups;
        this.sup2 = sup2;
        this.inf1 = inf1;
        this.infs = infs;
        this.inf2 = inf2;
        this.egu1 = egu1;
        this.egus = egus;
        this.egu2 = egu2;
        this.cs1 = cs1;
        this.cs2 = cs2;
        this.cs3 = cs3;
        this.sexe = sexe;
        this.age1 = age1;
        this.age2 = age2;
        this.sit1 = sit1;
        this.sit2 = sit2;
        this.sit3 = sit3;
        this.sit4 = sit4;
        this.nbe1 = nbe1;
        this.nbe2 = nbe2;
        this.nat1 = nat1;
        this.nat2 = nat2;
        this.zca1 = zca1;
        this.zca2 = zca2;
        this.zca3 = zca3;
        this.zca4 = zca4;
        this.cat1 = cat1;
        this.cat2 = cat2;
        this.tyc1 = tyc1;
        this.tyc2 = tyc2;
        this.tyc3 = tyc3;
        this.tyc4 = tyc4;
        this.tyc5 = tyc5;
        this.tyc6 = tyc6;
        this.tyc7 = tyc7;
        this.tyc8 = tyc8;
        this.gra1 = gra1;
        this.gra2 = gra2;
        this.gra3 = gra3;
        this.gra4 = gra4;
        this.gra5 = gra5;
        this.gra6 = gra6;
        this.gra7 = gra7;
        this.gra8 = gra8;
        this.avn = avn;
        this.niv11 = niv11;
        this.niv12 = niv12;
        this.niv13 = niv13;
        this.niv14 = niv14;
        this.niv21 = niv21;
        this.niv22 = niv22;
        this.niv23 = niv23;
        this.niv24 = niv24;
        this.niv31 = niv31;
        this.niv32 = niv32;
        this.niv33 = niv33;
        this.niv34 = niv34;
        this.synd = synd;
        this.reg1 = reg1;
        this.reg2 = reg2;
        this.reg3 = reg3;
        this.reg4 = reg4;
        this.reg5 = reg5;
        this.reg6 = reg6;
        this.reg7 = reg7;
        this.reg8 = reg8;
        this.clas1 = clas1;
        this.clas2 = clas2;
        this.clas3 = clas3;
        this.clas4 = clas4;
        this.cfon = cfon;
        this.hif1 = hif1;
        this.hif2 = hif2;
        this.hif3 = hif3;
        this.hif4 = hif4;
        this.fon1 = fon1;
        this.fon2 = fon2;
        this.fon3 = fon3;
        this.fon4 = fon4;
        this.fon5 = fon5;
        this.fon6 = fon6;
        this.fon7 = fon7;
        this.fon8 = fon8;
        this.zl11 = zl11;
        this.zl12 = zl12;
        this.zl21 = zl21;
        this.zl22 = zl22;
        this.cais = cais;
        this.dnbp = dnbp;
        this.txmt = txmt;
        this.trcu = trcu;
        this.basp = basp;
        this.abat = abat;
        this.abmx = abmx;
        this.pmin = pmin;
        this.pmax = pmax;
        this.pcab = pcab;
        this.pdap = pdap;
        this.comp = comp;
        this.cper = cper;
        this.de01 = de01;
        this.de02 = de02;
        this.de03 = de03;
        this.de04 = de04;
        this.de05 = de05;
        this.de06 = de06;
        this.de07 = de07;
        this.de08 = de08;
        this.de09 = de09;
        this.de10 = de10;
        this.de11 = de11;
        this.de12 = de12;
        this.de13 = de13;
        this.de14 = de14;
        this.de15 = de15;
        this.de16 = de16;
        this.de17 = de17;
        this.de18 = de18;
        this.de19 = de19;
        this.de20 = de20;
        this.cr01 = cr01;
        this.cr02 = cr02;
        this.cr03 = cr03;
        this.cr04 = cr04;
        this.cr05 = cr05;
        this.fbas = fbas;
        this.tbas = tbas;
        this.note = note;
        this.formule = formule;
        this.elementSalaireBase = elementSalaireBase;
        this.elementSalaireBareme = elementSalaireBareme;
    }

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
