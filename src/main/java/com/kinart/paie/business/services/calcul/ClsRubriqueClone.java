package com.kinart.paie.business.services.calcul;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kinart.paie.business.model.CaisseMutuelleSalarie;
import com.kinart.paie.business.model.CalculPaie;
import com.kinart.paie.business.model.CumulPaie;
import com.kinart.paie.business.model.ElementSalaireBareme;
import com.kinart.paie.business.services.utils.*;
import org.apache.commons.lang3.StringUtils;

/**
 * Cette classe permet d'impl�menter un wrapper d'une rubrique et d'encapsuler les donn�es d'une rubrique: les �l�ments variables de la rubrique, l'exc�ution de
 * l'algorithme ad�quat, le calcul des diff�rentes bases et montant, etc. Toutes les op�rations li�es � la gestion d'une rubrique y sont impl�ment�es. Elle
 * contient le salari� concern�.
 * 
 * @author c.mbassi
 * 
 */
public class ClsRubriqueClone
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
	private ClsSalaryToProcess salary = null;

	// periode de r�gulation
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
	 * @param salary
	 */
	public ClsRubriqueClone(ClsSalaryToProcess salary)
	{
		this.setSalary(salary);
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

	public ClsSalaryToProcess getSalary()
	{
		return salary;
	}

	public void setSalary(ClsSalaryToProcess salary)
	{
		this.salary = salary;
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
	 * Appelle l'algorithme correspondant � cette rubrique
	 * 
	 * @return true ou false �tant le r�sultat que l'algorithme a renvoy�
	 */
	public boolean applyAlgorithm()
	{
		// outputtext +="\n"+">>applyAlgorithm");
		Boolean result = false;
		try
		{
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\nEn entr�e : base = " + salary.getValeurRubriquePartage().getBase());
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\nEn entr�e : taux = " + salary.getValeurRubriquePartage().getRates());
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\nEn entr�e : montant = " + salary.getValeurRubriquePartage().getAmount());
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\nEn entr�e : base plaf= " + salary.getValeurRubriquePartage().getBasePlafonnee());

			long algoToApply = this.rubrique.getAlgo();
			Method theAlgo = IAlgorithm.class.getMethod("algo" + String.valueOf(algoToApply), new Class[] { ClsRubriqueClone.class });
			//IAlgorithm algo = new ClsAlgorithm(this.getSalary());
			IAlgorithm algo = ClsChoixAlgorithme.choixAlgorithm(salary, salary.parameter.nomClient);
			result = (Boolean) theAlgo.invoke(algo, new Object[] { this });
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\nEn sortie : base = " + salary.getValeurRubriquePartage().getBase());
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\nEn sortie : taux = " + salary.getValeurRubriquePartage().getRates());
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\nEn sortie : montant = " + salary.getValeurRubriquePartage().getAmount());
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\nEn sortie : base plaf= " + salary.getValeurRubriquePartage().getBasePlafonnee());
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\n--Resultat de l'application de l'algo " + algoToApply + "\n");
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "------------------------------------------------\n");
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( algo.getOutputtext());
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\nresult = " + result + "\n------------------------------------------------\n");
			//
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
	 * => champ_app V�rifie que cette rubrique est applicable au salari�.
	 * 
	 * @param numeroAjustement
	 * @return true si la rubrique est applicable et false dans le cas contraire
	 */
	public boolean champApplicationSalarieToRubrique(int numeroAjustement)
	{
		// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : >>champApplicationSalarieToRubrique");
		// nbr_an NUMBER(5);
		// moi_cal NUMBER(5);
		// l_dtad DATE;
		// l_dtrd DATE;
		// l_amdt NUMBER(6,0);
		// l_exist_cm BOOLEAN;
		//
		// l_czli PARUBQ_ZONELIBRE.CZLI%TYPE;
		// l_zli1 PARUBQ_ZONELIBRE.ZLI1%TYPE;
		// l_zli2 PARUBQ_ZONELIBRE.ZLI2%TYPE;
		// w_salzli PASA01_ZONELIBRE.ZLI%TYPE;
		//
		// CURSOR curs_zli IS
		// SELECT czli,zli1,zli2
		// FROM parubq_zonelibre
		// WHERE cdos = PA_CALCUL.wpdos.cdos
		// AND crub = PA_CALCUL.t_rub.crub;
		//

		// String queryStringZli = "from RhprubriqueZonelibre " +
		// " where comp_id.cdos = '" + this.getSalary().getParameter().getDossier() + "'" +
		// " and comp_id.crub = '" + rubrique.getComp_id().getCrub() + "'";
		//
		// CURSOR curs_cm IS
		// SELECT dtad , dtrd
		// FROM pacaiss
		// WHERE cdos = PA_CALCUL.wpdos.cdos
		// AND nmat = PA_CALCUL.wsal01.nmat
		// AND (rscm = PA_CALCUL.t_rub.crub OR rpcm = PA_CALCUL.t_rub.crub );
		String queryStringCm = "from CaisseMutuelleSalarie " + " where comp_id.cdos = '" + this.getSalary().getParameter().getDossier() + "'" + " and comp_id.nmat = '" + salary.getInfoSalary().getComp_id().getNmat() + "'"
				+ " and (comp_id.rscm = '" + rubrique.getComp_id().getCrub() + "' or rpcm = '" + rubrique.getComp_id().getCrub() + "')";
		// BEGIN
		//
		// PA_CALCUL.zz := 0;
		// ---------------------------------------------------------------------------
		// -- PAIE AU NET : Rubrique calculee uniquement si sa phase de calcul est >=
		// -- a la phase de calcul en cours
		// ---------------------------------------------------------------------------
		//
		// IF PA_CALCUL.w_paienet = 'O' AND (PA_CALCUL.t_rub.ajnu > PA_CALCUL.w_numcal AND PA_CALCUL.t_rub.ajus = 'O' ) THEN
		// RETURN FALSE;
		// END IF;w_paienet=infoSalary.getPnet()
		if (("O".equals(salary.getInfoSalary().getPnet())) && rubrique != null && (!ClsObjectUtil.isNull(rubrique.getAjnu()) && rubrique.getAjnu() > numeroAjustement && "O".equals(rubrique.getAjus())))
		{
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\n--In Rubrique Clone : ...Raison 1");
			return false;
		}
		//
		// ---------------------------------------------------------------------------
		// -- LH 051197
		// -- Test de la colonne parubq.apcf
		// ---------------------------------------------------------------------------
		// -- Remarque : Ces test ne sont valables que pour le fictif 'A'
		// -- Dans le fictif 'B' il n'y a qu'une chaine de tratement donc
		// -- pas de concordance a respecter.
		// -- apcf = 'N' : Pas de calcul pendant le fictif, donc, pour con-
		// -- server la concordance avec les bulletins de
		// -- controle, il ne faut pas calculer la rubrique
		// -- si FICTIF='A' et periode de conges.
		// -- apcf = 'U' : Calcul uniquement pendant le fictif. Donc calcul
		// -- uniquement quand pmcf <= w_aamm <= dfcf dans le
		// -- calcul 'Normal'.
		// -- apcf = 'P' : Calcul le premier mois du fictif, donc en mode
		// -- 'Normal', si w_aamm = pmcf
		// ---------------------------------------------------------------------------
		// -- LH 051197
		// -- apcf = 'E' : Exclus du traitement fictif, calcul uniquement dans pacalcul
		// ---------------------------------------------------------------------------
		// IF PA_CALCUL.Fictif_A
		// THEN
		if (this.getSalary().getParameter().isFictiveCalculusA())
		{
			// IF NOT PA_PAIE.NouB(PA_CALCUL.wsal01.pmcf) AND
			// PA_CALCUL.w_aamm >= PA_CALCUL.wsal01.pmcf AND
			// PA_CALCUL.w_aamm <= TO_CHAR(PA_CALCUL.wsal01.dfcf, 'YYYYMM')
			// THEN
			ClsDate myDfcf = new ClsDate(salary.getInfoSalary().getDfcf());
			if (!ClsObjectUtil.isNull(salary.getInfoSalary().getPmcf()) && (salary.getInfoSalary().getPmcf().compareToIgnoreCase(salary.getMyMonthOfPay().getYearAndMonth()) <= 0)
					&& myDfcf.getYearAndMonth().compareToIgnoreCase(salary.getMyMonthOfPay().getYearAndMonth()) >= 0)
			{
				// ------- Salarie en periode de conges ---------
				// IF PA_CALCUL.t_rub.apcf = 'N' THEN RETURN FALSE; END IF;
				if ("N".equals(rubrique.getApcf()))
				{
					if ('O' == salary.getParameter().getGenfile())
						salary.addToOutputtext( "\n--In Rubrique Clone : ...Raison 2");
					return false;
				}
				// IF PA_CALCUL.t_rub.apcf = 'P' AND PA_CALCUL.w_aamm != PA_CALCUL.wsal01.pmcf THEN RETURN FALSE; END IF;
				if ("P".equals(rubrique.getApcf()) && salary.getInfoSalary().getPmcf().compareToIgnoreCase(salary.getMyMonthOfPay().getYearAndMonth()) != 0)
				{
					if ('O' == salary.getParameter().getGenfile())
						salary.addToOutputtext( "\n--In Rubrique Clone : ...Raison 3");
					return false;
				}
			}
			// ELSE
			else
			{
				// ------- Salarie hors periode de conges ---------
				// IF PA_CALCUL.t_rub.apcf = 'U' THEN RETURN FALSE; END IF;
				if ("U".equals(rubrique.getApcf()))
				{
					if ('O' == salary.getParameter().getGenfile())
						salary.addToOutputtext( "\n--In Rubrique Clone : ...Raison 4");
					return false;
				}
				// --IF PA_CALCUL.t_rub.apcf = 'P' THEN RETURN FALSE; END IF;
				// END IF;
			}
		}
		// END IF;
		// -------------------------------------------------------------------------
		// -- Sexe
		// IF NOT Verif_Param_A(PA_CALCUL.wsal01.sexe, PA_CALCUL.t_rub.sexe) THEN
		// RETURN FALSE;
		// END IF;
		// if(rubrique.getSexe() != null && (! ClsObjectUtil.isAppliedToObject(salary.getInfoSalary().getSexe(), rubrique.getSexe()))){
		// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : Sex doesn't match !");
		// return false;
		// }

		if (!ClsObjectUtil.isAppliedToObject(salary.getInfoSalary().getSexe(), rubrique.getSexe()))
		{
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\n--In Rubrique Clone : Sex doesn't match !");
			return false;
		}
		//
		// -- Mois de calcul
		// moi_cal := TO_NUMBER(SUBSTR(PA_CALCUL.w_aamm,5,2));
		int moisCalcul = salary.getMyMonthOfPay().getMonth();
		// IF NOT Verif_Param_N(moi_cal,PA_CALCUL.t_rub.moi1, PA_CALCUL.t_rub.moi2,
		// PA_CALCUL.t_rub.moi3, PA_CALCUL.t_rub.moi4) THEN
		// RETURN FALSE;
		// END IF;
		// boolean mois = rubrique.getMoi1() == 0 && rubrique.getMoi2() == 0 && rubrique.getMoi3() == 0 && rubrique.getMoi4() == 0;
		// if(!mois && ! ClsObjectUtil.isAppliedToObject(moisCalcul, rubrique.getMoi1(), rubrique.getMoi2(), rubrique.getMoi3(), rubrique.getMoi4())){
		// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : moisCalcul doesn't match !");
		// return false;
		// }

		if (!ClsObjectUtil.isAppliedToObject(moisCalcul, rubrique.getMoi1(), rubrique.getMoi2(), rubrique.getMoi3(), rubrique.getMoi4()))
		{
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\n--In Rubrique Clone : moisCalcul doesn't match !");
			return false;
		}
		//
		// -- No Bulletin
		// IF NOT Verif_Param_N(PA_CALCUL.wsd_fcal1.nbul, PA_CALCUL.t_rub.bul1, PA_CALCUL.t_rub.bul2,
		// PA_CALCUL.t_rub.bul3, PA_CALCUL.t_rub.bul4) THEN
		// RETURN FALSE;
		// END IF;
		// mois = rubrique.getBul1() == 0 && rubrique.getBul2() == 0 && rubrique.getBul3() == 0 && rubrique.getBul4() == 0;
		// if(!mois && ! ClsObjectUtil.isAppliedToObject(salary.getNbul(), rubrique.getBul1(), rubrique.getBul2(), rubrique.getBul3(), rubrique.getBul4())){
		// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : Nbul doesn't match !");
		// return false;
		// }

		if (!ClsObjectUtil.isAppliedToObject(salary.getNbul(), rubrique.getBul1(), rubrique.getBul2(), rubrique.getBul3(), rubrique.getBul4()))
		{
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\n--In Rubrique Clone : Nbul doesn't match !");
			return false;
		}
		//
		// -- Avantage en nature
		// IF NOT PA_PAIE.NouB(PA_CALCUL.t_rub.avn) THEN
		// IF NOT Verif_Param_A(PA_CALCUL.wsal01.avn1, PA_CALCUL.t_rub.avn) AND
		// NOT Verif_Param_A(PA_CALCUL.wsal01.avn2, PA_CALCUL.t_rub.avn) AND
		// NOT Verif_Param_A(PA_CALCUL.wsal01.avn3, PA_CALCUL.t_rub.avn) AND
		// NOT Verif_Param_A(PA_CALCUL.wsal01.avn4, PA_CALCUL.t_rub.avn) AND
		// NOT Verif_Param_A(PA_CALCUL.wsal01.avn5, PA_CALCUL.t_rub.avn) AND
		// NOT Verif_Param_A(PA_CALCUL.wsal01.avn6, PA_CALCUL.t_rub.avn) THEN
		// RETURN FALSE;
		// END IF;
		// END IF;
		if (!ClsObjectUtil.isNull(rubrique.getAvn()))
		{
			// if(! ClsObjectUtil.isAppliedToObject(rubrique.getAvn()
			// , salary.getInfoSalary().getAvn1(), salary.getInfoSalary().getAvn2()
			// , salary.getInfoSalary().getAvn3(), salary.getInfoSalary().getAvn4()
			// , salary.getInfoSalary().getAvn5(), salary.getInfoSalary().getAvn6())){
			// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : Anv doesn't match !");
			// return false;
			// }
			if (!ClsObjectUtil.isAppliedToObject(salary.getInfoSalary().getAvn1(), rubrique.getAvn()))
				if (!ClsObjectUtil.isAppliedToObject(salary.getInfoSalary().getAvn2(), rubrique.getAvn()))
					if (!ClsObjectUtil.isAppliedToObject(salary.getInfoSalary().getAvn3(), rubrique.getAvn()))
						if (!ClsObjectUtil.isAppliedToObject(salary.getInfoSalary().getAvn4(), rubrique.getAvn()))
							if (!ClsObjectUtil.isAppliedToObject(salary.getInfoSalary().getAvn5(), rubrique.getAvn()))
								if (!ClsObjectUtil.isAppliedToObject(salary.getInfoSalary().getAvn6(), rubrique.getAvn()))
								{
									if (!ClsObjectUtil.isAppliedToObject(salary.getInfoSalary().getAvn7(), rubrique.getAvn()))
									{
										if ('O' == salary.getParameter().getGenfile())
											salary.addToOutputtext( "\n--In Rubrique Clone : avn xx doesn't match !");
										return false;
									}
								}
		}
		//
		// -- Code salaire
		// IF NOT Verif_Param_A(PA_CALCUL.wsal01.cods, PA_CALCUL.t_rub.cs1, PA_CALCUL.t_rub.cs2, PA_CALCUL.t_rub.cs3) THEN
		// RETURN FALSE;
		// END IF;
		if (!ClsObjectUtil.isAppliedToObject(salary.getInfoSalary().getCods(), rubrique.getCs1(), rubrique.getCs2(), rubrique.getCs3()))
		{
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\n--In Rubrique Clone : Cods doesn't match !");
			return false;
		}
		// -- Situation familiale
		// IF NOT Verif_Param_A(PA_CALCUL.wsal01.sitf, PA_CALCUL.t_rub.sit1, PA_CALCUL.t_rub.sit2,
		// PA_CALCUL.t_rub.sit3, PA_CALCUL.t_rub.sit4) THEN
		// RETURN FALSE;
		// END IF;
		if (!ClsObjectUtil.isAppliedToObject(salary.getInfoSalary().getSitf(), rubrique.getSit1(), rubrique.getSit2(), rubrique.getSit3(), rubrique.getSit4()))
		{
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\n--In Rubrique Clone : Sitf doesn't match !");
			return false;
		}
		// -- Nombre d'enfants
		// IF PA_CALCUL.t_rub.nbe1 > PA_CALCUL.wsal01.nbec OR PA_CALCUL.t_rub.nbe2 < PA_CALCUL.wsal01.nbec THEN
		// RETURN FALSE;
		// END IF;
		boolean app = false;
		// boolean app = (rubrique.getNbe1() != null && salary.getInfoSalary().getNbec() != null)
		// && ( salary.getInfoSalary().getNbec() != null && rubrique.getNbe2() != null);
		if (salary.getInfoSalary().getNbec() != null)
		{
			if (((rubrique.getNbe1() != null) && (rubrique.getNbe1() > salary.getInfoSalary().getNbec()) || ((rubrique.getNbe2() != null) && rubrique.getNbe2() < salary.getInfoSalary().getNbec())))
			{
				if ('O' == salary.getParameter().getGenfile())
					salary.addToOutputtext( "\n--In Rubrique Clone : Nbec doesn't match !");
				return false;
			}
		}
		// -- Zone categorielle
		// IF NOT Verif_Param_A(PA_CALCUL.wsal01.afec, PA_CALCUL.t_rub.zca1, PA_CALCUL.t_rub.zca2,
		// PA_CALCUL.t_rub.zca3, PA_CALCUL.t_rub.zca4) THEN
		// RETURN FALSE;
		// END IF;
		if (!ClsObjectUtil.isAppliedToObject(salary.getInfoSalary().getAfec(), rubrique.getZca1(), rubrique.getZca2(), rubrique.getZca3(), rubrique.getZca4()))
		{
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\n--In Rubrique Clone : Afec doesn't match !");
			return false;
		}
		// -- ----- Categorie
		//sous as400, les lettres sont plus petit que les chiffres
		//tandis que sous java, les lettres sont plus grands que les chiffres
		// IF PA_CALCUL.wsal01.cat < PA_CALCUL.t_rub.cat1 OR PA_CALCUL.wsal01.cat > PA_CALCUL.t_rub.cat2 THEN
		// RETURN FALSE;
		// END IF;
		// pour le cas de cat1
		//si les deux chaines sont diff�rents de null, et que cat1 est plus grande que cat2 et si on
		//if (salary.getInfoSalary().getCat() != null && rubrique.getCat1() != null && rubrique.getCat2() != null && rubrique.getCat2().compareTo(rubrique.getCat1()) < 0)
		if(1!=1)
		{
			
				if(!(salary.getInfoSalary().getCat().compareTo(rubrique.getCat2()) < 0 || salary.getInfoSalary().getCat().compareTo(rubrique.getCat1()) > 0))
				{
					if ('O' == salary.getParameter().getGenfile())
						salary.addToOutputtext( "\n--In Rubrique Clone : Cat doesn't match !, case rub cat 2#null and sal cat#null and cat1#null and cat2<cat1");
					return false;
				}
		}
		else
		{
			if ((rubrique.getCat1() != null && salary.getInfoSalary().getCat() != null && salary.getInfoSalary().getCat().compareTo(rubrique.getCat1()) < 0))
			{
				if ('O' == salary.getParameter().getGenfile())
					salary.addToOutputtext( "\n--In Rubrique Clone : Cat doesn't match !, case rub cat 1#null and sal cat#null and sal cat < rub cat1");
				return false;
			}
			else if ((salary.getInfoSalary().getCat() == null && rubrique.getCat1() != null))
			{
				if ('O' == salary.getParameter().getGenfile())
					salary.addToOutputtext( "\n--In Rubrique Clone : Cat doesn't match !, case rub cat 1#null and sal cat=null");
				return false;
			}
			// pour le cas de cat2
			if ((rubrique.getCat2() != null && salary.getInfoSalary().getCat() != null && salary.getInfoSalary().getCat().compareTo(rubrique.getCat2()) > 0))
			{
				if ('O' == salary.getParameter().getGenfile())
					salary.addToOutputtext( "\n--In Rubrique Clone : Cat doesn't match !, case rub cat 2#null and sal cat#null and sal cat > rub cat2");
				return false;
			}
			else if ((salary.getInfoSalary().getCat() != null && rubrique.getCat2() == null))
			{
				if ('O' == salary.getParameter().getGenfile())
					salary.addToOutputtext( "\n--In Rubrique Clone : Cat doesn't match !, case rub cat 2#null and sal cat=null");
				return false;
			}
		}
		// -- Type de contrat
		// IF NOT Verif_Param_A(PA_CALCUL.wsal01.typc, PA_CALCUL.t_rub.tyc1, PA_CALCUL.t_rub.tyc2,
		// PA_CALCUL.t_rub.tyc3, PA_CALCUL.t_rub.tyc4,
		// PA_CALCUL.t_rub.tyc5, PA_CALCUL.t_rub.tyc6,
		// PA_CALCUL.t_rub.tyc7, PA_CALCUL.t_rub.tyc8) THEN
		// RETURN FALSE;
		// END IF;
		if (!ClsObjectUtil.isAppliedToObject(salary.getInfoSalary().getTypc(), rubrique.getTyc1(), rubrique.getTyc2(), rubrique.getTyc3(), rubrique.getTyc4(), rubrique.getTyc5(), rubrique.getTyc6(), rubrique.getTyc7(),
				rubrique.getTyc8()))
		{
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\n--In Rubrique Clone : Typc doesn't match !");
			return false;
		}
		// -- ----- Grade
		// IF NOT Verif_Param_A(PA_CALCUL.wsal01.grad, PA_CALCUL.t_rub.gra1, PA_CALCUL.t_rub.gra2,
		// PA_CALCUL.t_rub.gra3, PA_CALCUL.t_rub.gra4,
		// PA_CALCUL.t_rub.gra5, PA_CALCUL.t_rub.gra6,
		// PA_CALCUL.t_rub.gra7, PA_CALCUL.t_rub.gra8) THEN
		// RETURN FALSE;
		// END IF;
		if (!ClsObjectUtil.isAppliedToObject(salary.getInfoSalary().getGrad(), rubrique.getGra1(), rubrique.getGra2(), rubrique.getGra3(), rubrique.getGra4(), rubrique.getGra5(), rubrique.getGra6(), rubrique.getGra7(),
				rubrique.getGra8()))
		{
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\n--In Rubrique Clone : Grad doesn't match !");
			return false;
		}
		// -- Niveau 1
		// IF NOT Verif_Param_A(PA_CALCUL.wsal01.niv1, PA_CALCUL.t_rub.niv11, PA_CALCUL.t_rub.niv12,
		// PA_CALCUL.t_rub.niv13, PA_CALCUL.t_rub.niv14) THEN
		// RETURN FALSE;
		// END IF;
		if (!ClsObjectUtil.isAppliedToObject(salary.getInfoSalary().getNiv1(), rubrique.getNiv11(), rubrique.getNiv12(), rubrique.getNiv13(), rubrique.getNiv14()))
		{
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\n--In Rubrique Clone : Niv1 doesn't match !");
			return false;
		}
		// -- Niveau 2
		// IF NOT Verif_Param_A(PA_CALCUL.wsal01.niv2, PA_CALCUL.t_rub.niv21, PA_CALCUL.t_rub.niv22,
		// PA_CALCUL.t_rub.niv23, PA_CALCUL.t_rub.niv24) THEN
		// RETURN FALSE;
		// END IF;
		if (!ClsObjectUtil.isAppliedToObject(salary.getInfoSalary().getNiv2(), rubrique.getNiv21(), rubrique.getNiv22(), rubrique.getNiv23(), rubrique.getNiv24()))
		{
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\n--In Rubrique Clone : Niv2 doesn't match !");
			return false;
		}
		// -- Niveau 3
		// IF NOT Verif_Param_A(PA_CALCUL.wsal01.niv3, PA_CALCUL.t_rub.niv31, PA_CALCUL.t_rub.niv32,
		// PA_CALCUL.t_rub.niv33, PA_CALCUL.t_rub.niv34) THEN
		// RETURN FALSE;
		// END IF;
		if (!ClsObjectUtil.isAppliedToObject(salary.getInfoSalary().getNiv3(), rubrique.getNiv31(), rubrique.getNiv32(), rubrique.getNiv33(), rubrique.getNiv34()))
		{
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\n--In Rubrique Clone : Niv3 doesn't match !");
			return false;
		}
		// -- Age
		// -- LH 260198 L'age etait calcule avec des decimales
		// -- LH 160700 nbr_an := FLOOR( ( date_dep - wsal01.dtna ) / 365 );
		// -- IF nbr_an < t_rub.age1 OR nbr_an > t_rub.age2 THEN
		// IF PA_CALCUL.Age_agent < PA_CALCUL.t_rub.age1 OR PA_CALCUL.Age_agent > PA_CALCUL.t_rub.age2 THEN
		// RETURN FALSE;
		// END IF;
		Long age1  = NumberUtils.nvl(rubrique.getAge1(), 0).longValue();
		Long age2  = NumberUtils.nvl(rubrique.getAge2(), 100).longValue();
		//app = rubrique.getAge1() == null && rubrique.getAge2() == null;
		// @yannick
		// if(app && (salary.getParameter().AGE_MAX_OF_SALARY < rubrique.getAge1()
		// || salary.getParameter().AGE_MAX_OF_SALARY > rubrique.getAge2())){
		// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : Age1 doesn't match !");
		// return false;
		// }
		if (salary.getAgeOfAgent() < age1 || salary.getAgeOfAgent() > age2)
		{
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\n--In Rubrique Clone : Age1 doesn't match !");
			return false;
		}
		// -- Nationalite
		// IF NOT Verif_Param_A(PA_CALCUL.wsal01.nato, PA_CALCUL.t_rub.nat1, PA_CALCUL.t_rub.nat2) THEN
		// RETURN FALSE;
		// END IF;
		if (!ClsObjectUtil.isAppliedToObject(salary.getInfoSalary().getNato(), rubrique.getNat1(), rubrique.getNat2()))
		{
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\n--In Rubrique Clone : Nato doesn't match !");
			return false;
		}
		// -- Syndicat
		// IF NOT Verif_Param_A(PA_CALCUL.wsal01.synd, PA_CALCUL.t_rub.synd) THEN
		// RETURN FALSE;
		// END IF;
		if (!ClsObjectUtil.isAppliedToObject(salary.getInfoSalary().getSynd(), rubrique.getSynd()))
		{
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\n--In Rubrique Clone : Synd doesn't match !");
			return false;
		}
		// -- Fonction
		// IF NOT Verif_Param_A(PA_CALCUL.wsal01.fonc, PA_CALCUL.t_rub.fon1, PA_CALCUL.t_rub.fon2,
		// PA_CALCUL.t_rub.fon3, PA_CALCUL.t_rub.fon4,
		// PA_CALCUL.t_rub.fon5, PA_CALCUL.t_rub.fon6,
		// PA_CALCUL.t_rub.fon7, PA_CALCUL.t_rub.fon8) THEN
		// RETURN FALSE;
		// END IF;
		if (!ClsObjectUtil.isAppliedToObject(salary.getInfoSalary().getFonc(), rubrique.getFon1(), rubrique.getFon2(), rubrique.getFon3(), rubrique.getFon4(), rubrique.getFon5(), rubrique.getFon6(), rubrique.getFon7(),
				rubrique.getFon8()))
		{
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\n--In Rubrique Clone : Fonc doesn't match !");
			return false;
		}
		// -- Regime
		// IF NOT Verif_Param_A(PA_CALCUL.wsal01.regi, PA_CALCUL.t_rub.reg1, PA_CALCUL.t_rub.reg2,
		// PA_CALCUL.t_rub.reg3, PA_CALCUL.t_rub.reg4,
		// PA_CALCUL.t_rub.reg5, PA_CALCUL.t_rub.reg6,
		// PA_CALCUL.t_rub.reg7, PA_CALCUL.t_rub.reg8) THEN
		// RETURN FALSE;
		// END IF;
		if (!ClsObjectUtil.isAppliedToObject(salary.getInfoSalary().getRegi(), rubrique.getReg1(), rubrique.getReg2(), rubrique.getReg3(), rubrique.getReg4(), rubrique.getReg5(), rubrique.getReg6(), rubrique.getReg7(),
				rubrique.getReg8()))
		{
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\n--In Rubrique Clone : Regi doesn't match !");
			return false;
		}
		// -- Classe
		// IF NOT Verif_Param_A(PA_CALCUL.wsal01.clas, PA_CALCUL.t_rub.clas1, PA_CALCUL.t_rub.clas2,
		// PA_CALCUL.t_rub.clas3, PA_CALCUL.t_rub.clas4) THEN
		// RETURN FALSE;
		// END IF;
		if (!ClsObjectUtil.isAppliedToObject(salary.getInfoSalary().getClas(), rubrique.getClas1(), rubrique.getClas2(), rubrique.getClas3(), rubrique.getClas4()))
		{
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\n--In Rubrique Clone : Clas doesn't match !");
			return false;
		}
		// -- Fonctionnaire
		// IF NOT Verif_Param_A(PA_CALCUL.wsal01.codf, PA_CALCUL.t_rub.cfon) THEN
		// RETURN FALSE;
		// END IF;
		if (!ClsObjectUtil.isAppliedToObject(salary.getInfoSalary().getCodf(), rubrique.getCfon()))
		{
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\n--In Rubrique Clone : Codf doesn't match !");
			return false;
		}
		// -- Hierarchie fonction publique
		// IF NOT Verif_Param_A(PA_CALCUL.wsal01.hifo, PA_CALCUL.t_rub.hif1, PA_CALCUL.t_rub.hif2,
		// PA_CALCUL.t_rub.hif3, PA_CALCUL.t_rub.hif4) THEN
		// RETURN FALSE;
		// END IF;
		if (!ClsObjectUtil.isAppliedToObject(salary.getInfoSalary().getHifo(), rubrique.getHif1(), rubrique.getHif2(), rubrique.getHif3(), rubrique.getHif4()))
		{
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\n--In Rubrique Clone : Hifo doesn't match !");
			return false;
		}
		// -- Zone libre 1
		// IF NOT Verif_Param_A(PA_CALCUL.wsal01.zli1, PA_CALCUL.t_rub.zl11, PA_CALCUL.t_rub.zl12) THEN
		// RETURN FALSE;
		// END IF;
		if (!ClsObjectUtil.isAppliedToObject(salary.getInfoSalary().getZli1(), rubrique.getZl11(), rubrique.getZl12()))
		{
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\n--In Rubrique Clone : Zli1 doesn't match !");
			return false;
		}
		// -- Zone libre 2
		// IF NOT Verif_Param_A(PA_CALCUL.wsal01.zli2, PA_CALCUL.t_rub.zl21, PA_CALCUL.t_rub.zl22) THEN
		// RETURN FALSE;
		// END IF;
		if (!ClsObjectUtil.isAppliedToObject(salary.getInfoSalary().getZli2(), rubrique.getZl21(), rubrique.getZl22()))
		{
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\n--In Rubrique Clone : Zli2 doesn't match !");
			return false;
		}
		//
		// -- MM 17/12/2003 Ajout des zones libres supplementaires.
		// OPEN curs_zli;
		// List zonelibre = this.getSalary().getService().find(queryStringZli);
		List zonelibre = new ArrayList();
		if (!this.getSalary().getParameter().getListOfRubqZoneLibre().isEmpty() && this.getSalary().getParameter().getListOfRubqZoneLibre().containsKey(rubrique.getComp_id().getCrub()))
		{
			zonelibre = this.getSalary().getParameter().getListOfRubqZoneLibre().get(rubrique.getComp_id().getCrub());
		}
		// LOOP
		// FETCH curs_zli INTO l_czli,l_zli1, l_zli2;
		// EXIT WHEN curs_zli%NOTFOUND;
		//
		// @emmanuel pour absence RhprubriqueZonelibre
		String zoneLibreSalarie = "";
//		RhprubriqueZonelibre zonelibre2 = null;
//		for (Object obj : zonelibre)
//		{
//			zonelibre2 = (RhprubriqueZonelibre) obj;
//			// w_salzli := RecZli(PA_CALCUL.wsal01.nmat,l_czli);
//			zoneLibreSalarie = salary.rechercheZoneLibre(zonelibre2.getComp_id().getCzli());
//			//
//			// IF NOT Verif_Param_A(w_salzli, l_zli1, l_zli2) THEN
//			if (!ClsObjectUtil.isAppliedToObject(zoneLibreSalarie, zonelibre2.getZli1(), zonelibre2.getZli2()))
//			{
//				if ('O' == salary.getParameter().getGenfile())
//					salary.addToOutputtext( "\n--In Rubrique Clone : zl "+zonelibre2.getComp_id().getCzli()+" doesn't match !");
//				return false;
//			}
//			// RETURN FALSE;
//			// END IF;
//		}
		//
		// END LOOP;
		// CLOSE curs_zli;
		// -- Fin modif MM.
		//
		// -- Verif. inscription caisses et mutuelles
		// IF PA_CALCUL.t_rub.cais = 'O' THEN
		// -- Recherche caisses et mutuelles
		//
		// boolean cmExist = false;
		Integer dateAmdt = 0;
		if ("O".equals(rubrique.getCais()))
		{
			// l_exist_cm := FALSE;
			// OPEN curs_cm;
			List listOfCaisse = this.getSalary().getService().find(queryStringCm);
			// LOOP
			CaisseMutuelleSalarie pacaiss = null;
			for (Object obj : listOfCaisse)
			{
				pacaiss = (CaisseMutuelleSalarie) obj;
				// FETCH curs_cm INTO l_dtad, l_dtrd;
				// EXIT WHEN curs_cm%NOTFOUND;
				//
				// l_exist_cm := TRUE;
				//
				// IF l_dtad IS NULL THEN
				// EXIT;
				if (pacaiss.getDtad() == null)
				{
					if ('O' == salary.getParameter().getGenfile())
						salary.addToOutputtext( "\n--In Rubrique Clone : date admission � la caisse EST NULL");
					// return false;
					break;
				}
				// END IF;
				// l_amdt := TO_NUMBER(TO_CHAR(l_dtad,'YYYY')) * 100 + TO_NUMBER(TO_CHAR(l_dtad,'MM')) ;
				dateAmdt = new ClsDate(pacaiss.getDtad()).getYear() * 100 + new ClsDate(pacaiss.getDtad()).getMonth();
				// IF PA_CALCUL.w_aamm < l_amdt THEN -- Controle date adhesion
				// RETURN FALSE;
				// END IF;
				if (salary.getMyMonthOfPay().getYearAndMonthInt() < dateAmdt)
				{
					if ('O' == salary.getParameter().getGenfile())
						salary.addToOutputtext( "\n--In Rubrique Clone : date admission caisse sup � periode de paie ");
					return false;
				}
				// IF l_dtrd IS NULL THEN
				// EXIT;
				// END IF;
				if (pacaiss.getDtrd() == null)
				{
					if ('O' == salary.getParameter().getGenfile())
						salary.addToOutputtext( "\n--In Rubrique Clone : dtrd de la caisse est null");
					// return false;
					break;
				}
				// l_amdt := TO_NUMBER(TO_CHAR(l_dtrd,'YYYY')) * 100 + TO_NUMBER(TO_CHAR(l_dtrd,'MM')) ;
				dateAmdt = new ClsDate(pacaiss.getDtrd()).getYear() * 100 + new ClsDate(pacaiss.getDtrd()).getMonth();
				// IF l_amdt < PA_CALCUL.w_aamm THEN -- Controle date radiation
				// RETURN FALSE;
				// END IF;
				if (salary.getMyMonthOfPay().getYearAndMonthInt() > dateAmdt)
				{
					if ('O' == salary.getParameter().getGenfile())
						salary.addToOutputtext( "\n--In Rubrique Clone : ...dtrd est sup � periode de paie actuelle");
					return false;
				}
				// EXIT;
				// END LOOP; -- FIN 'FETCH curs_cm INTO l_dtad, l_dtrd'
			}
			// CLOSE curs_cm;
			//
			if (listOfCaisse.size() == 0)
				return false;
			// IF NOT l_exist_cm THEN
			// RETURN FALSE;
			// END IF;
			// END IF;
		}
		//
		return true;
	}

	/**
	 * => ins_rubq Ins�rer la rubrique dans PaCalc
	 * 
	 * @param clas
	 * @param nprt
	 * @param ruba
	 * @param trtb
	 */
	public void insertIntoSalariesFile(String clas, String nprt, String ruba, String trtb)
	{
		
		//System.out.println(rubrique.getComp_id().getCrub()+" Sorite : Mnt = "+this.salary.getValeurRubriquePartage().getAmount());
		
		
		try
		{
			// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : >>insertIntoSalariesFile");
			// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : >>base ...: " +
			// this.salary.getValeurRubriquePartage().getBase());
			// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : >>base patronale ...: " +
			// this.salary.getValeurRubriquePartage().getBasePlafonnee());
			// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : >>montant ...: " +
			// this.salary.getValeurRubriquePartage().getAmount());
			// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : >>taux ...: " +
			// this.salary.getValeurRubriquePartage().getRates());
			// BEGIN
			// IF NOT PA_PAIE.NouB(t_rub.edbbu) THEN
			// ind_edbbu := TO_NUMBER(t_rub.edbbu);
			// w_bas := t9999_mont(ind_edbbu);
			// w_basp := t9999_mont(ind_edbbu);
			// IF PA_PAIE.NouZ(w_bas) THEN
			// w_bas := 0;
			// END IF;
			// IF PA_PAIE.NouZ(w_basp) THEN
			// w_basp := 0;
			// END IF;
			// END IF;
			// EXCEPTION
			// WHEN OTHERS THEN
			// null;
			// END;
			// @emmanuel: tmpRubrique est la rubrique dans laquelle on prend les valeurs de base, etc.
			ClsRubriqueClone tmpRubrique = null;
			if (!ClsObjectUtil.isNull(rubrique.getEdbbu()))
			{
				// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : getEdbbu :" + rubrique.getEdbbu());
				tmpRubrique = salary.findRubriqueClone(rubrique.getEdbbu());
//			salary.getValeurRubriquePartage().setBase(tmpRubrique.getBase());
//			salary.getValeurRubriquePartage().setBasePlafonnee(tmpRubrique.getBase());
				salary.getValeurRubriquePartage().setBase(tmpRubrique.getAmount());
				salary.getValeurRubriquePartage().setBasePlafonnee(tmpRubrique.getAmount());
			}
			//
			// i := rub_trt;
			//
			// -- Arrondi du resultat apres application de l'algo si necessaire
			// IF NOT PA_PAIE.NouB(t_rub.arro) THEN
			// arrondi(w_mon);
			// IF Pb_Calcul THEN
			// RETURN FALSE;
			// END IF;
			// END IF;

			if (!ClsObjectUtil.isNull(rubrique.getArro()))
			{
				double val = this.calculateArrondi(salary.getParameter(), this.salary.getValeurRubriquePartage().getAmount());
				this.salary.getValeurRubriquePartage().setAmount(val);
			}
			
			
			//On arrondi directement le montant calcul�, avant de le garder en m�moire, et en bd
			this.arrondirFonctionNddd();
			
			
			
			//
			// -- ----- Mise a jour des montants dans la table des rubriques calculees
			// -- LH 041297 IF t9999_basc(i) IS NULL THEN
			// IF NVL( t9999_basc(i), 0) = 0 THEN
			// t9999_basc(i) := w_bas;
			// ELSE
			// t9999_basc(i) := t9999_basc(i) + w_bas;
			// END IF;
			this.setBase(this.getBase() + this.salary.getValeurRubriquePartage().getBase());
			//this.setBase(ClsStringUtil.truncateToXDecimal(this.getBase(),salary.parameter.dossierNbreDecimale).doubleValue());
			
			//
			// -- LH 041297 IF t9999_basp(i) IS NULL THEN
			// IF NVL( t9999_basp(i), 0 ) = 0 THEN
			// t9999_basp(i) := w_basp;
			// ELSE
			// t9999_basp(i) := t9999_basp(i) + w_basp;
			// END IF;
			this.setBasePlafonnee(this.getBasePlafonnee() + this.salary.getValeurRubriquePartage().getBasePlafonnee());
			//this.setBasePlafonnee(ClsStringUtil.truncateToXDecimal(this.getBasePlafonnee(),salary.parameter.dossierNbreDecimale).doubleValue());
			
			
			//
			// -- LH 041297 IF t9999_taux(i) IS NULL THEN
			// IF NVL( t9999_taux(i), 0 ) = 0 THEN
			// t9999_taux(i) := w_tau;
			// ELSE
			// t9999_taux(i) := t9999_taux(i) + w_tau;
			// END IF;
			
			this.setRates(this.getRates() + this.salary.getValeurRubriquePartage().getRates());
			
			
			//
			// -- LH 041297 IF t9999_mont(i) IS NULL THEN
			// IF NVL( t9999_mont(i), 0 ) = 0 THEN
			// t9999_mont(i) := w_mon;
			// ELSE
			// t9999_mont(i) := t9999_mont(i) + w_mon;
			// END IF;
			this.setAmount(this.getAmount() + this.salary.getValeurRubriquePartage().getAmount());
			
			
			
			//
			// -------------------------------------------------------------------------
			// -- Insertion dans pacalc
			// -------------------------------------------------------------------------
			//
			// wcalc.rubq := LTRIM(TO_CHAR(rub_trt,'0999'));
			// wcalc.basc := w_bas;
			// wcalc.basp := w_basp;
			// wcalc.taux := w_tau;
			// wcalc.mont := w_mon;
			// wcalc.nprt := w_nprt;
			// wcalc.ruba := wevar.ruba;
			// IF w_argu = 'PSEUDO-EV' THEN
			// w_argu := ' ';
			// END IF;
			// wcalc.argu := w_argu;
			String argu = ("PSEUDO-EV".equals(salary.getEvparam().getArgu())) ? "" : salary.getEvparam().getArgu();
			//
			// derniere_ligne := derniere_ligne + 1;
			// ClsRubriqueClone.derniereLigne ++;
			salary.incrementerDerniereLigne();
			//
			// IF retroactif THEN
			// BEGIN
			// INSERT INTO prcalc (cdos,nmat,aamm,nbul,nlig,
			// rubq,basc,basp,taux,mont,nprt,ruba,argu,clas,trtb,nlot)
			//
			// VALUES (wcalc.cdos, wcalc.nmat, wcalc.aamm, wcalc.nbul, derniere_ligne,
			// wcalc.rubq, wcalc.basc, wcalc.basp, wcalc.taux, wcalc.mont,
			// wcalc.nprt, wcalc.ruba, wcalc.argu, wcalc.clas, wcalc.trtb,w_nlot);
			// EXCEPTION
			// WHEN OTHERS THEN
			// err_msg := 'INCIDENT INSERT FICHIER prcalc';
			// RETURN FALSE;
			// END;
			// ELSE
			// BEGIN
			// INSERT INTO pacalc (cdos,nmat,aamm,nbul,nlig,
			// rubq,basc,basp,taux,mont,nprt,ruba,argu,clas,trtb)
			//
			// VALUES (wcalc.cdos, wcalc.nmat, wcalc.aamm, wcalc.nbul, derniere_ligne,
			// wcalc.rubq, wcalc.basc, wcalc.basp, wcalc.taux, wcalc.mont,
			// wcalc.nprt, wcalc.ruba, wcalc.argu, wcalc.clas, wcalc.trtb);
			// EXCEPTION
			// WHEN OTHERS THEN
			// err_msg := 'INCIDENT INSERT FICHIER pacalc';
			// RETURN FALSE;
			// END;
			// END IF;

			/** ****formatage du numero de pret pour le transformer en entier au cas ou �a en est un******** */
			try
			{
				nprt = Integer.valueOf(nprt).toString();
			}
			catch (Exception e)
			{
				nprt = null;
			}

			if (salary.getParameter().isUseRetroactif())
			{
//				Rhtprcalc oRhtprcalc = new Rhtprcalc();
//				RhtprcalcPK pk = new RhtprcalcPK(salary.getParameter().getDossier(), salary.getInfoSalary().getComp_id().getNmat(), salary.getMonthOfPay(), salary.getNbul(), salary.getDerniereLigne(), this.rubrique
//						.getComp_id().getCrub(), salary.getNlot());
//				//
//				oRhtprcalc.setComp_id(pk);
//				oRhtprcalc.setBasc(new BigDecimal(this.salary.getValeurRubriquePartage().getBase()));
//				oRhtprcalc.setBasp(new BigDecimal(this.salary.getValeurRubriquePartage().getBasePlafonnee()));
//				oRhtprcalc.setTaux(new BigDecimal(this.salary.getValeurRubriquePartage().getRates()));
//				oRhtprcalc.setMont(new BigDecimal(this.salary.getValeurRubriquePartage().getAmount()));
//				//
//				oRhtprcalc.setArgu(argu);
//				oRhtprcalc.setClas(clas);
//				oRhtprcalc.setNprt(nprt);
//				oRhtprcalc.setRuba(ruba);
//				oRhtprcalc.setTrtb(trtb);
//				//
//				this.getSalary().getService().save(oRhtprcalc);
			}
			else
			{

				CalculPaie oCalculPaie = new CalculPaie();
				oCalculPaie.setIdEntreprise(Integer.valueOf(salary.getParameter().getDossier()));
				oCalculPaie.setNmat(salary.getInfoSalary().getComp_id().getNmat());
				oCalculPaie.setAamm(salary.getMonthOfPay());
				oCalculPaie.setNbul(salary.getNbul());
				oCalculPaie.setRubq(this.rubrique.getComp_id().getCrub());
				oCalculPaie.setNlig(salary.getDerniereLigne());
				oCalculPaie.setBasc(new BigDecimal(this.salary.getValeurRubriquePartage().getBase()));
				oCalculPaie.setBasp(new BigDecimal(this.salary.getValeurRubriquePartage().getBasePlafonnee()));
				if ('O' == salary.getParameter().getGenfile())
					salary.addToOutputtext( "\n--In Rubrique Clone : Before save, rate = " + this.salary.getValeurRubriquePartage().getRates() + " and bigdecimal = "
							+ new BigDecimal(this.salary.getValeurRubriquePartage().getRates()));
				if ('O' == salary.getParameter().getGenfile())
					salary.addToOutputtext( "\n--In Rubrique Clone : Before save, amount = " + this.salary.getValeurRubriquePartage().getAmount() + " and bigdecimal = "
							+ new BigDecimal(this.salary.getValeurRubriquePartage().getAmount()));
				if ('O' == salary.getParameter().getGenfile())
					salary.addToOutputtext( "\n--In Rubrique Clone : Before save, base = " + this.salary.getValeurRubriquePartage().getBase() + " and bigdecimal = "
							+ new BigDecimal(this.salary.getValeurRubriquePartage().getBase()));
				oCalculPaie.setTaux(new BigDecimal(this.salary.getValeurRubriquePartage().getRates()));
				oCalculPaie.setMont(new BigDecimal(this.salary.getValeurRubriquePartage().getAmount()));
				//
				oCalculPaie.setArgu(argu);
				oCalculPaie.setClas(clas);
				oCalculPaie.setNprt(nprt);
				oCalculPaie.setRuba(ruba);
				oCalculPaie.setTrtb(trtb);
				
				//
				
				oCalculPaie.setBasc(ClsStringUtil.truncateToXDecimal(oCalculPaie.getBasc(),3));
				oCalculPaie.setBasp(ClsStringUtil.truncateToXDecimal(oCalculPaie.getBasp(),3));
				oCalculPaie.setTaux(ClsStringUtil.truncateToXDecimal(oCalculPaie.getTaux(),3));
				oCalculPaie.setMont(ClsStringUtil.truncateToXDecimal(oCalculPaie.getMont(),3));
//			System.out.println("Base "+oCalculPaie.getBasc());
//			System.out.println("Base plaf "+oCalculPaie.getBasp());
//			System.out.println("Taux "+oCalculPaie.getTaux());
//			System.out.println("mont "+oCalculPaie.getMont());
				//
				this.getSalary().getService().save(oCalculPaie);
			}
			//
//			this.getSalary().getService().getSession().flush();
//			this.getSalary().getService().getSession().clear();
			//
			inserted = true;
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Integer getNbrDecimal()
	{
		//Gestion de la pr�sentation du bulletin
		long prbul = this.rubrique.getPrbul();
		Integer nbr = 0;
		//Valeur prbul
		//0 pas d'�dition
		//1 gain et taux entier
		//2 gain et taux deux d�cimales
		//3 retenue et taux entier
		//4 retenue et taux deux d�cimales
		//99 Net a payer
		
		return nbr;
	}
	
	private void arrondirFonctionNddd()
	{
		try
		{
			
			Integer nddd = 3;
			Integer nddd2 = 3;
			//Si c'est une rubrique comptabilis�e ou une rubrique � afficher sur le bulletin, alors on tronque
			if(this.getRubrique().getPrbul() != 0 || StringUtils.equals(this.getRubrique().getComp(),"O"))
				nddd=this.salary.parameter.dossierNbreDecimale;
			ParameterUtil.println("Matricule "+salary.getInfoSalary().getComp_id().getNmat()+" - Rubrique "+this.getRubrique().getComp_id().getCrub()+" - Nombre D�cimal = "+nddd+" / Mnt = ");
			//On arrondi directement le montant calcul�, avant de le garder en m�moire, et en bd
			double montantFinal = this.salary.getValeurRubriquePartage().getAmount() ;
			ParameterUtil.println(montantFinal);
			montantFinal = ClsStringUtil.truncateToXDecimal(montantFinal,nddd).doubleValue();
			double tauxFinal = this.salary.getValeurRubriquePartage().getRates();
			ParameterUtil.println(tauxFinal);
			tauxFinal = ClsStringUtil.truncateToXDecimal(tauxFinal,nddd2).doubleValue();
			double baseFinal = this.salary.getValeurRubriquePartage().getBase();
			ParameterUtil.println(baseFinal);
			baseFinal = ClsStringUtil.truncateToXDecimal(baseFinal,nddd2).doubleValue();
			double basePFinal = this.salary.getValeurRubriquePartage().getBasePlafonnee();
			ParameterUtil.println(basePFinal);
			basePFinal = ClsStringUtil.truncateToXDecimal(basePFinal,nddd2).doubleValue();
			//En m�moire : 
			this.salary.getValeurRubriquePartage().setAmount(montantFinal);
			this.salary.getValeurRubriquePartage().setRates(tauxFinal);
			this.salary.getValeurRubriquePartage().setBase(baseFinal);
			this.salary.getValeurRubriquePartage().setBasePlafonnee(basePFinal);
		}
		catch (Exception e)
		{
			System.out.print("Matricule "+salary.getInfoSalary().getComp_id().getNmat()+" - Rubrique "+this.getRubrique().getComp_id().getCrub()+" / "+this.getRubrique().getLrub()+" , erreur lors de l'arrondi");
			
			e.printStackTrace();
		}
	}

	/**
	 * CHARGEMENT DANS LA TABLE DES RUBRIQUES CALCULEES ET PACALC
	 * 
	 * @return true ou false
	 */
	public boolean insertRubriqueAlgo()
	{
		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\n--In Rubrique Clone : >>insertRubriqueAlgo");
		ClsRubriqueClone rubrique = this;
		if (salary.getValeurRubriquePartage().getAmount() == 0)
			return true;

		//On arrondi directement le montant calcul�, avant de le garder en m�moire, et en bd
		this.arrondirFonctionNddd();
		
		// -- ----- Mise a jour des montants dans la table des rubriques calculees
		if (rubrique.getBase() == 0)
			rubrique.setBase(salary.getValeurRubriquePartage().getBase());
		else
			rubrique.setBase(rubrique.getBase() + salary.getValeurRubriquePartage().getBase());
		if (rubrique.getBasePlafonnee() == 0)
			rubrique.setBasePlafonnee(salary.getValeurRubriquePartage().getBasePlafonnee());
		else
			rubrique.setBasePlafonnee(rubrique.getBasePlafonnee() + salary.getValeurRubriquePartage().getBasePlafonnee());
		if (rubrique.getRates() == 0)
			rubrique.setRates(salary.getValeurRubriquePartage().getRates());
		else
			rubrique.setRates(rubrique.getRates() + salary.getValeurRubriquePartage().getRates());
		if (rubrique.getAmount() == 0)
			rubrique.setAmount(salary.getValeurRubriquePartage().getAmount());
		else
			rubrique.setAmount(rubrique.getAmount() + salary.getValeurRubriquePartage().getAmount());
		
		//rubrique.setAmount(ClsStringUtil.truncateToXDecimal(this.getAmount(),3).doubleValue());
		//rubrique.setRates(ClsStringUtil.truncateToXDecimal(this.getRates(),salary.parameter.dossierNbreDecimale).doubleValue());
		//rubrique.setBasePlafonnee(ClsStringUtil.truncateToXDecimal(this.getBasePlafonnee(),salary.parameter.dossierNbreDecimale).doubleValue());
		//rubrique.setBase(ClsStringUtil.truncateToXDecimal(this.getBase(),salary.parameter.dossierNbreDecimale).doubleValue());
		
		
		//
		// -------------------------------------------------------------------------
		// -- Insertion dans pacalc
		// -------------------------------------------------------------------------
		// ClsRubriqueClone.derniereLigne ++;
		salary.incrementerDerniereLigne();
		//
		if (salary.getParameter().isUseRetroactif())
		{
//			Rhtprcalc oRhtprcalc = new Rhtprcalc();
//			RhtprcalcPK key = new RhtprcalcPK(salary.getInfoSalary().getComp_id().getCdos(), salary.getInfoSalary().getComp_id().getNmat(), salary.getMonthOfPay(), salary.getNbul(), salary.getDerniereLigne(), rubrique
//					.getRubrique().getComp_id().getCrub(), salary.getNlot());
//			oRhtprcalc.setArgu(salary.getEvparam().getArgu());
//			oRhtprcalc.setBasc(new BigDecimal(salary.getValeurRubriquePartage().getBase()));
//			oRhtprcalc.setBasp(new BigDecimal(salary.getValeurRubriquePartage().getBasePlafonnee()));
//			oRhtprcalc.setClas(salary.getParameter().getClas());
//			oRhtprcalc.setMont(new BigDecimal(salary.getValeurRubriquePartage().getAmount()));
//			oRhtprcalc.setComp_id(key);
//			oRhtprcalc.setNprt(salary.getEvparam().getNprt());
//			oRhtprcalc.setRuba("");
//			oRhtprcalc.setTaux(new BigDecimal(salary.getValeurRubriquePartage().getRates()));
//			oRhtprcalc.setTrtb("1");
//			//
//			salary.getService().save(oRhtprcalc);
		}
		else
		{
			CalculPaie oCalculPaie = new CalculPaie();
			oCalculPaie.setNlig(salary.getDerniereLigne());
			oCalculPaie.setNbul(salary.getNbul());
			oCalculPaie.setAamm(salary.getMonthOfPay());
			oCalculPaie.setNmat(salary.getInfoSalary().getComp_id().getNmat());
			oCalculPaie.setRubq(rubrique.getRubrique().getComp_id().getCrub());
			oCalculPaie.setIdEntreprise(Integer.valueOf(salary.getInfoSalary().getComp_id().getCdos()));

			oCalculPaie.setArgu(salary.getEvparam().getArgu());
			oCalculPaie.setBasc(new BigDecimal(salary.getValeurRubriquePartage().getBase()));
			oCalculPaie.setBasp(new BigDecimal(salary.getValeurRubriquePartage().getBasePlafonnee()));
			oCalculPaie.setClas(salary.getParameter().getClas());
			oCalculPaie.setMont(new BigDecimal(salary.getValeurRubriquePartage().getAmount()));
			oCalculPaie.setNprt(salary.getEvparam().getNprt());
			oCalculPaie.setRuba("");
			oCalculPaie.setTaux(new BigDecimal(salary.getValeurRubriquePartage().getRates()));
			oCalculPaie.setTrtb("1");
			//
			//salary.parameter.dossierNbreDecimale
			oCalculPaie.setBasc(ClsStringUtil.truncateToXDecimal(oCalculPaie.getBasc(),3));
			oCalculPaie.setBasp(ClsStringUtil.truncateToXDecimal(oCalculPaie.getBasp(),3));
			oCalculPaie.setTaux(ClsStringUtil.truncateToXDecimal(oCalculPaie.getTaux(),3));
			oCalculPaie.setMont(ClsStringUtil.truncateToXDecimal(oCalculPaie.getMont(),3));
			
			salary.getService().save(oCalculPaie);
		}
		return true;
	}

	/**
	 * calcule l'arrondi en fonction des param�tres lus dans la table 63
	 * 
	 * @param parameter
	 *            les param�tres g�n�raux du calcul de la paie
	 * @param montant
	 *            montant qu'on veut arrondir
	 * @return l'arrondi
	 */
	public double calculateArrondi(ClsParameterOfPay parameter, double montant)
	{
		// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : >>calculateArrondi");
		// w_decarro NUMBER(32,16);
		// int_mon INTEGER;
		// dec_mon NUMBER(16,0);
		//
		// BEGIN
		// -- Lecture du type d'arrondi
		// wfnom.lib2 := paf_lecfnomL(63,t_rub.arro,2,w_aamm,w_nlot,wsd_fcal1.nbul);
		// String libelle2 = salary.getUtilNomenclature().getLabelFromNomenclature(salary.getParameter().getDossier(), 63, rubrique.getArro(), 2,
		// salary.getNlot(), salary.getNbul(), salary.getMonthOfPay(), parameter.getPeriodOfPay());
		String libelle2 = salary.getUtilNomenclature().getLabelArrondi(salary.getParameter().getListOfArrondi(), salary.getParameter().getDossier(), 63, rubrique.getArro(), 2, salary.getNlot(), salary.getMonthOfPay(),
				parameter.getPeriodOfPay());
		// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : Libelle 2 = "+libelle2);
		//
		// IF wfnom.lib2 IS NULL THEN
		// err_msg := PA_PAIE.erreurp('ERR-90050',w_clang,t_rub.crub,wrubq.arro);
		// Pb_Calcul := TRUE;
		// RETURN;
		// END IF;
		if (ClsObjectUtil.isNull(libelle2))
		{
			parameter.setError(parameter.errorMessage("ERR-90050", parameter.getLangue(), rubrique.getComp_id().getCrub(), rubrique.getArro()));
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\n--In Rubrique Clone : " + "\n" + parameter.getError());
			parameter.setPbWithCalulation(true);
			// return 0;
			return montant;
			// on doit retourner le montant tel quel dans ce cas
		}
		//
		// -- Lecture de la base de l'arrondi
		// wfnom.tau1 := paf_lecfnomT(63,t_rub.arro,1,w_aamm,w_nlot,wsd_fcal1.nbul);
		// double taux = salary.getUtilNomenclature().getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,salary.getParameter().getDossier(), 63, rubrique.getArro(), 1,
		// salary.getNlot(), salary.getNbul(), salary.getMonthOfPay(), parameter.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.RATES);
		tempnumber = salary.getUtilNomenclature().getTauxArrondi(salary.getParameter().getListOfArrondi(), salary.getParameter().getDossier(), 63, rubrique.getArro(), 1, salary.getNlot(), salary.getMonthOfPay(),
				parameter.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.RATES);
		// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : taux 1 = "+taux);
		// IF wfnom.tau1 IS NULL OR
		// wfnom.tau1 = 0
		// THEN
		// err_msg := PA_PAIE.erreurp('ERR-90051',w_clang,t_rub.crub,wrubq.arro);
		// Pb_Calcul := TRUE;
		// RETURN;
		// END IF;
		if (tempnumber == null || tempnumber.doubleValue() == 0)
		{
			parameter.setError(parameter.errorMessage("ERR-90051", parameter.getLangue(), rubrique.getComp_id().getCrub(), rubrique.getArro()));
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\n--In Rubrique Clone : " + "\n" + parameter.getError());
			parameter.setPbWithCalulation(true);
			// return 0;
			return montant;
			
		}

		double taux = tempnumber.doubleValue();
		//
		// -- Calcul de l'arrondi
		// w_decarro := p_mon / wfnom.tau1;
		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "--Calcul de l'arrondi du montant " + montant + " Taux param�tr� = " + taux);
		Double result = montant / taux;

		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "--Calcul de l'arrondi du montant " + montant + " montat/taux = " + result);
		// IF wfnom.lib2 = 'I' THEN
		// int_mon := TRUNC(w_decarro);
		// dec_mon := int_mon;
		// ELSIF wfnom.lib2 = 'S' THEN
		// int_mon := TRUNC(w_decarro) + 1;
		// dec_mon := int_mon;
		// ELSIF wfnom.lib2 = 'N' THEN
		// dec_mon := w_decarro;
		// ELSE
		// err_msg := PA_PAIE.erreurp('ERR-90052',w_clang,t_rub.crub,wrubq.arro);
		// RETURN;
		// END IF;
		double partieEntiereResult = 0;
		if ("I".equals(libelle2))
		{
			//partieEntiereResult = Math.floor(result);
			partieEntiereResult = result.intValue();
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "Arrondi � I, partie entiere +Math.floor(result) " + partieEntiereResult);
		}
		else if ("S".equals(libelle2))
		{
			//partieEntiereResult = Math.floor(result) + 1;
			partieEntiereResult = result.intValue() + 1;
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "Arrondi � S, partie entiere +Math.floor(result) +1 " + partieEntiereResult);
		}
		else if ("N".equals(libelle2))
		{
			// partieEntiereResult = result;
			partieEntiereResult = Math.round(result);
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "Arrondi � N, partie entiere +Math.floor(result) " + partieEntiereResult);
		}
		else
		{
			return montant;
		}
		// parameter.setError(parameter.errorMessage("ERR-90052", parameter.getLangue(), rubrique.getComp_id().getCrub(), rubrique.getArro()));
		// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : "+"\n"+parameter.getError());
		// parameter.setPbWithCalulation(true);
		// return 0;
		// }
		// p_mon := dec_mon * wfnom.tau1;
		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\n--In Rubrique Clone : Arrondi Final partieEntiereResult * taux = " + partieEntiereResult * taux);
		return partieEntiereResult * taux;
	}

	/**
	 * => cal_base Calcul de la base de cette rubrique Le calcul tient compte de tous les �l�ments variables du salari�
	 */
	public void calculateBase()
	{
		// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : >>calculateBase");
		// CURSOR curs_evar IS
		// SELECT mont, argu, nprt, ruba FROM paevar
		// WHERE cdos = PA_CALCUL.wpdos.cdos
		// AND nmat = PA_CALCUL.wsal01.nmat
		// AND aamm = PA_CALCUL.w_aamm
		// AND nbul = PA_CALCUL.wsd_fcal1.nbul
		// AND rubq = PA_CALCUL.t_rub.crub;
		//
		// String queryString = "select mont, argu, nprt, ruba from Rhteltvardet"
		// + " where cdos = '" + salary.getParameter().getDossier() + "'"
		// + " and nmat = '" + salary.getInfoSalary().getComp_id().getNmat() + "'"
		// + " and aamm = '" + salary.getMonthOfPay() + "'"
		// + " and nbul = " + salary.getNbul()
		// + " and rubq = '" + rubrique.getComp_id().getCrub() + "'";
		// CURSOR curs_evar2 IS
		// SELECT mont, argu, nprt, ruba FROM pahevar
		// WHERE cdos = PA_CALCUL.wpdos.cdos
		// AND nmat = PA_CALCUL.wsal01.nmat
		// AND aamm = PA_CALCUL.w_aamm
		// AND nbul = PA_CALCUL.wsd_fcal1.nbul
		// AND rubq = PA_CALCUL.t_rub.crub;
		// String queryStringRetro = "select mont, argu, nprt, ruba from Rhthevar"
		// + " where cdos = '" + salary.getParameter().getDossier() + "'"
		// + " and nmat = '" + salary.getInfoSalary().getComp_id().getNmat() + "'"
		// + " and aamm = '" + salary.getMonthOfPay() + "'"
		// + " and nbul = " + salary.getNbul()
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
		this.salary.getValeurRubriquePartage().setBase(0);
		// this.salary.getValeurRubriquePartage().setAmount(0);
		// this.salary.getValeurRubriquePartage().setBasePlafonnee(0);
		// this.salary.getValeurRubriquePartage().setRates(0);
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
			if (!salary.getListLignePretMap().isEmpty() && salary.getListLignePretMap().containsKey(rubrique.getComp_id().getCrub()))
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
		// if(salary.getParameter().isUseRetroactif())
		// listOfElementVariable = salary.getService().find(queryStringRetro);
		// else
		// listOfElementVariable = salary.getService().find(queryString);
		// IF PA_CALCUL.retroactif THEN
		// CLOSE curs_evar2;
		// ELSE
		// CLOSE curs_evar;
		// END IF;
		//
		if (!salary.getListOfEltvar().isEmpty() && salary.getListOfEltvar().containsKey(rubrique.getComp_id().getCrub()))
			listOfElementVariable = salary.getListOfEltvar().get(rubrique.getComp_id().getCrub());
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
			// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : >>...listOfElementVariable.size() ok");
			Object montantEV = obj[0];
			if (montantEV == null)
			{
				this.salary.parameter.setPbWithCalulation(true);
				String errorMessage = "Sal. " + this.salary.infoSalary.getComp_id().getNmat();
				errorMessage += " EV." + this.rubrique.getComp_id().getCrub();
				errorMessage += " Montant null";
				this.salary.parameter.setError(errorMessage);
				this.salary.parameter.insererLogMessage(errorMessage);
				// return;
			}
			else
			{
				this.salary.getValeurRubriquePartage().setBase(((BigDecimal) (obj[0] != null ? obj[0] : 0)).doubleValue());
				this.setElementVariableValeur(((BigDecimal) (obj[0] != null ? obj[0] : 0)).doubleValue());
				this.setElementVariableTransit(0);
				this.setElementVariableApply(true);
				this.setRubriqueEV(true);
				//@Ajout le 05/04/2011
				if (!ClsObjectUtil.isNull(obj[1]))
					salary.evparam.setArgu((String) obj[1]);
				if (!ClsObjectUtil.isNull(obj[2]))
					salary.evparam.setNprt((String) obj[2]);
				if (!ClsObjectUtil.isNull(obj[3]))
					salary.evparam.setRuba((String) obj[3]);
			}
		}
		// END IF;
		//
		// -----------------------------------------------------------------------------
		// -- RECHERCHE ELEMENT PERSONNEL
		// -----------------------------------------------------------------------------
		// IF NOT PA_CALCUL.appl_elmt OR PA_CALCUL.t_rub.algo = 28
		// THEN
		if (!this.isElementVariableApply() || rubrique.getAlgo() == 28)
		{
			// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : >>...algo=28 ok (pas d'�l�ments variables)");
			// IF PA_CALCUL.retroactif THEN
			if (salary.getParameter().isUseRetroactif())
			{
				// -- test si donnee evenement
				tempnumber = salary.getUtilNomenclature().getAmountOrRateFromSalaryEventData(salary.getParameter().getDossier(), salary.getInfoSalary().getComp_id().getNmat(), salary.getNlot(),
						"R".concat(rubrique.getComp_id().getCrub()));
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
//					Rhthelfix obj = (Rhthelfix) salary.getService().get(Rhthelfix.class,
//							new RhthelfixPK(salary.getParameter().getDossier(), salary.getInfoSalary().getComp_id().getNmat(), rubrique.getComp_id().getCrub(), salary.getMonthOfPay(), salary.getNbul()));
//					if (obj != null)
//					{
//						this.setElementVariableTransit(obj.getMonp().doubleValue());
//						this.salary.getValeurRubriquePartage().setBase(obj.getMonp().doubleValue());
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
				// " where cdos = '" + salary.getParameter().getDossier() + "'" +
				// " and nmat = '" + salary.getInfoSalary().getComp_id().getNmat() + "'" +
				// " and codp = '" + rubrique.getComp_id().getCrub() + "'" +
				// " and (ddeb is null or ((ddeb is not null) and (ddeb <= '" + new ClsDate(salary.getMyMonthOfPay().getFirstDayOfMonth()).getDateS() + "')))" +
				// " and (dfin is null or ((dfin is not null) and (dfin >= '" + new ClsDate(salary.getMyMonthOfPay().getFirstDayOfMonth()).getDateS() + "')))";
				List l = new ArrayList();
				if (!salary.getListOfEltfix().isEmpty() && salary.getListOfEltfix().containsKey(rubrique.getComp_id().getCrub()))
					l = salary.getListOfEltfix().get(rubrique.getComp_id().getCrub());
				if (l != null && l.size() > 0)
				{
					if (l.get(0) != null)
					{
						double d = ClsObjectUtil.getDoubleFromObject(l.get(0));
						this.setElementVariableTransit(d);
						this.salary.getValeurRubriquePartage().setBase(d);
						this.setElementVariableValeur(d);
						this.setElementVariableApply(true);
						this.setRubriqueEF(true);
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
			// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : >>...algo=6,18,28 ok");
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
				// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : >>...basc=O ok");
				if ("N".equals(rubrique.getTrtc()))
				{
					// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : >>...trtc=N ok");
					this.salary.getValeurRubriquePartage().setBase(this.calculateBaseMonthOfPay());
				}
				else
				{
					this.salary.getValeurRubriquePartage().setBase(this.calculateBaseOnCumul());
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
		// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : ...............ElementVariableApply :" +
		// isElementVariableApply());
		// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : ...getBase : " +
		// this.salary.getValeurRubriquePartage().getBase());
		// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : <<Fin calculateBase\n");
	}
	
	/**
	 * Retourner la chaine correspondant � la liste des p�riodes � traiter pour les cumuls du salari�
	 */

	private String _computerMonthToCumul(String firstMonth, int numberOfMonth, int i_annee, int j_mois)
	{

		String moismin = this.salary._computeMinOfMonthForCumul();
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

		for (int idx = 0; idx <= nmois; idx++)
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

				if (moi1.equals(salary.getMonthOfPay()))
				{
					if ("O".equals(rubrique.getMopa()))
					{
						result += ",'" + moi1 + "'";
					}
				}
				else
				{
					if ((moi1.compareToIgnoreCase(salary.getMonthOfPay()) < 0) && (moi1.compareToIgnoreCase(moismin) >= 0))
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

			moi1 = ClsStringUtil.formatNumber(i_annee, ClsParameterOfPay.FORMAT_OF_YEAR) + ClsStringUtil.formatNumber(j_mois, "00");
		}
		if ("O".equals(rubrique.getMopa()) && ("N".equals(rubrique.getTrve()) || "C".equals(rubrique.getTrve()) || "M".equals(rubrique.getTrve()) || "X".equals(rubrique.getTrve())))
		{
			result += ",'" + salary.getMonthOfPay() + "'";
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

	private ClsCodeLibelle whereQueryForCumulBase(String firstMonth, int numberOfMonth, int i_annee, int j_mois)
	{
		String rubk = "";

		String result = "";

		int index = -1;

		List<String> resultList = new ArrayList<String>();
		String nouvelleFormule = rubrique.getFormule();
		if(salary.parameter.formulelitterale)
		{
			List<String> rubriques = ExpressionEvaluator.getRubriques(nouvelleFormule);
			for (String object : rubriques)
			{
				index++;
				if (index % 100 == 0 && index != 0)
				{
					resultList.add(result);
					result = "";
				}
	
				rubk = object;
				if (!Character.isLetter(rubk.toCharArray()[0]))
				{
					result += ",'" + rubk + "'";
				}
			}
		}
		else
		{
			List<Object[]> listOfRubBase = salary.getParameter().getListOfRubqBase().get(rubrique.getComp_id().getCrub());
			
			if (listOfRubBase == null)
				listOfRubBase = new ArrayList<Object[]>();
			for (Object[] object : listOfRubBase)
			{
				index++;
				if (index % 100 == 0 && index != 0)
				{
					resultList.add(result);
					result = "";
				}
	
				rubk = (String) object[2];
				if (!Character.isLetter(rubk.toCharArray()[0]))
				{
					// ClsRubriqueClone rubriqueClone = salary.findRubriqueClone(rubk);
					// if(rubriqueClone != null)
					// {
					// result +=",'"+rubk+"'";
					// }
					result += ",'" + rubk + "'";
				}
			}
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
				result = " ( comp_id.rubq in (" + string + ")";
			else
				result += " or comp_id.rubq in (" + string + ")";
		}
		if (StringUtils.isNotBlank(result))
			result += " )";
		else
			result="-1=1";

		String finalResult = this._computerMonthToCumul(firstMonth, numberOfMonth, i_annee, j_mois);

		String code = "O";
		if (StringUtils.isNotBlank(finalResult))
		{
			if (StringUtils.isNotBlank(result))
				finalResult += " and " + result;
		}
		else
		{
			code = "N";
			finalResult = result;
		}

		if (StringUtils.isBlank(finalResult))
			finalResult = " (1=1) ";

		return new ClsCodeLibelle(code, finalResult);
	}
	
	private Map<String, String> mapMoisValides = new HashMap<String, String>();

	/**
	 * => base_cumul Calcule la base sur les cumuls
	 * 
	 * @return le montant de la base sur les cumuls
	 */
	private double calculateBaseOnCumul()
	{
		
		mapMoisValides = new HashMap<String, String>();
		
		ClsDate myExercice = new ClsDate(salary.getParameter().getDossierDateDebutExercice(), this.salary.getParameter().getAppDateFormat());

		ClsDate myPreviousMontOfPay = new ClsDate(new ClsDate(salary.getParameter().getMyMoisPaieCourant().getDate()).addMonth(-1));

		int i = 0; // 2007
		int j = 0; // 8
		int k = 0;
		int l = 0;
		int nmois = 0;
		String moi1 = "";
		// -- ----- Initialisations
		// w_bas := 0;
		this.salary.getValeurRubriquePartage().setBase(0);
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
			{// je suis au j-i�me mois de l'ann�e suivante
				j = j - 12;
				i = i + 1;// contient le nombre d'ann�es
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

			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\nInit, ann�e = " + i);
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\nInit, mois = " + j);

			k = 0;
			if (!ClsObjectUtil.isNull(rubrique.getVal3()))
				k = Integer.valueOf(rubrique.getVal3());
			
			//-- ----- Cas du calcul des cong�s CNSS
//			if(StringUtils.equals(salary.parameter.nomClient, ClsEntreprise.CNSS))
//			{
			
			//			IF PA_CALCUL.T_RUB.ALGO = 68 AND NOT PA_PAIE.NOUB(PA_CALCUL.WSAL01.PMCF) THEN
			//	         I := TO_NUMBER(SUBSTR(PA_CALCUL.WSAL01.PMCF,1,4));  -- ANNEE
			//	         J := TO_NUMBER(SUBSTR(PA_CALCUL.WSAL01.PMCF,5,2));  -- MOIS
			//
			//	         J := J - 1;
			//	         IF J < 1 THEN
			//	            J := J + 12;
			//	            I := I - 1;
			//	         END IF;
			//	      END IF;
//		      if(rubrique.getAlgo() == 68 && StringUtils.isNotBlank(salary.infoSalary.getPmcf()))
//		      {
//		    	   ClsDate dtPMCF = new ClsDate(salary.infoSalary.getPmcf(), ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
//		    	   i = dtPMCF.getYear();
//		    	   j = dtPMCF.getMonth();
//
//		    	   j = j -1;
//		    	   while (j < 1)
//					{
//						j = j + 12;
//						i = i - 1;
//					}
//		      }
//			}
			
			
			

			// j := j - k + 1;
			j = j - k + 1;// et si j -k <= -2
			// IF j < 1 THEN
			// j := j + 12;
			// i := i - 1;
			// END IF;
			while (j < 1)
			{// je suis au j-i�me mois de l'ann�e pr�c�dante
				j = j + 12;
				i = i - 1;// 2007-1=2006
			}
			//
			// moi1 := LTRIM(TO_CHAR(i,'0999'))||LTRIM(TO_CHAR(j,'09'));
			// nb_mois := k;

			nmois = k;

		}
		/**
		 * Cas du cumul sur un cong�, on d�termine le nombre de mois d�puis la date de dernier retour de cong� Si la date de retour du dernier cong� est null,
		 * on prend la date d'entr�e dans la soci�t�
		 */
		if ("C".equals(rubrique.getTrve()))
		{
//			if(StringUtils.equals(salary.parameter.getNomClient(), ClsEntreprise.TASIAST_MAURITANIE))
//			{
//				ClsDate oDrtcg = new ClsDate(salary.getInfoSalary().getDrtcg() != null ? salary.getInfoSalary().getDrtcg() : salary.getInfoSalary().getDtes());
//
//				moi1 = oDrtcg.getYearAndMonth();
//				i = oDrtcg.getYear();
//				j = oDrtcg.getMonth();
//				if(this.salary.parameter.table91LabelNotEmpty)
//				{
//					//DockerSISVImpl docker= (DockerSISVImpl) ServiceFinder.findBean("DockerSISV");
//					String moi11 = salary.getService().getMoisPaieReel(salary.parameter.dossier, oDrtcg.getDateS("dd/MM/yyyy"));
//					if(StringUtil.notEquals("0", moi11))
//					{
//						moi1 = moi11;
//						i = new ClsDate(moi11,ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getYear();
//						j = new ClsDate(moi11,ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getMonth();
//					}
//				}
//				nmois = ClsDate.getMonthsBetween(oDrtcg.getDate(), salary.getParameter().getFirstDayOfMonth());
//			}
//			else
//			{

				ClsDate oDrtcg = new ClsDate(salary.getInfoSalary().getDrtcg() != null ? salary.getInfoSalary().getDrtcg() : salary.getInfoSalary().getDtes());

				moi1 = oDrtcg.getYearAndMonth();
				i = oDrtcg.getYear();
				j = oDrtcg.getMonth();
				if (this.salary.parameter.table91LabelNotEmpty)
				{
					//DockerSISVImpl docker = (DockerSISVImpl) ServiceFinder.findBean("DockerSISV");
					String moi11 = salary.getService().getMoisPaieReel(salary.parameter.dossier, oDrtcg.getDateS("dd/MM/yyyy"));
					if (StringUtil.notEquals("0", moi11))
					{
						moi1 = moi11;
						i = new ClsDate(moi11, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getYear();
						j = new ClsDate(moi11, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getMonth();
					}
				}
				Integer diviseur = 30;
				if ("O".equals(salary.parameter.getBase30Rubrique()))
				{
					if (salary.parameter.getBase30NombreJour() > 0)
						diviseur = salary.parameter.getBase30NombreJour();
					else diviseur = 30;
				}
				else diviseur = salary.parameter.getMyMonthOfPay().getMaxDayOfMonth();

				/*double tdays = ClsDate.getNomberOfDaysBetween(oDrtcg.getDate(), salary.getParameter().getFirstDayOfMonth());
				double tnmois = tdays/diviseur;
				nmois = Double.valueOf(Math.ceil(tnmois)).intValue();*/
				String cmois = oDrtcg.getYearAndMonth();
				while (cmois.compareTo(salary.parameter.getMoisPaieCourant()) < 0)
				{
					cmois = ClsDate.getDateS(new ClsDate(cmois, "yyyyMM").addMonth(1), "yyyyMM");
					nmois++;
				}
				nmois++;

				//-- ----- Cas du calcul des cong�s SABC (si la pr�sence est supp�rieur � 12 mois
				//          on ne consid�re que les 12 derniers mois)
				//			if(StringUtils.equals(salary.parameter.nomClient, ClsEntreprise.SABC))
				//			{

				k = 12;
				//				if (!ClsObjectUtil.isNull(rubrique.getVal3()))
				//					k = Integer.valueOf(rubrique.getVal3());	

				if (nmois > 12 && k != 0)
				{

					i = myPreviousMontOfPay.getYear();

					j = myPreviousMontOfPay.getMonth();

					// j := j - k + 1;
					j = j - k + 1;// et si j -k <= -2
					// IF j < 1 THEN
					// j := j + 12;
					// i := i - 1;
					// END IF; 
					while (j < 1)
					{// je suis au j-i�me mois de l'ann�e pr�c�dante
						j = j + 12;
						i = i - 1;// 2007-1=2006
					}

					nmois = k;
				}
				//			} 
			//}
		}
		//Cumul uniquement du (mois en cours - valeur stock�e en val3)
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
			//On fixe le nombre de mois � 1 vu qu'on ne veut qu'un mois de cumul
			nmois = 1;

		}
		//Cumul uniquement du mois stock� en val3
		if ("X".equals(rubrique.getTrve()))
		{
			k = 0;
			if (!ClsObjectUtil.isNull(rubrique.getVal3()))
				k = Integer.valueOf(rubrique.getVal3());
			if(k+"".length() != 6) k = salary.getParameter().getMyMoisPaieCourant().getYearAndMonthInt();
			
			i = new ClsDate(k+"","yyyyMM").getYear();
			j = new ClsDate(k+"","yyyyMM").getMonth();
			
			//On fixe le nombre de mois � 1 vu qu'on ne veut qu'un mois de cumul
			nmois = 1;
		}
		moi1 = ClsStringUtil.formatNumber(i, ClsParameterOfPay.FORMAT_OF_YEAR) + ClsStringUtil.formatNumber(j, "00");

		// END IF;
		//
		// ---------------------------------------------------------------------------------
		// -- Chargement du tableau, une base par mois
		// ---------------------------------------------------------------------------------
		//
		// moi := moi1;
		// i := TO_NUMBER(SUBSTR(moi1,5,2));
		// j := TO_NUMBER(SUBSTR(moi1,1,4));
		// on permute i et j de tel sorte que j devient l'ann�e et i le mois
		k = i;
		i = j;
		j = k;

		/**
		 * C'est ici qu'on va recup�rer les cumuls pour la rubrique courante et pour le salarie
		 */
		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\nPremier Mois pour le calcul du cumul = " + moi1);
		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\nNombre de mois du cumul = " + nmois);
		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\nAnnee depart = " + j + " et mois depart = " + i);
		ClsCodeLibelle codelib = this.whereQueryForCumulBase(moi1, nmois, j, i);

		//this.salary.calculDesCumuls(salary.getParameter().getNumeroBulletin(), codelib.getLibelle(), StringUtils.equals("O", codelib.getCode()));
		//this.salary.calculDesCumuls(salary.getNbul(), codelib.getLibelle(), StringUtils.equals("O", codelib.getCode()));
		this.salary.calculDesCumuls(rubrique.getComp_id().getCrub(),0, codelib.getLibelle(), StringUtils.equals("O", codelib.getCode()));

		//
		// FOR k IN 1..nb_mois LOOP
		Double[] mont = new Double[nmois + 1];
		for (int idx = 0; idx <= nmois; idx++)
		{
			mont[idx] = Double.valueOf(0);
		}
		
		String[] moisCalcules = new String[nmois + 1];
		for (int idx = 0; idx <= nmois; idx++)
		{
			moisCalcules[idx] = "";
		}
		
		boolean moiscourantdejacalcule = false;
		for (int idx = 0; idx < nmois; idx++)
		{
			// IF moi = PA_CALCUL.w_aamm THEN
			moisCalcules[idx]= moi1;
			if (moi1.equals(salary.getMonthOfPay()))
			{
				
				// IF PA_CALCUL.t_rub.mopa = 'O' THEN
				if ("O".equals(rubrique.getMopa()) && !moiscourantdejacalcule)
				{
					moiscourantdejacalcule = true;
					// mont(k) := Calc_formule(moi);
					mont[idx] = this.calculateFormule(moi1, 0);
					// IF PA_PAIE.NouZ(mont(k)) THEN
					// mont(k) := 0;
					// END IF;
					// mont(k) := mont(k) + base_mdp;
					mont[idx] += this.calculateBaseMonthOfPay();
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
				if (moi1.compareToIgnoreCase(salary.getMonthOfPay()) < 0)
					mont[idx] = this.calculateFormule(moi1, 0);
				else
					mont[idx] = null;
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
			moi1 = ClsStringUtil.formatNumber(j, ClsParameterOfPay.FORMAT_OF_YEAR) + ClsStringUtil.formatNumber(i, "00");
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
			mont[nmois - 1] = calculateFormule(salary.getMonthOfPay(), 0);
			mont[nmois - 1] += calculateBaseMonthOfPay();
			moisCalcules[nmois - 1] = salary.getMonthOfPay();
		}
		
		if ('O' == salary.parameter.getGenfile())
			salary.addToOutputtext( "\nNmois final = "+nmois) ;

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
		if ("S".equals(rubrique.getOpfi()))
		{
			totalMontant = 0;
			for (int idx = 0; idx < nmois; idx++)
			{
				if(mont[idx] != null)
				totalMontant += mont[idx];
			}
			this.salary.getValeurRubriquePartage().setBase(totalMontant);
		}
		else if ("O".equals(rubrique.getOpfi()))
		{
			int nombreMoisCorrect = 0;
			totalMontant = 0;
			for (int idx = 0; idx < nmois; idx++)
			{
				if (mont[idx] != null && StringUtils.isNotBlank(moisCalcules[idx]) && mapMoisValides.containsKey(moisCalcules[idx]))
				{
					totalMontant += mont[idx];
					nombreMoisCorrect++;
				}			
			}
			if ('O' == salary.parameter.getGenfile())
				salary.addToOutputtext( "\nTotal = "+totalMontant+" et Nombre de mois correct = " + nombreMoisCorrect) ;
			if (nombreMoisCorrect != 0)
				this.salary.getValeurRubriquePartage().setBase(totalMontant / nombreMoisCorrect);
			else
				this.salary.getValeurRubriquePartage().setBase(0);
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
		// this.salary.getValeurRubriquePartage().setBase(max);
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
		// // if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : .............nombreMoisCorrect: " + nombreMoisCorrect);
		// if (nombreMoisCorrect != 0)
		// this.salary.getValeurRubriquePartage().setBase((totalMontant / nombreMoisCorrect) * 12);
		// else
		// this.salary.getValeurRubriquePartage().setBase(0);
		// }
		else if ("M".equals(rubrique.getOpfi())) //Maximum
		{
			double max = 0;
			for (int idx = 0; idx < nmois; idx++)
			{
				if (mont[idx] != null && mont[idx] > max)
				{
					max = mont[idx];
				}
			}
			this.salary.getValeurRubriquePartage().setBase(max);
		}
		else if ("A".equals(rubrique.getOpfi())) // --Moyenne arithmetique
		{
			int nombreMoisCorrect = 0;
			totalMontant = 0;
			for (int idx = 0; idx < nmois; idx++)
			{
				if(mont[idx] != null)
				{
					totalMontant += mont[idx];
					nombreMoisCorrect++;
				}
			}
			if ('O' == salary.parameter.getGenfile())
				salary.addToOutputtext( "\nTotal = "+totalMontant+" et Nombre de mois correct = " + nombreMoisCorrect) ;
			if (nombreMoisCorrect != 0)
				this.salary.getValeurRubriquePartage().setBase(totalMontant / nombreMoisCorrect);
			else
				this.salary.getValeurRubriquePartage().setBase(0);
		}
		//
		// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : base :" +
		// this.salary.getValeurRubriquePartage().getBase());
		mapMoisValides = null;
		
		return this.salary.getValeurRubriquePartage().getBase();
	}

	/**
	 * Calcule la base en fonction des bases d�j� calcul�es
	 * 
	 * @param moi1
	 *            la p�riode
	 * @return le montant de la base
	 */
	private double calculateFormule(String moi1, int nbul)
	{
		List<Object[]> listOfRubBase = new ArrayList<Object[]>();
		if (!salary.getParameter().getListOfRubqBase().isEmpty() && salary.getParameter().getListOfRubqBase().containsKey(rubrique.getComp_id().getCrub()))
			listOfRubBase = salary.getParameter().getListOfRubqBase().get(rubrique.getComp_id().getCrub());
		//
		String rubk = "";
		String sign = "";
		String formule = null;
		List<Expression> parametres = new ArrayList<Expression>();
		double valeur = 0;
		double base = 0;
		
		/************GESTION DE LA NOUVELLE FORMULE STOCKE DANS LA ZONE NOTE DE LA RUBRIQUE*************/
		String nouvelleFormule = rubrique.getFormule();
		if(salary.parameter.formulelitterale)
		{
			List<String> rubriques = ExpressionEvaluator.getRubriques(nouvelleFormule);
			for(String crub : rubriques)
			{
				rubk = crub;
				if (Character.isLetter(rubk.toCharArray()[0]))
					valeur = 0;
				else
				{
					
					if ("B".equals(rubrique.getLbtm()))
						valeur = salary.getUtilNomenclature().getCumul(salary, moi1, rubk, nbul, ClsEnumeration.EnTypeOfColumn.BASE, salary.getMoisPaieCourant(), salary.getMonthOfPay());
					else if ("T".equals(rubrique.getLbtm()))
						valeur = salary.getUtilNomenclature().getCumul(salary, moi1, rubk, nbul, ClsEnumeration.EnTypeOfColumn.RATES, salary.getMoisPaieCourant(), salary.getMonthOfPay());
					else if ("M".equals(rubrique.getLbtm()))
						valeur = salary.getUtilNomenclature().getCumul(salary, moi1, rubk, nbul, ClsEnumeration.EnTypeOfColumn.AMOUNT, salary.getMoisPaieCourant(), salary.getMonthOfPay());
					else
						valeur = 0;
					
				}
				if(valeur != 0) mapMoisValides.put(moi1, moi1);
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
				valeur = 0;
			else
			{
				
				if ("B".equals(rubrique.getLbtm()))
					valeur = salary.getUtilNomenclature().getCumul(salary, moi1, rubk, nbul, ClsEnumeration.EnTypeOfColumn.BASE, salary.getMoisPaieCourant(), salary.getMonthOfPay());
				else if ("T".equals(rubrique.getLbtm()))
					valeur = salary.getUtilNomenclature().getCumul(salary, moi1, rubk, nbul, ClsEnumeration.EnTypeOfColumn.RATES, salary.getMoisPaieCourant(), salary.getMonthOfPay());
				else if ("M".equals(rubrique.getLbtm()))
					valeur = salary.getUtilNomenclature().getCumul(salary, moi1, rubk, nbul, ClsEnumeration.EnTypeOfColumn.AMOUNT, salary.getMoisPaieCourant(), salary.getMonthOfPay());
				else
					valeur = 0;
				
			}
			if(valeur != 0) mapMoisValides.put(moi1, moi1);
			
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
	 * @return montant de la base calcul�e
	 */
	private double calculateBaseMonthOfPay()
	{
		List<Object[]> listOfRubBase = new ArrayList<Object[]>();
		if (!salary.getParameter().getListOfRubqBase().isEmpty() && salary.getParameter().getListOfRubqBase().containsKey(rubrique.getComp_id().getCrub()))
			listOfRubBase = salary.getParameter().getListOfRubqBase().get(rubrique.getComp_id().getCrub());
		//
		String rubk = "";
		String sign = "";
		String formule = null;
		List<Expression> parametres = new ArrayList<Expression>();
		double valeur = 0;
		double base = 0;
		ClsRubriqueClone rubriqueClone = null;
		if(StringUtils.equals("7050", rubrique.getComp_id().getCrub()))
		{
			//System.out.println("Cas de la 7800");
		}
		/************GESTION DE LA NOUVELLE FORMULE STOCKE DANS LA ZONE NOTE DE LA RUBRIQUE*************/
		String nouvelleFormule = rubrique.getFormule();
		if(salary.parameter.formulelitterale)
		{
			
			if(StringUtils.equals(rubrique.getComp_id().getCrub(), "3260"))
			{
				System.out.println("rubrique test");
			}
			List<String> rubriques = ExpressionEvaluator.getRubriques(nouvelleFormule);
			for(String crub : rubriques)
			{
				rubk = crub;
				if (Character.isLetter(rubk.toCharArray()[0]))
					valeur = calculatePlafond(rubk);
				else
				{
					rubriqueClone = salary.findRubriqueClone(rubk);
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
				}
				salary.getValeurRubriquePartage().setValeur(valeur);
				if(valeur != 0) mapMoisValides.put(salary.parameter.getMonthOfPay(), salary.parameter.getMonthOfPay());
				
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
				// r�cuperer la base, le taux, le montant de la base rubk et
				// affecter � valeur selon le cas
				rubriqueClone = salary.findRubriqueClone(rubk);
				if (rubriqueClone != null)
				{
					
					if ("B".equals(rubrique.getLbtm()))
						valeur = rubriqueClone.getBase();
					else if ("T".equals(rubrique.getLbtm()))
						valeur = rubriqueClone.getRates();
					else
						valeur = rubriqueClone.getAmount();
					salary.getValeurRubriquePartage().setValeur(valeur);
					// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : .............. ...valeur de la rubrique :" +
					// valeur);
					// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : .............. ...sign de la rubrique :" +
					// sign);
					//
				}
				else
					valeur = 0;
			}
			if(valeur != 0) mapMoisValides.put(salary.parameter.getMonthOfPay(), salary.parameter.getMonthOfPay());
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
		// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : >>calculatePlafond");

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
		// -- cl� d'acc�s = les deux premiers char de RUBK
		// w_plafm := paf_lecfnomM(96,char2,1,PA_CALCUL.w_aamm,PA_CALCUL.w_nlot,PA_CALCUL.wsd_fcal1.nbul);
		// w_plaft := paf_lecfnomT(96,char2,1,PA_CALCUL.w_aamm,PA_CALCUL.w_nlot,PA_CALCUL.wsd_fcal1.nbul);
		plafondMontant = salary.getUtilNomenclature().getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,salary.getInfoSalary().getComp_id().getCdos(), 96, char2, 1, salary.getNlot(), salary.getNbul(), salary.getMoisPaieCourant(),
				salary.getMonthOfPay(), ClsEnumeration.EnColumnToRead.AMOUNT);
		plafondTaux = salary.getUtilNomenclature().getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,salary.getInfoSalary().getComp_id().getCdos(), 96, char2, 1, salary.getNlot(), salary.getNbul(), salary.getMoisPaieCourant(),
				salary.getMonthOfPay(), ClsEnumeration.EnColumnToRead.RATES);
		
		// IF w_plafm IS NOT NULL THEN
//		if (StringUtils.equals(salary.getParameter().nomClient, ClsEntreprise.SHELL_GABON))
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
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90081", salary.getParameter().getLangue(), rubrique.getAlgo(), rubrique.getComp_id().getCrub(), char2, salary.getInfoSalary().getComp_id().getNmat()));
			salary.getParameter().setPbWithCalulation(true);
			return 0;
		}
		return plafond;
	}

	/**
	 * => charg_prets permet de constituer la liste des num�ros de prets d'une rubrique
	 * 
	 */
	public void loadListOfLoanNumber()
	{
		// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : >>loadListOfLoanNumber");
		// CURSOR curs_preti IS SELECT lg FROM paprets
		// WHERE cdos = wpdos.cdos
		// AND nmat = wsal01.nmat
		// AND crub = t_rub.crub
		// AND premrb <= w_aamm
		// AND etatpr = 'D'
		// AND ( NVL(mtpr,0) != NVL(mtremb,0)
		// OR ( NVL(mtpr,0) = 0 AND
		// NVL(mtmens,0) != 0 )
		// )
		// ORDER BY lg;
		// String queryString = "select comp_id.lg from Rhtpretsagent" +
		// " where cdos = '" + salary.getParameter().getDossier() + "'" +
		// " and nmat = '" + salary.getInfoSalary().getComp_id().getNmat() + "'" +
		// " and crub = '" + rubrique.getComp_id().getCrub() + "'" +
		// " and premrb < '" + salary.getMonthOfPay() + "'" +
		// " and etatpr = 'D'" +
		// " and ((mtpr is not null and mtremb is not null )" +
		// " and (mtpr != mtremb or (mtpr = 0 and mtmens != 0)))" +
		// " order by lg";
		// String queryString = "select comp_id.lg from Rhtpretsagent" +
		// " where cdos = '" + salary.getParameter().getDossier() + "'" +
		// " and nmat = '" + salary.getInfoSalary().getComp_id().getNmat() + "'" +
		// " and crub = '" + rubrique.getComp_id().getCrub() + "'" +
		// " and premrb < '" + salary.getMonthOfPay() + "'" +
		// " and etatpr = 'D'" +
		// " and (((case when mtpr is null then 0 else mtpr end) != (case when mtremb is null then 0 else mtremb end))" +
		// " or (((case when mtpr is null then 0 else mtpr end) = 0) and ((case when mtmens is null then 0 else mtmens end) != 0)))" +
		// " order by comp_id.lg";
		//
		// CURSOR curs_pretx IS SELECT nprt FROM paprent
		// WHERE paprent.cdos = wpdos.cdos
		// AND paprent.nmat = wsal01.nmat
		// AND paprent.crub = t_rub.crub
		// -- AND paprent.per1 <= w_aamm
		// AND paprent.pact = 'O'
		// AND paprent.etpr = 'D'
		// AND paprent.resr != 0;
		// String queryStringRetro = "select comp_id.nprt from Rhtpretent" +
		// " where cdos = '" + salary.getParameter().getDossier() + "'" +
		// " and nmat = '" + salary.getInfoSalary().getComp_id().getNmat() + "'" +
		// " and crub = '" + rubrique.getComp_id().getCrub() + "'" +
		// // " and per1 <= '" + salary.getMyMonthOfPay().getDateS(ParameterUtil.SESSION_FORMAT_DATE) + "'" +
		// " and pact = 'O'" +
		// " and etpr = 'D'" +
		// " and resr != 0" ;
		//
		// num_pret paprent.nprt%TYPE;
		//
		// BEGIN
		// nb_prets := 0;
		// IF t_rub.algo = 13 THEN listOfLoanNumber
		// List simpleListe = (rubrique.getAlgo() == 13)? salary.getService().find(queryString):salary.getService().find(queryStringRetro);
		// List simpleListe = new ArrayList();
		listOfLoanNumber = new ArrayList<Object>();
		if (rubrique.getAlgo() == 13)
		{
			if (!salary.getListLignePretMap().isEmpty() && salary.getListLignePretMap().containsKey(rubrique.getComp_id().getCrub()))
			{
				List lst = salary.getListLignePretMap().get(rubrique.getComp_id().getCrub());
				for(Object o : lst)
					listOfLoanNumber.add(o);
//				listOfLoanNumber = salary.getListLignePretMap().get(rubrique.getComp_id().getCrub());
			}
		}
		else
		{
			if (!salary.getListNumeroPretMap().isEmpty() && salary.getListNumeroPretMap().containsKey(rubrique.getComp_id().getCrub()))
			{
				List lst = salary.getListNumeroPretMap().get(rubrique.getComp_id().getCrub());
				for(Object o : lst)
					listOfLoanNumber.add(o);
				//listOfLoanNumber = salary.getListNumeroPretMap().get(rubrique.getComp_id().getCrub());		
			}
		}
		
		// Integer row = null;
		// for (Object object : simpleListe) {
		// row = (object instanceof String)? Integer.valueOf(object.toString()) : (Integer)object;
		// if(listOfLoanNumber == null)
		// listOfLoanNumber = new ArrayList<Object>();
		// listOfLoanNumber.add(row);
		// }
		// OPEN curs_preti;
		// LOOP
		// FETCH curs_preti INTO num_pret;
		// EXIT WHEN curs_preti%NOTFOUND;
		// nb_prets := nb_prets + 1;
		// tab_prets(nb_prets) := num_pret;
		// END LOOP;
		// CLOSE curs_preti;
		// ELSE
		// OPEN curs_pretx;
		// LOOP
		// FETCH curs_pretx INTO num_pret;
		// EXIT WHEN curs_pretx%NOTFOUND;
		// nb_prets := nb_prets + 1;
		// tab_prets(nb_prets) := num_pret;
		// END LOOP;
		// CLOSE curs_pretx;
		// END IF;
	}

	/**
	 * retire certains EV qui sont des pr�ts de la liste des pr�ts
	 * 
	 */
	public void removeElementVariableFromLoans()
	{
		// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : >>removeElementVariableFromLoans");
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
		 List<Object> listOfLoanNumberClone = new ArrayList<Object>();
		 for (Object o : listOfLoanNumber) {
		 listOfLoanNumberClone.add(o);
		 }
		//List<Object> listOfLoanNumberClone = listOfLoanNumber;
		for (Object obj : listOfLoanNumberClone)
		{
			// IF retroactif THEN
			if (salary.getParameter().isUseRetroactif())
			{
				// SELECT COUNT(*) INTO nb_ev FROM pahevar
				// WHERE cdos = wpdos.cdos
				// AND nmat = wsal01.nmat
				// AND aamm = w_aamm
				// AND nbul = wsd_fcal1.nbul
				// AND rubq = t_rub.crub
				// AND nprt = LTRIM(TO_CHAR(tab_prets(i),'0999'));
				queryStringRetro = " select count(*) from Rhthevar" + " where cdos = '" + salary.getParameter().getDossier() + "'" + " and nmat = '" + salary.getInfoSalary().getComp_id().getNmat() + "'"
						+ " and aamm = '" + salary.getMonthOfPay() + "'" + " and nbul = " + salary.getNbul() + " and rubq = '" + rubrique.getComp_id().getCrub() + "'" + " and nprt = '"
						+ ClsStringUtil.formatNumber(ClsObjectUtil.getIntegerFromObject(obj), salary.getParameter().FORMAT_PERIOD_OF_RBQ) + "'";
				Object o = (Object) salary.getService().find(queryStringRetro).get(0);
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
				queryString = " select count(*) from Rhteltvardet" + " where cdos = '" + salary.getParameter().getDossier() + "'" + " and nmat = '" + salary.getInfoSalary().getComp_id().getNmat() + "'" + " and aamm = '"
						+ salary.getMonthOfPay() + "'" + " and nbul = " + salary.getNbul() + " and rubq = '" + rubrique.getComp_id().getCrub() + "'" + " and nprt = '"
						+ ClsObjectUtil.getIntegerFromObject(obj) + "'";
				Object o = (Object) salary.getService().find(queryString).get(0);
				if (o != null)
					count = ClsObjectUtil.getIntegerFromObject(o);
			}
			if (count > 0)
				listOfLoanNumber.remove(obj);
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
		//listOfLoanNumber = listOfLoanNumberClone;
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
		// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : >>calculateCumulOfBase");

		ClsRubriqueClone autreRub = null;
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
		int debutPeriodInt = new ClsDate(salary.getParameter().getDossierDateDebutExercice()).getYearAndMonthInt();
		int finPeriodInt = new Integer(salary.getMonthOfPay());
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
		this.salary.getValeurRubriquePartage().setRates(0);
		//
		// IF PA_CALCUL.t_rub.rreg = 'N' OR PA_CALCUL.t_rub.rman = 'O' THEN
		if ("N".equals(this.rubrique.getRreg()) || "O".equals(this.rubrique.getRman()))
		{
			// l_indcf := 1;
			idx = 0;
			// tab_cotf_base(l_indcf) := w_bas;

			regularisation = listOfRegularisationFr.get(idx);
			regularisation.setBase(this.salary.getValeurRubriquePartage().getBase());
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
					if (salary.getParameter().isUseRetroactif())
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
//						oRhtprcumu = (Rhtprcumu) salary.getService().get(Rhtprcumu.class,
//								new RhtprcumuPK(salary.getParameter().getDossier(), salary.getInfoSalary().getComp_id().getNmat(), idxDebutPeriodIntS, this.rubrique.getRcon(), nbul));
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
						oCumulPaie = (CumulPaie) salary.getService().find("FROM CumulPaie WHERE identreprise="+salary.getParameter().getDossier()+
																				" AND nmat='"+salary.getInfoSalary().getComp_id().getNmat()+
																				" AND aamm='"+idxDebutPeriodIntS+"'"+
																				" AND rubq='"+this.rubrique.getRcon()+"'"+
																				" AND nbul="+nbul);

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
					// @23-07-2009 by yannick
					if (objectIsNotNull == true && l_bas != 0 && l_tau != 0)
					{
						// if(objectIsNotNull == true
						// && l_bas > 0 && l_tau > 0){
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
						this.salary.getValeurRubriquePartage().setBase(this.salary.getValeurRubriquePartage().getBase() + l_bas);
						// tab_cotf_base(l_indcf) := tab_cotf_base(l_indcf) + l_bas;
						regularisation.setBase(regularisation.getBase() + l_bas);
						// tab_cotf_cot (l_indcf) := tab_cotf_cot (l_indcf) + l_mon;
						regularisation.setCotisation(regularisation.getCotisation() + l_mon);
						// l_mon := paf_lecCum(PA_CALCUL.wsal01.nmat,l_cam,PA_CALCUL.t_rub.crub,l_nbul,'M',PA_CALCUL.w_aamm);
						l_mon = salary.getUtilNomenclature().getCumul(salary, idxDebutPeriodIntS, rubrique.getComp_id().getCrub(), nbul, ClsEnumeration.EnTypeOfColumn.AMOUNT, salary.getMoisPaieCourant(),
								salary.getMonthOfPay());
						// tab_cotf_reg(l_indcf) := tab_cotf_reg(l_indcf) + NVL(l_mon,0);
						regularisation.setRegularisation(regularisation.getRegularisation() + l_mon);
						//
						// IF l_cam != w_aamm THEN
						if (idxDebutPeriodIntS != null && (!idxDebutPeriodIntS.equals(salary.getMonthOfPay())))
						{
							//
							// l_mon := paf_lecCum(PA_CALCUL.wsal01.nmat,l_cam,PA_CALCUL.rub_plaf,l_nbul,'M',PA_CALCUL.w_aamm);
							l_mon = salary.getUtilNomenclature().getCumul(salary, idxDebutPeriodIntS, rubrique.getComp_id().getCrub(), nbul, ClsEnumeration.EnTypeOfColumn.AMOUNT, salary.getMoisPaieCourant(),
									salary.getMonthOfPay());
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
			autreRub = salary.findRubriqueClone(rubrique.getRcon());
			//
			// IF PA_CALCUL.c_taux != w_tau THEN
			// l_indcf := l_indcf + 1;
			// END IF;
			if (autreRub.getRates() != this.salary.getValeurRubriquePartage().getRates())
				// if(rates != this.salary.getValeurRubriquePartage().getRates())
				idx++;
			//

			// w_bas := w_bas + PA_CALCUL.cc_mont;
			this.salary.getValeurRubriquePartage().setBase(this.salary.getValeurRubriquePartage().getBase() + autreRub.getBase());
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
		autreRub = salary.findRubriqueClone(salary.getParameter().getPlafondRubrique());
		//
		// tab_cotf_plaf(l_indcf) := tab_cotf_plaf(l_indcf) + PA_CALCUL.c_mont;
		d = listOfRegularisationFr.get(idx).getPlafond();
		listOfRegularisationFr.get(idx).setPlafond(d + autreRub.getAmount());
		//
		// IF w_aamm = f_per THEN
		if (salary.getMonthOfPay().equals(ClsStringUtil.formatNumber(finPeriodInt, "0000")))
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
			listOfRegularisationFr.get(idx).setPlafond(d + salary.getParameter().getPlafondOfMonthList().get(l_mois));
			// l_am := add_per(l_am,0,1);
			idxDebutPeriodInt = ClsGeneralUtil.addPer(idxDebutPeriodInt, 0, 1);
		}
		return true;
	}

	public boolean calculateCumulOfBaseOld()
	{
		// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : >>calculateCumulOfBase");

		ClsRubriqueClone autreRub = null;
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
		int debutPeriodInt = new ClsDate(salary.getParameter().getDossierDateDebutExercice()).getYearAndMonthInt();
		int finPeriodInt = new Integer(salary.getMonthOfPay());
		//
		// IF f_per < d_per THEN
		// f_per := d_per;
		if (finPeriodInt < debutPeriodInt)
			finPeriodInt = debutPeriodInt;
		// END IF;
		//
		// w_tau := 0;//il est global
		this.salary.getValeurRubriquePartage().setRates(0);
		//
		// IF PA_CALCUL.t_rub.rreg = 'N' OR PA_CALCUL.t_rub.rman = 'O' THEN
		if ("N".equals(this.rubrique.getRreg()) || "O".equals(this.rubrique.getRman()))
		{
			// l_indcf := 1;
			idx = 0;
			// tab_cotf_base(l_indcf) := w_bas;
			regularisation = listOfRegularisationFr.get(idx);
			regularisation.setBase(this.salary.getValeurRubriquePartage().getBase());
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
					if (salary.getParameter().isUseRetroactif())
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
//						oRhtprcumu = (Rhtprcumu) salary.getService().get(Rhtprcumu.class,
//								new RhtprcumuPK(salary.getParameter().getDossier(), salary.getInfoSalary().getComp_id().getNmat(), idxDebutPeriodIntS, this.rubrique.getRcon(), nbul));
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
						oCumulPaie = (CumulPaie) salary.getService().find("FROM CumulPaie WHERE identreprise="+salary.getParameter().getDossier()+
																			" AND nmat='"+salary.getInfoSalary().getComp_id().getNmat()+"'"+
																			" AND aamm='"+idxDebutPeriodIntS+"' AND rubq='"+this.rubrique.getRcon()+"'"+
																			" AND nbul="+nbul);
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
					// @23-07-2009 by yannick
					if (objectIsNotNull == true && l_bas != 0 && l_tau != 0)
					{
						// if(objectIsNotNull == true
						// && l_bas > 0 && l_tau > 0){
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
						this.salary.getValeurRubriquePartage().setBase(this.salary.getValeurRubriquePartage().getBase() + l_bas);
						// tab_cotf_base(l_indcf) := tab_cotf_base(l_indcf) + l_bas;
						regularisation.setBase(regularisation.getBase() + l_bas);
						// tab_cotf_cot (l_indcf) := tab_cotf_cot (l_indcf) + l_mon;
						regularisation.setCotisation(regularisation.getCotisation() + l_mon);
						// l_mon := paf_lecCum(PA_CALCUL.wsal01.nmat,l_cam,PA_CALCUL.t_rub.crub,l_nbul,'M',PA_CALCUL.w_aamm);
						l_mon = salary.getUtilNomenclature().getCumul(salary, idxDebutPeriodIntS, rubrique.getComp_id().getCrub(), salary.getNbul(), ClsEnumeration.EnTypeOfColumn.AMOUNT, salary.getMoisPaieCourant(),
								salary.getMonthOfPay());
						// tab_cotf_reg(l_indcf) := tab_cotf_reg(l_indcf) + NVL(l_mon,0);
						regularisation.setRegularisation(regularisation.getRegularisation() + l_mon);
						//
						// IF l_cam != w_aamm THEN
						if (idxDebutPeriodIntS != null && (!idxDebutPeriodIntS.equals(salary.getMonthOfPay())))
						{
							//
							// l_mon := paf_lecCum(PA_CALCUL.wsal01.nmat,l_cam,PA_CALCUL.rub_plaf,l_nbul,'M',PA_CALCUL.w_aamm);
							l_mon = salary.getUtilNomenclature().getCumul(salary, idxDebutPeriodIntS, rubrique.getComp_id().getCrub(), salary.getNbul(), ClsEnumeration.EnTypeOfColumn.AMOUNT, salary.getMoisPaieCourant(),
									salary.getMonthOfPay());
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
			autreRub = salary.findRubriqueClone(rubrique.getRcon());
			//
			// IF PA_CALCUL.c_taux != w_tau THEN
			// l_indcf := l_indcf + 1;
			// END IF;
			if (rates != this.salary.getValeurRubriquePartage().getRates())
				idx++;
			//

			// w_bas := w_bas + PA_CALCUL.cc_mont;
			this.salary.getValeurRubriquePartage().setBase(this.salary.getValeurRubriquePartage().getBase() + autreRub.getBase());
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
		autreRub = salary.findRubriqueClone(salary.getParameter().getPlafondRubrique());
		//
		// tab_cotf_plaf(l_indcf) := tab_cotf_plaf(l_indcf) + PA_CALCUL.c_mont;
		d = listOfRegularisationFr.get(idx).getPlafond();
		listOfRegularisationFr.get(idx).setPlafond(d + autreRub.getAmount());
		//
		// IF w_aamm = f_per THEN
		if (salary.getMonthOfPay().equals(ClsStringUtil.formatNumber(finPeriodInt, "0000")))
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
			listOfRegularisationFr.get(idx).setPlafond(d + salary.getParameter().getPlafondOfMonthList().get(l_mois));
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
		// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : >>regularisationFrancaise");
		// l_indcf NUMBER(5);
		// mon_regu NUMBER(15,3);
		//
		// BEGIN
		// w_bas := 0;
		this.salary.getValeurRubriquePartage().setBase(0);
		//
		// PA_ALGO.calc_algo(w_bas,w_basp,w_mon,w_tau,w_argu,w_aamm,wsd_fcal1.nbul,Pb_Calcul);
		boolean algoResult = this.applyAlgorithm();
		//
		// IF NOT Pb_Calcul THEN
		//
		// FOR l_indcf IN 1..12 LOOP
		// w_mon := w_mon - PA_ALGO.tab_cotf_cot(l_indcf);
		// w_mon := w_mon - PA_ALGO.tab_cotf_reg(l_indcf);
		// END LOOP;
		//
		// mon_regu := w_mon;
		// IF mon_regu < 0 THEN
		// mon_regu := mon_regu * -1;
		// END IF;
		//
		// IF mon_regu < reg_mini THEN
		// w_bas := 0;
		// w_basp := 0;
		// w_tau := 0;
		// w_mon := 0;
		// END IF;
		// END IF;

		if (algoResult)
		{
			for (int i = 0; i < 12; i++)
			{
				this.salary.getValeurRubriquePartage().setAmount(this.salary.getValeurRubriquePartage().getAmount() - this.listOfRegularisationFr.get(i).getCotisation());
				this.salary.getValeurRubriquePartage().setAmount(this.salary.getValeurRubriquePartage().getAmount() - this.listOfRegularisationFr.get(i).getRegularisation());
			}
			double montantRegu = this.salary.getValeurRubriquePartage().getAmount();
			montantRegu = Math.abs(montantRegu);
			if (montantRegu < salary.getParameter().getRegularisationMinimale())
			{
				this.salary.getValeurRubriquePartage().setAmount(0);
				this.salary.getValeurRubriquePartage().setBase(0);
				this.salary.getValeurRubriquePartage().setBasePlafonnee(0);
				this.salary.getValeurRubriquePartage().setRates(0);
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
	 * public boolean calculateRubriqueOfRegularisation(int nbrePeriodeRegularisation, int nbrePeriodeRegularisationEv){ if('O' ==
	 * salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : >>calculateRubriqueOfRegularisation"); // CURSOR curs_cais IS // SELECT
	 * dtad,dtrd FROM pacaiss // WHERE cdos = wpdos.cdos // AND nmat = wsal01.nmat // AND (rscm = t_rub.rcon OR rpcm = t_rub.rcon); String queryString = "from
	 * CaisseMutuelleSalarie" + " where comp_id.cdos = '" + salary.getParameter().getDossier() + "'" + " and comp_id.nmat = '" +
	 * salary.getInfoSalary().getComp_id().getNmat() + "'" + " and (comp_id.rscm = '" + rubrique.getRcon() + "'" + " or rpcm = '" + rubrique.getRcon() + "')"; // //
	 * l_dtad DATE; // l_dtrd DATE; // l_amdtad NUMBER(6,0); // l_amdtrd NUMBER(6,0); // l_am NUMBER(6,0); // mon_regu NUMBER(15,3); // // BEGIN //
	 * ------------------------------------------------------------------------------- // -- Calcul du rang du mois de paie dans l' exercice // -- wmoi :=
	 * TO_NUMBER( SUBSTR( w_aamm, 5, 2) ); --> wmoi = indice du mois // ------------------------------------------------------------------------------- // d_aa :=
	 * TO_NUMBER(TO_CHAR(wpdos.ddex,'YYYY')); // d_mm := TO_NUMBER(TO_CHAR(wpdos.ddex,'MM')); // ddm := d_aa * 100 + d_mm; // rg := wmoi - d_mm + 1; ClsDate
	 * myExcercise = new ClsDate(salary.getParameter().getDossierDateDebutExercice(), this.salary.getParameter().FORMAT_DATE); int anneeExercice =
	 * myExcercise.getYear(); int moisExercice = myExcercise.getMonth(); int aammExercice = anneeExercice * 100 + moisExercice; int rangMoisPaieExercice =
	 * salary.getMyMonthOfPay().getMonth() - moisExercice + 1; // IF rg <= 0 THEN // rg := rg + 12; // END IF; if(rangMoisPaieExercice <= 0)
	 * rangMoisPaieExercice += 12; // salary.getOPeriod().setAnneeExercice(anneeExercice); salary.getOPeriod().setMoisExercice(moisExercice);
	 * salary.getOPeriod().setAammExercice(aammExercice); salary.getOPeriod().setRangMoisPaieExercice(rangMoisPaieExercice); // //
	 * ClsObjectUtil.displayClassProperties(ClsPeriodUtil.class, salary.getOPeriod()); // //
	 * ------------------------------------------------------------------------------- // -- Determination de la periode de reference //
	 * ------------------------------------------------------------------------------- // -- d_per = Mois de debut // -- f_per = Mois de fin // -- p = Nb mois
	 * de la periode // -- w_a1 = annee de date de debut d'exercice NUMBER // -- w_m1 = mois de date de debut d'exercice NUMBER // -- w_am = Annee_mois de date
	 * de debut d'exercice NUMBER // ------------------------------------------------------------------------------- // w_a1 := d_aa; // w_m1 := d_mm; // w_am :=
	 * ddm; // // IF t_rub.perc = 'T' THEN // W_Faitout := trime(w_am,rg,d_per,f_per,p); // ELSIF t_rub.perc = 'S' THEN // W_Faitout :=
	 * semes(w_am,rg,d_per,f_per,p); // ELSIF t_rub.perc = 'A' THEN // W_Faitout := annee(w_am,d_per,f_per,p); // END IF; if("T".equals(rubrique.getPerc()))
	 * salary.getOPeriod().trimestre(); else if("S".equals(rubrique.getPerc())) salary.getOPeriod().semetre(); else if("A".equals(rubrique.getPerc()))
	 * salary.getOPeriod().annee(aammExercice); // int debutPeriode = salary.getOPeriod().getDebutPeriode(); int finPeriode =
	 * salary.getOPeriod().getFinPeriode(); // // --------------------------------------------------------------------------------- // -- Calcul de la
	 * regularisation en fonction de la frequence // --------------------------------------------------------------------------------- // // dec1 := 0; // // IF
	 * t_rub.addf = 'O' AND (wsal01.mrrx = 'RA' OR wsal01.mrrx = 'MU') // AND wsal01.dmrr IS NOT NULL THEN // anneemois :=
	 * TO_NUMBER(TO_CHAR(wsal01.dmrr,'YYYY')) * 100 // + TO_NUMBER(TO_CHAR(wsal01.dmrr,'MM')); // IF anneemois >= d_per AND anneemois <= f_per THEN // w_depdef :=
	 * 'O'; // ELSE // w_depdef := 'N'; // END IF; // ELSE // w_depdef := 'N'; // END IF; int dec1 = 0; int anneemois = 0; char departDefinitif = ' '; ClsDate
	 * myDate = null; if((salary.getInfoSalary().getDmrr() != null) && "O".equals(rubrique.getAddf()) && ("RA".equals(salary.getInfoSalary().getMrrx()) ||
	 * "MU".equals(salary.getInfoSalary().getMrrx()))){ myDate = new ClsDate(salary.getInfoSalary().getDmrr()); anneemois = myDate.getYear() * 100 +
	 * myDate.getMonth(); departDefinitif = (anneemois >= debutPeriode && anneemois <= finPeriode)? 'O' : 'N'; } else{ departDefinitif = 'N'; } // // IF
	 * w_depdef = 'N' THEN // IF t_rub.freq != 'M' THEN // i := TO_NUMBER(SUBSTR(LTRIM(TO_CHAR(f_per,'099999')),5,2)); // IF wmoi <> i AND w_depdef = 'N' THEN //
	 * w_bas := 0; // w_tau := 0; // w_mon := 0; // w_basp := 0; // dec1 := 1; // END IF; // END IF; // END IF; int i = 0; String finPeriodeS =
	 * ClsStringUtil.formatNumber(finPeriode, "000000"); myDate = new ClsDate(finPeriodeS, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM); if(departDefinitif ==
	 * 'N'){ if(! "M".equals(rubrique.getFreq())){ i = myDate.getMonth(); if(i != salary.getMyMonthOfPay().getMonth()){
	 * this.salary.getValeurRubriquePartage().setAmount(0); this.salary.getValeurRubriquePartage().setBase(0);
	 * this.salary.getValeurRubriquePartage().setBasePlafonnee(0); this.salary.getValeurRubriquePartage().setRates(0); dec1 = 1; } } } // // IF dec1 != 0 THEN //
	 * RETURN FALSE; // END IF; if(dec1 != 0){ if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : ..............dec1 !=
	 * 0(1)"); return false; } // // --------------------------------------------------------------------------------- // -- Controle si regul. prend effet en
	 * cours d'exercice // --------------------------------------------------------------------------------- // IF NOT PA_PAIE.NouB(t_rub.pdap) THEN // l_am :=
	 * t_rub.pdap; // IF l_am >= d_per AND l_am <= f_per THEN // d_per := l_am; // END IF; // END IF; int pdap = 0; if(!
	 * ClsObjectUtil.isNull(rubrique.getPdap())){ pdap = Integer.valueOf(rubrique.getPdap()); if(pdap >= debutPeriode && pdap <= finPeriode) debutPeriode =
	 * pdap; } // // --------------------------------------------------------------------------------- // -- Recherche caisses et mutuelles , si on trouve la
	 * rub. de base on extrapole // -- par rapport a la date de debut d'inscription a la caisse //
	 * --------------------------------------------------------------------------------- // dec1 := 0; // OPEN curs_cais; // LOOP // FETCH curs_cais INTO
	 * l_dtad, l_dtrd; // EXIT WHEN curs_cais%NOTFOUND; // // IF l_dtad IS NOT NULL THEN // l_amdtad := TO_NUMBER(TO_CHAR(l_dtad,'YYYY')) * 100 // +
	 * TO_NUMBER(TO_CHAR(l_dtad,'MM')); // IF l_amdtad > d_per AND l_amdtad <= f_per THEN // d_per := l_amdtad; // END IF; // END IF; // IF l_dtrd IS NOT NULL
	 * THEN // l_amdtrd := TO_NUMBER(TO_CHAR(l_dtrd,'YYYY')) * 100 // + TO_NUMBER(TO_CHAR(l_dtrd,'MM')); // IF l_amdtrd < w_aamm THEN // w_basp := 0; // w_bas :=
	 * 0; // w_tau := 0; // w_mon := 0; // dec1 := 1; // END IF; // END IF; // END LOOP; dec1 = 0; List listOfElmtCaisse =
	 * salary.getService().find(queryString); CaisseMutuelleSalarie oCaisse = null; ClsDate ad = null; ClsDate rd = null; int iad = 0; int ird = 0; for (Object obj :
	 * listOfElmtCaisse) { oCaisse = (CaisseMutuelleSalarie)obj; if(null != oCaisse.getDtad()){ ad = new ClsDate(oCaisse.getDtad()); iad = ad.getYear() * 100 +
	 * ad.getMonth(); if(iad >= debutPeriode && iad <= finPeriode) debutPeriode = iad; } if(null != oCaisse.getDtrd()){ rd = new ClsDate(oCaisse.getDtrd()); ird =
	 * rd.getYear() * 100 + rd.getMonth(); if(ird < aammExercice){ this.salary.getValeurRubriquePartage().setAmount(0);
	 * this.salary.getValeurRubriquePartage().setBase(0); this.salary.getValeurRubriquePartage().setBasePlafonnee(0);
	 * this.salary.getValeurRubriquePartage().setRates(0); dec1 = 1; } } } // // IF dec1 != 0 THEN // RETURN FALSE; // END IF; if(dec1 != 0){ if('O' ==
	 * salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : ..............dec1 != 0(2)"); return false; } // //
	 * --------------------------------------------------------------------------------- // -- Recherche de la base sur la periode de regularisation //
	 * --------------------------------------------------------------------------------- // // /////////////////////////////W_Faitout :=
	 * cumul_bas;///////////////////////////// this.calculateCumulOfBaseForPeriod(); // // char6 := LTRIM(TO_CHAR(d_per,'099999')); String debutPeriodeS =
	 * ClsStringUtil.formatNumber(debutPeriode, "000000"); myDate = new ClsDate(debutPeriodeS, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM); // w_mois_cum :=
	 * TO_NUMBER(SUBSTR(char6,5,2)); int moisCum = myDate.getMonth(); // // nbm_per := wmoi - w_mois_cum; int nbrePeriod = salary.getMyMonthOfPay().getMonth() -
	 * moisCum; // // IF nbm_per < 0 THEN // nbm_per := nbm_per + 1; // END IF; if(nbrePeriod < 0) nbrePeriod ++; // nbm_per := nbm_per + 1; nbrePeriod ++; // //
	 * IF t_rub.perc = 'T' AND nbm_per > 3 THEN // nbm_per := 3; // ELSIF t_rub.perc = 'S' AND nbm_per > 6 THEN // nbm_per := 6; // ELSIF t_rub.perc = 'A' AND
	 * nbm_per > 12 THEN // nbm_per := 12; // END IF; if("T".equals(rubrique.getPerc()) && nbrePeriod > 3) nbrePeriod = 3; else
	 * if("S".equals(rubrique.getPerc()) && nbrePeriod > 6) nbrePeriod = 6; else if("A".equals(rubrique.getPerc()) && nbrePeriod > 12) nbrePeriod = 12; // //
	 * --------------------------------------------------------------------------------- // -- Cas ou le salarie est entre dans la societe au cours de la // --
	 * periode de reference // --------------------------------------------------------------------------------- // // w_a1 :=
	 * TO_NUMBER(TO_CHAR(wsal01.dtes,'YYYY')); // w_m1 := TO_NUMBER(TO_CHAR(wsal01.dtes,'MM')); // w_am := w_a1 * 100 + w_m1;
	 * salary.getOPeriod().setAnneeExercice(myExcercise.getYear()); salary.getOPeriod().setMoisExercice(myExcercise.getMonth()); aammExercice =
	 * salary.getOPeriod().getAnneeExercice() * 100 + salary.getOPeriod().getMoisExercice(); // salary.getOPeriod().setAammExercice(aammExercice); // // IF w_am >
	 * f_per THEN // w_bas := 0; // w_tau := 0; // w_mon := 0; // w_basp := 0; // RETURN TRUE; // ELSE // IF w_am >= d_per THEN // IF t_rub.eddf = 'N' THEN //
	 * w_am := d_per; // nbm_per := 0; // WHILE w_am <= w_aamm LOOP // w_am := PA_ALGO.add_per(w_am,0,1); // nbm_per := nbm_per + 1; // END LOOP; // ELSE //
	 * nbm_per := 0; // WHILE w_am <= w_aamm LOOP // w_am := PA_ALGO.add_per(w_am,0,1); // nbm_per := nbm_per + 1; // END LOOP; // END IF; // END IF; // END IF;
	 * aammExercice = salary.getOPeriod().getAammExercice(); int v = salary.getMyMonthOfPay().getYear() * 100 + salary.getMyMonthOfPay().getMonth();
	 * if(aammExercice > finPeriode){ this.salary.getValeurRubriquePartage().setAmount(0); this.salary.getValeurRubriquePartage().setBase(0);
	 * this.salary.getValeurRubriquePartage().setBasePlafonnee(0); this.salary.getValeurRubriquePartage().setRates(0); // if('O' ==
	 * salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : La p�riode aammExercice "+aammExercice+" est sup � finPeriode
	 * "+finPeriode+" return true;"); return true; } else{ if(aammExercice > debutPeriode){ if("N".equals(rubrique.getEddf())){ aammExercice = debutPeriode;
	 * nbrePeriod = 0; while(aammExercice < v){ aammExercice = salary.getOPeriod().addPer(v, 0, 1); nbrePeriod ++; } } else{ nbrePeriod = 0; while(aammExercice <
	 * v){ aammExercice = salary.getOPeriod().addPer(v, 0, 1); nbrePeriod ++; } } } } // salary.getOPeriod().setAammExercice(aammExercice); // //
	 * --------------------------------------------------------------------------------- // -- Extrapolation de la base de calcul a la periode concernee //
	 * --------------------------------------------------------------------------------- // // nbm_per := nbm_per + nbm_sup; // // -- Modif P.A. 03/01/96 // --
	 * Cas ou il ne faut prendre en compte pour le calcul de la regul // -- que les mois ou l'agent a travaille // IF reg_cas = 'T' THEN // nbm_per := per_regu +
	 * 1; // ELSIF reg_cas = 'J' THEN // -- Modif LH 090698 // -- Regul en jours, il faut tenir compte de la periode d'imposition // IF t_rub.perc = 'A' // THEN //
	 * W_Faitout := PA_ALGO.char_mont(rub_nbjt_totA); // END IF; // IF t_rub.perc = 'S' // THEN // W_Faitout := PA_ALGO.char_mont(rub_nbjt_totS); // END IF; //
	 * IF t_rub.perc = 'T' // THEN // W_Faitout := PA_ALGO.char_mont(rub_nbjt_totT); // END IF; // nbm_per := NVL(c_mont,0) / 30; // -- Rien trouve donc 1er
	 * mois // IF PA_PAIE.NouZ(nbm_per) THEN // nbm_per := 1; // END IF; // END IF; nbrePeriod += salary.getWorkTime().getNbreMoisSuppl(); ClsRubriqueClone rub =
	 * null; if("T".equals(salary.getParameter().getRegularisationCas())) nbrePeriod += nbrePeriodeRegularisation; else{
	 * if("J".equals(salary.getParameter().getRegularisationCas())){ if("A".equals(rubrique.getPerc())) rub =
	 * salary.findRubriqueClone(salary.getParameter().getJourWorkDepuisExerciceRubrique()); if("S".equals(rubrique.getPerc())) rub =
	 * salary.findRubriqueClone(salary.getParameter().getJourWorkDepuisSemestreRubrique()); if("T".equals(rubrique.getPerc())) rub =
	 * salary.findRubriqueClone(salary.getParameter().getJourWorkDepuisTrimestreRubrique()); } nbrePeriod = rub == null? 0 : new Double(rub.getAmount() /
	 * 30).intValue(); if(nbrePeriod <= 0) nbrePeriod = 1; } // // -- Cas ou le nombre de periode de regul a ete saisi en EV // IF NOT PA_PAIE.NouZ(nbper_regu)
	 * THEN // nbm_per := nbper_regu; // END IF; if(nbrePeriodeRegularisationEv > 0) nbrePeriod = nbrePeriodeRegularisationEv; // // IF w_depdef = 'O' THEN //
	 * IF t_rub.eddf = 'O' THEN // w_bas := (cum_basc / nbm_per) * p; // ELSE // w_bas := cum_basc; // END IF; // ELSE // w_bas := (cum_basc / nbm_per) * p; //
	 * END IF; if('O' == departDefinitif){ if("O".equals(rubrique.getEddf())){ if(nbrePeriodeRegularisation != 0)
	 * this.salary.getValeurRubriquePartage().setBase((salary.getParamCumul().getCumulBaseCalc() / nbrePeriodeRegularisation) *
	 * this.salary.getOPeriod().getP()); } else this.salary.getValeurRubriquePartage().setBase(this.salary.getParamCumul().getCumulBaseCalc()); } else{
	 * if(nbrePeriodeRegularisation != 0) this.salary.getValeurRubriquePartage().setBase((this.salary.getParamCumul().getCumulBaseCalc() /
	 * nbrePeriodeRegularisation) * this.salary.getOPeriod().getP()); } // //
	 * PA_ALGO.calc_algo(w_bas,w_basp,w_mon,w_tau,w_argu,w_aamm,wsd_fcal1.nbul,Pb_Calcul); // IF pb_calcul THEN // RETURN FALSE; // END IF; boolean algoResult =
	 * this.applyAlgorithm(); if(! algoResult) return false; // // --------------------------------------------------------------------------------- // --
	 * Extrapolation du montant des cotisations du mois traite // --------------------------------------------------------------------------------- // // IF
	 * w_depdef = 'O' THEN // IF t_rub.eddf = 'O' THEN // mon_regu := (w_mon * nbm_per) / p; // ELSE // mon_regu := w_mon; // END IF; // ELSE // mon_regu :=
	 * (w_mon * nbm_per) / p; // END IF; double montantRegu = 0; if('O' == departDefinitif){ if("O".equals(rubrique.getEddf())){ if(
	 * this.salary.getOPeriod().getP() != 0) montantRegu = (this.salary.getValeurRubriquePartage().getAmount() * nbrePeriodeRegularisation) /
	 * this.salary.getOPeriod().getP(); } else montantRegu = this.salary.getValeurRubriquePartage().getAmount(); } else{ if( this.salary.getOPeriod().getP() !=
	 * 0) montantRegu = (this.salary.getValeurRubriquePartage().getAmount() * nbrePeriodeRegularisation) / this.salary.getOPeriod().getP(); } // //
	 * --------------------------------------------------------------------------------- // -- Calcul du montant de la regularisation //
	 * --------------------------------------------------------------------------------- // w_mon := mon_regu - cum_coti - cum_regu; // w_bas := cum_basc; //
	 * w_basp := cum_basc; this.salary.getValeurRubriquePartage().setAmount(montantRegu - this.salary.getParamCumul().getCumulCoti() -
	 * this.salary.getParamCumul().getCumulRegu()); this.salary.getValeurRubriquePartage().setBase(this.salary.getParamCumul().getCumulBaseCalc());
	 * this.salary.getValeurRubriquePartage().setBasePlafonnee(this.salary.getParamCumul().getCumulBaseCalc()); // //
	 * --------------------------------------------------------------------------------- // -- Comparaison du montant de la regularisation avec le montant // --
	 * minimal de regularisation (parametre REGMINI table 99) // --------------------------------------------------------------------------------- // // --
	 * ----- Arrondi du resultat de la regul avant test avec REGMINI // IF NOT PA_PAIE.NouB(t_rub.arro) THEN // arrondi(w_mon); // IF pb_calcul THEN // RETURN
	 * FALSE; // END IF; // END IF; double mnt = 0; if(! ClsObjectUtil.isNull(rubrique.getArro())){ mnt = this.calculateArrondi(salary.getParameter(),
	 * this.salary.getValeurRubriquePartage().getAmount()); this.salary.getValeurRubriquePartage().setAmount(mnt); } // mon_regu := w_mon; // // IF mon_regu < 0
	 * THEN // mon_regu := mon_regu * -1; // END IF; montantRegu = Math.abs(this.salary.getValeurRubriquePartage().getAmount()); // // IF mon_regu < reg_mini
	 * THEN // w_bas := 0; // w_tau := 0; // w_mon := 0; // w_basp := 0; // END IF; if(montantRegu < salary.getParameter().getRegularisationMinimale()){
	 * this.salary.getValeurRubriquePartage().setAmount(0); this.salary.getValeurRubriquePartage().setBase(0);
	 * this.salary.getValeurRubriquePartage().setBasePlafonnee(0); this.salary.getValeurRubriquePartage().setRates(0); }
	 * this.salary.getOPeriod().setP(nbrePeriod); // // RETURN TRUE; return true; }
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
		if(StringUtils.equals("2080", this.rubrique.getComp_id().getCrub()))
		{
			//System.out.println("Rubrique 2080");
		}
		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\n--In Rubrique Clone : call for calculateRubriqueOfRegularisation1 avec Nombre de p�riode =  " + nbrePeriodeRegularisationEv);
		// CURSOR curs_cais IS
		// SELECT dtad,dtrd FROM pacaiss
		// WHERE cdos = wpdos.cdos
		// AND nmat = wsal01.nmat
		// AND (rscm = t_rub.rcon OR rpcm = t_rub.rcon);

		String queryString = "from CaisseMutuelleSalarie" + " where comp_id.cdos = '" + salary.getParameter().getDossier() + "'" + " and comp_id.nmat = '" + salary.getInfoSalary().getComp_id().getNmat() + "'"
				+ " and (comp_id.rscm = '" + rubrique.getRcon() + "'" + " or rpcm = '" + rubrique.getRcon() + "')";
		//
		// l_dtad DATE;
		// l_dtrd DATE;
		// l_amdtad NUMBER(6,0);
		// l_amdtrd NUMBER(6,0);
		// l_am NUMBER(6,0);
		// mon_regu NUMBER(15,3);
		//
		// BEGIN
		// -------------------------------------------------------------------------------
		// -- Calcul du rang du mois de paie dans l' exercice
		// -- wmoi := TO_NUMBER( SUBSTR( w_aamm, 5, 2) ); --> wmoi = indice du mois
		// -------------------------------------------------------------------------------
		// d_aa := TO_NUMBER(TO_CHAR(wpdos.ddex,'YYYY'));
		// d_mm := TO_NUMBER(TO_CHAR(wpdos.ddex,'MM'));
		// ddm := d_aa * 100 + d_mm;
		// rg := wmoi - d_mm + 1;
		// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : >>salary.getParameter().getDossierDateDebutExercice():" +
		// salary.getParameter().getDossierDateDebutExercice());
		// ClsDate myExcercise = new ClsDate(salary.getParameter().getDossierDateDebutExercice(), this.salary.getParameter().getAppDateFormat());
		int debutExerciceAnnee = salary.getParameter().getDebutExerciceAnnee();// myExcercise.getYear();
		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\n--In Rubrique Clone : >>debutExerciceAnnee:" + debutExerciceAnnee);
		int debutExerciceMois = salary.getParameter().getDebutExerciceMois();// myExcercise.getMonth();
		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\n--In Rubrique Clone : >>debutExerciceMois:" + debutExerciceMois);
		int debutExerciceaamm = salary.getParameter().getDebutExerciceaamm(); // debutExerciceAnnee * 100 + debutExerciceMois;
		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\n--In Rubrique Clone : >>debutExerciceaamm:" + debutExerciceaamm);
		int debutExerciceRangMoisPaie = salary.getParameter().getDebutExerciceRangMoisPaie();// salary.getMyMonthOfPay().getMonth() - debutExerciceMois + 1;
		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\n--In Rubrique Clone : >>debutExerciceRangMoisPaie:" + debutExerciceRangMoisPaie);
		// IF rg <= 0 THEN
		// rg := rg + 12;
		// END IF;
		if (debutExerciceRangMoisPaie <= 0)
			debutExerciceRangMoisPaie += 12;
		//
		// ClsObjectUtil.displayClassProperties(ClsPeriodUtil.class, salary.getOPeriod());
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
		salary.getOPeriod().setAnneeExercice(debutExerciceAnnee);
		salary.getOPeriod().setMoisExercice(debutExerciceMois);
		salary.getOPeriod().setAammExercice(debutExerciceaamm);
		salary.getOPeriod().setRangMoisPaieExercice(debutExerciceRangMoisPaie);

		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\n--In Rubrique Clone, operiod from exercice = " + String.valueOf(salary.getOPeriod()));
		//
		if ("T".equals(rubrique.getPerc()))
			salary.getOPeriod().trimestre();
		else if ("S".equals(rubrique.getPerc()))
			salary.getOPeriod().semetre();
		else if ("A".equals(rubrique.getPerc()))
			salary.getOPeriod().annee(debutExerciceaamm);
		else if ("B".equals(rubrique.getPerc()))
			salary.getOPeriod().annee13(debutExerciceaamm);
		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\n--In Rubrique Clone, operiod from exercice after modify(trime, seme, annee) = " + String.valueOf(salary.getOPeriod()));
		//
		int debutPeriode = salary.getOPeriod().getDebutPeriode();
		int finPeriode = salary.getOPeriod().getFinPeriode();

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
		double nbreJoursPeriod = 0; 
		int dec1 = 0;
		int anneemois = 0;
		char departDefinitif = ' ';
		ClsDate myDate = null;
		if ((salary.getInfoSalary().getDmrr() != null) && "O".equals(rubrique.getAddf()) && ("RA".equals(salary.getInfoSalary().getMrrx()) || "MU".equals(salary.getInfoSalary().getMrrx())))
		{
			myDate = new ClsDate(salary.getInfoSalary().getDmrr());
			anneemois = myDate.getYear() * 100 + myDate.getMonth();
			departDefinitif = (anneemois >= debutPeriode && anneemois <= finPeriode) ? 'O' : 'N';
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
		String finPeriodeS = ClsStringUtil.formatNumber(finPeriode, "000000");
		myDate = new ClsDate(finPeriodeS, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
		if (departDefinitif == 'N')
		{
			if (!"M".equals(rubrique.getFreq()))
			{
				i = myDate.getMonth();
				if (i != salary.getMyMonthOfPay().getMonth())
				{
					this.salary.getValeurRubriquePartage().setAmount(0);
					this.salary.getValeurRubriquePartage().setBase(0);
					this.salary.getValeurRubriquePartage().setBasePlafonnee(0);
					this.salary.getValeurRubriquePartage().setRates(0);
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
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\n--In Rubrique Clone : ..............dec1 != 0(1)");
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
			if (pdap >= debutPeriode && pdap <= finPeriode)
				debutPeriode = pdap;
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
		List listOfElmtCaisse = salary.getService().find(queryString);
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
				if (iad > debutPeriode && iad <= finPeriode)
					debutPeriode = iad;
			}
			if (null != oCaisse.getDtrd())
			{
				rd = new ClsDate(oCaisse.getDtrd());
				ird = rd.getYear() * 100 + rd.getMonth();
				if (ird < this.salary.getMyMonthOfPay().getYearAndMonthInt())
				{
					this.salary.getValeurRubriquePartage().setAmount(0);
					this.salary.getValeurRubriquePartage().setBase(0);
					this.salary.getValeurRubriquePartage().setBasePlafonnee(0);
					this.salary.getValeurRubriquePartage().setRates(0);
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
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\n--In Rubrique Clone : ..............dec1 != 0(2)");
			return false;
		}
		//
		// ---------------------------------------------------------------------------------
		// -- Recherche de la base sur la periode de regularisation
		// ---------------------------------------------------------------------------------
		//
		// /////////////////////////////W_Faitout := cumul_bas;/////////////////////////////
		//this.calculateCumulOfBaseForPeriod();
		this.calculateCumulOfBaseForPeriod2();

		//
		// char6 := LTRIM(TO_CHAR(d_per,'099999'));
		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\ndebutPeriode= " + debutPeriode);
		String debutPeriodeS = ClsStringUtil.formatNumber(debutPeriode, "000000");
		myDate = new ClsDate(debutPeriodeS, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
		// w_mois_cum := TO_NUMBER(SUBSTR(char6,5,2));
		int moisCum = myDate.getMonth();
		//
		// nbm_per := wmoi - w_mois_cum;
		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\nMois cumul (mois de d�but periode) = " + moisCum);
		int moisPaie = salary.getMyMonthOfPay().getMonth();
		//Cas de SE2M/SE3M : la r�gul se fait sur 13 Mois
		//En fin d'ann�e, il y a deux paie : celle du mois en premier et le 13 i�me mois en deuxi�me.
		//De ce fait, on assimilera le bulletin 1 de decembre comme le 13i�me mois, donc moisPaie = 13;
		
		if(debutExerciceRangMoisPaie == 12 && "B".equals(rubrique.getPerc()))
		{
			if(this.salary.getNbul() == 1) 	moisPaie = 13;
		}
		
		double nbrePeriod = moisPaie - moisCum;
		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\nNbrPeriod initial (mois paie - mois cumul) = " + nbrePeriod);
		//
		// IF nbm_per < 0 THEN
		// nbm_per := nbm_per + 1;
		// END IF;
		// nbm_per := nbm_per + 1;
		if (nbrePeriod < 0)
			nbrePeriod++;
		nbrePeriod++;
		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\nIncrementation de nbrePeriod NbrPeriod = " + nbrePeriod);
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
		else if ("B".equals(rubrique.getPerc()) && nbrePeriod > 13)
			nbrePeriod = 13;
		
		if(nbreJoursPeriod == 0)
			nbreJoursPeriod = 30*nbrePeriod;
		
		//
		// ---------------------------------------------------------------------------------
		// -- Cas ou le salarie est entre dans la societe au cours de la
		// -- periode de reference
		// ---------------------------------------------------------------------------------
		//
		// w_a1 := TO_NUMBER(TO_CHAR(wsal01.dtes,'YYYY'));
		// w_m1 := TO_NUMBER(TO_CHAR(wsal01.dtes,'MM'));
		// w_am := w_a1 * 100 + w_m1;

		// salary.getOPeriod().setAnneeExercice(myExcercise.getYear());
		// salary.getOPeriod().setMoisExercice(myExcercise.getMonth());
		// aammExercice = salary.getOPeriod().getAnneeExercice() * 100 + salary.getOPeriod().getMoisExercice();
		//
		// salary.getOPeriod().setAammExercice(aammExercice);

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
		ClsDate dtes = new ClsDate(salary.getInfoSalary().getDtes());
		debutExerciceAnnee = dtes.getYear();
		debutExerciceMois = dtes.getMonth();
		debutExerciceaamm = debutExerciceAnnee * 100 + debutExerciceMois;
		int jADeduire = 0;

		if (debutExerciceaamm > finPeriode)
		{
			this.salary.getValeurRubriquePartage().setBase(0);
			this.salary.getValeurRubriquePartage().setRates(0);
			this.salary.getValeurRubriquePartage().setAmount(0);
			this.salary.getValeurRubriquePartage().setBasePlafonnee(0);
			return true;
		}
		else
		{
			if (debutExerciceaamm >= debutPeriode)
			{
				if ("N".equalsIgnoreCase(rubrique.getEddf()))
					debutExerciceaamm = debutPeriode;
				else
				{
					//Le montant est extrapol�, donc il faut d�duire les jours avant entre dtes et d�but p�riode
					jADeduire = dtes.getDay()+1;			
				}
				nbrePeriod = 0;
				while (debutExerciceaamm <= salary.getMyMonthOfPay().getYearAndMonthInt())
				{
					debutExerciceaamm = ClsGeneralUtil.addPer(debutExerciceaamm, 0, 1);
					nbrePeriod++;
				}
				nbreJoursPeriod = 30*nbrePeriod - jADeduire;
			}
		}
		
		//SOBRAGA : Si le salari� a �t� radie pendant le mois de paie on deduit le nombre de jours d'absence des jours impos.
		myDate = new ClsDate(salary.getInfoSalary().getDmrr());
		if ((salary.getInfoSalary().getDmrr() != null) && (myDate.getYearAndMonthInt()>=salary.parameter.myMonthOfPay.getYearAndMonthInt() && myDate.getYearAndMonthInt()<= salary.parameter.myMonthOfPay.getYearAndMonthInt()))
		{
			int jRadiation = myDate.getDay();
			jRadiation--;
			nbreJoursPeriod -= 30;
			nbreJoursPeriod += jRadiation;
            if(nbreJoursPeriod < 0)
            	nbreJoursPeriod = 0;
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
		nbrePeriod += salary.getWorkTime().getNbreMoisSuppl();
		nbreJoursPeriod += salary.getWorkTime().getNbreMoisSuppl()*30;
		
		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\nAjout du nombre de mois suppl (" + salary.getWorkTime().getNbreMoisSuppl() + ") � nbrePeriod NbrPeriod = " + nbrePeriod);
		ClsRubriqueClone rub = null;
		if ("T".equals(salary.getParameter().getRegularisationCas()))
		{
			// nbrePeriod += salary.getParamCumul().getNbreMoisCumul();
			nbrePeriod = salary.getParamCumul().getNbreMoisCumul() + 1;
		}
		else
		{
			if ("J".equals(salary.getParameter().getRegularisationCas()))
			{
				if ("A".equals(rubrique.getPerc()))
					rub = salary.findRubriqueClone(salary.getParameter().getJourWorkDepuisExerciceRubrique());
				if ("S".equals(rubrique.getPerc()))
					rub = salary.findRubriqueClone(salary.getParameter().getJourWorkDepuisSemestreRubrique());
				if ("T".equals(rubrique.getPerc()))
					rub = salary.findRubriqueClone(salary.getParameter().getJourWorkDepuisTrimestreRubrique());
				if ("B".equals(rubrique.getPerc()))
					rub = salary.findRubriqueClone(salary.getParameter().getJourWorkDepuisExerciceRubrique());

				nbrePeriod = rub == null ? 0 : new Double(rub.getAmount() / 30).intValue();
				nbreJoursPeriod = rub == null ? 0 : new Double(rub.getAmount()).intValue();
				// @23-07-2009 by yannick
				if (nbrePeriod == 0)
				{
					nbrePeriod = 1;
					nbreJoursPeriod = nbrePeriod * 30;
				}
				// if (nbrePeriod <= 0)
				// nbrePeriod = 1;
			}
		}
		//
		// -- Cas ou le nombre de periode de regul a ete saisi en EV
		// IF NOT PA_PAIE.NouZ(nbper_regu) THEN
		// nbm_per := nbper_regu;
		// END IF;
		// @23-07-2009 by yannick
		if (nbrePeriodeRegularisationEv != 0)
		{
			nbrePeriod = nbrePeriodeRegularisationEv;
			nbreJoursPeriod += nbrePeriod*30;
		}
		// if(nbrePeriodeRegularisationEv > 0)
		// nbrePeriod = nbrePeriodeRegularisationEv;

		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\n" + "Nombre de p�riode = " + nbrePeriod);
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
		//Pour SOBRAGA ou si REGMINI LIB1=D (Commes Days), prendre en compte les jours
		
//		if(StringUtils.equalsIgnoreCase(ClsEntreprise.SOBRAGA, salary.parameter.nomClient) || ("D".equals(salary.getParameter().getRegularisationCas())))
//		{
//			nbrePeriod = nbreJoursPeriod;
//			salary.getOPeriod().setP(this.salary.getOPeriod().getP() * 30);
//		}
		
		if ('O' == departDefinitif)
		{
			if ("O".equals(rubrique.getEddf()))
			{
				if (nbrePeriod != 0)
					this.salary.getValeurRubriquePartage().setBase((salary.getParamCumul().getCumulBaseCalc() / nbrePeriod) * this.salary.getOPeriod().getP());
			}
			else
				this.salary.getValeurRubriquePartage().setBase(this.salary.getParamCumul().getCumulBaseCalc());
		}
		else
		{
			if (nbrePeriod != 0)
			{
				this.salary.getValeurRubriquePartage().setBase((this.salary.getParamCumul().getCumulBaseCalc() / nbrePeriod) * this.salary.getOPeriod().getP());
				if ('O' == salary.getParameter().getGenfile())
					salary.addToOutputtext( "\n" + "ValeurRubriquePartage.base ((this.salary.getParamCumul().getCumulBaseCalc() / nbrePeriod) * this.salary.getOPeriod().getP()) = "
							+ this.salary.getValeurRubriquePartage().getBase());
			}
		}
		

		//
		// PA_ALGO.calc_algo(w_bas,w_basp,w_mon,w_tau,w_argu,w_aamm,wsd_fcal1.nbul,Pb_Calcul);
		// IF pb_calcul THEN
		// RETURN FALSE;
		// END IF;

		boolean algoResult = this.applyAlgorithm();
		if (!algoResult)
			return false;
		
		//Param�trage d'un taux tx dont tx*base <= montant
		
		String sql = " select valt from Rhfnom where cdos = '" + salary.getParameter().getDossier() + "' and ctab = 99 and cacc='POURCIMPOT' and nume = 1";
		List o = salary.getService().find(sql);
		if (o != null && !o.isEmpty() && o.get(0)!= null && new BigDecimal(o.get(0).toString()).intValue() != 0)
		{
			//on applique un max entre le montant trouv� et le plafond du taux (par exemple, l'impot annuel ne doit pas d�passer 3% du brut imposable
			double tx = new BigDecimal(o.get(0).toString()).doubleValue();
			double mnt = this.salary.getValeurRubriquePartage().getBase() * tx /100;
			double mont = this.salary.getValeurRubriquePartage().getAmount();
			if(mont>mnt) this.salary.getValeurRubriquePartage().setAmount(mnt);
		}
		
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
				montantRegu = (this.salary.getValeurRubriquePartage().getAmount() * nbrePeriod) / this.salary.getOPeriod().getP();
			else
				montantRegu = this.salary.getValeurRubriquePartage().getAmount();
		}
		else
		{
			montantRegu = (this.salary.getValeurRubriquePartage().getAmount() * nbrePeriod) / this.salary.getOPeriod().getP();
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\n" + "montantRegu =" + montantRegu);
		}

		//
		// ---------------------------------------------------------------------------------
		// -- Calcul du montant de la regularisation
		// ---------------------------------------------------------------------------------
		// w_mon := mon_regu - cum_coti - cum_regu;
		// w_bas := cum_basc;
		// w_basp := cum_basc;
		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\n" + "montantRegu =" + montantRegu);
		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\n" + "cum_coti =" + this.salary.getParamCumul().getCumulCoti());
		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\n" + "cum_regu =" + this.salary.getParamCumul().getCumulRegu());
		this.salary.getValeurRubriquePartage().setAmount(montantRegu - this.salary.getParamCumul().getCumulCoti() - this.salary.getParamCumul().getCumulRegu());
		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\n" + "ValeurRubriquePartage.amount (montantRegu - this.salary.getParamCumul().getCumulCoti() - this.salary.getParamCumul().getCumulRegu()) ="
					+ (montantRegu - this.salary.getParamCumul().getCumulCoti() - this.salary.getParamCumul().getCumulRegu()));
		this.salary.getValeurRubriquePartage().setBase(this.salary.getParamCumul().getCumulBaseCalc());
		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\n" + "ValeurRubriquePartage.base (this.salary.getParamCumul().getCumulBaseCalc()) =" + (this.salary.getParamCumul().getCumulBaseCalc()));
		this.salary.getValeurRubriquePartage().setBasePlafonnee(this.salary.getParamCumul().getCumulBaseCalc());
		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\n" + "ValeurRubriquePartage.baseplaf (this.salary.getParamCumul().getCumulBaseCalc()) =" + (this.salary.getParamCumul().getCumulBaseCalc()));

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
			mnt = this.calculateArrondi(salary.getParameter(), this.salary.getValeurRubriquePartage().getAmount());
			this.salary.getValeurRubriquePartage().setAmount(mnt);
		}
		// mon_regu := w_mon;
		//
		// IF mon_regu < 0 THEN
		// mon_regu := mon_regu * -1;
		// END IF;
		montantRegu = Math.abs(this.salary.getValeurRubriquePartage().getAmount());
		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\n" + "montantRegu =" + montantRegu);
		//
		// IF mon_regu < reg_mini THEN
		// w_bas := 0;
		// w_tau := 0;
		// w_mon := 0;
		// w_basp := 0;
		// END IF;
		if (montantRegu < salary.getParameter().getRegularisationMinimale())
		{
			this.salary.getValeurRubriquePartage().setAmount(0);
			this.salary.getValeurRubriquePartage().setBase(0);
			this.salary.getValeurRubriquePartage().setBasePlafonnee(0);
			this.salary.getValeurRubriquePartage().setRates(0);
		}
		//this.salary.getOPeriod().setP(nbrePeriod);
		//
		// RETURN TRUE;
		return true;
	}

	/**
	 * Cumuls des bases de cotisation pour la periode concernee => cumul_bas
	 */
	public void calculateCumulOfBaseForPeriod()
	{
		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\n--In Rubrique Clone : >>calculateCumulOfBaseForPeriod");
		// fin_periode NUMBER(6,0);
		// cum99 NUMBER(6,0);
		// aamm_cum NUMBER(6,0);
		// c_fin_periode CHAR(6);
		// c_cum99 CHAR(6);
		// c_aamm_cum CHAR(6);
		//
		// BEGIN
		// w_mois_cum := 0;
		// cum_coti := 0;
		// cum_regu := 0;
		// cum_basc := 0;
		// w_am := d_per;
		// aamm_cum := w_am;
		//
		// cum99 := aamm_cum / 100;
		// cum99 := cum99 * 100 + 99;
		// IF w_aamm < f_per THEN
		// fin_periode := w_aamm;
		// ELSE
		// fin_periode := f_per;
		// END IF;
		int fin_periode = 0;
		int yearAndMonthCumul = salary.getOPeriod().getAammExercice();
		int cum99 = yearAndMonthCumul / 100;
		cum99 = cum99 * 100 + 99;
		if (salary.getMyMonthOfPay().getYearAndMonthInt() < this.salary.getOPeriod().getFinPeriode())
			fin_periode = salary.getMyMonthOfPay().getYearAndMonthInt();
		else
		{
			fin_periode = this.salary.getOPeriod().getFinPeriode();
		}
		//
		// IF retroactif THEN
		// -- on exclu la periode de traitement (w_aamm)
		// IF aamm_cum < w_aamm AND fin_periode = w_aamm THEN
		// fin_periode := fin_periode - 1;
		// END IF;
		// END IF;
		if (salary.getParameter().isUseRetroactif())
		{
			if ((yearAndMonthCumul < salary.getMyMonthOfPay().getYearAndMonthInt()) && (fin_periode == salary.getMyMonthOfPay().getYearAndMonthInt()))
				fin_periode--;
		}
		//
		//
		// c_aamm_cum := LTRIM(TO_CHAR(aamm_cum,'099999'));
		// c_fin_periode := LTRIM(TO_CHAR(fin_periode,'099999'));
		// c_cum99 := LTRIM(TO_CHAR(cum99,'099999'));
		//
		// IF retroactif THEN
		// IF c_aamm_cum != w_aamm THEN
		// SELECT SUM(basc), SUM(mont) INTO cum_basc, cum_coti
		// FROM prcumu
		// WHERE cdos = wpdos.cdos
		// AND nmat = wsal01.nmat
		// AND aamm >= c_aamm_cum
		// AND aamm <= c_fin_periode
		// AND aamm != c_cum99
		// AND rubq = t_rub.rcon
		// AND nbul != 0;
		// ELSE
		// cum_basc := 0;
		// cum_coti := 0;
		// END IF;
		// ELSE
		// SELECT SUM(basc), SUM(mont) INTO cum_basc, cum_coti
		// FROM pacumu
		// WHERE cdos = wpdos.cdos
		// AND nmat = wsal01.nmat
		// AND aamm >= c_aamm_cum
		// AND aamm <= c_fin_periode
		// AND aamm != c_cum99
		// AND rubq = t_rub.rcon
		// AND nbul != 0;
		// END IF;
		// String yearAndMonthCumulS = ClsStringUtil.formatNumber(yearAndMonthCumul, "000000");
		// String finPeriodeS = ClsStringUtil.formatNumber(fin_periode, "000000");
		// String cum99S = ClsStringUtil.formatNumber(cum99, "000000");
		// String queryString = "select sum(basc), sum(mont) from CumulPaie" +
		// " where cdos = '" + salary.getParameter().getDossier() + "'" +
		// " and nmat = '" + salary.getInfoSalary().getComp_id().getNmat() + "'" +
		// " and aamm >= '" + yearAndMonthCumulS + "'" +
		// " and aamm <= '" + finPeriodeS + "'" +
		// " and aamm != '" + cum99S + "'" +
		// " and rubq = '" + rubrique.getRcon() + "'" +
		// " and nbul != 0";
		// String queryStringRetro = "select sum(basc), sum(mont) from Rhtprcumu" +
		// " where cdos = '" + salary.getParameter().getDossier() + "'" +
		// " and nmat = '" + salary.getInfoSalary().getComp_id().getNmat() + "'" +
		// " and aamm >= '" + yearAndMonthCumulS + "'" +
		// " and aamm <= '" + finPeriodeS + "'" +
		// " and aamm != '" + cum99S + "'" +
		// " and rubq = '" + rubrique.getRcon() + "'" +
		// " and nbul != 0";
		// Object[] row = (salary.getParameter().isUseRetroactif())? (Object[])salary.getService().find(queryStringRetro).get(0):
		// (Object[])salary.getService().find(queryString).get(0);
		
		
		String yearAndMonthCumulS = ClsStringUtil.formatNumber(yearAndMonthCumul, "000000");
		String finPeriodeS = ClsStringUtil.formatNumber(fin_periode, "000000");
		String cum99S = ClsStringUtil.formatNumber(cum99, "000000");
		String key = yearAndMonthCumulS+finPeriodeS+cum99S;
		
		salary.buildSpecifiqueCumul99Map(yearAndMonthCumulS,finPeriodeS, cum99S);
		//salary.buildSpecifiqueCumul99Map(rubrique.getRcon());
		Object[] row = null;
		if (salary.getParameter().isUseRetroactif())
		{
			if (yearAndMonthCumul != salary.getMyMonthOfPay().getYearAndMonthInt())
				row = salary.cumulMap1PourPeriode.get(key).get(rubrique.getRcon());
		}
		else
			row = salary.cumulMap1PourPeriode.get(key).get(rubrique.getRcon());
		double cumulBaseCalc = 0;
		double cumulCoti = 0;
		double cumulRegu = 0;
		int periodRegu = 0;
		if (!ClsObjectUtil.isNull(row))
		{
			if (!ClsObjectUtil.isNull(row[0]))
				cumulBaseCalc = ClsObjectUtil.getDoubleFromObject(row[0]);
			if (!ClsObjectUtil.isNull(row[1]))
				cumulCoti = ClsObjectUtil.getDoubleFromObject(row[1]);

			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\n--In Rubrique Clone : ...........Initial cumulBaseCalc:" + cumulBaseCalc);
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\n--In Rubrique Clone : ..........Initial  cumulCoti:" + cumulCoti);

			// @23-07-2009 by yannick
			// if(cumulBaseCalc < 0)
			// cumulBaseCalc = 0;
			// if(cumulCoti < 0)
			// cumulCoti = 0;
		}
		else
		{
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\n--In Rubrique Clone : ... Cumul de " + rubrique.getRcon() + " est null");
		}
		//
		// IF PA_PAIE.NouZ(cum_basc) THEN
		// cum_basc := 0;
		// END IF;
		// IF PA_PAIE.NouZ(cum_coti) THEN
		// cum_coti := 0;
		// END IF;
		//
		// IF retroactif THEN
		// IF c_aamm_cum != w_aamm THEN
		// SELECT SUM(mont) INTO cum_regu FROM prcumu
		// WHERE cdos = wpdos.cdos
		// AND nmat = wsal01.nmat
		// AND aamm >= c_aamm_cum
		// AND aamm <= c_fin_periode
		// AND aamm != c_cum99
		// AND rubq = t_rub.crub
		// AND nbul != 0;
		// ELSE
		// cum_regu := 0;
		// END IF;
		// ELSE
		// SELECT SUM(mont) INTO cum_regu FROM pacumu
		// WHERE cdos = wpdos.cdos
		// AND nmat = wsal01.nmat
		// AND aamm >= c_aamm_cum
		// AND aamm <= c_fin_periode
		// AND aamm != c_cum99
		// AND rubq = t_rub.crub
		// AND nbul != 0;
		// END IF;
		//
		// IF PA_PAIE.NouZ(cum_regu) THEN
		// cum_regu := 0;
		// END IF;
		// queryString = "select sum(mont) from CumulPaie" +
		// " where cdos = '" + salary.getParameter().getDossier() + "'" +
		// " and nmat = '" + salary.getInfoSalary().getComp_id().getNmat() + "'" +
		// " and aamm >= '" + yearAndMonthCumulS + "'" +
		// " and aamm <= '" + finPeriodeS + "'" +
		// " and aamm != '" + cum99S + "'" +
		// " and rubq = '" + rubrique.getComp_id().getCrub() + "'" +
		// " and nbul != 0";
		// queryStringRetro = "select sum(mont) from Rhtprcumu" +
		// " where cdos = '" + salary.getParameter().getDossier() + "'" +
		// " and nmat = '" + salary.getInfoSalary().getComp_id().getNmat() + "'" +
		// " and aamm >= '" + yearAndMonthCumulS + "'" +
		// " and aamm <= '" + finPeriodeS + "'" +
		// " and aamm != '" + cum99S + "'" +
		// " and rubq = '" + rubrique.getComp_id().getCrub() + "'" +
		// " and nbul != 0";
		// Object row1 = (salary.getParameter().isUseRetroactif())? (Object)salary.getService().find(queryStringRetro).get(0):
		// (Object)salary.getService().find(queryString).get(0);

		Object row1 = null;
		if (salary.getParameter().isUseRetroactif())
		{
			if (yearAndMonthCumul != salary.getMyMonthOfPay().getYearAndMonthInt())
				row1 = salary.cumulMap2PourPeriode.get(key).get(rubrique.getComp_id().getCrub());
		}
		else
			row1 = salary.cumulMap2PourPeriode.get(key).get(rubrique.getComp_id().getCrub());

		if (!ClsObjectUtil.isNull(row1))
			cumulRegu = ClsObjectUtil.getDoubleFromObject(row1);
		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\n--In Rubrique Clone : ..........Initial  cumulRegu:" + cumulRegu);
		// @23-07-2009 by yannick
		// if(cumulRegu < 0)
		// cumulRegu = 0;

		ClsRubriqueClone autreRub = salary.findRubriqueClone(rubrique.getRcon());
		//
		// W_Faitout := PA_ALGO.char_mont(t_rub.rcon);
		//
		// --IF NOT PA_PAIE.NouZ(c_mont) THEN
		// cum_basc := cum_basc + cc_mont;
		// cum_coti := cum_coti + c_mont;

		// ------------from Emmanuel
		/*
		 * cumulBaseCalc = this.salary.getParamCumul().getCumulBaseCalc() + this.getBase(); this.salary.getParamCumul().setCumulBaseCalc(cumulBaseCalc);
		 * cumulCoti = this.salary.getParamCumul().getCumulCoti() + this.getAmount(); this.salary.getParamCumul().setCumulCoti(cumulCoti);
		 */
		// ------------from Yannick
		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\nRubrique concern� par la regul (autreRub) = " + rubrique.getRcon());
		// cumulBaseCalc += this.getBase();
		cumulBaseCalc += autreRub.getBase();
		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\ncumulBaseCalc = cumulBaseCalc + autreRub.getBase() = " + cumulBaseCalc);
		// this.salary.getParamCumul().setCumulBaseCalc(cumulBaseCalc);
		// cumulCoti += this.getAmount();
		cumulCoti += autreRub.getAmount();
		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\ncumulCoti = cumulBaseCalc + autreRub.getAmount() = " + cumulCoti);
		// this.salary.getParamCumul().setCumulCoti(cumulCoti);
		// ------------------------End Yannick
		// --END IF;
		//
		// IF reg_cas = 'T' THEN
		// IF retroactif THEN
		// IF c_aamm_cum != w_aamm THEN
		// SELECT count(*) INTO per_regu FROM prcumu
		// WHERE cdos = wpdos.cdos
		// AND nmat = wsal01.nmat
		// AND aamm >= c_aamm_cum
		// AND aamm < c_fin_periode
		// AND aamm != c_cum99
		// AND rubq = w_rubbrut
		// AND nbul = 9;
		// ELSE
		// per_regu := 0;
		// END IF;
		// ELSE
		// SELECT count(*) INTO per_regu FROM pacumu
		// WHERE cdos = wpdos.cdos
		// AND nmat = wsal01.nmat
		// AND aamm >= c_aamm_cum
		// AND aamm < c_fin_periode
		// AND aamm != c_cum99
		// AND rubq = w_rubbrut
		// AND nbul = 9;
		// END IF;
		// IF PA_PAIE.NouZ(per_regu) THEN
		// per_regu := 0;
		// // END IF;
		// queryString = "select count(*) from CumulPaie" +
		// " where cdos = '" + salary.getParameter().getDossier() + "'" +
		// " and nmat = '" + salary.getInfoSalary().getComp_id().getNmat() + "'" +
		// " and aamm >= '" + yearAndMonthCumulS + "'" +
		// " and aamm < '" + finPeriodeS + "'" +
		// " and aamm != '" + cum99S + "'" +
		// " and rubq = '" + salary.getParameter().getBrutRubrique() + "'" +
		// " and nbul = 9";
		// queryStringRetro = "select count(*) from Rhtprcumu" +
		// " where cdos = '" + salary.getParameter().getDossier() + "'" +
		// " and nmat = '" + salary.getInfoSalary().getComp_id().getNmat() + "'" +
		// " and aamm >= '" + yearAndMonthCumulS + "'" +
		// " and aamm < '" + finPeriodeS + "'" +
		// " and aamm != '" + cum99S + "'" +
		// " and rubq = '" + salary.getParameter().getBrutRubrique() + "'" +
		// " and nbul = 9";
		// row1 = (salary.getParameter().isUseRetroactif())? (Object)salary.getService().find(queryStringRetro).get(0):
		// (Object)salary.getService().find(queryString).get(0);
		if(StringUtils.equals("T",salary.getParameter().getRegularisationCas()))
		{
			if (salary.getParameter().isUseRetroactif())
			{
				if (yearAndMonthCumul != salary.getMyMonthOfPay().getYearAndMonthInt())
					row1 = salary.cumulMap3PourPeriode.get(key).get(salary.getParameter().getBrutRubrique());
			}
			else
				row1 = salary.cumulMap3PourPeriode.get(key).get(salary.getParameter().getBrutRubrique());
		}

		if (!ClsObjectUtil.isNull(row1))
			periodRegu = ClsObjectUtil.getIntegerFromObject(row1);

		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\n--In Rubrique Clone : ..........Initial periodRegu:" + periodRegu);
		// @23-07-2009 by yannick
		// if(periodRegu < 0)
		// periodRegu = 0;

		//
		this.salary.getParamCumul().setCumulBaseCalc(cumulBaseCalc);
		this.salary.getParamCumul().setCumulCoti(cumulCoti);
		this.salary.getParamCumul().setCumulRegu(cumulRegu);
		this.salary.getParamCumul().setNbreMoisCumul(periodRegu);
		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\n--In Rubrique Clone : ................cumulBaseCalc:" + cumulBaseCalc);
		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\n--In Rubrique Clone : ................cumulCoti:" + cumulCoti);
		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\n--In Rubrique Clone : ................cumulRegu:" + cumulRegu);
		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\n--In Rubrique Clone : ................periodRegu:" + periodRegu);
	}
	
	private double calculateFormuleBaseRegulSurCumul(String key)
	{
		List<Object[]> listOfRubBase = new ArrayList<Object[]>();
		if (!salary.getParameter().getListOfRubqBase().isEmpty() && salary.getParameter().getListOfRubqBase().containsKey(rubrique.getComp_id().getCrub()))
			listOfRubBase = salary.getParameter().getListOfRubqBase().get(rubrique.getComp_id().getCrub());
		//
		String rubk = "";
		String sign = "";
		String formule = null;
		List<Expression> parametres = new ArrayList<Expression>();
		double valeur = 0;
		double base = 0;
		Object[] row = null;
		/************GESTION DE LA NOUVELLE FORMULE STOCKE DANS LA ZONE NOTE DE LA RUBRIQUE*************/
		String nouvelleFormule = rubrique.getFormule();
		if(salary.parameter.formulelitterale)
		{
			List<String> rubriques = ExpressionEvaluator.getRubriques(nouvelleFormule);
			for(String crub : rubriques)
			{
				rubk = crub;
				if (Character.isLetter(rubk.toCharArray()[0]))
					valeur = 0;
				else
				{
					row = salary.cumulMap1PourPeriode.get(key).get(rubk);
					if (!ClsObjectUtil.isNull(row))
					{
						if ("B".equals(rubrique.getLbtm()))
							valeur = ClsObjectUtil.getDoubleFromObject(row[0]);
						else
							valeur = ClsObjectUtil.getDoubleFromObject(row[1]);
					}
					else
						valeur = 0;
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
			valeur = 0;
			rubk = (String) object[2];
			sign = (String) object[1];
			if (Character.isLetter(rubk.toCharArray()[0]))
				valeur = 0;
			else
			{
				row = salary.cumulMap1PourPeriode.get(key).get(rubk);
				if (!ClsObjectUtil.isNull(row))
				{
					if ("B".equals(rubrique.getLbtm()))
						valeur = ClsObjectUtil.getDoubleFromObject(row[0]);
					else
						valeur = ClsObjectUtil.getDoubleFromObject(row[1]);
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
			

		return base;
	}
	
	private double calculateFormuleBaseRegulSurMoisCourant()
	{
		
		List<Object[]> listOfRubBase = new ArrayList<Object[]>();
		if (!salary.getParameter().getListOfRubqBase().isEmpty() && salary.getParameter().getListOfRubqBase().containsKey(rubrique.getComp_id().getCrub()))
			listOfRubBase = salary.getParameter().getListOfRubqBase().get(rubrique.getComp_id().getCrub());
		String rubk = "";
		String sign = "";
		String formule = null;
		List<Expression> parametres = new ArrayList<Expression>();
		double valeur = 0;
		double base = 0;
		ClsRubriqueClone rubriqueClone = null;
		
		/************GESTION DE LA NOUVELLE FORMULE STOCKE DANS LA ZONE NOTE DE LA RUBRIQUE*************/
		String nouvelleFormule = rubrique.getFormule();
		if(salary.parameter.formulelitterale)
		{
			List<String> rubriques = ExpressionEvaluator.getRubriques(nouvelleFormule);
			for(String crub : rubriques)
			{
				rubk = crub;
				if (Character.isLetter(rubk.toCharArray()[0]))
					valeur = calculatePlafond(rubk);
				else
				{
					rubriqueClone = salary.findRubriqueClone(rubk);
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
				rubriqueClone = salary.findRubriqueClone(rubk);
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
					
		return base;
	}
	
	public void calculateCumulOfBaseForPeriod2()
	{
		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\n--In Rubrique Clone : >>calculateCumulOfBaseForPeriod");
		
		int fin_periode = 0;
		int yearAndMonthCumul = salary.getOPeriod().getAammExercice();
		int cum99 = yearAndMonthCumul / 100;
		cum99 = cum99 * 100 + 99;
		if (salary.getMyMonthOfPay().getYearAndMonthInt() < this.salary.getOPeriod().getFinPeriode())
			fin_periode = salary.getMyMonthOfPay().getYearAndMonthInt();
		else
		{
			fin_periode = this.salary.getOPeriod().getFinPeriode();
		}
		
		if (salary.getParameter().isUseRetroactif())
		{
			if ((yearAndMonthCumul < salary.getMyMonthOfPay().getYearAndMonthInt()) && (fin_periode == salary.getMyMonthOfPay().getYearAndMonthInt()))
				fin_periode--;
		}
		
		
		
		String yearAndMonthCumulS = ClsStringUtil.formatNumber(yearAndMonthCumul, "000000");
		String finPeriodeS = ClsStringUtil.formatNumber(fin_periode, "000000");
		String cum99S = ClsStringUtil.formatNumber(cum99, "000000");
		String key = yearAndMonthCumulS+finPeriodeS+cum99S;
		
		salary.buildSpecifiqueCumul99Map(yearAndMonthCumulS,finPeriodeS, cum99S);
		
		Object[] rowCoti = null;
		if (salary.getParameter().isUseRetroactif())
		{
			if (yearAndMonthCumul != salary.getMyMonthOfPay().getYearAndMonthInt())
				rowCoti = salary.cumulMap1PourPeriode.get(key).get(rubrique.getRcon());
		}
		else
			rowCoti = salary.cumulMap1PourPeriode.get(key).get(rubrique.getRcon());
		double cumulBaseCalc = 0;
		double cumulCoti = 0;
		double cumulRegu = 0;
		int periodRegu = 0;
		if (!ClsObjectUtil.isNull(rowCoti))
		{
			if (!ClsObjectUtil.isNull(rowCoti[0]))
				cumulBaseCalc = ClsObjectUtil.getDoubleFromObject(rowCoti[0]);
			if (!ClsObjectUtil.isNull(rowCoti[1]))
				cumulCoti = ClsObjectUtil.getDoubleFromObject(rowCoti[1]);

			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\n--In Rubrique Clone : ...........Initial cumulBaseCalc:" + cumulBaseCalc);
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\n--In Rubrique Clone : ..........Initial  cumulCoti:" + cumulCoti);

		}
		if(StringUtils.equals("O", rubrique.getBasc()))
		{
			//si la base est calcul�e, alors c'est le montant cumul� de la base qui est prise en compte
			cumulBaseCalc = this.calculateFormuleBaseRegulSurCumul(key);
		}
		
		
		Object row1 = null;
		if (salary.getParameter().isUseRetroactif())
		{
			if (yearAndMonthCumul != salary.getMyMonthOfPay().getYearAndMonthInt())
				row1 = salary.cumulMap2PourPeriode.get(key).get(rubrique.getComp_id().getCrub());
		}
		else
			row1 = salary.cumulMap2PourPeriode.get(key).get(rubrique.getComp_id().getCrub());

		if (!ClsObjectUtil.isNull(row1))
			cumulRegu = ClsObjectUtil.getDoubleFromObject(row1);
		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\n--In Rubrique Clone : ..........Initial  cumulRegu:" + cumulRegu);
		

		ClsRubriqueClone autreRub = salary.findRubriqueClone(rubrique.getRcon());
		
		double cumulBaseCalcMois = 0;
		
		// ------------from Yannick
		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\nRubrique concern� par la regul (autreRub) = " + rubrique.getRcon());
		cumulBaseCalcMois = autreRub.getBase();
		if(StringUtils.equals("O", rubrique.getBasc()))
			cumulBaseCalcMois = this.calculateFormuleBaseRegulSurMoisCourant();
		
		cumulBaseCalc += cumulBaseCalcMois;
		
		
		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\ncumulBaseCalc = cumulBaseCalc + autreRub.getBase() = " + cumulBaseCalc);
		cumulCoti += autreRub.getAmount();
		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\ncumulCoti = cumulBaseCalc + autreRub.getAmount() = " + cumulCoti);
		
		if(StringUtils.equals("T",salary.getParameter().getRegularisationCas()))
		{
			if (salary.getParameter().isUseRetroactif())
			{
				if (yearAndMonthCumul != salary.getMyMonthOfPay().getYearAndMonthInt())
					row1 = salary.cumulMap3PourPeriode.get(key).get(salary.getParameter().getBrutRubrique());
			}
			else
				row1 = salary.cumulMap3PourPeriode.get(key).get(salary.getParameter().getBrutRubrique());
		}

		if (!ClsObjectUtil.isNull(row1))
			periodRegu = ClsObjectUtil.getIntegerFromObject(row1);

		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\n--In Rubrique Clone : ..........Initial periodRegu:" + periodRegu);
		
		this.salary.getParamCumul().setCumulBaseCalc(cumulBaseCalc);
		this.salary.getParamCumul().setCumulCoti(cumulCoti);
		this.salary.getParamCumul().setCumulRegu(cumulRegu);
		this.salary.getParamCumul().setNbreMoisCumul(periodRegu);
		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\n--In Rubrique Clone : ................cumulBaseCalc:" + cumulBaseCalc);
		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\n--In Rubrique Clone : ................cumulCoti:" + cumulCoti);
		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\n--In Rubrique Clone : ................cumulRegu:" + cumulRegu);
		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\n--In Rubrique Clone : ................periodRegu:" + periodRegu);
	}

	/**
	 * => cal_pc Calcul du pourcentage de reglement d'une absence
	 * 
	 */
	public void calculatePourcentage()
	{
		// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : >>calculatePourcentage");
		// FOR i IN 1..txabs_tot LOOP
		// IF txabs_crub(i) IS NULL THEN
		// EXIT;
		// ELSE
		// IF t_rub.crub = txabs_crub(i) THEN
		// IF txabs_taux(i) = 100 THEN
		// EXIT;
		// ELSE
		// appl_tx := TRUE;
		// w_mon := w_mon * txabs_taux(i) / 100;
		// END IF;
		// END IF;
		// END IF;
		// END LOOP;
		if (salary.getParameter().getHTauxAppliedToRubriqueAbsence() != null)
		{
			Object obj = salary.getParameter().getHTauxAppliedToRubriqueAbsence().get(rubrique.getComp_id().getCrub());
			if (obj != null)
			{
				this.salary.setApplyTaux(true);
				this.salary.getValeurRubriquePartage().setAmount(this.salary.getValeurRubriquePartage().getAmount() * ClsObjectUtil.getDoubleFromObject(obj) / 100);
			}
		}
		// String[] key = null;
		// key = salary.getParameter().getHTauxAppliedToRubriqueAbsence().keySet().toArray(key);
		// for (String string : key) {
		// }
	}

	/**
	 * Calcul du prorata jour ou heure (standard) => prorat_std
	 * 
	 */
	public void calculateProrataStandard(String dateFormat)
	{
		if ('O' == salary.getParameter().getGenfile())
			salary.addToOutputtext( "\n--In Rubrique Clone : >>calculateProrataStandard");
		// nbj NUMBER(5,2);
		// nbj_du_mois NUMBER(5,2);
		//
		// nbj_ded NUMBER(5,2);
		//
		// BEGIN
		//
		// IF PA_CALCUL.w_bas30 = 'O' THEN
		// IF NOT PA_PAIE.NouZ(PA_CALCUL.w_bnbj) THEN
		// nbj_du_mois := PA_CALCUL.w_bnbj;
		// ELSE
		// nbj_du_mois := 30;
		// END IF;
		// ELSE
		// nbj_du_mois := PA_CALCUL.nbm_jrn;
		// END IF;
		int nombreJourMois = 0;
		double nombreJours = 0;
		double amount = 0;
		if ("O".equals(this.getSalary().getParameter().getBase30Rubrique()))
		{
			if (this.getSalary().getParameter().getBase30NombreJour() > 0)
			{
				nombreJourMois = this.getSalary().getParameter().getBase30NombreJour();
			}
			else
				nombreJourMois = 30;
		}
		else
			nombreJourMois = salary.getParameter().getNbreJourMoisPourProrata();
		// nbj := PA_CALCUL.wnbjt + PA_CALCUL.wnbjs;
		double i = 0;
//		if(StringUtils.equals(salary.parameter.nomClient, ClsEntreprise.SONIBANK) && this.getSalary().getNbul()!=9){
//			ClsRubriqueClone rubJrSup = salary.findRubriqueClone(salary.getParameter().getJourSupplRubrique());
//			if(rubJrSup!=null){
//				i = rubJrSup.getAmount();
//			}
//		}
		nombreJours = this.getSalary().getWorkTime().getProrataNbreJourTravaillees() + this.getSalary().getWorkTime().getNbreJoursSupplementaires() + i;
		//
		// -- Si salaire horaire, le flag prorata sur conges n'est pas actif car le nbre
		// -- de jours de travail est saisi en EV
		// IF PA_CALCUL.wsal01.cods != 'HP' AND PA_CALCUL.wsal01.cods != 'HC' THEN
		if (!"HP".equals(this.getSalary().getInfoSalary().getCods()) && !"HC".equals(this.getSalary().getInfoSalary().getCods()))
		{
			// -- MM 11/09/2000 deduction des jrs de cg ponctuel.
			// -- IF PA_CALCUL.wnbjca != 0 OR PA_CALCUL.wnbja != 0 THEN
			// IF PA_CALCUL.wnbjca != 0 OR PA_CALCUL.wnbja != 0 OR PA_CALCUL.wnbjcpa != 0 THEN
			if (this.getSalary().getWorkTime().getNbreJoursAbsencePourCongeAnnuel() != 0 || this.getSalary().getWorkTime().getNbreJourAbsence() != 0
					|| this.getSalary().getWorkTime().getNbreJourAbsencePourCongesPonctuels() != 0)
			{
				// nbj := nbj_du_mois + PA_CALCUL.wnbjs;
				nombreJours = nombreJourMois + this.getSalary().getWorkTime().getNbreJoursSupplementaires();
				// IF PA_CALCUL.t_rub.ppcg = 'N' THEN
				if ("N".equals(this.rubrique.getPpcg()))
				{
					// -- MM 11/09/2000 deduction des jrs de cg ponctuel.
					// -- nbj := nbj - PA_CALCUL.wnbjca;
					// nbj := nbj - PA_CALCUL.wnbjca - PA_CALCUL.wnbjcpa;
					nombreJours = nombreJours - this.getSalary().getWorkTime().getNbreJoursAbsencePourCongeAnnuel() - this.getSalary().getWorkTime().getNbreJourAbsencePourCongesPonctuels();
				}
				// END IF;
				// IF PA_CALCUL.t_rub.prac = 'O' THEN
				// nbj := nbj - PA_CALCUL.wnbja;
				// END IF;
				if ('O' == salary.getParameter().getGenfile())
					salary.addToOutputtext( "\n--In Rubrique Clone : Nombre de jours d'absence this.getSalary().getWorkTime().getNbreJourAbsence()= " + this.getSalary().getWorkTime().getNbreJourAbsence());
				if ("O".equals(this.rubrique.getPrac()))
				{
					// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : Nombre de jours d'absence =
					// "+this.getSalary().getWorkTime().getNbreJourAbsence());
					nombreJours -= this.getSalary().getWorkTime().getNbreJourAbsence();
				}
			}
			// END IF;
			// -- MM 12/09/2000 prise en compte des jrs de non presence en cas
			// -- de STC ou embauche.
			// nbj_ded := 0;
			long nombreDeJourDeduit = 0;
			ClsDate oDate = new ClsDate(this.getSalary().getInfoSalary().getDtes());
			// IF PA_CALCUL.Embauche THEN
			// IF PA_CALCUL.wsal01.dtes > PA_CALCUL.w_ddpa THEN
			// nbj_ded := compte_jours('A',PA_CALCUL.w_ddpa,PA_CALCUL.wsal01.dtes - 1);
			// END IF;
			// END IF;

			if (this.getSalary().isEmbauche())
			{
				// if(true){
				if (oDate.getDate().compareTo(this.getSalary().getFirstDayOfMonth()) > 0)
				{
					if ('O' == salary.getParameter().getGenfile())
						salary.addToOutputtext( "\n--In Rubrique Clone : >>DTES comes after " + this.getSalary().getMyMonthOfPay().getFirstDayOfMonth());
					nombreDeJourDeduit = this.getSalary().getUtilNomenclature().compteJours(this.getSalary().getParameter().getDossier(), ClsEnumeration.EnTypeOfDay.A, this.getSalary().getFirstDayOfMonth(),
							oDate.addDay(-1), dateFormat);
					if ('O' == salary.getParameter().getGenfile())
						salary.addToOutputtext( "\n--In Rubrique Clone : >>nombreDeJourDeduit: " + nombreDeJourDeduit);
				}
			}
			
			//Cas d'une �ch�ance de contrat
			if(salary.parameter.priseEnCompteDateFinContrat && salary.infoSalary.getDecc() != null && 
					(ClsDate.getDateS(salary.infoSalary.getDecc(), ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).compareTo(salary.parameter.getMyMonthOfPay().getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM)) == 0))
			{
				oDate = new ClsDate(this.getSalary().getInfoSalary().getDecc());
				nombreDeJourDeduit += this.getSalary().getUtilNomenclature().compteJours(this.getSalary().getParameter().getDossier(), ClsEnumeration.EnTypeOfDay.A, oDate.addDay(1),
						this.getSalary().getParameter().getLastDayOfMonth(), dateFormat);
			}
			// IF PA_CALCUL.STC THEN
			// IF PA_CALCUL.wsal01.dmrr < PA_CALCUL.w_dfpa THEN
			// nbj_ded := NVL(nbj_ded,0) + compte_jours('A',PA_CALCUL.wsal01.dmrr + 1 ,PA_CALCUL.w_dfpa);
			// END IF;
			// END IF;

			Date dtMrr = this.getSalary().getInfoSalary().getDmrr();
			if (dtMrr != null)
			{
				oDate.setDate(dtMrr);

				if (this.getSalary().getParameter().isStc())
				{
					// if(true){
					if (oDate.getDate().compareTo(this.getSalary().getLastDayOfMonth()) < 0)
					{
						if ('O' == salary.getParameter().getGenfile())
							salary.addToOutputtext( "\n--In Rubrique Clone : >>DMRR comes before " + this.getSalary().getMyMonthOfPay().getLastDayOfMonth());
						nombreDeJourDeduit += this.getSalary().getUtilNomenclature().compteJours(this.getSalary().getParameter().getDossier(), ClsEnumeration.EnTypeOfDay.A, oDate.addDay(1),
								this.getSalary().getParameter().getLastDayOfMonth(), dateFormat);
						if ('O' == salary.getParameter().getGenfile())
							salary.addToOutputtext( "\n--In Rubrique Clone : >>nombreDeJourDeduit: " + nombreDeJourDeduit);
					}
				}
			}
			// nbj := nbj - nbj_ded;
			// IF nbj < 0 THEN
			// nbj := 0;
			// END IF;
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\n--In Rubrique Clone : >>nombreJours:" + nombreJours);
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\n--In Rubrique Clone : >>nombreDeJourDeduit:" + nombreDeJourDeduit);
			nombreJours -= nombreDeJourDeduit;
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\n--In Rubrique Clone : >>nombreJours2:" + nombreJours);
			
			// sp�cifique cnss
//			if (StringUtil.notEquals(salary.parameter.nomClient, ClsEntreprise.CNSS))
//			{
//			if (nombreJours < 0)
//				nombreJours = 0;
//			}
			// -- Fin modif 12/09/2000 MM
			//
			// PA_CALCUL.w_inter := nbj * PA_CALCUL.w_mon;
			// PA_CALCUL.w_mon := PA_CALCUL.w_mon * nbj / PA_CALCUL.wnbjt;
			// PA_CALCUL.w_monhs := PA_CALCUL.w_mon;
			//
			// IF PA_CALCUL.w_tau = 0 THEN
			// PA_CALCUL.w_tau := nbj;
			// END IF;
			amount = this.getSalary().getValeurRubriquePartage().getAmount() * nombreJours / this.getSalary().getWorkTime().getProrataNbreJourTravaillees();
			this.getSalary().getWorkTime().setInter(nombreJours * this.getSalary().getValeurRubriquePartage().getAmount());
			this.getSalary().getValeurRubriquePartage().setMonhs(amount);
			this.getSalary().getValeurRubriquePartage().setAmount(amount);
			//
			amount = this.getSalary().getValeurRubriquePartage().getRates();
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\n--In Rubrique Clone : >>amount:" + amount);
			if (amount == 0)
			{
				if ('O' == salary.getParameter().getGenfile())
					salary.addToOutputtext( "\n--In Rubrique Clone : =================>Fixer le taux au nombreJours = " + nombreJours);
				this.getSalary().getValeurRubriquePartage().setRates(nombreJours);
			}
		}
		// ELSE
		else
		{
			// IF PA_CALCUL.t_rub.prhr = 'O' THEN
			if ("O".equals(this.rubrique.getPrhr()))
			{
				// IF NOT PA_PAIE.NouB(PA_CALCUL.t_rub.prtb) THEN
				// PA_CALCUL.wnbhp := prorat_jour;
				// PA_CALCUL.w_mon := PA_CALCUL.wnbht * PA_CALCUL.w_mon / PA_CALCUL.wnbhp;
				// ELSE
				// PA_CALCUL.w_mon := PA_CALCUL.wnbht * PA_CALCUL.w_mon / PA_CALCUL.wnbh;
				// END IF;
				if (!ClsObjectUtil.isNull(this.rubrique.getPrtb()))
				{
					amount = calculateProrataJour();
					
					this.getSalary().getWorkTime().setProrataNbreHeuresPayees(amount);
					amount = this.getSalary().getValeurRubriquePartage().getAmount() * this.getSalary().getWorkTime().getProrataNbreHeureTravail() / this.getSalary().getWorkTime().getProrataNbreHeuresPayees();
					this.getSalary().getValeurRubriquePartage().setAmount(amount);
				}
				else
				{
					amount = this.getSalary().getValeurRubriquePartage().getAmount() * this.getSalary().getWorkTime().getProrataNbreHeureTravail() / this.getSalary().getWorkTime().getProrataNbreHeures();
					this.getSalary().getValeurRubriquePartage().setAmount(amount);
				}

				// PA_CALCUL.w_monhs := PA_CALCUL.w_mon;
				// IF PA_CALCUL.w_tau = 0 THEN
				// PA_CALCUL.w_tau := PA_CALCUL.wnbht;
				// END IF;
				double prorataHeures = 0;
				this.getSalary().getValeurRubriquePartage().setMonhs(this.getSalary().getValeurRubriquePartage().getAmount());
				if (this.getSalary().getValeurRubriquePartage().getRates() == 0)
				{
					prorataHeures = this.getSalary().getWorkTime().getProrataNbreHeureTravail();

					if ('O' == salary.getParameter().getGenfile())
						salary.addToOutputtext( "\n--In Rubrique Clone : =================>Fixer le taux au prorataHeures = " + prorataHeures);
					this.getSalary().getValeurRubriquePartage().setRates(prorataHeures);
				}
			}
			// ELSE
			else
			{
				// IF PA_CALCUL.t_rub.prac = 'O' THEN
				if ("O".equals(this.rubrique.getPrac()))
				{
					// -- Dans le cas de la prime de transport par exemple, celle-ci est toujours
					// -- proratee sur l'activite en jours. Un salarie horaire executera donc
					// -- les instructions suivantes si le flag prorata sur jours d'activite est
					// -- a 'O' et prorata sur heures d'activite est a 'N'
					//
					// PA_CALCUL.w_mon := PA_CALCUL.w_mon * PA_CALCUL.wnbjt / nbj_du_mois;
					// PA_CALCUL.w_monhs := PA_CALCUL.w_mon;
					//
					// IF PA_CALCUL.w_tau = 0 THEN
					// PA_CALCUL.w_tau := nbj;
					// END IF;
					
					
					amount = this.getSalary().getValeurRubriquePartage().getAmount() * this.getSalary().getWorkTime().getProrataNbreJourTravaillees() / nombreJourMois;
					amount = this.getSalary().getValeurRubriquePartage().getAmount() * nombreJours / nombreJourMois;
					this.getSalary().getValeurRubriquePartage().setAmount(amount);
					this.getSalary().getValeurRubriquePartage().setMonhs(amount);
					if (this.getSalary().getValeurRubriquePartage().getRates() == 0)
					{
						if ('O' == salary.getParameter().getGenfile())
							salary.addToOutputtext( "\n--In Rubrique Clone : =================>Fixer le taux au nombreJours 2 = " + nombreJours);
						this.getSalary().getValeurRubriquePartage().setRates(nombreJours);
					}
				}
				// END IF;
			}
			// END IF;
		}
		// END IF;
		//
		// RETURN TRUE;
	}

	/**
	 * => prorat_spc Calcul du prorata jour ou heure (specifique RCI)
	 * 
	 */
	public void calculateProrataSpecifiqueRCI()
	{
		// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : >>calculateProrataSpecifiqueRCI");
		// nbj NUMBER(5,2);
		//
		double nombreDeJour = 0;
		double amount = 0;
		// BEGIN
		// nbj := PA_CALCUL.wnbjt + PA_CALCUL.wnbjs;
		nombreDeJour = this.getSalary().getWorkTime().getProrataNbreJourTravaillees() + this.getSalary().getWorkTime().getNbreJoursSupplementaires();
		//
		// -- Si salaire horaire, le flag prorata sur conges n'est pas actif car le nbre
		// -- de jours de travail est saisi en EV
		// IF PA_CALCUL.wsal01.cods != 'HP' AND PA_CALCUL.wsal01.cods != 'HC' THEN
		if (!"HP".equals(this.getSalary().getInfoSalary().getCods()) && !"HC".equals(this.getSalary().getInfoSalary().getCods()))
		{
			// -- MM 11/09/2000 deduction des jrs de cg ponctuel.
			// -- IF PA_CALCUL.wnbjca != 0 OR PA_CALCUL.wnbja != 0 THEN
			// IF PA_CALCUL.wnbjca != 0 OR PA_CALCUL.wnbja != 0 OR PA_CALCUL.wnbjcpa != 0 THEN
			if (this.getSalary().getWorkTime().getNbreJoursAbsencePourCongeAnnuel() != 0 || this.getSalary().getWorkTime().getNbreJourAbsence() != 0
					|| this.getSalary().getWorkTime().getNbreJourAbsencePourCongesPonctuels() != 0)
			{
				// IF PA_CALCUL.t_rub.ppcg = 'N' THEN
				// -- MM 11/09/2000 deduction des jrs de cg ponctuel.
				// -- nbj := nbj - PA_CALCUL.wnbjca;
				// nbj := nbj - PA_CALCUL.wnbjca - PA_CALCUL.wnbjcpa;
				// END IF;
				if ("N".equals(this.rubrique.getPpcg()))
				{
					nombreDeJour = nombreDeJour - this.getSalary().getWorkTime().getNbreJoursAbsencePourCongeAnnuel() - this.getSalary().getWorkTime().getNbreJourAbsencePourCongesPonctuels();
				}
				// IF PA_CALCUL.t_rub.prac = 'O' THEN
				// nbj := nbj - PA_CALCUL.wnbja;
				// END IF;
				if ("O".equals(this.rubrique.getPrac()))
				{
					nombreDeJour -= this.getSalary().getWorkTime().getNbreJourAbsence();
				}
				//
				// IF PA_CALCUL.w_bas30 = 'O' THEN
				// IF NOT PA_PAIE.NouZ(PA_CALCUL.w_bnbj) THEN
				// nbj := PA_CALCUL.w_bnbj;
				// ELSE
				// nbj := 30;
				// END IF;
				// ELSE
				// nbj := PA_CALCUL.nbm_jrn;
				// END IF;
				if ("O".equals(this.getSalary().getParameter().getBase30Rubrique()))
				{
					if (this.getSalary().getParameter().getBase30NombreJour() > 0)
					{
						nombreDeJour = this.getSalary().getParameter().getBase30NombreJour();
					}
					else
						nombreDeJour = 30;
				}
				else
					nombreDeJour = this.getSalary().getMyMonthOfPay().getMaxDayOfMonth();
				//
				// nbj := nbj + PA_CALCUL.wnbjs;
				//
				// IF PA_CALCUL.t_rub.ppcg = 'N' THEN
				// nbj := nbj - PA_CALCUL.wnbjca;
				// END IF;
				// IF PA_CALCUL.t_rub.prac = 'O' THEN
				// nbj := nbj - PA_CALCUL.wnbja;
				// END IF;

				nombreDeJour += this.getSalary().getWorkTime().getNbreJoursSupplementaires();
				if ("N".equals(this.rubrique.getPpcg()))
				{
					nombreDeJour -= this.getSalary().getWorkTime().getNbreJoursAbsencePourCongeAnnuel();
				}
				if ("O".equals(this.rubrique.getPrac()))
				{
					nombreDeJour -= this.getSalary().getWorkTime().getNbreJourAbsence();
				}
			}
			// END IF;
			// PA_CALCUL.w_inter := nbj * PA_CALCUL.w_mon;
			// PA_CALCUL.w_mon := PA_CALCUL.w_mon * nbj / PA_CALCUL.wnbjt;
			// PA_CALCUL.w_monhs := PA_CALCUL.w_mon;
			//
			// IF PA_CALCUL.w_tau = 0 THEN
			// PA_CALCUL.w_tau := nbj;
			// END IF;
			amount = nombreDeJour * this.getSalary().getValeurRubriquePartage().getAmount();
			this.getSalary().getWorkTime().setInter(amount);
			amount = nombreDeJour * this.getSalary().getValeurRubriquePartage().getAmount() / this.getSalary().getWorkTime().getProrataNbreJourTravaillees();
			this.getSalary().getValeurRubriquePartage().setAmount(amount);
			this.getSalary().getValeurRubriquePartage().setMonhs(amount);
			//
			amount = this.getSalary().getValeurRubriquePartage().getRates();
			if (amount == 0)
				this.getSalary().getValeurRubriquePartage().setRates(nombreDeJour);
		}
		// ELSE
		else
		{
			// IF PA_CALCUL.t_rub.prhr = 'O' THEN
			// PA_CALCUL.w_mon := PA_CALCUL.wnbht * PA_CALCUL.w_mon;
			// PA_CALCUL.w_monhs := PA_CALCUL.w_mon;
			// IF PA_CALCUL.w_tau = 0 THEN
			// PA_CALCUL.w_tau := nbj;
			// END IF;
			// END IF;
			if ("O".equals(this.rubrique.getPrhr()))
			{
				amount = this.getSalary().getWorkTime().getProrataNbreHeureTravail() * this.getSalary().getValeurRubriquePartage().getAmount();
				this.getSalary().getValeurRubriquePartage().setAmount(amount);
				this.getSalary().getValeurRubriquePartage().setMonhs(amount);
				//
				amount = this.getSalary().getValeurRubriquePartage().getRates();
				if (amount == 0)
				{
					this.getSalary().getValeurRubriquePartage().setRates(nombreDeJour);
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
		// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : >>calculateProrataFictif");
		// nbj NUMBER(5,2);
		// nbj_du_mois NUMBER(5,2);
		double nombreDeJour = 0;
		double nombreJourDuMois = 0;
		double amount = 0;
		//
		// BEGIN
		// IF PA_CALCUL.w_bas30 = 'O' THEN
		// IF NOT PA_PAIE.NouZ(PA_CALCUL.w_bnbj) THEN
		// nbj_du_mois := PA_CALCUL.w_bnbj;
		// ELSE
		// nbj_du_mois := 30;
		// END IF;
		// ELSE
		// nbj_du_mois := PA_CALCUL.nbm_jrn;
		// END IF;
		if ("O".equals(this.getSalary().getParameter().getBase30Rubrique()))
		{
			if (this.getSalary().getParameter().getBase30NombreJour() > 0)
			{
				nombreJourDuMois = this.getSalary().getParameter().getBase30NombreJour();
			}
			else
				nombreJourDuMois = 30;
		}
		else
			nombreJourDuMois = this.getSalary().getMyMonthOfPay().getMaxDayOfMonth();
		// nbj := PA_CALCUL.wnbjt + PA_CALCUL.wnbjs;
		nombreDeJour = this.getSalary().getWorkTime().getProrataNbreJourTravaillees() + this.getSalary().getWorkTime().getNbreJoursSupplementaires();
		//
		// -- CAS DES SALARIES MENSUELS
		// IF PA_CALCUL.wsal01.cods != 'HP' AND PA_CALCUL.wsal01.cods != 'HC' THEN
		if (!"HP".equals(this.getSalary().getInfoSalary().getCods()) && !"HC".equals(this.getSalary().getInfoSalary().getCods()))
		{
			// IF PA_CALCUL.wnbjca != 0 OR PA_CALCUL.wnbja != 0 THEN
			// nbj := nbj_du_mois + PA_CALCUL.wnbjs - PA_CALCUL.wnbja_cg_abs;
			// END IF;
			if (this.getSalary().getWorkTime().getNbreJoursAbsencePourCongeAnnuel() != 0 || this.getSalary().getWorkTime().getNbreJourAbsence() != 0)
				nombreDeJour = nombreJourDuMois + this.getSalary().getWorkTime().getNbreJoursSupplementaires() - this.getSalary().getWorkTime().getNbreJourAbsenceConges();
			//
			// PA_CALCUL.w_mon := PA_CALCUL.w_mon * nbj / PA_CALCUL.wnbjt;
			// PA_CALCUL.w_monhs := PA_CALCUL.w_mon;
			amount = nombreDeJour * this.getSalary().getValeurRubriquePartage().getAmount() / this.getSalary().getWorkTime().getProrataNbreJourTravaillees();
			this.getSalary().getValeurRubriquePartage().setAmount(amount);
			this.getSalary().getValeurRubriquePartage().setMonhs(amount);
			//
			// IF PA_CALCUL.w_tau = 0 THEN
			// PA_CALCUL.w_tau := nbj;
			// END IF;
			//
			amount = this.getSalary().getValeurRubriquePartage().getRates();
			if (amount == 0)
			{
				if ('O' == salary.getParameter().getGenfile())
					salary.addToOutputtext( "\n--In Rubrique Clone : =================>Fixer le taux au nombreDeJour 4 = " + nombreDeJour);
				this.getSalary().getValeurRubriquePartage().setRates(nombreDeJour);
			}
		}
		// ELSE
		else
		{
			// -- CAS DES SALARIES HORAIRES
			// -- Cas d'un prorats sur dernier mois calcul fictif.
			// -- Ce traitement sera effectue pour les horaires sans tenir compte
			// -- des flags de proratas. On considere que pour prorata sur jours
			// -- est a 'O' et prorata sur heures d'activite est a 'N.'
			// PA_CALCUL.w_mon := PA_CALCUL.w_mon * PA_CALCUL.wnbjt / nbj_du_mois;
			// PA_CALCUL.w_monhs := PA_CALCUL.w_mon;
			//
			// IF PA_CALCUL.w_tau = 0 THEN
			// PA_CALCUL.w_tau := nbj;
			// END IF;
			amount = nombreDeJour * this.getSalary().getValeurRubriquePartage().getAmount() / nombreJourDuMois;
			this.getSalary().getValeurRubriquePartage().setAmount(amount);
			this.getSalary().getValeurRubriquePartage().setMonhs(amount);
			//
			amount = this.getSalary().getValeurRubriquePartage().getRates();
			if (amount == 0)
			{
				if ('O' == salary.getParameter().getGenfile())
					salary.addToOutputtext( "\n--In Rubrique Clone : =================>Fixer le taux au nombreDeJour 5 = " + nombreDeJour);
				this.getSalary().getValeurRubriquePartage().setRates(nombreDeJour);
			}
		}
	}

	/**
	 * => prorat_jour Calcul du prorata jour si ne s'appuie pas sur T.91
	 * 
	 */
	public int calculateProrataJour()
	{
		// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : >>calculateProrataJour");
		// w_wnbhp NUMBER(5,2);
		//
		// BEGIN
		//
		// IF PA_CALCUL.t_rub.prcl != 'R' THEN
		// PA_CALCUL.char10 := concpro(PA_CALCUL.t_rub.prcl);
		// w_wnbhp := PA_CALCUL.rech_txmt(PA_CALCUL.t_rub.prtb, PA_CALCUL.char10, PA_CALCUL.t_rub.prtm, PA_CALCUL.t_rub.prno);
		// ELSE
		// PA_CALCUL.char4 := PA_CALCUL.t_rub.crub;
		// PA_CALCUL.wnbhp := PA_CALCUL.rech_txmt(PA_CALCUL.t_rub.prtb, PA_CALCUL.char4, PA_CALCUL.t_rub.prtm, PA_CALCUL.t_rub.prno);
		// END IF;
		String res = "";
		Number value = null;
		ClsEnumeration.EnColumnToRead col = ClsEnumeration.EnColumnToRead.AMOUNT;
		if (!"R".equals(this.rubrique.getPrcl()))
		{
			res = this.getSalary().concPro(this.rubrique.getPrcl());
			if ("T".equals(this.rubrique.getPrtm()))
				col = ClsEnumeration.EnColumnToRead.RATES;
			value = this.getSalary().getUtilNomenclature().getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,this.getSalary().getParameter().getDossier(), Integer.valueOf(this.rubrique.getPrtb() == null ? "0" : this.rubrique.getPrtb()),
					res, this.rubrique.getPrno() == null ? new Long(0).intValue() : this.rubrique.getPrno().intValue(), this.getSalary().getNlot(), this.getSalary().getNbul(), this.getSalary().getMonthOfPay(),
					this.getSalary().getPeriodOfPay(), col);
		}
		else
		{
			res = this.getSalary().concPro(this.rubrique.getPrcl());
			if ("T".equals(this.rubrique.getPrtm()))
				col = ClsEnumeration.EnColumnToRead.RATES;
			value = this.getSalary().getUtilNomenclature().getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,this.getSalary().getParameter().getDossier(), Integer.valueOf(this.rubrique.getPrtb() == null ? "0" : this.rubrique.getPrtb()),
					this.rubrique.getComp_id().getCrub(), this.rubrique.getPrno(), this.getSalary().getNlot(), this.getSalary().getNbul(), this.getSalary().getMonthOfPay(), this.getSalary().getPeriodOfPay(), col);
		}
		//
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
	 * convertit une cha�ne de 16 bytes en nombre
	 * 
	 * @param char16
	 *            la cha�ne � convertir
	 * @return un double
	 */
	public double convertToNumber(String char16)
	{
		
		
		// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : >>Char to convert = "+char16);
		// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : >>convertToNumber");
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
				// for (ClsRubriqueClone rub : this.getSalary().getListOfAllRubrique()) {
				// if(rubrique.equals(rub.getRubrique().getComp_id().getCrub()))
				// w_nombre = rub.getAmount();
				// }
//				if(StringUtil.notEquals(this.salary.parameter.nomClient, ClsEntreprise.COMILOG) && StringUtils.equals(rubrique, this.rubrique.getComp_id().getCrub()))
//					return this.salary.getValeurRubriquePartage().getAmount();

				ClsRubriqueClone rub = salary.findRubriqueClone(rubrique);
				if (rub != null)
				{
					w_nombre = rub.getAmount();
					// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : rubrique :" + rubrique + " montant :" +
					// w_nombre);
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
		if (salary.getParameter().isPbWithCalulation())
		{
			return 0;
		}
		return w_nombre;
	}

	/**
	 * =>recvalnum RECUPERATION VALEUR NUMERIQUE GSAL := f( indice table 30 )
	 * 
	 * @return la valeur r�cup�r�e
	 */
	public double recupValeurNumeriqueGSAL(String keys)
	{
		// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : >>recupValeurNumeriqueGSAL");
		Integer key = NumberUtils.toInt(keys);
		double res = 0;
		String char30 = "";
		// IF a_cle1 = 23 THEN
		// char30 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.nbcj,'09'));
		if (23 == key)
			char30 = ClsStringUtil.formatNumber(salary.getInfoSalary().getNbcj(), "00");
		else if (142 == key)
			char30 = ClsStringUtil.formatNumber(salary.getInfoSalary().getNbef(),"00");
		// ELSIF a_cle1 = 24 THEN
		// char30 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.nbec,'09'));
		else if (24 == key)
			char30 = ClsStringUtil.formatNumber(salary.getInfoSalary().getNbec(), "00");
		// ELSIF a_cle1 = 25 THEN
		// char30 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.nbfe,'09'));
		else if (25 == key)
			char30 = ClsStringUtil.formatNumber(salary.getInfoSalary().getNbfe(), "00");
		// ELSIF a_cle1 = 26 THEN
		// char30 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.nbpt,'09D99'));
		else if (26 == key)
			char30 = ClsStringUtil.formatNumber(salary.getInfoSalary().getNbpt(), "#.##");
		// ELSIF a_cle1 = 47 THEN
		// char30 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.indi,'099'));
		else if (47 == key)
			char30 = ClsStringUtil.formatNumber(salary.getInfoSalary().getIndi(), "000");
		// ELSIF a_cle1 = 49 THEN
		// char30 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.tinp,'099D99'));
		else if (48 == key)
			char30 = ClsStringUtil.formatNumber(salary.getInfoSalary().getTinp(), "#.##");
		// ELSIF a_cle1 = 73 THEN
		// char30 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.npie,'09'));
		else if (73 == key)
			char30 = ClsStringUtil.formatNumber(salary.getInfoSalary().getNpie(), "00");
		// ELSIF a_cle1 = 83 THEN
		// char30 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.snet,'999999999999D999'));
		else if (83 == key)
			char30 = ClsStringUtil.formatNumber(salary.getInfoSalary().getSnet() != null ?salary.getInfoSalary().getSnet() : 0, "#.###");
		// ELSIF a_cle1 = 104 THEN
		// char30 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.nbjtr,'099D99'));
		else if (104 == key)
			char30 = ClsStringUtil.formatNumber(salary.getInfoSalary().getNbjtr(), "#.##");
		// ELSIF a_cle1 = 200 THEN
		// char30 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.japa,'099D99'));
		else if (200 == key)
			char30 = ClsStringUtil.formatNumber(salary.getInfoSalary().getJapa(), "#.##");
		// ELSIF a_cle1 = 201 THEN
		// char30 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.japec,'099D99'));
		else if (201 == key)
			char30 = ClsStringUtil.formatNumber(salary.getInfoSalary().getJapec(), "#.##");
		// ELSIF a_cle1 = 202 THEN
		// char30 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.jded,'099D99'));
		else if (202 == key)
			char30 = ClsStringUtil.formatNumber(salary.getInfoSalary().getJded(), "#.##");
		// ELSIF a_cle1 = 203 THEN
		// char30 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.jrla,'099D99'));
		else if (203 == key)
			char30 = ClsStringUtil.formatNumber(salary.getInfoSalary().getJrla(), "#.##");
		// ELSIF a_cle1 = 204 THEN
		// char30 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.jrlec,'099D99'));
		else if (204 == key)
			char30 = ClsStringUtil.formatNumber(salary.getInfoSalary().getJrlec(), "#.##");
		// ELSIF a_cle1 = 205 THEN
		// char30 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.nbjsa,'099D99'));
		else if (205 == key)
			char30 = ClsStringUtil.formatNumber(salary.getInfoSalary().getNbjsa(), "#.##");
		// ELSIF a_cle1 = 206 THEN
		// char30 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.nbjse,'099D99'));
		else if (206 == key)
			char30 = ClsStringUtil.formatNumber(salary.getInfoSalary().getNbjse(), "#.##");
		// ELSIF a_cle1 = 207 THEN
		// char30 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.nbjsm,'099D99'));
		else if (207 == key)
			char30 = ClsStringUtil.formatNumber(salary.getInfoSalary().getNbjsm(), "#.##");
		// ELSIF a_cle1 = 208 THEN
		// char30 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.dapa,'999999999999D999'));
		else if (208 == key)
			char30 = ClsStringUtil.formatNumber(salary.getInfoSalary().getDapa(), "#.###");
		// ELSIF a_cle1 = 209 THEN
		// char30 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.dapec,'999999999999D999'));
		else if (209 == key)
			char30 = ClsStringUtil.formatNumber(salary.getInfoSalary().getDapec(), "#.###");
		// ELSIF a_cle1 = 210 THEN
		// char30 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.dded,'999999999999D999'));
		else if (210 == key)
			char30 = ClsStringUtil.formatNumber(salary.getInfoSalary().getDded(), "#.###");
		else if (300 == key)
			char30 = ClsStringUtil.formatNumber(salary.getAgeOfAgent(), "00");
		else if (301 == key)
			char30 = ClsStringUtil.formatNumber(salary.getAnciennete(), "00");
		else if (302 == key)
			char30 = ClsStringUtil.formatNumber(new ClsDate(salary.parameter.getMonthOfPay(),"yyyyMM").getMonth(),"00");
		// ELSIF a_cle1 >= 801 AND a_cle1 <= 899 THEN
		// char30 := SUBSTR(RecZli(PA_CALCUL.wsal01.nmat,substr(To_char(a_cle1),2,2)),1,10);
		
		else if (801 <= key && 899 > key)
		{
			String tmp = keys.substring(1, 3);
			String xx = salary.rechercheZoneLibre(Integer.valueOf(tmp));
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
			// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : ................char30 = " + char30);
			salary.getParameter().setError(salary.getParameter().errorMessage("ERR-90082", salary.getParameter().getLangue(), rubrique.getComp_id().getCrub(), salary.getInfoSalary().getComp_id().getNmat()));
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\n--In Rubrique Clone : " + "\n" + salary.getParameter().getError());
			salary.getParameter().setPbWithCalulation(true);
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
			// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : ................char30 = " + char30);
			salary.getParameter().setError(salary.getParameter().errorMessage("ERR-10525", salary.getParameter().getLangue(), rubrique.getComp_id().getCrub(), salary.getInfoSalary().getComp_id().getNmat()));
			if ('O' == salary.getParameter().getGenfile())
				salary.addToOutputtext( "\n--In Rubrique Clone : " + "\n" + salary.getParameter().getError());
			salary.getParameter().setPbWithCalulation(true);
			return char30 == null ? 0 : Double.valueOf(char30);
		}
		//
		// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : ................char30 = " + char30);
		res = convertToNumber(char30);
		// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : ................res = " + res);
		// w_retour := conv_num(char30);
		//
		return res;
	}

	/**
	 * => compar Comparaison du resultat des montants des rubriques. Permet de faire l'harmisation du montant de la rubrique et de la base plafonn�e
	 */
	public void compareResult()
	{
		// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : >>compareResult");
		// -- Chargement du montant ou de la valeur de la rubrique
		// w_comp := PA_ALGO.conv_num(PA_CALCUL.t_rub.resl);
		// z1 := NULL;
		// wsig := ' ';
		boolean valeurRub1Exist = false;
		double valeurRub1 = 0;
		double valeurRub2 = 0;
		String signe = "";
		double montantPartage = this.getSalary().getValeurRubriquePartage().getAmount();
		double valeurComparaison = this.convertToNumber(this.rubrique.getResl());

		// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : ...montantPartage :" + montantPartage);
		// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : ...valeurComparaison :" + valeurComparaison);
		//
		// IF PA_CALCUL.w_mon > w_comp THEN
		// IF NOT PA_PAIE.NouB(PA_CALCUL.t_rub.sup1) THEN
		// z1 := PA_ALGO.conv_num(PA_CALCUL.t_rub.sup1);
		// z2 := PA_ALGO.conv_num(PA_CALCUL.t_rub.sup2);
		// wsig := PA_CALCUL.t_rub.sups;
		// END IF;
		// ELSIF PA_CALCUL.w_mon < w_comp THEN
		// IF NOT PA_PAIE.NouB(PA_CALCUL.t_rub.inf1) THEN
		// z1 := PA_ALGO.conv_num(PA_CALCUL.t_rub.inf1);
		// z2 := PA_ALGO.conv_num(PA_CALCUL.t_rub.inf2);
		// wsig := PA_CALCUL.t_rub.infs;
		// END IF;
		// ELSIF PA_CALCUL.w_mon = w_comp THEN
		// IF NOT PA_PAIE.NouB(PA_CALCUL.t_rub.egu1) THEN
		// z1 := PA_ALGO.conv_num(PA_CALCUL.t_rub.egu1);
		// z2 := PA_ALGO.conv_num(PA_CALCUL.t_rub.egu2);
		// wsig := PA_CALCUL.t_rub.egus;
		// END IF;
		// END IF;
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
		// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : ...valeurRub1 :" + valeurRub1);
		// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : ...valeurRub2 :" + valeurRub2);
		// if('O' == salary.getParameter().getGenfile()) salary.outputtext+="\n--In Rubrique Clone : ...signe :" + signe);
		//
		// IF wsig = '+' THEN
		// PA_CALCUL.w_mon := z1 + z2;
		// PA_CALCUL.w_basp := PA_CALCUL.w_mon;
		// ELSIF wsig = '-' THEN
		// PA_CALCUL.w_mon := z1 - z2;
		// PA_CALCUL.w_basp := PA_CALCUL.w_mon;
		// ELSIF wsig = '*' THEN
		// PA_CALCUL.w_mon := z1 * z2;
		// PA_CALCUL.w_basp := PA_CALCUL.w_mon;
		// ELSIF wsig = '/' THEN
		// IF z2 <> 0 THEN
		// PA_CALCUL.w_mon := z1 / z2;
		// PA_CALCUL.w_basp := PA_CALCUL.w_mon;
		// ELSE
		// PA_CALCUL.w_mon := 0;
		// PA_CALCUL.w_basp := PA_CALCUL.w_mon;
		// END IF;
		// ELSIF PA_PAIE.NouB(wsig) AND z1 IS NOT NULL THEN
		// PA_CALCUL.w_mon := z1;
		// PA_CALCUL.w_basp := PA_CALCUL.w_mon;
		// END IF;
		if ("+".equals(signe))
		{
			this.getSalary().getValeurRubriquePartage().setAmount(valeurRub1 + valeurRub2);
			this.getSalary().getValeurRubriquePartage().setBasePlafonnee(valeurRub1 + valeurRub2);
		}
		else if ("-".equals(signe))
		{
			this.getSalary().getValeurRubriquePartage().setAmount(valeurRub1 - valeurRub2);
			this.getSalary().getValeurRubriquePartage().setBasePlafonnee(valeurRub1 - valeurRub2);
		}
		else if ("*".equals(signe))
		{
			this.getSalary().getValeurRubriquePartage().setAmount(valeurRub1 * valeurRub2);
			this.getSalary().getValeurRubriquePartage().setBasePlafonnee(valeurRub1 * valeurRub2);
		}
		else if ("/".equals(signe))
		{
			if (valeurRub2 != 0)
			{
				this.getSalary().getValeurRubriquePartage().setAmount(valeurRub1 / valeurRub2);
				this.getSalary().getValeurRubriquePartage().setBasePlafonnee(valeurRub1 / valeurRub2);
			}
			else
			{
				this.getSalary().getValeurRubriquePartage().setAmount(0);
				this.getSalary().getValeurRubriquePartage().setBasePlafonnee(0);
			}
		}
		else if (ClsObjectUtil.isNull(signe) && valeurRub1Exist)
		{
			this.getSalary().getValeurRubriquePartage().setAmount(valeurRub1);
			this.getSalary().getValeurRubriquePartage().setBasePlafonnee(valeurRub1);
		}
	}

	/**
	 * construit un message d'erreur pour le mettre dans le param
	 * 
	 * @param codeError
	 */
	void setError(String codeError)
	{
		String error = salary.getParameter().errorMessage(codeError, salary.getParameter().getLangue(), this.getRubrique().getAlgo(), salary.getInfoSalary().getComp_id().getNmat(),
				this.getRubrique().getComp_id().getCrub());
		salary.getParameter().setError(error);
	}
}
