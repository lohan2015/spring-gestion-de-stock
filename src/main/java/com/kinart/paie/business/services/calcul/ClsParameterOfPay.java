package com.kinart.paie.business.services.calcul;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kinart.paie.business.model.*;
import com.kinart.paie.business.services.impl.CalculPaieServiceImpl;
import com.kinart.paie.business.services.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Session;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataAccessException;

/**
 * Cette permet d'initialiser les param�tres de calcul de la paie que toutes les autres classes utiliseront. Ainsi, ce sont des param�tres globaux. Une erreur
 * dans l'un de ces param�tres suspend le calcul jusqu'� ce que tous les param�tres soient renseign�s.
 * 
 * @author c.mbassi
 */

public class ClsParameterOfPay
{
	public CalculPaieServiceImpl lanceur;
	
	public boolean formulelitterale = false;
	
	public boolean utilisationmoduleexterne = false;
	
	public boolean calculPaieMoisSuivantCalculConge = false;

	// @emmanuel
	protected Number tempNumber = null;
	protected Number tempNumber2 = null;

	protected int threadmax;

	char genfile = 'N';

	String genfilefolder;

	Date ddmp = null;

	Date dtDdex = null;

	Date dtDfex = null;

	int debutExerciceAnnee;

	int debutExerciceMois;

	int debutExerciceaamm;

	int debutExerciceRangMoisPaie;

	String appDateFormat;

	public String rubATracer;

	public String nomClient;

	//
	public int AGE_MAX_OF_SALARY = 65;

	public static final String FORMAT_PERIOD_OF_PAY = "000000";

	public String FORMAT_PERIOD_OF_RBQ = ParameterUtil.formatRubrique;

	public static final String FORMAT_OF_YEAR = "0000";

	// public String FORMAT_DATE = "yyyy-MM-dd";
	// public String FORMAT_DATE_R = "dd-MM-yyyy";
	public static final String FORMAT_DATE_PAY_PERIOD_MMYYYY = "MMyyyy";

	public static final String FORMAT_DATE_PAY_PERIOD_YYYYMM = "yyyyMM";

	// info sur le dossier
	protected String uti = "DELT";

	// type utilisateur
	protected String typu = "3";

	// type utilisateur
	protected String access = "*";

	// type utilisateur
	protected String code = "*";

	// info sur le dossier
	protected DossierPaie dossierInfo = null;

	// l'entreprise dont on est en train de faire le calcul de la paie
	protected String dossier = "01";

	// l'entreprise dont on est en train de faire le calcul de la paie
	protected String dossierCcy = "";

	// l'entreprise dont on est en train de faire le calcul de la paie
	// protected String dossierAnneeExercice = "";
	// date de d�but de l'exercice de l'entreprise
	protected String dossierDateDebutExercice = "";

	// date de fin de l'exercice de l'entreprise
	protected String dossierDateFinExercice = "";

	// nbre de d�cimales apr�s la virgule
	protected int dossierNbreDecimale = 0;

	// -------------------REGULARISATION MINIMALE(REGMINI)------------
	protected String regularisationCas = "";

	protected double regularisationMinimale = 0;

	protected String regularisationRubrique = "";

	// -----------------PRETS/LOAN-------------------------
	protected String BasePrets = "";

	protected String BasePretsTaux = "";

	protected ClsEnumeration.EnLoan enBasePretsSTC = ClsEnumeration.EnLoan.UNKNOWN;

	// -----------------PLAFOND CAISSE FRANCAISE/PLAF-SS-----------
	protected String plafondRubrique = "";

	protected List<Double> plafondOfMonthList = null;

	// -----------------HORAIRE------------
	protected String horaireRubrique = "";

	protected String horaireNumberOfDayRubrique = "";
	
	protected String horaireNuitRubrique = "";

	// -----------------MIN. NET A PAYER------------
	protected double bulletinNegative = 0;

	// -----------------CALCUL FICTIF------------
	protected String fictiveCalculus = "";

	protected String fictiveCalculusType = "";

	protected boolean fictiveCalculusForExpatrie = false;

	protected boolean fictiveCalculusA = false;

	protected boolean fictiveCalculusB = false;

	protected boolean fictiveCalculusNon = false;
	
	protected String rubriqueAvanceConge = "";

	// -----------------ANCIENNETE MAXI------------
	protected int ancienneteMaxi = 0;
	
	protected int ancienneteMin = 0;

	// -----------------CALCUL PRORATA STANDARD------------
	protected boolean prorataStandard = false;

	// -----------------AGE MAXI ENFANT------------
	protected int enfantAgeMaximum = 0;

	protected boolean enfantComptage = true;

	// -----------------EXPATRIE------------
	protected String expatrieValeur = "";

	protected int expatrieTypeContrat = 0;

	// -----------------RUBRIQUE ABSENCE MOIS------------
	protected String absenceMoisRubrique = "";

	protected boolean ABSENCE_MOIS_PDC_SI_ATLM = false;

	// -----------------Gestion des acomptes quizaine ---------------------
	protected String acompteQuizaineRubrique = "";

	protected double acompteQuizainePourcentage = 0;

	protected String acompteQuizaineRubriqueMontant = "";

	protected String napRubrique = "";

	// -----------------RUBRIQUE NBRE JOURS TOTAL NON PAYE------------
	protected String jourNonPayeRubrique = "";

	// -----------------RUBRIQUE PAIE AU NET (RUBNET)------------
	protected String paieAuNetRubrique = "";

	protected double paieAuNetEcartMaximum = 0;

	protected String paieAuNetAffectationEcartRubrique = "";

	protected int paieAuNetMaxIteration = 0;

	// -----------------RUBRIQUE DU BRUT------------
	protected String brutRubrique = "";

	// -----------------RUBRIQUE BASE NBRE JOUR------------
	protected String base30Rubrique = "O";

	protected int base30NombreJour = 0;

	// -----------------RUBRIQUE BASE CONGE (BASE-CONGE)------------
	protected String baseCongeCodeMotif = "";

	protected String baseCongeAbsence = "";

	// -----------------RUBRIQUE NBRE JOURS SUPPL------------
	protected String jourSupplRubrique = "";

	protected double jourSupplDiviseur = 0;

	// -----------------RUBRIQUE NBRE JOURS PAYES NON PRIS------------
	protected String jourPayeNonPrisRubrique = "";

	// -----------------RUBRIQUE NBRE JOURS TRAVAIL-----------------------
	protected String jourTravailleRubrique = "";

	protected String jourTravaillePlage = ""; // T,M,B

	// -----------------RUBRIQUE NBRE JOURS TRAVAIL DEBUT EXERCICE--------
	protected String jourWorkDepuisExerciceRubrique = "";

	protected String jourWorkDepuisSemestreRubrique = "";

	protected String jourWorkDepuisTrimestreRubrique = "";

	//
	protected boolean useRetroactif = false;

	protected int numeroBulletin = 9;

	protected String periodOfPay = "";

	protected String monthOfPay = "";

	protected long nlot = 0;

	protected String clas = "";

	protected Integer sessionId = 0;

	protected String bankCcy = "CFA";

	// ----------------- DO RUBRIQUES NET/ADJUST -----------
	protected boolean RubriqueNetExist = false;

	protected boolean RubriqueAdjustExist = false;

	// le mode de paiement du salari�
	protected ClsEnumeration.EnModePaiement modePaiement = ClsEnumeration.EnModePaiement.UNKNOWN;

	// le service d'acc�s � la bd
	protected GeneriqueConnexionService service = null;

	// param�tres de calcul
	protected String calcul = "N";

	protected int numeroAjustementActuel = 0;

	protected String bouclage = "N";

	protected double ecartSalaireNet = 0;

	protected double pourcentageEcartNet = 0;

	//
	public Map<String, Integer> moisDonnees = null;
	
	protected Map<String, Double> hTauxAppliedToRubriqueAbsence = null;

	protected double tableOfAdjustmentValues[] = null;

	protected List<String> listOfRubriqueToCalculate = null;

	protected List<ClsInfoOfBank> listOfBank = null;

	protected Map<String, List<Object[]>> listOfRubqBase = null;

	protected Map<String, List<Object>> listOfRubqZoneLibre = null;

	protected Map<String, Object[]> listOfArrondi = null;

	protected Map<String, Object[]> listOfTable99Map = null;
	
	protected Map<String, ParamData> listOfTableXXMap = new HashMap<String, ParamData>();

	// protected Map<String, Object> listOfRubqOfsession = null;

	protected Map<String, Object> listOfRubriquebaremeMap = null;

	// construit une liste ordonn�e de codes des rubriques
	List<String> listOrdonneRubrique = null;

	// liste des rubrique
	protected Map<String, ClsRubriqueClone> ListOfAllRubriqueMap = null;

	//
	protected ClsNomenclatureUtil utilNomenclature = null;

	// STC
	protected boolean stc = false;

	protected boolean rubriqueStc = false;

	//
	protected String departNiv1 = "";

	protected String finNiv1 = "";

	protected String departNiv2 = "";

	protected String finNiv2 = "";

	protected String departNiv3 = "";

	protected String finNiv3 = "";

	protected String departMatricule = "";

	protected String finMatricule = "";

	protected String rubriquePlus = "";

	protected String precNiv3 = "";

	protected ClsDate myMonthOfPay = null;

	protected String moisPaieCourant = "";

	protected ClsDate myMoisPaieCourant = null;

	protected int rangMoisDePaieExercice = 0;

	protected int nbrePeriodeRegularisationEv = 0;

	// pour le choix de la liste des salari�s � traiter
	protected ClsEnumeration.EnTypeOfSalaryList enTypeSalaries = null;

	protected int nbreJourMoisPourProrata = 0;

	protected String langue = "0001";

	protected String error = "";

	protected boolean pbWithCalulation = false;

	protected Date firstDayOfMonth = null;

	protected Date lastDayOfMonth = null;

	protected boolean table91LabelNotEmpty = false;

	protected String outputtext = "";

	// -- Prise en compte des cong�s ant�rieurs au mois de paie
	protected boolean priseEnCompterCongeAnterieur = false;

	// -- TFN 06/03/2008 - Prise en compte de la date de fin de contrat CDD
	protected boolean priseEnCompteDateFinContrat = false;
	
	
	protected List<String> listeRubriquesAAjuster = new ArrayList<String>();
	protected List<String> listeRubriquesAAjouterAuNet = new ArrayList<String>();
	
	protected Map<String, Double> devises = null;
	

	public List<String> getListeRubriquesAAjuster()
	{
		return listeRubriquesAAjuster;
	}

	public void setListeRubriquesAAjuster(List<String> listeRubriquesAAjuster)
	{
		this.listeRubriquesAAjuster = listeRubriquesAAjuster;
	}

	public List<String> getListeRubriquesAAjouterAuNet()
	{
		return listeRubriquesAAjouterAuNet;
	}

	public void setListeRubriquesAAjouterAuNet(List<String> listeRubriquesAAjouterAuNet)
	{
		this.listeRubriquesAAjouterAuNet = listeRubriquesAAjouterAuNet;
	}

	public String getOutputtext()
	{
		return outputtext;
	}

	public void setOutputtext(String outputtext)
	{
		this.outputtext = outputtext;
	}

	public boolean isTable91LabelNotEmpty()
	{
		return table91LabelNotEmpty;
	}

	public void setTable91LabelNotEmpty(boolean table91LabelNotEmpty)
	{
		this.table91LabelNotEmpty = table91LabelNotEmpty;
	}

	public List<String> getListOrdonneRubrique()
	{
		return listOrdonneRubrique;
	}

	public void setListOrdonneRubrique(List<String> listOrdonneRubrique)
	{
		this.listOrdonneRubrique = listOrdonneRubrique;
	}

	public Date getLastDayOfMonth()
	{
		return lastDayOfMonth;
	}

	public void setLastDayOfMonth(Date lastDayOfMonth)
	{
		this.lastDayOfMonth = lastDayOfMonth;
	}

	public Date getFirstDayOfMonth()
	{
		return firstDayOfMonth;
	}

	public void setFirstDayOfMonth(Date firstDayOfMonth)
	{
		this.firstDayOfMonth = firstDayOfMonth;
	}

	public synchronized Map<String, Object> getListOfRubriquebaremeMap()
	{
		return listOfRubriquebaremeMap;
	}

	public synchronized void setListOfRubriquebaremeMap(Map<String, Object> listOfRubriquebaremeMap)
	{
		this.listOfRubriquebaremeMap = listOfRubriquebaremeMap;
	}

	public synchronized Map<String, Object[]> getListOfTable99Map()
	{
		return listOfTable99Map;
	}

	public synchronized void setListOfTable99Map(Map<String, Object[]> listOfTable99Map)
	{
		this.listOfTable99Map = listOfTable99Map;
	}

	public synchronized Map<String, ParamData> getListOfTableXXMap()
	{
		return listOfTableXXMap;
	}

	public synchronized void setListOfTableXXMap(Map<String, ParamData> listOfTableXXMap)
	{
		this.listOfTableXXMap = listOfTableXXMap;
	}

	public synchronized Map<String, Object[]> getListOfArrondi()
	{
		return listOfArrondi;
	}

	public synchronized void setListOfArrondi(Map<String, Object[]> listOfArrondi)
	{
		this.listOfArrondi = listOfArrondi;
	}

	public synchronized Map<String, List<Object>> getListOfRubqZoneLibre()
	{
		return listOfRubqZoneLibre;
	}

	public synchronized void setListOfRubqZoneLibre(Map<String, List<Object>> listOfRubqZoneLibre)
	{
		this.listOfRubqZoneLibre = listOfRubqZoneLibre;
	}

	public synchronized Map<String, List<Object[]>> getListOfRubqBase()
	{
		return listOfRubqBase;
	}

	public synchronized void setListOfRubqBase(Map<String, List<Object[]>> listOfRubqBase)
	{
		this.listOfRubqBase = listOfRubqBase;
	}

	public synchronized boolean isPbWithCalulation()
	{
		return pbWithCalulation;
	}

	public synchronized void setPbWithCalulation(boolean pbWithCalulation)
	{
		this.pbWithCalulation = pbWithCalulation;
	}

	public long getNlot()
	{
		return nlot;
	}

	public void setNlot(long nlot)
	{
		this.nlot = nlot;
	}

	public ClsNomenclatureUtil getUtilNomenclature()
	{
		return utilNomenclature;
	}

	public void setUtilNomenclature(ClsNomenclatureUtil utilNomenclature)
	{
		this.utilNomenclature = utilNomenclature;
	}

	public GeneriqueConnexionService getService()
	{
		return service;
	}

	public void setService(GeneriqueConnexionService service)
	{
		this.service = service;
	}

	public String getError()
	{
		return error;
	}

	public void setError(String error)
	{
		this.error = error;
	}

	public String getLangue()
	{
		return langue;
	}

	public void setLangue(String langue)
	{
		this.langue = langue;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getAccess()
	{
		return access;
	}

	public void setAccess(String access)
	{
		this.access = access;
	}

	public String getTypu()
	{
		return typu;
	}

	public void setTypu(String typu)
	{
		this.typu = typu;
	}

	public String getUti()
	{
		return uti;
	}

	public void setUti(String uti)
	{
		this.uti = uti;
	}

	public List<String> getListOfRubriqueToCalculate()
	{
		return listOfRubriqueToCalculate;
	}

	public void setListOfRubriqueToCalculate(List<String> listOfRubriqueToCalculate)
	{
		this.listOfRubriqueToCalculate = listOfRubriqueToCalculate;
	}

	public List<ClsInfoOfBank> getListOfBank()
	{
		return listOfBank;
	}

	public void setListOfBank(List<ClsInfoOfBank> listOfBank)
	{
		this.listOfBank = listOfBank;
	}

	public String getBankCcy()
	{
		return bankCcy;
	}

	public void setBankCcy(String bankCcy)
	{
		this.bankCcy = bankCcy;
	}

	public String getMonthOfPay()
	{
		return monthOfPay;
	}

	public void setMonthOfPay(String monthOfPay)
	{
		this.monthOfPay = monthOfPay;
	}

	public String getJourTravaillePlage()
	{
		return jourTravaillePlage;
	}

	public void setJourTravaillePlage(String jourTravaillePlage)
	{
		this.jourTravaillePlage = jourTravaillePlage;
	}

	public String getJourTravailleRubrique()
	{
		return jourTravailleRubrique;
	}

	public void setJourTravailleRubrique(String jourTravailleRubrique)
	{
		this.jourTravailleRubrique = jourTravailleRubrique;
	}

	public int getEnfantAgeMaximum()
	{
		return enfantAgeMaximum;
	}

	public void setEnfantAgeMaximum(int enfantAgeMaximum)
	{
		this.enfantAgeMaximum = enfantAgeMaximum;
	}

	public boolean isEnfantComptage()
	{
		return enfantComptage;
	}

	public void setEnfantComptage(boolean enfantComptage)
	{
		this.enfantComptage = enfantComptage;
	}

	public int getNbrePeriodeRegularisationEv()
	{
		return nbrePeriodeRegularisationEv;
	}

	public void setNbrePeriodeRegularisationEv(int nbrePeriodeRegularisationEv)
	{
		this.nbrePeriodeRegularisationEv = nbrePeriodeRegularisationEv;
	}

	public double getBulletinNegative()
	{
		return bulletinNegative;
	}

	public void setBulletinNegative(double bulletinNegative)
	{
		this.bulletinNegative = bulletinNegative;
	}

	public DossierPaie getDossierInfo()
	{
		return dossierInfo;
	}

	public void setDossierInfo(DossierPaie dossierInfo)
	{
		this.dossierInfo = dossierInfo;
	}

	public String getBasePrets()
	{
		return BasePrets;
	}

	public void setBasePrets(String basePrets)
	{
		BasePrets = basePrets;
	}

	public String getBasePretsTaux()
	{
		return BasePretsTaux;
	}

	public void setBasePretsTaux(String basePretsTaux)
	{
		BasePretsTaux = basePretsTaux;
	}

	public ClsEnumeration.EnLoan getEnBasePretsSTC()
	{
		return enBasePretsSTC;
	}

	public void setEnBasePretsSTC(ClsEnumeration.EnLoan enBasePretsSTC)
	{
		this.enBasePretsSTC = enBasePretsSTC;
	}

	public boolean isFictiveCalculusA()
	{
		return fictiveCalculusA;
	}

	public void setFictiveCalculusA(boolean fictiveCalculusA)
	{
		this.fictiveCalculusA = fictiveCalculusA;
	}

	public boolean isFictiveCalculusB()
	{
		return fictiveCalculusB;
	}

	public void setFictiveCalculusB(boolean fictiveCalculusB)
	{
		this.fictiveCalculusB = fictiveCalculusB;
	}

	public boolean isFictiveCalculusForExpatrie()
	{
		return fictiveCalculusForExpatrie;
	}

	public void setFictiveCalculusForExpatrie(boolean fictiveCalculusForExpatrie)
	{
		this.fictiveCalculusForExpatrie = fictiveCalculusForExpatrie;
	}

	public boolean isFictiveCalculusNon()
	{
		return fictiveCalculusNon;
	}

	public void setFictiveCalculusNon(boolean fictiveCalculusNon)
	{
		this.fictiveCalculusNon = fictiveCalculusNon;
	}

	public int getRangMoisDePaieExercice()
	{
		return rangMoisDePaieExercice;
	}

	public void setRangMoisDePaieExercice(int rangMoisDePaieExercice)
	{
		this.rangMoisDePaieExercice = rangMoisDePaieExercice;
	}

	public int getNbreJourMoisPourProrata()
	{
		return nbreJourMoisPourProrata;
	}

	public void setNbreJourMoisPourProrata(int nbreJourMoisPourProrata)
	{
		this.nbreJourMoisPourProrata = nbreJourMoisPourProrata;
	}

	public String getMoisPaieCourant()
	{
		return moisPaieCourant;
	}

	public void setMoisPaieCourant(String moisPaieCourant)
	{
		this.moisPaieCourant = moisPaieCourant;
	}

	public ClsDate getMyMoisPaieCourant()
	{
		return myMoisPaieCourant;
	}

	public void setMyMoisPaieCourant(ClsDate myMoisPaieCourant)
	{
		this.myMoisPaieCourant = myMoisPaieCourant;
	}

	public boolean isRubriqueStc()
	{
		return rubriqueStc;
	}

	public void setRubriqueStc(boolean rubriqueStc)
	{
		this.rubriqueStc = rubriqueStc;
	}

	public ClsEnumeration.EnTypeOfSalaryList getEnTypeSalaries()
	{
		return enTypeSalaries;
	}

	public void setEnTypeSalaries(ClsEnumeration.EnTypeOfSalaryList enTypeSalaries)
	{
		this.enTypeSalaries = enTypeSalaries;
	}

	public int getNumeroBulletin()
	{
		return numeroBulletin;
	}

	public void setNumeroBulletin(int numeroBulletin)
	{
		this.numeroBulletin = numeroBulletin;
	}

	public ClsDate getMyMonthOfPay()
	{
		return myMonthOfPay;
	}

	public void setMyMonthOfPay(ClsDate myMonthOfPay)
	{
		this.myMonthOfPay = myMonthOfPay;
	}

	public String getRubriquePlus()
	{
		return rubriquePlus;
	}

	public void setRubriquePlus(String rubriquePlus)
	{
		this.rubriquePlus = rubriquePlus;
	}

	public ClsEnumeration.EnModePaiement getModePaiement()
	{
		return modePaiement;
	}

	public void setModePaiement(ClsEnumeration.EnModePaiement modePaiement)
	{
		this.modePaiement = modePaiement;
	}

	public String getClas()
	{
		return clas;
	}

	public void setClas(String clas)
	{
		this.clas = clas;
	}

	public String getDepartMatricule()
	{
		return departMatricule;
	}

	public void setDepartMatricule(String departMatricule)
	{
		this.departMatricule = departMatricule;
	}

	public String getDepartNiv1()
	{
		return departNiv1;
	}

	public void setDepartNiv1(String departNiv1)
	{
		this.departNiv1 = departNiv1;
	}

	public String getDepartNiv2()
	{
		return departNiv2;
	}

	public void setDepartNiv2(String departNiv2)
	{
		this.departNiv2 = departNiv2;
	}

	public String getDepartNiv3()
	{
		return departNiv3;
	}

	public void setDepartNiv3(String departNiv3)
	{
		this.departNiv3 = departNiv3;
	}

	public String getFinMatricule()
	{
		return finMatricule;
	}

	public void setFinMatricule(String finMatricule)
	{
		this.finMatricule = finMatricule;
	}

	public String getFinNiv1()
	{
		return finNiv1;
	}

	public void setFinNiv1(String finNiv1)
	{
		this.finNiv1 = finNiv1;
	}

	public String getFinNiv2()
	{
		return finNiv2;
	}

	public void setFinNiv2(String finNiv2)
	{
		this.finNiv2 = finNiv2;
	}

	public String getFinNiv3()
	{
		return finNiv3;
	}

	public void setFinNiv3(String finNiv3)
	{
		this.finNiv3 = finNiv3;
	}

	public double[] getTableOfAdjustmentValues()
	{
		return tableOfAdjustmentValues;
	}

	public void setTableOfAdjustmentValues(double[] tableOfAdjustmentValues)
	{
		this.tableOfAdjustmentValues = tableOfAdjustmentValues;
	}

	public double getPourcentageEcartNet()
	{
		return pourcentageEcartNet;
	}

	public void setPourcentageEcartNet(double pourcentageEcartNet)
	{
		this.pourcentageEcartNet = pourcentageEcartNet;
	}

	public double getEcartSalaireNet()
	{
		return ecartSalaireNet;
	}

	public void setEcartSalaireNet(double ecartSalaireNet)
	{
		this.ecartSalaireNet = ecartSalaireNet;
	}

	public boolean isRubriqueAdjustExist()
	{
		return RubriqueAdjustExist;
	}

	public void setRubriqueAdjustExist(boolean rubriqueAdjustExist)
	{
		RubriqueAdjustExist = rubriqueAdjustExist;
	}

	public boolean isRubriqueNetExist()
	{
		return RubriqueNetExist;
	}

	public void setRubriqueNetExist(boolean rubriqueNetExist)
	{
		RubriqueNetExist = rubriqueNetExist;
	}

	public int getNumeroAjustementActuel()
	{
		return numeroAjustementActuel;
	}

	public void setNumeroAjustementActuel(int numeroAjustementActuel)
	{
		this.numeroAjustementActuel = numeroAjustementActuel;
	}

	public String getCalcul()
	{
		return calcul;
	}

	public void setCalcul(String calcul)
	{
		this.calcul = calcul;
	}

	public boolean isStc()
	{
		return stc;
	}

	public void setStc(boolean stc)
	{
		this.stc = stc;
	}

	public boolean isProrataStandard()
	{
		return prorataStandard;
	}

	public void setProrataStandard(boolean prorataStandard)
	{
		this.prorataStandard = prorataStandard;
	}

	public Map<String, Double> getHTauxAppliedToRubriqueAbsence()
	{
		return hTauxAppliedToRubriqueAbsence;
	}

	public void setHTauxAppliedToRubriqueAbsence(Map<String, Double> tauxAppliedToRubriqueAbsence)
	{
		hTauxAppliedToRubriqueAbsence = tauxAppliedToRubriqueAbsence;
	}
	

	public Map<String, Integer> getMoisDonnees()
	{
		return moisDonnees;
	}

	public void setMoisDonnees(Map<String, Integer> moisDonnees)
	{
		this.moisDonnees = moisDonnees;
	}

	public String getBrutRubrique()
	{
		return brutRubrique;
	}

	public void setBrutRubrique(String brutRubrique)
	{
		this.brutRubrique = brutRubrique;
	}

	public String getJourWorkDepuisExerciceRubrique()
	{
		return jourWorkDepuisExerciceRubrique;
	}

	public void setJourWorkDepuisExerciceRubrique(String jourWorkDepuisExerciceRubrique)
	{
		this.jourWorkDepuisExerciceRubrique = jourWorkDepuisExerciceRubrique;
	}

	public String getJourWorkDepuisSemestreRubrique()
	{
		return jourWorkDepuisSemestreRubrique;
	}

	public void setJourWorkDepuisSemestreRubrique(String jourWorkDepuisSemestreRubrique)
	{
		this.jourWorkDepuisSemestreRubrique = jourWorkDepuisSemestreRubrique;
	}

	public String getJourWorkDepuisTrimestreRubrique()
	{
		return jourWorkDepuisTrimestreRubrique;
	}

	public void setJourWorkDepuisTrimestreRubrique(String jourWorkDepuisTrimestreRubrique)
	{
		this.jourWorkDepuisTrimestreRubrique = jourWorkDepuisTrimestreRubrique;
	}

	public String getPlafondRubrique()
	{
		return plafondRubrique;
	}

	public void setPlafondRubrique(String plafondRubrique)
	{
		this.plafondRubrique = plafondRubrique;
	}

	public String getDossierDateDebutExercice()
	{
		return dossierDateDebutExercice;
	}

	public void setDossierDateDebutExercice(String dossierDateDebutExercice)
	{
		this.dossierDateDebutExercice = dossierDateDebutExercice;
	}

	public String getDossierDateFinExercice()
	{
		return dossierDateFinExercice;
	}

	public void setDossierDateFinExercice(String dossierDateFinExercice)
	{
		this.dossierDateFinExercice = dossierDateFinExercice;
	}

	public String getPeriodOfPay()
	{
		return periodOfPay;
	}

	public void setPeriodOfPay(String periodOfPay)
	{
		this.periodOfPay = periodOfPay;
	}

	public String getFictiveCalculus()
	{
		return fictiveCalculus;
	}

	public void setFictiveCalculus(String fictiveCalculus)
	{
		this.fictiveCalculus = fictiveCalculus;
	}

	public String getFictiveCalculusType()
	{
		return fictiveCalculusType;
	}

	public void setFictiveCalculusType(String fictiveCalculusType)
	{
		this.fictiveCalculusType = fictiveCalculusType;
	}

	public String getPaieAuNetAffectationEcartRubrique()
	{
		return paieAuNetAffectationEcartRubrique;
	}

	public void setPaieAuNetAffectationEcartRubrique(String paieAuNetAffectationEcartRubrique)
	{
		this.paieAuNetAffectationEcartRubrique = paieAuNetAffectationEcartRubrique;
	}

	public double getPaieAuNetEcartMaximum()
	{
		return paieAuNetEcartMaximum;
	}

	public void setPaieAuNetEcartMaximum(double paieAuNetEcartMaximum)
	{
		this.paieAuNetEcartMaximum = paieAuNetEcartMaximum;
	}

	public int getPaieAuNetMaxIteration()
	{
		return paieAuNetMaxIteration;
	}

	public void setPaieAuNetMaxIteration(int paieAuNetMaxIteration)
	{
		this.paieAuNetMaxIteration = paieAuNetMaxIteration;
	}

	public String getPaieAuNetRubrique()
	{
		return paieAuNetRubrique;
	}

	public void setPaieAuNetRubrique(String paieAuNetRubrique)
	{
		this.paieAuNetRubrique = paieAuNetRubrique;
	}

	public String getHoraireNumberOfDayRubrique()
	{
		return horaireNumberOfDayRubrique;
	}

	public void setHoraireNumberOfDayRubrique(String horaireNumberOfDayRubrique)
	{
		this.horaireNumberOfDayRubrique = horaireNumberOfDayRubrique;
	}

	public String getHoraireRubrique()
	{
		return horaireRubrique;
	}

	public void setHoraireRubrique(String horaireRubrique)
	{
		this.horaireRubrique = horaireRubrique;
	}
	
	public String getHoraireNuitRubrique()
	{
		return horaireNuitRubrique;
	}

	public void setHoraireNuitRubrique(String horaireNuitRubrique)
	{
		this.horaireNuitRubrique = horaireNuitRubrique;
	}

	public boolean isABSENCE_MOIS_PDC_SI_ATLM()
	{
		return ABSENCE_MOIS_PDC_SI_ATLM;
	}

	public void setABSENCE_MOIS_PDC_SI_ATLM(boolean absence_mois_pdc_si_atlm)
	{
		ABSENCE_MOIS_PDC_SI_ATLM = absence_mois_pdc_si_atlm;
	}

	public String getAbsenceMoisRubrique()
	{
		return absenceMoisRubrique;
	}

	public void setAbsenceMoisRubrique(String absenceMoisRubrique)
	{
		this.absenceMoisRubrique = absenceMoisRubrique;
	}

	public String getJourNonPayeRubrique()
	{
		return jourNonPayeRubrique;
	}

	public void setJourNonPayeRubrique(String jourNonPayeRubrique)
	{
		this.jourNonPayeRubrique = jourNonPayeRubrique;
	}

	public double getJourSupplDiviseur()
	{
		return jourSupplDiviseur;
	}

	public void setJourSupplDiviseur(double jourSupplDiviseur)
	{
		this.jourSupplDiviseur = jourSupplDiviseur;
	}

	public String getJourSupplRubrique()
	{
		return jourSupplRubrique;
	}

	public void setJourSupplRubrique(String jourSupplRubrique)
	{
		this.jourSupplRubrique = jourSupplRubrique;
	}

	public String getJourPayeNonPrisRubrique()
	{
		return jourPayeNonPrisRubrique;
	}

	public void setJourPayeNonPrisRubrique(String jourPayeNonPrisRubrique)
	{
		this.jourPayeNonPrisRubrique = jourPayeNonPrisRubrique;
	}

	public String getRegularisationCas()
	{
		return regularisationCas;
	}

	public void setRegularisationCas(String regularisationCas)
	{
		this.regularisationCas = regularisationCas;
	}

	public double getRegularisationMinimale()
	{
		return regularisationMinimale;
	}

	public void setRegularisationMinimale(double regularisationMinimale)
	{
		this.regularisationMinimale = regularisationMinimale;
	}

	public String getRegularisationRubrique()
	{
		return regularisationRubrique;
	}

	public void setRegularisationRubrique(String regularisationRubrique)
	{
		this.regularisationRubrique = regularisationRubrique;
	}

	// public String getDossierAnneeExercice() {
	// return dossierAnneeExercice;
	// }
	// public void setDossierAnneeExercice(String dossierAnneeExercice) {
	// this.dossierAnneeExercice = dossierAnneeExercice;
	// }
	public int getBase30NombreJour()
	{
		return base30NombreJour;
	}

	public void setBase30NombreJour(int base30NombreJour)
	{
		this.base30NombreJour = base30NombreJour;
	}

	public String getBase30Rubrique()
	{
		return base30Rubrique;
	}

	public void setBase30Rubrique(String base30Rubrique)
	{
		this.base30Rubrique = base30Rubrique;
	}

	public String getBaseCongeAbsence()
	{
		return baseCongeAbsence;
	}

	public void setBaseCongeAbsence(String baseCongeAbsence)
	{
		this.baseCongeAbsence = baseCongeAbsence;
	}

	public String getBaseCongeCodeMotif()
	{
		return baseCongeCodeMotif;
	}

	public void setBaseCongeCodeMotif(String baseCongeCodeMotif)
	{
		this.baseCongeCodeMotif = baseCongeCodeMotif;
	}

	public boolean isUseRetroactif()
	{
		return useRetroactif;
	}

	public void setUseRetroactif(boolean useRetroactif)
	{
		this.useRetroactif = useRetroactif;
	}

	public Integer getSessionId()
	{
		return sessionId;
	}

	public void setSessionId(Integer sessionId)
	{
		this.sessionId = sessionId;
	}

	public int getAncienneteMaxi()
	{
		return ancienneteMaxi;
	}

	public void setAncienneteMaxi(int ancienneteMaxi)
	{
		this.ancienneteMaxi = ancienneteMaxi;
	}

	public String getDossier()
	{
		return dossier;
	}

	public void setDossier(String dossier)
	{
		this.dossier = dossier;
	}

	public String getDossierCcy()
	{
		return dossierCcy;
	}

	public void setDossierCcy(String dossierCcy)
	{
		this.dossierCcy = dossierCcy;
	}

	public int getExpatrieTypeContrat()
	{
		return expatrieTypeContrat;
	}

	public void setExpatrieTypeContrat(int expatrieTypeContrat)
	{
		this.expatrieTypeContrat = expatrieTypeContrat;
	}

	public String getExpatrieValeur()
	{
		return expatrieValeur;
	}

	public void setExpatrieValeur(String expatrieValeur)
	{
		this.expatrieValeur = expatrieValeur;
	}

	public int getAGE_MAX_OF_SALARY()
	{
		return AGE_MAX_OF_SALARY;
	}

	public void setAGE_MAX_OF_SALARY(int age_max_of_salary)
	{
		AGE_MAX_OF_SALARY = age_max_of_salary;
	}

	public ClsParameterOfPay()
	{

	}

	/**
	 * Constructeur permet de cr�er une instance de param�tres
	 * 
	 * @param service
	 *            le service d'acc�s � la base de donn�es
	 * @param utilNomenclature
	 *            l'utilitaire d'acc�s aux tables de nommenclatures et aux donn�es historis�es
	 * @param useRetroactif
	 *            si le calcul est retroactif
	 * @param numeroBulletin
	 *            le num�ro de bulletin concern� par le calcul
	 * @param periodPay
	 *            la p�riode de paie en yyyymm
	 * @param clas
	 *            la classe de salari�s
	 * @param sessionId
	 *            la session
	 * @param modePaiement
	 *            le mode de paiement (V, E, C)
	 */
	public ClsParameterOfPay(GeneriqueConnexionService service, ClsNomenclatureUtil utilNomenclature, boolean useRetroactif, int numeroBulletin, String periodPay, String clas, int sessionId, ClsEnumeration.EnModePaiement modePaiement)
	{
		this.useRetroactif = useRetroactif;
		this.numeroBulletin = numeroBulletin;
		this.periodOfPay = periodPay;
		this.monthOfPay = periodPay;
		this.clas = clas;
		this.modePaiement = modePaiement;
		this.sessionId = sessionId;
		this.service = service;
		this.utilNomenclature = utilNomenclature;
	}

	/**
	 * => Charg_TauxAbs
	 * <p>
	 * Initialise the taux rubrique absence map construit le map qui fait correspondre chaque rubrique d'absence � son taux.
	 * </p>
	 */

	public void chargementTauxAbsence()
	{
		// com.cdi.deltarh.service.ClsParameter.println(">>chargementTauxAbsence");
		// must use hTauxAppliedToRubriqueAbsence
		// CURSOR curs_txabs IS
		// SELECT cacc, valm FROM pafnom
		// WHERE cdos = wpdos.identreprise
		// AND ctab = 22
		// AND nume = 8
		// AND NVL(valm,0) != 0;
		//
		// CURSOR curs_txabs2 IS
		// SELECT cacc, valm FROM pahfnom
		// WHERE cdos = wpdos.identreprise
		// AND ctab = 22
		// AND nume = 8
		// AND aamm = w_aamm
		// AND nbul = wsd_fcal1.nbul
		// AND NVL(valm,0) != 0;
		// ----- Chargement des taux de reglement d'absence
		String query = "select cacc, valm from ParamData" + " where cdos ='" + dossier + "'" + " and ctab = 22" + " and nume = 8" + " and valm <> 0";
		String queryRetro = "select cacc, valm from Rhthfnom" + " where cdos ='" + dossier + "'" + " and ctab = 22" + " and nume = 8" + " and aamm = '" + monthOfPay + "'" + " and nbul = "
				+ numeroBulletin + " and valm <> 0";
		List listOfParam = (useRetroactif) ? service.find(queryRetro) : service.find(query);
		//
		String rubrique = "";
		String cacc = "";
		double taux = 0;
		Object[] obj = null;
		boolean listSet = false;
		for (Object object : listOfParam)
		{
			if (!listSet)
			{
				hTauxAppliedToRubriqueAbsence = new HashMap<String, Double>();
				listSet = true;
			}
			obj = (Object[]) object;
			cacc = (String) obj[0];
			// rubrique = ClsStringUtil.formatNumber((Double)obj[1], FORMAT_PERIOD_OF_RBQ);
			rubrique = ClsStringUtil.formatNumber((obj[1] instanceof BigDecimal) ? ((BigDecimal) obj[1]).intValue() : ((Long) obj[1]).intValue(), FORMAT_PERIOD_OF_RBQ);
			taux = (tempNumber = utilNomenclature.getAmountOrRateFromNomenclature(this.listOfTableXXMap,dossier, 22, cacc, 1, nlot, numeroBulletin, moisPaieCourant, periodOfPay, ClsEnumeration.EnColumnToRead.RATES)) == null ? 0 : tempNumber
					.doubleValue();
			hTauxAppliedToRubriqueAbsence.put(rubrique, taux);
		}
	}
	
	public void chargementDevises()
	{
		String query = "select cacc, valt from ParamData where cdos ='" + dossier + "' and ctab = 27 and nume = 1";
		String queryRetro = "select cacc, valt from Rhthfnom where cdos ='" + dossier + "' and ctab = 27 and nume = 1 and aamm = '" + monthOfPay + "'" + " and nbul = "
				+ numeroBulletin;
		List listOfParam = (useRetroactif) ? service.find(queryRetro) : service.find(query);
		String cacc = "";
		double taux = 1;
		Object[] obj = null;
		boolean listSet = false;
		for (Object object : listOfParam)
		{
			if (!listSet)
			{
				devises = new HashMap<String, Double>();
				listSet = true;
			}
			obj = (Object[]) object;
			cacc = (String) obj[0];
			taux = ( ClsObjectUtil.getBigDecimalFromObject(obj[1] != null ? obj[1] : 1)).doubleValue();
			devises.put(cacc, taux);
		}
	}

	/**
	 * => Charg_TmpNet initialise the table of adjustment values -- PAIE AU NET : -- Creation et chargement des tables temporaires contenant -- les rubriques
	 * saisies en net -- les rubriques soumises a ajustement -- Les montants correspondant sont charges pour chaque salarie ( E.V. ou E.F. )
	 */
	public void chargementTableValeurAjustement()
	{
		// com.cdi.deltarh.service.ClsParameter.println(">>chargementTableValeurAjustement");
		// must use tableOfAdjustmentValues
		// BEGIN
		// rubnet_exist := TRUE;
		//
		// BEGIN
		// INSERT INTO parubnet
		// SELECT w_session_id, crub, NVL(ajnu,0) ajnu, 0 mont
		// FROM parubq
		// WHERE cdos = wpdos.identreprise
		// AND calc = 'O'
		// AND snet = 'O';
		// EXCEPTION
		// WHEN OTHERS THEN null;
		// END;
		

		// @Add by yannick : proposition de suppression de la table avant tout traitement
		Session session = service.getSession();
		session.createQuery("Delete From ElementSalaireNet where sessionId =" + sessionId).executeUpdate();
		service.closeSession(session);
		
		String queryString = "select 0, crub, ajnu from ElementSalaire a " + " where cdos ='" + dossier + "'" + " and calc = 'O'" + " and snet = 'O'";
		if("ClsEntreprise.BGFIGE".equalsIgnoreCase(nomClient) || "ClsEntreprise.SONIBANK".equalsIgnoreCase(nomClient)){
			queryString += " and (exists (";
			queryString += " select 'X' from ElementVariableDetailMois c";
			queryString += " where c.identreprise = a.identreprise";
			queryString += " and c.rubq = a.crub";
			queryString += " and c.aamm = '"+getMoisPaieCourant()+"'";
			queryString += " and c.nbul = '"+getNumeroBulletin()+"'";
			queryString += " )";
			queryString += " or exists (";
			queryString += " select 'X' from ElementFixeSalaire c";
			queryString += " where c.identreprise = a.identreprise";
			queryString += " and c.codp = a.crub";
			queryString += " )";
			queryString += " )";
		}
		ElementSalaireNet oParubnet = null;
		Object[] row = null;
		List listOfRubrique = service.find(queryString);
		if(listeRubriquesAAjouterAuNet!=null)
			listeRubriquesAAjouterAuNet.clear();
		for (Object object : listOfRubrique)
		{
			row = (Object[]) object;
			oParubnet = new ElementSalaireNet();
			// if(row[0] != null)
			// oParubnet.setSessionId(new BigDecimal((Integer)row[0]));
			if (row[1] != null)
				oParubnet.setCrub((String) row[1]);
			if (row[2] != null)
				oParubnet.setAjnu(((Long) row[2]).intValue());
			oParubnet.setMont(new BigDecimal(0));
			oParubnet.setSessionId(new BigDecimal(sessionId));
			//
			service.save(oParubnet);
			listeRubriquesAAjouterAuNet.add(oParubnet.getCrub());
		}
		//
		// i := 0;
		// SELECT COUNT(*) INTO i FROM parubnet WHERE session_id = w_session_id;
		// IF PA_PAIE.NouZ(i) THEN
		// rubnet_exist := FALSE;
		// END IF;
		//
		queryString = "select count(*) from Rhtrubnet " + " where session_id ='" + sessionId + "'";
		listOfRubrique = service.find(queryString);
		if (listOfRubrique != null && listOfRubrique.size() > 0)
		{
			Object obj = listOfRubrique.get(0);
			if (Integer.valueOf(obj.toString()) != 0)
				this.setRubriqueNetExist(true);
		} 
		// rubajus_exist := TRUE;
		// BEGIN
		// INSERT INTO parubajus
		// SELECT w_session_id, crub, NVL(ajnu,0) ajnu, 0 mont
		// FROM parubq
		// WHERE cdos = wpdos.identreprise
		// AND calc = 'O'
		// AND ajus = 'O';
		// EXCEPTION
		// WHEN OTHERS THEN null;
		// END;
		//

		// @Add by yannick : proposition de suppression de la table avant tout traitement
		session = service.getSession();
		session.createQuery("Delete From ElementSalaireAjus where sessionId =" + sessionId).executeUpdate();
		service.closeSession(session);

		queryString = "select " + sessionId + ", crub, ajnu from ElementSalaire a " + "where cdos ='" + dossier + "'" + " and calc = 'O'" + " and ajus = 'O'";
		if("ClsEntreprise.BGFIGE".equalsIgnoreCase(nomClient) || "ClsEntreprise.SONIBANK".equalsIgnoreCase(nomClient)){
			if(listeRubriquesAAjouterAuNet!=null && listeRubriquesAAjouterAuNet.size()>=1)
				queryString += " and a.snet = 'O'";
			else queryString += " and a.snet = 'N'";
			queryString += " and (exists (";
			queryString += " select 'X' from ElementVariableDetailMois c";
			queryString += " where c.identreprise = a.identreprise";
			queryString += " and c.rubq = a.crub";
			queryString += " and c.aamm = '"+getMoisPaieCourant()+"'";
			queryString += " and c.nbul = '"+getNumeroBulletin()+"'";
			queryString += " )";
			queryString += " or exists (";
			queryString += " select 'X' from ElementFixeSalaire c";
			queryString += " where c.identreprise = a.identreprise";
			queryString += " and c.codp = a.crub";
			queryString += " )";
			queryString += " )";
		}
		ElementSalaireAjus oParubajus = null;
		row = null;
		listOfRubrique = service.find(queryString);
		if(listeRubriquesAAjuster!=null)
			listeRubriquesAAjuster.clear();
		for (Object object : listOfRubrique)
		{
			row = (Object[]) object;
			oParubajus = new ElementSalaireAjus();
			// if(row[0] != null)
			// oParubajus.setSessionId(new BigDecimal((Integer)row[0]));
			if (row[1] != null)
				oParubajus.setCrub((String) row[1]);
			if (row[2] != null)
				oParubajus.setAjnu(((Long) row[2]).intValue());
			oParubajus.setMont(new BigDecimal(0));
			oParubajus.setSessionId(new BigDecimal(sessionId));
			//
			service.save(oParubajus);
			
			listeRubriquesAAjuster.add(oParubajus.getCrub());
		}
		// i := 0;
		// SELECT COUNT(*) INTO i FROM parubajus WHERE session_id = w_session_id;
		// IF PA_PAIE.NouZ(i) THEN
		// rubajus_exist := FALSE;
		// END IF;
		//
		queryString = "select count(*) from ElementSalaireajus " + " where session_id ='" + sessionId + "'";
		listOfRubrique = service.find(queryString);
		if (listOfRubrique != null && listOfRubrique.size() > 0)
		{
			Object obj = listOfRubrique.get(0);
			if (Integer.valueOf(obj.toString()) != 0)
				this.setRubriqueAdjustExist(true);
		}
		// -- LH 140298
		// -- Chargement table valeurs ajustement
		// TB_valajus(22) := 2000000;
		// TB_valajus(21) := 1000000;
		// TB_valajus(20) := 500000;
		// TB_valajus(19) := 250000;
		// TB_valajus(18) := 125000;
		// TB_valajus(17) := 100000;
		// TB_valajus(16) := 50000;
		// TB_valajus(15) := 25000;
		// TB_valajus(14) := 10000;
		// TB_valajus(13) := 5000;
		// TB_valajus(12) := 2500;
		// TB_valajus(11) := 1000;
		// TB_valajus(10) := 500;
		// TB_valajus(9) := 250;
		// TB_valajus(8) := 100;
		// TB_valajus(7) := 50;
		// TB_valajus(6) := 25;
		// TB_valajus(5) := 10;
		// TB_valajus(4) := 5;
		// TB_valajus(3) := 3;
		// TB_valajus(2) := 2;
		// TB_valajus(1) := 1;
		tableOfAdjustmentValues = new double[23];
		tableOfAdjustmentValues[22] = 2000000;
		tableOfAdjustmentValues[21] = 1000000;
		tableOfAdjustmentValues[20] = 500000;
		tableOfAdjustmentValues[19] = 250000;
		tableOfAdjustmentValues[18] = 125000;
		tableOfAdjustmentValues[17] = 100000;
		tableOfAdjustmentValues[16] = 50000;
		tableOfAdjustmentValues[15] = 25000;
		tableOfAdjustmentValues[14] = 10000;
		tableOfAdjustmentValues[13] = 5000;
		tableOfAdjustmentValues[12] = 2500;
		tableOfAdjustmentValues[11] = 1000;
		tableOfAdjustmentValues[10] = 500;
		tableOfAdjustmentValues[9] = 250;
		tableOfAdjustmentValues[8] = 100;
		tableOfAdjustmentValues[7] = 50;
		tableOfAdjustmentValues[6] = 25;
		tableOfAdjustmentValues[5] = 10;
		tableOfAdjustmentValues[4] = 5;
		tableOfAdjustmentValues[3] = 3;
		tableOfAdjustmentValues[2] = 2;
		tableOfAdjustmentValues[1] = 1;
		//
		// RETURN TRUE;
	}

	/**
	 * => Charg_Tabrubreg initialise the list of rubrique to calculate -- Chargement de - t_rub avec les rubriques a calculer (Table memoire) -- t_rub_basc avec
	 * le nombre de rubriques de la formule -- et indice dans tab_basc -- tab_basc avec les formules de calcul -- tab_rub avec les rubriques concernees par les
	 * regul.
	 */
	public void chargementListeRubriqueACalculer()
	{
		// com.cdi.deltarh.service.ClsParameter.println(">>chargementListeRubriqueACalculer");
		// must use listOfRubriqueToCalculate
		// CURSOR curs_rubreg IS
		// SELECT rcon FROM parubq
		// WHERE cdos = wpdos.identreprise
		// AND rreg = 'O'
		// AND rman = 'N'
		// ORDER BY cdos, crub;
		//
		String queryString = "select rcon from ElementSalaire where cdos ='" + dossier + "'" + " and rreg = 'O'" + " and rman = 'N'" + " order by cdos, crub";
		// CURSOR curs_rubreg2 IS
		// SELECT rcon FROM pahrubq
		// WHERE cdos = wpdos.identreprise
		// AND rreg = 'O'
		// AND rman = 'N'
		// AND aamm = w_aamm
		// AND nbul = wsd_fcal1.nbul
		// ORDER BY cdos, crub;
		String queryStringRetro = "select rcon from Rhthrubq where cdos ='" + dossier + "'" + " and rreg = 'O'" + " and rman = 'N'" + " and aamm = '" + monthOfPay + "'" + " and nbul = " + numeroBulletin
				+ " order by cdos, crub";

		List listOfRubrique = (useRetroactif) ? service.find(queryStringRetro) : service.find(queryString);
		//
		// BEGIN
		//
		// IF retroactif THEN
		// OPEN curs_rubreg2;
		// ELSE
		// OPEN curs_rubreg;
		// END IF;
		// nb_tab_rub := 1;
		// LOOP
		listOfRubriqueToCalculate = new ArrayList<String>();
		boolean listSet = false;
		for (Object object : listOfRubrique)
		{
			if (!listSet)
			{
				listOfRubriqueToCalculate = new ArrayList<String>();
				listSet = true;
			}
			listOfRubriqueToCalculate.add((String) object);
		}
		// IF retroactif THEN
		// FETCH curs_rubreg2 INTO tab_rub(nb_tab_rub);
		// EXIT WHEN curs_rubreg2%NOTFOUND;
		// ELSE
		// FETCH curs_rubreg INTO tab_rub(nb_tab_rub);
		// EXIT WHEN curs_rubreg%NOTFOUND;
		// END IF;
		//
		// nb_tab_rub := nb_tab_rub + 1;
		// END LOOP;
		// IF retroactif THEN
		// nb_tab_rub := curs_rubreg2%ROWCOUNT;
		// CLOSE curs_rubreg2;
		// ELSE
		// nb_tab_rub := curs_rubreg%ROWCOUNT;
		// CLOSE curs_rubreg;
		// END IF;
		//
		// RETURN TRUE;
	}
	
	public void chargerMoisDonnees()
	{
		this.moisDonnees = new HashMap<String, Integer>();
		List<ParamData> liste = service.find("From ParamData where cdos = '"+dossier+"' and ctab = 66 and nume = 1 order by cacc");
		for(ParamData nome : liste)
		{
			if(nome.getValm() != null && nome.getValm().intValue() != 0)
				moisDonnees.put(nome.getCacc(), nome.getValm().intValue());
		}
	}

	/**
	 * => Chg_TB10 initialise the list of banks -- Chargement de la table des banques.
	 */
	public void chargementListeBanques()
	{
		// com.cdi.deltarh.service.ClsParameter.println(">>chargementListeBanques");
		// must use listOfBank
		// Code_Banque pafnom.cacc%TYPE;
		// Devise_Banque pafnom.vall%TYPE;
		// Taux_Change pafnom.valt%TYPE;
		// Nb_Dec pafnom.valm%TYPE;
		// Vrmt_Mini pafnom.valm%TYPE;
		//
		// -- Curseur sur les banques
		// CURSOR Curs_TB10 IS
		// SELECT cacc, vall, NVL(valm, 0) FROM pafnom
		// WHERE cdos = wpdos.identreprise
		// AND ctab = 10
		// AND nume = 3
		// ORDER BY cacc;
		//
		// String queryString10 = "select cacc, vall, valm from ParamData " + " where cdos ='" + dossier + "'" + " and ctab = 10" + " and
		// nume = 3" + " order by cacc";
		// -- Curseur sur les banques
		// CURSOR Curs_TB10_2 IS
		// SELECT cacc, vall, NVL(valm, 0) FROM pahfnom
		// WHERE cdos = wpdos.identreprise
		// AND ctab = 10
		// AND nume = 3
		// AND aamm = w_aamm
		// AND nbul = wsd_fcal1.nbul
		// ORDER BY cacc;
		// String queryString10Retro = "select cacc, vall, valm from Rhthfnom " + " where cdos ='" + dossier + "'" + " and ctab = 10" + " and
		// nume = 3" + " and aamm = '"
		// + monthOfPay + "'" + " and nbul = " + numeroBulletin + " order by cacc";
		//
		// -- Curseur sur les devises
		// CURSOR Curs_TB27 IS
		// SELECT valt, valm FROM pafnom
		// WHERE cdos = wpdos.identreprise
		// AND ctab = 27
		// AND cacc = Devise_Banque
		// AND nume = 1;
		//
		// String queryString27 = "select valt, valm from ParamData " + " where cdos ='" + dossier + "'" + " and ctab = 27" + " and nume = 1" + "
		// and cacc = '" + bankCcy + "'"
		// + " order by cacc";
		// -- Curseur sur les devises
		// CURSOR Curs_TB27_2 IS
		// SELECT valt, valm FROM pahfnom
		// WHERE cdos = wpdos.identreprise
		// AND ctab = 27
		// AND cacc = Devise_Banque
		// AND aamm = w_aamm
		// AND nbul = wsd_fcal1.nbul
		// AND nume = 1;
		// String queryString27Retro = "select valt, valm from Rhthfnom " + " where cdos ='" + dossier + "'" + " and ctab = 27" + " and nume =
		// 1" + " and cacc = '" + bankCcy
		// + "'" + " and aamm = '" + monthOfPay + "'" + " and nbul = " + numeroBulletin + " order by cacc";
		//
		ClsInfoOfBank oInfoOfBank = null;
		String queryString10Retro = "SELECT banque.cacc as codebanque, banque.vall as devisebanque, banque.valm as virmini , " + " devise.valt as tauxchange, devise.valm as nbdecimale " + "FROM Rhthfnom banque "
				+ "Left join Rhthfnom devise on( " + "banque.identreprise=devise.identreprise and devise.ctab=27 and devise.nume=1 and devise.cacc=banque.vall " + "and banque.aamm = devise.aamm and banque.nbul = devise.nbul) "
				+ "WHERE banque.identreprise = '" + dossier + "' " + "AND banque.ctab = 10 " + "AND banque.nume = 3 " + "AND devise.aamm = '" + monthOfPay + "' " + "AND banque.nbul = " + numeroBulletin + " "
				+ "ORDER BY banque.cacc ";
		String queryString10 = "SELECT banque.cacc as codebanque, banque.vall as devisebanque, banque.valm as virmini , " + " devise.valt as tauxchange, devise.valm as nbdecimale " + "FROM ParamData banque "
				+ "Left join ParamData devise on( " + "banque.identreprise=devise.identreprise and devise.ctab=27 and devise.nume=1 and devise.cacc=banque.vall) " + "WHERE banque.identreprise = '" + dossier + "' " + "AND banque.ctab = 10 "
				+ "AND banque.nume = 3 " + "ORDER BY banque.cacc ";

		Session session = service.getSession();
		String query = useRetroactif ? queryString10Retro : queryString10;
		List liste = session.createSQLQuery(query).list();

		Object[] ligne = null;
		listOfBank = new ArrayList<ClsInfoOfBank>();
		for (Object object : liste)
		{
			ligne = (Object[]) object;
			oInfoOfBank = new ClsInfoOfBank();
			oInfoOfBank.setCode((String) ligne[0]);
			if (ligne[1] != null)
			{
				oInfoOfBank.setCcy((String) ligne[1]);
				boolean isForeignBank = !(((String) ligne[1]).equals(dossierCcy));
				oInfoOfBank.setForeignBank(isForeignBank);
			}
			else
				oInfoOfBank.setForeignBank(false);
			if (ligne[2] != null)
				oInfoOfBank.setVirementMini(NumberUtils.createLong(String.valueOf(ligne[2])));

			if (ligne[3] != null)
				oInfoOfBank.setExchangeRate(NumberUtils.createDouble(String.valueOf(ligne[3])));

			if (ligne[4] != null)
				oInfoOfBank.setNbreDecimal(NumberUtils.createInteger(String.valueOf(ligne[4])));

			listOfBank.add(oInfoOfBank);
		}
		service.closeSession(session);

		// List listOfRatesAndAmount10 = (useRetroactif) ? service.find(queryString10Retro) : service.find(queryString10);
		// List listOfRatesAndAmount27 = (useRetroactif) ? service.find(queryString27Retro) : service.find(queryString27);
		// Object[] infoBank = null;
		// if (listOfRatesAndAmount27 != null && listOfRatesAndAmount27.size() > 0)
		// {
		// infoBank = (Object[]) listOfRatesAndAmount27.get(0);
		// }
		//		
		// Object[] row = null;
		// boolean listSet = false;
		// for (Object object : listOfRatesAndAmount10)
		// {
		// if (!listSet)
		// {
		// listOfBank = new ArrayList<ClsInfoOfBank>();
		// listSet = true;
		// }
		// row = (Object[]) object;
		// oInfoOfBank = new ClsInfoOfBank();
		// oInfoOfBank.setCode((String) row[0]);
		// if (row[1] != null)
		// {
		// oInfoOfBank.setCcy((String) row[1]);
		// boolean isForeignBank = !(((String) row[1]).equals(dossierCcy));
		// oInfoOfBank.setForeignBank(isForeignBank);
		// }
		// else
		// oInfoOfBank.setForeignBank(false);
		// if (row[2] != null)
		// oInfoOfBank.setVirementMini((Long) row[2]);
		// if (infoBank != null && infoBank.length >= 0)
		// {
		// if (infoBank[0] != null)
		// oInfoOfBank.setExchangeRate(((BigDecimal) infoBank[0]).doubleValue());
		// if (infoBank[1] != null)
		// oInfoOfBank.setNbreDecimal(((Long) infoBank[1]).intValue());
		// }
		// //
		// listOfBank.add(oInfoOfBank);
		// }
	}

	public List<Double> getPlafondOfMonthList()
	{
		return plafondOfMonthList;
	}

	public void setPlafondOfMonthList(List<Double> plafondOfMonthList)
	{
		this.plafondOfMonthList = plafondOfMonthList;
	}

	public String getBouclage()
	{
		return bouclage;
	}

	public void setBouclage(String bouclage)
	{
		this.bouclage = bouclage;
	}

	/**
	 * Permet d'initialiser tous les param�tres pour effectuer le calcul de la paie. Ces param�tres sont globaux et s'applique donc � tous les objets
	 * responsables du calcul.
	 * 
	 * @return true ou false quand il y a un param�tre qui manque de valeur
	 */
	@SuppressWarnings("unchecked")
	public boolean init()
	{
		useRetroactif = monthOfPay.compareTo(moisPaieCourant) < 0;
		if (StringUtils.isBlank(monthOfPay))
			useRetroactif = false;
		
		// Determination du nom du client pour la gestion des sp�cifiques
		nomClient = "";//ClsConfigurationParameters.getConfigParameterValue(service, dossier, langue, ClsConfigurationParameters.NOM_CLIENT);

		String agemax = "80";//ClsConfigurationParameters.getConfigParameterValue(this.getService(), dossier, langue, ClsConfigurationParameters.AGE_MAX_AGENT);
		AGE_MAX_OF_SALARY = Integer.valueOf(agemax);
		String formatRub = "0000";
		FORMAT_PERIOD_OF_RBQ = formatRub;
		@SuppressWarnings("unchecked")
		List<Object[]> l = service.find("select ddmp, ddex, dfex from DossierPaie where identreprise = '" + dossier + "'");
		Object[] oPado = null;
		if (l != null && l.size() > 0)
		{
			oPado = (Object[]) l.get(0);
			ddmp = (Date) oPado[0];
			dtDdex = (Date) oPado[1];
			dtDfex = (Date) oPado[2];
			setDossierDateDebutExercice(new ClsDate(dtDdex).getDateS(appDateFormat));
			setDossierDateFinExercice(new ClsDate(dtDfex).getDateS(appDateFormat));

			// myMoisPaieCourant = new ClsDate(dtDdmp);
			myMoisPaieCourant = new ClsDate(new ClsDate(ddmp).addMonth(1));
			moisPaieCourant = myMoisPaieCourant.getYearAndMonth();
			ClsDate myExcercise = new ClsDate(this.getDossierDateDebutExercice(), appDateFormat);
			debutExerciceAnnee = myExcercise.getYear();
			debutExerciceMois = myExcercise.getMonth();
			debutExerciceaamm = debutExerciceAnnee * 100 + debutExerciceMois;
			debutExerciceRangMoisPaie = this.getMyMonthOfPay().getMonth() - debutExerciceMois + 1;
		}
		else
		{
			error = errorMessage("ERR-90007", langue, dossier);
			setError(error);

			return false;
		}

		List<DossierPaie> list2 = null;
		try
		{
			list2 = service.find("from Cpdo where cdos = '" + dossier + "'");
		}
		catch (DataAccessException e)
		{
			error = errorMessage("ERR-90008", langue, dossier);

			return false;
		}
		if (list2 != null && list2.size() > 0)
		{
			dossierInfo = list2.get(0);
			dossierCcy = dossierInfo.getDdev();
			dossierNbreDecimale = dossierInfo.getNddd() == null ? 0 : dossierInfo.getNddd().intValue();

		}
		else
		{
			error = "Dossier " + dossier + " inconnu dans cpdos";
			error = errorMessage("ERR-30032", langue, dossier) + "(cpdos)";

			return false;
		}

		// prec_niv3 := ' ';
		// err_msg := ' ';
		// w_calcul := 'N';
		this.setCalcul("N");
		//
		// ----------------------------------------------------------------------------
		// -- PAIE AU NET : Initialisations
		// ----------------------------------------------------------------------------
		// w_paienet := 'N';
		// w_bouclage := 'N';
		this.setBouclage("N");
		//
		// wan := TO_NUMBER(SUBSTR(w_aamm,1,4));
		// wmoi := TO_NUMBER(SUBSTR(w_aamm,5,2));
		//
		// -- Recherche du nombre de jours du mois pour calcul des proratas
		// nbm_jrn := TO_NUMBER(TO_CHAR(LAST_DAY(TO_DATE(w_aamm,'YYYYMM')),'DD'));
		// Nb_jrs_Mois := nbm_jrn;
		this.nbreJourMoisPourProrata = this.myMonthOfPay.getMaxDayOfMonth();
		//
		// -------------------------------------------------------------------------------
		// -- Calcul du rang du mois de paie dans l' exercice
		// -------------------------------------------------------------------------------
		// Rg_Mois_Exercice := wmoi - TO_NUMBER(TO_CHAR(wpdos.ddex,'MM')) + 1;
		// IF Rg_Mois_Exercice <= 0 THEN
		// Rg_Mois_Exercice := Rg_Mois_Exercice + 12;
		// END IF;
		if (!ClsObjectUtil.isNull(dtDdex))
		{
			this.rangMoisDePaieExercice = this.myMonthOfPay.getMonth() - new ClsDate(dtDdex).getMonth() + 1;
			if (this.rangMoisDePaieExercice <= 0)
				this.rangMoisDePaieExercice += 12;
		}
		//
		// -- Lecture du montant minimum d'une regularisation en table 99
		// -- reg_cas : 'J'=jour 'T'=Uniq Nb mois travailles ' '=Normal
		// reg_cas := SUBSTR(paf_lecfnomL(99,'REGMINI',1,w_aamm,w_nlot,wsd_fcal1.nbul),1,1);
		// IF reg_cas IS NULL THEN
		// err_msg := PA_PAIE.erreurp('ERR-90009',w_clang);
		// if('O' == this.getGenfile()) return false;
		// END IF;
		// reg_mini := paf_lecfnomT(99,'REGMINI',1,w_aamm,w_nlot,wsd_fcal1.nbul);
		// dec4 := paf_lecfnomM(99,'REGMINI',1,w_aamm,w_nlot,wsd_fcal1.nbul);
		// rub_regper := LTRIM(TO_CHAR(dec4,'0999'));
		// charge la table 99
		buildTableT51T52T99Map();
		// Lecture du montant minimum d'une regularisation en table 99
		// String libelle = utilNomenclature.getLabelFromNomenclature(dossier, 99, "REGMINI", 1, nlot, numeroBulletin, moisPaieCourant, periodOfPay);
		String libelle = utilNomenclature.getLabelFromT99(listOfTable99Map, dossier, 99, "REGMINI", 1, nlot, moisPaieCourant, periodOfPay);
		if (ClsObjectUtil.isNull(libelle))
		{
			error = errorMessage("ERR-90009", langue);

			return false;
		}
		regularisationCas = StringUtils.substring(libelle, 0, 1).toUpperCase();
		double tamo = (tempNumber = utilNomenclature.getAmountOrRateFromT99(listOfTable99Map, dossier, 99, "REGMINI", 1, nlot, moisPaieCourant, periodOfPay, ClsEnumeration.EnColumnToRead.AMOUNT)) == null ? 0
				: tempNumber.doubleValue();
		if (tamo > 0)
			regularisationRubrique = ClsStringUtil.formatNumber(tamo, FORMAT_PERIOD_OF_RBQ);
		// tamo = utilNomenclature.getAmountOrRateFromNomenclature(dossier, 99, "REGMINI", 1, nlot, numeroBulletin, moisPaieCourant, periodOfPay,
		// ClsEnumeration.EnColumnToRead.RATES);
		tamo = (tempNumber = utilNomenclature.getAmountOrRateFromT99(listOfTable99Map, dossier, 99, "REGMINI", 1, nlot, moisPaieCourant, periodOfPay, ClsEnumeration.EnColumnToRead.RATES)) == null ? 0 : tempNumber
				.doubleValue();
		regularisationMinimale = tamo;
		//
		// -- Permet de parametrer les colonnes Base pour les prets
		// base_pret := SUBSTR(nvl(paf_LecfnomL( 99, 'GESPRT', 1,w_aamm,w_nlot,wsd_fcal1.nbul),' '),1,3);
		// IF base_pret = ' '
		// THEN
		// err_msg := PA_PAIE.erreurp('ERR-90010',w_clang,'(L1)');
		// if('O' == this.getGenfile()) return false;
		// END IF;
		//
		// -- Permet de parametrer les colonnes Taux pour les prets
		// taux_pret := SUBSTR(nvl(paf_LecfnomL(99, 'GESPRT', 2,w_aamm,w_nlot,wsd_fcal1.nbul),' '),1,3);
		// IF taux_pret = ' '
		// THEN
		// err_msg := PA_PAIE.erreurp('ERR-90010',w_clang,'(L2)');
		// RETURN FALSE;
		// END IF;
		// libelle = utilNomenclature.getLabelFromNomenclature(dossier, 99, "GESPRT", 1, nlot, numeroBulletin, moisPaieCourant, periodOfPay);
		libelle = utilNomenclature.getLabelFromT99(listOfTable99Map, dossier, 99, "GESPRT", 1, nlot, moisPaieCourant, periodOfPay);
		if (ClsObjectUtil.isNull(libelle))
		{
			error = errorMessage("ERR-90010", langue, "(L1)");

			return false;
		}
		BasePrets = libelle;
		// libelle = utilNomenclature.getLabelFromNomenclature(dossier, 99, "GESPRT", 2, nlot, numeroBulletin, moisPaieCourant, periodOfPay);
		libelle = utilNomenclature.getLabelFromT99(listOfTable99Map, dossier, 99, "GESPRT", 2, nlot, moisPaieCourant, periodOfPay);
		if (ClsObjectUtil.isNull(libelle))
		{
			error = errorMessage("ERR-90010", langue, "(L2)");

			return false;
		}
		BasePretsTaux = libelle;
		//
		// -- LH 210198
		// -- Permet de parametrer le solde des prets en cas de radiation
		// dec1 := nvl(paf_LecfnomM( 99, 'GESPRT', 3,w_aamm,w_nlot,wsd_fcal1.nbul), 9);
		// IF NOT ( dec1 = 0 OR dec1 = 1 )
		// THEN
		// err_msg := PA_PAIE.erreurp('ERR-90011',w_clang);
		// RETURN FALSE;
		// ELSE
		// IF dec1 = 1
		// THEN
		// STC_Prets := TRUE;
		// ELSE
		// STC_Prets := FALSE;
		// END IF;
		// END IF;
		//
		// tamo = utilNomenclature.getAmountOrRateFromNomenclature(dossier, 99, "GESPRT", 3, nlot, numeroBulletin, moisPaieCourant, periodOfPay,
		// ClsEnumeration.EnColumnToRead.AMOUNT);
		tamo = (tempNumber = utilNomenclature.getAmountOrRateFromT99(listOfTable99Map, dossier, 99, "GESPRT", 3, nlot, moisPaieCourant, periodOfPay, ClsEnumeration.EnColumnToRead.AMOUNT)) == null ? 0 : tempNumber
				.doubleValue();
		if (tamo != 0 && tamo != 1)
		{
			error = errorMessage("ERR-90011", langue);

			return false;
		}
		enBasePretsSTC = tamo == 1 ? ClsEnumeration.EnLoan.STC : ClsEnumeration.EnLoan.UNKNOWN;
		// -- Lecture des parametres plafond caisses francaises
		// l_rub_plaf := NVL(paf_LecfnomM( 99, 'PLAF-SS', 1,w_aamm,w_nlot,wsd_fcal1.nbul),0);
		// l_plafsem1 := NVL(paf_LecfnomM(99, 'PLAF-SS', 2,w_aamm,w_nlot,wsd_fcal1.nbul),0);
		// l_plafsem2 := NVL(paf_LecfnomM(99, 'PLAF-SS', 3,w_aamm,w_nlot,wsd_fcal1.nbul),0);
		//
		// IF NOT PA_PAIE.NouZ(l_rub_plaf) THEN
		// rub_plaf := LTRIM(TO_CHAR(l_rub_plaf,'0999'));
		// ELSE
		// rub_plaf := ' ';
		// END IF;
		//
		// FOR l_indcf IN 1..12 LOOP
		// IF l_indcf < 7 THEN
		// plafond(l_indcf) := l_plafsem1;
		// ELSE
		// plafond(l_indcf) := l_plafsem2;
		// END IF;
		// END LOOP;
		// tamo = utilNomenclature.getAmountOrRateFromNomenclature(dossier, 99, "PLAF-SS", 1, nlot, numeroBulletin, moisPaieCourant, periodOfPay,
		// ClsEnumeration.EnColumnToRead.AMOUNT);
		tamo = (tempNumber = utilNomenclature.getAmountOrRateFromT99(listOfTable99Map, dossier, 99, "PLAF-SS", 1, nlot, moisPaieCourant, periodOfPay, ClsEnumeration.EnColumnToRead.AMOUNT)) == null ? 0 : tempNumber
				.doubleValue();
		// double tamo1 = utilNomenclature.getAmountOrRateFromNomenclature(dossier, 99, "PLAF-SS", 2, nlot, numeroBulletin, moisPaieCourant, periodOfPay,
		// ClsEnumeration.EnColumnToRead.AMOUNT);
		// double tamo2 = utilNomenclature.getAmountOrRateFromNomenclature(dossier, 99, "PLAF-SS", 3, nlot, numeroBulletin, moisPaieCourant, periodOfPay,
		// ClsEnumeration.EnColumnToRead.AMOUNT);
		double tamo1 = (tempNumber = utilNomenclature.getAmountOrRateFromT99(listOfTable99Map, dossier, 99, "PLAF-SS", 2, nlot, moisPaieCourant, periodOfPay, ClsEnumeration.EnColumnToRead.AMOUNT)) == null ? 0
				: tempNumber.doubleValue();
		double tamo2 = (tempNumber = utilNomenclature.getAmountOrRateFromT99(listOfTable99Map, dossier, 99, "PLAF-SS", 3, nlot, moisPaieCourant, periodOfPay, ClsEnumeration.EnColumnToRead.AMOUNT)) == null ? 0
				: tempNumber.doubleValue();
		plafondRubrique = tamo > 0 ? ClsStringUtil.formatNumber(tamo, FORMAT_PERIOD_OF_RBQ) : "    ";
		plafondOfMonthList = new ArrayList<Double>();
		for (int i = 0; i < 12; i++)
		{
			if (i < 6)
				plafondOfMonthList.add(tamo1);
			else
				plafondOfMonthList.add(tamo2);
		}
		//
		// -- Lecture rubriques nb heures travaillees, nb jours travailles
		// dec4 := paf_LecfnomM(99, 'HORAIRE', 1,w_aamm,w_nlot,wsd_fcal1.nbul);
		// dec5 := paf_LecfnomM(99, 'HORAIRE', 2,w_aamm,w_nlot,wsd_fcal1.nbul);
		// IF (dec4 IS NULL) OR (dec5 IS NULL) THEN
		// err_msg := PA_PAIE.erreurp('ERR-90012',w_clang);
		// RETURN FALSE;
		// END IF;
		//
		// IF NOT PA_PAIE.NouZ(dec4) THEN
		// rub_hor := LTRIM(TO_CHAR(dec4,'0999'));
		// ELSE
		// rub_hor := ' ';
		// END IF;
		// IF NOT PA_PAIE.NouZ(dec5) THEN
		// rub_nbj := LTRIM(TO_CHAR(dec5,'0999'));
		// ELSE
		// rub_nbj := ' ';
		// END IF;
		//
		// tamo = utilNomenclature.getAmountOrRateFromNomenclature(dossier, 99, "HORAIRE", 1, nlot, numeroBulletin, moisPaieCourant, periodOfPay,
		// ClsEnumeration.EnColumnToRead.AMOUNT);
		// tamo1 = utilNomenclature.getAmountOrRateFromNomenclature(dossier, 99, "HORAIRE", 2, nlot, numeroBulletin, moisPaieCourant, periodOfPay,
		// ClsEnumeration.EnColumnToRead.AMOUNT);
		tamo = (tempNumber = utilNomenclature.getAmountOrRateFromT99(listOfTable99Map, dossier, 99, "HORAIRE", 1, nlot, moisPaieCourant, periodOfPay, ClsEnumeration.EnColumnToRead.AMOUNT)) == null ? 0 : tempNumber
				.doubleValue();
		tamo1 = (tempNumber2 = utilNomenclature.getAmountOrRateFromT99(listOfTable99Map, dossier, 99, "HORAIRE", 2, nlot, moisPaieCourant, periodOfPay, ClsEnumeration.EnColumnToRead.AMOUNT)) == null ? 0 : tempNumber2
				.doubleValue();
		
		if (tempNumber == null || tempNumber2 == null)
		{
			error = errorMessage("ERR-90012", langue);

			return false;
		}
		horaireRubrique = tamo > 0 ? ClsStringUtil.formatNumber(tamo, FORMAT_PERIOD_OF_RBQ) : "    ";
		horaireNumberOfDayRubrique = tamo1 > 0 ? ClsStringUtil.formatNumber(tamo1, FORMAT_PERIOD_OF_RBQ) : "    ";
		
		tamo1 = (tempNumber2 = utilNomenclature.getAmountOrRateFromT99(listOfTable99Map, dossier, 99, "HORAIRE", 3, nlot, moisPaieCourant, periodOfPay, ClsEnumeration.EnColumnToRead.AMOUNT)) == null ? 0 : tempNumber2
				.doubleValue();
		
		horaireNuitRubrique = tamo1 > 0 ? ClsStringUtil.formatNumber(tamo1, FORMAT_PERIOD_OF_RBQ) : "    ";
		
		// -- Lecture Minimum Net a Payer
		// w_bulneg := paf_LecfnomM(99, 'BULNEG', 1,w_aamm,w_nlot,wsd_fcal1.nbul);
		// IF w_bulneg IS NULL THEN
		// err_msg := PA_PAIE.erreurp('ERR-90013',w_clang);
		// RETURN FALSE;
		// END IF;
		//
		// tamo = utilNomenclature.getAmountOrRateFromNomenclature(dossier, 99, "BULNEG", 1, nlot, numeroBulletin, moisPaieCourant, periodOfPay,
		// ClsEnumeration.EnColumnToRead.AMOUNT);
		tamo = (tempNumber = utilNomenclature.getAmountOrRateFromT99(listOfTable99Map, dossier, 99, "BULNEG", 1, nlot, moisPaieCourant, periodOfPay, ClsEnumeration.EnColumnToRead.AMOUNT)) == null ? 0 : tempNumber
				.doubleValue();
		if (tamo < 0)
		{
			error = errorMessage("ERR-90013", langue);

			return false;
		}
		bulletinNegative = tamo;
		// -- Calcul fictif ?
		// w_fictif := Substr(paf_LecfnomL(99, 'FICTIF', 1,w_aamm,w_nlot,wsd_fcal1.nbul), 1, 1);
		// IF w_fictif IS NULL THEN
		// err_msg := PA_PAIE.erreurp('ERR-90014',w_clang);
		// RETURN FALSE;
		// END IF;
		// IF NOT (w_fictif = 'O' OR w_fictif = 'N' )
		// THEN
		// err_msg := PA_PAIE.erreurp('ERR-90015',w_clang);
		// RETURN FALSE;
		// END IF;
		// libelle = utilNomenclature.getLabelFromNomenclature(dossier, 99, "FICTIF", 1, nlot, numeroBulletin, moisPaieCourant, periodOfPay);
		libelle = utilNomenclature.getLabelFromT99(listOfTable99Map, dossier, 99, "FICTIF", 1, nlot, moisPaieCourant, periodOfPay);
		if (ClsObjectUtil.isNull(libelle))
		{
			error = errorMessage("ERR-90014", langue);

			return false;
		}
		libelle = StringUtils.substring(libelle, 0, 1);
		if (!("O".equals(libelle) || "N".equals(libelle)))
		{
			error = errorMessage("ERR-90015", langue);

			return false;
		}
		fictiveCalculus = libelle;
		//
		// -- Type de calcul fictif ?
		// IF w_fictif = 'O' THEN
		// w_typ_fictif := Substr(paf_LecfnomL(99, 'FICTIF', 2,w_aamm,w_nlot,wsd_fcal1.nbul), 1, 1);
		// IF w_typ_fictif IS NULL THEN
		// err_msg := PA_PAIE.erreurp('ERR-90016',w_clang,'2');
		// RETURN FALSE;
		// END IF;
		// IF NOT (w_typ_fictif = 'A' OR w_typ_fictif = 'B' )
		// THEN
		// err_msg := PA_PAIE.erreurp('ERR-90017',w_clang);
		// RETURN FALSE;
		// END IF;
		// END IF;
		//
		// Fictif_A := FALSE;
		// Fictif_B := FALSE;
		// Fictif_Non := FALSE;
		// IF w_fictif = 'N'
		// THEN
		// Fictif_Non := TRUE;
		// ELSE
		// IF w_typ_fictif = 'A'
		// THEN
		// Fictif_A := TRUE;
		// ELSE
		// Fictif_B := TRUE;
		// END IF;
		// END IF;
		if ("O".equals(fictiveCalculus))
		{
			// libelle = utilNomenclature.getLabelFromNomenclature(dossier, 99, "FICTIF", 2, nlot, numeroBulletin, moisPaieCourant, periodOfPay);
			libelle = utilNomenclature.getLabelFromT99(listOfTable99Map, dossier, 99, "FICTIF", 2, nlot, moisPaieCourant, periodOfPay);
			if (ClsObjectUtil.isNull(libelle))
			{
				error = errorMessage("ERR-90016", langue,"2");

				return false;
			}
			libelle = StringUtils.substring(libelle, 0, 1);
			if (!("A".equals(libelle) || "B".equals(libelle)|| "C".equals(libelle)))
			{
				error = errorMessage("ERR-90017", langue);

				return false;
			}

			fictiveCalculusType = libelle;
		}
		fictiveCalculusA = false;
		fictiveCalculusB = false;
		fictiveCalculusNon = false;
		if ("N".equals(fictiveCalculus))
			fictiveCalculusNon = true;
		else
		{
			if ("A".equals(fictiveCalculusType))
				fictiveCalculusA = true;
			else
				fictiveCalculusB = true;
		}
		//
		// -- LH 261197
		// -- Calcul du fictif pour les expatries ?
		// IF w_fictif = 'O' THEN
		// char1 := Substr(paf_LecfnomL(99, 'FICTIF', 3,w_aamm,w_nlot,wsd_fcal1.nbul), 1, 1);
		// IF char1 IS NULL THEN
		// err_msg := PA_PAIE.erreurp('ERR-90016',w_clang,'3');
		// RETURN FALSE;
		// END IF;
		// IF NOT ( char1 = 'O' OR char1 = 'N' )
		// THEN
		// err_msg := PA_PAIE.erreurp('ERR-90018',w_clang);
		// RETURN FALSE;
		// END IF;
		// END IF;
		// IF char1 = 'O'
		// THEN
		// Fictif_Expat := TRUE;
		// ELSE
		// Fictif_Expat := FALSE;
		// END IF;
		// Calcul du fictif pour les expatries ?
		if ("O".equals(fictiveCalculus))
		{
			// libelle = utilNomenclature.getLabelFromNomenclature(dossier, 99, "FICTIF", 3, nlot, numeroBulletin, moisPaieCourant, periodOfPay);
			libelle = utilNomenclature.getLabelFromT99(listOfTable99Map, dossier, 99, "FICTIF", 3, nlot, moisPaieCourant, periodOfPay);
			if (ClsObjectUtil.isNull(libelle))
			{
				error = errorMessage("ERR-90016", langue,"3");

				return false;
			}
			libelle = StringUtils.substring(libelle, 0, 1);
			if (!("O".equals(libelle) || "N".equals(libelle)))
			{
				error = errorMessage("ERR-90018", langue);

				return false;
			}
		}
		fictiveCalculusForExpatrie = ("O".equals(libelle)) ? true : false;
		
		
		// -- Lecture Mnt1 = Rubrique somme des nets a payer (avance conges)
		// -- Lecture Mnt2 = jours minimum pour calcul rub. dernier mois
		String query = " SELECT sum(case nume when 1 then valm else 0 end ) as somme1, sum(case nume when 2 then valm else 0 end) as somme2";
		query += "  FROM ParamData WHERE cdos = '" + dossier + "' AND ctab = 99 AND cacc = 'FICTIF' AND nume IN (1,2)";
		List<Object[]> ls = this.getService().find(query);
		if ((!ls.isEmpty()) && ls.get(0)[0] != null)
		{
			rubriqueAvanceConge = ClsStringUtil.formatNumber((Long) ls.get(0)[0], FORMAT_PERIOD_OF_RBQ);
		}
		
		//
		// -- Anciennete Maxi
		// w_ancmax := paf_LecfnomM(99, 'ANC-MAX', 1,w_aamm,w_nlot,wsd_fcal1.nbul);
		// IF w_ancmax IS NULL THEN
		// err_msg := PA_PAIE.erreurp('ERR-90019',w_clang);
		// RETURN FALSE;
		// END IF;
		// tamo = utilNomenclature.getAmountOrRateFromNomenclature(dossier, 99, "ANC-MAX", 1, nlot, numeroBulletin, moisPaieCourant, periodOfPay,
		// ClsEnumeration.EnColumnToRead.AMOUNT);
		tamo = (tempNumber = utilNomenclature.getAmountOrRateFromT99(listOfTable99Map, dossier, 99, "ANC-MAX", 1, nlot, moisPaieCourant, periodOfPay, ClsEnumeration.EnColumnToRead.AMOUNT)) == null ? 0 : tempNumber
				.doubleValue();
		if (tamo <= 0)
		{
			error = errorMessage("ERR-90019", langue);

			return false;
		}
		ancienneteMaxi = new Double(tamo).intValue();
		
		tamo = (tempNumber = utilNomenclature.getAmountOrRateFromT99(listOfTable99Map, dossier, 99, "ANC-MAX", 2, nlot, moisPaieCourant, periodOfPay, ClsEnumeration.EnColumnToRead.AMOUNT)) == null ? 0 : tempNumber
				.doubleValue();
		if (tamo <= 0)
		{
			ancienneteMin = 0;
		}
		else
			ancienneteMin = new Double(tamo).intValue();
		
		//
		// -- Calcul proratas standard ?
		// wfnom.lib3 := paf_LecfnomL(99, 'PRORATAS', 1,w_aamm,w_nlot,wsd_fcal1.nbul);
		// IF wfnom.lib3 IS NULL THEN
		// wprorat_std := TRUE;
		// ELSE
		// IF SUBSTR(wfnom.lib3,1,1) = 'O' THEN
		// wprorat_std := TRUE;
		// ELSIF SUBSTR(wfnom.lib3,1,1) = 'N' THEN
		// wprorat_std := FALSE;
		// ELSE
		// wprorat_std := TRUE;
		// END IF;
		// END IF;
		// Calcul proratas standard ?
		// libelle = utilNomenclature.getLabelFromNomenclature(dossier, 99, "PRORATAS", 1, nlot, numeroBulletin, moisPaieCourant, periodOfPay);
		libelle = utilNomenclature.getLabelFromT99(listOfTable99Map, dossier, 99, "PRORATAS", 1, nlot, moisPaieCourant, periodOfPay);
		if (ClsObjectUtil.isNull(libelle)) // || !("O".equals(libelle) || "N".equals(libelle)))
			prorataStandard = true;
		else
		{
			libelle = StringUtils.substring(libelle, 0, 1);
			if ("O".equals(libelle))
				prorataStandard = true;
			else if ("N".equals(libelle))
				prorataStandard = false;
			else
				prorataStandard = true;
		}
		//
		// -- Age maxi des enfants donnant droit a des jours supp.
		// age_max_enfant := NVL( paf_LecfnomM(99, 'ENFANT', 1,w_aamm,w_nlot,wsd_fcal1.nbul), 0);
		// IF age_max_enfant = 0 THEN
		// comptage_enfant := FALSE;
		// END IF;
		// IF age_max_enfant < 0 THEN
		// err_msg := PA_PAIE.erreurp('ERR-90020',w_clang);
		// RETURN FALSE;
		// END IF;
		// tamo = utilNomenclature.getAmountOrRateFromNomenclature(dossier, 99, "ENFANT", 1, nlot, numeroBulletin, moisPaieCourant, periodOfPay,
		// ClsEnumeration.EnColumnToRead.AMOUNT);
		tamo = (tempNumber = utilNomenclature.getAmountOrRateFromT99(listOfTable99Map, dossier, 99, "ENFANT", 1, nlot, moisPaieCourant, periodOfPay, ClsEnumeration.EnColumnToRead.AMOUNT)) == null ? 0 : tempNumber
				.doubleValue();
		if (tamo == 0)
			enfantComptage = false;
		enfantAgeMaximum = tamo <= 0 ? 0 : new Double(tamo).intValue();

		//
		// -- Recuperation de la valeur de comparaison pour identifier un expatrie
		// -- val_exp = Lib 5 = valeur si c'est un expatrie
		// -- typ_rec = Mnt 1 : 1=Regime, 2=Type de contrat, 3=Classe salarie
		// val_exp := paf_lecfnomL(99,'EXPAT',5,w_aamm,w_nlot,wsd_fcal1.nbul);
		// typ_rec := paf_lecfnomM(99,'EXPAT',1,w_aamm,w_nlot,wsd_fcal1.nbul);
		//
		// IF val_exp IS NULL THEN
		// err_msg := PA_PAIE.erreurp('ERR-90021',w_clang);
		// RETURN FALSE;
		// END IF;
		// libelle = utilNomenclature.getLabelFromNomenclature(dossier, 99, "EXPAT", 5, nlot, numeroBulletin, moisPaieCourant, periodOfPay);
		libelle = utilNomenclature.getLabelFromT99(listOfTable99Map, dossier, 99, "EXPAT", 5, nlot, moisPaieCourant, periodOfPay);
		if (ClsObjectUtil.isNull(libelle))
		{
			error = errorMessage("ERR-90021", langue);

			return false;
		}
		expatrieValeur = libelle;
		// tamo = utilNomenclature.getAmountOrRateFromNomenclature(dossier, 99, "EXPAT", 1, nlot, numeroBulletin, moisPaieCourant, periodOfPay,
		// ClsEnumeration.EnColumnToRead.AMOUNT);
		tamo = (tempNumber = utilNomenclature.getAmountOrRateFromT99(listOfTable99Map, dossier, 99, "EXPAT", 1, nlot, moisPaieCourant, periodOfPay, ClsEnumeration.EnColumnToRead.AMOUNT)) == null ? 0 : tempNumber
				.doubleValue();
		expatrieTypeContrat = new Double(tamo).intValue();
		//
		// -- Rubrique nombre de jours d'absence
		// w_rubabs := nvl(LTRIM(TO_CHAR(paf_LecfnomM(99, 'ABSMOIS', 1,w_aamm,w_nlot,wsd_fcal1.nbul),'0999')),NULL);
		// tamo = utilNomenclature.getAmountOrRateFromNomenclature(dossier, 99, "ABSMOIS", 1, nlot, numeroBulletin, moisPaieCourant, periodOfPay,
		// ClsEnumeration.EnColumnToRead.AMOUNT);
		tamo = (tempNumber = utilNomenclature.getAmountOrRateFromT99(listOfTable99Map, dossier, 99, "ABSMOIS", 1, nlot, moisPaieCourant, periodOfPay, ClsEnumeration.EnColumnToRead.AMOUNT)) == null ? 0 : tempNumber
				.doubleValue();
		if (tamo >= 0)
			absenceMoisRubrique = ClsStringUtil.formatNumber(tamo, FORMAT_PERIOD_OF_RBQ);


		libelle = utilNomenclature.getLabelFromT99(listOfTable99Map, dossier, 99, "DECC", 2, nlot, moisPaieCourant, periodOfPay);
		priseEnCompteDateFinContrat = StringUtils.equals(libelle, "O");

		//
		// -- LH 181297
		// -- Permet de conditionner le calcul en cas d'absence tout le mois
		// -- Absent tout le mois = jours d'absences + jours de conges
		// -- Si lib2 = 'O' alors pas de calcul
		// char1 := NVL( Substr(paf_LecfnomL(99, 'ABSMOIS', 2,w_aamm,w_nlot,wsd_fcal1.nbul), 1, 1), 'Z');
		//
		// IF NOT (char1 = 'O' OR char1 = 'N')
		// THEN
		// err_msg := PA_PAIE.erreurp('ERR-90022',w_clang);
		// RETURN FALSE;
		// END IF;
		//
		// IF char1 = 'O'
		// THEN
		// PDC_si_ATLM := TRUE;
		// ELSE
		// PDC_si_ATLM := FALSE;
		// END IF;
		//
		// libelle = utilNomenclature.getLabelFromNomenclature(dossier, 99, "ABSMOIS", 2, nlot, numeroBulletin, moisPaieCourant, periodOfPay);
		libelle = utilNomenclature.getLabelFromT99(listOfTable99Map, dossier, 99, "ABSMOIS", 2, nlot, moisPaieCourant, periodOfPay);
		if (!("O".equals(libelle) || "N".equals(libelle)))
		{
			error = errorMessage("ERR-90022", langue);

			return false;
		}
		ABSENCE_MOIS_PDC_SI_ATLM = "O".equals(libelle) ? true : false;

		// [@yannick : Il manque ici les rubriques de gestion des acomptes quizaines et du nap]
		// -- MM 23/06/2004 ajout des acomptes
		// wfnom.mnt1 := NVL(paf_LecfnomM(99, 'RUBACQZ', 1,w_aamm,w_nlot,wsd_fcal1.nbul),0);
		// IF NOT PA_PAIE.NouZ(wfnom.mnt1) THEN
		// w_rpaieacq := LTRIM(TO_CHAR(wfnom.mnt1,'0999'));
		// ELSE
		// w_rpaieacq := '0000';
		// END IF;
		tamo = (tempNumber = utilNomenclature.getAmountOrRateFromT99(listOfTable99Map, dossier, 99, "RUBACQZ", 1, nlot, moisPaieCourant, periodOfPay, ClsEnumeration.EnColumnToRead.AMOUNT)) == null ? 0 : tempNumber
				.doubleValue();
		acompteQuizaineRubrique = ClsStringUtil.formatNumber(tamo, FORMAT_PERIOD_OF_RBQ);

		// wfnom.mnt2 := NVL(paf_LecfnomM(99, 'RUBACQZ', 2,w_aamm,w_nlot,wsd_fcal1.nbul),0);
		// IF NOT PA_PAIE.NouZ(wfnom.mnt2) THEN
		// w_pourcacqz := wfnom.mnt2;
		// ELSE
		// w_pourcacqz := 0;
		// END IF;
		acompteQuizainePourcentage = (tempNumber = utilNomenclature.getAmountOrRateFromT99(listOfTable99Map, dossier, 99, "RUBACQZ", 2, nlot, moisPaieCourant, periodOfPay, ClsEnumeration.EnColumnToRead.AMOUNT)) == null ? 0
				: tempNumber.doubleValue();
		//
		// wfnom.mnt3 := NVL(paf_LecfnomM(99, 'RUBACQZ', 3,w_aamm,w_nlot,wsd_fcal1.nbul),0);
		// IF NOT PA_PAIE.NouZ(wfnom.mnt3) THEN
		// w_rubacqz := LTRIM(TO_CHAR(wfnom.mnt3,'0999'));
		// ELSE
		// w_rubacqz := '0000';
		// END IF;
		tamo = (tempNumber = utilNomenclature.getAmountOrRateFromT99(listOfTable99Map, dossier, 99, "RUBACQZ", 3, nlot, moisPaieCourant, periodOfPay, ClsEnumeration.EnColumnToRead.AMOUNT)) == null ? 0 : tempNumber
				.doubleValue();
		acompteQuizaineRubriqueMontant = ClsStringUtil.formatNumber(tamo, FORMAT_PERIOD_OF_RBQ);
		//
		//
		// wfnom.mnt1 := NVL(paf_LecfnomM(99, 'RUBNAP', 1,w_aamm,w_nlot,wsd_fcal1.nbul),0);
		// IF NOT PA_PAIE.NouZ(wfnom.mnt1) THEN
		// rub_nap := LTRIM(TO_CHAR(wfnom.mnt1,'0999'));
		// ELSE
		// err_msg := PA_PAIE.erreurp('INF-10116',w_clang);
		// RETURN FALSE;
		// END IF;
		tamo = (tempNumber = utilNomenclature.getAmountOrRateFromT99(listOfTable99Map, dossier, 99, "RUBNAP", 1, nlot, moisPaieCourant, periodOfPay, ClsEnumeration.EnColumnToRead.AMOUNT)) == null ? 0 : tempNumber
				.doubleValue();
		napRubrique = ClsStringUtil.formatNumber(tamo, FORMAT_PERIOD_OF_RBQ);
		if (tamo <= 0)
		{
			error = errorMessage("INF-10116", langue);

			return false;
		}

		// -- Rubrique nombre de jours total non paye
		// w_rubjnp := nvl(LTRIM(TO_CHAR(paf_LecfnomM(99, 'JOURSNP', 1,w_aamm,w_nlot,wsd_fcal1.nbul),'0999')),NULL);
		// tamo = utilNomenclature.getAmountOrRateFromNomenclature(dossier, 99, "JOURSNP", 1, nlot, numeroBulletin, moisPaieCourant, periodOfPay,
		// ClsEnumeration.EnColumnToRead.AMOUNT);
		tamo = (tempNumber = utilNomenclature.getAmountOrRateFromT99(listOfTable99Map, dossier, 99, "JOURSNP", 1, nlot, moisPaieCourant, periodOfPay, ClsEnumeration.EnColumnToRead.AMOUNT)) == null ? 0 : tempNumber
				.doubleValue();
		jourNonPayeRubrique = ClsStringUtil.formatNumber(tamo, FORMAT_PERIOD_OF_RBQ);
		//
		// -- Paie au net : recup :
		// -- Numero de la rubrique du net
		// -- Ecart maximum
		// -- Rubrique d'affectation de l'ecart
		// -- Nombre maximum d'iteration
		//
		// wfnom.mnt1 := NVL(paf_LecfnomM(99, 'RUBNET', 1,w_aamm,w_nlot,wsd_fcal1.nbul),0);
		// wfnom.mnt2 := NVL(paf_LecfnomM(99, 'RUBNET', 2,w_aamm,w_nlot,wsd_fcal1.nbul),0);
		// wfnom.mnt3 := NVL(paf_LecfnomM(99, 'RUBNET', 3,w_aamm,w_nlot,wsd_fcal1.nbul),0);
		// wfnom.mnt4 := NVL(paf_LecfnomM(99, 'RUBNET', 4,w_aamm,w_nlot,wsd_fcal1.nbul),0);
		//
		// IF NOT PA_PAIE.NouZ(wfnom.mnt1) THEN
		// w_rubnet := LTRIM(TO_CHAR(wfnom.mnt1,'0999'));
		// ELSE
		// w_rubnet := '0000';
		// END IF;
		//
		// IF NOT PA_PAIE.NouZ(wfnom.mnt2) THEN
		// w_eca := wfnom.mnt2;
		// ELSE
		// w_eca := 0;
		// END IF;
		//
		// IF NOT PA_PAIE.NouZ(wfnom.mnt3) THEN
		// w_rubaff := LTRIM(TO_CHAR(wfnom.mnt3,'0999'));
		// ELSE
		// w_rubaff := '0000';
		// END IF;
		//
		// IF NOT PA_PAIE.NouZ(wfnom.mnt4) THEN
		// maxiter := wfnom.mnt4;
		// ELSE
		// maxiter := 99;
		// END IF;
		// tamo = utilNomenclature.getAmountOrRateFromNomenclature(dossier, 99, "RUBNET", 1, nlot, numeroBulletin, moisPaieCourant, periodOfPay,
		// ClsEnumeration.EnColumnToRead.AMOUNT);
		// tamo1 = utilNomenclature.getAmountOrRateFromNomenclature(dossier, 99, "RUBNET", 2, nlot, numeroBulletin, moisPaieCourant, periodOfPay,
		// ClsEnumeration.EnColumnToRead.AMOUNT);
		// tamo2 = utilNomenclature.getAmountOrRateFromNomenclature(dossier, 99, "RUBNET", 3, nlot, numeroBulletin, moisPaieCourant, periodOfPay,
		// ClsEnumeration.EnColumnToRead.AMOUNT);
		// double tamo3 = utilNomenclature.getAmountOrRateFromNomenclature(dossier, 99, "RUBNET", 4, nlot, numeroBulletin, moisPaieCourant, periodOfPay,
		// ClsEnumeration.EnColumnToRead.AMOUNT);

		tamo = (tempNumber = utilNomenclature.getAmountOrRateFromT99(listOfTable99Map, dossier, 99, "RUBNET", 1, nlot, moisPaieCourant, periodOfPay, ClsEnumeration.EnColumnToRead.AMOUNT)) == null ? 0 : tempNumber
				.doubleValue();
		tamo1 = (tempNumber = utilNomenclature.getAmountOrRateFromT99(listOfTable99Map, dossier, 99, "RUBNET", 2, nlot, moisPaieCourant, periodOfPay, ClsEnumeration.EnColumnToRead.AMOUNT)) == null ? 0 : tempNumber
				.doubleValue();
		tamo2 = (tempNumber = utilNomenclature.getAmountOrRateFromT99(listOfTable99Map, dossier, 99, "RUBNET", 3, nlot, moisPaieCourant, periodOfPay, ClsEnumeration.EnColumnToRead.AMOUNT)) == null ? 0 : tempNumber
				.doubleValue();
		double tamo3 = (tempNumber = utilNomenclature.getAmountOrRateFromT99(listOfTable99Map, dossier, 99, "RUBNET", 4, nlot, moisPaieCourant, periodOfPay, ClsEnumeration.EnColumnToRead.AMOUNT)) == null ? 0
				: tempNumber.doubleValue();
		//
		paieAuNetRubrique = tamo <= 0 ? FORMAT_PERIOD_OF_RBQ : ClsStringUtil.formatNumber(tamo, FORMAT_PERIOD_OF_RBQ);
		paieAuNetEcartMaximum = tamo1 <= 0 ? 0 : tamo1;
		paieAuNetAffectationEcartRubrique = tamo2 <= 0 ? FORMAT_PERIOD_OF_RBQ : ClsStringUtil.formatNumber(tamo2, FORMAT_PERIOD_OF_RBQ);
		paieAuNetMaxIteration = tamo3 <= 0 ? 0 : new Double(tamo3).intValue();
		//
		// -- Rubrique du brut
		// w_rubbrut := nvl(LTRIM(TO_CHAR(paf_LecfnomM(99, 'RUBBRUT', 1,w_aamm,w_nlot,wsd_fcal1.nbul),'0999')),NULL);
		// IF w_rubbrut IS NULL THEN
		// err_msg := PA_PAIE.erreurp('ERR-90023',w_clang);
		// RETURN FALSE;
		// END IF;
		// IF NOT paf_RbqExiste(w_rubbrut ) THEN
		// err_msg := PA_PAIE.erreurp('ERR-90024',w_clang)||w_rubbrut;
		// RETURN FALSE;
		// END IF;
		// tamo = utilNomenclature.getAmountOrRateFromNomenclature(dossier, 99, "RUBBRUT", 1, nlot, numeroBulletin, moisPaieCourant, periodOfPay,
		// ClsEnumeration.EnColumnToRead.AMOUNT);
		tamo = (tempNumber = utilNomenclature.getAmountOrRateFromT99(listOfTable99Map, dossier, 99, "RUBBRUT", 1, nlot, moisPaieCourant, periodOfPay, ClsEnumeration.EnColumnToRead.AMOUNT)) == null ? 0 : tempNumber
				.doubleValue();
		if (tamo <= 0)
		{
			error = errorMessage("ERR-90023", langue);

			return false;
		}
		brutRubrique = ClsStringUtil.formatNumber(tamo, FORMAT_PERIOD_OF_RBQ);
		if (!rubriqueExiste(brutRubrique))
		{
			error = errorMessage("ERR-90024", langue);

			return false;
		}
		//
		// -- Base nombre de jours = 30 ?
		// w_bas30 := paf_LecfnomL(99, 'BASE30', 1,w_aamm,w_nlot,wsd_fcal1.nbul);
		// w_bnbj := NVL(paf_LecfnomM( 99, 'BASE30', 1,w_aamm,w_nlot,wsd_fcal1.nbul),0);
		// IF w_bas30 IS NULL THEN
		// err_msg := PA_PAIE.erreurp('ERR-90025',w_clang);
		// RETURN FALSE;
		// END IF;
		//
		// IF w_bas30 NOT IN ('O', 'N')
		// THEN
		// err_msg := PA_PAIE.erreurp('ERR-90026',w_clang);
		// RETURN FALSE;
		// END IF;
		// IF w_bas30 = 'O' AND w_bnbj = 0
		// THEN
		// err_msg := PA_PAIE.erreurp('ERR-90027',w_clang);
		// RETURN FALSE;
		// END IF;
		// libelle = utilNomenclature.getLabelFromNomenclature(dossier, 99, "BASE30", 1, nlot, numeroBulletin, moisPaieCourant, periodOfPay);
		libelle = utilNomenclature.getLabelFromT99(listOfTable99Map, dossier, 99, "BASE30", 1, nlot, moisPaieCourant, periodOfPay);
		// tamo = utilNomenclature.getAmountOrRateFromNomenclature(dossier, 99, "BASE30", 1, nlot, numeroBulletin, moisPaieCourant, periodOfPay,
		// ClsEnumeration.EnColumnToRead.AMOUNT);
		tamo = (tempNumber = utilNomenclature.getAmountOrRateFromT99(listOfTable99Map, dossier, 99, "BASE30", 1, nlot, moisPaieCourant, periodOfPay, ClsEnumeration.EnColumnToRead.AMOUNT)) == null ? 0 : tempNumber
				.doubleValue();
		if (ClsObjectUtil.isNull(libelle))
		{
			error = errorMessage("ERR-90025", langue);
			
			return false;
		}
		if (!("O".equals(libelle) || "N".equals(libelle)))
		{
			error = errorMessage("ERR-90026", langue);

			return false;
		}
		if ("O".equals(libelle) && tamo == 0)
		{
			error = errorMessage("ERR-90027", langue);

			return false;
		}
		base30Rubrique = libelle;
		base30NombreJour = new Double(tamo).intValue();
		//
		// -- Code motif absence generee a partir d'un conges pendant la cloture
		// wfnom.lib4 := paf_LecfnomL(99, 'BASE-CONGE', 4,w_aamm,w_nlot,wsd_fcal1.nbul);
		// IF wfnom.lib4 IS NULL THEN
		// RETURN FALSE;
		// END IF;
		//
		// IF NOT PA_PAIE.NouB(wfnom.lib4) THEN
		// w_cg_abs := SUBSTR(wfnom.lib4,1,2);
		// ELSE
		// w_cg_abs := NULL;
		// END IF;
		// libelle = utilNomenclature.getLabelFromNomenclature(dossier, 99, "BASE-CONGE", 4, nlot, numeroBulletin, moisPaieCourant, periodOfPay);
		libelle = utilNomenclature.getLabelFromT99(listOfTable99Map, dossier, 99, "BASE-CONGE", 4, nlot, moisPaieCourant, periodOfPay);
		if (ClsObjectUtil.isNull(libelle))
		{
			// if('O' == this.getGenfile()) return false;
		}
		baseCongeCodeMotif = libelle;
		// baseCongeAbsence = libelle.substring(0, 2);
		baseCongeAbsence = libelle;
		//
		// -- Rubrique nombre de jours supplementaires
		// rub_nbjs := nvl(LTRIM(TO_CHAR(paf_LecfnomM(99, 'NBJS', 1,w_aamm,w_nlot,wsd_fcal1.nbul),'0999')),NULL);
		// w_divnjs := NVL(paf_LecfnomM(99, 'NBJS', 2,w_aamm,w_nlot,wsd_fcal1.nbul),0);
		// tamo = utilNomenclature.getAmountOrRateFromNomenclature(dossier, 99, "NBJS", 1, nlot, numeroBulletin, moisPaieCourant, periodOfPay,
		// ClsEnumeration.EnColumnToRead.AMOUNT);
		// tamo1 = utilNomenclature.getAmountOrRateFromNomenclature(dossier, 99, "NBJS", 2, nlot, numeroBulletin, moisPaieCourant, periodOfPay,
		// ClsEnumeration.EnColumnToRead.AMOUNT);
		//
		tamo = (tempNumber = utilNomenclature.getAmountOrRateFromT99(listOfTable99Map, dossier, 99, "NBJS", 1, nlot, moisPaieCourant, periodOfPay, ClsEnumeration.EnColumnToRead.AMOUNT)) == null ? 0 : tempNumber
				.doubleValue();
		tamo1 = (tempNumber = utilNomenclature.getAmountOrRateFromT99(listOfTable99Map, dossier, 99, "NBJS", 2, nlot, moisPaieCourant, periodOfPay, ClsEnumeration.EnColumnToRead.AMOUNT)) == null ? 0 : tempNumber
				.doubleValue();
		jourSupplRubrique = ClsStringUtil.formatNumber(tamo, FORMAT_PERIOD_OF_RBQ);
		jourSupplDiviseur = tamo1;
		//
		// -- Rubrique nombre de jours payes non pris
		// rub_pnp := LTRIM(TO_CHAR(paf_LecfnomM(99, 'NBJPNP', 1,w_aamm,w_nlot,wsd_fcal1.nbul),'0999'));
		// IF rub_pnp IS NULL THEN
		// err_msg := PA_PAIE.erreurp('ERR-90028',w_clang);
		// RETURN FALSE;
		// END IF;
		//
		// IF NOT Paf_RbqExiste(rub_pnp )
		// THEN
		// err_msg := PA_PAIE.erreurp('ERR-90029',w_clang);
		// RETURN FALSE;
		// END IF;
		// tamo = utilNomenclature.getAmountOrRateFromNomenclature(dossier, 99, "NBJPNP", 1, nlot, numeroBulletin, moisPaieCourant, periodOfPay,
		// ClsEnumeration.EnColumnToRead.AMOUNT);
		tamo = (tempNumber = utilNomenclature.getAmountOrRateFromT99(listOfTable99Map, dossier, 99, "NBJPNP", 1, nlot, moisPaieCourant, periodOfPay, ClsEnumeration.EnColumnToRead.AMOUNT)) == null ? 0 : tempNumber
				.doubleValue();
		if (tamo <= 0)
		{
			error = errorMessage("ERR-90028", langue);

			return false;
		}
		jourPayeNonPrisRubrique = ClsStringUtil.formatNumber(tamo, FORMAT_PERIOD_OF_RBQ);
		if (!rubriqueExiste(jourPayeNonPrisRubrique))
		{
			error = errorMessage("ERR-90029", langue);

			return false;
		}
		//
		// -- Rubrique nombre de jours travailles
		// dec4 := paf_lecfnomM(99,'RUBNBJTR',1,w_aamm,w_nlot,wsd_fcal1.nbul);
		// IF dec4 IS NULL THEN
		// err_msg := PA_PAIE.erreurp('ERR-90030',w_clang);
		// RETURN FALSE;
		// ELSE
		// rub_nbjt := LTRIM(TO_CHAR(dec4,'0999'));
		// IF NOT Paf_RbqExiste(rub_nbjt )
		// THEN
		// err_msg := PA_PAIE.erreurp('ERR-90031',w_clang);
		// RETURN FALSE;
		// END IF;
		// END IF;
		// nbj_pl := paf_lecfnomL(99,'RUBNBJTR',1,w_aamm,w_nlot,wsd_fcal1.nbul);
		// IF NOT ( nbj_pl = 'B' OR nbj_pl = 'T' OR nbj_pl = 'M')
		// THEN
		// err_msg := PA_PAIE.erreurp('ERR-90032',w_clang);
		// RETURN FALSE;
		// END IF;
		// tamo = utilNomenclature.getAmountOrRateFromNomenclature(dossier, 99, "RUBNBJTR", 1, nlot, numeroBulletin, moisPaieCourant, periodOfPay,
		// ClsEnumeration.EnColumnToRead.AMOUNT);
		tamo = (tempNumber = utilNomenclature.getAmountOrRateFromT99(listOfTable99Map, dossier, 99, "RUBNBJTR", 1, nlot, moisPaieCourant, periodOfPay, ClsEnumeration.EnColumnToRead.AMOUNT)) == null ? 0 : tempNumber
				.doubleValue();
		if (tamo <= 0)
		{
			error = errorMessage("ERR-90030", langue);

			return false;
		}
		jourTravailleRubrique = ClsStringUtil.formatNumber(tamo, FORMAT_PERIOD_OF_RBQ);
		if (!rubriqueExiste(jourTravailleRubrique))
		{
			error = errorMessage("ERR-90031", langue);

			return false;
		}
		// libelle = utilNomenclature.getLabelFromNomenclature(dossier, 99, "RUBNBJTR", 1, nlot, numeroBulletin, moisPaieCourant, periodOfPay);
		libelle = utilNomenclature.getLabelFromT99(listOfTable99Map, dossier, 99, "RUBNBJTR", 1, nlot, moisPaieCourant, periodOfPay);
		if (ClsObjectUtil.isNull(libelle) || !("B".equals(libelle) || "T".equals(libelle) || "M".equals(libelle)))
		{
			error = errorMessage("ERR-90032", langue);

			return false;
		}
		jourTravaillePlage = libelle;
		//
		// -- Rubrique nombre de jours travailles depuis debut ex.
		// dec4 := paf_lecfnomM(99,'RUBNBJTR',2,w_aamm,w_nlot,wsd_fcal1.nbul);
		// IF PA_PAIE.NouZ( dec4 ) THEN
		// IF reg_cas = 'J'
		// THEN
		// err_msg := PA_PAIE.erreurp('ERR-90033',w_clang);
		// RETURN FALSE;
		// ELSE
		// rub_nbjt_totA := '0000';
		// END IF;
		// ELSE
		// rub_nbjt_totA := LTRIM(TO_CHAR(dec4,'0999'));
		// IF NOT Paf_RbqExiste( rub_nbjt_totA )
		// THEN
		// err_msg := PA_PAIE.erreurp('ERR-90034',w_clang);
		// RETURN FALSE;
		// END IF;
		// END IF;
		// tamo = utilNomenclature.getAmountOrRateFromNomenclature(dossier, 99, "RUBNBJTR", 2, nlot, numeroBulletin, moisPaieCourant, periodOfPay,
		// ClsEnumeration.EnColumnToRead.AMOUNT);
		tamo = (tempNumber = utilNomenclature.getAmountOrRateFromT99(listOfTable99Map, dossier, 99, "RUBNBJTR", 2, nlot, moisPaieCourant, periodOfPay, ClsEnumeration.EnColumnToRead.AMOUNT)) == null ? 0 : tempNumber
				.doubleValue();
		if (tamo <= 0)
		{
			if ("J".equals(regularisationCas))
			{
				error = errorMessage("ERR-90033", langue);

				return false;
			}
			else
				jourWorkDepuisExerciceRubrique = FORMAT_PERIOD_OF_RBQ;
		}
		else
		{
			jourWorkDepuisExerciceRubrique = ClsStringUtil.formatNumber(tamo, FORMAT_PERIOD_OF_RBQ);
			if (!rubriqueExiste(jourWorkDepuisExerciceRubrique))
			{
				error = errorMessage("ERR-90034", langue);

				return false;
			}
		}
		//
		// -- LH 090698
		// -- Rubrique nombre de jours travailles depuis debut semestre
		// dec4 := paf_lecfnomM(99,'RUBNBJTR',3,w_aamm,w_nlot,wsd_fcal1.nbul);
		// IF PA_PAIE.NouZ( dec4 ) THEN
		// IF reg_cas = 'J'
		// THEN
		// err_msg := PA_PAIE.erreurp('ERR-90035',w_clang);
		// RETURN FALSE;
		// ELSE
		// rub_nbjt_totS := '0000';
		// END IF;
		// ELSE
		// rub_nbjt_totS := LTRIM(TO_CHAR(dec4,'0999'));
		// IF NOT Paf_RbqExiste(rub_nbjt_totS )
		// THEN
		// err_msg := PA_PAIE.erreurp('ERR-90036',w_clang);
		// RETURN FALSE;
		// END IF;
		// END IF;
		// tamo = utilNomenclature.getAmountOrRateFromNomenclature(dossier, 99, "RUBNBJTR", 3, nlot, numeroBulletin, moisPaieCourant, periodOfPay,
		// ClsEnumeration.EnColumnToRead.AMOUNT);
		tamo = (tempNumber = utilNomenclature.getAmountOrRateFromT99(listOfTable99Map, dossier, 99, "RUBNBJTR", 3, nlot, moisPaieCourant, periodOfPay, ClsEnumeration.EnColumnToRead.AMOUNT)) == null ? 0 : tempNumber
				.doubleValue();
		if (tamo <= 0)
		{
			if ("J".equals(regularisationCas))
			{
				error = errorMessage("ERR-90035", langue);

				return false;
			}
			else
				jourWorkDepuisSemestreRubrique = FORMAT_PERIOD_OF_RBQ;
		}
		else
		{
			jourWorkDepuisSemestreRubrique = ClsStringUtil.formatNumber(tamo, FORMAT_PERIOD_OF_RBQ);
			if (!rubriqueExiste(jourWorkDepuisSemestreRubrique))
			{
				error = errorMessage("ERR-90036", langue);

				return false;
			}
		}
		//
		// -- LH 090698
		// -- Rubrique nombre de jours travailles depuis debut trimestre
		// dec4 := paf_lecfnomM(99,'RUBNBJTR',4,w_aamm,w_nlot,wsd_fcal1.nbul);
		// IF PA_PAIE.NouZ( dec4 ) THEN
		// IF reg_cas = 'J'
		// THEN
		// err_msg := PA_PAIE.erreurp('ERR-90037',w_clang);
		// RETURN FALSE;
		// ELSE
		// rub_nbjt_totT := '0000';
		// END IF;
		// ELSE
		// rub_nbjt_totT := LTRIM(TO_CHAR(dec4,'0999'));
		// IF NOT Paf_RbqExiste( rub_nbjt_totT )
		// THEN
		// err_msg := PA_PAIE.erreurp('ERR-90038',w_clang);
		// RETURN FALSE;
		// END IF;
		// END IF;
		// tamo = utilNomenclature.getAmountOrRateFromNomenclature(dossier, 99, "RUBNBJTR", 4, nlot, numeroBulletin, moisPaieCourant, periodOfPay,
		// ClsEnumeration.EnColumnToRead.AMOUNT);
		tamo = (tempNumber = utilNomenclature.getAmountOrRateFromT99(listOfTable99Map, dossier, 99, "RUBNBJTR", 4, nlot, moisPaieCourant, periodOfPay, ClsEnumeration.EnColumnToRead.AMOUNT)) == null ? 0 : tempNumber
				.doubleValue();
		if (tamo <= 0)
		{
			if ("J".equals(regularisationCas))
			{
				error = errorMessage("ERR-90037", langue);

				return false;
			}
			else
				jourWorkDepuisTrimestreRubrique = FORMAT_PERIOD_OF_RBQ;
		}
		else
		{
			jourWorkDepuisTrimestreRubrique = ClsStringUtil.formatNumber(tamo, FORMAT_PERIOD_OF_RBQ);
			if (!rubriqueExiste(jourWorkDepuisTrimestreRubrique))
			{
				error = errorMessage("ERR-90038", langue);

				return false;
			}
		}
		//
		// IF NOT Charg_TauxAbs
		// THEN
		// err_msg := PA_PAIE.erreurp('ERR-90039',w_clang);
		// RETURN FALSE;
		// END IF;
		chargementTauxAbsence();
		
		chargementDevises();
		//
		// IF NOT Charg_TmpNet
		// THEN
		// err_msg := PA_PAIE.erreurp('ERR-90040',w_clang);
		// RETURN FALSE;
		// END IF;
		chargementTableValeurAjustement();
		//
		// IF NOT Charg_Tabrubreg
		// THEN
		// err_msg := PA_PAIE.erreurp('ERR-90041',w_clang);
		// RETURN FALSE;
		// END IF;
		chargementListeRubriqueACalculer();
		//
		chargerMoisDonnees();
		// w_numcal := 0;
		numeroAjustementActuel = 0;
		
		// ---------------------------------------------------------------------
		// -- Chargement de la table des banques utilisees
		// ---------------------------------------------------------------------
		// -- LH 161098
		// IF NOT Chg_TB10
		// THEN
		// RETURN FALSE;
		// END IF;
		chargementListeBanques();
		//
		// charge les bases une fois
		buildRubriqueOfBaseMap();
		// charge les zones libres
		buildRubriqueOfZoneLibreMap();
		// charge les arrondis
		buildArrondiMap();
		// charge les rubriques de session
		buildRubriqueOfSessionMap(); // cette table est mise � jour tout au long des traitements avant mm
		// son utilisation, le mieux c'est de le recharger au moment aupportin
		// charge les rubriques de bar�mes
		buildListOfRubriquebaremeMap();
		//
		// listOfTable99Map.clear();
		// for (String key : listOfTable99Map.keySet()) {
		// if(! Character.isDigit(key.toCharArray()[0])){
		// listOfTable99Map.remove(key);
		// }
		// }
		// -- Mode test 10. voir fichier mode_test10.sql
		// RETURN TRUE;

		// @yannick : ici on charge les p�riode de paie de mani�re � avoir la date de d�but et de fin de p�riode

		chargerPeriodeDePaie();

		//rubATracer = ClsConfigurationParameters.getConfigParameterValue(service, dossier, langue, ClsConfigurationParameters.RUBRIQUE_A_TRACER);

		libelle = utilNomenclature.getLabelFromT99(listOfTable99Map, dossier, 99, "FIRSTDAYEF", 1, nlot, moisPaieCourant, periodOfPay);
		this.useFirstDayForEltFix = StringUtils.isBlank(libelle)?"O":libelle;
		
		
		//formulelitterale = ClsRubriqueVO.formuleLitteraleRubrique(service, dossier);
		
		//Param�tre pour pouvoir ecrire un module externe de calcul de la paie d'un agent
		libelle = utilNomenclature.getLabelFromNomenclature(dossier, 266, "CALC_EXT", 2, nlot, numeroBulletin, moisPaieCourant, periodOfPay);
		utilisationmoduleexterne = StringUtils.equalsIgnoreCase("O", libelle);
		
		libelle = utilNomenclature.getLabelFromT99(listOfTable99Map, dossier, 99, "CALCULMPC", 2, nlot, moisPaieCourant, periodOfPay);
		if (ClsObjectUtil.isNull(libelle))
			calculPaieMoisSuivantCalculConge = false;
		else
			calculPaieMoisSuivantCalculConge = StringUtils.equalsIgnoreCase(libelle,"O");
		
		return true;
		//
		// END Init;
	}

	/**
	 * => charg_per_paie Chargement des periodes de paie
	 */
	public void chargerPeriodeDePaie()
	{
		String libelle1 = utilNomenclature.getLabelFromNomenclature(dossier, 91, periodOfPay, 1, nlot, numeroBulletin, moisPaieCourant, periodOfPay);
		if (StringUtils.isBlank(libelle1))
		{
			calculatePremierEtDernierJourMoisPaie(periodOfPay);
			return;
		}

		String libelle2 = utilNomenclature.getLabelFromNomenclature(dossier, 91, periodOfPay, 2, nlot, numeroBulletin, moisPaieCourant, periodOfPay);

		if (StringUtils.isBlank(libelle2))
		{
			calculatePremierEtDernierJourMoisPaie(periodOfPay);
			return;
		}

		String libelle3 = utilNomenclature.getLabelFromNomenclature(dossier, 91, periodOfPay, 3, nlot, numeroBulletin, moisPaieCourant, periodOfPay);

		if (StringUtils.isBlank(libelle3))
		{
			calculatePremierEtDernierJourMoisPaie(periodOfPay);
			return;
		}

		nombreHeure = new Double(
				(tempNumber = utilNomenclature.getAmountOrRateFromNomenclature(this.listOfTableXXMap,dossier, 91, periodOfPay, 1, nlot, numeroBulletin, moisPaieCourant, periodOfPay, ClsEnumeration.EnColumnToRead.RATES)) == null ? 0
						: tempNumber.doubleValue()).doubleValue();

		if (nombreHeure <= 0)
		{
			calculatePremierEtDernierJourMoisPaie(periodOfPay);
			return;
		}
		String[] datePatterns = new String[] { "dd-MM-yyyy", "dd/MM/yyyy", "yyyy-MM-dd", "yyyy/MM/dd", "ddMMyyyy" };
		try
		{
			firstDayOfMonth = DateUtils.parseDate(libelle2, datePatterns);// new ClsDate(libelle2, "dd/MM/yyyy").getDate();
			lastDayOfMonth = DateUtils.parseDate(libelle3, datePatterns);// new ClsDate(libelle3, "dd/MM/yyyy").getDate();
			//			
			firstDayOfMonthS = libelle2;
			lastDayOfMonthS = libelle3;	
			//
			table91LabelNotEmpty = true;
		}
		catch (ParseException e)
		{
			e.printStackTrace();
			calculatePremierEtDernierJourMoisPaie(periodOfPay);
			
		}
	}
	
	public void chargerPeriodeDePaie(String cods)
	{
		Session session = service.getSession();
		
		String query = " SELECT count(*)  FROM ParamData WHERE cdos = '" + dossier + "' and ctab=91 and cacc = '" + cods + "' and nume = 1";
		Integer count = Integer.valueOf(session.createSQLQuery(query).list().get(0).toString());
		service.closeSession(session);
		if(count==0)
		{
			calculatePremierEtDernierJourMoisPaie(periodOfPay);
			return;
		}
		
		String libelle1 = utilNomenclature.getLabelFromNomenclature(dossier, 91, periodOfPay, 1, nlot, numeroBulletin, moisPaieCourant, periodOfPay);
		if (StringUtils.isBlank(libelle1))
		{
			calculatePremierEtDernierJourMoisPaie(periodOfPay);
			return;
		}

		String libelle2 = utilNomenclature.getLabelFromNomenclature(dossier, 91, periodOfPay, 2, nlot, numeroBulletin, moisPaieCourant, periodOfPay);

		if (StringUtils.isBlank(libelle2))
		{
			calculatePremierEtDernierJourMoisPaie(periodOfPay);
			return;
		}

		String libelle3 = utilNomenclature.getLabelFromNomenclature(dossier, 91, periodOfPay, 3, nlot, numeroBulletin, moisPaieCourant, periodOfPay);

		if (StringUtils.isBlank(libelle3))
		{
			calculatePremierEtDernierJourMoisPaie(periodOfPay);
			return;
		}

		nombreHeure = new Double(
				(tempNumber = utilNomenclature.getAmountOrRateFromNomenclature(this.listOfTableXXMap,dossier, 91, periodOfPay, 1, nlot, numeroBulletin, moisPaieCourant, periodOfPay, ClsEnumeration.EnColumnToRead.RATES)) == null ? 0
						: tempNumber.intValue()).intValue();

		if (nombreHeure <= 0)
		{
			calculatePremierEtDernierJourMoisPaie(periodOfPay);
			return;
		}
		String[] datePatterns = new String[] { "dd-MM-yyyy", "dd/MM/yyyy", "yyyy-MM-dd", "yyyy/MM/dd", "ddMMyyyy" };
		try
		{
			firstDayOfMonth = DateUtils.parseDate(libelle2, datePatterns);// new ClsDate(libelle2, "dd/MM/yyyy").getDate();
			lastDayOfMonth = DateUtils.parseDate(libelle3, datePatterns);// new ClsDate(libelle3, "dd/MM/yyyy").getDate();
			//			
			firstDayOfMonthS = libelle2;
			lastDayOfMonthS = libelle3;	
			//
			table91LabelNotEmpty = true;
		}
		catch (ParseException e)
		{
			e.printStackTrace();
			calculatePremierEtDernierJourMoisPaie(periodOfPay);
			
		}
	}

	/**
	 * calcule le premier et le dernier jour du mois de paie. par exemple pour Ao�t: le premier jour c'est 01/08 et le dernier jour c'est 31/08
	 * 
	 * @param monthOfPay
	 */
	private void calculatePremierEtDernierJourMoisPaie(String monthOfPay)
	{
		// com.cdi.deltarh.service.ClsParameter.println(">>calculatePremierEtDernierJourMoisPaie");
		ClsDate oClsDate = new ClsDate(monthOfPay, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
		firstDayOfMonth = oClsDate.getFirstDayOfMonth();
		lastDayOfMonth = oClsDate.getLastDayOfMonth();
		
		firstDayOfMonthS = new ClsDate(firstDayOfMonth).getDateS(this.appDateFormat);	
		lastDayOfMonthS = new ClsDate(lastDayOfMonth).getDateS(this.appDateFormat);			
		
		table91LabelNotEmpty = false;
	}
	
	String firstDayOfMonthS = "";
	String lastDayOfMonthS = "";
	String useFirstDayForEltFix = "O";
	double nombreHeure;
	
	private boolean rubriqueExiste(String rubrique)
	{
		// com.cdi.deltarh.service.ClsParameter.println(">>rubriqueExiste");
		List l = service.find("select a from ElementSalaire a where cdos = '" + dossier + "' and crub = '" + rubrique + "'");
		if (l != null && l.size() > 0)
			return true;
		else
			return false;
	}

	public String getPrecNiv3()
	{
		return precNiv3;
	}

	public void setPrecNiv3(String precNiv3)
	{
		this.precNiv3 = precNiv3;
	}

	public int getDossierNbreDecimale()
	{
		return dossierNbreDecimale;
	}

	public void setDossierNbreDecimale(int dossierNbreDecimale)
	{
		this.dossierNbreDecimale = dossierNbreDecimale;
	}

	public ClsParameterOfPay clone()
	{
		// com.cdi.deltarh.service.ClsParameter.println(">>clone");
		// The constructor ClsParameterOfPay(ClsService, ClsNomenclatureUtil, boolean, int, String, int, ClsEnumeration.EnModePaiement) is undefined
		ClsParameterOfPay param = new ClsParameterOfPay(service, utilNomenclature, useRetroactif, numeroBulletin, periodOfPay, clas, sessionId, modePaiement);
		BeanUtils.copyProperties(this, param);
		param.setMyMoisPaieCourant(myMoisPaieCourant.clone());
		param.setMyMonthOfPay(myMonthOfPay.clone());
		param.setListeRubriquesAAjouterAuNet(ObjectUtils.clone(listeRubriquesAAjouterAuNet));
		param.setListeRubriquesAAjuster(ObjectUtils.clone(listeRubriquesAAjuster));
		
		if (1 == 1)
			return param;
		try
		{
			Method[] tabMethodes = this.getClass().getMethods();
			Object obj = null;
			for (Method methodSet : param.getClass().getMethods())
			{
				if (methodSet.getName().startsWith("set") && (!methodSet.getName().equals("setObject")))
				{
					for (Method methodGet : tabMethodes)
					{
						if (methodGet.getName().startsWith("get") && (!methodGet.getName().equals("getObject")) && methodGet.getName().substring(1).equals(methodSet.getName().substring(1)))
						{
							obj = methodGet.invoke(this, (Object[]) null);
							methodSet.invoke(param, (Object[]) new Object[] { obj });
							break;
						}
						// pour les m�thodes qui commencent par is
						else if (methodGet.getName().startsWith("is") && methodGet.getName().substring(2).equals(methodSet.getName().substring(3)))
						{
							obj = methodGet.invoke(this, (Object[]) null);
							methodSet.invoke(param, (Object[]) new Object[] { obj });
							break;
						}
					}
				}
			}
		}
		catch (IllegalAccessException illEx)
		{
			illEx.printStackTrace();
		}
		catch (InvocationTargetException invEx)
		{
			invEx.printStackTrace();
		}
		return param;
	}

	public void writeToLocalLog(String error)
	{
		if ('O' == this.getGenfile())
			outputtext += "\n" + error;
	}

	/**
	 * => erreurp
	 * 
	 * @param errorCode
	 * @param langue
	 * @param param
	 * @return la chaine d'error
	 */
	public String errorMessage(String errorCode, String langue, Object... param)
	{

		// FUNCTION erreurp(w_code_err varchar2,w_code_langue varchar2,
		// w_param1 varchar2 := null,
		// w_param2 varchar2 := null,
		// w_param3 varchar2 := null,
		// w_param4 varchar2 := null,
		// w_param5 varchar2 := null
		// ) RETURN VARCHAR2 IS
		//
		// w_lib_mess varchar2(500);
		// n number := null;
		String libelleMessage = "";
		int n = 0;
		// BEGIN
		// select lbmes into w_lib_mess
		// from evmsg
		// where cdmes = w_code_err
		// and clang = w_code_langue;
		try
		{
			List result = service.find("SELECT a FROM Message WHERE clang='"+errorCode+"' AND cdmes='"+langue+"'");

			Message message = null;
			if(result!= null) message =(Message) result.get(0);
			if (message == null){
				message = new Message();
				message.setCdmes(errorCode);
				message.setClang(langue);
				message.setLbmes(errorCode);
			}

			if (message != null)
			{
				//
				// n := instr(w_lib_mess,'%1');
				libelleMessage = message.getLbmes();
				// if w_param1 is not null and nvl(n,0) > 0 then
				// w_lib_mess := substr(w_lib_mess,1,n-1)||w_param1||substr(w_lib_mess,n+2);
				// end if;
				// n = libelleMessage.indexOf("%1");
				// if(ClsObjectUtil.isNull(param1) && n > 0){
				// libelleMessage = libelleMessage.substring(0, n - 1) + param1 + libelleMessage.substring(0, n + 2);
				// }
				// n := instr(w_lib_mess,'%2');
				// if w_param2 is not null and nvl(n,0) > 0 then
				// w_lib_mess := substr(w_lib_mess,1,n-1)||w_param2||substr(w_lib_mess,n+2);
				// end if;
				// n := instr(w_lib_mess,'%3');
				// if w_param3 is not null and nvl(n,0) > 0 then
				// w_lib_mess := substr(w_lib_mess,1,n-1)||w_param3||substr(w_lib_mess,n+2);
				// end if;
				// n := instr(w_lib_mess,'%4');
				// if w_param4 is not null and nvl(n,0) > 0 then
				// w_lib_mess := substr(w_lib_mess,1,n-1)||w_param4||substr(w_lib_mess,n+2);
				// end if;
				// n := instr(w_lib_mess,'%5');
				// if w_param5 is not null and nvl(n,0) > 0 then
				// w_lib_mess := substr(w_lib_mess,1,n-1)||w_param5||substr(w_lib_mess,n+2);
				// end if;
				//
				// RETURN w_lib_mess;
				String match = "%";
				int j = 0;
				for (int i = 0; i < param.length; i++)
				{
					// n = libelleMessage.indexOf("%" + i);
					j = i + 1;
					match = "%" + j;
					// if(ClsObjectUtil.isNull(param[i]) && n > 0){
					// libelleMessage = libelleMessage.substring(0, n - 1) + param[i].toString() + libelleMessage.substring(0, n + 2);
					// }
					Object params = param[i];
					if (ClsObjectUtil.isNull(params))
						params = " ";
					if (ClsObjectUtil.isNull(match))
						match = " ";
					if (!ClsObjectUtil.isNull(libelleMessage))
						libelleMessage = libelleMessage.replace(match, String.valueOf(params));
				}
				//
			}
		}
		catch (DataAccessException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Insertion yannick

		try
		{
			insererLogMessage(libelleMessage);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		this.writeToLocalLog(libelleMessage);

		return libelleMessage;
	}

	/**
	 * =>pap_logins
	 * 
	 * @param error
	 */
	public synchronized void insererLogMessage(String error)
	{
		LogMessage log = new LogMessage();
		log.setIdEntreprise(Integer.valueOf(getDossier()));
		log.setCuti(uti);
		log.setDatc(new Date());
		log.setLigne(error);
		//
		service.save(log);
		ClsTraiterSalaireThread.NBRE_ERREURS_RENCONTREES++;
		

	}

	/**
	 * chargement des zones libres
	 */
	public void buildRubriqueOfZoneLibreMap()
	{

		Map<String, List<Object>> map = new HashMap<String, List<Object>>();
		String queryStringZli = "from ElementSalaireZonelibre " + " where cdos = '" + this.getDossier() + "'";

		List listOfRubZli = this.getService().find(queryStringZli);
		//

		//
		listOfRubqZoneLibre = map;
	}

	/**
	 * chargement des rubriques de base
	 */
	public void buildRubriqueOfBaseMap()
	{

		Map<String, List<Object[]>> map = new HashMap<String, List<Object[]>>();
		String queryString = "select crub, nume, sign, rubk from ElementSalairebase " + " where cdos = '" + this.getDossier() + "'" +
		// " and crub in ( select crub from ElementSalaire" +
				// " where cdos = '" + this.getDossier() + "'" +
				// " and ( (basc = 'O' and trtc = 'N') or mopa = 'O'))" +
				" order by cdos, crub, nume";

		String queryStringRetro = "select crub, nume, sign, rubk from Rhthrbqba " + " where cdos = '" + this.getDossier() + "'" + " and aamm = '" + this.getMonthOfPay() + "'" + " and nbul = '"
				+ this.getNumeroBulletin() + "'" +
				// " and crub in ( select crub from ElementSalaire" +
				// " where cdos = '" + this.getDossier() + "'" +
				// " and ( (basc = 'O' and trtc = 'N') or mopa = 'O'))" +
				" order by cdos, crub, nume";

		List listOfRubBase = (this.isUseRetroactif()) ? this.getService().find(queryStringRetro) : this.getService().find(queryString);
		//
		Object eltbase[] = null;
		for (Object object : listOfRubBase)
		{
			// map.put(crub, value)
			eltbase = new Object[3];
			eltbase[0] = ((Object[]) object)[1];
			eltbase[1] = ((Object[]) object)[2];
			eltbase[2] = ((Object[]) object)[3];
			if (!map.containsKey((String) ((Object[]) object)[0]))
				map.put((String) ((Object[]) object)[0], new ArrayList<Object[]>());
			//
			map.get((String) ((Object[]) object)[0]).add(eltbase);
		}
		//
		listOfRubqBase = map;
	}

	/**
	 * chargement des arrondis
	 */
	public void buildArrondiMap()
	{

		listOfArrondi = new HashMap<String, Object[]>();
		List l = null;
		List l1 = null;
		if (!useRetroactif)
		{
			l = service.find("from ParamData " + " where cdos = '" + dossier + "'" + " and ctab = 63" + " and nume = 2");
			l1 = service.find("from ParamData " + " where cdos = '" + dossier + "'" + " and ctab = 63" + " and nume = 1");
		}
		else
		{
			l = service.find("from Rhthfnom " + " where cdos = '" + dossier + "'" + " and ctab = 63" + " and nume = 2" + " and nbul = " + numeroBulletin + " and aamm = '"
					+ periodOfPay + "'");
			l1 = service.find("from Rhthfnom " + " where cdos = '" + dossier + "'" + " and ctab = 63" + " and nume = 1" + " and nbul = " + numeroBulletin + " and aamm = '"
					+ periodOfPay + "'");
		}
		//
		ParamData o1 = null;
		Object[] arrond = null;
		for (Object object : l)
		{
			// map.put(crub, value)
			if (object instanceof ParamData)
			{
				o1 = (ParamData) object;
				arrond = new Object[2];
				arrond[0] = o1.getVall();
				arrond[1] = 0;
				listOfArrondi.put(o1.getCacc(), arrond);
			}
		}
		// mise � jour des taux
		arrond = null;
		for (Object object : l1)
		{
			// map.put(crub, value)
			if (object instanceof ParamData)
			{
				o1 = (ParamData) object;
				arrond = listOfArrondi.get(o1.getCacc());
				arrond[1] = o1.getValt();
				listOfArrondi.put(o1.getCacc(), arrond);
			}

		}
	}

	/**
	 * chargement des valeurs T99 pour ne pas aller chercher dans la base lors de l'initialisation des param�tres de calcul
	 */
	public void buildTableT51T52T99Map()
	{

		listOfTable99Map = new HashMap<String, Object[]>();
		List l = null;
		if (!useRetroactif)
		{
			l = service.find("from ParamData " + " where cdos = '" + dossier + "'" + " and ctab in (51, 52, 99)");
		}
		else
		{
			l = service.find("from Rhthfnom " + " where cdos = '" + dossier + "'" + " and ctab in (51, 52, 99)" + " and nbul = " + numeroBulletin + " and aamm = '" + periodOfPay + "'");
		}
		//
		ParamData o1 = null;
		Object[] t99 = null;
		String key = "";
		for (Object object : l)
		{
			t99 = new Object[4];
			// map.put(crub, value)
			if (object instanceof ParamData)
			{
				o1 = (ParamData) object;
				t99[0] = o1.getNume();
				t99[1] = o1.getVall();
				t99[2] = o1.getValm();
				t99[3] = o1.getValt();
				key = o1.getCacc() + o1.getNume();
				//
				listOfTable99Map.put(key.trim(), t99);
			}
		}
	}

	/**
	 * charge les rubriques appartenant � une session
	 */
	public void buildRubriqueOfSessionMap()
	{

	}

	// public void buildRubriqueOfSessionMap()
	// {
	// Map<String, Object> map = new HashMap<String, Object>();
	// String queryString = "select crub, mont from ElementSalaireajus" + " where session_id = " + this.sessionId + " order by mont ";
	// List ListOfRow = service.find(queryString);
	// double mont = 0;
	// Object[] oo = null;
	// try
	// {
	// // on ne va prendre que la plus grande valeur
	// for (Object object : ListOfRow)
	// {
	// // map.put(crub, value)
	// if (map.containsKey((String) ((Object[]) object)[0]))
	// {
	// mont = ((BigDecimal) (((Object[]) object)[1])).doubleValue();
	// oo = (Object[]) map.get((String) ((Object[]) object)[0]);
	// if (mont > ((BigDecimal) ((Object[]) oo)[1]).doubleValue())
	// map.put((String) ((Object[]) object)[0], object);
	// }
	// else
	// {
	// map.put((String) ((Object[]) object)[0], object);
	// }
	// }
	// }
	// catch (Exception e)
	// {
	// e.printStackTrace();
	// }
	// //
	// listOfRubqOfsession = map;
	// }

	/**
	 * Construit la liste des rubriques. Cette liste sera partag�e entre tous les salari�s.
	 * 
	 * @param listOfRubrique
	 *            liste contenant soit les rubriques historis�es ou non
	 */
	public void buildListOfRubrique(List listOfRubrique, ClsSalaryToProcess salary)
	{
		// com.cdi.deltarh.service.ClsParameter.println("<<buildListOfRubrique");
		listOrdonneRubrique = new ArrayList<String>();
		ClsRubriqueClone rubrique = null;
		try
		{
			if (this.useRetroactif)
			{
				for (Object obj : listOfRubrique)
				{
					rubrique = new ClsRubriqueClone(salary);
					rubrique.setRubrique(ClsParubqClone.clone(obj));
					//
					listOrdonneRubrique.add(rubrique.getRubrique().getComp_id().getCrub());
					//
					if (ListOfAllRubriqueMap == null)
						ListOfAllRubriqueMap = new HashMap<String, ClsRubriqueClone>();
					ListOfAllRubriqueMap.put(rubrique.getRubrique().getComp_id().getCrub(), rubrique);
				}
			}
			else
			{
				for (Object obj : listOfRubrique)
				{
					rubrique = new ClsRubriqueClone(salary);
					rubrique.setRubrique(ClsParubqClone.clone(obj));
					//
					listOrdonneRubrique.add(rubrique.getRubrique().getComp_id().getCrub());
					//
					if (ListOfAllRubriqueMap == null)
						ListOfAllRubriqueMap = new HashMap<String, ClsRubriqueClone>();
					ListOfAllRubriqueMap.put(rubrique.getRubrique().getComp_id().getCrub(), rubrique);
				}
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Construit la liste des rubriques des bar�mes. Cette liste sera partag�e entre tous les salari�s. la liste � construire est un param�tre de classe
	 */
	public void buildListOfRubriquebaremeMapEmmanuel()
	{
		// com.cdi.deltarh.service.ClsParameter.println("<<buildListOfRubrique");
		listOfRubriquebaremeMap = new HashMap<String, Object>();
		String queryString = "from ElementSalairebareme " + " where cdos = '" + dossier + "'" + " order by cdos , crub , nume";
		String queryStringRetro = "from Rhthrubbarem " + " where cdos = '" + dossier + "'" + " and aamm = '" + this.getMonthOfPay() + "'" + " and nbul = " + this.getNumeroBulletin()
				+ " order by cdos , crub , nume";
		try
		{
			String keyOfBaremeList = "";
			// construit la liste des bar�mes
			List listOfBarem = this.isUseRetroactif() ? service.find(queryStringRetro) : service.find(queryString);
			ElementSalaireBareme oElementSalairebareme = null;
			String lastrub = "";
			List<Object> listOfRubriquebareme = null;

				for (Object barem : listOfBarem)
				{
					oElementSalairebareme = (ElementSalaireBareme) barem;
					if (!oElementSalairebareme.getCrub().equals(lastrub))
					{
						if (listOfRubriquebareme != null && listOfRubriquebareme.size() > 0)
							listOfRubriquebaremeMap.put(keyOfBaremeList, listOfRubriquebareme);
						lastrub = oElementSalairebareme.getCrub();
						listOfRubriquebareme = new ArrayList<Object>();
					}
					keyOfBaremeList = oElementSalairebareme.getCrub();
					listOfRubriquebareme.add(barem);
				}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void buildListOfRubriquebaremeMap()
	{
		// com.cdi.deltarh.service.ClsParameter.println("<<buildListOfRubrique");
		listOfRubriquebaremeMap = new HashMap<String, Object>();
		String queryString = "from ElementSalairebareme " + " where cdos = '" + dossier + "'" + " order by cdos , crub , nume";
		String queryStringRetro = "from Rhthrubbarem " + " where cdos = '" + dossier + "'" + " and aamm = '" + this.getMonthOfPay() + "'" + " and nbul = " + this.getNumeroBulletin()
				+ " order by cdos , crub , nume";

		List<Object> listOfRubriquebareme = new ArrayList<Object>();

		List<Object> listOfRubriquebaremeRetroactif = new ArrayList<Object>();
		try
		{
			String keyOfBaremeList = "";
			// construit la liste des bar�mes
			List listOfBarem = this.isUseRetroactif() ? service.find(queryStringRetro) : service.find(queryString);

			ElementSalaireBareme oElementSalairebareme = null;

				for (Object barem : listOfBarem)
				{

					oElementSalairebareme = (ElementSalaireBareme) barem;

					keyOfBaremeList = oElementSalairebareme.getCrub();

					if (!listOfRubriquebaremeMap.containsKey(keyOfBaremeList))
						listOfRubriquebaremeMap.put(keyOfBaremeList, new ArrayList<Object>());

					((List<Object>) listOfRubriquebaremeMap.get(keyOfBaremeList)).add(barem);

				}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		// Iterator<String> keys = (Iterator<String>)listOfRubriquebaremeMap.keySet().iterator();
		// while(keys.hasNext())
		// {
		// String key = (String)keys.next();
		// List olist = (List)listOfRubriquebaremeMap.get(key);
		// for (Object object : olist) {
		// com.cdi.deltarh.service.ClsParameter.println("Key= "+ key + "and Rubrique ="+((ElementSalairebareme)object).getComp_id().getCrub()+" nume = "+
		// ((ElementSalairebareme)object).getComp_id().getNume()+ " et Valeur 1 = "+((ElementSalairebareme)object).getVal1()+" et taux =
		// "+((ElementSalairebareme)object).getTaux());
		// }
		// }

	}

	public synchronized Map<String, ClsRubriqueClone> getListOfAllRubriqueMap()
	{
		return ListOfAllRubriqueMap;
	}

	public synchronized void setListOfAllRubriqueMap(Map<String, ClsRubriqueClone> listOfAllRubriqueMap)
	{
		ListOfAllRubriqueMap = listOfAllRubriqueMap;
	}

	// public synchronized Map<String, Object> getListOfRubqOfsession()
	// {
	// return listOfRubqOfsession;
	// }
	//
	// public synchronized void setListOfRubqOfsession(Map<String, Object> listOfRubqOfsession)
	// {
	// this.listOfRubqOfsession = listOfRubqOfsession;
	// }

	public String getAppDateFormat()
	{
		return appDateFormat;
	}

	public void setAppDateFormat(String appDateFormat)
	{
		this.appDateFormat = appDateFormat;
	}

	public int getDebutExerciceAnnee()
	{
		return debutExerciceAnnee;
	}

	public void setDebutExerciceAnnee(int debutExerciceAnnee)
	{
		this.debutExerciceAnnee = debutExerciceAnnee;
	}

	public int getDebutExerciceMois()
	{
		return debutExerciceMois;
	}

	public void setDebutExerciceMois(int debutExerciceMois)
	{
		this.debutExerciceMois = debutExerciceMois;
	}

	public int getDebutExerciceaamm()
	{
		return debutExerciceaamm;
	}

	public void setDebutExerciceaamm(int debutExerciceaamm)
	{
		this.debutExerciceaamm = debutExerciceaamm;
	}

	public int getDebutExerciceRangMoisPaie()
	{
		return debutExerciceRangMoisPaie;
	}

	public void setDebutExerciceRangMoisPaie(int debutExerciceRangMoisPaie)
	{
		this.debutExerciceRangMoisPaie = debutExerciceRangMoisPaie;
	}

	public char getGenfile()
	{
		return genfile;
	}

	public void setGenfile(char genfile)
	{
		this.genfile = genfile;
	}

	public String getGenfilefolder()
	{
		return genfilefolder;
	}

	public void setGenfilefolder(String genfilefolder)
	{
		this.genfilefolder = genfilefolder;
	}

	public int getThreadmax()
	{
		return threadmax;
	}

	public void setThreadmax(int threadmax)
	{
		this.threadmax = threadmax;
	}

	public Date getDtDdex()
	{
		return dtDdex;
	}

	public void setDtDdex(Date dtDdex)
	{
		this.dtDdex = dtDdex;
	}

	public Date getDtDfex()
	{
		return dtDfex;
	}

	public void setDtDfex(Date dtDfex)
	{
		this.dtDfex = dtDfex;
	}

	public CalculPaieServiceImpl getLanceur()
	{
		return lanceur;
	}

	public void setLanceur(CalculPaieServiceImpl lanceur)
	{
		this.lanceur = lanceur;
	}

	public String getRubATracer()
	{
		return rubATracer;
	}

	public void setRubATracer(String rubATracer)
	{
		this.rubATracer = rubATracer;
	}

	public String getNomClient()
	{
		return nomClient;
	}

	public void setNomClient(String nomClient)
	{
		this.nomClient = nomClient;
	}

	public Date getDdmp()
	{
		return ddmp;
	}

	public void setDdmp(Date ddmp)
	{
		this.ddmp = ddmp;
	}

	public String getAcompteQuizaineRubrique()
	{
		return acompteQuizaineRubrique;
	}

	public void setAcompteQuizaineRubrique(String acompteQuizaineRubrique)
	{
		this.acompteQuizaineRubrique = acompteQuizaineRubrique;
	}

	public double getAcompteQuizainePourcentage()
	{
		return acompteQuizainePourcentage;
	}

	public void setAcompteQuizainePourcentage(double acompteQuizainePourcentage)
	{
		this.acompteQuizainePourcentage = acompteQuizainePourcentage;
	}

	public String getAcompteQuizaineRubriqueMontant()
	{
		return acompteQuizaineRubriqueMontant;
	}

	public void setAcompteQuizaineRubriqueMontant(String acompteQuizaineRubriqueMontant)
	{
		this.acompteQuizaineRubriqueMontant = acompteQuizaineRubriqueMontant;
	}

	public String getNapRubrique()
	{
		return napRubrique;
	}

	public void setNapRubrique(String napRubrique)
	{
		this.napRubrique = napRubrique;
	}

	public boolean isPriseEnCompterCongeAnterieur()
	{
		return priseEnCompterCongeAnterieur;
	}

	public void setPriseEnCompterCongeAnterieur(boolean priseEnCompterCongeAnterieur)
	{
		this.priseEnCompterCongeAnterieur = priseEnCompterCongeAnterieur;
	}

	public boolean isPriseEnCompteDateFinContrat()
	{
		return priseEnCompteDateFinContrat;
	}

	public void setPriseEnCompteDateFinContrat(boolean priseEnCompteDateFinContrat)
	{
		this.priseEnCompteDateFinContrat = priseEnCompteDateFinContrat;
	}

	public String getRubriqueAvanceConge()
	{
		return rubriqueAvanceConge;
	}

	public void setRubriqueAvanceConge(String rubriqueAvanceConge)
	{
		this.rubriqueAvanceConge = rubriqueAvanceConge;
	}

	/**
	 * @return the firstDayOfMonthS
	 */
	public String getFirstDayOfMonthS()
	{
		return firstDayOfMonthS;
	}

	/**
	 * @param firstDayOfMonthS the firstDayOfMonthS to set
	 */
	public void setFirstDayOfMonthS(String firstDayOfMonthS)
	{
		this.firstDayOfMonthS = firstDayOfMonthS;
	}

	/**
	 * @return the lastDayOfMonthS
	 */
	public String getLastDayOfMonthS()
	{
		return lastDayOfMonthS;
	}

	/**
	 * @param lastDayOfMonthS the lastDayOfMonthS to set
	 */
	public void setLastDayOfMonthS(String lastDayOfMonthS)
	{
		this.lastDayOfMonthS = lastDayOfMonthS;
	}

	public String getUseFirstDayForEltFix()
	{
		return useFirstDayForEltFix;
	}

	public void setUseFirstDayForEltFix(String useFirstDayForEltFix)
	{
		this.useFirstDayForEltFix = useFirstDayForEltFix;
	}

	public double getNombreHeure()
	{
		return nombreHeure;
	}

	public void setNombreHeure(double nombreHeure)
	{
		this.nombreHeure = nombreHeure;
	}

	public int getAncienneteMin()
	{
		return ancienneteMin;
	}

	public void setAncienneteMin(int ancienneteMin)
	{
		this.ancienneteMin = ancienneteMin;
	}

	public boolean isFormulelitterale()
	{
		return formulelitterale;
	}

	public void setFormulelitterale(boolean formulelitterale)
	{
		this.formulelitterale = formulelitterale;
	}

	

	public boolean isUtilisationmoduleexterne()
	{
		return utilisationmoduleexterne;
	}

	public void setUtilisationmoduleexterne(boolean utilisationmoduleexterne)
	{
		this.utilisationmoduleexterne = utilisationmoduleexterne;
	}

	public Map<String, Double> getDevises()
	{
		return devises;
	}

	public void setDevises(Map<String, Double> devises)
	{
		this.devises = devises;
	}

	public boolean isCalculPaieMoisSuivantCalculConge()
	{
		return calculPaieMoisSuivantCalculConge;
	}

	public void setCalculPaieMoisSuivantCalculConge(boolean calculPaieMoisSuivantCalculConge)
	{
		this.calculPaieMoisSuivantCalculConge = calculPaieMoisSuivantCalculConge;
	}
	
}
