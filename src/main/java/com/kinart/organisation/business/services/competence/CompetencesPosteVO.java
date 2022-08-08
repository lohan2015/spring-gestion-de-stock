package com.kinart.organisation.business.services.competence;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.kinart.organisation.business.model.Organigramme;
import com.kinart.organisation.business.model.Orgposte;
import com.kinart.organisation.business.model.Orgposteinfo;
import com.kinart.paie.business.model.ElementSalaire;
import com.kinart.paie.business.model.ParamData;
import com.kinart.paie.business.services.cloture.ClsNomenclature;
import com.kinart.paie.business.services.utils.ClsResultat;
import com.kinart.paie.business.services.utils.ClsTreater;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.kinart.paie.business.services.utils.ParameterUtil;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataAccessException;

public class CompetencesPosteVO extends AbsCompetencePosteVO
{
	public String codeEntite;

	public GeneriqueConnexionService service;

	public String dossier;

	public String langue;

	public HttpServletRequest request;

	public static final int ENREGISTRER_COMPETENCE = 1;

	public static final int SUPPRIMER_FORMATION = 2;

	public static final int SUPPRIMER_FONCTION = 3;

	public static final int SUPPRIMER_DIPLOME = 4;

	public static final int SUPPRIMER_DOCUMENT = 5;

	public static final int SUPPRIMER_LANGUE = 6;

	public static final int SUPPRIMER_THEME = 7;

	public static final int SUPPRIMER_TACHE = 8;

	public static final int SUPPRIMER_INTERIM = 9;

	public static final int SUPPRIMER_POSTE_CLIENT = 10;

	public static final int SUPPRIMER_AVANTAGE_NATURE = 11;

	public static final int SUPPRIMER_AVANTAGE_HORS_PAIE = 12;

	public static final int SUPPRIMER_RUBRIQUE = 13;

	public static final int SUPPRIMER_SAVOIR_ETRE = 14;

	public static final int SUPPRIMER_KEY_AREA = 15;

	public static final int SUPPRIMER_MISSION = 16;
	
	public static final int SUPPRIMER_SAVOIR = 17;
	
	public static final int SUPPRIMER_RELATIONS_INTERNES = 18;
	
	public static final int SUPPRIMER_RELATIONS_EXTERNES = 19;
	
	public static final int SUPPRIMER_PLAN_SUCCESSION = 20;
	
	public static final int SUPPRIMER_MOYENS = 21;

	ClsResultat headerNullValues = null;

	ClsResultat headerInvalidValues = null;

	ClsResultat headerformations = null;

	ClsResultat headerfonctions = null;

	ClsResultat headerdiplomes = null;

	ClsResultat headerdocuments = null;

	ClsResultat headerlangues = null;

	ClsResultat headerthemes = null;

	ClsResultat headertaches = null;

	ClsResultat headerinterims = null;

	ClsResultat headerposteclients = null;

	ClsResultat headeravantagenatures = null;

	ClsResultat headeravantagehorspaies = null;

	ClsResultat headerrubriques = null;

	ClsResultat headersavoiretres = null;

	ClsResultat headerkeyareas = null;

	ClsResultat headermissions = null;
	
	ClsResultat headersavoir = null;
	
	ClsResultat headerrelexternes = null;
	
	ClsResultat headerrelinternes = null;
	
	ClsResultat headerplansuccession = null;
	
	ClsResultat headermoyens = null;

	String sep = ParameterUtil.CHAINE_ALLER_A_LA_LIGNE + ParameterUtil.CHAINE_TIRET;

	String sep2 = ParameterUtil.CHAINE_ALLER_A_LA_LIGNE_DOUBLE + ParameterUtil.CHAINE_ESPACE + ParameterUtil.CHAINE_ESPACE;

	private int nivdcoisp = 3;

	private int nivsavetre = 2;

	public void clearListes()
	{
		this.oformations = getSortableList(new ArrayList<CompetencePosteVO>());

		this.ofonctions = getSortableList(new ArrayList<CompetencePosteVO>());

		this.odiplomes = getSortableList(new ArrayList<CompetencePosteVO>());

		this.odocuments = getSortableList(new ArrayList<CompetencePosteVO>());

		this.olangues = getSortableList(new ArrayList<CompetencePosteVO>());

		this.othemes = getSortableList(new ArrayList<CompetencePosteVO>());

		this.otaches = getSortableList(new ArrayList<CompetencePosteVO>());

		this.ointerims = getSortableList(new ArrayList<CompetencePosteVO>());

		this.oposteclients = getSortableList(new ArrayList<CompetencePosteVO>());

		this.oavantagenatures = getSortableList(new ArrayList<CompetencePosteVO>());

		this.oavantagehorspaies = getSortableList(new ArrayList<CompetencePosteVO>());

		this.orubriques = getSortableList(new ArrayList<CompetencePosteVO>());

		this.osavoiretres = getSortableList(new ArrayList<CompetencePosteVO>());

		this.okeyareas = getSortableList(new ArrayList<CompetencePosteVO>());

		this.omissions = getSortableList(new ArrayList<CompetencePosteVO>());
		
		this.omoyens = getSortableList(new ArrayList<CompetencePosteVO>());
	}

	public SortableListVO<CompetencePosteVO> getSortableList(List<CompetencePosteVO> liste)
	{
		SortableListVO<CompetencePosteVO> lst = new SortableListVO<CompetencePosteVO>(CompetencePosteVO.class, "codeinfo1", liste, CommonFunctions.Mode.CONSULTATION);
		lst.setPageRows(10);
		return lst;
	}

	public void loadListes()
	{
		ComputeCompetencePosteVO computer = new ComputeCompetencePosteVO(this);
		computer.computeAllCompetences();
	}

	public CompetencesPosteVO(HttpServletRequest request, GeneriqueConnexionService service, String codeEntite, String dossier, String langue)
	{
		this.codeEntite = codeEntite;

		this.service = service;

		this.dossier = dossier;

		this.langue = langue;

		this.request = request;

		this.loadListes();

		headerNullValues = ClsTreater._getResultat("Saisir la valeur de", "INF-80114", true);

		headerInvalidValues = ClsTreater._getResultat("Informations non valides", "ERR-80136", true);

		headerformations = ClsTreater._getResultat("Formations", "INF-80819", true);

		headerfonctions = ClsTreater._getResultat("Fonctions", "INF-81041", true);

		headerdiplomes = ClsTreater._getResultat("Diplémes", "INF-00931", true);

		headerdocuments = ClsTreater._getResultat("Documents", "INF-80471", true);

		headerlangues = ClsTreater._getResultat("Langues", "INF-51066", true);

		headerthemes = ClsTreater._getResultat("Savoirs Faire", "INF-80472", true);

		headertaches = ClsTreater._getResultat("Téches Suppl.", "INF-80473", true);

		headerinterims = ClsTreater._getResultat("Intérim Postes", "INF-80474", true);

		headerposteclients = ClsTreater._getResultat("Postes Clients", "INF-80477", true);

		headeravantagenatures = ClsTreater._getResultat("Avantages en Nature", "INF-80319", true);

		headeravantagehorspaies = ClsTreater._getResultat("Avantages Hors Paie", "INF-80478", true);

		headerrubriques = ClsTreater._getResultat("Rubriques de paie", "INF-80479", true);

		headersavoiretres = ClsTreater._getResultat("Savoirs Etre", "INF-80480", true);

		headerkeyareas = ClsTreater._getResultat("Domaines clés", "INF-80481", true);

		headermissions = ClsTreater._getResultat("Missions", "INF-80485", true);
		
		headersavoir = ClsTreater._getResultat("Savoir", "INF-0", true);
		
		headerrelinternes = ClsTreater._getResultat("Relations internes", "INF-0", true);
		
		headerrelexternes = ClsTreater._getResultat("Relations externes", "INF-0", true);
		
		headerplansuccession = ClsTreater._getResultat("Plan de succession", "INF-0", true);
		
		headermoyens = ClsTreater._getResultat("Moyens nécessaires", "INF-0", true);
	}

	public ClsResultat preSaving(int niveausauvegardedomainecle, int niveausauvegardesavoiretre)
	{
		this.nivdcoisp = niveausauvegardedomainecle;
		this.nivsavetre = niveausauvegardesavoiretre;

		ClsResultat resultat = this._checkValuesFormation();

		resultat = ClsTreater._concat(resultat, this._checkValuesFonction(), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE_DOUBLE);

		resultat = ClsTreater._concat(resultat, this._checkValuesDiplome(), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE_DOUBLE);

		resultat = ClsTreater._concat(resultat, this._checkValuesDocument(), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE_DOUBLE);

		resultat = ClsTreater._concat(resultat, this._checkValuesLangue(), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE_DOUBLE);

		resultat = ClsTreater._concat(resultat, this._checkValuesSavoirFaire(), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE_DOUBLE);

		resultat = ClsTreater._concat(resultat, this._checkValuesTache(), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE_DOUBLE);

		resultat = ClsTreater._concat(resultat, this._checkValuesInterimPoste(), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE_DOUBLE);

		resultat = ClsTreater._concat(resultat, this._checkValuesPosteClient(), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE_DOUBLE);

		resultat = ClsTreater._concat(resultat, this._checkValuesAvantageNature(), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE_DOUBLE);

		resultat = ClsTreater._concat(resultat, this._checkValuesAvantageHorsPaie(), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE_DOUBLE);

		resultat = ClsTreater._concat(resultat, this._checkValuesRubrique(), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE_DOUBLE);

		resultat = ClsTreater._concat(resultat, this._checkValuesSavoirEtre(), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE_DOUBLE);

		resultat = ClsTreater._concat(resultat, this._checkValuesDomaineCle(), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE_DOUBLE);

		resultat = ClsTreater._concat(resultat, this._checkValuesMission(), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE_DOUBLE);
		
		resultat = ClsTreater._concat(resultat, this._checkValuesSavoir(), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE_DOUBLE);
		
		resultat = ClsTreater._concat(resultat, this._checkValuesRelExternes(), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE_DOUBLE);
		
		resultat = ClsTreater._concat(resultat, this._checkValuesRelInternes(), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE_DOUBLE);
		
		resultat = ClsTreater._concat(resultat, this._checkValuesPlansuccession(), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE_DOUBLE);
		
		resultat = ClsTreater._concat(resultat, this._checkValuesMoyens(), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE_DOUBLE);

		return resultat;
	}

	public ClsResultat saveAll()
	{
		ClsResultat resultat = null;
		Session session = service.getSession();
		try
		{
			Transaction tx = session.beginTransaction();

			this.save(session, this.oformations);
			this.save(session, this.ofonctions);
			this.save(session, this.olangues);
			this.save(session, this.othemes);
			this.save(session, this.odiplomes);
			this.save(session, this.odocuments);
			this.save(session, this.otaches);
			this.save(session, this.ointerims);
			this.save(session, this.oposteclients);
			this.save(session, this.oavantagenatures);
			this.save(session, this.oavantagehorspaies);
			this.save(session, this.orubriques);
			this.save(session, this.osavoiretres);
			this.saveDC(session, this.okeyareas);
			this.save(session, this.omissions);
			this.save(session, this.osavoir);
			this.save(session, this.orelationsinternes);
			this.save(session, this.orelationsexternes);
			this.save(session, this.oplansuccession);
			this.save(session, this.omoyens);

			resultat = ClsTreater._getResultat("Sauvegarde éffectuée avec succés", "INF-80178", false);

			tx.commit();
		}
		catch (HibernateException e)
		{
			resultat = ClsTreater._getResultat(ClsTreater._getStackTrace(e), null, true);
		}
		catch (DataAccessException e)
		{
			resultat = ClsTreater._getResultat(ClsTreater._getStackTrace(e), null, true);
		}
		finally
		{
			service.closeSession(session);
		}

		return resultat;
	}

	public ClsResultat duplicateAll(Session session, String newCodeEntite)
	{
		ClsResultat resultat = null;
		try
		{
			this.duplicate(session, newCodeEntite, this.oformations);
			this.duplicate(session, newCodeEntite, this.ofonctions);
			this.duplicate(session, newCodeEntite, this.olangues);
			this.duplicate(session, newCodeEntite, this.othemes);
			this.duplicate(session, newCodeEntite, this.odiplomes);
			this.duplicate(session, newCodeEntite, this.odocuments);
			this.duplicate(session, newCodeEntite, this.otaches);
			this.duplicate(session, newCodeEntite, this.ointerims);
			this.duplicate(session, newCodeEntite, this.oposteclients);
			this.duplicate(session, newCodeEntite, this.oavantagenatures);
			this.duplicate(session, newCodeEntite, this.oavantagehorspaies);
			this.duplicate(session, newCodeEntite, this.orubriques);
			this.duplicate(session, newCodeEntite, this.osavoiretres);
			this.duplicate(session, newCodeEntite, this.okeyareas);
			this.duplicate(session, newCodeEntite, this.omissions);

		}
		catch (HibernateException e)
		{
			resultat = ClsTreater._getResultat(ClsTreater._getStackTrace(e), null, true);
		}
		catch (DataAccessException e)
		{
			resultat = ClsTreater._getResultat(ClsTreater._getStackTrace(e), null, true);
		}

		return resultat;
	}

	public void setConsultationMode()
	{
		this.setConsultationMode(this.oformations);
		this.setConsultationMode(this.ofonctions);
		this.setConsultationMode(this.olangues);
		this.setConsultationMode(this.othemes);
		this.setConsultationMode(this.odiplomes);
		this.setConsultationMode(this.odocuments);
		this.setConsultationMode(this.otaches);
		this.setConsultationMode(this.ointerims);
		this.setConsultationMode(this.oposteclients);
		this.setConsultationMode(this.oavantagenatures);
		this.setConsultationMode(this.oavantagehorspaies);
		this.setConsultationMode(this.orubriques);
		this.setConsultationMode(this.osavoiretres);
		this.setConsultationMode(this.okeyareas);
		this.setConsultationMode(this.omissions);
		this.setConsultationMode(this.osavoir);
		this.setConsultationMode(this.orelationsexternes);
		this.setConsultationMode(this.orelationsinternes);
		this.setConsultationMode(this.oplansuccession);
		this.setConsultationMode(this.omoyens);
	}

	private void setConsultationMode(SortableListVO<CompetencePosteVO> datas)
	{
		for (CompetencePosteVO vo : datas.getListe())
		{
			//vo.setMode(Mode.CONSULTATION);
		}
	}

	private ClsResultat save(Session session, SortableListVO<CompetencePosteVO> datas)
	{
		ClsResultat resultat = null;
		Orgposteinfo info = null;

		if (datas.getListeASupprimer() != null)
		{
			for (CompetencePosteVO vo : datas.getListeASupprimer())
			{
				if (datas.getListe().contains(vo))
					continue;
				info = new Orgposteinfo();
				BeanUtils.copyProperties(vo, info, Orgposteinfo.class);
				session.delete(info);
			}
		}

		for (CompetencePosteVO vo : datas.getListe())
		{
			info = new Orgposteinfo();
			BeanUtils.copyProperties(vo, info, Orgposteinfo.class);
			//if (Module.CREATIO.equals(vo.getMode()))
				session.saveOrUpdate(info);

//			if (Mode.MODIFICATION.equals(vo.getMode()))
//				session.update(info);
		}
		return resultat;
	}

	private ClsResultat saveDC(Session session, SortableListVO<CompetencePosteVO> datas)
	{
		ClsResultat resultat = null;
		Orgposteinfo info = null;

		if (datas.getListeASupprimer() != null)
		{
			for (CompetencePosteVO vo : datas.getListeASupprimer())
			{
				if (datas.getListe().contains(vo))
					continue;
				info = new Orgposteinfo();
				BeanUtils.copyProperties(vo, info, Orgposteinfo.class);
				session.delete(info);
			}
		}

		for (CompetencePosteVO vo : datas.getListe())
		{
			switch (nivdcoisp)
			{
			case 1:
				vo.setCodeinfo1(vo.getCodeinfo3());
				vo.setCodeinfo2(vo.getCodeinfo3());
				vo.setCodeinfo3(vo.getCodeinfo3());
				break;

			case 2:
				vo.setCodeinfo1(vo.getCodeinfo2());
				vo.setCodeinfo2(vo.getCodeinfo2());
				vo.setCodeinfo3(vo.getCodeinfo3());

				break;

			case 3:
				vo.setCodeinfo1(vo.getCodeinfo1());
				vo.setCodeinfo2(vo.getCodeinfo2());
				vo.setCodeinfo3(vo.getCodeinfo3());
				break;
			}
			
			info = new Orgposteinfo();
			BeanUtils.copyProperties(vo, info, Orgposteinfo.class);
			//if (Mode.CREATION.equals(vo.getMode()))
				session.saveOrUpdate(info);

//			if (Mode.MODIFICATION.equals(vo.getMode()))
//				session.update(info);
		}
		return resultat;
	}

	private ClsResultat duplicate(Session session, String newCodeEntite, SortableListVO<CompetencePosteVO> datas)
	{
		ClsResultat resultat = null;
		Orgposteinfo info = null;

		for (CompetencePosteVO vo : datas.getListe())
		{
			info = new Orgposteinfo();

			BeanUtils.copyProperties(vo, info, Orgposteinfo.class);

			info.setCodeposte(newCodeEntite);

			session.saveOrUpdate(info);
		}
		return resultat;
	}

	private ClsResultat mergeResults(ClsResultat header, ClsResultat nullResult, ClsResultat invalidResult)
	{
		ClsResultat result = null;
		if (nullResult != null)
			nullResult = ClsTreater._concat(headerNullValues, nullResult, sep);

		if (invalidResult != null)
			invalidResult = ClsTreater._concat(headerInvalidValues, invalidResult, sep);

		result = ClsTreater._concat(nullResult, invalidResult, ParameterUtil.CHAINE_ALLER_A_LA_LIGNE_DOUBLE);

		if (nullResult != null || invalidResult != null)
		{
			result = ClsTreater._concat(header, result, sep2);
			return result;
		}
		return null;
	}

	public ClsResultat _checkValuesFormation()
	{
		ClsResultat nullResult = null;

		ClsResultat invalidResult = null;

		for (CompetencePosteVO comp : this.oformations.getListe())
		{
//			if (Mode.CONSULTATION.equals(comp.getMode()))
//				continue;

			if (StringUtils.isBlank(comp.getCodeinfo1()))
				nullResult = ClsTreater._concat(nullResult, ClsTreater._getResultat("Formation", "INF-80723", true), sep);
			else
				invalidResult = ClsTreater._concat(invalidResult, _checkNomenclatureExistance(comp.getCodeinfo1(), ClsNomenclature.FORMATION), sep);
		}

		return this.mergeResults(headerformations, nullResult, invalidResult);
	}

	public ClsResultat _checkValuesFonction()
	{
		ClsResultat nullResult = null;

		ClsResultat invalidResult = null;

		for (CompetencePosteVO comp : this.ofonctions.getListe())
		{
//			if (Mode.CONSULTATION.equals(comp.getMode()))
//				continue;
			if (StringUtils.isBlank(comp.getCodeinfo1()))
				nullResult = ClsTreater._concat(nullResult, ClsTreater._getResultat("Fonction", "INF-10321", true), sep);
			else
				invalidResult = ClsTreater._concat(invalidResult, _checkNomenclatureExistance(comp.getCodeinfo1(), ClsNomenclature.FONCTION), sep);
		}

		return this.mergeResults(headerfonctions, nullResult, invalidResult);
	}

	public ClsResultat _checkValuesDiplome()
	{
		ClsResultat nullResult = null;

		ClsResultat invalidResult = null;

		for (CompetencePosteVO comp : this.odiplomes.getListe())
		{
//			if (Mode.CONSULTATION.equals(comp.getMode()))
//				continue;
			if (StringUtils.isBlank(comp.getCodeinfo1()))
				nullResult = ClsTreater._concat(nullResult, ClsTreater._getResultat("Dipléme", "INF-80487", true), sep);
			else
				invalidResult = ClsTreater._concat(invalidResult, _checkNomenclatureExistance(comp.getCodeinfo1(), ClsNomenclature.DIPLOME), sep);
		}

		return this.mergeResults(headerdiplomes, nullResult, invalidResult);
	}

	public ClsResultat _checkValuesDocument()
	{
		ClsResultat nullResult = null;

		ClsResultat invalidResult = null;

		for (CompetencePosteVO comp : this.odocuments.getListe())
		{
//			if (Mode.CONSULTATION.equals(comp.getMode()))
//				continue;
			if (StringUtils.isBlank(comp.getCodeinfo1()))
				nullResult = ClsTreater._concat(nullResult, ClsTreater._getResultat("Document", "INF-81022", true), sep);
			else
				invalidResult = ClsTreater._concat(invalidResult, _checkNomenclatureExistance(comp.getCodeinfo1(), ClsNomenclature.NATURE_DOCUMENT), sep);
		}

		return this.mergeResults(headerdocuments, nullResult, invalidResult);
	}

	public ClsResultat _checkValuesLangue()
	{
		ClsResultat nullResult = null;

		ClsResultat invalidResult = null;

		for (CompetencePosteVO comp : this.olangues.getListe())
		{
//			if (Mode.CONSULTATION.equals(comp.getMode()))
//				continue;
			if (StringUtils.isBlank(comp.getCodeinfo1()))
				nullResult = ClsTreater._concat(nullResult, ClsTreater._getResultat("Langue", "INF-81268", true), sep);
			else
				invalidResult = ClsTreater._concat(invalidResult, _checkNomenclatureExistance(comp.getCodeinfo1(), ClsNomenclature.LANGUE), sep);

			if (StringUtils.isBlank(comp.getCodeinfo2()))
				nullResult = ClsTreater._concat(nullResult, ClsTreater._getResultat("Niveau", "INF-80074", true), sep);
			else
				invalidResult = ClsTreater._concat(invalidResult, _checkNomenclatureExistance(comp.getCodeinfo2(), ClsNomenclature.NIVEAUX_LANGUE), sep);
		}

		return this.mergeResults(headerlangues, nullResult, invalidResult);
	}

	public ClsResultat _checkValuesSavoirFaire()
	{
		ClsResultat nullResult = null;

		ClsResultat invalidResult = null;

		for (CompetencePosteVO comp : this.othemes.getListe())
		{
//			if (Mode.CONSULTATION.equals(comp.getMode()))
//				continue;
			if (StringUtils.isBlank(comp.getCodeinfo1()))
				nullResult = ClsTreater._concat(nullResult, ClsTreater._getResultat("Théme", "INF-80756", true), sep);
			else
				invalidResult = ClsTreater._concat(invalidResult, _checkNomenclatureExistance(comp.getCodeinfo1(), ClsNomenclature.THEME), sep);

			if (StringUtils.isBlank(comp.getCodeinfo2()))
				nullResult = ClsTreater._concat(nullResult, ClsTreater._getResultat("Domaine", "INF-80771", true), sep);
			else
				invalidResult = ClsTreater._concat(invalidResult, _checkNomenclatureExistance(comp.getCodeinfo2(), ClsNomenclature.DOMAINE_COMPETENCE), sep);

			if (StringUtils.isBlank(comp.getCodeinfo3()))
				nullResult = ClsTreater._concat(nullResult, ClsTreater._getResultat("Niveau", "INF-80074", true), sep);
			else
				invalidResult = ClsTreater._concat(invalidResult, _checkNomenclatureExistance(comp.getCodeinfo3(), ClsNomenclature.NIVEAU_THEME), sep);
		}

		return this.mergeResults(headerthemes, nullResult, invalidResult);
	}

	public ClsResultat _checkValuesTache()
	{
		ClsResultat nullResult = null;

		ClsResultat invalidResult = null;

		for (CompetencePosteVO comp : this.otaches.getListe())
		{
//			if (Mode.CONSULTATION.equals(comp.getMode()))
//				continue;
			if (StringUtils.isBlank(comp.getCodeinfo1()))
				nullResult = ClsTreater._concat(nullResult, ClsTreater._getResultat("Téche Suppl.", "INF-80488", true), sep);
			else
				invalidResult = ClsTreater._concat(invalidResult, _checkNomenclatureExistance(comp.getCodeinfo1(), ClsNomenclature.TACHE), sep);
		}

		return this.mergeResults(headertaches, nullResult, invalidResult);
	}

	public ClsResultat _checkValuesInterimPoste()
	{
		ClsResultat nullResult = null;

		ClsResultat invalidResult = null;

		for (CompetencePosteVO comp : this.ointerims.getListe())
		{
//			if (Mode.CONSULTATION.equals(comp.getMode()))
//				continue;
			if (StringUtils.isBlank(comp.getCodeinfo1()))
				nullResult = ClsTreater._concat(nullResult, ClsTreater._getResultat("Poste", "INF-81250", true), sep);
			else
				invalidResult = ClsTreater._concat(invalidResult, _checkPosteExistance(comp.getCodeinfo1()), sep);
		}

		return this.mergeResults(headerinterims, nullResult, invalidResult);
	}

	public ClsResultat _checkValuesPosteClient()
	{
		ClsResultat nullResult = null;

		ClsResultat invalidResult = null;

		for (CompetencePosteVO comp : this.oposteclients.getListe())
		{
//			if (Mode.CONSULTATION.equals(comp.getMode()))
//				continue;
			if (StringUtils.isBlank(comp.getCodeinfo1()))
				nullResult = ClsTreater._concat(nullResult, ClsTreater._getResultat("Poste", "INF-81250", true), sep);
			else
				invalidResult = ClsTreater._concat(invalidResult, _checkPosteExistance(comp.getCodeinfo1()), sep);
		}

		return this.mergeResults(headerposteclients, nullResult, invalidResult);
	}
	
	public ClsResultat _checkValuesPlansuccession()
	{
		ClsResultat nullResult = null;

		ClsResultat invalidResult = null;

		for (CompetencePosteVO comp : this.oplansuccession.getListe())
		{
			//if (Mode.CONSULTATION.equals(comp.getMode()))
//				continue;
			if (StringUtils.isBlank(comp.getCodeinfo1()))
				nullResult = ClsTreater._concat(nullResult, ClsTreater._getResultat("Poste ou emploi type", "INF-0", true), sep);
			else
				invalidResult = ClsTreater._concat(invalidResult, _checkPosteExistance(comp.getCodeinfo1()), sep);
		}

		return this.mergeResults(headerposteclients, nullResult, invalidResult);
	}

	public ClsResultat _checkValuesAvantageNature()
	{
		ClsResultat nullResult = null;

		ClsResultat invalidResult = null;

		for (CompetencePosteVO comp : this.oavantagenatures.getListe())
		{
//			if (Mode.CONSULTATION.equals(comp.getMode()))
//				continue;
			if (StringUtils.isBlank(comp.getCodeinfo1()))
				nullResult = ClsTreater._concat(nullResult, ClsTreater._getResultat("Avantage en Nature", "INF-00337", true), sep);
			else
				invalidResult = ClsTreater._concat(invalidResult, _checkNomenclatureExistance(comp.getCodeinfo1(), ClsNomenclature.AVANTAGE_EN_NATURE), sep);
		}

		return this.mergeResults(headeravantagenatures, nullResult, invalidResult);
	}

	public ClsResultat _checkValuesAvantageHorsPaie()
	{
		ClsResultat nullResult = null;

		ClsResultat invalidResult = null;

		for (CompetencePosteVO comp : this.oavantagehorspaies.getListe())
		{
//			if (Mode.CONSULTATION.equals(comp.getMode()))
//				continue;
			if (StringUtils.isBlank(comp.getCodeinfo1()))
				nullResult = ClsTreater._concat(nullResult, ClsTreater._getResultat("Avantage en Nature", "INF-00337", true), sep);
			else
				invalidResult = ClsTreater._concat(invalidResult, _checkNomenclatureExistance(comp.getCodeinfo1(), ClsNomenclature.AVANTAGE_HORS_PAIE), sep);

			if (comp.getValminfo1() == null)
				nullResult = ClsTreater._concat(nullResult, ClsTreater._getResultat("Montant", "INF-00639", true), sep);
		}

		return this.mergeResults(headeravantagehorspaies, nullResult, invalidResult);
	}

	public ClsResultat _checkValuesRubrique()
	{
		ClsResultat nullResult = null;

		ClsResultat invalidResult = null;

		for (CompetencePosteVO comp : this.orubriques.getListe())
		{
//			if (Mode.CONSULTATION.equals(comp.getMode()))
//				continue;
			if (StringUtils.isBlank(comp.getCodeinfo1()))
				nullResult = ClsTreater._concat(nullResult, ClsTreater._getResultat("Rubrique", "INF-00082", true), sep);
			else
				invalidResult = ClsTreater._concat(invalidResult, _checkRubriqueExistance(comp.getCodeinfo1()), sep);

			if (comp.getValminfo1() == null)
				nullResult = ClsTreater._concat(nullResult, ClsTreater._getResultat("Montant", "INF-00639", true), sep);
		}

		return this.mergeResults(headerrubriques, nullResult, invalidResult);
	}

	public ClsResultat _checkValuesSavoirEtre()
	{
		ClsResultat nullResult = null;

		ClsResultat invalidResult = null;

		for (CompetencePosteVO comp : this.osavoiretres.getListe())
		{
//			if (Mode.CONSULTATION.equals(comp.getMode()))
//				continue;
			if (StringUtils.isBlank(comp.getCodeinfo1()))
				nullResult = ClsTreater._concat(nullResult, ClsTreater._getResultat("Savoir-étre", "INF-81267", true), sep);
			else
				invalidResult = ClsTreater._concat(invalidResult, _checkNomenclatureExistance(comp.getCodeinfo1(), ClsNomenclature.SAVOIR_ETRE), sep);

			if (StringUtils.isBlank(comp.getCodeinfo2()))
				nullResult = ClsTreater._concat(nullResult, ClsTreater._getResultat("Niveau", "INF-80074", true), sep);
			else
				invalidResult = ClsTreater._concat(invalidResult, _checkNomenclatureExistance(comp.getCodeinfo2(), ClsNomenclature.NIVEAU_THEME), sep);
		}

		return this.mergeResults(headersavoiretres, nullResult, invalidResult);
	}
	
	public ClsResultat _checkValuesSavoir()
	{
		ClsResultat nullResult = null;

		ClsResultat invalidResult = null;

		for (CompetencePosteVO comp : this.osavoir.getListe())
		{
//			if (Mode.CONSULTATION.equals(comp.getMode()))
//				continue;
			if (StringUtils.isBlank(comp.getCodeinfo1()))
				nullResult = ClsTreater._concat(nullResult, ClsTreater._getResultat("Savoir", "INF-0", true), sep);
			else
				invalidResult = ClsTreater._concat(invalidResult, _checkNomenclatureExistance(comp.getCodeinfo1(), ClsNomenclature.SAVOIR), sep);
		}

		return this.mergeResults(headersavoir, nullResult, invalidResult);
	}
	
	public ClsResultat _checkValuesRelExternes()
	{
		ClsResultat nullResult = null;

		ClsResultat invalidResult = null;

		for (CompetencePosteVO comp : this.orelationsexternes.getListe())
		{
//			if (Mode.CONSULTATION.equals(comp.getMode()))
//				continue;
			if (StringUtils.isBlank(comp.getCodeinfo1()))
				nullResult = ClsTreater._concat(nullResult, ClsTreater._getResultat("Relations externes", "INF-0", true), sep);
			else
				invalidResult = ClsTreater._concat(invalidResult, _checkNomenclatureExistance(comp.getCodeinfo1(), ClsNomenclature.RELATIONS_EXTERNES), sep);
		}

		return this.mergeResults(headerrelexternes, nullResult, invalidResult);
	}
	
	public ClsResultat _checkValuesRelInternes()
	{
		ClsResultat nullResult = null;

		ClsResultat invalidResult = null;

		for (CompetencePosteVO comp : this.orelationsinternes.getListe())
		{
//			if (Mode.CONSULTATION.equals(comp.getMode()))
//				continue;
			if (StringUtils.isBlank(comp.getCodeinfo1()))
				nullResult = ClsTreater._concat(nullResult, ClsTreater._getResultat("Relations internes", "INF-0", true), sep);
			else
				invalidResult = ClsTreater._concat(invalidResult, _checkNomenclatureExistance(comp.getCodeinfo1(), ClsNomenclature.RELATIONS_INTERNES), sep);
		}

		return this.mergeResults(headerrelinternes, nullResult, invalidResult);
	}

	public ClsResultat _checkValuesDomaineCle()
	{
		ClsResultat nullResult = null;

		ClsResultat invalidResult = null;

		for (CompetencePosteVO comp : this.okeyareas.getListe())
		{
//			if (Mode.CONSULTATION.equals(comp.getMode()))
//				continue;
//
//			if (StringUtils.isBlank(comp.getCodeinfo3()))
//				nullResult = ClsTreater._concat(nullResult, ClsTreater._getResultat("Domaine Clé", "INF-80482", true), sep);
//			else
//				invalidResult = ClsTreater._concat(invalidResult, _checkNomenclatureExistance(comp.getCodeinfo3(), ClsNomenclature.DOMAINE_CLE), sep);
//
//			if (nivdcoisp > 1)
//			{
//				if (StringUtils.isBlank(comp.getCodeinfo2()))
//					nullResult = ClsTreater._concat(nullResult, ClsTreater._getResultat("Objectif Intermédiaire", "INF-80483", true), sep);
//				else
//					invalidResult = ClsTreater._concat(invalidResult, _checkNomenclatureExistance(comp.getCodeinfo2(), ClsNomenclature.OBJECTIF_INTERMEDIAIRE), sep);
//			}
//
//			if (nivdcoisp > 2)
//			{
				if (StringUtils.isBlank(comp.getCodeinfo1()))
					nullResult = ClsTreater._concat(nullResult, ClsTreater._getResultat("Standard de Performance", "INF-80484", true), sep);
				else
					invalidResult = ClsTreater._concat(invalidResult, _checkNomenclatureExistance(comp.getCodeinfo1(), ClsNomenclature.STANDARD_PERFORMANCE), sep);
//			}
		}
		return this.mergeResults(headerkeyareas, nullResult, invalidResult);
	}
		
		public ClsResultat _checkValuesMoyens()
		{
			ClsResultat nullResult = null;

			ClsResultat invalidResult = null;

			for (CompetencePosteVO comp : this.omoyens.getListe())
			{
					if (StringUtils.isBlank(comp.getCodeinfo1()))
						nullResult = ClsTreater._concat(nullResult, ClsTreater._getResultat("Moyens nécessaires", "INF-0", true), sep);
					else
						invalidResult = ClsTreater._concat(invalidResult, _checkNomenclatureExistance(comp.getCodeinfo1(), "294"), sep);
			}

		return this.mergeResults(headermoyens, nullResult, invalidResult);
	}

	public ClsResultat _checkValuesMission()
	{
		ClsResultat nullResult = null;

		ClsResultat invalidResult = null;

		for (CompetencePosteVO comp : this.omissions.getListe())
		{
//			if (Mode.CONSULTATION.equals(comp.getMode()))
//				continue;
//			if (StringUtils.isBlank(comp.getCodeinfo2()))
//				nullResult = ClsTreater._concat(nullResult, ClsTreater._getResultat("Domaine Clé", "INF-80482", true), sep);
//			else
//				invalidResult = ClsTreater._concat(invalidResult, _checkNomenclatureExistance(comp.getCodeinfo2(), ClsNomenclature.DOMAINE_CLE), sep);

			if (StringUtils.isBlank(comp.getCodeinfo1()))
				nullResult = ClsTreater._concat(nullResult, ClsTreater._getResultat("Mission", "INF-80486", true), sep);
			else
				invalidResult = ClsTreater._concat(invalidResult, _checkNomenclatureExistance(comp.getCodeinfo1(), ClsNomenclature.MISSION), sep);
		}

		return this.mergeResults(headermissions, nullResult, invalidResult);
	}

	private ClsResultat _checkNomenclatureExistance(String code, String table)
	{
		ClsResultat result = null;
		ParamData fnom = null;//(Rhfnom) service.get(Rhfnom.class, new RhfnomPK(dossier, Integer.parseInt(table), code, 1));
		List<ParamData> list = service.find("from ParamData where identreprise='"+dossier+"' And ctab="+Integer.parseInt(table)+" And cacc='"+code+"' And nume=1");
		if (list != null && list.size() > 0)
		{
			fnom = list.get(0);
		}
		if (fnom == null)
			result = ClsTreater._getResultat("La donnée % n'existe pas", "ERR-80135", true, code);

		return result;
	}

	private ClsResultat _checkPosteExistance(String code)
	{
		ClsResultat result = null;
		Orgposte fnom = null;//(Rhtposte) service.get(Rhtposte.class, new RhtpostePK(dossier, code));
		List<Orgposte> list = service.find("from Orgposte where identreprise='"+dossier+"' And codeposte='"+code+"'");
		if (list != null && list.size() > 0)
		{
			fnom = list.get(0);
		}
		if (fnom == null)
			result = ClsTreater._getResultat("La donnée % n'existe pas", "ERR-80135", true, code);

		return result;
	}

	private ClsResultat _checkRubriqueExistance(String code)
	{
		ClsResultat result = null;
		ElementSalaire fnom = null;//(Rhprubrique) service.get(Rhprubrique.class, new RhprubriquePK(dossier, code));
		List<ElementSalaire> list = service.find("from ElementSalaire where identreprise='"+dossier+"' And crub='"+code+"'");
		if (list != null && list.size() > 0)
		{
			fnom = list.get(0);
		}
		if (fnom == null)
			result = ClsTreater._getResultat("La donnée % n'existe pas", "ERR-80135", true, code);

		return result;
	}

	public String getCodeEntite()
	{
		return codeEntite;
	}

	public void setCodeEntite(String codeEntite)
	{
		this.codeEntite = codeEntite;
	}

	public GeneriqueConnexionService getService()
	{
		return service;
	}

	public void setService(GeneriqueConnexionService service)
	{
		this.service = service;
	}

	public String getDossier()
	{
		return dossier;
	}

	public void setDossier(String dossier)
	{
		this.dossier = dossier;
	}

	public String getLangue()
	{
		return langue;
	}

	public void setLangue(String langue)
	{
		this.langue = langue;
	}

	public ClsResultat getHeadermoyens() {
		return headermoyens;
	}

	public void setHeadermoyens(ClsResultat headermoyens) {
		this.headermoyens = headermoyens;
	}
	
}
