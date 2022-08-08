/*
 * @Autor : Yannick Talla
 */
package com.kinart.organisation.business.vo;

import com.kinart.organisation.business.model.Organigramme;
import com.kinart.organisation.business.model.Orgniveau;
import com.kinart.organisation.business.services.ClsOrgCellsDesigner;
import com.kinart.organisation.business.services.ClsOrgListCellsDesigner;
import com.kinart.organisation.business.services.ICellsDesigner;
import com.kinart.paie.business.model.ParamData;
import com.kinart.paie.business.services.utils.ClsTreater;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.kinart.paie.business.services.utils.ParameterUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

// TODO: Auto-generated Javadoc
/**
 * The Class ClsParametreOrganigrammeVO.
 */
public class ClsParametreOrganigrammeVO
{

	public static String strDesaffectPoste;

	public static String STR_SPECIAL_CHAR = "V123V900F514Y";
	public static String STR_SPECIAL_CHAR_REPLACEMENT = "'";

	public static String LIENHAUT = "HAUT";

	public static String LIENBAS = "BAS";

	public static String STR_PHOTO_PATH = "photos/";

	public static String VUE_SANS_ACTIONS = "waction";

	public static String VUE_FONCTIONS_UNIQUEMENT = "function";

	public static String VUE_FONCTIONS_ET_NOM = "functionandname";
	
	public static String VUE_PHOTO_FONCTIONS_ET_NOM = "funcandnameandphoto";

	public static String VUE_TOUT = "all";

	public static final String REQUEST_CELLULE_DEPART = "codeorganigramme";

	public static final String REQUEST_NIVEAU_ARRIVEE = "codeniveau";

	public static final String REQUEST_SITE = "site";

	public static final String REQUEST_TYPE_VUE = "view";

	public static String REQUEST_TYPE_DIAGRAMME = "typeorg";

	public static String _STR_SAVE_FC = "save";

	public static String _STR_DELETE = "delete";

	public static String _STR_DELETE_ALL = "deleteall";

	public static String _STR_SITE = "site";

	public static String AFFICHER_PRESTATAIRES = "afficherprestataires";

	public static String ORGANIGRAMME_PRESTATAIRE = "computeorganigrammeprestataire";

	public static String COMPTER_UNIQUEMENT_CELLULES_AFFICHEES = "computeonlyprintedcellssize";

	public static String AFFICHER_LIGNES_FICTIVES = "afficherfictif";

	public static String DIAGRAMME_RATEAU = "RAKE";

	public static String DIAGRAMME_LISTE = "LISTE";
	
	public static String DIAGRAMME_RATEAU_NT = "RKNT";
	
	public static String DIAGRAMME_EXTRACTION = "EXTRACT";

	public static String TAILLE_BORDURE = "1";

	public static String COULEUR_CONTENU_CELLULE = "#CCECFF";

	public static String COULEUR_TRAIT = "0000FF";
	
	private boolean visible = false;
	private boolean traitementencours = false;

	/** The dossier. */
	private String dossier;

	/** The langue. */
	private String langue;

	/** The utilisateur. */
	private String utilisateur;

	private String formatDate;

	/** The cellule depart. */
	private String celluleDepart;
	
	private String libelleCelluleDepart;

	/** The site. */
	private String site;

	/** The niveau arrive. */
	private String niveauArrive;

	/** The type diagramme. */
	private String typeDiagramme;

	/** The affichage. */
	private String affichage;

	/** The taille bordure. */
	private String tailleBordure = ClsParametreOrganigrammeVO.TAILLE_BORDURE;

	/** The couleur contenu cellule. */
	private String couleurContenuCellule = ClsParametreOrganigrammeVO.COULEUR_CONTENU_CELLULE;

	/** The couleur trait. */
	private String couleurTrait = ClsParametreOrganigrammeVO.COULEUR_TRAIT;

	private String padding = "3";

	private String longueurLibelle = "17";

	/** The compter cellules affichees uniquement. */
	private boolean compterCellulesAfficheesUniquement = false;

	/** The organigramme prestataire. */
	private boolean organigrammePrestataire = false;

	/** The afficher postes prestataires. */
	private boolean afficherPostesPrestataires = false;

	/** The afficher lignes fictive. */
	private boolean afficherLignesFictive = false;
	
	private boolean afficherBoutonsSauvegarde = false;
	/** The sauvegarde. */
	private boolean sauvegarde = false;

	/** The supprimer. */
	private boolean supprimer = false;

	/** The supprimer tout. */
	private boolean supprimerTout = false;

	/** The afficher tous les fils. */
	private boolean afficherTousLesFils = false;

	private boolean iceface = false;
	
	public String racineImage= StringUtils.EMPTY;
	
	private boolean afficherechelon = true;
	
	private boolean activersecuriteservice = false;

	public static ClsParametreOrganigrammeVO getDefaultInstance(HttpServletRequest request, GeneriqueConnexionService service)
	{
		ClsParametreOrganigrammeVO param = new ClsParametreOrganigrammeVO();
		
		param.setDossier(ParameterUtil.getSessionObject(request, ParameterUtil.SESSION_DOSSIER));
		param.setLangue(ParameterUtil.getSessionObject(request, ParameterUtil.SESSION_LANGUE));
		param.setUtilisateur(ParameterUtil.getSessionObject(request, ParameterUtil.SESSION_LOGIN));
		param.setFormatDate(ParameterUtil.getSessionObject(request, ParameterUtil.SESSION_FORMAT_DATE));

		param.setCelluleDepart(ClsParametreOrganigrammeVO.__getDefaultCell(service, param.getDossier()));
		Organigramme org = ClsParametreOrganigrammeVO._getCelluleDepart(service, param.getDossier(), param.getCelluleDepart());
		if(org != null) param.setLibelleCelluleDepart(org.getLibelle());
		param.setNiveauArrive(ClsParametreOrganigrammeVO.__getDefaultLevel(service, param.getDossier(), param.getLangue()));

		ParamData data = null;
		String paramBorderCellSize = null;//ClsConfigurationParameters.getConfigParameterValue(service, param.getDossier(), param.getLangue(), ClsConfigurationParameters.FC_EP);
		List<ParamData> list = service.find("from ParamData " + " where identreprise =" + param.getDossier() + " and ctab=266 and nume=2 and cacc='EP_TRAIT'");
		if (list != null && list.size() > 0)
		{
			data = list.get(0);
			paramBorderCellSize = data.getVall();
		}
		param.setTailleBordure(paramBorderCellSize);

		String paramBorderCellColor = null;//ClsConfigurationParameters.getConfigParameterValue(service, param.getDossier(), param.getLangue(), ClsConfigurationParameters.COULEUR_TRAITS_ORGANIGRAMME);
		list = service.find("from ParamData " + " where identreprise =" + param.getDossier() + " and ctab=266 and nume=2 and cacc='FCLINCOLOR'");
		if (list != null && list.size() > 0)
		{
			data = list.get(0);
			paramBorderCellColor = data.getVall();
		}
		param.setCouleurTrait(paramBorderCellColor);

		String paramLongueurLibelle = null;//ClsConfigurationParameters.getConfigParameterValue(service, param.getDossier(), param.getLangue(), ClsConfigurationParameters.LONGUEUR_INTITULE_ORGANIGRAMME);
		list = service.find("from ParamData " + " where identreprise =" + param.getDossier() + " and ctab=266 and nume=2 and cacc='FCLENGTH'");
		if (list != null && list.size() > 0)
		{
			data = list.get(0);
			paramLongueurLibelle = data.getVall();
		}
		param.setLongueurLibelle(paramLongueurLibelle);

		String paramContentColor = null;//ClsConfigurationParameters.getConfigParameterValue(service, param.getDossier(), param.getLangue(), ClsConfigurationParameters.COULEUR_CELLULE_ORGANIGRAMME);
		list = service.find("from ParamData " + " where identreprise =" + param.getDossier() + " and ctab=266 and nume=2 and cacc='FCCOLOR'");
		if (list != null && list.size() > 0)
		{
			data = list.get(0);
			paramContentColor = data.getVall();
		}
		param.setCouleurContenuCellule(paramContentColor);
		
		String afficherBoutonsSauvegarde = null;//ClsConfigurationParameters.getConfigParameterValue(service, param.getDossier(), param.getLangue(), ClsConfigurationParameters.STR_SHOW_OP_FC);
		list = service.find("from ParamData " + " where identreprise =" + param.getDossier() + " and ctab=266 and nume=2 and cacc='SH_OP_FC'");
		if (list != null && list.size() > 0)
		{
			data = list.get(0);
			afficherBoutonsSauvegarde = data.getVall();
		}
		param.setAfficherBoutonsSauvegarde(StringUtils.equals("O", afficherBoutonsSauvegarde));
		
		String printEch = null;//ClsConfigurationParameters.getConfigParameterValue(service, param.getDossier(), param.getLangue(), ClsConfigurationParameters.AFFICHER_ECHELON_DANS_ORGANIGRAMME);
		list = service.find("from ParamData " + " where identreprise =" + param.getDossier() + " and ctab=266 and nume=2 and cacc='SHOW_ECH'");
		if (list != null && list.size() > 0)
		{
			data = list.get(0);
			printEch = data.getVall();
		}
		param.setAfficherechelon(StringUtils.equals("O", printEch));
		
		//printEch = ClsConfigurationParameters.getConfigParameterValue(service, param.getDossier(), param.getLangue(), ClsConfigurationParameters.ACTIVER_SECURITE_SERVICE_SUR_ORGANIGRAMME);
		list = service.find("from ParamData " + " where identreprise =" + param.getDossier() + " and ctab=266 and nume=2 and cacc='FC_SEC_SCE'");
		if (list != null && list.size() > 0)
		{
			data = list.get(0);
			printEch = data.getVall();
		}
		param.setActiversecuriteservice(StringUtils.equals("O", printEch));
		
		param.setTypeDiagramme(ClsParametreOrganigrammeVO.DIAGRAMME_RATEAU);
		
		return param;
	}
	public static ClsParametreOrganigrammeVO getDefaultInstance(String dossier,String login,String langue,String format_date,GeneriqueConnexionService service,int level){
		ClsParametreOrganigrammeVO param = new ClsParametreOrganigrammeVO();
		if(level<0){
			level=0;
		}
		param.setDossier(dossier);
		param.setLangue(langue);
		param.setUtilisateur(login);
		param.setFormatDate(format_date);

		List res=service.find("select org.codeorganigramme from Organigramme org,Salarie a,Utilisateur e where e.cuti='"+login+"' and e.identreprise='"+dossier+"'" +
				" and a.nmat=e.nmat and a.identreprise=e.identreprise and org.identreprise=a.identreprise and org.codeposte=a.codeposte");
		Object item=res==null||res.isEmpty()?null:res.get(0);
		if(!(item instanceof String)){
			return null;
		}
		//param.setCelluleDepart(ClsParametreOrganigrammeVO.__getDefaultCell(service, param.getDossier()));
		param.setCelluleDepart((String)item);
		res=service.find("select max(codeniveau) from Organigramme where codeorganigramme like '"+param.getCelluleDepart()+"'||'%' "+
				" and length(codeorganigramme)<=("+(level+1)+")*length('"+param.getCelluleDepart()+"') and identreprise='"+dossier+"'");
		item=res==null||res.isEmpty()?null:res.get(0);
		if(!(item instanceof String)){
			return null;
		}
		param.setNiveauArrive((String)item);
		//Organigramme org = ClsParametreOrganigrammeVO._getCelluleDepart(service, param.getDossier(), param.getCelluleDepart());
		Organigramme org = null;//(Organigramme)service.get(Organigramme.class, new OrganigrammePK(dossier,param.getCelluleDepart()));
		String query=" From Organigramme where codeorganigramme= '"+param.getCelluleDepart()+"' and identreprise = '"+dossier  +"'";
		List<Organigramme> fils = service.find(query);
		if(fils != null && fils.size()>0){
			org = fils.get(0);
			param.setLibelleCelluleDepart(org.getLibelle());
		}
		//param.setNiveauArrive(ClsParametreOrganigrammeVO.__getDefaultLevel(service, param.getDossier(), param.getLangue()));
		ParamData data = null;
		String paramBorderCellSize = null;//ClsConfigurationParameters.getConfigParameterValue(service, param.getDossier(), param.getLangue(), ClsConfigurationParameters.FC_EP);
		List<ParamData> list = service.find("from ParamData " + " where identreprise =" + param.getDossier() + " and ctab=266 and nume=2 and cacc='EP_TRAIT'");
		if (list != null && list.size() > 0)
		{
			data = list.get(0);
			paramBorderCellSize = data.getVall();
		}
		param.setTailleBordure(paramBorderCellSize);

		String paramBorderCellColor = null;//ClsConfigurationParameters.getConfigParameterValue(service, param.getDossier(), param.getLangue(), ClsConfigurationParameters.COULEUR_TRAITS_ORGANIGRAMME);
		list = service.find("from ParamData " + " where identreprise =" + param.getDossier() + " and ctab=266 and nume=2 and cacc='FCLINCOLOR'");
		if (list != null && list.size() > 0)
		{
			data = list.get(0);
			paramBorderCellColor = data.getVall();
		}
		param.setCouleurTrait(paramBorderCellColor);

		String paramLongueurLibelle = null;//ClsConfigurationParameters.getConfigParameterValue(service, param.getDossier(), param.getLangue(), ClsConfigurationParameters.LONGUEUR_INTITULE_ORGANIGRAMME);
		list = service.find("from ParamData " + " where identreprise =" + param.getDossier() + " and ctab=266 and nume=2 and cacc='FCLENGTH'");
		if (list != null && list.size() > 0)
		{
			data = list.get(0);
			paramLongueurLibelle = data.getVall();
		}
		param.setLongueurLibelle(paramLongueurLibelle);

		String paramContentColor = null;//ClsConfigurationParameters.getConfigParameterValue(service, param.getDossier(), param.getLangue(), ClsConfigurationParameters.COULEUR_CELLULE_ORGANIGRAMME);
		list = service.find("from ParamData " + " where identreprise =" + param.getDossier() + " and ctab=266 and nume=2 and cacc='FCCOLOR'");
		if (list != null && list.size() > 0)
		{
			data = list.get(0);
			paramContentColor = data.getVall();
		}
		param.setCouleurContenuCellule(paramContentColor);

		String afficherBoutonsSauvegarde = null;//ClsConfigurationParameters.getConfigParameterValue(service, param.getDossier(), param.getLangue(), ClsConfigurationParameters.STR_SHOW_OP_FC);
		list = service.find("from ParamData " + " where identreprise =" + param.getDossier() + " and ctab=266 and nume=2 and cacc='SH_OP_FC'");
		if (list != null && list.size() > 0)
		{
			data = list.get(0);
			afficherBoutonsSauvegarde = data.getVall();
		}
		param.setAfficherBoutonsSauvegarde(StringUtils.equals("O", afficherBoutonsSauvegarde));

		String printEch = null;//ClsConfigurationParameters.getConfigParameterValue(service, param.getDossier(), param.getLangue(), ClsConfigurationParameters.AFFICHER_ECHELON_DANS_ORGANIGRAMME);
		list = service.find("from ParamData " + " where identreprise =" + param.getDossier() + " and ctab=266 and nume=2 and cacc='SHOW_ECH'");
		if (list != null && list.size() > 0)
		{
			data = list.get(0);
			printEch = data.getVall();
		}
		param.setAfficherechelon(StringUtils.equals("O", printEch));

		//printEch = ClsConfigurationParameters.getConfigParameterValue(service, param.getDossier(), param.getLangue(), ClsConfigurationParameters.ACTIVER_SECURITE_SERVICE_SUR_ORGANIGRAMME);
		list = service.find("from ParamData " + " where identreprise =" + param.getDossier() + " and ctab=266 and nume=2 and cacc='FC_SEC_SCE'");
		if (list != null && list.size() > 0)
		{
			data = list.get(0);
			printEch = data.getVall();
		}
		param.setActiversecuriteservice(StringUtils.equals("O", printEch));

		param.setTypeDiagramme(ClsParametreOrganigrammeVO.DIAGRAMME_RATEAU);
		
		param.setAffichage(ClsParametreOrganigrammeVO.VUE_FONCTIONS_ET_NOM);
		
		return param;
	}
	
	public static String dessinerOrganigramme(HttpServletRequest request, String dossier, String login, String langue, String format_date, GeneriqueConnexionService service, int level){
		String org="";
		ClsParametreOrganigrammeVO param=ClsParametreOrganigrammeVO.getDefaultInstance(dossier, login, langue, format_date, service, level);
		if(param!=null){
			ICellsDesigner paintre = null;
			
			ClsMessageCelluleVO message=new ClsMessageCelluleVO(null);
			
			if (StringUtils.equalsIgnoreCase(param.getTypeDiagramme(), ClsParametreOrganigrammeVO.DIAGRAMME_LISTE)){
				List<ParamData> list = service.find("from ParamData " + " where identreprise =" + param.getDossier() + " and ctab=266 and nume=2 and cacc='NFILSTOTAL'");
				if (list != null && list.size() > 0)
				{
					ParamData data = list.get(0);
					//paramBorderCellSize = data.getVall();
					param.setAfficherTousLesFils(StringUtils.equals("O", data.getVall()));
				}

			}

			//ClsUserAccess useraccess = IceFaceUIGenericTable._setAccesParameter(request);
			
			if(StringUtils.equals(ClsParametreOrganigrammeVO.DIAGRAMME_RATEAU, param.getTypeDiagramme()))
				paintre = new ClsOrgCellsDesigner(service, param, message);
			else 
				paintre = new ClsOrgListCellsDesigner(service, param, message);

			org = paintre._getFlowChart();
			
			if (StringUtils.equalsIgnoreCase(param.getTypeDiagramme(), ClsParametreOrganigrammeVO.DIAGRAMME_RATEAU))
				org = StringUtils.replace(org, ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR, ClsParametreOrganigrammeVO. STR_SPECIAL_CHAR_REPLACEMENT);
		}
		return org;
	}
	private static Organigramme _getCelluleDepart(GeneriqueConnexionService service, String dossier, String code)
	{
		//Organigramme org = (Organigramme) service.get(Organigramme.class, new OrganigrammePK(dossier, code));
		List<Organigramme> orgs = service.find("FROM Organigramme WHERE identreprise = " + "'" + dossier + "'" + " AND codeorganigramme = " + "'" + code + "'");

		if (!orgs.isEmpty())
			return orgs.get(0);
		return null;
	}

	/**
	 *            : la langue de connexion
	 * @return le code niveau par d�faut, soit celui en table 266 avec pour cl� DEFT_LV , soit la premi�re valeur tir�e de Rhpniveau
	 */
	@SuppressWarnings("unchecked")
	public static String __getDefaultLevel(GeneriqueConnexionService service, String dossier, String langue)
	{

		String strNbreNiveau = "2";

		try
		{
			String strDefaultLevel = null;//ClsConfigurationParameters.getConfigParameterValue(service, dossier, langue, ClsConfigurationParameters.STR_DEFAULT_LV);
			ParamData data = null;
			List<ParamData> list = service.find("from ParamData " + " where identreprise =" + dossier + " and ctab=266 and nume=2 and cacc='DEFT_LV'");
			if (list != null && list.size() > 0)
			{
				data = list.get(0);
				strDefaultLevel = data.getVall();
			}


			if (StringUtils.isNotBlank(strDefaultLevel))
				strNbreNiveau = strDefaultLevel;
			else
			{
				String _strQuery = "FROM Orgniveau where identreprise='" + dossier + "' ORDER BY codeniveau ASC";
				List<Orgniveau> niveaux = service.find(_strQuery);
				if (!niveaux.isEmpty())
					strNbreNiveau = niveaux.get(0).getCodeniveau();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			ParameterUtil.__invokeGarbageCollection();
		}

		return strNbreNiveau;
	}

	/**
	 * @param dossier
	 *            : le code du dossier
	 * @return le code de la cellule par d�faut, premi�re cellule dont le code du p�re vaut 00
	 */
	@SuppressWarnings("unchecked")
	public static String __getDefaultCell(GeneriqueConnexionService service, String dossier)
	{

		String strDefaultStartCell = "02";

		List<Organigramme> orgs = service.find("FROM Organigramme WHERE identreprise = " + "'" + dossier + "'" + " AND codepere = " + "'" + ParameterUtil.STR_INIT_CODEPERE + "'");

		if (!orgs.isEmpty())
			strDefaultStartCell = orgs.get(0).getCodeorganigramme();

		return strDefaultStartCell;
	}

	@SuppressWarnings("unchecked")
	public static String __getLegendeOrganigramme(HttpServletRequest request, GeneriqueConnexionService service, String dossier)
	{
		String strLegend = null;
		try
		{
			List<Orgniveau> oLevelCollection = service.find("FROM Orgniveau WHERE priseencomptecouleur = 'O' AND identreprise = " + "'" + dossier + "'");
			if (oLevelCollection != null && oLevelCollection.size() > 0)
			{

				String strLegende = ClsTreater._getResultat("Legende", "INF-80161", false).getLibelle();

				strLegend = "<table style='border-bottom-width: 2px; border-right-width: 2px; border-left-width: 2px; border-top-width: 2px' width='100%'  bgcolor='#D9FFFF'>";
				strLegend += "<tr><td nowrap='nowrap'><span style='color: #6e6e6e; font-family: Verdana; font-weight:bold; font-size: 9px;'>"
						+ strLegende
						+ "----------------------------------------------------------------------------------------------------------------------------------------------------------</span></td></tr></table>";

				strLegend += ClsTemplate.STR_FLOWCHART_LEG_HEAD_START;

				for (Orgniveau oLevel : oLevelCollection)
				{
					strLegend += ClsTemplate._getFlowChartLegElement(oLevel.getCodecouleur(), oLevel.getLibelle());
				}

				strLegend += ClsTemplate._getFlowChartLegElement("#0033FF", "Par d�faut");

				strLegend += ClsTemplate.STR_FLOWCHART_LEG_HEAD_CLOSE;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return strLegend;
	}

	/**
	 * Gets the cellule depart.
	 * 
	 * @return the cellule depart
	 */
	public String getCelluleDepart()
	{
		return celluleDepart;
	}

	/**
	 * Sets the cellule depart.
	 * 
	 * @param celluleDepart
	 *            the new cellule depart
	 */
	public void setCelluleDepart(String celluleDepart)
	{
		this.celluleDepart = celluleDepart;
	}

	/**
	 * Gets the site.
	 * 
	 * @return the site
	 */
	public String getSite()
	{
		return site;
	}

	/**
	 * Sets the site.
	 * 
	 * @param site
	 *            the new site
	 */
	public void setSite(String site)
	{
		this.site = site;
	}

	/**
	 * Gets the niveau arrive.
	 * 
	 * @return the niveau arrive
	 */
	public String getNiveauArrive()
	{
		return niveauArrive;
	}

	/**
	 * Sets the niveau arrive.
	 * 
	 * @param niveauArrive
	 *            the new niveau arrive
	 */
	public void setNiveauArrive(String niveauArrive)
	{
		this.niveauArrive = niveauArrive;
	}

	/**
	 * Gets the type diagramme.
	 * 
	 * @return the type diagramme
	 */
	public String getTypeDiagramme()
	{
		return StringUtils.isNotBlank(typeDiagramme) ? typeDiagramme : ClsParametreOrganigrammeVO.DIAGRAMME_RATEAU;
	}

	/**
	 * Sets the type diagramme.
	 * 
	 * @param typeDiagramme
	 *            the new type diagramme
	 */
	public void setTypeDiagramme(String typeDiagramme)
	{
		this.typeDiagramme = typeDiagramme;
	}

	/**
	 * Gets the affichage.
	 * 
	 * @return the affichage
	 */
	public String getAffichage()
	{
		return StringUtils.isNotBlank(affichage) ? affichage : ClsParametreOrganigrammeVO.VUE_TOUT;
	}

	/**
	 * Sets the affichage.
	 * 
	 * @param affichage
	 *            the new affichage
	 */
	public void setAffichage(String affichage)
	{
		this.affichage = affichage;
	}

	/**
	 * Checks if is compter cellules affichees uniquement.
	 * 
	 * @return true, if is compter cellules affichees uniquement
	 */
	public boolean isCompterCellulesAfficheesUniquement()
	{
		return compterCellulesAfficheesUniquement;
	}

	/**
	 * Sets the compter cellules affichees uniquement.
	 * 
	 * @param compterCellulesAfficheesUniquement
	 *            the new compter cellules affichees uniquement
	 */
	public void setCompterCellulesAfficheesUniquement(boolean compterCellulesAfficheesUniquement)
	{
		this.compterCellulesAfficheesUniquement = compterCellulesAfficheesUniquement;
	}

	/**
	 * Checks if is organigramme prestataire.
	 * 
	 * @return true, if is organigramme prestataire
	 */
	public boolean isOrganigrammePrestataire()
	{
		return organigrammePrestataire;
	}

	/**
	 * Sets the organigramme prestataire.
	 * 
	 * @param organigrammePrestataire
	 *            the new organigramme prestataire
	 */
	public void setOrganigrammePrestataire(boolean organigrammePrestataire)
	{
		this.organigrammePrestataire = organigrammePrestataire;
	}

	/**
	 * Checks if is afficher postes prestataires.
	 * 
	 * @return true, if is afficher postes prestataires
	 */
	public boolean isAfficherPostesPrestataires()
	{
		return afficherPostesPrestataires;
	}

	/**
	 * Sets the afficher postes prestataires.
	 * 
	 * @param afficherPostesPrestataires
	 *            the new afficher postes prestataires
	 */
	public void setAfficherPostesPrestataires(boolean afficherPostesPrestataires)
	{
		this.afficherPostesPrestataires = afficherPostesPrestataires;
	}

	/**
	 * Checks if is afficher lignes fictive.
	 * 
	 * @return true, if is afficher lignes fictive
	 */
	public boolean isAfficherLignesFictive()
	{
		return afficherLignesFictive;
	}

	/**
	 * Sets the afficher lignes fictive.
	 * 
	 * @param afficherLignesFictive
	 *            the new afficher lignes fictive
	 */
	public void setAfficherLignesFictive(boolean afficherLignesFictive)
	{
		this.afficherLignesFictive = afficherLignesFictive;
	}

	/**
	 * Checks if is sauvegarde.
	 * 
	 * @return true, if is sauvegarde
	 */
	public boolean isSauvegarde()
	{
		return sauvegarde;
	}

	/**
	 * Sets the sauvegarde.
	 * 
	 * @param sauvegarde
	 *            the new sauvegarde
	 */
	public void setSauvegarde(boolean sauvegarde)
	{
		this.sauvegarde = sauvegarde;
	}

	/**
	 * Checks if is supprimer.
	 * 
	 * @return true, if is supprimer
	 */
	public boolean isSupprimer()
	{
		return supprimer;
	}

	/**
	 * Sets the supprimer.
	 * 
	 * @param supprimer
	 *            the new supprimer
	 */
	public void setSupprimer(boolean supprimer)
	{
		this.supprimer = supprimer;
	}

	/**
	 * Checks if is supprimer tout.
	 * 
	 * @return true, if is supprimer tout
	 */
	public boolean isSupprimerTout()
	{
		return supprimerTout;
	}

	/**
	 * Sets the supprimer tout.
	 * 
	 * @param supprimerTout
	 *            the new supprimer tout
	 */
	public void setSupprimerTout(boolean supprimerTout)
	{
		this.supprimerTout = supprimerTout;
	}

	/**
	 * Gets the dossier.
	 * 
	 * @return the dossier
	 */
	public String getDossier()
	{
		return dossier;
	}

	/**
	 * Sets the dossier.
	 * 
	 * @param dossier
	 *            the new dossier
	 */
	public void setDossier(String dossier)
	{
		this.dossier = dossier;
	}

	/**
	 * Gets the langue.
	 * 
	 * @return the langue
	 */
	public String getLangue()
	{
		return langue;
	}

	/**
	 * Sets the langue.
	 * 
	 * @param langue
	 *            the new langue
	 */
	public void setLangue(String langue)
	{
		this.langue = langue;
	}

	/**
	 * Gets the utilisateur.
	 * 
	 * @return the utilisateur
	 */
	public String getUtilisateur()
	{
		return utilisateur;
	}

	/**
	 * Sets the utilisateur.
	 * 
	 * @param utilisateur
	 *            the new utilisateur
	 */
	public void setUtilisateur(String utilisateur)
	{
		this.utilisateur = utilisateur;
	}

	/**
	 * Gets the taille bordure.
	 * 
	 * @return the taille bordure
	 */
	public String getTailleBordure()
	{
		return StringUtils.isNotBlank(tailleBordure) ? tailleBordure : ClsParametreOrganigrammeVO.TAILLE_BORDURE;
	}

	/**
	 * Sets the taille bordure.
	 * 
	 * @param tailleBordure
	 *            the new taille bordure
	 */
	public void setTailleBordure(String tailleBordure)
	{
		this.tailleBordure = tailleBordure;
	}

	/**
	 * Checks if is afficher tous les fils.
	 * 
	 * @return true, if is afficher tous les fils
	 */
	public boolean isAfficherTousLesFils()
	{
		return afficherTousLesFils;
	}

	/**
	 * Sets the afficher tous les fils.
	 * 
	 * @param afficherTousLesFils
	 *            the new afficher tous les fils
	 */
	public void setAfficherTousLesFils(boolean afficherTousLesFils)
	{
		this.afficherTousLesFils = afficherTousLesFils;
	}

	/**
	 * Gets the couleur contenu cellule.
	 * 
	 * @return the couleur contenu cellule
	 */
	public String getCouleurContenuCellule()
	{
		return StringUtils.isNotBlank(couleurContenuCellule) ? couleurContenuCellule : ClsParametreOrganigrammeVO.COULEUR_CONTENU_CELLULE;
	}

	/**
	 * Sets the couleur contenu cellule.
	 * 
	 * @param couleurContenuCellule
	 *            the new couleur contenu cellule
	 */
	public void setCouleurContenuCellule(String couleurContenuCellule)
	{
		this.couleurContenuCellule = couleurContenuCellule;
	}

	/**
	 * Gets the couleur trait.
	 * 
	 * @return the couleur trait
	 */
	public String getCouleurTrait()
	{
		return StringUtils.isNotBlank(couleurTrait) ? couleurTrait : ClsParametreOrganigrammeVO.COULEUR_TRAIT;
	}

	/**
	 * Sets the couleur trait.
	 * 
	 * @param couleurTrait
	 *            the new couleur trait
	 */
	public void setCouleurTrait(String couleurTrait)
	{
		this.couleurTrait = couleurTrait;
	}

	public String getFormatDate()
	{
		return formatDate;
	}

	public void setFormatDate(String formatDate)
	{
		this.formatDate = formatDate;
	}

	public String getPadding()
	{
		return padding;
	}

	public void setPadding(String padding)
	{
		this.padding = padding;
	}

	public String getLongueurLibelle()
	{
		return longueurLibelle;
	}

	public void setLongueurLibelle(String longueurLibelle)
	{
		this.longueurLibelle = longueurLibelle;
	}

	public boolean isIceface()
	{
		return iceface;
	}

	public void setIceface(boolean iceface)
	{
		this.iceface = iceface;
		if(this.iceface)
			racineImage="../../";
		else
			racineImage=StringUtils.EMPTY;
	}

	public String getLibelleCelluleDepart()
	{
		return libelleCelluleDepart;
	}

	public void setLibelleCelluleDepart(String libelleCelluleDepart)
	{
		this.libelleCelluleDepart = libelleCelluleDepart;
	}

	public boolean isAfficherBoutonsSauvegarde()
	{
		return afficherBoutonsSauvegarde;
	}

	public void setAfficherBoutonsSauvegarde(boolean afficherBoutonsSauvegarde)
	{
		this.afficherBoutonsSauvegarde = afficherBoutonsSauvegarde;
	}

	public boolean isVisible()
	{
		return visible;
	}

	public void setVisible(boolean visible)
	{
		this.visible = visible;
	}

	public boolean isTraitementencours()
	{
		return traitementencours;
	}

	public void setTraitementencours(boolean traitementencours)
	{
		this.traitementencours = traitementencours;
	}

	public boolean isAfficherechelon()
	{
		return afficherechelon;
	}

	public void setAfficherechelon(boolean afficherechelon)
	{
		this.afficherechelon = afficherechelon;
	}

	public boolean isActiversecuriteservice()
	{
		return activersecuriteservice;
	}

	public void setActiversecuriteservice(boolean activersecuriteservice)
	{
		this.activersecuriteservice = activersecuriteservice;
	}
	
}
