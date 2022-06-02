package com.kinart.paie.business.services.cloture;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.kinart.paie.business.model.*;
import com.kinart.paie.business.services.calcul.ClsParameterOfPay;
import com.kinart.paie.business.services.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.dao.DataAccessException;

@SuppressWarnings("unused")
public class ClsAgentUpdateBulletinNew
{
	public ClsGlobalUpdate globalUpdate;
	private Session oSession = null;

	public Salarie salarie = null;

	public boolean tab91 = true;

	public double bas_con = 0;

	public double moncp = 0;

	public double nbjcp = 0;

	public double conge = 0;

	public double nbjtr = 0;

	public boolean cgan_ms = false;

	public boolean exist_evcg = false;

	public double nbjreliq = 0;

	public boolean mois_conge = false;

	public boolean radie = false;

	public boolean radie_depuis = false;

	public String am = "";

	public boolean conges_annuels = false;

	public boolean finconge = false;

	public boolean pnp = false;
	
	public double nbj_a = 0;

	public double nbj_c = 0;

	public boolean ca = false;

	public double nbj_con = 0;

	public String sav_pmcf = "";

	public Date sav_dfcf = null;

	public Date sav_ddcf = null;

	public Date date_anniv = null;
	
	public Date date_debut_mois_ms;

	public Date date_fin_mois_ms;
	
	public Date date_debut_prochainmois;

	public Date date_fin_prochainmois;

	public double nbj_enf = 0;

	public double nbj_anc = 0;

	public double nbj_deco = 0;

	public int ancien = 0;

	public int numsalariecourant;

	public int nbsalarietotal;
	
	public boolean continuer = false;

	public boolean retour = false;
	
	public double jr_cg_ant = 0;
	
	public double jr_cg_ex = 0;
	
	public String error = "";

	private Map<CleCumulPaie, ClsCumul> cumulsMap = new HashMap<CleCumulPaie, ClsCumul>();
	
	private List<CalculPaie> listeCalculPaies = new ArrayList<CalculPaie>();
	
	

	public List<DetailEv> listOfEltVarConge = new ArrayList<DetailEv>(); // -- Liste initiale a ne pas modifier
	public List<DetailEv> listOfEltVarConge_ms = new ArrayList<DetailEv>(); // -- Liste des rhteltvarconge � g�n�rer le mois suivant

	private ClsUpdateBulletin update;
	
	public boolean deleteEVMois_flag = false;
	
	public boolean deleteEFMois_flag = false;

	public boolean transferEVMoisSuivant_flag = false;
	
	public boolean transferEcheancePretMoisSuivant_flag = false;
	
	//public boolean maj_conges_annuels_flag = false;

	public boolean maj_cp_flag = false;

	public boolean calc_pror_jcg = false;

	public boolean sup_prets17_flag = false;

	public boolean calc_nbjsup_flag = false;

	public boolean sup_fic_flag = false;

	public boolean update_flag = false;
	
	public String printOut = null;
	
	public List<Calcul> listeCalculs = new ArrayList<Calcul>();
	
	private Map<CleEltVarEnt,CleEltVarEnt> mapRhteltvarentDejaGenere = new HashMap<CleEltVarEnt,CleEltVarEnt>();
	
	private Map<CleEltVarDet,CleEltVarDet> mapRhteltvardetDejaGenere = new HashMap<CleEltVarDet,CleEltVarDet>();
	
	private boolean maj_eva_ent_already_check = false;
	
	private boolean maj_eva_ent_exist = false;
	
	private int maj_eva_nlig = 1;

	public synchronized Salarie getAgent()
	{
		return salarie;
	}

	public synchronized void setAgent(Salarie salarie)
	{
		this.salarie = salarie;
	}
	
	public ClsAgentUpdateBulletinNew(ClsUpdateBulletin update, Session oSession, Salarie salarie)
	{
		this.update = update;
		this.oSession = oSession;
		this.salarie = salarie;
		this.globalUpdate = update.globalUpdate;
	}
	
	private void initSalarie()
	{
		if(update.genfile == 'O')  printOut +="\n"+("-----Initialisation du salari�");
		
		sav_pmcf = salarie.getPmcf();
		sav_dfcf = salarie.getDfcf();
		sav_ddcf = salarie.getDdcf();
		
		//----------Date d'anniversaire du salari�--------------------
		if(update.type_dtanniv == 1)
			date_anniv = salarie.getDtes();
		else if (update.type_dtanniv == 2)
			date_anniv = salarie.getDdca();
		else if (update.type_dtanniv == 3)
			date_anniv = salarie.getDrtcg();
		
		//----------Gestion du code horaire du salari�--------------------
		tab91 = update.tab91b;
		if (tab91)
		{
			if ((!update.t91hc) && "HC".equals(salarie.getCods()))
				tab91 = false;
			if ((!update.t91hp) && "HP".equals(salarie.getCods()))
				tab91 = false;
			if ((!update.t91mc) && "MC".equals(salarie.getCods()))
				tab91 = false;
			if ((!update.t91mp) && "MP".equals(salarie.getCods()))
				tab91 = false;
			if ((!update.t91in) && "IN".equals(salarie.getCods()))
				tab91 = false;
		}
		//----------Date de d�but et de fin pour les cong�s-----------------
		if(tab91) 
		{
			date_debut_mois_ms = update.date_debut_mois_ms;
			date_fin_mois_ms = update.date_fin_mois_ms;
			
			date_debut_prochainmois = update.date_debut_prochainmois;
			date_fin_prochainmois = update.date_fin_prochainmois;
		}
		else
		{
	     	date_debut_mois_ms = new ClsDate(update.mois_ms, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getFirstDayOfMonth();
	     	date_fin_mois_ms = new ClsDate(date_debut_mois_ms).getLastDayOfMonth();
	     	
	     	date_debut_prochainmois= new ClsDate(update.prochainmois, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getFirstDayOfMonth();
	     	date_fin_prochainmois = new ClsDate(date_debut_prochainmois).getLastDayOfMonth();
			
		}
		
		//----------Determination si le salari� radi� ou mut�------------------
		radie = false;
		radie_depuis = false;
		if(StringUtil.equals("MU", salarie.getMrrx()) || StringUtil.equals("RA", salarie.getMrrx()))
		{
			if(salarie.getDmrr() != null && update.numerobulletin == 9)
			{
				String perioderadiation = new ClsDate(salarie.getDmrr()).getYearAndMonth();
				if(StringUtil.equals(update.periode, perioderadiation))
					radie = true;
				else
				{
					if(NumberUtils.toLong(update.periode) > NumberUtils.toLong(perioderadiation))
						radie_depuis = true;
				}
			}
		}
		
		//----------Zones nulles servant dans les conges------------------
		if (salarie.getJapa() == null)
			salarie.setJapa(new BigDecimal(0));
		
		if (salarie.getJapec() == null)
			salarie.setJapec(new BigDecimal(0));
		
		if (salarie.getNbjse() == null)
			salarie.setNbjse(new BigDecimal(0));
		
		if (salarie.getNbjsa() == null)
			salarie.setNbjsa(new BigDecimal(0));
		
		if (salarie.getNbjsm() == null)
			salarie.setNbjsm(new BigDecimal(0));
		
		if (salarie.getJrla() == null)
			salarie.setJrla(new BigDecimal(0));
		
		if (salarie.getJrlec() == null)
			salarie.setJrlec(new BigDecimal(0));
		
		if (salarie.getJded() == null)
			salarie.setJded(new BigDecimal(0));
		
		if (salarie.getDapa() == null)
			salarie.setDapa(new BigDecimal(0));
		
		if (salarie.getDapec() == null)
			salarie.setDapec(new BigDecimal(0));
		
		if (salarie.getDded() == null)
			salarie.setDded(new BigDecimal(0));
		
		if (salarie.getMtcf() == null)
			salarie.setMtcf(new BigDecimal(0));
		
		if (salarie.getNbjtr() == null)
			salarie.setNbjtr(new BigDecimal(0)); 
	}
	
	
	public boolean updateBulletin()
	{
		if(update.genfile == 'O')  printOut +="\n"+("-----Update Bulletin Salari�");
		
		try
		{
			initSalarie();
			continuer = false;
			if (StringUtil.equals("N" , salarie.getCals()))
			{
				if (radie_depuis || !radie)
				{
					//retour = deleteEVMois(salarie);
					deleteEVMois_flag = true;
					continuer = true;
				}
				if (!radie)
				{
					//retour = deleteEFMois(salarie);
					deleteEFMois_flag = true;
//					if (!transferEVMoisSuivant(salarie.getNmat()))
//						return false;
					transferEVMoisSuivant_flag = true;
					if (update.numerobulletin == 9)
					{
//						if (!transferEVCGMoisSuivant(salarie.getNmat()))
//							return false;
						chargerEVCGMoisCourant(salarie.getNmat());
						
//						if (!transferEcheancePretMoisSuivant(salarie.getNmat()))
//							return false;
						transferEcheancePretMoisSuivant_flag = true;
					}
					
					continuer = true;
				}
			}
			if (continuer == false)
			{
				chargerEVCGMoisCourant(salarie.getNmat());

				lectureCalculSurMoisCourant(salarie);
				
				if(update.numerobulletin == 9)
				{
					if (cgan_ms && !radie)
					{
						if (!alimenteCongeMoisProchain(salarie.getNmat()))
							return false;
					}
					if (exist_evcg)
						retour = updateZoneConge(salarie);
					
					retour = updateBaseCongeAgent(salarie);
				}
				else
				{
					salarie.setDapec(salarie.getDapec().add(new BigDecimal(bas_con)));
//					update_flag = true;
				}
				
				int count = listeCalculPaies.size();
				if (count == 0)
				{
//					retour = transferEVMoisSuivant(salarie.getNmat());
					transferEVMoisSuivant_flag = true;
					if (update.numerobulletin == 9)
					{
//						retour = transferEVCGMoisSuivant(salarie.getNmat());
//						retour = transferEcheancePretMoisSuivant(salarie.getNmat());
						transferEcheancePretMoisSuivant_flag = true;
					}
				}

//				retour = deleteEVMois(salarie);
				deleteEVMois_flag = true;

//				retour = deleteEFMois(salarie);
				deleteEFMois_flag = true;

				if (update.calc_part_auto)
				{
					if (!calculPartFiscale(salarie))
					{
						error = update.parameter.errorMessage("ERR-10518", update.langue, salarie.getNmat());
						if(update.genfile == 'O')  printOut +="\n"+(error);
						globalUpdate._setEvolutionTraitement(update.request, error, true);
						return false;
					}
				}
				if(StringUtils.equals(sav_pmcf, update.periode) && update.numerobulletin == 9)
				{
//					update.service.createQuery("delete from Rhtfic" + " where cdos = '" + update.dossier + "'" + " and nmat = '" + salarie.getNmat() + "'" + " and nbul = " + update.numerobulletin).executeUpdate();
					sup_fic_flag = true;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			update.error = ClsTreater._getStackTrace(e);
			return false;
		}	
		
		try
		{
			if(StringUtil.equalsIgnoreCase(globalUpdate.nomClient, ClsEntreprise.BGFIGE)){
				salarie.setJapec(new BigDecimal(jr_cg_ex));
				salarie.setJapa(new BigDecimal(jr_cg_ant));
			} 
			
			if( ! majSalarie())
				return false;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			update.error = ClsTreater._getStackTrace(e);
		}
		
		return true;
	}
	
	

	/**
	 * =>maj_pnp Conges payes non pris a partir des rubriques
	 */
	private boolean updateCongePayeNonPris(Salarie salarie, BigDecimal mont)
	{
		if(update.genfile == 'O')  printOut +="\n"+("-----Update Cpnp");
		
		double wnbjcad2 = 0;
		double wnbjcad = 0;
		double nbjstot = 0;
		if (exist_evcg == false)
		{
			salarie.setJapa(new BigDecimal(0));
			salarie.setJapec(new BigDecimal(0));
			salarie.setJrla(new BigDecimal(0));
			salarie.setJrlec(new BigDecimal(0));
			salarie.setJded(new BigDecimal(0));
			salarie.setNbjse(new BigDecimal(0));
			salarie.setNbjsa(new BigDecimal(0));
			salarie.setNbjsm(new BigDecimal(0));
			salarie.setDdcf(null);
			salarie.setDfcf(null);
			salarie.setDdenv(null);
			salarie.setDrenv(null);
			salarie.setNbjtr(new BigDecimal(0));
			salarie.setPmcf(" ");
			salarie.setNbjcf(new BigDecimal(0));
			salarie.setNbjaf(new BigDecimal(0));
			salarie.setDrtcg(null);
			salarie.setDapa(new BigDecimal(0));
			salarie.setDded(new BigDecimal(0));
			salarie.setDdcf(null);
			salarie.setDfcf(null);
			salarie.setJded(new BigDecimal(0));
			salarie.setDapec(new BigDecimal(0));
			salarie.setMtcf(new BigDecimal(0));

		}
		else
		{
			wnbjcad = mont.doubleValue();
			nbjstot = salarie.getNbjse().doubleValue() + salarie.getNbjsa().doubleValue() + salarie.getNbjsm().doubleValue();
			if (wnbjcad >= nbjstot)
			{
				salarie.setNbjse(new BigDecimal(0));
				salarie.setNbjsa(new BigDecimal(0));
				salarie.setNbjsm(new BigDecimal(0));
			}
			else
			{
				wnbjcad2 = nbjstot;
				if (wnbjcad2 >= salarie.getNbjsa().doubleValue())
				{
					wnbjcad2 -= salarie.getNbjsa().doubleValue();
					salarie.setNbjsa(new BigDecimal(0));
					if (wnbjcad2 >= salarie.getNbjse().doubleValue())
					{
						wnbjcad2 -= salarie.getNbjse().doubleValue();
						salarie.setNbjse(new BigDecimal(0));
						if (wnbjcad2 >= salarie.getNbjsm().doubleValue())
						{
							salarie.setNbjsm(new BigDecimal(0));
						}
						else
						{
							salarie.setNbjsm(new BigDecimal(salarie.getNbjsm().doubleValue() - wnbjcad2));
						}
					}
					else
					{
						salarie.setNbjse(new BigDecimal(salarie.getNbjse().doubleValue() - wnbjcad2));
					}
				}
				else
				{
					salarie.setNbjsa(new BigDecimal(salarie.getNbjsa().doubleValue() - wnbjcad2));
				}
			}
			if (wnbjcad <= salarie.getJapa().doubleValue())
			{
				salarie.setJapa(new BigDecimal(salarie.getJapa().doubleValue() - wnbjcad));
			}
			else
			{
				salarie.setJapec(new BigDecimal(salarie.getJapec().doubleValue() - (wnbjcad - salarie.getJapa().doubleValue())));
				salarie.setJapa(new BigDecimal(0));
			}

			salarie.setJrlec(new BigDecimal(salarie.getJrlec().doubleValue() + wnbjcad));

			if (salarie.getDdcf() == null)
				conges_annuels = true;
	
		}

		pnp = true;

		return true;
	}

	/**
	 * =>maj_pret13 Mise a jour de la table des prets (fiche salarie)
	 */
	private boolean updatePret13(String nmat, int lg, String crub, BigDecimal mont)
	{
		if(update.genfile == 'O')  printOut +="\n"+("-----Update prets 13");
		List<PretInterne> result = update.service.find("From PretInterne Where idEntreprise="+update.dossier+" And nmat='"+nmat+"' And lg="+lg);
		PretInterne pret = null;//update.service.get(Rhtpretsagent.class, new RhtpretsagentPK(update.dossier, nmat, lg));
		if(result!=null && !result.isEmpty()) pret = result.get(0);
		if (pret != null)
		{
			//PretInterne pret = (Rhtpretsagent) o;
			if (ClsObjectUtil.isNull(pret.getMtremb()))
				pret.setMtremb(new BigDecimal(0));
			pret.setMtremb(pret.getMtremb().add(mont));
			//
			update.service.update(pret);
		}
		else
		{
			error = update.parameter.errorMessage("ERR-90132", update.langue, nmat, crub, lg);
			globalUpdate._setEvolutionTraitement(update.request, error, true);
			return false;
		}
		return true;
	}

	/**
	 * =>maj_pret17 Mise a jour des zones de pret dans PAPRENT
	 */
	private boolean updatePret17(String nmat, String crub, String nprt, BigDecimal mont)
	{
		if(update.genfile == 'O')  printOut +="\n"+("-----Update prets 17");
		
		PretExterneEntete pretent = null;
		List<PretExterneEntete> result = update.service.find("From PretExterneEntete Where idEntreprise="+update.dossier+" And nprt="+nprt);
		//Object o = update.service.get(Rhtpretent.class, new RhtpretentPK(update.dossier, nprt));
		Object o = null;
		if(result!=null && !result.isEmpty()) o = result.get(0);
		if (o == null)
		{
			error = update.parameter.errorMessage("ERR-90133", update.langue, nmat, crub, nprt);
			globalUpdate._setEvolutionTraitement(update.request, error, true);
			return false;
		}
		else
		{
			//pretent = (Rhtpretent) o;
			if (!(nmat.equals(pretent.getNmat()) && crub.equals(pretent.getCrub())))
			{
				error = update.parameter.errorMessage("ERR-90133", update.langue, nmat, crub, nprt);
				globalUpdate._setEvolutionTraitement(update.request, error, true);
				return false;
			}
		}

		if (!ClsObjectUtil.isNull(pretent.getResr()))
		{
			pretent.setResr(pretent.getResr().subtract(mont));
			if (pretent.getResr().intValue() <= 0)
				pretent.setResr(new BigDecimal(0));
		}
		int i = 0;
		if (!ClsObjectUtil.isNull(pretent.getNber()))
		{
			i = pretent.getNber().intValue();
			i--;
			if (i < 0)
				i = 0;
			pretent.setNber(i);
		}

		if (pretent.getResr().intValue() == 0 && pretent.getNber() == 0)
			pretent.setPact("N");

		update.service.update(pretent);

		return true;
	}

	/**
	 * =>maj_eva Generation d'EV sur mois suivant
	 */
	private boolean genElementVariableNextMonth(String nmat, String crub, BigDecimal mont)
	{
		if(update.genfile == 'O')  printOut +="\n"+("-----g�n�ration ev sur mois suivant");
		
		ElementVariableDetailMois evar = new ElementVariableDetailMois();
		CleEltVarDet pkEvar = new CleEltVarDet(new Integer(update.dossier), update.mois_ms, nmat, maj_eva_nlig++);
		evar.setAamm(update.mois_ms);
		evar.setIdEntreprise(new Integer(update.dossier));
		evar.setNmat(nmat);
		evar.setNbul(update.nbul_ms);
		evar.setNlig(maj_eva_nlig++);
		//Comp_id(new RhteltvardetPK(update.dossier, nmat, update.nbul_ms, maj_eva_nlig++));
		evar.setCuti(update.user);
		evar.setRubq(crub);
		evar.setArgu(" ");
		evar.setNprt(" ");
		evar.setRuba(" ");
		evar.setMont(mont);
		
		ElementVariableEnteteMois o = null;
		if(! maj_eva_ent_already_check)
		{
         List<ElementVariableEnteteMois> result = update.service.find("From ElementVariableEnteteMois Where idEntreprise="+evar.getIdEntreprise().intValue()+" And aamm='"+evar.getAamm()+"' And nmat='"+evar.getNmat()+"' And nbul="+evar.getNbul().intValue());
		 //o = (Rhteltvarent) update.service.get(Rhteltvarent.class, new RhteltvarentPK(evar.getCdos(), evar.getAamm(), evar.getNmat(), evar.getNbul()));
		 if(result!=null && !result.isEmpty()) o = result.get(0);
			maj_eva_ent_already_check = true;
		 
		 if(o != null)
			 maj_eva_ent_exist = true;
		}
		if (! maj_eva_ent_exist)
		{
			ElementVariableEnteteMois event = new ElementVariableEnteteMois();
			CleEltVarEnt pkEvent = new CleEltVarEnt(evar.getIdEntreprise(), evar.getAamm(), evar.getNmat(), evar.getNbul());
			event.setIdEntreprise(evar.getIdEntreprise());
			event.setAamm(evar.getAamm());
			event.setNmat(evar.getNmat());
			event.setNbul(evar.getNbul());
			event.setDdpa(date_debut_mois_ms);
			event.setDfpa(date_fin_mois_ms);
			event.setBcmo("N");
			
			if(!mapRhteltvarentDejaGenere.containsKey(pkEvent))
			{
				update.service.save(event);
				mapRhteltvarentDejaGenere.put(pkEvent, pkEvent);
			}
		}
		
		if(!mapRhteltvardetDejaGenere.containsKey(pkEvar))
		{
			update.service.save(evar);
			mapRhteltvardetDejaGenere.put(pkEvar, pkEvar);
		}

		return true;
	}

	private void _chargerCumuls(String nmat)
	{
		if(update.genfile == 'O')  printOut +="\n"+("-----Suppression des anciens cumuls");
		
		List<CumulPaie> liste = update.updateservice.getCumulsToDelete(nmat);
		for (CumulPaie rhtcumul : liste)
		{
			CleCumulPaie cleCumulPaie = new CleCumulPaie();
			cleCumulPaie.setIdEntreprise(rhtcumul.getIdEntreprise());
			cleCumulPaie.setAamm(rhtcumul.getAamm());
			cleCumulPaie.setNbul(rhtcumul.getNbul());
			cleCumulPaie.setNmat(rhtcumul.getNmat());
			cleCumulPaie.setRubq(rhtcumul.getRubq());
			this.cumulsMap.put(cleCumulPaie, new ClsCumul(rhtcumul, true));
		}
		//System.out.println("Apr�s chargement des cumuls du salari� "+nmat+", taille du map = "+this.cumulsMap.size());
		//update.updateservice.deleteCumuls(update.service, nmat);
	}

	private void _chargerCumuls99(String nmat)
	{
		if(update.genfile == 'O')  printOut +="\n"+("-----Suppression des anciens cumuls annuels");
		
		List<CumulPaie> liste = update.updateservice.getCumulsToDelete99(nmat);
		for (CumulPaie rhtcumul : liste)
		{
			CleCumulPaie cleCumulPaie = new CleCumulPaie();
			cleCumulPaie.setIdEntreprise(rhtcumul.getIdEntreprise());
			cleCumulPaie.setAamm(rhtcumul.getAamm());
			cleCumulPaie.setNmat(rhtcumul.getNmat());
			cleCumulPaie.setNbul(rhtcumul.getNbul());
			cleCumulPaie.setRubq(rhtcumul.getRubq());
			this.cumulsMap.put(cleCumulPaie, new ClsCumul(rhtcumul,true));
		}
		//System.out.println("Apr�s chargement des cumuls 99 du salari� "+nmat+", taille du map = "+this.cumulsMap.size());

		//update.updateservice.deleteCumuls99(update.service, nmat);
	}

	private boolean _updateCumulMap(CleCumulPaie cumulPK, CalculPaie calcul)
	{
		//if(update.genfile == 'O')  printOut +="\n"+("-----Recuperation du cumul � enregistrer");
		ClsCumul cum = this.cumulsMap.get(cumulPK);
		CumulPaie cumul = null;
		boolean exist = false;
		try
		{
			if (cum == null)
			{
				cum = new ClsCumul();
				cumul = new CumulPaie();
				cumul.setAamm(cumulPK.getAamm());
				cumul.setRubq(cumulPK.getRubq());
				cumul.setNmat(cumulPK.getNmat());
				cumul.setNbul(cumulPK.getNbul());
				//cumul.setComp_id(new Integer(cumulPK.getCdos(), cumulPK.getNmat(), cumulPK.getAamm(), cumulPK.getRubq(), cumulPK.getNbul()));
				cumul.setBasc(calcul.getBasc());
				cumul.setBasp(calcul.getBasp());
				cumul.setMont(calcul.getMont());
				cumul.setTaux(calcul.getTaux());
				exist = false;
			}
			else
			{
				cumul = new CumulPaie();
				cumul.setAamm(cumulPK.getAamm());
				cumul.setRubq(cumulPK.getRubq());
				cumul.setNmat(cumulPK.getNmat());
				cumul.setNbul(cumulPK.getNbul());
				//cumul.setComp_id(new Integer(cumulPK.getCdos(), cumulPK.getNmat(), cumulPK.getAamm(), cumulPK.getRubq(), cumulPK.getNbul()));
				cumul.setBasc(cum.getCumul().getBasc().add(calcul.getBasc()));
				cumul.setBasp(cum.getCumul().getBasp().add(calcul.getBasp()));
				cumul.setMont(cum.getCumul().getMont().add(calcul.getMont()));
				cumul.setTaux(cum.getCumul().getTaux().add(calcul.getTaux()));
				exist = cum.isExistindb();
			}
			
			cum.setCumul(cumul);
			cum.setExistindb(exist);
			this.cumulsMap.put(cumulPK, cum);
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private boolean _updateCumulMois(Salarie salarie)
	{
		String queryString="";
		queryString+="INSERT INTO CumulPaie (idEntreprise, NMAT, AAMM,RUBQ, NBUL, BASC,BASP, TAUX, MONT) ";
		queryString+="SELECT idEntreprise, NMAT, AAMM, RUBQ, NBUL, sum(BASC), sum(BASP), max(TAUX), sum(MONT) ";
		queryString+="FROM CalculPaie WHERE idEntreprise = :cdos and NMAT = :nmat and AAMM = :aamm and NBUL = :nbul ";
		queryString+="GROUP BY idEntreprise, NMAT, AAMM, RUBQ, NBUL";
		
		try
		{
			Query q = update.service.getSession().createSQLQuery(queryString);
			q.setParameter("cdos", this.update.dossier, StandardBasicTypes.STRING);
			q.setParameter("nmat", salarie.getNmat(), StandardBasicTypes.STRING);
			q.setParameter("aamm", this.update.periode, StandardBasicTypes.STRING);
			q.setParameter("nbul", this.update.numerobulletin, StandardBasicTypes.INTEGER);
			q.executeUpdate();
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	/**
	 * =>lect_rub_cal Lecture du calcul sur mois courant
	 */

	private boolean lectureCalculSurMoisCourant(Salarie salarie)
	{
		if(update.genfile == 'O')  printOut +="\n"+("-----lectureCalculSurMoisCourant");
		
		String nmat = salarie.getNmat();

		listeCalculPaies = update.updateservice.getListeBulletinCalculFromCalculPaie(update.dossier, update.periode, update.numerobulletin, nmat);

		///////////Ajout par yannick :  au lieu de tester l'existence des cumuls avant de les inserer, je pr�fere tout charger en m�moire
		//////////de sorte qu'on ne fasse que la sauvegarde ou la mise � jour sans plus tester l'existence en bd
		this._chargerCumuls(nmat);
		//on insere directement les cumuls du mois
		//this._updateCumulMois(salarie);

		this._chargerCumuls99(nmat);

		ElementSalaire rubrique = null;
		
		Calcul calcul = null;

		for (CalculPaie ocalcul : listeCalculPaies)
		{
			calcul = new Calcul(ocalcul);
			
			if((!calcul.getMont().equals(new BigDecimal(0))) || this.update.rcon_reg(calcul.getRubq()))
			{
			
				rubrique = update._getRubrique(update.dossier, calcul.getRubq());
				if (rubrique == null)
				{
					error = update.parameter.errorMessage("ERR-90104", update.langue, nmat, calcul.getRubq());
					globalUpdate._setEvolutionTraitement(update.request, error, true);
					return false;
				}

				if (StringUtils.equals(calcul.getRubq(),update.rub_bc))
					bas_con = calcul.getMont().doubleValue();
	
				if (StringUtils.equals(calcul.getRubq(), update.rub_pnp))
					retour = updateCongePayeNonPris(salarie, calcul.getMont());
				
				if (StringUtils.equals(calcul.getRubq(), update.rub_cg_ant))
					jr_cg_ant = calcul.getMont().doubleValue();
				
				if (StringUtils.equals(calcul.getRubq(), update.rub_cg_ex))
					jr_cg_ex = calcul.getMont().doubleValue();
			
				if (StringUtils.equals(calcul.getRubq(), update.rubriqueNbreJourTravail))
				{
					if ("B".equals(update.rubriqueNbreJourPlage))
					{
						nbjtr = calcul.getBasc().doubleValue();
					}
					else if ("T".equals(update.rubriqueNbreJourPlage))
					{
						nbjtr = calcul.getTaux().doubleValue();
					}
					else if ("M".equals(update.rubriqueNbreJourPlage))
					{
						nbjtr = calcul.getMont().doubleValue();
					}
				}
				
				if (update.numerobulletin == 9)
				{
					if(NumberUtils.in(rubrique.getAlgo(), 21,83,31,50,27))
					{
						conge += calcul.getMont().doubleValue();
					}
					else if (rubrique.getAlgo() == 22 || rubrique.getAlgo() == 82)
					{
						moncp = calcul.getMont().doubleValue();
					}
				}
				if (rubrique.getAlgo() == 13)
				{
					if (StringUtils.isNotBlank(calcul.getNprt()))
					{
//						if (!updatePret13(nmat, Integer.valueOf(calcul.getNprt()), calcul.getRubq(), calcul.getMont()))
//						{
//							return false;
//						}
						calcul.maj_pret13_flag = true;
					}
				}
				else if (NumberUtils.in(rubrique.getAlgo(), 17,20))
				{
					if (StringUtils.isNotBlank(calcul.getNprt()))
					{
//						if (!updatePret17(nmat, calcul.getRubq(), calcul.getNprt(), calcul.getMont()))
//						{
//							return false;
//						}
						calcul.maj_pret17_flag = true;
					}
				}
	
				if (!radie)
				{
					if (update.t_ev_ms != null)
					{
						String crub_ms = update.t_ev_mc_ms.get(calcul.getRubq());
						if(StringUtils.isNotBlank(crub_ms) && !StringUtils.equals(ParameterUtil.formatRubrique, crub_ms))
							calcul.maj_eva_flag = true;
					}
				}
				
			}
			calcul.maj_cumul_flag = true;
			
			listeCalculs.add(calcul);
		}
		return true;
	}
	
	public boolean majSalarie()
	{
		if(update.genfile == 'O')  printOut +="\n"+("-----Maj Global du salari�");
		if(update.genfile == 'O')  printOut +="\n"+("---------------------------------------------------------------------------");
		if(update.genfile == 'O')  printOut +="\n"+("-----SALARIE : "+ salarie.getNmat()+" "+salarie.getNom()+" "+salarie.getPren());
		if(update.genfile == 'O')  printOut +="\n"+("---------------------------------------------------------------------------");
		if(update.genfile == 'O')  this.agentUpdateToString();
		
		if(deleteEVMois_flag)
			if(! deleteEVMois(salarie)) 
				return false;
		
		if(deleteEFMois_flag)
			if( ! deleteEFMois(salarie)) 
				return false;

		if(transferEVMoisSuivant_flag)
			if( ! transferEVMoisSuivant(salarie.getNmat())) 
				return false;
		
		if(transferEcheancePretMoisSuivant_flag)
			if( !transferEcheancePretMoisSuivant(salarie.getNmat())) 
				return false;
		
		Calcul calcul = null;
		for (int i = 0; i < listeCalculs.size(); i++)
		{
			calcul = listeCalculs.get(i);
			
			if(calcul.maj_pret13_flag)
				if( ! updatePret13(salarie.getNmat(), Integer.valueOf(calcul.getNprt()), calcul.getRubq(), calcul.getMont()))
					return false;
			
			if(calcul.maj_pret17_flag)
				if ( ! updatePret17(salarie.getNmat(), calcul.getRubq(), calcul.getNprt(), calcul.getMont()))
						return false;
			
			if(calcul.maj_eva_flag)
				if( ! genElementVariableNextMonth(salarie.getNmat(), update.t_ev_mc_ms.get(calcul.getRubq()),calcul.getMont()))
					return false;
			
			if(calcul.maj_cumul_flag)
				if( ! maj_cumuls(calcul, salarie.getNmat()))
					return false;
		}
		
		//maj effectif des cumuls
		Collection<ClsCumul> cumulIterator = this.cumulsMap.values();
		for (ClsCumul oCum : cumulIterator)
		{
			if(oCum.isExistindb())
				update.service.update(oCum.getCumul());
			else
				update.service.save(oCum.getCumul());
		}
		
		if(sup_fic_flag)
		{
			Session session = update.service.getSession();
			try
			{
				if( ! update.updateservice.supp_fictif(session, salarie.getNmat()))
					return false;
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally
			{
				update.service.closeSession(session);
			}
		}
		
		
		
		for (int i = 0; i < listOfEltVarConge.size(); i++)
		{
			if(listOfEltVarConge.get(i).transferEVCGMoisSuivant_flag)
				if( ! transfererUnEVCGMoisSuivant(listOfEltVarConge_ms.get(i),salarie.getNmat()))
					return false;
			
			if(listOfEltVarConge.get(i).pas_moi_flag)
				if( !insertCongeMoisSuivant(listOfEltVarConge_ms.get(i), salarie.getNmat()))
					return false;
				
			if(listOfEltVarConge.get(i).maj_det_flag)
				if( !updateDetailAbsConge(listOfEltVarConge.get(i), salarie.getNmat()))
					return false;
			
		}
		
		if(sup_prets17_flag)
		{
			Session session = update.service.getSession();
			try
			{
				update.updateservice.updatePrets17(session, update.dossier, salarie.getNmat());
			}
			catch (Exception e)
			{
				// TODO: handle exception
			}
			finally
			{
				update.service.closeSession(session);
			}
		}
		
		update.service.update(salarie);
		
		if(this.update.genfile == 'O')
			this.agentUpdateToString();
		
		return true;
		
	}
	
	private boolean maj_cumuls(Calcul calcul, String nmat)
	{
		
		if (StringUtils.isNotBlank(calcul.getRuba()))
		{
			calcul.setRubq(calcul.getRuba());
			calcul.setBasc(new BigDecimal(0));
			calcul.setBasp(new BigDecimal(0));
			calcul.setTaux(new BigDecimal(0));
		}
		
		boolean result = false;
	
		result = this._updateCumulMap(new CleCumulPaie(new Integer(update.dossier), nmat, update.periode, calcul.getRubq(), update.numerobulletin), calcul);
		
		if(! result)
			return false;

		

		result = this._updateCumulMap(new CleCumulPaie(new Integer(update.dossier), nmat, update.am99, calcul.getRubq(), 9), calcul);
		if(!result)
			return false;

		return true;
	}

	//	--------------------------------------------------------------------------------
	//	-- Transfert des echeances de prets sur mois suivant (wsal01.cals = 'N')
	//	---------------------------------------------------------------------------------
	private boolean transferEcheancePretMoisSuivant(String nmat)
	{
		if(update.genfile == 'O')  printOut +="\n"+("-----Transfert des echeances de prets");
		
		String perb1 = new ClsDate(update.periode, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getDateS(update.dateformat);
		 
		double wmont_pret = 0;
		List<PretExterneEntete> l = update.updateservice.getPretsToTransfert(update.dossier, nmat, perb1);
		PretExterneDetail o = null;
		for (PretExterneEntete entete : l)
		{
			List<PretExterneDetail> result = update.service.find("From PretExterneDetail Where idEntreprise="+update.dossier+" And nprt="+entete.getNprt()+" And perb='"+new ClsDate(update.periode, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getDateS()+"'");
			//o = update.service.get(Rhtpretlig.class, new RhtpretligPK(update.dossier, entete.getNprt(), new ClsDate(update.periode, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getDate()));
			if(result!=null && !result.isEmpty()) o = result.get(0);
			if (o == null)
			{
				wmont_pret = 0;
			}
			else
			{
				wmont_pret = o.getEchr().doubleValue();
				//((Rhtpretlig) o).setEchr(new BigDecimal(0));
				update.service.update(o);
				Object o2 = update.updateservice.getMaxPerbFromPret(update.dossier, entete.getNprt());
				if (o2 != null)
				{
					Date perb = new ClsDate((Date) o2).addMonth(1);
					PretExterneDetail ligne = new PretExterneDetail();
					ligne.setPerb(perb);
					ligne.setIdEntreprise(new Integer(update.dossier));
					ligne.setNprt(entete.getNprt());
					//ligne.setComp_id(new RhtpretligPK(update.dossier, entete.getNprt(), perb));
					ligne.setEcho(new BigDecimal(0));
					ligne.setEchr(new BigDecimal(wmont_pret));
					ligne.setInte(new BigDecimal(0));
					ligne.setNbul(9);
					ligne.setTaxe(new BigDecimal(0));

					update.service.save(ligne);
				}
			}

		}

		return true;
	}

	//	---------------------------------------------------------------------------------
	//	--        Transfert des EV sur mois suivant (wsal01.cals = 'N')
	//	--------------------------------------------------------------------------------
	private boolean transferEVMoisSuivant_save(String nmat)
	{
		if(update.genfile == 'O')  printOut +="\n"+("-----Transfert des ev sur le mois suivant");
		Session session = update.service.getSession();
		try
		{
			return update.updateservice.transferEVMoisSuivant(session, update.dossier, update.periode, update.numerobulletin, update.mois_ms,
					update.nbul_ms, nmat, new ClsDate(date_debut_mois_ms).getDateS(update.dateformat), new ClsDate(date_fin_mois_ms)
							.getDateS(update.dateformat), update.user);
		}
		catch (Exception e)
		{
			// TODO: handle exception
			return false;
		}
		finally
		{
			update.service.closeSession(session);
		}
	}
	
	private boolean transferEVMoisSuivant(String nmat)
	{
		if(update.genfile == 'O')  printOut +="\n"+("-----Transfert des ev sur le mois suivant");

		List<Object[]> l = update.updateservice.chargerEVEnteteEtDetail(update.dossier, update.periode, nmat, update.numerobulletin);

		Object[] ev = null;
		ElementVariableDetailMois evar = null;
		ElementVariableEnteteMois event = null;
		//Object o = null;
		for (Object object : l)
		{
			ev = (Object[]) object;
			evar = (ElementVariableDetailMois)ev[0];
			evar.setAamm(update.mois_ms);
			evar.setNbul(update.nbul_ms);
			evar.setCuti(StringUtil.nvl(evar.getCuti(), update.user));

			List<ElementVariableEnteteMois> result = update.service.find("From ElementVariableEnteteMois Where idEntreprise="+evar.getIdEntreprise()+" And aamm='"+evar.getAamm()+"' And nmat='"+evar.getNmat()+"' And nbul="+evar.getNbul());
			ElementVariableEnteteMois o = null;
			if(result!=null && !result.isEmpty()) o = result.get(0);
			//o = update.service.get(Rhteltvarent.class, new RhteltvarentPK(evar.getCdos(), evar.getAamm(), evar.getNmat(), evar.getNbul()));
			if (o == null)
			{
				event = new ElementVariableEnteteMois();
				CleEltVarEnt pkEv = new CleEltVarEnt(evar.getIdEntreprise(), evar.getAamm(), evar.getNmat(), evar.getNbul());
				event.setIdEntreprise(evar.getIdEntreprise());
				event.setAamm(evar.getAamm());
				event.setNmat(evar.getNmat());
				event.setNbul(evar.getNbul());
				//event.setComp_id(new RhteltvarentPK(evar.getCdos(), evar.getAamm(), evar.getNmat(), evar.getNbul()));
				event.setDdpa(date_debut_mois_ms);
				event.setDfpa(date_fin_mois_ms);
				event.setBcmo("N");

				if(!mapRhteltvarentDejaGenere.containsKey(pkEv))
				{
					update.service.save(event);
					mapRhteltvarentDejaGenere.put(pkEv, pkEv);
				}					
			}
			//
			update.service.save(evar);
		}
		return true;
	}
	
	/**
	 * Charger les ev du salari� pour la p�riode courante
	 * @param nmat
	 * @return
	 */
	
	private boolean chargerEVCGMoisCourant(String nmat)
	{
		if(update.genfile == 'O')  printOut +="\n"+("-----Chargement de la liste des ev � traiter");
		
		List<Object[]> l = update.updateservice.chargerEVMoisCourant(update.dossier, update.periode, nmat, update.numerobulletin);
		ElementVariableConge evconge = null;
		ElementVariableEnteteMois event = null;
		ClsUpdateBulletin.RhfnomMotifConge fnomMotif = null;
		DetailEv elementConge = null;
		
		exist_evcg = l.size() > 0;
		
		for (Object[] obj : l)
		{
			evconge = (ElementVariableConge) obj[0];
			
			evconge.setCuti(StringUtil.nvl(evconge.getCuti(), update.user));
			
			event = (ElementVariableEnteteMois) obj[1];
			
			elementConge = new DetailEv();
			
			fnomMotif = update._getRhfnomMotifConges(evconge.getMotf());
			
			if(fnomMotif != null)
			{
				if (StringUtils.equals(update.motif_reliq, evconge.getMotf()))
				{
					nbjreliq += evconge.getNbja().doubleValue();
				}

				if (fnomMotif.montant1 == 1 && fnomMotif.montant3 == 1)
				{
					nbjcp += evconge.getNbjc().doubleValue();
				}

				if ((fnomMotif.montant1 == 1 && fnomMotif.montant3 == 0) || StringUtils.equals(evconge.getMotf(), update.motif_cg_abs_ms))
				{
					if(evconge.getDdeb().equals(event.getDdpa()))
					{
						mois_conge = true;
					}
					
					String d1 = null;
					ClsInfoPeriode period = null;
					int ind91 = 0;
					if (tab91)
					{
						for (int j = 0; j < update.listePeriodesT91.size(); j++)
						{
							period = update.listePeriodesT91.get(j);
							ind91 = j;
							if (period.ddebut != null)
							{
								d1 = new ClsDate(period.ddebut).getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
								if(StringUtils.equals(update.dernierePeriodeCloturee, d1))
									break;
							}
						}
						if (ind91 < update.listePeriodesT91.size() - 1)
						{
							period = update.listePeriodesT91.get(ind91 + 1);
							if(evconge.getDdeb().compareTo(period.dfin) > 0)
							{
								elementConge.transferEVCGMoisSuivant_flag = true;
								cgan_ms = true;
							}
						}
					}
					else
					{
						am = new ClsDate(evconge.getDdeb()).getYearAndMonth();
						if (NumberUtils.toLong(am) > NumberUtils.toLong(update.periode))
						{
							elementConge.transferEVCGMoisSuivant_flag = true;
							cgan_ms = true;
						}
					}
				}
			}
			else
			{
				fnomMotif = update.new RhfnomMotifConge();
				fnomMotif.montant1 = null;
				fnomMotif.montant3 = null;
			}

			elementConge = new DetailEv();
			elementConge.bcmo = event.getBcmo();
			elementConge.ddeb = evconge.getDdeb();
			elementConge.ddpa = event.getDdpa();
			elementConge.dfin = evconge.getDfin();
			elementConge.dfpa = event.getDfpa();
			elementConge.mnt1 = fnomMotif.montant1;
			elementConge.mnt3 = fnomMotif.montant3;
			elementConge.mont = evconge.getMont();
			elementConge.motf = evconge.getMotf();
			elementConge.nbja = evconge.getNbja().doubleValue();
			elementConge.nbjc = evconge.getNbjc().doubleValue();
			elementConge.cuti = evconge.getCuti();

			listOfEltVarConge.add(elementConge);
			listOfEltVarConge_ms.add(elementConge);
		}
		
		if(nbjtr < 0)
			nbjtr = 0;
		
		if (StringUtils.isBlank(update.motif_cg_abs_ms))
			cgan_ms = false;
		return true;
	}

	/**
	 * =>trans_cong Transfert des conges sur mois suivant (wsal01.cals = 'N')
	 */
	private boolean transfererUnEVCGMoisSuivant(DetailEv detailEv ,String nmat)
	{
		if(update.genfile == 'O')  printOut +="\n"+("-----Transfert des evcg sur le mois suivant");
		
		ElementVariableConge evconge = null;
		ElementVariableEnteteMois eventete = null;
		try
		{
			evconge = new ElementVariableConge();
			evconge.setIdEntreprise(new Integer(update.dossier));
			evconge.setAamm(update.mois_ms);
			evconge.setNmat(nmat);
			evconge.setNbul(update.nbul_ms);
			evconge.setDdeb(detailEv.ddeb);
			//evconge.setComp_id(new RhteltvarcongePK(update.dossier, update.mois_ms, nmat, update.nbul_ms, detailEv.ddeb));
			evconge.setDfin(detailEv.dfin);
			evconge.setNbjc(new BigDecimal(detailEv.nbjc));
			evconge.setNbja(new BigDecimal(detailEv.nbja));
			evconge.setMotf(detailEv.motf);
			evconge.setMont(detailEv.mont);
			evconge.setCuti(StringUtil.nvl(detailEv.cuti, update.user));

			List<ElementVariableEnteteMois> result = update.service.find("From ElementVariableEnteteMois Where idEntreprise="+evconge.getIdEntreprise()+" And aamm='"+evconge.getAamm()+"' And nmat='"+evconge.getNmat()+"' And nbul="+evconge.getNbul());
			ElementVariableEnteteMois o = null;//update.service.get(Rhteltvarent.class, new RhteltvarentPK(evconge.getCdos(), evconge.getAamm(), evconge.getNmat(), evconge.getNbul()));
			if(result!=null && !result.isEmpty()) eventete = result.get(0);
			//eventete = (Rhteltvarent) update.service.get(Rhteltvarent.class, new RhteltvarentPK(evconge.getCdos(), evconge.getAamm(), evconge.getNmat(), evconge.getNbul()));
			if (eventete == null)
			{
				ElementVariableEnteteMois event = new ElementVariableEnteteMois();
				event.setIdEntreprise(evconge.getIdEntreprise());
				event.setAamm(evconge.getAamm());
				event.setNmat(evconge.getNmat());
				event.setNbul(evconge.getNbul());
				//event.setComp_id(new RhteltvarentPK(evconge.getCdos(), evconge.getAamm(), evconge.getNmat(), evconge.getNbul()));
				event.setDdpa(date_debut_mois_ms);
				event.setDfpa(date_fin_mois_ms);
				event.setBcmo("N");
				
				update.service.save(event);
			}
			
			update.service.save(evconge);
		}
		catch (DataAccessException e)
		{
			e.printStackTrace();
			return false;
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private boolean transferEVCGMoisSuivant(String nmat)
	{
		if(update.genfile == 'O')  printOut +="\n"+("-----Transfert des evcg sur le mois suivant");
		
		DetailEv detailEv = null;
		ElementVariableConge evconge = null;
		ElementVariableEnteteMois eventete = null;
		try
		{
			for (int i = 0; i < listOfEltVarConge.size(); i++)
			{
				if(listOfEltVarConge.get(i).transferEVCGMoisSuivant_flag)
				{
					detailEv = listOfEltVarConge_ms.get(i);
					evconge = new ElementVariableConge();
					evconge.setIdEntreprise(new Integer(update.dossier));
					evconge.setAamm(update.mois_ms);
					evconge.setNmat(nmat);
					evconge.setNbul(update.nbul_ms);
					evconge.setDdeb(detailEv.ddeb);
					//evconge.setComp_id(new RhteltvarcongePK(update.dossier, update.mois_ms, nmat, update.nbul_ms, detailEv.ddeb));
					evconge.setDfin(detailEv.dfin);
					evconge.setNbjc(new BigDecimal(detailEv.nbjc));
					evconge.setNbja(new BigDecimal(detailEv.nbja));
					evconge.setMotf(detailEv.motf);
					evconge.setMont(detailEv.mont);
					evconge.setCuti(StringUtil.nvl(detailEv.cuti, update.user));

					List<ElementVariableEnteteMois> result = update.service.find("From ElementVariableEnteteMois Where idEntreprise="+evconge.getIdEntreprise()+" And aamm='"+evconge.getAamm()+"' And nmat='"+evconge.getNmat()+"' And nbul="+evconge.getNbul());
					if(result!=null && !result.isEmpty()) eventete = result.get(0);
					//eventete = (Rhteltvarent) update.service.get(Rhteltvarent.class, new RhteltvarentPK(evconge.getCdos(), evconge.getAamm(), evconge.getNmat(), evconge.getNbul()));
					if (eventete == null)
					{
						ElementVariableEnteteMois event = new ElementVariableEnteteMois();
						event.setIdEntreprise(evconge.getIdEntreprise());
						event.setAamm(evconge.getAamm());
						event.setNmat(evconge.getNmat());
						event.setNbul(evconge.getNbul());
						//event.setComp_id(new RhteltvarentPK(evconge.getCdos(), evconge.getAamm(), evconge.getNmat(), evconge.getNbul()));
						event.setDdpa(date_debut_mois_ms);
						event.setDfpa(date_fin_mois_ms);
						event.setBcmo("N");
						
						update.service.save(event);
					}
					
					update.service.save(evconge);
				}
			}
		}
		catch (DataAccessException e)
		{
			e.printStackTrace();
			return false;
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public int  getIndexDernierMoisClotureDansListOfPeriodeT91()
	{
		for (int i = 0; i < update.listePeriodesT91.size(); i++)
		{
			if(StringUtils.equals(update.listePeriodesT91.get(i).periode, update.periode))
					return i;
		}
		return -1;
	}

	public int  getIndexMoisCongeDansListOfPeriodeT91(DetailEv detailEv)
	{
		for (int i = 0; i < update.listePeriodesT91.size(); i++)
		{
			if(detailEv.ddeb.compareTo(update.listePeriodesT91.get(i).ddebut) >= 0 &&
					detailEv.ddeb.compareTo(update.listePeriodesT91.get(i).dfin) <= 0)
					return i;
		} 
		return -1;
	}

	/**
	 * =>alim_evcg_ms Generation de conges sur mois suivant par l'appel de la fonction 'pas_moi'
	 * 
	 * @return
	 */
	private boolean alimenteCongeMoisProchain(String nmat)
	{
		if(update.genfile == 'O')  printOut +="\n"+("-----Alimentation des conges sur le mois suivant");

		Date ddpa_ms = null;

		DetailEv evcg = null;
		DetailEv evcg_ms = null;
		for (int i = 0; i < listOfEltVarConge.size(); i++)
		{
			evcg = listOfEltVarConge.get(i);
			if (evcg.mnt1 == 1 && evcg.mnt3 == 0)
			{
				if (tab91)
				{
					int t91_imcg = getIndexMoisCongeDansListOfPeriodeT91(evcg);
					int t91_idmp = getIndexDernierMoisClotureDansListOfPeriodeT91();
					if (t91_imcg - t91_idmp > 1)
					{
//						if (!insertCongeMoisSuivant(evcg, nmat))
//						{
//							return false;
//						}
						evcg.pas_moi_flag = true;

						if ("O".equals(update.fictif) && "B".equals(update.typ_fictif) && evcg.ddeb.compareTo(update.listePeriodesT91.get(t91_imcg).ddebut)>0)
						{
							evcg_ms = evcg; //	-- On fait une copie de l'object courant, pour ne pas alterer ses valeurs
							evcg_ms.dfin = new ClsDate(evcg.ddeb).addDay(-1);
							evcg_ms.ddeb = update.listePeriodesT91.get(t91_imcg).ddebut;
							//
							nbreJourOuvrableNonOuvrable(evcg_ms.ddeb, evcg_ms.dfin);
							evcg_ms.nbja = nbj_a;
							evcg_ms.nbjc = 0;
							//
//							if (!insertCongeMoisSuivant(evcg, nmat))
//							{
//								return false;
//							}
							evcg.pas_moi_flag = true;
							listOfEltVarConge_ms.set(i, evcg_ms);	// -- on fixe l'object � g�n�rer le mois suivant
						}
					}
				}
				else
				{
					am = new ClsDate(evcg.ddeb).getYearAndMonth();

					if (NumberUtils.toLong(am) > NumberUtils.toLong(update.periode))
					{
//						if (!insertCongeMoisSuivant(evcg, nmat))
//						{
//							return false;
//						}
						evcg.pas_moi_flag = true;

						String s = new ClsDate(evcg.ddeb).getYearAndMonth();
						ddpa_ms = new ClsDate(s, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getDate();

						if ("O".equals(update.fictif) && "B".equals(update.typ_fictif) && evcg.ddeb.compareTo(ddpa_ms)>0)
						{
							evcg_ms = evcg;
							//
							evcg_ms.dfin = new ClsDate(evcg.ddeb).addDay(-1);
							evcg_ms.ddeb = ddpa_ms;
							//
							nbreJourOuvrableNonOuvrable(evcg.ddeb, evcg.dfin);
							evcg_ms.nbja = nbj_a;
							evcg_ms.nbjc = 0;

//							if (!insertCongeMoisSuivant(evcg, nmat))
//							{
//								return false;
//							}
							evcg.pas_moi_flag = true;
							listOfEltVarConge_ms.set(i, evcg_ms);	// -- on fixe l'object � g�n�rer le mois suivant
						}
					}
				}
			}
		}

		return true;
	}
	

	/**
	 * =>
	 * <p>
	 * pas_moi
	 * <p>
	 * INSERTION DE CONGES SUR LE MOIS SUIVANT
	 * <p>
	 * Ces absences sont generes par les conges lorsque le libelle 4 de BASE-CONGE est renseigne (variable update.motif_cg_abs_ms).
	 * </p>
	 * <p>
	 * Cette generation se fait lors de la cloture du mois d'imputation des conges.
	 * </p>
	 * <p>
	 * L'appel est fait dans la fonction 'alim_evcg_ms' qui est elle meme declechee lorsque l'on a des conges sur le mois suivant (cgan_ms=TRUE) ET que le.
	 * </p>
	 * <p>
	 * salarie n'est pas radie ET qu'il s'agit du update.bulletin 9.
	 * </p>
	 * <p>
	 * 'l_cg' est le numero de la ligne concernee dans le tableau ou ont ete stockes les mouvements de conges et absences.
	 * </p>
	 * 
	 * @param infoperiode
	 * @param nmat
	 * @return true if tout s'est bien pass�; et false sinon
	 */
	private boolean insertCongeMoisSuivant(DetailEv infoperiode, String nmat)
	{
		if(update.genfile == 'O')  printOut +="\n"+(">>insertCongeMoisSuivant");
		//System.out.println(">>insertCongeMoisSuivant");
		
		Date ddpa = null;
		Date dfpa = null;

		if (ClsObjectUtil.isNull(update.motif_cg_abs_ms))
			return true;

		ElementVariableConge evconge = new ElementVariableConge();
		evconge.setIdEntreprise(new Integer(update.dossier));
		evconge.setAamm(update.prochainmois);
		evconge.setNmat(nmat);
		evconge.setNbul(9);
		evconge.setDdeb(infoperiode.ddeb);
		//evconge.setComp_id(new RhteltvarcongePK(update.dossier, update.prochainmois, nmat, 9, infoperiode.ddeb));
		evconge.setDfin(infoperiode.dfin);
		evconge.setNbjc(new BigDecimal(infoperiode.nbjc));
		evconge.setNbja(new BigDecimal(infoperiode.nbja));
		evconge.setMotf(infoperiode.motf);
		evconge.setMont(infoperiode.mont);
		evconge.setCuti(update.user);
		
		ddpa = date_debut_prochainmois;
		dfpa = date_fin_prochainmois;

		evconge.setMotf(update.motif_cg_abs_ms);

		List<ElementVariableEnteteMois> result = update.service.find("From ElementVariableEnteteMois Where idEntreprise="+evconge.getIdEntreprise()+" And aamm='"+evconge.getAamm()+"' And nmat='"+evconge.getNmat()+"' And nbul="+evconge.getNbul());
		ElementVariableEnteteMois o = null;//update.service.get(Rhteltvarent.class, new RhteltvarentPK(evconge.getCdos(), evconge.getAamm(), evconge.getNmat(), evconge.getNbul()));
		if(result!=null && !result.isEmpty()) o = result.get(0);
		if (o == null)
		{
			ElementVariableEnteteMois event = new ElementVariableEnteteMois();
			CleEltVarEnt pkEv = new CleEltVarEnt(evconge.getIdEntreprise(), evconge.getAamm(), evconge.getNmat(), evconge.getNbul());
			//event.setComp_id(new RhteltvarentPK(evconge.getCdos(), evconge.getAamm(), evconge.getNmat(), evconge.getNbul()));
			event.setIdEntreprise(evconge.getIdEntreprise());
			event.setAamm(evconge.getAamm());
			event.setNmat(nmat);
			event.setNbul(evconge.getNbul());
			event.setDdpa(ddpa);
			event.setDfpa(dfpa);
			event.setBcmo("N");
			//
			
			if(!mapRhteltvarentDejaGenere.containsKey(pkEv))
			{
				update.service.save(event);	
				mapRhteltvarentDejaGenere.put(pkEv, pkEv);
			}	
		}
		//
		List<ElementVariableConge> result2 = update.service.find("From ElementVariableConge Where idEntreprise="+evconge.getIdEntreprise()+" And aamm='"+evconge.getAamm()+"' And nmat='"+evconge.getNmat()+"'");
		ElementVariableConge o2 = null;
		if(result2!=null && !result2.isEmpty()) o2 = result2.get(0);
		//o = update.service.get(Rhteltvarconge.class, evconge.getComp_id());
		if (o2 == null)
			update.service.save(evconge);
		return true;
	}

	/**
	 * =>calc_nb_jours Calcul du nombre de jours ouvrables et non ouvrables entre 2 dates (jours reels)
	 */
	private void nbreJourOuvrableNonOuvrable(Date debut, Date fin)
	{
		if(update.genfile == 'O')  printOut +="\n"+("-----nbreJourOuvrableNonOuvrable");
		
		nbj_a = update.updateservice.computeNbjrAbsences(update.dossier, new ClsDate(debut).getDateS(update.dateformat), new ClsDate(fin).getDateS(update.dateformat));
		
		nbj_c = update.updateservice.computeNbjrConges(update.dossier, new ClsDate(debut).getDateS(update.dateformat), new ClsDate(fin).getDateS(update.dateformat));
	}

	/**
	 * =>maj_conges_annuels Conges payes pris
	 */
	private boolean updateCongeAnnuel(DetailEv ev, Salarie salarie)
	{
		if(update.genfile == 'O')  printOut +="\n"+("-----maj des conges annuels");
		
		double wnbjcad2 = 0;
		double wnbjcad = 0;
		double nbjstot = 0;
		if (pnp)
		{
			salarie.setJapa(new BigDecimal(0));
			salarie.setJapec(new BigDecimal(0));
			salarie.setJrla(new BigDecimal(0));
			salarie.setJrlec(new BigDecimal(0));
			salarie.setJded(new BigDecimal(0));
			salarie.setNbjse(new BigDecimal(0));
			salarie.setNbjsa(new BigDecimal(0));
			salarie.setNbjsm(new BigDecimal(0));
			salarie.setDdcf(null);
			salarie.setDfcf(null);
			salarie.setDdenv(null);
			salarie.setDrenv(null);
			salarie.setNbjtr(new BigDecimal(0));
			salarie.setPmcf("");
			salarie.setNbjcf(new BigDecimal(0));
			salarie.setNbjaf(new BigDecimal(0));
			salarie.setDrtcg(ev.dfin);
			salarie.setDapa(new BigDecimal(0));
			salarie.setDded(new BigDecimal(0));
			salarie.setDdcf(null);
			salarie.setDfcf(null);
			salarie.setJded(new BigDecimal(0));
			salarie.setDapec(new BigDecimal(0));
			salarie.setMtcf(new BigDecimal(0));
		}
		else
		{
			if ("O".equals(update.rhpdo.getDccg()))
			{
				wnbjcad = ev.nbjc;
			}
			else
			{
				wnbjcad = ev.nbja;
			}

			nbjstot = salarie.getNbjse().doubleValue() + salarie.getNbjsa().doubleValue() + salarie.getNbjsm().doubleValue();

			if (wnbjcad >= nbjstot)
			{
				salarie.setNbjse(new BigDecimal(0));
				salarie.setNbjsa(new BigDecimal(0));
				salarie.setNbjsm(new BigDecimal(0));
			}
			else
			{
				wnbjcad2 = nbjstot;
				if (wnbjcad2 >= salarie.getNbjsa().doubleValue())
				{
					wnbjcad2 -= salarie.getNbjsa().doubleValue();
					salarie.setNbjsa(new BigDecimal(0));
					if (wnbjcad2 >= salarie.getNbjse().doubleValue())
					{
						wnbjcad2 -= salarie.getNbjse().doubleValue();
						salarie.setNbjse(new BigDecimal(0));
						if (wnbjcad2 >= salarie.getNbjsm().doubleValue())
						{
							salarie.setNbjsm(new BigDecimal(0));
						}
						else
						{
							salarie.setNbjsm(new BigDecimal(salarie.getNbjsm().doubleValue() - wnbjcad2));
						}
					}
					else
					{
						salarie.setNbjse(new BigDecimal(salarie.getNbjse().doubleValue() - wnbjcad2));
					}
				}
				else
				{
					salarie.setNbjsa(new BigDecimal(salarie.getNbjsa().doubleValue() - wnbjcad2));
				}
			}

			if (wnbjcad <= salarie.getJapa().doubleValue())
			{
				salarie.setJapa(new BigDecimal(salarie.getJapa().doubleValue() - wnbjcad));
			}
			else
			{
				salarie.setJapec(new BigDecimal(salarie.getJapec().doubleValue() - (wnbjcad - salarie.getJapa().doubleValue())));
				salarie.setJapa(new BigDecimal(0));
			}
		
			if(new ClsDate(ev.dfin).getDateS("yyyyMMdd").equalsIgnoreCase(new ClsDate(salarie.getDfcf()).getDateS("yyyyMMdd")))
				salarie.setDrtcg(ev.dfin);
			
			ca = true;

		}

		return true;
	}

	//	---------------------------------------------------------------------------------
	//	-- Mise a jour de la base conges salarie
	//	---------------------------------------------------------------------------------
	private boolean updateBaseCongeAgent(Salarie salarie)
	{
		if(update.genfile == 'O')  printOut +="\n"+("-----maj de la base cong�");
		
		boolean l_expat = false;
		ParamData fnom = null;
		if (bas_con != 0)
		{
			if (!mois_conge)
			{
				if (StringUtils.isBlank(update.val_exp))
				{
					fnom = update._getRhfnom(update.dossier, 35, salarie.getCat(), 3);
					if (fnom != null && fnom.getValt() != null )
					{
						nbj_con = fnom.getValt().doubleValue();
					}
					else
						nbj_con = update.nbjc_lx;
					if(nbj_con <= 0)
						nbj_con = update.nbjc_lx;
				}
				else
				{
					if (update.colonne_expatrie == 1)
					{
						if (update.val_exp.equals(salarie.getRegi()))
							l_expat = true;
					}
					else if (update.colonne_expatrie == 2)
					{
						if (update.val_exp.equals(salarie.getTypc()))
							l_expat = true;
					}
					else if (update.colonne_expatrie == 3)
					{
						if (update.val_exp.equals(salarie.getClas()))
							l_expat = true;
					}
					if (l_expat)
					{
						fnom = update._getRhfnom(update.dossier, 35, salarie.getCat(), 5);
						if (fnom != null && fnom.getValt() != null)
							nbj_con = fnom.getValt().doubleValue();
						else
							nbj_con = update.nbjc_ex;
						if(nbj_con <= 0)
							nbj_con = update.nbjc_ex;
					}
					//	           ELSE
					else
					{
						fnom = (update._getRhfnom(update.dossier, 35, salarie.getCat(), 3));
						if (fnom != null && fnom.getValt() != null)
							nbj_con = fnom.getValt().doubleValue();
						else
							nbj_con = update.nbjc_lx;
						if(nbj_con <= 0)
							nbj_con = update.nbjc_lx;
					}
				}
			}
			else
			{
				nbj_con = 0;
			}
			
			calculProrataNbreJourDroitParMois();
		}
		if (radie)
		{
			salarie.setCals("N");
			salarie.setJapa(new BigDecimal(0));
			salarie.setJapec(new BigDecimal(0));
			salarie.setJrla(new BigDecimal(0));
			salarie.setJrlec(new BigDecimal(0));
			salarie.setJded(new BigDecimal(0));
			salarie.setNbjse(new BigDecimal(0));
			salarie.setNbjsa(new BigDecimal(0));
			salarie.setNbjsm(new BigDecimal(0));
			salarie.setDdcf(null);
			salarie.setDfcf(null);
			salarie.setDdenv(null);
			salarie.setDrenv(null);
			salarie.setNbjtr(new BigDecimal(0));
			salarie.setPmcf("");
			salarie.setNbjcf(new BigDecimal(0));
			salarie.setNbjaf(new BigDecimal(0));
			salarie.setDrtcg(null);
			salarie.setDapa(new BigDecimal(0));
			salarie.setDded(new BigDecimal(0));
			salarie.setDdcf(null);
			salarie.setDfcf(null);
			salarie.setJded(new BigDecimal(0));
			salarie.setDapec(new BigDecimal(0));
			salarie.setMtcf(new BigDecimal(0));
			
			sup_prets17_flag = true;
			
//			salarie.update_flag = true
			
			return true;
		}

		ClsDate anniv = new ClsDate(date_anniv);
		
		if ((update.myPeriode.getYear() != anniv.getYear()) && (update.myPeriode.getMonth() == anniv.getMonth()))
		{
			if(StringUtils.equalsIgnoreCase(globalUpdate.nomClient, ClsEntreprise.BGFIGE)){
				salarie.setJapec(new BigDecimal(jr_cg_ex));
				salarie.setJapa(new BigDecimal(jr_cg_ant));
			} else {
			salarie.setJapa(salarie.getJapa().add(salarie.getJapec()));
			salarie.setJapec(new BigDecimal(0));
			salarie.setDapa(salarie.getDapa().add(salarie.getDapec()));
			salarie.setDapec(new BigDecimal(0));
			salarie.setJrla(salarie.getJrla().add(salarie.getJrlec()));
			salarie.setJrlec(new BigDecimal(0));
			nbj_enf = 0;
			nbj_anc = 0;
			nbj_deco = 0;
			
			if(StringUtil.in(update.calcul_auto_nbjsup, "A,O"))
			{
				retour = calculateNbreJourSupplementaire(salarie);
			}
			if ("O".equals(update.calcul_auto_nbjsup))
			{
				salarie.setJapec(salarie.getJapec().add(new BigDecimal(nbj_con + nbj_anc + nbj_enf + nbj_deco)));
			}
			else if ("A".equals(update.calcul_auto_nbjsup))
			{
				if(StringUtil.notEquals(globalUpdate.nomClient, ClsEntreprise.BGFIGE))
					salarie.setJapa(salarie.getJapa().add(new BigDecimal(nbj_anc + nbj_enf + nbj_deco)));
				salarie.setJapec(salarie.getJapec().add(new BigDecimal(nbj_con)));
			}
			else
			{
				salarie.setJapec(salarie.getJapec().add(new BigDecimal(nbj_con)));
			}
			salarie.setNbjse(salarie.getNbjse().add(new BigDecimal(nbj_enf)));
			salarie.setNbjsa(salarie.getNbjsa().add(new BigDecimal(nbj_anc)));
			salarie.setNbjsm(salarie.getNbjsm().add(new BigDecimal(nbj_deco)));
			}
		}
		else
		{
			if(StringUtils.equalsIgnoreCase(globalUpdate.nomClient, ClsEntreprise.BGFIGE)){
				salarie.setJapec(new BigDecimal(jr_cg_ex));
				salarie.setJapa(new BigDecimal(jr_cg_ant));
			} else salarie.setJapec(salarie.getJapec().add(new BigDecimal(nbj_con)));
		}

		nbj_con = 0;

		if (conges_annuels && finconge)
		{
			if ("O".equals(update.raz_bc_jour))
			{
				if(StringUtil.notEquals(globalUpdate.nomClient, ClsEntreprise.BGFIGE))
					salarie.setJapa(new BigDecimal(0));
				salarie.setJapec(new BigDecimal(0));
			}

			salarie.setDapa(new BigDecimal(0));
			salarie.setDded(new BigDecimal(0));
			salarie.setDfcf(null);
			salarie.setDdcf(null);
			salarie.setNbjcf(new BigDecimal(0));
			salarie.setNbjaf(new BigDecimal(0));
			salarie.setJded(new BigDecimal(0));
			salarie.setPmcf("");

			salarie.setNbjtr(new BigDecimal(nbjtr));

			if ("O".equals(update.fictif) && "A".equals(update.typ_fictif))
			{
				salarie.setDapec(new BigDecimal(bas_con));
				salarie.setNbjtr(new BigDecimal(nbjtr));
			}
			else
			{
				salarie.setDapec(new BigDecimal(0));
				salarie.setNbjtr(new BigDecimal(0));
			}
	
			if ("O".equals(update.ajout_bc))
			{
				salarie.setDapec(salarie.getDapec().add(salarie.getMtcf().add(new BigDecimal(conge))));
			}

			salarie.setMtcf(new BigDecimal(0));
		}
		else
		{
			if (conges_annuels && pnp && (!ca))
			{
				salarie.setDapa(new BigDecimal(0));
				salarie.setDded(new BigDecimal(0));
				salarie.setJded(new BigDecimal(0));
				//
				salarie.setNbjtr(new BigDecimal(nbjtr));
				salarie.setDapec(new BigDecimal(bas_con));
				//
				if ("O".equals(update.ajout_bc))
				{
					salarie.setDapec(salarie.getDapec().add(new BigDecimal(conge)));
				}
			}
			else
			{
				if (salarie.getNbjtr().add(new BigDecimal(nbjtr)).doubleValue() < 999)
				{
					salarie.setNbjtr(new BigDecimal(nbjtr).add(salarie.getNbjtr()));
				}
				salarie.setDapec(new BigDecimal(bas_con).add(salarie.getDapec()));
				salarie.setDded(new BigDecimal(moncp).add(salarie.getDded()));
				//
				if ("O".equals(update.ajout_bc))
				{
					salarie.setMtcf(new BigDecimal(conge).add(salarie.getMtcf()));
				}
			}
		}
//		update_flag = true;
		return true;
	}
	
	//	---------------------------------------------------------------------------------
	//	-- Calcul du nombre de jours supplementaires
	//	---------------------------------------------------------------------------------
	private boolean calculateNbreJourSupplementaire(Salarie salarie)
	{
		if(update.genfile == 'O')  printOut +="\n"+("-----calcul du nombre de jours suppl");
		
		try
		{

			int nb_enf = 0;
			double nbj_par_enf = 0;
			int l_tranche = 0;


			ClsUpdateBulletin.RhfnomCalcPartFiscal fnom = null;
			ClsDate ddca = new ClsDate(salarie.getDdca());
			int x_an = update.myPeriode.getYear();
			int x_mois =  update.myPeriode.getMonth();
			int wmois = x_an - ddca.getYear();
			wmois = (wmois * 12) + x_mois;
			wmois = wmois - ddca.getMonth();
			int nbr_an = new BigDecimal(Math.floor(wmois / 12)).intValue();
			//
			ancien = nbr_an;
			if(ancien < 0)
				ancien = 0;
			fnom = update.getRhfnomCalcPartFiscal(salarie.getCat());
			if(fnom.montant3 <= 0) fnom.montant3 = 1;
			if(fnom.taux1 <= 0) fnom.taux1 = 0;
			if(fnom.taux2 <= 0) fnom.taux2 = 0;
			l_tranche = new BigDecimal(Math.ceil(ancien / fnom.montant3)).intValue();
			nbj_anc = l_tranche * fnom.taux1;
			nbj_par_enf = fnom.taux2;

			String char2 = ClsStringUtil.formatNumber(ancien, "00");
			ParamData ofnom = update._getRhfnom(update.dossier, 34, char2, 2);
			fnom.taux2 = 0;
			if (ofnom != null && ofnom.getValt() != null)
			{
				fnom.taux2 = ofnom.getValt().intValue();
			}

			nbj_anc = nbj_anc + fnom.taux2;

			if ("F".equals(salarie.getSexe()))
			{
				if (update.comptage_enfant)
				{
					ClsDate sysdate = new ClsDate(new Date());
					String sdate = ClsStringUtil.formatNumber(sysdate.getDay(), "00") + "-" + ClsStringUtil.formatNumber(sysdate.getMonth(), "00") + "-" + ClsStringUtil.formatNumber((sysdate.getYear() - update.age_max_enfant), "0000");
					Date date_min = new ClsDate(sdate, "dd-MM-yyyy").getDate();
					//
					nb_enf = update.updateservice.getNbEnfantsSalarie(update.dossier, salarie.getNmat(), new ClsDate(date_min, this.update.dateformat).getDateS());
				}
				else
				{
					nb_enf = salarie.getNbec();
				}
				//
				if (nbj_enf < update.minimum_enfant)
				{
					nbj_enf = 0;
				}
				else
				{
					nbj_enf = nbj_par_enf * (nb_enf - (update.minimum_enfant - 1));
				}
			}
			nbj_deco = update.updateservice.getNbJrsDecoration(update.dossier, salarie.getNmat());
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	//	---------------------------------------------------------------------------------
	//	-- Calcul du proratas sur les jours de droits acquis sur le mois
	//	---------------------------------------------------------------------------------
	private boolean calculProrataNbreJourDroitParMois()
	{
		if(update.genfile == 'O')  printOut +="\n"+("-----calcul du prorata sur nbr de jours");
		if (update.pror_jcg_normal)
		{
			if ("O".equals(update.fictif) && "A".equals(update.typ_fictif))
			{
				if ("O".equals(update.pror_jr))
					nbj_con = (nbj_con * nbjtr) / 30;
			}
			else
			{
				if (update.periode.equals(sav_pmcf))
				{
					if (sav_dfcf.compareTo(update.myPeriode.getLastDayOfMonth())>=0)
						nbj_con = 0;
					else
						nbj_con = nbj_con * (30 - new ClsDate(sav_dfcf).getDay()) / 30;
				}
				else
				{
					if ("O".equals(update.pror_jr))
						nbj_con = (nbj_con * nbjtr) / 30;
				}
			}
		}
		else
		{
			int i1 = Integer.valueOf(update.dbcg_barem1);
			int i2 = Integer.valueOf(update.fincg_barem1);
			int i3 = Integer.valueOf(update.dbcg_barem2);
			int i4 = Integer.valueOf(update.fincg_barem2);
			int i5 = Integer.valueOf(update.dbcg_barem3);
			int i6 = Integer.valueOf(update.fincg_barem3);
			if (nbjtr >= i1 && nbjtr <= i2)
				nbj_con = nbj_con * update.nbjcg_barem1 / 100;
			else if (nbjtr >= i3 && nbjtr <= i4)
				nbj_con = nbj_con * update.nbjcg_barem2 / 100;
			else if (nbjtr >= i5 && nbjtr <= i6)
				nbj_con = nbj_con * update.nbjcg_barem3 / 100;
			else
				nbj_con = 0;
		}
		return true;
	}

	//	---------------------------------------------------------------------------------
	//	-- Suppression des EVs du mois courant
	//	---------------------------------------------------------------------------------
	private boolean deleteEVMois(Salarie salarie)
	{
		if(update.genfile == 'O')  printOut +="\n"+("-----suppression des ev du mois");
		
		if(update.genfile == 'O')  printOut +="\n"+(">>deleteEVMois");
		boolean result = true;
		Session session = update.service.getSession();
		try
		{
			
			result = update.updateservice.deleteEVMois(session, update.dossier, update.periode, update.numerobulletin, salarie.getNmat());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			result = false;
		}
		finally
		{
			update.service.closeSession(session);
		}
		return result;
	}

	//	---------------------------------------------------------------------------------
	//	-- Suppression des elements fixes
	//	---------------------------------------------------------------------------------
	private boolean deleteEFMois(Salarie salarie)
	{
		if(update.genfile == 'O')  printOut +="\n"+("-----suppression des elt fixes du mois");
		Session session = update.service.getSession();
		try
		{
			return update.updateservice.deleteEFMois(session, update.dossier, update.myPeriode.getDateS(update.dateformat), salarie.getNmat());
		}
		catch (Exception e)
		{
			return false;
		}
		finally
		{
			update.service.closeSession(session);
		}
	}

	//	---------------------------------------------------------------------------------
	//	--     MISE A JOUR DES ZONES CONCERNANT LES CONGES
	//	---------------------------------------------------------------------------------
	private boolean updateZoneConge(Salarie salarie)
	{
		if(update.genfile == 'O')  printOut +="\n"+("-----maj des zones conges");

		DetailEv ev = null;
		for (int i = 0; i < listOfEltVarConge.size(); i++)
		{
			ev = (DetailEv) listOfEltVarConge.get(i);
			if (ev.mnt1 == 0)
			{
				if (StringUtils.equals(update.motif_reliq, ev.motf))
					retour = updateJourDesJourReliquat(salarie, ev);
				
				if (!update.motif_cg_abs_ms.equals(ev.motf))
				{
//					retour = updateDetailAbsConge(ev, salarie.getNmat());
					ev.maj_det_flag = true;
				}
				
			}
			else
			{
				if (ev.mnt3 == 0)
					conges_annuels = true;
				else if (ev.mnt3 == 1)
					conges_annuels = false;

				if ("N".equals(update.fictif) || ("O".equals(update.fictif) && "B".equals(update.fictif)))
				{
					if (update.periode.equals(salarie.getPmcf()))
					{
//						retour = updateDetailAbsConge(ev, salarie.getNmat());
						ev.maj_det_flag = true;
						//			            -- Les jours sont tous defalques pour etre en phase avec les droits
						if (conges_annuels)
						{
							retour = updateCongeAnnuel(ev, salarie);
							finconge = true;
						}
						else
						{
							retour = updateCongePayePonctuel(ev, salarie);
						}
					}
				}
				else
				{
					if (ev.dfin.compareTo(ev.dfpa) <= 0)
					{
//						retour = updateDetailAbsConge(ev, salarie.getNmat()); // -- Les jours sont tous defalques pour etre en phase avec les droits
						ev.maj_det_flag = true;
						
						if (conges_annuels)
						{
							retour = updateCongeAnnuel(ev, salarie);
						}
						else
						{
							retour = updateCongePayePonctuel(ev, salarie);
						}
						//
						if (!finconge)
						{
//							if (ev.dfin.equals(salarie.getDfcf()) && conges_annuels)
							if(new ClsDate(ev.dfin).getDateS("yyyyMMdd").equalsIgnoreCase(new ClsDate(salarie.getDfcf()).getDateS("yyyyMMdd")) && conges_annuels)
								finconge = true;
						}
					}
				}
			}
		}
		return true;
	}

	//	---------------------------------------------------------------------------------
	//	-- Mise a jour des jours de reliquat
	//	---------------------------------------------------------------------------------
	private boolean updateJourDesJourReliquat(Salarie salarie, DetailEv ev)
	{
		if(update.genfile == 'O')  printOut +="\n"+("-----maj du nb de jours de reliquat");
		
		Integer o = update.updateservice.countNbJourReliquat(update.dossier, new ClsDate(ev.ddeb).getDateS(update.dateformat) , new ClsDate(ev.dfin).getDateS(update.dateformat) );
		salarie.setJrla(new BigDecimal(NumberUtils.nvl(salarie.getJrla(), 0).longValue() - o));
		return true;
	}

	//	---------------------------------------------------------------------------------
	//	-- Mise a jour du detail des conges et absences
	//	---------------------------------------------------------------------------------
	private boolean updateDetailAbsConge(DetailEv ev, String nmat)
	{
		if(update.genfile == 'O')  printOut +="\n"+("-----updateDetailAbsConge");
		
		HistoCongeSalarie conge = new HistoCongeSalarie();
		conge.setIdEntreprise(new Integer(update.dossier));
		conge.setNmat(nmat);
		conge.setDdcg(ev.ddeb);
		//(new RhtcongeagentPK(update.dossier, nmat, ev.ddeb));

		conge.setCmcg(ev.motf);
		conge.setDfcg(ev.dfin);
		conge.setMtcg(ev.mont);
		conge.setNbja(new BigDecimal(ev.nbja));
		conge.setNbjc(new BigDecimal(ev.nbjc));
		
		update.service.save(conge);

		return true;
	}

	//	---------------------------------------------------------------------------------
	//	-- Conges payes ponctuels
	//	-- Les jours de conges PONCTUELS sont deduits des jours a prendre
	//	--     et stockes dans la zone des jours deductibles.
	//	---------------------------------------------------------------------------------
	private boolean updateCongePayePonctuel(DetailEv ev, Salarie salarie)
	{
		if(update.genfile == 'O')  printOut +="\n"+("-----maj coge ponctuel");
		double wnbjcad = 0;
		
		if ("O".equals(update.rhpdo.getDccg()))
			wnbjcad = ev.nbjc;
		else
			wnbjcad = ev.nbja;
		
		if (salarie.getJded() != null)
			salarie.setJded(salarie.getJded().add(new BigDecimal(wnbjcad)));
		else
			salarie.setJded(new BigDecimal(wnbjcad));

		if (wnbjcad <= salarie.getJapa().doubleValue())
		{
			if (salarie.getJapa() != null)
				salarie.setJapa(salarie.getJapa().subtract(new BigDecimal(wnbjcad)));
			else
				salarie.setJapa(new BigDecimal(-1 * wnbjcad));
		}
		else
		{
			double i = salarie.getJapa().doubleValue();
			if (salarie.getJapec() != null)
				salarie.setJapec(salarie.getJapec().subtract(new BigDecimal(wnbjcad - i)));
			else
				salarie.setJapec(new BigDecimal(i - wnbjcad));
			//
			salarie.setJapa(new BigDecimal(0));
		}
		return true;
	}

	//	---------------------------------------------------------------------------------
	//	-- Calcul du nombre de part fiscal automatiquement et maj de la fiche sal.
	//	-- en fonction de age maxi
	//	---------------------------------------------------------------------------------
	
	private boolean calculPartFiscale(Salarie salarie)
	{
		if(update.genfile == 'O')  printOut +="\n"+("-----calculPartFiscale");
		@SuppressWarnings("unchecked")
		List l = update.updateservice.getDtnaEnfantsSalarie(update.dossier, salarie.getNmat());

		int enfant_a_charge = 0;
		Date curdate = update.myPeriode.getLastDayOfMonth();
		Date dtna = null;
		long age_enfant = 0;
		for (Object obj : l)
		{
			if(obj != null)
			{
				dtna = (Date) obj;
				age_enfant = ClsDate.getNumberOfDay(curdate, dtna) / 365;
				if (age_enfant <= update.age_max_fiscal)
					enfant_a_charge++;
			}
		}
		
		ClsUpdateBulletin.RhfnomNbParts fnom = update.getRhfnomNbParts(salarie.getSitf());
		double nbpt = 1;
		nbpt += fnom.taux1;
		if (enfant_a_charge >= 1)
		{
			nbpt += fnom.taux2;
			if (enfant_a_charge >= 2)
			{
				nbpt += fnom.taux3;
				if (enfant_a_charge >= 3)
				{
					nbpt += fnom.taux4;
					if (enfant_a_charge >= 4)
					{
						nbpt += fnom.taux5;
						if (enfant_a_charge >= 5)
						{
							nbpt += fnom.taux6;
							if (enfant_a_charge >= 6)
							{
								nbpt += fnom.taux7;
								if (enfant_a_charge >= 7)
								{
									nbpt += fnom.taux8;
								}
							}
						}
					}
				}
			}
		}

		if (nbpt > update.max_part_fisc && update.max_part_fisc != 0)
			nbpt = update.max_part_fisc;
		
		if (nbpt != salarie.getNbpt().doubleValue())
		{
			salarie.setNbpt(new BigDecimal(nbpt));
			
//			update_flag = true;
		}
		return true;
	}


	
	
	public void agentUpdateToString()
	{
		
		String salarieString="\n->Japa = "+salarie.getJapa();
		salarieString+="\n->Japec = "+salarie.getJapec();
		salarieString+="\n->Jded = "+salarie.getJded();
		salarieString+="\n->Jrla = "+salarie.getJrla();
		salarieString+="\n->Jrlec = "+salarie.getJrlec();
		salarieString+="\n->Dapa = "+salarie.getDapa();
		salarieString+="\n->Dapec = "+salarie.getDapec();
		String texte = printOut + StringUtil.toString(this) + salarieString;
		update.printOutSalarie(salarie.getNmat(), texte);
	}
	
    /*
	public void agentUpdateToString()
	{
		ClsParameter.println("------------------------------" + salarie.getNmat() + " " + salarie.getNom() + " " + salarie.getPren() + "-------------------------------------------------------------------------------");
		ClsParameter.println(StringUtils.toString(salarie));
		ClsParameter.println("boolean tab91 = " + tab91);
		ClsParameter.println("double bas_con = " + bas_con);
		ClsParameter.println("double moncp = " + moncp);
		ClsParameter.println("int nbjcp = " + nbjcp);
		ClsParameter.println("double conge = " + conge);
		ClsParameter.println("int nbjtr = " + nbjtr);
		ClsParameter.println("boolean cgan_ms = " + cgan_ms);
		ClsParameter.println("boolean exist_evcg = " + exist_evcg);
		ClsParameter.println("int nbjreliq = " + nbjreliq);
		ClsParameter.println("boolean mois_conge = " + mois_conge);
		ClsParameter.println("boolean radie = " + radie);
		ClsParameter.println("boolean radie_depuis = " + radie_depuis);
		ClsParameter.println("boolean continuer = " + continuer);
		ClsParameter.println("boolean retour = " + retour);
		ClsParameter.println("String am =  " + am);
		ClsParameter.println("boolean conges_annuels = " + conges_annuels);
		ClsParameter.println("boolean finconge = " + finconge);
		ClsParameter.println("boolean pnp = " + pnp);
		ClsParameter.println("int nbj_a = " + nbj_a);
		ClsParameter.println("int nbj_c = " + nbj_c);
		ClsParameter.println("boolean ca = " + ca);
		ClsParameter.println("int nbj_con = " + nbj_con);
		ClsParameter.println("String sav_pmcf = " + sav_pmcf);
		ClsParameter.println("Date sav_dfcf = " + sav_dfcf);
		ClsParameter.println("Date sav_ddcf = " + sav_ddcf);
		ClsParameter.println("Date date_anniv = " + date_anniv);
		ClsParameter.println("int nbj_enf = " + nbj_enf);
		ClsParameter.println("int nbj_anc = " + nbj_anc);
		ClsParameter.println("int nbj_deco = " + nbj_deco);
		ClsParameter.println("int ancien = " + ancien);

	}
	*/
	
	/***************************   LISTE DES GETTERS POUR AVOIR LES VALEURS DES DIFFERENTES PROPRIETES DE L'OBJECT *********************/


	public Salarie getSalarie()
	{
		return salarie;
	}

	public boolean getTab91()
	{
		return tab91;
	}

	public double getBas_con()
	{
		return bas_con;
	}

	public double getMoncp()
	{
		return moncp;
	}

	public double getNbjcp()
	{
		return nbjcp;
	}

	public double getConge()
	{
		return conge;
	}

	public double getNbjtr()
	{
		return nbjtr;
	}

	public boolean getCgan_ms()
	{
		return cgan_ms;
	}

	public boolean getExist_evcg()
	{
		return exist_evcg;
	}

	public double getNbjreliq()
	{
		return nbjreliq;
	}

	public boolean getMois_conge()
	{
		return mois_conge;
	}

	public boolean getRadie()
	{
		return radie;
	}

	public boolean getRadie_depuis()
	{
		return radie_depuis;
	}

	public String getAm()
	{
		return am;
	}

	public boolean getConges_annuels()
	{
		return conges_annuels;
	}

	public boolean getFinconge()
	{
		return finconge;
	}

	public boolean getPnp()
	{
		return pnp;
	}

	public double getNbj_a()
	{
		return nbj_a;
	}

	public double getNbj_c()
	{
		return nbj_c;
	}

	public boolean getCa()
	{
		return ca;
	}

	public double getNbj_con()
	{
		return nbj_con;
	}

	public String getSav_pmcf()
	{
		return sav_pmcf;
	}

	public Date getSav_dfcf()
	{
		return sav_dfcf;
	}

	public Date getSav_ddcf()
	{
		return sav_ddcf;
	}

	public Date getDate_anniv()
	{
		return date_anniv;
	}

	public Date getDate_debut_mois_ms()
	{
		return date_debut_mois_ms;
	}

	public Date getDate_fin_mois_ms()
	{
		return date_fin_mois_ms;
	}

	public Date getDate_debut_prochainmois()
	{
		return date_debut_prochainmois;
	}

	public Date getDate_fin_prochainmois()
	{
		return date_fin_prochainmois;
	}

	public double getNbj_enf()
	{
		return nbj_enf;
	}

	public double getNbj_anc()
	{
		return nbj_anc;
	}

	public double getNbj_deco()
	{
		return nbj_deco;
	}

	public int getAncien()
	{
		return ancien;
	}

	public int getNumsalariecourant()
	{
		return numsalariecourant;
	}

	public int getNbsalarietotal()
	{
		return nbsalarietotal;
	}

	public boolean getContinuer()
	{
		return continuer;
	}

	public boolean getRetour()
	{
		return retour;
	}

	public String getError()
	{
		return error;
	}

	public Map<CleCumulPaie, ClsCumul> getCumulsMap()
	{
		return cumulsMap;
	}

	public List<CalculPaie> getListeCalculPaies()
	{
		return listeCalculPaies;
	}

	public List<Calcul> getListeCalculs()
	{
		return listeCalculs;
	}

	public List<DetailEv> getListOfEltVarConge()
	{
		return listOfEltVarConge;
	}

	public List<DetailEv> getListOfEltVarConge_ms()
	{
		return listOfEltVarConge_ms;
	}

	public boolean getDeleteEVMois_flag()
	{
		return deleteEVMois_flag;
	}

	public boolean getDeleteEFMois_flag()
	{
		return deleteEFMois_flag;
	}

	public boolean getTransferEVMoisSuivant_flag()
	{
		return transferEVMoisSuivant_flag;
	}

	public boolean getTransferEcheancePretMoisSuivant_flag()
	{
		return transferEcheancePretMoisSuivant_flag;
	}

	public boolean getMaj_cp_flag()
	{
		return maj_cp_flag;
	}

	public boolean getCalc_pror_jcg()
	{
		return calc_pror_jcg;
	}

	public boolean getSup_prets17_flag()
	{
		return sup_prets17_flag;
	}

	public boolean getCalc_nbjsup_flag()
	{
		return calc_nbjsup_flag;
	}

	public boolean getSup_fic_flag()
	{
		return sup_fic_flag;
	}

	public boolean getUpdate_flag()
	{
		return update_flag;
	}
	
	

}
