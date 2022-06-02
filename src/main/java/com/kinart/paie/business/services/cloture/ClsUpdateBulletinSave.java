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
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Session;

public class ClsUpdateBulletinSave implements IFutureListener<Boolean>
{
	ClsGlobalUpdate globalUpdate;
	
	static CountDownLatch latch;

	ExecutorService threadPool;

	Session oSession = null;

	// List<ClsAgentUpdateBulletin> llagent = null;

	boolean finalResult = true;

	private HttpServletRequest request;

	protected String user = "";

	protected String dossier = "";

	protected int numerobulletin = 0;

	protected String periode = "";

	protected String langue = "";

	protected String dateformat = "";

	protected GeneriqueConnexionService service = null;

	//
	protected ClsBulletin bulletin = null;

	protected ClsParameterOfPay parameter = null;

	//
	protected ClsDate myPeriode = null;

	protected String rubriqueNbreJourTravail = "";

	protected String rubriqueNbreJourPlage = "";

	protected boolean tab91 = true;

	protected boolean tab91b = true;

	protected int nb_t91 = 0;

	protected String moisPaieCourant = null;

	protected String mois_ms = "";

	protected int nbul_ms = 0;

	protected List<ClsInfoPeriode> listOfPeriodeT91 = null;

	protected List listOfRubriqueCon = null;

	// protected ClsDate myMoisPaieCourant = null;
	protected String w_am99 = "";

	protected int compt_sal = 0;

	protected boolean calc_part_auto = false;

	protected int max_part_fisc = 0;

	protected String w_motif_reliq = "";

	protected String rub_pnp = "";

	protected String val_exp = "";

	protected int typ_rec = 0;

	protected String w_fictif = "";

	protected String w_typ_fictif = "";

	protected int nbjc_lx = 0;

	protected int nbjc_ex = 0;

	protected int nbbul = 0;

	protected String editMatricule = "";

	protected String[] t_ev_mc = null;

	protected String[] t_ev_ms = null;

	protected String rub_bc = "";

	protected String calcul_nbjsup = "";

	protected String ajout_bc = "";

	protected String pror_jr = "";

	protected String w_cg_abs = "";

	protected String raz_jour = "";

	protected String w_brut = "";

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

	protected int w_type_dtanniv = 0;

	protected boolean t91hc = false;

	protected boolean t91hp = false;

	protected boolean t91mc = false;

	protected boolean t91mp = false;

	protected boolean t91in = false;

	protected int nbrcon = 0;

	protected String prochainmois = "";

	protected String w_cas = "";

	String error = "";

	protected double bas_con = 0;

	protected double w_moncp = 0;

	protected int w_nbjcp = 0;

	protected double w_conge = 0;

	protected int w_nbjtr = 0;

	protected boolean cgan_ms = false;

	protected boolean exist_evcg = false;

	protected int nb_evcg = 0;

	//
	protected int w_nbjreliq = 0;

	//
	protected boolean mois_conge = false;

	protected boolean sal_RA_MU = false;

	protected boolean sal_RA_MU_depuis = false;

	protected boolean w_continue = false;

	protected boolean w_retour = false;

	protected String w_am = "";

	protected boolean conges_annuels = false;

	protected boolean finconge = false;

	protected boolean pnp = false;

	// pour calc_nb_jours
	protected int wnbj_a = 0;

	protected int wnbj_c = 0;

	protected Date dtDdmp = null;

	protected Date dtDdex = null;

	protected DossierPaie rhpdo = null;

	protected boolean ca = false;

	protected int nbj_con = 0;

	protected String sav_pmcf = "";

	protected Date sav_dfcf = null;

	protected Date sav_ddcf = null;

	protected Date w_date_anniv = null;

	protected int nbj_enf = 0;

	protected int nbj_anc = 0;

	protected int nbj_deco = 0;

	protected int ancien = 0;

	protected int minimum_enfant = 0;

	protected int age_max_fiscal = 0;

	protected String nap = "";

	public Map<String, ElementSalaire> rubriquesMap = new HashMap<String, ElementSalaire>();

	public Map<String, ParamData> rhfnomsMap = new HashMap<String, ParamData>();

	public HttpServletRequest getRequest()
	{
		return request;
	}

	public void setRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	public synchronized String getDateformat()
	{
		return dateformat;
	}

	public synchronized void setDateformat(String dateformat)
	{
		this.dateformat = dateformat;
	}

	public synchronized ExecutorService getThreadPool()
	{
		return threadPool;
	}

	public synchronized void setThreadPool(ExecutorService threadPool)
	{
		this.threadPool = threadPool;
	}

	public synchronized ClsParameterOfPay getParameter()
	{
		return parameter;
	}

	public synchronized void setParameter(ClsParameterOfPay parameter)
	{
		this.parameter = parameter;
	}

	public synchronized GeneriqueConnexionService getService()
	{
		return service;
	}

	public synchronized void setService(GeneriqueConnexionService service)
	{
		this.service = service;
	}

	public synchronized String getLangue()
	{
		return langue;
	}

	public synchronized void setLangue(String langue)
	{
		this.langue = langue;
	}

	public synchronized String getPeriode()
	{
		return periode;
	}

	public synchronized void setPeriode(String periode)
	{
		this.periode = periode;
	}

	public synchronized int getNumerobulletin()
	{
		return numerobulletin;
	}

	public synchronized void setNumerobulletin(int numerobulletin)
	{
		this.numerobulletin = numerobulletin;
	}

	public synchronized String getDossier()
	{
		return dossier;
	}

	public synchronized void setDossier(String dossier)
	{
		this.dossier = dossier;
	}

	public synchronized String getUser()
	{
		return user;
	}

	public synchronized void setUser(String user)
	{
		this.user = user;
	}

	public synchronized ClsBulletin getBulletin()
	{
		return bulletin;
	}

	public synchronized void setBulletin(ClsBulletin bulletin)
	{
		this.bulletin = bulletin;
	}

	public synchronized boolean isFinalResult()
	{
		return finalResult;
	}

	public synchronized void setFinalResult(boolean finalResult)
	{
		this.finalResult = finalResult;
	}

	public ClsUpdateBulletinSave()
	{

	}

	public ClsUpdateBulletinSave(ClsParameterOfPay parameter, GeneriqueConnexionService service, String dossier, String user, String periode, String langue, int numerobulletin, String dateformat)
	{
		this.user = user;
		this.dossier = dossier;
		this.numerobulletin = numerobulletin;
		this.periode = periode;
		this.langue = langue;
		this.service = service;
		this.parameter = parameter;
		//
		this.bulletin = new ClsBulletin(service);
		this.dateformat = dateformat;
	}

	//
	public void execute(Session oSession)
	{
		ParameterUtil.println(">>execute");
		// i SMALLINT;
		// j SMALLINT;
		// w_date DATE;
		//
		// w_chaine VARCHAR2(200);
		// w_id INTEGER;
		// w_exec INTEGER;
		//
		//
		// BEGIN
		// -- DBMS_OUTPUT.ENABLE(50000);
		//
		// -- LH 261198 w_chaine := 'ALTER ROLLBACK SEGMENT R05 OFFLINE';
		// /*
		// BEGIN
		// w_chaine := 'ALTER ROLLBACK SEGMENT R05 ONLINE';
		// w_id := DBMS_SQL.OPEN_CURSOR;
		// DBMS_SQL.PARSE(w_id,w_chaine,DBMS_SQL.V7);
		// w_exec := DBMS_SQL.EXECUTE(w_id);
		// DBMS_SQL.CLOSE_CURSOR(w_id);
		// EXCEPTION
		// WHEN OTHERS THEN NULL;
		// END;
		// */
		//
		// BEGIN
		// SET TRANSACTION USE ROLLBACK SEGMENT R05;
		// EXCEPTION
		// WHEN OTHERS THEN NULL;
		// END;
		//
		//
		// -- Recuperation des donnees du lanceur.
		// wpdos.cdos := SUBSTR(l_options,1,2);
		// w_aamm := SUBSTR(l_options,3,6);
		// w_nbul := TO_NUMBER(SUBSTR(l_options,9,1));
		//
		// -- MM 10/2003 recuperation du code user par le l_options
		// w_cuti_app := SUBSTR(l_options,10,4);
		// -- Fin modif
		//
		// w_pipe_name := l_pipe_name;
		//
		// BEGIN
		// SELECT * INTO wpdos FROM pados
		// WHERE cdos = wpdos.cdos;
		// EXCEPTION
		// WHEN NO_DATA_FOUND THEN null;
		// END;
		//
//		 List l = service.find("from Rhpdo where idEntreprise = '" + dossier + "'");
//		 // Date dtDdmp = null;
//		 // Date dtDdex = null;
////		 myMoisPaieCourant = null;
////		 moisPaieCourant = null;
//		 if(l != null && l.size() > 0){
//		 rhpdo = (Rhpdo)l.get(0);
//		 dtDdmp = rhpdo.getDdmp();
//		 dtDdex = rhpdo.getDdex();
//		 ClsDate myMoisPaieCourant = new ClsDate(dtDdmp);
//		 moisPaieCourant = myMoisPaieCourant.getYearAndMonth();
//		 }
		
		List l = service.find("from DossierPaie where idEntreprise = '" + dossier + "'");
		// Date dtDdex = null;
		// myMoisPaieCourant = null;
		moisPaieCourant = null;
		if (l != null && l.size() > 0)
		{
			rhpdo = (DossierPaie) l.get(0);
			dtDdmp = rhpdo.getDdmp();
			dtDdex = rhpdo.getDdex();
			// myMoisPaieCourant = new ClsDate(dtDdmp);
			//moisPaieCourant = myMoisPaieCourant.getYearAndMonth();
			if(dtDdmp != null)
				moisPaieCourant = new ClsDate(dtDdmp, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getDateS();
		}
//		myPeriode = new ClsDate(periode, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
		// w_am99 := to_char(wpdos.ddex,'YYYY')||'99';
		w_am99 = new ClsDate(dtDdex).getYear() + "99";
		
		// w_clang := PA_PAIE.rec_clang(wpdos.cdos,w_cuti_app);
		//
		//
		// j := TO_NUMBER(SUBSTR(w_aamm,5,2)) + 1;
		// i := TO_NUMBER(SUBSTR(w_aamm,1,4));
		// IF j > 12 THEN
		// j := 1;
		// i := i + 1;
		// END IF;
		// prochain_mois := ltrim(to_char(i,'0999'))||ltrim(to_char(j,'09'));
		myPeriode = new ClsDate(periode, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
		int i = NumberUtils.toInt(StringUtil.substring(periode, 0,4)); // myPeriode.getMonth();
		int j = NumberUtils.toInt(StringUtil.substring(periode, 4,6)) + 1;
		if (j > 12)
		{
			j = 1;
			i++;
		}
		prochainmois = ClsStringUtil.formatNumber(i, "0000") + ClsStringUtil.formatNumber(j, "00");
		//
		// -- Bulletin et Mois suivant
		// PA_PAIE.prochaine_paie(wpdos.cdos, TO_DATE(w_aamm,'YYYYMM'),w_nbul,
		// w_date, nbul_ms);
		ClsBulletin.InfoBulletin infoBulletin = bulletin.prochainePaie(dossier, periode, numerobulletin);//bulletin.premierePeriode(dossier, periode);
		// mois_ms := TO_CHAR(w_date,'YYYYMM');
		//mois_ms = new ClsDate(new ClsDate(periode, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).addMonth(1),ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getDateS();//infoBulletin.getPeriodepaie();
		mois_ms = infoBulletin.getPeriodepaie();
		nbul_ms = infoBulletin.getNbul();
		//
		// IF init THEN
		// IF NOT majbul THEN
		// ROLLBACK;
		// w_cas := 'E';
		// ELSE
		// COMMIT;
		// w_cas := 'F';
		// w_mess := 'TRAITEMENT TERMINE AVEC SUCCES...';
		// END IF;
		// ELSE
		// w_cas := 'E';
		// END IF;
		// if(initialize(numerobulletin)){
		
		

		this.UpdateToString();
		
		threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);

		String queryString = "from Salarie" + " where idEntreprise = '" + dossier + "'" + " order by nmat";
		List lagent = service.find(queryString);
		if (lagent.isEmpty())
			ClsUpdateBulletinSave.latch = new CountDownLatch(0);
		else
			ClsUpdateBulletinSave.latch = new CountDownLatch(lagent.size());
		Salarie agent = null;
		ClsAgentUpdateTask task = null;
		//		for (Object obj : lagent)
		//		{
		//			agent = (Rhpagent) obj;
		//			globalUpdate._setEvolutionTraitement(request, "Mise � jour du salari� "+agent.getNmat()+" : "+agent.getNom()+" "+agent.getPren(), false);
		//			task = new ClsAgentUpdateTask(request, user, this, service, parameter, dateformat, agent);
		//			task.setBulletin(bulletin);
		//			//
		//			startTask(task);
		//		}
		for (int k = 1; k <= lagent.size(); k++)
		{
			agent = (Salarie) lagent.get(k - 1);
			//task = new ClsAgentUpdateTask(request, user, this, service,oSession, parameter, dateformat, agent);
			//task.setBulletin(bulletin);
			task.numsalariecourant = k;
			task.nbsalarietotal = lagent.size();
			//
			startTask(task);
			
			//ParameterUtil.println("--------------------------------Informations du salarie "+agent.getNmat()+" "+agent.getNom()+" "+agent.getPren()+"------------------------------------");
			//this.UpdateToString();
			//ParameterUtil.println("--------------------------------Fin Informations du salarie "+agent.getNmat()+" "+agent.getNom()+" "+agent.getPren()+"--------------------------------");
		}
		//
		try
		{
			latch.await(); // wait until all the tasks have been terminated
			if (!finalResult)
			{
				// tx.rollback();
				w_cas = "E";
			}
			else
			{
				w_cas = "F";
				error = "TRAITEMENT TERMINE AVEC SUCCES...";
			}
			// oSession.flush();
			// oSession.clear();
		}
		catch (InterruptedException iex)
		{
			iex.printStackTrace();
		}
		// }
		// else{
		// w_cas = "E";
		// }
		// DBMS_PIPE.PACK_MESSAGE(w_cas);
		// DBMS_PIPE.PACK_MESSAGE(w_mess);
		// w_status := DBMS_PIPE.SEND_MESSAGE(w_pipe_name);
		//
		// EXCEPTION
		// WHEN OTHERS THEN
		// ROLLBACK;
		// DBMS_PIPE.PACK_MESSAGE('E');
		// --INSERT INTO palog
		// -- VALUES (wpdos.cdos, 'MAJB', sysdate,
		// -- ' Erreur ' || SUBSTR(SQLERRM, 1, 90) );
		// DBMS_PIPE.PACK_MESSAGE('Fin anormale de traitement : '
		// ||SUBSTR(SQLERRM, 1, 90));
		// w_status := DBMS_PIPE.SEND_MESSAGE(w_pipe_name);
		//
		// END lmajbul;
	}

	private FutureTalker<Boolean> startTask(ClsAgentUpdateTask task)
	{
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
			// latch.countDown();
			return futureTalker;
		}
	}

	// Listener methods
	/**
	 * Called on normal completion A call to this method implies the task's run() method has exited.
	 */
	public void futureResult(Boolean result, FutureTalker<Boolean> talker)
	{
		ClsAgentUpdateTask task = (ClsAgentUpdateTask) talker.getCallable();
		// startTask();
		if (task.numsalariecourant == task.nbsalarietotal)
		{
			globalUpdate._setEvolutionTraitement(request, "Mise � jour termin�e avec succ�s", false);
			globalUpdate._setTitreEvolutionTraitement(request, "");
		}
		else
			globalUpdate._setEvolutionTraitement(request, "Mise � jour du salari� " + task.getAgent().getNmat() + " : " + task.getAgent().getNom() + " " + task.getAgent().getPren() + " (" + task.numsalariecourant + "/" + task.nbsalarietotal + ")", false, task.numsalariecourant,
					task.nbsalarietotal);
		synchronized (latch)
		{
			latch.countDown();
			if (!result)
			{// si le result=false alors arr�ter tout!
				finalResult = result;
				// for(int i =0; i < latch.getCount(); i++)
				// latch.countDown();
				ParameterUtil.println("Le traitement de MAJ du matricule " + task.getAgent().getNmat() + "=>" + task.getAgent().getNom() + "  s'est d�roul� en renvoyant false. ");
			}
			else
			{
				ParameterUtil.println("Le traitement de MAJ du matricule " + task.getAgent().getNmat() + "=>" + task.getAgent().getNom() + "  s'est bien d�roul�. ");
			}
		}
	}

	/**
	 * Called if cancelled Note: You need to provide a rejecthandler and override ShutDownNow if you want this to be called on rejection and shutdown. A call to this method does NOT imply the task's
	 * call() method ever been called.
	 */
	public void futureCancelled(CancellationException t, FutureTalker<Boolean> talker)
	{
		ClsAgentUpdateTask task = (ClsAgentUpdateTask) talker.getCallable();
		ParameterUtil.println("Le traitement de MAJ du matricule " + task.getAgent().getNmat() + "=>" + task.getAgent().getNom() + "  s'est mal d�roul�. " + t.getClass().getName() + "  Message:" + t.getMessage());
		t.printStackTrace();
		// startTask();
		finalResult = false;
		latch.countDown();
		// synchronized (latch) {
		// FutureTalker.shutdownNow(threadPool);
		// for(int i =0; i < latch.getCount(); i++)
		// latch.countDown();
		// }
	}

	/**
	 * Called on error and passed the throwable that was wrapped in ExecutionException A call to this method implies the task's run() method has exited.
	 */
	public void futureError(Throwable t, FutureTalker<Boolean> talker)
	{
		ClsAgentUpdateTask task = (ClsAgentUpdateTask) talker.getCallable();
		String msg = t.getMessage();
		ParameterUtil.println("Le traitement de MAJ du matricule " + task.getAgent().getNmat() + "=>" + task.getAgent().getNom() + "  a �t� stopp� de suite d'une erreur. " + t.getClass().getName() + "  Message:" + t.getMessage());
		t.printStackTrace();
		// //start another one
		// startTask();
		finalResult = false;
		latch.countDown();
		// synchronized (latch) {
		// FutureTalker.shutdownNow(threadPool);
		// for(int i =0; i < latch.getCount(); i++)
		// latch.countDown();
		// }
	}

	private Map<String, Object[]> listOfTable99Map = null;

	/**
	 * chargement des valeurs T99 pour ne pas aller chercher dans la base lors de l'initialisation des param�tres de calcul
	 * 
	 */
	private void buildTableT51T52T99Map()
	{

		listOfTable99Map = new HashMap<String, Object[]>();
		List l = service.find("from ParamData where idEntreprise = '" + dossier + "'  and ctab in (91, 99)"
				+ " and cacc in('RUBNAP', 'RUBNBJTR', 'CALCPART', 'CGRELIQ', 'NBJPNP', 'EXPAT', 'FICTIF', 'NBJC-DEF', 'BASE-CONGE', 'RUBBRUT', 'PROR-JCG', 'PRJ-BARCG', 'ANNIV-CG', 'HC', 'HP', 'MC', 'MP', 'IN')");
		//
		ParamData o1 = null;
		Object[] t99 = null;
		String key = "";
		for (Object object : l)
		{
			t99 = new Object[4];
			// map.put(crub, value)
			o1 = (ParamData) object;
			t99[0] = o1.getNume();
			t99[1] = o1.getVall();
			t99[2] = o1.getValm();
			t99[3] = o1.getValt();
			key = o1.getCtab() + o1.getCacc() + o1.getNume();
			//
			listOfTable99Map.put(key.trim(), t99);
		}
	}

	public boolean initialize(int nbul)
	{
		ParameterUtil.println(">>initialize parameters of clsupdatebulletin");
		// charge les donn�es des tables T99, T91
		buildTableT51T52T99Map();
		// nbbul INTEGER ;
		// poursuite SMALLINT;
		// dosreg parubq.cdos%TYPE;
		// rubreg parubq.crub%TYPE;
		// rconreg parubq.rcon%TYPE;
		// edi_mat pasa01.nmat%TYPE;
		//
		// CURSOR curs_rub_reg IS
		// SELECT cdos , crub , rcon
		// FROM parubq
		// WHERE cdos = wpdos.cdos
		// AND rreg = 'O'
		// AND rman = 'N'
		// ORDER BY cdos , crub;
		String queryRubrique = "select cdos, crub, rcon from Rhprubrique where idEntreprise = '" + dossier + "' and rreg = 'O'" + " and rman = 'N' order by cdos, crub";
		//
		//
		// ---- MM 28/09/2000 on ne controle pas les bulletins vierge = toute rub = zero
		// mat_exist pasa01.nmat%TYPE;
		// CURSOR curs_bull_exist IS
		// SELECT a.nmat
		// FROM pacalc a, parubq b
		// WHERE a.cdos = wpdos.cdos
		// AND a.aamm = w_aamm
		// AND a.nmat = edi_mat
		// AND a.nbul = w_nbul
		// AND a.rubq != w_nap
		// AND b.cdos = a.cdos
		// AND b.crub=a.rubq
		// AND b.prbul !=0
		// AND (b.ednul = 'O' OR NVL(a.mont,0) != 0);
		// -- Fin MM 28/09/2000
		// String editMatricule = "";
		// String nap = "";

		//
		// BEGIN
		// -- Lecture de la rubrique du net a payer table 99
		// BEGIN
		// SELECT valm INTO wfnom.mnt1
		// FROM pafnom
		// WHERE cdos = wpdos.cdos
		// AND ctab = 99
		// AND cacc = 'RUBNAP'
		// AND nume = 1;
		// EXCEPTION
		// WHEN NO_DATA_FOUND THEN NULL;
		// END;
		//
		// IF SQL%NOTFOUND THEN
		// w_mess := PA_PAIE.erreurp('INF-10116',w_clang);
		// RETURN FALSE;
		// ELSE
		// w_nap := ltrim(to_char(wfnom.mnt1,'0999'));
		// END IF;
		String key = "99RUBNAP1";
		Object obj = listOfTable99Map.get(key);
		if (obj == null)
		{
			error = parameter.errorMessage("INF-10116", langue);
			ParameterUtil.println(error);
			globalUpdate._setEvolutionTraitement(request, error, true);
			finalResult = false;
			return false;
		}
		else
		{
			nap = ClsStringUtil.formatNumber(((Object[]) obj)[2] instanceof BigDecimal ? (BigDecimal) ((Object[]) obj)[2] : (Long) ((Object[]) obj)[2], "0000");
		}
		//
		// -- Lecture de la rubrique du nombre de jours travailles
		// BEGIN
		// SELECT valm, vall INTO wfnom.mnt1, nbj_pl
		// FROM pafnom
		// WHERE cdos = wpdos.cdos
		// AND ctab = 99
		// AND cacc = 'RUBNBJTR'
		// AND nume = 1;
		// EXCEPTION
		// WHEN NO_DATA_FOUND THEN NULL;
		// END;
		//
		// IF SQL%NOTFOUND THEN
		// w_mess := PA_PAIE.erreurp('ERR-90030',w_clang);
		// RETURN FALSE;
		// ELSE
		// rub_nbjt := ltrim(to_char(wfnom.mnt1,'0999'));
		// IF PA_PAIE.NouB(nbj_pl) THEN
		// nbj_pl := 'M';
		// END IF;
		// END IF;
		key = "99RUBNBJTR1";
		obj = listOfTable99Map.get(key);
		if (obj == null)
		{
			error = parameter.errorMessage("ERR-90030", langue);
			ParameterUtil.println(error);
			globalUpdate._setEvolutionTraitement(request, error, true);
			finalResult = false;
			return false;
		}
		else
		{
			rubriqueNbreJourTravail = ClsStringUtil.formatNumber(((Object[]) obj)[2] instanceof BigDecimal ? (BigDecimal) ((Object[]) obj)[2] : (Long) ((Object[]) obj)[2], "0000");
			if (((Object[]) obj)[1] != null)
				rubriqueNbreJourPlage = (String) ((Object[]) obj)[1];
			if (ClsObjectUtil.isNull(rubriqueNbreJourPlage))
				rubriqueNbreJourPlage = "M";
		}
		//
		//
		// -- Lecture si code calcul automatique des parts fiscal.
		// calc_part_auto := FALSE;
		// BEGIN
		// SELECT SUBSTR(vall,1,1) INTO wfnom.lib1
		// FROM pafnom
		// WHERE cdos = wpdos.cdos
		// AND ctab = 99
		// AND cacc = 'CALCPART'
		// AND nume = 5;
		// EXCEPTION
		// WHEN NO_DATA_FOUND THEN wfnom.lib1 := 'N';
		// END;
		//
		// IF SQL%NOTFOUND THEN
		// wfnom.lib1 := 'N';
		// END IF;
		// IF wfnom.lib1 = 'O' THEN
		// calc_part_auto := TRUE;
		// END IF;
		String tmplib1 = "N";
		// boolean calc_part_auto = false;
		key = "99CALCPART5";
		obj = listOfTable99Map.get(key);
		if (obj != null)
		{
			if (((Object[]) obj)[1] != null)
				tmplib1 = (String) ((Object[]) obj)[1];
		}
		if ("O".equals(tmplib1))
			calc_part_auto = true;
		//
		// IF calc_part_auto THEN
		//
		// 	BEGIN
		// 		SELECT valt INTO max_part_fisc
		// 		FROM pafnom
		// 		WHERE cdos = wpdos.cdos
		// 		AND ctab = 99
		// 		AND cacc = 'CALCPART'
		// 		AND nume = 2;
		// 		EXCEPTION
		// 		WHEN NO_DATA_FOUND THEN max_part_fisc := 0;
		// 	END;
		//
		// 	IF SQL%NOTFOUND OR PA_PAIE.NouZ(max_part_fisc) THEN
		// 		max_part_fisc := 0;
		// 	END IF;
		//
		// END IF;
		// int max_part_fisc = 0;
		if (calc_part_auto)
		{
			key = "99CALCPART2";
			obj = listOfTable99Map.get(key);
			if (obj != null)
			{
				max_part_fisc = (((Object[]) obj)[2] instanceof BigDecimal ? (BigDecimal) ((Object[]) obj)[2] : (Long) ((Object[]) obj)[2]).intValue();
			}
		}
		//
		// -- Motif de conge reliquat
		// BEGIN
		// SELECT SUBSTR(vall,1,2) INTO w_motif_reliq
		// FROM pafnom
		// WHERE cdos = wpdos.cdos
		// AND ctab = 99
		// AND cacc = 'CGRELIQ'
		// AND NUME = 2;
		// EXCEPTION
		// WHEN NO_DATA_FOUND THEN null;
		// END;
		// IF SQL%NOTFOUND THEN
		// w_motif_reliq := null;
		// END IF;
		//		if (calc_part_auto)
		//		{
		key = "99CGRELIQ2";
		obj = listOfTable99Map.get(key);
		if (obj != null && ((Object[]) obj)[1] != null)
		{
			w_motif_reliq = (String) ((Object[]) obj)[1];
			w_motif_reliq = StringUtil.substring(w_motif_reliq, 0, 2);
		}
		//		}
		//
		//
		// -- Lecture de la rubrique du nombre de jours de conges payes non pris
		// BEGIN
		// SELECT valm INTO wfnom.mnt1
		// FROM pafnom
		// WHERE cdos = wpdos.cdos
		// AND ctab = 99
		// AND cacc = 'NBJPNP'
		// AND nume = 1;
		// EXCEPTION
		// WHEN NO_DATA_FOUND THEN null;
		// END;
		//
		// IF SQL%NOTFOUND THEN
		// w_mess := PA_PAIE.erreurp('ERR-90028',w_clang);
		// RETURN FALSE;
		// ELSE
		// rub_pnp := ltrim(to_char(wfnom.mnt1,'0999'));
		// END IF;
		// String rub_pnp = "";
		key = "99NBJPNP1";
		obj = listOfTable99Map.get(key);
		if (obj == null)
		{
			error = parameter.errorMessage("ERR-90028", langue);
			ParameterUtil.println(error);
			globalUpdate._setEvolutionTraitement(request, error, true);
			finalResult = false;
			return false;
		}
		else
		{
			rub_pnp = ClsStringUtil.formatNumber(((Object[]) obj)[2] instanceof BigDecimal ? (BigDecimal) ((Object[]) obj)[2] : (Long) ((Object[]) obj)[2], "0000");
		}
		//
		// -- Comment connaitre si le salarie est un expatrie
		// -- Libelle 5 = Valeur si c'est un expatrie
		// -- Montant 1 = 1 : Regime, 2 : Type de contrat, 3 : Classe salarie
		// BEGIN
		// SELECT vall INTO val_exp
		// FROM pafnom
		// WHERE cdos = wpdos.cdos
		// AND ctab = 99
		// AND cacc = 'EXPAT'
		// AND NUME = 5;
		// EXCEPTION
		// WHEN NO_DATA_FOUND THEN null;
		// END;
		//
		// IF SQL%NOTFOUND THEN
		// w_mess := PA_PAIE.erreurp('ERR-90021',w_clang);
		// RETURN FALSE;
		// END IF;
		// String val_exp = "";
		//		if (calc_part_auto)
		//		{
		key = "99EXPAT5";
		obj = listOfTable99Map.get(key);
		if (obj != null && ((Object[]) obj)[1] != null)
		{
			val_exp = (String) ((Object[]) obj)[1];
		}
		else
		{
			error = parameter.errorMessage("ERR-90021", langue);
			ParameterUtil.println(error);
			globalUpdate._setEvolutionTraitement(request, error, true);
			finalResult = false;
			return false;
		}
		//		}
		//
		// BEGIN
		// SELECT valm INTO typ_rec FROM pafnom
		// WHERE cdos = wpdos.cdos
		// AND ctab = 99
		// AND cacc = 'EXPAT'
		// AND NUME = 1;
		// EXCEPTION
		// WHEN NO_DATA_FOUND THEN null;
		// END;
		// IF SQL%NOTFOUND THEN
		// w_mess := PA_PAIE.erreurp('ERR-90021',w_clang);
		// RETURN FALSE;
		// END IF;
		// int typ_rec = 0;
		key = "99EXPAT1";
		obj = listOfTable99Map.get(key);
		if (obj == null || ((Object[]) obj)[1] == null)
		{
			error = parameter.errorMessage("ERR-90021", langue);
			ParameterUtil.println(error);
			globalUpdate._setEvolutionTraitement(request, error, true);
			finalResult = false;
			return false;
		}
		else
		{
			typ_rec = (((Object[]) obj)[2] instanceof BigDecimal ? (BigDecimal) ((Object[]) obj)[2] : (Long) ((Object[]) obj)[2]).intValue();
		}
		//
		// -- Lecture du parametre donnant le nbre de jours de conge par defaut
		// -- Calcul fictif effectue ou pas
		// BEGIN
		// SELECT vall INTO wfnom.lib1 FROM pafnom
		// WHERE cdos = wpdos.cdos
		// AND ctab = 99
		// AND cacc = 'FICTIF'
		// AND nume = 1;
		// EXCEPTION
		// WHEN NO_DATA_FOUND THEN null;
		// END;
		// IF SQL%NOTFOUND THEN
		// w_mess := PA_PAIE.erreurp('ERR-90014',w_clang);
		// RETURN FALSE;
		// ELSE
		// w_fictif := substr(wfnom.lib1,1,1);
		// IF w_fictif != 'N' AND w_fictif != 'O' THEN
		// w_mess := PA_PAIE.erreurp('ERR-90015',w_clang);
		// RETURN FALSE;
		// END IF;
		// END IF;
		// String w_fictif = "";
		tmplib1 = "";
		key = "99FICTIF1";
		obj = listOfTable99Map.get(key);
		if (obj != null)
		{
			tmplib1 = (String) ((Object[]) obj)[1];
			w_fictif = StringUtil.substring(tmplib1, 0, 1);
			if (!"N".equals(w_fictif) && !"O".equals(w_fictif))
			{
				error = parameter.errorMessage("ERR-90015", langue);
				ParameterUtil.println(error);
				globalUpdate._setEvolutionTraitement(request, error, true);
				finalResult = false;
				return false;
			}
		}
		else
		{
			error = parameter.errorMessage("ERR-90014", langue);
			ParameterUtil.println(error);
			finalResult = false;
			return false;
		}
		//
		// IF w_fictif = 'O' THEN
		// -- Type de calcul fictif
		// BEGIN
		// SELECT vall INTO wfnom.lib2 FROM pafnom
		// WHERE cdos = wpdos.cdos
		// AND ctab = 99
		// AND cacc = 'FICTIF'
		// AND nume = 2;
		// EXCEPTION
		// WHEN NO_DATA_FOUND THEN null;
		// END;
		// IF SQL%NOTFOUND THEN
		// w_mess := PA_PAIE.erreurp('ERR-90014',w_clang);
		// RETURN FALSE;
		// ELSE
		// w_typ_fictif := substr(wfnom.lib2,1,1);
		// IF w_typ_fictif != 'A' AND w_typ_fictif != 'B' THEN
		// w_mess := PA_PAIE.erreurp('ERR-90017',w_clang);
		// RETURN FALSE;
		// END IF;
		// END IF;
		// END IF;
		String tmplib2 = "";
		// String w_typ_fictif = "";
		if ("O".equals(w_fictif))
		{
			key = "99FICTIF2";
			obj = listOfTable99Map.get(key);
			if (obj != null)
			{
				tmplib2 = (String) ((Object[]) obj)[1];
				w_typ_fictif = StringUtil.substring(tmplib2, 0, 1);
				if ((!"A".equals(w_typ_fictif)) && (!"B".equals(w_typ_fictif)))
				{
					error = parameter.errorMessage("ERR-90017", langue);
					ParameterUtil.println(error);
					globalUpdate._setEvolutionTraitement(request, error, true);
					finalResult = false;
					return false;
				}
			}
			else
			{
				error = parameter.errorMessage("ERR-90014", langue);
				ParameterUtil.println(error);
				globalUpdate._setEvolutionTraitement(request, error, true);
				finalResult = false;
				return false;
			}
		}
		//
		// -- Tau1 = locaux, tau2 = expatrie
		// BEGIN
		// SELECT valt INTO nbjc_lx
		// FROM pafnom
		// WHERE cdos = wpdos.cdos
		// AND ctab = 99
		// AND cacc = 'NBJC-DEF'
		// AND nume = 1;
		// EXCEPTION
		// WHEN NO_DATA_FOUND THEN null;
		// END;
		//
		// IF SQL%NOTFOUND OR PA_PAIE.NouZ(nbjc_lx) THEN
		// nbjc_lx := 2;
		// END IF;
		// int nbjc_lx = 0;
		key = "99NBJC-DEF1";
		obj = listOfTable99Map.get(key);
		if (obj == null)
		{
			nbjc_lx = 2;
		}
		else
		{
			nbjc_lx = (((Object[]) obj)[3] instanceof BigDecimal ? (BigDecimal) ((Object[]) obj)[3] : (Long) ((Object[]) obj)[3]).intValue();
		}
		//
		// BEGIN
		// SELECT valt INTO nbjc_ex
		// FROM pafnom
		// WHERE cdos = wpdos.cdos
		// AND ctab = 99
		// AND cacc = 'NBJC-DEF'
		// AND nume = 2;
		// EXCEPTION
		// WHEN NO_DATA_FOUND THEN null;
		// END;
		//
		// IF SQL%NOTFOUND OR PA_PAIE.NouZ(nbjc_ex) THEN
		// nbjc_ex := 5;
		// END IF;
		// int nbjc_ex = 0;
		key = "99NBJC-DEF2";
		obj = listOfTable99Map.get(key);
		if (obj == null)
		{
			nbjc_ex = 5;
		}
		else
		{
			nbjc_ex = (((Object[]) obj)[3] instanceof BigDecimal ? (BigDecimal) ((Object[]) obj)[3] : (Long) ((Object[]) obj)[3]).intValue();
		}
		//
		// -- Verification si des bulletins existent pour ce dossier
		// SELECT count(*) INTO nbbul
		// FROM pacalc
		// WHERE cdos = wpdos.cdos
		// AND aamm = w_aamm
		// AND nbul = w_nbul
		// AND rubq = w_nap;
		//
		// IF PA_PAIE.NouZ(nbbul) THEN
		// w_mess := PA_PAIE.erreurp('ERR-90101',w_clang);
		// RETURN FALSE;
		// END IF;

		List lcount = service.find("select count(*) from Rhtcalcul" + " where idEntreprise = '" + dossier + "'" + " and aamm = '" + periode + "'" + " and nbul = " + nbul + " and rubq = '" + nap + "'");
		// int nbbul = 0;
		if (lcount != null && lcount.size() > 0)
			nbbul = ((Long) lcount.get(0)).intValue();
		if (nbbul <= 0)
		{
			error = parameter.errorMessage("ERR-90101", langue);
			ParameterUtil.println(error);
			globalUpdate._setEvolutionTraitement(request, error, true);
			finalResult = false;
			return false;
		}

		// CURSOR curs_edibul IS
		// SELECT nmat
		// FROM pacalc
		// WHERE cdos = wpdos.cdos
		// AND aamm = w_aamm
		// AND nbul = w_nbul
		// AND rubq = w_nap
		// AND trtb = '1';
		//
		// -- Verification si tous les bulletins ont ete edites
		// nbbul := 0;
		// OPEN curs_edibul;
		// LOOP
		// FETCH curs_edibul INTO edi_mat;
		// EXIT WHEN curs_edibul%NOTFOUND;
		//
		// -- MM 28/09/2000
		// mat_exist := NULL;
		// OPEN curs_bull_exist;
		// FETCH curs_bull_exist INTO mat_exist;
		// CLOSE curs_bull_exist;
		// IF mat_exist IS NOT NULL THEN
		// nbbul := nbbul + 1;
		// END IF;
		//
		// END LOOP;
		// IF nbbul > 0 THEN
		// w_mess := PA_PAIE.erreurp('ERR-90102',w_clang,edi_mat);
		// RETURN FALSE;
		// END IF;
		String query = "Select count(*) from CalculPaie b where b.idEntreprise = '" + dossier + "'";
		query += " and b.aamm='" + periode + "'";
		query += " and b.nbul=" + nbul;
		query += " and b.rubq='" + nap + "'";
		query += " and b.trtb ='1'";
//		query += " and not exists (";
//		query += " select 'X' from Rhtcalcul a, Rhprubrique c";
//		query += " where a.cdos = b.cdos";
//		query += " and a.aamm = b.aamm";
//		query += " and a.nbul = b.nbul";
//		query += " and a.rubq !='" + nap + "'";
//		query += " and a.cdos = c.cdos and a.rubq = c.crub";
//		query += " and c.prbul != 0 ";
//		query += " and (c.ednul = 'O' or a.mont != 0))";

		//		String queryBulletin = "select distinct a.nmat from Rhtcalcul a, Rhprubrique b" + " where a.cdos = b.cdos" + " and a.aamm = '" + periode + "'" + " and a.nmat = 'ADOX'" + " and a.nbul = " + nbul + " and a.rubq = '" + nap + "'"
		//				+ " and a.idEntreprise = '" + dossier + "'" + " and a.rubq = b.crub" + " and b.prbul != 0" + " and (b.ednul = 'O' or ((case a.mont when null then 0 else a.mont end) != 0))";
		//		List lnmatcalcul = service.find("select distinct nmat from Rhtcalcul" + " where idEntreprise = '" + dossier + "'" + " and aamm = '" + periode + "'" + " and nbul = " + nbul + " and rubq = '" + nap + "'" + " and trtb = '1'");
		//		nbbul = 0;
		//		editMatricule = "";
		//		for (Object obj3 : lnmatcalcul)
		//		{
		//			editMatricule = obj3 != null ? (String) obj3 : "";
		//			// ParameterUtil.println("....editMatricule: " + editMatricule);
		//			lcount = service.find(queryBulletin.replace("ADOX", editMatricule));
		//			if (lcount != null && lcount.size() > 0)
		//				nbbul++;
		//		}
		nbbul = new Integer(service.find(query).get(0).toString());
		if (nbbul > 0)
		{
			error = parameter.errorMessage("ERR-90102", langue, editMatricule);
			ParameterUtil.println(error);
			globalUpdate._setEvolutionTraitement(request, error, true);
			return false;
		}

		//
		// -- ----- Lecture des rubriques generant un E.V. sur le mois suivant
		// SELECT NVL(max(decode(nume,1,valm,0)),0),
		// NVL(max(decode(nume,2,valm,0)),0),
		// NVL(max(decode(nume,3,valm,0)),0),
		// NVL(max(decode(nume,4,valm,0)),0),
		// NVL(max(decode(nume,5,valm,0)),0),
		// NVL(max(decode(nume,6,valm,0)),0),
		// NVL(max(decode(nume,7,valm,0)),0),
		// NVL(max(decode(nume,8,valm,0)),0)
		// INTO wfnom.mnt1,wfnom.mnt2,wfnom.mnt3,wfnom.mnt4,
		// wfnom.mnt5,wfnom.mnt6,wfnom.mnt7,wfnom.mnt8
		// FROM pafnom
		// WHERE cdos = wpdos.cdos
		// AND ctab = 99
		// AND cacc = 'EV-MC'
		// AND nume in (1,2,3,4,5,6,7,8);
		//
		// t_ev_mc(1) := ltrim(to_char(wfnom.mnt1,'0999'));
		// t_ev_mc(2) := ltrim(to_char(wfnom.mnt2,'0999'));
		// t_ev_mc(3) := ltrim(to_char(wfnom.mnt3,'0999'));
		// t_ev_mc(4) := ltrim(to_char(wfnom.mnt4,'0999'));
		// t_ev_mc(5) := ltrim(to_char(wfnom.mnt5,'0999'));
		// t_ev_mc(6) := ltrim(to_char(wfnom.mnt6,'0999'));
		// t_ev_mc(7) := ltrim(to_char(wfnom.mnt7,'0999'));
		// t_ev_mc(8) := ltrim(to_char(wfnom.mnt8,'0999'));
		String complexQuery = "select max(case nume when 1 then valm else 0 end)," + " max(case nume when 2 then valm else 0 end)," + " max(case nume when 3 then valm else 0 end)," + " max(case nume when 4 then valm else 0 end)," + " max(case nume when 5 then valm else 0 end),"
				+ " max(case nume when 6 then valm else 0 end)," + " max(case nume when 7 then valm else 0 end)," + " max(case nume when 8 then valm else 0 end) " + " from Rhfnom" + " where idEntreprise = '" + dossier + "'" + " and ctab = 99" + " and cacc = 'EV-MC'"
				+ " and nume in (1,2,3,4,5,6,7,8)";
		// t_ev_mc = new String[8];
		t_ev_mc = new String[] { "", "", "", "", "", "", "", "" };
		Object[] max = (Object[]) service.find(complexQuery).get(0);
		for (int i = 0; i < 8; i++)
		{
			t_ev_mc[i] = ClsStringUtil.formatNumber(max[i] instanceof BigDecimal ? (BigDecimal) max[i] : (Long) max[i], "0000");
		}
		//
		// -- ----- Lecture des rubriques generees en E.V. par le mois precedent
		// SELECT NVL(max(decode(nume,1,valm,0)),0),
		// NVL(max(decode(nume,2,valm,0)),0),
		// NVL(max(decode(nume,3,valm,0)),0),
		// NVL(max(decode(nume,4,valm,0)),0),
		// NVL(max(decode(nume,5,valm,0)),0),
		// NVL(max(decode(nume,6,valm,0)),0),
		// NVL(max(decode(nume,7,valm,0)),0),
		// NVL(max(decode(nume,8,valm,0)),0)
		// INTO wfnom.mnt1,wfnom.mnt2,wfnom.mnt3,wfnom.mnt4,
		// wfnom.mnt5,wfnom.mnt6,wfnom.mnt7,wfnom.mnt8
		// FROM pafnom
		// WHERE cdos = wpdos.cdos
		// AND ctab = 99
		// AND cacc = 'EV-MS'
		// AND nume in (1,2,3,4,5,6,7,8);
		complexQuery = "select max(case nume when 1 then valm else 0 end)," + " max(case nume when 2 then valm else 0 end)," + " max(case nume when 3 then valm else 0 end)," + " max(case nume when 4 then valm else 0 end)," + " max(case nume when 5 then valm else 0 end),"
				+ " max(case nume when 6 then valm else 0 end)," + " max(case nume when 7 then valm else 0 end)," + " max(case nume when 8 then valm else 0 end) " + " from Rhfnom" + " where idEntreprise = '" + dossier + "'" + " and ctab = 99" + " and cacc = 'EV-MS'"
				+ " and nume in (1,2,3,4,5,6,7,8)";
		//
		// t_ev_ms(1) := ltrim(to_char(wfnom.mnt1,'0999'));
		// t_ev_ms(2) := ltrim(to_char(wfnom.mnt2,'0999'));
		// t_ev_ms(3) := ltrim(to_char(wfnom.mnt3,'0999'));
		// t_ev_ms(4) := ltrim(to_char(wfnom.mnt4,'0999'));
		// t_ev_ms(5) := ltrim(to_char(wfnom.mnt5,'0999'));
		// t_ev_ms(6) := ltrim(to_char(wfnom.mnt6,'0999'));
		// t_ev_ms(7) := ltrim(to_char(wfnom.mnt7,'0999'));
		// t_ev_ms(8) := ltrim(to_char(wfnom.mnt8,'0999'));
		// t_ev_ms = new String[8];
		t_ev_ms = new String[] { "", "", "", "", "", "", "", "" };
		max = (Object[]) service.find(complexQuery).get(0);
		for (int i = 0; i < 8; i++)
		{
			t_ev_ms[i] = ClsStringUtil.formatNumber(max[i] instanceof BigDecimal ? (BigDecimal) max[i] : (Long) max[i], "0000");
		}
		//
		// -- Lecture de la rubrique base conges table 99
		// BEGIN
		// SELECT valm, vall INTO wfnom.mnt1, wfnom.lib1
		// FROM pafnom
		// WHERE cdos = wpdos.cdos
		// AND ctab = 99
		// AND cacc = 'BASE-CONGE'
		// AND nume = 1;
		// EXCEPTION
		// WHEN NO_DATA_FOUND THEN null;
		// END;
		// IF SQL%NOTFOUND THEN
		// w_mess := PA_PAIE.erreurp('ERR-90126',w_clang);
		// RETURN FALSE;
		// ELSE
		// rub_bc := ltrim(to_char(wfnom.mnt1,'0999'));
		// calcul_nbjsup := SUBSTR(NVL(wfnom.lib1,' '),1,1);
		// IF calcul_nbjsup NOT IN ('A','O') THEN
		// calcul_nbjsup := 'N';
		// END IF;
		// END IF;
		// String rub_bc = "";
		key = "99BASE-CONGE1";
		obj = listOfTable99Map.get(key);
		if (obj == null)
		{
			error = parameter.errorMessage("ERR-90126", langue);
			ParameterUtil.println(error);
			globalUpdate._setEvolutionTraitement(request, error, true);
			finalResult = false;
			return false;
		}
		else
		{
			rub_bc = ClsStringUtil.formatNumber(((Object[]) obj)[2] instanceof BigDecimal ? (BigDecimal) ((Object[]) obj)[2] : (Long) ((Object[]) obj)[2], "0000");
			calcul_nbjsup = ((String) ((Object[]) obj)[1]).substring(0, 1);
			if (!"A".equals(calcul_nbjsup) && !"O".equals(calcul_nbjsup))
			{
				calcul_nbjsup = "N";
			}
		}
		//
		// SELECT vall INTO wfnom.lib2 FROM pafnom
		// WHERE cdos = wpdos.cdos
		// AND ctab = 99
		// AND cacc = 'BASE-CONGE'
		// AND nume = 2;
		// ajout_bc := SUBSTR(NVL(wfnom.lib2,' '),1,1);
		// String ajout_bc = "";
		key = "99BASE-CONGE2";
		obj = listOfTable99Map.get(key);
		if (obj != null && ((Object[]) obj)[1] != null)
		{
			ajout_bc = ((String) ((Object[]) obj)[1]).substring(0, 1);
		}
		//
		// SELECT vall INTO wfnom.lib3 FROM pafnom
		// WHERE cdos = wpdos.cdos
		// AND ctab = 99
		// AND cacc = 'BASE-CONGE'
		// AND nume = 3;
		// pror_jr := SUBSTR(NVL(wfnom.lib3,' '),1,1);
		// String pror_jr = "";
		key = "99BASE-CONGE3";
		obj = listOfTable99Map.get(key);
		if (obj != null && ((Object[]) obj)[1] != null)
		{
			pror_jr = ((String) ((Object[]) obj)[1]).substring(0, 1);
		}
		//
		// SELECT vall INTO wfnom.lib4 FROM pafnom
		// WHERE cdos = wpdos.cdos
		// AND ctab = 99
		// AND cacc = 'BASE-CONGE'
		// AND nume = 4;
		// IF NOT PA_PAIE.NouB(wfnom.lib4) THEN
		// w_cg_abs := SUBSTR(wfnom.lib4,1,2);
		// ELSE
		// w_cg_abs := NULL;
		// END IF;
		// String w_cg_abs = "";
		key = "99BASE-CONGE4";
		obj = listOfTable99Map.get(key);
		if (obj != null && ((Object[]) obj)[1] != null)
		{
			w_cg_abs = ((String) ((Object[]) obj)[1]).substring(0, 2);
		}
		//
		// SELECT vall INTO wfnom.lib1 FROM pafnom
		// WHERE cdos = wpdos.cdos
		// AND ctab = 99
		// AND cacc = 'BASE-CONGE'
		// AND nume = 5;
		// raz_jour := SUBSTR(NVL(wfnom.lib1,' '),1,1);
		// String raz_jour = "";
		key = "99BASE-CONGE5";
		obj = listOfTable99Map.get(key);
		if (obj != null && ((Object[]) obj)[1] != null)
		{
			raz_jour = ((String) ((Object[]) obj)[1]).substring(0, 1);
		}
		//
		// -- Test coherence FICTIF - BASE-CONGE.
		// IF w_fictif = 'O' THEN
		// -- MM 24/11/1999
		// -- on genere aussi les evs de conges sur le mois suivant
		// -- IF w_typ_fictif = 'B' THEN
		// IF PA_PAIE.NouB(w_cg_abs) THEN
		// w_mess := PA_PAIE.erreurp('ERR-90127',w_clang);
		// RETURN FALSE;
		// END IF;
		// -- ELSE
		// -- w_cg_abs := NULL;
		// -- END IF;
		// END IF;
		if ("O".equals(w_fictif))
		{
			if (ClsObjectUtil.isNull(w_cg_abs))
			{
				error = parameter.errorMessage("ERR-90127", langue);
				ParameterUtil.println(error);
				globalUpdate._setEvolutionTraitement(request, error, true);
				finalResult = false;
				return false;
			}
		}
		//
		// -- Lecture de la rubrique du brut table 99
		// BEGIN
		// SELECT valm INTO wfnom.mnt1 FROM pafnom
		// WHERE cdos = wpdos.cdos
		// AND ctab = 99
		// AND cacc = 'RUBBRUT'
		// AND nume = 1;
		// EXCEPTION
		// WHEN NO_DATA_FOUND THEN null;
		// END;
		//
		// IF SQL%NOTFOUND THEN
		// w_mess := PA_PAIE.erreurp('ERR-90023',w_clang);
		// RETURN FALSE;
		// ELSE
		// w_brut := ltrim(to_char(wfnom.mnt1,'0999'));
		// END IF;
		// String w_brut = "";
		key = "99RUBBRUT1";
		obj = listOfTable99Map.get(key);
		if (obj != null)
		{
			w_brut = ClsStringUtil.formatNumber(((Object[]) obj)[2] instanceof BigDecimal ? (BigDecimal) ((Object[]) obj)[2] : (Long) ((Object[]) obj)[2], "0000");
		}
		else
		{
			error = parameter.errorMessage("ERR-90023", langue);
			ParameterUtil.println(error);
			globalUpdate._setEvolutionTraitement(request, error, true);
			finalResult = false;
			return false;
		}
		//
		// -- Lecture de l'age maxi des enfants donnant droit a des jrs supp
		// BEGIN
		// SELECT MAX(decode(nume,1,valm,0)), MAX(decode(nume,2,valm,0)), MAX(decode(nume,3,valm,0))
		// INTO age_max_enfant, minimum_enfant, age_max_fiscal FROM pafnom
		// WHERE cdos = wpdos.cdos
		// AND ctab = 99
		// AND cacc = 'ENFANT'
		// AND nume IN (1,2,3);
		// EXCEPTION
		// WHEN NO_DATA_FOUND THEN null;
		// END;
		//
		// IF SQL%NOTFOUND THEN
		// comptage_enfant := FALSE;
		// ELSE
		// IF PA_PAIE.NouZ(age_max_enfant) THEN
		// comptage_enfant := FALSE;
		// ELSE
		// comptage_enfant := TRUE;
		// END IF;
		// IF PA_PAIE.NouZ(minimum_enfant) THEN
		// minimum_enfant := 0;
		// END IF;
		// END IF;

		Object[] o = (Object[]) service.find(
				"select " + " max(case nume when 1 then valm else 0 end), " + " max(case nume when 2 then valm else 0 end), " + " max(case nume when 3 then valm else 0 end) from Rhfnom" + " where idEntreprise = '" + dossier + "'" + " and ctab = 99" + " and cacc = 'ENFANT'"
						+ " and nume in(1, 2, 3)").get(0);
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
		//
		// -- MM 22/06/2004
		// -- Lecture des parametres pour le prorata des jours de droit mois depart et retour
		// BEGIN
		// SELECT SUBSTR(vall,1,1) INTO wfnom.lib1
		// FROM pafnom
		// WHERE cdos = wpdos.cdos
		// AND ctab = 99
		// AND cacc = 'PROR-JCG'
		// AND nume = 1;
		// EXCEPTION
		// WHEN NO_DATA_FOUND THEN NULL;
		// END;
		// IF SQL%NOTFOUND THEN
		// pror_jcg_normal := TRUE;
		// ELSE
		// IF NVL(wfnom.lib1,' ') = 'B' THEN
		//
		// pror_jcg_normal := FALSE;
		// ELSE
		// pror_jcg_normal := TRUE;
		// END IF;
		// END IF;
		// boolean pror_jcg_normal = false;
		tmplib1 = "";
		key = "99PROR-JCG1";
		obj = listOfTable99Map.get(key);
		if (obj != null)
		{
			tmplib1 = ((String) ((Object[]) obj)[1]).substring(0, 1);
			if ("B".equals(tmplib1))
			{
				pror_jcg_normal = false;
			}
			else
			{
				pror_jcg_normal = true;
			}
		}
		else
		{
			pror_jcg_normal = true;
		}
		//
		// IF NOT pror_jcg_normal THEN
		if (!pror_jcg_normal)
		{
			//
			// BEGIN
			// SELECT SUBSTR(vall,1,2),SUBSTR(vall,3,2),valt INTO dbcg_barem1,fincg_barem1,nbjcg_barem1
			// FROM pafnom
			// WHERE cdos = wpdos.cdos
			// AND ctab = 99
			// AND cacc = 'PRJ-BARCG'
			// AND nume = 1;
			// EXCEPTION
			// WHEN NO_DATA_FOUND THEN NULL;
			// END;
			// IF SQL%NOTFOUND THEN
			// w_mess := PA_PAIE.erreurp('ERR-30165',w_clang,'PRJ-BARCG','99','1');
			// RETURN FALSE;
			// ELSE
			// IF dbcg_barem1 <= 0 OR dbcg_barem1 > 31 THEN
			// w_mess := PA_PAIE.erreurp('ERR-10516',w_clang,'PRJ-BARCG','99','1');
			// RETURN FALSE;
			// END IF;
			// END IF;
			tmplib1 = "";
			key = "99PRJ-BARCG1";
			obj = listOfTable99Map.get(key);
			// String dbcg_barem1 = "";
			// String fincg_barem1 = "";
			// int nbjcg_barem1 = 0;
			if (obj != null)
			{
				tmplib1 = (String) ((Object[]) obj)[1];
				dbcg_barem1 = tmplib1.substring(0, 2);
				fincg_barem1 = tmplib1.substring(2, 4);
				nbjcg_barem1 = (((Object[]) obj)[3] instanceof BigDecimal ? (BigDecimal) ((Object[]) obj)[3] : (Long) ((Object[]) obj)[3]).intValue();
				if (Integer.valueOf(dbcg_barem1) <= 0 || Integer.valueOf(fincg_barem1) > 31)
				{
					error = parameter.errorMessage("ERR-10516", langue, "PRJ-BARCG", "99", "1");
					ParameterUtil.println(error);
					globalUpdate._setEvolutionTraitement(request, error, true);
					finalResult = false;
					return false;
				}
			}
			else
			{
				error = parameter.errorMessage("ERR-30165", langue, "PRJ-BARCG", "99", "1");
				ParameterUtil.println(error);
				globalUpdate._setEvolutionTraitement(request, error, true);
				finalResult = false;
				return false;
			}
			//
			// BEGIN
			// SELECT SUBSTR(vall,1,2),SUBSTR(vall,3,2),valt INTO dbcg_barem2,fincg_barem2,nbjcg_barem2
			// FROM pafnom
			// WHERE cdos = wpdos.cdos
			// AND ctab = 99
			// AND cacc = 'PRJ-BARCG'
			// AND nume = 2;
			// EXCEPTION
			// WHEN NO_DATA_FOUND THEN NULL;
			// END;
			// IF SQL%NOTFOUND THEN
			// w_mess := PA_PAIE.erreurp('ERR-30165',w_clang,'PRJ-BARCG','99','2');
			// RETURN FALSE;
			// ELSE
			// IF dbcg_barem2 <= 0 OR fincg_barem2 > 31 THEN
			// w_mess := PA_PAIE.erreurp('ERR-10516',w_clang,'PRJ-BARCG','99','2');
			// RETURN FALSE;
			// END IF;
			// END IF;
			tmplib1 = "";
			key = "99PRJ-BARCG2";
			obj = listOfTable99Map.get(key);
			// String dbcg_barem2 = "";
			// String fincg_barem2 = "";
			// int nbjcg_barem2 = 0;
			if (obj != null)
			{
				tmplib1 = (String) ((Object[]) obj)[1];
				dbcg_barem2 = tmplib1.substring(0, 2);
				fincg_barem2 = tmplib1.substring(2, 4);
				nbjcg_barem2 = (((Object[]) obj)[3] instanceof BigDecimal ? (BigDecimal) ((Object[]) obj)[3] : (Long) ((Object[]) obj)[3]).intValue();
				if (Integer.valueOf(dbcg_barem2) <= 0 || Integer.valueOf(fincg_barem2) > 31)
				{
					error = parameter.errorMessage("ERR-10516", langue, "PRJ-BARCG", "99", "2");
					ParameterUtil.println(error);
					globalUpdate._setEvolutionTraitement(request, error, true);
					finalResult = false;
					return false;
				}
			}
			else
			{
				error = parameter.errorMessage("ERR-30165", langue, "PRJ-BARCG", "99", "2");
				ParameterUtil.println(error);
				globalUpdate._setEvolutionTraitement(request, error, true);
				finalResult = false;
				return false;
			}
			//
			// BEGIN
			// SELECT SUBSTR(vall,1,2),SUBSTR(vall,3,2),valt INTO dbcg_barem3,fincg_barem3,nbjcg_barem3
			// FROM pafnom
			// WHERE cdos = wpdos.cdos
			// AND ctab = 99
			// AND cacc = 'PRJ-BARCG'
			// AND nume = 3;
			// EXCEPTION
			// WHEN NO_DATA_FOUND THEN NULL;
			// END;
			// IF SQL%NOTFOUND THEN
			// w_mess := PA_PAIE.erreurp('ERR-30165',w_clang,'PRJ-BARCG','99','3');
			// RETURN FALSE;
			// ELSE
			// IF dbcg_barem3 <= 0 OR fincg_barem3 > 31 THEN
			// w_mess := PA_PAIE.erreurp('ERR-10516',w_clang,'PRJ-BARCG','99','3');
			// RETURN FALSE;
			// END IF;
			// END IF;
			tmplib1 = "";
			key = "99PRJ-BARCG3";
			obj = listOfTable99Map.get(key);
			// String dbcg_barem3 = "";
			// String fincg_barem3 = "";
			// int nbjcg_barem3 = 0;
			if (obj != null)
			{
				tmplib1 = (String) ((Object[]) obj)[1];
				dbcg_barem3 = tmplib1.substring(0, 2);
				fincg_barem3 = tmplib1.substring(2, 4);
				nbjcg_barem3 = (((Object[]) obj)[3] instanceof BigDecimal ? (BigDecimal) ((Object[]) obj)[3] : (Long) ((Object[]) obj)[3]).intValue();
				if (Integer.valueOf(dbcg_barem3) <= 0 || Integer.valueOf(fincg_barem3) > 31)
				{
					error = parameter.errorMessage("ERR-10516", langue, "PRJ-BARCG", "99", "3");
					ParameterUtil.println(error);
					finalResult = false;
					return false;
				}
			}
			else
			{
				error = parameter.errorMessage("ERR-30165", langue, "PRJ-BARCG", "99", "3");
				ParameterUtil.println(error);
				globalUpdate._setEvolutionTraitement(request, error, true);
				finalResult = false;
				return false;
			}
			//
			//
			//
			// END IF;
		}
		//
		// BEGIN
		// SELECT valm INTO wfnom.mnt1
		// FROM pafnom
		// WHERE cdos = wpdos.cdos
		// AND ctab = 99
		// AND cacc = 'ANNIV-CG'
		// AND nume = 1;
		// EXCEPTION
		// WHEN NO_DATA_FOUND THEN NULL;
		// END;
		// IF SQL%NOTFOUND THEN
		// wfnom.mnt1 := 1;
		// ELSE
		// IF wfnom.mnt1 NOT IN (1,2,3) THEN
		// w_mess := PA_PAIE.erreurp('ERR-10517',w_clang,'ANNIV-CG','99','1');
		// RETURN FALSE;
		// END IF;
		// w_type_dtanniv := wfnom.mnt1;
		// END IF;
		// int w_type_dtanniv = 0;
		key = "99ANNIV-CG1";
		obj = listOfTable99Map.get(key);
		if (obj != null && ((Object[]) obj)[2] != null)
		{
			w_type_dtanniv = (((Object[]) obj)[2] instanceof BigDecimal ? (BigDecimal) ((Object[]) obj)[2] : (Long) ((Object[]) obj)[2]).intValue();
			if (w_type_dtanniv != 1 && w_type_dtanniv != 2 && w_type_dtanniv != 3)
			{
				w_type_dtanniv = 1;
				error = parameter.errorMessage("ERR-10517", langue, "ANNIV-CG", "99", "1");
				ParameterUtil.println(error);
				globalUpdate._setEvolutionTraitement(request, error, true);
				finalResult = false;
				return false;
			}
		}
		else
		{
			w_type_dtanniv = 1;
		}
		//
		//
		//
		// -- Ctle de l'existence et chargement de la table 91
		// -- MM 17/11/1999
		// w_count := 0;
		// BEGIN
		// SELECT count(*) INTO w_count FROM palnom
		// WHERE cdos = wpdos.cdos
		// AND ctab = 91
		// AND nume = 1;
		// EXCEPTION
		// WHEN NO_DATA_FOUND THEN null;
		// END;
		//
		lcount = service.find("select count(*) from Rhlnom" + " where idEntreprise = '" + dossier + "'" + " and ctab = 91" + " and nume = 1");
		int w_count = 0;
		if (lcount != null && lcount.size() > 0)
			w_count = ((Long) lcount.get(0)).intValue();
		// -- MM
		// boolean t91hc = false;
		// boolean t91hp = false;
		// boolean t91mc = false;
		// boolean t91mp = false;
		// boolean t91in = false;
		// IF w_count = 0 THEN
		// tab91b := FALSE;
		if (w_count == 0)
		{
			tab91b = false;
		}
		// ELSE
		else
		{
			// t91hc := TRUE;
			// t91hp := TRUE;
			// t91mc := TRUE;
			// t91mp := TRUE;
			// t91in := TRUE;
			t91hc = true;
			t91hp = true;
			t91mc = true;
			t91mp = true;
			t91in = true;
			// BEGIN
			// SELECT 'X' INTO w_exist FROM pafnom
			// WHERE cdos = wpdos.cdos
			// AND ctab = 91
			// AND cacc = 'HC'
			// AND nume = 1;
			// EXCEPTION
			// WHEN NO_DATA_FOUND THEN null;
			// END;
			//
			// IF SQL%NOTFOUND THEN
			// t91hc := FALSE;
			// END IF;
			key = "91HC1";
			obj = listOfTable99Map.get(key);
			if (obj == null)
				t91hc = false;
			//
			// BEGIN
			// SELECT 'X' INTO w_exist
			// FROM pafnom
			// WHERE cdos = wpdos.cdos
			// AND ctab = 91
			// AND cacc = 'HP'
			// AND nume = 1;
			// EXCEPTION
			// WHEN NO_DATA_FOUND THEN null;
			// END;
			//
			// IF SQL%NOTFOUND THEN
			// t91hp := FALSE;
			// END IF;
			key = "91HP1";
			obj = listOfTable99Map.get(key);
			if (obj == null)
				t91hp = false;
			//
			// BEGIN
			// SELECT 'X' INTO w_exist
			// FROM pafnom
			// WHERE cdos = wpdos.cdos
			// AND ctab = 91
			// AND cacc = 'MC'
			// AND nume = 1;
			// EXCEPTION
			// WHEN NO_DATA_FOUND THEN null;
			// END;
			//
			// IF SQL%NOTFOUND THEN
			// t91mc := FALSE;
			// END IF;
			key = "91MC1";
			obj = listOfTable99Map.get(key);
			if (obj == null)
				t91mc = false;
			//
			// BEGIN
			// SELECT 'X' INTO w_exist
			// FROM pafnom
			// WHERE cdos = wpdos.cdos
			// AND ctab = 91
			// AND cacc = 'MP'
			// AND nume = 1;
			// EXCEPTION
			// WHEN NO_DATA_FOUND THEN null;
			// END;
			//
			// IF SQL%NOTFOUND THEN
			// t91mp := FALSE;
			// END IF;
			key = "91MP1";
			obj = listOfTable99Map.get(key);
			if (obj == null)
				t91mp = false;
			//
			// BEGIN
			// SELECT 'X' INTO w_exist
			// FROM pafnom
			// WHERE cdos = wpdos.cdos
			// AND ctab = 91
			// AND cacc = 'IN'
			// AND nume = 1;
			// EXCEPTION
			// WHEN NO_DATA_FOUND THEN null;
			// END;
			//
			// IF SQL%NOTFOUND THEN
			// t91in := FALSE;
			// END IF;
			key = "91IN1";
			obj = listOfTable99Map.get(key);
			if (obj == null)
				t91in = false;
			//
			// tab91b := TRUE;
			// nb_t91 := 0;
			tab91b = true;
			nb_t91 = 0;
			String queryFnom = "select a.cacc, a.vall, b.vall from Rhfnom a, Rhfnom b " + " where a.idEntreprise = '" + dossier + "'" + " and a.ctab = 91" + " and (a.cacc >= '" + moisPaieCourant + "' " + " and a.cacc not in('MC','HC','MP','HP','IN'))"
					+ " and a.nume = 2" + " and b.cdos = a.cdos" + " and b.ctab = a.ctab" + " and b.cacc = a.cacc" + " and b.nume = 3" + " order by a.cacc";
			// OPEN curs_fnom;
			// LOOP
			// FETCH curs_fnom INTO wcacc, wlib2, wlib3;
			// EXIT WHEN curs_fnom%NOTFOUND;
			// nb_t91 := nb_t91 + 1;
			// t91_periode(nb_t91) := wcacc;
			// BEGIN
			// t91_dd(nb_t91) := TO_DATE(wlib2, 'DD/MM/YYYY');
			// t91_df(nb_t91) := TO_DATE(wlib3, 'DD/MM/YYYY');
			// EXCEPTION
			// WHEN OTHERS THEN
			// w_mess := PA_PAIE.erreurp('ERR-90128',w_clang,wcacc);
			// RETURN FALSE;
			// END;
			// END LOOP;
			// CLOSE curs_fnom;
			lcount = service.find(queryFnom);
			nb_t91 = lcount.size();
			Object[] nomenc = null;
			listOfPeriodeT91 = new ArrayList<ClsInfoPeriode>();
			ClsInfoPeriode periodeinfo = null;
			for (Object obj1 : lcount)
			{
				try
				{
					periodeinfo = new ClsInfoPeriode();
					nomenc = (Object[]) obj1;
					
					periodeinfo.periode = nomenc[0].toString();
					periodeinfo.ddebut = DateUtils.parseDate((String)nomenc[1], new String[]{"dd-MM-yyyy","dd/MM/yyyy","yyyy-MM-dd","yyyy/MM/dd"});
					periodeinfo.dfin = DateUtils.parseDate((String)nomenc[2], new String[]{"dd-MM-yyyy","dd/MM/yyyy","yyyy-MM-dd","yyyy/MM/dd"});
					listOfPeriodeT91.add(periodeinfo);
				}
				catch (ParseException e)
				{
					e.printStackTrace();
					continue;
				}
			}
			// END IF;
		}
		//
		//
//		List l = service.find("from Rhpdo where idEntreprise = '" + dossier + "'");
//		// Date dtDdex = null;
//		// myMoisPaieCourant = null;
//		moisPaieCourant = null;
//		if (l != null && l.size() > 0)
//		{
//			rhpdo = (Rhpdo) l.get(0);
//			dtDdmp = rhpdo.getDdmp();
//			dtDdex = rhpdo.getDdex();
//			// myMoisPaieCourant = new ClsDate(dtDdmp);
//			//moisPaieCourant = myMoisPaieCourant.getYearAndMonth();
//			if(dtDdmp != null)
//				moisPaieCourant = new ClsDate(dtDdmp, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getDateS();
//		}
//		myPeriode = new ClsDate(periode, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
//		// w_am99 := to_char(wpdos.ddex,'YYYY')||'99';
		w_am99 = new ClsDate(dtDdex).getYear() + "99";
		//
		// nbrcon := 0;
		// OPEN curs_rub_reg;
		// LOOP
		// FETCH curs_rub_reg INTO dosreg , rubreg , rconreg;
		// EXIT WHEN curs_rub_reg%NOTFOUND;
		// nbrcon := nbrcon + 1;
		// tab_rub(nbrcon) := rconreg;
		// END LOOP;
		// CLOSE curs_rub_reg;
		lcount = service.find(queryRubrique);
		nbrcon = lcount.size();
		listOfRubriqueCon = new ArrayList();
		Object[] rcon = null;
		for (Object obj2 : lcount)
		{
			rcon = (Object[]) obj2;
			listOfRubriqueCon.add(rcon[2]);
		}
		//
		// compt_sal := 0;
		compt_sal = 0;
		//
		// RETURN TRUE;
		//
		// END init;

		//
		return true;
	}

	// ---------------------------------------------------------------------------------
	// -- Maj du dossier de paie
	// ---------------------------------------------------------------------------------
	public boolean updateDossierDePaie(Session oSession)
	{
		try
		{
			// FUNCTION maj_dossier RETURN BOOLEAN IS
			//
			// mois VARCHAR2(6);
			// w_dmp DATE;
			// dtdeb DATE;
			// dtfin DATE;
			// andeb SMALLINT;
			// anfin SMALLINT;
			// w_dnbu SMALLINT;
			// w_dern BOOLEAN;
			//
			// w_id INTEGER;
			// w_exec INTEGER;
			// w_chaine VARCHAR2(250);
			//
			// BEGIN
			// mois := to_char(wpdos.dfex,'YYYY')||to_char(wpdos.dfex,'MM');
			// andeb := to_number(to_char(wpdos.ddex,'YYYY')) + 1;
			// anfin := to_number(to_char(wpdos.dfex,'YYYY')) + 1;
			String mois = new ClsDate(rhpdo.getDfex()).getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
			ClsDate ddex = new ClsDate(rhpdo.getDdex());
			ClsDate dfex = new ClsDate(rhpdo.getDfex());
			int andeb = ddex.getYear() + 1;
			int anfin = dfex.getYear() + 1;
			//
			// w_dern := PA_PAIE.dernier_periode(wpdos.cdos,TO_DATE(w_aamm,'YYYYMM'),w_nbul);
			//
			// IF w_aamm = mois AND w_dern THEN
			// dtdeb := to_date(to_char(wpdos.ddex,'DD')||to_char(wpdos.ddex,'MM')||TO_CHAR(andeb,'0999'),'DDMMYYYY');
			// dtfin := to_date(to_char(wpdos.dfex,'DD')||to_char(wpdos.dfex,'MM')||TO_CHAR(anfin,'0999'),'DDMMYYYY');
			// ELSE
			// dtdeb := wpdos.ddex;
			// dtfin := wpdos.dfex;
			// END IF;
			//
			Date dtdeb = null;
			Date dtfin = null;
			boolean w_dern = bulletin.dernierePeriode(dossier, periode, numerobulletin);
			if (periode.equals(mois) && w_dern)
			{
				dtdeb = new ClsDate(ClsStringUtil.formatNumber(ddex.getDay(), "00") + ClsStringUtil.formatNumber(ddex.getMonth(), "00") + ClsStringUtil.formatNumber(andeb, "0000"), "ddMMyyyy").getDate();
				dtfin = new ClsDate(ClsStringUtil.formatNumber(dfex.getDay(), "00") + ClsStringUtil.formatNumber(dfex.getMonth(), "00") + ClsStringUtil.formatNumber(anfin, "0000"), "ddMMyyyy").getDate();
			}
			else
			{
				dtdeb = rhpdo.getDdex();
				dtfin = rhpdo.getDfex();
			}
			// IF w_dern THEN
			// w_dmp := TO_DATE(w_aamm,'YYYYMM');
			// wpdos.dnbu := 0;
			// w_dnbu := 0;
			// ELSE
			// w_dmp := wpdos.ddmp;
			// wpdos.dnbu := w_nbul;
			// w_dnbu := w_nbul;
			// END IF;
			Date w_dmp = null;
			int w_dnbu = 0;
			if (w_dern)
			{
				w_dmp = myPeriode.getDate();
				rhpdo.setDnbu(0);
				w_dnbu = 0;
			}
			else
			{
				w_dmp = rhpdo.getDdmp();
				rhpdo.setDnbu(numerobulletin);
				w_dnbu = numerobulletin;
			}
			//
			// wpdos.ddmp := w_dmp;
			// wpdos.ddex := dtdeb;
			// wpdos.dfex := dtfin;
			rhpdo.setDdmp(w_dmp);
			rhpdo.setDdex(dtdeb);
			rhpdo.setDfex(dtfin);
			//
			// UPDATE pados
			// SET ddmp = w_dmp,
			// ddex = dtdeb,
			// dfex = dtfin,
			// dnbu = w_dnbu
			// WHERE cdos = wpdos.cdos;
			//
			oSession.update(rhpdo);
			//
			// /*
			// w_id := DBMS_SQL.OPEN_CURSOR;
			// w_chaine := 'CREATE TABLE tmpcalc AS'
			// || ' SELECT * FROM tmpevar '
			// || ' WHERE ( aamm > ''' || ''' || w_aamm || ''' || ') OR'
			// || ' ( aamm = ' || ''' || w_aamm || '''
			// || ' AND nbul != ' || TO_CHAR(w_nbul) || ')';
			// DBMS_SQL.PARSE(w_id,w_chaine,DBMS_SQL.V7);
			// w_exec := DBMS_SQL.EXECUTE(w_id);
			//
			// w_chaine := 'TRUNCATE TABLE pacalc';
			// DBMS_SQL.PARSE(w_id,w_chaine,DBMS_SQL.V7);
			// w_exec := DBMS_SQL.EXECUTE(w_id);
			//
			// w_chaine := 'INSERT INTO pacalc SELECT * FROM tmpcalc';
			// DBMS_SQL.PARSE(w_id,w_chaine,DBMS_SQL.V7);
			// w_exec := DBMS_SQL.EXECUTE(w_id);
			//
			// DBMS_SQL.CLOSE_CURSOR(w_id);
			//
			// COMMIT;
			// */
			//
			// BEGIN
			// DELETE FROM pacalc
			// WHERE cdos = wpdos.cdos
			// AND aamm = w_aamm
			// AND nbul = w_nbul;
			// EXCEPTION
			// WHEN OTHERS THEN null;
			// END;
			ParameterUtil.println("Before Deleting rhtcalcul " + "delete from Rhtcalcul" + " where idEntreprise = '" + dossier + "'" + " and aamm = '" + periode + "'" + " and nbul = " + numerobulletin);
			oSession.createQuery("delete from Rhtcalcul" + " where idEntreprise = '" + dossier + "'" + " and aamm = '" + periode + "'" + " and nbul = " + numerobulletin).executeUpdate();
			ParameterUtil.println("After Deleting rhtcalcul");
			//
			// BEGIN
			// DELETE FROM paev
			// WHERE cdos = wpdos.cdos
			// AND aamm = w_aamm
			// AND nbul = w_nbul;
			// EXCEPTION
			// WHEN OTHERS THEN null;
			// END;
			oSession.createQuery("delete from Rhteltvarent" + " where idEntreprise = '" + dossier + "'" + " and aamm = '" + periode + "'" + " and nbul = " + numerobulletin).executeUpdate();
			//
			// BEGIN
			// DELETE FROM paevar
			// WHERE cdos = wpdos.cdos
			// AND aamm = w_aamm
			// AND nbul = w_nbul;
			// EXCEPTION
			// WHEN OTHERS THEN null;
			// END;
			oSession.createQuery("delete from Rhteltvardet" + " where idEntreprise = '" + dossier + "'" + " and aamm = '" + periode + "'" + " and nbul = " + numerobulletin).executeUpdate();
			//
			// BEGIN
			// DELETE FROM paevcg
			// WHERE cdos = wpdos.cdos
			// AND aamm = w_aamm
			// AND nbul = w_nbul;
			// EXCEPTION
			// WHEN OTHERS THEN null;
			// END;
			oSession.createQuery("delete from Rhteltvarconge" + " where idEntreprise = '" + dossier + "'" + " and aamm = '" + periode + "'" + " and nbul = " + numerobulletin).executeUpdate();
			//
			//
			// BEGIN
			// DELETE FROM palog
			// WHERE cdos = wpdos.cdos;
			// EXCEPTION
			// WHEN OTHERS THEN null;
			// END;
			oSession.createQuery("delete from Rhtlog" + " where idEntreprise = '" + dossier + "'").executeUpdate();
			//
			//
			// -- LH 101298
			// BEGIN
			// DELETE FROM pablq
			// WHERE cdos = wpdos.cdos;
			// EXCEPTION
			// WHEN OTHERS THEN null;
			// END;
			oSession.createQuery("delete from Rhtblq" + " where idEntreprise = '" + dossier + "'").executeUpdate();

			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		
		//
		// COMMIT;
		//
		// RETURN TRUE;
		//
		// EXCEPTION
		// WHEN OTHERS THEN
		// RETURN FALSE;
		return true;
		//
		// END maj_dossier;
	}
	
	public boolean rcon_reg(String crub)
	{
	  if (nbrcon == 0)
			return false;

		for (int i = 0; i < nbrcon; i++)
		{
			if (crub.equals(this.listOfRubriqueCon.get(i)))
				return true;
		}
		return false;
	 
	}
	public void UpdateToString()
	{
		ParameterUtil.println("String dossier = " + dossier);
		ParameterUtil.println("int numerobulletin = " + numerobulletin);
		ParameterUtil.println("String periode = " + periode);
		ParameterUtil.println("String user = " + user);
		ParameterUtil.println("String langue = " + langue);
		ParameterUtil.println("ClsBulletin bulletin = " + bulletin.toString());
		ParameterUtil.println("ClsParameterOfPay parameter = " + parameter.toString());
		ParameterUtil.println("ClsDate myPeriode = " + myPeriode == null ?null : myPeriode.getDateS());
		ParameterUtil.println("String rubriqueNbreJourTravail = " + rubriqueNbreJourTravail);
		ParameterUtil.println("String rubriqueNbreJourPlage = " + rubriqueNbreJourPlage);
		ParameterUtil.println("boolean tab91 = " + tab91);
		ParameterUtil.println("boolean tab91b = " + tab91b);
		ParameterUtil.println("String moisPaieCourant = " + moisPaieCourant);
		ParameterUtil.println("String mois_ms = " + mois_ms);
		ParameterUtil.println("int nbul_ms = " + nbul_ms);
		ParameterUtil.println("String w_am99 = " + w_am99);
		ParameterUtil.println("boolean calc_part_auto = " + calc_part_auto);
		ParameterUtil.println("int max_part_fisc = " + max_part_fisc);
		ParameterUtil.println("String w_motif_reliq = " + w_motif_reliq);
		ParameterUtil.println("String rub_pnp = " + rub_pnp);
		ParameterUtil.println("String val_exp = " + val_exp);
		ParameterUtil.println("int typ_rec = " + typ_rec);
		ParameterUtil.println("String w_fictif = " + w_fictif);
		ParameterUtil.println("String w_typ_fictif = " + w_typ_fictif);
		ParameterUtil.println("int nbjc_lx = " + nbjc_lx);
		ParameterUtil.println("int nbjc_ex = "+nbjc_ex);
		ParameterUtil.println("int nbbul = "+nbbul);
		ParameterUtil.println("String editMatricule = "+editMatricule);
		ParameterUtil.println("String[]t_ev_mc = "+t_ev_mc[0]+"-"+t_ev_mc[1]+"-"+t_ev_mc[2]+"-"+t_ev_mc[3]+"-"+t_ev_mc[4]+"-"+t_ev_mc[5]+"-"+t_ev_mc[6]);
		ParameterUtil.println("String[]t_ev_ms = "+t_ev_ms[0]+"-"+t_ev_ms[1]+"-"+t_ev_ms[2]+"-"+t_ev_ms[3]+"-"+t_ev_ms[4]+"-"+t_ev_ms[5]+"-"+t_ev_ms[6]);
		 ParameterUtil.println("String rub_bc = "+rub_bc); 
		 ParameterUtil.println("String calcul_nbjsup = "+calcul_nbjsup); 
		 ParameterUtil.println("String ajout_bc = "+ajout_bc);
	  ParameterUtil.println("String pror_jr = "+pror_jr); 
	  ParameterUtil.println("String w_cg_abs = "+w_cg_abs); 
	  ParameterUtil.println("String raz_jour = "+raz_jour);  
	  ParameterUtil.println("String w_brut = "+w_brut);
	  ParameterUtil.println("boolean pror_jcg_normal = "+pror_jcg_normal); 
	  ParameterUtil.println("int age_max_enfant = "+age_max_enfant); 
	  ParameterUtil.println("boolean comptage_enfant = "+comptage_enfant); 
	  ParameterUtil.println("String dbcg_barem1 = "+dbcg_barem1); 
	  ParameterUtil.println("String fincg_barem1 = "+fincg_barem1); 
	  ParameterUtil.println("int nbjcg_barem1 = "+nbjcg_barem1); 
	  ParameterUtil.println("String dbcg_barem2 = "+dbcg_barem2); 
	  ParameterUtil.println("String fincg_barem2 = "+fincg_barem2); 
	  ParameterUtil.println("int nbjcg_barem2 = "+nbjcg_barem2); 
	  ParameterUtil.println("String dbcg_barem3 = "+dbcg_barem3); 
	  ParameterUtil.println("String fincg_barem3 = "+fincg_barem3); 
	  ParameterUtil.println("int nbjcg_barem3 = "+nbjcg_barem3); 
	  ParameterUtil.println("int w_type_dtanniv = "+w_type_dtanniv);
	  ParameterUtil.println("boolean t91hc = "+t91hc); 
	  ParameterUtil.println("boolean t91hp = "+t91hp); 
	  ParameterUtil.println("boolean t91mc =  "+t91mc); 
	  ParameterUtil.println("boolean t91mp = "+t91mp);
	  ParameterUtil.println("boolean t91in = "+t91mp); 
	  ParameterUtil.println("int nbrcon = "+nbrcon); 
	  ParameterUtil.println("String prochainmois = "+prochainmois); 
	  ParameterUtil.println("String w_cas = "+w_cas); 
	  ParameterUtil.println("double bas_con = "+bas_con); 
	  ParameterUtil.println("double w_moncp = "+w_moncp); 
	  ParameterUtil.println("int w_nbjcp = "+w_nbjcp);
	  ParameterUtil.println("double w_conge = "+w_conge); 
	  ParameterUtil.println("int w_nbjtr = "+w_nbjtr); 
	  ParameterUtil.println("boolean cgan_ms = "+cgan_ms); 
	  ParameterUtil.println("boolean exist_evcg = "+exist_evcg); 
	  ParameterUtil.println("int nb_evcg = "+nb_evcg); 
	  ParameterUtil.println("int w_nbjreliq = "+w_nbjreliq); 
	  ParameterUtil.println("boolean mois_conge = "+mois_conge); 
	  ParameterUtil.println("boolean sal_RA_MU = "+sal_RA_MU);
	  ParameterUtil.println("boolean sal_RA_MU_depuis = "+sal_RA_MU_depuis); 
	  ParameterUtil.println("boolean w_continue = "+w_continue); 
	  ParameterUtil.println("boolean w_retour = "+w_retour); 
	  ParameterUtil.println("String w_am =  "+w_am); 
	  ParameterUtil.println("boolean conges_annuels = "+conges_annuels); 
	  ParameterUtil.println("boolean finconge = "+finconge); 
	  ParameterUtil.println("boolean pnp = "+pnp); 
	  ParameterUtil.println("int wnbj_a = "+wnbj_a); 
	  ParameterUtil.println("int wnbj_c = "+wnbj_c); 
	  ParameterUtil.println("Date dtDdmp = "+dtDdmp);
	  ParameterUtil.println("Date dtDdex = "+dtDdmp); 
	  ParameterUtil.println("Rhpdo rhpdo = "+rhpdo);
	  ParameterUtil.println("boolean ca = "+ca); 
	  ParameterUtil.println("int nbj_con = "+nbj_con); 
	  ParameterUtil.println("String sav_pmcf = "+sav_pmcf); 
	  ParameterUtil.println("Date sav_dfcf = "+sav_dfcf);
	  ParameterUtil.println("Date sav_ddcf = "+sav_ddcf); 
	  ParameterUtil.println("Date w_date_anniv = "+w_date_anniv); 
	  ParameterUtil.println("int nbj_enf = "+nbj_enf); 
	  ParameterUtil.println("int nbj_anc = "+nbj_anc);
	  ParameterUtil.println("int nbj_deco = "+nbj_deco); 
	  ParameterUtil.println("int ancien = "+ancien); 
	  ParameterUtil.println("int minimum_enfant = "+minimum_enfant); 
	  ParameterUtil.println("int age_max_fiscal = "+age_max_fiscal); 
	  ParameterUtil.println("String nap = "+nap); 
	  ParameterUtil.println("String dateformat = "+dateformat); 
		  
		 
	}
}
