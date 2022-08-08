package com.kinart.organisation.business.vo;


public class ClsLienOrganigrammeVO
{
	private ClsParametreOrganigrammeVO param;
	
	private ClsMessageCelluleVO message;
	
	public ClsLienOrganigrammeVO(ClsParametreOrganigrammeVO param,  ClsMessageCelluleVO message)
	{
		this.param = param;
		this.message = message;
	}
	public String getLienHaut(String codeOrganigramme, String codeNiveau)
	{
		String codelibelleniveau = ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR + codeOrganigramme + ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR + ","
		+ ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR + codeNiveau + ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR;
			return "<a class=\"nonsouligne\" href=\"#\" title=\""+message.getMessageLienHautBas(codeOrganigramme, codeNiveau)+"\" onclick=\"rechargerPage("+codelibelleniveau+") \"><img src=\""+param.racineImage+"images/application/fleche-haut.gif\" border=\"0\"></a><br>";
	}

	public String getLienBas(String codeOrganigramme, String codeNiveau)
	{			
			String codelibelleniveau = ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR + codeOrganigramme + ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR + ","
			+ ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR + codeNiveau + ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR;
				return "<a class=\"nonsouligne\" href=\"#\" title=\""+message.getMessageLienHautBas(codeOrganigramme, codeNiveau)+"\" onclick=\"rechargerPage("+codelibelleniveau+") \"><img src=\""+param.racineImage+"images/application/fleche-bas.gif\" border=\"0\"></a><br>";
	}
	
//	public String getLienHaut(String codeOrganigramme, String codeNiveau)
//	{
//		if(! param.isIceface())
//			return "<a class=\"nonsouligne\" href=\"frmRHListeOrganigramme.html?codeorganigramme=" + codeOrganigramme+ "&codeniveau=" +codeNiveau
//			+ "\"><img src=\""+param.racineImage+"img/fleche-haut.gif\" border=\"0\"></a><br>";
//		
//		String value = codeOrganigramme + "-SEP-" + codeNiveau;
//		String strLienHaut = "<h:commandLink charset=\"utf-8\" style=\"text-decoration : none;\">";
//		strLienHaut += "<h:graphicImage   actionListener=\"#{uiorganigramme.lienHautEvent}\"	action=\"lienHaut\"  url=\""+param.racineImage+"img/fleche-haut.gif\" height=\"16\" 	width=\"16\" border=\"0\" value=\"" + value + "\"></h:graphicImage>";
//		strLienHaut += "</h:commandLink>";
//		strLienHaut += "<br />";
//		return strLienHaut;
//	}
//
//	public String getLienBas(String codeOrganigramme, String codeNiveau)
//	{
//		if(! param.isIceface())
//			return "<a class=\"nonsouligne\" href=\"frmRHListeOrganigramme.html?codeorganigramme=" + codeOrganigramme+ "&codeniveau=" + codeNiveau
//			+ "\"><img src=\""+param.racineImage+"img/fleche-bas.gif\" border=\"0\"></a>";
//		
//		String value = codeOrganigramme + "-SEP-" + codeNiveau;
//		String strLienBas = "<h:commandLink charset=\"utf-8\" style=\"text-decoration : none;\">";
//		strLienBas += "<h:graphicImage 	actionListener=\"#{uiorganigramme.lienBasEvent}\"	action=\"lienBas\" 	url=\""+param.racineImage+"img/fleche-bas.gif\" height=\"16\" 	width=\"16\" border=\"0\" value=\"" + value + "\"></h:graphicImage>";
//		strLienBas += "</h:commandLink>";
//		strLienBas += "<br />";
//		return strLienBas;
//	}
}
