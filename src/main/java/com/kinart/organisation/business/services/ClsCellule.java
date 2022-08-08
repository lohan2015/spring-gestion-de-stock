package com.kinart.organisation.business.services;

import com.kinart.organisation.business.vo.ClsTemplate;
import com.kinart.paie.business.services.utils.ClsDate;
import com.kinart.paie.business.services.utils.ParameterUtil;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ClsCellule extends ClsAbsCellule {
	
	public boolean isFirstcellule ( ) {
		
		return firstcellule;
	}
	
	public void setFirstcellule ( boolean firstcellule ) {
	
		this.firstcellule = firstcellule;
	}
	
	public String getLibellecourt() {
		return libellecourt;
	}
	public void setLibellecourt(String libellecourt) {
		this.libellecourt = libellecourt;
	}
	
	public String getNivfonction() {
		return nivfonction;
	}
	public void setNivfonction(String nivfonction) {
		this.nivfonction = nivfonction;
	}
	
	
	public String getCodesite() {
		return codesite;
	}
	public void setCodesite(String codesite) {
		this.codesite = codesite;
	}
	
	
	public String getBcasefic() {
		return bcasefic;
	}

	public void setBcasefic(String bcasefic) {
		this.bcasefic = bcasefic;
	}

	public String getBprest() {
		return bprest;
	}

	public void setBprest(String bprest) {
		this.bprest = bprest;
	}

	public EnumFlowChartView getEnumFlowChartView() {
		return enumFlowChartView;
	}

	public void setEnumFlowChartView(EnumFlowChartView enumFlowChartView) {
		this.enumFlowChartView = enumFlowChartView;
	}

	public int getNbreCell() {
		return nbreCell;
	}

	public void setNbreCell(int nbreCell) {
		this.nbreCell = nbreCell;
	}

	public int getNbreCellTreat() {
		return nbreCellTreat;
	}

	public void setNbreCellTreat(int nbreCellTreat) {
		this.nbreCellTreat = nbreCellTreat;
	}

	public String getCategoriePoste() {
		return categoriePoste;
	}

	public void setCategoriePoste(String categoriePoste) {
		this.categoriePoste = categoriePoste;
	}

	public String getCategorie() {
		return categorie;
	}

	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}

	public EnumTypeOrganigramme getEnumOrganigrammeType() {
		return enumOrganigrammeType;
	}

	public void setEnumOrganigrammeType(EnumTypeOrganigramme enumOrganigrammeType) {
		this.enumOrganigrammeType = enumOrganigrammeType;
	}

	public Integer getNbreFils() {
		return nbreFils;
	}

	public void setNbreFils(Integer nbreFils) {
		this.nbreFils = nbreFils;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String getNomagent() {
		return nomagent;
	}

	public void setNomagent(String nomagent) {
		this.nomagent = nomagent;
	}

	public ClsCellule()
	{
		
	}
	
	public ClsCellule(HttpServletRequest _request, String strCodeorganigramme, String strLibelle, String strCodeniveau, String strMatriculeagent, String strColor, String strCodePoste)
	{
		this.setCodeorganigramme(strCodeorganigramme);
		this.setLibelle(strLibelle);
		this.setCodeniveau(strCodeniveau);
		this.setMatriculeagent(strMatriculeagent);		
		this.setColor(strColor);
		this.setCodeposte(strCodePoste);
		this.setRequest(_request);
	}
	
	public boolean addFils(ClsCellule _oCellule)
	{
		return this.getOListefils().add(_oCellule);
	}
	
	public boolean addAllFils(List<ClsCellule> _oCelluleCollection)
	{
		return this.getOListefils().addAll(_oCelluleCollection);
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public List<ClsCellule> getOListefils() {
		return oListefils;
	}

	public void setOListefils(List<ClsCellule> listefils) {
		oListefils = listefils;
	}
	
	// Plot flow chart.
	public String dessinerOrganigramme(ClsCellule oCellulePere, HttpServletRequest request, HttpSession session, String strBorderSize)
	{
		String strOrganigramme = "";
		
		try
		{
			if(oCellulePere == null)
				return strOrganigramme;
			
			strOrganigramme = ClsTemplate.getTemplate(oCellulePere);
			
			/**
			 * On supprime le tiret en entete de la premiere cellule
			 */
			
			strOrganigramme = strOrganigramme.replaceFirst(ClsTemplate.TIRET_ENTETE_CELLULE, ClsTemplate.TIRET_ENTETE_PREMIERE_CELLULE);
			
			if(oCellulePere.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
				strOrganigramme = ClsTemplate.replaceString(oCellulePere.getCodeorganigramme() + oCellulePere.getLibelle(), 
															ClsTemplate.getCell(this.getRequest(), oCellulePere.getCodeorganigramme(), oCellulePere.getLibelle(), oCellulePere.getCodeposte(), oCellulePere.getMatriculeagent(), oCellulePere.getNomagent(), oCellulePere.getCodeniveau(), oCellulePere.getNbreFils(), oCellulePere.getColor(), oCellulePere.getEnumFlowChartView(), strBorderSize, oCellulePere.getBprest(), oCellulePere.getBcasefic()), 
															strOrganigramme
															);
			else
				strOrganigramme = ClsTemplate.replaceString(oCellulePere.getCodeorganigramme() + oCellulePere.getLibelle(), 
						ClsTemplate.getCellList(oCellulePere.getCategorie(), oCellulePere.getCategoriePoste(), oCellulePere.getLibelle(), oCellulePere.getNbreFils().toString(), oCellulePere.getNomagent(), oCellulePere.getMatriculeagent(), oCellulePere.getColor(), strBorderSize, oCellulePere.getBcasefic()), 
						strOrganigramme
						);
				
			if(oCellulePere.getOListefils().size() > 0)
			{
				for(int i = 0; i < oCellulePere.getOListefils().size(); i++)
				{
					ClsDate dtStartDate = new ClsDate(new Date(), "yyyy/MM/dd hh:mm:ss");
					session.setAttribute(ClsAbsCellule.STR_START_DATE, dtStartDate.getDateS());
//					this.setNbreCellTreat(this.getNbreCellTreat() + 1);
//					Integer percentDone = new Integer((int)((this.getNbreCellTreat() / (this.getNbreCell()))*100)+1);
//					session.setAttribute(ClsAbsCellule.STR_PERCENT_DONE, percentDone);
					strOrganigramme = dessinerOrganigrammeFils(oCellulePere.getOListefils().get(i), strOrganigramme, request, session, strBorderSize);
				}
			}
			
			return strOrganigramme;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return strOrganigramme;
		}
		finally
		{
			if(oCellulePere.getOListefils().size() > 0)
				oCellulePere.getOListefils().clear(); 
			oCellulePere.setOListefils(null);
			oCellulePere = null;
			ParameterUtil.__invokeGarbageCollection();
		}
	}	
//	 Plot flow chart.
	public String dessinerOrganigrammeFils(ClsCellule oCellule, String strOrganigramme, HttpServletRequest request, HttpSession session, String strBorderSize)
	{
		ParameterUtil.println("..........................."+"Après chargement initial de l'organigramme, nbr Fils cellule du fils = "+oCellule.getLibelle()+" = " +oCellule.getNbreFils());
		try
		{
			if(oCellule == null)
				return strOrganigramme;
			
			if(oCellule.getOListefils().size() == 0)
			{
				if(oCellule.getBcasefic() != null && oCellule.getBcasefic().equalsIgnoreCase("O"))
				{
					strOrganigramme = ClsTemplate.replaceString(oCellule.getCodeorganigramme() + oCellule.getLibelle(), "", strOrganigramme);
					return strOrganigramme;
				}
				
				strOrganigramme = ClsTemplate.replaceString(oCellule.getCodeorganigramme() + oCellule.getLibelle(), ClsTemplate.getTemplate(oCellule), strOrganigramme);
				if(oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
					strOrganigramme = ClsTemplate.replaceString(oCellule.getCodeorganigramme() + oCellule.getLibelle(), 
																ClsTemplate.getCell(this.getRequest(), oCellule.getCodeorganigramme(), oCellule.getLibelle(), oCellule.getCodeposte(), oCellule.getMatriculeagent(), oCellule.getNomagent(), oCellule.getCodeniveau(), oCellule.getNbreFils(), oCellule.getColor(), oCellule.getEnumFlowChartView(), strBorderSize, oCellule.getBprest(), oCellule.getBcasefic()), 
																strOrganigramme
																);
				else
					strOrganigramme = ClsTemplate.replaceString(oCellule.getCodeorganigramme() + oCellule.getLibelle(), 
							ClsTemplate.getCellList(oCellule.getCategorie(), oCellule.getCategoriePoste(), oCellule.getLibelle(), oCellule.getNbreFils().toString(), oCellule.getNomagent(), oCellule.getMatriculeagent(), oCellule.getColor(), strBorderSize, oCellule.getBcasefic()), 
							strOrganigramme
							);
				return strOrganigramme;
			}
			else
			{
				strOrganigramme = ClsTemplate.replaceString(oCellule.getCodeorganigramme() + oCellule.getLibelle(), ClsTemplate.getTemplate(oCellule), strOrganigramme);
				if(oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
					strOrganigramme = ClsTemplate.replaceString(oCellule.getCodeorganigramme() + oCellule.getLibelle(), 
																ClsTemplate.getCell(this.getRequest(), oCellule.getCodeorganigramme(), oCellule.getLibelle(), oCellule.getCodeposte(), oCellule.getMatriculeagent(), oCellule.getNomagent(), oCellule.getCodeniveau(), oCellule.getNbreFils(), oCellule.getColor(), oCellule.getEnumFlowChartView(), strBorderSize, oCellule.getBprest(), oCellule.getBcasefic()), 
																strOrganigramme
																);
				else
					strOrganigramme = ClsTemplate.replaceString(oCellule.getCodeorganigramme() + oCellule.getLibelle(), 
							ClsTemplate.getCellList(oCellule.getCategorie(), oCellule.getCategoriePoste(), oCellule.getLibelle(), oCellule.getNbreFils().toString(), oCellule.getNomagent(), oCellule.getMatriculeagent(), oCellule.getColor(), strBorderSize, oCellule.getBcasefic()), 
							strOrganigramme
							);
			}
						
			if(oCellule.getOListefils().size() > 0)
			{
				for(int i = 0; i < oCellule.getOListefils().size(); i++)
				{
					ClsDate dtStartDate = new ClsDate(new Date(), "yyyy/MM/dd hh:mm:ss");
					session.setAttribute(ClsAbsCellule.STR_START_DATE, dtStartDate.getDateS());
//					this.setNbreCellTreat(this.getNbreCellTreat() + 1);
//					Integer percentDone = new Integer((int)((this.getNbreCellTreat() / (this.getNbreCell()))*100)+1);
//					session.setAttribute(ClsAbsCellule.STR_PERCENT_DONE, percentDone);
					strOrganigramme = dessinerOrganigrammeFils(oCellule.getOListefils().get(i), strOrganigramme, request, session, strBorderSize);
				}
			}
			
			return strOrganigramme;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return strOrganigramme;
		}
		finally
		{
			ParameterUtil.__invokeGarbageCollection();
		}
	}

	public String getCodeniveau() {
		return codeniveau;
	}

	public void setCodeniveau(String codeniveau) {
		this.codeniveau = codeniveau;
	}

	public String getCodeorganigramme() {
		return codeorganigramme;
	}

	public void setCodeorganigramme(String codeorganigramme) {
		this.codeorganigramme = codeorganigramme;
	}

	public String getCodeposte() {
		return codeposte;
	}

	public void setCodeposte(String codeposte) {
		this.codeposte = codeposte;
	}
	
	public String getCodepere() {
		return codepere;
	}

	public void setCodepere(String codepere) {
		this.codepere = codepere;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public String getMatriculeagent() {
		return matriculeagent;
	}

	public void setMatriculeagent(String matriculeagent) {
		this.matriculeagent = matriculeagent;
	}	
	
	
	
}
