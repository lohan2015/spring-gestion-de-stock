package com.kinart.paie.business.services.calcul;


/**
 * Classe contenant les param�tres des cumuls. Cette a �t� cr��e pour partager ces param�tres avec les autres classes.
 * @author c.mbassi
 *
 */
public class ClsParametreCumul {
	private double cumulBaseCalc = 0;
	private double cumulRegu = 0;
	private double cumulCoti = 0;
	private int nbreMoisCumul = 0;
	public double getCumulBaseCalc() {
		return cumulBaseCalc;
	}
	public void setCumulBaseCalc(double cumulBaseCalc) {
		this.cumulBaseCalc = cumulBaseCalc;
	}
	public double getCumulCoti() {
		return cumulCoti;
	}
	public void setCumulCoti(double cumulCoti) {
		this.cumulCoti = cumulCoti;
	}
	public double getCumulRegu() {
		return cumulRegu;
	}
	public void setCumulRegu(double cumulRegu) {
		this.cumulRegu = cumulRegu;
	}
	public int getNbreMoisCumul() {
		return nbreMoisCumul;
	}
	public void setNbreMoisCumul(int nbreMoisCumul) {
		this.nbreMoisCumul = nbreMoisCumul;
	}
	
	public String localToString()
	{
		return null;//StringUtils.toString(this);
	}
}
