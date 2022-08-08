package com.kinart.organisation.business.vo;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.kinart.organisation.business.model.Organigramme;
import com.kinart.organisation.business.model.Orgniveauemploitype;
import com.kinart.organisation.business.model.Orgposte;
import com.kinart.paie.business.services.cloture.ClsNomenclature;
import com.kinart.paie.business.services.utils.ClsResultat;
import com.kinart.paie.business.services.utils.ClsTreater;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.kinart.paie.business.services.utils.ParameterUtil;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;

public class EmploiTypeNiveauOrganigrammeVO extends Orgniveauemploitype
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	EmploiTypeNiveauOrganigrammeVO copie = null;

	public void creerCopie()
	{
		copie = new EmploiTypeNiveauOrganigrammeVO();
		BeanUtils.copyProperties(this, copie);
	}

	public void annulerModification()
	{
		BeanUtils.copyProperties(copie, this);
		copie = null;
	}

	boolean selected = false;

	//Mode mode;
	
	private String libelle;
	
	private String cat;

	private String libellecategorie;
	
	private String ech;

	private String libelleechelon;

	@SuppressWarnings("unused")
	private boolean hasbeenmodify = false;

	public EmploiTypeNiveauOrganigrammeVO()
	{
		super();
	}

	public static EmploiTypeNiveauOrganigrammeVO getBusinessObject(GeneriqueConnexionService service, String codeDossier, String codeNiveau, String codeET)
	{
		EmploiTypeNiveauOrganigrammeVO vo = new EmploiTypeNiveauOrganigrammeVO();

		Orgniveauemploitype business = null;//(Orgniveauemploitype) service.get(Orgniveauemploitype.class, new OrgniveauemploitypePK(codeNiveau, codeET,codeDossier));
		List<Orgniveauemploitype> orgs = service.find("FROM Orgniveauemploitype WHERE identreprise = " + "'" + codeDossier + "'" + " AND codeniveau = " + "'" + codeNiveau + "'"+ " AND codeemploitype = " + "'" + codeET + "'");
		if (!orgs.isEmpty())
			business = orgs.get(0);
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
		//OrgniveauemploitypePK pk = this.getComp_id();
		Orgniveauemploitype business = null;//(Orgniveauemploitype) service.get(Orgniveauemploitype.class, new OrgniveauemploitypePK(pk.getCodeniveau(), pk.getCodeemploitype(),pk.getCdos()));
		List<Orgniveauemploitype> orgs = service.find("FROM Orgniveauemploitype WHERE identreprise = " + "'" + this.getIdEntreprise() + "'" + " AND codeniveau = " + "'" + this.getCodeniveau() + "'"+ " AND codeemploitype = " + "'" + this.getCodeemploitype() + "'");
		if (!orgs.isEmpty())
			business = orgs.get(0);
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

		//OrgniveauemploitypePK pk = this.getComp_id();

		if (StringUtils.isBlank(this.getCodeemploitype()))
			_o_Result_End = ClsTreater._concat(_o_Result_End, ClsTreater._getResultat("Code", "INF-00034", true), sep);
		else
		{
			Orgposte business = null;//(Orgposte) service.get(Orgposte.class, new OrgpostePK(pk.getCdos(), pk.getCodeemploitype()));
			List<Orgposte> orgs = service.find("FROM Orgposte WHERE identreprise = " + "'" + this.getIdEntreprise() + "'" + " AND codeemploitype = " + "'" + this.getCodeemploitype() + "'");
			if (!orgs.isEmpty())
				business = orgs.get(0);
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
		//OrgniveauemploitypePK pk = this.getComp_id();
		Orgniveauemploitype business = null;//(Orgniveauemploitype) service.get(Orgniveauemploitype.class, new OrgniveauemploitypePK(pk.getCodeniveau(), pk.getCodeemploitype(),pk.getCdos()));
		List<Orgniveauemploitype> orgs = service.find("FROM Orgniveauemploitype WHERE identreprise = " + "'" + this.getIdEntreprise() + "'" + " AND codeemploitype = " + "'" + this.getCodeemploitype() + "'"+ " AND codeniveau = " + "'" + this.getCodeniveau() + "'");
		if (!orgs.isEmpty())
			business = orgs.get(0);
		if (business != null)
			resultat = ClsTreater._getResultat("La donnée % existe déja", "ERR-80020", true, this.getCodeniveau()+" -  "+this.getCodeemploitype());
		return resultat;
	}

	@SuppressWarnings("unchecked")
	public static List<EmploiTypeNiveauOrganigrammeVO> buildListOfDataToDisplay(GeneriqueConnexionService service, RechercheEmploiTypeNiveauOrganigrammeVO search)
	{
		List<EmploiTypeNiveauOrganigrammeVO> listOfDataToDisplay = new ArrayList<EmploiTypeNiveauOrganigrammeVO>();

		String queryString = getQueryString(service, search);

		try
		{
			Session session = service.getSession();
			Query q = session.createSQLQuery(queryString);

			EmploiTypeNiveauOrganigrammeVO.setQueryParameters(q, search);

			List<Object[]> liste = q.list();

			EmploiTypeNiveauOrganigrammeVO vo = null;

			for (Object[] o : liste)
			{
				vo = new EmploiTypeNiveauOrganigrammeVO();
				 
				 vo.setIdEntreprise(new Integer(search.cdos));
				 vo.setCodeniveau(search.codeniveau);
				 
				if (o[0] != null)
					vo.setCodeemploitype((String) o[0]);

				if (o[1] != null)
					vo.setLibelle((String) o[1]);
				
				if (o[2] != null)
					vo.setLibellecategorie((String) o[2]);
				
				if (o[3] != null)
					vo.setLibelleechelon((String) o[3]);
				
				if (o[4] != null)
					vo.setCat((String) o[4]);
				
				if (o[5] != null)
					vo.setEch((String) o[5]);


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

	private static String getQueryString(GeneriqueConnexionService service, RechercheEmploiTypeNiveauOrganigrammeVO search)
	{

		String queryString = "Select a.codeemploitype,d.libelle, b.vall as libellecategorie, c.vall as libelleechelon, d.cat, d.ech";
		queryString += " From Orgniveauemploitype a ";
		queryString += " Left join Orgposte d on a.identreprise=d.identreprise and a.codeemploitype=d.codeposte and d.fonc is null ";
		queryString += " Left join ParamData b on a.identreprise=b.identreprise and d.cat=b.cacc and b.nume = :nume1 and b.ctab = :ctab1";
		queryString += " Left join ParamData c on a.identreprise=c.identreprise and d.ech=c.cacc and c.nume = :nume1 and c.ctab = :ctab2";
		queryString += " Where a.identreprise = :cdos and a.codeniveau = :codeniveau ";

		if (StringUtils.isNotBlank(search.getCode()))
			queryString += "and UPPER(a.codeemploitype) like :codeemploitype ";

		if (StringUtils.isNotBlank(search.getLibelle()))
			queryString += "and UPPER(d.libelle) like :libelle ";

		queryString += " order by a.codeemploitype";

		return queryString;
	}

	private static void setQueryParameters(Query query, RechercheEmploiTypeNiveauOrganigrammeVO search)
	{
		query.setParameter("cdos", search.cdos, StandardBasicTypes.STRING);
		
		query.setParameter("codeniveau", search.codeniveau, StandardBasicTypes.STRING);
		
		
		query.setParameter("nume1",1, StandardBasicTypes.INTEGER);
		
		query.setParameter("ctab1", ClsNomenclature.CATEGORIE, StandardBasicTypes.STRING);
		
		query.setParameter("ctab2", ClsNomenclature.ECHELON, StandardBasicTypes.STRING);

		if (StringUtils.isNotBlank(search.code))
			query.setParameter("codeemploitype", "%" + search.code.toUpperCase() + "%", StandardBasicTypes.STRING);

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

	public String getLibellecategorie()
	{
		return libellecategorie;
	}

	public void setLibellecategorie(String libellecategorie)
	{
		this.libellecategorie = libellecategorie;
	}

	public String getLibelleechelon()
	{
		return libelleechelon;
	}

	public void setLibelleechelon(String libelleechelon)
	{
		this.libelleechelon = libelleechelon;
	}

	public String getCat()
	{
		return cat;
	}

	public void setCat(String cat)
	{
		this.cat = cat;
	}

	public String getEch()
	{
		return ech;
	}

	public void setEch(String ech)
	{
		this.ech = ech;
	}


}
