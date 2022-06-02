package com.kinart.paie.business.services.cloture;

import java.util.Date;

import com.kinart.paie.business.model.LogMessage;
import com.kinart.paie.business.services.utils.ClsResultat;

public class ClsLog
{

	private LogMessage rhtlog;

	private String messageLigne;

	private boolean errormessage;

	private boolean isendblock;

	private boolean isblockingmessage;
	
	private ClsResultat resultat;

	public boolean isErrormessage()
	{
		return errormessage;
	}

	public void setErrormessage(boolean errormessage)
	{
		this.errormessage = errormessage;
	}

	public String getMessageLigne()
	{
		return messageLigne;
	}

	public void setMessageLigne(String messageLigne)
	{
		this.messageLigne = messageLigne;
	}

	public LogMessage getRhtlog()
	{
		return rhtlog;
	}

	public void setRhtlog(LogMessage rhtlog)
	{
		this.rhtlog = rhtlog;
	}

	public boolean isIsendblock()
	{
		return isendblock;
	}

	public void setIsendblock(boolean isendblock)
	{
		this.isendblock = isendblock;
	}

	public boolean isIsblockingmessage()
	{
		return isblockingmessage;
	}

	public void setIsblockingmessage(boolean isblockingmessage)
	{
		this.isblockingmessage = isblockingmessage;
	}

	public ClsLog(ClsOutPrecentralisation centralisateur, boolean typeOfMessage)
	{
		this.setResultat(centralisateur.message);
		this.setRhtlog(new LogMessage(new Integer(centralisateur.cdos), centralisateur.cuti, new Date(), centralisateur.message.getLibelle()));
		this.setMessageLigne(centralisateur.message.getLibelle());
		this.setErrormessage(centralisateur.message.isErrormessage());
		if (centralisateur.message.isErrormessage())
		{
			this.setIsblockingmessage(typeOfMessage);
			this.setIsendblock(false);
		}
		else
		{
			this.setIsblockingmessage(false);
			this.setIsendblock(typeOfMessage);
		}
	}

	public ClsResultat getResultat()
	{
		return resultat;
	}

	public void setResultat(ClsResultat resultat)
	{
		this.resultat = resultat;
	}
	
	
}
