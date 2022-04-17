package com.kinart.paie.business.services.calcul;

import java.util.Date;
import java.util.List;

import com.kinart.paie.business.services.utils.ClsDate;
import com.kinart.paie.business.services.utils.ClsStringUtil;
import com.kinart.paie.business.services.utils.ParameterUtil;

/**
 * Cette classe permet de traiter les paramètres de temps d'un agent pour le calcul de son salaire. Ces paramètres sont par exemple, le nombre d'heures
 * travaillèes, le nombre de jours de congès annuels, etc. Chaque instance de salariè contient qu'une seule instance de cette classe qui permet de contenir ses
 * paramètres de temps.
 * 
 * @author c.mbassi
 * 
 */
public class ClsFictifSalaryWorkTime
{
	// nombre heures trvaillèes
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

	// nombre de pèriodes de règulation
	private double nbrePeriodRegu = 0;

	// elements variables congès existe
	private boolean elementVariableCGExist = false;

	// nombre heures travaillèes pour prorata
	// wnbht
	private double prorataNbreHeureTravail = 0;

	// nombre de jours travaillès pour prorata
	// wnbjt
	private double prorataNbreJourTravaillees = 0;

	// nombre d'heures travaillès pour prorata
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

	private Date date_debut_conge = null;

	private double nbj_cabf = 0;
	
	private double nbreJourPresence = 0;

	public Date getDate_debut_conge()
	{
		return date_debut_conge;
	}

	public void setDate_debut_conge(Date date_debut_conge)
	{
		this.date_debut_conge = date_debut_conge;
	}

	public double getNbj_cabf()
	{
		return nbj_cabf;
	}

	public void setNbj_cabf(double nbj_cabf)
	{
		this.nbj_cabf = nbj_cabf;
	}

	public double getNbreJourPresence()
	{
		return nbreJourPresence;
	}

	public void setNbreJourPresence(double nbreJourPresence)
	{
		this.nbreJourPresence = nbreJourPresence;
	}

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
	 * Calcule le nombre de pèriodes de règulation d'un salariè. Cette fonction met è jour nbrePeriodRegu.
	 * 
	 * @param salary
	 *            le salariè dont on calcule le nombre de pèriodes
	 */
	public void calculNbrePeriodeRegulation(ClsFictifSalaryToProcess salary)
	{
		// nbper_regu := 0;
		int i = 0;
		this.nbrePeriodRegu = 0;
		String rubrique = salary.param.getRegularisationRubrique();
		if (rubrique != null && rubrique.trim() != "" && rubrique.trim() != ParameterUtil.formatRubrique)
		{
			
			// nbper_regu := paf_LecEvar(wsal01.nmat,w_aamm,wsd_fcal1.nbul,rub_regper);
			tempNumber = salary.utilNomenclatureFictif.getSumOfAmountOfVariableElement(salary.param.getDossier(), salary.getInfoSalary().getComp_id().getNmat(), salary.param.getMonthOfPay(),
					rubrique, salary.param.getNumeroBulletin());
			if (tempNumber != null)
				i = tempNumber.intValue();
//			if (i < 0)
//				i = 0;
		}
		this.nbrePeriodRegu = i;
	}

	/**
	 * Calcule le nombre de mois supplèmentaires. Cette fonction met è jour nbreMoisSuppl.
	 * 
	 * @param salary
	 *            le salariè dont on calcule le nombre de pèriodes
	 */
	public void calculateNbreMoisSuppl(ClsFictifSalaryToProcess salary)
	{
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
		String queryStringRetro = "select ddpa, dfpa from Rhthev" + " where comp_id.cdos = '" + salary.param.getDossier() + "'" + " and comp_id.nmat = '"
				+ salary.getInfoSalary().getComp_id().getNmat() + "'" + " and comp_id.aamm = '" + salary.param.getMonthOfPay() + "'" + " and comp_id.nbul = " + salary.param.getNumeroBulletin();
		String queryString = "select ddpa, dfpa from Rhteltvarent" + " where comp_id.cdos = '" + salary.param.getDossier() + "'" + " and comp_id.nmat = '"
				+ salary.getInfoSalary().getComp_id().getNmat() + "'" + " and comp_id.aamm = '" + salary.param.getMonthOfPay() + "'" + " and comp_id.nbul = " + salary.param.getNumeroBulletin();
		//
		List listOfEv = (salary.param.isUseRetroactif()) ? salary.param.getService().find(queryStringRetro) : salary.param.getService().find(queryString);
		Object[] rowEv = null;
		if (listOfEv != null && listOfEv.size() > 0)
		{
			rowEv = (Object[]) listOfEv.get(0);
			// IF NOT tab91 THEN
			if (!salary.param.isTable91LabelNotEmpty())
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
						dernierJourMois = new ClsDate(salary.param.getDossierDateDebutExercice(), salary.param.appDateFormat).getMonth();
					// END IF;
					// nbm_sup := mois_fin - mois_deb;
					this.setNbreMoisSuppl(dernierJourMois - premierJourMois);
				}
				// END IF;
			}
			// END IF;
		}
		// this.setNbreMoisSuppl(0);
		ParameterUtil.println("............... setNbreMoisSuppl:" + this.getNbreMoisSuppl());
	}

	/**
	 * Calcule le nombre de jours payes non pris. Cette fonction met è jour nbreJourPayeNonPrisRubrique.
	 * 
	 * @param salary
	 *            le salariè dont on calcule le nombre de pèriodes
	 */
	public void calculNbreJourPaidNonPris(ClsFictifSalaryToProcess salary)
	{
		double i = 0;
		tempNumber = salary.utilNomenclatureFictif.getSumOfAmountOfVariableElement(salary.param.getDossier(), salary.getInfoSalary().getComp_id().getNmat(), salary.param.getMonthOfPay(), salary.param
				.getJourPayeNonPrisRubrique(), salary.param.getNumeroBulletin());
		if (tempNumber != null)
			i = tempNumber.doubleValue();
		this.setNbreJourCongesAnnuelPayeNonPris(i);

		if (i == 0)
		{
			this.setNbreJourCongesAnnuelPayeNonPris(0);
			this.setNbreJourCongesNonPris(0);
			this.setNbreJourPayeSupplPayeNonPris(i);
		}
		else
		{
			this.setNbreJourCongesNonPris(i);
			this.setNbreJourPayeSupplPayeNonPris(i);
		}
	}

	/**
	 * Calcule le nombre de jours supplementaires. Cette fonction met è jour nbreJourSuppl.
	 * 
	 * @param salary
	 *            le salariè dont on calcule le nombre de pèriodes
	 */
	public void calculateNbreJourSuppl(ClsFictifSalaryToProcess salary)
	{
		ParameterUtil.println(">>calculateNbreJourSuppl");
		// wnbjs := paf_LecEvar(wsal01.nmat,w_aamm,wsd_fcal1.nbul,rub_nbjs);
		double i = 0;

		tempNumber = salary.utilNomenclatureFictif.getSumOfAmountOfVariableElement(salary.param.getDossier(), salary.getInfoSalary().getComp_id().getNmat(), salary.param.getMonthOfPay(), salary.param
				.getJourSupplRubrique(), salary.param.getNumeroBulletin());
		if (tempNumber != null)
			i = tempNumber.doubleValue();

		this.setNbreJoursSupplementaires(i);
	}
	
	/**
	 * Etant donnèe que pour les congès, toutes les lignes sont saisies sur la mème pèriode, il n'est donc pas nècessaire
	 * de faire des requètes è chaque boucle. è la première requete, on peut stocker la liste des conges du salaries
	 */

	/**
	 * calcule le nombre de jours de congè d'un salariè è partir des èlèments variables congè historisès ou non. Cette fonction met è jour
	 * nbreJourPayeNonPrisRubrique.
	 * 
	 * @param salary
	 *            le salariè dont on calcule le nombre de pèriodes
	 * @throws Exception
	 */
	public void calculateNbreJourCongeEtAbsence(ClsFictifSalaryToProcess salary) throws Exception
	{

		String nmat = salary.getInfoSalary().getComp_id().getNmat();
		String queryString = "from Rhteltvarconge" + " where cdos='" + salary.param.getDossier() + "'" + " and nmat ='" + nmat + "'" + " and aamm ='" + salary.param.getFictiveMonthOfPay() + "'"
				+ " and nbul =" + salary.param.getNumeroBulletin();

		Date dtLastDayOfMonthPay = salary.param.getLastDayOfMonth();
		Date dtFirstDayOfMonthPay = salary.param.getFirstDayOfMonth();
		//
		List listOfElementVariableConge = salary.listeConges;
		if(listOfElementVariableConge == null)
		{
			listOfElementVariableConge =salary.param.getService().find(queryString);
			salary.listeConges = listOfElementVariableConge;
		}
		//
		ClsElementVariableCongeClone elementVariableConge = null;
		try
		{
			for (Object obj : listOfElementVariableConge)
			{
				elementVariableConge = ClsElementVariableCongeClone.clone(obj);
				elementVariableCGExist = true;

				this.readAbsenceCode(salary, elementVariableConge.getMotf());

				if (codeAbsence[0] == 0)
				{
					if ((elementVariableConge.getDfin().getTime() < salary.getInfoSalary().getDdcf().getTime()) && (elementVariableConge.getDfin().getTime() < dtLastDayOfMonthPay.getTime()) && (elementVariableConge.getComp_id().getDdeb().getTime() >= dtFirstDayOfMonthPay.getTime()))
					{
						if (codeAbsence[3] != 1)
							nbreJourAbsence += elementVariableConge.getNbja().doubleValue();
						if (codeAbsence[7] > 0)
						{
							String rubrique = ClsStringUtil.formatNumber(codeAbsence[7], ParameterUtil.formatRubrique);
							salary.generatePseudoEv(rubrique, rubrique, "PSEUDO-EV", "", "", elementVariableConge.getNbja().doubleValue());
						}
						else
						{
							if (salary.param.getJourNonPayeRubrique() != null && salary.param.getJourNonPayeRubrique().trim() != "" && !ParameterUtil.formatRubrique.equalsIgnoreCase(salary.param.getJourNonPayeRubrique()))
								salary.generatePseudoEv(salary.param.getJourNonPayeRubrique(), salary.param.getJourNonPayeRubrique(), "PSEUDO-EV", "", "", elementVariableConge.getNbja().doubleValue());
						}
						String rubabs = salary.param.getAbsenceMoisRubrique();
						if (rubabs != null && rubabs.trim() != ""  && !ParameterUtil.formatRubrique.equalsIgnoreCase(salary.param.getAbsenceMoisRubrique()))
							salary.generatePseudoEv(rubabs, rubabs, "PSEUDO-EV", "", "", elementVariableConge.getNbja().doubleValue());
					}
				}
				else
				{
					if (dtFirstDayOfMonthPay.compareTo(elementVariableConge.getComp_id().getDdeb()) <= 0 && dtLastDayOfMonthPay.compareTo(elementVariableConge.getDfin()) >= 0)
					{

						double nbja = elementVariableConge.getNbja().doubleValue();
						String rubjnp = salary.param.getJourNonPayeRubrique();
						String rubabs = salary.param.getAbsenceMoisRubrique();
						if (rubabs != null && rubabs.trim() != "" && !ParameterUtil.formatRubrique.equalsIgnoreCase(rubabs))
						{
							if ("O".equals(salary.param.getBase30Rubrique()))
							{
								if (salary.param.getBase30NombreJour() > 0)
									nbja = salary.param.getBase30NombreJour();
								else
									nbja = 30;
							}
							else
								nbja = salary.param.myMonthOfPay.getMaxDayOfMonth();

							salary.generatePseudoEv(rubabs, rubabs, "PSEUDO-EV", "", "", nbja);
						}

						if (rubjnp != null && rubjnp.trim() != "" && !ParameterUtil.formatRubrique.equalsIgnoreCase(rubjnp))
							salary.generatePseudoEv(rubjnp, rubjnp, "PSEUDO-EV", "", "", elementVariableConge.getNbja().doubleValue());

						if (codeAbsence[3] != 1)
						{
							if (codeAbsence[2] == 0)
							{
								if (date_debut_conge == null)
									date_debut_conge = elementVariableConge.getComp_id().getDdeb();

								nbreJourConges += elementVariableConge.getNbjc().doubleValue();
								nbreJourCongesSalaireMoyMois += elementVariableConge.getNbja().doubleValue();
								nbreJoursAbsencePourCongeAnnuel += elementVariableConge.getNbja().doubleValue();
								
								if (elementVariableConge.getDfin().equals(salary.getInfoSalary().getDfcf()))
								{
									nbj_cabf = elementVariableConge.getNbja().doubleValue();
									salary.dern_mois_de_conge = true;
								}

								if ((elementVariableConge.getDfin().equals(salary.infoSalary.getDfcf())) && (elementVariableConge.getDfin().compareTo(salary.param.lastDayOfMonth) != 0))
								{
									nbja = 0;
									if ("O".equals(salary.param.getBase30Rubrique()))
									{
										if (salary.param.getBase30NombreJour() > 0)
											nbja = salary.param.getBase30NombreJour();
										else
											nbja = 30;
									}
									else
										nbja = salary.param.myMonthOfPay.getMaxDayOfMonth();
									double nbj_abs_sup = 0;
									if (elementVariableConge.getComp_id().getDdeb().compareTo(salary.param.firstDayOfMonth) != 0)
									{
										double nbj_trav_dern_mois_cng = new ClsDate(elementVariableConge.getComp_id().getDdeb()).getDay() - 1;
										nbj_abs_sup = nbja - this.nbreJoursAbsencePourCongeAnnuel - nbj_trav_dern_mois_cng;
									}
									else
										nbj_abs_sup = nbja - this.nbreJoursAbsencePourCongeAnnuel;

									if (nbj_abs_sup > 0)
										this.nbreJourAbsence += nbj_abs_sup;
								}
							}
							else if (codeAbsence[2] == 1)
							{
								this.nbreJourCongesPonctuels += elementVariableConge.getNbjc().doubleValue();
								this.nbreJourAbsencePourCongesPonctuels += elementVariableConge.getNbja().doubleValue();
								if (elementVariableConge.getMont() != null)
									this.montantCongePonctuel = elementVariableConge.getMont().doubleValue();
							}
						}

						if (codeAbsence[2] == 0 || codeAbsence[2] == 1)
						{
							rubabs = salary.param.getAbsenceMoisRubrique();
							if (rubabs != null && rubabs.trim() != "" && !ParameterUtil.formatRubrique.equalsIgnoreCase(rubabs))
								salary.generatePseudoEv(rubabs, rubabs, "PSEUDO-EV", "", "", elementVariableConge.getNbja().doubleValue());
						}
					}

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
	 *            le salariè dont on calcule le nombre de pèriodes
	 * @param motif
	 *            la clè d'accès è la TA22
	 */
	private void readAbsenceCode(ClsFictifSalaryToProcess salary, String motif)
	{
		codeAbsence = new int[8];
		for (int i = 0; i < 8; i++)
		{
			codeAbsence[i] = 0;
		}
		String complexQuery = "select max(case when nume = 1 then valm else 0 end)";
		complexQuery += ", max(case when nume = 2 then valm else 0 end)";
		complexQuery += ", max(case when nume = 3 then valm else 0 end)";
		complexQuery += ", max(case when nume = 4 then valm else 0 end)";
		complexQuery += ", max(case when nume = 5 then valm else 0 end)";
		complexQuery += ", max(case when nume = 6 then valm else 0 end)";
		complexQuery += ", max(case when nume = 7 then valm else 0 end)";
		complexQuery += ", max(case when nume = 8 then valm else 0 end)";
		complexQuery += " from table" + " where cdos = '" + salary.param.getDossier() + "'" + " and ctab = " + 22 + " and cacc = '" + motif + "'" + " and nume in (1, 2, 3, 4, 5, 5, 6, 7, 8)";
		complexQuery = complexQuery.replace("table", "Rhfnom");
		List listOfMaxsum = salary.param.getService().find(complexQuery);
		if (listOfMaxsum != null && listOfMaxsum.size() > 0)
		{
			Object[] val = (Object[]) listOfMaxsum.get(0);
			for (int i = 0; i < 8; i++)
			{
				if (val[i] != null)
					codeAbsence[i] = Double.valueOf(val[i].toString()).intValue();
			}
		}
	}

	/**
	 * Recherche du nombre d'heures et du nombre de jours travailles pour proratas
	 * 
	 * @param salary
	 *            le salariè dont on calcule le nombre de pèriodes
	 */
	public void prorataNbreHeuresJoursTravailles(ClsFictifSalaryToProcess salary)
	{
		if(salary.param.getNombreHeure() == 0)
		{
			double nombreHeure = salary.param.getNombreHeure();
			tempNumber = salary.utilNomenclatureFictif.getAmountOrRateFromNomenclatureRates(salary.getInfoSalary().getComp_id().getCdos(), 3, salary.getInfoSalary().getNiv3(), 1, salary.param.getNlot(),
					salary.param.getNumeroBulletin(), salary.param.getMonthOfPay(), ClsEnumeration.EnColumnToRead.RATES);
			if (tempNumber != null)
				nombreHeure = tempNumber.doubleValue();
			this.setProrataNbreHeures(nombreHeure);
			salary.param.setNombreHeure(nombreHeure);
		}
		
		if ("HC".equals(salary.getInfoSalary().getCods()) || "HP".equals(salary.getInfoSalary().getCods()))
		{
			double i = 0;
			tempNumber = salary.utilNomenclatureFictif.getSumOfAmountOfVariableElement(salary.getInfoSalary().getComp_id().getCdos(), salary.getInfoSalary().getComp_id().getNmat(), salary.param
					.getMonthOfPay(), salary.param.getHoraireNumberOfDayRubrique(), salary.param.getNumeroBulletin());
			if (tempNumber != null)
				i = tempNumber.doubleValue();
			this.setProrataNbreJourTravaillees(i);

			if (i == 0)
			{
				if (salary.param.getBase30NombreJour() > 0)
				{
					i = salary.param.getBase30NombreJour();
					this.setProrataNbreJourTravaillees(i);
				}
				else
				{
					this.setProrataNbreJourTravaillees(30);
				}
			}

			i = 0;
			tempNumber = salary.utilNomenclatureFictif.getSumOfAmountOfVariableElement(salary.getInfoSalary().getComp_id().getCdos(), salary.getInfoSalary().getComp_id().getNmat(), salary.param
					.getMonthOfPay(), salary.param.getHoraireRubrique(), salary.param.getNumeroBulletin());
			if (tempNumber != null)
				i = tempNumber.doubleValue();
			this.setProrataNbreHeureTravail(i);
		}
	}
	
	public void resetValues()
	{
		// wnbht := 0; -- nb heures travaillees
		prorataNbreHeureTravail = 0;
		// wnbjc := 0; -- nb jours conges annuels
		nbreJourConges = 0;
		// wnbjcpnp := 0; -- nb jours conges payes non pris
		nbreJourCongesNonPris = 0;
		// wnbjcp := 0; -- nb jours conges ponctuels (ouvrables)
		nbreJourCongesPonctuels = 0;
		// wnbjcpa := 0; -- nb jours conges ponctuels (calendaires)
		nbreJourAbsencePourCongesPonctuels = 0;
		// wmntcp := 0; -- montant des conges payes
		montantCongePonctuel = 0;
		// nbm_sup := 0; -- nombre de mois suppl. (extrapolation de calcul)
		nbreMoisSuppl = 0;
		// wnbja := 0; -- nb jours absences
		nbreJourAbsence = 0;
		// wnbjca := 0;
		nbreJoursAbsencePourCongeAnnuel = 0;
		// wnbjsmm := 0;
		nbreJourCongesSalaireMoyMois = 0;
		// wnbjsmmpnp := 0;
		nbreJourPayeSupplPayeNonPris = 0;
		// wnbjcapnp := 0;
		nbreJourCongesAnnuelPayeNonPris = 0;
		// wnbjs := 0;
		nbreJoursSupplementaires = 0;
		// -- mem_ja := 0; -- Nbre de jours d'absences.
		//	      nbj_cabf := 0;
		nbj_cabf = 0;
	}
}
