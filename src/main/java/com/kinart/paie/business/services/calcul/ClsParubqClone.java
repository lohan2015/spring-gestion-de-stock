package com.kinart.paie.business.services.calcul;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.kinart.paie.business.model.ElementSalaire;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * Classe contenant structure des rubriques de la base de donnée afin de ne pas traiter 
 * séparément les rubriques historisées et celles qui ne sont pas historisées.
 * @author c.mbassi
 *
 */
public class ClsParubqClone implements Serializable {

    /**
	 * the serial id of that class
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private ClsParubqClonePK comp_id;

    /** persistent field */
    private String lrub;

    /** persistent field */
    private String calc;

    /** persistent field */
    private String pror;

    /** persistent field */
    private String ppcg;

    /** nullable persistent field */
    private String prac;

    /** nullable persistent field */
    private String prhr;

    /** nullable persistent field */
    private String prtb;

    /** nullable persistent field */
    private String prcl;

    /** nullable persistent field */
    private String prtm;

    /** nullable persistent field */
    private Long prno;

    /** nullable persistent field */
    private String ppas;

    /** nullable persistent field */
    private Long moi1;

    /** nullable persistent field */
    private Long moi2;

    /** nullable persistent field */
    private Long moi3;

    /** nullable persistent field */
    private Long moi4;

    /** persistent field */
    private long bul1;

    /** persistent field */
    private long bul2;

    /** persistent field */
    private long bul3;

    /** persistent field */
    private long bul4;

    /** persistent field */
    private String apcf;

    /** nullable persistent field */
    private String cabf;

    /** persistent field */
    private long prbul;

    /** nullable persistent field */
    private String cbulf;

    /** persistent field */
    private String ednul;

    /** persistent field */
    private String edcum;

    /** nullable persistent field */
    private String edbbu;

    /** nullable persistent field */
    private Long epbul;

    /** persistent field */
    private String ajus;

    /** nullable persistent field */
    private Long ajnu;

    /** persistent field */
    private String snet;

    /** persistent field */
    private String ecar;

    /** nullable persistent field */
    private String typr;

    /** nullable persistent field */
    private String esat;

    /** nullable persistent field */
    private String rreg;

    /** nullable persistent field */
    private String rman;

    /** nullable persistent field */
    private String perc;

    /** nullable persistent field */
    private String freq;

    /** nullable persistent field */
    private String addf;

    /** nullable persistent field */
    private String rcon;

    /** nullable persistent field */
    private String eddf;

    /** persistent field */
    private String basc;

    /** nullable persistent field */
    private String trtc;

    /** nullable persistent field */
    private String trve;

    /** nullable persistent field */
    private String exo;

    /** nullable persistent field */
    private String val1;

    /** nullable persistent field */
    private String val2;

    /** nullable persistent field */
    private String val3;

    /** nullable persistent field */
    private String mopa;

    /** nullable persistent field */
    private String lbtm;

    /** nullable persistent field */
    private String opfi;

    /** persistent field */
    private long algo;

    /** nullable persistent field */
    private String cle1;

    /** nullable persistent field */
    private String cle2;

    /** nullable persistent field */
    private String tabl;

    /** nullable persistent field */
    private String toum;

    /** nullable persistent field */
    private Long nutm;

    /** nullable persistent field */
    private String arro;

    /** nullable persistent field */
    private String resl;

    /** nullable persistent field */
    private String sup1;

    /** nullable persistent field */
    private String sups;

    /** nullable persistent field */
    private String sup2;

    /** nullable persistent field */
    private String inf1;

    /** nullable persistent field */
    private String infs;

    /** nullable persistent field */
    private String inf2;

    /** nullable persistent field */
    private String egu1;

    /** nullable persistent field */
    private String egus;

    /** nullable persistent field */
    private String egu2;

    /** nullable persistent field */
    private String cs1;

    /** nullable persistent field */
    private String cs2;

    /** nullable persistent field */
    private String cs3;

    /** nullable persistent field */
    private String sexe;

    /** nullable persistent field */
    private Long age1;

    /** nullable persistent field */
    private Long age2;

    /** nullable persistent field */
    private String sit1;

    /** nullable persistent field */
    private String sit2;

    /** nullable persistent field */
    private String sit3;

    /** nullable persistent field */
    private String sit4;

    /** nullable persistent field */
    private Long nbe1;

    /** nullable persistent field */
    private Long nbe2;

    /** nullable persistent field */
    private String nat1;

    /** nullable persistent field */
    private String nat2;

    /** nullable persistent field */
    private String zca1;

    /** nullable persistent field */
    private String zca2;

    /** nullable persistent field */
    private String zca3;

    /** nullable persistent field */
    private String zca4;

    /** nullable persistent field */
    private String cat1;

    /** nullable persistent field */
    private String cat2;

    /** nullable persistent field */
    private String tyc1;

    /** nullable persistent field */
    private String tyc2;

    /** nullable persistent field */
    private String tyc3;

    /** nullable persistent field */
    private String tyc4;

    /** nullable persistent field */
    private String tyc5;

    /** nullable persistent field */
    private String tyc6;

    /** nullable persistent field */
    private String tyc7;

    /** nullable persistent field */
    private String tyc8;

    /** nullable persistent field */
    private String gra1;

    /** nullable persistent field */
    private String gra2;

    /** nullable persistent field */
    private String gra3;

    /** nullable persistent field */
    private String gra4;

    /** nullable persistent field */
    private String gra5;

    /** nullable persistent field */
    private String gra6;

    /** nullable persistent field */
    private String gra7;

    /** nullable persistent field */
    private String gra8;

    /** nullable persistent field */
    private String avn;

    /** nullable persistent field */
    private String niv11;

    /** nullable persistent field */
    private String niv12;

    /** nullable persistent field */
    private String niv13;

    /** nullable persistent field */
    private String niv14;

    /** nullable persistent field */
    private String niv21;

    /** nullable persistent field */
    private String niv22;

    /** nullable persistent field */
    private String niv23;

    /** nullable persistent field */
    private String niv24;

    /** nullable persistent field */
    private String niv31;

    /** nullable persistent field */
    private String niv32;

    /** nullable persistent field */
    private String niv33;

    /** nullable persistent field */
    private String niv34;

    /** nullable persistent field */
    private String synd;

    /** nullable persistent field */
    private String reg1;

    /** nullable persistent field */
    private String reg2;

    /** nullable persistent field */
    private String reg3;

    /** nullable persistent field */
    private String reg4;

    /** nullable persistent field */
    private String reg5;

    /** nullable persistent field */
    private String reg6;

    /** nullable persistent field */
    private String reg7;

    /** nullable persistent field */
    private String reg8;

    /** nullable persistent field */
    private String clas1;

    /** nullable persistent field */
    private String clas2;

    /** nullable persistent field */
    private String clas3;

    /** nullable persistent field */
    private String clas4;

    /** nullable persistent field */
    private String cfon;

    /** nullable persistent field */
    private String hif1;

    /** nullable persistent field */
    private String hif2;

    /** nullable persistent field */
    private String hif3;

    /** nullable persistent field */
    private String hif4;

    /** nullable persistent field */
    private String fon1;

    /** nullable persistent field */
    private String fon2;

    /** nullable persistent field */
    private String fon3;

    /** nullable persistent field */
    private String fon4;

    /** nullable persistent field */
    private String fon5;

    /** nullable persistent field */
    private String fon6;

    /** nullable persistent field */
    private String fon7;

    /** nullable persistent field */
    private String fon8;

    /** nullable persistent field */
    private String zl11;

    /** nullable persistent field */
    private String zl12;

    /** nullable persistent field */
    private String zl21;

    /** nullable persistent field */
    private String zl22;

    /** nullable persistent field */
    private String cais;

    /** nullable persistent field */
    private String dnbp;

    /** nullable persistent field */
    private String txmt;

    /** nullable persistent field */
    private String trcu;

    /** nullable persistent field */
    private String basp;

    /** nullable persistent field */
    private String abat;

    /** nullable persistent field */
    private String abmx;

    /** nullable persistent field */
    private String pmin;

    /** nullable persistent field */
    private String pmax;

    /** nullable persistent field */
    private BigDecimal pcab;

    /** nullable persistent field */
    private String pdap;

    /** nullable persistent field */
    private Date dtcr;

    /** nullable persistent field */
    private Date dtdm;

    /** nullable persistent field */
    private String comp;

    /** nullable persistent field */
    private String cper;

    /** nullable persistent field */
    private String de01;

    /** nullable persistent field */
    private String de02;

    /** nullable persistent field */
    private String de03;

    /** nullable persistent field */
    private String de04;

    /** nullable persistent field */
    private String de05;

    /** nullable persistent field */
    private String de06;

    /** nullable persistent field */
    private String de07;

    /** nullable persistent field */
    private String de08;

    /** nullable persistent field */
    private String de09;

    /** nullable persistent field */
    private String de10;

    /** nullable persistent field */
    private String de11;

    /** nullable persistent field */
    private String de12;

    /** nullable persistent field */
    private String de13;

    /** nullable persistent field */
    private String de14;

    /** nullable persistent field */
    private String de15;

    /** nullable persistent field */
    private String de16;

    /** nullable persistent field */
    private String de17;

    /** nullable persistent field */
    private String de18;

    /** nullable persistent field */
    private String de19;

    /** nullable persistent field */
    private String de20;

    /** nullable persistent field */
    private String cr01;

    /** nullable persistent field */
    private String cr02;

    /** nullable persistent field */
    private String cr03;

    /** nullable persistent field */
    private String cr04;

    /** nullable persistent field */
    private String cr05;

    /** nullable persistent field */
    private String fbas;

    /** nullable persistent field */
    private String tbas;

    /** nullable persistent field */
    private String note;
    
    /** nullable persistent field */
    private String formule;
    
    /** default constructor */
    public ClsParubqClone() {
    }
    
    /**
     * le clonage d'une rubrique
     * @param rubrique l'objet rubrique qui peut étre Rhprubrique ou Rhthrubq
     * @return un objet construit é partir soit de Rhprubrique ou Rhthrubq
     * @throws Exception
     */
    public static ClsParubqClone clone(Object rubrique) throws Exception{
    	ClsParubqClone ruba = null;
    	if(rubrique instanceof ElementSalaire)
    		ruba = new ClsParubqClone((ElementSalaire) rubrique);
    	else
    		throw new Exception("Type of rubrique mismatch.");
    	//
    	return ruba;
    }
    /**
     * permet de créer une nouvelle qui ne tient pas compte de l'historique
     * @param parubq
     */
    public ClsParubqClone(ElementSalaire parubq) {
    	this.comp_id = new ClsParubqClonePK();
    	this.comp_id.setCdos(parubq.getIdEntreprise().intValue()+"");
    	this.comp_id.setCrub(parubq.getCrub());
        this.lrub = parubq.getLrub();
        this.calc = parubq.getCalc();
        this.pror = parubq.getPror();
        this.ppcg = parubq.getPpcg();
        this.prac = parubq.getPrac();
        this.prhr = parubq.getPrhr();
        this.prtb = parubq.getPrtb();
        this.prcl = parubq.getPrcl();
        this.prtm = parubq.getPrtm();
        this.prno = parubq.getPrno();
        this.ppas = parubq.getPpas();
        this.moi1 = parubq.getMoi1();
        this.moi2 = parubq.getMoi2();
        this.moi3 = parubq.getMoi3();
        this.moi4 = parubq.getMoi4();
        this.bul1 = parubq.getBul1();
        this.bul2 = parubq.getBul2();
        this.bul3 = parubq.getBul3();
        this.bul4 = parubq.getBul4();
        this.apcf = parubq.getApcf();
        this.cabf = parubq.getCabf();
        this.prbul = parubq.getPrbul();
        this.cbulf = parubq.getCbulf();
        this.ednul = parubq.getEdnul();
        this.edcum = parubq.getEdcum();
        this.edbbu = parubq.getEdbbu();
        this.epbul = parubq.getEpbul();
        this.ajus = parubq.getAjus();
        this.ajnu = parubq.getAjnu();
        this.snet = parubq.getSnet();
        this.ecar = parubq.getEcar();
        this.typr = parubq.getTypr();
        this.esat = parubq.getEsat();
        this.rreg = parubq.getRreg();
        this.rman = parubq.getRman();
        this.perc = parubq.getPerc();
        this.freq = parubq.getFreq();
        this.addf = parubq.getAddf();
        this.rcon = parubq.getRcon();
        this.eddf = parubq.getEddf();
        this.basc = parubq.getBasc();
        this.trtc = parubq.getTrtc();
        this.trve = parubq.getTrve();
        this.exo = parubq.getExo();
        this.val1 = parubq.getVal1();
        this.val2 = parubq.getVal2();
        this.val3 = parubq.getVal3();
        this.mopa = parubq.getMopa();
        this.lbtm = parubq.getLbtm();
        this.opfi = parubq.getOpfi();
        this.algo = parubq.getAlgo();
        this.cle1 = parubq.getCle1();
        this.cle2 = parubq.getCle2();
        this.tabl = parubq.getTabl();
        this.toum = parubq.getToum();
        this.nutm = parubq.getNutm();
        this.arro = parubq.getArro();
        this.resl = parubq.getResl();
        this.sup1 = parubq.getSup1();
        this.sups = parubq.getSups();
        this.sup2 = parubq.getSup2();
        this.inf1 = parubq.getInf1();
        this.infs = parubq.getInfs();
        this.inf2 = parubq.getInf2();
        this.egu1 = parubq.getEgu1();
        this.egus = parubq.getEgus();
        this.egu2 = parubq.getEgu2();
        this.cs1 = parubq.getCs1();
        this.cs2 = parubq.getCs2();
        this.cs3 = parubq.getCs3();
        this.sexe = parubq.getSexe();
        this.age1 = parubq.getAge1();
        this.age2 = parubq.getAge2();
        this.sit1 = parubq.getSit1();
        this.sit2 = parubq.getSit2();
        this.sit3 = parubq.getSit3();
        this.sit4 = parubq.getSit4();
        this.nbe1 = parubq.getNbe1();
        this.nbe2 = parubq.getNbe2();
        this.nat1 = parubq.getNat1();
        this.nat2 = parubq.getNat2();
        this.zca1 = parubq.getZca1();
        this.zca2 = parubq.getZca2();
        this.zca3 = parubq.getZca3();
        this.zca4 = parubq.getZca4();
        this.cat1 = parubq.getCat1();
        this.cat2 = parubq.getCat2();
        this.tyc1 = parubq.getTyc1();
        this.tyc2 = parubq.getTyc2();
        this.tyc3 = parubq.getTyc3();
        this.tyc4 = parubq.getTyc4();
        this.tyc5 = parubq.getTyc5();
        this.tyc6 = parubq.getTyc6();
        this.tyc7 = parubq.getTyc7();
        this.tyc8 = parubq.getTyc8();
        this.gra1 = parubq.getGra1();
        this.gra2 = parubq.getGra2();
        this.gra3 = parubq.getGra3();
        this.gra4 = parubq.getGra4();
        this.gra5 = parubq.getGra5();
        this.gra6 = parubq.getGra6();
        this.gra7 = parubq.getGra7();
        this.gra8 = parubq.getGra8();
        this.avn = parubq.getAvn();
        this.niv11 = parubq.getNiv11();
        this.niv12 = parubq.getNiv12();
        this.niv13 = parubq.getNiv13();
        this.niv14 = parubq.getNiv14();
        this.niv21 = parubq.getNiv21();
        this.niv22 = parubq.getNiv22();
        this.niv23 = parubq.getNiv23();
        this.niv24 = parubq.getNiv24();
        this.niv31 = parubq.getNiv31();
        this.niv32 = parubq.getNiv32();
        this.niv33 = parubq.getNiv33();
        this.niv34 = parubq.getNiv34();
        this.synd = parubq.getSynd();
        this.reg1 = parubq.getReg1();
        this.reg2 = parubq.getReg2();
        this.reg3 = parubq.getReg3();
        this.reg4 = parubq.getReg4();
        this.reg5 = parubq.getReg5();
        this.reg6 = parubq.getReg6();
        this.reg7 = parubq.getReg7();
        this.reg8 = parubq.getReg8();
        this.clas1 = parubq.getClas1();
        this.clas2 = parubq.getClas2();
        this.clas3 = parubq.getClas3();
        this.clas4 = parubq.getClas4();
        this.cfon = parubq.getCfon();
        this.hif1 = parubq.getHif1();
        this.hif2 = parubq.getHif2();
        this.hif3 = parubq.getHif3();
        this.hif4 = parubq.getHif4();
        this.fon1 = parubq.getFon1();
        this.fon2 = parubq.getFon2();
        this.fon3 = parubq.getFon3();
        this.fon4 = parubq.getFon4();
        this.fon5 = parubq.getFon5();
        this.fon6 = parubq.getFon6();
        this.fon7 = parubq.getFon7();
        this.fon8 = parubq.getFon8();
        this.zl11 = parubq.getZl11();
        this.zl12 = parubq.getZl12();
        this.zl21 = parubq.getZl21();
        this.zl22 = parubq.getZl22();
        this.cais = parubq.getCais();
        this.dnbp = parubq.getDnbp();
        this.txmt = parubq.getTxmt();
        this.trcu = parubq.getTrcu();
        this.basp = parubq.getBasp();
        this.abat = parubq.getAbat();
        this.abmx = parubq.getAbmx();
        this.pmin = parubq.getPmin();
        this.pmax = parubq.getPmax();
        this.pcab = parubq.getPcab();
        this.pdap = parubq.getPdap();
        this.dtcr = Date.from(parubq.getCreationDate());
        this.dtdm = Date.from(parubq.getLastModifiedDate());
        this.comp = parubq.getComp();
        this.cper = parubq.getCper();
        this.de01 = parubq.getDe01();
        this.de02 = parubq.getDe02();
        this.de03 = parubq.getDe03();
        this.de04 = parubq.getDe04();
        this.de05 = parubq.getDe05();
        this.de06 = parubq.getDe06();
        this.de07 = parubq.getDe07();
        this.de08 = parubq.getDe08();
        this.de09 = parubq.getDe09();
        this.de10 = parubq.getDe10();
        this.de11 = parubq.getDe11();
        this.de12 = parubq.getDe12();
        this.de13 = parubq.getDe13();
        this.de14 = parubq.getDe14();
        this.de15 = parubq.getDe15();
        this.de16 = parubq.getDe16();
        this.de17 = parubq.getDe17();
        this.de18 = parubq.getDe18();
        this.de19 = parubq.getDe19();
        this.de20 = parubq.getDe20();
        this.cr01 = parubq.getCr01();
        this.cr02 = parubq.getCr02();
        this.cr03 = parubq.getCr03();
        this.cr04 = parubq.getCr04();
        this.cr05 = parubq.getCr05();
        this.fbas = parubq.getFbas();
        this.tbas = parubq.getTbas();
        this.note = parubq.getNote();
        this.formule = parubq.getFormule();
    }
    
    public ClsParubqClonePK getComp_id() {
        return this.comp_id;
    }

    public void setComp_id(ClsParubqClonePK comp_id) {
        this.comp_id = comp_id;
    }

    public String getLrub() {
        return this.lrub;
    }

    public void setLrub(String lrub) {
        this.lrub = lrub;
    }

    public String getCalc() {
        return this.calc;
    }

    public void setCalc(String calc) {
        this.calc = calc;
    }

    public String getPror() {
        return this.pror;
    }

    public void setPror(String pror) {
        this.pror = pror;
    }

    public String getPpcg() {
        return this.ppcg;
    }

    public void setPpcg(String ppcg) {
        this.ppcg = ppcg;
    }

    public String getPrac() {
        return this.prac;
    }

    public void setPrac(String prac) {
        this.prac = prac;
    }

    public String getPrhr() {
        return this.prhr;
    }

    public void setPrhr(String prhr) {
        this.prhr = prhr;
    }

    public String getPrtb() {
        return this.prtb;
    }

    public void setPrtb(String prtb) {
        this.prtb = prtb;
    }

    public String getPrcl() {
        return this.prcl;
    }

    public void setPrcl(String prcl) {
        this.prcl = prcl;
    }

    public String getPrtm() {
        return this.prtm;
    }

    public void setPrtm(String prtm) {
        this.prtm = prtm;
    }

    public Long getPrno() {
        return this.prno;
    }

    public void setPrno(Long prno) {
        this.prno = prno;
    }

    public String getPpas() {
        return this.ppas;
    }

    public void setPpas(String ppas) {
        this.ppas = ppas;
    }

    public Long getMoi1() {
        return this.moi1;
    }

    public void setMoi1(Long moi1) {
        this.moi1 = moi1;
    }

    public Long getMoi2() {
        return this.moi2;
    }

    public void setMoi2(Long moi2) {
        this.moi2 = moi2;
    }

    public Long getMoi3() {
        return this.moi3;
    }

    public void setMoi3(Long moi3) {
        this.moi3 = moi3;
    }

    public Long getMoi4() {
        return this.moi4;
    }

    public void setMoi4(Long moi4) {
        this.moi4 = moi4;
    }

    public long getBul1() {
        return this.bul1;
    }

    public void setBul1(long bul1) {
        this.bul1 = bul1;
    }

    public long getBul2() {
        return this.bul2;
    }

    public void setBul2(long bul2) {
        this.bul2 = bul2;
    }

    public long getBul3() {
        return this.bul3;
    }

    public void setBul3(long bul3) {
        this.bul3 = bul3;
    }

    public long getBul4() {
        return this.bul4;
    }

    public void setBul4(long bul4) {
        this.bul4 = bul4;
    }

    public String getApcf() {
        return this.apcf;
    }

    public void setApcf(String apcf) {
        this.apcf = apcf;
    }

    public String getCabf() {
        return this.cabf;
    }

    public void setCabf(String cabf) {
        this.cabf = cabf;
    }

    public long getPrbul() {
        return this.prbul;
    }

    public void setPrbul(long prbul) {
        this.prbul = prbul;
    }

    public String getCbulf() {
        return this.cbulf;
    }

    public void setCbulf(String cbulf) {
        this.cbulf = cbulf;
    }

    public String getEdnul() {
        return this.ednul;
    }

    public void setEdnul(String ednul) {
        this.ednul = ednul;
    }

    public String getEdcum() {
        return this.edcum;
    }

    public void setEdcum(String edcum) {
        this.edcum = edcum;
    }

    public String getEdbbu() {
        return this.edbbu;
    }

    public void setEdbbu(String edbbu) {
        this.edbbu = edbbu;
    }

    public Long getEpbul() {
        return this.epbul;
    }

    public void setEpbul(Long epbul) {
        this.epbul = epbul;
    }

    public String getAjus() {
        return this.ajus;
    }

    public void setAjus(String ajus) {
        this.ajus = ajus;
    }

    public Long getAjnu() {
        return this.ajnu;
    }

    public void setAjnu(Long ajnu) {
        this.ajnu = ajnu;
    }

    public String getSnet() {
        return this.snet;
    }

    public void setSnet(String snet) {
        this.snet = snet;
    }

    public String getEcar() {
        return this.ecar;
    }

    public void setEcar(String ecar) {
        this.ecar = ecar;
    }

    public String getTypr() {
        return this.typr;
    }

    public void setTypr(String typr) {
        this.typr = typr;
    }

    public String getEsat() {
        return this.esat;
    }

    public void setEsat(String esat) {
        this.esat = esat;
    }

    public String getRreg() {
        return this.rreg;
    }

    public void setRreg(String rreg) {
        this.rreg = rreg;
    }

    public String getRman() {
        return this.rman;
    }

    public void setRman(String rman) {
        this.rman = rman;
    }

    public String getPerc() {
        return this.perc;
    }

    public void setPerc(String perc) {
        this.perc = perc;
    }

    public String getFreq() {
        return this.freq;
    }

    public void setFreq(String freq) {
        this.freq = freq;
    }

    public String getAddf() {
        return this.addf;
    }

    public void setAddf(String addf) {
        this.addf = addf;
    }

    public String getRcon() {
        return this.rcon;
    }

    public void setRcon(String rcon) {
        this.rcon = rcon;
    }

    public String getEddf() {
        return this.eddf;
    }

    public void setEddf(String eddf) {
        this.eddf = eddf;
    }

    public String getBasc() {
        return this.basc;
    }

    public void setBasc(String basc) {
        this.basc = basc;
    }

    public String getTrtc() {
        return this.trtc;
    }

    public void setTrtc(String trtc) {
        this.trtc = trtc;
    }

    public String getTrve() {
        return this.trve;
    }

    public void setTrve(String trve) {
        this.trve = trve;
    }

    public String getExo() {
        return this.exo;
    }

    public void setExo(String exo) {
        this.exo = exo;
    }

    public String getVal1() {
        return this.val1;
    }

    public void setVal1(String val1) {
        this.val1 = val1;
    }

    public String getVal2() {
        return this.val2;
    }

    public void setVal2(String val2) {
        this.val2 = val2;
    }

    public String getVal3() {
        return this.val3;
    }

    public void setVal3(String val3) {
        this.val3 = val3;
    }

    public String getMopa() {
        return this.mopa;
    }

    public void setMopa(String mopa) {
        this.mopa = mopa;
    }

    public String getLbtm() {
        return this.lbtm;
    }

    public void setLbtm(String lbtm) {
        this.lbtm = lbtm;
    }

    public String getOpfi() {
        return this.opfi;
    }

    public void setOpfi(String opfi) {
        this.opfi = opfi;
    }

    public long getAlgo() {
        return this.algo;
    }

    public void setAlgo(long algo) {
        this.algo = algo;
    }

    public String getCle1() {
        return this.cle1;
    }

    public void setCle1(String cle1) {
        this.cle1 = cle1;
    }

    public String getCle2() {
        return this.cle2;
    }

    public void setCle2(String cle2) {
        this.cle2 = cle2;
    }

    public String getTabl() {
        return this.tabl;
    }

    public void setTabl(String tabl) {
        this.tabl = tabl;
    }

    public String getToum() {
        return this.toum;
    }

    public void setToum(String toum) {
        this.toum = toum;
    }

    public Long getNutm() {
        return this.nutm;
    }

    public void setNutm(Long nutm) {
        this.nutm = nutm;
    }

    public String getArro() {
        return this.arro;
    }

    public void setArro(String arro) {
        this.arro = arro;
    }

    public String getResl() {
        return this.resl;
    }

    public void setResl(String resl) {
        this.resl = resl;
    }

    public String getSup1() {
        return this.sup1;
    }

    public void setSup1(String sup1) {
        this.sup1 = sup1;
    }

    public String getSups() {
        return this.sups;
    }

    public void setSups(String sups) {
        this.sups = sups;
    }

    public String getSup2() {
        return this.sup2;
    }

    public void setSup2(String sup2) {
        this.sup2 = sup2;
    }

    public String getInf1() {
        return this.inf1;
    }

    public void setInf1(String inf1) {
        this.inf1 = inf1;
    }

    public String getInfs() {
        return this.infs;
    }

    public void setInfs(String infs) {
        this.infs = infs;
    }

    public String getInf2() {
        return this.inf2;
    }

    public void setInf2(String inf2) {
        this.inf2 = inf2;
    }

    public String getEgu1() {
        return this.egu1;
    }

    public void setEgu1(String egu1) {
        this.egu1 = egu1;
    }

    public String getEgus() {
        return this.egus;
    }

    public void setEgus(String egus) {
        this.egus = egus;
    }

    public String getEgu2() {
        return this.egu2;
    }

    public void setEgu2(String egu2) {
        this.egu2 = egu2;
    }

    public String getCs1() {
        return this.cs1;
    }

    public void setCs1(String cs1) {
        this.cs1 = cs1;
    }

    public String getCs2() {
        return this.cs2;
    }

    public void setCs2(String cs2) {
        this.cs2 = cs2;
    }

    public String getCs3() {
        return this.cs3;
    }

    public void setCs3(String cs3) {
        this.cs3 = cs3;
    }

    public String getSexe() {
        return this.sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public Long getAge1() {
        return this.age1;
    }

    public void setAge1(Long age1) {
        this.age1 = age1;
    }

    public Long getAge2() {
        return this.age2;
    }

    public void setAge2(Long age2) {
        this.age2 = age2;
    }

    public String getSit1() {
        return this.sit1;
    }

    public void setSit1(String sit1) {
        this.sit1 = sit1;
    }

    public String getSit2() {
        return this.sit2;
    }

    public void setSit2(String sit2) {
        this.sit2 = sit2;
    }

    public String getSit3() {
        return this.sit3;
    }

    public void setSit3(String sit3) {
        this.sit3 = sit3;
    }

    public String getSit4() {
        return this.sit4;
    }

    public void setSit4(String sit4) {
        this.sit4 = sit4;
    }

    public Long getNbe1() {
        return this.nbe1;
    }

    public void setNbe1(Long nbe1) {
        this.nbe1 = nbe1;
    }

    public Long getNbe2() {
        return this.nbe2;
    }

    public void setNbe2(Long nbe2) {
        this.nbe2 = nbe2;
    }

    public String getNat1() {
        return this.nat1;
    }

    public void setNat1(String nat1) {
        this.nat1 = nat1;
    }

    public String getNat2() {
        return this.nat2;
    }

    public void setNat2(String nat2) {
        this.nat2 = nat2;
    }

    public String getZca1() {
        return this.zca1;
    }

    public void setZca1(String zca1) {
        this.zca1 = zca1;
    }

    public String getZca2() {
        return this.zca2;
    }

    public void setZca2(String zca2) {
        this.zca2 = zca2;
    }

    public String getZca3() {
        return this.zca3;
    }

    public void setZca3(String zca3) {
        this.zca3 = zca3;
    }

    public String getZca4() {
        return this.zca4;
    }

    public void setZca4(String zca4) {
        this.zca4 = zca4;
    }

    public String getCat1() {
        return this.cat1;
    }

    public void setCat1(String cat1) {
        this.cat1 = cat1;
    }

    public String getCat2() {
        return this.cat2;
    }

    public void setCat2(String cat2) {
        this.cat2 = cat2;
    }

    public String getTyc1() {
        return this.tyc1;
    }

    public void setTyc1(String tyc1) {
        this.tyc1 = tyc1;
    }

    public String getTyc2() {
        return this.tyc2;
    }

    public void setTyc2(String tyc2) {
        this.tyc2 = tyc2;
    }

    public String getTyc3() {
        return this.tyc3;
    }

    public void setTyc3(String tyc3) {
        this.tyc3 = tyc3;
    }

    public String getTyc4() {
        return this.tyc4;
    }

    public void setTyc4(String tyc4) {
        this.tyc4 = tyc4;
    }

    public String getTyc5() {
        return this.tyc5;
    }

    public void setTyc5(String tyc5) {
        this.tyc5 = tyc5;
    }

    public String getTyc6() {
        return this.tyc6;
    }

    public void setTyc6(String tyc6) {
        this.tyc6 = tyc6;
    }

    public String getTyc7() {
        return this.tyc7;
    }

    public void setTyc7(String tyc7) {
        this.tyc7 = tyc7;
    }

    public String getTyc8() {
        return this.tyc8;
    }

    public void setTyc8(String tyc8) {
        this.tyc8 = tyc8;
    }

    public String getGra1() {
        return this.gra1;
    }

    public void setGra1(String gra1) {
        this.gra1 = gra1;
    }

    public String getGra2() {
        return this.gra2;
    }

    public void setGra2(String gra2) {
        this.gra2 = gra2;
    }

    public String getGra3() {
        return this.gra3;
    }

    public void setGra3(String gra3) {
        this.gra3 = gra3;
    }

    public String getGra4() {
        return this.gra4;
    }

    public void setGra4(String gra4) {
        this.gra4 = gra4;
    }

    public String getGra5() {
        return this.gra5;
    }

    public void setGra5(String gra5) {
        this.gra5 = gra5;
    }

    public String getGra6() {
        return this.gra6;
    }

    public void setGra6(String gra6) {
        this.gra6 = gra6;
    }

    public String getGra7() {
        return this.gra7;
    }

    public void setGra7(String gra7) {
        this.gra7 = gra7;
    }

    public String getGra8() {
        return this.gra8;
    }

    public void setGra8(String gra8) {
        this.gra8 = gra8;
    }

    public String getAvn() {
        return this.avn;
    }

    public void setAvn(String avn) {
        this.avn = avn;
    }

    public String getNiv11() {
        return this.niv11;
    }

    public void setNiv11(String niv11) {
        this.niv11 = niv11;
    }

    public String getNiv12() {
        return this.niv12;
    }

    public void setNiv12(String niv12) {
        this.niv12 = niv12;
    }

    public String getNiv13() {
        return this.niv13;
    }

    public void setNiv13(String niv13) {
        this.niv13 = niv13;
    }

    public String getNiv14() {
        return this.niv14;
    }

    public void setNiv14(String niv14) {
        this.niv14 = niv14;
    }

    public String getNiv21() {
        return this.niv21;
    }

    public void setNiv21(String niv21) {
        this.niv21 = niv21;
    }

    public String getNiv22() {
        return this.niv22;
    }

    public void setNiv22(String niv22) {
        this.niv22 = niv22;
    }

    public String getNiv23() {
        return this.niv23;
    }

    public void setNiv23(String niv23) {
        this.niv23 = niv23;
    }

    public String getNiv24() {
        return this.niv24;
    }

    public void setNiv24(String niv24) {
        this.niv24 = niv24;
    }

    public String getNiv31() {
        return this.niv31;
    }

    public void setNiv31(String niv31) {
        this.niv31 = niv31;
    }

    public String getNiv32() {
        return this.niv32;
    }

    public void setNiv32(String niv32) {
        this.niv32 = niv32;
    }

    public String getNiv33() {
        return this.niv33;
    }

    public void setNiv33(String niv33) {
        this.niv33 = niv33;
    }

    public String getNiv34() {
        return this.niv34;
    }

    public void setNiv34(String niv34) {
        this.niv34 = niv34;
    }

    public String getSynd() {
        return this.synd;
    }

    public void setSynd(String synd) {
        this.synd = synd;
    }

    public String getReg1() {
        return this.reg1;
    }

    public void setReg1(String reg1) {
        this.reg1 = reg1;
    }

    public String getReg2() {
        return this.reg2;
    }

    public void setReg2(String reg2) {
        this.reg2 = reg2;
    }

    public String getReg3() {
        return this.reg3;
    }

    public void setReg3(String reg3) {
        this.reg3 = reg3;
    }

    public String getReg4() {
        return this.reg4;
    }

    public void setReg4(String reg4) {
        this.reg4 = reg4;
    }

    public String getReg5() {
        return this.reg5;
    }

    public void setReg5(String reg5) {
        this.reg5 = reg5;
    }

    public String getReg6() {
        return this.reg6;
    }

    public void setReg6(String reg6) {
        this.reg6 = reg6;
    }

    public String getReg7() {
        return this.reg7;
    }

    public void setReg7(String reg7) {
        this.reg7 = reg7;
    }

    public String getReg8() {
        return this.reg8;
    }

    public void setReg8(String reg8) {
        this.reg8 = reg8;
    }

    public String getClas1() {
        return this.clas1;
    }

    public void setClas1(String clas1) {
        this.clas1 = clas1;
    }

    public String getClas2() {
        return this.clas2;
    }

    public void setClas2(String clas2) {
        this.clas2 = clas2;
    }

    public String getClas3() {
        return this.clas3;
    }

    public void setClas3(String clas3) {
        this.clas3 = clas3;
    }

    public String getClas4() {
        return this.clas4;
    }

    public void setClas4(String clas4) {
        this.clas4 = clas4;
    }

    public String getCfon() {
        return this.cfon;
    }

    public void setCfon(String cfon) {
        this.cfon = cfon;
    }

    public String getHif1() {
        return this.hif1;
    }

    public void setHif1(String hif1) {
        this.hif1 = hif1;
    }

    public String getHif2() {
        return this.hif2;
    }

    public void setHif2(String hif2) {
        this.hif2 = hif2;
    }

    public String getHif3() {
        return this.hif3;
    }

    public void setHif3(String hif3) {
        this.hif3 = hif3;
    }

    public String getHif4() {
        return this.hif4;
    }

    public void setHif4(String hif4) {
        this.hif4 = hif4;
    }

    public String getFon1() {
        return this.fon1;
    }

    public void setFon1(String fon1) {
        this.fon1 = fon1;
    }

    public String getFon2() {
        return this.fon2;
    }

    public void setFon2(String fon2) {
        this.fon2 = fon2;
    }

    public String getFon3() {
        return this.fon3;
    }

    public void setFon3(String fon3) {
        this.fon3 = fon3;
    }

    public String getFon4() {
        return this.fon4;
    }

    public void setFon4(String fon4) {
        this.fon4 = fon4;
    }

    public String getFon5() {
        return this.fon5;
    }

    public void setFon5(String fon5) {
        this.fon5 = fon5;
    }

    public String getFon6() {
        return this.fon6;
    }

    public void setFon6(String fon6) {
        this.fon6 = fon6;
    }

    public String getFon7() {
        return this.fon7;
    }

    public void setFon7(String fon7) {
        this.fon7 = fon7;
    }

    public String getFon8() {
        return this.fon8;
    }

    public void setFon8(String fon8) {
        this.fon8 = fon8;
    }

    public String getZl11() {
        return this.zl11;
    }

    public void setZl11(String zl11) {
        this.zl11 = zl11;
    }

    public String getZl12() {
        return this.zl12;
    }

    public void setZl12(String zl12) {
        this.zl12 = zl12;
    }

    public String getZl21() {
        return this.zl21;
    }

    public void setZl21(String zl21) {
        this.zl21 = zl21;
    }

    public String getZl22() {
        return this.zl22;
    }

    public void setZl22(String zl22) {
        this.zl22 = zl22;
    }

    public String getCais() {
        return this.cais;
    }

    public void setCais(String cais) {
        this.cais = cais;
    }

    public String getDnbp() {
        return this.dnbp;
    }

    public void setDnbp(String dnbp) {
        this.dnbp = dnbp;
    }

    public String getTxmt() {
        return this.txmt;
    }

    public void setTxmt(String txmt) {
        this.txmt = txmt;
    }

    public String getTrcu() {
        return this.trcu;
    }

    public void setTrcu(String trcu) {
        this.trcu = trcu;
    }

    public String getBasp() {
        return this.basp;
    }

    public void setBasp(String basp) {
        this.basp = basp;
    }

    public String getAbat() {
        return this.abat;
    }

    public void setAbat(String abat) {
        this.abat = abat;
    }

    public String getAbmx() {
        return this.abmx;
    }

    public void setAbmx(String abmx) {
        this.abmx = abmx;
    }

    public String getPmin() {
        return this.pmin;
    }

    public void setPmin(String pmin) {
        this.pmin = pmin;
    }

    public String getPmax() {
        return this.pmax;
    }

    public void setPmax(String pmax) {
        this.pmax = pmax;
    }

    public BigDecimal getPcab() {
        return this.pcab;
    }

    public void setPcab(BigDecimal pcab) {
        this.pcab = pcab;
    }

    public String getPdap() {
        return this.pdap;
    }

    public void setPdap(String pdap) {
        this.pdap = pdap;
    }

    public Date getDtcr() {
        return this.dtcr;
    }

    public void setDtcr(Date dtcr) {
        this.dtcr = dtcr;
    }

    public Date getDtdm() {
        return this.dtdm;
    }

    public void setDtdm(Date dtdm) {
        this.dtdm = dtdm;
    }

    public String getComp() {
        return this.comp;
    }

    public void setComp(String comp) {
        this.comp = comp;
    }

    public String getCper() {
        return this.cper;
    }

    public void setCper(String cper) {
        this.cper = cper;
    }

    public String getDe01() {
        return this.de01;
    }

    public void setDe01(String de01) {
        this.de01 = de01;
    }

    public String getDe02() {
        return this.de02;
    }

    public void setDe02(String de02) {
        this.de02 = de02;
    }

    public String getDe03() {
        return this.de03;
    }

    public void setDe03(String de03) {
        this.de03 = de03;
    }

    public String getDe04() {
        return this.de04;
    }

    public void setDe04(String de04) {
        this.de04 = de04;
    }

    public String getDe05() {
        return this.de05;
    }

    public void setDe05(String de05) {
        this.de05 = de05;
    }

    public String getDe06() {
        return this.de06;
    }

    public void setDe06(String de06) {
        this.de06 = de06;
    }

    public String getDe07() {
        return this.de07;
    }

    public void setDe07(String de07) {
        this.de07 = de07;
    }

    public String getDe08() {
        return this.de08;
    }

    public void setDe08(String de08) {
        this.de08 = de08;
    }

    public String getDe09() {
        return this.de09;
    }

    public void setDe09(String de09) {
        this.de09 = de09;
    }

    public String getDe10() {
        return this.de10;
    }

    public void setDe10(String de10) {
        this.de10 = de10;
    }

    public String getDe11() {
        return this.de11;
    }

    public void setDe11(String de11) {
        this.de11 = de11;
    }

    public String getDe12() {
        return this.de12;
    }

    public void setDe12(String de12) {
        this.de12 = de12;
    }

    public String getDe13() {
        return this.de13;
    }

    public void setDe13(String de13) {
        this.de13 = de13;
    }

    public String getDe14() {
        return this.de14;
    }

    public void setDe14(String de14) {
        this.de14 = de14;
    }

    public String getDe15() {
        return this.de15;
    }

    public void setDe15(String de15) {
        this.de15 = de15;
    }

    public String getDe16() {
        return this.de16;
    }

    public void setDe16(String de16) {
        this.de16 = de16;
    }

    public String getDe17() {
        return this.de17;
    }

    public void setDe17(String de17) {
        this.de17 = de17;
    }

    public String getDe18() {
        return this.de18;
    }

    public void setDe18(String de18) {
        this.de18 = de18;
    }

    public String getDe19() {
        return this.de19;
    }

    public void setDe19(String de19) {
        this.de19 = de19;
    }

    public String getDe20() {
        return this.de20;
    }

    public void setDe20(String de20) {
        this.de20 = de20;
    }

    public String getCr01() {
        return this.cr01;
    }

    public void setCr01(String cr01) {
        this.cr01 = cr01;
    }

    public String getCr02() {
        return this.cr02;
    }

    public void setCr02(String cr02) {
        this.cr02 = cr02;
    }

    public String getCr03() {
        return this.cr03;
    }

    public void setCr03(String cr03) {
        this.cr03 = cr03;
    }

    public String getCr04() {
        return this.cr04;
    }

    public void setCr04(String cr04) {
        this.cr04 = cr04;
    }

    public String getCr05() {
        return this.cr05;
    }

    public void setCr05(String cr05) {
        this.cr05 = cr05;
    }

    public String getFbas() {
        return this.fbas;
    }

    public void setFbas(String fbas) {
        this.fbas = fbas;
    }

    public String getTbas() {
        return this.tbas;
    }

    public void setTbas(String tbas) {
        this.tbas = tbas;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }
    
    public String getFormule() {
        return this.formule;
    }

    public void setFormule(String formule) {
        this.formule = formule;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("comp_id", getComp_id())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof ClsParubqClone) ) return false;
        ClsParubqClone castOther = (ClsParubqClone) other;
        return new EqualsBuilder()
            .append(this.getComp_id(), castOther.getComp_id())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getComp_id())
            .toHashCode();
    }
}
