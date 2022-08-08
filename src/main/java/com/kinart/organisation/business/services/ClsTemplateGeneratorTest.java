package com.kinart.organisation.business.services;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Classe qui permet de g�n�rer les templates des cellules
 * @author c.mbassi
 * @version 1.0
 */
public class ClsTemplateGeneratorTest
{
	public static String STR_PERE = "Pere";
	
	public static String STR_FILS = "Fils";
	
	public static String STR_STYLE_FILS = "STR_STYLE_FILS_";
	
	public static String STR_STYLE_BORDURE = "STR_STYLE_BORDURE_";
	
	public static String STR_PADDING= "STR_PADDING";
	
	public static String STR_LIGNE_GAUCHE = "STR_LIGNE_GAUCHE";
	
	/**
	 * 
	 * @return la table qui ouvre le template
	 */
	private static  String _openTemplate()
	{
		return "<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\"> "; 
	}
	
	/**
	 * 
	 * @param cellulePere
	 * @return la ligne html correspondant � la cellule pere
	 */
	private static  String _drawFatherLine(ClsOrgCellule cellulePere)
	{
		String result="";
	
		result += "	<tr>"; 
		result += "		<td colspan=\"3\" align=\"left\" valign=\"bottom\">"+ClsTemplateGeneratorTest.STR_PERE+cellulePere.getCodeorganigramme()+"</td> "; 
		result += "	</tr> "; 
		
		return result;
	}
	
	/**
	 * 
	 * @param cellulePere
	 * @param celluleFille
	 * @param allCellules
	 * @return la ligne fils (celluleFille) du p�re cellulePere
	 */
	private static  String _drawChildLine(ClsOrgCellule cellulePere,ClsOrgCellule celluleFille, List<ClsOrgCellule> allCellules)
	{
		String result="";
		
		String strPadding = ClsTemplateGeneratorTest.STR_PADDING;
		
		if((ClsOrgListCellsBuilder._isFirstChild(cellulePere, celluleFille) && allCellules.get(ClsOrgListCellsBuilder._getCelluleIndex(allCellules,celluleFille)).getFils().size()>0) || (! ClsOrgListCellsBuilder._isFirstChild(cellulePere, celluleFille)))
		{
			if(ClsOrgListCellsBuilder._isFirstChild(cellulePere, celluleFille)) strPadding="";
			result  += "			<tr>"; 
			result  += "				<td width=\"10px\">&nbsp;</td>"; 
			result  += "				<td colspan=\"2\" style=\""+strPadding+" "+ClsTemplateGeneratorTest.STR_LIGNE_GAUCHE+"\">&nbsp;</td>"; 
			result  += "			</tr>"; 
		}
		
		result  += _drawLeftLine(strPadding);
		
		result  += "			<tr>"; 
		result  += "				<td width=\"10px\">&nbsp;</td>"; 
		result  += "				<td width=\"10px\" style=\""+ClsTemplateGeneratorTest.STR_STYLE_BORDURE+celluleFille.getCodeorganigramme()+" "+ClsTemplateGeneratorTest.STR_STYLE_FILS+celluleFille.getCodeorganigramme()+"\">&nbsp;</td>"; 
		result  += "				<td rowspan=\"2\" align=\"left\" valign=\"center\">"+ClsTemplateGeneratorTest.STR_FILS+celluleFille.getCodeorganigramme()+"</td>"; 
		result  += "			</tr>"; 
		
		return result;
	}
	
	private static  String _drawFictiveChildLine(ClsOrgCellule cellulePere,ClsOrgCellule celluleFille, List<ClsOrgCellule> allCellules)
	{
		String result = "";
		
		if (! StringUtils.equals("O", celluleFille.getBcasefictive()))
		{
			String strPadding = ClsTemplateGeneratorTest.STR_PADDING;

			if (ClsOrgListCellsBuilder._isFirstChild(cellulePere, celluleFille))
				strPadding = "";
			
			result += _drawLeftLine(strPadding);

			result += "			<tr>";
			result += "				<td width=\"10px\">&nbsp;</td>";
			result += "				<td width=\"10px\" style=\"" + ClsTemplateGeneratorTest.STR_STYLE_BORDURE + celluleFille.getCodeorganigramme() + " " + ClsTemplateGeneratorTest.STR_STYLE_FILS
					+ celluleFille.getCodeorganigramme() + "\">&nbsp;</td>";
			result += "				<td rowspan=\"2\" align=\"left\" valign=\"center\">" + ClsTemplateGeneratorTest.STR_FILS + celluleFille.getCodeorganigramme() + "</td>";
			result += "			</tr>";
		}
		else
		{
			result += "			<tr>";
			result += "				<td rowspan=\"2\" align=\"left\" valign=\"center\">"+ ClsTemplateGeneratorTest.STR_FILS + celluleFille.getCodeorganigramme() + "</td>";
			result += "			</tr>";
		}
		
		return result;
	}
	
	private static String _drawLeftLine(String strPadding)
	{
		String result="";
		
		result  += "			<tr>"; 
		result  += "				<td width=\"10px\">&nbsp;</td>"; 
		result  += "				<td colspan=\"2\" style=\""+strPadding+" "+ClsTemplateGeneratorTest.STR_LIGNE_GAUCHE+"\">&nbsp;</td>"; 
		result  += "			</tr>"; 	
		
		return result;
	}
	
	/**
	 * 
	 * @return la ligne en dessous de chaque ligne fille
	 */
	private static  String _drawEndLine()
	{
		String result="";
	
		result += "			<tr>"; 
		result += "				<td colspan=\"2\" style=\""+ClsTemplateGeneratorTest.STR_PADDING+"\">&nbsp;</td>"; 
		result += "			</tr>"; 
		
		return result;
	}
	
	/**
	 * 
	 * @return la balise fermant le template courant
	 */
	private static  String _closeTemplate()
	{
		return "</table> "; 
	}
	
	/**
	 * 
	 * @param cellule cellule pour laquelle on va g�n�rer le template
	 * @param allCellules toutes les cellules de l'organigramme, issu de ClsOrgCellsListBuilder
	 * @return le template de la cellule cellule
	 */
	
	public static String _getTemplate(ClsOrgCellule cellule, List<ClsOrgCellule> allCellules)
	{
		String strTemplate= _openTemplate();
		
		ClsOrgCellule celluleFille = null;
		
		strTemplate+=_drawFatherLine(cellule);
		
		String childTemplate = "";
		if(StringUtils.equals("O", cellule.getBcasefictive()))
		{
			for (int i = 0; i < cellule.getFils().size(); i++)
			{
				celluleFille = cellule.getFils().get(i);

				childTemplate += _drawFictiveChildLine(cellule, celluleFille, allCellules);

			}
		}
		else
		{
			for (int i = 0; i < cellule.getFils().size(); i++)
			{
				celluleFille = cellule.getFils().get(i);

				childTemplate += _drawChildLine(cellule, celluleFille, allCellules);

			}
		}
		if(StringUtils.equals("O", cellule.getBcasefictive()))
		{
			strTemplate = strTemplate.replace(ClsTemplateGeneratorTest.STR_PERE+cellule.getCodeorganigramme(), childTemplate);
		}
		else
			strTemplate+=childTemplate;
		
		if(cellule.getFils().size() != 0 )
			strTemplate+=_drawEndLine();
		
		strTemplate+=_closeTemplate();
		
		return strTemplate;
	}
		
}
