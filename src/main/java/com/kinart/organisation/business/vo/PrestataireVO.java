package com.kinart.organisation.business.vo;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.kinart.organisation.business.model.Organigramme;
import com.kinart.organisation.business.model.Organigrammeassocie;
import com.kinart.paie.business.services.cloture.ClsNomenclature;
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

public class PrestataireVO extends Organigrammeassocie
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	PrestataireVO copie = null;

	boolean selected = false;

	//Mode mode;

	@SuppressWarnings("unused")
	private boolean hasbeenmodify = false;
	
	private List<ClsTemplate> prestataires;

	public PrestataireVO()
	{
		super();
		//this.setMode(Mode.CREATION);
	}
	
	public void creerCopie()
	{
		copie = new PrestataireVO();
		//RhporganigrammeassociePK copiePK = new RhporganigrammeassociePK();
		//BeanUtils.copyProperties(this.getComp_id(), copiePK);
		BeanUtils.copyProperties(this, copie);
		//copie.setComp_id(copiePK);
	}
	
	public void annulerModification()
	{
		//RhporganigrammeassociePK copiePK = new RhporganigrammeassociePK();
		//BeanUtils.copyProperties(copie.getComp_id() ,copiePK);
		BeanUtils.copyProperties(copie,this);
		//this.setComp_id(copiePK);
		copie = null;
	}

	public static PrestataireVO getBusinessObject(GeneriqueConnexionService service, String codeDossier, String codeOrganigramme, String code)
	{
		PrestataireVO vo = new PrestataireVO();

		Organigrammeassocie business = null;//(Rhporganigrammeassocie) service.get(Rhporganigrammeassocie.class, new RhporganigrammeassociePK( codeDossier, codeOrganigramme, code));
		List<Organigrammeassocie> list = service.find("from Organigrammeassocie where identreprise='"+codeDossier+"' And codeorganigramme='"+codeOrganigramme+"' And codeexterne='"+code+"'");
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

	public Organigrammeassocie getEntityObject()
	{
		Organigrammeassocie business = new Organigrammeassocie();

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
		//RhporganigrammeassociePK pk = this.getComp_id();
		Organigrammeassocie business = null;//(Rhporganigrammeassocie) service.get(Rhporganigrammeassocie.class, new RhporganigrammeassociePK(pk.getCdos(), pk.getCodeorganigramme(), pk.getCodeexterne()));
		List<Organigrammeassocie> list = service.find("from Organigrammeassocie where identreprise='"+this.getIdEntreprise()+"' And codeorganigramme='"+this.getCodeorganigramme()+"' And codeexterne='"+this.getCodeexterne()+"'");
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

		Organigrammeassocie business = this.getEntityObject();

		try
		{
			tx = sess.beginTransaction();
			if (delete)
			{
				sess.delete(business);
				// Suppression du détail
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

	public ClsResultat _checkValues(HttpServletRequest request)
	{

		ClsResultat _o_Result_Start = ClsTreater._getResultat("Saisir la valeur du", "INF-80097", true);

		ClsResultat _o_Result_End = null;

		String sep = ParameterUtil.CHAINE_ALLER_A_LA_LIGNE + ParameterUtil.CHAINE_TIRET;

		//RhporganigrammeassociePK pk = this.getComp_id();

		if (StringUtils.isBlank(this.getCodeexterne()))
			_o_Result_End = ClsTreater._concat(_o_Result_End, ClsTreater._getResultat("Code", "INF-00034", true), sep);

//		if (StringUtils.isBlank(this.getNomexterne()))
//			_o_Result_End = ClsTreater._concat(_o_Result_End, ClsTreater._getResultat("Libellé", "INF-00626", true), sep);

//		if (this.getEffectif()== null)
//			_o_Result_End = ClsTreater._concat(_o_Result_End, ClsTreater._getResultat("Effectif", "INF-81107", true), sep);
		

		if (_o_Result_End == null)
			return null;
		else
			return ClsTreater._concat(_o_Result_Start, _o_Result_End, sep);
	}

	public ClsResultat _checkExistance(HttpServletRequest request, GeneriqueConnexionService service)
	{
		ClsResultat resultat = null;
		//RhporganigrammeassociePK pk = this.getComp_id();
		Organigrammeassocie business = null;//(Rhporganigrammeassocie) service.get(Rhporganigrammeassocie.class, new RhporganigrammeassociePK(pk.getCdos(), pk.getCodeorganigramme(), pk.getCodeexterne()));
		List<Organigrammeassocie> list = service.find("from Organigrammeassocie where identreprise='"+this.getIdEntreprise()+"' And codeorganigramme='"+this.getCodeorganigramme()+"' And codeexterne='"+this.getCodeexterne()+"'");
		if (list != null && list.size() > 0)
		{
			business = list.get(0);
		}
		if (business != null)
			resultat = ClsTreater._getResultat("La donnée % existe déja", "ERR-80020", true, this.getCodeexterne());
		return resultat;
	}

	@SuppressWarnings("unchecked")
	public static List<PrestataireVO> buildListOfDataToDisplay(GeneriqueConnexionService service, RecherchePrestataireVO search)
	{
		List<PrestataireVO> listOfDataToDisplay = new ArrayList<PrestataireVO>();

		String queryString = getQueryString(search);

		try
		{
			Session session = service.getSession();
			Query q = session.createSQLQuery(queryString)
								.addEntity("a",Organigrammeassocie.class)
								.addScalar("libelleexterne", StandardBasicTypes.STRING);
			PrestataireVO.setQueryParameters(q, search);
			List<Object[]> liste = q.list();
			PrestataireVO vo = null;
			for (Object[] o : liste)
			{
				vo = new PrestataireVO();
				BeanUtils.copyProperties((Organigrammeassocie)o[0], vo);
				
				if(o[1] != null)
					vo.setNomexterne((String)o[1]);
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

	private static String getQueryString(RecherchePrestataireVO search)
	{
		String queryString = "Select a.*, b.vall as libelleexterne From Organigrammeassocie a ";
		queryString+="Left Join ParamData b on a.identreprise=b.identreprise and a.codeexterne=b.cacc and b.nume = :nume1 and b.ctab = :ctab1 ";
		queryString+=" where a.identreprise = :cdos and a.codeorganigramme = :codeorganigramme ";

		if (StringUtils.isNotBlank(search.code))
			queryString += "and a.codeexterne like :code ";
		
		if (StringUtils.isNotBlank(search.libelle))
			queryString += "and b.vall like :libelle ";

		queryString += " order by a.codeexterne";

		return queryString;
	}

	private static void setQueryParameters(Query query, RecherchePrestataireVO search)
	{
		query.setParameter("cdos", search.cdos, StandardBasicTypes.STRING);
		
		query.setParameter("codeorganigramme", search.codeorganigramme, StandardBasicTypes.STRING);
		
		query.setParameter("nume1",1, StandardBasicTypes.INTEGER);
		
		query.setParameter("ctab1", ClsNomenclature.PRESTATAIRES, StandardBasicTypes.STRING);
		

		if (StringUtils.isNotBlank(search.code))
			query.setParameter("code", "%" + search.code + "%", StandardBasicTypes.STRING);
		
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

	public List<ClsTemplate> getPrestataires()
	{
		return prestataires;
	}

	public void setPrestataires(List<ClsTemplate> prestataires)
	{
		this.prestataires = prestataires;
	}
	
	

}
