package com.kinart.paie.business.services.calcul;

/**
 * Classe permettant de garder les param�tres concernant la r�gularisation fran�aise.
 * Ces param�tres seront partag�s par plusieurs classes.
 * @author e.etoundi
 */
public class ClsRegularisationFrParam {
	double base = 0;
	double plafond = 0;
	double taux = 0;
	double cotisation = 0;
	double regularisation = 0;
	public double getBase() {
		return base;
	}
	public void setBase(double base) {
		this.base = base;
	}
	public double getCotisation() {
		return cotisation;
	}
	public void setCotisation(double cotisation) {
		this.cotisation = cotisation;
	}
	public double getPlafond() {
		return plafond;
	}
	public void setPlafond(double plafond) {
		this.plafond = plafond;
	}
	public double getRegularisation() {
		return regularisation;
	}
	public void setRegularisation(double regularisation) {
		this.regularisation = regularisation;
	}
	public double getTaux() {
		return taux;
	}
	public void setTaux(double taux) {
		this.taux = taux;
	}
}
