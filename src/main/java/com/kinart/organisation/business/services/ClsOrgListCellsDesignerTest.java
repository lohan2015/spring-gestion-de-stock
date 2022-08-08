
package com.kinart.organisation.business.services;

import com.kinart.organisation.business.model.Organigramme;
import com.kinart.organisation.business.model.Orgniveau;
import com.kinart.organisation.business.vo.ClsParametreOrganigrammeVO;
import com.kinart.paie.business.model.ParamData;
import com.kinart.paie.business.services.cloture.ClsNomenclature;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.kinart.paie.business.services.utils.ParameterUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class ClsOrgListCellsDesignerTest
{

	/**
	 * Dessiner l'organigramme � partir de $Depart et s'arreter au niveau
	 * $strNiveaux
	 * 
	 * @param service
	 * @param $Depart
	 * @param $strNiveaux
	 * @return
	 */

	private String niveauarrivee;

	private GeneriqueConnexionService service;

	private String celluledepart;

	private String typevue;

	private HttpServletRequest request;

	private List<ClsOrgCellule> allCellules;

	public static String STR_FICTIVE_CELL = "<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">" + "<tr>"
			+ "<td width=\"100%\" align=\"center\" valign=\"top\" >&nbsp;</td>"
			+ "<td width=\"100%\" height=\"135\" align=\"center\" valign=\"top\" style=\"border-left: blue 1px solid; \">&nbsp;</td>"
			+ "</tr>" + "</table>";

	public static String STR_SPECIAL_CHAR = "V123V900F514Y";

	public static String LIENHAUT = "HAUT";

	public static String LIENBAS = "BAS";

	public static String STR_PHOTO_PATH = "photos/";

	public static String VUE_SANS_ACTIONS = "waction";

	public static String VUE_FONCTIONS_UNIQUEMENT = "function";

	private static final String REQUEST_CELLULE_DEPART = "codeorganigramme";

	private static final String REQUEST_NIVEAU_ARRIVEE = "codeniveau";
	
	private static final String REQUEST_SITE="site";

	private static final String REQUEST_TYPE_VUE = "view";

	public static String VUE_TOUT = "all";

	private String strLineBorder;

	private String strLineColor;
	

	private String strContentColor;

	private String strLongueurLibelle;
	
	private ClsOrgListCellsStyle _o_orgStyle = null;
	
	private String strComputeOrganigrammePrestataire;
	
	private ClsOrgListCellsBuilder _o_Organigrammee;

	public ClsOrgListCellsDesignerTest ( )
	{

		super();
	}

	/*public ClsOrgListCellsDesignerTest ( HttpServletRequest request , ClsService service , String celluledepart , String niveauarrivee ,
			String typevue , List<ClsOrgCellule> allCellules )
	{

		super();
		this.request = request;
		this.service = service;
		this.celluledepart = celluledepart;
		this.niveauarrivee = niveauarrivee;
		this.typevue = typevue;
		this.allCellules = allCellules;
		
	}*/

	public ClsOrgListCellsDesignerTest ( HttpServletRequest request , GeneriqueConnexionService service , String strContentColor , String strLineBorder ,
			String strLineColor, String strPadding )
	{

		super();

		this.request = request;

		this.service = service;

		this.strLineBorder = strLineBorder;

		this.strLineColor = strLineColor;

		if ( ParameterUtil._isStringNull(strLineColor) )
			this.strLineColor = "blue";

		if ( ParameterUtil._isStringNull(strLineBorder) )
			this.strLineBorder = "1";

		this.strContentColor = strContentColor;

		if ( ParameterUtil._isStringNull(strContentColor) )
			this.strContentColor = "#CCECFF";
		
		
		_o_orgStyle = new ClsOrgListCellsStyle(strLineBorder , strLineColor, strPadding);

	}

	public String _getFlowChart ( )
	{

		String strCodeDossier = ParameterUtil.getSessionObject(request, ParameterUtil.SESSION_DOSSIER);

		String strLangue = ParameterUtil.getSessionObject(request, ParameterUtil.SESSION_LANGUE);

		String _strCelluleDepart = request.getParameter(REQUEST_CELLULE_DEPART);

		String _strNiveauArrivee = request.getParameter(REQUEST_NIVEAU_ARRIVEE);

		ParameterUtil.println("Niveau d'arriv�e = " + _strNiveauArrivee);
		
		String _strSite= request.getParameter(REQUEST_SITE);

		String _strTypeVue = request.getParameter(REQUEST_TYPE_VUE);
		
		strComputeOrganigrammePrestataire = request.getParameter("computeorganigrammeprestataire");
		

		if ( ParameterUtil._isStringNull(_strNiveauArrivee) )
			_strNiveauArrivee = this.__getDefaultLevel(request, strCodeDossier, strLangue);

		if ( "-1".equalsIgnoreCase(_strNiveauArrivee) )
			_strNiveauArrivee = this.__getDefaultLevel(request, strCodeDossier, strLangue);

		if ( ParameterUtil._isStringNull(_strCelluleDepart) )
			_strCelluleDepart = this.__getDefaultCell(strCodeDossier);

		if ( ParameterUtil._isStringNull(strLongueurLibelle) )
			strLongueurLibelle = "17";

		this.celluledepart = _strCelluleDepart;

		this.niveauarrivee = _strNiveauArrivee;

		this.typevue = _strTypeVue;

		String strCouleurTrait = strLineColor;

		int intTailleTrait = Integer.valueOf(strLineBorder);

		boolean orgprestataire = StringUtils.equalsIgnoreCase(strComputeOrganigrammePrestataire,"O");
		_o_Organigrammee = new ClsOrgListCellsBuilder(service, new ClsParametreOrganigrammeVO());

		List<ClsOrgCellule> allCellules = null;
		
		if(orgprestataire)
			allCellules = _o_Organigrammee._getAllPrestatairesCellules();
		else
			allCellules = _o_Organigrammee._getAllCellules();

		this.allCellules = allCellules;

		String strOrganigramme = null;

		if ( allCellules.size() > 0 )

			strOrganigramme = this._drawFlowChart();

		else
			strOrganigramme = ParameterUtil.CHAINE_ESPACE;

		

		return strOrganigramme;

	}
	
	public String _drawFlowChart(){
		
		ClsOrgCellule cellulePere = allCellules.get(0);
		
		ClsOrgCellule celluleFille = null;
		
		String _strOrganigramme = ClsTemplateGenerator._getTemplate(cellulePere, allCellules);
		
		_strOrganigramme = _strOrganigramme.replace(ClsTemplateGenerator.STR_PERE+cellulePere.getCodeorganigramme(), this.getCellContent(cellulePere));

		
		int j=0;
		
		for ( int i = 0 ; i < cellulePere.getFils().size(); i++ )
		{
			j=i+1;
			
			celluleFille = cellulePere.getFils().get(i);
			
			celluleFille = allCellules.get(ClsOrgListCellsBuilder._getCelluleIndex(allCellules,celluleFille));
			
			celluleFille.setNumero(i+1);
			
			if(celluleFille.getNbrFils()>0)
			{
				
				if(ClsOrgListCellsBuilder._isLastChild(cellulePere, celluleFille))
				{
					_strOrganigramme = _strOrganigramme.replace(ClsTemplateGenerator.STR_STYLE_BORDURE+celluleFille.getCodeorganigramme(), _o_orgStyle._getStyleBordureDernierFils());
				}
				else
				{
					_strOrganigramme = _strOrganigramme.replace(ClsTemplateGenerator.STR_STYLE_BORDURE+celluleFille.getCodeorganigramme(), _o_orgStyle._getStyleBordureNonDernierFils());
				}
				
				_strOrganigramme = _strOrganigramme.replace(ClsTemplateGenerator.STR_STYLE_FILS+celluleFille.getCodeorganigramme(), _o_orgStyle._getStyleFilsAvecFils());
				
				_strOrganigramme = _strOrganigramme.replace(ClsTemplateGenerator.STR_FILS+celluleFille.getCodeorganigramme(), ClsTemplateGenerator._getTemplate(celluleFille, allCellules));
				
				_strOrganigramme = _drawChildFlowChart(celluleFille,_strOrganigramme );
			}
			else
			{
				_strOrganigramme = _strOrganigramme.replace(ClsTemplateGenerator.STR_STYLE_FILS+celluleFille.getCodeorganigramme(), _o_orgStyle._getStyleFilsSansFils());
				
				_strOrganigramme = _strOrganigramme.replace(ClsTemplateGenerator.STR_STYLE_BORDURE+celluleFille.getCodeorganigramme(), _o_orgStyle._getStyleBordureNonDernierFils());
				
				_strOrganigramme = _strOrganigramme.replace(ClsTemplateGenerator.STR_FILS+celluleFille.getCodeorganigramme(), this.getCellContent(celluleFille));
				
			}
			
		}
		_strOrganigramme = _strOrganigramme.replaceAll(ClsTemplateGenerator.STR_LIGNE_GAUCHE, _o_orgStyle._getStyleLigneGauche());
		
		_strOrganigramme = _strOrganigramme.replaceAll(ClsTemplateGenerator.STR_PADDING, _o_orgStyle._getPadding());
		
		
		return _strOrganigramme;
	}
	
	public String _drawChildFlowChart(ClsOrgCellule cellulePere, String _strOrganigramme){
				
		ClsOrgCellule celluleFille = null;
		
		
		
		_strOrganigramme = _strOrganigramme.replace(ClsTemplateGenerator.STR_PERE+cellulePere.getCodeorganigramme(), this.getCellContent(cellulePere));
		
		int j=0;
		
		for ( int i = 0 ; i < cellulePere.getFils().size(); i++ )
		{
			j=i+1;
			celluleFille = cellulePere.getFils().get(i);
			
			celluleFille = allCellules.get(ClsOrgListCellsBuilder._getCelluleIndex(allCellules,celluleFille));
			
			celluleFille.setNumero(i+1);
			
			if(celluleFille.getNbrFils()>0)
			{
				if(ClsOrgListCellsBuilder._isLastChild(cellulePere, celluleFille))
				{
					_strOrganigramme = _strOrganigramme.replace(ClsTemplateGenerator.STR_STYLE_BORDURE+celluleFille.getCodeorganigramme(), _o_orgStyle._getStyleBordureDernierFils());
				}
				else
				{
					_strOrganigramme = _strOrganigramme.replace(ClsTemplateGenerator.STR_STYLE_BORDURE+celluleFille.getCodeorganigramme(), _o_orgStyle._getStyleBordureNonDernierFils());
				}
				
				_strOrganigramme = _strOrganigramme.replace(ClsTemplateGenerator.STR_STYLE_FILS+celluleFille.getCodeorganigramme(), _o_orgStyle._getStyleFilsAvecFils());
				
				_strOrganigramme = _strOrganigramme.replace(ClsTemplateGenerator.STR_FILS+celluleFille.getCodeorganigramme(), ClsTemplateGenerator._getTemplate(celluleFille, allCellules));
				
				_strOrganigramme = _drawChildFlowChart(celluleFille,_strOrganigramme);
			}
			else
			{
				_strOrganigramme = _strOrganigramme.replace(ClsTemplateGenerator.STR_STYLE_FILS+celluleFille.getCodeorganigramme(), _o_orgStyle._getStyleFilsSansFils());
				
				_strOrganigramme = _strOrganigramme.replace(ClsTemplateGenerator.STR_STYLE_BORDURE+celluleFille.getCodeorganigramme(), _o_orgStyle._getStyleBordureNonDernierFils());
				
				_strOrganigramme = _strOrganigramme.replace(ClsTemplateGenerator.STR_FILS+celluleFille.getCodeorganigramme(), this.getCellContent(celluleFille));
			}
			
		}
		
		return _strOrganigramme;
	}

	@SuppressWarnings("unchecked")
	private String __getDefaultCell ( String strCdos )
	{

		String strDefaultStartCell = "02";

		List<Organigramme> _o_Collection = this.getService().find(
				"FROM Organigramme WHERE identreprise = " + "'" + strCdos + "'" + " AND codepere = " + "'"
						+ ParameterUtil.STR_INIT_CODEPERE + "'");

		if ( _o_Collection != null && _o_Collection.size() > 0 )
		{
			strDefaultStartCell = _o_Collection.get(0).getCodeorganigramme();
		}

		return strDefaultStartCell;
	}

	private String __getDefaultLevel ( HttpServletRequest request , String strCdos , String strLangue )
	{

		String strNbreNiveau = "2";

		try
		{
			String strDefaultLevel = null;

			try
			{
				List l = service.find("from ParamData " + " where identreprise =" + strCdos + " and ctab=266 and nume=2 and cacc='DEFT_LV'");
				for (Object object : l)
				{
					if (object instanceof ParamData)
					{
						ParamData o1 = (ParamData) object;
						strDefaultLevel = o1.getVall();
					}
				}

			} catch (Exception e)
			{
				e.printStackTrace();
			}

			if ( strDefaultLevel != null && strDefaultLevel.trim().length() != 0 )
				strNbreNiveau = strDefaultLevel;
			else
			{
				String _strQuery = "FROM Orgniveau where identreprise='" + strCdos + "' ORDER BY codeniveau ASC";
				List<Orgniveau> oRhpniveauCollection = this.getService().find(_strQuery);
				if ( oRhpniveauCollection != null && oRhpniveauCollection.size() > 0 )
				{
					strNbreNiveau = oRhpniveauCollection.get(0).getCodeniveau();
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			ParameterUtil.__invokeGarbageCollection();
		}

		return strNbreNiveau;
	}
	
	
	private String getCellContent(ClsOrgCellule cellule)
	{
		return getCellContent(cellule.getCodeorganigramme(),cellule.getCategorieagent(), cellule.getCategorieposte(), cellule.getLibelle(), String.valueOf(cellule.getEffectif()), cellule.getNomagent(), cellule.getMatricule(), cellule.getCouleur(), cellule.getTaillebordure(), "N", cellule.getEffectifprestataire(),cellule.getBprestataire());
	}
	
	
	private String getCellContent (String codeorganigramme, String strCategorie , String strCategoriePoste , String strLibelleOrganigramme ,
			String strEffectif , String strNom , String strMatricule , String color , String strBorderSize , String strCaseFictive, Integer effectifprestataire, String estPrestataire)
	{

		Integer intEffectif = 0;

		if ( strCaseFictive != null && strCaseFictive.equalsIgnoreCase("O") )
			return "";// STR_FICTIVE_CELL_LIST;

		if ( strEffectif != null && strEffectif.trim().length() != 0 )
		{
			try
			{
				intEffectif = Integer.parseInt(strEffectif.trim()) + 1;
			} catch (Exception e)
			{
				// TODO: handle exception
			}
		} else
			intEffectif += 1;

		if ( strNom != null && strNom.trim().length() != 0 )
			strNom = strNom.toUpperCase();
		else
			strNom = "";

		if ( strLibelleOrganigramme != null && strLibelleOrganigramme.trim().length() != 0 )
			strLibelleOrganigramme = strLibelleOrganigramme.toUpperCase();

		if ( strCategorie == null )
			strCategorie = "";

		if ( strMatricule == null )
			strMatricule = "";

		if ( color == null || color.trim().length() == 0 )
			color = "blue";

		if ( strCategorie == null )
			strCategorie = "--";
		if ( strCategoriePoste == null )
			strCategoriePoste = "--";

		if ( strBorderSize == null || strBorderSize.trim().length() == 0 )
			strBorderSize = "2";
		
		String strAgent = strNom + " " + strMatricule + " [" + strCategorie+ "]";
		
		if(ParameterUtil._isStringNull(strNom))
			strAgent = ParameterUtil.CHAINE_ESPACE;
		
		String prestataireName="externe rattach�";
		if(effectifprestataire>1) prestataireName="externes rattach�s";
		String imagePrestataire = "["+effectifprestataire+"<img src=\"images/application/prestataires.gif\" width=\"10\" height=\"10\" border=\"0\" title=\""+effectifprestataire+" "+prestataireName+" � ce poste\">]";
		if(effectifprestataire == 0) imagePrestataire="";

		String result= "<table cellspacing=\"0\" border=\"0\" cellpadding=\"0\"><tr><td>" + "<table border=\"0\" style=\"border: " + color + " "
				+ strBorderSize + "px solid;\" cellspacing=\"0\" cellpadding=\"0\">" + "<tr>" + "<td colspan=\"3\" align=\"center\"   style=\""+ClsTemplateGenerator.STR_PADDING+"\">"
				+ strCategoriePoste + " " + strLibelleOrganigramme + " " + "[" + intEffectif.toString() + "]"+imagePrestataire+ "</td>" + "</tr>"
				+ "</table></td>" + "<td align=\"left\" >" + "&nbsp;&nbsp;&nbsp;" + strAgent  +"</td></tr></table>";
		
		boolean orgprestataire = StringUtils.equalsIgnoreCase(strComputeOrganigrammePrestataire,"O");
		if(orgprestataire)
		{
			Map<String, List<ClsNomenclature> > map = _o_Organigrammee.getPrestatairesListMap();
			
			String effectif = imagePrestataire;// "[" + intEffectif.toString() + "]";
			strAgent = this._drawPrestatairesAndEffectif(map.get(codeorganigramme), color, strBorderSize);
			if(StringUtils.isBlank(strAgent))
				strAgent="";
			if(StringUtils.equalsIgnoreCase("O", estPrestataire))
			{
				effectif ="";
			}
			else
				strAgent = strNom + " " + strMatricule+ " " +strAgent;
			result= "<table cellspacing=\"0\" border=\"0\" cellpadding=\"0\"><tr><td>" + "<table border=\"0\" style=\"border: " + color + " "
			+ strBorderSize + "px solid;\" cellspacing=\"0\" cellpadding=\"0\">" + "<tr>" + "<td colspan=\"3\" align=\"center\"   style=\""+ClsTemplateGenerator.STR_PADDING+"\">"
			+ " " + strLibelleOrganigramme + " " + effectif + "</td>" + "</tr>"
			+ "</table></td>" + "<td align=\"left\" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td> <td align=\"right\" valign=\"middle\" >"  + strAgent  +"</td></tr></table>";
			
		}
		
		
		return result;
	}
	
	private String _drawPrestatairesAndEffectif(List<ClsNomenclature> liste, String color, String strBorderSize)
	{
		String result ="";
		if(liste != null && ! liste.isEmpty())
		{
			result = "<table cellspacing=\"0\" border=\"0\" cellpadding=\"0\" style=\"border : #000000 1px solid; \">";
			for (ClsNomenclature clsNomenclature : liste)
			{
				result += "<tr><td style=\"border : #000000 1px solid; \">&nbsp;"+ clsNomenclature.getCode() + "&nbsp;&nbsp;</td><td align=\"center\" style=\"border : #000000 1px solid; \">&nbsp;"+ clsNomenclature.getLibelle() + "&nbsp;</td></tr>";	
			}
			
			result += "</table>";
		}
		
		return result;
		
	}


	public GeneriqueConnexionService getService ( )
	{

		return service;
	}

	public void setService ( GeneriqueConnexionService service )
	{

		this.service = service;
	}

	public String getCelluledepart ( )
	{

		return celluledepart;
	}

	public void setCelluledepart ( String celluledepart )
	{

		this.celluledepart = celluledepart;
	}

	public String getNiveauarrivee ( )
	{

		return niveauarrivee;
	}

	public void setNiveauarrivee ( String niveauarrivee )
	{

		this.niveauarrivee = niveauarrivee;
	}

	public String getTypevue ( )
	{

		return typevue;
	}

	public void setTypevue ( String typevue )
	{

		this.typevue = typevue;
	}

	public List<ClsOrgCellule> getAllCellules ( )
	{

		return allCellules;
	}

	public void setAllCellules ( List<ClsOrgCellule> allCellules )
	{

		this.allCellules = allCellules;
	}

	public String getStrLongueurLibelle ( )
	{

		return strLongueurLibelle;
	}

	public void setStrLongueurLibelle ( String strLongueurLibelle )
	{

		this.strLongueurLibelle = strLongueurLibelle;
	}

}
