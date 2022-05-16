package com.kinart.paie.business.services.calcul;

import java.io.File;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

import com.kinart.paie.business.repository.ElementSalaireRepository;
import com.kinart.paie.business.model.*;
import com.kinart.paie.business.services.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataAccessException;

/**
 * Cette classe permet d'encapsuler toutes les données propres é un agent qui participent au calcul de son salaire. Elle contient une méthode qui permet de
 * calculer toutes les rubriques. Elle a été conéue pour que ses intances soient tangibles les unes des autres. Ainsi, son espace de nommage est complétement
 * isolée.
 * 
 * @author e.etoundi
 * 
 */
public class ClsSalaryToProcess
{//
	
	Integer maxNLigEV = - 1;
	
	Session session = null;

	// Le numero de la ligne dans CalculPaie
	protected int derniereLigne = 0;

	// le champ contenant toutes les informations du salarié
	protected ClsInfoSalaryClone infoSalary = null;

	// champ contenant les fonctions d'accés aux tables de nomenclature
	protected ClsNomenclatureUtil utilNomenclature = null;

	protected GeneriqueConnexionService service = null;

	protected ElementSalaireRepository elementSalaireRepository;

	protected ClsParameterOfPay parameter = null;

	protected ClsFictifParameterOfPay fictiveParameter = null;

	protected ClsDate myMonthOfPay = null;

	protected Number tempNumber = null;

	public File outputFile = null;

	//
	// List<ClsRubriqueClone> ListOfRubriqueOfAbsences = null;
	protected List<ClsRubriqueClone> ListOfRubriqueOfMotifFinContrat = null;// --

	// STC
	// solde
	// de
	// tout
	// compte

	protected Map<String, List<Object[]>> listOfEltvar = null;

	protected Map<String, List<Object>> listOfEltfix = null;

	protected Map<String, Object[]> cumul1 = null;

	protected Map<String, Object[]> cumul2 = null;

	protected Map<String, Object[]> cumul3 = null;

	protected Map<String, Object[]> cumul4 = null;

	// FROM PARAMETER
	protected int nbul = 9;

	protected long nlot = 0;

	protected String monthOfPay = "";

	protected String periodOfPay = "";

	protected String moisPaieCourant = "";

	protected boolean useRetroactif = false;

	// Param spécifiques
	protected int anciennete = 0;

	protected Date firstDayOfMonth = null;

	protected Date lastDayOfMonth = null;
	
	protected Date premierJourDuMois = null;

	protected Date dernierJourDuMois = null;

	protected boolean expatrie = false;

	protected boolean table91LabelNotEmpty = false;

	/**
	 * c'est le numéro de bulletion renvoyé lors de la destruction des info salarié dans le fichier salariés
	 */
	protected int numeroBulletinVerified = 0;

	// la nature de l'entreprise
	protected ClsEnumeration.EnEnterprise entreprise = ClsEnumeration.EnEnterprise.UNKNOWN;

	protected int ageOfAgent = 0;

	// POUR LE CALCUL DES PERIODES
	protected ClsPeriodUtil oPeriod = null;

	// PARAMETRES CUMUL
	protected ClsParametreCumul paramCumul = null;

	// VALEURS RUBRIQUES PARTAGEES
	protected ClsValeurRubriquePartage valeurRubriquePartage = null;

	// PARAMETRES RELATIFS AU TEMPS DE TRAVAIL
	protected ClsSalaryWorkTime workTime = null;

	// Embauche
	protected boolean embauche = false;

	// Map de => concPro
	protected Map<String, String> mapOfConcPro = null;

	// salaire é écarter du net
	protected double salaireAecarterDuNet = 0;

	// salaire brut
	protected double salaireBrut = 0;

	// salaire net
	protected double salaireNet = 0;

	// salaire net é calculer
	protected double salaireNetATrouver = 0;

	// contient les paramétres du dernier EV
	protected ClsParamElementVariable evparam = null;

	// Cpt_valajus
	protected int compteurValeurAjus = 0;

	// Max_Nb_valajus
	protected int maxNbreValeurAjus = 22;

	// nombre d'itération => nbiter
	protected int nbiter = 0;

	boolean sensPositif = true;

	// continuer le traitement du salaire
	protected boolean continuer = false;

	// salaire é l'itération paire
	double salaireIterPaire = 0;

	// salaire é l'itération impaire
	double salaireIterImpaire = 0;

	//
	protected double nbreJourPourEnfant = 0;

	public String outputtext = "";

	private double tot_cgs = 0;

	public ClsFictifParameterOfPay getFictiveParameter()
	{
		return fictiveParameter;
	}

	public void setFictiveParameter(ClsFictifParameterOfPay fictiveParameter)
	{
		this.fictiveParameter = fictiveParameter;
	}

	public int getDerniereLigne()
	{
		return derniereLigne;
	}

	public void setDerniereLigne(int derniereLigne)
	{
		this.derniereLigne = derniereLigne;
	}

	public synchronized Map<String, Object> getListSpecifiqueCumul99Map31()
	{
		return listSpecifiqueCumul99Map31;
	}

	public synchronized void setListSpecifiqueCumul99Map31(Map<String, Object> listSpecifiqueCumul99Map31)
	{
		this.listSpecifiqueCumul99Map31 = listSpecifiqueCumul99Map31;
	}

	public synchronized Map<String, Object[]> getListSpecifiqueCumul99Map11()
	{
		return listSpecifiqueCumul99Map11;
	}

	public synchronized void setListSpecifiqueCumul99Map11(Map<String, Object[]> listSpecifiqueCumul99Map11)
	{
		this.listSpecifiqueCumul99Map11 = listSpecifiqueCumul99Map11;
	}

	public synchronized Map<String, Object> getListSpecifiqueCumul99Map21()
	{
		return listSpecifiqueCumul99Map21;
	}

	public synchronized void setListSpecifiqueCumul99Map21(Map<String, Object> listSpecifiqueCumul99Map21)
	{
		this.listSpecifiqueCumul99Map21 = listSpecifiqueCumul99Map21;
	}

	public Map<String, Map<String, Object[]>> getCumulMap1PourPeriode()
	{
		return cumulMap1PourPeriode;
	}

	public void setCumulMap1PourPeriode(Map<String, Map<String, Object[]>> cumulMap1PourPeriode)
	{
		this.cumulMap1PourPeriode = cumulMap1PourPeriode;
	}

	public Map<String, Map<String, Object>> getCumulMap2PourPeriode()
	{
		return cumulMap2PourPeriode;
	}

	public void setCumulMap2PourPeriode(Map<String, Map<String, Object>> cumulMap2PourPeriode)
	{
		this.cumulMap2PourPeriode = cumulMap2PourPeriode;
	}

	public Map<String, Map<String, Object>> getCumulMap3PourPeriode()
	{
		return cumulMap3PourPeriode;
	}

	public void setCumulMap3PourPeriode(Map<String, Map<String, Object>> cumulMap3PourPeriode)
	{
		this.cumulMap3PourPeriode = cumulMap3PourPeriode;
	}

	public String getOutputtext()
	{
		return outputtext;
	}
	public void setOutputtext(String outputtext)
	{
		this.outputtext = outputtext;
	}

	public void addToOutputtext(String outputtext)
	{
		if ('O' == this.parameter.getGenfile())
		{
			//System.out.println(outputtext);
//			if ("O".equals(this.getInfoSalary().getPnet()))
//			{
//			}
//			else
//			{
				this.outputtext += outputtext;
//			}
		}
	}

	public synchronized Map<String, List<Object>> getListLignePretMap()
	{
		return listLignePretMap;
	}

	public synchronized void setListLignePretMap(Map<String, List<Object>> listLignePretMap)
	{
		this.listLignePretMap = listLignePretMap;
	}

	public synchronized Map<String, List<Object>> getListNumeroPretMap()
	{
		return listNumeroPretMap;
	}

	public synchronized void setListNumeroPretMap(Map<String, List<Object>> listNumeroPretMap)
	{
		this.listNumeroPretMap = listNumeroPretMap;
	}

	public synchronized Map<String, Object[]> getCumul1()
	{
		return cumul1;
	}

	public synchronized void setCumul1(Map<String, Object[]> cumul1)
	{
		this.cumul1 = cumul1;
	}

	public synchronized Map<String, Object[]> getCumul2()
	{
		return cumul2;
	}

	public synchronized void setCumul2(Map<String, Object[]> cumul2)
	{
		this.cumul2 = cumul2;
	}

	public synchronized Map<String, Object[]> getCumul3()
	{
		return cumul3;
	}

	public synchronized void setCumul3(Map<String, Object[]> cumul3)
	{
		this.cumul3 = cumul3;
	}

	public synchronized Map<String, Object[]> getCumul4()
	{
		return cumul4;
	}

	public synchronized void setCumul4(Map<String, Object[]> cumul4)
	{
		this.cumul4 = cumul4;
	}

	public synchronized Map<String, List<Object>> getListOfEltfix()
	{
		return listOfEltfix;
	}

	public synchronized void setListOfEltfix(Map<String, List<Object>> listOfEltfix)
	{
		this.listOfEltfix = listOfEltfix;
	}

	public synchronized Map<String, List<Object[]>> getListOfEltvar()
	{
		return listOfEltvar;
	}

	public synchronized void setListOfEltvar(Map<String, List<Object[]>> listOfEltvar)
	{
		this.listOfEltvar = listOfEltvar;
	}
	
	public boolean isApplyTaux()
	{
		return applyTaux;
	}

	public void setApplyTaux(boolean applyTaux)
	{
		this.applyTaux = applyTaux;
	}

	public int getAgeOfAgent()
	{
		return ageOfAgent;
	}

	public void setAgeOfAgent(int ageOfAgent)
	{
		this.ageOfAgent = ageOfAgent;
	}

	public int getCompteurValeurAjus()
	{
		return compteurValeurAjus;
	}

	public void setCompteurValeurAjus(int compteurValeurAjus)
	{
		this.compteurValeurAjus = compteurValeurAjus;
	}

	public Map<String, String> getMapOfConcPro()
	{
		return mapOfConcPro;
	}

	public void setMapOfConcPro(Map<String, String> mapOfConcPro)
	{
		this.mapOfConcPro = mapOfConcPro;
	}

	public int getMaxNbreValeurAjus()
	{
		return maxNbreValeurAjus;
	}

	public void setMaxNbreValeurAjus(int maxNbreValeurAjus)
	{
		this.maxNbreValeurAjus = maxNbreValeurAjus;
	}

	public int getNbiter()
	{
		return nbiter;
	}

	public void setNbiter(int nbiter)
	{
		this.nbiter = nbiter;
	}

	public int getNumeroBulletinVerified()
	{
		return numeroBulletinVerified;
	}

	public void setNumeroBulletinVerified(int numeroBulletinVerified)
	{
		this.numeroBulletinVerified = numeroBulletinVerified;
	}

	public boolean isUseRetroactif()
	{
		return useRetroactif;
	}

	public void setUseRetroactif(boolean useRetroactif)
	{
		this.useRetroactif = useRetroactif;
	}

	public List<ClsRubriqueClone> getListOfRubriqueOfMotifFinContrat()
	{
		return ListOfRubriqueOfMotifFinContrat;
	}

	public void setListOfRubriqueOfMotifFinContrat(List<ClsRubriqueClone> listOfRubriqueOfMotifFinContrat)
	{
		ListOfRubriqueOfMotifFinContrat = listOfRubriqueOfMotifFinContrat;
	}

	public double getNbreJourPourEnfant()
	{
		return nbreJourPourEnfant;
	}

	public void setNbreJourPourEnfant(double nbreJourPourEnfant)
	{
		this.nbreJourPourEnfant = nbreJourPourEnfant;
	}

	public ClsParamElementVariable getEvparam()
	{
		return evparam;
	}

	public void setEvparam(ClsParamElementVariable evparam)
	{
		this.evparam = evparam;
	}

	public boolean isContinuer()
	{
		return continuer;
	}

	public void setContinuer(boolean continuer)
	{
		this.continuer = continuer;
	}

	public ClsSalaryToProcess()
	{

	}

	/**
	 * 
	 * @param parameter
	 * @param infoSalary
	 * @param service
	 */
	public ClsSalaryToProcess(ClsParameterOfPay parameter, ClsInfoSalaryClone infoSalary, GeneriqueConnexionService service)
	{
		this.parameter = parameter;
		this.infoSalary = infoSalary;
		this.service = service;
		this.elementSalaireRepository = elementSalaireRepository;
		this.utilNomenclature = new ClsNomenclatureUtil(parameter);
		this.myMonthOfPay = parameter.getMyMonthOfPay();
		this.nbul = parameter.getNumeroBulletin();
		this.useRetroactif = parameter.isUseRetroactif();
		this.oPeriod = new ClsPeriodUtil(0, 0);
		this.paramCumul = new ClsParametreCumul();
		this.valeurRubriquePartage = new ClsValeurRubriquePartage();
		this.workTime = new ClsSalaryWorkTime();
		this.mapOfConcPro = new HashMap<String, String>();
		this.evparam = new ClsParamElementVariable();
		this.setMonthOfPay(parameter.getMonthOfPay());
		this.setPeriodOfPay(parameter.getPeriodOfPay());
		this.setMoisPaieCourant(parameter.getMoisPaieCourant());

//		if (parameter.getEnCorporationSell() != ClsEnumeration.EnEnterprise.UNKNOWN)
//			this.setEntreprise(parameter.getEnCorporationSell());
//
//		if (parameter.getEnCorporationSgmb() != ClsEnumeration.EnEnterprise.UNKNOWN)
//			this.setEntreprise(parameter.getEnCorporationSgmb());

		this.setFirstDayOfMonth(parameter.getFirstDayOfMonth());
		this.setLastDayOfMonth(parameter.getLastDayOfMonth());
		
		this.setPremierJourDuMois(parameter.getFirstDayOfMonth());
		this.setDernierJourDuMois(parameter.getLastDayOfMonth());

	}

	public ClsEnumeration.EnEnterprise getEntreprise()
	{
		return entreprise;
	}

	public void setEntreprise(ClsEnumeration.EnEnterprise entreprise)
	{
		this.entreprise = entreprise;
	}

	public void setExpatrie(boolean expatrie)
	{
		this.expatrie = expatrie;
	}

	public double getSalaireIterPaire()
	{
		return salaireIterPaire;
	}

	public void setSalaireIterPaire(double salaireIterPaire)
	{
		this.salaireIterPaire = salaireIterPaire;
	}

	public double getSalaireIterImpaire()
	{
		return salaireIterImpaire;
	}

	public void setSalaireIterImpaire(double salaireIterImpaire)
	{
		this.salaireIterImpaire = salaireIterImpaire;
	}

	public boolean isExpatrie()
	{
		return expatrie;
	}

	public double getSalaireNetATrouver()
	{
		return salaireNetATrouver;
	}

	public void setSalaireNetATrouver(double salaireNetACalculer)
	{
		this.salaireNetATrouver = salaireNetACalculer;
	}

	public boolean isTable91LabelNotEmpty()
	{
		return table91LabelNotEmpty;
	}

	public void setTable91LabelNotEmpty(boolean table91LabelNotEmpty)
	{
		this.table91LabelNotEmpty = table91LabelNotEmpty;
	}

	public double getSalaireBrut()
	{
		return salaireBrut;
	}

	public void setSalaireBrut(double salaireBrut)
	{
		this.salaireBrut = salaireBrut;
	}

	public double getSalaireNet()
	{
		return salaireNet;
	}

	public void setSalaireNet(double salaireNet)
	{
		this.salaireNet = salaireNet;
	}

	public double getSalaireAecarterDuNet()
	{
		return salaireAecarterDuNet;
	}

	public void setSalaireAecarterDuNet(double salaireAecarterDuNet)
	{
		this.salaireAecarterDuNet = salaireAecarterDuNet;
	}

	public Date getFirstDayOfMonth()
	{
		return firstDayOfMonth;
	}

	public void setFirstDayOfMonth(Date firstDayOfMonth)
	{
		this.firstDayOfMonth = firstDayOfMonth;
	}

	public Date getLastDayOfMonth()
	{
		return lastDayOfMonth;
	}

	public void setLastDayOfMonth(Date lastDayOfMonth)
	{
		this.lastDayOfMonth = lastDayOfMonth;
	}

	public boolean isEmbauche()
	{
		return embauche;
	}

	public void setEmbauche(boolean embauche)
	{
		this.embauche = embauche;
	}

	//
	public ClsPeriodUtil getOPeriod()
	{
		return oPeriod;
	}

	public void setOPeriod(ClsPeriodUtil period)
	{
		oPeriod = period;
	}

	public ClsParametreCumul getParamCumul()
	{
		return paramCumul;
	}

	public void setParamCumul(ClsParametreCumul paramCumul)
	{
		this.paramCumul = paramCumul;
	}

	public ClsValeurRubriquePartage getValeurRubriquePartage()
	{
		return valeurRubriquePartage;
	}

	public void setValeurRubriquePartage(ClsValeurRubriquePartage valeurRubriquePartage)
	{
		this.valeurRubriquePartage = valeurRubriquePartage;
	}

	public ClsSalaryWorkTime getWorkTime()
	{
		return workTime;
	}

	public void setWorkTime(ClsSalaryWorkTime workTime)
	{
		this.workTime = workTime;
	}

	public String getMonthOfPay()
	{
		return monthOfPay;
	}

	public void setMonthOfPay(String monthOfPay)
	{
		this.monthOfPay = monthOfPay;
	}

	public String getPeriodOfPay()
	{
		return periodOfPay;
	}

	public void setPeriodOfPay(String periodOfPay)
	{
		this.periodOfPay = periodOfPay;
	}

	public String getMoisPaieCourant()
	{
		return moisPaieCourant;
	}

	public void setMoisPaieCourant(String moisPaieCourant)
	{
		this.moisPaieCourant = moisPaieCourant;
	}

	public GeneriqueConnexionService getService()
	{
		return service;
	}

	public void setService(GeneriqueConnexionService service)
	{
		this.service = service;
	}

	public ClsParameterOfPay getParameter()
	{
		return parameter;
	}

	public void setParameter(ClsParameterOfPay parameter)
	{
		this.parameter = parameter;
	}

	public ClsNomenclatureUtil getUtilNomenclature()
	{
		return utilNomenclature;
	}

	public void setUtilNomenclature(ClsNomenclatureUtil utilNomenclature)
	{
		this.utilNomenclature = utilNomenclature;
	}

	public ClsDate getMyMonthOfPay()
	{
		return myMonthOfPay;
	}

	public void setMyMonthOfPay(ClsDate myMonthOfPay)
	{
		this.myMonthOfPay = myMonthOfPay;
	}

	public int getAnciennete()
	{
		return anciennete;
	}

	public void setAnciennete(int anciennete)
	{
		this.anciennete = anciennete;
	}

	//
	public ClsInfoSalaryClone getInfoSalary()
	{
		return infoSalary;
	}

	public void setInfoSalary(ClsInfoSalaryClone infoSalary)
	{
		this.infoSalary = infoSalary;
	}

	public int getNbul()
	{
		return nbul;
	}

	public void setNbul(int nbul)
	{
		this.nbul = nbul;
	}

	public long getNlot()
	{
		return nlot;
	}

	public void setNlot(long nlot)
	{
		this.nlot = nlot;
	}

	/*
	 * public void chargerPeriodeDePaieSave() { // addToOutputtext("\n"+">>chargerPeriodeDePaie"); // PROCEDURE cas_normal IS // BEGIN // tab91 := FALSE; //
	 * date_dep := LAST_DAY(TO_DATE(w_aamm,'YYYYMM')); // w_ddpa := TO_DATE(w_aamm,'YYYYMM'); // w_dfpa := date_dep; // // -- LH 230298 // Premier_Jour_du_Mois :=
	 * w_ddpa; // Dernier_Jour_du_Mois := w_dfpa; // // RETURN; // END; // // BEGIN // tab91 := TRUE; // -- Recherche code salaire du salarie dans table 91 //
	 * wfnom.lib1 := // paf_lecfnomL(91,wsal01.cods,1,w_aamm,w_nlot,wsd_fcal1.nbul); String libelle1 =
	 * utilNomenclature.getLabelFromNomenclature(infoSalary.getComp_id().getCdos(), 91, periodOfPay, 1, nlot, nbul, moisPaieCourant, periodOfPay); // IF
	 * wfnom.lib1 IS NULL THEN // cas_normal; // RETURN TRUE; // END IF; if (libelle1 != null && libelle1.trim() != "") {
	 * calculatePremierEtDernierJourMoisPaie(periodOfPay); return; } // // -- Recuperation Table 91 de la periode de paie lib2 et lib3 // -- ainsi que du
	 * nombres d'heures du mois // -- Si pas trouve, calcul 1er et dernier jour de la periode. // wfnom.lib2 :=
	 * paf_lecfnomL(91,w_aamm,2,w_aamm,w_nlot,wsd_fcal1.nbul); String libelle2 = utilNomenclature.getLabelFromNomenclature(infoSalary.getComp_id().getCdos(),
	 * 91, periodOfPay, 2, nlot, nbul, moisPaieCourant, periodOfPay); // IF wfnom.lib2 IS NULL THEN // cas_normal; // RETURN TRUE; // END IF; if (libelle2 !=
	 * null && libelle2.trim() != "") { calculatePremierEtDernierJourMoisPaie(periodOfPay); return; } // // wfnom.lib3 :=
	 * paf_lecfnomL(91,w_aamm,3,w_aamm,w_nlot,wsd_fcal1.nbul); String libelle3 = utilNomenclature.getLabelFromNomenclature(infoSalary.getComp_id().getCdos(),
	 * 91, periodOfPay, 3, nlot, nbul, moisPaieCourant, periodOfPay); // IF wfnom.lib3 IS NULL THEN // cas_normal; // RETURN TRUE; // END IF; if (libelle3 !=
	 * null && libelle3.trim() != "") { calculatePremierEtDernierJourMoisPaie(periodOfPay); return; } // // wnbh :=
	 * paf_lecfnomT(91,w_aamm,1,w_aamm,w_nlot,wsd_fcal1.nbul); int nombreHeure = new
	 * Double(utilNomenclature.getAmountOrRateFromNomenclature(this.parameter.listOfTableXXMap,infoSalary.getComp_id().getCdos(), 91, periodOfPay, 1, nlot, nbul, moisPaieCourant, periodOfPay,
	 * ClsEnumeration.EnColumnToRead.RATES)).intValue(); // IF wnbh IS NULL THEN // cas_normal; // RETURN TRUE; // END IF; if (nombreHeure <= 0) {
	 * calculatePremierEtDernierJourMoisPaie(periodOfPay); table91LabelNotEmpty = false; return; } // // w_ddpa :=
	 * TO_DATE(SUBSTR(wfnom.lib2,1,10),'DD/MM/YYYY'); // firstDayOfMonth = new ClsDate(libelle2, "dd/MM/yyyy").getDate(); // w_dfpa :=
	 * TO_DATE(SUBSTR(wfnom.lib3,1,10),'DD/MM/YYYY'); // lastDayOfMonth = new ClsDate(libelle3, "dd/MM/yyyy").getDate(); // // -- LH 230298 //
	 * Premier_Jour_du_Mois := w_ddpa; // Dernier_Jour_du_Mois := w_dfpa; table91LabelNotEmpty = true; }
	 */

	/**
	 * => destruction_fcal supprime les lignes de ce salarié dans la table PACALC
	 * 
	 * @return le nombre de ligne supprimées
	 * @throws Exception
	 */
	public int deleteFromSalaryFile() throws Exception
	{
		// addToOutputtext("\n"+">>deleteFromSalaryFile");
		String error = "";
		// w_verif_bul NUMBER(1);
		//
		// BEGIN
		//
		// -- test si traitement retro
		// IF retroactif THEN
		// BEGIN
		// SELECT DISTINCT nbul INTO w_verif_bul FROM prcalc
		// WHERE identreprise = wpdos.identreprise
		// AND aamm = w_aamm
		// AND nmat = wsal01.nmat
		// AND nlot = w_nlot;
		// EXCEPTION
		// WHEN NO_DATA_FOUND THEN null;
		// END;
		String queryStringRetro = "select distinct nbul from Rhtprcalc" + " where identreprise ='" + infoSalary.getComp_id().getCdos() + "'" + " and aamm = '" + monthOfPay + "'" + " and nmat = '"
				+ infoSalary.getComp_id().getNmat() + "'" + " and nlot = " + nlot;
		// ELSE
		// BEGIN
		// SELECT DISTINCT nbul INTO w_verif_bul FROM pacalc
		// WHERE identreprise = wpdos.identreprise
		// AND aamm = w_aamm
		// AND nmat = wsal01.nmat;
		// EXCEPTION
		// WHEN NO_DATA_FOUND THEN null;
		// END;
		String queryString = "select distinct nbul from CalculPaie" + " where identreprise ='" + infoSalary.getComp_id().getCdos() + "'" + " and aamm = '" + monthOfPay + "'" + " and nmat = '" + infoSalary.getComp_id().getNmat()
				+ "'";
		// END IF;
		// IF SQL%FOUND THEN
		// IF NOT PA_PAIE.NouZ(w_verif_bul) AND w_verif_bul != wsd_fcal1.nbul
		// THEN
		// err_msg :=
		// PA_PAIE.erreurp('ERR-90053',w_clang,wsal01.nmat,TO_CHAR(w_verif_bul,'0'));
		// --ERRORLOG(err_msg);
		// i_errcal := i_errcal + 1;
		// RETURN FALSE;
		// END IF;
		// END IF;
		//
		List listOfNumBulletin = (useRetroactif) ? service.find(queryStringRetro) : service.find(queryString);
		if (listOfNumBulletin == null || listOfNumBulletin.size() <= 0)
		{
			error = parameter.errorMessage("ERR-90053", parameter.getLangue(), parameter.getDossier());

			addToOutputtext("\n" + error);
			parameter.setError(error);
			// return false;
		}
		Object[] row = (Object[]) listOfNumBulletin.get(0);
		numeroBulletinVerified = (Integer) row[0];
		// IF retroactif THEN
		// BEGIN
		// DELETE FROM prcalc
		// WHERE identreprise = wpdos.identreprise
		// AND aamm = w_aamm
		// AND nmat = wsal01.nmat
		// AND nlot = w_nlot;
		// EXCEPTION
		// WHEN NO_DATA_FOUND THEN null;
		// WHEN OTHERS THEN
		// err_msg := 'INCIDENT DELETE FICHIER prcalc STATUS
		// '||TO_CHAR(SQLCODE);
		// RETURN FALSE;
		// -- erreur(err_msg) ;
		// END;
		queryStringRetro = "delete from Rhtprcalc" + " where identreprise ='" + infoSalary.getComp_id().getCdos() + "'" + " and aamm = '" + monthOfPay + "'" + " and nmat = '" + infoSalary.getComp_id().getNmat() + "'"
				+ " and nlot = " + nlot;
		// ELSE
		// BEGIN
		// DELETE FROM pacalc
		// WHERE identreprise = wpdos.identreprise
		// AND aamm = w_aamm
		// AND nmat = wsal01.nmat;
		// EXCEPTION
		// WHEN NO_DATA_FOUND THEN null;
		// WHEN OTHERS THEN
		// err_msg := 'INCIDENT DELETE FICHIER pacalc STATUS
		// '||TO_CHAR(SQLCODE);
		// RETURN FALSE;
		// -- erreur(err_msg) ;
		// END;
		queryString = "delete from CalculPaie" + " where identreprise ='" + infoSalary.getComp_id().getCdos() + "'" + " and aamm = '" + monthOfPay + "'" + " and nmat = '" + infoSalary.getComp_id().getNmat() + "'";
		// END IF;
		//
		if (useRetroactif)
			service.deleteFromTable(queryStringRetro);
		else
			service.deleteFromTable(queryString);
		// -- Sortie
		// verif_bul_o := w_verif_bul;
		//
		// RETURN TRUE;
		return (Integer) row[0];
	}

	/**
	 * => Absent_Tout_Mois est-ce que ce salarié était absent tout le mois?
	 * 
	 * @return true ou false
	 */
	// public boolean isAbsentToutMois(){
	// addToOutputtext("\n"+">>isAbsentToutMois");
	// // boolean w_retro = false;
	// Date debutConge = new Date();
	// Date finConge = new Date();
	// long w_mnt1 = -1;
	// long w_mnt4 = -1;
	// long w_mnt8 = -1;
	// Date date_min = null;
	// Date date_max = null;
	// Date date_suiv = null;
	// boolean w_suite = false;
	// Date ddeb = new Date();
	// Date dfin = new Date();
	// int nbjc = 0;
	// int nbja = 0;
	// String motf = "";
	// long mont = 0;
	// String firstDayOfMonthS = new ClsDate(firstDayOfMonth).getDateS();
	// String lastDayOfMonthS = new ClsDate(lastDayOfMonth).getDateS();
	//	    
	// String queryString = "from Rhteltvarconge "
	// + " where identreprise = '" + infoSalary.getComp_id().getCdos() + "'"
	// + " and nmat = '" + infoSalary.getComp_id().getNmat() + "'"
	// + " and aamm = '" + monthOfPay + "'"
	// + " and nbul = " + nbul
	// + " and ddeb between '" + firstDayOfMonthS + "' and '" +
	// lastDayOfMonthS + "'"
	// + " and dfin between '" + firstDayOfMonthS + "' and '" + lastDayOfMonthS
	// + "'"
	// + " order by motf, ddeb";
	//
	// String queryStringRetro = "from Rhthevcg "
	// + " where identreprise = '" + infoSalary.getComp_id().getCdos() + "'"
	// + " and nmat = '" + infoSalary.getComp_id().getNmat() + "'"
	// + " and aamm = '" + monthOfPay + "'"
	// + " and nbul = " + nbul
	// + " and ddeb between '" + firstDayOfMonthS + "' and '" +
	// lastDayOfMonthS + "'"
	// + " and dfin between '" + firstDayOfMonthS + "' and '" + lastDayOfMonthS
	// + "'"
	// + " order by motf, ddeb";
	//		
	// List listOfInfoConge = (useRetroactif)? service.find(queryStringRetro) :
	// service.find(queryString);
	// if(ClsObjectUtil.isListEmty(listOfInfoConge))
	// return false;
	//		
	// ClsElementVariableCongeClone evConge = null;
	// // try{
	// for (Object obj : listOfInfoConge) {
	// //utilisation de ClsElementVariableCongeClone
	// evConge = useRetroactif? new ClsElementVariableCongeClone((Rhthevcg)obj)
	// : new ClsElementVariableCongeClone((Rhteltvarconge)obj);
	// //
	// ddeb = evConge.getComp_id().getDdeb();
	// dfin = evConge.getDfin();
	// if(evConge.getNbjc() != null)
	// nbjc = evConge.getNbjc().intValue();
	// if(evConge.getNbja() != null)
	// nbja = evConge.getNbja().intValue();
	// if(evConge.getMotf() != null)
	// motf = evConge.getMotf();
	// if(evConge.getMont() != null)
	// mont = evConge.getMont().longValue();
	// // IF w_retro THEN
	// if(parameter.isUseRetroactif()){
	// // FETCH curs_ver_cg2 INTO wevcg;
	// // EXIT WHEN curs_ver_cg2%NOTFOUND;
	// // BEGIN
	// // select MAX(DECODE(nume,1,NVL(valm,0))),
	// // MAX(DECODE(nume,4,NVL(valm,0))),
	// // MAX(DECODE(nume,8,NVL(valm,0)))
	// // INTO w_mnt1, w_mnt4, w_mnt8
	// // from pahfnom
	// // where identreprise = PA.identreprise
	// // and ctab = 22
	// // and cacc = wevcg.motf
	// // and nume IN (1,4,8)
	// // and aamm = w_aamm
	// // and nbul = w_nbul;
	// // EXCEPTION
	// // WHEN NO_DATA_FOUND THEN
	// // null;
	// // END;
	// String strQueryMaxValm = "select valm from Rhthfnom "
	// + " where identreprise = '" + infoSalary.getComp_id().getCdos() + "'"
	// + " and nume in (1, 4, 8)"
	// + " and aamm = '" + monthOfPay + "'"
	// + " and nbul = " + nbul
	// + " and ctab = 22"
	// + " and cacc ='" + motf + "'";
	// //pour w_mnt1
	// List listOfMax = service.find(strQueryMaxValm);
	// if(listOfMax != null && listOfMax.size() >= 3){
	// if(listOfMax.get(0) != null)
	// w_mnt1 = (Long)listOfMax.get(0);
	// if(listOfMax.get(1) != null)
	// w_mnt4 = (Long)listOfMax.get(1);
	// if(listOfMax.get(2) != null)
	// w_mnt8 = (Long)listOfMax.get(2);
	// }
	// }
	// // ELSE
	// else{
	// //
	// // FETCH curs_ver_cg INTO wevcg;
	// // EXIT WHEN curs_ver_cg%NOTFOUND;
	// // BEGIN
	// // select MAX(DECODE(nume,1,NVL(valm,0))),
	// // MAX(DECODE(nume,4,NVL(valm,0))),
	// // MAX(DECODE(nume,8,NVL(valm,0)))
	// // INTO w_mnt1, w_mnt4, w_mnt8
	// // from pafnom
	// // where identreprise = PA.identreprise
	// // and ctab = 22
	// // and cacc = wevcg.motf
	// // and nume IN (1,4,8);
	// // EXCEPTION
	// // WHEN NO_DATA_FOUND THEN
	// // null;
	// // END;
	// String strQueryMaxValm = "select valm from Rhfnom "
	// + " where identreprise = '" + infoSalary.getComp_id().getCdos() + "'"
	// + " and nume in (1, 4, 8)"
	// + " and ctab = 22"
	// + " and cacc ='" + motf + "'";
	// //pour w_mnt1
	// List listOfMax = service.find(strQueryMaxValm);
	// if(listOfMax != null && listOfMax.size() >= 3){
	// addToOutputtext("\n"+"........... len = " + listOfMax.size());
	// if(listOfMax.get(0) != null)
	// w_mnt1 = (Long)listOfMax.get(0);
	// if(listOfMax.get(1) != null)
	// w_mnt4 = (Long)listOfMax.get(1);
	// if(listOfMax.get(2) != null)
	// w_mnt8 = (Long)listOfMax.get(2);
	// }
	// }
	// //
	// // END IF;
	// //
	// // w_suite := TRUE;
	// w_suite = true;
	// // IF SQL%NOTFOUND THEN
	// // w_suite := FALSE;
	// // END IF;
	// //
	// // IF w_suite THEN
	// // addToOutputtext("\n"+"");
	// if(w_suite){
	// // IF w_mnt1 = 0 and w_mnt4 = 0 and w_mnt8 = 0 THEN
	// if( w_mnt1 == 0 && w_mnt4 == 0 && w_mnt8 == 0){
	// // IF date_min IS NULL THEN
	// if(date_min == null){
	// // date_min := wevcg.ddeb;
	// date_min = ddeb;
	// // date_max := wevcg.dfin;
	// date_max = dfin;
	// // w_contigue := TRUE;
	// // w_contigue = true;
	// // END IF;
	// }
	// //
	// // IF date_suiv IS NULL THEN
	// if(date_suiv == null){
	// // date_suiv := wevcg.dfin;
	// date_suiv = dfin;
	// }
	// // ELSE
	// else{
	// // IF wevcg.ddeb != date_suiv + 1 THEN
	// if(date_suiv != null && ddeb != new ClsDate(date_suiv,
	// "yyyy-MM-dd").addDay(1)){
	// // w_contigue := FALSE;
	// // w_contigue = false;
	// // EXIT;
	// break;
	// }
	// // ELSE
	// else
	// // date_suiv := wevcg.dfin;
	// date_suiv = dfin;
	// // END IF;
	// // END IF;
	// }
	// // IF wevcg.dfin > date_max THEN
	// if( date_max != null
	// && dfin.getTime() > new ClsDate(date_max,
	// "yyyy-MM-dd").getDate().getTime())
	// // date_max := wevcg.dfin;
	// date_max = dfin;
	// // END IF;
	// // END IF;
	// }
	// }
	// // ELSE
	// else{
	// // w_contigue := FALSE;
	// // w_contigue = false;
	// // EXIT;
	// break;
	// // END IF;
	// }
	// // END LOOP;
	// }
	// // }
	// // catch(Exception e){
	// // throw new Exception(e.getMessage());
	// // }
	// //
	// // IF w_retro THEN
	// // CLOSE curs_ver_cg2;
	// // ELSE
	// // CLOSE curs_ver_cg;
	// // END IF;
	// //
	// // IF date_min = w_date_deb and date_max = w_date_fin THEN
	// addToOutputtext("\n"+"date_min : " + date_min);
	// if(date_min != null)
	// date_min = new ClsDate(date_min, "yyyy-MM-dd").getDate();
	// if(debutConge != null)
	// debutConge = new ClsDate(debutConge, "yyyy-MM-dd").getDate();
	// if(date_max != null)
	// date_max = new ClsDate(date_max, "yyyy-MM-dd").getDate();
	// if(finConge != null)
	// finConge = new ClsDate(finConge, "yyyy-MM-dd").getDate();
	//    	
	// if( date_min == debutConge && date_max == finConge)
	// // RETURN TRUE;
	// return true;
	// // ELSE
	// else
	// // RETURN FALSE;
	// return false;
	// }
	/**
	 * => trait_sal
	 * <p>
	 * Traitement proprement dit de la paie d'un salarié
	 * </p>
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<ClsValeurRubrique>> valeursModuleExterne;
	boolean tjrsComparer = false;
	public void traitementSalaire(String strDateFormat)
	{
		ParamData nome1 = service.findAnyColumnFromNomenclature(infoSalary.getComp_id().getCdos(),null, "266","COMPARRUB", "2");
		tjrsComparer = StringUtils.equalsIgnoreCase(nome1.getVall(), "O");
		// addToOutputtext("\n"+">>traitementSalaire");
		try
		{
			this.deleteBulletin();
		}
		catch (Exception e)
		{
			// logger
			e.printStackTrace();
		}
		// double salaireIterPaire = 0;
		// double salaireIterImpaire = 0;

		// x_an NUMBER(5);
		// x_mois NUMBER(5);
		// wmois NUMBER(5);
		// nbr_an NUMBER(5);
		// A_ajouter_au_net NUMBER(15,3);
		//
		// w_options VARCHAR2(50);
		// w_rowid ROWID;
		// Rbq_EV parubq.crub%TYPE;
		//
		// CURSOR curs_evcg IS
		// SELECT * FROM paevcg
		// WHERE identreprise = wpdos.identreprise
		// AND nmat = wsal01.nmat
		// AND aamm = w_aamm
		// AND nbul = wsd_fcal1.nbul;
		//
		// CURSOR curs_evcg2 IS
		// SELECT * FROM pahevcg
		// WHERE identreprise = wpdos.identreprise
		// AND nmat = wsal01.nmat
		// AND aamm = w_aamm
		// AND nbul = wsd_fcal1.nbul;
		//
		// CURSOR curs_taux_ajus IS
		// SELECT rowid, crub, ajnu, NVL(mont,0)
		// FROM parubajus
		// WHERE NVL(ajnu,0) = w_numcal
		// AND sessionid = w_sessionid;
		//
		// BEGIN
		// -------------------------------------------------------------------------------
		// -- LH 281197
		// -- TEST SI SALARIE EXPATRIE
		// -- typ_rec = Zone de pasa01 a tester : 1=Regime, 2=Type de contrat, 3=Classe salarie
		// -- val_exp = Valeur de la zone pour un expatrie
		// -------------------------------------------------------------------------------
		// Expatrie := FALSE;
		this.setExpatrie(this._isExpatrie());
		// spécifique cnss
//		if (StringUtils.equals(parameter.nomClient, ClsEntreprise.CNSS) || StringUtils.equals(parameter.nomClient, ClsEntreprise.SDV_RDC) )
//		{
			// CURSOR Curs_TB27 IS
			// SELECT valt FROM pafnom
			// WHERE identreprise = wpdos.identreprise
			// AND ctab = 27
			// AND cacc = wsal01.devp
			// AND nume = 1;
			// -- MM 11-2005 convertion du salaire net en devise locale si devp != dev dos.
			// IF wsal01.pnet = 'O' THEN
			// IF wsal01.devp != Devise_Dossier THEN
			// OPEN Curs_TB27;
			// FETCH Curs_TB27 INTO wfnom.tau1;
			// CLOSE Curs_TB27;
			// wsal01.snet := ROUND( wsal01.snet * NVL(wfnom.tau1,0), Nb_Dec_Devise_Dossier);
			// END IF;
			// END IF;
			if(StringUtils.isBlank(infoSalary.getDevp()))
				infoSalary.setDevp(parameter.dossierCcy);
			
			if (StringUtils.equals("O", infoSalary.getPnet()) && !infoSalary.getDevp().equalsIgnoreCase(parameter.dossierCcy))
			{
				ParamData nome = service.findAnyColumnFromNomenclature(infoSalary.getComp_id().getCdos(), null,"27", infoSalary.getDevp(), "1");
				double taux = (nome.getValt()!=null) ? nome.getValt().doubleValue() : 0;
				double oldSNet = infoSalary.getSnet() == null ? 0 : infoSalary.getSnet().doubleValue();
				double newSNet = oldSNet * taux;
				infoSalary.setSnet(new BigDecimal(newSNet));
			}
//		}
		//
		// -- LH 160700
		// ancien := 0; -- anciennete
		// Nb_Mois_Ancien := 0; -- Nb de mois d'anciennete de l'Agent
		// Age_Agent := 0; -- Age de l'Agent
		// ------------------------------------------------------------------------------
		// -- CALCUL ANCIENNETE
		// IF Mode_Test THEN pap_logins(' Calcul anciennete'); END IF;
		// ------------------------------------------------------------------------------
		// IF Ste_SGMB AND TO_CHAR(wsal01.ddca,'DD') <> '01'
		// THEN
		// wsal01.ddca := add_months( wsal01.ddca , 1 );
		// END IF;
		//
		// x_an := TO_NUMBER(SUBSTR(w_aamm,1,4));
		// x_mois := TO_NUMBER(SUBSTR(w_aamm,5,2));
		//
		// -- MM Modif DELTACI-010302-94 Controle de coherence sur la date
		// anciennete
		// IF x_an - TO_NUMBER(TO_CHAR(wsal01.ddca,'YYYY')) > 65 THEN
		// pb_calcul := TRUE;
		// err_msg := PA_PAIE.erreurp('ERR-90045',w_clang,wsal01.nmat);
		// RETURN FALSE;
		// END IF;
		// -- Fin modif MM.
		//
		// -- MM 03-2003 Ajout fonction calcul anciennete avec deduction des
		// arrets paie
		// nbr_an :=
		// pa_paie.calc_anc(wsal01.identreprise,wsal01.ddca,wsal01.nmat,w_aamm);
		// -- Fin modif MM.
		//
		// IF nbr_an < 0 THEN
		// -- LH 160700 nbr_an := 0;
		// ancien := 0;
		// ancien1 := 0;
		// pb_calcul := TRUE;
		// err_msg := PA_PAIE.erreurp('ERR-90046',w_clang,wsal01.nmat);
		// RETURN FALSE;
		// END IF;
		//
		// IF nbr_an > 65 THEN
		// pb_calcul := TRUE;
		// err_msg := PA_PAIE.erreurp('ERR-90045',w_clang,wsal01.nmat);
		// RETURN FALSE;
		// END IF;
		//
		// ancien := nbr_an;
		// ancien1 := nbr_an;
		//
		// IF ancien < 0 THEN
		// ancien := 0;
		// ancien1 := 0;
		// pb_calcul := TRUE;
		// err_msg := PA_PAIE.erreurp('ERR-90046',w_clang,wsal01.nmat);
		// RETURN FALSE;
		// ELSIF ancien > w_ancmax THEN
		// ancien := w_ancmax;
		// ancien1 := w_ancmax;
		// END IF;
		//
		String paieAuNet = "N";
		try
		{
			// addToOutputtext("\n"+">> calculateAnciennete");
			this.anciennete = this.calculateAnciennete(strDateFormat);

			// addToOutputtext("\n"+">> ancienneté :" + anciennete);
			// ------------------------------------------------------------------------------
			// -- CALCUL Age de l'agent
			// IF Mode_Test THEN pap_logins(' Calcul Age Agent'); END IF;
			// ------------------------------------------------------------------------------
			//
			// -- MM Modif DELTACI-010302-94 Controle de coherence sur la date
			// naissance
			// IF FLOOR( ( date_dep - wsal01.dtna ) / 365 ) > 100 THEN
			// pb_calcul := TRUE;
			// err_msg := PA_PAIE.erreurp('ERR-90047',w_clang,wsal01.nmat);
			// RETURN FALSE;
			// END IF;
			//
			// nbr_an := FLOOR( ( date_dep - wsal01.dtna ) / 365 );
			// IF nbr_an > 100 THEN
			// pb_calcul := TRUE;
			// err_msg := PA_PAIE.erreurp('ERR-90047',w_clang,wsal01.nmat);
			// RETURN FALSE;
			// END IF;
			// IF nbr_an < 0 THEN
			// pb_calcul := TRUE;
			// err_msg := PA_PAIE.erreurp('ERR-90048',w_clang,wsal01.nmat);
			// RETURN FALSE;
			// END IF;
			// Age_Agent := nbr_an;

			// calcule l'ége de l'agent
			// addToOutputtext("\n"+">> calculateAgentAge");
			this.ageOfAgent = calculateAgentAge();

			// addToOutputtext("\n"+">> ageOfAgent : " + ageOfAgent);
			// [@yannick : Calcul fictif laissé en suspend
			/** *CALCUL FICTIF EST LAISSE EN SUSPENS**** */

			//
			// -------------------------------------------------------------------------
			// -- LH 281197
			// -- TEST SI CALCUL FICTIF A LANCER POUR LE MATRICULE
			// -- On ne lance pas le fictif pour les expatries si on a pas
			// Fictif_Expat
			// -------------------------------------------------------------------------
			// -- LH 040298
			// -- TEST SI Bulletin deja valide.Donc on conserve l'avance conges
			// calculee
			// -- il ne faut pas relancer le fictif.
			// -------------------------------------------------------------------------
			// IF NOT PA_PAIE.NouB(wsal01.pmcf) AND
			// w_fictif = 'O' AND
			// NOT PA_PAIE.Bulletin_valide( wpdos.identreprise, wsal01.nmat) AND
			// NOT retroactif
			// THEN
			// IF wsal01.pmcf = w_aamm THEN
			// IF ( wsal01.ddcf >= w_ddpa AND wsal01.dfcf <= w_dfpa ) OR
			// ( Expatrie AND NOT Fictif_Expat )
			// THEN
			// -- Le conge est inclus dans le mois de paiement : pas de fictif
			// -- Il s'agit d'un expatrie et ne leur calcul pas de fictif
			// NULL;
			// ELSE
			// IF Mode_Test THEN pap_logins('Lancement fictif'); END IF;
			// -- MM 08/2004 on passe le code util. sur 4 char.
			// w_options:= wpdos.identreprise || LTRIM(TO_CHAR(wsd_fcal1.nbul))
			// || w_aamm
			// || RPAD(w_cuti,4,' ')
			// || wsal01.nmat;
			// Pb_Calcul := FALSE;
			// PA_CALFIC.calcul(w_options,w_sessionid,Pb_Calcul,err_msg);
			// -- SAVEPOINT Test_Save;
			//
			// IF w_typ_fictif = 'B' THEN
			// RETURN Pb_Calcul;
			// END IF;
			// END IF;
			// END IF;
			// END IF;
			boolean bulValide = this.bulletinValide();
			ClsFictifSalaryToProcess salarie = null;
			if (StringUtils.isNotBlank(this.infoSalary.getPmcf()) && StringUtils.equals("O", this.parameter.getFictiveCalculus()) && !bulValide && !this.parameter.isUseRetroactif())
			{
				if (StringUtils.equals(this.infoSalary.getPmcf(), this.parameter.getMonthOfPay()))
				{
					String ddcf = new ClsDate(this.infoSalary.getDdcf()).getDateS("yyyyMMdd");
					String dfcf = new ClsDate(this.infoSalary.getDfcf()).getDateS("yyyyMMdd");
					String ddpa = new ClsDate(this.parameter.getFirstDayOfMonth()).getDateS("yyyyMMdd");
					String dfpa = new ClsDate(this.parameter.getLastDayOfMonth()).getDateS("yyyyMMdd");
					if (((ddcf.compareTo(ddpa) >= 0) && (dfcf.compareTo(dfpa) <= 0))
							|| (this.isExpatrie() && !this.parameter.isFictiveCalculusForExpatrie()))
					{

					}
					else
					{
						this.parameter.setPbWithCalulation(false);
						// lancement du calcul fictif

						salarie = this.convertToFictiveSalary();

						salarie.traiterSalaireFictif();

						// salarie.traiterSalaire();

						if (StringUtils.equals("B", this.parameter.getFictiveCalculusType()))
						{
							if (this.parameter.isPbWithCalulation())
								return;
						}
					}
				}
			}

			this.parameter.myMonthOfPay = new ClsDate(this.parameter.monthOfPay, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
			this.myMonthOfPay = this.parameter.myMonthOfPay;
			if (salarie != null)
			{
				// ajout de l'avance congé généré en ev
				// memoListeAvancesConge = this.getListOfEltvar().get(this.param.getFictiveRubrique());
				List<Object[]> avances = new ArrayList<Object[]>();
				avances.add(salarie.elementsEVAvancesConges);
				this.getListOfEltvar().put(salarie.param.getFictiveRubrique(), avances);

				this.buildLignePretMap();

				this.buildNumeroPretMap();
			}

			//
			/** ***************************************** */
			// -------------------------------------------------------------------------------
			// -- R A Z ELEMENTS DE CALCUL
			// -------------------------------------------------------------------------------
			// negat := 0; -- bulletin negatif
			// k := 0;
			// zz := 0;
			// wnbjc := 0; -- nbr de jours de conges
			// mois_deb := 0;
			// mois_fin := 0;
			//
			// ----- pc le 28.06.95, pb si des horaires et des mensuels se
			// suivent
			// --IF PA_PAIE.NouZ(wnbhm) THEN
			// -- wnbh := 0;
			// --ELSE
			// -- wnbh := wnbhm;
			// --END IF;
			/** ***************************************** */
			/** ******chargementRubriqueSaisiesEnNetAjus**** */
			/** ***************************************** */
			// ----------------------------------------------------------------------------
			// -- PAIE AU NET :
			// -- CHARGEMENT MONTANT RUBRIQUE SAISIE EN NET ET SOUMISES A
			// AJUSTEMENT
			// ----------------------------------------------------------------------------
			// w_paienet := wsal01.pnet;
			paieAuNet = this.getInfoSalary().getPnet();
			if (ClsObjectUtil.isNull(paieAuNet))
				paieAuNet = "N";
			//
			// IF w_paienet = 'O'
			// THEN
			// IF Mode_Test THEN pap_logins('Paie au net'); END IF;
			//
			// w_calcul := 'O';
			// nbiter := 0;
			// w_numcal := 0;
			//
			// IF rubajus_exist THEN
			// -- Chargement des montants (E.F. et E.V.),
			// -- rubriques ajustees pour le salarie en cours
			// W_Faitout := charg_ajus;
			// END IF;
			// IF rubnet_exist THEN
			// -- Chargement des montants (E.F. et E.V.),
			// -- rubriques en net pour le salarie en cours
			// W_Faitout := charg_net;
			// END IF;
			//
			// END IF;
			//
			// chargement des montants de rubriques saisies en net
			// et soumises é un ajustement
			// addToOutputtext("\n"+">>appel chargementRubriqueSaisiesEnNetAjus");

			this.chargementRubriqueSaisiesEnNetAjus();

			// addToOutputtext("\n"+">>fin chargementRubriqueSaisiesEnNetAjus");
			/** ***************************************** */

			/** ********************************************** */
			/** ****calculateNbreJourAbsenceEtCongeTotal****** */
			/** ********************************************** */
			// -------------------------------------------------------------------------------
			// -- CALCUL DES CONGES ET ABSENCES
			// IF Mode_Test THEN pap_logins(' Traitement des conges/absences');
			// END IF;
			// -------------------------------------------------------------------------------
			// IF w_bas30 = 'O' THEN
			// IF NOT PA_PAIE.NouZ(w_bnbj) THEN
			// wnbjt := w_bnbj;
			// ELSE
			// wnbjt := 30;
			// END IF;
			// ELSE
			// wnbjt := nbm_jrn;
			// END IF;
			if ("O".equals(this.getParameter().getBase30Rubrique()))
			{
				if (this.getParameter().getBase30NombreJour() > 0)
				{
					this.getWorkTime().setProrataNbreJourTravaillees(this.getParameter().getBase30NombreJour());
				}
				else
					this.getWorkTime().setProrataNbreJourTravaillees(30);
			}
			else
				// this.getWorkTime().setProrataNbreJourTravaillees(this.getParameter().getNbreJourMoisPourProrata());
				this.getWorkTime().setProrataNbreJourTravaillees(this.getParameter().getMyMonthOfPay().getMaxDayOfMonth());
			//
			// wnbht := 0; -- nb heures travaillees
			// wnbjc := 0; -- nb jours conges annuels
			// wnbjcpnp := 0; -- nb jours conges payes non pris
			// wnbjcp := 0; -- nb jours conges ponctuels (ouvrables)
			// wnbjcpa := 0; -- nb jours conges ponctuels (calendaires)
			// wmntcp := 0; -- montant des conges payes
			// nbm_sup := 0; -- nombre de mois suppl. (extrapolation de calcul)
			// wnbja := 0; -- nb jours absences
			// wnbjca := 0;
			// wnbjsmm := 0;
			// wnbjsmmpnp := 0;
			// wnbjcapnp := 0;
			// wnbjs := 0;
			// debmois := FALSE;
			// wnbjc_ms := 0; -- nb jours conges annuels mois suivant
			// wnbjsmm_ms := 0; -- nb jours conges salaire moyen mensuel mois
			// suivant
			// wnbjc_ma := 0; -- nb jours conges annuels mois anterieurs
			// wnbja_ma := 0; -- nb jours absence conges annuels mois anterieurs
			// wnbja_cg_abs := 0;
			//
			// IF retroactif THEN
			// BEGIN
			// SELECT ddpa, dfpa
			// INTO w_ddpa, w_dfpa
			// FROM pahev
			// WHERE identreprise = wpdos.identreprise
			// AND nmat = wsal01.nmat
			// AND aamm = w_aamm
			// AND nbul = wsd_fcal1.nbul;
			// EXCEPTION
			// WHEN NO_DATA_FOUND THEN
			// -- MM 13/12/2000 Aucun sens !!!!!!! et en plus ca fout la
			// merde...
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
			// WHERE identreprise = wpdos.identreprise
			// AND nmat = wsal01.nmat
			// AND aamm = w_aamm
			// AND nbul = wsd_fcal1.nbul;
			// EXCEPTION
			// WHEN NO_DATA_FOUND THEN
			// -- MM 13/12/2000 Aucun sens !!!!!!! et en plus ca fout la
			// merde...
			// -- w_ddpa := NULL;
			// -- w_dfpa := NULL;
			// null;
			// -- Fin Modif MM.
			// END;
			// END IF;
			// IF NOT tab91 THEN
			// -- Permet de calculer le nombre de mois supplementaire
			// IF w_ddpa IS NOT NULL THEN
			// mois_deb := TO_NUMBER(TO_CHAR(w_ddpa,'MM'));
			// mois_fin := TO_NUMBER(TO_CHAR(w_dfpa,'MM'));
			// IF mois_fin < mois_deb THEN
			// mois_fin := TO_NUMBER(TO_CHAR(wpdos.ddex,'MM'));
			// END IF;
			// nbm_sup := mois_fin - mois_deb;
			// END IF;
			// END IF;
			this.calculateNbreMoisSuppl();

			/** ********************************************** */
			/** ****calculateNbreJourCongeEtAbsence****** */
			/** ********************************************** */
			//
			// -- Recherche du nombre de periodes pour le calcul des reguls.
			// nbper_regu := 0;
			// IF NOT PA_PAIE.NouB(rub_regper) THEN
			// nbper_regu :=
			// paf_LecEvar(wsal01.nmat,w_aamm,wsd_fcal1.nbul,rub_regper);
			// IF PA_PAIE.NouZ(nbper_regu) THEN
			// nbper_regu := 0;
			// END IF;
			// END IF;
			//
			this.calculNbrePeriodeRegulation();

			// -- Recherche du nombre de jours supplementaires
			// wnbjs := paf_LecEvar(wsal01.nmat,w_aamm,wsd_fcal1.nbul,rub_nbjs);
			//
			// IF PA_PAIE.NouZ(wnbjs) THEN
			// wnbjs := 0;
			// END IF;
			//

			this.calculateNbreJourSuppl();

			// -- Recherche du nombre de jours payes non pris
			// wnbjcapnp :=
			// paf_LecEvar(wsal01.nmat,w_aamm,wsd_fcal1.nbul,rub_pnp);
			//
			// IF PA_PAIE.NouZ(wnbjcapnp) THEN
			// wnbjcapnp := 0;
			// wnbjcpnp := 0;
			// wnbjsmmpnp := 0;
			// ELSE
			// wnbjcpnp := wnbjcapnp;
			// wnbjsmmpnp := wnbjcapnp;
			// END IF;

			this.calculNbreJourPaidNonPris();

			// -- Calcul des jours de conges et d'absences
			// nb_evcg := 0;
			// IF retroactif THEN
			// OPEN curs_evcg2;
			// ELSE
			// OPEN curs_evcg;
			// END IF;
			// LOOP
			// IF retroactif THEN
			// FETCH curs_evcg2 INTO wevcg;
			// EXIT WHEN curs_evcg2%NOTFOUND;
			// ELSE
			// FETCH curs_evcg INTO wevcg;
			// EXIT WHEN curs_evcg%NOTFOUND;
			// END IF;
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
			// err_msg :=
			// PA_PAIE.erreurp('ERR-90049',w_clang,wsal01.nmat,wevcg.motf);
			// EXIT;
			// END IF;
			//
			// IF PA_PAIE.NouZ(wfnom.mnt4) THEN
			// wfnom.mnt4 := 0;
			// END IF;
			// IF wfnom.mnt1 = 0 THEN
			// ----------- TRAITEMENT DES ABSENCES
			// --------------------------------
			// -- On ne tient pas compte des absences dont mnt4 = 1 ou qui sont
			// -- superieure au mois de paie
			// IF wfnom.mnt4 != 1 AND
			// -- wevcg.dfin <= w_dfpa
			// -- LH 230298
			// wevcg.dfin <= Dernier_Jour_du_Mois
			// THEN
			// -- Absence a prendre en compte
			// wnbja := wnbja + wevcg.nbja;
			//
			// IF wevcg.motf = w_cg_abs
			// THEN
			// -- On est sur une absence generee par un conges en automatique
			// -- wnbja_cg_abs sera utilise par prorat_fic.
			// wnbja_cg_abs := wnbja_cg_abs + wevcg.nbja;
			// END IF;
			//
			// -- L'absence genere un paiement sur la rubrique stockee dans mnt8
			// IF NOT PA_PAIE.NouZ(wfnom.mnt8) THEN
			// Rbq_EV := LTRIM(TO_CHAR(wfnom.mnt8,'0999'));
			// Pseudo_Ev(Rbq_Ev, wevcg.nbja);
			// ELSE
			// Pseudo_Ev(w_rubjnp, wevcg.nbja);
			// END IF;
			//
			// IF NOT PA_PAIE.NouZ(w_rubabs) THEN
			// Pseudo_Ev(w_rubabs, wevcg.nbja);
			// END IF;
			// END IF;
			// ELSE
			// ----------- TRAITEMENT DES CONGES
			// --------------------------------
			// IF wevcg.ddeb >= w_ddpa AND wevcg.dfin <= w_dfpa THEN
			// IF NOT PA_PAIE.NouZ(w_rubjnp) THEN
			// Pseudo_Ev(w_rubjnp, wevcg.nbja);
			// END IF;
			//
			// -- on ne prend en compte que les conges dont mnt4 = 0
			// -- Ils seront quand meme stockes dans l'histo des conges/Abs.
			// IF wfnom.mnt4 != 1 THEN
			// IF wfnom.mnt3 = 0 THEN -- Conges annuels
			// wnbjc := wnbjc + wevcg.nbjc;
			// wnbjsmm := wnbjsmm + wevcg.nbja;
			// wnbjca := wnbjca + wevcg.nbja;
			// IF wevcg.ddeb = w_ddpa THEN
			// debmois := TRUE;
			// END IF;
			// ELSIF wfnom.mnt3 = 1 THEN -- Conges ponctuels
			// wnbjcp := wnbjcp + wevcg.nbjc;
			// wnbjcpa := wnbjcpa + wevcg.nbja;
			// IF NOT PA_PAIE.NouZ(wevcg.mont) THEN
			// wmntcp := wevcg.mont;
			// END IF;
			// END IF;
			// END IF;
			//
			// IF wfnom.mnt3 = 0 OR wfnom.mnt3 = 1 THEN
			// IF NOT PA_PAIE.NouZ(w_rubabs) THEN
			// Pseudo_Ev(w_rubabs, wevcg.nbja);
			// END IF;
			// END IF;
			//
			// END IF; -- FIN 'IF wevcg.ddeb >= w_ddpa AND
			// -- wevcg.dfin <= w_dfpa'
			//
			// -- Nombre de jours pour conges posterieurs
			// IF wevcg.ddeb > w_dfpa THEN
			// IF wfnom.mnt3 = 0 THEN -- Conges annuels
			// wnbjc_ms := wnbjc_ms + wevcg.nbjc;
			// wnbjsmm_ms := wnbjsmm_ms + wevcg.nbja;
			// END IF;
			// END IF;
			//
			// -- Nombre de jours pour conges anterieurs
			// IF wevcg.ddeb < w_ddpa THEN
			// IF wfnom.mnt3 = 0 THEN -- Conges annuels
			// wnbjc_ma := wnbjc_ma + wevcg.nbjc;
			// wnbja_ma := wnbja_ma + wevcg.nbja;
			// END IF;
			// END IF;
			//
			// END IF; -- FIN 'IF wfnom.mnt1 = 0'
			//
			// END LOOP; -- FIN LOOP sur 'curs_evcg INTO wevcg.*
			// IF retroactif THEN
			// CLOSE curs_evcg2;
			// ELSE
			// CLOSE curs_evcg;
			// END IF;
			//
			// IF pb_calcul THEN
			// RETURN FALSE;
			// END IF;
			//
			// IF nb_evcg = 0 THEN
			// evcg_exist := FALSE;
			// IF Mode_Test THEN pap_logins(' Pas de conges/absences'); END IF;
			// ELSE
			// evcg_exist := TRUE;
			// IF Mode_Test THEN pap_logins(' on a des conges/absences'); END
			// IF;
			// END IF;
			//
			this.getWorkTime().calculateNbreJourCongeEtAbsence(this);
		}
		catch (Exception e)
		{
			// logger
			e.printStackTrace();
		}
		/** ********************************************** */
		// ------------------------------------------------------------------------------
		// -- Recherche du nombre d'heures et du nombre de jours travailles pour
		// proratas
		// ------------------------------------------------------------------------------
		//
		// -- recup nombre d'heures de la section
		// IF PA_PAIE.NouZ(wnbh) THEN
		// wnbh := paf_lecfnomT(3,wsal01.niv3,1,w_aamm,w_nlot,wsd_fcal1.nbul);
		// END IF;
		//
		// IF wsal01.cods = 'HC' OR wsal01.cods = 'HP' THEN
		// wnbjt := paf_lecEvar(wsal01.nmat,w_aamm,wsd_fcal1.nbul,rub_nbj);
		//
		// IF PA_PAIE.NouZ(wnbjt) THEN
		// IF NOT PA_PAIE.NouZ(w_bnbj) THEN -- nb jours travailles
		// wnbjt := w_bnbj;
		// ELSE
		// wnbjt := 30;
		// END IF;
		// END IF;
		//
		// wnbht := paf_lecEvar(wsal01.nmat,w_aamm,wsd_fcal1.nbul,rub_hor);
		// IF PA_PAIE.NouZ(wnbht) THEN
		// wnbht := 0;
		// END IF;
		//
		// -- Si wnbht = 0 alors passage au salarie suivant
		// IF wnbht = 0 AND w_fictif = 'N' THEN
		// ----------------
		// COMMIT;
		// ----------------
		// RETURN TRUE;
		// END IF;
		// END IF;
		//
		this.getWorkTime().prorataNbreHeuresJoursTravailles(this);
		// ClsObjectUtil.displayClassProperties(this.getWorkTime().getClass(),
		// this.getWorkTime());
		// -------------------------------------------------------------------------------
		// -- APPEL TRAITEMENT RUBRIQUES
		// IF Mode_Test THEN pap_logins(' Appel traitement des rubriques'); END
		// IF;
		// -------------------------------------------------------------------------------
		//
		// WHILE TRUE LOOP
		boolean arret = false;
		int numeroAjustementActuel = 0;
		// String paieAuNet = this.getInfoSalary().getPnet();
		// addToOutputtext("\n"+">> paieAuNet :" + paieAuNet);
		while (true)
		{
			// ----------------------------------------------------------------------------
			// -- PAIE AU NET : Recalcul du net en fonction de la presence
			// -- et ajout des rubriques concernees
			// ----------------------------------------------------------------------------
			//
			// IF w_paienet = 'O' THEN
			if ("O".equals(paieAuNet.toUpperCase()))
			{
				//
				// nbiter := 0;
				// w_numcal := w_numcal + 1;
				this.nbiter = 0;
				numeroAjustementActuel = this.getParameter().getNumeroAjustementActuel();
				numeroAjustementActuel++;
				this.getParameter().setNumeroAjustementActuel(numeroAjustementActuel);
				// IF Mode_Test THEN pap_logins('PNET: No de calcul =' ||
				// to_char(w_numcal, '999')); END IF;
				//
				// -- LH 140298
				// Cpt_valajus := Max_Nb_valajus;
				this.compteurValeurAjus = this.maxNbreValeurAjus;

				//
				// -- LH 161297 Les absences sont prises en compte
				// -- nbj := wnbjt;
				// -- LH 120298
				// -- MM 05-2003
				// -- nbj := wnbjt - wnbja
				// nbj := wnbjt - wnbja + wnbjs;
				// IF Ste_SHELL_GABON
				// THEN
				// nbj := nbj + wnbjcpnp;
				// END IF;
				// Net_a_trouver := ROUND( wsal01.snet * nbj / wnbjt, 0 );
				//
				double nbreJourProrata = this.getWorkTime().getProrataNbreJourTravaillees() - this.getWorkTime().getNbreJourAbsence() + this.getWorkTime().getNbreJoursSupplementaires();
				// addToOutputtext("\n"+">>>>>>>>>>>>>>>>>> nbreJourProrata:" +
				// nbreJourProrata);
				if (this.getEntreprise().equals(ClsEnumeration.EnEnterprise.SHELL_GABON))
					nbreJourProrata += this.getWorkTime().getNbreJourCongesNonPris();

				double a = 0;
				if (!ClsObjectUtil.isNull(this.getInfoSalary().getSnet()))
					a = this.getInfoSalary().getSnet().doubleValue();

				addToOutputtext("\n" + "Salaire net de l'agent dans la fiche salarié = " + a);
				if (this.getWorkTime().getProrataNbreJourTravaillees() != 0)
					this.salaireNetATrouver = Math.ceil(a * nbreJourProrata / this.getWorkTime().getProrataNbreJourTravaillees());
				
				addToOutputtext("\n" + "Salaire net é trouver de l'agent proraté au nombre de jours de travail = " + this.salaireNetATrouver);
				// addToOutputtext("\n"+">>>>>>>>>>>>>>>>>> a:" + a);
				// w_res1 := 0;
				// w_res2 := 0;
				salaireIterPaire = 0;
				salaireIterImpaire = 0;
				//
				// IF rubnet_exist THEN
				// SELECT SUM(NVL(mont,0)) -- Sommation des rubriques a
				// INTO A_ajouter_au_net -- ajouter au net
				// FROM parubnet
				// WHERE NVL(ajnu,0) <= w_numcal
				// AND sessionid = w_sessionid;
				//
				// Net_a_trouver := Net_a_trouver + NVL(A_ajouter_au_net,0);
				// END IF;
				double salaireAjouterAuNet = 0;
				double tauxAjustement = 0;
				if (this.getParameter().isRubriqueNetExist())
				{

					String queryAjouterAuNet = "select sum(mont) from ElementSalaireNet" + " where ajnu <= " + this.getParameter().getNumeroAjustementActuel() + " and sessionid = '" + this.getParameter().getSessionId() + "'";

					addToOutputtext("\n" + "Recherche du montant dans ElementSalaireNet é ajouter au net é trouver pour l'ajuster (sum(mont) de ElementSalaireNet) ");
					List l = service.find(queryAjouterAuNet);
					if (l != null && l.size() > 0 && l.get(0) != null)
					{
						salaireAjouterAuNet = ((BigDecimal) l.get(0)).doubleValue();
					}

					this.salaireNetATrouver += salaireAjouterAuNet;

					addToOutputtext("\n" + "Montant dans ElementSalaireNet é ajouter au net é trouver pour l'ajuster = " + salaireAjouterAuNet + " => Salaire Net é trouver = " + this.salaireNetATrouver);
				}
				//
				// ------------------------------------------------------------------
				// -- LH 191297
				// -- Les rubriques soumises a ajustement sont soumises a un
				// taux
				// -- Ce taux donne la difference entre le Net_a_trouver et le
				// -- salaire net stocke dans la fiche salarie.
				// ------------------------------------------------------------------
				// IF Net_a_trouver != wsal01.snet
				// THEN
				// Taux_de_reajustement := Net_a_trouver / wsal01.snet;
				//
				// IF Curs_taux_ajus%ISOPEN THEN
				// CLOSE Curs_taux_ajus;
				// END IF;
				// OPEN curs_taux_ajus;
				//
				// LOOP
				// FETCH curs_taux_ajus
				// INTO w_rowid, wrubajus.crub, wrubajus.ajnu, wrubajus.mont;
				// EXIT WHEN curs_taux_ajus%NOTFOUND;
				//
				// BEGIN
				// UPDATE parubajus
				// SET mont = ROUND( mont * Taux_de_reajustement, 0)
				// WHERE rowid = w_rowid;
				// EXCEPTION
				// WHEN OTHERS THEN NULL;
				// END;
				//
				// END LOOP;
				// CLOSE curs_taux_ajus;
				// END IF;
				if ((!ClsObjectUtil.isNull(this.getInfoSalary().getSnet())) && (this.salaireNetATrouver != this.getInfoSalary().getSnet().doubleValue()))
				{
					if (this.getInfoSalary().getSnet().doubleValue() != 0)
						tauxAjustement = this.salaireNetATrouver / this.getInfoSalary().getSnet().doubleValue();

					addToOutputtext("\n" + "Taux de réajustement du net é trouver (salaireNetATrouver/Salaire Fiche Sal) = " + tauxAjustement);
					//
					List l = service.find("from ElementSalaireAjus" + " where ajnu is not null " + " and ajnu = " + this.getParameter().getNumeroAjustementActuel() + " and sessionid = '"
							+ this.getParameter().getSessionId() + "'" + " and mont is not null ");
					//
					try
					{
						if (l.size() > 0)
						{

							addToOutputtext("\n"
									+ "Les valeurs des montants de ElementSalaireAjus doivent étre réajustées par rapport au taux de réajustement =>Multiplication des montants de la table par le taux de réajustemnet");
						}
						else
						{

							addToOutputtext("\n" + "Aucun montant é réajuster dans la table ElementSalaireAjus");
						}

						ElementSalaireAjus ajust = null;
						for (Object obj : l)
						{
							ajust = (ElementSalaireAjus) obj;
							service.updateFromTable("update ElementSalaireAjus set mont = mont *" + tauxAjustement + " where ajnu = " + ajust.getAjnu() + " and crub = '" + ajust.getCrub() + "'" + " and sessionid = "
									+ ajust.getSessionId());
							// ajust.setMont(new BigDecimal(ajust.getMont().doubleValue() * tauxAjustement));
							// service.update(ajust);
						}
					}
					catch (Exception e)
					{
						// logger
						e.printStackTrace();
					}
				}
			} // pour le test si c'est la paie au net
			// END IF;
			//
			// -----------------------------------------------------------------------------
			// -- LANCEMENT CALCUL DES RUBRIQUES
			// IF Mode_Test THEN pap_logins(' Lancement traitement des
			// rubriques'); END IF;
			// -----------------------------------------------------------------------------
			//
			// WHILE TRUE LOOP
			while (true)
			{
				//
				// A_ecarter_du_Net := 0;
				this.salaireAecarterDuNet = 0;
				// Net_calcule := 0;
				this.salaireNet = 0;
				// Salaire_brut := 0;
				this.salaireBrut = 0;
				// w_majo := 0;
				// majo_rest := 0;
				// rsa_tot := 0;
				//
				// ---------------------------------------------------------------
				// -- DESTRUCTION ENREGISTREMENTS DU FICHIER CALCUL SI PAIE AU
				// NET
				// -- ET ITERATION > 1
				// ---------------------------------------------------------------
				// IF w_paienet = 'O' -- LH 240198 AND nbiter > 1
				// THEN
				// sup_bulletin(wsal01.nmat,wsd_fcal1.nbul,w_aamm);
				// END IF;
				try
				{
					if ("O".equals(paieAuNet))
					{
						this.deleteBulletin();
					}
				}
				catch (Exception e)
				{
					// logger
					e.printStackTrace();
				}
				//
				// W_Faitout := corps(0); -- 0 = Lecture de toutes les rubriques
				// addToOutputtext("\n"+"processAllRubrique(0)");

				processAllRubrique(0, strDateFormat);

				//
				// -- LH 111298
				// IF stc AND rub_stc THEN
				// W_Faitout := RAZ_rubstc;
				// END IF;

				if (this.getParameter().isStc() && this.getParameter().isRubriqueStc())
				{

				}
				//
				// IF pb_calcul THEN
				// RETURN FALSE;
				// END IF;
				if (parameter.isPbWithCalulation())
				{

					addToOutputtext("\n" + "Pb with calculation, return");
					return;
				}
				//
				// IF w_paienet != 'O' THEN
				// EXIT;
				// END IF;
				// ParameterUtil.println(paieAuNet);
				if (!"O".equals(paieAuNet))
				{

					addToOutputtext("\n" + "break paieAuNet != O");
					break;
				}
				//
				// ----------------------------------------------------------------------------
				// -- PAIE AU NET : COMPARAISON PAIE AU NET
				// ----------------------------------------------------------------------------
				// w_calcul := 'N';
				// W_Faitout := comp_net;
				// -- LH IF w_calcul = 'O' THEN aff_net; ELSE EXIT; END IF;
				//
				// IF w_calcul = 'N' THEN EXIT; END IF;
				this.getParameter().setCalcul("N");
				// addToOutputtext("\n"+">> appel comparerAvecNet");

				this.comparerAvecNet();
				// addToOutputtext("\n"+">> fin comparerAvecNet");

				if ("N".equals(this.getParameter().getCalcul()))
				{

					addToOutputtext("\n" + "break getCalcul = N");
					//System.out.println("break getCalcul = N");
					break;
				}
			}
			// END LOOP;
			//
			// IF w_paienet != 'O' THEN
			// EXIT;
			// END IF;
			if (!"O".equals(paieAuNet))
			{

				addToOutputtext("\n" + "break paieAuNet != O");
				break;
			}
			//
			// IF NOT pb_calcul THEN
			// -- IF st_rub = 0 THEN
			// W_Faitout := corps(1); -- 1 = rubriques > rubrique du net
			// -- END IF;
			// END IF;
			// arret := TRUE;
			//
			// IF rubnet_exist THEN
			// SELECT COUNT(*) -- On relance le calcul du bulletin
			// INTO i -- Si il reste une rubrique dont
			// FROM parubnet -- le numero d'ajustement est
			// WHERE ajnu > w_numcal -- > au numero actuel
			// AND sessionid = w_sessionid;
			//
			// IF NOT PA_PAIE.NouZ(i) THEN
			// arret := FALSE;
			// END IF;
			// END IF;

			// addToOutputtext("\n"+"processAllRubrique(1)");
			if (!parameter.isPbWithCalulation())
			{
				processAllRubrique(1, strDateFormat);// -- 1 = rubriques > rubrique du net
			}
			arret = true;
			int i = 0;
			if (this.getParameter().isRubriqueNetExist())
			{
				String queryAjouterAuNet = "select count(*) from ElementSalaireNet" + " where ajnu > " + this.getParameter().getNumeroAjustementActuel() + " and sessionid = '" + this.getParameter().getSessionId() + "'";
				List l = service.find(queryAjouterAuNet);
				if (l != null && l.size() > 0)
				{
					i = ((Long) l.get(0)).intValue();
					if (i != 0)
						arret = false;
				}
			}
			//
			// IF arret
			// THEN
			// EXIT;
			// END IF;
			if (arret)
			{

				addToOutputtext("\n" + "break arret = N");
				//System.out.println("break arret = N");
				break;
			}
		}
		//
		// addToOutputtext("\n"+"----------------------");
		// ClsObjectUtil.displayClassProperties(this.getValeurRubriquePartage().getClass(),
		// this.getValeurRubriquePartage());
		// addToOutputtext("\n"+"----------------------");
		// ClsObjectUtil.displayClassProperties(this.getParameter().getClass(),
		// this.getParameter());
		// addToOutputtext("\n"+"----------------------");
		// ClsObjectUtil.displayClassProperties(this.getClass(), this);
		// addToOutputtext("\n"+"----------------------");
		// END LOOP;
		//
		// ------------------
		// COMMIT;
		// ------------------
		// RETURN TRUE;
		//
		// END trait_sal;
	}

	/**
	 * => chg_rubstc
	 * <p>
	 * Construire la liste des rubriques motif fin de contrat
	 * </p>
	 * Lecture de la table 023 'Motifs de fin de contrat'. Les libelles 2,3 et 4 contiennent les rbq a declencher pour le motif (XXXX+YYYY+ZZZZ+...) Les rbq
	 * sont chargees dans un tableau puis activees pour un calcul de STC
	 */
	public boolean chargerRubriqueMotifFinContrat(String motf)
	{
		if (ListOfRubriqueOfMotifFinContrat == null)
			ListOfRubriqueOfMotifFinContrat = new ArrayList<ClsRubriqueClone>();
		// addToOutputtext("\n"+">>chargerRubriqueMotifFinContrat");
		// addToOutputtext("\n"+"motif fin:" + motf);
		// w_rub CHAR(4);
		// i NUMBER(5);
		// j NUMBER(5);
		// w_chaine VARCHAR2(200);
		//
		// BEGIN
		// wfnom.lib2 := null;
		// wfnom.lib3 := null;
		// wfnom.lib4 := null;
		//
		// -- Lecture des rubriques a declencher
		//
		// wfnom.lib2 := paf_lecfnomL( 23, motf,
		// 2,w_aamm,w_nlot,wsd_fcal1.nbul);
		String libelle2 = utilNomenclature.getLabelFromNomenclature(infoSalary.getComp_id().getCdos(), 23, motf, 2, nlot, nbul, monthOfPay, periodOfPay);
		// IF wfnom.lib2 IS NULL THEN
		// RETURN FALSE;
		// END IF;
		if (libelle2 == null || libelle2 == "")
			return false;
		//
		// wfnom.lib3 := paf_lecfnomL( 23, motf,
		// 3,w_aamm,w_nlot,wsd_fcal1.nbul);
		String libelle3 = utilNomenclature.getLabelFromNomenclature(infoSalary.getComp_id().getCdos(), 23, motf, 3, nlot, nbul, monthOfPay, periodOfPay);
		// if (libelle3 == null || libelle3 == "")
		// return false;

		// wfnom.lib4 := paf_lecfnomL(23, motf, 4,w_aamm,w_nlot,wsd_fcal1.nbul);
		String libelle4 = utilNomenclature.getLabelFromNomenclature(infoSalary.getComp_id().getCdos(), 23, motf, 4, nlot, nbul, monthOfPay, periodOfPay);
		// if (libelle4 == null || libelle4 == "")
		// return false;
		//
		// w_chaine := ' ';
		// IF NOT PA_PAIE.NouB(wfnom.lib2) THEN
		// w_chaine := wfnom.lib2;
		// END IF;
		// IF NOT PA_PAIE.NouB(wfnom.lib3) THEN
		// w_chaine := w_chaine || wfnom.lib3;
		// END IF;
		// IF NOT PA_PAIE.NouB(wfnom.lib4) THEN
		// w_chaine := w_chaine || wfnom.lib4;
		// END IF;
		// w_chaine := LTRIM(w_chaine);
		//
		// -- Chargement des rubriques dans le tableau
		// IF PA_PAIE.NouB(w_chaine) THEN
		// RETURN FALSE;
		// ELSE
		// i := 1;
		// nb_tab_stc := 0;
		// WHILE i <= LENGTH(w_chaine) LOOP
		// IF NOT PA_PAIE.NouB(SUBSTR(w_chaine,i,4)) THEN
		// nb_tab_stc := nb_tab_stc + 1;
		// tab_rub_stc(nb_tab_stc) := SUBSTR(w_chaine,i,4);
		// i := i + 5;
		// ELSE
		// EXIT;
		// END IF;
		// END LOOP;
		// END IF;
		String arrayRubrique = libelle2;
		if (StringUtils.isNotBlank(libelle3))
			arrayRubrique += libelle3;
		if (StringUtils.isNotBlank(libelle4))
			arrayRubrique += libelle4;
		List<String> liste = this._loadListOfRubriqueOfMotifFinContrat(arrayRubrique);

		ClsRubriqueClone rubriqueClone = null;
		ElementSalaire myRubrique = null;
		
		for (String string : liste)
		{
			myRubrique = elementSalaireRepository.findByCodeRubriqueExactly(string);
			ClsParubqClone myRubriqueClone = null;
			if (myRubrique != null)
			{
				myRubriqueClone = new ClsParubqClone(myRubrique);
				rubriqueClone = new ClsRubriqueClone(this);
				rubriqueClone.setRubrique(myRubriqueClone);
				ListOfRubriqueOfMotifFinContrat.add(rubriqueClone);
			}
		}
		return true;
	}

	private List<String> _loadListOfRubriqueOfMotifFinContrat(String arrayRubrique)
	{
		List<String> liste = new ArrayList<String>();
		if (StringUtils.isNotBlank(arrayRubrique))
		{
			int i = 0;
			while (i < arrayRubrique.length())
			{
				if (StringUtils.isNotBlank(StringUtils.substring(arrayRubrique, i, i + ParameterUtil.longueurRubrique)))
				{
					liste.add(StringUtils.substring(arrayRubrique, i, i + ParameterUtil.longueurRubrique));
					i = i + ParameterUtil.longueurRubrique +1;
				}
			}
		}
		return liste;
	}

	/**
	 * TEST SI SALARIE EXPATRIE
	 * <p>
	 * typ_rec = Zone de pasa01 a tester : 1=Regime, 2=Type de contrat, 3=Classe salarie
	 * </p>
	 * <p>
	 * val_exp = Valeur de la zone pour un expatrie
	 * </p>
	 * 
	 * @return true si c'est un expatrié ou false dans le cas contraire
	 */
	public boolean _isExpatrie()
	{
		int typeContratExpatrie = parameter.getExpatrieTypeContrat();
		String valeurExpatrie = parameter.getExpatrieValeur();
		// Expatrie := FALSE;
		// IF typ_rec = 1 AND wsal01.regi = val_exp THEN Expatrie := TRUE; END
		// IF;
		if (typeContratExpatrie == 1 && valeurExpatrie.equals(this.infoSalary.getRegi()))
			return true;
		// IF typ_rec = 2 AND wsal01.typc = val_exp THEN Expatrie := TRUE; END
		// IF;
		if (typeContratExpatrie == 2 && valeurExpatrie.equals(this.infoSalary.getTypc()))
			return true;
		// IF typ_rec = 3 AND wsal01.clas = val_exp THEN Expatrie := TRUE; END
		// IF;
		if (typeContratExpatrie == 3 && valeurExpatrie.equals(this.infoSalary.getClas()))
			return true;
		return false;
	}

	/**
	 * calculate anciennete of salary
	 */
	public int calculateAnciennete(String strDateFormat) throws Exception
	{
		// addToOutputtext("\n"+">>calculateAnciennete");
		if (this.infoSalary.getDdca() == null)
			return 0;
		ClsDate myDdca = new ClsDate(this.infoSalary.getDdca());
		int nbreYear = 0;
		// -- CALCUL ANCIENNETE
		// IF Mode_Test THEN pap_logins(' Calcul anciennete'); END IF;
		// ------------------------------------------------------------------------------
		// IF Ste_SGMB AND TO_CHAR(wsal01.ddca,'DD') <> '01'
		// THEN
		if (entreprise.equals(ClsEnumeration.EnEnterprise.SGMB) && myDdca.getDay() != 1)
			// wsal01.ddca := add_months( wsal01.ddca , 1 );
			this.infoSalary.setDdca(myDdca.clone().addMonth(1));
		// END IF;
		//
		// x_an := TO_NUMBER(SUBSTR(w_aamm,1,4));
		int nyear = myMonthOfPay.getYear();
		// x_mois := TO_NUMBER(SUBSTR(w_aamm,5,2));
		// int nmois = myMonthOfPay.getMonth();
		//
		// -- MM Modif DELTACI-010302-94 Controle de coherence sur la date
		// anciennete
		// IF x_an - TO_NUMBER(TO_CHAR(wsal01.ddca,'YYYY')) > 65 THEN
		// pb_calcul := TRUE;
		// err_msg := PA_PAIE.erreurp('ERR-90045',w_clang,wsal01.nmat);
		// RETURN FALSE;
		// END IF;
		if ((nyear - myDdca.getYear()) > this.getParameter().AGE_MAX_OF_SALARY)
		{
			parameter.setError(parameter.errorMessage("ERR-90045", parameter.getLangue(), infoSalary.getComp_id().getNmat()));

			addToOutputtext("\n" + parameter.getError());
			getParameter().setPbWithCalulation(true);
			// return false;
		}
		// -- Fin modif MM.
		//
		// -- MM 03-2003 Ajout fonction calcul anciennete avec deduction des
		// arrets paie
		// nbreYear :=
		// pa_paie.calc_anc(wsal01.identreprise,wsal01.ddca,wsal01.nmat,w_aamm);
		nbreYear = calculateAnciennetePre(strDateFormat);

		// -- Fin modif MM.
		//
		// IF nbreYear < 0 THEN
		// -- LH 160700 nbreYear := 0;
		// ancien := 0;
		// ancien1 := 0;
		// pb_calcul := TRUE;
		// err_msg := PA_PAIE.erreurp('ERR-90046',w_clang,wsal01.nmat);
		// RETURN FALSE;
		if (nbreYear < 0)
		{
			parameter.setError(parameter.errorMessage("ERR-90046", parameter.getLangue(), infoSalary.getComp_id().getNmat()));

			addToOutputtext("\n" + parameter.getError());
			getParameter().setPbWithCalulation(true);
			// return false;
		}
		// END IF;
		//
		// IF nbreYear > 65 THEN
		// pb_calcul := TRUE;
		// err_msg := PA_PAIE.erreurp('ERR-90045',w_clang,wsal01.nmat);
		// RETURN FALSE;
		// END IF;
		if (nbreYear > parameter.AGE_MAX_OF_SALARY)
		{
			parameter.setError(parameter.errorMessage("ERR-90045", parameter.getLangue(), infoSalary.getComp_id().getNmat()));

			addToOutputtext("\n" + parameter.getError());
			getParameter().setPbWithCalulation(true);
			// return false;
		}
		//
		// ancien := nbreYear;
		// ancien1 := nbreYear;
		//
		// IF ancien < 0 THEN
		// ancien := 0;
		// ancien1 := 0;
		// pb_calcul := TRUE;
		// err_msg := PA_PAIE.erreurp('ERR-90046',w_clang,wsal01.nmat);
		// RETURN FALSE;
		// ELSIF ancien > w_ancmax THEN
		if (nbreYear > parameter.getAncienneteMaxi())
			nbreYear = parameter.getAncienneteMaxi();
//			this.setAnciennete();
//		else
			this.setAnciennete(nbreYear);
		// ancien := w_ancmax;
		// ancien1 := w_ancmax;
		// END IF;
		return nbreYear;
	}

	public int calculateAnciennetePre(String strDateFormat)
	{
		// if('O' == this.getParameter().getGenfile()) addToOutputtext("\n"+">>calculateAnciennetePre");
		// w_mois_deduct NUMBER;
		int nMonthDeducted = -1;
		// nDayDeduted NUMBER;
		int nDayDeduted = -1;
		//
		//
		// w_ddar DATE;
		Date dateDebutArriereOfPay = new Date();
		// w_dfar DATE;
		Date dateFinArriereOfPay = new Date();
		//
		// x_an NUMBER(5);
		int year = -1;
		// x_mois NUMBER(5);
		int mois = -1;
		// wmois NUMBER(5);
		int wmois = -1;
		// nbreYear NUMBER(5);
		int nbreYear = -1;
		//		   

		//
		// CURSOR C_ARRPAIE IS
		// SELECT ddar,dfar from paarrpai
		// WHERE identreprise = cdos_i
		// AND mtar IN (Select cacc from pafnom where identreprise = cdos_i and ctab =21
		// and nume = 1 and valm =1)
		// AND nmat = mat_i
		// AND ddar < TO_date(aamm_i,'YYYYMM')
		// ORDER BY ddar;
		//		   
		// x_an := 0;
		year = 0;
		// mois := 0;
		mois = 0;
		// wmois := 0;
		wmois = 0;
		// nbreYear := 0;
		nbreYear = 0;

		// ------------------------------------------------------------------------------
		// -- CALCUL ANCIENNETE
		// ------------------------------------------------------------------------------
		//
		// --- 1 - Calcul du nombre de mois arret de paie a deduire du calcul
		// anciennete

		nDayDeduted = 0;
		ClsDate myMonthOfPay = new ClsDate(monthOfPay, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
		Date monthOfPayDate = myMonthOfPay.getDate();
		String queryString = "select ddar, dfar from Rhtarrpaiagent" + " where identreprise ='" + this.parameter.getDossier() + "'" + " and mtar in (" + " select cacc from Rhfnom" + " where identreprise ='"
				+ this.parameter.getDossier() + "'" + " and ctab = 21" + " and nume = 1 and valm =1)" + " and nmat ='" + this.infoSalary.getComp_id().getNmat() + "'" +
				// " and ddar < '" + myMonthOfPay.getDateS("dd-MM-yyyy")
				// + "'" +
				" and ddar < '" + myMonthOfPay.getDateS(strDateFormat) + "'" + " order by ddar";
		//
		// --- 1 - Calcul du nombre de mois arret de paie a deduire du calcul
		// anciennete
		//
		// nDayDeduted := 0;
		//
		// OPEN C_ARRPAIE;
		// LOOP
		// FETCH C_ARRPAIE INTO dateDebutAr,dateFinAr;
		// EXIT WHEN C_ARRPAIE%NOTFOUND;
		//
		// IF dateFinAr >= TO_date(aamm_i,'YYYYMM') THEN
		// nDayDeduted := nDayDeduted + (TO_date(aamm_i,'YYYYMM') -
		// dateDebutAr);
		// ELSE
		// nDayDeduted := nDayDeduted + (dateFinAr - dateDebutAr);
		// END IF;
		// END LOOP;
		// CLOSE C_ARRPAIE;
		//
		// nMonthDeducted := FLOOR(nDayDeduted/ 30);
		//
		//
		// x_an := TO_NUMBER(SUBSTR(aamm_i,1,4));
		// mois := TO_NUMBER(SUBSTR(aamm_i,5,2));
		// wmois := x_an - TO_NUMBER(TO_CHAR(ddca_i,'YYYY'));
		// wmois := (wmois * 12) + mois - NVL(nMonthDeducted,0);
		// wmois := wmois - TO_NUMBER(TO_CHAR(ddca_i,'mm'));
		// nbreYear := FLOOR(wmois / 12);
		//
		// IF nbreYear < 0 THEN
		// nbreYear := 0;
		// END IF;
		//
		//
		// RETURN (nbreYear);
		//
		List listOfArriere = service.find(queryString);
		//
		long l_nDayDeduted = 0;
		// long i_nDayDeduted = 0;
		Object[] rowOfArr = null;
		long millisecondperday = 86400000;
		for (Object object : listOfArriere)
		{
			rowOfArr = (Object[]) object;
			// dateDebutArriereOfPay = new ClsDate(String.valueOf(rowOfArr[0]),
			// this.getParameter().FORMAT_DATE).getDate() ;
			// dateFinArriereOfPay = new ClsDate(String.valueOf(rowOfArr[1]),
			// this.getParameter().FORMAT_DATE).getDate() ;
			//

			addToOutputtext("\n" + ">>Date = " + String.valueOf(rowOfArr[0]) + " " + this.getParameter().getAppDateFormat());
			dateDebutArriereOfPay = (Date) rowOfArr[0];
			dateFinArriereOfPay = (Date) rowOfArr[1];

			if (dateFinArriereOfPay.compareTo(monthOfPayDate) >= 0)
			{
				l_nDayDeduted = (monthOfPayDate.getTime() - dateDebutArriereOfPay.getTime()) / millisecondperday;
			}
			else
			{
				// nDayDeduted := nDayDeduted + (dateFinAr - dateDebutAr);
				l_nDayDeduted = (dateFinArriereOfPay.getTime() - dateDebutArriereOfPay.getTime()) / millisecondperday;
			}
			// récupération de la valeur <nDayDeduted> calculée
			// i_nDayDeduted = l_nDayDeduted;
			nDayDeduted += l_nDayDeduted;
		}
		//
		// nMonthDeducted := FLOOR(nDayDeduted/ 30);
		nMonthDeducted = nDayDeduted / 30;
		//
		//
		ClsDate myDdca = new ClsDate(this.infoSalary.getDdca());
		// x_an := TO_NUMBER(SUBSTR(aamm_i,1,4));
		year = myMonthOfPay.getYear();
		// mois := TO_NUMBER(SUBSTR(aamm_i,5,2));
		mois = myMonthOfPay.getMonth();
		// wmois := x_an - TO_NUMBER(TO_CHAR(ddca_i,'YYYY'));
		wmois = year - myDdca.getYear();
		// wmois := (wmois * 12) + mois - NVL(nMonthDeducted,0);
		wmois = (wmois * 12) + mois - nMonthDeducted;
		// wmois := wmois - TO_NUMBER(TO_CHAR(ddca_i,'mm'));
		wmois = wmois - myDdca.getMonth();
		// nbreYear := FLOOR(wmois / 12);
		nbreYear = wmois / 12;
		//
		// IF nbreYear < 0 THEN
		// nbreYear := 0;
		// END IF;
		if (nbreYear < 0)
			nbreYear = 0;
		//
		// RETURN (nbreYear);
		return nbreYear;
		//
	}

	/**
	 * Calcul de l'age de l'agent
	 * 
	 * @return l'age
	 */
	public int calculateAgentAge()
	{
		// -- CALCUL Age de l'agent
		Date dtLastDayOfMonthPay = myMonthOfPay.getLastDayOfMonth();
		int ecart = ClsDate.getMonthsBetween(infoSalary.getDtna(), dtLastDayOfMonthPay);
		int nombreYears = ecart / 12;
		//
		// -- MM Modif DELTACI-010302-94 Controle de coherence sur la date
		// naissance
		// IF FLOOR( ( dtLastDayOfMonthPay - wsal01.dtna ) / 365 ) > 100 THEN
		// pb_calcul := TRUE;
		// err_msg := PA_PAIE.erreurp('ERR-90047',w_clang,wsal01.nmat);
		// RETURN FALSE;
		// END IF;
		//
		// nbr_an := FLOOR( ( dtLastDayOfMonthPay - wsal01.dtna ) / 365 );
		// IF nbr_an > 100 THEN
		// pb_calcul := TRUE;
		// err_msg := PA_PAIE.erreurp('ERR-90047',w_clang,wsal01.nmat);
		// RETURN FALSE;
		// END IF;
		if (nombreYears > 100)
		{
			parameter.setError(parameter.errorMessage("ERR-90047", parameter.getLangue(), infoSalary.getComp_id().getNmat()));

			addToOutputtext("\n" + parameter.getError());
			getParameter().setPbWithCalulation(true);
			return -1;
		}
		// IF nbr_an < 0 THEN
		// pb_calcul := TRUE;
		// err_msg := PA_PAIE.erreurp('ERR-90048',w_clang,wsal01.nmat);
		// RETURN FALSE;
		// END IF;
		if (nombreYears < 0)
		{
			parameter.setError(parameter.errorMessage("ERR-90048", parameter.getLangue(), infoSalary.getComp_id().getNmat()));

			addToOutputtext("\n" + parameter.getError());
			getParameter().setPbWithCalulation(true);
			return -1;
		}
		// Age_Agent := nbr_an;
		this.ageOfAgent = nombreYears;
		return this.ageOfAgent;
	}

	/**
	 * update the amount of rubriques under adjustment => charg_ajus
	 */
	private void chargementMontantDesRubriquesAdjustment()
	{
		// if('O' == this.getParameter().getGenfile()) addToOutputtext("\n"+">>chargementMontantDesRubriquesAdjustment");
		// montant PAELFIX.MONP%TYPE;
		double montant = 0;

		List listOfCount = null;

		int count = 0;
		//
		// wajus parubajus%ROWTYPE;
		//
		// CURSOR C_AJUS IS
		// SELECT * FROM parubajus
		// WHERE sessionid = w_sessionid;
		String queryString = "from ElementSalaireAjus" + " where sessionid =" + parameter.getSessionId();
		//
		//
		// BEGIN
		// -- RAZ des montants des tables
		//
		// UPDATE parubajus SET mont = 0
		// WHERE sessionid = w_sessionid;
		try
		{
			service.updateFromTable("update ElementSalaireAjus set mont = 0 where sessionid = " + parameter.getSessionId());
			// prendre la liste des ajustements
			List listOfRubajus = service.find(queryString);
			//
			// -- Lecture des montants,en E.F., pour les rubriques soumises a
			// ajustement
			// IF retroactif
			// THEN
			if (parameter.isUseRetroactif())
			{
				// OPEN C_AJUS;
				// LOOP
				for (Object parubajus : listOfRubajus)
				{
					// FETCH C_AJUS INTO wajus;
					// EXIT WHEN C_AJUS%NOTFOUND;
					//
					// montant = this.utilNomenclature.getAmountOrRateFromSalaryEventData(parameter.getDossier(), infoSalary.getComp_id().getNmat(), nlot,
					// ClsEnumeration.EnColumnToRead.AMOUNT);

					montant = (tempNumber = this.utilNomenclature.getAmountOrRateFromSalaryEventData(parameter.getDossier(), infoSalary.getComp_id().getNmat(), nlot, "R" + ((ElementSalaireAjus) parubajus).getCrub())) == null ? 0
							: tempNumber.doubleValue();

					// -- test si donnee evenement
					// IF
					// paf_EvenSalmnt(w_nlot,'R'||wajus.crub,PA_CALCUL.wsal01.nmat,montant)
					// THEN
					// if('O' == this.getParameter().getGenfile()) addToOutputtext("\n"+"montant :" + montant);
					if (montant > 0)
					{
						// UPDATE parubajus
						// SET mont = montant
						// WHERE EXISTS ( SELECT codp
						// FROM pahelfix
						// WHERE identreprise = wpdos.identreprise
						// AND nmat = wsal01.nmat
						// AND codp = wajus.crub
						// AND aamm = w_aamm
						// AND nbul = wsd_fcal1.nbul
						// )
						// AND sessionid = w_sessionid;
						String queryStringforupdate = "update ElementSalaireAjus set mont = " + montant + " where exists(" + " select codp from Rhthelfix" + " where identreprise = '" + parameter.getDossier() + "'"
								+ " and nmat = '" + infoSalary.getComp_id().getNmat() + "'" + " and codp = '" + ((ElementSalaireAjus) parubajus).getCrub() + "'" + " and aamm = '"
								+ myMonthOfPay.getYearAndMonth() + "'" + " and nbul = " + this.getNbul() + " )" + " and sessionid = " + parameter.getSessionId();

						this.addToOutputtext("\n Deuxieme Requete de mise é jour de la table ElementSalaireNet " + queryStringforupdate);

						session = service.getSession();
						session.createSQLQuery(queryStringforupdate).executeUpdate();
						service.closeConnexion(session);
					}
					// ELSE
					else
					{
						//
						// UPDATE parubajus
						// SET mont = ( SELECT NVL( monp, 0)
						// FROM pahelfix
						// -- LH 240198 C'EST LA !!! ET J'AI CHERCHE PENDANT DES
						// HEURES ....
						// -- WHERE identreprise = PA_CALCUL.wpdos.identreprise
						// WHERE identreprise = wpdos.identreprise
						// AND nmat = wsal01.nmat
						// AND codp = wajus.crub
						// AND aamm = w_aamm
						// AND nbul = wsd_fcal1.nbul)
						// WHERE EXISTS ( SELECT codp
						// FROM pahelfix
						// WHERE identreprise = wpdos.identreprise
						// AND nmat = wsal01.nmat
						// AND codp = wajus.crub
						// AND aamm = w_aamm
						// AND nbul = wsd_fcal1.nbul
						// )
						// AND sessionid = w_sessionid;
//						Rhthelfix elfix = (Rhthelfix) service.get(Rhthelfix.class, new RhthelfixPK(parameter.getDossier(), infoSalary.getComp_id().getNmat(), ((ElementSalaireAjus) parubajus).getCrub(), myMonthOfPay
//								.getYearAndMonth(), this.getNbul()));
//						double mont = 0;
//						if (elfix != null && elfix.getMonp().doubleValue() > 0)
//							mont = elfix.getMonp().doubleValue();
//						service.updateFromTable("update ElementSalaireAjus set mont = " + mont + " where exists(" + " select codp from Rhthelfix" + " where identreprise = '" + parameter.getDossier() + "'" + " and nmat = '"
//								+ infoSalary.getComp_id().getNmat() + "'" + " and codp = '" + ((ElementSalaireAjus) parubajus).getCrub() + "'" + " and aamm = '" + myMonthOfPay.getYearAndMonth() + "'" + " and nbul = "
//								+ this.getNbul() + " )" + " and sessionid = " + parameter.getSessionId());
						//
						// END IF;
					}
					// END LOOP;
				}
				// CLOSE C_AJUS;
				//
				//
				// SELECT COUNT(*) INTO nb_evar FROM pahevar
				// WHERE identreprise = wpdos.identreprise
				// AND nmat = wsal01.nmat
				// AND aamm = w_aamm
				// AND nbul = wsd_fcal1.nbul;
				listOfCount = service.find("select count(*) from Rhthevar" + " where identreprise ='" + parameter.getDossier() + "'" + " and nmat = '" + infoSalary.getComp_id().getNmat() + "'" + " and aamm = '"
						+ myMonthOfPay.getYearAndMonth() + "'" + " and nbul = " + this.getNbul());

				if (listOfCount != null && listOfCount.size() > 0)
				{
					count = ((Long) listOfCount.get(0)).intValue();
				}
				//
				// IF NOT PA_PAIE.NouZ(nb_evar) THEN
				//
				// -- Lecture des montants, en E.V., pour les rubriques soumises
				// a ajustement
				//
				// -- LH 051297 On peut avoir un EV en double
				if (count > 0)
				{
					// UPDATE parubajus
					// SET mont = ( SELECT SUM( mont ) FROM pahevar
					// WHERE identreprise = wpdos.identreprise
					// AND nmat = wsal01.nmat
					// AND aamm = w_aamm
					// AND nbul = wsd_fcal1.nbul
					// AND rubq = parubajus.crub )
					//
					// WHERE EXISTS ( SELECT rubq
					// FROM pahevar
					// WHERE identreprise = wpdos.identreprise
					// AND nmat = wsal01.nmat
					// AND aamm = w_aamm
					// AND nbul = wsd_fcal1.nbul
					// AND rubq = parubajus.crub )
					// AND sessionid = w_sessionid;
					String queryStringforupdate = "update ElementSalaireAjus a " + "set mont = (select sum( mont ) from Rhthevar" + " where identreprise ='" + parameter.getDossier() + "' and nmat = '"
							+ infoSalary.getComp_id().getNmat() + "'" + " and rubq = a.crub " + " and aamm = '" + myMonthOfPay.getYearAndMonth() + "'" + " and nbul = " + this.getNbul() + " )" + " where exists("
							+ " select rubq from Rhthevar" + " where identreprise = '" + parameter.getDossier() + "'" + " and nmat = '" + infoSalary.getComp_id().getNmat() + "'" + " and rubq = a.crub " + " and aamm = '"
							+ myMonthOfPay.getYearAndMonth() + "'" + " and nbul = " + this.getNbul() + " )" + " and sessionid = " + parameter.getSessionId();

					this.addToOutputtext("\n Premiére Requete de mise é jour de la table ElementSalaireAjus " + queryStringforupdate);

					session = service.getSession();
					session.createSQLQuery(queryStringforupdate).executeUpdate();
					service.closeSession(session);
					// END IF;
				}
			}
			//
			// ELSE
			else
			{
				double mont = 0;
				String strQueryUpdate = StringUtils.EMPTY;
				for (String rubAAjuster : parameter.listeRubriquesAAjuster)
				{
					mont = 0;
					String strLib=" select c.monp from ElementFixeSalaire c where  c.identreprise ='"+parameter.getDossier()+"' and c.nmat = '"+infoSalary.getComp_id().getNmat()+"' and c.codp = '"+rubAAjuster+"'";
					strLib+=" and ( c.ddeb is null or (c.ddeb is not null and c.ddeb <= '" + new ClsDate(myMonthOfPay.getLastDayOfMonth()).getDateS(parameter.appDateFormat) + "'))";
					strLib+=" and ( c.dfin is null or (c.dfin is not null and c.dfin >= '" + new ClsDate(myMonthOfPay.getFirstDayOfMonth()).getDateS(parameter.appDateFormat) + "'))";
					List<Object> lst = service.find(strLib);
					if(! lst.isEmpty() && lst.get(0) != null)
						mont = new BigDecimal(lst.get(0).toString()).doubleValue();
					
					strQueryUpdate = "update ElementSalaireAjus set mont = " + mont + " where crub = '" + rubAAjuster + "' and sessionid = "+ parameter.getSessionId();
					if(mont != 0)
					service.updateFromTable(strQueryUpdate);
				}
				
				listOfCount = service.find("select count(*) from ElementVariableDetailMois" + " where identreprise ='" + parameter.getDossier() + "'" + " and nmat = '" + infoSalary.getComp_id().getNmat() + "'" + " and aamm = '"
						+ myMonthOfPay.getYearAndMonth() + "'" + " and nbul = " + this.getNbul());
				count = 0;
				if (listOfCount != null && listOfCount.size() > 0)
				{
					count = ((Long) listOfCount.get(0)).intValue();
				}
				if (count > 0)
				{
					for (String rubAAjuster : parameter.listeRubriquesAAjuster)
					{
						mont = 0;
						String strLib=" select sum( mont ) from ElementVariableDetailMois c where  c.identreprise ='"+parameter.getDossier()+"' and c.nmat = '"+infoSalary.getComp_id().getNmat()+"' and c.rubq = '"+rubAAjuster+"'";
						strLib+=" and nbul = "+this.getNbul()+" and aamm = '" + myMonthOfPay.getYearAndMonth() + "'" ;
						
						List<Object> lst = service.find(strLib);
						if(! lst.isEmpty() && lst.get(0) != null)
							mont = new BigDecimal(lst.get(0).toString()).doubleValue();
						
						strQueryUpdate = "update ElementSalaireAjus set mont = " + mont + " where crub = '" + rubAAjuster + "' and sessionid = "+ parameter.getSessionId();
						if(mont != 0)
						service.updateFromTable(strQueryUpdate);
					}	
				}
			}

			String strQuery = "update ElementSalaireAjus set mont = 0" + " where sessionid = " + parameter.getSessionId();
			service.updateFromTable(strQuery);
//			session = service.getSession();
//
//			session.createSQLQuery(strQuery).executeUpdate();
//
//			service.closeConnexion(session);
			//
			// RETURN TRUE;
		}
		catch (SQLException e)
		{
			// logger
			e.printStackTrace();
		}
	}

	/**
	 * => charg_net update the amount of rubriques under adjustment
	 */
	private void chargementMontantDesRubriquesNettes()
	{
		// if('O' == this.getParameter().getGenfile()) addToOutputtext("\n"+">>chargementMontantDesRubriquesNettes");
		String queryString = "from ElementSalaireNet" + " where sessionid =" + parameter.getSessionId();
		double montant = 0;
		int count = 0;
		List listOfCount = null;
		// -- RAZ des montants des tables
		// UPDATE parubnet SET mont = 0
		// WHERE sessionid = w_sessionid;
		try
		{
			service.updateFromTable("update ElementSalaireNet set mont = 0" + " where sessionid = " + parameter.getSessionId());
			//
			// -- Lecture des montants, en E.F., pour les rubriques saisies en
			// net
			//
			if (parameter.isUseRetroactif())
			{
				// IF retroactif THEN
				// OPEN C_RUBNET;
				List listOfRubnet = service.find(queryString);
				// LOOP
				for (Object parubnet : listOfRubnet)
				{
					// FETCH C_RUBNET INTO wnet;
					// EXIT WHEN C_RUBNET%NOTFOUND;
					//
					// -- test si donnee evenement
					// IF
					// paf_EvenSalmnt(w_nlot,'R'||wnet.crub,PA_CALCUL.wsal01.nmat,w_val_mnt)
					// THEN
					// montant = this.utilNomenclature.getAmountOrRateFromSalaryEventData(parameter.getDossier(), infoSalary.getComp_id().getNmat(), nlot,
					// ClsEnumeration.EnColumnToRead.AMOUNT);
					montant = (tempNumber = this.utilNomenclature.getAmountOrRateFromSalaryEventData(parameter.getDossier(), infoSalary.getComp_id().getNmat(), nlot, "R" + ((ElementSalaireNet) parubnet).getCrub())) == null ? 0
							: tempNumber.doubleValue();
					//
					if (montant > 0)
					{
						// UPDATE parubnet
						// SET mont = w_val_mnt
						// WHERE EXISTS ( SELECT NVL( codp, 0)
						// FROM pahelfix
						// WHERE identreprise = wpdos.identreprise
						// AND nmat = wsal01.nmat
						// AND codp = wnet.crub
						// AND aamm = w_aamm
						// AND nbul = wsd_fcal1.nbul
						// )
						// AND sessionid = w_sessionid;
						service.updateFromTable("update ElementSalaireNet set mont = " + montant + " where exists(" + " select * from Rhthelfix" + " where identreprise = '" + parameter.getDossier() + "'" + " and nmat = '"
								+ infoSalary.getComp_id().getNmat() + "'" + " and codp = '" + ((ElementSalaireNet) parubnet).getCrub() + "'" + " and aamm = '" + myMonthOfPay.getYearAndMonth() + "'" + " and nbul = "
								+ this.getNbul() + " )" + " and sessionid = " + parameter.getSessionId());
					}
					// ELSE
					else
					{
						// UPDATE parubnet
						// SET mont = ( SELECT NVL(monp, 0)
						// FROM pahelfix
						// WHERE identreprise = wpdos.identreprise
						// AND nmat = wsal01.nmat
						// AND codp = wnet.crub )
						// WHERE EXISTS ( SELECT NVL( codp, 0)
						// FROM pahelfix
						// WHERE identreprise = wpdos.identreprise
						// AND nmat = wsal01.nmat
						// AND codp = wnet.crub
						// AND aamm = w_aamm
						// AND nbul = wsd_fcal1.nbul
						// )
						// AND sessionid = w_sessionid;
//						Rhthelfix elfix = (Rhthelfix) service.get(Rhthelfix.class, new RhthelfixPK(parameter.getDossier(), infoSalary.getComp_id().getNmat(), ((ElementSalaireNet) parubnet).getCrub(), myMonthOfPay
//								.getYearAndMonth(), this.getNbul()));
//						double mont = 0;
//						if (elfix != null && elfix.getMonp().doubleValue() > 0)
//							mont = elfix.getMonp().doubleValue();
//						service.updateFromTable("update ElementSalaireNet set mont = " + mont + " where exists(" + " select * from Rhthelfix" + " where identreprise = '" + parameter.getDossier() + "'" + " and nmat = '"
//								+ infoSalary.getComp_id().getNmat() + "'" + " and codp = '" + ((ElementSalaireNet) parubnet).getCrub() + "'" + " and aamm = '" + myMonthOfPay.getYearAndMonth() + "'" + " and nbul = "
//								+ this.getNbul() + " )" + " and sessionid = " + parameter.getSessionId());
					}
					// END IF;
					//
					// END LOOP;
					// CLOSE C_RUBNET;

				}
				//
				// SELECT COUNT(*) INTO nb_evar FROM pahevar
				// WHERE identreprise = wpdos.identreprise
				// AND nmat = wsal01.nmat
				// AND aamm = w_aamm
				// AND nbul = wsd_fcal1.nbul;
				listOfCount = service.find("select count(*) from Rhthevar" + " where identreprise ='" + parameter.getDossier() + "'" + " and nmat = '" + infoSalary.getComp_id().getNmat() + "'" + " and aamm = '"
						+ myMonthOfPay.getYearAndMonth() + "'" + " and nbul = " + this.getNbul());
				count = 0;
				if (listOfCount != null && listOfCount.size() > 0)
				{
					count = ((Long) listOfCount.get(0)).intValue();
				}
				//
				// IF NOT PA_PAIE.NouZ(nb_evar)
				// THEN
				if (count > 0)
				{
					// -- Lecture des montants, en E.V., pour les rubriques
					// saisies en net
					// UPDATE parubnet
					// -- LH 051297 On peut avoir un EV en double
					// -- SET mont = ( SELECT mont FROM paevar
					// SET mont = ( SELECT SUM( mont) FROM pahevar
					// WHERE identreprise = wpdos.identreprise
					// AND nmat = wsal01.nmat
					// AND aamm = w_aamm
					// AND nbul = wsd_fcal1.nbul
					// AND rubq = parubnet.crub )
					// WHERE EXISTS ( SELECT rubq FROM pahevar
					// WHERE identreprise = wpdos.identreprise
					// AND nmat = wsal01.nmat
					// AND aamm = w_aamm
					// AND nbul = wsd_fcal1.nbul
					// AND rubq = parubnet.crub)
					// AND sessionid = w_sessionid;nbr_elmt
					// END IF;

					service.updateFromTable("update ElementSalaireNet a " + "set mont = (select sum( mont ) from Rhthevar" + " where identreprise = '" + parameter.getDossier() + "'" + " and nmat = '" + infoSalary.getComp_id().getNmat()
							+ "'" + " and rubq = a.crub " + " and aamm = '" + myMonthOfPay.getYearAndMonth() + "'" + " and nbul = " + this.getNbul() + " )" + " where exists(" + " select rubq from Rhthevar"
							+ " where identreprise = '" + parameter.getDossier() + "'" + " and nmat = '" + infoSalary.getComp_id().getNmat() + "'" + " and rubq = a.crub " + " and aamm = '" + myMonthOfPay.getYearAndMonth() + "'"
							+ " and nbul = " + this.getNbul() + " )" + " and sessionid = " + parameter.getSessionId());
				}
			}
			//
			// ELSE
			else
			{
				
				
				double mont = 0;
				String strQueryUpdate = StringUtils.EMPTY;
				
				for (String rubAAjouterAuNet : parameter.listeRubriquesAAjouterAuNet)
				{
					mont = 0;
					String strLib=" select c.monp from ElementFixeSalaire c where  c.identreprise ='"+parameter.getDossier()+"' and c.nmat = '"+infoSalary.getComp_id().getNmat()+"' and c.codp = '"+rubAAjouterAuNet+"'";
					strLib+=" and ( c.ddeb is null or (c.ddeb is not null and c.ddeb <= '" + new ClsDate(myMonthOfPay.getLastDayOfMonth()).getDateS(parameter.appDateFormat) + "'))";
					strLib+=" and ( c.dfin is null or (c.dfin is not null and c.dfin >= '" + new ClsDate(myMonthOfPay.getFirstDayOfMonth()).getDateS(parameter.appDateFormat) + "'))";
					List<Object> lst = service.find(strLib);
					if(! lst.isEmpty() && lst.get(0) != null)
						mont = new BigDecimal(lst.get(0).toString()).doubleValue();
					
					strQueryUpdate = "update ElementSalaireNet set mont = " + mont + " where crub = '" + rubAAjouterAuNet + "' and sessionid = "+ parameter.getSessionId();
					
					if(mont != 0)
					service.updateFromTable(strQueryUpdate);
				}

				listOfCount = service.find("select count(*) from ElementVariableDetailMois" + " where identreprise ='" + parameter.getDossier() + "'" + " and nmat = '" + infoSalary.getComp_id().getNmat() + "'" + " and aamm = '"
						+ myMonthOfPay.getYearAndMonth() + "'" + " and nbul = " + this.getNbul());
				count = 0;
				if (listOfCount != null && listOfCount.size() > 0)
				{
					count = ((Long) listOfCount.get(0)).intValue();
				}

				if (count > 0)
				{
				
					for (String rubAAjouterAuNet : parameter.listeRubriquesAAjouterAuNet)
					{
						mont = 0;
						String strLib=" select sum( mont ) from ElementVariableDetailMois c where  c.identreprise ='"+parameter.getDossier()+"' and c.nmat = '"+infoSalary.getComp_id().getNmat()+"' and c.rubq = '"+rubAAjouterAuNet+"'";
						strLib+=" and nbul = "+this.getNbul()+" and aamm = '" + myMonthOfPay.getYearAndMonth() + "'" ;
						
						List<Object> lst = service.find(strLib);
						if(! lst.isEmpty() && lst.get(0) != null)
							mont = new BigDecimal(lst.get(0).toString()).doubleValue();
						
						strQueryUpdate = "update ElementSalaireNet set mont = " + mont + " where crub = '" + rubAAjouterAuNet + "' and sessionid = "+ parameter.getSessionId();
						if(mont != 0)
						service.updateFromTable(strQueryUpdate);
					}
				}

			}

		}
		catch (SQLException e)
		{
			// logger
			e.printStackTrace();
		}
	}

	/**
	 * => corps() process all the rubriques of that salary
	 * 
	 * @param w_dec1
	 */
	public void processAllRubrique(int w_dec1, String dateformat)
	{
		// if('O' == this.getParameter().getGenfile()) addToOutputtext("\n"+">> processAllRubrique");
		// if('O' == this.getParameter().getGenfile()) addToOutputtext("\n"+">> AJUSTEMENT :" + w_dec1);
		//
		if (this.getParameter().isUseRetroactif())
			processAllRubrique(parameter.listOrdonneRubrique, w_dec1, dateformat);
		else
			processAllRubrique(parameter.listOrdonneRubrique, w_dec1, dateformat);
	}

	/**
	 * => corps() process all the rubriques of that salary numeroAjustement => w_numcal
	 * 
	 * @param listOfAllRubrique
	 * @param w_dec1
	 */
	boolean applyTaux = false;

	private void processAllRubrique(List<String> listOrdonneRubrique, int w_dec1, String dateformat)
	{
		boolean go = false;
		// if('O' == this.getParameter().getGenfile()) addToOutputtext("\n"+">> processAllRubrique");
		// INITIALISER LES PARAM DES RUBRIQUES A ZERO
		// IF w_dec1 = 0 THEN
		// st_rub := 0;
		// rub_trt := 0;
		// t9999_basc := t9999_basc_vide;
		// t9999_basp := t9999_basp_vide;
		// t9999_taux := t9999_taux_vide;
		// t9999_mont := t9999_mont_vide;
		// derniere_ligne := 0;
		// ELSE
		// rub_trt := rub_trt - 1;
		// END IF;
		// UTILISER ListOfAllRubriqueRetro
		// LOOP

		if (w_dec1 == 0)
		{
			ClsRubriqueClone rubriqueClone = null;
		
			for (String crub : listOrdonneRubrique)
			{
				rubriqueClone = parameter.getListOfAllRubriqueMap().get(crub);
				if(rubriqueClone != null)
				{
					rubriqueClone.setRates(0);
					rubriqueClone.setAmount(0);
					rubriqueClone.setBase(0);
					rubriqueClone.setBasePlafonnee(0);
				}
			}
			this.derniereLigne = 0;
		}
		else
		{
			
		}
		String codeRubrique = "";
		String rubriqueNet = this.getParameter().getPaieAuNetRubrique();
		String rubriqueAffectationEcart = this.getParameter().getPaieAuNetAffectationEcartRubrique();
		ClsRubriqueClone rubriqueClone = null;
		int sx = 0;
		for (String crub : listOrdonneRubrique)
		{// rubriqueClone=t_rub
			if(crub.startsWith("38") || "0333".equals(crub) || "0330".equals(crub))
				sx = 0;
			rubriqueClone = parameter.getListOfAllRubriqueMap().get(crub);
			codeRubrique = rubriqueClone.getRubrique().getComp_id().getCrub();
			addToOutputtext("\n" + "\n************** RUBRIQUE : " + codeRubrique);
			if (StringUtils.equals(parameter.rubATracer, codeRubrique))
			{
				addToOutputtext("\n" + "\nBreak Point : " + codeRubrique);
			}
			//
			// rub_trt := TO_NUMBER(t_rub.crub);
			// --------------------------------------------------------------------------
			// -- PAIE AU NET :
			// --------------------------------------------------------------------------
			//
			// W_Cont := TRUE;
			go = true;

			if (codeRubrique.equals(rubriqueNet))
			{
				ParameterUtil.println("En Entrée : " + rubriqueClone.getAmount() + ", Base = " + rubriqueClone.getBase());
			}

			if (codeRubrique.equals(rubriqueAffectationEcart))
			{
				ParameterUtil.println("En Entrée : " + rubriqueAffectationEcart + " " + rubriqueClone.getAmount() + ", Base = " + rubriqueClone.getBase());
			}

			addToOutputtext("\n" + "w_dec1==" + w_dec1 + " et current rub = " + rubriqueClone.getRubrique().getComp_id().getCrub() + "et this.getParameter().getPaieAuNetRubrique() = "
					+ this.getParameter().getPaieAuNetRubrique());
			//
			// IF w_dec1 = 1 AND t_rub.crub <= w_rubnet THEN
			// //w_rubnet=parameter.getPaieAuNetRubrique()
			// W_Cont := FALSE;
			// END IF;
			// @emmanuel:
			// si la rubrique est inf. é la rubrique du net

			if ((w_dec1 == 1) && (rubriqueClone.getRubrique().getComp_id().getCrub().compareTo(this.getParameter().getPaieAuNetRubrique()) <= 0))
			{

				addToOutputtext("\n" + "w_dec1==1 et current rub = " + rubriqueClone.getRubrique().getComp_id().getCrub() + "<= " + this.getParameter().getPaieAuNetRubrique());
				go = false;
//				if (codeRubrique.equals(rubriqueNet))
//				{
//					ParameterUtil.println("En Sortie precipité 1 : " + rubriqueClone.getAmount() + ", Base = " + rubriqueClone.getBase());
//				}
//				if (codeRubrique.equals(rubriqueAffectationEcart))
//				{
//					ParameterUtil.println("En Sortie precipité 1 : " + rubriqueAffectationEcart + " " + rubriqueClone.getAmount() + ", Base = " + rubriqueClone.getBase());
//				}
			}
			//
			// IF W_Cont THEN
			if (go)
			{
				// i := TO_NUMBER(t_rub.crub);
				// IF w_paienet = 'O' AND t_rub.crub = w_rubaff AND //
				// w_rubaff=parameter.getPaieAuNetAffectationEcartRubrique()
				// (w_rubaff > w_rubnet)
				// //w_rubnet=parameter.getPaieAuNetRubrique()
				// THEN
				// t9999_taux(i) := 0;
				// ELSE
				// t9999_basc(i) := 0; -- Le tableau est charge a zero
				// t9999_basp(i) := 0; -- pour toutes les rubriques sauf
				// t9999_taux(i) := 0; -- celle d'affectation
				// t9999_mont(i) := 0;
				// END IF;
				if ("O".equals(this.infoSalary.getPnet()) && rubriqueClone.getRubrique().getComp_id().getCrub().equals(this.getParameter().getPaieAuNetAffectationEcartRubrique())
						&& parameter.getPaieAuNetAffectationEcartRubrique().compareToIgnoreCase(rubriqueNet) > 0)
				{

					addToOutputtext("\n" + ">> Salarié traité au net, rubrique courante egal é la rubrique affectation ecart rubrique; et rubrique affectation ecart superieur é la rubrique du paie au Net " + rubriqueNet);
					rubriqueClone.setRates(0);
				}
				else
				{
					rubriqueClone.setRates(0);
					rubriqueClone.setAmount(0);
					rubriqueClone.setBase(0);
					rubriqueClone.setBasePlafonnee(0);
				}
				//
				// -- Pas de calcul si flag de calcul a NON
				// IF t_rub.calc = 'N'
				// THEN
				// W_Cont := FALSE;
				//
				// IF stc AND rub_stc THEN
				// FOR i IN 1..nb_tab_stc LOOP
				// IF tab_rub_stc(i) = t_rub.crub THEN
				// //ListOfRubriqueOfMotifFinContrat=tab_rub_stc
				// W_Cont := TRUE;
				// EXIT;
				// END IF;
				// END LOOP;
				// END IF;
				// END IF;
				if ("N".equals(rubriqueClone.getRubrique().getCalc()))
				{

					addToOutputtext("\n" + "...Rubrique non calculée!");
					go = false;

					if (this.getParameter().isStc() && this.getParameter().isRubriqueStc())
					{
						for (ClsRubriqueClone clone : ListOfRubriqueOfMotifFinContrat)
						{
							if (codeRubrique.equals(clone.getRubrique().getComp_id().getCrub()))
							{
								go = true;
								break;
							}
						}
					}
				}
			}
			// END IF;
			//
			// IF W_Cont THEN
			if (go)
			{
				// @emmanuel:on ne fait rien ici car on a déjé la list :
				// ClsRubriqueClone.listOfElementVariable
				// FOR i IN 1..20 LOOP
				// tab_elmt_mont(i) := NULL;
				// tab_elmt_argu(i) := NULL;
				// tab_elmt_nprt(i) := NULL;
				// tab_elmt_ruba(i) := NULL;
				// END LOOP;
				//
				// nb_prets := 0;
				// IF t_rub.algo IN (13,17,20) AND NOT retroactif THEN
				// charg_prets;
				// END IF;
				if (!this.getParameter().isUseRetroactif() && (rubriqueClone.getRubrique().getAlgo() == 13 || rubriqueClone.getRubrique().getAlgo() == 17 || rubriqueClone.getRubrique().getAlgo() == 20))
				{
					rubriqueClone.loadListOfLoanNumber();
				}

				//
				// wcalc.identreprise := wpdos.identreprise;
				// wcalc.aamm := w_aamm ;
				// wcalc.nmat := wsal01.nmat;
				// wcalc.nbul := wsd_fcal1.nbul;
				// wcalc.trtb := '1';
				// wcalc.clas := wsal01.clas;
				// w_mon := 0;
				// w_monhs := 0;
				// w_bas := 0;
				// w_basp := 0;
				// w_tau := 0;
				// w4a := t_rub.crub;
				// w_rub := t_rub.crub;
				// w_nprt := ' ';
				//
				this.getValeurRubriquePartage().setAmount(0);
				this.getValeurRubriquePartage().setMonhs(0);
				this.getValeurRubriquePartage().setBase(0);
				this.getValeurRubriquePartage().setBasePlafonnee(0);
				this.getValeurRubriquePartage().setRates(0);
				// -- Verification du champ d'application
				// IF NOT PA_CALC_ANX.champ_app THEN
				// IF t_rub.epbul != 0 THEN
				// -- Conservation car pied de bulletin
				// W_Faitout := ins_rubq(rub_trt);
				// END IF;
				// W_Cont := FALSE;
				// END IF;
				if (!rubriqueClone.champApplicationSalarieToRubrique(this.getParameter().getNumeroAjustementActuel()))
				{

					addToOutputtext("\n" + ">> LE CHAMP NE S'APPLIQUE PAS : numero ajustement, valeur actuelle= " + this.getParameter().getNumeroAjustementActuel());
					// if('O' == this.getParameter().getGenfile()) addToOutputtext("\n"+">> LE CHAMP NE S'APPLIQUE PAS
					// "+rubriqueClone.getRubrique().getEpbul() );
					if (rubriqueClone.getRubrique().getEpbul() != null && rubriqueClone.getRubrique().getEpbul() != 0)
					{
						//cas du pied de bull; on insere si le montant est non null : yannick le 09 08 2011
						if(this.getValeurRubriquePartage().getAmount() != 0)
						  this.insererUneRubriqueClone(rubriqueClone, "1");
					}
					// --
					// Conservation
					// car
					// pied
					// de
					// bulletin
					go = false;

				}
			}
			// END IF;
			//
			// IF W_Cont THEN
			if (go)
			{
				// -------------------------------------------------------------------------
				// -- RECHERCHE DE LA BASE CALCULEE
				// -------------------------------------------------------------------------
				// -------------------------------------------------------------------------
				// -- PAIE AU NET : Lecture de la table temporaire de la
				// rubrique soumise a
				// -- ajustement ( Pas de passage dans cal_base() )
				// -------------------------------------------------------------------------
				//
				// IF w_paienet = 'O' THEN -- Calcul d une paie au net
				// BEGIN
				// -- Lecture du montant en parubajus
				// SELECT mont INTO w_bas FROM parubajus
				// WHERE crub = t_rub.crub
				// AND sessionid = w_sessionid;
				// EXCEPTION
				// WHEN NO_DATA_FOUND THEN null;
				// END;
				// IF w_bas IS NULL THEN
				// w_bas := 0;
				// END IF;
				//
				// IF SQL%NOTFOUND THEN
				// W_Faitout := PA_CALC_ANX.cal_base;
				// ELSE
				// appl_elmt := TRUE;
				// nbr_elmt := 1;
				// val_elmt := w_bas;
				// END IF;
				// ELSE
				// W_Faitout := PA_CALC_ANX.cal_base;
				// END IF;
				// String queryString = "select distinct mont from
				// ElementSalaireAjus" +v
				// " where sessionid = " + parameter.getSessionId() +
				// " and crub = '" +
				// rubriqueClone.getRubrique().getComp_id().getCrub() + "'";
				// List ListOfRow = service.find(queryString);
				double base = 0;
				// if("O".equals(this.infoSalary.getPnet())
				// &&(ListOfRow != null && ListOfRow.size() > 0 && !
				// ClsObjectUtil.isNull(ListOfRow.get(0)))){
				// if('O' == this.getParameter().getGenfile()) addToOutputtext("\n"+">> RUBRIQUE NETTE");
				// base = (Double)((Object[])ListOfRow.get(0))[0];
				// this.getValeurRubriquePartage().setBase(base);
				// //
				// rubriqueClone.setElementVariableApply(true);
				// rubriqueClone.setElementVariableValeur(this.getValeurRubriquePartage().getBase());
				// }
				// else{
				// rubriqueClone.calculateBase();
				// }
				Object[] obj = null;
				//int nbre_elvar = 1;
				if ("O".equals(this.infoSalary.getPnet()))
				{
					// List l = service.find("select mont from ElementSalaireAjus" + " where ajnu = " + this.getParameter().getNumeroAjustementActuel()
					// + " and sessionid = '" + this.getParameter().getSessionId() + "' and crub ='"+rubriqueClone.getRubrique().getComp_id().getCrub()+"'");
					List l = null;
					//YT : Ajout le 30/10/2010; on n'exécute cette recherche que pour les rubriques soumises é ajustement
					if(StringUtils.equals("O", rubriqueClone.getRubrique().getAjus()))
							l = service.find("select mont from ElementSalaireAjus where sessionid = '" + this.getParameter().getSessionId() + "' and crub ='" + rubriqueClone.getRubrique().getComp_id().getCrub() + "'");

					if (l != null && l.size() > 0)
					{
						// if (!parameter.getListOfRubqOfsession().isEmpty()
						// && parameter.getListOfRubqOfsession().containsKey(rubriqueClone.getRubrique().getComp_id().getCrub()))
						// {

						addToOutputtext("\n" + ">> Salarié traité au net " + this.getParameter().getCalcul() + " avec liste des rubriques de session contenat la rubrique courante ");
						// obj = (Object[]) parameter.getListOfRubqOfsession().get(rubriqueClone.getRubrique().getComp_id().getCrub());
						// base = (obj[1] instanceof BigDecimal) ? ((BigDecimal) obj[1]).doubleValue() : (Long) obj[1];
						base = ((BigDecimal) (l.get(0) != null ? l.get(0) : 0)).doubleValue();
						this.getValeurRubriquePartage().setBase(base);
						//
						rubriqueClone.setElementVariableApply(true);
						rubriqueClone.setElementVariableValeur(this.getValeurRubriquePartage().getBase());
						//nbre_elvar = 1;
					}
					else
					{

						addToOutputtext("\n" + ">> Salarié traité au net, calcul de la base de calcul");
						rubriqueClone.calculateBase();
					}
				}
				else
				{
					rubriqueClone.calculateBase();
					
				}

				addToOutputtext("\n" + ">> Suite du calcul de la rubrique " + rubriqueClone.getRubrique().getComp_id().getCrub());
				// dsfdfsdf

				//
				// num_elmt := 1;
				// WHILE num_elmt <= nbr_elmt LOOP
				rubriqueClone.setNumElementVarCourant(0);
				// Object[] elementVariable = null;
				Object[] elementVariableSuiv = null;
				int i = 1;
				int nbre_elvar = 1;
				//rubriqueClone.setNumElementVarCourant(num_elmt-1);
				if (rubriqueClone.getListOfElementVariable() != null || rubriqueClone.getListOfLoanNumber() != null)
				{
					nbre_elvar = rubriqueClone.getListOfElementVariable() != null ? rubriqueClone.getListOfElementVariable().size() : 0;
					if (nbre_elvar == 0)
						nbre_elvar = rubriqueClone.getListOfLoanNumber() != null ? rubriqueClone.getListOfLoanNumber().size() : 0;
					if (nbre_elvar == 0)
						nbre_elvar = 1;
				}
				else
				{
					rubriqueClone.setListOfElementVariable(new ArrayList());
				}

				addToOutputtext("\n" + ">> Traitement des elements variables de taille " + nbre_elvar);
				while (i <= nbre_elvar)
				{
					// if('O' == this.getParameter().getGenfile()) addToOutputtext("\n"+"************************ élément
					// variable idx: " + i);
					//@add
					i++;
					
					// elementVariable = (Object[])obj;
					// w_basp := w_bas; -- Base plafonnee
					// if('O' == this.getParameter().getGenfile()) addToOutputtext("\n"+"getValeurRubriquePartage base:
					// "+this.getValeurRubriquePartage().getBase());
					this.getValeurRubriquePartage().setBasePlafonnee(this.getValeurRubriquePartage().getBase());
					//
					// IF t_rub.rreg = 'O' AND t_rub.rman = 'N' THEN
					// IF t_rub.algo = 37 THEN
					// W_Faitout := regul_fr;
					// IF pb_calcul THEN
					// RETURN FALSE;
					// END IF;
					// ELSE
					// W_Faitout := cal_rub_reg;
					// IF pb_calcul THEN
					// RETURN FALSE;
					// END IF;
					// END IF;
					if ("O".equals(rubriqueClone.getRubrique().getRreg()) && "N".equals(rubriqueClone.getRubrique().getRman()))
					{

						if (rubriqueClone.getRubrique().getAlgo() == 37)
						{
							rubriqueClone.regularisationFrancaise();
						}
						else
						{// per_regu=nbrePeriodeRegularisation/getParamCumul().getNbreMoisCumul();nbper_regu=nbrePeriodeRegularisationEv
							Double nbrePeriodeRegularisationEv = Double.valueOf(0);
							if(!StringUtils.equalsIgnoreCase(this.getParameter().getRegularisationRubrique(), ParameterUtil.formatRubrique))
							nbrePeriodeRegularisationEv = (tempNumber = this.getUtilNomenclature().getSumOfAmountOfVariableElement(this.getParameter().getDossier(), infoSalary.getComp_id().getNmat(),
									moisPaieCourant, periodOfPay, this.getParameter().getRegularisationRubrique(), nbul)) == null ? 0 : tempNumber.doubleValue();
							// rubriqueClone.calculateRubriqueOfRegularisation1(this.getParamCumul().getNbreMoisCumul(), nbrePeriodeRegularisationEv
							// .intValue());
							rubriqueClone.calculateRubriqueOfRegularisation1(nbrePeriodeRegularisationEv.intValue());
							parameter.setNbrePeriodeRegularisationEv(nbrePeriodeRegularisationEv.intValue());
						}
						if (parameter.isPbWithCalulation())
						{

							addToOutputtext("\n" + "1---PB avec le calcul d'une rubrique, annuler le calcul de toutes les autres rubriques");
							// commenté par yannick
							// return ;
						}
					}
					// ELSE
					else
					{

						addToOutputtext("\n" + "rubriqueClone.getRubrique().getRreg() = " + rubriqueClone.getRubrique().getRreg() + " et rubriqueClone.getRubrique().getRman()=" + rubriqueClone.getRubrique().getRman());
						// IF (w_paienet = 'O' AND t_rub.ajus = 'O') OR
						// t_rub.algo IN (1, 6, 18, 28)
						// THEN
						boolean isalgo161828 = (rubriqueClone.getRubrique().getAlgo() == 1 || rubriqueClone.getRubrique().getAlgo() == 6 || rubriqueClone.getRubrique().getAlgo() == 18 || rubriqueClone.getRubrique()
								.getAlgo() == 28);
						if (("O".equals(this.infoSalary.getPnet()) && "O".equals(rubriqueClone.getRubrique().getAjus())) || isalgo161828)
						{
							// IF appl_elmt OR t_rub.algo = 1 THEN
							// -- Application de l'algo
							// PA_ALGO.calc_algo(w_bas, w_basp, w_mon,
							// w_tau, w_argu, w_aamm,
							// wsd_fcal1.nbul, Pb_Calcul);
							// IF pb_calcul THEN
							// RETURN FALSE;
							// END IF;
							// END IF;

							addToOutputtext("\n" + "this.infoSalary.getPnet() = " + this.infoSalary.getPnet() + " et rubriqueClone.getRubrique().getAjus()=" + rubriqueClone.getRubrique().getAjus());

							if (rubriqueClone.isElementVariableApply() || rubriqueClone.getRubrique().getAlgo() == 1)
							{
								if (!rubriqueClone.applyAlgorithm())
								{
									// logger

									addToOutputtext("\n" + ".............................ERREUR algo" + rubriqueClone.getRubrique().getAlgo() + " !!!");
									if (parameter.isPbWithCalulation())
									{

										addToOutputtext("\n" + "2---PB avec le calcul d'une rubrique, annuler le calcul de toutes les autres rubriques");
										// commenté par yannick
										// return ;
									}
								}
							}
						}
						// ELSE
						else
						{
							// IF appl_elmt -- Il existe un E.V. ou E.F.
							// donc pas d'algo
							// THEN
							// w_mon := val_elmt;
							// ELSE
							// PA_ALGO.calc_algo(w_bas,w_basp,w_mon,w_tau,w_argu,w_aamm,wsd_fcal1.nbul,Pb_Calcul);
							// IF pb_calcul THEN
							// RETURN FALSE;
							// END IF;
							// END IF;
							if (rubriqueClone.isElementVariableApply())
							{

								addToOutputtext("\n" + "rubriqueClone.isElementVariableApply() must be true, value= " + rubriqueClone.isElementVariableApply() + " and rub value = "
										+ rubriqueClone.getElementVariableValeur());
								this.getValeurRubriquePartage().setAmount(rubriqueClone.getElementVariableValeur());

							}
							else
							{

								addToOutputtext("\n" + "rubriqueClone.isElementVariableApply() must be false, value= " + rubriqueClone.isElementVariableApply());
								if (!rubriqueClone.applyAlgorithm())
								{
									// logger

									addToOutputtext("\n" + ".............................ERREUR algo" + rubriqueClone.getRubrique().getAlgo() + " !!!");
									if (parameter.isPbWithCalulation())
									{

										addToOutputtext("\n" + "3---PB avec le calcul d'une rubrique, annuler le calcul de toutes les autres rubriques");
										// commenté par yannick
										// return ;
									}
								}
							}
						}
						// END IF;
					}
					// END IF;
					//
					// appl_tx := FALSE;
					applyTaux = false;
					//
					// IF t_rub.algo <> 17 AND t_rub.algo <> 20 THEN
					// W_Faitout := cal_pc;
					// END IF;
					if (rubriqueClone.getRubrique().getAlgo() != 17 && rubriqueClone.getRubrique().getAlgo() != 20)
					{
						rubriqueClone.calculatePourcentage();
					}
					//
					// -- Prorata des rubriques
					// -- LH 270198
					// -- Le prorata ne s'applique pas aux rubriques saisie
					// en E.V.
					// IF NOT appl_tx AND
					// NOT Rbq_en_EV
					// THEN
					if ((!applyTaux) && (!rubriqueClone.isRubriqueEV()))
					{
						// IF t_rub.pror = 'O' AND
						// w_mon != 0 AND
						// t_rub.algo <> 17 AND
						// t_rub.algo <> 20
						// THEN
						if ("O".equals(rubriqueClone.getRubrique().getPror()) && this.getValeurRubriquePartage().getAmount() != 0 && rubriqueClone.getRubrique().getAlgo() != 17
								&& rubriqueClone.getRubrique().getAlgo() != 20)
						{
							// IF wprorat_std THEN
							// W_Faitout := PA_CALC_ANX.prorat_std;
							// ELSE
							// W_Faitout := PA_CALC_ANX.prorat_spc;
							// END IF;
							if (this.getParameter().isProrataStandard())
							{
								rubriqueClone.calculateProrataStandard(dateformat);
							}
							else
							{
								rubriqueClone.calculateProrataSpecifiqueRCI();
							}
						}
						// ELSE
						else
						{
							// -- LH 050198
							// -- Si une rubrique est proratee sur le
							// dernier mois de conges dans un fictif B
							// -- elle sera proratee au jours de presence
							// sur le bulletin reel de retour de conges.
							// -- Ceci ne s'applique pas aux prets externes.
							// -- IF w_fictif = 'O' AND w_typ_fictif = 'B'
							// AND
							// IF Fictif_B AND
							// t_rub.cabf = 'P' AND
							// w_mon != 0 AND
							// t_rub.algo <> 17 AND
							// t_rub.algo <> 20 AND
							// NOT PA_PAIE.NouZ(wnbja_cg_abs)
							// THEN
							// W_Faitout := PA_CALC_ANX.prorat_fic;
							// END IF;
							if ("P".equals(rubriqueClone.getRubrique().getCabf()) && this.getValeurRubriquePartage().getAmount() != 0 && rubriqueClone.getRubrique().getAlgo() != 17
									&& rubriqueClone.getRubrique().getAlgo() != 20 && this.getWorkTime().getNbreJourAbsenceConges() != 0 && this.getParameter().isFictiveCalculusB())
							{
								rubriqueClone.calculateProrataFictif();
							}
						}
						// END IF;
						//
						// IF NOT PA_PAIE.NouB(t_rub.resl) AND
						// t_rub.algo <> 13 AND t_rub.algo <> 17 AND
						// t_rub.algo <> 20 THEN
						// W_Faitout := PA_CALC_ANX.compar;
						// END IF;
						if (!ClsObjectUtil.isNull(rubriqueClone.getRubrique().getResl()) && !tjrsComparer)
						{
							if (rubriqueClone.getRubrique().getAlgo() != 13 && rubriqueClone.getRubrique().getAlgo() != 17 && rubriqueClone.getRubrique().getAlgo() != 20)
							{
								rubriqueClone.compareResult();
								// if('O' == this.getParameter().getGenfile()) addToOutputtext("\n"+">> Fin
								// compareResult");
							}
						}
					}
					// ELSE
					// -- on ne fait rien si ondoit appliquer un taux de
					// paiement pour absence ???
					// appl_tx := FALSE;
					// END IF;
					else
						applyTaux = false;
					if (tjrsComparer)
					{
						if (!ClsObjectUtil.isNull(rubriqueClone.getRubrique().getResl()))
						{
							if (rubriqueClone.getRubrique().getAlgo() != 13 && rubriqueClone.getRubrique().getAlgo() != 17 && rubriqueClone.getRubrique().getAlgo() != 20)
							{
								rubriqueClone.compareResult();
								// if('O' == this.getParameter().getGenfile()) addToOutputtext("\n"+">> Fin
								// compareResult");
							}
						}
					}
					//
					// IF w_paienet = 'O' THEN
					// IF t_rub.ecar = 'O' THEN
					// IF t_rub.prbul = 3 OR t_rub.prbul = 4 THEN
					// A_ecarter_du_Net := A_ecarter_du_Net + w_mon;
					// ELSE
					// A_ecarter_du_Net := A_ecarter_du_Net - w_mon;
					// END IF;
					// END IF;
					//
					// IF t_rub.crub = w_rubbrut THEN
					// Salaire_brut := w_mon;
					// END IF;
					//
					// IF t_rub.crub = w_rubnet THEN
					// Net_calcule := w_mon;
					// END IF;
					// END IF;
					if ("O".equals(this.infoSalary.getPnet()))
					{
						if ("O".equals(rubriqueClone.getRubrique().getEcar()))
						{

							addToOutputtext("\n" + ">> Salarié traité au net, avec rubrique.ecar = O ");
							if (rubriqueClone.getRubrique().getPrbul() == 3 || rubriqueClone.getRubrique().getPrbul() == 4)
							{
								this.salaireAecarterDuNet += this.getValeurRubriquePartage().getAmount();
							}
							else
							{
								this.salaireAecarterDuNet -= this.getValeurRubriquePartage().getAmount();
							}
						}

						addToOutputtext("\n" + ">> Salarié traité au net, modification du salaireBrut et du salaireNet");
						// si la rubrique est une rubrique du brut
						if (rubriqueClone.getRubrique().getComp_id().getCrub().equals(this.getParameter().getBrutRubrique()))
							this.salaireBrut = this.getValeurRubriquePartage().getAmount();
						// si la rubrique est une rubrique du net
						if (rubriqueClone.getRubrique().getComp_id().getCrub().equals(this.getParameter().getPaieAuNetRubrique()))
							this.salaireNet = this.getValeurRubriquePartage().getAmount();
					}
					//
					// insertion := FALSE;
					// IF NOT PA_PAIE.NouZ(w_mon) THEN
					// insertion := TRUE;
					// END IF;
					// IF NOT insertion AND t_rub.ednul = 'O' THEN
					// insertion := TRUE;
					// END IF;
					// IF NOT insertion AND t_rub.epbul != 0 THEN
					// insertion := TRUE;
					// END IF;
					boolean insertion = false;
					if (this.getValeurRubriquePartage().getAmount() != 0)
					{
						insertion = true;
					}
					else
					{

						addToOutputtext("\n" + "Montant de la rubrique  = " + this.getValeurRubriquePartage().getAmount() + ", insertion reste é false");

					}
					if (!insertion && "O".equals(rubriqueClone.getRubrique().getEdnul()))
					{
						insertion = true;
					}
					else
					{

						addToOutputtext("\n" + "Edition si null =" + rubriqueClone.getRubrique().getEdnul() + ", dc insertion reste é false");
					}
					if (!insertion && rubriqueClone.getRubrique().getEpbul() != null && (0 != rubriqueClone.getRubrique().getEpbul()))
					{
						//cas du pied de bull; on insere si le montant est non null : yannick le 09 08 2011
						if(this.getValeurRubriquePartage().getAmount() != 0)
							insertion = true;
					}
					else
					{

						addToOutputtext("\n" + "Edition pied de bulletin =" + rubriqueClone.getRubrique().getEpbul() + ", dc insertion reste é false");
					}
					// IF NOT insertion THEN
					// FOR i IN 1..nb_tab_rub LOOP
					// IF PA_PAIE.NouZ(tab_rub(i)) THEN
					// insertion := FALSE;
					// EXIT;
					// END IF;
					// IF tab_rub(i) = t_rub.crub THEN
					// insertion := TRUE;
					// EXIT;
					// END IF;
					// END LOOP;
					// END IF;
					if (!insertion)
					{
						if (this.parameter.getListOfRubriqueToCalculate() != null)
							insertion = this.parameter.getListOfRubriqueToCalculate().contains(rubriqueClone.getRubrique().getComp_id().getCrub());
						if (!insertion)

							addToOutputtext("\n" + "Rubrique de non régularisation, dc insertion reste é false");
					}

					// -- Insertion dans pacalc et la table des rubriques
					// deja calculees
					// IF insertion THEN
					// W_Faitout := ins_rubq(rub_trt);
					// END IF;
					// if('O' == this.getParameter().getGenfile()) addToOutputtext("\n"+"rubrique :" +
					// rubriqueClone.getRubrique().getComp_id().getCrub());
					// if('O' == this.getParameter().getGenfile()) addToOutputtext("\n"+"getAmount :" +
					// getValeurRubriquePartage().getAmount());
					// if('O' == this.getParameter().getGenfile()) addToOutputtext("\n"+"getEdnul :" +
					// rubriqueClone.getRubrique().getEdnul());
					// if('O' == this.getParameter().getGenfile()) addToOutputtext("\n"+"getEpbul :" +
					// rubriqueClone.getRubrique().getEpbul());
					if (insertion)
					{
						this.insererUneRubriqueClone(rubriqueClone, "1");
					}
					else
					{
						evparam.setArgu("");
						evparam.setNprt("");
						evparam.setRuba("");
					}
					//
					// num_elmt := num_elmt + 1;
					// IF num_elmt <= nbr_elmt THEN
					// IF NOT (t_rub.algo = 6 OR t_rub.algo = 7 OR
					// t_rub.algo = 18 OR t_rub.algo = 28 ) THEN
					// w_bas := tab_elmt_mont(num_elmt);
					// END IF;
					// w_argu := tab_elmt_argu(num_elmt);
					// wevar.nprt := tab_elmt_nprt(num_elmt);
					// wevar.ruba := tab_elmt_ruba(num_elmt);
					// val_elmt := tab_elmt_mont(num_elmt);
					// appl_elmt := TRUE;
					//
					// ELSE
					// IF nb_prets != 0 THEN
					// num_elmt := 1;
					// nbr_elmt := 1;
					// appl_elmt := FALSE;
					// END IF;
					// END IF;
					//
					
					rubriqueClone.setNumElementVarCourant(1 + rubriqueClone.getNumElementVarCourant());
					//num_elmt = num_elmt+1;
					//rubriqueClone.setNumElementVarCourant(num_elmt-1);
					// si l'élément suivant existe
					if (rubriqueClone.getNumElementVarCourant() < rubriqueClone.getListOfElementVariable().size())
					//if(num_elmt<=nbre_elvar)
					{
						elementVariableSuiv = (Object[]) rubriqueClone.getListOfElementVariable().get(rubriqueClone.getNumElementVarCourant());
						//elementVariableSuiv = (Object[]) rubriqueClone.getListOfElementVariable().get(num_elmt-1);
						//
						if (!(rubriqueClone.getRubrique().getAlgo() == 6 || rubriqueClone.getRubrique().getAlgo() == 7 || rubriqueClone.getRubrique().getAlgo() == 18 || rubriqueClone.getRubrique().getAlgo() == 28))
						{
							if (!ClsObjectUtil.isNull(elementVariableSuiv[0]))
							{
								this.getValeurRubriquePartage().setBase(new BigDecimal(elementVariableSuiv[0].toString()).doubleValue());
							}
						}
						if (!ClsObjectUtil.isNull(elementVariableSuiv[0]))
							rubriqueClone.setElementVariableValeur(new BigDecimal(elementVariableSuiv[0].toString()).doubleValue());
						// else
						// rubriqueClone.setElementVariableValeur(0);
						rubriqueClone.setElementVariableApply(true);
						evparam.setApplyElementVariable(true);
						if (!ClsObjectUtil.isNull(elementVariableSuiv[0]))
							evparam.setMont(new BigDecimal(elementVariableSuiv[0].toString()).doubleValue());
						if (!ClsObjectUtil.isNull(elementVariableSuiv[1]))
							evparam.setArgu((String) elementVariableSuiv[1]);
						if (!ClsObjectUtil.isNull(elementVariableSuiv[2]))
							evparam.setNprt((String) elementVariableSuiv[2]);
						if (!ClsObjectUtil.isNull(elementVariableSuiv[3]))
							evparam.setRuba((String) elementVariableSuiv[3]);
					}
					else
					{
						// IF nb_prets != 0 THEN
						// num_elmt := 1;
						// nbr_elmt := 1;
						// appl_elmt := FALSE;
						// END IF;
						// END IF;
						if (rubriqueClone.getListOfLoanNumber() != null && !rubriqueClone.getListOfLoanNumber().isEmpty())
						{
							rubriqueClone.setNumElementVarCourant(1);
							rubriqueClone.setElementVariableApply(false);
							i = 1;
							//rubriqueClone.setNumElementVarCourant(num_elmt-1);
							nbre_elvar = 1;
						}

					}
					//
					// elementVariableSuiv = null;
				} // for
				// }
				// END LOOP; -- FIN 'WHILE num_elmt <= nbr_elmt'
				//
				// -------------------------------------------------------------------------
				// -- PAIE AU NET : Fin du calcul des rubriques si paie au net
				// -- et rubrique = rubrique du net
				// -------------------------------------------------------------------------
				//
				// IF w_paienet = 'O' AND
				// w_calcul = 'O' AND
				// w_rub = w_rubnet
				// THEN
				// EXIT;
				// END IF;
				
				if (("O".equals(this.getParameter().getCalcul())) && ("O".equals(this.infoSalary.getPnet())) && (rubriqueClone.getRubrique().getComp_id().getCrub().equals(this.getParameter().getPaieAuNetRubrique())))
				{

					addToOutputtext("\n" + "getCalcul = O");
					if (codeRubrique.equals(rubriqueNet))
					{
						ParameterUtil.println("En Sortie precipité 4 : " + rubriqueClone.getAmount() + ", Base = " + rubriqueClone.getBase());
					}
					if (codeRubrique.equals(rubriqueAffectationEcart))
					{
						ParameterUtil.println("En Sortie precipité 4 : " + rubriqueAffectationEcart + " " + rubriqueClone.getAmount() + ", Base = " + rubriqueClone.getBase());
					}
					break;
				}

			}
			// END IF; -- FIN 'IF W_Cont'
			//
			// END LOOP; -- FIN 'WHILE TRUE'
			// ajout de l'affichage des rubriques


			
			this.insererModuleExterne(rubriqueClone, "1");
			
		}// for
	}

	private String minAamm = null;
	private boolean alreadyComputeMinAamm = false;
	
	public String _computeMinOfMonthForCumul()
	{
		if(alreadyComputeMinAamm)
		{
			return minAamm;
		}
		
		String strSqlQuery = "select min(aamm) from CumulPaie " + " where identreprise = '" + this.getParameter().getDossier() + "'" + " and nmat = '" + this.getInfoSalary().getComp_id().getNmat()
				+ "'";

		List aammmin = this.getService().find(strSqlQuery);
		

		if (aammmin.size() > 0)
		{
			if (aammmin.get(0) != null)
				minAamm = (String) aammmin.get(0);
		}
		alreadyComputeMinAamm = true;
		return minAamm;
	}

	
	/**
	 * Calculate the total days of holidays/absence
	 */
	public void calculateNbreMoisSuppl()
	{
		this.workTime.calculateNbreMoisSuppl(this);
	}

	/**
	 * Calcul du nombre de periodes de régulation
	 */
	public void calculNbrePeriodeRegulation()
	{
		this.workTime.calculNbrePeriodeRegulation(this);
	}

	/**
	 * calculer le nombre de jours payés non pris
	 */
	public void calculNbreJourPaidNonPris()
	{
		this.workTime.calculNbreJourPaidNonPris(this);
	}

	/**
	 * calculer le nombre de jours supplémentaires
	 */
	public void calculateNbreJourSuppl()
	{
		this.workTime.calculateNbreJourSuppl(this);
	}

	/**
	 * generer les rubriques
	 * 
	 * @param pseudoRubrique
	 * @param rubriqueConge
	 * @param argu
	 * @param nprt
	 * @param ruba
	 * @param montant
	 */
	public void generatePseudoEv(String pseudoRubrique, String rubriqueConge, String argu, String nprt, String ruba, double montant)
	{
		// Nb_Rbq NUMBER;
		// w_rowid ROWID;
		//
		// CURSOR C_Pseudo IS
		// SELECT rowid FROM paevar
		// WHERE identreprise = wevcg.identreprise
		// AND nmat = wevcg.nmat
		// AND aamm = wevcg.aamm
		// AND nbul = wsd_fcal1.nbul
		// AND rubq = Pseudo_Rbq
		// AND argu = 'PSEUDO-EV';

		//
		// CURSOR C_Pseudo2 IS
		// SELECT rowid FROM pahevar
		// WHERE identreprise = wevcg.identreprise
		// AND nmat = wevcg.nmat
		// AND aamm = wevcg.aamm
		// AND nbul = wsd_fcal1.nbul
		// AND rubq = Pseudo_Rbq
		// AND argu = 'PSEUDO-EV';
		//
		// BEGIN

		if (StringUtils.equals(ParameterUtil.formatRubrique, pseudoRubrique) || StringUtils.equals(ParameterUtil.formatRubrique, rubriqueConge))
			return;

		String queryString = "from ElementVariableDetailMois" + " where cdos='" + parameter.getDossier() + "'" + " and nmat ='" + infoSalary.getComp_id().getNmat() + "'" + " and aamm ='" + monthOfPay + "'" + " and nbul =" + nbul
				+ " and rubq ='" + pseudoRubrique + "'" + " and argu ='PSEUDO-EV'";
		String queryStringRetro = "from Rhthevar" + " where cdos='" + parameter.getDossier() + "'" + " and nmat ='" + infoSalary.getComp_id().getNmat() + "'" + " and aamm ='" + monthOfPay + "'" + " and nbul =" + nbul
				+ " and rubq ='" + pseudoRubrique + "'" + " and argu ='PSEUDO-EV'";
		List listOfElementVariableDetailMois = null;
		List listOfPahevar = null;
		//
		// IF retroactif THEN
		if (parameter.isUseRetroactif())
		{
//			// IF C_Pseudo2%ISOPEN THEN CLOSE C_Pseudo2; END IF;
//			// OPEN C_Pseudo2;
//			// FETCH C_Pseudo2 INTO w_rowid;
//			//
//			// IF C_Pseudo2%NOTFOUND THEN
//			try
//			{
//				listOfPahevar = service.find(queryStringRetro);
//				if (listOfPahevar.isEmpty())
//				{
//					// wevar.identreprise := wpdos.identreprise;
//					// wevar.aamm := wevcg.aamm;
//					// wevar.nmat := wevcg.nmat;
//					// wevar.nbul := wsd_fcal1.nbul;
//					// wevar.rubq := Pseudo_Rbq;
//					// wevar.argu := 'PSEUDO-EV';
//					// wevar.nprt := ' ';
//					// wevar.ruba := ' ';
//					// wevar.mont := Pseudo_Mnt;
//					// INSERT INTO pahevar
//					// (cdos,aamm,nmat,nbul,rubq,argu,nprt,ruba,mont,cuti)
//					// VALUES (wevar.identreprise, wevar.aamm, wevar.nmat, wevar.nbul,
//					// wevar.rubq, wevar.argu, wevar.nprt, wevar.ruba,
//					// wevar.mont,'PSEU');
//					//
//					Rhthevar ev = new Rhthevar();
//					ev.setCdos(parameter.getDossier());
//					ev.setAamm(monthOfPay);
//					ev.setNmat(infoSalary.getComp_id().getNmat());
//					ev.setNbul(nbul);
//					ev.setRubq(rubriqueConge);
//					ev.setArgu(argu);
//					ev.setNprt(nprt);
//					ev.setRuba(ruba);
//					ev.setMont(new BigDecimal(montant));
//					ev.setCuti("PSEU");
//					//
//					//service.save(ev);
//					
//					//Ajout dans la liste des EV deja chargés du salarié
//					Object elvar[] = null;
//						// map.put(crub, value)
//						elvar = new Object[4];
//						elvar[0] = ev.getMont();
//						elvar[1] = ev.getArgu();
//						elvar[2] = ev.getNprt();
//						elvar[3] = ev.getRuba();
//						if (!this.listOfEltvar.containsKey(ev.getRubq()))
//							this.listOfEltvar.put(ev.getRubq(), new ArrayList<Object[]>());
//						this.listOfEltvar.get(ev.getRubq()).add(elvar);
//					
//				}
//				// ELSE
//				else
//				{
//					if (listOfPahevar.size() > 0)
//					{
//						Rhthevar o = new Rhthevar();
//						o = (Rhthevar) listOfPahevar.get(0);
//						// UPDATE pahevar
//						// SET mont = mont + Pseudo_Mnt
//						// WHERE rowid = w_rowid;
//						// service.updateFromTable("update Rhthevar mont = mont
//						// + " + montant +
//						// " where aamm ='" + o.getAamm() + "'" +
//						// " argu = '" + o.getArgu() + "'" +
//						// " identreprise = '" + o.getCdos() + "'" +
//						// " cuti = '" + o.getCuti() + "'" +
//						// " nmat = '" + o.getNmat() + "'" +
//						// " nprt = '" + o.getNprt() + "'" +
//						// " ruba = '" + o.getRuba() + "'" +
//						// " rubq = '" + o.getRubq() + "'" +
//						// " nbul = " + o.getNbul()
//						// );
//						o.setMont(new BigDecimal(o.getMont().doubleValue() + montant));
//						//service.update(o);
//						
//						//MAJ du montant dans la liste des EV deja chargés du salarié
//						Object elvar[] = null;
//						// map.put(crub, value)
//						elvar = new Object[4];
//						elvar[0] = montant;
//						elvar[1] = o.getArgu();
//						elvar[2] = o.getNprt();
//						elvar[3] = o.getRuba();
//						if (!this.listOfEltvar.containsKey(o.getRubq()))
//							this.listOfEltvar.put(o.getRubq(), new ArrayList<Object[]>());
//						this.listOfEltvar.get(o.getRubq()).add(elvar);
//					}
//				}
//			}
//			catch (Exception e)
//			{
//				// logger
//				e.printStackTrace();
//			}
		}
		// ELSE
		else
		{
			// IF C_Pseudo%ISOPEN THEN CLOSE C_Pseudo; END IF;
			// OPEN C_Pseudo;
			// FETCH C_Pseudo INTO w_rowid;
			try
			{
				listOfElementVariableDetailMois = service.find(queryString);
				if (listOfElementVariableDetailMois.isEmpty())
				{
					//
					// IF C_Pseudo%NOTFOUND THEN
					// wevar.identreprise := wpdos.identreprise;
					// wevar.aamm := wevcg.aamm;
					// wevar.nmat := wevcg.nmat;
					// wevar.nbul := wsd_fcal1.nbul;
					// wevar.rubq := Pseudo_Rbq;
					// wevar.argu := 'PSEUDO-EV';
					// wevar.nprt := ' ';
					// wevar.ruba := ' ';
					// wevar.mont := Pseudo_Mnt;
					// INSERT INTO paevar
					// (cdos,aamm,nmat,nbul,rubq,argu,nprt,ruba,mont,cuti)
					// VALUES (wevar.identreprise, wevar.aamm, wevar.nmat, wevar.nbul,
					// wevar.rubq, wevar.argu, wevar.nprt, wevar.ruba,
					// wevar.mont,'PSEU');
					//
					ElementVariableDetailMois ev = new ElementVariableDetailMois();
					ev.setIdEntreprise(Integer.valueOf(parameter.getDossier()));
					ev.setNmat(infoSalary.getComp_id().getNmat());
					ev.setNbul(nbul);
					ev.setNlig(maxNLigEV++);
					// ev.getComp_id().setAamm(monthOfPay);
					// ev.setNmat(infoSalary.getComp_id().getNmat());
					// ev.setNbul(nbul);
					ev.setAamm(monthOfPay);
					ev.setRubq(rubriqueConge);
					ev.setArgu(argu);
					ev.setNprt(nprt);
					ev.setRuba(ruba);
					ev.setMont(new BigDecimal(montant));
					//
					//service.save(ev);
					
					//Ajout dans la liste des EV deja chargés du salarié
					Object elvar[] = null;
						// map.put(crub, value)
						elvar = new Object[4];
						elvar[0] = ev.getMont();
						elvar[1] = ev.getArgu();
						elvar[2] = ev.getNprt();
						elvar[3] = ev.getRuba();
						if (!this.listOfEltvar.containsKey(ev.getRubq()))
							this.listOfEltvar.put(ev.getRubq(), new ArrayList<Object[]>());
						this.listOfEltvar.get(ev.getRubq()).add(elvar);
				}
				// ELSE
				else
				{
					if (listOfElementVariableDetailMois.size() > 0)
					{
						ElementVariableDetailMois o = new ElementVariableDetailMois();
						o = (ElementVariableDetailMois) listOfElementVariableDetailMois.get(0);
						// UPDATE paevar
						// SET mont = mont + Pseudo_Mnt
						// WHERE rowid = w_rowid;
						// service.updateFromTable("update ElementVariableDetailMois mont =
						// mont + " + montant +
						// " where argu = '" + o.getArgu() + "'" +
						// " identreprise = '" + o.getCdos() + "'" +
						// " nmat = '" + o.getNmat() + "'" +
						// " nprt = '" + o.getNprt() + "'" +
						// " ruba = '" + o.getRuba() + "'" +
						// " rubq = '" + o.getRubq() + "'" +
						// " nbul = " + o.getNbul());
						o.setMont(new BigDecimal(o.getMont().doubleValue() + montant));
						//service.update(o);
						// END IF;
						
						//MAJ du montant dans la liste des EV deja chargés du salarié
						Object elvar[] = null;
						// map.put(crub, value)
						elvar = new Object[4];
						elvar[0] = montant;
						elvar[1] = o.getArgu();
						elvar[2] = o.getNprt();
						elvar[3] = o.getRuba();
						if (!this.listOfEltvar.containsKey(o.getRubq()))
							this.listOfEltvar.put(o.getRubq(), new ArrayList<Object[]>());
						this.listOfEltvar.get(o.getRubq()).add(elvar);
					}
				}
			}
			catch (Exception e)
			{
				// logger
				e.printStackTrace();
			}
		}
	}
	
	public void generateEVAvenir(String pseudoRubrique, String rubriqueConge, String argu, String nprt, String ruba, double montant)
	{


	}

	/**
	 * vérifie si le salaire de ce salarié est bloqué ou pas
	 * 
	 * @return true ou false
	 */
	public boolean bulletinBloque()
	{
		// if('O' == this.getParameter().getGenfile()) addToOutputtext("\n"+"nmat : " +
		// this.getInfoSalary().getComp_id().getNmat());
//		Rhtblq oRhtblq = (Rhtblq) service.get(Rhtblq.class, new RhtblqPK(this.getParameter().getDossier(), this.getInfoSalary().getComp_id().getNmat(), 1));
//		if (oRhtblq != null)
//			return true;
		return false;
	}

	/**
	 * bloque ou débloque le bulletin d'un agent. si on veut bloquer et que l'agent existe déjé dans la table de blocage on met le code de blocage é 1. s'il
	 * n'est pas encore dans la table, on le crée.
	 */
	public void bloquerBulletin(boolean bloquer)
	{
//		try
//		{
//			Session session = service.getSession();
//			Transaction tx = null;
//			String queryString = null;
//			Integer resultat;
//			Rhtblq oRhtblq = null;
//			try
//			{
//				tx = session.beginTransaction();
//
//				if(bloquer)
//				{
//					oRhtblq = (Rhtblq) session.get(Rhtblq.class, new RhtblqPK(this.getParameter().getDossier(), this.getInfoSalary().getComp_id().getNmat(), 0));
//					if (oRhtblq != null)
//						session.delete(oRhtblq);
//
//					oRhtblq = (Rhtblq) session.get(Rhtblq.class, new RhtblqPK(this.getParameter().getDossier(), this.getInfoSalary().getComp_id().getNmat(), 1));
//					if (oRhtblq == null)
//					{
//						oRhtblq = new Rhtblq();
//						oRhtblq.setComp_id(new RhtblqPK(this.getParameter().getDossier(), this.getInfoSalary().getComp_id().getNmat(), 1));
//						session.save(oRhtblq);
//					}
//
//				}
//				else
//				{
//					oRhtblq = (Rhtblq) session.get(Rhtblq.class, new RhtblqPK(this.getParameter().getDossier(), this.getInfoSalary().getComp_id().getNmat(), 1));
//					if (oRhtblq != null)
//						session.delete(oRhtblq);
//
//					oRhtblq = (Rhtblq) session.get(Rhtblq.class, new RhtblqPK(this.getParameter().getDossier(), this.getInfoSalary().getComp_id().getNmat(), 0));
//					if (oRhtblq == null)
//					{
//						oRhtblq = new Rhtblq();
//						oRhtblq.setComp_id(new RhtblqPK(this.getParameter().getDossier(), this.getInfoSalary().getComp_id().getNmat(), 0));
//						session.save(oRhtblq);
//					}
//
//				}
//
//				tx.commit();
//			}
//			catch (Exception e)
//			{
//				if (tx != null)
//					tx.rollback();
//			}
//			finally
//			{
//				service.closeConnexion(session);
//			}
//
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
	}
	public void bulletinBloqueDebloquer(boolean blocked)
	{
//		Rhtblq oRhtblq = null;
//		if (blocked)
//		{// on veut bloquer
//			oRhtblq = new Rhtblq();
//			oRhtblq.setComp_id(new RhtblqPK(this.getParameter().getDossier(), this.getInfoSalary().getComp_id().getNmat(), 1));
//			service.saveOrUpdate(oRhtblq);
//
//			oRhtblq = (Rhtblq) service.get(Rhtblq.class, new RhtblqPK(this.getParameter().getDossier(), this.getInfoSalary().getComp_id().getNmat(), 0));
//			if (oRhtblq != null)
//			{
//				service.delete(oRhtblq);
//			}
//		}
//		else
//		{// on débloque un agent qui est bloqué
//			oRhtblq = (Rhtblq) service.get(Rhtblq.class, new RhtblqPK(this.getParameter().getDossier(), this.getInfoSalary().getComp_id().getNmat(), 1));
//			if (oRhtblq != null)
//			{
//				service.delete(oRhtblq);
//
//				oRhtblq = new Rhtblq();
//				oRhtblq.setComp_id(new RhtblqPK(this.getParameter().getDossier(), this.getInfoSalary().getComp_id().getNmat(), 0));
//				service.saveOrUpdate(oRhtblq);
//			}
//		}
	}

	public void bulletinBloqueDebloquerOld(boolean blocked)
	{
		// if('O' == this.getParameter().getGenfile()) addToOutputtext("\n"+">>bulletinBloqueDebloquer");
		// if('O' == this.getParameter().getGenfile()) addToOutputtext("\n"+"nmat : " +
		// this.getInfoSalary().getComp_id().getNmat());
//		Rhtblq oRhtblq = null;
//		if (blocked)
//		{// on veut bloquer
//			oRhtblq = (Rhtblq) service.get(Rhtblq.class, new RhtblqPK(this.getParameter().getDossier(), this.getInfoSalary().getComp_id().getNmat(), 0));
//			if (oRhtblq != null)
//			{
//				oRhtblq.getComp_id().setCode(1);
//				service.update(oRhtblq);
//			}
//			else
//			{
//				oRhtblq = new Rhtblq();
//				oRhtblq.setComp_id(new RhtblqPK(this.getParameter().getDossier(), this.getInfoSalary().getComp_id().getNmat(), 1));
//				service.save(oRhtblq);
//			}
//		}
//		else
//		{// on débloque un agent qui est bloqué
//			oRhtblq = (Rhtblq) service.get(Rhtblq.class, new RhtblqPK(this.getParameter().getDossier(), this.getInfoSalary().getComp_id().getNmat(), 1));
//			if (oRhtblq != null)
//			{
//				// if('O' == this.getParameter().getGenfile()) addToOutputtext("\n"+"...oRhtblq != null...............");
//				oRhtblq.getComp_id().setCode(0);
//				service.update(oRhtblq);
//				// service.delete(oRhtblq);
//			}
//		}
	}

	// ---------------------------------------------------------------------
	// -- Les bulletin sont valides quand on a calcule un hors-paie et que
	// -- l'on veut conserver l'avance conges qui a ete generee.
	// -- C'est a dire que pacalcul ne lancera pas pacalfic si le bulletin
	// -- et valide mais il calculera tout de meme un bulletin reel.
	// ---------------------------------------------------------------------
	// -- Entrees : Dossier Dossier de l'agent
	// -- Matricule Matricule de l'agent
	// -- Sortie : TRUE si bulletin valide, sinon FALSE.
	// ---------------------------------------------------------------------

	public boolean bulletinValide()
	{
		// BEGIN
		// SELECT 'X'
		// INTO Variable_Bidon
		// FROM pablq
		// WHERE identreprise = Dossier
		// AND nmat = Matricule
		// AND code = 2;
		// EXCEPTION
		// WHEN NO_DATA_FOUND THEN
		// RETURN FALSE;
		// WHEN OTHERS THEN
		// RETURN FALSE;
		// END;
		//
		// RETURN TRUE;

		try
		{
//			Rhtblq oRhtblq = (Rhtblq) service.get(Rhtblq.class, new RhtblqPK(this.getParameter().getDossier(), this.getInfoSalary().getComp_id().getNmat(), 2));
//			if (oRhtblq == null)
//			{
//				return false;
//			}
		}
		catch (DataAccessException e)
		{
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	public boolean validateBulletin(String action)
	{
//		if(StringUtils.notIn(action,"O,N") )
//			return false;
//		if(StringUtils.equals("O", action))
//		{
//			Rhtblq oRhtblq = new Rhtblq();
//			oRhtblq.setComp_id(new RhtblqPK(this.getParameter().getDossier(), this.getInfoSalary().getComp_id().getNmat(), 2));
//			service.saveOrUpdate(oRhtblq);
//		}
//		else
//		{
//			Rhtblq oRhtblq = (Rhtblq) service.get(Rhtblq.class, new RhtblqPK(this.getParameter().getDossier(), this.getInfoSalary().getComp_id().getNmat(), 2));
//			if (oRhtblq != null)
//				service.delete(oRhtblq);
//		}
		
		return true;
	}

	/**
	 * => destruction_fcal supprime toutes les info de ce salarié dans les tables CalculPaie et Rhtprcalc
	 * 
	 * @throws Exception
	 */
	public void deleteBulletin() throws Exception
	{
		// if('O' == this.getParameter().getGenfile()) addToOutputtext("\n"+">>deleteBulletin(1)");
		try
		{
			session = service.getSession();
			// if('O' == this.getParameter().getGenfile()) addToOutputtext("\n"+"...monthOfPay:" + monthOfPay);
			// IF retroactif THEN
			// DELETE FROM prcalc
			// WHERE identreprise = wpdos.identreprise
			// AND aamm = i_aamm
			// AND nmat = i_nmat
			// AND nbul = i_nbul
			// AND nlot = w_nlot;
			if (parameter.isUseRetroactif())
			{
				// service.deleteFromTable("delete from Rhtprcalc " +
				// " where identreprise = '" + parameter.getDossier() + "'" +
				// " and aamm = '" + new ClsDate( monthOfPay,
				// ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getYearAndMonth()
				// + "'" +
				// " and nmat = '" + infoSalary.getComp_id().getNmat() + "'" +
				// " and nbul = " + nbul +
				// " and nlot = " + nlot);
				session.createQuery(
						"delete from Rhtprcalc " + " where identreprise = '" + parameter.getDossier() + "'" + " and aamm = '" + new ClsDate(monthOfPay, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getYearAndMonth() + "'"
								+ " and nmat = '" + infoSalary.getComp_id().getNmat() + "'" + " and nbul = " + nbul + " and nlot = " + nlot).executeUpdate();
			}
			// ELSE
			else
				// DELETE FROM pacalc
				// WHERE identreprise = wpdos.identreprise
				// AND aamm = i_aamm
				// AND nmat = i_nmat
				// AND nbul = i_nbul;
				// service.deleteFromTable("delete from CalculPaie " +
				// " where identreprise = '" + parameter.getDossier() + "'" +
				// " and aamm = '" + new ClsDate( monthOfPay,
				// ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getYearAndMonth()
				// + "'" +
				// " and nmat = '" + infoSalary.getComp_id().getNmat() + "'" +
				// " and nbul = " + nbul);
				session.createQuery(
						"delete from CalculPaie " + " where identreprise = '" + parameter.getDossier() + "'" + " and aamm = '" + new ClsDate(monthOfPay, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getYearAndMonth() + "'"
								+ " and nmat = '" + infoSalary.getComp_id().getNmat() + "'" + " and nbul = " + nbul).executeUpdate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}
		finally
		{
			if (session.isOpen() || session.isConnected())
			{
				service.closeConnexion(session);
			}
		}

		// END IF;
	}

	/**
	 * => destruction_fcal supprime toutes les info de ce salarié dans les tables CalculPaie et Rhtprcalc
	 * 
	 * @throws Exception
	 */
	public void deleteBulletin(int nbul, long nlot, String monthOfPay) throws Exception
	{

		addToOutputtext("\n" + ">>deleteBulletin");

		addToOutputtext("\n" + "monthOfPay : " + monthOfPay);
		// IF retroactif THEN
		// DELETE FROM prcalc
		// WHERE identreprise = wpdos.identreprise
		// AND aamm = i_aamm
		// AND nmat = i_nmat
		// AND nbul = i_nbul
		// AND nlot = w_nlot;
		if (parameter.isUseRetroactif())
			service.deleteFromTable("delete from Rhtprcalc " + " where identreprise = '" + parameter.getDossier() + "'" + " and aamm = '"
					+ new ClsDate(monthOfPay, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getYearAndMonth() + "'" + " and nmat = '" + infoSalary.getComp_id().getNmat() + "'" + " and nbul = " + nbul + " and nlot = "
					+ nlot);
		// ELSE
		else
			// DELETE FROM pacalc
			// WHERE identreprise = wpdos.identreprise
			// AND aamm = i_aamm
			// AND nmat = i_nmat
			// AND nbul = i_nbul;
			service.deleteFromTable("delete from CalculPaie " + " where identreprise = '" + parameter.getDossier() + "'" + " and aamm = '"
					+ new ClsDate(monthOfPay, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getYearAndMonth() + "'" + " and nmat = '" + infoSalary.getComp_id().getNmat() + "'" + " and nbul = " + nbul);
		// END IF;
	}
	
	public void deleteElementsFictif() throws Exception
	{

		if (StringUtils.equals("O", parameter.getFictiveCalculus()))
		{
			addToOutputtext("\n" + ">>deleteBulletin");
			if(!this.bulletinValide())
			{
				service.deleteFromTable("delete from Rhtfic where identreprise = '" + parameter.getDossier() + "' and nmat = '" + infoSalary.getComp_id().getNmat() + "'" );
				
				
				String query = "DELETE FROM ElementVariableDetailMois WHERE identreprise = '" + parameter.getDossier() + "' AND aamm = '" + monthOfPay+ "' AND nmat = '" + infoSalary.getComp_id().getNmat() + "' AND rubq = '"
				+ fictiveParameter.getFictiveRubrique() + "' and argu='AUTO'";
				if(StringUtil.notEquals(ParameterUtil.formatRubrique, fictiveParameter.getFictiveRubrique()))
				{
					//System.out.println("Deleting avancge conge "+query);
					Session session = service.getSession();
					session.createQuery(query).executeUpdate();
					service.closeSession(session);
				}
			}
			// END IF;
		}
	}

	/**
	 * => destruction_fcal supprime toutes les info de ce salarié dans les tables CalculPaie et Rhtprcalc
	 * 
	 * @param monthOfPay
	 *            la période pour laquelle on veut supprimer le bulletin
	 * @throws Exception
	 */
	public void deleteBulletin(String monthOfPay) throws Exception
	{
		// if('O' == this.getParameter().getGenfile()) addToOutputtext("\n"+">>deleteBulletin");
		// IF retroactif THEN
		// DELETE FROM prcalc
		// WHERE identreprise = wpdos.identreprise
		// AND aamm = i_aamm
		// AND nmat = i_nmat
		// AND nbul = i_nbul
		// AND nlot = w_nlot;
		if (parameter.isUseRetroactif())
			service.deleteFromTable("delete from Rhtprcalc " + " where identreprise = '" + parameter.getDossier() + "'" + " and aamm = '"
					+ new ClsDate(monthOfPay, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getYearAndMonth() + "'" + " and nmat = '" + infoSalary.getComp_id().getNmat() + "'");
		// ELSE
		else
			// DELETE FROM pacalc
			// WHERE identreprise = wpdos.identreprise
			// AND aamm = i_aamm
			// AND nmat = i_nmat
			// AND nbul = i_nbul;
			service.deleteFromTable("delete from CalculPaie " + " where identreprise = '" + parameter.getDossier() + "'" + " and aamm = '"
					+ new ClsDate(monthOfPay, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getYearAndMonth() + "'" + " and nmat = '" + infoSalary.getComp_id().getNmat() + "'");
		// END IF;
	}

	/**
	 * Suppression de l'avance congé
	 */
	public void deleteAvanceConge2()
	{
		try
		{
			String query = "DELETE FROM ElementVariableDetailMois WHERE identreprise = '" + parameter.getDossier() + "' AND aamm = '" + parameter.getMonthOfPay() + "' AND nmat = '" + infoSalary.getComp_id().getNmat() + "' AND rubq = '"
					+ parameter.getRubriqueAvanceConge() + "'";
			//System.out.println(query);
			session = service.getSession();
			session.createQuery(query).executeUpdate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}
		finally
		{
			if (session.isOpen() || session.isConnected())
			{
				service.closeConnexion(session);
			}
		}
	}

	/**
	 * recherche la zone libre en fonction du code
	 * 
	 * @param codezone
	 * @return la zone libre ou une chaine vide si rien n'est trouvé
	 */
	public String rechercheZoneLibre(int codezone)
	{
		String zone_libre = "";
//		Rhtzonelibre oZonelibre = (Rhtzonelibre) service.get(Rhtzonelibre.class, new RhtzonelibrePK(parameter.getDossier(), infoSalary.getComp_id().getNmat(), codezone));
//		if (oZonelibre != null)
//		{
//			Rhpzonelibre oPZonelibre = (Rhpzonelibre) service.get(Rhpzonelibre.class, new RhpzonelibrePK(parameter.getDossier(), codezone));
//			if (oPZonelibre != null)
//			{
//				if ("L".equalsIgnoreCase(oPZonelibre.getTypezl()))
//					zone_libre = oZonelibre.getVallzl();
//
//				if ("M".equalsIgnoreCase(oPZonelibre.getTypezl()))
//					zone_libre = oZonelibre.getValmzl() == null ? "0" : String.valueOf(oZonelibre.getValmzl());
//
//				if ("T".equalsIgnoreCase(oPZonelibre.getTypezl()))
//					zone_libre = oZonelibre.getValtzl() == null ? "0" : String.valueOf(oZonelibre.getValtzl());
//
//				if ("D".equalsIgnoreCase(oPZonelibre.getTypezl()))
//					zone_libre = oZonelibre.getValdzl() == null ? "0" : String.valueOf(new SimpleDateFormat(this.getParameter().appDateFormat).format(oZonelibre.getValdzl()));
//			}
//		}
		return zone_libre;
	}

	public void deletePseudoEV()
	{
		try
		{
			session = service.getSession();

			// IF retroactif THEN
			// DELETE FROM pahevar
			// WHERE identreprise = wpdos.identreprise
			// AND nmat = wsal01.nmat
			// AND aamm = w_aamm
			// AND nbul = wsd_fcal1.nbul
			// AND argu = 'PSEUDO-EV';
			// ELSE
			// DELETE FROM paevar
			// WHERE identreprise = wpdos.identreprise
			// AND nmat = wsal01.nmat
			// AND aamm = w_aamm
			// AND nbul = wsd_fcal1.nbul
			// AND argu = 'PSEUDO-EV';
			// END IF;

			if (parameter.isUseRetroactif())
			{

				session.createQuery(
						"delete from Rhthevar where identreprise ='" + parameter.getDossier() + "'" + " and nmat = '" + infoSalary.getComp_id().getNmat() + "'" + " and aamm = '" + parameter.getMyMonthOfPay().getYearAndMonth()
								+ "'" + " and argu = 'PSEUDO-EV'" + " and nbul = " + parameter.getNumeroBulletin()).executeUpdate();
			}
			else
			{

				session.createQuery(
						"delete from ElementVariableDetailMois" + " where identreprise ='" + parameter.getDossier() + "'" + " and nmat = '" + infoSalary.getComp_id().getNmat() + "'" + " and aamm = '"
								+ parameter.getMyMonthOfPay().getYearAndMonth() + "'" + " and argu = 'PSEUDO-EV'" + " and nbul = " + parameter.getNumeroBulletin()).executeUpdate();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}
		finally
		{
			if (session.isOpen() || session.isConnected())
			{
				service.closeConnexion(session);
			}
		}
		// END IF;
	}

	public void deleteNonRetroFictif()
	{
		try
		{
			session = service.getSession();

			// DELETE FROM pafic
			// WHERE identreprise = wpdos.identreprise
			// AND nmat = wsal01.nmat;

			session.createQuery("delete from Rhtfic" + " where identreprise ='" + parameter.getDossier() + "'" + " and nmat = '" + infoSalary.getComp_id().getNmat() + "'").executeUpdate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}
		finally
		{
			if (session.isOpen() || session.isConnected())
			{
				service.closeConnexion(session);
			}
		}
		// END IF;
	}

	/**
	 * cherche une rubrique dans la table des rubriques en fonction de son code une fois la rubrique retrouvée, on va utiliser les valeurs des taux, montant et
	 * de base qui seront utilisées pour le calcule de la base du mois de paie =>
	 * 
	 * @param crub
	 * @return le wrapper de la rubrique, et null si rien n'est trouvé
	 */
	public ClsRubriqueClone findRubriqueClone(String crub)
	{
		if (crub == null || crub.trim().equals(""))
			return null;
		ClsRubriqueClone rubClone = null;
		// if(this.getParameter().isUseRetroactif()){
		// rubClone = ListOfAllRubriqueRetroMap.get(crub);
		// }
		// else{
		// rubClone = ListOfAllRubriqueMap.get(crub);
		// }
		rubClone = parameter.getListOfAllRubriqueMap().get(crub);
		return rubClone;
	}

	public void initMapOfConcPro()
	{
		int i = 0;
		mapOfConcPro.put("1", infoSalary.getNiv1());
		mapOfConcPro.put("2", infoSalary.getNiv2());
		mapOfConcPro.put("3", infoSalary.getNiv3());
		mapOfConcPro.put("8", infoSalary.getEqui());
		mapOfConcPro.put("11", infoSalary.getSexe());
		mapOfConcPro.put("21", infoSalary.getNato());
		mapOfConcPro.put("22", infoSalary.getSitf());
		if (infoSalary.getNbcj() != 0)
			i = infoSalary.getNbcj();
		mapOfConcPro.put("23", ClsStringUtil.formatNumber(i, "0000"));
		i = 0;
		if (infoSalary.getNbec() != 0)
			i = infoSalary.getNbec();
		mapOfConcPro.put("24", ClsStringUtil.formatNumber(i, "0000"));
		i = 0;
		if (infoSalary.getNbfe() != 0)
			i = infoSalary.getNbfe();
		mapOfConcPro.put("25", ClsStringUtil.formatNumber(infoSalary.getNbfe(), "0000"));
		i = 0;
		mapOfConcPro.put("27", infoSalary.getModp());
		mapOfConcPro.put("39", infoSalary.getCat());
		mapOfConcPro.put("40", infoSalary.getEch());
		mapOfConcPro.put("43", infoSalary.getGrad());
		mapOfConcPro.put("44", infoSalary.getFonc());
		mapOfConcPro.put("45", infoSalary.getAfec());
		if (infoSalary.getIndi() != 0)
			i = infoSalary.getIndi();
		mapOfConcPro.put("47", ClsStringUtil.formatNumber(infoSalary.getIndi(), "000000"));
		i = 0;
		mapOfConcPro.put("58", infoSalary.getHifo());
		mapOfConcPro.put("59", infoSalary.getZli1());
		mapOfConcPro.put("60", infoSalary.getZli2());
		mapOfConcPro.put("67", infoSalary.getTypc());
		mapOfConcPro.put("70", infoSalary.getRegi());
		mapOfConcPro.put("71", infoSalary.getZres());
		mapOfConcPro.put("72", infoSalary.getDmst());
		mapOfConcPro.put("73", infoSalary.getDmst());
		if (infoSalary.getNpie() != 0)
			i = infoSalary.getNpie();
		mapOfConcPro.put("73", ClsStringUtil.formatNumber(infoSalary.getNpie(), "0000"));
		i = 0;
	}

	public boolean majTableVirements()
	{
		String updateString = "update Rhtvrmtagent" + " set mntdb = 0, mntdvd = 0" + " where identreprise = '" + infoSalary.getComp_id().getCdos() + "'" + " and nmat = '" + infoSalary.getComp_id().getNmat() + "'";
		try
		{
			this.getService().updateFromTable(updateString);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * => concPro(String cle)
	 * 
	 * @param cle
	 * @return xxx
	 */
	public String concPro(String cle)
	{
		// if('O' == this.getParameter().getGenfile()) addToOutputtext("\n"+">>concPro");
		if (ClsObjectUtil.isNull(cle))
			return "";
		String codezone = cle.substring(1, 2);
		String char10 = "";

		// BEGIN
		// w_cle := a_cle1;
		// IF w_cle = 1 THEN
		// PA_CALCUL.char10 := PA_CALCUL.wsal01.niv1;
		if ("1".equals(cle))
			char10 = infoSalary.getNiv1();
		// ELSIF w_cle = 2 THEN
		// PA_CALCUL.char10 := PA_CALCUL.wsal01.niv2;
		else if ("2".equals(cle))
			char10 = infoSalary.getNiv2();
		// ELSIF w_cle = 3 THEN
		// PA_CALCUL.char10 := PA_CALCUL.wsal01.niv3;
		else if ("3".equals(cle))
			char10 = infoSalary.getNiv3();
		// ELSIF w_cle = 8 THEN
		// PA_CALCUL.char10 := PA_CALCUL.wsal01.equi;
		else if ("8".equals(cle))
			char10 = infoSalary.getEqui();
		// ELSIF w_cle = 11 THEN
		// PA_CALCUL.char10 := PA_CALCUL.wsal01.sexe;
		else if ("11".equals(cle))
			char10 = infoSalary.getSexe();
		// ELSIF w_cle = 21 THEN
		// PA_CALCUL.char10 := PA_CALCUL.wsal01.nato;
		else if ("21".equals(cle))
			char10 = infoSalary.getNato();
		// ELSIF w_cle = 22 THEN
		// PA_CALCUL.char10 := PA_CALCUL.wsal01.sitf;
		else if ("22".equals(cle))
			char10 = infoSalary.getSitf();
		// ELSIF w_cle = 23 THEN
		// PA_CALCUL.char10 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.nbcj,'9999'));
		else if ("23".equals(cle))
			char10 = ClsStringUtil.formatNumber(infoSalary.getNbcj(), "#");
		// ELSIF w_cle = 24 THEN
		// PA_CALCUL.char10 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.nbec,'9999'));
		else if ("24".equals(cle))
			char10 = ClsStringUtil.formatNumber(infoSalary.getNbec(), "#");
		// ELSIF w_cle = 25 THEN
		// PA_CALCUL.char10 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.nbfe,'9999'));
		else if ("25".equals(cle))
			char10 = ClsStringUtil.formatNumber(infoSalary.getNbfe(), "#");
		// ELSIF w_cle = 27 THEN
		// PA_CALCUL.char10 := PA_CALCUL.wsal01.modp;
		else if ("27".equals(cle))
			char10 = infoSalary.getModp();
		// ELSIF w_cle = 39 THEN
		// PA_CALCUL.char10 := PA_CALCUL.wsal01.cat;
		else if ("39".equals(cle))
			char10 = infoSalary.getCat();
		// ELSIF w_cle = 40 THEN
		// PA_CALCUL.char10 := PA_CALCUL.wsal01.ech;
		else if ("40".equals(cle))
			char10 = infoSalary.getEch();
		// ELSIF w_cle = 43 THEN
		// PA_CALCUL.char10 := PA_CALCUL.wsal01.grad;
		else if ("43".equals(cle))
			char10 = infoSalary.getGrad();
		// ELSIF w_cle = 44 THEN
		// PA_CALCUL.char10 := PA_CALCUL.wsal01.fonc;
		else if ("44".equals(cle))
			char10 = infoSalary.getFonc();
		// ELSIF w_cle = 45 THEN
		// PA_CALCUL.char10 := PA_CALCUL.wsal01.afec;
		else if ("45".equals(cle))
			char10 = infoSalary.getAfec();
		// ELSIF w_cle = 47 THEN
		// PA_CALCUL.char10 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.indi,'999999'));
		else if ("47".equals(cle))
			char10 = ClsStringUtil.formatNumber(infoSalary.getIndi(), "#");
		// ELSIF w_cle = 58 THEN
		// PA_CALCUL.char10 := PA_CALCUL.wsal01.hifo;
		else if ("58".equals(cle))
			char10 = infoSalary.getHifo();
		// ELSIF w_cle = 59 THEN
		// PA_CALCUL.char10 := PA_CALCUL.wsal01.zli1;
		else if ("59".equals(cle))
			char10 = infoSalary.getZli1();
		// ELSIF w_cle = 60 THEN
		// PA_CALCUL.char10 := PA_CALCUL.wsal01.zli2;
		else if ("60".equals(cle))
			char10 = infoSalary.getZli2();
		// ELSIF w_cle = 67 THEN
		// PA_CALCUL.char10 := PA_CALCUL.wsal01.typc;
		else if ("67".equals(cle))
			char10 = infoSalary.getTypc();
		// ELSIF w_cle = 70 THEN
		// PA_CALCUL.char10 := PA_CALCUL.wsal01.regi;
		else if ("70".equals(cle))
			char10 = infoSalary.getRegi();
		// ELSIF w_cle = 71 THEN
		// PA_CALCUL.char10 := PA_CALCUL.wsal01.zres;
		else if ("71".equals(cle))
			char10 = infoSalary.getZres();
		// ELSIF w_cle = 72 THEN
		// PA_CALCUL.char10 := PA_CALCUL.wsal01.dmst;
		else if ("72".equals(cle))
			char10 = infoSalary.getDmst();
		// ELSIF w_cle = 73 THEN
		// PA_CALCUL.char10 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.npie,'9999'));
		else if ("73".equals(cle))
			char10 = ClsStringUtil.formatNumber(infoSalary.getNpie(), "#");
		if (cle.compareTo("801") >= 0 && cle.compareTo("899") <= 0)
			char10 = rechercheZoneLibre(Integer.valueOf(codezone));
		// else
		// char10 = this.mapOfConcPro.get(cle);
		//
		return char10;
	}
	
	private void insererModuleExterne(ClsRubriqueClone rubriqueClone, String trtb)
	{
		String crub = rubriqueClone.getRubrique().getComp_id().getCrub();
		if(rubriqueClone.isElementVariableApply()) return;
		if(StringUtils.equalsIgnoreCase(rubriqueClone.getRubrique().getCalc(),"N")) return;
		if (this.valeursModuleExterne != null)
		{
			List<ClsValeurRubrique> val = this.valeursModuleExterne.get(crub);
			if (val != null)
			{
				for (ClsValeurRubrique curr : val)
				{
					if(curr.getAmount() == 0 && curr.getBase() == 0 && curr.getRates() == 0) continue;
					this.valeurRubriquePartage.setAmount(curr.getAmount());
					this.valeurRubriquePartage.setBase(curr.getBase());
					this.valeurRubriquePartage.setBasePlafonnee(curr.getBasePlafonnee());
					this.valeurRubriquePartage.setRates(curr.getRates());
					this.evparam.setArgu(curr.getArgu());
					this.evparam.setNprt("");
					this.evparam.setRuba("");
					rubriqueClone.insertIntoSalariesFile(this.getInfoSalary().getClas(), evparam.getNprt(), evparam.getRuba(), trtb);
					this.evparam.setArgu("");
					this.evparam.setNprt("");
					this.evparam.setRuba("");
				}
			}
		}
	}


	/**
	 * permet d'insérer une rubrique dans pacalc
	 * 
	 * @param rubriqueClone
	 * @param trtb
	 */
	
	private void insererUneRubriqueClone(ClsRubriqueClone rubriqueClone, String trtb)
	{
		// if('O' == this.getParameter().getGenfile()) addToOutputtext("\n"+">>insererUneRubriqueClone");
		rubriqueClone.insertIntoSalariesFile(this.getInfoSalary().getClas(), evparam.getNprt(), evparam.getRuba(), trtb);
		evparam.setArgu("");
		evparam.setNprt("");
		evparam.setRuba("");
	}

	/**
	 * PAIE AU NET : chargement montant rubrique saisie en net et soumises é l'ajustement
	 */
	public void chargementRubriqueSaisiesEnNetAjus()
	{
		// if('O' == this.getParameter().getGenfile()) addToOutputtext("\n"+">>chargementRubriqueSaisiesEnNetAjus");
		// IF w_paienet = 'O'
		// THEN
		// IF Mode_Test THEN pap_logins('Paie au net'); END IF;
		//
		// w_calcul := 'O';
		// nbiter := 0;
		// w_numcal := 0;
		//
		// IF rubajus_exist THEN
		// -- Chargement des montants (E.F. et E.V.),
		// -- rubriques ajustees pour le salarie en cours
		// W_Faitout := charg_ajus;
		// END IF;
		// IF rubnet_exist THEN
		// -- Chargement des montants (E.F. et E.V.),
		// -- rubriques en net pour le salarie en cours
		// W_Faitout := charg_net;
		// END IF;
		if ("O".equals(infoSalary.getPnet()))
		{
			this.getParameter().setCalcul("O");
			this.nbiter = 0;
			this.getParameter().setNumeroAjustementActuel(0);
			//
			if (this.getParameter().isRubriqueAdjustExist())
				chargementMontantDesRubriquesAdjustment();

			if (this.getParameter().isRubriqueNetExist())
				chargementMontantDesRubriquesNettes();
		}
		// @emmanuel:test
		// chargementMontantDesRubriquesAdjustment();
		// if(this.getParameter().isRubriqueNetExist())
		// chargementMontantDesRubriquesNettes();
	}

	/**
	 * => comp_net Comparaison du salaire net calcule avec le salaire avec le net théorique La variable w_calcul est mise a 'N' avant de lancer cette fonction
	 */
	public void comparerAvecNet()
	{
		// Increment NUMBER(2);
		//
		// w_rowid ROWID;
		//
		// CURSOR curs_ajus IS
		// SELECT rowid, crub, ajnu, NVL(mont,0)
		// FROM parubajus
		// WHERE NVL(ajnu,0) = w_numcal
		// AND sessionid = w_sessionid;

		// int nbiter = this.getParameter().getPaieAuNetMaxIteration();
		//
		// BEGIN
		// -- Nombre d'iteration en relation avec le max stocke dans RUBNET, T99
		// nbiter := nbiter + 1;
		nbiter++;
		//System.out.println("Iteration courante = "+nbiter);
		if(nbiter == 27)
		{
			//System.out.println("Ajout du break point");
		}
		
//		if (this.parameter.genfile== 'O')
//		{
//			String texte = StringUtils.toString(this) + "\n" + StringUtils.toString(this.getWorkTime()) + "\n---------------LISTE PARAMETRAGE POUR LE SALARIE"
//					+ StringUtils.toString(this.getParameter());
//			this.outputtext = StringUtils.EMPTY;
//
//			StringUtils.printOutObject(texte, parameter.getGenfilefolder() + File.separator  + this.getInfoSalary().getComp_id().getNmat() + "-Iter-"+nbiter+".txt");
//		}
		//System.out.println("Iteration courante  ="+this.nbiter);
		// this.getParameter().setPaieAuNetMaxIteration(nbiter);
		// IF Mode_Test THEN pap_logins('PNET: Iteration No ' || to_char(nbiter,
		// '999')); END IF;
		//
		// -- LH On ajoute au net calcule le montant a ecarter du net car la
		// -- la comparaison se fait avec le net_a_touver
		// Net_calcule := Net_calcule + NVL( A_ecarter_du_Net, 0);
		this.salaireNet += this.salaireAecarterDuNet;
		//
		// aff_net;
		//
		// --------------------------------------------------------------------------------
		// -- A-t-on trouve le meme net a la derniere ou avant la derniere
		// iteration ?
		// -- Si oui on boucle, donc on arrete de calculer (variable w_bouclage)
		// --------------------------------------------------------------------------------
		//
		// w_bouclage := 'N';
		this.getParameter().setBouclage("N");
		//
		// -- Ce calcul a donne le meme salaire net qu'au calcul precedent
		// IF Net_calcule = w_res1 OR Net_calcule = w_res2 THEN
		// w_bouclage := 'O';
		// END IF;
		if (this.salaireNet == this.salaireIterImpaire || this.salaireNet == this.salaireIterPaire)
			this.getParameter().setBouclage("O");
		//
		// -- Calcul de l'ecart entre le net calcule et le net a trouver
		// w_ecart := Net_a_trouver - Net_calcule;
		// if('O' == this.getParameter().getGenfile()) addToOutputtext("\n"+">> salaireNetATrouver :" + salaireNetATrouver);
		// if('O' == this.getParameter().getGenfile()) addToOutputtext("\n"+">> salaireNet :" + salaireNet);
		this.getParameter().setEcartSalaireNet(this.salaireNetATrouver - this.salaireNet);
		// IF Mode_Test THEN pap_logins('PNET: Net a trouver =' ||
		// to_char(Net_a_trouver)); END IF;
		// IF Mode_Test THEN pap_logins('PNET: Net calcule =' ||
		// to_char(Net_calcule)); END IF;
		// IF Mode_Test THEN pap_logins('PNET: Ecart =' || to_char(w_ecart));
		// END IF;
		//
		// -- LH 041297
		// -- SI l'ecart calcule est nul ALORS on a trouve
		// IF w_ecart = 0 THEN
		// RETURN TRUE;
		// END IF;
		if (this.getParameter().getEcartSalaireNet() == 0)
		{

			addToOutputtext("\n" + "Ecart de salaire net est nulle = " + this.getParameter().getEcartSalaireNet() + " donc on continue pas");
			//System.out.println("Ecart de salaire net est nulle");
			return;
		}

		this.parameter.setEcartSalaireNet(Math.abs(this.parameter.getEcartSalaireNet()));

		//
		// -- On prend la valeur absolue de l'ecart calcule
		// --IF w_ecart < 0 THEN
		// -- w_ecart := w_ecart * -1;
		// --END IF;
		// w_ecart := ABS( w_ecart );
		//
		// --------------------------------------------------------------------
		// -- Si on boucle avec un ecart superieur a l' ecart tolere
		// -- ou que l' on depasse le maximum d' iteration on garde ce resultat
		// --------------------------------------------------------------------
		// -- SI ( bouclage ET Ecart_calcule > Ecart_autorise ) OU
		// -- Nombre_iteration > Nombre_iteration_maximum
		// -- ALORS on_arrete_le_le_calcul
		// --------------------------------------------------------------------
		// -- LH 120298
		// --IF (w_bouclage = 'O' AND w_ecart > w_eca) OR nbiter > maxiter THEN
		// IF nbiter > maxiter THEN
		// RETURN TRUE;
		// END IF;
		if (nbiter > this.getParameter().getPaieAuNetMaxIteration())
		{

			addToOutputtext("\n" + "Atteinte du maximum d'iteration = " + this.getParameter().getPaieAuNetMaxIteration() + " donc on continue pas");
			//System.out.println("Atteinte du maximum d'iteration" );
			return;
		}
		//
		// ------------------------------------------------------------------
		// -- Bouclage ou pas on est < a l'ecart :
		// -- si pas de rubrique d' affectation, on garde ce resultat
		// ------------------------------------------------------------------
		// IF w_ecart <= w_eca
		// THEN
		// ParameterUtil.println("this.getParameter().getEcartSalaireNet() ="+this.getParameter().getEcartSalaireNet()+" et
		// this.getParameter().getPaieAuNetEcartMaximum() = "+this.getParameter().getPaieAuNetEcartMaximum()+ " et this.getParameter().getEcartSalaireNet() >=
		// this.getParameter().getPaieAuNetEcartMaximum() = "+(this.getParameter().getEcartSalaireNet() >= this.getParameter().getPaieAuNetEcartMaximum()));
		if (this.getParameter().getEcartSalaireNet() <= this.getParameter().getPaieAuNetEcartMaximum())
		{

			addToOutputtext("\n" + "Ecart de salaire net < é ecart maximum paramétré (" + this.getParameter().getEcartSalaireNet() + " <= " + this.getParameter().getPaieAuNetEcartMaximum());

			// w_ecart := Net_a_trouver - Net_calcule;
			this.getParameter().setEcartSalaireNet(this.salaireNetATrouver - this.salaireNet);
			// l := w_rubaff;
			ClsRubriqueClone rubriqueAffectationEcart = null;
			// IF w_rubaff != '0000' THEN
			try
			{
				if (!ParameterUtil.formatRubrique.equals(parameter.getPaieAuNetAffectationEcartRubrique()))
				{
					// IF w_rubaff < w_rubnet
					// THEN

					// BigDecimal bigEcartPositif = new BigDecimal(Math.abs(this.getParameter().getEcartSalaireNet()));
					// BigDecimal bigEcart = new BigDecimal(this.getParameter().getEcartSalaireNet());
					// t9999_mont(l) := t9999_mont(l) - w_ecart;
					// t9999_basc(l) := t9999_mont(l);
					// t9999_basp(l) := t9999_mont(l);
					// recherche de la rubrique dans la collection des rubriques
					// de ce salarié
					rubriqueAffectationEcart = this.findRubriqueClone(parameter.getPaieAuNetAffectationEcartRubrique());
					if (parameter.getPaieAuNetAffectationEcartRubrique().compareToIgnoreCase(parameter.getPaieAuNetRubrique()) < 0)
					{
						rubriqueAffectationEcart.setAmount(rubriqueAffectationEcart.getAmount() - this.getParameter().getEcartSalaireNet());
						rubriqueAffectationEcart.setBase(rubriqueAffectationEcart.getAmount());
						rubriqueAffectationEcart.setBasePlafonnee(rubriqueAffectationEcart.getAmount());
						//
						// IF retroactif THEN
						//
						// BEGIN
						// SELECT 'X' INTO char1 FROM prcalc
						// WHERE identreprise = wpdos.identreprise
						// AND aamm = w_aamm
						// AND nmat = wsal01.nmat
						// AND nbul = wsd_fcal1.nbul
						// AND rubq = w_rubaff
						// AND nlot = w_nlot;
						// EXCEPTION
						// WHEN NO_DATA_FOUND THEN
						// w_ecart := w_ecart * -1;
						// t9999_mont(l) := w_ecart;
						// t9999_basc(l) := w_ecart;
						// t9999_basp(l) := w_ecart;
						// wcalc.rubq := w_rubaff;
						// wcalc.basc := w_ecart;
						// wcalc.basp := w_ecart;
						// wcalc.taux := 0;
						// wcalc.mont := w_ecart;
						// wcalc.nprt := w_nprt;
						// wcalc.ruba := wevar.ruba;
						// wcalc.argu := w_argu;
						// derniere_ligne := derniere_ligne + 1;
						// INSERT INTO prcalc
						// (cdos,nmat,aamm,nbul,nlig,rubq,basc,basp,taux,mont,nprt,ruba,argu,clas,trtb,nlot)
						// VALUES (wcalc.identreprise, wsal01.nmat, wcalc.aamm,
						// wcalc.nbul, derniere_ligne,
						// wcalc.rubq, wcalc.basc, wcalc.basp, wcalc.taux,
						// wcalc.mont,
						// wcalc.nprt, wcalc.ruba, wcalc.argu, wcalc.clas,
						// wcalc.trtb,w_nlot);
						// END;
						// IF SQL%FOUND THEN
						// UPDATE prcalc SET mont = t9999_mont(l)
						// WHERE identreprise = wpdos.identreprise
						// AND aamm = w_aamm
						// AND nmat = wsal01.nmat
						// AND nbul = wsd_fcal1.nbul
						// AND rubq = w_rubaff
						// AND nlot = w_nlot;
						// END IF;
						// ELSE
						//
						// BEGIN
						// SELECT 'X' INTO char1 FROM pacalc
						// WHERE identreprise = wpdos.identreprise
						// AND aamm = w_aamm
						// AND nmat = wsal01.nmat
						// AND nbul = wsd_fcal1.nbul
						// AND rubq = w_rubaff;
						// EXCEPTION
						// WHEN NO_DATA_FOUND THEN
						// w_ecart := w_ecart * -1;
						// t9999_mont(l) := w_ecart;
						// t9999_basc(l) := w_ecart;
						// t9999_basp(l) := w_ecart;
						// wcalc.rubq := w_rubaff;
						// wcalc.basc := w_ecart;
						// wcalc.basp := w_ecart;
						// wcalc.taux := 0;
						// wcalc.mont := w_ecart;
						// wcalc.nprt := w_nprt;
						// wcalc.ruba := wevar.ruba;
						// wcalc.argu := w_argu;
						// derniere_ligne := derniere_ligne + 1;
						// INSERT INTO pacalc
						// (cdos,nmat,aamm,nbul,nlig,rubq,basc,basp,taux,mont,nprt,ruba,argu,clas,trtb)
						// VALUES (wcalc.identreprise, wsal01.nmat, wcalc.aamm,
						// wcalc.nbul, derniere_ligne,
						// wcalc.rubq, wcalc.basc, wcalc.basp, wcalc.taux,
						// wcalc.mont,
						// wcalc.nprt, wcalc.ruba, wcalc.argu, wcalc.clas,
						// wcalc.trtb);
						// END;
						//
						// IF SQL%FOUND THEN
						// UPDATE pacalc SET mont = t9999_mont(l)
						// WHERE identreprise = wpdos.identreprise
						// AND aamm = w_aamm
						// AND nmat = wsal01.nmat
						// AND nbul = wsd_fcal1.nbul
						// AND rubq = w_rubaff;
						// END IF;
						//
						// END IF;

						List l = null;
						if (this.getParameter().isUseRetroactif())
						{
							String queryStringRetro = "select nmat from Rhtprcalc " + " where identreprise = '" + parameter.getDossier() + "'" + " and aamm = '" + monthOfPay + "'"
									+ " and nmat = '" + infoSalary.getComp_id().getNmat() + "'" + " and nbul = " + nbul + " and rubq = '" + parameter.getPaieAuNetAffectationEcartRubrique() + "'"
									+ " and nlot = " + nlot;
							//
							l = service.find(queryStringRetro);
							if (l != null && l.size() > 0)
							{
								// update
								String queryStringUpdate = "update Rhtprcalc set mont = " + rubriqueAffectationEcart.getAmount() + " where identreprise = '" + parameter.getDossier() + "'" + " and aamm = '"
										+ monthOfPay + "'" + " and nmat = '" + infoSalary.getComp_id().getNmat() + "'" + " and nbul = " + nbul + " and rubq = '"
										+ parameter.getPaieAuNetAffectationEcartRubrique() + "'" + " and nlot = " + nlot;
								//
								session = service.getSession();
								session.createQuery(queryStringUpdate).executeUpdate();
								service.closeConnexion(session);
							}
							else
							{
								// insert
								// w_ecart := w_ecart * -1;
								// t9999_mont(l) := w_ecart;
								// t9999_basc(l) := w_ecart;
								// t9999_basp(l) := w_ecart;
								// wcalc.basc := w_ecart;
								// wcalc.basp := w_ecart;
								// wcalc.mont := w_ecart;
								// wcalc.rubq := w_rubaff;
								// wcalc.taux := 0;
								// wcalc.nprt := w_nprt;
								// wcalc.ruba := wevar.ruba;
								// wcalc.argu := w_argu;
								// derniere_ligne := derniere_ligne + 1;
								//
								// ClsRubriqueClone.setDerniereLigne(1 + ClsRubriqueClone.getDerniereLigne());
								this.getParameter().setEcartSalaireNet(-1 * this.getParameter().getEcartSalaireNet());
								rubriqueAffectationEcart.setAmount(this.getParameter().getEcartSalaireNet());
								rubriqueAffectationEcart.setBase(this.getParameter().getEcartSalaireNet());
								rubriqueAffectationEcart.setBasePlafonnee(this.getParameter().getEcartSalaireNet());

								this.incrementerDerniereLigne();
								/*Rhtprcalc oPrcalc = new Rhtprcalc();
								RhtprcalcPK pk = new RhtprcalcPK();
								pk.setAamm(this.getMonthOfPay());
								pk.setCdos(this.getParameter().getDossier());
								pk.setNbul(this.getNbul());
								pk.setNlig(this.derniereLigne);
								pk.setNlot(this.getNlot());
								pk.setNmat(this.getInfoSalary().getComp_id().getNmat());
								pk.setRubq(this.getParameter().getPaieAuNetAffectationEcartRubrique());
								oPrcalc.setArgu(evparam.getArgu());
								oPrcalc.setBasc(new BigDecimal(this.getParameter().getEcartSalaireNet()));
								oPrcalc.setBasp(new BigDecimal(this.getParameter().getEcartSalaireNet()));
								oPrcalc.setComp_id(pk);
								oPrcalc.setMont(new BigDecimal(this.getParameter().getEcartSalaireNet()));
								oPrcalc.setNprt(evparam.getNprt());
								oPrcalc.setRuba(evparam.getRuba());
								oPrcalc.setTaux(new BigDecimal(0));
								oPrcalc.setTrtb("1");
								//
								service.save(oPrcalc);
								*/

							}
						}
						else
						{
							String queryString1 = "select nmat from CalculPaie " + " where identreprise = '" + parameter.getDossier() + "'" + " and aamm = '" + monthOfPay + "'" + " and nmat = '"
									+ infoSalary.getComp_id().getNmat() + "'" + " and nbul = " + nbul + " and rubq = '" + parameter.getPaieAuNetAffectationEcartRubrique() + "'";
							//
							l = service.find(queryString1);
							if (l != null && l.size() > 0)
							{
								// update
								String queryStringUpdate = "update CalculPaie set mont = " + rubriqueAffectationEcart.getAmount() + " where identreprise = '" + parameter.getDossier() + "'" + " and aamm = '"
										+ monthOfPay + "'" + " and nmat = '" + infoSalary.getComp_id().getNmat() + "'" + " and nbul = " + nbul + " and rubq = '"
										+ parameter.getPaieAuNetAffectationEcartRubrique() + "'";
								//
								session = service.getSession();
								session.createQuery(queryStringUpdate).executeUpdate();
								service.closeConnexion(session);
							}
							else
							{
								// insert
								// w_ecart := w_ecart * -1;
								// t9999_mont(l) := w_ecart;
								// t9999_basc(l) := w_ecart;
								// t9999_basp(l) := w_ecart;
								// wcalc.basc := w_ecart;
								// wcalc.basp := w_ecart;
								// wcalc.mont := w_ecart;
								// wcalc.rubq := w_rubaff;
								// wcalc.taux := 0;
								// wcalc.nprt := w_nprt;
								// wcalc.ruba := wevar.ruba;
								// wcalc.argu := w_argu;
								// derniere_ligne := derniere_ligne + 1;
								// ClsRubriqueClone.setDerniereLigne(1 + ClsRubriqueClone.getDerniereLigne());

								this.getParameter().setEcartSalaireNet(-1 * this.getParameter().getEcartSalaireNet());
								rubriqueAffectationEcart.setAmount(this.getParameter().getEcartSalaireNet());
								rubriqueAffectationEcart.setBase(this.getParameter().getEcartSalaireNet());
								rubriqueAffectationEcart.setBasePlafonnee(this.getParameter().getEcartSalaireNet());

								this.incrementerDerniereLigne();
								CalculPaie oCalculPaie = new CalculPaie();
								oCalculPaie.setAamm(this.getMonthOfPay());
								oCalculPaie.setIdEntreprise(Integer.valueOf(this.getParameter().getDossier()));
								oCalculPaie.setNbul(this.getNbul());
								oCalculPaie.setNmat(this.getInfoSalary().getComp_id().getNmat());
								oCalculPaie.setRubq(this.getParameter().getPaieAuNetAffectationEcartRubrique());
								oCalculPaie.setArgu(evparam.getArgu());
								oCalculPaie.setBasc(new BigDecimal(this.getParameter().getEcartSalaireNet()));
								oCalculPaie.setBasp(new BigDecimal(this.getParameter().getEcartSalaireNet()));

								oCalculPaie.setMont(new BigDecimal(this.getParameter().getEcartSalaireNet()));
								oCalculPaie.setNprt(evparam.getNprt());
								oCalculPaie.setRuba(evparam.getRuba());
								oCalculPaie.setTaux(new BigDecimal(0));
								oCalculPaie.setTrtb("1");
								//
								service.save(oCalculPaie);
							}
						}
						//
					}
					// ELSE
					else
					{
						// t9999_mont(l) := w_ecart;
						// t9999_basc(l) := w_ecart;
						// t9999_basp(l) := w_ecart;
						// wcalc.rubq := w_rubaff;
						// wcalc.basc := w_ecart;
						// wcalc.basp := w_ecart;
						// wcalc.taux := 0;
						// wcalc.mont := w_ecart;
						// wcalc.nprt := w_nprt;
						// wcalc.ruba := wevar.ruba;
						// wcalc.argu := w_argu;
						// derniere_ligne := derniere_ligne + 1;
						//
						// IF retroactif THEN
						// INSERT INTO prcalc
						// (cdos,nmat,aamm,nbul,nlig,rubq,basc,basp,taux,mont,nprt,ruba,argu,clas,trtb,nlot)
						// VALUES (wcalc.identreprise, wsal01.nmat, wcalc.aamm,
						// wcalc.nbul, derniere_ligne,
						// wcalc.rubq, wcalc.basc, wcalc.basp, wcalc.taux,
						// wcalc.mont,
						// wcalc.nprt, wcalc.ruba, wcalc.argu, wcalc.clas,
						// wcalc.trtb,w_nlot);
						// ELSE
						// INSERT INTO pacalc
						// (cdos,nmat,aamm,nbul,nlig,rubq,basc,basp,taux,mont,nprt,ruba,argu,clas,trtb)
						// VALUES (wcalc.identreprise, wsal01.nmat, wcalc.aamm,
						// wcalc.nbul, derniere_ligne,
						// wcalc.rubq, wcalc.basc, wcalc.basp, wcalc.taux,
						// wcalc.mont,
						// wcalc.nprt, wcalc.ruba, wcalc.argu, wcalc.clas,
						// wcalc.trtb);
						// END IF;
						rubriqueAffectationEcart.setAmount(this.getParameter().getEcartSalaireNet());
						rubriqueAffectationEcart.setBase(this.getParameter().getEcartSalaireNet());
						rubriqueAffectationEcart.setBasePlafonnee(this.getParameter().getEcartSalaireNet());

						if (this.getParameter().isUseRetroactif())
						{
							// ClsRubriqueClone.setDerniereLigne(1 + ClsRubriqueClone.getDerniereLigne());
							this.incrementerDerniereLigne();
//							Rhtprcalc oPrcalc = new Rhtprcalc();
//							RhtprcalcPK pk = new RhtprcalcPK();
//							pk.setAamm(this.getMonthOfPay());
//							pk.setCdos(this.getParameter().getDossier());
//							pk.setNbul(this.getNbul());
//							pk.setNlig(this.derniereLigne);
//							pk.setNlot(this.getNlot());
//							pk.setNmat(this.getInfoSalary().getComp_id().getNmat());
//							pk.setRubq(this.getParameter().getPaieAuNetAffectationEcartRubrique());
//							oPrcalc.setArgu(evparam.getArgu());
//							oPrcalc.setBasc(new BigDecimal(this.getParameter().getEcartSalaireNet()));
//							oPrcalc.setBasp(new BigDecimal(this.getParameter().getEcartSalaireNet()));
//							oPrcalc.setComp_id(pk);
//							oPrcalc.setMont(new BigDecimal(this.getParameter().getEcartSalaireNet()));
//							oPrcalc.setNprt(evparam.getNprt());
//							oPrcalc.setRuba(evparam.getRuba());
//							oPrcalc.setTaux(new BigDecimal(0));
//							oPrcalc.setTrtb("1");
//							//
//							service.save(oPrcalc);
						}
						else
						{
							// ClsRubriqueClone.setDerniereLigne(1 + ClsRubriqueClone.getDerniereLigne());
							this.incrementerDerniereLigne();
							CalculPaie oCalculPaie = new CalculPaie();
							oCalculPaie.setAamm(this.getMonthOfPay());
							oCalculPaie.setIdEntreprise(Integer.valueOf(this.getParameter().getDossier()));
							oCalculPaie.setNbul(this.getNbul());
							//oCalculPaie.setNlig(this.derniereLigne);
							oCalculPaie.setNmat(this.getInfoSalary().getComp_id().getNmat());
							oCalculPaie.setRubq(this.getParameter().getPaieAuNetAffectationEcartRubrique());
							oCalculPaie.setArgu(evparam.getArgu());
							oCalculPaie.setBasc(new BigDecimal(this.getParameter().getEcartSalaireNet()));
							oCalculPaie.setBasp(new BigDecimal(this.getParameter().getEcartSalaireNet()));
							oCalculPaie.setMont(new BigDecimal(this.getParameter().getEcartSalaireNet()));
							oCalculPaie.setNprt(evparam.getNprt());
							oCalculPaie.setRuba(evparam.getRuba());
							oCalculPaie.setTaux(new BigDecimal(0));
							oCalculPaie.setTrtb("1");
							//
							service.save(oCalculPaie);
						}
						//
					}
					// END IF;
					//
					// -- LH 010498
					// l := w_rubnet;
					// t9999_mont(l) := Net_a_trouver - NVL( A_ecarter_du_Net,
					// 0);
					// t9999_basc(l) := Net_a_trouver - NVL( A_ecarter_du_Net,
					// 0);
					// t9999_basp(l) := Net_a_trouver - NVL( A_ecarter_du_Net,
					// 0);
					//
					// recherche de la rubrique dans la collection des rubriques
					// de ce salarié
					ClsRubriqueClone rubriqueNet = this.findRubriqueClone(parameter.getPaieAuNetRubrique());
					rubriqueNet.setAmount(this.salaireNetATrouver - this.salaireAecarterDuNet);
					rubriqueNet.setBase(this.salaireNetATrouver - this.salaireAecarterDuNet);
					rubriqueNet.setBasePlafonnee(this.salaireNetATrouver - this.salaireAecarterDuNet);
					
					// IF retroactif THEN
					// UPDATE prcalc
					// SET mont = t9999_mont(l),
					// basc = t9999_basc(l),
					// basp = t9999_basp(l)
					// WHERE identreprise = wpdos.identreprise
					// AND aamm = w_aamm
					// AND nmat = wsal01.nmat
					// AND nbul = wsd_fcal1.nbul
					// AND rubq = w_rubnet
					// AND nlot = w_nlot;
					// ELSE
					// UPDATE pacalc
					// SET mont = t9999_mont(l),
					// basc = t9999_basc(l),
					// basp = t9999_basp(l)
					// WHERE identreprise = wpdos.identreprise
					// AND aamm = w_aamm
					// AND nmat = wsal01.nmat
					// AND nbul = wsd_fcal1.nbul
					// AND rubq = w_rubnet;
					// END IF;

					/*
					 * if (this.getParameter().isUseRetroactif()) { String queryStringUpdate = "update Rhtprcalc " + " set mont = " +
					 * rubriqueAffectationEcart.getAmount() + " ,basc = " + rubriqueAffectationEcart.getBase() + " ,basp = " +
					 * rubriqueAffectationEcart.getBasePlafonnee() + " where identreprise = '" + parameter.getDossier() + "'" + " and aamm = '" +
					 * monthOfPay+ "'" + " and nmat = '" + infoSalary.getComp_id().getNmat() + "'" + " and nbul = " + nbul + " and rubq = '" +
					 * parameter.getPaieAuNetAffectationEcartRubrique() + "'" + " and nlot = " + this.getNlot(); //
					 * service.updateFromTable(queryStringUpdate); } else { String queryStringUpdate = "update CalculPaie " + " set mont = " +
					 * rubriqueAffectationEcart.getAmount() + " ,basc = " + rubriqueAffectationEcart.getBase() + " ,basp = " +
					 * rubriqueAffectationEcart.getBasePlafonnee() + " where identreprise = '" + parameter.getDossier() + "'" + " and aamm = '" +
					 * monthOfPay+ "'" + " and nmat = '" + infoSalary.getComp_id().getNmat() + "'" + " and nbul = " + nbul + " and rubq = '" +
					 * parameter.getPaieAuNetAffectationEcartRubrique() + "'"; // service.updateFromTable(queryStringUpdate); }
					 */

					if (this.getParameter().isUseRetroactif())
					{
						String queryStringUpdate = "update Rhtprcalc " + " set mont = " + rubriqueNet.getAmount() + " ,basc = " + rubriqueNet.getBase() + " ,basp = " + rubriqueNet.getBasePlafonnee()
								+ " where identreprise = '" + parameter.getDossier() + "'" + " and aamm = '" + monthOfPay + "'" + " and nmat = '" + infoSalary.getComp_id().getNmat() + "'"
								+ " and nbul = " + nbul + " and rubq = '" + parameter.getPaieAuNetRubrique() + "'" + " and nlot = " + this.getNlot();
						//
						session = service.getSession();
						session.createQuery(queryStringUpdate).executeUpdate();
						service.closeConnexion(session);
					}
					else
					{
						String queryStringUpdate = "update CalculPaie " + " set mont = " + rubriqueNet.getAmount() + " ,basc = " + rubriqueNet.getBase() + " ,basp = " + rubriqueNet.getBasePlafonnee()
								+ " where identreprise = '" + parameter.getDossier() + "'" + " and aamm = '" + monthOfPay + "'" + " and nmat = '" + infoSalary.getComp_id().getNmat() + "'"
								+ " and nbul = " + nbul + " and rubq = '" + parameter.getPaieAuNetRubrique() + "'";
						//

						this.addToOutputtext("\n" + "Requete de Mise é jour de la rubrique du net " + parameter.getPaieAuNetRubrique() + " " + queryStringUpdate);
						session = service.getSession();
						session.createQuery(queryStringUpdate).executeUpdate();
						service.closeConnexion(session);
					}
				}
			}
			catch (Exception e)
			{
				// logger
				e.printStackTrace();
			}
		}
		else
		{

			addToOutputtext("\n" + "Ecart de salaire net > é ecart maximum paramétré (" + this.getParameter().getEcartSalaireNet() + " > " + this.getParameter().getPaieAuNetEcartMaximum());

		}
		// END IF;
		//
		// -------------------------------------------------------------------------------
		// -- On ne boucle pas encore mais on est superieur a l' ecart tolere
		// -- Donc on continue le calcul (w_calcul := 'O')
		// -------------------------------------------------------------------------------
		// w_calcul := 'O';
		this.getParameter().setCalcul("O");
		//
		// -- stockage du dernier salaire net calcule pour detecter un bouclage
		// IF (nbiter mod 2) = 1 THEN
		// w_res1 := Net_calcule;
		// ELSE
		// w_res2 := Net_calcule;
		// END IF;
		if (nbiter % 2 == 1)
			this.salaireIterImpaire = this.salaireNet;
		else
			this.salaireIterPaire = this.salaireNet;
		//
		// -- Calcul du pourcentage d'ecart entre le net calcule et le net a
		// trouver
		// IF NOT PA_PAIE.NouZ(Net_a_trouver) THEN
		// w_pecart := ( Net_a_trouver - Net_calcule ) / Net_a_trouver;
		// END IF;
		if (this.salaireNetATrouver > 0)
			this.getParameter().setPourcentageEcartNet((this.salaireNetATrouver - this.salaireNet) / this.salaireNetATrouver);
		//
		// w_ecart := Net_a_trouver - Net_calcule;
		this.getParameter().setEcartSalaireNet(this.salaireNetATrouver - this.salaireNet);
		//
		// -- Le pourcentage est applique au brut pour trouver le montant de
		// -- majoration
		// -- w_majo := ROUND( ABS( Salaire_brut ) * w_pecart, 0);
		//
		// -- LH 120298
		// -- IF w_bouclage = 'O'
		// -- THEN
		// -- w_majo := w_ecart ;
		// -- END IF;
		//
		// -- Lecture du total de majoration deja affecte
		// SELECT SUM(mont) INTO rsa_tot FROM parubajus
		// WHERE NVL(ajnu,0) = w_numcal
		// AND sessionid = w_sessionid;
		//
		// rsa_tot := ROUND( NVL( rsa_tot, 0 ), 0);
		// -- w_faitout := ecr_lognet(' Compensation fiscale *' || rsa_tot ||
		// '*');
		List l = service.find("select sum(mont) from ElementSalaireAjus" + " where ajnu = " + this.getParameter().getNumeroAjustementActuel() + " and sessionid = '" + this.getParameter().getSessionId() + "'");
		double totalAjnu = 0;
		if (l != null && l.size() > 0 && !ClsObjectUtil.isNull(l.get(0)))
		{
			totalAjnu = ((BigDecimal) l.get(0)).doubleValue();
		}
		ParameterUtil.println("Total Ajutement = " + totalAjnu);
		//
		// IF nbiter = 1
		// THEN
		// IF w_ecart > 0
		// THEN
		// Sens_Positif := TRUE;
		// ELSE
		// Sens_Positif := FALSE;
		// END IF;
		// END IF;

		if (nbiter == 1)
		{
			sensPositif = (this.getParameter().getEcartSalaireNet() > 0) ? true : false;
		}
		//
		// -- LH 140298
		// IF Sens_Positif
		// THEN
		double majo = 0;
		if (sensPositif)
		{
			// IF w_ecart > 0
			// THEN
			if (this.getParameter().getEcartSalaireNet() > 0)
			{
				// -- Net_calcule < Net_a_trouver Il faut augmenter le montant
				// d'ajustement
				// -- w_faitout := ecr_lognet(' Net_a_trouver > Net_calcule' );
				// w_majo := TB_valajus(Cpt_valajus);
				if (this.getParameter().getTableOfAdjustmentValues() != null)
				{
					majo = this.getParameter().getTableOfAdjustmentValues()[this.compteurValeurAjus];
				}
			}
			// ELSE
			else
			{
				// -- Net_calcule > Net_a_trouver : Il faut diminuer le montant
				// d'ajustement
				// w_majo := - TB_valajus(Cpt_valajus);
				//
				// IF w_bouclage = 'O'
				// THEN
				// -- Increment := - 2;
				// Cpt_valajus := Cpt_valajus - 2;
				// ELSE
				// -- Increment := - 1;
				// Cpt_valajus := Cpt_valajus - 1;
				// END IF;
				//
				// -- Cpt_valajus := Cpt_valajus + Increment;
				//
				// IF Cpt_valajus <= 0 THEN Cpt_valajus := 1; END IF;
				// IF Cpt_valajus > Max_Nb_valajus THEN Cpt_valajus :=
				// Max_Nb_valajus; END IF;
				//
				// w_majo := w_majo + TB_valajus(Cpt_valajus);
				majo = -1 * this.getParameter().getTableOfAdjustmentValues()[this.compteurValeurAjus];
				this.compteurValeurAjus = ("O".equals(this.getParameter().getBouclage())) ? this.compteurValeurAjus - 2 : this.compteurValeurAjus - 1;
				if (this.compteurValeurAjus <= 0)
					this.compteurValeurAjus = 1;
				if (this.compteurValeurAjus > this.maxNbreValeurAjus)
					this.compteurValeurAjus = this.maxNbreValeurAjus;
				majo = majo + this.getParameter().getTableOfAdjustmentValues()[this.compteurValeurAjus];
			}
			//
			// END IF;
		}
		// ELSE
		else
		{
			// IF w_ecart < 0
			// THEN
			// -- Net_calcule < Net_a_trouver Il faut augmenter le montant
			// d'ajustement
			// -- w_faitout := ecr_lognet(' Net_a_trouver < Net_calcule' );
			// w_majo := - TB_valajus(Cpt_valajus);
			// ELSE
			// -- Net_calcule > Net_a_trouver : Il faut diminuer le montant
			// d'ajustement
			// w_majo := TB_valajus(Cpt_valajus);
			//
			// IF w_bouclage = 'O'
			// THEN
			// -- Increment := - 2;
			// Cpt_valajus := Cpt_valajus - 2;
			// ELSE
			// -- Increment := - 1;
			// Cpt_valajus := Cpt_valajus - 1;
			// END IF;
			//
			// -- Cpt_valajus := Cpt_valajus + Increment;
			//
			// IF Cpt_valajus <= 0 THEN Cpt_valajus := 1; END IF;
			// IF Cpt_valajus > Max_Nb_valajus THEN Cpt_valajus :=
			// Max_Nb_valajus; END IF;
			//
			// w_majo := w_majo - TB_valajus(Cpt_valajus);
			//
			// END IF;
			if (this.getParameter().getEcartSalaireNet() < 0)
			{
				majo = -1 * this.getParameter().getTableOfAdjustmentValues()[this.compteurValeurAjus];
			}
			else
			{
				// majo = -1 * this.getParameter().getTableOfAdjustmentValues()[this.compteurValeurAjus];
				majo = this.getParameter().getTableOfAdjustmentValues()[this.compteurValeurAjus];
				this.compteurValeurAjus = ("O".equals(this.getParameter().getBouclage())) ? this.compteurValeurAjus - 2 : this.compteurValeurAjus - 1;
				if (this.compteurValeurAjus <= 0)
					this.compteurValeurAjus = 1;
				if (this.compteurValeurAjus > this.maxNbreValeurAjus)
					this.compteurValeurAjus = this.maxNbreValeurAjus;
				majo = majo - this.getParameter().getTableOfAdjustmentValues()[this.compteurValeurAjus];
			}


		}
		// END IF;
		//
		// majo_rest := w_majo;
		double majoReste = majo;
		//
		// ---------------------------------------------------------------------
		// -- On reparti le montant de la majoration totale sur les rubriques
		// -- soumises a ajustement
		// ---------------------------------------------------------------------
		// OPEN curs_ajus;
		// LOOP
		// FETCH curs_ajus INTO w_rowid, wrubajus.crub,
		// wrubajus.ajnu, wrubajus.mont;
		// EXIT WHEN curs_ajus%NOTFOUND;
		//
		// IF rsa_tot <> 0 THEN
		// w_pmajo := ABS( wrubajus.mont / rsa_tot );
		// mon_majo := ROUND(w_majo * w_pmajo, 0);
		// ELSE
		// w_pmajo := wrubajus.mont;
		// mon_majo := w_majo;
		// END IF;
		//
		// UPDATE parubajus
		// SET mont = mont + mon_majo
		// WHERE rowid = w_rowid;
		//
		// majo_rest := NVL(majo_rest, 0) - NVL(mon_majo, 0);
		//
		// IF majo_rest <= 0 THEN
		// EXIT;
		// END IF;
		//
		// END LOOP;
		// CLOSE curs_ajus;

		String queryString = "from ElementSalaireAjus" + " where sessionid = " + parameter.getSessionId() + " and ajnu is not null" + " and ajnu = " + this.getParameter().getNumeroAjustementActuel();

		l = service.find(queryString);
		double pourcentageMajo = 0;
		double monMajo = 0;
		ElementSalaireAjus ruba = null;
		for (Object obj : l)
		{
			ruba = (ElementSalaireAjus) obj;
			if (totalAjnu != 0)
			{
				pourcentageMajo = Math.abs(ruba.getMont().doubleValue() / totalAjnu);
				monMajo = Math.round(majo * pourcentageMajo);
			}
			else
			{
				pourcentageMajo = ruba.getMont().doubleValue();
				monMajo = majo;
			}
			// Faire le update
			try
			{
				service.updateFromTable("update ElementSalaireAjus set mont = mont + " + monMajo + " where crub =" + ruba.getCrub() + " and ajnu = " + ruba.getAjnu() + " and sessionid = " + ruba.getSessionId());
				// ruba.setMont(new BigDecimal(ruba.getMont().doubleValue() + monMajo));
				// service.update(ruba);
			}
			catch (Exception e)
			{
				// logger
				e.printStackTrace();
			}
			// PB: absence de clé dans la table ElementSalaireAjus
			majoReste -= monMajo;
			if (majoReste <= 0)
			{
				break;
			}
		}
		// RETURN TRUE;
	}

	/**
	 * => charg_infos_salh Chargement des infos historisees dans le cas d'un traitement retro
	 */
	public void chargerInfoSalaryHistorique()
	{
//		// if('O' == this.getParameter().getGenfile()) addToOutputtext("\n"+"<<<<<<<<<<<<<<<<<<<<<<<<<<chargerInfoSalaryHistorique<<<<<<<<<<<<<<<<<<<<<<<<<<");
//		String nmat = this.getInfoSalary().getComp_id().getNmat();
//		String queryString = "from Rhthsal" + " where identreprise = '" + this.getParameter().getDossier() + "'" + " and nmat = '" + nmat + "'" + " and nbul = " + this.getNbul() + " and aamm = '"
//				+ this.getMyMonthOfPay().getYearAndMonth() + "'";
//		// whsal PAHSAL%ROWTYPE;
//		// CURSOR C_HSAL IS
//		// SELECT * FROM pahsal
//		// WHERE identreprise = PA_CALCUL.wpdos.identreprise
//		// AND nmat = PA_CALCUL.wsal01.nmat
//		// AND nbul = PA_CALCUL.wsd_fcal1.nbul
//		// AND aamm = PA_CALCUL.w_aamm;
//		//
//		// w_val_lib PREVSAL.VALL%TYPE;
//		// w_val_mnt PREVSAL.VALM%TYPE;
//		// w_val_dat PREVSAL.VALD%TYPE;
//		// BEGIN
//		//
//		// OPEN C_HSAL;
//		// LOOP
//		Rhthsal histo = null;
//		String tmp = "";
//		Double tmpDouble = null;
//		Date tmpDate = null;
//		List l = service.find(queryString);
//		for (Object obj : l)
//		{
//			histo = (Rhthsal) obj;
//			// FETCH C_HSAL INTO whsal;
//			// EXIT WHEN C_HSAL%NOTFOUND;
//			//
//			// -- niveau 1
//			// IF whsal.colname = 'NIV1' THEN
//			// PA_CALCUL.wsal01.niv1 := substr(rtrim(ltrim(whsal.vall)),1,3);
//			// IF
//			// paf_EvenSallib(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_lib)
//			// THEN
//			// PA_CALCUL.wsal01.niv1 := substr(rtrim(ltrim(w_val_lib)),1,3);
//			// END IF;
//			if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "NIV1".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setNiv1(histo.getVall());
//				tmp = this.getUtilNomenclature().getLabelOfPreviousSalary(this.getParameter().getDossier(), nlot, histo.getComp_id().getColname().trim(), nmat);
//				if (!ClsObjectUtil.isNull(tmp))
//					this.getInfoSalary().setNiv1(tmp);
//			}
//			// ELSIF whsal.colname = 'NIV2' THEN
//			// PA_CALCUL.wsal01.niv2 := substr(rtrim(ltrim(whsal.vall)),1,3);
//			// IF
//			// paf_EvenSallib(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_lib)
//			// THEN
//			// PA_CALCUL.wsal01.niv2 := substr(rtrim(ltrim(w_val_lib)),1,3);
//			// END IF;
//			else if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "NIV2".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setNiv2(histo.getVall());
//				tmp = this.getUtilNomenclature().getLabelOfPreviousSalary(this.getParameter().getDossier(), nlot, histo.getComp_id().getColname().trim(), nmat);
//				if (!ClsObjectUtil.isNull(tmp))
//					this.getInfoSalary().setNiv2(tmp);
//			}
//			// ELSIF whsal.colname = 'NIV3' THEN
//			// PA_CALCUL.wsal01.niv3 := substr(rtrim(ltrim(whsal.vall)),1,8);
//			// IF
//			// paf_EvenSallib(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_lib)
//			// THEN
//			// PA_CALCUL.wsal01.niv3 := substr(rtrim(ltrim(w_val_lib)),1,8);
//			// END IF;
//			else if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "NIV3".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setNiv3(histo.getVall());
//				tmp = this.getUtilNomenclature().getLabelOfPreviousSalary(this.getParameter().getDossier(), nlot, histo.getComp_id().getColname().trim(), nmat);
//				if (!ClsObjectUtil.isNull(tmp))
//					this.getInfoSalary().setNiv3(tmp);
//			}
//			// ELSIF whsal.colname = 'CAT' THEN
//			// PA_CALCUL.wsal01.cat :=
//			// RPAD(substr(rtrim(ltrim(whsal.vall)),1,3),3,' ');
//			// IF
//			// paf_EvenSallib(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_lib)
//			// THEN
//			// PA_CALCUL.wsal01.cat :=
//			// RPAD(substr(rtrim(ltrim(w_val_lib)),1,3),3,' ');
//			// END IF;
//			else if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "CAT".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setCat(histo.getVall() + "   ");
//				tmp = this.getUtilNomenclature().getLabelOfPreviousSalary(this.getParameter().getDossier(), nlot, histo.getComp_id().getColname().trim(), nmat);
//				if (!ClsObjectUtil.isNull(tmp))
//					this.getInfoSalary().setCat(tmp + "   ");
//			}
//			// ELSIF whsal.colname = 'ECH' THEN
//			// PA_CALCUL.wsal01.ech := substr(rtrim(ltrim(whsal.vall)),1,3);
//			// IF
//			// paf_EvenSallib(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_lib)
//			// THEN
//			// PA_CALCUL.wsal01.ech := substr(rtrim(ltrim(w_val_lib)),1,3);
//			// END IF;
//			else if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "ECH".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setEch(histo.getVall());
//				tmp = this.getUtilNomenclature().getLabelOfPreviousSalary(this.getParameter().getDossier(), nlot, histo.getComp_id().getColname().trim(), nmat);
//				if (!ClsObjectUtil.isNull(tmp))
//					this.getInfoSalary().setEch(tmp);
//			}
//			// ELSIF whsal.colname = 'REGI' THEN
//			// PA_CALCUL.wsal01.regi := substr(rtrim(ltrim(whsal.vall)),1,2);
//			// IF
//			// paf_EvenSallib(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_lib)
//			// THEN
//			// PA_CALCUL.wsal01.regi := substr(rtrim(ltrim(w_val_lib)),1,2);
//			// END IF;
//			else if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "REGI".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setRegi(histo.getVall());
//				tmp = this.getUtilNomenclature().getLabelOfPreviousSalary(this.getParameter().getDossier(), nlot, histo.getComp_id().getColname().trim(), nmat);
//				if (!ClsObjectUtil.isNull(tmp))
//					this.getInfoSalary().setRegi(tmp);
//			}
//			// ELSIF whsal.colname = 'FONC' THEN
//			// PA_CALCUL.wsal01.fonc := substr(rtrim(ltrim(whsal.vall)),1,4);
//			// IF
//			// paf_EvenSallib(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_lib)
//			// THEN
//			// PA_CALCUL.wsal01.fonc := substr(rtrim(ltrim(w_val_lib)),1,4);
//			// END IF;
//			else if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "FONC".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setFonc(histo.getVall());
//				tmp = this.getUtilNomenclature().getLabelOfPreviousSalary(this.getParameter().getDossier(), nlot, histo.getComp_id().getColname().trim(), nmat);
//				if (!ClsObjectUtil.isNull(tmp))
//					this.getInfoSalary().setFonc(tmp);
//			}
//			// ELSIF whsal.colname = 'GRAD' THEN
//			// PA_CALCUL.wsal01.grad := substr(rtrim(ltrim(whsal.vall)),1,3);
//			// IF
//			// paf_EvenSallib(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_lib)
//			// THEN
//			// PA_CALCUL.wsal01.grad := substr(rtrim(ltrim(w_val_lib)),1,3);
//			// END IF;
//			else if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "GRAD".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setGrad(histo.getVall());
//				tmp = this.getUtilNomenclature().getLabelOfPreviousSalary(this.getParameter().getDossier(), nlot, histo.getComp_id().getColname().trim(), nmat);
//				if (!ClsObjectUtil.isNull(tmp))
//					this.getInfoSalary().setGrad(tmp);
//			}
//			// ELSIF whsal.colname = 'CALS' THEN
//			// PA_CALCUL.wsal01.cals := substr(rtrim(ltrim(whsal.vall)),1,1);
//			// IF
//			// paf_EvenSallib(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_lib)
//			// THEN
//			// PA_CALCUL.wsal01.cals := substr(rtrim(ltrim(w_val_lib)),1,1);
//			// END IF;
//			else if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "CALS".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setCals(histo.getVall());
//				tmp = this.getUtilNomenclature().getLabelOfPreviousSalary(this.getParameter().getDossier(), nlot, histo.getComp_id().getColname().trim(), nmat);
//				if (!ClsObjectUtil.isNull(tmp))
//					this.getInfoSalary().setCals(tmp);
//			}
//			// ELSIF whsal.colname = 'CLAS' THEN
//			// PA_CALCUL.wsal01.clas := substr(rtrim(ltrim(whsal.vall)),1,2);
//			// IF
//			// paf_EvenSallib(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_lib)
//			// THEN
//			// PA_CALCUL.wsal01.clas := substr(rtrim(ltrim(w_val_lib)),1,2);
//			// END IF;
//			else if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "CLAS".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setClas(histo.getVall());
//				tmp = this.getUtilNomenclature().getLabelOfPreviousSalary(this.getParameter().getDossier(), nlot, histo.getComp_id().getColname().trim(), nmat);
//				if (!ClsObjectUtil.isNull(tmp))
//					this.getInfoSalary().setClas(tmp);
//			}
//			// ELSIF whsal.colname = 'SITF' THEN
//			// PA_CALCUL.wsal01.sitf := substr(rtrim(ltrim(whsal.vall)),1,1);
//			// IF
//			// paf_EvenSallib(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_lib)
//			// THEN
//			// PA_CALCUL.wsal01.sitf := substr(rtrim(ltrim(w_val_lib)),1,1);
//			// END IF;
//			else if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "SITF".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setSitf(histo.getVall());
//				tmp = this.getUtilNomenclature().getLabelOfPreviousSalary(this.getParameter().getDossier(), nlot, histo.getComp_id().getColname().trim(), nmat);
//				if (!ClsObjectUtil.isNull(tmp))
//					this.getInfoSalary().setSitf(tmp);
//			}
//			// ELSIF whsal.colname = 'NBEC' THEN
//			// PA_CALCUL.wsal01.nbec := NVL(whsal.valm,0);
//			// IF
//			// paf_EvenSalmnt(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_mnt)
//			// THEN
//			// PA_CALCUL.wsal01.nbec := NVL(w_val_lib,0);
//			// END IF;
//			else if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "NBEC".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setNbec(histo.getValm().intValueExact());
//				tempNumber = this.getUtilNomenclature().getAmountOrRateFromSalaryEventData(this.getParameter().getDossier(), nmat, nlot, histo.getComp_id().getColname().trim());
//				if (tempNumber != null)
//					this.getInfoSalary().setNbec(tempNumber.intValue());
//			}
//			// ELSIF whsal.colname = 'NBPT' THEN
//			// PA_CALCUL.wsal01.nbpt := NVL(whsal.valm,0);
//			// IF
//			// paf_EvenSalmnt(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_mnt)
//			// THEN
//			// PA_CALCUL.wsal01.nbpt := NVL(w_val_lib,0);
//			// END IF;
//			else if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "NBPT".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setNbpt(histo.getValm());
//				tempNumber = this.getUtilNomenclature().getAmountOrRateFromSalaryEventData(this.getParameter().getDossier(), nmat, nlot, histo.getComp_id().getColname().trim());
//				if (tempNumber != null)
//					this.getInfoSalary().setNbpt(new BigDecimal(tempNumber.doubleValue()));
//			}
//			// ELSIF whsal.colname = 'AFEC' THEN
//			// PA_CALCUL.wsal01.afec := substr(rtrim(ltrim(whsal.vall)),1,3);
//			// IF
//			// paf_EvenSallib(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_lib)
//			// THEN
//			// PA_CALCUL.wsal01.afec :=substr(rtrim(ltrim(w_val_lib)),1,3);
//			// END IF;
//			else if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "AFEC".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setAfec(histo.getVall());
//				tmp = this.getUtilNomenclature().getLabelOfPreviousSalary(this.getParameter().getDossier(), nlot, histo.getComp_id().getColname().trim(), nmat);
//				if (!ClsObjectUtil.isNull(tmp))
//					this.getInfoSalary().setAfec(tmp);
//			}
//			// ELSIF whsal.colname = 'ZLI1' THEN
//			// PA_CALCUL.wsal01.zli1 := substr(rtrim(ltrim(whsal.vall)),1,10);
//			// IF
//			// paf_EvenSallib(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_lib)
//			// THEN
//			// PA_CALCUL.wsal01.zli1 :=substr(rtrim(ltrim(w_val_lib)),1,10);
//			// END IF;
//			else if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "ZLI1".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setZli1(histo.getVall());
//				tmp = this.getUtilNomenclature().getLabelOfPreviousSalary(this.getParameter().getDossier(), nlot, histo.getComp_id().getColname().trim(), nmat);
//				if (!ClsObjectUtil.isNull(tmp))
//					this.getInfoSalary().setZli1(tmp);
//			}
//			// ELSIF whsal.colname = 'ZLI2' THEN
//			// PA_CALCUL.wsal01.zli2 := substr(rtrim(ltrim(whsal.vall)),1,10);
//			// IF
//			// paf_EvenSallib(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_lib)
//			// THEN
//			// PA_CALCUL.wsal01.zli2 :=substr(rtrim(ltrim(w_val_lib)),1,10);
//			// END IF;
//			else if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "ZLI2".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setZli2(histo.getVall());
//				tmp = this.getUtilNomenclature().getLabelOfPreviousSalary(this.getParameter().getDossier(), nlot, histo.getComp_id().getColname().trim(), nmat);
//				if (!ClsObjectUtil.isNull(tmp))
//					this.getInfoSalary().setZli2(tmp);
//			}
//			// ELSIF whsal.colname = 'DDCA' THEN
//			// PA_CALCUL.wsal01.ddca := whsal.vald;
//			// IF
//			// paf_EvenSaldat(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_dat)
//			// THEN
//			// PA_CALCUL.wsal01.ddca := w_val_dat;
//			// END IF;
//			else if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "DDCA".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setDdca(histo.getVald());
//				tmpDate = this.getUtilNomenclature().getDateOfPreviousSalary(this.getParameter().getDossier(), nlot, histo.getComp_id().getColname().trim(), nmat);
//				if (!ClsObjectUtil.isNull(tmpDate))
//					this.getInfoSalary().setDdca(tmpDate);
//			}
//			// ELSIF whsal.colname = 'DTES' THEN
//			// PA_CALCUL.wsal01.dtes := whsal.vald;
//			// IF
//			// paf_EvenSaldat(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_dat)
//			// THEN
//			// PA_CALCUL.wsal01.dtes := w_val_dat;
//			// END IF;
//			else if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "DTES".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setDtes(histo.getVald());
//				tmpDate = this.getUtilNomenclature().getDateOfPreviousSalary(this.getParameter().getDossier(), nlot, histo.getComp_id().getColname().trim(), nmat);
//				if (!ClsObjectUtil.isNull(tmpDate))
//					this.getInfoSalary().setDtes(tmpDate);
//			}
//			// ELSIF whsal.colname = 'TYPC' THEN
//			// PA_CALCUL.wsal01.typc := substr(rtrim(ltrim(whsal.vall)),1,1);
//			// IF
//			// paf_EvenSallib(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_lib)
//			// THEN
//			// PA_CALCUL.wsal01.typc :=substr(rtrim(ltrim(w_val_lib)),1,1);
//			// END IF;
//			else if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "TYPC".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setTypc(histo.getVall());
//				tmp = this.getUtilNomenclature().getLabelOfPreviousSalary(this.getParameter().getDossier(), nlot, histo.getComp_id().getColname().trim(), nmat);
//				if (!ClsObjectUtil.isNull(tmp))
//					this.getInfoSalary().setTypc(tmp);
//			}
//			// ELSIF whsal.colname = 'NATO' THEN
//			// PA_CALCUL.wsal01.nato := substr(rtrim(ltrim(whsal.vall)),1,4);
//			// IF
//			// paf_EvenSallib(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_lib)
//			// THEN
//			// PA_CALCUL.wsal01.nato :=substr(rtrim(ltrim(w_val_lib)),1,4);
//			// END IF;
//			else if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "NATO".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setNato(histo.getVall());
//				tmp = this.getUtilNomenclature().getLabelOfPreviousSalary(this.getParameter().getDossier(), nlot, histo.getComp_id().getColname().trim(), nmat);
//				if (!ClsObjectUtil.isNull(tmp))
//					this.getInfoSalary().setNato(tmp);
//			}
//			// ELSIF whsal.colname = 'CODS' THEN
//			// PA_CALCUL.wsal01.cods := substr(rtrim(ltrim(whsal.vall)),1,2);
//			// IF
//			// paf_EvenSallib(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_lib)
//			// THEN
//			// PA_CALCUL.wsal01.cods :=substr(rtrim(ltrim(w_val_lib)),1,2);
//			// END IF;
//			else if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "CODS".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setCods(histo.getVall());
//				tmp = this.getUtilNomenclature().getLabelOfPreviousSalary(this.getParameter().getDossier(), nlot, histo.getComp_id().getColname().trim(), nmat);
//				if (!ClsObjectUtil.isNull(tmp))
//					this.getInfoSalary().setCods(tmp);
//			}
//			// ELSIF whsal.colname = 'SNET' THEN
//			// PA_CALCUL.wsal01.snet := NVL(whsal.valm,0);
//			// IF
//			// paf_EvenSalmnt(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_mnt)
//			// THEN
//			// PA_CALCUL.wsal01.snet := NVL(w_val_mnt,0);
//			// END IF;
//			else if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "SNET".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setSnet(histo.getValm());
//				tempNumber = this.getUtilNomenclature().getAmountOrRateFromSalaryEventData(this.getParameter().getDossier(), nmat, nlot, histo.getComp_id().getColname().trim());
//				if (tempNumber != null)
//					this.getInfoSalary().setSnet(new BigDecimal(tempNumber.doubleValue()));
//			}
//			// IF PA_CALCUL.Mode_Test THEN pap_logins('*** salaire net
//			// ***'||TO_CHAR(PA_CALCUL.wsal01.snet)); END IF;
//			// ELSIF whsal.colname = 'AVN1' THEN
//			// PA_CALCUL.wsal01.avn1 := substr(rtrim(ltrim(whsal.vall)),1,2);
//			// IF
//			// paf_EvenSallib(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_lib)
//			// THEN
//			// PA_CALCUL.wsal01.avn1 :=substr(rtrim(ltrim(w_val_lib)),1,2);
//			// END IF;
//			else if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "AVN1".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setAvn1(histo.getVall());
//				tmp = this.getUtilNomenclature().getLabelOfPreviousSalary(this.getParameter().getDossier(), nlot, histo.getComp_id().getColname().trim(), nmat);
//				if (!ClsObjectUtil.isNull(tmp))
//					this.getInfoSalary().setAvn1(tmp);
//			}
//			// ELSIF whsal.colname = 'AVN2' THEN
//			// PA_CALCUL.wsal01.avn2 := substr(rtrim(ltrim(whsal.vall)),1,2);
//			// IF
//			// paf_EvenSallib(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_lib)
//			// THEN
//			// PA_CALCUL.wsal01.avn2 :=substr(rtrim(ltrim(w_val_lib)),1,2);
//			// END IF;
//			else if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "AVN2".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setAvn2(histo.getVall());
//				tmp = this.getUtilNomenclature().getLabelOfPreviousSalary(this.getParameter().getDossier(), nlot, histo.getComp_id().getColname().trim(), nmat);
//				if (!ClsObjectUtil.isNull(tmp))
//					this.getInfoSalary().setAvn2(tmp);
//			}
//			// ELSIF whsal.colname = 'AVN3' THEN
//			// PA_CALCUL.wsal01.avn3 := substr(rtrim(ltrim(whsal.vall)),1,2);
//			// IF
//			// paf_EvenSallib(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_lib)
//			// THEN
//			// PA_CALCUL.wsal01.avn3 :=substr(rtrim(ltrim(w_val_lib)),1,2);
//			// END IF;
//			else if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "AVN3".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setAvn3(histo.getVall());
//				tmp = this.getUtilNomenclature().getLabelOfPreviousSalary(this.getParameter().getDossier(), nlot, histo.getComp_id().getColname().trim(), nmat);
//				if (!ClsObjectUtil.isNull(tmp))
//					this.getInfoSalary().setAvn3(tmp);
//			}
//			// ELSIF whsal.colname = 'AVN4' THEN
//			// PA_CALCUL.wsal01.avn4 := substr(rtrim(ltrim(whsal.vall)),1,2);
//			// IF
//			// paf_EvenSallib(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_lib)
//			// THEN
//			// PA_CALCUL.wsal01.avn4 :=substr(rtrim(ltrim(w_val_lib)),1,2);
//			// END IF;
//			else if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "AVN4".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setAvn4(histo.getVall());
//				tmp = this.getUtilNomenclature().getLabelOfPreviousSalary(this.getParameter().getDossier(), nlot, histo.getComp_id().getColname().trim(), nmat);
//				if (!ClsObjectUtil.isNull(tmp))
//					this.getInfoSalary().setAvn4(tmp);
//			}
//			// ELSIF whsal.colname = 'AVN5' THEN
//			// PA_CALCUL.wsal01.avn5 := substr(rtrim(ltrim(whsal.vall)),1,2);
//			// IF
//			// paf_EvenSallib(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_lib)
//			// THEN
//			// PA_CALCUL.wsal01.avn5 :=substr(rtrim(ltrim(w_val_lib)),1,2);
//			// END IF;
//			else if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "AVN5".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setAvn5(histo.getVall());
//				tmp = this.getUtilNomenclature().getLabelOfPreviousSalary(this.getParameter().getDossier(), nlot, histo.getComp_id().getColname().trim(), nmat);
//				if (!ClsObjectUtil.isNull(tmp))
//					this.getInfoSalary().setAvn5(tmp);
//			}
//			// ELSIF whsal.colname = 'AVN6' THEN
//			// PA_CALCUL.wsal01.avn6 := substr(rtrim(ltrim(whsal.vall)),1,2);
//			// IF
//			// paf_EvenSallib(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_lib)
//			// THEN
//			// PA_CALCUL.wsal01.avn6 :=substr(rtrim(ltrim(w_val_lib)),1,2);
//			// END IF;
//			else if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "AVN6".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setAvn6(histo.getVall());
//				tmp = this.getUtilNomenclature().getLabelOfPreviousSalary(this.getParameter().getDossier(), nlot, histo.getComp_id().getColname().trim(), nmat);
//				if (!ClsObjectUtil.isNull(tmp))
//					this.getInfoSalary().setAvn6(tmp);
//			}
//			// ELSIF whsal.colname = 'AVN7' THEN
//			// PA_CALCUL.wsal01.avn7 := substr(rtrim(ltrim(whsal.vall)),1,2);
//			// IF
//			// paf_EvenSallib(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_lib)
//			// THEN
//			// PA_CALCUL.wsal01.avn7 :=substr(rtrim(ltrim(w_val_lib)),1,2);
//			// END IF;
//			else if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "AVN7".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setAvn7(histo.getVall());
//				tmp = this.getUtilNomenclature().getLabelOfPreviousSalary(this.getParameter().getDossier(), nlot, histo.getComp_id().getColname().trim(), nmat);
//				if (!ClsObjectUtil.isNull(tmp))
//					this.getInfoSalary().setAvn7(tmp);
//			}
//			// ELSIF whsal.colname = 'CODF' THEN
//			// PA_CALCUL.wsal01.codf := substr(rtrim(ltrim(whsal.vall)),1,2);
//			// IF
//			// paf_EvenSallib(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_lib)
//			// THEN
//			// PA_CALCUL.wsal01.codf :=substr(rtrim(ltrim(w_val_lib)),1,2);
//			// END IF;
//			else if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "CODF".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setCodf(histo.getVall());
//				tmp = this.getUtilNomenclature().getLabelOfPreviousSalary(this.getParameter().getDossier(), nlot, histo.getComp_id().getColname().trim(), nmat);
//				if (!ClsObjectUtil.isNull(tmp))
//					this.getInfoSalary().setCodf(tmp);
//			}
//			// ELSIF whsal.colname = 'JAPA' THEN
//			// PA_CALCUL.wsal01.japa := NVL(whsal.valm,0);
//			// IF
//			// paf_EvenSalmnt(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_mnt)
//			// THEN
//			// PA_CALCUL.wsal01.japa := NVL(w_val_mnt,0);
//			// END IF;
//			else if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "JAPA".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setJapa(histo.getValm());
//				tempNumber = this.getUtilNomenclature().getAmountOrRateFromSalaryEventData(this.getParameter().getDossier(), nmat, nlot, histo.getComp_id().getColname().trim());
//				if (tempNumber != null)
//					this.getInfoSalary().setJapa(new BigDecimal(tempNumber.doubleValue()));
//			}
//			// ELSIF whsal.colname = 'DAPA' THEN
//			// PA_CALCUL.wsal01.dapa := NVL(whsal.valm,0);
//			// IF
//			// paf_EvenSalmnt(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_mnt)
//			// THEN
//			// PA_CALCUL.wsal01.dapa := NVL(w_val_mnt,0);
//			// END IF;
//			else if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "DAPA".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setDapa(histo.getValm());
//				tempNumber = this.getUtilNomenclature().getAmountOrRateFromSalaryEventData(this.getParameter().getDossier(), nmat, nlot, histo.getComp_id().getColname().trim());
//				if (tempNumber != null)
//					this.getInfoSalary().setDapa(new BigDecimal(tempNumber.doubleValue()));
//			}
//			// ELSIF whsal.colname = 'JAPEC' THEN
//			// PA_CALCUL.wsal01.japec := NVL(whsal.valm,0);
//			// IF
//			// paf_EvenSalmnt(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_mnt)
//			// THEN
//			// PA_CALCUL.wsal01.japec := NVL(w_val_mnt,0);
//			// END IF;
//			else if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "JAPEC".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setJapec(histo.getValm());
//				tempNumber = this.getUtilNomenclature().getAmountOrRateFromSalaryEventData(this.getParameter().getDossier(), nmat, nlot, histo.getComp_id().getColname().trim());
//				if (tempNumber != null)
//					this.getInfoSalary().setJapec(new BigDecimal(tempNumber.doubleValue()));
//			}
//			// ELSIF whsal.colname = 'DAPEC' THEN
//			// PA_CALCUL.wsal01.dapec := NVL(whsal.valm,0);
//			// IF
//			// paf_EvenSalmnt(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_mnt)
//			// THEN
//			// PA_CALCUL.wsal01.dapec := NVL(w_val_mnt,0);
//			// END IF;
//			else if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "DAPEC".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setDapec(histo.getValm());
//				tempNumber = this.getUtilNomenclature().getAmountOrRateFromSalaryEventData(this.getParameter().getDossier(), nmat, nlot, histo.getComp_id().getColname().trim());
//				if (tempNumber != null)
//					this.getInfoSalary().setDapec(new BigDecimal(tempNumber.doubleValue()));
//			}
//			// ELSIF whsal.colname = 'JDED' THEN
//			// PA_CALCUL.wsal01.jded := NVL(whsal.valm,0);
//			// IF
//			// paf_EvenSalmnt(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_mnt)
//			// THEN
//			// PA_CALCUL.wsal01.jded := NVL(w_val_mnt,0);
//			// END IF;
//			else if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "JDED".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setJded(histo.getValm());
//				tempNumber = this.getUtilNomenclature().getAmountOrRateFromSalaryEventData(this.getParameter().getDossier(), nmat, nlot, histo.getComp_id().getColname().trim());
//				if (tempNumber != null)
//					this.getInfoSalary().setJded(new BigDecimal(tempNumber.doubleValue()));
//			}
//			// ELSIF whsal.colname = 'DDED' THEN
//			// PA_CALCUL.wsal01.dded := NVL(whsal.valm,0);
//			// IF
//			// paf_EvenSalmnt(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_mnt)
//			// THEN
//			// PA_CALCUL.wsal01.dded := NVL(w_val_mnt,0);
//			// END IF;
//			else if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "DDED".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setDded(histo.getValm());
//				tempNumber = this.getUtilNomenclature().getAmountOrRateFromSalaryEventData(this.getParameter().getDossier(), nmat, nlot, histo.getComp_id().getColname().trim());
//				if (tempNumber != null)
//					this.getInfoSalary().setDded(new BigDecimal(tempNumber.doubleValue()));
//			}
//			// ELSIF whsal.colname = 'NBJSA' THEN
//			// PA_CALCUL.wsal01.nbjsa := NVL(whsal.valm,0);
//			// IF
//			// paf_EvenSalmnt(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_mnt)
//			// THEN
//			// PA_CALCUL.wsal01.nbjsa := NVL(w_val_mnt,0);
//			// END IF;
//			else if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "NBJSA".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setNbjsa(histo.getValm());
//				tempNumber = this.getUtilNomenclature().getAmountOrRateFromSalaryEventData(this.getParameter().getDossier(), nmat, nlot, histo.getComp_id().getColname().trim());
//				if (tempNumber != null)
//					this.getInfoSalary().setNbjsa(new BigDecimal(tempNumber.doubleValue()));
//			}
//			// ELSIF whsal.colname = 'NBJSE' THEN
//			// PA_CALCUL.wsal01.nbjse := NVL(whsal.valm,0);
//			// IF
//			// paf_EvenSalmnt(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_mnt)
//			// THEN
//			// PA_CALCUL.wsal01.nbjse := NVL(w_val_mnt,0);
//			// END IF;
//			else if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "NBJSE".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setNbjse(histo.getValm());
//				tempNumber = this.getUtilNomenclature().getAmountOrRateFromSalaryEventData(this.getParameter().getDossier(), nmat, nlot, histo.getComp_id().getColname().trim());
//				if (tempNumber != null)
//					this.getInfoSalary().setNbjse(new BigDecimal(tempNumber.doubleValue()));
//			}
//			// ELSIF whsal.colname = 'DDCF' THEN
//			// PA_CALCUL.wsal01.ddcf := whsal.vald;
//			// IF
//			// paf_EvenSaldat(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_dat)
//			// THEN
//			// PA_CALCUL.wsal01.ddcf := w_val_dat;
//			// END IF;
//			else if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "DDCF".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setDdcf(histo.getVald());
//				tmpDate = this.getUtilNomenclature().getDateOfPreviousSalary(this.getParameter().getDossier(), nlot, histo.getComp_id().getColname().trim(), nmat);
//				if (!ClsObjectUtil.isNull(tmpDate))
//					this.getInfoSalary().setDdcf(tmpDate);
//			}
//			// ELSIF whsal.colname = 'DFCF' THEN
//			// PA_CALCUL.wsal01.dfcf := whsal.vald;
//			// IF
//			// paf_EvenSaldat(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_dat)
//			// THEN
//			// PA_CALCUL.wsal01.dfcf := w_val_dat;
//			// END IF;
//			else if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "DFCF".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setDfcf(histo.getVald());
//				tmpDate = this.getUtilNomenclature().getDateOfPreviousSalary(this.getParameter().getDossier(), nlot, histo.getComp_id().getColname().trim(), nmat);
//				if (!ClsObjectUtil.isNull(tmpDate))
//					this.getInfoSalary().setDfcf(tmpDate);
//			}
//			// ELSIF whsal.colname = 'PMCF' THEN
//			// PA_CALCUL.wsal01.pmcf := substr(rtrim(ltrim(whsal.vall)),1,6);
//			// IF
//			// paf_EvenSallib(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_lib)
//			// THEN
//			// PA_CALCUL.wsal01.pmcf :=substr(rtrim(ltrim(w_val_lib)),1,6);
//			// END IF;
//			else if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "PMCF".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setPmcf(histo.getVall());
//				tmp = this.getUtilNomenclature().getLabelOfPreviousSalary(this.getParameter().getDossier(), nlot, histo.getComp_id().getColname().trim(), nmat);
//				if (!ClsObjectUtil.isNull(tmp))
//					this.getInfoSalary().setPmcf(tmp);
//			}
//			// ELSIF whsal.colname = 'NBJCF' THEN
//			// PA_CALCUL.wsal01.nbjcf := NVL(whsal.valm,0);
//			// IF
//			// paf_EvenSalmnt(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_mnt)
//			// THEN
//			// PA_CALCUL.wsal01.nbjcf := NVL(w_val_mnt,0);
//			// END IF;
//			else if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "NBJCF".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setNbjcf(histo.getValm());
//				tempNumber = this.getUtilNomenclature().getAmountOrRateFromSalaryEventData(this.getParameter().getDossier(), nmat, nlot, histo.getComp_id().getColname().trim());
//				if (tempNumber != null)
//					this.getInfoSalary().setNbjcf(new BigDecimal(tempNumber.doubleValue()));
//			}
//			// ELSIF whsal.colname = 'NBJAF' THEN
//			// PA_CALCUL.wsal01.nbjaf := NVL(whsal.valm,0);
//			// IF
//			// paf_EvenSalmnt(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_mnt)
//			// THEN
//			// PA_CALCUL.wsal01.nbjaf := NVL(w_val_mnt,0);
//			// END IF;
//			else if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "NBJAF".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setNbjaf(histo.getValm());
//				tempNumber = this.getUtilNomenclature().getAmountOrRateFromSalaryEventData(this.getParameter().getDossier(), nmat, nlot, histo.getComp_id().getColname().trim());
//				if (tempNumber != null)
//					this.getInfoSalary().setNbjaf(new BigDecimal(tempNumber.doubleValue()));
//			}
//			// ELSIF whsal.colname = 'NBJTR' THEN
//			// PA_CALCUL.wsal01.nbjtr := NVL(whsal.valm,0);
//			// IF
//			// paf_EvenSalmnt(PA_CALCUL.w_nlot,whsal.colname,PA_CALCUL.wsal01.nmat,w_val_mnt)
//			// THEN
//			// PA_CALCUL.wsal01.nbjtr := NVL(w_val_mnt,0);
//			// END IF;
//			else if (!ClsObjectUtil.isNull(histo.getComp_id().getColname()) && "NBJTR".equals(histo.getComp_id().getColname().trim().toUpperCase()))
//			{
//				this.getInfoSalary().setNbjtr(histo.getValm());
//				tempNumber = this.getUtilNomenclature().getAmountOrRateFromSalaryEventData(this.getParameter().getDossier(), nmat, nlot, histo.getComp_id().getColname().trim());
//				if (tempNumber != null)
//					this.getInfoSalary().setNbjtr(new BigDecimal(tempNumber.doubleValue()));
//			}
//			// END IF;
//		}
//		// END LOOP;
	}

	/**
	 * => PA_ALGO.calc_nbjsup
	 * <p>
	 * Calcul du nombre de jours supplementaires
	 * 
	 * @return le nombre de jours supplementaires
	 */
	public double calculNombreDJourSuppl()
	{
		String identreprise = getInfoSalary().getComp_id().getCdos();
		int montant = 0;
		double taux1 = 0;
		double taux2 = 0;
		// nbr_an NUMBER(5);
		// int nbr_an = 0;
		// // an_s NUMBER(5);
		// int an_s = 0;
		// // mois_s NUMBER(5);
		// int mois_s = 0;
		// // nb_enf NUMBER(5);
		// int nb_enf = 0;
		// // nbj_par_enf NUMBER(5);
		// double nbj_par_enf = 0;
		// // l_tranche NUMBER(5,2);
		// double l_tranche = 0;
		// //
		// // nbj_sup NUMBER(5,2);
		// double nbj_sup = 0;
		//
		// BEGIN
		//
		// nbj_sup := 0;
		// nbj_deco := 0;
		//
		// -- Calcul des jours supp selon la categorie et
		// -- acquis tous les MNT3 annees
		montant = new Double((tempNumber = getUtilNomenclature().getAmountOrRateFromNomenclature(this.parameter.listOfTableXXMap,cdos, 35, getInfoSalary().getCat(), 3, getNlot(), getNbul(), getMonthOfPay(), getPeriodOfPay(),
				ClsEnumeration.EnColumnToRead.AMOUNT)) == null ? 0 : tempNumber.intValue()).intValue();

		taux1 = new Double((tempNumber = getUtilNomenclature().getAmountOrRateFromNomenclature(this.parameter.listOfTableXXMap,cdos, 35, getInfoSalary().getCat(), 1, getNlot(), getNbul(), getMonthOfPay(), getPeriodOfPay(),
				ClsEnumeration.EnColumnToRead.RATES)) == null ? 0 : tempNumber.intValue()).doubleValue();
		taux2 = new Double((tempNumber = getUtilNomenclature().getAmountOrRateFromNomenclature(this.parameter.listOfTableXXMap,cdos, 35, getInfoSalary().getCat(), 2, getNlot(), getNbul(), getMonthOfPay(), getPeriodOfPay(),
				ClsEnumeration.EnColumnToRead.RATES)) == null ? 0 : tempNumber.intValue()).doubleValue();
		//
		if (montant <= 0)
			montant = 1;
		//
		// l_tranche := (PA_CALCUL.ancien / wfnom.mnt3);
		double tranche = getAnciennete() / montant;
		// nbj_sup := l_tranche * wfnom.tau1;
		double nbreJourSuppl = tranche * taux1;
		// nbj_par_enf := wfnom.tau2;
		double nbreJourParEnfant = taux2;
		// -- Calcul du nombre de jours supp en fonction de l'anciennete
		String ancienS = ClsStringUtil.formatNumber(getAnciennete(), "00");
		int nbreEnfant = 0;
		//
		// wfnom.tau2 :=
		// paf_lecfnomT(34,char2,2,PA_CALCUL.w_aamm,PA_CALCUL.w_nlot,PA_CALCUL.wsd_fcal1.nbul);
		taux2 = new Double(
				(tempNumber = getUtilNomenclature().getAmountOrRateFromNomenclature(this.parameter.listOfTableXXMap,cdos, 34, ancienS, 2, getNlot(), getNbul(), getMonthOfPay(), getPeriodOfPay(), ClsEnumeration.EnColumnToRead.RATES)) == null ? 0
						: tempNumber.intValue()).doubleValue();
		//
		// nbj_sup := nbj_sup + wfnom.tau2;
		nbreJourSuppl = nbreJourSuppl + taux2;
		//
		// -- Calcul du nombre de jrs supp en fonction du nombre d'enfants
		// -- a charge, uniquement pour les femmes
		// IF PA_CALCUL.wsal01.sexe = 'F' THEN
		if ("F".equals(getInfoSalary().getSexe()))
		{
			// IF PA_CALCUL.comptage_enfant THEN
			// /*
			// SELECT COUNT(*) INTO nb_enf FROM paenfan
			// WHERE identreprise = PA_CALCUL.wpdos.identreprise
			// AND nmat = PA_CALCUL.wsal01.nmat
			// AND dtna > SYSDATE - ( age_max_enfant UNITS YEAR );
			// */
			// null;
			// ELSE
			// nb_enf := PA_CALCUL.wsal01.nbec;
			if (!getParameter().isEnfantComptage())
				nbreEnfant = getInfoSalary().getNbec();
			else
			{
				try
				{
					ClsDate sysdate = new ClsDate(new Date());
					String sdate = ClsStringUtil.formatNumber(sysdate.getDay(), "00") + "-" + ClsStringUtil.formatNumber(sysdate.getMonth(), "00") + "-"
							+ ClsStringUtil.formatNumber((sysdate.getYear() - parameter.enfantAgeMaximum), "0000");
					Date date_min = new ClsDate(sdate, "dd-MM-yyyy").getDate();
					
					String dtna = new ClsDate(date_min, parameter.getAppDateFormat()).getDateS();
					
					String query="select count(*) from Rhtenfantagent" + " where identreprise = '" + cdos + "'" + " and nmat = '" + getInfoSalary().getComp_id().getNmat() + "'" + " and achg = 'O'" + " and dtna > '" + dtna + "'";
					nbreEnfant = new Integer(service.find(query).get(0).toString());
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}			
			}
			// END IF;
			// nbj_sup := nbj_sup + (nbj_par_enf * nb_enf);
			nbreJourSuppl += nbreJourParEnfant * nbreEnfant;
		}
		// END IF;
		//
		// nbj_enf := NVL(nbj_par_enf,0) * NVL(nb_enf,0);
		nbreJourPourEnfant = nbreJourParEnfant * nbreEnfant;
		//
		// -- Calcul du nombre de jrs supp en fonction des decorations
		// nbj_deco := 0;
		int nbreJourDeco = 0;
		// SELECT SUM(valt) INTO nbj_deco from pahfnom
		// WHERE identreprise = PA_CALCUL.wpdos.identreprise
		// AND ctab = 18
		// AND nume = 1
		// AND cacc IN (SELECT UNIQUE cdis FROM padistin
		// WHERE identreprise = PA_CALCUL.wpdos.identreprise
		// AND nmat = PA_CALCUL.wsal01.nmat);
		// SELECT SUM(valt) INTO nbj_deco from pafnom
		// WHERE identreprise = PA_CALCUL.wpdos.identreprise
		// AND ctab = 18
		// AND nume = 1
		// AND cacc IN (SELECT UNIQUE cdis FROM padistin
		// WHERE identreprise = PA_CALCUL.wpdos.identreprise
		// AND nmat = PA_CALCUL.wsal01.nmat);
		List l = getParameter().isUseRetroactif() ? getService().find(
				"select coalesce(sum(valt),0) from Rhthfnom" + " where identreprise = '" + cdos + "'" + " and ctab = 18 " + " and nume = 1 " + " and cacc in (select distinct cdis from Rhtdistinctionagent "
						+ " where identreprise = '" + cdos + "'" + " and nmat = '" + getInfoSalary().getComp_id().getNmat() + "')") : getService().find(
				"select coalesce(sum(valt),0) from Rhfnom" + " where identreprise = '" + cdos + "'" + " and ctab = 18 " + " and nume = 1 " + " and cacc in (select distinct cdis from Rhtdistinctionagent "
						+ " where identreprise = '" + cdos + "'" + " and nmat = '" + getInfoSalary().getComp_id().getNmat() + "')");
		if (!ClsObjectUtil.isListEmty(l))
			nbreJourDeco = new BigDecimal(l.get(0).toString()).intValue();
		nbreJourSuppl += nbreJourDeco;
		return nbreJourSuppl;
	}

	/**
	 * =>paf_TypePaiement Retrouve le type de paiement é utiliser
	 * 
	 * @return le type de paiement
	 */
	public String getTypePaiement()
	{
		String typa = utilNomenclature.getLabelFromNomenclature(infoSalary.getComp_id().getCdos(), 54, infoSalary.getModp(), 4, nlot, nbul, moisPaieCourant, periodOfPay);
		//
		return typa;
	}

	/**
	 * 
	 * 
	 */
	public void annulation(HibernateConnexionService service, String cdos, String nmat, String aamm, Integer nbul)
	{
		String queryString = "from CumulPaie" + " where identreprise = '" + cdos + "'" + " and nmat = '" + nmat + "'" + " and aamm = '" + aamm + "'" + " and nbul = " + nbul;
		try
		{
			List<CumulPaie> l = service.find(queryString);

			CalculPaie calcul = null;
			Session session = service.getSession();
			Transaction tx = null;
			try
			{
				tx = session.beginTransaction();

				session.createSQLQuery(
						"delete from CalculPaie " + " where identreprise = '" + cdos + "'" + " and aamm = '" + new ClsDate(aamm, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getYearAndMonth() + "'" + " and nmat = '" + nmat
								+ "'" + " and nbul = " + nbul).executeUpdate();

				for (CumulPaie cum : l)
				{
					calcul = new CalculPaie();
					calcul.setNmat(nmat);
					calcul.setNbul(cum.getNbul());
					calcul.setAamm(this.getMonthOfPay());
					calcul.setTaux(cum.getTaux());
					calcul.setRubq(cum.getRubq());
					calcul.setBasp(cum.getBasp());
					calcul.setMont(cum.getMont().multiply(new BigDecimal(-1)));
					calcul.setTrtb("2");
					calcul.setClas(null);
					session.save(calcul);

					queryString = "Update CumulPaie set nbul = nbul *  " + (-1) + " where cdos='" + cdos + "' and nmat ='" + nmat + "'";
					queryString += " and nbul =" + nbul + " and rubq ='" + cum.getRubq() + "' and aamm ='" + aamm + "'";
					session.createSQLQuery(queryString).executeUpdate();

					queryString = "Update CumulPaie set mont = mont - " + cum.getMont() + " where cdos='" + cdos + "' and nmat ='" + nmat + "'";
					queryString += " and nbul =" + nbul + " and rubq ='" + cum.getRubq() + "' and aamm ='" + new ClsDate(aamm, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getYear() + "99'";
					session.createSQLQuery(queryString).executeUpdate();
				}

				tx.commit();
			}
			catch (Exception e)
			{
				if (tx != null)
					tx.rollback();
			}
			finally
			{
				service.closeConnexion(session);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void annulation()
	{
		//this.annulation(service, infoSalary.getComp_id().getCdos(), getInfoSalary().getComp_id().getNmat(), this.getMonthOfPay(), getParameter().getNumeroBulletin());

		// String queryString = "from CumulPaie" + " where identreprise = '" + infoSalary.getComp_id().getCdos() + "'" + " and nmat = '" +
		// getInfoSalary().getComp_id().getNmat() + "'" + " and aamm = '"
		// + this.getMonthOfPay() + "'" + " and nbul = " + getParameter().getNumeroBulletin();
		// try
		// {
		// List<CumulPaie> l = service.find(queryString);
		//
		// CalculPaie calcul = null;
		// CumulPaie cumul = null;
		// Session session = service.getSession();
		// Transaction tx = null;
		// try
		// {
		// tx = session.beginTransaction();
		//
		// session.createSQLQuery(
		// "delete from CalculPaie " + " where identreprise = '" + parameter.getDossier() + "'" + " and aamm = '" + new ClsDate(monthOfPay,
		// ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getYearAndMonth() + "'"
		// + " and nmat = '" + infoSalary.getComp_id().getNmat() + "'" + " and nbul = " + nbul).executeUpdate();
		//
		// for (CumulPaie CumulPaie : l)
		// {
		// calcul = new CalculPaie(new CalculPaiePK(infoSalary.getComp_id().getCdos(), getInfoSalary().getComp_id().getNmat(), this.getMonthOfPay(),
		// CumulPaie.getComp_id().getNbul(), 0, CumulPaie.getComp_id()
		// .getRubq()), CumulPaie.getBasc(), CumulPaie.getBasp(), CumulPaie.getTaux(), CumulPaie.getMont());
		// calcul.setTrtb("1");
		// calcul.setClas(infoSalary.getClas());
		// service.save(calcul);
		//
		// cumul = new CumulPaie();
		// BeanUtils.copyProperties(CumulPaie, cumul);
		// cumul.getComp_id().setNbul(-1 * CumulPaie.getComp_id().getNbul());
		//
		// service.delete(CumulPaie);
		// service.save(cumul);
		// // service.update(CumulPaie);
		//
		// // -- MAJ 99
		// // UPDATE pacumu SET mont = mont - wcumu.mont
		// // WHERE identreprise = wcumu.identreprise
		// // AND nmat = wcumu.nmat
		// // AND nbul = wcumu.nbul
		// // AND rubq = wcumu.rubq
		// // AND aamm = substr(wcumu.aamm,1,4)||'99';
		// queryString = "Update CumulPaie set mont = mont - " + cumul.getMont() + " where cdos='" + infoSalary.getComp_id().getCdos() + "' and nmat ='" +
		// infoSalary.getComp_id().getNmat() + "'";
		// queryString += " and nbul =" + parameter.numeroBulletin + " and rubq ='" + cumul.getComp_id().getRubq() + "' and aamm ='"
		// + new ClsDate(this.getMonthOfPay(), ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getYear() + "99'";
		// session.createSQLQuery(queryString).executeUpdate();
		// }
//		tx.commit();
		// }
		// catch (Exception e)
		// {
		// if (tx != null)
		// tx.rollback();
		// }
		// finally
		// {
		// service.closeConnexion(session);
		// }
		//
		// }
		// catch (Exception e)
		// {
		// e.printStackTrace();
		// }
	}

	/**
	 * construit un map qui contient les éléments variables d'un agent
	 */
	public void buildElementVarMap()
	{
		Map<String, List<Object[]>> map = new HashMap<String, List<Object[]>>();
		List listOfElementVariable = new ArrayList();
		// CURSOR curs_evar IS
		// SELECT mont, argu, nprt, ruba FROM paevar
		// WHERE identreprise = PA_CALCUL.wpdos.identreprise
		// AND nmat = PA_CALCUL.wsal01.nmat
		// AND aamm = PA_CALCUL.w_aamm
		// AND nbul = PA_CALCUL.wsd_fcal1.nbul
		// AND rubq = PA_CALCUL.t_rub.crub;
		//
		String queryString = "select rubq, mont, argu, nprt, ruba from ElementVariableDetailMois" + " where identreprise = '" + this.getParameter().getDossier() + "'" + " and nmat = '" + this.getInfoSalary().getComp_id().getNmat() + "'"
				+ " and aamm = '" + this.getMonthOfPay() + "'" + " and nbul = " + this.getNbul();
		
//		String queryString = "select rubq, sum(mont) as mont, argu, nprt, ruba from ElementVariableDetailMois" + " where identreprise = '" + this.getParameter().getDossier() + "'" + " and nmat = '" + this.getInfoSalary().getComp_id().getNmat() + "'"
//		+ " and aamm = '" + this.getMonthOfPay() + "'" + " and nbul = " + this.getNbul()+" group by rubq,argu,nprt,ruba";
		// CURSOR curs_evar2 IS
		// SELECT mont, argu, nprt, ruba FROM pahevar
		// WHERE identreprise = PA_CALCUL.wpdos.identreprise
		// AND nmat = PA_CALCUL.wsal01.nmat
		// AND aamm = PA_CALCUL.w_aamm
		// AND nbul = PA_CALCUL.wsd_fcal1.nbul
		// AND rubq = PA_CALCUL.t_rub.crub;
		String queryStringRetro = "select rubq, mont, argu, nprt, ruba from Rhthevar" + " where identreprise = '" + this.getParameter().getDossier() + "'" + " and nmat = '" + this.getInfoSalary().getComp_id().getNmat() + "'"
				+ " and aamm = '" + this.getMonthOfPay() + "'" + " and nbul = " + this.getNbul();
//		String queryStringRetro = "select rubq, sum(mont) as mont, argu, nprt, ruba from Rhthevar" + " where identreprise = '" + this.getParameter().getDossier() + "'" + " and nmat = '" + this.getInfoSalary().getComp_id().getNmat() + "'"
//		+ " and aamm = '" + this.getMonthOfPay() + "'" + " and nbul = " + this.getNbul()+" group by rubq,argu,nprt,ruba";
		
		//
		if (this.getParameter().isUseRetroactif())
			listOfElementVariable = this.getService().find(queryStringRetro);
		else
			listOfElementVariable = this.getService().find(queryString);
		//
		Object elvar[] = null;
		for (Object object : listOfElementVariable)
		{
			// map.put(crub, value)
			elvar = new Object[4];
			elvar[0] = ((Object[]) object)[1];
			elvar[1] = ((Object[]) object)[2];
			elvar[2] = ((Object[]) object)[3];
			elvar[3] = ((Object[]) object)[4];
			if (!map.containsKey((String) ((Object[]) object)[0]))
				map.put((String) ((Object[]) object)[0], new ArrayList<Object[]>());
			map.get((String) ((Object[]) object)[0]).add(elvar);
		}
		//
		listOfEltvar = map;
	}
	
	/**
	 * construit un map qui contient les éléments variables d'un agent
	 * 
	 */
	public void buildElementFixeMap()
	{
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();
		// BEGIN
		// SELECT monp INTO PA_CALCUL.w_transit FROM paelfix
		// WHERE identreprise = PA_CALCUL.wpdos.identreprise
		// AND nmat = PA_CALCUL.wsal01.nmat
		// AND codp = PA_CALCUL.t_rub.crub
		// AND (ddeb IS NULL OR
		// (ddeb IS NOT NULL AND ddeb <= TO_DATE(PA_CALCUL.w_aamm,'YYYYMM')))
		// AND (dfin IS NULL OR
		// (dfin IS NOT NULL AND dfin >= TO_DATE(PA_CALCUL.w_aamm,'YYYYMM')));
		// EXCEPTION
		// WHEN OTHERS THEN null;
		// END;
		String dateS = StringUtils.equals("O", parameter.useFirstDayForEltFix) ? parameter.getFirstDayOfMonthS() : parameter.getLastDayOfMonthS();
		String complexQuery = "select codp, monp from ElementFixeSalaire where identreprise = '" + this.getParameter().getDossier() + "'";
		complexQuery+= " and nmat = '" + this.getInfoSalary().getComp_id().getNmat() + "'";
		complexQuery+= " and (ddeb is null or ((ddeb is not null) and (ddeb <= '" + dateS + "')))";
		complexQuery+= " and (dfin is null or ((dfin is not null) and (dfin >= '" + dateS + "')))";
		// if('O' == this.getParameter().getGenfile()) addToOutputtext("\n"+"Query: " + complexQuery);
		List l = this.getService().find(complexQuery);
		//
		//
		for (Object object : l)
		{
			// map.put(crub, value)
			if (!map.containsKey((String) ((Object[]) object)[0]))
				map.put((String) ((Object[]) object)[0], new ArrayList<Object>());
			map.get((String) ((Object[]) object)[0]).add(((Object[]) object)[1]);
		}
		//
		listOfEltfix = map;
	}

	/**
	 * calcule les cumuls de cet agent et les met dans un map. En fait, elle permet d'optimiser le calcul proprement dit des cumuls car elle anticipe sur les
	 * données.
	 * 
	 */
	public Map<String, Map<String, Object[]>> cumulMap1PourRubrique = new HashMap<String, Map<String,Object[]>>();
	public Map<String, Map<String, Object[]>> cumulMap2PourRubrique = new HashMap<String, Map<String,Object[]>>();
	public Map<String, Map<String, Object[]>> cumulMap3PourRubrique = new HashMap<String, Map<String,Object[]>>();
	public Map<String, Map<String, Object[]>> cumulMap4PourRubrique = new HashMap<String, Map<String,Object[]>>();
	public void calculDesCumuls(String crub,Integer nbul, String wherePart, boolean computecumul)
	{
		String key=this.infoSalary.getComp_id().getNmat()+crub;
		cumul1 = new HashMap<String, Object[]>();
		cumul2 = new HashMap<String, Object[]>();
		cumul3 = new HashMap<String, Object[]>();
		cumul4 = new HashMap<String, Object[]>();
		//
		String nmat = infoSalary.getComp_id().getNmat();
		String identreprise = parameter.getDossier();

		String strSqlQuery1 = "select rubq, sum(mont), sum(taux), sum(basp) , aamm from CumulPaie " + " where identreprise = '" + cdos + "'" + " and nmat = '" + nmat
				+ "' and nbul != 0 and " + wherePart + " and ( mont != 0 or taux != 0 or basp != 0) group by aamm, rubq";

		String strSqlQuery2 = "select rubq, sum(mont), sum(taux), sum(basp) , aamm from Rhtprcumu " + " where identreprise = '" + cdos + "'" + " and nmat = '" + nmat
				+ "' and nbul != 0  and " + wherePart + " and ( mont != 0 or taux != 0 or basp != 0)  group by aamm, rubq";

		String strSqlQuery3 = "select rubq, sum(mont), sum(taux), sum(basp) , aamm from CumulPaie " + " where identreprise = '" + cdos + "'" + " and nmat = '" + nmat + "' and nbul = "
				+ nbul + " and " + wherePart + " and ( mont != 0 or taux != 0 or basp != 0)  group by aamm, rubq";

		String strSqlQuery4 = "select rubq, sum(mont), sum(taux), sum(basp) , aamm from Rhtprcumu " + " where identreprise = '" + cdos + "'" + " and nmat = '" + nmat + "' and nbul = "
				+ nbul + " and " + wherePart + " and ( mont != 0 or taux != 0 or basp != 0)  group by aamm, rubq";

		List ListOfRow = null;
		boolean retro = this.parameter.isUseRetroactif();
		boolean exist = false;
		if (computecumul)
		{
			
			if (nbul == 0)
			{
				if (!retro)
				{
					if(cumulMap1PourRubrique.containsKey(key))
					{
						cumul1 = cumulMap1PourRubrique.get(key);
						exist = true;
					}
				}

				if (retro)
				{
					if(cumulMap2PourRubrique.containsKey(key))
					{
						cumul2 = cumulMap2PourRubrique.get(key);
						exist = true;
					}
				}
			}
			else
			{

				if (!retro)
				{
					if(cumulMap3PourRubrique.containsKey(key))
					{
						cumul3 = cumulMap3PourRubrique.get(key);
						exist = true;
					}
				}

				if (retro)
				{
					if(cumulMap4PourRubrique.containsKey(key))
					{
						cumul4 = cumulMap4PourRubrique.get(key);
						exist = true;
					}
				}
			}
			
			if(exist) return;

			if (nbul == 0)
			{
				if (!retro)
				{

					addToOutputtext("\n" + "Query for cumul1 = " + strSqlQuery1);
					ListOfRow = service.find(strSqlQuery1);
					// on ne va prendre que la plus grande valeur
					for (Object object : ListOfRow)
					{
						// map.put(crub, value)
						cumul1.put((String) ((Object[]) object)[0] + (String) ((Object[]) object)[4], (Object[]) object);

					}
					cumulMap1PourRubrique.put(key, cumul1);
				}

				if (retro)
				{

					addToOutputtext("\n" + "Query for cumul2 = " + strSqlQuery2);
					ListOfRow = service.find(strSqlQuery2);
					// on ne va prendre que la plus grande valeur
					for (Object object : ListOfRow)
					{
						// map.put(crub, value)
						cumul2.put((String) ((Object[]) object)[0] + (String) ((Object[]) object)[4], (Object[]) object);
					}
					cumulMap2PourRubrique.put(key, cumul2);
				}
			}
			else
			{

				if (!retro)
				{

					addToOutputtext("\n" + "Query for cumul3 = " + strSqlQuery3);

					ListOfRow = service.find(strSqlQuery3);
					// on ne va prendre que la plus grande valeur
					for (Object object : ListOfRow)
					{
						// map.put(crub, value)
						cumul3.put((String) ((Object[]) object)[0] + (String) ((Object[]) object)[4], (Object[]) object);

					}
					cumulMap3PourRubrique.put(key, cumul3);
				}

				if (retro)
				{

					addToOutputtext("\n" + "Query for cumul4 = " + strSqlQuery4);
					ListOfRow = service.find(strSqlQuery4);
					// on ne va prendre que la plus grande valeur
					for (Object object : ListOfRow)
					{
						// map.put(crub, value)
						cumul4.put((String) ((Object[]) object)[0] + (String) ((Object[]) object)[4], (Object[]) object);
					}
					cumulMap4PourRubrique.put(key, cumul4);
				}
			}
		}
	}

	/**
	 * construit un map qui contient les lignes de préts d'un agent
	 */
	Map<String, List<Object>> listLignePretMap = null;

	public void buildLignePretMap()
	{
		listLignePretMap = new HashMap<String, List<Object>>();
		// CURSOR curs_preti IS SELECT lg FROM paprets
		// WHERE identreprise = wpdos.identreprise
		// AND nmat = wsal01.nmat
		// AND crub = t_rub.crub
		// AND premrb <= w_aamm
		// AND etatpr = 'D'
		// AND ( NVL(mtpr,0) != NVL(mtremb,0)
		// OR ( NVL(mtpr,0) = 0 AND
		// NVL(mtmens,0) != 0 )
		// )
		// ORDER BY lg;
		String complexQuery = "select crub, lg from Rhtpretsagent" + " where identreprise = '" + this.getParameter().getDossier() + "'" + " and nmat = '" + this.getInfoSalary().getComp_id().getNmat() + "'"
				+ " and premrb <= '" + this.getMonthOfPay() + "'" + " and etatpr = 'D'" + " and (((case when mtpr is null then 0 else mtpr end) != (case when mtremb is null then 0 else mtremb end))"
				+ " or (((case when mtpr is null then 0 else mtpr end) = 0) and ((case when mtmens is null then 0 else mtmens end) != 0)))" + " order by lg";
		List l = this.getService().find(complexQuery);
		//
		//
		for (Object object : l)
		{
			// map.put(crub, value)
			if (!listLignePretMap.containsKey((String) ((Object[]) object)[0]))
				listLignePretMap.put((String) ((Object[]) object)[0], new ArrayList<Object>());
			listLignePretMap.get((String) ((Object[]) object)[0]).add(((Object[]) object)[1]);
		}
	}

	/**
	 * construit un map qui contient les numéros de préts d'un agent
	 */
	Map<String, List<Object>> listNumeroPretMap = null;

	public void buildNumeroPretMap()
	{
		listNumeroPretMap = new HashMap<String, List<Object>>();
		//
		// CURSOR curs_pretx IS SELECT nprt FROM paprent
		// WHERE paprent.identreprise = wpdos.identreprise
		// AND paprent.nmat = wsal01.nmat
		// AND paprent.crub = t_rub.crub
		// -- AND paprent.per1 <= w_aamm
		// AND paprent.pact = 'O'
		// AND paprent.etpr = 'D'
		// AND paprent.resr != 0;
		String complexQuery = "select crub, nprt from Rhtpretent" + " where identreprise = '" + this.getParameter().getDossier() + "'" + " and nmat = '" + this.getInfoSalary().getComp_id().getNmat() + "'" +
		
		// " and per1 <= '" +
				// salary.getMyMonthOfPay().getDateS(ParameterUtil.SESSION_FORMAT_DATE)
				// + "'" +
				" and pact = 'O'" + " and etpr = 'D'" + " and resr != 0 order by crub";
		List l = this.getService().find(complexQuery);
		//
		for (Object object : l)
		{
			// map.put(crub, value)
			if (!listNumeroPretMap.containsKey((String) ((Object[]) object)[0]))
				listNumeroPretMap.put((String) ((Object[]) object)[0], new ArrayList<Object>());
			listNumeroPretMap.get((String) ((Object[]) object)[0]).add(((Object[]) object)[1]);
		}
	}

	/**
	 * construit un map qui contient les numéros de préts d'un agent
	 */
	Map<String, Object[]> listSpecifiqueCumul99Map11 = null;

	Map<String, Object> listSpecifiqueCumul99Map21 = null;

	Map<String, Object> listSpecifiqueCumul99Map31 = null;
	
	public Map<String, Map<String, Object[]>> cumulMap1PourPeriode = new HashMap<String, Map<String,Object[]>>();
	public Map<String, Map<String, Object>> cumulMap2PourPeriode = new HashMap<String, Map<String,Object>>();
	public Map<String, Map<String, Object>> cumulMap3PourPeriode = new HashMap<String, Map<String,Object>>();

	public void buildSpecifiqueCumul99Map(String debutPeriode, String finPeriode, String periode99)
	{
		String identreprise = this.getParameter().getDossier();
		String nmat = this.getInfoSalary().getComp_id().getNmat();
		HibernateConnexionService serv = this.getService();
		String queryString = StringUtils.EMPTY;
		String queryStringRetro = StringUtils.EMPTY;
		List l = null;
		
		String key = debutPeriode+finPeriode+periode99;
		
		Map<String, Object[]> listSpecifiqueCumul99Map1 = new HashMap<String, Object[]>();
		Map<String, Object> listSpecifiqueCumul99Map2 = new HashMap<String, Object>();
		Map<String, Object> listSpecifiqueCumul99Map3 = new HashMap<String, Object>();

		//Cas 1 : 
		if( ! cumulMap1PourPeriode.containsKey(key))
		{
			listSpecifiqueCumul99Map1 = new HashMap<String, Object[]>();
			
			queryString = "select rubq, sum(basc), sum(mont) from CumulPaie where identreprise = '" + cdos + "'";
			queryString+=" and nmat = '" + nmat + "' and aamm >= '" + debutPeriode + "'";
			queryString+="  and aamm <= '" + finPeriode + "'" + " and aamm != '" + periode99 + "'  and nbul != 0" + " group by  rubq";
	
			addToOutputtext("\n" + "..Query for map 1: " + queryString);
			queryStringRetro = queryString.replace("CumulPaie", "Rhtprcumu");
			
			l = (this.getParameter().isUseRetroactif()) ? serv.find(queryStringRetro) : serv.find(queryString);
	
			Object[] array = null;
	
			for (Object object : l)
			{
				array = new Object[2];
				array[0] = ((Object[]) object)[1];
				array[1] = ((Object[]) object)[2];
				listSpecifiqueCumul99Map1.put((String) ((Object[]) object)[0], array);
			}
			cumulMap1PourPeriode.put(key, listSpecifiqueCumul99Map1);
		}

		//Cas 2 : 
		if( ! cumulMap2PourPeriode.containsKey(key))
		{
			listSpecifiqueCumul99Map2 = new HashMap<String, Object>();
			
			queryString = "select rubq, sum(mont) from CumulPaie where identreprise = '" + cdos + "' and nmat = '" + nmat + "'" ;
			queryString+=" and aamm >= '" + debutPeriode + "'" + " and aamm <= '";
			queryString+= finPeriode + "'" + " and aamm != '" + periode99 + "'  and nbul != 0" + " group by  rubq";
	
			addToOutputtext("\n" + "..Query for map 2: " + queryString);
			queryStringRetro = queryString.replace("CumulPaie", "Rhtprcumu");
			l = (this.getParameter().isUseRetroactif()) ? serv.find(queryStringRetro) : serv.find(queryString);
			//
			for (Object object : l)
			{
				listSpecifiqueCumul99Map2.put((String) ((Object[]) object)[0], ((Object[]) object)[1]);
			}
			cumulMap2PourPeriode.put(key, listSpecifiqueCumul99Map2);
		}
		
		//Cas 2 : 
		if( ! cumulMap3PourPeriode.containsKey(key))
		{
			listSpecifiqueCumul99Map3 = new HashMap<String, Object>();
			
			queryString = "select rubq, count(*) from CumulPaie" + " where identreprise = '" + cdos + "'" + " and nmat = '" + nmat + "'";
			queryString+=" and aamm >= '" + debutPeriode + "'" + " and aamm < '" + finPeriode + "'" + " and aamm != '" + periode99 + "'";
			queryString+="  and nbul = 9" + " group by  rubq";
	
			addToOutputtext("\n" + "..Query for map 3: " + queryString);
			queryStringRetro = queryString.replace("CumulPaie", "Rhtprcumu");
			l = (this.getParameter().isUseRetroactif()) ? serv.find(queryStringRetro) : serv.find(queryString);
			//
			for (Object object : l)
			{
				listSpecifiqueCumul99Map3.put((String) ((Object[]) object)[0], ((Object[]) object)[1]);
			}
			cumulMap3PourPeriode.put(key, listSpecifiqueCumul99Map3);
		}
	}
	
	public void buildSpecifiqueCumul99MapOld()
	{
		listSpecifiqueCumul99Map11 = new HashMap<String, Object[]>();
		listSpecifiqueCumul99Map21 = new HashMap<String, Object>();
		listSpecifiqueCumul99Map31 = new HashMap<String, Object>();

		int fin_periode = 0;
		int yearAndMonthCumul = this.getOPeriod().getDebutPeriode();// this.getOPeriod().getAammExercice();
		int cum99 = yearAndMonthCumul / 100;
		cum99 = cum99 * 100 + 99;
		if (this.getMyMonthOfPay().getYearAndMonthInt() < this.getOPeriod().getFinPeriode())
			fin_periode = this.getMyMonthOfPay().getYearAndMonthInt();
		else
		{
			fin_periode = this.getOPeriod().getFinPeriode();
		}
		//
		// IF retroactif THEN
		// -- on exclu la periode de traitement (w_aamm)
		// IF aamm_cum < w_aamm AND fin_periode = w_aamm THEN
		// fin_periode := fin_periode - 1;
		// END IF;
		// END IF;
		if (this.getParameter().isUseRetroactif())
		{
			if ((yearAndMonthCumul < this.getMyMonthOfPay().getYearAndMonthInt()) && (fin_periode == this.getMyMonthOfPay().getYearAndMonthInt()))
				fin_periode--;
		}
		//
		// SELECT SUM(basc), SUM(mont) INTO cum_basc, cum_coti
		// FROM prcumu
		// WHERE identreprise = wpdos.identreprise
		// AND nmat = wsal01.nmat
		// AND aamm >= c_aamm_cum
		// AND aamm <= c_fin_periode
		// AND aamm != c_cum99
		// AND rubq = t_rub.rcon
		// AND nbul != 0;
		String yearAndMonthCumulS = ClsStringUtil.formatNumber(yearAndMonthCumul, "000000");
		String finPeriodeS = ClsStringUtil.formatNumber(fin_periode, "000000");
		String cum99S = ClsStringUtil.formatNumber(cum99, "000000");
		String queryString = "select rubq, sum(basc), sum(mont) from CumulPaie" + " where identreprise = '" + this.getParameter().getDossier() + "'" + " and nmat = '" + this.getInfoSalary().getComp_id().getNmat() + "'"
				+ " and aamm >= '" + yearAndMonthCumulS + "'" + " and aamm <= '" + finPeriodeS + "'" + " and aamm != '" + cum99S + "'" +
				 //" and rubq = '" +rubriqueRegul+ "'" +
				" and nbul != 0" + " group by  rubq";

		addToOutputtext("\n" + "..Query for map 1: " + queryString);
		String queryStringRetro = "select rubq, sum(basc), sum(mont) from Rhtprcumu" + " where identreprise = '" + this.getParameter().getDossier() + "'" + " and nmat = '" + this.getInfoSalary().getComp_id().getNmat()
				+ "'" + " and aamm >= '" + yearAndMonthCumulS + "'" + " and aamm <= '" + finPeriodeS + "'" + " and aamm != '" + cum99S + "'" +
				// " and rubq = '" + rubriqueRegul + "'" +
				" and nbul != 0" + " group by  rubq";
		List l = (this.getParameter().isUseRetroactif()) ? this.getService().find(queryStringRetro) : this.getService().find(queryString);
		//
		Object[] array = null;
		//
		for (Object object : l)
		{
			// map.put(crub, value)
			array = new Object[2];
			array[0] = ((Object[]) object)[1];
			array[1] = ((Object[]) object)[2];
			listSpecifiqueCumul99Map11.put((String) ((Object[]) object)[0], array);
		}
		//

		queryString = "select rubq, sum(mont) from CumulPaie" + " where identreprise = '" + this.getParameter().getDossier() + "'" + " and nmat = '" + this.getInfoSalary().getComp_id().getNmat() + "'" + " and aamm >= '"
				+ yearAndMonthCumulS + "'" + " and aamm <= '" + finPeriodeS + "'" + " and aamm != '" + cum99S + "'" +
				 //" and rubq = '" + rubriqueRegul + "'" +
				" and nbul != 0" + " group by  rubq";

		addToOutputtext("\n" + "..Query for map 2: " + queryString);
		queryStringRetro = "select rubq, sum(mont) from Rhtprcumu" + " where identreprise = '" + this.getParameter().getDossier() + "'" + " and nmat = '" + this.getInfoSalary().getComp_id().getNmat() + "'"
				+ " and aamm >= '" + yearAndMonthCumulS + "'" + " and aamm <= '" + finPeriodeS + "'" + " and aamm != '" + cum99S + "'" +
				 //" and rubq = '" + rubriqueRegul + "'" +
				" and nbul != 0" + " group by  rubq";
		l = (this.getParameter().isUseRetroactif()) ? this.getService().find(queryStringRetro) : this.getService().find(queryString);
		//
		for (Object object : l)
		{
			// map.put(crub, value)
			listSpecifiqueCumul99Map21.put((String) ((Object[]) object)[0], ((Object[]) object)[1]);
		}
		//
		queryString = "select rubq, count(*) from CumulPaie" + " where identreprise = '" + this.getParameter().getDossier() + "'" + " and nmat = '" + this.getInfoSalary().getComp_id().getNmat() + "'" + " and aamm >= '"
				+ yearAndMonthCumulS + "'" + " and aamm < '" + finPeriodeS + "'" + " and aamm != '" + cum99S + "'" +
				 //" and rubq = '" + rubriqueRegul+ "'" +
				" and nbul = 9" + " group by  rubq";

		addToOutputtext("\n" + "..Query for map 3: " + queryString);
		queryStringRetro = "select rubq, count(*) from Rhtprcumu" + " where identreprise = '" + this.getParameter().getDossier() + "'" + " and nmat = '" + this.getInfoSalary().getComp_id().getNmat() + "'"
				+ " and aamm >= '" + yearAndMonthCumulS + "'" + " and aamm < '" + finPeriodeS + "'" + " and aamm != '" + cum99S + "'" +
				 //" and rubq = '" +  rubriqueRegul + "'" +
				" and nbul = 9" + " group by  rubq";
		l = (this.getParameter().isUseRetroactif()) ? this.getService().find(queryStringRetro) : this.getService().find(queryString);
		//
		for (Object object : l)
		{
			// map.put(crub, value)
			listSpecifiqueCumul99Map31.put((String) ((Object[]) object)[0], ((Object[]) object)[1]);
		}
	}

	public void incrementerDerniereLigne()
	{
		this.derniereLigne++;
	}

	public ClsFictifSalaryToProcess convertToFictiveSalary()
	{
		ClsFictifSalaryWorkTime workTimeFictif = new ClsFictifSalaryWorkTime();
		BeanUtils.copyProperties(this.getWorkTime(), workTimeFictif);

		ClsFictifSalaryToProcess salaryToProcess = new ClsFictifSalaryToProcess(fictiveParameter, infoSalary, workTimeFictif);
		BeanUtils.copyProperties(this, salaryToProcess);

		fictiveParameter.setListOfAllRubriqueMap(this.parameter.getListOfAllRubriqueMap());

		fictiveParameter.setListOrdonneRubrique(this.parameter.getListOrdonneRubrique());

		fictiveParameter.buildListOfRubrique(salaryToProcess);
		
		//On construit é part la liste des ev fictif, car elle va étre modifiée lors du calcu fictif
		salaryToProcess.setListOfEltvar(new TreeMap<String, List<Object[]>>(this.listOfEltvar));


		return salaryToProcess;
	}

	// --------------------------------------------------------------------
	// -- Procedure de generation des acomptes
	// --------------------------------------------------------------------

	public void gen_acomptes()
	{
		// PROCEDURE gen_acomptes IS
		//
		// w_mnt PAEVAR.MONT%TYPE;
		// w_rowid ROWID;
		// w_count NUMBER;
		// w_mntefix PAELFIX.MONP%TYPE;
		// w_mntevar PAEVAR.MONT%TYPE;

		// CURSOR C_evars_1 IS
		// SELECT rowid FROM paevar
		// WHERE identreprise = wpdos.identreprise
		// AND nmat = wsal01.nmat
		// AND aamm = w_aamm
		// AND nbul = wsd_fcal1.nbul
		// AND rubq = w_rpaieacq;
		String queryEVCount = "Select count(*) From ElementVariableDetailMois where identreprise = :cdos and nmat = :nmat and aamm = :aamm and nbul = :nbul and rubq = :rubq";

		// CURSOR C_evars_2 IS
		// SELECT mont FROM paevar
		// WHERE identreprise = wpdos.identreprise
		// AND nmat = wsal01.nmat
		// AND aamm = w_aamm
		// AND nbul = wsd_fcal1.nbul
		// AND rubq = w_rubacqz;
		String queryEVMont = "Select mont From ElementVariableDetailMois where identreprise = :cdos and nmat = :nmat and aamm = :aamm and nbul = :nbul and rubq = :rubq";

		// CURSOR C_elfix IS
		// SELECT monp FROM paelfix
		// WHERE identreprise = wpdos.identreprise
		// AND nmat = wsal01.nmat
		// AND codp = w_rubacqz
		// AND (ddeb is null OR (ddeb is not null and ddeb <= to_date(w_aamm,'YYYYMM') ) )
		// AND (dfin IS NULL OR (dfin is not null and dfin >= to_date(w_aamm,'YYYYMM') ) );
		String queryEF = "Select monp From ElementFixeSalaire where identreprise = :cdos and nmat = :nmat and codp = :codp ";
		queryEF += " and (ddeb is null or (ddeb is not null and ddeb <= :ddeb)) ";
		queryEF += " and (dfin is null or (dfin is not null and dfin>= :dfin)) ";

		// BEGIN
		//
		// IF not retroactif THEN
		//
		if (!this.parameter.isUseRetroactif())
		{
			// IF C_evars_1%ISOPEN THEN CLOSE C_evars_1; END IF;
			// OPEN C_evars_1;
			// FETCH C_evars_1 INTO w_rowid;
			// IF C_evars_1%NOTFOUND THEN
			Session session = this.service.getSession();
			Query q = session.createSQLQuery(queryEVCount);
			q.setParameter("cdos", this.parameter.dossier);
			q.setParameter("nmat", this.infoSalary.getComp_id().getNmat());
			q.setParameter("aamm", this.parameter.monthOfPay);
			q.setParameter("nbul", this.parameter.numeroBulletin);
			q.setParameter("rubq", this.parameter.acompteQuizaineRubrique);
			Integer nbr = Integer.parseInt((q.list().get(0).toString()));
			if (nbr == 0)
			{
				//
				// BEGIN
				// SELECT count(*)
				// INTO w_count
				// FROM paev
				// WHERE identreprise = wpdos.identreprise
				// AND nmat = wsal01.nmat
				// AND aamm = w_aamm
				// AND nbul = wsd_fcal1.nbul;
				// EXCEPTION
				// WHEN NO_DATA_FOUND THEN w_count := 0;
				// END;
				// IF w_count = 0 THEN
				// INSERT INTO paev (cdos,aamm,nmat,nbul,ddpa,dfpa,bcmo)
				// VALUES (wpdos.identreprise,w_aamm, wsal01.nmat, wsd_fcal1.nbul,w_ddpa,w_dfpa,'N');
				// END IF;
				String queryEVEnt = "Select count(*) From ElementVariableEnteteMois where identreprise = :cdos and nmat = :nmat and aamm = :aamm and nbul = :nbul";
				q = session.createSQLQuery(queryEVEnt);
				q.setParameter("cdos", this.parameter.dossier);
				q.setParameter("nmat", this.infoSalary.getComp_id().getNmat());
				q.setParameter("aamm", this.parameter.monthOfPay);
				q.setParameter("nbul", this.parameter.numeroBulletin);
				nbr = Integer.parseInt((q.list().get(0).toString()));
				if (nbr == 0)
				{
					ElementVariableEnteteMois ev = new ElementVariableEnteteMois();
					//new ElementVariableEnteteMoisPK(, , , ));
					ev.setIdEntreprise(Integer.valueOf(this.parameter.dossier));
					ev.setNmat(this.infoSalary.getComp_id().getNmat());
					ev.setNbul(this.parameter.numeroBulletin);
					ev.setAamm(this.parameter.monthOfPay);
			        ev.setBcmo("N");
					ev.setDdpa(this.firstDayOfMonth);
					ev.setDfpa(this.lastDayOfMonth);
					service.save(ev);
				}
				//
				// w_mnt := ROUND(NVL(t9999_mont(rub_nap),0) * w_pourcacqz / 100);
				ClsRubriqueClone nap = this.findRubriqueClone(this.parameter.napRubrique);
				double montant = 0;
				if (nap != null)
					montant = nap.getAmount() * this.parameter.acompteQuizainePourcentage / 100;

				//
				// IF C_evars_2%ISOPEN THEN CLOSE C_evars_2; END IF;
				// OPEN C_evars_2;
				// FETCH C_evars_2 INTO w_mntevar;
				//
				// IF C_evars_2%NOTFOUND THEN
				double montantEvar = 0;
				q = session.createSQLQuery(queryEVMont);
				q.setParameter("cdos", this.parameter.dossier);
				q.setParameter("nmat", this.infoSalary.getComp_id().getNmat());
				q.setParameter("aamm", this.parameter.monthOfPay);
				q.setParameter("nbul", this.parameter.numeroBulletin);
				q.setParameter("rubq", this.parameter.acompteQuizaineRubriqueMontant);
				List lst = q.list();
				if (lst.isEmpty())
				{
					double montantEF = 0;
					//
					// w_mntefix := 0;
					// OPEN C_elfix;
					// FETCH C_elfix INTO w_mntefix;
					// CLOSE C_elfix;
					// IF PA_PAIE.NouZ(w_mntefix) THEN
					// w_mntefix := 0;
					// END IF;

					q = session.createSQLQuery(queryEF);
					q.setParameter("cdos", this.parameter.dossier);
					q.setParameter("nmat", this.infoSalary.getComp_id().getNmat());
					q.setParameter("codp", this.parameter.acompteQuizaineRubriqueMontant);
					q.setParameter("ddeb", this.parameter.myMonthOfPay.getFirstDayOfMonth());
					q.setParameter("dfin", this.parameter.myMonthOfPay.getLastDayOfMonth());
					lst = q.list();
					if (!lst.isEmpty() && lst.get(0) != null)
						montantEF = new BigDecimal(lst.get(0).toString()).doubleValue();
					//
					// IF w_mntefix < w_mnt THEN
					// w_mnt := w_mntefix;
					// END IF;
					if (montantEF < montant)
						montant = montantEF;
					//
					// wevar.identreprise := wpdos.identreprise;
					// wevar.aamm := w_aamm;
					// wevar.nmat := wsal01.nmat;
					// wevar.nbul := wsd_fcal1.nbul;
					// wevar.rubq := w_rpaieacq;
					// wevar.argu := ' ';
					// wevar.nprt := ' ';
					// wevar.ruba := ' ';
					// wevar.mont := w_Mnt;
					// INSERT INTO paevar (cdos,aamm,nmat,nbul,rubq,argu,nprt,ruba,mont,cuti)
					// VALUES (wevar.identreprise, wevar.aamm, wevar.nmat, wevar.nbul,
					// wevar.rubq, wevar.argu, wevar.nprt, wevar.ruba,
					// wevar.mont,w_cuti);
					ElementVariableDetailMois det = new ElementVariableDetailMois();
					//new ElementVariableDetailMoisPK(this.parameter.dossier, , , maxNLigEV++), parameter.monthOfPay);
					det.setNmat(infoSalary.getComp_id().getNmat());
					det.setNbul(parameter.numeroBulletin);
					det.setAamm(parameter.monthOfPay);
					det.setArgu(" ");
					det.setRubq(parameter.acompteQuizaineRubrique);
					det.setNprt(" ");
					det.setRuba(" ");
					det.setMont(new BigDecimal(montant));
					service.save(det);
					//		         
				}
				else
				{
					// ELSE
					//
					// IF PA_PAIE.NouZ(w_mntevar) THEN
					// w_mntevar := 0;
					// END IF;
					if (lst.get(0) != null)
						montantEvar = new BigDecimal(lst.get(0).toString()).doubleValue();
					//
					// IF w_mntevar < w_mnt THEN
					// w_mnt := w_mntevar;
					// END IF;

					if (montantEvar < montant)
						montant = montantEvar;
					//
					// wevar.identreprise := wpdos.identreprise;
					// wevar.aamm := w_aamm;
					// wevar.nmat := wsal01.nmat;
					// wevar.nbul := wsd_fcal1.nbul;
					// wevar.rubq := w_rpaieacq;
					// wevar.argu := ' ';
					// wevar.nprt := ' ';
					// wevar.ruba := ' ';
					// wevar.mont := w_Mnt;
					// INSERT INTO paevar (cdos,aamm,nmat,nbul,rubq,argu,nprt,ruba,mont,cuti)
					// VALUES (wevar.identreprise, wevar.aamm, wevar.nmat, wevar.nbul,
					// wevar.rubq, wevar.argu, wevar.nprt, wevar.ruba,
					// wevar.mont,w_cuti);
					
					ElementVariableDetailMois det = new ElementVariableDetailMois();
					det.setIdEntreprise(Integer.valueOf(this.parameter.dossier));
					det.setNmat(infoSalary.getComp_id().getNmat());
					det.setNbul(parameter.numeroBulletin);
					det.setNlig(maxNLigEV++);
					det.setAamm(parameter.monthOfPay);
					det.setArgu(" ");
					det.setRubq(parameter.acompteQuizaineRubrique);
					det.setNprt(" ");
					det.setRuba(" ");
					det.setMont(new BigDecimal(montant));
					service.save(det);
					//
					// END IF;
					// CLOSE C_evars_2;
				}
				//
				// END IF;
			}
			// CLOSE C_evars_1;
			//
			// END IF;
		}
		//
		// EXCEPTION
		// WHEN OTHERS THEN null;
		// END gen_acomptes;

	}

	public Integer getMaxNumLigneEltVar()
	{
		String queryString = "select max(nlig) From ElementVariableDetailMois  where identreprise=" + "'" + parameter.dossier + "'" + " and  nmat=" + "'" + infoSalary.getComp_id().getNmat() + "' and nbul ="
				+ parameter.numeroBulletin;
		List result = new ArrayList();
		try
		{
			result = service.findByQuery(queryString);
		}
		catch (DataAccessException e)
		{
			e.printStackTrace();
			return 0;
		}

		if (result.size() > 0)
			return Integer.valueOf(result.get(0) == null ? "0" : result.get(0).toString());
		else
			return 0;
	}
	
	//cas de la paie au net, inutile de chercher plusieurs dans les cumuls le mm montant
	//on cherche une fois et on stocke en mémoire
	public Map<String, Double> mapAlgo89 = new HashMap<String, Double>();

	public double calculDuNBJSUPP(double cgprinc)
	{
		String identreprise = getInfoSalary().getComp_id().getCdos();
		int montant = 0;
		int taux1 = 0;
		int taux2 = 0;

		// -- Calcul des jours supp selon la categorie et
		// -- acquis tous les MNT3 annees
		montant = new Double((tempNumber = getUtilNomenclature().getAmountOrRateFromNomenclature(this.parameter.listOfTableXXMap,cdos, 35, getInfoSalary().getCat(), 3, getNlot(), getNbul(), getMonthOfPay(), getPeriodOfPay(),
				ClsEnumeration.EnColumnToRead.AMOUNT)) == null ? 0 : tempNumber.intValue()).intValue();

		taux1 = new Double((tempNumber = getUtilNomenclature().getAmountOrRateFromNomenclature(this.parameter.listOfTableXXMap,cdos, 35, getInfoSalary().getCat(), 1, getNlot(), getNbul(), getMonthOfPay(), getPeriodOfPay(),
				ClsEnumeration.EnColumnToRead.RATES)) == null ? 0 : tempNumber.intValue()).intValue();		
		taux2 = new Double((tempNumber = getUtilNomenclature().getAmountOrRateFromNomenclature(this.parameter.listOfTableXXMap,cdos, 35, getInfoSalary().getCat(), 2, getNlot(), getNbul(), getMonthOfPay(), getPeriodOfPay(),
				ClsEnumeration.EnColumnToRead.RATES)) == null ? 0 : tempNumber.intValue()).intValue();
	
		int ageMin = new Double((tempNumber = getUtilNomenclature().getAmountOrRateFromNomenclature(this.parameter.listOfTableXXMap,cdos, 35, getInfoSalary().getCat(), 5, getNlot(), getNbul(), getMonthOfPay(), getPeriodOfPay(),
				ClsEnumeration.EnColumnToRead.AMOUNT)) == null ? 0 : tempNumber.intValue()).intValue();
		
		//
		if (montant <= 0)
			montant = 1;
		//
		// l_tranche := (PA_CALCUL.ancien / wfnom.mnt3);
		int tranche = getAnciennete() / montant;
		// nbj_sup := l_tranche * wfnom.tau1;
		double nbreJourSuppl = tranche * taux1;
		nbreJourSuppl= nbreJourSuppl* cgprinc / (taux1 * 12);
		// nbj_par_enf := wfnom.tau2;
		int nbreJourParEnfant = taux2;
		// -- Calcul du nombre de jours supp en fonction de l'anciennete
		String ancienS = ClsStringUtil.formatNumber(getAnciennete(), "00");
		int nbreEnfant = 0;

		//
		// -- Calcul du nombre de jrs supp en fonction du nombre d'enfants
		// -- a charge, uniquement pour les femmes
		// IF PA_CALCUL.wsal01.sexe = 'F' THEN
		if ("F".equals(getInfoSalary().getSexe()))
		{

				try
				{
					ClsDate sysdate = new ClsDate(new Date());
					String sdate = ClsStringUtil.formatNumber(sysdate.getDay(), "00") + "-" + ClsStringUtil.formatNumber(sysdate.getMonth(), "00") + "-"
							+ ClsStringUtil.formatNumber((sysdate.getYear() - parameter.enfantAgeMaximum), "0000");
					Date date_min = new ClsDate(sdate, "dd-MM-yyyy").addYear(-1 * ageMin);//.getDate();
					
					String dtna = new ClsDate(date_min, parameter.getAppDateFormat()).getDateS();
					
					String query="select count(*) from Rhtenfantagent" + " where identreprise = '" + cdos + "'" + " and nmat = '" + getInfoSalary().getComp_id().getNmat() + "'" + " and achg = 'O'" + " and dtna > '" + dtna + "'";
					nbreEnfant = new Integer(service.find(query).get(0).toString());
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}			
			
			// END IF;
			// nbj_sup := nbj_sup + (nbj_par_enf * nb_enf);
			nbreJourSuppl += (nbreJourParEnfant * nbreEnfant)* cgprinc / (taux1 * 12);
		}
		// END IF;
		//
		// nbj_enf := NVL(nbj_par_enf,0) * NVL(nb_enf,0);
		nbreJourPourEnfant = nbreJourParEnfant * nbreEnfant;
		//
		return nbreJourSuppl; 
	}	
	
	public int calculDuNBJRELIQUAT()
	{
		int nbj_reliq = 0;

			try
			{
				
				String identreprise = getInfoSalary().getComp_id().getCdos();
				String query="select sum(nbjrc-nbjrd) from Spcongesabc where identreprise = '" + cdos + "' and nmat = '" + getInfoSalary().getComp_id().getNmat() + "' ";
				
				nbj_reliq = new Integer(service.find(query).get(0).toString());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}			
			

		//
		return nbj_reliq;
	}	
	
	public double getTot_cgs()
	{
		return tot_cgs;
	}

	public void setTot_cgs(double tot_cgs)
	{
		this.tot_cgs = tot_cgs;
	}

	public Date getPremierJourDuMois()
	{
		return premierJourDuMois;
	}

	public void setPremierJourDuMois(Date premierJourDuMois)
	{
		this.premierJourDuMois = premierJourDuMois;
	}

	public Date getDernierJourDuMois()
	{
		return dernierJourDuMois;
	}

	public void setDernierJourDuMois(Date dernierJourDuMois)
	{
		this.dernierJourDuMois = dernierJourDuMois;
	}
	
	

	
}
