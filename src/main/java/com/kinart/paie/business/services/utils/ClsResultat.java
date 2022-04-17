package com.kinart.paie.business.services.utils;

public class ClsResultat {
	
	
	
	private String code;
	private String libelle;
	private String description;
	private String table;
	private String tr;
	private String td;
	private boolean iserrormessage;
	
	public ClsResultat(){
		
	}


	public ClsResultat(boolean iserrormessage,String code, String libelle, String table, String tr, String td) {
		super();
		this.iserrormessage=iserrormessage;
		this.code = code;
		this.libelle = libelle;
		this.table = table;
		this.tr = tr;
		this.td = td;
	}
	
	public ClsResultat(String code, String libelle, String table, String tr, String td) {
		super();
		this.iserrormessage=false;
		this.code = code;
		this.libelle = libelle;
		this.table = table;
		this.tr = tr;
		this.td = td;
	}
	
	

	public ClsResultat(boolean iserrormessage, String code, String libelle, String description, String table, String tr, String td) {
		super();
		this.iserrormessage = iserrormessage;
		this.code = code;
		this.libelle = libelle;
		this.description = description;
		this.table = table;
		this.tr = tr;
		this.td = td;
	}

	public ClsResultat concat(ClsResultat resultat2, String separateur)
	{
		return ClsTreater._concat(this, resultat2, separateur);
	}
	
	public boolean isErrormessage() {
		return iserrormessage;
	}
	
	public boolean getIserrormessage() {
		return iserrormessage;
	}


	public void setIsErrormessage(boolean errormessage) {
		this.iserrormessage = errormessage;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getLibelle() {
		return libelle;
	}


	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}


	public String getTable() {
		return table;
	}


	public void setTable(String table) {
		this.table = table;
	}


	public String getTd() {
		return td;
	}


	public void setTd(String td) {
		this.td = td;
	}


	public String getTr() {
		return tr;
	}


	public void setTr(String tr) {
		this.tr = tr;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}
	
	

}
