package com.kinart.organisation.business.services;

import java.util.List;

import com.kinart.organisation.business.vo.ClsLienOrganigrammeVO;
import com.kinart.organisation.business.vo.ClsMessageCelluleVO;
import com.kinart.organisation.business.vo.ClsParametreOrganigrammeVO;
import com.kinart.paie.business.model.ParamData;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.kinart.paie.business.services.utils.ParameterUtil;
import com.mchange.v1.util.StringTokenizerUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Classe permettant de dessiner l'organigramme graphique
 * 
 * @author c.mbassi
 * @version 1.0
 */
public class ClsOrgCellsDesigner implements ICellsDesigner
{
	private GeneriqueConnexionService service;
	
	//private ClsUserAccess useraccess;

	private List<ClsOrgCellule> allCellules;

	private String cellHeigthString = "height=\"160px\"";

	private String hauteur = "160";

	private String hauteurCelluleFictive = "130";// 170 - 40 + 16

	// private String chainevide="&nbsp;";
	private String chainevide = "";

	private ClsParametreOrganigrammeVO param;

	private ClsMessageCelluleVO message;

	private ClsLienOrganigrammeVO lien;

	/**
	 * Constructeur par d�faut
	 */
	public ClsOrgCellsDesigner()
	{

		super();
	}

	/**
	 * Constructeur complet
	 * 
	 *            couleur des traits de l'organigramme (mis � part celle des cellules)
	 */
	public ClsOrgCellsDesigner(GeneriqueConnexionService service, ClsParametreOrganigrammeVO param, ClsMessageCelluleVO message)
	{

		super();

		this.param = param;

		this.message = message;

		this.lien = new ClsLienOrganigrammeVO(this.param, message);

		this.service = service;
		
		//this.useraccess = useraccess;

		List l = service.find("from ParamData " + " where identreprise =" + param.getDossier() + " and ctab=266 and nume=2 and cacc='FCHEIGHT1'");
		//hauteur = ClsConfigurationParameters.getConfigParameterValue(service, param.getDossier(), param.getLangue(), ClsConfigurationParameters.HAUTEUR_CELLULE_ORGANIGRAMME_COMPLET);
		for (Object object : l)
		{
			if (object instanceof ParamData)
			{
				ParamData o1 = (ParamData) object;
				hauteur = o1.getVall();
			}
		}
		
		l = service.find("from ParamData " + " where identreprise =" + param.getDossier() + " and ctab=266 and nume=2 and cacc='FICTHEIGHT'");
		//hauteurCelluleFictive = ClsConfigurationParameters.getConfigParameterValue(service, param.getDossier(), param.getLangue(), ClsConfigurationParameters.HAUTER_CELLULE_FICTIVE_VUE_COMPLETE);
		for (Object object : l)
		{
			if (object instanceof ParamData)
			{
				ParamData o1 = (ParamData) object;
				hauteurCelluleFictive = o1.getVall();
			}
		}

		hauteurCelluleFictive = String.valueOf(Integer.parseInt(hauteurCelluleFictive) + Integer.parseInt(hauteur));

		if (ClsParametreOrganigrammeVO.VUE_FONCTIONS_UNIQUEMENT.equalsIgnoreCase(param.getAffichage()))
		{
			l = service.find("from ParamData " + " where identreprise =" + param.getDossier() + " and ctab=266 and nume=2 and cacc='FCHEIGHT2'");
			//hauteur = ClsConfigurationParameters.getConfigParameterValue(service, param.getDossier(), param.getLangue(), ClsConfigurationParameters.HAUTEUR_CELLULE_ORGANIGRAMME_FONCTION_UNIQUEMENT);
			for (Object object : l)
			{
				if (object instanceof ParamData)
				{
					ParamData o1 = (ParamData) object;
					hauteur = o1.getVall();
				}
			}

			hauteurCelluleFictive = String.valueOf(5 + Integer.parseInt(hauteur));

		}

		if (ClsParametreOrganigrammeVO.VUE_FONCTIONS_ET_NOM.equalsIgnoreCase(param.getAffichage()))
		{
			l = service.find("from ParamData " + " where identreprise =" + param.getDossier() + " and ctab=266 and nume=2 and cacc='FCHEIGHT3'");
			//hauteur = ClsConfigurationParameters.getConfigParameterValue(service, param.getDossier(), param.getLangue(), ClsConfigurationParameters.HAUTEUR_CELLULE_ORGANIGRAMME_FONCTION_ET_NOM);
			for (Object object : l)
			{
				if (object instanceof ParamData)
				{
					ParamData o1 = (ParamData) object;
					hauteur = o1.getVall();
				}
			}
			hauteurCelluleFictive = hauteur;
		}

		if (ClsParametreOrganigrammeVO.VUE_SANS_ACTIONS.equalsIgnoreCase(param.getAffichage())||ClsParametreOrganigrammeVO.VUE_PHOTO_FONCTIONS_ET_NOM.equalsIgnoreCase(param.getAffichage()))
		{
			hauteurCelluleFictive = String.valueOf(80 + Integer.parseInt(hauteur));
		}

		cellHeigthString = "height=\"" + hauteur + "px\"";

	}

	/**
	 * @return la chaine HTML correspondant � l'organigramme
	 */

	public String _getFlowChart()
	{

		String afficherfictif = param.isAfficherLignesFictive() ? "O" : "N";

		if (ParameterUtil._isStringNull(param.getLongueurLibelle()))
			param.setLongueurLibelle("17");

		String strCouleurTrait = param.getCouleurTrait();

		int intTailleTrait = Integer.valueOf(param.getTailleBordure());

		ClsOrgCellsStyle style = new ClsOrgCellsStyle(param.getCouleurContenuCellule(), strCouleurTrait, intTailleTrait);

		param.setAfficherLignesFictive(StringUtils.equals("O", afficherfictif));

		ClsOrgCellsBuilder _o_Organigrammee = new ClsOrgCellsBuilder(service, param);

		List<ClsOrgCellule> allCellules = _o_Organigrammee._getAllCellules();

		if (StringUtils.equals("N", afficherfictif) || StringUtils.isBlank(afficherfictif))
		{
			if (_o_Organigrammee.existFictiveCell)
			{
				if (allCellules.size() > 0)
					_o_Organigrammee.supprimerCellulesFictivesSansFils(allCellules.get(0), allCellules);
			}
		}

		this.allCellules = allCellules;

		String strOrganigramme = null;

		if (allCellules.size() > 0)

			strOrganigramme = this._drawFlowChart();

		else
			strOrganigramme = ParameterUtil.CHAINE_ESPACE;

		strOrganigramme = strOrganigramme.replaceAll(style.getLigneGaucheOld(), style.getLigneGauche());
		strOrganigramme = strOrganigramme.replaceAll(style.getLigneHauteOld(), style.getLigneHaute());
		strOrganigramme = strOrganigramme.replaceAll(style.getLigneGaucheHauteOld(), style.getLigneGaucheHaute());
		strOrganigramme = strOrganigramme.replaceAll(style.getLigneDroiteHauteOld(), style.getLigneDroiteHaute());
		strOrganigramme = strOrganigramme.replaceAll(style.getLigneDroiteOld(), style.getLigneDroite());
		strOrganigramme = strOrganigramme.replaceAll(style.getContenucelluleOld(), style.getContenucellule());
		strOrganigramme = strOrganigramme.replaceAll(style.getCellulefinctiveOld(), style.getCellulefictive());
		strOrganigramme = strOrganigramme.replaceAll(style.getTableLigneDroiteFictiveOld(), style.getTableLigneDroiteFictive());
		strOrganigramme = strOrganigramme.replaceAll(style.getTdLigneDroiteFictiveOld(), style.getTdLigneDroiteFictive());
		strOrganigramme = strOrganigramme.replaceAll("stylecouleurdefond", "color: " + param.getCouleurContenuCellule());

		return strOrganigramme;

	}

	public GeneriqueConnexionService getService()
	{

		return service;
	}

	public void setService(GeneriqueConnexionService service)
	{

		this.service = service;
	}

	/**
	 * @return la table qui va contenir tout l'organigramme
	 */
	private String dessinerTableOrganigramme()
	{

		String _strOrganigramme = "<table id=\"htmlorgtable\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">";

		return _strOrganigramme;
	}

	/**
	 * Dessin du contenu d'une cellule
	 * 
	 * @param cellule :
	 *            la cellule dont on veut avoir le contenu
	 * @param strPositionLien :
	 *            la position de la fl�che permettant d'afficher soit � partir de la cellule courante, soit � partir de son p�re
	 * @return le contenu de la cellule
	 */
	private String _drawCellContent(ClsOrgCellule cellule, String strPositionLien)
	{

		String strLienHaut = this.lien.getLienHaut(cellule.getCodepere(), param.getNiveauArrive());

		String strLienBas = this.lien.getLienBas(cellule.getCodeorganigramme(), param.getNiveauArrive());
		
		String affectionAgent="&nbsp;";

		String strCell = "";

		String nomImagePrestataire = "prestataires.gif";

		if (cellule.getEffectifprestataire() != 0)
			nomImagePrestataire = "prestatairesuse.gif";

		if (ParameterUtil._isStringNull(cellule.getCouleur()))
			cellule.setCouleur("#0000FF");

		if (ClsParametreOrganigrammeVO.VUE_FONCTIONS_UNIQUEMENT.equalsIgnoreCase(param.getAffichage()))
			return _getFunctionTemplate(cellule.getLibelle(), cellule.getCouleur(), cellule.getTaillebordure());

		if (ClsParametreOrganigrammeVO.VUE_FONCTIONS_ET_NOM.equalsIgnoreCase(param.getAffichage()))
			return _getFunctionAndNameTemplate(cellule.getNomagent(), cellule.getLibelle(), cellule.getCouleur(), cellule.getTaillebordure());

		if (ClsParametreOrganigrammeVO.VUE_SANS_ACTIONS.equalsIgnoreCase(param.getAffichage()))
			return _getWithoutActionTemplate(cellule.getMatricule(), cellule.getLibelle(), cellule.getCouleur(), cellule.getTaillebordure());
		
		if (ClsParametreOrganigrammeVO.VUE_PHOTO_FONCTIONS_ET_NOM.equalsIgnoreCase(param.getAffichage()))
			return _getWithoutActionTemplate(cellule.getMatricule(), cellule.getLibelle()+(StringUtils.isEmpty(cellule.getNomagent())?"":" - "+cellule.getNomagent()), cellule.getCouleur(), cellule.getTaillebordure());

		String codelibelle = ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR + cellule.getCodeorganigramme() + ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR;

		String codelibelleniveau = ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR + cellule.getCodeorganigramme() + ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR + ","
				+ ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR + cellule.getCodeniveau() + ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR;

		String codelibellenbrefils = ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR + cellule.getCodeorganigramme() + ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR + ","
				+ ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR + cellule.getNbrFils() + ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR;

		String orgposte = ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR + cellule.getCodeorganigramme() + ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR + "," + ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR
				+ cellule.getCodeposte() + ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR;

		String libelleCellule = __getFlowChartName(cellule.getLibelle(), param.getLongueurLibelle());
		
		ClsLienCellule lien = new ClsLienCellule(param.racineImage);
		try
		{
			if (cellule.getMatricule() == null || cellule.getMatricule().trim().length() == 0)
			{
				strCell = "<table width=\"100%\" height=\"100%\" style=\"border: " + cellule.getCouleur() + " " + cellule.getTaillebordure() + "px solid;\" cellspacing=\"0\">" + "<tr>" + "<td align=\"center\">";
				if (ClsParametreOrganigrammeVO.LIENHAUT.equalsIgnoreCase(strPositionLien))
					strCell += strLienHaut;
				strCell += "<table width=\"10\" height=\"10\" align=\"center\">" + "<tr>" + "<td  align=\"center\"><img width=\"70\" height=\"70\" src=\"" + param.racineImage
						+ "assets/photoorg.gif\" border=\"0\" align=\"middle\"></td>" + "</tr>" + "</table></td>"
						+ "<td  rowspan=\"1\" valign=\"top\"><table width=\"30\" height=\"28\" border=\"0\" align=\"left\" cellspacing=\"5\" bordercolor=\"#000000\">" + "<tr>"
						+ "<td align=\"center\" valign=\"middle\">"
						+"<img width=\"16\" height=\"16\" src=\"" + param.racineImage
						+ "common/images/telephone.png\" border=\"0\" align=\"middle\">"
						+"</td>" + "</tr>" + "<tr>"
						+ "<td align=\"center\" valign=\"middle\">"
						+lien.getLienRevaloriser(message.getRevaloriser(), codelibelleniveau)
						+"</td>" + "</tr>" + "<tr>"
						+ "<td align=\"center\" valign=\"middle\">"
						+ lien.getLienSaisirPrestataire(nomImagePrestataire, message.getSaisirPrestataire(), codelibelle, cellule.getBprestataire())
						+"</td>" + "</tr>" + "<tr>";// +
			}
			else
			{
				String strPath = ParameterUtil.getPhotoPath(message.getRequest(), service, cellule.getMatricule(), param.getDossier(), param.getLangue());
				strPath = param.racineImage+strPath;

				strCell = "<table width=\"100%\" height=\"100%\" style=\"border: " + cellule.getCouleur() + " " + cellule.getTaillebordure() + "px solid;\" cellspacing=\"0\" bordercolor=\"#999999\">" + "<tr>"
						+ "<td align=\"center\">";
				if (ClsParametreOrganigrammeVO.LIENHAUT.equalsIgnoreCase(strPositionLien))
					strCell += strLienHaut;
				strCell += "<table width=\"10\" height=\"10\" align=\"center\">" + "<tr>" + "<td  align=\"center\">"
				+lien.getLienAppercuAgent(strPath, cellule.getMatricule(), cellule.getNomagent())
				
						
						+"</td>" + "</tr>" + "</table></td>"
						+ "<td  rowspan=\"1\" valign=\"top\"><table width=\"30\" height=\"28\" border=\"0\" align=\"left\" cellspacing=\"5\" bordercolor=\"#000000\">" + "<tr>"
						+ "<td align=\"center\" valign=\"middle\">"
						+lien.getLienAppercuAnnuaire(cellule.getMatricule(), message.getConsulterAnnuaire())
						+"</td>" + "</tr>" + "<tr>"
						+ "<td align=\"center\" valign=\"middle\">"
						+lien.getLienRevaloriser(message.getRevaloriser(), codelibelleniveau)
						+"</td>" + "</tr>" + "<tr>"
						+ "<td align=\"center\" valign=\"middle\">"
						+ lien.getLienSaisirPrestataire(nomImagePrestataire, message.getSaisirPrestataire(), codelibelle, cellule.getBprestataire())
						+"</td>" + "</tr>" + "<tr>";
			}

			if (cellule.getCodeposte() == null || cellule.getCodeposte().trim().length() == 0)
			{
				affectionAgent="<br><img src=\"" + param.racineImage
				+ "assets/agents.png\" width=\"16\" height=\"16\" border=\"0\" title=\""+message.getAffecterAgent()+"\">";
				strCell += "<td align=\"center\" valign=\"middle\">"
						+lien.getLienAffecterPoste(message.getAffecterPoste(), codelibelle)
						+affectionAgent+"</td>" + "</tr>"
						+ "</table></td>" + "</tr>" + "<tr heigth=\"40\">" + "<td align=\"left\"" + cellHeigthString + " valign=\"middle\" colspan=\"2\"><b class=\"titre\" title=\"" + cellule.getLibelle() + "\">"
						+ libelleCellule + "</b></td>" + "</tr>" + "<tr>" + "<td colspan=\"2\" align=\"center\" ><table width=\"96%\" height=\"20%\" border=\"0\" cellspacing=\"0\" bordercolor=\"#000000\">" + "<tr>"
						+ "<td align=\"center\" valign=\"middle\">"
						+lien.getLienAjouterFils(message.getAjouterFils(), codelibelle)
						+"</td>" + "<td align=\"center\" valign=\"middle\">"
						+lien.getLienModifierOrganigramme(message.getModifierOrganigramme(),codelibelleniveau)
						+"</td>"
						+ "<td align=\"center\" valign=\"middle\">"
						+lien.getLienSupprimerOrganigramme(message.getSupprimerOrganigramme(), codelibellenbrefils)
						+"</td>"
						+ "<td align=\"center\" valign=\"middle\"><img src=\"" + param.racineImage + "images/application/consulterposte.gif\" border=\"0\" title=\""+message.getConsulterPoste()+"\"></td>" + "</tr>" + "</table>";
				if (ClsParametreOrganigrammeVO.LIENBAS.equalsIgnoreCase(strPositionLien))
					strCell += strLienBas;
				strCell += "</td>" + "</tr>" + "</table>";
			}
			else
			{

				affectionAgent="<br>"
				+lien.getLienAffecterAgent(message.getAffecterAgent(), codelibelle, cellule.getCodeposte());
				
				strCell += "<td align=\"center\" valign=\"middle\">"
					
						+lien.getLienDesactiverPoste(message.getDesaffecterLePosteDeLaCellule(), orgposte)
						+affectionAgent+"</td>" + "</tr>" + "</table></td>" + "</tr>" + "<tr heigth=\"40\">"
						+ "<td align=\"left\"" + cellHeigthString + " valign=\"middle\" colspan=\"2\"><b class=\"titre\" title=\"" + cellule.getLibelle() + "\">" + libelleCellule + "</b></td>" + "</tr>" + "<tr>"
						+ "<td colspan=\"2\" align=\"center\"><table width=\"96%\" height=\"20%\" border=\"0\" cellspacing=\"0\" bordercolor=\"#000000\">" + "<tr>"
						+ "<td align=\"center\" valign=\"middle\">"
						
						+lien.getLienAjouterFils(message.getAjouterFils(), codelibelle)
						
						+"</td>" + "<td align=\"center\" valign=\"middle\">"
						
						+lien.getLienModifierOrganigramme(message.getModifierOrganigramme(), codelibelleniveau)
						
						+"</td>"
						+ "<td align=\"center\" valign=\"middle\">"
						
						+lien.getLienSupprimerOrganigramme(message.getSupprimerOrganigramme(), codelibellenbrefils)
						
						+"</td>"
						+ "<td align=\"center\" valign=\"middle\">"
						
						+lien.getLienConsulterPoste(message.getConsulterPoste(), cellule.getCodeposte())
						
						+"</td>" + "</tr>" + "</table>";
				if (ClsParametreOrganigrammeVO.LIENBAS.equalsIgnoreCase(strPositionLien))
					strCell += strLienBas;
				strCell += "</td>" + "</tr>" + "</table>";
			}

			return strCell;
		}
		catch (Exception e)
		{
			return strCell;
		}
	}

	/**
	 * Le template de contenu lorsque l'utilisateur choisir l'affichage sans actions
	 * 
	 * @param matriculeagent :
	 *            matricule de l'agent
	 * @param strLibelle :
	 *            intitul� de l'organigramme
	 * @param color :
	 *            couleur de la bordure de la cellule
	 * @param strBorderSize :
	 *            taille de la bordure de la cellule
	 * @return le contenu de la cellule
	 */
	private String _getWithoutActionTemplate(String matriculeagent, String strLibelle, String color, String strBorderSize)
	{
		String strCell = "";

		try
		{
			String saveLibelle = strLibelle;

			strLibelle = __getFlowChartName(strLibelle, param.getLongueurLibelle());

			if (matriculeagent == null || matriculeagent.trim().length() == 0)
				strCell = "<table width=\"100%\" height=\"100%\" style=\"border: " + color + " " + strBorderSize + "px solid;\" cellspacing=\"0\" cellpading=\"0\">" + "<tr>"
						+ "<td align=\"center\"><table width=\"100%\" height=\"100%\" align=\"center\">" + "<tr>" + "<td  align=\"center\"><img width=\"70\" height=\"70\" src=\"" + param.racineImage
						+ "assets/photoorg.gif\" border=\"0\" align=\"middle\"></td>" + "</tr>" + "</table></td>" + "</tr>" + "<tr>" + "<td align=\"center\" " + cellHeigthString
						+ " valign=\"middle\"><b class=\"titre\" title=\"" + saveLibelle + "\">" + strLibelle + "</b></td>" + "</tr>" + "</table>";
			else
			{
				String strPath = ParameterUtil.getPhotoPath(message.getRequest(), service, matriculeagent, param.getDossier(), param.getLangue());
				strPath = param.racineImage+strPath;
				strCell = "<table width=\"100%\" height=\"100%\" style=\"border:" + color + " " + strBorderSize + "px solid;\" cellspacing=\"0\" cellpading=\"0\">" + "<tr>"
						+ "<td ><table width=\"100%\" height=\"100%\" align=\"center\">" + "<tr>" + "<td  align=\"center\"><img width=\"70\" height=\"70\" src=\"" + strPath + "\" border=\"0\" align=\"middle\"></td>"
						+ "</tr>" + "</table></td>" + "</tr>" + "<tr>" + "<td align=\"center\" " + cellHeigthString + " valign=\"middle\"><b class=\"titre\" title=\"" + saveLibelle + "\">" + strLibelle + "</b></td>"
						+ "</tr>" + "</table>";
			}
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}

		return strCell;
	}

	/**
	 * @param strLibelle :
	 *            intitul� de l'organigramme
	 * @param color :
	 *            couleur de la bordure de la cellule
	 * @param strBorderSize :
	 *            taille de la bordure de la cellule
	 * @return
	 */
	private String _getFunctionTemplate(String strLibelle, String color, String strBorderSize)
	{
		String strCell = "";

		try
		{
			String saveLibelle = strLibelle;

			strLibelle = __getFlowChartName(strLibelle, param.getLongueurLibelle());

			strCell = "<table width=\"100%\" height=\"100%\" style=\"border: " + color + " " + strBorderSize + "px solid;\" cellspacing=\"0\">" + "<tr>" + "<td align=\"center\" " + cellHeigthString
					+ " valign=\"middle\" rowspan=\"2\">" + "<b class=\"titre\" title=\"" + saveLibelle + "\">" + strLibelle + "</b>" + "</td>" + "</tr>" + "</table>";
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}

		return strCell;
	}

	/**
	 * @param strLibelle :
	 *            intitul� de l'organigramme
	 * @param color :
	 *            couleur de la bordure de la cellule
	 * @param strBorderSize :
	 *            taille de la bordure de la cellule
	 * @return
	 */
	private String _getFunctionAndNameTemplate(String strNomAgent, String strLibelle, String color, String strBorderSize)
	{
		String strCell = "";

		try
		{
			int intHauter = Integer.parseInt(hauteur);
			String cellHeigthString1 = "height=\"" + intHauter / 2 + "px\"";

			String cellHeigthString2 = "height=\"" + intHauter / 2 + "px\"";

			String saveLibelle = strLibelle;

			String saveNom = strNomAgent;

			strLibelle = __getFlowChartName(strLibelle, param.getLongueurLibelle());

			if (StringUtils.isBlank(strNomAgent) || StringUtils.equals("null", strNomAgent))
				strNomAgent = "";
			else
			{
				//if (!param.isIceface())
				//{
					strNomAgent = strNomAgent.replaceAll("apostrphe123", "'");
					strNomAgent = strNomAgent.replaceAll("APOSTRPHE123", "'");
				//}
				strNomAgent = __getFlowChartName(strNomAgent, param.getLongueurLibelle());
				if (!param.isIceface())
				{
					strNomAgent = strNomAgent.replaceAll("'", "apostrphe123");
					strNomAgent = strNomAgent.replaceAll("'", "APOSTRPHE123");
				}
			}

			strCell = "<table width=\"100%\" height=\"100%\" style=\"border: " + color + " " + strBorderSize + "px solid;\" cellspacing=\"0\">" + "<tr>" + "<td align=\"center\" " + cellHeigthString1
					+ " valign=\"middle\">" + "<b class=\"titre\" title=\"" + saveLibelle + "\">" + strLibelle + "</b>" + "</td></tr>" + "<tr>" + "<td align=\"center\" " + cellHeigthString2
					+ " valign=\"middle\" style=\"background-color : gray;\" >" + "<b class=\"titre\" title=\"" + saveNom + "\">" + strNomAgent + "</b>" + "</td>" + "</tr>" + "</table>";
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return strCell;
	}

	public String _getFictiveTable(ClsOrgCellule cellule, String hauteurCellule)
	{
		String codelibelleniveau = ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR + cellule.getCodeorganigramme() + ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR + ","
		+ ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR + cellule.getCodeniveau() + ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR;
		String strCell = "";
		strCell += "<table  title=\"" + cellule.getCodeorganigramme() + "  " + cellule.getLibelle() + "\" border=\"1\" height= \"" + hauteurCellule
				+ "\"  cellspacing=\"0\" cellpadding=\"0\" class=\"tableLigneDroiteFictive\">";
		// strCell +="<table title=\""+cellule.getCodeorganigramme() +
		// " "+cellule.getLibelle()+"\" width=\"100%\" height= \""+hauteurCellule+"\" cellspacing=\"0\" cellpadding=\"0\" class=\"tableLigneDroiteFictive\">";
		strCell += "<tr>";
		// strCell +="<td class=\"tdLigneDroiteFictive\" title=\""+cellule.getCodeorganigramme() + " "+cellule.getLibelle()+"\"></td>";
		strCell += "<td ondblclick=\"modifierorganigramme("+codelibelleniveau+")\" title=\"" + cellule.getCodeorganigramme() + "  " + cellule.getLibelle() + "\"></td>";
		strCell += "</tr>";
		strCell += "</table>";

		return strCell;
	}

	/**
	 * Renvoit l'intitul� de la cellule sur une longueur de Strsize sur chacune des deux lignes
	 * 
	 * @param libelle :
	 *            intitul� initial de la cellule
	 * @param Strsize :
	 *            longueur de l'intitul� final sur chaque ligne (avec deux lignes maxi)
	 * @return
	 */
	private String __getFlowChartName(String libelle, String Strsize)
	{
		if (StringUtils.isBlank(libelle))
			return "";
		// int size= Integer.valueOf(param.getLongueurLibelle()

		// String marginLeft="&nbsp;";
		String marginLeft = "";

		int size = Integer.valueOf(Strsize);

		size = size - 1;

		// String strChaineCompleteur = "<font style=\"stylecouleurdefond\">.</font>";

		String[] taba = StringTokenizerUtils.tokenizeToArray(libelle, " ");

		String resultat = null;

		String chaine1 = "";
		String chaine2 = "";

		int lastIndex = 0;

		int l1 = 0;
		int l2 = 0;

		for (int i = 0; i < taba.length; i++)
		{
			l1 += taba[i].length();
			if (l1 <= size)
			{

				if (chaine1.trim().length() == 0)
					chaine1 = taba[i];
				else
					chaine1 += ParameterUtil.CHAINE_ESPACE + taba[i];
			}
			else
			{
				for (int j = i; j < taba.length; j++)
				{
					l2 += taba[j].length();
					if (l2 <= size)
					{
						if (chaine2.trim().length() == 0)
							chaine2 = taba[j];
						else
							chaine2 += ParameterUtil.CHAINE_ESPACE + taba[j];
					}
					else
					{
						lastIndex = j;
						break;
					}
				}

				break;
			}
		}

		if (ParameterUtil._isStringNull(chaine1))
		{
			if (taba.length > 0)
				resultat = marginLeft + taba[0].substring(0, size) + "<br>" + marginLeft;
			else
				resultat = marginLeft + "<br>" + marginLeft;
		}
		else
		{
			if (ParameterUtil._isStringNull(chaine2))
			{
				if (lastIndex != 0)
					resultat = marginLeft + chaine1 + "<br>" + marginLeft + taba[lastIndex].substring(0, size);
				else
					resultat = marginLeft + chaine1 + "<br>" + marginLeft;
			}
			else
			{
				resultat = marginLeft + chaine1 + "<br>" + marginLeft + chaine2;
			}
		}

		return resultat;

		// int intLongueurLibelle = Integer.valueOf(param.getLongueurLibelle());

		// if (libelle.trim().length() > size)
		//			
		// return libelle.substring(0, size);
		// else
		// return libelle;

		// try {
		// if (libelle.trim().length() < intLongueurLibelle) {
		//				
		// int intRest = intLongueurLibelle - libelle.trim().length();
		// if(intRest > 0)
		// {
		// libelle = libelle.trim();
		// String _strCompleteChars = "<span class=\"contenucellule\">";
		// for(int i = 1; i <= intRest; i++)
		// {
		// _strCompleteChars += "_";
		// }
		// libelle = libelle + _strCompleteChars + "</span>";
		// }
		// } else
		// libelle = libelle.substring(0, intLongueurLibelle);
		//			
		// return libelle;
		//			
		// } catch (Exception e) {
		// // TODO: handle exception
		// return libelle;
		// }
	}

	/**
	 * dessiner la cellule p�re
	 * 
	 * @param cellulePere
	 * @return partie de l'organigramme correspondant � la ligne du p�re
	 */

	private String dessinerLignePremierElement(ClsOrgCellule cellulePere)
	{
		String _strOrganigramme = "";

		_strOrganigramme += "<tr>";

		_strOrganigramme += "<td align=\"center\" valign=\"top\">";

		// Tableau qui sert � afficher le premier �l�ment de l'organigramme

		_strOrganigramme += "<table width=\"140px\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">";

		_strOrganigramme += "<tr>";

		_strOrganigramme += "<td width=\"10px\">&nbsp;</td>";

		_strOrganigramme += "<td width=\"120px\" class=\"contenucellule\" style=\"border:" + cellulePere.getCouleur() + " " + cellulePere.getTaillebordure() + "px solid;\" valign=\"top\">";

		_strOrganigramme += _drawCellContent(cellulePere, ClsParametreOrganigrammeVO.LIENHAUT);

		_strOrganigramme += "</td>";

		_strOrganigramme += "<td width=\"10px\">&nbsp;</td>";

		_strOrganigramme += "</tr>";

		_strOrganigramme += "</table>";

		// S'il y a de personnes d�pendant de ce niveau, on affiche un trait vertical qui permettra de faire la relation avec les �l�ments

		if (cellulePere._possedeFils())
		{
			// Ce tableau affiche les deux cases qui ont une barre verticale pour mettre en �vidence les liens avec les subordonn�s
			_strOrganigramme += "<table width=\"180px\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">";

			_strOrganigramme += "<tr>";

			_strOrganigramme += "<td width=\"70px\" class=\"ligneDroite\">&nbsp;</td>";

			_strOrganigramme += "<td width=\"70px\" class=\"ligneGauche\">&nbsp;</td>";

			_strOrganigramme += "</tr>";

			_strOrganigramme += "</table>";
		}

		_strOrganigramme += "</td>";

		_strOrganigramme += "</tr>";

		return _strOrganigramme;
	}

	/**
	 * @return partie de l'organigramme
	 */
	private String dessinerIntroduireLigneAutresNiveaux()
	{
		String _strOrganigramme = "";

		_strOrganigramme += "<tr>";

		_strOrganigramme += "<td valign=\"top\" width=\"100%\">";

		_strOrganigramme += "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">";

		_strOrganigramme += "<tr align=\"center\">";

		return _strOrganigramme;
	}

	/**
	 * @return partie de l'organigramme
	 */

	private String dessinerFermerIntroduireLigneAutresNiveaux()
	{
		String _strOrganigramme = "</tr>";

		_strOrganigramme += "</table>";

		_strOrganigramme += "</td>";

		_strOrganigramme += "</tr>";

		_strOrganigramme += "<tr>";

		_strOrganigramme += "<td>&nbsp;</td>";

		_strOrganigramme += "</tr>";

		return _strOrganigramme;

	}

	/**
	 * @return fermer le tableau contenant tout l'organigramme
	 */

	private String dessinerFermerTableOrganigramme()
	{
		String _strOrganigramme = "</table>";

		return _strOrganigramme;
	}

	/**
	 * Introduit une partie du dessin des fils du p�re courant
	 * 
	 * @param indexCourant_lors_du_parcours_des_fils_du_pere
	 * @param nbrFils_du_Pere
	 * @param celluleFille
	 * @param cellulePere
	 * @return
	 */
	private String drawFirstPartOfFils(int indexCourant_lors_du_parcours_des_fils_du_pere, int nbrFils_du_Pere, ClsOrgCellule celluleFille, ClsOrgCellule cellulePere)
	{

		String _strOrganigramme = "<td valign=\"top\" align=\"center\">";
		_strOrganigramme += "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">";
		_strOrganigramme += "<tr align=\"center\" width=\"100%\">";
		_strOrganigramme += "<td align=\"center\" width=\"100%\" valign=\"top\">";
		_strOrganigramme += "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">";
		_strOrganigramme += "<tr valign=\"top\">";
		if (indexCourant_lors_du_parcours_des_fils_du_pere == 1 && nbrFils_du_Pere > 1)
		{
			_strOrganigramme += "<td width=\"50%\" class=\"ligneDroite\">&nbsp;</td>";
			_strOrganigramme += "<td width=\"50%\" class=\"ligneGaucheHaute\">&nbsp;</td>";
		}
		else if (indexCourant_lors_du_parcours_des_fils_du_pere == 1 && nbrFils_du_Pere == 1)
		{
			_strOrganigramme += "<td width=\"50%\" class=\"ligneDroite\">&nbsp;</td>";
			_strOrganigramme += "<td width=\"50%\" class=\"ligneGauche\">&nbsp;</td>";
		}
		else if (indexCourant_lors_du_parcours_des_fils_du_pere == nbrFils_du_Pere)
		{
			_strOrganigramme += "<td width=\"50%\" class=\"ligneDroiteHaute\">&nbsp;</td>";
			_strOrganigramme += "<td width=\"50%\" class=\"ligneGauche\">&nbsp;</td>";
		}
		else
		{
			_strOrganigramme += "<td width=\"50%\" class=\"ligneDroiteHaute\">&nbsp;</td>";
			_strOrganigramme += "<td width=\"50%\" class=\"ligneGaucheHaute\">&nbsp;</td>";
		}
		_strOrganigramme += "</tr>";
		_strOrganigramme += "</table>";

		if (StringUtils.equals("O", celluleFille.getBcasefictive()))
			_strOrganigramme += "<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">";
		else
			_strOrganigramme += "<table width=\"140px\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">";

		_strOrganigramme += "<tr>";
		if (StringUtils.equals("O", celluleFille.getBcasefictive()))
			_strOrganigramme += "<td>" + chainevide + "</td>";
		else
			_strOrganigramme += "<td width=\"10px\">&nbsp;</td>";
		if (StringUtils.equals("O", celluleFille.getBcasefictive()))
			_strOrganigramme += "<td>";
		else
			_strOrganigramme += "<td width=\"120px\">";

		if (StringUtils.equals("O", celluleFille.getBcasefictive()))
			_strOrganigramme += _getFictiveTable(celluleFille, hauteurCelluleFictive);
		else
		{
			_strOrganigramme += "<table width=\"100%\" border=\"0\" class=\"contenucellule\" cellspacing=\"0\" cellpadding=\"0\"  style=\"border:" + celluleFille.getCouleur() + " " + celluleFille.getTaillebordure()
					+ "px solid;\">";
			_strOrganigramme += "<tr>";
			_strOrganigramme += "<td valign=\"top\">";

			_strOrganigramme += _drawCellContent(celluleFille, ClsParametreOrganigrammeVO.LIENBAS);

			_strOrganigramme += "</td>";
			_strOrganigramme += "</tr>";
			_strOrganigramme += "</table>";
		}

		_strOrganigramme += "</td>";
		if (StringUtils.equals("O", celluleFille.getBcasefictive()))
			_strOrganigramme += "<td>" + chainevide + "</td>";
		else
			_strOrganigramme += "<td width=\"10px\">&nbsp;</td>";
		_strOrganigramme += "</tr>";
		_strOrganigramme += "</table>";

		int nbrFils_du_Fils_Courant = celluleFille.getNbrFils();

		if (nbrFils_du_Fils_Courant != 0)
		{
			_strOrganigramme += "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">";
			_strOrganigramme += "<tr>";
			_strOrganigramme += "<td width=\"50%\" class=\"ligneDroite\">&nbsp;</td>";
			_strOrganigramme += "<td width=\"50%\" class=\"ligneGauche\">&nbsp;</td>";
			_strOrganigramme += "</tr>";
			_strOrganigramme += "</table>";
		}
		_strOrganigramme += "</td>";
		_strOrganigramme += "</tr>";
		_strOrganigramme += "</table>";

		return _strOrganigramme;
	}

	/**
	 * ferme la partie du dessin des fils du p�re courant, partie ouverte par la fonction ci-dessus
	 * 
	 * @return
	 */
	private String drawLastPartOfFils()
	{

		String _strOrganigramme = "</td>";

		return _strOrganigramme;
	}

	/**
	 * Dessine les fils de niveau sup�rieur � 2
	 * 
	 * @param celluleFille
	 * @return
	 */
	private String drawFilsFilsNiveauSuperieureA2(ClsOrgCellule celluleFille)
	{

		String _strOrganigramme = "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">";
		_strOrganigramme += "<tr>";

		int indexCourant_lors_du_parcours_du_Fils = 1;
		for (int i = 0; i < celluleFille.getFils().size(); i++)
		{
			_strOrganigramme += "<td align=\"center\" valign=\"top\">";
			_strOrganigramme += "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">";
			_strOrganigramme += "<tr>";
			if (indexCourant_lors_du_parcours_du_Fils == 1 && celluleFille.getNbrFils() > 1) // Le premier �l�ment
			{
				_strOrganigramme += "<td width=\"50%\" class=\"ligneDroite\">&nbsp;</td>";
				_strOrganigramme += "<td width=\"50%\" class=\"ligneGaucheHaute\">&nbsp;</td>";
			}
			else if (indexCourant_lors_du_parcours_du_Fils == 1 && celluleFille.getNbrFils() == 1) // Le premier �l�ment
			{
				_strOrganigramme += "<td width=\"50%\" class=\"ligneDroite\">&nbsp;</td>";
				_strOrganigramme += "<td width=\"50%\" class=\"ligneGauche\">&nbsp;</td>";
			}
			else if (indexCourant_lors_du_parcours_du_Fils == celluleFille.getNbrFils()) // Le second �l�ment
			{
				_strOrganigramme += "<td width=\"50%\" class=\"ligneDroiteHaute\">&nbsp;</td>";
				_strOrganigramme += "<td width=\"50%\" class=\"ligneGauche\">&nbsp;</td>";
			}
			else
			{
				_strOrganigramme += "<td width=\"50%\" class=\"ligneDroiteHaute\">&nbsp;</td>";
				_strOrganigramme += "<td width=\"50%\" class=\"ligneGaucheHaute\">&nbsp;</td>";
			}
			_strOrganigramme += "</tr>";
			_strOrganigramme += "</table>";
			if (StringUtils.equals("O", celluleFille.getFils().get(i).getBcasefictive()))
				_strOrganigramme += "<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">";
			else
				_strOrganigramme += "<table width=\"140px\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">";

			_strOrganigramme += "<tr>";
			if (StringUtils.equals("O", celluleFille.getFils().get(i).getBcasefictive()))
				_strOrganigramme += "<td>" + chainevide + "</td>";
			else
				_strOrganigramme += "<td width=\"10px\">&nbsp;</td>";
			if (StringUtils.equals("O", celluleFille.getFils().get(i).getBcasefictive()))
				_strOrganigramme += "<td>";
			else
				_strOrganigramme += "<td width=\"120px\">";

			if (StringUtils.equals("O", celluleFille.getFils().get(i).getBcasefictive()))
				_strOrganigramme += _getFictiveTable(celluleFille.getFils().get(i), hauteurCelluleFictive);
			else
			{
				_strOrganigramme += "<table width=\"100%\" border=\"0\" class=\"contenucellule\" cellspacing=\"0\" cellpadding=\"0\"  style=\"border:" + celluleFille.getFils().get(i).getCouleur() + " "
						+ celluleFille.getFils().get(i).getTaillebordure() + "px solid;\">";
				_strOrganigramme += "<tr>";
				_strOrganigramme += "<td valign=\"top\">";

				_strOrganigramme += _drawCellContent(celluleFille.getFils().get(i), ClsParametreOrganigrammeVO.LIENBAS);

				_strOrganigramme += "</td>";
				_strOrganigramme += "</tr>";
				_strOrganigramme += "</table>";
			}

			_strOrganigramme += "</td>";
			if (StringUtils.equals("O", celluleFille.getFils().get(i).getBcasefictive()))
				_strOrganigramme += "<td>" + chainevide + "</td>";
			else
				_strOrganigramme += "<td width=\"10px\">&nbsp;</td>";
			_strOrganigramme += "</tr>";
			_strOrganigramme += "</table>";

			if (allCellules.get(ClsOrgCellsBuilder._getCelluleIndex(allCellules, celluleFille.getFils().get(i))).getNbrFils() != 0)
			{
				_strOrganigramme += "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">";
				_strOrganigramme += "<tr>";
				_strOrganigramme += "<td width=\"50%\" class=\"ligneDroite\">&nbsp;</td>";
				_strOrganigramme += "<td width=\"50%\" class=\"ligneGauche\">&nbsp;</td>";
				_strOrganigramme += "</tr>";
				_strOrganigramme += "</table>";
			}
			_strOrganigramme += drawFilsFilsNiveauSuperieureA2(allCellules.get(ClsOrgCellsBuilder._getCelluleIndex(allCellules, celluleFille.getFils().get(i))));

			_strOrganigramme += "</td>";

			indexCourant_lors_du_parcours_du_Fils++;
		}
		_strOrganigramme += "</tr>";
		_strOrganigramme += "</table>";

		return _strOrganigramme;

	}

	/**
	 * @return la chaine HTML correspondant organigramme
	 */
	public String _drawFlowChart()
	{

		ClsOrgCellule cellulePere = allCellules.get(0);

		String _strOrganigramme = dessinerTableOrganigramme();

		_strOrganigramme += dessinerLignePremierElement(cellulePere);

		_strOrganigramme += dessinerIntroduireLigneAutresNiveaux();

		_strOrganigramme += _dessinDesFils(cellulePere);

		_strOrganigramme += dessinerFermerIntroduireLigneAutresNiveaux();

		_strOrganigramme += dessinerFermerTableOrganigramme();

		return _strOrganigramme;
	}

	/**
	 * Dessin des fils d'une cellule
	 * 
	 * @param cellulePere
	 * @return
	 */
	private String _dessinDesFils(ClsOrgCellule cellulePere)
	{

		String _strOrganigramme = "";

		int indexCourant_lors_du_parcours_des_fils_du_pere = 1;

		ClsOrgCellule celluleFille = null;
		for (int i = 0; i < cellulePere.getFils().size(); i++)
		{

			celluleFille = allCellules.get(ClsOrgCellsBuilder._getCelluleIndex(allCellules, cellulePere.getFils().get(i)));
			_strOrganigramme += drawFirstPartOfFils(indexCourant_lors_du_parcours_des_fils_du_pere, cellulePere.getNbrFils(), celluleFille, cellulePere);

			_strOrganigramme += drawFilsFilsNiveauSuperieureA2(celluleFille);

			_strOrganigramme += drawLastPartOfFils();

			indexCourant_lors_du_parcours_des_fils_du_pere += 1;

		}

		return _strOrganigramme;
	}

	public List<ClsOrgCellule> getAllCellules()
	{

		return allCellules;
	}

	public void setAllCellules(List<ClsOrgCellule> allCellules)
	{

		this.allCellules = allCellules;
	}
}
