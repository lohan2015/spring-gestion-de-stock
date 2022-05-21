package com.kinart.paie.business.services.calcul;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.kinart.paie.business.model.Salarie;

/**
 * Représente la table des agents.
 * @author c.mbassi
 *
 */
public class ClsInfoSalaryClone implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private ClsInfoSalaryClonePK comp_id;

    /** nullable persistent field */
    private String niv1;

    /** nullable persistent field */
    private String niv2;

    /** nullable persistent field */
    private String niv3;

    /** nullable persistent field */
    private String cals;

    /** nullable persistent field */
    private String clas;

    /** nullable persistent field */
    private String nom;

    /** nullable persistent field */
    private String pren;

    /** nullable persistent field */
    private String sexe;

    /** nullable persistent field */
    private Date dtna;

    /** nullable persistent field */
    private String nato;

    /** nullable persistent field */
    private String sitf;

    /** nullable persistent field */
    private Integer nbcj;

    /** nullable persistent field */
    private Integer nbef;

    /** nullable persistent field */
    private Integer nbec;

    /** nullable persistent field */
    private Integer nbfe;

    /** nullable persistent field */
    private BigDecimal nbpt;

    /** nullable persistent field */
    private String modp;

    /** nullable persistent field */
    private String banq;

    /** nullable persistent field */
    private String guic;

    /** nullable persistent field */
    private String comp;

    /** nullable persistent field */
    private String cle;

    /** nullable persistent field */
    private String ccpt;

    /** nullable persistent field */
    private String bqso;

    /** nullable persistent field */
    private String vild;

    /** nullable persistent field */
    private Date dcre;

    /** nullable persistent field */
    private Date dmaj;

    /** nullable persistent field */
    private String cat;

    /** nullable persistent field */
    private String ech;

    /** nullable persistent field */
    private String grad;

    /** nullable persistent field */
    private String fonc;

    /** nullable persistent field */
    private String afec;

    /** nullable persistent field */
    private String codf;

    /** nullable persistent field */
    private Integer indi;

    /** nullable persistent field */
    private String ctat;

    /** nullable persistent field */
    private BigDecimal tinp;

    /** nullable persistent field */
    private String synd;

    /** nullable persistent field */
    private String hifo;

    /** nullable persistent field */
    private String zli1;

    /** nullable persistent field */
    private String zli2;

    /** nullable persistent field */
    private Date dtes;

    /** nullable persistent field */
    private Date ddca;

    /** nullable persistent field */
    private String typc;

    /** nullable persistent field */
    private String avn1;

    /** nullable persistent field */
    private String avn2;

    /** nullable persistent field */
    private String avn3;

    /** nullable persistent field */
    private String avn4;

    /** nullable persistent field */
    private String avn5;

    /** nullable persistent field */
    private String avn6;

    /** nullable persistent field */
    private String avn7;

    /** nullable persistent field */
    private String regi;

    /** nullable persistent field */
    private String zres;

    /** nullable persistent field */
    private String dmst;

    /** nullable persistent field */
    private Integer npie;

    /** nullable persistent field */
    private String mrrx;

    /** nullable persistent field */
    private Date dmrr;

    /** nullable persistent field */
    private String mtfr;

    /** nullable persistent field */
    private String lieu;

    /** nullable persistent field */
    private String cods;

    /** nullable persistent field */
    private String pnet;

    /** nullable persistent field */
    private BigDecimal snet;

    /** nullable persistent field */
    private String devp;

    /** nullable persistent field */
    private String equi;

    /** nullable persistent field */
    private String dels;

    /** nullable persistent field */
    private String tits;

    /** nullable persistent field */
    private Date dtit;

    /** nullable persistent field */
    private Date depr;

    /** nullable persistent field */
    private Date decc;

    /** nullable persistent field */
    private BigDecimal japa;

    /** nullable persistent field */
    private BigDecimal dapa;

    /** nullable persistent field */
    private BigDecimal japec;

    /** nullable persistent field */
    private BigDecimal dapec;

    /** nullable persistent field */
    private BigDecimal jded;

    /** nullable persistent field */
    private BigDecimal dded;

    /** nullable persistent field */
    private BigDecimal jrla;

    /** nullable persistent field */
    private BigDecimal jrlec;

    /** nullable persistent field */
    private BigDecimal nbjcf;

    /** nullable persistent field */
    private BigDecimal nbjaf;

    /** nullable persistent field */
    private Date ddcf;

    /** nullable persistent field */
    private Date dfcf;

    /** nullable persistent field */
    private BigDecimal mtcf;

    /** nullable persistent field */
    private String pmcf;

    /** nullable persistent field */
    private BigDecimal nbjse;

    /** nullable persistent field */
    private BigDecimal nbjsa;

    /** nullable persistent field */
    private String nmjf;

    /** nullable persistent field */
    private String adr1;

    /** nullable persistent field */
    private String adr2;

    /** nullable persistent field */
    private String adr3;

    /** nullable persistent field */
    private String adr4;

    /** nullable persistent field */
    private String bpos;

    /** nullable persistent field */
    private String ntel;

    /** nullable persistent field */
    private String pnai;

    /** nullable persistent field */
    private String comm;

    /** nullable persistent field */
    private String pbpe;

    /** nullable persistent field */
    private Date dchg;

    /** nullable persistent field */
    private String mchg;

    /** nullable persistent field */
    private Date dfes;

    /** nullable persistent field */
    private String stor;

    /** nullable persistent field */
    private BigDecimal nbjtr;

    /** nullable persistent field */
    private Date drtcg;

    /** nullable persistent field */
    private Date ddenv;

    /** nullable persistent field */
    private Date drenv;

    /** nullable persistent field */
    private String noss;

    /** nullable persistent field */
    private String cont;

    /** nullable persistent field */
    private BigDecimal nbjsm;

    /** nullable persistent field */
    private String sana;

    /** nullable persistent field */
    private String tyfo1;

    /** nullable persistent field */
    private String tyfo2;

    /** nullable persistent field */
    private String nifo;

    /** nullable persistent field */
    private Date dchf;

    /** nullable persistent field */
    private String note;
	private String codeposte;
	private String codesite;
	private String lemb;
	private String lnai;
	private String metier;
	private String zli10;
	private String zli3;
	private String zli4;
	private String zli5;
	private String zli6;
	private String zli7;
	private String zli8;
	private String zli9;
    

    public ClsInfoSalaryClonePK getComp_id() {
		return comp_id;
	}

	public void setComp_id(ClsInfoSalaryClonePK comp_id) {
		this.comp_id = comp_id;
	}

	/** full constructor */
    public ClsInfoSalaryClone(Salarie salaryInfo) {
    	this.comp_id = new ClsInfoSalaryClonePK();
        this.comp_id.setCdos(String.valueOf(salaryInfo.getIdentreprise()));
        this.comp_id.setNmat(salaryInfo.getNmat());
        
        this.adr1 =  salaryInfo.getAdr1();
        this.adr2 =  salaryInfo.getAdr2();
        this.adr3 =  salaryInfo.getAdr3();
        this.adr4 =  salaryInfo.getAdr4();
        this.afec =  salaryInfo.getAfec();
        this.avn1 =  salaryInfo.getAvn1();
        this.avn2 =  salaryInfo.getAvn2();
        this.avn3 =  salaryInfo.getAvn3();
        this.avn4 =  salaryInfo.getAvn4();
        this.avn5 =  salaryInfo.getAvn5();
        this.avn6 =  salaryInfo.getAvn6();
        this.avn7 =  salaryInfo.getAvn7();
        this.banq =  salaryInfo.getBanq();
        this.bpos =  salaryInfo.getBpos();
        this.bqso =  salaryInfo.getBqso();
        this.cals =  salaryInfo.getCals();
        this.cat =  salaryInfo.getCat();
        this.ccpt =  salaryInfo.getCcpt();
        this.clas =  salaryInfo.getClas();
        this.cle =  salaryInfo.getCle();
        this.codeposte =  salaryInfo.getCodeposte();
        this.codesite =  salaryInfo.getCodesite();
        this.codf =  salaryInfo.getCodf();
        this.cods =  salaryInfo.getCods();
        this.comm =  salaryInfo.getComm();
        this.comp =  salaryInfo.getComp();
        this.cont =  salaryInfo.getCont();
        this.ctat =  salaryInfo.getCtat();
        this.dapa =  salaryInfo.getDapa();
        this.dapec =  salaryInfo.getDapec();
        this.dchf =  salaryInfo.getDchf();
        this.dchg =  salaryInfo.getDchg();
        this.dcre =  Date.from(salaryInfo.getCreationDate());
        this.ddca =  salaryInfo.getDdca();
        this.ddcf =  salaryInfo.getDdcf();
        this.dded =  salaryInfo.getDded();
        this.ddenv =  salaryInfo.getDdenv();
        this.decc =  salaryInfo.getDecc();
        this.dels =  salaryInfo.getDels();
        this.depr =  salaryInfo.getDepr();
        this.devp =  salaryInfo.getDevp();
        this.dfcf =  salaryInfo.getDfcf();
        this.dfes =  salaryInfo.getDfes();
        this.dmaj =  Date.from(salaryInfo.getLastModifiedDate());
        this.dmrr =  salaryInfo.getDmrr();
        this.dmst =  salaryInfo.getDmst();
        this.drenv =  salaryInfo.getDrenv();
        this.drtcg =  salaryInfo.getDrtcg();
        this.dtes =  salaryInfo.getDtes();
        this.dtit =  salaryInfo.getDtit();
        this.dtna =  salaryInfo.getDtna();
        this.ech =  salaryInfo.getEch();
        this.equi =  salaryInfo.getEqui();
        this.fonc =  salaryInfo.getFonc();
        this.grad =  salaryInfo.getGrad();
        this.guic =  salaryInfo.getGuic();
        this.hifo =  salaryInfo.getHifo();
        this.indi =  salaryInfo.getIndi();
        this.japa =  salaryInfo.getJapa();
        this.japec =  salaryInfo.getJapec();
        this.jded =  salaryInfo.getJded();
        this.jrla =  salaryInfo.getJrla();
        this.jrlec =  salaryInfo.getJrlec();
        this.lemb =  salaryInfo.getLemb();
        this.lieu =  salaryInfo.getLieu();
        this.lnai =  salaryInfo.getLnai();
        this.mchg =  salaryInfo.getMchg();
        this.modp =  salaryInfo.getModp();
        this.mrrx =  salaryInfo.getMrrx();
        this.mtcf =  salaryInfo.getMtcf();
        this.mtfr =  salaryInfo.getMtfr();
        this.nato =  salaryInfo.getNato();
        this.nbcj =  salaryInfo.getNbcj();
        this.nbec =  salaryInfo.getNbec();
        this.nbef =  salaryInfo.getNbef();
        this.nbfe =  salaryInfo.getNbfe();
        this.nbjaf =  salaryInfo.getNbjaf();
        this.nbjcf =  salaryInfo.getNbjcf();
        this.nbjsa =  salaryInfo.getNbjsa();
        this.nbjse =  salaryInfo.getNbjse();
        this.nbjsm =  salaryInfo.getNbjsm();
        this.nbjtr =  salaryInfo.getNbjtr();
        this.nbpt =  salaryInfo.getNbpt();
        this.nifo =  salaryInfo.getNifo();
        this.niv1 =  salaryInfo.getNiv1();
        this.niv2 =  salaryInfo.getNiv2();
        this.niv3 =  salaryInfo.getNiv3();
        this.nmjf =  salaryInfo.getNmjf();
        this.nom =  salaryInfo.getNom();
        this.noss =  salaryInfo.getNoss();
        this.note =  salaryInfo.getNote();
        this.npie =  salaryInfo.getNpie();
        this.ntel =  salaryInfo.getNtel();
        this.pbpe =  salaryInfo.getPbpe();
        this.pmcf =  salaryInfo.getPmcf();
        this.pnai =  salaryInfo.getPnai();
        this.pnet =  salaryInfo.getPnet();
        this.pren =  salaryInfo.getPren();
        this.regi =  salaryInfo.getRegi();
        this.sana =  salaryInfo.getSana();
        this.sexe =  salaryInfo.getSexe();
        this.sitf =  salaryInfo.getSitf();
        this.snet =  salaryInfo.getSnet();
        this.stor =  salaryInfo.getStor();
        this.synd =  salaryInfo.getSynd();
        this.tinp =  salaryInfo.getTinp();
        this.tits =  salaryInfo.getTits();
        this.tyfo1 =  salaryInfo.getTyfo1();
        this.tyfo2 =  salaryInfo.getTyfo2();
        this.typc =  salaryInfo.getTypc();
        this.vild =  salaryInfo.getVild();
        this.zli1 =  salaryInfo.getZli1();
        this.zli10 =  salaryInfo.getZli10();
        this.zli2 =  salaryInfo.getZli2();
        this.zli3 =  salaryInfo.getZli3();
        this.zli4 =  salaryInfo.getZli4();
        this.zli5 =  salaryInfo.getZli5();
        this.zli6 =  salaryInfo.getZli6();
        this.zli7 =  salaryInfo.getZli7();
        this.zli8 =  salaryInfo.getZli8();
        this.zli9 =  salaryInfo.getZli9();
        this.zres =  salaryInfo.getZres();
        
    }

    /** default constructor */
    public ClsInfoSalaryClone() {
    }

    /** minimal constructor */
    public ClsInfoSalaryClone(String cdos, String nmat) {
        this.comp_id = new ClsInfoSalaryClonePK();
    	this.comp_id.setCdos(cdos);
        this.comp_id.setNmat(nmat);
    }

    public String getNiv1() {
        return this.niv1;
    }

    public void setNiv1(String niv1) {
        this.niv1 = niv1;
    }

    public String getNiv2() {
        return this.niv2;
    }

    public void setNiv2(String niv2) {
        this.niv2 = niv2;
    }

    public String getNiv3() {
        return this.niv3;
    }

    public void setNiv3(String niv3) {
        this.niv3 = niv3;
    }

    public String getCals() {
        return this.cals;
    }

    public void setCals(String cals) {
        this.cals = cals;
    }

    public String getClas() {
        return this.clas;
    }

    public void setClas(String clas) {
        this.clas = clas;
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPren() {
        return this.pren;
    }

    public void setPren(String pren) {
        this.pren = pren;
    }

    public String getSexe() {
        return this.sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public Date getDtna() {
        return this.dtna;
    }

    public void setDtna(Date dtna) {
        this.dtna = dtna;
    }

    public String getNato() {
        return this.nato;
    }

    public void setNato(String nato) {
        this.nato = nato;
    }

    public String getSitf() {
        return this.sitf;
    }

    public void setSitf(String sitf) {
        this.sitf = sitf;
    }

    public Integer getNbcj() {
        return this.nbcj;
    }

    public void setNbcj(Integer nbcj) {
        this.nbcj = nbcj;
    }

    public Integer getNbef() {
        return this.nbef;
    }

    public void setNbef(Integer nbef) {
        this.nbef = nbef;
    }

    public Integer getNbec() {
        return this.nbec;
    }

    public void setNbec(Integer nbec) {
        this.nbec = nbec;
    }

    public Integer getNbfe() {
        return this.nbfe;
    }

    public void setNbfe(Integer nbfe) {
        this.nbfe = nbfe;
    }

    public BigDecimal getNbpt() {
        return this.nbpt;
    }

    public void setNbpt(BigDecimal nbpt) {
        this.nbpt = nbpt;
    }

    public String getModp() {
        return this.modp;
    }

    public void setModp(String modp) {
        this.modp = modp;
    }

    public String getBanq() {
        return this.banq;
    }

    public void setBanq(String banq) {
        this.banq = banq;
    }

    public String getGuic() {
        return this.guic;
    }

    public void setGuic(String guic) {
        this.guic = guic;
    }

    public String getComp() {
        return this.comp;
    }

    public void setComp(String comp) {
        this.comp = comp;
    }

    public String getCle() {
        return this.cle;
    }

    public void setCle(String cle) {
        this.cle = cle;
    }

    public String getCcpt() {
        return this.ccpt;
    }

    public void setCcpt(String ccpt) {
        this.ccpt = ccpt;
    }

    public String getBqso() {
        return this.bqso;
    }

    public void setBqso(String bqso) {
        this.bqso = bqso;
    }

    public String getVild() {
        return this.vild;
    }

    public void setVild(String vild) {
        this.vild = vild;
    }

    public Date getDcre() {
        return this.dcre;
    }

    public void setDcre(Date dcre) {
        this.dcre = dcre;
    }

    public Date getDmaj() {
        return this.dmaj;
    }

    public void setDmaj(Date dmaj) {
        this.dmaj = dmaj;
    }

    public String getCat() {
        return this.cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getEch() {
        return this.ech;
    }

    public void setEch(String ech) {
        this.ech = ech;
    }

    public String getGrad() {
        return this.grad;
    }

    public void setGrad(String grad) {
        this.grad = grad;
    }

    public String getFonc() {
        return this.fonc;
    }

    public void setFonc(String fonc) {
        this.fonc = fonc;
    }

    public String getAfec() {
        return this.afec;
    }

    public void setAfec(String afec) {
        this.afec = afec;
    }

    public String getCodf() {
        return this.codf;
    }

    public void setCodf(String codf) {
        this.codf = codf;
    }

    public Integer getIndi() {
        return this.indi;
    }

    public void setIndi(Integer indi) {
        this.indi = indi;
    }

    public String getCtat() {
        return this.ctat;
    }

    public void setCtat(String ctat) {
        this.ctat = ctat;
    }

    public BigDecimal getTinp() {
        return this.tinp;
    }

    public void setTinp(BigDecimal tinp) {
        this.tinp = tinp;
    }

    public String getSynd() {
        return this.synd;
    }

    public void setSynd(String synd) {
        this.synd = synd;
    }

    public String getHifo() {
        return this.hifo;
    }

    public void setHifo(String hifo) {
        this.hifo = hifo;
    }

    public String getZli1() {
        return this.zli1;
    }

    public void setZli1(String zli1) {
        this.zli1 = zli1;
    }

    public String getZli2() {
        return this.zli2;
    }

    public void setZli2(String zli2) {
        this.zli2 = zli2;
    }

    public Date getDtes() {
        return this.dtes;
    }

    public void setDtes(Date dtes) {
        this.dtes = dtes;
    }

    public Date getDdca() {
        return this.ddca;
    }

    public void setDdca(Date ddca) {
        this.ddca = ddca;
    }

    public String getTypc() {
        return this.typc;
    }

    public void setTypc(String typc) {
        this.typc = typc;
    }

    public String getAvn1() {
        return this.avn1;
    }

    public void setAvn1(String avn1) {
        this.avn1 = avn1;
    }

    public String getAvn2() {
        return this.avn2;
    }

    public void setAvn2(String avn2) {
        this.avn2 = avn2;
    }

    public String getAvn3() {
        return this.avn3;
    }

    public void setAvn3(String avn3) {
        this.avn3 = avn3;
    }

    public String getAvn4() {
        return this.avn4;
    }

    public void setAvn4(String avn4) {
        this.avn4 = avn4;
    }

    public String getAvn5() {
        return this.avn5;
    }

    public void setAvn5(String avn5) {
        this.avn5 = avn5;
    }

    public String getAvn6() {
        return this.avn6;
    }

    public void setAvn6(String avn6) {
        this.avn6 = avn6;
    }

    public String getAvn7() {
        return this.avn7;
    }

    public void setAvn7(String avn7) {
        this.avn7 = avn7;
    }

    public String getRegi() {
        return this.regi;
    }

    public void setRegi(String regi) {
        this.regi = regi;
    }

    public String getZres() {
        return this.zres;
    }

    public void setZres(String zres) {
        this.zres = zres;
    }

    public String getDmst() {
        return this.dmst;
    }

    public void setDmst(String dmst) {
        this.dmst = dmst;
    }

    public Integer getNpie() {
        return this.npie;
    }

    public void setNpie(Integer npie) {
        this.npie = npie;
    }

    public String getMrrx() {
        return this.mrrx;
    }

    public void setMrrx(String mrrx) {
        this.mrrx = mrrx;
    }

    public Date getDmrr() {
        return this.dmrr;
    }

    public void setDmrr(Date dmrr) {
        this.dmrr = dmrr;
    }

    public String getMtfr() {
        return this.mtfr;
    }

    public void setMtfr(String mtfr) {
        this.mtfr = mtfr;
    }

    public String getLieu() {
        return this.lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getCods() {
        return this.cods;
    }

    public void setCods(String cods) {
        this.cods = cods;
    }

    public String getPnet() {
        return this.pnet;
    }

    public void setPnet(String pnet) {
        this.pnet = pnet;
    }

    public BigDecimal getSnet() {
        return this.snet;
    }

    public void setSnet(BigDecimal snet) {
        this.snet = snet;
    }

    public String getDevp() {
        return this.devp;
    }

    public void setDevp(String devp) {
        this.devp = devp;
    }

    public String getEqui() {
        return this.equi;
    }

    public void setEqui(String equi) {
        this.equi = equi;
    }

    public String getDels() {
        return this.dels;
    }

    public void setDels(String dels) {
        this.dels = dels;
    }

    public String getTits() {
        return this.tits;
    }

    public void setTits(String tits) {
        this.tits = tits;
    }

    public Date getDtit() {
        return this.dtit;
    }

    public void setDtit(Date dtit) {
        this.dtit = dtit;
    }

    public Date getDepr() {
        return this.depr;
    }

    public void setDepr(Date depr) {
        this.depr = depr;
    }

    public Date getDecc() {
        return this.decc;
    }

    public void setDecc(Date decc) {
        this.decc = decc;
    }

    public BigDecimal getJapa() {
        return this.japa;
    }

    public void setJapa(BigDecimal japa) {
        this.japa = japa;
    }

    public BigDecimal getDapa() {
        return this.dapa;
    }

    public void setDapa(BigDecimal dapa) {
        this.dapa = dapa;
    }

    public BigDecimal getJapec() {
        return this.japec;
    }

    public void setJapec(BigDecimal japec) {
        this.japec = japec;
    }

    public BigDecimal getDapec() {
        return this.dapec;
    }

    public void setDapec(BigDecimal dapec) {
        this.dapec = dapec;
    }

    public BigDecimal getJded() {
        return this.jded;
    }

    public void setJded(BigDecimal jded) {
        this.jded = jded;
    }

    public BigDecimal getDded() {
        return this.dded;
    }

    public void setDded(BigDecimal dded) {
        this.dded = dded;
    }

    public BigDecimal getJrla() {
        return this.jrla;
    }

    public void setJrla(BigDecimal jrla) {
        this.jrla = jrla;
    }

    public BigDecimal getJrlec() {
        return this.jrlec;
    }

    public void setJrlec(BigDecimal jrlec) {
        this.jrlec = jrlec;
    }

    public BigDecimal getNbjcf() {
        return this.nbjcf;
    }

    public void setNbjcf(BigDecimal nbjcf) {
        this.nbjcf = nbjcf;
    }

    public BigDecimal getNbjaf() {
        return this.nbjaf;
    }

    public void setNbjaf(BigDecimal nbjaf) {
        this.nbjaf = nbjaf;
    }

    public Date getDdcf() {
        return this.ddcf;
    }

    public void setDdcf(Date ddcf) {
        this.ddcf = ddcf;
    }

    public Date getDfcf() {
        return this.dfcf;
    }

    public void setDfcf(Date dfcf) {
        this.dfcf = dfcf;
    }

    public BigDecimal getMtcf() {
        return this.mtcf;
    }

    public void setMtcf(BigDecimal mtcf) {
        this.mtcf = mtcf;
    }

    public String getPmcf() {
        return this.pmcf;
    }

    public void setPmcf(String pmcf) {
        this.pmcf = pmcf;
    }

    public BigDecimal getNbjse() {
        return this.nbjse;
    }

    public void setNbjse(BigDecimal nbjse) {
        this.nbjse = nbjse;
    }

    public BigDecimal getNbjsa() {
        return this.nbjsa;
    }

    public void setNbjsa(BigDecimal nbjsa) {
        this.nbjsa = nbjsa;
    }

    public String getNmjf() {
        return this.nmjf;
    }

    public void setNmjf(String nmjf) {
        this.nmjf = nmjf;
    }

    public String getAdr1() {
        return this.adr1;
    }

    public void setAdr1(String adr1) {
        this.adr1 = adr1;
    }

    public String getAdr2() {
        return this.adr2;
    }

    public void setAdr2(String adr2) {
        this.adr2 = adr2;
    }

    public String getAdr3() {
        return this.adr3;
    }

    public void setAdr3(String adr3) {
        this.adr3 = adr3;
    }

    public String getAdr4() {
        return this.adr4;
    }

    public void setAdr4(String adr4) {
        this.adr4 = adr4;
    }

    public String getBpos() {
        return this.bpos;
    }

    public void setBpos(String bpos) {
        this.bpos = bpos;
    }

    public String getNtel() {
        return this.ntel;
    }

    public void setNtel(String ntel) {
        this.ntel = ntel;
    }

    public String getPnai() {
        return this.pnai;
    }

    public void setPnai(String pnai) {
        this.pnai = pnai;
    }

    public String getComm() {
        return this.comm;
    }

    public void setComm(String comm) {
        this.comm = comm;
    }

    public String getPbpe() {
        return this.pbpe;
    }

    public void setPbpe(String pbpe) {
        this.pbpe = pbpe;
    }

    public Date getDchg() {
        return this.dchg;
    }

    public void setDchg(Date dchg) {
        this.dchg = dchg;
    }

    public String getMchg() {
        return this.mchg;
    }

    public void setMchg(String mchg) {
        this.mchg = mchg;
    }

    public Date getDfes() {
        return this.dfes;
    }

    public void setDfes(Date dfes) {
        this.dfes = dfes;
    }

    public String getStor() {
        return this.stor;
    }

    public void setStor(String stor) {
        this.stor = stor;
    }

    public BigDecimal getNbjtr() {
        return this.nbjtr;
    }

    public void setNbjtr(BigDecimal nbjtr) {
        this.nbjtr = nbjtr;
    }

    public Date getDrtcg() {
        return this.drtcg;
    }

    public void setDrtcg(Date drtcg) {
        this.drtcg = drtcg;
    }

    public Date getDdenv() {
        return this.ddenv;
    }

    public void setDdenv(Date ddenv) {
        this.ddenv = ddenv;
    }

    public Date getDrenv() {
        return this.drenv;
    }

    public void setDrenv(Date drenv) {
        this.drenv = drenv;
    }

    public String getNoss() {
        return this.noss;
    }

    public void setNoss(String noss) {
        this.noss = noss;
    }

    public String getCont() {
        return this.cont;
    }

    public void setCont(String cont) {
        this.cont = cont;
    }

    public BigDecimal getNbjsm() {
        return this.nbjsm;
    }

    public void setNbjsm(BigDecimal nbjsm) {
        this.nbjsm = nbjsm;
    }

    public String getSana() {
        return this.sana;
    }

    public void setSana(String sana) {
        this.sana = sana;
    }

    public String getTyfo1() {
        return this.tyfo1;
    }

    public void setTyfo1(String tyfo1) {
        this.tyfo1 = tyfo1;
    }

    public String getTyfo2() {
        return this.tyfo2;
    }

    public void setTyfo2(String tyfo2) {
        this.tyfo2 = tyfo2;
    }

    public String getNifo() {
        return this.nifo;
    }

    public void setNifo(String nifo) {
        this.nifo = nifo;
    }

    public Date getDchf() {
        return this.dchf;
    }

    public void setDchf(Date dchf) {
        this.dchf = dchf;
    }

    public String getNote() {
        return this.note;
    }
    
    

    public String getCodeposte()
	{
		return codeposte;
	}

	public void setCodeposte(String codeposte)
	{
		this.codeposte = codeposte;
	}

	public String getCodesite()
	{
		return codesite;
	}

	public void setCodesite(String codesite)
	{
		this.codesite = codesite;
	}

	public String getLemb()
	{
		return lemb;
	}

	public void setLemb(String lemb)
	{
		this.lemb = lemb;
	}

	public String getLnai()
	{
		return lnai;
	}

	public void setLnai(String lnai)
	{
		this.lnai = lnai;
	}

	public String getMetier()
	{
		return metier;
	}

	public void setMetier(String metier)
	{
		this.metier = metier;
	}

	public String getZli10()
	{
		return zli10;
	}

	public void setZli10(String zli10)
	{
		this.zli10 = zli10;
	}

	public String getZli3()
	{
		return zli3;
	}

	public void setZli3(String zli3)
	{
		this.zli3 = zli3;
	}

	public String getZli4()
	{
		return zli4;
	}

	public void setZli4(String zli4)
	{
		this.zli4 = zli4;
	}

	public String getZli5()
	{
		return zli5;
	}

	public void setZli5(String zli5)
	{
		this.zli5 = zli5;
	}

	public String getZli6()
	{
		return zli6;
	}

	public void setZli6(String zli6)
	{
		this.zli6 = zli6;
	}

	public String getZli7()
	{
		return zli7;
	}

	public void setZli7(String zli7)
	{
		this.zli7 = zli7;
	}

	public String getZli8()
	{
		return zli8;
	}

	public void setZli8(String zli8)
	{
		this.zli8 = zli8;
	}

	public String getZli9()
	{
		return zli9;
	}

	public void setZli9(String zli9)
	{
		this.zli9 = zli9;
	}

	public void setNote(String note) {
        this.note = note;
    }

    public String toString() {
        return null;//new ToStringBuilder(this)
        //.append("comp_id", getComp_id())
        // .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof ClsInfoSalaryClone) ) return false;
        ClsInfoSalaryClone castOther = (ClsInfoSalaryClone) other;
        return true;//new EqualsBuilder()
        //.append(this.getComp_id(), castOther.getComp_id())
        //.isEquals();
    }

    public int hashCode() {
        return 0;//new HashCodeBuilder()
        //.append(getComp_id())
        //.toHashCode();
    }
    
    public static ClsInfoSalaryClone clone(Salarie salaryInfo){
    	ClsInfoSalaryClone salary = new ClsInfoSalaryClone(salaryInfo);
    	//
    	return salary;
    }
}
