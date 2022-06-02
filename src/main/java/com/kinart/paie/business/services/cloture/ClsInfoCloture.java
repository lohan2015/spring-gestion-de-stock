package com.kinart.paie.business.services.cloture;

import com.kinart.paie.business.services.utils.ParameterUtil;

public class ClsInfoCloture {
	
	private String aamm;
	private int nbul;
	private String datecompta;	
	private String datevaleur;
	private String mvtcomptaunique;	
	private String miseajouruniquement;
	private String cdos;
	
	private String filepath;
	
	private String actiontoexec = ClsInfoCloture.ALL;
	
	private String oninit;
	
	private String datesupcumul;
	private String delaisupsalarie;
	private String if_sup_sal;
	
	private boolean mvtcomptauniquement = false;
	
	private boolean boolmiseajouruniquement = false;
	
	private boolean boolif_sup_sal = false;
	
	public static String ALL="ALL";
	public static String CENTRA="CENTRA";
	public static String HISTORIQUE="HISTO";
	public static String MAJ="MAJ";
	public static String MAJ_SAN_HISTORIQUE="MAJSANSHISTO";
	public static String GENFICASCII="GENFICASCII";

       
    /** default constructor */
    public ClsInfoCloture(String cdos,String aamm,int nbul, String datecompta , String datevaleur, String mvtcomptaunique, String miseajouruniquement) {
    	setAamm(aamm);
    	setNbul(nbul);
    	setDatecompta(datecompta);
    	setDatevaleur(datevaleur);
    	setMvtcomptaunique(mvtcomptaunique);
    	setMiseajouruniquement(miseajouruniquement);
    	setCdos(cdos);
    }
    
    /** default constructor */
    public ClsInfoCloture(String cdos,String datesupcumul,String delaisupsalarie) {
    	setCdos(cdos);
    }
    public ClsInfoCloture() {
    	setBoolmiseajouruniquement(false);
    	setMvtcomptauniquement(false);
    	setBoolif_sup_sal(false);
    }

	public String getAamm() {
		return aamm;
	}

	public void setAamm(String aamm) {
		this.aamm = aamm;
	}

	
	public String getCdos() {
		return cdos;
	}

	public void setCdos(String cdos) {
		this.cdos = cdos;
	}

	

	
	public int getNbul() {
		return nbul;
	}

	public void setNbul(int nbul) {
		this.nbul = nbul;
	}

	public String getDatecompta() {
		return datecompta;
	}

	public void setDatecompta(String datecompta) {
		this.datecompta = datecompta;
	}

	public String getDatevaleur() {
		return datevaleur;
	}

	public void setDatevaleur(String datevaleur) {
		this.datevaleur = datevaleur;
	}

	public String getMvtcomptaunique() {
		return mvtcomptaunique;
	}

	public void setMvtcomptaunique(String mvtcomptaunique) {
		this.mvtcomptaunique = mvtcomptaunique;
	}

	public String getDatesupcumul() {
		return datesupcumul;
	}

	public void setDatesupcumul(String datesupcumul) {
		this.datesupcumul = datesupcumul;
	}

	public String getDelaisupsalarie() {
		return delaisupsalarie;
	}

	public void setDelaisupsalarie(String delaisupsalarie) {
		this.delaisupsalarie = delaisupsalarie;
	}

	public String getIf_sup_sal() {
		return if_sup_sal;
	}

	public void setIf_sup_sal(String if_sup_sal) {
		this.if_sup_sal = if_sup_sal;
	}

	public String getMiseajouruniquement()
	{
		return miseajouruniquement;
	}

	public void setMiseajouruniquement(String miseajouruniquement)
	{
		this.miseajouruniquement = miseajouruniquement;
	}

	public String getFilepath()
	{
		return filepath;
	}

	public void setFilepath(String filepath)
	{
		this.filepath = filepath;
	}

	public String getActiontoexec()
	{
		return actiontoexec;
	}

	public void setActiontoexec(String actiontoexec)
	{
		this.actiontoexec = actiontoexec;
	}

	public String getOninit()
	{
		return oninit;
	}

	public void setOninit(String oninit)
	{
		this.oninit = oninit;
	}

	public boolean isMvtcomptauniquement()
	{
		return mvtcomptauniquement;
	}

	public void setMvtcomptauniquement(boolean mvtcomptauniquement)
	{
		this.mvtcomptauniquement = mvtcomptauniquement;
		if(this.mvtcomptauniquement)
			this.setMvtcomptaunique("O");
		else
			this.setMvtcomptaunique("N");
	}

	public boolean isBoolmiseajouruniquement()
	{
		return boolmiseajouruniquement;
	}

	public void setBoolmiseajouruniquement(boolean boolmiseajouruniquement)
	{
		this.boolmiseajouruniquement = boolmiseajouruniquement;
		if(this.boolmiseajouruniquement)
			this.setMiseajouruniquement("O");
		else
			this.setMiseajouruniquement("N");
	}

	public boolean isBoolif_sup_sal()
	{
		return boolif_sup_sal;
	}

	public void setBoolif_sup_sal(boolean boolif_sup_sal)
	{
		this.boolif_sup_sal = boolif_sup_sal;
		if(! this.boolif_sup_sal)
			if_sup_sal = "N";
		else
			if_sup_sal = "O";
	}
	
	

}
