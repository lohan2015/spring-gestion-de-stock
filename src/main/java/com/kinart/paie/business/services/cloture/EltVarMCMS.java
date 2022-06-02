package com.kinart.paie.business.services.cloture;

import java.util.Date;

public class EltVarMCMS
{
	public String periode = "";

	public Date ddebut = null;

	public Date dfin = null;

	public String getPeriode()
	{
		return periode;
	}

	public void setPeriode(String periode)
	{
		this.periode = periode;
	}

	public Date getDdebut()
	{
		return ddebut;
	}

	public void setDdebut(Date ddebut)
	{
		this.ddebut = ddebut;
	}

	public Date getDfin()
	{
		return dfin;
	}

	public void setDfin(Date dfin)
	{
		this.dfin = dfin;
	}
}
