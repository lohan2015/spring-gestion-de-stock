package com.kinart.organisation.business.services;

import com.kinart.organisation.business.vo.ClsParametreOrganigrammeVO;
import org.apache.commons.lang3.StringUtils;

public class ClsLienCellule
{
	private String racineImage = null;
	

	public ClsLienCellule(String racineImage)
	{
		super();
		this.racineImage = racineImage;
	}

	public String getLienRevaloriser( String msgRevaloriser, String codeLibelleNiveau)
	{
		String img = "<img src=\"" + racineImage	+ "images/application/bulletin.gif\"  width=\"16\" height=\"16\" border=\"0\" title=\""+msgRevaloriser+"\">";
		String txt = "<a href=\"#\" onclick=\"javascript:revaloriser(" + codeLibelleNiveau + ")\">"+img+"</a>";
		
		return txt;
	}
	
	public String getLienSaisirPrestataire(String nomImagePrestataire, String msgSaisirPrestataire, String codelibelle,String bPrestataire )
	{
		String img="<img src=\"" + racineImage + "images/application/" + nomImagePrestataire
		+ "\" width=\"18\" height=\"18\" border=\"0\" title=\""+msgSaisirPrestataire+"\">";
		String txt="<a href=\"#\"  onclick=\"javascript:editRhpexterne2(" + codelibelle + ")\">" +img+"</a>" ;
		if(StringUtils.equalsIgnoreCase("O", bPrestataire))
			return txt;
		else
			return img;
	}
	
	public String getLienAffecterPoste(String msgAffecterPoste, String codelibelle)
	{
		String img="<img src=\"" + racineImage
		+ "images/application/affecterposte.gif\" width=\"18\" height=\"18\" border=\"0\" title=\""+msgAffecterPoste+"\" >";
		String txt = "<a href=\"#\" onclick=\"javascript:affecterposte(" + codelibelle + ")\">"+img+"</a>";
		
		return txt;
	}
	
	public String getLienAjouterFils(String messageAjouterFils,String codelibelle)
	{
		String img="<img src=\"" + racineImage
		+ "images/application/fils1.gif\" border=\"0\" title=\""+messageAjouterFils+"\">";
		String txt="<a href=\"javascript:ajouterfils(" + codelibelle + ")\">"+img+"</a>";
		
		return txt;
	}
	
	public String getLienModifierOrganigramme(String messageModifierOrganigramme, String codelibelleniveau)
	{
		String img="<img src=\"" + racineImage + "images/application/modifier.gif\" border=\"0\" title=\""+messageModifierOrganigramme+"\" >";
		String txt="<a href=\"#\" onclick=\"javascript:modifierorganigramme(" + codelibelleniveau + ")\">"+img+"</a>";		
		
		return txt;
	}
	
	public String getLienSupprimerOrganigramme(String messageSupprimerOrganigramme, String codelibellenbrefils)
	{
		String img="<img src=\"" + racineImage
						+ "images/application/supprimer.gif\" border=\"0\" title=\""+messageSupprimerOrganigramme+"\" >";
		String txt="<a href=\"#\" onclick=\"javascript:supprimerorganigramme(" + codelibellenbrefils + ")\">"+img+"</a>";
		
		return txt;
	}
	
	public String getLienAffecterAgent(String messageAffecterAgent,String codelibelle, String codePoste)
	{
		String img= "<img src=\"" + racineImage+"images/application/agents.png\" width=\"16\" height=\"16\" border=\"0\" title=\""+messageAffecterAgent+"\" >";
		String txt="<a href=\"#\" onclick=\"javascript:affecteragent("+codelibelle+","+  ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR +codePoste+ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR + ")\">"
								+img+"</a>";
		

		return txt;
	}
	
	public String getLienDesactiverPoste(String messageDesaffecterPoste, String orgposte)
	{
		String img="<img src=\"" + racineImage + "images/application/affecterposte.gif\" width=\"18\" height=\"18\" border=\"0\" title=\""
		+ messageDesaffecterPoste + "\">";
		String txt="<a href=\"#\" onclick=\"javascript:desaffecterposte(" + orgposte + ")\">"+img+"</a>";
		
		return txt;
	}
	
	public String getLienConsulterPoste(String messageConsulterPoste, String codePoste)
	{
		String txt="<a href=\"#\"><img src=\"" + racineImage
		+ "images/application/consulterposte.gif\" border=\"0\" title=\""+messageConsulterPoste+"\" onclick=\"javascript:consulterposte(" + ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR + codePoste
		+ ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR + ")\"></a>";
		return txt;
	}
	
	public String getLienAppercuAgent(String strPath,String matricule, String nomagent)
	{
		String txt="<a href=\"#\"><img src=\"" + strPath
		+ "\" border=\"0\" align=\"middle\" width=\"70\" height=\"70\"  onclick=\"javascript:appercuagent(" + ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR + matricule
		+ ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR + ")\" title=\"" + nomagent + "\"></a>";
		return txt;
	}
	
	public String getLienAppercuAnnuaire(String matricule, String nomagent)
	{
		String txt="<a href=\"#\"><img src=\"" + racineImage + "common/images/telephone.png"
		+ "\" border=\"0\" align=\"middle\" width=\"16\" height=\"16\"  onclick=\"javascript:appercuannuaire(" + ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR + matricule
		+ ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR + ")\" title=\"" + nomagent + "\"></a>";
		return txt;
	}
}
