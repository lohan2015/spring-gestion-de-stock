package com.kinart.organisation.business.vo;

import java.util.Date;

public class RechercheEffectifPrevisionnelVO
{
	public String libelle;

	public String code;

	public String cdos;

	public String clang;

	public String codeorganigramme;

	public Date datemin;

	public Date datemax;

	public RechercheEffectifPrevisionnelVO(String cdos, String clang, String codeorganigramme)
	{
		super();
		this.cdos = cdos;
		this.clang = clang;
		this.codeorganigramme = codeorganigramme;
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

	public String getCodeorganigramme()
	{
		return codeorganigramme;
	}

	public void setCodeorganigramme(String codeorganigramme)
	{
		this.codeorganigramme = codeorganigramme;
	}

	public Date getDatemin()
	{
		return datemin;
	}

	public void setDatemin(Date datemin)
	{
		this.datemin = datemin;
	}

	public Date getDatemax()
	{
		return datemax;
	}

	public void setDatemax(Date datemax)
	{
		this.datemax = datemax;
	}

}
