package com.kinart.paie.business.services.calcul;

public class ClsInfoOfBank {
	private String ccy = "";
	private String code = "";
	private double virementMini = 0;
	private int NbreDecimal = 0;
	private double exchangeRate = 0;
	private boolean isForeignBank = false;
	//
	public boolean isForeignBank() {
		return isForeignBank;
	}
	public void setForeignBank(boolean isForeignBank) {
		this.isForeignBank = isForeignBank;
	}
	public String getCcy() {
		return ccy;
	}
	public void setCcy(String ccy) {
		this.ccy = ccy;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public double getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public int getNbreDecimal() {
		return NbreDecimal;
	}
	public void setNbreDecimal(int nbreDecimal) {
		NbreDecimal = nbreDecimal;
	}
	public double getVirementMini() {
		return virementMini;
	}
	public void setVirementMini(double virementMini) {
		this.virementMini = virementMini;
	}
	
	@Override
	public String toString(){
		String res = "";
		res = code; 
		res += " ".concat(ccy);
		res += " ".concat(String.valueOf(virementMini));
		res += " ".concat(String.valueOf(NbreDecimal));
		//
		return res;
	}
}
