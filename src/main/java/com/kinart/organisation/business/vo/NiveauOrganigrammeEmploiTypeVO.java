package com.kinart.organisation.business.vo;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.kinart.organisation.business.model.Organigramme;
import com.kinart.organisation.business.model.Orgniveauemploitype;
import com.kinart.organisation.business.model.Orgposte;
import com.kinart.paie.business.services.utils.ClsResultat;
import com.kinart.paie.business.services.utils.ClsTreater;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.kinart.paie.business.services.utils.ParameterUtil;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;

public class NiveauOrganigrammeEmploiTypeVO extends Orgniveauemploitype
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	NiveauOrganigrammeEmploiTypeVO copie = null;

	public void creerCopie()
	{
		copie = new NiveauOrganigrammeEmploiTypeVO();
		//RhpniveauemploitypePK copiePK = new RhpniveauemploitypePK();
		//BeanUtils.copyProperties(this.getComp_id(), copiePK);
		BeanUtils.copyProperties(this, copie);
		//copie.setComp_id(copiePK);
	}

	public void annulerModification()
	{
		//RhpniveauemploitypePK copiePK = new RhpniveauemploitypePK();
		//BeanUtils.copyProperties(copie.getComp_id(), copiePK);
		BeanUtils.copyProperties(copie, this);
		//this.setComp_id(copiePK);
		copie = null;
	}

	boolean selected = false;

	//Mode mode;
	
	private String libelle;
	
	@SuppressWarnings("unused")
	private boolean hasbeenmodify = false;

	public NiveauOrganigrammeEmploiTypeVO()
	{
		super();
		//this.setMode(Mode.CREATION);
	}

	public static NiveauOrganigrammeEmploiTypeVO getBusinessObject(GeneriqueConnexionService service, String codeDossier, String codeNiveau, String codeET)
	{
		NiveauOrganigrammeEmploiTypeVO vo = new NiveauOrganigrammeEmploiTypeVO();

        Orgniveauemploitype business = null;//(Rhpniveauemploitype) service.get(Rhpniveauemploitype.class, new RhpniveauemploitypePK(codeNiveau, codeET,codeDossier));
        List<Orgniveauemploitype> list = service.find("from Orgniveauemploitype where identreprise='"+codeDossier+"' And codeniveau='"+codeNiveau+"' And codeemploitype='"+codeET+"'");
        if (list != null && list.size() > 0)
        {
            business = list.get(0);
        }

		if (business != null)
		{
			BeanUtils.copyProperties(business, vo);

			//vo.setMode(Mode.MODIFICATION);
		}
		return vo;
	}

	public Orgniveauemploitype getEntityObject()
	{
        Orgniveauemploitype business = new Orgniveauemploitype();

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
		//RhpniveauemploitypePK pk = this.getComp_id();
        Orgniveauemploitype business = null;//(Rhpniveauemploitype) service.get(Rhpniveauemploitype.class, new RhpniveauemploitypePK(pk.getCodeniveau(), pk.getCodeemploitype(),pk.getCdos()));
        List<Orgniveauemploitype> list = service.find("from Orgniveauemploitype where identreprise='"+this.getIdEntreprise()+"' And codeniveau='"+this.getCodeniveau()+"' And codeemploitype='"+this.getCodeemploitype()+"'");
        if (list != null && list.size() > 0)
        {
            business = list.get(0);
        }

        if (business != null)
			return ClsTreater._getResultat("La ligne existe deja", "INF-80432", true);
		else
			return null;
	}

	private ClsResultat saveAData(HttpServletRequest request, GeneriqueConnexionService service, boolean save, boolean delete)
	{
		ClsResultat resultat = null;
		Session sess = service.getSession();
		Transaction tx = null;

        Orgniveauemploitype business = this.getEntityObject();

		try
		{
			tx = sess.beginTransaction();
			if (delete)
			{
				sess.delete(business);
			}
			else
			{
				resultat = this._checkValues(request, service);
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
				resultat = ClsTreater._getResultat("Suppression éffectuée avec succés", "INF-80180", false);
			else
			{
				if (save)
					resultat = ClsTreater._getResultat("Sauvegarde éffectuée avec succés", "INF-80178", false);
				else
					resultat = ClsTreater._getResultat("Mise é jour éffectuée avec succés", "INF-80179", false);
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

	public ClsResultat _checkValues(HttpServletRequest request, GeneriqueConnexionService service)
	{

		ClsResultat _o_Result_Start = ClsTreater._getResultat("Saisir la valeur du", "INF-80097", true);

		ClsResultat _o_Result_End = null;

		String sep = ParameterUtil.CHAINE_ALLER_A_LA_LIGNE + ParameterUtil.CHAINE_TIRET;

		//RhpniveauemploitypePK pk = this.getComp_id();

		if (StringUtils.isBlank(this.getCodeemploitype()))
			_o_Result_End = ClsTreater._concat(_o_Result_End, ClsTreater._getResultat("Code", "INF-00034", true), sep);
		else
		{
			Orgposte business = null;//(Rhtposte) service.get(Rhtposte.class, new RhtpostePK(pk.getCdos(), pk.getCodeemploitype()));
            List<Orgposte> list = service.find("from Orgposte where identreprise='"+this.getIdEntreprise()+"' And codeemploitype='"+this.getCodeemploitype()+"'");
            if (list != null && list.size() > 0)
            {
                business = list.get(0);
            }

			if (business == null)
				return ClsTreater._getResultat("La donnée % n'existe pas", "ERR-80135", true, this.getCodeemploitype());
		}

		if (_o_Result_End == null)
			return null;
		else
			return ClsTreater._concat(_o_Result_Start, _o_Result_End, sep);
	}

	public ClsResultat _checkExistance(HttpServletRequest request, GeneriqueConnexionService service)
	{
		ClsResultat resultat = null;
		//RhpniveauemploitypePK pk = this.getComp_id();
        Orgniveauemploitype business = null;//(Rhpniveauemploitype) service.get(Rhpniveauemploitype.class, new RhpniveauemploitypePK(pk.getCodeniveau(), pk.getCodeemploitype(),pk.getCdos()));
        List<Orgniveauemploitype> list = service.find("from Orgniveauemploitype where identreprise='"+this.getIdEntreprise()+"' And codeniveau='"+this.getCodeniveau()+"' And codeemploitype='"+this.getCodeemploitype()+"'");
        if (list != null && list.size() > 0)
        {
            business = list.get(0);
        }
        if (business != null)
			resultat = ClsTreater._getResultat("La donnée % existe déja", "ERR-80020", true, this.getCodeniveau()+" -  "+this.getCodeemploitype());
		return resultat;
	}

	@SuppressWarnings("unchecked")
	public static List<NiveauOrganigrammeEmploiTypeVO> buildListOfDataToDisplay(GeneriqueConnexionService service, RechercheNiveauOrganigrammeEmploiTypeVO search)
	{
		List<NiveauOrganigrammeEmploiTypeVO> listOfDataToDisplay = new ArrayList<NiveauOrganigrammeEmploiTypeVO>();

		String queryString = getQueryString(service, search);

		try
		{
			Session session = service.getSession();
			Query q = session.createSQLQuery(queryString);

			NiveauOrganigrammeEmploiTypeVO.setQueryParameters(q, search);

			List<Object[]> liste = q.list();

			NiveauOrganigrammeEmploiTypeVO vo = null;

			for (Object[] o : liste)
			{
				vo = new NiveauOrganigrammeEmploiTypeVO();
				 
				 vo.setIdEntreprise(new Integer(search.cdos));
				 vo.setCodeemploitype(search.codeemploitype);
				 
				if (o[0] != null)
					vo.setCodeniveau((String) o[0]);

				if (o[1] != null)
					vo.setLibelle((String) o[1]);

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

	private static String getQueryString(GeneriqueConnexionService service, RechercheNiveauOrganigrammeEmploiTypeVO search)
	{

		String queryString = "Select a.codeniveau,d.libelle ";
		queryString += " From Orgniveauemploitype a ";
		queryString += " Left join Orgniveau d on a.identreprise=d.identreprise and a.codeniveau=d.codeniveau ";
		queryString += " Where a.identreprise = :cdos and a.codeemploitype = :codeemploitype ";

		if (StringUtils.isNotBlank(search.getCode()))
			queryString += "and UPPER(a.codeniveau) like :codeniveau ";

		if (StringUtils.isNotBlank(search.getLibelle()))
			queryString += "and UPPER(d.libelle) like :libelle ";

		queryString += " order by a.codeniveau";

		return queryString;
	}

	private static void setQueryParameters(Query query, RechercheNiveauOrganigrammeEmploiTypeVO search)
	{
		query.setParameter("cdos", search.cdos, StandardBasicTypes.STRING);
		
		query.setParameter("codeemploitype", search.codeemploitype, StandardBasicTypes.STRING);

		if (StringUtils.isNotBlank(search.code))
			query.setParameter("codeniveau", "%" + search.code.toUpperCase() + "%", StandardBasicTypes.STRING);

		if (StringUtils.isNotBlank(search.libelle))
			query.setParameter("libelle", "%" + search.libelle.toUpperCase() + "%", StandardBasicTypes.STRING);
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

	public String getLibelle()
	{
		return libelle;
	}

	public void setLibelle(String libelle)
	{
		this.libelle = libelle;
	}
}
