package com.kinart.paie.business.services.calcul;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

import javax.servlet.http.HttpSession;

import com.kinart.paie.business.model.Salarie;
import com.kinart.paie.business.model.SuspensionPaie;
import com.kinart.paie.business.services.impl.CalculPaieServiceImpl;
import com.kinart.paie.business.services.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * Le but de cette classe est de lancer le calcul de la paie sur chaque salari�.
 * 
 * @author c.mbassi
 */
public class ClsSalariesEngine
{
	private int threadmax = 5;
	
	public boolean synchronize_traiter_salaire = true;

	ExecutorService poolSrev = null;

	private GeneriqueConnexionService service = null;

	// contenant les param�tres de fonctionnement du moteur de paie
	private ClsParameterOfPay parameterOfSalary = null;

	// liste des salari�s � traiter
	private List<ClsSalaryToProcess> listOfSalaryToProcess = null;

	public List<Future> allTask = null;

	// liste des rubriques de paie
	private List listOfParubq = null;

	public HttpSession httpSession;

	//public UICalculBulletin ui;

	public CalculPaieServiceImpl lanceur;

	public Integer nbrSalaries = 0;

	public Integer nbrSalariesTraite = 0;

	private ClsFictifParameterOfPay fictiveParameterOfPay = null;

	public synchronized ExecutorService getPoolSrev()
	{
		return poolSrev;
	}

	// public synchronized int getNombreThread() {
	// return nombreThread;
	// }
	// public synchronized void setNombreThread(int nombreThread) {
	// this.nombreThread = nombreThread;
	// }
	public int getThreadmax()
	{
		return threadmax;
	}

	public void setThreadmax(int threadmax)
	{
		this.threadmax = threadmax;
	}

	public List getListOfParubq()
	{
		return listOfParubq;
	}

	public void setListOfParubq(List listOfParubq)
	{
		this.listOfParubq = listOfParubq;
	}

	public GeneriqueConnexionService getService()
	{
		return service;
	}

	public void setService(GeneriqueConnexionService service)
	{
		this.service = service;
	}

	public List<ClsSalaryToProcess> getListOfSalaryToProcess()
	{
		return listOfSalaryToProcess;
	}

	public void setListOfSalaryToProcess(List<ClsSalaryToProcess> listOfSalaryToProcess)
	{
		this.listOfSalaryToProcess = listOfSalaryToProcess;
	}

	public ClsParameterOfPay getParameterOfSalary()
	{
		return parameterOfSalary;
	}

	public void setParameterOfSalary(ClsParameterOfPay parameterOfSalary)
	{
		this.parameterOfSalary = parameterOfSalary;
	}

	public ClsFictifParameterOfPay getFictiveParameterOfPay()
	{
		return fictiveParameterOfPay;
	}

	public void setFictiveParameterOfPay(ClsFictifParameterOfPay fictiveParameterOfPay)
	{
		this.fictiveParameterOfPay = fictiveParameterOfPay;
	}

	public void initialise()
	{

	}

	public ClsSalariesEngine()
	{

	}

	/**
	 * construit la liste des rubriques
	 */
	public List buildListOfAllRubrique()
	{
		ParameterUtil.println(">>buildListOfAllRubrique");
		// List listOfParubq = service.find("from Rhprubrique" +
		// " where identreprise = '" + parameterOfSalary.getDossier() + "'" +
		// " order by identreprise , crub");
		//List listOfParubq = service.find("from Rhprubrique" + " where identreprise = '" + parameterOfSalary.getDossier() + "'" + " and crub between '0001' and '9999'" + " order by identreprise , crub");
		List listOfParubq = service.find("from Rhprubrique" + " where identreprise = '" + parameterOfSalary.getDossier() + "'" + " order by identreprise , crub");
		return listOfParubq;
	}

	/**
	 * construit la liste des rubriques retro
	 */
	public List buildListOfAllRubriqueRetro()
	{
		ParameterUtil.println("<<buildListOfAllRubriqueRetro");
		List listOfParubq = service.find("from Rhthrubq" + " where identreprise = '" + parameterOfSalary.getDossier() + "'" + " and aamm = '" + parameterOfSalary.getMonthOfPay() + "'" + " and nbul = "
				+ parameterOfSalary.getNumeroBulletin() + " order by identreprise , crub");
		return listOfParubq;
		
	}

	/**
	 * construit une liste contenant les salari�s clon�s
	 * 
	 * @param listOfSalaries
	 * @return liste des agents � traiter
	 */
	public List<ClsInfoSalaryClone> getListOfInfoSalaryClone(List listOfSalaries)
	{
		//
		List<ClsInfoSalaryClone> l = new ArrayList<ClsInfoSalaryClone>();
		for (Object obj : listOfSalaries)
		{
			l.add(new ClsInfoSalaryClone((Salarie) obj));
		}
		//
		return l;
	}

	private boolean existFictiveSalaries(String cdos, String aamm, String lastdayofmonth)
	{
		String query = "Select count(*) From Salarie where cdos='" + cdos + "' and pmcf='" + aamm + "' and dfcf>='" + lastdayofmonth + "'";

		// expatri�, pour lequel le fictif est actif

		return true;
	}

	/**
	 * Lance le calcul de la paie sur chaque salari� contenu dans la liste re�ue en param�tre. Cette liste est compos�e des �l�ments de type Salarie.
	 * 
	 * @param listOfSalaries
	 *            liste des agents � utiliser pour le calcul des salaires
	 */
	public void execute(List listOfSalaries)
	{
		ParameterUtil.println(">>constructionOfListSalaries(1)");
		listOfParubq = parameterOfSalary.isUseRetroactif() ? buildListOfAllRubriqueRetro() : buildListOfAllRubrique();

		// mis en commentaire par yannick; mauvaise influence sur la paie au net
		// parameterOfSalary.setPaieAuNetMaxIteration(2);

		parameterOfSalary.setThreadmax(threadmax);
		
		
		try
		{
			if(parameterOfSalary.utilisationmoduleexterne)
				this.initCalculExterne(parameterOfSalary.dossier);
		}
		catch (Exception e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// --on determine s'il existe des salaries en conge fictif
		// if(StringUtils.equals("O",parameterOfSalary.getFictiveCalculus()))
		// {
		// fictiveParameterOfPay = new ClsFictifParameterOfPay();
		// BeanUtils.copyProperties(parameterOfSalary, fictiveParameterOfPay);
		// ClsFictifNomenclatureUtil utilNomenclatureFictif = new ClsFictifNomenclatureUtil(fictiveParameterOfPay);
		// fictiveParameterOfPay.setUtilNomenclatureFictif(utilNomenclatureFictif);
		// boolean init = fictiveParameterOfPay.init();
		// if(!init)
		// {
		// String error = fictiveParameterOfPay.error;
		// return;
		// }
		// }

		int i = 0;
		nbrSalaries = listOfSalaries.size();
//		if (ui != null)
//			ui.mettreAJourProgressBar(nbrSalariesTraite);

		allTask = new ArrayList<Future>();

		Future currentTask = null;
		boolean paieAuNet = false;

		String query="select cacc, valm from ParamData where ctab=99 and cdos='"+parameterOfSalary.getDossier()+"' and cacc='BULAUNET' and nume >=1 and nume<=10 and valm="+parameterOfSalary.getNumeroBulletin();
		Session session = service.getSession();
		try {
			Query q = session.createSQLQuery(query);
			List lst = q.list();
			
			if(!lst.isEmpty() && lst.size()>=1)
				paieAuNet = true;
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			service.closeSession(session);
		}
		
//		System.out.println("PAIE AU NET: "+paieAuNet+"............................................");

		for (Object obj : listOfSalaries)
		{
			i++;
			//System.out.println("Envoit du salari� "+i+"/"+nbrSalaries+" - Matricule "+((Salarie) obj).getComp_id().getNmat());

			// if(i%20 == 0)
			// Runtime.getRuntime().gc();
			try
			{
				if (poolSrev == null)
				{
					poolSrev = Executors.newFixedThreadPool(threadmax);
				}
				
				Salarie agent = (Salarie) obj;
				if(paieAuNet==true){
//					System.out.println("FIXATION DES PARAMETRES AU NET............................................");
					agent.setPnet("O");
					agent.setSnet(BigDecimal.ZERO);
				}
				currentTask = poolSrev.submit(new ClsTraiterSalaireThread(this, agent, i));
				allTask.add(currentTask);
			}
			catch (RejectedExecutionException e)
			{
				e.printStackTrace();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public void cancelAllCalcul()
	{
		for (Future future : allTask)
		{
			try
			{
				if (!future.isDone())
				{
					future.cancel(true);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * <p>
	 * PAIE AFRIQUE : Test des autorisations d'acces aux ecrans salaries, rubriques et aux tables
	 * </p>
	 * <p>
	 * ENTREES utype : type d'acces 1 - Ecrans des salaries
	 * </p>
	 * <p>
	 * 2 - Ecrans des rubriques
	 * </p>
	 * <p>
	 * 3 - Classes de salarie
	 * </p>
	 * <p>
	 * 4 - Tables de nomenclature
	 * </p>
	 * <p>
	 * ucode : designe l'ecran par son numero, sinon
	 * </p>
	 * <p>* pour tous les ecrans
	 * </p>
	 * <p>
	 * uacces : acces demande C - creation , modification , int
	 * </p>
	 * <p>
	 * M - modification , interrogation
	 * </p>
	 * <p>
	 * I - interrogation
	 * </p>
	 * <p>
	 * S - suppression
	 * </p> => autorpaie
	 * 
	 * @param l_cuti
	 *            l'utilisateur connect�
	 * @param l_utype
	 *            type d'acc�s
	 * @param l_ucode
	 *            designe l'ecran par son numero
	 * @param l_uacces
	 *            acces demande
	 * @return true si autoris� ou false sinon
	 */
	public boolean autorPaie(String l_cuti, String l_utype, String l_ucode, String l_uacces)
	{
		// w_acces varchar2(1);
		String w_acces = "";
		//
		// BEGIN
		// BEGIN
		// SELECT acces INTO w_acces FROM PAUTI
		// where identreprise = l_cdos
		// AND CUTI = l_cuti
		// AND TYPU = l_utype
		// AND CODE = l_ucode;
		ParameterUtil.println(">> CUTI : " + l_cuti);
		ParameterUtil.println(">> TYPE : " + l_utype);
		ParameterUtil.println(">> CODE : " + l_ucode);
		ParameterUtil.println(">> ACCESS : " + l_uacces);
//		Object oPauti = service.get(Rhtuti.class, new RhtutiPK(parameterOfSalary.getDossier(), l_cuti, l_utype, l_ucode));
//		if (oPauti == null)
//		{
//			return false;
//		}
//		w_acces = ((Rhtuti) oPauti).getAcces();
//		if ((l_utype.equals("1")) && (l_uacces.equals(" ")))
		if(StringUtils.isBlank(w_acces))
			return true;
		//
		if (w_acces != null)
		{
			if (w_acces.equals("*"))
				return true;
			else if ((w_acces.equals("S")) && (l_uacces.equals("S")))
				return true;
			else if ((w_acces.equals("I")) && (l_uacces.equals("I")))
				return true;
			else if ((w_acces.equals("M")) && (l_uacces.equals("I") || (l_uacces.equals("M"))))
				return true;
			else if (w_acces.equals("C") && (l_uacces.equals("I") || l_uacces.equals("M") || (l_uacces.equals("C"))))
				return true;
		}
		return false;
	}

	/**
	 * clacule le bulletin de paie d'un agent
	 * 
	 * @param agent
	 *            l'agent concern�
	 */
	public void traiterSalaire(Salarie agent, Integer sessionId)
	{
		if(this.synchronize_traiter_salaire)
			synchroTraiterUnSalarie(agent, sessionId);
		else
			traiterUnSalarie(agent, sessionId);
	}
	public  synchronized void synchroTraiterUnSalarie(Salarie agent,Integer sessionId)
	{
		traiterUnSalarie(agent, sessionId);
	}
	
	private Object m;
	private Object callMethode(Object obj, String nomMethode,Object valeurParam)
	{
		try
		{
//			Method oMethod = obj.getClass().getMethod(nomMethode, classe);
//			if (oMethod != null) return  oMethod.invoke(obj, valeurParam);
			if(obj != null)
				return MethodUtils.invokeExactMethod(obj, nomMethode, valeurParam);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	private void instanceMethodesAvantApresCalcul(ClsParameterOfPay param,ClsInfoSalaryClone infoSalary)
	{

		try
		{
			m = Class.forName ("com.cdi.deltarh.paie.engine.ClsMethodesAvantEtApresCalcul").newInstance ();
			callMethode(m, "setService", service);
			callMethode(m, "setCdos", param.dossier);
			callMethode(m, "setNmat", infoSalary.getComp_id().getNmat());
			callMethode(m, "setAamm", param.getMonthOfPay());
			callMethode(m, "setNbul", param.getNumeroBulletin());
		}
		catch (Exception e)
		{
			//e.printStackTrace();
		}
	}
	
	public  void traiterUnSalarie(Salarie agent,Integer sessionId)
	{
		ParameterUtil.println(">>traiterSalaire at " + new Date());

		boolean continuer = true;

		ClsInfoSalaryClone infoSalary = new ClsInfoSalaryClone(agent);

		ClsParameterOfPay param = parameterOfSalary.clone();
		
//		if (StringUtils.equals(param.nomClient, ClsEntreprise.COMILOG))
//			param.chargerPeriodeDePaie(infoSalary.getCods());
		
		ClsFictifParameterOfPay paramFictif = null;
		if (StringUtils.equals("O", param.getFictiveCalculus()))
		{
			paramFictif = fictiveParameterOfPay.clone();
		}
		
		this.instanceMethodesAvantApresCalcul(param, infoSalary);
		//ClsMethodesAvantEtApresCalcul m = new ClsMethodesAvantEtApresCalcul(service,param.dossier,param.getMonthOfPay(), param.getNumeroBulletin(), infoSalary.getComp_id().getNmat());
		callMethode(m,"avantCalcul", StringUtils.EMPTY);

		ClsSalaryToProcess salaryToProcess = new ClsSalaryToProcess(param, infoSalary, param.getService());
		
		//*****************************************************************************************************
		try
		{
			param.sessionId = Integer.valueOf(agent.getNmat());
		}
		catch (NumberFormatException e2)
		{
			param.sessionId = sessionId;
		}
		//
		param.chargementTableValeurAjustement();
		
		
		//*******************************************************************************************************

		// fictif
		salaryToProcess.setFictiveParameter(paramFictif);

		/**
		 * Mise � jour des montants dans la table des virements
		 */

		//
		salaryToProcess.buildElementVarMap();

		salaryToProcess.buildElementFixeMap();

		// salaryToProcess.buildRubriqueOfSessionMap();

		salaryToProcess.buildLignePretMap();

		salaryToProcess.buildNumeroPretMap();

		// salaryToProcess.calculDesCumuls();

		// salaryToProcess.buildSpecifiqueCumul99Map();

		salaryToProcess.getOPeriod().setDebutPeriode(param.getMyMonthOfPay().getYearAndMonthInt());

		salaryToProcess.getOPeriod().setFinPeriode(param.getMyMonthOfPay().getYearAndMonthInt());		
		
		salaryToProcess.maxNLigEV = salaryToProcess.getMaxNumLigneEltVar() + 1;

		// IF Mode_Test THEN pap_logins('Salarie: ' || wsal01.nmat || '*'); END IF;
		//
		// SAVEPOINT Test_Save;
		//
		// -- chargement des donnees infos salaries historise
		// -- dans le cas d'un traitement retro
		// IF retroactif THEN
		// PA_CALC_ANX.charg_infos_salh;
		// END IF;
		if (param.isUseRetroactif())
			salaryToProcess.chargerInfoSalaryHistorique();
		boolean autopaie = autorPaie(param.getUti(), param.getTypu(), agent.getClas(), param.getAccess());
		boolean bulletinbloque = salaryToProcess.bulletinBloque();
		boolean finArrierePaie = false;
		// IF PA_PAIE.autorpaie(wsd_fcal1.cdos,w_cuti,3,wsal01.clas,'I') AND
		// NOT PA_PAIE.Bulletin_Bloque(wsd_fcal1.cdos, wsal01.nmat)
		// THEN
		//
		autopaie = true;
		ParameterUtil.println("...bulletinbloque:" + bulletinbloque);

		if(bulletinbloque)
		{
			salaryToProcess.parameter.errorMessage("INF-00449", salaryToProcess.parameter.getLangue(), salaryToProcess.infoSalary.getComp_id().getNmat());
		}
		

		if (autopaie && !bulletinbloque)
		{
			// Suppression des lignes de calcul avant tout lancement pour le salari�
			try
			{
				salaryToProcess.majTableVirements();
				salaryToProcess.deleteBulletin();
				salaryToProcess.deleteElementsFictif();
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
			}
			
			// IF Mode_Test THEN pap_logins('* Debut Salarie: ' || wsal01.nmat || ' *'); END IF;
			//
			// -- Chargement des periodes de paie(Inclue la table 91)
			// W_Faitout := charg_per_paie;

			// salaryToProcess.chargerPeriodeDePaie();
			//
			// -- Tester si le salarie se trouve deja dans PACALC avec un No de
			// -- bulletin different.
			// verif_bul := 0;
			//
			// IF NOT destruction_fcal(verif_bul)
			// THEN
			// err_msg := PA_PAIE.erreurp('ERR-90043',w_clang);
			// RETURN FALSE;
			// END IF;
			//
			// -- Pas de calcul mais destruction des enregistrements calcul
			// IF wsal01.cals = 'O' THEN
			if ("O".equals(agent.getCals()))
			{
				//
				// Fin_Arrp := FALSE;
				finArrierePaie = false;
				//
				// -- On ne calcule pas le conges les mois suivants le mois de
				// -- paiement du conges, ni le dernier mois si il est plein.
				// -- Cela ne concerne que le Fictif_N et le Fictif_B
				// IF ( ( w_fictif = 'O' AND w_typ_fictif = 'B' ) OR w_fictif = 'N' )
				// AND
				// NOT PA_PAIE.NouB(wsal01.pmcf)
				// AND
				// w_aamm > wsal01.pmcf
				// AND
				// ( wsal01.dfcf >= w_dfpa )
				// THEN
				// Fin_Arrp := TRUE;
				// END IF;
				//on rajoute une cl� en table 99 pour dire si O/N on veut calculer ou pas
				
				if ((("O".equals(param.getFictiveCalculus()) && "B".equals(param.getFictiveCalculusType())) || "N".equals(param.getFictiveCalculus())) && !ClsObjectUtil.isNull(agent.getPmcf())
						&& param.getMyMonthOfPay().getYearAndMonth().compareToIgnoreCase(agent.getPmcf()) > 0 && !ClsObjectUtil.isNull(agent.getDfcf())
						&& salaryToProcess.getLastDayOfMonth().compareTo(agent.getDfcf()) <= 0)
				{
					//Ce test n'est pas n�cessaire pour SOBRAGA
//					if(StringUtils.notEquals(param.nomClient, ClsEntreprise.SOBRAGA) && StringUtils.notEquals(param.nomClient, ClsEntreprise.BRASSERIES_BBLOME) )
						finArrierePaie = true;
					if(param.calculPaieMoisSuivantCalculConge)
						finArrierePaie = false;
				}
				//
				// -- LH 181297
				// -- Pas de calcul si salarie absent tobut le mois
				// IF Absent_Tout_Mois(wsal01.nmat,w_aamm,wsd_fcal1.nbul,retroactif,w_ddpa, w_dfpa) AND
				// PDC_si_ATLM
				// THEN
				// Fin_Arrp := TRUE;
				// END IF;
				// if(salaryToProcess.isAbsentToutMois() && param.isABSENCE_MOIS_PDC_SI_ATLM()){
				// finArrierePaie = true;
				// }
				if (salaryToProcess.getUtilNomenclature().isAbsentAllOfTheMonth(param.getDossier(), infoSalary.getComp_id().getNmat(), param.getMonthOfPay(), param.getNumeroBulletin(), param.isUseRetroactif(),
						salaryToProcess.getFirstDayOfMonth(), salaryToProcess.getLastDayOfMonth(), param.getAppDateFormat())
						&& param.isABSENCE_MOIS_PDC_SI_ATLM())
				{
					finArrierePaie = true;
					//salaryToProcess.getParameter().setError(salaryToProcess.getParameter().errorMessage("Salari� %1 absent tout le mois", salaryToProcess.getParameter().getLangue(),salaryToProcess.getInfoSalary().getComp_id().getNmat()));
				}
				//
				// -- Lecture des arrets de paie
				// IF NOT Fin_Arrp THEN
				// -- Test si traitement retro
				// IF retroactif THEN
				// OPEN curs_arp2;
				// ELSE
				// OPEN curs_arp;
				// END IF;
				// LOOP
				// IF retroactif THEN
				// FETCH curs_arp2 INTO warrpai;
				// EXIT WHEN curs_arp2%NOTFOUND;
				// ELSE
				// FETCH curs_arp INTO warrpai;
				// EXIT WHEN curs_arp%NOTFOUND;
				// END IF;
				// IF warrpai.ddar <= w_ddpa AND warrpai.dfar >= w_dfpa THEN
				// Fin_Arrp := TRUE;
				// EXIT;
				// END IF;
				// END LOOP;
				// IF retroactif THEN
				// CLOSE curs_arp2;
				// ELSE
				// CLOSE curs_arp;
				// END IF;
				// END IF;
				if (!finArrierePaie)
				{
					String queryArrierePaie = "from SuspensionPaie" + " where identreprise = '" + this.getParameterOfSalary().getDossier() + "'" + " and nmat = '" + agent.getNmat() + "'"
							+ " order by identreprise , nmat, ddar";

					String queryArrierePaieRetro = "from SuspensionPaie" + " where identreprise = '" + this.getParameterOfSalary().getDossier() + "'" + " and nmat = '" + (agent).getNmat() + "'" + " and aamm = '"
							+ param.getMyMonthOfPay().getYearAndMonth() + "'" + " and nbul = " + param.getNumeroBulletin() + " order by identreprise , nmat, ddar";
					List listOfArriere = param.isUseRetroactif() ? salaryToProcess.getService().find(queryArrierePaieRetro) : salaryToProcess.getService().find(queryArrierePaie);
					Date ddar = null;
					Date dfar = null;
					for (Object obj1 : listOfArriere)
					{
						if (param.isUseRetroactif())
						{
							ddar = ((SuspensionPaie) obj1).getDdar();
							dfar = ((SuspensionPaie) obj1).getDfar();
						}
						else
						{
							ddar = ((SuspensionPaie) obj1).getDdar();
							dfar = ((SuspensionPaie) obj1).getDfar();
						}
						if (ddar.compareTo(salaryToProcess.getFirstDayOfMonth()) <= 0 && dfar.compareTo(salaryToProcess.getLastDayOfMonth()) >= 0)
						{
							finArrierePaie = true;
							//salaryToProcess.getParameter().setError(salaryToProcess.getParameter().errorMessage("Salari� %1 en arret de paie", salaryToProcess.getParameter().getLangue(),salaryToProcess.getInfoSalary().getComp_id().getNmat()));
							break;
						}
					}
				}
				//
				// W_Cont := TRUE;
				// IF Fin_Arrp THEN
				// W_Cont := FALSE;
				// DBMS_PIPE.PACK_MESSAGE('I2');
				// DBMS_PIPE.PACK_MESSAGE(wsal01.nmat||PA_PAIE.erreurp('INF-10345',w_clang));
				// w_status := DBMS_PIPE.SEND_MESSAGE(w_pipe_name);
				// ELSE
				//
				// -- MM 12/09/2000 flag agent embauche
				// Embauche := FALSE;
				if (finArrierePaie)
				{
					ParameterUtil.println(">>>>>>>>>>>>>>>>>>>>>>>>Matricule non calculable: " + agent.getNmat());
					continuer = false;
				}
				//YT : On ne calcule pas les agents dont la date d'entr�e est sup�rieur � la paie actuelle
				if(infoSalary.getDtes() == null)
				{
					salaryToProcess.parameter.errorMessage("ERR-90069", salaryToProcess.parameter.getLangue(), salaryToProcess.infoSalary.getComp_id().getNmat());
					continuer = false;
				}
				else
				if(param.getLastDayOfMonth().compareTo(infoSalary.getDtes()) < 0)
					continuer = false;
				
				if (continuer)
				{
					// sp�cifique cnss
					if (StringUtils.equals(param.nomClient, "CNSS"))
					{
//						ELSIF wsal01.dtes > w_dfpa THEN -- TFN 271006 On ne calcul pas si la date d'entr�e et sup�rieur au dernier jours du mois
//			               pap_logins('* Salarie: ' || wsal01.nmat || ' Contrat pas encore d�but�');                                     
//			               DBMS_PIPE.PACK_MESSAGE('I2');
//			               DBMS_PIPE.PACK_MESSAGE(wsal01.nmat||PA_PAIE.erreurp('INF-01606',w_clang)); --'Contrat non commenc�'
//			               w_status := DBMS_PIPE.SEND_MESSAGE(w_pipe_name);
//			               W_Cont := FALSE;
//			            ELSIF wsal01.mrrx IS NOT NULL 
//			                   AND TO_CHAR(wsal01.dmrr, 'YYYY')||TO_CHAR(wsal01.dmrr, 'MM') <= TO_CHAR(wpdos.ddmp, 'YYYY')||TO_CHAR(wpdos.ddmp, 'MM') THEN -- LM 12/03/2008 On ne calcul pas si le salari� � quitter la soci�t�
//			               pap_logins('* Salarie: ' || wsal01.nmat || ' a quitt� la soci�t�');   
//			               DBMS_PIPE.PACK_MESSAGE('I2');
//			               DBMS_PIPE.PACK_MESSAGE(wsal01.nmat||PA_PAIE.erreurp('INF-01605',w_clang)); --Salari� a quitt� la soci�t�
//			               w_status := DBMS_PIPE.SEND_MESSAGE(w_pipe_name);
//			               W_Cont := FALSE;
//			            ELSIF w_Cdecc AND wsal01.decc IS NOT NULL 
//			                   AND TO_CHAR(wsal01.decc, 'YYYY')||TO_CHAR(wsal01.decc, 'MM') <= TO_CHAR(wpdos.ddmp, 'YYYY')||TO_CHAR(wpdos.ddmp, 'MM') THEN --LM 12/03/2008 Test si contrat �chu              
//			               pap_logins('* Salarie: ' || wsal01.nmat || ' Contrat �chu -> pas de calcul*');                                     
//			               W_Cont := FALSE;                  
//			               DBMS_PIPE.PACK_MESSAGE('I2');
//			               DBMS_PIPE.PACK_MESSAGE(wsal01.nmat||PA_PAIE.erreurp('INF-01604',w_clang)); --'contrat �chu'
//			               w_status := DBMS_PIPE.SEND_MESSAGE(w_pipe_name); 
						if(param.getLastDayOfMonth().compareTo(infoSalary.getDtes()) < 0)
							continuer = false;
						else if(StringUtils.isNotBlank(infoSalary.getMrrx()) && (ClsDate.getDateS(infoSalary.getDmrr(), ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).compareTo(ClsDate.getDateS(param.getDdmp(), ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM)) <= 0))
							continuer = false;
						else if(param.priseEnCompteDateFinContrat && infoSalary.getDecc() != null && (ClsDate.getDateS(infoSalary.getDecc(), ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).compareTo(ClsDate.getDateS(param.getDdmp(), ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM)) <= 0))
							continuer = false;
					}
					if(StringUtils.equals("O",infoSalary.getCals()) &&param.priseEnCompteDateFinContrat && infoSalary.getDecc() != null && (ClsDate.getDateS(infoSalary.getDecc(), ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).compareTo(ClsDate.getDateS(param.getDdmp(), ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM)) <= 0))
					{
						continuer = false;
						salaryToProcess.getParameter().setError(salaryToProcess.getParameter().errorMessage("Salari� %1 . Contrat Echu le %2", salaryToProcess.getParameter().getLangue(),salaryToProcess.getInfoSalary().getComp_id().getNmat(), new ClsDate(salaryToProcess.getInfoSalary().getDecc()).getDateS("dd/MM/yyyy")));
					}
				}
				if (continuer)
				{
					salaryToProcess.setEmbauche(false);
					// IF wsal01.dtes >= w_ddpa AND
					// wsal01.dtes <= w_dfpa AND
					// wsd_fcal1.nbul = 9 THEN
					//
					// Embauche := TRUE;
					//
					// END IF;
					if (agent.getDtes() != null && salaryToProcess.getFirstDayOfMonth().compareTo(agent.getDtes()) <= 0 && salaryToProcess.getLastDayOfMonth().compareTo(agent.getDtes()) >= 0
							&& param.getNumeroBulletin() == 9)
					{
						salaryToProcess.setEmbauche(true);
					}
					// -- Fin modif 12/09/2000
					//
					// stc := FALSE;
					param.setStc(false);
					//
					// -- Solde de tous comptes : dernier mois de paie de ce salarie
					// IF wsal01.mrrx = 'MU' OR wsal01.mrrx = 'RA'
					// THEN
					if ("MU".equals(agent.getMrrx()) || "RA".equals(agent.getMrrx()))
					{
						// IF wsal01.dmrr >= w_ddpa AND
						// wsal01.dmrr <= w_dfpa AND
						// wsd_fcal1.nbul = 9
						// THEN
						if (agent.getDmrr() != null)
						{
							if (salaryToProcess.getFirstDayOfMonth().compareTo(agent.getDmrr()) <= 0 && salaryToProcess.getLastDayOfMonth().compareTo(agent.getDmrr()) >= 0 && param.getNumeroBulletin() == 9)
							{
								// stc := TRUE;
								param.setStc(true);
								// rub_stc := TRUE;
								param.setRubriqueStc(true);
								// IF NOT chg_rubstc(wsal01.mtfr ) THEN
								// -- MM Pas de controle du param pour le calcul ISR
								// /*
								// err_msg := 'Sal: '|| wsal01.nmat|| '. Motif radiation ('||
								// wsal01.mtfr|| ') inexistant table 23';
								// W_Faitout := err_calc (err_msg);
								// w_cont := FALSE;
								// */
								// rub_stc := FALSE;
								// END IF;
								
								
								if (!salaryToProcess.chargerRubriqueMotifFinContrat(agent.getMtfr()))
								{
									//ajout yannick 30/09/09
									//ajout armel 30/11/09 continuer = false;
									
									//ajout armel 30/11/09 param.setRubriqueStc(false);
								}
							}
							// ELSE
							else
							{
								// -- MM
								// -- Test si date de radiation < date debut de paie
								// -- alors pas de calcul et passage salarie suivant.
								// IF wsal01.dmrr < w_ddpa AND
								// NOT Ste_SHELL_GABON
								// THEN
								// w_cont := FALSE;
								// END IF;
								if (salaryToProcess.getFirstDayOfMonth().compareTo(agent.getDmrr()) > 0 && !salaryToProcess.getEntreprise().equals(ClsEnumeration.EnEnterprise.SHELL_GABON))
									continuer = false;
							}
						}
						// END IF;
					}
					// ELSE
					else
					{
						// -- Test si date de radiation < date debut de paie
						// -- alors pas de calcul et passage salarie suivant.
						// IF wsal01.dmrr < w_ddpa AND
						// NOT Ste_SHELL_GABON
						// THEN
						// w_cont := FALSE;
						// END IF;
						if (salaryToProcess.getFirstDayOfMonth() != null && (agent).getDmrr() != null && salaryToProcess.getFirstDayOfMonth().compareTo(agent.getDmrr()) > 0
								&& !salaryToProcess.getEntreprise().equals(ClsEnumeration.EnEnterprise.SHELL_GABON))
							continuer = false;
					}
					// END IF;
				}
				// END IF;
				// else
				// continuer = false;
				//
				salaryToProcess.setContinuer(continuer);
				// if(listOfSalaryToProcess == null)
				// listOfSalaryToProcess = new ArrayList<ClsSalaryToProcess>();
				// listOfSalaryToProcess.add(salaryToProcess);

				// ParameterUtil.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
				// ParameterUtil.println("listOfSalaryToProcess len :" + listOfSalaryToProcess.size());
				// for (Object object : listOfSalaryToProcess) {
				// ClsObjectUtil.displaySalaryProperties((ClsSalaryToProcess)object);
				// }
				// IF w_cont THEN

				if (continuer)
				{
					// --------------------------------------
					// -- APPEL DU TRAITEMENT D'UN SALARIE
					// --------------------------------------
					// -- o Possibilite de controler le nombre de salaries traites avec celui
					// -- autorise par la licence
					// -- o Si il ya eu un probleme lors du calcul sur ce salarie (ex: parametre
					// -- manquant dans les nomenclatures), l'erreur est consignee dans le fichier
					// -- log ERRCALCPAIE, les donnees sont remises a l'etat anterieur (ROLLBACK)
					// -- et le calcul passe au salarie suivant.
					// -----------------------------------------------------------------------------
					// pb_calcul := FALSE;
					// W_Faitout := trait_sal;
					// if(param.isUseRetroactif()){
					// salaryToProcess.buildListOfAllRubriqueRetro();
					// }
					// else{
					// salaryToProcess.buildListOfAllRubrique();
					// }
					// salaryToProcess.buildListOfRubrique(listOfParubq);
					param.buildListOfRubrique(listOfParubq, salaryToProcess);
					
					try
					{
						if(parameterOfSalary.utilisationmoduleexterne)
							this.calculExterne(salaryToProcess);
					}
					catch (Exception e1)
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					salaryToProcess.traitementSalaire(param.getAppDateFormat());
					
//					 IF NOT pb_calcul THEN
//
//	                  ------------------------------------
//	                  -- MM 23/06/2004 Calcul et generation
//	                  -- acompte dans les evars
//	                  ------------------------------------
//			  IF calcul_des_acomptes THEN
//	                     gen_acomptes;
//	                  END IF;
					if (!param.isPbWithCalulation())
					{
						
					}
					
					//
					// IF NOT pb_calcul THEN
					// ------------------------------------
					// -- Notification traitement termine
					// ------------------------------------
					// nb_sal := nb_sal + 1;
					// DBMS_PIPE.PACK_MESSAGE('I1');
					// DBMS_PIPE.PACK_MESSAGE(wsal01.nmat
					// || LTRIM(TO_CHAR(nb_sal,'09999'))
					// || 'Traitement Ok');
					// w_status := DBMS_PIPE.SEND_MESSAGE(w_pipe_name);
					//
					// -- Suppression des pseudo-evs generes pour calcul des absences
					// IF retroactif THEN
					// DELETE FROM pahevar
					// where identreprise = wpdos.cdos
					// AND nmat = wsal01.nmat
					// AND aamm = w_aamm
					// AND nbul = wsd_fcal1.nbul
					// AND argu = 'PSEUDO-EV';
					// ELSE
					// DELETE FROM paevar
					// where identreprise = wpdos.cdos
					// AND nmat = wsal01.nmat
					// AND aamm = w_aamm
					// AND nbul = wsd_fcal1.nbul
					// AND argu = 'PSEUDO-EV';
					// END IF;
					// COMMIT;
					// ELSE
					// DBMS_PIPE.PACK_MESSAGE('I2');
					// DBMS_PIPE.PACK_MESSAGE(wsal01.nmat||PA_PAIE.erreurp('ERR-90044',w_clang));
					// w_status := DBMS_PIPE.SEND_MESSAGE(w_pipe_name);
					//
					// pb_calcul := FALSE;
					// -------------
					// BEGIN
					// ROLLBACK TO SAVEPOINT Test_Save;
					// EXCEPTION
					// WHEN OTHERS THEN NULL;
					// END;
					// -------------
					// W_Faitout := err_calc (err_msg);
					// COMMIT;
					//
					// END IF; -- FIN 'NOT pbcalcul'
					if (!param.isPbWithCalulation())
					{
						salaryToProcess.deletePseudoEV();
					}
					else
					{
						param.setPbWithCalulation(false);
						param.insererLogMessage(param.getError());
					}
					//
				}
			}

			// END IF; -- FIN 'IF w_cont'
			//
			// ELSE
			else
			{
				//
				// IF Mode_Test THEN pap_logins('Salarie a calcul NON: suppression pacalc et pafic'); END IF;
				//
				// IF retroactif THEN
				// DELETE FROM prcalc
				// where identreprise = wpdos.cdos
				// AND nmat = wsal01.nmat
				// AND aamm = w_aamm
				// AND nbul = wsd_fcal1.nbul
				// AND nlot = w_nlot;
				// ELSE
				// DELETE FROM pacalc
				// where identreprise = wpdos.cdos
				// AND nmat = wsal01.nmat
				// AND aamm = w_aamm
				// AND nbul = wsd_fcal1.nbul;
				// END IF;
				//
				// IF NOT PA_PAIE.NouB(wsal01.pmcf) AND w_fictif = 'O' AND NOT retroactif THEN
				// IF wsal01.pmcf = w_aamm THEN
				// DELETE FROM pafic
				// where identreprise = wpdos.cdos
				// AND nmat = wsal01.nmat;
				// END IF;
				// END IF;
				//
				// COMMIT;
				try
				{
					salaryToProcess.deleteBulletin();
					if ("O".equals(param.getFictiveCalculus()) && !param.isUseRetroactif())
					{
						if (param.getMyMonthOfPay().getYearAndMonth().equals(agent.getPmcf()))
						{
							// salaryToProcess.getService().deleteFromTable("delete from Pafic" +
							// " where identreprise = '"+ param.getDossier() + "'" +
							// " and nmat = '" + (agent).getComp_id().getNmat() + "'");
							// @yannick : ajout� par yannick, n'existait pas avant
							salaryToProcess.deleteNonRetroFictif();
						}
					}
					
					// sp�cifique cnss
//					if (StringUtils.equals(param.nomClient, ClsEntreprise.CNSS))
//					{
//						-- MM 01-2006 maj des virements
//			            UPDATE pavrmt SET mntdb=null,mntdvd=null,aamm=null
//			             where identreprise = wpdos.cdos
//			               AND nmat = wsal01.nmat;
						String updateString = "Update Rhtvrmtagent set mntdb = null, mntdvd = null, aamm=null where identreprise = '" + infoSalary.getComp_id().getCdos() + "'" + " and nmat = '"
								+ infoSalary.getComp_id().getNmat() + "'";

						this.getService().updateFromTable(updateString);
//					}
				}
				catch (Exception e)
				{
					// logger
					e.printStackTrace();
				}
				//
				// DBMS_PIPE.PACK_MESSAGE('I2');
				// DBMS_PIPE.PACK_MESSAGE(wsal01.nmat||PA_PAIE.erreurp('INF-10344',w_clang));
				// w_status := DBMS_PIPE.SEND_MESSAGE(w_pipe_name);
				//
			}
			// END IF; -- FIN 'IF wsal1.cals= 'O''
			//
			// IF Mode_Test THEN pap_logins('Fin Salarie: ' || wsal01.nmat || '*'); END IF;
			//
		}
		// END IF; -- Fin 'IF autorpaie'

		if (salaryToProcess != null)
		{
			callMethode(m,"apresCalcul",StringUtils.EMPTY);
			
			if (parameterOfSalary.genfile == 'O')
			{
				String texte = String.valueOf(salaryToProcess) + "\n" + String.valueOf(salaryToProcess.getWorkTime()) + "\n---------------LISTE PARAMETRAGE POUR LE SALARIE"
						+ String.valueOf(salaryToProcess.getParameter());

				//StringUtil.printOutObject(texte, parameterOfSalary.getGenfilefolder() + File.separator  + salaryToProcess.getInfoSalary().getComp_id().getNmat() + ".txt");
			}

			salaryToProcess = (ClsSalaryToProcess) ObjectUtils.setNull(salaryToProcess);
			// salaryToProcess = null;
			// Runtime.getRuntime().gc();
		}
	}

	/**
	 * => trait
	 * <p>
	 * Lance le calcul de la paie sur chaque salari� contenu dans la liste re�ue en param�tre.
	 * </p>
	 */
	public void constructionOfListSalaries(String strDateFormat)
	{
		ParameterUtil.println(">>constructionOfListSalaries");
		listOfParubq = parameterOfSalary.isUseRetroactif() ? buildListOfAllRubriqueRetro() : buildListOfAllRubrique();
		// FUNCTION trait RETURN BOOLEAN
		// IS
		//
		// p_ctrt VARCHAR2(2);
		// mois_stc VARCHAR2(6);
		// destruction BOOLEAN;
		// verif_bul NUMBER(1);
		// warrpai paarrpai%ROWTYPE;
		// w_cont BOOLEAN;
		//
		// -- Choix d'une liste de salarie
		// CURSOR curs_chx_sal IS
		// SELECT * from pasa01
		// where identreprise = wpdos.cdos
		// and nmat IN
		// (SELECT nmat from pasalist WHERE session_id = w_session_id)
		// order by nmat;
		// String queryStringPasaList = "from Salarie" +
		// " where identreprise = '" + this.getParameterOfSalary().getDossier() + "'" +
		// " and nmat in ( select nmat from Rhtsalist where session_id =" + this.getParameterOfSalary().getSessionId() + ")";
		String queryStringPasaList = "from Salarie" + " where identreprise = '" + this.getParameterOfSalary().getDossier() + "'" + " and nmat = '013203'";//

		//
		// -- Chois d'un intervalle
		// CURSOR curs_chx_opt IS
		// SELECT * from pasa01
		// where identreprise = wpdos.cdos
		// and niv1 between NVL(LTRIM(SUBSTR(woptions,26,3)),' ') and LTRIM(SUBSTR(woptions,29,3))
		// and niv2 between NVL(LTRIM(SUBSTR(woptions,32,3)),' ') and LTRIM(SUBSTR(woptions,35,3))
		// and niv3 between NVL(LTRIM(SUBSTR(woptions,38,8)),' ') and LTRIM(SUBSTR(woptions,46,8))
		// and nmat between NVL(LTRIM(SUBSTR(woptions,54,6)),' ') and SUBSTR(woptions,60,6)
		// and modp like wsd_fcal1.modp
		// and clas like wsd_fcal1.clas
		// -- and cals = 'O'
		// order by identreprise , niv1, niv2, niv3, nmat;
		String res = getFinalEmployeeList(this.getParameterOfSalary().getDepartMatricule(), this.getParameterOfSalary().getFinMatricule(), this.getParameterOfSalary().getDepartNiv1(), this.getParameterOfSalary()
				.getFinNiv1(), this.getParameterOfSalary().getDepartNiv2(), this.getParameterOfSalary().getFinNiv2(), this.getParameterOfSalary().getDepartNiv3(), this.getParameterOfSalary().getFinNiv3());
		String res2 = getFinalEmployeeList(this.getParameterOfSalary().getDepartNiv1(), this.getParameterOfSalary().getFinNiv1(), this.getParameterOfSalary().getDepartNiv2(), this.getParameterOfSalary().getFinNiv2(),
				this.getParameterOfSalary().getDepartNiv3(), this.getParameterOfSalary().getFinNiv3());
		String queryStringListIntervalle = "from Salarie" + " where identreprise = '" + this.getParameterOfSalary().getDossier() + "'" + res2 + " and modp like '"
				+ this.getParameterOfSalary().getModePaiement().toString() + "'" + " and clas like '" + this.getParameterOfSalary().getClas() + "'";

		String orderString = " order by identreprise , niv1, niv2, niv3, nmat ";
		String queryStringListIntervalle1 = queryStringListIntervalle + orderString;
		//
		// -- Choix des salaries ayant un EF
		// CURSOR curs_chx_opt1 IS
		// SELECT * from pasa01 a
		// where identreprise = wpdos.cdos
		// and niv1 between NVL(LTRIM(SUBSTR(woptions,26,3)),' ') and LTRIM(SUBSTR(woptions,29,3))
		// and niv2 between NVL(LTRIM(SUBSTR(woptions,32,3)),' ') and LTRIM(SUBSTR(woptions,35,3))
		// and niv3 between NVL(LTRIM(SUBSTR(woptions,38,8)),' ') and LTRIM(SUBSTR(woptions,46,8))
		// and nmat between NVL(LTRIM(SUBSTR(woptions,54,6)),' ') and SUBSTR(woptions,60,6)
		// and modp like wsd_fcal1.modp
		// and clas like wsd_fcal1.clas
		// and nmat IN (select nmat from paelfix b
		// where b.cdos = a.cdos
		// and b.nmat = a.nmat
		// and b.codp = w_plus_rub
		// and (ddeb is null OR
		// (ddeb is not null and ddeb <= to_date(w_aamm,'YYYYMM')))
		// and (dfin IS NULL OR
		// (dfin is not null and dfin >= to_date(w_aamm,'YYYYMM')))
		// )
		// order by identreprise , niv1, niv2, niv3, nmat;

		String critereEF = " and nmat in ( " + " select b.nmat from Rhteltfixagent b" + " where b.cdos = cdos" + " and b.nmat = nmat" + " and b.codp = '"
				+ this.getParameterOfSalary().getRubriquePlus() + "'" + " and (b.ddeb is null or (b.ddeb is not null and b.ddeb <= '" + this.getParameterOfSalary().getMyMonthOfPay().getYearAndMonth() + "'))"
				+ " and (b.dfin is null or (b.dfin is not null and b.dfin >= '" + this.getParameterOfSalary().getMyMonthOfPay().getYearAndMonth() + "')))";
		String critereEF1 = " nmat in ( " + " select b.nmat from Rhteltfixagent b" + " where b.cdos = cdos" + " and b.nmat = nmat" + " and b.codp = '"
				+ this.getParameterOfSalary().getRubriquePlus() + "'" + " and (b.ddeb is null or (b.ddeb is not null and b.ddeb <= '" + this.getParameterOfSalary().getMyMonthOfPay().getYearAndMonth() + "'))"
				+ " and (b.dfin is null or (b.dfin is not null and b.dfin >= '" + this.getParameterOfSalary().getMyMonthOfPay().getYearAndMonth() + "')))";
		String queryStringListIntervalleEF = queryStringListIntervalle + critereEF + orderString;
		//
		// -- Choix des salaries ayant un EV
		// CURSOR curs_chx_opt2 IS
		// SELECT * from pasa01 a
		// where identreprise = wpdos.cdos
		// and niv1 between NVL(LTRIM(SUBSTR(woptions,26,3)),' ') and LTRIM(SUBSTR(woptions,29,3))
		// and niv2 between NVL(LTRIM(SUBSTR(woptions,32,3)),' ') and LTRIM(SUBSTR(woptions,35,3))
		// and niv3 between NVL(LTRIM(SUBSTR(woptions,38,8)),' ') and LTRIM(SUBSTR(woptions,46,8))
		// and nmat between NVL(LTRIM(SUBSTR(woptions,54,6)),' ') and SUBSTR(woptions,60,6)
		// and modp like wsd_fcal1.modp
		// and clas like wsd_fcal1.clas
		// and nmat IN (select nmat from paevar b
		// WHERE b.cdos = a.cdos
		// AND b.nmat = a.nmat
		// AND b.aamm = w_aamm
		// AND b.nbul = wsd_fcal1.nbul
		// AND b.rubq = w_plus_rub)
		// order by identreprise , niv1, niv2, niv3, nmat;
		//
		String critereEV = " and nmat in ( " + " select b.nmat from Rhteltvardet b" + " where b.cdos = cdos" + " and b.nmat = nmat" + " and b.aamm = '" + this.getParameterOfSalary().getMyMonthOfPay().getYearAndMonth()
				+ "'" + " and b.nbul = " + this.getParameterOfSalary().getNumeroBulletin() + " and b.rubq = '" + this.getParameterOfSalary().getRubriquePlus() + "')";

		String critereEV1 = " nmat in ( " + " select b.nmat from Rhteltvardet b" + " where b.cdos = cdos" + " and b.nmat = nmat" + " and b.aamm = '" + this.getParameterOfSalary().getMyMonthOfPay().getYearAndMonth()
				+ "'" + " and b.nbul = " + this.getParameterOfSalary().getNumeroBulletin() + " and b.rubq = '" + this.getParameterOfSalary().getRubriquePlus() + "')";

		String queryStringListIntervalleEV = queryStringListIntervalle + critereEV + orderString;
		// -- Choix des salaries ayant un EF ou un EV
		// CURSOR curs_chx_opt3 IS
		// SELECT * from pasa01 a
		// where identreprise = wpdos.cdos
		// and niv1 between NVL(LTRIM(SUBSTR(woptions,26,3)),' ') and LTRIM(SUBSTR(woptions,29,3))
		// and niv2 between NVL(LTRIM(SUBSTR(woptions,32,3)),' ') and LTRIM(SUBSTR(woptions,35,3))
		// and niv3 between NVL(LTRIM(SUBSTR(woptions,38,8)),' ') and LTRIM(SUBSTR(woptions,46,8))
		// and nmat between NVL(LTRIM(SUBSTR(woptions,54,6)),' ') and SUBSTR(woptions,60,6)
		// and modp like wsd_fcal1.modp
		// and clas like wsd_fcal1.clas
		// and (nmat IN (select nmat from paelfix b
		// where b.cdos = a.cdos
		// and b.nmat = a.nmat
		// and b.codp = w_plus_rub
		// and (ddeb is null OR
		// (ddeb is not null and ddeb <= to_date(w_aamm,'YYYYMM')))
		// and (dfin IS NULL OR
		// (dfin is not null and dfin >= to_date(w_aamm,'YYYYMM')))
		// )
		// OR
		// nmat IN (select nmat from paevar b
		// WHERE b.cdos = a.cdos
		// AND b.nmat = a.nmat
		// AND b.aamm = w_aamm
		// AND b.nbul = wsd_fcal1.nbul
		// AND b.rubq = w_plus_rub))
		// order by identreprise , niv1, niv2, niv3, nmat;
		String critereEVF = " and ( " + critereEF1 + "  or  " + critereEV1 + " )";
		String queryStringListIntervalleEVF = queryStringListIntervalle + critereEVF + orderString;
		//
		// -- Choix des salaries ayant un conges
		// CURSOR curs_chx_opt4 IS
		// SELECT * from pasa01
		// where identreprise = wpdos.cdos
		// and niv1 between NVL(LTRIM(SUBSTR(woptions,26,3)),' ') and LTRIM(SUBSTR(woptions,29,3))
		// and niv2 between NVL(LTRIM(SUBSTR(woptions,32,3)),' ') and LTRIM(SUBSTR(woptions,35,3))
		// and niv3 between NVL(LTRIM(SUBSTR(woptions,38,8)),' ') and LTRIM(SUBSTR(woptions,46,8))
		// and nmat between NVL(LTRIM(SUBSTR(woptions,54,6)),' ') and SUBSTR(woptions,60,6)
		// and modp like wsd_fcal1.modp
		// and clas like wsd_fcal1.clas
		// and NVL(pmcf,' ') = w_aamm
		// order by identreprise , niv1, niv2, niv3, nmat;
		String queryStringListIntervalleConge = "from Salarie" + " where identreprise = '" + this.getParameterOfSalary().getDossier() + "'" + res + " and modp like '"
				+ this.getParameterOfSalary().getModePaiement().toString() + "'" + " and clas like '" + this.getParameterOfSalary().getClas() + "'" + " and (pmcf = ' ' or pmcf is null)" +
				// " and pmcf = '" + this.getParameterOfSalary().getMyMonthOfPay().getYearAndMonth() + "'" +
				orderString;
		//
		// CURSOR curs_arp IS
		// SELECT *
		// FROM paarrpai
		// where identreprise = wpdos.cdos
		// AND nmat = wsal01.nmat
		// order by identreprise ,nmat,ddar;
		//
		// CURSOR curs_arp2 IS
		// SELECT cdos,nmat,ddar,dfar,mtar
		// FROM paharrpai
		// where identreprise = wpdos.cdos
		// AND nmat = wsal01.nmat
		// AND aamm = w_aamm
		// AND nbul = wsd_fcal1.nbul
		// order by identreprise ,nmat,ddar;
		//
		// BEGIN
		// ---------------------------------------------------------------------------------
		// -- Il faut prevoir 2 types de curseur : - Selection niv1, niv2, niv3, nmat
		// -- - Selection nmat
		// ---------------------------------------------------------------------------------
		// IF wsd_fcal1.choix = 'O' AND NOT retroactif THEN
		// pb_calcul := chg_pasalist;
		// END IF;
		//
		// nb_sal := 0;
		//
		// -------------------------------------------------------------------------------
		// -- LECTURE FICHIER SALARIES
		// -------------------------------------------------------------------------------
		// IF wsd_fcal1.choix = 'O' THEN
		// OPEN curs_chx_sal;
		// ELSE
		// IF NOT PA_PAIE.NouB(w_plus_cas) THEN
		// IF w_plus_cas = 'F' THEN
		// OPEN curs_chx_opt1;
		// ELSIF w_plus_cas = 'V' THEN
		// OPEN curs_chx_opt2;
		// ELSIF w_plus_cas = 'R' THEN
		// OPEN curs_chx_opt3;
		// ELSIF w_plus_cas = 'C' THEN
		// OPEN curs_chx_opt4;
		// ELSE
		// OPEN curs_chx_opt;
		// END IF;
		// ELSE
		// OPEN curs_chx_opt;
		// END IF;
		// END IF;
		//
		//
		// IF Mode_Test THEN pap_logins('Niv1 '||SUBSTR(woptions,26,3)||'**'||SUBSTR(woptions,29,3)); END IF;
		// IF Mode_Test THEN pap_logins('Niv2 '||SUBSTR(woptions,32,3)||'**'||SUBSTR(woptions,35,3)); END IF;
		// IF Mode_Test THEN pap_logins('Niv3 '||SUBSTR(woptions,38,8)||'**'||SUBSTR(woptions,46,8)); END IF;
		// IF Mode_Test THEN pap_logins('Mat '||SUBSTR(woptions,54,6)||'**'||SUBSTR(woptions,60,6)); END IF;
		//
		//

		List listOfSalaries = null;
		if (this.getParameterOfSalary().getEnTypeSalaries().equals(ClsEnumeration.EnTypeOfSalaryList.CONGES))
		{
			listOfSalaries = service.find(queryStringListIntervalleConge);
		}
		else if (this.getParameterOfSalary().getEnTypeSalaries().equals(ClsEnumeration.EnTypeOfSalaryList.ELTFIX))
		{
			listOfSalaries = service.find(queryStringListIntervalleEF);
		}
		else if (this.getParameterOfSalary().getEnTypeSalaries().equals(ClsEnumeration.EnTypeOfSalaryList.ELTVAR))
		{
			listOfSalaries = service.find(queryStringListIntervalleEV);
		}
		else if (this.getParameterOfSalary().getEnTypeSalaries().equals(ClsEnumeration.EnTypeOfSalaryList.ELTVAR_FIX))
		{
			listOfSalaries = service.find(queryStringListIntervalleEVF);
		}
		else if (this.getParameterOfSalary().getEnTypeSalaries().equals(ClsEnumeration.EnTypeOfSalaryList.INTERVAL))
		{
			listOfSalaries = service.find(queryStringListIntervalle1);
		}
		else if (this.getParameterOfSalary().getEnTypeSalaries().equals(ClsEnumeration.EnTypeOfSalaryList.LIST))
		{
			listOfSalaries = service.find(queryStringPasaList);
		}
		// if(1==1)
		// return;
		// LOOP
		// IF wsd_fcal1.choix = 'O' THEN
		// FETCH curs_chx_sal INTO wsal01;
		// EXIT WHEN curs_chx_sal%NOTFOUND;
		// ELSE
		// IF w_plus_cas = 'F' THEN
		// FETCH curs_chx_opt1 INTO wsal01;
		// EXIT WHEN curs_chx_opt1%NOTFOUND;
		// ELSIF w_plus_cas = 'V' THEN
		// FETCH curs_chx_opt2 INTO wsal01;
		// EXIT WHEN curs_chx_opt2%NOTFOUND;
		// ELSIF w_plus_cas = 'R' THEN
		// FETCH curs_chx_opt3 INTO wsal01;
		// EXIT WHEN curs_chx_opt3%NOTFOUND;
		// ELSIF w_plus_cas = 'C' THEN
		// FETCH curs_chx_opt4 INTO wsal01;
		// EXIT WHEN curs_chx_opt4%NOTFOUND;
		// ELSE
		// FETCH curs_chx_opt INTO wsal01;
		// EXIT WHEN curs_chx_opt%NOTFOUND;
		// END IF;
		// END IF;

		// ClsSalaryToProcess salaryToProcess = null;
		// ClsInfoSalaryClone infoSalary = null;
		// String queryArrierePaie = "";
		// String queryArrierePaieRetro = "";
		parameterOfSalary.setPaieAuNetMaxIteration(2);
		// ClsParameterOfPay param = null;
		// boolean continuer = true;
		Integer i = 0;
		for (Object obj : listOfSalaries)
		{
			i++;
			if (poolSrev == null)
			{
				// threadWorkQueue = new ThreadPoolExecutor(5, 5, -1, TimeUnit.MICROSECONDS, new ArrayBlockingQueue<Runnable>(5));
				poolSrev = Executors.newFixedThreadPool(threadmax);
			}
			// traiterSalaire((Salarie)obj);
			try
			{
				// if(getNombreThread() > threadmax){
				// this.wait();
				// }
				// threadWorkQueue.execute(new ClsTraiterSalaireThread(this, (Salarie)obj));
				poolSrev.execute(new ClsTraiterSalaireThread(this, (Salarie) obj, i));
				// Thread T1 = new Thread( new ClsTraiterSalaireThread(this, (Salarie)obj));
				// T1.setName(((Salarie)obj).getComp_id().getNmat() + "_" + ((Salarie)obj).getNom());
				// T1.start();
				// setNombreThread(getNombreThread() + 1);
			}
			catch (RejectedExecutionException e)
			{
				e.printStackTrace();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			// infoSalary = new ClsInfoSalaryClone((Salarie)obj);
			// param = parameterOfSalary.clone();
			// salaryToProcess = new ClsSalaryToProcess(param, infoSalary, service);
			// salaryToProcess.setMonthOfPay(parameterOfSalary.getMonthOfPay());
			// salaryToProcess.setPeriodOfPay(parameterOfSalary.getPeriodOfPay());
			// // IF Mode_Test THEN pap_logins('Salarie: ' || wsal01.nmat || '*'); END IF;
			// //
			// // SAVEPOINT Test_Save;
			// //
			// // -- chargement des donnees infos salaries historise
			// // -- dans le cas d'un traitement retro
			// // IF retroactif THEN
			// // PA_CALC_ANX.charg_infos_salh;
			// // END IF;
			// if(parameterOfSalary.isUseRetroactif())
			// salaryToProcess.chargerInfoSalaryHistorique();
			// boolean autopaie = autorPaie(parameterOfSalary.getUti(), parameterOfSalary.getTypu(), ((Salarie)obj).getClas(), parameterOfSalary.getAccess());
			// boolean bulletinbloque = salaryToProcess.bulletinBloque();
			// boolean finArrierePaie = false;
			// // IF PA_PAIE.autorpaie(wsd_fcal1.cdos,w_cuti,3,wsal01.clas,'I') AND
			// // NOT PA_PAIE.Bulletin_Bloque(wsd_fcal1.cdos, wsal01.nmat)
			// // THEN
			// //
			// autopaie = true;
			// if(autopaie && ! bulletinbloque){
			// // IF Mode_Test THEN pap_logins('* Debut Salarie: ' || wsal01.nmat || ' *'); END IF;
			// //
			// // -- Chargement des periodes de paie(Inclue la table 91)
			// // W_Faitout := charg_per_paie;
			// ParameterUtil.println(">> DEBUT chargerPeriodeDePaie");
			// salaryToProcess.chargerPeriodeDePaie();
			// ParameterUtil.println(">> FIN chargerPeriodeDePaie");
			// //
			// // -- Tester si le salarie se trouve deja dans PACALC avec un No de
			// // -- bulletin different.
			// // verif_bul := 0;
			// //
			// // IF NOT destruction_fcal(verif_bul)
			// // THEN
			// // err_msg := PA_PAIE.erreurp('ERR-90043',w_clang);
			// // RETURN FALSE;
			// // END IF;
			// //
			// // -- Pas de calcul mais destruction des enregistrements calcul
			// // IF wsal01.cals = 'O' THEN
			// if("O".equals(((Salarie)obj).getCals())){
			// //
			// // Fin_Arrp := FALSE;
			// finArrierePaie = false;
			// //
			// // -- On ne calcule pas le conges les mois suivants le mois de
			// // -- paiement du conges, ni le dernier mois si il est plein.
			// // -- Cela ne concerne que le Fictif_N et le Fictif_B
			// // IF ( ( w_fictif = 'O' AND w_typ_fictif = 'B' ) OR w_fictif = 'N' )
			// // AND
			// // NOT PA_PAIE.NouB(wsal01.pmcf)
			// // AND
			// // w_aamm > wsal01.pmcf
			// // AND
			// // ( wsal01.dfcf >= w_dfpa )
			// // THEN
			// // Fin_Arrp := TRUE;
			// // END IF;
			// if(("O".equals(parameterOfSalary.getFictiveCalculus())
			// && "B".equals(parameterOfSalary.getFictiveCalculusType())) || "N".equals(parameterOfSalary.getFictiveCalculus())
			// && !ClsObjectUtil.isNull(((Salarie)obj).getPmcf())
			// && parameterOfSalary.getMyMonthOfPay().getYearAndMonth().compareToIgnoreCase(((Salarie)obj).getPmcf()) > 0
			// && ClsObjectUtil.isNull(((Salarie)obj).getDfcf())
			// && salaryToProcess.getLastDayOfMonth().equals(((Salarie)obj).getDfcf())){
			// finArrierePaie = true;
			// }
			// //
			// // -- LH 181297
			// // -- Pas de calcul si salarie absent tout le mois
			// // IF Absent_Tout_Mois(wsal01.nmat,w_aamm,wsd_fcal1.nbul,retroactif,w_ddpa, w_dfpa) AND
			// // PDC_si_ATLM
			// // THEN
			// // Fin_Arrp := TRUE;
			// // END IF;
			// if(salaryToProcess.isAbsentToutMois() && parameterOfSalary.isABSENCE_MOIS_PDC_SI_ATLM()){
			// finArrierePaie = true;
			// }
			// //
			// // -- Lecture des arrets de paie
			// // IF NOT Fin_Arrp THEN
			// // -- Test si traitement retro
			// // IF retroactif THEN
			// // OPEN curs_arp2;
			// // ELSE
			// // OPEN curs_arp;
			// // END IF;
			// // LOOP
			// // IF retroactif THEN
			// // FETCH curs_arp2 INTO warrpai;
			// // EXIT WHEN curs_arp2%NOTFOUND;
			// // ELSE
			// // FETCH curs_arp INTO warrpai;
			// // EXIT WHEN curs_arp%NOTFOUND;
			// // END IF;
			// // IF warrpai.ddar <= w_ddpa AND warrpai.dfar >= w_dfpa THEN
			// // Fin_Arrp := TRUE;
			// // EXIT;
			// // END IF;
			// // END LOOP;
			// // IF retroactif THEN
			// // CLOSE curs_arp2;
			// // ELSE
			// // CLOSE curs_arp;
			// // END IF;
			// // END IF;
			// queryArrierePaie = "from Rhtarrpaiagent" +
			// " where identreprise = '" + this.getParameterOfSalary().getDossier() + "'" +
			// " and nmat = '" + ((Salarie)obj).getComp_id().getNmat() + "'" +
			// " order by identreprise , nmat, ddar";
			//
			// queryArrierePaieRetro = "from SuspensionPaie" +
			// " where identreprise = '" + this.getParameterOfSalary().getDossier() + "'" +
			// " and nmat = '" + ((Salarie)obj).getComp_id().getNmat() + "'" +
			// " and aamm = '" + parameterOfSalary.getMyMonthOfPay().getYearAndMonth() + "'" +
			// " and nbul = " + parameterOfSalary.getNumeroBulletin() +
			// " order by identreprise , nmat, ddar";
			// List listOfArriere = this.parameterOfSalary.isUseRetroactif() ? service.find(queryArrierePaieRetro) : service.find(queryArrierePaie);
			// Date ddar = null;
			// Date dfar = null;
			// for (Object obj1 : listOfArriere) {
			// if(parameterOfSalary.isUseRetroactif()){
			// ddar = ((SuspensionPaie)obj1).getDdar();
			// dfar = ((SuspensionPaie)obj1).getDfar();
			// }
			// else{
			// ddar = ((Rhtarrpaiagent)obj1).getComp_id().getDdar();
			// dfar = ((Rhtarrpaiagent)obj1).getDfar();
			// }
			// if(ddar.compareTo(salaryToProcess.getFirstDayOfMonth()) <= 0 && dfar.compareTo(salaryToProcess.getLastDayOfMonth()) >= 0){
			// finArrierePaie = true;
			// break;
			// }
			// }
			// //
			// // W_Cont := TRUE;
			// // IF Fin_Arrp THEN
			// // W_Cont := FALSE;
			// // DBMS_PIPE.PACK_MESSAGE('I2');
			// // DBMS_PIPE.PACK_MESSAGE(wsal01.nmat||PA_PAIE.erreurp('INF-10345',w_clang));
			// // w_status := DBMS_PIPE.SEND_MESSAGE(w_pipe_name);
			// // ELSE
			// //
			// // -- MM 12/09/2000 flag agent embauche
			// // Embauche := FALSE;
			// if(finArrierePaie){
			// continuer = false;
			// }
			// else
			// salaryToProcess.setEmbauche(false);
			// // IF wsal01.dtes >= w_ddpa AND
			// // wsal01.dtes <= w_dfpa AND
			// // wsd_fcal1.nbul = 9 THEN
			// if(salaryToProcess.getFirstDayOfMonth().compareTo(((Salarie)obj).getDtes()) <= 0
			// && salaryToProcess.getLastDayOfMonth().compareTo(((Salarie)obj).getDtes()) <= 0
			// && parameterOfSalary.getNumeroBulletin() == 9){
			// //
			// // Embauche := TRUE;
			// salaryToProcess.setEmbauche(true);
			// //
			// // END IF;
			// }
			// // -- Fin modif 12/09/2000
			// //
			// // stc := FALSE;
			// parameterOfSalary.setStc(false);
			// //
			// // -- Solde de tous comptes : dernier mois de paie de ce salarie
			// // IF wsal01.mrrx = 'MU' OR wsal01.mrrx = 'RA'
			// // THEN
			// if("MU".equals(((Salarie)obj).getMrrx()) || "RA".equals(((Salarie)obj).getMrrx())){
			// // IF wsal01.dmrr >= w_ddpa AND
			// // wsal01.dmrr <= w_dfpa AND
			// // wsd_fcal1.nbul = 9
			// // THEN
			// if(salaryToProcess.getFirstDayOfMonth().compareTo(((Salarie)obj).getDtes()) <= 0
			// && salaryToProcess.getLastDayOfMonth().compareTo(((Salarie)obj).getDtes()) <= 0
			// && parameterOfSalary.getNumeroBulletin() == 9){
			// // stc := TRUE;
			// parameterOfSalary.setStc(true);
			// // rub_stc := TRUE;
			// parameterOfSalary.setRubriqueStc(true);
			// // IF NOT chg_rubstc(wsal01.mtfr ) THEN
			// // -- MM Pas de controle du param pour le calcul ISR
			// // /*
			// // err_msg := 'Sal: '|| wsal01.nmat|| '. Motif radiation ('||
			// // wsal01.mtfr|| ') inexistant table 23';
			// // W_Faitout := err_calc (err_msg);
			// // w_cont := FALSE;
			// // */
			// // rub_stc := FALSE;
			// // END IF;
			// salaryToProcess.chargerRubriqueMotifFinContrat(((Salarie)obj).getModp());
			// }
			// // ELSE
			// else{
			// // -- MM
			// // -- Test si date de radiation < date debut de paie
			// // -- alors pas de calcul et passage salarie suivant.
			// // IF wsal01.dmrr < w_ddpa AND
			// // NOT Ste_SHELL_GABON
			// // THEN
			// // w_cont := FALSE;
			// // END IF;
			// if(salaryToProcess.getFirstDayOfMonth().compareTo(((Salarie)obj).getDmrr()) > 0
			// && salaryToProcess.getEntreprise().equals(ClsEnumeration.EnEnterprise.SHELL_GABON))
			// continuer = false;
			// }
			// // END IF;
			// }
			// // ELSE
			// else{
			// // -- Test si date de radiation < date debut de paie
			// // -- alors pas de calcul et passage salarie suivant.
			// // IF wsal01.dmrr < w_ddpa AND
			// // NOT Ste_SHELL_GABON
			// // THEN
			// // w_cont := FALSE;
			// // END IF;
			// if(salaryToProcess.getFirstDayOfMonth() != null && ((Salarie)obj).getDmrr() != null &&
			// salaryToProcess.getFirstDayOfMonth().compareTo(((Salarie)obj).getDmrr()) > 0
			// && salaryToProcess.getEntreprise().equals(ClsEnumeration.EnEnterprise.SHELL_GABON))
			// continuer = false;
			// }
			// // END IF;
			// }
			// // END IF;
			// //
			// salaryToProcess.setContinuer(continuer);
			// if(listOfSalaryToProcess == null)
			// listOfSalaryToProcess = new ArrayList<ClsSalaryToProcess>();
			// listOfSalaryToProcess.add(salaryToProcess);
			//
			// // ParameterUtil.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
			// // ParameterUtil.println("listOfSalaryToProcess len :" + listOfSalaryToProcess.size());
			// // for (Object object : listOfSalaryToProcess) {
			// // ClsObjectUtil.displaySalaryProperties((ClsSalaryToProcess)object);
			// // }
			// // IF w_cont THEN
			// if(continuer){
			// // --------------------------------------
			// // -- APPEL DU TRAITEMENT D'UN SALARIE
			// // --------------------------------------
			// // -- o Possibilite de controler le nombre de salaries traites avec celui
			// // -- autorise par la licence
			// // -- o Si il ya eu un probleme lors du calcul sur ce salarie (ex: parametre
			// // -- manquant dans les nomenclatures), l'erreur est consignee dans le fichier
			// // -- log ERRCALCPAIE, les donnees sont remises a l'etat anterieur (ROLLBACK)
			// // -- et le calcul passe au salarie suivant.
			// // -----------------------------------------------------------------------------
			// // pb_calcul := FALSE;
			// // W_Faitout := trait_sal;
			// // if(parameterOfSalary.isUseRetroactif()){
			// // salaryToProcess.buildListOfAllRubriqueRetro();
			// // }
			// // else{
			// // salaryToProcess.buildListOfAllRubrique();
			// // }
			// salaryToProcess.buildListOfRubrique(listOfParubq);
			// salaryToProcess.traitementSalaire();
			// //
			// // IF NOT pb_calcul THEN
			// // ------------------------------------
			// // -- Notification traitement termine
			// // ------------------------------------
			// // nb_sal := nb_sal + 1;
			// // DBMS_PIPE.PACK_MESSAGE('I1');
			// // DBMS_PIPE.PACK_MESSAGE(wsal01.nmat
			// // || LTRIM(TO_CHAR(nb_sal,'09999'))
			// // || 'Traitement Ok');
			// // w_status := DBMS_PIPE.SEND_MESSAGE(w_pipe_name);
			// //
			// // -- Suppression des pseudo-evs generes pour calcul des absences
			// // IF retroactif THEN
			// // DELETE FROM pahevar
			// // where identreprise = wpdos.cdos
			// // AND nmat = wsal01.nmat
			// // AND aamm = w_aamm
			// // AND nbul = wsd_fcal1.nbul
			// // AND argu = 'PSEUDO-EV';
			// // ELSE
			// // DELETE FROM paevar
			// // where identreprise = wpdos.cdos
			// // AND nmat = wsal01.nmat
			// // AND aamm = w_aamm
			// // AND nbul = wsd_fcal1.nbul
			// // AND argu = 'PSEUDO-EV';
			// // END IF;
			// // COMMIT;
			// try{
			// if(parameterOfSalary.isUseRetroactif()){
			// service.deleteFromTable("delete from Rhthevar" +
			// " where identreprise ='" + parameterOfSalary.getDossier() + "'" +
			// " nmat = '" + ((Salarie)obj).getComp_id().getNmat() + "'" +
			// " aamm = '" + parameterOfSalary.getMyMonthOfPay().getYearAndMonth() + "'" +
			// " argu = 'PSEUDO-EV'" +
			// " nbul = " + parameterOfSalary.getNumeroBulletin());
			// }
			// else{
			// service.deleteFromTable("delete from Rhteltvardet" +
			// " where identreprise ='" + parameterOfSalary.getDossier() + "'" +
			// " nmat = '" + ((Salarie)obj).getComp_id().getNmat() + "'" +
			// " aamm = '" + parameterOfSalary.getMyMonthOfPay().getYearAndMonth() + "'" +
			// " argu = 'PSEUDO-EV'" +
			// " nbul = " + parameterOfSalary.getNumeroBulletin());
			// }
			// }catch(Exception e){
			// //logger
			// }
			// // ELSE
			// // DBMS_PIPE.PACK_MESSAGE('I2');
			// // DBMS_PIPE.PACK_MESSAGE(wsal01.nmat||PA_PAIE.erreurp('ERR-90044',w_clang));
			// // w_status := DBMS_PIPE.SEND_MESSAGE(w_pipe_name);
			// //
			// // pb_calcul := FALSE;
			// // -------------
			// // BEGIN
			// // ROLLBACK TO SAVEPOINT Test_Save;
			// // EXCEPTION
			// // WHEN OTHERS THEN NULL;
			// // END;
			// // -------------
			// // W_Faitout := err_calc (err_msg);
			// // COMMIT;
			// //
			// // END IF; -- FIN 'NOT pbcalcul'
			// //
			// }
			// // END IF; -- FIN 'IF w_cont'
			// //
			// // ELSE
			// else{
			// //
			// // IF Mode_Test THEN pap_logins('Salarie a calcul NON: suppression pacalc et pafic'); END IF;
			// //
			// // IF retroactif THEN
			// // DELETE FROM prcalc
			// // where identreprise = wpdos.cdos
			// // AND nmat = wsal01.nmat
			// // AND aamm = w_aamm
			// // AND nbul = wsd_fcal1.nbul
			// // AND nlot = w_nlot;
			// // ELSE
			// // DELETE FROM pacalc
			// // where identreprise = wpdos.cdos
			// // AND nmat = wsal01.nmat
			// // AND aamm = w_aamm
			// // AND nbul = wsd_fcal1.nbul;
			// // END IF;
			// try{
			// salaryToProcess.deleteBulletin();
			// //
			// // IF NOT PA_PAIE.NouB(wsal01.pmcf) AND w_fictif = 'O' AND NOT retroactif THEN
			// // IF wsal01.pmcf = w_aamm THEN
			// // DELETE FROM pafic
			// // where identreprise = wpdos.cdos
			// // AND nmat = wsal01.nmat;
			// // END IF;
			// // END IF;
			// //
			// // COMMIT;
			// if("O".equals(parameterOfSalary.getFictiveCalculus()) && ! parameterOfSalary.isUseRetroactif()){
			// if(parameterOfSalary.getMyMonthOfPay().getYearAndMonth().equals(((Salarie)obj).getPmcf())){
			// service.deleteFromTable("delete from Pafic" +
			// " where identreprise = '"+ parameterOfSalary.getDossier() + "'" +
			// " nmat = '" + ((Salarie)obj).getComp_id().getNmat() + "'");
			// }
			// }
			// }catch(Exception e){
			// //logger
			// }
			// //
			// // DBMS_PIPE.PACK_MESSAGE('I2');
			// // DBMS_PIPE.PACK_MESSAGE(wsal01.nmat||PA_PAIE.erreurp('INF-10344',w_clang));
			// // w_status := DBMS_PIPE.SEND_MESSAGE(w_pipe_name);
			// //
			// }
			// // END IF; -- FIN 'IF wsal1.cals= 'O''
			// //
			// // IF Mode_Test THEN pap_logins('Fin Salarie: ' || wsal01.nmat || '*'); END IF;
			// //
			// }
			// // END IF; -- Fin 'IF autorpaie'
			//
		}
		// END LOOP; -- FIN 'curs_chx_(sal ou opt)'
		if (poolSrev != null)
		{
			poolSrev.shutdown();
		}
		//
		// END trait;
	}

	/**
	 * the purpose is to construct a query
	 * 
	 * @return
	 */
	private String getFinalEmployeeList(String startMatricule, String endMatricule, String startNiv1, String endNiv1, String startNiv2, String endNiv2, String startNiv3, String endNiv3)
	{
		String res = "";
		if (res != null)
		{
			// matricule
			if (endMatricule != null && endMatricule.length() > 0)
			{
				if (startMatricule != null && startMatricule.length() > 0)
				{
					res = res.concat(" and nmat between '" + startMatricule);
					res = res.concat("' and '" + endMatricule + "'");
				}
			}
			else if (startMatricule != null && startMatricule.length() > 0)
			{
				res = res.concat(" and nmat = '" + startMatricule);
			}
			// niv1
			if (endNiv1 != null && endNiv1.trim().length() > 0)
			{
				if (startNiv1 != null && startNiv1.trim().length() > 0)
				{
					res = res.concat(" and niv1 between '" + startNiv1);
					res = res.concat("' and '" + endNiv1 + "'");
				}
			}
			else if (startNiv1 != null && startNiv1.trim().length() > 0)
			{
				res = res.concat(" and niv1 = '" + startNiv1);
			}
			// niv2
			if (endNiv2 != null && endNiv2.trim().length() > 0)
			{
				if (startNiv2 != null && startNiv2.trim().length() > 0)
				{
					res = res.concat(" and niv2 between '" + startNiv2);
					res = res.concat("' and '" + endNiv2 + "'");
				}
			}
			else if (startNiv2 != null && startNiv2.trim().length() > 0)
			{
				res = res.concat(" and niv2 = '" + startNiv2);
			}
			// niv3
			if (endNiv3 != null && endNiv3.trim().length() > 0)
			{
				if (startNiv3 != null && startNiv3.trim().length() > 0)
				{
					res = res.concat(" and niv3 between '" + startNiv3);
					res = res.concat("' and '" + endNiv3 + "'");
				}
			}
			else if (startNiv3 != null && startNiv3.trim().length() > 0)
			{
				res = res.concat(" and niv3 = '" + startNiv3);
			}
		}
		ParameterUtil.println("res :" + res);
		// return a query to get employees
		return res;
	}

	/**
	 * the purpose is to construct a query
	 * 
	 * @return
	 */
	private String getFinalEmployeeList(String startNiv1, String endNiv1, String startNiv2, String endNiv2, String startNiv3, String endNiv3)
	{
		String res = "";
		if (res != null)
		{
			// niv1
			if (endNiv1 != null && endNiv1.trim().length() > 0)
			{
				if (startNiv1 != null && startNiv1.trim().length() > 0)
				{
					res = res.concat(" and niv1 between '" + startNiv1);
					res = res.concat("' and '" + endNiv1 + "'");
				}
			}
			else if (startNiv1 != null && startNiv1.trim().length() > 0)
			{
				res = res.concat(" and niv1 = '" + startNiv1);
			}
			// niv2
			if (endNiv2 != null && endNiv2.trim().length() > 0)
			{
				if (startNiv2 != null && startNiv2.trim().length() > 0)
				{
					res = res.concat(" and niv2 between '" + startNiv2);
					res = res.concat("' and '" + endNiv2 + "'");
				}
			}
			else if (startNiv2 != null && startNiv2.trim().length() > 0)
			{
				res = res.concat(" and niv2 = '" + startNiv2);
			}
			// niv3
			if (endNiv3 != null && endNiv3.trim().length() > 0)
			{
				if (startNiv3 != null && startNiv3.trim().length() > 0)
				{
					res = res.concat(" and niv3 between '" + startNiv3);
					res = res.concat("' and '" + endNiv3 + "'");
				}
			}
			else if (startNiv3 != null && startNiv3.trim().length() > 0)
			{
				res = res.concat(" and niv3 = '" + startNiv3);
			}
		}
		// return a query to get employees
		return res;
	}

	public synchronized void incrementerNombreSalariesTraite()
	{
		ClsTraiterSalaireThread.NBRE_AGENT_TRAITE++;
		nbrSalariesTraite++;
//		if (ui != null)
//			ui.mettreAJourProgressBar(nbrSalariesTraite);
//
//		if (lanceur != null)
//			lanceur.setNbrTraite(nbrSalariesTraite);
//		if(httpSession != null) httpSession.setAttribute(ClsSessionObjectName.SESSION_O_CALCUL_NOMBRE_SALARIES_TRAITE, nbrSalariesTraite);
		
	}
	
	public  Map<String,ParamCalculExterne> mapParamCalculExterne;
	
	String idBeanCalculExterne = null;
	String nomMethodeCalculExterne = null;
	String colonneParamCalculExterne = null;
	
	public void initCalculExterne(String dossier) throws Exception
	{
		mapParamCalculExterne = new HashMap<String, ParamCalculExterne>();
		//ApplicationContext app = ServiceFinder.applicationContext;

		String sqlQ = "Select a.vall as idBean, c.vall as methodeCalcul, d.vall as colonneParam ";
		sqlQ+=" From ParamData a, ParamData b, ParamData c, ParamData d where a.cdos='" + dossier + "' and a.ctab=99 and a.nume =1 and a.cacc='CALC_EXT' ";
		sqlQ+=" and a.cdos=c.cdos and a.ctab=c.ctab and a.cacc=c.cacc and c.nume = 2";
		sqlQ+=" and a.cdos=d.cdos and a.ctab=d.ctab and a.cacc=d.cacc and d.nume = 3";

		List<Object[]> fonctions = this.getService().find(sqlQ);
		idBeanCalculExterne = null;
		nomMethodeCalculExterne = null;
		colonneParamCalculExterne = null;

		try
		{
			if(!fonctions.isEmpty())
			{
			    Object[] o = fonctions.get(0);
				idBeanCalculExterne = (String) o[0];
				nomMethodeCalculExterne = (String) o[1];
				colonneParamCalculExterne = (String) o[2];
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
	}
	
	public void calculExterne(ClsSalaryToProcess salary) throws Exception
	{	
		//ApplicationContext app = ServiceFinder.applicationContext;
		Object obj = null;
		Method oMethod = null;
		try
		{
			try
			{
				if (StringUtils.isNotBlank(idBeanCalculExterne) && StringUtils.isNotBlank(nomMethodeCalculExterne) && StringUtils.isNotBlank(colonneParamCalculExterne))
				{
					//obj = app.getBean(idBeanCalculExterne);
					if (obj != null)
					{
						oMethod = obj.getClass().getMethod(nomMethodeCalculExterne, ClsSalaryToProcess.class, Map.class, String.class);
						if (oMethod != null)
						{
							oMethod.invoke(obj, salary, getMapParamCalculExterne(), colonneParamCalculExterne);
						}
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw e;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
	}
	

	public synchronized Integer getNbrSalariesTraite()
	{
		return nbrSalariesTraite;
	}

	public CalculPaieServiceImpl getLanceur()
	{
		return lanceur;
	}

	public void setLanceur(CalculPaieServiceImpl lanceur)
	{
		this.lanceur = lanceur;
	}

	public  Map<String, ParamCalculExterne> getMapParamCalculExterne()
	{
		return mapParamCalculExterne;
	}

	public void setMapParamCalculExterne(Map<String, ParamCalculExterne> mapParamCalculExterne)
	{
		this.mapParamCalculExterne = mapParamCalculExterne;
	}
}
