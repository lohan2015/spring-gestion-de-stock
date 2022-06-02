package com.kinart.paie.business.services.cloture;

import com.kinart.paie.business.model.CalculPaie;
import org.springframework.beans.BeanUtils;


public class Calcul extends CalculPaie
{
	public String crub;
	public boolean maj_pret13_flag = false;
	public boolean maj_pret17_flag = false;
	public boolean maj_cumul_flag = false;
	public boolean maj_eva_flag = false;
	
	public Calcul(CalculPaie calcul)
	{
		BeanUtils.copyProperties(calcul, this);
		crub = calcul.getRubq();
	}

	public boolean getMaj_pret13_flag()
	{
		return maj_pret13_flag;
	}

	public void setMaj_pret13_flag(boolean maj_pret13_flag)
	{
		this.maj_pret13_flag = maj_pret13_flag;
	}

	public boolean getMaj_pret17_flag()
	{
		return maj_pret17_flag;
	}

	public void setMaj_pret17_flag(boolean maj_pret17_flag)
	{
		this.maj_pret17_flag = maj_pret17_flag;
	}

	public boolean getMaj_cumul_flag()
	{
		return maj_cumul_flag;
	}

	public void setMaj_cumul_flag(boolean maj_cumul_flag)
	{
		this.maj_cumul_flag = maj_cumul_flag;
	}

	public boolean getMaj_eva_flag()
	{
		return maj_eva_flag;
	}

	public void setMaj_eva_flag(boolean maj_eva_flag)
	{
		this.maj_eva_flag = maj_eva_flag;
	}

	public String getCrub()
	{
		return crub;
	}

	public void setCrub(String crub)
	{
		this.crub = crub;
	}
	
	
}
