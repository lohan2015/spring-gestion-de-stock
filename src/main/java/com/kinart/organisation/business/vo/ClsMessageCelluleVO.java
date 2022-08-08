package com.kinart.organisation.business.vo;

import com.kinart.paie.business.services.utils.ClsTreater;

import javax.servlet.http.HttpServletRequest;

public class ClsMessageCelluleVO
{
	private HttpServletRequest request;

	public ClsMessageCelluleVO(HttpServletRequest request)
	{
		super();
		this.request = request;
	}

	public String getDesaffecterLePosteDeLaCellule()
	{
		return ClsTreater._getResultat("Desaffecter le poste de la cellule", "INF-80536", false).getLibelle().replaceAll(ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR_REPLACEMENT,
				ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR);
	}
	
	public String getConsulterAnnuaire()
	{
		return ClsTreater._getResultat("Consulter Annuaire", "INF-70-RH-156", false).getLibelle().replaceAll(ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR_REPLACEMENT, ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR);
	}

	public String getRevaloriser()
	{
		return ClsTreater._getResultat("Revaloriser", "INF-80533", false).getLibelle().replaceAll(ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR_REPLACEMENT, ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR);
	}

	public String getSaisirEffectif()
	{
		return ClsTreater._getResultat("Saisir Effectif", "INF-80168", false).getLibelle().replaceAll(ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR_REPLACEMENT, ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR);
	}

	public String getSaisirPrestataire()
	{
		return ClsTreater._getResultat("Saisir Prestataire", "INF-80167", false).getLibelle().replaceAll(ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR_REPLACEMENT, ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR);
	}

	public String getAffecterAgent()
	{
		return ClsTreater._getResultat("Affecter un agent à ce poste", "INF-80502", false).getLibelle().replaceAll(ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR_REPLACEMENT,
				ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR);
	}

	public String getAffecterPoste()
	{
		return ClsTreater._getResultat("Affecter un poste", "INF-80530", false).getLibelle().replaceAll(ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR_REPLACEMENT, ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR);
	}

	public String getAjouterFils()
	{
		return ClsTreater._getResultat("Ajouter Fils", "INF-80022", false).getLibelle().replaceAll(ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR_REPLACEMENT, ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR);
	}

	public String getModifierOrganigramme()
	{
		return ClsTreater._getResultat("Modifier", "INF-00215", false).getLibelle().replaceAll(ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR_REPLACEMENT, ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR);
	}

	public String getSupprimerOrganigramme()
	{
		return ClsTreater._getResultat("Supprimer", "INF-51123", false).getLibelle().replaceAll(ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR_REPLACEMENT, ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR);
	}

	public String getConsulterPoste()
	{
		return ClsTreater._getResultat("Consulter Poste", "INF-80529", false).getLibelle().replaceAll(ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR_REPLACEMENT, ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR);
	}
	
	public String getMessageLienHautBas(String codeOrganigramme, String codeNiveau)
	{
		return ClsTreater._getResultat("Afficher à partir de % jusqu'au niveau %", "INF-80557", false, codeOrganigramme, codeNiveau).getLibelle().replaceAll(ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR_REPLACEMENT, ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR);
	}

	public String getNomPrestataire(int nombrePrestataire)
	{
		return ClsTreater._getResultat("% externe(s) rattachà(s) à ce poste", "INF-80556", false, String.valueOf(nombrePrestataire)).getLibelle().replaceAll(
				ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR_REPLACEMENT, ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR);
	}

	public HttpServletRequest getRequest()
	{
		return request;
	}

	public void setRequest(HttpServletRequest request)
	{
		this.request = request;
	}

}
