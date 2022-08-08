package com.kinart.organisation.business.vo;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.kinart.organisation.business.model.Orgeffectif;
import com.kinart.paie.business.services.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;

public class EffectifPrevisionnelVO extends Orgeffectif
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	EffectifPrevisionnelVO copie = null;

	boolean selected = false;

	//Mode mode;

	@SuppressWarnings("unused")
	private boolean hasbeenmodify = false;

	public EffectifPrevisionnelVO()
	{
		super();
		//this.setMode(Mode.CREATION);
	}
	
	public void creerCopie()
	{
		copie = new EffectifPrevisionnelVO();
		BeanUtils.copyProperties(this, copie);
	}
	
	public void annulerModification()
	{
		BeanUtils.copyProperties(copie,this);
		copie = null;
	}

	public static EffectifPrevisionnelVO getBusinessObject(GeneriqueConnexionService service, String codeDossier, String codeOrganigramme, String code)
	{
		EffectifPrevisionnelVO vo = new EffectifPrevisionnelVO();

//		Orgeffectif business = (Orgeffectif) service.get(Orgeffectif.class, new OrgeffectifPK( codeDossier, codeOrganigramme, code));
//
//		if (business != null)
//		{
//			BeanUtils.copyProperties(business, vo);
//
//			vo.setMode(Mode.MODIFICATION);
//		}
		return vo;
	}

	public Orgeffectif getEntityObject()
	{
		Orgeffectif business = new Orgeffectif();

		BeanUtils.copyProperties(this, business);

		return business;
	}

	public ClsResultat save(HttpServletRequest request, GeneriqueConnexionService service)
	{
		return this.saveAData(request, service, true, false);
	}

	public ClsResultat update(HttpServletRequest request, GeneriqueConnexionService service)
	{
		return this.saveAData(request, service, false, false);
	}

	public ClsResultat delete(HttpServletRequest request, GeneriqueConnexionService service)
	{
		return this.saveAData(request, service, false, true);
	}

	public ClsResultat exist(HttpServletRequest request, GeneriqueConnexionService service)
	{
//		Orgeffectif business = (Orgeffectif) service.get(Orgeffectif.class, new OrgeffectifPK(pk.getCdos(), pk.getCodeorganigramme(), pk.getCodeeffectif()));
//		if (business != null)
//			return ClsTreater._getResultat("La ligne existe deja", "INF-80432", true);
//		else
			return null;
	}

	private ClsResultat saveAData(HttpServletRequest request, GeneriqueConnexionService service, boolean save, boolean delete)
	{
		ClsResultat resultat = null;
		Session sess = service.getSession();
		Transaction tx = null;

		Orgeffectif business = this.getEntityObject();

		try
		{
			tx = sess.beginTransaction();
			if (delete)
			{
				sess.delete(business);
				// Suppression du d�tail
			}
			else
			{
				resultat = this._checkValues(request);
				if (resultat != null)
					return resultat;

				if (save)
				{
					sess.save(business);
				}
				else
					sess.update(business);
			}
			tx.commit();
			
			if (delete)
				resultat = ClsTreater._getResultat("Suppression �ffectu�e avec succ�s", "INF-80180", false);
			else
			{
				if (save)
					resultat = ClsTreater._getResultat("Sauvegarde �ffectu�e avec succ�s", "INF-80178", false);
				else
					resultat = ClsTreater._getResultat("Mise � jour �ffectu�e avec succ�s", "INF-80179", false);
			}
		}
		catch (Exception e)
		{
			if (tx != null)
				tx.rollback();

			e.printStackTrace();

			resultat = ClsTreater._getResultat(ClsTreater._getStackTrace(e), null, true);
		}
		finally
		{
			service.closeSession(sess);
		}
		return resultat;
	}

	public ClsResultat _checkValues(HttpServletRequest request)
	{

		ClsResultat _o_Result_Start = ClsTreater._getResultat("Saisir la valeur du", "INF-80097", true);

		ClsResultat _o_Result_End = null;

		String sep = ParameterUtil.CHAINE_ALLER_A_LA_LIGNE + ParameterUtil.CHAINE_TIRET;

//		OrgeffectifPK pk = this.getComp_id();
//
//		if (StringUtils.isBlank(pk.getCodeeffectif()))
//			_o_Result_End = ClsTreater._concat(_o_Result_End, ClsTreater._getResultat("Code", "INF-00034", true), sep);

		if (StringUtils.isBlank(this.getLibelle()))
			_o_Result_End = ClsTreater._concat(_o_Result_End, ClsTreater._getResultat("Libell�", "INF-00626", true), sep);

		if (this.getDateprevision() == null)
			_o_Result_End = ClsTreater._concat(_o_Result_End, ClsTreater._getResultat("Date", "INF-51145", true), sep);
		

		if (_o_Result_End == null)
			return null;
		else
			return ClsTreater._concat(_o_Result_Start, _o_Result_End, sep);
	}

	public ClsResultat _checkExistance(HttpServletRequest request, GeneriqueConnexionService service)
	{
		ClsResultat resultat = null;
//		OrgeffectifPK pk = this.getComp_id();
//		Orgeffectif business = (Orgeffectif) service.get(Orgeffectif.class, new OrgeffectifPK(pk.getCdos(), pk.getCodeorganigramme(), pk.getCodeeffectif()));
//		if (business != null)
//			resultat = ClsTreater._getResultat("La donn�e % existe d�ja", "ERR-80020", true, pk.getCodeeffectif());
		return resultat;
	}

	@SuppressWarnings("unchecked")
	public static List<EffectifPrevisionnelVO> buildListOfDataToDisplay(GeneriqueConnexionService service, RechercheEffectifPrevisionnelVO search)
	{
		List<EffectifPrevisionnelVO> listOfDataToDisplay = new ArrayList<EffectifPrevisionnelVO>();

		String queryString = getQueryString(search);

		try
		{
			Session session = service.getSession();
			Query q = session.createQuery(queryString);
			EffectifPrevisionnelVO.setQueryParameters(q, search);
			List<Orgeffectif> liste = q.list();
			EffectifPrevisionnelVO vo = null;
			for (Orgeffectif o : liste)
			{
				vo = new EffectifPrevisionnelVO();
				BeanUtils.copyProperties(o, vo);
				//vo.setMode(Mode.CONSULTATION);
				listOfDataToDisplay.add(vo);
			}

			service.closeSession(session);
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
		}
		catch (BeansException e)
		{
			e.printStackTrace();
		}
		return listOfDataToDisplay;
	}

	private static String getQueryString(RechercheEffectifPrevisionnelVO search)
	{
		String queryString = "From Orgeffectif where identreprise = :cdos and codeorganigramme = :codeorganigramme ";

		if (StringUtils.isNotBlank(search.code))
			queryString += "and comp_id.codeeffectif like :code ";

		if (StringUtils.isNotBlank(search.libelle))
			queryString += "and UPPER(libelle) like :libelle ";

		if (search.datemin != null)
			queryString += "and dateprevision >= :datemin ";

		if (search.datemax != null)
			queryString += "and dateprevision<= :datemax ";

		queryString += " order by codeeffectif";

		return queryString;
	}

	private static void setQueryParameters(Query query, RechercheEffectifPrevisionnelVO search)
	{
		query.setParameter("cdos", search.cdos, StandardBasicTypes.STRING);
		
		query.setParameter("codeorganigramme", search.codeorganigramme, StandardBasicTypes.STRING);
		

		if (StringUtils.isNotBlank(search.code))
			query.setParameter("code", "%" + search.code + "%", StandardBasicTypes.STRING);

		if (StringUtils.isNotBlank(search.libelle))
			query.setParameter("libelle", "%" + search.libelle.toUpperCase() + "%", StandardBasicTypes.STRING);

		if (search.datemin != null)
			query.setParameter("datemin", search.datemin, StandardBasicTypes.DATE);

		if (search.datemax != null)
			query.setParameter("datemax", search.datemax, StandardBasicTypes.DATE);

	}

	public boolean isSelected()
	{
		return selected;
	}

	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}

//	public Mode getMode()
//	{
//		return mode;
//	}
//
//	public void setMode(Mode mode)
//	{
//		this.mode = mode;
//	}
//
//	public boolean hasbeenmodify()
//	{
//		return Mode.MODIFICATION.equals(this.getMode()) || Mode.CREATION.equals(this.getMode());
//	}

	public void setHasbeenmodify(boolean hasbeenmodify)
	{
		this.hasbeenmodify = hasbeenmodify;
	}

}
