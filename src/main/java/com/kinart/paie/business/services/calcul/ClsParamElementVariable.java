package com.kinart.paie.business.services.calcul;

/**
 * Classe contenant les param�tres des �l�ments variables. Cette a �t� cr��e pour partager ces param�tres avec les autres classes.
 * @author c.mbassi
 */
public class ClsParamElementVariable {
	private double mont = 0;
	private String ruba = "";
	private String nprt = "";
	private String argu = "";
	private boolean applyElementVariable = false;
	public boolean isApplyElementVariable() {
		return applyElementVariable;
	}
	public void setApplyElementVariable(boolean applyElementVariable) {
		this.applyElementVariable = applyElementVariable;
	}
	public String getArgu() {
		return argu;
	}
	public void setArgu(String argu) {
		this.argu = argu;
	}
	public double getMont() {
		return mont;
	}
	public void setMont(double mont) {
		this.mont = mont;
	}
	public String getNprt() {
		return nprt;
	}
	public void setNprt(String nprt) {
		this.nprt = nprt;
	}
	public String getRuba() {
		return ruba;
	}
	public void setRuba(String ruba) {
		this.ruba = ruba;
	}
}
