package com.kinart.organisation.business.vo;

public class RechercheListeOrganigrammeVO
{
	public String libelle;

	public String code;

	public String cdos;
	
	public String clang;
	
	public String cuti;
	
	public String niveau;
	
	public String site;
	
	public String fictive;
	
	public String prestataire;
		

	public RechercheListeOrganigrammeVO(String cdos, String clang, String cuti)
	{
		super();
		this.cdos = cdos;
		this.clang = clang;
		this.cuti = cuti;
	}

	public String getLibelle()
	{
		return libelle;
	}

	public void setLibelle(String libelle)
	{
		this.libelle = libelle;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getCdos()
	{
		return cdos;
	}

	public void setCdos(String cdos)
	{
		this.cdos = cdos;
	}

	public String getClang()
	{
		return clang;
	}

	public void setClang(String clang)
	{
		this.clang = clang;
	}

	public String getNiveau()
	{
		return niveau;
	}

	public void setNiveau(String niveau)
	{
		this.niveau = niveau;
	}

	public String getSite()
	{
		return site;
	}

	public void setSite(String site)
	{
		this.site = site;
	}

	public String getFictive()
	{
		return fictive;
	}

	public void setFictive(String fictive)
	{
		this.fictive = fictive;
	}

	public String getPrestataire()
	{
		return prestataire;
	}

	public void setPrestataire(String prestataire)
	{
		this.prestataire = prestataire;
	}

}
