package com.kinart.paie.business.services.calcul;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.kinart.paie.business.model.*;
import com.kinart.paie.business.services.utils.*;
import org.apache.commons.lang3.StringUtils;

/**
 * Cette classe permet d'implémenter un wrapper d'une rubrique et d'encapsuler les données d'une rubrique: les éléments variables de la rubrique, l'excéution de
 * l'algorithme adéquat, le calcul des différentes bases et montant, etc. Toutes les opérations liées é la gestion d'une rubrique y sont implémentées. Elle
 * contient le salarié concerné.
 * 
 * @author s.mbassi
 * 
 */
public class ClsFictifRubriqueClone
{
	// private static int derniereLigne = 0;
	private Number tempnumber = null;

	private int numElementVarCourant = 0;

	private ClsParubqClone rubrique = null;

	private double amount = 0;

	private double rates = 0;

	private double base = 0;

	private double basePlafonnee = 0;

	// ELEMENTS VARIABLES DE CETTE RUBRIQUE POUR CE SALARIE
	private List listOfElementVariable = null;

	// LISTE DES PRETS
	private List<Object> listOfLoanNumber = null;

	// LISTE DES REGULARISATIONS FRANCAISES
	private List<ClsRegularisationFrParam> listOfRegularisationFr = null;

	// CHAMPS DES EV
	private int elementVariableNombre = 0;

	private double elementVariableValeur = 0;

	private double elementVariableTransit = 0;

	private boolean elementVariableApply = false;

	private boolean rubriqueEF = false;

	private boolean rubriqueEV = false;

	//
	private ClsFictifSalaryToProcess fictivesalary = null;

	// periode de régulation
	// private int nbrePeriodeRegularisation = 0;
	private int nbrePeriodeRegularisationEv = 0;

	private boolean inserted = false;

	public synchronized List<Object> getListOfLoanNumber()
	{
		return listOfLoanNumber;
	}

	public synchronized void setListOfLoanNumber(List<Object> listOfLoanNumber)
	{
		this.listOfLoanNumber = listOfLoanNumber;
	}

	public boolean isInserted()
	{
		return inserted;
	}

	public void setInserted(boolean inserted)
	{
		this.inserted = inserted;
	}

	/**
	 * 
	 * @param fictivesalary
	 */
	public ClsFictifRubriqueClone(ClsFictifSalaryToProcess fictivesalary)
	{
		this.setFictivesalary(fictivesalary);
	}

	// public int getNbrePeriodeRegularisation() {
	// return nbrePeriodeRegularisation;
	// }
	// public void setNbrePeriodeRegularisation(int nbrePeriodeRegularisation) {
	// this.nbrePeriodeRegularisation = nbrePeriodeRegularisation;
	// }
	public int getNbrePeriodeRegularisationEv()
	{
		return nbrePeriodeRegularisationEv;
	}

	public void setNbrePeriodeRegularisationEv(int nbrePeriodeRegularisationEv)
	{
		this.nbrePeriodeRegularisationEv = nbrePeriodeRegularisationEv;
	}

	public int getNumElementVarCourant()
	{
		return numElementVarCourant;
	}

	public void setNumElementVarCourant(int numElementVarCourant)
	{
		this.numElementVarCourant = numElementVarCourant;
	}

	public List<ClsRegularisationFrParam> getListOfRegularisationFr()
	{
		return listOfRegularisationFr;
	}

	public void setListOfRegularisationFr(List<ClsRegularisationFrParam> listOfRegularisationFr)
	{
		this.listOfRegularisationFr = listOfRegularisationFr;
	}

	public boolean isRubriqueEF()
	{
		return rubriqueEF;
	}

	public void setRubriqueEF(boolean rubriqueEF)
	{
		this.rubriqueEF = rubriqueEF;
	}

	public boolean isRubriqueEV()
	{
		return rubriqueEV;
	}

	public void setRubriqueEV(boolean rubriqueEV)
	{
		this.rubriqueEV = rubriqueEV;
	}

	public boolean isElementVariableApply()
	{
		return elementVariableApply;
	}

	public void setElementVariableApply(boolean elementVariableApply)
	{
		this.elementVariableApply = elementVariableApply;
	}

	public int getElementVariableNombre()
	{
		return elementVariableNombre;
	}

	public void setElementVariableNombre(int elementVariableNombre)
	{
		this.elementVariableNombre = elementVariableNombre;
	}

	public double getElementVariableTransit()
	{
		return elementVariableTransit;
	}

	public void setElementVariableTransit(double elementVariableTransit)
	{
		this.elementVariableTransit = elementVariableTransit;
	}

	public double getElementVariableValeur()
	{
		return elementVariableValeur;
	}

	public void setElementVariableValeur(double elementVariableValeur)
	{
		this.elementVariableValeur = elementVariableValeur;
	}

	// public List<Object> getListOfLoanNumber() {
	// return listOfLoanNumber;
	// }
	// public void setListOfLoanNumber(List<Object> listOfLoanNumber) {
	// this.listOfLoanNumber = listOfLoanNumber;
	// }
	public List getListOfElementVariable()
	{
		return listOfElementVariable;
	}

	public void setListOfElementVariable(List listOfElementVariable)
	{
		this.listOfElementVariable = listOfElementVariable;
	}

	

	public ClsFictifSalaryToProcess getFictivesalary()
	{
		return fictivesalary;
	}

	public void setFictivesalary(ClsFictifSalaryToProcess fictivesalary)
	{
		this.fictivesalary = fictivesalary;
	}

	//
	public double getAmount()
	{
		return amount;
	}

	public void setAmount(double amount)
	{
		this.amount = amount;
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

	public double getRates()
	{
		return rates;
	}

	public void setRates(double rates)
	{
		this.rates = rates;
	}

	public ClsParubqClone getRubrique()
	{
		return rubrique;
	}

	public void setRubrique(ClsParubqClone rubrique)
	{
		this.rubrique = rubrique;
	}

	/**
	 * Appelle l'algorithme correspondant é cette rubrique
	 * 
	 * @return true ou false étant le résultat que l'algorithme a renvoyé
	 */
	public boolean applyAlgorithm()
	{
		// outputtext +="\n"+">>applyAlgorithm");
		Boolean result = false;
		try
		{
			String outputtext = StringUtils.EMPTY;

			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\nEn entrée : base = " + fictivesalary.getValeurRubriquePartage().getBase();
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\nEn entrée : taux = " + fictivesalary.getValeurRubriquePartage().getRates();
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\nEn entrée : montant = " + fictivesalary.getValeurRubriquePartage().getAmount();
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\nEn entrée : base plaf= " + fictivesalary.getValeurRubriquePartage().getBasePlafonnee();
			
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext +="\n"+outputtext;


			long algoToApply = this.rubrique.getAlgo();
			Method theAlgo = IFictifAlgorithm.class.getMethod("algo" + String.valueOf(algoToApply), new Class[] { ClsFictifRubriqueClone.class });
			IFictifAlgorithm algo = new ClsFictifAlgorithm(this.getFictivesalary());
			//IFictifAlgorithm algo = (IFictifAlgorithm) ClassUtils.getClass(fictivesalary.param.nomClient).newInstance();
			//algo.setSalary(salary);
			result = (Boolean) theAlgo.invoke(algo, new Object[] { this });
			
			outputtext = StringUtils.EMPTY;
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\nEn sortie : base = " + fictivesalary.getValeurRubriquePartage().getBase();
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\nEn sortie : taux = " + fictivesalary.getValeurRubriquePartage().getRates();
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\nEn sortie : montant = " + fictivesalary.getValeurRubriquePartage().getAmount();
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\nEn sortie : base plaf= " + fictivesalary.getValeurRubriquePartage().getBasePlafonnee();
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n--Resultat de l'application de l'algo " + algoToApply + "\n";
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "------------------------------------------------\n";
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += algo.getOutputtext();
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\nresult = " + result + "\n------------------------------------------------\n";
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext +="\n"+outputtext;
			
			
		}
		catch (InvocationTargetException e)
		{
			// write into the log file
			e.printStackTrace();
			return false;
		}
		catch (IllegalAccessException e)
		{
			// write into the log file
			e.printStackTrace();
			return false;
		}
		catch (NoSuchMethodException e)
		{
			// write into the log file
			e.printStackTrace();
			return false;
		}
		catch (Exception e)
		{
			// write into the log file
			e.printStackTrace();
			return false;
		}
		return result;
	}

	/**
	 * => champ_app Vérifie que cette rubrique est applicable au salarié.
	 * 
	 * @param numeroAjustement
	 * @return true si la rubrique est applicable et false dans le cas contraire
	 */
	public boolean champApplicationSalarieToRubrique(int numeroAjustement)
	{
		String queryStringCm = "from CaisseMutuelleSalaire " + " where comp_id.cdos = '" + this.fictivesalary.param.getDossier() + "'" + " and comp_id.nmat = '" + fictivesalary.getInfoSalary().getComp_id().getNmat()
				+ "'" + " and (comp_id.rscm = '" + rubrique.getComp_id().getCrub() + "' or rpcm = '" + rubrique.getComp_id().getCrub() + "')";

		if (("O".equals(fictivesalary.getInfoSalary().getPnet())) && rubrique != null
				&& (!ClsObjectUtil.isNull(rubrique.getAjnu()) && rubrique.getAjnu() > numeroAjustement && "O".equals(rubrique.getAjus())))
		{
			return false;
		}
		
		if ('O' == fictivesalary.param.getGenfile())
			fictivesalary.outputtext += "\n--In Rubrique Clone : Sex rubrique = "+rubrique.getSexe();
		if (!ClsObjectUtil.isAppliedToObject(fictivesalary.getInfoSalary().getSexe(), rubrique.getSexe()))
		{
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "\n--In Rubrique Clone : Sex doesn't match !";
			return false;
		}

		int moisCalcul = fictivesalary.param.getMyMonthOfPay().getMonth();
		if ('O' == fictivesalary.param.getGenfile())
			fictivesalary.outputtext += "\n--In Rubrique Clone : mois Calcul = "+moisCalcul;
			
		if (!ClsObjectUtil.isAppliedToObject(moisCalcul, rubrique.getMoi1(), rubrique.getMoi2(), rubrique.getMoi3(), rubrique.getMoi4()))
		{
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "\n--In Rubrique Clone : moisCalcul doesn't match !";
			return false;
		}

		if (!ClsObjectUtil.isAppliedToObject(fictivesalary.param.getNumeroBulletin(), rubrique.getBul1(), rubrique.getBul2(), rubrique.getBul3(), rubrique.getBul4()))
		{
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "\n--In Rubrique Clone : Nbul doesn't match !";
			return false;
		}

		if (!ClsObjectUtil.isNull(rubrique.getAvn()))
		{
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "\n--In Rubrique Clone : AVN rubrique = "+rubrique.getAvn();
			if (!ClsObjectUtil.isAppliedToObject(fictivesalary.getInfoSalary().getAvn1(), rubrique.getAvn()))
				if (!ClsObjectUtil.isAppliedToObject(fictivesalary.getInfoSalary().getAvn2(), rubrique.getAvn()))
					if (!ClsObjectUtil.isAppliedToObject(fictivesalary.getInfoSalary().getAvn3(), rubrique.getAvn()))
						if (!ClsObjectUtil.isAppliedToObject(fictivesalary.getInfoSalary().getAvn4(), rubrique.getAvn()))
							if (!ClsObjectUtil.isAppliedToObject(fictivesalary.getInfoSalary().getAvn5(), rubrique.getAvn()))
								if (!ClsObjectUtil.isAppliedToObject(fictivesalary.getInfoSalary().getAvn6(), rubrique.getAvn()))
								{
									if ('O' == fictivesalary.param.getGenfile())
										fictivesalary.outputtext += "\n--In Rubrique Clone : avn xx doesn't match !";
									return false;
								}
		}

		if (!ClsObjectUtil.isAppliedToObject(fictivesalary.getInfoSalary().getCods(), rubrique.getCs1(), rubrique.getCs2(), rubrique.getCs3()))
		{
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "\n--In Rubrique Clone : Cods doesn't match !";
			return false;
		}

		if (!ClsObjectUtil.isAppliedToObject(fictivesalary.getInfoSalary().getSitf(), rubrique.getSit1(), rubrique.getSit2(), rubrique.getSit3(), rubrique.getSit4()))
		{
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "\n--In Rubrique Clone : Sitf doesn't match !";
			return false;
		}

		boolean app = false;
		if (fictivesalary.getInfoSalary().getNbec() != null)
		{
			if (((rubrique.getNbe1() != null) && (rubrique.getNbe1() > fictivesalary.getInfoSalary().getNbec()) || ((rubrique.getNbe2() != null) && rubrique.getNbe2() < fictivesalary.getInfoSalary().getNbec())))
			{
				if ('O' == fictivesalary.param.getGenfile())
					fictivesalary.outputtext += "\n--In Rubrique Clone : Nbec doesn't match !";
				return false;
			}
		}

		if (!ClsObjectUtil.isAppliedToObject(fictivesalary.getInfoSalary().getAfec(), rubrique.getZca1(), rubrique.getZca2(), rubrique.getZca3(), rubrique.getZca4()))
		{
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "\n--In Rubrique Clone : Afec doesn't match !";
			return false;
		}
		
		if ('O' == fictivesalary.param.getGenfile())
			fictivesalary.outputtext += "\n--In Rubrique Clone : Cat 1 to applied = "+rubrique.getCat1();
		if ((rubrique.getCat1() != null && fictivesalary.getInfoSalary().getCat() != null && fictivesalary.getInfoSalary().getCat().compareTo(rubrique.getCat1()) < 0))
		{
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "\n--In Rubrique Clone : Cat doesn't match !, case rub cat 1#null and sal cat#null and sal cat < rub cat1";
			return false;
		}
		else if ((fictivesalary.getInfoSalary().getCat() == null && rubrique.getCat1() != null))
		{
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "\n--In Rubrique Clone : Cat doesn't match !, case rub cat 1#null and sal cat=null";
			return false;
		}

		if ((rubrique.getCat2() != null && fictivesalary.getInfoSalary().getCat() != null && fictivesalary.getInfoSalary().getCat().compareTo(rubrique.getCat2()) > 0))
		{
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "\n--In Rubrique Clone : Cat doesn't match !, case rub cat é#null and sal cat#null and sal cat < rub cat2";
			return false;
		}
		else if ((fictivesalary.getInfoSalary().getCat() != null && rubrique.getCat2() == null))
		{
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "\n--In Rubrique Clone : Cat doesn't match !, case rub cat 2#null and sal cat=null";
			return false;
		}

		if (!ClsObjectUtil.isAppliedToObject(fictivesalary.getInfoSalary().getTypc(), rubrique.getTyc1(), rubrique.getTyc2(), rubrique.getTyc3(), rubrique.getTyc4(), rubrique.getTyc5(), rubrique.getTyc6(),
				rubrique.getTyc7(), rubrique.getTyc8()))
		{
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "\n--In Rubrique Clone : Typc doesn't match !";
			return false;
		}

		if (!ClsObjectUtil.isAppliedToObject(fictivesalary.getInfoSalary().getGrad(), rubrique.getGra1(), rubrique.getGra2(), rubrique.getGra3(), rubrique.getGra4(), rubrique.getGra5(), rubrique.getGra6(),
				rubrique.getGra7(), rubrique.getGra8()))
		{
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "\n--In Rubrique Clone : Grad doesn't match !";
			return false;
		}
		
		if ('O' == fictivesalary.param.getGenfile())
			fictivesalary.outputtext += "\n--In Rubrique Clone : Niv1 to applied = "+rubrique.getNiv11()+" - "+rubrique.getNiv12()+" - "+rubrique.getNiv13()+" - "+rubrique.getNiv14();
		if (!ClsObjectUtil.isAppliedToObject(fictivesalary.getInfoSalary().getNiv1(), rubrique.getNiv11(), rubrique.getNiv12(), rubrique.getNiv13(), rubrique.getNiv14()))
		{
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "\n--In Rubrique Clone : Niv1 doesn't match !";
			return false;
		}

		if (!ClsObjectUtil.isAppliedToObject(fictivesalary.getInfoSalary().getNiv2(), rubrique.getNiv21(), rubrique.getNiv22(), rubrique.getNiv23(), rubrique.getNiv24()))
		{
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "\n--In Rubrique Clone : Niv2 doesn't match !";
			return false;
		}

		if (!ClsObjectUtil.isAppliedToObject(fictivesalary.getInfoSalary().getNiv3(), rubrique.getNiv31(), rubrique.getNiv32(), rubrique.getNiv33(), rubrique.getNiv34()))
		{
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "\n--In Rubrique Clone : Niv3 doesn't match !";
			return false;
		}

		//app = rubrique.getAge1() == null && rubrique.getAge2() == null;
		Long age1  = NumberUtils.nvl(rubrique.getAge1(), 0).longValue();
		Long age2  = NumberUtils.nvl(rubrique.getAge2(), 100).longValue();

		if (fictivesalary.getAgeOfAgent() < age1 || fictivesalary.getAgeOfAgent() > age2)
		{
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "\n--In Rubrique Clone : Age1 doesn't match !";
			return false;
		}

		if (!ClsObjectUtil.isAppliedToObject(fictivesalary.getInfoSalary().getNato(), rubrique.getNat1(), rubrique.getNat2()))
		{
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "\n--In Rubrique Clone : Nato doesn't match !";
			return false;
		}

		if (!ClsObjectUtil.isAppliedToObject(fictivesalary.getInfoSalary().getSynd(), rubrique.getSynd()))
		{
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "\n--In Rubrique Clone : Synd doesn't match !";
			return false;
		}

		if (!ClsObjectUtil.isAppliedToObject(fictivesalary.getInfoSalary().getFonc(), rubrique.getFon1(), rubrique.getFon2(), rubrique.getFon3(), rubrique.getFon4(), rubrique.getFon5(), rubrique.getFon6(),
				rubrique.getFon7(), rubrique.getFon8()))
		{
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "\n--In Rubrique Clone : Fonc doesn't match !";
			return false;
		}

		if (!ClsObjectUtil.isAppliedToObject(fictivesalary.getInfoSalary().getRegi(), rubrique.getReg1(), rubrique.getReg2(), rubrique.getReg3(), rubrique.getReg4(), rubrique.getReg5(), rubrique.getReg6(),
				rubrique.getReg7(), rubrique.getReg8()))
		{
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "\n--In Rubrique Clone : Regi doesn't match !";
			return false;
		}

		if (!ClsObjectUtil.isAppliedToObject(fictivesalary.getInfoSalary().getClas(), rubrique.getClas1(), rubrique.getClas2(), rubrique.getClas3(), rubrique.getClas4()))
		{
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "\n--In Rubrique Clone : Clas doesn't match !";
			return false;
		}

		if (!ClsObjectUtil.isAppliedToObject(fictivesalary.getInfoSalary().getCodf(), rubrique.getCfon()))
		{
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "\n--In Rubrique Clone : Codf doesn't match !";
			return false;
		}

		if (!ClsObjectUtil.isAppliedToObject(fictivesalary.getInfoSalary().getHifo(), rubrique.getHif1(), rubrique.getHif2(), rubrique.getHif3(), rubrique.getHif4()))
		{
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "\n--In Rubrique Clone : Hifo doesn't match !";
			return false;
		}

		if (!ClsObjectUtil.isAppliedToObject(fictivesalary.getInfoSalary().getZli1(), rubrique.getZl11(), rubrique.getZl12()))
		{
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "\n--In Rubrique Clone : Zli1 doesn't match !";
			return false;
		}

		if (!ClsObjectUtil.isAppliedToObject(fictivesalary.getInfoSalary().getZli2(), rubrique.getZl21(), rubrique.getZl22()))
		{
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "\n--In Rubrique Clone : Zli2 doesn't match !";
			return false;
		}

		List zonelibre = new ArrayList();
		if (!this.fictivesalary.param.getListOfRubqZoneLibre().isEmpty() && this.fictivesalary.param.getListOfRubqZoneLibre().containsKey(rubrique.getComp_id().getCrub()))
		{
			zonelibre = this.fictivesalary.param.getListOfRubqZoneLibre().get(rubrique.getComp_id().getCrub());
		}

		String zoneLibreSalarie = "";
//		RhprubriqueZonelibre zonelibre2 = null;
//		for (Object obj : zonelibre)
//		{
//			zonelibre2 = (RhprubriqueZonelibre) obj;
//			zoneLibreSalarie = fictivesalary.rechercheZoneLibre(zonelibre2.getComp_id().getCzli());
//
//			if (!ClsObjectUtil.isAppliedToObject(zoneLibreSalarie, zonelibre2.getZli1(), zonelibre2.getZli2()))
//			{
//				if ('O' == fictivesalary.param.getGenfile())
//					fictivesalary.outputtext += "\n--In Rubrique Clone : zl doesn't match !";
//				return false;
//			}
//		}

		Integer dateAmdt = 0;
		if ("O".equals(rubrique.getCais()))
		{
			List listOfCaisse = this.fictivesalary.getService().find(queryStringCm);
			CaisseMutuelleSalarie pacaiss = null;
			for (Object obj : listOfCaisse)
			{
				pacaiss = (CaisseMutuelleSalarie) obj;

				if (pacaiss.getDtad() == null)
				{
					if ('O' == fictivesalary.param.getGenfile())
						fictivesalary.outputtext += "\n--In Rubrique Clone : date admission é la caisse EST NULL";
					break;
				}

				dateAmdt = new ClsDate(pacaiss.getDtad()).getYear() * 100 + new ClsDate(pacaiss.getDtad()).getMonth();

				if (fictivesalary.param.getMyMonthOfPay().getYearAndMonthInt() < dateAmdt)
				{
					if ('O' == fictivesalary.param.getGenfile())
						fictivesalary.outputtext += "\n--In Rubrique Clone : date admission caisse sup é periode de paie ";
					return false;
				}

				if (pacaiss.getDtrd() == null)
				{
					if ('O' == fictivesalary.param.getGenfile())
						fictivesalary.outputtext += "\n--In Rubrique Clone : dtrd de la caisse est null";
					// return false;
					break;
				}
				dateAmdt = new ClsDate(pacaiss.getDtrd()).getYear() * 100 + new ClsDate(pacaiss.getDtrd()).getMonth();

				if (fictivesalary.param.getMyMonthOfPay().getYearAndMonthInt() > dateAmdt)
				{
					if ('O' == fictivesalary.param.getGenfile())
						fictivesalary.outputtext += "\n--In Rubrique Clone : ...dtrd est sup é periode de paie actuelle";
					return false;
				}
			}

			if (listOfCaisse.size() == 0)
				return false;
		}
		return true;
	}

	/**
	 * => ins_rubq Insérer la rubrique dans PaCalc
	 * 
	 * @param clas
	 * @param nprt
	 * @param ruba
	 * @param trtb
	 */
	public void insertIntoSalariesFile(String clas, String nprt, String ruba, String trtb)
	{
		
		
		ClsFictifRubriqueClone tmpRubrique = null;
		if (!ClsObjectUtil.isNull(rubrique.getEdbbu()))
		{
			tmpRubrique = fictivesalary.findRubriqueCloneFictif(rubrique.getEdbbu());
//			fictivesalary.getValeurRubriquePartage().setBase(tmpRubrique.getBase());
//			fictivesalary.getValeurRubriquePartage().setBasePlafonnee(tmpRubrique.getBase());
			fictivesalary.getValeurRubriquePartage().setBase(tmpRubrique.getAmount());
			fictivesalary.getValeurRubriquePartage().setBasePlafonnee(tmpRubrique.getAmount());
		}

		if (!ClsObjectUtil.isNull(rubrique.getArro()))
		{
			double val = this.calculateArrondi(fictivesalary.getParam(), this.fictivesalary.getValeurRubriquePartage().getAmount());
			this.fictivesalary.getValeurRubriquePartage().setAmount(val);
		}
		
		//On arrondi directement le montant calculé, avant de le garder en mémoire, et en bd
		this.arrondirFonctionNddd();

		this.setBase(this.getBase() + this.fictivesalary.getValeurRubriquePartage().getBase());

		this.setBasePlafonnee(this.getBasePlafonnee() + this.fictivesalary.getValeurRubriquePartage().getBasePlafonnee());

		this.setRates(this.getRates() + this.fictivesalary.getValeurRubriquePartage().getRates());

		this.setAmount(this.getAmount() + this.fictivesalary.getValeurRubriquePartage().getAmount());

		String argu = ("PSEUDO-EV".equals(fictivesalary.getEvparam().getArgu())) ? "" : fictivesalary.getEvparam().getArgu();

		fictivesalary.incrementerDerniereLigne();

		try
		{
			nprt = Integer.valueOf(nprt).toString();
		}
		catch (Exception e)
		{
			nprt = null;
		}

		CongeFictif oCongeFictif = new CongeFictif();

		oCongeFictif.setIdEntreprise(Integer.valueOf(fictivesalary.param.getDossier()));
        oCongeFictif.setNmat(fictivesalary.getInfoSalary().getComp_id().getNmat());
        oCongeFictif.setAamm(fictivesalary.param.getMonthOfPay());
        oCongeFictif.setNbul(fictivesalary.param.getNumeroBulletin());
        oCongeFictif.setNlig(fictivesalary.getDerniereLigne());
        oCongeFictif.setRubq(this.rubrique.getComp_id().getCrub());
		oCongeFictif.setBasc(new BigDecimal(this.fictivesalary.getValeurRubriquePartage().getBase()));
		oCongeFictif.setBasp(new BigDecimal(this.fictivesalary.getValeurRubriquePartage().getBasePlafonnee()));
		oCongeFictif.setTaux(new BigDecimal(this.fictivesalary.getValeurRubriquePartage().getRates()));
		oCongeFictif.setMont(new BigDecimal(this.fictivesalary.getValeurRubriquePartage().getAmount()));

		oCongeFictif.setArgu(argu);
		oCongeFictif.setClas(clas);
		oCongeFictif.setNprt(nprt);
		oCongeFictif.setRuba(ruba);
		oCongeFictif.setTrtb(trtb);
		
		oCongeFictif.setBasc(ClsStringUtil.truncateToXDecimal(oCongeFictif.getBasc(),3));
		oCongeFictif.setBasp(ClsStringUtil.truncateToXDecimal(oCongeFictif.getBasp(),3));
		oCongeFictif.setTaux(ClsStringUtil.truncateToXDecimal(oCongeFictif.getTaux(),3));
		oCongeFictif.setMont(ClsStringUtil.truncateToXDecimal(oCongeFictif.getMont(),3));

		this.fictivesalary.getService().save(oCongeFictif);

//		this.fictivesalary.getService().getSession().flush();
//		this.fictivesalary.getService().getSession().clear();

		if (StringUtils.equals(fictivesalary.param.getNapRubrique(), rubrique.getComp_id().getCrub()))
			fictivesalary.total_nap += this.fictivesalary.getValeurRubriquePartage().getAmount();

		inserted = true;
	}
	
	private void arrondirFonctionNddd()
	{
		Integer nddd = 3;
		Integer nddd2 = 3;
		//Si c'est une rubrique comptabilisée ou une rubrique é afficher sur le bulletin, alors on tronque
		if(this.getRubrique().getPrbul() != 0 || StringUtils.equals(this.getRubrique().getComp(),"O"))
			nddd=this.fictivesalary.param.dossierNbreDecimale;
		//On arrondi directement le montant calculé, avant de le garder en mémoire, et en bd
		double montantFinal = ClsStringUtil.truncateToXDecimal(this.fictivesalary.getValeurRubriquePartage().getAmount(),nddd).doubleValue();
		double tauxFinal = ClsStringUtil.truncateToXDecimal(this.fictivesalary.getValeurRubriquePartage().getRates(),nddd2).doubleValue();
		double baseFinal = ClsStringUtil.truncateToXDecimal(this.fictivesalary.getValeurRubriquePartage().getBase(),nddd2).doubleValue();
		double basePFinal = ClsStringUtil.truncateToXDecimal(this.fictivesalary.getValeurRubriquePartage().getBasePlafonnee(),nddd2).doubleValue();
		//En mémoire : 
		this.fictivesalary.getValeurRubriquePartage().setAmount(montantFinal);
		this.fictivesalary.getValeurRubriquePartage().setRates(tauxFinal);
		this.fictivesalary.getValeurRubriquePartage().setBase(baseFinal);
		this.fictivesalary.getValeurRubriquePartage().setBasePlafonnee(basePFinal);
	}

	/**
	 * CHARGEMENT DANS LA TABLE DES RUBRIQUES CALCULEES ET PACALC
	 * 
	 * @return true ou false
	 */
	public boolean insertRubriqueAlgo()
	{
		if ('O' == fictivesalary.param.getGenfile())
			fictivesalary.outputtext += "\n--In Rubrique Clone : >>insertRubriqueAlgo";
		ClsFictifRubriqueClone rubrique = this;
		if (fictivesalary.getValeurRubriquePartage().getAmount() == 0)
			return true;
		
		//On arrondi directement le montant calculé, avant de le garder en mémoire, et en bd
		this.arrondirFonctionNddd();
		// -- ----- Mise a jour des montants dans la table des rubriques calculees
		if (rubrique.getBase() == 0)
			rubrique.setBase(fictivesalary.getValeurRubriquePartage().getBase());
		else
			rubrique.setBase(rubrique.getBase() + fictivesalary.getValeurRubriquePartage().getBase());
		if (rubrique.getBasePlafonnee() == 0)
			rubrique.setBasePlafonnee(fictivesalary.getValeurRubriquePartage().getBasePlafonnee());
		else
			rubrique.setBasePlafonnee(rubrique.getBasePlafonnee() + fictivesalary.getValeurRubriquePartage().getBasePlafonnee());
		if (rubrique.getRates() == 0)
			rubrique.setRates(fictivesalary.getValeurRubriquePartage().getRates());
		else
			rubrique.setRates(rubrique.getRates() + fictivesalary.getValeurRubriquePartage().getRates());
		if (rubrique.getAmount() == 0)
			rubrique.setAmount(fictivesalary.getValeurRubriquePartage().getAmount());
		else
			rubrique.setAmount(rubrique.getAmount() + fictivesalary.getValeurRubriquePartage().getAmount());
		//
		// -------------------------------------------------------------------------
		// -- Insertion dans pacalc
		// -------------------------------------------------------------------------
		// ClsFictifRubriqueClone.derniereLigne ++;
		fictivesalary.incrementerDerniereLigne();
		//
		if (fictivesalary.param.isUseRetroactif())
		{
//			Rhtprcalc oRhtprcalc = new Rhtprcalc();
//			RhtprcalcPK key = new RhtprcalcPK(fictivesalary.getInfoSalary().getComp_id().getCdos(), fictivesalary.getInfoSalary().getComp_id().getNmat(), fictivesalary.param.getMonthOfPay(), fictivesalary.param
//					.getNumeroBulletin(), fictivesalary.getDerniereLigne(), rubrique.getRubrique().getComp_id().getCrub(), fictivesalary.param.getNlot());
//			oRhtprcalc.setArgu(fictivesalary.getEvparam().getArgu());
//			oRhtprcalc.setBasc(new BigDecimal(fictivesalary.getValeurRubriquePartage().getBase()));
//			oRhtprcalc.setBasp(new BigDecimal(fictivesalary.getValeurRubriquePartage().getBasePlafonnee()));
//			oRhtprcalc.setClas(fictivesalary.param.getClas());
//			oRhtprcalc.setMont(new BigDecimal(fictivesalary.getValeurRubriquePartage().getAmount()));
//			oRhtprcalc.setComp_id(key);
//			oRhtprcalc.setNprt(fictivesalary.getEvparam().getNprt());
//			oRhtprcalc.setRuba("");
//			oRhtprcalc.setTaux(new BigDecimal(fictivesalary.getValeurRubriquePartage().getRates()));
//			oRhtprcalc.setTrtb("1");
//			//
//			fictivesalary.getService().save(oRhtprcalc);
		}
		else
		{
			CalculPaie oCalculPaie = new CalculPaie();

            oCalculPaie.setIdEntreprise(Integer.valueOf(fictivesalary.getInfoSalary().getComp_id().getCdos()));
            oCalculPaie.setNmat(fictivesalary.getInfoSalary().getComp_id().getNmat());
            oCalculPaie.setAamm(fictivesalary.param.getMonthOfPay());
            oCalculPaie.setRubq(rubrique.getRubrique().getComp_id().getCrub());
            oCalculPaie.setNbul(fictivesalary.param.getNumeroBulletin());
            oCalculPaie.setArgu(fictivesalary.getEvparam().getArgu());
            oCalculPaie.setNlig(fictivesalary.getDerniereLigne());
			oCalculPaie.setBasc(new BigDecimal(fictivesalary.getValeurRubriquePartage().getBase()));
			oCalculPaie.setBasp(new BigDecimal(fictivesalary.getValeurRubriquePartage().getBasePlafonnee()));
			oCalculPaie.setClas(fictivesalary.param.getClas());
			oCalculPaie.setMont(new BigDecimal(fictivesalary.getValeurRubriquePartage().getAmount()));

			oCalculPaie.setNprt(fictivesalary.getEvparam().getNprt());
			oCalculPaie.setRuba("");
			oCalculPaie.setTaux(new BigDecimal(fictivesalary.getValeurRubriquePartage().getRates()));
			oCalculPaie.setTrtb("1");
			//
			
//			oCalculPaie.setBasc(ClsStringUtil.truncateTo3Decimal(oCalculPaie.getBasc()));
//			oCalculPaie.setBasp(ClsStringUtil.truncateTo3Decimal(oCalculPaie.getBasp()));
//			oCalculPaie.setTaux(ClsStringUtil.truncateTo3Decimal(oCalculPaie.getTaux()));
//			oCalculPaie.setMont(ClsStringUtil.truncateTo3Decimal(oCalculPaie.getMont()));
			
			oCalculPaie.setBasc(ClsStringUtil.truncateToXDecimal(oCalculPaie.getBasc(),3));
			oCalculPaie.setBasp(ClsStringUtil.truncateToXDecimal(oCalculPaie.getBasp(),3));
			oCalculPaie.setTaux(ClsStringUtil.truncateToXDecimal(oCalculPaie.getTaux(),3));
			oCalculPaie.setMont(ClsStringUtil.truncateToXDecimal(oCalculPaie.getMont(),3));
			
			fictivesalary.getService().save(oCalculPaie);
		}
		return true;
	}

	/**
	 * calcule l'arrondi en fonction des paramétres lus dans la table 63
	 * 
	 * @param param
	 *            les paramétres généraux du calcul de la paie
	 * @param montant
	 *            montant qu'on veut arrondir
	 * @return l'arrondi
	 */
	private double calculateArrondi(ClsFictifParameterOfPay param, double montant)
	{

		String libelle2 = fictivesalary.utilNomenclatureFictif.getLabelArrondi(fictivesalary.param.getListOfArrondi(), fictivesalary.param.getDossier(), 63, rubrique.getArro(), 2, fictivesalary.param.getNlot(), fictivesalary.param
				.getMonthOfPay());

		if (ClsObjectUtil.isNull(libelle2))
		{
			param.setError(param.errorMessage("ERR-90050", param.getLangue(), rubrique.getComp_id().getCrub(), rubrique.getArro()));
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "\n--In Rubrique Clone : " + "\n" + param.getError();
			param.setPbWithCalulation(true);
			return montant;
		}
		tempnumber = fictivesalary.utilNomenclatureFictif.getTauxArrondi(fictivesalary.param.getListOfArrondi(), fictivesalary.param.getDossier(), 63, rubrique.getArro(), 1, fictivesalary.param.getNlot(), fictivesalary.param
				.getMonthOfPay(), ClsEnumeration.EnColumnToRead.RATES);

		if (tempnumber == null || tempnumber.doubleValue() == 0)
		{
			param.setError(param.errorMessage("ERR-90051", param.getLangue(), rubrique.getComp_id().getCrub(), rubrique.getArro()));
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "\n--In Rubrique Clone : " + "\n" + param.getError();
			param.setPbWithCalulation(true);
			// return 0;
			return montant;
		}

		double taux = tempnumber.doubleValue();

		if ('O' == fictivesalary.param.getGenfile())
			fictivesalary.outputtext += "--Calcul de l'arrondi du montant " + montant + " Taux paramétré = " + taux;
		double result = montant / taux;

		if ('O' == fictivesalary.param.getGenfile())
			fictivesalary.outputtext += "--Calcul de l'arrondi du montant " + montant + " montat/taux = " + result;

		double partieEntiereResult = 0;
		if ("I".equals(libelle2))
		{
			partieEntiereResult = Math.floor(result);
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "Arrondi é I, partie entiere +Math.floor(result) " + partieEntiereResult;
		}
		else if ("S".equals(libelle2))
		{
			partieEntiereResult = Math.floor(result) + 1;
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "Arrondi é S, partie entiere +Math.floor(result) +1 " + partieEntiereResult;
		}
		else if ("N".equals(libelle2))
		{
			// partieEntiereResult = result;
			partieEntiereResult = Math.round(result);
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "Arrondi é N, partie entiere +Math.floor(result) " + partieEntiereResult;
		}
		else
		{
			return montant;
		}
		if ('O' == fictivesalary.param.getGenfile())
			fictivesalary.outputtext += "\n--In Rubrique Clone : Arrondi Final partieEntiereResult * taux = " + partieEntiereResult * taux;
		return partieEntiereResult * taux;
	}

	/**
	 * => cal_base Calcul de la base de cette rubrique Le calcul tient compte de tous les éléments variables du salarié
	 */
	public void calculateBase()
	{
		// if('O' == fictivesalary.param.getGenfile()) fictivesalary.outputtext+="\n--In Rubrique Clone : >>calculateBase");
		// CURSOR curs_evar IS
		// SELECT mont, argu, nprt, ruba FROM paevar
		// WHERE cdos = PA_CALCUL.wpdos.cdos
		// AND nmat = PA_CALCUL.wsal01.nmat
		// AND aamm = PA_CALCUL.w_aamm
		// AND nbul = PA_CALCUL.wsd_fcal1.nbul
		// AND rubq = PA_CALCUL.t_rub.crub;
		//
		// String queryString = "select mont, argu, nprt, ruba from Rhteltvardet"
		// + " where cdos = '" + fictivesalary.param.getDossier() + "'"
		// + " and nmat = '" + fictivesalary.getInfoSalary().getComp_id().getNmat() + "'"
		// + " and aamm = '" + fictivesalary.param.getMonthOfPay() + "'"
		// + " and nbul = " + fictivesalary.param.getNumeroBulletin()
		// + " and rubq = '" + rubrique.getComp_id().getCrub() + "'";
		// CURSOR curs_evar2 IS
		// SELECT mont, argu, nprt, ruba FROM pahevar
		// WHERE cdos = PA_CALCUL.wpdos.cdos
		// AND nmat = PA_CALCUL.wsal01.nmat
		// AND aamm = PA_CALCUL.w_aamm
		// AND nbul = PA_CALCUL.wsd_fcal1.nbul
		// AND rubq = PA_CALCUL.t_rub.crub;
		// String queryStringRetro = "select mont, argu, nprt, ruba from Rhthevar"
		// + " where cdos = '" + fictivesalary.param.getDossier() + "'"
		// + " and nmat = '" + fictivesalary.getInfoSalary().getComp_id().getNmat() + "'"
		// + " and aamm = '" + fictivesalary.param.getMonthOfPay() + "'"
		// + " and nbul = " + fictivesalary.param.getNumeroBulletin()
		// + " and rubq = '" + rubrique.getComp_id().getCrub() + "'";
		//
		// BEGIN
		// -- ----- Initialisations
		// PA_CALCUL.w_bas := 0;
		// PA_CALCUL.val_elmt := 0;
		// PA_CALCUL.nbr_elmt := 0;
		// PA_CALCUL.w_transit := 0;
		// PA_CALCUL.appl_elmt := FALSE;
		// PA_CALCUL.Rbq_en_EF := FALSE;
		// PA_CALCUL.Rbq_en_EV := FALSE;
		// PA_CALCUL.w_argu := ' ';
		// PA_CALCUL.wevar.nprt := ' ';
		// PA_CALCUL.wevar.ruba := ' ';
		this.fictivesalary.getValeurRubriquePartage().setBase(0);
		// this.fictivesalary.getValeurRubriquePartage().setAmount(0);
		// this.fictivesalary.getValeurRubriquePartage().setBasePlafonnee(0);
		// this.fictivesalary.getValeurRubriquePartage().setRates(0);
		this.setElementVariableValeur(0);
		this.setElementVariableTransit(0);
		this.setElementVariableApply(false);
		this.setRubriqueEV(false);
		this.setRubriqueEF(false);

		//
		// -- Si algo 13 et (aucun prets charge ?) ALORS suppression des prets
		// -- saisis en EV
		// IF PA_CALCUL.t_rub.algo = 13 AND PA_CALCUL.nb_prets != 0 THEN
		// PA_CALCUL.W_Faitout := PA_CALCUL.sup_prets_ev;
		// END IF;

		if (rubrique.getAlgo() == 13)
		{
			if (!fictivesalary.getListLignePretMap().isEmpty() && fictivesalary.getListLignePretMap().containsKey(rubrique.getComp_id().getCrub()))
				this.removeElementVariableFromLoans();
		}

		//
		// ------------------------------------------------------------------------------
		// -- RECHERCHE ELEMENT VARIABLE
		// ------------------------------------------------------------------------------
		// PA_CALCUL.nbr_elmt := 0;
		// IF PA_CALCUL.retroactif THEN
		// --
		// OPEN curs_evar2;
		// ELSE
		// OPEN curs_evar;
		// END IF;
		// LOOP
		// IF PA_CALCUL.retroactif THEN
		// FETCH curs_evar2 INTO PA_CALCUL.w_bas, PA_CALCUL.w_argu, PA_CALCUL.wevar.nprt, PA_CALCUL.wevar.ruba;
		// EXIT WHEN curs_evar2%NOTFOUND;
		// ELSE
		// FETCH curs_evar INTO PA_CALCUL.w_bas, PA_CALCUL.w_argu, PA_CALCUL.wevar.nprt, PA_CALCUL.wevar.ruba;
		// EXIT WHEN curs_evar%NOTFOUND;
		// END IF;
		//
		// PA_CALCUL.nbr_elmt := PA_CALCUL.nbr_elmt + 1;
		// PA_CALCUL.tab_elmt_mont(PA_CALCUL.nbr_elmt) := PA_CALCUL.w_bas;
		// PA_CALCUL.tab_elmt_argu(PA_CALCUL.nbr_elmt) := PA_CALCUL.w_argu;
		// PA_CALCUL.tab_elmt_nprt(PA_CALCUL.nbr_elmt) := PA_CALCUL.wevar.nprt;
		// PA_CALCUL.tab_elmt_ruba(PA_CALCUL.nbr_elmt) := PA_CALCUL.wevar.ruba;
		// END LOOP;
		// if(fictivesalary.param.isUseRetroactif())
		// listOfElementVariable = fictivesalary.getService().find(queryStringRetro);
		// else
		// listOfElementVariable = fictivesalary.getService().find(queryString);
		// IF PA_CALCUL.retroactif THEN
		// CLOSE curs_evar2;
		// ELSE
		// CLOSE curs_evar;
		// END IF;
		//
		if (!fictivesalary.getListOfEltvar().isEmpty() && fictivesalary.getListOfEltvar().containsKey(rubrique.getComp_id().getCrub()))
			listOfElementVariable = fictivesalary.getListOfEltvar().get(rubrique.getComp_id().getCrub());
		else
		{
			if (listOfElementVariable == null)
				listOfElementVariable = new ArrayList();
		}
		// IF PA_CALCUL.nbr_elmt != 0 THEN
		if (listOfElementVariable.size() > 0)
		{
			Object[] obj = (Object[]) listOfElementVariable.get(0);
			// PA_CALCUL.w_bas := PA_CALCUL.tab_elmt_mont(1);
			// PA_CALCUL.val_elmt := PA_CALCUL.w_bas;
			// PA_CALCUL.w_argu := PA_CALCUL.tab_elmt_argu(1);
			// PA_CALCUL.wevar.nprt := PA_CALCUL.tab_elmt_nprt(1);
			// PA_CALCUL.wevar.ruba := PA_CALCUL.tab_elmt_ruba(1);
			// PA_CALCUL.val_elmt := PA_CALCUL.tab_elmt_mont(1);
			// PA_CALCUL.appl_elmt := TRUE;
			// PA_CALCUL.Rbq_en_EV := TRUE;
			// if('O' == fictivesalary.param.getGenfile()) fictivesalary.outputtext+="\n--In Rubrique Clone : >>...listOfElementVariable.size() ok");
			this.fictivesalary.getValeurRubriquePartage().setBase(((BigDecimal) (obj[0] != null ? obj[0] : 0)).doubleValue());
			this.setElementVariableValeur(((BigDecimal) (obj[0] != null ? obj[0] : 0)).doubleValue());
			this.setElementVariableTransit(0);
			this.setElementVariableApply(true);
			this.setRubriqueEV(true);
			if (!ClsObjectUtil.isNull(obj[1]))
				fictivesalary.evparam.setArgu((String) obj[1]);
			if (!ClsObjectUtil.isNull(obj[2]))
				fictivesalary.evparam.setNprt((String) obj[2]);
			if (!ClsObjectUtil.isNull(obj[3]))
				fictivesalary.evparam.setRuba((String) obj[3]);
		}
		// END IF;
		//
		// -----------------------------------------------------------------------------
		// -- RECHERCHE ELEMENT PERSONNEL
		// -----------------------------------------------------------------------------
		// IF NOT PA_CALCUL.appl_elmt OR PA_CALCUL.t_rub.algo = 28
		// THEN
		if (!this.isElementVariableApply() || rubrique.getAlgo() == 28 || rubrique.getAlgo() == 1)
		{
			// if('O' == fictivesalary.param.getGenfile()) fictivesalary.outputtext+="\n--In Rubrique Clone : >>...algo=28 ok (pas d'éléments variables)");
			// IF PA_CALCUL.retroactif THEN
			if (fictivesalary.param.isUseRetroactif())
			{
				// -- test si donnee evenement
				tempnumber = fictivesalary.utilNomenclatureFictif.getAmountOrRateFromSalaryEventData(fictivesalary.param.getDossier(), fictivesalary.getInfoSalary().getComp_id().getNmat(), fictivesalary.param.getNlot(), "R"
						.concat(rubrique.getComp_id().getCrub()));
				if (tempnumber == null)
				{
					// IF NOT paf_EvenSalmnt(PA_CALCUL.w_nlot,'R'||PA_CALCUL.t_rub.crub,PA_CALCUL.wsal01.nmat,PA_CALCUL.w_transit) THEN
					// BEGIN
					// SELECT monp INTO PA_CALCUL.w_transit FROM pahelfix
					// WHERE cdos = PA_CALCUL.wpdos.cdos
					// AND nmat = PA_CALCUL.wsal01.nmat
					// AND codp = PA_CALCUL.t_rub.crub
					// AND aamm = PA_CALCUL.w_aamm;
					// EXCEPTION
					// WHEN OTHERS THEN null;
					// END;
//					Rhthelfix obj = (Rhthelfix) fictivesalary.getService().get(
//							Rhthelfix.class,
//							new RhthelfixPK(fictivesalary.param.getDossier(), fictivesalary.getInfoSalary().getComp_id().getNmat(), rubrique.getComp_id().getCrub(), fictivesalary.param.getMonthOfPay(), fictivesalary.param
//									.getNumeroBulletin()));
//					if (obj != null)
//					{
//						this.setElementVariableTransit(obj.getMonp().doubleValue());
//						this.fictivesalary.getValeurRubriquePartage().setBase(obj.getMonp().doubleValue());
//						this.setElementVariableValeur(obj.getMonp().doubleValue());
//						this.setElementVariableApply(true);
//						this.setRubriqueEF(true);
//					}
					// END IF;
				}
				else
					this.setElementVariableTransit(tempnumber.doubleValue());
			}
			// ELSE
			else
			{
				// BEGIN
				// SELECT monp INTO PA_CALCUL.w_transit FROM paelfix
				// WHERE cdos = PA_CALCUL.wpdos.cdos
				// AND nmat = PA_CALCUL.wsal01.nmat
				// AND codp = PA_CALCUL.t_rub.crub
				// AND (ddeb IS NULL OR
				// (ddeb IS NOT NULL AND ddeb <= TO_DATE(PA_CALCUL.w_aamm,'YYYYMM')))
				// AND (dfin IS NULL OR
				// (dfin IS NOT NULL AND dfin >= TO_DATE(PA_CALCUL.w_aamm,'YYYYMM')));
				// EXCEPTION
				// WHEN OTHERS THEN null;
				// END;
				// String complexQuery = "select monp from Rhteltfixagent" +
				// " where cdos = '" + fictivesalary.param.getDossier() + "'" +
				// " and nmat = '" + fictivesalary.getInfoSalary().getComp_id().getNmat() + "'" +
				// " and codp = '" + rubrique.getComp_id().getCrub() + "'" +
				// " and (ddeb is null or ((ddeb is not null) and (ddeb <= '" + new ClsDate(fictivesalary.param.getMyMonthOfPay().getFirstDayOfMonth()).getDateS() +
				// "')))" +
				// " and (dfin is null or ((dfin is not null) and (dfin >= '" + new ClsDate(fictivesalary.param.getMyMonthOfPay().getFirstDayOfMonth()).getDateS() +
				// "')))";
				List l = new ArrayList();
				if (!fictivesalary.getListOfEltfix().isEmpty() && fictivesalary.getListOfEltfix().containsKey(rubrique.getComp_id().getCrub()))
					l = fictivesalary.getListOfEltfix().get(rubrique.getComp_id().getCrub());
				if (l != null && l.size() > 0)
				{
					if (l.get(0) != null)
					{
						double d = ClsObjectUtil.getDoubleFromObject(l.get(0));
						this.setElementVariableTransit(d);
						this.fictivesalary.getValeurRubriquePartage().setBase(d);
						this.fictivesalary.getValeurRubriquePartage().setBasePlafonnee(d);
						this.setElementVariableValeur(d);
						this.setElementVariableApply(true);
						this.setRubriqueEF(true);
//						System.out.println("Elet code:"+rubrique.getComp_id().getCrub());
//						System.out.println("Elet valeur:"+d);
					}
				}
			}
			// END IF;
			//
			// IF NOT SQL%NOTFOUND THEN
			// PA_CALCUL.w_bas := PA_CALCUL.w_transit;
			// PA_CALCUL.val_elmt := PA_CALCUL.w_transit;
			// PA_CALCUL.appl_elmt := TRUE;
			// PA_CALCUL.Rbq_en_EF := TRUE;
			// PA_CALCUL.nbr_elmt := 1;
			// END IF;
		}
		// END IF;
		//
		// ---------------------------------------------------------------------------
		// -- Calcul selon la formule stockee pour la rubrique en question
		// ---------------------------------------------------------------------------
		// --IF NOT appl_elmt OR t_rub.algo = 6 OR t_rub.algo = 7 OR
		// -- t_rub.algo = 18 OR t_rub.algo = 28
		// IF NOT PA_CALCUL.appl_elmt OR PA_CALCUL.t_rub.algo IN (6, 18, 28 )
		// THEN
		if ((!this.isElementVariableApply()) || rubrique.getAlgo() == 6 || rubrique.getAlgo() == 18 || rubrique.getAlgo() == 28)
		{
			// if('O' == fictivesalary.param.getGenfile()) fictivesalary.outputtext+="\n--In Rubrique Clone : >>...algo=6,18,28 ok");
			// IF PA_CALCUL.t_rub.basc = 'O' THEN
			// PA_CALCUL.nb_rbqbas := 0; -- nb_rbqbas permet de savoir si la
			// IF PA_CALCUL.t_rub.trtc = 'N' THEN -- formule de calcul a deja ete lue
			// PA_CALCUL.w_bas := base_mdp; -- et chargee dans tab_basc[], car
			// ELSE -- base_cumul() peut faire appel a
			// PA_CALCUL.w_bas := base_cumul; -- base_mdp()
			// END IF;
			// END IF;
			if ("O".equals(rubrique.getBasc()))
			{
				// if('O' == fictivesalary.param.getGenfile()) fictivesalary.outputtext+="\n--In Rubrique Clone : >>...basc=O ok");
				if ("N".equals(rubrique.getTrtc()))
				{
					// if('O' == fictivesalary.param.getGenfile()) fictivesalary.outputtext+="\n--In Rubrique Clone : >>...trtc=N ok");
					this.fictivesalary.getValeurRubriquePartage().setBase(this.calculateBaseMonthOfPay());
					if(fictivesalary.dern_mois_de_conge && StringUtils.equals(rubrique.getCabf(), "U"))
					{
						double bs = this.fictivesalary.getValeurRubriquePartage().getBase();
						this.fictivesalary.getValeurRubriquePartage().setBase(bs + this.calculateBaseFictif());
					}
				}
				else
				{
					this.fictivesalary.getValeurRubriquePartage().setBase(this.calculateBaseOnCumul());
				}
			}
		}
		// END IF;
		//
		// IF PA_CALCUL.nbr_elmt = 0 THEN
		// PA_CALCUL.nbr_elmt := 1;
		// END IF;
		if (listOfElementVariable == null)
		{
			listOfElementVariable = new ArrayList();
		}
		//
		// if('O' == fictivesalary.param.getGenfile()) fictivesalary.outputtext+="\n--In Rubrique Clone : ...............ElementVariableApply :" +
		// isElementVariableApply());
		// if('O' == fictivesalary.param.getGenfile()) fictivesalary.outputtext+="\n--In Rubrique Clone : ...getBase : " +
		// this.fictivesalary.getValeurRubriquePartage().getBase());
		// if('O' == fictivesalary.param.getGenfile()) fictivesalary.outputtext+="\n--In Rubrique Clone : <<Fin calculateBase\n");
	}
	
	private double calculateBaseFictif()
	{
//		FUNCTION base_pafic RETURN NUMBER
//		IS
//
//		  w_bas NUMBER(15,3);
//		  ind   NUMBER(5);
//		  i     NUMBER(5);
//		  w_per VARCHAR2(6);
//
//		BEGIN
//
//		   w_bas := 0;
//		   w_per := memo_mdp;
//		   t_rub.lbtm := 'M';
//		   WHILE w_per < w_aamm LOOP
//		      w_bas := w_bas + calc_formule(w_per);
//		      w_per := TO_CHAR(ADD_MONTHS(TO_DATE(w_per,'YYYYMM'),1),'YYYYMM');
//		   END LOOP;
//
//		   RETURN NVL(w_bas,0);
//
//		END base_pafic;
		double base = 0;
		String w_per = fictivesalary.param.getFictiveMonthOfPay();
		rubrique.setLbtm("M");
		ClsDate date = null;
		while(w_per.compareTo(fictivesalary.param.monthOfPay) < 0)
		{
			base += calculateFormule(w_per);
			date =  new ClsDate(w_per,ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
			date = new ClsDate(date.addMonth(1));
			w_per = date.getYearAndMonth();
		}
		
		return base;
	}

	private String _computeMinOfMonthForCumul()
	{
		String strSqlQuery = "select min(comp_id.aamm) from CumulPaie " + " where comp_id.cdos = '" + fictivesalary.param.getDossier() + "'" + " and comp_id.nmat = '"
				+ fictivesalary.getInfoSalary().getComp_id().getNmat() + "'";

		List aammmin = fictivesalary.getService().find(strSqlQuery);

		if (aammmin.size() > 0)
		{
			if (aammmin.get(0) != null)
				return (String) aammmin.get(0);
		}
		return null;

	}

	/**
	 * Retourner la chaine correspondant é la liste des périodes é traiter pour les cumuls du salarié
	 */

	private String _computerMonthToCumul(String firstMonth, int numberOfMonth, int i_annee, int j_mois)
	{

		String moismin = this._computeMinOfMonthForCumul();
		if (StringUtils.isBlank(moismin))
		{
			return "";
		}

		String moi1 = firstMonth;

		int nmois = numberOfMonth;

		String result = "";

		int index = -1;

		String moisPeriode = null;

		List<String> resultList = new ArrayList<String>();

		for (int idx = 0; idx < nmois; idx++)
		{
			moisPeriode = StringUtils.substring(moi1, 4, 6);
			if (!StringUtils.equals("00", moisPeriode))
			{
				if (index % 80 == 0 && index != 0)
				{
					if (StringUtils.isNotBlank(result))
						resultList.add(result);
					result = "";
				}

				if (moi1.equals(fictivesalary.param.getMonthOfPay()))
				{
					if ("O".equals(rubrique.getMopa()))
					{
						result += ",'" + moi1 + "'";
					}
				}
				else
				{
					if ((moi1.compareToIgnoreCase(fictivesalary.param.getMonthOfPay()) < 0) && (moi1.compareToIgnoreCase(moismin) >= 0))
					{
						index++;
						result += ",'" + moi1 + "'";
					}
				}
			}

			j_mois = j_mois + 1;
			if (j_mois > 12)
			{
				j_mois = 1;
				i_annee = i_annee + 1;
			}

			moi1 = ClsStringUtil.formatNumber(i_annee, ClsFictifParameterOfPay.FORMAT_OF_YEAR) + ClsStringUtil.formatNumber(j_mois, "00");
		}
		if ("O".equals(rubrique.getMopa()) && ("N".equals(rubrique.getTrve()) || "C".equals(rubrique.getTrve())|| "M".equals(rubrique.getTrve())))
		{
			result += ",'" + fictivesalary.param.getMonthOfPay() + "'";
		}

		if (StringUtils.isNotBlank(result))
		{
			resultList.add(result);
			result = "";
		}

		String string = null;
		for (int kk = 0; kk < resultList.size(); kk++)
		{
			string = resultList.get(kk);
			if (string.startsWith(","))
				string = string.replaceFirst(",", "");
			if (kk == 0)
				result = " ( comp_id.aamm in (" + string + ")";
			else
				result += " or comp_id.aamm in (" + string + ")";
		}
		if (StringUtils.isNotBlank(result))
			result += " )";
		return result;
	}

	

	/**
	 * => base_cumul Calcule la base sur les cumuls
	 * 
	 * @return le montant de la base sur les cumuls
	 */
	private double calculateBaseOnCumul()
	{
		//A revoir
		//ClsDate myExercice = new ClsDate(fictivesalary.param.getDossierDateDebutExercice(), this.fictivesalary.param.getAppDateFormat());
		//ClsDate myPreviousMontOfPay = new ClsDate(new ClsDate(fictivesalary.param.getMyMonthOfPay().getDate()).addMonth(-1));	
		ClsDate myExercice = new ClsDate(fictivesalary.param.getFictiveDossierDateDebutExercice(), this.fictivesalary.param.getAppDateFormat());
		ClsDate myPreviousMontOfPay = new ClsDate(new ClsDate(fictivesalary.param.getFictiveMyMonthOfPay().getDate()).addMonth(-1));

		int i = 0; // 2007
		int j = 0; // 8
		int k = 0;
		int l = 0;
		int nmois = 0;
		String moi1 = "";
		// -- ----- Initialisations
		// w_bas := 0;
		this.fictivesalary.getValeurRubriquePartage().setBase(0);
		//
		// ---------------------------------------------------------------------------------
		// -- Calcul des mois concernes
		// ---------------------------------------------------------------------------------
		//
		// IF PA_CALCUL.t_rub.trve = 'O' THEN
		if ("O".equals(rubrique.getTrve()))
		{
			// -- ----- Calcul sur un exercice
			// i := TO_NUMBER(TO_CHAR(PA_CALCUL.wpdos.ddex,'YYYY')); -- annee
			// j := TO_NUMBER(TO_CHAR(PA_CALCUL.wpdos.ddex,'MM')); -- mois
			// k := PA_CALCUL.t_rub.exo;
			i = myExercice.getYear();

			j = myExercice.getMonth();

			if (!ClsObjectUtil.isNull(rubrique.getExo()))
				k = Integer.valueOf(rubrique.getExo());// 2007?
			// i := i - k;
			i = i - k;
			//
			// k := PA_CALCUL.t_rub.val1; -- calcul du k eme mois
			if (!ClsObjectUtil.isNull(rubrique.getVal1()))
				k = Integer.valueOf(rubrique.getVal1());
			else
				k = 0;
			// l := PA_CALCUL.t_rub.val2; -- au l eme mois
			if (!ClsObjectUtil.isNull(rubrique.getVal2()))
				l = Integer.valueOf(rubrique.getVal2());

			// nb_mois := l - k + 1; -- soit nb_mois
			nmois = l - k + 1;// et si l -k <= -2
			// j := j + k - 1;
			j = j + k - 1;
			// IF j > 12 THEN
			// j := j - 12;
			// i := i + 1;
			// END IF;
			if (j > 12)
			{// je suis au j-iéme mois de l'année suivante
				j = j - 12;
				i = i + 1;// contient le nombre d'années
			}
			// moi1 := LTRIM(TO_CHAR(i,'0999'))||LTRIM(TO_CHAR(j,'09'));
			//
		}
		// ELSE
		if ("N".equals(rubrique.getTrve()))
		{
			// -- ----- Calcul sur les derniers mois
			// i := TO_NUMBER(TO_CHAR(PA_CALCUL.wpdos.ddmp,'YYYY')); -- annee
			// j := TO_NUMBER(TO_CHAR(PA_CALCUL.wpdos.ddmp,'MM')); -- mois
			// k := PA_CALCUL.t_rub.val3; -- calcul sur les k derniers mois

			i = myPreviousMontOfPay.getYear();

			j = myPreviousMontOfPay.getMonth();

			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "\nInit, année = " + i;
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "\nInit, mois = " + j;

			k = 0;
			if (!ClsObjectUtil.isNull(rubrique.getVal3()))
				k = Integer.valueOf(rubrique.getVal3());
			
			// spécifique cnss
//			if (StringUtils.equals(this.fictivesalary.param.nomClient, ClsEntreprise.CNSS))
//			{
//				if(rubrique.getAlgo() == 68)
//				{
//					ClsDate myPMCF = new ClsDate(new ClsDate(fictivesalary.getInfoSalary().getPmcf(), ClsFictifParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getDate());
//					 i = myPMCF.getYear();
//					 j = myPMCF.getMonth();
//					 j = j - 1;
//					 if(j < 1)
//					 {
//						 j += 12;
//						 i = i - 1;
//					 }
//				}
//
//			}

			// j := j - k + 1;
			j = j - k + 1;// et si j -k <= -2
			// IF j < 1 THEN
			// j := j + 12;
			// i := i - 1;
			// END IF;
			while (j < 1)
			{// je suis au j-iéme mois de l'année précédante
				j = j + 12;
				i = i - 1;// 2007-1=2006
			}
			//
			// moi1 := LTRIM(TO_CHAR(i,'0999'))||LTRIM(TO_CHAR(j,'09'));
			// nb_mois := k;

			nmois = k;

		}
		/**
		 * Cas du cumul sur un congé, on détermine le nombre de mois dépuis la date de dernier retour de congé Si la date de retour du dernier congé est null,
		 * on prend la date d'entrée dans la société
		 */
		if ("C".equals(rubrique.getTrve()))
		{
			ClsDate oDrtcg = new ClsDate(fictivesalary.getInfoSalary().getDrtcg() != null ? fictivesalary.getInfoSalary().getDrtcg() : fictivesalary.getInfoSalary().getDtes());

			moi1 = oDrtcg.getYearAndMonth();
			i = oDrtcg.getYear();
			j = oDrtcg.getMonth();
			if(this.fictivesalary.param.table91LabelNotEmpty)
			{
				String moi11 = fictivesalary.getService().getMoisPaieReel(fictivesalary.param.dossier, oDrtcg.getDateS("dd/MM/yyyy"));
				if(!"0".equalsIgnoreCase(moi11))
				{
					moi1 = moi11;
					i = new ClsDate(moi11,ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getYear();
					j = new ClsDate(moi11,ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getMonth();
				}
			}
			nmois = ClsDate.getMonthsBetween(oDrtcg.getDate(), fictivesalary.param.getFirstDayOfMonth());

		}
		//Cumul uniquement du mois stocké en val3
		if ("M".equals(rubrique.getTrve()))
		{
			// -- ----- Calcul sur le mois val3
			i = myPreviousMontOfPay.getYear();

			j = myPreviousMontOfPay.getMonth();

			k = 0;
			if (!ClsObjectUtil.isNull(rubrique.getVal3()))
				k = Integer.valueOf(rubrique.getVal3());
			
			j = j - k + 1;
			
			while (j < 1)
			{
				j = j + 12;
				i = i - 1;
			}
			//On fixe le nombre de mois é 1 vu qu'on ne veut qu'un mois de cumul
			nmois = 1;

		}
		moi1 = ClsStringUtil.formatNumber(i, ClsFictifParameterOfPay.FORMAT_OF_YEAR) + ClsStringUtil.formatNumber(j, "00");

		// END IF;
		//
		// ---------------------------------------------------------------------------------
		// -- Chargement du tableau, une base par mois
		// ---------------------------------------------------------------------------------
		//
		// moi := moi1;
		// i := TO_NUMBER(SUBSTR(moi1,5,2));
		// j := TO_NUMBER(SUBSTR(moi1,1,4));
		// on permute i et j de tel sorte que j devient l'année et i le mois
		k = i;
		i = j;
		j = k;
		if(StringUtils.equals("3805", rubrique.getComp_id().getCrub()))
		System.out.println("Rubrique "+rubrique.getComp_id().getCrub()+" Mois1 "+moi1+" i = "+i+" et j = "+j+" et Mois Courant "+fictivesalary.param.getMonthOfPay());
		/**
		 * C'est ici qu'on va recupérer les cumuls pour la rubrique courante et pour le salarie
		 */
		if ('O' == fictivesalary.param.getGenfile())
			fictivesalary.outputtext += "\nPremier Mois pour le calcul du cumul = " + moi1;
		if ('O' == fictivesalary.param.getGenfile())
			fictivesalary.outputtext += "\nNombre de mois du cumul = " + nmois;
		if ('O' == fictivesalary.param.getGenfile())
			fictivesalary.outputtext += "\nAnnee depart = " + j + " et mois depart = " + i;
		
		//
		// FOR k IN 1..nb_mois LOOP
		double[] mont = new double[nmois + 1];
		for (int idx = 0; idx <= nmois; idx++)
		{
			mont[idx] = 0;
		}
//		FOR k IN 1..nb_mois LOOP
//	      IF moi = w_aamm THEN
//	         IF t_rub.mopa = 'O' THEN
//	            mont(k) := Calc_formule(moi);
//	            IF PA_PAIE.NouZ(mont(k)) THEN
//	               mont(k) := 0;
//	            END IF;
//	            --mont(k) := mont(k) + base_mdp;
//	         ELSE
//	            mont(k) := 0;
//	         END IF;
//	      ELSE
//	         IF moi < w_aamm THEN
//	            mont(k) := Calc_formule(moi);
//	            IF PA_PAIE.NouZ(mont(k)) THEN
//	               mont(k) := 0;
//	            END IF;
//	         ELSE
//	            mont(k) := 0;
//	         END IF;
//	      END IF;
//	      i := i + 1;
//	      IF i > 12 THEN
//	         i := 1;
//	         j := j + 1;
//	      END IF;
//	      moi := LTRIM(TO_CHAR(j,'0999'))||LTRIM(TO_CHAR(i,'09'));
//	   END LOOP;
		boolean moiscourantdejacalcule = false;
		for (int idx = 0; idx < nmois; idx++)
		{
			// IF moi = PA_CALCUL.w_aamm THEN
			if (moi1.equals(fictivesalary.param.getMonthOfPay()))
			{
				// IF PA_CALCUL.t_rub.mopa = 'O' THEN
				if ("O".equals(rubrique.getMopa()) && !moiscourantdejacalcule)
				{
					moiscourantdejacalcule = true;
					// mont(k) := Calc_formule(moi);
					mont[idx] = this.calculateFormule(moi1);
					// IF PA_PAIE.NouZ(mont(k)) THEN
					// mont(k) := 0;
					// END IF;
				}
			}
			// ELSE
			else
			{
				// IF moi < PA_CALCUL.w_aamm THEN
				// mont(k) := Calc_formule(moi);
				// ELSE
				// mont(k) := 0;
				// END IF;
				if (moi1.compareToIgnoreCase(fictivesalary.param.getMonthOfPay()) < 0)
					mont[idx] = this.calculateFormule(moi1);
				else
					mont[idx] = 0;
			}
			// END IF;
			// i := i + 1;
			// IF i > 12 THEN
			// i := 1;
			// j := j + 1;
			// END IF;
			i = i + 1;
			if (i > 12)
			{
				i = 1;
				j = j + 1;
			}
			// moi := LTRIM(TO_CHAR(j,'0999'))||LTRIM(TO_CHAR(i,'09'));
			moi1 = ClsStringUtil.formatNumber(j, ClsFictifParameterOfPay.FORMAT_OF_YEAR) + ClsStringUtil.formatNumber(i, "00");
		}
		// END LOOP;
		//
		// -- ----- Ajout du mois de paye si necessaire
		//
		// IF PA_CALCUL.t_rub.mopa = 'O' AND PA_CALCUL.t_rub.trve = 'N' THEN
		// nb_mois := nb_mois + 1;
		// -- Recuperation eventuelle des bulletins supplementaires du mois
		// -- en cours de traitement.
		// mont(nb_mois) := calc_formule(PA_CALCUL.w_aamm) ;
		// IF PA_PAIE.NouZ(nb_mois) THEN
		// mont(nb_mois) := 0;
		// END IF;
		// mont(nb_mois) := mont(nb_mois) + base_mdp ;
		// END IF;
		if ("O".equals(rubrique.getMopa()) && ("N".equals(rubrique.getTrve()) || "C".equals(rubrique.getTrve())|| "M".equals(rubrique.getTrve())) && !moiscourantdejacalcule)
		{
			nmois++;
			mont[nmois - 1] = calculateFormule(fictivesalary.param.getMonthOfPay());
		}
		if ('O' == fictivesalary.param.getGenfile())
			fictivesalary.outputtext += "\nNmois final = "+nmois ;

		//
		// ---------------------------------------------------------------------------------
		// -- ----- Application de l'operation finale
		// ---------------------------------------------------------------------------------
		//
		// IF PA_CALCUL.t_rub.opfi = 'S' THEN ------ Calcul de la somme
		// total_mois := 0;
		// FOR i IN 1..nb_mois LOOP
		// IF NOT PA_PAIE.NouZ(mont(i)) THEN
		// total_mois := total_mois + mont(i);
		// END IF;
		// END LOOP;
		// w_bas := total_mois;
		//
		// ELSIF PA_CALCUL.t_rub.opfi = 'O' THEN ------ Calcul de la moyenne
		// pmcum := 0;
		// total_mois := 0;
		// nb_mois_ok := 0;
		// FOR i IN 1..nb_mois LOOP
		// IF mont(i) IS NOT NULL THEN
		// total_mois := total_mois + mont(i);
		// pmcum := 1;
		// END IF;
		// IF pmcum = 1 THEN
		// nb_mois_ok := nb_mois_ok + 1;
		// END IF;
		// END LOOP;
		// IF nb_mois_ok != 0 THEN
		// w_bas := total_mois / nb_mois_ok;
		// ELSE
		// w_bas := 0;
		// END IF;
		// ELSIF PA_CALCUL.t_rub.opfi = 'A' THEN ------ Recherche du maximum
		// moi_max := 0;
		// FOR i IN 1..nb_mois LOOP
		// IF mont(i) > moi_max THEN
		// moi_max := mont(i);
		// END IF;
		// END LOOP;
		// w_bas := moi_max;
		// ELSIF PA_CALCUL.t_rub.opfi = 'P' THEN ------ Recherche du maximum
		// pmcum := 0;
		// total_mois := 0;
		// nb_mois_ok := 0;
		// WHILE PA_PAIE.NouZ(nb_mois) LOOP
		// nb_mois := nb_mois - 1;
		// IF PA_PAIE.NouZ(nb_mois) THEN
		// EXIT ;
		// END IF;
		// END LOOP;
		//
		// FOR i IN 1..nb_mois LOOP
		// IF NOT PA_PAIE.NouZ(mont(i)) THEN
		// total_mois := total_mois + mont(i);
		// pmcum := 1;
		// END IF;
		// IF pmcum = 1 THEN
		// nb_mois_ok := nb_mois_ok + 1;
		// END IF;
		// END LOOP;
		// IF nb_mois_ok != 0 THEN
		// w_bas := (total_mois / nb_mois_ok) * 12;
		// ELSE
		// w_bas := 0;
		// END IF;
		// END IF;
		// double base = 0;
		
		double totalMontant = 0;
		if ("S".equals(rubrique.getOpfi())) //Somme
		{
			totalMontant = 0;
			for (int idx = 0; idx < nmois; idx++)
			{
				totalMontant += mont[idx];
			}
			this.fictivesalary.getValeurRubriquePartage().setBase(totalMontant);
		}
		else if ("O".equals(rubrique.getOpfi())) //Moyenne pondérée
		{
			int nombreMoisCorrect = 0;
			totalMontant = 0;
			for (int idx = 0; idx < nmois; idx++)
			{
				if (mont[idx] > 0)
				{
					totalMontant += mont[idx];
					nombreMoisCorrect++;
				}
					
			}
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "\nTotal = "+totalMontant+" et Nombre de mois correct = " + nombreMoisCorrect ;
			if (nombreMoisCorrect != 0)
				this.fictivesalary.getValeurRubriquePartage().setBase(totalMontant / nombreMoisCorrect);
			else
				this.fictivesalary.getValeurRubriquePartage().setBase(0);
		}
		// else if ("A".equals(rubrique.getOpfi()))
		// {
		// double max = 0;
		// for (int idx = 0; idx < nmois; idx++)
		// {
		// if (mont[idx] > max)
		// {
		// max = mont[idx];
		// }
		// }
		// this.fictivesalary.getValeurRubriquePartage().setBase(max);
		// }
		// else if ("P".equals(rubrique.getOpfi()))
		// {
		// int id = nmois;
		// while (id > 0)
		// {
		// id--;
		// if (mont[id] < 0)
		// break;
		// }
		// int nombreMoisCorrect = 0;
		// totalMontant = 0;
		// for (int idx = 0; idx < id; idx++)
		// {
		// if (mont[idx] > 0)
		// {
		// totalMontant += mont[idx];
		// nombreMoisCorrect++;
		// }
		// }
		// // if('O' == fictivesalary.param.getGenfile()) fictivesalary.outputtext+="\n--In Rubrique Clone : .............nombreMoisCorrect: " + nombreMoisCorrect);
		// if (nombreMoisCorrect != 0)
		// this.fictivesalary.getValeurRubriquePartage().setBase((totalMontant / nombreMoisCorrect) * 12);
		// else
		// this.fictivesalary.getValeurRubriquePartage().setBase(0);
		// }
		else if ("M".equals(rubrique.getOpfi())) //Maximum
		{
			double max = 0;
			for (int idx = 0; idx < nmois; idx++)
			{
				if (mont[idx] > max)
				{
					max = mont[idx];
				}
			}
			this.fictivesalary.getValeurRubriquePartage().setBase(max);
		}
		else if ("A".equals(rubrique.getOpfi())) // --Moyenne arithmetique
		{
			int nombreMoisCorrect = 0;
			totalMontant = 0;
			for (int idx = 0; idx < nmois; idx++)
			{
				totalMontant += mont[idx];
				nombreMoisCorrect++;
			}
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "\nTotal = "+totalMontant+" et Nombre de mois correct = " + nombreMoisCorrect ;
			if (nombreMoisCorrect != 0)
				this.fictivesalary.getValeurRubriquePartage().setBase(totalMontant / nombreMoisCorrect);
			else
				this.fictivesalary.getValeurRubriquePartage().setBase(0);
		}
		//
		// if('O' == fictivesalary.param.getGenfile()) fictivesalary.outputtext+="\n--In Rubrique Clone : base :" + this.fictivesalary.getValeurRubriquePartage().getBase());
		return this.fictivesalary.getValeurRubriquePartage().getBase();
	}

	/**
	 * Calcule la base en fonction des bases déjé calculées
	 * 
	 *            la période
	 * @return le montant de la base
	 */
	private double calculateFormule(String moi)
	{
		// if('O' == fictivesalary.param.getGenfile()) fictivesalary.outputtext += "\n Calcul du cumul pour le mois "+moi1;
		// if('O' == fictivesalary.param.getGenfile()) fictivesalary.outputtext+="\n--In Rubrique Clone : >>calculateFormule");

		// String queryString = "select comp_id.nume, sign, rubk from Rhprubriquebase " +
		// " where cdos = '" + fictivesalary.param.getDossier() + "'" +
		// " and crub = '" + rubrique.getComp_id().getCrub() + "'" +
		// " order by cdos, crub, comp_id.nume";
		//		
		// String queryStringRetro = "select comp_id.nume, sign, rubk from Rhthrbqba " +
		// " where cdos = '" + fictivesalary.param.getDossier() + "'" +
		// " and crub = '" + rubrique.getComp_id().getCrub() + "'" +
		// " and aamm = '" + fictivesalary.param.getMonthOfPay() + "'" +
		// " and nbul = '" + fictivesalary.param.getNumeroBulletin() + "'" +
		// " order by cdos, crub, comp_id.nume";
		// if('O' == fictivesalary.param.getGenfile()) fictivesalary.outputtext+="\n--In Rubrique Clone : queryString :" + queryString);
		// //
		// List<Object[]> listOfRubBase = (fictivesalary.param.isUseRetroactif())? fictivesalary.getService().find(queryStringRetro)
		// : fictivesalary.getService().find(queryString);

		List<Object[]> listOfRubBase = new ArrayList<Object[]>();
		if (!fictivesalary.param.getListOfRubqBase().isEmpty() && fictivesalary.param.getListOfRubqBase().containsKey(rubrique.getComp_id().getCrub()))
			listOfRubBase = fictivesalary.param.getListOfRubqBase().get(rubrique.getComp_id().getCrub());
		//
		String rubk = "";
		String sign = "";
		String formule = null;
		List<Expression> parametres = new ArrayList<Expression>();
		double valeur = 0;
		double valeur_sup = 0;
		double base = 0;
		String cdos = fictivesalary.param.dossier;
		String nmat = fictivesalary.getInfoSalary().getComp_id().getNmat();
		
		/************GESTION DE LA NOUVELLE FORMULE STOCKE DANS LA ZONE NOTE DE LA RUBRIQUE*************/
		String nouvelleFormule = rubrique.getFormule();
		if(fictivesalary.param.formulelitterale)
		{
			List<String> rubriques = ExpressionEvaluator.getRubriques(nouvelleFormule);
			for(String crub : rubriques)
			{
				rubk = crub;
				if (Character.isLetter(rubk.toCharArray()[0]))
					valeur = 0;
				else
				{
					if (moi.compareTo(fictivesalary.param.getFictiveMonthOfPay()) < 0)
					{
						if ("B".equals(rubrique.getLbtm()))
							valeur = fictivesalary.utilNomenclatureFictif.getSumFromTable(cdos, nmat, moi, rubk, null, "CumulPaie", ClsEnumeration.EnTypeOfColumn.BASE);
						else if ("T".equals(rubrique.getLbtm()))
							valeur = fictivesalary.utilNomenclatureFictif.getSumFromTable(cdos, nmat, moi, rubk, null, "CumulPaie", ClsEnumeration.EnTypeOfColumn.RATES);
						else if ("M".equals(rubrique.getLbtm()))
							valeur = fictivesalary.utilNomenclatureFictif.getSumFromTable(cdos, nmat, moi, rubk, null, "CumulPaie", ClsEnumeration.EnTypeOfColumn.AMOUNT);
						else valeur = 0;
					}
					else
					{
						if ("B".equals(rubrique.getLbtm()))
							valeur = fictivesalary.utilNomenclatureFictif.getSumFromTable(cdos, nmat, moi, rubk, null, "CongeFictif", ClsEnumeration.EnTypeOfColumn.BASE);
						else if ("T".equals(rubrique.getLbtm()))
							valeur = fictivesalary.utilNomenclatureFictif.getSumFromTable(cdos, nmat, moi, rubk, null, "CongeFictif", ClsEnumeration.EnTypeOfColumn.RATES);
						else if ("M".equals(rubrique.getLbtm()))
							valeur = fictivesalary.utilNomenclatureFictif.getSumFromTable(cdos, nmat, moi, rubk, null, "CongeFictif", ClsEnumeration.EnTypeOfColumn.AMOUNT);
						else valeur = 0;

						if (StringUtils.equals(moi, fictivesalary.getParam().getFictiveMonthOfPay()))
						{
							if ("B".equals(rubrique.getLbtm()))
								valeur_sup = fictivesalary.utilNomenclatureFictif.getSumFromTable(cdos, nmat, moi, rubk, null, "CumulPaie", ClsEnumeration.EnTypeOfColumn.BASE);
							else if ("T".equals(rubrique.getLbtm()))
								valeur_sup = fictivesalary.utilNomenclatureFictif.getSumFromTable(cdos, nmat, moi, rubk, null, "CumulPaie", ClsEnumeration.EnTypeOfColumn.RATES);
							else if ("M".equals(rubrique.getLbtm()))
								valeur_sup = fictivesalary.utilNomenclatureFictif.getSumFromTable(cdos, nmat, moi, rubk, null, "CumulPaie", ClsEnumeration.EnTypeOfColumn.AMOUNT);
							else valeur_sup = 0;
						}
						valeur += valeur_sup;
					}
				}
				parametres.add(new Expression(rubk,valeur));
			}
			Double initBase = ExpressionEvaluator.evaluate(rubrique.getComp_id().getCrub(),nouvelleFormule, parametres);
			
			if(initBase != null) return initBase.doubleValue();
			else parametres = new ArrayList<Expression>();
			return 0;	
		}
		/************FIN GESTION FORMULE COMPLEXE DEFINIE DANS LA ZONE NOTE************/
		for (Object[] object : listOfRubBase)
		{

			rubk = (String) object[2];
			sign = (String) object[1];
			// if('O' == fictivesalary.param.getGenfile()) fictivesalary.outputtext += "\t Base courante "+rubk;
			if (Character.isLetter(rubk.toCharArray()[0]))
				valeur = 0;
			else
			{
//				IF moi < memo_mdp
//		         THEN
//		            -- Les montants sont dans les cumuls
//		            IF t_rub.lbtm = 'B' THEN
//		               SELECT SUM(basp) INTO w_valeur FROM pacumu
//		                WHERE cdos = wpdos.cdos
//		                  AND nmat = wsal01.nmat
//		                  AND aamm = moi
//		                  AND rubq = wrbqbas.rubk
//		                  AND nbul != 0;
//		            ELSIF t_rub.lbtm = 'T' THEN
//		              SELECT SUM(taux) INTO w_valeur FROM pacumu
//		               WHERE cdos = wpdos.cdos
//		                 AND nmat = wsal01.nmat
//		                 AND aamm = moi
//		                 AND rubq = wrbqbas.rubk
//		                 AND nbul != 0;
//		            ELSIF t_rub.lbtm = 'M' THEN
//		               SELECT SUM(mont) INTO w_valeur FROM pacumu
//		                WHERE cdos = wpdos.cdos
//		                  AND nmat = wsal01.nmat
//		                  AND aamm = moi
//		                  AND rubq = wrbqbas.rubk
//		                  AND nbul != 0;
//		            ELSE
//		               w_valeur := 0;
//		            END IF;
//		         ELSE
				
				if(moi.compareTo(fictivesalary.param.getFictiveMonthOfPay()) < 0)
				{
					if ("B".equals(rubrique.getLbtm()))
					valeur = fictivesalary.utilNomenclatureFictif.getSumFromTable(cdos, nmat, moi, rubk, null, "CumulPaie", ClsEnumeration.EnTypeOfColumn.BASE);
					else if ("T".equals(rubrique.getLbtm()))
						valeur = fictivesalary.utilNomenclatureFictif.getSumFromTable(cdos, nmat, moi, rubk, null, "CumulPaie", ClsEnumeration.EnTypeOfColumn.RATES);
					else if ("M".equals(rubrique.getLbtm()))
						valeur = fictivesalary.utilNomenclatureFictif.getSumFromTable(cdos, nmat, moi, rubk, null, "CumulPaie", ClsEnumeration.EnTypeOfColumn.AMOUNT);
					else
						valeur = 0;
				}
				else
				{
//					 IF t_rub.lbtm = 'B' THEN
//		               SELECT SUM(basp) INTO w_valeur FROM pafic
//		                WHERE cdos = wpdos.cdos
//		                  AND nmat = wsal01.nmat
//		                  AND aamm = moi
//		                  AND rubq = wrbqbas.rubk
//		                  AND nbul != 0;
//		            ELSIF t_rub.lbtm = 'T' THEN
//		              SELECT SUM(taux) INTO w_valeur FROM pafic
//		               WHERE cdos = wpdos.cdos
//		                 AND nmat = wsal01.nmat
//		                 AND aamm = moi
//		                 AND rubq = wrbqbas.rubk
//		                 AND nbul != 0;
//		            ELSIF t_rub.lbtm = 'M' THEN
//		               SELECT SUM(mont) INTO w_valeur FROM pafic
//		                WHERE cdos = wpdos.cdos
//		                  AND nmat = wsal01.nmat
//		                  AND aamm = moi
//		                  AND rubq = wrbqbas.rubk
//		                  AND nbul != 0;
//		            ELSE
//		               w_valeur := 0;
//		            END IF;
					
						if ("B".equals(rubrique.getLbtm()))
							valeur = fictivesalary.utilNomenclatureFictif.getSumFromTable(cdos, nmat, moi, rubk, null, "CongeFictif", ClsEnumeration.EnTypeOfColumn.BASE);
						else if ("T".equals(rubrique.getLbtm()))
							valeur = fictivesalary.utilNomenclatureFictif.getSumFromTable(cdos, nmat, moi, rubk, null, "CongeFictif", ClsEnumeration.EnTypeOfColumn.RATES);
						else if ("M".equals(rubrique.getLbtm()))
							valeur = fictivesalary.utilNomenclatureFictif.getSumFromTable(cdos, nmat, moi, rubk, null, "CongeFictif", ClsEnumeration.EnTypeOfColumn.AMOUNT);
						else
							valeur = 0;
						
//						IF moi = memo_mdp
//			            THEN
//			               -- Les montants sont dans les cumuls
//			               IF t_rub.lbtm = 'B' THEN
//			                  SELECT SUM(basp) INTO w_valeur_sup FROM pacumu
//			                   WHERE cdos = wpdos.cdos
//			                     AND nmat = wsal01.nmat
//			                     AND aamm = moi
//			                     AND rubq = wrbqbas.rubk
//			                     AND nbul != 0;
//			               ELSIF t_rub.lbtm = 'T' THEN
//			                  SELECT SUM(taux) INTO w_valeur_sup FROM pacumu
//			                   WHERE cdos = wpdos.cdos
//			                     AND nmat = wsal01.nmat
//			                     AND aamm = moi
//			                     AND rubq = wrbqbas.rubk
//			                     AND nbul != 0;
//			               ELSIF t_rub.lbtm = 'M' THEN
//			                  SELECT SUM(mont) INTO w_valeur_sup FROM pacumu
//			                   WHERE cdos = wpdos.cdos
//			                     AND nmat = wsal01.nmat
//			                     AND aamm = moi
//			                     AND rubq = wrbqbas.rubk
//			                     AND nbul != 0;
//			               ELSE
//			                  w_valeur_sup := 0;
//			               END IF;
//			            END IF;
					
					if(StringUtils.equals(moi, fictivesalary.getParam().getFictiveMonthOfPay()))
					{
							if ("B".equals(rubrique.getLbtm()))
								valeur_sup = fictivesalary.utilNomenclatureFictif.getSumFromTable(cdos, nmat, moi, rubk, null, "CumulPaie", ClsEnumeration.EnTypeOfColumn.BASE);
							else if ("T".equals(rubrique.getLbtm()))
								valeur_sup = fictivesalary.utilNomenclatureFictif.getSumFromTable(cdos, nmat, moi, rubk, null, "CumulPaie", ClsEnumeration.EnTypeOfColumn.RATES);
							else if ("M".equals(rubrique.getLbtm()))
								valeur_sup = fictivesalary.utilNomenclatureFictif.getSumFromTable(cdos, nmat, moi, rubk, null, "CumulPaie", ClsEnumeration.EnTypeOfColumn.AMOUNT);
							else
								valeur_sup = 0;
					}
//					 w_valeur := NVL(w_valeur,0) + NVL(w_valeur_sup,0);
//
//			         END IF;
//
//			         IF PA_PAIE.NouZ(w_valeur) THEN
//			            w_valeur := 0;
//			         END IF;
			         
					valeur += valeur_sup;
				}

				// if('O' == fictivesalary.param.getGenfile()) fictivesalary.outputtext += "\t Valeur trouvée "+valeur;
				// }
			}
			if (StringUtils.isBlank(formule)) formule = "("+Expression.symboleParamOuvert + rubk+Expression.symboleParamFerme+")";
			else  formule = "("+formule+ sign + Expression.symboleParamOuvert + rubk +Expression.symboleParamFerme + ")";
						
			parametres.add(new Expression(rubk,valeur));
						
						if ("+".equals(sign))
							base += valeur;
						if ("-".equals(sign))
							base -= valeur;
						if ("*".equals(sign))
							base *= valeur;
						if ("/".equals(sign))
							if (valeur != 0)
								base /= valeur;

			}

			//base = ExpressionEvaluator.evaluate(rubrique.getComp_id().getCrub(),formule, parametres);
					

		return base;
	}

	/**
	 * => base_mdp Calcule de la base en fonction des rubriques de base de la rubrique courante
	 * 
	 * @return montant de la base calculée
	 */
	private double calculateBaseMonthOfPay()
	{
		// if('O' == fictivesalary.param.getGenfile()) fictivesalary.outputtext+="\n--In Rubrique Clone : >>calculateBaseMonthOfPay");

		// String queryString = "select comp_id.nume, sign, rubk from Rhprubriquebase " +
		// " where cdos = '" + fictivesalary.param.getDossier() + "'" +
		// " and crub = '" + rubrique.getComp_id().getCrub() + "'" +
		// " order by cdos, crub, nume";
		//
		// String queryStringRetro = "select comp_id.nume, sign, rubk from Rhthrbqba " +
		// " where cdos = '" + fictivesalary.param.getDossier() + "'" +
		// " and crub = '" + rubrique.getComp_id().getCrub() + "'" +
		// " and aamm = '" + fictivesalary.param.getMonthOfPay() + "'" +
		// " and nbul = '" + fictivesalary.param.getNumeroBulletin() + "'" +
		// " order by cdos, crub, nume";
		//		
		// List<Object[]> listOfRubBase = (fictivesalary.param.isUseRetroactif())? fictivesalary.getService().find(queryStringRetro)
		// : fictivesalary.getService().find(queryString);
		List<Object[]> listOfRubBase = new ArrayList<Object[]>();
		if (!fictivesalary.param.getListOfRubqBase().isEmpty() && fictivesalary.param.getListOfRubqBase().containsKey(rubrique.getComp_id().getCrub()))
			listOfRubBase = fictivesalary.param.getListOfRubqBase().get(rubrique.getComp_id().getCrub());
		//
		String rubk = "";
		String sign = "";
		String formule = null;
		List<Expression> parametres = new ArrayList<Expression>();
		double valeur = 0;
		double base = 0;
		ClsFictifRubriqueClone rubriqueClone = null;
		/************GESTION DE LA NOUVELLE FORMULE STOCKE DANS LA ZONE NOTE DE LA RUBRIQUE*************/
		String nouvelleFormule = rubrique.getFormule();
		if(fictivesalary.param.formulelitterale)
		{
			List<String> rubriques = ExpressionEvaluator.getRubriques(nouvelleFormule);
			for(String crub : rubriques)
			{
				rubk = crub;
				if (Character.isLetter(rubk.toCharArray()[0]))
					valeur = calculatePlafond(rubk);
				else
				{
					rubriqueClone = fictivesalary.findRubriqueCloneFictif(rubk);
					if (rubriqueClone != null)
					{
						if ("B".equals(rubrique.getLbtm()))
							valeur = rubriqueClone.getBase();
						else if ("T".equals(rubrique.getLbtm()))
							valeur = rubriqueClone.getRates();
						else
							valeur = rubriqueClone.getAmount();
					}
					else
						valeur = 0;
					fictivesalary.getValeurRubriquePartage().setValeur(valeur);
				}
				parametres.add(new Expression(rubk,valeur));
			}
			Double initBase = ExpressionEvaluator.evaluate(rubrique.getComp_id().getCrub(),nouvelleFormule, parametres);
			
			if(initBase != null) return initBase.doubleValue();
			else parametres = new ArrayList<Expression>();
			return 0;	
		}
		/************FIN GESTION FORMULE COMPLEXE DEFINIE DANS LA ZONE NOTE************/
		for (Object[] object : listOfRubBase)
		{
			rubk = (String) object[2];
			sign = (String) object[1];
			if (Character.isLetter(rubk.toCharArray()[0]))
				valeur = calculatePlafond(rubk);
			else
			{
				// récuperer la base, le taux, le montant de la base rubk et
				// affecter é valeur selon le cas
				rubriqueClone = fictivesalary.findRubriqueCloneFictif(rubk);
				if (rubriqueClone != null)
				{
					 if('O' == fictivesalary.param.getGenfile()) fictivesalary.outputtext+="\n--In Rubrique Clone : .............. ..Local String rubrique " +rubriqueClone.localToString();
					// if('O' == fictivesalary.param.getGenfile()) fictivesalary.outputtext+="\n--In Rubrique Clone : \n.............. rubk :" + rubk);
					// if('O' == fictivesalary.param.getGenfile()) fictivesalary.outputtext+="\n--In Rubrique Clone : .............. lbtm :" + rubrique.getLbtm());
					// if('O' == fictivesalary.param.getGenfile()) fictivesalary.outputtext+="\n--In Rubrique Clone : .............. base partagée :" +
					// fictivesalary.getValeurRubriquePartage().getBase());
					// if('O' == fictivesalary.param.getGenfile()) fictivesalary.outputtext+="\n--In Rubrique Clone : .............. base de la rubrique :" +
					// rubriqueClone.getBase());
					// if('O' == fictivesalary.param.getGenfile()) fictivesalary.outputtext+="\n--In Rubrique Clone : .............. montant de la rubrique :" +
					// rubriqueClone.getAmount());
					if ("B".equals(rubrique.getLbtm()))
						valeur = rubriqueClone.getBase();
					else if ("T".equals(rubrique.getLbtm()))
						valeur = rubriqueClone.getRates();
					else
						valeur = rubriqueClone.getAmount();
					fictivesalary.getValeurRubriquePartage().setValeur(valeur);
					// if('O' == fictivesalary.param.getGenfile()) fictivesalary.outputtext+="\n--In Rubrique Clone : .............. ...valeur de la rubrique :" +
					// valeur);
					// if('O' == fictivesalary.param.getGenfile()) fictivesalary.outputtext+="\n--In Rubrique Clone : .............. ...sign de la rubrique :" + sign);
					//
				}
				else
					valeur = 0;
			}
			if (StringUtils.isBlank(formule)) formule = "("+Expression.symboleParamOuvert + rubk+Expression.symboleParamFerme+")";
			else  formule = "("+formule+ sign + Expression.symboleParamOuvert + rubk+Expression.symboleParamFerme + ")";
						
			parametres.add(new Expression(rubk,valeur));
						
						if ("+".equals(sign))
							base += valeur;
						if ("-".equals(sign))
							base -= valeur;
						if ("*".equals(sign))
							base *= valeur;
						if ("/".equals(sign))
							if (valeur != 0)
								base /= valeur;

			}

			//base = ExpressionEvaluator.evaluate(rubrique.getComp_id().getCrub(),formule, parametres);
					
		//
		// if('O' == fictivesalary.param.getGenfile()) fictivesalary.outputtext+="\n--In Rubrique Clone : base :" + base);
		// if('O' == fictivesalary.param.getGenfile()) fictivesalary.outputtext+="\n--In Rubrique Clone : valeur :" + valeur);
		return base;
	}

	/**
	 * => PA_ALGO.cal_plaf
	 * 
	 * @param char16
	 * @return la valeur obtenue
	 */
	public double calculatePlafond(String char16)
	{
		// if('O' == fictivesalary.param.getGenfile()) fictivesalary.outputtext+="\n--In Rubrique Clone : >>calculatePlafond");

		int coeff = -1;
		double plafond = 0;
		Number plafondTaux = null;
		Number plafondMontant = null;
		String char2 = "";

		if (StringUtils.isEmpty(char16))
			return plafond;
		char2 = StringUtils.substring(char16, 0, 2);

		// lire le montant et le taux dans la table 96
		// -- ligne = 1
		// -- clé d'accés = les deux premiers char de RUBK
		// w_plafm := paf_lecfnomM(96,char2,1,PA_CALCUL.w_aamm,PA_CALCUL.w_nlot,PA_CALCUL.wsd_fcal1.nbul);
		// w_plaft := paf_lecfnomT(96,char2,1,PA_CALCUL.w_aamm,PA_CALCUL.w_nlot,PA_CALCUL.wsd_fcal1.nbul);
		plafondMontant = fictivesalary.utilNomenclatureFictif.getAmountOrRateFromNomenclature(fictivesalary.getInfoSalary().getComp_id().getCdos(), 96, char2, 1, fictivesalary.param.getNlot(), fictivesalary.param
				.getNumeroBulletin(), fictivesalary.param.getMonthOfPay(), ClsEnumeration.EnColumnToRead.AMOUNT);
		plafondTaux = fictivesalary.utilNomenclatureFictif.getAmountOrRateFromNomenclature(fictivesalary.getInfoSalary().getComp_id().getCdos(), 96, char2, 1, fictivesalary.param.getNlot(), fictivesalary.param
				.getNumeroBulletin(), fictivesalary.param.getMonthOfPay(), ClsEnumeration.EnColumnToRead.RATES);
		// IF w_plafm IS NOT NULL THEN
//		if (StringUtils.equals(fictivesalary.getParam().nomClient, ClsEntreprise.SHELL_GABON))
//			if(plafondMontant == null)
//				plafondMontant = 0;
		
		if (plafondMontant != null)
		{
			// coeff := SUBSTR(char16,3,2);

			coeff = NumberUtils.toInt(StringUtils.substring(char16, 2, 4));

			// IF PA_PAIE.NouZ(w_plafm)
			// THEN
			if (plafondMontant.intValue() == 0)
				// w_plaf := w_plaft * coeff;
				plafond = (plafondTaux == null ? 0 : plafondTaux.doubleValue()) * coeff;
			// ELSE
			else
				// w_plaf := w_plafm * coeff;
				plafond = plafondMontant.doubleValue() * coeff;
			// END IF;
		}
		// ELSE
		else
		{
			// PA_CALCUL.err_msg := PA_PAIE.erreurp('ERR-90081',PA_CALCUL.w_clang,PA_CALCUL.t_rub.algo,PA_CALCUL.t_rub.crub,char2,PA_CALCUL.wsal01.nmat);
			// Pb_Calcul := TRUE;
			// RETURN 0;
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90081", fictivesalary.param.getLangue(), rubrique.getAlgo(), rubrique.getComp_id().getCrub(), char2, fictivesalary.getInfoSalary().getComp_id()
					.getNmat()));
			fictivesalary.param.setPbWithCalulation(true);
			return 0;
		}
		return plafond;
	}

	/**
	 * => charg_prets permet de constituer la liste des numéros de prets d'une rubrique
	 * 
	 */
	public void loadListOfLoanNumber()
	{
		if (fictivesalary.param.getBasePretsNb_ret() > 0)
		{
			int num_cal = fictivesalary.param.getMyMonthOfPay().getMonth() - fictivesalary.param.getFictiveMyMonthOfPay().getMonth() + 1;
			if (num_cal < 0)
				num_cal += 12;

			if (num_cal > fictivesalary.param.getBasePretsNb_ret())
				return;
		}

		listOfLoanNumber = new ArrayList<Object>();
		if (rubrique.getAlgo() == 13)
		{
			if (!fictivesalary.getListLignePretMap().isEmpty() && fictivesalary.getListLignePretMap().containsKey(rubrique.getComp_id().getCrub()))
			{
				List lst = fictivesalary.getListLignePretMap().get(rubrique.getComp_id().getCrub());
				for(Object o : lst)
					listOfLoanNumber.add(o);
//				listOfLoanNumber = fictivesalary.getListLignePretMap().get(rubrique.getComp_id().getCrub());
			}
		}
		else
		{
			if (!fictivesalary.getListNumeroPretMap().isEmpty() && fictivesalary.getListNumeroPretMap().containsKey(rubrique.getComp_id().getCrub()))
			{
				List lst = fictivesalary.getListNumeroPretMap().get(rubrique.getComp_id().getCrub());
				for(Object o : lst)
					listOfLoanNumber.add(o);
				//listOfLoanNumber = fictivesalary.getListNumeroPretMap().get(rubrique.getComp_id().getCrub());		
			}
		}
	}

	/**
	 * retire certains EV qui sont des préts de la liste des préts
	 * 
	 */
	public void removeElementVariableFromLoans()
	{
		// if('O' == fictivesalary.param.getGenfile()) fictivesalary.outputtext+="\n--In Rubrique Clone : >>removeElementVariableFromLoans");
		// i NUMBER;
		// j NUMBER;
		// k NUMBER;
		// nb_ev NUMBER;
		//
		// BEGIN
		// i := 1;
		// WHILE i <= nb_prets LOOP
		// -- Recherche du nombre d'EV pour ce pret
		// nb_ev := 0;
		String queryStringRetro = "";
		String queryString = "";
		int count = 0;
		// List<Object> listOfLoanNumberClone = new ArrayList<Object>();
		// for (Object o : listOfLoanNumber) {
		// listOfLoanNumberClone.add(o);
		// }
		List<Object> listOfLoanNumberClone = listOfLoanNumber;
		for (Object obj : listOfLoanNumberClone)
		{
			// IF retroactif THEN
			if (fictivesalary.param.isUseRetroactif())
			{
				// SELECT COUNT(*) INTO nb_ev FROM pahevar
				// WHERE cdos = wpdos.cdos
				// AND nmat = wsal01.nmat
				// AND aamm = w_aamm
				// AND nbul = wsd_fcal1.nbul
				// AND rubq = t_rub.crub
				// AND nprt = LTRIM(TO_CHAR(tab_prets(i),'0999'));
				queryStringRetro = " select count(*) from Rhthevar" + " where cdos = '" + fictivesalary.param.getDossier() + "'" + " and nmat = '" + fictivesalary.getInfoSalary().getComp_id().getNmat() + "'"
						+ " and aamm = '" + fictivesalary.param.getMonthOfPay() + "'" + " and nbul = " + fictivesalary.param.getNumeroBulletin() + " and rubq = '" + rubrique.getComp_id().getCrub() + "'"
						+ " and nprt = '" + ClsStringUtil.formatNumber(ClsObjectUtil.getIntegerFromObject(obj), fictivesalary.param.FORMAT_PERIOD_OF_RBQ) + "'";
				Object o = (Object) fictivesalary.getService().find(queryStringRetro).get(0);
				if (o != null)
					count = ClsObjectUtil.getIntegerFromObject(o);
			}
			// ELSE
			else
			{
				// SELECT COUNT(*) INTO nb_ev FROM paevar
				// WHERE cdos = wpdos.cdos
				// AND nmat = wsal01.nmat
				// AND aamm = w_aamm
				// AND nbul = wsd_fcal1.nbul
				// AND rubq = t_rub.crub
				// AND nprt = LTRIM(TO_CHAR(tab_prets(i),'0999'));
				queryString = " select count(*) from Rhteltvardet" + " where cdos = '" + fictivesalary.param.getDossier() + "'" + " and nmat = '" + fictivesalary.getInfoSalary().getComp_id().getNmat() + "'"
						+ " and aamm = '" + fictivesalary.param.getMonthOfPay() + "'" + " and nbul = " + fictivesalary.param.getNumeroBulletin() + " and rubq = '" + rubrique.getComp_id().getCrub() + "'"
						+ " and nprt = '" + ClsStringUtil.formatNumber(ClsObjectUtil.getIntegerFromObject(obj), fictivesalary.param.FORMAT_PERIOD_OF_RBQ) + "'";
				Object o = (Object) fictivesalary.getService().find(queryString).get(0);
				if (o != null)
					count = ClsObjectUtil.getIntegerFromObject(o);
			}
			if (count > 0)
				listOfLoanNumberClone.remove(obj);
			// END IF;
			//
			// IF NOT PA_PAIE.NouZ(nb_ev) THEN
			// FOR j IN i..nb_prets-1 LOOP
			// k := j + 1;
			// tab_prets(j) := tab_prets(k);
			// END LOOP;
			// tab_prets(nb_prets) := NULL;
			// nb_prets := nb_prets - 1;
			// ELSE
			// i := i + 1;
			// END IF;
			// END LOOP;
		}

		// @Yannick
		// en fin de suppression, on reinitialise la liste des prets par la courante
		listOfLoanNumber = listOfLoanNumberClone;
	}

	/**
	 * Initialisation de la liste des cotisations francaises
	 */
	public void initCotisationFrancaise()
	{
		if (listOfRegularisationFr == null)
			listOfRegularisationFr = new ArrayList<ClsRegularisationFrParam>();
		for (int i = 0; i < 12; i++)
		{
			listOfRegularisationFr.add(new ClsRegularisationFrParam());
		}
	}

	/**
	 * => bas_cumul Cumuls des bases, plafonds, taux et cotisations reguls pour cotisations aux caisses francaises => PA_ALGO.bas_cumul()
	 * 
	 * @return montant des cumuls des bases
	 */
	public boolean calculateCumulOfBase()
	{
		// if('O' == fictivesalary.param.getGenfile()) fictivesalary.outputtext+="\n--In Rubrique Clone : >>calculateCumulOfBase");

		ClsFictifRubriqueClone autreRub = null;
		int idx = 0;
		// l_am NUMBER(6,0);
		int idxDebutPeriodInt = 0;
		// l_cam VARCHAR2(6);
		String idxDebutPeriodIntS = "";
		// l_mois NUMBER(5);
		int l_mois = 0;
		// l_bas NUMBER(15,3);
		double l_bas = 0;
		// l_tau NUMBER(10,3);
		double l_tau = 0;
		// l_mon NUMBER(15,3);
		double l_mon = 0;
		//
		// l_nbul NUMBER(1);
		int nbul = 0;
		double d = 0;
		ClsRegularisationFrParam regularisation = null;
		//
		// BEGIN
		// d_per := TO_CHAR(PA_CALCUL.wpdos.ddex,'YYYYMM');
		int debutPeriodInt = new ClsDate(fictivesalary.param.getDossierDateDebutExercice()).getYearAndMonthInt();
		int finPeriodInt = new Integer(fictivesalary.param.getMonthOfPay());
		//
		// IF f_per < d_per THEN
		// f_per := d_per;
		if (finPeriodInt < debutPeriodInt)
			finPeriodInt = debutPeriodInt;
		// END IF;
		//
		// W_Retour := init_cotf;

		initCotisationFrancaise();
		// w_tau := 0;//il est global
		this.fictivesalary.getValeurRubriquePartage().setRates(0);
		//
		// IF PA_CALCUL.t_rub.rreg = 'N' OR PA_CALCUL.t_rub.rman = 'O' THEN
		if ("N".equals(this.rubrique.getRreg()) || "O".equals(this.rubrique.getRman()))
		{
			// l_indcf := 1;
			idx = 0;
			// tab_cotf_base(l_indcf) := w_bas;

			regularisation = listOfRegularisationFr.get(idx);
			regularisation.setBase(this.fictivesalary.getValeurRubriquePartage().getBase());
		}
		// ELSE
		else
		{
			// l_am := d_per;
			idxDebutPeriodInt = debutPeriodInt;
			// l_cam := LTRIM(TO_CHAR(l_am,'099999'));
			idxDebutPeriodIntS = ClsStringUtil.formatNumber(idxDebutPeriodInt, "000000");
			//Rhtprcumu oRhtprcumu = null;
			CumulPaie oCumulPaie = null;
			boolean objectIsNotNull = false;
			// WHILE l_am <= f_per LOOP
			while (idxDebutPeriodInt <= finPeriodInt)
			{
				// FOR l_nbul IN 1..9 LOOP
				for (nbul = 1; nbul <= 9; nbul++)
				{
					// IF PA_CALCUL.retroactif THEN
					if (fictivesalary.param.isUseRetroactif())
					{
						// BEGIN
						// SELECT basc, taux, mont INTO l_bas, l_tau, l_mon FROM prcumu
						// WHERE cdos = PA_CALCUL.wpdos.cdos
						// AND nmat = PA_CALCUL.wsal01.nmat
						// AND aamm = l_cam
						// AND rubq = PA_CALCUL.t_rub.rcon
						// AND nbul = l_nbul;
						// EXCEPTION
						// WHEN NO_DATA_FOUND THEN
						// null;
						// END;
//						oRhtprcumu = (Rhtprcumu) fictivesalary.getService().get(Rhtprcumu.class,
//								new RhtprcumuPK(fictivesalary.param.getDossier(), fictivesalary.getInfoSalary().getComp_id().getNmat(), idxDebutPeriodIntS, this.rubrique.getRcon(), nbul));
//						l_bas = 0;
//						l_tau = 0;
//						l_mon = 0;
//						if (oRhtprcumu != null)
//						{
//							objectIsNotNull = true;
//							if (oCumulPaie.getBasc() != null)
//								l_bas = oCumulPaie.getBasc().doubleValue();
//							if (oCumulPaie.getTaux() != null)
//								l_tau = oCumulPaie.getTaux().doubleValue();
//							if (oCumulPaie.getMont() != null)
//								l_mon = oCumulPaie.getMont().doubleValue();
//						}
					}
					// ELSE
					else
					{
						// BEGIN
						// SELECT basc, taux, mont INTO l_bas, l_tau, l_mon FROM pacumu
						// WHERE cdos = PA_CALCUL.wpdos.cdos
						// AND nmat = PA_CALCUL.wsal01.nmat
						// AND aamm = l_cam
						// AND rubq = PA_CALCUL.t_rub.rcon
						// AND nbul = l_nbul;
						// EXCEPTION
						// WHEN NO_DATA_FOUND THEN
						// null;
						// END;
						// END IF;
						oCumulPaie = (CumulPaie) fictivesalary.getService().find("FROM CumulPaie WHERE identreprise="+fictivesalary.param.getDossier()
													+" AND nmat='"+fictivesalary.getInfoSalary().getComp_id().getNmat()+"'"
													+" AND aamm='"+idxDebutPeriodIntS+"' AND rubq='"+this.rubrique.getRcon()+"'"
													+" AND nbul="+nbul);

						l_bas = 0;
						l_tau = 0;
						l_mon = 0;
						if (oCumulPaie != null)
						{
							objectIsNotNull = true;
							if (oCumulPaie.getBasc() != null)
								l_bas = oCumulPaie.getBasc().doubleValue();
							if (oCumulPaie.getTaux() != null)
								l_tau = oCumulPaie.getTaux().doubleValue();
							if (oCumulPaie.getMont() != null)
								l_mon = oCumulPaie.getMont().doubleValue();
						}
					}
					// IF NOT SQL%NOTFOUND AND NOT PA_PAIE.NouZ(l_bas)
					// AND NOT PA_PAIE.NouZ(l_tau) THEN
					if (objectIsNotNull == true && l_bas > 0 && l_tau > 0)
					{
						// IF l_tau != w_tau THEN
						// l_indcf := l_indcf + 1;
						// w_tau := l_tau;
						// tab_cotf_tau(l_indcf) := l_tau;
						// END IF;
						if (l_tau != this.getRates())
						{
							idx++;
							this.setRates(l_tau);
							regularisation = listOfRegularisationFr.get(idx);
							regularisation.setTaux(l_tau);
						}
						regularisation = listOfRegularisationFr.get(idx);
						// w_bas := w_bas + l_bas;
						this.fictivesalary.getValeurRubriquePartage().setBase(this.fictivesalary.getValeurRubriquePartage().getBase() + l_bas);
						// tab_cotf_base(l_indcf) := tab_cotf_base(l_indcf) + l_bas;
						regularisation.setBase(regularisation.getBase() + l_bas);
						// tab_cotf_cot (l_indcf) := tab_cotf_cot (l_indcf) + l_mon;
						regularisation.setCotisation(regularisation.getCotisation() + l_mon);
						// l_mon := paf_lecCum(PA_CALCUL.wsal01.nmat,l_cam,PA_CALCUL.t_rub.crub,l_nbul,'M',PA_CALCUL.w_aamm);
						l_mon = fictivesalary.utilNomenclatureFictif.getCumul(fictivesalary, idxDebutPeriodIntS, rubrique.getComp_id().getCrub(), nbul, ClsEnumeration.EnTypeOfColumn.AMOUNT, fictivesalary.param
								.getMonthOfPay());
						// tab_cotf_reg(l_indcf) := tab_cotf_reg(l_indcf) + NVL(l_mon,0);
						regularisation.setRegularisation(regularisation.getRegularisation() + l_mon);
						//
						// IF l_cam != w_aamm THEN
						if (idxDebutPeriodIntS != null && (!idxDebutPeriodIntS.equals(fictivesalary.param.getMonthOfPay())))
						{
							//
							// l_mon := paf_lecCum(PA_CALCUL.wsal01.nmat,l_cam,PA_CALCUL.rub_plaf,l_nbul,'M',PA_CALCUL.w_aamm);
							l_mon = fictivesalary.utilNomenclatureFictif.getCumul(fictivesalary, idxDebutPeriodIntS, rubrique.getComp_id().getCrub(), nbul, ClsEnumeration.EnTypeOfColumn.AMOUNT, fictivesalary.param
									.getMonthOfPay());
							//
							// tab_cotf_plaf(l_indcf) := tab_cotf_plaf(l_indcf) + NVL(l_mon,0);
							regularisation.setPlafond(regularisation.getPlafond() + l_mon);
						}
						//
						// END IF;
						//
					}
					// END IF;
				}
				// END LOOP; -- FIN 'FOR l_nbul IN 1..9'
				//
				// l_am := add_per(l_am,0,1);
				idxDebutPeriodInt = ClsGeneralUtil.addPer(idxDebutPeriodInt, 0, 1);

				// l_cam := LTRIM(TO_CHAR(l_am,'099999'));
				idxDebutPeriodIntS = ClsStringUtil.formatNumber(idxDebutPeriodInt, "000000");
				//
				// END LOOP; -- WHILE
			}
			//
			// IF PA_PAIE.NouZ(l_indcf) THEN
			if (idx == 0)
				// RETURN TRUE;
				return true;
			// END IF;
			//

			// W_Retour := char_mont(PA_CALCUL.t_rub.rcon);
			autreRub = fictivesalary.findRubriqueCloneFictif(rubrique.getRcon());
			//
			// IF PA_CALCUL.c_taux != w_tau THEN
			// l_indcf := l_indcf + 1;
			// END IF;
			if (autreRub.getRates() != this.fictivesalary.getValeurRubriquePartage().getRates())
				// if(rates != this.fictivesalary.getValeurRubriquePartage().getRates())
				idx++;
			//

			// w_bas := w_bas + PA_CALCUL.cc_mont;
			this.fictivesalary.getValeurRubriquePartage().setBase(this.fictivesalary.getValeurRubriquePartage().getBase() + autreRub.getBase());
			// tab_cotf_base(l_indcf) := tab_cotf_base(l_indcf) + PA_CALCUL.cc_mont;
			d = listOfRegularisationFr.get(idx).getBase();
			listOfRegularisationFr.get(idx).setBase(d + autreRub.getBase());
			// tab_cotf_cot(l_indcf) := tab_cotf_cot(l_indcf) + PA_CALCUL.c_mont;
			d = listOfRegularisationFr.get(idx).getCotisation();
			listOfRegularisationFr.get(idx).setCotisation(d + autreRub.getAmount());
			// tab_cotf_tau(l_indcf) := PA_CALCUL.c_taux;
			listOfRegularisationFr.get(idx).setTaux(autreRub.getRates());
		}
		// END IF;
		//
		// W_Retour := char_mont(PA_CALCUL.rub_plaf);
		autreRub = fictivesalary.findRubriqueCloneFictif(fictivesalary.param.getPlafondRubrique());
		//
		// tab_cotf_plaf(l_indcf) := tab_cotf_plaf(l_indcf) + PA_CALCUL.c_mont;
		d = listOfRegularisationFr.get(idx).getPlafond();
		listOfRegularisationFr.get(idx).setPlafond(d + autreRub.getAmount());
		//
		// IF w_aamm = f_per THEN
		if (fictivesalary.param.getMonthOfPay().equals(ClsStringUtil.formatNumber(finPeriodInt, "0000")))
			// RETURN TRUE;
			return true;
		// END IF;
		//
		// l_am := add_per(w_aamm,0,1);
		idxDebutPeriodInt = ClsGeneralUtil.addPer(idxDebutPeriodInt, 0, 1);
		//
		// WHILE l_am <= f_per LOOP
		while (idxDebutPeriodInt <= finPeriodInt)
		{
			// l_mois := TO_NUMBER(SUBSTR(LTRIM(TO_CHAR(l_am,'099999')),5,2));
			idxDebutPeriodIntS = ClsStringUtil.formatNumber(idxDebutPeriodInt, "000000");
			l_mois = NumberUtils.toInt(StringUtils.substring(idxDebutPeriodIntS, 4, 6));
			// tab_cotf_plaf(l_indcf) := tab_cotf_plaf(l_indcf) + PA_CALCUL.plafond(l_mois);
			d = listOfRegularisationFr.get(idx).getPlafond();
			listOfRegularisationFr.get(idx).setPlafond(d + fictivesalary.param.getPlafondOfMonthList().get(l_mois));
			// l_am := add_per(l_am,0,1);
			idxDebutPeriodInt = ClsGeneralUtil.addPer(idxDebutPeriodInt, 0, 1);
		}
		return true;
	}

	public boolean calculateCumulOfBaseOld()
	{
		// if('O' == fictivesalary.param.getGenfile()) fictivesalary.outputtext+="\n--In Rubrique Clone : >>calculateCumulOfBase");

		ClsFictifRubriqueClone autreRub = null;
		int idx = 0;
		// l_am NUMBER(6,0);
		int idxDebutPeriodInt = 0;
		// l_cam VARCHAR2(6);
		String idxDebutPeriodIntS = "";
		// l_mois NUMBER(5);
		int l_mois = 0;
		// l_bas NUMBER(15,3);
		double l_bas = 0;
		// l_tau NUMBER(10,3);
		double l_tau = 0;
		// l_mon NUMBER(15,3);
		double l_mon = 0;
		//
		// l_nbul NUMBER(1);
		int nbul = 0;
		double d = 0;
		ClsRegularisationFrParam regularisation = null;
		//
		// BEGIN
		// d_per := TO_CHAR(PA_CALCUL.wpdos.ddex,'YYYYMM');
		int debutPeriodInt = new ClsDate(fictivesalary.param.getDossierDateDebutExercice()).getYearAndMonthInt();
		int finPeriodInt = new Integer(fictivesalary.param.getMonthOfPay());
		//
		// IF f_per < d_per THEN
		// f_per := d_per;
		if (finPeriodInt < debutPeriodInt)
			finPeriodInt = debutPeriodInt;
		// END IF;
		//
		// w_tau := 0;//il est global
		this.fictivesalary.getValeurRubriquePartage().setRates(0);
		//
		// IF PA_CALCUL.t_rub.rreg = 'N' OR PA_CALCUL.t_rub.rman = 'O' THEN
		if ("N".equals(this.rubrique.getRreg()) || "O".equals(this.rubrique.getRman()))
		{
			// l_indcf := 1;
			idx = 0;
			// tab_cotf_base(l_indcf) := w_bas;
			regularisation = listOfRegularisationFr.get(idx);
			regularisation.setBase(this.fictivesalary.getValeurRubriquePartage().getBase());
		}
		// ELSE
		else
		{
			// l_am := d_per;
			idxDebutPeriodInt = debutPeriodInt;
			// l_cam := LTRIM(TO_CHAR(l_am,'099999'));
			idxDebutPeriodIntS = ClsStringUtil.formatNumber(idxDebutPeriodInt, "000000");
			//Rhtprcumu oRhtprcumu = null;
			CumulPaie oCumulPaie = null;
			boolean objectIsNotNull = false;
			// WHILE l_am <= f_per LOOP
			while (idxDebutPeriodInt <= finPeriodInt)
			{
				// FOR l_nbul IN 1..9 LOOP
				for (nbul = 0; nbul < 9; nbul++)
				{
					// IF PA_CALCUL.retroactif THEN
					if (fictivesalary.param.isUseRetroactif())
					{
						// BEGIN
						// SELECT basc, taux, mont INTO l_bas, l_tau, l_mon FROM prcumu
						// WHERE cdos = PA_CALCUL.wpdos.cdos
						// AND nmat = PA_CALCUL.wsal01.nmat
						// AND aamm = l_cam
						// AND rubq = PA_CALCUL.t_rub.rcon
						// AND nbul = l_nbul;
						// EXCEPTION
						// WHEN NO_DATA_FOUND THEN
						// null;
						// END;
//						oRhtprcumu = (Rhtprcumu) fictivesalary.getService().get(Rhtprcumu.class,
//								new RhtprcumuPK(fictivesalary.param.getDossier(), fictivesalary.getInfoSalary().getComp_id().getNmat(), idxDebutPeriodIntS, this.rubrique.getRcon(), nbul));
//						l_bas = 0;
//						l_tau = 0;
//						l_mon = 0;
//						if (oRhtprcumu != null)
//						{
//							objectIsNotNull = true;
//							if (oCumulPaie.getBasc() != null)
//								l_bas = oCumulPaie.getBasc().doubleValue();
//							if (oCumulPaie.getTaux() != null)
//								l_tau = oCumulPaie.getTaux().doubleValue();
//							if (oCumulPaie.getMont() != null)
//								l_mon = oCumulPaie.getMont().doubleValue();
//						}
					}
					// ELSE
					else
					{
						// BEGIN
						// SELECT basc, taux, mont INTO l_bas, l_tau, l_mon FROM pacumu
						// WHERE cdos = PA_CALCUL.wpdos.cdos
						// AND nmat = PA_CALCUL.wsal01.nmat
						// AND aamm = l_cam
						// AND rubq = PA_CALCUL.t_rub.rcon
						// AND nbul = l_nbul;
						// EXCEPTION
						// WHEN NO_DATA_FOUND THEN
						// null;
						// END;
						// END IF;
						oCumulPaie = (CumulPaie) fictivesalary.getService().find("FROM CumulPaie WHERE identreprise="+fictivesalary.param.getDossier()
								+" AND nmat='"+fictivesalary.getInfoSalary().getComp_id().getNmat()+"'"
								+" AND aamm='"+idxDebutPeriodIntS+"' AND rubq='"+this.rubrique.getRcon()+"'"
								+" AND nbul="+nbul);
						l_bas = 0;
						l_tau = 0;
						l_mon = 0;
						if (oCumulPaie != null)
						{
							objectIsNotNull = true;
							if (oCumulPaie.getBasc() != null)
								l_bas = oCumulPaie.getBasc().doubleValue();
							if (oCumulPaie.getTaux() != null)
								l_tau = oCumulPaie.getTaux().doubleValue();
							if (oCumulPaie.getMont() != null)
								l_mon = oCumulPaie.getMont().doubleValue();
						}
					}
					// IF NOT SQL%NOTFOUND AND NOT PA_PAIE.NouZ(l_bas)
					// AND NOT PA_PAIE.NouZ(l_tau) THEN
					if (objectIsNotNull == true && l_bas > 0 && l_tau > 0)
					{
						// IF l_tau != w_tau THEN
						// l_indcf := l_indcf + 1;
						// w_tau := l_tau;
						// tab_cotf_tau(l_indcf) := l_tau;
						// END IF;
						if (l_tau != this.getRates())
						{
							idx++;
							this.setRates(l_tau);
							regularisation = listOfRegularisationFr.get(idx);
							regularisation.setTaux(l_tau);
						}
						regularisation = listOfRegularisationFr.get(idx);
						// w_bas := w_bas + l_bas;
						this.fictivesalary.getValeurRubriquePartage().setBase(this.fictivesalary.getValeurRubriquePartage().getBase() + l_bas);
						// tab_cotf_base(l_indcf) := tab_cotf_base(l_indcf) + l_bas;
						regularisation.setBase(regularisation.getBase() + l_bas);
						// tab_cotf_cot (l_indcf) := tab_cotf_cot (l_indcf) + l_mon;
						regularisation.setCotisation(regularisation.getCotisation() + l_mon);
						// l_mon := paf_lecCum(PA_CALCUL.wsal01.nmat,l_cam,PA_CALCUL.t_rub.crub,l_nbul,'M',PA_CALCUL.w_aamm);
						l_mon = fictivesalary.utilNomenclatureFictif.getCumul(fictivesalary, idxDebutPeriodIntS, rubrique.getComp_id().getCrub(), fictivesalary.param.getNumeroBulletin(),
								ClsEnumeration.EnTypeOfColumn.AMOUNT, fictivesalary.param.getMonthOfPay());
						// tab_cotf_reg(l_indcf) := tab_cotf_reg(l_indcf) + NVL(l_mon,0);
						regularisation.setRegularisation(regularisation.getRegularisation() + l_mon);
						//
						// IF l_cam != w_aamm THEN
						if (idxDebutPeriodIntS != null && (!idxDebutPeriodIntS.equals(fictivesalary.param.getMonthOfPay())))
						{
							//
							// l_mon := paf_lecCum(PA_CALCUL.wsal01.nmat,l_cam,PA_CALCUL.rub_plaf,l_nbul,'M',PA_CALCUL.w_aamm);
							l_mon = fictivesalary.utilNomenclatureFictif.getCumul(fictivesalary, idxDebutPeriodIntS, rubrique.getComp_id().getCrub(), fictivesalary.param.getNumeroBulletin(),
									ClsEnumeration.EnTypeOfColumn.AMOUNT, fictivesalary.param.getMonthOfPay());
							//
							// tab_cotf_plaf(l_indcf) := tab_cotf_plaf(l_indcf) + NVL(l_mon,0);
							regularisation.setPlafond(regularisation.getPlafond() + l_mon);
						}
						//
						// END IF;
						//
					}
					// END IF;
				}
				// END LOOP; -- FIN 'FOR l_nbul IN 1..9'
				//
				// l_am := add_per(l_am,0,1);
				idxDebutPeriodInt = ClsGeneralUtil.addPer(idxDebutPeriodInt, 0, 1);
				// l_cam := LTRIM(TO_CHAR(l_am,'099999'));
				idxDebutPeriodIntS = ClsStringUtil.formatNumber(idxDebutPeriodInt, "000000");
				//
				// END LOOP; -- WHILE
			}
			//
			// IF PA_PAIE.NouZ(l_indcf) THEN
			if (idx == 0)
				// RETURN TRUE;
				return true;
			// END IF;
			//

			// W_Retour := char_mont(PA_CALCUL.t_rub.rcon);
			autreRub = fictivesalary.findRubriqueCloneFictif(rubrique.getRcon());
			//
			// IF PA_CALCUL.c_taux != w_tau THEN
			// l_indcf := l_indcf + 1;
			// END IF;
			if (rates != this.fictivesalary.getValeurRubriquePartage().getRates())
				idx++;
			//

			// w_bas := w_bas + PA_CALCUL.cc_mont;
			this.fictivesalary.getValeurRubriquePartage().setBase(this.fictivesalary.getValeurRubriquePartage().getBase() + autreRub.getBase());
			// tab_cotf_base(l_indcf) := tab_cotf_base(l_indcf) + PA_CALCUL.cc_mont;
			d = listOfRegularisationFr.get(idx).getBase();
			listOfRegularisationFr.get(idx).setBase(d + autreRub.getBase());
			// tab_cotf_cot(l_indcf) := tab_cotf_cot(l_indcf) + PA_CALCUL.c_mont;
			d = listOfRegularisationFr.get(idx).getCotisation();
			listOfRegularisationFr.get(idx).setBase(d + autreRub.getAmount());
			// tab_cotf_tau(l_indcf) := PA_CALCUL.c_taux;
			listOfRegularisationFr.get(idx).setTaux(autreRub.getRates());
		}
		// END IF;
		//
		// W_Retour := char_mont(PA_CALCUL.rub_plaf);
		autreRub = fictivesalary.findRubriqueCloneFictif(fictivesalary.param.getPlafondRubrique());
		//
		// tab_cotf_plaf(l_indcf) := tab_cotf_plaf(l_indcf) + PA_CALCUL.c_mont;
		d = listOfRegularisationFr.get(idx).getPlafond();
		listOfRegularisationFr.get(idx).setPlafond(d + autreRub.getAmount());
		//
		// IF w_aamm = f_per THEN
		if (fictivesalary.param.getMonthOfPay().equals(ClsStringUtil.formatNumber(finPeriodInt, "0000")))
			// RETURN TRUE;
			return true;
		// END IF;
		//
		// l_am := add_per(w_aamm,0,1);
		idxDebutPeriodInt = ClsGeneralUtil.addPer(idxDebutPeriodInt, 0, 1);
		//
		// WHILE l_am <= f_per LOOP
		while (idxDebutPeriodInt <= finPeriodInt)
		{
			// l_mois := TO_NUMBER(SUBSTR(LTRIM(TO_CHAR(l_am,'099999')),5,2));
			idxDebutPeriodIntS = ClsStringUtil.formatNumber(idxDebutPeriodInt, "000000");
			l_mois = Integer.valueOf(idxDebutPeriodIntS.substring(4, 5 + 1));
			// tab_cotf_plaf(l_indcf) := tab_cotf_plaf(l_indcf) + PA_CALCUL.plafond(l_mois);
			d = listOfRegularisationFr.get(idx).getPlafond();
			listOfRegularisationFr.get(idx).setPlafond(d + fictivesalary.param.getPlafondOfMonthList().get(l_mois));
			// l_am := add_per(l_am,0,1);
			idxDebutPeriodInt = ClsGeneralUtil.addPer(idxDebutPeriodInt, 0, 1);
		}
		return true;
	}

	/**
	 * => regul_fr Calcul d' une rubrique de regularisation (caisse francaise)
	 */
	public void regularisationFrancaise()
	{

		this.fictivesalary.getValeurRubriquePartage().setBase(0);

		boolean algoResult = this.applyAlgorithm();

		if (algoResult)
		{
			for (int i = 0; i < 12; i++)
			{
				this.fictivesalary.getValeurRubriquePartage().setAmount(this.fictivesalary.getValeurRubriquePartage().getAmount() - this.listOfRegularisationFr.get(i).getCotisation());
				this.fictivesalary.getValeurRubriquePartage().setAmount(this.fictivesalary.getValeurRubriquePartage().getAmount() - this.listOfRegularisationFr.get(i).getRegularisation());
			}
			double montantRegu = this.fictivesalary.getValeurRubriquePartage().getAmount();
			montantRegu = Math.abs(montantRegu);
			if (montantRegu < fictivesalary.param.getRegularisationMinimale())
			{
				this.fictivesalary.getValeurRubriquePartage().setAmount(0);
				this.fictivesalary.getValeurRubriquePartage().setBase(0);
				this.fictivesalary.getValeurRubriquePartage().setBasePlafonnee(0);
				this.fictivesalary.getValeurRubriquePartage().setRates(0);
			}
		}
	}

	/**
	 * => cal_rub_reg Calcul d' une rubrique de regularisation
	 * 
	 * @param nbrePeriodeRegularisation
	 * @param nbrePeriodeRegularisationEv
	 * @return true ou false
	 */
	/*
	 * public boolean calculateRubriqueOfRegularisation(int nbrePeriodeRegularisation, int nbrePeriodeRegularisationEv){ if('O' == fictivesalary.param.getGenfile())
	 * fictivesalary.outputtext+="\n--In Rubrique Clone : >>calculateRubriqueOfRegularisation"); // CURSOR curs_cais IS // SELECT dtad,dtrd FROM pacaiss // WHERE cdos =
	 * wpdos.cdos // AND nmat = wsal01.nmat // AND (rscm = t_rub.rcon OR rpcm = t_rub.rcon); String queryString = "from CaisseMutuelleSalaire" + " where comp_id.cdos = '" +
	 * fictivesalary.param.getDossier() + "'" + " and comp_id.nmat = '" + fictivesalary.getInfoSalary().getComp_id().getNmat() + "'" + " and (comp_id.rscm = '" +
	 * rubrique.getRcon() + "'" + " or rpcm = '" + rubrique.getRcon() + "')"; // // l_dtad DATE; // l_dtrd DATE; // l_amdtad NUMBER(6,0); // l_amdtrd
	 * NUMBER(6,0); // l_am NUMBER(6,0); // mon_regu NUMBER(15,3); // // BEGIN //
	 * ------------------------------------------------------------------------------- // -- Calcul du rang du mois de paie dans l' exercice // -- wmoi :=
	 * TO_NUMBER( SUBSTR( w_aamm, 5, 2) ); --> wmoi = indice du mois // ------------------------------------------------------------------------------- // d_aa :=
	 * TO_NUMBER(TO_CHAR(wpdos.ddex,'YYYY')); // d_mm := TO_NUMBER(TO_CHAR(wpdos.ddex,'MM')); // ddm := d_aa * 100 + d_mm; // rg := wmoi - d_mm + 1; ClsDate
	 * myExcercise = new ClsDate(fictivesalary.param.getDossierDateDebutExercice(), this.fictivesalary.param.FORMAT_DATE); int anneeExercice = myExcercise.getYear(); int
	 * moisExercice = myExcercise.getMonth(); int aammExercice = anneeExercice * 100 + moisExercice; int rangMoisPaieExercice =
	 * fictivesalary.param.getMyMonthOfPay().getMonth() - moisExercice + 1; // IF rg <= 0 THEN // rg := rg + 12; // END IF; if(rangMoisPaieExercice <= 0)
	 * rangMoisPaieExercice += 12; // fictivesalary.getOFictifPeriod().setAnneeExercice(anneeExercice); fictivesalary.getOFictifPeriod().setMoisExercice(moisExercice);
	 * fictivesalary.getOFictifPeriod().setAammExercice(aammExercice); fictivesalary.getOFictifPeriod().setRangMoisPaieExercice(rangMoisPaieExercice); // //
	 * ClsObjectUtil.displayClassProperties(ClsPeriodUtil.class, fictivesalary.getOFictifPeriod()); // //
	 * ------------------------------------------------------------------------------- // -- Determination de la periode de reference //
	 * ------------------------------------------------------------------------------- // -- d_per = Mois de debut // -- f_per = Mois de fin // -- p = Nb mois
	 * de la periode // -- w_a1 = annee de date de debut d'exercice NUMBER // -- w_m1 = mois de date de debut d'exercice NUMBER // -- w_am = Annee_mois de date
	 * de debut d'exercice NUMBER // ------------------------------------------------------------------------------- // w_a1 := d_aa; // w_m1 := d_mm; // w_am :=
	 * ddm; // // IF t_rub.perc = 'T' THEN // W_Faitout := trime(w_am,rg,d_per,f_per,p); // ELSIF t_rub.perc = 'S' THEN // W_Faitout :=
	 * semes(w_am,rg,d_per,f_per,p); // ELSIF t_rub.perc = 'A' THEN // W_Faitout := annee(w_am,d_per,f_per,p); // END IF; if("T".equals(rubrique.getPerc()))
	 * fictivesalary.getOFictifPeriod().trimestre(); else if("S".equals(rubrique.getPerc())) fictivesalary.getOFictifPeriod().semetre(); else if("A".equals(rubrique.getPerc()))
	 * fictivesalary.getOFictifPeriod().annee(aammExercice); // int debutPeriode = fictivesalary.getOFictifPeriod().getDebutPeriode(); int finPeriode =
	 * fictivesalary.getOFictifPeriod().getFinPeriode(); // // --------------------------------------------------------------------------------- // -- Calcul de la
	 * regularisation en fonction de la frequence // --------------------------------------------------------------------------------- // // dec1 := 0; // // IF
	 * t_rub.addf = 'O' AND (wsal01.mrrx = 'RA' OR wsal01.mrrx = 'MU') // AND wsal01.dmrr IS NOT NULL THEN // anneemois :=
	 * TO_NUMBER(TO_CHAR(wsal01.dmrr,'YYYY')) * 100 // + TO_NUMBER(TO_CHAR(wsal01.dmrr,'MM')); // IF anneemois >= d_per AND anneemois <= f_per THEN // w_depdef :=
	 * 'O'; // ELSE // w_depdef := 'N'; // END IF; // ELSE // w_depdef := 'N'; // END IF; int dec1 = 0; int anneemois = 0; char departDefinitif = ' '; ClsDate
	 * myDate = null; if((fictivesalary.getInfoSalary().getDmrr() != null) && "O".equals(rubrique.getAddf()) && ("RA".equals(fictivesalary.getInfoSalary().getMrrx()) ||
	 * "MU".equals(fictivesalary.getInfoSalary().getMrrx()))){ myDate = new ClsDate(fictivesalary.getInfoSalary().getDmrr()); anneemois = myDate.getYear() * 100 +
	 * myDate.getMonth(); departDefinitif = (anneemois >= debutPeriode && anneemois <= finPeriode)? 'O' : 'N'; } else{ departDefinitif = 'N'; } // // IF
	 * w_depdef = 'N' THEN // IF t_rub.freq != 'M' THEN // i := TO_NUMBER(SUBSTR(LTRIM(TO_CHAR(f_per,'099999')),5,2)); // IF wmoi <> i AND w_depdef = 'N' THEN //
	 * w_bas := 0; // w_tau := 0; // w_mon := 0; // w_basp := 0; // dec1 := 1; // END IF; // END IF; // END IF; int i = 0; String finPeriodeS =
	 * ClsStringUtil.formatNumber(finPeriode, "000000"); myDate = new ClsDate(finPeriodeS, ClsFictifParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
	 * if(departDefinitif == 'N'){ if(! "M".equals(rubrique.getFreq())){ i = myDate.getMonth(); if(i != fictivesalary.param.getMyMonthOfPay().getMonth()){
	 * this.fictivesalary.getValeurRubriquePartage().setAmount(0); this.fictivesalary.getValeurRubriquePartage().setBase(0);
	 * this.fictivesalary.getValeurRubriquePartage().setBasePlafonnee(0); this.fictivesalary.getValeurRubriquePartage().setRates(0); dec1 = 1; } } } // // IF dec1 != 0 THEN //
	 * RETURN FALSE; // END IF; if(dec1 != 0){ if('O' == fictivesalary.param.getGenfile()) fictivesalary.outputtext+="\n--In Rubrique Clone : ..............dec1 != 0(1)");
	 * return false; } // // --------------------------------------------------------------------------------- // -- Controle si regul. prend effet en cours
	 * d'exercice // --------------------------------------------------------------------------------- // IF NOT PA_PAIE.NouB(t_rub.pdap) THEN // l_am :=
	 * t_rub.pdap; // IF l_am >= d_per AND l_am <= f_per THEN // d_per := l_am; // END IF; // END IF; int pdap = 0; if(!
	 * ClsObjectUtil.isNull(rubrique.getPdap())){ pdap = Integer.valueOf(rubrique.getPdap()); if(pdap >= debutPeriode && pdap <= finPeriode) debutPeriode =
	 * pdap; } // // --------------------------------------------------------------------------------- // -- Recherche caisses et mutuelles , si on trouve la
	 * rub. de base on extrapole // -- par rapport a la date de debut d'inscription a la caisse //
	 * --------------------------------------------------------------------------------- // dec1 := 0; // OPEN curs_cais; // LOOP // FETCH curs_cais INTO
	 * l_dtad, l_dtrd; // EXIT WHEN curs_cais%NOTFOUND; // // IF l_dtad IS NOT NULL THEN // l_amdtad := TO_NUMBER(TO_CHAR(l_dtad,'YYYY')) * 100 // +
	 * TO_NUMBER(TO_CHAR(l_dtad,'MM')); // IF l_amdtad > d_per AND l_amdtad <= f_per THEN // d_per := l_amdtad; // END IF; // END IF; // IF l_dtrd IS NOT NULL
	 * THEN // l_amdtrd := TO_NUMBER(TO_CHAR(l_dtrd,'YYYY')) * 100 // + TO_NUMBER(TO_CHAR(l_dtrd,'MM')); // IF l_amdtrd < w_aamm THEN // w_basp := 0; // w_bas :=
	 * 0; // w_tau := 0; // w_mon := 0; // dec1 := 1; // END IF; // END IF; // END LOOP; dec1 = 0; List listOfElmtCaisse =
	 * fictivesalary.getService().find(queryString); CaisseMutuelleSalaire oCaisse = null; ClsDate ad = null; ClsDate rd = null; int iad = 0; int ird = 0; for (Object obj :
	 * listOfElmtCaisse) { oCaisse = (CaisseMutuelleSalaire)obj; if(null != oCaisse.getDtad()){ ad = new ClsDate(oCaisse.getDtad()); iad = ad.getYear() * 100 +
	 * ad.getMonth(); if(iad >= debutPeriode && iad <= finPeriode) debutPeriode = iad; } if(null != oCaisse.getDtrd()){ rd = new ClsDate(oCaisse.getDtrd()); ird =
	 * rd.getYear() * 100 + rd.getMonth(); if(ird < aammExercice){ this.fictivesalary.getValeurRubriquePartage().setAmount(0);
	 * this.fictivesalary.getValeurRubriquePartage().setBase(0); this.fictivesalary.getValeurRubriquePartage().setBasePlafonnee(0);
	 * this.fictivesalary.getValeurRubriquePartage().setRates(0); dec1 = 1; } } } // // IF dec1 != 0 THEN // RETURN FALSE; // END IF; if(dec1 != 0){ if('O' ==
	 * fictivesalary.param.getGenfile()) fictivesalary.outputtext+="\n--In Rubrique Clone : ..............dec1 != 0(2)"); return false; } // //
	 * --------------------------------------------------------------------------------- // -- Recherche de la base sur la periode de regularisation //
	 * --------------------------------------------------------------------------------- // // /////////////////////////////W_Faitout :=
	 * cumul_bas;///////////////////////////// this.calculateCumulOfBaseForPeriod(); // // char6 := LTRIM(TO_CHAR(d_per,'099999')); String debutPeriodeS =
	 * ClsStringUtil.formatNumber(debutPeriode, "000000"); myDate = new ClsDate(debutPeriodeS, ClsFictifParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM); //
	 * w_mois_cum := TO_NUMBER(SUBSTR(char6,5,2)); int moisCum = myDate.getMonth(); // // nbm_per := wmoi - w_mois_cum; int nbrePeriod =
	 * fictivesalary.param.getMyMonthOfPay().getMonth() - moisCum; // // IF nbm_per < 0 THEN // nbm_per := nbm_per + 1; // END IF; if(nbrePeriod < 0) nbrePeriod ++; //
	 * nbm_per := nbm_per + 1; nbrePeriod ++; // // IF t_rub.perc = 'T' AND nbm_per > 3 THEN // nbm_per := 3; // ELSIF t_rub.perc = 'S' AND nbm_per > 6 THEN //
	 * nbm_per := 6; // ELSIF t_rub.perc = 'A' AND nbm_per > 12 THEN // nbm_per := 12; // END IF; if("T".equals(rubrique.getPerc()) && nbrePeriod > 3)
	 * nbrePeriod = 3; else if("S".equals(rubrique.getPerc()) && nbrePeriod > 6) nbrePeriod = 6; else if("A".equals(rubrique.getPerc()) && nbrePeriod > 12)
	 * nbrePeriod = 12; // // --------------------------------------------------------------------------------- // -- Cas ou le salarie est entre dans la
	 * societe au cours de la // -- periode de reference // --------------------------------------------------------------------------------- // // w_a1 :=
	 * TO_NUMBER(TO_CHAR(wsal01.dtes,'YYYY')); // w_m1 := TO_NUMBER(TO_CHAR(wsal01.dtes,'MM')); // w_am := w_a1 * 100 + w_m1;
	 * fictivesalary.getOFictifPeriod().setAnneeExercice(myExcercise.getYear()); fictivesalary.getOFictifPeriod().setMoisExercice(myExcercise.getMonth()); aammExercice =
	 * fictivesalary.getOFictifPeriod().getAnneeExercice() * 100 + fictivesalary.getOFictifPeriod().getMoisExercice(); // fictivesalary.getOFictifPeriod().setAammExercice(aammExercice); // // IF w_am >
	 * f_per THEN // w_bas := 0; // w_tau := 0; // w_mon := 0; // w_basp := 0; // RETURN TRUE; // ELSE // IF w_am >= d_per THEN // IF t_rub.eddf = 'N' THEN //
	 * w_am := d_per; // nbm_per := 0; // WHILE w_am <= w_aamm LOOP // w_am := PA_ALGO.add_per(w_am,0,1); // nbm_per := nbm_per + 1; // END LOOP; // ELSE //
	 * nbm_per := 0; // WHILE w_am <= w_aamm LOOP // w_am := PA_ALGO.add_per(w_am,0,1); // nbm_per := nbm_per + 1; // END LOOP; // END IF; // END IF; // END IF;
	 * aammExercice = fictivesalary.getOFictifPeriod().getAammExercice(); int v = fictivesalary.param.getMyMonthOfPay().getYear() * 100 + fictivesalary.param.getMyMonthOfPay().getMonth();
	 * if(aammExercice > finPeriode){ this.fictivesalary.getValeurRubriquePartage().setAmount(0); this.fictivesalary.getValeurRubriquePartage().setBase(0);
	 * this.fictivesalary.getValeurRubriquePartage().setBasePlafonnee(0); this.fictivesalary.getValeurRubriquePartage().setRates(0); // if('O' == fictivesalary.param.getGenfile())
	 * fictivesalary.outputtext+="\n--In Rubrique Clone : La période aammExercice "+aammExercice+" est sup é finPeriode "+finPeriode+" return true;"); return true; }
	 * else{ if(aammExercice > debutPeriode){ if("N".equals(rubrique.getEddf())){ aammExercice = debutPeriode; nbrePeriod = 0; while(aammExercice < v){
	 * aammExercice = fictivesalary.getOFictifPeriod().addPer(v, 0, 1); nbrePeriod ++; } } else{ nbrePeriod = 0; while(aammExercice < v){ aammExercice =
	 * fictivesalary.getOFictifPeriod().addPer(v, 0, 1); nbrePeriod ++; } } } } // fictivesalary.getOFictifPeriod().setAammExercice(aammExercice); // //
	 * --------------------------------------------------------------------------------- // -- Extrapolation de la base de calcul a la periode concernee //
	 * --------------------------------------------------------------------------------- // // nbm_per := nbm_per + nbm_sup; // // -- Modif P.A. 03/01/96 // --
	 * Cas ou il ne faut prendre en compte pour le calcul de la regul // -- que les mois ou l'agent a travaille // IF reg_cas = 'T' THEN // nbm_per := per_regu +
	 * 1; // ELSIF reg_cas = 'J' THEN // -- Modif LH 090698 // -- Regul en jours, il faut tenir compte de la periode d'imposition // IF t_rub.perc = 'A' // THEN //
	 * W_Faitout := PA_ALGO.char_mont(rub_nbjt_totA); // END IF; // IF t_rub.perc = 'S' // THEN // W_Faitout := PA_ALGO.char_mont(rub_nbjt_totS); // END IF; //
	 * IF t_rub.perc = 'T' // THEN // W_Faitout := PA_ALGO.char_mont(rub_nbjt_totT); // END IF; // nbm_per := NVL(c_mont,0) / 30; // -- Rien trouve donc 1er
	 * mois // IF PA_PAIE.NouZ(nbm_per) THEN // nbm_per := 1; // END IF; // END IF; nbrePeriod += fictivesalary.workTimeFictif.getNbreMoisSuppl();
	 * ClsFictifRubriqueClone rub = null; if("T".equals(fictivesalary.param.getRegularisationCas())) nbrePeriod += nbrePeriodeRegularisation; else{
	 * if("J".equals(fictivesalary.param.getRegularisationCas())){ if("A".equals(rubrique.getPerc())) rub =
	 * fictivesalary.findRubriqueCloneFictif(fictivesalary.param.getJourWorkDepuisExerciceRubrique()); if("S".equals(rubrique.getPerc())) rub =
	 * fictivesalary.findRubriqueCloneFictif(fictivesalary.param.getJourWorkDepuisSemestreRubrique()); if("T".equals(rubrique.getPerc())) rub =
	 * fictivesalary.findRubriqueCloneFictif(fictivesalary.param.getJourWorkDepuisTrimestreRubrique()); } nbrePeriod = rub == null? 0 : new Double(rub.getAmount() /
	 * 30).intValue(); if(nbrePeriod <= 0) nbrePeriod = 1; } // // -- Cas ou le nombre de periode de regul a ete saisi en EV // IF NOT PA_PAIE.NouZ(nbper_regu)
	 * THEN // nbm_per := nbper_regu; // END IF; if(nbrePeriodeRegularisationEv > 0) nbrePeriod = nbrePeriodeRegularisationEv; // // IF w_depdef = 'O' THEN //
	 * IF t_rub.eddf = 'O' THEN // w_bas := (cum_basc / nbm_per) * p; // ELSE // w_bas := cum_basc; // END IF; // ELSE // w_bas := (cum_basc / nbm_per) * p; //
	 * END IF; if('O' == departDefinitif){ if("O".equals(rubrique.getEddf())){ if(nbrePeriodeRegularisation != 0)
	 * this.fictivesalary.getValeurRubriquePartage().setBase((fictivesalary.getParamCumul().getCumulBaseCalc() / nbrePeriodeRegularisation) *
	 * this.fictivesalary.getOFictifPeriod().getP()); } else this.fictivesalary.getValeurRubriquePartage().setBase(this.fictivesalary.getParamCumul().getCumulBaseCalc()); } else{
	 * if(nbrePeriodeRegularisation != 0) this.fictivesalary.getValeurRubriquePartage().setBase((this.fictivesalary.getParamCumul().getCumulBaseCalc() /
	 * nbrePeriodeRegularisation) * this.fictivesalary.getOFictifPeriod().getP()); } // //
	 * PA_ALGO.calc_algo(w_bas,w_basp,w_mon,w_tau,w_argu,w_aamm,wsd_fcal1.nbul,Pb_Calcul); // IF pb_calcul THEN // RETURN FALSE; // END IF; boolean algoResult =
	 * this.applyAlgorithm(); if(! algoResult) return false; // // --------------------------------------------------------------------------------- // --
	 * Extrapolation du montant des cotisations du mois traite // --------------------------------------------------------------------------------- // // IF
	 * w_depdef = 'O' THEN // IF t_rub.eddf = 'O' THEN // mon_regu := (w_mon * nbm_per) / p; // ELSE // mon_regu := w_mon; // END IF; // ELSE // mon_regu :=
	 * (w_mon * nbm_per) / p; // END IF; double montantRegu = 0; if('O' == departDefinitif){ if("O".equals(rubrique.getEddf())){ if(
	 * this.fictivesalary.getOFictifPeriod().getP() != 0) montantRegu = (this.fictivesalary.getValeurRubriquePartage().getAmount() * nbrePeriodeRegularisation) /
	 * this.fictivesalary.getOFictifPeriod().getP(); } else montantRegu = this.fictivesalary.getValeurRubriquePartage().getAmount(); } else{ if( this.fictivesalary.getOFictifPeriod().getP() !=
	 * 0) montantRegu = (this.fictivesalary.getValeurRubriquePartage().getAmount() * nbrePeriodeRegularisation) / this.fictivesalary.getOFictifPeriod().getP(); } // //
	 * --------------------------------------------------------------------------------- // -- Calcul du montant de la regularisation //
	 * --------------------------------------------------------------------------------- // w_mon := mon_regu - cum_coti - cum_regu; // w_bas := cum_basc; //
	 * w_basp := cum_basc; this.fictivesalary.getValeurRubriquePartage().setAmount(montantRegu - this.fictivesalary.getParamCumul().getCumulCoti() -
	 * this.fictivesalary.getParamCumul().getCumulRegu()); this.fictivesalary.getValeurRubriquePartage().setBase(this.fictivesalary.getParamCumul().getCumulBaseCalc());
	 * this.fictivesalary.getValeurRubriquePartage().setBasePlafonnee(this.fictivesalary.getParamCumul().getCumulBaseCalc()); // //
	 * --------------------------------------------------------------------------------- // -- Comparaison du montant de la regularisation avec le montant // --
	 * minimal de regularisation (parametre REGMINI table 99) // --------------------------------------------------------------------------------- // // --
	 * ----- Arrondi du resultat de la regul avant test avec REGMINI // IF NOT PA_PAIE.NouB(t_rub.arro) THEN // arrondi(w_mon); // IF pb_calcul THEN // RETURN
	 * FALSE; // END IF; // END IF; double mnt = 0; if(! ClsObjectUtil.isNull(rubrique.getArro())){ mnt = this.calculateArrondi(fictivesalary.getParam(),
	 * this.fictivesalary.getValeurRubriquePartage().getAmount()); this.fictivesalary.getValeurRubriquePartage().setAmount(mnt); } // mon_regu := w_mon; // // IF mon_regu < 0
	 * THEN // mon_regu := mon_regu * -1; // END IF; montantRegu = Math.abs(this.fictivesalary.getValeurRubriquePartage().getAmount()); // // IF mon_regu < reg_mini
	 * THEN // w_bas := 0; // w_tau := 0; // w_mon := 0; // w_basp := 0; // END IF; if(montantRegu < fictivesalary.param.getRegularisationMinimale()){
	 * this.fictivesalary.getValeurRubriquePartage().setAmount(0); this.fictivesalary.getValeurRubriquePartage().setBase(0);
	 * this.fictivesalary.getValeurRubriquePartage().setBasePlafonnee(0); this.fictivesalary.getValeurRubriquePartage().setRates(0); }
	 * this.fictivesalary.getOFictifPeriod().setP(nbrePeriod); // // RETURN TRUE; return true; }
	 */

	/**
	 * => cal_rub_reg Calcul d' une rubrique de regularisation --------------------------------------------------------------------------------- -- Calcul d'
	 * une rubrique de regularisation --------------------------------------------------------------------------------- -- d_per = periode de debut d'imposition
	 * (AAAAMM) -- f_per = periode de fin d'imposition (AAAAMM) -- nbm_per = Nombre de mois traites, mois de paie inclus, sur la periode -- p = Nombre de mois
	 * total sur la periode -- mon_regu = montant de l'impot ramene au mois de paie -- cum_coti = Montant deja cotise, mois de paie inclus -- cum_regu = Montant
	 * deja regule, mois de paie inclus -- w_mon = Montant de la regularisation pour ce mois
	 * ---------------------------------------------------------------------------------
	 * 
	 * @param nbrePeriodeRegularisationEv
	 * @return true ou false
	 */

	public boolean calculateRubriqueOfRegularisation1(int nbrePeriodeRegularisationEv)
	{

		String queryString = "from CaisseMutuelleSalaire" + " where comp_id.cdos = '" + fictivesalary.param.getDossier() + "'" + " and comp_id.nmat = '" + fictivesalary.getInfoSalary().getComp_id().getNmat() + "'"
				+ " and (comp_id.rscm = '" + rubrique.getRcon() + "'" + " or rpcm = '" + rubrique.getRcon() + "')";
//		 l_dtad   DATE;
//		   l_dtrd   DATE;
//		   l_amdtad NUMBER(6,0);
//		   l_amdtrd NUMBER(6,0);
//		   l_am     NUMBER(6,0);
//		   mon_regu NUMBER(15,3);
//
//		   -- LH 170298
//		   Nbj_reg_dern_mois    NUMBER(5,2);
		double Nbj_reg_dern_mois;
		// BEGIN
		// -------------------------------------------------------------------------------
		// -- Calcul du rang du mois de paie dans l' exercice
		// -- wmoi := TO_NUMBER( SUBSTR( w_aamm, 5, 2) ); --> wmoi = indice du mois
		// -------------------------------------------------------------------------------
		// d_aa := TO_NUMBER(TO_CHAR(wpdos.ddex,'YYYY'));
		// d_mm := TO_NUMBER(TO_CHAR(wpdos.ddex,'MM'));
		// ddm := d_aa * 100 + d_mm;
		// rg := wmoi - d_mm + 1;
		// if('O' == fictivesalary.param.getGenfile()) fictivesalary.outputtext+="\n--In Rubrique Clone : >>fictivesalary.param.getDossierDateDebutExercice():" +
		// fictivesalary.param.getDossierDateDebutExercice());
		// ClsDate myExcercise = new ClsDate(fictivesalary.param.getDossierDateDebutExercice(), this.fictivesalary.param.getAppDateFormat());
		int debutExerciceAnnee = fictivesalary.param.getDebutExerciceAnnee();// myExcercise.getYear();
		int debutExerciceMois = fictivesalary.param.getDebutExerciceMois();// myExcercise.getMonth();
		int debutExerciceaamm = fictivesalary.param.getDebutExerciceaamm(); // debutExerciceAnnee * 100 + debutExerciceMois;
		int debutExerciceRangMoisPaie = fictivesalary.param.getDebutExerciceRangMoisPaie();// fictivesalary.param.getMyMonthOfPay().getMonth() - debutExerciceMois +
		// 1;
		if ('O' == fictivesalary.param.getGenfile())
			fictivesalary.outputtext += "\n--In Rubrique Clone : >>debutExerciceRangMoisPaie:" + debutExerciceRangMoisPaie;
		// IF rg <= 0 THEN
		// rg := rg + 12;
		// END IF;
		if (debutExerciceRangMoisPaie <= 0)
			debutExerciceRangMoisPaie += 12;
		//
		// ClsObjectUtil.displayClassProperties(ClsPeriodUtil.class, fictivesalary.getOFictifPeriod());
		//
		// -------------------------------------------------------------------------------
		// -- Determination de la periode de reference
		// -------------------------------------------------------------------------------
		// -- d_per = Mois de debut
		// -- f_per = Mois de fin
		// -- p = Nb mois de la periode
		// -- w_a1 = annee de date de debut d'exercice NUMBER
		// -- w_m1 = mois de date de debut d'exercice NUMBER
		// -- w_am = Annee_mois de date de debut d'exercice NUMBER
		// -------------------------------------------------------------------------------
		// w_a1 := d_aa;
		// w_m1 := d_mm;
		// w_am := ddm;
		//
		// IF t_rub.perc = 'T' THEN
		// W_Faitout := trime(w_am,rg,d_per,f_per,p);
		// ELSIF t_rub.perc = 'S' THEN
		// W_Faitout := semes(w_am,rg,d_per,f_per,p);
		// ELSIF t_rub.perc = 'A' THEN
		// W_Faitout := annee(w_am,d_per,f_per,p);
		// END IF;
		//
		fictivesalary.getOFictifPeriod().setAnneeExercice(debutExerciceAnnee);
		fictivesalary.getOFictifPeriod().setMoisExercice(debutExerciceMois);
		fictivesalary.getOFictifPeriod().setAammExercice(debutExerciceaamm);
		fictivesalary.getOFictifPeriod().setRangMoisPaieExercice(debutExerciceRangMoisPaie);
		if ('O' == fictivesalary.param.getGenfile())
		{
//			ParameterUtil.println(fictivesalary.param.getMonthOfPay()+" AnneeExercice Rubrique "+this.getRubrique().getComp_id().getCrub()+" = "+debutExerciceAnnee);
//			ParameterUtil.println(fictivesalary.param.getMonthOfPay()+" MoisExercice Rubrique "+this.getRubrique().getComp_id().getCrub()+" = "+debutExerciceMois);
//			ParameterUtil.println(fictivesalary.param.getMonthOfPay()+" AammExercice Rubrique "+this.getRubrique().getComp_id().getCrub()+" = "+debutExerciceaamm);
//			ParameterUtil.println(fictivesalary.param.getMonthOfPay()+" RangMoisPaieExercice Rubrique "+this.getRubrique().getComp_id().getCrub()+" = "+debutExerciceRangMoisPaie);
		}

		if ('O' == fictivesalary.param.getGenfile())
			fictivesalary.outputtext += "\n--In Rubrique Clone, operiod from exercice = " + String.valueOf(fictivesalary.getOFictifPeriod());
		//
		if ("T".equals(rubrique.getPerc()))
			fictivesalary.getOFictifPeriod().trimestre();
		else if ("S".equals(rubrique.getPerc()))
			fictivesalary.getOFictifPeriod().semetre();
		else if ("A".equals(rubrique.getPerc()))
			fictivesalary.getOFictifPeriod().annee();
		if ('O' == fictivesalary.param.getGenfile())
			fictivesalary.outputtext += "\n--In Rubrique Clone, operiod from exercice after modify(trime, seme, annee) = " + String.valueOf(fictivesalary.getOFictifPeriod());
		//
//		int debutPeriode = fictivesalary.getOFictifPeriod().getDebutPeriode();
//		int finPeriode = fictivesalary.getOFictifPeriod().getFinPeriode();

		//
		// ---------------------------------------------------------------------------------
		// -- Calcul de la regularisation en fonction de la frequence
		// ---------------------------------------------------------------------------------
		//
		// dec1 := 0;
		//
		// IF t_rub.addf = 'O' AND (wsal01.mrrx = 'RA' OR wsal01.mrrx = 'MU')
		// AND wsal01.dmrr IS NOT NULL THEN
		// anneemois := TO_NUMBER(TO_CHAR(wsal01.dmrr,'YYYY')) * 100
		// + TO_NUMBER(TO_CHAR(wsal01.dmrr,'MM'));
		// IF anneemois >= d_per AND anneemois <= f_per THEN
		// w_depdef := 'O';
		// ELSE
		// w_depdef := 'N';
		// END IF;
		// ELSE
		// w_depdef := 'N';
		// END IF;
		int dec1 = 0;
		int anneemois = 0;
		char departDefinitif = ' ';
		ClsDate myDate = null;
		if ((fictivesalary.getInfoSalary().getDmrr() != null) && "O".equals(rubrique.getAddf()) && ("RA".equals(fictivesalary.getInfoSalary().getMrrx()) || "MU".equals(fictivesalary.getInfoSalary().getMrrx())))
		{
			myDate = new ClsDate(fictivesalary.getInfoSalary().getDmrr());
			anneemois = myDate.getYear() * 100 + myDate.getMonth();
			departDefinitif = (anneemois >= fictivesalary.getOFictifPeriod().getDebutPeriode() && anneemois <= fictivesalary.getOFictifPeriod().getFinPeriode()) ? 'O' : 'N';
		}
		else
		{
			departDefinitif = 'N';
		}
		//
		// IF w_depdef = 'N' THEN
		// IF t_rub.freq != 'M' THEN
		// i := TO_NUMBER(SUBSTR(LTRIM(TO_CHAR(f_per,'099999')),5,2));
		// IF wmoi <> i AND w_depdef = 'N' THEN
		// w_bas := 0;
		// w_tau := 0;
		// w_mon := 0;
		// w_basp := 0;
		// dec1 := 1;
		// END IF;
		// END IF;
		// END IF;
		int i = 0;
		String finPeriodeS = ClsStringUtil.formatNumber(fictivesalary.getOFictifPeriod().getFinPeriode(), "000000");
		myDate = new ClsDate(finPeriodeS, ClsFictifParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
		if (departDefinitif == 'N')
		{
			if (!"M".equals(rubrique.getFreq()))
			{
				i = myDate.getMonth();
				if (i != fictivesalary.param.getMyMonthOfPay().getMonth())
				{
					this.fictivesalary.getValeurRubriquePartage().setAmount(0);
					this.fictivesalary.getValeurRubriquePartage().setBase(0);
					this.fictivesalary.getValeurRubriquePartage().setBasePlafonnee(0);
					this.fictivesalary.getValeurRubriquePartage().setRates(0);
					dec1 = 1;
				}
			}
		}
		//
		// IF dec1 != 0 THEN
		// RETURN FALSE;
		// END IF;
		if (dec1 != 0)
		{
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "\n--In Rubrique Clone : ..............dec1 != 0(1)";
			return false;
		}
		//
		// ---------------------------------------------------------------------------------
		// -- Controle si regul. prend effet en cours d'exercice
		// ---------------------------------------------------------------------------------
		// IF NOT PA_PAIE.NouB(t_rub.pdap) THEN
		// l_am := t_rub.pdap;
		// IF l_am >= d_per AND l_am <= f_per THEN
		// d_per := l_am;
		// END IF;
		// END IF;
		int pdap = 0;
		if (!ClsObjectUtil.isNull(rubrique.getPdap()))
		{
			pdap = Integer.valueOf(rubrique.getPdap());
			if (pdap >= fictivesalary.getOFictifPeriod().getDebutPeriode() && pdap <= fictivesalary.getOFictifPeriod().getFinPeriode())
				fictivesalary.getOFictifPeriod().setDebutPeriode(pdap);
		}
		//
		// ---------------------------------------------------------------------------------
		// -- Recherche caisses et mutuelles , si on trouve la rub. de base on extrapole
		// -- par rapport a la date de debut d'inscription a la caisse
		// ---------------------------------------------------------------------------------
		// dec1 := 0;
		// OPEN curs_cais;
		// LOOP
		// FETCH curs_cais INTO l_dtad, l_dtrd;
		// EXIT WHEN curs_cais%NOTFOUND;
		//
		// IF l_dtad IS NOT NULL THEN
		// l_amdtad := TO_NUMBER(TO_CHAR(l_dtad,'YYYY')) * 100
		// + TO_NUMBER(TO_CHAR(l_dtad,'MM'));
		// IF l_amdtad > d_per AND l_amdtad <= f_per THEN
		// d_per := l_amdtad;
		// END IF;
		// END IF;
		// IF l_dtrd IS NOT NULL THEN
		// l_amdtrd := TO_NUMBER(TO_CHAR(l_dtrd,'YYYY')) * 100
		// + TO_NUMBER(TO_CHAR(l_dtrd,'MM'));
		// IF l_amdtrd < w_aamm THEN
		// w_basp := 0;
		// w_bas := 0;
		// w_tau := 0;
		// w_mon := 0;
		// dec1 := 1;
		// END IF;
		// END IF;
		// END LOOP;
		dec1 = 0;
		List listOfElmtCaisse = fictivesalary.getService().find(queryString);
		CaisseMutuelleSalarie oCaisse = null;
		ClsDate ad = null;
		ClsDate rd = null;
		int iad = 0;
		int ird = 0;
		for (Object obj : listOfElmtCaisse)
		{
			oCaisse = (CaisseMutuelleSalarie) obj;
			if (null != oCaisse.getDtad())
			{
				ad = new ClsDate(oCaisse.getDtad());
				iad = ad.getYear() * 100 + ad.getMonth();
				if (iad > fictivesalary.getOFictifPeriod().getDebutPeriode() && iad <= fictivesalary.getOFictifPeriod().getFinPeriode())
					fictivesalary.getOFictifPeriod().setDebutPeriode(iad);
			}
			if (null != oCaisse.getDtrd())
			{
				rd = new ClsDate(oCaisse.getDtrd());
				ird = rd.getYear() * 100 + rd.getMonth();
				if (ird < this.fictivesalary.param.getMyMonthOfPay().getYearAndMonthInt())
				{
					this.fictivesalary.getValeurRubriquePartage().setAmount(0);
					this.fictivesalary.getValeurRubriquePartage().setBase(0);
					this.fictivesalary.getValeurRubriquePartage().setBasePlafonnee(0);
					this.fictivesalary.getValeurRubriquePartage().setRates(0);
					dec1 = 1;
				}
			}
		}
		//
		// IF dec1 != 0 THEN
		// RETURN FALSE;
		// END IF;
		if (dec1 != 0)
		{
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "\n--In Rubrique Clone : ..............dec1 != 0(2)";
			return false;
		}
		//
		// ---------------------------------------------------------------------------------
		// -- Recherche de la base sur la periode de regularisation
		// ---------------------------------------------------------------------------------
		//
		// /////////////////////////////W_Faitout := cumul_bas;/////////////////////////////
		this.calculateCumulOfBaseForPeriod();
		if(StringUtils.equals("3260", this.rubrique.getComp_id().getCrub()))
		{
			ParameterUtil.println(fictivesalary.param.getMonthOfPay()+" Base calcul Rubrique "+this.getRubrique().getComp_id().getCrub()+" = "+this.fictivesalary.getValeurRubriquePartage().getBase());
			ParameterUtil.println(fictivesalary.param.getMonthOfPay()+" Base calcul regulisation Rubrique "+this.getRubrique().getComp_id().getCrub()+" = "+this.fictivesalary.getParamCumul().getCumulBaseCalc());
			ParameterUtil.println(fictivesalary.param.getMonthOfPay()+" Coti calcul regulisation Rubrique "+this.getRubrique().getComp_id().getCrub()+" = "+this.fictivesalary.getParamCumul().getCumulCoti());
			ParameterUtil.println(fictivesalary.param.getMonthOfPay()+" Regu calcul regulisation Rubrique "+this.getRubrique().getComp_id().getCrub()+" = "+this.fictivesalary.getParamCumul().getCumulRegu());
			ParameterUtil.println(fictivesalary.param.getMonthOfPay()+" Nombre de Period calcul regulisation Rubrique "+this.getRubrique().getComp_id().getCrub()+" = "+this.fictivesalary.getParamCumul().getNbreMoisCumul());
		}

		//
		// char6 := LTRIM(TO_CHAR(d_per,'099999'));
		String debutPeriodeS = ClsStringUtil.formatNumber(fictivesalary.getOFictifPeriod().getDebutPeriode(), "000000");
		myDate = new ClsDate(debutPeriodeS, ClsFictifParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
		// w_mois_cum := TO_NUMBER(SUBSTR(char6,5,2));
		int moisCum = myDate.getMonth();
		//
		// nbm_per := wmoi - w_mois_cum;
		double nbrePeriod = fictivesalary.param.getMyMonthOfPay().getMonth() - moisCum;
		if(StringUtils.equals("3260", this.rubrique.getComp_id().getCrub()))
			ParameterUtil.println("Nombre de période Initial = "+nbrePeriod);
		//
		// IF nbm_per < 0 THEN
		// nbm_per := nbm_per + 1;
		// END IF;
		// nbm_per := nbm_per + 1;
		if (nbrePeriod < 0)
			nbrePeriod++;
		
//		 IF dern_mois_de_conge THEN
//	      -- nbm_per := nbm_per + (nbj_cabf/30);
//	      Nbj_reg_dern_mois := Nbj_Presence_du_Mois + Nbj_Conges_Cal_du_Mois;
//	      nbm_per := nbm_per + ( Nbj_reg_dern_mois / 30);
//	   ELSE
//	      nbm_per := nbm_per + 1;
//	   END IF;
		//Nbj_Presence_du_Mois = this.workTimeFictif.getNbreJourPresence()
		//Nbj_Conges_Cal_du_Mois = this.workTimeFictif.getNbreJoursAbsencePourCongeAnnuel()
		if(fictivesalary.dern_mois_de_conge)
		{
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "\n--In Rubrique Clone :Dernier mois de conges";
			Nbj_reg_dern_mois = fictivesalary.workTimeFictif.getNbreJourPresence() + fictivesalary.workTimeFictif.getNbreJoursAbsencePourCongeAnnuel();
			if(StringUtils.equals("3260", this.rubrique.getComp_id().getCrub()))
			{
				ParameterUtil.println("Nbj_reg_dern_mois = "+Nbj_reg_dern_mois+" Du fait que Nbr Jours Presence = "+fictivesalary.workTimeFictif.getNbreJourPresence()+" et Nbr Jour absc pr conge annuel = "+fictivesalary.workTimeFictif.getNbreJoursAbsencePourCongeAnnuel());
				ParameterUtil.println("Ajout de "+Nbj_reg_dern_mois+"/30 = "+Nbj_reg_dern_mois/30);
			}
			nbrePeriod += Nbj_reg_dern_mois/30;
			if(StringUtils.equals("3260", this.rubrique.getComp_id().getCrub()))
				ParameterUtil.println("Nombre de période au dernier mois de conge = "+nbrePeriod);
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "\n--In Rubrique Clone :Nbj_reg_dern_mois = "+Nbj_reg_dern_mois;
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "\n--In Rubrique Clone :Nombre de période = "+nbrePeriod;
		}
		else
			nbrePeriod++;
		
		if(StringUtils.equals("3260", this.rubrique.getComp_id().getCrub()))
			ParameterUtil.println("Nombre de période aprés test dernier mois de conge = "+nbrePeriod);

		//
		// IF t_rub.perc = 'T' AND nbm_per > 3 THEN
		// nbm_per := 3;
		// ELSIF t_rub.perc = 'S' AND nbm_per > 6 THEN
		// nbm_per := 6;
		// ELSIF t_rub.perc = 'A' AND nbm_per > 12 THEN
		// nbm_per := 12;
		// END IF;
		if ("T".equals(rubrique.getPerc()) && nbrePeriod > 3)
			nbrePeriod = 3;
		else if ("S".equals(rubrique.getPerc()) && nbrePeriod > 6)
			nbrePeriod = 6;
		else if ("A".equals(rubrique.getPerc()) && nbrePeriod > 12)
			nbrePeriod = 12;
		
		if(StringUtils.equals("3260", this.rubrique.getComp_id().getCrub()))
			ParameterUtil.println("Nombre de période pour se rassurer qu'il n'exéde pas 3 , 6 ou 9 = "+nbrePeriod);
		//
		// ---------------------------------------------------------------------------------
		// -- Cas ou le salarie est entre dans la societe au cours de la
		// -- periode de reference
		// ---------------------------------------------------------------------------------
		//
		// w_a1 := TO_NUMBER(TO_CHAR(wsal01.dtes,'YYYY'));
		// w_m1 := TO_NUMBER(TO_CHAR(wsal01.dtes,'MM'));
		// w_am := w_a1 * 100 + w_m1;

		// fictivesalary.getOFictifPeriod().setAnneeExercice(myExcercise.getYear());
		// fictivesalary.getOFictifPeriod().setMoisExercice(myExcercise.getMonth());
		// aammExercice = fictivesalary.getOFictifPeriod().getAnneeExercice() * 100 + fictivesalary.getOFictifPeriod().getMoisExercice();
		//
		// fictivesalary.getOFictifPeriod().setAammExercice(aammExercice);

		//
		// IF w_am > f_per THEN
		// w_bas := 0;
		// w_tau := 0;
		// w_mon := 0;
		// w_basp := 0;
		// RETURN TRUE;
		// ELSE
		// IF w_am >= d_per THEN
		// IF t_rub.eddf = 'N' THEN
		// w_am := d_per;
		// nbm_per := 0;
		// WHILE w_am <= w_aamm LOOP
		// w_am := PA_ALGO.add_per(w_am,0,1);
		// nbm_per := nbm_per + 1;
		// END LOOP;
		// ELSE
		// nbm_per := 0;
		// WHILE w_am <= w_aamm LOOP
		// w_am := PA_ALGO.add_per(w_am,0,1);
		// nbm_per := nbm_per + 1;
		// END LOOP;
		// END IF;
		// END IF;
		// END IF;

		// @add by yannick
		ClsDate dtes = new ClsDate(fictivesalary.getInfoSalary().getDtes());
		debutExerciceAnnee = dtes.getYear();
		debutExerciceMois = dtes.getMonth();
		debutExerciceaamm = debutExerciceAnnee * 100 + debutExerciceMois;
		fictivesalary.getOFictifPeriod().setAammExercice(debutExerciceaamm);

		if (debutExerciceaamm > fictivesalary.getOFictifPeriod().getFinPeriode())
		{
			ParameterUtil.println("Debut exercice "+debutExerciceaamm+" est sup é la fin de période "+fictivesalary.getOFictifPeriod().getFinPeriode()+" -> tous les valeurs é 0 et sortie");
			this.fictivesalary.getValeurRubriquePartage().setBase(0);
			this.fictivesalary.getValeurRubriquePartage().setRates(0);
			this.fictivesalary.getValeurRubriquePartage().setAmount(0);
			this.fictivesalary.getValeurRubriquePartage().setBasePlafonnee(0);
			return true;
		}
		else
		{
			if (debutExerciceaamm >= fictivesalary.getOFictifPeriod().getDebutPeriode())
			{
				if ("N".equalsIgnoreCase(rubrique.getEddf()))
				{
					debutExerciceaamm = fictivesalary.getOFictifPeriod().getDebutPeriode();
					fictivesalary.getOFictifPeriod().setAammExercice(debutExerciceaamm);
					nbrePeriod = 0;
					while (debutExerciceaamm <= fictivesalary.param.getMyMonthOfPay().getYearAndMonthInt())
					{
						debutExerciceaamm = ClsGeneralUtil.addPer(debutExerciceaamm, 0, 1);
						fictivesalary.getOFictifPeriod().setAammExercice(debutExerciceaamm);
						nbrePeriod++;
					}
				}
				else
				{
					nbrePeriod = 0;
					while (debutExerciceaamm <= fictivesalary.param.getMyMonthOfPay().getYearAndMonthInt())
					{
						debutExerciceaamm = ClsGeneralUtil.addPer(debutExerciceaamm, 0, 1);
						fictivesalary.getOFictifPeriod().setAammExercice(debutExerciceaamm);
						nbrePeriod++;
					}
					if(StringUtils.equals("3260", this.rubrique.getComp_id().getCrub()))
						ParameterUtil.println("Nombre de période aprés test sur date entree societe = "+nbrePeriod);
				}
			}
		}

		//
		// ---------------------------------------------------------------------------------
		// -- Extrapolation de la base de calcul a la periode concernee
		// ---------------------------------------------------------------------------------
		//
		// nbm_per := nbm_per + nbm_sup;
		//
		// -- Modif P.A. 03/01/96
		// -- Cas ou il ne faut prendre en compte pour le calcul de la regul
		// -- que les mois ou l'agent a travaille
		// IF reg_cas = 'T' THEN
		// nbm_per := per_regu + 1;
		// ELSIF reg_cas = 'J' THEN
		// -- Modif LH 090698
		// -- Regul en jours, il faut tenir compte de la periode d'imposition
		// IF t_rub.perc = 'A'
		// THEN
		// W_Faitout := PA_ALGO.char_mont(rub_nbjt_totA);
		// END IF;
		// IF t_rub.perc = 'S'
		// THEN
		// W_Faitout := PA_ALGO.char_mont(rub_nbjt_totS);
		// END IF;
		// IF t_rub.perc = 'T'
		// THEN
		// W_Faitout := PA_ALGO.char_mont(rub_nbjt_totT);
		// END IF;
		// nbm_per := NVL(c_mont,0) / 30;
		// -- Rien trouve donc 1er mois
		// IF PA_PAIE.NouZ(nbm_per) THEN
		// nbm_per := 1;
		// END IF;
		// END IF;
		
		//nbrePeriod += fictivesalary.workTimeFictif.getNbreMoisSuppl();
		
		if ('O' == fictivesalary.param.getGenfile())
			fictivesalary.outputtext += "\n--In Rubrique Clone : Regularisation cas = "+fictivesalary.param.getRegularisationCas();
		ClsFictifRubriqueClone rub = null;
		if ("T".equals(fictivesalary.param.getRegularisationCas()))
			nbrePeriod += fictivesalary.getParamCumul().getNbreMoisCumul() + 1;
		else
		{
			if ("J".equals(fictivesalary.param.getRegularisationCas()))
			{
				if ("A".equals(rubrique.getPerc()))
					rub = fictivesalary.findRubriqueCloneFictif(fictivesalary.param.getJourWorkDepuisExerciceRubrique());
				if ("S".equals(rubrique.getPerc()))
					rub = fictivesalary.findRubriqueCloneFictif(fictivesalary.param.getJourWorkDepuisSemestreRubrique());
				if ("T".equals(rubrique.getPerc()))
					rub = fictivesalary.findRubriqueCloneFictif(fictivesalary.param.getJourWorkDepuisTrimestreRubrique());

			
			nbrePeriod = rub == null ? 0 : new Double(rub.getAmount() / 30).intValue();
			if (nbrePeriod == 0)
				nbrePeriod = 1;
			}
		}
		//
		// -- Cas ou le nombre de periode de regul a ete saisi en EV
		// IF NOT PA_PAIE.NouZ(nbper_regu) THEN
		// nbm_per := nbper_regu;
		// END IF;
		if (nbrePeriodeRegularisationEv > 0)
			nbrePeriod = nbrePeriodeRegularisationEv;
		if(StringUtils.equals("3260", this.rubrique.getComp_id().getCrub()))
			ParameterUtil.println("Nombre de période aprés test si nbr saisie en ev = "+nbrePeriod);
		//
		// IF w_depdef = 'O' THEN
		// IF t_rub.eddf = 'O' THEN
		// w_bas := (cum_basc / nbm_per) * p;
		// ELSE
		// w_bas := cum_basc;
		// END IF;
		// ELSE
		// w_bas := (cum_basc / nbm_per) * p;
		// END IF;
		if ('O' == departDefinitif)
		{
			if ("O".equals(rubrique.getEddf()))
			{
				if (nbrePeriod != 0)
					this.fictivesalary.getValeurRubriquePartage().setBase((fictivesalary.getParamCumul().getCumulBaseCalc() / nbrePeriod) * this.fictivesalary.getOFictifPeriod().getP());
			}
			else
				this.fictivesalary.getValeurRubriquePartage().setBase(this.fictivesalary.getParamCumul().getCumulBaseCalc());
		}
		else
		{
			if (nbrePeriod != 0)
			{
				if(StringUtils.equals("3260", this.rubrique.getComp_id().getCrub()))
				{
					ParameterUtil.println("Nombre de période = "+nbrePeriod);
					ParameterUtil.println("Nombre total de période de regul = "+this.fictivesalary.getOFictifPeriod().getP());
					ParameterUtil.println("Nouvelle base calcul = ("+this.fictivesalary.getParamCumul().getCumulBaseCalc()+"/"+nbrePeriod+")*"+this.fictivesalary.getOFictifPeriod().getP()+" = "+(this.fictivesalary.getParamCumul().getCumulBaseCalc() / nbrePeriod) * this.fictivesalary.getOFictifPeriod().getP());
				}
				this.fictivesalary.getValeurRubriquePartage().setBase((this.fictivesalary.getParamCumul().getCumulBaseCalc() / nbrePeriod) * this.fictivesalary.getOFictifPeriod().getP());
				
			}
		}

		//
		// PA_ALGO.calc_algo(w_bas,w_basp,w_mon,w_tau,w_argu,w_aamm,wsd_fcal1.nbul,Pb_Calcul);
		// IF pb_calcul THEN
		// RETURN FALSE;
		// END IF;
		if ('O' == fictivesalary.param.getGenfile())
			fictivesalary.outputtext += "\n--In Rubrique Clone : Base obtenu= "+this.fictivesalary.getValeurRubriquePartage().getBase()+" appel de l'algo de traitement ";
		if(StringUtils.equals("3260", this.rubrique.getComp_id().getCrub()))
			ParameterUtil.println(fictivesalary.param.getMonthOfPay()+" Info 2 Base calcul Rubrique "+this.getRubrique().getComp_id().getCrub()+" = "+this.fictivesalary.getValeurRubriquePartage().getBase());
		boolean algoResult = this.applyAlgorithm();
		if (!algoResult)
			return false;
		//
		// ---------------------------------------------------------------------------------
		// -- Extrapolation du montant des cotisations du mois traite
		// ---------------------------------------------------------------------------------
		//
		// IF w_depdef = 'O' THEN
		// IF t_rub.eddf = 'O' THEN
		// mon_regu := (w_mon * nbm_per) / p;
		// ELSE
		// mon_regu := w_mon;
		// END IF;
		// ELSE
		// mon_regu := (w_mon * nbm_per) / p;
		// END IF;
		double montantRegu = 0;
		if ('O' == departDefinitif)
		{
			if ("O".equals(rubrique.getEddf()))
				montantRegu = (this.fictivesalary.getValeurRubriquePartage().getAmount() * nbrePeriod) / this.fictivesalary.getOFictifPeriod().getP();
			else
				montantRegu = this.fictivesalary.getValeurRubriquePartage().getAmount();
		}
		else
			montantRegu = (this.fictivesalary.getValeurRubriquePartage().getAmount() * nbrePeriod) / this.fictivesalary.getOFictifPeriod().getP();

		//
		// ---------------------------------------------------------------------------------
		// -- Calcul du montant de la regularisation
		// ---------------------------------------------------------------------------------
		// w_mon := mon_regu - cum_coti - cum_regu;
		// w_bas := cum_basc;
		// w_basp := cum_basc;
		
		ParameterUtil.println("Montant de la regularisation  ="+montantRegu);
		ParameterUtil.println("Montant de la cotisation  ="+this.fictivesalary.getParamCumul().getCumulCoti() );
		ParameterUtil.println("Montant du cumul de regularisation  ="+this.fictivesalary.getParamCumul().getCumulRegu());
		this.fictivesalary.getValeurRubriquePartage().setAmount(montantRegu - this.fictivesalary.getParamCumul().getCumulCoti() - this.fictivesalary.getParamCumul().getCumulRegu());
		ParameterUtil.println("Montant final de la rubrique  ="+montantRegu+" - "+this.fictivesalary.getParamCumul().getCumulCoti()+" - "+this.fictivesalary.getParamCumul().getCumulRegu()+" = "+this.fictivesalary.getValeurRubriquePartage().getAmount());
		this.fictivesalary.getValeurRubriquePartage().setBase(this.fictivesalary.getParamCumul().getCumulBaseCalc());
		this.fictivesalary.getValeurRubriquePartage().setBasePlafonnee(this.fictivesalary.getParamCumul().getCumulBaseCalc());
		//
		// ---------------------------------------------------------------------------------
		// -- Comparaison du montant de la regularisation avec le montant
		// -- minimal de regularisation (parametre REGMINI table 99)
		// ---------------------------------------------------------------------------------
		//
		// -- ----- Arrondi du resultat de la regul avant test avec REGMINI
		// IF NOT PA_PAIE.NouB(t_rub.arro) THEN
		// arrondi(w_mon);
		// IF pb_calcul THEN
		// RETURN FALSE;
		// END IF;
		// END IF;
		double mnt = 0;
		if (!ClsObjectUtil.isNull(rubrique.getArro()))
		{
			mnt = this.calculateArrondi(fictivesalary.getParam(), this.fictivesalary.getValeurRubriquePartage().getAmount());
			this.fictivesalary.getValeurRubriquePartage().setAmount(mnt);
		}
		// mon_regu := w_mon;
		//
		// IF mon_regu < 0 THEN
		// mon_regu := mon_regu * -1;
		// END IF;
		montantRegu = Math.abs(this.fictivesalary.getValeurRubriquePartage().getAmount());
		//
		// IF mon_regu < reg_mini THEN
		// w_bas := 0;
		// w_tau := 0;
		// w_mon := 0;
		// w_basp := 0;
		// END IF;
		if (montantRegu < fictivesalary.param.getRegularisationMinimale())
		{
			this.fictivesalary.getValeurRubriquePartage().setAmount(0);
			this.fictivesalary.getValeurRubriquePartage().setBase(0);
			this.fictivesalary.getValeurRubriquePartage().setBasePlafonnee(0);
			this.fictivesalary.getValeurRubriquePartage().setRates(0);
		}
		//this.fictivesalary.getOFictifPeriod().setP(nbrePeriod);
		//
		// RETURN TRUE;
		return true;
	}

	/**
	 * Cumuls des bases de cotisation pour la periode concernee => cumul_bas
	 */
	public void calculateCumulOfBaseForPeriod()
	{
		if ('O' == fictivesalary.param.getGenfile())
			fictivesalary.outputtext += "\n--In Rubrique Clone : >>calculateCumulOfBaseForPeriod";
//		fin_periode   NUMBER(6,0);
//		  cum99         NUMBER(6,0);
//		  aamm_cum      NUMBER(6,0);
//		  c_fin_periode CHAR(6);
//		  c_cum99       CHAR(6);
//		  c_aamm_cum    CHAR(6);
//		  l_bas         pafic.basc%TYPE;
//		  l_mont        pafic.mont%TYPE;
//
//		BEGIN
//		   w_mois_cum := 0;
//		   cum_coti   := 0;
//		   cum_regu   := 0;
//		   cum_basc   := 0;
//		   w_am       := d_per;
//		   aamm_cum   := w_am; 
//
//		   -- cum99 = Mois de totalisation de la rubrique dans les cumuls
//		   cum99 := aamm_cum / 100;
//		   cum99 := cum99 * 100 + 99;
//
//		   IF w_aamm < f_per THEN
//		     fin_periode := w_aamm;
//		   ELSE
//		     fin_periode := f_per;
//		   END IF;
		
		int fin_periode = 0;
		double cum_coti = 0;
		double cum_regu = 0;
		double cum_basc = 0;
		this.fictivesalary.getOFictifPeriod().setAammExercice(this.fictivesalary.getOFictifPeriod().getDebutPeriode());
		//aamm_cum
		int yearAndMonthCumul = fictivesalary.getOFictifPeriod().getAammExercice();
		int cum99 = yearAndMonthCumul / 100;
		cum99 = cum99 * 100 + 99;
		if (fictivesalary.param.getMyMonthOfPay().getYearAndMonthInt() < this.fictivesalary.getOFictifPeriod().getFinPeriode())
			fin_periode = fictivesalary.param.getMyMonthOfPay().getYearAndMonthInt();
		else
			fin_periode = this.fictivesalary.getOFictifPeriod().getFinPeriode();
		
//		
//		 -- c_aamm_cum    = Debut de lecture sur les cumuls
//		   -- c_fin_periode = Fin   de lecture sur les cumuls
//		   -- c_cum99       = Mois  de totalisation dans les cumuls
//		   c_aamm_cum    := LTRIM(TO_CHAR(aamm_cum,'099999'));
//		   c_fin_periode := LTRIM(TO_CHAR(fin_periode,'099999'));
//		   c_cum99       := LTRIM(TO_CHAR(cum99,'099999'));
		String c_aamm_cum = ClsStringUtil.formatNumber(yearAndMonthCumul, "000000");
		String c_fin_periode = ClsStringUtil.formatNumber(fin_periode, "000000");
		String c_cum99 = ClsStringUtil.formatNumber(cum99, "000000");
		
//		 -- Lecture dans les cumuls de la base de cotisation et du montant deja cotise
//		   SELECT SUM(basc), SUM(mont)
//		     INTO cum_basc, cum_coti
//		     FROM pacumu
//		    WHERE cdos = wpdos.cdos
//		      AND nmat = wsal01.nmat
//		      AND aamm >= c_aamm_cum
//		      AND aamm <= c_fin_periode
//		      AND aamm != c_cum99
//		      AND rubq = t_rub.rcon
//		      AND nbul != 0;
//		IF PA_PAIE.NouZ(cum_basc) THEN
//	      cum_basc := 0;
//	   END IF;
//	   IF PA_PAIE.NouZ(cum_coti) THEN
//	     cum_coti := 0;
//	   END IF;
		double[] bascAndMont = fictivesalary.utilNomenclatureFictif.getSumOfBascAndMontBorneSupInclus(fictivesalary.param.dossier, fictivesalary.infoSalary.getComp_id().getNmat(), c_aamm_cum, c_fin_periode, c_cum99, rubrique.getRcon(), null, "CumulPaie");
		cum_basc = bascAndMont[0];
		cum_coti = bascAndMont[1];
		ParameterUtil.println("----------------------->Rubrique "+rubrique.getComp_id().getCrub()+" Initialement, dans cumul, Cotisation  : "+cum_coti);
		
		
//		 -- Lecture dans les cumuls du montant deja regule
//		   SELECT SUM(mont) INTO cum_regu FROM pacumu
//		    WHERE cdos = wpdos.cdos
//		      AND nmat = wsal01.nmat
//		      AND aamm >= c_aamm_cum
//		      AND aamm <= c_fin_periode
//		      AND aamm != c_cum99
//		      AND rubq = t_rub.crub
//		      AND nbul != 0;
//
//		   IF PA_PAIE.NouZ(cum_regu) THEN
//		     cum_regu := 0;
//		   END IF;
		bascAndMont = fictivesalary.utilNomenclatureFictif.getSumOfBascAndMontBorneSupInclus(fictivesalary.param.dossier, fictivesalary.infoSalary.getComp_id().getNmat(), c_aamm_cum, c_fin_periode, c_cum99, rubrique.getComp_id().getCrub(), null, "CumulPaie");
		cum_regu = bascAndMont[1];
		ParameterUtil.println("----------------------->Rubrique "+rubrique.getComp_id().getCrub()+" Initialement, dans cumul, Regul  : "+cum_regu);
		
//		 -- Lecture dans le bulletin en cours de la base de cotisation et du montant deja cotise
//		   --    ex: PA_CALFIC.c_mont  := PA_CALFIC.t9999_mont(i);
//		   W_Faitout := PA_ALGFIC.char_mont(t_rub.rcon);
//
//		   --IF NOT PA_PAIE.NouZ(c_mont) THEN
//		      cum_basc := cum_basc + cc_mont;
//		      cum_coti := cum_coti + c_mont;
//		   --END IF;
		ClsFictifRubriqueClone autreRub = fictivesalary.findRubriqueCloneFictif(rubrique.getRcon());
		ParameterUtil.println("----------------------->Rubrique "+rubrique.getComp_id().getCrub()+" Valeurs  : "+autreRub.localToString());
		cum_basc += autreRub.getBase();
		cum_coti += autreRub.getAmount();
		ParameterUtil.println("----------------------->Rubrique "+rubrique.getComp_id().getCrub()+" Ensuite, provenant de fictif en cours, coti  : "+cum_coti);
		
		int periodRegu = 0;
		
		
		
//		 -- Recherche dans pacalc si wchg = TRUE
//		   -- Lecture de la base de cotisation, du montant cotise et du montant regule
//		   IF wchg THEN
//		      SELECT SUM(basc), SUM(mont)
//		        INTO l_bas, l_mont
//		        FROM pacalc
//		       WHERE cdos = wpdos.cdos
//		         AND aamm = memoprec
//		         AND nmat = wsal01.nmat
//		         AND nbul != 0
//		         AND rubq = t_rub.rcon;
//
//		      IF l_bas IS NULL THEN
//		         l_bas := 0;
//		      END IF;
//
//		      IF l_mont IS NULL THEN
//		         l_mont := 0;
//		      END IF;
//
//		      cum_basc := cum_basc + l_bas;
//		      cum_coti := cum_coti + l_mont;
//
//		      SELECT SUM(mont)
//		        INTO l_mont
//		        FROM pacalc
//		       WHERE cdos = wpdos.cdos
//		         AND aamm = memoprec
//		         AND nmat = wsal01.nmat
//		         AND nbul != 0
//		         AND rubq = t_rub.crub;
//
//		      IF l_mont IS NULL THEN
//		         l_mont := 0;
//		      END IF;
//
//		      cum_regu := cum_regu + l_mont;
//
//		   END IF;
		
		
	
//		 -- LH 241197 La lecture dans le fictif ne doit se faire que sur la periode d'imposition
//		   -- Lecture dans le fictif de la base de cotisation et du montant deja cotise
//		   SELECT SUM(basc), SUM(mont)
//		     INTO l_bas, l_mont
//		     FROM pafic
//		    WHERE cdos = wpdos.cdos
//		      AND aamm >= c_aamm_cum
//		      AND aamm < w_aamm
//		      --AND aamm <= c_fin_periode
//		      AND aamm != c_cum99
//		      AND nmat = wsal01.nmat
//		      AND nbul != 0
//		      AND rubq = t_rub.rcon;
		
//		 IF l_bas IS NULL THEN
//	      l_bas := 0;
//	   END IF;
//
//	   IF l_mont IS NULL THEN
//	      l_mont := 0;
//	   END IF;
		
		bascAndMont = fictivesalary.utilNomenclatureFictif.getSumOfBascAndMontBorneSupNonInclus(fictivesalary.param.dossier, fictivesalary.infoSalary.getComp_id().getNmat(), c_aamm_cum, fictivesalary.param.getMonthOfPay(), c_cum99, rubrique.getRcon(), null,"CongeFictif");
		double l_bas = bascAndMont[0];
		double l_mont = bascAndMont[1];
		
		ParameterUtil.println("----------------------->Rubrique "+rubrique.getComp_id().getCrub()+" Valeur é ajouter é la cotisation deja payé "+l_mont);
		ParameterUtil.println("----------------------->Rubrique "+rubrique.getComp_id().getCrub()+" Valeur é ajouter é la base deja cotitsé "+l_bas);
		
//		cum_basc := cum_basc + l_bas;
//		   cum_coti := cum_coti + l_mont;
		cum_basc +=  l_bas;
		cum_coti += l_mont;
		
//		-- LH 241197 La lecture dans le fictif ne soit se faire que sur la periode d'imposition
//		   -- Lecture dans le fictif du montant deja regule
//		   SELECT SUM(mont)
//		     INTO l_mont
//		     FROM pafic
//		    WHERE cdos = wpdos.cdos
//		      AND aamm >= c_aamm_cum
//		      AND aamm < w_aamm
//		      --AND aamm <= c_fin_periode
//		      AND aamm != c_cum99
//		      AND nmat = wsal01.nmat
//		      AND nbul != 0
//		      AND rubq = t_rub.crub;
//
//		   IF l_mont IS NULL THEN
//		      l_mont := 0;
//		   END IF;
		
		bascAndMont = fictivesalary.utilNomenclatureFictif.getSumOfBascAndMontBorneSupNonInclus(fictivesalary.param.dossier, fictivesalary.infoSalary.getComp_id().getNmat(), c_aamm_cum, fictivesalary.param.getMonthOfPay(), c_cum99, rubrique.getComp_id().getCrub(), null,"CongeFictif");
		l_mont = bascAndMont[1];
		ParameterUtil.println("----------------------->Rubrique "+rubrique.getComp_id().getCrub()+" Valeur é ajouter é la base deja cotitsé "+l_bas);
		
//		 cum_regu := cum_regu + l_mont;
		 cum_regu += l_mont;
		 
//		 -- Si reg_cas='T', cad si calcul des regul uniquement sur les mois travailles, on compte
//		   --    ce nombre de mois en comptant les BRUTS existants
//		   IF reg_cas = 'T' THEN
//		      SELECT count(*) INTO per_regu FROM pacumu
//		       WHERE cdos = wpdos.cdos
//		         AND nmat = wsal01.nmat
//		         AND aamm >= c_aamm_cum
//		         AND aamm <  c_fin_periode
//		         AND aamm != c_cum99
//		         AND rubq = w_rubbrut
//		         AND nbul = 9;
//		      IF PA_PAIE.NouZ(per_regu) THEN
//		         per_regu := 0;
//		      END IF;
//		   END IF;
		 
		 if(StringUtils.equals("T", fictivesalary.param.regularisationCas))
		 {
			 periodRegu = fictivesalary.utilNomenclatureFictif.getCountBorneSupNonInclus(fictivesalary.param.dossier, fictivesalary.infoSalary.getComp_id().getNmat(), c_aamm_cum, c_fin_periode, c_cum99, fictivesalary.param.brutRubrique, 9,"CumulPaie");
		 }
		
			this.fictivesalary.getParamCumul().setCumulBaseCalc(cum_basc);
			this.fictivesalary.getParamCumul().setCumulCoti(cum_coti);
			this.fictivesalary.getParamCumul().setCumulRegu(cum_regu);
			this.fictivesalary.getParamCumul().setNbreMoisCumul(periodRegu);
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "\n--In Rubrique Clone : ................cumulBaseCalc:" + cum_basc;
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "\n--In Rubrique Clone : ................cumulCoti:" + cum_coti;
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "\n--In Rubrique Clone : ................cumulRegu:" + cum_regu;
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "\n--In Rubrique Clone : ................periodRegu:" + periodRegu;
	}

	/**
	 * => cal_pc Calcul du pourcentage de reglement d'une absence
	 * 
	 */
	public void calculatePourcentage()
	{
		if (fictivesalary.param.getHTauxAppliedToRubriqueAbsence() != null)
		{
			Object obj = fictivesalary.param.getHTauxAppliedToRubriqueAbsence().get(rubrique.getComp_id().getCrub());
			if (obj != null)
			{
				if(ClsObjectUtil.getDoubleFromObject(obj) == 100)
					return;
				this.fictivesalary.setApplyTaux(true);
				this.fictivesalary.getValeurRubriquePartage().setAmount(this.fictivesalary.getValeurRubriquePartage().getAmount() * ClsObjectUtil.getDoubleFromObject(obj) / 100);
			}
		}
	}

	/**
	 * Calcul du prorata jour ou heure (standard) => prorat_std
	 * 
	 */
	public void calculateProrataStandard(String dateFormat)
	{
		int nombreJourMois = 0;
		double nombreJours = 0;
		double amount = 0;
		if ("O".equals(this.fictivesalary.param.getBase30Rubrique()))
		{
			if (this.fictivesalary.param.getBase30NombreJour() > 0)
			{
				nombreJourMois = this.fictivesalary.param.getBase30NombreJour();
			}
			else
				nombreJourMois = 30;
		}
		else
			nombreJourMois = fictivesalary.param.getMyMonthOfPay().getMaxDayOfMonth();

		nombreJours = this.fictivesalary.workTimeFictif.getProrataNbreJourTravaillees() + this.fictivesalary.workTimeFictif.getNbreJoursSupplementaires();

		if (!"HP".equals(this.fictivesalary.getInfoSalary().getCods()) && !"HC".equals(this.fictivesalary.getInfoSalary().getCods()))
		{
			if (this.fictivesalary.workTimeFictif.getNbreJoursAbsencePourCongeAnnuel() != 0 || this.fictivesalary.workTimeFictif.getNbreJourAbsence() != 0
					|| this.fictivesalary.workTimeFictif.getNbreJourAbsencePourCongesPonctuels() != 0)
			{
				nombreJours = nombreJourMois + this.fictivesalary.workTimeFictif.getNbreJoursSupplementaires();

				if ("N".equals(this.rubrique.getPpcg()))
				{
					nombreJours = nombreJours - this.fictivesalary.workTimeFictif.getNbreJoursAbsencePourCongeAnnuel() - this.fictivesalary.workTimeFictif.getNbreJourAbsencePourCongesPonctuels();
				}

				if ("O".equals(this.rubrique.getPrac()))
				{
					nombreJours -= this.fictivesalary.workTimeFictif.getNbreJourAbsence();
				}
			}

			long nombreDeJourDeduit = 0;
			ClsDate oDate = new ClsDate(this.fictivesalary.getInfoSalary().getDtes());

			if (this.fictivesalary.isEmbauche())
			{
				if (oDate.getDate().compareTo(this.fictivesalary.param.getFirstDayOfMonth()) > 0)
				{
					nombreDeJourDeduit = this.fictivesalary.utilNomenclatureFictif.compteJours(this.fictivesalary.param.getDossier(), ClsEnumeration.EnTypeOfDay.A, this.fictivesalary.param.getFirstDayOfMonth(), oDate
							.addDay(-1), dateFormat);
				}
			}
			
			//Cas d'une échéance de contrat
			if(fictivesalary.param.priseEnCompteDateFinContrat && fictivesalary.infoSalary.getDecc() != null && 
					(ClsDate.getDateS(fictivesalary.infoSalary.getDecc(), ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).compareTo(fictivesalary.param.getMyMonthOfPay().getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM)) == 0))
			{
				oDate = new ClsDate(this.fictivesalary.getInfoSalary().getDecc());
				nombreDeJourDeduit += this.fictivesalary.utilNomenclatureFictif.compteJours(this.fictivesalary.getParam().getDossier(), ClsEnumeration.EnTypeOfDay.A, oDate.addDay(1),
						this.fictivesalary.getParam().getLastDayOfMonth(), dateFormat);
			}

			Date dtMrr = this.fictivesalary.getInfoSalary().getDmrr();
			if (dtMrr != null)
			{
				oDate.setDate(dtMrr);

				if (this.fictivesalary.param.isStc())
				{
					if (oDate.getDate().compareTo(this.fictivesalary.param.getLastDayOfMonth()) < 0)
					{
						nombreDeJourDeduit += this.fictivesalary.utilNomenclatureFictif.compteJours(this.fictivesalary.param.getDossier(), ClsEnumeration.EnTypeOfDay.A, oDate.addDay(1), this.fictivesalary.param
								.getLastDayOfMonth(), dateFormat);
					}
				}
			}

			nombreJours -= nombreDeJourDeduit;
			if (nombreJours < 0)
				nombreJours = 0;

			amount = this.fictivesalary.getValeurRubriquePartage().getAmount() * nombreJours / this.fictivesalary.workTimeFictif.getProrataNbreJourTravaillees();
			this.fictivesalary.workTimeFictif.setInter(nombreJours * this.fictivesalary.getValeurRubriquePartage().getAmount());
			this.fictivesalary.getValeurRubriquePartage().setMonhs(amount);
			this.fictivesalary.getValeurRubriquePartage().setAmount(amount);

			amount = this.fictivesalary.getValeurRubriquePartage().getRates();
			if (amount == 0)
			{
				this.fictivesalary.getValeurRubriquePartage().setRates(nombreJours);
			}
		}
		else
		{
			if ("O".equals(this.rubrique.getPrhr()))
			{
				amount = calculateProrataJour();
				if (!ClsObjectUtil.isNull(this.rubrique.getPrtb()))
				{
					this.fictivesalary.workTimeFictif.setProrataNbreHeuresPayees(amount);
					amount = this.fictivesalary.getValeurRubriquePartage().getAmount() * this.fictivesalary.workTimeFictif.getProrataNbreHeureTravail() / this.fictivesalary.workTimeFictif.getProrataNbreHeuresPayees();
					this.fictivesalary.getValeurRubriquePartage().setAmount(amount);
				}
				else
				{
					amount = this.fictivesalary.getValeurRubriquePartage().getAmount() * this.fictivesalary.workTimeFictif.getProrataNbreHeureTravail() / this.fictivesalary.workTimeFictif.getProrataNbreHeures();
					this.fictivesalary.getValeurRubriquePartage().setAmount(amount);
				}

				double prorataHeures = 0;
				this.fictivesalary.getValeurRubriquePartage().setMonhs(this.fictivesalary.getValeurRubriquePartage().getAmount());
				if (this.fictivesalary.getValeurRubriquePartage().getRates() == 0)
				{
					prorataHeures = this.fictivesalary.workTimeFictif.getProrataNbreHeureTravail();
					this.fictivesalary.getValeurRubriquePartage().setRates(prorataHeures);
				}
			}
			else
			{
				if ("O".equals(this.rubrique.getPrac()))
				{
					amount = this.fictivesalary.getValeurRubriquePartage().getAmount() * this.fictivesalary.workTimeFictif.getProrataNbreJourTravaillees() / nombreJourMois;
					this.fictivesalary.getValeurRubriquePartage().setAmount(amount);
					this.fictivesalary.getValeurRubriquePartage().setMonhs(amount);
					if (this.fictivesalary.getValeurRubriquePartage().getRates() == 0)
					{
						this.fictivesalary.getValeurRubriquePartage().setRates(nombreJours);
					}
				}
			}
		}
	}

	/**
	 * => prorat_spc Calcul du prorata jour ou heure (specifique RCI)
	 * 
	 */
	public void calculateProrataSpecifiqueRCI()
	{

		double nombreDeJour = 0;
		double amount = 0;

		nombreDeJour = this.fictivesalary.workTimeFictif.getProrataNbreJourTravaillees() + this.fictivesalary.workTimeFictif.getNbreJoursSupplementaires();

		if (!"HP".equals(this.fictivesalary.getInfoSalary().getCods()) && !"HC".equals(this.fictivesalary.getInfoSalary().getCods()))
		{
			if (this.fictivesalary.workTimeFictif.getNbreJoursAbsencePourCongeAnnuel() != 0 || this.fictivesalary.workTimeFictif.getNbreJourAbsence() != 0
					|| this.fictivesalary.workTimeFictif.getNbreJourAbsencePourCongesPonctuels() != 0)
			{

				if ("N".equals(this.rubrique.getPpcg()))
				{
					nombreDeJour = nombreDeJour - this.fictivesalary.workTimeFictif.getNbreJoursAbsencePourCongeAnnuel() - this.fictivesalary.workTimeFictif.getNbreJourAbsencePourCongesPonctuels();
				}

				if ("O".equals(this.rubrique.getPrac()))
				{
					nombreDeJour -= this.fictivesalary.workTimeFictif.getNbreJourAbsence();
				}

				if ("O".equals(this.fictivesalary.param.getBase30Rubrique()))
				{
					if (this.fictivesalary.param.getBase30NombreJour() > 0)
					{
						nombreDeJour = this.fictivesalary.param.getBase30NombreJour();
					}
					else
						nombreDeJour = 30;
				}
				else
					nombreDeJour = this.fictivesalary.getMyMonthOfPay().getMaxDayOfMonth();

				nombreDeJour += this.fictivesalary.workTimeFictif.getNbreJoursSupplementaires();
				if ("N".equals(this.rubrique.getPpcg()))
				{
					nombreDeJour = nombreDeJour - this.fictivesalary.workTimeFictif.getNbreJoursAbsencePourCongeAnnuel() - this.fictivesalary.workTimeFictif.getNbreJourAbsencePourCongesPonctuels();
				}
				if ("O".equals(this.rubrique.getPrac()))
				{
					nombreDeJour -= this.fictivesalary.workTimeFictif.getNbreJourAbsence();
				}
			}
			amount = nombreDeJour * this.fictivesalary.getValeurRubriquePartage().getAmount();
			this.fictivesalary.workTimeFictif.setInter(amount);
			amount = nombreDeJour * this.fictivesalary.getValeurRubriquePartage().getAmount() / this.fictivesalary.workTimeFictif.getProrataNbreJourTravaillees();
			this.fictivesalary.getValeurRubriquePartage().setAmount(amount);
			this.fictivesalary.getValeurRubriquePartage().setMonhs(amount);
			//
			amount = this.fictivesalary.getValeurRubriquePartage().getRates();
			if (amount == 0)
				this.fictivesalary.getValeurRubriquePartage().setRates(nombreDeJour);
		}
		else
		{
			if ("O".equals(this.rubrique.getPrhr()))
			{
				amount = this.fictivesalary.workTimeFictif.getProrataNbreHeureTravail() * this.fictivesalary.getValeurRubriquePartage().getAmount();
				this.fictivesalary.getValeurRubriquePartage().setAmount(amount);
				this.fictivesalary.getValeurRubriquePartage().setMonhs(amount);

				amount = this.fictivesalary.getValeurRubriquePartage().getRates();
				if (amount == 0)
				{
					this.fictivesalary.getValeurRubriquePartage().setRates(nombreDeJour);
				}
			}
		}
	}

	/**
	 * => prorat_fic Calcul d'un prorata pour le dernier mois de conges avec fictif = 'O'. Cas des rubriques non proratees qui doivent l'etre sur le dernier
	 * mois. Uniquement si FICTIF est actif. Cas des rubriques qui ne sont pas soumise a prorata (parubq.pror = 'N') mais dont le parametre 'calcul dernier
	 * bulletin fictif' vaut 'prorata' :
	 * <p>* Lors du calcul fictif elles sont payees au prorata du nombre de jours de conges
	 * </p>
	 * <p>* Lors du calcul normal elles sont payees au prorata du nombre de jours de presence
	 * </p>
	 */
	public void calculateProrataFictif()
	{
		double nombreDeJour = 0;
		int nombreJourDuMois = 0;
		double amount = 0;
	
		if ("O".equals(this.fictivesalary.param.getBase30Rubrique()))
		{
			if (this.fictivesalary.param.getBase30NombreJour() > 0)
			{
				nombreJourDuMois = this.fictivesalary.param.getBase30NombreJour();
			}
			else
				nombreJourDuMois = 30;
		}
		else
			nombreJourDuMois = this.fictivesalary.getMyMonthOfPay().getMaxDayOfMonth();
		nombreDeJour = this.fictivesalary.workTimeFictif.getProrataNbreJourTravaillees() + this.fictivesalary.workTimeFictif.getNbreJoursSupplementaires();
		
		if (!"HP".equals(this.fictivesalary.getInfoSalary().getCods()) && !"HC".equals(this.fictivesalary.getInfoSalary().getCods()))
		{

			if (this.fictivesalary.workTimeFictif.getNbreJoursAbsencePourCongeAnnuel() != 0 || this.fictivesalary.workTimeFictif.getNbreJourAbsence() != 0)
				nombreDeJour = nombreJourDuMois + this.fictivesalary.workTimeFictif.getNbreJoursSupplementaires() - this.fictivesalary.workTimeFictif.getNbreJourAbsence();

			amount = nombreDeJour * this.fictivesalary.getValeurRubriquePartage().getAmount() / this.fictivesalary.workTimeFictif.getProrataNbreJourTravaillees();
			this.fictivesalary.getValeurRubriquePartage().setAmount(amount);
			this.fictivesalary.getValeurRubriquePartage().setMonhs(amount);
			this.fictivesalary.getValeurRubriquePartage().setInter(nombreDeJour * this.fictivesalary.getValeurRubriquePartage().getAmount());

			amount = this.fictivesalary.getValeurRubriquePartage().getRates();
			if (amount == 0)
			{
				this.fictivesalary.getValeurRubriquePartage().setRates(nombreDeJour);
			}
		}
		else
		{
			if (this.fictivesalary.workTimeFictif.getNbreJoursAbsencePourCongeAnnuel() != 0 || this.fictivesalary.workTimeFictif.getNbreJourAbsence() != 0)
				nombreDeJour = nombreJourDuMois + this.fictivesalary.workTimeFictif.getNbreJoursSupplementaires() - this.fictivesalary.workTimeFictif.getNbreJourAbsence();
			
			amount = nombreDeJour * this.fictivesalary.getValeurRubriquePartage().getAmount() / nombreJourDuMois;
			this.fictivesalary.getValeurRubriquePartage().setAmount(amount);
			this.fictivesalary.getValeurRubriquePartage().setMonhs(amount);

			amount = this.fictivesalary.getValeurRubriquePartage().getRates();
			if (amount == 0)
			{
				this.fictivesalary.getValeurRubriquePartage().setRates(nombreDeJour);
			}
		}
	}

	/**
	 * => prorat_jour Calcul du prorata jour si ne s'appuie pas sur T.91
	 * 
	 */
	public int calculateProrataJour()
	{
		String res = "";
		Number value = null;
		ClsEnumeration.EnColumnToRead col = ClsEnumeration.EnColumnToRead.AMOUNT;
		if (!"R".equals(this.rubrique.getPrcl()))
		{
			res = this.fictivesalary.concPro(this.rubrique.getPrcl());
			if ("T".equals(this.rubrique.getPrtm()))
				col = ClsEnumeration.EnColumnToRead.RATES;
			value = this.fictivesalary.utilNomenclatureFictif.getAmountOrRateFromNomenclature(this.fictivesalary.param.getDossier(),
					Integer.valueOf(this.rubrique.getPrtb() == null ? "0" : this.rubrique.getPrtb()), res, this.rubrique.getPrno() == null ? new Long(0).intValue() : this.rubrique.getPrno()
							.intValue(), this.fictivesalary.param.getNlot(), this.fictivesalary.param.getNumeroBulletin(), this.fictivesalary.param.getMonthOfPay(), col);
		}
		else
		{
			res = this.fictivesalary.concPro(this.rubrique.getPrcl());
			if ("T".equals(this.rubrique.getPrtm()))
				col = ClsEnumeration.EnColumnToRead.AMOUNT;
			value = this.fictivesalary.utilNomenclatureFictif.getAmountOrRateFromNomenclature(this.fictivesalary.param.getDossier(),
					Integer.valueOf(this.rubrique.getPrtb() == null ? "0" : this.rubrique.getPrtb()), this.rubrique.getComp_id().getCrub(), this.rubrique.getPrno(), this.fictivesalary.param.getNlot(),
					this.fictivesalary.param.getNumeroBulletin(), this.fictivesalary.param.getMonthOfPay(), col);
		}
		return (value == null ? 0 : value.intValue());
	}
	
	public String getValeurBareme(ElementSalaireBareme oElementSalaireBareme, boolean min)
	{
		String colonne = "val1";
		
		String valeur = StringUtils.EMPTY;
		
		if(min) colonne = "val1";
		if(!min) colonne = "val2";
		try
		{
			valeur = (String) MethodUtils.invokeMethod(oElementSalaireBareme, "get"+StringUtils.capitalize(colonne),null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return valeur;
	}
	
	public double getTauxOuMontantBareme(ElementSalaireBareme oElementSalaireBareme, boolean taux)
	{
		
		String colonne = "taux";
		
		
		
		
		if(taux) colonne = "taux";
		if(!taux) colonne = "mont";
		
		BigDecimal valeur = BigDecimal.ZERO;
		try
		{
			valeur = (BigDecimal) MethodUtils.invokeMethod(oElementSalaireBareme, "get"+StringUtils.capitalize(colonne),null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if(valeur != null)
			return valeur.doubleValue();
		
		return 0;
	}

	/**
	 * convertit une chaéne de 16 bytes en nombre
	 * 
	 * @param char16
	 *            la chaéne é convertir
	 * @return un double
	 */
	public double convertToNumber(String char16)
	{
				
		// if('O' == fictivesalary.param.getGenfile()) fictivesalary.outputtext+="\n--In Rubrique Clone : >>Char to convert = "+char16);
		// if('O' == fictivesalary.param.getGenfile()) fictivesalary.outputtext+="\n--In Rubrique Clone : >>convertToNumber");
		double w_nombre = 0;
		if (ClsObjectUtil.isNull(char16)/* || char16.length() < 16 */)
			return 0;

		if (StringUtils.isBlank(char16))
			return 0;
		//
		try
		{
			// IF SUBSTR(char16 ,1,1) NOT BETWEEN 'A' AND 'z' THEN
			// BEGIN
			// w_nombre := TO_NUMBER (char16);
			// EXCEPTION
			// WHEN INVALID_NUMBER THEN
			// w_nombre := 0;
			// END;
			// RETURN w_nombre;
			// END IF;
			//
			// IF SUBSTR(char16,1,1) = 'R' AND SUBSTR(char16,2,1) BETWEEN '0' AND '9' THEN
			// W_Retour := char_mont(SUBSTR(char16,2,4)) ;
			// w_nombre := PA_CALCUL.c_mont;
			// RETURN w_nombre;
			// END IF;
			Character ch = char16.toCharArray()[0];
			if (ch == ',')
				char16 = ("0" + char16).replace(',', '.');
			if (!Character.isLetter(ch))
			{
				return Double.parseDouble(char16.replaceAll(",", "."));
			}
			//
			if (char16.length() < 2)
				return 0;

			Character ch1 = char16.toCharArray()[0];
			Character ch2 = char16.toCharArray()[1];
			if (ch1 == 'R' && Character.isDigit(ch2))
			{
				String rubrique = StringUtils.substring(char16, 1, ParameterUtil.longueurRubrique+1);
				// for (ClsFictifRubriqueClone rub : this.fictivesalary.getListOfAllRubrique()) {
				// if(rubrique.equals(rub.getRubrique().getComp_id().getCrub()))
				// w_nombre = rub.getAmount();
				// }
				ClsFictifRubriqueClone rub = fictivesalary.findRubriqueCloneFictif(rubrique);
				if (rub != null)
				{
					w_nombre = rub.getAmount();
					// if('O' == fictivesalary.param.getGenfile()) fictivesalary.outputtext+="\n--In Rubrique Clone : rubrique :" + rubrique + " montant :" + w_nombre);
				}
				return w_nombre;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		//
		// w_nombre := cal_plaf(char16);
		w_nombre = calculatePlafond(char16);
		if (fictivesalary.param.isPbWithCalulation())
		{
			return 0;
		}
		return w_nombre;
	}

	/**
	 * =>recvalnum RECUPERATION VALEUR NUMERIQUE GSAL := f( indice table 30 )
	 * 
	 * @return la valeur récupérée
	 */
	public double recupValeurNumeriqueGSAL(String keys)
	{
		// if('O' == fictivesalary.param.getGenfile()) fictivesalary.outputtext+="\n--In Rubrique Clone : >>recupValeurNumeriqueGSAL");
		Integer key = NumberUtils.toInt(keys);
		double res = 0;
		String char30 = "";
		// IF a_cle1 = 23 THEN
		// char30 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.nbcj,'09'));
		if (23 == key)
			char30 = ClsStringUtil.formatNumber(fictivesalary.getInfoSalary().getNbcj(), "00");
		else if (142 == key)
			char30 = ClsStringUtil.formatNumber(fictivesalary.getInfoSalary().getNbef(),"00");
		// ELSIF a_cle1 = 24 THEN
		// char30 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.nbec,'09'));
		else if (24 == key)
			char30 = ClsStringUtil.formatNumber(fictivesalary.getInfoSalary().getNbec(), "00");
		// ELSIF a_cle1 = 25 THEN
		// char30 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.nbfe,'09'));
		else if (25 == key)
			char30 = ClsStringUtil.formatNumber(fictivesalary.getInfoSalary().getNbfe(), "00");
		// ELSIF a_cle1 = 26 THEN
		// char30 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.nbpt,'09D99'));
		else if (26 == key)
			char30 = ClsStringUtil.formatNumber(fictivesalary.getInfoSalary().getNbpt(), "00.00");
		// ELSIF a_cle1 = 47 THEN
		// char30 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.indi,'099'));
		else if (47 == key)
			char30 = ClsStringUtil.formatNumber(fictivesalary.getInfoSalary().getIndi(), "000");
		// ELSIF a_cle1 = 49 THEN
		// char30 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.tinp,'099D99'));
		else if (48 == key)
			char30 = ClsStringUtil.formatNumber(fictivesalary.getInfoSalary().getTinp(), "000.00");
		// ELSIF a_cle1 = 73 THEN
		// char30 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.npie,'09'));
		else if (73 == key)
			char30 = ClsStringUtil.formatNumber(fictivesalary.getInfoSalary().getNpie(), "00");
		// ELSIF a_cle1 = 83 THEN
		// char30 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.snet,'999999999999D999'));
		else if (83 == key)
			char30 = ClsStringUtil.formatNumber(fictivesalary.getInfoSalary().getSnet(), "#.000");
		// ELSIF a_cle1 = 104 THEN
		// char30 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.nbjtr,'099D99'));
		else if (104 == key)
			char30 = ClsStringUtil.formatNumber(fictivesalary.getInfoSalary().getNbjtr(), "000.00");
		// ELSIF a_cle1 = 200 THEN
		// char30 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.japa,'099D99'));
		else if (200 == key)
			char30 = ClsStringUtil.formatNumber(fictivesalary.getInfoSalary().getJapa(), "00.00");
		// ELSIF a_cle1 = 201 THEN
		// char30 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.japec,'099D99'));
		else if (201 == key)
			char30 = ClsStringUtil.formatNumber(fictivesalary.getInfoSalary().getJapec(), "00.00");
		// ELSIF a_cle1 = 202 THEN
		// char30 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.jded,'099D99'));
		else if (202 == key)
			char30 = ClsStringUtil.formatNumber(fictivesalary.getInfoSalary().getJded(), "00.00");
		// ELSIF a_cle1 = 203 THEN
		// char30 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.jrla,'099D99'));
		else if (203 == key)
			char30 = ClsStringUtil.formatNumber(fictivesalary.getInfoSalary().getJrla(), "000.00");
		// ELSIF a_cle1 = 204 THEN
		// char30 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.jrlec,'099D99'));
		else if (204 == key)
			char30 = ClsStringUtil.formatNumber(fictivesalary.getInfoSalary().getJrlec(), "000.00");
		// ELSIF a_cle1 = 205 THEN
		// char30 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.nbjsa,'099D99'));
		else if (205 == key)
			char30 = ClsStringUtil.formatNumber(fictivesalary.getInfoSalary().getNbjsa(), "000.00");
		// ELSIF a_cle1 = 206 THEN
		// char30 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.nbjse,'099D99'));
		else if (206 == key)
			char30 = ClsStringUtil.formatNumber(fictivesalary.getInfoSalary().getNbjse(), "000.00");
		// ELSIF a_cle1 = 207 THEN
		// char30 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.nbjsm,'099D99'));
		else if (207 == key)
			char30 = ClsStringUtil.formatNumber(fictivesalary.getInfoSalary().getNbjsm(), "000.00");
		// ELSIF a_cle1 = 208 THEN
		// char30 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.dapa,'999999999999D999'));
		else if (208 == key)
			char30 = ClsStringUtil.formatNumber(fictivesalary.getInfoSalary().getDapa(), "#.000");
		// ELSIF a_cle1 = 209 THEN
		// char30 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.dapec,'999999999999D999'));
		else if (209 == key)
			char30 = ClsStringUtil.formatNumber(fictivesalary.getInfoSalary().getDapec(), "#.000");
		// ELSIF a_cle1 = 210 THEN
		// char30 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.dded,'999999999999D999'));
		else if (210 == key)
			char30 = ClsStringUtil.formatNumber(fictivesalary.getInfoSalary().getDded(), "#.000");
		// ELSIF a_cle1 >= 801 AND a_cle1 <= 899 THEN
		// char30 := SUBSTR(RecZli(PA_CALCUL.wsal01.nmat,substr(To_char(a_cle1),2,2)),1,10);
		else if (300 == key)
			char30 = ClsStringUtil.formatNumber(fictivesalary.getAgeOfAgent(), "00");
		else if (301 == key)
			char30 = ClsStringUtil.formatNumber(fictivesalary.getAnciennete(), "00");
		else if (302 == key)
			char30 = ClsStringUtil.formatNumber(new ClsDate(fictivesalary.param.getMonthOfPay(),"yyyyMM").getMonth(),"00");
		else if (801 <= key && 899 > key)
		{
			String tmp = keys.substring(1, 3);
			String xx = fictivesalary.rechercheZoneLibre(Integer.valueOf(tmp));
			char30 = StringUtils.substring(xx, 0, 10);
		}
		// ELSE
		// PA_CALCUL.err_msg := PA_PAIE.erreurp('ERR-90082',PA_CALCUL.w_clang,PA_CALCUL.t_rub.crub,TO_CHAR(a_cle1,'099'),PA_CALCUL.wsal01.nmat);
		// char30 := ' ';
		// Pb_Calcul := TRUE;
		// RETURN char30;
		// END IF;
		else
		{
			// if('O' == fictivesalary.param.getGenfile()) fictivesalary.outputtext+="\n--In Rubrique Clone : ................char30 = " + char30);
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90082", fictivesalary.param.getLangue(), rubrique.getComp_id().getCrub(), fictivesalary.getInfoSalary().getComp_id().getNmat()));
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "\n--In Rubrique Clone : " + "\n" + fictivesalary.param.getError();
			fictivesalary.param.setPbWithCalulation(true);
			return char30 == null ? 0 : Double.valueOf(char30);
		}
		//
		// IF LENGTH(char30) > 16 THEN
		// PA_CALCUL.err_msg := PA_PAIE.erreurp('ERR-10525',PA_CALCUL.w_clang,PA_CALCUL.t_rub.crub,PA_CALCUL.wsal01.nmat);
		// char30 := ' ';
		// Pb_Calcul := TRUE;
		// RETURN char30;
		// END IF;
		if (char30.length() > 16)
		{
			// if('O' == fictivesalary.param.getGenfile()) fictivesalary.outputtext+="\n--In Rubrique Clone : ................char30 = " + char30);
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-10525", fictivesalary.param.getLangue(), rubrique.getComp_id().getCrub(), fictivesalary.getInfoSalary().getComp_id().getNmat()));
			if ('O' == fictivesalary.param.getGenfile())
				fictivesalary.outputtext += "\n--In Rubrique Clone : " + "\n" + fictivesalary.param.getError();
			fictivesalary.param.setPbWithCalulation(true);
			return char30 == null ? 0 : Double.valueOf(char30);
		}
		//
		// if('O' == fictivesalary.param.getGenfile()) fictivesalary.outputtext+="\n--In Rubrique Clone : ................char30 = " + char30);
		res = convertToNumber(char30);
		// if('O' == fictivesalary.param.getGenfile()) fictivesalary.outputtext+="\n--In Rubrique Clone : ................res = " + res);
		// w_retour := conv_num(char30);
		//
		return res;
	}

	/**
	 * => compar Comparaison du resultat des montants des rubriques. Permet de faire l'harmisation du montant de la rubrique et de la base plafonnée
	 */
	public void compareResult()
	{
		boolean valeurRub1Exist = false;
		double valeurRub1 = 0;
		double valeurRub2 = 0;
		String signe = "";
		double montantPartage = this.fictivesalary.getValeurRubriquePartage().getAmount();
		double valeurComparaison = this.convertToNumber(this.rubrique.getResl());

		if (montantPartage > valeurComparaison)
		{
			if (!ClsObjectUtil.isNull(this.rubrique.getSup1()))
			{
				valeurRub1Exist = true;
				valeurRub1 = this.convertToNumber(this.rubrique.getSup1());
				valeurRub2 = this.convertToNumber(this.rubrique.getSup2());
				signe = this.rubrique.getSups();
			}
		}
		else if (montantPartage < valeurComparaison)
		{
			if (!ClsObjectUtil.isNull(this.rubrique.getInf1()))
			{
				valeurRub1Exist = true;
				valeurRub1 = this.convertToNumber(this.rubrique.getInf1());
				valeurRub2 = this.convertToNumber(this.rubrique.getInf2());
				signe = this.rubrique.getInfs();
			}
		}
		else if (montantPartage == valeurComparaison)
		{
			if (!ClsObjectUtil.isNull(this.rubrique.getEgu1()))
			{
				valeurRub1Exist = true;
				valeurRub1 = this.convertToNumber(this.rubrique.getEgu1());
				valeurRub2 = this.convertToNumber(this.rubrique.getEgu2());
				signe = this.rubrique.getEgus();
			}
		}
		
		if ("+".equals(signe))
		{
			this.fictivesalary.getValeurRubriquePartage().setAmount(valeurRub1 + valeurRub2);
			this.fictivesalary.getValeurRubriquePartage().setBasePlafonnee(valeurRub1 + valeurRub2);
		}
		else if ("-".equals(signe))
		{
			this.fictivesalary.getValeurRubriquePartage().setAmount(valeurRub1 - valeurRub2);
			this.fictivesalary.getValeurRubriquePartage().setBasePlafonnee(valeurRub1 - valeurRub2);
		}
		else if ("*".equals(signe))
		{
			this.fictivesalary.getValeurRubriquePartage().setAmount(valeurRub1 * valeurRub2);
			this.fictivesalary.getValeurRubriquePartage().setBasePlafonnee(valeurRub1 * valeurRub2);
		}
		else if ("/".equals(signe))
		{
			if (valeurRub2 != 0)
			{
				this.fictivesalary.getValeurRubriquePartage().setAmount(valeurRub1 / valeurRub2);
				this.fictivesalary.getValeurRubriquePartage().setBasePlafonnee(valeurRub1 / valeurRub2);
			}
			else
			{
				this.fictivesalary.getValeurRubriquePartage().setAmount(0);
				this.fictivesalary.getValeurRubriquePartage().setBasePlafonnee(0);
			}
		}
		else if (ClsObjectUtil.isNull(signe) && valeurRub1Exist)
		{
			this.fictivesalary.getValeurRubriquePartage().setAmount(valeurRub1);
			this.fictivesalary.getValeurRubriquePartage().setBasePlafonnee(valeurRub1);
		}
	}

	/**
	 * construit un message d'erreur pour le mettre dans le param
	 * 
	 * @param codeError
	 */
	void setError(String codeError)
	{
		String error = fictivesalary.param.errorMessage(codeError, fictivesalary.param.getLangue(), this.getRubrique().getAlgo(), fictivesalary.getInfoSalary().getComp_id().getNmat(), this.getRubrique().getComp_id()
				.getCrub());
		fictivesalary.param.setError(error);
	}
	
	public String localToString()
	{
		return "Rubrique "+this.getRubrique().getComp_id().getCrub()+" ->Base = "+this.getBase()+" Taux = "+this.getRates()+" Montant ="+this.getAmount();
	}

}
