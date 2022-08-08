package com.kinart.organisation.business.services;

import com.kinart.organisation.business.vo.ClsMessageCelluleVO;
import com.kinart.organisation.business.vo.ClsParametreOrganigrammeVO;
import com.kinart.paie.business.services.cloture.ClsNomenclature;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.kinart.paie.business.services.utils.ParameterUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;


public class ClsOrgRateauCellsDesigner implements ICellsDesigner
{

	/**
	 * Dessiner l'organigramme � partir de $Depart et s'arreter au niveau $strNiveaux
	 * 
	 * @param service
	 * @param $Depart
	 * @param $strNiveaux
	 * @return
	 */
	private GeneriqueConnexionService service;

	private ClsParametreOrganigrammeVO param;
	
	private ClsMessageCelluleVO message;

	private List<ClsOrgCellule> allCellules;

	public static String STR_FICTIVE_CELL = "<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">" + "<tr>" + "<td width=\"100%\" align=\"center\" valign=\"top\" >&nbsp;</td>"
			+ "<td width=\"100%\" height=\"135\" align=\"center\" valign=\"top\" style=\"border-left: blue 1px solid; \">&nbsp;</td>" + "</tr>" + "</table>";

	private ClsOrgListCellsStyle _o_orgStyle = null;

	private ClsOrgListCellsBuilder _o_Organigrammee;

	public ClsOrgRateauCellsDesigner(GeneriqueConnexionService service, ClsParametreOrganigrammeVO param,ClsMessageCelluleVO message)
	{

		super();

		this.param = param;
		
		this.message = message;

		this.service = service;

		_o_orgStyle = new ClsOrgListCellsStyle(param.getTailleBordure() , param.getCouleurTrait(), param.getPadding());

	}

	public String _getFlowChart()
	{
		_o_Organigrammee = new ClsOrgListCellsBuilder(service, param);

		List<ClsOrgCellule> allCellules = null;

		if (param.isOrganigrammePrestataire())
			allCellules = _o_Organigrammee._getAllPrestatairesCellules();
		else
			allCellules = _o_Organigrammee._getAllCellules();

		if (_o_Organigrammee.existFictiveCell)
			allCellules = _o_Organigrammee.rebuildListe(allCellules);

		this.allCellules = allCellules;

		String strOrganigramme = null;

		if (allCellules.size() > 0)

			strOrganigramme = this._drawFlowChart();

		else
			strOrganigramme = ParameterUtil.CHAINE_ESPACE;

		return strOrganigramme;

	}

	public String _drawFlowChart()
	{

		ClsOrgCellule cellulePere = allCellules.get(0);

		ClsOrgCellule celluleFille = null;

		String _strOrganigramme = "graph G { "+
											    "bgcolor=white; "+
										        "edge [color=black, arrowsize=2]; "+
										        "node [shape=box, color=blue, sides=6, fontname=Verdana]; ";
		// Le p�re
		_strOrganigramme += cellulePere.getCodeorganigramme()+" [label=\""+cellulePere.getLibelle().replaceAll("'", "\'")+"\", color="+cellulePere.getCouleur()+"]; ";
		String chgFils = StringUtils.EMPTY;
		for ( int  i= 0 ; i < cellulePere.getFils().size() ; i++ )
		{
				celluleFille = cellulePere.getFils().get(i);
				chgFils += ";"+celluleFille.getCodeorganigramme();
				_strOrganigramme += celluleFille.getCodeorganigramme()+" [label=\""+celluleFille.getLibelle().replaceAll("'", "\'")+"\", color="+celluleFille.getCouleur()+"]; ";
		}
		if(StringUtils.isNotEmpty(chgFils)){
			chgFils = chgFils.replaceFirst(";", StringUtils.EMPTY);
			_strOrganigramme += cellulePere.getCodeorganigramme()+" -- {"+chgFils+"};";
		}
		
		@SuppressWarnings("unused")
		int j = 0;

		for (int i = 0; i < cellulePere.getFils().size(); i++)
		{
			celluleFille = cellulePere.getFils().get(i);

			if (StringUtils.equals("O", celluleFille.getBcasefictive()))
				continue;

			j = i + 1;

			celluleFille = allCellules.get(ClsOrgListCellsBuilder._getCelluleIndex(allCellules, celluleFille));

			celluleFille.setNumero(i + 1);

			if (celluleFille.getNbrFils() > 0)
			{
				_strOrganigramme = _drawChildFlowChart(celluleFille, _strOrganigramme);
			}
//			System.out.println("CHAINE:"+_strOrganigramme);
		}
		_strOrganigramme += "}";

		return _strOrganigramme;
	}

	public String _drawChildFlowChart(ClsOrgCellule cellulePere, String _strOrganigramme)
	{

		ClsOrgCellule celluleFille = null;

		@SuppressWarnings("unused")
		int j = 0;
		String chgFils = StringUtils.EMPTY;
		for (int i = 0; i < cellulePere.getFils().size(); i++)
		{
			celluleFille = cellulePere.getFils().get(i);
			
			_strOrganigramme += celluleFille.getCodeorganigramme()+" [label=\""+celluleFille.getLibelle().replaceAll("'", "\'")+"\", color="+celluleFille.getCouleur()+"]; ";
			chgFils += ";"+celluleFille.getCodeorganigramme();

			if (StringUtils.equals("O", celluleFille.getBcasefictive()))
				continue;

			j = i + 1;

			celluleFille = allCellules.get(ClsOrgListCellsBuilder._getCelluleIndex(allCellules, celluleFille));

			celluleFille.setNumero(i + 1);

			if (celluleFille.getNbrFils() > 0)
			{
				_strOrganigramme = _drawChildFlowChart(celluleFille, _strOrganigramme);
			}
		}
		if(StringUtils.isNotEmpty(chgFils)){
			chgFils = chgFils.replaceFirst(";", StringUtils.EMPTY);
			_strOrganigramme += cellulePere.getCodeorganigramme()+" -- {"+chgFils+"};";
		}
		
		return _strOrganigramme;
	}

	private String getCellContent(ClsOrgCellule cellule)
	{
		return getCellContent(cellule.getCodeorganigramme(), cellule.getCategorieagent(), cellule.getCategorieposte(), cellule.getLibelle(), String.valueOf(cellule.getEffectif()),
				cellule.getNomagent(), cellule.getMatricule(), cellule.getCouleur(), cellule.getTaillebordure(), "N", cellule.getEffectifprestataire(), cellule.getBprestataire(), cellule.getEchelonagent(), cellule.getEchelonposte());
	}

	private String getCellContent(String codeorganigramme, String strCategorie, String strCategoriePoste, String strLibelleOrganigramme, String strEffectif, String strNom, String strMatricule,
			String color, String strBorderSize, String strCaseFictive, Integer effectifprestataire, String estPrestataire, String echelonAgent, String echelonPoste)
	{

		Integer intEffectif = 0;

		if (strCaseFictive != null && strCaseFictive.equalsIgnoreCase("O"))
			return "";// STR_FICTIVE_CELL_LIST;

		if (strEffectif != null && strEffectif.trim().length() != 0)
		{
			try
			{
				intEffectif = Integer.parseInt(strEffectif.trim()) + 1;
			}
			catch (Exception e)
			{
				// TODO: handle exception
			}
		}
		else
			intEffectif += 1;

		if (strNom != null && strNom.trim().length() != 0)
			strNom = strNom.toUpperCase();
		else
			strNom = "";

		if (strLibelleOrganigramme != null && strLibelleOrganigramme.trim().length() != 0)
			strLibelleOrganigramme = strLibelleOrganigramme.toUpperCase();

		if (strCategorie == null)
			strCategorie = "";
		
		if(StringUtils.isBlank(echelonAgent) || ! param.isAfficherechelon())
			echelonAgent = StringUtils.EMPTY;
		
		if(StringUtils.isBlank(echelonPoste) || ! param.isAfficherechelon())
				echelonPoste = StringUtils.EMPTY;

		if (strMatricule == null)
			strMatricule = "";

		if (color == null || color.trim().length() == 0)
			color = "blue";

		if (strCategorie == null)
			strCategorie = "--";
		if (strCategoriePoste == null)
			strCategoriePoste = "--";

		if (strBorderSize == null || strBorderSize.trim().length() == 0)
			strBorderSize = "2";

		String strAgent = strNom + " " + strMatricule + " [" + strCategorie+echelonAgent + "]";

		if (ParameterUtil._isStringNull(strNom))
			strAgent = ParameterUtil.CHAINE_ESPACE;

		String prestataireName = message.getNomPrestataire(effectifprestataire) ;
		
		String imagePrestataire = "[" + effectifprestataire + "<img src=\""+param.racineImage+"images/application/prestataires.gif\" width=\"10\" height=\"10\" border=\"0\" title=\""+ prestataireName + "\">]";
		if (effectifprestataire == 0)
			imagePrestataire = "";

		String result = "<table cellspacing=\"0\" border=\"0\" cellpadding=\"0\"><tr><td>" + "<table border=\"0\" style=\"border: " + color + " " + strBorderSize
				+ "px solid;\" cellspacing=\"0\" cellpadding=\"0\">" + "<tr>" + "<td class=\"couleurcontenucellule\" colspan=\"3\" align=\"center\"   style=\"" + ClsTemplateGenerator.STR_PADDING + "\">" +"<span  style=\"font-family : verdana; font-size : 10px;\">"+ strCategoriePoste +echelonPoste+ " "
				+ strLibelleOrganigramme + " " + "[" + intEffectif.toString() + "]" + imagePrestataire + "</span></td>" + "</tr>" + "</table></td>" + "<td align=\"left\" style=\"font-family : verdana; font-size : 10px;\" >" + "&nbsp;&nbsp;&nbsp;" + strAgent
				+ "</td></tr></table>";

		if (param.isOrganigrammePrestataire())
		{
			Map<String, List<ClsNomenclature>> map = _o_Organigrammee.getPrestatairesListMap();

			String effectif = imagePrestataire;// "[" + intEffectif.toString() + "]";
			strAgent = this._drawPrestatairesAndEffectif(map.get(codeorganigramme), color, strBorderSize);
			if (StringUtils.isBlank(strAgent))
				strAgent = "";
			if (StringUtils.equalsIgnoreCase("O", estPrestataire))
			{
				effectif = "";
			}
			else
				strAgent = strNom + " " + strMatricule + " " + strAgent;
			result = "<table cellspacing=\"0\" border=\"0\" cellpadding=\"0\"><tr><td>" + "<table border=\"0\" style=\"border: " + color + " " + strBorderSize
					+ "px solid;\" cellspacing=\"0\" cellpadding=\"0\">" + "<tr>" + "<td class=\"couleurcontenucellule\" colspan=\"3\" align=\"center\"   style=\"" + ClsTemplateGenerator.STR_PADDING + "\">" + " "
					 +"<span  style=\"font-family : verdana; font-size : 10px;\">"+ strLibelleOrganigramme + " " + effectif + "</span></td>" + "</tr>" + "</table></td>"
					+ "<td align=\"left\" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td> <td align=\"right\" valign=\"middle\" style=\"font-family : verdana; font-size : 10px;\" >" + strAgent + "</td></tr></table>";

		}

		return result;
	}

	private String _drawPrestatairesAndEffectif(List<ClsNomenclature> liste, String color, String strBorderSize)
	{
		String result = "";
		if (liste != null && !liste.isEmpty())
		{
			result = "<table cellspacing=\"0\" border=\"0\" cellpadding=\"0\" style=\"border : #000000 1px solid; \">";
			for (ClsNomenclature clsNomenclature : liste)
			{
				result += "<tr><td style=\"border : #000000 1px solid;font-family : verdana; font-size : 10px; \">&nbsp;" + clsNomenclature.getCode() + "&nbsp;&nbsp;</td><td align=\"center\" style=\"border : #000000 1px solid; font-family : verdana; font-size : 10px;\">&nbsp;"
						+ clsNomenclature.getLibelle() + "&nbsp;</td></tr>";
			}

			result += "</table>";
		}

		return result;

	}

	public GeneriqueConnexionService getService()
	{

		return service;
	}

	public void setService(GeneriqueConnexionService service)
	{

		this.service = service;
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
