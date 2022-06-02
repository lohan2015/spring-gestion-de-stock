package com.kinart.paie.business.services.cloture;

import java.math.BigDecimal;

public class ClsRubriqueAComptabiliser
{
	
	private String sens;
	
	private long prbul;
	
	private String signe;
	
	private BigDecimal montant;
	
	private String rubrique;

	public String getSens()
	{
		return sens;
	}

	public void setSens(String sens)
	{
		this.sens = sens;
	}

	
	public long getPrbul()
	{
		return prbul;
	}

	public void setPrbul(long prbul)
	{
		this.prbul = prbul;
	}

	public String getSigne()
	{
		return signe;
	}

	public void setSigne(String signe)
	{
		this.signe = signe;
	}

	public BigDecimal getMontant()
	{
		return montant;
	}

	public void setMontant(BigDecimal montant)
	{
		this.montant = montant;
	}

	public String getRubrique()
	{
		return rubrique;
	}

	public void setRubrique(String rubrique)
	{
		this.rubrique = rubrique;
	}
	
	
}
