package com.kinart.paie.business.services.calcul;

import com.kinart.paie.business.services.utils.*;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Cette classe permet de traiter les paramétres de temps d'un agent pour le calcul de son salaire. Ces paramétres sont par exemple, le nombre d'heures
 * travaillées, le nombre de jours de congés annuels, etc. Chaque instance de salarié contient qu'une seule instance de cette classe qui permet de contenir ses
 * paramétres de temps.
 * 
 * @author c.mbassi
 * 
 */
public class ClsSalaryWorkTime
{
	// nombre heures trvaillées
	private double workedHour = 0;

	// wnbjc := 0; -- nb jours conges annuels
	private double nbreJourConges = 0;

	// wnbjcpnp := 0; -- nb jours conges payes non pris
	private double nbreJourCongesNonPris = 0;

	// wnbjcp := 0; -- nb jours conges ponctuels (ouvrables)
	private double nbreJourCongesPonctuels = 0;

	// wnbjcpa := 0; -- nb jours conges ponctuels (calendaires)
	private double nbreJourAbsencePourCongesPonctuels = 0;

	// wmntcp := 0; -- montant des conges payes
	private double montantCongePonctuel = 0;

	// nbm_sup := 0; -- nombre de mois suppl. (extrapolation de calcul)
	private double nbreMoisSuppl = 0;

	// wnbja := 0; -- nb jours absences
	private double nbreJourAbsence = 0;

	// wnbjca := 0;
	private double nbreJoursAbsencePourCongeAnnuel = 0;

	// wnbjsmm := 0;
	private double nbreJourCongesSalaireMoyMois = 0;

	// wnbjsmmpnp := 0;
	private double nbreJourPayeSupplPayeNonPris = 0;

	// wnbjcapnp := 0;
	private double nbreJourCongesAnnuelPayeNonPris = 0;

	// wnbjs := 0;
	private double nbreJoursSupplementaires = 0;

	// debmois := FALSE;
	private boolean debutDeMois = false;

	// wnbjc_ms := 0; -- nb jours conges annuels mois suivant
	private double nbreJourCongesAnnuelMoisSuiv = 0;

	// wnbjsmm_ms := 0; -- nb jours conges salaire moyen mensuel mois suivant
	private double nbreJourCongesSalaireMoyMoisSuiv = 0;

	// wnbjc_ma := 0; -- nb jours conges annuels mois anterieurs
	private double nbreJourCongesAnnuelMoisAnte = 0;

	// wnbja_ma := 0; -- nb jours absence conges annuels mois anterieurs
	private double nbreJourAbsenceCongesAnnuelMoisAnte = 0;

	// wnbja_cg_abs := 0;
	private double nbreJourAbsenceConges = 0;

	// nombre de périodes de régulation
	private double nbrePeriodRegu = 0;

	// elements variables congés existe
	private boolean elementVariableCGExist = false;

	// nombre heures travaillées pour prorata
	// wnbht
	private double prorataNbreHeureTravail = 0;

	// nombre de jours travaillés pour prorata
	// wnbjt
	private double prorataNbreJourTravaillees = 0;

	// nombre d'heures travaillés pour prorata
	// wnbhp
	private double prorataNbreHeuresPayees = 0;

	// nombre d'heures pour prorata
	// wnbh
	private double prorataNbreHeures = 0;

	// w_inter
	private double inter = 0;

	/** ******** CODES D4ABSENCE ************** */
	int[] codeAbsence = null;

	private Number tempNumber = null;
	
	//----------------SPECIFIQUE CNSS------------------------------------
	// --TFN 29/11/2007 Rajout pour le calcul des congés en montant
	// wnbj_acquis NUMBER(5,2);
	// wmont_acquis NUMBER(15,3);
	// wnbja_donnant_conge NUMBER(5,2); --TFN 29/01/2007 Indique le nombre de jours d'absence donnant droit é congé.
	private double nbrJoursAcquis;
	private double montantAcquis;
	private double nbrJoursAbsencesDonnantConge = 0;

	public synchronized double getNbreJourCongesSalaireMoyMois()
	{
		return nbreJourCongesSalaireMoyMois;
	}

	public int[] getCodeAbsence()
	{
		return codeAbsence;
	}

	public void setCodeAbsence(int[] codeAbsence)
	{
		this.codeAbsence = codeAbsence;
	}

	public void setNbreJourCongesSalaireMoyMois(double nbreJourCongesSalaireMoyMois)
	{
		this.nbreJourCongesSalaireMoyMois = nbreJourCongesSalaireMoyMois;
	}

	public void setProrataNbreJourTravaillees(double prorataNbreJourTravaillees)
	{
		this.prorataNbreJourTravaillees = prorataNbreJourTravaillees;
	}

	public double getNbreJoursAbsencePourCongeAnnuel()
	{
		return nbreJoursAbsencePourCongeAnnuel;
	}

	public void setNbreJoursAbsencePourCongeAnnuel(double nbreJoursAbsencePourCongeAnnuel)
	{
		this.nbreJoursAbsencePourCongeAnnuel = nbreJoursAbsencePourCongeAnnuel;
	}

	public double getProrataNbreHeures()
	{
		return prorataNbreHeures;
	}

	public void setProrataNbreHeures(double prorataNbreHeures)
	{
		this.prorataNbreHeures = prorataNbreHeures;
	}

	public double getProrataNbreHeuresPayees()
	{
		return prorataNbreHeuresPayees;
	}

	public void setProrataNbreHeuresPayees(double prorataNbreHeuresPayees)
	{
		this.prorataNbreHeuresPayees = prorataNbreHeuresPayees;
	}

	public double getInter()
	{
		return inter;
	}

	public void setInter(double inter)
	{
		this.inter = inter;
	}

	public double getNbreJourPayeSupplPayeNonPris()
	{
		return nbreJourPayeSupplPayeNonPris;
	}

	public void setNbreJourPayeSupplPayeNonPris(double nbreJourPayeSupplPayeNonPris)
	{
		this.nbreJourPayeSupplPayeNonPris = nbreJourPayeSupplPayeNonPris;
	}

	public double getNbreJourCongesAnnuelPayeNonPris()
	{
		return nbreJourCongesAnnuelPayeNonPris;
	}

	public void setNbreJourCongesAnnuelPayeNonPris(double nbreJourCongesAnnuelPayeNonPris)
	{
		this.nbreJourCongesAnnuelPayeNonPris = nbreJourCongesAnnuelPayeNonPris;
	}

	public double getNbreJoursSupplementaires()
	{
		return nbreJoursSupplementaires;
	}

	public void setNbreJoursSupplementaires(double nbreJoursSupplementaires)
	{
		this.nbreJoursSupplementaires = nbreJoursSupplementaires;
	}

	public double getProrataNbreHeureTravail()
	{
		return prorataNbreHeureTravail;
	}

	public void setProrataNbreHeureTravail(double prorataNbreHeureTravail)
	{
		this.prorataNbreHeureTravail = prorataNbreHeureTravail;
	}

	public double getProrataNbreJourTravaillees()
	{
		return prorataNbreJourTravaillees;
	}

	// public void setProrataNbreJourTravail(int prorataNbreJourTravail) {
	// this.prorataNbreJourTravaillees = prorataNbreJourTravail;
	// }
	public boolean isElementVariableCGExist()
	{
		return elementVariableCGExist;
	}

	public void setElementVariableCGExist(boolean elementVariableCGExist)
	{
		this.elementVariableCGExist = elementVariableCGExist;
	}

	// public int getNbreJourCongesSalaireMoyMois() {
	// return nbreJourCongesSalaireMoyMois;
	// }
	// public void setWnbjsmm(int nbreJourCongesSalaireMoyMois) {
	// this.nbreJourCongesSalaireMoyMois = nbreJourCongesSalaireMoyMois;
	// }
	public boolean isDebutDeMois()
	{
		return debutDeMois;
	}

	public void setDebutDeMois(boolean debutDeMois)
	{
		this.debutDeMois = debutDeMois;
	}

	public double getNbrePeriodRegu()
	{
		return nbrePeriodRegu;
	}

	public void setNbrePeriodRegu(double nbrePeriodRegu)
	{
		this.nbrePeriodRegu = nbrePeriodRegu;
	}

	public double getNbreJourAbsence()
	{
		return nbreJourAbsence;
	}

	public void setNbreJourAbsence(double nbreJourAbsence)
	{
		this.nbreJourAbsence = nbreJourAbsence;
	}

	public double getNbreJourAbsenceConges()
	{
		return nbreJourAbsenceConges;
	}

	public void setNbreJourAbsenceConges(double nbreJourAbsenceConges)
	{
		this.nbreJourAbsenceConges = nbreJourAbsenceConges;
	}

	public double getNbreJourAbsenceCongesAnnuelMoisAnte()
	{
		return nbreJourAbsenceCongesAnnuelMoisAnte;
	}

	public void setNbreJourAbsenceCongesAnnuelMoisAnte(double nbreJourAbsenceCongesAnnuelMoisAnte)
	{
		this.nbreJourAbsenceCongesAnnuelMoisAnte = nbreJourAbsenceCongesAnnuelMoisAnte;
	}

	public double getNbreJourConges()
	{
		return nbreJourConges;
	}

	public void setNbreJourConges(double nbreJourConges)
	{
		this.nbreJourConges = nbreJourConges;
	}

	public double getNbreJourCongesAnnuelMoisAnte()
	{
		return nbreJourCongesAnnuelMoisAnte;
	}

	public void setNbreJourCongesAnnuelMoisAnte(double nbreJourCongesAnnuelMoisAnte)
	{
		this.nbreJourCongesAnnuelMoisAnte = nbreJourCongesAnnuelMoisAnte;
	}

	public double getNbreJourCongesAnnuelMoisSuiv()
	{
		return nbreJourCongesAnnuelMoisSuiv;
	}

	public void setNbreJourCongesAnnuelMoisSuiv(double nbreJourCongesAnnuelMoisSuiv)
	{
		this.nbreJourCongesAnnuelMoisSuiv = nbreJourCongesAnnuelMoisSuiv;
	}

	public double getNbreJourCongesNonPris()
	{
		return nbreJourCongesNonPris;
	}

	public void setNbreJourCongesNonPris(double nbreJourCongesNonPris)
	{
		this.nbreJourCongesNonPris = nbreJourCongesNonPris;
	}

	public double getMontantCongePonctuel()
	{
		return montantCongePonctuel;
	}

	public void setMontantCongePonctuel(double montantCongePonctuel)
	{
		this.montantCongePonctuel = montantCongePonctuel;
	}

	public double getNbreJourAbsencePourCongesPonctuels()
	{
		return nbreJourAbsencePourCongesPonctuels;
	}

	public void setNbreJourAbsencePourCongesPonctuels(double nbreJourAbsencePourCongesPonctuels)
	{
		this.nbreJourAbsencePourCongesPonctuels = nbreJourAbsencePourCongesPonctuels;
	}

	public double getNbreJourCongesPonctuels()
	{
		return nbreJourCongesPonctuels;
	}

	public void setNbreJourCongesPonctuels(double nbreJourCongesPonctuels)
	{
		this.nbreJourCongesPonctuels = nbreJourCongesPonctuels;
	}

	public double getNbreJourCongesSalaireMoyMoisSuiv()
	{
		return nbreJourCongesSalaireMoyMoisSuiv;
	}

	public void setNbreJourCongesSalaireMoyMoisSuiv(double nbreJourCongesSalaireMoyMoisSuiv)
	{
		this.nbreJourCongesSalaireMoyMoisSuiv = nbreJourCongesSalaireMoyMoisSuiv;
	}

	public double getNbreMoisSuppl()
	{
		return nbreMoisSuppl;
	}

	public void setNbreMoisSuppl(double nbreMoisSuppl)
	{
		this.nbreMoisSuppl = nbreMoisSuppl;
	}

	public double getWorkedHour()
	{
		return workedHour;
	}

	public void setWorkedHour(double workedHour)
	{
		this.workedHour = workedHour;
	}

	/**
	 * Calcule le nombre de périodes de régulation d'un salarié. Cette fonction met é jour nbrePeriodRegu.
	 * 
	 * @param salary
	 *            le salarié dont on calcule le nombre de périodes
	 */
	public void calculNbrePeriodeRegulation(ClsSalaryToProcess salary)
	{
		ParameterUtil.println(">>calculNbrePeriodeRegulation");
		// nbper_regu := 0;
		double i = 0;
		this.setNbrePeriodRegu(0);
		String rubrique = salary.getParameter().getRegularisationRubrique();
		// IF NOT PA_PAIE.NouB(rub_regper) THEN
		if (rubrique != null && rubrique.trim() != "")
		{
			// nbper_regu := paf_LecEvar(wsal01.nmat,w_aamm,wsd_fcal1.nbul,rub_regper);
			tempNumber = salary.getUtilNomenclature().getSumOfAmountOfVariableElement(salary.getParameter().getDossier(), salary.getInfoSalary().getComp_id().getNmat(), salary.getMoisPaieCourant(),
					salary.getPeriodOfPay(), rubrique, salary.getNbul());
			if (tempNumber != null)
				i = tempNumber.doubleValue();
			// IF PA_PAIE.NouZ(nbper_regu) THEN
			if (i < 0)
				// nbper_regu := 0;
				i = 0;
			// END IF;
		}
		// END IF;
		this.setNbrePeriodRegu(i);
	}

	/**
	 * Calcule le nombre de mois supplémentaires. Cette fonction met é jour nbreMoisSuppl.
	 * 
	 * @param salary
	 *            le salarié dont on calcule le nombre de périodes
	 */
	public void calculateNbreMoisSuppl(ClsSalaryToProcess salary)
	{
		ParameterUtil.println(">>calculateNbreMoisSuppl");
		int premierJourMois = 0;
		int dernierJourMois = 0;
		this.setNbreMoisSuppl(0);
		// IF retroactif THEN
		// BEGIN
		// SELECT ddpa, dfpa
		// INTO w_ddpa, w_dfpa
		// FROM pahev
		// WHERE cdos = wpdos.cdos
		// AND nmat = wsal01.nmat
		// AND aamm = w_aamm
		// AND nbul = wsd_fcal1.nbul;
		// EXCEPTION
		// WHEN NO_DATA_FOUND THEN
		// -- MM 13/12/2000 Aucun sens !!!!!!! et en plus ca fout la merde...
		// -- w_ddpa := NULL;
		// -- w_dfpa := NULL;
		// null;
		// -- Fin Modif MM.
		// END;
		// ELSE
		// BEGIN
		// SELECT ddpa, dfpa
		// INTO w_ddpa, w_dfpa
		// FROM paev
		// WHERE cdos = wpdos.cdos
		// AND nmat = wsal01.nmat
		// AND aamm = w_aamm
		// AND nbul = wsd_fcal1.nbul;
		// EXCEPTION
		// WHEN NO_DATA_FOUND THEN
		// -- MM 13/12/2000 Aucun sens !!!!!!! et en plus ca fout la merde...
		// -- w_ddpa := NULL;
		// -- w_dfpa := NULL;
		// null;
		// -- Fin Modif MM.
		// END;
		// END IF;
		String queryStringRetro = "select ddpa, dfpa from Rhthev" + " where identreprise = '" + salary.getParameter().getDossier() + "'" + " and nmat = '" + salary.getInfoSalary().getComp_id().getNmat() + "'"
				+ " and aamm = '" + salary.getMonthOfPay() + "'" + " and nbul = " + salary.getNbul();
		String queryString = "select ddpa, dfpa from ElementVariableEnteteMois" + " where identreprise = '" + salary.getParameter().getDossier() + "'" + " and nmat = '" + salary.getInfoSalary().getComp_id().getNmat() + "'"
				+ " and aamm = '" + salary.getMonthOfPay() + "'" + " and nbul = " + salary.getNbul();
		//
		List listOfEv = (salary.getParameter().isUseRetroactif()) ? salary.getService().find(queryStringRetro) : salary.getService().find(queryString);
		Object[] rowEv = null;
		if (listOfEv != null && listOfEv.size() > 0)
		{
			rowEv = (Object[]) listOfEv.get(0);
			if (rowEv[0] != null && rowEv[1] != null)
			{
				//Ajout Yannick le 29/08/2012
				salary.setFirstDayOfMonth((Date)rowEv[0]);
				salary.setLastDayOfMonth((Date)rowEv[1]);
				
				salary.parameter.setFirstDayOfMonth((Date)rowEv[0]);
				salary.parameter.setLastDayOfMonth((Date)rowEv[1]);
			}
			// IF NOT tab91 THEN
			if (!salary.getParameter().isTable91LabelNotEmpty())
			{
				// -- Permet de calculer le nombre de mois supplementaire
				// IF w_ddpa IS NOT NULL THEN
				if (rowEv[0] != null && rowEv[1] != null)
				{

					// mois_deb := TO_NUMBER(TO_CHAR(w_ddpa,'MM'));
					premierJourMois = new ClsDate((Date) rowEv[0]).getMonth();
					// mois_fin := TO_NUMBER(TO_CHAR(w_dfpa,'MM'));
					dernierJourMois = new ClsDate((Date) rowEv[1]).getMonth();
					// IF mois_fin < mois_deb THEN
					if (dernierJourMois < premierJourMois)
						// mois_fin := TO_NUMBER(TO_CHAR(wpdos.ddex,'MM'));
						dernierJourMois = new ClsDate(salary.getParameter().getDossierDateDebutExercice(), salary.getParameter().appDateFormat).getMonth();
					// END IF;
					// nbm_sup := mois_fin - mois_deb;
					//this.setNbreMoisSuppl(dernierJourMois - premierJourMois);
				}
				// END IF;
			}
			// END IF;
		}
		// this.setNbreMoisSuppl(0);
		ParameterUtil.println("............... setNbreMoisSuppl:" + this.getNbreMoisSuppl());
	}

	/**
	 * Calcule le nombre de jours payes non pris. Cette fonction met é jour nbreJourPayeNonPrisRubrique.
	 * 
	 * @param salary
	 *            le salarié dont on calcule le nombre de périodes
	 */
	public void calculNbreJourPaidNonPris(ClsSalaryToProcess salary)
	{
		ParameterUtil.println(">>calculNbreJourPaidNonPris");
		// wnbjcapnp := paf_LecEvar(wsal01.nmat,w_aamm,wsd_fcal1.nbul,rub_pnp);
		double i = 0;
		tempNumber = salary.getUtilNomenclature().getSumOfAmountOfVariableElement(salary.getParameter().getDossier(), salary.getInfoSalary().getComp_id().getNmat(), salary.getMoisPaieCourant(), salary.getPeriodOfPay(),
				salary.getParameter().getJourPayeNonPrisRubrique(), salary.getNbul());
		if (tempNumber != null)
			i = tempNumber.doubleValue();
		this.setNbreJourCongesAnnuelPayeNonPris(i);

		//
		// IF PA_PAIE.NouZ(wnbjcapnp) THEN
		if (i == 0)
		{
			// wnbjcapnp := 0;
			this.setNbreJourCongesAnnuelPayeNonPris(0);
			// wnbjcpnp := 0;
			this.setNbreJourCongesNonPris(0);
			// wnbjsmmpnp := 0;
			this.setNbreJourPayeSupplPayeNonPris(i);
		}
		// ELSE
		else
		{
			// wnbjcpnp := wnbjcapnp;
			this.setNbreJourCongesNonPris(i);
			// wnbjsmmpnp := wnbjcapnp;
			this.setNbreJourPayeSupplPayeNonPris(i);
		}
		// END IF;
	}

	/**
	 * Calcule le nombre de jours supplementaires. Cette fonction met é jour nbreJourSuppl.
	 * 
	 * @param salary
	 *            le salarié dont on calcule le nombre de périodes
	 */
	public void calculateNbreJourSuppl(ClsSalaryToProcess salary)
	{
		ParameterUtil.println(">>calculateNbreJourSuppl");
		// wnbjs := paf_LecEvar(wsal01.nmat,w_aamm,wsd_fcal1.nbul,rub_nbjs);
		double i = 0;

		tempNumber = salary.getUtilNomenclature().getSumOfAmountOfVariableElement(salary.getParameter().getDossier(), salary.getInfoSalary().getComp_id().getNmat(), salary.getMoisPaieCourant(), salary.getPeriodOfPay(),
				salary.getParameter().getJourSupplRubrique(), salary.getNbul());
		if (tempNumber != null)
			i = tempNumber.doubleValue();
		
		this.setNbreJoursSupplementaires(i);
		//
		// IF PA_PAIE.NouZ(wnbjs) THEN
		if (i == 0)
			// wnbjs := 0;
			this.setNbreJoursSupplementaires(0);
		// END IF;
	}

	/**
	 * calcule le nombre de jours de congé d'un salarié é partir des éléments variables congé historisés ou non. Cette fonction met é jour
	 * nbreJourPayeNonPrisRubrique.
	 * 
	 * @param salary
	 *            le salarié dont on calcule le nombre de périodes
	 * @throws Exception
	 */
	public void calculateNbreJourCongeEtAbsence(ClsSalaryToProcess salary) throws Exception
	{

		ParameterUtil.println(">>calculateNbreJourCongeEtAbsence");
		String nmat = salary.getInfoSalary().getComp_id().getNmat();
		String queryString = "from ElementVariableConge" + " where identreprise='" + salary.getParameter().getDossier() + "'" + " and nmat ='" + nmat + "'" + " and aamm ='" + salary.getMonthOfPay() + "'" + " and nbul ="
				+ salary.getNbul();

		String queryStringRetro = "from Rhthevcg" + " where identreprise='" + salary.getParameter().getDossier() + "'" + " and nmat ='" + nmat + "'" + " and aamm ='" + salary.getMonthOfPay() + "'" + " and nbul ="
				+ salary.getNbul();

		ClsDate myMonthOfPay = new ClsDate(salary.getMonthOfPay(), ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
		Date dtLastDayOfMonthPay = salary.getLastDayOfMonth();
		Date dtFirstDayOfMonthPay = salary.getFirstDayOfMonth();
		// liste des éléments variables congés historisés ou non
		List listOfElementVariableConge = (salary.getParameter().isUseRetroactif()) ? salary.getService().find(queryStringRetro) : salary.getService().find(queryString);
		ClsElementVariableCongeClone elementVariableConge = null;
		double nbr = 0;
		try
		{
			for (Object obj : listOfElementVariableConge)
			{
				elementVariableConge = ClsElementVariableCongeClone.clone(obj);
				elementVariableCGExist = true;
				//
				// nb_evcg := nb_evcg + 1;
				// -- Memorisation des dates de debut et fin de paie
				// -- w_ddpa := wevcg.ddpa;
				// -- w_dfpa := wevcg.dfpa;
				//
				// -- Lecture du code absence en table 22
				// IF NOT PA_CALC_ANX.lec_codeAbs(wevcg.motf)
				// THEN
				// pb_calcul := TRUE;
				// err_msg := PA_PAIE.erreurp('ERR-90049',w_clang,wsal01.nmat,wevcg.motf);
				// EXIT;
				// END IF;
				this.readAbsenceCode(salary, elementVariableConge.getMotf()); 
				//
				// IF PA_PAIE.NouZ(wfnom.mnt4) THEN
				// wfnom.mnt4 := 0;
				// END IF;
				// IF wfnom.mnt1 = 0 THEN
				if (codeAbsence[0] == 0)
				{
					// ----------- TRAITEMENT DES ABSENCES --------------------------------
					// -- On ne tient pas compte des absences dont mnt4 = 1 ou qui sont
					// -- superieure au mois de paie
					// IF wfnom.mnt4 != 1 AND
					// -- wevcg.dfin <= w_dfpa
					// -- LH 230298
					// wevcg.dfin <= Dernier_Jour_du_Mois
					// THEN
					boolean test = (codeAbsence[3] != 1 && ((elementVariableConge.getDfin() == null && dtLastDayOfMonthPay != null) || elementVariableConge.getDfin().getTime() <= dtLastDayOfMonthPay.getTime()));
					if(StringUtils.equals(TypeBDUtil.typeBD,TypeBDUtil.MS))
						test = (codeAbsence[3] != 1 && ((elementVariableConge.getDfin() == null && dtLastDayOfMonthPay != null) || elementVariableConge.getDfin().compareTo(dtLastDayOfMonthPay) <= 0));
					//if (codeAbsence[3] != 1 && ((elementVariableConge.getDfin() == null && dtLastDayOfMonthPay != null) || elementVariableConge.getDfin().getTime() <= dtLastDayOfMonthPay.getTime()))
					
					if (test)
					{
						// -- Absence a prendre en compte
						// wnbja := wnbja + wevcg.nbja;
						nbr = elementVariableConge.getNbja().doubleValue();
						//traitement du taux 1 : % de reglement de l'absence; (pour l'instant je le fais pour SDV)
						//En principe, le taux 1  sert é déterminer la proportion du nombre de jours é payer pour la rubrique saisie en mnt8
//						if (StringUtils.equals(salary.parameter.nomClient, ClsEntreprise.SDV_NIGER))
//						{
						//On met dans le taux 2 de la table 22 1/0 suivant que l'on veut que le taux 1 proratise les autres rubriques de la paie, pas seulement celle en mnt 8 
						
						
						if (StringUtil.notEquals(salary.parameter.nomClient, "COMILOG"))
						{
							if (codeAbsence[9] == 0)
							{
								if (codeAbsence[8] >= 0)
								{
									double p = (float) codeAbsence[8] / (float) 100;
									p = 1 - p;
									p = p * nbr;
									nbr = new BigDecimal(p).doubleValue();
								}
							}
						}
//						}
						nbreJourAbsence +=  nbr;
						
						// spécifique cnss
						if (StringUtils.equals(salary.parameter.nomClient, "CNSS"))
						{
//							 --TFN 29/01/2007 certaine absences donnent droit é congé.
//							    IF wfnom.mnt6 = 1 THEN
//								wnbja_donnant_conge := 	 wnbja_donnant_conge  + wevcg.nbja;
//							    END IF;
							if(codeAbsence[5] == 1)
								nbrJoursAbsencesDonnantConge += elementVariableConge.getNbja().doubleValue();
							
						}
						
						//
						// IF wevcg.motf = w_cg_abs
						// THEN
						if (elementVariableConge.getMotf().equals(salary.getParameter().getBaseCongeCodeMotif()))
							// -- On est sur une absence generee par un conges en automatique
							// -- wnbja_cg_abs sera utilise par prorat_fic.
							// wnbja_cg_abs := wnbja_cg_abs + wevcg.nbja;
							nbreJourAbsenceConges += elementVariableConge.getNbja().doubleValue();
						// END IF;
						//
						// -- L'absence genere un paiement sur la rubrique stockee dans mnt8
						// IF NOT PA_PAIE.NouZ(wfnom.mnt8) THEN
						if (codeAbsence[7] > 0)
						{
							// Rbq_EV := LTRIM(TO_CHAR(wfnom.mnt8,'0999'));
							String rubrique = ClsStringUtil.formatNumber(codeAbsence[7], ParameterUtil.formatRubrique);
							// Pseudo_Ev(Rbq_Ev, wevcg.nbja);
							salary.generatePseudoEv(rubrique, rubrique, "PSEUDO-EV", "", "", elementVariableConge.getNbja().doubleValue());
						}
						// ELSE
						else
							// Pseudo_Ev(w_rubjnp, wevcg.nbja);
							salary.generatePseudoEv(salary.getParameter().getJourNonPayeRubrique(), salary.getParameter().getJourNonPayeRubrique(), "PSEUDO-EV", "", "", elementVariableConge.getNbja().doubleValue());
						// END IF;
						//
						// IF NOT PA_PAIE.NouZ(w_rubabs) THEN
						String rubabs = salary.getParameter().getAbsenceMoisRubrique();
						if (rubabs != null && rubabs.trim() != "" && StringUtil.notEquals(ParameterUtil.formatRubrique, rubabs))
							// Pseudo_Ev(w_rubabs, wevcg.nbja);
							salary.generatePseudoEv(rubabs, rubabs, "PSEUDO-EV", "", "", elementVariableConge.getNbja().doubleValue());
						// END IF;
						
					}
					// END IF;
				}
				// ELSE
				else
				{
					// ----------- TRAITEMENT DES CONGES --------------------------------
					// IF wevcg.ddeb >= w_ddpa AND wevcg.dfin <= w_dfpa THEN
//					if (dtFirstDayOfMonthPay.compareTo(elementVariableConge.getComp_id().getDdeb()) <= 0 && dtLastDayOfMonthPay.compareTo(elementVariableConge.getDfin()) >= 0)
					if ((dtLastDayOfMonthPay.compareTo(elementVariableConge.getDfin()) >= 0 && (StringUtils.equals(salary.parameter.nomClient, "BDU") || StringUtils.equals(salary.parameter.nomClient, "BGFIGE")))
							|| (dtFirstDayOfMonthPay.compareTo(elementVariableConge.getComp_id().getDdeb()) <= 0 && dtLastDayOfMonthPay.compareTo(elementVariableConge.getDfin()) >= 0))
					{
						String rubjnp = salary.getParameter().getJourNonPayeRubrique();
						// IF NOT PA_PAIE.NouZ(w_rubjnp) THEN
						if (rubjnp != null && rubjnp.trim() != "")
							// Pseudo_Ev(w_rubjnp, wevcg.nbja);
							salary.generatePseudoEv(rubjnp, rubjnp, "PSEUDO-EV", "", "", elementVariableConge.getNbja().doubleValue());
						// END IF;
						//
						// -- on ne prend en compte que les conges dont mnt4 = 0
						// -- Ils seront quand meme stockes dans l'histo des conges/Abs.
						// IF wfnom.mnt4 != 1 THEN
						if (codeAbsence[3] != 1)
						{

							// IF wfnom.mnt3 = 0 THEN -- Conges annuels
							if (codeAbsence[2] == 0)
							{
								// wnbjc := wnbjc + wevcg.nbjc;
								nbreJourConges += elementVariableConge.getNbjc().doubleValue();
								// wnbjsmm := wnbjsmm + wevcg.nbja;
								nbreJourCongesSalaireMoyMois += elementVariableConge.getNbja().doubleValue();
								// wnbjca := wnbjca + wevcg.nbja;
								nbreJoursAbsencePourCongeAnnuel += elementVariableConge.getNbja().doubleValue();
								// IF wevcg.ddeb = w_ddpa THEN
								if (dtFirstDayOfMonthPay.equals(elementVariableConge.getComp_id().getDdeb()))
									// debmois := TRUE;
									this.setDebutDeMois(true);
								// END IF;

								if(StringUtils.equals("SOBRAGA", salary.parameter.nomClient))
									this.setDebutDeMois(false);
							}
							// ELSIF wfnom.mnt3 = 1 THEN -- Conges ponctuels
							else if (codeAbsence[2] == 1)
							{
								// wnbjcp := wnbjcp + wevcg.nbjc;
								nbreJourCongesPonctuels += elementVariableConge.getNbjc().doubleValue();
								// wnbjcpa := wnbjcpa + wevcg.nbja;
								nbreJourAbsencePourCongesPonctuels += elementVariableConge.getNbja().doubleValue();
								// IF NOT PA_PAIE.NouZ(wevcg.mont) THEN
								if (elementVariableConge.getMont() != null)
									// wmntcp := wevcg.mont;
									montantCongePonctuel = elementVariableConge.getMont().doubleValue();
								// END IF;
							}
							// END IF;
						}
						// END IF;
						//
						// IF wfnom.mnt3 = 0 OR wfnom.mnt3 = 1 THEN
						if (codeAbsence[2] == 0 || codeAbsence[2] == 1)
						{
							String rubabs = salary.getParameter().getAbsenceMoisRubrique();
							// IF NOT PA_PAIE.NouZ(w_rubabs) THEN
							if (rubabs != null && rubabs.trim() != "")
								// Pseudo_Ev(w_rubabs, wevcg.nbja);
								salary.generatePseudoEv(rubabs, rubabs, "PSEUDO-EV", "", "", elementVariableConge.getNbja().doubleValue());
							// END IF;
						}
						// END IF;
						//
						// END IF; -- FIN 'IF wevcg.ddeb >= w_ddpa AND
						// -- wevcg.dfin <= w_dfpa'
					}
					//
					// -- Nombre de jours pour conges posterieurs
					// IF wevcg.ddeb > w_dfpa THEN
					if (dtLastDayOfMonthPay.compareTo(elementVariableConge.getComp_id().getDdeb()) < 0)
					{
						// IF wfnom.mnt3 = 0 THEN -- Conges annuels
						if (codeAbsence[2] == 0)
						{
							// wnbjc_ms := wnbjc_ms + wevcg.nbjc;
							nbreJourCongesAnnuelMoisSuiv += elementVariableConge.getNbjc().doubleValue();
							// wnbjsmm_ms := wnbjsmm_ms + wevcg.nbja;
							nbreJourCongesSalaireMoyMoisSuiv += elementVariableConge.getNbja().doubleValue();
						}
						// END IF;
					}
					// END IF;
					//
					// -- Nombre de jours pour conges anterieurs
					// IF wevcg.ddeb < w_ddpa THEN
					if (dtFirstDayOfMonthPay.compareTo(elementVariableConge.getComp_id().getDdeb()) > 0)
					{
						// IF wfnom.mnt3 = 0 THEN -- Conges annuels
						if (codeAbsence[2] == 0)
						{
							// wnbjc_ma := wnbjc_ma + wevcg.nbjc;
							nbreJourCongesAnnuelMoisAnte += elementVariableConge.getNbjc().doubleValue();
							// wnbja_ma := wnbja_ma + wevcg.nbja;
							nbreJourAbsenceCongesAnnuelMoisAnte += elementVariableConge.getNbja().doubleValue();
						}
						// END IF;
					}
					// END IF;
					//
					// END IF; -- FIN 'IF wfnom.mnt1 = 0'
				}
				//cas ou on veut quand méme pour le motif, générer les jours d'absences/congés dans les montants 10 et 11, on met en lib4 :O, en Mnt10 la rub des abs, en Mnt11 la rub des cgs
				//en ne prenant que les lignes incluses dans le mois en cours
				boolean abs = codeAbsence[0] == 0;
				boolean test2 = codeAbsence[10] != 0 ;
				if (abs)
				{
					test2 = test2 &&((elementVariableConge.getDfin() == null && dtLastDayOfMonthPay != null) || elementVariableConge.getDfin().getTime() <= dtLastDayOfMonthPay.getTime());
					if (StringUtils.equals(TypeBDUtil.typeBD, TypeBDUtil.MS))
						test2 = test2 && ((elementVariableConge.getDfin() == null && dtLastDayOfMonthPay != null) || elementVariableConge.getDfin().compareTo(dtLastDayOfMonthPay) <= 0);
				}
				if(!abs)
				{
					test2 = test2 && (dtFirstDayOfMonthPay.compareTo(elementVariableConge.getComp_id().getDdeb()) <= 0 && dtLastDayOfMonthPay.compareTo(elementVariableConge.getDfin()) >= 0);
				}
				if (test2)
				{
					if (codeAbsence[11] > 0)
					{
						String rubrique = ClsStringUtil.formatNumber(codeAbsence[11], ParameterUtil.formatRubrique);
						salary.generatePseudoEv(rubrique, rubrique, "PSEUDO-EV", "", "", elementVariableConge.getNbja().doubleValue());
					}
					if (codeAbsence[12] > 0)
					{
						String rubrique = ClsStringUtil.formatNumber(codeAbsence[12], ParameterUtil.formatRubrique);
						salary.generatePseudoEv(rubrique, rubrique, "PSEUDO-EV", "", "", elementVariableConge.getNbjc().doubleValue());
					}
				}
				//
				// END LOOP; -- FIN LOOP sur 'curs_evcg INTO wevcg.*
			}
			// spécifique cnss
			if (StringUtils.equals(salary.parameter.nomClient, "CNSS"))
			{
//				 -- TFN 12/2006
//				   -- Prise en compte des congés antérieurs, on défalque le congé pris
//				   -- le mois precedent sur le mois courant.
//				   IF conge_anterieur=1 THEN
//				     wnbjc := wnbjc + wnbjc_ma;
//				     wnbja := wnbja + wnbja_ma;
//				--              wnbjsmm := wnbjsmm + wnbja_ma;
//				--              wnbjc   := wnbjc + wevcg.nbjc;
//				--              wnbjsmm := wnbjsmm + wevcg.nbja;
//				--              wnbjca  := wnbjca + wevcg.nbja;
//				   END IF;
				if(salary.parameter.priseEnCompterCongeAnterieur)
				{
					nbreJourConges += nbreJourCongesAnnuelMoisAnte;
					nbreJourAbsence += nbreJourAbsenceCongesAnnuelMoisAnte;					
				}
				
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * LECTURE DES CODES DE NOMENCLATURE POUR LES MOTIFS DE CG OU ABS. => PA_CALC_ANX.lec_codeAbs()
	 * 
	 * @param salary
	 *            le salarié dont on calcule le nombre de périodes
	 * @param motif
	 *            la clé d'accés é la TA22
	 */
	private void readAbsenceCode(ClsSalaryToProcess salary, String motif)
	{
		ParameterUtil.println(">>readAbsenceCode");
		final Integer nbr = 13;
		codeAbsence = new int[nbr];
		for (int i = 0; i < nbr; i++)
		{
			codeAbsence[i] = 0;
		}
		// String queryRetro = "select max(valm) from Rhthfnom " +
		// "where cdos = '" + salary.getParameter().getDossier() + "'" +
		// " and ctab = " + 22 +
		// " and cacc = '" + motif + "'" +
		// " and aamm = '" + salary.getMonthOfPay() + "'" +
		// " and nbul = " + salary.getNbul() +
		// " and nume ="
		// ;
		// String query = "select max(valm) from Rhfnom " +
		// "where cdos = '" + salary.getParameter().getDossier() + "'" +
		// " and ctab = " + 22 +
		// " and cacc = '" + motif + "'" +
		// " and nume ="
		// ;
		// List l = null;
		// for(int i = 0; i<8; i++){
		// l = salary.getParameter().isUseRetroactif()? salary.getService().find(queryRetro + (i+1)) : salary.getService().find(query + (i+1));
		// if(l != null && l.size() > 0 && l.get(0) != null){
		// codeAbsence[i] = ((Long)l.get(0)).intValue();
		// }
		// }
		
		String complexQuery = "select max(case when nume = 1 then valm else 0 end)";
		complexQuery += ", max(case when nume = 2 then valm else 0 end)";
		complexQuery += ", max(case when nume = 3 then valm else 0 end)";
		complexQuery += ", max(case when nume = 4 then valm else 0 end)";
		complexQuery += ", max(case when nume = 5 then valm else 0 end)";
		complexQuery += ", max(case when nume = 6 then valm else 0 end)";
		complexQuery += ", max(case when nume = 7 then valm else 0 end)";
		complexQuery += ", max(case when nume = 8 then valm else 0 end)";
		complexQuery += ", max(case when nume = 1 then valt else 0 end)";
		complexQuery += ", max(case when nume = 2 then valt else 0 end)";
		complexQuery += ", max(case when nume = 4 then (case vall when 'O' then 1 else 0 end) else 0 end)";
		complexQuery += ", max(case when nume = 10 then valm else 0 end)";
		complexQuery += ", max(case when nume = 11 then valm else 0 end)";
		complexQuery += " from table" + " where cdos = '" + salary.getParameter().getDossier() + "'" + " and ctab = " + 22 + " and cacc = '" + motif + "'" + " and nume in (1, 2, 3, 4, 5, 5, 6, 7, 8,10,11)";
		complexQuery = salary.getParameter().isUseRetroactif() ? complexQuery.replace("table", "Rhthfnom") : complexQuery.replace("table", "Rhfnom");
		ParameterUtil.println("...complexQuery : " + complexQuery);
		List listOfMaxsum = salary.getService().find(complexQuery);
		if (listOfMaxsum != null && listOfMaxsum.size() > 0)
		{
			Object[] val = (Object[]) listOfMaxsum.get(0);
			for (int i = 0; i < nbr; i++)
			{
				if (val[i] != null)
					codeAbsence[i] = Double.valueOf(val[i].toString()).intValue();
			}
		}

		// int j=0;
		// for(int i = 0; i<8; i++){
		// j+=1;
		// ParameterUtil.println("Valm "+j+"="+ codeAbsence[i]);
		// }
	}

	/**
	 * Recherche du nombre d'heures et du nombre de jours travailles pour proratas
	 * 
	 * @param salary
	 *            le salarié dont on calcule le nombre de périodes
	 */
	public void prorataNbreHeuresJoursTravailles(ClsSalaryToProcess salary)
	{
		ParameterUtil.println(">>prorataNbreHeuresJoursTravailles");
		// ------------------------------------------------------------------------------
		// -- Recherche du nombre d'heures et du nombre de jours travailles pour proratas
		// ------------------------------------------------------------------------------
		//
		// -- recup nombre d'heures de la section
		// IF PA_PAIE.NouZ(wnbh) THEN
		// wnbh := paf_lecfnomT(3,wsal01.niv3,1,w_aamm,w_nlot,wsd_fcal1.nbul);
		double nombreHeure = salary.parameter.nombreHeure;
		tempNumber = salary.getUtilNomenclature().getAmountOrRateFromNomenclatureRates(salary.parameter.listOfTableXXMap,salary.getInfoSalary().getComp_id().getCdos(), 3, salary.getInfoSalary().getNiv3(), 1, salary.getNlot(), salary.getNbul(),
				salary.getMoisPaieCourant(), salary.getMonthOfPay(), ClsEnumeration.EnColumnToRead.RATES);
		if (tempNumber != null)
			nombreHeure = tempNumber.doubleValue();
		this.setProrataNbreHeures(nombreHeure);
		salary.parameter.setNombreHeure(nombreHeure);
		// END IF;
		//
		// IF wsal01.cods = 'HC' OR wsal01.cods = 'HP' THEN
		if ("HC".equals(salary.getInfoSalary().getCods()) || "HP".equals(salary.getInfoSalary().getCods()))
		{
			// wnbjt := paf_lecEvar(wsal01.nmat,w_aamm,wsd_fcal1.nbul,rub_nbj);
			double i = 0;
			tempNumber = salary.getUtilNomenclature().getSumOfAmountOfVariableElement(salary.getInfoSalary().getComp_id().getCdos(), salary.getInfoSalary().getComp_id().getNmat(), salary.getMoisPaieCourant(),
					salary.getPeriodOfPay(), salary.getParameter().getHoraireNumberOfDayRubrique(), salary.getNbul());
			if (tempNumber != null)
				i = tempNumber.doubleValue();
			this.setProrataNbreJourTravaillees(i);

			// IF PA_PAIE.NouZ(wnbjt) THEN
			//pour SOBRAGA
			if(StringUtil.notEquals(salary.parameter.nomClient, "SOBRAGA"))
			{
				if (i == 0)
				{
					// IF NOT PA_PAIE.NouZ(w_bnbj) THEN -- nb jours travailles
					if (salary.getParameter().getBase30NombreJour() > 0)
					{
						i = salary.getParameter().getBase30NombreJour();
						
					}
					// ELSE
					else
					{
						i = 30;
					}
					ParameterUtil.println("-------------------------->Prorata Nbre Jr Travail = " + this.getProrataNbreHeureTravail());
					// END IF;
					this.setProrataNbreJourTravaillees(i);
					
				}
			}
			// END IF;
			//
			// wnbht := paf_lecEvar(wsal01.nmat,w_aamm,wsd_fcal1.nbul,rub_hor);
			i = 0;
			tempNumber = salary.getUtilNomenclature().getSumOfAmountOfVariableElement(salary.getInfoSalary().getComp_id().getCdos(), salary.getInfoSalary().getComp_id().getNmat(), salary.getMoisPaieCourant(),
					salary.getPeriodOfPay(), salary.getParameter().getHoraireRubrique(), salary.getNbul());
			if (tempNumber != null)
				i = tempNumber.doubleValue();
			
			tempNumber = salary.getUtilNomenclature().getSumOfAmountOfVariableElement(salary.getInfoSalary().getComp_id().getCdos(), salary.getInfoSalary().getComp_id().getNmat(), salary.getMoisPaieCourant(),
					salary.getPeriodOfPay(), salary.getParameter().getHoraireNuitRubrique(), salary.getNbul());
			if (tempNumber != null)
				i += tempNumber.doubleValue();			
			
			this.setProrataNbreHeureTravail(i);
			// IF PA_PAIE.NouZ(wnbht) THEN
			if (i == 0)
				// wnbht := 0;
				this.setProrataNbreHeureTravail(0);
			// END IF;
			//
			// -- Si wnbht = 0 alors passage au salarie suivant
			// IF wnbht = 0 AND w_fictif = 'N' THEN
			// ----------------
			// COMMIT;
			// ----------------
			// RETURN TRUE;
			// END IF;
		}
		// END I
	}

	public double getNbrJoursAcquis()
	{
		return nbrJoursAcquis;
	}

	public void setNbrJoursAcquis(double nbrJoursAcquis)
	{
		this.nbrJoursAcquis = nbrJoursAcquis;
	}

	public double getMontantAcquis()
	{
		return montantAcquis;
	}

	public void setMontantAcquis(double montantAcquis)
	{
		this.montantAcquis = montantAcquis;
	}

	public double getNbrJoursAbsencesDonnantConge()
	{
		return nbrJoursAbsencesDonnantConge;
	}

	public void setNbrJoursAbsencesDonnantConge(double nbrJoursAbsencesDonnantConge)
	{
		this.nbrJoursAbsencesDonnantConge = nbrJoursAbsencesDonnantConge;
	}
}
