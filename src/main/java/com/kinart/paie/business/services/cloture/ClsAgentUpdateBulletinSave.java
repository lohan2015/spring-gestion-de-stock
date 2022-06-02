package com.kinart.paie.business.services.cloture;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;


public class ClsAgentUpdateBulletinSave
{
	/*
	private Session oSession = null;

	private Rhpagent agent = null;

	private String dossier = "";

	private int numerobulletin = 0;

	private String periode = "";

	private String user = "";

	private String langue = "";

	private ClsService service = null;

	private ClsBulletin bulletin = null;

	private ClsParameterOfPay parameter = null;

	private ClsDate myPeriode = null;

	private String rubriqueNbreJourTravail = "";

	private String rubriqueNbreJourPlage = "";

	private boolean tab91 = true;

	private boolean tab91b = true;

	//	private int nb_t91 = 0;
	private String moisPaieCourant = null;

	private String mois_ms = "";

	private int nbul_ms = 0;

	private List<ClsInfoPeriode> listOfPeriodeT91 = null;

	private String w_am99 = "";

	//	private int compt_sal = 0;
	private boolean calc_part_auto = false;

	private int max_part_fisc = 0;

	private String w_motif_reliq = "";

	private String rub_pnp = "";

	private String val_exp = "";

	private int typ_rec = 0;

	private String w_fictif = "";

	private String w_typ_fictif = "";

	private int nbjc_lx = 0;

	private int nbjc_ex = 0;

	//	private int nbbul = 0;
	//	private String editMatricule = "";
	private String[] t_ev_mc = null;

	private String[] t_ev_ms = null;

	private String rub_bc = "";

	private String calcul_nbjsup = "";

	private String ajout_bc = "";

	private String pror_jr = "";

	private String w_cg_abs = "";

	private String raz_jour = "";

	//	private String w_brut = "";
	private boolean pror_jcg_normal = false;

	private int age_max_enfant = 0;

	private boolean comptage_enfant = false;

	private String dbcg_barem1 = "";

	private String fincg_barem1 = "";

	private int nbjcg_barem1 = 0;

	private String dbcg_barem2 = "";

	private String fincg_barem2 = "";

	private int nbjcg_barem2 = 0;

	private String dbcg_barem3 = "";

	private String fincg_barem3 = "";

	private int nbjcg_barem3 = 0;

	private int w_type_dtanniv = 0;

	private boolean t91hc = false;

	private boolean t91hp = false;

	private boolean t91mc = false;

	private boolean t91mp = false;

	private boolean t91in = false;

	//	private int nbrcon = 0;
	private String prochainmois = "";

	//	private String w_cas = "";
	String error = "";

	private double bas_con = 0;

	private double w_moncp = 0;

	private int w_nbjcp = 0;

	private double w_conge = 0;

	private int w_nbjtr = 0;

	private boolean cgan_ms = false;

	private boolean exist_evcg = false;

	//	private int nb_evcg = 0;
	//
	private int w_nbjreliq = 0;

	//
	private boolean mois_conge = false;

	private boolean sal_RA_MU = false;

	private boolean sal_RA_MU_depuis = false;

	private boolean w_continue = false;

	private boolean w_retour = false;

	private String w_am = "";

	private boolean conges_annuels = false;

	private boolean finconge = false;

	private boolean pnp = false;

	//pour calc_nb_jours
	private int wnbj_a = 0;

	private int wnbj_c = 0;

	private Date dtDdmp = null;

	//	private Date dtDdex = null;
	private Rhpdo rhpdo = null;

	private boolean ca = false;

	private int nbj_con = 0;

	private String sav_pmcf = "";

	private Date sav_dfcf = null;

	private Date sav_ddcf = null;

	private Date w_date_anniv = null;

	private int nbj_enf = 0;

	private int nbj_anc = 0;

	private int nbj_deco = 0;

	private int ancien = 0;

	private int minimum_enfant = 0;

	private int age_max_fiscal = 0;

	//	private String nap = "";
	private String dateformat = "";

	private HttpServletRequest request;

	public int numsalariecourant;

	public int nbsalarietotal;

	public Map<RhtcumulPK, Rhtcumul> cumulsMap = new HashMap<RhtcumulPK, Rhtcumul>();

	public ClsUpdateBulletin updateBulletin;

	public synchronized ClsBulletin getBulletin()
	{
		return bulletin;
	}

	public synchronized void setBulletin(ClsBulletin bulletin)
	{
		this.bulletin = bulletin;
	}

	public synchronized Rhpagent getAgent()
	{
		return agent;
	}

	public synchronized void setAgent(Rhpagent agent)
	{
		this.agent = agent;
	}

	public ClsAgentUpdateBulletinSave(HttpServletRequest request, String user, ClsUpdateBulletin updateBulletin, ClsService service, Session oSession, ClsParameterOfPay parameter, String dateformat, Rhpagent agent)
	{
		this.request = request;
		this.agent = agent;
		this.service = service;
		this.oSession = oSession;
		//
		this.updateBulletin = updateBulletin;
		this.dossier = updateBulletin.dossier;
		this.bulletin = updateBulletin.bulletin;
		this.numerobulletin = updateBulletin.numerobulletin;
		this.periode = updateBulletin.periode;
		this.user = user;
		this.langue = updateBulletin.langue;
		this.service = service;
		this.parameter = parameter;
		this.myPeriode = updateBulletin.myPeriode;
		this.rubriqueNbreJourTravail = updateBulletin.rubriqueNbreJourTravail;
		this.rubriqueNbreJourPlage = updateBulletin.rubriqueNbreJourPlage;
		this.tab91 = updateBulletin.tab91;
		this.tab91b = updateBulletin.tab91b;
		//		this.nb_t91 = 0;
		this.moisPaieCourant = updateBulletin.moisPaieCourant;
		this.mois_ms = updateBulletin.mois_ms;
		this.nbul_ms = updateBulletin.nbul_ms;
		if (updateBulletin.listOfPeriodeT91 != null)
		{
			this.listOfPeriodeT91 = new ArrayList<ClsInfoPeriode>();
			this.listOfPeriodeT91.addAll(updateBulletin.listOfPeriodeT91);
		}
		else
			this.listOfPeriodeT91 = new ArrayList<ClsInfoPeriode>();
		//		this.myMoisPaieCourant = updateBulletin.myMoisPaieCourant;
		this.w_am99 = updateBulletin.w_am99;
		//com.cdi.deltarh.service.ClsParameter.println(">>>>>>>>>>>>>>>>>>>>----<<<<<<<<<<<<<<<<<<<<<Valeur de la p�riode = "+w_am99);
		//		this.compt_sal = 0;
		this.calc_part_auto = updateBulletin.calc_part_auto;
		this.max_part_fisc = updateBulletin.max_part_fisc;
		this.w_motif_reliq = updateBulletin.w_motif_reliq;
		this.rub_pnp = updateBulletin.rub_pnp;
		this.val_exp = updateBulletin.val_exp;
		this.typ_rec = updateBulletin.typ_rec;
		this.w_fictif = updateBulletin.w_fictif;
		this.w_typ_fictif = updateBulletin.w_typ_fictif;
		this.nbjc_lx = updateBulletin.nbjc_lx;
		this.nbjc_ex = updateBulletin.nbjc_ex;
		//		this.nbbul = 0;
		//		this.editMatricule = "";
		this.t_ev_mc = updateBulletin.t_ev_mc;
		this.t_ev_ms = updateBulletin.t_ev_ms;
		this.rub_bc = updateBulletin.rub_bc;
		this.calcul_nbjsup = updateBulletin.calcul_nbjsup;
		this.ajout_bc = updateBulletin.ajout_bc;
		this.pror_jr = updateBulletin.pror_jr;
		this.w_cg_abs = updateBulletin.w_cg_abs;
		this.raz_jour = updateBulletin.raz_jour;
		//		this.w_brut = "";
		this.pror_jcg_normal = updateBulletin.pror_jcg_normal;
		this.age_max_enfant = updateBulletin.age_max_enfant;
		this.comptage_enfant = updateBulletin.comptage_enfant;
		this.dbcg_barem1 = updateBulletin.dbcg_barem1;
		this.fincg_barem1 = updateBulletin.fincg_barem1;
		this.nbjcg_barem1 = updateBulletin.nbjcg_barem1;
		this.dbcg_barem2 = updateBulletin.dbcg_barem2;
		this.fincg_barem2 = updateBulletin.fincg_barem2;
		this.nbjcg_barem2 = updateBulletin.nbjcg_barem2;
		this.dbcg_barem3 = updateBulletin.dbcg_barem3;
		this.fincg_barem3 = updateBulletin.fincg_barem3;
		this.nbjcg_barem3 = updateBulletin.nbjcg_barem3;
		this.w_type_dtanniv = updateBulletin.w_type_dtanniv;
		this.t91hc = updateBulletin.t91hc;
		this.t91hp = updateBulletin.t91hp;
		this.t91mc = updateBulletin.t91mp;
		this.t91mp = updateBulletin.t91mp;
		this.t91in = updateBulletin.t91in;
		//		this.nbrcon = updateBulletin.nbjc_lx;
		this.prochainmois = updateBulletin.prochainmois;
		//		this.w_cas = "";
		String error = updateBulletin.error;
		this.bas_con = updateBulletin.bas_con;
		this.w_moncp = updateBulletin.w_moncp;
		this.w_nbjcp = updateBulletin.w_nbjcp;
		this.w_conge = updateBulletin.w_conge;
		this.w_nbjtr = updateBulletin.w_nbjtr;
		this.cgan_ms = updateBulletin.cgan_ms;
		this.exist_evcg = updateBulletin.exist_evcg;
		//		this.nb_evcg = 0;
		//
		this.w_nbjreliq = updateBulletin.w_nbjreliq;
		//
		this.mois_conge = updateBulletin.mois_conge;
		this.sal_RA_MU = updateBulletin.sal_RA_MU;
		this.sal_RA_MU_depuis = updateBulletin.sal_RA_MU_depuis;
		this.w_continue = updateBulletin.w_continue;
		this.w_retour = updateBulletin.w_retour;
		this.w_am = updateBulletin.w_am;
		this.conges_annuels = updateBulletin.conges_annuels;
		this.finconge = updateBulletin.finconge;
		this.pnp = updateBulletin.pnp;
		//pour calc_nb_jours
		this.wnbj_a = updateBulletin.wnbj_a;
		this.wnbj_c = updateBulletin.wnbj_c;
		this.dtDdmp = updateBulletin.dtDdmp;
		//		this.dtDdex = null;
		this.rhpdo = updateBulletin.rhpdo;
		this.ca = updateBulletin.ca;
		this.nbj_con = updateBulletin.nbj_con;
		this.sav_pmcf = updateBulletin.sav_pmcf;
		this.sav_dfcf = updateBulletin.sav_dfcf;
		this.sav_ddcf = updateBulletin.sav_ddcf;
		this.w_date_anniv = updateBulletin.w_date_anniv;
		this.nbj_enf = updateBulletin.nbj_enf;
		this.nbj_anc = updateBulletin.nbj_anc;
		this.nbj_deco = updateBulletin.nbj_deco;
		this.ancien = updateBulletin.ancien;
		this.minimum_enfant = updateBulletin.minimum_enfant;
		this.age_max_fiscal = updateBulletin.age_max_fiscal;
		//
		this.dateformat = dateformat;
	}

	public boolean updateBulletin()
	{
		com.cdi.deltarh.service.ClsParameter.println(">>updateBulletin");
		try
		{

			//		---------------------------------------------------------------------------
			//		-- Mise a jour des bulletins
			//		---------------------------------------------------------------------------
			//		FUNCTION majbul RETURN BOOLEAN
			//		IS
			//
			//		annee      VARCHAR2(4);
			//		mois       VARCHAR2(2);
			//		sal_rst    VARCHAR2(4);
			//		wsal_rst   SMALLINT;
			//
			//		CURSOR curs_sal IS
			//		     select pasa01.* from pasa01
			//		      where cdos = wpdos.cdos
			//		      order by cdos, nmat;
			//		String queryString = "from Rhpagent" +
			//				" where comp_id.cdos = '" + dossier + "'" +
			//				" order by com_id.cdos, comp_id.nmat";
			//
			//		BEGIN
			//		-- ----- Calcul du nbre de salaries a traiter
			//		  w_retour := chg_sal;
			//		  wsal_rst := nb_sal;
			//
			//		  WHILE true LOOP
			//		    LOCK TABLE pasa01 IN EXCLUSIVE MODE;
			//		    IF SQLCODE = 0 THEN
			//		       LOCK TABLE pados IN EXCLUSIVE MODE;
			//		       IF SQLCODE = 0 THEN
			//		          LOCK TABLE paevar IN EXCLUSIVE MODE;
			//		          IF SQLCODE = 0 THEN
			//		             LOCK TABLE paevcg IN EXCLUSIVE MODE;
			//		             IF SQLCODE = 0 THEN
			//		                LOCK TABLE paprent IN EXCLUSIVE MODE;
			//		                IF SQLCODE = 0 THEN
			//		                   LOCK TABLE paprlig IN EXCLUSIVE MODE;
			//		                   IF SQLCODE = 0 THEN
			//		                      LOCK TABLE pacalc IN EXCLUSIVE MODE;
			//		                      IF SQLCODE = 0 THEN
			//		                         LOCK TABLE pacumu IN EXCLUSIVE MODE;
			//		                         IF SQLCODE = 0 THEN
			//		                            LOCK TABLE paconge IN EXCLUSIVE MODE;
			//		                            IF SQLCODE = 0 THEN
			//		                               LOCK TABLE pafic IN EXCLUSIVE MODE;
			//		                               IF SQLCODE = 0 THEN
			//		                                  EXIT;
			//		                               END IF;
			//		                            END IF;
			//		                         END IF;
			//		                      END IF;
			//		                   END IF;
			//		                END IF;
			//		             END IF;
			//		          END IF;
			//		       END IF;
			//		    END IF;
			//		    EXIT;
			//		  END LOOP;
			//
			//		  OPEN curs_sal;
			//		List l = service.find(queryString);
			//		for (Object obj : l) {
			//		  LOOP
			//		    FETCH curs_sal INTO wsal01;
			//		    EXIT WHEN curs_sal%NOTFOUND;
			//
			//		       sav_pmcf := wsal01.pmcf;
			//		       sav_dfcf := wsal01.dfcf;
			//		       sav_ddcf := wsal01.ddcf;
			sav_pmcf = agent.getPmcf();
			sav_dfcf = agent.getDfcf();
			sav_ddcf = agent.getDdcf();
			//
			//		       IF w_type_dtanniv = 1 THEN
			//		          w_date_anniv := wsal01.dtes;
			//		       ELSIF w_type_dtanniv = 2 THEN
			//		          w_date_anniv := wsal01.ddca;
			//		       ELSIF w_type_dtanniv = 3 THEN
			//		          w_date_anniv := wsal01.drtcg;
			//		       END IF;
			if (w_type_dtanniv == 1)
				w_date_anniv = agent.getDtes();
			else if (w_type_dtanniv == 2)
				w_date_anniv = agent.getDdca();
			else if (w_type_dtanniv == 3)
				w_date_anniv = agent.getDrtcg();
			//
			//		       DBMS_PIPE.PACK_MESSAGE('I');
			//		       w_mess := wsal01.nmat || ltrim(to_char(wsal_rst,'0999'));
			//		       DBMS_PIPE.PACK_MESSAGE(w_mess);
			//		       w_status := DBMS_PIPE.SEND_MESSAGE(w_pipe_name);
			//
			//		       wsal_rst := wsal_rst - 1;
			//
			//		       tab91 := tab91b;
			tab91 = tab91b;
			//
			//		       IF tab91 THEN
			//		          IF NOT t91hc AND wsal01.cods = 'HC' THEN
			//		              tab91 := FALSE;
			//		          END IF;
			//		          IF NOT t91hp AND wsal01.cods = 'HP' THEN
			//		              tab91 := FALSE;
			//		          END IF;
			//		          IF NOT t91mc AND wsal01.cods = 'MC' THEN
			//		              tab91 := FALSE;
			//		          END IF;
			//		          IF NOT t91mp AND wsal01.cods = 'MP' THEN
			//		              tab91 := FALSE;
			//		          END IF;
			//		          IF NOT t91in AND wsal01.cods = 'IN' THEN
			//		              tab91 := FALSE;
			//		          END IF;
			//		       END IF;
			if (tab91)
			{
				if ((!t91hc) && "HC".equals(agent.getCods()))
					tab91 = false;
				if ((!t91hp) && "HP".equals(agent.getCods()))
					tab91 = false;
				if ((!t91mc) && "MC".equals(agent.getCods()))
					tab91 = false;
				if ((!t91mp) && "MP".equals(agent.getCods()))
					tab91 = false;
				if ((!t91in) && "IN".equals(agent.getCods()))
					tab91 = false;
			}
			//
			//		       -- Le salarie est il radie ou mute ?
			//		       sal_RA_MU        := FALSE;
			//		       sal_RA_MU_depuis := FALSE;
			//		       IF (wsal01.mrrx = 'MU' OR wsal01.mrrx = 'RA')
			//		          AND wsal01.dmrr is not null AND w_nbul = 9 THEN
			//		          char6 := to_char(wsal01.dmrr,'YYYY')||to_char(wsal01.dmrr,'MM');
			//		          IF w_aamm = char6 THEN
			//		             sal_RA_MU := TRUE;
			//		          END IF;
			//		          IF w_aamm > char6 THEN
			//		             sal_RA_MU_depuis := TRUE;
			//		          END IF;
			//		       END IF;
			sal_RA_MU = false;
			sal_RA_MU_depuis = false;
			if (("MU".equals(agent.getMrrx()) || "RA".equals(agent.getMrrx())) && agent.getDmrr() != null && numerobulletin == 9)
			{
				String char6 = new SimpleDateFormat("yyyyMM").format(agent.getDmrr());
				if (periode.equals(char6))
					sal_RA_MU = true;
				if (periode.compareTo(char6) > 0)
					sal_RA_MU_depuis = true;
			}
			//
			//		       -- Le salarie a ete calcule mais mis a calcul NON depuis
			//		       w_continue:=FALSE;
			w_continue = false;
			//		       IF wsal01.cals = 'N' THEN
			//		          IF sal_RA_MU_depuis THEN
			//		             w_retour := sup_eve;
			//		             w_continue:=TRUE;
			//		          END IF;
			//		          IF NOT sal_RA_MU THEN
			//		             IF NOT trans_ev THEN
			//		                CLOSE curs_sal;
			//		                RETURN FALSE;
			//		             END IF;
			//		             IF w_nbul = 9 THEN
			//		                IF NOT trans_cong THEN
			//		                   CLOSE curs_sal;
			//		                   RETURN FALSE;
			//		                END IF;
			//		                IF NOT trans_prets THEN
			//		                   CLOSE curs_sal;
			//		                   RETURN FALSE;
			//		                END IF;
			//		             END IF;
			//		             w_retour := sup_eve;
			//		             w_retour := sup_paelfix;
			//		             w_continue:=TRUE;
			//		          END IF;
			//		       END IF;
			if ("N".equals(agent.getCals()))
			{
				if (sal_RA_MU_depuis)
				{
					w_retour = deleteEVMois(agent);
					w_continue = true;
				}
				if (!sal_RA_MU)
				{
					if (!transferEVMoisSuivant(agent.getComp_id().getNmat()))
						return false;
					if (numerobulletin == 9)
					{
						if (!transferEVCGMoisSuivant(agent.getComp_id().getNmat()))
							return false;
						if (!transferEcheancePretMoisSuivant(agent.getComp_id().getNmat()))
							return false;
					}
					w_retour = deleteEVMois(agent);
					w_retour = deleteEFMois(agent);
					w_continue = true;
				}
			}
			//		       if w_continue = FALSE then
			if (w_continue == false)
			{
				//		          finconge := FALSE;
				//		          pnp      := FALSE;
				//		          ca       := FALSE;
				//		          conges_annuels  := FALSE;
				finconge = false;
				pnp = false;
				ca = false;
				conges_annuels = false;
				//
				//		          -- Controle des zones servant dans le conge, car celles-ci ont pu
				//		          -- etre initialisees a NULL dans la saisie des salaries.
				//		          IF wsal01.japa IS NULL THEN
				//		             wsal01.japa := 0;
				//		          END IF;
				if (agent.getJapa() == null)
					agent.setJapa(new BigDecimal(0));
				//		          IF wsal01.japec IS NULL THEN
				//		             wsal01.japec := 0;
				//		          END IF;
				if (agent.getJapec() == null)
					agent.setJapec(new BigDecimal(0));
				//		          IF wsal01.nbjse IS NULL THEN
				//		             wsal01.nbjse := 0;
				//		          END IF;
				if (agent.getNbjse() == null)
					agent.setNbjse(new BigDecimal(0));
				//		          IF wsal01.nbjsa IS NULL THEN
				//		             wsal01.nbjsa := 0;
				//		          END IF;
				if (agent.getNbjsa() == null)
					agent.setNbjsa(new BigDecimal(0));
				//		          IF wsal01.nbjsm IS NULL THEN
				//		             wsal01.nbjsm := 0;
				//		          END IF;
				if (agent.getNbjsm() == null)
					agent.setNbjsm(new BigDecimal(0));
				//		          IF wsal01.jrla IS NULL THEN
				//		             wsal01.jrla := 0;
				//		          END IF;
				if (agent.getJrla() == null)
					agent.setJrla(new BigDecimal(0));
				//		          IF wsal01.jrlec IS NULL THEN
				//		             wsal01.jrlec := 0;
				//		          END IF;
				if (agent.getJrlec() == null)
					agent.setJrlec(new BigDecimal(0));
				//		          IF wsal01.jded IS NULL THEN
				//		             wsal01.jded := 0;
				//		          END IF;
				if (agent.getJded() == null)
					agent.setJded(new BigDecimal(0));
				//		          IF wsal01.dapa IS NULL THEN
				//		             wsal01.dapa := 0;
				//		          END IF;
				if (agent.getDapa() == null)
					agent.setDapa(new BigDecimal(0));
				//		          IF wsal01.dapec IS NULL THEN
				//		             wsal01.dapec := 0;
				//		          END IF;
				if (agent.getDapec() == null)
					agent.setDapec(new BigDecimal(0));
				//		          IF wsal01.dded IS NULL THEN
				//		             wsal01.dded := 0;
				//		          END IF;
				if (agent.getDded() == null)
					agent.setDded(new BigDecimal(0));
				//		          IF wsal01.mtcf IS NULL THEN
				//		             wsal01.nbjtr := 0;
				//		          END IF;
				if (agent.getMtcf() == null)
					//agent.setMtcf(new BigDecimal(0));
					agent.setNbjtr(new BigDecimal(0));
				//		          IF wsal01.nbjtr IS NULL THEN
				//		             wsal01.nbjtr := 0;
				//		          END IF;
				if (agent.getNbjtr() == null)
					agent.setNbjtr(new BigDecimal(0));
				//
				//		          -- Lecture des conges
				//		          w_retour := lect_ev;
				w_retour = lectureElementVariable(agent.getComp_id().getNmat());
				//
				//		          -- Traitement des elements de la table de calcul
				//		          IF NOT lect_rub_cal THEN
				//		             CLOSE curs_sal;
				//		             RETURN FALSE;
				//		          END IF;

				if (!lectureCalculSurMoisCourant(agent))
				{
					return false;
				}

				//
				//		          -- Transfert des EV conges sur mois suivant
				//		          IF cgan_ms AND NOT sal_RA_MU AND w_nbul = 9 THEN
				//		             IF NOT alim_evcg_ms THEN
				//		                CLOSE curs_sal;
				//		                RETURN FALSE;
				//		             END IF;
				//		          END IF;
				if (cgan_ms && (!sal_RA_MU) && numerobulletin == 9)
				{
					if (!alimenteCongeMoisProchain(agent.getComp_id().getNmat()))
						return false;
				}
				//
				//		          -- M a j des droits conges
				//		          IF exist_evcg AND w_nbul = 9 THEN
				//		             w_retour := maj_con;
				//		          END IF;
				if (exist_evcg && numerobulletin == 9)
					w_retour = updateZoneConge(agent);
				//
				//		          -- M a j de la base conges
				//		          IF w_nbul = 9 THEN
				//		             w_retour := maj_bas;
				//		          ELSE
				//		             wsal01.dapec := wsal01.dapec + bas_con;
				//		             UPDATE pasa01 SET dapec = wsal01.dapec
				//		              WHERE cdos = wpdos.cdos AND nmat = wsal01.nmat;
				//		          END IF;
				if (numerobulletin == 9)
				{
					w_retour = updateBaseCongeAgent(agent);
				}
				else
				{
					if (agent.getDapec() != null)
						agent.setDapec(agent.getDapec().add(new BigDecimal(bas_con)));
					else
						agent.setDapec(new BigDecimal(bas_con));
					//
					oSession.update(agent);
				}
				//
				//		          -- Passage des EV et modif. des prets si le salarie
				//		          -- est a calcul Oui, mais n'a pas ete calcule
				//		          -- MM 17/05/2001 controle sur toutes rubriques
				//		
				//		          w_count := 0;
				//		          BEGIN
				//		             SELECT count(*) INTO w_count FROM pacalc
				//		              WHERE cdos = wpdos.cdos
				//		                AND  aamm = w_aamm
				//		                AND  nmat = wsal01.nmat
				//		                AND  nbul = w_nbul;
				//		          EXCEPTION
				//		             WHEN NO_DATA_FOUND THEN w_count := 0;
				//		          END;
				//		          IF w_count = 0 THEN
				//		             w_retour := trans_ev;
				//		             IF w_nbul = 9 THEN
				//		                w_retour := trans_cong;
				//		                w_retour := trans_prets;
				//		             END IF;
				//		          END IF;
	
				Object o = service.find("select count(*) from Rhtcalcul" + " where comp_id.cdos = '" + dossier + "'" + " and comp_id.aamm = '" + periode + "'" + " and comp_id.nmat = '" + agent.getComp_id().getNmat() + "'" + " and comp_id.nbul = " + numerobulletin).get(0);
				int count = 0;
				if (o != null)
					count = ((Long) o).intValue();
				if (count == 0)
				{
					w_retour = transferEVMoisSuivant(agent.getComp_id().getNmat());
					if (numerobulletin == 9)
					{
						w_retour = transferEVCGMoisSuivant(agent.getComp_id().getNmat());
						w_retour = transferEcheancePretMoisSuivant(agent.getComp_id().getNmat());
					}
				}
				//
				//		          -- Suppression des EV du mois
				//		          w_retour := sup_eve;
				w_retour = deleteEVMois(agent);
				//
				//		          -- Suppression des elements fixes dont la date de fin
				//		          -- est <= a la periode cloturee
				//		          w_retour := sup_paelfix;
				w_retour = deleteEFMois(agent);
				//
				//		          -- MM 22/06/2004
				//		          IF calc_part_auto THEN
				//		             IF NOT calc_partfisc THEN
				//		                w_mess := PA_PAIE.erreurp('ERR-10518',w_clang,wsal01.nmat);
				//		                RETURN FALSE;
				//		             END IF;
				//		          END IF;
				if (calc_part_auto)
				{
					if (!calculPartFiscale(agent))
					{
						error = parameter.errorMessage("ERR-10518", langue, agent.getComp_id().getNmat());
						com.cdi.deltarh.service.ClsParameter.println(error);
						ClsGlobalUpdate._setEvolutionTraitement(request, error, true);
						return false;
					}
				}
				//
				//		            -- Suppression du fichier fictif
				//		          IF NOT PA_PAIE.NouB(sav_pmcf) AND sav_pmcf = w_aamm
				//		                                        AND w_nbul = 9 THEN
				//		             w_retour := sup_fic;
				//		          END IF;
				if ((!ClsObjectUtil.isNull(sav_pmcf)) && sav_pmcf.equals(periode) && numerobulletin == 9)
				{
					oSession.createQuery("delete from Rhtfic" + " where cdos = '" + dossier + "'" + " and nmat = '" + agent.getComp_id().getNmat() + "'" + " and nbul = " + numerobulletin).executeUpdate();
				}
				//		       END IF;
			}
			//		    END LOOP;
			//		    CLOSE curs_sal;
			//		}
			//
			//		    RETURN TRUE;
			//
			//		END majbul;
			//

			this.agentUpdateToString();

			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}

	}

	
	 * =>maj_pnp Conges payes non pris a partir des rubriques
	 *
	private boolean updateCongePayeNonPris(Rhpagent agent, BigDecimal mont)
	{
		com.cdi.deltarh.service.ClsParameter.println(">>updateCongePayeNonPris");
		//		FUNCTION maj_pnp RETURN BOOLEAN
		//		IS
		//
		//		  nbjstot   SMALLINT;
		//		  wnbjcad   SMALLINT;
		//		  wnbjcad2  SMALLINT;
		int wnbjcad2 = 0;
		int wnbjcad = 0;
		int nbjstot = 0;
		//
		//		BEGIN
		//		----- Si que des conges payes non pris, alors remise a zero de toutes
		//		----- les zones de droit et de jours
		//		----- pc le 26.04.95
		//		  IF exist_evcg = FALSE THEN
		if (exist_evcg == false)
		{
			//		     wsal01.japa  := 0;
			//		     wsal01.japec := 0;
			//		     wsal01.jrla  := 0;
			//		     wsal01.jrlec := 0;
			//		     wsal01.jded  := 0;
			//		     wsal01.nbjse := 0;
			//		     wsal01.nbjsa := 0;
			//		     wsal01.nbjsm := 0;
			//		     wsal01.ddcf  := NULL;
			//		     wsal01.dfcf  := NULL;
			//		     wsal01.ddenv := NULL;
			//		     wsal01.drenv := NULL;
			//		     wsal01.nbjtr := 0;
			//		     wsal01.pmcf  := ' ';
			//		     wsal01.nbjcf := 0;
			//		     wsal01.nbjaf := 0;
			//		     wsal01.drtcg := NULL;
			//		     wsal01.dapa  := 0;
			//		     wsal01.dded  := 0;
			//		     wsal01.ddcf  := NULL;
			//		     wsal01.dfcf  := NULL;
			//		     wsal01.nbjcf := 0;
			//		     wsal01.nbjaf := 0;
			//		     wsal01.jded  := 0;
			//		     wsal01.pmcf  := ' ';
			//		     wsal01.dapec := 0;
			//		     wsal01.mtcf  := 0;
			agent.setJapa(new BigDecimal(0));
			agent.setJapec(new BigDecimal(0));
			agent.setJrla(new BigDecimal(0));
			agent.setJrlec(new BigDecimal(0));
			agent.setJded(new BigDecimal(0));
			agent.setNbjse(new BigDecimal(0));
			agent.setNbjsa(new BigDecimal(0));
			agent.setNbjsm(new BigDecimal(0));
			agent.setDdcf(null);
			agent.setDfcf(null);
			agent.setDdenv(null);
			agent.setDrenv(null);
			agent.setNbjtr(new BigDecimal(0));
			agent.setPmcf("");
			agent.setNbjcf(new BigDecimal(0));
			agent.setNbjaf(new BigDecimal(0));
			agent.setDrtcg(null);
			agent.setDapa(new BigDecimal(0));
			agent.setDded(new BigDecimal(0));
			agent.setDdcf(null);
			agent.setDfcf(null);
			agent.setJded(new BigDecimal(0));
			agent.setDapec(new BigDecimal(0));
			agent.setMtcf(new BigDecimal(0));

		}
		//		  ELSE
		else
		{
			//		     wnbjcad := wcalc.mont;
			wnbjcad = mont.intValue();
			//		----- pc le 26.04.95, si des conges ont ete saisis,le nombre de jours
			//		----- de conges payes non pris n'est plus soustrait du nombre de jours
			//		----- de la base
			//		-- ----- Mise a jour des droits en jrs supp
			//		     nbjstot := wsal01.nbjse + wsal01.nbjsa + wsal01.nbjsm;
			nbjstot = agent.getNbjse().intValue() + agent.getNbjsa().intValue() + agent.getNbjsm().intValue();
			//
			//		     IF wnbjcad >= nbjstot THEN
			//		        wsal01.nbjse := 0;
			//		        wsal01.nbjsa := 0;
			//		        wsal01.nbjsm := 0;
			//		     ELSE
			//		        wnbjcad2 := nbjstot;
			//		        IF wnbjcad2 >= wsal01.nbjsa THEN
			//		           wnbjcad2 := wnbjcad2 - wsal01.nbjsa;
			//		           wsal01.nbjsa := 0;
			//		           IF wnbjcad2 >= wsal01.nbjse THEN
			//		              wnbjcad2 := wnbjcad2 - wsal01.nbjse;
			//		              wsal01.nbjse := 0;
			//		              IF wnbjcad2 >= wsal01.nbjsm THEN
			//		                 wsal01.nbjsm := 0;
			//		              ELSE
			//		                 wsal01.nbjsm := wsal01.nbjsm - wnbjcad2;
			//		              END IF;
			//		           ELSE
			//		              wsal01.nbjse := wsal01.nbjse - wnbjcad2;
			//		           END IF;
			//		        ELSE
			//		           wsal01.nbjsa := wsal01.nbjsa - wnbjcad2;
			//		        END IF;
			//		     END IF;
			if (wnbjcad >= nbjstot)
			{
				agent.setNbjse(new BigDecimal(0));
				agent.setNbjsa(new BigDecimal(0));
				agent.setNbjsm(new BigDecimal(0));
			}
			else
			{
				wnbjcad2 = nbjstot;
				if (wnbjcad2 >= agent.getNbjsa().intValue())
				{
					wnbjcad2 -= agent.getNbjsa().intValue();
					agent.setNbjsa(new BigDecimal(0));
					if (wnbjcad2 >= agent.getNbjse().intValue())
					{
						wnbjcad2 -= agent.getNbjse().intValue();
						agent.setNbjse(new BigDecimal(0));
						if (wnbjcad2 >= agent.getNbjsm().intValue())
						{
							agent.setNbjsm(new BigDecimal(0));
						}
						else
						{
							agent.setNbjsm(new BigDecimal(agent.getNbjsm().intValue() - wnbjcad2));
						}
					}
					else
					{
						agent.setNbjse(new BigDecimal(agent.getNbjse().intValue() - wnbjcad2));
					}
				}
				else
				{
					agent.setNbjsa(new BigDecimal(agent.getNbjsa().intValue() - wnbjcad2));
				}
			}
			//
			//		     -- Mise a jour des droits en jrs
			//		     IF wnbjcad <= wsal01.japa THEN
			//		        wsal01.japa := wsal01.japa - wnbjcad;
			//		     ELSE
			//		        wsal01.japec := wsal01.japec - ( wnbjcad - wsal01.japa );
			//		        wsal01.japa  := 0;
			//		     END IF;
			if (wnbjcad <= agent.getJapa().intValue())
			{
				agent.setJapa(new BigDecimal(agent.getJapa().intValue() - wnbjcad));
			}
			else
			{
				agent.setJapec(new BigDecimal(agent.getJapec().intValue() - (wnbjcad - agent.getJapa().intValue())));
				agent.setJapa(new BigDecimal(0));
			}

			ClsParameter.println("-------Pour le salari� " + agent.getComp_id().getNmat() + ", Japa=" + agent.getJapa() + ", Japec=" + agent.getJapec() + " pour nbjcad=" + wnbjcad);
			//
			//		     wsal01.jrlec := wsal01.jrlec + wnbjcad;
			agent.setJrlec(new BigDecimal(agent.getJrlec().intValue() + wnbjcad));
			//
			//		     IF wsal01.ddcf IS NULL THEN
			//		         conges_annuels := TRUE;
			//		     END IF;
			if (agent.getDdcf() == null)
				conges_annuels = true;
			//
			//		  END IF;
		}
		//
		//		  pnp := TRUE;
		pnp = true;
		//
		//		  RETURN TRUE;
		//
		//		END maj_pnp;
		//
		return true;
	}

	
	 * =>maj_pret13 Mise a jour de la table des prets (fiche salarie)
	 *
	private boolean updatePret13(String nmat, int lg, String crub, BigDecimal mont)
	{
		com.cdi.deltarh.service.ClsParameter.println(">>updatePret13");
		//		w_rowid   ROWID;
		//		  wprets    paprets%ROWTYPE;
		//
		//		BEGIN
		//		  BEGIN
		//		     SELECT rowid, mtpr, mtremb
		//		       INTO w_rowid, wprets.mtpr, wprets.mtremb FROM paprets
		//		      WHERE cdos = wpdos.cdos
		//		        AND nmat = wsal01.nmat
		//		        AND lg   = wcalc.nprt
		//		        AND crub = wcalc.rubq;
		//		  EXCEPTION
		//		     WHEN NO_DATA_FOUND THEN
		//		       w_mess := PA_PAIE.erreurp('ERR-90132',w_clang,wsal01.nmat,wcalc.rubq,wcalc.nprt);
		//		       RETURN FALSE;
		//		  END;
		//
		//		  IF PA_PAIE.NouZ(wprets.mtremb) THEN
		//		     wprets.mtremb := 0;
		//		  END IF;
		//		  wprets.mtremb := wprets.mtremb + wcalc.mont;
		//
		//		  UPDATE paprets SET mtremb = wprets.mtremb WHERE rowid = w_rowid;

		Object o = service.get(Rhtpretsagent.class, new RhtpretsagentPK(dossier, nmat, lg));
		if (o != null)
		{
			Rhtpretsagent pret = (Rhtpretsagent) o;
			if (ClsObjectUtil.isNull(pret.getMtremb()))
				pret.setMtremb(new BigDecimal(0));
			pret.setMtremb(pret.getMtremb().add(mont));
			//
			oSession.update(pret);
		}
		else
		{
			error = parameter.errorMessage("ERR-90132", langue, nmat, crub, "XXX");
			ClsGlobalUpdate._setEvolutionTraitement(request, error, true);
			com.cdi.deltarh.service.ClsParameter.println(error);
			return false;
		}
		//
		//		  RETURN TRUE;
		//
		//		END maj_pret13;
		//
		return false;
	}

	**
	 * =>maj_pret17 Mise a jour des zones de pret dans PAPRENT
	 *
	private boolean updatePret17(String nmat, String crub, String nprt, BigDecimal mont)
	{
		com.cdi.deltarh.service.ClsParameter.println(">>updatePret17");
		//		w_rowid ROWID;
		//		w_echo  paprlig.echo%TYPE;
		//
		//		BEGIN
		//		  BEGIN
		//		    SELECT rowid, paprent.*
		//		      INTO w_rowid, wprent.cdos,wprent.nprt,wprent.nmat,wprent.crub,
		//		           wprent.com1,wprent.com2,wprent.dmep,wprent.dpec,wprent.per1,
		//		           wprent.mntp,wprent.resr,wprent.nbec,wprent.nber,wprent.mtec,
		//		           wprent.tint,wprent.ttax,wprent.pact,wprent.etpr,wprent.dcrp,
		//		           wprent.ddmo,wprent.ncpt
		//		      FROM paprent
		//		     WHERE cdos = wpdos.cdos
		//		       AND nmat = wsal01.nmat
		//		       AND nprt = wcalc.nprt
		//		       AND crub = wcalc.rubq;
		//		  EXCEPTION
		//		    WHEN NO_DATA_FOUND THEN null;
		//		  END;
		//
		//		  IF SQL%NOTFOUND THEN
		//		     w_mess := PA_PAIE.erreurp('ERR-90133',w_clang,wsal01.nmat,wcalc.rubq,wcalc.nprt);
		//		     RETURN FALSE;
		//		END IF;

		Rhtpretent pretent = null;
		Object o = service.get(Rhtpretent.class, new RhtpretentPK(dossier, nprt));
		if (o == null)
		{
			error = parameter.errorMessage("ERR-90133", langue, nmat, crub, nprt);
			com.cdi.deltarh.service.ClsParameter.println(error);
			ClsGlobalUpdate._setEvolutionTraitement(request, error, true);
			return false;
		}
		else
		{
			pretent = (Rhtpretent) o;
			if (!(nmat.equals(pretent.getNmat()) && crub.equals(pretent.getCrub())))
			{
				error = parameter.errorMessage("ERR-90133", langue, nmat, crub, nprt);
				com.cdi.deltarh.service.ClsParameter.println(error);
				ClsGlobalUpdate._setEvolutionTraitement(request, error, true);
				return false;
			}
		}
		//
		//		-- ----- Mise a jour du reste a rembourser
		//		IF NOT PA_PAIE.NouZ(wprent.resr) THEN
		if (!ClsObjectUtil.isNull(pretent.getResr()))
		{
			//		  --IF f_cas = 1 THEN
			//		      wprent.resr := wprent.resr - wcalc.mont;
			//		     IF wprent.resr <= 0 THEN
			//		         wprent.resr := 0;
			//		     END IF;
			pretent.setResr(pretent.getResr().subtract(mont));
			if (pretent.getResr().intValue() <= 0)
				pretent.setResr(new BigDecimal(0));
			//		  -- MM 12/2003 pour tous les cas de figure on prend le montant calcule ( Ref pb Fictif B)
			//		  --ELSE
			//		  --  BEGIN
			//		  --   SELECT echo INTO w_echo FROM paprlig
			//		  --    WHERE cdos = wpdos.cdos
			//		  --      AND nprt = wprent.nprt
			//		  --      AND perb = TO_DATE(w_aamm,'YYYYMM');
			//		  --  EXCEPTION
			//		  --   WHEN NO_DATA_FOUND THEN null;
			//		  --  END;
			//		  --   IF SQLCODE = 0 THEN
			//		  --       wprent.resr := wprent.resr - w_echo;
			//		  --      IF wprent.resr <= 0 THEN
			//		  --          wprent.resr := 0;
			//		  --      END IF;
			//		  --   ELSE
			//		  --       w_mess := PA_PAIE.erreurp('ERR-90134',w_clang,wsal01.nmat,wcalc.rubq,wcalc.nprt);
			//		  --       RETURN FALSE;
			//		  --   END IF;
			//		  -- Fin modif MM.
			//		  --END IF;
			//		END IF;
		}
		//
		//		-- ----- Mise a jour du nombre d'echeances restantes
		//		IF NOT PA_PAIE.NouZ(wprent.nber) THEN
		//		   wprent.nber := wprent.nber - 1;
		//		  IF wprent.nber <= 0 THEN
		//		      wprent.nber := 0;
		//		  END IF;
		//		END IF;
		int i = 0;
		if (!ClsObjectUtil.isNull(pretent.getNber()))
		{
			i = pretent.getNber().intValue();
			i--;
			if (i < 0)
				i = 0;
			pretent.setNber(i);
		}
		//
		//		-- ----- Le pret passe a actif 'NON' si il est echu
		//		IF wprent.resr = 0 AND wprent.nber = 0 THEN
		//		    wprent.pact := 'N';
		//		END IF;
		if (pretent.getResr().intValue() == 0 && pretent.getNber() == 0)
			pretent.setPact("N");
		//
		//		UPDATE paprent
		//		   SET resr = wprent.resr,
		//		       nber = wprent.nber,
		//		       pact = wprent.pact
		//		 WHERE rowid = w_rowid;
		oSession.update(pretent);
		//
		//		 RETURN TRUE;
		//
		//		END maj_pret17;
		//
		//return false;
		return true;
	}

	**
	 * =>maj_eva Generation d'EV sur mois suivant
	 *
	private boolean genElementVariableNextMonth(String nmat, String crub, BigDecimal mont)
	{
		com.cdi.deltarh.service.ClsParameter.println(">>genElementVariableNextMonth");
		//		annee   VARCHAR2(4);
		//		  mois    VARCHAR2(2);
		//
		//		BEGIN
		//		   wevar.cdos := wpdos.cdos;
		//		   wevar.aamm := mois_ms;
		//		   wevar.nmat := wsal01.nmat;
		//		   wevar.nbul := nbul_ms;
		//		   wevar.cuti := w_cuti;
		Rhteltvardet evar = new Rhteltvardet();
		evar.setAamm(mois_ms);
		evar.setComp_id(new RhteltvardetPK(dossier, nmat, nbul_ms, 0));
		//
		//		  IF tab91 THEN
		//		    BEGIN
		//		    SELECT vall INTO wfnom.lib2
		//		      FROM pafnom
		//		     WHERE cdos = wpdos.cdos
		//		       AND ctab = 91
		//		       AND cacc = mois_ms
		//		       AND nume = 2;
		//		    EXCEPTION
		//		      WHEN NO_DATA_FOUND THEN null;
		//		    END;
		//
		//		    IF SQL%NOTFOUND THEN
		//		       w_mess := PA_PAIE.erreurp('ERR-90131',w_clang,mois_ms);
		//		       RETURN FALSE;
		//		    ELSE
		//		    SELECT vall INTO  wfnom.lib3
		//		      FROM pafnom
		//		     WHERE cdos = wpdos.cdos
		//		       AND ctab = 91
		//		       AND cacc = mois_ms
		//		       AND nume = 3;
		//
		//		       wpaev.ddpa := TO_DATE(wfnom.lib2,'DD/MM/YYYY');
		//		       wpaev.dfpa := TO_DATE(wfnom.lib3,'DD/MM/YYYY');
		//		    END IF;
		//		  ELSE
		//		     annee := substr(mois_ms,1,4);
		//		     mois  := substr(mois_ms,5,2);
		//		     wpaev.ddpa := to_date(mois||'01'||annee,'MMDDYYYY');
		//		     wpaev.dfpa := LAST_DAY(wpaev.ddpa);
		//		  END IF;
		Object o = null;
		Object o2 = null;
		String libelle3 = "";
		String libelle2 = "";
		Date ddpa = null;
		Date dfpa = null;
		if (tab91)
		{
			o = (this._getRhfnom(dossier, 91, mois_ms, 2));
			if (o == null)
			{
				error = parameter.errorMessage("ERR-90131", langue, mois_ms);
				com.cdi.deltarh.service.ClsParameter.println(error);
				ClsGlobalUpdate._setEvolutionTraitement(request, error, true);
				return false;
			}
			else
			{
				libelle2 = ((Rhfnom) o).getVall();
				o2 = (this._getRhfnom(dossier, 91, mois_ms, 3));
				libelle3 = ((Rhfnom) o2).getVall();
				ddpa = new ClsDate(libelle2, dateformat).getDate();
				dfpa = new ClsDate(libelle3, dateformat).getDate();
			}
		}
		else
		{
			ddpa = new ClsDate(mois_ms, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getDate();
			dfpa = new ClsDate(ddpa).getLastDayOfMonth();

		}
		//		  wevar.rubq := w_rub;
		//		  wevar.argu := ' ';
		//		  wevar.nprt := ' ';
		//		  wevar.ruba := ' ';
		//		  wevar.mont := wcalc.mont;
		//
		//		BEGIN
		//		  SELECT 'X' INTO w_exist
		//		    FROM paev
		//		   WHERE cdos = wevar.cdos
		//		     AND nmat = wevar.nmat
		//		     AND aamm = wevar.aamm
		//		     AND nbul = wevar.nbul;
		//		  EXCEPTION
		//		    WHEN NO_DATA_FOUND THEN null;
		//		  END;
		//		  IF SQL%NOTFOUND THEN
		//		     INSERT INTO paev
		//		            VALUES (wevar.cdos,wevar.aamm,wevar.nmat,wevar.nbul,wpaev.ddpa,
		//		                    wpaev.dfpa,'N');
		//		  END IF;
		//
		//		   INSERT INTO paevar
		//		          VALUES (wevar.cdos,wevar.aamm,wevar.nmat,wevar.nbul,wevar.rubq,
		//		                  wevar.argu,wevar.nprt,wevar.ruba,wevar.mont,wevar.cuti);
		//
		evar.setRubq(crub);
		evar.setMont(mont);
		//
		o = service.get(Rhteltvarent.class, new RhteltvarentPK(evar.getComp_id().getCdos(), evar.getAamm(), evar.getComp_id().getNmat(), evar.getComp_id().getNbul()));
		if (o == null)
		{
			Rhteltvarent event = new Rhteltvarent();
			event.setComp_id(new RhteltvarentPK(evar.getComp_id().getCdos(), evar.getAamm(), evar.getComp_id().getNmat(), evar.getComp_id().getNbul()));
			event.setDdpa(ddpa);
			event.setDfpa(dfpa);
			event.setBcmo("N");
			//
			oSession.save(event);
		}
		//
		oSession.save(evar);

		//		   RETURN TRUE;
		//
		//		END maj_eva;
		//
		//return false;
		return true;
	}

	private Rhprubrique _getRubrique(String cdos, String crub)
	{
		Rhprubrique rubrique = this.updateBulletin.rubriquesMap.get(crub);
		if (rubrique == null)
		{
			rubrique = (Rhprubrique) service.get(Rhprubrique.class, new RhprubriquePK(dossier, crub));
			if (rubrique != null)
				this.updateBulletin.rubriquesMap.put(crub, rubrique);
		}
		return rubrique;
	}

	private Rhfnom _getRhfnom(String dossier, Integer ctab, String cacc, Integer nume)
	{
		String cle = String.valueOf(ctab) + "-" + cacc + "-" + String.valueOf(nume);
		Rhfnom fnom = this.updateBulletin.rhfnomsMap.get(cle);
		if (fnom == null)
		{
			fnom = (Rhfnom) this.service.get(Rhfnom.class, new RhfnomPK(dossier, ctab, cacc, nume));
			if (fnom != null)
				this.updateBulletin.rhfnomsMap.put(cle, fnom);
		}
		return fnom;
	}

	private void _supprimerCumuls(String nmat)
	{
		String query = "From Rhtcumul cumul where exists (Select 'X' from Rhtcalcul calcul" + " where calcul.comp_id.cdos = cumul.comp_id.cdos" + " and calcul.comp_id.aamm = cumul.comp_id.aamm" + " and calcul.comp_id.nmat = cumul.comp_id.nmat" + " and calcul.comp_id.nbul = cumul.comp_id.nbul )"
				+ " and  cumul.comp_id.cdos = '" + dossier + "'" + " and cumul.comp_id.aamm = '" + periode + "'" + " and cumul.comp_id.nmat = '" + nmat + "'" + " and cumul.comp_id.nbul = " + numerobulletin;
		List<Rhtcumul> liste = service.find(query);
		for (Rhtcumul rhtcumul : liste)
		{
			this.cumulsMap.put(rhtcumul.getComp_id(), rhtcumul);
		}

		oSession.createQuery("Delete " + query).executeUpdate();
	}

	private void _supprimerCumuls99(String nmat)
	{
		String query = "From Rhtcumul cumul where exists (Select 'X' from Rhtcalcul calcul" + " where calcul.comp_id.aamm = '" + periode + "'" + " and calcul.comp_id.nbul = " + numerobulletin + " and calcul.comp_id.cdos = cumul.comp_id.cdos" + " and calcul.comp_id.nmat = cumul.comp_id.nmat )"
				+ " and cumul.comp_id.cdos = '" + dossier + "'" + " and cumul.comp_id.aamm = '" + w_am99 + "'" + " and cumul.comp_id.nmat = '" + nmat + "'" + " and cumul.comp_id.nbul = " + 9;
		List<Rhtcumul> liste = service.find(query);
		for (Rhtcumul rhtcumul : liste)
		{
			this.cumulsMap.put(rhtcumul.getComp_id(), rhtcumul);
		}

		oSession.createQuery("Delete " + query).executeUpdate();
	}

	private Rhtcumul _getRhtcumulToSave(RhtcumulPK cumulPK, Rhtcalcul calcul)
	{
		Rhtcumul cumul1 = this.cumulsMap.get(cumulPK);
		Rhtcumul cumul = null;
		if (cumul1 == null)
		{
			cumul = new Rhtcumul();
			cumul.setComp_id(new RhtcumulPK(cumulPK.getCdos(), cumulPK.getNmat(), cumulPK.getAamm(), cumulPK.getRubq(), cumulPK.getNbul()));
			cumul.setBasc(calcul.getBasc());
			cumul.setBasp(calcul.getBasp());
			cumul.setMont(calcul.getMont());
			cumul.setTaux(calcul.getTaux());
		}
		else
		{
			cumul = new Rhtcumul();
			cumul.setComp_id(new RhtcumulPK(cumulPK.getCdos(), cumulPK.getNmat(), cumulPK.getAamm(), cumulPK.getRubq(), cumulPK.getNbul()));
			cumul.setBasc(cumul1.getBasc().add(calcul.getBasc()));
			cumul.setBasp(cumul1.getBasp().add(calcul.getBasp()));
			cumul.setMont(cumul1.getMont().add(calcul.getMont()));
			cumul.setTaux(cumul1.getTaux().add(calcul.getTaux()));
		}
		return cumul;
	}

	**
	 * =>lect_rub_cal Lecture du calcul sur mois courant
	 *

	private boolean lectureCalculSurMoisCourant(Rhpagent agent)
	{
		com.cdi.deltarh.service.ClsParameter.println(">>lectureCalculSurMoisCourant");
		String nmat = agent.getComp_id().getNmat();
		//	FUNCTION lect_rub_cal RETURN BOOLEAN
		//	IS
		//
		//	  w_rowid   ROWID;
		//	  ind_rev   SMALLINT;
		//	  char_deb     VARCHAR(15);
		//	  char_cre  VARCHAR2(15);
		//
		//	  CURSOR curs_cal IS
		//	     SELECT pacalc.* FROM pacalc
		//	      WHERE cdos = wpdos.cdos
		//	        AND aamm = w_aamm
		//	        AND nmat = wsal01.nmat
		//	        AND nbul = w_nbul
		//	      ORDER BY cdos, aamm, nmat, nbul, rubq;
		List<Rhtcalcul> l = service.find("from Rhtcalcul where comp_id.cdos = '" + dossier + "'" + " and comp_id.aamm = '" + periode + "'" + " and comp_id.nmat = '" + nmat + "'" + " and comp_id.nbul = " + numerobulletin
				+ " order by comp_id.cdos, comp_id.aamm, comp_id.nmat, comp_id.nbul, comp_id.rubq");

		///////////Ajout par yannick :  au lieu de tester l'existence des cumuls avant de les inserer, je pr�fere tout supprimer
		//////////de sorte qu'on ne fasse que la sauvegarde sans plus tester l'existence
		this._supprimerCumuls(nmat);

		this._supprimerCumuls99(nmat);
		//
		//	  BEGIN
		//	  OPEN curs_cal;
		//	  LOOP
		Rhprubrique rubrique = null;
		for (Rhtcalcul calcul : l)
		{
			//	    FETCH curs_cal INTO wcalc;
			//	    EXIT WHEN curs_cal%NOTFOUND;
			//
			//	   IF wcalc.mont!=0 OR rcon_reg(wcalc.rubq) THEN
			//	    BEGIN
			//	    SELECT parubq.* INTO wrubq FROM parubq
			//	     WHERE cdos = wpdos.cdos
			//	       AND crub = wcalc.rubq;
			//	    EXCEPTION
			//	      WHEN NO_DATA_FOUND THEN null;
			//	    END;
			//
			//	    IF SQL%NOTFOUND THEN
			//	       CLOSE curs_cal;
			//	       w_mess := PA_PAIE.erreurp('ERR-90104',w_clang,wsal01.nmat,wcalc.rubq);
			//	       RETURN FALSE;
			//	    END IF;
			if((!calcul.getMont().equals(new BigDecimal(0))) || this.updateBulletin.rcon_reg(calcul.getComp_id().getRubq()))
			{
			
				rubrique = this._getRubrique(dossier, calcul.getComp_id().getRubq());
				if (rubrique == null)
				{
					error = parameter.errorMessage("ERR-90104", langue, nmat, calcul.getComp_id().getRubq());
					com.cdi.deltarh.service.ClsParameter.println(error);
					ClsGlobalUpdate._setEvolutionTraitement(request, error, true);
					return false;
				}
				//
				//	    IF wcalc.rubq = rub_bc THEN
				//	       bas_con := wcalc.mont;
				//	    END IF;
				if (calcul.getComp_id().getRubq().equals(rub_bc))
					bas_con = calcul.getMont().doubleValue();
				//
				//	    IF wcalc.rubq = rub_pnp THEN
				//	       w_retour := maj_pnp;
				//	    END IF;
				if (calcul.getComp_id().getRubq().equals(rub_pnp))
					w_retour = updateCongePayeNonPris(agent, calcul.getMont());
				//
				//	    IF wcalc.rubq = rub_nbjt THEN
				//	       IF nbj_pl = 'B' THEN
				//	          w_nbjtr := wcalc.basc;
				//	       ELSIF nbj_pl = 'T' THEN
				//	          w_nbjtr := wcalc.taux;
				//	       ELSIF nbj_pl = 'M' THEN
				//	          w_nbjtr := wcalc.mont;
				//	       END IF;
				//	    END IF;
				if (calcul.getComp_id().getRubq().equals(rubriqueNbreJourTravail))
				{
					if ("B".equals(rubriqueNbreJourPlage))
					{
						w_nbjtr = calcul.getBasc().intValue();
					}
					else if ("T".equals(rubriqueNbreJourPlage))
					{
						w_nbjtr = calcul.getTaux().intValue();
					}
					else if ("M".equals(rubriqueNbreJourPlage))
					{
						w_nbjtr = calcul.getMont().intValue();
					}
				}
				//
				//	    IF w_nbul = 9 THEN
				//	       -- MM 19/10/2000 Ajout algo 50 conges specifique COMILOG
				//	       -- MM 21/05/2001 Ajout algo 27 conges supplementaires
				//	       IF wrubq.algo IN (21,83,31,50,27) THEN
				//	            w_conge := w_conge + wcalc.mont;
				//	       ELSIF wrubq.algo IN (22,82)  THEN
				//	            w_moncp := wcalc.mont;
				//	       END IF;
				//	    END IF;
				if (numerobulletin == 9)
				{
					if (rubrique.getAlgo() == 21 || rubrique.getAlgo() == 83 || rubrique.getAlgo() == 31 || rubrique.getAlgo() == 50 || rubrique.getAlgo() == 27)
					{
						w_conge += calcul.getMont().doubleValue();
					}
					else if (rubrique.getAlgo() == 22 || rubrique.getAlgo() == 82)
					{
						w_moncp = calcul.getMont().doubleValue();
					}
				}
				//
				//	    IF wrubq.algo = 13 THEN
				//	        IF NOT PA_PAIE.NouB(wcalc.nprt) THEN
				//	           IF NOT maj_pret13 THEN
				//	              CLOSE curs_cal;
				//	              RETURN FALSE;
				//	           END IF;
				//	        END IF;
				//	    ELSIF wrubq.algo = 17 THEN
				//	        IF NOT PA_PAIE.NouB(wcalc.nprt) THEN
				//	           IF NOT maj_pret17(1) THEN
				//	              CLOSE curs_cal;
				//	              RETURN FALSE;
				//	           END IF;
				//	        END IF;
				//	    ELSIF wrubq.algo = 20 THEN
				//	       IF NOT PA_PAIE.NouB(wcalc.nprt) THEN
				//	          IF NOT maj_pret17(2) THEN
				//	              CLOSE curs_cal;
				//	              RETURN FALSE;
				//	          END IF;
				//	       END IF;
				//	    END IF;
				if (rubrique.getAlgo() == 13)
				{
					if (StringUtils.isNotBlank(calcul.getNprt()))
					{
						if (!updatePret13(nmat, Integer.valueOf(calcul.getNprt()), calcul.getComp_id().getRubq(), calcul.getMont()))
						{
							return false;
						}
					}
				}
				else if (rubrique.getAlgo() == 17)
				{
					if (StringUtils.isNotBlank(calcul.getNprt()))
					{
						if (!updatePret17(nmat, calcul.getComp_id().getRubq(), calcul.getNprt(), calcul.getMont()))
						{
							return false;
						}
					}
				}
				else if (rubrique.getAlgo() == 20)
				{
					if (StringUtils.isNotBlank(calcul.getNprt()))
					{
						if (!updatePret17(nmat, calcul.getComp_id().getRubq(), calcul.getNprt(), calcul.getMont()))
						{
							return false;
						}
					}
				}
				//
				//	    -- Verification si la rubrique calculee genere un E.V. sur le mois suivant
				//	    IF NOT sal_RA_MU THEN
				//	      FOR ind_rev IN 1..8 LOOP
				//	        IF wcalc.rubq = t_ev_mc(ind_rev) THEN
				//	          IF t_ev_ms(ind_rev) != '0000' THEN
				//	             IF NOT maj_eva(t_ev_ms(ind_rev)) THEN
				//	                CLOSE curs_cal;
				//	                RETURN FALSE;
				//	             ELSE
				//	                EXIT;
				//	             END IF;
				//	          END IF;
				//	        END IF;
				//	      END LOOP;
				//	    END IF;
				//			if(! sal_RA_MU){
				//				if(t_ev_ms != null)
				//				{
				//					for (String str : t_ev_ms) {
				//						if(calcul.getComp_id().getRubq().equals(str)){
				//							if(! "0000".equals(str)){
				//								if(! genElementVariableNextMonth(nmat, calcul.getComp_id().getRubq(), calcul.getMont())){
				//									return false;
				//								}
				//								else{
				//									break;
				//								}
				//							}
				//						}
				//					}
				//				}
				//			}
	
				if (!sal_RA_MU)
				{
					if (t_ev_ms != null)
					{
						for (int ind_rev = 0; ind_rev < 8; ind_rev++)
						{
							if (calcul.getComp_id().getRubq().equals(t_ev_mc[ind_rev]))
							{
								if (!"0000".equals(t_ev_ms[ind_rev]))
								{
									if (!genElementVariableNextMonth(nmat, t_ev_ms[ind_rev], calcul.getMont()))
									{
										return false;
									}
									else
									{
										break;
									}
								}
							}
						}
					}
				}
				//
				//	    -- Si rub. associee -> cumul de la rub. courante dans rub. associee
				//	    IF NOT PA_PAIE.NouB(wcalc.ruba) THEN
				//	       wcalc.rubq := wcalc.ruba;
				//	       wcalc.basc := 0;
				//	       wcalc.basp := 0;
				//	       wcalc.taux := 0;
				//	    END IF;
				if (StringUtils.isNotBlank(calcul.getRuba()))
				{
					calcul.getComp_id().setRubq(calcul.getRuba());
					calcul.setBasc(new BigDecimal(0));
					calcul.setBasp(new BigDecimal(0));
					calcul.setTaux(new BigDecimal(0));
				}
				//
				//	    -- ----- Mise a jour des cumuls pour mois courant
				//	    BEGIN
				//	    SELECT rowid , pacumu.*
				//	      INTO w_rowid , wcumu.cdos,wcumu.nmat,wcumu.aamm,wcumu.rubq,wcumu.nbul,
				//	           wcumu.basc,wcumu.basp,wcumu.taux,wcumu.mont
				//	      FROM pacumu
				//	     WHERE cdos = wpdos.cdos
				//	       AND nmat = wsal01.nmat
				//	       AND aamm = w_aamm
				//	       AND rubq = wcalc.rubq
				//	       AND nbul = w_nbul;
				//	    EXCEPTION
				//	      WHEN NO_DATA_FOUND THEN null;
				//	    END;
				//
				//	    IF SQL%NOTFOUND THEN
				//	       wcumu.cdos := wpdos.cdos;
				//	       wcumu.nmat := wsal01.nmat;
				//	       wcumu.aamm := w_aamm;
				//	       wcumu.rubq := wcalc.rubq;
				//	       wcumu.nbul := w_nbul;
				//	       wcumu.basc := wcalc.basc;
				//	       wcumu.basp := wcalc.basp;
				//	       wcumu.taux := wcalc.taux;
				//	       wcumu.mont := wcalc.mont;
				//
				//	      INSERT INTO pacumu
				//	         VALUES (wcumu.cdos,wcumu.nmat,wcumu.aamm,wcumu.rubq,
				//	                 wcumu.nbul,wcumu.basc,wcumu.basp,wcumu.taux,wcumu.mont);
				//	    ELSE
				//	       wcumu.basc := wcumu.basc + wcalc.basc;
				//	       wcumu.basp := wcumu.basp + wcalc.basp;
				//	       wcumu.mont := wcumu.mont + wcalc.mont;
				//
				//	       UPDATE pacumu
				//	         SET basc = wcumu.basc,
				//	             basp = wcumu.basp,
				//	             mont = wcumu.mont
				//	       WHERE rowid = w_rowid;
				//	    END IF;
				//			Object obj = service.get(Rhtcumul.class, new RhtcumulPK(dossier, nmat, periode, calcul.getComp_id().getRubq(), numerobulletin));
				//			if(obj == null){
				//				Rhtcumul cumul = new Rhtcumul();
				//				cumul.setComp_id(new RhtcumulPK(dossier, nmat, periode, calcul.getComp_id().getRubq(), numerobulletin));
				//				cumul.setBasc(calcul.getBasc());
				//				cumul.setBasp(calcul.getBasp());
				//				cumul.setMont(calcul.getMont());
				//				cumul.setTaux(calcul.getTaux());
				//				//
				//				oSession.save(cumul);
				//			}
				//			else{
				//				Rhtcumul cumul = (Rhtcumul)obj;
				//				cumul.setBasc(calcul.getBasc().add(cumul.getBasc()));
				//				cumul.setBasp(calcul.getBasp().add(cumul.getBasp()));
				//				cumul.setMont(calcul.getMont().add(cumul.getMont()));
				//				cumul.setTaux(calcul.getTaux().add(cumul.getTaux()));
				//				//
				//				service.update(cumul);
				//			}
	
				Rhtcumul cumul = this._getRhtcumulToSave(new RhtcumulPK(dossier, nmat, periode, calcul.getComp_id().getRubq(), numerobulletin), calcul);
				oSession.save(cumul);
	
				//
				//	    -- Mise a jour des cumuls annuels (Annee99)
				//	    BEGIN
				//	    SELECT rowid , pacumu.*
				//	      INTO w_rowid , wcumu.cdos,wcumu.nmat,wcumu.aamm,wcumu.rubq,wcumu.nbul,
				//	           wcumu.basc,wcumu.basp,wcumu.taux,wcumu.mont
				//	      FROM pacumu
				//	     WHERE cdos = wpdos.cdos
				//	       AND nmat = wsal01.nmat
				//	       AND aamm = w_am99
				//	       AND rubq = wcalc.rubq
				//	       AND nbul = 9;
				//	    EXCEPTION
				//	     WHEN NO_DATA_FOUND THEN null;
				//	    END;
				//
				//	    IF SQL%NOTFOUND THEN
				//	       wcumu.cdos := wpdos.cdos;
				//	       wcumu.nmat := wsal01.nmat;
				//	       wcumu.aamm := w_am99;
				//	       wcumu.rubq := wcalc.rubq;
				//	       wcumu.nbul := 9;
				//	       wcumu.basc := wcalc.basc;
				//	       wcumu.basp := wcalc.basp;
				//	       wcumu.taux := wcalc.taux;
				//	       wcumu.mont := wcalc.mont;
				//
				//	      INSERT INTO pacumu
				//	         VALUES (wcumu.cdos,wcumu.nmat,wcumu.aamm,wcumu.rubq,
				//	                 wcumu.nbul,wcumu.basc,wcumu.basp,wcumu.taux,wcumu.mont);
				//	    ELSE
				//	       wcumu.basc := wcumu.basc + wcalc.basc;
				//	       wcumu.basp := wcumu.basp + wcalc.basp;
				//	       wcumu.mont := wcumu.mont + wcalc.mont;
				//	       UPDATE pacumu
				//	         SET basc = wcumu.basc,
				//	             basp = wcumu.basp,
				//	             mont = wcumu.mont
				//	       WHERE rowid = w_rowid;
				//	    END IF;
				//			 obj = service.get(Rhtcumul.class, new RhtcumulPK(dossier, nmat, w_am99, calcul.getComp_id().getRubq(), 9));
				//			if(obj == null){
				//				Rhtcumul cumul = new Rhtcumul();
				//				cumul.setComp_id(new RhtcumulPK(dossier, nmat, w_am99, calcul.getComp_id().getRubq(), 9));
				//				cumul.setBasc(calcul.getBasc());
				//				cumul.setBasp(calcul.getBasp());
				//				cumul.setMont(calcul.getMont());
				//				cumul.setTaux(calcul.getTaux());
				//				//
				//				oSession.save(cumul);
				//			}
				//			else{
				//				Rhtcumul cumul = (Rhtcumul)obj;
				//				cumul.setBasc(calcul.getBasc().add(cumul.getBasc()));
				//				cumul.setBasp(calcul.getBasp().add(cumul.getBasp()));
				//				cumul.setMont(calcul.getMont().add(cumul.getMont()));
				//				cumul.setTaux(calcul.getTaux().add(cumul.getTaux()));
				//				//
				//				service.update(cumul);
				//			}
	
				cumul = this._getRhtcumulToSave(new RhtcumulPK(dossier, nmat, w_am99, calcul.getComp_id().getRubq(), 9), calcul);
				oSession.save(cumul);
	
				//	  END IF;
				//	  END LOOP;
				//	  CLOSE curs_cal;
			}
		}
		//
		//	  RETURN TRUE;
		return true;
		//
		//	END lect_rub_cal;
	}

	//	--------------------------------------------------------------------------------
	//	-- Transfert des echeances de prets sur mois suivant (wsal01.cals = 'N')
	//	---------------------------------------------------------------------------------
	private boolean transferEcheancePretMoisSuivant(String nmat)
	{
		com.cdi.deltarh.service.ClsParameter.println(">>transferEcheancePretMoisSuivant");
		//	FUNCTION trans_prets RETURN BOOLEAN
		//	IS
		//
		//	wmont_pret    DECIMAL(15,3);
		//	wrowid        ROWID;
		//	i             SMALLINT;
		//	j             SMALLINT;
		//	wperb         paprlig.perb%TYPE;
		//
		//	CURSOR curs_trpr IS
		//	       SELECT paprent.*
		//	         FROM paprent
		//	        WHERE cdos = wpdos.cdos
		//	          AND nmat = wsal01.nmat
		//	          AND per1 <= TO_DATE(w_aamm,'YYYYMM')
		//	          AND resr > 0
		//	          AND (tint = 0 OR tint IS NULL)
		//	          AND pact = 'O'
		//	          FOR UPDATE ;
		//	--             ' ORDER BY cdos, nmat '
		double wmont_pret = 0;
		String queryString = "from Rhtpretent" + " where comp_id.cdos = '" + dossier + "'" + " and nmat = '" + nmat + "'" + " and per1 <= '" + periode + "'" + " and resr > 0" + " and (tint = 0 or tint is null)" + " and pact = 'O'";
		List<Rhtpretent> l = service.find(queryString);
		//	BEGIN
		//	  OPEN  curs_trpr;
		//	  LOOP
		Object o = null;
		for (Rhtpretent entete : l)
		{
			//	   FETCH curs_trpr INTO wprent;
			//	   EXIT WHEN curs_trpr%NOTFOUND;
			//
			//	  BEGIN
			//	   SELECT echr, rowid INTO wmont_pret, wrowid
			//	     FROM paprlig
			//	    WHERE cdos = wpdos.cdos
			//	      AND nprt = wprent.nprt
			//	      AND perb = TO_DATE(w_aamm,'YYYYMM');
			//	  EXCEPTION
			//	    WHEN NO_DATA_FOUND THEN null;
			//	  END;
			o = service.get(Rhtpretlig.class, new RhtpretligPK(dossier, entete.getComp_id().getNprt(), new ClsDate(periode, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getDate()));
			//	   IF SQL%NOTFOUND THEN
			//	       wmont_pret := 0;
			//	   ELSE
			//
			//	      UPDATE paprlig SET echr = 0 WHERE rowid = wrowid;
			//
			//	      wperb := NULL;
			//	      SELECT MAX(perb) INTO wperb
			//	        FROM paprlig
			//	       WHERE cdos = wpdos.cdos
			//	         AND nprt = wprent.nprt;
			//
			//	      IF wperb IS NOT NULL THEN
			//	          wperb := ADD_MONTHS(wperb,1);
			//	          INSERT INTO paprlig
			//	            VALUES (wpdos.cdos, wprent.nprt, wperb, 0, wmont_pret,0,0,9);
			//	      END IF;
			//	   END IF;
			if (o == null)
			{
				wmont_pret = 0;
			}
			else
			{
				wmont_pret = ((Rhtpretlig) o).getEchr().doubleValue();
				((Rhtpretlig) o).setEchr(new BigDecimal(0));
				oSession.update(o);
				o = service.find("select max(perb) from Rhtpretlig" + " where comp_id.cdos = '" + dossier + "'" + " and comp_id.nprt = '" + entete.getComp_id().getNprt() + "'");
				if (o != null)
				{
					Date perb = new ClsDate((Date) o).addMonth(1);
					Rhtpretlig ligne = new Rhtpretlig();
					ligne.setComp_id(new RhtpretligPK(dossier, entete.getComp_id().getNprt(), perb));
					ligne.setEcho(new BigDecimal(0));
					ligne.setEchr(new BigDecimal(wmont_pret));
					ligne.setInte(new BigDecimal(0));
					ligne.setNbul(9);
					ligne.setTaxe(new BigDecimal(0));
					//
					oSession.save(ligne);
				}
			}
			//
			//	END LOOP;
			//	CLOSE curs_trpr;
		}
		//
		//	RETURN TRUE;
		return true;
		//
		//	END trans_prets;
	}

	//	---------------------------------------------------------------------------------
	//	--        Transfert des EV sur mois suivant (wsal01.cals = 'N')
	//	---------------------------------------------------------------------------------
	private boolean transferEVMoisSuivant(String nmat)
	{
		com.cdi.deltarh.service.ClsParameter.println(">>transferEVMoisSuivant");
		//	FUNCTION trans_ev RETURN BOOLEAN
		//	IS
		//
		//	annee  VARCHAR2(4);
		//	mois   VARCHAR2(2);
		//
		//	CURSOR curs_trev IS
		//	  SELECT paevar.*,paev.* FROM paevar,paev
		//	  WHERE paev.cdos = wpdos.cdos
		//	   AND paev.nmat = wsal01.nmat
		//	   AND paev.aamm = w_aamm
		//	   AND paev.nbul = w_nbul
		//	   AND paevar.cdos = paev.cdos
		//	   AND paevar.aamm = paev.aamm
		//	   AND paevar.nmat = paev.nmat
		//	   AND paevar.nbul = paev.nbul
		//	   FOR UPDATE;
		//	--             ' ORDER BY cdos, aamm, nmat '

		String queryString = "select a.comp_id.cdos, a.aamm, a.comp_id.nmat, a.comp_id.nbul, a.rubq, a.argu, " + "a.nprt, a.ruba, a.mont, a.cuti," + "b.comp_id.cdos, b.comp_id.aamm, b.comp_id.nmat, b.comp_id.nbul, b.ddpa, b.dfpa, b.bcmo" + " from Rhteltvardet a, Rhteltvarent b"
				+ " where b.comp_id.cdos = '" + dossier + "'" + " and b.comp_id.aamm = '" + periode + "'" + " and b.comp_id.nmat = '" + nmat + "'" + " and b.comp_id.nbul = " + numerobulletin + " and a.comp_id.cdos = b.comp_id.cdos" + " and a.aamm = b.comp_id.aamm"
				+ " and a.comp_id.nmat = b.comp_id.nmat" + " and a.comp_id.nbul = b.comp_id.nbul";
		List l = service.find(queryString);

		//
		//	BEGIN
		//	  OPEN curs_trev;
		//	  LOOP
		boolean transcg = true;
		Object[] ev = null;
		Rhteltvardet evar = null;
		for (Object object : l)
		{
			//			a.comp_id.cdos0, a.aamm1, a.comp_id.nmat2, a.comp_id.nbul3, a.rubq4, a.argu5, " +
			//	          "a.nprt6, a.ruba7, a.mont8, a.cuti9," +
			//	          "b.comp_id.cdos10, b.comp_id.aamm11, b.comp_id.nmat12, b.comp_id.nbul13, b.ddpa14, b.dfpa15, b.bcmo16" +
			//	    FETCH curs_trev
			//	     INTO wevar.cdos,wevar.aamm,wevar.nmat,wevar.nbul,wevar.rubq,wevar.argu,
			//	          wevar.nprt,wevar.ruba,wevar.mont,wevar.cuti,
			//	          wpaev.cdos,wpaev.aamm,wpaev.nmat,wpaev.nbul,wpaev.ddpa,wpaev.dfpa,
			//	          wpaev.bcmo;
			//	    EXIT WHEN curs_trev%NOTFOUND;
			ev = (Object[]) object;
			evar = new Rhteltvardet();
			evar.setComp_id(new RhteltvardetPK(dossier, nmat, numerobulletin, 0));
			evar.setAamm((String) ev[1]);
			evar.setArgu((String) ev[5]);
			evar.setCuti((String) ev[9]);
			evar.setMont((BigDecimal) ev[8]);
			evar.setNprt((String) ev[6]);
			evar.setRuba((String) ev[7]);
			evar.setRubq((String) ev[4]);
			//	   wevar.aamm := mois_ms;
			//	   wevar.nbul := nbul_ms;
			//	   wevar.cuti := NVL( wevar.cuti, w_cuti);
			evar.setAamm(mois_ms);
			evar.getComp_id().setNbul(nbul_ms);
			if (ClsObjectUtil.isNull(ev[9]))
				evar.setCuti(user);
			//	  IF tab91 THEN
			//	    BEGIN
			//	     SELECT vall
			//	       INTO wlib2
			//	       FROM pafnom
			//	      WHERE cdos = wpdos.cdos
			//	        AND ctab = 91
			//	        AND cacc = mois_ms
			//	        AND nume = 2;
			//	     EXCEPTION
			//	       WHEN NO_DATA_FOUND THEN null;
			//	     END;
			//	     IF SQL%NOTFOUND THEN
			//	         CLOSE curs_trev;
			//	         w_mess := PA_PAIE.erreurp('ERR-90131',w_clang,mois_ms);
			//	         RETURN FALSE;
			//	     ELSE
			//	     SELECT vall
			//	       INTO wlib3
			//	       FROM pafnom
			//	      WHERE cdos = wpdos.cdos
			//	        AND ctab = 91
			//	        AND cacc = mois_ms
			//	        AND nume = 3;
			//
			//	     wpaev.ddpa := TO_DATE(wlib2,'DD/MM/YYYY');
			//	     wpaev.dfpa := TO_DATE(wlib3,'DD/MM/YYYY');
			//	     END IF;
			//	  ELSE
			//	      annee := substr(mois_ms,1,4);
			//	      mois  := substr(mois_ms,5,2);
			//	      wpaev.ddpa := to_date('01'||mois||annee,'DDMMYYYY');
			//	      wpaev.dfpa := LAST_DAY(wpaev.ddpa);
			//	  END IF;
			Object o = null;
			Object o2 = null;
			String libelle3 = "";
			String libelle2 = "";
			Date ddpa = null;
			Date dfpa = null;
			if (tab91)
			{
				o = (this._getRhfnom(dossier, 91, mois_ms, 2));
				//o = this._getRhfnom(dossier, 91, mois_ms, 2);
				if (o == null)
				{
					error = parameter.errorMessage("ERR-90131", langue, mois_ms);
					com.cdi.deltarh.service.ClsParameter.println(error);
					return false;
				}
				else
				{
					libelle2 = ((Rhfnom) o).getVall();
					o2 = (this._getRhfnom(dossier, 91, mois_ms, 3));
					libelle3 = ((Rhfnom) o2).getVall();
					ddpa = new ClsDate(libelle2, dateformat).getDate();
					dfpa = new ClsDate(libelle3, dateformat).getDate();
				}
			}
			else
			{
				ClsParameter.println("---------------mois_ms=" + mois_ms);
				ddpa = new ClsDate(mois_ms, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getDate();
				dfpa = new ClsDate(ddpa).getLastDayOfMonth();

			}
			//	   wpaev.bcmo := 'N';
			//
			//	BEGIN
			//	  SELECT 'X' INTO w_exist
			//	    FROM paev
			//	   WHERE cdos = wevar.cdos
			//	     AND nmat = wevar.nmat
			//	     AND aamm = wevar.aamm
			//	     AND nbul = wevar.nbul;
			//	  EXCEPTION
			//	    WHEN NO_DATA_FOUND THEN null;
			//	  END;
			//	  IF SQL%NOTFOUND THEN
			//	  INSERT INTO paev
			//	         VALUES (wevar.cdos,wevar.aamm,wevar.nmat,wevar.nbul,
			//	                 wpaev.ddpa,wpaev.dfpa,wpaev.bcmo);
			//	  END IF;
			//
			//	  INSERT INTO paevar
			//	         VALUES (wevar.cdos,wevar.aamm,wevar.nmat,wevar.nbul,
			//	                 wevar.rubq,wevar.argu,wevar.nprt,wevar.ruba,wevar.mont,wevar.cuti);
			//
			//
			o = service.get(Rhteltvarent.class, new RhteltvarentPK(evar.getComp_id().getCdos(), evar.getAamm(), evar.getComp_id().getNmat(), evar.getComp_id().getNbul()));
			if (o == null)
			{
				Rhteltvarent event = new Rhteltvarent();
				event.setComp_id(new RhteltvarentPK(evar.getComp_id().getCdos(), evar.getAamm(), evar.getComp_id().getNmat(), evar.getComp_id().getNbul()));
				event.setDdpa(ddpa);
				event.setDfpa(dfpa);
				event.setBcmo("N");
				//
				oSession.save(event);
			}
			//
			oSession.save(evar);
			//	END LOOP;
			//	CLOSE curs_trev;
		}
		//
		//	RETURN TRUE;
		return true;
		//
		//	END trans_ev;
	}

	**
	 * =>trans_cong Transfert des conges sur mois suivant (wsal01.cals = 'N')
	 *
	private boolean transferEVCGMoisSuivant(String nmat)
	{
		com.cdi.deltarh.service.ClsParameter.println(">>transferEVCGMoisSuivant");
		//		---------------------------------------------------------------------------------
		//		--        Transfert des conges sur mois suivant (wsal01.cals = 'N')
		//		---------------------------------------------------------------------------------
		//		FUNCTION trans_cong RETURN BOOLEAN
		//		IS
		//
		//		annee       VARCHAR2(4);
		//		mois        VARCHAR2(2);
		//		transcg     BOOLEAN;
		//
		//		CURSOR curs_trcg IS
		//		       SELECT paevcg.*,paev.*
		//		         FROM paevcg,paev
		//		        WHERE paevcg.cdos = wpdos.cdos
		//		          AND paevcg.nmat = wsal01.nmat
		//		          AND paevcg.aamm = w_aamm
		//		          AND paevcg.nbul = w_nbul
		//		          AND paev.cdos = paevcg.cdos
		//		          AND paev.nmat = paevcg.nmat
		//		          AND paev.aamm = paevcg.aamm
		//		          AND paev.nbul = paevcg.nbul
		//		       FOR UPDATE;
		//		--             ' ORDER BY cdos , aamm , nmat , ddeb '
		String queryString = "select a.comp_id.cdos, a.comp_id.aamm, a.comp_id.nmat, a.comp_id.nbul, a.comp_id.ddeb,a.dfin," + "a.nbjc, a.nbja, a.motf, a.mont, a.cuti," + "b.comp_id.cdos, b.comp_id.aamm, b.comp_id.nmat, b.comp_id.nbul, b.ddpa, b.dfpa, b.bcmo"
				+ " from Rhteltvarconge a, Rhteltvarent b" + " where b.comp_id.cdos = '" + dossier + "'" + " and b.comp_id.aamm = '" + periode + "'" + " and b.comp_id.nmat = '" + nmat + "'" + " and b.comp_id.nbul = " + numerobulletin + " and a.comp_id.cdos = b.comp_id.cdos"
				+ " and a.comp_id.aamm = b.comp_id.aamm" + " and a.comp_id.nmat = b.comp_id.nmat" + " and a.comp_id.nbul = b.comp_id.nbul";
		//
		//		BEGIN
		//
		//		OPEN curs_trcg;
		//		LOOP
		List l = service.find(queryString);

		boolean transcg = true;
		Object[] ev = null;
		for (Object obj : l)
		{
			//		  FETCH curs_trcg
			//		   INTO wevcg.cdos,wevcg.aamm,wevcg.nmat,wevcg.nbul,wevcg.ddeb,wevcg.dfin,
			//		        wevcg.nbjc,wevcg.nbja,wevcg.motf,wevcg.mont,wevcg.cuti,
			//		        wpaev.cdos,wpaev.aamm,wpaev.nmat,wpaev.nbul,wpaev.ddpa,wpaev.dfpa,
			//		        wpaev.bcmo;
			//		  EXIT WHEN curs_trcg%NOTFOUND;
			//			"select a.comp_id.cdos0, a.comp_id.aamm1, a.comp_id.nmat2, a.comp_id.nbul3, a.ddeb4, a.dfin5," +
			//	        "a.nbjc6, a.nbja7, a.motf8, a.mont9, a.cuti10," +
			//	        "b.comp_id.cdos11, b.comp_id.aamm12, b.comp_id.nmat13, b.comp_id.nbul14, b.ddpa15, b.dfpa16, b.bcmo17"
			ev = (Object[]) obj;
			//		  transcg := FALSE;
			//		  wevcg.cuti := NVL( wevcg.cuti, w_cuti);
			transcg = false;
			if (ClsObjectUtil.isNull(ev[10]))
				ev[10] = user;
			//
			Rhteltvarconge evconge = new Rhteltvarconge();
			evconge.setComp_id(new RhteltvarcongePK(dossier, (String) ev[1], (String) ev[2], (Integer) ev[3], (Date) ev[4]));
			evconge.setDfin((Date) ev[5]);
			evconge.setNbjc(new BigDecimal(ev[6] == null ? "0" : ev[6].toString()));
			evconge.setNbja(new BigDecimal(ev[7] == null ? "0" : ev[7].toString()));
			evconge.setMotf((String) ev[8]);
			evconge.setMont((BigDecimal) ev[9]);
			evconge.setCuti((String) ev[10]);
			//
			//		 BEGIN
			//		  SELECT max(decode(nume,1,valm,0)),
			//		         max(decode(nume,3,valm,0))
			//		    INTO wfnom.mnt1,wfnom.mnt3
			//		    FROM pafnom
			//		   WHERE cdos = wpdos.cdos
			//		     AND ctab = 22
			//		     AND cacc = wevcg.motf
			//		     AND nume in (1,3);
			//		 EXCEPTION
			//		  WHEN NO_DATA_FOUND THEN null;
			//		 END;
			Object[] o = (Object[]) service.find(
					"select max(case nume when 1 then valm else 0 end), " + " max(case nume when 3 then valm else 0 end) from Rhfnom" + " where comp_id.cdos = '" + dossier + "'" + " and comp_id.ctab = 22" + " and comp_id.cacc = '" + evconge.getMotf() + "'" + " and comp_id.nume in(1, 3)").get(0);

			//		  IF SQLCODE = 0 THEN
			if (o != null && o[0] != null && o[1] != null)
			{
				int mnt1 = (o[0] instanceof BigDecimal ? (BigDecimal) o[0] : (Long) o[0]).intValue();
				int mnt3 = (o[1] instanceof BigDecimal ? (BigDecimal) o[1] : (Long) o[1]).intValue();
				//		    IF (wfnom.mnt1 = 1 AND wfnom.mnt3 = 0) OR wevcg.motf = w_cg_abs THEN
				if ((mnt1 == 1 && mnt3 == 0) || evconge.getMotf().equals(w_cg_abs))
				{
					//		       IF tab91 THEN
					//		          FOR ind91_sav IN 1..nb_t91 LOOP
					//		             ind91 := ind91_sav;
					//		             IF T91_periode(ind91) = TO_CHAR(wpdos.ddmp,'YYYYMM') THEN
					//		                EXIT;
					//		             END IF;
					//		          END LOOP;
					//		          -- MM 17/11/1999
					//		          IF ind91 < nb_t91 THEN
					//		             IF wevcg.ddeb > T91_df(ind91 + 1) THEN
					//		                transcg := TRUE;
					//		             END IF;
					//		          END IF;
					String d1 = null;
					int i = 0;
					if (tab91)
					{
						for (ClsInfoPeriode period : listOfPeriodeT91)
						{
							i++;
							if (period.ddebut != null)
							{
								d1 = new ClsDate(period.ddebut).getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
								if (moisPaieCourant.equals(d1))
									break;
							}
						}
						if (i < listOfPeriodeT91.size())
						{
							ClsInfoPeriode period = listOfPeriodeT91.get(i);
							if (ev[4] != null && (evconge.getComp_id().getDdeb()).after(period.dfin))
							{
								transcg = true;
							}
						}
					}
					//		       ELSE
					else
					{
						//		           w_am := to_char(wevcg.ddeb,'YYYY')||to_char(wevcg.ddeb,'MM');
						//		          IF w_am > w_aamm THEN
						//		              transcg := TRUE;
						//		          END IF;
						w_am = new ClsDate(evconge.getComp_id().getDdeb()).getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
						if (w_am.compareTo(periode) > 0)
						{
							transcg = true;
						}
						//
					}
					//		       END IF;
					//
					//		    END IF;
				}
				//
				//		  END IF;
			}
			//
			//		  IF transcg THEN
			if (transcg)
			{
				//		   wevcg.aamm := mois_ms;
				//		   wevcg.nbul := nbul_ms;
				evconge.getComp_id().setAamm(mois_ms);
				evconge.getComp_id().setNbul(nbul_ms);
				//		  IF tab91 THEN
				//		    BEGIN
				//		     SELECT vall INTO wlib2
				//		       FROM pafnom
				//		      WHERE cdos = wpdos.cdos
				//		        AND ctab = 91
				//		        AND cacc = mois_ms
				//		        AND nume = 2;
				//		    EXCEPTION
				//		      WHEN NO_DATA_FOUND THEN null;
				//		    END;
				//		     IF SQL%NOTFOUND THEN
				//		         CLOSE curs_trcg;
				//		         w_mess := PA_PAIE.erreurp('ERR-90131',w_clang,mois_ms);
				//		         RETURN FALSE;
				//		     ELSE
				//		     SELECT vall INTO wlib3
				//		       FROM pafnom
				//		      WHERE cdos = wpdos.cdos
				//		        AND ctab = 91
				//		        AND cacc = mois_ms
				//		        AND nume = 3;
				//
				//		         wpaev.ddpa := TO_DATE(wlib2,'DD/MM/YYYY');
				//		         wpaev.dfpa := TO_DATE(wlib3,'DD/MM/YYYY');
				//		     END IF;
				//		  ELSE
				//		      annee := substr(mois_ms,1,4);
				//		      mois  := substr(mois_ms,5,2);
				//		      wpaev.ddpa := to_date('01'||mois||annee,'DDMMYYYY');
				//		      wpaev.dfpa := LAST_DAY(wpaev.ddpa);
				//		  END IF;
				Object o3 = null;
				Object o2 = null;
				String libelle3 = "";
				String libelle2 = "";
				Date ddpa = null;
				Date dfpa = null;
				if (tab91)
				{
					o3 = (this._getRhfnom(dossier, 91, mois_ms, 2));
					if (o3 == null)
					{
						error = parameter.errorMessage("ERR-90131", langue, mois_ms);
						com.cdi.deltarh.service.ClsParameter.println(error);
						return false;
					}
					else
					{
						libelle2 = ((Rhfnom) o3).getVall();
						o2 = (this._getRhfnom(dossier, 91, mois_ms, 3));
						libelle3 = ((Rhfnom) o2).getVall();
						ddpa = new ClsDate(libelle2, dateformat).getDate();
						dfpa = new ClsDate(libelle3, dateformat).getDate();
					}
				}
				else
				{
					ddpa = new ClsDate(mois_ms, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getDate();
					dfpa = new ClsDate(ddpa).getLastDayOfMonth();

				}
				//		   wpaev.bcmo := 'N';
				//		BEGIN
				//		  SELECT 'X' INTO w_exist
				//		    FROM paev
				//		   WHERE cdos = wevcg.cdos
				//		     AND nmat = wevcg.nmat
				//		     AND aamm = wevcg.aamm
				//		     AND nbul = wevcg.nbul;
				//		  EXCEPTION
				//		    WHEN NO_DATA_FOUND THEN null;
				//		  END;
				//		  IF SQL%NOTFOUND THEN
				//		  INSERT INTO paev
				//		         VALUES (wevcg.cdos,wevcg.aamm,wevcg.nmat,wevcg.nbul,
				//		                 wpaev.ddpa,wpaev.dfpa,wpaev.bcmo);
				//		  END IF;
				//
				//		  BEGIN
				//		    INSERT INTO paevcg
				//		           VALUES (wevcg.cdos,wevcg.aamm,wevcg.nmat,wevcg.nbul,wevcg.ddeb,
				//		                   wevcg.dfin,wevcg.nbjc,wevcg.nbja,wevcg.motf,wevcg.mont,wevcg.cuti);
				//		  EXCEPTION
				//		     WHEN DUP_VAL_ON_INDEX THEN null;
				//		  END;
				o3 = service.get(Rhteltvarent.class, new RhteltvarentPK(evconge.getComp_id().getCdos(), evconge.getComp_id().getAamm(), evconge.getComp_id().getNmat(), evconge.getComp_id().getNbul()));
				if (o3 == null)
				{
					Rhteltvarent event = new Rhteltvarent();
					event.setComp_id(new RhteltvarentPK(evconge.getComp_id().getCdos(), evconge.getComp_id().getAamm(), evconge.getComp_id().getNmat(), evconge.getComp_id().getNbul()));
					event.setDdpa(ddpa);
					event.setDfpa(dfpa);
					event.setBcmo("N");
					//
					oSession.save(event);
				}
				//
				oSession.save(evconge);
				//
				//		 END IF;
			}
			//		END LOOP;
			//		CLOSE curs_trcg;
		}
		//
		//		RETURN TRUE;
		return true;
		//
		//		END trans_cong;
	}

	List listOfEltVarConge = null;

	**
	 * =>lect_ev Lecture des conges sur mois courant
	 * 
	 * @return
	 *
	private boolean lectureElementVariable(String nmat)
	{
		com.cdi.deltarh.service.ClsParameter.println(">>lectureElementVariable");
		listOfEltVarConge = new ArrayList();

		//		---------------------------------------------------------------------------------
		//		-- Lecture des conges sur mois courant
		//		---------------------------------------------------------------------------------
		//		FUNCTION lect_ev RETURN BOOLEAN
		//		IS
		//
		//		CURSOR curs_cg IS
		//		     SELECT paevcg.*,paev.*
		//		       FROM paevcg,paev
		//		      WHERE paev.cdos = wpdos.cdos
		//		        AND paev.aamm = w_aamm
		//		        AND paev.nmat = wsal01.nmat
		//		        AND paev.nbul = w_nbul
		//		        AND paevcg.cdos = paev.cdos
		//		        AND paevcg.aamm = paev.aamm
		//		        AND paevcg.nmat = paev.nmat
		//		        AND paevcg.nbul = paev.nbul
		//		      ORDER BY paev.cdos , paev.aamm , paev.nmat , paevcg.ddeb;
		String queryString = "select a.comp_id.cdos, a.comp_id.aamm, a.comp_id.nmat, a.comp_id.nbul, a.comp_id.ddeb,a.dfin," + "a.nbjc, a.nbja, a.motf, a.mont, a.cuti," + "b.comp_id.cdos, b.comp_id.aamm, b.comp_id.nmat, b.comp_id.nbul, b.ddpa, b.dfpa, b.bcmo"
				+ " from Rhteltvarconge a, Rhteltvarent b" + " where b.comp_id.cdos = '" + dossier + "'" + " and b.comp_id.aamm = '" + periode + "'" + " and b.comp_id.nmat = '" + nmat + "'" + " and b.comp_id.nbul = " + numerobulletin + " and a.comp_id.cdos = b.comp_id.cdos"
				+ " and a.comp_id.aamm = b.comp_id.aamm" + " and a.comp_id.nmat = b.comp_id.nmat" + " and a.comp_id.nbul = b.comp_id.nbul" + " order by b.comp_id.cdos, b.comp_id.aamm, b.comp_id.nmat, a.comp_id.ddeb";

		//		BEGIN
		//		   bas_con := 0;
		//		   w_moncp := 0;
		//		   w_nbjcp := 0;
		//		   w_conge := 0;
		//		   w_nbjtr := 0;
		//		   cgan_ms := FALSE;
		//		   exist_evcg := FALSE;
		//		   nb_evcg := 0;
		//
		//		   w_nbjreliq := 0;
		//
		//		   mois_conge := FALSE;
		//
		List l = service.find(queryString);
		Object[] ev = null;
		DetailEv elementConge = null;
		for (Object obj : l)
		{
			//		   OPEN curs_cg;
			//		   LOOP
			//		     FETCH curs_cg
			//		      INTO wevcg.cdos1,wevcg.aamm2,wevcg.nmat3,wevcg.nbul4,wevcg.ddeb5,wevcg.dfin6,
			//		           wevcg.nbjc7,wevcg.nbja8,wevcg.motf9,wevcg.mont10,wevcg.cuti11,
			//		           wpaev.cdos12,wpaev.aamm13,wpaev.nmat14,wpaev.nbul15,wpaev.ddpa16,wpaev.dfpa17,
			//		           wpaev.bcmo18;
			//		     EXIT WHEN curs_cg%NOTFOUND;
			ev = (Object[]) obj;
			//
			//		     exist_evcg := TRUE;
			exist_evcg = true;
			//
			//		    BEGIN
			//		    SELECT max(decode(nume,1,valm,0)),
			//		           max(decode(nume,3,valm,0))
			//		      INTO wfnom.mnt1,wfnom.mnt3
			//		      FROM pafnom
			//		     WHERE cdos = wpdos.cdos
			//		       AND ctab = 22
			//		       AND cacc = wevcg.motf
			//		       AND nume in (1,3);
			//		    EXCEPTION
			//		      WHEN NO_DATA_FOUND THEN null;
			//		    END;
			Object[] o = (Object[]) service.find(
					"select max(case nume when 1 then valm else 0 end), " + " max(case nume when 3 then valm else 0 end) from Rhfnom" + " where comp_id.cdos = '" + dossier + "'" + " and comp_id.ctab = 22" + " and comp_id.cacc = '" + (String) ev[8] + "'" + " and comp_id.nume in(1, 3)").get(0);
			//
			//		    IF SQL%FOUND THEN
			if (o != null && o[0] != null && o[1] != null)
			{
				//
				//		      -- MM 07-2004 ajout de la prise en compte des conges jours de reliquat : cg deja paye
				//			IF NOT PA_PAIE.NouB(w_motif_reliq) AND w_motif_reliq = wevcg.motf THEN
				//		         -- jours de reliquats pris
				//		         w_nbjreliq := NVL(w_nbjreliq,0) + wevcg.nbja;
				//			END IF;
				if (!ClsObjectUtil.isNull(w_motif_reliq) && w_motif_reliq.equals((String) ev[8]))
				{
					w_nbjreliq += (o[7] instanceof BigDecimal ? (BigDecimal) o[7] : (Long) o[7]).intValue();
				}
				//
				int mnt1 = (o[0] instanceof BigDecimal ? (BigDecimal) o[0] : (Long) o[0]).intValue();
				int mnt3 = (o[1] instanceof BigDecimal ? (BigDecimal) o[1] : (Long) o[1]).intValue();
				//		      IF wfnom.mnt1 = 1 AND wfnom.mnt3 = 1
				//		      THEN
				//		         -- Conges ponctuels
				//		         w_nbjcp := w_nbjcp + wevcg.nbjc;
				//		      END IF;
				if (o != null && mnt1 == 1 && mnt3 == 1)
				{
					w_nbjcp += (ev[6] instanceof BigDecimal ? (BigDecimal) ev[6] : (Long) ev[6]).intValue();
				}
				//
				//		      IF wfnom.mnt1 = 1 AND wfnom.mnt3 = 0
				//		      THEN
				if (mnt1 == 1 && mnt3 == 0)
				{
					//		         -- Conges annuels
					//		        IF wevcg.ddeb = wpaev.ddpa AND
					//		           wevcg.dfin = wpaev.dfpa
					//		        THEN
					//		           mois_conge := TRUE;      -- Salarie en conge tout le mois
					//		        END IF;
					if (((Date) ev[4]).equals((Date) ev[15]) && ((Date) ev[16]).equals((Date) ev[5]))
					{
						mois_conge = true;
					}
					//
					//		        IF tab91 THEN
					//		          FOR ind91_sav IN 1..nb_t91 LOOP
					//		            ind91 := ind91_sav;
					//		            IF T91_periode(ind91) = TO_CHAR(wpdos.ddmp,'YYYYMM') THEN
					//		              EXIT;
					//		            END IF;
					//		          END LOOP;
					//		          -- MM 17/11/1999
					//		          IF ind91 < nb_t91 THEN
					//		             IF wevcg.ddeb > T91_df(ind91 + 1) THEN
					//		                cgan_ms := TRUE;
					//		             END IF;
					//		          END IF;
					String d1 = null;
					int i = 0;
					if (tab91)
					{
						for (ClsInfoPeriode period : listOfPeriodeT91)
						{
							i++;
							if (period.ddebut != null)
							{
								d1 = new ClsDate(period.ddebut).getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
								if (moisPaieCourant.equals(d1))
									break;
							}
						}
						if (i < listOfPeriodeT91.size())
						{
							ClsInfoPeriode period = listOfPeriodeT91.get(i);
							if (ev[4] != null && ((Date) ev[4]).compareTo(period.dfin) > 0)
							{
								cgan_ms = true;
							}
						}
					}
					//		        ELSE
					//		           w_am := to_char(wevcg.ddeb,'YYYY')||to_char(wevcg.ddeb,'MM');
					//		           IF w_am > w_aamm THEN
					//		              cgan_ms := TRUE;
					//		           END IF;
					//		        END IF;
					else
					{
						w_am = new ClsDate((Date) ev[4]).getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
						if (w_am.compareTo(periode) > 0)
						{
							cgan_ms = true;
						}
					}
					//		      END IF;
				}
			}
			//		    ELSE
			//		       wfnom.mnt1 := NULL;
			//		       wfnom.mnt3 := NULL;
			//		    END IF;
			else
			{

			}
			//
			//		    nb_evcg := nb_evcg + 1;
			//
			elementConge = new DetailEv();
			//		    tab_evcg_ddpa(nb_evcg) := wpaev.ddpa;
			//		    tab_evcg_dfpa(nb_evcg) := wpaev.dfpa;
			//		    tab_evcg_bcmo(nb_evcg) := wpaev.bcmo;
			//
			//		    tab_evcg_ddeb(nb_evcg) := wevcg.ddeb;
			//		    tab_evcg_dfin(nb_evcg) := wevcg.dfin;
			//		    tab_evcg_nbjc(nb_evcg) := wevcg.nbjc;
			//		    tab_evcg_nbja(nb_evcg) := wevcg.nbja;
			//		    tab_evcg_motf(nb_evcg) := wevcg.motf;
			//		    tab_evcg_mont(nb_evcg) := wevcg.mont;

			//		    tab_motf_mnt1(nb_evcg) := wfnom.mnt1;
			//		    tab_motf_mnt3(nb_evcg) := wfnom.mnt3;

			elementConge = new DetailEv();
			if (ev[17] != null)
				elementConge.bcmo = ev[17].toString().toCharArray()[0];
			if (ev[4] != null)
				elementConge.ddeb = (Date) ev[4];
			if (ev[15] != null)
				elementConge.ddpa = (Date) ev[15];
			if (ev[5] != null)
				elementConge.dfin = (Date) ev[5];
			if (ev[16] != null)
				elementConge.dfpa = (Date) ev[16];
			if (ev[0] != null)
				elementConge.mnt1 = (o[0] instanceof BigDecimal ? (BigDecimal) o[0] : (Long) o[0]).intValue();
			if (ev[1] != null)
				elementConge.mnt3 = (o[1] instanceof BigDecimal ? (BigDecimal) o[1] : (Long) o[1]).intValue();
			if (ev[9] != null)
				elementConge.mont = (ev[9] instanceof BigDecimal ? (BigDecimal) ev[9] : (Double) ev[9]).doubleValue();
			if (ev[8] != null)
				elementConge.motf = (String) ev[8];
			if (ev[7] != null)
				elementConge.nbja = (ev[7] instanceof BigDecimal ? (BigDecimal) ev[7] : (Long) ev[7]).intValue();
			if (ev[6] != null)
				elementConge.nbjc = (ev[6] instanceof BigDecimal ? (BigDecimal) ev[6] : (Long) ev[6]).intValue();
			//
			listOfEltVarConge.add(elementConge);
			//
			//		  END LOOP;

		}
		//		  CLOSE curs_cg;
		//
		//		  IF w_nbjtr < 0 THEN
		//		     w_nbjtr := 0;
		//		  END IF;
		//
		//		  -- Si pas de passage du conges generant une absence sur le mois suivant,
		//		  -- alors on ne passera pas dans la fonction 'alim_evcg_ms'.
		//		  IF PA_PAIE.NouB(w_cg_abs) THEN
		//		     cgan_ms := FALSE;
		//		  END IF;
		
		if(w_nbjtr < 0)
			w_nbjtr = 0;
		
		if (ClsObjectUtil.isNull(w_cg_abs))
			cgan_ms = false;
		//
		//		  RETURN TRUE;
		//
		//		END lect_ev;
		//
		return true;
	}

	**
	 * =>alim_evcg_ms Generation de conges sur mois suivant par l'appel de la fonction 'pas_moi'
	 * 
	 * @return
	 *
	private boolean alimenteCongeMoisProchain(String nmat)
	{
		com.cdi.deltarh.service.ClsParameter.println(">>alimenteCongeMoisProchain");
		//	FUNCTION alim_evcg_ms RETURN BOOLEAN
		//	IS
		//
		//	  i             SMALLINT;
		//	  ddeb_sav      DATE;
		//	  dfin_sav      DATE;
		//	  ddpa_ms       DATE;
		//
		//	  t91_idmp      SMALLINT;
		//	  t91_imcg      SMALLINT;
		//
		//	  wnbj_a        NUMBER(5,2);
		//	  wnbj_c        NUMBER(5,2);
		//	  snbj_a        NUMBER(5,2);
		//	  snbj_c        NUMBER(5,2);
		int t91_idmp = 0;
		int t91_imcg = 0;
		//		int wnbj_a = 0;
		//		int wnbj_c = 0;
		int snbj_a = 0;
		int snbj_c = 0;
		Date ddeb_sav = null;
		Date dfin_sav = null;
		Date ddpa_ms = null;

		//
		//	BEGIN
		//
		//
		DetailEv evcg = null;
		//	  FOR i IN 1..nb_evcg LOOP
		List<DetailEv> newListe = new ArrayList<DetailEv>();
		for (Object obj : listOfEltVarConge)
		{
			ClsParameter.println("-----------------------Debut alimentation des conges du mois suivant pour salari� = " + nmat + " ------------------");
			evcg = (DetailEv) obj;
			//
			//	    -- Si mouvement de conges annuels
			//	    IF tab_motf_mnt1(i) = 1 AND tab_motf_mnt3(i) = 0 THEN
			if (evcg.mnt1 == 1 && evcg.mnt3 == 0)
			{
				//	       IF tab91 THEN
				if (tab91)
				{
					//
					//	          t91_idmp := 0;
					//	          t91_imcg := 0;
					t91_idmp = 0;
					t91_imcg = 0;
					//	          --  Recherche de la derniere periode cloture dans la table 91
					//	          FOR ind91_sav IN 1..nb_t91 LOOP
					//	             ind91 := ind91_sav;
					//	             IF T91_periode(ind91) = TO_CHAR(wpdos.ddmp,'YYYYMM') THEN
					//	                t91_idmp := ind91;
					//	             END IF;
					//	             -- MM 02/01/00
					//	             IF tab_evcg_ddeb(i) >= T91_dd(t91_idmp+1) AND
					//	                tab_evcg_dfin(i) <= T91_df(t91_idmp+1)
					//	             THEN
					//	                NULL;
					//	             ELSE
					//	                IF tab_evcg_ddeb(i) >= T91_dd(ind91) AND
					//	                   tab_evcg_ddeb(i) <= T91_df(ind91)
					//	                THEN
					//	                   t91_imcg := ind91;
					//	                   EXIT;
					//	                END IF;
					//	             END IF;
					//	          END LOOP;
					int ind91 = 0;
					for (int j=0; j<listOfPeriodeT91.size(); j++)
					{
						ind91 = j;
						if (listOfPeriodeT91.get(j).periode.equals(myPeriode.getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM)))
						{
							t91_idmp = ind91;
						}
						ClsInfoPeriode infotmp = listOfPeriodeT91.get(t91_idmp + 1);
						if (!(evcg.ddeb.compareTo(infotmp.ddebut) >= 0 && evcg.ddeb.compareTo(infotmp.dfin) <= 0))
						{
							infotmp = listOfPeriodeT91.get(ind91);
							if (!(evcg.ddeb.compareTo(infotmp.ddebut) >= 0 && evcg.ddeb.compareTo(infotmp.dfin) <= 0))
							{
								t91_imcg = ind91;
								break;
							}
						}
						//
						
					}
					//
					//	          --  Test si le mouvement de conges commence sur la prochaine periode
					//	          --  ATT sur T91
					//	          IF t91_imcg - t91_idmp > 1 THEN
					if (t91_imcg - t91_idmp > 1)
					{
						//	             -- Generation de l'absence correspondant au conges
						//	             IF NOT pas_moi(i) THEN
						//	                RETURN FALSE;
						//	             END IF;
						if (!insertCongeMoisSuivant(evcg, nmat))
						{
							return false;
						}
						//
						//	             -- Generation de l'absence comprise entre le premier jour de la
						//	             --    periode suivante et le debut du conges
						//	             -- MM 27/07/2000
						//	             -- uniquement dans le cas du fictif B.
						//	             IF (w_fictif = 'O' AND w_typ_fictif = 'B') AND (tab_evcg_ddeb(i) > T91_dd(t91_imcg))
						//	             THEN
						if ("O".equals(w_fictif) && "B".equals(w_typ_fictif) && evcg.ddeb.compareTo(listOfPeriodeT91.get(t91_imcg).ddebut)>0)
						{
							//	                ddeb_sav := tab_evcg_ddeb(i);
							//	                dfin_sav := tab_evcg_dfin(i);
							//	                snbj_a   := tab_evcg_nbja(i);
							//	                snbj_c   := tab_evcg_nbjc(i);
							//
							//	                tab_evcg_dfin(i) := tab_evcg_ddeb(i) - 1;
							//	                tab_evcg_ddeb(i) := T91_dd(t91_imcg);
							//
							//	                calc_nb_jours(wnbj_a,
							//	                              wnbj_c,
							//	                              tab_evcg_ddeb(i),
							//	                              tab_evcg_dfin(i));
							//	                tab_evcg_nbja(i) := wnbj_a;
							//	                tab_evcg_nbjc(i) := 0;
							//
							//	                IF NOT pas_moi(i) THEN
							//	                   RETURN FALSE;
							//	                END IF;
							//
							//	                tab_evcg_ddeb(i) := ddeb_sav;
							//	                tab_evcg_dfin(i) := dfin_sav;
							//	                tab_evcg_nbja(i) := snbj_a;
							//	                tab_evcg_nbjc(i) := snbj_c;
							ddeb_sav = evcg.ddeb;
							dfin_sav = evcg.dfin;
							snbj_a = evcg.nbja;
							snbj_c = evcg.nbjc;
							//
							evcg.dfin = new ClsDate(evcg.ddeb).addDay(-1);
							evcg.ddeb = listOfPeriodeT91.get(t91_imcg).ddebut;
							//
							nbreJourOuvrableNonOuvrable(evcg.ddeb, evcg.dfin);
							evcg.nbja = wnbj_a;
							evcg.nbjc = 0;
							//
							if (!insertCongeMoisSuivant(evcg, nmat))
							{
								return false;
							}
							//
							evcg.ddeb = ddeb_sav;
							evcg.dfin = dfin_sav;
							evcg.nbja = snbj_a;
							evcg.nbjc = snbj_c;
						}
						//	             END IF;
						//
						//	          END IF;
					}
				}
				//	       ELSE
				else
				{
					//	          --  Test si le mouvement de conges commence sur le prochain mois de paie
					//	          w_am := to_char(tab_evcg_ddeb(i),'YYYY')||to_char(tab_evcg_ddeb(i),'MM');
					w_am = new ClsDate(evcg.ddeb).getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
					//	          IF w_am > w_aamm THEN
					if (w_am.compareTo(periode) > 0)
					{
						//	             IF NOT pas_moi(i) THEN
						//	                RETURN FALSE;
						//	             END IF;
						if (!insertCongeMoisSuivant(evcg, nmat))
						{
							return false;
						}
						//
						//	             -- Dates de debut du prochain mois de paie
						//	             ddpa_ms := TO_DATE(TO_CHAR(tab_evcg_ddeb(i),'YYYYMM'),'YYYYMM');
						String s = new ClsDate(evcg.ddeb).getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
						ddpa_ms = new ClsDate(s, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getDate();
						//
						//	             -- Generation de l'absence comprise entre le premier jour de la
						//	             --    periode suivante et le debut du conges
						//	             -- MM 27/07/2000
						//	             -- uniquement dans le cas du fictif B.
						//	             IF (w_fictif = 'O' AND w_typ_fictif = 'B') AND tab_evcg_ddeb(i) > ddpa_ms
						//	             THEN
						if ("O".equals(w_fictif) && "B".equals(w_typ_fictif) && evcg.ddeb.compareTo(ddpa_ms)>0)
						{
							//	                ddeb_sav := tab_evcg_ddeb(i);
							//	                dfin_sav := tab_evcg_dfin(i);
							//	                snbj_a   := tab_evcg_nbja(i);
							//	                snbj_c   := tab_evcg_nbjc(i);
							//
							//	                tab_evcg_dfin(i) := tab_evcg_ddeb(i) - 1;
							//	                tab_evcg_ddeb(i) := ddpa_ms;
							//
							//	                calc_nb_jours(wnbj_a,
							//	                              wnbj_c,
							//	                              tab_evcg_ddeb(i),
							//	                              tab_evcg_dfin(i));
							//	                tab_evcg_nbja(i) := wnbj_a;
							//	                tab_evcg_nbjc(i) := 0;
							//
							//	                IF NOT pas_moi(i) THEN
							//	                   RETURN FALSE;
							//	                END IF;
							//
							//	                tab_evcg_ddeb(i) := ddeb_sav;
							//	                tab_evcg_dfin(i) := dfin_sav;
							//	                tab_evcg_nbja(i) := snbj_a;
							//	                tab_evcg_nbjc(i) := snbj_c;
							ddeb_sav = evcg.ddeb;
							dfin_sav = evcg.dfin;
							snbj_a = evcg.nbja;
							snbj_c = evcg.nbjc;
							//
							evcg.dfin = new ClsDate(evcg.ddeb).addDay(-1);
							evcg.ddeb = ddpa_ms; //listOfPeriodeT91.get(t91_imcg).ddebut;
							//
							nbreJourOuvrableNonOuvrable(evcg.ddeb, evcg.dfin);
							evcg.nbja = wnbj_a;
							evcg.nbjc = 0;
							//
							if (!insertCongeMoisSuivant(evcg, nmat))
							{
								return false;
							}
							//
							evcg.ddeb = ddeb_sav;
							evcg.dfin = dfin_sav;
							evcg.nbja = snbj_a;
							evcg.nbjc = snbj_c;
							//	             END IF;
						}
						//
						//	          END IF;
					}
					//	       END IF;
				}
				//	    END IF;
			}
			//	  END LOOP;
			newListe.add(evcg);
		}
		
		listOfEltVarConge = newListe;
		//
		//	  RETURN TRUE;
		return true;
		//
		//	END alim_evcg_ms;
	}

	**
	 * =>
	 * <p>
	 * pas_moi
	 * <p>
	 * INSERTION DE CONGES SUR LE MOIS SUIVANT
	 * <p>
	 * Ces absences sont generes par les conges lorsque le libelle 4 de BASE-CONGE est renseigne (variable w_cg_abs).
	 * </p>
	 * <p>
	 * Cette generation se fait lors de la cloture du mois d'imputation des conges.
	 * </p>
	 * <p>
	 * L'appel est fait dans la fonction 'alim_evcg_ms' qui est elle meme declechee lorsque l'on a des conges sur le mois suivant (cgan_ms=TRUE) ET que le.
	 * </p>
	 * <p>
	 * salarie n'est pas radie ET qu'il s'agit du bulletin 9.
	 * </p>
	 * <p>
	 * 'l_cg' est le numero de la ligne concernee dans le tableau ou ont ete stockes les mouvements de conges et absences.
	 * </p>
	 * 
	 * @param infoperiode
	 * @param nmat
	 * @return true si tout s'est bien pass�; et false sinon
	 *
	private boolean insertCongeMoisSuivant(ClsAgentUpdateBulletinSave.DetailEv infoperiode, String nmat)
	{
		com.cdi.deltarh.service.ClsParameter.println(">>insertCongeMoisSuivant");
		//	FUNCTION pas_moi(l_cg IN SMALLINT) RETURN BOOLEAN
		//	IS
		//
		//	annee          VARCHAR2(4);
		//	mois           VARCHAR2(2);
		Date ddpa = null;
		Date dfpa = null;
		//
		//	BEGIN
		//	   -- On sort de la fonction si pas de passage du conges generant
		//	   -- une absence sur le mois suivant.
		//	   IF PA_PAIE.NouB(w_cg_abs) THEN
		//	      RETURN TRUE;
		//	   END IF;
		if (ClsObjectUtil.isNull(w_cg_abs))
			return true;
		//
		//	   wevcg.cdos := wpdos.cdos;
		//	   wevcg.ddeb := tab_evcg_ddeb(l_cg);
		//	   wevcg.dfin := tab_evcg_dfin(l_cg);
		//
		//	-- MM    wevcg.nbjc := 0;  --tab_evcg_nbjc(l_cg);
		//	   wevcg.nbjc := tab_evcg_nbjc(l_cg);
		//
		//	   wevcg.nbja := tab_evcg_nbja(l_cg);
		//	   wevcg.motf := tab_evcg_motf(l_cg);
		//	   wevcg.mont := tab_evcg_mont(l_cg);
		//	   wevcg.aamm := prochain_mois;
		//	   wevcg.nbul := 9;
		//	   wevcg.cuti := w_cuti;
		//		ClsUpdateBulletin.DetailEv infoperiode = (DetailEv)listOfEltVarConge.get(evIdx);
		Rhteltvarconge evconge = new Rhteltvarconge();
		//evconge.setComp_id(new RhteltvarcongePK(dossier, prochainmois, nmat, numerobulletin, infoperiode.ddeb));
		evconge.setComp_id(new RhteltvarcongePK(dossier, prochainmois, nmat, 9, infoperiode.ddeb));
		evconge.setDfin(infoperiode.dfin);
		evconge.setNbjc(new BigDecimal(infoperiode.nbjc));
		evconge.setNbja(new BigDecimal(infoperiode.nbja));
		evconge.setMotf(infoperiode.motf);
		evconge.setMont(new BigDecimal(infoperiode.mont));
		evconge.setCuti(user);
		//
		//	  IF tab91 THEN
		if (tab91)
		{
			//	     BEGIN
			//	     SELECT vall INTO wfnom.lib2 FROM pafnom
			//	     WHERE cdos = wpdos.cdos
			//	       AND ctab = 91
			//	       AND cacc = prochain_mois
			//	       AND nume = 2;
			//	     EXCEPTION
			//	       WHEN NO_DATA_FOUND THEN null;
			//	     END;
			//
			//	    IF SQL%NOTFOUND THEN
			//	        w_mess := PA_PAIE.erreurp('ERR-90131',w_clang,prochain_mois);
			//	        RETURN FALSE;
			//	    ELSE
			//	       SELECT vall INTO wfnom.lib3 FROM pafnom
			//	        WHERE cdos = wpdos.cdos
			//	          AND ctab = 91
			//	          AND cacc = prochain_mois
			//	          AND nume = 3;
			//	       wpaev.ddpa := TO_DATE(wfnom.lib2,'DD/MM/YYYY');
			//	       wpaev.dfpa := TO_DATE(wfnom.lib3,'DD/MM/YYYY');
			//	    END IF;
			Object o = (this._getRhfnom(dossier, 91, prochainmois, 2));
			String libelle2 = "";
			String libelle3 = "";
			if (o != null)
			{
				libelle2 = ((Rhfnom) o).getVall();
				o = (this._getRhfnom(dossier, 91, prochainmois, 3));
				if (o != null)
				{
					libelle3 = ((Rhfnom) o).getVall();
					ddpa = new ClsDate(libelle2).getDate();
					dfpa = new ClsDate(libelle3).getDate();
				}
			}
			else
			{
				error = parameter.errorMessage("ERR-90131", langue, prochainmois);
				com.cdi.deltarh.service.ClsParameter.println(error);
				ClsGlobalUpdate._setEvolutionTraitement(request, error, true);
				return false;
			}
			//
		}
		//	  ELSE
		else
		{
			//	      annee := substr(prochain_mois,1,4);
			//	      mois  := substr(prochain_mois,5,2);
			//	      wpaev.ddpa := TO_DATE(mois||'01'||annee,'MMDDYYYY');
			//	      wpaev.dfpa := LAST_DAY(wpaev.ddpa);
			ddpa = new ClsDate(mois_ms, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getDate();
			dfpa = new ClsDate(ddpa).getLastDayOfMonth();
		}
		//	  END IF;
		//
		//	  ---------------------------
		//	  wevcg.motf := w_cg_abs;
		evconge.setMotf(w_cg_abs);
		//	  ---------------------------
		//
		//	BEGIN
		//	  SELECT 'X' INTO w_exist FROM paev
		//	   WHERE cdos = wevcg.cdos
		//	     AND nmat = wevcg.nmat
		//	     AND aamm = wevcg.aamm
		//	     AND nbul = wevcg.nbul;
		//	  EXCEPTION
		//	    WHEN NO_DATA_FOUND THEN null;
		//	  END;
		//	  IF SQL%NOTFOUND THEN
		//	     INSERT INTO paev
		//	            VALUES (wevcg.cdos,wevcg.aamm,wevcg.nmat,wevcg.nbul,wpaev.ddpa,
		//	                    wpaev.dfpa,'N');
		//	  END IF;
		//
		//	  BEGIN
		//	  SELECT 'X' INTO w_exist FROM paevcg
		//	   WHERE cdos = wevcg.cdos
		//	     AND nmat = wevcg.nmat
		//	     AND aamm = wevcg.aamm
		//	     AND nbul = wevcg.nbul
		//	     AND ddeb = wevcg.ddeb;
		//	  EXCEPTION
		//	    WHEN NO_DATA_FOUND THEN null;
		//	  END;
		//
		//	  IF SQL%NOTFOUND THEN
		//	     INSERT INTO paevcg
		//	            VALUES (wevcg.cdos,wevcg.aamm,wevcg.nmat,wevcg.nbul,wevcg.ddeb,
		//	                    wevcg.dfin,wevcg.nbjc,wevcg.nbja,wevcg.motf,wevcg.mont,wevcg.cuti);
		//	  END IF;
		//
		Object o = service.get(Rhteltvarent.class, new RhteltvarentPK(evconge.getComp_id().getCdos(), evconge.getComp_id().getAamm(), evconge.getComp_id().getNmat(), evconge.getComp_id().getNbul()));
		if (o == null)
		{
			Rhteltvarent event = new Rhteltvarent();
			event.setComp_id(new RhteltvarentPK(evconge.getComp_id().getCdos(), evconge.getComp_id().getAamm(), evconge.getComp_id().getNmat(), evconge.getComp_id().getNbul()));
			event.setDdpa(ddpa);
			event.setDfpa(dfpa);
			event.setBcmo("N");
			//
			oSession.save(event);
		}
		//
		o = service.get(Rhteltvarconge.class, evconge.getComp_id());
		if (o == null)
			oSession.save(evconge);
		//
		//	  RETURN TRUE;
		return true;
		//
		//	END pas_moi;
	}

	**
	 * =>calc_nb_jours Calcul du nombre de jours ouvrables et non ouvrables entre 2 dates (jours reels)
	 *
	private void nbreJourOuvrableNonOuvrable(Date debut, Date fin)
	{
		com.cdi.deltarh.service.ClsParameter.println(">>nbreJourOuvrableNonOuvrable");
		//	PROCEDURE calc_nb_jours(p_nbja  IN OUT NUMBER,
		//	                        p_nbjc  IN OUT NUMBER,
		//	                        p_date1 IN DATE,
		//	                        p_date2 IN DATE)
		//	IS
		//	BEGIN
		//	   SELECT COUNT(*) INTO p_nbja FROM pacal
		//	    WHERE cdos = wpdos.cdos
		//	      AND jour >= p_date1
		//	      AND jour <= p_date2;
		List l = service.find("select count(*) from Rhpcalendrier" + " where comp_id.cdos = '" + dossier + "'" + " and comp_id.jour >= '" + new ClsDate(debut).getDateS(dateformat) + "'" + " and comp_id.jour <= '" + new ClsDate(fin).getDateS(dateformat) + "'");
		wnbj_a = l.size();
		//
		//	   SELECT COUNT(*) INTO p_nbjc FROM pacal
		//	    WHERE cdos = wpdos.cdos
		//	      AND jour >= p_date1
		//	      AND jour <= p_date2
		//	      AND ouvr = 'O'
		//	      AND fer  = 'N';
		l = service.find("select count(*) from Rhpcalendrier" + " where comp_id.cdos = '" + dossier + "'" + " and comp_id.jour >= '" + new ClsDate(debut).getDateS(dateformat) + "'" + " and comp_id.jour <= '" + new ClsDate(fin).getDateS(dateformat) + "'" + " and ouvr = 'O'" + " and fer = 'N'");
		wnbj_c = l.size();
		//
		//	   RETURN;
		//
		//	END calc_nb_jours;
	}

	**
	 * =>maj_conges_annuels Conges payes pris
	 *
	private boolean updateCongeAnnuel(DetailEv ev, Rhpagent agent)
	{
		com.cdi.deltarh.service.ClsParameter.println(">>updateCongeAnnuel");
		//	FUNCTION maj_conges_annuels(i IN SMALLINT) RETURN BOOLEAN
		//	IS
		//
		//	  nbjstot   SMALLINT;
		//	  wnbjcad   SMALLINT;
		//	  wnbjcad2  SMALLINT;
		int wnbjcad2 = 0;
		int wnbjcad = 0;
		int nbjstot = 0;
		//
		//	BEGIN
		//
		//	  -- si le salarie a des conges payes et des conges payes non pris
		//	  -- alors RAZ de la base conges
		//	  IF pnp THEN
		if (pnp)
		{
			//	      wsal01.japa  := 0;
			//	      wsal01.japec := 0;
			//	      wsal01.jrla  := 0;
			//	      wsal01.jrlec := 0;
			//	      wsal01.jded  := 0;
			//	      wsal01.nbjse := 0;
			//	      wsal01.nbjsa := 0;
			//	      wsal01.nbjsm := 0;
			//	      wsal01.ddcf  := NULL;
			//	      wsal01.dfcf  := NULL;
			//	      wsal01.ddenv := NULL;
			//	      wsal01.drenv := NULL;
			//	      wsal01.nbjtr := 0;
			//	      wsal01.pmcf  := ' ';
			//	      wsal01.nbjcf := 0;
			//	      wsal01.nbjaf := 0;
			//	      wsal01.drtcg := NULL;
			//	      wsal01.dapa  := 0;
			//	      wsal01.dded  := 0;
			//	      wsal01.jded  := 0;
			//	      wsal01.dapec := 0;
			//	      wsal01.mtcf  := 0;
			//	      wsal01.drtcg := tab_evcg_dfin(i);    -- Date de retour des conges annuels
			agent.setJapa(new BigDecimal(0));
			agent.setJapec(new BigDecimal(0));
			agent.setJrla(new BigDecimal(0));
			agent.setJrlec(new BigDecimal(0));
			agent.setJded(new BigDecimal(0));
			agent.setNbjse(new BigDecimal(0));
			agent.setNbjsa(new BigDecimal(0));
			agent.setNbjsm(new BigDecimal(0));
			agent.setDdcf(null);
			agent.setDfcf(null);
			agent.setDdenv(null);
			agent.setDrenv(null);
			agent.setNbjtr(new BigDecimal(0));
			agent.setPmcf("");
			agent.setNbjcf(new BigDecimal(0));
			agent.setNbjaf(new BigDecimal(0));
			agent.setDrtcg(ev.dfin);
			agent.setDapa(new BigDecimal(0));
			agent.setDded(new BigDecimal(0));
			agent.setDdcf(null);
			agent.setDfcf(null);
			agent.setJded(new BigDecimal(0));
			agent.setDapec(new BigDecimal(0));
			agent.setMtcf(new BigDecimal(0));

			ClsParameter.println("-------Pour le salari� " + agent.getComp_id().getNmat() + " Cas pnp=" + pnp + ", Japa=" + agent.getJapa() + ", Japec=" + agent.getJapec() + " pour nbjcad=" + wnbjcad);
		}
		//	  ELSE
		//	     -- Decompte en jours Ouvrables ou Calendaires
		//	     IF wpdos.dccg = 'O' THEN
		//	         wnbjcad := tab_evcg_nbjc(i);
		//	     ELSE
		//	         wnbjcad := tab_evcg_nbja(i);
		//	     END IF;
		else
		{
			if ("O".equals(rhpdo.getDccg()))
			{
				wnbjcad = ev.nbjc;
			}
			else
			{
				wnbjcad = ev.nbja;
			}
			//
			//	     -- Mise a jour des droits en jrs supp
			//	     nbjstot := wsal01.nbjse + wsal01.nbjsa + wsal01.nbjsm;
			nbjstot = agent.getNbjse().intValue() + agent.getNbjsa().intValue() + agent.getNbjsm().intValue();
			//
			//	     IF wnbjcad >= nbjstot THEN
			//	        wsal01.nbjse := 0;
			//	        wsal01.nbjsa := 0;
			//	        wsal01.nbjsm := 0;
			//	     ELSE
			//	        wnbjcad2 := nbjstot;
			//	        IF wnbjcad2 >= wsal01.nbjsa THEN
			//	           wnbjcad2 := wnbjcad2 - wsal01.nbjsa;
			//	           wsal01.nbjsa := 0;
			//	           IF wnbjcad2 >= wsal01.nbjse THEN
			//	              wnbjcad2 := wnbjcad2 - wsal01.nbjse;
			//	              wsal01.nbjse := 0;
			//	              IF wnbjcad2 >= wsal01.nbjsm THEN
			//	                 wsal01.nbjsm := 0;
			//	              ELSE
			//	                 wsal01.nbjsm := wsal01.nbjsm - wnbjcad2;
			//	              END IF;
			//	           ELSE
			//	              wsal01.nbjse := wsal01.nbjse - wnbjcad2;
			//	           END IF;
			//	        ELSE
			//	           wsal01.nbjsa := wsal01.nbjsa - wnbjcad2;
			//	        END IF;
			//	     END IF;

			if (wnbjcad >= nbjstot)
			{
				agent.setNbjse(new BigDecimal(0));
				agent.setNbjsa(new BigDecimal(0));
				agent.setNbjsm(new BigDecimal(0));
			}
			else
			{
				wnbjcad2 = nbjstot;
				if (wnbjcad2 >= agent.getNbjsa().intValue())
				{
					wnbjcad2 -= agent.getNbjsa().intValue();
					agent.setNbjsa(new BigDecimal(0));
					if (wnbjcad2 >= agent.getNbjse().intValue())
					{
						wnbjcad2 -= agent.getNbjse().intValue();
						agent.setNbjse(new BigDecimal(0));
						if (wnbjcad2 >= agent.getNbjsm().intValue())
						{
							agent.setNbjsm(new BigDecimal(0));
						}
						else
						{
							agent.setNbjsm(new BigDecimal(agent.getNbjsm().intValue() - wnbjcad2));
						}
					}
					else
					{
						agent.setNbjse(new BigDecimal(agent.getNbjse().intValue() - wnbjcad2));
					}
				}
				else
				{
					agent.setNbjsa(new BigDecimal(agent.getNbjsa().intValue() - wnbjcad2));
				}
			}
			//
			//	    -- ----- Mise a jour des droits en jrs
			//	     IF wnbjcad <= wsal01.japa THEN
			//	         wsal01.japa := wsal01.japa - wnbjcad;
			//	     ELSE
			//	         wsal01.japec := wsal01.japec - ( wnbjcad - wsal01.japa );
			//	         wsal01.japa  := 0;
			//	     END IF;
			if (wnbjcad <= agent.getJapa().intValue())
			{
				agent.setJapa(new BigDecimal(agent.getJapa().intValue() - wnbjcad));
			}
			else
			{
				agent.setJapec(new BigDecimal(agent.getJapec().intValue() - (wnbjcad - agent.getJapa().intValue())));
				agent.setJapa(new BigDecimal(0));
			}

			ClsParameter.println("----Mise a jour des droits en jrs-------Pour le salari� " + agent.getComp_id().getNmat() + ", Japa=" + agent.getJapa() + ", Japec=" + agent.getJapec() + " pour nbjcad=" + wnbjcad);
			//
			//	     -- MM 02/11/2004 maj uniquement a la fin des cg
			//	     IF tab_evcg_dfin(i) = wsal01.dfcf THEN
			//	        wsal01.drtcg := tab_evcg_dfin(i);    -- Date de retour des conges annuels
			//	     END IF;
			//
			//	     ca := TRUE;
			if(ev.dfin.equals(agent.getDfcf()))
				agent.setDrtcg(ev.dfin);
			
			ca = true;
			//
		}
		//	  END IF;
		//
		//	  RETURN TRUE;
		return true;
		//
		//	END maj_conges_annuels;
	}

	//	---------------------------------------------------------------------------------
	//	-- Mise a jour de la base conges salarie
	//	---------------------------------------------------------------------------------
	private boolean updateBaseCongeAgent(Rhpagent agent)
	{
		com.cdi.deltarh.service.ClsParameter.println(">>updateBAseCongeAgent");
		//
		//	FUNCTION maj_bas RETURN BOOLEAN
		//	IS
		//
		//	  annee   VARCHAR2(4);
		//	  mois    VARCHAR2(2);
		//	  l_expat BOOLEAN;
		//	  w_ret   BOOLEAN;
		//
		//	BEGIN
		//	   l_expat := FALSE;
		boolean l_expat = false;
		//		boolean w_ret = false;
		Object o = null;
		//
		//	 ----- pc le 12.06.95, si bas_con = 0 alors pas mise a jour des jours.
		//	 -- mois_conge = TRUE = En conges tout le mois
		//	  IF NOT PA_PAIE.NouZ(bas_con) THEN
		if (bas_con != 0)
		{
			//	     IF NOT mois_conge THEN
			if (!mois_conge)
			{
				//	        IF PA_PAIE.NouB(val_exp) THEN
				if (ClsObjectUtil.isNull(val_exp))
				{
					//	           BEGIN
					//	           SELECT valt INTO nbj_con FROM pafnom
					//	            WHERE cdos = wpdos.cdos
					//	            AND ctab = 35
					//	            AND cacc = wsal01.cat
					//	            AND nume = 3;
					//	            EXCEPTION
					//	              WHEN NO_DATA_FOUND THEN null;
					//	            END;
					//
					//	            IF SQL%NOTFOUND OR PA_PAIE.NouZ(nbj_con) THEN
					//	               nbj_con := nbjc_lx;
					//	            END IF;
					o = (this._getRhfnom(dossier, 35, agent.getCat(), 3));
					if (o != null)
					{
						nbj_con = ((Rhfnom) o).getValt().intValue();
					}
					else
						nbj_con = nbjc_lx;
					if(nbj_con <= 0)
						nbj_con = nbjc_lx;
				}
				//	        ELSE
				else
				{
					//	           IF typ_rec = 1 THEN
					//	              IF wsal01.regi = val_exp THEN
					//	                 l_expat := TRUE;
					//	                END IF;
					//	           ELSIF typ_rec = 2 THEN
					//	              IF wsal01.typc = val_exp THEN
					//	                 l_expat := TRUE;
					//	              END IF;
					//	           ELSIF typ_rec = 3 THEN
					//	              IF wsal01.clas = val_exp THEN
					//	                 l_expat := TRUE;
					//	              END IF;
					//	           END IF;
					if (typ_rec == 1)
					{
						if (val_exp.equals(agent.getRegi()))
							l_expat = true;
					}
					else if (typ_rec == 2)
					{
						if (val_exp.equals(agent.getTypc()))
							l_expat = true;
					}
					else if (typ_rec == 3)
					{
						if (val_exp.equals(agent.getClas()))
							l_expat = true;
					}
					//	           IF l_expat THEN
					if (l_expat)
					{
						//	             BEGIN
						//	              SELECT valt INTO nbj_con
						//	               FROM pafnom
						//	              WHERE cdos = wpdos.cdos
						//	               AND ctab = 35
						//	               AND cacc = wsal01.cat
						//	               AND nume = 5;
						//	             EXCEPTION
						//	               WHEN NO_DATA_FOUND THEN null;
						//	             END;
						//	              IF SQL%NOTFOUND OR PA_PAIE.NouZ(nbj_con) THEN
						//	                  nbj_con := nbjc_ex;
						//	              END IF;
						o = (this._getRhfnom(dossier, 35, agent.getCat(), 5));
						if (o != null)
							nbj_con = ((Rhfnom) o).getValt().intValue();
						else
							//nbj_con = nbjc_lx;
							nbj_con = nbjc_ex;
						if(nbj_con <= 0)
							nbj_con = nbjc_ex;
					}
					//	           ELSE
					else
					{
						//	             BEGIN
						//	              SELECT valt INTO nbj_con FROM pafnom
						//	              WHERE cdos = wpdos.cdos
						//	               AND ctab = 35
						//	               AND cacc = wsal01.cat
						//	               AND nume = 3;
						//	              EXCEPTION
						//	                WHEN NO_DATA_FOUND THEN null;
						//	              END;
						//
						//	              IF SQL%NOTFOUND OR PA_PAIE.NouZ(nbj_con) THEN
						//	                  nbj_con := nbjc_lx;
						//	              END IF;
						o = (this._getRhfnom(dossier, 35, agent.getCat(), 3));
						if (o != null)
							nbj_con = ((Rhfnom) o).getValt().intValue();
						else
							nbj_con = nbjc_lx;
						if(nbj_con <= 0)
							nbj_con = nbjc_lx;
						//	           END IF;
					}
					//	        END IF;
				}
			}
			//	     ELSE                -- Forcage du nombre de jours de conge acquis a zero
			else
			{
				//	        nbj_con := 0;    -- si le salarie est en conge tout le mois
				nbj_con = 0;
			}
			//	     END IF;
			//
			//	     -- MM 22/06/2004
			//	     w_ret := calc_pror_jcg;
			calculProrataNbreJourDroitParMois();
			//
			//	  END IF;
		}
		//
		//	  IF sal_RA_MU THEN
		if (sal_RA_MU)
		{
			//	     -- Le salarie est RAdie ou MUte
			//	     wsal01.cals  := 'N';
			//	     wsal01.japa  := 0;
			//	     wsal01.japec := 0;
			//	     wsal01.jrla  := 0;
			//	     wsal01.jrlec := 0;
			//	     wsal01.jded  := 0;
			//	     wsal01.nbjse := 0;
			//	     wsal01.nbjsa := 0;
			//	     wsal01.nbjsm := 0;
			//	     wsal01.ddcf  := NULL;
			//	     wsal01.dfcf  := NULL;
			//	     wsal01.ddenv := NULL;
			//	     wsal01.drenv := NULL;
			//	     wsal01.nbjtr := 0;
			//	     wsal01.pmcf  := ' ';
			//	     wsal01.nbjcf := 0;
			//	     wsal01.nbjaf := 0;
			//	     wsal01.drtcg := NULL;
			//	     wsal01.dapa  := 0;
			//	     wsal01.dded  := 0;
			//	     wsal01.ddcf  := NULL;
			//	     wsal01.dfcf  := NULL;
			//	     wsal01.dapec := 0;
			//	     wsal01.mtcf  := 0;
			//	     w_retour := sup_prets17;
			agent.setClas("N");
			agent.setJapa(new BigDecimal(0));
			agent.setJapec(new BigDecimal(0));
			agent.setJrla(new BigDecimal(0));
			agent.setJrlec(new BigDecimal(0));
			agent.setJded(new BigDecimal(0));
			agent.setNbjse(new BigDecimal(0));
			agent.setNbjsa(new BigDecimal(0));
			agent.setNbjsm(new BigDecimal(0));
			agent.setDdcf(null);
			agent.setDfcf(null);
			agent.setDdenv(null);
			agent.setDrenv(null);
			agent.setNbjtr(new BigDecimal(0));
			agent.setPmcf("");
			agent.setNbjcf(new BigDecimal(0));
			agent.setNbjaf(new BigDecimal(0));
			agent.setDrtcg(null);
			agent.setDapa(new BigDecimal(0));
			agent.setDded(new BigDecimal(0));
			agent.setDdcf(null);
			agent.setDfcf(null);
			agent.setJded(new BigDecimal(0));
			agent.setDapec(new BigDecimal(0));
			agent.setMtcf(new BigDecimal(0));
			//
			//		     w_retour := sup_prets17;
			int i = oSession.createQuery("update Rhtpretent" + " set pact = 'N'" + " where cdos = '" + dossier + "'" + " and nmat = '" + agent.getComp_id().getNmat() + "'").executeUpdate();
			//
			//	     UPDATE pasa01 SET
			//	      japa = wsal01.japa,
			//	      dapa = wsal01.dapa,
			//	      japec = wsal01.japec,
			//	      dapec = wsal01.dapec,
			//	      jded = wsal01.jded,
			//	      dded = wsal01.dded,
			//	      jrla = wsal01.jrla,
			//	      jrlec = wsal01.jrlec,
			//	      nbjcf = wsal01.nbjcf,
			//	      nbjaf = wsal01.nbjaf,
			//	      ddcf = wsal01.ddcf,
			//	      dfcf = wsal01.dfcf,
			//	      mtcf = wsal01.mtcf,
			//	      pmcf = wsal01.pmcf,
			//	      nbjse = wsal01.nbjse,
			//	      nbjsa = wsal01.nbjsa,
			//	      nbjsm = wsal01.nbjsm,
			//	      nmjf = wsal01.nmjf,
			//	      nbjtr = wsal01.nbjtr,
			//	      drtcg = wsal01.drtcg,
			//	      ddenv = wsal01.ddenv,
			//	      drenv = wsal01.drenv
			//	     WHERE cdos = wpdos.cdos
			//	       AND nmat = wsal01.nmat;
			//
			//	    RETURN TRUE;
			//
			ClsParameter.println("----------------------Update,Cas d'un salari� mut� ou radi�");
			oSession.update(agent);
			//
			return true;
			//	  END IF;
		}
		//
		//	  -- Calcul de la date d'anniversaire
		//	  -- Bascule des droits en cours --> anterieurs
		//	  -- Sinon ajout des jours acquis.
		//
		//	  mois  := to_char(w_date_anniv,'MM');
		//	  annee := to_char(w_date_anniv,'YYYY');
		ClsDate anniv = new ClsDate(w_date_anniv);
		//
		//	  IF substr(w_aamm,1,4) != annee AND substr(w_aamm,5,2) = mois THEN
		ClsParameter.println("----Date anniv agent = " + agent.getComp_id().getNmat() + " " + agent.getNom() + " = " + anniv.getDateS("dd-MM-yyyy"));
		ClsParameter.println("----myPeriode.getYear() = " + myPeriode.getDateS("yyyy") + " anniv.getYear() = " + anniv.getDateS("yyyy"));
		ClsParameter.println("----myPeriode.getMonth() = " + myPeriode.getDateS("MM") + " anniv.getMonth() = " + anniv.getDateS("MM"));
		if ((myPeriode.getYear() != anniv.getYear()) && (myPeriode.getMonth() == anniv.getMonth()))
		//if ((!(myPeriode.getDateS("yyyy").equals(anniv.getDateS("yyyy")))) && (myPeriode.getDateS("MM").equals(anniv.getDateS("MM"))))
		{
			ClsParameter.println("-----------------------Update,Cas de l'anniv du salari�");
			//
			//	      -- Date anniversaire
			//	      wsal01.japa  := wsal01.japa + wsal01.japec;
			//	      wsal01.japec := 0;
			//	      wsal01.dapa  := wsal01.dapa + wsal01.dapec;
			//	      wsal01.dapec := 0;
			//	      wsal01.jrla  := wsal01.jrla + wsal01.jrlec;
			//	      wsal01.jrlec := 0;
			//	      nbj_enf      := 0;
			//	      nbj_anc      := 0;
			//	      nbj_deco     := 0;
			agent.setJapa(agent.getJapa().add(agent.getJapec()));
			agent.setJapec(new BigDecimal(0));
			agent.setDapa(agent.getDapa().add(agent.getDapec()));
			agent.setDapec(new BigDecimal(0));
			agent.setJrla(agent.getJrla().add(agent.getJrlec()));
			agent.setJrlec(new BigDecimal(0));
			nbj_enf = 0;
			nbj_anc = 0;
			nbj_deco = 0;
			//
			//	     -- MM 23/06/2004
			//	     -- Calcul des jours sup si Lib1 BASE-CONGE = O ou A
			//	     IF calcul_nbjsup IN ('O','A') THEN
			//	        w_retour := calc_nbjsup;
			//	     END IF;
			if ("O".equals(calcul_nbjsup) || "A".equals(calcul_nbjsup))
			{
				//		    	  w_retour = calc_nbjsup;
				w_retour = calculateNbreJourSupplementaire(agent);
			}
			//
			//	     -- MM 23/06/2004
			//	     -- si calcul Anterieur 'A' des jours suppl. alors on bascule tous les jrs supp
			//	     -- en anterieur a la date anniversaire
			//	     IF calcul_nbjsup = 'O' THEN
			//	        wsal01.japec := wsal01.japec + nbj_con + nbj_anc + nbj_enf + nbj_deco;
			//	     ELSIF calcul_nbjsup = 'A' THEN
			//		   wsal01.japa  := wsal01.japa + nbj_anc + nbj_enf + nbj_deco;
			//	         wsal01.japec := wsal01.japec + nbj_con;
			//	     ELSE
			//	         wsal01.japec := wsal01.japec + nbj_con;
			//	     END IF;
			if ("O".equals(calcul_nbjsup))
			{
				agent.setJapec(agent.getJapec().add(new BigDecimal(nbj_con + nbj_anc + nbj_enf + nbj_deco)));
			}
			else if ("A".equals(calcul_nbjsup))
			{
				agent.setJapa(agent.getJapa().add(new BigDecimal(nbj_anc + nbj_enf + nbj_deco)));
				//agent.setJapec(agent.getJapec().add(new BigDecimal(nbj_anc + nbj_enf + nbj_deco)));
				agent.setJapec(agent.getJapec().add(new BigDecimal(nbj_con)));
			}
			else
			{
				//agent.setJapec(agent.getJapec().add(new BigDecimal(nbj_deco)));
				agent.setJapec(agent.getJapec().add(new BigDecimal(nbj_con)));
			}
			//
			//	     wsal01.nbjse := wsal01.nbjse + nbj_enf;
			//	     wsal01.nbjsa := wsal01.nbjsa + nbj_anc;
			//	     wsal01.nbjsm := wsal01.nbjsm + nbj_deco;
			agent.setNbjse(agent.getNbjse().add(new BigDecimal(nbj_enf)));
			agent.setNbjsa(agent.getNbjsa().add(new BigDecimal(nbj_anc)));
			agent.setNbjsm(agent.getNbjsm().add(new BigDecimal(nbj_deco)));

			ClsParameter.println(((myPeriode.getYear() != anniv.getYear()) && (myPeriode.getMonth() == anniv.getMonth())) + "-------Pour le salari� " + agent.getComp_id().getNmat() + ", Japa=" + agent.getJapa() + ", Japec=" + agent.getJapec());
		}
		//	  ELSE
		//	     wsal01.japec := wsal01.japec + nbj_con;
		//	  END IF;
		else
		{
			agent.setJapec(agent.getJapec().add(new BigDecimal(nbj_con)));
		}
		//
		//	  nbj_con := 0;
		nbj_con = 0;
		//
		//	  IF conges_annuels AND finconge THEN  -- conge
		//	     -- Si fin de conges annuels (conges_annuels etant force a TRUE
		//	     -- si aamm = pmcf pour fictif N ou fictif B).
		//
		if (conges_annuels && finconge)
		{
			ClsParameter.println("--------------Update,Cas d'un cong� annuel et fin de conge");
			//	     IF raz_jour = 'O' THEN
			//	        wsal01.japa  := 0;
			//	        wsal01.japec := 0;
			//	     END IF;
			if ("O".equals(raz_jour))
			{
				agent.setJapa(new BigDecimal(0));
				agent.setJapec(new BigDecimal(0));
			}
			//
			//	     wsal01.dapa  := 0;
			//	     wsal01.dded  := 0;
			//	     wsal01.ddcf  := NULL;
			//	     wsal01.dfcf  := NULL;
			//	     wsal01.nbjcf := 0;
			//	     wsal01.nbjaf := 0;
			//	     wsal01.jded  := 0;
			//	     wsal01.pmcf  := ' ';
			//
			//	     wsal01.nbjtr := w_nbjtr;
			//
			agent.setDapa(new BigDecimal(0));
			agent.setDded(new BigDecimal(0));
			agent.setDfcf(null);
			agent.setDdcf(null);
			agent.setNbjcf(new BigDecimal(0));
			agent.setNbjaf(new BigDecimal(0));
			agent.setJded(new BigDecimal(0));
			agent.setPmcf("");
			//
			agent.setNbjtr(new BigDecimal(w_nbjtr));
			//	     -- ajout uniquement si fictif
			//	     IF w_fictif = 'O' AND w_typ_fictif = 'A' THEN
			//	        wsal01.dapec := bas_con;
			//	        wsal01.nbjtr := w_nbjtr;
			//	     ELSE
			//	        wsal01.dapec := 0;
			//	        wsal01.nbjtr := 0;
			//	     END IF;
			if ("O".equals(w_fictif) && "A".equals(w_typ_fictif))
			{
				agent.setDapec(new BigDecimal(bas_con));
				agent.setNbjtr(new BigDecimal(w_nbjtr));
			}
			else
			{
				agent.setDapec(new BigDecimal(0));
				agent.setNbjtr(new BigDecimal(0));
			}
			//
			//	    IF ajout_bc = 'O' THEN
			//	       -- wsal01.dapec := wsal01.dapec + w_conge + wsal01.mtcf;
			//
			//	       -- MM MODIF REF : COMILOG-010226-87
			//	       -- Maj de la base conge = base cg dernier mois conge + montant total du cg
			//	       -- wsal01.dapec := wsal01.dapec + w_conge;
			//	       wsal01.dapec := wsal01.dapec + wsal01.mtcf + w_conge;
			//	       -- Fin modif REF : COMILOG-010226-87
			//
			//	    END IF;
			if ("O".equals(ajout_bc))
			{
				agent.setDapec(agent.getDapec().add(agent.getMtcf().add(new BigDecimal(w_conge))));
			}
			//
			//
			//	    wsal01.mtcf  := 0;
			agent.setMtcf(new BigDecimal(0));
		}
		//
		//	  ELSE
		else
		{
			//
			//	    IF conges_annuels AND pnp AND NOT ca THEN  -- Conges payes non pris uniquement
			//
			//	       wsal01.dapa  := 0;
			//	       wsal01.dded  := 0;
			//	       wsal01.jded  := 0;
			//
			//	       wsal01.nbjtr := w_nbjtr;
			//	       wsal01.dapec := bas_con;
			//
			//	       -- pc le 06.06.95
			//	       IF ajout_bc = 'O' THEN
			//	          wsal01.dapec := wsal01.dapec + w_conge;
			//	       END IF;
			//
			//	    ELSE
			//	       -- Cas normal
			//
			//	       -- MM 02-2004 Controle sur le nombre de jour maxi
			//	       IF ( NVL(wsal01.nbjtr,0) + NVL(w_nbjtr,0) ) < 999 THEN
			//	          wsal01.nbjtr := wsal01.nbjtr + w_nbjtr;
			//	       END IF;
			//
			//	       wsal01.dapec := wsal01.dapec + bas_con;
			//	       wsal01.dded  := wsal01.dded  + w_moncp;
			//
			//	       IF ajout_bc = 'O' THEN
			//	          wsal01.mtcf  := wsal01.mtcf  + w_conge;
			//	       END IF;
			//
			//	    END IF;
			if (conges_annuels && pnp && (!ca))
			{
				ClsParameter.println("Update,Cas cong� annuel, pnp true et ca false");
				agent.setDapa(new BigDecimal(0));
				agent.setDded(new BigDecimal(0));
				agent.setJded(new BigDecimal(0));
				//
				agent.setNbjtr(new BigDecimal(w_nbjtr));
				agent.setDapec(new BigDecimal(bas_con));
				//
				if ("O".equals(ajout_bc))
				{
					agent.setDapec(agent.getDapec().add(new BigDecimal(w_conge)));
				}
			}
			else
			{
				if (agent.getNbjtr().add(new BigDecimal(w_nbjtr)).intValue() < 999)
				{
					agent.setNbjtr(new BigDecimal(w_nbjtr).add(agent.getNbjtr()));
				}
				agent.setDapec(new BigDecimal(bas_con).add(agent.getDapec()));
				agent.setDded(new BigDecimal(w_moncp).add(agent.getDded()));
				//
				if ("O".equals(ajout_bc))
				{
					agent.setMtcf(new BigDecimal(w_conge).add(agent.getMtcf()));
				}
			}
			//
			//	  END IF;
		}
		//
		//	  UPDATE pasa01
		//	    SET
		//	      japa = wsal01.japa,
		//	      dapa = wsal01.dapa,
		//	      japec = wsal01.japec,
		//	      dapec = wsal01.dapec,
		//	      jded = wsal01.jded,
		//	      dded = wsal01.dded,
		//	      jrla = wsal01.jrla,
		//	      jrlec = wsal01.jrlec,
		//	      nbjcf = wsal01.nbjcf,
		//	      nbjaf = wsal01.nbjaf,
		//	      ddcf = wsal01.ddcf,
		//	      dfcf = wsal01.dfcf,
		//	      mtcf = wsal01.mtcf,
		//	      pmcf = wsal01.pmcf,
		//	      nbjse = wsal01.nbjse,
		//	      nbjsa = wsal01.nbjsa,
		//	      nbjsm = wsal01.nbjsm,
		//	      nmjf = wsal01.nmjf,
		//	      nbjtr = wsal01.nbjtr,
		//	      drtcg = wsal01.drtcg,
		//	      ddenv = wsal01.ddenv,
		//	      drenv = wsal01.drenv
		//	   WHERE cdos = wpdos.cdos AND nmat = wsal01.nmat;
		//
		ClsParameter.println("----------------------Update,Cas mise a jour base cong�");
		oSession.update(agent);
		//	   RETURN TRUE;
		return true;
		//
		//	END maj_bas;
	}

	//	---------------------------------------------------------------------------------
	//	-- Mise a actif NON des prets externes en cours
	//	---------------------------------------------------------------------------------
	//	FUNCTION sup_prets17 RETURN BOOLEAN
	//	IS
	//
	//	BEGIN
	//	  UPDATE paprent
	//	     SET pact = 'N'
	//	   WHERE cdos = wpdos.cdos
	//	     AND nmat = wsal01.nmat;
	//
	//	  RETURN TRUE;
	//
	//	END sup_prets17;

	//	---------------------------------------------------------------------------------
	//	-- Calcul du nombre de jours supplementaires
	//	---------------------------------------------------------------------------------
	private boolean calculateNbreJourSupplementaire(Rhpagent agent)
	{
		com.cdi.deltarh.service.ClsParameter.println(">>calculateNbreJourSupplementaire");
		try
		{
			//	FUNCTION calc_nbjsup RETURN BOOLEAN
			//	IS
			//
			//	 x_an        NUMBER(5);
			//	 x_mois      NUMBER(5);
			//	 wmois       NUMBER(5);
			//	 nbr_an      SMALLINT;
			//	 an_s        VARCHAR2(4);
			//	 mois_s      VARCHAR2(2);
			//	 nb_enf      SMALLINT;
			//	 nbj_par_enf SMALLINT;
			//	 l_tranche   SMALLINT;
			//
			//	 w_date_min  DATE;

			int x_an = 0;
			int x_mois = 0;
			int wmois = 0;
			int nbr_an = 0;
			String an_s = null;
			String mois_s = null;
			int nb_enf = 0;
			int nbj_par_enf = 0;
			int l_tranche = 0;

			Date w_date_min = null;
			//
			//	BEGIN
			//	   ------------------------------------------------------------------------------
			//	   -- CALCUL ANCIENNETE
			//	   ------------------------------------------------------------------------------
			//	   x_an   := TO_NUMBER(SUBSTR(w_aamm,1,4));
			//	   x_mois := TO_NUMBER(SUBSTR(w_aamm,5,2));
			//	   wmois  := x_an - TO_NUMBER(TO_CHAR(wsal01.ddca,'YYYY'));
			//	   wmois  := (wmois * 12) + x_mois;
			//	   wmois  := wmois - TO_NUMBER(TO_CHAR(wsal01.ddca,'mm'));
			//	   nbr_an := FLOOR(wmois / 12);
			//
			//	   ancien := nbr_an;
			//	   IF ancien < 0 THEN
			//	      ancien := 0;
			//	   END IF;
			ClsDate ddca = new ClsDate(agent.getDdca());
			x_an = myPeriode.getYear();
			x_mois =  myPeriode.getMonth();
			wmois = x_an - ddca.getYear();
			wmois = (wmois * 12) + x_mois;
			wmois = wmois - ddca.getMonth();
			nbr_an = new BigDecimal(Math.floor(wmois / 12)).intValue();
			//
			ancien = nbr_an;
			if(ancien < 0)
				ancien = 0;
			//
			//	   -- Calcul des jours supp selon la categorie et acquis tous les MNT3 annees
			//	   SELECT max(decode(nume,3,valm,0)),
			//	          max(decode(nume,1,valt,0)),
			//	          max(decode(nume,2,valt,0))
			//	     INTO wfnom.mnt3,wfnom.tau1,wfnom.tau2 FROM pafnom
			//	    WHERE cdos = wpdos.cdos
			//	      AND ctab = 35
			//	      AND cacc = wsal01.cat
			//	      AND nume in (1,2,3);
			Object[] maxdec = (Object[]) service.find(
					"select max(case nume when 3 then valm else 0 end)" + ", max(case nume when 1 then valt else 0 end)" + ", max(case nume when 2 then valt else 0 end)" + " from Rhfnom" + " where comp_id.cdos = '" + dossier + "'" + " and comp_id.ctab = 35" + " and comp_id.cacc = '"
							+ agent.getCat() + "'" + " and comp_id.nume in (1, 2, 3)").get(0);
			int mnt3 = 0;
			int tau1 = 0;
			int tau2 = 0;
			if (maxdec != null)
			{
				if (maxdec[0] != null)
					mnt3 = ((Long) maxdec[0]).intValue();
				if (maxdec[1] != null)
					tau1 = maxdec[1] instanceof BigDecimal ? ((BigDecimal) maxdec[1]).intValue() : ((Double) maxdec[1]).intValue();
				if (maxdec[2] != null)
					tau2 = maxdec[2] instanceof BigDecimal ? ((BigDecimal) maxdec[2]).intValue() : ((Double) maxdec[2]).intValue();
			}
			//
			//	   IF PA_PAIE.NouZ(wfnom.mnt3) THEN
			//	      wfnom.mnt3 := 1;
			//	   END IF;
			//	   IF PA_PAIE.NouZ(wfnom.tau1) THEN
			//	      wfnom.tau1 := 0;
			//	   END IF;
			//	   IF PA_PAIE.NouZ(wfnom.tau2) THEN
			//	      wfnom.tau2 := 0;
			//	   END IF;
			if (mnt3 <= 0)
				mnt3 = 1;
			if (tau1 <= 0)
				tau1 = 0;
			if (tau2 <= 0)
				tau2 = 0;
			ClsParameter.println("---------Agent=" + agent.getComp_id().getNmat() + ",Taux 1=" + tau1 + ", Tranche=" + l_tranche + ", tau2(nbj_par_enf)=" + tau2 + ", et mnt3=" + mnt3);
			//
			//	   l_tranche   := CEIL((ancien / wfnom.mnt3));
			//	   nbj_anc     := l_tranche * wfnom.tau1;
			//	   nbj_par_enf := wfnom.tau2;
			l_tranche = new BigDecimal(Math.ceil(ancien / mnt3)).intValue();
			nbj_anc = l_tranche * tau1;
			nbj_par_enf = tau2;
			//
			//	   -- Calcul du nombre de jours supp en fonction de l'anciennete
			//	   char2 := LTRIM(TO_CHAR(ancien,'09'));
			//	   BEGIN
			//	      SELECT valt INTO wfnom.tau2 FROM pafnom
			//	       WHERE cdos = wpdos.cdos
			//	         AND ctab = 34
			//	         AND cacc = char2
			//	         AND nume = 2;
			//	   EXCEPTION
			//	      WHEN NO_DATA_FOUND THEN null;
			//	   END;
			//
			//	   IF SQL%NOTFOUND OR PA_PAIE.NouZ(wfnom.tau2) THEN
			//	      wfnom.tau2 := 0;
			//	   END IF;
			String char2 = ClsStringUtil.formatNumber(ancien, "00");
			ClsParameter.println("----------->Deuxieme taux de la table 34 avec cl�=" + char2 + " initialement � " + ancien);
			Object obj = (this._getRhfnom(dossier, 34, char2, 2));
			tau2 = 0;
			if (obj != null)
			{
				tau2 = ((Rhfnom) obj).getValt().intValue();
			}
			//
			//	   nbj_anc := nbj_anc + wfnom.tau2;
			nbj_anc = nbj_anc + tau2;
			//
			//	   -- Calcul du nombre de jrs supp en fonction du nombre d'enfants
			//	   -- a charge, uniquement pour les femmes
			//	   IF wsal01.sexe = 'F' THEN
			//	      IF comptage_enfant THEN
			//
			//	         w_date_min := TO_DATE(
			//	                         TO_CHAR(sysdate,'DDMM') ||
			//	                         TO_CHAR(to_number(to_char(sysdate,'YYYY'))
			//	                                          - age_max_enfant,'0999')
			//	                       ,'DDMMYYYY');
			//
			//	         SELECT COUNT(*) INTO nb_enf FROM paenfan
			//	          WHERE cdos = wpdos.cdos
			//	            AND nmat = wsal01.nmat
			//	            AND NVL(achg,' ') = 'O'
			//	            AND dtna > w_date_min;
			//	      ELSE
			//	         nb_enf := wsal01.nbec;
			//	      END IF;
			//
			//	      -- Nb de jours sup a compter du Nieme enfant.
			//	      IF nbj_enf < minimum_enfant THEN
			//	         nbj_enf := 0;
			//	      ELSE
			//	         nbj_enf := nbj_par_enf * (nb_enf - (minimum_enfant - 1));
			//	      END IF;
			//	   END IF;
			if ("F".equals(agent.getSexe()))
			{
				if (comptage_enfant)
				{
					ClsDate sysdate = new ClsDate(new Date());
					//				 Calendar cal = Calendar.getInstance();
					//				 cal.set(sysdate.getYear() - age_max_enfant, sysdate.getMonth(), sysdate.getDay());
					String sdate = ClsStringUtil.formatNumber(sysdate.getDay(), "00") + "-" + ClsStringUtil.formatNumber(sysdate.getMonth(), "00") + "-" + ClsStringUtil.formatNumber((sysdate.getYear() - age_max_enfant), "0000");
					com.cdi.deltarh.service.ClsParameter.println("....sdate :" + sdate);
					w_date_min = new ClsDate(sdate, "dd-MM-yyyy").getDate();
					//
					obj = service.find("select count(*) from Rhtenfantagent" + " where comp_id.cdos = '" + dossier + "'" + " and comp_id.nmat = '" + agent.getComp_id().getNmat() + "'" + " and achg = 'O'" + " and dtna > '" + new ClsDate(sdate, this.dateformat).getDateS() + "'").get(0);
					if (obj != null)
					{
						nb_enf = ((Long) obj).intValue();
					}
				}
				else
				{
					nb_enf = agent.getNbec();
				}
				//
				if (nbj_enf < minimum_enfant)
				{
					nbj_enf = 0;
				}
				else
				{
					nbj_enf = nbj_par_enf * (nb_enf - (minimum_enfant - 1));
				}
			}
			//
			//	   -- Calcul du nombre de jrs supp en fonction des decorations
			//	   nbj_deco := 0;
			//	   SELECT SUM(valt) INTO nbj_deco from pafnom
			//	    WHERE cdos = wpdos.cdos
			//	      AND ctab = 18
			//	      AND nume = 1
			//	      AND cacc IN (SELECT UNIQUE cdis FROM padistin
			//	                    WHERE cdos = wpdos.cdos
			//	                      AND nmat = wsal01.nmat);
			//	   IF PA_PAIE.NouZ(nbj_deco) THEN
			//	      nbj_deco := 0;
			//	   END IF;
			nbj_deco = 0;
			obj = service.find(
					"select sum(valt) from Rhfnom" + " where comp_id.cdos = '" + dossier + "'" + " and comp_id.ctab = 18" + " and comp_id.nume = 1" + " and comp_id.cacc in( select distinct cdis from Rhtdistinctionagent" + " where comp_id.cdos = '" + dossier + "'" + " and comp_id.nmat = '"
							+ agent.getComp_id().getNmat() + "')").get(0);
			if (obj != null)
			{
				nbj_deco = obj instanceof BigDecimal ? ((BigDecimal) obj).intValue() : ((Double) obj).intValue();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		//
		//	   RETURN TRUE;
		return true;
		//
		//	END calc_nbjsup;
	}

	//	---------------------------------------------------------------------------------
	//	-- Calcul du proratas sur les jours de droits acquis sur le mois
	//	---------------------------------------------------------------------------------
	private boolean calculProrataNbreJourDroitParMois()
	{
		com.cdi.deltarh.service.ClsParameter.println(">>calculProrataNbreJourDroitParMois");
		//	FUNCTION calc_pror_jcg RETURN BOOLEAN
		//	IS
		//
		//	BEGIN
		//
		//
		//	   IF pror_jcg_normal THEN
		if (pror_jcg_normal)
		{
			//
			//	     ----- pc le 27.04.95, prorata sur nbre de jours acquis si le salarie
			//	     ----- ne travaille pas 30 jours
			//	     IF w_fictif = 'O' AND w_typ_fictif = 'A' THEN
			if ("O".equals(w_fictif) && "A".equals(w_typ_fictif))
			{
				//	        IF pror_jr = 'O' THEN
				//	           nbj_con := ( nbj_con * w_nbjtr ) / 30;
				//	        END IF;
				if ("O".equals(pror_jr))
					nbj_con = (nbj_con * w_nbjtr) / 30;
			}
			//	     ELSE
			else
			{
				//	        IF w_aamm = sav_pmcf THEN
				//	           IF sav_dfcf >= LAST_DAY(TO_DATE(w_aamm,'YYYYMM')) THEN
				//	              nbj_con := 0;
				//	           ELSE
				//	              nbj_con := nbj_con * (30 - TO_NUMBER(TO_CHAR(sav_dfcf,'DD'))) / 30;
				//	           END IF;
				//	        ELSE
				//	           IF pror_jr = 'O' THEN
				//	              nbj_con := ( nbj_con * w_nbjtr ) / 30;
				//	           END IF;
				//	        END IF;
				if (periode.equals(sav_pmcf))
				{
					if (sav_dfcf.compareTo(myPeriode.getDate())>=0)
						nbj_con = 0;
					else
						nbj_con = nbj_con * (30 - new ClsDate(sav_dfcf).getDay()) / 30;
				}
				else
				{
					if ("O".equals(pror_jr))
						nbj_con = (nbj_con * w_nbjtr) / 30;
				}
			}
			//	     END IF;
		}
		//
		//	  ELSE
		else
		{
			//	     -- prorata en fonction bareme de jours de presence
			//	     IF w_nbjtr >= dbcg_barem1 and w_nbjtr <= fincg_barem1 THEN
			//	        nbj_con := nbj_con * nbjcg_barem1 / 100;
			//	     ELSIF w_nbjtr >= dbcg_barem2 and w_nbjtr <= fincg_barem2 THEN
			//	        nbj_con := nbj_con * nbjcg_barem2 / 100;
			//	     ELSIF w_nbjtr >= dbcg_barem3 and w_nbjtr <= fincg_barem3 THEN
			//	        nbj_con := nbj_con * nbjcg_barem3 / 100;
			//	     ELSE
			//	        nbj_con := 0;
			//	     END IF;
			int i1 = Integer.valueOf(dbcg_barem1);
			int i2 = Integer.valueOf(fincg_barem1);
			int i3 = Integer.valueOf(dbcg_barem2);
			int i4 = Integer.valueOf(fincg_barem2);
			int i5 = Integer.valueOf(dbcg_barem3);
			int i6 = Integer.valueOf(fincg_barem3);
			if (w_nbjtr >= i1 && w_nbjtr <= i2)
				nbj_con = nbj_con * nbjcg_barem1 / 100;
			else if (w_nbjtr >= i3 && w_nbjtr <= i4)
				nbj_con = nbj_con * nbjcg_barem2 / 100;
			else if (w_nbjtr >= i5 && w_nbjtr <= i6)
				nbj_con = nbj_con * nbjcg_barem3 / 100;
			else
				nbj_con = 0;
			//
		}
		//	  END IF;
		//
		//	  RETURN TRUE;
		return true;
		//
		//	END calc_pror_jcg;

	}

	//	---------------------------------------------------------------------------------
	//	-- Suppression des EVs du mois courant
	//	---------------------------------------------------------------------------------
	private boolean deleteEVMois(Rhpagent agent)
	{
		com.cdi.deltarh.service.ClsParameter.println(">>deleteEVMois");
		try
		{
			//	FUNCTION sup_eve RETURN BOOLEAN
			//	IS
			//
			//	BEGIN
			//	  DELETE FROM paevar
			//	   WHERE cdos = wpdos.cdos
			//	     AND nmat = wsal01.nmat
			//	     AND aamm = w_aamm
			//	     AND nbul = w_nbul;
			oSession.createQuery("delete from Rhteltvardet" + " where cdos = '" + dossier + "'" + " and nmat = '" + agent.getComp_id().getNmat() + "'" + " and aamm = '" + periode + "'" + " and nbul = " + numerobulletin).executeUpdate();
			//		Object o = service.get(Rhteltvardet.class, new RhteltvardetPK(dossier, agent.getComp_id().getNmat(), numerobulletin, 0));
			//		if(o != null)
			//			oSession.delete(o);
			//
			//	  DELETE FROM paevcg
			//	   WHERE cdos = wpdos.cdos
			//	     AND nmat = wsal01.nmat
			//	     AND aamm = w_aamm
			//	     AND nbul = w_nbul;
			oSession.createQuery("delete from Rhteltvarconge" + " where cdos = '" + dossier + "'" + " and nmat = '" + agent.getComp_id().getNmat() + "'" + " and aamm = '" + periode + "'" + " and nbul = " + numerobulletin).executeUpdate();
			//
			//	  DELETE FROM paev
			//	   WHERE cdos = wpdos.cdos
			//	     AND nmat = wsal01.nmat
			//	     AND aamm = w_aamm
			//	     AND nbul = w_nbul;
			//		o = service.get(Rhteltvarent.class, new RhteltvarentPK(dossier, periode, agent.getComp_id().getNmat(), numerobulletin));
			oSession.createQuery("delete from Rhteltvarent" + " where cdos = '" + dossier + "'" + " and nmat = '" + agent.getComp_id().getNmat() + "'" + " and aamm = '" + periode + "'" + " and nbul = " + numerobulletin).executeUpdate();
			//		if(o != null)
			//			service.delete(o);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		//
		//	  RETURN TRUE;
		return true;
		//
		//	END sup_eve;
	}

	//	---------------------------------------------------------------------------------
	//	-- Suppression des elements fixes
	//	---------------------------------------------------------------------------------
	private boolean deleteEFMois(Rhpagent agent)
	{
		com.cdi.deltarh.service.ClsParameter.println(">>deleteEFMois");
		//	FUNCTION sup_paelfix RETURN BOOLEAN
		//	IS
		//

		//	BEGIN
		//	   BEGIN
		//	      DELETE FROM paelfix
		//	       WHERE cdos = wpdos.cdos
		//	         AND nmat = wsal01.nmat
		//	         AND dfin IS NOT NULL
		//	         AND dfin <= TO_DATE(w_aamm,'YYYYMM');
		//	   EXCEPTION
		//	      WHEN OTHERS THEN null;
		//	   END;
		try
		{
			oSession.createQuery("delete from Rhteltfixagent" + " where cdos = '" + dossier + "'" + " and nmat = '" + agent.getComp_id().getNmat() + "'" + " and dfin is not null" + " and dfin <= '" + myPeriode.getDateS(dateformat) + "'").executeUpdate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		//	   RETURN TRUE;
		return true;
		//
		//	END sup_paelfix;
	}

	//	---------------------------------------------------------------------------------
	//	--     MISE A JOUR DES ZONES CONCERNANT LES CONGES
	//	---------------------------------------------------------------------------------
	private boolean updateZoneConge(Rhpagent agent)
	{
		com.cdi.deltarh.service.ClsParameter.println(">>updateZoneConge");
		//	FUNCTION maj_con RETURN BOOLEAN
		//	IS
		//
		//	  i SMALLINT;
		//
		//	  BEGIN
		//
		DetailEv ev = null;
		//	  FOR i IN 1..nb_evcg LOOP
		for (Object obj : listOfEltVarConge)
		{
			ClsParameter.println("Parcours de la liste des conges de l'agent");
			ev = (DetailEv) obj;
			//	    -- Absences, on ne s'occupe pas de la date
			if (ev.mnt1 == 0)
			{
				//	    IF tab_motf_mnt1(i) = 0 THEN
				//
				//	       -- MM 07-2004 pris en compte des reliquats
				//		 	IF tab_evcg_motf(i) = w_motif_reliq THEN
				//	          w_retour := maj_reliq(i);
				//	       END IF;
				if (w_motif_reliq.equals(ev.motf))
					w_retour = updateJourDesJourReliquat(agent, ev);
				//
				//	       IF tab_evcg_motf(i) != w_cg_abs THEN
				//	          w_retour := maj_det(i);
				//	       END IF;
				if (!w_cg_abs.equals(ev.motf))
					w_retour = updateDetailAbsConge(ev, agent.getComp_id().getNmat());
			}
			//	    ELSE
			else
			{
				//	    -- Conges
				//	      IF tab_motf_mnt3(i) = 0    THEN -- Conges annuels
				//	         conges_annuels := TRUE;
				//	      ELSIF tab_motf_mnt3(i) = 1 THEN -- Conges ponctuels
				//	         conges_annuels := FALSE;
				//	      END IF;
				if (ev.mnt3 == 0)
					conges_annuels = true;
				else if (ev.mnt3 == 1)
					conges_annuels = false;
				//
				//	      ---------------------------------------------------------------
				//	      -- Mise a jour de l'historique CONGES et des jours de DROITS.
				//	      ---------------------------------------------------------------
				//	      -- Dans le cas du fictif a Non ou de la methode B :
				//	      --    * mise a jour de l'historique uniquement le mois de
				//	      --      paiement du conges pour l'ensemble du conges.
				//	      --    * mise a jour des droits uniquement le mois de
				//	      --      paiement du conges pour l'ensemble du conges.
				//	      --
				//	      -- Dans le cas du fictif a Oui / Methode A :
				//	      --    * mise a jour de l'histo et des jours de droits chaque
				//	      --      mois au cours des clotures successives.
				//	      ---------------------------------------------------------------
				//	      IF w_fictif = 'N' OR (w_fictif = 'O' AND w_typ_fictif = 'B') THEN
				if ("N".equals(w_fictif) || ("O".equals(w_fictif) && "B".equals(w_fictif)))
				{
					//	         IF wsal01.pmcf = w_aamm THEN
					//	            -- Tout est enregistre dans l'histo des mvts
					//	            w_retour := maj_det(i);
					//
					//	            -- Les jours sont tous defalques pour etre en phase avec les droits
					//	            IF conges_annuels THEN
					//	               w_retour := maj_conges_annuels(i);
					//	               -- forcage a TRUE de finconge pour maj de la base
					//	               finconge := TRUE;
					//	            ELSE
					//	               w_retour := maj_cp(i);
					//	            END IF;
					//	         END IF;
					if (periode.equals(agent.getPmcf()))
					{
						w_retour = updateDetailAbsConge(ev, agent.getComp_id().getNmat());
						//			            -- Les jours sont tous defalques pour etre en phase avec les droits
						if (conges_annuels)
						{
							w_retour = updateCongeAnnuel(ev, agent);
							finconge = true;
						}
						else
						{
							w_retour = updateCongePayePonctuel(ev, agent);
						}
					}
				}
				//	      ELSE
				else
				{
					//	         -- Fictif A
					//	         IF tab_evcg_dfin(i) <= tab_evcg_dfpa(i) THEN
					//	            -- On enregistre que le mvt du mois
					//	            w_retour := maj_det(i);
					//
					//	            -- Les jours sont defalques pour ce mois
					//	            IF conges_annuels THEN
					//	               w_retour := maj_conges_annuels(i);
					//	            ELSE
					//	               w_retour := maj_cp(i);
					//	            END IF;
					//
					//	            IF NOT finconge THEN
					//	               IF tab_evcg_dfin(i) = wsal01.dfcf AND conges_annuels THEN
					//	                  finconge := TRUE;
					//	               END IF;
					//	            END IF;
					//
					//	         END IF;
					if (ev.dfin.compareTo(ev.dfpa) <= 0)
					{
						w_retour = updateDetailAbsConge(ev, agent.getComp_id().getNmat());
						//			            -- Les jours sont tous defalques pour etre en phase avec les droits
						if (conges_annuels)
						{
							w_retour = updateCongeAnnuel(ev, agent);
						}
						else
						{
							w_retour = updateCongePayePonctuel(ev, agent);
						}
						//
						if (!finconge)
						{
							if (ev.dfin.equals(agent.getDfcf()) && conges_annuels)
								finconge = true;
						}
					}
					//	      END IF;
				}
				//	    END IF;
			}
			//	  END LOOP;
		}
		//
		//	  RETURN TRUE;
		return true;
		//
		//	END maj_con;
	}

	//	---------------------------------------------------------------------------------
	//	-- Mise a jour des jours de reliquat
	//	---------------------------------------------------------------------------------
	private boolean updateJourDesJourReliquat(Rhpagent agent, DetailEv ev)
	{
		com.cdi.deltarh.service.ClsParameter.println(">>updateJourDesJourReliquat");
		//	FUNCTION maj_reliq(i IN SMALLINT) RETURN BOOLEAN
		//	IS
		//
		//	   w_nbjc		NUMBER(5,2);
		//
		//	BEGIN
		//	   -- les reliquats sont en jours absence il faut calculer en jrs cg
		//
		//	   SELECT count(*) INTO w_nbjc FROM pacal
		//	    WHERE cdos = wpdos.cdos
		//	      AND jour between tab_evcg_ddeb(i) AND tab_evcg_dfin(i)
		//	      AND ouvr = 'O'
		//	      AND fer = 'N';
		Object o = service.find("select count(*) from Rhpcalendrier" + " where comp_id.cdos = '" + dossier + "'" + " and comp_id.jour between " + new ClsDate(ev.ddeb).getDateS(dateformat) + " and " + new ClsDate(ev.dfin).getDateS(dateformat)).get(0);
		//
		//
		//	   wsal01.jrla := NVL(wsal01.jrla,0) - w_nbjc;
		if (o != null)
		{
			if (agent.getJrla() != null)
				agent.setJrla(new BigDecimal(-1 * (Long) o).add(agent.getJrla()));
			else
				agent.setJrla(new BigDecimal(-1 * (Long) o));
		}
		//
		//	   RETURN TRUE;
		return true;
		//
		//	END maj_reliq;
	}

	//	---------------------------------------------------------------------------------
	//	-- Mise a jour du detail des conges et absences
	//	---------------------------------------------------------------------------------
	private boolean updateDetailAbsConge(DetailEv ev, String nmat)
	{
		com.cdi.deltarh.service.ClsParameter.println(">>updateDetailAbsConge");
		//	FUNCTION maj_det(i IN SMALLINT) RETURN BOOLEAN
		//	IS
		//
		//	BEGIN
		//	   wconge.cdos := wpdos.cdos;
		//	   wconge.nmat := wsal01.nmat;
		//	   wconge.ddcg := tab_evcg_ddeb(i);
		//	   wconge.dfcg := tab_evcg_dfin(i);
		//	   wconge.nbja := tab_evcg_nbja(i);
		//	   wconge.nbjc := tab_evcg_nbjc(i);
		//	   wconge.cmcg := tab_evcg_motf(i);
		//	   wconge.mtcg := tab_evcg_mont(i);
		//
		//	   -- MM 30/09/2004 Pb plantage si mvt existe deja
		//	   BEGIN
		//	       INSERT INTO paconge
		//	              VALUES (wconge.cdos,wconge.nmat,wconge.ddcg,wconge.dfcg,
		//	                      wconge.nbja,wconge.nbjc,wconge.cmcg,wconge.mtcg);
		//	   EXCEPTION
		//	      WHEN DUP_VAL_ON_INDEX THEN null;
		//	   END;
		Rhtcongeagent conge = new Rhtcongeagent(new RhtcongeagentPK(dossier, nmat, ev.ddeb));
		//
		conge.setCmcg(ev.motf);
		conge.setDfcg(ev.dfin);
		conge.setMtcg(new BigDecimal(ev.mont));
		conge.setNbja(new BigDecimal(ev.nbja));
		conge.setNbjc(new BigDecimal(ev.nbjc));
		//
		service.saveOrUpdate(conge);
		//
		//	   RETURN TRUE;
		return true;
		//
		//	END maj_det;
	}

	//	---------------------------------------------------------------------------------
	//	-- Conges payes ponctuels
	//	-- Les jours de conges PONCTUELS sont deduits des jours a prendre
	//	--     et stockes dans la zone des jours deductibles.
	//	---------------------------------------------------------------------------------
	private boolean updateCongePayePonctuel(DetailEv ev, Rhpagent agent)
	{
		com.cdi.deltarh.service.ClsParameter.println(">>updateCongePayePonctuel");
		//
		//	FUNCTION maj_cp(i IN SMALLINT) RETURN BOOLEAN
		//	IS
		//
		//	  wnbjcad   SMALLINT;
		int wnbjcad = 0;
		//
		//	BEGIN
		//	-- ----- Decompte en jours Ouvrables ou Calendaires
		//	  IF wpdos.dccg = 'O' THEN
		//	     wnbjcad := tab_evcg_nbjc(i);
		//	  ELSE
		//	     wnbjcad := tab_evcg_nbja(i);
		//	  END IF;
		if ("O".equals(rhpdo.getDccg()))
			wnbjcad = ev.nbjc;
		else
			wnbjcad = ev.nbja;
		//
		//	  wsal01.jded := wsal01.jded + wnbjcad;
		if (agent.getJded() != null)
			agent.setJded(agent.getJded().add(new BigDecimal(wnbjcad)));
		else
			agent.setJded(new BigDecimal(wnbjcad));
		//
		//	  IF wnbjcad  <= wsal01.japa THEN
		//	     wsal01.japa := wsal01.japa - wnbjcad;
		//	  ELSE
		//	     wsal01.japec := wsal01.japec - ( wnbjcad - wsal01.japa );
		//	     wsal01.japa  := 0;
		//	  END IF;
		if (wnbjcad <= agent.getJapa().intValue())
		{
			if (agent.getJapa() != null)
				agent.setJapa(agent.getJapa().subtract(new BigDecimal(wnbjcad)));
			else
				agent.setJapa(new BigDecimal(-1 * wnbjcad));
		}
		else
		{
			int i = agent.getJapa().intValue();
			if (agent.getJapec() != null)
				agent.setJapec(agent.getJapec().subtract(new BigDecimal(wnbjcad - i)));
			else
				agent.setJapec(new BigDecimal(wnbjcad - i));
			//
			agent.setJapa(new BigDecimal(0));
		}
		//
		//	  RETURN TRUE;
		return true;
		//
		//	END maj_cp;
	}

	//	---------------------------------------------------------------------------------
	//	-- Calcul du nombre de part fiscal automatiquement et maj de la fiche sal.
	//	-- en fonction de age maxi
	//	---------------------------------------------------------------------------------
	private boolean calculPartFiscale(Rhpagent agent)
	{
		com.cdi.deltarh.service.ClsParameter.println(">>calculPartFiscale");
		//	FUNCTION calc_partfisc RETURN BOOLEAN
		//	IS
		//
		//	 w_tau1 	PAFNOM.VALT%TYPE;
		//	 w_tau2 	PAFNOM.VALT%TYPE;
		//	 w_tau3 	PAFNOM.VALT%TYPE;
		//	 w_tau4 	PAFNOM.VALT%TYPE;
		//	 w_tau5 	PAFNOM.VALT%TYPE;
		//	 w_tau6 	PAFNOM.VALT%TYPE;
		//	 w_tau7 	PAFNOM.VALT%TYPE;
		//	 w_tau8 	PAFNOM.VALT%TYPE;

		//
		//	 w_curdate  DATE;
		//	 w_dtna	DATE;
		//	 enfant_a_charge 	NUMBER;
		//	 w_age_enfant	NUMBER;
		//	 w_nbpt		NUMBER;
		//
		//	 CURSOR C_enfant IS
		//	    SELECT dtna FROM paenfan
		//	     WHERE cdos = wpdos.cdos
		//	       AND nmat = wsal01.nmat;

		List l = service.find("select dtna from Rhtenfantagent" + " where comp_id.cdos = '" + dossier + "'" + " and comp_id.nmat = '" + agent.getComp_id().getNmat() + "'");
		//
		//
		//	BEGIN
		//
		//	   enfant_a_charge := 0;
		//	   w_curdate := LAST_DAY(TO_DATE(w_aamm,'YYYYMM'));
		//	   OPEN C_enfant;
		//	   LOOP
		//	      FETCH C_enfant INTO w_dtna;
		//	      EXIT WHEN C_enfant%NOTFOUND;
		//
		//		w_age_enfant := FLOOR( ( w_curdate - w_dtna ) / 365 );
		//
		//	      IF w_age_enfant <= age_max_fiscal THEN
		//	         enfant_a_charge := enfant_a_charge + 1;
		//	      END IF;
		//
		//	   END LOOP;
		//	   CLOSE C_enfant;
		int enfant_a_charge = 0;
		Date w_curdate = myPeriode.getLastDayOfMonth();
		Date w_dtna = null;
		long w_age_enfant = 0;
		for (Object obj : l)
		{
			w_dtna = (Date) obj;
			w_age_enfant = ClsDate.getNumberOfDay(w_curdate, w_dtna) / 365;
			if (w_age_enfant <= age_max_fiscal)
				enfant_a_charge++;
		}
		//
		//	   w_tau1 := 0;
		//	   w_tau2 := 0;
		//	   w_tau3 := 0;
		//	   w_tau4 := 0;
		//	   w_tau5 := 0;
		//	   w_tau6 := 0;
		//	   w_tau7 := 0;
		//	   w_tau8 := 0;
		Double[] taux = new Double[]{null,null,null,null,null,null,null,null};
		//
		//	   IF NOT PA_PAIE.NouB(wsal01.sitf) THEN
		//	      -- Lecture de la table 55
		//	      BEGIN
		//	         SELECT SUM(DECODE(nume,1,NVL(valt,0),0)),
		//	                SUM(DECODE(nume,2,NVL(valt,0),0)),
		//	                SUM(DECODE(nume,3,NVL(valt,0),0)),
		//	                SUM(DECODE(nume,4,NVL(valt,0),0)),
		//	                SUM(DECODE(nume,5,NVL(valt,0),0)),
		//	                SUM(DECODE(nume,6,NVL(valt,0),0)),
		//	                SUM(DECODE(nume,7,NVL(valt,0),0)),
		//	                SUM(DECODE(nume,8,NVL(valt,0),0))
		//	           INTO w_tau1, w_tau2, w_tau3, w_tau4, w_tau5, w_tau6, w_tau7, w_tau8
		//	           FROM pafnom
		//	          WHERE cdos = wpdos.cdos
		//	            AND ctab = 55
		//	            AND cacc = wsal01.sitf
		//	            AND nume IN (1,2,3,4,5,6,7,8);
		//	      EXCEPTION
		//	         WHEN NO_DATA_FOUND THEN
		//	            RETURN FALSE;
		//	     END;
		Object[] maxdec = (Object[]) service.find(
				"select " + "max(case nume when 1 then valt else 0 end)" + ", max(case nume when 2 then valt else 0 end)" + ", max(case nume when 3 then valt else 0 end)" + ", max(case nume when 4 then valt else 0 end)" + ", max(case nume when 5 then valt else 0 end)"
						+ ", max(case nume when 6 then valt else 0 end)" + ", max(case nume when 7 then valt else 0 end)" + ", max(case nume when 8 then valt else 0 end)" + " from Rhfnom" + " where comp_id.cdos = '" + dossier + "'" + " and comp_id.ctab = 55" + " and comp_id.cacc = '"
						+ agent.getSitf() + "'" + " and comp_id.nume in (1, 2, 3, 4, 5, 6, 7, 8)").get(0);
		for (int i = 0; i < 8; i++)
		{
			taux[i] = maxdec[i] instanceof BigDecimal ? ((BigDecimal) maxdec[i]).doubleValue() : (Double) maxdec[i];
		}
		//
		//	     -- Initialisation du nbre de part � 1 pour le salari� lui m�me
		//	     w_nbpt := 1;
		//	     -- Ajout du nombre de part concernant le conjoint
		//	     w_nbpt := w_nbpt + NVL(w_tau1,0);
		//	     -- Ajout du nombre de part concernant les enfants
		//	     IF NVL(enfant_a_charge,0) >= 1 THEN
		//	        w_nbpt := w_nbpt + NVL(w_tau2,0);
		//	        IF NVL(enfant_a_charge,0) >= 2 THEN
		//	           w_nbpt := w_nbpt + NVL(w_tau3,0);
		//	           IF NVL(enfant_a_charge,0) >= 3 THEN
		//	              w_nbpt := w_nbpt + NVL(w_tau4,0);
		//	              IF NVL(enfant_a_charge,0) >= 4 THEN
		//	                 w_nbpt := w_nbpt + NVL(w_tau5,0);
		//	                 IF NVL(enfant_a_charge,0) >= 5 THEN
		//	                    w_nbpt := w_nbpt + NVL(w_tau6,0);
		//	                    IF NVL(enfant_a_charge,0) >= 6 THEN
		//	                       w_nbpt := w_nbpt + NVL(w_tau7,0);
		//	                       IF NVL(enfant_a_charge,0) >= 7 THEN
		//	                          w_nbpt := w_nbpt + NVL(w_tau8,0);
		//	                       END IF;
		//	                    END IF;
		//	                 END IF;
		//	              END IF;
		//	           END IF;
		//	        END IF;
		//	     END IF;
		//	   END IF;
		double w_nbpt = 1;
		w_nbpt += taux[0];
		if (enfant_a_charge >= 1)
		{
			w_nbpt += taux[1];
			if (enfant_a_charge >= 2)
			{
				w_nbpt += taux[2];
				if (enfant_a_charge >= 3)
				{
					w_nbpt += taux[3];
					if (enfant_a_charge >= 4)
					{
						w_nbpt += taux[4];
						if (enfant_a_charge >= 5)
						{
							w_nbpt += taux[5];
							if (enfant_a_charge >= 6)
							{
								w_nbpt += taux[6];
								if (enfant_a_charge >= 7)
								{
									w_nbpt += taux[7];
								}
							}
						}
					}
				}
			}
		}
		//
		//	   IF w_nbpt > max_part_fisc AND max_part_fisc != 0 THEN
		//	      w_nbpt := max_part_fisc;
		//	   END IF;
		if (w_nbpt > max_part_fisc && max_part_fisc != 0)
			w_nbpt = max_part_fisc;
		//
		//	   IF w_nbpt != NVL(wsal01.nbpt,0) THEN
		//	      wsal01.nbpt := w_nbpt;
		//	      UPDATE pasa01 SET nbpt = wsal01.nbpt
		//	       WHERE cdos = wpdos.cdos AND nmat = wsal01.nmat;
		//	   END IF;
		
		if (w_nbpt != agent.getNbpt().intValue())
		{
			agent.setNbpt(new BigDecimal(w_nbpt));
			//
			ClsParameter.println("----------------------Update,Cas calcul part fiscal");
			oSession.update(agent);
		}
		//
		//	   RETURN TRUE;
		return true;
		//
		//	END calc_partfisc;
	}

	class EltVarMCMS
	{
		String periode = "";

		Date ddebut = null;

		Date dfin = null;

	}

	class DetailEv
	{
		Date ddpa = null;

		Date dfpa = null;

		char bcmo;

		Date ddeb = null;

		Date dfin = null;

		int nbjc = 0;

		int nbja = 0;

		String motf = "";

		double mont = 0;

		int mnt1 = 0;

		int mnt3 = 0;
	}

	public void agentUpdateToString()
	{
		ClsParameter.println("------------------------------" + agent.getComp_id().getNmat() + " " + agent.getNom() + " " + agent.getPren() + "-------------------------------------------------------------------------------");
		ClsParameter.println("String dossier = " + dossier);
		ClsParameter.println("int numerobulletin = " + numerobulletin);
		ClsParameter.println("String periode = " + periode);
		ClsParameter.println("String user = " + user);
		ClsParameter.println("String langue = " + langue);
		ClsParameter.println("ClsBulletin bulletin = " + bulletin.toString());
		ClsParameter.println("ClsParameterOfPay parameter = " + parameter.toString());
		ClsParameter.println("ClsDate myPeriode = " + myPeriode.getDateS());
		ClsParameter.println("String rubriqueNbreJourTravail = " + rubriqueNbreJourTravail);
		ClsParameter.println("String rubriqueNbreJourPlage = " + rubriqueNbreJourPlage);
		ClsParameter.println("boolean tab91 = " + tab91);
		ClsParameter.println("boolean tab91b = " + tab91b);
		ClsParameter.println("String moisPaieCourant = " + moisPaieCourant);
		ClsParameter.println("String mois_ms = " + mois_ms);
		ClsParameter.println("int nbul_ms = " + nbul_ms);
		ClsParameter.println("String w_am99 = " + w_am99);
		ClsParameter.println("boolean calc_part_auto = " + calc_part_auto);
		ClsParameter.println("int max_part_fisc = " + max_part_fisc);
		ClsParameter.println("String w_motif_reliq = " + w_motif_reliq);
		ClsParameter.println("String rub_pnp = " + rub_pnp);
		ClsParameter.println("String val_exp = " + val_exp);
		ClsParameter.println("int typ_rec = " + typ_rec);
		ClsParameter.println("String w_fictif = " + w_fictif);
		ClsParameter.println("String w_typ_fictif = " + w_typ_fictif);
		ClsParameter.println("int nbjc_lx = " + nbjc_lx);
		ClsParameter.println("int nbjc_ex = " + nbjc_ex);
		ClsParameter.println("String[]t_ev_mc = " + t_ev_mc[0] + "-" + t_ev_mc[1] + "-" + t_ev_mc[2] + "-" + t_ev_mc[3] + "-" + t_ev_mc[4] + "-" + t_ev_mc[5] + "-" + t_ev_mc[6]);
		ClsParameter.println("String[]t_ev_ms = " + t_ev_ms[0] + "-" + t_ev_ms[1] + "-" + t_ev_ms[2] + "-" + t_ev_ms[3] + "-" + t_ev_ms[4] + "-" + t_ev_ms[5] + "-" + t_ev_ms[6]);
		ClsParameter.println("String rub_bc = " + rub_bc);
		ClsParameter.println("String calcul_nbjsup = " + calcul_nbjsup);
		ClsParameter.println("String ajout_bc = " + ajout_bc);
		ClsParameter.println("String pror_jr = " + pror_jr);
		ClsParameter.println("String w_cg_abs = " + w_cg_abs);
		ClsParameter.println("String raz_jour = " + raz_jour);
		ClsParameter.println("boolean pror_jcg_normal = " + pror_jcg_normal);
		ClsParameter.println("int age_max_enfant = " + age_max_enfant);
		ClsParameter.println("boolean comptage_enfant = " + comptage_enfant);
		ClsParameter.println("String dbcg_barem1 = " + dbcg_barem1);
		ClsParameter.println("String fincg_barem1 = " + fincg_barem1);
		ClsParameter.println("int nbjcg_barem1 = " + nbjcg_barem1);
		ClsParameter.println("String dbcg_barem2 = " + dbcg_barem2);
		ClsParameter.println("String fincg_barem2 = " + fincg_barem2);
		ClsParameter.println("int nbjcg_barem2 = " + nbjcg_barem2);
		ClsParameter.println("String dbcg_barem3 = " + dbcg_barem3);
		ClsParameter.println("String fincg_barem3 = " + fincg_barem3);
		ClsParameter.println("int nbjcg_barem3 = " + nbjcg_barem3);
		ClsParameter.println("int w_type_dtanniv = " + w_type_dtanniv);
		ClsParameter.println("boolean t91hc = " + t91hc);
		ClsParameter.println("boolean t91hp = " + t91hp);
		ClsParameter.println("boolean t91mc =  " + t91mc);
		ClsParameter.println("boolean t91mp = " + t91mp);
		ClsParameter.println("boolean t91in = " + t91mp);
		ClsParameter.println("String prochainmois = " + prochainmois);
		ClsParameter.println("double bas_con = " + bas_con);
		ClsParameter.println("double w_moncp = " + w_moncp);
		ClsParameter.println("int w_nbjcp = " + w_nbjcp);
		ClsParameter.println("double w_conge = " + w_conge);
		ClsParameter.println("int w_nbjtr = " + w_nbjtr);
		ClsParameter.println("boolean cgan_ms = " + cgan_ms);
		ClsParameter.println("boolean exist_evcg = " + exist_evcg);
		ClsParameter.println("int w_nbjreliq = " + w_nbjreliq);
		ClsParameter.println("boolean mois_conge = " + mois_conge);
		ClsParameter.println("boolean sal_RA_MU = " + sal_RA_MU);
		ClsParameter.println("boolean sal_RA_MU_depuis = " + sal_RA_MU_depuis);
		ClsParameter.println("boolean w_continue = " + w_continue);
		ClsParameter.println("boolean w_retour = " + w_retour);
		ClsParameter.println("String w_am =  " + w_am);
		ClsParameter.println("boolean conges_annuels = " + conges_annuels);
		ClsParameter.println("boolean finconge = " + finconge);
		ClsParameter.println("boolean pnp = " + pnp);
		ClsParameter.println("int wnbj_a = " + wnbj_a);
		ClsParameter.println("int wnbj_c = " + wnbj_c);
		ClsParameter.println("Date dtDdmp = " + dtDdmp);
		ClsParameter.println("Date dtDdex = " + dtDdmp);
		ClsParameter.println("Rhpdo rhpdo = " + rhpdo);
		ClsParameter.println("boolean ca = " + ca);
		ClsParameter.println("int nbj_con = " + nbj_con);
		ClsParameter.println("String sav_pmcf = " + sav_pmcf);
		ClsParameter.println("Date sav_dfcf = " + sav_dfcf);
		ClsParameter.println("Date sav_ddcf = " + sav_ddcf);
		ClsParameter.println("Date w_date_anniv = " + w_date_anniv);
		ClsParameter.println("int nbj_enf = " + nbj_enf);
		ClsParameter.println("int nbj_anc = " + nbj_anc);
		ClsParameter.println("int nbj_deco = " + nbj_deco);
		ClsParameter.println("int ancien = " + ancien);
		ClsParameter.println("int minimum_enfant = " + minimum_enfant);
		ClsParameter.println("int age_max_fiscal = " + age_max_fiscal);
		ClsParameter.println("String dateformat = " + dateformat);

	}
	*/

}
