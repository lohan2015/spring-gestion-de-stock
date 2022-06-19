package com.kinart.paie.business.utils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.kinart.paie.business.model.ParamData;
import com.kinart.paie.business.model.Salarie;
import com.kinart.paie.business.services.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ClsJournalPaieGlobal implements IJournalPaie
{

	String cdos;

	String cuti;

	Integer ctab;

	String clas;

	String niv1min;

	String niv1max;

	String niv2min;

	String niv2max;

	String niv3min;

	String niv3max;

	String nmatmin;

	String nmatmax;

	String per1;

	String per2;

	Integer nbul;

	Integer cas;

	String tri1;

	String tri2;

	String tri3;

	GeneriqueConnexionService service;

	private Integer nbrLignes = 20;

	private Integer nbrColonnes = 9;

	/**
	 * D�claration des variables de classe
	 */
	LstSession listIdSession;

	/**
	 * Tables des formule/libell�
	 */

	FormuleLibelle tabForm[][];

	/**
	 * Liste des �l�ments de la table de param�trage 501
	 */
	List<ParamData> tabParametrage;

	/**
	 * Liste des salar�s sans tri par classe
	 */
	List<Salarie> tabsalaries;

	/**
	 * Ligne total
	 */
	List<BigDecimal> total;
	
	public String tablePhysiqueJournal="JournalPaie";
	
	HttpServletRequest request;

	public ClsJournalPaieGlobal(HttpServletRequest request)
	{
		this.request = request;
	}
	
	public ClsJournalPaieGlobal()
	{
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#initLstSessionID(java.lang.String, java.lang.String)
	 */
	public LstSession initLstSessionID(String cdos, String cuti)
	{
		String dateJour;
		String dateJourSessMax;

		LstSession listSession = new LstSession();
		dateJour = new ClsDate(new Date()).getDateS("yyyyMMddhhmm");
		listSession.setSessionId(cdos.concat(cuti).concat(dateJour));
		listSession.setSessionSuppMin(cdos.concat(cuti));
		dateJourSessMax = new ClsDate(new Date()).getDateS("yyyyMMdd");
		listSession.setSessionSuppMax(cdos.concat(cuti).concat(dateJourSessMax));

		return listSession;
	}

	// Fonction de suppression des donn�es de la table temporaire

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#purgerTablesTemp(java.lang.String)
	 */
	public void purgerTablesTemp(String CLE)
	{
		String queryString = "delete from "+tablePhysiqueJournal+" where SESSIONID like '" + CLE + "%'";
		try
		{
			Session session = service.getSession();
			session.createSQLQuery(queryString).executeUpdate();
			service.closeSession(session);
			ParameterUtil.println("Suppression des donn�es des tables termin�e  ");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	// Fonction d'initialisation des donn�es

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#initData()
	 */
	public void initData()
	{

		// tabsalaries = null;
		// total.set(i, new BigDecimal(0));
		tabForm = new FormuleLibelle[nbrLignes+1][nbrColonnes+1];
		for (int i = 1; i <= nbrLignes; i++)
		{
			for (int j = 1; j <= nbrColonnes; j++)
			{
				tabForm[i][j] = new FormuleLibelle();
				tabForm[i][j].setLibelle("");
				tabForm[i][j].setFormule("");
				tabForm[i][j].setFormulepos("");
				tabForm[i][j].setFormuleneg("");
				tabForm[i][j].setColonnealire("M");
			}

		}
		tabsalaries = new ArrayList<Salarie>();
	}

	// Fonction d'initialisation de la liste des param�tres en table de param�trage

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#getRowRhfnom(java.lang.String, java.lang.Integer)
	 */
	public List<ParamData> getRowRhfnom(String cdos, Integer ctab)
	{
		List<ParamData> lstEltsParamtrage = new ArrayList<ParamData>();
		String queryString = "select cacc, nume, vall " + "from ParamData " + " where (cdos = '" + cdos + "')" + " and (ctab = " + ctab + ")" + " and (nume in(1,2,3))"
				+ " and (cacc like 'L%')" + " ORDER BY cacc, nume";
		// r�cup�ration des r�sultats
		try
		{
			List res = service.find(queryString);
			// service.deleteFromTable(queryString);
			ParameterUtil.println("Lecture des donn�es termin�e ");
			ParameterUtil.println("Taille des donn�es : " + res.size());
			Object[] line;
			ParamData rnom = null;
			for (Object obj : res)
			{
				line = (Object[]) obj;
				rnom = new ParamData();
				if (line[0] != null)
				{
					rnom.setCacc((String) line[0]);
					ParameterUtil.println("ma valeur = " + line[0]);
					rnom.setNume(new Integer(line[1].toString()));
					ParameterUtil.println("ma valeur1 = " + line[1].toString());
					rnom.setVall((String) line[2]);
					ParameterUtil.println("ma valeur2 = " + line[2]);
					lstEltsParamtrage.add(rnom);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return lstEltsParamtrage;
	}

	// Lecture des cl�s pour le calcul des valeurs des cellules

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#lectureCle(java.lang.String, java.lang.Integer)
	 */
	public void lectureCle(String cdos, Integer ctab)
	{
		String strVall;
		Integer numlign;
		Integer numcol;
		int maxLigne = 0;
		int maxColonne = 0;
		tabParametrage = getRowRhfnom(cdos, ctab);
		ParameterUtil.println("Taille de la tables des param�trages = " + tabParametrage.size());
		for (ParamData ligne : tabParametrage)
		{
			strVall = ligne.getCacc();
			if(StringUtil.nvl(strVall, StringUtils.EMPTY).length()!=10)
				continue;

			if (strVall.substring(0, 3).equalsIgnoreCase("LIG") && strVall.substring(3, 5).matches("([0-9]*)") && strVall.substring(5, 8).equalsIgnoreCase("COL") && strVall.substring(8, 10).matches("([0-9]*)"))
			{
				numlign = new Integer(strVall.substring(3, 5));
				numcol = new Integer(strVall.substring(8, 10));

				if (numlign > /*nbrLignes*/50)
					continue;

				if (numcol > nbrColonnes)
					continue;

				if (numcol > maxColonne)
					maxColonne = numcol;

				if (numlign > maxLigne)
					maxLigne = numlign;

				if (ligne.getNume().equals(1))
				{
					ParameterUtil.println("num�ro ligne = " + numlign);
					ParameterUtil.println("num�ro colonne = " + numcol);
					ParameterUtil.println("Valeur cellule = " + ligne.getVall());
					tabForm[numlign][numcol].setFormule(ligne.getVall());
					tabForm[numlign][numcol].generateFormulePosAndNeg();
				}
				else if (ligne.getNume().equals(2))
				{
					ParameterUtil.println("num�ro ligne = " + numlign);
					ParameterUtil.println("num�ro colonne = " + numcol);
					ParameterUtil.println("Valeur cellule = " + ligne.getVall());
					tabForm[numlign][numcol].setLibelle(ligne.getVall());
				}
				else if (ligne.getNume().equals(3))
				{
					if(StringUtils.isNotBlank(ligne.getVall()))
						tabForm[numlign][numcol].setColonnealire(ligne.getVall());
				}
			}
			else
			{
				ParameterUtil.println("Une erreur c'est produite dans le if");
				break;
			}
		}

		//this.nbrColonnes = maxColonne;
		this.nbrLignes = maxLigne;
	}

	// Proc�dure permettant d'ins�rer les donn�es dans la table temporaire en base de donn�es

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#insereRhtedjpai(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.String[], org.hibernate.Session, boolean)
	 */
	public void insereRhtedjpai(String idSession, String cdos, String NIV1, String NIV2, String NIV3, String NMAT, Integer NUME, String[] COLS, Session session, boolean montants)
	{
		int retour = 0;
		if (montants)
		{

		}

		String queryString = "INSERT INTO " + tablePhysiqueJournal + " VALUES ('" + idSession + "', '" + cdos + "', '" + NIV1 + "', '" + NIV2 + "', '" + NIV3 + "', '" + NMAT + "', " + NUME;

		if(COLS!=null && COLS.length>=1){
			for (int i = 1; i < COLS.length; i++)
				queryString += " , " + TypeBDUtil.convertirEnChaineDeCaractere1(COLS[i]);
			} else {
				queryString += ", '0' , '0' , '0' , '0' , '0' , '0' , '0' , '0' , '0'";
		}
		queryString += ")";

		ParameterUtil.println("La requ�te d'insertion  = " + queryString);

		retour = session.createSQLQuery(queryString).executeUpdate();
		
		session.flush();
		session.clear();

	}

	// Proc�dure permettant de g�n�rer l'ent�te de la table du journal

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#genereEntete(java.lang.String)
	 */
	public void genereEntete(String cdos)
	{

		Integer j = 1;
		Session session = service.getSession();
		// Transaction tx = session.getTransaction();
		String[] colonnes = new String[nbrColonnes + 1];
		try
		{
			while ((j < nbrLignes + 1) /*&& StringUtils.isNotBlank(tabForm[j][1].libelle)*/)
			{
				colonnes = new String[nbrColonnes + 1];
				for(int k = 1; k<= nbrColonnes; k++)
					colonnes[k] = StringUtil.nvl(tabForm[j][k].libelle," ");
				insereRhtedjpai(listIdSession.sessionId, cdos, "LIG", "COL", "ZZZZZZZZ", "ZZZZZZ", j, colonnes, session, false);
				j++;
				if (j % 20 == 0)
					session.flush();
				ParameterUtil.println("valeur compteur " + j);
			}
			// tx.commit();
			ParameterUtil.println("Requ�te d'insertion de l'ent�te termin�e");
		}
		catch (Exception e)
		{
			// tx.rollback();
			e.printStackTrace();
		}
		finally
		{
			service.closeSession(session);
		}

	}

	// liste des salari�s

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#getListeSalarie(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void getListeSalarie(String cdos, String Tri_1, String Tri_2, String Tri_3, String niv1Min, String niv1Max, String niv2Min, String niv2Max, String niv3Min, String niv3Max, String nmatMin, String nmatMax,
			String clas,String queryExt)
	{
		String queryString;
		String table;
		if (cas.equals(1))
			table="Rhtcalcul";
		else
			table="Rhtcumul";
		
		// Contrainte sur les autorisation du user
		String autorisationagent = "1=1";//ClsAutorisationsUtilisateur._getChaineAutorisations(request, "a", true);
		
		
		if (StringUtils.isBlank(clas))
		{
			// Cas de la requ�te sans restriction sur la classe
			queryString = "select a.idEntreprise" + ", case when " + Tri_1 + " = '1' then niv1 else 'ZZZ' end  " + ", case when " + Tri_2 + " = '1' then niv2 else 'ZZZ' end  " + ", case when " + Tri_3
					+ " = '1' then niv3 else 'ZZZZZZZZ' end  " + ", a.nmat " + "from Rhpagent a where a.idEntreprise = '" + cdos + "'"
					+ ((StringUtils.isNotBlank(niv1Min))? " and a.niv1 >= '" + niv1Min + "'" : "")
					+ ((StringUtils.isNotBlank(niv1Max))? " and a.niv1 <= '" + niv1Max + "'" : "")
					+ ((StringUtils.isNotBlank(niv2Min))? " and a.niv2 >= '" + niv2Min + "'" : "")
					+ ((StringUtils.isNotBlank(niv2Max))? " and a.niv2 <= '" + niv2Max + "'" : "")
					+ ((StringUtils.isNotBlank(niv3Min))? " and a.niv3 >= '" + niv3Min + "'" : "")
					+ ((StringUtils.isNotBlank(niv3Max))? " and a.niv3 <= '" + niv3Max + "'" : "")
					+ ((StringUtils.isNotBlank(nmatMin))? " and a.nmat >= '" + nmatMin + "'" : "")
					+ ((StringUtils.isNotBlank(nmatMax))? " and a.nmat <= '" + nmatMax + "'" : "");
		}
		else
		{
			// cas de la requ�te avec restriction sur la classe
			queryString = "select a.idEntreprise" + ", case when " + Tri_1 + " = '1' then niv1 else 'ZZZ' end  " + ", case when " + Tri_2 + " = '1' then niv2 else 'ZZZ' end  " + ", case when " + Tri_3
					+ " = '1' then niv3 else 'ZZZZZZZZ' end  " + ", a.nmat " + "from Rhpagent a where a.idEntreprise = '" + cdos + "'"
					+ ((StringUtils.isNotBlank(niv1Min))? " and a.niv1 >= '" + niv1Min + "'" : "")
					+ ((StringUtils.isNotBlank(niv1Max))? " and a.niv1 <= '" + niv1Max + "'" : "")
					+ ((StringUtils.isNotBlank(niv2Min))? " and a.niv2 >= '" + niv2Min + "'" : "")
					+ ((StringUtils.isNotBlank(niv2Max))? " and a.niv2 <= '" + niv2Max + "'" : "")
					+ ((StringUtils.isNotBlank(niv3Min))? " and a.niv3 >= '" + niv3Min + "'" : "")
					+ ((StringUtils.isNotBlank(niv3Max))? " and a.niv3 <= '" + niv3Max + "'" : "")
					+ ((StringUtils.isNotBlank(nmatMin))? " and a.nmat >= '" + nmatMin + "'" : "")
					+ ((StringUtils.isNotBlank(nmatMax))? " and a.nmat <= '" + nmatMax + "'" : "")
					+ " and a.clas = '" + clas + "'";// +
			// " order by (cdos, niv1, niv2, niv3, nmat)";
		}
		if(!StringUtils.isBlank(queryExt)) queryString += " and " + queryExt;
		queryString += " and " + autorisationagent;
		queryString+=" And exists (Select b.nmat From "+table+" b where a.idEntreprise = b.idEntreprise and a.nmat = b.nmat and b.nbul = "+nbul;
		if (cas.equals(1))
			queryString+=" and b.aamm = '"+per1+"' ";
		else
			queryString+=" and b.aamm >= '"+per1+"' and b.aamm <= '"+per2+"' ";
		queryString+=" ) ";
		

		// r�cup�ration des r�sultats
		ParameterUtil.println("La cha�ne de requ�te pour agent = " + queryString);
		List res = service.find(queryString);
		ParameterUtil.println("J'ai d�j� ex�cut� la requ�te");
		Object[] line;
		Salarie agent = null;
		for (Object obj : res)
		{
			line = (Object[]) obj;
			agent = new Salarie();
			// ParameterUtil.println("matricule = " + line[4].toString());
			agent.setNmat(line[4].toString());
			// ParameterUtil.println("dossier = " + line[0].toString());
			agent.setIdentreprise(new Integer(line[0].toString()));
			// ParameterUtil.println("Niveau 1 = " + line[1].toString());
			agent.setNiv1(line[1].toString());
			// ParameterUtil.println("Niveau 2 = " + line[2].toString());
			agent.setNiv2(line[2].toString());
			// ParameterUtil.println("Niveau 3 = " + line[3].toString());
			agent.setNiv3(line[3].toString());
			tabsalaries.add(agent);
		}
	}
	Map<String,BigDecimal> valeurspos = new HashMap<String, BigDecimal>();
	Map<String,BigDecimal> valeursneg = new HashMap<String, BigDecimal>();
	
	public void buildAllDatas(String cdos,String niv1Min, String niv1Max, String niv2Min, String niv2Max, String niv3Min, String niv3Max, String nmatMin, String nmatMax,
			String clas)
	{
		
		valeurspos = new HashMap<String, BigDecimal>();
		valeursneg = new HashMap<String, BigDecimal>();
		
		String table;
		if (cas.equals(1))
			table="Rhtcalcul";
		else
			table="Rhtcumul";
		
		String queryAgent;
		
		if (StringUtils.isBlank(clas))
		{
			queryAgent = "Rhpagent a where a.idEntreprise = '" + cdos + "'"
					+ ((StringUtils.isNotBlank(niv1Min))? " and a.niv1 >= '" + niv1Min + "'" : "")
					+ ((StringUtils.isNotBlank(niv1Max))? " and a.niv1 <= '" + niv1Max + "'" : "")
					+ ((StringUtils.isNotBlank(niv2Min))? " and a.niv2 >= '" + niv2Min + "'" : "")
					+ ((StringUtils.isNotBlank(niv2Max))? " and a.niv2 <= '" + niv2Max + "'" : "")
					+ ((StringUtils.isNotBlank(niv3Min))? " and a.niv3 >= '" + niv3Min + "'" : "")
					+ ((StringUtils.isNotBlank(niv3Max))? " and a.niv3 <= '" + niv3Max + "'" : "")
					+ ((StringUtils.isNotBlank(nmatMin))? " and a.nmat >= '" + nmatMin + "'" : "")
					+ ((StringUtils.isNotBlank(nmatMax))? " and a.nmat <= '" + nmatMax + "'" : "");
		}
		else
		{
			queryAgent = "Rhpagent a where a.idEntreprise = '" + cdos + "'"
					+ ((StringUtils.isNotBlank(niv1Min))? " and a.niv1 >= '" + niv1Min + "'" : "")
					+ ((StringUtils.isNotBlank(niv1Max))? " and a.niv1 <= '" + niv1Max + "'" : "")
					+ ((StringUtils.isNotBlank(niv2Min))? " and a.niv2 >= '" + niv2Min + "'" : "")
					+ ((StringUtils.isNotBlank(niv2Max))? " and a.niv2 <= '" + niv2Max + "'" : "")
					+ ((StringUtils.isNotBlank(niv3Min))? " and a.niv3 >= '" + niv3Min + "'" : "")
					+ ((StringUtils.isNotBlank(niv3Max))? " and a.niv3 <= '" + niv3Max + "'" : "")
					+ ((StringUtils.isNotBlank(nmatMin))? " and a.nmat >= '" + nmatMin + "'" : "")
					+ ((StringUtils.isNotBlank(nmatMax))? " and a.nmat <= '" + nmatMax + "'" : "")
					+ " and a.clas = '" + clas + "'";
		}
		queryAgent+=" And exists (Select b.nmat From "+table+" b where a.idEntreprise = b.idEntreprise and a.nmat = b.nmat and b.nbul = "+nbul;
		if (cas.equals(1))
			queryAgent+=" and b.aamm = '"+per1+"' ";
		else
			queryAgent+=" and b.aamm >= '"+per1+"' and b.aamm <= '"+per2+"'  and b.aamm not like '%99'";
		queryAgent+=" ) ";
		
		String requeteFinale;
		
		
		BigDecimal val = null;
		String nmat;
		for (int i = 1; i <= nbrLignes; i++)
		{
			if (tabForm[i][1].formule != null)
			{
				for (int j = 1; j <= nbrColonnes; j++)
				{
					if ((tabForm[i][j] != null) && (tabForm[i][j].formule != null))
					{
						if (tabForm[i][j].formule.length() > 3)
						{
							if(StringUtils.isNotBlank(tabForm[i][j].formulepos))
							{	
								requeteFinale = " select c.nmat, sum(c."+tabForm[i][j].getColonne()+")" + " from "+table+" c ";
								requeteFinale+=","+queryAgent+" and a.idEntreprise=c.idEntreprise and a.nmat=c.nmat AND (c.aamm not like '%99') and (c.aamm >= '" + per1 + "') ";
								requeteFinale+=" AND (c.aamm <= '" + per2 + "') AND (c.nbul = "+nbul+") AND (c.rubq in (" + tabForm[i][j].formulepos + ") ) group by c.nmat";
								
								List<Object[]> res = (List<Object[]>) service.find(requeteFinale);
								for(Object[] obj : res)
								{
									nmat = (String)obj[0];
									val = BigDecimal.ZERO;
									if(obj[1] != null) val = new BigDecimal(ClsObjectUtil.getDoubleFromObject(obj[1]));
									valeurspos.put(nmat+"-"+i+"-"+j, val);
								}		
							}
							
							if(StringUtils.isNotBlank(tabForm[i][j].formuleneg))
							{	
								requeteFinale = " select c.nmat, sum(c."+tabForm[i][j].getColonne()+")" + " from "+table+" c ";
								requeteFinale+=","+queryAgent+" and a.idEntreprise=c.idEntreprise and a.nmat=c.nmat AND (c.aamm not like '%99') and (c.aamm >= '" + per1 + "') ";
								requeteFinale+=" AND (c.aamm <= '" + per2 + "') AND (c.nbul = "+nbul+") AND (c.rubq in (" + tabForm[i][j].formuleneg + ") ) group by c.nmat";
								
								List<Object[]> res = (List<Object[]>) service.find(requeteFinale);
								for(Object[] obj : res)
								{
									nmat = (String)obj[0];
									val = BigDecimal.ZERO;
									if(obj[1] != null) val = new BigDecimal(ClsObjectUtil.getDoubleFromObject(obj[1]));
									valeursneg.put(nmat+"-"+i+"-"+j, val);
								}	
							}
						}
					}
				}
			}
		}
	}

	// lecture montant rubrique selon le cas du module : PostPaie -> cas = 1; Cumul -> cas = 2

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#lectureMontantRub(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String)
	 */
	public BigDecimal lectureMontantRub(String RUBQ, String cdos, String NMAT, String periode1, String PER2, Integer cas, Integer nbul, String colonnealire)
	{
		BigDecimal valmont = new BigDecimal(0);
		String queryString;
		periode1 = new ClsDate(periode1, "yyyyMM").getDateS("yyyyMM");
		if (cas.equals(1) && (RUBQ.length() >= 4))
		{
			queryString = " select sum("+colonnealire+")" + " from Rhtcalcul " + " WHERE (cdos = '" + cdos + "')" + " AND (nmat = '" + NMAT + "')" + " AND (aamm = '" + periode1 + "')" + " AND (nbul = "
					+ nbul + ")" + " AND (rubq = '" + RUBQ + "')";
		}
		else
		{
			queryString = " select sum("+colonnealire+")" + " from Rhtcumul " + " WHERE (cdos = '" + cdos + "')" + " AND (nmat = '" + NMAT + "')" + " AND (aamm >= '" + periode1 + "')" + " AND (aamm <= '"
					+ PER2 + "')" + " AND (aamm not like '%99') and (nbul = "+nbul+")" + " AND (rubq = '" + RUBQ + "')";
		}
		ParameterUtil.println("La requ�te de calcul du montant = " + queryString);

		//
		List res = service.find(queryString);
		if ((res.get(0) != null) && (res.get(0) != ""))
		{
			valmont = new BigDecimal(ClsObjectUtil.getDoubleFromObject(res.get(0)));
		}
		else
		{
			return valmont;
		}
		//
		ParameterUtil.println("La valeur renvoy�e est : " + valmont);
		return valmont;
	}
	
	public BigDecimal lectureMontantRub(String formulePos, String formuleNeg, String cdos, String NMAT, String periode1, String PER2, Integer cas, Integer nbul, String colonnealire)
	{
		BigDecimal valmontpos = new BigDecimal(0);
		BigDecimal valmontneg = new BigDecimal(0);
		String queryString;
		periode1 = new ClsDate(periode1, "yyyyMM").getDateS("yyyyMM");
		
		//Traitement de la partie positive
		if(StringUtils.isNotBlank(formulePos))
		{
			if (cas.equals(1))
			{
				queryString = " select sum("+colonnealire+")" + " from Rhtcalcul " + " WHERE (cdos = '" + cdos + "')" + " AND (nmat = '" + NMAT + "')" + " AND (aamm = '" + periode1 + "')" + " AND (nbul = "
						+ nbul + ")" + " AND (rubq in (" + formulePos + ") )";
			}
			else
			{
				queryString = " select sum("+colonnealire+")" + " from Rhtcumul " + " WHERE (cdos = '" + cdos + "')" + " AND (nmat = '" + NMAT + "')" + " AND (aamm >= '" + periode1 + "')" + " AND (aamm <= '"
						+ PER2 + "')" + " AND (nbul = "+nbul+")" + " AND (rubq in (" + formulePos + ") )";
			}
	
			//
			List res = service.find(queryString);
			if ((res.get(0) != null) && (res.get(0) != ""))
				valmontpos = new BigDecimal(ClsObjectUtil.getDoubleFromObject(res.get(0)));
			
		}
		
		//Traitement de la partie positive
		if(StringUtils.isNotBlank(formuleNeg))
		{
			if (cas.equals(1))
			{
				queryString = " select sum("+colonnealire+")" + " from Rhtcalcul " + " WHERE (cdos = '" + cdos + "')" + " AND (nmat = '" + NMAT + "')" + " AND (aamm = '" + periode1 + "')" + " AND (nbul = "
						+ nbul + ")" + " AND (rubq in (" + formuleNeg + ") )";
			}
			else
			{
				queryString = " select sum("+colonnealire+")" + " from Rhtcumul " + " WHERE (cdos = '" + cdos + "')" + " AND (nmat = '" + NMAT + "')" + " AND (aamm >= '" + periode1 + "')" + " AND (aamm <= '"
						+ PER2 + "')" + " AND (aamm not like '%99') and (nbul = "+nbul+")" + " AND (rubq in (" + formuleNeg + ") )";
			}
	
			//
			List res = service.find(queryString);
			if ((res.get(0) != null) && (res.get(0) != ""))
				valmontneg = new BigDecimal(ClsObjectUtil.getDoubleFromObject(res.get(0)));
			
		}
		
		
		//
		ParameterUtil.println("La valeur renvoy�e est : " + valmontpos + " et "+valmontneg);
		return valmontpos.subtract(valmontneg);
	}

	// Insertion des totaux dans la table temporaire

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#genereRegTotalGeneral(java.lang.String)
	 */
	public void genereRegTotalGeneral(String idsession)
	{
		String queryString = "INSERT INTO "+tablePhysiqueJournal+" select idsession,cdos,niv1,niv2,niv3,nmat, nume ";
		for(int i=1; i<= nbrColonnes; i++)
			queryString+=" , "+TypeBDUtil.convertirEnChaineDeCaractere2("dcol"+i)+" as col"+i;
		
		queryString+=" from (select '" + idsession + "' as idsession, cdos as cdos, 'ZZZ' as niv1, 'ZZZ' as niv2, 'ZZZZZZZZ' as niv3, 'ZZZZZZ' as nmat,nume";
		for(int i=1; i<= nbrColonnes; i++)
			queryString+=" , sum(" + TypeBDUtil.convertirEnNumeric("col"+i) + ") as dcol"+i+" ";

		queryString+=" from "+tablePhysiqueJournal+" where (sessionid = '" + idsession + "')";

		queryString+=" and (niv1 != 'ZZZ')  and (niv2 != 'ZZZ') and (niv3 != 'ZZZZZZZZ') and (nmat != 'ZZZZZZ') group by cdos, nume) test";

		//System.out.print(queryString);
		
		this.executeQuery(queryString, service);

	}
	
	private void executeQuery(String queryString, GeneriqueConnexionService connexionService){
//		BasicDataSource bd = (BasicDataSource) ServiceFinder.findBean("dataSource");
//
//		String strDriver = bd.getDriverClassName();
//
//		String url = bd.getUrl();
//
//		String user = bd.getUsername();
//
//		String password = bd.getPassword();
		//Connection connection = null;
		
			try {
//			Class.forName(strDriver);
//
//			connection = DriverManager.getConnection(url, user, password);
//
//			int ret = connection.prepareStatement(queryString).executeUpdate();
				connexionService.executeNativeSQLQuery(queryString);

				ParameterUtil.println("Requete �x�cut�e avec succ�s");

		}catch(Exception e){
			ParameterUtil.println("Requete non ex�cut�e");
			e.printStackTrace();
		}
	}

	// Insertion des valeur cas niveau1 de regroupement

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#genereRegNiv1(java.lang.String)
	 */
	public void genereRegNiv1(String idsession)
	{
		String queryString = "insert into "+tablePhysiqueJournal+" select idsession,cdos,niv1,niv2,niv3,nmat, nume ";
		for(int i=1; i<= nbrColonnes; i++)
			queryString+=" , "+TypeBDUtil.convertirEnChaineDeCaractere2("dcol"+i)+" as col"+i;
		
		queryString+=" from (select '" + idsession + "' as idsession, cdos as cdos, 'ZZZ' as niv1, 'ZZZ' as niv2, 'ZZZZZZZZ' as niv3, 'ZZZZZZ' as nmat,nume";
		for(int i=1; i<= nbrColonnes; i++)
			queryString+=" , sum(" + TypeBDUtil.convertirEnNumeric("col"+i) + ") as dcol"+i+" ";

		queryString+=" from "+tablePhysiqueJournal+" where (sessionid = '" + idsession + "')";

		queryString+=" and (niv1 != 'ZZZ')  and (niv2 != 'ZZZ') and (niv3 != 'ZZZZZZZZ') and (nmat != 'ZZZZZZ') group by cdos, nume, niv1) test";


		//System.out.print(queryString);


		this.executeQuery(queryString, service);

	}

	// Insertion des valeur cas niveau2 de regroupement
	
	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#genereRegNiv2(java.lang.String)
	 */
	public void genereRegNiv2(String idsession)
	{
		String queryString = "insert into "+tablePhysiqueJournal+" select idsession,cdos,niv1,niv2,niv3,nmat, nume ";
		for(int i=1; i<= nbrColonnes; i++)
			queryString+=" , "+TypeBDUtil.convertirEnChaineDeCaractere2("dcol"+i)+" as col"+i;
		
		queryString+=" from (select '" + idsession + "' as idsession, cdos as cdos, 'ZZZ' as niv1, 'ZZZ' as niv2, 'ZZZZZZZZ' as niv3, 'ZZZZZZ' as nmat,nume";
		for(int i=1; i<= nbrColonnes; i++)
			queryString+=" , sum(" + TypeBDUtil.convertirEnNumeric("col"+i) + ") as dcol"+i+" ";

		queryString+=" from "+tablePhysiqueJournal+" where (sessionid = '" + idsession + "')";

		queryString+=" and (niv1 != 'ZZZ')  and (niv2 != 'ZZZ') and (niv3 != 'ZZZZZZZZ') and (nmat != 'ZZZZZZ') group by cdos, nume, niv1,niv2) test";


		//System.out.print(queryString);


		this.executeQuery(queryString, service);

	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#genereRegNiv3(java.lang.String)
	 */
	public void genereRegNiv3(String idsession)
	{
		String queryString = "insert into "+tablePhysiqueJournal+" select idsession,idEntreprise,niv1,niv2,niv3,nmat, nume ";
		for(int i=1; i<= nbrColonnes; i++)
			queryString+=" , "+TypeBDUtil.convertirEnChaineDeCaractere2("dcol"+i)+" as col"+i;
		
		queryString+=" from (select '" + idsession + "' as idsession, cdos as cdos, 'ZZZ' as niv1, 'ZZZ' as niv2, 'ZZZZZZZZ' as niv3, 'ZZZZZZ' as nmat,nume";
		for(int i=1; i<= nbrColonnes; i++)
			queryString+=" , sum(" + TypeBDUtil.convertirEnNumeric("col"+i) + ") as dcol"+i+" ";

		queryString+=" from "+tablePhysiqueJournal+" where (sessionid = '" + idsession + "')";

		queryString+=" and (niv1 != 'ZZZ')  and (niv2 != 'ZZZ') and (niv3 != 'ZZZZZZZZ') and (nmat != 'ZZZZZZ') group by cdos, nume, niv1, niv2, niv3) test";


		//System.out.print(queryString);


		this.executeQuery(queryString, service);

	}
	
	// G�n�ration du regroupement

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#genereRegroupement(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void genereRegroupement(String Tri_1, String Tri_2, String Tri_3, String session)
	{
		genereRegTotalGeneral(session);
		if (Tri_1.equalsIgnoreCase("1"))
		{
			genereRegNiv1(session);
		}
		if (Tri_2.equalsIgnoreCase("1"))
		{
			genereRegNiv2(session);
		}
		if (Tri_3.equalsIgnoreCase("1"))
		{
			genereRegNiv3(session);
		}

	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#ecrireSalarie(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	public void ecrireSalarie(String cdos, String NMAT, String niv1, String niv2, String niv3, String periode1, String PER2, Integer cas, Integer nbul)
	{
		BigDecimal w_total[] = new BigDecimal[nbrColonnes + 1];
		String w_rub;
		String w_signe;
		BigDecimal w_montrub;

		Session session = service.getSession();
		Transaction tx = session.beginTransaction();
		// newcrub = ClsRubriqueTreater._completerLongueurRubrique(strCodeRubrique);
		BigDecimal valPos;
		BigDecimal valNeg;
		boolean nepasinserer = true;
		Map<Integer, String[]> tableau = new HashMap<Integer, String[]>();
		try
		{
			for (int i = 1; i <= nbrLignes; i++)
			{
				if (tabForm[i][1].formule != null)
				{
					for (int j = 1; j <= nbrColonnes; j++)
					{
						w_total[j] = new BigDecimal(0);
						if ((tabForm[i][j] != null) && (tabForm[i][j].formule != null))
						{
							if (tabForm[i][j].formule.length() > 3)
							{
								valPos = NumberUtils.bdnvl(valeurspos.get(NMAT+"-"+i+"-"+j),BigDecimal.ZERO);
								valNeg =  NumberUtils.bdnvl(valeursneg.get(NMAT+"-"+i+"-"+j),BigDecimal.ZERO);
								w_montrub = valPos.subtract(valNeg);
								//w_montrub = lectureMontantRub(tabForm[i][j].formulepos,tabForm[i][j].formuleneg, cdos, NMAT, periode1, PER2, cas, nbul,tabForm[i][j].getColonne());
								ParameterUtil.println("Le montant lu : " + w_montrub);
								w_total[j] = w_total[j].add(w_montrub);
								nepasinserer = nepasinserer && (w_total[j].compareTo(BigDecimal.ZERO)==0);
								
//								int k = 0;
//								while ((k <= (tabForm[i][j].formule.length() - 1)))
//								{
//									w_rub = StringUtils.oraSubstring(tabForm[i][j].formule, k + 1, 4);// .substring(k, 4);
//									ParameterUtil.println("Ma formule de calcul " + tabForm[i][j].formule);
//									// ParameterUtil.println("La formule ici est : " + w_rub);
//									k += 4;
//									w_signe = String.valueOf(tabForm[i][j].formule.toCharArray()[k]);
//									k++;
//									w_montrub = lectureMontantRub(w_rub, cdos, NMAT, periode1, PER2, cas, nbul,tabForm[i][j].getColonne());
//									if (w_signe.equalsIgnoreCase("+"))
//									{
//										ParameterUtil.println("Le montant lu : " + w_montrub);
//										w_total[j] = w_total[j].add(w_montrub);
//										ParameterUtil.println("Le montant ins�r� addition : " + w_total[j]);
//									}
//									else if (w_signe.equalsIgnoreCase("-"))
//									{
//										w_total[j] = w_total[j].subtract(w_montrub);
//										ParameterUtil.println("Le montant ins�r� soustraction : " + w_total[j]);
//									}
//								}
							}

						}
						else
							continue;
					}
					//On va fabriquer un tableau qui permet de n'inserer un agent que si au moins une des valeurs est non nulle
					String[] colonnes = new String[nbrColonnes + 1];
					for(int ii=1;ii<=nbrColonnes; ii++)
						colonnes[ii] = ClsStringUtil.truncateTo3Decimal(NumberUtils.bdnvl(w_total[ii],0)).toString();
					tableau.put(i, colonnes);
					//insereRhtedjpai(listIdSession.sessionId, cdos, niv1, niv2, niv3, NMAT, i, colonnes, session, true);
					
				}
				else
					continue;
			}
			if(insererJournalToutesValeursNulles) nepasinserer = false;
			if(!nepasinserer)
			{
				for (int i = 1; i <= nbrLignes; i++)
				{
					String[] colonnes = tableau.get(i);
					insereRhtedjpai(listIdSession.sessionId, cdos, niv1, niv2, niv3, NMAT, i, colonnes, session, true);
				}		
			}
			tableau.clear();
			tableau = null;
			w_total = null;
			ParameterUtil.println("Requ�te d'insertion des montants termin�e");
			// session.flush();
			tx.commit();
		}
		catch (Exception e)
		{
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
		finally
		{
			this.getService().closeSession(session);
		}
	}

	public boolean insererJournalToutesValeursNulles = true;
	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#initJournalPaie(java.lang.String, java.lang.String, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void initJournalPaie(String cdos, String cuti, Integer ctab, String clas, String niv1Min, String niv1Max, String niv2Min, String niv2Max, String niv3Min, String niv3Max, String nmatMin, String nmatMax,
			String periode1, String PER2, Integer nbul, Integer cas, String tri1, String tri2, String tri3,String queryExt)
	{

		this.cdos = cdos;

		this.cuti = cuti; 

		this.ctab = ctab;

		this.clas = clas;

		niv1min = niv1Min;

		niv1max = niv1Max;

		niv2min = niv2Min;

		niv2max = niv2Max;

		niv3min = niv3Min;

		niv3max = niv3Max;

		nmatmin = nmatMin;

		nmatmax = nmatMax;

		per1 = periode1;

		per2 = PER2;

		this.nbul = nbul;

		this.cas = cas;

		this.tri1 = tri1;

		this.tri2 = tri2;

		this.tri3 = tri3;

		initData();

		listIdSession = initLstSessionID(cdos, cuti);
		ParameterUtil.println("La session a �t� bien initialis�e");
		lectureCle(cdos, ctab);
		try
		{
			purgerTablesTemp(listIdSession.sessionSuppMin);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		genereEntete(cdos);
		getListeSalarie(cdos, tri1, tri2, tri3, niv1min, niv1max, niv2min, niv2max, niv3min, niv3max, nmatmin, nmatmax, clas,queryExt);
		
		this.buildAllDatas(cdos, niv1Min, niv1Max, niv2Min, niv2Max, niv3Min, niv3Max, nmatMin, nmatMax, clas);
		
		String clang = ParameterUtil.getSessionObject(request, ParameterUtil.SESSION_LANGUE);
		String ins = service.findAnyColumnFromNomenclature(cdos, clang, "4", "", "2").getVall();
				//ClsConfigurationParameters.getConfigParameterValue(service, cdos, clang, ClsConfigurationParameters.INSERTION_JOURNAL_PAIE_VALEURS_NULLES);
		insererJournalToutesValeursNulles = StringUtils.equalsIgnoreCase("O", ins);
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#journaliserUnAgent(com.cdi.deltarh.entites.Rhpagent)
	 */
	public void journaliserUnAgent(Salarie salarie)
	{
		ecrireSalarie(cdos, salarie.getNmat(), salarie.getNiv1(), salarie.getNiv2(), salarie.getNiv3(), per1, per2, cas, nbul);
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#cloturerJournalPaie()
	 */
	public String cloturerJournalPaie()
	{
		genereRegroupement(tri1, tri2, tri3, listIdSession.sessionId);
				
		return listIdSession.sessionId;
	}
	
	public void viderMap()
	{
		if(valeursneg != null) valeursneg.clear();
		 valeursneg = null;
		if(valeurspos != null) valeurspos.clear();
		valeurspos = null;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#genererJournalPaieCumule(java.lang.String, java.lang.String, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String)
	 */

	public String genererJournalPaieCumule(String cdos, String cuti, Integer ctab, String clas, String niv1Min, String niv1Max, String niv2Min, String niv2Max, String niv3Min, String niv3Max, String nmatMin,
			String nmatMax, String periode1, String PER2, Integer nbul, Integer cas, String tri1, String tri2, String tri3)
	{
		this.firstPart(cdos, cuti, ctab, clas, niv1Min, niv1Max, niv2Min, niv2Max, niv3Min, niv3Max, nmatMin, nmatMax, periode1, nbul, cas, tri1, tri2, tri3,"");

		for (Salarie salarie : tabsalaries)
		{
			ecrireSalarie(cdos, salarie.getNmat(), salarie.getNiv1(), salarie.getNiv2(), salarie.getNiv3(), periode1, PER2, cas, nbul);
		}

		genereRegroupement(tri1, tri2, tri3, listIdSession.sessionId);

		return listIdSession.sessionId;
	}

	private void firstPart(String cdos, String cuti, Integer ctab, String clas, String niv1Min, String niv1Max, String niv2Min, String niv2Max, String niv3Min, String niv3Max, String nmatMin, String nmatMax,
			String periode1, Integer nbul, Integer cas, String tri1, String tri2, String tri3,String queryExt)
	{
		
		//Rhfnom fnom = (Rhfnom)service.get(Rhfnom.class, new RhfnomPK(cdos, 266,"NBCOLJPAIE",2));
		ParamData fnom = service.findAnyColumnFromNomenclature(cdos, "", "266", "NBCOLJPAIE", "2");
		if(fnom != null && StringUtils.isBlank(fnom.getVall()))
			nbrColonnes = Integer.valueOf(fnom.getVall());
		
		
		initData();
		listIdSession = initLstSessionID(cdos, cuti);
		ParameterUtil.println("La session a �t� bien initialis�e");
		lectureCle(cdos, ctab);
		try
		{
			purgerTablesTemp(listIdSession.sessionSuppMin);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		genereEntete(cdos);
		getListeSalarie(cdos, tri1, tri2, tri3, niv1Min, niv1Max, niv2Min, niv2Max, niv3Min, niv3Max, nmatMin, nmatMax, clas,queryExt);
	}

	// Ecriture de la classe principale pour la g�n�ration du journal de paie
	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#genererJournalPaie(java.lang.String, java.lang.String, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String)
	 */
	public String genererJournalPaie(String cdos, String cuti, Integer ctab, String clas, String niv1Min, String niv1Max, String niv2Min, String niv2Max, String niv3Min, String niv3Max, String nmatMin, String nmatMax,
			String periode1, Integer nbul, Integer cas, String tri1, String tri2, String tri3,String queryExt)
	{
		this.firstPart(cdos, cuti, ctab, clas, niv1Min, niv1Max, niv2Min, niv2Max, niv3Min, niv3Max, nmatMin, nmatMax, periode1, nbul, cas, tri1, tri2, tri3,queryExt);
		for (Salarie salarie : tabsalaries)
		{
			ecrireSalarie(cdos, salarie.getNmat(), salarie.getNiv1(), salarie.getNiv2(), salarie.getNiv3(), periode1, periode1, cas, nbul);
		}
		genereRegroupement(tri1, tri2, tri3, listIdSession.sessionId);
		return listIdSession.sessionId;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#getListIdSession()
	 */
	public LstSession getListIdSession()
	{
		return listIdSession;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#setListIdSession(com.cdi.deltarh.paie.engine.LstSession)
	 */
	public void setListIdSession(LstSession listIdSession)
	{
		this.listIdSession = listIdSession;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#getService()
	 */
	public GeneriqueConnexionService getService()
	{
		return service;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#setService(com.cdi.deltarh.web.nomenclature.Business.ClsService)
	 */
	public void setService(GeneriqueConnexionService service)
	{
		this.service = service;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#getTabParametrage()
	 */
	public List<ParamData> getTabParametrage()
	{
		return tabParametrage;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#setTabParametrage(java.util.List)
	 */
	public void setTabParametrage(List<ParamData> tabParametrage)
	{
		this.tabParametrage = tabParametrage;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#getTabForm()
	 */
	public FormuleLibelle[][] getTabForm()
	{
		return tabForm;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#setTabForm(com.cdi.deltarh.paie.engine.ClsJournalPaieGlobal.FormuleLibelle[][])
	 */
	public void setTabForm(FormuleLibelle[][] tabForm)
	{
		this.tabForm = tabForm;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#getTabsalaries()
	 */
	public List<Salarie> getTabsalaries()
	{
		return tabsalaries;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#setTabsalaries(java.util.List)
	 */
	public void setTabsalaries(List<Salarie> tabsalaries)
	{
		this.tabsalaries = tabsalaries;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#getTotal()
	 */
	public List<BigDecimal> getTotal()
	{
		return total;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#setTotal(java.util.List)
	 */
	public void setTotal(List<BigDecimal> total)
	{
		this.total = total;
	}

	/*
	 * Classe de d�finition des cellules qui comporte le libell� et la formule d'obtention des valeurs
	 */

	public class FormuleLibelle
	{
		/*
		 * identificateur de la formule de calcul de la valeur de la cellule
		 */
		String formule;
		
		String formulepos;
		
		String formuleneg;

		/*
		 * identificateur du libell� de la cellule
		 */
		String libelle;
		
		String colonnealire = "M";
		
		public String getColonne()
		{
			if(StringUtils.equals("M", colonnealire))
				return "mont";
			
			if(StringUtils.equals("B", colonnealire))
				return "basp";
			
			if(StringUtils.equals("T", colonnealire))
				return "taux";
			
			return "mont";
		}
		
		public void generateFormulePosAndNeg()
		{
			int k = 0;
			String w_rub;
			String w_signe;
			if(StringUtils.isBlank(formule)) return;
			if(formule.length() <= 3) return;
			while ((k <= (formule.length() - 1)))
			{
				w_rub = StringUtil.oraSubstring(formule, k + 1, 4);
				k += 4;
				w_signe = "+";
				if(formule.toCharArray().length>k)
					w_signe = String.valueOf(formule.toCharArray()[k]);
				k++;
				if (w_signe.equalsIgnoreCase("+"))
				{
					if(StringUtils.isBlank(formulepos)) formulepos="'"+w_rub+"'";
					else formulepos+=",'"+w_rub+"'";
				}
				else if (w_signe.equalsIgnoreCase("-"))
				{
					if(StringUtils.isBlank(formuleneg)) formuleneg="'"+w_rub+"'";
					else formuleneg+=",'"+w_rub+"'";
				}
			}
		}

		public String getFormule()
		{
			return formule;
		}

		public void setFormule(String formule)
		{
			this.formule = formule;
		}

		public String getLibelle()
		{
			return libelle;
		}

		public void setLibelle(String libelle)
		{
			this.libelle = libelle;
		}
		
		public String getColonnealire()
		{
			return colonnealire;
		}
		public void setColonnealire(String colonnealire)
		{
			this.colonnealire = colonnealire;
		}

		public String getFormulepos()
		{
			return formulepos;
		}

		public void setFormulepos(String formulepos)
		{
			this.formulepos = formulepos;
		}

		public String getFormuleneg()
		{
			return formuleneg;
		}

		public void setFormuleneg(String formuleneg)
		{
			this.formuleneg = formuleneg;
		}
		
		
		
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#getCdos()
	 */
	public String getCdos()
	{
		return cdos;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#setCdos(java.lang.String)
	 */
	public void setCdos(String cdos)
	{
		this.cdos = cdos;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#getCuti()
	 */
	public String getCuti()
	{
		return cuti;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#setCuti(java.lang.String)
	 */
	public void setCuti(String cuti)
	{
		this.cuti = cuti;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#getCtab()
	 */
	public Integer getCtab()
	{
		return ctab;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#setCtab(java.lang.Integer)
	 */
	public void setCtab(Integer ctab)
	{
		this.ctab = ctab;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#getClas()
	 */
	public String getClas()
	{
		return clas;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#setClas(java.lang.String)
	 */
	public void setClas(String clas)
	{
		this.clas = clas;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#getNiv1min()
	 */
	public String getNiv1min()
	{
		return niv1min;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#setNiv1min(java.lang.String)
	 */
	public void setNiv1min(String niv1min)
	{
		this.niv1min = niv1min;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#getNiv1max()
	 */
	public String getNiv1max()
	{
		return niv1max;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#setNiv1max(java.lang.String)
	 */
	public void setNiv1max(String niv1max)
	{
		this.niv1max = niv1max;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#getNiv2min()
	 */
	public String getNiv2min()
	{
		return niv2min;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#setNiv2min(java.lang.String)
	 */
	public void setNiv2min(String niv2min)
	{
		this.niv2min = niv2min;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#getNiv2max()
	 */
	public String getNiv2max()
	{
		return niv2max;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#setNiv2max(java.lang.String)
	 */
	public void setNiv2max(String niv2max)
	{
		this.niv2max = niv2max;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#getNiv3min()
	 */
	public String getNiv3min()
	{
		return niv3min;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#setNiv3min(java.lang.String)
	 */
	public void setNiv3min(String niv3min)
	{
		this.niv3min = niv3min;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#getNiv3max()
	 */
	public String getNiv3max()
	{
		return niv3max;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#setNiv3max(java.lang.String)
	 */
	public void setNiv3max(String niv3max)
	{
		this.niv3max = niv3max;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#getNmatmin()
	 */
	public String getNmatmin()
	{
		return nmatmin;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#setNmatmin(java.lang.String)
	 */
	public void setNmatmin(String nmatmin)
	{
		this.nmatmin = nmatmin;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#getNmatmax()
	 */
	public String getNmatmax()
	{
		return nmatmax;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#setNmatmax(java.lang.String)
	 */
	public void setNmatmax(String nmatmax)
	{
		this.nmatmax = nmatmax;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#getPer1()
	 */
	public String getPer1()
	{
		return per1;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#setPer1(java.lang.String)
	 */
	public void setPer1(String per1)
	{
		this.per1 = per1;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#getPer2()
	 */
	public String getPer2()
	{
		return per2;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#setPer2(java.lang.String)
	 */
	public void setPer2(String per2)
	{
		this.per2 = per2;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#getNbul()
	 */
	public Integer getNbul()
	{
		return nbul;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#setNbul(java.lang.Integer)
	 */
	public void setNbul(Integer nbul)
	{
		this.nbul = nbul;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#getCas()
	 */
	public Integer getCas()
	{
		return cas;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#setCas(java.lang.Integer)
	 */
	public void setCas(Integer cas)
	{
		this.cas = cas;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#getTri1()
	 */
	public String getTri1()
	{
		return tri1;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#setTri1(java.lang.String)
	 */
	public void setTri1(String tri1)
	{
		this.tri1 = tri1;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#getTri2()
	 */
	public String getTri2()
	{
		return tri2;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#setTri2(java.lang.String)
	 */
	public void setTri2(String tri2)
	{
		this.tri2 = tri2;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#getTri3()
	 */
	public String getTri3()
	{
		return tri3;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IJournalPaie#setTri3(java.lang.String)
	 */
	public void setTri3(String tri3)
	{
		this.tri3 = tri3;
	}

	public Map<String, BigDecimal> getValeurspos()
	{
		return valeurspos;
	}

	public void setValeurspos(Map<String, BigDecimal> valeurspos)
	{
		this.valeurspos = valeurspos;
	}

	public Map<String, BigDecimal> getValeursneg()
	{
		return valeursneg;
	}

	public void setValeursneg(Map<String, BigDecimal> valeursneg)
	{
		this.valeursneg = valeursneg;
	}

	
}
