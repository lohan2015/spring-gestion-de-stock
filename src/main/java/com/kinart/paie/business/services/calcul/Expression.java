package com.kinart.paie.business.services.calcul;

public class Expression
{
	public static String symboleParamOuvert="[";
	public static String symboleParamFerme="]";
	private String param;
	private double valeur;

	public String getParam()
	{
		return symboleParamOuvert+param+symboleParamFerme;
	}

	public void setParam(String param)
	{
		this.param = param;
	}

	public double getValeur()
	{
		return valeur;
	}

	public void setValeur(double valeur)
	{
		this.valeur = valeur;
	}

	public Expression(String param, double valeur)
	{
		super();
		this.param = param;
		this.valeur = valeur;
	}

}
