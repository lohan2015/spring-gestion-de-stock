package com.kinart.paie.business.services.cloture;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataAccessException;

@SuppressWarnings("unused")
public class ClsAgentUpdateBulletin
{
	public ClsGlobalUpdate globalUpdate;

	public Session oSession = null;

	public Salarie salarie = null;

	public boolean tab91 = true;

	public double bas_con = 0;

	public double jr_cg_ant = 0;

	public double jr_cg_ex = 0;

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

	public String error = "";

	private Map<CleCumulPaie, ClsCumul> cumulsMap = new HashMap<CleCumulPaie, ClsCumul>();

	private List<CalculPaie> listeCalculPaies = new ArrayList<CalculPaie>();

	public List<DetailEv> listOfEltVarConge = new ArrayList<DetailEv>(); // -- Liste initiale a ne pas modifier

	public List<DetailEv> listOfEltVarConge_ms = new ArrayList<DetailEv>(); // -- Liste des rhteltvarconge é générer le mois suivant

	private ClsUpdateBulletin update;

	public boolean deleteEVMois_flag = false;

	public boolean deleteEFMois_flag = false;

	public boolean transferEVMoisSuivant_flag = false;

	public boolean transferEcheancePretMoisSuivant_flag = false;

	// public boolean maj_conges_annuels_flag = false;

	public boolean maj_cp_flag = false;

	public boolean calc_pror_jcg = false;

	public boolean sup_prets17_flag = false;

	public boolean calc_nbjsup_flag = false;

	public boolean sup_fic_flag = false;

	public boolean update_flag = false;

	public String printOut = null;

	public List<Calcul> listeCalculs = new ArrayList<Calcul>();

	private Map<CleEltVarEnt, CleEltVarEnt> mapElementVariableEnteteMoisDejaGenere = new HashMap<CleEltVarEnt, CleEltVarEnt>();

	private Map<CleEltVarEnt, Boolean> mapElementVariableEnteteMoisDejaRecherche = new HashMap<CleEltVarEnt, Boolean>();

	private Map<CleEltVarDet, CleEltVarDet> mapElementVariableDetailMoisDejaGenere = new HashMap<CleEltVarDet, CleEltVarDet>();

	private Map<CleEltVarConge, CleEltVarConge> mapRhteltvarcongeDejaGenere = new HashMap<CleEltVarConge, CleEltVarConge>();

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

	public ClsAgentUpdateBulletin(ClsUpdateBulletin update, Session oSession, Salarie salarie)
	{
		this.update = update;
		this.oSession = oSession;
		this.salarie = salarie;
		this.globalUpdate = update.globalUpdate;
	}

	private boolean initSalarie() throws Exception
	{
		refreshConnexion();
		if (update.genfile == 'O')
			printOut += "\n" + ("-----Initialisation du salarié");

		// on determine le max du numero de ligne des elts var det pour le mois suivant
		// (dans certains cas il peut exister avant cloture des lignes pour le mois suivant

		maj_eva_nlig = getMaxNumLigneEltVar() + 1;

		sav_pmcf = salarie.getPmcf();
		sav_dfcf = salarie.getDfcf();
		sav_ddcf = salarie.getDdcf();

		// ----------Date d'anniversaire du salarié--------------------
		if (update.type_dtanniv == 1)
			date_anniv = salarie.getDtes();
		else if (update.type_dtanniv == 2)
			date_anniv = salarie.getDdca();
		else if (update.type_dtanniv == 3)
			date_anniv = salarie.getDrtcg();
		else if (update.type_dtanniv == 4)
		{
			Integer month = 12;
			ParamData fnom = this.update.updateservice.chercherEnNomenclature(update.dossier, 99, "ANNIV-CG", 2);
			if (fnom != null && fnom.getValm() != null)
				month = fnom.getValm().intValue();
			Integer year = update.myPeriode.getYear() - 1;
			date_anniv = new ClsDate("01" + ClsStringUtil.formatNumber(month, "00") + year, "ddMMyyyy").getDate();
		}

		// ----------Gestion du code horaire du salarié--------------------
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
		// ----------Date de début et de fin pour les congés-----------------
		if (tab91)
		{
			date_debut_mois_ms = update.date_debut_mois_ms;
			date_fin_mois_ms = update.date_fin_mois_ms;

			date_debut_prochainmois = update.date_debut_prochainmois;
			date_fin_prochainmois = update.date_fin_prochainmois;

			if(date_debut_mois_ms == null || date_fin_mois_ms == null)
			{
				error = update.updateservice.errorMessage("ERR-90131", update.mois_ms);
				globalUpdate._setEvolutionTraitement(update.request, error, true);
				//return false;
				throw new Exception(error);

			}
		}
		else
		{
			date_debut_mois_ms = new ClsDate(update.mois_ms, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getFirstDayOfMonth();
			date_fin_mois_ms = new ClsDate(date_debut_mois_ms).getLastDayOfMonth();

			date_debut_prochainmois = new ClsDate(update.prochainmois, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getFirstDayOfMonth();
			date_fin_prochainmois = new ClsDate(date_debut_prochainmois).getLastDayOfMonth();

		}

		// ----------Determination si le salarié radié ou muté------------------
		radie = false;
		radie_depuis = false;
		if (StringUtil.equals("MU", salarie.getMrrx()) || StringUtil.equals("RA", salarie.getMrrx()))
		{
			if (salarie.getDmrr() != null && update.numerobulletin == 9)
			{
				String perioderadiation = new ClsDate(salarie.getDmrr()).getYearAndMonth();
				if (StringUtil.equals(update.periode, perioderadiation))
					radie = true;
				else
				{
					if (NumberUtils.toLong(update.periode) > NumberUtils.toLong(perioderadiation))
						radie_depuis = true;
				}
			}
		}

		// ----------Zones nulles servant dans les conges------------------
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

		// ----------------------Zones libres pour le calcul du nombre de parts
		if (salarie.getNbec() == null)
			salarie.setNbec(0);
		if (salarie.getNbef() == null)
			salarie.setNbef(0);
		if (salarie.getNbfe() == null)
			salarie.setNbfe(0);
		if (salarie.getNbcj() == null)
			salarie.setNbcj(0);
		if (salarie.getNbpt() == null)
			salarie.setNbpt(new BigDecimal(0));

		return true;
	}

	@SuppressWarnings("unchecked")
	private int getMaxNumLigneEltVar() throws Exception
	{
		String queryString = "select max(nlig) From ElementVariableDetailMois  where idEntreprise=" + "'" + update.dossier + "'" + " and  nmat=" + "'" + salarie.getNmat()
				+ "' and nbul =" + update.numerobulletin;
		List result = null;
		//Sous informix, on ne peux pas selectionner une colonne d'une clé avec une autre session alors qu'une session en cours existe
		if(TypeBDUtil.IN.equalsIgnoreCase(TypeBDUtil.typeBD))
			result = oSession.createQuery(queryString).list();
		else
			result = update.service.findByQuery(queryString);

		if (result.size() > 0)
			return Integer.valueOf(result.get(0) == null ? "0" : result.get(0).toString());
		else return 0;
	}

	// pour un salarié non calculé, on doit transferer ses éléments sur le mois suivant

	public boolean updateBulletin()
	{
		if(StringUtil.equals("420013", salarie.getNmat()))
			System.out.println("Salarié 420011");
		//ParameterUtil.println("Updating " + this.getAgent().getNmat());
		try
		{
			refreshConnexion();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			globalUpdate._setEvolutionTraitement(update.request, ClsTreater._getStackTrace(e), true);
			globalUpdate.logError("Mat : " + this.salarie.getNmat() + "\r\n" + ClsTreater._getStackTrace(e));
			return false;
		}
		if (update.genfile == 'O')
			printOut += "\n" + ("-----Update Bulletin Salarié");

		try
		{
			continuer = false;
			try
			{
				if (!initSalarie())
					return false;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				globalUpdate._setEvolutionTraitement(update.request, ClsTreater._getStackTrace(e), true);
				globalUpdate.logError("Mat : " + this.salarie.getNmat() + "\r\n" + ClsTreater._getStackTrace(e));
				return false;
			}

			if (StringUtil.equals("N", salarie.getCals()))
			{
				if (radie_depuis || !radie)
				{
					// retour = deleteEVMois(salarie);
					deleteEVMois_flag = true;
					continuer = true;
				}
				if (!radie)
				{
					// retour = deleteEFMois(salarie);
					deleteEFMois_flag = true;
					// if (!transferEVMoisSuivant(salarie.getNmat()))
					// return false;
					transferEVMoisSuivant_flag = true;
					if (update.numerobulletin == 9)
					{
						// if (!transferEVCGMoisSuivant(salarie.getNmat()))
						// return false;
						chargerEVCGMoisCourant(salarie.getNmat());

						// if (!transferEcheancePretMoisSuivant(salarie.getNmat()))
						// return false;
						transferEcheancePretMoisSuivant_flag = true;
					}

					continuer = true;
				}
			}
			if (continuer == false)
			{
				if(!lectureCalculSurMoisCourant(salarie))
					return false;

				int count = listeCalculPaies.size();
				if (count == 0)
				{
					// retour = transferEVMoisSuivant(salarie.getNmat());
					transferEVMoisSuivant_flag = true;
					if (update.numerobulletin == 9)
					{
						// retour = transferEVCGMoisSuivant(salarie.getNmat());
						// retour = transferEcheancePretMoisSuivant(salarie.getNmat());
						transferEcheancePretMoisSuivant_flag = true;
					}
				}

				if(!chargerEVCGMoisCourant(salarie.getNmat())) return false;

				if (cgan_ms && !radie && update.numerobulletin == 9)
				{
					if (!alimenteCongeMoisProchain(salarie.getNmat()))
						return false;
				}
				// spécifique cnss
				if (StringUtil.notEquals(globalUpdate.nomClient, ClsEntreprise.CNSS))
				{
					if (exist_evcg && update.numerobulletin == 9)
						retour = updateZoneConge(salarie);

					if (update.numerobulletin == 9)
					{
						retour = updateBaseCongeAgent(salarie);
						if(!retour) return false;
					}
					else
					{
						salarie.setDapec(salarie.getDapec().add(new BigDecimal(bas_con)));
						// update_flag = true;
					}
				}
				// spécifique cnss
				if (StringUtil.equals(globalUpdate.nomClient, ClsEntreprise.CNSS))
				{
					if (update.conge_montant)
					{
						if (update.numerobulletin == 9)
							retour = updateZoneConge(salarie);
					}
					else
					{
						if (exist_evcg && update.numerobulletin == 9)
							retour = updateZoneConge(salarie);

						if (update.numerobulletin == 9)
						{
							retour = updateBaseCongeAgent(salarie);
							if(!retour) return false;
						}
						else salarie.setDapec(salarie.getDapec().add(new BigDecimal(bas_con)));
					}
				}

				// retour = deleteEVMois(salarie);
				deleteEVMois_flag = true;

				// retour = deleteEFMois(salarie);
				deleteEFMois_flag = true;

				if (update.calc_part_auto)
				{
					if (!calculPartFiscale(salarie))
					{
						error = update.parameter.errorMessage("ERR-10518", update.langue, salarie.getNmat());
						refreshConnexion();
						if (update.genfile == 'O')
							printOut += "\n" + (error);
						globalUpdate._setEvolutionTraitement(update.request, error, true);
						return false;
					}
				}
				if (StringUtil.equals(sav_pmcf, update.periode) && update.numerobulletin == 9)
				{
					// oSession.createQuery("delete from Rhtfic" + " where idEntreprise = '" + update.dossier + "'" + " and nmat = '" + salarie.getNmat() +
					// "'" + " and nbul = " + update.numerobulletin).executeUpdate();
					sup_fic_flag = true;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			update.error = ClsTreater._getStackTrace(e);
			globalUpdate._setEvolutionTraitement(update.request, ClsTreater._getStackTrace(e), true);
			globalUpdate.logError("Mat : " + this.salarie.getNmat() + "\r\n" + ClsTreater._getStackTrace(e));
			return false;
		}

		try
		{
			if(StringUtil.notEquals(globalUpdate.nomClient, ClsEntreprise.BGFIGE)){
				salarie.setJapec(new BigDecimal(jr_cg_ex));
				salarie.setJapa(new BigDecimal(jr_cg_ant));
			}

			if (!majSalarie())
				return false;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			update.error = ClsTreater._getStackTrace(e);
			globalUpdate._setEvolutionTraitement(update.request, ClsTreater._getStackTrace(e), true);
			globalUpdate.logError("Mat : " + this.salarie.getNmat() + "\r\n" + ClsTreater._getStackTrace(e));
		}

		return true;
	}

	/**
	 * =>maj_pnp Conges payes non pris a partir des rubriques
	 */
	private boolean updateCongePayeNonPris(Salarie salarie, BigDecimal mont)
	{
		refreshConnexion();
		if (update.genfile == 'O')
			printOut += "\n" + ("-----Update Cpnp");

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
			ParameterUtil.println("Setting pmcf =null, case updateCongePayeNonPris");
			salarie.setPmcf(" ");
			salarie.setNbjcf(new BigDecimal(0));
			salarie.setNbjaf(new BigDecimal(0));
			// salarie.setDrtcg(null);
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

			// -- Mise a jour des droits en jrs
			// -- LH 151097 Specif SHELL : On deduit d'abord des jours de reliquat
			// -- On deduit d'abord des jours de reliquat anterieurs
//			if (StringUtil.equals(ClsEntreprise.SHELL_GABON, this.update.parameter.nomClient))
//			{
//				if (wnbjcad > salarie.getJapa().doubleValue())
//				{
//					wnbjcad = wnbjcad - salarie.getJrla().doubleValue();
//					salarie.setJrla(new BigDecimal(0));
//				}
//				else
//				{
//					salarie.setJrla(new BigDecimal(salarie.getJrla().doubleValue() - wnbjcad));
//					wnbjcad = 0;
//				}
//				// -- On deduit ensuite des jours de reliquat en cours
//				if (wnbjcad > salarie.getJrlec().doubleValue())
//				{
//					wnbjcad = wnbjcad - salarie.getJrlec().doubleValue();
//					salarie.setJrlec(new BigDecimal(0));
//				}
//				else
//				{
//					salarie.setJrlec(salarie.getJrlec().subtract(new BigDecimal(wnbjcad)));
//					wnbjcad = 0;
//				}
//			}
			// -- On deduit ensuite des jours a prendre anterieurs puis en cours

			if (wnbjcad <= salarie.getJapa().doubleValue())
			{
				salarie.setJapa(new BigDecimal(salarie.getJapa().doubleValue() - wnbjcad));
			}
			else
			{
				salarie.setJapec(new BigDecimal(salarie.getJapec().doubleValue() - (wnbjcad - salarie.getJapa().doubleValue())));
				salarie.setJapa(new BigDecimal(0));
			}

			// -- LH 151097 Specif SHELL :
			// -- Les jours restants a prendre sont passes dans le reliquat
			// --wsal01.jrla := wsal01.japa ;
			// --wsal01.jrlec := wsal01.japec ;
			// --wsal01.japa := 0 ;
			// --wsal01.japec := 0 ;
			// --wsal01.jrlec := wsal01.jrlec + wnbjcad;
//			if (StringUtil.notEquals(ClsEntreprise.SHELL_GABON, this.update.parameter.nomClient))
//				salarie.setJrlec(new BigDecimal(salarie.getJrlec().doubleValue() + wnbjcad));

			if (salarie.getDdcf() == null)
				conges_annuels = true;

		}

		pnp = true;

		return true;
	}

	/**
	 * =>maj_pret13 Mise a jour de la table des prets (fiche salarie)
	 */
	private boolean updatePret13(String nmat, int lg, String crub, BigDecimal mont) throws Exception
	{
		refreshConnexion();
		if (update.genfile == 'O')
			printOut += "\n" + ("-----Update prets 13");

		// Object o = update.service.get(Rhtpretsagent.class, new RhtpretsagentPK(update.dossier, nmat, lg));
		PretInterne pretent = null;
		String query = "From PretInterne where idEntreprise = '" + update.dossier + "' and nmat = '" + nmat + "' and lg = " + lg;
		List<PretInterne> lst = update.service.find(query);
		Object o = lst.isEmpty() ? null : lst.get(0);
		if (o != null)
		{
			PretInterne pret = (PretInterne) o;
			if (ClsObjectUtil.isNull(pret.getMtremb()))
				pret.setMtremb(new BigDecimal(0));
			pret.setMtremb(pret.getMtremb().add(mont));
			//
			oSession.update(pret);
		}
		else
		{
			error = update.parameter.errorMessage("ERR-90132", update.langue, nmat, crub, lg);
			globalUpdate._setEvolutionTraitement(update.request, error, true);
			globalUpdate.logError("Mat : " + this.salarie.getNmat() + "\r\n" + error);
			return false;
		}
		return true;
	}

	/**
	 * =>maj_pret17 Mise a jour des zones de pret dans PAPRENT
	 */
	private boolean updatePret17(String nmat, String crub, String nprt, BigDecimal mont) throws Exception
	{
		refreshConnexion();
		if (update.genfile == 'O')
			printOut += "\n" + ("-----Update prets 17");

		PretExterneEntete pretent = null;
		String query = "From PretExterneEntete where idEntreprise = '" + update.dossier + "' and nprt = '" + nprt + "'";
		List<PretExterneEntete> lst = update.service.find(query);

		// Object o = update.service.get(PretExterneEntete.class, new PretExterneEntetePK(update.dossier, nprt));
		Object o = lst.isEmpty() ? null : lst.get(0);
		if (o == null)
		{
			error = update.parameter.errorMessage("ERR-90133", update.langue, nmat, crub, nprt);
			globalUpdate._setEvolutionTraitement(update.request, error, true);
			return false;
		}
		else
		{
			pretent = (PretExterneEntete) o;
			if (!(nmat.equals(pretent.getNmat()) && crub.equals(pretent.getCrub())))
			{
				error = update.parameter.errorMessage("ERR-90133", update.langue, nmat, crub, nprt);
				globalUpdate._setEvolutionTraitement(update.request, error, true);
				return false;
			}
		}

		//Gestion de la devise
		String devise = pretent.getCodedevise();
		if(StringUtil.isNotBlank(devise))
		{
			ParamData nome = this.update.updateservice.chercherEnNomenclature(update.dossier, 27, devise, 1);
			if(nome != null && nome.getValt() != null && nome.getValt().intValue()!=0 && nome.getValt().intValue()!=1)
			{
				mont = mont.divide(nome.getValt(), MathContext.DECIMAL128);
			}
		}

		if (!ClsObjectUtil.isNull(pretent.getResr()))
		{
			pretent.setResr(pretent.getResr().subtract(mont));
			if (pretent.getResr().intValue() <= 0.001)
				pretent.setResr(new BigDecimal(0));
		}
//		else
//			pretent.setResr(BigDecimal.ZERO);
		int i = 0;
		if (!ClsObjectUtil.isNull(pretent.getNber()))
		{
			i = pretent.getNber().intValue();
			i--;
			if (i < 0)
				i = 0;
			pretent.setNber(i);
		}
//		else
//			pretent.setNber(0);

		if (pretent.getResr() != null && pretent.getResr().intValue() == 0 && pretent.getNber()!=null && pretent.getNber() == 0)
		{
			pretent.setPact("N");
			// Ajout Armel ligne qui suit
//			if (globalUpdate.ui != null && globalUpdate.ui.request != null)
//			{
//				if (StringUtil.equals("O", ClsParamete.getSessionObject(globalUpdate.ui.request, "isgpac")))
//					pretent.setEtpr("T");
//			}
		}
		oSession.update(pretent);

		return true;
	}

	/**
	 * =>maj_eva Generation d'EV sur mois suivant
	 */
	private boolean genElementVariableNextMonth(String nmat, String crub, BigDecimal mont) throws Exception
	{
		refreshConnexion();
		if (update.genfile == 'O')
			printOut += "\n" + ("-----génération ev sur mois suivant");
		CleEltVarEnt pk = null;
		try
		{
			//ParameterUtil.println("Génération des ev du mois suivant");

			ElementVariableDetailMois evar = new ElementVariableDetailMois();
			evar.setAamm(update.mois_ms);
			evar.setIdEntreprise(new Integer(update.dossier));
			evar.setNmat(nmat);
			evar.setNbul(update.nbul_ms);
			evar.setNlig(maj_eva_nlig++);
			//evar.setComp_id(new Integer(update.dossier, nmat, update.nbul_ms, maj_eva_nlig++));
			evar.setCuti(update.user);
			evar.setCuti(StringUtils.isNotEmpty(update.userEV)?update.userEV:evar.getCuti());
			evar.setRubq(crub);
			evar.setArgu(" ");
			evar.setNprt(" ");
			evar.setRuba(" ");
			evar.setMont(mont);

			ElementVariableEnteteMois o = null;
			if (!maj_eva_ent_already_check)
			{
				pk = new CleEltVarEnt(evar.getIdEntreprise(), evar.getAamm(), evar.getNmat(), evar.getNbul());
				if (!this.mapElementVariableEnteteMoisDejaRecherche.containsKey(pk))
				{
					//ParameterUtil.println("Début Recuperation de l'entete dans la base " + evar.toString());
					// o = (ElementVariableEnteteMois) update.service.get(ElementVariableEnteteMois.class, pk);
					// this.mapElementVariableEnteteMoisDejaRecherche.put(pk, o == null);

					String query = "From ElementVariableEnteteMois where idEntreprise='" + evar.getIdEntreprise() + "' and aamm='" + evar.getAamm() + "' and nmat='"
							+ evar.getNmat() + "'" + " and nbul=" + evar.getNbul();
					List<ElementVariableEnteteMois> l = update.service.find(query);
					//ParameterUtil.println("Fin Recuperation de l'entete dans la base" + evar.toString());
					this.mapElementVariableEnteteMoisDejaRecherche.put(pk, l.isEmpty());
				}

				maj_eva_ent_already_check = true;

				if (!this.mapElementVariableEnteteMoisDejaRecherche.get(pk))
					maj_eva_ent_exist = true;
			}
			if (!maj_eva_ent_exist)
			{
				ElementVariableEnteteMois event = new ElementVariableEnteteMois();
				CleEltVarEnt cleEV = new CleEltVarEnt(evar.getIdEntreprise(), evar.getAamm(), evar.getNmat(), evar.getNbul());
				event.setIdEntreprise(evar.getIdEntreprise());
				event.setAamm(evar.getAamm());
				event.setNmat(evar.getNmat());
				event.setNbul(evar.getNbul());
				event.setDdpa(date_debut_mois_ms);
				event.setDfpa(date_fin_mois_ms);
				event.setBcmo("N");

				if (!mapElementVariableEnteteMoisDejaGenere.containsKey(cleEV))
				{
					//ParameterUtil.println("Debut Sauvegardde de l'entete dans la base" + evar.toString());
					oSession.save(event);
					//ParameterUtil.println("Fin Sauvegardde de l'entete dans la base" + evar.toString());
					mapElementVariableEnteteMoisDejaGenere.put(cleEV, cleEV);
				}
			}

			// if (!mapElementVariableDetailMoisDejaGenere.containsKey(evar.getComp_id()))
			// {
			ParameterUtil.println("Debut Sauvegardde du détail de l'ev dans la base" + evar.toString());
			oSession.save(evar);
			ParameterUtil.println("Fin Debut Sauvegardde du détail de l'ev dans la base" + evar.toString());
			// mapElementVariableDetailMoisDejaGenere.put(evar.getComp_id(), evar.getComp_id());
			// }
		}
		catch (Exception e)
		{

			e.printStackTrace();
			globalUpdate.logError("Mat : " + this.salarie.getNmat() + "\r\n" + ClsTreater._getStackTrace(e));
			return false;
		}
		//ParameterUtil.println("Fin génération des ev du mois suivant");
		return true;
	}

	private void _chargerCumuls(String nmat) throws Exception
	{
		refreshConnexion();
		if (update.genfile == 'O')
			printOut += "\n" + ("-----Suppression des anciens cumuls");

		List<CumulPaie> liste = update.updateservice.getCumulsToDelete(oSession, nmat);
		for (CumulPaie cumulPaie : liste)
		{
			CleCumulPaie pk = new CleCumulPaie();
			pk.setAamm(cumulPaie.getAamm());
			pk.setIdEntreprise(cumulPaie.getIdEntreprise());
			pk.setNmat(cumulPaie.getNmat());
			pk.setNbul(cumulPaie.getNbul());
			pk.setRubq(cumulPaie.getRubq());
			this.cumulsMap.put(pk, new ClsCumul(cumulPaie, true));
		}
		// ParameterUtil.println("Aprés chargement des cumuls du salarié "+nmat+", taille du map = "+this.cumulsMap.size());
		// update.updateservice.deleteCumuls(oSession, nmat);
	}

	private void _chargerCumuls99(String nmat) throws Exception
	{
		refreshConnexion();
		if (update.genfile == 'O')
			printOut += "\n" + ("-----Suppression des anciens cumuls annuels");

		List<CumulPaie> liste = update.updateservice.getCumulsToDelete99(oSession, nmat);
		for (CumulPaie cumulPaie : liste)
		{
			CleCumulPaie pk = new CleCumulPaie(cumulPaie.getIdEntreprise(), cumulPaie.getNmat(), cumulPaie.getAamm(), cumulPaie.getRubq(), cumulPaie.getNbul());
			this.cumulsMap.put(pk, new ClsCumul(cumulPaie, true));
		}
		// ParameterUtil.println("Aprés chargement des cumuls 99 du salarié "+nmat+", taille du map = "+this.cumulsMap.size());

		update.updateservice.deleteCumuls99(oSession, nmat);
	}

	private boolean _updateCumulMap(CleCumulPaie cumulPK, CalculPaie calcul) throws Exception
	{
		// if(update.genfile == 'O') printOut +="\n"+("-----Recuperation du cumul é enregistrer");
		ClsCumul cum = this.cumulsMap.get(cumulPK);
		CumulPaie cumul = null;
		boolean exist = false;
		try
		{
			if (cum == null)
			{
				cum = new ClsCumul();
				cumul = new CumulPaie();
				cumul.setIdEntreprise(cumulPK.getIdEntreprise());
				cumul.setNbul(cumulPK.getNbul());
				cumul.setNmat(cumulPK.getNmat());
				cumul.setAamm(cumulPK.getAamm());
				cumul.setRubq(cumulPK.getRubq());
				cumul.setNbul(cumulPK.getNbul());
				cumul.setBasc(calcul.getBasc());
				cumul.setBasp(calcul.getBasp());
				cumul.setMont(calcul.getMont());
				cumul.setTaux(calcul.getTaux());
				exist = false;
			}
			else
			{
				cumul = new CumulPaie();
				cumul.setNbul(cumulPK.getNbul());
				cumul.setNmat(cumulPK.getNmat());
				cumul.setAamm(cumulPK.getAamm());
				cumul.setRubq(cumulPK.getRubq());
				cumul.setNbul(cumulPK.getNbul());
				cumul.setBasc(cum.getCumul().getBasc().add(calcul.getBasc()));
				cumul.setBasp(cum.getCumul().getBasp().add(calcul.getBasp()));
				cumul.setMont(cum.getCumul().getMont().add(calcul.getMont()));
				cumul.setTaux(cum.getCumul().getTaux().add(calcul.getTaux()));
				exist = cum.isExistindb();
			}

			// if(cumul.getAamm().equals(update.am99))
			// {
			// ParameterUtil.println("------>Cumul annuel de la rubrique "+cumul.toString());
			// }

			cum.setCumul(cumul);
			cum.setExistindb(exist);
			this.cumulsMap.put(cumulPK, cum);
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
			globalUpdate._setEvolutionTraitement(update.request, ClsTreater._getStackTrace(e), true);
			globalUpdate.logError("Mat : " + this.salarie.getNmat() + "\r\n" + ClsTreater._getStackTrace(e));
			return false;
		}
		return true;
	}

	private boolean _updateCumulMois(Salarie salarie)
	{
		String queryString = "";
		queryString += "INSERT INTO CumulPaie (CDOS, NMAT, AAMM,RUBQ, NBUL, BASC,BASP, TAUX, MONT) ";
		queryString += "SELECT CDOS, NMAT, AAMM, RUBQ, NBUL, sum(BASC), sum(BASP), max(TAUX), sum(MONT) ";
		queryString += "FROM CalculPaie WHERE CDOS = :cdos and NMAT = :nmat and AAMM = :aamm and NBUL = :nbul ";
		queryString += "GROUP BY IDENTREPRISE, NMAT, AAMM, RUBQ, NBUL";

		try
		{
			Query q = oSession.createSQLQuery(queryString);
			q.setParameter("cdos", this.update.dossier, StandardBasicTypes.STRING);
			q.setParameter("nmat", salarie.getNmat(), StandardBasicTypes.STRING);
			q.setParameter("aamm", this.update.periode, StandardBasicTypes.STRING);
			q.setParameter("nbul", this.update.numerobulletin, StandardBasicTypes.INTEGER);
			q.executeUpdate();
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
			globalUpdate._setEvolutionTraitement(update.request, ClsTreater._getStackTrace(e), true);
			globalUpdate.logError("Mat : " + this.salarie.getNmat() + "\r\n" + ClsTreater._getStackTrace(e));
			return false;
		}

		return true;
	}

	private int _updateUnCumul(CumulPaie cum)
	{
		String queryString = "";
		queryString += "UPDATE CumulPaie SET BASC =:BASC,BASP =:BASP, TAUX=:TAUX, MONT=:MONT ";
		queryString += " WHERE IDENTREPRISE = :cdos and NMAT = :nmat and AAMM = :aamm and RUBQ = :rubq and NBUL = :nbul ";

		Integer nbr = 0;

		try
		{
			Query q = oSession.createSQLQuery(queryString);
			q.setParameter("BASC", cum.getBasc(), StandardBasicTypes.BIG_DECIMAL);
			q.setParameter("BASP", cum.getBasp(), StandardBasicTypes.BIG_DECIMAL);
			q.setParameter("TAUX", cum.getTaux(), StandardBasicTypes.BIG_DECIMAL);
			q.setParameter("MONT", cum.getMont(), StandardBasicTypes.BIG_DECIMAL);
			q.setParameter("cdos", this.update.dossier, StandardBasicTypes.STRING);
			q.setParameter("nmat", salarie.getNmat(), StandardBasicTypes.STRING);
			q.setParameter("aamm", cum.getAamm(), StandardBasicTypes.STRING);
			q.setParameter("rubq", cum.getRubq(), StandardBasicTypes.STRING);
			q.setParameter("nbul", cum.getNbul(), StandardBasicTypes.INTEGER);
			nbr = q.executeUpdate();
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
			globalUpdate._setEvolutionTraitement(update.request, ClsTreater._getStackTrace(e), true);
			globalUpdate.logError("Mat : " + this.salarie.getNmat() + "\r\n" + ClsTreater._getStackTrace(e));
			return -1;
		}

		return nbr;
	}

	private int _insertUnCumul(CumulPaie cumul)
	{
		String queryString = "";
		queryString += "INSERT INTO CumulPaie (IDENTREPRISE, NMAT, AAMM,RUBQ, NBUL, BASC,BASP, TAUX, MONT) ";
		queryString += "VALUES (:CDOS,:NMAT,:AAMM,:RUBQ,:NBUL,:BASC,:BASP,:TAUX,:MONT)";

		Integer nbr = 0;

		try
		{
			Query q = oSession.createSQLQuery(queryString);
			q.setParameter("CDOS", this.update.dossier, StandardBasicTypes.STRING);
			q.setParameter("NMAT", cumul.getNmat(), StandardBasicTypes.STRING);
			q.setParameter("AAMM", cumul.getAamm(), StandardBasicTypes.STRING);
			q.setParameter("RUBQ", cumul.getRubq(), StandardBasicTypes.STRING);
			q.setParameter("NBUL", cumul.getNbul(), StandardBasicTypes.INTEGER);
			q.setParameter("BASC", cumul.getBasc(), StandardBasicTypes.BIG_DECIMAL);
			q.setParameter("BASP", cumul.getBasp(), StandardBasicTypes.BIG_DECIMAL);
			q.setParameter("TAUX", cumul.getTaux(), StandardBasicTypes.BIG_DECIMAL);
			q.setParameter("MONT", cumul.getMont(), StandardBasicTypes.BIG_DECIMAL);
			nbr = q.executeUpdate();
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
			globalUpdate._setEvolutionTraitement(update.request, ClsTreater._getStackTrace(e), true);
			globalUpdate.logError("Mat : " + this.salarie.getNmat() + "\r\n" + ClsTreater._getStackTrace(e));
			return -1;
		}

		return nbr;
	}

	/**
	 * =>lect_rub_cal Lecture du calcul sur mois courant
	 */

	private boolean lectureCalculSurMoisCourant(Salarie salarie)
	{
		refreshConnexion();
		if (update.genfile == 'O')
			printOut += "\n" + ("-----lectureCalculSurMoisCourant");

		String nmat = salarie.getNmat();

		listeCalculPaies = update.updateservice.getListeBulletinCalculFromRhtcalcul(oSession, update.dossier, update.periode, update.numerobulletin, nmat);

		// /////////Ajout par yannick : au lieu de tester l'existence des cumuls avant de les inserer, je préfere tout charger en mémoire
		// ////////de sorte qu'on ne fasse que la sauvegarde ou la mise é jour sans plus tester l'existence en bd
		try
		{
			this._chargerCumuls(nmat);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			globalUpdate._setEvolutionTraitement(update.request, ClsTreater._getStackTrace(e), true);
			globalUpdate.logError("Mat : " + this.salarie.getNmat() + "\r\n" + ClsTreater._getStackTrace(e));
			return false;
		}
		// on insere directement les cumuls du mois
		// this._updateCumulMois(salarie);

		try
		{
			this._chargerCumuls99(nmat);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			globalUpdate._setEvolutionTraitement(update.request, ClsTreater._getStackTrace(e), true);
			globalUpdate.logError("Mat : " + this.salarie.getNmat() + "\r\n" + ClsTreater._getStackTrace(e));
			return false;
		}

		ElementSalaire rubrique = null;

		Calcul calcul = null;

		for (CalculPaie ocalcul : listeCalculPaies)
		{
			calcul = new Calcul(ocalcul);

			if ((!calcul.getMont().equals(new BigDecimal(0))) || this.update.rcon_reg(calcul.getRubq()))
			{

				rubrique = update._getRubrique(update.dossier, calcul.getRubq());
				if (rubrique == null)
				{
					error = update.parameter.errorMessage("ERR-90104", update.langue, nmat, calcul.getRubq());
					globalUpdate._setEvolutionTraitement(update.request, error, true);
					return false;
				}

				if (StringUtil.equals(calcul.getRubq(), update.rub_bc))
					bas_con = calcul.getMont().doubleValue();

				if (StringUtil.equals(calcul.getRubq(), update.rub_pnp))
					retour = updateCongePayeNonPris(salarie, calcul.getMont());

				if (StringUtil.equals(calcul.getRubq(), update.rub_cg_ant))
					jr_cg_ant = calcul.getMont().doubleValue();

				if (StringUtil.equals(calcul.getRubq(), update.rub_cg_ex))
					jr_cg_ex = calcul.getMont().doubleValue();

				if (StringUtil.equals(calcul.getRubq(), update.rubriqueNbreJourTravail))
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
					if (NumberUtils.in(rubrique.getAlgo(), 21, 83, 31, 50, 27))
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
					if (StringUtil.isNotBlank(calcul.getNprt()))
					{
						// if (!updatePret13(nmat, Integer.valueOf(calcul.getNprt()), calcul.getRubq(), calcul.getMont()))
						// {
						// return false;
						// }
						calcul.maj_pret13_flag = true;
					}
				}
				else if (NumberUtils.in(rubrique.getAlgo(), 17, 20))
				{
					if (StringUtil.isNotBlank(calcul.getNprt()))
					{
						// if (!updatePret17(nmat, calcul.getRubq(), Integer.valueOf(calcul.getNprt().trim()), calcul.getMont()))
						// {
						// return false;
						// }
						calcul.maj_pret17_flag = true;
					}
				}

				if (!radie)
				{
					if (update.t_ev_ms != null)
					{
						String crub_ms = update.t_ev_mc_ms.get(calcul.getRubq());
						if (StringUtil.isNotBlank(crub_ms) && !StringUtil.equals(ParameterUtil.formatRubrique, crub_ms))
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
		if (deleteEVMois_flag)
		{
			try
			{
				if (!deleteEVMois(salarie))
					return false;
			}
			catch (Exception e)
			{

				e.printStackTrace();
				globalUpdate._setEvolutionTraitement(update.request, ClsTreater._getStackTrace(e), true);
				globalUpdate.logError("Mat : " + this.salarie.getNmat() + "\r\n" + ClsTreater._getStackTrace(e));
				return false;
			}
		}

		if (deleteEFMois_flag)
		{
			try
			{
				if (!deleteEFMois(salarie))
					return false;
			}
			catch (Exception e)
			{

				e.printStackTrace();
				globalUpdate._setEvolutionTraitement(update.request, ClsTreater._getStackTrace(e), true);
				globalUpdate.logError("Mat : " + this.salarie.getNmat() + "\r\n" + ClsTreater._getStackTrace(e));
				return false;
			}
		}

		if (transferEVMoisSuivant_flag)
		{
			try
			{
				if (!transferEVMoisSuivant(salarie.getNmat()))
					return false;
			}
			catch (Exception e)
			{

				e.printStackTrace();
				globalUpdate._setEvolutionTraitement(update.request, ClsTreater._getStackTrace(e), true);
				globalUpdate.logError("Mat : " + this.salarie.getNmat() + "\r\n" + ClsTreater._getStackTrace(e));
				return false;
			}
		}

		if (transferEcheancePretMoisSuivant_flag)
		{
			try
			{
				if (!transferEcheancePretMoisSuivant(salarie.getNmat()))
					return false;
			}
			catch (Exception e)
			{

				e.printStackTrace();
				globalUpdate._setEvolutionTraitement(update.request, ClsTreater._getStackTrace(e), true);
				globalUpdate.logError("Mat : " + this.salarie.getNmat() + "\r\n" + ClsTreater._getStackTrace(e));
				return false;
			}
		}

		Calcul calcul = null;
		for (int i = 0; i < listeCalculs.size(); i++)
		{
			calcul = listeCalculs.get(i);

			// ParameterUtil.println("Traitement du calcul "+calcul.toString());

			if (calcul.maj_pret13_flag)
			{
				try
				{
					if (!updatePret13(salarie.getNmat(), Integer.valueOf(calcul.getNprt().trim()), calcul.getRubq(), calcul.getMont()))
						return false;
				}
				catch (NumberFormatException e)
				{
					e.printStackTrace();
					globalUpdate._setEvolutionTraitement(update.request, ClsTreater._getStackTrace(e), true);
					globalUpdate.logError("Mat : " + this.salarie.getNmat() + "\r\n" + ClsTreater._getStackTrace(e));
					return false;
				}
				catch (Exception e)
				{
					e.printStackTrace();
					globalUpdate._setEvolutionTraitement(update.request, ClsTreater._getStackTrace(e), true);
					globalUpdate.logError("Mat : " + this.salarie.getNmat() + "\r\n" + ClsTreater._getStackTrace(e));
					return false;
				}
			}

			if (calcul.maj_pret17_flag)
			{
				try
				{
					if (!updatePret17(salarie.getNmat(), calcul.getRubq(), String.valueOf(Integer.valueOf(calcul.getNprt().trim())), calcul.getMont()))
						return false;
				}
				catch (Exception e)
				{

					e.printStackTrace();
					globalUpdate._setEvolutionTraitement(update.request, ClsTreater._getStackTrace(e), true);
					globalUpdate.logError("Mat : " + this.salarie.getNmat() + "\r\n" + ClsTreater._getStackTrace(e));
					return false;
				}
			}

			if (calcul.maj_eva_flag)
			{
				try
				{
					if (!genElementVariableNextMonth(salarie.getNmat(), update.t_ev_mc_ms.get(calcul.getRubq()), calcul.getMont()))
						return false;
				}
				catch (Exception e)
				{

					e.printStackTrace();
					globalUpdate._setEvolutionTraitement(update.request, ClsTreater._getStackTrace(e), true);
					globalUpdate.logError("Mat : " + this.salarie.getNmat() + "\r\n" + ClsTreater._getStackTrace(e));
					return false;
				}
			}

			if (calcul.maj_cumul_flag)
			{
				try
				{
					if (!maj_cumuls(calcul, salarie.getNmat()))
						return false;
				}
				catch (Exception e)
				{

					e.printStackTrace();
					globalUpdate._setEvolutionTraitement(update.request, ClsTreater._getStackTrace(e), true);
					globalUpdate.logError("Mat : " + this.salarie.getNmat() + "\r\n" + ClsTreater._getStackTrace(e));
					return false;
				}
			}
		}

		// maj effectif des cumuls
		Collection<ClsCumul> cumulIterator = this.cumulsMap.values();
		Integer ii = 0;
		for (ClsCumul oCum : cumulIterator)
		{
			ii++;
			// ParameterUtil.println("Saving or updating cumul "+oCum.getCumul().toString());
			try
			{
				// if (oCum.isExistindb())
				// oSession.update(oCum.getCumul());
				// else
				// oSession.save(oCum.getCumul());

				// if (oCum.isExistindb())
				// {
				// this._updateUnCumul(oCum.getCumul());
				// }
				// else
				// this._insertUnCumul(oCum.getCumul());
				oSession.save(oCum.getCumul());

				if (ii % 20 == 0)
				{
					//oSession.flush();
					oSession.clear();
				}
			}
			catch (HibernateException e)
			{

				e.printStackTrace();
				globalUpdate._setEvolutionTraitement(update.request, ClsTreater._getStackTrace(e), true);
				globalUpdate.logError("Mat : " + this.salarie.getNmat() + "\r\n" + ClsTreater._getStackTrace(e));
				return false;
			}
		}

		if (sup_fic_flag)
		{
			try
			{
				if (!update.updateservice.supp_fictif(oSession, salarie.getNmat()))
					return false;
			}
			catch (Exception e)
			{

				e.printStackTrace();
				globalUpdate._setEvolutionTraitement(update.request, ClsTreater._getStackTrace(e), true);
				globalUpdate.logError("Mat : " + this.salarie.getNmat() + "\r\n" + ClsTreater._getStackTrace(e));
				return false;
			}
		}

		//ParameterUtil.println("Mise é jour des zones de congés fictifs");
		// mise é jour des zones de conges fictifs du salarié : ajout le 09/02/2009
		if ("O".equals(update.fictif))
			_UpdateZoneCongeFictifAgent(salarie);

		for (int i = 0; i < listOfEltVarConge.size(); i++)
		{
			//ParameterUtil.println("Traitement du congé " + listOfEltVarConge.get(i).toString());

			if (listOfEltVarConge.get(i).pas_moi_flag)
			{
				try
				{
					if (!insertCongeMoisSuivant(listOfEltVarConge_ms.get(i), salarie.getNmat()))
						return false;
				}
				catch (Exception e)
				{

					e.printStackTrace();
					globalUpdate._setEvolutionTraitement(update.request, ClsTreater._getStackTrace(e), true);
					globalUpdate.logError("Mat : " + this.salarie.getNmat() + "\r\n" + ClsTreater._getStackTrace(e));
					return false;
				}
			}

			if (listOfEltVarConge.get(i).transferEVCGMoisSuivant_flag)
			{
				try
				{
					if (!transfererUnEVCGMoisSuivant(listOfEltVarConge_ms.get(i), salarie.getNmat()))
						return false;
				}
				catch (Exception e)
				{

					e.printStackTrace();
					globalUpdate._setEvolutionTraitement(update.request, ClsTreater._getStackTrace(e), true);
					globalUpdate.logError("Mat : " + this.salarie.getNmat() + "\r\n" + ClsTreater._getStackTrace(e));
					return false;
				}
			}


			if (listOfEltVarConge.get(i).maj_det_flag)
			{
				try
				{
					if (!updateDetailAbsConge(listOfEltVarConge.get(i), salarie.getNmat()))
						return false;
				}
				catch (Exception e)
				{

					e.printStackTrace();
					globalUpdate._setEvolutionTraitement(update.request, ClsTreater._getStackTrace(e), true);
					globalUpdate.logError("Mat : " + this.salarie.getNmat() + "\r\n" + ClsTreater._getStackTrace(e));
					return false;
				}
			}

		}
		// On sauvegarde effectivement les HistoCongeSalarie
		try
		{
			saveDetailAbsConge();
		}
		catch (Exception e2)
		{
			e2.printStackTrace();
			globalUpdate._setEvolutionTraitement(update.request, ClsTreater._getStackTrace(e2), true);
			return false;
		}

		if (sup_prets17_flag)
		{
			//ParameterUtil.println("Update des prets");
			try
			{
				update.updateservice.updatePrets17(oSession, update.dossier, salarie.getNmat());
			}
			catch (Exception e)
			{

				e.printStackTrace();
				globalUpdate._setEvolutionTraitement(update.request, ClsTreater._getStackTrace(e), true);
				globalUpdate.logError("Mat : " + this.salarie.getNmat() + "\r\n" + ClsTreater._getStackTrace(e));
				return false;
			}
		}

		try
		{
			//ParameterUtil.println("debut Update du salarié");
			//Formatage des zones montants suivant le nbr de decimales requis
			truncateMontants();
//			if(StringUtil.equals("420011", salarie.getNmat()))
//				System.out.println("Salarié test");
			salarie.setCaisseMutuelleSalarie(null);
			salarie.setElementFixeSalaire(null);
			salarie.setPretExterneEntete(null);
			salarie.setVirementSalarie(null);
			salarie.setPretInterne(null);
			oSession.update(salarie);
			//ParameterUtil.println("fin Update du salarié");
		}
		catch (HibernateException e1)
		{

			e1.printStackTrace();
			globalUpdate._setEvolutionTraitement(update.request, ClsTreater._getStackTrace(e1), true);
			globalUpdate.logError("Mat : " + this.salarie.getNmat() + "\r\n" + ClsTreater._getStackTrace(e1));
			return false;
		}

		if (this.update.genfile == 'O')
		{
			//ParameterUtil.println("#########Ecriture du résultat");
			this.agentUpdateToString();
		}

		if (update.numsalariecourant % 20 == 0)
		{
			try
			{
				//ParameterUtil.println("Before Flush");
				oSession.flush();
				oSession.clear();
				//ParameterUtil.println("After Flush");
			}
			catch (Exception e)
			{
				e.printStackTrace();
				globalUpdate._setEvolutionTraitement(update.request, ClsTreater._getStackTrace(e), true);
				globalUpdate.logError("Mat : " + this.salarie.getNmat() + "\r\n" + ClsTreater._getStackTrace(e));
				return false;
			}
		}
		return true;

	}

	private boolean maj_cumuls(Calcul calcul, String nmat) throws Exception
	{

		if (StringUtil.isNotBlank(calcul.getRuba()))
		{
			calcul.setRubq(calcul.getRuba());
			calcul.setBasc(new BigDecimal(0));
			calcul.setBasp(new BigDecimal(0));
			calcul.setTaux(new BigDecimal(0));
		}

		boolean result = false;

		result = this._updateCumulMap(new CleCumulPaie(new Integer(update.dossier), nmat, update.periode, calcul.getRubq(), new Integer(update.numerobulletin)), calcul);

		if (!result)
			return false;

		result = this._updateCumulMap(new CleCumulPaie(new Integer(update.dossier), nmat, update.am99, calcul.getRubq(), new Integer(9)), calcul);
		if (!result)
			return false;

		return true;
	}

	// --------------------------------------------------------------------------------
	// -- Transfert des echeances de prets sur mois suivant (wsal01.cals = 'N')
	// ---------------------------------------------------------------------------------
	private boolean transferEcheancePretMoisSuivant(String nmat) throws Exception
	{
		refreshConnexion();
		if (update.genfile == 'O')
			printOut += "\n" + ("-----Transfert des echeances de prets");

		String perb1 = new ClsDate(update.periode, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getDateS(update.dateformat);

		double wmont_pret = 0;
		// List<PretExterneEntete> l = update.updateservice.getPretsToTransfert(update.dossier, nmat, perb1);
		List<PretExterneEntete> l = update.updateservice.getPretsToTransfert(oSession, update.dossier, nmat, new ClsDate(update.periode, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getDate());
		Object o = null;

		String testPerb = "to_char(a.perb,'YYYYMM')";
		if (StringUtil.equals(TypeBDUtil.typeBD, TypeBDUtil.IN)) testPerb = "to_char1(a.perb,'YYYYMM')";
		if (StringUtil.equals(TypeBDUtil.typeBD, TypeBDUtil.MS)) testPerb = "dbo.formaterDateEnChaine(a.perb,'YYYYMM')";
		if (StringUtil.equals(TypeBDUtil.typeBD, TypeBDUtil.MY)) testPerb = "date_format(a.perb,'%Y%m')";

		String query = " Select a.* From PretExterneDetail a where a.idEntreprise = '" + update.dossier + "' and a.nprt = :nprt and "+testPerb+"= :perb";
		List<PretExterneDetail> liste = null;
		for (PretExterneEntete entete : l)
		{
			Query q = oSession.createSQLQuery(query).addEntity("a", PretExterneDetail.class);
			q.setParameter("nprt", entete.getNprt(), StandardBasicTypes.STRING);
			q.setParameter("perb", update.periode, StandardBasicTypes.STRING);
			liste = q.list();

			// o = update.service.get(PretExterneDetail.class, new PretExterneDetailPK(update.dossier, entete.getNprt(), new ClsDate(update.periode,
			// ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getLastDayOfMonth()));
			o = liste.isEmpty() ? null : liste.get(0);

			if (o == null)
			{
				wmont_pret = 0;
			}
			else
			{
				wmont_pret = ((PretExterneDetail) o).getEchr().doubleValue();
				((PretExterneDetail) o).setEchr(new BigDecimal(0));
				oSession.update(o);
				o = update.updateservice.getMaxPerbFromPret(update.dossier, entete.getNprt());
				if (o != null)
				{
					Date perb = new ClsDate((Date) o).addMonth(1);
					PretExterneDetail ligne = new PretExterneDetail();
					ligne.setIdEntreprise(new Integer(update.dossier));
					ligne.setNprt(entete.getNprt());
					ligne.setPerb(perb);
					ligne.setEcho(new BigDecimal(0));
					ligne.setEchr(new BigDecimal(wmont_pret));
					ligne.setInte(new BigDecimal(0));
					ligne.setNbul(9);
					ligne.setTaxe(new BigDecimal(0));

					oSession.save(ligne);
				}
			}

		}

		return true;
	}

	// ---------------------------------------------------------------------------------
	// -- Transfert des EV sur mois suivant (wsal01.cals = 'N')
	// --------------------------------------------------------------------------------
	private boolean transferEVMoisSuivant_save(String nmat)
	{
		refreshConnexion();
		if (update.genfile == 'O')
			printOut += "\n" + ("-----Transfert des ev sur le mois suivant");
		return update.updateservice.transferEVMoisSuivant(oSession, update.dossier, update.periode, update.numerobulletin, update.mois_ms, update.nbul_ms, nmat, new ClsDate(date_debut_mois_ms)
				.getDateS(update.dateformat), new ClsDate(date_fin_mois_ms).getDateS(update.dateformat), update.user);
	}

	private boolean transferEVMoisSuivant(String nmat) throws Exception
	{
		refreshConnexion();
		if (update.genfile == 'O')
			printOut += "\n" + ("-----Transfert des ev sur le mois suivant");

		ParameterUtil.println("Debut Chargement les EV é transferer sur le mois suivant");
		List<Object[]> l = update.updateservice.chargerEVEnteteEtDetail(oSession, update.dossier, update.periode, nmat, update.numerobulletin);
		ParameterUtil.println("Fin Chargement les EV é transferer sur le mois suivant");

		Object[] ev = null;
		ElementVariableDetailMois evar = null;
		ElementVariableEnteteMois event = null;
		Object o = null;
		CleEltVarEnt pk = null;

		List<ElementVariableEnteteMois> lst = null;
		String query = null;
		for (Object object : l)
		{
			ev = (Object[]) object;
			evar = new ElementVariableDetailMois();
			BeanUtils.copyProperties((ElementVariableDetailMois) ev[0], evar);
			evar.setAamm(update.mois_ms);
			evar.setNbul(update.nbul_ms);
			evar.setNlig(maj_eva_nlig++);
			evar.setCuti(StringUtils.isNotEmpty(evar.getCuti())?evar.getCuti():update.user);
			evar.setCuti(StringUtils.isNotEmpty(update.userEV)?update.userEV:evar.getCuti());

			pk = new CleEltVarEnt(evar.getIdEntreprise(), evar.getAamm(), evar.getNmat(), evar.getNbul());
			if (!this.mapElementVariableEnteteMoisDejaRecherche.containsKey(pk))
			{
				// o = (ElementVariableEnteteMois) update.service.get(ElementVariableEnteteMois.class, pk);
				query = "From ElementVariableEnteteMois where idEntreprise='" + evar.getIdEntreprise() + "' and aamm='" + evar.getAamm() + "' and nmat='" + evar.getNmat() + "'"
						+ " and nbul=" + evar.getNbul();
				lst = update.service.find(query);
				o = lst.isEmpty() ? null : lst.get(0);
				this.mapElementVariableEnteteMoisDejaRecherche.put(pk, o == null);
			}

			if (this.mapElementVariableEnteteMoisDejaRecherche.get(pk))
			{
				event = new ElementVariableEnteteMois();
				CleEltVarEnt pkEv = new CleEltVarEnt(evar.getIdEntreprise(), evar.getAamm(), evar.getNmat(), evar.getNbul());
				event.setIdEntreprise(evar.getIdEntreprise());
				event.setAamm(evar.getAamm());
				event.setNmat(evar.getNmat());
				event.setNbul(evar.getNbul());
				event.setDdpa(date_debut_mois_ms);
				event.setDfpa(date_fin_mois_ms);
				event.setBcmo("N");

				if (!mapElementVariableEnteteMoisDejaGenere.containsKey(pkEv))
				{
					oSession.save(event);
					mapElementVariableEnteteMoisDejaGenere.put(pkEv, pkEv);
				}
			}
			//
			oSession.save(evar);
		}
		return true;
	}

	public void refreshConnexion()
	{
		try
		{
			String query = "Update Langue set clang=clang where clang='XX'";
//			String query = "Select 'X' From Cpdos where idEntreprise = '"+update.dossier+"'";
			oSession.createSQLQuery(query).executeUpdate();
			//oSession.flush();
			oSession.clear();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			globalUpdate._setEvolutionTraitement(update.request, ClsTreater._getStackTrace(e), true);
			if(salarie != null)
				globalUpdate.logError("Mat : " + this.salarie.getNmat() + "\r\n" + ClsTreater._getStackTrace(e));
		}
	}

	/**
	 * Charger les ev du salarié pour la période courante
	 *
	 * @param nmat
	 * @return
	 */

	private boolean chargerEVCGMoisCourant(String nmat)
	{
		//refreshConnexion();
		if (update.genfile == 'O')
			printOut += "\n" + ("-----Chargement de la liste des ev é traiter");

		List<Object[]> l = update.updateservice.chargerEVMoisCourant(oSession, update.dossier, update.periode, nmat, update.numerobulletin);
		ElementVariableConge evconge = null;
		ElementVariableEnteteMois event = null;
		ClsUpdateBulletin.RhfnomMotifConge fnomMotif = null;
		DetailEv elementConge = null;
		DetailEv save = null;

		exist_evcg = l.size() > 0;

		for (Object[] obj : l)
		{
			evconge = (ElementVariableConge) obj[0];

			evconge.setCuti(StringUtils.isNotEmpty(evconge.getCuti())?evconge.getCuti():update.user);
			evconge.setCuti(StringUtils.isNotEmpty(update.userEV)?update.userEV:evconge.getCuti());

			event = (ElementVariableEnteteMois) obj[1];

			elementConge = new DetailEv();
			save = new DetailEv();

			fnomMotif = update._getRhfnomMotifConges(evconge.getMotf());

			if (fnomMotif != null)
			{
				if (StringUtil.equals(update.motif_reliq, evconge.getMotf()))
				{
					nbjreliq += evconge.getNbja().doubleValue();
				}

				if (fnomMotif.montant1 == 1 && fnomMotif.montant3 == 1)
				{
					nbjcp += evconge.getNbjc().doubleValue();
				}

				if ((fnomMotif.montant1 == 1 && fnomMotif.montant3 == 0) || StringUtil.equals(evconge.getMotf(), update.motif_cg_abs_ms))
				{
					if (evconge.getDdeb().equals(event.getDdpa()) && evconge.getDfin().equals(event.getDfpa()))
					{
						mois_conge = true; //Salarié en congé tout le mois
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
								if (StringUtil.equals(update.dernierePeriodeCloturee, d1))
									break;
							}
						}
						if (ind91 < update.listePeriodesT91.size() - 1)
						{
							period = update.listePeriodesT91.get(ind91);
							if (evconge.getDdeb().compareTo(period.dfin) > 0)
							{
								//if (this.transferEVMoisSuivant_flag)
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
							//if (this.transferEVMoisSuivant_flag)
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

			// elementConge = new DetailEv();
			elementConge.bcmo = event.getBcmo();
			elementConge.ddeb = evconge.getDdeb();
			elementConge.ddpa = event.getDdpa();
			elementConge.dfin = evconge.getDfin();
			elementConge.dfpa = event.getDfpa();
			elementConge.mnt1 = fnomMotif.montant1;
			elementConge.mnt3 = fnomMotif.montant3;
			elementConge.mont = evconge.getMont();
			elementConge.motf = evconge.getMotf();
			elementConge.nbja = evconge.getNbja() != null ? evconge.getNbja().doubleValue() : 0;
			elementConge.nbjc = evconge.getNbjc() != null ? evconge.getNbjc().doubleValue() : 0;
			elementConge.cuti = evconge.getCuti();

			listOfEltVarConge.add(elementConge);
			BeanUtils.copyProperties(elementConge, save);
			listOfEltVarConge_ms.add(save);
		}

		if (nbjtr < 0)
			nbjtr = 0;

		if (StringUtil.isBlank(update.motif_cg_abs_ms))
			cgan_ms = false;
		return true;
	}

	/**
	 * =>trans_cong Transfert des conges sur mois suivant (wsal01.cals = 'N')
	 */
	private boolean transfererUnEVCGMoisSuivant(DetailEv detailEv, String nmat) throws Exception
	{
		refreshConnexion();
		if (update.genfile == 'O')
			printOut += "\n" + ("-----Transfert des evcg sur le mois suivant");

		ElementVariableConge evconge = null;
		ElementVariableEnteteMois eventete = null;
		CleEltVarEnt pk = null;
		boolean entExist = false;
		try
		{
			evconge = new ElementVariableConge();
			CleEltVarConge pkEvCg = new CleEltVarConge();
			pkEvCg.setIdEntreprise(new Integer(update.dossier));
			pkEvCg.setAamm(update.mois_ms);
			pkEvCg.setNmat(nmat);
			pkEvCg.setNbul(update.nbul_ms);
			pkEvCg.setDdeb(detailEv.ddeb);

			evconge.setIdEntreprise(new Integer(update.dossier));
			evconge.setAamm(update.mois_ms);
			evconge.setNmat(nmat);
			evconge.setNbul(update.nbul_ms);
			evconge.setDdeb(detailEv.ddeb);

			evconge.setDfin(detailEv.dfin);
			evconge.setNbjc(ClsStringUtil.truncateToXDecimal(new BigDecimal(detailEv.nbjc),2));
			evconge.setNbja(ClsStringUtil.truncateToXDecimal(new BigDecimal(detailEv.nbja),2));
			evconge.setMotf(detailEv.motf);
			evconge.setMont(detailEv.mont);
			evconge.setCuti(StringUtils.isNotEmpty(detailEv.cuti)?detailEv.cuti:update.user);
			evconge.setCuti(StringUtils.isNotEmpty(update.userEV)?update.userEV:evconge.getCuti());

			pk = new CleEltVarEnt(evconge.getIdEntreprise(), evconge.getAamm(), evconge.getNmat(), evconge.getNbul());
			ParameterUtil.println("Looking for congé entete " + pk.toString());
			if (!this.mapElementVariableEnteteMoisDejaRecherche.containsKey(pk))
			{
				// eventete = (ElementVariableEnteteMois) update.service.get(ElementVariableEnteteMois.class, pk);
				String query = "From ElementVariableEnteteMois where idEntreprise='" + pk.getIdEntreprise() + "' and aamm='" + pk.getAamm() + "' and nmat='" + pk.getNmat() + "'" + " and nbul="
						+ pk.getNbul();
				List<ElementVariableEnteteMois> lst = update.service.find(query);
				eventete = lst.isEmpty() ? null : lst.get(0);
				this.mapElementVariableEnteteMoisDejaRecherche.put(pk, eventete == null);
			}

			if (this.mapElementVariableEnteteMoisDejaRecherche.get(pk))
			{
				ElementVariableEnteteMois event = new ElementVariableEnteteMois();
				CleEltVarEnt pkEv = new CleEltVarEnt(evconge.getIdEntreprise(), evconge.getAamm(), evconge.getNmat(), evconge.getNbul());
				//event.setComp_id(new Integer(evconge.getCdos(), evconge.getAamm(), evconge.getNmat(), evconge.getNbul()));
				event.setIdEntreprise(evconge.getIdEntreprise());
				event.setAamm(evconge.getAamm());
				event.setNmat(evconge.getNmat());
				event.setNbul(evconge.getNbul());
				event.setDdpa(date_debut_mois_ms);
				event.setDfpa(date_fin_mois_ms);
				event.setBcmo("N");

				if (!mapElementVariableEnteteMoisDejaGenere.containsKey(pkEv))
				{
					oSession.save(event);
					mapElementVariableEnteteMoisDejaGenere.put(pkEv, pkEv);
				}

				// oSession.save(event);
			}

			if (!mapRhteltvarcongeDejaGenere.containsKey(pkEvCg))
			{
				oSession.save(evconge);
				mapRhteltvarcongeDejaGenere.put(pkEvCg, pkEvCg);
			}

		}
		catch (DataAccessException e)
		{
			e.printStackTrace();
			globalUpdate.logError("Mat : " + this.salarie.getNmat() + "\r\n" + ClsTreater._getStackTrace(e));
			return false;
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
			globalUpdate.logError("Mat : " + this.salarie.getNmat() + "\r\n" + ClsTreater._getStackTrace(e));
			return false;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			globalUpdate.logError("Mat : " + this.salarie.getNmat() + "\r\n" + ClsTreater._getStackTrace(e));
			return false;
		}
		return true;
	}

	private boolean transferEVCGMoisSuivant(String nmat)
	{
		refreshConnexion();
		if (update.genfile == 'O')
			printOut += "\n" + ("-----Transfert des evcg sur le mois suivant");

		DetailEv detailEv = null;
		ElementVariableConge evconge = null;
		ElementVariableEnteteMois eventete = null;
		CleEltVarEnt pk = null;
		try
		{
			for (int i = 0; i < listOfEltVarConge.size(); i++)
			{
				if (listOfEltVarConge.get(i).transferEVCGMoisSuivant_flag)
				{
					detailEv = listOfEltVarConge_ms.get(i);
					evconge = new ElementVariableConge();
					CleEltVarConge pkEvCg = new CleEltVarConge();
					pkEvCg.setIdEntreprise(new Integer(update.dossier));
					pkEvCg.setAamm(update.mois_ms);
					pkEvCg.setNmat(nmat);
					pkEvCg.setNbul(update.nbul_ms);
					pkEvCg.setDdeb(detailEv.ddeb);

					evconge.setIdEntreprise(new Integer(update.dossier));
					evconge.setAamm(update.mois_ms);
					evconge.setNmat(nmat);
					evconge.setNbul(update.nbul_ms);
					evconge.setDdeb(detailEv.ddeb);

					evconge.setDfin(detailEv.dfin);
					evconge.setNbjc(ClsStringUtil.truncateToXDecimal(new BigDecimal(detailEv.nbjc),2));
					evconge.setNbja(ClsStringUtil.truncateToXDecimal(new BigDecimal(detailEv.nbja),2));
					evconge.setMotf(detailEv.motf);
					evconge.setMont(detailEv.mont);
					evconge.setCuti(StringUtils.isNotEmpty(detailEv.cuti)?detailEv.cuti:update.user);
					evconge.setCuti(StringUtils.isNotEmpty(update.userEV)?update.userEV:evconge.getCuti());

					// evconge.setCuti(update.user);

					pk = new CleEltVarEnt(evconge.getIdEntreprise(), evconge.getAamm(), evconge.getNmat(), evconge.getNbul());
					if (!this.mapElementVariableEnteteMoisDejaRecherche.containsKey(pk))
					{
						// eventete = (ElementVariableEnteteMois) update.service.get(ElementVariableEnteteMois.class, pk);
						String query = "From ElementVariableEnteteMois where idEntreprise='" + pk.getIdEntreprise() + "' and aamm='" + pk.getAamm() + "' and nmat='" + pk.getNmat() + "'"
								+ " and nbul=" + pk.getNbul();
						List<ElementVariableEnteteMois> lst = update.service.find(query);
						eventete = lst.isEmpty() ? null : lst.get(0);

						this.mapElementVariableEnteteMoisDejaRecherche.put(pk, eventete == null);
					}

					if (this.mapElementVariableEnteteMoisDejaRecherche.get(pk))
					{
						ElementVariableEnteteMois event = new ElementVariableEnteteMois();
						event.setIdEntreprise(evconge.getIdEntreprise());
						event.setAamm(evconge.getAamm());
						event.setNmat(evconge.getNmat());
						event.setNbul(evconge.getNbul());
						event.setDdpa(date_debut_mois_ms);
						event.setDfpa(date_fin_mois_ms);
						event.setBcmo("N");

						if (!mapElementVariableEnteteMoisDejaGenere.containsKey(pk))
						{
							oSession.save(event);
							mapElementVariableEnteteMoisDejaGenere.put(pk, pk);
						}

						// oSession.save(event);
					}

					if (!mapRhteltvarcongeDejaGenere.containsKey(pkEvCg))
					{
						oSession.save(evconge);
						mapRhteltvarcongeDejaGenere.put(pkEvCg, pkEvCg);
						// sauvegarde du ok congé pour le mois
						String _strTypeMotif = "absence";
						ParamData fnom = (ParamData) update._getRhfnom(update.dossier, 22, evconge.getMotf(), 1);
						if (fnom != null)
							_strTypeMotif = fnom.getValm() == 1 ? "conge" : "absence";

						if (_strTypeMotif.equalsIgnoreCase("conge"))
						{
							fnom = (ParamData) update._getRhfnom(update.dossier, 22, evconge.getMotf(), 10);
							String strCrub = null;
							if (fnom != null)
							{
								strCrub = fnom.getVall();
								if (StringUtil.isNotBlank(strCrub))
								{
									strCrub = ClsStringUtil.formatNumber(NumberUtils.toLong(strCrub), ParameterUtil.formatRubrique);
									if (!StringUtil.equalsIgnoreCase(ParameterUtil.formatRubrique, strCrub))
									{
										ElementVariableDetailMois evar = new ElementVariableDetailMois();
										evar.setAamm(update.mois_ms);
										evar.setIdEntreprise(new Integer(update.dossier));
										evar.setNmat(nmat);
										evar.setNbul(update.nbul_ms);
										evar.setNlig(maj_eva_nlig++);
										evar.setCuti(update.user);
										evar.setCuti(StringUtils.isNotEmpty(update.userEV)?update.userEV:evar.getCuti());
										evar.setRubq(strCrub);
										evar.setArgu("OKCONGE");
										evar.setNprt(" ");
										evar.setRuba(" ");
										evar.setMont(new BigDecimal(1));
										oSession.save(evar);
									}
								}
							}

							fnom = (ParamData) update._getRhfnom(update.dossier, 22, evconge.getMotf(), 11);
							if (fnom != null)
							{
								strCrub = fnom.getVall();
								if (StringUtil.isNotBlank(strCrub))
								{
									strCrub = ClsStringUtil.formatNumber(NumberUtils.toLong(strCrub), ParameterUtil.formatRubrique);
									if (!StringUtil.equalsIgnoreCase(ParameterUtil.formatRubrique, strCrub))
									{
										ElementVariableDetailMois evar = new ElementVariableDetailMois();
										evar.setAamm(update.mois_ms);
										evar.setIdEntreprise(new Integer(update.dossier));
										evar.setNmat(nmat);
										evar.setNbul(update.nbul_ms);
										evar.setNlig(maj_eva_nlig++);
										evar.setCuti(update.user);
										evar.setCuti(StringUtils.isNotEmpty(update.userEV)?update.userEV:evar.getCuti());
										evar.setRubq(strCrub);
										evar.setArgu("OKCONGE");
										evar.setNprt(" ");
										evar.setRuba(" ");
										evar.setMont(evconge.getNbjc());
										oSession.save(evar);
									}
								}
							}

						}
					}
				}
			}
		}
		catch (DataAccessException e)
		{
			e.printStackTrace();
			globalUpdate.logError("Mat : " + this.salarie.getNmat() + "\r\n" + ClsTreater._getStackTrace(e));
			return false;
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
			globalUpdate.logError("Mat : " + this.salarie.getNmat() + "\r\n" + ClsTreater._getStackTrace(e));
			return false;
		}
		return true;
	}

	public int getIndexDernierMoisClotureDansListOfPeriodeT91()
	{
		for (int i = 0; i < update.listePeriodesT91.size(); i++)
		{
			if (StringUtil.equals(update.listePeriodesT91.get(i).periode, update.dernierePeriodeCloturee))
				return i;
		}
		return -1;
	}

	public int getIndexMoisCongeDansListOfPeriodeT91(DetailEv detailEv)
	{
		for (int i = 0; i < update.listePeriodesT91.size(); i++)
		{
			if (detailEv.ddeb.compareTo(update.listePeriodesT91.get(i).ddebut) >= 0 && detailEv.ddeb.compareTo(update.listePeriodesT91.get(i).dfin) <= 0)
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
		refreshConnexion();
		if (update.genfile == 'O')
			printOut += "\n" + ("-----Alimentation des conges sur le mois suivant");

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
						// if (!insertCongeMoisSuivant(evcg, nmat))
						// {
						// return false;
						// }
						evcg.pas_moi_flag = true;

						if ("O".equals(update.fictif) && "B".equals(update.typ_fictif) && evcg.ddeb.compareTo(update.listePeriodesT91.get(t91_imcg).ddebut) > 0)
						{
							evcg_ms = new DetailEv();// evcg_ms = evcg; // -- On fait une copie de l'object courant, pour ne pas alterer ses valeurs
							BeanUtils.copyProperties(evcg, evcg_ms);
							evcg_ms.dfin = new ClsDate(evcg.ddeb).addDay(-1);
							evcg_ms.ddeb = update.listePeriodesT91.get(t91_imcg).ddebut;
							//
							nbreJourOuvrableNonOuvrable(evcg_ms.ddeb, evcg_ms.dfin);
							evcg_ms.nbja = nbj_a;
							evcg_ms.nbjc = 0;
							//
							// if (!insertCongeMoisSuivant(evcg, nmat))
							// {
							// return false;
							// }
							evcg.pas_moi_flag = true;
							listOfEltVarConge_ms.set(i, evcg_ms); // -- on fixe l'object é générer le mois suivant
						}
					}
				}
				else
				{
					am = new ClsDate(evcg.ddeb).getYearAndMonth();

					if (NumberUtils.toLong(am) > NumberUtils.toLong(update.periode))
					{
						// if (!insertCongeMoisSuivant(evcg, nmat))
						// {
						// return false;
						// }
						evcg.pas_moi_flag = true;

						String s = new ClsDate(evcg.ddeb).getYearAndMonth();
						ddpa_ms = new ClsDate(s, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getDate();

						if ("O".equals(update.fictif) && "B".equals(update.typ_fictif) && evcg.ddeb.compareTo(ddpa_ms) > 0)
						{
							evcg_ms = new DetailEv();// evcg_ms = evcg; // -- On fait une copie de l'object courant, pour ne pas alterer ses valeurs
							BeanUtils.copyProperties(evcg, evcg_ms);
							//
							evcg_ms.dfin = new ClsDate(evcg.ddeb).addDay(-1);
							evcg_ms.ddeb = ddpa_ms;
							//
							nbreJourOuvrableNonOuvrable(evcg.ddeb, evcg.dfin);
							evcg_ms.nbja = nbj_a;
							evcg_ms.nbjc = 0;

							// if (!insertCongeMoisSuivant(evcg, nmat))
							// {
							// return false;
							// }
							evcg.pas_moi_flag = true;
							listOfEltVarConge_ms.set(i, evcg_ms); // -- on fixe l'object é générer le mois suivant
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
	 * @return true if tout s'est bien passé; et false sinon
	 */
	private boolean insertCongeMoisSuivant(DetailEv infoperiode, String nmat) throws Exception
	{
		refreshConnexion();
		if (update.genfile == 'O')
			printOut += "\n" + (">>insertCongeMoisSuivant");
		// ParameterUtil.println(">>insertCongeMoisSuivant");

		Date ddpa = null;
		Date dfpa = null;

		Object o = null;
		CleEltVarEnt pk = null;

		if (ClsObjectUtil.isNull(update.motif_cg_abs_ms))
			return true;

		ElementVariableConge evconge = new ElementVariableConge();
		CleEltVarConge pkEvCg = new CleEltVarConge(new Integer(update.dossier), update.prochainmois, nmat, 9, infoperiode.ddeb);

		evconge.setIdEntreprise(new Integer(update.dossier));
		evconge.setAamm(update.prochainmois);
		evconge.setNmat(nmat);
		evconge.setNbul(9);
		evconge.setDdeb(infoperiode.ddeb);
		evconge.setDfin(infoperiode.dfin);
		evconge.setNbjc(ClsStringUtil.truncateToXDecimal(new BigDecimal(infoperiode.nbjc),2));
		if (StringUtil.equals(globalUpdate.nomClient, ClsEntreprise.EDM))
			evconge.setNbjc(new BigDecimal(0));
		evconge.setNbja(ClsStringUtil.truncateToXDecimal(new BigDecimal(infoperiode.nbja),2));
		evconge.setMotf(infoperiode.motf);
		evconge.setMont(infoperiode.mont);
		evconge.setCuti(update.user);
		evconge.setCuti(StringUtils.isNotEmpty(update.userEV)?update.userEV:evconge.getCuti());

		ddpa = date_debut_prochainmois;
		dfpa = date_fin_prochainmois;

		evconge.setMotf(update.motif_cg_abs_ms);

		pk = new CleEltVarEnt(evconge.getIdEntreprise(), evconge.getAamm(), evconge.getNmat(), evconge.getNbul());
		if (!this.mapElementVariableEnteteMoisDejaRecherche.containsKey(pk))
		{
			// o = (ElementVariableEnteteMois) update.service.get(ElementVariableEnteteMois.class, pk);
			String query = "From ElementVariableEnteteMois where idEntreprise='" + pk.getIdEntreprise() + "' and aamm='" + pk.getAamm() + "' and nmat='" + pk.getNmat() + "'" + " and nbul="
					+ pk.getNbul();
			List<ElementVariableEnteteMois> lst = update.service.find(query);
			o = lst.isEmpty() ? null : lst.get(0);
			this.mapElementVariableEnteteMoisDejaRecherche.put(pk, o == null);
		}

		if (this.mapElementVariableEnteteMoisDejaRecherche.get(pk))
		{
			ElementVariableEnteteMois event = new ElementVariableEnteteMois();
			event.setIdEntreprise(evconge.getIdEntreprise());
			event.setAamm(evconge.getAamm());
			event.setNmat(evconge.getNmat());
			event.setNbul(evconge.getNbul());
			event.setDdpa(ddpa);
			event.setDfpa(dfpa);
			event.setBcmo("N");
			//

			if (!mapElementVariableEnteteMoisDejaGenere.containsKey(pk))
			{
				oSession.save(event);
				mapElementVariableEnteteMoisDejaGenere.put(pk, pk);
			}
		}
		//
		boolean existEVCG = mapRhteltvarcongeDejaGenere.containsKey(pkEvCg);

		String query = " Select a.* From ElementVariableConge a where a.idEntreprise = '" + update.dossier + "' and a.aamm = '" + evconge.getAamm() + "' and a.nmat = '"
				+ evconge.getNmat() + "' and a.nbul = " + evconge.getNbul() + "  and a.ddeb = :ddeb";
		List<ElementVariableConge> liste = null;
		Query q = oSession.createSQLQuery(query).addEntity("a", ElementVariableConge.class);
		q.setParameter("ddeb", evconge.getDdeb(), StandardBasicTypes.DATE);
		liste = q.list();
		o = liste.isEmpty() ? null : liste.get(0);

		// o = update.service.get(Rhteltvarconge.class, evconge.getComp_id());
		if (o == null && !existEVCG)
		{
			oSession.save(evconge);
			mapRhteltvarcongeDejaGenere.put(pkEvCg, pkEvCg);
		}
		return true;
	}

	/**
	 * =>calc_nb_jours Calcul du nombre de jours ouvrables et non ouvrables entre 2 dates (jours reels)
	 */
	private void nbreJourOuvrableNonOuvrable(Date debut, Date fin)
	{
		refreshConnexion();
		if (update.genfile == 'O')
			printOut += "\n" + ("-----nbreJourOuvrableNonOuvrable");

		nbj_a = update.updateservice.computeNbjrAbsences(update.dossier, new ClsDate(debut).getDateS(update.dateformat), new ClsDate(fin).getDateS(update.dateformat));

		nbj_c = update.updateservice.computeNbjrConges(update.dossier, new ClsDate(debut).getDateS(update.dateformat), new ClsDate(fin).getDateS(update.dateformat));
	}

	/**
	 * =>maj_conges_annuels Conges payes pris
	 */
	private boolean updateCongeAnnuel(DetailEv ev, Salarie salarie)
	{
		refreshConnexion();
		if (update.genfile == 'O')
			printOut += "\n" + ("-----maj des conges annuels");

		double wnbjcad2 = 0;
		// nbj conge a deduire
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
			ParameterUtil.println("Setting pmcf =null, case updateCongeAnnuel");
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

			// -- ----- Mise a jour des droits en jrs
			// -- LH 151097 Specif SHELL : On deduit d'abord des jours de reliquat
			// -- On deduit d'abord des jours de reliquat anterieurs
			if (StringUtil.equals(ClsEntreprise.SHELL_GABON, this.update.parameter.nomClient))
			{
				if (wnbjcad > salarie.getJrla().doubleValue())
				{
					wnbjcad = wnbjcad - salarie.getJrla().doubleValue();
					salarie.setJrla(BigDecimal.ZERO);
				}
				else
				{
					salarie.setJrla(new BigDecimal(salarie.getJrla().doubleValue() - wnbjcad));
					wnbjcad = 0;
				}
				// -- On deduit ensuite des jours de reliquat en cours
				if (wnbjcad > salarie.getJrlec().doubleValue())
				{
					wnbjcad = wnbjcad - salarie.getJrlec().doubleValue();
					salarie.setJrlec(BigDecimal.ZERO);
				}
				else
				{
					salarie.setJrlec(salarie.getJrlec().subtract(new BigDecimal(wnbjcad)));
					wnbjcad = 0;
				}
			}

			// -- On deduit ensuite des jours a prendre anterieurs puis en cours

			if (wnbjcad <= salarie.getJapa().doubleValue())
			{
				salarie.setJapa(new BigDecimal(salarie.getJapa().doubleValue() - wnbjcad));
			}
			else
			{
				salarie.setJapec(new BigDecimal(salarie.getJapec().doubleValue() - (wnbjcad - salarie.getJapa().doubleValue())));
				salarie.setJapa(new BigDecimal(0));
			}

//			if (ev.dfin.equals(salarie.getDfcf()))
			if(new ClsDate(ev.dfin).getDateS("yyyyMMdd").equalsIgnoreCase(new ClsDate(salarie.getDfcf()).getDateS("yyyyMMdd")))
			{
				salarie.setDrtcg(ev.dfin);
				if (StringUtil.equals(ClsEntreprise.TASIAST_MAURITANIE, globalUpdate.nomClient) && StringUtil.notEquals(this.update.dossier, "02"))
				{
					salarie.setDrtcg(salarie.getDdcf());
					// System.out.println("Nmat "+salarie.getNmat()+" , drtcg = "+ClsDate.getDateS(salarie.getDdcf(), "dd/MM/yyyy"));
				}
			}
			if (StringUtil.equals(ClsEntreprise.COMILOG, globalUpdate.nomClient))
					salarie.setDrtcg(ev.dfin);
			if (StringUtil.equals(ClsEntreprise.BANQUE_COMMERCIALE_INTERNATIONALE, globalUpdate.nomClient))
				salarie.setDrtcg(ev.dfin);
			// -- LH 151097 Specif SHELL :
			// -- Les jours restants a prendre sont passes dans le reliquat
			if (StringUtil.equals(ClsEntreprise.SHELL_GABON, globalUpdate.nomClient))
			{
				salarie.setJrla(salarie.getJrla().add(salarie.getJapa()));
				salarie.setJrlec(salarie.getJrlec().add(salarie.getJapec()));
				salarie.setJapa(new BigDecimal(0));
				salarie.setJapec(new BigDecimal(0));
			}

			ca = true;

		}

		return true;
	}

	// ---------------------------------------------------------------------------------
	// -- Mise a jour de la base conges salarie
	// ---------------------------------------------------------------------------------
	private boolean updateBaseCongeAgent(Salarie salarie)
	{
		refreshConnexion();
		if (update.genfile == 'O')
			printOut += "\n" + ("-----maj de la base congé");

		boolean l_expat = false;
		ParamData fnom = null;
		if (bas_con != 0)
		{
			if (!mois_conge)
			{
				if (StringUtil.isBlank(update.val_exp))
				{
					fnom = update._getRhfnom(update.dossier, 35, salarie.getCat(), 3);
					if (fnom != null && fnom.getValt() != null)
					{
						nbj_con = fnom.getValt().doubleValue();
					}
					else nbj_con = update.nbjc_lx;
					if (nbj_con <= 0)
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
						else nbj_con = update.nbjc_ex;
						if (nbj_con <= 0)
							nbj_con = update.nbjc_ex;
					}
					// ELSE
					else
					{
						fnom = (update._getRhfnom(update.dossier, 35, salarie.getCat(), 3));
						if (fnom != null && fnom.getValt() != null)
							nbj_con = fnom.getValt().doubleValue();
						else nbj_con = update.nbjc_lx;
						if (nbj_con <= 0)
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
			ParameterUtil.println("Setting pmcf =null, case updateBaseCongeAgent, and radié");
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

			// salarie.update_flag = true

			return true;
		}

		ClsDate anniv = new ClsDate(date_anniv);

		if ((update.myPeriode.getYear() != anniv.getYear()) && (update.myPeriode.getMonth() == anniv.getMonth()))
		{
			if(StringUtil.equalsIgnoreCase(globalUpdate.nomClient, ClsEntreprise.BGFIGE)){
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

				if (StringUtil.in(update.calcul_auto_nbjsup, "A,O"))
				{
					retour = calculateNbreJourSupplementaire(salarie);
					if(!retour) return false;
				}
				if ("O".equals(update.calcul_auto_nbjsup))
				{
					salarie.setJapec(salarie.getJapec().add(new BigDecimal(nbj_con + nbj_anc + nbj_enf + nbj_deco)));
				}
				else if ("A".equals(update.calcul_auto_nbjsup))
				{
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
			if(StringUtil.equalsIgnoreCase(globalUpdate.nomClient, ClsEntreprise.BGFIGE)){
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
			ParameterUtil.println("Setting pmcf =null, case updateBaseCongeAgent, case conges_annuels et finconge");
			salarie.setPmcf("");

			salarie.setNbjtr(new BigDecimal(nbjtr));

			if ("O".equals(update.fictif) && "A".equals(update.typ_fictif))
			{
				salarie.setDapec(new BigDecimal(bas_con));
				salarie.setNbjtr(new BigDecimal(nbjtr));
			}
			else
			{
				BigDecimal dapec = BigDecimal.ZERO;
				//Si les congés sont fractionnées, alors dans zli10 on retrouve la base congé restante
				try
				{
					dapec = new BigDecimal(salarie.getZli10());
					salarie.setZli10(null);
				}
				catch(Exception e)
				{
					dapec = BigDecimal.ZERO;
				}

				salarie.setDapec(dapec);

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

		// update_flag = true;
		return true;
	}

	// ---------------------------------------------------------------------------------
	// -- Calcul du nombre de jours supplementaires
	// ---------------------------------------------------------------------------------
	private boolean calculateNbreJourSupplementaire(Salarie salarie)
	{
		refreshConnexion();
		if (update.genfile == 'O')
			printOut += "\n" + ("-----calcul du nombre de jours suppl");

		try
		{

			int nb_enf = 0;
			double nbj_par_enf = 0;
			int l_tranche = 0;

			ClsUpdateBulletin.RhfnomCalcPartFiscal fnom = null;
			ClsDate ddca = new ClsDate(salarie.getDdca());
			int x_an = update.myPeriode.getYear();
			int x_mois = update.myPeriode.getMonth();
			int wmois = x_an - ddca.getYear();
			wmois = (wmois * 12) + x_mois;
			wmois = wmois - ddca.getMonth();
			int nbr_an = new BigDecimal(Math.floor(wmois / 12)).intValue();
			//
			ancien = nbr_an;
			if (ancien < 0)
				ancien = 0;
			fnom = update.getRhfnomCalcPartFiscal(salarie.getCat());
			if (fnom.montant3 <= 0)
				fnom.montant3 = 1;
			if (fnom.taux1 <= 0)
				fnom.taux1 = 0;
			if (fnom.taux2 <= 0)
				fnom.taux2 = 0;
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
					String sdate = ClsStringUtil.formatNumber(sysdate.getDay(), "00") + "-" + ClsStringUtil.formatNumber(sysdate.getMonth(), "00") + "-"
							+ ClsStringUtil.formatNumber((sysdate.getYear() - update.age_max_enfant), "0000");
					Date date_min = new ClsDate(sdate, "dd-MM-yyyy").getDate();
					//
					nb_enf = update.updateservice.getNbEnfantsSalarie(update.dossier, salarie.getNmat(), new ClsDate(date_min, this.update.dateformat).getDateS());
				}
				else
				{
					nb_enf = salarie.getNbec();
				}
				//
				if (nb_enf < update.minimum_enfant)
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
			globalUpdate.logError("Mat : " + this.salarie.getNmat() + "\r\n" + ClsTreater._getStackTrace(e));
			return false;
		}
		return true;
	}

	// ---------------------------------------------------------------------------------
	// -- Calcul du proratas sur les jours de droits acquis sur le mois
	// ---------------------------------------------------------------------------------
	private boolean calculProrataNbreJourDroitParMois()
	{
		refreshConnexion();
		if (update.genfile == 'O')
			printOut += "\n" + ("-----calcul du prorata sur nbr de jours");
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
					if (sav_dfcf.compareTo(update.myPeriode.getLastDayOfMonth()) >= 0)
						nbj_con = 0;
					else nbj_con = nbj_con * (30 - new ClsDate(sav_dfcf).getDay()) / 30;
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
			else nbj_con = 0;
		}

		return true;
	}

	// ---------------------------------------------------------------------------------
	// -- Suppression des EVs du mois courant
	// ---------------------------------------------------------------------------------
	private boolean deleteEVMois(Salarie salarie) throws Exception
	{
		refreshConnexion();
		if (update.genfile == 'O')
			printOut += "\n" + ("-----suppression des ev du mois");

		refreshConnexion();
		if (update.genfile == 'O')
			printOut += "\n" + (">>deleteEVMois");
		boolean result = true;
		try
		{
			result = update.updateservice.deleteEVMois(oSession, update.dossier, update.periode, update.numerobulletin, salarie.getNmat());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			result = false;
			globalUpdate._setEvolutionTraitement(update.request, ClsTreater._getStackTrace(e), true);
			globalUpdate.logError("Mat : " + this.salarie.getNmat() + "\r\n" + ClsTreater._getStackTrace(e));
		}
		return result;
	}

	// ---------------------------------------------------------------------------------
	// -- Suppression des elements fixes
	// ---------------------------------------------------------------------------------
	private boolean deleteEFMois(Salarie salarie) throws Exception
	{
		refreshConnexion();
		if (update.genfile == 'O')
			printOut += "\n" + ("-----suppression des elt fixes du mois");
		return update.updateservice.deleteEFMois(oSession, update.dossier, update.myPeriode.getDateS(update.dateformat), salarie.getNmat());
	}

	// ---------------------------------------------------------------------------------
	// -- MISE A JOUR DES ZONES CONCERNANT LES CONGES
	// ---------------------------------------------------------------------------------
	private boolean updateZoneConge(Salarie salarie)
	{
		refreshConnexion();
		if (update.genfile == 'O')
			printOut += "\n" + ("-----maj des zones conges");
		// spécifique cnss
		if (StringUtil.equals(globalUpdate.nomClient, ClsEntreprise.CNSS))
		{
			return updateZoneCongeCNSS(salarie);
		}

		updateZoneCongeGeneral();

		return true;
	}

	private void updateZoneCongeGeneral()
	{
		if (update.genfile == 'O')
			printOut += "\n" + ("-----maj des zones conges général");
		DetailEv ev = null;
		for (int i = 0; i < listOfEltVarConge.size(); i++)
		{
			ev = (DetailEv) listOfEltVarConge.get(i);
			if (ev.mnt1 == 0)
			{
				ParameterUtil.println("Cas mnt1=0");
				if (StringUtil.equals(update.motif_reliq, ev.motf))
					retour = updateJourDesJourReliquat(salarie, ev);

				if (!update.motif_cg_abs_ms.equals(ev.motf))
				{
					ParameterUtil.println("Cas motf!=motif_cg_abs_ms");
					// retour = updateDetailAbsConge(ev, salarie.getNmat());
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
					ParameterUtil.println("Cas du fictif é N ou du fictif B ,periode = " + update.periode + " et pmcf = " + salarie.getPmcf());
					if (update.periode.equals(sav_pmcf))//salarie.getPmcf()))
					{
						ParameterUtil.println("Periode de paie egal é pmcf");
						// retour = updateDetailAbsConge(ev, salarie.getNmat());
						ev.maj_det_flag = true;
						// -- Les jours sont tous defalques pour etre en phase avec les droits
						if (conges_annuels)
						{
							ParameterUtil.println("cas d'un conge annuel, finconge=true");
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
						// retour = updateDetailAbsConge(ev, salarie.getNmat()); // -- Les jours sont tous defalques pour etre en phase avec les
						// droits
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
	}

	private boolean updateZoneCongeCNSS(Salarie salarie)
	{
		refreshConnexion();
		if (update.genfile == 'O')
			printOut += "\n" + ("-----maj des zones conges");

		double mont_pris = 0;
		double mont_acquis = 0;
		double jours_pris = 0;
		double jours_acquis = 0;
		String annee;
		String mois;

		DetailEv ev = null;
		// IF w_conge_montant THEN
		if (update.conge_montant) // TFN 122006 Traitement des congés en montantt
		{
			// --Mise é jours des éléments dans paconge en fonction des éléments variables saisis
			// FOR i IN 1..nb_evcg LOOP
			// wconge.cdos := wpdos.cdos;
			// wconge.nmat := wsal01.nmat;
			// wconge.ddcg := tab_evcg_ddeb(i);
			// wconge.dfcg := tab_evcg_dfin(i);
			// wconge.nbja := tab_evcg_nbja(i);
			// wconge.nbjc := tab_evcg_nbjc(i);
			// wconge.cmcg := tab_evcg_motf(i);
			// wconge.mtcg := tab_evcg_mont(i);
			// BEGIN
			// INSERT INTO paconge
			// VALUES (wconge.cdos,wconge.nmat,wconge.ddcg,wconge.dfcg,
			// wconge.nbja,wconge.nbjc,wconge.cmcg,wconge.mtcg);
			// EXCEPTION
			// WHEN DUP_VAL_ON_INDEX THEN null;
			// END;
			// END LOOP;
			for (int i = 0; i < listOfEltVarConge.size(); i++)
			{
				ev = (DetailEv) listOfEltVarConge.get(i);
				ev.maj_det_flag = true;
			}
			// BEGIN
			// select taux,mont into w_jours_pris, w_mont_pris from pacalc
			// where rubq=w_rub_pris
			// and cdos =wpdos.cdos
			// and aamm=w_aamm
			// and nmat=wsal01.nmat;
			// EXCEPTION
			// WHEN NO_DATA_FOUND THEN
			// w_jours_pris:=0;
			// w_mont_pris:=0;
			// END;
			List<Calcul> lsCalc = update.service.find("From CalculPaie where idEntreprise = '" + update.dossier + "' and rubq = '" + update.rub_pris + "' and nmat = '"
					+ salarie.getNmat() + "' and aamm ='" + update.periode + "'");
			if (!lsCalc.isEmpty())
			{
				jours_pris = lsCalc.get(0).getTaux().doubleValue();
				mont_pris = lsCalc.get(0).getMont().doubleValue();
			}

			// BEGIN
			// select mont into w_jours_acquis from pacalc
			// where rubq= w_rub_jours_acquis
			// and cdos =wpdos.cdos
			// and aamm=w_aamm
			// and nmat=wsal01.nmat;
			// EXCEPTION
			// WHEN NO_DATA_FOUND THEN
			// w_jours_acquis:=0;
			// END;
			lsCalc = update.service.find("From CalculPaie where idEntreprise = '" + update.dossier + "' and rubq = '" + update.rub_jours_acquis + "' and nmat = '"
					+ salarie.getNmat() + "' and aamm ='" + update.periode + "'");
			if (!lsCalc.isEmpty())
			{
				jours_acquis = lsCalc.get(0).getMont().doubleValue();
			}
			//
			// BEGIN
			// select mont into w_mont_acquis from pacalc
			// where rubq= w_rub_montant_acquis
			// and cdos =wpdos.cdos
			// and aamm=w_aamm
			// and nmat=wsal01.nmat;
			// EXCEPTION
			// WHEN NO_DATA_FOUND THEN
			// w_mont_acquis:=0;
			// END;
			lsCalc = update.service.find("From CalculPaie where idEntreprise = '" + update.dossier + "' and rubq = '" + update.rub_montant_acquis + "' and nmat = '"
					+ salarie.getNmat() + "' and aamm ='" + update.periode + "'");
			if (!lsCalc.isEmpty())
			{
				mont_acquis = lsCalc.get(0).getMont().doubleValue();
			}

			// mois := to_char(w_date_anniv,'MM');
			// annee := to_char(w_date_anniv,'YYYY');
			// IF substr(w_aamm,1,4) != annee AND substr(w_aamm,5,2) = mois THEN
			// -- Date anniversaire, on bascule !
			// wsal01.japa := wsal01.japa + wsal01.japec;
			// wsal01.japec := 0;
			// wsal01.dapa := wsal01.dapa + wsal01.dapec;
			// wsal01.dapec := 0;
			// END IF;
			ClsDate anniv = new ClsDate(date_anniv);
			ClsDate per = new ClsDate(update.periode, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);

			if ((update.myPeriode.getYear() != anniv.getYear()) && (update.myPeriode.getMonth() == anniv.getMonth()))
			{
				// -- Date anniversaire, on bascule !
				salarie.setJapa(salarie.getJapa().add(salarie.getJapec()));
				salarie.setJapec(new BigDecimal(0));
				salarie.setDapa(salarie.getDapa().add(salarie.getDapec()));
				salarie.setDapec(new BigDecimal(0));
			}

			// IF wsal01.japa >= w_jours_pris THEN
			// wsal01.dapa :=wsal01.dapa - w_mont_pris;
			// wsal01.japa :=wsal01.japa - w_jours_pris;
			// wsal01.dapec :=wsal01.dapec + w_mont_acquis;
			// wsal01.japec :=wsal01.japec + w_jours_acquis;
			// ELSE
			// wsal01.dapec := wsal01.dapec + wsal01.dapa - w_mont_pris + w_mont_acquis;
			// wsal01.dapa :=0;
			// wsal01.japec := wsal01.japec + wsal01.japa - w_jours_pris + w_jours_acquis;
			// wsal01.japa :=0;
			// END IF;
			if (salarie.getJapa().doubleValue() >= jours_pris)
			{
				salarie.setDapa(salarie.getDapa().subtract(new BigDecimal(mont_pris)));
				salarie.setJapa(salarie.getJapa().subtract(new BigDecimal(jours_pris)));
				salarie.setDapec(salarie.getDapec().add(new BigDecimal(mont_acquis)));
				salarie.setJapec(salarie.getJapec().add(new BigDecimal(jours_acquis)));
			}
			else
			{
				salarie.setDapec(salarie.getDapec().add(salarie.getDapa()).subtract(new BigDecimal(mont_pris)).add(new BigDecimal(mont_acquis)));
				salarie.setDapa(new BigDecimal(0));
				salarie.setJapec(salarie.getJapec().add(salarie.getJapa()).subtract(new BigDecimal(jours_pris)).add(new BigDecimal(jours_acquis)));
				salarie.setJapa(new BigDecimal(0));
			}

		}
		// ELSE -- Cas général
		else
		{
			updateZoneCongeGeneral();
		}
		return true;
	}

	// ---------------------------------------------------------------------------------
	// -- Mise a jour des jours de reliquat
	// ---------------------------------------------------------------------------------
	private boolean updateJourDesJourReliquat(Salarie salarie, DetailEv ev)
	{
		refreshConnexion();
		if (update.genfile == 'O')
			printOut += "\n" + ("-----maj du nb de jours de reliquat");

		Integer o = update.updateservice.countNbJourReliquat(update.dossier, new ClsDate(ev.ddeb).getDateS(update.dateformat), new ClsDate(ev.dfin).getDateS(update.dateformat));
		salarie.setJrla(new BigDecimal(NumberUtils.nvl(salarie.getJrla(), 0).longValue() - o));
		return true;
	}

	// ---------------------------------------------------------------------------------
	// -- Mise a jour du detail des conges et absences
	// ---------------------------------------------------------------------------------
	private Map<String, List<HistoCongeSalarie>> congesagent = new HashMap<String, List<HistoCongeSalarie>>();

	private boolean updateDetailAbsConge(DetailEv ev, String nmat) throws Exception
	{
		refreshConnexion();
		if (update.genfile == 'O')
			printOut += "\n" + ("-----updateDetailAbsConge");

		HistoCongeSalarie conge = new HistoCongeSalarie();//new HistoCongeSalariePK(update.dossier, nmat, ev.ddeb));
		conge.setIdEntreprise(new Integer(update.dossier));
		conge.setNmat(nmat);
		conge.setDdcg(ev.ddeb);
		conge.setMtcg(ev.mont);
		conge.setNbja(ClsStringUtil.truncateToXDecimal(new BigDecimal(ev.nbja),2));
		conge.setNbjc(ClsStringUtil.truncateToXDecimal(new BigDecimal(ev.nbjc),2));
		conge.setCmcg(ev.motf);
		conge.setDfcg(ev.dfin);
		if (!congesagent.containsKey(ev.getMotf()))
			congesagent.put(ev.motf, new ArrayList<HistoCongeSalarie>());

		congesagent.get(ev.motf).add(conge);
		// oSession.saveOrUpdate(conge);
		return true;
	}

	private boolean saveDetailAbsConge() throws Exception
	{
		Iterator<String> iter = congesagent.keySet().iterator();
		List<HistoCongeSalarie> liste = null;
		List<HistoCongeSalarie> finale = null;
		HistoCongeSalarie conge = null;
		HistoCongeSalarie congeav = null;
		while (iter.hasNext())
		{
			finale = new ArrayList<HistoCongeSalarie>();
			liste = congesagent.get(iter.next());
			// if (StringUtil.equals("O", update.fusionHistoCongeSalarie))
			// {
			// Collections.sort(liste, new ClsGenericComparator(HistoCongeSalarie.class, "ddcg", "ASC"));
			// for (int i = 0; i < liste.size(); i++)
			// {
			// conge = liste.get(i);
			// if (i == 0)
			// {
			// finale.add(conge);
			// continue;
			// }
			// congeav = liste.get(i - 1);
			//
			// if (StringUtil.equals(new ClsDate(new ClsDate(conge.getDdcg()).addDay(-1)).getDateS("dd/MM/yyyy"), new
			// ClsDate(congeav.getDfcg()).getDateS("dd/MM/yyyy")))
			// {
			// congeav.setDfcg(conge.getDfcg());
			// congeav.setMtcg(congeav.getMtcg().add(conge.getMtcg()));
			// congeav.setNbja(congeav.getNbja().add(conge.getNbja()));
			// congeav.setNbjc(congeav.getNbjc().add(conge.getNbjc()));
			// }
			// else
			// finale.add(conge);
			// }
			// }
			// else
			finale.addAll(liste);
			for (int j = 0; j < finale.size(); j++)
			{
				oSession.saveOrUpdate(finale.get(j));
			}
		}

		return true;
	}

	// ---------------------------------------------------------------------------------
	// -- Conges payes ponctuels
	// -- Les jours de conges PONCTUELS sont deduits des jours a prendre
	// -- et stockes dans la zone des jours deductibles.
	// ---------------------------------------------------------------------------------
	private boolean updateCongePayePonctuel(DetailEv ev, Salarie salarie)
	{
		refreshConnexion();
		if (update.genfile == 'O')
			printOut += "\n" + ("-----maj coge ponctuel");
		double wnbjcad = 0;

		if ("O".equals(update.rhpdo.getDccg()))
			wnbjcad = ev.nbjc;
		else wnbjcad = ev.nbja;

		if (salarie.getJded() != null)
			salarie.setJded(salarie.getJded().add(new BigDecimal(wnbjcad)));
		else salarie.setJded(new BigDecimal(wnbjcad));


		if (wnbjcad <= salarie.getJapa().doubleValue())
		{
			if (salarie.getJapa() != null)
				salarie.setJapa(salarie.getJapa().subtract(new BigDecimal(wnbjcad)));
			else salarie.setJapa(new BigDecimal(-1 * wnbjcad));
		}
		else
		{
			double i = salarie.getJapa().doubleValue();
			if (salarie.getJapec() != null)
				salarie.setJapec(salarie.getJapec().subtract(new BigDecimal(wnbjcad - i)));
			else salarie.setJapec(new BigDecimal(i - wnbjcad));
			//
			salarie.setJapa(new BigDecimal(0));
		}
		return true;
	}

	// ---------------------------------------------------------------------------------
	// -- Calcul du nombre de part fiscal automatiquement et maj de la fiche sal.
	// -- en fonction de age maxi
	// ---------------------------------------------------------------------------------

	private boolean calculPartFiscale(Salarie salarie)
	{
		refreshConnexion();
		if (update.genfile == 'O')
			printOut += "\n" + ("-----calculPartFiscale");

		//Détermination de la fréquence de calcul: A,S,T,M
		String frequence = "M";
		ParamData fnom1 = this.update.updateservice.chercherEnNomenclature(update.dossier, 99, "CALCPART", 7);
		if(fnom1 != null && StringUtil.isNotBlank(fnom1.getVall()))
		{
			frequence = fnom1.getVall().trim();
			if(StringUtil.notIn(frequence, "A,S,T,M")) frequence = "M";
		}
		int mois = update.myPeriode.getMonth();
		if(StringUtil.equals("A", frequence) && mois != 12) return true;
		if(StringUtil.equals("S", frequence) && mois != 12 && mois != 6) return true;
		if(StringUtil.equals("T", frequence) && mois != 12 && mois != 9 && mois != 6 && mois != 3) return true;


		if (1 == 1)
		{
//
//			double result = ClsAgent._computeSalaryNumberOfParts(update.service, update.dossier, salarie.getSitf(), Integer.valueOf(salarie.getNbec()), salarie.getNbpt().toString());
//			salarie.setNbpt(new BigDecimal(result));
			return true;
		}

		@SuppressWarnings("unchecked")
		List l = update.updateservice.getDtnaEnfantsSalarie(update.dossier, salarie.getNmat());

		int enfant_a_charge = 0;
		Date curdate = update.myPeriode.getLastDayOfMonth();
		Date dtna = null;
		long age_enfant = 0;
		for (Object obj : l)
		{
			if (obj != null)
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

			// update_flag = true;
		}
		return true;
	}

	public boolean _UpdateZoneCongeFictifAgent(Salarie agent)
	{
		if (StringUtil.equals(globalUpdate.nomClient, ClsEntreprise.CNSS))
			return true;

		if(StringUtil.equals(update.periode,update.mois_ms)) return true;

		String query = "From ElementVariableConge where idEntreprise='" + update.dossier + "'  and aamm='" + update.mois_ms + "' and nbul=9 and nmat='"
				+ agent.getNmat() + "' order by ddeb";
		ParameterUtil.println("Début sélection des conges du salarié");
		List<ElementVariableConge> conges = oSession.createQuery(query).list();
		ParameterUtil.println("Fin sélection des conges du salarié");
		if (conges.isEmpty())
			return true;

		ParamData motif = update.service.findAnyColumnFromNomenclature(update.dossier, update.langue, "22", conges.get(0).getMotf(), "1");
		if (motif!=null && motif.getValm()!=null && motif.getValm().intValue()==1)
		{
			ParameterUtil.println("Fin Test si conge annuel 1");
			if (agent.getNbjcf() == null)
				agent.setNbjcf(BigDecimal.ZERO);
			if (agent.getNbjaf() == null)
				agent.setNbjaf(BigDecimal.ZERO);
			for (ElementVariableConge cg : conges)
			{
				agent.setNbjcf(agent.getNbjcf().add(cg.getNbjc()));
				agent.setNbjaf(agent.getNbjaf().add(cg.getNbja()));
				agent.setDdcf(conges.get(0).getDdeb());
				agent.setDfcf(cg.getDfin());
				agent.setMtcf(new BigDecimal(0));
				agent.setPmcf(cg.getAamm());
			}
		}
		return true;
	}

	public void agentUpdateToString()
	{

		String salarieString = "\n->Japa = " + salarie.getJapa();
		salarieString += "\n->Japec = " + salarie.getJapec();
		salarieString += "\n->Jded = " + salarie.getJded();
		salarieString += "\n->Jrla = " + salarie.getJrla();
		salarieString += "\n->Jrlec = " + salarie.getJrlec();
		salarieString += "\n->Dapa = " + salarie.getDapa();
		salarieString += "\n->Dapec = " + salarie.getDapec();
		String texte = printOut + salarieString;
		update.printOutSalarie(salarie.getNmat(), texte);
	}

	/*
	 * public void agentUpdateToString() { ParameterUtil.println("------------------------------" + salarie.getNmat() + " " + salarie.getNom() + " " +
	 * salarie.getPren() + "-------------------------------------------------------------------------------");
	 * ParameterUtil.println(StringUtil.toString(salarie)); ParameterUtil.println("boolean tab91 = " + tab91); ParameterUtil.println("double bas_con = " +
	 * bas_con); ParameterUtil.println("double moncp = " + moncp); ParameterUtil.println("int nbjcp = " + nbjcp); ParameterUtil.println("double conge = " + conge);
	 * ParameterUtil.println("int nbjtr = " + nbjtr); ParameterUtil.println("boolean cgan_ms = " + cgan_ms); ParameterUtil.println("boolean exist_evcg = " +
	 * exist_evcg); ParameterUtil.println("int nbjreliq = " + nbjreliq); ParameterUtil.println("boolean mois_conge = " + mois_conge);
	 * ParameterUtil.println("boolean radie = " + radie); ParameterUtil.println("boolean radie_depuis = " + radie_depuis); ParameterUtil.println("boolean continuer = " +
	 * continuer); ParameterUtil.println("boolean retour = " + retour); ParameterUtil.println("String am = " + am); ParameterUtil.println("boolean conges_annuels = " +
	 * conges_annuels); ParameterUtil.println("boolean finconge = " + finconge); ParameterUtil.println("boolean pnp = " + pnp); ParameterUtil.println("int nbj_a = " +
	 * nbj_a); ParameterUtil.println("int nbj_c = " + nbj_c); ParameterUtil.println("boolean ca = " + ca); ParameterUtil.println("int nbj_con = " + nbj_con);
	 * ParameterUtil.println("String sav_pmcf = " + sav_pmcf); ParameterUtil.println("Date sav_dfcf = " + sav_dfcf); ParameterUtil.println("Date sav_ddcf = " +
	 * sav_ddcf); ParameterUtil.println("Date date_anniv = " + date_anniv); ParameterUtil.println("int nbj_enf = " + nbj_enf); ParameterUtil.println("int nbj_anc = " +
	 * nbj_anc); ParameterUtil.println("int nbj_deco = " + nbj_deco); ParameterUtil.println("int ancien = " + ancien); }
	 */

	/** ************************* LISTE DES GETTERS POUR AVOIR LES VALEURS DES DIFFERENTES PROPRIETES DE L'OBJECT ******************** */
	
	public void truncateMontants()
	{
		// ----------Zones nulles servant dans les conges------------------
		salarie.setJapa(ClsStringUtil.truncateToXDecimal(salarie.getJapa(), 2));
		salarie.setJapec(ClsStringUtil.truncateToXDecimal(salarie.getJapec(), 2));
		salarie.setJded(ClsStringUtil.truncateToXDecimal(salarie.getJded(), 2));
		salarie.setJrla(ClsStringUtil.truncateToXDecimal(salarie.getJrla(), 2));
		salarie.setJrlec(ClsStringUtil.truncateToXDecimal(salarie.getJrlec(), 2));
		salarie.setNbjse(ClsStringUtil.truncateToXDecimal(salarie.getNbjse(), 2));
		salarie.setNbjsa(ClsStringUtil.truncateToXDecimal(salarie.getNbjsa(), 2));
		salarie.setNbjsm(ClsStringUtil.truncateToXDecimal(salarie.getNbjsm(), 2));
		salarie.setNbjtr(ClsStringUtil.truncateToXDecimal(salarie.getNbjtr(), 2));
	}

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
