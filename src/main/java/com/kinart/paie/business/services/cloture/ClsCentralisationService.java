package com.kinart.paie.business.services.cloture;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.kinart.paie.business.model.*;
import com.kinart.paie.business.services.utils.ClsResultat;
import com.kinart.paie.business.services.utils.ClsTreater;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.kinart.paie.business.services.utils.ParameterUtil;
import com.kinart.stock.business.model.Utilisateur;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.springframework.dao.DataAccessException;

/**
 * @author Administrateur
 */
public class ClsCentralisationService
{

	ClsCentralisationDao dao = new ClsCentralisationDao();
	
	Map<String, ElementSalaire> rubriquesMap = new HashMap<String, ElementSalaire>();
	Map<String, ParamData> rhfnomsMap = new HashMap<String, ParamData>();
	
	boolean nouvelleCompta = false;
	RechercheCompteRubriqueVO nouvelleComptabilisation;

	GeneriqueConnexionService service;
	
	public void init(HttpServletRequest request, GeneriqueConnexionService service, String cdos)
	{
		this.service = service;
		ParamData fnom =null;
		String queryString = "From ParamData where idEntreprise='"+cdos+"' and ctab=99 and cacc='NEWCPTRUB' and nume=1";
		List result = service.find(queryString);
		if(result.isEmpty()) fnom = null;
		else fnom = (ParamData)result.get(0);

		//ParamData fnom = (Rhfnom)service.get(Rhfnom.class, new RhfnomPK(cdos,99,"NEWCPTRUB",1));
		nouvelleCompta = (fnom != null && StringUtils.isNotBlank(fnom.getVall()) && StringUtils.equals("O", fnom.getVall()));
		if(nouvelleCompta)
		{
			nouvelleComptabilisation = new RechercheCompteRubriqueVO();
			nouvelleComptabilisation.init(request, service, cdos);
		}	
	}

	public String getLangue(ClsOutPrecentralisation centralisateur)
	{
		List result = service.find("From Utilisateur Where identreprise="+centralisateur.cdos+" And email='"+centralisateur.cuti+"'");
		if(result.isEmpty()) return null;
		return ((Utilisateur)result.get(0)).getClang();

	}

	public String getCompFromRhpdos(String cdos)
	{
		List result = service.find("From DossierPaie Where idEntreprise="+cdos);
		if(result.isEmpty()) return null;
		return ((DossierPaie)result.get(0)).getComp();
		//return this.dao._getCompFromRhpdos(cdos);
	}

	public String[] centra_dossier(String cdos)
	{
		String[] result = new String[] { "N", "" };
		ParamData nome = this.chercherEnNomenclature(cdos, 99, "DOS-CENTRA", 1);
		result[0] = nome.getVall();

		if ("O".equalsIgnoreCase(nome.getVall()))
		{
			nome = this.chercherEnNomenclature(cdos, 99, "DOS-CENTRA", 2);
			result[1] = nome.getVall();
		}
		return result;
	}

	public String genereNumpieceSimpe(String aamm, String nbul)
	{
		return StringUtils.substring(aamm, 4, 6) + StringUtils.substring(aamm, 0, 4) + nbul;
	}

	public String genereLibelleEcriture(String aamm, String nbul)
	{
		return "Paie:Bul. " + nbul + " de " + StringUtils.substring(aamm, 4, 6) + " " + StringUtils.substring(aamm, 0, 4);
	}

	public String chercherRubriqueNAP(String cdos)
	{
		ParamData nome = this.chercherEnNomenclature(cdos, 99, "RUBNAP", 1);
		if (nome.getValm() != null)
			return StringUtils.leftPad(String.valueOf(nome.getValm()), 4, '0');
		return null;
	}

	public String chercherRubriqueBrut(String cdos)
	{
		ParamData nome = this.chercherEnNomenclature(cdos, 99, "RUBBRUT", 1);
		if (nome.getValm() != null)
			return StringUtils.leftPad(String.valueOf(nome.getValm()), 4, '0');
		return null;
	}

	public String chercherCodeJournal(String cdos)
	{
		ParamData nome = this.chercherEnNomenclature(cdos, 99, "JOURPAIE", 1);
		return StringUtils.substring(nome.getVall(), 0, 5);
	}

	public String chercherCodeEtablissementCommun(String cdos)
	{
		ParamData nome = this.chercherEnNomenclature(cdos, 99, "JOURPAIE", 2);
		return StringUtils.substring(nome.getVall(), 0, 3);
	}

	public String chercherDest2Commune(String cdos)
	{
		ParamData nome = this.chercherEnNomenclature(cdos, 99, "JOURPAIE", 3);
		return StringUtils.substring(nome.getVall(), 0, 8);
	}

	public String chercherLibelleAbrege(String cdos)
	{
		ParamData nome = this.chercherEnNomenclature(cdos, 99, "JOURPAIE", 4);
		return StringUtils.substring(nome.getVall(), 0, 2);
	}

	public ParamData chercherInterfaceGL(String cdos)
	{
		ParamData nome = this.chercherEnNomenclature(cdos, 99, "INTER-GL", 1);
		if (nome == null)
			return new ParamData();
		return nome;
	}
	
	public String chercherLibelleJournalFromCp_Jou(String cdos, String codeJournal)
	{
		return this.dao._chercherLibelleJournalFromCp_Jou(service, cdos, codeJournal);
	}
	
	public String chercherLibelleEtablissementFromCpEt(String cdos, String codeEtablissement)
	{
		return this.dao._chercherLibelleEtablissementFromCpEt(service, cdos, codeEtablissement);
	}
	
	public ClsOutPrecentralisation verifieValiditeSection(ClsOutPrecentralisation centralisateur,String cdos, String codedestination, Integer numaxe, Date datec)
	{
		centralisateur.verifok = true;
		
		CpDevise cpde = getInformationSectionFromCpDe(cdos, codedestination, numaxe);
		if(cpde == null)
		{
			centralisateur.message = ClsTreater._getResultat("Destination 2 standard %1 inexistante en comptabilite.", "ERR-90093", true);
			this.insertIntoMemoryLog(centralisateur, true);
			centralisateur.verifok = false;
			return centralisateur;
		}
		
		if("I".equalsIgnoreCase(cpde.getEtat()))
		{
			centralisateur.message = ClsTreater._getResultat("Destination 2 standard %1 inactive.", "ERR-90094", true);
			this.insertIntoMemoryLog(centralisateur, true);
			centralisateur.verifok = false;
			return centralisateur;
		}
		
		if(datec.compareTo(cpde.getFinsai()) > 0)
		{
			centralisateur.message = ClsTreater._getResultat("Destination 2 standard %1 ferm�e.", "ERR-90095", true);
			this.insertIntoMemoryLog(centralisateur, true);
			centralisateur.verifok = false;
			return centralisateur;
		}
		
		if(datec.compareTo(cpde.getDebsai()) < 0)
		{
			centralisateur.message = ClsTreater._getResultat("Destination 2 standard %1 pas encore ouverte.", "ERR-90096", true);
			this.insertIntoMemoryLog(centralisateur, true);
			centralisateur.verifok = false;
			return centralisateur;
		}
		
		return centralisateur;
	}
	
	public CpDevise getInformationSectionFromCpDe(String cdos, String codedestination, Integer numaxe)
	{
		return this.dao._getInformationSectionFromCpDe(service, cdos, codedestination, numaxe);
	}
	
	
	public String chercherLibelleAbbregeFromCpAbr(String cdos, String libelleAbrege)
	{
		return this.dao._chercherLibelleAbbregeFromCpAbr(service, cdos, libelleAbrege);
	}
	
	public String getDeviseDossierFromCpdos(String cdos)
	{
		return this.dao._getDeviseDossierFromCpdos(service, cdos);
	}
	
	public DossierPaie getCpdos(String cdos)
	{
		return this.dao._getCpdos(service, cdos);
	}
	
	public Integer getNbreBulFromRhtcalcul(String cdos, String aamm, String nbul, String rubNAP)
	{
		return this.dao._getNbreBulFromRhtcalcul(service, cdos, aamm, nbul, rubNAP);
	}
	
	public Integer getNbreBulNonEditeFromRhtcalcul(String cdos, String aamm, String nbul, String rubNAP)
	{
		return this.dao._getNbreBulNonEditeFromRhtcalcul(service, cdos, aamm, nbul, rubNAP);
	}
	
	public Integer getNbreRubComptaFromRhprubrique(String cdos)
	{
		return this.dao._getNbreRubComptaFromRhprubrique(service, cdos);
	}
	
	public List<Salarie>  getListeSalarieNationaliteInexistante(String cdos, String aamm, String nbul, String rubNAP)
	{
		return this.dao._getListeSalarieNationaliteInexistante(service, cdos, aamm, nbul, rubNAP);
	}
	
	public List<Salarie> getListeSalarieGradeInexistant(String cdos, String aamm, String nbul, String rubNAP)
	{
		return this.dao._getListeSalarieGradeInexistant(service, cdos, aamm, nbul, rubNAP);
	}
	
	public void viderTableCPINT(String cdos)
	{
		this.dao._viderTableCPINT(service, cdos);
	}
	
	public void viderTableCPINTWithPiece(String cdos, String numpiece)
	{
		this.dao._viderTableCPINTWithPiece(service, cdos, numpiece);
	}
	
	public void viderTableCPINT(Session session ,String cdos)
	{
		this.dao._viderTableCPINT(session ,cdos);
	}
	
	public void viderTableCPINTWithPiece(Session session ,String cdos, String numpiece)
	{
		this.dao._viderTableCPINTWithPiece(session , cdos, numpiece);
	}
	
	public void updateLibErrTableCPINT(Session session ,String cdos)
	{
		this.dao._updateLibErrTableCPINT(session , cdos);
	}
	
	public List<Salarie> getListeSalarieFromRhpagent(String cdos, String aamm, Integer nbul)
	{
		return this.dao._getListeSalarieFromRhpagent(service, cdos, aamm, nbul);
	}
	
	public List<CalculPaie> getListeBulletinCalculFromRhtcalcul(String cdos, String aamm, String nbul, String nmat)
	{
		return this.dao._getListeBulletinCalculFromRhtcalcul(service, cdos, aamm, nbul, nmat);
	}
	
	public ElementSalaire getInfoRubriqueFromRhprubrique(String cdos, String crub)
	{
		ElementSalaire rubrique = this.rubriquesMap.get(crub);
		if(rubrique == null) 
			rubrique = this.dao._getInfoRubriqueFromRhprubrique(service, cdos, crub);
		this.rubriquesMap.put(crub, rubrique);
		return rubrique;
	}
	
	public String getCodproFromCp_Cpt(String cdos, String numcompte)
	{
		return this.dao._getCodproFromCp_Cpt(service, cdos, numcompte);
	}
	
	public String getNumcptFromCpAux(String cdos, String numcompte)
	{
		return this.dao._getNumcptFromCpAux(service, cdos, numcompte);
	}
	
	public Integer getNbreCpAuxFromCpAux(String cdos, String numcompte, String numtiers)
	{
		return this.dao._getNbreCpAuxFromCpAux(service, cdos, numcompte, numtiers);
	}
	public Produit getCpProFromCpPro(String cdos, String codeProfil)
	{
		return this.dao._getCpProFromCpPro(service, cdos, codeProfil);
	}
	
	public String getTypcFromCp_Cpt(String cdos, String numcompte)
	{
		return this.dao._getTypcFromCp_Cpt(service, cdos, numcompte);
	}
	
	public Compte getCp_Cpt(String cdos, String numcompte)
	{
		return this.dao._getCp_Cpt(service, cdos, numcompte);
	}
	
	public void insereDansCPINT(Session session, InterfComptable cpint)
	{
		 session.save(cpint);
	}
	
	public String getNcptFromRhtpretEnt(String cdos, String nprt)
	{
		return this.dao._getNcptFromRhtpretEnt(service, cdos, nprt);
	}
	
	public String getLibetsFromCpEts(String cdos, String codeetablissement)
	{
		return this.dao._getLibetsFromCpEts(service, cdos, codeetablissement);
	}
	
	public String chercherZoneLibreAgent(String cdos,String nmat, Integer numZl)
	{
		FreeZone zone = null;
		FreeZoneParam pzone = null;
		String query = "From FreeZone where idEntreprise='"+cdos+"' and nmat='"+nmat+"' and numerozl="+numZl.intValue();
		List<FreeZone> fzl = service.find(query);
		if (!fzl.isEmpty())
			zone = fzl.get(0);
		query = "From FreeZoneParam where idEntreprise='"+cdos+"' and numerozl="+numZl.intValue();
		List<FreeZoneParam> fzlp = service.find(query);
		if (!fzlp.isEmpty())
			pzone = fzlp.get(0);
		if(zone != null && pzone != null)
		{
			if(StringUtils.equalsIgnoreCase("L",pzone.getTypezl())) return zone.getVallzl();
			if(StringUtils.equalsIgnoreCase("M",pzone.getTypezl())) return zone.getValmzl()==null? null : zone.getValmzl().toString();
			if(StringUtils.equalsIgnoreCase("T",pzone.getTypezl())) return zone.getValtzl() == null ? null  :zone.getValtzl().toString();
			if(StringUtils.equalsIgnoreCase("D",pzone.getTypezl())) return zone.getValdzl() == null ? null : new SimpleDateFormat("dd-MM-yyyy").format(zone.getValdzl());
		}
		return null;
	}

	
	private ClsLog _createNewRhtLogEtendu(ClsOutPrecentralisation centralisateur, boolean isBlockingErrorOrIsEndBlock)
	{
		return new ClsLog(centralisateur, isBlockingErrorOrIsEndBlock);
	}

	public boolean insertIntoMemoryLog(ClsOutPrecentralisation centralisateur)
	{
		ParameterUtil.println("Message courant = "+centralisateur.message.getLibelle());
//		if (centralisateur.request.getSession().getAttribute(ClsSessionObjectName.SESSION_O_PRECENTRALISATION_LISTE) == null)
//			return false;
		if ("O".equalsIgnoreCase(centralisateur.uniquementsierreur))
		{
			if (centralisateur.message != null && centralisateur.message.isErrormessage())
			{
				centralisateur.log.add(this._createNewRhtLogEtendu(centralisateur,  false));
				//centralisateur.request.getSession().setAttribute(ClsSessionObjectName.SESSION_O_PRECENTRALISATION_LISTE, centralisateur.log);
			}
		}
		else
		{
			centralisateur.log.add(this._createNewRhtLogEtendu(centralisateur, false));
			//centralisateur.request.getSession().setAttribute(ClsSessionObjectName.SESSION_O_PRECENTRALISATION_LISTE, centralisateur.log);
		}
		
		this._setEvolutionTraitement(centralisateur);
		return true;
	}

	public boolean insertIntoMemoryLog(ClsOutPrecentralisation centralisateur,boolean isBlockingErrorOrIsEndBlock)
	{
		ParameterUtil.println("Message courant = "+centralisateur.message.getLibelle());
//		if (centralisateur.request.getSession().getAttribute(ClsSessionObjectName.SESSION_O_PRECENTRALISATION_LISTE) == null)
//			return false;
		
//		if (isBlockingErrorOrIsEndBlock && centralisateur.message != null && centralisateur.message.isErrormessage())
//			centralisateur.request.getSession().setAttribute(ClsSessionObjectName.SESSION_O_PRECENTRALISATION_BLOCKING_MESSAGE, centralisateur.message);
		ParameterUtil.println("Continue ...");
		if ("O".equalsIgnoreCase(centralisateur.uniquementsierreur))
		{
			if (centralisateur.message != null && centralisateur.message.isErrormessage())
			{
				centralisateur.log.add(this._createNewRhtLogEtendu(centralisateur, isBlockingErrorOrIsEndBlock));
				//centralisateur.request.getSession().setAttribute(ClsSessionObjectName.SESSION_O_PRECENTRALISATION_LISTE, centralisateur.log);
			}
		}
		else
		{
			centralisateur.log.add(this._createNewRhtLogEtendu(centralisateur, isBlockingErrorOrIsEndBlock));
			//centralisateur.request.getSession().setAttribute(ClsSessionObjectName.SESSION_O_PRECENTRALISATION_LISTE, centralisateur.log);
		}
		ParameterUtil.println("Setting evolution message ...");
		this._setEvolutionTraitement(centralisateur);
		return true;
	}
	
	public void _setEvolutionTraitement(ClsOutPrecentralisation centralisateur)
	{
		ClsResultat oResult = null;// ClsTreater._getResultat(centralisateur.request, centralisateur.message.getLibelle(), centralisateur.message.getLibelle(), centralisateur.message.isErrormessage());
		for (int i = 0; i < centralisateur.log.size(); i++)
		{
			oResult = ClsTreater._concat(oResult, centralisateur.log.get(i).getResultat(), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);	
		}
		 
		//centralisateur.request.getSession().setAttribute(ClsSessionObjectName.SESSION_O_CENTRALISATION_MESSAGE_SUIVI, oResult);
	}
	
	public void _setEvolutionTraitement(HttpServletRequest request, String message, boolean isError, int nbrCourant, int nbrTotal)
	{
		ClsResultat oResult = ClsTreater._getResultat(message, message, isError);
		//request.getSession().setAttribute(ClsSessionObjectName.SESSION_O_CENTRALISATION_MESSAGE_SUIVI, oResult);

//		request.getSession().setAttribute(ClsSessionObjectName.SESSION_O_CENTRALISATION_SALARIES_TRAITES_SUIVI,
//				new ClsNomenclature(String.valueOf(nbrCourant), String.valueOf(nbrTotal)));

	}
	
	public void _setTitreEvolutionTraitement(HttpServletRequest request, String message)
	{
		ClsResultat oResult = ClsTreater._getResultat(message, message, false);
		//request.getSession().setAttribute(ClsSessionObjectName.SESSION_O_CENTRALISATION_TITRE_SUIVI, oResult);

	}

	public ParamData chercherEnNomenclature(String cdos, String ctab, String cacc, Integer nume)
	{
		return this.dao._chercherEnNomenclature(service, cdos, ctab, cacc, nume);
	}

	public ParamData chercherEnNomenclature(String cdos, Integer ctab, String cacc, Integer nume)
	{
		///ParameterUtil.println("---A rechercher en nomenclature = "+cdos+" - "+ctab+" - "+cacc+" - "+nume);
//		if("RUBBRUT".equalsIgnoreCase(cacc))
//			System.out.println("---A rechercher en nomenclature = "+cdos+" - "+ctab+" - "+cacc+" - "+nume);
		String cle = cdos+"-"+ctab+"-"+cacc+"-"+nume;
		ParamData fnom = this.rhfnomsMap.get(cle);
		if(fnom == null)
		{
//			if("RUBBRUT".equalsIgnoreCase(cacc))
//				System.out.println("PARAM ABSENT DANS TABLEAU");
			fnom = this.dao._chercherEnNomenclature(service, cdos, ctab, cacc, nume);
			if(fnom != null) this.rhfnomsMap.put(cle, fnom);
		}
		
		return fnom;
	}
	
	public List<ParamData> chercherEnNomenclature(String queryString)
	{
		return this.service.find(queryString);
	}
	
	public String chercherMessage(HttpServletRequest request,String errorCode, String langue)
	{
		return this.chercherMessage(request, errorCode, langue, null);
	}
	
	public String chercherMessage(HttpServletRequest request,String errorCode, String langue, String ... param){
		
		ClsResultat resultat = ClsTreater._getResultat(null, errorCode, false, param);
		
		String libelleMessage = StringUtils.isNotEmpty(resultat.getLibelle())?resultat.getLibelle():errorCode;
		
		//com.cdi.deltarh.service.ParameterUtil.println("ERROR: (" + errorCode + ")" + libelleMessage);
		
		try 
		{
//			LogMessage log = new LogMessage();
//			log.setIdEntreprise(ParameterUtil.getSessionObject(request, ParameterUtil.SESSION_DOSSIER));
//			log.setCuti(ParameterUtil.getSessionObject(request, ParameterUtil.SESSION_LOGIN));
//			log.setDatc(new Date());
//			log.setLigne(libelleMessage);
//			//
//			this.service.save(log);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return libelleMessage;
	}
	public String getCompteRubriqueIndDeb(Salarie agent,ElementSalaire rubrique, int index_debit)
	{
		if(nouvelleCompta)
		{
			return nouvelleComptabilisation.getCompte(rubrique.getCrub(), "D", agent);
		}
		return getCompteRubriqueIndDeb( rubrique,index_debit);
		
	}
	
	public String getCompteRubriqueIndDeb(ElementSalaire rubrique, int index_debit)
	{
		if (index_debit == 1)
			return rubrique.getDe01();
		if (index_debit == 2)
			return rubrique.getDe02();
		if (index_debit == 3)
			return rubrique.getDe03();
		if (index_debit == 4)
			return rubrique.getDe04();
		if (index_debit == 5)
			return rubrique.getDe05();
		if (index_debit == 6)
			return rubrique.getDe06();
		if (index_debit == 7)
			return rubrique.getDe07();
		if (index_debit == 8)
			return rubrique.getDe08();
		if (index_debit == 9)
			return rubrique.getDe09();
		if (index_debit == 10)
			return rubrique.getDe10();
		if (index_debit == 11)
			return rubrique.getDe11();
		if (index_debit == 12)
			return rubrique.getDe12();
		if (index_debit == 13)
			return rubrique.getDe13();
		if (index_debit == 14)
			return rubrique.getDe14();
		if (index_debit == 15)
			return rubrique.getDe15();
		if (index_debit == 16)
			return rubrique.getDe16();
		if (index_debit == 17)
			return rubrique.getDe17();
		if (index_debit == 18)
			return rubrique.getDe18();
		if (index_debit == 19)
			return rubrique.getDe19();
		if (index_debit == 20)
			return rubrique.getDe20();
		return null;
	}
	
	public String getCompteRubriqueIndCre(Salarie agent,ElementSalaire rubrique, int index_credit)
	{
		if(nouvelleCompta)
		{
			return nouvelleComptabilisation.getCompte(rubrique.getCrub(), "C", agent);
		}
		return  getCompteRubriqueIndCre(rubrique, index_credit);
	}
	
	public String getCompteRubriqueIndCre(ElementSalaire rubrique, int index_credit)
	{
		if (index_credit == 1)
			return rubrique.getCr01();
		if (index_credit == 2)
			return rubrique.getCr02();
		if (index_credit == 3)
			return rubrique.getCr03();
		if (index_credit == 4)
			return rubrique.getCr04();
		if (index_credit == 5)
			return rubrique.getCr05();
		return null;
	}
	
	public void save(Object entity) throws DataAccessException
	{
		this.service.save(entity);
	}
	
	
	public Session getSession()
	{
		return this.service.getSession();
	}
	
	public void closeConnexion ( Session session ) 
	{
		this.service.closeSession(session);
	}

	public ClsCentralisationDao getDao()
	{
		return this.dao;
	}

	public void setDao(ClsCentralisationDao dao)
	{
		this.dao = dao;
	}

	public Map<String, ElementSalaire> getRubriquesMap()
	{
		return rubriquesMap;
	}

	public void setRubriquesMap(Map<String, ElementSalaire> rubriquesMap)
	{
		this.rubriquesMap = rubriquesMap;
	}

	public Map<String, ParamData> getRhfnomsMap()
	{
		return rhfnomsMap;
	}

	public void setRhfnomsMap(Map<String, ParamData> rhfnomsMap)
	{
		this.rhfnomsMap = rhfnomsMap;
	}
	
	
}
