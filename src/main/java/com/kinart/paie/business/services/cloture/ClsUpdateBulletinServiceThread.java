package com.kinart.paie.business.services.cloture;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.kinart.paie.business.model.*;
import com.kinart.paie.business.services.utils.*;
import org.hibernate.Session;
import org.springframework.dao.DataAccessException;

/**
 * @author Administrateur
 */
public class ClsUpdateBulletinServiceThread
{

	ClsUpdateBulletinDao dao;

	ClsUpdateBulletinThread update;
	
	public DossierPaie getRhpdos()
	{
		return this.dao._getRhpdos(update.service, update.dossier);
	}
	
	public String getLangue()
	{
		String clang = this.dao._getLangueUtilisateur(update.service, update.dossier, update.user);
//		if (StringUtils.isBlank(clang))
//			clang = ClsParameter.getSessionObject(update.request, ClsParameter.SESSION_LANGUE);
		return clang;
	}
	
	public String getProchainMois()
	{
		int annee = NumberUtils.toInt(StringUtil.substring(update.periode, 0, 4)); // myPeriode.getMonth();
		int mois = NumberUtils.toInt(StringUtil.substring(update.periode, 4, 6)) + 1;
		if (mois > 12)
		{
			mois = 1;
			annee++;
		}
		return ClsStringUtil.formatNumber(annee, "0000") + ClsStringUtil.formatNumber(mois, "00");
	}

	/**
	 * chargement des valeurs T99 pour ne pas aller chercher dans la bd lors de l'initialisation des param�tres de calcul
	 * 
	 */
	public void getDonneesNomenclature91_99Map()
	{

		update.listeDonneesParametrage = new HashMap<String, ParamData>();

		List<ParamData> l = this.dao._getDonneesNomenclature91_99(update.service, update.dossier, update.mois_ms, update.prochainmois);
		ParamData o1 = null;
		String key = "";
		for (Object object : l)
		{
			o1 = (ParamData) object;
			key = o1.getCtab() + o1.getCacc() + o1.getNume();
			update.listeDonneesParametrage.put(key.trim(), o1);
		}
	}

	public Integer getNbreBulFromRhtcalcul(String cdos, String aamm, Integer nbul, String rubNAP)
	{
		return this.dao._getNbreBulFromRhtcalcul(update.service, cdos, aamm, nbul, rubNAP);
	}

	public Integer getNbreBulNonEditeFromRhtcalcul(String cdos, String aamm, Integer nbul, String rubNAP)
	{
		return this.dao._getNbreBulNonEditeFromRhtcalcul(update.service, cdos, aamm, nbul, rubNAP);
	}
	
	public List<Object[]> chargerT_EV_MC(String cdos)
	{
		return this.dao._chargerT_EV_MC(update.service, cdos);
	}
	
	public List<Object[]> chargerT_EV_MS(String cdos)
	{
		return this.dao._chargerT_EV_MS(update.service, cdos);
	}
	
	public List<Object[]> getEnfantsJoursSup(String cdos)
	{
		return this.dao._getEnfantsJoursSup(update.service, cdos);
	}
	
	public Integer countNbColonnesT91(String cdos)
	{
		return this.dao._countNbColonnesT91(update.service, cdos);
	}
	
	public List<Object[]> getPeriodesT91(String cdos, String cacc)
	{
		return this.dao._getPeriodesT91(update.service, cdos, cacc);
	}
	
	public List<ElementSalaire> getRubriquesRegularisation(String cdos)
	{
		return this.dao._getRubriquesRegularisation(update.service, cdos);
	}
	
	public List<Salarie> getListeSalarieFromRhpagent(String cdos,String dateformat)
	{
		return this.dao._getListeSalarieFromRhpagent(update.service, cdos,dateformat);
	}

	public List<CalculPaie> getListeBulletinCalculFromRhtcalcul(String cdos, String aamm, Integer nbul, String nmat)
	{
		return this.dao._getListeBulletinCalculFromRhtcalcul(update.service, cdos, aamm, nbul, nmat);
	}

	public ElementSalaire getInfoRubriqueFromRhprubrique(String cdos, String crub)
	{
		ElementSalaire rubrique = update.rubriquesMap.get(crub);
		if (rubrique == null)
			rubrique = this.dao._getInfoRubriqueFromRhprubrique(update.service, cdos, crub);
		update.rubriquesMap.put(crub, rubrique);
		return rubrique;
	}
	
	protected Object[] getRhfnomMotifConges(String cdos, String cacc)
	{
		return this.dao._getRhfnomMotifConges(update.service, cdos, cacc);
	}
	public List<Object[]> chargerEVMoisCourant(String cdos, String aamm, String nmat, Integer nbul)
	{	
		return this.dao._chargerEVMoisCourant(update.service, cdos, aamm, nmat, nbul);
	}

	public ParamData chercherEnNomenclature(String cdos, String ctab, String cacc, Integer nume)
	{
		return this.dao._chercherEnNomenclature(update.service, cdos, ctab, cacc, nume);
	}
	
	public List<CumulPaie> getCumulsToDelete(String nmat)
	{
		return this.dao._getCumulsToDelete(update.service, update.dossier, update.periode, update.numerobulletin, nmat);
	}

	public List<CumulPaie> getCumulsToDelete99(String nmat)
	{
		return this.dao._getCumulsToDelete99(update.service, update.dossier, update.periode,update.am99, update.numerobulletin, nmat);
	}
	
	public void deleteCumuls(Session session,String nmat)
	{
		this.dao._deleteCumulsWithoutCommit(session, update.dossier, update.periode, update.numerobulletin, nmat);
	}

	public void deleteCumuls99(Session session,String nmat)
	{
		this.dao._deleteCumuls99WithoutCommit(session,update.dossier, update.periode,update.am99, update.numerobulletin, nmat);
	}
	
	public boolean supp_fictif(Session session, String nmat)
	{
		try
		{
			this.dao._supp_fictif(session, update.dossier, nmat, update.numerobulletin);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public Integer countNbJourReliquat(String cdos,String dateDebut,String dateFin)
	{
		return this.dao._countNbJourReliquat(update.service, cdos, dateDebut, dateFin);
	}
	
	public Integer updatePrets17(Session session, String cdos, String nmat)
	{
		try
		{
			return this.dao._updatePrets17(session, cdos, nmat);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	
	protected Object[] getRhfnomCalcPartFiscal(String cdos, String cacc)
	{
		return this.dao._getRhfnomCalcPartFiscal(update.service, cdos, cacc);
	}
	
	public Integer getNbEnfantsSalarie(String cdos, String nmat, String dtna)
	{
		return this.dao._getNbEnfantsSalarie(update.service, cdos, nmat, dtna);
	}
	
	public double getNbJrsDecoration(String cdos, String nmat)
	{
		return this.dao._getNbJrsDecoration(update.service, cdos, nmat);
	}
	
	public List getDtnaEnfantsSalarie(String cdos,String nmat)
	{
		return this.dao._getDtnaEnfantsSalarie(update.service, cdos, nmat);
	}
	
	public Object[] getRhfnomNbParts(String cdos, String cacc)
	{
		return this.dao._getRhfnomNbParts(update.service, cdos, cacc);
	}
	
	public Integer computeNbjrAbsences(String cdos, String jourdebut,String jourfin)
	{
		return this.dao._computeNbjrAbsences(update.service, cdos, jourdebut, jourfin);
	}
	
	public Integer computeNbjrConges(String cdos, String jourdebut,String jourfin)
	{
		return this.dao._computeNbjrConges(update.service, cdos, jourdebut, jourfin);
	}
	
	public void updateDossier(Session oSession, String cdos, String aamm, Integer nbul)
	{
		this.dao._updateDossier(oSession, cdos, aamm, nbul);
	}
	
	public List<PretExterneEntete> getPretsToTransfert(String cdos, String nmat, String perb1)
	{
		return this.dao._getPretsToTransfert(update.service, cdos, nmat, perb1);
	}
	
	public Object getMaxPerbFromPret(String cdos, String nprt)
	{
		return this.dao._getMaxPerbFromPret(update.service, cdos, nprt);
	}
	
	public boolean transferEVMoisSuivant(Session oSession,String cdos,String aamm,Integer nbul,String mois_ms, Integer nbul_ms,String nmat, String date_debut_ms, String date_fin_ms, String cuti)
	{
		return this.dao._transferEVMoisSuivant(oSession, cdos, aamm, nbul, mois_ms, nbul_ms, nmat, date_debut_ms, date_fin_ms, cuti);
	}
	
	public List<Object[]> chargerEVEnteteEtDetail(String cdos, String aamm, String nmat, Integer nbul)
	{
		return this.dao._chargerEVEnteteEtDetail(update.service, cdos, aamm, nmat, nbul);
	}

	public ParamData chercherEnNomenclature(String cdos, Integer ctab, String cacc, Integer nume)
	{
		///ClsParameter.println("---A rechercher en nomenclature = "+cdos+" - "+ctab+" - "+cacc+" - "+nume);
		String cle = cdos + "-" + ctab + "-" + cacc + "-" + nume;
		ParamData fnom = update.rhfnomsMap.get(cle);
		if (fnom == null)
		{
			fnom = this.dao._chercherEnNomenclature(update.service, cdos, ctab, cacc, nume);
			if (fnom != null)
				update.rhfnomsMap.put(cle, fnom);
		}

		return fnom;
	}

	public List<ParamData> chercherEnNomenclature(String queryString)
	{
		return update.service.find(queryString);
	}

	public String chercherMessage(HttpServletRequest request, String errorCode, String langue)
	{
		return this.errorMessage(errorCode);
	}
	public static Map<LogMessage, String> getStartMessage(HttpServletRequest request,String clang, String... arrayCodes)
	{
		Map<LogMessage, String> map = new HashMap<LogMessage, String>();
		String valeur = null;
		HashMap<LogMessage, String> msg = null;//ClsTreater.getMapMessages(request);
		if (msg != null)
		{
			for(String code : arrayCodes)
			{
			 valeur= msg.get(code);
			 //map.put(new LogMessage(code,clang), valeur);
			}
		}
		return map;
	}
	public String errorMessage(String errorCode, Object... param)
	{
		String libelleMessage = "";
		int n = 0;
		try
		{
			Map<LogMessage, String> libelles = new HashMap<LogMessage, String>();

			Object oMap = null;

			String clang = null;//ParameterUtil.getSessionObject(update.request, ClsParameter.SESSION_LANGUE);
			if(update.request != null) oMap = getStartMessage(update.request,clang, errorCode);// update.request.getSession().getAttribute(ClsParameter.SESSION_LIBELLES);

			if (oMap != null && errorCode != null)
			{

				libelles = (Map<LogMessage, String>) oMap;

				LogMessage pk = null;//new LogMessage(errorCode, clang);

				if (libelles.get(pk) != null)
					libelleMessage = libelles.get(pk);

				String match = "%";
				int j = 0;
				for (int i = 0; i < param.length; i++)
				{
					j = i + 1;
					match = "%" + j;
					Object params = param[i];
					if (ClsObjectUtil.isNull(params))
						params = " ";
					if (ClsObjectUtil.isNull(match))
						match = " ";
					if (!ClsObjectUtil.isNull(libelleMessage))
						libelleMessage = libelleMessage.replace(match, String.valueOf(params));
				}
			}
		}
		catch (DataAccessException e1)
		{
			e1.printStackTrace();
		}

		ParameterUtil.println("ERROR: (" + errorCode + ")" + libelleMessage);
		return libelleMessage;
	}
	
	public boolean deleteEVMois(Session oSession,String cdos, String aamm, Integer nbul, String nmat)
	{
		boolean result = true;
		try
		{
			result =  this.dao._deleteEVMois(oSession, cdos, aamm, nbul, nmat);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			result = false;
		}
		return result;
	}
	
	public boolean deleteEFMois(Session oSession,String cdos,String datefin,String nmat)
	{
		try
		{
			return this.dao._deleteEFMois(oSession, cdos, datefin, nmat);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public void save(Object entity) throws DataAccessException
	{
		update.service.save(entity);
	}

	public Session getSession()
	{
		return update.service.getSession();
	}

	public void closeConnexion(Session session)
	{
		update.service.closeSession(session);
	}

	public ClsUpdateBulletinDao getDao()
	{
		return dao;
	}

	public void setDao(ClsUpdateBulletinDao dao)
	{
		this.dao = dao;
	}

}
