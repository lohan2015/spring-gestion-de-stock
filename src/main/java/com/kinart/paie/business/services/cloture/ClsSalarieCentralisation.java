package com.kinart.paie.business.services.cloture;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.kinart.paie.business.model.CalculPaie;
import com.kinart.paie.business.model.Salarie;
import org.springframework.beans.BeanUtils;


public class ClsSalarieCentralisation extends Salarie
{
	
	public ClsSalarieCentralisation(Salarie salarie)
	{
		BeanUtils.copyProperties(salarie, this);
	}
	
	BigDecimal Total_Debit = new BigDecimal(0);
	
	BigDecimal Total_Credit = new BigDecimal(0);
	
	public List<ClsRubriqueAComptabiliser> rubriqueacomptabiliser = new ArrayList<ClsRubriqueAComptabiliser>();
	
	BigDecimal Ecart = new BigDecimal(0);
	
	public List<CalculPaie> listeCalculs;
	
	public List<ClsCalculCentralisation> listeCalculsCentralisation = new ArrayList<ClsCalculCentralisation>();
	
	public ClsCalculCentralisation calcul;

	public BigDecimal getTotal_Debit()
	{
		return Total_Debit;
	}

	public void setTotal_Debit(BigDecimal total_Debit)
	{
		Total_Debit = total_Debit;
	}

	public BigDecimal getTotal_Credit()
	{
		return Total_Credit;
	}

	public void setTotal_Credit(BigDecimal total_Credit)
	{
		Total_Credit = total_Credit;
	}

	public BigDecimal getEcart()
	{
		return Ecart;
	}

	public void setEcart(BigDecimal ecart)
	{
		Ecart = ecart;
	}

	public List<ClsCalculCentralisation> getListeCalculsCentralisation()
	{
		return listeCalculsCentralisation;
	}

	public void setListeCalculsCentralisation(List<ClsCalculCentralisation> listeCalculsCentralisation)
	{
		this.listeCalculsCentralisation = listeCalculsCentralisation;
	}

	public List<ClsRubriqueAComptabiliser> getRubriqueacomptabiliser()
	{
		return rubriqueacomptabiliser;
	}

	public void setRubriqueacomptabiliser(List<ClsRubriqueAComptabiliser> rubriqueacomptabiliser)
	{
		this.rubriqueacomptabiliser = rubriqueacomptabiliser;
	}

	
}
