package com.kinart.paie.business.services.calcul;

import java.math.BigDecimal;
import java.util.*;

import com.kinart.paie.business.model.ElementVariableConge;
import com.kinart.paie.business.model.ParamData;
import com.kinart.paie.business.repository.ParamDataRepository;
import com.kinart.paie.business.services.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

/**
 * Cette classe est un utilitaire pour les tables de nommenclature et des tables de donn�es �v�nementielles.
 * 
 * @author c.mbassi
 * 
 */
public class ClsNomenclatureUtil
{

	HibernateConnexionService service = null;

	ParamDataRepository paramDataRepository = null;

	ClsParameterOfPay parameter = null;

	public HibernateConnexionService getService()
	{
		return service;
	}

	public void setService(HibernateConnexionService service)
	{
		this.service = service;
	}

	public ParamDataRepository getParamDataRepository() {
		return paramDataRepository;
	}

	public void setParamDataRepository(ParamDataRepository paramDataRepository) {
		this.paramDataRepository = paramDataRepository;
	}

	private ClsNomenclatureUtil()
	{

	}

	public ClsNomenclatureUtil(ClsParameterOfPay parameter)
	{
		this.setService(parameter.getService());
		this.parameter = parameter;
	}
	
	private double getNomberOfDaysBetweenOracle(Date from, Date to)
	{
		if (from == null || to == null)
			return 0;
		double nbrj = 0;

		Session session = service.getSession();

		try
		{
			String query = "Select :fin - :deb from dual";
			SQLQuery q1 = session.createSQLQuery(query);
			q1.setParameter("fin", to);
			q1.setParameter("deb", from);
			List lst1 = q1.list();

			if (!lst1.isEmpty())
				nbrj = Double.valueOf(lst1.get(0).toString());
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			service.closeConnexion(session);
		}
		return nbrj;
	}

	private double getMonthsBetweenOracle(Date from, Date to)
	{
		if (from == null || to == null)
			return 0;

		double nbrj = 0;

		Session session = service.getSession();

		try
		{
			String query = "Select MONTHS_BETWEEN(:deb ,:fin) from dual";
			SQLQuery q1 = session.createSQLQuery(query);
			q1.setParameter("fin", to);
			q1.setParameter("deb", from);
			List lst1 = q1.list();

			if (!lst1.isEmpty())
				nbrj = Double.valueOf(lst1.get(0).toString());
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			service.closeConnexion(session);
		}
		return nbrj;
	}
	
	public double getMonthsBetween(Date from, Date to)
	{
		if(TypeBDUtil.OR.equalsIgnoreCase(TypeBDUtil.typeBD))
		{
			return this.getMonthsBetweenOracle(from, to);
		}
		else
		 return ClsDate.getMonthsBetweenIncludingDays(from, to);
	}
	
	public double getNomberOfDaysBetween(Date from, Date to)
	{
		if(TypeBDUtil.OR.equalsIgnoreCase(TypeBDUtil.typeBD))
		{
			return this.getNomberOfDaysBetweenOracle(from, to);
		}
		else
		 return ClsDate.getNumberOfDay(from, to);
	}


	/**
	 * => paf_EvenSallib
	 * <p>
	 * Lecture des donnees evenements salariales Si l'enregistrement n'existe pas ou si probleme sur le select alors la fonction renvoie FALSE.
	 * </p>
	 * 
	 * @param cdos
	 * @param nlot
	 * @param colname
	 * @param nmat
	 * @return le libell�
	 */
	public String getLabelOfPreviousSalary(String cdos, long nlot, String colname, String nmat)
	{
		String result = "";
		List l = service.find("select vall from Rhtprevsal " + " where comp_id.cdos ='" + cdos + "'" + " and comp_id.colname = '" + colname + "'" + " and comp_id.nmat = '" + nmat + "'"
				+ " and comp_id.nlot = " + nlot + " and tval = '1'");
		if (l != null && l.size() > 0 && l.get(0) != null)
		{
			result = (String) l.get(0);
		}
		//
		return result;
	}

	/**
	 * => paf_EvenSalmnt
	 * <p>
	 * Lire le montant ou le taux du salaire pass�
	 * </p>
	 * 
	 * @param cdos
	 * @param nlot
	 * @param colname
	 * @param nmat
	 * @param columnToRead
	 * @return le montant ou le taux
	 */
	public Number getAmountOrRateOfPreviousSalary(String cdos, long nlot, String colname, String nmat, ClsEnumeration.EnColumnToRead columnToRead)
	{
		Number result = null;
		// pr�ciser le nom de la colonne
		String colName = (columnToRead == ClsEnumeration.EnColumnToRead.AMOUNT) ? "valm" : "valt";
		List l = service.find("select " + colName + " from Rhtprevsal " + " where comp_id.cdos ='" + cdos + "'" + " and comp_id.colname = '" + colname + "'" + " and comp_id.nmat = '" + nmat + "'"
				+ " and comp_id.nlot = " + nlot + " and tval = '1'");
		if (!ClsObjectUtil.isListEmty(l) && l.get(0) != null)
		{
			result = (Double) l.get(0);
		}
		//
		return result;
	}

	/**
	 * => paf_EvenSaldat
	 * <p>
	 * lire la date du dernier salaire
	 * </p>
	 * 
	 * @param cdos
	 * @param nlot
	 * @param colname
	 * @param nmat
	 * @return la date
	 */
	public Date getDateOfPreviousSalary(String cdos, long nlot, String colname, String nmat)
	{
		Date result = null;
		List l = service.find("select vald from Rhtprevsal " + " where comp_id.cdos ='" + cdos + "'" + " and comp_id.colname = '" + colname + "'" + " and comp_id.nmat = '" + nmat + "'"
				+ " and comp_id.nlot = " + nlot + " and tval = '2'");
		if (!ClsObjectUtil.isListEmty(l) && l.get(0) != null)
		{
			result = (Date) l.get(0);
		}
		//
		return result;
	}

	/**
	 * => paf_LecCum
	 * <p>
	 * Lecture du montant (somme) d'une rubrique de cumul. Si l'enregistrement n'existe pas ou si probleme sur le select alors la fonction renvoie 0 dans le montant. Si la periode de traitement est
	 * differente du mois de paie courant alors le traitement est de type retroactif donc lecture sur PRCUMUL.
	 * </p>
	 * 
	 * @param nomcol
	 * @param aamm
	 * @param rubq
	 * @param nbul
	  * @return le montant
	 */
	public double calculateMontant(ClsSalaryToProcess salary, String nomcol, String aamm, String rubq, int nbul, String monthOfPay_in_db, String periodTraitement_on_screen, boolean retro)
	{
		int idx = 0;
		if("basp".equals(nomcol)){
			idx = 3;
		}
		if("taux".equals(nomcol)){
			idx = 2;
		}
		if("mont".equals(nomcol)){
			idx = 1;
		}
		
		double result = 0;
		if(nbul == 0){
			if((periodTraitement_on_screen == "" || periodTraitement_on_screen == null)
					|| (periodTraitement_on_screen.equals(monthOfPay_in_db))){	
				if(! salary.getCumul1().isEmpty() && salary.getCumul1().containsKey(rubq+aamm)){
					result = ((BigDecimal)((Object[])salary.getCumul1().get(rubq+aamm))[idx]).doubleValue();
				}
			}
			else{
				if(! periodTraitement_on_screen.equals(aamm)){
					if(! salary.getCumul2().isEmpty() && salary.getCumul2().containsKey(rubq+aamm)){
						result = ((BigDecimal)((Object[])salary.getCumul2().get(rubq+aamm))[idx]).doubleValue();
					}
				}
				else
					result = 0 ;
			}
		}
		else{
//			if((periodTraitement_on_screen == "" || periodTraitement_on_screen == null)
//					|| (periodTraitement_on_screen.equals(monthOfPay_in_db))){	
			if(retro == false){
				if(! salary.getCumul3().isEmpty() && salary.getCumul3().containsKey(rubq+aamm)){
					
					result = ((BigDecimal)((Object[])salary.getCumul3().get(rubq+aamm))[idx]).doubleValue();
				}
			}
			else{
				if(! periodTraitement_on_screen.equals(aamm)){
					
					if(! salary.getCumul4().isEmpty() && salary.getCumul4().containsKey(rubq+aamm)){
						result = ((BigDecimal)((Object[])salary.getCumul4().get(rubq+aamm))[idx]).doubleValue();
					}
				}
				else
					result = 0 ;
			}
		}
		//
		return result;
	}
	/*
	public double calculateMontantFromType(ClsSalaryToProcess salary, String nomcol, String aamm, String rubq, int nbul, String moisPaieCourant, String periodeTraitement)
	{
		return calculerUnCumul(salary.getInfoSalary().getComp_id().getCdos(), salary.getInfoSalary().getComp_id().getNmat(), nomcol, aamm, rubq, nbul, moisPaieCourant, periodeTraitement);
	}
	
	private double calculerUnCumul(String cdos,String nmat,String nomcol, String aamm, String rubq, int nbul, String moisPaieCourant, String periodeTraitement)
	{


		 String strSqlQuery1 = "select sum(" + nomcol + ") from Rhtcumul "
		 + " where comp_id.cdos = '" + cdos +"'"
		 + " and comp_id.nmat = '" + nmat + "'"
		 + " and comp_id.aamm = '" + aamm + "'"
		 + " and comp_id.rubq = '" + rubq + "'"
		 + " and comp_id.nbul != 0";
			
		 String strSqlQuery2 = "select sum(" + nomcol + ") from Rhtprcumu "
		 + " where comp_id.cdos = '" + cdos +"'"
		 + " and comp_id.nmat = '" + nmat + "'"
		 + " and comp_id.aamm = '" + aamm + "'"
		 + " and comp_id.rubq = '" + rubq + "'"
		 + " and comp_id.nbul != 0";
			
		 String strSqlQuery3 = "select sum(" + nomcol + ") from Rhtcumul "
		 + " where comp_id.cdos = '" + cdos +"'"
		 + " and comp_id.nmat = '" + nmat + "'"
		 + " and comp_id.aamm = '" + aamm + "'"
		 + " and comp_id.rubq = '" + rubq + "'"
		 + " and comp_id.nbul = " + nbul;
				
		 String strSqlQuery4 = "select sum(" + nomcol + ") from Rhtprcumu "
		 + " where comp_id.cdos = '" + cdos +"'"
		 + " and comp_id.nmat = '" + nmat + "'"
		 + " and comp_id.aamm = '" + aamm + "'"
		 + " and comp_id.rubq = '" + rubq + "'"
		 + " and comp_id.nbul = " + nbul;
		List ListOfRow = null;
		
		double result = 0;
		
		Object o = null;
		
		
		
		if (nbul == 0)
		{
			if (StringUtils.isBlank(periodeTraitement) || (periodeTraitement.equals(moisPaieCourant)))
				ListOfRow = service.find(strSqlQuery1);
			else
			{
				if (!periodeTraitement.equals(aamm))
				{
					ListOfRow = service.find(strSqlQuery2);
				}
			}
		}
		else
		{
			if (StringUtils.isBlank(periodeTraitement)  || (periodeTraitement.equals(moisPaieCourant)))
			{
				ListOfRow = service.find(strSqlQuery3);	
			}
			else
			{
				if (!periodeTraitement.equals(aamm))
				{
					ListOfRow = service.find(strSqlQuery4);
				}
			}
		}
		
		if(ListOfRow != null && ListOfRow.get(0) != null)
			result = ((BigDecimal)ListOfRow.get(0)).doubleValue();
		else result = 0;
		
		return result;
	}
*/
	/**
	 * => paf_LecCum
	 * <p>
	 * calcul le cumul mont, taux, base
	 * </p>
	 * 
	 * @param aamm
	 * @param rubq
	 * @param nbul
	 * @param type
	 * @param moisPaieCourant
	 * @param periodeTraitement
	 * @return le montant du cumul
	 */
	public double getCumul(ClsSalaryToProcess salary, String aamm, String rubq, int nbul, ClsEnumeration.EnTypeOfColumn type, String moisPaieCourant, String periodeTraitement)
	{
		double cumul = 0;
		String colName = "";
		if (type.equals(ClsEnumeration.EnTypeOfColumn.UNKNOWN))
		{
			return cumul;
		}
		if (type.equals(ClsEnumeration.EnTypeOfColumn.BASE))
		{
			colName = "basp";
		}
		if (type.equals(ClsEnumeration.EnTypeOfColumn.RATES))
		{
			colName = "taux";
		}
		if (type.equals(ClsEnumeration.EnTypeOfColumn.AMOUNT))
		{
			colName = "mont";
		}
		//
		//cumul = calculateMontantFromType(salary, colName, aamm, rubq, nbul, moisPaieCourant, periodeTraitement);
		cumul = calculateMontant(salary, colName, aamm, rubq, nbul, moisPaieCourant, periodeTraitement, false);
		return cumul;
	}

	/**
	 * => paf_LecElfix
	 * <p>
	 * lire le montant ou le taux d'un �l�ment fixe Lecture du montant d'un element fixe Si l'enregistrement n'existe pas ou si probleme sur le select alors la fonction renvoie 0 dans le montant.
	 * </p>
	 * 
	 * @param cdos
	 * @param moisPaieCourant
	 * @param nmat
	 * @param rubq
	 * @param nbul
	 * @return le montant
	 */
	public Number getAmountOrRateOfFixElement(String cdos, String moisPaieCourant, String nmat, String periodeTraitement, String rubq, int nbul)
	{
		Number result = null;
		List l = null;
		Object[] row = null;
		//
		if (StringUtils.isBlank(periodeTraitement) || periodeTraitement.equals(moisPaieCourant))
		{
			l = service.find("select monp, ddeb, dfin from Rhteltfixagent " + " where comp_id.cdos = '" + cdos + "'" + " and comp_id.nmat = '" + nmat + "'" + " and comp_id.codp = '" + rubq + "'");
			if (l != null && !l.isEmpty())
			{
				row = (Object[]) l.get(0);
				if (row[1] == null)
				{
					if (row[2] == null)
					{
						result = ((BigDecimal) row[0]).doubleValue();
					}
					else
					{
						ClsDate dfin = new ClsDate(row[2]);
						if (dfin.getYearAndMonth().compareTo(periodeTraitement) >= 0)
							result = ((BigDecimal) row[0]).doubleValue();
						else
							result = 0;
					}
				}
				else
				{
					ClsDate ddeb = new ClsDate(row[1]);
					if (ddeb.getYearAndMonth().compareTo(periodeTraitement) <= 0)
					{
						if (row[2] == null)
						{
							result = ((BigDecimal) row[0]).doubleValue();
						}
						else
						{
							ClsDate dfin = new ClsDate(row[2]);
							if (dfin.getYearAndMonth().compareTo(periodeTraitement) >= 0)
								result = ((BigDecimal) row[0]).doubleValue();
							else
								result = 0;
						}
					}
				}
			}
			else
				result = 0;
		}
		else
		{
			l = service.find("select monp from Rhthelfix " + " where comp_id.cdos = '" + cdos + "'" + " and comp_id.nmat = '" + nmat + "'" + " and comp_id.aamm = '" + periodeTraitement + "'"
					+ " and comp_id.nbul = " + nbul + " and comp_id.codp = '" + rubq + "'");
			if (!ClsObjectUtil.isListEmty(l) && l.get(0) != null)
			{
				result = ((BigDecimal) l.get(0)).doubleValue();
			}
			else
				result = 0;
		}
		//
		return result;
	}

	/**
	 * => paf_LecEvar
	 * <p>
	 * Lecture du montant (somme) d'un element variable Si l'enregistrement n'existe pas ou si probleme sur le select alors la fonction renvoie 0 dans le montant.
	 * 
	 * @param cdos
	 * @param nmat
	 * @param moisPaieCourant
	 * @param periodeTraitement
	 * @param rubq
	 * @param nbul
	 * @return la somme des montants obtenue
	 */
	public Number getSumOfAmountOfVariableElement(String cdos, String nmat, String moisPaieCourant, String periodeTraitement, String rubq, int nbul)
	{
		Number result = null;
		List l = null;
		//
		if (StringUtils.isBlank(periodeTraitement) || periodeTraitement.equals(moisPaieCourant))
		{
			l = service.find("select sum(mont) from Rhteltvardet " + " where cdos = '" + cdos + "'" + " and nmat = '" + nmat + "'" + " and aamm = '" + moisPaieCourant + "'" + " and nbul = " + nbul
					+ " and rubq = '" + rubq + "'");
			if (!ClsObjectUtil.isListEmty(l) && l.get(0) != null)
			{
				result = ((BigDecimal) l.get(0)).doubleValue();
			}
		}
		else
		{
			l = service.find("select sum(mont) from Rhthevar " + " where cdos = '" + cdos + "'" + " and nmat = '" + nmat + "'" + " and aamm = '" + periodeTraitement + "'" + " and nbul = " + nbul
					+ " and rubq = '" + rubq + "'");
			if (!ClsObjectUtil.isListEmty(l) && l.get(0) != null)
			{
				result = ((BigDecimal) l.get(0)).doubleValue();
			}
		}
		return result;
	}

	/**
	 * => paf_NomEvenL
	 * <p>
	 * Lecture des donnees evenements nomenclature si traitement normal alors pafnom si traitement retro et si donnee evenement alors lecture table des evenements. si uniquement traitement retro
	 * lecture sur historique nomenclature. Recuperation d'un libelle. Si l'enregistrement n'existe pas ou si probleme sur le select alors la fonction renvoie NULL.
	 * </p>
	 * 
	 * @param cdos
	 * @param nlot
	 * @param ntable
	 * @param cleAcces
	 * @param numLigne
	 * @return le libell�
	 */
	public String getLabelFromEventData(String cdos, long nlot, int ntable, String cleAcces, int numLigne)
	{
		String result = "";
		List l = service.find("select vall from Rhtprevnmlig " + " where comp_id.cdos = '" + cdos + "'" + " and comp_id.nlot = " + nlot + " and comp_id.ntab = " + ntable + " and comp_id.cacc = '"
				+ cleAcces + "'" + " and comp_id.nume = " + numLigne + " and comp_id.lmt = 'L'");
		if (!ClsObjectUtil.isListEmty(l) && !ClsObjectUtil.isNull(l.get(0)))
		{
			result = (String) l.get(0);
		}
		//
		return result;
	}

	/**
	 * => paf_NomEvenM & paf_NomEvenT
	 * <p>
	 * Lecture des donnees evenements nomenclature. Recuperation d'un montant. Si l'enregistrement n'existe pas ou si probleme sur le select alors la fonction renvoie NULL.
	 * </p>
	 * 
	 * @param cdos
	 * @param nlot
	 * @param ntable
	 * @param cleAcces
	 * @param numLigne
	 * @param columnToRead
	 * @return le montant ou le taux
	 */
	public Number getAmountOrRateFromEventData(String cdos, long nlot, int ntable, String cleAcces, long numLigne, ClsEnumeration.EnColumnToRead columnToRead)
	{
		Number result = null;
		// pr�ciser le nom de la colonne
		String colName = (columnToRead == ClsEnumeration.EnColumnToRead.AMOUNT) ? "valm" : "valt";
		String lmt = (columnToRead == ClsEnumeration.EnColumnToRead.AMOUNT) ? "M" : "L";
		List l = service.find("select " + colName + " from Rhtprevnmlig " + " where comp_id.cdos = '" + cdos + "'" + " and comp_id.nlot = " + nlot + " and comp_id.ntab = " + ntable
				+ " and comp_id.cacc = '" + cleAcces + "'" + " and comp_id.lmt = '" + lmt + "'" + " and comp_id.nume = " + numLigne);
		if (!ClsObjectUtil.isListEmty(l) && l.get(0) != null)
		{
			result = (Double) l.get(0);
		}
		//
		return result;
	}

	/**
	 * => paf_EvenSalmnt Lecture des donnees evenements salariales Si l'enregistrement n'existe pas ou si probleme sur le select alors la fonction renvoie FALSE.
	 * 
	 * @param cdos
	 * @param nmat
	 * @param nlot
	 * @param columnToRead
	 * @return le montant ou le taux
	 */
	public Number getAmountOrRateFromSalaryEventData(String cdos, String nmat, long nlot, ClsEnumeration.EnColumnToRead columnToRead)
	{
		Number result = null;
		List l = null;
		// pr�ciser le nom de la colonne
		String colName = (columnToRead == ClsEnumeration.EnColumnToRead.AMOUNT) ? "valm" : "valt";
		l = service.find("select " + colName + " from Rhtprevsal " + " where comp_id.cdos = '" + cdos + "'" + " and comp_id.nlot = " + nlot + " and comp_id.colname = '" + colName + "'"
				+ " and comp_id.nmat = '" + nmat + "'" + " and tval = '0'");
		if (!ClsObjectUtil.isListEmty(l) && l.get(0) != null)
		{
			result = (Double) l.get(0);
		}
		//
		return result;
	}

	/**
	 * => paf_EvenSalmnt Lecture des donnees evenements salariales Si l'enregistrement n'existe pas ou si probleme sur le select alors la fonction renvoie FALSE.
	 * 
	 * @param cdos
	 * @param nmat
	 * @param nlot
	 * @param colName
	 * @return le montant ou le taux
	 */
	public Number getAmountOrRateFromSalaryEventData(String cdos, String nmat, long nlot, String colName)
	{
		Number result = null;
		List l = service.find("select valm from Rhtprevsal " + " where comp_id.cdos = '" + cdos + "'" + " and comp_id.nlot = " + nlot + " and comp_id.colname = '" + colName + "'"
				+ " and comp_id.nmat = '" + nmat + "'" + " and tval = '0'");
		if (!ClsObjectUtil.isListEmty(l) && l.get(0) != null)
		{
			result = (Double) l.get(0);
		}
		//
		return result;
	}

	/**
	 * => paf_lecfnomM & paf_lecfnomT Lecture d'une Table de nomenclature dans pafnom. Recuperation d'un montant. Si l'enregistrement n'existe pas ou si probleme sur le select alors la fonction
	 * renvoie NULL.
	 * 
	 * @param cdos
	 * @param ntable
	 * @param cleAcces
	 * @param numLigne
	 * @param nlot
	 * @param nbul
	 * @param moisPaieCourant
	 * @param periodeTraitement
	 * @param columnName
	 * @return le montant ou le taux
	 */
	 
	public Number getAmountOrRateFromNomenclature(Map<String, ParamData> listOfTableXXMap, String cdos, int ntable, String cleAcces, long numLigne, long nlot, int nbul, String moisPaieCourant, String periodeTraitement,
												  ClsEnumeration.EnColumnToRead columnName)
	{
		// com.cdi.deltarh.service.ClsParameter.println(">>getAmountOrRateFromNomenclature");
		Number result = null;
		List l = null;
		//
		String colName = (ClsEnumeration.EnColumnToRead.AMOUNT == columnName) ? "valm" : "valt";
		String clemap = ntable+"-"+cleAcces+"-"+numLigne;
		ParamData fnom = null;
		if (StringUtils.isBlank(periodeTraitement) || periodeTraitement.equals(moisPaieCourant))
		{
			if(! listOfTableXXMap.containsKey(clemap))
			{
				l = service.find("From ParamData  where comp_id.cdos = '" + cdos + "'" + " and comp_id.ctab = " + ntable + " and comp_id.cacc = '" + cleAcces + "'  and comp_id.nume = " + numLigne);			
				if(!l.isEmpty())
					fnom =(ParamData) l.get(0);
				listOfTableXXMap.put(clemap, fnom);
			}
			fnom = listOfTableXXMap.get(clemap);
			if(fnom != null)
			{
				result = (ClsEnumeration.EnColumnToRead.AMOUNT == columnName) ? (fnom.getValm() != null ? fnom.getValm() : 0) : (fnom.getValt() != null ? fnom.getValt() : 0 ).doubleValue();
			}
//			l = service.find("select " + colName + " from ParamData " + " where comp_id.cdos = '" + cdos + "'" + " and comp_id.ctab = " + ntable + " and comp_id.cacc = '" + cleAcces + "'"
//					+ " and comp_id.nume = " + numLigne);
//			if (!ClsObjectUtil.isListEmty(l) && l.get(0) != null)
//			{
//				result = (l.get(0) instanceof BigDecimal) ? ((BigDecimal) l.get(0)).doubleValue() : (Long) l.get(0);
//			}
		}
		else
		{
			result = this.getAmountOrRateFromEventData(cdos, nlot, ntable, cleAcces, numLigne, columnName);
			if (result == null)
			{
				l = service.find("select " + colName + " from Rhthfnom " + " where comp_id.cdos = '" + cdos + "'" + " and comp_id.ctab = " + ntable + " and comp_id.cacc = '" + cleAcces + "'"
						+ " and comp_id.nume = " + numLigne + " and comp_id.nbul = " + nbul + " and comp_id.aamm = '" + periodeTraitement + "'");
				if (!ClsObjectUtil.isListEmty(l) && l.get(0) != null)
				{
					result = (l.get(0) instanceof BigDecimal) ? ((BigDecimal) l.get(0)).doubleValue() : (Long) l.get(0);
				}
			}
		}
		return result;
	}

	/**
	 * cas sp�cifique de la table T99
	 * 
	* @param cdos
	 * @param ntable
	 * @param cleAcces
	 * @param numLigne
	 * @param nlot
	 * @param moisPaieCourant
	 * @param periodeTraitement
	 * @param columnName
	 * @return
	 */
	public Number getAmountOrRateFromT99(Map<String, Object[]> t99, String cdos, int ntable, String cleAcces, long numLigne, long nlot, String moisPaieCourant, String periodeTraitement,
			ClsEnumeration.EnColumnToRead columnName)
	{
		Number result = null;
		Object[] arrond = null;
		String key = cleAcces + numLigne;
		//
		if (StringUtils.isBlank(periodeTraitement) || periodeTraitement.equals(moisPaieCourant))
		{
			if (!t99.isEmpty() && t99.containsKey(key.trim()))
			{
				arrond = (Object[]) t99.get(key);
				if (ClsEnumeration.EnColumnToRead.AMOUNT == columnName)
				{
					if (arrond[2] != null)
						result = (arrond[2] instanceof BigDecimal) ? ((BigDecimal) arrond[2]).doubleValue() : (Long) arrond[2];
				}
				else if (ClsEnumeration.EnColumnToRead.RATES == columnName)
				{
					if (arrond[3] != null)
						result = (arrond[3] instanceof BigDecimal) ? ((BigDecimal) arrond[3]).doubleValue() : (Long) arrond[3];
				}
			}
		}
		else
		{
			result = this.getAmountOrRateFromEventData(cdos, nlot, ntable, cleAcces, numLigne, columnName);
			if (result ==null)
			{
				if (!t99.isEmpty() && t99.containsKey(key))
				{
					arrond = (Object[]) t99.get(key);
					if (!t99.isEmpty() && t99.containsKey(key))
					{
						arrond = (Object[]) t99.get(key);
						if (ClsEnumeration.EnColumnToRead.AMOUNT == columnName)
						{
							if (arrond[2] != null)
								result = (arrond[2] instanceof BigDecimal) ? ((BigDecimal) arrond[2]).doubleValue() : (Long) arrond[2];
						}
						else if (ClsEnumeration.EnColumnToRead.RATES == columnName)
						{
							if (arrond[3] != null)
								result = (arrond[3] instanceof BigDecimal) ? ((BigDecimal) arrond[3]).doubleValue() : (Long) arrond[3];
						}
					}
				}
			}
		}
		//
		return result;
	}

	/**
	 * sp�cifique pour le taux des arrondis
	 * 
	 * @param arr
	 *            map contenant les arrondis
	 * @param cdos
	 * @param ntable
	 * @param cleAcces
	 * @param numLigne
	 * @param nlot
	 * @param moisPaieCourant
	 * @param periodeTraitement
	 * @param columnName
	 * @return
	 */
	public Number getTauxArrondi(Map<String, Object[]> arr, String cdos, int ntable, String cleAcces, long numLigne, long nlot, String moisPaieCourant, String periodeTraitement,
			ClsEnumeration.EnColumnToRead columnName)
	{
		// com.cdi.deltarh.service.ClsParameter.println(">>getTauxArrondi");
		Number result = null;
		Object[] arrond = null;
		if (StringUtils.isBlank(periodeTraitement) || periodeTraitement.equals(moisPaieCourant))
		{
			if (!arr.isEmpty() && arr.containsKey(cleAcces))
			{
				arrond = (Object[]) arr.get(cleAcces);
				result = arrond[1] instanceof BigDecimal ? ((BigDecimal) arrond[1]).doubleValue() : (Double) arrond[1];
			}
		}
		else
		{
			result = this.getAmountOrRateFromEventData(cdos, nlot, ntable, cleAcces, numLigne, columnName);
			if (result == null)
			{
				if (!arr.isEmpty() && arr.containsKey(cleAcces))
				{
					arrond = (Object[]) arr.get(cleAcces);
					result = arrond[1] instanceof BigDecimal ? ((BigDecimal) arrond[1]).doubleValue() : (Double) arrond[1];
				}
			}
		}
		return result;
	}

	/**
	 * => paf_lecfnomM & paf_lecfnomT Lecture d'une Table de nomenclature dans pafnom. Recuperation d'un montant. Si l'enregistrement n'existe pas ou si probleme sur le select alors la fonction
	 * renvoie NULL.
	 * 
	 * @param cdos
	 * @param ntable
	 * @param cleAcces
	 * @param numLigne
	 * @param nlot
	 * @param nbul
	 * @param moisPaieCourant
	 * @param periodeTraitement
	 * @param columnName
	 * @return le montant ou le taux
	 */
	public Number getAmountOrRateFromNomenclatureRates(Map<String, ParamData> listOfTableXXMap,String cdos, int ntable, String cleAcces, long numLigne, long nlot, int nbul, String moisPaieCourant, String periodeTraitement,
			ClsEnumeration.EnColumnToRead columnName)
	{

		Number result = null;
		List l = null;
		//
		String colName = (ClsEnumeration.EnColumnToRead.AMOUNT == columnName) ? "valm" : "valt";
		
		String clemap = ntable+"-"+cleAcces+"-"+numLigne;
		ParamData fnom = null;
				
		if (StringUtils.isBlank(periodeTraitement)  || periodeTraitement.equals(moisPaieCourant))
		{
			if(! listOfTableXXMap.containsKey(clemap))
			{
				l = service.find("From ParamData  where comp_id.cdos = '" + cdos + "'" + " and comp_id.ctab = " + ntable + " and comp_id.cacc = '" + cleAcces + "'  and comp_id.nume = " + numLigne);			
				if(!l.isEmpty())
					fnom =(ParamData) l.get(0);
				listOfTableXXMap.put(clemap, fnom);
			}
			fnom = listOfTableXXMap.get(clemap);
			if(fnom != null)
				result = (ClsEnumeration.EnColumnToRead.AMOUNT == columnName) ? fnom.getValm() : fnom.getValt();		
				
//			l = service.find("select " + colName + " from ParamData " + " where comp_id.cdos = '" + cdos + "'" + " and comp_id.ctab = " + ntable + " and comp_id.cacc = '" + cleAcces + "'"
//					+ " and comp_id.nume = " + numLigne);
//			if (!ClsObjectUtil.isListEmty(l) && l.get(0) != null)
//			{
//				result = (l.get(0) instanceof BigDecimal) ? ((BigDecimal) l.get(0)).doubleValue() : (Long) l.get(0);
//			}
		}
		else
		{
			result = this.getAmountOrRateFromEventData(cdos, nlot, ntable, cleAcces, numLigne, columnName);
			if (result == null)
			{
				l = service.find("select " + colName + " from Rhthfnom " + " where comp_id.cdos = '" + cdos + "'" + " and comp_id.ctab = " + ntable + " and comp_id.cacc = '" + cleAcces + "'"
						+ " and comp_id.nume = " + numLigne + " and comp_id.nbul = " + nbul + " and comp_id.aamm = '" + periodeTraitement + "'");
				if (!ClsObjectUtil.isListEmty(l) && l.get(0) != null)
				{
					result = (l.get(0) instanceof BigDecimal) ? ((BigDecimal) l.get(0)).doubleValue() : (Long) l.get(0);
				}
			}
		}
		return result;
	}

	/**
	 * => paf_lecfnomL
	 * <p>
	 * Lecture d'une Table de nomenclature.
	 * <p>
	 * si traitement normal alors pafnom
	 * <p>
	 * si traitement retro et si donnee evenement alors lecture table des evenements.
	 * <p>
	 * si uniquement traitement retro lecture sur historique nomenclature.
	 * <p>
	 * Recuperation d'un libelle.
	 * <p>
	 * Si l'enregistrement n'existe pas ou si probleme sur le select
	 * <p>
	 * alors la fonction renvoie NULL.
	 * 
	 * @param cdos
	 * @param ntable
	 * @param cleAcces
	 * @param numLigne
	 * @param nlot
	 * @param nbul
	 * @param moisPaieCourant
	 * @param periodeTraitement
	 * @return le libell�
	 */
	public String getLabelFromNomenclature(String cdos, int ntable, String cleAcces, int numLigne, long nlot, int nbul, String moisPaieCourant, String periodeTraitement)
	{
		String result = "";
		List l = null;
		//
		if (StringUtils.isBlank(periodeTraitement) || periodeTraitement.equals(moisPaieCourant))
		{
			l = service.find("select vall from ParamData " + " where comp_id.cdos = '" + cdos + "'" + " and comp_id.ctab = " + ntable + " and comp_id.cacc = '" + cleAcces + "'" + " and comp_id.nume = "
					+ numLigne);
			if (!ClsObjectUtil.isListEmty(l) && !ClsObjectUtil.isNull(l.get(0)))
			{
				result = (String) l.get(0);
			}
		}
		else
		{
			result = this.getLabelFromEventData(cdos, nlot, ntable, cleAcces, numLigne);
			if (result == "")
			{
				l = service.find("select vall from Rhthfnom " + " where comp_id.cdos = '" + cdos + "'" + " and comp_id.ctab = " + ntable + " and comp_id.cacc = '" + cleAcces + "'"
						+ " and comp_id.nume = " + numLigne + " and comp_id.nbul = " + nbul + " and comp_id.aamm = '" + periodeTraitement + "'");
				if (!ClsObjectUtil.isListEmty(l) && !ClsObjectUtil.isNull(l.get(0)))
				{
					result = (String) l.get(0);
				}
			}
		}
		return result;
	}

	/**
	 * cas sp�cifique de la table T99
	 * 
	 * @param t99
	 * @param cdos
	 * @param ntable
	 * @param cleAcces
	 * @param numLigne
	 * @param nlot
	 * @param moisPaieCourant
	 * @param periodeTraitement
	 * @return
	 */
	public String getLabelFromT99(Map<String, Object[]> t99, String cdos, int ntable, String cleAcces, int numLigne, long nlot, String moisPaieCourant, String periodeTraitement)
	{
		String result = "";
		Object[] arrond = null;
		String key = cleAcces + numLigne;
		//
		if (StringUtils.isBlank(periodeTraitement)  || periodeTraitement.equals(moisPaieCourant))
		{
			if (!t99.isEmpty() && t99.containsKey(key))
			{
				arrond = (Object[]) t99.get(key);
				result = (String) arrond[1];
			}
		}
		else
		{
			result = this.getLabelFromEventData(cdos, nlot, ntable, cleAcces, numLigne);
			if (result == "")
			{
				if (!t99.isEmpty() && t99.containsKey(key))
				{
					arrond = (Object[]) t99.get(key);
					result = (String) arrond[1];
				}
			}
		}
		return result;
	}

	/**
	 * sp�cifique pour les arrondis
	 * 
	 * @param arr
	 *            contient le map des arrondi
	 * @param cdos
	 * @param ntable
	 * @param cleAcces
	 * @param numLigne
	 * @param nlot
	 * @param moisPaieCourant
	 * @param periodeTraitement
	 * @return
	 */
	public String getLabelArrondi(Map<String, Object[]> arr, String cdos, int ntable, String cleAcces, int numLigne, long nlot, String moisPaieCourant, String periodeTraitement)
	{
		String result = "";
		Object[] arrond = null;
		//
		if (StringUtils.isBlank(periodeTraitement) || periodeTraitement.equals(moisPaieCourant))
		{
			if (!arr.isEmpty() && arr.containsKey(cleAcces))
			{
				arrond = (Object[]) arr.get(cleAcces);
				result = (String) arrond[0];
			}
		}
		else
		{
			result = this.getLabelFromEventData(cdos, nlot, ntable, cleAcces, numLigne);
			if (result == "")
			{
				if (!arr.isEmpty() && arr.containsKey(cleAcces))
				{
					arrond = (Object[]) arr.get(cleAcces);
					result = (String) arrond[0];
				}
			}
		}
		return result;
	}

	/**
	 * => paf_TypePaiement
	 * <p>
	 * Lecture du mode de paiement de l'agent dans pasa01.modp Lecture de la Table 54 des nomenclature dans pafnom. Recuperation du Libelle 4. Si l'enregistrement n'existe pas ou si probleme sur le
	 * select alors la fonction renvoie NULL.
	 * </p>
	 * 
	 * @param cdos
	 * @param moisPaieCourant
	 * @param matricule
	 * @return la chaine contenant le type de paiement
	 */
	public String getTypePaiementOfSalary(String cdos, String moisPaieCourant, String matricule)
	{
		String modePaiement = "";
		List l = service.find("select modp from Rhpagent " + " where cdos = '" + cdos + "'" + " and comp_id.nmat = '" + matricule + "'");
		if (!ClsObjectUtil.isListEmty(l) && l.get(0) != null)
		{
			modePaiement = (String) l.get(0);
			modePaiement = this.getLabelFromNomenclature(cdos, 54, modePaiement, 4, 0, 9, moisPaieCourant, "");
		}
		//
		return modePaiement;
	}

	/**
	 * => Compte_Jours
	 * 
	 * @param cdos
	 * @param typeOfDay
	 * @param dateDebut
	 * @param dateFin
	 * @return le nombre de jours
	 */
	public long compteJours(String cdos, ClsEnumeration.EnTypeOfDay typeOfDay, Date dateDebut, Date dateFin, String strDateFormat)
	{
		if(1==1)
			return AbsenceCongeUtil.pr_compte_jours(service, paramDataRepository, cdos, typeOfDay == ClsEnumeration.EnTypeOfDay.C?"C":"A", dateDebut, dateFin, TypeBDUtil.typeBD);
		long w_nb = 0;
		long w_nbtot = 0;
		String w_mois = "";
		long numberMaxOfDayByMonth = 0;
		int totalDaysOfMonth = 0;
		boolean Base_30 = false;
		String W_Cas_1 = (typeOfDay == ClsEnumeration.EnTypeOfDay.C) ? "O" : "%";
		String W_Cas_2 = (typeOfDay == ClsEnumeration.EnTypeOfDay.C) ? "N" : "%";
//		String strPacalCursor = "select count(*), concat(month(comp_id.jour), year(comp_id.jour)) as period from Rhpcalendrier " + " where comp_id.cdos = '" + cdos + "'"
//				+ " and comp_id.jour between '" + new ClsDate(dateDebut, strDateFormat).getDateS() + "' and '" + new ClsDate(dateFin, strDateFormat).getDateS() + "'" + " and ouvr like '" + W_Cas_1
//				+ "'" + " and fer  like '" + W_Cas_2 + "'" + " group by concat(month(comp_id.jour), year(comp_id.jour))";
		
		
		String strPacalCursor = "select count(*), TO_CHAR(comp_id.jour,'yyyyMM') as period from Rhpcalendrier " + " where comp_id.cdos = '" + cdos + "'"
		+ " and comp_id.jour between '" + new ClsDate(dateDebut, strDateFormat).getDateS() + "' and '" + new ClsDate(dateFin, strDateFormat).getDateS() + "'" + " and ouvr like '" + W_Cas_1
		+ "'" + " and fer  like '" + W_Cas_2 + "'" + " group by TO_CHAR(comp_id.jour,'yyyyMM')";
		
		if(TypeBDUtil.AS == TypeBDUtil.typeBD || TypeBDUtil.DB == TypeBDUtil.typeBD)
			strPacalCursor = "select count(*), concat(year(comp_id.jour),month(comp_id.jour)) as period from Rhpcalendrier " + " where comp_id.cdos = '" + cdos + "'"
			+ " and comp_id.jour between '" + new ClsDate(dateDebut, strDateFormat).getDateS() + "' and '" + new ClsDate(dateFin, strDateFormat).getDateS() + "'" + " and ouvr like '" + W_Cas_1
			+ "'" + " and fer  like '" + W_Cas_2 + "'" + " group by concat(year(comp_id.jour),month(comp_id.jour))";
		
		
		String strQuery = "select vall, valm from ParamData " + " where comp_id.cdos = '" + cdos + "'" + " and comp_id.ctab = 99 " + " and comp_id.cacc = 'BASE30'" + " and comp_id.nume  = 1";
		List listOfNomenc = service.find(strQuery);
		String valueLabel = "";
		if (listOfNomenc != null && listOfNomenc.size() > 0)
		{
			Object[] row = (Object[]) listOfNomenc.get(0);
			valueLabel = (row[0] == null) ? "" : (String) row[0];
			numberMaxOfDayByMonth = (row[1] == null) ? 0 : (Long) row[1];
		}
		else
		{
			valueLabel = "O";
			numberMaxOfDayByMonth = 30;
		}
		if ((!"O".equals(valueLabel)) && (!"N".equals(valueLabel)))
		{
			valueLabel = "O";
			numberMaxOfDayByMonth = 30;
		}
		if ("O".equals(valueLabel))
		{
			Base_30 = true;
			if (numberMaxOfDayByMonth < 1 || numberMaxOfDayByMonth > 99)
				numberMaxOfDayByMonth = 30;
		}
		else
		{
			Base_30 = false;
		}
		w_nbtot = 0;
		List oListPacal = service.find(strPacalCursor);
		Object[] oPacalRow = null;
		for (Object object : oListPacal)
		{
			oPacalRow = (Object[]) object;
			w_nb = ClsObjectUtil.getIntegerFromObject(oPacalRow[0]);
			w_mois = ClsStringUtil.formatNumber(Integer.valueOf((String) oPacalRow[1]), ClsFictifParameterOfPay.FORMAT_PERIOD_OF_PAY);
			if (Base_30 == true)
			{
				totalDaysOfMonth = new ClsDate(w_mois, ClsFictifParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getMaxDayOfMonth();
				if (totalDaysOfMonth == w_nb)
				{
					w_nb = numberMaxOfDayByMonth;
				}
				else if ((typeOfDay != ClsEnumeration.EnTypeOfDay.C) && ClsDate.haveTheSameMonth(dateDebut, dateFin))
				{
					if (totalDaysOfMonth == 31)
					{
						if (ClsDate.getDay(dateFin) == 31)
						{
							w_nb = numberMaxOfDayByMonth - ClsDate.getDay(dateFin) + 1;
						}
						else
						{
							if (ClsDate.getDay(dateFin) == 30 && ClsDate.getDay(dateDebut) == 1)
								w_nb = 29;
						}
					}
				}
			}
			w_nbtot = w_nbtot + w_nb;
		}
		return w_nbtot;
	}

	/**
	 * 
	 * @param cdos
	 * @param typeOfDay
	 * @param dateDebut
	 * @param dateFin
	 * @param dateFormat
	 * @return le nombre de jours
	 */
	public long compteJours(String cdos, ClsEnumeration.EnTypeOfDay typeOfDay, String dateDebut, String dateFin, String dateFormat)
	{
		ClsDate debutDateCls = new ClsDate(dateDebut, dateFormat);
		ClsDate finDateCls = new ClsDate(dateFin, dateFormat);
		return compteJours(cdos, typeOfDay, debutDateCls.getDate(), finDateCls.getDate(), dateFormat);

		/*
		 * long w_nb = 0; long w_nbtot = 0; String w_mois = ""; long numberMaxOfDayByMonth = 0; int totalDaysOfMonth = 0; boolean Base_30 = false; String W_Cas_1 = (typeOfDay ==
		 * ClsEnumeration.EnTypeOfDay.C)? "O" : "%"; String W_Cas_2 = (typeOfDay == ClsEnumeration.EnTypeOfDay.C)? "N" : "%"; String strPacalCursor = "select count(*), concat(month(comp_id.jour),
		 * year(comp_id.jour)) as period from Rhpcalendrier " + " where comp_id.cdos = '" + cdos + "'" + " and comp_id.jour between '" + debutDateCls.getDateS() + "' and '" + finDateCls.getDateS() +
		 * "'" + " and ouvr like '" + W_Cas_1 + "'" + " and fer like '" + W_Cas_2 + "'" + " group by concat(month(comp_id.jour), year(comp_id.jour))"; String strQuery = "select vall, valm from ParamData " + "
		 * where comp_id.cdos = '" + cdos + "'" + " and comp_id.ctab = 99 " + " and comp_id.cacc = 'BASE30'" + " and comp_id.nume = 1"; List listOfNomenc = service.find(strQuery); String valueLabel =
		 * ""; if(listOfNomenc != null && listOfNomenc.size() > 0){ Object[] row = (Object[])listOfNomenc.get(0); valueLabel = (row[0] == null)? "" : (String)row[0]; numberMaxOfDayByMonth = (row[1] ==
		 * null)? 0 : (Long)row[1]; } else{ valueLabel = "O"; numberMaxOfDayByMonth = 30; } if((! "O".equals(valueLabel)) && (! "N".equals(valueLabel))){ valueLabel = "O"; numberMaxOfDayByMonth = 30; }
		 * if("O".equals(valueLabel)){ Base_30 = true; if(numberMaxOfDayByMonth < 1 || numberMaxOfDayByMonth > 99) numberMaxOfDayByMonth = 30; } else{ Base_30 = false; } w_nbtot = 0; List oListPacal =
		 * service.find(strPacalCursor); Object[] oPacalRow = null; for (Object object : oListPacal) { oPacalRow = (Object[])object; w_nb = (Integer)oPacalRow[0]; w_mois =
		 * ClsStringUtil.formatNumber(Integer.valueOf((String)oPacalRow[1]), ClsParameterOfPay.FORMAT_PERIOD_OF_PAY) ; if(Base_30 == true){ totalDaysOfMonth = new ClsDate(w_mois,
		 * ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_MMYYYY).getMaxDayOfMonth(); if(totalDaysOfMonth == w_nb){ w_nb = numberMaxOfDayByMonth; } else if((typeOfDay == ClsEnumeration.EnTypeOfDay.C) &&
		 * ClsDate.haveTheSameMonth(debutDateCls.getDate(), finDateCls.getDate())){ if(totalDaysOfMonth == 31){ if(ClsDate.getDay(debutDateCls.getDate()) == 31){ w_nb = numberMaxOfDayByMonth -
		 * ClsDate.getDay(finDateCls.getDate()) + 1; } else{ if(ClsDate.getDay(finDateCls.getDate()) == 30 && ClsDate.getDay(debutDateCls.getDate()) == 1) w_nb = 29; } } } } w_nbtot = w_nbtot + w_nb; }
		 * return w_nbtot;
		 */
	}

	/**
	 * => Absent_Tout_Mois
	 * 
	 * @param cdos
	 * @param i_nmat
	 * @param i_aamm
	 * @param i_nbul
	 * @param retro
	 * @param dateDebut
	 * @param dateFin
	 * @return true ou false
	 */
	public boolean isAbsentAllOfTheMonth(String cdos, String i_nmat, String i_aamm, int i_nbul, boolean retro, Date dateDebut, Date dateFin, String strDateFormat)
	{
		ParameterUtil.println(">>isAbsentAllOfTheMonthw");
		//
		ClsDate debutDateCls = new ClsDate(dateDebut, strDateFormat);
		ClsDate finDateCls = new ClsDate(dateFin, strDateFormat);
		//
		double montantWithCol1 = -1;
		double montantWithCol4 = -1;
		double montantWithCol8 = -1;
		Date dateMin = null;
		Date dateMax = null;
		Date dateSuivante = null;
		boolean w_suite = true;
		Date ddeb = new Date();
		Date dfin = new Date();
		String motf = "";
		//
		String strPaevcgSqlString = "From Rhteltvarconge " + " where comp_id.cdos = '" + cdos
				+ "'" + " and comp_id.nmat = '" + i_nmat + "'" + " and comp_id.aamm = '" + i_aamm + "'" + " and comp_id.nbul = " + i_nbul + " and comp_id.ddeb between '" + debutDateCls.getDateS()
				+ "' and '" + finDateCls.getDateS() + "'" + " and dfin between '" + debutDateCls.getDateS() + "' and '" + finDateCls.getDateS() + "'" + " ORDER BY motf, comp_id.ddeb";

		String strPahevcgSqlString = "From Rhthevcg " + " where comp_id.cdos = '" + cdos
				+ "'" + " and comp_id.nmat = '" + i_nmat + "'" + " and comp_id.aamm = '" + i_aamm + "'" + " and comp_id.nbul = " + i_nbul + " and comp_id.ddeb between '" + debutDateCls.getDateS()
				+ "' and '" + finDateCls.getDateS() + "'" + " and dfin between '" + debutDateCls.getDateS() + "' and '" + finDateCls.getDateS() + "'" + " ORDER BY motf, comp_id.ddeb";
		String selectPart = "select max(case when nume = 1 then valm else 0 end)" +
							", max(case when nume = 4 then valm else 0 end)"+
							", max(case when nume = 8 then valm else 0 end)";
		String wherePart = " where comp_id.cdos = '" + cdos + "'" + " and comp_id.ctab = 22" + " and comp_id.cacc = 'MOTIF'" + " and comp_id.nume in (1, 4, 8)";
		List oIter = null;
		oIter = (retro == true) ? service.find(strPahevcgSqlString) : service.find(strPaevcgSqlString);
		ElementVariableConge oPaevcgRow = null;
		String complexQuery = "";
		List lh = null;
		Object[] lho = null;

		for (Object object : oIter)
		{
				oPaevcgRow = (ElementVariableConge) object;
				ddeb = oPaevcgRow.getDdeb();
				dfin = oPaevcgRow.getDfin();
				motf = StringUtils.isBlank(oPaevcgRow.getMotf())?"":oPaevcgRow.getMotf();

				complexQuery = selectPart;
				complexQuery += " from ParamData";
				complexQuery += wherePart.replace("MOTIF", motf);
				
				lh = service.find(complexQuery);
				
				if(lh == null || lh.size() == 0)
					w_suite = false;

			if (w_suite)
			{
				lho = (Object[]) lh.get(0);
					
				if (lho[0] != null)
					montantWithCol1 = (Long) lho[0];

				if (lho[1] != null)
					montantWithCol4 = (Long) lho[1];

				if (lho[2] != null)
					montantWithCol8 = (Long) lho[2];
				
				if (montantWithCol1 == 0 && montantWithCol4 == 0 && montantWithCol8 == 0)
				{
					if (dateMin == null)
					{
						dateMin = ddeb;
						dateMax = dfin;
					}
					if (dateSuivante == null)
					{
						dateSuivante = dfin;
					}
					else
					{
						if (ddeb .compareTo( new ClsDate(dateSuivante).addDay(1)) != 0)
						{
							break;
						}
						else
							dateSuivante = dfin;
					}
					if (dfin.getTime() > new ClsDate(dateMax).getDate().getTime())
						dateMax = dfin;
				}
			}
			else
			{
				break;
			}
		}
		//
		if (dateMin != null && dateMax != null)
		{
			if (new ClsDate(dateMin).getDate().equals(debutDateCls.getDate()) && new ClsDate(dateMax).getDate().equals(finDateCls.getDate()))
				return true;
			else
				return false;
		}
		else
			return false;
	}

	private double _getMaxValMFromTable22(String strCodeDossier, String strCodeLangue, String strMotif, String strColNumber)
	{
		Optional<ParamData> oNomenclature = paramDataRepository.findByNumeroLigne(Integer.valueOf("22"), strMotif, Integer.valueOf(strColNumber));

		if (oNomenclature.isEmpty())
			return 0;
		else
			return Double.valueOf(oNomenclature.get().getValm());
	}
}
