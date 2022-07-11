package com.kinart.paie.business.services.precentralisation;

import com.kinart.paie.business.services.utils.StringUtil;

import java.io.Serializable;
import java.util.Date;

public class ClsTemplate implements Serializable {

	private String code = null;
	
	private String libelle = null;
	
	private String format = null;
	
	private String value = "";
	
	private Date dtvalue = new Date();
	
	private int range = 0;
	
	private String codereduit = null;
	
	public ClsTemplate()
	{}
	
	public ClsTemplate(String strCode, String _strLibelle)
	{
		this.code = strCode;
		this.libelle = 	StringUtil.nvl(_strLibelle," ");
	}
	
	public ClsTemplate(String strCode, String _strLibelle, int _strRange)
	{
		this.code = strCode;
		this.libelle = 	_strLibelle;
		this.range = _strRange;
		constructCodeReduit();
	}
	
	public ClsTemplate(String strCode, String _strLibelle, String _strValue)
	{
		this.code = strCode;
		this.libelle = 	_strLibelle;
		this.value = _strValue;
		constructCodeReduit();
	}
	
	public ClsTemplate(String strCode, String _strLibelle, int _strRange, String _strValue)
	{
		this.code = strCode;
		this.libelle = 	_strLibelle;
		this.range = _strRange;
		this.value = _strValue;
		constructCodeReduit();
	}
	
	private void constructCodeReduit()
	{
		this.codereduit =  StringUtil.substring(this.code, 1, this.code.length() -1);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
		constructCodeReduit();
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getCodereduit()
	{
		return codereduit;
	}

	public void setCodereduit(String codereduit)
	{
		this.codereduit = codereduit;
	}

	public Date getDtvalue() {
		return dtvalue;
	}

	public void setDtvalue(Date dtvalue) {
		this.dtvalue = dtvalue;
	}
	
	public static String replaceString(String value1, String value2, String value3) {
		return "";
	}
	
}
