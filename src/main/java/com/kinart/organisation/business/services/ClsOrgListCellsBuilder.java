package com.kinart.organisation.business.services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kinart.organisation.business.vo.ClsParametreOrganigrammeVO;
import com.kinart.paie.business.services.cloture.ClsNomenclature;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.kinart.paie.business.services.utils.ParameterUtil;
import com.kinart.paie.business.services.utils.StringUtil;
import com.kinart.utils.ClsGenericComparator;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;

public class ClsOrgListCellsBuilder
{

	private ClsParametreOrganigrammeVO param;

	private ClsOrgCellule cellDepart;

	private Map<String, Integer> effectifMap = new HashMap<String, Integer>();

	private Map<String, Integer> prestatairesMap = new HashMap<String, Integer>();

	private Map<String, List<ClsNomenclature>> prestatairesListMap = new HashMap<String, List<ClsNomenclature>>();

	private GeneriqueConnexionService service;

	public boolean existFictiveCell = false;

	public ClsOrgListCellsBuilder(GeneriqueConnexionService service, ClsParametreOrganigrammeVO parametres)
	{

		this.param = parametres;
		this.service = service;
		if (!param.isOrganigrammePrestataire())
		{
			this.effectifMap = computeEffectifCellCollection();
			this.prestatairesMap = this.computeOrganigrammeAssociePrestataire();
		}
		else
		{

		}

	}

	// Construct cell collection from database data.
	@SuppressWarnings("deprecation")
	public Map<String, Integer> computeEffectifCellCollection()
	{

		Map<String, Integer> oResultCellCollection = new HashMap<String, Integer>();
		
		String strQuerySite = _getSites();
		
		String strQuerySiteFils= StringUtil.replace(strQuerySite,"Rhorg.", "orgfils.") ;

		String strQueryFils = "orgfils.codepere like rhorg.codeorganigramme||'%'";

		if (!param.isAfficherTousLesFils())
			strQueryFils = "orgfils.codepere = rhorg.codeorganigramme";
		
		if(StringUtil.isNotBlank(strQuerySiteFils))
			strQueryFils+=" and "+strQuerySiteFils;

		if (param.isCompterCellulesAfficheesUniquement())
			strQueryFils = strQueryFils + " and orgfils.codeniveau <= " + "'" + param.getNiveauArrive() + "'";

		if (!param.isOrganigrammePrestataire())
			strQueryFils = strQueryFils + " and orgfils.bprestataire = 'N'";

		String strSQLNbFils = "SELECT count(orgfils.codeorganigramme) as nbFilsTotal,rhorg.codeorganigramme, rhorg.codepere "
				+ "FROM Organigramme rhorg LEFT JOIN Orgniveau rhniv ON (rhorg.codeniveau = rhniv.codeniveau AND rhorg.identreprise = rhniv.identreprise) " + "LEFT JOIN Organigramme orgfils ON ("
				+ strQueryFils + " AND orgfils.identreprise = rhorg.identreprise) ";
		
		strSQLNbFils+=" left join Orgposte poste on (poste.identreprise=orgfils.identreprise and poste.codeposte=orgfils.codeposte) ";
		strSQLNbFils+= " WHERE rhorg.codeniveau = rhniv.codeniveau and rhorg.bcasefictive='N' and orgfils.bcasefictive='N'";

		strSQLNbFils = strSQLNbFils + "AND Rhorg.identreprise = " + "'" + param.getDossier() + "'" + " AND Rhniv.identreprise = " + "'" + param.getDossier() + "'";

		if (!ParameterUtil._isStringNull(param.getCelluleDepart()))
		{
			strSQLNbFils = strSQLNbFils + " AND Rhorg.codeorganigramme LIKE " + "'" + param.getCelluleDepart() + "%'";
		}
		else
		{
			strSQLNbFils = strSQLNbFils + " AND Rhorg.codeorganigramme >= " + "'" + param.getCelluleDepart() + "'";
		}

		if (!ParameterUtil._isStringNull(param.getNiveauArrive()))
			strSQLNbFils = strSQLNbFils + " AND Rhniv.codeniveau <= " + "'" + param.getNiveauArrive() + "'";
		
		if (!ParameterUtil._isStringNull(strQuerySite))
			strSQLNbFils += " and " + strQuerySite;
		
		if(param.isActiversecuriteservice())
		{
			String strQueryService = _getServices("poste");
			if(StringUtil.isNotBlank(strQueryService))
				strSQLNbFils += " and ( " + strQueryService+" )";
			
		}

		strSQLNbFils = strSQLNbFils + " group by rhorg.codeorganigramme,rhorg.codepere";
		strSQLNbFils = strSQLNbFils + " ORDER BY Rhorg.codepere, Rhorg.codeorganigramme ASC";
		Session _oSession = null;
		Connection oConnexion = null;
		Statement oStatement = null;
		ResultSet oRsCell = null;
		try
		{
			System.out.println("Query for counting = "+strSQLNbFils);
			ParameterUtil.println("---------- SQLNbFils for FlowChart :" + strSQLNbFils);
			_oSession = this.getService().getSession();
			oConnexion = this.getService().getConnection();
			oStatement = oConnexion.createStatement();
			oRsCell = oStatement.executeQuery(strSQLNbFils);

			if (oRsCell == null)
				return null;

			while (oRsCell.next())
			{
				try
				{

					oResultCellCollection.put(oRsCell.getString(ParameterUtil.STR_CODEORGANIGRAMME), oRsCell.getInt(ParameterUtil.STR_NB_FILS_TOTAL));

				}
				catch (Exception ex)
				{
					ex.printStackTrace();
					ParameterUtil.println(ex.getMessage());
					continue;
				}
			}

			// this.getService().closeConnexion(_oSession);
			return oResultCellCollection;
		}
		catch (Exception e)
		{
			ParameterUtil.println(e.getMessage());
			return oResultCellCollection;
		}
		finally
		{
			ParameterUtil.__invokeGarbageCollection();
			try
			{
				if (oRsCell != null)
					oRsCell.close();
				if (oStatement != null)
					oStatement.close();
				if (oConnexion != null)
					oConnexion.close();
				if (_oSession != null)
					_oSession.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}
	}

	// compute the number of direct prestataires
	@SuppressWarnings("deprecation")
	public Map<String, Integer> computeDirectEffectifPrestataireCellCollection()
	{

		Map<String, Integer> oResultCellCollection = new HashMap<String, Integer>();

		String strSQLNbFils = "SELECT sum(effectif) as nbFilsTotal,codeorganigramme " + "FROM Organigrammeassocie " + "WHERE identreprise = " + "'" + param.getDossier() + "'";

		if (!ParameterUtil._isStringNull(param.getCelluleDepart()))
		{
			strSQLNbFils = strSQLNbFils + " AND codeorganigramme LIKE " + "'" + param.getCelluleDepart() + "%'";
		}
		else
		{
			strSQLNbFils = strSQLNbFils + " AND codeorganigramme >= " + "'" + param.getCelluleDepart() + "'";
		}

		strSQLNbFils = strSQLNbFils + " group by codeorganigramme";
		strSQLNbFils = strSQLNbFils + " ORDER BY codeorganigramme ASC";
		Session _oSession = null;
		Connection oConnexion = null;
		Statement oStatement = null;
		ResultSet oRsCell = null;
		try
		{

			ParameterUtil.println("---------- SQLNb Prestataires for FlowChart :" + strSQLNbFils);
			_oSession = this.getService().getSession();
			oConnexion = this.getService().getConnection();
			oStatement = oConnexion.createStatement();
			oRsCell = oStatement.executeQuery(strSQLNbFils);
			Integer effectif = 0;
			if (oRsCell == null)
				return null;

			while (oRsCell.next())
			{
				try
				{
					effectif = oRsCell.getInt(ParameterUtil.STR_NB_FILS_TOTAL);
					if (effectif == null)
						effectif = 0;
					oResultCellCollection.put(oRsCell.getString(ParameterUtil.STR_CODEORGANIGRAMME), effectif);

				}
				catch (Exception ex)
				{
					ex.printStackTrace();
					ParameterUtil.println(ex.getMessage());
					continue;
				}
			}

			// this.getService().closeConnexion(_oSession);
			return oResultCellCollection;
		}
		catch (Exception e)
		{
			ParameterUtil.println(e.getMessage());
			return oResultCellCollection;
		}
		finally
		{
			ParameterUtil.__invokeGarbageCollection();
			try
			{
				if (oRsCell != null)
					oRsCell.close();
				if (oStatement != null)
					oStatement.close();
				if (oConnexion != null)
					oConnexion.close();
				if (_oSession != null)
					_oSession.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}
	}

	// compute the number of all prestataires of all cells
	@SuppressWarnings("deprecation")
	private Map<String, Integer> computeDirectEffectifPrestataireOfAllCellCollection()
	{

		Map<String, Integer> oResultCellCollection = new HashMap<String, Integer>();

		String strQueryFils = "orgfils.codepere like orgpere.codeorganigramme||'%' and orgfils.bprestataire='O'";

		if (!param.isAfficherTousLesFils())
			strQueryFils = "orgfils.codepere = orgpere.codeorganigramme and orgfils.bprestataire='O'";

		String strSQLNbFils = "SELECT count(orgfils.codeorganigramme) as nbFilsTotal,orgpere.codeorganigramme, orgpere.codepere "
				+ "FROM Orgrganigramme orgpere LEFT JOIN Orgniveau rhniv ON (orgpere.codeniveau = rhniv.codeniveau AND orgpere.identreprise = rhniv.identreprise) " + "LEFT JOIN Organigramme orgfils ON ("
				+ strQueryFils + " AND orgfils.identreprise = orgpere.identreprise) " + "WHERE orgpere.codeniveau = rhniv.codeniveau and orgpere.bcasefictive='N' and orgfils.bcasefictive='N'";

		strSQLNbFils = strSQLNbFils + "AND orgpere.identreprise = " + "'" + param.getDossier() + "'" + " AND Rhniv.identreprise = " + "'" + param.getDossier() + "'";

		if (!ParameterUtil._isStringNull(param.getCelluleDepart()))
		{
			strSQLNbFils = strSQLNbFils + " AND orgpere.codeorganigramme LIKE " + "'" + param.getCelluleDepart() + "%'";
		}
		else
		{
			strSQLNbFils = strSQLNbFils + " AND orgpere.codeorganigramme >= " + "'" + param.getCelluleDepart() + "'";
		}

		if (!ParameterUtil._isStringNull(param.getNiveauArrive()))
			strSQLNbFils = strSQLNbFils + " AND Rhniv.codeniveau <= " + "'" + param.getNiveauArrive() + "'";

		strSQLNbFils = strSQLNbFils + " group by orgpere.codeorganigramme,orgpere.codepere";
		strSQLNbFils = strSQLNbFils + " ORDER BY orgpere.codepere, orgpere.codeorganigramme ASC";
		Session _oSession = null;
		Connection oConnexion = null;
		Statement oStatement = null;
		ResultSet oRsCell = null;
		try
		{

			ParameterUtil.println("---------- SQLNbFils for FlowChart :" + strSQLNbFils);
			_oSession = this.getService().getSession();
			oConnexion = this.getService().getConnection();
			oStatement = oConnexion.createStatement();
			oRsCell = oStatement.executeQuery(strSQLNbFils);

			if (oRsCell == null)
				return null;

			while (oRsCell.next())
			{
				try
				{

					oResultCellCollection.put(oRsCell.getString(ParameterUtil.STR_CODEORGANIGRAMME), oRsCell.getInt(ParameterUtil.STR_NB_FILS_TOTAL));

				}
				catch (Exception ex)
				{
					ex.printStackTrace();
					ParameterUtil.println(ex.getMessage());
					continue;
				}
			}

			// this.getService().closeConnexion(_oSession);
			return oResultCellCollection;
		}
		catch (Exception e)
		{
			ParameterUtil.println(e.getMessage());
			return oResultCellCollection;
		}
		finally
		{
			ParameterUtil.__invokeGarbageCollection();
			try
			{
				if (oRsCell != null)
					oRsCell.close();
				if (oStatement != null)
					oStatement.close();
				if (oConnexion != null)
					oConnexion.close();
				if (_oSession != null)
					_oSession.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}
	}

	// compute the number of all prestataires of all cells from rhporganigramme associe table
	@SuppressWarnings("deprecation")
	private Map<String, Integer> computeOrganigrammeAssociePrestataire()
	{

		Map<String, Integer> oResultCellCollection = new HashMap<String, Integer>();

		String strQueryFils = "orgfils.codeorganigramme like orgpere.codeorganigramme||'%'";

		if (!param.isAfficherTousLesFils())
			strQueryFils = "orgfils.codeorganigramme = orgpere.codeorganigramme";

		String strSQLNbFils = "SELECT sum(orgfils.effectif) as nbFilsTotal,orgpere.codeorganigramme "
				+ "FROM Orgrganigramme orgpere LEFT JOIN Orgniveau rhniv ON (orgpere.codeniveau = rhniv.codeniveau AND orgpere.identreprise = rhniv.identreprise) " + "LEFT JOIN Rhporganigrammeassocie orgfils ON ("
				+ strQueryFils + " AND orgfils.identreprise = orgpere.identreprise) " + "WHERE orgpere.codeniveau = rhniv.codeniveau and orgpere.bcasefictive='N'";

		strSQLNbFils = strSQLNbFils + "AND orgpere.identreprise = " + "'" + param.getDossier() + "'" + " AND Rhniv.identreprise = " + "'" + param.getDossier() + "'";

		if (!ParameterUtil._isStringNull(param.getCelluleDepart()))
		{
			strSQLNbFils = strSQLNbFils + " AND orgpere.codeorganigramme LIKE " + "'" + param.getCelluleDepart() + "%'";
		}
		else
		{
			strSQLNbFils = strSQLNbFils + " AND orgpere.codeorganigramme >= " + "'" + param.getCelluleDepart() + "'";
		}

		if (!ParameterUtil._isStringNull(param.getNiveauArrive()))
			strSQLNbFils = strSQLNbFils + " AND Rhniv.codeniveau <= " + "'" + param.getNiveauArrive() + "'";

		strSQLNbFils = strSQLNbFils + " group by orgpere.codeorganigramme,orgpere.codepere";
		strSQLNbFils = strSQLNbFils + " ORDER BY orgpere.codepere, orgpere.codeorganigramme ASC";
		Session _oSession = null;
		Connection oConnexion = null;
		Statement oStatement = null;
		ResultSet oRsCell = null;
		try
		{

			ParameterUtil.println("---------- SQLNbFils for FlowChart :" + strSQLNbFils);
			_oSession = this.getService().getSession();
			oConnexion = this.getService().getConnection();
			oStatement = oConnexion.createStatement();
			oRsCell = oStatement.executeQuery(strSQLNbFils);

			if (oRsCell == null)
				return null;

			while (oRsCell.next())
			{
				try
				{

					oResultCellCollection.put(oRsCell.getString(ParameterUtil.STR_CODEORGANIGRAMME), oRsCell.getInt(ParameterUtil.STR_NB_FILS_TOTAL));

				}
				catch (Exception ex)
				{
					ex.printStackTrace();
					ParameterUtil.println(ex.getMessage());
					continue;
				}
			}

			// this.getService().closeConnexion(_oSession);
			return oResultCellCollection;
		}
		catch (Exception e)
		{
			ParameterUtil.println(e.getMessage());
			return oResultCellCollection;
		}
		finally
		{
			ParameterUtil.__invokeGarbageCollection();
			try
			{
				if (oRsCell != null)
					oRsCell.close();
				if (oStatement != null)
					oStatement.close();
				if (oConnexion != null)
					oConnexion.close();
				if (_oSession != null)
					_oSession.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}
	}

	@SuppressWarnings("deprecation")
	private Map<String, List<ClsNomenclature>> computePrestatairesList()
	{

		Map<String, List<ClsNomenclature>> oResultCellCollection = new HashMap<String, List<ClsNomenclature>>();

		String strQuery = "SELECT associe.codeorganigramme, associe.codeexterne, associe.effectif, prestataire.vall as nomexterne, prestataire.vall as libelleexterne " + "FROM Rhporganigrammeassocie associe "
				+ "LEFT JOIN Rhfnom prestataire ON (prestataire.cacc=associe.codeexterne and prestataire.nume=1 and prestataire.ctab=201 AND associe.identreprise = prestataire.identreprise) "
				+ "WHERE associe.identreprise = " + "'" + param.getDossier() + "'";

		Session _oSession = null;
		Connection oConnexion = null;
		Statement oStatement = null;
		ResultSet oRsCell = null;

		String strCodeOrganigramme = null;

		String strLibelleExterne = null;
		try
		{
			_oSession = this.getService().getSession();
			oConnexion = this.getService().getConnection();
			oStatement = oConnexion.createStatement();
			oRsCell = oStatement.executeQuery(strQuery);

			if (oRsCell == null)
				return null;

			while (oRsCell.next())
			{
				try
				{
					strCodeOrganigramme = oRsCell.getString("codeorganigramme");
					strLibelleExterne = oRsCell.getString("libelleexterne");
					if (StringUtils.isBlank(strLibelleExterne))
						strLibelleExterne = oRsCell.getString("nomexterne");
					if (!oResultCellCollection.containsKey(strCodeOrganigramme))
						oResultCellCollection.put(strCodeOrganigramme, new ArrayList<ClsNomenclature>());

					oResultCellCollection.get(strCodeOrganigramme).add(new ClsNomenclature(strLibelleExterne, oRsCell.getInt("effectif")+""));

				}
				catch (Exception ex)
				{
					ex.printStackTrace();
					ParameterUtil.println(ex.getMessage());
					continue;
				}
			}

			return oResultCellCollection;
		}
		catch (Exception e)
		{
			ParameterUtil.println(e.getMessage());
			return oResultCellCollection;
		}
		finally
		{
			ParameterUtil.__invokeGarbageCollection();
			try
			{
				if (oRsCell != null)
					oRsCell.close();
				if (oStatement != null)
					oStatement.close();
				if (oConnexion != null)
					oConnexion.close();
				if (_oSession != null)
					_oSession.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}
	}
	
	private String site;
	private String _getSites()
	{
		if(StringUtils.isNotBlank(site))
			return site;
		
//		ClsAutorisationsUtilisateur auto = new ClsAutorisationsUtilisateur(param.getDossier(), param.getUtilisateur(), "Rhorg", ClsTypeProfilAgent.COLONNES, this.getService());
//
//		auto.setUseHibernate(false);

		String chaineauto = "";//auto.getSites(null);

		String strQuerySite = chaineauto;
		
		if(StringUtil.notEquals("*", param.getSite()) && StringUtils.isNotBlank(param.getSite()))
		//if (!"*".equalsIgnoreCase(param.getSite()))
			strQuerySite += " and Rhorg.codesite='" + param.getSite() + "'";
		
		site = strQuerySite;

		return strQuerySite;
	}
	
	private String auto;
	private String _getServices(String alias)
	{
		if(StringUtils.isNotBlank(auto))
			return auto;
		
//		ClsAutorisationsUtilisateur auto1 = new ClsAutorisationsUtilisateur(param.getDossier(), param.getUtilisateur(), alias, ClsTypeProfilAgent.COLONNES, this.getService());
//
//		auto1.setUseHibernate(false);

		String strQueryService= "";//auto1.getAutorisations(); //auto.getColumnAutorisation("niv3");
		
		auto = strQueryService;
		
		return strQueryService;
	}

	@SuppressWarnings("deprecation")
	public List<ClsOrgCellule> _getAllCellules()
	{

		String strTempCelluleDepart = "(rhorg.codepere ='" + param.getCelluleDepart() + "' or rhorg.codeorganigramme='02')";

		if (!ParameterUtil._isStringNull(param.getCelluleDepart()))
			strTempCelluleDepart = "(rhorg.codepere like '" + param.getCelluleDepart() + "%' or rhorg.codeorganigramme='" + param.getCelluleDepart() + "')";

		String strQuerySite = _getSites();

		String strSQL = "SELECT rhorg.*,rhniv.codecouleur as couleur, agent.nmat as nmat, agent.nom as nomagent, agent.pren as pren, poste.cat as categorieposte, agent.cat as categorieagent "
				+ ", poste.ech as echelonposte, agent.ech as echelonagent "
				+ "FROM Organigramme rhorg " + "LEFT JOIN Orgniveau rhniv ON (rhorg.codeniveau = rhniv.codeniveau AND rhorg.identreprise = rhniv.identreprise and rhniv.priseencomptecouleur='O') "
				+ "LEFT JOIN Orgposte poste ON (rhorg.codeposte = poste.codeposte AND rhorg.identreprise = poste.identreprise) "
				+ "LEFT JOIN Salarie agent ON (agent.codeposte = poste.codeposte AND agent.identreprise = poste.identreprise) " + "WHERE rhorg.identreprise='" + param.getDossier() + "'";

		if (! param.isAfficherPostesPrestataires())
			strSQL += " and rhorg.bprestataire='N'";

		strSQL += " and " + strTempCelluleDepart;

		if (!ParameterUtil._isStringNull(param.getNiveauArrive()))
			strSQL += " and rhorg.codeniveau <='" + param.getNiveauArrive() + "' ";

		if (!ParameterUtil._isStringNull(strQuerySite))
			strSQL += " and " + strQuerySite;
		
		if(param.isActiversecuriteservice())
		{
			String strQueryService = _getServices("poste");
			if(StringUtils.isNotBlank(strQueryService))
			strSQL += " and ( " + strQueryService+" )";
			
		}

		strSQL += " order by rhorg.codepere,rhorg.codeorganigramme ASC";

		ParameterUtil.println("Query = " + strSQL);
		System.out.println(strSQL);

		Session _o_Session = this.getService().getSession();
		Connection oConnexion = null;
		Statement oStatement = null;
		ResultSet _o_Result = null;

		List<ClsOrgCellule> _o_Cellules_Collection = new ArrayList<ClsOrgCellule>();

		ClsOrgCellule cellule = null;

		try
		{

			oConnexion = this.getService().getConnection();
			oStatement = oConnexion.createStatement();
			_o_Result = oStatement.executeQuery(strSQL);

			String strNom = null;

			String strPrenom = null;

			while (_o_Result.next())
			{
				try
				{

					cellule = new ClsOrgCellule();

					cellule.setIdEntreprise(new Integer(param.getDossier()));
					cellule.setCodeorganigramme(_o_Result.getString("codeorganigramme"));
					cellule.setLibelle(_o_Result.getString("libelle"));

					cellule.setCodeposte(_o_Result.getString("codeposte"));

					cellule.setCouleur(_o_Result.getString("couleur"));

					if (ParameterUtil._isStringNull(cellule.getCouleur()))
						cellule.setCouleur("blue");

					cellule.setMatricule(_o_Result.getString("nmat"));

					strNom = _o_Result.getString("nomagent");

					strPrenom = _o_Result.getString("pren");

					if (ParameterUtil._isStringNull(strNom))
						strNom = ParameterUtil.CHAINE_VIDE;

					if (ParameterUtil._isStringNull(strPrenom))
						strPrenom = ParameterUtil.CHAINE_VIDE;

					cellule.setNomagent(strNom + " " + strPrenom);

					cellule.setCategorieagent(_o_Result.getString("categorieagent"));
					
					cellule.setEchelonagent(_o_Result.getString("echelonagent"));

					cellule.setCodepere(_o_Result.getString("codepere"));

					cellule.setCodesite(_o_Result.getString("codesite"));

					cellule.setNivfonction(_o_Result.getString("nivfonction"));

					cellule.setLibellecourt(_o_Result.getString("libellecourt"));

					cellule.setBprestataire(_o_Result.getString("bprestataire"));

					cellule.setBcasefictive(_o_Result.getString("bcasefictive"));

					if (StringUtils.equals("O", cellule.getBcasefictive()))
						existFictiveCell = true;

					cellule.setCodeposte(_o_Result.getString("codeposte"));

					cellule.setCategorieposte(_o_Result.getString("categorieposte"));
					
					cellule.setEchelonposte(_o_Result.getString("echelonposte"));

					cellule.setCodeniveau(_o_Result.getString("codeniveau"));
					if(! param.isIceface())
					{
						if (cellule.getLibelle() != null)
						{
							cellule.setLibelle(cellule.getLibelle().replaceAll("�", "apostrphe123"));
							cellule.setLibelle(cellule.getLibelle().replaceAll("'", "apostrphe123"));
							cellule.setLibelle(cellule.getLibelle().replaceAll("''", "apostrphe123"));
	
						}
	
						if (cellule.getNomagent() != null)
						{
							cellule.setNomagent(cellule.getNomagent().replaceAll("�", "apostrphe123"));
							cellule.setNomagent(cellule.getNomagent().replaceAll("'", "apostrphe123"));
							cellule.setNomagent(cellule.getNomagent().replaceAll("''", "apostrphe123"));
	
						}
					}

					cellule.setTaillebordure(param.getTailleBordure());

					_o_Cellules_Collection.add(cellule);

				}
				catch (Exception e)
				{
					// TODO: handle exception
					e.printStackTrace();
					if (cellule != null)
						_o_Cellules_Collection.add(cellule);
					continue;
				}
				finally
				{
					cellule = null;
				}
			}

		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (_o_Result != null)
					_o_Result.close();
				if (oStatement != null)
					oStatement.close();
				if (oConnexion != null)
					oConnexion.close();
				if (_o_Session != null)
					_o_Session.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}

		List<ClsOrgCellule> allcell = new ArrayList<ClsOrgCellule>();

		List<ClsOrgCellule> filsCelluleCourante = new ArrayList<ClsOrgCellule>();

		ClsOrgCellule celluleFinale = null;

		ClsOrgCellule celluleFils = null;

		List<ClsOrgCellule> listeCellules = null;

		for (int i = 0; i < _o_Cellules_Collection.size(); i++)
		{

			listeCellules = new ArrayList<ClsOrgCellule>();

			celluleFinale = _o_Cellules_Collection.get(i);

			filsCelluleCourante = _getListeFils(celluleFinale, _o_Cellules_Collection);

			celluleFinale.setNumero(1);

			for (int ii = 0; ii < filsCelluleCourante.size(); ii++)
			{

				celluleFils = filsCelluleCourante.get(ii);

				celluleFils.setNumero(ii + 1);

				celluleFils.setCellulepere(celluleFinale);

				listeCellules.add(celluleFils);

			}

			celluleFinale.setNbrFils(filsCelluleCourante.size());

			celluleFinale.setFils(listeCellules);

			if (effectifMap.get(celluleFinale.getCodeorganigramme()) != null)
				celluleFinale.setEffectif(effectifMap.get(celluleFinale.getCodeorganigramme()));
			else
				celluleFinale.setEffectif(0);

			if (StringUtils.equalsIgnoreCase("O", celluleFinale.getBprestataire()))
				celluleFinale.setEffectifprestataire(0);
			else
			{
				if (prestatairesMap.get(celluleFinale.getCodeorganigramme()) != null)
					celluleFinale.setEffectifprestataire(prestatairesMap.get(celluleFinale.getCodeorganigramme()));
				else
					celluleFinale.setEffectifprestataire(0);
			}

			if (i == 0)
				celluleFinale.setIsallcellsfather(true);

			// celluleFinale.setTemplate(ClsTemplateGenerator._getTemplate(celluleFinale));
			// celluleFinale.setTemplate(ClsOrgTemplate.getTemplate(celluleFinale));

			allcell.add(celluleFinale);

		}

		return allcell;

	}

	@SuppressWarnings("deprecation")
	public List<ClsOrgCellule> _getAllPrestatairesCellules()
	{

		Map<String, Integer> mapPrestataires = this.computeDirectEffectifPrestataireOfAllCellCollection();

		Map<String, Integer> mapPrestatairesAssocies = this.computeOrganigrammeAssociePrestataire();

		this.setPrestatairesListMap(this.computePrestatairesList());

		String strTempCelluleDepart = "(rhorg.codepere ='" + param.getCelluleDepart() + "' or rhorg.codeorganigramme='02')";

		if (!ParameterUtil._isStringNull(param.getCelluleDepart()))
			strTempCelluleDepart = "(rhorg.codepere like '" + param.getCelluleDepart() + "%' or rhorg.codeorganigramme='" + param.getCelluleDepart() + "')";

		String strQuerySite = _getSites();

		String strSQL = "SELECT rhorg.*,rhniv.codecouleur as couleur, agent.nmat as nmat, agent.nom as nomagent, agent.pren as pren, poste.cat as categorieposte, agent.cat as categorieagent "
				+ ", poste.ech as echelonposte, agent.ech as echelonagent "
				+ "FROM Organigramme rhorg " + "LEFT JOIN Orgniveau rhniv ON (rhorg.codeniveau = rhniv.codeniveau AND rhorg.identreprise = rhniv.identreprise and rhniv.priseencomptecouleur='O') "
				+ "LEFT JOIN Orgposte poste ON (rhorg.codeposte = poste.codeposte AND rhorg.identreprise = poste.identreprise) "
				+ "LEFT JOIN Salarie agent ON (agent.codeposte = poste.codeposte AND agent.identreprise = poste.identreprise) " + "WHERE rhorg.identreprise='" + param.getDossier() + "'";

		strSQL += " and " + strTempCelluleDepart;

		if (!ParameterUtil._isStringNull(param.getNiveauArrive()))
			strSQL += " and rhorg.codeniveau <='" + param.getNiveauArrive() + "' ";

		if (!ParameterUtil._isStringNull(strQuerySite))
			strSQL += " and " + strQuerySite;
		
		if(param.isActiversecuriteservice())
		{
			String strQueryService = _getServices("poste");
			if(StringUtils.isNotBlank(strQueryService))
			strSQL += " and ( " + strQueryService+" )";
			
		}

		strSQL += " order by rhorg.codepere,rhorg.codeorganigramme ASC";

		ParameterUtil.println("Query = " + strSQL);

		Session _o_Session = this.getService().getSession();
		Connection oConnexion = null;
		Statement oStatement = null;
		ResultSet _o_Result = null;

		List<ClsOrgCellule> _o_Cellules_Collection = new ArrayList<ClsOrgCellule>();

		ClsOrgCellule cellule = null;

		Integer nbPrestataires = 0;

		String estAssocie = null;

		String codeOrganigramme = null;

		try
		{

			oConnexion = this.getService().getConnection();
			oStatement = oConnexion.createStatement();
			_o_Result = oStatement.executeQuery(strSQL);

			String strNom = null;

			String strPrenom = null;

			while (_o_Result.next())
			{
				try
				{

					codeOrganigramme = _o_Result.getString("codeorganigramme");

					estAssocie = _o_Result.getString("bprestataire");

					nbPrestataires = mapPrestataires.get(codeOrganigramme);

					if ((nbPrestataires != null && nbPrestataires > 0) || (StringUtils.equalsIgnoreCase(estAssocie, "O")))
					{

						cellule = new ClsOrgCellule();

						cellule.setIdEntreprise(new Integer(param.getDossier()));
						cellule.setCodeorganigramme(codeOrganigramme);

						cellule.setLibelle(_o_Result.getString("libelle"));

						cellule.setCodeposte(_o_Result.getString("codeposte"));

						cellule.setCouleur(_o_Result.getString("couleur"));

						if (ParameterUtil._isStringNull(cellule.getCouleur()))
							cellule.setCouleur("blue");

						cellule.setMatricule(_o_Result.getString("nmat"));

						strNom = _o_Result.getString("nomagent");

						strPrenom = _o_Result.getString("pren");

						if (ParameterUtil._isStringNull(strNom))
							strNom = ParameterUtil.CHAINE_VIDE;

						if (ParameterUtil._isStringNull(strPrenom))
							strPrenom = ParameterUtil.CHAINE_VIDE;

						cellule.setNomagent(strNom + " " + strPrenom);

						cellule.setCategorieagent(_o_Result.getString("categorieagent"));
						
						cellule.setEchelonagent(_o_Result.getString("echelonagent"));

						cellule.setCodepere(_o_Result.getString("codepere"));

						cellule.setCodesite(_o_Result.getString("codesite"));

						cellule.setNivfonction(_o_Result.getString("nivfonction"));

						cellule.setLibellecourt(_o_Result.getString("libellecourt"));

						cellule.setBprestataire(_o_Result.getString("bprestataire"));

						cellule.setBcasefictive(_o_Result.getString("bcasefictive"));

						if (StringUtils.equals("O", cellule.getBcasefictive()))
							existFictiveCell = true;

						cellule.setCodeposte(_o_Result.getString("codeposte"));

						cellule.setCategorieposte(_o_Result.getString("categorieposte"));
						
						cellule.setEchelonposte(_o_Result.getString("echelonposte"));

						cellule.setCodeniveau(_o_Result.getString("codeniveau"));
						
						if(! param.isIceface())
						{
							if (cellule.getLibelle() != null)
							{
								cellule.setLibelle(cellule.getLibelle().replaceAll("�", "apostrphe123"));
								cellule.setLibelle(cellule.getLibelle().replaceAll("'", "apostrphe123"));
								cellule.setLibelle(cellule.getLibelle().replaceAll("''", "apostrphe123"));
	
							}
	
							if (cellule.getNomagent() != null)
							{
								cellule.setNomagent(cellule.getNomagent().replaceAll("�", "apostrphe123"));
								cellule.setNomagent(cellule.getNomagent().replaceAll("'", "apostrphe123"));
								cellule.setNomagent(cellule.getNomagent().replaceAll("''", "apostrphe123"));
	
							}
						}

						cellule.setTaillebordure(param.getTailleBordure());

						_o_Cellules_Collection.add(cellule);
					}

				}
				catch (Exception e)
				{
					// TODO: handle exception
					e.printStackTrace();
					if (cellule != null)
						_o_Cellules_Collection.add(cellule);
					continue;
				}
				finally
				{
					cellule = null;
				}
			}

		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (_o_Result != null)
					_o_Result.close();
				if (oStatement != null)
					oStatement.close();
				if (oConnexion != null)
					oConnexion.close();
				if (_o_Session != null)
					_o_Session.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}

		List<ClsOrgCellule> allcell = new ArrayList<ClsOrgCellule>();

		List<ClsOrgCellule> filsCelluleCourante = new ArrayList<ClsOrgCellule>();

		ClsOrgCellule celluleFinale = null;

		ClsOrgCellule celluleFils = null;

		List<ClsOrgCellule> listeCellules = null;

		for (int i = 0; i < _o_Cellules_Collection.size(); i++)
		{

			listeCellules = new ArrayList<ClsOrgCellule>();

			celluleFinale = _o_Cellules_Collection.get(i);

			filsCelluleCourante = _getListeFils(celluleFinale, _o_Cellules_Collection);

			celluleFinale.setNumero(1);

			for (int ii = 0; ii < filsCelluleCourante.size(); ii++)
			{

				celluleFils = filsCelluleCourante.get(ii);

				celluleFils.setNumero(ii + 1);

				listeCellules.add(celluleFils);

			}

			celluleFinale.setNbrFils(filsCelluleCourante.size());

			celluleFinale.setFils(listeCellules);

			if (mapPrestatairesAssocies.get(celluleFinale.getCodeorganigramme()) != null)
				celluleFinale.setEffectif(mapPrestatairesAssocies.get(celluleFinale.getCodeorganigramme()) - 1);
			else
				celluleFinale.setEffectif(0);

			if (mapPrestatairesAssocies.get(celluleFinale.getCodeorganigramme()) != null)
				celluleFinale.setEffectifprestataire(mapPrestatairesAssocies.get(celluleFinale.getCodeorganigramme()));
			else
				celluleFinale.setEffectifprestataire(0);

			if (i == 0)
				celluleFinale.setIsallcellsfather(true);

			allcell.add(celluleFinale);

		}

		return allcell;

	}

	public static int _getCelluleIndex(List<ClsOrgCellule> liste, ClsOrgCellule cellule)
	{

		int index = 0;
		for (int i = 0; i < liste.size(); i++)
		{
			if (liste.get(i).getCodeorganigramme().equalsIgnoreCase(cellule.getCodeorganigramme()))
			{
				index = i;
				break;
			}
		}
		return index;
	}

	@SuppressWarnings("unchecked")
	private List<ClsOrgCellule> _getListeFils(ClsOrgCellule _o_Cellule, List<ClsOrgCellule> liste)
	{

		List<ClsOrgCellule> fils = new ArrayList<ClsOrgCellule>();

		ClsOrgCellule _o_Cell = null;

		for (int i = 0; i < liste.size(); i++)
		{

			_o_Cell = liste.get(i);

			if (_o_Cell.getCodepere().equalsIgnoreCase(_o_Cellule.getCodeorganigramme()))
				fils.add(_o_Cell);

		}

		Collections.sort(fils, new ClsGenericComparator(ClsOrgCellule.class, "codeorganigramme", "ASC"));

		return fils;
	}

	public static boolean _isLastChild(ClsOrgCellule cellulePere, ClsOrgCellule celluleFille)
	{

		if (celluleFille.getCodeorganigramme().equalsIgnoreCase(cellulePere.getFils().get(cellulePere.getFils().size() - 1).getCodeorganigramme()))
			return true;
		else
			return false;
	}

	public static boolean _isFirstChild(ClsOrgCellule cellulePere, ClsOrgCellule celluleFille)
	{

		if (celluleFille.getCodeorganigramme().equalsIgnoreCase(cellulePere.getFils().get(0).getCodeorganigramme()))
			return true;
		else
			return false;
	}

	public List<ClsOrgCellule> rebuildListe(List<ClsOrgCellule> allCells)
	{
		for (ClsOrgCellule cellule : allCells)
		{
			affecterFilsAuPereNonFictif(cellule, cellule.getCellulepere());
		}

		List<ClsOrgCellule> newAllCells = new ArrayList<ClsOrgCellule>();

		for (ClsOrgCellule cellule : allCells)
		{
			if (!StringUtils.equals("O", cellule.getBcasefictive()))
			{
				newAllCells.add(cellule);
				rebuildChildListe(cellule);
			}
		}

		return newAllCells;
	}

	private void rebuildChildListe(ClsOrgCellule cellulePere)
	{
		List<ClsOrgCellule> newCells = new ArrayList<ClsOrgCellule>();

		int i = 0;
		for (ClsOrgCellule cellule : cellulePere.getFils())
		{
			if (!StringUtils.equals("O", cellule.getBcasefictive()))
			{
				cellule.setNumero(++i);
				newCells.add(cellule);
			}
		}
		cellulePere.setFils(newCells);
	}

	private void affecterFilsAuPereNonFictif(ClsOrgCellule celluleAAffecter, ClsOrgCellule cellulePere)
	{
		if (cellulePere != null)
		{
			if (StringUtils.equals("O", cellulePere.getBcasefictive()))
			{
				affecterFilsAuPereNonFictif(celluleAAffecter, cellulePere.getCellulepere());
			}
			else
			{
				if ((!cellulePere.getFils().contains(celluleAAffecter)) && (!StringUtils.equals("O", celluleAAffecter.getBcasefictive())))
					cellulePere.getFils().add(celluleAAffecter);
			}
		}
	}

	public GeneriqueConnexionService getService()
	{

		return service;
	}

	public void setService(GeneriqueConnexionService service)
	{

		this.service = service;
	}

	public ClsOrgCellule getCellDepart()
	{

		return cellDepart;
	}

	public void setCellDepart(ClsOrgCellule cellDepart)
	{

		this.cellDepart = cellDepart;
	}

	public Map<String, List<ClsNomenclature>> getPrestatairesListMap()
	{
		return prestatairesListMap;
	}

	public void setPrestatairesListMap(Map<String, List<ClsNomenclature>> prestatairesListMap)
	{
		this.prestatairesListMap = prestatairesListMap;
	}

}
