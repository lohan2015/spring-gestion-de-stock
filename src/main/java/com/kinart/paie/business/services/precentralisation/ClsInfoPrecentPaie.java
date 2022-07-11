package com.kinart.paie.business.services.precentralisation;


public class ClsInfoPrecentPaie {
	
	private String aamm;
	private String nbul;
	private String nmat;
	private String valeurnmat;
	private String verif_edit_bul;
	
	private String rub_a_comptabilise ;
	private String continu_si_erreur;
	
	private String onlyerrormessage;
	
	private String cdos;



       
    /** default constructor */
    public ClsInfoPrecentPaie() {
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

	
	

	public String getNmat() {
		return nmat;
	}

	public void setNmat(String nmat) {
		this.nmat = nmat;
	}

	public String getContinu_si_erreur() {
		return continu_si_erreur;
	}

	public void setContinu_si_erreur(String continu_si_erreur) {
		this.continu_si_erreur = continu_si_erreur;
	}

	public String getNbul() {
		return nbul;
	}

	public void setNbul(String nbul) {
		this.nbul = nbul;
	}

	public String getRub_a_comptabilise() {
		return rub_a_comptabilise;
	}

	public void setRub_a_comptabilise(String rub_a_comptabilise) {
		this.rub_a_comptabilise = rub_a_comptabilise;
	}

	public String getVerif_edit_bul() {
		return verif_edit_bul;
	}

	public void setVerif_edit_bul(String verif_edit_bul) {
		this.verif_edit_bul = verif_edit_bul;
	}

	public String getValeurnmat()
	{
		return valeurnmat;
	}

	public void setValeurnmat(String valeurnmat)
	{
		this.valeurnmat = valeurnmat;
	}

	public String getOnlyerrormessage()
	{
		return onlyerrormessage;
	}

	public void setOnlyerrormessage(String onlyerrormessage)
	{
		this.onlyerrormessage = onlyerrormessage;
	}

	

}
