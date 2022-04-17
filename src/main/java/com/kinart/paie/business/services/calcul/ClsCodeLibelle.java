package com.kinart.paie.business.services.calcul;

public class ClsCodeLibelle
{
	private String code;

	private String libelle;
	
	private String orderby;

	public ClsCodeLibelle()
	{

	}

	public ClsCodeLibelle(String code, String libelle)
	{
		setCode(code);
		setLibelle(libelle);
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getLibelle()
	{
		return libelle;
	}

	public void setLibelle(String libelle)
	{
		this.libelle = libelle;
	}

	public String getOrderby() {
		return orderby;
	}

	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}
	
}
