package com.kinart.paie.business.services.cloture;

import java.math.BigDecimal;
import java.util.Date;

public class DetailEv
{
	public Date ddpa = null;

	public Date dfpa = null;

	public String bcmo;

	public Date ddeb = null;

	public Date dfin = null;

	public double nbjc = 0;

	public double nbja = 0;

	public String motf = "";

	public BigDecimal mont = new BigDecimal(0);

	public int mnt1 = 0;

	public int mnt3 = 0;
	
	public String cuti ="";
	
	public boolean transferEVCGMoisSuivant_flag = false;
	
	public boolean pas_moi_flag = false;
	
	public boolean maj_det_flag = false;

	public Date getDdpa()
	{
		return ddpa;
	}

	public void setDdpa(Date ddpa)
	{
		this.ddpa = ddpa;
	}

	public Date getDfpa()
	{
		return dfpa;
	}

	public void setDfpa(Date dfpa)
	{
		this.dfpa = dfpa;
	}

	public String getBcmo()
	{
		return bcmo;
	}

	public void setBcmo(String bcmo)
	{
		this.bcmo = bcmo;
	}

	public Date getDdeb()
	{
		return ddeb;
	}

	public void setDdeb(Date ddeb)
	{
		this.ddeb = ddeb;
	}

	public Date getDfin()
	{
		return dfin;
	}

	public void setDfin(Date dfin)
	{
		this.dfin = dfin;
	}

	public double getNbjc()
	{
		return nbjc;
	}

	public void setNbjc(double nbjc)
	{
		this.nbjc = nbjc;
	}

	public double getNbja()
	{
		return nbja;
	}

	public void setNbja(double nbja)
	{
		this.nbja = nbja;
	}

	public String getMotf()
	{
		return motf;
	}

	public void setMotf(String motf)
	{
		this.motf = motf;
	}

	public BigDecimal getMont()
	{
		return mont;
	}

	public void setMont(BigDecimal mont)
	{
		this.mont = mont;
	}

	public int getMnt1()
	{
		return mnt1;
	}

	public void setMnt1(int mnt1)
	{
		this.mnt1 = mnt1;
	}

	public int getMnt3()
	{
		return mnt3;
	}

	public void setMnt3(int mnt3)
	{
		this.mnt3 = mnt3;
	}

	public String getCuti()
	{
		return cuti;
	}

	public void setCuti(String cuti)
	{
		this.cuti = cuti;
	}

	public boolean getTransferEVCGMoisSuivant_flag()
	{
		return transferEVCGMoisSuivant_flag;
	}

	public void setTransferEVCGMoisSuivant_flag(boolean transferEVCGMoisSuivant_flag)
	{
		this.transferEVCGMoisSuivant_flag = transferEVCGMoisSuivant_flag;
	}

	public boolean getPas_moi_flag()
	{
		return pas_moi_flag;
	}

	public void setPas_moi_flag(boolean pas_moi_flag)
	{
		this.pas_moi_flag = pas_moi_flag;
	}

	public boolean getMaj_det_flag()
	{
		return maj_det_flag;
	}

	public void setMaj_det_flag(boolean maj_det_flag)
	{
		this.maj_det_flag = maj_det_flag;
	}
	
}
