package com.kinart.paie.business.services.precentralisation;

public class ClsCheckNomenclature
{
	boolean keyexist;
	
	boolean columnexist;
	
	String columnvalue;
	
	
	
	public ClsCheckNomenclature(boolean keyexist, boolean columnexist, String columnvalue)
	{
		super();
		this.keyexist = keyexist;
		this.columnexist = columnexist;
		this.columnvalue = columnvalue;
	}
	public boolean isKeyexist()
	{
		return keyexist;
	}
	public void setKeyexist(boolean keyexist)
	{
		this.keyexist = keyexist;
	}
	public boolean isColumnexist()
	{
		return columnexist;
	}
	public void setColumnexist(boolean columnexist)
	{
		this.columnexist = columnexist;
	}
	public String getColumnvalue()
	{
		return columnvalue;
	}
	public void setColumnvalue(String columnvalue)
	{
		this.columnvalue = columnvalue;
	}
	
	
}
