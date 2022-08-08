package com.kinart.organisation.business.services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.kinart.organisation.business.model.Organigramme;
import com.kinart.paie.business.services.utils.ClsDate;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.kinart.paie.business.services.utils.ParameterUtil;
import org.hibernate.Session;

/****************CLASSE PERMETTANT DE DESSINER L'ORGANIGRAMME A PARTIR DE LA BD**********************/
public class ClsOrganigramme extends ClsAbsCellule{

	private List<Organigramme> flowChart1 = new ArrayList<Organigramme>();
	private List<Organigramme> flowChart2 = new ArrayList<Organigramme>();
	
	
	
	public String getBprest() {
		return bprest;
	}

	public void setBprest(String bprest) {
		this.bprest = bprest;
	}

	public List<Organigramme> getFlowChart1() {
		return flowChart1;
	}

	public void setFlowChart1(List<Organigramme> flowChart1) {
		this.flowChart1 = flowChart1;
	}

	public List<Organigramme> getFlowChart2() {
		return flowChart2;
	}

	public void setFlowChart2(List<Organigramme> flowChart2) {
		this.flowChart2 = flowChart2;
	}

	public String getCdos() {
		return cdos;
	}

	public void setCdos(String cdos) {
		this.cdos = cdos;
	}

	public EnumFlowChartView getEnumFlowChartView() {
		return enumFlowChartView;
	}

	public void setEnumFlowChartView(EnumFlowChartView enumFlowChartView) {
		this.enumFlowChartView = enumFlowChartView;
	}

	public ClsCellule getOCellFather() {
		return oCellFather;
	}

	public void setOCellFather(ClsCellule cellFather) {
		oCellFather = cellFather;
	}

	public int getNbreCellTreat() {
		return nbreCellTreat;
	}

	public void setNbreCellTreat(int nbreCellTreat) {
		this.nbreCellTreat = nbreCellTreat;
	}

	public EnumTypeOrganigramme getEnumOrganigrammeType() {
		return enumOrganigrammeType;
	}

	public void setEnumOrganigrammeType(EnumTypeOrganigramme enumOrganigrammeType) {
		this.enumOrganigrammeType = enumOrganigrammeType;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public GeneriqueConnexionService getService() {
		return service;
	}

	public void setService(GeneriqueConnexionService service) {
		this.service = service;
	}

	public int getNbreCell() {
		return nbreCell;
	}

	public void setNbreCell(int nbreCell) {
		this.nbreCell = nbreCell;
	}

	public ClsOrganigramme(HttpServletRequest _request, String strCodeorganigramme, String strCodeniveau, String strCdos)
	{
		try
		{
			this.setRequest(_request);
			this.setStrSQL(this.getStrSQL() + "AND Rhorg.identreprise = " + "'" + strCdos.trim() + "'" + " AND Rhniv.identreprise = " + "'" + strCdos.trim() + "'");
			this.setSQLParameter(strCodeorganigramme, strCodeniveau);
			this.setCdos(strCdos);
//			ClsParameter.println(this.getStrSQL());
//			oDao = new DataAccesDAO(ClsParameter.STR_URL, ClsParameter.STR_USER, ClsParameter.STR_PASSWORD);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public ClsOrganigramme(GeneriqueConnexionService _service, HttpServletRequest _request, String strCodeorganigramme, String strCodeniveau, String strCdos)
	{
		try
		{
			//ClsAutorisationsUtilisateur auto = new ClsAutorisationsUtilisateur(ClsParameter.getSessionObject(_request, ClsParameter.SESSION_DOSSIER),ClsParameter.getSessionObject(_request, ClsParameter.SESSION_LOGIN),"Rhorg",ClsTypeProfilAgent.COLONNES,_service);
			String chaineauto = "1=1";//auto.getSites(null);
			
			this.setRequest(_request);
			this.setService(_service);
			this.setStrSQL(this.getStrSQL() + "AND Rhorg.identreprise = " + "'" + strCdos.trim() + "'" + " AND Rhniv.identreprise = " + "'" + strCdos.trim() + "'");
			
			this.setStrSQL(this.getStrSQL()+" and "+chaineauto);
			
//			auto = new ClsAutorisationsUtilisateur(ClsParameter.getSessionObject(request, ClsParameter.SESSION_DOSSIER),ClsParameter.getSessionObject(request, ClsParameter.SESSION_LOGIN),"AGENT",_service);
//			chaineauto = auto.getAutorisations();
//			
//			this.setStrSQL(this.getStrSQL()+" and "+chaineauto);
			ParameterUtil.println(">>>>>>>>>>>>>>>Query for organigramme = "+this.getStrSQL());
			
			this.setSQLParameter(strCodeorganigramme, strCodeniveau);
			this.setCdos(strCdos);
//			ClsParameter.println(this.getStrSQL());
//			oDao = new DataAccesDAO(ClsParameter.STR_URL, ClsParameter.STR_USER, ClsParameter.STR_PASSWORD);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	// Set sql parameter to define end level and start cell of the flow chart
	private void setSQLParameter(String strCodeorganigramme, String strCodeniveau)
	{
		try
		{
			setCodeorganigramme(strCodeorganigramme);
			
			if(strCodeorganigramme != null && strCodeorganigramme.trim().length() != 0)
			{
//				if(strCodeorganigramme.trim().length() != 0)
					this.setCodepere(strCodeorganigramme);
//				else
//					this.setCodepere(ClsParameter.STR_INIT_CODEPERE);
				
				this.setStrSQL(this.getStrSQL() + " AND Rhorg.codeorganigramme LIKE " + "'" + this.getCodepere().trim() + "%'");
			}
			else
			{
				this.setCodepere(ParameterUtil.STR_INIT_CODEPERE);
				this.setStrSQL(this.getStrSQL() + " AND Rhorg.codeorganigramme >= " + "'" + this.getCodepere().trim() + "'");
			}
			
//			this.setStrSQL(this.getStrSQL() + " AND Rhorg.codeorganigramme >= " + "'" + this.getCodepere().trim() + "'");
//			this.setStrSQL(this.getStrSQL() + " AND Rhorg.codeorganigramme LIKE " + "'" + this.getCodepere().trim() + "%'");
						
			if(strCodeniveau != null)
				if(strCodeniveau.trim().length() != 0)
					this.setStrSQL(this.getStrSQL() + " AND Rhniv.codeniveau <= " + "'" + strCodeniveau.trim() + "'");
			
			this.setStrSQL(this.getStrSQL() + " ORDER BY Rhorg.codepere, Rhorg.codeorganigramme ASC");
		}
		catch(Exception e)
		{
			
		}
	}
	
	// Get father cell that contain all soon in order of the flow chart.
	public ClsCellule getCellPere(HttpServletRequest request, HttpSession session)
	{
		Map<String, List<ClsCellule>> oCellCollection = null;
		
		try
		{
//			if(oDao == null)
//				return null;
			
//			ResultSet oRsCell = this.getService().getSession().connection().createStatement().executeQuery(this.getStrSQL());
//			if(oRsCell == null)
//				return null;
//			ClsParameter.println((new java.util.Date()) + "-------------------------------- GET CELL --------------------------");
			ClsDate dtStartDate = new ClsDate(new Date(), "yyyy/MM/dd hh:mm:ss");
			session.setAttribute(ClsAbsCellule.STR_START_DATE, dtStartDate.getDateS());
			session.setAttribute(ClsAbsCellule.STR_TASK, "Chargement des cellules...");
			oCellCollection = getCellCollection();//(oRsCell);
			if(oCellCollection == null)
				return null;
			
//			int int_Duration = (int)((2*this.getNbreCell()) /1963);
//			double dblDuration = (double)((2*this.getNbreCell()) /1963);
//			ClsParameter.println( ((double)(2*this.getNbreCell())) /((double)1963) );
			
//			session.setAttribute(ClsAbsCellule.STR_TOTAL_SIZE, this.getNbreCell());
			try {
				
				String dblDuration = String.valueOf(((double)(2*this.getNbreCell())) /((double)250));
				String strDuration = null;
				if(dblDuration.length() >= 5)
				  strDuration = dblDuration.substring(0, 5);
				else if(dblDuration.length() >= 4)
					  strDuration = dblDuration.substring(0, 4);
				else if(dblDuration.length() >= 3)
					  strDuration = dblDuration.substring(0, 3);
//				ClsParameter.println(strDuration);
//				ClsParameter.println(this.getNbreCell());
//				ClsParameter.println((2*this.getNbreCell())/250);
//				ClsParameter.println(((double)(2*this.getNbreCell())) /((double)250));
				session.setAttribute(ClsAbsCellule.STR_DURATION, strDuration + " Min.");
				dtStartDate.addMin(Integer.parseInt(strDuration.substring(0, 1)));
				String strSec = strDuration.substring(2, strDuration.length() - 1);
				if(strSec != null && strSec.trim().length() != 0)
					dtStartDate.addSecond(Integer.parseInt(strSec));
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			session.setAttribute(ClsAbsCellule.STR_END_DATE, dtStartDate.getDateS());
//			ClsParameter.println((new java.util.Date()) + "-------------------------------- GET CELL FATHER--------------------------");
			oCellPere = getOCellFather(); //this.getCellPereByCode(this.getCodepere(), oCellCollection); //getOCellFather(); 
			if(oCellPere == null)
				return null;
			
//			ClsParameter.println((new java.util.Date()) + "-------------------------------- START CONSTRUCT TREE --------------------------");
			session.setAttribute(ClsAbsCellule.STR_TASK, "Construction de l'arbre...");
			oCellPere = this.constructTree(oCellPere, oCellCollection, request, session);
			session.setAttribute(ClsAbsCellule.STR_PERCENT_DONE, 50);
			oCellPere.setNbreCell(this.getNbreCell());
//			ClsParameter.println((new java.util.Date()) + "-------------------------------- END CONSTRUCT TREE --------------------------");
			
			return oCellPere;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return oCellPere;
		}
		finally
		{
			if(oCellCollection != null)
				if(oCellCollection.size() > 0)
					oCellCollection.clear();
			
			oCellCollection = null;	
				
			this.getService().getSession().close();
			
			ParameterUtil.__invokeGarbageCollection();
		}
	}
	
	// Get specific cell by code.
	private ClsCellule getCellPereByCode(String strCode, List<ClsCellule> oCellCollection)
	{
		ClsCellule oCell = null;
		
		if(oCellCollection.size() == 0)
			return oCell;
		
		for(int index = 0; index < oCellCollection.size(); index++)
		{
			if(strCode.trim().equals(ParameterUtil.STR_INIT_CODEPERE))
			{
				if(oCellCollection.get(index).getCodepere().trim().toUpperCase().equals(strCode.trim().toUpperCase()))
				{
					oCell = oCellCollection.get(index);
					return oCell;
//					break;
				}
			}
			else
			{
				if(oCellCollection.get(index).getCodeorganigramme().trim().toUpperCase().equals(strCode.trim().toUpperCase()))
				{
					oCell = oCellCollection.get(index);
					return oCell;
//					break;
				}
			}
		}
			
		return oCell;
	}

	// Construct father tree.
	public ClsCellule constructTree(ClsCellule oCellPere, Map<String, List<ClsCellule>> oCellCollection, HttpServletRequest request, HttpSession session)
	{
		try
		{
			List<ClsCellule> oCellsFils = new ArrayList<ClsCellule>();
			
//			ClsParameter.println((new java.util.Date()) + "-------------------------------- 1 --------------------------");
			try {
				List<ClsCellule> oCellsFils_1 = oCellCollection.get(oCellPere.getCodeorganigramme());
				if(oCellsFils_1 != null)
//				{
//					ClsParameter.println("-----------------Nbre fils " + oCellPere.getCodeorganigramme() + " : " + oCellsFils_1.size());
					oCellsFils.addAll(oCellsFils_1);//this.getFilsCell(oCellPere, oCellCollection);
//				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
//			ClsParameter.println((new java.util.Date()) + "-------------------------------- 2 --------------------------");
			if(oCellsFils == null || oCellsFils.size() == 0)
				return oCellPere;
			
			if(getCodeorganigramme() == null)
			{
//				oCellPere.setNbreFils(oCellsFils.size());
				oCellPere.addAllFils(oCellsFils);
				return oCellPere;
			}
			
//			oCellPere.setNbreFils(oCellsFils.size());
			oCellCollection.remove(oCellPere.getCodeorganigramme());
//			ClsParameter.println((new java.util.Date()) + "-------------------------------- 3 --------------------------");
//			ClsParameter.println((new java.util.Date()) + "-------------------------------- 3.2 " + oCellPere.getLibelle()  + "--------------------------");
			for(int index = 0; index < oCellsFils.size(); index++)
			{
//				ClsParameter.println((new java.util.Date()) + "-------------------------------- 3.2." + index + " " + oCellsFils.get(index).getLibelle()  + "--------------------------");
//				this.setNbreCellTreat(this.getNbreCellTreat() + 1);
//				Integer percentDone = new Integer((int)((this.getNbreCellTreat() / (this.getNbreCell()))*100)+1);
//				session.setAttribute(ClsAbsCellule.STR_PERCENT_DONE, percentDone);
				if(!oCellsFils.get(index).getCodeorganigramme().equalsIgnoreCase(oCellPere.getCodeorganigramme()))
				{
					ClsDate dtStartDate2 = new ClsDate(new Date(), "yyyy/MM/dd hh:mm:ss");
					session.setAttribute(ClsAbsCellule.STR_START_DATE, dtStartDate2.getDateS());
					List<ClsCellule> oCellFils = constructTreeFils(oCellsFils.get(index), oCellCollection, String.valueOf(index), request, session);
					oCellsFils.get(index).addAllFils(oCellFils);
				}
//				oCellCollection.removeAll(oCellFils);
				oCellCollection.remove(oCellsFils.get(index));
			}
//			ClsParameter.println((new java.util.Date()) + "-------------------------------- 4 --------------------------");
			oCellPere.addAllFils(oCellsFils);
//			ClsParameter.println((new java.util.Date()) + "-------------------------------- 5 --------------------------");
			return oCellPere;
		}
		catch(Exception e)
		{
			return oCellPere;
		}
		finally
		{
			ParameterUtil.__invokeGarbageCollection();
		}
	}
	
	// Construct soon tree.
	private List<ClsCellule> constructTreeFils(ClsCellule oCellFils, Map<String, List<ClsCellule>> oCellCollection, String index2, HttpServletRequest request, HttpSession session)
	{
		List<ClsCellule> oCellsFils = new ArrayList<ClsCellule>();
		
		try
		{
			try {
				List<ClsCellule> oCellsFils_1 = oCellCollection.get(oCellFils.getCodeorganigramme());
				if(oCellsFils_1 != null)
//				{
//					ClsParameter.println("-----------------Nbre fils " + oCellPere.getCodeorganigramme() + " : " + oCellsFils_1.size());
					oCellsFils.addAll(oCellsFils_1);//this.getFilsCell(oCellPere, oCellCollection);
//				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			if(oCellsFils == null || oCellsFils.size() == 0)
			{
//				oCellCollection.remove(oCellFils);
				return oCellsFils;
			}
//			ClsParameter.println((new java.util.Date()) + "-------------------------------- 3.2." + index2 + " Nbre fils :" + oCellsFils.size()  + "--------------------------");
//			oCellFils.setNbreFils(oCellsFils.size());
			oCellCollection.remove(oCellFils.getCodeorganigramme());
			
			for(int index = 0; index < oCellsFils.size(); index++)
			{
//				ClsParameter.println((new java.util.Date()) + "-------------------------------- 3.2." + index2 + "." + index + " Nbre fils :" + oCellsFils.get(index).getLibelle()  + "--------------------------");
				if(!oCellsFils.get(index).getCodeorganigramme().equalsIgnoreCase(oCellFils.getCodeorganigramme()))
				{
					ClsDate dtStartDate2 = new ClsDate(new Date(), "yyyy/MM/dd hh:mm:ss");
					session.setAttribute(ClsAbsCellule.STR_START_DATE, dtStartDate2.getDateS());
					String indexNext = index2 + "." + String.valueOf(index);
//					this.setNbreCellTreat(this.getNbreCellTreat() + 1);
//					Integer percentDone = new Integer((int)((this.getNbreCellTreat() / (this.getNbreCell()))*100)+1);
//					session.setAttribute(ClsAbsCellule.STR_PERCENT_DONE, percentDone);
					List<ClsCellule> oCellFilsCollection = constructTreeFils(oCellsFils.get(index), oCellCollection, indexNext, request, session);
					oCellsFils.get(index).addAllFils(oCellFilsCollection);
				}
//				oCellCollection.removeAll(oCellFilsCollection);
//				oCellCollection.remove(oCellsFils.get(index));
			}
			
//			oCellCollection.remove(oCellFils);
			
			return oCellsFils;
		}
		catch(Exception e)
		{
			return oCellsFils;
		}
		finally
		{
			ParameterUtil.__invokeGarbageCollection();
		}
	}
	
	// Get specific cell by code.
	private List<ClsCellule> getFilsCell(ClsCellule oCell, List<ClsCellule> oCellCollection)
	{
		List<ClsCellule> oCells = new ArrayList<ClsCellule>();
		
		if(oCellCollection.size() == 0)
			return null;
		
		for(int index = 0; index < oCellCollection.size(); index++)
		{
			if(oCellCollection.get(index).getCodepere().trim().toUpperCase().equals(oCell.getCodeorganigramme().trim().toUpperCase()))
			{
				oCells.add(oCellCollection.get(index));
			}
		}
			
		return oCells;
	}
	
	/**
	 * Arrange cell flow chart id.
	 *
	 */
	public void _arrangeCell()
	{
		try {
			
			Map<String, List<ClsCellule>> oCellCollection = this.getCellCollection();
			oCellPere = getOCellFather(); //this.getCellPereByCode(this.getCodepere(), oCellCollection); //getOCellFather(); 
			if(oCellPere == null)
				return;
			
			this.getFlowChart2().add(new Organigramme(new Integer(this.getCdos()), oCellPere.getCodeorganigramme(), oCellPere.getLibelle(),
					oCellPere.getCodeniveau(), new Date(), "", "", oCellPere.getCodeposte(), oCellPere.getCodepere(), "", "N", "N", oCellPere.getCodesite() , oCellPere.getNivfonction(),oCellPere.getLibellecourt() ));
			
			List<ClsCellule> oCellsFils = new ArrayList<ClsCellule>();
			
//			ClsParameter.println((new java.util.Date()) + "-------------------------------- 1 --------------------------");
			oCellsFils.addAll(oCellCollection.get(oCellPere.getCodeorganigramme()));//this.getFilsCell(oCellPere, oCellCollection);
//			ClsParameter.println((new java.util.Date()) + "-------------------------------- 2 --------------------------");
			if(oCellsFils == null || oCellsFils.size() == 0)
				return;
			
//			ClsParameter.println((new java.util.Date()) + "-------------------------------- 3 --------------------------");
//			ClsParameter.println((new java.util.Date()) + "-------------------------------- 3.2 " + oCellPere.getCodeorganigramme() + " " + oCellPere.getLibelle() + "--------------------------");
			
			for(int index = 0; index < oCellsFils.size(); index++)
			{
				try
				{
					if(!oCellsFils.get(index).getCodeorganigramme().equalsIgnoreCase(oCellPere.getCodeorganigramme()))
					{
						_arrangeCellSon(oCellsFils.get(index), oCellCollection, String.valueOf(index));
					}
				}
				catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					continue;
				}
				
			}
			Session _oSession = this.getService().getSession();
			Connection oConnexion = null;
			Statement oStatement = null;
			ResultSet oResultSet = null;

			try {
				
			ParameterUtil.println("------------------------------------ DELETE OLD CELL-----------------------");
			
			String strSql = "DELETE FROM Organigramme WHERE cdos = " + "'" + this.getCdos() + "'";
			oConnexion = this.getService().getConnection();
			oStatement = oConnexion.createStatement();
			oResultSet = oStatement.executeQuery(strSql);

				ParameterUtil.println("------------------------------------ DELETE SUCCESSFULL-----------------------");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			finally{
				try
				{
				  if(oResultSet!=null) oResultSet.close();
				  if(oStatement!=null)  oStatement.close();
				  if(oConnexion!=null)  oConnexion.close();
				  if(_oSession!=null) _oSession.close();
				}
				catch (Exception e)
				{
				   e.printStackTrace();
				}

			}

			ParameterUtil.println("------------------------------------ SAVE NEWS-----------------------");
//			this.getService().saveOrUpdateAll(this.getFlowChart2());
			for(Organigramme oCellul : this.getFlowChart2())
			{
				try {
					this.getService().save(oCellul);
				} catch (Exception e) {
					// TODO: handle exception
					continue;
				}
			}
			ParameterUtil.println("------------------------------------ SAVE SUCCESSFULL-----------------------");
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	private void _arrangeCellSon(ClsCellule oCellFils, Map<String, List<ClsCellule>> oCellCollection, String index2)
	{
		List<ClsCellule> oCellsFils = new ArrayList<ClsCellule>();
		
		try
		{
			oCellsFils.addAll(oCellCollection.get(oCellFils.getCodeorganigramme()));
			
			
			
			if(oCellsFils == null || oCellsFils.size() == 0)
			{
				if(!oCellFils.getCodepere().equalsIgnoreCase(this.getCodepere()))
				{
					try
					{
					 String strSonNbr = oCellFils.getCodeorganigramme().substring(oCellFils.getCodepere().length(), oCellFils.getCodeorganigramme().length());
					 if(strSonNbr.length() == 1)
					 {
						 this._updateCellSon(oCellFils.getCodeorganigramme(), oCellFils.getCodepere() + "0" + strSonNbr, this.getCdos());
						 ParameterUtil.println((new Date()) + "--------------------------------New code 3.2." + index2 + " " + oCellFils.getCodepere() + "0" + strSonNbr + "--------------------------");
					 }
					 else
						 this.getFlowChart2().add(new Organigramme(new Integer(this.getCdos()), oCellFils.getCodeorganigramme(), oCellFils.getLibelle(),
								 oCellFils.getCodeniveau(), new Date(), "", "", oCellFils.getCodeposte(), oCellFils.getCodepere(), "", oCellFils.getBprest(), oCellFils.getBcasefic(),oCellFils.getCodesite(), oCellFils.getNivfonction(),oCellFils.getLibellecourt()));
					}
					catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
				
				return;
			}
			
			if(!oCellFils.getCodepere().equalsIgnoreCase(this.getCodepere()))
			{
				try
				{
				 String strSonNbr = oCellFils.getCodeorganigramme().substring(oCellFils.getCodepere().length(), oCellFils.getCodeorganigramme().length());
				 if(strSonNbr.length() == 1)
				 {
					 this._updateCellSon(oCellFils.getCodeorganigramme(), oCellFils.getCodepere() + "0" + strSonNbr, this.getCdos());
					 this._updateCellFather(oCellFils.getCodeorganigramme(), oCellFils.getCodeorganigramme(), oCellFils.getCodepere() + "0" + strSonNbr, this.getCdos());
					 ParameterUtil.println((new Date()) + "--------------------------------New code 3.2." + index2 + " " + oCellFils.getCodepere() + "0" + strSonNbr + "--------------------------");
				 }
				 else
					 this.getFlowChart2().add(new Organigramme(new Integer(this.getCdos()), oCellFils.getCodeorganigramme(), oCellFils.getLibelle(),
							 oCellFils.getCodeniveau(), new Date(), "", "", oCellFils.getCodeposte(), oCellFils.getCodepere(), "", oCellFils.getBprest(), oCellFils.getBcasefic(),oCellFils.getCodesite(), oCellFils.getNivfonction(),oCellFils.getLibellecourt()));
				}
				catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			
			ParameterUtil.println((new Date()) + "-------------------------------- 3.2." + index2 + " " + oCellFils.getCodeorganigramme() + " " + oCellFils.getLibelle() + " Nbre fils :" + oCellsFils.size()  + "--------------------------");
			oCellCollection.remove(oCellFils.getCodeorganigramme());
			
			for(int index = 0; index < oCellsFils.size(); index++)
			{
				ParameterUtil.println((new Date()) + "-------------------------------- 3.2." + index2 + "." + index + " " + oCellsFils.get(index).getCodeorganigramme() + " " + " Nbre fils :" + oCellsFils.get(index).getLibelle()  + "--------------------------");
				if(!oCellsFils.get(index).getCodeorganigramme().equalsIgnoreCase(oCellFils.getCodeorganigramme()))
				{
					String indexNext = index2 + "." + String.valueOf(index);
					if(!oCellsFils.get(index).getCodepere().equalsIgnoreCase(this.getCodepere()))
					{
						try
						{
						 String strSonNbr = oCellsFils.get(index).getCodeorganigramme().substring(oCellsFils.get(index).getCodepere().length(), oCellsFils.get(index).getCodeorganigramme().length());
						 if(strSonNbr.length() == 1)
						 {
							 this._updateCellSon(oCellsFils.get(index).getCodeorganigramme(), oCellsFils.get(index).getCodepere() + "0" + strSonNbr, this.getCdos());
							 this._updateCellFather(oCellsFils.get(index).getCodeorganigramme(), oCellsFils.get(index).getCodeorganigramme(), oCellsFils.get(index).getCodepere() + "0" + strSonNbr, this.getCdos());
							 ParameterUtil.println((new Date()) + "--------------------------------New code 3.2." + index2 + "." + index + " " + oCellsFils.get(index).getCodepere() + "0" + strSonNbr + "--------------------------");
						 }
						 else
							 this.getFlowChart2().add(new Organigramme(new Integer(this.getCdos()), oCellsFils.get(index).getCodeorganigramme(), oCellsFils.get(index).getLibelle(),
									 oCellsFils.get(index).getCodeniveau(), new Date(), "", "", oCellsFils.get(index).getCodeposte(), oCellsFils.get(index).getCodepere(), "", oCellsFils.get(index).getBprest(), oCellsFils.get(index).getBcasefic(),oCellsFils.get(index).getCodesite(), oCellFils.getNivfonction(),oCellFils.getLibellecourt()));
						}
						catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					}
					_arrangeCellSon(oCellsFils.get(index), oCellCollection, indexNext);
				}
			}
			
			return;
		}
		catch(Exception e)
		{
			return;
		}
		finally
		{
			ParameterUtil.__invokeGarbageCollection();
		}
	}
	
	/**
	 * 
	 * @param strOldFather
	 * @param strNewFather
	 * @param strCdos
	 */
	private void _updateCellFather(String strOrgID, String strOldFather, String strNewFather, String strCdos)
	{
//		Session _oSession = this.getService().getSession();
		
		try {
			
//			String strSql = "UPDATE Rhtorganigramme SET codepere = " + "'" + strNewFather + "'" + " WHERE codepere = " + "'" + strOldFather + "'" + " AND cdos = " + "'" + strCdos + "'";
//			_oSession.connection().createStatement().executeQuery(strSql);
			
			if(this.getFlowChart1().size() > 0)
			{
				for(Organigramme org : this.getFlowChart1())
				{
					if(org.getCodepere().equalsIgnoreCase(strOldFather))
					{
						boolean isBelong = false;
						for(Organigramme org2 : this.getFlowChart2())
						{
							if(org.getCodeorganigramme().equalsIgnoreCase(strOrgID))
							{
								Organigramme org1 = new Organigramme(new Integer(this.getCdos()), org2.getCodeorganigramme(), org2.getLibelle(),
										org2.getCodeniveau(), new Date(), "", "", org2.getCodeposte(), strNewFather, "", org2.getBprestataire(), org2.getBcasefictive(),org2.getCodesite(), org2.getNivfonction(),org2.getLibellecourt());
								this.getFlowChart2().remove(org2);
								this.getFlowChart2().add(org1);
								isBelong = true;
								break;
							}
						}
						
						if(!isBelong)
						{
							Organigramme org1 = new Organigramme(new Integer(this.getCdos()), org.getCodeorganigramme(), org.getLibelle(),
									org.getCodeniveau(), new Date(), "", "", org.getCodeposte(), strNewFather, "", org.getBprestataire(), org.getBcasefictive(),org.getCodesite(), org.getNivfonction(),org.getLibellecourt());
								this.getFlowChart2().add(org1);
						}
					}
						
				}
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		finally
		{
//			this.getService().closeConnexion(_oSession);
		}
	}
	/**
	 * 
	 * @param strCdos
	 */
	private void _updateCellSon(String strOldID, String strNewID, String strCdos)
	{
//		Session _oSession = this.getService().getSession();
		
		try {
			
//			String strSql = "UPDATE Rhtorganigramme SET codepere = " + "'" + strNewFather + "'" + " WHERE codepere = " + "'" + strOldFather + "'" + " AND cdos = " + "'" + strCdos + "'";
//			_oSession.connection().createStatement().executeQuery(strSql);
			
			if(this.getFlowChart1().size() > 0)
			{
				for(Organigramme org : this.getFlowChart1())
				{
					if(org.getCodeorganigramme().equalsIgnoreCase(strOldID))
					{
						boolean isBelong = false;
						for(Organigramme org2 : this.getFlowChart2())
						{
							if(org2.getCodeorganigramme().equalsIgnoreCase(strOldID))
							{
								Organigramme org1 = new Organigramme(new Integer(this.getCdos()), strNewID, org2.getLibelle(),
										org2.getCodeniveau(), new Date(), "", "", org2.getCodeposte(), org2.getCodepere(), "", org2.getBprestataire(), org2.getBcasefictive(),org2.getCodesite(), org2.getNivfonction(),org2.getLibellecourt());
								this.getFlowChart2().remove(org);
								this.getFlowChart2().add(org1);
								isBelong = true;
								break;
							}
						}
						
						if(!isBelong)
						{
							Organigramme org1 = new Organigramme(new Integer(this.getCdos()), strNewID, org.getLibelle(),
									org.getCodeniveau(), new Date(), "", "", org.getCodeposte(), org.getCodepere(), "", org.getBprestataire(), org.getBcasefictive(),org.getCodesite(), org.getNivfonction(),org.getLibellecourt());
								this.getFlowChart2().add(org1);
						}
					}
						
				}
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		finally
		{
//			this.getService().closeConnexion(_oSession);
		}
	}
	
	// Construct cell collection from database data.
	public Map<String, List<ClsCellule>> getCellCollection()//(ResultSet oRsCell)
	{
		String strPriseEnCompteCouleur = null;
		List<ClsCellule> oCellCollection = new ArrayList<ClsCellule>();
		Map<String, List<ClsCellule>> oResultCellCollection = new HashMap<String, List<ClsCellule>>();
		Session _oSession = this.getService().getSession();
		Connection oConnexion = null;
		Statement oStatement = null;
		ResultSet oRsCell = null;

		try
		{
//			if(oRsCell == null)
//				return null;
			ParameterUtil.println("---------- SQL for organigramme : " + this.getStrSQL());
			oConnexion = this.getService().getConnection();
			oStatement = oConnexion.createStatement();
			oRsCell = oStatement.executeQuery(this.getStrSQL());

			if(oRsCell == null)
				return null;
			
			String strCodePoste = null;
			String strCodePere  = this.getCodepere();
			String strLibelle   = null;
			int _intCellNbr = 0;
			
			while(oRsCell.next())
			{
				try
				{
					_intCellNbr++;
					ClsCellule oCell = new ClsCellule();
					oCell.setEnumOrganigrammeType(this.getEnumOrganigrammeType());
					oCell.setEnumFlowChartView(this.getEnumFlowChartView());
					
					try
					{
						oCell.setCodeniveau(String.valueOf(oRsCell.getInt(ParameterUtil.STR_CODENIVEAU)));
						oCell.setCodeorganigramme(oRsCell.getString(ParameterUtil.STR_CODEORGANIGRAMME).trim());
						oCell.setCodepere(oRsCell.getString(ParameterUtil.STR_CODEPERE).trim());
						oCell.setCodesite(oRsCell.getString("codesite"));
						oCell.setNivfonction(oRsCell.getString("nivfonction"));
						oCell.setLibellecourt(oRsCell.getString("libellecourt"));
						oCell.setBprest(oRsCell.getString("bprestataire"));
						oCell.setBcasefic(oRsCell.getString("bcasefictive"));
						
						strLibelle = oRsCell.getString(ParameterUtil.STR_LIBELLE);
						
						if(strLibelle != null)
						{
							strLibelle = strLibelle.replaceAll("�", "apostrphe123");
							strLibelle = strLibelle.replaceAll("'", "apostrphe123");
							strLibelle = strLibelle.replaceAll("''", "apostrphe123");
							oCell.setLibelle(strLibelle);
						}
						strPriseEnCompteCouleur = oRsCell.getString(ParameterUtil.STR_PRISE_EN_COMPTE_COULEUR);
						if(strPriseEnCompteCouleur != null && strPriseEnCompteCouleur.trim().length() != 0)
							if(strPriseEnCompteCouleur.equals("O"))
								oCell.setColor(oRsCell.getString(ParameterUtil.STR_CODECOULEUR));
						strCodePoste = oRsCell.getString(ParameterUtil.STR_CODEPOSTE);
						if(strCodePoste != null)
							if(strCodePoste.trim().length() != 0)
							{
								try {
									oCell.setCategoriePoste(oRsCell.getString(ParameterUtil.STR_CODE_CAT_POSTE));
									
								} catch (Exception e) {
									// TODO: handle exception
								}
								
								oCell.setCodeposte(strCodePoste);
								String strAdminNumber = oRsCell.getString(ParameterUtil.STR_CODE_MAT);
								if(strAdminNumber != null && strAdminNumber.trim().length() > 0)
								{
									oCell.setMatriculeagent(strAdminNumber);
									strLibelle = oRsCell.getString(ParameterUtil.STR_NOM_AGENT) + " " + oRsCell.getString(ParameterUtil.STR_PRENOM_AGENT);
									if(strLibelle != null)
									{
										strLibelle = strLibelle.replaceAll("�", "apostrphe123");
										strLibelle = strLibelle.replaceAll("'", "apostrphe123");
										strLibelle = strLibelle.replaceAll("''", "apostrphe123");
										strLibelle = strLibelle.replaceAll("/", "apostrphe124");
										oCell.setNomagent(strLibelle);
									}
									
									String strCategory = oRsCell.getString(ParameterUtil.STR_CODE_CAT_AGENT);
									
									try {
										if(strCategory != null && strCategory.trim().length() >= 2)
											strCategory = strCategory.substring(strCategory.length() - 2, strCategory.length());
									} catch (Exception e) {
										// TODO: handle exception
									}
									oCell.setCategorie(strCategory);
								}
								
							}
					}
					catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					oCell.setRequest(this.getRequest());
//					ClsParameter.println(oRsCell.getString(ClsParameter.STR_CODEORGANIGRAMME));
					if(oCell.getCodepere().equals(strCodePere))
						oCellCollection.add(oCell);
					else
					{
						strCodePere = oCell.getCodepere();
						oCellCollection = new ArrayList<ClsCellule>();
						oCellCollection.add(oCell);
						oResultCellCollection.put(strCodePere, oCellCollection);
					}
					
					if(getCodepere().trim().equals(ParameterUtil.STR_INIT_CODEPERE))
					{
						if(getCodepere().equalsIgnoreCase(oCell.getCodepere()))
						    this.setOCellFather(oCell);
					}
					else
					{
						if(getCodepere().equalsIgnoreCase(oCell.getCodeorganigramme()))
						    this.setOCellFather(oCell);
					}
					
//					this.getFlowChart1().add(new Rhtorganigramme(new RhtorganigrammePK(this.getCdos(), oCell.getCodeorganigramme()), oCell.getLibelle(),
//							oCell.getCodeniveau(), new Date(), "", "", oCell.getCodeposte(), oCell.getCodepere(), ""));
//					oCellCollection.add(oCell);
//					
//					if(!this.getService().getSession().isOpen())
//						this.getService().getSession().reconnect();
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
//					ClsParameter.println(ex.getMessage());
					continue;
				}
				finally{
					
				}
//				
			}
			
			this.setNbreCell(_intCellNbr);
			return oResultCellCollection;
		}
		catch(Exception e)
		{
//			ClsParameter.println(e.getMessage());
			return oResultCellCollection;
		}
		finally
		{
			ParameterUtil.__invokeGarbageCollection();
			try
			{
			  if(oRsCell!=null) oRsCell.close();
			  if(oStatement!=null)  oStatement.close();
			  if(oConnexion!=null)  oConnexion.close();
			  if(_oSession!=null) _oSession.close();
			}
			catch (Exception e)
			{
			   e.printStackTrace();
			}

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

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
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

	public String getStrSQL() {
		return strSQL;
	}

	public void setStrSQL(String strSQL) {
		this.strSQL = strSQL;
	}
}
