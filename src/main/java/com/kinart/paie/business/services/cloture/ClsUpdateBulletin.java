package com.kinart.paie.business.services.cloture;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import javax.servlet.http.HttpServletRequest;

import com.kinart.paie.business.model.DossierPaie;
import com.kinart.paie.business.model.ElementSalaire;
import com.kinart.paie.business.model.ParamData;
import com.kinart.paie.business.model.Salarie;
import com.kinart.paie.business.services.calcul.ClsParameterOfPay;
import com.kinart.paie.business.services.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Session;

@SuppressWarnings("unused")
public class ClsUpdateBulletin implements IFutureListener<Boolean>
{
	public ArrayList<FutureTalker> allTasks;
	public UIClotureMensuelle ui;
	
	public ClsGlobalUpdate globalUpdate;
	
	public int numsalariecourant;

	public int nbsalarietotal;

	CountDownLatch latch;

	ExecutorService threadPool;

	Session oSession = null;

	boolean finalResult = true;

	public HttpServletRequest request;

	protected GeneriqueConnexionService service = null;

	public ClsUpdateBulletinService updateservice = null;

	public String dateformat = "";
	
	public char genfile = 'N';
	
	public String genfilefolder;
	
	public String fusionRhtcongeagent="N";

	/***********************************************************************************************************************************************************
	 * -------------variables du out_maj-----------------------*
	 **********************************************************************************************************************************************************/
	protected String user = "";
	public String userEV = "";

	protected String dossier = "";

	protected int numerobulletin = 0;

	protected String periode = "";

	protected String langue = "";

	protected ClsBulletin bulletin = null;

	protected ClsParameterOfPay parameter = null;

	protected ClsDate myPeriode = null;

	protected String rubriqueNbreJourTravail = "";

	protected String rubriqueNbreJourPlage = "";

	protected boolean tab91b = true;

	protected String dernierePeriodeCloturee = null;

	protected String mois_ms = "";

	protected Date date_debut_mois_ms;

	protected Date date_fin_mois_ms;

	protected int nbul_ms = 0;

	protected List<ClsInfoPeriode> listePeriodesT91 = null;

	protected Map<String, String> mapRubriqueRegularisation = null;

	protected String am99 = "";

	protected boolean calc_part_auto = false;

	protected int max_part_fisc = 0;

	protected String motif_reliq = "";

	protected String rub_pnp = "";
	
	
	//-----------sp�cifiques CNSS
	protected boolean conge_montant;	//Permet de savoir si on gere les cong�s en montant (param CG-MONT)
	protected String rub_jours_acquis;    //Rubrique des nombres de jours acquis
	protected String rub_montant_acquis;  //VARCHAR2(4); --Rubrique du montant acquis
	protected String rub_pris;	      //VARCHAR2(4); --Rubrique du nombre de jours pris
	//----------Fin sp�cifique CNSS
	
	

	protected String val_exp = "";

	protected int colonne_expatrie = 0;

	protected String fictif = "";

	protected String typ_fictif = "";

	protected double nbjc_lx = 0;

	protected double nbjc_ex = 0;

	protected int nbbul = 0;
	
	protected int nbbul_non_edite = 0;

	protected String editMatricule = "";

	protected String[] t_ev_mc = new String[] { "", "", "", "", "", "", "", "","", "", "", "", "", "", "", "" };

	protected String[] t_ev_ms = new String[] { "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "" };
	
	protected Map<String, String> t_ev_mc_ms = new HashMap<String, String>();

	protected String rub_bc = "";
	
	protected String rub_cg_ant = "";
	
	protected String rub_cg_ex = "";

	protected String calcul_auto_nbjsup = "";

	protected String ajout_bc = "";

	protected String pror_jr = "";

	protected String motif_cg_abs_ms = "";

	protected String raz_bc_jour = "";

	protected String rub_brut = "";

	protected boolean pror_jcg_normal = false;

	protected int age_max_enfant = 0;

	protected boolean comptage_enfant = false;

	protected String dbcg_barem1 = "";

	protected String fincg_barem1 = "";

	protected int nbjcg_barem1 = 0;

	protected String dbcg_barem2 = "";

	protected String fincg_barem2 = "";

	protected int nbjcg_barem2 = 0;

	protected String dbcg_barem3 = "";

	protected String fincg_barem3 = "";

	protected int nbjcg_barem3 = 0;

	protected int type_dtanniv = 0;

	protected boolean t91hc = false;

	protected boolean t91hp = false;

	protected boolean t91mc = false;

	protected boolean t91mp = false;

	protected boolean t91in = false;

	protected String prochainmois = "";

	protected Date date_debut_prochainmois;

	protected Date date_fin_prochainmois;

	protected String cas = "";

	String error = "";

	protected Date dtDdmp = null;

	protected Date dtDdex = null;
	protected Date dtFex = null;

	protected DossierPaie rhpdo = null;

	protected boolean ca = false;

	protected int minimum_enfant = 0;

	protected int age_max_fiscal = 0;

	protected String rub_nap = "";
	
	protected List<Salarie> listeSalaries = new ArrayList<Salarie>();
	
	protected Map<String, ParamData> listeDonneesParametrage = null;

	public Map<String, ElementSalaire> rubriquesMap = null;

	public Map<String, ParamData> RhfnomsMap = null;
	
	public Map<String, RhfnomMotifConge> RhfnomsMotifsCongesMap = null;
	
	public Map<String, RhfnomCalcPartFiscal> RhfnomsCalcPartFiscalMap = null;
	
	public Map<String, RhfnomNbParts> RhfnomsNbPartsMap = null;
	
	private String[] datePatterns = new String[] { "dd-MM-yyyy", "dd/MM/yyyy", "yyyy-MM-dd", "yyyy/MM/dd", "ddMMyyyy" };
	
	

	public synchronized ExecutorService getThreadPool()
	{
		return threadPool;
	}

	public synchronized void setThreadPool(ExecutorService threadPool)
	{
		this.threadPool = threadPool;
	}

	public synchronized boolean getFinalResult()
	{
		return finalResult;
	}

	public synchronized void setFinalResult(boolean finalResult)
	{
		this.finalResult = finalResult;
	}

	public ClsUpdateBulletin()
	{

	}

	public ClsUpdateBulletin(ClsGlobalUpdate globalUpdate,ClsParameterOfPay parameter, ClsUpdateBulletinService updateservice, GeneriqueConnexionService service, String dossier, String user,
			String periode, String langue, int numerobulletin, String dateformat)
	{
		this.user = user;
		this.dossier = dossier;
		this.numerobulletin = numerobulletin;
		this.periode = periode;
		this.langue = langue;
		this.service = service;
		this.parameter = parameter;
		this.updateservice = updateservice;
		this.updateservice.service = service;
		this.updateservice.update = this;
		this.bulletin = new ClsBulletin(service);
		this.dateformat = dateformat;
		this.globalUpdate = globalUpdate;
	}
	
	
	
	public boolean initVariables()
	{
		ParameterUtil.println(">>initialize parameters of clsupdatebulletin");
		
		/*----------------------------------------------------------------------------------------
		 * IL FAUT PENSER A REINITIALISER LES MAP, CAR L'OBJET UPDATEBULLETIN EST INSTANCIE AU DEMARAGE DE L'APPLICATION
		 * ET SES PROPRIETES NE PEUVENT CHANGER DE VALEUR QUE SI CES PROPRIETES SONT EXPLICITEMENT MODIFIEES
		 */
		rubriquesMap = new HashMap<String, ElementSalaire>();
		
		RhfnomsMap = new HashMap<String, ParamData>();
		
		RhfnomsMotifsCongesMap = new HashMap<String, RhfnomMotifConge>();
		
		RhfnomsCalcPartFiscalMap = new HashMap<String, RhfnomCalcPartFiscal>();
		
		RhfnomsNbPartsMap = new HashMap<String, RhfnomNbParts>();
		
		this.updateservice.update = this;
		
		try
		{
			this.rhpdo = updateservice.getRhpdos();
			if (rhpdo != null)
			{
				dtDdmp = rhpdo.getDdmp();
				dtDdex = rhpdo.getDdex();
				dtFex = rhpdo.getDfex();
				dernierePeriodeCloturee = new ClsDate(dtDdmp, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getDateS();
			}

			am99 = String.valueOf(new ClsDate(dtDdex).getYear()) + "99";

			this.langue = updateservice.getLangue();

			myPeriode = new ClsDate(periode, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
			
			if(!(myPeriode.getDate().compareTo(dtDdex)>=0 && myPeriode.getDate().compareTo(dtFex)<=0))
			{
				error = updateservice.errorMessage("INF-51016")+" "+new ClsDate(dtDdex).getDateS("dd/MM/yyyy");
				error +="<br>"+ updateservice.errorMessage("INF-51017")+" "+new ClsDate(dtFex).getDateS("dd/MM/yyyy");
				error +="<br>"+ updateservice.errorMessage("INF-00506")+" "+myPeriode.getDateS("dd/MM/yyyy");
				globalUpdate._setInitializeError(request, error, true);
			}
			
			prochainmois = updateservice.getProchainMois();

			ClsBulletin.InfoBulletin infoBulletin = bulletin.prochainePaie(dossier, periode, numerobulletin);
			mois_ms = infoBulletin.getPeriodepaie();
			nbul_ms = infoBulletin.getNbul();
			
			// charge toute les donn�es de nomenclatures des tables 91 et 99
			updateservice.getDonneesNomenclature91_99Map(); 
			
			String cle = "91"+mois_ms+"2";
			ParamData fnom = listeDonneesParametrage.get(cle);
			if (fnom != null && StringUtils.isNotBlank(fnom.getVall()))
			{
				
				date_debut_mois_ms = DateUtils.parseDate(fnom.getVall(), datePatterns);
				
				//date_debut_mois_ms = new ClsDate(fnom.getVall()).getDate();
			}
//		else
//		{
//			error = updateservice.errorMessage("ERR-90131", mois_ms);
//			globalUpdate._setInitializeError(request, error, true);		
//		}
			
			cle = "91"+mois_ms+"3";
			fnom = listeDonneesParametrage.get(cle);
			if (fnom != null && StringUtils.isNotBlank(fnom.getVall()))
			{
				date_fin_mois_ms = DateUtils.parseDate(fnom.getVall(), datePatterns);
				//date_fin_mois_ms = new ClsDate(fnom.getVall()).getDate();
			}
//		else
//		{
//			error = updateservice.errorMessage("ERR-90131", mois_ms);
//			globalUpdate._setInitializeError(request, error, true);	
//		}
			
			cle = "91"+prochainmois+"2";
			fnom = listeDonneesParametrage.get(cle);
			if (fnom != null && StringUtils.isNotBlank(fnom.getVall()))
			{
				date_debut_prochainmois = new ClsDate(fnom.getVall()).getDate();
			}
//		else
//		{
//			error = updateservice.errorMessage("ERR-90131", prochainmois);
//			globalUpdate._setInitializeError(request, error, true);	
//		}
			
			cle = "91"+prochainmois+"3";
			fnom = listeDonneesParametrage.get(cle);
			if (fnom != null && StringUtils.isNotBlank(fnom.getVall()))
			{
				date_fin_prochainmois = new ClsDate(fnom.getVall()).getDate();
			}
//		else
//		{
//			error = updateservice.errorMessage("ERR-90131", prochainmois);
//			globalUpdate._setInitializeError(request, error, true);	
//		}
			
			// -- Lecture de la rubrique du net a payer table 99
			cle = "99RUBNAP1";
			fnom = listeDonneesParametrage.get(cle);
			if(fnom != null && fnom.getValm()!=null)
			{
				rub_nap = ClsStringUtil.formatNumber(fnom.getValm(), ParameterUtil.formatRubrique);
			}
			else
			{
				error = updateservice.errorMessage("INF-10116");
				globalUpdate._setInitializeError(request, error, true);
			}
			
			// -- Lecture de la rubrique du nombre de jours travailles
			
			cle = "99RUBNBJTR1";
			fnom = listeDonneesParametrage.get(cle);
			if(fnom!=null && fnom.getValm()!=null)
			{
				rubriqueNbreJourTravail = ClsStringUtil.formatNumber(fnom.getValm(), ParameterUtil.formatRubrique);
				
				rubriqueNbreJourPlage = StringUtils.isNotBlank(fnom.getVall())?fnom.getVall():"M";
			}
			else
			{
				error = updateservice.errorMessage("ERR-90030");
				globalUpdate._setInitializeError(request, error, true);
			}
			
			// -- Lecture si code calcul automatique des parts fiscal.
			cle = "99CALCPART5";
			fnom = listeDonneesParametrage.get(cle);
			if (fnom != null)
				calc_part_auto = StringUtils.equalsIgnoreCase("O" ,fnom.getVall());
			else
				calc_part_auto = false;

			if (calc_part_auto)
			{
				cle = "99CALCPART2";
				fnom = listeDonneesParametrage.get(cle);
				if (fnom != null && fnom.getValm()!=null)
				{
					max_part_fisc = fnom.getValm().intValue();
				}
				else
					max_part_fisc = 0;
			}

			// -- Motif de conge reliquat
			cle = "99CGRELIQ2";
			fnom = listeDonneesParametrage.get(cle);
			if (fnom != null && StringUtils.isNotBlank(fnom.getVall()))
				motif_reliq = StringUtils.substring(fnom.getVall(), 0, 2);
			else
				motif_reliq = null;
			
			// -- Lecture de la rubrique du nombre de jours de conges payes non pris
			cle = "99NBJPNP1";
			fnom = listeDonneesParametrage.get(cle);
			if (fnom != null && fnom.getValm() != null)
			{
				rub_pnp = ClsStringUtil.formatNumber(fnom.getValm(), ParameterUtil.formatRubrique);
			}
			else
			{
				error = updateservice.errorMessage("ERR-90028");
				globalUpdate._setInitializeError(request, error, true);
			}
			
			// sp�cifique cnss
			if (StringUtils.equals(globalUpdate.nomClient, ClsEntreprise.CNSS))
			{		
//			---------------------------------------------------------
//			  --TFN 12/2006 INIT pour le calcul des cong�s en montant--
//			  ---------------------------------------------------------
//			  --lecture du param�tre d'activation
//			  BEGIN
//			  SELECT valm INTO wfnom.mnt1
//			    FROM pafnom
//			   WHERE cdos = wpdos.cdos
//			     AND ctab = 99
//			     AND cacc = 'CG-MONT'
//			     AND nume = 1;
//			  EXCEPTION
//			    WHEN NO_DATA_FOUND THEN
//			       w_conge_montant := FALSE;
//			  END;
//			  IF SQL%NOTFOUND THEN
//			     w_conge_montant := FALSE;
//			  END IF;
				cle = "99CG-MONT1";
				fnom = listeDonneesParametrage.get(cle);
				if(fnom == null || (fnom != null && fnom.getValm() == null))
					conge_montant = false;
				else
				{
					if(fnom.getValm() == 1)
					{
//						  IF wfnom.mnt1 = 1 THEN
//					     w_conge_montant := TRUE;
						conge_montant = true;
//					     --Les cong�s sont g�r�s en montant, on r�cup�re les rubriques qui vont bien
//					     -- Lecture de la rubrique du nombre de jours pris en table 99
//					     BEGIN
//					     SELECT valm INTO wfnom.mnt1
//					       FROM pafnom
//					       WHERE cdos = wpdos.cdos
//					       AND ctab = 99
//					       AND cacc = 'CG-MONT'
//					       AND nume = 2;
//					     EXCEPTION
//					        WHEN NO_DATA_FOUND THEN NULL;
//					     END;
		//
//					     IF SQL%NOTFOUND THEN
//					        w_mess := PA_PAIE.erreurp('INF-10116',w_clang);
//					        RETURN FALSE;
//					     ELSE
//					        w_rub_pris := ltrim(to_char(wfnom.mnt1,'0999'));
//					     END IF;
						
						cle = "99CG-MONT2";
						fnom = listeDonneesParametrage.get(cle);
						if (fnom != null && fnom.getValm() != null)
						{
							rub_pris = ClsStringUtil.formatNumber(fnom.getValm(), ParameterUtil.formatRubrique);
						}
						else
						{
							error = updateservice.errorMessage("INF-10116");
							globalUpdate._setInitializeError(request, error, true);
						}
//					     -- Lecture de la rubrique du nombre de jours acquis en table 99
//					     BEGIN
//					     SELECT valm INTO wfnom.mnt1
//					       FROM pafnom
//					       WHERE cdos = wpdos.cdos
//					       AND ctab = 99
//					       AND cacc = 'CG-MONT'
//					       AND nume = 3;
//					     EXCEPTION
//					        WHEN NO_DATA_FOUND THEN NULL;
//					     END;
		//
//					     IF SQL%NOTFOUND THEN
//					        w_mess := PA_PAIE.erreurp('INF-10116',w_clang);
//					        RETURN FALSE;
//					     ELSE
//					        w_rub_jours_acquis := ltrim(to_char(wfnom.mnt1,'0999'));
//					     END IF;
						cle = "99CG-MONT3";
						fnom = listeDonneesParametrage.get(cle);
						if (fnom != null && fnom.getValm() != null)
						{
							rub_jours_acquis = ClsStringUtil.formatNumber(fnom.getValm(), ParameterUtil.formatRubrique);
						}
						else
						{
							error = updateservice.errorMessage("INF-10116");
							globalUpdate._setInitializeError(request, error, true);
						}
//					     -- Lecture de la rubrique du montant  acquis en table 99
//					     BEGIN
//					     SELECT valm INTO wfnom.mnt1
//					       FROM pafnom
//					       WHERE cdos = wpdos.cdos
//					       AND ctab = 99
//					       AND cacc = 'CG-MONT'
//					       AND nume = 4;
//					     EXCEPTION
//					        WHEN NO_DATA_FOUND THEN NULL;
//					     END;
		//
//					     IF SQL%NOTFOUND THEN
//					        w_mess := PA_PAIE.erreurp('INF-10116',w_clang);
//					        RETURN FALSE;
//					     ELSE
//					        w_rub_montant_acquis := ltrim(to_char(wfnom.mnt1,'0999'));
//					     END IF;
						cle = "99CG-MONT4";
						fnom = listeDonneesParametrage.get(cle);
						if (fnom != null && fnom.getValm() != null)
						{
							rub_montant_acquis = ClsStringUtil.formatNumber(fnom.getValm(), ParameterUtil.formatRubrique);
						}
						else
						{
							error = updateservice.errorMessage("INF-10116");
							globalUpdate._setInitializeError(request, error, true);
						}
					}
//					  ELSE
//				     w_conge_montant := FALSE;
					else
						conge_montant = false;
				}
//			   ---------------------------------------------------------
//			   --FIN INIT pour calcul des cong�s en montant		  --
//			   ---------------------------------------------------------
				
			}

			// -- Connaitre si le salarie est un expatrie :
			// -- Libelle 5 = Valeur si c'est un expatrie
			// -- Montant 1 = 1 : Regime, 2 : Type de contrat, 3 : Classe salarie =>
			// -- Si le montant 1 vaut 1, alors regarder la colonne regime du salariè poss�de la valeur du libellè 5
			// -- Si le montant 1 vaut 2, regarder le type de contrat poss�de la valeur du libellè 5
			// -- Si le montant 1 vaut 3, regarder si la classe du salariè poss�de la valeur du libellè 5

			cle = "99EXPAT5";
			fnom = listeDonneesParametrage.get(cle);
			if (fnom != null && StringUtils.isNotBlank(fnom.getVall()))
			{
				val_exp = fnom.getVall();
			}
			else
			{
				error = updateservice.errorMessage("ERR-90021");
				globalUpdate._setInitializeError(request, error, true);	
			}
			
			cle = "99EXPAT1";
			fnom = listeDonneesParametrage.get(cle);
			if (fnom != null && fnom.getValm() != null)
			{	
				colonne_expatrie = fnom.getValm().intValue();
			}
			else
			{
				error = updateservice.errorMessage("ERR-90021");
				globalUpdate._setInitializeError(request, error, true);
			}

			// -- Lecture du parametre donnant le nbre de jours de conge par defaut
			
			// -- Calcul fictif effectue ou pas
			cle = "99FICTIF1";
			fnom = listeDonneesParametrage.get(cle);
			if (fnom != null && StringUtils.isNotBlank(fnom.getVall()))
			{
				fictif = StringUtils.substring(fnom.getVall(), 0, 1);
				if(!"N".equalsIgnoreCase(fictif) && !"O".equalsIgnoreCase(fictif))
				{
					error = updateservice.errorMessage("ERR-90015");
					globalUpdate._setInitializeError(request, error, true);	
				}
			}
			else
			{
				error = updateservice.errorMessage("ERR-90014");
				globalUpdate._setInitializeError(request, error, true);	
			}

			// -- Type de calcul fictif effectue
			if (StringUtils.equals("O" ,fictif))
			{
				cle = "99FICTIF2";
				fnom = listeDonneesParametrage.get(cle);
				if (fnom != null && StringUtils.isNotBlank(fnom.getVall()))
				{
					typ_fictif = StringUtils.substring(fnom.getVall(), 0, 1);
					if(!"A".equalsIgnoreCase(typ_fictif) && !"B".equalsIgnoreCase(typ_fictif) && !"C".equalsIgnoreCase(typ_fictif))
					{
						error = updateservice.errorMessage("ERR-90017");
						globalUpdate._setInitializeError(request, error, true);
					}
				}
				else
				{
					error = updateservice.errorMessage("ERR-90016",2);
					globalUpdate._setInitializeError(request, error, true);	
				}
			}
			
			// -- Nombre de jours de cong�s par d�faut pour les locaux et les expatri�s
			// -- Tau 1 = locaux, Tau 2 = expatrie
			cle = "99NBJC-DEF1";
			fnom = listeDonneesParametrage.get(cle);
			if (fnom != null && fnom.getValt() != null)
				nbjc_lx = fnom.getValt().doubleValue();
			else
				nbjc_lx = 2;
			
			cle = "99NBJC-DEF2";
			fnom = listeDonneesParametrage.get(cle);
			if (fnom != null && fnom.getValt() != null)
				nbjc_ex = fnom.getValt().doubleValue();
			else
				nbjc_ex = 5;

			// -- Verification si des bulletins existent pour ce dossier
			nbbul = updateservice.getNbreBulFromRhtcalcul(dossier, periode, numerobulletin, rub_nap);
			if (nbbul == 0)
			{
				error = updateservice.errorMessage("ERR-90101");
				globalUpdate._setInitializeError(request, error, true);	
			}

			// -- Verification si tous les bulletins ont ete edites
			nbbul_non_edite = updateservice.getNbreBulNonEditeFromRhtcalcul(dossier, periode, numerobulletin, rub_nap);
			if (nbbul_non_edite > 0)
			{
				error = updateservice.errorMessage("ERR-90102", editMatricule);
				globalUpdate._setInitializeError(request, error, true);
			}

			// -- ----- Lecture des rubriques generant un E.V. sur le mois suivant
			Object[] max = (Object[]) updateservice.chargerT_EV_MC(dossier).get(0);
			for (int i = 0; i < 16; i++)
			{
				t_ev_mc[i] = ClsStringUtil.formatNumber(max[i] instanceof BigDecimal ? (BigDecimal) max[i] : (Long) max[i], ParameterUtil.formatRubrique);
			}
			
			// -- ----- Lecture des rubriques generees en E.V. par le mois precedent
			max = (Object[]) updateservice.chargerT_EV_MS(dossier).get(0);
			for (int i = 0; i < 16; i++)
			{
				t_ev_ms[i] = ClsStringUtil.formatNumber(max[i] instanceof BigDecimal ? (BigDecimal) max[i] : (Long) max[i], ParameterUtil.formatRubrique);
				t_ev_mc_ms.put(t_ev_mc[i], t_ev_ms[i]);
			}

			// -- Lecture de la rubrique base conges table 99
			cle = "99BASE-CONGE1";
			fnom = listeDonneesParametrage.get(cle);
			if (fnom != null && fnom.getValm() != null)
			{
				rub_bc = ClsStringUtil.formatNumber(fnom.getValm(), ParameterUtil.formatRubrique);
				
				// -- Calcul automatique des jours suppl�mentaires de cong�s
				calcul_auto_nbjsup = StringUtils.substring(fnom.getVall(), 0, 1);		
				if(!"A".equalsIgnoreCase(calcul_auto_nbjsup) && !"O".equalsIgnoreCase(calcul_auto_nbjsup))
				{
					calcul_auto_nbjsup = "N";
				}
			}
			else
			{
				error = updateservice.errorMessage("ERR-90126");
				globalUpdate._setInitializeError(request, error, true);
			}
			
			// --Ajout du montant cong�s è la base des cong�s lors du retour de l'agent  O/N
			cle = "99BASE-CONGE2";
			fnom = listeDonneesParametrage.get(cle);
			if (fnom != null && StringUtils.isNotBlank(fnom.getVall()))
			{
				ajout_bc = StringUtils.substring(fnom.getVall(),0, 1);	
			}
			else
				ajout_bc="N";
			
			//	-- Prorata du nombre de jours de cong�s par mois   O/N
			cle = "99BASE-CONGE3";
			fnom = listeDonneesParametrage.get(cle);
			if (fnom != null && StringUtils.isNotBlank(fnom.getVall()))
			{
				pror_jr = StringUtils.substring(fnom.getVall(),0, 1);	
			}
			else
				pror_jr = "N";

			// -- Motif code cong�s pour les absences sur mois suivant, uniquement pour le calcul Fictif
			cle = "99BASE-CONGE4";
			fnom = listeDonneesParametrage.get(cle);
			if (fnom != null && StringUtils.isNotBlank(fnom.getVall()))
			{
				motif_cg_abs_ms = StringUtils.substring(fnom.getVall(),0, 2);
			}
			
			// -- RAZ base cong�s et jour lors du calcul du congè le premier mois
			cle = "99BASE-CONGE5";
			fnom = listeDonneesParametrage.get(cle);
			if (fnom != null && StringUtils.isNotBlank(fnom.getVall()))
				raz_bc_jour = ((String) fnom.getVall()).substring(0, 1);
			else
				raz_bc_jour ="N";
			
			cle = "99BASE-CONGE5";
			fnom = listeDonneesParametrage.get(cle);
			if (fnom != null && fnom.getValm() != null)
			{
				rub_cg_ant = ClsStringUtil.formatNumber(fnom.getValm(), ParameterUtil.formatRubrique);
			}
			cle = "99BASE-CONGE6";
			fnom = listeDonneesParametrage.get(cle);
			if (fnom != null && fnom.getValm() != null)
			{
				rub_cg_ex = ClsStringUtil.formatNumber(fnom.getValm(), ParameterUtil.formatRubrique);
			}
			
			// -- Test coherence FICTIF - BASE-CONGE.
			if (StringUtils.equals("O", fictif))
			{
				if (StringUtils.isBlank(motif_cg_abs_ms))
				{
					error = updateservice.errorMessage("ERR-90127");
					globalUpdate._setInitializeError(request, error, true);
				}
			}

			// -- Lecture de la rubrique du rub_brut table 99
			cle = "99RUBBRUT1";
			fnom = listeDonneesParametrage.get(cle);
			if (fnom != null && fnom.getValm() != null)
			{
				rub_brut = ClsStringUtil.formatNumber(fnom.getValm(), ParameterUtil.formatRubrique);
			}
			else
			{
				error = updateservice.errorMessage("ERR-90023");
				globalUpdate._setInitializeError(request, error, true);
			}
			
			// -- Lecture de l'age maxi des enfants donnant droit a des jrs supp

			Object[] o = (Object[]) updateservice.getEnfantsJoursSup(dossier).get(0);
			if (o != null)
			{
				if (o[0] != null && o[1] != null)
				{
					age_max_enfant = (o[0] instanceof BigDecimal ? (BigDecimal) o[0] : (Long) o[0]).intValue();
					minimum_enfant = (o[1] instanceof BigDecimal ? (BigDecimal) o[1] : (Long) o[1]).intValue();
					age_max_fiscal = (o[2] instanceof BigDecimal ? (BigDecimal) o[2] : (Long) o[2]).intValue();
					//
					if (age_max_enfant <= 0)
						comptage_enfant = false;
					else
						comptage_enfant = true;
				}
			}
			else
			{
				comptage_enfant = false;
			}
			
			// -- Lecture des parametres pour le prorata des jours de droit mois depart et retour
			cle = "99PROR-JCG1";
			fnom = listeDonneesParametrage.get(cle);
			if (fnom != null && StringUtils.isNotBlank(fnom.getVall()))
				pror_jcg_normal = ! StringUtils.equals("B" , StringUtils.substring(fnom.getVall(), 0, 1));
			else
				pror_jcg_normal = true;
			
			// -- Prorata basè sur un bar�me, recherche des bar�mes
			if (!pror_jcg_normal)
			{
				//	--bar�me 1
				cle = "99PRJ-BARCG1";
				fnom = listeDonneesParametrage.get(cle);
				if (fnom != null && StringUtils.isNotBlank(fnom.getVall()) && fnom.getValt() != null)
				{
					dbcg_barem1 = StringUtils.substring(fnom.getVall() , 0, 2);
					fincg_barem1 = StringUtils.substring(fnom.getVall(), 2, 4);
					nbjcg_barem1 = fnom.getValt().intValue();
					if (Integer.valueOf(dbcg_barem1) <= 0 || Integer.valueOf(fincg_barem1) > 31)
					{
						error = updateservice.errorMessage("ERR-10516", "PRJ-BARCG", "99", "1");
						globalUpdate._setInitializeError(request, error, true);	
					}
				}
				else
				{
					error = updateservice.errorMessage("ERR-30165", "PRJ-BARCG", "99", "1");
					globalUpdate._setInitializeError(request, error, true);
				}
				
				// --bar�me 2
				cle = "99PRJ-BARCG2";
				fnom = listeDonneesParametrage.get(cle);
				if (fnom != null && StringUtils.isNotBlank(fnom.getVall()) && fnom.getValt() != null)
				{
					dbcg_barem2 = StringUtils.substring(fnom.getVall() , 0, 2);
					fincg_barem2 = StringUtils.substring(fnom.getVall(), 2, 4);
					nbjcg_barem2 = fnom.getValt().intValue();
					if (Integer.valueOf(dbcg_barem2) <= 0 || Integer.valueOf(fincg_barem2) > 31)
					{
						error = updateservice.errorMessage("ERR-10516", "PRJ-BARCG", "99", "2");
						globalUpdate._setInitializeError(request, error, true);	
					}
				}
				else
				{
					error = updateservice.errorMessage("ERR-30165", "PRJ-BARCG", "99", "2");
					globalUpdate._setInitializeError(request, error, true);
				}
				
				// --bar�me 3
				cle = "99PRJ-BARCG3";
				fnom = listeDonneesParametrage.get(cle);
				if (fnom != null && StringUtils.isNotBlank(fnom.getVall()) && fnom.getValt() != null)
				{
					dbcg_barem3 = StringUtils.substring(fnom.getVall() , 0, 2);
					fincg_barem3 = StringUtils.substring(fnom.getVall(), 2, 4);
					nbjcg_barem3 = fnom.getValt().intValue();
					if (Integer.valueOf(dbcg_barem3) <= 0 || Integer.valueOf(fincg_barem3) > 31)
					{
						error = updateservice.errorMessage("ERR-10516", "PRJ-BARCG", "99", "3");
						globalUpdate._setInitializeError(request, error, true);	
					}
				}
				else
				{
					error = updateservice.errorMessage("ERR-30165", "PRJ-BARCG", "99", "3");
					globalUpdate._setInitializeError(request, error, true);
				}
			}
			//	-- Determinination de la colonne qui permet de calculer la date d'anniv du salari�
			//	-- Si Montant 1 vaut 1, alors regarder la date d'entr�e dans la soci�t�
			//	-- Si Montant 1 vaut 2, alors regarder la date de depart de calcul d'anciennet�
			//	-- Si Montant 1 vaut 3, alors regarder la date de retour du dernier cong�
			//  -- Si Montant 1 vaut 4, alors regarder si le mois (mois sans ann�e) de paie est egal au montant 2
			cle = "99ANNIV-CG1";
			fnom = listeDonneesParametrage.get(cle);
			if (fnom != null && fnom.getValm() != null)
			{
				type_dtanniv = fnom.getValm().intValue();
				if(NumberUtils.notIn(type_dtanniv, 1,2,3,4))
				{
					error = updateservice.errorMessage("ERR-10517", "ANNIV-CG", "99", "1");
					globalUpdate._setInitializeError(request, error, true);	
				}
			}
			else
			{
				type_dtanniv = 1;
			}

			// -- Ctle de l'existence et chargement de la table 91
			tab91b = updateservice.countNbColonnesT91(dossier) > 0;
			if(tab91b)
			{
				cle = "91HC1";
				fnom = listeDonneesParametrage.get(cle);
				t91hc = fnom != null;
				
				
				cle = "91HP1";
				fnom = listeDonneesParametrage.get(cle);
				t91hp = fnom != null;
				
				cle = "91MC1";
				fnom = listeDonneesParametrage.get(cle);
				t91mc = fnom != null;
				
				cle = "91MP1";
				fnom = listeDonneesParametrage.get(cle);
				t91mp = fnom != null;
				
				cle = "91IN1";
				fnom = listeDonneesParametrage.get(cle);
				t91in = fnom != null;
				
				
				List<Object[]> periodes91 = updateservice.getPeriodesT91(dossier, dernierePeriodeCloturee);
				Object[] nomenc = null;
				listePeriodesT91 = new ArrayList<ClsInfoPeriode>();
				ClsInfoPeriode periodeinfo = null;
				for (Object obj1 : periodes91)
				{
					try
					{
						periodeinfo = new ClsInfoPeriode();
						nomenc = (Object[]) obj1;

						periodeinfo.periode = nomenc[0].toString();
						periodeinfo.ddebut = DateUtils.parseDate((String) nomenc[1], datePatterns);
						periodeinfo.dfin = DateUtils.parseDate((String) nomenc[2], datePatterns);
						listePeriodesT91.add(periodeinfo);
					}
					catch (ParseException e)
					{
						e.printStackTrace();
						error = ClsTreater._getStackTrace(e);
						globalUpdate._setInitializeError(request, error, true);	
						continue;
					}
				}
			}
			
			//	-- Chargement des rubriques de regularisation
			List<ElementSalaire> rubriquesRegularisation = updateservice.getRubriquesRegularisation(dossier);
			mapRubriqueRegularisation = new HashMap<String, String>();
			for (ElementSalaire rubrique : rubriquesRegularisation)
			{
				mapRubriqueRegularisation.put(rubrique.getRcon() , rubrique.getCrub());
			}
			return true;
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			error = ClsTreater._getStackTrace(e);
			globalUpdate._setInitializeError(request, error, true);	
			return false;
		}
	}
	
	
	public boolean verificationParametrage()
	{
		//ici, on ne fait plus qu'afficher les messages d'erreurs qu'on aura constituè 
		//au fur et è mesur dans l'initialisation
//		ClsResultat resultatInitialisation = globalUpdate._printInitializeError(request);
//		if(resultatInitialisation != null)
//			return ! resultatInitialisation.isErrormessage();
		
		return true;	
	}
	
	protected ElementSalaire _getRubrique(String cdos, String crub)
	{
		ElementSalaire rubrique = rubriquesMap.get(crub);
		if (rubrique == null)
		{
			List result = service.find("FROM ElementSalaire WHERE idEntreprise="+dossier+" AND crub='"+crub+"'");

			if(result!= null && result.size()>0) rubrique = (ElementSalaire)result.get(0);
			if (rubrique != null)
				rubriquesMap.put(crub, rubrique);
		}
		return rubrique;
	}

	protected ParamData _getRhfnom(String dossier, Integer ctab, String cacc, Integer nume)
	{
		String cle = String.valueOf(ctab) + "-" + cacc + "-" + String.valueOf(nume);
		ParamData fnom = RhfnomsMap.get(cle);
		if (fnom == null)
		{
			fnom = service.findAnyColumnFromNomenclature(dossier, "", ctab.intValue()+"", cacc, nume.intValue()+"");
			if (fnom != null)
				RhfnomsMap.put(cle, fnom);
		}
		return fnom;
	}
	
	protected RhfnomMotifConge _getRhfnomMotifConges(String cacc)
	{

		RhfnomMotifConge fnom = RhfnomsMotifsCongesMap.get(cacc);
		if (fnom == null)
		{
			Object[] o = updateservice.getRhfnomMotifConges(dossier, cacc);
			if (o != null)
			{
				fnom = new RhfnomMotifConge();
				if(o[0] != null) fnom.montant1 = (o[0] instanceof BigDecimal ? (BigDecimal) o[0] : (Long) o[0]).intValue();
				if(o[1] != null) fnom.montant3 = (o[1] instanceof BigDecimal ? (BigDecimal) o[1] : (Long) o[1]).intValue();
				RhfnomsMotifsCongesMap.put(cacc, fnom);
			}
		}
		
		return fnom;
	}
	
	protected RhfnomCalcPartFiscal getRhfnomCalcPartFiscal(String cacc)
	{

		RhfnomCalcPartFiscal fnom = RhfnomsCalcPartFiscalMap.get(cacc);
		if (fnom == null)
		{
			Object[] maxdec = updateservice.getRhfnomCalcPartFiscal(dossier, cacc);
			if(maxdec != null)
			{
				fnom = new RhfnomCalcPartFiscal();
				if(maxdec[0] != null) fnom.montant3 = ((Long) maxdec[0]).intValue();
				if(maxdec[1] != null) fnom.taux1 = maxdec[1] instanceof BigDecimal ? ((BigDecimal) maxdec[1]).intValue() : ((Double) maxdec[1]).intValue();
				if(maxdec[2] != null) fnom.taux2 = maxdec[2] instanceof BigDecimal ? ((BigDecimal) maxdec[2]).intValue() : ((Double) maxdec[2]).intValue();
					
				RhfnomsCalcPartFiscalMap.put(cacc, fnom);
			}
		}
		
		return fnom;
	}
	
	protected RhfnomNbParts getRhfnomNbParts(String cacc)
	{

		RhfnomNbParts fnom = RhfnomsNbPartsMap.get(cacc);
		if (fnom == null)
		{
			fnom = new RhfnomNbParts();
			
			Object[] maxdec = updateservice.getRhfnomNbParts(dossier, cacc);
			if(maxdec != null)
			{
				if(maxdec[0] != null) fnom.taux1 = maxdec[0] instanceof BigDecimal ? ((BigDecimal) maxdec[0]).doubleValue() : ((Double) maxdec[0]).doubleValue();
				if(maxdec[1] != null) fnom.taux2 = maxdec[1] instanceof BigDecimal ? ((BigDecimal) maxdec[1]).doubleValue() : ((Double) maxdec[1]).doubleValue();
				if(maxdec[2] != null) fnom.taux3 = maxdec[2] instanceof BigDecimal ? ((BigDecimal) maxdec[2]).doubleValue() : ((Double) maxdec[2]).doubleValue();
				if(maxdec[3] != null) fnom.taux4 = maxdec[3] instanceof BigDecimal ? ((BigDecimal) maxdec[3]).doubleValue() : ((Double) maxdec[3]).doubleValue();
				if(maxdec[4] != null) fnom.taux5 = maxdec[4] instanceof BigDecimal ? ((BigDecimal) maxdec[4]).doubleValue() : ((Double) maxdec[4]).doubleValue();
				if(maxdec[5] != null) fnom.taux6 = maxdec[5] instanceof BigDecimal ? ((BigDecimal) maxdec[5]).doubleValue() : ((Double) maxdec[5]).doubleValue();
				if(maxdec[6] != null) fnom.taux7 = maxdec[6] instanceof BigDecimal ? ((BigDecimal) maxdec[6]).doubleValue() : ((Double) maxdec[6]).doubleValue();
				if(maxdec[7] != null) fnom.taux8 = maxdec[7] instanceof BigDecimal ? ((BigDecimal) maxdec[7]).doubleValue() : ((Double) maxdec[7]).doubleValue();
				
				RhfnomsNbPartsMap.put(cacc, fnom);
			}
		}
		
		return fnom;
	}

	//
	public void executeTest(Session oSession)
	{
		ParameterUtil.println("---------------------------------------------------------------------------");
		ParameterUtil.println("-----PARAMETRES D'INITIALISATION: ");
		ParameterUtil.println("---------------------------------------------------------------------------");
		if(this.genfile == 'O')  this.UpdateToString();

		threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);

		listeSalaries = updateservice.getListeSalarieFromSalarie(dossier,dateformat);
		if (listeSalaries.isEmpty())
			this.latch = new CountDownLatch(0);
		else
			this.latch = new CountDownLatch(2);
		Salarie agent = null;
		ClsAgentUpdateTask task = null;
		
		Integer nbTotal = 2;
		
		for (int k = 1; k <= 2; k++)
		{
			agent = (Salarie) listeSalaries.get(k - 1);
			task = new ClsAgentUpdateTask(this, oSession, agent);
			task.numsalariecourant = k;
			task.nbsalarietotal = nbTotal;
			
			startTask(task);
		}
		//
		try
		{
			latch.await(); // wait until all the tasks have been terminated
			if (!finalResult)
			{
				cas = "E";
			}
			else
			{
				cas = "F";
				error = "TRAITEMENT TERMINE AVEC SUCCES...";
			}
		}
		catch (InterruptedException iex)
		{
			iex.printStackTrace();
		}
	}
	public void execute(Session oSession)
	{
		if(this.genfile == 'O')  this.UpdateToString();
		
		allTasks = new ArrayList<FutureTalker>();

		threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
		//threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(this.parameter.getThreadmax());
		listeSalaries = updateservice.getListeSalarieFromSalarie(dossier,dateformat);
		
		if (listeSalaries.isEmpty())
			this.latch = new CountDownLatch(0);
		else
			this.latch = new CountDownLatch(listeSalaries.size());
		Salarie agent = null;
		ClsAgentUpdateTask task = null;
		
		Integer nbTotal = listeSalaries.size();
		
		
//		if(ui != null)
//			ui.debutAffichageProgressBar(nbTotal);
		try
		{
			for (int k = 1; k <= listeSalaries.size(); k++)
			{
				agent = (Salarie) listeSalaries.get(k - 1);
				this.numsalariecourant = k;
				this.nbsalarietotal = nbTotal;
				task = new ClsAgentUpdateTask(this, oSession, agent);
				//on arr�te tout si l'utilisateur decide d'arreter tous les threads
//				if(StringUtils.equals("O", ParameterUtil.getSessionObject(request, ClsSessionObjectName.SESSION_STOP_ALL_THREADS)))
//				{
//					threadPool.shutdownNow();
//					break;
//				}
				
				allTasks.add(startTask(task));
			}
			threadPool.shutdown();
		}
		catch (Exception iex)
		{
			iex.printStackTrace();
			error = ClsTreater._getStackTrace(iex);
			globalUpdate._setEvolutionTraitement(request, error, true);
		}
		//
		try
		{
			latch.await(); // wait until all the tasks have been terminated
			if (!finalResult)
			{
				cas = "E";
				ParameterUtil.println("<--------->"+finalResult);
				//globalUpdate._setEvolutionTraitement(request, error, false);	
			}
			else
			{
				cas = "F";
				error = "TRAITEMENT TERMINE AVEC SUCCES...";
				error = updateservice.errorMessage("INF-10077", new ClsDate(this.periode,"yyyyMM").getDateS("MM/yyyy"));
				globalUpdate._setEvolutionTraitement(request, error, false);	
			}
		}
		catch (InterruptedException iex)
		{
			iex.printStackTrace();
		}
	}

	private FutureTalker<Boolean> startTask(ClsAgentUpdateTask task)
	{
//		if(StringUtils.equals("O", ParameterUtil.getSessionObject(request, ClsSessionObjectName.SESSION_STOP_ALL_THREADS)))
//		{
//			///threadPool.shutdownNow();
//			try
//			{
//				this.cancelAllThread();
//			}
//			catch (Exception e)
//			{
//				// TODO: handle exception
//			};
//			return new FutureTalker<Boolean>(null);
//		}
		
		
		FutureTalker<Boolean> futureTalker = null;
		synchronized (latch)
		{
			long count = latch.getCount();
			if (count <= 0)
			{
				return null;
			}
			futureTalker = new FutureTalker<Boolean>(task);
			futureTalker.addListener(this);
			// start another one
			try
			{
				threadPool.execute(futureTalker);
			}
			catch (RejectedExecutionException rex)
			{
				futureTalker.rejected(rex); // failed to execute set task to error and notifies listeners
				rex.printStackTrace();
			}
			return futureTalker;
		}
	}
	
	

	// Listener methods
	/**
	 * Called on normal completion A call to this method implies the task's run() method has exited.
	 */
	public void futureResult(Boolean result, FutureTalker<Boolean> talker)
	{
//		if(StringUtils.equals("O", ParameterUtil.getSessionObject(request, ClsSessionObjectName.SESSION_STOP_ALL_THREADS)))
//		{
//			//threadPool.shutdownNow();
//			try
//			{
//				this.cancelAllThread();
//			}
//			catch (Exception e)
//			{
//				// TODO: handle exception
//			}
//			return;
//		}
		
		ClsAgentUpdateTask task = (ClsAgentUpdateTask) talker.getCallable();
		// startTask();
		
		synchronized (latch)
		{
			latch.countDown();
			int numsalarieCourant = this.nbsalarietotal - Long.valueOf(latch.getCount()).intValue();
			this.numsalariecourant = numsalarieCourant;
			if (!result)
			{// si le result=false alors arr�ter tout!
				finalResult = result;
//				ParameterUtil.println("Le traitement de MAJ du matricule " + task.getAgent().getNmat() + "=>"
//						+ task.getAgent().getNom() + "  s'est d�roulè en renvoyant false. ");
				System.out.println("Le traitement de MAJ du matricule " + task.getAgent().getNmat() + "=>"
						+ task.getAgent().getNom() + "  s'est d�roulè en renvoyant false. ");
				try
				{
					this.cancelAllThread();
				}
				catch (Exception e)
				{
					// TODO: handle exception
				}
			}
			else
			{
				if ((numsalarieCourant == this.nbsalarietotal) || (numsalarieCourant < 0))
				{
					globalUpdate._setEvolutionTraitement(request, "Mise è jour termin�e avec succ�s", false,numsalarieCourant, this.nbsalarietotal);
					globalUpdate._setTitreEvolutionTraitement(request, "");
//					if(ui != null)
//						ui.mettreAJourProgressBar(numsalarieCourant);
				}
				else
				{
//					if(ui != null)
//						ui.mettreAJourProgressBar(numsalarieCourant);
					
					globalUpdate._setEvolutionTraitement(request, "Mise è jour du salariè " + task.getAgent().getNmat() + " : "
							+ task.getAgent().getNom() + " " + task.getAgent().getPren() + " (" + numsalarieCourant + "/" + this.nbsalarietotal + ")", false,
							numsalarieCourant, this.nbsalarietotal);
					task = (ClsAgentUpdateTask) ObjectUtils.setNull(task);
				}
				ParameterUtil.println("Le traitement de MAJ du matricule " + task.getAgent().getNmat() + "=>"
						+ task.getAgent().getNom() + "  s'est bien d�roul�. ");
			}
		}
	}

	/**
	 * Called if cancelled Note: You need to provide a rejecthandler and override ShutDownNow if you want this to be called on rejection and shutdown. A call to
	 * this method does NOT imply the task's call() method ever been called.
	 */
	public void futureCancelled(CancellationException t, FutureTalker<Boolean> talker)
	{
		finalResult = false;
		ClsAgentUpdateTask task = (ClsAgentUpdateTask) talker.getCallable();
//		ParameterUtil.println("Le traitement de MAJ du matricule " + task.getAgent().getNmat() + "=>"
//				+ task.getAgent().getNom() + "  s'est mal d�roul�. " + t.getClass().getName() + "  Message:" + t.getMessage());
		System.out.println("Le traitement de MAJ du matricule " + task.getAgent().getNmat() + "=>"
				+ task.getAgent().getNom() + "  s'est mal d�roul�. " + t.getClass().getName() + "  Message:" + t.getMessage());
		//t.printStackTrace();
		
		task = (ClsAgentUpdateTask) ObjectUtils.setNull(task);
		
		latch.countDown();
	}

	/**
	 * Called on error and passed the throwable that was wrapped in ExecutionException A call to this method implies the task's run() method has exited.
	 */
	public void futureError(Throwable t, FutureTalker<Boolean> talker)
	{
		finalResult = false;
		ClsAgentUpdateTask task = (ClsAgentUpdateTask) talker.getCallable();
//		ParameterUtil.println("Le traitement de MAJ du matricule " + task.getAgent().getNmat() + "=>"
//				+ task.getAgent().getNom() + "  a �tè stoppè de suite d'une erreur. " + t.getClass().getName() + "  Message:" + t.getMessage());
		System.out.println("Le traitement de MAJ du matricule " + task.getAgent().getNmat() + "=>"
				+ task.getAgent().getNom() + "  a �tè stoppè de suite d'une erreur. " + t.getClass().getName() + "  Message:" + t.getMessage());
		t.printStackTrace();
		task = (ClsAgentUpdateTask) ObjectUtils.setNull(task);
		latch.countDown();
		try
		{
			this.cancelAllThread();
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
	}
	
	public void cancelAllThread()   throws Exception
	{
		ClsResultat oResult = null;//globalUpdate._getEvolutionTraitement();
		if (oResult != null && oResult.isErrormessage())
		{
			
		}
		else
		{
			error = updateservice.errorMessage("ERR-90044");
			globalUpdate._setEvolutionTraitement(request, error, true);
		}
		
			if (allTasks != null)
			{
				for (int i = 0; i < 100; i++)
				{
					for (FutureTalker task : allTasks)
					{
						if(! task.isDone())
							task.cancel(true);
						
							task = null;
						
					}
				}
			}
		
		
	}
	
	// ---------------------------------------------------------------------------------
	// -- Maj du dossier de paie
	// ---------------------------------------------------------------------------------
	public boolean updateDossierDePaie(Session oSession)
	{
		try
		{
			String mois = new ClsDate(rhpdo.getDfex()).getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
			ClsDate ddex = new ClsDate(rhpdo.getDdex());
			ClsDate dfex = new ClsDate(rhpdo.getDfex());
			int andeb = ddex.getYear() + 1;
			int anfin = dfex.getYear() + 1;

			Date dtdeb = null;
			Date dtfin = null;
			boolean dern = false;
			try
			{
				dern = bulletin.dernierePeriode(dossier, periode, numerobulletin);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return false;
			}
			if (periode.equals(mois) && dern)
			{
				dtdeb = new ClsDate(ClsStringUtil.formatNumber(ddex.getDay(), "00") + ClsStringUtil.formatNumber(ddex.getMonth(), "00")
						+ ClsStringUtil.formatNumber(andeb, "0000"), "ddMMyyyy").getDate();
				dtfin = new ClsDate(ClsStringUtil.formatNumber(dfex.getDay(), "00") + ClsStringUtil.formatNumber(dfex.getMonth(), "00")
						+ ClsStringUtil.formatNumber(anfin, "0000"), "ddMMyyyy").getDate();
			}
			else
			{
				dtdeb = rhpdo.getDdex();
				dtfin = rhpdo.getDfex();
			}

			Date dmp = null;
			@SuppressWarnings("unused")
			int dnbu = 9;
			if (dern)
			{
				dmp = myPeriode.getDate();
				rhpdo.setDnbu(9);
				dnbu = 9;
			}
			else
			{
				dmp = rhpdo.getDdmp();
				rhpdo.setDnbu(numerobulletin);
				dnbu = numerobulletin;
			}

			rhpdo.setDdmp(dmp);
			rhpdo.setDdex(dtdeb);
			rhpdo.setDfex(dtfin);

			oSession.update(rhpdo);
			
			updateservice.updateDossier(oSession, dossier, periode, numerobulletin);
			

		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean rcon_reg(String crub)
	{
		String value = mapRubriqueRegularisation.get(crub);
		if (value == null)
			return false;
		return true;
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

	public String getUser()
	{
		return user;
	}

	public void setUser(String user)
	{
		this.user = user;
	}

	public String getDossier()
	{
		return dossier;
	}

	public void setDossier(String dossier)
	{
		this.dossier = dossier;
	}

	public String getPeriode()
	{
		return periode;
	}

	public void setPeriode(String periode)
	{
		this.periode = periode;
	}

	public String getLangue()
	{
		return langue;
	}

	public void setLangue(String langue)
	{
		this.langue = langue;
	}
	
	public class RhfnomMotifConge
	{
		public Integer montant1 = 0;
		public Integer montant3 = 0;
		public RhfnomMotifConge()
		{
			
		}	
	}
	
	public class RhfnomCalcPartFiscal
	{
		public double taux1 = 0;
		public double taux2 = 0;
		public Integer montant3 = 0;
		public RhfnomCalcPartFiscal()
		{
			
		}	
	}
	
	public class RhfnomNbParts
	{
		public double taux1 = 0;
		public double taux2 = 0;
		public double taux3 = 0;
		public double taux4 = 0;
		public double taux5 = 0;
		public double taux6 = 0;
		public double taux7 = 0;
		public double taux8 = 0;
		
		public RhfnomNbParts()
		{
			
		}
	}
	
	
	public void printOutSalarie(String matricule, String texte)
	{
		String filename="salarie"+matricule+".sql";
		if(this.genfile == 'O')
		{
			//StringUtils.printOutObject(texte, this.genfilefolder+"\\cloture\\"+filename);
		}
		
	}
	
	public void UpdateToString()
	{
		String texte = null;//StringUtils.toString(this);
		
		String filename="parametrage.sql";
		
		if(this.genfile == 'O')
		{
			//StringUtils.printOutObject(texte, this.genfilefolder+"\\cloture\\"+filename);
		}
	}
	
/*
	public void UpdateToString()
	{
		ParameterUtil.println("String dossier = " + dossier);
		ParameterUtil.println("int numerobulletin = " + numerobulletin);
		ParameterUtil.println("String periode = " + periode);
		ParameterUtil.println("String user = " + user);
		ParameterUtil.println("String langue = " + langue);
		ParameterUtil.println("ClsBulletin bulletin = " + bulletin.toString());
		ParameterUtil.println("ClsParameterOfPay parameter = " + parameter.toString());
		ParameterUtil.println("ClsDate myPeriode = " + myPeriode == null ? null : myPeriode.getDateS());
		ParameterUtil.println("String rubriqueNbreJourTravail = " + rubriqueNbreJourTravail);
		ParameterUtil.println("String rubriqueNbreJourPlage = " + rubriqueNbreJourPlage);
		ParameterUtil.println("boolean tab91b = " + tab91b);
		ParameterUtil.println("String dernierePeriodeCloturee = " + dernierePeriodeCloturee);
		ParameterUtil.println("String mois_ms = " + mois_ms);
		ParameterUtil.println("int nbul_ms = " + nbul_ms);
		ParameterUtil.println("String am99 = " + am99);
		ParameterUtil.println("boolean calc_part_auto = " + calc_part_auto);
		ParameterUtil.println("int max_part_fisc = " + max_part_fisc);
		ParameterUtil.println("String motif_reliq = " + motif_reliq);
		ParameterUtil.println("String rub_pnp = " + rub_pnp);
		ParameterUtil.println("String val_exp = " + val_exp);
		ParameterUtil.println("int colonne_expatrie = " + colonne_expatrie);
		ParameterUtil.println("String fictif = " + fictif);
		ParameterUtil.println("String typ_fictif = " + typ_fictif);
		ParameterUtil.println("int nbjc_lx = " + nbjc_lx);
		ParameterUtil.println("int nbjc_ex = " + nbjc_ex);
		ParameterUtil.println("int nbbul = " + nbbul);
		ParameterUtil.println("String editMatricule = " + editMatricule);
		ParameterUtil.println("String[]t_ev_mc = " + t_ev_mc[0] + "-" + t_ev_mc[1] + "-" + t_ev_mc[2] + "-" + t_ev_mc[3] + "-" + t_ev_mc[4] + "-" + t_ev_mc[5]
				+ "-" + t_ev_mc[6]);
		ParameterUtil.println("String[]t_ev_ms = " + t_ev_ms[0] + "-" + t_ev_ms[1] + "-" + t_ev_ms[2] + "-" + t_ev_ms[3] + "-" + t_ev_ms[4] + "-" + t_ev_ms[5]
				+ "-" + t_ev_ms[6]);
		ParameterUtil.println("String rub_bc = " + rub_bc);
		ParameterUtil.println("String calcul_auto_nbjsup = " + calcul_auto_nbjsup);
		ParameterUtil.println("String ajout_bc = " + ajout_bc);
		ParameterUtil.println("String pror_jr = " + pror_jr);
		ParameterUtil.println("String motif_cg_abs_ms = " + motif_cg_abs_ms);
		ParameterUtil.println("String raz_bc_jour = " + raz_bc_jour);
		ParameterUtil.println("String rub_brut = " + rub_brut);
		ParameterUtil.println("boolean pror_jcg_normal = " + pror_jcg_normal);
		ParameterUtil.println("int age_max_enfant = " + age_max_enfant);
		ParameterUtil.println("boolean comptage_enfant = " + comptage_enfant);
		ParameterUtil.println("String dbcg_barem1 = " + dbcg_barem1);
		ParameterUtil.println("String fincg_barem1 = " + fincg_barem1);
		ParameterUtil.println("int nbjcg_barem1 = " + nbjcg_barem1);
		ParameterUtil.println("String dbcg_barem2 = " + dbcg_barem2);
		ParameterUtil.println("String fincg_barem2 = " + fincg_barem2);
		ParameterUtil.println("int nbjcg_barem2 = " + nbjcg_barem2);
		ParameterUtil.println("String dbcg_barem3 = " + dbcg_barem3);
		ParameterUtil.println("String fincg_barem3 = " + fincg_barem3);
		ParameterUtil.println("int nbjcg_barem3 = " + nbjcg_barem3);
		ParameterUtil.println("int type_dtanniv = " + type_dtanniv);
		ParameterUtil.println("boolean t91hc = " + t91hc);
		ParameterUtil.println("boolean t91hp = " + t91hp);
		ParameterUtil.println("boolean t91mc =  " + t91mc);
		ParameterUtil.println("boolean t91mp = " + t91mp);
		ParameterUtil.println("boolean t91in = " + t91mp);
		ParameterUtil.println("String prochainmois = " + prochainmois);
		ParameterUtil.println("String cas = " + cas);
		ParameterUtil.println("Date dtDdmp = " + dtDdmp);
		ParameterUtil.println("Date dtDdex = " + dtDdex);
		ParameterUtil.println("Rhpdo rhpdo = " + rhpdo);
		ParameterUtil.println("boolean ca = " + ca);
		ParameterUtil.println("int minimum_enfant = " + minimum_enfant);
		ParameterUtil.println("int age_max_fiscal = " + age_max_fiscal);
		ParameterUtil.println("String rub_nap = " + rub_nap);
		ParameterUtil.println("String dateformat = " + dateformat);

	}	 
*/
	
	//******************************** LISTE DES GETTERS POUR AVOIR LES VALEURS DES PROPRIETES DE L'OBJECT****************************
	public String getDateformat()
	{
		return dateformat;
	}

	public int getNumerobulletin()
	{
		return numerobulletin;
	}

	public ClsBulletin getBulletin()
	{
		return bulletin;
	}

	public ClsDate getMyPeriode()
	{
		return myPeriode;
	}

	public String getRubriqueNbreJourTravail()
	{
		return rubriqueNbreJourTravail;
	}

	public String getRubriqueNbreJourPlage()
	{
		return rubriqueNbreJourPlage;
	}

	public boolean getTab91b()
	{
		return tab91b;
	}

	public String getDernierePeriodeCloturee()
	{
		return dernierePeriodeCloturee;
	}

	public String getMois_ms()
	{
		return mois_ms;
	}

	public Date getDate_debut_mois_ms()
	{
		return date_debut_mois_ms;
	}

	public Date getDate_fin_mois_ms()
	{
		return date_fin_mois_ms;
	}

	public int getNbul_ms()
	{
		return nbul_ms;
	}

	public List<ClsInfoPeriode> getListePeriodesT91()
	{
		return listePeriodesT91;
	}

	public Map<String, String> getMapRubriqueRegularisation()
	{
		return mapRubriqueRegularisation;
	}

	public String getAm99()
	{
		return am99;
	}

	public boolean getCalc_part_auto()
	{
		return calc_part_auto;
	}

	public int getMax_part_fisc()
	{
		return max_part_fisc;
	}

	public String getMotif_reliq()
	{
		return motif_reliq;
	}

	public String getRub_pnp()
	{
		return rub_pnp;
	}

	public String getVal_exp()
	{
		return val_exp;
	}

	public int getColonne_expatrie()
	{
		return colonne_expatrie;
	}

	public String getFictif()
	{
		return fictif;
	}

	public String getTyp_fictif()
	{
		return typ_fictif;
	}

	public double getNbjc_lx()
	{
		return nbjc_lx;
	}

	public double getNbjc_ex()
	{
		return nbjc_ex;
	}

	public int getNbbul()
	{
		return nbbul;
	}

	public int getNbbul_non_edite()
	{
		return nbbul_non_edite;
	}

	public String getEditMatricule()
	{
		return editMatricule;
	}

	public String[] getT_ev_mc()
	{
		return t_ev_mc;
	}

	public String[] getT_ev_ms()
	{
		return t_ev_ms;
	}

	public Map<String, String> getT_ev_mc_ms()
	{
		return t_ev_mc_ms;
	}

	public String getRub_bc()
	{
		return rub_bc;
	}

	public String getCalcul_auto_nbjsup()
	{
		return calcul_auto_nbjsup;
	}

	public String getAjout_bc()
	{
		return ajout_bc;
	}

	public String getPror_jr()
	{
		return pror_jr;
	}

	public String getMotif_cg_abs_ms()
	{
		return motif_cg_abs_ms;
	}

	public String getRaz_bc_jour()
	{
		return raz_bc_jour;
	}

	public String getRub_brut()
	{
		return rub_brut;
	}

	public boolean getPror_jcg_normal()
	{
		return pror_jcg_normal;
	}

	public int getAge_max_enfant()
	{
		return age_max_enfant;
	}

	public boolean getComptage_enfant()
	{
		return comptage_enfant;
	}

	public String getDbcg_barem1()
	{
		return dbcg_barem1;
	}

	public String getFincg_barem1()
	{
		return fincg_barem1;
	}

	public int getNbjcg_barem1()
	{
		return nbjcg_barem1;
	}

	public String getDbcg_barem2()
	{
		return dbcg_barem2;
	}

	public String getFincg_barem2()
	{
		return fincg_barem2;
	}

	public int getNbjcg_barem2()
	{
		return nbjcg_barem2;
	}

	public String getDbcg_barem3()
	{
		return dbcg_barem3;
	}

	public String getFincg_barem3()
	{
		return fincg_barem3;
	}

	public int getNbjcg_barem3()
	{
		return nbjcg_barem3;
	}

	public int getType_dtanniv()
	{
		return type_dtanniv;
	}

	public boolean getT91hc()
	{
		return t91hc;
	}

	public boolean getT91hp()
	{
		return t91hp;
	}

	public boolean getT91mc()
	{
		return t91mc;
	}

	public boolean getT91mp()
	{
		return t91mp;
	}

	public boolean getT91in()
	{
		return t91in;
	}

	public String getProchainmois()
	{
		return prochainmois;
	}

	public Date getDate_debut_prochainmois()
	{
		return date_debut_prochainmois;
	}

	public Date getDate_fin_prochainmois()
	{
		return date_fin_prochainmois;
	}

	public String getCas()
	{
		return cas;
	}

	public String getError()
	{
		return error;
	}

	public Date getDtDdmp()
	{
		return dtDdmp;
	}

	public Date getDtDdex()
	{
		return dtDdex;
	}

	public DossierPaie getRhpdo()
	{
		return rhpdo;
	}

	public boolean getCa()
	{
		return ca;
	}

	public int getMinimum_enfant()
	{
		return minimum_enfant;
	}

	public int getAge_max_fiscal()
	{
		return age_max_fiscal;
	}

	public String getRub_nap()
	{
		return rub_nap;
	}


	public Map<String, ElementSalaire> getRubriquesMap()
	{
		return rubriquesMap;
	}

	public Map<String, ParamData> getRhfnomsMap()
	{
		return RhfnomsMap;
	}

	public Map<String, RhfnomMotifConge> getRhfnomsMotifsCongesMap()
	{
		return RhfnomsMotifsCongesMap;
	}

	public Map<String, RhfnomCalcPartFiscal> getRhfnomsCalcPartFiscalMap()
	{
		return RhfnomsCalcPartFiscalMap;
	}

	public Map<String, RhfnomNbParts> getRhfnomsNbPartsMap()
	{
		return RhfnomsNbPartsMap;
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
	
	

}
