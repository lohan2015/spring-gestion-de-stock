package com.kinart.paie.business.services.calcul;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kinart.paie.business.model.ElementSalaireBareme;
import com.kinart.paie.business.model.LogMessage;
import com.kinart.paie.business.model.Message;
import com.kinart.paie.business.model.ParamData;
import com.kinart.paie.business.services.CongeFictifService;
import com.kinart.paie.business.services.impl.CalculPaieServiceImpl;
import com.kinart.paie.business.services.utils.ClsDate;
import com.kinart.paie.business.services.utils.ClsObjectUtil;
import com.kinart.paie.business.services.utils.ClsStringUtil;
import com.kinart.paie.business.services.utils.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Session;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataAccessException;

/**
 * Cette permet d'initialiser les paramètres de calcul de la paie que toutes les autres classes utiliseront. Ainsi, ce sont des paramètres globaux. Une erreur
 * dans l'un de ces paramètres suspend le calcul jusqu'à ce que tous les paramètres soient renseignés.
 *
 * @author C.mbassi
 */

public class ClsFictifParameterOfPay extends ClsParameterOfPay
{
	public CongeFictifService congeFictifService;

	private int BasePretsNb_ret;

	private String salaireMoyenMensuelRubrique = "";

	private String fictiveRubrique = "";

	private int fictiveNombreDeJour = 0;

	private String baseCongeRubrique = "";

	private String napRubrique = "";

	private String fictiveMonthOfPay = "";

	private String fictiveDossierDateDebutExercice = "";

	private Date fictiveLastDayOfMonth = null;

	private Date fictiveFirstDayOfMonth = null;

	private ClsDate fictiveMyMonthOfPay = null;

	ClsFictifNomenclatureUtil utilNomenclatureFictif;

	private int ancienneteMini = 0;

	private double nombreHeure = 0;

	public String nomClient;

	protected Map<String, ClsFictifRubriqueClone> ListOfAllFictiveRubriqueMap = null;

	protected double exclure_mois_1 = 0;

	public ClsDate getFictiveMyMonthOfPay()
	{
		return fictiveMyMonthOfPay;
	}

	public void setFictiveMyMonthOfPay(ClsDate fictiveMyMonthOfPay)
	{
		this.fictiveMyMonthOfPay = fictiveMyMonthOfPay;
	}

	public double getNombreHeure()
	{
		return nombreHeure;
	}

	public void setNombreHeure(double nombreHeure)
	{
		this.nombreHeure = nombreHeure;
	}

	public int getBasePretsNb_ret()
	{
		return BasePretsNb_ret;
	}

	public void setBasePretsNb_ret(int basePretsNb_ret)
	{
		BasePretsNb_ret = basePretsNb_ret;
	}

	public int getAncienneteMini()
	{
		return ancienneteMini;
	}

	public void setAncienneteMini(int ancienneteMini)
	{
		this.ancienneteMini = ancienneteMini;
	}

	public String getFictiveMonthOfPay()
	{
		return fictiveMonthOfPay;
	}

	public void setFictiveMonthOfPay(String fictiveMonthOfPay)
	{
		this.fictiveMonthOfPay = fictiveMonthOfPay;
	}

	public String getNomClient()
	{
		return nomClient;
	}

	public void setNomClient(String nomClient)
	{
		this.nomClient = nomClient;
	}

	public Date getFictiveLastDayOfMonth()
	{
		return fictiveLastDayOfMonth;
	}

	public void setFictiveLastDayOfMonth(Date fictiveLastDayOfMonth)
	{
		this.fictiveLastDayOfMonth = fictiveLastDayOfMonth;
	}

	public Date getFictiveFirstDayOfMonth()
	{
		return fictiveFirstDayOfMonth;
	}

	public void setFictiveFirstDayOfMonth(Date fictiveFirstDayOfMonth)
	{
		this.fictiveFirstDayOfMonth = fictiveFirstDayOfMonth;
	}

	public ClsFictifNomenclatureUtil getUtilNomenclatureFictif()
	{
		return utilNomenclatureFictif;
	}

	public void setUtilNomenclatureFictif(ClsFictifNomenclatureUtil utilNomenclatureFictif)
	{
		this.utilNomenclatureFictif = utilNomenclatureFictif;
	}

	public String getFictiveRubrique()
	{
		return fictiveRubrique;
	}

	public void setFictiveRubrique(String fictiveRubrique)
	{
		this.fictiveRubrique = fictiveRubrique;
	}

	public int getFictiveNombreDeJour()
	{
		return fictiveNombreDeJour;
	}

	public void setFictiveNombreDeJour(int fictiveNombreDeJour)
	{
		this.fictiveNombreDeJour = fictiveNombreDeJour;
	}

	public String getBaseCongeRubrique()
	{
		return baseCongeRubrique;
	}

	public void setBaseCongeRubrique(String baseCongeRubrique)
	{
		this.baseCongeRubrique = baseCongeRubrique;
	}

	public String getNapRubrique()
	{
		return napRubrique;
	}

	public void setNapRubrique(String napRubrique)
	{
		this.napRubrique = napRubrique;
	}



	public String getSalaireMoyenMensuelRubrique()
	{
		return salaireMoyenMensuelRubrique;
	}

	public void setSalaireMoyenMensuelRubrique(String salaireMoyenMensuelRubrique)
	{
		this.salaireMoyenMensuelRubrique = salaireMoyenMensuelRubrique;
	}

	public double getExclure_mois_1()
	{
		return exclure_mois_1;
	}

	public void setExclure_mois_1(double exclure_mois_1)
	{
		this.exclure_mois_1 = exclure_mois_1;
	}

	public ClsFictifParameterOfPay()
	{

	}

	/**
	 * Constructeur permet de créer une instance de paramètres
	 *
	 *            le service d'accès à la base de données
	 * @param utilNomenclatureFictif
	 *            l'utilitaire d'accès aux tables de nommenclatures et aux données historisées
	 *            le mode de paiement (V, E, C)
	 */
	public ClsFictifParameterOfPay(ClsFictifNomenclatureUtil utilNomenclatureFictif)
	{
		this.utilNomenclatureFictif = utilNomenclatureFictif;
		this.fictiveMonthOfPay = this.monthOfPay;
		this.fictiveFirstDayOfMonth = this.firstDayOfMonth;
		this.fictiveLastDayOfMonth = this.lastDayOfMonth;
		this.fictiveMyMonthOfPay = this.myMonthOfPay;
		this.fictiveDossierDateDebutExercice = this.dossierDateDebutExercice;
	}

	public void setFictiveParams(ClsFictifParameterOfPay param)
	{
		this.fictiveMonthOfPay = param.monthOfPay;
		this.fictiveFirstDayOfMonth = param.firstDayOfMonth;
		this.fictiveLastDayOfMonth = param.lastDayOfMonth;
		this.fictiveMyMonthOfPay = param.myMonthOfPay.clone();
		this.fictiveDossierDateDebutExercice = param.dossierDateDebutExercice;
	}

	public void initFictivesList()
	{

	}

	/**
	 * => Charg_TauxAbs
	 * <p>
	 * Initialise the taux rubrique absence map construit le map qui fait correspondre chaque rubrique d'absence à son taux.
	 * </p>
	 */

	public void chargementTauxAbsence()
	{

		String query = "select cacc, valm from ParamData" + " where identreprise ='" + dossier + "'" + " and ctab = 22" + " and nume = 8" + " and valm <> 0";
		List listOfParam = service.find(query);
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
			rubrique = ClsStringUtil.formatNumber((obj[1] instanceof BigDecimal) ? ((BigDecimal) obj[1]).intValue() : ((Long) obj[1]).intValue(), FORMAT_PERIOD_OF_RBQ);
			taux = (tempNumber = utilNomenclatureFictif.getAmountOrRateFromNomenclature(dossier, 22, cacc, 1, nlot, numeroBulletin, monthOfPay, ClsEnumeration.EnColumnToRead.RATES)) == null ? 0
					: tempNumber.doubleValue();
			hTauxAppliedToRubriqueAbsence.put(rubrique, taux);
		}
	}

	/**
	 * => Charg_TmpNet initialise the table of adjustment values -- PAIE AU NET : -- Creation et chargement des tables temporaires contenant -- les rubriques
	 * saisies en net -- les rubriques soumises a ajustement -- Les montants correspondant sont charges pour chaque salarie ( E.V. ou E.F. )
	 */
	public void chargementTableValeurAjustementFictif()
	{

		String queryString = "select count(*) from Rhtrubnet " + " where session_id ='" + session_id + "'";
		List listOfRubrique = service.find(queryString);
		if (listOfRubrique != null && listOfRubrique.size() > 0)
		{
			Object obj = listOfRubrique.get(0);
			if (Integer.valueOf(obj.toString()) != 0)
				this.setRubriqueNetExist(true);
		}

		queryString = "select count(*) from ElementSalaireajus " + " where session_id ='" + session_id + "'";
		listOfRubrique = service.find(queryString);
		if (listOfRubrique != null && listOfRubrique.size() > 0)
		{
			Object obj = listOfRubrique.get(0);
			if (Integer.valueOf(obj.toString()) != 0)
				this.setRubriqueAdjustExist(true);
		}

		// ----------------------------------------------------------------------------
		// -- Chargement de - t_rub avec les rubriques a calculer (Table memoire)
		// -- - t_rub_basc avec le nombre de rubriques de la formule
		// -- et indice dans tab_basc
		// -- - tab_basc avec les formules de calcul
		// -- - tab_rub avec les rubriques concernees par les regul.
		// ----------------------------------------------------------------------------
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
		// WHERE cdos = wpdos.cdos
		// AND rreg = 'O'
		// AND rman = 'N'
		// ORDER BY cdos, crub;
		//
		String queryString = "select rcon from ElementSalaire where identreprise ='" + dossier + "'" + " and rreg = 'O'" + " and rman = 'N'" + " order by cdos, crub";
		// CURSOR curs_rubreg2 IS
		// SELECT rcon FROM pahrubq
		// WHERE cdos = wpdos.cdos
		// AND rreg = 'O'
		// AND rman = 'N'
		// AND aamm = w_aamm
		// AND nbul = wsd_fcal1.nbul
		// ORDER BY cdos, crub;
		String queryStringRetro = "select rcon from Rhthrubq where identreprise ='" + dossier + "'" + " and rreg = 'O'" + " and rman = 'N'" + " and aamm = '" + monthOfPay + "'" + " and nbul = "
				+ numeroBulletin + " order by cdos, crub";

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
		// WHERE cdos = wpdos.cdos
		// AND ctab = 10
		// AND nume = 3
		// ORDER BY cacc;
		//
		// String queryString10 = "select cacc, vall, valm from ParamData " + " where identreprise ='" + dossier + "'" + " and ctab = 10" + " and
		// nume = 3" + " order by cacc";
		// -- Curseur sur les banques
		// CURSOR Curs_TB10_2 IS
		// SELECT cacc, vall, NVL(valm, 0) FROM pahfnom
		// WHERE cdos = wpdos.cdos
		// AND ctab = 10
		// AND nume = 3
		// AND aamm = w_aamm
		// AND nbul = wsd_fcal1.nbul
		// ORDER BY cacc;
		// String queryString10Retro = "select cacc, vall, valm from Rhthfnom " + " where identreprise ='" + dossier + "'" + " and ctab = 10" + " and
		// nume = 3" + " and aamm = '"
		// + monthOfPay + "'" + " and nbul = " + numeroBulletin + " order by cacc";
		//
		// -- Curseur sur les devises
		// CURSOR Curs_TB27 IS
		// SELECT valt, valm FROM pafnom
		// WHERE cdos = wpdos.cdos
		// AND ctab = 27
		// AND cacc = Devise_Banque
		// AND nume = 1;
		//
		// String queryString27 = "select valt, valm from ParamData " + " where identreprise ='" + dossier + "'" + " and ctab = 27" + " and nume = 1" + "
		// and cacc = '" + bankCcy + "'"
		// + " order by cacc";
		// -- Curseur sur les devises
		// CURSOR Curs_TB27_2 IS
		// SELECT valt, valm FROM pahfnom
		// WHERE cdos = wpdos.cdos
		// AND ctab = 27
		// AND cacc = Devise_Banque
		// AND aamm = w_aamm
		// AND nbul = wsd_fcal1.nbul
		// AND nume = 1;
		// String queryString27Retro = "select valt, valm from Rhthfnom " + " where identreprise ='" + dossier + "'" + " and ctab = 27" + " and nume =
		// 1" + " and cacc = '" + bankCcy
		// + "'" + " and aamm = '" + monthOfPay + "'" + " and nbul = " + numeroBulletin + " order by cacc";
		//
		ClsInfoOfBank oInfoOfBank = null;
		String queryString10Retro = "SELECT banque.cacc as codebanque, banque.vall as devisebanque, banque.valm as virmini , " + " devise.valt as tauxchange, devise.valm as nbdecimale "
				+ "FROM Rhthfnom banque " + "Left join Rhthfnom devise on( " + "banque.cdos=devise.cdos and devise.ctab=27 and devise.nume=1 and devise.cacc=banque.vall "
				+ "and banque.aamm = devise.aamm and banque.nbul = devise.nbul) " + "WHERE banque.identreprise ='" + dossier + "' " + "AND banque.ctab = 10 " + "AND banque.nume = 3 " + "AND devise.aamm = '"
				+ monthOfPay + "' " + "AND banque.nbul = " + numeroBulletin + " " + "ORDER BY banque.cacc ";
		String queryString10 = "SELECT banque.cacc as codebanque, banque.vall as devisebanque, banque.valm as virmini , " + " devise.valt as tauxchange, devise.valm as nbdecimale "
				+ "FROM ParamData banque " + "Left join ParamData devise on( " + "banque.cdos=devise.cdos and devise.ctab=27 and devise.nume=1 and devise.cacc=banque.vall) " + "WHERE banque.identreprise ='"
				+ dossier + "' " + "AND banque.ctab = 10 " + "AND banque.nume = 3 " + "ORDER BY banque.cacc ";

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
				oInfoOfBank.setVirementMini(NumberUtils.createLong(ligne[2].toString()));

			if (ligne[3] != null)
				oInfoOfBank.setExchangeRate(NumberUtils.createDouble(ligne[3].toString()));

			if (ligne[4] != null)
				oInfoOfBank.setNbreDecimal(NumberUtils.createInteger(ligne[4].toString()));

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
	 * Permet d'initialiser tous les paramètres pour effectuer le calcul de la paie. Ces paramètres sont globaux et s'applique donc à tous les objets
	 * responsables du calcul.
	 *
	 * @return true ou false quand il y a un paramètre qui manque de valeur
	 */

	/**
	 * @return
	 */
	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean init()
	{
		double tamo = 0;

		// -- Permet de parametrer les colonnes Taux pour les prets
		tamo = (tempNumber = utilNomenclatureFictif.getAmountOrRateFromT99(listOfTable99Map, dossier, 99, "GESPRT", 2, nlot, monthOfPay, ClsEnumeration.EnColumnToRead.AMOUNT)) == null ? 0
				: tempNumber.doubleValue();
		BasePretsNb_ret = new BigDecimal(tamo).intValue();

		// -- Lecture rubrique salaire moyen mensuel
		tamo = (tempNumber = utilNomenclatureFictif.getAmountOrRateFromT99(listOfTable99Map, dossier, 99, "RUBSMM", 1, nlot, monthOfPay, ClsEnumeration.EnColumnToRead.AMOUNT)) == null ? 0
				: tempNumber.doubleValue();
		if (tempNumber == null)
		{
			error = errorMessage("ERR-30153", langue, "99", "RUBSMM");
			return false;
		}
		salaireMoyenMensuelRubrique = tamo > 0 ? ClsStringUtil.formatNumber(tamo, FORMAT_PERIOD_OF_RBQ) : "    ";

		// -- Anciennete Mini
		tamo = (tempNumber = utilNomenclatureFictif.getAmountOrRateFromT99(listOfTable99Map, dossier, 99, "ANC-MAX", 2, nlot, monthOfPay, ClsEnumeration.EnColumnToRead.AMOUNT)) == null ? 0
				: tempNumber.doubleValue();
		ancienneteMini = new Double(tamo).intValue();

		// -- Chargement des taux de reglement d'absence
		chargementTauxAbsence();

		// -------------------------------------------------------------
		// -- FICTIF : initialisation des parametres
		// -------------------------------------------------------------

		//--Exclusion du premier fictif
		tamo = (tempNumber = utilNomenclatureFictif.getAmountOrRateFromT99(listOfTable99Map, dossier, 99, "FICTIF",3, nlot, monthOfPay, ClsEnumeration.EnColumnToRead.AMOUNT)) == null ? 0
				: tempNumber.doubleValue();
		exclure_mois_1 = tamo;

		fictiveCalculusA = false;
		fictiveCalculusB = false;
		fictiveCalculusNon = false;
		if ("N".equals(fictiveCalculus))
		{
			fictiveCalculusNon = true;
		}
		if ("O".equals(fictiveCalculus))
		{
			if ("A".equals(fictiveCalculusType))
				fictiveCalculusA = true;
			else
				fictiveCalculusB = true;
		}

		// -- Lecture Mnt1 = Rubrique somme des nets a payer (avance conges)
		// -- Lecture Mnt2 = jours minimum pour calcul rub. dernier mois
		String query = " SELECT sum(case nume when 1 then valm else 0 end ) as somme1, sum(case nume when 2 then valm else 0 end) as somme2";
		query += "  FROM ParamData WHERE identreprise ='" + dossier + "' AND ctab = 99 AND cacc = 'FICTIF' AND nume IN (1,2)";
		List<Object[]> l = this.getService().find(query);
		if ((!l.isEmpty()) && l.get(0)[0] != null && l.get(0)[1] != null)
		{
			fictiveRubrique = ClsStringUtil.formatNumber((Long) l.get(0)[0], FORMAT_PERIOD_OF_RBQ);
			fictiveNombreDeJour = ((Long) l.get(0)[1]).intValue();
		}
		else
		{
			error = errorMessage("ERR-90014", langue);
			return false;
		}

		// -- Lecture Mnt1 = No Rubrique BASE CONGE
		tamo = (tempNumber = utilNomenclatureFictif.getAmountOrRateFromT99(listOfTable99Map, dossier, 99, "BASE-CONGE", 1, nlot, monthOfPay, ClsEnumeration.EnColumnToRead.AMOUNT)) == null ? 0
				: tempNumber.doubleValue();
		if (tamo <= 0)
		{
			error = errorMessage("ERR-90126", langue);
			return false;
		}
		baseCongeRubrique = ClsStringUtil.formatNumber(tamo, FORMAT_PERIOD_OF_RBQ);

		// -- Lecture Mnt1 = No Rubrique Net a payer
		tamo = (tempNumber = utilNomenclatureFictif.getAmountOrRateFromT99(listOfTable99Map, dossier, 99, "RUBNAP", 1, nlot, monthOfPay, ClsEnumeration.EnColumnToRead.AMOUNT)) == null ? 0
				: tempNumber.doubleValue();
		if (tamo <= 0)
		{
			error = errorMessage("INF-10116", langue);
			return false;
		}
		napRubrique = ClsStringUtil.formatNumber(tamo, FORMAT_PERIOD_OF_RBQ);

		// ----------------------------------------------------------------------------
		// -- PAIE AU NET :
		// -- Creation et chargement des tables temporaires contenant
		// -- - les rubriques saisies en net
		// -- - les rubriques soumises a ajustement
		// -- Les montants correspondant sont charges pour chaque salarie ( E.V. ou E.F. )
		// -- LH 160198
		// -- * Les tables parubnet et parubajus ne doivent pas etre chargees dans
		// -- pacalfic si elle l'ont ete dans pacalcul. Le repere se fait sur w_session_id
		// -- qui est transmis de pacalcul a pacalfic.
		// ----------------------------------------------------------------------------
		chargementTableValeurAjustementFictif();

		//nomClient = ClsConfigurationParameters.getConfigParameterValue(service, dossier, langue, ClsConfigurationParameters.NOM_CLIENT);

		return true;
		// END Init;
	}

	/**
	 * => charg_per_paie Chargement des periodes de paie
	 */
	public void chargerPeriodeDePaieFictif()
	{
		String libelle1 = utilNomenclatureFictif.getLabelFromNomenclature(dossier, 91, monthOfPay, 1, nlot, numeroBulletin, monthOfPay);
		if (StringUtils.isBlank(libelle1))
		{
			calculatePremierEtDernierJourMoisPaie(monthOfPay);
			return;
		}

		String libelle2 = utilNomenclatureFictif.getLabelFromNomenclature(dossier, 91, monthOfPay, 2, nlot, numeroBulletin, monthOfPay);

		if (StringUtils.isBlank(libelle2))
		{
			calculatePremierEtDernierJourMoisPaie(monthOfPay);
			return;
		}

		String libelle3 = utilNomenclatureFictif.getLabelFromNomenclature(dossier, 91, monthOfPay, 3, nlot, numeroBulletin, monthOfPay);

		if (StringUtils.isBlank(libelle3))
		{
			calculatePremierEtDernierJourMoisPaie(monthOfPay);
			return;
		}

		nombreHeure = new Double((tempNumber = utilNomenclatureFictif
				.getAmountOrRateFromNomenclature(dossier, 91, monthOfPay, 1, nlot, numeroBulletin, monthOfPay, ClsEnumeration.EnColumnToRead.RATES)) == null ? 0 : tempNumber.doubleValue()).doubleValue();

		if (nombreHeure <= 0)
		{
			calculatePremierEtDernierJourMoisPaie(monthOfPay);
			return;
		}
		String[] datePatterns = new String[] { "dd-MM-yyyy", "dd/MM/yyyy", "yyyy-MM-dd", "yyyy/MM/dd", "ddMMyyyy" };
		try
		{
			firstDayOfMonth = DateUtils.parseDate(libelle2, datePatterns);
			lastDayOfMonth = DateUtils.parseDate(libelle3, datePatterns);
			table91LabelNotEmpty = true;
			if(StringUtils.equals(monthOfPay, fictiveMonthOfPay))
			{
				fictiveFirstDayOfMonth = firstDayOfMonth;
				fictiveLastDayOfMonth = lastDayOfMonth;
			}
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

		String query = " SELECT count(*)  FROM ParamData WHERE identreprise ='" + dossier + "' and ctab=91 and cacc = '" + cods + "' and nume = 1";
		Integer count = Integer.valueOf(session.createSQLQuery(query).list().get(0).toString());
		service.closeSession(session);
		if(count==0)
		{
			calculatePremierEtDernierJourMoisPaie(monthOfPay);
			return;
		}

		String libelle1 = utilNomenclatureFictif.getLabelFromNomenclature(dossier, 91, monthOfPay, 1, nlot, numeroBulletin, monthOfPay);
		if (StringUtils.isBlank(libelle1))
		{
			calculatePremierEtDernierJourMoisPaie(monthOfPay);
			return;
		}

		String libelle2 = utilNomenclatureFictif.getLabelFromNomenclature(dossier, 91, monthOfPay, 2, nlot, numeroBulletin, monthOfPay);

		if (StringUtils.isBlank(libelle2))
		{
			calculatePremierEtDernierJourMoisPaie(monthOfPay);
			return;
		}

		String libelle3 = utilNomenclatureFictif.getLabelFromNomenclature(dossier, 91, monthOfPay, 3, nlot, numeroBulletin, monthOfPay);

		if (StringUtils.isBlank(libelle3))
		{
			calculatePremierEtDernierJourMoisPaie(monthOfPay);
			return;
		}

		nombreHeure = new Double((tempNumber = utilNomenclatureFictif
				.getAmountOrRateFromNomenclature(dossier, 91, monthOfPay, 1, nlot, numeroBulletin, monthOfPay, ClsEnumeration.EnColumnToRead.RATES)) == null ? 0 : tempNumber.intValue()).intValue();

		if (nombreHeure <= 0)
		{
			calculatePremierEtDernierJourMoisPaie(monthOfPay);
			return;
		}
		String[] datePatterns = new String[] { "dd-MM-yyyy", "dd/MM/yyyy", "yyyy-MM-dd", "yyyy/MM/dd", "ddMMyyyy" };
		try
		{
			firstDayOfMonth = DateUtils.parseDate(libelle2, datePatterns);
			lastDayOfMonth = DateUtils.parseDate(libelle3, datePatterns);
			table91LabelNotEmpty = true;
			if(StringUtils.equals(monthOfPay, fictiveMonthOfPay))
			{
				fictiveFirstDayOfMonth = firstDayOfMonth;
				fictiveLastDayOfMonth = lastDayOfMonth;
			}
		}
		catch (ParseException e)
		{
			e.printStackTrace();
			calculatePremierEtDernierJourMoisPaie(periodOfPay);
		}
	}

	/**
	 * calcule le premier et le dernier jour du mois de paie. par exemple pour Août: le premier jour c'est 01/08 et le dernier jour c'est 31/08
	 *
	 * @param monthOfPay
	 */
	private void calculatePremierEtDernierJourMoisPaie(String monthOfPay)
	{
		ClsDate oClsDate = new ClsDate(monthOfPay, FORMAT_DATE_PAY_PERIOD_YYYYMM);
		firstDayOfMonth = oClsDate.getFirstDayOfMonth();
		lastDayOfMonth = oClsDate.getLastDayOfMonth();
		firstDayOfMonthS = new ClsDate(firstDayOfMonth).getDateS(this.appDateFormat);
		lastDayOfMonthS = new ClsDate(lastDayOfMonth).getDateS(this.appDateFormat);
		table91LabelNotEmpty = false;
		if(StringUtils.equals(monthOfPay, fictiveMonthOfPay))
		{
			fictiveFirstDayOfMonth = firstDayOfMonth;
			fictiveLastDayOfMonth = lastDayOfMonth;
		}
	}

	private boolean rubriqueExiste(String rubrique)
	{
		// com.cdi.deltarh.service.ClsParameter.println(">>rubriqueExiste");
		List l = service.find("from ElementSalaire where identreprise ='" + dossier + "' and crub = '" + rubrique + "'");
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

	public ClsFictifParameterOfPay clone()
	{
		ClsFictifParameterOfPay param = new ClsFictifParameterOfPay(utilNomenclatureFictif);
		BeanUtils.copyProperties(this, param);
		param.setMyMoisPaieCourant(myMoisPaieCourant.clone());
		param.setMyMonthOfPay(myMonthOfPay.clone());
		param.setFictiveParams(this);
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
						// pour les méthodes qui commencent par is
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

	public String errorMessage(String errorCode, String langue, Object... param)
	{
		String message = errorMessages(errorCode, langue, param);

		writeToLocalLog(message);

		return message;
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
	public String errorMessages(String errorCode, String langue, Object... param)
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
			Message message = (Message) service.find("FROM Message WHERE cdmes='"+errorCode+"' AND clang='"+langue+"'");
			if (message == null){
				message = new Message();
				message.setClang(langue);
				message.setCdmes(errorCode);
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
						libelleMessage = libelleMessage.replace(match, params.toString());
				}
				//
			}
		}
		catch (DataAccessException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// END erreurp;

		// Insertion yannick

		try
		{
			insererLogMessage(libelleMessage);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return libelleMessage;
	}

	/**
	 * =>pap_logins
	 *
	 * @param error
	 */
	public void insererLogMessage(String error)
	{
		LogMessage log = new LogMessage();
		log.setIdEntreprise(Integer.valueOf(getDossier()));
		log.setCuti(uti);
		log.setDatc(new Date());
		log.setLigne(error);
		//
		service.save(log);
   }

	/**
	 * chargement des zones libres
	 */
	public void buildRubriqueOfZoneLibreMap()
	{

		Map<String, List<Object>> map = new HashMap<String, List<Object>>();
//		String queryStringZli = "from ElementSalaireZonelibre " + " where identreprise ='" + this.getDossier() + "'";
//
//		List listOfRubZli = this.getService().find(queryStringZli);
//		//
//		ElementSalaireZonelibre zonelibre2 = null;
//		for (Object object : listOfRubZli)
//		{
//			// map.put(crub, value)
//			zonelibre2 = (ElementSalaireZonelibre) object;
//			if (!map.containsKey(zonelibre2.getComp_id().getCrub()))
//				map.put(zonelibre2.getComp_id().getCrub(), new ArrayList<Object>());
//			//
//			map.get(zonelibre2.getComp_id().getCrub()).add(zonelibre2);
//		}
		//
		listOfRubqZoneLibre = map;
	}

	/**
	 * chargement des rubriques de base
	 */
	public void buildRubriqueOfBaseMap()
	{

		Map<String, List<Object[]>> map = new HashMap<String, List<Object[]>>();
		String queryString = "select crub, nume, sign, rubk from ElementSalairebase " + " where identreprise ='" + this.getDossier() + "'" +
				// " and crub in ( select crub from ElementSalaire" +
				// " where identreprise ='" + this.getDossier() + "'" +
				// " and ( (basc = 'O' and trtc = 'N') or mopa = 'O'))" +
				" order by cdos, crub, nume";

		String queryStringRetro = "select crub, nume, sign, rubk from Rhthrbqba " + " where identreprise ='" + this.getDossier() + "'" + " and aamm = '" + this.getMonthOfPay() + "'"
				+ " and nbul = '" + this.getNumeroBulletin() + "'" +
				// " and crub in ( select crub from ElementSalaire" +
				// " where identreprise ='" + this.getDossier() + "'" +
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
			l = service.find("from ParamData " + " where identreprise ='" + dossier + "'" + " and ctab = 63" + " and nume = 2");
			l1 = service.find("from ParamData " + " where identreprise ='" + dossier + "'" + " and ctab = 63" + " and nume = 1");
		}
		else
		{
			l = service.find("from Rhthfnom " + " where identreprise ='" + dossier + "'" + " and ctab = 63" + " and nume = 2" + " and nbul = " + numeroBulletin
					+ " and aamm = '" + monthOfPay + "'");
			l1 = service.find("from Rhthfnom " + " where identreprise ='" + dossier + "'" + " and ctab = 63" + " and nume = 1" + " and nbul = " + numeroBulletin
					+ " and aamm = '" + monthOfPay + "'");
		}
		//
		ParamData o1 = null;
		//Rhthfnom o2 = null;
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
			else
			{
//				o2 = (Rhthfnom) object;
//				arrond = new Object[2];
//				arrond[0] = o2.getVall();
//				arrond[1] = 0;
//				listOfArrondi.put(o2.getComp_id().getCacc(), arrond);
			}
		}
		// mise à jour des taux
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
			else
			{
//				o2 = (Rhthfnom) object;
//				arrond = listOfArrondi.get(o1.getComp_id().getCacc());
//				arrond[1] = o1.getValt();
//				listOfArrondi.put(o1.getComp_id().getCacc(), arrond);
			}
		}
	}

	/**
	 * chargement des valeurs T99 pour ne pas aller chercher dans la base lors de l'initialisation des paramètres de calcul
	 */
	public void buildTableT51T52T99Map()
	{

		listOfTable99Map = new HashMap<String, Object[]>();
		List l = null;
		if (!useRetroactif)
		{
			l = service.find("from ParamData " + " where identreprise ='" + dossier + "'" + " and ctab in (51, 52, 99)");
		}
		else
		{
			l = service.find("from Rhthfnom " + " where identreprise ='" + dossier + "'" + " and ctab in (51, 52, 99)" + " and nbul = " + numeroBulletin + " and aamm = '"
					+ monthOfPay + "'");
		}
		//
		ParamData o1 = null;
		//Rhthfnom o2 = null;
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
			else
			{
//				o2 = (Rhthfnom) object;
//				t99[0] = o2.getComp_id().getNume();
//				t99[1] = o2.getVall();
//				t99[2] = o2.getValm();
//				t99[3] = o2.getValt();
//				key = o2.getComp_id().getCacc() + o2.getComp_id().getNume();
//				//
//				listOfTable99Map.put(key.trim(), t99);
			}
		}
	}

	/**
	 * charge les rubriques appartenant à une session
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
	 * Construit la liste des rubriques. Cette liste sera partagée entre tous les salariés.
	 *
	 *            liste contenant soit les rubriques historisées ou non
	 */
	public void buildListOfRubrique(ClsFictifSalaryToProcess salary)
	{
		ClsFictifRubriqueClone rubrique = null;
		ClsParubqClone rub = null;
		try
		{
			if(ListOfAllRubriqueMap != null)
			{
				Set<String> keys = ListOfAllRubriqueMap.keySet();
				if (ListOfAllFictiveRubriqueMap == null)
					ListOfAllFictiveRubriqueMap = new HashMap<String, ClsFictifRubriqueClone>();
				for (String key : keys)
				{
					rubrique = new ClsFictifRubriqueClone(salary);
					//
					rub = new ClsParubqClone();
					BeanUtils.copyProperties(ListOfAllRubriqueMap.get(key).getRubrique(), rub);
					//
					rubrique.setRubrique(rub);
					ListOfAllFictiveRubriqueMap.put(key, rubrique);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void reBuildListOfRubrique(ClsFictifSalaryToProcess salary, String minRubrique)
	{
		Iterator iter = salary.param.getListOfAllFictiveRubriqueMap().keySet().iterator();
		String key = null;
		ClsFictifRubriqueClone rubrique = null;
		ClsFictifRubriqueClone rubrique2 = null;
		while(iter.hasNext())
		{
			key = (String) iter.next();
			rubrique = salary.param.getListOfAllFictiveRubriqueMap().get(key);
			rubrique2 = new ClsFictifRubriqueClone(salary);
			rubrique2.setRubrique(rubrique.getRubrique());
			if(rubrique2.getRubrique().getComp_id().getCrub().compareTo(minRubrique) > 0)
				salary.param.getListOfAllFictiveRubriqueMap().put(key, rubrique2);
		}
	}



	public void buildListOfRubriquebaremeMap()
	{
		// com.cdi.deltarh.service.ClsParameter.println("<<buildListOfRubrique");
		listOfRubriquebaremeMap = new HashMap<String, Object>();
		String queryString = "from ElementSalairebareme " + " where identreprise ='" + dossier + "'" + " order by cdos , crub , nume";
		String queryStringRetro = "from Rhthrubbarem " + " where identreprise ='" + dossier + "'" + " and aamm = '" + this.getMonthOfPay() + "'" + " and nbul = "
				+ this.getNumeroBulletin() + " order by cdos , crub , nume";

		List<Object> listOfRubriquebareme = new ArrayList<Object>();

		List<Object> listOfRubriquebaremeRetroactif = new ArrayList<Object>();
		try
		{
			String keyOfBaremeList = "";
			// construit la liste des barêmes
			List listOfBarem = this.isUseRetroactif() ? service.find(queryStringRetro) : service.find(queryString);

			//Rhthrubbarem oRhthrubbarem = null;

			ElementSalaireBareme oElementSalairebareme = null;

			String lastrub = "";

//			if (this.isUseRetroactif())
//			{
//				for (Object barem : listOfBarem)
//				{
//					oRhthrubbarem = (ElementSalaireBareme) barem;
//
//					keyOfBaremeList = oRhthrubbarem.getComp_id().getCrub() + oRhthrubbarem.getComp_id().getAamm() + oRhthrubbarem.getComp_id().getNbul();
//
//					if (!oRhthrubbarem.getComp_id().getCrub().equals(lastrub))
//					{
//						listOfRubriquebareme = new ArrayList<Object>();
//					}
//					listOfRubriquebareme.add(barem);
//
//					listOfRubriquebaremeMap.put(keyOfBaremeList, listOfRubriquebareme);
//
//					lastrub = oRhthrubbarem.getComp_id().getCrub();
//
//				}
//			}
//			else
//			{
				for (Object barem : listOfBarem)
				{

					oElementSalairebareme = (ElementSalaireBareme) barem;

					keyOfBaremeList = oElementSalairebareme.getCrub();

					if (!oElementSalairebareme.getCrub().equals(lastrub))
					{

						if ("".equalsIgnoreCase(lastrub))
						{
							listOfRubriquebareme = new ArrayList<Object>();
							listOfRubriquebareme.add(barem);
						}
						else
						{
							listOfRubriquebaremeMap.put(lastrub, listOfRubriquebareme);
							listOfRubriquebareme = new ArrayList<Object>();
							listOfRubriquebareme.add(barem);
						}

					}
					else
					{
						listOfRubriquebareme.add(barem);
					}

					lastrub = oElementSalairebareme.getCrub();
				}
			//}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	public synchronized Map<String, ClsFictifRubriqueClone> getListOfAllFictiveRubriqueMap()
	{
		return ListOfAllFictiveRubriqueMap;
	}

	public synchronized void setListOfAllFictiveRubriqueMap(Map<String, ClsFictifRubriqueClone> listOfAllRubriqueMap)
	{
		ListOfAllFictiveRubriqueMap = listOfAllRubriqueMap;
	}

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

	public String getFictiveDossierDateDebutExercice()
	{
		return fictiveDossierDateDebutExercice;
	}

	public void setFictiveDossierDateDebutExercice(String fictiveDossierDateDebutExercice)
	{
		this.fictiveDossierDateDebutExercice = fictiveDossierDateDebutExercice;
	}

	public CongeFictifService getCongeFictifService() {
		return congeFictifService;
	}

	public void setCongeFictifService(CongeFictifService congeFictifService) {
		this.congeFictifService = congeFictifService;
	}
}
