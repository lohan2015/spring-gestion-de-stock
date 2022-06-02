package com.kinart.paie.business.services.cloture;


import com.kinart.paie.business.model.CumulPaie;

public class ClsCumul
{
	private CumulPaie cumul;
	private boolean existindb;
	
	public ClsCumul()
	{
		
	}
	public ClsCumul(CumulPaie cumul, boolean existindb)
	{
		super();
		this.cumul = cumul;
		this.existindb = existindb;
	}
	public ClsCumul(CumulPaie cumul)
	{
		super();
		this.cumul = cumul;
		this.existindb = false;
	}
	
	public CumulPaie getCumul()
	{
		return cumul;
	}
	public void setCumul(CumulPaie cumul)
	{
		this.cumul = cumul;
	}
	public boolean isExistindb()
	{
		return existindb;
	}
	public void setExistindb(boolean existindb)
	{
		this.existindb = existindb;
	}
	
	

}
