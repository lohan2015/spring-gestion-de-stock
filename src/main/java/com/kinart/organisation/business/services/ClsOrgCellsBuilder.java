package com.kinart.organisation.business.services;

import java.beans.PropertyDescriptor;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kinart.organisation.business.vo.ClsParametreOrganigrammeVO;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.kinart.paie.business.services.utils.ParameterUtil;
import com.kinart.paie.business.services.utils.StringUtil;
import com.kinart.utils.ClsGenericComparator;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.springframework.beans.BeanUtils;

/**
 * Cette Classe permet d'initialiser toutes les cellules qui vont apparaitre sur l'organigramme Son principe : r�cup�rer toutes les c�llules qui correspondent
 * au param�trage de l'utilisateur A partir de ces cellules, construire une liste de toutes les cellules avec pour chaque cellule l'ensemble de ses propri�tes
 * (voir la classe ClsOrgCellule)
 * 
 * @author c.mbassi
 * @version 1.0
 */
public class ClsOrgCellsBuilder
{

	private ClsParametreOrganigrammeVO param;

	private GeneriqueConnexionService service;

	private Map<String, Integer> prestatairesMap = new HashMap<String, Integer>();

	public boolean existFictiveCell = false;

	/**
	 * Constructeur de la classe
	 * 
	 *            La taille de la bordure d'une cellule, cl� EP_TRAIT en table 266 des nomenclatures
	 */
	public ClsOrgCellsBuilder(GeneriqueConnexionService service, ClsParametreOrganigrammeVO parametres)
	{
		this.param = parametres;
		this.service = service;
	}

	/**
	 * @return : la liste des sites auquels l'utilisateur connect� a droit
	 */

	private String site;
	private String _getSites()
	{
		if(StringUtils.isNotBlank(site))
			return site;
		
		String strQuerySite = "";
		
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

	/**
	 * @return la liste de toutes les cellules � traiter pour obtenir la structure hi�rarchique de l'organigramme
	 */

	@SuppressWarnings("deprecation")
	public List<ClsOrgCellule> _getAllCellules()
	{

		String strTempCelluleDepart = "(rhorg.codepere ='" + param.getCelluleDepart() + "' or rhorg.codeorganigramme='02')";

		if (!ParameterUtil._isStringNull(param.getCelluleDepart()))
			strTempCelluleDepart = "(rhorg.codepere like '" + param.getCelluleDepart() + "%' or rhorg.codeorganigramme='" + param.getCelluleDepart() + "')";

		String strQuerySite = _getSites();

		String strSQL = "SELECT rhorg.*,rhniv.codecouleur as couleur, agent.nmat as nmat, agent.nom as nomagent, agent.pren as pren " + "FROM Organigramme rhorg "
				+ "LEFT JOIN Orgniveau rhniv ON (rhorg.codeniveau = rhniv.codeniveau AND rhorg.identreprise = rhniv.identreprise and rhniv.priseencomptecouleur='O') "
				+ "LEFT JOIN Orgposte poste ON (rhorg.codeposte = poste.codeposte AND rhorg.identreprise = poste.identreprise) " + "LEFT JOIN Salarie agent ON (agent.codeposte = poste.codeposte AND agent.identreprise = poste.identreprise) "
				+ "WHERE rhorg.identreprise='" + param.getDossier() + "'";

		if (!param.isAfficherPostesPrestataires())
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

		Session _o_Session = this.getService().getSession();

		List<ClsOrgCellule> _o_Cellules_Collection = new ArrayList<ClsOrgCellule>();

		ClsOrgCellule cellule = null;
		Connection oConnexion = null;
		Statement oStatement = null;
		ResultSet _o_Result = null;

		try
		{

			oConnexion = service.getConnection();
			oStatement = oConnexion.createStatement();
			_o_Result = oStatement.executeQuery(strSQL);
			String tmpPren = null;

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

					cellule.setNomagent(_o_Result.getString("nomagent"));

					tmpPren = _o_Result.getString("pren");

					if (StringUtils.isNotBlank(tmpPren))
					{
						if (StringUtils.isNotBlank(cellule.getNomagent()))
							cellule.setNomagent(cellule.getNomagent() + " " + tmpPren);
						else
							cellule.setNomagent(tmpPren);

					}

					cellule.setCodepere(_o_Result.getString("codepere"));

					cellule.setCodesite(_o_Result.getString("codesite"));

					cellule.setNivfonction(_o_Result.getString("nivfonction"));

					cellule.setLibellecourt(_o_Result.getString("libellecourt"));

					cellule.setBprestataire(_o_Result.getString("bprestataire"));

					cellule.setBcasefictive(_o_Result.getString("bcasefictive"));

					if (StringUtils.equals("O", cellule.getBcasefictive()))
						existFictiveCell = true;

					cellule.setCodeposte(_o_Result.getString("codeposte"));

					cellule.setCodeniveau(_o_Result.getString("codeniveau"));
					if (!param.isIceface())
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
				_o_Result = null;
				if (oStatement != null)
					oStatement.close();
				oStatement = null;
				if (oConnexion != null)
					oConnexion.close();
				oConnexion = null;
				service.closeSession(_o_Session);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		prestatairesMap = this.computeOrganigrammeAssociePrestataire(); // this.computeEffectifPrestataireCellCollection();

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

			celluleFinale.setEffectifprestataire(prestatairesMap.get(celluleFinale.getCodeorganigramme()) == null ? 0 : prestatairesMap.get(celluleFinale.getCodeorganigramme()));

			allcell.add(celluleFinale);

		}

		return allcell;

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
				+ "FROM Organigramme orgpere LEFT JOIN Orgniveau rhniv ON (orgpere.codeniveau = rhniv.codeniveau AND orgpere.identreprise = rhniv.identreprise) " + "LEFT JOIN Organigrammeassocie orgfils ON (" + strQueryFils
				+ " AND orgfils.identreprise = orgpere.identreprise) " + "WHERE orgpere.codeniveau = rhniv.codeniveau and orgpere.bcasefictive='N'";

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
			oConnexion = service.getConnection();
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

	// compute the number of prestataires
	@SuppressWarnings("deprecation")
	public Map<String, Integer> computeEffectifPrestataireCellCollection()
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
			oConnexion = service.getConnection();
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

	/**
	 * @param liste :
	 *            la liste qui contient toutes les cellules
	 * @param cellule :
	 *            la cellule dont on veut avoir l'index
	 * @return l'index de cellule dans la liste liste
	 */
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

	/**
	 * @param _o_Cellule :
	 *            cellule dont on veut avoir les fils
	 * @param liste :
	 *            liste de toutes les cellules
	 * @return la liste des fils de la cellule _o_Cellule
	 */

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

	public void supprimerCellulesFictivesSansFils(ClsOrgCellule cellule, List<ClsOrgCellule> allCells)
	{
		List<ClsOrgCellule> childCells = new ArrayList<ClsOrgCellule>();
		childCells.addAll(cellule.getFils());

		for (ClsOrgCellule fils : childCells)
		{
			if (StringUtils.equals("O", fils.getBcasefictive()))
			{
				if (!fils._possedeFils())
				{
					cellule.getFils().remove(fils);
					allCells.remove(fils);
				}
				else
				{
					supprimerCellulesFictivesSansFils(fils, allCells);
				}
			}
			else
			{
				supprimerCellulesFictivesSansFils(fils, allCells);
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

}
