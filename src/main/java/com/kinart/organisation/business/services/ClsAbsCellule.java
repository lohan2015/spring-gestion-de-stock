package com.kinart.organisation.business.services;

import com.kinart.paie.business.services.utils.GeneriqueConnexionService;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public abstract class ClsAbsCellule {

	protected static String STR_ONE = "1";
	public static String STR_TASK = "task";
	public static String STR_DURATION = "duration";
	public static String STR_START_DATE = "startdate";
	public static String STR_END_DATE = "enddate";
	public static String STR_FLOW_CHART_STRING = "flowstring";
	public static String STR_FLOW_CHART_NAME = "flowname";
	public static String STR_PERCENT_DONE = "percentDone";
	public static String STR_STILL_PROCESSING = "stillProcessing";
	public static String STR_PROCESSING = "bProcessing";
	public static String STR_THREAD_TASK = "th_task";
	public static String STR_TOTAL_SIZE = "totalsize";
	public static String STR_FLOW_CHART_RESULT = "strOrganigramme";
	public static String STR_SNAPSHOT_ID = "strSnapShotId";
	public static  long _lgTimerSleep_1 = 800;
	public static  long _lgTimerSleep_2 = 200;
	
	protected String codeorganigramme = null;
	protected String libelle = null;
	protected String codeposte = null;
	protected String codepere = null;
	protected String matriculeagent = null;
	protected String categorie = null;
	protected String categoriePoste = null;
	protected String nomagent = null;
	protected String codeniveau = null;
	protected String color = null;
	protected String cdos = null;
	protected Integer nbreFils = 0;
	protected Integer nbreFilsTotal = 0;
	protected int childnbre = 15;
	protected int nbreCell = 0;
	protected int nbreCellTreat = 0;
	protected String flowchartstring = null;
	protected String bprest = "N";
	protected String bcasefic = "N";
	protected String codesite = null;
	protected String nivfonction = null;
	protected String libellecourt = null;
	protected boolean firstcellule  = true;
	
	protected HttpServletRequest request;
	protected String photoRealPath;
	protected GeneriqueConnexionService service;
	protected ClsCellule oCellFather = null;
	protected ClsCelluleThread oCellFather2 = null;
	
//	protected String strSQL = "SELECT Rhorg.codeorganigramme, Rhniv.codeniveau, Rhorg.codepere, Rhorg.codeposte, Rhorg.codematricule, Rhorg.libelle, Rhorg.accepteexterne, Rhniv.codecouleur, Rhniv.priseencomptecouleur " +
//							"FROM Rhtorganigramme Rhorg, Rhpniveau Rhniv " +
//							"WHERE Rhorg.codeniveau = Rhniv.codeniveau ";
	
	protected String strSQL = "SELECT Rhorg.libellecourt,Rhorg.nivfonction, Rhorg.codesite,Rhorg.codeorganigramme, Rhniv.codeniveau, Rhorg.codepere, Rhorg.codeposte, Rhorg.codematricule, Rhorg.libelle, Rhorg.accepteexterne, Rhorg.bprestataire, Rhorg.bcasefictive, Rhniv.codecouleur, Rhniv.priseencomptecouleur, " +
       						  "poste.cat as pcat, agent.nmat, agent.nom, agent.pren, agent.cat " +
       						  "FROM Organigramme rhorg LEFT JOIN Orgniveau rhniv ON (rhorg.codeniveau = rhniv.codeniveau AND rhorg.identreprise = rhniv.identreprise) LEFT JOIN rhtposte poste ON (rhorg.codeposte = poste.codeposte AND rhorg.identreprise = poste.identreprise) " +
       						  "LEFT JOIN Salarie AGENT ON (AGENT.codeposte = poste.codeposte AND AGENT.identreprise = poste.identreprise) WHERE Rhorg.codeniveau = Rhniv.codeniveau ";
	
	protected String strSQLNbFils = "SELECT count(orgfils.codeorganigramme) as nbFilsTotal,rhorg.codeorganigramme, rhorg.codepere " +
		  													"FROM Organigramme rhorg LEFT JOIN Orgniveau rhniv ON (rhorg.codeniveau = rhniv.codeniveau AND rhorg.identreprise = rhniv.identreprise) " +
  															"LEFT JOIN Organigramme orgfils ON (orgfils.codepere like rhorg.codeorganigramme||'%' AND orgfils.identreprise = rhorg.identreprise) " +
															"WHERE rhorg.codeniveau = Rhniv.codeniveau and rhorg.BCASEFICTIVE='N' and orgfils.bcasefictive='N'";

	protected List<ClsCellule> oListefils = new ArrayList<ClsCellule>();
	protected List<ClsCelluleThread> oListefils2 = new ArrayList<ClsCelluleThread>();
	
//	protected DataAccesDAO oDao = null;
	
	protected ClsCellule	 oCellPere = null;
	protected ClsCelluleThread	 oCellPere2 = null;
	
	protected EnumTypeOrganigramme enumOrganigrammeType = EnumTypeOrganigramme.RAKE;
	protected EnumFlowChartView enumFlowChartView = EnumFlowChartView.ALL;
	
	
}

