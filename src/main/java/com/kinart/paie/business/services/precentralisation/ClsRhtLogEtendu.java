package com.kinart.paie.business.services.precentralisation;

import java.io.Serializable;

import com.kinart.paie.business.model.LogMessage;

public class ClsRhtLogEtendu implements Serializable {
	
	private LogMessage rhtlog;
	
	private String messageLigne;
	
	private boolean errormessage;
	
	private boolean isendblock;
	
	private boolean isblockingmessage;
	
	public boolean isErrormessage()
	{
		return errormessage;
	}
	public void setErrormessage(boolean errormessage)
	{
		this.errormessage = errormessage;
	}
	public String getMessageLigne() {
		return messageLigne;
	}
	public void setMessageLigne(String messageLigne) {
		this.messageLigne = messageLigne;
	}
	public LogMessage getRhtlog() {
		return rhtlog;
	}
	public void setRhtlog(LogMessage rhtlog) {
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
	
	public ClsRhtLogEtendu (LogMessage rhtlog , String messageLigne, boolean iserrormessage, boolean typeOfMessage){
		this.setRhtlog(rhtlog);
		this.setMessageLigne(messageLigne);
		this.setErrormessage(iserrormessage);
		if(iserrormessage)
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
	
	
}
