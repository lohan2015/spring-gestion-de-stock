package com.kinart.paie.business.services.cloture;
import com.kinart.paie.business.model.CalculPaie;
import com.kinart.paie.business.model.ElementSalaire;
import com.kinart.paie.business.model.InterfComptable;
import org.springframework.beans.BeanUtils;


public class ClsCalculCentralisation extends CalculPaie
{
	public ClsCalculCentralisation(CalculPaie calcul)
	{
		BeanUtils.copyProperties(calcul, this);
		sens = new Sens[]{new Sens(),new Sens()};
		wint = new InterfComptable();
	}
	
	public ElementSalaire rubrique;
	
	public Integer Type_Rub;
	
	public Sens[] sens = new Sens[]{new Sens(),new Sens()};
	
	public InterfComptable wint = new InterfComptable();
	
	
	
	
	public class Sens
	{
		public String sens;
		public String Num_Compte;
		public boolean suivi_anal;
		public boolean suivi_let;
		public String Num_Tiers;
		public String Cpt_Type;
		public String[] Destination = new String[] { null, null, null, null, null, null, null, null, null };
		
		
		public Sens()
		{
			
		}

		public String getSens()
		{
			return sens;
		}

		public void setSens(String sens)
		{
			this.sens = sens;
		}

		public String getNum_Compte()
		{
			return Num_Compte;
		}

		public void setNum_Compte(String num_Compte)
		{
			Num_Compte = num_Compte;
		}

		public boolean isSuivi_anal()
		{
			return suivi_anal;
		}

		public void setSuivi_anal(boolean suivi_anal)
		{
			this.suivi_anal = suivi_anal;
		}

		public boolean isSuivi_let()
		{
			return suivi_let;
		}

		public void setSuivi_let(boolean suivi_let)
		{
			this.suivi_let = suivi_let;
		}

		public String getNum_Tiers()
		{
			return Num_Tiers;
		}

		public void setNum_Tiers(String num_Tiers)
		{
			Num_Tiers = num_Tiers;
		}

		public String getCpt_Type()
		{
			return Cpt_Type;
		}

		public void setCpt_Type(String cpt_Type)
		{
			Cpt_Type = cpt_Type;
		}

		public String[] getDestination()
		{
			return Destination;
		}

		public void setDestination(String[] destination)
		{
			Destination = destination;
		}

	}




	public ElementSalaire getRubrique()
	{
		return rubrique;
	}




	public void setRubrique(ElementSalaire rubrique)
	{
		this.rubrique = rubrique;
	}




	public Integer getType_Rub()
	{
		return Type_Rub;
	}




	public void setType_Rub(Integer type_Rub)
	{
		Type_Rub = type_Rub;
	}




	public Sens[] getSens()
	{
		return sens;
	}




	public void setSens(Sens[] sens)
	{
		this.sens = sens;
	}




	public InterfComptable getWint()
	{
		return wint;
	}




	public void setWint(InterfComptable wint)
	{
		this.wint = wint;
	}
	
	
}
