package com.kinart.paie.business.services.calcul;
/**
 * Classe contenant les param�tres du calcul des rubriques, c'est-�-dire les bases, 
 * le taux et le montant, etc.
 * @author c.mbassi
 *
 */
public class ClsValeurRubriquePartage {
	private double amount = 0;
	private double rates = 0;
	private double base = 0;
	private double basePlafonnee = 0;
	private double abattement = 0;
	private double inter = 0;
	private double basetaux = 0;
	private double valeur = 0;
	private double plafond = 0;
	private double monhs = 0;
	public double getPlafond() {
		return plafond;
	}
	public void setPlafond(double plafond) {
		this.plafond = plafond;
	}
	public double getValeur() {
		return valeur;
	}
	public void setValeur(double valeur) {
		this.valeur = valeur;
	}
	public double getBasetaux() {
		return basetaux;
	}
	public void setBasetaux(double basetaux) {
		this.basetaux = basetaux;
	}
	public double getAbattement() {
		return abattement;
	}
	public void setAbattement(double abattement) {
		this.abattement = abattement;
	}
	public double getInter() {
		return inter;
	}
	public void setInter(double inter) {
		this.inter = inter;
	}
	public double getMonhs() {
		return monhs;
	}
	public void setMonhs(double monhs) {
		this.monhs = monhs;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double getBase() {
		return base;
	}
	public void setBase(double base) {
		this.base = base;
	}
	public double getBasePlafonnee() {
		return basePlafonnee;
	}
	public void setBasePlafonnee(double basePlafonnee) {
		this.basePlafonnee = basePlafonnee;
	}
	public double getRates() {
		return rates;
	}
	public void setRates(double rates) {
		this.rates = rates;
	}
}
