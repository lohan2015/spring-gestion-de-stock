package com.kinart.paie.business.services.calcul;


/**
 * Classe contenant les param�tres du calcul des rubriques, c'est-�-dire les bases, 
 * le taux et le montant, etc.
 * @author c.mbassi
 *
 */
public class ClsValeurRubrique {
	private double amount = 0;
	private double rates = 0;
	private double base = 0;
	private double basePlafonnee = 0;
	private String argu = "";
	public double getAmount()
	{
		return amount;
	}
	public void setAmount(double amount)
	{
		this.amount = amount;
	}
	public double getRates()
	{
		return rates;
	}
	public void setRates(double rates)
	{
		this.rates = rates;
	}
	public double getBase()
	{
		return base;
	}
	public void setBase(double base)
	{
		this.base = base;
	}
	public double getBasePlafonnee()
	{
		return basePlafonnee;
	}
	public void setBasePlafonnee(double basePlafonnee)
	{
		this.basePlafonnee = basePlafonnee;
	}
	public String getArgu()
	{
		return argu;
	}
	public void setArgu(String argu)
	{
		this.argu = argu;
	}
	
}
