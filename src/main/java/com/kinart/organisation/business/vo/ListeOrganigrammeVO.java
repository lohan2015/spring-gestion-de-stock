package com.kinart.organisation.business.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.kinart.api.organisation.dto.RechercheListeOrganigrammeDto;
import com.kinart.organisation.business.model.Organigramme;
import com.kinart.paie.business.services.cloture.ClsNomenclature;
import com.kinart.paie.business.services.utils.ClsResultat;
import com.kinart.paie.business.services.utils.ClsTreater;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.kinart.paie.business.services.utils.TypeBDUtil;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;

public class ListeOrganigrammeVO extends Organigramme
{
	private static final long serialVersionUID = 1L;

	boolean selected = false;

	//Mode mode;

	@SuppressWarnings("unused")
	private boolean hasbeenmodify = false;

	private String libelleorganigramme;

	private String libelleniveau;

	private String libellesite;

	private String nmat;

	private String nomagent;

	private int nombrefils = 0;
	
	private int nombrefilstotal = 0;

	public ListeOrganigrammeVO()
	{
		super();
		//this.setMode(Mode.CREATION);
	}

	public static ListeOrganigrammeVO getBusinessObject(GeneriqueConnexionService service, String codeDossier, String codeOrganigramme)
	{
		ListeOrganigrammeVO vo = new ListeOrganigrammeVO();

		Organigramme emploi = null;//(Organigramme) service.get(Organigramme.class, new OrganigrammePK(codeDossier, codeOrganigramme));
		List<Organigramme> list = service.find("from Organigramme where identreprise='"+codeDossier+"' And codeorganigramme='"+codeOrganigramme+"'");
		if (list != null && list.size() > 0)
		{
			emploi = list.get(0);
		}
		if (emploi != null)
		{
			BeanUtils.copyProperties(emploi, vo);
			
			vo.setLibelleorganigramme(vo.getLibelle());

			//vo.setMode(Mode.MODIFICATION);
		}
		return vo;
	}

	public Organigramme getEntityObject()
	{
		Organigramme org = new Organigramme();

		BeanUtils.copyProperties(this, org);

		return org;
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
//		OrganigrammePK pk = this.getComp_id();
		Organigramme emploi = null;//(Organigramme) service.get(Organigramme.class, new OrganigrammePK(pk.getCdos(), pk.getCodeorganigramme()));
		List<Organigramme> list = service.find("from Organigramme where identreprise='"+this.getIdEntreprise()+"' And codeorganigramme='"+this.getCodeorganigramme()+"'");
		if (list != null && list.size() > 0)
		{
			emploi = list.get(0);
		}
		if (emploi != null)
			return ClsTreater._getResultat("La ligne existe deja", "INF-80432", true);
		else
			return null;
	}

	private ClsResultat saveAData(HttpServletRequest request, GeneriqueConnexionService service, boolean save, boolean delete)
	{
		ClsResultat resultat = null;
		Session sess = service.getSession();
		Transaction tx = null;

		Organigramme emploi = this.getEntityObject();

		try
		{
			tx = sess.beginTransaction();
			if (delete)
			{
				sess.delete(emploi);
				String strSQLFils="Delete From Organigramme  where codepere = " + "'" + emploi.getCodeorganigramme() + "'" + " and identreprise=" +"'" + emploi.getIdEntreprise() +"'";
			    String strSQLPrestataires="Delete From Organigrammeassocie  where codeorganigramme like '" + emploi.getCodeorganigramme() + "%'" + " and cdos=" +"'" + emploi.getIdEntreprise() +"'";
			    sess.createSQLQuery(strSQLFils).executeUpdate();
			    sess.createSQLQuery(strSQLPrestataires).executeUpdate();
			}
			else
			{
				if (save)
				{
					sess.save(emploi);
				}
				else
					sess.update(emploi);
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

	@SuppressWarnings("unchecked")
	public static List<ListeOrganigrammeVO> buildListOfDataToDisplay(GeneriqueConnexionService service, RechercheListeOrganigrammeDto search, boolean showChildNumber)
	{
		List<ListeOrganigrammeVO> listOfDataToDisplay = new ArrayList<ListeOrganigrammeVO>();
		
		String sites = ListeOrganigrammeVO._getSites(service, search);
		
		String queryString = getQueryString(search, sites);

		try
		{
			Session session = service.getSession();

			Query q = session.createSQLQuery(queryString).addEntity("a", Organigramme.class).addScalar("libelleniveau", StandardBasicTypes.STRING).addScalar("libellesite", StandardBasicTypes.STRING).addScalar("nmat", StandardBasicTypes.STRING).addScalar("nomagent",
					StandardBasicTypes.STRING).addScalar("prenomagent", StandardBasicTypes.STRING);

			ListeOrganigrammeVO.setQueryParameters(q, search);

			List<Object[]> liste = q.list();
			
			service.closeSession(session);

			ListeOrganigrammeVO vo = null;

			Organigramme org;

			Map<String, Integer> map = null;
			Map<String, Integer> maptotal = null;
			if (showChildNumber)
			{
				map = ListeOrganigrammeVO.buildMapOfChildNumber(service, search);
				maptotal = ListeOrganigrammeVO.buildMapOfTotalChildNumber(service, search);
			}
			for (Object[] o : liste)
			{
				vo = new ListeOrganigrammeVO();
				vo.setNomagent(StringUtils.EMPTY);
				if (o[0] != null)
				{
					org = (Organigramme) o[0];
					BeanUtils.copyProperties(org, vo);
				}

				if (o[1] != null)
					vo.setLibelleniveau((String) o[1]);

				if (o[2] != null)
					vo.setLibellesite((String) o[2]);

				if (o[3] != null)
					vo.setNmat((String) o[3]);

				if (o[4] != null)
					vo.setNomagent((String) o[4]);

				if (o[5] != null)
					vo.setNomagent(vo.getNomagent() + " " + (String) o[5]);

				if (showChildNumber)
				{
					vo.setNombrefils(map.get(vo.getCodeorganigramme()));
					vo.setNombrefilstotal(maptotal.get(vo.getCodeorganigramme()));
				}
				
				vo.setLibelleorganigramme(vo.getLibelle());

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

	@SuppressWarnings("unchecked")
	private static Map<String, Integer> buildMapOfChildNumber(GeneriqueConnexionService service, RechercheListeOrganigrammeDto search)
	{
		Map<String, Integer> map = new HashMap<String, Integer>();

		String queryString = getCountChildQueryString(search);
		
		Session session = null;

		try
		{
			session = service.getSession();

			Query q = session.createSQLQuery(queryString).addScalar("nombrefils", StandardBasicTypes.INTEGER).addScalar("codeorg", StandardBasicTypes.STRING);

			ListeOrganigrammeVO.setWhereParQueryParameters(q, search);

			List<Object[]> liste = q.list();
			for (Object[] obj : liste)
			{
				map.put((String) obj[1], Integer.parseInt((obj[0].toString())));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			service.closeSession(session);
		}
		return map;
	}
	
	private static Map<String, Integer> buildMapOfTotalChildNumber(GeneriqueConnexionService service, RechercheListeOrganigrammeDto search)
	{
		Map<String, Integer> map = new HashMap<String, Integer>();

		String queryString = getCountTotalChildQueryString(search);
		
		Session session = null;

		try
		{
			session = service.getSession();

			Query q = session.createSQLQuery(queryString).addScalar("nombrefils", StandardBasicTypes.INTEGER).addScalar("codeorg", StandardBasicTypes.STRING);

			ListeOrganigrammeVO.setWhereParQueryParameters(q, search);

			List<Object[]> liste = q.list();
			service.closeSession(session);
			
			for (Object[] obj : liste)
			{
				map.put((String) obj[1], Integer.parseInt((obj[0].toString())));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			service.closeSession(session);
		}
		return map;
	}

	private static String getQueryString(RechercheListeOrganigrammeDto search, String sites)
	{

		String queryString = "Select a.*, b.libelle as libelleniveau, c.vall as libellesite, d.nmat as nmat, d.nom as nomagent, d.pren as prenomagent From Organigramme a ";
		queryString += " Left join Rhpniveau b on a.cdos=b.cdos and a.codeniveau=b.codeniveau";
		queryString += " Left join Rhfnom c on a.cdos=c.cdos and a.codesite=c.cacc and c.nume =:nume1 and c.ctab = :ctab1";
		queryString += " Left join Rhpagent d on a.cdos=d.cdos and a.codeposte=d.codeposte";
		queryString += " Where a.cdos = :cdos ";
		
		if(StringUtils.isNotBlank(sites))
			queryString+=" and "+sites;

		queryString = getWherePart(search, queryString, false);

		return queryString;
	}

	private static void setQueryParameters(Query query, RechercheListeOrganigrammeDto search)
	{
		query.setParameter("nume1", 1, StandardBasicTypes.INTEGER);

		query.setParameter("ctab1", ClsNomenclature.SITE_TRAVAIL, StandardBasicTypes.STRING);

		setWhereParQueryParameters(query, search);
	}

	private static String getWherePart(RechercheListeOrganigrammeDto search, String queryString, boolean addGroupBy)
	{
		if (StringUtils.isNotBlank(search.code))
			queryString += " and UPPER(a.codeorganigramme) like :code ";

		if (StringUtils.isNotBlank(search.libelle))
			queryString += " and UPPER(a.libelle) like :libelle ";

		if (StringUtils.isNotBlank(search.niveau))
			queryString += " and UPPER(a.codeniveau)  = :niveau ";

		if (StringUtils.isNotBlank(search.site))
			queryString += " and UPPER(a.codesite)  = :site ";

		if (StringUtils.isNotBlank(search.fictive))
			queryString += " and UPPER(a.bcasefictive)  = :fictive ";

		if (StringUtils.isNotBlank(search.prestataire))
			queryString += " and UPPER(a.bprestataire)  = :prestataire ";

		if (addGroupBy)
			queryString += " group by a.codeorganigramme";

		queryString += " order by a.codeorganigramme";

		return queryString;
	}

	private static void setWhereParQueryParameters(Query query, RechercheListeOrganigrammeDto search)
	{
		query.setParameter("cdos", search.cdos, StandardBasicTypes.STRING);

//		if (StringUtils.isNotBlank(search.code))
//			query.setParameter("code", "%" + search.code.toUpperCase() + "%", StandardBasicTypes.STRING);
		if (StringUtils.isNotBlank(search.code))
			query.setParameter("code", search.code.toUpperCase() + "%", StandardBasicTypes.STRING);

		if (StringUtils.isNotBlank(search.libelle))
			query.setParameter("libelle", "%" + search.libelle.toUpperCase() + "%", StandardBasicTypes.STRING);

		if (StringUtils.isNotBlank(search.niveau))
			query.setParameter("niveau", search.niveau.toUpperCase(), StandardBasicTypes.STRING);

		if (StringUtils.isNotBlank(search.site))
			query.setParameter("site", search.site.toUpperCase(), StandardBasicTypes.STRING);

		if (StringUtils.isNotBlank(search.fictive))
			query.setParameter("fictive", search.fictive.toUpperCase(), StandardBasicTypes.STRING);

		if (StringUtils.isNotBlank(search.prestataire))
			query.setParameter("prestataire", search.prestataire.toUpperCase(), StandardBasicTypes.STRING);
	}

	private static String getCountChildQueryString(RechercheListeOrganigrammeDto search)
	{

		String queryString = "Select count(b.codeorganigramme) as nombrefils, a.codeorganigramme as codeorg From Organigramme a ";
		queryString += " Left join Organigramme b on a.cdos=b.cdos and b.codepere = a.codeorganigramme ";
		queryString += " Where a.cdos = :cdos ";

		queryString = getWherePart(search, queryString, true);

		return queryString;
	}
	
	private static String getCountTotalChildQueryString(RechercheListeOrganigrammeDto search)
	{

		String queryString = "Select count(b.codeorganigramme) as nombrefils, a.codeorganigramme as codeorg From Organigramme a ";
		queryString += " Left join Organigramme b on a.identreprise=b.identreprise and b.codepere like "+ TypeBDUtil.contatener("a.codeorganigramme","'%'");
		queryString += " Where a.identreprise = :cdos ";
		

		queryString = getWherePart(search, queryString, true);

		return queryString;
	}
	
	private static String _getSites(GeneriqueConnexionService service, RechercheListeOrganigrammeDto search)
	{
		//ClsAutorisationsUtilisateur auto = new ClsAutorisationsUtilisateur(search.cdos, search.cuti, "a", ClsTypeProfilAgent.COLONNES, service);

		//auto.setUseHibernate(false);

		String chaineauto = null;//auto.getSites(null);

		return chaineauto;
	}

	public boolean isSelected()
	{
		return selected;
	}

	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}
//
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

	public String getLibelleniveau()
	{
		return libelleniveau;
	}

	public void setLibelleniveau(String libelleniveau)
	{
		this.libelleniveau = libelleniveau;
	}

	public String getLibellesite()
	{
		return libellesite;
	}

	public void setLibellesite(String libellesite)
	{
		this.libellesite = libellesite;
	}

	public String getNmat()
	{
		return nmat;
	}

	public void setNmat(String nmat)
	{
		this.nmat = nmat;
	}

	public String getNomagent()
	{
		return nomagent;
	}

	public void setNomagent(String nomagent)
	{
		this.nomagent = nomagent;
	}

	public int getNombrefils()
	{
		return nombrefils;
	}

	public void setNombrefils(int nombrefils)
	{
		this.nombrefils = nombrefils;
	}

	public int getNombrefilstotal()
	{
		return nombrefilstotal;
	}

	public void setNombrefilstotal(int nombrefilstotal)
	{
		this.nombrefilstotal = nombrefilstotal;
	}

	public String getLibelleorganigramme()
	{
		return libelleorganigramme;
	}

	public void setLibelleorganigramme(String libelleorganigramme)
	{
		this.libelleorganigramme = libelleorganigramme;
	}

}
