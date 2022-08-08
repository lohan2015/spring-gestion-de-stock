package com.kinart.organisation.business.vo;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.kinart.api.organisation.dto.ElementDto;
import com.kinart.organisation.business.model.Orgniveau;
import com.kinart.paie.business.model.ParamData;
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

public class NiveauOrganigrammeVO extends Orgniveau
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	NiveauOrganigrammeVO copie = null;

	public void creerCopie()
	{
		copie = new NiveauOrganigrammeVO();
		//RhpniveauPK copiePK = new RhpniveauPK();
		//BeanUtils.copyProperties(this.getComp_id(), copiePK);
		BeanUtils.copyProperties(this, copie);
		//copie.setComp_id(copiePK);
	}

	public void annulerModification()
	{
//		RhpniveauPK copiePK = new RhpniveauPK();
//		BeanUtils.copyProperties(copie.getComp_id(), copiePK);
		BeanUtils.copyProperties(copie, this);
		//this.setComp_id(copiePK);
		copie = null;
	}

	boolean selected = false;

	//Mode mode;

	private String libellecouleur;

	private String libelleouinon;

	private List<ElementDto> couleurs;

	private List<ElementDto> ouisnons;

	@SuppressWarnings("unused")
	private boolean hasbeenmodify = false;

	public NiveauOrganigrammeVO()
	{
		super();
		//this.setMode(Mode.CREATION);
	}

	public static NiveauOrganigrammeVO getBusinessObject(GeneriqueConnexionService service, String codeDossier, String code)
	{
		NiveauOrganigrammeVO vo = new NiveauOrganigrammeVO();

		Orgniveau business = null;//(Rhpniveau) service.get(Rhpniveau.class, new RhpniveauPK(codeDossier, code));
		List<Orgniveau> list = service.find("from Orgniveau where identreprise='"+codeDossier+"' And codeniveau='"+code+"'");
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

	public Orgniveau getEntityObject()
	{
		Orgniveau business = new Orgniveau();

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
		//RhpniveauPK pk = this.getComp_id();
		Orgniveau business = null;//(Rhpniveau) service.get(Rhpniveau.class, new RhpniveauPK(pk.getCdos(), pk.getCodeniveau()));
		List<Orgniveau> list = service.find("from Orgniveau where identreprise='"+this.getIdEntreprise()+"' And codeniveau='"+this.getCodeniveau()+"'");
		if (list != null && list.size() > 0)
		{
			business = list.get(0);
		}

		if (business != null)
			return ClsTreater._getResultat("La ligne existe deja", "INF-80432", true);
		else
			return null;
	}
	
	public ClsResultat checkDelete(HttpServletRequest request, GeneriqueConnexionService service)
	{
		//RhpniveauPK pk = this.getComp_id();
		String query = "Select count(*) From Organigramme Where identreprise='" + this.getIdEntreprise() + "' and codeniveau ='" + this.getCodeniveau() + "'";
		List ls = service.find(query);
		Integer nbr = Integer.parseInt(ls.get(0).toString());
		if(nbr > 0)
			return ClsTreater._getResultat("Des cases d'organigramme sont déja affectées au niveau %", "ERR-80143", true, this.getCodeniveau());
		else
			return null;
	}

	private ClsResultat saveAData(HttpServletRequest request, GeneriqueConnexionService service, boolean save, boolean delete)
	{
		ClsResultat resultat = null;
		Session sess = service.getSession();
		Transaction tx = null;
		
		String deleteETNiveau = null;

		Orgniveau business = this.getEntityObject();

		try
		{
			tx = sess.beginTransaction();
			if (delete)
			{
				sess.delete(business);
				//suppression des niveaux de l'emploi type
				deleteETNiveau = "Delete from Orgniveauemploitype where identreprise='"+business.getIdEntreprise()+"' and codeniveau ='"+business.getCodeniveau()+"'";
				sess.createQuery(deleteETNiveau).executeUpdate();
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

		//RhpniveauPK pk = this.getComp_id();

		if (StringUtils.isBlank(this.getCodeniveau()))
			_o_Result_End = ClsTreater._concat(_o_Result_End, ClsTreater._getResultat("Code", "INF-00034", true), sep);

		if (StringUtils.isBlank(this.getLibelle()))
			_o_Result_End = ClsTreater._concat(_o_Result_End, ClsTreater._getResultat("Libellé", "INF-00626", true), sep);

		if (_o_Result_End == null)
			return null;
		else
			return ClsTreater._concat(_o_Result_Start, _o_Result_End, sep);
	}
	
	public ClsResultat _checkInteger(HttpServletRequest request)
	{
		ClsResultat resultat = null;
		//RhpniveauPK pk = this.getComp_id();

//		if( ! NumberUtils.isInteger(pk.getCodeniveau()))
//			resultat = ClsTreater._getResultat("Le code du niveau doit étre un nombre", "INF-80578", true);
		
		return resultat;
	}

	public ClsResultat _checkExistance(HttpServletRequest request, GeneriqueConnexionService service)
	{
		ClsResultat resultat = null;
		//RhpniveauPK pk = this.getComp_id();
		Orgniveau business = null;//(Rhpniveau) service.get(Rhpniveau.class, new RhpniveauPK(pk.getCdos(), pk.getCodeniveau()));
		List<Orgniveau> list = service.find("from Orgniveau where identreprise='"+this.getIdEntreprise()+"' And codeniveau='"+this.getCodeniveau()+"'");
		if (list != null && list.size() > 0)
		{
			business = list.get(0);
		}

		if (business != null)
			resultat = ClsTreater._getResultat("La donnée % existe déja", "ERR-80020", true, this.getCodeniveau());
		return resultat;
	}

	@SuppressWarnings("unchecked")
	public static List<NiveauOrganigrammeVO> buildListOfDataToDisplay(GeneriqueConnexionService service, RechercheNiveauOrganigrammeVO search)
	{
		List<NiveauOrganigrammeVO> listOfDataToDisplay = new ArrayList<NiveauOrganigrammeVO>();

		String queryString = getQueryString(service, search);

		try
		{
			Session session = service.getSession();
			Query q = session.createSQLQuery(queryString).addEntity("a", Orgniveau.class).addScalar("libellecouleur", StandardBasicTypes.STRING).addScalar("libelleouinon", StandardBasicTypes.STRING);

			NiveauOrganigrammeVO.setQueryParameters(q, search);

			List<Object[]> liste = q.list();

			NiveauOrganigrammeVO vo = null;

			Orgniveau niveau;
			
			List<ElementDto> couleurs = new ArrayList<ElementDto>();
			List<ElementDto> ouisnons = new ArrayList<ElementDto>();

			List<ParamData> listColors = service.find("from ParamData where identreprise='"+search.cdos+"' And ctab=211 And nume=1");
			if (listColors != null && listColors.size() > 0)
			{
				for(int i=0; i<listColors.size(); i++){
					ParamData data = listColors.get(i);
					couleurs.add(new ElementDto(data.getCacc(), data.getVall()));
				}
			}

			List<ParamData> listOuiNon = service.find("from ParamData where identreprise='"+search.cdos+"' And ctab=210 And nume=1");
			if (listOuiNon != null && listOuiNon.size() > 0)
			{
				for(int i=0; i<listOuiNon.size(); i++){
					ParamData data = listOuiNon.get(i);
					couleurs.add(new ElementDto(data.getCacc(), data.getVall()));
				}

			}

			for (Object[] o : liste)
			{
				vo = new NiveauOrganigrammeVO();
				if (o[0] != null)
				{
					niveau = (Orgniveau) o[0];
					BeanUtils.copyProperties(niveau, vo);
				}

				if (o[1] != null)
					vo.setLibellecouleur((String) o[1]);

				if (o[2] != null)
					vo.setLibelleouinon((String) o[2]);
				
				vo.setCouleurs(couleurs);
				vo.setOuisnons(ouisnons);

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

	private static String getQueryString(GeneriqueConnexionService service, RechercheNiveauOrganigrammeVO search)
	{

		String queryString = "Select a.*, b.vall as libellecouleur, c.vall as libelleouinon";
		queryString += " From Orgniveau a ";
		queryString += " Left join ParamData b on a.identreprise=b.identreprise and a.codecouleur=b.cacc and b.nume = :nume1 and b.ctab = :ctab1";
		queryString += " Left join ParamData c on a.identreprise=c.identreprise and a.priseencomptecouleur=c.cacc and c.nume = :nume1 and c.ctab = :ctab2";
		queryString += " Where a.identreprise = :cdos ";

		if (StringUtils.isNotBlank(search.getCode()))
			queryString += "and UPPER(a.codeniveau) like :codeniveau ";

		if (StringUtils.isNotBlank(search.getLibelle()))
			queryString += "and UPPER(a.libelle) like :libelle ";

		queryString += " order by a.codeniveau";

		return queryString;
	}

	private static void setQueryParameters(Query query, RechercheNiveauOrganigrammeVO search)
	{
		query.setParameter("cdos", search.cdos, StandardBasicTypes.STRING);
		
		query.setParameter("nume1",1, StandardBasicTypes.INTEGER);
		
		query.setParameter("ctab1", 211, StandardBasicTypes.STRING);
		
		query.setParameter("ctab2",210, StandardBasicTypes.STRING);

		if (StringUtils.isNotBlank(search.code))
			query.setParameter("codeniveau", "%" + search.code + "%", StandardBasicTypes.STRING);

		if (StringUtils.isNotBlank(search.libelle))
			query.setParameter("libelle", "%" + search.libelle.toUpperCase() + "%", StandardBasicTypes.STRING);
	}
	
	public static List<ElementDto> getNiveaux(GeneriqueConnexionService service, String dossier, boolean showCode, boolean addNullEntry)
	{
		List<ElementDto> liste = new ArrayList<ElementDto>();
		if(addNullEntry)
			liste.add(new ElementDto("",""));
		List<Orgniveau> niveaux = service.find("From Orgniveau where identreprise='"+dossier+"' order by codeniveau");
		for (Orgniveau niv : niveaux)
		{
			if(showCode)
				liste.add(new ElementDto(niv.getCodeniveau(), niv.getCodeniveau()+" - "+niv.getLibelle()));
			else
				liste.add(new ElementDto(niv.getCodeniveau(), niv.getLibelle()));
		}
		return liste;
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

	public String getLibellecouleur()
	{
		return libellecouleur;
	}

	public void setLibellecouleur(String libellecouleur)
	{
		this.libellecouleur = libellecouleur;
	}

	public String getLibelleouinon()
	{
		return libelleouinon;
	}

	public void setLibelleouinon(String libelleouinon)
	{
		this.libelleouinon = libelleouinon;
	}

	public List<ElementDto> getCouleurs()
	{
		return couleurs;
	}

	public void setCouleurs(List<ElementDto> couleurs)
	{
		this.couleurs = couleurs;
	}

	public List<ElementDto> getOuisnons()
	{
		return ouisnons;
	}

	public void setOuisnons(List<ElementDto> ouisnons)
	{
		this.ouisnons = ouisnons;
	}

	public String getCouleur() {
		return libellecouleur;
	}

	public void setCouleur(String couleur) {
		this.libellecouleur = couleur;
	}

	public String getLibelleniveau() {
		return this.getLibelle();
	}

	public void setLibelleniveau(String libelleniveau) {
		this.setLibelle(libelleniveau);
	}
	
	

}
