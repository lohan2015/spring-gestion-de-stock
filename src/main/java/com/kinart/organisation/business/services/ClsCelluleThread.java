package com.kinart.organisation.business.services;

import com.kinart.organisation.business.vo.ClsTemplate;
import com.kinart.paie.business.services.utils.ParameterUtil;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ClsCelluleThread extends ClsAbsCellule implements ClsCelluleService{
	
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
	
	public Integer getNbreFilsTotal() {
		return nbreFilsTotal;
	}

	public void setNbreFilsTotal(Integer nbreFilsTotal) {
		this.nbreFilsTotal = nbreFilsTotal;
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

	public ClsCelluleThread()
	{
		
	}
	
	public ClsCelluleThread(String flowcstr)
	{
	 this.setFlowchartstring(flowcstr);	
	}
	
	public String getFlowchartstring() {
		return flowchartstring;
	}

	public void setFlowchartstring(String flowchartstring) {
		this.flowchartstring = flowchartstring;
	}

	public ClsCelluleThread(HttpServletRequest _request, String strCodeorganigramme, String strLibelle, String strCodeniveau, String strMatriculeagent, String strColor, String strCodePoste)
	{
		this.setCodeorganigramme(strCodeorganigramme);
		this.setLibelle(strLibelle);
		this.setCodeniveau(strCodeniveau);
		this.setMatriculeagent(strMatriculeagent);		
		this.setColor(strColor);
		this.setCodeposte(strCodePoste);
		this.setRequest(_request);
	}
	
	public boolean addFils(ClsCelluleThread _oCellule)
	{
		return this.getOListefils2().add(_oCellule);
	}
	
	public boolean addAllFils(List<ClsCelluleThread> _oCelluleCollection)
	{
		return this.getOListefils2().addAll(_oCelluleCollection);
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public List<ClsCelluleThread> getOListefils2() {
		return oListefils2;
	}

	public void setOListefils2(List<ClsCelluleThread> listefils) {
		oListefils2 = listefils;
	}
	
//	 Plot flow chart.
//	public String dessinerOrganigramme(ClsCelluleThread oCellulePere, HttpServletRequest request, HttpSession session)
//	{
//		String strOrganigramme = "";
//		
//		try
//		{
//			if(oCellulePere == null)
//				return strOrganigramme;
//			
//			strOrganigramme = ClsTemplate.getTemplateThread(oCellulePere);
//			if(oCellulePere.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
//				strOrganigramme = ClsTemplate.replaceString(oCellulePere.getCodeorganigramme() + oCellulePere.getLibelle(), 
//															ClsTemplate.getCell(this.getRequest(), oCellulePere.getCodeorganigramme(), oCellulePere.getLibelle(), oCellulePere.getCodeposte(), oCellulePere.getMatriculeagent(), oCellulePere.getNomagent(), oCellulePere.getCodeniveau(), oCellulePere.getNbreFils(), oCellulePere.getColor(), oCellulePere.getEnumFlowChartView()), 
//															strOrganigramme
//															);
//			else
//				strOrganigramme = ClsTemplate.replaceString(oCellulePere.getCodeorganigramme() + oCellulePere.getLibelle(), 
//						ClsTemplate.getCellList(oCellulePere.getCategorie(), oCellulePere.getCategoriePoste(), oCellulePere.getLibelle(), oCellulePere.getNbreFils().toString(), oCellulePere.getNomagent(), oCellulePere.getMatriculeagent(), oCellulePere.getColor()), 
//						strOrganigramme
//						);
//				
//			if(oCellulePere.getOListefils2().size() > 0)
//			{
//				for(int i = 0; i < oCellulePere.getOListefils2().size(); i++)
//				{
//					ClsDate dtStartDate = new ClsDate(new Date(), "yyyy/MM/dd hh:mm:ss");
//					session.setAttribute(ClsAbsCellule.STR_START_DATE, dtStartDate.getDateS());
////					this.setNbreCellTreat(this.getNbreCellTreat() + 1);
////					Integer percentDone = new Integer((int)((this.getNbreCellTreat() / (this.getNbreCell()))*100)+1);
////					session.setAttribute(ClsAbsCellule.STR_PERCENT_DONE, percentDone);
//					strOrganigramme = dessinerOrganigrammeFils(oCellulePere.getOListefils2().get(i), strOrganigramme, request, session);
//					Thread.sleep(ClsAbsCellule._lgTimerSleep_2);
//				}
//			}
//			
//			return strOrganigramme;
//		}
//		catch(Exception e)
//		{
//			return strOrganigramme;
//		}
//		finally
//		{
////			if(oCellulePere.getOListefils2().size() > 0)
////				oCellulePere.getOListefils2().clear(); 
////			oCellulePere.setOListefils2(null);
////			oCellulePere = null;
//			ClsTools.__invokeGarbageCollection();
//		}
//	}	
////	 Plot flow chart.
//	public String dessinerOrganigrammeFils(ClsCelluleThread oCellule, String strOrganigramme, HttpServletRequest request, HttpSession session)
//	{
//		try
//		{
//			if(oCellule == null)
//				return strOrganigramme;
//			
//			if(oCellule.getOListefils2().size() == 0)
//			{
//				// Ajouter
//				strOrganigramme = ClsTemplate.replaceString(oCellule.getCodeorganigramme() + oCellule.getLibelle(), ClsTemplate.getTemplateThread(oCellule), strOrganigramme);
//				
//				if(oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
//					strOrganigramme = ClsTemplate.replaceString(oCellule.getCodeorganigramme() + oCellule.getLibelle(), 
//																ClsTemplate.getCell(this.getRequest(), oCellule.getCodeorganigramme(), oCellule.getLibelle(), oCellule.getCodeposte(), oCellule.getMatriculeagent(), oCellule.getNomagent(), oCellule.getCodeniveau(), oCellule.getNbreFils(), oCellule.getColor(), oCellule.getEnumFlowChartView()), 
//																strOrganigramme
//																);
//				else
//					strOrganigramme = ClsTemplate.replaceString(oCellule.getCodeorganigramme() + oCellule.getLibelle(), 
//							ClsTemplate.getCellList(oCellule.getCategorie(), oCellule.getCategoriePoste(), oCellule.getLibelle(), oCellule.getNbreFils().toString(), oCellule.getNomagent(), oCellule.getMatriculeagent(), oCellule.getColor()), 
//							strOrganigramme
//							);
//				return strOrganigramme;
//			}
//			else
//			{
//				strOrganigramme = ClsTemplate.replaceString(oCellule.getCodeorganigramme() + oCellule.getLibelle(), ClsTemplate.getTemplateThread(oCellule), strOrganigramme);
//				if(oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
//					strOrganigramme = ClsTemplate.replaceString(oCellule.getCodeorganigramme() + oCellule.getLibelle(), 
//																ClsTemplate.getCell(this.getRequest(), oCellule.getCodeorganigramme(), oCellule.getLibelle(), oCellule.getCodeposte(), oCellule.getMatriculeagent(), oCellule.getNomagent(), oCellule.getCodeniveau(), oCellule.getNbreFils(), oCellule.getColor(), oCellule.getEnumFlowChartView()), 
//																strOrganigramme
//																);
//				else
//					strOrganigramme = ClsTemplate.replaceString(oCellule.getCodeorganigramme() + oCellule.getLibelle(), 
//							ClsTemplate.getCellList(oCellule.getCategorie(), oCellule.getCategoriePoste(), oCellule.getLibelle(), oCellule.getNbreFils().toString(), oCellule.getNomagent(), oCellule.getMatriculeagent(), oCellule.getColor()), 
//							strOrganigramme
//							);
//			}
//						
//			if(oCellule.getOListefils2().size() > 0)
//			{
//				for(int i = 0; i < oCellule.getOListefils2().size(); i++)
//				{
//					ClsDate dtStartDate = new ClsDate(new Date(), "yyyy/MM/dd hh:mm:ss");
//					session.setAttribute(ClsAbsCellule.STR_START_DATE, dtStartDate.getDateS());
//////					this.setNbreCellTreat(this.getNbreCellTreat() + 1);
//////					Integer percentDone = new Integer((int)((this.getNbreCellTreat() / (this.getNbreCell()))*100)+1);
//////					session.setAttribute(ClsAbsCellule.STR_PERCENT_DONE, percentDone);
//					strOrganigramme = dessinerOrganigrammeFils(oCellule.getOListefils2().get(i), strOrganigramme, request, session);
//					Thread.sleep(ClsAbsCellule._lgTimerSleep_2);
//				}
//			}
//			
//			return strOrganigramme;
//		}
//		catch(Exception e)
//		{
//			return strOrganigramme;
//		}
//		finally
//		{
//			ClsTools.__invokeGarbageCollection();
//		}
//	}
	
	// Plot flow chart.
	public String dessinerOrganigramme(ClsCelluleThread oCellulePere, HttpServletRequest request, HttpSession session, String strBorderSize)
	{
		String strOrganigramme = "";
		
		try
		{
			if(oCellulePere == null)
				return strOrganigramme;
			
			strOrganigramme = ClsTemplate.getTemplateThread(oCellulePere);
			/**
			 * On supprime le tiret en entete de la premiere cellule
			 */
			//strOrganigramme = strOrganigramme.replaceFirst(ClsTemplate.TIRET_ENTETE_CELLULE, ClsTemplate.TIRET_ENTETE_PREMIERE_CELLULE);
			
			if(oCellulePere.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
				strOrganigramme = ClsTemplate.replaceString(ClsTemplate.STR_SPECIFIC_CHAR + oCellulePere.getCodeorganigramme() + ClsTemplate.STR_SPECIFIC_CHAR, 
															ClsTemplate.getCell(this.getRequest(), oCellulePere.getCodeorganigramme(), oCellulePere.getLibelle(), oCellulePere.getCodeposte(), oCellulePere.getMatriculeagent(), oCellulePere.getNomagent(), oCellulePere.getCodeniveau(), oCellulePere.getNbreFils(), oCellulePere.getColor(), oCellulePere.getEnumFlowChartView(), strBorderSize, oCellulePere.getBprest(), oCellulePere.getBcasefic()), 
															strOrganigramme
															);
			else
				strOrganigramme = ClsTemplate.replaceString(ClsTemplate.STR_SPECIFIC_CHAR + oCellulePere.getCodeorganigramme() + ClsTemplate.STR_SPECIFIC_CHAR, 
						ClsTemplate.getCellList(oCellulePere.getCategorie(), oCellulePere.getCategoriePoste(), oCellulePere.getLibelle(), oCellulePere.getNbreFils().toString(), oCellulePere.getNomagent(), oCellulePere.getMatriculeagent(), oCellulePere.getColor(), strBorderSize, oCellulePere.getBcasefic()), 
						strOrganigramme
						);
				
			if(oCellulePere.getOListefils2().size() > 0)
			{
				for(int i = 0; i < oCellulePere.getOListefils2().size(); i++)
				{
//					ClsDate dtStartDate = new ClsDate(new Date(), "yyyy/MM/dd hh:mm:ss");
//					session.setAttribute(ClsAbsCellule.STR_START_DATE, dtStartDate.getDateS());
////					this.setNbreCellTreat(this.getNbreCellTreat() + 1);
////					Integer percentDone = new Integer((int)((this.getNbreCellTreat() / (this.getNbreCell()))*100)+1);
////					session.setAttribute(ClsAbsCellule.STR_PERCENT_DONE, percentDone);
					strOrganigramme = dessinerOrganigrammeFils(oCellulePere.getOListefils2().get(i), strOrganigramme, request, session, strBorderSize);
//					Thread.sleep(ClsAbsCellule._lgTimerSleep_2);
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
//			if(oCellulePere.getOListefils2().size() > 0)
//				oCellulePere.getOListefils2().clear(); 
//			oCellulePere.setOListefils2(null);
//			oCellulePere = null;
			ParameterUtil.__invokeGarbageCollection();
		}
	}	
//	 Plot flow chart.
	public String dessinerOrganigrammeFils(ClsCelluleThread oCellule, String strOrganigramme, HttpServletRequest request, HttpSession session, String strBorderSize)
	{
		try
		{
			if(oCellule == null)
				return strOrganigramme;
			
			if(oCellule.getOListefils2().size() == 0)
			{
				if(oCellule.getBcasefic() != null && oCellule.getBcasefic().equalsIgnoreCase("O"))
				{
//					if(oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
//					{
						strOrganigramme = ClsTemplate.replaceString(ClsTemplate.STR_SPECIFIC_CHAR + oCellule.getCodeorganigramme() + ClsTemplate.STR_SPECIFIC_CHAR, "", strOrganigramme);
						return strOrganigramme;
//					}
				}
				
				// Ajouter
				strOrganigramme = ClsTemplate.replaceString(ClsTemplate.STR_SPECIFIC_CHAR + oCellule.getCodeorganigramme() + ClsTemplate.STR_SPECIFIC_CHAR, ClsTemplate.getTemplateThread(oCellule), strOrganigramme);
				
				if(oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
					strOrganigramme = ClsTemplate.replaceString(ClsTemplate.STR_SPECIFIC_CHAR + oCellule.getCodeorganigramme() + ClsTemplate.STR_SPECIFIC_CHAR, 
																ClsTemplate.getCell(this.getRequest(), oCellule.getCodeorganigramme(), oCellule.getLibelle(), oCellule.getCodeposte(), oCellule.getMatriculeagent(), oCellule.getNomagent(), oCellule.getCodeniveau(), oCellule.getNbreFils(), oCellule.getColor(), oCellule.getEnumFlowChartView(), strBorderSize, oCellule.getBprest(), oCellule.getBcasefic()), 
																strOrganigramme
																);
				else
					strOrganigramme = ClsTemplate.replaceString(ClsTemplate.STR_SPECIFIC_CHAR + oCellule.getCodeorganigramme() + ClsTemplate.STR_SPECIFIC_CHAR, 
							ClsTemplate.getCellList(oCellule.getCategorie(), oCellule.getCategoriePoste(), oCellule.getLibelle(), oCellule.getNbreFils().toString(), oCellule.getNomagent(), oCellule.getMatriculeagent(), oCellule.getColor(), strBorderSize, oCellule.getBcasefic()), 
							strOrganigramme
							);
				return strOrganigramme;
			}
			else
			{
				strOrganigramme = ClsTemplate.replaceString(ClsTemplate.STR_SPECIFIC_CHAR + oCellule.getCodeorganigramme() + ClsTemplate.STR_SPECIFIC_CHAR, ClsTemplate.getTemplateThread(oCellule), strOrganigramme);
				if(oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
					strOrganigramme = ClsTemplate.replaceString(ClsTemplate.STR_SPECIFIC_CHAR + oCellule.getCodeorganigramme() + ClsTemplate.STR_SPECIFIC_CHAR, 
																ClsTemplate.getCell(this.getRequest(), oCellule.getCodeorganigramme(), oCellule.getLibelle(), oCellule.getCodeposte(), oCellule.getMatriculeagent(), oCellule.getNomagent(), oCellule.getCodeniveau(), oCellule.getNbreFils(), oCellule.getColor(), oCellule.getEnumFlowChartView(), strBorderSize, oCellule.getBprest(), oCellule.getBcasefic()), 
																strOrganigramme
																);
				else
					strOrganigramme = ClsTemplate.replaceString(ClsTemplate.STR_SPECIFIC_CHAR + oCellule.getCodeorganigramme() + ClsTemplate.STR_SPECIFIC_CHAR, 
							ClsTemplate.getCellList(oCellule.getCategorie(), oCellule.getCategoriePoste(), oCellule.getLibelle(), oCellule.getNbreFils().toString(), oCellule.getNomagent(), oCellule.getMatriculeagent(), oCellule.getColor(), strBorderSize, oCellule.getBcasefic()), 
							strOrganigramme
							);
			}
						
			if(oCellule.getOListefils2().size() > 0)
			{
				for(int i = 0; i < oCellule.getOListefils2().size(); i++)
				{
//					ClsDate dtStartDate = new ClsDate(new Date(), "yyyy/MM/dd hh:mm:ss");
//					session.setAttribute(ClsAbsCellule.STR_START_DATE, dtStartDate.getDateS());
//					this.setNbreCellTreat(this.getNbreCellTreat() + 1);
//					Integer percentDone = new Integer((int)((this.getNbreCellTreat() / (this.getNbreCell()))*100)+1);
//					session.setAttribute(ClsAbsCellule.STR_PERCENT_DONE, percentDone);
					strOrganigramme = dessinerOrganigrammeFils(oCellule.getOListefils2().get(i), strOrganigramme, request, session, strBorderSize);
//					Thread.sleep(ClsAbsCellule._lgTimerSleep_2);
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
