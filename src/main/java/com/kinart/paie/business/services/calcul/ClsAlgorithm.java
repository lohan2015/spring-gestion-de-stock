package com.kinart.paie.business.services.calcul;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kinart.paie.business.model.*;
import com.kinart.paie.business.services.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.dao.DataAccessException;

/**
 * Cette classe donne une implémentation de tous les algorithmes utilisés pour le calcul de la base de clacul, de la base plafonnée, du taux et du montant des différentes rubriques. Au paramétrage, il
 * est précisé pour chaque rubrique l'algorithme qui sera utilisé.
 * <p>
 * Elle est la transcription Java du package Oracle PA_ALGO de la version Oracle de la paie.
 * </p>
 * @author
 * @version 1.0
 */
public class ClsAlgorithm implements IAlgorithm
{
	public String outputtext = "";

	ClsSalaryToProcess salary = null;
	
	private Number tempNumber = null;

	Map<String, ClsInfoSalaryClone> infos = new HashMap<String, ClsInfoSalaryClone>();
	
	//ClsAlgorithmComilog comilog;
	
	Object sncc;

	/**
	 * Constructeur par défaut
	 */
	private ClsAlgorithm()
	{
		infos = new HashMap<String, ClsInfoSalaryClone>();
	}

	/**
	 * A la construction de l'objet, il faut indiquer le salarié auquel correspond cet objet pour pouvoir utiliser les ressources qu'il regroupe déjé comme les paramétres de calcul et le service
	 * d'accés é la base de données.
	 * @param salary le salarié dont on calcule actuellement la paie.
	 */
	public ClsAlgorithm(ClsSalaryToProcess salary)
	{
		this.salary = salary;
		infos = new HashMap<String, ClsInfoSalaryClone>();
		// = new ClsAlgorithmComilog(salary);
		//this.instanceSNCC();
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo1(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo1(ClsRubriqueClone rubrique)
	{
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo1";		
		salary.getValeurRubriquePartage().setRates(0);
		salary.getValeurRubriquePartage().setAmount(salary.getValeurRubriquePartage().getBase());
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "montant : " + salary.getValeurRubriquePartage().getAmount();
		return true;
	}
	
	public boolean algo9(ClsRubriqueClone rubrique)
	{
		String error = null;
		salary.getValeurRubriquePartage().setBase(0);
		salary.getValeurRubriquePartage().setBasePlafonnee(0);
		salary.getValeurRubriquePartage().setAmount(0);
		salary.getValeurRubriquePartage().setRates(0);
		
		String cleAcces = rubrique.getRubrique().getComp_id().getCrub();
		String tabl = rubrique.getRubrique().getTabl();
		String cdos = salary.parameter.dossier;
		String nmat = salary.getInfoSalary().getComp_id().getNmat();
		long algo = rubrique.getRubrique().getAlgo();
		String queryParam = "From ParamData where identreprise='"+cdos+"' and ctab = "+tabl+"  and cacc = '"+cleAcces+"' order by nume ";
		List<ParamData> lst = salary.service.find(queryParam);
		if(lst.isEmpty())
		{
			error = "ALGO" + algo + ", Sal " + nmat + ", Rbq " + cleAcces + ": Parametre " + cleAcces + ", Mq en T " + tabl;
			salary.getParameter().setError(error);
			salary.getParameter().setPbWithCalulation(true);
			return false;
		}
		Integer sizeParam = lst.size();
		//LIB1 TABLE PHYSIQUE
		//LIB2 FONCTION SQL
		//LIBXX CONDITION XX-2 SOUS LA FORME COLONNE#SIGNE#VALEUR
		String table = null;
		String fonction = null;
		String[] conditions = new String[20];
		ParamData fnom = null;
		Integer nume = 0;
		ClsRubriqueClone clone = null;
		
			
		for(int i=0; i<sizeParam; i++)
		{
			fnom = lst.get(i);
			nume = fnom.getNume();
			if(nume==1)
				table = fnom.getVall();
			else if(nume==2)
				fonction = fnom.getVall();
			else
			{
				conditions[i] = fnom.getVall();
			}		
		}
		
		if(StringUtils.isBlank(table))
		{
			error = "ALGO" + algo + ", Sal " + nmat + ", Rbq " + cleAcces + ": Parametre " + cleAcces + ",Lib 1 Mq en T " + tabl;
			salary.getParameter().setError(error);
			salary.getParameter().setPbWithCalulation(true);
			return false;
		}
		
		if(StringUtils.isBlank(fonction))
		{
			error = "ALGO" + algo + ", Sal " + nmat + ", Rbq " + cleAcces + ": Parametre " + cleAcces + ",Lib 2 Mq en T " + tabl;
			salary.getParameter().setError(error);
			salary.getParameter().setPbWithCalulation(true);
			return false;
		}
		
		String queryEnd="Select "+fonction+" From "+table+" Where identreprise='"+cdos+"' and nmat = '"+ nmat +"'";
		String colonne;
		String signe;
		String valeur;
		String[] decoup = null;
		String[] cle;
		for(int i=0; i<conditions.length; i++)
		{
			//Traitement des conditions
			if(StringUtils.isBlank(conditions[i]))
				continue;
			decoup = StringUtils.split(conditions[i], "#");
//			if(decoup.length != 3)
//			{
//				error = "ALGO" + algo + ", Sal " + nmat + ", Rbq " + cleAcces + ": Parametre " + cleAcces + ",Lib "+i+3+" Mal formé en T " + tabl;
//				salary.getParameter().setError(error);
//				salary.getParameter().setPbWithCalulation(true);
//				return false;
//			}
			colonne = decoup[0] ;
			signe = decoup[1];
			if(decoup.length>2)
				valeur = decoup[2];
			else
				valeur = StringUtils.EMPTY;
			
			if(StringUtils.contains(colonne, "AGE"))
			{
				cle = StringUtils.split(colonne,".");
				if(cle.length != 2)
				{
					error = "ALGO" + algo + ", Sal " + nmat + ", Rbq " + cleAcces + ": Parametre " + cleAcces + ",Lib "+i+3+" Mal formé en T " + tabl;
					salary.getParameter().setError(error);
					salary.getParameter().setPbWithCalulation(true);
					return false;
				}
				colonne = cle[1];
				if(StringUtils.contains(signe, "<"))
					signe = StringUtils.replace(signe, "<", ">");
				else
					if(StringUtils.contains(signe, ">"))
						signe = StringUtils.replace(signe, ">", "<");
				
				long anneecourante = salary.getParameter().getMyMonthOfPay().getYear();
				long annee = anneecourante - Integer.valueOf(decoup[2]);// - 1;
				long mois = salary.getParameter().getMyMonthOfPay().getMonth();
//				mois++;
//				if (mois > 12)
//				{
//					mois = 1;
//					annee++;
//				}
				String dtna = ClsStringUtil.formatNumber(annee, "0000") + ClsStringUtil.formatNumber(mois, "00");
				Date dtna1 = new ClsDate(dtna, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getFirstDayOfMonth();
				valeur = new ClsDate(dtna1).getDateS(salary.parameter.appDateFormat) ;		
			}
			if(StringUtils.contains(signe.toLowerCase(), "null"))
				queryEnd+=" and "+colonne+" "+signe;
			else
				queryEnd+=" and "+colonne+" "+signe+" '" + valeur + "'";
		}
		
		//Les paramétres standard de la requete
		queryEnd  = queryEnd.replaceAll(":cdos", cdos);
		queryEnd  = queryEnd.replaceAll(":CDOS", cdos);
		queryEnd  = queryEnd.replaceAll(":nmat", nmat);
		queryEnd  = queryEnd.replaceAll(":NMAT", nmat);
		queryEnd  = queryEnd.replaceAll(":MDP", salary.parameter.monthOfPay);
		queryEnd  = queryEnd.replaceAll(":mdp", salary.parameter.monthOfPay);
		queryEnd  = queryEnd.replaceAll(":DDMP", new ClsDate(new ClsDate(salary.parameter.getMyMoisPaieCourant().getDate()).addMonth(-1)).getDateS("yyyyMM"));
		queryEnd  = queryEnd.replaceAll(":ddmp", new ClsDate(new ClsDate(salary.parameter.getMyMoisPaieCourant().getDate()).addMonth(-1)).getDateS("yyyyMM"));
		
		
		List nbreenfant = null;
		Session session = null;
		try
		{
			session = salary.getService().getSession();
			Query q = session.createSQLQuery(queryEnd);		
			nbreenfant = q.list();	
		}
		catch (Exception e)
		{
			error = "ALGO" + algo + ", Sal " + nmat + ", Rbq " + cleAcces + ": Parametre " + cleAcces + ", Impossible de fusionner les conditions en T " + tabl;
			salary.getParameter().setError(error);
			salary.getParameter().setPbWithCalulation(true);
			return false;
		}
		finally
		{
			salary.getService().closeSession(session);
		}
		if(nbreenfant!= null && ! nbreenfant.isEmpty())
		{
			double nbr = 0;
			if(nbreenfant.get(0) != null)
				nbr = Double.parseDouble(nbreenfant.get(0).toString().replaceAll(",", "."));
			
			salary.getValeurRubriquePartage().setBase(nbr);
			salary.getValeurRubriquePartage().setBasePlafonnee(nbr);
			salary.getValeurRubriquePartage().setAmount(nbr);
		}
		return true;
	}
	//Idem é l'algo 9, sauf qu'on peut faire des requetes comme souhaité, et le resultat de la requete doit étre un nombre
	public boolean algo32(ClsRubriqueClone rubrique)
	{
		String error = null;
		salary.getValeurRubriquePartage().setBase(0);
		salary.getValeurRubriquePartage().setBasePlafonnee(0);
		salary.getValeurRubriquePartage().setAmount(0);
		salary.getValeurRubriquePartage().setRates(0);
		
		String cleAcces = rubrique.getRubrique().getComp_id().getCrub();
		String tabl = rubrique.getRubrique().getTabl();
		String cdos = salary.parameter.dossier;
		String nmat = salary.getInfoSalary().getComp_id().getNmat();
		long algo = rubrique.getRubrique().getAlgo();
		String queryParam = "From ParamData where identreprise='"+cdos+"' and ctab = "+tabl+"  and cacc = '"+cleAcces+"' and nume>1 order by nume ";
		List<ParamData> lst = salary.service.find(queryParam);
		if(lst.isEmpty())
		{
			error = "ALGO" + algo + ", Sal " + nmat + ", Rbq " + cleAcces + ": Parametre " + cleAcces + ", Mq en T " + tabl;
			salary.getParameter().setError(error);
			salary.getParameter().setPbWithCalulation(true);
			return false;
		}
		Integer sizeParam = lst.size();
		//LIB1  Description de la requete
		//LIB2  Requete SQL, les variables sont sous la forme :var
		//LIB i SuiteRequete SQL, les variables sont sous la forme :var
		String requete = null;
		ParamData fnom = null;
		Integer nume = 0;
		
			
		for(int i=0; i<sizeParam; i++)
		{
			fnom = lst.get(i);
			nume = fnom.getNume();
			if(nume>=2)
				requete = StringUtils.isBlank(requete) ?fnom.getVall() : requete + " "+fnom.getVall();
		}
		
		if(StringUtils.isBlank(requete))
		{
			error = "ALGO" + algo + ", Sal " + nmat + ", Rbq " + cleAcces + ": Parametre " + cleAcces + ",Lib 2 Mq en Tab. " + tabl;
			salary.getParameter().setError(error);
			salary.getParameter().setPbWithCalulation(true);
			return false;
		}

		String queryEnd=requete;
		
		//Les paramétres standard de la requete
		queryEnd  = queryEnd.replaceAll(":cdos", cdos);
		queryEnd  = queryEnd.replaceAll(":Cdos", cdos);
		queryEnd  = queryEnd.replaceAll(":CDOS", cdos);
		queryEnd  = queryEnd.replaceAll(":Nmat", nmat);
		queryEnd  = queryEnd.replaceAll(":nmat", nmat);
		queryEnd  = queryEnd.replaceAll(":NMAT", nmat);
		queryEnd  = queryEnd.replaceAll(":MDP", salary.parameter.monthOfPay);
		queryEnd  = queryEnd.replaceAll(":mdp", salary.parameter.monthOfPay);
		queryEnd  = queryEnd.replaceAll(":DDMP", new ClsDate(new ClsDate(salary.parameter.getMyMoisPaieCourant().getDate()).addMonth(-1)).getDateS("yyyyMM"));
		queryEnd  = queryEnd.replaceAll(":ddmp", new ClsDate(new ClsDate(salary.parameter.getMyMoisPaieCourant().getDate()).addMonth(-1)).getDateS("yyyyMM"));
		//Pour une rubrique, le paramétre est du genre :[CODE_RUBRIQUE]
		List<String> rubriques = ExpressionEvaluator.getRubriques(queryEnd, false);
		ClsRubriqueClone clone;
		for(String crub : rubriques)
		{
			clone = this.salary.findRubriqueClone(crub);
			if(clone != null) queryEnd  = queryEnd.replaceAll(":["+crub+"]", clone.getAmount()+"");
		}
		
		List liste = null;
		Session session = null;
		try
		{
			session = salary.getService().getSession();
			Query q = session.createSQLQuery(queryEnd);	
			liste = q.list();	
		}
		catch (Exception e)
		{
			error = "ALGO" + algo + ", Sal " + nmat + ", Rbq " + cleAcces + ": Parametre " + cleAcces + ", Impossible de fusionner les conditions en T " + tabl;
			salary.getParameter().setError(error);
			salary.getParameter().setPbWithCalulation(true);
			return false;
		}
		finally
		{
			salary.getService().closeSession(session);
		}
		if(liste!= null && ! liste.isEmpty())
		{
			double nbr = 0;
			if(liste.get(0) != null)
			{
				try
				{
					nbr = Double.parseDouble(liste.get(0).toString().replaceAll(",", "."));
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			salary.getValeurRubriquePartage().setBase(nbr);
			salary.getValeurRubriquePartage().setBasePlafonnee(nbr);
			salary.getValeurRubriquePartage().setAmount(nbr);
		}
		return true;
	}
	
	private Number getAmountOrRateFromNomenclature(Map<String, ParamData> map,String cdos, int ntable, String cleAcces, long numLigne, long nlot, int nbul, String moisPaieCourant, String periodeTraitement,
			ClsEnumeration.EnColumnToRead columnName, String crub)
	{
		Integer moisDonnee =  this.salary.parameter.moisDonnees.get(crub);
		if(moisDonnee != null)
		{
			periodeTraitement = new ClsDate(new ClsDate(periodeTraitement,"yyyyMM").addMonth(-moisDonnee)).getYearAndMonth();
		}
		Number tempNumber = this.salary.utilNomenclature.getAmountOrRateFromNomenclature(map,cdos, ntable, cleAcces, numLigne, nlot,
				nbul, moisPaieCourant, periodeTraitement, columnName);
		
		return tempNumber;
		
	}
	
	private String getLabelFromNomenclature(String cdos, int ntable, String cleAcces, int numLigne, long nlot, int nbul, String moisPaieCourant, String periodeTraitement, String crub)
	{
		Integer moisDonnee =  this.salary.parameter.moisDonnees.get(crub);
		if(moisDonnee != null)
		{
			periodeTraitement = new ClsDate(new ClsDate(periodeTraitement,"yyyyMM").addMonth(-moisDonnee)).getYearAndMonth();
		}
		String libelle = this.salary.utilNomenclature.getLabelFromNomenclature(cdos, ntable, cleAcces, numLigne, nlot, nbul, moisPaieCourant, periodeTraitement);
		return libelle;
	}
	

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo10(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo10(ClsRubriqueClone rubrique)
	{
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo10";
		// String codeRubrique = rubrique.getRubrique().getComp_id().getCrub();
		// double amountValue = -1;
		double rateValue = 0;
		int tabl = 0;
		double val = -1;
		String cleAccess = "";
		tempNumber = null;
//		if (StringUtils.notEquals(this.salary.getInfoSalary().getNiv3(),this.salary.getParameter().getPrecNiv3()))
//		{
//			
			tabl = 3;
			cleAccess = this.salary.getInfoSalary().getNiv3();
			tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,this.salary.getInfoSalary().getComp_id().getCdos(), tabl, cleAccess, 1, this.salary.getNlot(),
					this.salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.RATES, rubrique.getRubrique().getComp_id().getCrub());
			
			if (tempNumber == null)
			{
				// logger
				salary.getParameter().setError(
						salary.getParameter().errorMessage("ERR-90056", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
								salary.getInfoSalary().getComp_id().getNmat(), salary.getInfoSalary().getNiv3(), cleAccess, rubrique.getRubrique().getTabl()));
				return false;
			}
			rateValue = tempNumber.doubleValue();
			this.salary.getWorkTime().setProrataNbreHeures(rateValue);
			this.salary.getParameter().setPrecNiv3(this.salary.getInfoSalary().getNiv3());
//		}
//		else
//			this.salary.getWorkTime().setProrataNbreHeures(0);
		//
		this.salary.getValeurRubriquePartage().setRates(rateValue);
		if ("T".equals(rubrique.getRubrique().getToum()) || "D".equals(rubrique.getRubrique().getToum()))
		{
			if (salary.getValeurRubriquePartage().getRates() != 0)
			{
				val = salary.getValeurRubriquePartage().getBase() / salary.getValeurRubriquePartage().getRates();
				salary.getValeurRubriquePartage().setAmount(val);
			}
		}
		else
		{
			val = salary.getValeurRubriquePartage().getBase() * salary.getValeurRubriquePartage().getRates();
			salary.getValeurRubriquePartage().setAmount(val);
		}
		return true;
	}

	public boolean algo100(ClsRubriqueClone rubrique)
	{
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo100";
		salary.getValeurRubriquePartage().setRates(0);
		int agePeriode = ClsDate.getYearsBetween(salary.getInfoSalary().getDtna(), salary.getParameter().getMyMonthOfPay().getDate());
		salary.getValeurRubriquePartage().setAmount(agePeriode);
		salary.getValeurRubriquePartage().setBase(agePeriode);
		salary.getValeurRubriquePartage().setBasePlafonnee(agePeriode);
		return true;
	}

	public boolean algo101(ClsRubriqueClone rubrique)
	{
		
		if(1==1)
		{
			int anciennete =  salary.getAnciennete();
			int i = new ClsDate(salary.getInfoSalary().getDdca()).getMonth();
			int j = salary.getMyMonthOfPay().getMonth();
			j = j - i;
			if (j < 0)
				j = j + 12;
			i = new ClsDate(salary.getInfoSalary().getDdca()).getDay();
			if (i == 1)
				i = 0;
			else if (i == 31)
				i = 30;
			Integer nbrjours = 360 * anciennete+ (30 * (j + 1)) - i;
			salary.getValeurRubriquePartage().setRates(j);
			salary.getValeurRubriquePartage().setAmount(anciennete);
			salary.getValeurRubriquePartage().setBase(nbrjours);
			salary.getValeurRubriquePartage().setBasePlafonnee(nbrjours);
			

			return true;
		}
		
		Integer resultat = 0;
		
			if (salary.infoSalary.getDdca() == null)
				resultat= 0;
			ClsDate myDdca = new ClsDate(salary.infoSalary.getDdca());
			int nbreYear = 0;
			
			if (salary.entreprise.equals(ClsEnumeration.EnEnterprise.SGMB) && myDdca.getDay() != 1)
				salary.infoSalary.setDdca(myDdca.clone().addMonth(1));

			int nyear = salary.myMonthOfPay.getYear();
		
			if ((nyear - myDdca.getYear()) > salary.getParameter().AGE_MAX_OF_SALARY)
			{
				salary.parameter.setError(salary.parameter.errorMessage("ERR-90045", salary.parameter.getLangue(), salary.infoSalary.getComp_id().getNmat()));

				return false;
			}

			nbreYear = calculateAnciennetePre();

			if (nbreYear < 0)
			{
				salary.parameter.setError(salary.parameter.errorMessage("ERR-90046", salary.parameter.getLangue(), salary.infoSalary.getComp_id().getNmat()));

				return false;
			}
			
			if (nbreYear > salary.parameter.AGE_MAX_OF_SALARY)
			{
				salary.parameter.setError(salary.parameter.errorMessage("ERR-90045", salary.parameter.getLangue(), salary.infoSalary.getComp_id().getNmat()));

				return false;
			}
		
			if (nbreYear > salary.parameter.getAncienneteMaxi())
				resultat = salary.parameter.getAncienneteMaxi();
			else
				resultat = nbreYear;
			
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo101";
			salary.getValeurRubriquePartage().setRates(0);
			int anciennete = resultat; // ClsDate.getYearsBetween(salary.getInfoSalary().getDdca(),  salary.getParameter().getMyMonthOfPay().getDate());
			salary.getValeurRubriquePartage().setAmount(anciennete);
			salary.getValeurRubriquePartage().setBase(anciennete);
			salary.getValeurRubriquePartage().setBasePlafonnee(anciennete);
			return true;
		
	}
	public int calculateAnciennetePre()
	{
		int nMonthDeducted = -1;
		int nDayDeduted = -1;
	
		Date dateDebutArriereOfPay = new Date();
		Date dateFinArriereOfPay = new Date();
		int year = -1;
		int mois = -1;
		int wmois = -1;
		int nbreYear = -1;
		
		year = 0;
		mois = 0;
		wmois = 0;
		nbreYear = 0;

		nDayDeduted = 0;
		ClsDate myMonthOfPay = new ClsDate(salary.monthOfPay, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
		Date monthOfPayDate = myMonthOfPay.getDate();
		Session session = salary.service.getSession();
		try
		{
			String query = "select ddar, dfar from Rhtarrpaiagent where cdos = :cdos and mtar in (select cacc from ParamData where cdos =:cdos and ctab = :ctab and nume = :nume and valm = 1) and nmat = :nmat" +
			" and ddar < :ddar order by ddar";
			Query q = session.createQuery(query);
			q.setParameter("cdos", salary.parameter.getDossier());
			q.setParameter("ctab", 21);
			q.setParameter("nume", 1);
			q.setParameter("nmat", salary.infoSalary.getComp_id().getNmat());
			q.setParameter("ddar", myMonthOfPay.getDate());
			List listOfArriere = q.list();
			long l_nDayDeduted = 0;
			Object[] rowOfArr = null;
			long millisecondperday = 86400000;
			for (Object object : listOfArriere)
			{
				rowOfArr = (Object[]) object;
				dateDebutArriereOfPay = (Date) rowOfArr[0];
				dateFinArriereOfPay = (Date) rowOfArr[1];

				if (dateFinArriereOfPay.compareTo(monthOfPayDate) >= 0)
					l_nDayDeduted = (monthOfPayDate.getTime() - dateDebutArriereOfPay.getTime()) / millisecondperday;
				else
					l_nDayDeduted = (dateFinArriereOfPay.getTime() - dateDebutArriereOfPay.getTime()) / millisecondperday;
				
				nDayDeduted += l_nDayDeduted;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			salary.service.closeSession(session);
		}
		nMonthDeducted = nDayDeduted / 30;
		ClsDate myDdca = new ClsDate(salary.infoSalary.getDdca());
		year = myMonthOfPay.getYear();
		mois = myMonthOfPay.getMonth();
		wmois = year - myDdca.getYear();
		wmois = (wmois * 12) + mois - nMonthDeducted;
		wmois = wmois - myDdca.getMonth();
		nbreYear = wmois / 12;
		if (nbreYear < 0)
			nbreYear = 0;
		return nbreYear;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo102(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo102(ClsRubriqueClone rubrique)
	{
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo102";
		String cdos = salary.getInfoSalary().getComp_id().getCdos();
		Double montant = new Double(0);
		String a_cacc = "";
		//
		salary.getValeurRubriquePartage().setRates(0);
		salary.getValeurRubriquePartage().setAmount(0);
		BigDecimal bd = new BigDecimal(salary.getValeurRubriquePartage().getBase());
		a_cacc = String.valueOf(bd.longValue());
		//
		// -- Lecture de la valeur dans les nomenclatures
		ClsEnumeration.EnColumnToRead col = "M".equals(rubrique.getRubrique().getToum()) ? ClsEnumeration.EnColumnToRead.AMOUNT : ClsEnumeration.EnColumnToRead.RATES;
		tempNumber = null;
		if (rubrique.getRubrique().getTabl() != null)
		{
			tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,cdos, Integer.valueOf(rubrique.getRubrique().getTabl()), a_cacc, rubrique.getRubrique().getNutm(), salary.getNlot(),
					salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), col, rubrique.getRubrique().getComp_id().getCrub());
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "----------------------------->Montant de la table " + rubrique.getRubrique().getTabl() + " = " + montant;
		}
		
		if (tempNumber == null)
		{
			// logger
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90056", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat(), a_cacc, rubrique.getRubrique().getTabl()));
			salary.getParameter().setPbWithCalulation(true);
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + salary.getParameter().getError();
			return false;
		}
		
		montant = tempNumber.doubleValue();
		salary.getValeurRubriquePartage().setValeur(montant);
		salary.getValeurRubriquePartage().setRates(0);
		salary.getValeurRubriquePartage().setAmount(montant);
		return true;
	}
	
	/**
	 * Calcul du nombre de jours supplementaires
	 * En fonction de l'ancienneté, le nombre de jours meres
	 */
	public boolean algo103(ClsRubriqueClone rubrique)
	{
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo103";
		salary.getValeurRubriquePartage().setRates(0);
		double anciennete = salary.calculNombreDJourSuppl();
		salary.getValeurRubriquePartage().setAmount(anciennete);
		salary.getValeurRubriquePartage().setBase(anciennete);
		salary.getValeurRubriquePartage().setBasePlafonnee(anciennete);
		return true;
	}
	
	/* 
	 * Calcul du nombre de jours d'absences suivant un motif paramétré en table (tabl) sur clé le code de la rubrique, en libellé 1.
	 * Le libellé 2 contient la colonne é lire (C ou A)
	 * Le montant 2 indique si on doit lire tous les congés (1) ou uniquement ceux dont les dates sont dans le mois en cours de traitement (0)
	 */
	public boolean algo104(ClsRubriqueClone rubrique)
	{
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo5";

		String codeRubrique = rubrique.getRubrique().getComp_id().getCrub();
		String cdos = salary.getInfoSalary().getComp_id().getCdos();
		String nmat = this.salary.getInfoSalary().getComp_id().getNmat();
		GeneriqueConnexionService service = salary.getService();
		if(StringUtils.isBlank(rubrique.getRubrique().getTabl()))
		{
			// logger
			salary.getParameter().setError(
					salary.getParameter().errorMessage("Rub. "+codeRubrique+" ,Algo."+rubrique.getRubrique().getAlgo()+", Mat. "+nmat+" Table Nom renseigné",salary.getParameter().getLangue()));
			return false;
		}
		Integer table =  Integer.valueOf(rubrique.getRubrique().getTabl());
		ParamData fnom = (ParamData)service.find("FROM ParamData WHERE identreprise="+cdos+" AND ctab="+table+" AND cacc='"+codeRubrique+" AND nume=1");
		if(fnom == null)
		{
			salary.getParameter().setError(
					salary.getParameter().errorMessage("Rub. "+codeRubrique+" ,Algo."+rubrique.getRubrique().getAlgo()+", Mat. "+nmat+" Clé "+codeRubrique+" Inexistante en table "+table,salary.getParameter().getLangue()));
			return false;
		}
		
		if(StringUtils.isBlank(fnom.getVall()))
		{
			salary.getParameter().setError(
					salary.getParameter().errorMessage("Rub. "+codeRubrique+" ,Algo."+rubrique.getRubrique().getAlgo()+", Mat. "+nmat+" Clé "+codeRubrique+", Libellé 1 Non renseigné en table "+table,salary.getParameter().getLangue()));
			return false;
		}
		String motif = fnom.getVall().trim();
		String colonne="A";
		 fnom = (ParamData)service.find("FROM ParamData WHERE identreprise="+cdos+" AND ctab="+table+" AND cacc='"+codeRubrique+" AND nume=2");
		if(StringUtils.isNotBlank(fnom.getVall()))
			colonne = fnom.getVall().trim().toUpperCase();
		
		if(fnom.getValm() == null)
			fnom.setValm(Long.valueOf(0));
		
		if(StringUtil.notIn(colonne, "A,C"))
		{
			salary.getParameter().setError(
					salary.getParameter().errorMessage("Rub. "+codeRubrique+" ,Algo."+rubrique.getRubrique().getAlgo()+", Mat. "+nmat+" Clé "+codeRubrique+", Libellé 2 doit étre A(Absence) ou C(congé) en table "+table,salary.getParameter().getLangue()));
			return false;
		}
		colonne = StringUtils.equals("C", colonne) ?"nbjc":"nbja";

		String ddeb = new ClsDate(salary.parameter.getFirstDayOfMonth()).getDateS(salary.parameter.appDateFormat);
		String dfin = new ClsDate(salary.parameter.getLastDayOfMonth()).getDateS(salary.parameter.appDateFormat);
		
		String query="select sum("+colonne+") from ElementVariableConge where identreprise='" + cdos + "' and nmat = '" + nmat+ "'  and nbul = " + salary.getNbul()+" and motf = '"+motif+"'";
		if(fnom.getValm() == 0)
			query+=" and ddeb >= '"+ddeb+"' and dfin <= '"+dfin+"'";
		List l = service.find( query);
		//List l = service.find( "select sum("+colonne+") from ElementVariableConge where identreprise='" + cdos + "' and nmat = '" + nmat+ "' and aamm = '" + salary.getPeriodOfPay() + "'" + " and nbul = " + salary.getNbul()+" and ddeb >= '"+ddeb+"' and dfin <= '"+dfin+"' and motf = '"+motif+"'" );
		double amountValue = 0;
		if (!ClsObjectUtil.isListEmty(l) && l.get(0) != null)
			amountValue = (Double)(((BigDecimal) l.get(0)).doubleValue());
		
		salary.getValeurRubriquePartage().setRates(0);
		salary.getValeurRubriquePartage().setAmount(amountValue);
		salary.getValeurRubriquePartage().setBase(amountValue);
		salary.getValeurRubriquePartage().setBasePlafonnee(amountValue);
	
		return true;
	}
	
	/**
	 * Calcul du nombre de jours dus é un agent lors de son départ en retraite ou licenciement : cas de Tasiast
	 */
	public boolean algo105(ClsRubriqueClone rubrique)
	{
//		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo105";
//		ClsRotationCalculation rotation = new ClsRotationCalculation(salary.service,salary.parameter.dossier);
//		boolean result = rotation.calculateRotation(salary, rubrique);
//		if(!result)
//			return false;
//		salary.getValeurRubriquePartage().setRates(rotation.getNbrDaysOffExercised());
//		salary.getValeurRubriquePartage().setAmount(rotation.getNbrDaysOwed());
//		salary.getValeurRubriquePartage().setBase(rotation.getNbrDaysOffAllowed());
//		salary.getValeurRubriquePartage().setBasePlafonnee(rotation.getNbrDaysOffAllowed());
		return true;
	}
	public boolean algo106(ClsRubriqueClone rubrique)
	{
		String cdos = salary.parameter.dossier;
		String nmat = salary.infoSalary.getComp_id().getNmat();
		String motifCongeAnnuel="01";
		Date firstDate = salary.infoSalary.getDtes();
		Date lastDate = salary.parameter.getFirstDayOfMonth();
		
		String query="Select a.* From HistoCongeSalarie a where a.identreprise='"+cdos+"' and a.nmat = '"+nmat+"' and a.cmcg='"+motifCongeAnnuel+"' and a.ddcg=(select max(b.ddcg) ";
		query+=" From HistoCongeSalarie b where a.cdos=b.cdos and a.nmat=b.nmat and a.cmcg=b.cmcg) ";
		
		Session session = salary.service.getSession();
		try
		{
			Query q = session.createSQLQuery(query).addEntity("a", HistoCongeSalarie.class);
			List<HistoCongeSalarie> lst = q.list();
			if(! lst.isEmpty())
				firstDate = lst.get(0).getDfcg();
			
			query="Select a.* From ElementVariableConge a where a.identreprise='"+cdos+"' and a.nmat = '"+nmat+"' and a.motf='"+motifCongeAnnuel+"' and a.ddeb=(select min(b.ddeb) ";
			query+=" From ElementVariableConge b where a.cdos=b.cdos and a.nmat=b.nmat and a.motf=b.motf) ";
			q = session.createSQLQuery(query).addEntity("a", ElementVariableConge.class);
			List<ElementVariableConge> lste = q.list();
			if(!lste.isEmpty())
				lastDate = lste.get(0).getDdeb();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		finally
		{
			salary.service.closeSession(session);
		}
		Integer base = Double.valueOf(ClsDate.getDateS(firstDate, "ddMMyy")).intValue();
		Integer taux = Double.valueOf(ClsDate.getDateS(lastDate, "ddMMyy")).intValue();
		//System.out.println("Base = "+base+" et Taux = "+taux);
		Integer nbr = ClsDate.getNumberOfDay(lastDate, firstDate).intValue();
		salary.getValeurRubriquePartage().setRates(taux);
		salary.getValeurRubriquePartage().setAmount(nbr);
		salary.getValeurRubriquePartage().setBase(base);
		salary.getValeurRubriquePartage().setBasePlafonnee(base);

		return true;
	}
	
	public boolean algo107(ClsRubriqueClone rubrique)
	{
		String cdos = salary.parameter.dossier;
		String nmat =  salary.getInfoSalary().getComp_id().getNmat();
		String aamm = salary.getMonthOfPay();
		String cacc1 = "JOURDUS-MP";
		String cacc2 = "JOURDUS-MC";
		String cacc6 = "JOURDUS-MS";
		String[] rubMP = new String[]{"","","","","","","",""};
		String[] rubMC =  new String[]{"","","","","","","",""};
		String[] rubMS =  new String[]{"","","","","","","",""};
		double[] montantsMC =  new double[]{0,0,0,0,0,0,0,0};
		double[] montantsMS =  new double[]{0,0,0,0,0,0,0,0};
		String crubJoursPris = null;
		int nbrRubrique = 8;
		
		String query = "Select nume,valm From ParamData where identreprise='" + cdos + "' and ctab = 99 and cacc = '" + cacc1 + "' order by nume";
		List<Object[]> lst = salary.service.find(query);
		for (Object[] o : lst)
		{
			if(o[1] != null && !"0".equalsIgnoreCase(o[1].toString()))
			{
				rubMP[Integer.valueOf(o[0].toString())-1]= ClsStringUtil.formatNumber(Integer.valueOf(o[1].toString()), ParameterUtil.formatRubrique);
			}
		}
		query = "Select nume,valm From ParamData where identreprise='" + cdos + "' and ctab = 99 and cacc = '" + cacc2 + "' order by nume";
		lst = salary.service.find(query);
		for (Object[] o : lst)
		{
			if(o[1] != null && !"0".equalsIgnoreCase(o[1].toString()))
			{
				rubMC[Integer.valueOf(o[0].toString())-1]= ClsStringUtil.formatNumber(Integer.valueOf(o[1].toString()), ParameterUtil.formatRubrique);
			}
		}
		
		query = "Select nume,valm From ParamData where identreprise='" + cdos + "' and ctab = 99 and cacc = '" + cacc6 + "' order by nume";
		lst = salary.service.find(query);
		for (Object[] o : lst)
		{
			if(o[1] != null && !"0".equalsIgnoreCase(o[1].toString()))
			{
				rubMS[Integer.valueOf(o[0].toString())-1]= ClsStringUtil.formatNumber(Integer.valueOf(o[1].toString()), ParameterUtil.formatRubrique);
			}
		}
		
		//Recherche de la rubrique du nombre de jours total de congés pris
		String cacc3="JOURCGPRIS";
		query = "Select valm From ParamData where identreprise='" + cdos + "' and ctab = 99 and cacc = '" + cacc3 + "' and nume = 1 ";
		List lst1 = salary.service.find(query);
		if(!lst1.isEmpty() && lst1.get(0) != null && !"0".equalsIgnoreCase(lst1.get(0).toString()))
			crubJoursPris = ClsStringUtil.formatNumber(Integer.valueOf(lst1.get(0).toString()), ParameterUtil.formatRubrique);
		double nbjPris = 0;
		if(StringUtils.isNotBlank(crubJoursPris))
		{
			ClsRubriqueClone rubJPris = salary.findRubriqueClone(crubJoursPris);
			if(rubJPris != null) nbjPris = rubJPris.getAmount();
		}
		double joursacquismois = 2;
		String cacc4="NBJC-DEF";
		query = "Select valt From ParamData where identreprise='" + cdos + "' and ctab = 99 and cacc = '" + cacc4 + "' and nume = 1 ";
		lst1 = salary.service.find(query);
		if(!lst1.isEmpty() && lst1.get(0) != null && !"0".equalsIgnoreCase(lst1.get(0).toString()))
			joursacquismois = Double.valueOf(lst1.get(0).toString());
		
		ClsRubriqueClone temp = null;
		for(int i=0; i<nbrRubrique;i++)
		{
			if(StringUtils.isNotBlank(rubMP[i]) && StringUtils.isNotBlank(rubMC[i]) )
			{
				temp = salary.findRubriqueClone(rubMP[i]);
				if(temp != null)
				{
					
					if(temp.getAmount() - nbjPris<0)
					{
						nbjPris = nbjPris - temp.getAmount();
						montantsMC[i]=Double.valueOf(0);
					}
					else
					{
						montantsMC[i]=Double.valueOf(temp.getAmount() - nbjPris);
						nbjPris = 0;
					}
					
				}
				
				if(i == nbrRubrique - 1)
				{
					//On calcul le nombre de jours de congé de l'année, puis on divise par 12 et on multiplie par le nombre de mois travaillés
					if(1==1)
					{
						//Recherche de la rubrique du nombre de congé par an
						String cacc7="JOURCGAN";
						String crubJCGAn  = null;
						query = "Select valm From ParamData where identreprise='" + cdos + "' and ctab = 99 and cacc = '" + cacc7 + "' and nume = 1 ";
						lst1 = salary.service.find(query);
						if(!lst1.isEmpty() && lst1.get(0) != null && !"0".equalsIgnoreCase(lst1.get(0).toString()))
							crubJCGAn = ClsStringUtil.formatNumber(Integer.valueOf(lst1.get(0).toString()), ParameterUtil.formatRubrique);
						double nbjrParAn = 26;
						if(StringUtils.isNotBlank(crubJCGAn))
						{
							ClsRubriqueClone rubJAN = salary.findRubriqueClone(crubJoursPris);
							if(rubJAN != null) nbjrParAn = rubJAN.getAmount();
						}
						//ON y rajoute les jours suppl:
						double joursuppl = salary.calculNombreDJourSuppl();
						
						nbjrParAn += joursuppl;
						//Nombre de mois
						int nbrMois = new ClsDate(aamm,"yyyyMM").getMonth();
						if(new ClsDate(aamm,"yyyyMM").getYear() == new ClsDate(salary.infoSalary.getDtes()).getYear())
							nbrMois = 1 + new ClsDate(aamm,"yyyyMM").getMonth() - new ClsDate(salary.infoSalary.getDtes()).getMonth();
						
						montantsMC[i] = nbrMois * nbjrParAn / 12;
					}
					else
					{
						//rubrique du mois en cours, on rajoute les 2.5 jours du mois
						double am = montantsMC[i];
						montantsMC[i] = am + joursacquismois;
						//si on c'est le mois anniv pour le bul 9, on rajoute les jours suppl
						if((new ClsDate(aamm,"yyyyMM").getMonth() == new ClsDate(salary.infoSalary.getDtes()).getMonth()) && salary.getNbul() == 9)
						{
							double joursuppl = salary.calculNombreDJourSuppl();
							am = montantsMC[i];
							montantsMC[i] = am + joursuppl;
						}
					}
				}
			}	
		}
		//si le mois en cours est le 12 et le nbul egal é 9, on decal les jours et on rajoute les jours suppl de l'année
		if(new ClsDate(aamm,"yyyyMM").getMonth() == 12 && salary.getNbul() == 9)
		{
			for(int i=0; i<nbrRubrique;i++)
			{
				if(i<nbrRubrique-1)
				{
					montantsMS[i] = montantsMC[i+1];
				}
				else
					montantsMS[i] = Double.valueOf(0);
				
				if(montantsMC[0] != 0)
				{
					double am = montantsMS[0];
					montantsMS[0]= am + montantsMC[0];
				}
			}
		}
		else
		{
			for(int i=0; i<nbrRubrique;i++)
			{
				montantsMS[i] = montantsMC[i];
			}
		}
		//Génération des lignes dans la table de calcul en supprimant les anciennes
		String rubq="";
		for(int i=0; i<nbrRubrique;i++)
		{
			if(StringUtils.isNotBlank(rubMC[i]))
			{
				if(StringUtils.isBlank(rubq)) 
					rubq="'"+rubMC[i]+"'";
				else 
					rubq+=",'"+rubMC[i]+"'";
			}
		}
		if(StringUtils.isNotBlank(rubq))
		{
			query="delete from CalculPaie where identreprise='"+cdos+"' and nmat = '"+nmat+"' and rubq in ("+rubq+") and aamm = '"+aamm+"'";
			salary.service.deleteFromTable(query);
		}
		// fin suppression des anciennes lignes
		ClsRubriqueClone rubriqueClone = null;
		for(int i=0; i<nbrRubrique;i++)
		{
			if(StringUtils.isNotBlank(rubMC[i]) && montantsMC[i] != 0)
			{
				salary.incrementerDerniereLigne();
				
				CalculPaie oCalculPaie = new CalculPaie();
				oCalculPaie.setAamm(aamm);
				oCalculPaie.setNmat(nmat);
				oCalculPaie.setNbul(salary.getNbul());
				oCalculPaie.setRubq(rubMC[i]);
				oCalculPaie.setIdEntreprise(Integer.valueOf(cdos));
				oCalculPaie.setBasc(new BigDecimal(montantsMC[i]));
				oCalculPaie.setBasp(new BigDecimal(montantsMC[i]));
				oCalculPaie.setTaux(new BigDecimal(0));
				oCalculPaie.setMont(new BigDecimal(montantsMC[i]));
				//
				oCalculPaie.setArgu("");
				oCalculPaie.setClas(salary.infoSalary.getClas());
				oCalculPaie.setNprt("");
				oCalculPaie.setRuba("");
				oCalculPaie.setTrtb("1");
				//
				oCalculPaie.setBasc(ClsStringUtil.truncateToXDecimal(oCalculPaie.getBasc(),3));
				oCalculPaie.setBasp(ClsStringUtil.truncateToXDecimal(oCalculPaie.getBasp(),3));
				oCalculPaie.setTaux(ClsStringUtil.truncateToXDecimal(oCalculPaie.getTaux(),3));
				oCalculPaie.setMont(ClsStringUtil.truncateToXDecimal(oCalculPaie.getMont(),3));
				
				rubriqueClone = salary.parameter.getListOfAllRubriqueMap().get(rubMC[i]);
				if(rubriqueClone != null)
				{
					rubriqueClone.setRates(0);
					rubriqueClone.setAmount(montantsMC[i]);
					rubriqueClone.setBase(montantsMC[i]);
					rubriqueClone.setBasePlafonnee(montantsMC[i]);
					salary.parameter.getListOfAllRubriqueMap().put(rubMC[i], rubriqueClone);
				}
				

				this.getSalary().getService().save(oCalculPaie);
			}
		}
		for(int i=0; i<nbrRubrique;i++)
		{
			if(StringUtils.isNotBlank(rubMS[i]) && montantsMS[i] != 0)
			{
				salary.incrementerDerniereLigne();
				
				CalculPaie oCalculPaie = new CalculPaie();
				oCalculPaie.setAamm(aamm);
				oCalculPaie.setNmat(nmat);
				oCalculPaie.setNbul(salary.getNbul());
				oCalculPaie.setRubq(rubMC[i]);
				oCalculPaie.setIdEntreprise(Integer.valueOf(cdos));
				oCalculPaie.setBasc(new BigDecimal(montantsMS[i]));
				oCalculPaie.setBasp(new BigDecimal(montantsMS[i]));
				oCalculPaie.setTaux(new BigDecimal(0));
				oCalculPaie.setMont(new BigDecimal(montantsMS[i]));
				//
				oCalculPaie.setArgu("");
				oCalculPaie.setClas(salary.infoSalary.getClas());
				oCalculPaie.setNprt("");
				oCalculPaie.setRuba("");
				oCalculPaie.setTrtb("1");
				//
				oCalculPaie.setBasc(ClsStringUtil.truncateToXDecimal(oCalculPaie.getBasc(),3));
				oCalculPaie.setBasp(ClsStringUtil.truncateToXDecimal(oCalculPaie.getBasp(),3));
				oCalculPaie.setTaux(ClsStringUtil.truncateToXDecimal(oCalculPaie.getTaux(),3));
				oCalculPaie.setMont(ClsStringUtil.truncateToXDecimal(oCalculPaie.getMont(),3));

				rubriqueClone = salary.parameter.getListOfAllRubriqueMap().get(rubMS[i]);
				if(rubriqueClone != null)
				{
					rubriqueClone.setRates(0);
					rubriqueClone.setAmount(montantsMS[i]);
					rubriqueClone.setBase(montantsMS[i]);
					rubriqueClone.setBasePlafonnee(montantsMS[i]);
					salary.parameter.getListOfAllRubriqueMap().put(rubMS[i], rubriqueClone);
				}
				
				this.getSalary().getService().save(oCalculPaie);
			}
		}
		
		return true;
	}
	
	public boolean algo110(ClsRubriqueClone rubrique)
	{
		String cdos = salary.parameter.dossier;
		String nmat = salary.infoSalary.getComp_id().getNmat();
		String tabl = rubrique.getRubrique().getTabl();
		String crub = rubrique.getRubrique().getComp_id().getCrub();
		long algo = rubrique.getRubrique().getAlgo();
		
		if(StringUtils.isBlank(tabl))
		{
			salary.getParameter().setError(
					salary.getParameter().errorMessage("Rub. "+crub+" ,Algo."+algo+", Mat. "+nmat+" Table non renseignée ",salary.getParameter().getLangue()));
			return false;
		}
		ParamData fnom =(ParamData) salary.getService().find("FROM ParamData WHERE identreprise="+cdos+" AND ctab="+tabl+" AND cacc='"+crub+" AND nume=1");
		if(fnom == null)
		{
			salary.getParameter().setError(
					salary.getParameter().errorMessage("Rub. "+crub+" ,Algo."+algo+", Mat. "+nmat+" Table "+tabl+" ,Clé "+crub+" inexistant",salary.getParameter().getLangue()));
			return false;
		}
		
		if(StringUtils.isBlank(fnom.getVall()))
		{
			salary.getParameter().setError(
					salary.getParameter().errorMessage("Rub. "+crub+" ,Algo."+algo+", Mat. "+nmat+" Table "+tabl+" ,Clé "+crub+", Renseigner le Lib 1",salary.getParameter().getLangue()));
			return false;
		}
		
		if(fnom.getValm()==null)
		{
			salary.getParameter().setError(
					salary.getParameter().errorMessage("Rub. "+crub+" ,Algo."+algo+", Mat. "+nmat+" Table "+tabl+" ,Clé "+crub+", Renseigner le Mont 1",salary.getParameter().getLangue()));
			return false;
		}
		
		String colonne = fnom.getVall().toLowerCase();
		String methode = "get"+StringUtils.capitalize(colonne);
		Date dt = null;
		try
		{
			dt = (Date) MethodUtils.invokeExactMethod(salary.infoSalary, methode, null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			salary.getParameter().setError(
					salary.getParameter().errorMessage("Rub. "+crub+" ,Algo."+algo+", Mat. "+nmat+" Table "+tabl+" ,Clé "+crub+", Lib 1 : colonne "+colonne+" non existant dans la fiche du salarié",salary.getParameter().getLangue()));
			return false;
		}
		
		salary.getValeurRubriquePartage().setBase(0);
		salary.getValeurRubriquePartage().setBasePlafonnee(0);
		salary.getValeurRubriquePartage().setAmount(0);
		salary.getValeurRubriquePartage().setRates(0);
		
		if(new ClsDate(dt).getMonth() == new ClsDate(salary.getMonthOfPay(),"yyyyMM").getMonth())
		{
			if((new ClsDate(salary.getMonthOfPay(),"yyyyMM").getYear() - new ClsDate(dt).getYear())%fnom.getValm().intValue() == 0)
			{
				salary.getValeurRubriquePartage().setBase(new ClsDate(salary.getMonthOfPay(),"yyyyMM").getYear() - new ClsDate(dt).getYear());
				salary.getValeurRubriquePartage().setBasePlafonnee(new ClsDate(salary.getMonthOfPay(),"yyyyMM").getYear() - new ClsDate(dt).getYear());
				salary.getValeurRubriquePartage().setAmount(1);
				salary.getValeurRubriquePartage().setRates(0);
			}
		}
		
		return true;
	}
	
	public boolean algo111(ClsRubriqueClone rubrique)
	{
		
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo11";
		salary.getParameter().setPbWithCalulation(false);
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "---Directly into algo, salary.getParameter().isPbWithCalulation()=" + salary.getParameter().isPbWithCalulation();

		//w_dec NUMBER;
		double w_dec = 0;
		double val = 0;
		salary.getValeurRubriquePartage().setAmount(0);
		salary.getValeurRubriquePartage().setRates(0);
		double inter = salary.getValeurRubriquePartage().getBase();
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "En entrée, Base = "+inter;
		salary.getValeurRubriquePartage().setInter(inter);
		// spécifique edm

		String cdos = salary.getInfoSalary().getComp_id().getCdos();
		String crub = rubrique.getRubrique().getComp_id().getCrub();

		Object obj = null;
		//Rhthrubbarem oRhthrubbarem = null;
		ElementSalaireBareme oElementSalaireBareme = null;
		String valeur2 = "";
		// -------------------------------------------------------------------------------
		// -- APPLICATION D'UN BAREME ( tranche cumulees ou non )
		// -------------------------------------------------------------------------------
		salary.getValeurRubriquePartage().setAmount(0);
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "On Fixe le montant é 0, montant = "+salary.getValeurRubriquePartage().getAmount();
		int nbreBaremeRubrique = 0;
		double montant = 0;
		double taux = 0;
		String valeur1 = "";
		double dblValeur1 = 0;
		double dblValeur2 = 0;
		// construit la liste des barémes
		// List listOfBarem = salary.getParameter().isUseRetroactif() ? salary.getService().find(queryStringRetro) : salary.getService().find(queryString);
		//
		String keyOfBaremeList = salary.getParameter().isUseRetroactif() ? crub + this.salary.getPeriodOfPay() + this.salary.getNbul() : crub;
		List listOfBarem = (List) salary.getParameter().getListOfRubriquebaremeMap().get(keyOfBaremeList);
		
		if (listOfBarem != null)
		{
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "Lecture du baréme de taille "+listOfBarem.size()+" avec valeur de comparaison ="+salary.getValeurRubriquePartage().getInter();
			int i = 0,j = 0;
			double Ti = 0;
			double Tj = 0;
			double M1 = 0;
			double M2 = 0;
			double Som1 = 0;
			double Som2 = 0;
			double Base = 0;
			for (Object barem : listOfBarem)
			{
						

				oElementSalaireBareme = (ElementSalaireBareme) barem;

				if (oElementSalaireBareme.getTaux() != null)
				{

					taux = oElementSalaireBareme.getTaux().doubleValue();
				}
				valeur1 = oElementSalaireBareme.getVal1();
				valeur2 = oElementSalaireBareme.getVal2();
				
				// incrémente le nombre de rubrique baréme
				nbreBaremeRubrique++;
				
				M2 = new Double (valeur2);
				M1 = new Double (valeur1);
				Ti = taux;
				Base = salary.getValeurRubriquePartage().getBase();
				
				if (nbreBaremeRubrique == 1)
				{
					if(Base <= (M2-M1) * Ti/100)
					{
						salary.getValeurRubriquePartage().setRates(Ti);
						if(Ti != 0)
						 salary.getValeurRubriquePartage().setAmount(Base * 100/ Ti);
						else salary.getValeurRubriquePartage().setAmount(0);
						return true;
						 
					}
				}
				else {
					

					
					if(Base <= (((M2 - M1 )* Ti/100)+ Som1))
					{
						salary.getValeurRubriquePartage().setRates(Ti);
						if(Ti != 0)
						 salary.getValeurRubriquePartage().setAmount(((Base-Som1)*100 + M1*Ti)/ Ti);
						else salary.getValeurRubriquePartage().setAmount(0);
						return true;						
					}
					
					Tj = Ti;

					//Mj = Mi;
					
				}
				Som1 = Som1 + (M2-M1) * Ti/100;
//				Som2 = Som2 + M1 * (Ti - Tj)/100;
			}
	   }
		if (nbreBaremeRubrique == 0)
		{
			// logger
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90061", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), salary.getInfoSalary().getComp_id().getNmat(),
							rubrique.getRubrique().getComp_id().getCrub()));
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "------------------>nbreBaremeRubrique is equal to 0, return false";
			return false;
		}

		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "------------------>no pb, return true, end algo";
		return true;		
		
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo11(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo11(ClsRubriqueClone rubrique)
	{
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo11";
		salary.getParameter().setPbWithCalulation(false);
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "---Directly into algo, salary.getParameter().isPbWithCalulation()=" + salary.getParameter().isPbWithCalulation();

		// w_dec NUMBER;
		double w_dec = 0;
		double val = 0;
		salary.getValeurRubriquePartage().setAmount(0);
		salary.getValeurRubriquePartage().setRates(0);
		double inter = salary.getValeurRubriquePartage().getBase();
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "En entrée, Base = "+inter;
		salary.getValeurRubriquePartage().setInter(inter);
		// spécifique edm
//		if(StringUtil.notIn(rubrique.getSalary().getParameter().nomClient, ClsEntreprise.EDM+","+ClsEntreprise.TASIAST_MAURITANIE+","+ClsEntreprise.SHELL_GABON+","+ClsEntreprise.COMILOG))
//		{
//			if (inter <= 0)
//			{
//				if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "------------------>Inter <= 0, return true";
//				return true;
//			}
//		}
		String cdos = salary.getInfoSalary().getComp_id().getCdos();
		String crub = rubrique.getRubrique().getComp_id().getCrub();
		// if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "Rubrique courante = "+crub);
		//
		// -- Declaration d' un curseur sur la table des baremes
		// CURSOR curs_barem6 IS
		// SELECT cdos , crub , nume , val1, val2, NVL(taux, 0), NVL(mont, 0)
		// FROM parubbarem
		// WHERE cdos = PA_CALCUL.wpdos.cdos
		// AND crub = PA_CALCUL.t_rub.crub
		// ORDER BY cdos , crub , nume;
		// String queryString = "from ElementSalaireBareme "
		// + " where identreprise='" + cdos + "'"
		// + " and crub = '" + crub + "'"
		// + " order by cdos , crub , nume";
		//
		// CURSOR curs_barem62 IS
		// SELECT cdos , crub , nume , val1, val2, NVL(taux, 0), NVL(mont, 0)
		// FROM pahrubbarem
		// WHERE cdos = PA_CALCUL.wpdos.cdos
		// AND crub = PA_CALCUL.t_rub.crub
		// AND aamm = PA_CALCUL.w_aamm
		// AND nbul = PA_CALCUL.wsd_fcal1.nbul
		// ORDER BY cdos , crub , nume;
		// String queryStringRetro = "from Rhthrubbarem "
		// + " where identreprise='" + cdos + "'"
		// + " and crub = '" + crub + "'"
		// + " and aamm = '" + this.salary.getPeriodOfPay() + "'"
		// + " and nbul = " + this.salary.getNbul()
		// + " order by cdos , crub , nume";
		// ------------------------------------------------------------------------------
		// -- Abattement
		// -------------------------------------------------------------------------------
		if (rubrique.getRubrique()!=null && rubrique.getRubrique().getPcab()!=null)
		{
			if('O' == salary.getParameter().getGenfile()) outputtext += "\nRubrique.pcab is not null";
			val = salary.getValeurRubriquePartage().getBase() * rubrique.getRubrique().getPcab().doubleValue() / 100;
			if('O' == salary.getParameter().getGenfile()) outputtext += "\nValeurRubriquePartage.abattement (salary.getValeurRubriquePartage().getBase() * rubrique.getRubrique().getPcab().doubleValue() / 100) = "+val;
			salary.getValeurRubriquePartage().setAbattement(val);
			w_dec = rubrique.convertToNumber(rubrique.getRubrique().getAbmx());
			if (salary.getParameter().isPbWithCalulation())
			{
				if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "------------------>pb with calculation , converting abmx to number, return false; value is " + rubrique.getRubrique().getAbmx() + " and value must be " + val;
				return false;
			}
			if('O' == salary.getParameter().getGenfile()) outputtext += "\nRubrique.abmx = "+w_dec;
			if (w_dec > 0 && salary.getValeurRubriquePartage().getAbattement() > w_dec)
			{
				if('O' == salary.getParameter().getGenfile()) outputtext += "\nRubrique.abmx>ValeurRubriquePartage.abattement = "+w_dec;
				salary.getValeurRubriquePartage().setAbattement(w_dec);
			}
			
			val = salary.getValeurRubriquePartage().getBase() - salary.getValeurRubriquePartage().getAbattement();
			if('O' == salary.getParameter().getGenfile()) outputtext += "\nValeurRubriquePartage.inter (salary.getValeurRubriquePartage().getBase() - salary.getValeurRubriquePartage().getAbattement()) = "+val;
			salary.getValeurRubriquePartage().setInter(val);
		}
		else
		{
			if('O' == salary.getParameter().getGenfile()) outputtext += "\nRubrique.pcab is null, finding Rubrique.abat";
			w_dec = rubrique.convertToNumber(rubrique.getRubrique().getAbat());
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "Convertion du montant de l'abattement => "+w_dec;
			if (salary.getParameter().isPbWithCalulation())
			{
				if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "------------------>pb with calculation , converting abat to number, return false";
				return false;
			}
			if (w_dec > 0)
			{
				val = salary.getValeurRubriquePartage().getInter() - w_dec;
				if('O' == salary.getParameter().getGenfile()) outputtext += "\nValeurRubriquePartage.inter(salary.getValeurRubriquePartage().getInter() - w_dec) = "+val;
				salary.getValeurRubriquePartage().setInter(val);
			}
		}
		inter = salary.getValeurRubriquePartage().getInter();
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "Au Finish ,inter = "+inter;
		if (inter < 0)
		{
			salary.getValeurRubriquePartage().setAmount(0);
			salary.getValeurRubriquePartage().setBase(0);
			salary.getValeurRubriquePartage().setRates(0);
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "------------------>inter <0 , return true";
			return true;
		}
		// -------------------------------------------------------------------------------
		// -- Division par nombre de parts
		// -------------------------------------------------------------------------------
		if ("O".equals(rubrique.getRubrique().getDnbp()))
		{
			if('O' == salary.getParameter().getGenfile()) outputtext += "\nRubrique.dnbp = O";
			if('O' == salary.getParameter().getGenfile()) outputtext += "\nNombre Part = "+salary.getInfoSalary().getNbpt();
			if (!ClsObjectUtil.isNull(salary.getInfoSalary().getNbpt()))
			{
				val = salary.getValeurRubriquePartage().getInter() / salary.getInfoSalary().getNbpt().doubleValue();
				if('O' == salary.getParameter().getGenfile()) outputtext += "\nValeurRubriquePartage.inter (salary.getValeurRubriquePartage().getInter() / salary.getInfoSalary().getNbpt().doubleValue()) = "+val;
				salary.getValeurRubriquePartage().setInter(val);
			}
			else
			{
				// logger
				salary.getParameter().setError(
						salary.getParameter().errorMessage("ERR-90059", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), salary.getInfoSalary().getComp_id().getNmat(),
								rubrique.getRubrique().getComp_id().getCrub()));
				if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "------------------>salary nbpt  is null , return false";
				return false;
			}
		}
		//
		// -------------------------------------------------------------------------------
		// -- Base plafonnee
		// -------------------------------------------------------------------------------
		// -- Si la base est plafonnee, le plafond est la seconde valeur de la premiere
		// -- tranche dans la table des baremes (ecran 5)
		// -------------------------------------------------------------------------------
		Object obj = null;
		//Rhthrubbarem oRhthrubbarem = null;
		ElementSalaireBareme oElementSalaireBareme = null;
		String valeur2 = "";
		if ("O".equals(rubrique.getRubrique().getBasp()))
		{
			if('O' == salary.getParameter().getGenfile()) outputtext += "\nRubrique.basp = O";
			if (salary.getParameter().isUseRetroactif())
			{
//				int nbul = salary.getNbul();
//				RhthrubbaremPK pk = null;// new RhthrubbaremPK(cdos, rubrique.getRubrique().getComp_id().getCrub(), 1, salary.getPeriodOfPay(), nbul);
//
//				String _strQueryForFirstElement = "From Rhthrubbarem where identreprise='" + cdos + "' and crub='" + rubrique.getRubrique().getComp_id().getCrub() + "' and aamm ='"
//						+ salary.getPeriodOfPay() + "' and nbul=" + nbul + " order by nume";
//
//				List<Rhthrubbarem> _l_Result = salary.getService().find(_strQueryForFirstElement);
//				if (_l_Result.size() > 0)
//					obj = _l_Result.get(0);
//
//				// obj = salary.getService().get(Rhthrubbarem.class, pk);
//				if (obj != null)
//				{
//					oRhthrubbarem = (Rhthrubbarem) obj;
//					valeur2 = oRhthrubbarem.getVal2();
//					// ajout yannick
//					// this.salary.getValeurRubriquePartage().setRates(oRhthrubbarem.getTaux().doubleValue());
//				}
			}
			else
			{
				if('O' == salary.getParameter().getGenfile()) outputtext += "\nRecherche de la premiére ligne de bare";
				ElementSalaireBareme pk1 = null;

				String _strQueryForFirstElement = "From ElementSalaireBareme where identreprise='" + cdos + "' and crub='" + rubrique.getRubrique().getComp_id().getCrub()
						+ "' order by nume";

				List<ElementSalaireBareme> _l_Result = salary.getService().find(_strQueryForFirstElement);
				if (_l_Result.size() > 0)
					obj = _l_Result.get(0);

				//if (_l_Result.size() > 0)
				    pk1 = (ElementSalaireBareme)_l_Result.get(0);
				// new ElementSalaireBaremePK(cdos, rubrique.getRubrique().getComp_id().getCrub(), 1);
				// obj = salary.getService().get(ElementSalaireBareme.class, pk1);
				if (obj != null)
				{
					oElementSalaireBareme = (ElementSalaireBareme) obj;
					//valeur2 = oElementSalaireBareme.getVal2();
					valeur2 = rubrique.getValeurBareme(oElementSalaireBareme, false);
					if('O' == salary.getParameter().getGenfile()) outputtext += "\nBorne sup (valeur2) = "+valeur2;
					// ajout yannick
					// this.salary.getValeurRubriquePartage().setRates(oElementSalaireBareme.getTaux().doubleValue());
				}
			}
			if (obj == null)
			{
				// logger
				salary.getParameter().setError(
						salary.getParameter().errorMessage("ERR-90060", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), salary.getInfoSalary().getComp_id().getNmat(),
								rubrique.getRubrique().getComp_id().getCrub()));
				if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "------------------>pas de bareme alors que plafonné, return false";
				return false;
			}
			val = rubrique.convertToNumber(valeur2);
			if (salary.getParameter().isPbWithCalulation())
			{
				if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "------------------>pb with calculation , converting valeur2 to number, return false";
				return false;
			}
			if('O' == salary.getParameter().getGenfile()) outputtext += "\nValeurRubriquePartage().setPlafond = "+val;
			salary.getValeurRubriquePartage().setPlafond(val);
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "Taux = " + salary.getValeurRubriquePartage().getRates();
			if (salary.getValeurRubriquePartage().getInter() > salary.getValeurRubriquePartage().getPlafond())
			{
				if('O' == salary.getParameter().getGenfile()) outputtext += "\nsalary.getValeurRubriquePartage().getInter() > salary.getValeurRubriquePartage().getPlafond() ";
				salary.getValeurRubriquePartage().setBasePlafonnee(val);
				salary.getValeurRubriquePartage().setInter(val);
				if('O' == salary.getParameter().getGenfile()) outputtext += "\nValeurRubriquePartage.inter = ValeurRubriquePartage.basePlafonnee = "+val;
			}
		}
		// -------------------------------------------------------------------------------
		// -- APPLICATION D'UN BAREME ( tranche cumulees ou non )
		// -------------------------------------------------------------------------------
		salary.getValeurRubriquePartage().setAmount(0);
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "On Fixe le montant é 0, montant = "+salary.getValeurRubriquePartage().getAmount();
		int nbreBaremeRubrique = 0;
		double montant = 0;
		double taux = 0;
		String valeur1 = "";
		double dblValeur1 = 0;
		double dblValeur2 = 0;
		// construit la liste des barémes
		// List listOfBarem = salary.getParameter().isUseRetroactif() ? salary.getService().find(queryStringRetro) : salary.getService().find(queryString);
		//
		String keyOfBaremeList = salary.getParameter().isUseRetroactif() ? crub + this.salary.getPeriodOfPay() + this.salary.getNbul() : crub;
		List listOfBarem = (List) salary.getParameter().getListOfRubriquebaremeMap().get(keyOfBaremeList);
		
		if (listOfBarem != null)
		{
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "Lecture du baréme de taille "+listOfBarem.size()+" avec valeur de comparaison ="+salary.getValeurRubriquePartage().getInter();
			for (Object barem : listOfBarem)
			{
				if (salary.getParameter().isUseRetroactif())
				{
//					oRhthrubbarem = (Rhthrubbarem) barem;
//					if (oRhthrubbarem.getMont() != null)
//						montant = oRhthrubbarem.getMont().doubleValue();
//					if (oRhthrubbarem.getTaux() != null)
//						taux = oRhthrubbarem.getTaux().doubleValue();
//					valeur1 = oRhthrubbarem.getVal1();
//					valeur2 = oRhthrubbarem.getVal2();
				}
				else
				{
					oElementSalaireBareme = (ElementSalaireBareme) barem;
//					if (oElementSalaireBareme.getMont() != null)
//						montant = oElementSalaireBareme.getMont().doubleValue();
//					if (oElementSalaireBareme.getTaux() != null)
//					{
//						taux = oElementSalaireBareme.getTaux().doubleValue();
//					}
					montant = rubrique.getTauxOuMontantBareme(oElementSalaireBareme, false);
					taux = rubrique.getTauxOuMontantBareme(oElementSalaireBareme, true);
					//valeur1 = oElementSalaireBareme.getVal1();
					valeur1 = rubrique.getValeurBareme(oElementSalaireBareme, true);
					//valeur2 = oElementSalaireBareme.getVal2();
					valeur2 = rubrique.getValeurBareme(oElementSalaireBareme, false);
				}
				// incrémente le nombre de rubrique baréme
				nbreBaremeRubrique++;
//				if (nbreBaremeRubrique > 50)
//				{
//					nbreBaremeRubrique = 50;
//					break;
//				}
				//
				dblValeur1 = rubrique.convertToNumber(valeur1);
				if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "Borne inf converti = "+dblValeur1;
				if (salary.getParameter().isPbWithCalulation())
				{
					if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "------------------>pb with calculation , converting valeur1,who is bareme val1, to number, return false";
					return false;
				}
				dblValeur2 = rubrique.convertToNumber(valeur2);
				if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "Borne sup converti = "+dblValeur2;
				if (salary.getParameter().isPbWithCalulation())
				{
					if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "------------------>pb with calculation , converting valeur2,who is bareme val1, to number, return false";
					return false;
				}
				val = 0;
				if (salary.getValeurRubriquePartage().getInter() >= dblValeur1)
				{
					if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "Intervalle trouvé = "+dblValeur1+" é "+dblValeur2;
					val = salary.getValeurRubriquePartage().getInter() <= dblValeur2 ? salary.getValeurRubriquePartage().getInter() - dblValeur1 : dblValeur2 - dblValeur1;
					if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "ValeurRubriquePartage.basetaux = "+val;
					salary.getValeurRubriquePartage().setBasetaux(val);
					
					if ("N".equals(rubrique.getRubrique().getTrcu()))
					{
						if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "Rubrique.trcu =N donc ValeurRubriquePartage.basetaux = inter = "+salary.getValeurRubriquePartage().getInter();
						salary.getValeurRubriquePartage().setBasetaux(salary.getValeurRubriquePartage().getInter());
					}
					val = 0;
					if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "RubriquePartage.txmt = "+rubrique.getRubrique().getTxmt();
					if ("M".equals(rubrique.getRubrique().getTxmt()))
					{
						salary.getValeurRubriquePartage().setValeur(montant);
						salary.getValeurRubriquePartage().setRates(0);
						if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "ValeurRubriquePartage.valeur = "+montant+" et taux = 0";
					}
					else if ("D".equals(rubrique.getRubrique().getTxmt()))
					{
						salary.getValeurRubriquePartage().setRates(taux);
						if(salary.getValeurRubriquePartage().getRates() != 0)
						{
							val = salary.getValeurRubriquePartage().getBasetaux() / salary.getValeurRubriquePartage().getRates();
							salary.getValeurRubriquePartage().setValeur(val);
							if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "ValeurRubriquePartage.valeur (salary.getValeurRubriquePartage().getBasetaux() / salary.getValeurRubriquePartage().getRates()) = "+val+" et taux = "+taux;
						}
					}
					else 
					{
						//("T".equals(rubrique.getRubrique().getTxmt())) on suppose ici que c'est le taux
						salary.getValeurRubriquePartage().setRates(taux);
						val = salary.getValeurRubriquePartage().getBasetaux() * taux / 100;
						salary.getValeurRubriquePartage().setValeur(val);
						if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "ValeurRubriquePartage.valeur (salary.getValeurRubriquePartage().getBasetaux() * taux / 100) = "+val+" et taux = "+taux;
					}
					
					if ("O".equals(rubrique.getRubrique().getTrcu()))
					{
						val = salary.getValeurRubriquePartage().getAmount() + salary.getValeurRubriquePartage().getValeur();
						if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "Rubrique.trcu = O donc ValeurRubriquePartage.montant(salary.getValeurRubriquePartage().getAmount() + salary.getValeurRubriquePartage().getValeur()) = "+val;
						salary.getValeurRubriquePartage().setAmount(val);
					}
					else
					{
						val = salary.getValeurRubriquePartage().getValeur();
						salary.getValeurRubriquePartage().setAmount(val);
						if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "Rubrique.trcu = N donc ValeurRubriquePartage.montant(salary.getValeurRubriquePartage().getValeur()) = "+val;
					}
					if ("O".equals(rubrique.getRubrique().getBasp()))
						break;
				}
				else
					break;
			}
	}
		if (nbreBaremeRubrique == 0)
		{
			// logger
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90061", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), salary.getInfoSalary().getComp_id().getNmat(),
							rubrique.getRubrique().getComp_id().getCrub()));
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "------------------>nbreBaremeRubrique is equal to 0, return false";
			return false;
		}
		// -------------------------------------------------------------------------------
		// -- Multiplication par nombre de part
		// -- le calcul se fait sur une part mais la cotisation sur toutes
		// -------------------------------------------------------------------------------
		if ("O".equals(rubrique.getRubrique().getDnbp()) && salary.getInfoSalary().getNbpt() != null)
		{
			val = salary.getValeurRubriquePartage().getAmount() * salary.getInfoSalary().getNbpt().doubleValue();
			salary.getValeurRubriquePartage().setAmount(val);
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "Rubrique.dnbp = O donc ValeurRubriquePartage.montant(salary.getValeurRubriquePartage().getAmount() * salary.getInfoSalary().getNbpt().doubleValue()) = "+val;
		}
		// -------------------------------------------------------------------------------
		// -- Perception mini
		// -------------------------------------------------------------------------------
		if (!ClsObjectUtil.isNull(rubrique.getRubrique().getPmin()))
		{
			val = rubrique.convertToNumber(rubrique.getRubrique().getPmin());
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "Rubrique.pmin = "+val;
			if (salary.getParameter().isPbWithCalulation())
			{
				if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "------------------>pb with calculation , converting rubrique.pmin to number, return false";
				return false;
			}
			salary.getValeurRubriquePartage().setValeur(val);
			if (salary.getValeurRubriquePartage().getAmount() < salary.getValeurRubriquePartage().getValeur() && salary.getValeurRubriquePartage().getValeur() != 0)
			{
				val = salary.getValeurRubriquePartage().getValeur();
				salary.getValeurRubriquePartage().setAmount(val);
			}
		}
		// -------------------------------------------------------------------------------
		// -- Perception maxi
		// -------------------------------------------------------------------------------
		if (!ClsObjectUtil.isNull(rubrique.getRubrique().getPmax()))
		{
			val = rubrique.convertToNumber(rubrique.getRubrique().getPmax());
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "Rubrique.pmax = "+val;
			if (salary.getParameter().isPbWithCalulation())
			{
				if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "------------------>pb with calculation rubrique.pmax to number, return false";
				return false;
			}
			salary.getValeurRubriquePartage().setValeur(val);
			if (salary.getValeurRubriquePartage().getAmount() > salary.getValeurRubriquePartage().getValeur() && salary.getValeurRubriquePartage().getValeur() != 0)
			{
				val = salary.getValeurRubriquePartage().getValeur();
				salary.getValeurRubriquePartage().setAmount(val);
			}
		}

		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "------------------>no pb, return true, end algo";
		return true;
	}
	
	public boolean algo7(ClsRubriqueClone rubrique)
	{
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo11";
		salary.getParameter().setPbWithCalulation(false);
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "---Directly into algo, salary.getParameter().isPbWithCalulation()=" + salary.getParameter().isPbWithCalulation();

		// w_dec NUMBER;
		double w_dec = 0;
		double val = 0;
		salary.getValeurRubriquePartage().setAmount(0);
		salary.getValeurRubriquePartage().setRates(0);
		double inter = salary.getValeurRubriquePartage().getBase();
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "En entrée, Base = "+inter;
		salary.getValeurRubriquePartage().setInter(inter);
		
		if (inter <= 0)
		{
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "------------------>Inter <= 0, return true";
			return true;
		}
		
		String cdos = salary.getInfoSalary().getComp_id().getCdos();
		String crub = rubrique.getRubrique().getComp_id().getCrub();
		String nmat = salary.infoSalary.getComp_id().getNmat();
		Integer table = Integer.valueOf(rubrique.getRubrique().getTabl());
		String annee=new ClsDate(salary.parameter.getMonthOfPay(),"yyyyMM").getYear()+"";
		//Abattement pour autres charges comme la CNSS ou autres cotisations
		//En table paramétré sur la rubrique, clé : CUM-numéro de la rubrique, lib1, on met la formule des rubriques d'abbattement
		val = salary.getValeurRubriquePartage().getBase();
		String cle="COT"+rubrique.getRubrique().getComp_id().getCrub();
		String formule = this.getLabelFromNomenclature(cdos, table, cle, 1, salary.getNlot(), salary.getNbul(), salary.getMoisPaieCourant(), salary.getPeriodOfPay(), rubrique.getRubrique().getComp_id().getCrub());
		if (!ClsObjectUtil.isNull(formule))
		{
			String w_rubq;
			double w_mont = 0;
			String formuleRubriques = null;
			ClsRubriqueClone rubTemp;
			try
			{
				for (int i = 1; i <= 30; i++)
				{
					if (i % 5 == 1)
					{
						w_rubq = StringUtil.oraSubstring(formule, i, 4);
						if (StringUtils.isBlank(StringUtil.oraLTrim(w_rubq)))
							break;
						if(StringUtils.isBlank(formuleRubriques)) formuleRubriques="'"+w_rubq+"'";
						else formuleRubriques +=",'"+w_rubq+"'";
					}
				}
				if(StringUtils.isNotBlank(formuleRubriques))
				{
					String query="SELECT sum(mont)  FROM CumulPaie WHERE identreprise='"+cdos+"' and nmat = '"+nmat+"' ";
					query+=" and aamm  like '"+annee+"%' and aamm not like '%99' and rubq in ("+formuleRubriques+") and nbul >0";
					w_mont = 0;
					List lst = salary.getService().find(query);
					if(! lst.isEmpty() && lst.get(0) != null)
						w_mont = ClsObjectUtil.getBigDecimalFromObject(lst.get(0)).doubleValue();
					
					val = val - w_mont;
				}
				if(StringUtils.isNotBlank(formuleRubriques))
				{
					String query="SELECT sum(mont)  FROM CalculPaie WHERE identreprise='"+cdos+"' and nmat = '"+nmat+"' ";
					query+=" and aamm  = '"+salary.parameter.getMonthOfPay()+"' and rubq in ("+formuleRubriques+") and nbul >0";
					w_mont = 0;
					List lst = salary.getService().find(query);
					if(! lst.isEmpty() && lst.get(0) != null)
						w_mont = ClsObjectUtil.getBigDecimalFromObject(lst.get(0)).doubleValue();
					
					val = val - w_mont;
				}
			}
			catch(Exception e)
			{
				salary.getParameter().setError(salary.getParameter().errorMessage(e.getMessage(), salary.getParameter().getLangue()));
				return false;
			}
		}
		salary.getValeurRubriquePartage().setBase(val);
		
		
		// ------------------------------------------------------------------------------
		// -- Abattement
		// -------------------------------------------------------------------------------
		if (!ClsObjectUtil.isNull(rubrique.getRubrique().getPcab()))
		{
			if('O' == salary.getParameter().getGenfile()) outputtext += "\nRubrique.pcab is not null";
			val = salary.getValeurRubriquePartage().getBase() * rubrique.getRubrique().getPcab().doubleValue() / 100;
			if('O' == salary.getParameter().getGenfile()) outputtext += "\nValeurRubriquePartage.abattement (salary.getValeurRubriquePartage().getBase() * rubrique.getRubrique().getPcab().doubleValue() / 100) = "+val;
			salary.getValeurRubriquePartage().setAbattement(val);
			w_dec = rubrique.convertToNumber(rubrique.getRubrique().getAbmx());
			if (salary.getParameter().isPbWithCalulation())
			{
				if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "------------------>pb with calculation , converting abmx to number, return false; value is " + rubrique.getRubrique().getAbmx() + " and value must be " + val;
				return false;
			}
			if('O' == salary.getParameter().getGenfile()) outputtext += "\nRubrique.abmx = "+w_dec;
			if (w_dec > 0 && salary.getValeurRubriquePartage().getAbattement() > w_dec)
			{
				if('O' == salary.getParameter().getGenfile()) outputtext += "\nRubrique.abmx>ValeurRubriquePartage.abattement = "+w_dec;
				salary.getValeurRubriquePartage().setAbattement(w_dec);
			}
			
			val = salary.getValeurRubriquePartage().getBase() - salary.getValeurRubriquePartage().getAbattement();
			if('O' == salary.getParameter().getGenfile()) outputtext += "\nValeurRubriquePartage.inter (salary.getValeurRubriquePartage().getBase() - salary.getValeurRubriquePartage().getAbattement()) = "+val;
			salary.getValeurRubriquePartage().setInter(val);
			
		}
		else
		{
			if('O' == salary.getParameter().getGenfile()) outputtext += "\nRubrique.pcab is null, finding Rubrique.abat";
			w_dec = rubrique.convertToNumber(rubrique.getRubrique().getAbat());
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "Convertion du montant de l'abattement => "+w_dec;
			if (salary.getParameter().isPbWithCalulation())
			{
				if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "------------------>pb with calculation , converting abat to number, return false";
				return false;
			}
			if (w_dec > 0)
			{
				val = salary.getValeurRubriquePartage().getInter() - w_dec;
				if('O' == salary.getParameter().getGenfile()) outputtext += "\nValeurRubriquePartage.inter(salary.getValeurRubriquePartage().getInter() - w_dec) = "+val;
				salary.getValeurRubriquePartage().setInter(val);
			}
		}
		
		//Deuxiéme abattement 15%/5% suivant tranches
		List<ParamData> baremes = salary.getService().find("From ParamData where identreprise='"+cdos+"' and ctab = "+table+" and nume=1  and cacc like 'B%' order by valm ");
		double min;
		double max;
		double tx;
		double val15 = 0;
		double mntprec = 0;
		for(ParamData nome : baremes)
		{
			min = nome.getValm() != null? nome.getValm().doubleValue() : 0;
			max = StringUtils.isNotBlank(nome.getVall()) ? new BigDecimal(nome.getVall()).doubleValue():0;
			tx = nome.getValt() != null? nome.getValt().doubleValue() : 0;
			if ("N".equals(rubrique.getRubrique().getTrcu())) mntprec = 0;
			if(val<=max)
			{
				val15 = (val-min)*tx/100 + mntprec;
				break;
			}
			else
				mntprec = mntprec + (max-min)*tx/100;
		}
		
		salary.getValeurRubriquePartage().setInter(val15);
		
		
		inter = salary.getValeurRubriquePartage().getInter();
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "Au Finish ,inter = "+inter;
		if (inter < 0)
		{
			salary.getValeurRubriquePartage().setAmount(0);
			salary.getValeurRubriquePartage().setBase(0);
			salary.getValeurRubriquePartage().setRates(0);
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "------------------>inter <0 , return true";
			return true;
		}
		// -------------------------------------------------------------------------------
		// -- Division par nombre de parts : ici,il s'agit de faire l'abattement suivant la situation familiale
		// -------------------------------------------------------------------------------
		if ("O".equals(rubrique.getRubrique().getDnbp()))
		{
			if('O' == salary.getParameter().getGenfile()) outputtext += "\nRubrique.dnbp = O";
			if('O' == salary.getParameter().getGenfile()) outputtext += "\nNombre Part = "+salary.getInfoSalary().getNbpt();
			if (salary.getInfoSalary().getNbpt() != null)
			{
				
				int cle1 = ClsObjectUtil.isNull(rubrique.getRubrique().getCle1()) ? 0 : Integer.valueOf(rubrique.getRubrique().getCle1());
				int cle2 = ClsObjectUtil.isNull(rubrique.getRubrique().getCle2()) ? 0 : Integer.valueOf(rubrique.getRubrique().getCle2());
				String a_cacc = concatene(cle1, cle2, rubrique.getRubrique().getComp_id().getCrub());
				
				// -- Lecture de la valeur dans les nomenclatures
				ClsEnumeration.EnColumnToRead col = "M".equals(rubrique.getRubrique().getToum()) ? ClsEnumeration.EnColumnToRead.AMOUNT : ClsEnumeration.EnColumnToRead.RATES;
				tempNumber = null;
				if (rubrique.getRubrique().getTabl() != null)
				{
					tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,cdos, Integer.valueOf(rubrique.getRubrique().getTabl()), a_cacc, rubrique.getRubrique().getNutm(), salary.getNlot(),
							salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), col, rubrique.getRubrique().getComp_id().getCrub());
					if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "----------------------------->Montant de la table " + rubrique.getRubrique().getTabl() + " = " + tempNumber;
				}
				
				if (tempNumber == null)
				{
					// logger
					salary.getParameter().setError(
							salary.getParameter().errorMessage("ERR-90056", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
									salary.getInfoSalary().getComp_id().getNmat(), a_cacc, rubrique.getRubrique().getTabl()));
					salary.getParameter().setPbWithCalulation(true);
					return false;
				}
				
				double abattementCharges = tempNumber.doubleValue();

				val = salary.getValeurRubriquePartage().getInter() - abattementCharges;
				if('O' == salary.getParameter().getGenfile()) outputtext += "\nValeurRubriquePartage.inter (salary.getValeurRubriquePartage().getInter() / salary.getInfoSalary().getNbpt().doubleValue()) = "+val;
				salary.getValeurRubriquePartage().setInter(val);
			}
			else
			{
				// logger
				salary.getParameter().setError(
						salary.getParameter().errorMessage("ERR-90059", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), salary.getInfoSalary().getComp_id().getNmat(),
								rubrique.getRubrique().getComp_id().getCrub()));
				if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "------------------>salary nbpt  is null , return false";
				return false;
			}
		}
		
		val = salary.getValeurRubriquePartage().getInter();
		//Abattement pour autres charges forfaitaires
		//En table paramétré sur la rubrique, clé : numéro de la rubrique, lib1, on met la formule des rubriques d'abbattement
		formule = this.getLabelFromNomenclature(cdos, table, rubrique.getRubrique().getComp_id().getCrub(), 1, salary.getNlot(), salary.getNbul(), salary.getMoisPaieCourant(), salary.getPeriodOfPay(), rubrique.getRubrique().getComp_id().getCrub());
		if (!ClsObjectUtil.isNull(formule))
		{
			String w_rubq;
			double w_mont = 0;
			ClsRubriqueClone rubTemp;
			try
			{
				for (int i = 1; i <= 30; i++)
				{
					if (i % 5 == 1)
					{
						w_rubq = StringUtil.oraSubstring(formule, i, 4);
						if (StringUtils.isBlank(StringUtil.oraLTrim(w_rubq)))
							break;
						
						rubTemp = salary.findRubriqueClone(w_rubq);
						w_mont = 0;
						if(rubTemp != null) w_mont = rubTemp.getAmount();
						val = val - w_mont;
					}
				}
			}
			catch(Exception e)
			{
				salary.getParameter().setError(salary.getParameter().errorMessage(e.getMessage(), salary.getParameter().getLangue()));
				return false;
			}
		}
		salary.getValeurRubriquePartage().setInter(val);
		
		
		
		
		val = arrondi2('I', 1000, salary.getValeurRubriquePartage().getInter());
		salary.getValeurRubriquePartage().setInter(val);
		//
		// -------------------------------------------------------------------------------
		// -- Base plafonnee
		// -------------------------------------------------------------------------------
		// -- Si la base est plafonnee, le plafond est la seconde valeur de la premiere
		// -- tranche dans la table des baremes (ecran 5)
		// -------------------------------------------------------------------------------
		Object obj = null;
		//Rhthrubbarem oRhthrubbarem = null;
		ElementSalaireBareme oElementSalaireBareme = null;
		String valeur2 = "";
		if ("O".equals(rubrique.getRubrique().getBasp()))
		{
			if('O' == salary.getParameter().getGenfile()) outputtext += "\nRubrique.basp = O";
			if (salary.getParameter().isUseRetroactif())
			{
//				int nbul = salary.getNbul();
//				RhthrubbaremPK pk = null;// new RhthrubbaremPK(cdos, rubrique.getRubrique().getComp_id().getCrub(), 1, salary.getPeriodOfPay(), nbul);
//
//				String _strQueryForFirstElement = "From Rhthrubbarem where identreprise='" + cdos + "' and crub='" + rubrique.getRubrique().getComp_id().getCrub() + "' and aamm ='"
//						+ salary.getPeriodOfPay() + "' and nbul=" + nbul + " order by nume";
//
//				List<Rhthrubbarem> _l_Result = salary.getService().find(_strQueryForFirstElement);
//				if (_l_Result.size() > 0)
//					obj = _l_Result.get(0);
//
//				if (obj != null)
//				{
//					oRhthrubbarem = (Rhthrubbarem) obj;
//					valeur2 = oRhthrubbarem.getVal2();
//				}
			}
			else
			{
				if('O' == salary.getParameter().getGenfile()) outputtext += "\nRecherche de la premiére ligne de bare";
				ElementSalaireBareme pk1 = null;

				String _strQueryForFirstElement = "From ElementSalaireBareme where identreprise='" + cdos + "' and crub='" + rubrique.getRubrique().getComp_id().getCrub()
						+ "' order by nume";

				List<ElementSalaireBareme> _l_Result = salary.getService().find(_strQueryForFirstElement);
				if (_l_Result.size() > 0)
					obj = _l_Result.get(0);

				pk1 = _l_Result.get(0);
				if (obj != null)
				{
					oElementSalaireBareme = (ElementSalaireBareme) obj;
					//valeur2 = oElementSalaireBareme.getVal2();
					valeur2 = rubrique.getValeurBareme(oElementSalaireBareme, false);
					if('O' == salary.getParameter().getGenfile()) outputtext += "\nBorne sup (valeur2) = "+valeur2;
				}
			}
			if (obj == null)
			{
				// logger
				salary.getParameter().setError(
						salary.getParameter().errorMessage("ERR-90060", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), salary.getInfoSalary().getComp_id().getNmat(),
								rubrique.getRubrique().getComp_id().getCrub()));
				if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "------------------>pas de bareme alors que plafonné, return false";
				return false;
			}
			val = rubrique.convertToNumber(valeur2);
			if (salary.getParameter().isPbWithCalulation())
			{
				if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "------------------>pb with calculation , converting valeur2 to number, return false";
				return false;
			}
			if('O' == salary.getParameter().getGenfile()) outputtext += "\nValeurRubriquePartage().setPlafond = "+val;
			salary.getValeurRubriquePartage().setPlafond(val);
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "Taux = " + salary.getValeurRubriquePartage().getRates();
			if (salary.getValeurRubriquePartage().getInter() > salary.getValeurRubriquePartage().getPlafond())
			{
				if('O' == salary.getParameter().getGenfile()) outputtext += "\nsalary.getValeurRubriquePartage().getInter() > salary.getValeurRubriquePartage().getPlafond() ";
				salary.getValeurRubriquePartage().setBasePlafonnee(val);
				salary.getValeurRubriquePartage().setInter(val);
				if('O' == salary.getParameter().getGenfile()) outputtext += "\nValeurRubriquePartage.inter = ValeurRubriquePartage.basePlafonnee = "+val;
			}
		}
		// -------------------------------------------------------------------------------
		// -- APPLICATION D'UN BAREME ( tranche cumulees ou non )
		// -------------------------------------------------------------------------------
		salary.getValeurRubriquePartage().setAmount(0);
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "On Fixe le montant é 0, montant = "+salary.getValeurRubriquePartage().getAmount();
		int nbreBaremeRubrique = 0;
		double montant = 0;
		double taux = 0;
		String valeur1 = "";
		double dblValeur1 = 0;
		double dblValeur2 = 0;
		// construit la liste des barémes
		//
		String keyOfBaremeList = salary.getParameter().isUseRetroactif() ? crub + this.salary.getPeriodOfPay() + this.salary.getNbul() : crub;
		List listOfBarem = (List) salary.getParameter().getListOfRubriquebaremeMap().get(keyOfBaremeList);
		
		if (listOfBarem != null)
		{
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "Lecture du baréme de taille "+listOfBarem.size()+" avec valeur de comparaison ="+salary.getValeurRubriquePartage().getInter();
			for (Object barem : listOfBarem)
			{
				if (salary.getParameter().isUseRetroactif())
				{
//					oRhthrubbarem = (Rhthrubbarem) barem;
//					if (oRhthrubbarem.getMont() != null)
//						montant = oRhthrubbarem.getMont().doubleValue();
//					if (oRhthrubbarem.getTaux() != null)
//						taux = oRhthrubbarem.getTaux().doubleValue();
//					valeur1 = oRhthrubbarem.getVal1();
//					valeur2 = oRhthrubbarem.getVal2();
				}
				else
				{
					oElementSalaireBareme = (ElementSalaireBareme) barem;
//					if (oElementSalaireBareme.getMont() != null)
//						montant = oElementSalaireBareme.getMont().doubleValue();
//					if (oElementSalaireBareme.getTaux() != null)
//					{
//
//						taux = oElementSalaireBareme.getTaux().doubleValue();
//					}
//					valeur1 = oElementSalaireBareme.getVal1();
//					valeur2 = oElementSalaireBareme.getVal2();
					montant = rubrique.getTauxOuMontantBareme(oElementSalaireBareme, false);
					taux = rubrique.getTauxOuMontantBareme(oElementSalaireBareme, true);
					valeur1 = rubrique.getValeurBareme(oElementSalaireBareme, true);
					valeur2 = rubrique.getValeurBareme(oElementSalaireBareme, false);
				}
				// incrémente le nombre de rubrique baréme
				nbreBaremeRubrique++;

				dblValeur1 = rubrique.convertToNumber(valeur1);
				if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "Borne inf converti = "+dblValeur1;
				if (salary.getParameter().isPbWithCalulation())
				{
					if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "------------------>pb with calculation , converting valeur1,who is bareme val1, to number, return false";
					return false;
				}
				dblValeur2 = rubrique.convertToNumber(valeur2);
				if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "Borne sup converti = "+dblValeur2;
				if (salary.getParameter().isPbWithCalulation())
				{
					if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "------------------>pb with calculation , converting valeur2,who is bareme val1, to number, return false";
					return false;
				}
				val = 0;
				if (salary.getValeurRubriquePartage().getInter() >= dblValeur1)
				{
					if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "Intervalle trouvé = "+dblValeur1+" é "+dblValeur2;
					val = salary.getValeurRubriquePartage().getInter() <= dblValeur2 ? salary.getValeurRubriquePartage().getInter() - dblValeur1 : dblValeur2 - dblValeur1;
					if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "ValeurRubriquePartage.basetaux = "+val;
					salary.getValeurRubriquePartage().setBasetaux(val);
					
					if ("N".equals(rubrique.getRubrique().getTrcu()))
					{
						if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "Rubrique.trcu =N donc ValeurRubriquePartage.basetaux = inter = "+salary.getValeurRubriquePartage().getInter();
						salary.getValeurRubriquePartage().setBasetaux(salary.getValeurRubriquePartage().getInter());
					}
					val = 0;
					if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "RubriquePartage.txmt = "+rubrique.getRubrique().getTxmt();
					if ("M".equals(rubrique.getRubrique().getTxmt()))
					{
						salary.getValeurRubriquePartage().setValeur(montant);
						salary.getValeurRubriquePartage().setRates(0);
						if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "ValeurRubriquePartage.valeur = "+montant+" et taux = 0";
					}
					else if ("D".equals(rubrique.getRubrique().getTxmt()))
					{
						salary.getValeurRubriquePartage().setRates(taux);
						if(salary.getValeurRubriquePartage().getRates() != 0)
						{
							val = salary.getValeurRubriquePartage().getBasetaux() / salary.getValeurRubriquePartage().getRates();
							salary.getValeurRubriquePartage().setValeur(val);
							if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "ValeurRubriquePartage.valeur (salary.getValeurRubriquePartage().getBasetaux() / salary.getValeurRubriquePartage().getRates()) = "+val+" et taux = "+taux;
						}
					}
					else 
					{
						//("T".equals(rubrique.getRubrique().getTxmt())) on suppose ici que c'est le taux
						salary.getValeurRubriquePartage().setRates(taux);
						val = salary.getValeurRubriquePartage().getBasetaux() * taux / 100;
						salary.getValeurRubriquePartage().setValeur(val);
						if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "ValeurRubriquePartage.valeur (salary.getValeurRubriquePartage().getBasetaux() * taux / 100) = "+val+" et taux = "+taux;
					}
					
					if ("O".equals(rubrique.getRubrique().getTrcu()))
					{
						val = salary.getValeurRubriquePartage().getAmount() + salary.getValeurRubriquePartage().getValeur();
						if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "Rubrique.trcu = O donc ValeurRubriquePartage.montant(salary.getValeurRubriquePartage().getAmount() + salary.getValeurRubriquePartage().getValeur()) = "+val;
						salary.getValeurRubriquePartage().setAmount(val);
					}
					else
					{
						val = salary.getValeurRubriquePartage().getValeur();
						salary.getValeurRubriquePartage().setAmount(val);
						if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "Rubrique.trcu = N donc ValeurRubriquePartage.montant(salary.getValeurRubriquePartage().getValeur()) = "+val;
					}
					if ("O".equals(rubrique.getRubrique().getBasp()))
						break;
				}
				else
					break;
			}
	}
		if (nbreBaremeRubrique == 0)
		{
			// logger
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90061", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), salary.getInfoSalary().getComp_id().getNmat(),
							rubrique.getRubrique().getComp_id().getCrub()));
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "------------------>nbreBaremeRubrique is equal to 0, return false";
			return false;
		}
		
		// -------------------------------------------------------------------------------
		// -- Perception mini
		// -------------------------------------------------------------------------------
		if (!ClsObjectUtil.isNull(rubrique.getRubrique().getPmin()))
		{
			val = rubrique.convertToNumber(rubrique.getRubrique().getPmin());
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "Rubrique.pmin = "+val;
			if (salary.getParameter().isPbWithCalulation())
			{
				if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "------------------>pb with calculation , converting rubrique.pmin to number, return false";
				return false;
			}
			salary.getValeurRubriquePartage().setValeur(val);
			if (salary.getValeurRubriquePartage().getAmount() < salary.getValeurRubriquePartage().getValeur() && salary.getValeurRubriquePartage().getValeur() != 0)
			{
				val = salary.getValeurRubriquePartage().getValeur();
				salary.getValeurRubriquePartage().setAmount(val);
			}
		}
		// -------------------------------------------------------------------------------
		// -- Perception maxi
		// -------------------------------------------------------------------------------
		if (!ClsObjectUtil.isNull(rubrique.getRubrique().getPmax()))
		{
			val = rubrique.convertToNumber(rubrique.getRubrique().getPmax());
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "Rubrique.pmax = "+val;
			if (salary.getParameter().isPbWithCalulation())
			{
				if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "------------------>pb with calculation rubrique.pmax to number, return false";
				return false;
			}
			salary.getValeurRubriquePartage().setValeur(val);
			if (salary.getValeurRubriquePartage().getAmount() > salary.getValeurRubriquePartage().getValeur() && salary.getValeurRubriquePartage().getValeur() != 0)
			{
				val = salary.getValeurRubriquePartage().getValeur();
				salary.getValeurRubriquePartage().setAmount(val);
			}
		}

		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "------------------>no pb, return true, end algo";
		return true;
	}
	
	public boolean algo8(ClsRubriqueClone rubrique)
	{
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo8";
		String cdos = salary.getInfoSalary().getComp_id().getCdos();
		Double montant = new Double(0);
		String a_cacc = "";
		//
		salary.getValeurRubriquePartage().setRates(0);
		salary.getValeurRubriquePartage().setAmount(0);
		//
		String strCle1 = rubrique.getRubrique().getCle1();
		String strCle2 = rubrique.getRubrique().getCle2();
		String val = null;
		String crub = rubrique.getRubrique().getComp_id().getCrub();
		
		String valeur1 = StringUtils.EMPTY;
		String valeur2 = StringUtils.EMPTY;
		//On peut concatener plusieurs informations en mettant dans cle1 038;040 et dans cle2 001;002 afin d'avoir la concatenation de la catégorie, echelon,niv1 et niv2
		if(StringUtils.isNotBlank(strCle1))
		{
			String[] strCles1 = StringUtils.split(strCle1,";");
			for(String str : strCles1)
			{
				val = StringUtils.EMPTY;
				if(StringUtils.isNotBlank(str))
				{
					Character ch = str.toCharArray()[0];
					if (Character.isLetter(ch) && ch =='C') val = StringUtils.substring(str, 1, 5);
					else
					{
						if(NumberUtils.isNumber(str)) val = concateneCle1(Integer.valueOf(str), crub);
						if (salary.getParameter().isPbWithCalulation()) return false;
					}
				}
				valeur1 +=val;
			}
		}
		
		if(StringUtils.isNotBlank(strCle2))
		{
			String[] strCles2 = StringUtils.split(strCle2,";");
			for(String str : strCles2)
			{
				val = StringUtils.EMPTY;
				if(StringUtils.isNotBlank(str))
				{
					Character ch = str.toCharArray()[0];
					if (Character.isLetter(ch) && ch =='C') val = StringUtils.substring(str, 1, 5);
					else
					{
						if(NumberUtils.isNumber(str)) val = concateneCle2(Integer.valueOf(str), crub);
						//if (salary.getParameter().isPbWithCalulation()) return false;
					}
				}
				valeur2 +=val;
			}
		}
		a_cacc = valeur1+valeur2;
//		int cle1 = ClsObjectUtil.isNull(rubrique.getRubrique().getCle1()) ? 0 : Integer.valueOf(rubrique.getRubrique().getCle1());
//		int cle2 = ClsObjectUtil.isNull(rubrique.getRubrique().getCle2()) ? 0 : Integer.valueOf(rubrique.getRubrique().getCle2());
//		a_cacc = concatene(cle1, cle2, rubrique.getRubrique().getComp_id().getCrub());
		
		if (salary.getParameter().isPbWithCalulation())
		{
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "...Probléme de calcul!";
			return false;
		}
		if(StringUtils.isBlank(a_cacc)) return true;
			
		//cas d'une date
		try
		{
			String[] datePatterns = new String[] { "dd-MM-yyyy", "dd/MM/yyyy", "yyyy-MM-dd", "yyyy/MM/dd", "ddMMyyyy" };
			Date dt1 = DateUtils.parseDate(a_cacc, datePatterns);
			ClsDate dt = new ClsDate(dt1);
			
			salary.getValeurRubriquePartage().setBase(dt.getDay());
			salary.getValeurRubriquePartage().setBasePlafonnee(dt.getDay());
			salary.getValeurRubriquePartage().setRates(dt.getMonth());
			salary.getValeurRubriquePartage().setAmount(dt.getYear());
			
		}
		catch (ParseException e)
		{
			//ce n'est pas une date, donc c'est autre chose, soit RXXXX pour retourner le montant de la rubrique XXXX, soit un monant
			montant = Double.valueOf(0);
			try
			{
				montant = Double.parseDouble(a_cacc.replaceAll(",", "."));
			}
			catch(Exception ex2)
			{
				
			}
			
			salary.getValeurRubriquePartage().setValeur(montant);
			// -- Calcul du montant
			if ("T".equals(rubrique.getRubrique().getToum()))
			{
				if (salary.getValeurRubriquePartage().getBase() == 0)
				{
					salary.getValeurRubriquePartage().setBase(salary.getValeurRubriquePartage().getValeur());
					salary.getValeurRubriquePartage().setBasePlafonnee(salary.getValeurRubriquePartage().getValeur());
					salary.getValeurRubriquePartage().setAmount(salary.getValeurRubriquePartage().getValeur());
				}
				else
				{
					salary.getValeurRubriquePartage().setRates(salary.getValeurRubriquePartage().getValeur());
					montant = salary.getValeurRubriquePartage().getBase() * salary.getValeurRubriquePartage().getRates() / 100;
					salary.getValeurRubriquePartage().setAmount(montant);
				}
			}
			else if ("D".equals(rubrique.getRubrique().getToum()))
			{
				salary.getValeurRubriquePartage().setRates(salary.getValeurRubriquePartage().getValeur());
				if (salary.getValeurRubriquePartage().getRates() != 0)
				{
					montant = salary.getValeurRubriquePartage().getBase() / salary.getValeurRubriquePartage().getRates();
					salary.getValeurRubriquePartage().setAmount(montant);
				}
				else
					salary.getValeurRubriquePartage().setAmount(0);
			}
			else if ("M".equals(rubrique.getRubrique().getToum()))
			{
				salary.getValeurRubriquePartage().setBase(salary.getValeurRubriquePartage().getValeur());
				salary.getValeurRubriquePartage().setBasePlafonnee(salary.getValeurRubriquePartage().getValeur());
				salary.getValeurRubriquePartage().setAmount(salary.getValeurRubriquePartage().getValeur());
				salary.getValeurRubriquePartage().setRates(0);
			}
			else if ("R".equals(rubrique.getRubrique().getToum()))
			{
				salary.getValeurRubriquePartage().setBase(salary.getValeurRubriquePartage().getValeur());
				salary.getValeurRubriquePartage().setBasePlafonnee(salary.getValeurRubriquePartage().getValeur());
				salary.getValeurRubriquePartage().setAmount(salary.getValeurRubriquePartage().getValeur());
				salary.getValeurRubriquePartage().setRates(0);
			}
			else
			{
				// logger
				String error = salary.getParameter().errorMessage("ERR-90058", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), salary.getInfoSalary().getComp_id().getNmat(),
						rubrique.getRubrique().getComp_id().getCrub());
				salary.getParameter().setError(error);
				if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + salary.getParameter().getError();
				return false;
			}
		}
			
		
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo12(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo12(ClsRubriqueClone rubrique)
	{
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo12";
		int tabl = 0;
		double val = 0;
		String cleAccess = "";
		String RateOrAmount = rubrique.getRubrique().getToum();
		ClsEnumeration.EnColumnToRead colToRead = "T".equals(RateOrAmount) ? ClsEnumeration.EnColumnToRead.RATES : ClsEnumeration.EnColumnToRead.AMOUNT;
		double amountOrRateValue = -1;
		tempNumber = null;
		if (rubrique.getRubrique().getTabl() != null)
		{
			cleAccess = rubrique.getRubrique().getComp_id().getCrub();
			tabl = Integer.valueOf(rubrique.getRubrique().getTabl());
			tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,this.salary.getInfoSalary().getComp_id().getCdos(), tabl, cleAccess,
					rubrique.getRubrique().getNutm(), this.salary.getNlot(), this.salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), colToRead, rubrique.getRubrique().getComp_id().getCrub());
		}
		if (tempNumber  == null)
		{
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90056", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat(), cleAccess, rubrique.getRubrique().getTabl()));
			salary.getParameter().setPbWithCalulation(true);
			return false;
		}
		amountOrRateValue = tempNumber.doubleValue();
		salary.getValeurRubriquePartage().setValeur(amountOrRateValue);
		salary.getValeurRubriquePartage().setBase(this.salary.getInfoSalary().getIndi());
		salary.getValeurRubriquePartage().setBasePlafonnee(this.salary.getInfoSalary().getIndi());
		salary.getValeurRubriquePartage().setRates(amountOrRateValue);
		val = salary.getValeurRubriquePartage().getBase() * salary.getValeurRubriquePartage().getRates();
		salary.getValeurRubriquePartage().setAmount(val);
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo13(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo13(ClsRubriqueClone rubrique)
	{
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo13";
		double w_mtrest = 0;
		double montant = 0;
		double taux = 0;
		double base = 0;
		double val = 0;
		//
		// BEGIN
		// -- Initialisations
		salary.getValeurRubriquePartage().setAmount(0);
		salary.getValeurRubriquePartage().setBase(0);
		salary.getValeurRubriquePartage().setRates(0);
		//
		// -- si traitement retroactif alors on prend les donnees dans les cumuls.
		String cdos = salary.getInfoSalary().getComp_id().getCdos();
		String crub = rubrique.getRubrique().getComp_id().getCrub();
		if (salary.getParameter().isUseRetroactif())
		{
			montant = salary.getUtilNomenclature().getCumul(salary, salary.getPeriodOfPay(), crub, salary.getNbul(), ClsEnumeration.EnTypeOfColumn.AMOUNT, salary.getMoisPaieCourant(),
					salary.getPeriodOfPay());
			taux = salary.getUtilNomenclature().getCumul(salary, salary.getPeriodOfPay(), crub, salary.getNbul(), ClsEnumeration.EnTypeOfColumn.RATES, salary.getMoisPaieCourant(),
					salary.getPeriodOfPay());
			base = salary.getUtilNomenclature().getCumul(salary, salary.getPeriodOfPay(), crub, salary.getNbul(), ClsEnumeration.EnTypeOfColumn.BASE, salary.getMoisPaieCourant(),
					salary.getPeriodOfPay());
			//
			salary.getValeurRubriquePartage().setBase(base);
			salary.getValeurRubriquePartage().setBasePlafonnee(base);
			salary.getValeurRubriquePartage().setAmount(montant);
			salary.getValeurRubriquePartage().setRates(taux);
			return true;
		}
		//
		int nbreDeprets = 0;
		if (rubrique.getListOfLoanNumber() != null)
		{
			nbreDeprets = rubrique.getListOfLoanNumber().size();
			if (nbreDeprets == 0)
				return true;
		}
		else
			return true;
		// on prend le premier prét de la liste des préts de ce salarié
		Object obj = salary.getService().find("FROM PretInterne WHERE identreprise='"+cdos+"' AND nmat='"+salary.getInfoSalary().getComp_id().getNmat()+"' AND identreprise="+((Integer) rubrique.getListOfLoanNumber().get(0)).intValue());
		if (obj == null)
			return true;
		PretInterne pret = (PretInterne) obj;
		if (ClsObjectUtil.isNull(pret.getMtremb()))
			pret.setMtremb(new BigDecimal(0));
		if (!ClsObjectUtil.isNull(pret.getMtpr()))
			w_mtrest = pret.getMtpr().doubleValue() - pret.getMtremb().doubleValue();
		else
			w_mtrest = 0;
		
		// --------------------------------------------------------------------
		// -- Calcul du montant ( montant = mensualite prevue ou calculee )
		// --------------------------------------------------------------------
		// -- LH 210198
		if (salary.getParameter().isStc() && salary.getParameter().getEnBasePretsSTC() == ClsEnumeration.EnLoan.STC)
		{
			if (!ClsObjectUtil.isNull(pret.getMtpr()))
				salary.getValeurRubriquePartage().setAmount(w_mtrest);
			else
				salary.getValeurRubriquePartage().setAmount(pret.getMtmens().doubleValue());
		}
		else
		{
			if (!ClsObjectUtil.isNull(pret.getMtmens()))
			{
				salary.getValeurRubriquePartage().setAmount(pret.getMtmens().doubleValue());
			}
			else
			{
				if (pret.getNbmens().intValue() != 0)
				{
					val = pret.getMtpr().doubleValue() / pret.getNbmens().doubleValue();
					salary.getValeurRubriquePartage().setAmount(Math.ceil(val));
				}
			}
			if (!ClsObjectUtil.isNull(pret.getMtpr()))
			{
				if (salary.getValeurRubriquePartage().getAmount() > w_mtrest)
					salary.getValeurRubriquePartage().setAmount(w_mtrest);
			}
		}

		//
		// --------------------------------------------------------------------
		// -- Calcul de la base ( base = montant emprunte )
		// --------------------------------------------------------------------
		if (salary.getParameter().isStc() && salary.getParameter().getEnBasePretsSTC() == ClsEnumeration.EnLoan.STC)
			salary.getValeurRubriquePartage().setBase(0);
		else
		{
			if (ClsObjectUtil.isNull(pret.getMtpr()))
			{
				// -- Base = deja rembourse, mois inclus
				val = pret.getMtremb().doubleValue() + pret.getMtmens().doubleValue();
				salary.getValeurRubriquePartage().setBase(val);
				salary.getValeurRubriquePartage().setBasePlafonnee(val);
			}
			else
			{
				if (salary.getEntreprise() == ClsEnumeration.EnEnterprise.SHELL_GABON)
				{
//					if (ClsObjectUtil.isNull(pret.getMtmens()))
//					{
//						if (pret.getNbmens().intValue() != 0)
//						{
//							val = w_mtrest - (pret.getMtpr().doubleValue() / pret.getNbmens().intValue());
//							salary.getValeurRubriquePartage().setBase(val);
//						}
//					}
//					else
//					{
//						val = w_mtrest - pret.getMtmens().doubleValue();
//						salary.getValeurRubriquePartage().setBase(val);
//					}
//					salary.getValeurRubriquePartage().setBasePlafonnee(salary.getValeurRubriquePartage().getBase());
				}
				else
				{
					// -- Base = Montant du pret
					if ("MP".equals(salary.getParameter().getBasePrets()))
						salary.getValeurRubriquePartage().setBase(pret.getMtpr().doubleValue());
					else if ("DRI".equals(salary.getParameter().getBasePrets()))
					{
						salary.getValeurRubriquePartage().setBase(pret.getMtremb().doubleValue() + salary.getValeurRubriquePartage().getAmount());
					}
					else if ("DRN".equals(salary.getParameter().getBasePrets()))
					{
						salary.getValeurRubriquePartage().setBase(pret.getMtremb().doubleValue());
					}
					else if ("RRI".equals(salary.getParameter().getBasePrets()))
					{
						salary.getValeurRubriquePartage().setBase(pret.getMtpr().doubleValue() - pret.getMtremb().doubleValue() - salary.getValeurRubriquePartage().getAmount());
					}
					else if ("RRN".equals(salary.getParameter().getBasePrets()))
					{
						salary.getValeurRubriquePartage().setBase(pret.getMtpr().doubleValue() - pret.getMtremb().doubleValue());
					}
					else
						salary.getValeurRubriquePartage().setBase(pret.getMtpr().doubleValue());
					//salary.getValeurRubriquePartage().setBase(pret.getMtpr().doubleValue());
					//salary.getValeurRubriquePartage().setBasePlafonnee(pret.getMtpr().doubleValue());
				}
			}
		}
		
		// ---------------------------------------------------------------------
		// -- Calcul du taux
		// -- ( taux = nombre de mois equivalent au montant deja
		// -- rembourse pour la mensualite precedemment calculee )
		// ---------------------------------------------------------------------
		if (salary.getParameter().isStc() && salary.getParameter().getEnBasePretsSTC() == ClsEnumeration.EnLoan.STC)
			salary.getValeurRubriquePartage().setRates(0);
		else
		{
			// -- w_tau = Nb d'echeances deja payees
			if (ClsObjectUtil.isNull(pret.getNbmens()))
			{
				if (salary.getValeurRubriquePartage().getAmount() != 0)
				{
					val = (pret.getMtremb().doubleValue() + salary.getValeurRubriquePartage().getAmount()) / salary.getValeurRubriquePartage().getAmount();
					salary.getValeurRubriquePartage().setRates(val);
				}
			}
			else
			{
				if (pret.getNbmens().intValue() != 0)
				{
					val = (pret.getMtremb().doubleValue() + salary.getValeurRubriquePartage().getAmount()) / (pret.getMtpr().doubleValue() / pret.getNbmens().doubleValue());
					salary.getValeurRubriquePartage().setRates(val);
				}
			}
			if (salary.getEntreprise() == ClsEnumeration.EnEnterprise.SHELL_GABON)
			{
				// -- w_tau = Nb d'echances restantes
//				if (!ClsObjectUtil.isNull(pret.getMtpr()))
//				{
//					if (ClsObjectUtil.isNull(pret.getNbmens()))
//					{
//						if (!ClsObjectUtil.isNull(pret.getMtmens()))
//						{
//							val = (pret.getMtpr().doubleValue() / pret.getMtmens().doubleValue()) - salary.getValeurRubriquePartage().getRates();
//							salary.getValeurRubriquePartage().setRates(val);
//						}
//					}
//					else
//					{
//						val = pret.getNbmens().doubleValue() - salary.getValeurRubriquePartage().getRates();
//						salary.getValeurRubriquePartage().setRates(val);
//					}
//				}
			}
			else
			{
				if (!ClsObjectUtil.isNull(pret.getMtpr()))
				{
					if(pret.getNbmens() == null || (pret.getNbmens() != null && pret.getNbmens().doubleValue() == 0))
						if (pret.getMtmens() != null && pret.getMtmens().doubleValue() != 0)
							pret.setNbmens(new BigDecimal(pret.getMtpr().doubleValue()/pret.getMtmens().doubleValue()));
					
					if (pret.getNbmens() != null && pret.getNbmens().doubleValue() != 0)
					{
						double nber = (pret.getMtpr().doubleValue()-pret.getMtremb().doubleValue())/(pret.getMtpr().doubleValue()/pret.getNbmens().doubleValue());
						if ("NM".equals(salary.getParameter().getBasePretsTaux()))
						{
							salary.getValeurRubriquePartage().setRates(pret.getNbmens().doubleValue());
						}
						else if ("DRI".equals(salary.getParameter().getBasePretsTaux()))
						{
							salary.getValeurRubriquePartage().setRates(pret.getNbmens().doubleValue() - nber + 1);
						}
						else if ("DRN".equals(salary.getParameter().getBasePretsTaux()))
						{
							salary.getValeurRubriquePartage().setRates(pret.getNbmens().doubleValue() - nber);
						}
						else if ("RRI".equals(salary.getParameter().getBasePretsTaux()))
						{
							salary.getValeurRubriquePartage().setRates(nber - 1);
						}
						else if ("RRN".equals(salary.getParameter().getBasePretsTaux()))
						{
							salary.getValeurRubriquePartage().setRates(nber);
						}
						else
							salary.getValeurRubriquePartage().setRates(pret.getNbmens().doubleValue());
					}
				}
			}
			if (salary.getValeurRubriquePartage().getRates() > 999)
				salary.getValeurRubriquePartage().setRates(999);
		}
		salary.getValeurRubriquePartage().setBasePlafonnee(salary.getValeurRubriquePartage().getBase());
		
		salary.getEvparam().setNprt(ClsStringUtil.formatNumber(Integer.valueOf(pret.getIdEntreprise()), "000000"));
		salary.getEvparam().setArgu("No ".concat(ClsStringUtil.formatNumber(Integer.valueOf(pret.getIdEntreprise()), "000000")));
		// enlever ce prét de la liste des préts
		rubrique.getListOfLoanNumber().remove(pret.getIdEntreprise());
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo14(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo14(ClsRubriqueClone rubrique)
	{
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo14";
		// double w_mtrest = 0;
		// double montant = 0;
		// double base = 0;
		double amountOrRate = 0;
		double val = 0;
		double w_basap = salary.getValeurRubriquePartage().getBase();
		double wcalc1 = 0;
		double wcalc2 = 0;
		int e = 0;
		int multAppoint = 0;
		double baseAppoint = 0;
		if (salary.getValeurRubriquePartage().getBase() < 0)
			return true;
		// -- Lecture table 54 si l'appoint est calcule pour ce mode de paiement
		String cdos = salary.getInfoSalary().getComp_id().getCdos();
		String libelle = this.getLabelFromNomenclature(cdos, 54, salary.getInfoSalary().getModp(), 3, salary.getNlot(), salary.getNbul(), salary.getMoisPaieCourant(),
				salary.getPeriodOfPay(), rubrique.getRubrique().getComp_id().getCrub());
		// if(! ClsObjectUtil.isNull(libelle)){
		if (ClsObjectUtil.isNull(libelle))
		{
			// logger
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90056", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat(), salary.getInfoSalary().getModp(), 54));
			return false;
		}
		if (!"O".equals(libelle))
			return true;
		// -- Lecture de la valeur de l'appoint
		String cleAccess = "";
		int tabl = 0;
		String RateOrAmount = rubrique.getRubrique().getToum();
		ClsEnumeration.EnColumnToRead colToRead = "T".equals(RateOrAmount) ? ClsEnumeration.EnColumnToRead.RATES : ClsEnumeration.EnColumnToRead.AMOUNT;
		//
		tempNumber = null;
		if (rubrique.getRubrique().getTabl() != null)
		{
			cleAccess = rubrique.getRubrique().getComp_id().getCrub();
			tabl = Integer.valueOf(rubrique.getRubrique().getTabl());
//			taux = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,this.salary.getInfoSalary().getComp_id().getCdos(), tabl, cleAccess, 1, this.salary.getNlot(),
//					this.salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.RATES);
			tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,this.salary.getInfoSalary().getComp_id().getCdos(), tabl, cleAccess, 1, this.salary.getNlot(),
					this.salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), colToRead, rubrique.getRubrique().getComp_id().getCrub());
		}
		if (tempNumber == null)
		{
			// logger
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90056", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat(), cleAccess, rubrique.getRubrique().getTabl()));
			return false;
		}
		amountOrRate = tempNumber.doubleValue();
		multAppoint = 1;
		if (!ClsObjectUtil.isNull(salary.getParameter().getDossierInfo()))
		{

			if (salary.getParameter().getDossierInfo().getNddd() == null)
				salary.getParameter().getDossierInfo().setNddd(0);

			if (salary.getParameter().getDossierInfo().getNddd() == 0)
				multAppoint = 1;
			else if (salary.getParameter().getDossierInfo().getNddd() == 1)
				multAppoint = 10;
			else if (salary.getParameter().getDossierInfo().getNddd() == 2)
				multAppoint = 100;
			else if (salary.getParameter().getDossierInfo().getNddd() == 3)
				multAppoint = 1000;
			else
				multAppoint = 1;
		}
		
		//On arrondi directement la base calculée, dans le cas du calcul des arrondis
		int nddd=this.salary.parameter.dossierNbreDecimale;
		double newBaseArrondi = ClsStringUtil.truncateToXDecimal(salary.getValeurRubriquePartage().getBase(),nddd).doubleValue();
		this.salary.getValeurRubriquePartage().setBase(newBaseArrondi);

		
		
		w_basap = salary.getValeurRubriquePartage().getBase() * multAppoint;
		w_basap = ClsStringUtil.truncateToXDecimal(w_basap,nddd).doubleValue();
		baseAppoint = amountOrRate * multAppoint;
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "--------------------------txt=" + amountOrRate + " et base appoint=" + baseAppoint + " pr mult=" + multAppoint;
		//
		//
		// -- Chargement de la base
		// char18 := LTRIM(TO_CHAR(w_basap,'099999999999999999'));
		// -- w_basap := SUBSTR(char18,1,18);
		// w_basap10 := SUBSTR(char18,17,2);
		// w_basap100 := SUBSTR(char18,16,3);
		// w_basap1000 := SUBSTR(char18,15,4);
		// w_basap10000 := SUBSTR(char18,14,5);
		// w_basap100000 := SUBSTR(char18,13,6);
		// w_basap1000000 := SUBSTR(char18,12,7);
		// String sbasap = ClsStringUtil.formatNumber(w_basap, "0#");
		// String sbasap = ClsStringUtil.formatNumber(w_basap, "099999999999999999");
		String sbasap = ClsStringUtil._getPartieEntiere(w_basap, 18);
		String nullStr = "0";
		// int sbasap10 = Integer.valueOf(sbasap.length() > 17 ? sbasap.substring(17+1) : nullStr);
		// int sbasap100 = Integer.valueOf(sbasap.length() > 16 ? sbasap.substring(16+1) : nullStr);
		// int sbasap1000 = Integer.valueOf(sbasap.length() > 15 ? sbasap.substring(15+1) : sbasap);
		// int sbasap10000 = Integer.valueOf(sbasap.length() > 14 ? sbasap.substring(14+1) : nullStr);
		// int sbasap100000 = Integer.valueOf(sbasap.length() > 13 ? sbasap.substring(13+1) : nullStr);
		// int sbasap1000000 = Integer.valueOf(sbasap.length() > 12 ? sbasap.substring(12+1) : nullStr);

		int sbasap10 = Integer.valueOf(sbasap.length() > 17 ? sbasap.substring(17 - 1) : nullStr);
		int sbasap100 = Integer.valueOf(sbasap.length() > 16 ? sbasap.substring(16 - 1) : nullStr);
		int sbasap1000 = Integer.valueOf(sbasap.length() > 15 ? sbasap.substring(15 - 1) : nullStr);
		int sbasap10000 = Integer.valueOf(sbasap.length() > 14 ? sbasap.substring(14 - 1) : nullStr);
		int sbasap100000 = Integer.valueOf(sbasap.length() > 13 ? sbasap.substring(13 - 1) : nullStr);
		int sbasap1000000 = Integer.valueOf(sbasap.length() > 12 ? sbasap.substring(12 - 1) : nullStr);
		
		//
		// -- ----- Calcul de l'appoint
		if (baseAppoint < 100)
		{
			if (sbasap10 == 0)
			{
				if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "sbasap10 est = 0, return true";
				return true;
			}
			if (baseAppoint != 0)
				wcalc1 = 100 / baseAppoint;
			e = 1;
			while (e <= wcalc1)
			{
				wcalc2 = baseAppoint * e;
				if (wcalc2 == sbasap10)
					break;
				if (wcalc2 > sbasap10)
				{
					if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "--------------->>>wcalc2 = " + wcalc2 + " et sbasap10=" + sbasap10 + " => montant = " + (wcalc2 - sbasap10);
					salary.getValeurRubriquePartage().setAmount(wcalc2 - sbasap10);
					break;
				}
				e = e + 1;
			}
			if (multAppoint != 0)
				salary.getValeurRubriquePartage().setAmount(salary.getValeurRubriquePartage().getAmount() / multAppoint);

			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "baseAppoint < 100, return true";
			return true;
		}
		//
		if (baseAppoint < 1000)
		{
			if (sbasap100 == 0)
			{
				if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "sbasap100 est = 0, return true";
				return true;
			}
			if (baseAppoint != 0)
				wcalc1 = 1000 / baseAppoint;
			e = 1;
			while (e <= wcalc1)
			{
				wcalc2 = baseAppoint * e;
				if (wcalc2 == sbasap100)
					break;
				if (wcalc2 > sbasap100)
				{
					salary.getValeurRubriquePartage().setAmount(wcalc2 - sbasap100);
					break;
				}
				e = e + 1;
			}
			if (multAppoint != 0)
				salary.getValeurRubriquePartage().setAmount(salary.getValeurRubriquePartage().getAmount() / multAppoint);

			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "baseAppoint < 1000, return true";
			return true;
		}
		if (baseAppoint < 10000)
		{
			if (sbasap1000 == 0)
				return true;
			if (baseAppoint != 0)
				wcalc1 = 10000 / baseAppoint;
			e = 1;
			while (e <= wcalc1)
			{
				wcalc2 = baseAppoint * e;
				if (wcalc2 == sbasap1000)
					break;
				if (wcalc2 > sbasap1000)
				{
					salary.getValeurRubriquePartage().setAmount(wcalc2 - sbasap1000);
					break;
				}
				e = e + 1;
			}
			if (multAppoint != 0)
				salary.getValeurRubriquePartage().setAmount(salary.getValeurRubriquePartage().getAmount() / multAppoint);
			return true;
		}
		if (baseAppoint < 100000)
		{
			if (sbasap10000 == 0)
				return true;
			if (baseAppoint != 0)
				wcalc1 = 100000 / baseAppoint;
			e = 1;
			while (e <= wcalc1)
			{
				wcalc2 = baseAppoint * e;
				if (wcalc2 == sbasap10000)
					break;
				if (wcalc2 > sbasap10000)
				{
					salary.getValeurRubriquePartage().setAmount(wcalc2 - sbasap10000);
					break;
				}
				e = e + 1;
			}
			if (multAppoint != 0)
			{
				salary.getValeurRubriquePartage().setAmount(salary.getValeurRubriquePartage().getAmount() / multAppoint);
			}
			return true;
		}

		if (baseAppoint < 1000000)
		{
			if (sbasap100000 == 0)
				return true;
			if (baseAppoint != 0)
				wcalc1 = 1000000 / baseAppoint;
			e = 1;
			while (e <= wcalc1)
			{
				wcalc2 = baseAppoint * e;
				if (wcalc2 == sbasap100000)
					break;
				if (wcalc2 > sbasap100000)
				{
					salary.getValeurRubriquePartage().setAmount(wcalc2 - sbasap100000);
					break;
				}
				e = e + 1;
			}
			if (multAppoint != 0)
				salary.getValeurRubriquePartage().setAmount(salary.getValeurRubriquePartage().getAmount() / multAppoint);
			return true;
		}

		if (baseAppoint < 10000000)
		{
			if (sbasap1000000 == 0)
				return true;
			if (baseAppoint != 0)
				wcalc1 = 10000000 / baseAppoint;
			e = 1;
			while (e <= wcalc1)
			{
				wcalc2 = baseAppoint * e;
				if (wcalc2 == sbasap1000000)
					break;
				if (wcalc2 > sbasap1000000)
				{
					salary.getValeurRubriquePartage().setAmount(wcalc2 - sbasap1000000);
					break;
				}
				e = e + 1;
			}
			if (multAppoint != 0)
				salary.getValeurRubriquePartage().setAmount(salary.getValeurRubriquePartage().getAmount() / multAppoint);
			return true;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo15(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo15(ClsRubriqueClone rubrique)
	{
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo15";
		salary.getValeurRubriquePartage().setRates(0);
		if (salary.getValeurRubriquePartage().getBase() < salary.getParameter().getBulletinNegative())
			salary.getValeurRubriquePartage().setAmount(salary.getParameter().getBulletinNegative() - salary.getValeurRubriquePartage().getBase());
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo16(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo16(ClsRubriqueClone rubrique)
	{
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo16";
		String cdos = salary.getInfoSalary().getComp_id().getCdos();
		String crub = rubrique.getRubrique().getComp_id().getCrub();

		// String queryString = "from ElementSalaireBareme "
		// + " where identreprise='" + cdos + "'"
		// + " and crub = '" + crub + "'"
		// + " order by cdos , crub , nume";
		// String queryStringRetro = "from Rhthrubbarem "
		// + " where identreprise='" + cdos + "'"
		// + " and crub = '" + crub + "'"
		// + " and aamm = '" + salary.getPeriodOfPay() + "'"
		// + " and nbul = " + salary.getNbul()
		// + " order by cdos , crub , nume";
		//
		salary.getValeurRubriquePartage().setAmount(0);
		salary.getValeurRubriquePartage().setRates(0);
		//
		double montant = 0;
		boolean ok = false;
		double taux = 0;
		String valeur1 = "";
		String valeur2 = "";
		double dblValeur1 = 0;
		double dblValeur2 = 0;
		int nbreBaremeRubrique = 0;
		//Rhthrubbarem oRhthrubbarem = null;
		ElementSalaireBareme oElementSalaireBareme = null;
		// construit la liste des barémes
		// List listOfBarem = salary.getParameter().isUseRetroactif() ? salary.getService().find(queryStringRetro) : salary.getService().find(queryString);
		String keyOfBaremeList = salary.getParameter().isUseRetroactif() ? crub + this.salary.getPeriodOfPay() + this.salary.getNbul() : crub;
		List listOfBarem = (List) salary.getParameter().getListOfRubriquebaremeMap().get(keyOfBaremeList);
		if (listOfBarem != null)
			for (Object barem : listOfBarem)
			{
				if (salary.getParameter().isUseRetroactif())
				{
//					oRhthrubbarem = (Rhthrubbarem) barem;
//					montant = oRhthrubbarem.getMont().doubleValue();
//					taux = oRhthrubbarem.getTaux().doubleValue();
//					valeur1 = oRhthrubbarem.getVal1();
//					valeur2 = oRhthrubbarem.getVal2();
				}
				else
				{
					oElementSalaireBareme = (ElementSalaireBareme) barem;
//					montant = oElementSalaireBareme.getMont().doubleValue();
//					taux = oElementSalaireBareme.getTaux().doubleValue();
//					valeur1 = oElementSalaireBareme.getVal1();
//					valeur2 = oElementSalaireBareme.getVal2();
					montant = rubrique.getTauxOuMontantBareme(oElementSalaireBareme, false);
					taux = rubrique.getTauxOuMontantBareme(oElementSalaireBareme, true);
					valeur1 = rubrique.getValeurBareme(oElementSalaireBareme, true);
					valeur2 = rubrique.getValeurBareme(oElementSalaireBareme, false);
				}
				//
				nbreBaremeRubrique++;
				if (nbreBaremeRubrique > 50)
				{
					nbreBaremeRubrique = 50;
					break;
				}
				//
				dblValeur1 = rubrique.convertToNumber(valeur1);
				if (salary.getParameter().isPbWithCalulation())
				{
					return false;
				}
				dblValeur2 = rubrique.convertToNumber(valeur2);
				if (salary.getParameter().isPbWithCalulation())
				{
					return false;
				}
				if (salary.getValeurRubriquePartage().getBase() >= dblValeur1 && salary.getValeurRubriquePartage().getBase() <= dblValeur2)
				{
					ok = true;
					salary.getValeurRubriquePartage().setAmount(montant);
					break;
				}
			}
		if (nbreBaremeRubrique == 0)
		{
			// logger
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90061", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), salary.getInfoSalary().getComp_id().getNmat(),
							rubrique.getRubrique().getComp_id().getCrub()));
			return false;
		}
		if (ok == false)
			salary.getValeurRubriquePartage().setAmount(0);
		// -- Multiplication par nombre de femmes
		if (ok == true && "M".equals(salary.getInfoSalary().getSitf()))
			salary.getValeurRubriquePartage().setAmount(salary.getValeurRubriquePartage().getAmount() * (salary.getInfoSalary().getNbfe() + 1));
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo17(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo17(ClsRubriqueClone rubrique)
	{
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo17";
		String cdos = salary.getInfoSalary().getComp_id().getCdos();
		String crub = rubrique.getRubrique().getComp_id().getCrub();
		PretExterneEntete wpaprent = null;
		double w_mens = 0;
		double montant = 0;
		double taux = 0;
		double base = 0;
		salary.getValeurRubriquePartage().setAmount(0);
		salary.getValeurRubriquePartage().setBase(0);
		salary.getValeurRubriquePartage().setRates(0);
		//
		// -- si traitement retroactif alors on prend les donnees dans les cumuls.
		if (salary.getParameter().isUseRetroactif())
		{
			base = salary.getUtilNomenclature().getCumul(salary, salary.getPeriodOfPay(), crub, salary.getNbul(), ClsEnumeration.EnTypeOfColumn.BASE, salary.getMoisPaieCourant(),
					salary.getPeriodOfPay());
			montant = salary.getUtilNomenclature().getCumul(salary, salary.getPeriodOfPay(), crub, salary.getNbul(), ClsEnumeration.EnTypeOfColumn.AMOUNT, salary.getMoisPaieCourant(),
					salary.getPeriodOfPay());
			taux = salary.getUtilNomenclature().getCumul(salary, salary.getPeriodOfPay(), crub, salary.getNbul(), ClsEnumeration.EnTypeOfColumn.RATES, salary.getMoisPaieCourant(),
					salary.getPeriodOfPay());
			//
			salary.getValeurRubriquePartage().setAmount(montant);
			salary.getValeurRubriquePartage().setBase(base);
			salary.getValeurRubriquePartage().setRates(taux);
			salary.getValeurRubriquePartage().setBasePlafonnee(base);
			return true;
		}
		if (rubrique.getListOfLoanNumber() == null || rubrique.getListOfLoanNumber().size() == 0)
			return true;
		
		List listOfRhtprentagent = salary.getService().find(
				"from PretExterneEntete" + " where identreprise='" + cdos + "'" + " and nprt = " + rubrique.getListOfLoanNumber().get(0) + " and nmat = '"
						+ salary.getInfoSalary().getComp_id().getNmat() + "'");
		if (listOfRhtprentagent == null || listOfRhtprentagent.size() == 0)
			return true;
		else
			wpaprent = (PretExterneEntete) listOfRhtprentagent.get(0);
		//
		// --LH 2101998
		if (salary.getParameter().isStc() && salary.getParameter().getEnBasePretsSTC() == ClsEnumeration.EnLoan.STC)
			// -- Dans le cas d'une radiation, le pret est solde
			w_mens = wpaprent.getResr().doubleValue();
		else
		{
			// -- Lecture de la mensualite
			List listOfPretExterneDetail = salary.getService().find("from PretExterneDetail" + " where identreprise='" + cdos + "'" + " and nprt = " + wpaprent.getNprt());
			for (Object prlig : listOfPretExterneDetail)
			{
				if (salary.getPeriodOfPay().equals(new ClsDate(((PretExterneDetail) prlig).getPerb()).getYearAndMonth()))
				{
					w_mens = ((PretExterneDetail) prlig).getEchr().doubleValue();
					break;
				}
			}
		}
		//
		if ("MP".equals(salary.getParameter().getBasePrets()))
			salary.getValeurRubriquePartage().setBase(wpaprent.getMntp().doubleValue());
		else if ("DRI".equals(salary.getParameter().getBasePrets()))
		{
			salary.getValeurRubriquePartage().setBase(wpaprent.getMntp().doubleValue() - wpaprent.getResr().doubleValue() + w_mens);
		}
		else if ("DRN".equals(salary.getParameter().getBasePrets()))
		{
			salary.getValeurRubriquePartage().setBase(wpaprent.getMntp().doubleValue() - wpaprent.getResr().doubleValue());
		}
		else if ("RRI".equals(salary.getParameter().getBasePrets()))
		{
			salary.getValeurRubriquePartage().setBase(wpaprent.getResr().doubleValue() - w_mens);
		}
		else if ("RRN".equals(salary.getParameter().getBasePrets()))
		{
			salary.getValeurRubriquePartage().setBase(wpaprent.getResr().doubleValue());
		}
		else
			salary.getValeurRubriquePartage().setBase(wpaprent.getMntp().doubleValue());
		salary.getValeurRubriquePartage().setBasePlafonnee(salary.getValeurRubriquePartage().getBase());
		salary.getEvparam().setArgu("No ".concat(ClsStringUtil.formatNumber(Integer.valueOf(wpaprent.getNprt()), "000000")));
		salary.getValeurRubriquePartage().setAmount(w_mens);
		salary.getValeurRubriquePartage().setRates(0);
		// if(wpaprent.getNbec()==null)
		if (wpaprent.getNbec() != null && wpaprent.getNbec() != 0)
		{
			if ("NM".equals(salary.getParameter().getBasePretsTaux()))
			{
				salary.getValeurRubriquePartage().setRates(wpaprent.getNbec());
			}
			else if ("DRI".equals(salary.getParameter().getBasePretsTaux()))
			{
				salary.getValeurRubriquePartage().setRates(wpaprent.getNbec() - wpaprent.getNber() + 1);
			}
			else if ("DRN".equals(salary.getParameter().getBasePretsTaux()))
			{
				salary.getValeurRubriquePartage().setRates(wpaprent.getNbec() - wpaprent.getNber());
			}
			else if ("RRI".equals(salary.getParameter().getBasePretsTaux()))
			{
				salary.getValeurRubriquePartage().setRates(wpaprent.getNber() - 1);
			}
			else if ("RRN".equals(salary.getParameter().getBasePretsTaux()))
			{
				salary.getValeurRubriquePartage().setRates(wpaprent.getNber());
			}
			else
				salary.getValeurRubriquePartage().setRates(wpaprent.getNbec());
		}
		salary.getEvparam().setNprt(ClsStringUtil.formatNumber(Integer.valueOf(wpaprent.getNprt()), "000000"));

		// enlever un numéro de prét dans la liste
		rubrique.getListOfLoanNumber().remove(0);
		//
		//Gestion de la devise
		String devise = wpaprent.getCodedevise();
		if(StringUtils.isNotBlank(devise))
		{
			if(!devise.equalsIgnoreCase(salary.parameter.dossierCcy))
			{
				tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,cdos, 27, devise, 1, salary.getNlot(),
						salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.RATES, rubrique.getRubrique().getComp_id().getCrub());
				if(tempNumber != null && tempNumber.intValue() !=0 && tempNumber.intValue() !=1)
				{
					double mnt = salary.getValeurRubriquePartage().getAmount();
					double basc = salary.getValeurRubriquePartage().getBase();
					mnt = mnt * tempNumber.doubleValue();
					basc = basc * tempNumber.doubleValue();
					salary.getValeurRubriquePartage().setAmount(mnt);
					salary.getValeurRubriquePartage().setBase(basc);
					salary.getValeurRubriquePartage().setBasePlafonnee(basc);
				}
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo18(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo18(ClsRubriqueClone rubrique)
	{
		String cdos = salary.getInfoSalary().getComp_id().getCdos();
		String crub = rubrique.getRubrique().getComp_id().getCrub();
		double base = 0;
		double val = 0;
		int tabl = 0;
		String cleAccess = "";
		//
		salary.getValeurRubriquePartage().setAmount(0);
		//salary.getValeurRubriquePartage().setBase(0);
		salary.getValeurRubriquePartage().setRates(0);
		salary.getValeurRubriquePartage().setValeur(0);
		//
		tempNumber = null;
		if (rubrique.getRubrique().getTabl() != null)
		{
			cleAccess = rubrique.getRubrique().getComp_id().getCrub();
			tabl = Integer.valueOf(rubrique.getRubrique().getTabl());
			tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,this.salary.getInfoSalary().getComp_id().getCdos(), tabl, cleAccess, 1, this.salary.getNlot(),
					this.salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.AMOUNT, rubrique.getRubrique().getComp_id().getCrub());
			//salary.getValeurRubriquePartage().setBase(base);
		}
		if (tempNumber == null)
		{
			// -- appeler le logger
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90056", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat(), rubrique.getRubrique().getComp_id().getCrub(), cleAccess, rubrique.getRubrique().getTabl()));
			return false;
		}
		base = tempNumber.doubleValue();
		salary.getValeurRubriquePartage().setBase(base);
		salary.getValeurRubriquePartage().setBasePlafonnee(base);
		Object obj = null;
		//
		if (rubrique.getElementVariableNombre() != 0 && rubrique.getElementVariableValeur() != 0)
		{
			salary.getValeurRubriquePartage().setValeur(rubrique.getElementVariableValeur());
		}
		// if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "..isUseRetroactif:" + salary.getParameter().isUseRetroactif());
		else
		{
			if (salary.getParameter().isUseRetroactif())
			{
				//obj = salary.getService().get(Rhthelfix.class, new RhthelfixPK(cdos, salary.getInfoSalary().getComp_id().getNmat(), crub, salary.getPeriodOfPay(), salary.getNbul()));
			}
			else
			{
				obj = (ElementFixeSalaire)salary.getService().find("FROM ElementFixeSalaire WHERE identreprise="+cdos+" AND codp='"+crub+"' AND nmat='"+salary.getInfoSalary().getComp_id().getNmat()+"'");
			}
			if (obj != null)
			{
				val = ((ElementFixeSalaire) obj).getMonp().doubleValue();
				salary.getValeurRubriquePartage().setValeur(val);
			}
		}
		// fixer le taux avec la valeur
		salary.getValeurRubriquePartage().setRates(salary.getValeurRubriquePartage().getValeur());
		if ("T".equals(rubrique.getRubrique().getToum()))
		{
			salary.getValeurRubriquePartage().setAmount(salary.getValeurRubriquePartage().getBase() * salary.getValeurRubriquePartage().getRates() / 100);
		}
		else if ("D".equals(rubrique.getRubrique().getToum()))
		{
			if (salary.getValeurRubriquePartage().getRates() != 0)
				salary.getValeurRubriquePartage().setAmount(salary.getValeurRubriquePartage().getBase() / salary.getValeurRubriquePartage().getRates());
		}
		else if ("M".equals(rubrique.getRubrique().getToum()))
		{
			salary.getValeurRubriquePartage().setAmount(salary.getValeurRubriquePartage().getBase() * salary.getValeurRubriquePartage().getValeur());
		}
		else
		{
			// logger
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90058", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), salary.getInfoSalary().getComp_id().getNmat(),
							rubrique.getRubrique().getComp_id().getCrub()));
			return false;
		}
		return true;
	}
	
	public boolean algo19(ClsRubriqueClone rubrique)
	{
//		if (StringUtils.equals(salary.getParameter().nomClient, ClsEntreprise.SOBRAGA))
//		{
//			salary.getValeurRubriquePartage().setRates(NumberUtils.bdnvl(salary.infoSalary.getNbjsa(),0).doubleValue());
//			salary.getValeurRubriquePartage().setAmount(salary.getValeurRubriquePartage().getBase() * salary.getValeurRubriquePartage().getRates() / 24);
//			return true;
//		}
		
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo2(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo2(ClsRubriqueClone rubrique)
	{
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo2";
		
		
		
		
		String accessKey = this.salary.getInfoSalary().getAfec() + this.salary.getInfoSalary().getCat() + this.salary.getInfoSalary().getEch();
		double amountOrRateValue = -1;
		double val = -1;
		//
		String RateOrAmount = rubrique.getRubrique().getToum();
		ClsEnumeration.EnColumnToRead colToRead = "T".equals(RateOrAmount) ? ClsEnumeration.EnColumnToRead.RATES : ClsEnumeration.EnColumnToRead.AMOUNT;
		int tabl = 0;
		tempNumber = null;
		if (rubrique.getRubrique().getTabl() != null)
		{
			tabl = Integer.valueOf(rubrique.getRubrique().getTabl());
			tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,this.salary.getInfoSalary().getComp_id().getCdos(), tabl, accessKey,
					rubrique.getRubrique().getNutm(), this.salary.getNlot(), this.salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), colToRead, rubrique.getRubrique().getComp_id().getCrub());
		}
		if (tempNumber == null)
		{
			salary.getParameter().setPbWithCalulation(true);
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90056", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat(), accessKey, rubrique.getRubrique().getTabl()));
			return false;
		}
		amountOrRateValue = tempNumber.doubleValue();
		salary.getValeurRubriquePartage().setValeur(amountOrRateValue);
		if ("M".equals(rubrique.getRubrique().getToum()))
		{
			salary.getValeurRubriquePartage().setRates(0);
			salary.getValeurRubriquePartage().setAmount(amountOrRateValue);
			salary.getValeurRubriquePartage().setBase(amountOrRateValue);
			salary.getValeurRubriquePartage().setBasePlafonnee(amountOrRateValue);
		}
		else if ("T".equals(rubrique.getRubrique().getToum()))
		{
			if (salary.getValeurRubriquePartage().getBase() != 0)
			{
				salary.getValeurRubriquePartage().setRates(amountOrRateValue);
				val = salary.getValeurRubriquePartage().getBase() * salary.getValeurRubriquePartage().getRates() / 100;
				salary.getValeurRubriquePartage().setAmount(val);
			}
			else
			{
				salary.getValeurRubriquePartage().setRates(0);
				salary.getValeurRubriquePartage().setAmount(amountOrRateValue);
				salary.getValeurRubriquePartage().setBase(amountOrRateValue);
				salary.getValeurRubriquePartage().setBasePlafonnee(amountOrRateValue);
			}
		}
		else
		{
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90057", salary.getParameter().getLangue(), salary.getInfoSalary().getComp_id().getNmat(), rubrique.getRubrique().getComp_id().getCrub()));
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo20(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo20(ClsRubriqueClone rubrique)
	{
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo20";
		String cdos = salary.getInfoSalary().getComp_id().getCdos();
		String crub = rubrique.getRubrique().getComp_id().getCrub();
		PretExterneEntete wpaprent = null;
		PretExterneDetail wpaprlig = null;
		double montant = 0;
		double taux = 0;
		double base = 0;
		salary.getValeurRubriquePartage().setAmount(0);
		salary.getValeurRubriquePartage().setBase(0);
		salary.getValeurRubriquePartage().setRates(0);
		// si traitement retroactif alors on prend les donnees dans les cumuls.
		if (salary.getParameter().isUseRetroactif())
		{
			base = salary.getUtilNomenclature().getCumul(salary, salary.getPeriodOfPay(), crub, salary.getNbul(), ClsEnumeration.EnTypeOfColumn.BASE, salary.getMoisPaieCourant(),
					salary.getPeriodOfPay());
			montant = salary.getUtilNomenclature().getCumul(salary, salary.getPeriodOfPay(), crub, salary.getNbul(), ClsEnumeration.EnTypeOfColumn.AMOUNT, salary.getMoisPaieCourant(),
					salary.getPeriodOfPay());
			taux = salary.getUtilNomenclature().getCumul(salary, salary.getPeriodOfPay(), crub, salary.getNbul(), ClsEnumeration.EnTypeOfColumn.RATES, salary.getMoisPaieCourant(),
					salary.getPeriodOfPay());
			//
			salary.getValeurRubriquePartage().setBase(base);
			salary.getValeurRubriquePartage().setBasePlafonnee(base);
			salary.getValeurRubriquePartage().setAmount(montant);
			salary.getValeurRubriquePartage().setRates(taux);
			//
			return true;
		}
		if (ClsObjectUtil.isListEmty(rubrique.getListOfLoanNumber()))
			return true;
		List listOfRhtprentagent = salary.getService().find(
				"from PretExterneEntete" + " where identreprise='" + cdos + "'" + " and nprt = " + rubrique.getListOfLoanNumber().get(0) + " and nmat = '"
						+ salary.getInfoSalary().getComp_id().getNmat() + "'");
		if (ClsObjectUtil.isListEmty(listOfRhtprentagent))
			return true;
		wpaprent = (PretExterneEntete) listOfRhtprentagent.get(0);
		//
		List listOfPretExterneDetail = null;
		// -- LH 210198
		if (salary.getParameter().isStc() && salary.getParameter().getEnBasePretsSTC() == ClsEnumeration.EnLoan.STC)
		{
			// -- Dans le cas d'une radiation, le pret est solde
			// BEGIN
			// SELECT SUM(echo), SUM(echr), SUM(inte), SUM(taxe)
			// INTO wpaprlig.echo, wpaprlig.echr, wpaprlig.inte, wpaprlig.taxe
			// FROM paprlig
			// WHERE cdos = PA_CALCUL.wpdos.cdos
			// AND nprt = wpaprent.nprt
			// AND TO_CHAR(perb,'YYYYMM') >= w_aamm;
			// EXCEPTION
			// WHEN NO_DATA_FOUND THEN
			// wpaprlig.echo := 0;
			// wpaprlig.echr := 0;
			// wpaprlig.inte := 0;
			// wpaprlig.taxe := 0;
			// END;
			wpaprlig = new PretExterneDetail();
			//
			listOfPretExterneDetail = salary.getService().find(
					"select echo, echr, inte, taxe, perb from PretExterneDetail" + " where identreprise='" + cdos + "'" + " and nprt = " + wpaprent.getNprt());
			Object[] row = null;
			double echo = 0;
			double echr = 0;
			double inte = 0;
			double taxe = 0;
			Date perb = null;
			if (ClsObjectUtil.isListEmty(listOfPretExterneDetail))
			{
				wpaprlig.setEcho(new BigDecimal(0));
				wpaprlig.setEchr(new BigDecimal(0));
				wpaprlig.setInte(new BigDecimal(0));
				wpaprlig.setTaxe(new BigDecimal(0));
			}
			else
			{
				for (Object prlig : listOfPretExterneDetail)
				{
					row = (Object[]) prlig;
					perb = (Date) row[4];
					if (new ClsDate(perb, this.salary.getParameter().getAppDateFormat()).getYearAndMonth().compareTo(salary.getPeriodOfPay()) >= 0)
					{
						echo += ((BigDecimal) row[0]).doubleValue();
						echr += ((BigDecimal) row[1]).doubleValue();
						inte += ((BigDecimal) row[2]).doubleValue();
						taxe += ((BigDecimal) row[3]).doubleValue();
					}
				}
				//
				wpaprlig.setEcho(new BigDecimal(echo));
				wpaprlig.setEchr(new BigDecimal(echr));
				wpaprlig.setInte(new BigDecimal(inte));
				wpaprlig.setTaxe(new BigDecimal(taxe));
			}
		}
		else
		{
			// -- Lecture de la mensualite
			// BEGIN
			// SELECT * INTO wpaprlig FROM paprlig
			// WHERE cdos = PA_CALCUL.wpdos.cdos
			// AND nprt = wpaprent.nprt
			// AND TO_CHAR(perb,'YYYYMM') = w_aamm;
			// EXCEPTION
			// WHEN NO_DATA_FOUND THEN
			// wpaprlig.echo := 0;
			// wpaprlig.echr := 0;
			// wpaprlig.inte := 0;
			// wpaprlig.taxe := 0;
			// END;
			listOfPretExterneDetail = salary.getService().find(
					"from PretExterneDetail" + " where identreprise='" + wpaprent.getIdEntreprise().intValue() + "'" + " and nprt = " + wpaprent.getNprt());
			PretExterneDetail row = null;
			Date perb = null;
			for (Object prlig : listOfPretExterneDetail)
			{
				row = (PretExterneDetail) prlig;
				perb = row.getPerb();
				if (new ClsDate(perb, this.salary.getParameter().getAppDateFormat()).getYearAndMonth().equals(salary.getPeriodOfPay()))
				{
					wpaprlig = row;
					break;
				}
			}
		}
		montant = 0;
		// tab_gen_crub(1) := paf_lecfnomM(38, PA_CALCUL.t_rub.crub, 1,PA_CALCUL.w_aamm,PA_CALCUL.w_nlot,PA_CALCUL.wsd_fcal1.nbul);
		double[] tablGenCrub = new double[3];
		double[] tablGenTaux = new double[3];
		String cleAccess = "";
		int tabl = 0;
		//
		cleAccess = crub;
		tabl = 38;
		// Rubrique sur laquelle on injecte le capital
		tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,this.salary.getInfoSalary().getComp_id().getCdos(), tabl, cleAccess, 1, this.salary.getNlot(),
				this.salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.AMOUNT, rubrique.getRubrique().getComp_id().getCrub());
		tablGenCrub[0] = tempNumber == null ? 0 : tempNumber.doubleValue();
		// Active la génération de la rubrique saisie en montant 1 (1 = Oui, 0 = Non)
		tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,this.salary.getInfoSalary().getComp_id().getCdos(), tabl, cleAccess, 1, this.salary.getNlot(),
				this.salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.RATES, rubrique.getRubrique().getComp_id().getCrub());
		tablGenTaux[0] = tempNumber == null ? 0 : tempNumber.doubleValue();
		// Rubrique sur laquelle on injecte le montant des intéréts
		tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,this.salary.getInfoSalary().getComp_id().getCdos(), tabl, cleAccess, 2, this.salary.getNlot(),
				this.salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.AMOUNT, rubrique.getRubrique().getComp_id().getCrub());
		tablGenCrub[1] = tempNumber == null ? 0 : tempNumber.doubleValue();
		// Active la génération de la rubrique saisie en montant 2 (1 = Oui, 0 = Non)
		tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,this.salary.getInfoSalary().getComp_id().getCdos(), tabl, cleAccess, 2, this.salary.getNlot(),
				this.salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.RATES, rubrique.getRubrique().getComp_id().getCrub());
		tablGenTaux[1] = tempNumber == null ? 0 : tempNumber.doubleValue();
		// Rubrique sur laquelle on injecte le montant des taxes
		tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,this.salary.getInfoSalary().getComp_id().getCdos(), tabl, cleAccess, 3, this.salary.getNlot(),
				this.salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.AMOUNT, rubrique.getRubrique().getComp_id().getCrub());
		tablGenCrub[2] = tempNumber == null ? 0 : tempNumber.doubleValue();
		// Active la génération de la rubrique saisie en montant 3 (1 = Oui, 0 = Non)
		tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,this.salary.getInfoSalary().getComp_id().getCdos(), tabl, cleAccess, 3, this.salary.getNlot(),
				this.salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.RATES, rubrique.getRubrique().getComp_id().getCrub());
		tablGenTaux[2] = tempNumber == null ? 0 : tempNumber.doubleValue();
		salary.getEvparam().setNprt("");
		salary.getEvparam().setArgu("No ".concat(ClsStringUtil.formatNumber(Integer.valueOf(wpaprent.getNprt()), "000000")));
		//
		for (int i = 0; i < 3; i++)
		{
			if (tablGenTaux[i] != 0)
			{
				if (i == 0)
				{
					// -- Insertion de la rubrique amortissement
					salary.getValeurRubriquePartage().setAmount(wpaprlig.getEcho().doubleValue());
					salary.getValeurRubriquePartage().setBase(wpaprlig.getEcho().doubleValue());
					salary.getValeurRubriquePartage().setRates(0);
				}
				else if (i == 1)
				{
					// -- Insertion de la rubrique interet
					salary.getValeurRubriquePartage().setAmount(wpaprlig.getInte().doubleValue());
					salary.getValeurRubriquePartage().setBase(wpaprent.getResr().doubleValue());
					salary.getValeurRubriquePartage().setRates(wpaprent.getTint().doubleValue());
				}
				else if (i == 2)
				{
					// -- Insertion de la rubrique taxe
					salary.getValeurRubriquePartage().setAmount(wpaprlig.getTaxe().doubleValue());
					salary.getValeurRubriquePartage().setBase(wpaprlig.getInte().doubleValue());
					salary.getValeurRubriquePartage().setRates(wpaprent.getTtax().doubleValue());
				}
				//
				salary.getValeurRubriquePartage().setBasePlafonnee(salary.getValeurRubriquePartage().getBase());
				if(tablGenCrub[i] != 0)
				{
					ClsRubriqueClone rubTemp = salary.findRubriqueClone(ClsStringUtil.formatNumber(tablGenCrub[i],ParameterUtil.formatRubrique));
					rubTemp.insertRubriqueAlgo();
				}
			}
		}
		//
		if ("MP".equals(salary.getParameter().getBasePrets()))
		{
			salary.getValeurRubriquePartage().setBase(wpaprent.getMntp().doubleValue());
		}
		else if ("DRI".equals(salary.getParameter().getBasePrets()))
		{
			salary.getValeurRubriquePartage().setBase(wpaprent.getMntp().doubleValue() - wpaprent.getResr().doubleValue() + wpaprlig.getEchr().doubleValue());
		}
		else if ("DRN".equals(salary.getParameter().getBasePrets()))
		{
			salary.getValeurRubriquePartage().setBase(wpaprent.getMntp().doubleValue() - wpaprent.getResr().doubleValue());
		}
		else if ("RRI".equals(salary.getParameter().getBasePrets()))
		{
			salary.getValeurRubriquePartage().setBase(wpaprent.getResr().doubleValue() - wpaprlig.getEchr().doubleValue());
		}
		else if ("RRN".equals(salary.getParameter().getBasePrets()))
		{
			salary.getValeurRubriquePartage().setBase(wpaprent.getResr().doubleValue());
		}
		else
			salary.getValeurRubriquePartage().setBase(wpaprent.getMntp().doubleValue());
		//
		salary.getValeurRubriquePartage().setBasePlafonnee(salary.getValeurRubriquePartage().getBase());
		salary.getEvparam().setArgu("No ".concat(ClsStringUtil.formatNumber(Integer.valueOf(wpaprent.getNprt()), "000000")));
		salary.getValeurRubriquePartage().setAmount(wpaprlig.getEchr().doubleValue());
		//
		salary.getValeurRubriquePartage().setRates(0);
		//
		if (wpaprent.getNbec() != null && wpaprent.getNbec().doubleValue() != 0)
		{
			if ("NM".equals(salary.getParameter().getBasePretsTaux()))
			{
				salary.getValeurRubriquePartage().setRates(wpaprent.getNbec().doubleValue());
			}
			else if ("DRI".equals(salary.getParameter().getBasePretsTaux()))
			{
				salary.getValeurRubriquePartage().setRates(wpaprent.getNbec().doubleValue() - wpaprent.getNber().doubleValue() + 1);
			}
			else if ("DRN".equals(salary.getParameter().getBasePretsTaux()))
			{
				salary.getValeurRubriquePartage().setRates(wpaprent.getNbec().doubleValue() - wpaprent.getNber().doubleValue());
			}
			else if ("RRI".equals(salary.getParameter().getBasePretsTaux()))
			{
				salary.getValeurRubriquePartage().setRates(wpaprent.getNber().doubleValue() - 1);
			}
			else if ("RRN".equals(salary.getParameter().getBasePretsTaux()))
			{
				salary.getValeurRubriquePartage().setRates(wpaprent.getNber().doubleValue());
			}
			else
				salary.getValeurRubriquePartage().setRates(wpaprent.getNbec().doubleValue());
		}
		//
		//salary.getEvparam().setNprt("No ".concat(wpaprent.getComp_id().getNprt().toString()));
		salary.getEvparam().setNprt(wpaprent.getNprt().toString());
		// enlever le prét traité
		rubrique.getListOfLoanNumber().remove(0);
		//
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo21(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo211(ClsRubriqueClone rubrique)
	{
		// String cdos = salary.getInfoSalary().getComp_id().getCdos();
		// String crub = rubrique.getRubrique().getComp_id().getCrub();
		double montant = 0;
		// double taux = 0;
		double base = 0;
		// divbascg NUMBER(15,0);
		double divbascg = 0;
		//
		if (salary.getParameter().isStc())
		{
			if (salary.getInfoSalary().getDapa() == null)
				salary.getInfoSalary().setDapa(new BigDecimal(0));
			//
			if (salary.getInfoSalary().getDapec() == null)
				salary.getInfoSalary().setDapec(new BigDecimal(0));
			//
			if (salary.getInfoSalary().getDded() == null)
				salary.getInfoSalary().setDded(new BigDecimal(0));
				
				if (salary.getInfoSalary().getJapa() == null)
				salary.getInfoSalary().setJapa(new BigDecimal(0));
				
				if (salary.getInfoSalary().getJapec() == null)
				salary.getInfoSalary().setJapec(new BigDecimal(0));
			//
			base = salary.getValeurRubriquePartage().getBase();
			salary.getValeurRubriquePartage().setBase(base + salary.getInfoSalary().getDapa().doubleValue() + salary.getInfoSalary().getDapec().doubleValue());
			salary.getValeurRubriquePartage().setBasePlafonnee(base + salary.getInfoSalary().getDapa().doubleValue() + salary.getInfoSalary().getDapec().doubleValue());
			//
			// -- Lecture du diviseur de la base conges
			tempNumber = null;
			if (rubrique.getRubrique().getTabl() != null)
			{
				tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,this.salary.getInfoSalary().getComp_id().getCdos(), Integer.valueOf(rubrique.getRubrique().getTabl()),
						salary.getInfoSalary().getCat(), 1, this.salary.getNlot(), this.salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(),
						ClsEnumeration.EnColumnToRead.AMOUNT, rubrique.getRubrique().getComp_id().getCrub());
			}
			if (tempNumber == null || tempNumber.intValue() == 0)
			{
				// logger
				salary.getParameter().setError(
						salary.getParameter().errorMessage("ERR-90062", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
								salary.getInfoSalary().getComp_id().getNmat(), rubrique.getRubrique().getTabl(), salary.getInfoSalary().getCat()));
				return false;
			}
			
			divbascg = tempNumber.doubleValue();
			salary.getValeurRubriquePartage().setRates(salary.getInfoSalary().getJapa().doubleValue() + salary.getInfoSalary().getJapec().doubleValue());
			base = salary.getValeurRubriquePartage().getBase();
			salary.getValeurRubriquePartage().setAmount((base / divbascg) - salary.getInfoSalary().getDded().doubleValue());
			return true;
		}
		// -- ----- Pas de calcul si pas de fictif et WMDP != du premier mois de conges
		
		 if("N".equals(salary.getParameter().getFictiveCalculus()) && (! salary.getPeriodOfPay().equals(salary.getInfoSalary().getPmcf()))){
		 salary.getValeurRubriquePartage().setAmount(0);
		 salary.getValeurRubriquePartage().setBase(0);
		 salary.getValeurRubriquePartage().setBasePlafonnee(0);
		 salary.getValeurRubriquePartage().setRates(0);
					
		 return true;
		 }
		// -- Pas de calcul si pas de conges
		if (salary.getWorkTime().getNbreJourConges() == 0 && salary.getWorkTime().getNbreJourCongesAnnuelMoisSuiv() == 0)
		{
			salary.getValeurRubriquePartage().setAmount(0);
			salary.getValeurRubriquePartage().setBase(0);
			salary.getValeurRubriquePartage().setBasePlafonnee(0);
			salary.getValeurRubriquePartage().setRates(0);
			//
			return true;
		}

		// -- Pas de calcul si montant du conges deja calcule
		/*
		 * if(salary.getWorkTime().getMontantCongePonctuel() != 0){ salary.getValeurRubriquePartage().setAmount(salary.getWorkTime().getMontantCongePonctuel()); return true; }
		 */
		if (salary.getInfoSalary().getDapa() == null)
			salary.getInfoSalary().setDapa(new BigDecimal(0));
		if (salary.getInfoSalary().getDapec() == null)
			salary.getInfoSalary().setDapec(new BigDecimal(0));
		if (salary.getInfoSalary().getDded() == null)
			salary.getInfoSalary().setDded(new BigDecimal(0));
		if (salary.getInfoSalary().getJapa() == null)
			salary.getInfoSalary().setJapa(new BigDecimal(0));
		if (salary.getInfoSalary().getJapec() == null)
			salary.getInfoSalary().setJapec(new BigDecimal(0));
		
		if (salary.getWorkTime().isDebutDeMois())
			salary.getValeurRubriquePartage().setBase(0);
		//
		base = salary.getValeurRubriquePartage().getBase();
		salary.getValeurRubriquePartage().setBase(base + salary.getInfoSalary().getDapa().doubleValue() + salary.getInfoSalary().getDapec().doubleValue());
		base = salary.getValeurRubriquePartage().getBase();
		salary.getValeurRubriquePartage().setBasePlafonnee(base);
		// -- Lecture du diviseur de la base conges
		tempNumber = null;
		if (rubrique.getRubrique().getTabl() != null)
		{
			tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,salary.getInfoSalary().getComp_id().getCdos(), Integer.valueOf(rubrique.getRubrique().getTabl()),
					salary.getInfoSalary().getCat(), 1, salary.getNlot(), salary.getNbul(), salary.getMoisPaieCourant(), salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.AMOUNT, rubrique.getRubrique().getComp_id().getCrub());
		}
		if (tempNumber == null || tempNumber.intValue() == 0)
		{
			// logger
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90062", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat(), rubrique.getRubrique().getTabl(), salary.getInfoSalary().getCat()));
			return false;
		}
		divbascg = tempNumber.doubleValue();
		if ("N".equals(salary.getParameter().getFictiveCalculus()))
		{
			if (salary.getPeriodOfPay().equals(salary.getInfoSalary().getPmcf()))
				salary.getWorkTime().setNbreJourConges(salary.getInfoSalary().getNbjcf().intValue());
			else
				salary.getWorkTime().setNbreJourConges(0);
		}
		salary.getValeurRubriquePartage().setRates(salary.getWorkTime().getNbreJourConges() + salary.getWorkTime().getNbreJourCongesNonPris());
		base = salary.getValeurRubriquePartage().getBase();
		///------------le fractionnement doit se gérer é partir d'ici.
		base = (base / divbascg) - salary.getInfoSalary().getDded().doubleValue() - salary.getWorkTime().getMontantCongePonctuel();
		salary.getValeurRubriquePartage().setAmount(base);
		//
		if (salary.getInfoSalary().getNbjaf() != null && salary.getInfoSalary().getNbjaf().doubleValue() != 0 && "O".equals(salary.getParameter().getFictiveCalculus()))
		{
			montant = salary.getValeurRubriquePartage().getAmount();
			montant = montant * (salary.getValeurRubriquePartage().getRates() / salary.getInfoSalary().getNbjaf().doubleValue());
			salary.getValeurRubriquePartage().setAmount(montant);
		}
		
//		if (salary.getInfoSalary().getNbjcf() != null && salary.getInfoSalary().getNbjcf().doubleValue() != 0 && "O".equals(salary.getParameter().getFictiveCalculus()))
//		{
//			montant = salary.getValeurRubriquePartage().getAmount();
//			montant = montant * (salary.getValeurRubriquePartage().getRates() / salary.getInfoSalary().getNbjcf().doubleValue());
//			salary.getValeurRubriquePartage().setAmount(montant);
//		}
		
		return true;
	}

	public boolean algo21(ClsRubriqueClone rubrique)
	{
		double montant = 0;
		double base = 0;
		double divbascg = 0;
		if (salary.getParameter().isStc())
		{
			if (salary.getInfoSalary().getDapa() == null) salary.getInfoSalary().setDapa(new BigDecimal(0));
			if (salary.getInfoSalary().getDapec() == null) salary.getInfoSalary().setDapec(new BigDecimal(0));
			if (salary.getInfoSalary().getDded() == null) salary.getInfoSalary().setDded(new BigDecimal(0));		
			if (salary.getInfoSalary().getJapa() == null) salary.getInfoSalary().setJapa(new BigDecimal(0));	
			if (salary.getInfoSalary().getJapec() == null)salary.getInfoSalary().setJapec(new BigDecimal(0));
			
			base = salary.getValeurRubriquePartage().getBase();
			salary.getValeurRubriquePartage().setBase(base + salary.getInfoSalary().getDapa().doubleValue() + salary.getInfoSalary().getDapec().doubleValue());
			salary.getValeurRubriquePartage().setBasePlafonnee(base + salary.getInfoSalary().getDapa().doubleValue() + salary.getInfoSalary().getDapec().doubleValue());
			
			// -- Lecture du diviseur de la base conges
			tempNumber = null;
			if (rubrique.getRubrique().getTabl() != null)
			{
				tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,
						this.salary.getInfoSalary().getComp_id().getCdos(), Integer.valueOf(rubrique.getRubrique().getTabl()), salary.getInfoSalary().getCat(), 1,
						this.salary.getNlot(), this.salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.AMOUNT, rubrique.getRubrique().getComp_id().getCrub());
			}
			if (tempNumber == null || tempNumber.intValue() == 0)
			{
				salary.getParameter().setError(
						salary.getParameter().errorMessage("ERR-90062", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(),
								rubrique.getRubrique().getComp_id().getCrub(), salary.getInfoSalary().getComp_id().getNmat(), rubrique.getRubrique().getTabl(),
								salary.getInfoSalary().getCat()));
				return false;
			}
			
			divbascg = tempNumber.doubleValue();
			salary.getValeurRubriquePartage().setRates(salary.getInfoSalary().getJapa().doubleValue() + salary.getInfoSalary().getJapec().doubleValue());
			base = salary.getValeurRubriquePartage().getBase();
			salary.getValeurRubriquePartage().setAmount((base / divbascg) - salary.getInfoSalary().getDded().doubleValue());
			return true;
		}
		// -- ----- Pas de calcul si pas de fictif et WMDP != du premier mois de conges
		
		 if("N".equals(salary.getParameter().getFictiveCalculus()) && (! salary.getPeriodOfPay().equals(salary.getInfoSalary().getPmcf())))
		 {
			 salary.getValeurRubriquePartage().setAmount(0);
			 salary.getValeurRubriquePartage().setBase(0);
			 salary.getValeurRubriquePartage().setBasePlafonnee(0);
			 salary.getValeurRubriquePartage().setRates(0);
			 return true;
		 }
		// -- Pas de calcul si pas de conges
		if (salary.getWorkTime().getNbreJourConges() == 0 && salary.getWorkTime().getNbreJourCongesAnnuelMoisSuiv() == 0)
		{
			salary.getValeurRubriquePartage().setAmount(0);
			salary.getValeurRubriquePartage().setBase(0);
			salary.getValeurRubriquePartage().setBasePlafonnee(0);
			salary.getValeurRubriquePartage().setRates(0);
			//
			return true;
		}

		// -- Pas de calcul si montant du conges deja calcule
		/*
		 * if(salary.getWorkTime().getMontantCongePonctuel() != 0){ salary.getValeurRubriquePartage().setAmount(salary.getWorkTime().getMontantCongePonctuel()); return true; }
		 */
		if (salary.getInfoSalary().getDapa() == null) salary.getInfoSalary().setDapa(new BigDecimal(0));
		if (salary.getInfoSalary().getDapec() == null) salary.getInfoSalary().setDapec(new BigDecimal(0));
		if (salary.getInfoSalary().getDded() == null) salary.getInfoSalary().setDded(new BigDecimal(0));
		if (salary.getInfoSalary().getJapa() == null) salary.getInfoSalary().setJapa(new BigDecimal(0));
		if (salary.getInfoSalary().getJapec() == null) salary.getInfoSalary().setJapec(new BigDecimal(0));
		
		if (salary.getWorkTime().isDebutDeMois()) salary.getValeurRubriquePartage().setBase(0);
		//
		base = salary.getValeurRubriquePartage().getBase();
		salary.getValeurRubriquePartage().setBase(base + salary.getInfoSalary().getDapa().doubleValue() + salary.getInfoSalary().getDapec().doubleValue());
		base = salary.getValeurRubriquePartage().getBase();
		salary.getValeurRubriquePartage().setBasePlafonnee(base);
		// -- Lecture du diviseur de la base conges
		tempNumber = null;
		if (rubrique.getRubrique().getTabl() != null)
		{
			tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,salary.getInfoSalary().getComp_id().getCdos(), Integer.valueOf(rubrique.getRubrique().getTabl()),
					salary.getInfoSalary().getCat(), 1, salary.getNlot(), salary.getNbul(), salary.getMoisPaieCourant(), salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.AMOUNT, rubrique.getRubrique().getComp_id().getCrub());
		}
		if (tempNumber == null || tempNumber.intValue() == 0)
		{
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90062", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat(), rubrique.getRubrique().getTabl(), salary.getInfoSalary().getCat()));
			return false;
		}
		divbascg = tempNumber.doubleValue();
		if ("N".equals(salary.getParameter().getFictiveCalculus()))
		{
			if (salary.getPeriodOfPay().equals(salary.getInfoSalary().getPmcf()))
				salary.getWorkTime().setNbreJourConges(salary.getInfoSalary().getNbjcf().intValue());
			else
				salary.getWorkTime().setNbreJourConges(0);
		}
		salary.getValeurRubriquePartage().setRates(salary.getWorkTime().getNbreJourConges() + salary.getWorkTime().getNbreJourCongesNonPris());
		base = salary.getValeurRubriquePartage().getBase();
		
		///------------le fractionnement doit se gérer é partir d'ici.
		boolean fractionConge = false;
		double droitsRestant = 0;
		String cdos = salary.parameter.dossier;
		String nmat = salary.infoSalary.getComp_id().getNmat();
		List<String> lst = salary.service.find("Select vall From ParamData where identreprise='"+cdos+"' and ctab = 99  and cacc = 'FRACTIONCG' and nume = 2");
		if(!lst.isEmpty() && StringUtils.isNotBlank(lst.get(0)))
			fractionConge = StringUtils.equals("O", lst.get(0));
		
		if(fractionConge)
		{
			double nbrJoursPris = salary.valeurRubriquePartage.getRates();
			double nbrJoursAcquis = salary.infoSalary.getJapa().add(salary.infoSalary.getJapec()).doubleValue();
			double droitsAcquis = base;
			if(nbrJoursPris>nbrJoursAcquis)
			{
					salary.parameter.setError(
							salary.parameter.errorMessage("Algo %1, Rub. %2 Sal. %3, Nombre de jours pris (%4) suppérieur au nombre Acquis (%5)", salary.parameter.getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
									salary.getInfoSalary().getComp_id().getNmat(), nbrJoursPris+"", nbrJoursAcquis+""));
					return false;
			}
			if (nbrJoursAcquis <= 0)
			{
				salary.parameter.setError(
						salary.parameter.errorMessage("Algo %1, Rub. %2 Sal. %3, Nombre de jours Acquis (%4) incorrect (négatif)", salary.parameter.getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
								salary.getInfoSalary().getComp_id().getNmat(), nbrJoursAcquis+""));
				return false;
			}
			
			base = droitsAcquis * nbrJoursPris / nbrJoursAcquis;
			droitsRestant = droitsAcquis - base;
			
			String query="Update Rhpagent set zli10 = '"+droitsRestant+"' where identreprise='"+cdos+"' and nmat = '"+nmat+"'";
			try
			{
				salary.service.updateFromTable(query);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				salary.parameter.setError(salary.parameter.errorMessage(ClsTreater._getStackTrace(e), salary.parameter.getLangue()));
			}
		}
		
		base = (base / divbascg) - salary.getInfoSalary().getDded().doubleValue() - salary.getWorkTime().getMontantCongePonctuel();
		salary.getValeurRubriquePartage().setAmount(base);
		//
		if (salary.getInfoSalary().getNbjaf() != null && salary.getInfoSalary().getNbjaf().doubleValue() != 0 && "O".equals(salary.getParameter().getFictiveCalculus()))
		{
			montant = salary.getValeurRubriquePartage().getAmount();
			montant = montant * (salary.getValeurRubriquePartage().getRates() / salary.getInfoSalary().getNbjaf().doubleValue());
			salary.getValeurRubriquePartage().setAmount(montant);
		}
		
//		if (salary.getInfoSalary().getNbjcf() != null && salary.getInfoSalary().getNbjcf().doubleValue() != 0 && "O".equals(salary.getParameter().getFictiveCalculus()))
//		{
//			montant = salary.getValeurRubriquePartage().getAmount();
//			montant = montant * (salary.getValeurRubriquePartage().getRates() / salary.getInfoSalary().getNbjcf().doubleValue());
//			salary.getValeurRubriquePartage().setAmount(montant);
//		}

		return true;
	}
	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo22(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo22(ClsRubriqueClone rubrique)
	{
		if (salary.getWorkTime().getNbreJourCongesPonctuels() == 0)
			salary.getValeurRubriquePartage().setBase(0);
		else
		{
			salary.getValeurRubriquePartage().setRates(salary.getWorkTime().getNbreJourAbsencePourCongesPonctuels());
			if (salary.getWorkTime().getMontantCongePonctuel() != 0)
				salary.getValeurRubriquePartage().setAmount(salary.getWorkTime().getMontantCongePonctuel());
			else
			{
				salary.getValeurRubriquePartage().setAmount(salary.getValeurRubriquePartage().getBase() * salary.getValeurRubriquePartage().getRates() / 30);
				salary.getWorkTime().setMontantCongePonctuel(salary.getValeurRubriquePartage().getAmount());
			}
		}
		// END IF;
		//
		// -- MM 11/09/2000
		// -- w_tau := PA_CALCUL.wnbjcp;
		salary.getValeurRubriquePartage().setRates(salary.getWorkTime().getNbreJourAbsencePourCongesPonctuels());
		//
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo23(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo23(ClsRubriqueClone rubrique)
	{
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo23";
		String cdos = salary.getInfoSalary().getComp_id().getCdos();
		String crub = rubrique.getRubrique().getComp_id().getCrub();
		double montant = 0;
		double taux = 0;
		double base = 0;

		double A23_inter1 = 0;
		//
		// CURSOR curs_barem8 IS
		// SELECT cdos , crub , val1 , val2 , NVL(taux, 0) , NVL(mont, 0)
		// FROM parubbarem
		// WHERE cdos = PA_CALCUL.wpdos.cdos
		// AND crub = PA_CALCUL.t_rub.crub
		// ORDER BY cdos, crub, nume;
		// RhprubriquePK t_rubPK = this.rubriquePKFromEngine;
		// String queryString = " from ElementSalaireBareme "
		// + " where identreprise='" + cdos + "'"
		// + " and crub = '" + crub + "'"
		// + " order by cdos , crub , nume";
		//
		// CURSOR curs_barem82 IS
		// SELECT cdos , crub , val1 , val2 , NVL(taux, 0) , NVL(mont, 0)
		// FROM pahrubbarem
		// WHERE cdos = PA_CALCUL.wpdos.cdos
		// AND crub = PA_CALCUL.t_rub.crub
		// AND aamm = PA_CALCUL.w_aamm
		// AND nbul = PA_CALCUL.wsd_fcal1.nbul
		// ORDER BY cdos, crub, nume;
		// String queryStringRetro = " from Rhthrubbarem "
		// + " where identreprise='" + cdos + "'"
		// + " and crub = '" + crub + "'"
		// + " and aamm = '" + salary.getPeriodOfPay() + "'"
		// + " and nbul = " + salary.getNbul()
		// + " order by cdos , crub , nume";
		//
		salary.getValeurRubriquePartage().setAmount(0);
		salary.getValeurRubriquePartage().setInter(salary.getValeurRubriquePartage().getBase());
		salary.getValeurRubriquePartage().setRates(0);
		//
		// -- Base <= 0 : Pas de calcul
		if (salary.getValeurRubriquePartage().getInter() <= 0)
			return true;
		// -- Abattement en pourcentage
		if (!ClsObjectUtil.isNull(rubrique.getRubrique().getPcab()))
		{
			base = salary.getValeurRubriquePartage().getBase();
			base = base * rubrique.getRubrique().getPcab().doubleValue() / 100;
			salary.getValeurRubriquePartage().setAbattement(base);
			salary.getValeurRubriquePartage().setInter(salary.getValeurRubriquePartage().getBase() - salary.getValeurRubriquePartage().getAbattement());
		}
		//
		// -- Division par le nombre de parts
		// -- LH 130899
		if ("O".equals(rubrique.getRubrique().getDnbp()))
		{
			if (ClsObjectUtil.isNull(salary.getInfoSalary().getNbpt()))
			{
				// logger
				salary.getParameter().setError(
						salary.getParameter().errorMessage("ERR-90063", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), 
								salary.getInfoSalary().getComp_id().getNmat(),rubrique.getRubrique().getComp_id().getCrub()));
				salary.getParameter().setPbWithCalulation(true);
				return false;
			}
			base = salary.getValeurRubriquePartage().getInter();
			base = base / salary.getInfoSalary().getNbpt().doubleValue();
			salary.getValeurRubriquePartage().setInter(base);
		}
		//
		// -- Abattement en montant
		base = rubrique.convertToNumber(rubrique.getRubrique().getAbat());
		if (salary.getParameter().isPbWithCalulation())
		{
			return false;
		}
		salary.getValeurRubriquePartage().setPlafond(base);
		base = salary.getValeurRubriquePartage().getInter() - salary.getValeurRubriquePartage().getPlafond();
		salary.getValeurRubriquePartage().setInter(base);
		//
		// -- Base <= 0 : Pas de calcul
		if (salary.getValeurRubriquePartage().getInter() <= 0)
		{
			salary.getValeurRubriquePartage().setBase(0);
			return true;
		}
		// -- Recherche du montant ET du taux a appliquer
		int nbreBaremeRubrique = 0;
		boolean ok = false;
		String valeur1 = "";
		String valeur2 = "";
		double dblValeur1 = 0;
		double dblValeur2 = 0;
		//Rhthrubbarem oRhthrubbarem = null;
		ElementSalaireBareme oElementSalaireBareme = null;
		// List listOfBarem = salary.getParameter().isUseRetroactif() ? salary.getService().find(queryStringRetro) : salary.getService().find(queryString);
		String keyOfBaremeList = salary.getParameter().isUseRetroactif() ? crub + this.salary.getPeriodOfPay() + this.salary.getNbul() : crub;
		List listOfBarem = (List) salary.getParameter().getListOfRubriquebaremeMap().get(keyOfBaremeList);
		if (listOfBarem != null)
			for (Object barem : listOfBarem)
			{
				if (salary.getParameter().isUseRetroactif())
				{
//					oRhthrubbarem = (Rhthrubbarem) barem;
//					montant = oRhthrubbarem.getMont().doubleValue();
//					taux = oRhthrubbarem.getTaux().doubleValue();
//					valeur1 = oRhthrubbarem.getVal1();
//					valeur2 = oRhthrubbarem.getVal2();
				}
				else
				{
					oElementSalaireBareme = (ElementSalaireBareme) barem;
//					montant = oElementSalaireBareme.getMont().doubleValue();
//					taux = oElementSalaireBareme.getTaux().doubleValue();
//					valeur1 = oElementSalaireBareme.getVal1();
//					valeur2 = oElementSalaireBareme.getVal2();
					montant = rubrique.getTauxOuMontantBareme(oElementSalaireBareme, false);
					taux = rubrique.getTauxOuMontantBareme(oElementSalaireBareme, true);
					valeur1 = rubrique.getValeurBareme(oElementSalaireBareme, true);
					valeur2 = rubrique.getValeurBareme(oElementSalaireBareme, false);
				}
				//
				nbreBaremeRubrique++;
				if (nbreBaremeRubrique > 50)
				{
					nbreBaremeRubrique = 50;
					break;
				}
				//
				dblValeur1 = rubrique.convertToNumber(valeur1);
				if (salary.getParameter().isPbWithCalulation())
				{
					return false;
				}
				dblValeur2 = rubrique.convertToNumber(valeur2);
				if (salary.getParameter().isPbWithCalulation())
				{
					return false;
				}
				if (salary.getValeurRubriquePartage().getBase() >= dblValeur1 && salary.getValeurRubriquePartage().getBase() <= dblValeur2)
				{
					ok = true;
					salary.getValeurRubriquePartage().setAmount(montant);
					break;
				}
				if (salary.getValeurRubriquePartage().getInter() >= dblValeur1 && salary.getValeurRubriquePartage().getInter() <= dblValeur2)
				{
					ok = true;
					break;
				}
			}// foreach
		if (nbreBaremeRubrique == 0)
		{
			// logger
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90061", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat()));
			return false;
		}
		double inter1 = 0;
		if (ok == true)
		{
			salary.getValeurRubriquePartage().setRates(taux);
			// w_inter1 := w_bas * w_tau / 100;
			inter1 = salary.getValeurRubriquePartage().getBase() * salary.getValeurRubriquePartage().getRates() / 100;
			//
			// -- LH 130899
			A23_inter1 = inter1;
			if ("O".equals(rubrique.getRubrique().getDnbp()))
				salary.getValeurRubriquePartage().setInter(montant * salary.getInfoSalary().getNbpt().doubleValue());
			else
				salary.getValeurRubriquePartage().setInter(montant);
			// -- LH 130899
			if (salary.getParameter().getDossierInfo().getNddd() == 0)
				salary.getValeurRubriquePartage().setAmount(inter1 - salary.getValeurRubriquePartage().getInter());
			else
				salary.getValeurRubriquePartage().setAmount(A23_inter1 - salary.getValeurRubriquePartage().getInter());
		}
		else
		{
			// logger
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90064", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat()));
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo24(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo24(ClsRubriqueClone rubrique)
	{
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo24";
		String cdos = salary.getInfoSalary().getComp_id().getCdos();
		String crub = rubrique.getRubrique().getComp_id().getCrub();
		double montant = 0;
		double taux = 0;
		double base = 0;
		int ind_f = 0;
		double inter1 = 0;
		double inter2 = 0;
		double inter3 = 0;
		String c_cum99 = "";
		//
		// String queryString = " from ElementSalaireBareme "
		// + " where identreprise='" + cdos + "'"
		// + " and crub = '" + crub + "'"
		// + " order by cdos , crub , nume";
		// //
		// String queryStringRetro = " from Rhthrubbarem "
		// + " where identreprise='" + cdos + "'"
		// + " and crub = '" + crub + "'"
		// + " and aamm = '" + salary.getPeriodOfPay() + "'"
		// + " and nbul = " + salary.getNbul()
		// + " order by cdos , crub , nume";
		//
		salary.getValeurRubriquePartage().setAmount(0);
		salary.getValeurRubriquePartage().setRates(0);
		//
		// -- Prorata sur periode
		// -- Recherche du nombre de mois travailles
		int periodRegu = 0;
		int nbrePeriod = 0;
		if (salary.getParameter().getNbrePeriodeRegularisationEv() <= 0)
		{
			ClsDate oClsDate = new ClsDate(salary.getParameter().getDossierInfo().getDdex(), this.salary.getParameter().getAppDateFormat());
			c_cum99 = String.valueOf(oClsDate.getYear()) + "99";
			//
			// SELECT count(*) INTO per_regu FROM pacumu
			// WHERE cdos = PA_CALCUL.wpdos.cdos
			// AND nmat = PA_CALCUL.wsal01.nmat
			// AND aamm >= w_prem
			// AND aamm < w_aamm
			// AND aamm != c_cum99
			// AND rubq = PA_CALCUL.w_rubbrut
			// AND nbul = 9;
			//

			List l = salary.getService().find(
					"select count(*) from CumulPaie" + " where identreprise='" + cdos + "'" + " and nmat = '" + salary.getInfoSalary().getComp_id().getNmat() + "'" + " and aamm >= '"
							+ oClsDate.getYearAndMonth() + "'" + " and aamm < '" + oClsDate.getYearAndMonth() + "'" + " and aamm != '" + c_cum99 + "'" + " and rubq = '"
							+ salary.getParameter().getBrutRubrique() + "'" + " and nbul = 9");
			if (!ClsObjectUtil.isListEmty(l))
			{
				periodRegu = ((Long) l.get(0)).intValue();
				salary.getParameter().setNbrePeriodeRegularisationEv(periodRegu);
			}
			nbrePeriod = periodRegu + 1;
		}
		else
			nbrePeriod = salary.getParameter().getNbrePeriodeRegularisationEv();
		//
		if (nbrePeriod != 0)
		{
			base = (base / nbrePeriod) * 12;
			salary.getValeurRubriquePartage().setBase(base);
		}
		salary.getValeurRubriquePartage().setInter(salary.getValeurRubriquePartage().getBase());
		if (salary.getValeurRubriquePartage().getInter() <= 0)
		{
			salary.getValeurRubriquePartage().setBase(0);
			return true;
		}
		//
		inter2 = 0;
		if (!ClsObjectUtil.isNull(rubrique.getRubrique().getPmin()))
			ind_f = Integer.valueOf(rubrique.getRubrique().getPmin());
		else
			ind_f = 0;
		if (ind_f > 0 && ind_f <= 9999)
			inter2 = rubrique.getAmount();
		if (!ClsObjectUtil.isNull(rubrique.getRubrique().getPmax()))
			inter3 = Integer.valueOf(rubrique.getRubrique().getPmax());
		else
			inter3 = 0;
		if ((salary.getValeurRubriquePartage().getInter() - inter2) < inter3)
		{
			salary.getValeurRubriquePartage().setBase(0);
			return true;
		}
		// -- Recherche du montant ET du taux a appliquer
		int nbreBaremeRubrique = 0;
		boolean ok = false;
		String valeur1 = "";
		String valeur2 = "";
		double dblValeur1 = 0;
		double dblValeur2 = 0;
		//Rhthrubbarem oRhthrubbarem = null;
		ElementSalaireBareme oElementSalaireBareme = null;
		// List listOfBarem = salary.getParameter().isUseRetroactif() ? salary.getService().find(queryStringRetro) : salary.getService().find(queryString);
		String keyOfBaremeList = salary.getParameter().isUseRetroactif() ? crub + this.salary.getPeriodOfPay() + this.salary.getNbul() : crub;
		List listOfBarem = (List) salary.getParameter().getListOfRubriquebaremeMap().get(keyOfBaremeList);
		if (listOfBarem != null)
			for (Object barem : listOfBarem)
			{
				if (salary.getParameter().isUseRetroactif())
				{
//					oRhthrubbarem = (Rhthrubbarem) barem;
//					montant = oRhthrubbarem.getMont().doubleValue();
//					taux = oRhthrubbarem.getTaux().doubleValue();
//					valeur1 = oRhthrubbarem.getVal1();
//					valeur2 = oRhthrubbarem.getVal2();
				}
				else
				{
					oElementSalaireBareme = (ElementSalaireBareme) barem;
//					montant = oElementSalaireBareme.getMont().doubleValue();
//					taux = oElementSalaireBareme.getTaux().doubleValue();
//					valeur1 = oElementSalaireBareme.getVal1();
//					valeur2 = oElementSalaireBareme.getVal2();
					montant = rubrique.getTauxOuMontantBareme(oElementSalaireBareme, false);
					taux = rubrique.getTauxOuMontantBareme(oElementSalaireBareme, true);
					valeur1 = rubrique.getValeurBareme(oElementSalaireBareme, true);
					valeur2 = rubrique.getValeurBareme(oElementSalaireBareme, false);
				}
				//
				nbreBaremeRubrique++;
				if (nbreBaremeRubrique > 50)
				{
					nbreBaremeRubrique = 50;
					break;
				}
				//
				dblValeur1 = rubrique.convertToNumber(valeur1);
				if (salary.getParameter().isPbWithCalulation())
				{
					return false;
				}
				dblValeur2 = rubrique.convertToNumber(valeur2);
				if (salary.getParameter().isPbWithCalulation())
				{
					return false;
				}
				if (salary.getValeurRubriquePartage().getBase() >= dblValeur1 && salary.getValeurRubriquePartage().getBase() <= dblValeur2)
				{
					ok = true;
					salary.getValeurRubriquePartage().setAmount(montant);
					break;
				}
				if (salary.getValeurRubriquePartage().getInter() >= dblValeur1 && salary.getValeurRubriquePartage().getInter() <= dblValeur2)
				{
					ok = true;
					break;
				}
			}// foreach
		if (nbreBaremeRubrique == 0)
		{
			// logger
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90061", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat()));
			return false;
		}
		if (ok == true)
		{
			salary.getValeurRubriquePartage().setRates(taux);
			// fixer le taux
			inter1 = salary.getValeurRubriquePartage().getBase() * salary.getValeurRubriquePartage().getRates() / 100;
			//
			if ("O".equals(rubrique.getRubrique().getDnbp()))
				salary.getValeurRubriquePartage().setInter(montant * salary.getInfoSalary().getNbpt().doubleValue());
			else
				salary.getValeurRubriquePartage().setInter(montant);
			// fixer le montant
			salary.getValeurRubriquePartage().setAmount(inter1 - salary.getValeurRubriquePartage().getInter());
		}
		//
		salary.getValeurRubriquePartage().setAmount((salary.getValeurRubriquePartage().getAmount() / 12) - salary.getValeurRubriquePartage().getInter());

		salary.getValeurRubriquePartage().setInter(0);
		ind_f = 0;
		if (!ClsObjectUtil.isNull(rubrique.getRubrique().getAbat()))
			ind_f = Integer.valueOf(rubrique.getRubrique().getAbat());
		if (ind_f > 0 && ind_f <= 9999)
			salary.getValeurRubriquePartage().setInter(rubrique.getAmount());
		if (salary.getValeurRubriquePartage().getAmount() > 0)
			salary.getValeurRubriquePartage().setAmount(salary.getValeurRubriquePartage().getAmount() - salary.getValeurRubriquePartage().getInter());
		else
			salary.getValeurRubriquePartage().setAmount(0);
		//
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo25(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo25(ClsRubriqueClone rubrique)
	{
		String cdos = salary.getInfoSalary().getComp_id().getCdos();
		String crub = rubrique.getRubrique().getComp_id().getCrub();
		double montant = 0;
		double taux = 0;
		double base = 0;
		String cleAccess = "";
		int tabl = 0;
		String libelle = "";
		// double w4 = 0;
		//
		salary.getValeurRubriquePartage().setRates(0);
		salary.getValeurRubriquePartage().setAmount(0);
		salary.getValeurRubriquePartage().setValeur(0);
		if (rubrique.getRubrique().getTabl() != null)
		{
			cleAccess = crub;
			tabl = Integer.valueOf(rubrique.getRubrique().getTabl());
			libelle = this.getLabelFromNomenclature(cdos, tabl, cleAccess, 3, salary.getNlot(), salary.getNbul(), this.salary.getMoisPaieCourant(),
					this.salary.getPeriodOfPay(), rubrique.getRubrique().getComp_id().getCrub());
		}
		if (ClsObjectUtil.isNull(libelle))
		{
			// logger
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90056", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat(), cleAccess, rubrique.getRubrique().getTabl()));
			return false;
		}
		tempNumber = null;
		if (rubrique.getRubrique().getTabl() != null)
		{
			tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,salary.getInfoSalary().getComp_id().getCdos(), tabl, cleAccess, 1, salary.getNlot(), salary.getNbul(),
					salary.getMoisPaieCourant(), salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.AMOUNT, rubrique.getRubrique().getComp_id().getCrub());
		}
		if (tempNumber == null)
		{
			// logger
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90056", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat(), cleAccess, rubrique.getRubrique().getTabl()));
			return false;
		}
		
		if (tempNumber.intValue() == 0)
		{
			// logger
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90065", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat(), cleAccess, rubrique.getRubrique().getTabl()));
			return false;
		}
		montant = tempNumber.doubleValue();
		char char1 = libelle.toCharArray()[0];
		if (char1 != 'B' && char1 != 'T' && char1 != 'M')
		{
			// logger
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90066", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat(), crub, rubrique.getRubrique().getTabl()));
			return false;
		}
		// prendre les valeurs de taux, montant et base associées é la rubrique correspondant au libelle
		// ClsRubriqueClone rubriqueTmp = salary.findRubriqueClone(String.valueOf(libelle));
		String newCrub = ClsStringUtil.formatNumber(montant, ParameterUtil.formatRubrique);
		ClsRubriqueClone rubriqueTmp = salary.findRubriqueClone(newCrub);

		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "rubrique trouvée = " + rubriqueTmp;
		if (char1 == 'B')
			salary.getValeurRubriquePartage().setRates(rubriqueTmp.getBasePlafonnee());
		else if (char1 == 'T')
			salary.getValeurRubriquePartage().setRates(rubriqueTmp.getRates());
		else if (char1 == 'M')
			salary.getValeurRubriquePartage().setRates(rubriqueTmp.getAmount());
		//
		if (salary.getValeurRubriquePartage().getRates() != 0)
		{
			if ("T".equals(rubrique.getRubrique().getToum()))
			{
				// w_mon := w_bas * w_tau / 100;
				taux = salary.getValeurRubriquePartage().getRates();
				base = salary.getValeurRubriquePartage().getBase();
				salary.getValeurRubriquePartage().setAmount(taux * base / 100);
			}
			else if ("M".equals(rubrique.getRubrique().getToum()))
			{
				// w_mon := w_bas * w_tau;
				taux = salary.getValeurRubriquePartage().getRates();
				base = salary.getValeurRubriquePartage().getBase();
				salary.getValeurRubriquePartage().setAmount(taux * base);
			}
			else if ("D".equals(rubrique.getRubrique().getToum()))
			{
				// w_mon := w_bas / w_tau;
				taux = salary.getValeurRubriquePartage().getRates();
				base = salary.getValeurRubriquePartage().getBase();
				if (taux != 0)
					salary.getValeurRubriquePartage().setAmount(base / taux);
			}
			else
			{
				// logger
				salary.getParameter().setError(
						salary.getParameter().errorMessage("ERR-90058", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), salary.getInfoSalary().getComp_id().getNmat(),
								rubrique.getRubrique().getComp_id().getCrub()));
				if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + salary.getParameter().getError();
				return false;
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo26(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo26(ClsRubriqueClone rubrique)
	{
		// String cdos = salary.getInfoSalary().getComp_id().getCdos();
		// String crub = rubrique.getRubrique().getComp_id().getCrub();
		double montant = 0;
		// double taux = 0;
		// double base = 0;
		// String cleAccess = "";
		// int tabl = 0;
		// String libelle = "";
		double div_nbjs = 0;
		//
		salary.getValeurRubriquePartage().setRates(0);
		salary.getValeurRubriquePartage().setAmount(0);
		salary.getValeurRubriquePartage().setValeur(0);
		//
		// -- Pas de calcul si pas de base ( Base := Montant du conges normal )
		// -- Pas de calcul si pas de jours supp
		if (salary.getValeurRubriquePartage().getBase() == 0 || ((ClsObjectUtil.isNull(salary.getInfoSalary().getNbjsa())) && (ClsObjectUtil.isNull(salary.getInfoSalary().getNbjse()))))
			return true;
		//
		// -- Stc - Calcul du montant
		if (salary.getParameter().isStc())
		{
			salary.getValeurRubriquePartage().setRates(salary.getInfoSalary().getNbjse().doubleValue() + salary.getInfoSalary().getNbjsa().doubleValue());
			div_nbjs = (salary.getInfoSalary().getJapa().doubleValue() + salary.getInfoSalary().getJapec().doubleValue()) - salary.getValeurRubriquePartage().getRates()
					- salary.getInfoSalary().getJded().doubleValue();
			// w_mon := w_bas * w_tau / div_nbjs;
			if (div_nbjs != 0)
			{
				montant = salary.getValeurRubriquePartage().getBase() * salary.getValeurRubriquePartage().getRates() / div_nbjs;
				salary.getValeurRubriquePartage().setAmount(montant);
			}
		}
		// ELSE
		else
		{
			salary.getValeurRubriquePartage().setRates(salary.getInfoSalary().getNbjse().doubleValue() + salary.getInfoSalary().getNbjsa().doubleValue());
			// div_nbjs := PA_CALCUL.wsal01.nbjcf;
			div_nbjs = salary.getInfoSalary().getNbjcf().doubleValue();
			// w_mon := w_bas * w_tau / div_nbjs;
			if (div_nbjs != 0)
			{
				montant = salary.getValeurRubriquePartage().getBase() * salary.getValeurRubriquePartage().getRates() / div_nbjs;
				salary.getValeurRubriquePartage().setAmount(montant);
			}
		}
		//
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo27(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo27(ClsRubriqueClone rubrique)
	{
		String cdos = salary.getInfoSalary().getComp_id().getCdos();
		// String crub = rubrique.getRubrique().getComp_id().getCrub();
		Double montant = new Double(0);
		// double taux = 0;
		double base = 0;
		double totalNbreJourSuppl = 0;
		int diviseurNbreJourSuppl = 0;
		//
		salary.getValeurRubriquePartage().setRates(0);
		salary.getValeurRubriquePartage().setAmount(0);
		salary.getValeurRubriquePartage().setValeur(0);
		//
		if (salary.getValeurRubriquePartage().getBase() == 0)
		{
			// logger
			return true;
		}
		if (ClsObjectUtil.isNull(salary.getInfoSalary().getDrtcg()) && ClsObjectUtil.isNull(salary.getInfoSalary().getDtes()))
		{
			// logger
			salary.getParameter().setError(salary.getParameter().errorMessage("ERR-90067", salary.getParameter().getLangue(), salary.getInfoSalary().getComp_id().getNmat()));
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + salary.getParameter().getError();
			return false;
		}
		// -- Verification de la coherence de la date de retour conges annuels
		Date oDate = new ClsDate("01-01-1990", "dd-MM-yyyy").getDate();
		if ((salary.getInfoSalary().getDrtcg()!=null) && (salary.getInfoSalary().getDrtcg().getTime() < oDate.getTime()))
		{
			// logger
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90068", salary.getParameter().getLangue(), salary.getInfoSalary().getComp_id().getNmat(),
							new ClsDate(salary.getInfoSalary().getDrtcg()).getDateS()));
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + salary.getParameter().getError();
			return false;
		}
		Date date_retcg = null;
		Date date_debcg = null;
		
		String ddcf = null;
		if (salary.getInfoSalary().getDdcf() != null)
			ddcf = "'" + new ClsDate(salary.getInfoSalary().getDdcf()).getDateS(salary.parameter.appDateFormat) + "'";
		
		String requete=!salary.getParameter().isUseRetroactif() ?
			"select max(ddcg) from HistoCongeSalarie " + " where identreprise='" + cdos + "'" + " and nmat = '" + salary.getInfoSalary().getComp_id().getNmat() + "'"
			+ " and ddcg < " + ddcf + "" + " and cmcg in (select a.cacc from ParamData a, ParamData b " + " where b.cacc = a.cacc "
			+ " and b.ctab = b.ctab" + " and b.identreprise='" + cdos + "'" + " and a.identreprise='" + cdos + "'" + " and a.ctab = 22 "
			+ " and b.ctab = 22 " + " and a.nume = 1 " + " and b.nume = 3 " + " and a.valm = 1 " + " and b.valm = 0 )" :
	"select max(ddcg) from HistoCongeSalarie " + " where identreprise='" + cdos + "'" + " and nmat = '" + salary.getInfoSalary().getComp_id().getNmat() + "'"
			+ " and ddcg < " + ddcf + "" + " and cmcg in (select a.cacc from Rhthfnom a, Rhthfnom b "
			+ " where b.cacc = a.cacc " + " and b.ctab = b.ctab" + " and b.identreprise='" + cdos + "'" + " and a.identreprise='" + cdos + "'"
			+ " and a.ctab = 22 " + " and b.ctab = 22 " + " and a.nume = 1 " + " and b.nume = 3 " + " and a.valm = 1 " + " and b.valm = 0 )";
	
		Session session = salary.getService().getSession();
		Query q = session.createSQLQuery(requete);
		
		List l = q.list();
		salary.getService().closeSession(session);
		if (!ClsObjectUtil.isListEmty(l))
		{
			if (!ClsObjectUtil.isNull(l.get(0)))
				date_retcg = (Date) l.get(0);
		}
		if (date_retcg == null)
		{
			date_retcg = ClsObjectUtil.isNull(salary.getInfoSalary().getDrtcg()) ? salary.getInfoSalary().getDtes() : salary.getInfoSalary().getDrtcg();
		}
		// -- Determination de la date de debut de conges
		// -- MM 12/10/2000 si STC date deb = date radiation
		date_debcg = salary.getParameter().isStc() ? salary.getInfoSalary().getDmrr() : salary.getInfoSalary().getDdcf();
		date_debcg = ClsObjectUtil.isNull(date_debcg) ? date_retcg : new ClsDate(date_debcg).addDay(-1);
		
		//
		// -- Calcul du nombre de mois ecoule depuis le dernier conges
		if (date_debcg.getTime() < date_retcg.getTime())
		{
			// logger
			salary.getParameter().setError(salary.getParameter().errorMessage("ERR-90071", salary.getParameter().getLangue(), salary.getInfoSalary().getComp_id().getNmat()));
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + salary.getParameter().getError();
			return false;
		}
		double nbreDeMoisTr = ClsDate.getMonthsBetween(date_debcg, date_retcg);
//		if(salary.parameter.nomClient.equalsIgnoreCase(ClsEntreprise.COMILOG))
//			nbreDeMoisTr = ClsStringUtil.truncateToXDecimal(new ClsAlgorithmComilog(salary).getMonthsBetween(date_debcg, date_retcg),2).doubleValue();
		// -- Récupération du nombre de jrs de delai de route
		tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,cdos, 99, "DELAI-RTE", 1, salary.getNlot(), salary.getNbul(), salary.getMoisPaieCourant(), salary.getPeriodOfPay(),
				ClsEnumeration.EnColumnToRead.AMOUNT, rubrique.getRubrique().getComp_id().getCrub());
		String rubriqueDelaiDeRoute = ClsStringUtil.formatNumber(tempNumber == null ? 0 : tempNumber.doubleValue(), ParameterUtil.formatRubrique);
		int nbreJoursDelai = 0;
		tempNumber = salary.getUtilNomenclature().getAmountOrRateOfFixElement(cdos, salary.getMoisPaieCourant(), salary.getInfoSalary().getComp_id().getNmat(), salary.getPeriodOfPay(),
				rubriqueDelaiDeRoute, salary.getNbul());
		nbreJoursDelai = tempNumber == null ? 0 :  tempNumber.intValue();
		// -- Stc - Calcul du montant
		if (salary.getParameter().isStc())
		{
			// -- w_tau := PA_CALCUL.wsal01.nbjse + PA_CALCUL.wsal01.nbjsa;
			totalNbreJourSuppl = salary.calculNombreDJourSuppl();
			// IF PA_CALCUL.wsal01.sexe = 'F' THEN
			if ("F".equals(salary.getInfoSalary().getSexe()))
				totalNbreJourSuppl -= salary.getNbreJourPourEnfant() + salary.getInfoSalary().getNbjse().doubleValue();
			else
				totalNbreJourSuppl -= salary.getNbreJourPourEnfant();
			// -- Prorata sur nb mois travaillés donne nb jrs supp ouvrables
			totalNbreJourSuppl = totalNbreJourSuppl * nbreDeMoisTr / 12;
			// -- Calcul du nb jrs supp en calendaire
			totalNbreJourSuppl = totalNbreJourSuppl * 7 / 6;
			if (totalNbreJourSuppl - Math.floor(totalNbreJourSuppl) > 0)
				salary.getValeurRubriquePartage().setRates(Math.floor(totalNbreJourSuppl) + 1);
			else
				salary.getValeurRubriquePartage().setRates(Math.floor(totalNbreJourSuppl));
			// -- MM 25/09/2002 les jrs deduct (ponctuels) . a la COMILOG sont inclus dans les jrs de droits.
			diviseurNbreJourSuppl = salary.getInfoSalary().getJapa().intValue() + salary.getInfoSalary().getJapec().intValue();
			salary.getValeurRubriquePartage().setBasePlafonnee(salary.getValeurRubriquePartage().getBase());
			base = salary.getValeurRubriquePartage().getBase() * salary.getValeurRubriquePartage().getRates() / diviseurNbreJourSuppl;
			salary.getValeurRubriquePartage().setAmount(base);
			// -- Fin modif MM.
		}
		else
		{
			// -- w_tau := PA_CALCUL.wsal01.nbjse + PA_CALCUL.wsal01.nbjsa;
			totalNbreJourSuppl = salary.calculNombreDJourSuppl();
			if ("F".equals(salary.getInfoSalary().getSexe()))
				totalNbreJourSuppl = totalNbreJourSuppl - salary.getNbreJourPourEnfant() + salary.getInfoSalary().getNbjse().doubleValue();
			else
				totalNbreJourSuppl = totalNbreJourSuppl - salary.getNbreJourPourEnfant();
			// END IF;
			// -- Prorata sur nb mois travaillés donne nb jrs supp ouvables
			totalNbreJourSuppl = totalNbreJourSuppl * nbreDeMoisTr / 12;
			//
			// -- Calcul du nb jrs supp en calendaire
			totalNbreJourSuppl = totalNbreJourSuppl * 7 / 6;
			if (totalNbreJourSuppl - Math.floor(totalNbreJourSuppl) > 0)
				salary.getValeurRubriquePartage().setRates(Math.floor(totalNbreJourSuppl) + 1);
			else
				salary.getValeurRubriquePartage().setRates(Math.floor(totalNbreJourSuppl));
			//
			//
			// -- MM 25/09/2002 les jrs deduct (ponctuels) . a la COMILOG sont inclus dans les jrs de droits.
			// -- div_nbjs := PA_CALCUL.wsal01.nbjaf - w_tau - nbj_delai;
			try {
				diviseurNbreJourSuppl = new Double((salary.getInfoSalary().getNbjaf().intValue() + salary.getInfoSalary().getJded().intValue()) - salary.getValeurRubriquePartage().getRates()
					- nbreJoursDelai).intValue();
			} catch (Exception e) {
				// TODO: handle exception
			}
			// -- w_mon := w_bas * w_tau / div_nbjs;
			base = salary.getValeurRubriquePartage().getBase() + NumberUtils.nvl(salary.getInfoSalary().getDded(),0).doubleValue();
			salary.getValeurRubriquePartage().setBase(base);
			salary.getValeurRubriquePartage().setBasePlafonnee(salary.getValeurRubriquePartage().getBase());
			montant = salary.getValeurRubriquePartage().getBase() * salary.getValeurRubriquePartage().getRates() / diviseurNbreJourSuppl;
			salary.getValeurRubriquePartage().setAmount(montant);
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo28(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo28(ClsRubriqueClone rubrique)
	{
		String cdos = salary.getInfoSalary().getComp_id().getCdos();
		String crub = rubrique.getRubrique().getComp_id().getCrub();
		Double montant = new Double(0);
		// double taux = 0;
		// double base = 0;
		double valeur = 0;
		int status = 0;
		//
		salary.getValeurRubriquePartage().setRates(0);
		salary.getValeurRubriquePartage().setAmount(0);
		salary.getValeurRubriquePartage().setValeur(0);
		//
		tempNumber = null;
		if (!ClsObjectUtil.isListEmty(rubrique.getListOfElementVariable()))
		{
			Object[] obj = (Object[]) rubrique.getListOfElementVariable().get(rubrique.getNumElementVarCourant());
			if (obj != null && obj[0] != null)
				tempNumber = (new BigDecimal( obj[0].toString())).doubleValue();
			else
			{
				tempNumber = salary.getUtilNomenclature().getAmountOrRateOfFixElement(cdos, salary.getMoisPaieCourant(), salary.getInfoSalary().getComp_id().getNmat(), salary.getPeriodOfPay(), crub,
						salary.getNbul());
				if (tempNumber == null)
					status = 1;
			}
			//tempNumber = rubrique.getAmount();
		}
		else
		{
			tempNumber = salary.getUtilNomenclature().getAmountOrRateOfFixElement(cdos, salary.getMoisPaieCourant(), salary.getInfoSalary().getComp_id().getNmat(), salary.getPeriodOfPay(), crub,
					salary.getNbul());
			if (tempNumber == null)
				status = 1;
		}
		
		if (status == 0)
		{
			valeur = tempNumber.doubleValue();
			salary.getValeurRubriquePartage().setValeur(valeur);
			//
			
			if ("T".equals(rubrique.getRubrique().getToum()))
			{
				salary.getValeurRubriquePartage().setRates(valeur);
				montant = salary.getValeurRubriquePartage().getBase() * salary.getValeurRubriquePartage().getRates() / 100;
				salary.getValeurRubriquePartage().setAmount(montant);
			}
			else if ("M".equals(rubrique.getRubrique().getToum()))
			{
				salary.getValeurRubriquePartage().setRates(valeur);
				montant = salary.getValeurRubriquePartage().getBase() * salary.getValeurRubriquePartage().getRates();
				salary.getValeurRubriquePartage().setAmount(montant);
			}
			else if ("D".equals(rubrique.getRubrique().getToum()))
			{
				if (valeur != 0)
				{
					salary.getValeurRubriquePartage().setRates(valeur);
					montant = salary.getValeurRubriquePartage().getBase() / salary.getValeurRubriquePartage().getRates();
					salary.getValeurRubriquePartage().setAmount(montant);
				}
				else
					salary.getValeurRubriquePartage().setAmount(0);
			}
			else
			{
				// logger
				salary.getParameter().setError(
						salary.getParameter().errorMessage("ERR-90058", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), salary.getInfoSalary().getComp_id().getNmat(),
								rubrique.getRubrique().getComp_id().getCrub()));
				if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + salary.getParameter().getError();
				return false;
			}
		}
		//
		return true;
	}
	
	public boolean algo29(ClsRubriqueClone rubrique)
	{
		double montant = 0;
		double base = 0;
		double divbascg = 0;
		if (salary.getParameter().isStc())
		{
			if (salary.getInfoSalary().getDapa() == null) salary.getInfoSalary().setDapa(new BigDecimal(0));
			if (salary.getInfoSalary().getDapec() == null) salary.getInfoSalary().setDapec(new BigDecimal(0));
			if (salary.getInfoSalary().getDded() == null) salary.getInfoSalary().setDded(new BigDecimal(0));		
			if (salary.getInfoSalary().getJapa() == null) salary.getInfoSalary().setJapa(new BigDecimal(0));	
			if (salary.getInfoSalary().getJapec() == null)salary.getInfoSalary().setJapec(new BigDecimal(0));
			
			base = salary.getValeurRubriquePartage().getBase();
			salary.getValeurRubriquePartage().setBase(base + salary.getInfoSalary().getDapa().doubleValue() + salary.getInfoSalary().getDapec().doubleValue());
			salary.getValeurRubriquePartage().setBasePlafonnee(base + salary.getInfoSalary().getDapa().doubleValue() + salary.getInfoSalary().getDapec().doubleValue());
			
			// -- Lecture du diviseur de la base conges
			tempNumber = null;
			if (rubrique.getRubrique().getTabl() != null)
			{
				tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,
						this.salary.getInfoSalary().getComp_id().getCdos(), Integer.valueOf(rubrique.getRubrique().getTabl()), salary.getInfoSalary().getCat(), 1,
						this.salary.getNlot(), this.salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.AMOUNT, rubrique.getRubrique().getComp_id().getCrub());
			}
			if (tempNumber == null || tempNumber.intValue() == 0)
			{
				salary.getParameter().setError(
						salary.getParameter().errorMessage("ERR-90062", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(),
								rubrique.getRubrique().getComp_id().getCrub(), salary.getInfoSalary().getComp_id().getNmat(), rubrique.getRubrique().getTabl(),
								salary.getInfoSalary().getCat()));
				return false;
			}
			
			divbascg = tempNumber.doubleValue();
			salary.getValeurRubriquePartage().setRates(salary.getInfoSalary().getJapa().doubleValue() + salary.getInfoSalary().getJapec().doubleValue());
			base = salary.getValeurRubriquePartage().getBase();
			salary.getValeurRubriquePartage().setAmount((base / divbascg) - salary.getInfoSalary().getDded().doubleValue());
			return true;
		}
		// -- ----- Pas de calcul si pas de fictif et WMDP != du premier mois de conges
		
//		 if("N".equals(salary.getParameter().getFictiveCalculus()) && (! salary.getPeriodOfPay().equals(salary.getInfoSalary().getPmcf())))
//		 {
//			 salary.getValeurRubriquePartage().setAmount(0);
//			 salary.getValeurRubriquePartage().setBase(0);
//			 salary.getValeurRubriquePartage().setBasePlafonnee(0);
//			 salary.getValeurRubriquePartage().setRates(0);
//			 return true;
//		 }
		// -- Pas de calcul si pas de conges
		if (salary.getWorkTime().getNbreJourConges() == 0 && salary.getWorkTime().getNbreJourCongesAnnuelMoisSuiv() == 0)
		{
			salary.getValeurRubriquePartage().setAmount(0);
			salary.getValeurRubriquePartage().setBase(0);
			salary.getValeurRubriquePartage().setBasePlafonnee(0);
			salary.getValeurRubriquePartage().setRates(0);
			//
			return true;
		}

		// -- Pas de calcul si montant du conges deja calcule
		/*
		 * if(salary.getWorkTime().getMontantCongePonctuel() != 0){ salary.getValeurRubriquePartage().setAmount(salary.getWorkTime().getMontantCongePonctuel()); return true; }
		 */
		if (salary.getInfoSalary().getDapa() == null) salary.getInfoSalary().setDapa(new BigDecimal(0));
		if (salary.getInfoSalary().getDapec() == null) salary.getInfoSalary().setDapec(new BigDecimal(0));
		if (salary.getInfoSalary().getDded() == null) salary.getInfoSalary().setDded(new BigDecimal(0));
		if (salary.getInfoSalary().getJapa() == null) salary.getInfoSalary().setJapa(new BigDecimal(0));
		if (salary.getInfoSalary().getJapec() == null) salary.getInfoSalary().setJapec(new BigDecimal(0));
		
		if (salary.getWorkTime().isDebutDeMois()) salary.getValeurRubriquePartage().setBase(0);
		//
		base = salary.getValeurRubriquePartage().getBase();
		salary.getValeurRubriquePartage().setBase(base + salary.getInfoSalary().getDapa().doubleValue() /*+ salary.getInfoSalary().getDapec().doubleValue()*/);
		base = salary.getValeurRubriquePartage().getBase();
		salary.getValeurRubriquePartage().setBasePlafonnee(base);
		// -- Lecture du diviseur de la base conges
		tempNumber = null;
		if (rubrique.getRubrique().getTabl() != null)
		{
			tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,salary.getInfoSalary().getComp_id().getCdos(), Integer.valueOf(rubrique.getRubrique().getTabl()),
					salary.getInfoSalary().getCat(), 1, salary.getNlot(), salary.getNbul(), salary.getMoisPaieCourant(), salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.AMOUNT, rubrique.getRubrique().getComp_id().getCrub());
		}
		if (tempNumber == null || tempNumber.intValue() == 0)
		{
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90062", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat(), rubrique.getRubrique().getTabl(), salary.getInfoSalary().getCat()));
			return false;
		}
		divbascg = tempNumber.doubleValue();
		if ("N".equals(salary.getParameter().getFictiveCalculus()))
		{
			if (salary.getPeriodOfPay().equals(salary.getInfoSalary().getPmcf()))
				salary.getWorkTime().setNbreJourConges(salary.getInfoSalary().getNbjcf().intValue());
			else
				salary.getWorkTime().setNbreJourConges(0);
		}
		
		
		double tau = 0;
		String query="select sum(nvl(nbjc,0)) from ElementVariableConge where identreprise='"+salary.getInfoSalary().getComp_id().getCdos()+"' and aamm = '"+salary.getMoisPaieCourant()+"' ";
		query+=" and nmat = '"+salary.getInfoSalary().getComp_id().getNmat()+"' and nbul = "+salary.getNbul()+" AND motf = '01' ";
		List lst = salary.getService().find(query);
		
		if(! lst.isEmpty() && lst.get(0) != null)
			tau = ClsObjectUtil.getDoubleFromObject(lst.get(0));
		
		salary.getValeurRubriquePartage().setRates(tau);
		base = salary.getValeurRubriquePartage().getBase();
		base = (base / divbascg)*tau;
		salary.getValeurRubriquePartage().setAmount(base);
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo3(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo3(ClsRubriqueClone rubrique)
	{
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo3";
		
		
		
		
		int MonthOfDtes = 0, monthOfPay = 0;
		double val = 0;
		double amountOrRateValue = 0;
		salary.getValeurRubriquePartage().setRates(0);
		salary.getValeurRubriquePartage().setAmount(0);
		//
		if (this.salary.getAnciennete() == 0)
			return true;
		MonthOfDtes = new ClsDate(this.salary.getInfoSalary().getDtes()).getMonth();
		monthOfPay = new ClsDate(this.salary.getPeriodOfPay(), ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getMonth();
		// ce n'est pas le mois anniversaire d'entrée dans la société pour cet employé
		if (MonthOfDtes != monthOfPay)
			return true;
		String formattedMonth = ClsStringUtil.formatNumber(this.salary.getAnciennete(), "00");
		//
		String RateOrAmount = rubrique.getRubrique().getToum();
		ClsEnumeration.EnColumnToRead colToRead = "T".equals(RateOrAmount) ? ClsEnumeration.EnColumnToRead.RATES : ClsEnumeration.EnColumnToRead.AMOUNT;
		int tabl = 0;
		tempNumber = null;
		if (rubrique.getRubrique().getTabl() != null)
		{
			tabl = Integer.valueOf(rubrique.getRubrique().getTabl());
			tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,this.salary.getInfoSalary().getComp_id().getCdos(), tabl, formattedMonth,
					rubrique.getRubrique().getNutm(), this.salary.getNlot(), this.salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), colToRead, rubrique.getRubrique().getComp_id().getCrub());
		}
		if (tempNumber == null)
		{
			salary.getParameter().setPbWithCalulation(true);
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90056", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat(), formattedMonth, rubrique.getRubrique().getTabl()));
			return false;
		}
		amountOrRateValue = tempNumber.doubleValue();
		salary.getValeurRubriquePartage().setValeur(amountOrRateValue);
		if ("M".equals(rubrique.getRubrique().getToum()))
		{
			salary.getValeurRubriquePartage().setRates(0);
			salary.getValeurRubriquePartage().setAmount(amountOrRateValue);
		}
		else if ("T".equals(rubrique.getRubrique().getToum()))
		{
			salary.getValeurRubriquePartage().setRates(amountOrRateValue);
			val = salary.getValeurRubriquePartage().getBase() * salary.getValeurRubriquePartage().getRates() / 100;
			salary.getValeurRubriquePartage().setAmount(val);
		}
		else if ("D".equals(rubrique.getRubrique().getToum()))
		{
			salary.getValeurRubriquePartage().setRates(amountOrRateValue);
			val = salary.getValeurRubriquePartage().getBase() * salary.getValeurRubriquePartage().getRates();
			salary.getValeurRubriquePartage().setAmount(val);
		}
		else
		{
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90058", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), salary.getInfoSalary().getComp_id().getNmat(),
							rubrique.getRubrique().getComp_id().getCrub()));
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo30(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo30(ClsRubriqueClone rubrique)
	{
		if('O' == salary.getParameter().getGenfile()) outputtext += "\nin algo=> 30";
		
		
		
		
		// Mnt_a_repartir NUMBER(15,3);
		// Mnt_restant NUMBER(15,3);
		// w_rowid ROWID;
		// w_rowid_Pal ROWID;
		// Banque_etrangere BOOLEAN;
		// Montant_Banque_dvd NUMBER(15,3);
		// Montant_Banque_db NUMBER(15,3);
		// Ref_TB10 INTEGER;
		// Ref_TB10_Pal INTEGER;
		// w_retour BOOLEAN;
		// Arrondi_dossier NUMBER(7,5);
		// Arrondi_Banque NUMBER(7,5);
		//
		// Num_Ligne pavrmt.nlig%TYPE;
		// Bq_agent pavrmt.bqag%TYPE;
		// Pourcent pavrmt.pourc%TYPE;
		// Montant pavrmt.mont%TYPE;
		// Devise_dossier pavrmt.dvd%TYPE;
		// Taux_Change pavrmt.txchg%TYPE;
		// Mnt_Devise_Bq pavrmt.mntdb%TYPE;
		// Mnt_Devise_Dossier pavrmt.mntdvd%TYPE;
		// AM_Paie pavrmt.aamm%TYPE;
		// Bq_Pal pavrmt.princ%TYPE;
		//
		// CURSOR curs_vrmt IS
		// SELECT nlig, bqag, pourc, mont, dvd,
		// txchg, mntdb, mntdvd, aamm, princ, rowid
		// FROM pavrmt
		// WHERE cdos = PA_CALCUL.wpdos.cdos
		// AND nmat = PA_CALCUL.wsal01.nmat
		// ORDER BY cdos, nmat, nlig;
		// numero de ligne pour la banque principale
		Integer nligPrincipal = -1;

//		String queryString = "select nlig, bqag, pourc, mont, dvd, txchg, mntdb, mntdvd, aamm, princ, guic, comp from VirementSalaire " + " where identreprise='"
//				+ salary.getInfoSalary().getComp_id().getCdos() + "'" + " and nmat = '" + salary.getInfoSalary().getComp_id().getNmat() + "'" + " order by cdos, nmat, nlig";
		String queryString = "From VirementSalarie " + " where identreprise='"
		+ salary.getInfoSalary().getComp_id().getCdos() + "'" + " and nmat = '" + salary.getInfoSalary().getComp_id().getNmat() + "'" + " order by identreprise, nmat, nlig";
		//
		// BEGIN
		//
		// -- Initialisations
		// w_tau := 0;
		// w_mon := 0;
		// Ref_TB10_Pal := 0;
		salary.getValeurRubriquePartage().setRates(0);
		salary.getValeurRubriquePartage().setAmount(0);
		int ref_tb10_pal = 0;
		//
		// -- Controle si agent paye par virement
		// IF paf_TypePaiement(PA_CALCUL.wsal01.nmat) != 'V'
		// THEN
		// RETURN TRUE;
		// END IF;
		//
		String typa = salary.getTypePaiement();
		if (!"V".equals(typa))
		{
			return true;
		}
		// -- La base contient le net a payer
		// IF PA_PAIE.NouZ( w_bas )
		// THEN
		double montantarepartir = 0;
		double montantrestant = 0;
		double montantbanquedvd = 0;
		double montantbanquedb = 0;
		double arrondidossier = 0;
		double arrondibanque = 0;
		if (salary.getValeurRubriquePartage().getBase() == 0)
		{
			// -- * Modification AB le 03/09/1999 * --
			// -- * Ajout de la mise é jour de PAVRMT quand base = 0 *--
			// BEGIN
			// UPDATE pavrmt
			// SET mntdb = 0,
			// mntdvd = 0
			// WHERE cdos = PA_CALCUL.wpdos.cdos
			// AND nmat = PA_CALCUL.wsal01.nmat
			// AND aamm = w_aamm;
			// EXCEPTION
			// WHEN OTHERS THEN
			// PA_CALCUL.err_msg := PA_PAIE.erreurp('ERR-90072',PA_CALCUL.w_clang,PA_CALCUL.t_rub.algo,PA_CALCUL.wsal01.nmat);
			// RETURN FALSE;
			// END;
			String updateString = "update VirementSalarie" + " set mntdb = 0, mntdvd = 0" + " where identreprise='" + salary.getInfoSalary().getComp_id().getCdos() + "'" + " and nmat = '"
					+ salary.getInfoSalary().getComp_id().getNmat() + "'";
			// salary.getService().bulkUpdate(updateString);
			try
			{
				if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "...................Updating vrmtagent";
				salary.getService().updateFromTable(updateString);
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				salary.getParameter().setError(ClsTreater._getStackTrace(e));
				return false;
			}
			//
			// RETURN TRUE;
			return true;
		}
		// ELSE
		else
		{
			// Mnt_a_repartir := w_bas;
			montantarepartir = salary.getValeurRubriquePartage().getBase();
			// Mnt_restant := w_bas;
			montantrestant = salary.getValeurRubriquePartage().getBase();
		}
		// END IF;
		//
		// --w_retour := Ins_palog( 'Mnt_a_repartir =' || to_char(Mnt_a_repartir ) ||'*');
		//
		// -- Lecture des banques du salarie
		// IF Curs_vrmt%ISOPEN THEN
		// CLOSE Curs_vrmt;
		// END IF;
		// OPEN curs_vrmt;

		// String queryString1 = "From VirementSalaire" ;
		// List l1 = salary.getService().find(queryString1);
		// for (int i = 0; i < l1.size(); i++) {
		// if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "-------------------->"+l1.get(i));
		// }

		List l = salary.getService().find(queryString);
		VirementSalarie virement = null;
		ClsInfoOfBank bankRef = null;
		int idxTB10 = 0;
		int refTB10 = 0;
		Object[] array = null;
		// LOOP
		for (Object obj : l)
		{
			// virement = (VirementSalaire)obj;
			// nlig, bqag, pourc, mont, dvd, txchg, mntdb, mntdvd, aamm, princ
//			virement = new VirementSalaire();
			virement = (VirementSalarie) obj;
//			array = (Object[]) obj;
//			virement.setComp_id(new VirementSalairePK());
//			virement.getComp_id().setCdos(salary.getInfoSalary().getComp_id().getCdos());
//			virement.getComp_id().setNmat(salary.getInfoSalary().getComp_id().getNmat());
//			virement.getComp_id().setNlig((Integer) array[0]);
//			virement.setBqag(array[1].toString());
//			virement.setPourc((Integer) array[2]);
//			virement.setMont((BigDecimal) array[3]);
//			virement.setDvd(array[4].toString());
//			virement.setTxchg((BigDecimal) array[5]);
//			virement.setMntdb((BigDecimal) array[6]);
//			virement.setMntdvd((BigDecimal) array[7]);
//			virement.setAamm(array[8] != null ? array[8].toString() : null);
//			virement.setPrinc(array[9].toString());
//			virement.setGuic(array[10].toString());
//			virement.setComp(array[11].toString());
			// FETCH curs_vrmt
			// INTO Num_Ligne, Bq_agent, Pourcent, Montant, Devise_dossier,
			// Taux_Change, Mnt_Devise_Bq, Mnt_Devise_Dossier,
			// AM_Paie, Bq_Pal, w_rowid;
			// EXIT WHEN curs_vrmt%NOTFOUND;
			//
			// -- Recherche indice banque dans TB10
			// PA_CALCUL.Index_TB10 := 1;
			// Ref_TB10 := 0;
			refTB10 = 0;// Ref_TB10
			idxTB10 = 1;// Index_TB10
			// WHILE PA_CALCUL.Index_TB10 <= PA_CALCUL.Index_TB10_Max LOOP
			for (ClsInfoOfBank bank : salary.getParameter().getListOfBank())
			{
				// IF PA_CALCUL.TB10_Banque( PA_CALCUL.Index_TB10 ) = Bq_agent
				// THEN
				// Ref_TB10 := PA_CALCUL.Index_TB10;
				// EXIT;
				// ELSe
				// PA_CALCUL.Index_TB10 := PA_CALCUL.Index_TB10 + 1;
				// END IF;
				if (virement.getBqag().equals(bank.getCode()))
				{
					bankRef = bank;
					refTB10 = idxTB10;
					break;
				}
				else
				{
					idxTB10++;
				}
			}
			// END LOOP;
			//
			// IF Ref_TB10 = 0
			// THEN
			// PA_CALCUL.err_msg := PA_PAIE.erreurp('ERR-90073',PA_CALCUL.w_clang,PA_CALCUL.t_rub.algo,PA_CALCUL.wsal01.nmat,PA_CALCUL.t_rub.crub,Bq_Agent);
			// w_retour := Ins_palog( PA_CALCUL.err_msg );
			// RETURN FALSE;
			// END IF;
			if (refTB10 == 0)
			{
				String error = salary.getParameter().errorMessage("ERR-90073", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), salary.getInfoSalary().getComp_id().getNmat(),
						rubrique.getRubrique().getComp_id().getCrub(), virement.getBqag());
				//salary.getParameter().insererLogMessage(error);
				salary.getParameter().setError(error);
				return false;
			}
			//
			// -- Memorisation du rowid de la banque principale
			// IF Bq_Pal = 'O'
			// THEN
			// w_rowid_Pal := w_rowid;
			// Ref_TB10_Pal := Ref_TB10;
			// END IF;
			if ("O".equals(virement.getPrinc()))
			{
				nligPrincipal = virement.getIdEntreprise();
				ref_tb10_pal = refTB10;
			}
			//
			// --w_retour := Ins_palog( 'Bq_agent =' || Bq_agent || '*, Indice=' || to_char(Ref_TB10 )|| '*');
			// --w_retour := Ins_palog( 'Devise_dossier =' || Devise_dossier ||'*');
			//
			//
			// -- Erreur si ni montant ni pourcentage pour calcul
			// IF PA_PAIE.NouZ( Pourcent ) AND
			// PA_PAIE.NouZ( Montant )
			// THEN
			// PA_CALCUL.err_msg := PA_PAIE.erreurp('ERR-90074',PA_CALCUL.w_clang,PA_CALCUL.t_rub.algo,PA_CALCUL.wsal01.nmat,PA_CALCUL.t_rub.crub,Bq_Agent);
			// w_retour := Ins_palog( PA_CALCUL.err_msg );
			// RETURN FALSE;
			// END IF;
			if ((virement.getPourc() == null || virement.getPourc() == 0) && (virement.getMont() == null || virement.getMont().doubleValue() == 0))
			{
				String error = salary.getParameter().errorMessage("ERR-90074", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), salary.getInfoSalary().getComp_id().getNmat(),
						rubrique.getRubrique().getComp_id().getCrub(), virement.getBqag());
				//salary.getParameter().insererLogMessage(error);
				salary.getParameter().setError(error);
				return false;
			}
			//
			// -- Calcul du montant a virer en devise dossier
			// IF PA_PAIE.NouZ( Pourcent )
			// THEN
			if (virement.getPourc() == null || virement.getPourc() == 0)
			{
				// -- Calcul en montant
				// IF Devise_dossier = 'N'
				// THEN
				// Montant_Banque_dvd := Montant * PA_CALCUL.TB10_Taux_Change( Ref_TB10 );
				// ELSE
				// Montant_Banque_dvd := Montant;
				// END IF;
				// IF Montant_Banque_dvd > Mnt_a_repartir
				// THEN
				// Montant_Banque_dvd := Mnt_a_repartir;
				// END IF;
				montantbanquedvd = ("N".equals(virement.getDvd())) ? virement.getMont().doubleValue() * bankRef.getExchangeRate() : virement.getMont().doubleValue();
				if (montantbanquedvd > montantarepartir)
					montantbanquedvd = montantarepartir;
			}
			else
			{
				// ELSE
				// -- Calcul en pourcentage
				// Montant_Banque_dvd := Mnt_a_repartir * Pourcent / 100;
				// IF Montant_Banque_dvd < PA_CALCUL.TB10_Vrmt_Mini( Ref_TB10 )
				// THEN
				// Montant_Banque_dvd := 0;
				// END IF;
				// END IF;
				montantbanquedvd = montantarepartir * virement.getPourc() / 100;
				if (montantbanquedvd < bankRef.getVirementMini())
					montantbanquedvd = 0;
			}
			// IF PA_CALCUL.TB10_Devise_Etrangere( Ref_TB10 )
			// THEN
			// Montant_Banque_db := Montant_Banque_dvd / PA_CALCUL.TB10_Taux_Change( Ref_TB10 );
			// ELSE
			// Montant_Banque_db := Montant_Banque_dvd;
			// END IF;
			if(bankRef.isForeignBank())
			{
				if (bankRef.getExchangeRate() != 0)
					montantbanquedb = montantbanquedvd / bankRef.getExchangeRate();
			}
			else
				montantbanquedb = montantbanquedvd;
			//
			// --w_retour := Ins_palog( 'Montant_Banque_db =' || to_char(Montant_Banque_db )|| '*');
			// --w_retour := Ins_palog( 'Montant_Banque_dvd =' || to_char(Montant_Banque_dvd )|| '*');
			//
			// -- Arrondi des montants en fonction des decimales de la devise
			// IF PA_CALCUL.Nb_Dec_Devise_Dossier = 0
			// THEN
			// Arrondi_dossier := 1;
			// ELSE
			// Arrondi_dossier := 1 / ( 10 * PA_CALCUL.Nb_Dec_Devise_Dossier );
			// END IF;
			//if (salary.getParameter().getDossierNbreDecimale() != 0)
			arrondidossier = salary.getParameter().getDossierNbreDecimale() == 0 ? 1 : 1 / (10 * salary.getParameter().getDossierNbreDecimale());

			// IF PA_CALCUL.TB10_Nb_Dec( Ref_TB10 ) = 0
			// THEN
			// Arrondi_Banque := 1;
			// ELSE
			// Arrondi_Banque := 1 / ( 10 * PA_CALCUL.TB10_Nb_Dec( Ref_TB10 ) );
			// END IF;
			//if (bankRef.getNbreDecimal() != 0)
			arrondibanque = bankRef.getNbreDecimal() == 0 ? 1 : 1 / (10 * bankRef.getNbreDecimal());
			// PA_CALCUL.arrondi2('N', Arrondi_Dossier, Montant_Banque_dvd );
			// PA_CALCUL.arrondi2('N', Arrondi_Banque, Montant_Banque_db );
			double arrondidossier1 = this.arrondi2('N', arrondidossier, montantbanquedvd);
			montantbanquedvd = arrondidossier1;
			double arrondibanque1 = this.arrondi2('N', arrondibanque, montantbanquedb);
			montantbanquedb = arrondibanque1;
			//
			// --w_retour := Ins_palog( 'Montant_Banque_db Arro =' || to_char(Montant_Banque_db )|| '*');
			// --w_retour := Ins_palog( 'Montant_Banque_dvd Arro =' || to_char(Montant_Banque_dvd )|| '*');
			//
			// IF PA_PAIE.NouZ( Pourcent )
			// THEN
			// -- Repartition en Montant
			// Mnt_a_repartir := Mnt_a_repartir - Montant_Banque_dvd;
			// Mnt_restant := Mnt_restant - Montant_Banque_dvd;
			// ELSE
			// -- Repartition en Pourcentage
			// Mnt_restant := Mnt_restant - Montant_Banque_dvd;
			// END IF;
			if (virement.getPourc() == null || virement.getPourc() == 0)
			{
				// -- Repartition en Montant
				montantarepartir -= montantbanquedvd;
				montantrestant -= montantbanquedvd;
			}
			else
			{
				// -- Repartition en Pourcentage
				montantrestant -= montantbanquedvd;
			}
			//
			// --w_retour := Ins_palog( 'Mnt_restant =' || to_char(Mnt_restant )|| '*');
			//
			// -- Prise en compte montant si banque devise etrangere
			// IF PA_CALCUL.TB10_Devise_Etrangere( Ref_TB10 )
			// THEN
			// w_mon := w_mon + Montant_Banque_dvd;
			// END IF;
			if (bankRef.isForeignBank())
				salary.getValeurRubriquePartage().setAmount(salary.getValeurRubriquePartage().getAmount() + montantbanquedvd);
			//
			// --w_retour := Ins_palog( 'w_mon =' || to_char(w_mon )|| '*');
			//
			// -- Mise a jour table des virements
			// BEGIN
			// UPDATE pavrmt
			// SET txchg = PA_CALCUL.TB10_Taux_Change( Ref_TB10 ),
			// mntdb = Montant_Banque_db,
			// mntdvd = Montant_Banque_dvd,
			// aamm = w_aamm
			// WHERE rowid = w_rowid;
			// EXCEPTION
			// WHEN OTHERS THEN
			// PA_CALCUL.err_msg := PA_PAIE.erreurp('ERR-90075',PA_CALCUL.w_clang,PA_CALCUL.t_rub.algo,PA_CALCUL.wsal01.nmat,PA_CALCUL.t_rub.crub,Bq_Agent);
			// w_retour := Ins_palog( PA_CALCUL.err_msg );
			// RETURN FALSE;
			// END;
			virement.setTxchg(new BigDecimal(bankRef.getExchangeRate()));
			virement.setMntdb(ClsStringUtil.truncateTo3Decimal(new BigDecimal(montantbanquedb)));
			virement.setMntdvd(ClsStringUtil.truncateTo3Decimal(new BigDecimal(montantbanquedvd)));
			virement.setAamm(salary.getPeriodOfPay());
			//
			try
			{
				if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "Vrmt to update = " + virement.toString();
				salary.getService().update(virement);
			}
			catch (DataAccessException e)
			{
				e.printStackTrace();
			}
			//
		}
		// END LOOP;
		// CLOSE curs_vrmt;
		//
		// -- Test si tous l'argent a ete reparti
		// IF Mnt_restant != 0
		// THEN
		if (montantrestant != 0)
		{
			// IF Ref_TB10_Pal = 0
			// THEN
			// PA_CALCUL.err_msg := PA_PAIE.erreurp('ERR-90076',PA_CALCUL.w_clang,PA_CALCUL.t_rub.algo,PA_CALCUL.wsal01.nmat,PA_CALCUL.t_rub.crub);
			// w_retour := Ins_palog( PA_CALCUL.err_msg );
			// RETURN FALSE;
			// END IF;
			if (ref_tb10_pal == 0)
			{
				String error = salary.getParameter().errorMessage("ERR-90076", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), salary.getInfoSalary().getComp_id().getNmat(),
						rubrique.getRubrique().getComp_id().getCrub());
				//salary.getParameter().insererLogMessage(error);
				salary.getParameter().setError(error);
				return false;
			}
			// -- Recuperation et conversion du reste a affecter
			// Montant_Banque_dvd := Mnt_restant;
			montantbanquedvd = montantrestant;
			// IF PA_CALCUL.TB10_Devise_Etrangere( Ref_TB10_Pal )
			// THEN
			// Montant_Banque_db := Montant_Banque_dvd / PA_CALCUL.TB10_Taux_Change( Ref_TB10_Pal );
			// ELSE
			// Montant_Banque_db := Montant_Banque_dvd;
			// END IF;
			
				if(bankRef.isForeignBank())
				{
					if(bankRef.getExchangeRate() != 0)
					{
						montantbanquedb = montantbanquedvd / bankRef.getExchangeRate();
					}
				}
				else
					montantbanquedb = montantbanquedvd;
			// --w_retour := Ins_palog( 'Montant_restant dvd=' || to_char(Montant_Banque_dvd )|| '*');
			// --w_retour := Ins_palog( 'Montant_restant db=' || to_char(Montant_Banque_db )|| '*');
			// -- Maj de la banque principale
			// BEGIN
			// UPDATE pavrmt
			// SET txchg = PA_CALCUL.TB10_Taux_Change( Ref_TB10_Pal ),
			// mntdb = NVL(mntdb, 0) + Montant_Banque_db, -- LH 150700
			// mntdvd = NVL(mntdvd, 0) + Montant_Banque_dvd,
			// aamm = w_aamm
			// WHERE rowid = w_rowid_Pal;
			// EXCEPTION
			// WHEN OTHERS THEN
			// PA_CALCUL.err_msg := PA_PAIE.erreurp('ERR-90077',PA_CALCUL.w_clang,PA_CALCUL.t_rub.algo,PA_CALCUL.wsal01.nmat,PA_CALCUL.t_rub.crub,PA_CALCUL.TB10_Banque( Ref_TB10_Pal ));
			// w_retour := Ins_palog( PA_CALCUL.err_msg );
			// RETURN FALSE;
			// END;
			// ajout yannick
			virement = (VirementSalarie) salary.getService().find("FROM VirementSalaire WHERE identreprise="+Integer.valueOf(salary.getInfoSalary().getComp_id().getCdos())+" AND nmat='"+salary.getInfoSalary().getComp_id().getNmat()+"'");

			// ////////fin ajout
			virement.setTxchg(new BigDecimal(bankRef.getExchangeRate()));
			double a = virement.getMntdb() == null ? 0 : virement.getMntdb().doubleValue();
			virement.setMntdb(ClsStringUtil.truncateTo3Decimal(new BigDecimal(a + montantbanquedb)));
			a = virement.getMntdvd() == null ? 0 : virement.getMntdvd().doubleValue();
			virement.setMntdvd(ClsStringUtil.truncateTo3Decimal(new BigDecimal(a + montantbanquedvd)));
			virement.setAamm(salary.getPeriodOfPay());
			//
			try
			{
				salary.getService().update(virement);
			}
			catch (DataAccessException e1)
			{
				String error = salary.getParameter().errorMessage("ERR-90077", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), salary.getInfoSalary().getComp_id().getNmat(),
						rubrique.getRubrique().getComp_id().getCrub());
				//salary.getParameter().insererLogMessage(error);
				salary.getParameter().setError(error);
				return false;
			}

			/** ****par yannick ************** */
//			String updateString = "update VirementSalaire set txchg  = " + virement.getTxchg() + ",mntdb  = mntdb + " + virement.getMntdb() + ",mntdvd = mntdvd + " + virement.getMntdvd() + ",aamm   ="
//					+ salary.getPeriodOfPay();
//			updateString += " where identreprise='" + salary.getInfoSalary().getComp_id().getCdos() + "'" + " and nmat = '" + salary.getInfoSalary().getComp_id().getNmat() + "' and nlig=" + nligPrincipal;
//			try
//			{
//				salary.getService().updateFromTable(updateString);
//			}
//			catch (SQLException e)
//			{
//				e.printStackTrace();
//			}
		}
		// END IF;
		//
		// RETURN TRUE;
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo31(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo31(ClsRubriqueClone rubrique)
	{
//		if(StringUtils.equals(salary.parameter.getNomClient(), ClsEntreprise.COMILOG))
//			return comilog.algo31(rubrique);
		
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo31";
		Double montant = new Double(0);
		double nbreJourscstc = 0;
		String type_nbj = "";

		// nbj_cstc := 0;
		//
		// wfnom.lib2 := paf_lecfnomL(99,'ALGO-31',2,PA_CALCUL.w_aamm,PA_CALCUL.w_nlot,PA_CALCUL.wsd_fcal1.nbul);
		// IF wfnom.lib2 IS NULL THEN
		// type_nbj := 'A';
		// ELSE
		// type_nbj := SUBSTR(wfnom.lib2,1,1);
		// END IF;
		String libelle2 = this.getLabelFromNomenclature(salary.getInfoSalary().getComp_id().getCdos(), 99, "ALGO-31", 2, salary.getNlot(), salary.getNbul(),
				this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), rubrique.getRubrique().getComp_id().getCrub());
		if (ClsObjectUtil.isNull(libelle2))
		{
			type_nbj = "A";
		}
		else
		{
			type_nbj = StringUtils.substring(libelle2 , 0, 1);
		}
		
		String type_nbj2 = "";
		String libelle3 = this.getLabelFromNomenclature(salary.getInfoSalary().getComp_id().getCdos(), 99, "ALGO-31", 2, salary.getNlot(), salary.getNbul(),
				this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), rubrique.getRubrique().getComp_id().getCrub());
		if (ClsObjectUtil.isNull(libelle3))
		{
			type_nbj2 = "A";
		}
		else
		{
			type_nbj2 = StringUtils.substring(libelle3 , 0, 1);
		}
		//
		// diviseur := paf_lecfnomM(99,'ALGO-31',1,PA_CALCUL.w_aamm,PA_CALCUL.w_nlot,PA_CALCUL.wsd_fcal1.nbul);
		// IF PA_PAIE.NouZ(diviseur) THEN
		// diviseur := 30;
		// END IF;
		tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,salary.getInfoSalary().getComp_id().getCdos(), 99, "ALGO-31", 1, salary.getNlot(), salary.getNbul(),
				salary.getMoisPaieCourant(), salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.AMOUNT, rubrique.getRubrique().getComp_id().getCrub());
		double diviseur = (tempNumber == null || tempNumber.intValue() == 0) ? 30 : tempNumber.doubleValue();
		
		//
		// IF type_nbj = 'C' THEN
		// PA_CALCUL.wnbjsmm := PA_CALCUL.wnbjc;
		// ELSE
		// PA_CALCUL.wnbjsmm := PA_CALCUL.wnbjca;
		// END IF;
		if ("C".equals(type_nbj))
		{
			salary.getWorkTime().setNbreJourCongesSalaireMoyMois(salary.getWorkTime().getNbreJourConges());
		}
		else
		{
			salary.getWorkTime().setNbreJourCongesSalaireMoyMois(salary.getWorkTime().getNbreJoursAbsencePourCongeAnnuel());
		}
		//
		if (salary.getInfoSalary().getJapa() == null)
			salary.getInfoSalary().setJapa(new BigDecimal(0));
		if (salary.getInfoSalary().getJapec() == null)
			salary.getInfoSalary().setJapec(new BigDecimal(0));
		//
		// -- LH 210198
		if (salary.getParameter().isStc() && ! salary.getEntreprise().equals(ClsEnumeration.EnEnterprise.SHELL_GABON))
		{
			if (salary.getInfoSalary().getJapa().doubleValue() == 0 && salary.getInfoSalary().getJapec().doubleValue() == 0)
				nbreJourscstc = salary.getWorkTime().getNbreJourCongesSalaireMoyMois();
			else
			{
				if (salary.getWorkTime().getNbreJourCongesSalaireMoyMois() > salary.getInfoSalary().getJapa().doubleValue())
				{
					salary.getInfoSalary()
							.setJapec(
									new BigDecimal(salary.getInfoSalary().getJapec().doubleValue()
											- (salary.getWorkTime().getNbreJourCongesSalaireMoyMois() - salary.getInfoSalary().getJapa().doubleValue())));
					salary.getInfoSalary().setJapa(new BigDecimal(0));
				}
				else
				{
					salary.getInfoSalary().setJapa(new BigDecimal(salary.getInfoSalary().getJapa().doubleValue() - salary.getWorkTime().getNbreJourCongesSalaireMoyMois()));
				}
				nbreJourscstc = nbreJourscstc
						+ new Double(salary.getInfoSalary().getJapa().doubleValue() + salary.getInfoSalary().getJapec().doubleValue() + salary.getWorkTime().getNbreJourCongesSalaireMoyMois())
								.intValue();
			}

			// -- calcul des jours de cg du mois.
			// l_nbjtr := rec_nbjtr;
			// IF PA_PAIE.NouZ(l_nbjtr) THEN
			// l_nbjtr := 0;
			// END IF;
			int nbjtr = rechercheNombreDeJourTravailDuMois(rubrique);
			if (nbjtr < 0)
				nbjtr = 0;
			// l_nbjcg := rec_nbjcg;
			// IF PA_PAIE.NouZ(l_nbjcg) THEN
			// l_nbjcg := 0;
			// END IF;
			int nbjcg = rechercheNombreDeJourCongeDuMois(rubrique.getRubrique().getComp_id().getCrub());
			if (nbjcg < 0)
				nbjcg = 0;
			// l_nbjcg := l_nbjcg / 30 * l_nbjtr;
			// nbj_cstc := nbj_cstc + l_nbjcg;
			if (nbjtr != 0)
			{
				nbjcg = nbjcg / 30 * nbjtr;
				nbreJourscstc += nbjcg;
			}
			//
			// -- pour calculer en jour calendaire on applique la regle : 6 jrs ouvr / sem.
			// -- car pour un stc pas de date de cg.
			// IF type_nbj != 'C' THEN
			// nbj_cstc := ROUND(nbj_cstc / 6 * 7,1);
			// END IF;
			if (!"C".equals(type_nbj))
			{
				nbreJourscstc = new Double(Math.floor(nbreJourscstc / 6 * 7.1)).intValue();
			}
			if (nbreJourscstc == 0)
			{
				//
				salary.getValeurRubriquePartage().setRates(0);
				salary.getValeurRubriquePartage().setAmount(0);
				salary.getValeurRubriquePartage().setBase(0);
				//
			}
			else
			{
				salary.getValeurRubriquePartage().setRates(nbreJourscstc);
				montant = salary.getValeurRubriquePartage().getBase() * nbreJourscstc / diviseur;
				salary.getValeurRubriquePartage().setAmount(montant);
			}
		}
		else
		{
			if (salary.getWorkTime().getNbreJourCongesSalaireMoyMois() == 0 && salary.getWorkTime().getNbreJourPayeSupplPayeNonPris() == 0
					&& salary.getWorkTime().getNbreJourCongesSalaireMoyMoisSuiv() == 0)
			{
				//
				salary.getValeurRubriquePartage().setRates(0);
				salary.getValeurRubriquePartage().setAmount(0);
				salary.getValeurRubriquePartage().setBase(0);
				//
				return true;
			}
			double taux = 0;
			if ("N".equals(salary.getParameter().getFictiveCalculus()))
			{
				if (salary.getPeriodOfPay().equals(salary.getInfoSalary().getPmcf()))
				{
					if("C".equalsIgnoreCase(type_nbj))
						taux = salary.getInfoSalary().getNbjcf().doubleValue();
					else
						taux = salary.getInfoSalary().getNbjaf().doubleValue();
				}
				else
				{
					//
					salary.getValeurRubriquePartage().setRates(0);
					salary.getValeurRubriquePartage().setAmount(0);
					//
					return true;
				}
			}
			else
				taux = salary.getWorkTime().getNbreJourCongesSalaireMoyMois();
			
			salary.getValeurRubriquePartage().setRates(taux);
			//
			salary.getValeurRubriquePartage().setRates(taux + salary.getWorkTime().getNbreJourPayeSupplPayeNonPris());
			salary.getValeurRubriquePartage().setAmount(salary.getValeurRubriquePartage().getBase() * taux / diviseur);
			
			if ("N".equals(salary.getParameter().getFictiveCalculus()))
			{
				if (salary.getPeriodOfPay().equals(salary.getInfoSalary().getPmcf()))
				{
					if("C".equalsIgnoreCase(type_nbj2))
						taux = salary.getInfoSalary().getNbjcf().doubleValue();
					else
						taux = salary.getInfoSalary().getNbjaf().doubleValue();
					
					salary.getValeurRubriquePartage().setRates(taux);
				}
			}
			
		}
		return true;
	}
	
	
	
	public boolean algo34(ClsRubriqueClone rubrique)
	{
	
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo31";
		Double montant = new Double(0);
		
		Integer mois1;
		Integer mois2;
		
		if(salary.anciennete == 0)
		{
			salary.getValeurRubriquePartage().setRates(0);
			salary.getValeurRubriquePartage().setAmount(0);
			return true;
		}
		mois1 = new ClsDate(new ClsDate(salary.infoSalary.getDtes()).addMonth(1)).getMonth();
		mois2 = this.salary.getMyMonthOfPay().getMonth();
		
		if(mois1 != mois2)
		{
			salary.getValeurRubriquePartage().setRates(0);
			salary.getValeurRubriquePartage().setAmount(0);
			return true;
		}
		 
		String char10 = ClsStringUtil.formatNumber(salary.anciennete, "00");
		String cdos = salary.parameter.dossier;
		
		ClsEnumeration.EnColumnToRead col = "M".equals(rubrique.getRubrique().getToum()) ? ClsEnumeration.EnColumnToRead.AMOUNT : ClsEnumeration.EnColumnToRead.RATES;
		tempNumber = null;
		if (rubrique.getRubrique().getTabl() != null)
		{
			tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,cdos, Integer.valueOf(rubrique.getRubrique().getTabl()), char10, rubrique.getRubrique().getNutm(), salary.getNlot(),
					salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), col, rubrique.getRubrique().getComp_id().getCrub());
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "----------------------------->Montant de la table " + rubrique.getRubrique().getTabl() + " = " + montant;
		}
		
		if (tempNumber == null)
		{
			// logger
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90056", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat(), char10, rubrique.getRubrique().getTabl()));
			salary.getParameter().setPbWithCalulation(true);
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + salary.getParameter().getError();
			return false;
		}
		montant = tempNumber.doubleValue();
		String toum = rubrique.getRubrique().getToum();
		if(StringUtils.equals("M", toum))
		{
			salary.getValeurRubriquePartage().setRates(0);
			salary.getValeurRubriquePartage().setAmount(montant);
		}
		else if(StringUtils.equals("T", toum))
		{
			salary.getValeurRubriquePartage().setAmount(salary.valeurRubriquePartage.getBase()*montant/100);
			salary.getValeurRubriquePartage().setRates(salary.anciennete);
		}
		else if(StringUtils.equals("D", toum))
		{
			salary.getValeurRubriquePartage().setAmount(salary.valeurRubriquePartage.getBase()*montant);
			salary.getValeurRubriquePartage().setRates(montant);
		}
		else
		{
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90058", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), salary.getInfoSalary().getComp_id().getNmat(),
							rubrique.getRubrique().getComp_id().getCrub()));
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo37(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo37(ClsRubriqueClone rubrique)
	{
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo37";
		String cdos = salary.getInfoSalary().getComp_id().getCdos();
		String crub = rubrique.getRubrique().getComp_id().getCrub();
		Double montant = new Double(0);
		double taux = 0;
		double plaf1 = 0;
		double plaf2 = 0;
		double reste = 0;
		double inter = 0;
		//
		// String queryString = " from ElementSalaireBareme "
		// + " where identreprise='" + cdos + "'"
		// + " and crub = '" + crub + "'"
		// + " order by cdos , crub , nume";
		// //
		// String queryStringRetro = " from Rhthrubbarem "
		// + " where identreprise='" + cdos + "'"
		// + " and crub = '" + crub + "'"
		// + " and aamm = '" + salary.getPeriodOfPay() + "'"
		// + " and nbul = " + salary.getNbul()
		// + " order by cdos , crub , nume";
		//
		rubrique.calculateCumulOfBase();
		//
		salary.getValeurRubriquePartage().setRates(0);
		salary.getValeurRubriquePartage().setAmount(0);
		double tauxCotisation = 0;
		//
		for (int i = 0; i < 12; i++)
		{
			if (rubrique.getListOfRegularisationFr().get(i).getPlafond() == 0)
				// EXIT;
				break;
			salary.getValeurRubriquePartage().setInter(rubrique.getListOfRegularisationFr().get(i).getBase());
			if ("O".equals(rubrique.getRubrique().getRreg()) && "N".equals(rubrique.getRubrique().getRman()))
				tauxCotisation = rubrique.getListOfRegularisationFr().get(i).getTaux();
			// -- Recherche du montant ET du taux a appliquer
			String valeur1 = "";
			String valeur2 = "";
			//Rhthrubbarem oRhthrubbarem = null;
			ElementSalaireBareme oElementSalaireBareme = null;
			// List listOfBarem = salary.getParameter().isUseRetroactif() ? salary.getService().find(queryStringRetro) : salary.getService().find(queryString);
			String keyOfBaremeList = salary.getParameter().isUseRetroactif() ? crub + this.salary.getPeriodOfPay() + this.salary.getNbul() : crub;
			List listOfBarem = (List) salary.getParameter().getListOfRubriquebaremeMap().get(keyOfBaremeList);
			if (listOfBarem != null)
				for (Object barem : listOfBarem)
				{
					if (salary.getParameter().isUseRetroactif())
					{
//						oRhthrubbarem = (Rhthrubbarem) barem;
//						montant = oRhthrubbarem.getMont().doubleValue();
//						taux = oRhthrubbarem.getTaux().doubleValue();
//						valeur1 = oRhthrubbarem.getVal1();
//						valeur2 = oRhthrubbarem.getVal2();
					}
					else
					{
						oElementSalaireBareme = (ElementSalaireBareme) barem;
//						montant = oElementSalaireBareme.getMont().doubleValue();
//						taux = oElementSalaireBareme.getTaux().doubleValue();
//						valeur1 = oElementSalaireBareme.getVal1();
//						valeur2 = oElementSalaireBareme.getVal2();
						montant = rubrique.getTauxOuMontantBareme(oElementSalaireBareme, false);
						taux = rubrique.getTauxOuMontantBareme(oElementSalaireBareme, true);
						valeur1 = rubrique.getValeurBareme(oElementSalaireBareme, true);
						valeur2 = rubrique.getValeurBareme(oElementSalaireBareme, false);
					}
					//
					montant = convertToNumber37(valeur2, rubrique.getListOfRegularisationFr().get(i).getPlafond());
					if (salary.getParameter().isPbWithCalulation())
					{
						return false;
					}
					salary.getValeurRubriquePartage().setPlafond(montant);
					//
					if (salary.getValeurRubriquePartage().getInter() <= salary.getValeurRubriquePartage().getPlafond())
						break;
					plaf1 = salary.getValeurRubriquePartage().getPlafond();
					plaf2 = convertToNumber37(valeur1, rubrique.getListOfRegularisationFr().get(i).getPlafond());
					if (salary.getParameter().isPbWithCalulation())
					{
						return false;
					}
					reste = plaf1 - plaf2;
					if ("O".equals(rubrique.getRubrique().getRreg()) && "N".equals(rubrique.getRubrique().getRman()))
						inter = reste * tauxCotisation / 100;
					else
					{
						inter = reste * taux / 100;
						salary.getValeurRubriquePartage().setBasePlafonnee(reste);
					}
					//
					montant = salary.getValeurRubriquePartage().getAmount() + inter;
					salary.getValeurRubriquePartage().setAmount(montant);
					//
					if ("N".equals(rubrique.getRubrique().getRreg()) && "O".equals(rubrique.getRubrique().getRman()))
						salary.getValeurRubriquePartage().setRates(taux);
					else
						salary.getValeurRubriquePartage().setRates(tauxCotisation);
				}// foreach
			//
			montant = convertToNumber37(valeur1, rubrique.getListOfRegularisationFr().get(i).getPlafond());
			if (salary.getParameter().isPbWithCalulation())
			{
				return false;
			}
			salary.getValeurRubriquePartage().setPlafond(montant);
			//
			if (salary.getValeurRubriquePartage().getPlafond() > salary.getValeurRubriquePartage().getInter())
			{
				if ("N".equals(rubrique.getRubrique().getRreg()) && "O".equals(rubrique.getRubrique().getRman()))
				{
					reste = 0;
					salary.getValeurRubriquePartage().setRates(taux);
				}
				else
				{
					reste = 0;
					salary.getValeurRubriquePartage().setRates(tauxCotisation);
				}
			}
			else
			{
				reste = salary.getValeurRubriquePartage().getInter() - salary.getValeurRubriquePartage().getPlafond();
				if ((taux != 0) && "O".equals(rubrique.getRubrique().getRreg()) && "N".equals(rubrique.getRubrique().getRman()))
					tauxCotisation = 0;
				if ("O".equals(rubrique.getRubrique().getRreg()) && "N".equals(rubrique.getRubrique().getRman()))
					inter = tauxCotisation * reste / 100;
				else
				{
					inter = taux * reste / 100;
					if (taux <= 0)
						salary.getValeurRubriquePartage().setBasePlafonnee(reste);
				}
				if (inter > 0)
				{
					montant = salary.getValeurRubriquePartage().getAmount() + inter;
					salary.getValeurRubriquePartage().setAmount(inter);
				}
				if ((taux != 0) &&( "N".equals(rubrique.getRubrique().getRreg()) || "O".equals(rubrique.getRubrique().getRman())))
					salary.getValeurRubriquePartage().setRates(taux);
			}
		}
		//
		return true;
	}
	
	public boolean algo38(ClsRubriqueClone rubrique)
	{
		ClsParubqClone rub = rubrique.getRubrique();
		String crub = rub.getComp_id().getCrub();
		ClsInfoSalaryClone info = this.salary.infoSalary;
		String cdos = info.getComp_id().getCdos();
		String nmat = info.getComp_id().getNmat();
		ClsNomenclatureUtil util = this.salary.getUtilNomenclature();
		String err = "";
		
		double w_inf = 0;
		double w_sup = 0;

		ClsValeurRubriquePartage pa_algo = salary.getValeurRubriquePartage();
		pa_algo.setAmount(0);
		pa_algo.setRates(0);
		
		tempNumber = util.getAmountOrRateFromT99(salary.parameter.listOfTable99Map, cdos, 99, "IRG", 1, this.salary.getNlot(), this.salary.moisPaieCourant, this.salary.periodOfPay, ClsEnumeration.EnColumnToRead.RATES);
		if (tempNumber != null)
			pa_algo.setRates(tempNumber.doubleValue());
		else
		{
			err = "Algo 38 " + " Sal : " + nmat + ". Taux 1 Param IRG table 99 non renseigne. " + "Verifier paramétrage";
			err = salary.getParameter().errorMessage(err, salary.getParameter().getLangue());
			salary.getParameter().setError(err);
			return false;
		}

		tempNumber = util.getAmountOrRateFromT99(salary.parameter.listOfTable99Map, cdos, 99, "IRG", 1, this.salary.getNlot(), this.salary.moisPaieCourant, this.salary.periodOfPay, ClsEnumeration.EnColumnToRead.AMOUNT);
		if (tempNumber != null)
			w_inf = tempNumber.doubleValue();
		else
		{
			err = "Algo 38 " + " Sal : " + nmat + ". Montant 1 Param IRG table 99 non renseigne. " + "Verifier paramétrage";
			err = salary.getParameter().errorMessage(err, salary.getParameter().getLangue());
			salary.getParameter().setError(err);
			return false;
		}

		tempNumber = util.getAmountOrRateFromT99(salary.parameter.listOfTable99Map, cdos, 99, "IRG", 2, this.salary.getNlot(), this.salary.moisPaieCourant, this.salary.periodOfPay, ClsEnumeration.EnColumnToRead.AMOUNT);
		if (tempNumber != null)
			w_sup = tempNumber.doubleValue();
		else
		{
			err = "Algo 38 " + " Sal : " + nmat + ". Montant 2 Param IRG table 99 non renseigne. " + "Verifier paramétrage";
			err = salary.getParameter().errorMessage(err, salary.getParameter().getLangue());
			salary.getParameter().setError(err);
			return false;
		}

		//		 -- Calcul de l'abattement
		//		 -- La base est le montant brut imposable soumis é l'IRG
		pa_algo.setAmount(pa_algo.getBase() * pa_algo.getRates() / 100);

		if (pa_algo.getAmount() > w_sup)
			pa_algo.setAmount(w_sup);
		else if (pa_algo.getAmount() < w_inf && pa_algo.getBase() > w_inf)
			pa_algo.setAmount(w_inf);
		else if (pa_algo.getAmount() < w_inf && pa_algo.getBase() < w_inf)
			pa_algo.setAmount(pa_algo.getBase());

		//pa_algo.setRates(40);
		   
		
//		err = "R" + rub.getComp_id().getCrub() + "*, " + "S" + info.getComp_id().getNmat() + "*, " + "A" + rub.getAlgo() + "*. " + "Tb " + rub.getTabl() + ", " + "Cle *" + Cle_acces + "*: " + "Montant 2 incorrect.";
//		err = salary.getParameter().errorMessage(err, salary.getParameter().getLangue());
//		salary.getParameter().setError(err);
//		return false;
		
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo4(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo4(ClsRubriqueClone rubrique)
	{
		
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo4";
		
		
		
		
//		if (this.salary.getAnciennete() == 0)
//		{
//			salary.getValeurRubriquePartage().setRates(0);
//			salary.getValeurRubriquePartage().setAmount(0);
//			return true;
//		}
		String formattedMonth = ClsStringUtil.formatNumber(this.salary.getAnciennete(), "00");
		if('O' == salary.getParameter().getGenfile()) outputtext += "Anciennete du salarié = " + formattedMonth;
		String RateOrAmount = rubrique.getRubrique().getToum();
		ClsEnumeration.EnColumnToRead colToRead = "T".equals(RateOrAmount) ? ClsEnumeration.EnColumnToRead.RATES : ClsEnumeration.EnColumnToRead.AMOUNT;
		double amountOrRateValue = 0;
		int tabl = 0;
		double val = -1;
		tempNumber = null;
		if (rubrique.getRubrique().getTabl() != null)
		{
			tabl = Integer.valueOf(rubrique.getRubrique().getTabl());
			tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,this.salary.getInfoSalary().getComp_id().getCdos(), tabl, formattedMonth,
					rubrique.getRubrique().getNutm(), this.salary.getNlot(), this.salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), colToRead, rubrique.getRubrique().getComp_id().getCrub());
		}
		if (tempNumber == null)
		{
			salary.getParameter().setPbWithCalulation(true);
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90056", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat(), formattedMonth, rubrique.getRubrique().getTabl()));
			return false;
		}
		amountOrRateValue = tempNumber.doubleValue();
		salary.getValeurRubriquePartage().setValeur(amountOrRateValue);
		if ("M".equals(rubrique.getRubrique().getToum()))
		{
			salary.getValeurRubriquePartage().setRates(0);
			salary.getValeurRubriquePartage().setAmount(amountOrRateValue);
		}
		else if ("T".equals(rubrique.getRubrique().getToum()))
		{
			salary.getValeurRubriquePartage().setRates(amountOrRateValue);
			val = salary.getValeurRubriquePartage().getBase() * salary.getValeurRubriquePartage().getRates() / 100;
			salary.getValeurRubriquePartage().setAmount(val);
		}
		else if ("D".equals(rubrique.getRubrique().getToum()))
		{
			salary.getValeurRubriquePartage().setRates(amountOrRateValue);
			val = salary.getValeurRubriquePartage().getBase() * salary.getValeurRubriquePartage().getRates();
			salary.getValeurRubriquePartage().setAmount(val);
			// spécifique edm
//			if (StringUtils.equals(rubrique.getSalary().getParameter().nomClient, ClsEntreprise.EDM))
//			{
//				if(amountOrRateValue == 0)
//					salary.getValeurRubriquePartage().setAmount(0);
//				else
//				{
//					val = salary.getValeurRubriquePartage().getBase() / salary.getValeurRubriquePartage().getRates();
//					salary.getValeurRubriquePartage().setAmount(val);
//				}
//			}
			
		}
		else
		{
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90058", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), salary.getInfoSalary().getComp_id().getNmat(),
							rubrique.getRubrique().getComp_id().getCrub()));
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo40(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo40(ClsRubriqueClone rubrique)
	{
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo41(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo41(ClsRubriqueClone rubrique)
	{
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo41";
		String cdos = salary.getInfoSalary().getComp_id().getCdos();
		Double montant = new Double(0);
		String a_cacc = "";
		//
		salary.getValeurRubriquePartage().setRates(0);
		salary.getValeurRubriquePartage().setAmount(0);
		String strCle1 = rubrique.getRubrique().getCle1();
		String strCle2 = rubrique.getRubrique().getCle2();
		String val = null;
		String crub = rubrique.getRubrique().getComp_id().getCrub();
		
		String valeur1 = StringUtils.EMPTY;
		String valeur2 = StringUtils.EMPTY;
		//On peut concatener plusieurs informations en mettant dans cle1 038;040 et dans cle2 001;002 afin d'avoir la concatenation de la catégorie, echelon,niv1 et niv2
		if(StringUtils.isNotBlank(strCle1))
		{
			String[] strCles1 = StringUtils.split(strCle1,";");
			for(String str : strCles1)
			{
				val = StringUtils.EMPTY;
				if(StringUtils.isNotBlank(str))
				{
					Character ch = str.toCharArray()[0];
					if (Character.isLetter(ch) && ch =='C') val = StringUtils.substring(str, 1, 5);
					else
					{
						if(NumberUtils.isNumber(str)) val = concateneCle1(Integer.valueOf(str), crub);
						if (salary.getParameter().isPbWithCalulation()) return false;
					}
				}
				valeur1 +=val;
			}
		}
		
		if(StringUtils.isNotBlank(strCle2))
		{
			String[] strCles2 = StringUtils.split(strCle2,";");
			for(String str : strCles2)
			{
				val = StringUtils.EMPTY;
				if(StringUtils.isNotBlank(str))
				{
					Character ch = str.toCharArray()[0];
					if (Character.isLetter(ch) && ch =='C') val = StringUtils.substring(str, 1, 5);
					else
					{
						if(NumberUtils.isNumber(str)) val = concateneCle2(Integer.valueOf(str), crub);
						//if (salary.getParameter().isPbWithCalulation()) return false;
					}
				}
				valeur2 +=val;
			}
		}
		a_cacc = valeur1+valeur2;
		
		
//		int cle1 = ClsObjectUtil.isNull(strCle1) ? 0 : Integer.valueOf(strCle1);
//		int cle2 = ClsObjectUtil.isNull(strCle2) ? 0 : Integer.valueOf(strCle2);
//		a_cacc = concatene(cle1, cle2, rubrique.getRubrique().getComp_id().getCrub());
		
		if(StringUtils.isBlank(a_cacc))
			return true;
		if (salary.getParameter().isPbWithCalulation())
		{
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "...Probléme de calcul!";
			return false;
		}
		//
		// -- Lecture de la valeur dans les nomenclatures
		ClsEnumeration.EnColumnToRead col = "M".equals(rubrique.getRubrique().getToum()) ? ClsEnumeration.EnColumnToRead.AMOUNT : ClsEnumeration.EnColumnToRead.RATES;
		tempNumber = null;
		if (rubrique.getRubrique().getTabl() != null)
		{
			tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,cdos, Integer.valueOf(rubrique.getRubrique().getTabl()), a_cacc, rubrique.getRubrique().getNutm(), salary.getNlot(),
					salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), col, rubrique.getRubrique().getComp_id().getCrub());
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "----------------------------->Montant de la table " + rubrique.getRubrique().getTabl() + " = " + montant;
		}
		
		if (tempNumber == null)
		{
			// logger
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90056", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat(), a_cacc, rubrique.getRubrique().getTabl()));
			salary.getParameter().setPbWithCalulation(true);
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + salary.getParameter().getError();
			return false;
		}
		
		montant = tempNumber.doubleValue();
		salary.getValeurRubriquePartage().setValeur(montant);
		// -- Calcul du montant
		if ("T".equals(rubrique.getRubrique().getToum()))
		{
			if (salary.getValeurRubriquePartage().getBase() == 0)
			{
//				if(StringUtil.notEquals(rubrique.getSalary().getParameter().nomClient, ClsEntreprise.BRASSERIES_BBLOME))
//				{
					salary.getValeurRubriquePartage().setBase(salary.getValeurRubriquePartage().getValeur());
					salary.getValeurRubriquePartage().setBasePlafonnee(salary.getValeurRubriquePartage().getValeur());
					salary.getValeurRubriquePartage().setAmount(salary.getValeurRubriquePartage().getValeur());
//				}
			}
			else
			{
				salary.getValeurRubriquePartage().setRates(salary.getValeurRubriquePartage().getValeur());
				montant = salary.getValeurRubriquePartage().getBase() * salary.getValeurRubriquePartage().getRates() / 100;
				salary.getValeurRubriquePartage().setAmount(montant);
			}
		}
		else if ("D".equals(rubrique.getRubrique().getToum()))
		{
			salary.getValeurRubriquePartage().setRates(salary.getValeurRubriquePartage().getValeur());
			if (salary.getValeurRubriquePartage().getRates() != 0)
			{
				montant = salary.getValeurRubriquePartage().getBase() / salary.getValeurRubriquePartage().getRates();
				salary.getValeurRubriquePartage().setAmount(montant);
			}
			else
				salary.getValeurRubriquePartage().setAmount(0);
		}
		else if ("M".equals(rubrique.getRubrique().getToum()))
		{
			salary.getValeurRubriquePartage().setBase(salary.getValeurRubriquePartage().getValeur());
			salary.getValeurRubriquePartage().setBasePlafonnee(salary.getValeurRubriquePartage().getValeur());
			salary.getValeurRubriquePartage().setAmount(salary.getValeurRubriquePartage().getValeur());
			salary.getValeurRubriquePartage().setRates(0);
		}
		else if ("R".equals(rubrique.getRubrique().getToum()))
		{
			salary.getValeurRubriquePartage().setBase(salary.getValeurRubriquePartage().getValeur());
			salary.getValeurRubriquePartage().setBasePlafonnee(salary.getValeurRubriquePartage().getValeur());
			salary.getValeurRubriquePartage().setAmount(salary.getValeurRubriquePartage().getValeur());
			salary.getValeurRubriquePartage().setRates(0);
		}
		else
		{
			// logger
			String error = salary.getParameter().errorMessage("ERR-90058", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), salary.getInfoSalary().getComp_id().getNmat(),
					rubrique.getRubrique().getComp_id().getCrub());
			salary.getParameter().setError(error);
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + salary.getParameter().getError();
			return false;
		}
		return true;
	}
	
	
	public boolean algo42(ClsRubriqueClone rubrique)
	{
		return true;//comilog.algo42(rubrique);
	}

	
	public boolean algo46(ClsRubriqueClone rubrique)
	{
		return true;//comilog.algo46(rubrique);
	}
	
	public boolean algo47(ClsRubriqueClone rubrique)
	{
		return true;//comilog.algo47(rubrique);
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo43(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo43(ClsRubriqueClone rubrique)
	{
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo43";
		String cdos = salary.getInfoSalary().getComp_id().getCdos();
		String crub = rubrique.getRubrique().getComp_id().getCrub();
		Double montant = new Double(0);
		double taux = 0;
		//
		// String queryString = " from ElementSalaireBareme "
		// + " where identreprise='" + cdos + "'"
		// + " and crub = '" + crub + "'"
		// + " order by cdos , crub , nume";
		// //
		// String queryStringRetro = " from Rhthrubbarem "
		// + " where identreprise='" + cdos + "'"
		// + " and crub = '" + crub + "'"
		// + " and aamm = '" + salary.getPeriodOfPay() + "'"
		// + " and nbul = " + salary.getNbul()
		// + " order by cdos , crub , nume";
		//
		salary.getValeurRubriquePartage().setRates(0);
		salary.getValeurRubriquePartage().setAmount(0);
		salary.getParameter().setPbWithCalulation(false);
		// -- Lecture de la donnee designee par la cle1
		String valeur = concat43(Integer.valueOf(rubrique.getRubrique().getCle1()), crub);
		valeur = StringUtils.trim(valeur);
		// w_inter := a_valeur;
		//int inter = Integer.valueOf(valeur);
		valeur = StringUtils.replace(valeur, ",", ".");
		if(StringUtils.isBlank(valeur)) valeur = "0";
		salary.getValeurRubriquePartage().setInter(new BigDecimal(valeur).doubleValue());
		if (salary.getParameter().isPbWithCalulation())
		{
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90058", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), salary.getInfoSalary().getComp_id().getNmat(),
							rubrique.getRubrique().getComp_id().getCrub()));
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + salary.getParameter().getError();
			return false;
		}
		String valeur1 = "";
		String valeur2 = "";
		boolean ok = false;
		int nbreBaremeRubrique = 0;
		double dblValeur1 = 0;
		double dblValeur2 = 0;
		//Rhthrubbarem oRhthrubbarem = null;
		ElementSalaireBareme oElementSalaireBareme = null;
		// List listOfBarem = salary.getParameter().isUseRetroactif() ? salary.getService().find(queryStringRetro) : salary.getService().find(queryString);
		String keyOfBaremeList = salary.getParameter().isUseRetroactif() ? crub + this.salary.getPeriodOfPay() + this.salary.getNbul() : crub;
		List listOfBarem = (List) salary.getParameter().getListOfRubriquebaremeMap().get(keyOfBaremeList);
		if (listOfBarem != null)
			for (Object barem : listOfBarem)
			{
				if (salary.getParameter().isUseRetroactif())
				{
//					oRhthrubbarem = (Rhthrubbarem) barem;
//					montant = oRhthrubbarem.getMont() != null ? oRhthrubbarem.getMont().doubleValue() : 0;
//					taux = oRhthrubbarem.getTaux() != null ?oRhthrubbarem.getTaux().doubleValue() : 0;
//					valeur1 = oRhthrubbarem.getVal1();
//					valeur2 = oRhthrubbarem.getVal2();
				}
				else
				{
					oElementSalaireBareme = (ElementSalaireBareme) barem;
//					montant = oElementSalaireBareme.getMont() != null ? oElementSalaireBareme.getMont().doubleValue() : 0;
//					taux = oElementSalaireBareme.getTaux() != null ?oElementSalaireBareme.getTaux().doubleValue() : 0;
//					valeur1 = oElementSalaireBareme.getVal1();
//					valeur2 = oElementSalaireBareme.getVal2();
					montant = rubrique.getTauxOuMontantBareme(oElementSalaireBareme, false);
					taux = rubrique.getTauxOuMontantBareme(oElementSalaireBareme, true);
					valeur1 = rubrique.getValeurBareme(oElementSalaireBareme, true);
					valeur2 = rubrique.getValeurBareme(oElementSalaireBareme, false);
				}
				//
				nbreBaremeRubrique++;
				if (nbreBaremeRubrique > 50)
				{
					nbreBaremeRubrique = 50;
					break;
				}
				//
				if (!ClsObjectUtil.isNull(valeur1) && !ClsObjectUtil.isNull(valeur2))
				{
					dblValeur1 = rubrique.convertToNumber(valeur1);
					dblValeur2 = rubrique.convertToNumber(valeur2);
					if (salary.getValeurRubriquePartage().getInter() >= dblValeur1 && salary.getValeurRubriquePartage().getInter() <= dblValeur2)
					{
						ok = true;
						break;
					}
					
				}
			}
		//
		if (nbreBaremeRubrique == 0)
		{
			// logger
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90061", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat()));
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + salary.getParameter().getError();
			return false;
		}
		// -- Calcul de base, taux et montant
		if (! ok)
		{
			salary.getValeurRubriquePartage().setRates(0);
			salary.getValeurRubriquePartage().setAmount(0);
		}
		else
		{
			if ("T".equals(rubrique.getRubrique().getTxmt()))
			{
				salary.getValeurRubriquePartage().setRates(taux);
				double montant1 = salary.getValeurRubriquePartage().getBase() * salary.getValeurRubriquePartage().getRates() / 100;
				salary.getValeurRubriquePartage().setAmount(montant1);
			}
			else if ("M".equals(rubrique.getRubrique().getTxmt()))
			{
				salary.getValeurRubriquePartage().setBase(montant);
				salary.getValeurRubriquePartage().setBasePlafonnee(montant);
				salary.getValeurRubriquePartage().setAmount(montant);
			}
			else if ("D".equals(rubrique.getRubrique().getTxmt()))
			{
				salary.getValeurRubriquePartage().setRates(taux);
				if (taux != 0)
					salary.getValeurRubriquePartage().setAmount(salary.getValeurRubriquePartage().getBase() / taux);
				else
					salary.getValeurRubriquePartage().setAmount(0);
			}
			else
			{
				//
				salary.getParameter().setError(
						salary.getParameter().errorMessage("ERR-90058", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), salary.getInfoSalary().getComp_id().getNmat(),
								rubrique.getRubrique().getComp_id().getCrub()));
				if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + salary.getParameter().getError();
				return false;
			}
		}
		return true;
	}
	
	public boolean algo44(ClsRubriqueClone rubrique)
	{
		double divbascg = 0;
		double coefbascg = 0;
		  
		  salary.infoSalary.setDapa(NumberUtils.convert(NumberUtils.nvl(salary.infoSalary.getDapa(), 0)));

		  salary.infoSalary.setDapec(NumberUtils.convert(NumberUtils.nvl(salary.infoSalary.getDapec(), 0)));

		  salary.infoSalary.setDded(NumberUtils.convert(NumberUtils.nvl(salary.infoSalary.getDded(), 0)));
		  
		  double base = salary.getValeurRubriquePartage().getBase();
		  base = base + salary.infoSalary.getDapa().doubleValue() + salary.infoSalary.getDapec().doubleValue();
		  salary.getValeurRubriquePartage().setBase(base);
		  salary.getValeurRubriquePartage().setBasePlafonnee(base);

		  //-- Lecture du diviseur de la base conges
		 
		  
		 Integer tabl = Integer.valueOf(rubrique.getRubrique().getTabl());
		 tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,this.salary.getInfoSalary().getComp_id().getCdos(), tabl, salary.infoSalary.getCat(),
					1, this.salary.getNlot(), this.salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(),  ClsEnumeration.EnColumnToRead.AMOUNT, rubrique.getRubrique().getComp_id().getCrub());
		
		 if(tempNumber != null)
			 divbascg = tempNumber.doubleValue();
		 
		  if(divbascg == 0)
		{
		     salary.getParameter().setError(
						salary.getParameter().errorMessage("ERR-90062", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(),
								rubrique.getRubrique().getComp_id().getCrub(), salary.getInfoSalary().getComp_id().getNmat(), salary.infoSalary.getComp_id().getNmat(), tabl, salary.infoSalary.getCat()));
				if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + salary.getParameter().getError();
				return false;
		}

		   //-- Lecture du coefficient de la base conges
		  
		   tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,this.salary.getInfoSalary().getComp_id().getCdos(), tabl, salary.infoSalary.getCat(),
					4, this.salary.getNlot(), this.salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(),  ClsEnumeration.EnColumnToRead.RATES, rubrique.getRubrique().getComp_id().getCrub());
		
		   if(tempNumber != null)
			   coefbascg = tempNumber.doubleValue();
			 
			  if(coefbascg == 0)
			{
			     salary.getParameter().setError(
							salary.getParameter().errorMessage("ERR-90078", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(),
									rubrique.getRubrique().getComp_id().getCrub(), salary.getInfoSalary().getComp_id().getNmat(), salary.infoSalary.getComp_id().getNmat(), tabl, salary.infoSalary.getCat()));
					if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + salary.getParameter().getError();
					return false;
			}

		   double mont = ((base / divbascg) * coefbascg )- salary.infoSalary.getDded().doubleValue();
		   salary.getValeurRubriquePartage().setAmount(mont);

		  return true;
	}
	
	public boolean algo45(ClsRubriqueClone rubrique)
	{
	  salary.infoSalary.setJapa(NumberUtils.convert(NumberUtils.nvl(salary.infoSalary.getJapa(), 0)));

	  salary.infoSalary.setJapec(NumberUtils.convert(NumberUtils.nvl(salary.infoSalary.getJapec(), 0)));

	  salary.getValeurRubriquePartage().setBasePlafonnee(salary.getValeurRubriquePartage().getBase());
	  salary.getValeurRubriquePartage().setAmount((salary.getValeurRubriquePartage().getBase()/30) *  (salary.infoSalary.getJapec().add(salary.infoSalary.getJapa()).doubleValue()));

	  return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo48(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo48(ClsRubriqueClone rubrique)
	{
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo48";
		String cdos = salary.getInfoSalary().getComp_id().getCdos();
		// String crub = rubrique.getRubrique().getComp_id().getCrub();
		Double montant = new Double(0);
		// double taux = 0;
		double divBaseConge = 0;
		double divBaseSex = 0;
		double nbreJourSuppl = 0;
		// -- Pas de calcul si pas de fictif et WMDP != du premier mois de conges
		if ("N".equals(salary.getParameter().getFictiveCalculus()) && ! salary.getPeriodOfPay().equals(salary.getInfoSalary().getPmcf()))
		{
			salary.getValeurRubriquePartage().setBase(0);
			salary.getValeurRubriquePartage().setBasePlafonnee(0);
			salary.getValeurRubriquePartage().setRates(0);
			salary.getValeurRubriquePartage().setAmount(0);
			return true;
		}
		// -- Pas de calcul si pas de conges
		if (salary.getWorkTime().getNbreJourConges() == 0 && salary.getWorkTime().getNbreJourCongesAnnuelMoisSuiv() == 0)
		{
			salary.getValeurRubriquePartage().setBase(0);
			salary.getValeurRubriquePartage().setBasePlafonnee(0);
			salary.getValeurRubriquePartage().setRates(0);
			salary.getValeurRubriquePartage().setAmount(0);
			return true;
		}
		// -- Pas de calcul si montant du conges deja calcule
		if (salary.getWorkTime().getMontantCongePonctuel() != 0)
		{
			salary.getValeurRubriquePartage().setAmount(salary.getWorkTime().getMontantCongePonctuel());
			return true;
		}
		// IF PA_CALCUL.wsal01.dapa IS NULL THEN
		// PA_CALCUL.wsal01.dapa := 0;
		if (salary.getInfoSalary().getDapa() == null || salary.getInfoSalary().getDapa().doubleValue() == 0)
			salary.getInfoSalary().setDapa(new BigDecimal(0));
		// END IF;
		// IF PA_CALCUL.wsal01.dapec IS NULL THEN
		// PA_CALCUL.wsal01.dapec := 0;
		// PA_CALCUL.wsal01.dapa := 0;
		if (salary.getInfoSalary().getDapec() == null || salary.getInfoSalary().getDapec().doubleValue() == 0)
			salary.getInfoSalary().setDapec(new BigDecimal(0));
		// END IF;
		// IF PA_CALCUL.wsal01.dded IS NULL THEN
		// PA_CALCUL.wsal01.dded := 0;
		// PA_CALCUL.wsal01.dapa := 0;
		if (salary.getInfoSalary().getDded() == null || salary.getInfoSalary().getDded().doubleValue() == 0)
			salary.getInfoSalary().setDded(new BigDecimal(0));
		// END IF;
		//
		if (salary.getWorkTime().isDebutDeMois())
			salary.getValeurRubriquePartage().setBase(0);
		//
		montant = salary.getValeurRubriquePartage().getBase() + salary.getInfoSalary().getDapa().doubleValue() + salary.getInfoSalary().getDapec().doubleValue();
		salary.getValeurRubriquePartage().setBase(montant);
		salary.getValeurRubriquePartage().setBasePlafonnee(montant);
		//
		// -- Lecture du diviseur de la base conges
		// IF PA_CALCUL.retroactif THEN
		List l = null;
		if (salary.getParameter().isUseRetroactif())
		{
			// BEGIN
			// SELECT SUM(DECODE(nume,1,valm)),
			// SUM(DECODE(nume,2,valm))
			// INTO divbascg, divbasex
			// FROM pahfnom
			// WHERE cdos = PA_CALCUL.wpdos.cdos
			// AND ctab = PA_CALCUL.t_rub.tabl
			// AND cacc = PA_CALCUL.wsal01.cat
			// AND nume IN (1,2)
			// AND aamm = PA_CALCUL.w_aamm
			// AND nbul = PA_CALCUL.wsd_fcal1.nbul;
			// EXCEPTION
			// WHEN NO_DATA_FOUND THEN null;
			// END;
			l = salary.getService().find(
					"select sum(valm) from Rhthfnom" + " where identreprise='" + cdos + "'" + " and ctab = " + rubrique.getRubrique().getTabl() + " and cacc = '"
							+ salary.getInfoSalary().getCat() + "'" + " and aamm = '" + salary.getPeriodOfPay() + "'" + " and nbul = " + salary.getNbul() + " and nume = 1");
			if (!ClsObjectUtil.isListEmty(l))
				divBaseConge = (Double) l.get(0);
			//
			l = salary.getService().find(
					"select sum(valm) from Rhthfnom" + " where identreprise='" + cdos + "'" + " and ctab = '" + rubrique.getRubrique().getTabl() + "'" + " and cacc = '"
							+ salary.getInfoSalary().getCat() + "'" + " and aamm = '" + salary.getPeriodOfPay() + "'" + " and nbul = " + salary.getNbul() + " and nume = 2");
			if (!ClsObjectUtil.isListEmty(l))
				divBaseSex = (Double) l.get(0);
		}
		// ELSE
		else
		{
			// BEGIN
			// SELECT SUM(DECODE(nume,1,valm)),
			// SUM(DECODE(nume,2,valm))
			// INTO divbascg, divbasex
			// FROM pafnom
			// WHERE cdos = PA_CALCUL.wpdos.cdos
			// AND ctab = PA_CALCUL.t_rub.tabl
			// AND cacc = PA_CALCUL.wsal01.cat
			// AND nume IN (1,2);
			// EXCEPTION
			// WHEN NO_DATA_FOUND THEN null;
			// END;
			l = salary.getService().find(
					"select sum(valm) from ParamData" + " where identreprise='" + cdos + "'" + " and ctab = '" + rubrique.getRubrique().getTabl() + "'" + " and cacc = '"
							+ salary.getInfoSalary().getCat() + "'" + " and nume = 1");
			if (!ClsObjectUtil.isListEmty(l))
				divBaseConge = (Double) l.get(0);
			//
			l = salary.getService().find(
					"select sum(valm) from ParamData" + " where identreprise='" + cdos + "'" + " and ctab = '" + rubrique.getRubrique().getTabl() + "'" + " and cacc = '"
							+ salary.getInfoSalary().getCat() + "'" + " and nume = 2");
			if (!ClsObjectUtil.isListEmty(l) &&  l.get(0) != null)
				divBaseSex = (Double) l.get(0);
		}
		if (divBaseConge <= 0)
		{
			//
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90062", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat(), rubrique.getRubrique().getTabl(), salary.getInfoSalary().getCat()));
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + salary.getParameter().getError();
			return false;
		}
		if (!ClsObjectUtil.isNull(salary.getParameter().getExpatrieValeur()))
		{
			if (salary.getParameter().getExpatrieTypeContrat() == 1)
			{
				if (salary.getParameter().getExpatrieValeur().equals(salary.getInfoSalary().getRegi()))
					divBaseConge = divBaseSex;
			}
			else if (salary.getParameter().getExpatrieTypeContrat() == 2)
			{
				if (salary.getParameter().getExpatrieValeur().equals(salary.getInfoSalary().getTypc()))
					divBaseConge = divBaseSex;
			}
			else if (salary.getParameter().getExpatrieTypeContrat() == 3)
			{
				if (salary.getParameter().getExpatrieValeur().equals(salary.getInfoSalary().getClas()))
					divBaseConge = divBaseSex;
			}
		}
		if ("N".equals(salary.getParameter().getFictiveCalculus()))
		{
			if (salary.getPeriodOfPay().equals(salary.getInfoSalary().getPmcf()))
				salary.getWorkTime().setNbreJourConges(salary.getInfoSalary().getNbjcf().intValue());
			else
				salary.getWorkTime().setNbreJourConges(0);
		}
		//
		salary.getValeurRubriquePartage().setRates(salary.getWorkTime().getNbreJourConges() + salary.getWorkTime().getNbreJourCongesNonPris());
		montant = (salary.getValeurRubriquePartage().getBase() / divBaseConge) - salary.getInfoSalary().getDded().doubleValue();
		salary.getValeurRubriquePartage().setAmount(montant);
		//
		if (salary.getInfoSalary().getNbjaf() != null && salary.getInfoSalary().getNbjaf().intValue() != 0 && salary.getInfoSalary().getNbjaf().doubleValue() != 0
				&& "O".equals(salary.getParameter().getFictiveCalculus()))
		{
			montant = salary.getValeurRubriquePartage().getAmount() * (salary.getValeurRubriquePartage().getRates() / salary.getInfoSalary().getNbjaf().intValue());
			salary.getValeurRubriquePartage().setAmount(montant);
		}
		//
		nbreJourSuppl = salary.calculNombreDJourSuppl();
		
		if ((salary.getValeurRubriquePartage().getRates() - nbreJourSuppl) != 0)
		{
			montant = salary.getValeurRubriquePartage().getAmount() * (salary.getValeurRubriquePartage().getRates() / (salary.getValeurRubriquePartage().getRates() - nbreJourSuppl));
			salary.getValeurRubriquePartage().setAmount(montant);
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo5(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo5(ClsRubriqueClone rubrique)
	{
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo5";
		
		
		
		
		String codeRubrique = rubrique.getRubrique().getComp_id().getCrub();
		double amountValue = 0;
		double rateValue = 0;
		Number amountValueNumber = null;
		Number rateValueNumber = null;
		int tabl = 0;
		double val = 0;
		if ("M".equals(rubrique.getRubrique().getToum()))
		{
			if (rubrique.getRubrique().getTabl() != null)
			{
				tabl = Integer.valueOf(rubrique.getRubrique().getTabl());
				if (tabl == 51 || tabl == 52 || tabl == 99)
				{
					amountValueNumber = this.salary.getUtilNomenclature().getAmountOrRateFromT99(salary.getParameter().getListOfTable99Map(), this.salary.getInfoSalary().getComp_id().getCdos(), tabl,
							codeRubrique, 1, this.salary.getNlot(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.AMOUNT);
				}
				else
				{
					amountValueNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,this.salary.getInfoSalary().getComp_id().getCdos(), tabl, codeRubrique, 1, this.salary.getNlot(),
							this.salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.AMOUNT, rubrique.getRubrique().getComp_id().getCrub());
				}
			}
		}
		else if (rubrique.getRubrique().getTabl() != null)
		{
			tabl = Integer.valueOf(rubrique.getRubrique().getTabl());
			if (tabl == 51 || tabl == 52 || tabl == 99)
			{
				rateValueNumber = this.salary.getUtilNomenclature().getAmountOrRateFromT99(salary.getParameter().getListOfTable99Map(), this.salary.getInfoSalary().getComp_id().getCdos(), tabl,
						codeRubrique, 1, this.salary.getNlot(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.RATES);
			}
			else
			{
				rateValueNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,this.salary.getInfoSalary().getComp_id().getCdos(), tabl, codeRubrique, 1, this.salary.getNlot(),
						this.salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.RATES, rubrique.getRubrique().getComp_id().getCrub());
			}
		}
		if ((amountValueNumber == null && "M".equals(rubrique.getRubrique().getToum())) || (rateValueNumber == null && (!"M".equals(rubrique.getRubrique().getToum()))))
		{
			// logger
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90056", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat(), codeRubrique, rubrique.getRubrique().getTabl()));
			return false;
		}
		amountValue = amountValueNumber == null ? 0 : amountValueNumber.doubleValue();
		rateValue = rateValueNumber == null ? 0 : rateValueNumber.doubleValue();
		
		if ("T".equals(rubrique.getRubrique().getToum()))
		{
			salary.getValeurRubriquePartage().setRates(rateValue);
			val = salary.getValeurRubriquePartage().getBase() * salary.getValeurRubriquePartage().getRates() / 100;
			salary.getValeurRubriquePartage().setAmount(val);
		}
		else if ("D".equals(rubrique.getRubrique().getToum()))
		{
			salary.getValeurRubriquePartage().setRates(rateValue);
			if(rateValue != 0)
			{
				val = salary.getValeurRubriquePartage().getBase() / salary.getValeurRubriquePartage().getRates();
				salary.getValeurRubriquePartage().setAmount(val);
			}
		}
		else if ("M".equals(rubrique.getRubrique().getToum()))
		{
			salary.getValeurRubriquePartage().setRates(0);
			salary.getValeurRubriquePartage().setAmount(amountValue);
			salary.getValeurRubriquePartage().setBase(amountValue);
			salary.getValeurRubriquePartage().setBasePlafonnee(amountValue);
		}
		else if ("R".equals(rubrique.getRubrique().getToum()))
		{
			salary.getValeurRubriquePartage().setRates(0);
			salary.getValeurRubriquePartage().setAmount(rateValue);
			salary.getValeurRubriquePartage().setBase(rateValue);
			salary.getValeurRubriquePartage().setBasePlafonnee(rateValue);
		}
		else
		{
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90058", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), salary.getInfoSalary().getComp_id().getNmat(),
							rubrique.getRubrique().getComp_id().getCrub()));
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo50(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo50(ClsRubriqueClone rubrique)
	{
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo50";
		Double montant = new Double(0);
		double divBaseConge = 0;
		double coefBaseConge = 0;
		//
		if (salary.getParameter().isStc())
		{
			if (ClsObjectUtil.isNull(salary.getInfoSalary().getDapa()))
				salary.getInfoSalary().setDapa(new BigDecimal(0));
			if (ClsObjectUtil.isNull(salary.getInfoSalary().getDapec()))
				salary.getInfoSalary().setDapec(new BigDecimal(0));
			if (ClsObjectUtil.isNull(salary.getInfoSalary().getDded()))
				salary.getInfoSalary().setDded(new BigDecimal(0));
			montant = salary.getValeurRubriquePartage().getBase() + salary.getInfoSalary().getDapa().doubleValue() + salary.getInfoSalary().getDapec().doubleValue();
			salary.getValeurRubriquePartage().setBase(montant);
			salary.getValeurRubriquePartage().setBasePlafonnee(montant);
			// -- Lecture du diviseur de la base conges
			tempNumber = null;
			if (rubrique.getRubrique().getTabl() != null)
			{
				tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,salary.getInfoSalary().getComp_id().getCdos(), Integer.valueOf(rubrique.getRubrique().getTabl()),
						salary.getInfoSalary().getCat(), 1, salary.getNlot(), salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.AMOUNT, rubrique.getRubrique().getComp_id().getCrub());
			}
			if (tempNumber == null || tempNumber.intValue() == 0)
			{
				//
				salary.getParameter().setError(
						salary.getParameter().errorMessage("ERR-90062", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
								salary.getInfoSalary().getComp_id().getNmat(), rubrique.getRubrique().getTabl(), salary.getInfoSalary().getCat()));
				if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + salary.getParameter().getError();
				return false;
			}
			
			montant = tempNumber.doubleValue();
			//
			divBaseConge = montant;
			// -- Lecture du coefficient de la base conges
			tempNumber = null;
			if (rubrique.getRubrique().getTabl() != null)
			{
				tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,salary.getInfoSalary().getComp_id().getCdos(), Integer.valueOf(rubrique.getRubrique().getTabl()),
						salary.getInfoSalary().getCat(), 4, salary.getNlot(), salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.RATES, rubrique.getRubrique().getComp_id().getCrub());
			}
			if (tempNumber == null || tempNumber.intValue() == 0)
			{
				//
				salary.getParameter().setError(
						salary.getParameter().errorMessage("ERR-90078", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
								salary.getInfoSalary().getComp_id().getNmat(), rubrique.getRubrique().getTabl(), salary.getInfoSalary().getCat()));
				if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + salary.getParameter().getError();
				return false;
			}
			montant = tempNumber.doubleValue();
			//
			coefBaseConge = montant;
			//
			salary.getValeurRubriquePartage().setRates(salary.getInfoSalary().getJapa().add(salary.getInfoSalary().getJapec()).doubleValue());
			montant = ((salary.getValeurRubriquePartage().getBase() / divBaseConge) * coefBaseConge) - salary.getInfoSalary().getDded().doubleValue();
			salary.getValeurRubriquePartage().setAmount(montant);
			//
			return true;
		}
		// -- ----- Pas de calcul si pas de fictif et WMDP != du premier mois de conges
		if (("N".equals(salary.getParameter().getFictiveCalculus()) &&  ! salary.getPeriodOfPay().equals(salary.getInfoSalary().getPmcf())) ||( salary.getWorkTime().getNbreJourConges() == 0
				&& salary.getWorkTime().getNbreJourCongesAnnuelMoisSuiv() == 0))
		{
			//
			salary.getValeurRubriquePartage().setBase(0);
			salary.getValeurRubriquePartage().setBasePlafonnee(0);
			salary.getValeurRubriquePartage().setRates(0);
			salary.getValeurRubriquePartage().setAmount(0);
			//
			return true;
		}
		//
		if (ClsObjectUtil.isNull(salary.getInfoSalary().getDapa()))
			salary.getInfoSalary().setDapa(new BigDecimal(0));
		if (ClsObjectUtil.isNull(salary.getInfoSalary().getDapec()))
			salary.getInfoSalary().setDapec(new BigDecimal(0));
		if (ClsObjectUtil.isNull(salary.getInfoSalary().getDded()))
			salary.getInfoSalary().setDded(new BigDecimal(0));
		//
		if (salary.getWorkTime().isDebutDeMois())
			salary.getValeurRubriquePartage().setBase(0);
		//
		montant = salary.getValeurRubriquePartage().getBase() + salary.getInfoSalary().getDapa().add(salary.getInfoSalary().getDapec()).doubleValue();
		salary.getValeurRubriquePartage().setBase(montant);
		salary.getValeurRubriquePartage().setBasePlafonnee(montant);
		//
		// -- Lecture du diviseur de la base conges
		tempNumber = null;
		if (rubrique.getRubrique().getTabl() != null)
		{
			tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,salary.getInfoSalary().getComp_id().getCdos(), Integer.valueOf(rubrique.getRubrique().getTabl()),
					salary.getInfoSalary().getCat(), 1, salary.getNlot(), salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.AMOUNT, rubrique.getRubrique().getComp_id().getCrub());
		}
		if (tempNumber == null || tempNumber.intValue() == 0)
		{
			//
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90062", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat(), rubrique.getRubrique().getTabl(), salary.getInfoSalary().getCat()));
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + salary.getParameter().getError();
			return false;
		}
		
		montant = tempNumber.doubleValue();
		//
		divBaseConge = montant;
		//
		// -- Lecture du coefficient de la base conges
		tempNumber = null;
		if (rubrique.getRubrique().getTabl() != null)
		{
			tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,salary.getInfoSalary().getComp_id().getCdos(), Integer.valueOf(rubrique.getRubrique().getTabl()),
					salary.getInfoSalary().getCat(), 4, salary.getNlot(), salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.RATES, rubrique.getRubrique().getComp_id().getCrub());
		}
		if (tempNumber == null || tempNumber.intValue() == 0)
		{
			// logger
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90078", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat(), rubrique.getRubrique().getTabl(), salary.getInfoSalary().getCat()));
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + salary.getParameter().getError();
			return false;
		}
		montant = tempNumber.doubleValue();
		//
		coefBaseConge = montant;
		//
		if ("N".equals(salary.getParameter().getFictiveCalculus()))
		{
			if (salary.getPeriodOfPay().equals(salary.getInfoSalary().getPmcf()))
				salary.getWorkTime().setNbreJoursAbsencePourCongeAnnuel(salary.getInfoSalary().getNbjaf().intValue());
			else
				salary.getWorkTime().setNbreJoursAbsencePourCongeAnnuel(0);
		}
		//
		salary.getValeurRubriquePartage().setRates(salary.getWorkTime().getNbreJoursAbsencePourCongeAnnuel() + salary.getWorkTime().getNbreJourCongesAnnuelPayeNonPris());
		montant = (salary.getValeurRubriquePartage().getBase() / divBaseConge) * coefBaseConge - salary.getInfoSalary().getDded().doubleValue() - salary.getWorkTime().getMontantCongePonctuel();
		salary.getValeurRubriquePartage().setAmount(montant);
		//
		if ("O".equals(salary.getParameter().getFictiveCalculus()) && !ClsObjectUtil.isNull(salary.getInfoSalary().getNbjaf()))
		{
			montant = salary.getValeurRubriquePartage().getAmount() * salary.getValeurRubriquePartage().getRates() / salary.getInfoSalary().getNbjaf().intValue();
			salary.getValeurRubriquePartage().setAmount(montant);
		}
		//
		return true;
	}
	
//	---------------------------------------------------------------------------------
//	--              Calcul du nombre de congé acquis au cours du mois              --
//	----------------------------------------------------------------------
	public boolean algo52(ClsRubriqueClone rubrique)
	{
		
		
//		if (StringUtil.notEquals(rubrique.getSalary().getParameter().nomClient, ClsEntreprise.CNSS))
//		{
			String cdos = this.salary.getInfoSalary().getComp_id().getCdos();
			//--age enfant é charge
			int ageEnfantACharge = 0;
			
			tempNumber = this.salary.getUtilNomenclature().getAmountOrRateFromT99(salary.getParameter().getListOfTable99Map(), cdos , 99,
					"AGEENFAN", 1, this.salary.getNlot(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.AMOUNT);
			if(tempNumber != null) ageEnfantACharge = tempNumber.intValue();
			
			//--montant par enfant é charge
			double montantParEnfant = 0;
			tempNumber = this.salary.getUtilNomenclature().getAmountOrRateFromT99(salary.getParameter().getListOfTable99Map(), cdos, 99,
					"AGEENFAN", 2, this.salary.getNlot(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.AMOUNT);
			if(tempNumber != null) montantParEnfant = tempNumber.doubleValue();
			
			int nbec = 0;
			String query = "select count(*) from Rhtenfantagent" + " where identreprise='" + cdos + "'" + " and nmat = '" + salary.getInfoSalary().getComp_id().getNmat() + "'" + " and achg='O' ";
//			if(TypeBDUtil.typeBD.equalsIgnoreCase(TypeBDUtil.IN))
				query+=" and months_between(last_day(to_date('"+this.salary.getMoisPaieCourant()+"01','%Y%m%d')),dtna)/12 <= "+ageEnfantACharge;
//			else query+=" and months_between(last_day(to_date("+this.salary.getMoisPaieCourant()+",'yyyymm')),dtna)/12 <= "+ageEnfantACharge;
			nbec = Integer.valueOf(salary.getService().find(query).get(0).toString());
			
			salary.getValeurRubriquePartage().setRates(0);
			salary.getValeurRubriquePartage().setBase(nbec);
			salary.getValeurRubriquePartage().setBasePlafonnee(nbec);
			salary.getValeurRubriquePartage().setAmount(nbec * montantParEnfant);
			
		//}
		 
		
		
		
		
		// spécifique cnss
//		if (StringUtils.equals(rubrique.getSalary().getParameter().nomClient, ClsEntreprise.CNSS))
//		{
////			FUNCTION algo52 RETURN BOOLEAN
////			IS
////
////				w_nume		INTEGER:=0;
//			int nume = 0;
////				w_prorat	pafnom.valm%type;
//			double prorata = 0;
////				w_nbjtr		NUMBER(5,2);
//			double nbrJoursTravaille;
////
////			BEGIN
////
////
////				BEGIN
////				   SELECT valm INTO w_prorat
////				   FROM pafnom
////				   WHERE cdos = PA_CALCUL.wpdos.cdos
////				   AND ctab = 99
////			   	   AND cacc = 'CG-MONT'
////				   AND nume = 5;
////				EXCEPTION
////				    WHEN NO_DATA_FOUND THEN
////				       w_prorat := 0;
////				END;
//
//			tempNumber = this.salary.getUtilNomenclature().getAmountOrRateFromT99(salary.getParameter().getListOfTable99Map(), this.salary.getInfoSalary().getComp_id().getCdos(), 99,
//					"CG-MONT", 5, this.salary.getNlot(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.AMOUNT);
//			if(tempNumber != null)
//				prorata = tempNumber.doubleValue();
//
////			if w_prorat=1 then
////			w_nbjtr:=30-(PA_CALCUL.wnbjc+PA_CALCUL.wnbjc_ma);
////
////		else
////			w_nbjtr:=30;
////		end if;
////
////
////		IF PA_CALCUL.Expatrie THEN
////			w_nume:=2;
////		ELSE
////			w_nume:=1;
////		END IF;
//			if(prorata == 1)
//				nbrJoursTravaille = 30 - salary.workTime.getNbreJourConges()  + salary.workTime.getNbreJourCongesAnnuelMoisAnte();
//			else
//				nbrJoursTravaille = 30;
//
//			if(salary.isExpatrie())
//				nume = 2;
//			else
//				nume = 1;
////			--on regarde si il existe dans la table 35, taux 3
////			BEGIN
////				SELECT valt INTO w_tau FROM pafnom WHERE ctab=35 AND cacc= PA_CALCUL.wsal01.cat AND nume=3;
////			EXCEPTION
////				WHEN NO_DATA_FOUND THEN
////					w_tau := 0;
////			END;
////
////			IF w_tau = 0 THEN --Le taux n'a pas été récupéré, on essaie sur la clé 'NBJC-DEF'
////				BEGIN
////					SELECT valt INTO w_tau FROM pafnom WHERE ctab=99 AND cacc='NBJC-DEF' AND nume=1;
////				EXCEPTION
////					WHEN NO_DATA_FOUND THEN
////						w_tau := 0;
////				END;
////
////			END IF;
//			double taux = 0;
//			tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,salary.getInfoSalary().getComp_id().getCdos(), 35,
//					salary.getInfoSalary().getCat(), 3, salary.getNlot(), salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.RATES, rubrique.getRubrique().getComp_id().getCrub());
//			if(tempNumber != null)
//				taux = tempNumber.doubleValue();
//
//			if(taux == 0)
//			{
//				tempNumber = this.salary.getUtilNomenclature().getAmountOrRateFromT99(salary.getParameter().getListOfTable99Map(), this.salary.getInfoSalary().getComp_id().getCdos(), 99,
//						"NBJC-DEF",1, this.salary.getNlot(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.AMOUNT);
//				if(tempNumber != null)
//					taux = tempNumber.doubleValue();
//			}
//
////			IF w_tau > 0 THEN
////			--		w_mon := (w_tau *(w_nbjtr-PA_CALCUL.wnbja+PA_CALCUL.wnbja_ma))/30;
////					w_mon := (w_tau *(w_nbjtr-PA_CALCUL.wnbja+PA_CALCUL.wnbja_donnant_conge))/PA_CALCUL.wnbjt;
////
////				ELSE
////					w_mon :=0;
////				END IF;
////
////
////				PA_CALCUL.wnbj_acquis:=w_mon;
//			double montant = 0;
//			if(taux > 0)
//			{
//				montant = (taux*(nbrJoursTravaille +salary.workTime.getNbreJourAbsence() + salary.workTime.getNbrJoursAbsencesDonnantConge())) / salary.workTime.getProrataNbreJourTravaillees();
//			}
//
//			salary.workTime.setNbrJoursAcquis(montant);
//
//		}
		return true;
	}
	
	/**
	 * ---------------------------------------------------------------------------------
-- Calcul d une rubrique qui cumul le montant d une autre rubrique             --
-- du mois de retour de conge jqa le mois de paie courant                      --
---------------------------------------------------------------------------------
	 */
	public boolean algo59(ClsRubriqueClone rubrique)
	{
//		w4    	 NUMBER(4);
		Integer w4;
//		  w_prem     VARCHAR2(6);
		String w_prem;
//		  mntcum     PACUMU.MONT%TYPE;
		BigDecimal mntcum = new BigDecimal(0);
//		  c_cum99    VARCHAR2(6);
		String c_cum99;


//		BEGIN
//		   w_tau    := 0;
//		   w_mon    := 0;
//		   w_valeur := 0;
		salary.getValeurRubriquePartage().setValeur(0);
		salary.getValeurRubriquePartage().setAmount(0);
		salary.getValeurRubriquePartage().setRates(0);
//		   char4 := PA_CALCUL.t_rub.crub ;
		String char4 = rubrique.getRubrique().getComp_id().getCrub();

//		   BEGIN
//		      SELECT valm INTO w4 FROM pafnom
//		       WHERE cdos = PA_CALCUL.wpdos.cdos
//		         AND ctab = TO_NUMBER(PA_CALCUL.t_rub.tabl)
//		         AND cacc = char4
//		         AND nume = 1;
//		   EXCEPTION
//		      WHEN NO_DATA_FOUND THEN null;
//		   END;
//		   IF SQL%NOTFOUND THEN
//		      PA_CALCUL.err_msg := 'ALGO '|| PA_CALCUL.t_rub.algo||
//		                 ' Rbq '|| PA_CALCUL.t_rub.crub||
//		                 ' Sal '|| PA_CALCUL.wsal01.nmat||
//		                 '. Cle '|| char4 || ' inexistante en table '||
//		                 PA_CALCUL.t_rub.tabl;
//		      RETURN FALSE;
//		   END IF;
		Integer tabl = Integer.valueOf(rubrique.getRubrique().getTabl());
		tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,salary.getInfoSalary().getComp_id().getCdos(), tabl,
				char4, 1, salary.getNlot(), salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.AMOUNT, rubrique.getRubrique().getComp_id().getCrub());
		if (tempNumber == null)
		{
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "..................................ERROR on montant4!";
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90056", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat(), char4, rubrique.getRubrique().getTabl()));
			// logger
			return false;
		}
//		 IF PA_PAIE.NouZ(w4) THEN
//	      PA_CALCUL.err_msg := 'ALGO '|| PA_CALCUL.t_rub.algo||
//	                 ' Sal '|| PA_CALCUL.wsal01.nmat||
//	                 ' Rbq '|| PA_CALCUL.t_rub.crub||
//	                 '. Cle '|| char4 ||
//	                 ' Table '|| PA_CALCUL.t_rub.tabl||
//	                 ' Montant 1 mal renseigne';
//	      RETURN FALSE;
//	   END IF;
		w4 = tempNumber.intValue();
		if (tempNumber.intValue() == 0)
		{
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "..................................ERROR on montant4!";
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90065", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat(), char4, rubrique.getRubrique().getTabl()));
			// logger
			return false;
		}

		  

//		   w_prem := TO_CHAR(PA_CALCUL.wsal01.drtcg,'YYYYMM');
		w_prem = new ClsDate(this.salary.getInfoSalary().getDrtcg() != null ? this.salary.getInfoSalary().getDrtcg() : this.salary.getInfoSalary().getDtes()).getYearAndMonth();
//		   IF PA_PAIE.NouB(w_prem) THEN
//		      w_prem := TO_CHAR(PA_CALCUL.wsal01.dtes,'YYYYMM');
//		   END IF;

//		   IF PA_PAIE.NouB(w_prem) THEN
//		      PA_CALCUL.err_msg := 'ALGO '|| PA_CALCUL.t_rub.algo||
//		                 ' Sal '|| PA_CALCUL.wsal01.nmat||
//		                 ' Rbq '|| PA_CALCUL.t_rub.crub||
//		                 '. Date dernier conge ou '||
//		                 '. Date entree dans la societe'||
//		                 ' mal renseigne';
//		      RETURN FALSE;
//		   END IF;
		if(StringUtils.isBlank(w_prem))
		{
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "..................................ERROR on montant4!";
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90070", salary.getParameter().getLangue(), salary.getInfoSalary().getComp_id().getNmat()));
			// logger
			return false;
		}
//		   c_cum99 := substr(w_prem,1,4)||'99';
		c_cum99 = StringUtil.oraSubstring(w_prem, 1, 4)+"99";


//		   SELECT sum(NVL(mont,0)) INTO mntcum FROM pacumu
//		    WHERE cdos = PA_CALCUL.wpdos.cdos
//		      AND nmat = PA_CALCUL.wsal01.nmat
//		      AND aamm >= w_prem
//		      AND aamm <  w_aamm
//		      AND aamm != c_cum99
//		      AND rubq =  w4
//		      AND nbul = 9;
		String crub = rubrique.getRubrique().getComp_id().getCrub();
		String cdos = rubrique.getRubrique().getComp_id().getCdos();
		String nmat = this.salary.getInfoSalary().getComp_id().getNmat();
		String aamm = this.salary.getMonthOfPay();
		String query="SELECT sum(mont)  FROM CumulPaie WHERE identreprise='"+cdos+"' and nmat = '"+nmat+"' ";
		query+=" and aamm >= '"+w_prem+"' and aamm < '"+aamm+"' and aamm != '"+c_cum99+"' and rubq = '"+ClsStringUtil.formatNumber(w4,ParameterUtil.formatRubrique)+"' and nbul = 9";
		
		List lst = salary.getService().find(query);
		
		if(! lst.isEmpty() && lst.get(0) != null)
			mntcum = ClsObjectUtil.getBigDecimalFromObject(lst.get(0));
		
		ClsRubriqueClone clone = this.getSalary().findRubriqueClone(ClsStringUtil.formatNumber(w4,ParameterUtil.formatRubrique));
		if(clone != null)
			mntcum = mntcum.add(new BigDecimal(clone.getAmount()));
		
//		w_bas := PA_CALCUL.t9999_mont(w4)+ NVL(mntcum,0);
//		   w_mon := w_bas;
		salary.getValeurRubriquePartage().setBase(mntcum.doubleValue());
		salary.getValeurRubriquePartage().setAmount(mntcum.doubleValue());

//		   RETURN TRUE;
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo6(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo6(ClsRubriqueClone rubrique)
	{
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo6";
		salary.getValeurRubriquePartage().setAmount(0);
		salary.getValeurRubriquePartage().setRates(0);
		Object[] obj = null;
		
		//Pour Sobraga, cet algo ne concerne que les rubrique provennant des EV
		//if(!rubrique.isRubriqueEV() && StringUtils.equals(ClsEntreprise.SOBRAGA, salary.parameter.nomClient)) return true;
		
		if (rubrique.getListOfElementVariable() != null && rubrique.getListOfElementVariable().size() > 0)
		{
			obj = (Object[]) rubrique.getListOfElementVariable().get(rubrique.getNumElementVarCourant());
			if (obj != null)
			{
				// if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>obj = " + ((BigDecimal)obj[0]).doubleValue());
				if (salary.getParameter().isUseRetroactif())
					rubrique.setElementVariableTransit((new BigDecimal( obj[0].toString())).doubleValue());
				else
					rubrique.setElementVariableTransit((new BigDecimal(obj[0].toString())).doubleValue());
			}
		}
		double val = 0;
		// if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>toum = " + rubrique.getRubrique().getToum());
		if ("T".equals(rubrique.getRubrique().getToum()))
		{
			salary.getValeurRubriquePartage().setRates(rubrique.getElementVariableTransit());
			val = salary.getValeurRubriquePartage().getBase() * salary.getValeurRubriquePartage().getRates() / 100;
			salary.getValeurRubriquePartage().setAmount(val);
		}
		else if ("M".equals(rubrique.getRubrique().getToum()))
		{
			salary.getValeurRubriquePartage().setRates(rubrique.getElementVariableTransit());
			val = salary.getValeurRubriquePartage().getBase() * salary.getValeurRubriquePartage().getRates();
			salary.getValeurRubriquePartage().setAmount(val);
		}
		else if ("D".equals(rubrique.getRubrique().getToum()))
		{
			if (rubrique.getElementVariableTransit() != 0)
			{
				salary.getValeurRubriquePartage().setRates(rubrique.getElementVariableTransit());
				val = salary.getValeurRubriquePartage().getBase() / salary.getValeurRubriquePartage().getRates();
				salary.getValeurRubriquePartage().setAmount(val);
			}
			else
				salary.getValeurRubriquePartage().setAmount(0);
		}
		else
		{
			// logger
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90058", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), salary.getInfoSalary().getComp_id().getNmat(),
							rubrique.getRubrique().getComp_id().getCrub()));
			return false;
		}
		//
		// if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "..amount :" + salary.getValeurRubriquePartage().getAmount());
		return true;
	}

	public boolean algo60(ClsRubriqueClone rubrique)
	{
		// TODO Auto-generated method stub
		return false;
	}

	String A61_Per1;
	String A61_Per2;
	int A61_Nb_Mois;
	int A61_Ind_Mois;

	double A61_Info;
	double A61_I1;
	double A61_I2;
	Date A61_Date1;
	Date A61_Date2;
	String A61_dtad;
	String A61_dtrd;
	String A61_Rubq;
	
	public boolean algo61(ClsRubriqueClone rubrique)
	{
		ClsParubqClone rub = rubrique.getRubrique();
		String crub = rub.getComp_id().getCrub();
		ClsInfoSalaryClone info = this.salary.infoSalary;

		ClsValeurRubriquePartage pa_algo = salary.getValeurRubriquePartage();
		pa_algo.setAmount(0);
		pa_algo.setBase(0);
		pa_algo.setRates(0);
//		 ---------------------------------------------------------------------
//		   -- Lecture du parametrage
//		   ---------------------------------------------------------------------
//		   -- Constitution de la cle
		String Cle_acces = "A" + ClsStringUtil.formatNumber(rub.getAlgo(), "00") + "-" + crub + "-1";
		//		 -- Lecture de l'enregistrement
		//		 -- W_Retour := Lec_pafnom10( PA_CALCUL.t_rub.tabl, Cle_acces);
		if (!lec_ParamData10(rub, rub.getTabl(), Cle_acces))
			return false;

		//		 ---------------------------------------------------------------------
		//		   -- Lecture du numero identifiant l'information a traiter
		//		   ---------------------------------------------------------------------
		A61_Info = wfnom10.mnt[1];

		//		   ---------------------------------------------------------------------
		//		   -- Controle du parametrage ET Recherche de l'information
		//		   ---------------------------------------------------------------------
		String err;
		boolean w_retour;
		if (A61_Info == 1)
		{
			//			   ---------------------------------------------------------------------
			//			      --PA_ALGO.w_mon = Anciennete de l'agent
			//			      ---------------------------------------------------------------------
			if (wfnom10.mnt[2] != 0 && wfnom10.mnt[2] != 1)
			{
				err = "R" + rub.getComp_id().getCrub() + "*, " + "S" + info.getComp_id().getNmat() + "*, " + "A" + rub.getAlgo() + "*. " + "Tb " + rub.getTabl() + ", " + "Cle *" + Cle_acces + "*: " + "Montant 2 incorrect.";
				err = salary.getParameter().errorMessage(err, salary.getParameter().getLangue());
				salary.getParameter().setError(err);
				return false;
			}
			int i = new ClsDate(salary.getInfoSalary().getDdca()).getMonth();
			int j = salary.getMyMonthOfPay().getMonth();
			j = j - i;
			if (j < 0)
				j = j + 12;
			pa_algo.setBase(NumberUtils.toDouble(new ClsDate(info.getDdca(), "YYYYMMDD")));
			pa_algo.setAmount(salary.getAnciennete());
			pa_algo.setRates(j);
			if (wfnom10.mnt[2] == 1 && new ClsDate(info.getDdca()).getMonth() != new ClsDate(salary.parameter.periodOfPay, "yyyyMM").getMonth())
				pa_algo.setAmount(0);
		}
		else if (A61_Info == 2)
		{
			//			   ---------------------------------------------------------------------
			//			      --PA_ALGO.w_mon = Nb de mois travailles (dtes) sur periode (T/S/A)
			//			      ---------------------------------------------------------------------
			//			      -- Test du parametrage 
			if (wfnom10.mnt[2] != 0 && wfnom10.mnt[2] != 1)
			{
				err = "R" + rub.getComp_id().getCrub() + "*, " + "S" + info.getComp_id().getNmat() + "*, " + "A" + rub.getAlgo() + "*. " + "Tb " + rub.getTabl() + ", " + "Cle *" + Cle_acces + "*: " + "Montant 2 incorrect.";
				err = salary.getParameter().errorMessage(err, salary.getParameter().getLangue());
				salary.getParameter().setError(err);
				return false;
			}
			if (StringUtil.notIn(wfnom10.lib[2], "T,S,A"))
			{
				err = "R" + rub.getComp_id().getCrub() + "*, " + "S" + info.getComp_id().getNmat() + "*, " + "A" + rub.getAlgo() + "*. " + "Tb " + rub.getTabl() + ", " + "Cle *" + Cle_acces + "*: " + "Libelle 2 incorrect.";
				err = salary.getParameter().errorMessage(err, salary.getParameter().getLangue());
				salary.getParameter().setError(err);
				return false;
			}

			//-- Calcul periodes debut et fin
			w_retour = this.Deb_Fin_Periode(wfnom10.lib[2]);

			//-- Calcul Nombre de mois
			pa_algo.setBase(NumberUtils.toDouble(new ClsDate(info.getDtes()).getDateS("yyyyMMdd")));
			pa_algo.setRates(A61_Ind_Mois);

			if (StringUtil.isLesserThan(new ClsDate(info.getDtes()).getYearAndMonth(), A61_Per1))
				pa_algo.setAmount(A61_Ind_Mois);
			else if (StringUtil.isGreaterThan(new ClsDate(info.getDtes()).getYearAndMonth(), A61_Per2))
				pa_algo.setAmount(0);
			else
			{
				A61_I1 = NumberUtils.toDouble(StringUtil.oraSubstring(A61_Per1, 5, 2));
				A61_I2 = new ClsDate(info.getDtes()).getMonth();
				pa_algo.setAmount(A61_Ind_Mois - (A61_I2 - A61_I1));
			}
			if (wfnom10.mnt[2] == 0 && pa_algo.getAmount() > 0)
				pa_algo.setAmount(pa_algo.getAmount() - 1);

		}
		else if (A61_Info == 3)
		{
			//			   ---------------------------------------------------------------------
			//			      --PA_ALGO.w_mon = 1 = Agent titularise
			//			      ---------------------------------------------------------------------
			//			      NULL;
		}
		else if (A61_Info == 4)
		{
			//			   ---------------------------------------------------------------------
			//			      --PA_ALGO.w_mon = pasa01.nbec Nb d"enfants a charge
			//			      --PA_ALGO.w_bas = pasa01.nbef Nb d"enfants total
			//			      ---------------------------------------------------------------------
			pa_algo.setBase(info.getNbef());
			pa_algo.setAmount(info.getNbec());
		}
		else if (A61_Info == 6)
		{
			//			   ---------------------------------------------------------------------
			//			      --PA_ALGO.w_mon = Nb de jours calendaires du mois
			//			      ---------------------------------------------------------------------
			pa_algo.setBase(salary.parameter.nbreJourMoisPourProrata);
			pa_algo.setAmount(pa_algo.getBase());
		}
		else if (A61_Info == 10)
		{
			//			   ---------------------------------------------------------------------
			//			      --PA_ALGO.w_mon = Indice du mois sur la periode
			//			      ---------------------------------------------------------------------
			if (StringUtil.notIn(wfnom10.lib[2], "T,S,A"))
			{
				err = "R" + rub.getComp_id().getCrub() + "*, " + "S" + info.getComp_id().getNmat() + "*, " + "A" + rub.getAlgo() + "*, " + "Libelle 2 incorrect.";
				err = salary.getParameter().errorMessage(err, salary.getParameter().getLangue());
				salary.parameter.setError(err);
				return false;
			}
			//-- Calcul periodes debut et fin
			w_retour = this.Deb_Fin_Periode(wfnom10.lib[2]);
			A61_I1 = NumberUtils.toDouble(StringUtil.oraSubstring(A61_Per1, 5, 2));
			A61_I2 = new ClsDate(salary.parameter.periodOfPay, "yyyyMM").getMonth();
			pa_algo.setAmount(A61_I2 - A61_I1 + 1);
			if (pa_algo.getAmount() < 1)
				pa_algo.setAmount(pa_algo.getAmount() + A61_Nb_Mois);
		}
		else if (A61_Info == 11)
		{
			//			   ---------------------------------------------------------------------
			//			      --PA_ALGO.w_mon = 1 = Age de l"Agent
			//			      ---------------------------------------------------------------------
			pa_algo.setBase(NumberUtils.toDouble(new ClsDate(info.getDtna()).getDateS("yyyyMMdd")));
			pa_algo.setAmount(salary.getAgeOfAgent());
		}
		else if (A61_Info == 12)
		{
			//			   ---------------------------------------------------------------------
			//			      --PA_ALGO.w_mon = Nb de mois d"adhesion a une caisse / une periode
			//			      ---------------------------------------------------------------------
			//-- Test du parametrage
			if (wfnom10.mnt[2] != 0 && wfnom10.mnt[2] != 1)
			{
				err = "R" + rub.getComp_id().getCrub() + "*, " + "S" + info.getComp_id().getNmat() + "*, " + "A" + rub.getAlgo() + "*, " + "Montant 2 incorrect.";
				err = salary.getParameter().errorMessage(err, salary.getParameter().getLangue());
				salary.parameter.setError(err);
				return false;
			}
			A61_Rubq = ClsStringUtil.formatNumber(wfnom10.mnt[3], ParameterUtil.formatRubrique);
			if (salary.findRubriqueClone(A61_Rubq) == null)
			{
				err = "R" + rub.getComp_id().getCrub() + "*, " + "S" + info.getComp_id().getNmat() + "*, " + "A" + rub.getAlgo() + "*, " + "Montant 3 incorrect.";
				err = salary.getParameter().errorMessage(err, salary.getParameter().getLangue());
				salary.parameter.setError(err);
				return false;
			}

			if (StringUtil.notIn(wfnom10.lib[2], "T,S,A"))
			{
				err = "R" + rub.getComp_id().getCrub() + "*, " + "S" + info.getComp_id().getNmat() + "*, " + "A" + rub.getAlgo() + "*, " + "Libelle 2 incorrect.";
				err = salary.getParameter().errorMessage(err, salary.getParameter().getLangue());
				salary.parameter.setError(err);
				return false;
			}
			//-- Lecture des dates adhesion et radiation
			String sql = "From CaisseMutuelleSalarie where identreprise='" + rub.getComp_id().getCdos() + "'";
			sql += " and nmat = '" + info.getComp_id().getNmat() + "'";
			sql += " (and rscm = '" + A61_Rubq + "' or rpcm ='" + A61_Rubq + "')";
			List<CaisseMutuelleSalarie> lst = salary.service.find(sql);
			if (lst.isEmpty())
			{
				pa_algo.setBase(0);
				pa_algo.setAmount(0);
				return true;
			}
			else
			{
				A61_Date1 = lst.get(0).getDtad();
				A61_Date2 = lst.get(0).getDtrd();
			}
			//-- Calcul periodes debut et fin
			w_retour = this.Deb_Fin_Periode(wfnom10.lib[2]);
			//-- Date d"adhesion
			if (A61_Date1 == null)
			{
				pa_algo.setBase(0);
				pa_algo.setAmount(0);
				return true;
			}
			pa_algo.setBase(NumberUtils.toDouble(new ClsDate(A61_Date1).getDateS("yyyyMMdd")));
			A61_dtad = new ClsDate(A61_Date1).getYearAndMonth();
			//-- Date de radiation
			if (A61_Date2 == null)
				A61_Date2 = new ClsDate("31/12/2999", "dd/MM/yyyy").getDate();

			A61_dtrd = new ClsDate(A61_Date2).getYearAndMonth();
			//-- Calcul Nombre de mois

			if (A61_dtrd.compareTo(A61_Per1) < 0)
				pa_algo.setAmount(0);
			else if (A61_dtad.compareTo(A61_Per2) > 0)
			{
				//-- Salarie pas encore adhere
				pa_algo.setAmount(0);
			}
			else if (A61_dtad.compareTo(A61_Per1) < 0 && A61_dtrd.compareTo(A61_Per2) > 0)
			{
				//-- Salarie sur toute la periode
				pa_algo.setAmount(A61_Nb_Mois);
			}
			else if (A61_dtad.compareTo(A61_Per1) > 0 && A61_dtrd.compareTo(A61_Per2) < 0)
			{
				A61_I1 = NumberUtils.toDouble(StringUtil.oraSubstring(A61_dtad, 5, 2));
				A61_I2 = NumberUtils.toDouble(StringUtil.oraSubstring(A61_dtrd, 5, 2));
				pa_algo.setAmount(A61_I1 - A61_I1 + 1);
			}
			else if (A61_dtad.compareTo(A61_Per1) < 0)
			{
				A61_I1 = NumberUtils.toDouble(StringUtil.oraSubstring(A61_Per1, 5, 2));
				A61_I2 = NumberUtils.toDouble(StringUtil.oraSubstring(A61_dtrd, 5, 2));
				pa_algo.setAmount(A61_I1 - A61_I1 + 1);
			}
			else
			{
				A61_I1 = NumberUtils.toDouble(StringUtil.oraSubstring(A61_dtad, 5, 2));
				A61_I2 = NumberUtils.toDouble(StringUtil.oraSubstring(A61_Per2, 5, 2));
				pa_algo.setAmount(A61_I1 - A61_I1 + 1);
			}
			if (wfnom10.mnt[2] == 0 && pa_algo.getAmount() > 0)
				pa_algo.setAmount(pa_algo.getAmount() - 1);
		}
		else if (A61_Info == 14)
		{
			//			   ---------------------------------------------------------------------
			//			      --PA_ALGO.w_mon =PA_ALGO.w_bas si mois anniversaire sur ddca
			//			      ---------------------------------------------------------------------
			if (new ClsDate(salary.parameter.periodOfPay, "yyyyMM").getMonth() == new ClsDate(info.getDdca()).getMonth())
				pa_algo.setAmount(pa_algo.getBase());
		}
		else if (A61_Info == 15)
		{
//			---------------------------------------------------------------------
//		      --PA_ALGO.w_mon = nombre de jours travaillé entre dtes et année courante -X
//		      ---------------------------------------------------------------------
			
			int A61_annee = new ClsDate(salary.parameter.dtDdex).getYear();
			pa_algo.setAmount(A61_annee + wfnom10.mnt[2]);
			A61_Per1 = NumberUtils.toInt(pa_algo.getAmount()) + "" + ClsStringUtil.formatNumber(new ClsDate(salary.parameter.dtDdex).getMonth(),"00");

			A61_Per2 = ClsGeneralUtil.addPer(Integer.valueOf(A61_Per1), 0, 11) + "";

			A61_Date1 = info.getDtes();
			A61_Date2 = ClsDate.last_day(new ClsDate(A61_Per2, "yyyyMM").getDate());

			pa_algo.setAmount(salary.utilNomenclature.getNomberOfDaysBetween(A61_Date1, A61_Date2));

			pa_algo.setAmount(pa_algo.getAmount() + 1); //--Prise en compte du jours d'entrée

			if (pa_algo.getAmount() < 0)
				pa_algo.setAmount(0);
		     
			
		}
		else
		{

			//			   ---------------------------------------------------------------------
			//			      -- !! Cette information n"est pas traitee !!
			//			      ---------------------------------------------------------------------
			err = "S" + info.getComp_id().getNmat() + "*, " + "R" + rub.getComp_id().getCrub() + "*, " + "A" + rub.getAlgo() + "*, " + "Tb " + rub.getTabl() + ", " +
			// --"Cle *" + LTRIM(RTRIM(Cle_acces))   + "*: " +
					"Cle *" + Cle_acces + "*: " + "Info No " + ClsStringUtil.formatNumber(A61_Info, "00") + "* non traitee.";
			err = salary.getParameter().errorMessage(err, salary.getParameter().getLangue());
			salary.parameter.setError(err);
			return false;
		}

		pa_algo.setBasePlafonnee(pa_algo.getBase());
		return true;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private boolean Deb_Fin_Periode(String i_periode)
	{
		String periode;
		String per_deb;
		String per_fin;
		int nb_mois;
		int ind_mois = 0;

		periode = StringUtil.oraSubstring(i_periode, 1, 1);
		per_deb = new ClsDate(salary.parameter.dtDdex).getDateS("yyyyMM");
		per_fin = per_deb;
		if (StringUtils.equalsIgnoreCase(periode, "T"))
		{
			nb_mois = 3;
			if (salary.parameter.rangMoisDePaieExercice <= 3)
			{
				per_fin = ClsGeneralUtil.addPer(Integer.valueOf(per_fin), 0, 2) + "";
				ind_mois = salary.parameter.rangMoisDePaieExercice;
			}
			else if (salary.parameter.rangMoisDePaieExercice <= 6)
			{
				per_deb = ClsGeneralUtil.addPer(Integer.valueOf(per_deb), 0, 3) + "";
				per_fin = ClsGeneralUtil.addPer(Integer.valueOf(per_fin), 0, 5) + "";
				ind_mois = salary.parameter.rangMoisDePaieExercice - 3;
			}
			else if (salary.parameter.rangMoisDePaieExercice <= 9)
			{
				per_deb = ClsGeneralUtil.addPer(Integer.valueOf(per_deb), 0, 6) + "";
				per_fin = ClsGeneralUtil.addPer(Integer.valueOf(per_fin), 0, 8) + "";
				ind_mois = salary.parameter.rangMoisDePaieExercice - 6;
			}
			else if (salary.parameter.rangMoisDePaieExercice <= 12)
			{
				per_deb = ClsGeneralUtil.addPer(Integer.valueOf(per_deb), 0, 9) + "";
				per_fin = ClsGeneralUtil.addPer(Integer.valueOf(per_fin), 0, 11) + "";
				ind_mois = salary.parameter.rangMoisDePaieExercice - 9;
			}
		}
		else if (StringUtils.equalsIgnoreCase(periode, "S"))
		{
			nb_mois = 6;
			if (salary.parameter.rangMoisDePaieExercice <= 6)
			{
				per_fin = ClsGeneralUtil.addPer(Integer.valueOf(per_fin), 0, 5) + "";
				ind_mois = salary.parameter.rangMoisDePaieExercice;
			}
			else
			{
				per_deb = ClsGeneralUtil.addPer(Integer.valueOf(per_deb), 0, 6) + "";
				per_fin = ClsGeneralUtil.addPer(Integer.valueOf(per_fin), 0, 11) + "";
				ind_mois = salary.parameter.rangMoisDePaieExercice - 6;
			}
		}
		else
		{
			nb_mois = 12;
			ind_mois = salary.parameter.rangMoisDePaieExercice;
			per_fin = ClsGeneralUtil.addPer(Integer.valueOf(per_fin), 0, 11) + "";
		}

		A61_Per1 = per_deb;
		A61_Per2 = per_fin;
		A61_Nb_Mois = nb_mois;
		A61_Ind_Mois = ind_mois;
		return true;
	}

	//	-----------------------------------------------------------------------
	//	-- Lecture de pafnom pour 10 Libelles, Montants et taux
	//	-----------------------------------------------------------------------
	private boolean lec_ParamData10(ClsParubqClone rubq, String strNumTable, String cleN)
	{
		Integer num_table = Integer.valueOf(strNumTable);
		String cle10 = StringUtil.oraSubstring(cleN, 1, 10);
		//	   ---------------------------------------------------------------------
		//	   -- Test existence parametre
		//	   ---------------------------------------------------------------------
		String err;
		ParamData nome = (ParamData) this.salary.service.find("FROM ParamData WHERE identreprise="+rubq.getComp_id().getCdos()+" AND ctab="+num_table+" AND cacc='"+cle10+"' AND nume=1");
		if (nome == null)
		{
			err = "A" + rubq.getAlgo() + ", S" + this.salary.infoSalary.getComp_id().getNmat() + ", R" + rubq.getComp_id().getCrub() + ": Param: *" + cle10 + "* Mq en T" + rubq.getTabl();
			err = salary.getParameter().errorMessage(err, salary.getParameter().getLangue());
			salary.getParameter().setError(err);
			return false;
		}
		//	   ---------------------------------------------------------------------
		//	   -- Lecture de l"enregistrement
		//	   ---------------------------------------------------------------------

		String sql = " select max(case nume when 1 then vall else ' ' end),";
		sql += " max(case nume when 2 then vall else ' ' end), ";
		sql += " max(case nume when 3 then vall else ' ' end), ";
		sql += " max(case nume when 4 then vall else ' ' end), ";
		sql += " max(case nume when 5 then vall else ' ' end), ";
		sql += " max(case nume when 6 then vall else ' ' end), ";
		sql += " max(case nume when 7 then vall else ' ' end), ";
		sql += " max(case nume when 8 then vall else ' ' end), ";
		sql += " max(case nume when 9 then vall else ' ' end), ";
		sql += " max(case nume when 10 then vall else ' ' end), ";

		sql += " sum(case nume when 1 then valm else 0 end), ";
		sql += " sum(case nume when 2 then valm else 0 end), ";
		sql += " sum(case nume when 3 then valm else 0 end), ";
		sql += " sum(case nume when 4 then valm else 0 end), ";
		sql += " sum(case nume when 5 then valm else 0 end), ";
		sql += " sum(case nume when 6 then valm else 0 end), ";
		sql += " sum(case nume when 7 then valm else 0 end), ";
		sql += " sum(case nume when 8 then valm else 0 end), ";
		sql += " sum(case nume when 9 then valm else 0 end), ";
		sql += " sum(case nume when 10 then valm else 0 end), ";

		sql += " sum(case nume when 1 then valt else 0 end), ";
		sql += " sum(case nume when 2 then valt else 0 end), ";
		sql += " sum(case nume when 3 then valt else 0 end), ";
		sql += " sum(case nume when 4 then valt else 0 end), ";
		sql += " sum(case nume when 5 then valt else 0 end), ";
		sql += " sum(case nume when 6 then valt else 0 end), ";
		sql += " sum(case nume when 7 then valt else 0 end), ";
		sql += " sum(case nume when 8 then valt else 0 end), ";
		sql += " sum(case nume when 9 then valt else 0 end), ";
		sql += " sum(case nume when 10 then valt else 0 end) ";

		sql += " From ParamData Where identreprise='" + rubq.getComp_id().getCdos() + "' and ctab = " + num_table + " and cacc = '" + cle10 + "' and nume in (1,2,3,4,5,6,7,8,9,10)";

		List listOfMaxsum = salary.getService().find(sql);
		if (listOfMaxsum.isEmpty())
		{
			err = "A" + rubq.getAlgo() + ", S" + this.salary.infoSalary.getComp_id().getNmat() + ", R" + rubq.getComp_id().getCrub() + ": Param: *" + cle10 + "* Mq en T" + rubq.getTabl();
			err = salary.getParameter().errorMessage(err, salary.getParameter().getLangue());
			salary.getParameter().setError(err);
			return false;
		}
		Object[] o = (Object[]) listOfMaxsum.get(0);
		wfnom10 = new Wfnom();
		for (int i = 1; i <= 10; i++)
		{
			wfnom10.lib[i] = (o[i - 1] != null) ? o[i - 1].toString() : StringUtils.EMPTY;
			wfnom10.mnt[i] = new BigDecimal(o[i + 10 - 1] != null ? o[i + 10 - 1].toString() : "0").doubleValue();
			wfnom10.tau[i] = new BigDecimal(o[i + 20 - 1] != null ? o[i + 20 - 1].toString() : "0").doubleValue();
		}

		return true;
	}

	Wfnom wfnom10 = new Wfnom();

	private class Wfnom
	{
		public String[] lib = new String[] { "", "", "", "", "", "", "", "", "", "", "" };
		public double[] mnt = new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		public double[] tau = new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo62(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo62(ClsRubriqueClone rubrique)
	{
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo62";
		
		
		
		
		// A62_sexe paenfan.sexe%TYPE;
		// A62_pnai paenfan.pnai%TYPE;
		// A62_scol paenfan.scol%TYPE;
		// A62_achg paenfan.achg%TYPE;
		// A62_age1 pafnom.valm%TYPE;
		// A62_age2 pafnom.valm%TYPE;
		// A62_dtna1 VARCHAR2(6);
		// A62_dtna2 VARCHAR2(6);
		// A62_nucj1 paenfan.nucj%TYPE;
		// A62_nucj2 paenfan.nucj%TYPE;
		// Cle_acces VARCHAR2(10);
		// A62_char1 VARCHAR2(1);
		// Nb_enfants NUMBER(5);
		// Annee_courante NUMBER(5);
		// A62_algo VARCHAR2(2);
		// A62_Annee NUMBER(5);
		// A62_Mois NUMBER(5);
		//
		// BEGIN
		// PA_ALGO.w_mon := 0;
		// PA_ALGO.w_tau := 0;
		// PA_ALGO.w_bas := 0;
		salary.getValeurRubriquePartage().setAmount(0);
		salary.getValeurRubriquePartage().setRates(0);
		salary.getValeurRubriquePartage().setBase(0);
		// PA_ALGO.Pb_Calcul := FALSE;
		salary.getParameter().setPbWithCalulation(false);
		// 0
		// ---------------------------------------------------------------------
		// -- Lecture du parametrage
		// ---------------------------------------------------------------------
		// -- Constitution de la cle
		// A62_algo := LTRIM(TO_CHAR(PA_CALCUL.t_rub.algo, '09'));
		// Cle_acces := 'A' || LTRIM(TO_CHAR(PA_CALCUL.t_rub.algo, '09')) ||
		// '-' || PA_CALCUL.t_rub.crub || '-1';
		String algo = ClsStringUtil.formatNumber(rubrique.getRubrique().getAlgo(), "00");
		String accesKey = "A" + algo + "-" + rubrique.getRubrique().getComp_id().getCrub() + "-1";
		//
		// -- Lecture de l'enregistrement
		// BEGIN
		// SELECT 'X'
		// INTO A62_char1
		// FROM pafnom
		// WHERE cdos = PA_CALCUL.wpdos.cdos
		// AND ctab = PA_CALCUL.t_rub.tabl
		// AND cacc = Cle_acces
		// AND nume = 1;
		// EXCEPTION
		// WHEN NO_DATA_FOUND THEN
		// PA_CALCUL.err_msg := 'ALGO ' || PA_CALCUL.t_rub.algo ||
		// ', Sal ' || PA_CALCUL.wsal01.nmat ||
		// ', Rbq '|| PA_CALCUL.t_rub.crub ||
		// ': Parametre ' || Cle_acces ||
		// ' Mq en T' || PA_CALCUL.t_rub.tabl;
		// PA_ALGO.Pb_Calcul := TRUE;
		// RETURN FALSE;
		// WHEN OTHERS THEN
		// PA_CALCUL.err_msg := 'ALGO ' || PA_CALCUL.t_rub.algo ||
		// ', Sal ' || PA_CALCUL.wsal01.nmat ||
		// ', Rbq '|| PA_CALCUL.t_rub.crub ||
		// ': Clé ' || Cle_acces ||
		// ', Tb' || PA_CALCUL.t_rub.tabl ||
		// ': Pb lecture.';
		// PA_ALGO.Pb_Calcul := TRUE;
		// RETURN FALSE;
		// END;
		String error = "";
		String crub = rubrique.getRubrique().getComp_id().getCrub();
		String cdos = rubrique.getRubrique().getComp_id().getCdos();
		int tabl = Integer.valueOf(rubrique.getRubrique().getTabl());
		Object objNomenc = salary.getService().findAnyColumnFromNomenclature(cdos, null, String.valueOf(tabl), accesKey, "1");

		if (objNomenc == null)
		{
			// logger
			error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + salary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + crub + ": Clé " + accesKey + ", Tb "
					+ rubrique.getRubrique().getTabl() + ": Pb lecture.";
			salary.getParameter().setError(error);
			salary.getParameter().setPbWithCalulation(true);
			return false;
		}
		//
		// BEGIN
		// SELECT MAX(nvl(DECODE(nume,2,vall),' ')),
		// MAX(nvl(DECODE(nume,3,vall),' ')),
		// MAX(nvl(DECODE(nume,4,vall),' ')),
		// MAX(nvl(DECODE(nume,5,vall),' ')),
		// SUM(nvl(DECODE(nume,3,valm),0)),
		// SUM(nvl(DECODE(nume,4,valm),0)),
		// SUM(nvl(DECODE(nume,5,valm),0)),
		// SUM(nvl(DECODE(nume,6,valm),0))
		// INTO wfnom.lib2, wfnom.lib3, wfnom.lib4, wfnom.lib5,
		// wfnom.mnt3, wfnom.mnt4, wfnom.mnt5, wfnom.mnt6
		// FROM pafnom
		// WHERE cdos = PA_CALCUL.wpdos.cdos
		// AND ctab = PA_CALCUL.t_rub.tabl
		// AND cacc = Cle_acces
		// AND nume IN (2,3,4,5,6);
		// EXCEPTION
		// WHEN NO_DATA_FOUND THEN
		// PA_CALCUL.err_msg := 'ALGO ' || PA_CALCUL.t_rub.algo ||
		// ', Sal ' || PA_CALCUL.wsal01.nmat ||
		// ', Rbq '|| PA_CALCUL.t_rub.crub ||
		// ': Parametre ' || Cle_acces ||
		// ' Mq en T' || PA_CALCUL.t_rub.tabl;
		// PA_ALGO.Pb_Calcul := TRUE;
		// RETURN FALSE;
		// WHEN OTHERS THEN
		// PA_CALCUL.err_msg := 'ALGO ' || PA_CALCUL.t_rub.algo ||
		// ', Sal ' || PA_CALCUL.wsal01.nmat ||
		// ', Rbq '|| PA_CALCUL.t_rub.crub ||
		// ': Clé ' || Cle_acces ||
		// ', Tb' || PA_CALCUL.t_rub.tabl ||
		// ': Pb lecture.';
		// PA_ALGO.Pb_Calcul := TRUE;
		// RETURN FALSE;
		// END;
		String complexQuery = "select max(case when nume = 2 then vall else ' ' end)";
		complexQuery += ", max(case when nume = 3 then vall else ' ' end)";
		complexQuery += ", max(case when nume = 4 then vall else ' ' end)";
		complexQuery += ", max(case when nume = 5 then vall else ' ' end)";
		complexQuery += ", sum(case when nume = 3 then valm else 0 end)";
		complexQuery += ", sum(case when nume = 4 then valm else 0 end)";
		complexQuery += ", sum(case when nume = 5 then valm else 0 end)";
		complexQuery += ", sum(case when nume = 6 then valm else 0 end)";
		complexQuery += " from ParamData" + " where identreprise='" + cdos + "'" + " and ctab = " + tabl + " and cacc = '" + accesKey + "'" + " and nume in (2, 3, 4, 5, 6)";
		List listOfMaxsum = salary.getService().find(complexQuery);
		if (listOfMaxsum == null)
		{
			// logger
			error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + salary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + crub + ": Clé " + accesKey + ", Tb "
					+ rubrique.getRubrique().getTabl() + ": Pb lecture.";
			salary.getParameter().setError(error);
			salary.getParameter().setPbWithCalulation(true);
			return false;
		}
		Object[] maxsum = (Object[]) listOfMaxsum.get(0);
		//
		// ---------------------------------------------------------------------
		// -- Controle du parametrage
		// ---------------------------------------------------------------------
		// -- Sexe ?
		// IF wfnom.lib2 NOT IN ('M', 'F', ' ')
		// THEN
		// PA_CALCUL.err_msg := 'A ' || PA_CALCUL.t_rub.algo ||
		// ', S ' || PA_CALCUL.wsal01.nmat ||
		// ', R '|| PA_CALCUL.t_rub.crub ||
		// ': Clé ' || Cle_acces ||
		// ', T' || PA_CALCUL.t_rub.tabl ||
		// ': Lib2 incorrect.';
		// PA_ALGO.Pb_Calcul := TRUE;
		// RETURN FALSE;
		// ELSE
		// A62_sexe := wfnom.lib2;
		// END IF;
		String sexe = "";
		if (!("M".equals(maxsum[0]) || "F".equals(maxsum[0]) || " ".equals(maxsum[0])))
		{
			// if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "..................................ERROR on SEX!");
			error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + salary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + crub + ": Clé " + accesKey + ", Tb "
					+ rubrique.getRubrique().getTabl() + ": Lib2 incorrect.";
			salary.getParameter().setError(error);
			salary.getParameter().setPbWithCalulation(true);
			return false;
		}
		if (!ClsObjectUtil.isNull(maxsum[0]))
			sexe = (String) maxsum[0];
		//
		// -- Pays de naissance ?
		// IF NOT PA_PAIE.NouB(wfnom.lib3)
		// THEN
		// BEGIN
		// SELECT 'X' INTO A62_char1
		// FROM pafnom
		// WHERE cdos = PA_CALCUL.wpdos.cdos
		// AND ctab = 74
		// AND cacc = wfnom.lib3
		// AND nume = 1;
		// EXCEPTION
		// WHEN NO_DATA_FOUND THEN
		// PA_CALCUL.err_msg := 'A ' || PA_CALCUL.t_rub.algo ||
		// ', S ' || PA_CALCUL.wsal01.nmat ||
		// ', R '|| PA_CALCUL.t_rub.crub ||
		// ': Clé ' || Cle_acces ||
		// ', T' || PA_CALCUL.t_rub.tabl ||
		// ': Lib3 inconnu en T74.';
		// PA_ALGO.Pb_Calcul := TRUE;
		// RETURN FALSE;
		// WHEN OTHERS THEN
		// PA_CALCUL.err_msg := 'A ' || PA_CALCUL.t_rub.algo ||
		// ', S ' || PA_CALCUL.wsal01.nmat ||
		// ', R '|| PA_CALCUL.t_rub.crub ||
		// ': Clé ' || Cle_acces ||
		// ', T' || PA_CALCUL.t_rub.tabl ||
		// ': Pb lect. sur T74.';
		// PA_ALGO.Pb_Calcul := TRUE;
		// RETURN FALSE;
		// END;
		// ELSE
		// A62_pnai := substr(wfnom.lib3, 1, 3);
		// END IF;
		String lib3 = "";
		String pnai = "";
		if (!ClsObjectUtil.isNull(maxsum[1]))
		{
			lib3 = (String) maxsum[1];
			if (!"".equals(lib3.trim()))
			{
				objNomenc = salary.getService().findAnyColumnFromNomenclature(cdos, "", "74", lib3, "1");

				if (objNomenc == null)
				{
					if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "..................................ERROR on lib3!";
					// logger
					error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + salary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + crub + ": Clé " + accesKey + ", Tb "
							+ rubrique.getRubrique().getTabl() + ": Lib3 inconnu en T74.";
					salary.getParameter().setError(error);
					salary.getParameter().setPbWithCalulation(true);
					return false;
				}
				pnai = lib3.length() > 1 ? lib3.substring(0, 2) : lib3;
			}
			else
			{
				// logger
				error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + salary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + crub + ": Clé " + accesKey + ", Tb "
						+ rubrique.getRubrique().getTabl() + ": Pb lect. sur T74.";
				salary.getParameter().setPbWithCalulation(true);
				salary.getParameter().setError(error);
				return false;
			}
		}
		//
		// -- Scolarise ?
		// IF wfnom.lib4 NOT IN ('O', 'N', ' ')
		// THEN
		// PA_CALCUL.err_msg := 'A ' || PA_CALCUL.t_rub.algo ||
		// ', S ' || PA_CALCUL.wsal01.nmat ||
		// ', R '|| PA_CALCUL.t_rub.crub ||
		// ': Clé ' || Cle_acces ||
		// ', T' || PA_CALCUL.t_rub.tabl ||
		// ': Lib4 incorrect.';
		// PA_ALGO.Pb_Calcul := TRUE;
		// RETURN FALSE;
		// ELSE
		// A62_scol := wfnom.lib4;
		// END IF;
		if (!("O".equals(maxsum[2]) || "N".equals(maxsum[2]) || " ".equals(maxsum[2])))
		{
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "..................................ERROR on  lib4!";
			// logger
			error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + salary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + crub + ": Clé " + accesKey + ", Tb "
					+ rubrique.getRubrique().getTabl() + ": Lib4 incorrect.";
			salary.getParameter().setError(error);
			salary.getParameter().setPbWithCalulation(true);
			return false;
		}
		String scol = (String) maxsum[2];
		//
		// -- A charge ?
		// IF wfnom.lib5 NOT IN ('O', 'N', ' ')
		// THEN
		// PA_CALCUL.err_msg := 'A ' || PA_CALCUL.t_rub.algo ||
		// ', S ' || PA_CALCUL.wsal01.nmat ||
		// ', R '|| PA_CALCUL.t_rub.crub ||
		// ': Clé ' || Cle_acces ||
		// ', T' || PA_CALCUL.t_rub.tabl ||
		// ': Lib5 incorrect.';
		// PA_ALGO.Pb_Calcul := TRUE;
		// RETURN FALSE;
		// ELSE
		// A62_achg := wfnom.lib5;
		// END IF;
		//
		if (!("O".equals(maxsum[3]) || "N".equals(maxsum[3]) || " ".equals(maxsum[3])))
		{
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "..................................ERROR on  lib5!";
			// logger
			error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + salary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + crub + ": Clé " + accesKey + ", Tb "
					+ rubrique.getRubrique().getTabl() + ": Lib5 incorrect.";
			salary.getParameter().setError(error);
			salary.getParameter().setPbWithCalulation(true);
			return false;
		}
		String achg = (String) maxsum[3];
		// -- Age mini ?
		// IF wfnom.mnt3 < 0 OR
		// wfnom.mnt3 > 99
		// THEN
		// PA_CALCUL.err_msg := 'A ' || PA_CALCUL.t_rub.algo ||
		// ', S ' || PA_CALCUL.wsal01.nmat ||
		// ', R '|| PA_CALCUL.t_rub.crub ||
		// ': Clé ' || Cle_acces ||
		// ', T' || PA_CALCUL.t_rub.tabl ||
		// ': mnt3 incorrect.';
		// PA_ALGO.Pb_Calcul := TRUE;
		// RETURN FALSE;
		// ELSE
		// A62_age1 := wfnom.mnt3;
		// END IF;
		long age1 = 0;
		if (!ClsObjectUtil.isNull(maxsum[4]))
			age1 = (Long) maxsum[4];
		if (age1 < 0 || age1 > 99)
		{
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "..................................ERROR on  mnt2!";
			// logger
			error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + salary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + crub + ": Clé " + accesKey + ", Tb "
					+ rubrique.getRubrique().getTabl() + ": mnt3 incorrect.";
			salary.getParameter().setError(error);
			salary.getParameter().setPbWithCalulation(true);
			return false;
		}
		//
		// -- Age maxi ?
		// IF wfnom.mnt4 < 0 OR
		// wfnom.mnt4 > 99
		// THEN
		// PA_CALCUL.err_msg := 'A ' || PA_CALCUL.t_rub.algo ||
		// ', S ' || PA_CALCUL.wsal01.nmat ||
		// ', R '|| PA_CALCUL.t_rub.crub ||
		// ': Clé ' || Cle_acces ||
		// ', T' || PA_CALCUL.t_rub.tabl ||
		// ': mnt4 incorrect.';
		// PA_ALGO.Pb_Calcul := TRUE;
		// RETURN FALSE;
		// ELSE
		// A62_age2 := wfnom.mnt4;
		// END IF;
		long age2 = 0;
		if (!ClsObjectUtil.isNull(maxsum[5]))
			age2 = (Long) maxsum[5];
		if (age2 < 0 || age2 > 99)
		{
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "..................................ERROR on  mnt4!";
			// logger
			error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + salary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + crub + ": Clé " + accesKey + ", Tb "
					+ rubrique.getRubrique().getTabl() + ": mnt4 incorrect.";
			salary.getParameter().setError(error);
			salary.getParameter().setPbWithCalulation(true);
			return false;
		}
		//
		// IF wfnom.mnt4 < wfnom.mnt3
		// THEN
		// PA_CALCUL.err_msg := 'A ' || PA_CALCUL.t_rub.algo ||
		// ', S ' || PA_CALCUL.wsal01.nmat ||
		// ', R '|| PA_CALCUL.t_rub.crub ||
		// ': Clé ' || Cle_acces ||
		// ', T' || PA_CALCUL.t_rub.tabl ||
		// ': mnt3, mnt4 decroissant.';
		// PA_ALGO.Pb_Calcul := TRUE;
		// RETURN FALSE;
		// END IF;
		if (age2 < age1)
		{
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "..................................ERROR on  age2 < age1!";
			// logger
			error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + salary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + crub + ": Clé " + accesKey + ", Tb "
					+ rubrique.getRubrique().getTabl() + ": mnt3, mnt4 decroissant.";
			salary.getParameter().setError(error);
			salary.getParameter().setPbWithCalulation(true);
			return false;
		}
		//
		// -- Conjoint mini ?
		// IF wfnom.mnt5 < 0 OR
		// wfnom.mnt5 > 99
		// THEN
		// PA_CALCUL.err_msg := 'A ' || PA_CALCUL.t_rub.algo ||
		// ', S ' || PA_CALCUL.wsal01.nmat ||
		// ', R '|| PA_CALCUL.t_rub.crub ||
		// ': Clé ' || Cle_acces ||
		// ', T' || PA_CALCUL.t_rub.tabl ||
		// ': mnt5 incorrect.';
		// PA_ALGO.Pb_Calcul := TRUE;
		// RETURN FALSE;
		// ELSE
		// A62_nucj1 := wfnom.mnt5;
		// END IF;
		long age3 = 0;
		if (!ClsObjectUtil.isNull(maxsum[6]))
			age3 = (Long) maxsum[6];
		if (age3 < 0 || age3 > 99)
		{
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "..................................ERROR on  mnt5!";
			// logger
			error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + salary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + crub + ": Clé " + accesKey + ", Tb "
					+ rubrique.getRubrique().getTabl() + ": mnt5 incorrect.";
			salary.getParameter().setError(error);
			salary.getParameter().setPbWithCalulation(true);
			return false;
		}
		long nucj1 = age3;
		//
		// -- Conjoint maxi ?
		// IF wfnom.mnt6 < 0 OR
		// wfnom.mnt6 > 99
		// THEN
		// PA_CALCUL.err_msg := 'A ' || PA_CALCUL.t_rub.algo ||
		// ', S ' || PA_CALCUL.wsal01.nmat ||
		// ', R '|| PA_CALCUL.t_rub.crub ||
		// ': Clé ' || Cle_acces ||
		// ', T' || PA_CALCUL.t_rub.tabl ||
		// ': mnt6 incorrect.';
		// PA_ALGO.Pb_Calcul := TRUE;
		// RETURN FALSE;
		// ELSE
		// A62_nucj2 := wfnom.mnt6;
		// END IF;
		long age4 = 0;
		if (!ClsObjectUtil.isNull(maxsum[7]))
			age4 = (Long) maxsum[7];
		if (age4 < 0 || age4 > 99)
		{
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "..................................ERROR on  mnt6!";
			// logger
			error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + salary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + crub + ": Clé " + accesKey + ", Tb "
					+ rubrique.getRubrique().getTabl() + ": mnt6 incorrect.";
			salary.getParameter().setError(error);
			salary.getParameter().setPbWithCalulation(true);
			return false;
		}
		long nucj2 = age4;
		//
		// IF wfnom.mnt6 < wfnom.mnt5
		// THEN
		// PA_CALCUL.err_msg := 'A' || PA_CALCUL.t_rub.algo ||
		// ', S' || PA_CALCUL.wsal01.nmat ||
		// ', R'|| PA_CALCUL.t_rub.crub ||
		// ': Clé ' || Cle_acces ||
		// ', T' || PA_CALCUL.t_rub.tabl ||
		// ': mnt5, mnt6 decroissant.';
		// PA_ALGO.Pb_Calcul := TRUE;
		// RETURN FALSE;
		// END IF;
		if (age2 < age1)
		{
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "..................................ERROR on  age2 < age1!";
			// logger
			error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + salary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + crub + ": Clé " + accesKey + ", Tb "
					+ rubrique.getRubrique().getTabl() + ": mnt5, mnt6 decroissant.";
			salary.getParameter().setError(error);
			salary.getParameter().setPbWithCalulation(true);
			return false;
		}
		//
		// IF PA_PAIE.NouB(A62_sexe) THEN A62_sexe := '%'; END IF;
		// IF PA_PAIE.NouB(A62_pnai) THEN A62_pnai := '%'; END IF;
		// IF PA_PAIE.NouB(A62_scol) THEN A62_scol := '%'; END IF;
		// IF PA_PAIE.NouB(A62_achg) THEN A62_achg := '%'; END IF;
		// IF PA_PAIE.NouZ(A62_nucj1) AND PA_PAIE.NouZ(A62_nucj2)
		// THEN
		// A62_nucj1 := 0;
		// A62_nucj2 := 99;
		// END IF;
		if (ClsObjectUtil.isNull(sexe.trim()))
			sexe = "%";
		if (ClsObjectUtil.isNull(pnai.trim()))
			pnai = "%";
		if (ClsObjectUtil.isNull(scol.trim()))
			scol = "%";
		if (ClsObjectUtil.isNull(achg.trim()))
			achg = "%";
		if (nucj1 <= 0 && nucj2 <= 0)
		{
			nucj1 = 0;
			nucj2 = 99;
		}
		//
		// ---------------------------------------------------------------------
		// -- Calcul des dates de naissance
		// ---------------------------------------------------------------------
		// Annee_courante := TO_NUMBER( substr(PA_ALGO.w_aamm, 1, 4) );
		//
		// A62_Annee := Annee_courante - A62_age2 - 1;
		// A62_Mois := TO_NUMBER( substr(PA_ALGO.w_aamm, 5, 2) );
		// A62_Mois := A62_Mois + 1;
		// IF A62_Mois > 12
		// THEN
		// A62_Mois := 1;
		// A62_Annee := A62_Annee + 1;
		// END IF;
		// A62_dtna1 := LTRIM(TO_CHAR( (A62_Annee), '0999')) ||
		// LTRIM(TO_CHAR( (A62_Mois), '09')) ;
		long anneecourante = salary.getParameter().getMyMonthOfPay().getYear();
		long annee = anneecourante - age2 - 1;
		long mois = salary.getParameter().getMyMonthOfPay().getMonth();
		mois++;
		if (mois > 12)
		{
			mois = 1;
			annee++;
		}
		String dtna1 = ClsStringUtil.formatNumber(annee, "0000") + ClsStringUtil.formatNumber(mois, "00");
		//
		// IF A62_age1 = 0
		// THEN
		// A62_dtna2 := PA_ALGO.w_aamm;
		// ELSE
		// A62_Annee := Annee_courante - A62_age1 - 1;
		// A62_Mois := TO_NUMBER( substr(PA_ALGO.w_aamm, 5, 2) );
		// A62_Mois := A62_Mois + 1;
		// IF A62_Mois > 12
		// THEN
		// A62_Mois := 1;
		// A62_Annee := A62_Annee + 1;
		// END IF;
		// A62_dtna2 := LTRIM(TO_CHAR( (A62_Annee), '0999')) ||
		// LTRIM(TO_CHAR( (A62_Mois), '09')) ;
		// END IF;
		String dtna2 = "";
		if (age1 == 0)
		{
			dtna2 = salary.getParameter().getMyMonthOfPay().getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
		}
		else
		{
			annee = anneecourante - age1 - 1;
			mois = salary.getParameter().getMyMonthOfPay().getMonth();
			mois++;
			if (mois > 12)
			{
				mois = 1;
				annee++;
			}
			dtna2 = ClsStringUtil.formatNumber(annee, "0000") + ClsStringUtil.formatNumber(mois, "00");
		}
		//
		// ---------------------------------------------------------------------
		// -- Comptage des enfants
		// ---------------------------------------------------------------------
		// IF A62_age1 = 0 AND A62_age2 = 99
		// THEN
		List nbreenfant = null;
		if (age1 == 0 && age2 == 99)
		{
			// BEGIN
			// -- Pas de test sur la date de naissance
			// SELECT NVL(count(*), 0)
			// INTO Nb_enfants
			// FROM paenfan
			// WHERE cdos = PA_CALCUL.wpdos.cdos
			// AND nmat = PA_CALCUL.wsal01.nmat
			// AND nucj BETWEEN A62_nucj1 AND A62_nucj2
			// AND sexe LIKE A62_sexe
			// AND pnai LIKE A62_pnai
			// AND scol LIKE A62_scol
			// AND achg LIKE A62_achg;
			// EXCEPTION
			// WHEN OTHERS THEN
			// PA_CALCUL.err_msg := 'A' || PA_CALCUL.t_rub.algo ||
			// ', S' || PA_CALCUL.wsal01.nmat ||
			// ', R'|| PA_CALCUL.t_rub.crub ||
			// ': Pb lect. paenfan. N1';
			// PA_ALGO.Pb_Calcul := TRUE;
			// RETURN FALSE;
			// END;
			complexQuery = "select count(*) from Rhtenfantagent" + " where identreprise='" + cdos + "'" + " and nmat = '" + salary.getInfoSalary().getComp_id().getNmat() + "'" + " and nucj between "
					+ nucj1 + " and " + nucj2 + " and sexe like '" + sexe + "'" + " and pnai like '" + pnai + "'" + " and scol like '" + scol + "'" + " and achg like '" + achg + "'";
			nbreenfant = salary.getService().find(complexQuery);
			if (nbreenfant == null)
			{
				if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "..................................ERROR on  enfants : date naissance!";
				// logger
				error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + salary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + crub + ": Clé " + accesKey + ", Tb "
						+ rubrique.getRubrique().getTabl() + ": Pb lect. paenfan. N1";
				salary.getParameter().setError(error);
				salary.getParameter().setPbWithCalulation(true);
				return false;
			}
		}
		else
		{
			// ELSE
			// -- Test sur la date de naissance, elle doit etre renseignee
			// BEGIN
			// SELECT NVL(count(*), 0)
			// INTO Nb_enfants
			// FROM paenfan
			// WHERE cdos = PA_CALCUL.wpdos.cdos
			// AND nmat = PA_CALCUL.wsal01.nmat
			// AND dtna IS NULL;
			// EXCEPTION
			// WHEN OTHERS THEN
			// PA_CALCUL.err_msg := 'A' || PA_CALCUL.t_rub.algo ||
			// ', S' || PA_CALCUL.wsal01.nmat ||
			// ', R'|| PA_CALCUL.t_rub.crub ||
			// ': Pb lect. paenfan. N2';
			// PA_ALGO.Pb_Calcul := TRUE;
			// RETURN FALSE;
			// END;
			complexQuery = "select count(*) from Rhtenfantagent" + " where identreprise='" + cdos + "'" + " and nmat = '" + salary.getInfoSalary().getComp_id().getNmat() + "'" + " and dtna is null";
			nbreenfant = salary.getService().find(complexQuery);
			// IF Nb_enfants > 0
			// THEN
			// PA_CALCUL.err_msg := 'A' || PA_CALCUL.t_rub.algo ||
			// ', S' || PA_CALCUL.wsal01.nmat ||
			// ', R'|| PA_CALCUL.t_rub.crub ||
			// ' enfants : date naissance';
			// PA_ALGO.Pb_Calcul := TRUE;
			// RETURN FALSE;
			// END IF;
			if (nbreenfant != null && ((Long) nbreenfant.get(0)) > 0)
			{
				if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "..................................ERROR on  enfants : date naissance!";
				// logger
				error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + salary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + crub + ": Clé " + accesKey + ", Tb "
						+ rubrique.getRubrique().getTabl() + "enfants : date naissance";
				salary.getParameter().setError(error);
				salary.getParameter().setPbWithCalulation(true);
				return false;
			}
			//
			// BEGIN
			// SELECT NVL(count(*), 0)
			// INTO Nb_enfants
			// FROM paenfan
			// WHERE cdos = PA_CALCUL.wpdos.cdos
			// AND nmat = PA_CALCUL.wsal01.nmat
			// AND nucj BETWEEN A62_nucj1 AND A62_nucj2
			// AND sexe LIKE A62_sexe
			// AND TO_CHAR(dtna, 'YYYYMM') BETWEEN A62_dtna1 AND A62_dtna2
			// AND pnai LIKE A62_pnai
			// AND scol LIKE A62_scol
			// AND achg LIKE A62_achg;
			Date dtna11 = new ClsDate(dtna1, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getFirstDayOfMonth();
			Date dtna22 = new ClsDate(dtna2, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getLastDayOfMonth();

			complexQuery = "select count(*) from Rhtenfantagent" + " where identreprise='" + cdos + "'" + " and nmat = '" + salary.getInfoSalary().getComp_id().getNmat() + "'" + " and nucj between "
					+ nucj1 + " and " + nucj2 + " and sexe like '" + sexe + "'" + " and dtna between '" + new ClsDate(dtna11).getDateS(salary.parameter.appDateFormat) + "' and '"
					+ new ClsDate(dtna22).getDateS(salary.parameter.appDateFormat) + "'" + " and pnai like '" + pnai + "'" + " and scol like '" + scol + "'" + " and achg like '"
					+ achg + "'";
			nbreenfant = salary.getService().find(complexQuery);
			if (nbreenfant == null)
			{
				if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "..................................ERROR on  enfants : date naissance!";
				// logger
				error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + salary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + crub + ": Clé " + accesKey + ", Tb "
						+ rubrique.getRubrique().getTabl() + ": Pb lect. paenfan. N3";
				salary.getParameter().setError(error);
				salary.getParameter().setPbWithCalulation(true);
				return false;
			}
			// EXCEPTION
			// WHEN OTHERS THEN
			// PA_CALCUL.err_msg := 'A' || PA_CALCUL.t_rub.algo ||
			// ', S' || PA_CALCUL.wsal01.nmat ||
			// ', R'|| PA_CALCUL.t_rub.crub ||
			// ': Pb lect. paenfan. N3';
			// PA_ALGO.Pb_Calcul := TRUE;
			// RETURN FALSE;
			// END;
			//
		}
		// END IF;
		//
		// PA_ALGO.w_bas := Nb_enfants;
		// PA_ALGO.w_basp := PA_ALGO.w_bas;
		// PA_ALGO.w_mon := Nb_enfants;
		salary.getValeurRubriquePartage().setBase((Integer) nbreenfant.get(0));
		salary.getValeurRubriquePartage().setBasePlafonnee(salary.getValeurRubriquePartage().getBase());
		salary.getValeurRubriquePartage().setAmount((Integer) nbreenfant.get(0));
		//
		return true;
	}
	
	

	/**
	 * Algo Ecrit le 11/06/2010 é 10h
	 */
	public boolean algo63(ClsRubriqueClone rubrique)
	{
		String error = null;
		salary.getValeurRubriquePartage().setBase(0);
		salary.getValeurRubriquePartage().setBasePlafonnee(0);
		salary.getValeurRubriquePartage().setAmount(0);
		salary.getValeurRubriquePartage().setRates(0);
		//salary.getParameter().setPbWithCalulation(false);
		
		String cleAcces = rubrique.getRubrique().getComp_id().getCrub();
		String cdos = salary.parameter.dossier;
		List<ParamData> lst = salary.service.find("From ParamData where identreprise='"+cdos+"' and ctab = "+rubrique.getRubrique().getTabl()+"  and cacc = '"+cleAcces+"' order by nume");
		if(lst.isEmpty())
		{
			error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + salary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + cleAcces + ": Parametre " + cleAcces + ", Mq en T "
					+ rubrique.getRubrique().getTabl();
			salary.getParameter().setError(error);
			salary.getParameter().setPbWithCalulation(true);
			return false;
		}
		String[] lib = new String[]{StringUtils.EMPTY, StringUtils.EMPTY,StringUtils.EMPTY};
		Integer[] mnt = new Integer[]{0,0,0};
		String[] rub = new String[]{ParameterUtil.formatRubrique,ParameterUtil.formatRubrique,ParameterUtil.formatRubrique};
		ClsRubriqueClone[] clones = new  ClsRubriqueClone[]{null,null,null};
		ParamData fnom = null;
		Integer nume = 0;
		ClsRubriqueClone clone = null;
		for(int i=0; i<lst.size(); i++)
		{
			fnom = lst.get(i);
			nume = fnom.getNume();
			if(nume == i+1)
			{
				if(StringUtils.isNotBlank(fnom.getVall()))
					lib[i] = fnom.getVall();
				if(fnom.getValm() != null)
					mnt[i] = fnom.getValm().intValue();
			}
		}
		
		for(int i=0; i<3; i++)
		{
			if(StringUtil.notIn(lib[i], "B,T,M"))
			{
				error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + salary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + cleAcces + ",:  Clé " + cleAcces + ", T " + rubrique.getRubrique().getTabl()
						+ " Lib " + (i + 1) + " incorrect. ";
				salary.getParameter().setError(error);
				salary.getParameter().setPbWithCalulation(true);
				return false;
			}
			if(mnt[i] == 0)
			{
				error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + salary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + cleAcces + ",:  Clé " + cleAcces + ", T " + rubrique.getRubrique().getTabl()
						+ " Mnt " + (i + 1) + " incorrect. ";
				salary.getParameter().setError(error);
				salary.getParameter().setPbWithCalulation(true);
				return false;
			}
			rub[i] = ClsStringUtil.formatNumber(mnt[i], ParameterUtil.formatRubrique);
			clone = salary.findRubriqueClone(rub[i]);
			if (clone == null)
			{
				error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + salary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + cleAcces + ",:  Clé " + cleAcces + ", T " + rubrique.getRubrique().getTabl()
						+ " Mnt " + (i + 1) + " Rbq inexistante. ";
				salary.getParameter().setError(error);
				salary.getParameter().setPbWithCalulation(true);
				return false;
			}
			else
				clones[i]= clone;
		}
	
		double[] valeurs = new double[]{0,0,0};
		for(int i=0; i<3; i++)
		{
			if(StringUtils.equals(lib[i],"B"))
				valeurs[i] = clones[i].getBase();
			if(StringUtils.equals(lib[i],"T"))
				valeurs[i] = clones[i].getRates();
			if(StringUtils.equals(lib[i],"M"))
				valeurs[i] = clones[i].getAmount();
		}
		salary.getValeurRubriquePartage().setBase(valeurs[0]);
		salary.getValeurRubriquePartage().setBasePlafonnee(valeurs[0]);
		salary.getValeurRubriquePartage().setRates(valeurs[1]);
		salary.getValeurRubriquePartage().setAmount(valeurs[2]);
		
		return true;
	}

	public boolean algo64(ClsRubriqueClone rubrique)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo65(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo65(ClsRubriqueClone rubrique)
	{
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo65";
		
		//if(StringUtils.equals(salary.parameter.getNomClient(), ClsEntreprise.COMILOG))
			//return comilog.algo65(rubrique);
		
		
		// w_tau := 0;
		// w_mon := 0;
		// w_valeur := 0;
		salary.getValeurRubriquePartage().setAmount(0);
		salary.getValeurRubriquePartage().setRates(0);
		salary.getValeurRubriquePartage().setBase(0);
		//
		// char4 := PA_CALCUL.t_rub.crub;
		String char4 = rubrique.getRubrique().getComp_id().getCrub();
		int tabl = Integer.valueOf(rubrique.getRubrique().getTabl());
		String cdos = salary.getInfoSalary().getComp_id().getCdos();
		
		//@AJout Yannick le 18/02/2010 : Nombre de mois é ajouter é la date en lib4
		tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,cdos, tabl, char4, 3, salary.getNlot(), salary.getNbul(), this.salary.getMoisPaieCourant(),
				this.salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.AMOUNT, rubrique.getRubrique().getComp_id().getCrub());
		Integer nbrMoisAAjouter = 0; 
		if(tempNumber != null)
			nbrMoisAAjouter = tempNumber.intValue();
			
		
		
		// wfnom.lib3 := paf_lecfnomL(TO_NUMBER(PA_CALCUL.t_rub.tabl),char4,3,PA_CALCUL.w_aamm,PA_CALCUL.w_nlot,PA_CALCUL.wsd_fcal1.nbul);
		// IF wfnom.lib3 IS NULL THEN
		// PA_CALCUL.err_msg := PA_PAIE.erreurp('ERR-90056',PA_CALCUL.w_clang,PA_CALCUL.t_rub.algo,PA_CALCUL.t_rub.crub,PA_CALCUL.wsal01.nmat,char4,PA_CALCUL.t_rub.tabl);
		// RETURN FALSE;
		// END IF;
		String libelle3 = this.getLabelFromNomenclature(cdos, tabl, char4, 3, salary.getNlot(), salary.getNbul(), this.salary.getMoisPaieCourant(),
				this.salary.getPeriodOfPay(), rubrique.getRubrique().getComp_id().getCrub());
		if (ClsObjectUtil.isNull(libelle3))
		{
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "..................................ERROR on lib3!";
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90056", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat(), char4, rubrique.getRubrique().getTabl()));
			// logger
			return false;
		}
		//
		// wfnom.lib4 := paf_lecfnomL(TO_NUMBER(PA_CALCUL.t_rub.tabl),char4,4,PA_CALCUL.w_aamm,PA_CALCUL.w_nlot,PA_CALCUL.wsd_fcal1.nbul);
		// IF wfnom.lib4 IS NULL THEN
		// PA_CALCUL.err_msg := PA_PAIE.erreurp('ERR-90056',PA_CALCUL.w_clang,PA_CALCUL.t_rub.algo,PA_CALCUL.t_rub.crub,PA_CALCUL.wsal01.nmat,char4,PA_CALCUL.t_rub.tabl);
		// RETURN FALSE;
		// END IF;
		String libelle4 = this.getLabelFromNomenclature(cdos, tabl, char4, 4, salary.getNlot(), salary.getNbul(), this.salary.getMoisPaieCourant(),
				this.salary.getPeriodOfPay(), rubrique.getRubrique().getComp_id().getCrub());
		if (ClsObjectUtil.isNull(libelle4))
		{
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "..................................ERROR on lib4!";
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90056", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat(), char4, rubrique.getRubrique().getTabl()));
			// logger
			return false;
		}
		//
		// wfnom.lib5 := paf_lecfnomL(TO_NUMBER(PA_CALCUL.t_rub.tabl),char4,5,PA_CALCUL.w_aamm,PA_CALCUL.w_nlot,PA_CALCUL.wsd_fcal1.nbul);
		// IF wfnom.lib5 IS NULL THEN
		// PA_CALCUL.err_msg := PA_PAIE.erreurp('ERR-90056',PA_CALCUL.w_clang,PA_CALCUL.t_rub.algo,PA_CALCUL.t_rub.crub,PA_CALCUL.wsal01.nmat,char4,PA_CALCUL.t_rub.tabl);
		// RETURN FALSE;
		// END IF;
		String libelle5 = this.getLabelFromNomenclature(cdos, tabl, char4, 5, salary.getNlot(), salary.getNbul(), this.salary.getMoisPaieCourant(),
				this.salary.getPeriodOfPay(), rubrique.getRubrique().getComp_id().getCrub());
		if (ClsObjectUtil.isNull(libelle5))
		{
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90056", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat(), char4, rubrique.getRubrique().getTabl()));
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "..................................ERROR on lib4!";
			// logger
			return false;
		}
		//
		// w4 := paf_lecfnomM(TO_NUMBER(PA_CALCUL.t_rub.tabl),char4,1,PA_CALCUL.w_aamm,PA_CALCUL.w_nlot,PA_CALCUL.wsd_fcal1.nbul);
		// IF w4 IS NULL THEN
		// PA_CALCUL.err_msg := PA_PAIE.erreurp('ERR-90056',PA_CALCUL.w_clang,PA_CALCUL.t_rub.algo,PA_CALCUL.t_rub.crub,PA_CALCUL.wsal01.nmat,char4,PA_CALCUL.t_rub.tabl);
		// RETURN FALSE;
		// END IF;
		//
		// IF PA_PAIE.NouZ(w4) THEN
		// PA_CALCUL.err_msg := PA_PAIE.erreurp('ERR-90065',PA_CALCUL.w_clang,PA_CALCUL.t_rub.algo,PA_CALCUL.t_rub.crub,PA_CALCUL.wsal01.nmat,char4,PA_CALCUL.t_rub.tabl);
		// RETURN FALSE;
		// END IF;
		tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,cdos, tabl, char4, 1, salary.getNlot(), salary.getNbul(), salary.getMoisPaieCourant(), salary.getPeriodOfPay(),
				ClsEnumeration.EnColumnToRead.AMOUNT, rubrique.getRubrique().getComp_id().getCrub());
		if (tempNumber == null)
		{
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "..................................ERROR on montant4!";
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90056", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat(), char4, rubrique.getRubrique().getTabl()));
			// logger
			return false;
		}
		
		if (tempNumber.intValue() == 0)
		{
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "..................................ERROR on montant4!";
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90065", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat(), char4, rubrique.getRubrique().getTabl()));
			// logger
			return false;
		}
		double montant4 = tempNumber.doubleValue();
		//
		// c4 := LTRIM(TO_CHAR(w4,'0999'));
		//
		String c4 = ClsStringUtil.formatNumber(montant4, ParameterUtil.formatRubrique);
		// wmax := paf_lecfnomM(TO_NUMBER(PA_CALCUL.t_rub.tabl),char4,2,PA_CALCUL.w_aamm,PA_CALCUL.w_nlot,PA_CALCUL.wsd_fcal1.nbul);
		tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,cdos, tabl, char4, 2, salary.getNlot(), salary.getNbul(), this.salary.getMoisPaieCourant(),
				this.salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.AMOUNT, rubrique.getRubrique().getComp_id().getCrub());
		Double wmax = new Double(0); 
		if(tempNumber != null)
			wmax = tempNumber.doubleValue();
		//
		//
		// char1 := SUBSTR(wfnom.lib3,1,1);
		char char1 = libelle3.toCharArray()[0];
		//
		// IF char1 NOT IN ('S','M','C') THEN
		// PA_CALCUL.err_msg := PA_PAIE.erreurp('ERR-10514',PA_CALCUL.w_clang,PA_CALCUL.t_rub.algo,PA_CALCUL.t_rub.crub,PA_CALCUL.wsal01.nmat,char4,PA_CALCUL.t_rub.tabl);
		// RETURN FALSE;
		// END IF;
		if (!('S' == char1 || 'M' == char1 || 'C' == char1 || 'L' == char1))
		{
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "..................................ERROR on char1!";
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-10514", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat(), char4, rubrique.getRubrique().getTabl()));
			// logger
			return false;
		}
		//
		// tdate := RTRIM(LTRIM(SUBSTR(wfnom.lib4,1,5)));
		//
		// IF tdate NOT IN ('DTES','DDCA','DMRR','DTIT','DEPR','DECC','DCHG','DFES','DRTCG','DCHF') THEN
		// PA_CALCUL.err_msg := PA_PAIE.erreurp('ERR-10515',PA_CALCUL.w_clang,PA_CALCUL.t_rub.algo,PA_CALCUL.t_rub.crub,PA_CALCUL.wsal01.nmat,char4,PA_CALCUL.t_rub.tabl);
		// RETURN FALSE;
		// END IF;
		// String tdate = libelle4.substring(0, 4).trim();
		String tdate = libelle4.trim();
		if (!("DTES".equals(tdate) || "DDCA".equals(tdate) || "DMRR".equals(tdate) || "DTIT".equals(tdate) || "DEPR".equals(tdate) || "DECC".equals(tdate) || "DCHG".equals(tdate)
				|| "DFES".equals(tdate) || "DRTCG".equals(tdate) || "DCHF".equals(tdate) || "M-12".equals(tdate)))
		{
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "..................................ERROR on tdate! = " + tdate + " libelle4 =" + libelle4;
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-10515", salary.getParameter().getLangue(), salary.getInfoSalary().getComp_id().getNmat(), rubrique.getRubrique().getComp_id().getCrub(),
							rubrique.getRubrique().getTabl()));
			// logger
			return false;
		}
		//
		// tdate2 := RTRIM(LTRIM(SUBSTR(wfnom.lib5,1,5)));
		// IF tdate2 NOT IN ('PMCF') THEN
		// w_date_base := PA_CALCUL.w_aamm;
		// ELSE
		// IF NOT PA_PAIE.NouB(PA_CALCUL.wsal01.pmcf) THEN
		// w_date_base := PA_CALCUL.wsal01.pmcf;
		// ELSE
		// w_date_base := PA_CALCUL.w_aamm;
		// END IF;
		// END IF;
		String dateReference = "";
		String dateBase = "";
		String tdate2 = libelle5.substring(0, 4).trim();
		if (!"PMCF".equals(tdate2))
		{
			dateBase = salary.getMyMonthOfPay().getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
		}
		else
		{
			dateBase = (!ClsObjectUtil.isNull(salary.getInfoSalary().getPmcf())) ? salary.getInfoSalary().getPmcf() : salary.getMyMonthOfPay().getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
		}
		//
		// IF tdate = 'DTES' THEN
		// w_date_ref := TO_CHAR(PA_CALCUL.wsal01.dtes,'YYYYMM');
		// ELSIF tdate = 'DDCA' THEN
		// w_date_ref := TO_CHAR(PA_CALCUL.wsal01.ddca,'YYYYMM');
		// ELSIF tdate = 'DMRR' THEN
		// w_date_ref := TO_CHAR(PA_CALCUL.wsal01.dmrr,'YYYYMM');
		// ELSIF tdate = 'DTIT' THEN
		// w_date_ref := TO_CHAR(PA_CALCUL.wsal01.dtit,'YYYYMM');
		// ELSIF tdate = 'DEPR' THEN
		// w_date_ref := TO_CHAR(PA_CALCUL.wsal01.depr,'YYYYMM');
		// ELSIF tdate = 'DECC' THEN
		// w_date_ref := TO_CHAR(PA_CALCUL.wsal01.decc,'YYYYMM');
		// ELSIF tdate = 'DCHG' THEN
		// w_date_ref := TO_CHAR(PA_CALCUL.wsal01.dchg,'YYYYMM');
		// ELSIF tdate = 'DFES' THEN
		// w_date_ref := TO_CHAR(PA_CALCUL.wsal01.dfes,'YYYYMM');
		// ELSIF tdate = 'DRTCG' THEN
		// w_date_ref := TO_CHAR(PA_CALCUL.wsal01.drtcg,'YYYYMM');
		// IF PA_PAIE.NouB(w_date_ref) THEN
		// w_date_ref := TO_CHAR(PA_CALCUL.wsal01.dtes,'YYYYMM');
		// END IF;
		// ELSIF tdate = 'DCHF' THEN
		// w_date_ref := TO_CHAR(PA_CALCUL.wsal01.dchf,'YYYYMM');
		// END IF;
		if ("DTES".equals(tdate) && !ClsObjectUtil.isNull(salary.getInfoSalary().getDtes()))
			dateReference = new ClsDate(salary.getInfoSalary().getDtes()).getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
		else if ("DDCA".equals(tdate) && !ClsObjectUtil.isNull(salary.getInfoSalary().getDdca()))
			dateReference = new ClsDate(salary.getInfoSalary().getDdca()).getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
		else if ("DMRR".equals(tdate) && !ClsObjectUtil.isNull(salary.getInfoSalary().getDmrr()))
			dateReference = new ClsDate(salary.getInfoSalary().getDmrr()).getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
		else if ("DTIT".equals(tdate) && !ClsObjectUtil.isNull(salary.getInfoSalary().getDtit()))
			dateReference = new ClsDate(salary.getInfoSalary().getDtit()).getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
		else if ("DEPR".equals(tdate) && !ClsObjectUtil.isNull(salary.getInfoSalary().getDepr()))
			dateReference = new ClsDate(salary.getInfoSalary().getDepr()).getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
		else if ("DECC".equals(tdate) && !ClsObjectUtil.isNull(salary.getInfoSalary().getDecc()))
			dateReference = new ClsDate(salary.getInfoSalary().getDecc()).getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
		else if ("DCHG".equals(tdate) && !ClsObjectUtil.isNull(salary.getInfoSalary().getDchg()))
			dateReference = new ClsDate(salary.getInfoSalary().getDchg()).getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
		else if ("DFES".equals(tdate) && !ClsObjectUtil.isNull(salary.getInfoSalary().getDfes()))
			dateReference = new ClsDate(salary.getInfoSalary().getDfes()).getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
		else if ("DRTCG".equals(tdate))
		{
			if (!ClsObjectUtil.isNull(salary.getInfoSalary().getDrtcg()))
				dateReference = new ClsDate(salary.getInfoSalary().getDrtcg()).getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
			else
				dateReference = new ClsDate(salary.getInfoSalary().getDtes()).getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
		}
		else if ("DCHF".equals(tdate) && !ClsObjectUtil.isNull(salary.getInfoSalary().getDchf()))
			dateReference = new ClsDate(salary.getInfoSalary().getDchf()).getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
		else  if ("M-12".equals(tdate))
			dateReference = new ClsDate(new ClsDate(salary.parameter.periodOfPay,"yyyyMM").addMonth(-12)).getYearAndMonth();
		
		//
		// IF PA_PAIE.NouB(w_date_ref) THEN
		// PA_CALCUL.err_msg := PA_PAIE.erreurp('ERR-10522',PA_CALCUL.w_clang,PA_CALCUL.t_rub.algo,PA_CALCUL.wsal01.nmat,PA_CALCUL.t_rub.crub);
		// RETURN FALSE;
		// END IF;
		//
		// IF PA_PAIE.NouB(w_date_base) THEN
		// PA_CALCUL.err_msg := PA_PAIE.erreurp('ERR-10522',PA_CALCUL.w_clang,PA_CALCUL.t_rub.algo,PA_CALCUL.wsal01.nmat,PA_CALCUL.t_rub.crub);
		// RETURN FALSE;
		// END IF;
		if (ClsObjectUtil.isNull(dateReference) || ClsObjectUtil.isNull(dateBase))
		{
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "..................................ERROR on dateReference et dateBase!";
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-10522", salary.getParameter().getLangue(), salary.getInfoSalary().getComp_id().getNmat(), rubrique.getRubrique().getComp_id().getCrub(),
							rubrique.getRubrique().getTabl()));
			// logger
			return false;
		}
		dateReference =new ClsDate(new ClsDate(dateReference,ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).addMonth(nbrMoisAAjouter)).getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
		
		//
		//
		// IF NOT PA_PAIE.NouZ(wmax) THEN
		// Periode_debut := TO_CHAR(ADD_MONTHS(TO_DATE(w_date_base,'YYYYMM'),wmax * -1),'YYYYMM');
		// IF w_date_ref > Periode_debut THEN
		// Periode_debut := w_date_ref;
		// END IF;
		// ELSE
		// Periode_debut := w_date_ref;
		// END IF;
		ClsDate debut = new ClsDate(dateBase, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
		String periodeDebut = "";
		if (!ClsObjectUtil.isNull(wmax))
		{
			debut.addMonth(wmax.intValue() * -1);
			periodeDebut = debut.getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
			if (dateReference.compareTo(periodeDebut) > 0)
				periodeDebut = dateReference;
		}
		else
		{
			periodeDebut = dateReference;
		}
		//
		//
		// IF PA_CALCUL.retroactif THEN
		// SELECT SUM(mont),count(*)
		// INTO sum_montant,count_montant
		// FROM prcumu
		// WHERE cdos = PA_CALCUL.wpdos.cdos
		// AND nmat = PA_CALCUL.wsal01.nmat
		// AND aamm >= Periode_debut
		// AND aamm < w_date_base
		// AND SUBSTR(aamm,5,2) != '99'
		// AND rubq = c4
		// AND nbul != 0;
		// ELSE
		// SELECT SUM(mont),count(*)
		// INTO sum_montant,count_montant
		// FROM pacumu
		// WHERE cdos = PA_CALCUL.wpdos.cdos
		// AND nmat = PA_CALCUL.wsal01.nmat
		// AND aamm >= Periode_debut
		// AND aamm < w_date_base
		// AND SUBSTR(aamm,5,2) != '99'
		// AND rubq = c4
		// AND nbul != 0;
		// END IF;
		if (char1 == 'L')
		{
			double sum_montant = 0;
			String last_periode = "";
			
			String C_last_cum = " SELECT mont,aamm ";
			C_last_cum += " FROM table";
			C_last_cum += " WHERE cdos =  '" + cdos + "' ";
			C_last_cum += " AND nmat =  '" + salary.getInfoSalary().getComp_id().getNmat() + "' ";
			C_last_cum += " AND aamm >= '" + periodeDebut + "' ";
			C_last_cum += " AND aamm <  '" + dateBase + "' ";
			C_last_cum += " AND aamm not like '%99' ";
			C_last_cum += " AND rubq =  '" + c4 + "' ";
			C_last_cum += " AND nbul != 0 ";
			C_last_cum += " order by aamm desc ";

			C_last_cum = salary.getParameter().isUseRetroactif() ? C_last_cum.replace("table", "Rhtprcumu") : C_last_cum.replace("table", "CumulPaie");
			if ('O' == salary.getParameter().getGenfile())
				outputtext += "\n" + "................query: " + C_last_cum;
			List l = salary.getService().find(C_last_cum);
			if (l != null && l.size() > 0)
			{
				Object[] valeur = (Object[]) l.get(0);
				last_periode = valeur[1] == null ?"":valeur[1].toString();
				sum_montant = valeur[0] == null ? 0 : valeur[0] instanceof BigDecimal ? ((BigDecimal) valeur[0]).doubleValue() : (Double) valeur[0];
			}
		        
			salary.getValeurRubriquePartage().setBase(sum_montant);
			salary.getValeurRubriquePartage().setAmount(sum_montant);
			salary.getValeurRubriquePartage().setRates(0);
		}
		else
		{
			String queryString = "select sum(mont), count(*) from table" + " where identreprise='" + cdos + "'" + " and nmat = '" + salary.getInfoSalary().getComp_id().getNmat() + "'" + " and aamm >= '" + periodeDebut + "' and aamm < '" + dateBase + "'" + " and aamm not like '%99'" + " and rubq = '" + c4 + "'" + " and nbul != 0";
//			queryString = "select sum(mont), count(*) from table" + " where identreprise='" + cdos + "'" + " and nmat = '" + salary.getInfoSalary().getComp_id().getNmat() + "'" + " and aamm >= '" + periodeDebut + "' and aamm < '" + dateBase + "'" + " and substring(aamm, 5, 2) != '99'" + " and rubq = '" + c4 + "'" + " and nbul != 0";
			queryString = salary.getParameter().isUseRetroactif() ? queryString.replace("table", "Rhtprcumu") : queryString.replace("table", "CumulPaie");
			if ('O' == salary.getParameter().getGenfile())
				outputtext += "\n" + "................query: " + queryString;
			List l = salary.getService().find(queryString);
			if (l != null && l.size() > 0)
			{
				Object[] valeur = (Object[]) l.get(0);
				double sum_montant = valeur[0] == null ? 0 : valeur[0] instanceof BigDecimal ? ((BigDecimal) valeur[0]).doubleValue() : (Double) valeur[0];
				int count_montant = valeur[1] == null ? 0 : new BigDecimal(valeur[1].toString()).intValue();
				//
				//
				//
				// IF char1 = 'S' THEN
				// w_bas := sum_montant;
				// w_mon := sum_montant;
				// w_tau := 0;
				// ELSIF char1 = 'M' THEN
				// w_bas := sum_montant;
				// w_mon := sum_montant / count_montant;
				// w_tau := 0;
				// ELSIF char1 = 'C' THEN
				// w_bas := count_montant;
				// w_mon := count_montant;
				// w_tau := 0;
				// END IF;
				if (char1 == 'S' || char1 == 'L')
				{
					salary.getValeurRubriquePartage().setBase(sum_montant);
					salary.getValeurRubriquePartage().setAmount(sum_montant);
					salary.getValeurRubriquePartage().setRates(0);
				}
				else if (char1 == 'M')
				{
					salary.getValeurRubriquePartage().setBase(sum_montant);
					salary.getValeurRubriquePartage().setAmount(sum_montant / count_montant);
					salary.getValeurRubriquePartage().setRates(0);
				}
				else if (char1 == 'C')
				{
					salary.getValeurRubriquePartage().setBase(count_montant);
					salary.getValeurRubriquePartage().setAmount(count_montant);
					salary.getValeurRubriquePartage().setRates(0);
				}
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo66(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo66(ClsRubriqueClone rubrique)
	{
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo66";
		// w_valeur := NULL;
		// w_tau := 0;
		// w_mon := 0;
		salary.getValeurRubriquePartage().setValeur(0);
		salary.getValeurRubriquePartage().setAmount(0);
		salary.getValeurRubriquePartage().setRates(0);
		//
		// -- cle
		// w_valeur := recvalnum(TO_NUMBER(PA_CALCUL.t_rub.cle1));
		// IF Pb_Calcul THEN
		// RETURN FALSE;
		// END IF;
		double valeur = rubrique.recupValeurNumeriqueGSAL(rubrique.getRubrique().getCle1());
		if (salary.getParameter().isPbWithCalulation())
		{
			return false;
		}
		
		if(StringUtils.isNotBlank(rubrique.getRubrique().getCle2()))
		{
			double valeur2 = rubrique.recupValeurNumeriqueGSAL(rubrique.getRubrique().getCle2());
			if (salary.getParameter().isPbWithCalulation())
			{
				return false;
			}
			if(valeur2 != 0) valeur = rubrique.convertToNumber(valeur+""+valeur2);
		}
		
		//
		// IF w_valeur IS NULL THEN
		// w_valeur := 0;
		// END IF;
//		if (valeur <= 0)
//			valeur = 0;
		salary.getValeurRubriquePartage().setValeur(valeur);
		//
		// -- Calcul du montant
		// IF PA_CALCUL.t_rub.toum = 'T' THEN
		if ("T".equals(rubrique.getRubrique().getToum()))
		{
			// IF w_bas = 0 THEN
			// w_bas := w_valeur;
			// w_basp := w_valeur;
			// w_mon := w_valeur;
			// ELSE
			// w_tau := w_valeur;
			// w_mon := w_bas * w_tau / 100;
			// END IF;
			if (salary.getValeurRubriquePartage().getBase() == 0)
			{
				salary.getValeurRubriquePartage().setBase(valeur);
				salary.getValeurRubriquePartage().setBasePlafonnee(valeur);
				salary.getValeurRubriquePartage().setAmount(valeur);
			}
			else
			{
				salary.getValeurRubriquePartage().setRates(valeur);
				salary.getValeurRubriquePartage().setAmount(salary.getValeurRubriquePartage().getBase() * valeur / 100);
			}
		}
		// ELSIF PA_CALCUL.t_rub.toum = 'D' THEN
		else if ("D".equals(rubrique.getRubrique().getToum()))
		{
			// w_tau := w_valeur;
			// IF w_tau != 0 THEN
			// w_mon := w_bas / w_tau;
			// ELSE
			// w_mon := 0;
			// END IF;
			salary.getValeurRubriquePartage().setRates(valeur);
			if (salary.getValeurRubriquePartage().getRates() != 0)
			{
				salary.getValeurRubriquePartage().setAmount(salary.getValeurRubriquePartage().getBase() / valeur);
			}
			else
			{
				salary.getValeurRubriquePartage().setAmount(0);
			}
		}
		// ELSIF PA_CALCUL.t_rub.toum = 'M' THEN
		else if ("M".equals(rubrique.getRubrique().getToum()))
		{
			// IF w_bas = 0 THEN
			// w_bas := w_valeur;
			// w_basp := w_valeur;
			// w_mon := w_valeur;
			// w_tau := 0;
			// ELSE --- multiplication
			// w_tau := w_valeur;
			// w_basp := w_bas;
			// w_mon := w_bas * w_tau;
			// END IF;
			if (salary.getValeurRubriquePartage().getBase() == 0)
			{
				salary.getValeurRubriquePartage().setBase(valeur);
				salary.getValeurRubriquePartage().setBasePlafonnee(valeur);
				salary.getValeurRubriquePartage().setAmount(valeur);
				salary.getValeurRubriquePartage().setRates(0);
			}
			else
			{// --- multiplication
				salary.getValeurRubriquePartage().setRates(valeur);
				salary.getValeurRubriquePartage().setBasePlafonnee(salary.getValeurRubriquePartage().getBase());
				salary.getValeurRubriquePartage().setAmount(salary.getValeurRubriquePartage().getBase() * salary.getValeurRubriquePartage().getRates());
			}
		}
		// ELSE
		// PA_CALCUL.err_msg := PA_PAIE.erreurp('ERR-90058',PA_CALCUL.w_clang,PA_CALCUL.t_rub.algo,PA_CALCUL.wsal01.nmat,PA_CALCUL.t_rub.crub);
		// RETURN FALSE;
		// END IF;
		else
		{
			// logger
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90058", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), salary.getInfoSalary().getComp_id().getNmat(),
							rubrique.getRubrique().getComp_id().getCrub()));
			return false;
		}
		return true;
	}

	public boolean algo67(ClsRubriqueClone rubrique)
	{
		// TODO Auto-generated method stub
		return false;
	}

	public boolean algo68(ClsRubriqueClone rubrique)
	{
//		if (StringUtils.equals(salary.getParameter().nomClient, ClsEntreprise.SOBRAGA))
//		{
//			salary.getValeurRubriquePartage().setRates(NumberUtils.bdnvl(salary.infoSalary.getNbjsa(),0).doubleValue());
//			salary.getValeurRubriquePartage().setAmount(salary.getValeurRubriquePartage().getBase() * salary.getValeurRubriquePartage().getRates() / 24);
//			return true;
//		}
//		nbj_cstc  NUMBER(5,2);
		double nbj_cstc = 0;
//		nbj_reste NUMBER(5,2);
		double nbj_reste = 0;

//		BEGIN
//
//		   nbj_cstc := 0;
//
//		   IF PA_CALCUL.wnbjc = 0  AND PA_CALCUL.wnbjcpnp = 0 THEN
//		      PA_ALGO.w_bas  := 0;
//		      PA_ALGO.w_basp := 0;
//		      PA_ALGO.w_tau  := 0;
//		      PA_ALGO.w_mon  := 0;
//		      RETURN TRUE;
//		   END IF;
		if(salary.workTime.getNbreJourConges() == 0 && salary.workTime.getNbreJourCongesNonPris() == 0)
		{
			salary.getValeurRubriquePartage().setBase(0);
			salary.getValeurRubriquePartage().setBasePlafonnee(0);
			salary.getValeurRubriquePartage().setRates(0);
			salary.getValeurRubriquePartage().setAmount(0);
			return true;
		}
//
//		-- Si Solde de Tout Comptes
//		   IF PA_CALCUL.stc THEN
		if(salary.getParameter().isStc())
		{
//		      IF PA_CALCUL.wsal01.japa IS NULL THEN
//		         PA_CALCUL.wsal01.japa := 0;
//		      END IF;
			if(salary.getInfoSalary().getJapa() == null)
				salary.getInfoSalary().setJapa(new BigDecimal(0));
//
//		      IF PA_CALCUL.wsal01.japec IS NULL THEN
//		         PA_CALCUL.wsal01.japec := 0;
//		      END IF;
			if(salary.getInfoSalary().getJapec() == null)
				salary.getInfoSalary().setJapec(new BigDecimal(0));
//
//		      IF PA_CALCUL.wsal01.japa = 0 AND PA_CALCUL.wsal01.japec = 0 THEN
//			   nbj_cstc := PA_CALCUL.wnbjc;
//		         IF nbj_cstc = 0 THEN
//		            PA_ALGO.w_bas  := 0;
//		            PA_ALGO.w_basp := 0;
//		            PA_ALGO.w_tau  := 0;
//		            PA_ALGO.w_mon  := 0;
//		            RETURN TRUE;
//		         END IF;
//		         IF PA_CALCUL.wmntcp != 0 THEN
//		            PA_ALGO.w_mon := PA_CALCUL.wmntcp;
//		            RETURN TRUE;
//		         END IF;
//		         PA_ALGO.w_tau := nbj_cstc;
//		         PA_ALGO.w_mon := (PA_ALGO.w_bas * PA_ALGO.w_tau) / 24;
//		--         PA_ALGO.w_mon := (PA_ALGO.w_bas * PA_ALGO.w_tau) / 30;
//		         RETURN TRUE;
//		      END IF;
			if(salary.getInfoSalary().getJapa().compareTo(new BigDecimal(0)) == 0 && salary.getInfoSalary().getJapec().compareTo(new BigDecimal(0)) == 0)
			{
				nbj_cstc = salary.workTime.getNbreJourConges();
				if(nbj_cstc == 0)
				{
					salary.getValeurRubriquePartage().setBase(0);
					salary.getValeurRubriquePartage().setBasePlafonnee(0);
					salary.getValeurRubriquePartage().setRates(0);
					salary.getValeurRubriquePartage().setAmount(0);
					return true;
				}
				if(salary.workTime.getMontantCongePonctuel() != 0)
				{
					salary.getValeurRubriquePartage().setAmount(salary.workTime.getMontantCongePonctuel());
					return true;
				}
				salary.getValeurRubriquePartage().setRates(nbj_cstc);
				salary.getValeurRubriquePartage().setAmount(salary.getValeurRubriquePartage().getBase() * nbj_cstc / 24);
				return true;
			}
//
//		      IF PA_CALCUL.wnbjc > PA_CALCUL.wsal01.japa THEN
//		         nbj_reste := PA_CALCUL.wnbjc - PA_CALCUL.wsal01.japa;
//			   PA_CALCUL.wsal01.japa := 0;
//			   PA_CALCUL.wsal01.japec := PA_CALCUL.wsal01.japec - nbj_reste;
//			ELSE
//			   PA_CALCUL.wsal01.japa := PA_CALCUL.wsal01.japa - PA_CALCUL.wnbjc;
//			END IF;
			if(salary.workTime.getNbreJourConges() > salary.getInfoSalary().getJapa().doubleValue())
			{
				nbj_reste = salary.workTime.getNbreJourConges() - salary.getInfoSalary().getJapa().doubleValue();
				salary.getInfoSalary().setJapa(new BigDecimal(0));
				salary.getInfoSalary().setJapec(salary.getInfoSalary().getJapec().subtract(new BigDecimal(nbj_reste)));
			}
			else
				salary.getInfoSalary().setJapa(salary.getInfoSalary().getJapa().subtract(new BigDecimal( salary.workTime.getNbreJourConges())));
//
//		      nbj_cstc := nbj_cstc + PA_CALCUL.wsal01.japa + PA_CALCUL.wsal01.japec;
//		      nbj_cstc := nbj_cstc + PA_CALCUL.wnbjc;
//		--      nbj_cstc := nbj_cstc + PA_CALCUL.wnbjca;
			nbj_cstc += salary.getInfoSalary().getJapa().doubleValue() + salary.getInfoSalary().getJapec().doubleValue();
			nbj_cstc += salary.workTime.getNbreJourConges();
//
//		      IF nbj_cstc = 0 THEN
//		         PA_ALGO.w_bas  := 0;
//		         PA_ALGO.w_basp := 0;
//		         PA_ALGO.w_tau  := 0;
//		         PA_ALGO.w_mon  := 0;
//		         RETURN TRUE;
//		      END IF;
			if(nbj_cstc == 0)
			{
				salary.getValeurRubriquePartage().setBase(0);
				salary.getValeurRubriquePartage().setBasePlafonnee(0);
				salary.getValeurRubriquePartage().setRates(0);
				salary.getValeurRubriquePartage().setAmount(0);
				return true;
			}
//
//		      IF PA_CALCUL.wmntcp != 0 THEN
//		         PA_ALGO.w_mon := PA_CALCUL.wmntcp;
//		         RETURN TRUE;
//		      END IF;
			if(salary.workTime.getNbreJourConges() != 0)
			{
				salary.getValeurRubriquePartage().setAmount(salary.workTime.getMontantCongePonctuel());
				return true;
			}
//
//		      PA_ALGO.w_tau := nbj_cstc;
//		      PA_ALGO.w_mon := (PA_ALGO.w_bas * PA_ALGO.w_tau) / 24;
//		--      PA_ALGO.w_mon := (PA_ALGO.w_bas * PA_ALGO.w_tau) / 30;
//		      RETURN TRUE;
//		   END IF;
			salary.getValeurRubriquePartage().setRates(nbj_cstc);
			salary.getValeurRubriquePartage().setAmount(salary.getValeurRubriquePartage().getBase() * nbj_cstc / 24);
			return true;
		}
//
//		-- Ce n'est pas un Solde de Tout Compte	   
//		   -- ----- Pas de calcul si pas de fictif et WMDP != du premier mois de conges
//		   IF PA_CALCUL.w_fictif = 'N' AND PA_CALCUL.wsal01.pmcf != PA_ALGO.w_aamm THEN
//		      PA_ALGO.w_bas  := 0;
//		      PA_ALGO.w_basp := 0;
//		      PA_ALGO.w_tau  := 0;
//		      PA_ALGO.w_mon  := 0;
//		      RETURN TRUE;
//		   END IF;
		if(StringUtils.equals(salary.getParameter().fictiveCalculus,"N") && !salary.getInfoSalary().getPmcf().equalsIgnoreCase(salary.getMonthOfPay()))
		{
			salary.getValeurRubriquePartage().setBase(0);
			salary.getValeurRubriquePartage().setBasePlafonnee(0);
			salary.getValeurRubriquePartage().setRates(0);
			salary.getValeurRubriquePartage().setAmount(0);
			return true;
		}
//
//		   -- Pas de calcul si pas de conges
//		   IF PA_CALCUL.wnbjc = 0  AND PA_CALCUL.wnbjc_ms = 0 THEN
//		      PA_ALGO.w_bas  := 0;
//		      PA_ALGO.w_basp := 0;
//		      PA_ALGO.w_tau  := 0;
//		      PA_ALGO.w_mon  := 0;
//		      RETURN TRUE;
//		   END IF;
		if(salary.workTime.getNbreJourConges() == 0 &&  salary.workTime.getNbreJourCongesAnnuelMoisSuiv() == 0)
		{
			salary.getValeurRubriquePartage().setBase(0);
			salary.getValeurRubriquePartage().setBasePlafonnee(0);
			salary.getValeurRubriquePartage().setRates(0);
			salary.getValeurRubriquePartage().setAmount(0);
			return true;
		}
//
//		   -- Pas de calcul si montant du conges deja calcule
//		   IF PA_CALCUL.wmntcp != 0 THEN
//		      PA_ALGO.w_mon := PA_CALCUL.wmntcp;
//		      RETURN TRUE;
//		   END IF;
		if(salary.workTime.getMontantCongePonctuel() != 0)
		{
			salary.getValeurRubriquePartage().setAmount(salary.workTime.getMontantCongePonctuel());
			return true;
		}
//		      
//		   -- Ajout du nb de jours payes non pris pour prise en compte dans calcul congés
//		   PA_ALGO.w_tau := PA_CALCUL.wnbjc + PA_CALCUL.wnbjcpnp;
//		--   PA_ALGO.w_tau := PA_CALCUL.wnbjca + PA_CALCUL.wnbjcpnp;
//		   PA_ALGO.w_mon := (PA_ALGO.w_bas * PA_ALGO.w_tau) / 24;
//		--   PA_ALGO.w_mon := (PA_ALGO.w_bas * PA_ALGO.w_tau) / 30;
//		   PA_ALGO.w_mon := PA_ALGO.w_mon - PA_CALCUL.wsal01.mtcf;
//
//		   PA_CALCUL.tot_cgs := (PA_ALGO.w_bas * PA_CALCUL.wsal01.nbjcf) / 24;
//		--   PA_CALCUL.tot_cgs := (PA_ALGO.w_bas * PA_CALCUL.wsal01.nbjaf) / 30;
		salary.getValeurRubriquePartage().setRates(salary.workTime.getNbreJourConges() + salary.workTime.getNbreJourCongesNonPris());
		salary.getValeurRubriquePartage().setAmount(salary.getValeurRubriquePartage().getBase() * salary.getValeurRubriquePartage().getRates() / 24);
		if(salary.getInfoSalary().getMtcf() == null) salary.getInfoSalary().setMtcf(new BigDecimal(0));
		salary.getValeurRubriquePartage().setAmount(salary.getValeurRubriquePartage().getAmount() - salary.getInfoSalary().getMtcf().doubleValue());
		
		if(salary.getInfoSalary().getNbjcf() == null) salary.getInfoSalary().setNbjcf(new BigDecimal(0));
		salary.setTot_cgs((salary.getValeurRubriquePartage().getBase() * salary.getInfoSalary().getNbjcf().doubleValue()) / 24);
		
//
//		   RETURN TRUE;
		// TODO Auto-generated method stub
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo78(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo69(ClsRubriqueClone rubrique)
	{
//		if (StringUtils.equals(salary.getParameter().nomClient, ClsEntreprise.SOBRAGA))
//		{
//			salary.getValeurRubriquePartage().setRates(NumberUtils.bdnvl(salary.infoSalary.getNbjse(),0).doubleValue());
//			salary.getValeurRubriquePartage().setAmount(salary.getValeurRubriquePartage().getBase() * salary.getValeurRubriquePartage().getRates() / 24);
//			return true;
//		}
		
//		wtotretenue NUMBER(15,3);
		double totretenue = 0;
//		wtotcalc    NUMBER(15,3);
		double totcalc = 0;
//		wfonction   VARCHAR2(1);
		char fonction;
//		wcalc_acpte VARCHAR2(1);
		char calc_acpte;
//		wpretint    paprets%ROWTYPE;
//		wpretexe    paprent%ROWTYPE;
//		wpretexl    paprlig%ROWTYPE;
//
//		CURSOR curs_pretint IS        
//			     SELECT *
//		           FROM paprets
//		           WHERE cdos = PA_CALCUL.wpdos.cdos 
//		             AND nmat = PA_CALCUL.wsal01.nmat
//		           ORDER BY lg, crub;
		String curs_pretint = "From PretInterne where identreprise='"+salary.getParameter().getDossier()+"' and nmat='"+salary.getInfoSalary().getComp_id().getNmat()+"' order by lg, crub";
//
//		CURSOR curs_pretexe IS        
//			     SELECT *
//		           FROM paprent
//		           WHERE cdos = PA_CALCUL.wpdos.cdos 
//		             AND nmat = PA_CALCUL.wsal01.nmat
//		           ORDER BY nprt, crub;
		String curs_pretexe = "From PretExterneEntete where identreprise='"+salary.getParameter().getDossier()+"' and nmat='"+salary.getInfoSalary().getComp_id().getNmat()+"' order by nprt ,crub";
//
//		CURSOR curs_pretexl IS        
//		           SELECT *
//		           FROM paprlig
//		           WHERE cdos = wpretexe.cdos 
//		             AND nprt = wpretexe.nprt
//		             AND TO_CHAR(perb,'YYYYMM') = PA_ALGO.w_aamm;
		String curs_pretexl = "From PretExterneDetail where identreprise='"+salary.getParameter().getDossier()+"' and nprt= :nprt order by nprt";
//		perb = row.getComp_id().getPerb();
//		if (new ClsDate(perb, this.salary.getParameter().getAppDateFormat()).getYearAndMonth().equals(salary.getPeriodOfPay()))
		
//
//		BEGIN
//
//		   -- Initialisations
//		   wfonction := 'N';
		fonction = 'N';
//		   wcalc_acpte := 'N';
		calc_acpte = 'N';
//		   PA_ALGO.w_tau    := 0;
		 salary.getValeurRubriquePartage().setRates(0);
//		   PA_ALGO.w_mon    := 0;
		 salary.getValeurRubriquePartage().setAmount(0);
//
//		   IF PA_CALCUL.stc THEN
//		      RETURN TRUE;
//		   END IF;
		 
		 if(salary.getParameter().isStc())
			 return true;
//
//		   IF PA_CALCUL.wnbjc_ms != 0 THEN
//		      PA_ALGO.w_bas  := 0;
//		      PA_ALGO.w_basp := 0;
//		      PA_ALGO.w_tau  := 0;
//		      PA_ALGO.w_mon  := 0;
//		      RETURN TRUE;
//		   END IF;
		if(salary.workTime.getNbreJourCongesAnnuelMoisSuiv() != 0)
		 {
				salary.getValeurRubriquePartage().setBase(0);
				salary.getValeurRubriquePartage().setBasePlafonnee(0);
				salary.getValeurRubriquePartage().setRates(0);
				salary.getValeurRubriquePartage().setAmount(0);
				return true;
			}
//
//		   -- Récupération code médecin : Si médecin --> pas de calcul
//		   BEGIN
//		      SELECT vall INTO wfnom.lib2 FROM pafnom
//		       WHERE cdos = PA_CALCUL.wpdos.cdos
//		         AND ctab = 07
//		         AND cacc = PA_CALCUL.wsal01.fonc
//		         AND nume = 3;
//		   EXCEPTION
//		      WHEN NO_DATA_FOUND THEN null;
//		   END;
		String libelle = this.getLabelFromNomenclature(salary.getInfoSalary().getComp_id().getCdos(),7, salary.getInfoSalary().getFonc(), 3, salary.getNlot(), salary.getNbul(), this.salary.getMoisPaieCourant(),
				this.salary.getPeriodOfPay(), rubrique.getRubrique().getComp_id().getCrub());
//
//		   IF wfnom.lib2 IS NULL THEN
//		      wfnom.lib2 := 'N';
//		   END IF;
		if(StringUtils.isBlank(libelle))
			libelle="N";
//
//		   wfonction := SUBSTR(wfnom.lib2,1,1);
		fonction = libelle.charAt(0);
//		   IF wfonction != 'O' and wfonction != 'o' THEN
//		      wfonction := 'N';
//		   ELSE
//		      wfonction := 'O';
//		   END IF;
		if(fonction != 'O' && fonction !='o')
			fonction = 'N';
		else
			fonction= 'O';
//
//		   IF wfonction = 'O' THEN
//		      PA_ALGO.w_bas  := 0;
//		      PA_ALGO.w_basp := 0;
//		      PA_ALGO.w_tau  := 0;
//		      PA_ALGO.w_mon  := 0;
//		      RETURN TRUE;
//		   END IF;
		if(fonction == 'O')
		 {
				salary.getValeurRubriquePartage().setBase(0);
				salary.getValeurRubriquePartage().setBasePlafonnee(0);
				salary.getValeurRubriquePartage().setRates(0);
				salary.getValeurRubriquePartage().setAmount(0);
				return true;
			}
//
//		   -- Récupération code acompte : Si pas acompte --> pas de calcul
//		   BEGIN
//		      SELECT vall INTO wfnom.lib2 FROM pafnom
//		       WHERE cdos = PA_CALCUL.wpdos.cdos
//		         AND ctab = 03
//		         AND cacc = PA_CALCUL.wsal01.niv3
//		         AND nume = 2;
//		   EXCEPTION
//		      WHEN NO_DATA_FOUND THEN null;
//		   END;
		
		libelle = this.getLabelFromNomenclature(salary.getInfoSalary().getComp_id().getCdos(),3, salary.getInfoSalary().getNiv3(), 2, salary.getNlot(), salary.getNbul(), this.salary.getMoisPaieCourant(),
				this.salary.getPeriodOfPay(), rubrique.getRubrique().getComp_id().getCrub());
//
//		   IF wfnom.lib2 IS NULL THEN
//		      wfnom.lib2 := 'N';
//		   END IF;
		if(StringUtils.isBlank(libelle))
			libelle = "N";
//
//		   wcalc_acpte := SUBSTR(wfnom.lib2,1,1);
		calc_acpte = libelle.charAt(0);
//		   IF wcalc_acpte != 'O' and wcalc_acpte != 'o' THEN
//		      wcalc_acpte := 'N';
//		   ELSE
//		      wcalc_acpte := 'O';
//		   END IF;
		
		if(calc_acpte != 'O' && calc_acpte != 'o')
			calc_acpte = 'N';
		else
			calc_acpte = 'O';
//
//		   IF wcalc_acpte = 'N' THEN
//		      PA_ALGO.w_bas  := 0;
//		      PA_ALGO.w_basp := 0;
//		      PA_ALGO.w_tau  := 0;
//		      PA_ALGO.w_mon  := 0;
//		      RETURN TRUE;
//		   END IF;
		if(calc_acpte == 'N')
		 {
				salary.getValeurRubriquePartage().setBase(0);
				salary.getValeurRubriquePartage().setBasePlafonnee(0);
				salary.getValeurRubriquePartage().setRates(0);
				salary.getValeurRubriquePartage().setAmount(0);
				return true;
			}
//
//		   wtotretenue := 0;
		totretenue = 0;
//		   wtotcalc := 0;
		totcalc = 0;
//
//		    -- Declaration d' un curseur sur la table des prets internes
//
//		      OPEN curs_pretint;
			List<PretInterne> pretints = salary.getService().find(curs_pretint);
			for (PretInterne pretint : pretints)
			{
				
			
//		      LOOP
//		         FETCH curs_pretint INTO wpretint;
//		         EXIT WHEN curs_pretint%NOTFOUND;
//
//		         IF wpretint.mtpr IS NULL THEN
//		            wpretint.mtpr := 0;
//		         END IF;
				if(pretint.getMtpr() == null)
					pretint.setMtpr(new BigDecimal(0));
				
//		         IF wpretint.mtremb IS NULL THEN
//		            wpretint.mtremb := 0;
//		         END IF;
				if(pretint.getMtremb() == null)
					pretint.setMtremb(new BigDecimal(0));
				
//		         IF wpretint.mtmens IS NULL THEN
//		            wpretint.mtmens := 0;
//		         END IF;
				if(pretint.getMtmens() == null)
					pretint.setMtmens(new BigDecimal(0));
				
//		         IF wpretint.nbmens IS NULL THEN
//		            wpretint.nbmens := 0;
//		         END IF;
				if(pretint.getNbmens() == null)
					pretint.setNbmens(new BigDecimal(0));
//
//		         IF wpretint.etatpr = 'D' AND wpretint.premrb >= PA_ALGO.w_aamm THEN
//		            IF wpretint.mtpr != 0 THEN
//		               IF wpretint.mtpr > wpretint.mtremb THEN
//		                  IF wpretint.mtmens != 0 THEN
//		                     wtotretenue := wtotretenue + wpretint.mtmens;
//		                  ELSIF wpretint.nbmens != 0 THEN
//		                     wtotcalc := wpretint.mtpr / wpretint.nbmens;
//		                     wtotretenue := wtotretenue + wtotcalc;
//		   			END IF;
//		               END IF; 
//		            END IF;
//		         END IF;
				if(StringUtils.equals(pretint.getEtatpr(), "D") && salary.getMonthOfPay().compareTo(pretint.getPremrb()) <= 0)
				{
					if(pretint.getMtpr().compareTo(new BigDecimal(0)) != 0)
					{
						if(pretint.getMtpr().compareTo(pretint.getMtremb()) > 0)
						{
							if(pretint.getMtmens().compareTo(new BigDecimal(0)) != 0)
								totretenue += pretint.getMtmens().doubleValue();
							else
							{
								if(pretint.getNbmens().compareTo(new BigDecimal(0)) != 0)
								{
									totcalc = (pretint.getMtpr().divide(pretint.getNbmens())).doubleValue();
									totretenue += totcalc;
								}
							}
						}
					}
				}
//		               
//		      END LOOP;
//		      CLOSE curs_pretint;
			}
//
//		    -- Declaration d' un curseur sur la table des prets externes
//
//		      OPEN curs_pretexe;
			List<PretExterneEntete> pretents = salary.getService().find(curs_pretexe);
			List<PretExterneDetail> pretligs = new ArrayList<PretExterneDetail>();
//		      LOOP
			for (PretExterneEntete pretexe: pretents)
			{
//		         FETCH curs_pretexe INTO wpretexe;
//		         EXIT WHEN curs_pretexe%NOTFOUND;
//
//		         IF wpretexe.pact = 'O' AND wpretexe.etpr = 'D' THEN
				if(StringUtils.equals(pretexe.getPact(), "O") && StringUtils.equals(pretexe.getEtpr(), "D"))
				{
//
//		              OPEN curs_pretexl;
					curs_pretexl = "From PretExterneDetail where identreprise='"+salary.getParameter().getDossier()+"' and nprt= '"+pretexe.getNprt()+"' order by nprt";
					pretligs = salary.getService().find(curs_pretexl);
//		              LOOP
					for (PretExterneDetail pretexl: pretligs)
					{
						if ( ! new ClsDate(pretexl.getPerb(), this.salary.getParameter().getAppDateFormat()).getYearAndMonth().equals(salary.getPeriodOfPay()))
							continue;
//		                 FETCH curs_pretexl INTO wpretexl;
//		                 EXIT WHEN curs_pretexl%NOTFOUND;
//		 
//		                 IF wpretexl.echr IS NULL THEN
//		                    wpretexl.echr := 0;
//		                 END IF;
						if(pretexl.getEchr() == null)
							pretexl.setEchr(new BigDecimal(0));
//
//		                 wtotretenue := wtotretenue + wpretexl.echr;
						totretenue += pretexl.getEchr().doubleValue();
//
//		              END LOOP;
//		              CLOSE curs_pretexl;
					}
//		         END IF;
				}
//		               
//		      END LOOP;
//		      CLOSE curs_pretexe;
			}
//
//		   IF wtotretenue > PA_ALGO.w_bas THEN
//		      PA_ALGO.w_bas  := 0;
//		      PA_ALGO.w_basp := 0;
//		      PA_ALGO.w_tau  := 0;
//		      PA_ALGO.w_mon  := 0;
//		   ELSE
//		      PA_ALGO.w_bas := PA_ALGO.w_bas - wtotretenue;
//		   END IF;
			if(totretenue > salary.getValeurRubriquePartage().getBase())
			 {
					salary.getValeurRubriquePartage().setBase(0);
					salary.getValeurRubriquePartage().setBasePlafonnee(0);
					salary.getValeurRubriquePartage().setRates(0);
					salary.getValeurRubriquePartage().setAmount(0);
			}
			else
				salary.getValeurRubriquePartage().setBase(salary.getValeurRubriquePartage().getBase() -  totretenue);
			
			this.algo5(rubrique);
//
//		   W_Retour := PA_ALGO.algo5;
//
//		   RETURN TRUE;
		return true;
	}
	
	/**
	 * Algo Ecrit le 06/01/2011 é 09h
	 */
	public boolean algo70(ClsRubriqueClone rubrique)
	{
		String error = null;
		salary.getValeurRubriquePartage().setBase(0);
		salary.getValeurRubriquePartage().setBasePlafonnee(0);
		salary.getValeurRubriquePartage().setAmount(0);
		salary.getValeurRubriquePartage().setRates(0);
		// salary.getParameter().setPbWithCalulation(false);

		String cleAcces = rubrique.getRubrique().getComp_id().getCrub();
		String cdos = salary.parameter.dossier;
		List<ParamData> lst = salary.service.find("From ParamData where identreprise='" + cdos + "' and ctab = " + rubrique.getRubrique().getTabl() + "  and cacc = '" + cleAcces
				+ "' order by nume");
		if (lst.isEmpty())
		{
			error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + salary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + cleAcces + ": Parametre " + cleAcces + ", Mq en T "
					+ rubrique.getRubrique().getTabl();
			salary.getParameter().setError(error);
			salary.getParameter().setPbWithCalulation(true);
			return false;
		}
		String[] lib = new String[] { StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY };
		Integer[] mnt = new Integer[] { 0, 0, 0 };
		ParamData fnom = null;
		Integer nume = 0;
		ClsRubriqueClone clone = null;
		double[] valeurs = new double[] { 0, 0, 0 };
		for (int i = 0; i < lst.size(); i++)
		{
			fnom = lst.get(i);
			nume = fnom.getNume();
			if (nume == i + 1)
			{
				if (StringUtils.isNotBlank(fnom.getVall()))
					lib[i] = fnom.getVall();
				if (fnom.getValm() != null)
					mnt[i] = fnom.getValm().intValue();
			}
		}

		for (int i = 0; i < 3; i++)
		{
			if (StringUtil.notIn(mnt[i].toString(), "0,1,2"))
			{
				error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + salary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + cleAcces + ",:  Clé " + cleAcces + ", T "
						+ rubrique.getRubrique().getTabl() + " Mont " + (i + 1) + " incorrect. ";
				salary.getParameter().setError(error);
				salary.getParameter().setPbWithCalulation(true);
				return false;
			}
			if (StringUtils.isBlank(lib[i]))
			{
				error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + salary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + cleAcces + ",:  Clé " + cleAcces + ", T "
						+ rubrique.getRubrique().getTabl() + " Lib " + (i + 1) + " incorrect. ";
				salary.getParameter().setError(error);
				salary.getParameter().setPbWithCalulation(true);
				return false;
			}

			// hjhjh

			int j = 0;
			String crub;
			String sign;
			double valeur = 0;
			while (j < lib[i].length())
			{
				if (StringUtils.isNotBlank(StringUtils.substring(lib[i], j, j + 1)))
				{
					sign = StringUtils.substring(lib[i], j, j + 1);
					crub = StringUtils.substring(lib[i], j + 1, j + ParameterUtil.longueurRubrique +1);

					clone = salary.findRubriqueClone(crub);
					if (clone == null)
					{
						error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + salary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + cleAcces + ",:  Clé " + cleAcces + ", T "
								+ rubrique.getRubrique().getTabl() + " Lib " + (i + 1) + " Rbq " + crub + " inexistante. ";
						salary.getParameter().setError(error);
						salary.getParameter().setPbWithCalulation(true);
						return false;
					}
					if (mnt[i] == 0)
						valeur = clone.getBase();
					if (mnt[i] == 1)
						valeur = clone.getRates();
					if (mnt[i] == 2)
						valeur = clone.getAmount();

					if ("+".equals(sign))
						valeurs[i] += valeur;
					if ("-".equals(sign))
						valeurs[i] -= valeur;
					if ("*".equals(sign))
						valeurs[i] *= valeur;
					if ("/".equals(sign))
						if (valeur != 0)
							valeurs[i] /= valeur;
					j = j + ParameterUtil.longueurRubrique +1;
				}
			}
		}

		salary.getValeurRubriquePartage().setBase(valeurs[0]);
		salary.getValeurRubriquePartage().setBasePlafonnee(valeurs[0]);
		salary.getValeurRubriquePartage().setRates(valeurs[1]);
		salary.getValeurRubriquePartage().setAmount(valeurs[2]);

		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo78(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo78(ClsRubriqueClone rubrique)
	{
//		if(StringUtils.equals(salary.parameter.getNomClient(), ClsEntreprise.COMILOG))
//			return comilog.algo78(rubrique);
//
//		if(StringUtils.equals(salary.parameter.getNomClient(), ClsEntreprise.TASIAST_MAURITANIE))
//			return tasiast78(rubrique);
		
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo78";
		String cdos = salary.getInfoSalary().getComp_id().getCdos();
		String crub = rubrique.getRubrique().getComp_id().getCrub();
		String tabl = rubrique.getRubrique().getTabl();
		String toum = rubrique.getRubrique().getToum();
		Long nume= rubrique.getRubrique().getNutm();
		double montantbareme = new Double(0);
		double tauxbareme = 0;
		salary.getValeurRubriquePartage().setInter(0);
		int i = 0, j = 0;
		String typeCalcul = "";	
		if(!(StringUtils.isNotBlank(tabl) && StringUtils.isNotBlank(toum) && nume != null && StringUtils.equalsIgnoreCase("M", toum)))
		
		typeCalcul = this.getLabelFromNomenclature(cdos, 99, "ALGO-78", 2, salary.getNlot(), salary.getNbul(), salary.getMoisPaieCourant(), salary.getPeriodOfPay(), rubrique.getRubrique().getComp_id().getCrub());
		
		if (ClsObjectUtil.isNull(typeCalcul) || (StringUtil.notIn(typeCalcul, "1,2")))
			typeCalcul = "2";
		
		if(salary.parameter.isStc())
			typeCalcul = "3";
		
		String typeDate="";
		if(StringUtils.isNotBlank(tabl) && StringUtils.isNotBlank(toum) && nume != null && StringUtils.equalsIgnoreCase("M", toum))
		{
			typeCalcul = "1";
			Number	taux = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,cdos, Integer.valueOf(tabl), crub,nume , this.salary.getNlot(),
				this.salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.AMOUNT, crub);
			if(taux != null) typeDate = taux.intValue()+"";
		}
		
		if (ClsObjectUtil.isNull(typeDate) || (StringUtil.notIn(typeDate, "1,2,3,4,5,6,7")))
			typeDate = "2";
		
		
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "..................................Type Calcul = "+typeCalcul;
		//
		if ("2".equals(typeCalcul))
		{
			// spécifique edm
//			if (StringUtils.equals(salary.getParameter().nomClient, ClsEntreprise.EDM))
//			{
//				// -- MM 12/2003 Prise en compte de dtes
//				// -- Conversion de l'anciennete en nombre de jours
//				if (salary.getAnciennete() != 0)
//					salary.getValeurRubriquePartage().setInter(360 * salary.getAnciennete());
//				i = new ClsDate(salary.getInfoSalary().getDtes()).getMonth();
//				j = salary.getMyMonthOfPay().getMonth();
//				j = j - i;
//				if (j < 0)
//					j = j + 12;
//				i = new ClsDate(salary.getInfoSalary().getDtes()).getDay();
//				salary.getValeurRubriquePartage().setInter(salary.getValeurRubriquePartage().getInter() + (30 * j ) + i);
//			}
//			else
//			{
				// -- MM 12/2003 Prise en compte de ddca
				// -- Conversion de l'anciennete en nombre de jours
				if (salary.getAnciennete() != 0)
					salary.getValeurRubriquePartage().setInter(360 * salary.getAnciennete());
				if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "..................................Inter Initial (360 * ancienn) = (360 * "+salary.getAnciennete()+")="+salary.getValeurRubriquePartage().getInter();
				i = new ClsDate(salary.getInfoSalary().getDdca()).getMonth();
				j = salary.getMyMonthOfPay().getMonth();
				j = j - i;
				if (j < 0)
					j = j + 12;
				i = new ClsDate(salary.getInfoSalary().getDdca()).getDay();
				if (i == 1)
					i = 0;
				else if (i == 31)
					i = 30;
				if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "..................................Nombre de mois = "+j+" et nombre de jours = "+i;
				salary.getValeurRubriquePartage().setInter(salary.getValeurRubriquePartage().getInter() + (30 * (j + 1)) - i);//Identique é 30*j +(30-i)
				if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "..................................Nombre de jours baréme "+salary.getValeurRubriquePartage().getInter() ;
			//}
		}
		if ("1".equals(typeCalcul))
		{
			double tmpInter = 0;
			ClsDate ddtes = new ClsDate(salary.getInfoSalary().getDtes());
			// spécifique cnss
//			if (StringUtils.equals(salary.getParameter().nomClient, ClsEntreprise.CNSS))
//			{
////				--TFN 28/02/2007 - Test si l'année est bissextile ou non.
////				IF (to_number(TO_char(to_date(w_aamm),'YYYY'))/4) = TRUNC(to_number(TO_char(to_date(w_aamm),'YYYY'))/4) then --Année bissextile
//				if((salary.getMyMonthOfPay().getYear() / 4) == Math.round(salary.getMyMonthOfPay().getYear() / 4))
//				{
////					j := TRUNC(MONTHS_BETWEEN(last_day(TO_DATE(w_aamm,'YYYYMM')),PA_CALCUL.wsal01.dtes));
////		        i := to_number(TO_CHAR( PA_CALCUL.wsal01.dtes,'DD'));
////		        IF i IS NULL THEN
////		          i := 0;
////		        END IF;
////		        IF j IS NULL THEN
////		          j := 0;
////		        END IF;
////		        IF i = to_number(to_char(last_day(PA_CALCUL.wsal01.dtes),'dd')) THEN
////		            i := 30;
////		        END IF;
////		        w_inter := (30 * (j + 1)) - i;
//					j = ClsDate.getMonthsBetween(new ClsDate(salary.getPeriodOfPay(), "yyyyMM").getLastDayOfMonth(), salary.getInfoSalary().getDtes());
//					i = new ClsDate(salary.getInfoSalary().getDtes()).getDay();
//					// recueillir le dernier jour du mois ddtes
//					int i1 = new ClsDate(ddtes.getLastDayOfMonth()).getDay();
//					if (i == i1)
//						i = 30;
//
//					tmpInter = (30 * (j + 1)) - i;
//				}
//				else
//				{
////					j := TRUNC(MONTHS_BETWEEN(TO_DATE(w_aamm,'YYYYMM'),PA_CALCUL.wsal01.dtes));
////		        i := to_number(TO_CHAR( PA_CALCUL.wsal01.dtes,'DD'));
////		        w_inter := ( 30 * j ) + i;
//					j = Double.valueOf(Math.ceil(salary.utilNomenclature.getMonthsBetween(new ClsDate(salary.getPeriodOfPay(), "yyyyMM").getLastDayOfMonth(), salary.getInfoSalary().getDtes()))).intValue();
//					i = new ClsDate(salary.getInfoSalary().getDtes()).getDay();
//					tmpInter = (30 * j) + i;
//				//}
//			}
//			else
//			{
				Date dt = null;
				String champ="";
				if(StringUtils.equalsIgnoreCase(typeDate, "1")){ dt = salary.getInfoSalary().getDtes();champ=ClsTreater._getMessage("INF-00599");}
				else if(StringUtils.equalsIgnoreCase(typeDate, "2")){ dt = salary.getInfoSalary().getDdca();champ=ClsTreater._getMessage("INF-00709");}
				else if(StringUtils.equalsIgnoreCase(typeDate, "3")){ dt = salary.getInfoSalary().getDfes();champ=ClsTreater._getMessage("INF-00708");}
				else if(StringUtils.equalsIgnoreCase(typeDate, "4")){ dt = salary.getInfoSalary().getDtit();champ=ClsTreater._getMessage("INF-00701");}
				else if(StringUtils.equalsIgnoreCase(typeDate, "5")){ dt = salary.getInfoSalary().getDepr();champ=ClsTreater._getMessage("INF-00700");}
				else if(StringUtils.equalsIgnoreCase(typeDate, "6")){ dt = salary.getInfoSalary().getDecc();champ=ClsTreater._getMessage("INF-00711");}
				if(dt == null)
				{
					
					String error = ClsTreater._getResultat("Algo %, Matricule % Rubrique % Date de référence nulle","ERR-10522",true,rubrique.getRubrique().getAlgo()+"",salary.getInfoSalary().getComp_id().getNmat(), rubrique.getRubrique().getComp_id().getCrub()).getLibelle();
					error +="["+champ+"]";
					error = salary.getParameter().errorMessage(error, salary.getParameter().getLangue() );
					salary.getParameter().setError(error);
					return false;
				}
				
				j = Double.valueOf(salary.utilNomenclature.getMonthsBetween(new ClsDate(salary.getPeriodOfPay(), "yyyyMM").getLastDayOfMonth(), dt)).intValue();
				
				//System.out.println("Nombre de mois entre "+(new ClsDate(new ClsDate(salary.getPeriodOfPay(), "yyyyMM").getLastDayOfMonth()).getDateS("dd/MM/yyyy"))+" et "+ new ClsDate(dt).getDateS("dd/MM/yyyy")+" = "+j);
				i = new ClsDate(dt).getDay();
				// recueillir le dernier jour du mois ddtes
				int i1 = new ClsDate(new ClsDate(dt).getLastDayOfMonth()).getDay();
				if (i == i1)
					i = 30;
				
//				if (i == 1)
//					i = 0;
//				else if (i == 31)
//					i = 30;
				
				tmpInter = (30 * (j + 1)) - i;
				//System.out.println("Nombre de jours final = "+tmpInter);
			//}
			salary.getValeurRubriquePartage().setInter(tmpInter);
		}
		
		if ("3".equals(typeCalcul))
		{
			double tmpInter = 0;
			ClsDate dtes = new ClsDate(salary.getInfoSalary().getDtes());
			ClsDate dmrr = new ClsDate(salary.getInfoSalary().getDmrr());
			if(salary.infoSalary.getDmrr() == null) dmrr = new ClsDate(new ClsDate(salary.getPeriodOfPay(), "yyyyMM").getLastDayOfMonth());
			
			int year = dmrr.getYear();
			int mois = dmrr.getMonth();
			int day = dmrr.getDay();
			
			int year2 = dtes.getYear();
			int mois2 = dtes.getMonth();
			int day2 = dtes.getDay();
			
			//Les jours
			if(day < day2)
			{
				mois = mois -1;
				day = day+30;
			}
			//Les mois
			if(mois < mois2)
			{
				year = year - 1;
				mois = mois + 12;
			}
			//Les années
			if(year < year2) year = year2;
			
			tmpInter = (360*(year-year2))+ (30*(mois-mois2))+ (day-day2+1);
			
			salary.getValeurRubriquePartage().setInter(tmpInter);
		}
		//
		// String queryString = " from ElementSalaireBareme "
		// + " where identreprise='" + cdos + "'"
		// + " and crub = '" + crub + "'"
		// + " order by cdos , crub , nume";
		// //
		// String queryStringRetro = " from Rhthrubbarem "
		// + " where identreprise='" + cdos + "'"
		// + " and crub = '" + crub + "'"
		// + " and aamm = '" + salary.getPeriodOfPay() + "'"
		// + " and nbul = " + salary.getNbul()
		// + " order by cdos , crub , nume";
		//
//		salary.getValeurRubriquePartage().setRates(0);
//		salary.getValeurRubriquePartage().setAmount(0);
		// -- Lecture de la donnee designee par la cle1
		String valeur1 = "";
		String valeur2 = "";
		int nbreBaremeRubrique = 0;
		double dblValeur1 = 0;
		double dblValeur2 = 0;
		//Rhthrubbarem oRhthrubbarem = null;
		ElementSalaireBareme oElementSalaireBareme = null;
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "..................................Montant initial = "+salary.getValeurRubriquePartage().getAmount();
		// List listOfBarem = salary.getParameter().isUseRetroactif() ? salary.getService().find(queryStringRetro) : salary.getService().find(queryString);
		String keyOfBaremeList = salary.getParameter().isUseRetroactif() ? crub + this.salary.getPeriodOfPay() + this.salary.getNbul() : crub;
		List listOfBarem = (List) salary.getParameter().getListOfRubriquebaremeMap().get(keyOfBaremeList);
		if (listOfBarem != null)
			for (Object barem : listOfBarem)
			{
				if (salary.getParameter().isUseRetroactif())
				{
//					oRhthrubbarem = (Rhthrubbarem) barem;
//					if (oRhthrubbarem.getMont() != null)
//						montantbareme = oRhthrubbarem.getMont().doubleValue();
//					if (oRhthrubbarem.getTaux() != null)
//						tauxbareme = oRhthrubbarem.getTaux().doubleValue();
//					valeur1 = oRhthrubbarem.getVal1();
//					valeur2 = oRhthrubbarem.getVal2();
				}
				else
				{
					oElementSalaireBareme = (ElementSalaireBareme) barem;
//					if (oElementSalaireBareme.getMont() != null)
//						montantbareme = oElementSalaireBareme.getMont().doubleValue();
//					if (oElementSalaireBareme.getTaux() != null)
//						tauxbareme = oElementSalaireBareme.getTaux().doubleValue();
//					valeur1 = oElementSalaireBareme.getVal1();
//					valeur2 = oElementSalaireBareme.getVal2();
					montantbareme = rubrique.getTauxOuMontantBareme(oElementSalaireBareme, false);
					tauxbareme = rubrique.getTauxOuMontantBareme(oElementSalaireBareme, true);
					valeur1 = rubrique.getValeurBareme(oElementSalaireBareme, true);
					valeur2 = rubrique.getValeurBareme(oElementSalaireBareme, false);
				}
				//
				nbreBaremeRubrique++;
				if (nbreBaremeRubrique > 50)
				{
					nbreBaremeRubrique = 50;
					break;
				}
				if (!ClsObjectUtil.isNull(valeur1) && !ClsObjectUtil.isNull(valeur2))
				{
					dblValeur1 = rubrique.convertToNumber(valeur1);
					dblValeur2 = rubrique.convertToNumber(valeur2);
					if (salary.getValeurRubriquePartage().getInter() <= dblValeur2)
						break;
					//
					salary.getValeurRubriquePartage().setValeur((dblValeur2 - dblValeur1) * tauxbareme);
					salary.getValeurRubriquePartage().setAmount(salary.getValeurRubriquePartage().getAmount() + salary.getValeurRubriquePartage().getValeur());
				}
			}
		//
		if (nbreBaremeRubrique == 0)
		{
			// logger
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90061", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat()));
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + salary.getParameter().getError();
			return false;
		}
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "..................................Derniére borne = "+dblValeur1+" - "+dblValeur2;
		montantbareme = salary.getValeurRubriquePartage().getAmount() + tauxbareme * (salary.getValeurRubriquePartage().getInter() - dblValeur1);
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "..................................Montant baréme =(tauxbareme * (inter - dblvaleur1) "+montantbareme;
		salary.getValeurRubriquePartage().setAmount(montantbareme);
		// -- Calcul du montant
		salary.getValeurRubriquePartage().setRates(salary.getValeurRubriquePartage().getAmount() / 360);
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "..................................Taux =(montantbareme/360) "+salary.getValeurRubriquePartage().getRates();
		montantbareme = salary.getValeurRubriquePartage().getBase() * salary.getValeurRubriquePartage().getRates() / 100;
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "..................................Base rubrique "+salary.getValeurRubriquePartage().getBase();
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "..................................Montant final =(base * taux/100 "+montantbareme;
		salary.getValeurRubriquePartage().setAmount(montantbareme);
		//
		return true;
	}
	
	public boolean tasiast78(ClsRubriqueClone rubrique)
	{
		if ('O' == salary.getParameter().getGenfile())
			outputtext += "\n" + ">>algo78";
		String cdos = salary.getInfoSalary().getComp_id().getCdos();
		String crub = rubrique.getRubrique().getComp_id().getCrub();
		Double montantbareme = new Double(0);
		double tauxbareme = 0;
		int i = 0, j = 0;
		String typeCalcul = salary.getUtilNomenclature().getLabelFromNomenclature(cdos, 99, "ALGO-78", 2, salary.getNlot(), salary.getNbul(), salary.getMoisPaieCourant(), salary.getPeriodOfPay());
		if (ClsObjectUtil.isNull(typeCalcul) || (!"1".equals(typeCalcul) && !"2".equals(typeCalcul)))
			typeCalcul = "2";
		salary.getValeurRubriquePartage().setInter(0);
		if ('O' == salary.getParameter().getGenfile())
			outputtext += "\n" + "..................................Type Calcul = " + typeCalcul;
		//
		if ("2".equals(typeCalcul))
		{
			if (salary.getAnciennete() != 0)
				salary.getValeurRubriquePartage().setInter(360 * salary.getAnciennete());
			if ('O' == salary.getParameter().getGenfile())
				outputtext += "\n" + "..................................Inter Initial (360 * ancienn) = (360 * " + salary.getAnciennete() + ")=" + salary.getValeurRubriquePartage().getInter();
			i = new ClsDate(salary.getInfoSalary().getDdca()).getMonth();
			j = salary.getMyMonthOfPay().getMonth();
			j = j - i;
			if (j < 0)
				j = j + 12;
			i = new ClsDate(salary.getInfoSalary().getDdca()).getDay();
			if (i == 1)
				i = 0;
			else if (i == 31)
				i = 30;
			if ('O' == salary.getParameter().getGenfile())
				outputtext += "\n" + "..................................Nombre de mois = " + j + " et nombre de jours = " + i;
			salary.getValeurRubriquePartage().setInter(salary.getValeurRubriquePartage().getInter() + (30 * (j + 1)) - i);//Identique é 30*j +(30-i)
			if ('O' == salary.getParameter().getGenfile())
				outputtext += "\n" + "..................................Nombre de jours baréme " + salary.getValeurRubriquePartage().getInter();
		}
		else
		{
			double tmpInter = 0;
			ClsDate ddtes = new ClsDate(salary.getInfoSalary().getDtes());

			j = ClsDate.getMonthsBetween(new ClsDate(salary.getPeriodOfPay(), "yyyyMM").getLastDayOfMonth(), salary.getInfoSalary().getDtes());
			i = new ClsDate(salary.getInfoSalary().getDtes()).getDay();
			// recueillir le dernier jour du mois ddtes
			int i1 = new ClsDate(ddtes.getLastDayOfMonth()).getDay();
			if (i == i1)
				i = 30;

			tmpInter = (30 * (j + 1)) - i;

			salary.getValeurRubriquePartage().setInter(tmpInter);
		}

		String valeur1 = "";
		String valeur2 = "";
		int nbreBaremeRubrique = 0;
		double dblValeur1 = 0;
		double dblValeur2 = 0;
		//Rhthrubbarem oRhthrubbarem = null;
		ElementSalaireBareme oElementSalaireBareme = null;
		if ('O' == salary.getParameter().getGenfile())
			outputtext += "\n" + "..................................Montant initial = " + salary.getValeurRubriquePartage().getAmount();
		// List listOfBarem = salary.getParameter().isUseRetroactif() ? salary.getService().find(queryStringRetro) : salary.getService().find(queryString);
		String keyOfBaremeList = salary.getParameter().isUseRetroactif() ? crub + this.salary.getPeriodOfPay() + this.salary.getNbul() : crub;
		List listOfBarem = (List) salary.getParameter().getListOfRubriquebaremeMap().get(keyOfBaremeList);
		if (listOfBarem != null)
			for (Object barem : listOfBarem)
			{
				if (salary.getParameter().isUseRetroactif())
				{
//					oRhthrubbarem = (Rhthrubbarem) barem;
//					if (oRhthrubbarem.getMont() != null)
//						montantbareme = oRhthrubbarem.getMont().doubleValue();
//					if (oRhthrubbarem.getTaux() != null)
//						tauxbareme = oRhthrubbarem.getTaux().doubleValue();
//					valeur1 = oRhthrubbarem.getVal1();
//					valeur2 = oRhthrubbarem.getVal2();
				}
				else
				{
					oElementSalaireBareme = (ElementSalaireBareme) barem;
					if (oElementSalaireBareme.getMont() != null)
						montantbareme = oElementSalaireBareme.getMont().doubleValue();
					if (oElementSalaireBareme.getTaux() != null)
						tauxbareme = oElementSalaireBareme.getTaux().doubleValue();
					valeur1 = oElementSalaireBareme.getVal1();
					valeur2 = oElementSalaireBareme.getVal2();
				}
				//
				nbreBaremeRubrique++;
				if (nbreBaremeRubrique > 50)
				{
					nbreBaremeRubrique = 50;
					break;
				}
				if (!ClsObjectUtil.isNull(valeur1) && !ClsObjectUtil.isNull(valeur2))
				{
					dblValeur1 = rubrique.convertToNumber(valeur1);
					dblValeur2 = rubrique.convertToNumber(valeur2);
					if (salary.getValeurRubriquePartage().getInter() <= dblValeur2)
						break;
					//
					salary.getValeurRubriquePartage().setValeur((dblValeur2 - dblValeur1) * tauxbareme);
					salary.getValeurRubriquePartage().setAmount(salary.getValeurRubriquePartage().getAmount() + salary.getValeurRubriquePartage().getValeur());
				}
			}
		//
		if (nbreBaremeRubrique == 0)
		{
			// logger
			salary.getParameter().setError(salary.getParameter().errorMessage("ERR-90061", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(), salary.getInfoSalary().getComp_id().getNmat()));
			if ('O' == salary.getParameter().getGenfile())
				outputtext += "\n" + salary.getParameter().getError();
			return false;
		}
		if ('O' == salary.getParameter().getGenfile())
			outputtext += "\n" + "..................................Derniére borne = " + dblValeur1 + " - " + dblValeur2;
		montantbareme = salary.getValeurRubriquePartage().getAmount() + tauxbareme * (salary.getValeurRubriquePartage().getInter() - dblValeur1);
		if ('O' == salary.getParameter().getGenfile())
			outputtext += "\n" + "..................................Montant baréme =(tauxbareme * (inter - dblvaleur1) " + montantbareme;
		salary.getValeurRubriquePartage().setAmount(montantbareme);
		// -- Calcul du montant
		salary.getValeurRubriquePartage().setRates(salary.getValeurRubriquePartage().getAmount() / 360);
		if ('O' == salary.getParameter().getGenfile())
			outputtext += "\n" + "..................................Taux =(montantbareme/360) " + salary.getValeurRubriquePartage().getRates();
		montantbareme = salary.getValeurRubriquePartage().getBase() * salary.getValeurRubriquePartage().getRates() / 100;
		if ('O' == salary.getParameter().getGenfile())
			outputtext += "\n" + "..................................Base rubrique " + salary.getValeurRubriquePartage().getBase();
		if ('O' == salary.getParameter().getGenfile())
			outputtext += "\n" + "..................................Montant final =(base * taux/100 " + montantbareme;
		salary.getValeurRubriquePartage().setAmount(montantbareme);
		//
		return true;
	}

	

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo82(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo82(ClsRubriqueClone rubrique)
	{
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo82";
		int n = 0;
		int nt = 0;
		double valeur = 0;
		if (salary.getWorkTime().getNbreJourCongesPonctuels() == 0)
			salary.getValeurRubriquePartage().setBase(0);
		else
		{
			salary.getValeurRubriquePartage().setRates(salary.getWorkTime().getNbreJourAbsencePourCongesPonctuels());// Jours calendaires
			if (salary.getWorkTime().getMontantCongePonctuel() != 0)
				salary.getValeurRubriquePartage().setAmount(salary.getWorkTime().getMontantCongePonctuel());
			else
			{
				valeur = salary.getValeurRubriquePartage().getBase() + salary.getInfoSalary().getDapa().doubleValue() + salary.getInfoSalary().getDapec().doubleValue();
				salary.getValeurRubriquePartage().setBase(valeur);
				salary.getValeurRubriquePartage().setBasePlafonnee(valeur);
				nt = salary.getInfoSalary().getNbjtr().intValue();
				n = rechercheNombreDeJourTravailDuMois(rubrique);
				if (n <= 0)
					n = 0;
				if (nt <= 0)
					nt = 0;
				if (n == 0 && nt == 0)
					salary.getValeurRubriquePartage().setAmount(0);
				else
				{
					salary.getValeurRubriquePartage().setAmount((salary.getValeurRubriquePartage().getBase() / (n + nt)) * salary.getWorkTime().getNbreJourAbsencePourCongesPonctuels());
					salary.getWorkTime().setMontantCongePonctuel(salary.getValeurRubriquePartage().getAmount());
				}
			}
		}
		//
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo83(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo83(ClsRubriqueClone rubrique)
	{
		double n = 0;
		double nt = 0;
		double montant = 0;
		// ------------------- Calcul pour un STC ----------------------
		if (salary.getParameter().isStc() && (salary.getWorkTime().getNbreJourConges() == 0 && salary.getWorkTime().getNbreJourCongesNonPris() == 0))
		{
			n = rechercheNombreDeJourTravailDuMois(rubrique);
			//
			// -- RAZ base des conges si conges de debut de mois
			if (salary.getWorkTime().isDebutDeMois())
			{
				salary.getValeurRubriquePartage().setBase(0);
				n = 0;
			}
			//
			salary.getValeurRubriquePartage().setInter(salary.getInfoSalary().getDapa().doubleValue() + salary.getInfoSalary().getDapec().doubleValue() + salary.getValeurRubriquePartage().getBase());
			nt = 0;
			if (!ClsObjectUtil.isNull(salary.getInfoSalary().getNbjtr()))
				nt = salary.getInfoSalary().getNbjtr().doubleValue();
			if (nt == 0 && n == 0)
			{
				salary.getValeurRubriquePartage().setAmount(0);
				return true;
			}
			salary.getValeurRubriquePartage().setBase(salary.getValeurRubriquePartage().getInter() / (nt + n));
			salary.getValeurRubriquePartage().setRates(salary.getInfoSalary().getJapa().doubleValue() + salary.getInfoSalary().getJapec().doubleValue());
			montant = salary.getValeurRubriquePartage().getBase() * salary.getValeurRubriquePartage().getRates() - salary.getInfoSalary().getDded().doubleValue();
			montant = montant - salary.getWorkTime().getMontantCongePonctuel();
			salary.getValeurRubriquePartage().setAmount(montant);
			salary.getValeurRubriquePartage().setBasePlafonnee(montant);
			//salary.getValeurRubriquePartage().setBase(montant);
			//
			return true;
		}
		// --------------- Calcul pour un conges annuel ------------------
		if (salary.getWorkTime().getNbreJourConges() == 0 && salary.getWorkTime().getNbreJourCongesNonPris() == 0 && salary.getWorkTime().getNbreJourCongesAnnuelMoisAnte() == 0
				&& salary.getWorkTime().getNbreJourCongesAnnuelMoisSuiv() == 0)
		{
			// -- Pas de conges ce mois ci
			salary.getValeurRubriquePartage().setAmount(0);
			salary.getValeurRubriquePartage().setRates(0);
			salary.getValeurRubriquePartage().setBase(0);
		}
		else
		{
			// -- Nb jrs trav du mois
			n = rechercheNombreDeJourTravailDuMois(rubrique);
			//
			// -- RAZ base des conges si conges de debut de mois
			if (salary.getWorkTime().isDebutDeMois())
			{
				salary.getValeurRubriquePartage().setBase(0);
				n = 0;
			}
			montant = salary.getInfoSalary().getDapa().doubleValue() + salary.getInfoSalary().getDapec().doubleValue() + salary.getValeurRubriquePartage().getBase();
			salary.getValeurRubriquePartage().setInter(montant);
			//
			// -- Ajout du nb de jours payes non pris (PA_CALCUL.wnbjcapnp)
			// -- pour prise en compte dans le calcul du conge
			if (! "O".equals(salary.getParameter().getFictiveCalculus()))
			{
				// -- Pas de traitement fictif, on paie tout le premier mois
				if (salary.getPeriodOfPay().equals(salary.getInfoSalary().getPmcf()))
				{
					montant = salary.getWorkTime().getNbreJourCongesAnnuelPayeNonPris() + salary.getInfoSalary().getNbjaf().doubleValue();
					salary.getValeurRubriquePartage().setRates(montant);
				}
				else
				{
					salary.getValeurRubriquePartage().setAmount(0);
					//salary.getValeurRubriquePartage().setBasePlafonnee(0);
					salary.getValeurRubriquePartage().setRates(0);
					salary.getValeurRubriquePartage().setBase(0);
					//
					return true;
				}
			}
			else
			{
				montant = salary.getWorkTime().getNbreJourCongesAnnuelPayeNonPris() + salary.getWorkTime().getNbreJoursAbsencePourCongeAnnuel() + salary.getWorkTime().getNbreJourAbsenceCongesAnnuelMoisAnte()
						;//+ salary.getWorkTime().getNbreJourCongesSalaireMoyMoisSuiv();
				salary.getValeurRubriquePartage().setRates(montant);
			}
			//
			// -- Nb jrs trav du mois depuis dernier conges
			if (!ClsObjectUtil.isNull(salary.getInfoSalary().getNbjtr()))
				nt = salary.getInfoSalary().getNbjtr().doubleValue();
			if (nt == 0 && n == 0)
			{
				salary.getValeurRubriquePartage().setAmount(0);
				return true;
			}
			//
			montant = salary.getValeurRubriquePartage().getInter() / (nt + n);
			salary.getValeurRubriquePartage().setBase(montant);
			montant = salary.getValeurRubriquePartage().getBase() * salary.getValeurRubriquePartage().getRates();
			montant = montant - salary.getInfoSalary().getDded().doubleValue() - salary.getWorkTime().getMontantCongePonctuel();
			salary.getValeurRubriquePartage().setAmount(montant);
			//
			salary.getValeurRubriquePartage().setBasePlafonnee(salary.getValeurRubriquePartage().getBase());
		}
		//
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo84(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo84(ClsRubriqueClone rubrique)
	{
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo100(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo85(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo85(ClsRubriqueClone rubrique)
	{
		String cdos = salary.getInfoSalary().getComp_id().getCdos();
		String crub = rubrique.getRubrique().getComp_id().getCrub();
		Double montant = new Double(0);
		double taux = 0;
		String Periode_debut = "";
		double Base_annuelle = 0;
		double Base_mensuelle = 0;
		double Plafond_annuel = 0;
		double Abattement_annuel = 0;
		double Abattement_Mensuel = 0;
		double Impot_deja_paye = 0;
		double Impot_une_part = 0;
		double Impot_un_mois = 0;
		double Impot_Annuel = 0;
		double Cotisation_du_mois = 0;
		int Indice_mois_de_paie = 0;
		byte Code_nationalite = 0;
		boolean Deja_abattement = false;
		List l = null;
		//
		salary.getValeurRubriquePartage().setAmount(0);
		salary.getValeurRubriquePartage().setRates(0);
		//
		Base_annuelle = 0;
		Deja_abattement = false;
		// -- Recuperation du plafond annuel
		Plafond_annuel = convertToNumber(rubrique.getRubrique().getPmax(), rubrique);
		if (salary.getParameter().isPbWithCalulation())
		{
			return false;
		}
		//
		// -- Recuperation de l'abattement annuel
		Abattement_annuel = convertToNumber(rubrique.getRubrique().getAbmx(), rubrique);
		if (salary.getParameter().isPbWithCalulation())
		{
			return false;
		}
		//
		// -- Abattement d'un montant fixe sur la base annuelle
		// -- si elle depasse le plafond
		if (Base_annuelle > Plafond_annuel)
		{
			Base_annuelle = Base_annuelle - Abattement_annuel;
			Deja_abattement = true;
		}
		//
		// -- Calcul de l'indice du mois
		Indice_mois_de_paie = salary.getMyMonthOfPay().getMonth();
		//
		// -- Si il s'agit d'un solde de tous comptes pour un non gabonais,
		// -- le calcul est fait sur 12 mois
		if (salary.getParameter().isStc())
		{
			Code_nationalite = Lecture_Code_nationalite(rubrique).byteValue();
			if (Code_nationalite <= 0)
				return false;
			if (Code_nationalite != 0)
				Indice_mois_de_paie = 12;
		}
		//
		// -- Modif P.A. 24/12/97
		// -- Si le salarie est entre en cours d'exercice et s'il est gabonnais,
		// -- il faut deduire les mois non travailles
		ClsDate oClsDate = new ClsDate(salary.getInfoSalary().getDtes());
		if (oClsDate.getYear() == salary.getMyMonthOfPay().getYear())
		{
			Code_nationalite = Lecture_Code_nationalite(rubrique).byteValue();
			if (Code_nationalite < 0)
				return false;
			if (Code_nationalite == 0)
				Indice_mois_de_paie = Indice_mois_de_paie - oClsDate.getMonth() + 1;
		}
		//
		// -- Calcul base mensuelle : base = base / Mois de paie
		if (Indice_mois_de_paie != 0)
		{
			Base_mensuelle = Base_annuelle / Indice_mois_de_paie;
			Base_mensuelle = arrondi2('I', 1, Base_mensuelle);
		}
		//
		// -- Affectation des valeurs dans pacalc
		salary.getValeurRubriquePartage().setBase(Base_mensuelle);
		salary.getValeurRubriquePartage().setBasePlafonnee(Base_mensuelle);
		//
		// -- Abattement d'un taux si on a pas eu un abattement sur la base annuelle
		if (!Deja_abattement)
		{
			Abattement_Mensuel = Base_mensuelle * rubrique.getRubrique().getPcab().doubleValue() / 100;
			Abattement_Mensuel = arrondi2('I', 1, Abattement_Mensuel);
			Base_mensuelle = Base_mensuelle - Abattement_Mensuel;
		}
		//
		// -- Inutile de continuer si la base imposable est negative
		if (Base_mensuelle <= 0)
			return true;
		//
		// -- Division de la base par le nombre de parts fiscales
		if (ClsObjectUtil.isNull(salary.getInfoSalary().getNbpt()))
		{
			// logger
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90083", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), salary.getInfoSalary().getComp_id().getNmat(),
							rubrique.getRubrique().getComp_id().getCrub()));
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + salary.getParameter().getError();
			return false;
		}
		else
		{
			if (!ClsObjectUtil.isNull(salary.getInfoSalary().getNbpt()))
			{
				Base_mensuelle = Base_mensuelle / salary.getInfoSalary().getNbpt().doubleValue();
				Base_mensuelle = arrondi2('I', 100, Base_mensuelle);
			}
		}
		// END IF;
		//
//		String queryString = " from ElementSalaireBareme " + " where identreprise='" + cdos + "'" + " and crub = '" + crub + "'" + " and val1 <= '" + Base_mensuelle + "'" + " and val2 >= '"
//				+ Base_mensuelle + "'" + " order by cdos , crub , nume";
//		String queryStringRetro = " from Rhthrubbarem " + " where identreprise='" + cdos + "'" + " and crub = '" + crub + "'" + " and aamm = '" + salary.getPeriodOfPay() + "'"
//				+ " and nbul = " + salary.getNbul() + " and val1 <= '" + Base_mensuelle + "'" + " and val2 >= '" + Base_mensuelle + "'"
//				+ " order by cdos , crub , nume";
		String queryString = " from ElementSalaireBareme " + " where identreprise='" + cdos + "'" + " and crub = '" + crub + "' order by cdos , crub , nume";
		String queryStringRetro = " from Rhthrubbarem " + " where identreprise='" + cdos + "'" + " and crub = '" + crub + "'" + " and aamm = '" + salary.getPeriodOfPay() + "'"
		+ " and nbul = " + salary.getNbul() + " order by cdos , crub , nume";
		//
		l = salary.getParameter().isUseRetroactif() ? salary.getService().find(queryStringRetro) : salary.getService().find(queryString);

		//Rhthrubbarem oRhthrubbarem = null;
		ElementSalaireBareme oElementSalaireBareme = null;
		double valeur1 = 0, valeur2 = 0;
		boolean trouve = false;
		if (!ClsObjectUtil.isListEmty(l))
		{
			
			
			for (Object barem : l)
			{
				if (salary.getParameter().isUseRetroactif())
				{
//					oRhthrubbarem = (Rhthrubbarem) barem;
//					if (oRhthrubbarem.getMont() != null)
//						montant = oRhthrubbarem.getMont().doubleValue();
//					if (oRhthrubbarem.getTaux() != null)
//						taux = oRhthrubbarem.getTaux().doubleValue();
//					valeur1 = rubrique.convertToNumber(oRhthrubbarem.getVal1());
//					valeur2 = rubrique.convertToNumber(oRhthrubbarem.getVal2());
				}
				else
				{
					oElementSalaireBareme = (ElementSalaireBareme) barem;
//					if (oElementSalaireBareme.getMont() != null)
//						montantbareme = oElementSalaireBareme.getMont().doubleValue();
//					if (oElementSalaireBareme.getTaux() != null)
//						tauxbareme = oElementSalaireBareme.getTaux().doubleValue();
//					valeur1 = oElementSalaireBareme.getVal1();
//					valeur2 = oElementSalaireBareme.getVal2();
					montant = rubrique.getTauxOuMontantBareme(oElementSalaireBareme, false);
					taux = rubrique.getTauxOuMontantBareme(oElementSalaireBareme, true);
					valeur1 = rubrique.convertToNumber(rubrique.getValeurBareme(oElementSalaireBareme, true));
					valeur2 = rubrique.convertToNumber(rubrique.getValeurBareme(oElementSalaireBareme, false));
				}
				if(valeur1 <= Base_mensuelle && valeur2 >= Base_mensuelle)
				{
					trouve = true;
					break;
				}
			}
			
//			if (salary.getParameter().isUseRetroactif())
//			{
//				oRhthrubbarem = (Rhthrubbarem) l.get(0);
//				montant = oRhthrubbarem.getMont().doubleValue();
//				taux = oRhthrubbarem.getTaux().doubleValue();
//			}
//			else
//			{
//				oElementSalaireBareme = (ElementSalaireBareme) l.get(0);
//				montant = oElementSalaireBareme.getMont().doubleValue();
//				taux = oElementSalaireBareme.getTaux().doubleValue();
//			}
		}
		if(!trouve)
		{
			taux = 0;
			montant = new Double(0);
			String error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + salary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + crub + ". Tranche "
					+ salary.getValeurRubriquePartage().getInter() + " introuvable.";
			salary.getParameter().setError(error);
		}
		//
		// -- Calcul de l'impot pour une part
		Impot_une_part = (Base_mensuelle * taux / 100) - montant;
		//
		// -- Calcul de l'impot total pour un mois
		Impot_un_mois = Impot_une_part * salary.getInfoSalary().getNbpt().doubleValue();
		Impot_un_mois = arrondi2('I', 10, Impot_un_mois);
		//
		// -- Calcul de l'impot total annuel
		Impot_Annuel = Impot_un_mois * Indice_mois_de_paie;
		//
		// -- Calcul du montant deja paye pour l'IRPP
		// -- Le numero de la rubrique de totalisation de l'IRPP par mois
		// -- est stocke dans la zone rcon de la rubrique.
		Impot_deja_paye = 0;
		if (Indice_mois_de_paie != 1)
		{
			Periode_debut = String.valueOf(salary.getMyMonthOfPay().getYear()) + "01";
			if (salary.getParameter().isUseRetroactif())
			{
				l = salary.getService().find(
						"select sum(mont) from Rhtprcumu" + " where identreprise='" + cdos + "'" + " and nmat = '" + salary.getInfoSalary().getComp_id().getNmat() + "'"
								+ " and aamm >= '" + Periode_debut + "'" + " and aamm < '" + salary.getPeriodOfPay() + "'" + " and rubq = '" + rubrique.getRubrique().getRcon()
								+ "'" + " and nbul != 0");
				if (!ClsObjectUtil.isListEmty(l))
					Impot_deja_paye = (Double) l.get(0);
			}
			else
			{
				l = salary.getService().find(
						"select sum(mont) from CumulPaie" + " where identreprise='" + cdos + "'" + " and nmat = '" + salary.getInfoSalary().getComp_id().getNmat() + "'"
								+ " and aamm >= '" + Periode_debut + "'" + " and aamm < '" + salary.getPeriodOfPay() + "'" + " and rubq = '" + rubrique.getRubrique().getRcon()
								+ "'" + " and nbul != 0");
				if (!ClsObjectUtil.isListEmty(l) &&  l.get(0) != null)
					Impot_deja_paye = (Double) l.get(0);
			}
		}
		Cotisation_du_mois = Impot_Annuel - Impot_deja_paye;
		//
		// -- Affectation des valeurs dans pacalc
		salary.getValeurRubriquePartage().setRates(0);
		salary.getValeurRubriquePartage().setAmount(Cotisation_du_mois);
		//
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo101(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo86(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo86(ClsRubriqueClone rubrique)
	{
		// String cdos = salary.getInfoSalary().getComp_id().getCdos();
		// String crub = rubrique.getRubrique().getComp_id().getCrub();
		Double montant = new Double(0);
		int nombre_de_mois = 0;
		Date Date_Debut = null;
		// Date_Fin DATE;
		Date Date_Fin = null;
		//
		//
		salary.getValeurRubriquePartage().setAmount(0);
		salary.getValeurRubriquePartage().setRates(0);
		//
		// -- Si aucun jour de conges, il est inutile de continuer
		salary.getValeurRubriquePartage().setRates(salary.getWorkTime().getNbreJourCongesSalaireMoyMois() + salary.getWorkTime().getNbreJourPayeSupplPayeNonPris());
		if (salary.getValeurRubriquePartage().getRates() == 0)
		{
			salary.getValeurRubriquePartage().setBase(0);
			salary.getValeurRubriquePartage().setRates(0);
			salary.getValeurRubriquePartage().setAmount(0);
			//
			return true;
		}
		// -------------------- CALCUL DE LA BASE ------------------------------------------
		// -- La base de calcul est egale a la base du mois plus les droits aux conges memorises
//		if (ClsObjectUtil.isNull(salary.getInfoSalary().getDapa()))
//			salary.getInfoSalary().setDapa(new BigDecimal(0));
//		if (ClsObjectUtil.isNull(salary.getInfoSalary().getDapec()))
//			salary.getInfoSalary().setDapec(new BigDecimal(0));
		salary.getValeurRubriquePartage().setBase(salary.getValeurRubriquePartage().getBase() + salary.getInfoSalary().getDapa().doubleValue() + salary.getInfoSalary().getDapec().doubleValue());
		// -- Si la base est nulle, il est inutile de continuer
		if (salary.getValeurRubriquePartage().getBase() == 0)
		{
			salary.getValeurRubriquePartage().setBase(0);
			salary.getValeurRubriquePartage().setRates(0);
			salary.getValeurRubriquePartage().setAmount(0);
			//
			return true;
		}
		//
		String error = "";
		// -- Verification si date de retour conges annuels renseignee
		if (ClsObjectUtil.isNull(salary.getInfoSalary().getDrtcg()))
		{
			// logger
			error = "Sal " + salary.getInfoSalary().getComp_id().getNmat() + ". Dernier retour conges non renseignee.  Verifier fiche salarie.";
			salary.getParameter().setError(error);
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + salary.getParameter().getError();
			return false;
		}
		if (ClsObjectUtil.isNull(salary.getInfoSalary().getDtes()))
		{
			// logger
			error = "Sal " + salary.getInfoSalary().getComp_id().getNmat() + ". Dernier entree societe non renseignee.  Verifier fiche salarie.";
			salary.getParameter().setError(error);
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + salary.getParameter().getError();
			return false;
		}
		// -- Verification de la coherence de la date de retour conges annuels
		if (salary.getInfoSalary().getDrtcg().getTime() < new ClsDate("01/01/1990", "dd/MM/yyyy").getDate().getTime())
		{
			// logger
			error = "Sal " + salary.getInfoSalary().getComp_id().getNmat() + ". Dernier retour conges=." + salary.getInfoSalary().getDrtcg() + "  Verifier fiche salarie.";
			salary.getParameter().setError(error);
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + salary.getParameter().getError();
			return false;
		}
		//
		// -- Calcul du nombre de mois ecoule depuis le dernier conges
		//
		// -- Determination de la date de debut
		Date_Debut = ClsObjectUtil.isNull(salary.getInfoSalary().getDrtcg()) ? ClsDate.getFirstDayOfMonth(salary.getInfoSalary().getDtes()) : ClsDate.getFirstDayOfMonth(salary.getInfoSalary()
				.getDrtcg());
		//
		// -- Determination de la date de fin
		Date_Fin = ClsObjectUtil.isNull(salary.getInfoSalary().getPmcf()) ? ClsDate.getLastDayOfMonth(new ClsDate(salary.getPeriodOfPay() + "01", "yyyyMMdd").getDate()) : ClsDate
				.getLastDayOfMonth(new ClsDate(salary.getInfoSalary().getPmcf() + "01", "yyyyMMdd").getDate());
		// -- Calcul du nombre de mois ecoule depuis le dernier conges
		nombre_de_mois = Double.valueOf(ClsDate.getMonthsBetweenIncludingDays(Date_Debut,Date_Fin)).intValue() + 1;
		// -- Calcul de la moyenne, affectation a la base
		if (nombre_de_mois > 0)
		{
			salary.getValeurRubriquePartage().setBase(salary.getValeurRubriquePartage().getBase() / nombre_de_mois);
			montant = arrondi2('I', 1, salary.getValeurRubriquePartage().getBase());
			salary.getValeurRubriquePartage().setBase(montant);
		}
		else
		{
			// logger
			error = "Sal: " + salary.getInfoSalary().getComp_id().getNmat() + ". Algo 86 : Pb calcul nombre de mois (NULL ou zero)";
			salary.getParameter().setError(error);
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + salary.getParameter().getError();
			return false;
		}
		// --------------------- Application d'un algo31 ------------------------------------------
		//
		if (salary.getWorkTime().getNbreJourCongesSalaireMoyMois() == 0)
			salary.getWorkTime().setNbreJourCongesSalaireMoyMois(salary.getWorkTime().getNbreJoursAbsencePourCongeAnnuel());
		//
		if (ClsObjectUtil.isNull(salary.getInfoSalary().getJapa()))
			salary.getInfoSalary().setJapa(new BigDecimal(0));
		if (ClsObjectUtil.isNull(salary.getInfoSalary().getJapec()))
			salary.getInfoSalary().setJapec(new BigDecimal(0));
		//
		if (salary.getWorkTime().getNbreJourCongesSalaireMoyMois() == 0 && salary.getWorkTime().getNbreJourPayeSupplPayeNonPris() == 0)
		{
			salary.getValeurRubriquePartage().setBase(0);
			salary.getValeurRubriquePartage().setRates(0);
			salary.getValeurRubriquePartage().setAmount(0);
			//
			return true;
		}
		//
		double i = salary.getWorkTime().getNbreJourCongesSalaireMoyMois() + salary.getWorkTime().getNbreJourPayeSupplPayeNonPris();
		salary.getValeurRubriquePartage().setRates(i);
		montant = salary.getValeurRubriquePartage().getBase() * salary.getValeurRubriquePartage().getRates() / 30;
		salary.getValeurRubriquePartage().setAmount(montant);
		salary.getValeurRubriquePartage().setBasePlafonnee(salary.getValeurRubriquePartage().getBase());
		//
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo87(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo87(ClsRubriqueClone rubrique)
	{
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo87";
		String cdos = salary.getInfoSalary().getComp_id().getCdos();
		String crub = rubrique.getRubrique().getComp_id().getCrub();
		Double montant = new Double(0);
		Double taux = new Double(0);
		String Periode_debut = "";
		double Base_mensuelle = 0;
		double Base_annuelle = 0;
		double Impot_deja_paye = 0;
		double Impot_un_mois = 0;
		double Impot_Annuel = 0;
		double Cotisation_du_mois = 0;
		int Indice_mois_de_paie = 0;
		int Code_nationalite = 0;
		int Nombre_Lignes = 0;
		double Borne_inferieure = 0;
		double Borne_superieure = 0;
		double Base_tranche = 0;
		double Montant_tranche = 0;
		//
		// String queryString = " from ElementSalaireBareme "
		// + " where identreprise='" + cdos + "'"
		// + " and crub = '" + crub + "'"
		// + " order by cdos , crub , nume";
		// String queryStringRetro = " from Rhthrubbarem "
		// + " where identreprise='" + cdos + "'"
		// + " and crub = '" + crub + "'"
		// + " and aamm = '" + salary.getPeriodOfPay() + "'"
		// + " and nbul = " + salary.getNbul()
		// + " order by cdos , crub , nume";
		//
		salary.getValeurRubriquePartage().setRates(0);
		salary.getValeurRubriquePartage().setAmount(0);
		// -- Chargement de la base annuelle avec la base calculee
		Base_annuelle = salary.getValeurRubriquePartage().getBase();
		//
		// -- Calcul de l'indice du mois
		Indice_mois_de_paie = salary.getMyMonthOfPay().getMonth();
		//
		// -- Si il s'agit d'un solde de tous comptes pour un non gabonais,
		// -- le calcul est fait sur 12 mois
		if (salary.getParameter().isStc())
		{
			Code_nationalite = 0;
			Code_nationalite = Lecture_Code_nationalite(rubrique).intValue();
			if (Code_nationalite == 0)
				return false;
			if (Code_nationalite != 0)
				Indice_mois_de_paie = 12;
		}
		// -- Si le salarie est entre en cours d'exercice il faut deduire les mois
		// -- non travailles pour les gabonais
		ClsDate oDtes = new ClsDate(salary.getInfoSalary().getDtes());
		if (oDtes.getYear() == salary.getMyMonthOfPay().getYear())
		{
			Code_nationalite = Lecture_Code_nationalite(rubrique).intValue();
			if (Code_nationalite == 0)
				Indice_mois_de_paie = Indice_mois_de_paie - oDtes.getMonth() + 1;
		}
		// -- Calcul base mensuelle : base = base / Mois de paie
		if (Indice_mois_de_paie != 0)
			Base_mensuelle = Base_annuelle / Indice_mois_de_paie;
		Base_mensuelle = arrondi2('I', 1, Base_mensuelle);
		//
		// -- Inutile de continuer si la base imposable est negative
		if (Base_mensuelle <= 0)
			return true;
		// -- Calcul de l'impot total pour un mois
		Nombre_Lignes = 0;
		Impot_un_mois = 0;
		String valeur1 = "";
		String valeur2 = "";
		int nbreBaremeRubrique = 0;
		double dblValeur1 = 0;
		double dblValeur2 = 0;
		//Rhthrubbarem oRhthrubbarem = null;
		ElementSalaireBareme oElementSalaireBareme = null;
		// List listOfBarem = salary.getParameter().isUseRetroactif() ? salary.getService().find(queryStringRetro) : salary.getService().find(queryString);
		// @emmanuel: add to not call multiple times the same query
		String keyOfBaremeList = salary.getParameter().isUseRetroactif() ? crub + this.salary.getPeriodOfPay() + this.salary.getNbul() : crub;
		List listOfBarem = (List) salary.getParameter().getListOfRubriquebaremeMap().get(keyOfBaremeList);
		if (listOfBarem != null)
			for (Object barem : listOfBarem)
			{
				if (salary.getParameter().isUseRetroactif())
				{
//					oRhthrubbarem = (Rhthrubbarem) barem;
//					montant = oRhthrubbarem.getMont().doubleValue();
//					taux = oRhthrubbarem.getTaux().doubleValue();
//					valeur1 = oRhthrubbarem.getVal1();
//					valeur2 = oRhthrubbarem.getVal2();
				}
				else
				{
					oElementSalaireBareme = (ElementSalaireBareme) barem;
//					montant = oElementSalaireBareme.getMont().doubleValue();
//					taux = oElementSalaireBareme.getTaux().doubleValue();
//					valeur1 = oElementSalaireBareme.getVal1();
//					valeur2 = oElementSalaireBareme.getVal2();
					montant = rubrique.getTauxOuMontantBareme(oElementSalaireBareme, false);
					taux = rubrique.getTauxOuMontantBareme(oElementSalaireBareme, true);
					valeur1 = rubrique.getValeurBareme(oElementSalaireBareme, true);
					valeur2 = rubrique.getValeurBareme(oElementSalaireBareme, false);
				}
				//
				nbreBaremeRubrique++;
				if (nbreBaremeRubrique > 50)
				{
					nbreBaremeRubrique = 50;
					break;
				}
				Borne_inferieure = rubrique.convertToNumber(valeur1);// convertToNumber(valeur1, rubrique);
				Borne_superieure = rubrique.convertToNumber(valeur2);// convertToNumber(valeur2, rubrique);
				//
//				if (!ClsObjectUtil.isNull(valeur1) && !ClsObjectUtil.isNull(valeur2))
//				{
//					dblValeur1 = rubrique.convertToNumber(valeur1);
//					dblValeur2 = rubrique.convertToNumber(valeur2);
//					if (salary.getValeurRubriquePartage().getInter() <= dblValeur2)
//						break;
//					//
//					salary.getValeurRubriquePartage().setValeur((dblValeur2 - dblValeur1) * taux);
//					salary.getValeurRubriquePartage().setAmount(salary.getValeurRubriquePartage().getAmount() + salary.getValeurRubriquePartage().getValeur());
//				}
				if (Base_mensuelle >= Borne_inferieure)
				{
					if (Base_mensuelle <= Borne_superieure)
						Base_tranche = Base_mensuelle - Borne_inferieure;
					else
						Base_tranche = Borne_superieure - Borne_inferieure;
					// -- Application du taux
					Montant_tranche = Base_tranche * taux / 100;
					Montant_tranche = arrondi2('N', 1, Montant_tranche);
					//
					// -- Calcul du montant de l'impot
					Impot_un_mois = Impot_un_mois + Montant_tranche;
				}
				else
					break;
			}// foreach
		String error = "";
		if (Nombre_Lignes == 0)
		{
			error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + salary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + crub + ". Il manque le bareme, ecran 6";
			salary.getParameter().setError(error);
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + salary.getParameter().getError();
			return false;
		}
		else if (Nombre_Lignes == 50)
		{
			error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + salary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + crub + ". Il manque le bareme, ecran 6";
			salary.getParameter().setError(error);
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + salary.getParameter().getError();
			return false;
		}
		// -- Calcul de l'impot total annuel
		Impot_Annuel = Impot_un_mois * Indice_mois_de_paie;
		//
		// -- Calcul du montant deja paye pour la TCS
		// -- Le numero de la rubrique de totalisation de la TCS par mois
		// -- est stocke dans la zone rcon de la rubrique.
		Impot_deja_paye = 0;
		if (Indice_mois_de_paie != 1)
		{
			Periode_debut = String.valueOf(salary.getMyMonthOfPay().getYear()).concat("01");
			List l = null;
			if (salary.getParameter().isUseRetroactif())
			{
				l = salary.getService().find(
						"select sum(mont) from Rhtprcumu" + " where identreprise='" + cdos + "'" + " and nmat = '" + salary.getInfoSalary().getComp_id().getNmat() + "'"
								+ " and aamm >= '" + Periode_debut + "'" + " and aamm < '" + salary.getPeriodOfPay() + "'" + " and rubq = '" + rubrique.getRubrique().getRcon()
								+ "'" + " and nbul != 0");
			}
			else
			{
				l = salary.getService().find(
						"select sum(mont) from CumulPaie" + " where identreprise='" + cdos + "'" + " and nmat = '" + salary.getInfoSalary().getComp_id().getNmat() + "'"
								+ " and aamm >= '" + Periode_debut + "'" + " and aamm < '" + salary.getPeriodOfPay() + "'" + " and rubq = '" + rubrique.getRubrique().getRcon()
								+ "'" + " and nbul != 0");
			}
			if (!ClsObjectUtil.isListEmty(l))
				Impot_deja_paye = (Double) (l.get(0) == null ? 0 : l.get(0));
		}
		// -- Calcul de la cotisation de ce mois
		// Cotisation_du_mois := Impot_annuel - NVL(Impot_deja_paye, 0);
		Cotisation_du_mois = Impot_Annuel - Impot_deja_paye;
		//
		// -- Affectation des zones dans pacalc
		if (Indice_mois_de_paie != 0)
		{
			salary.getValeurRubriquePartage().setBase(Base_annuelle / Indice_mois_de_paie);
			montant = arrondi2('I', 1, Base_annuelle / Indice_mois_de_paie);
			salary.getValeurRubriquePartage().setBase(montant);
		}
		salary.getValeurRubriquePartage().setBasePlafonnee(Base_mensuelle);
		salary.getValeurRubriquePartage().setRates(0);
		salary.getValeurRubriquePartage().setAmount(Cotisation_du_mois);
		//
		return true;
	}
	
	public boolean algo89(ClsRubriqueClone rubrique)
	{
		String cdos = salary.getInfoSalary().getComp_id().getCdos();
		String nmat = salary.getInfoSalary().getComp_id().getNmat();
		// --Recuperation du nombre de mois restant

		Integer w_nb_mois_restant;
		String w_formule;
		String w_formule2;
		double w_abattement;
		String w_aamm1;
		String w_aamm2;
		String w_aamm99;
		String w_rubq = StringUtils.EMPTY;
		double w_mont;
		double w_montant_bonus;
		double w_montant_bonus_mois;
		double w_montant_13mois;
		double w_montant_provision = 0;
		double w_result;
		double w_montant_deja_provisionne;

		w_nb_mois_restant = 12 - Integer.valueOf(StringUtil.oraSubstring(salary.moisPaieCourant, 5, 6)) + 1;
		w_aamm99 = StringUtil.oraSubstring(salary.moisPaieCourant, 1, 4) + "99";

		// --Recuperation de la formule de calcul Bonus

		String libelle = this.getLabelFromNomenclature(cdos, 99, "CALC-ACPTE", 1, salary.getNlot(), salary.getNbul(), salary.getMoisPaieCourant(), salary.getPeriodOfPay(), rubrique.getRubrique().getComp_id().getCrub());
		if (ClsObjectUtil.isNull(libelle))
		{
			libelle = "Rub. " + rubrique.getRubrique().getComp_id().getCrub() + " Algo. " + rubrique.getRubrique().getAlgo();
			libelle += " Pb Calcul Provision. Libelle 1 en table 99 sur clé CALC-ACPTE inexistant";
			salary.getParameter().setError(salary.getParameter().errorMessage(libelle, salary.getParameter().getLangue()));
			return false;
		}

		w_formule = libelle;

		// --Recuperation de la formule de calcul 13 mois
		libelle = this.getLabelFromNomenclature(cdos, 99, "CALC-ACPTE", 2, salary.getNlot(), salary.getNbul(), salary.getMoisPaieCourant(), salary.getPeriodOfPay(), rubrique.getRubrique().getComp_id().getCrub());
		if (ClsObjectUtil.isNull(libelle))
		{
			libelle = "Rub. " + rubrique.getRubrique().getComp_id().getCrub() + " Algo. " + rubrique.getRubrique().getAlgo();
			libelle += " Pb Calcul Provision. Libelle 2 en table 99 sur clé CALC-ACPTE inexistant";
			salary.getParameter().setError(salary.getParameter().errorMessage(libelle, salary.getParameter().getLangue()));
			return false;
		}
		w_formule2 = libelle;

		// --Recuperation abattement
		tempNumber = this.salary.getUtilNomenclature().getAmountOrRateFromT99(salary.getParameter().getListOfTable99Map(), this.salary.getInfoSalary().getComp_id().getCdos(), 99, "CALC-ACPTE", 1,
				this.salary.getNlot(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.AMOUNT);
		if (tempNumber == null)
		{
			libelle = "Rub. " + rubrique.getRubrique().getComp_id().getCrub() + " Algo. " + rubrique.getRubrique().getAlgo();
			libelle += " Pb Calcul Provision. Montant 1 en table 99 sur clé CALC-ACPTE inexistant";
			salary.getParameter().setError(salary.getParameter().errorMessage(libelle, salary.getParameter().getLangue()));
			return false;
		}
		w_abattement = tempNumber.doubleValue();

		// --Calcul du cumul provision/impot depuis le debut de l'annee
		w_montant_bonus = 0;
		String C_PACUMU = "SELECT SUM(NVL(mont,0)) FROM CumulPaie WHERE cdos = :cdos AND nmat = :nmat AND nbul > 0 ";
		C_PACUMU += " AND aamm =  :aamm99 AND rubq = :rubq ";
		Session session = salary.parameter.service.getSession();
		String key = null;
		Query q = null;
		List lst = null;
		try
		{
			for (int i = 1; i <= 30; i++)
			{
				if (i % 5 == 1)
				{
					w_rubq = StringUtil.oraSubstring(w_formule, i, 4);
					if (StringUtils.isBlank(StringUtil.oraLTrim(w_rubq)))
						break;
					w_mont = 0;
					key = w_rubq+w_aamm99;
					if(salary.mapAlgo89.containsKey(key))
						w_mont = salary.mapAlgo89.get(key) != null ?salary.mapAlgo89.get(key).doubleValue() : 0;
					else
					{
						q = session.createSQLQuery(C_PACUMU);
						q.setParameter("cdos", cdos);
						q.setParameter("nmat", nmat);
						q.setParameter("aamm99", w_aamm99);
						q.setParameter("rubq", w_rubq);
						lst = q.list();
						if (!lst.isEmpty() && lst.get(0) != null)
							w_mont = new BigDecimal(lst.get(0).toString()).doubleValue();
						salary.mapAlgo89.put(key, w_mont);
					}

					if (StringUtils.equals(StringUtil.oraSubstring(w_formule, i + ParameterUtil.longueurRubrique, 1), "+"))
						w_montant_bonus = w_montant_bonus + w_mont;
					else if (StringUtils.equals(StringUtil.oraSubstring(w_formule, i + ParameterUtil.longueurRubrique, 1), "-"))
						w_montant_bonus = w_montant_bonus - w_mont;
				}
			}
			
			//--Ajout de ce qui a ete percu sur le mois en cours
			String C_PACALC = "SELECT SUM(NVL(mont,0)) FROM CalculPaie WHERE cdos = :cdos AND nmat = :nmat AND nbul = :nbul ";
			C_PACALC += " AND aamm =  :aamm AND rubq = :rubq ";
			   w_montant_bonus_mois = 0;
			   for (int i = 1; i <= 30; i++)
				{
					if (i % 5 == 1)
					{
						w_rubq = StringUtil.oraSubstring(w_formule, i, 4);
						if (StringUtils.isBlank(StringUtil.oraLTrim(w_rubq)))
							break;
						w_mont = 0;

						q = session.createSQLQuery(C_PACALC);
						q.setParameter("cdos", cdos);
						q.setParameter("nmat", nmat);
						q.setParameter("nbul", salary.getNbul());
						q.setParameter("aamm", salary.getMoisPaieCourant());
						q.setParameter("rubq", w_rubq);
						lst = q.list();
						if (!lst.isEmpty() && lst.get(0) != null)
							w_mont = new BigDecimal(lst.get(0).toString()).doubleValue();

						if (StringUtils.equals(StringUtil.oraSubstring(w_formule, i + ParameterUtil.longueurRubrique, 1), "+"))
							w_montant_bonus_mois = w_montant_bonus_mois + w_mont;
						else if (StringUtils.equals(StringUtil.oraSubstring(w_formule, i + ParameterUtil.longueurRubrique, 1), "-"))
							w_montant_bonus_mois = w_montant_bonus_mois - w_mont;
					}
				}
			   
			  // --Ajout du 13eme mois
			   w_montant_13mois = 0;
			   for (int i = 1; i <= 30; i++)
				{
					if (i % 5 == 1)
					{
						w_rubq = StringUtil.oraSubstring(w_formule2, i, 4);
						if (StringUtils.isBlank(StringUtil.oraLTrim(w_rubq)))
							break;
						w_mont = 0;

						q = session.createSQLQuery(C_PACALC);
						q.setParameter("cdos", cdos);
						q.setParameter("nmat", nmat);
						q.setParameter("nbul", salary.getNbul());
						q.setParameter("aamm", salary.getMoisPaieCourant());
						q.setParameter("rubq", w_rubq);
						lst = q.list();
						if (!lst.isEmpty() && lst.get(0) != null)
							w_mont = new BigDecimal(lst.get(0).toString()).doubleValue();

						if (StringUtils.equals(StringUtil.oraSubstring(w_formule, i + ParameterUtil.longueurRubrique, 1), "+"))
							w_montant_13mois = w_montant_13mois + w_mont;
						else if (StringUtils.equals(StringUtil.oraSubstring(w_formule, i + ParameterUtil.longueurRubrique, 1), "-"))
							w_montant_13mois = w_montant_13mois - w_mont;
					}
				}
			   
			   w_montant_bonus =w_montant_bonus+w_montant_bonus_mois+w_montant_13mois;

				  //--On applique l'abattement
				  if(w_montant_bonus > w_abattement)
				    w_montant_bonus = w_montant_bonus - w_abattement;
				  else
				   w_montant_bonus = 0;
				  
				  salary.getValeurRubriquePartage().setBase(w_montant_bonus);
				  salary.getValeurRubriquePartage().setBasePlafonnee(w_montant_bonus);
				  
				 // --Calcul du montant deja provisionne
				  w_montant_deja_provisionne = 0;
				  w_rubq = "2030";
				  //par Yannick
				  w_rubq = rubrique.getRubrique().getComp_id().getCrub();
				  key = w_rubq+w_aamm99;
				  if(salary.mapAlgo89.containsKey(key))
					  w_montant_deja_provisionne = salary.mapAlgo89.get(key) != null ?salary.mapAlgo89.get(key).doubleValue() : 0;
				  else
				 {
					q = session.createSQLQuery(C_PACUMU);
					q.setParameter("cdos", cdos);
					q.setParameter("nmat", nmat);
					q.setParameter("aamm99", w_aamm99);
					q.setParameter("rubq", w_rubq);
					lst = q.list();
					if (!lst.isEmpty() && lst.get(0) != null)
						w_montant_deja_provisionne = new BigDecimal(lst.get(0).toString()).doubleValue();
					salary.mapAlgo89.put(key,w_montant_deja_provisionne);
				 }
					
					
					if(w_montant_bonus > 0)
					   w_montant_bonus = w_montant_bonus - w_montant_deja_provisionne;

					  if(w_montant_bonus > 0)
					   w_montant_provision = w_montant_bonus / w_nb_mois_restant;
					  

					  salary.getValeurRubriquePartage().setRates(w_nb_mois_restant);
					  salary.getValeurRubriquePartage().setAmount(w_montant_provision);
				  
		}
		catch (Exception e)
		{
			e.printStackTrace();
			libelle = ClsTreater._getStackTrace(e);
			salary.getParameter().setError(salary.getParameter().errorMessage(libelle, salary.getParameter().getLangue()));
			return false;
		}
		finally
		{
			salary.parameter.service.closeSession(session);
		}

		return true;
	}

//***************** Algo ajoutés par JOSEPH pour les SABC **********************
	
	public boolean algo120(ClsRubriqueClone rubrique)
	{
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo120";
		
		double nbjr_principaux = 0;
		double nbjr_supp = 0;
		double nbjr_reliquat= 0;
		double nbjr_total =0;
		
		double nbjc_par_mois = 2;
		
		double nbj_total_mois = 30;
		
		long nlig = 3;
		
		if(rubrique.getRubrique().getNutm()!= 0)
		nlig = rubrique.getRubrique().getNutm();
		
		Number	taux = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,this.salary.getInfoSalary().getComp_id().getCdos(), 35, this.salary.getInfoSalary().getCat(), nlig, this.salary.getNlot(),
					this.salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.RATES, rubrique.getRubrique().getComp_id().getCrub());

		Number	montant = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,this.salary.getInfoSalary().getComp_id().getCdos(), 35, this.salary.getInfoSalary().getCat(), 4, this.salary.getNlot(),
				this.salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.AMOUNT, rubrique.getRubrique().getComp_id().getCrub());

		
		 if(taux != null && taux.doubleValue() != 0)nbjc_par_mois = taux.doubleValue();
		
		 if(montant != null && montant.doubleValue() != 0)nbj_total_mois = montant.doubleValue();
			
		ClsDate oDrtcg = new ClsDate(salary.getInfoSalary().getDrtcg() != null ? salary.getInfoSalary().getDrtcg() : salary.getInfoSalary().getDtes());

		double nbjr_presence = ClsDate.getNomberOfDaysBetween(oDrtcg.getDate(), salary.getParameter().getFirstDayOfMonth());

		
		double nbjr_travail = nbj_total_mois- salary.getWorkTime().getNbreJourConges();
		
		nbjr_presence += nbjr_travail; // nombre de jour total de présence accumulé jusqu'é la fin du mois de paie
		
        double nbAns = Math.floor(nbjr_presence / 365.25);
        double nbMois = Math.floor((nbjr_presence % 365.25) / 30.4375);
        double nbJours = Math.floor(((nbjr_presence % 365.25) % 30.4375));
		
        nbMois += 12 * nbAns;
        
        nbjr_principaux = Math.floor((nbjc_par_mois * nbMois) + (nbjc_par_mois * nbJours / nbj_total_mois) );
        
		nbjr_supp = salary.calculDuNBJSUPP(nbjr_principaux);
		
		nbjr_reliquat = salary.calculDuNBJRELIQUAT();
		
		System.out.println("::DEBUG----- JOS::  DECOMPTE CONGE NON PRIS:==> NBR PRINC="+nbjr_principaux+" NBR SUPP="+nbjr_supp+" NBR RELIQ="+nbjr_reliquat);
		
		nbjr_total = nbjr_principaux + nbjr_supp + nbjr_reliquat ;
		salary.getValeurRubriquePartage().setRates(0);
		salary.getValeurRubriquePartage().setAmount(nbjr_total);
		
		return true;
	}	
	
	
	
	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo121(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo121(ClsRubriqueClone rubrique)
	{
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo121";
		
		String cdos = salary.getInfoSalary().getComp_id().getCdos();
		String crub = rubrique.getRubrique().getComp_id().getCrub();
		double montant = 0;
		double taux = 0;
		double base = 0;
		String cleAccess = "";
		int tabl = 0;
		String libelle = "";
		// double w4 = 0;
		//
		salary.getValeurRubriquePartage().setRates(0);
		salary.getValeurRubriquePartage().setAmount(0);
		salary.getValeurRubriquePartage().setValeur(0);
		if (rubrique.getRubrique().getTabl() != null)
		{
			cleAccess = crub;
			tabl = Integer.valueOf(rubrique.getRubrique().getTabl());
			libelle = this.getLabelFromNomenclature(cdos, tabl, cleAccess, 3, salary.getNlot(), salary.getNbul(), this.salary.getMoisPaieCourant(),
					this.salary.getPeriodOfPay(), rubrique.getRubrique().getComp_id().getCrub());
		}
		if (ClsObjectUtil.isNull(libelle))
		{
			// logger
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90056", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat(), cleAccess, rubrique.getRubrique().getTabl()));
			return false;
		}
		tempNumber = null;
		if (rubrique.getRubrique().getTabl() != null)
		{
			tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,salary.getInfoSalary().getComp_id().getCdos(), tabl, cleAccess, 1, salary.getNlot(), salary.getNbul(),
					salary.getMoisPaieCourant(), salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.AMOUNT, rubrique.getRubrique().getComp_id().getCrub());
		}
		if (tempNumber == null)
		{
			// logger
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90056", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat(), cleAccess, rubrique.getRubrique().getTabl()));
			return false;
		}
		
		if (tempNumber.intValue() == 0)
		{
			// logger
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90065", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat(), cleAccess, rubrique.getRubrique().getTabl()));
			return false;
		}
		montant = tempNumber.doubleValue();
		char char1 = libelle.toCharArray()[0];
	/*	if (char1 != 'B' && char1 != 'T' && char1 != 'M')
		{
			// logger
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90066", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat(), crub, rubrique.getRubrique().getTabl()));
			return false;
		}*/
		// prendre les valeurs de taux, montant et base associées é la rubrique correspondant au libelle
		// ClsRubriqueClone rubriqueTmp = salary.findRubriqueClone(String.valueOf(libelle));
		String newCrub = ClsStringUtil.formatNumber(montant, ParameterUtil.formatRubrique);
		ClsRubriqueClone rubriqueTmp = salary.findRubriqueClone(newCrub);

		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "rubrique trouvée = " + rubriqueTmp;
/*		if (char1 == 'B')
			salary.getValeurRubriquePartage().setRates(rubriqueTmp.getBasePlafonnee());
		else if (char1 == 'T')
			salary.getValeurRubriquePartage().setRates(rubriqueTmp.getRates());
		else if (char1 == 'M')
			salary.getValeurRubriquePartage().setRates(rubriqueTmp.getAmount());
*/
		salary.getValeurRubriquePartage().setRates(rubriqueTmp.getAmount());
		//
		if (salary.getValeurRubriquePartage().getRates() > salary.getValeurRubriquePartage().getBase())
		
			salary.getValeurRubriquePartage().setAmount(salary.getValeurRubriquePartage().getRates());
			
		else if (salary.getValeurRubriquePartage().getRates() <= salary.getValeurRubriquePartage().getBase()) 
				
			salary.getValeurRubriquePartage().setAmount(salary.getValeurRubriquePartage().getBase());
		
		else
			{
				// logger
				salary.getParameter().setError(
						salary.getParameter().errorMessage("ERR-90058", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), salary.getInfoSalary().getComp_id().getNmat(),
								rubrique.getRubrique().getComp_id().getCrub()));
				if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + salary.getParameter().getError();
				return false;
			}
		
		return true;
	}
	
	public boolean algo122(ClsRubriqueClone rubrique)
	{
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo122";
		
		double taux = 1 ;		
		Date date_entree= this.salary.getInfoSalary().getDtes();
		Date date_mois_paie = this.salary.getLastDayOfMonth();
		
		ClsDate dt = new ClsDate(date_entree);
		double nbr_mois = Math.floor(dt.getNomberOfDaysBetween(date_entree, date_mois_paie) / 30);

		if(nbr_mois < 48)
			taux = nbr_mois / 48;
		
		double result = salary.getValeurRubriquePartage().getBase()* taux;
		salary.getValeurRubriquePartage().setRates(taux);
		salary.getValeurRubriquePartage().setAmount(result);
		
		return true;
	}	
	
	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo123(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo123(ClsRubriqueClone rubrique)
	{
		
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo123";
			
//		if (this.salary.getAnciennete() == 0)
//		{
//			salary.getValeurRubriquePartage().setRates(0);
//			salary.getValeurRubriquePartage().setAmount(0);
//			return true;
//		} 
		String accesKey = ClsStringUtil.formatNumber(this.salary.getAnciennete(), "00");
		accesKey= accesKey.concat(salary.getInfoSalary().getCat());
		
		if('O' == salary.getParameter().getGenfile()) outputtext += "Anciennete||Categorie du salarié = " + accesKey;

		System.out.println("ALGO 123 rub = "+rubrique.getRubrique().getComp_id().getCrub()+"  AccesKey = " + accesKey);
		
		String RateOrAmount = rubrique.getRubrique().getToum();
		
		ClsEnumeration.EnColumnToRead colToRead = ("T".equals(RateOrAmount) || "D".equals(RateOrAmount) )? ClsEnumeration.EnColumnToRead.RATES : ClsEnumeration.EnColumnToRead.AMOUNT;
		double amountOrRateValue = 0;
		int tabl = 0;
		double val = -1;
		tempNumber = null;
		if (rubrique.getRubrique().getTabl() != null)
		{
			tabl = Integer.valueOf(rubrique.getRubrique().getTabl());
			tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,this.salary.getInfoSalary().getComp_id().getCdos(), tabl, accesKey,
					rubrique.getRubrique().getNutm(), this.salary.getNlot(), this.salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), colToRead, rubrique.getRubrique().getComp_id().getCrub());
		}
		if (tempNumber == null)
		{
			salary.getParameter().setPbWithCalulation(true);
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90056", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat(), accesKey, rubrique.getRubrique().getTabl()));
			return false;
		}
		amountOrRateValue = tempNumber.doubleValue();
		salary.getValeurRubriquePartage().setValeur(amountOrRateValue);
		if ("M".equals(rubrique.getRubrique().getToum()))
		{
			salary.getValeurRubriquePartage().setRates(0);
			salary.getValeurRubriquePartage().setAmount(amountOrRateValue);
		}
		else if ("T".equals(rubrique.getRubrique().getToum()))
		{
			salary.getValeurRubriquePartage().setRates(amountOrRateValue);
			val = salary.getValeurRubriquePartage().getBase() * salary.getValeurRubriquePartage().getRates() / 100;
			salary.getValeurRubriquePartage().setAmount(val);
		}
		else if ("D".equals(rubrique.getRubrique().getToum()))
		{
			salary.getValeurRubriquePartage().setRates(amountOrRateValue);

				if(amountOrRateValue == 0)
					salary.getValeurRubriquePartage().setAmount(0);
				else
				{
					val = salary.getValeurRubriquePartage().getBase() / salary.getValeurRubriquePartage().getRates();
					salary.getValeurRubriquePartage().setAmount(val);
				}
			
			
		}
		else
		{
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90058", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), salary.getInfoSalary().getComp_id().getNmat(),
							rubrique.getRubrique().getComp_id().getCrub()));
			return false;
		}
		return true;
		
		
	}	
	

	
	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo124(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo124(ClsRubriqueClone rubrique)
	{
		
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo124";
			
		if (this.salary.getAgeOfAgent() < 18)
		{
			salary.getValeurRubriquePartage().setRates(0);
			salary.getValeurRubriquePartage().setAmount(0);
			return true;
		}
		String accesKey = ClsStringUtil.formatNumber(this.salary.getAgeOfAgent()<=60 ?this.salary.getAgeOfAgent():60, "00");
//		accesKey= accesKey.concat(salary.getInfoSalary().getCat());
		System.out.println("ALGO 124 rub = "+rubrique.getRubrique().getComp_id().getCrub()+"  AccesKey = " + accesKey);
		
		if('O' == salary.getParameter().getGenfile()) outputtext += "Age du salarié = " + accesKey;

		
		String RateOrAmount = rubrique.getRubrique().getToum();
		
		ClsEnumeration.EnColumnToRead colToRead = ("T".equals(RateOrAmount) || "D".equals(RateOrAmount) )? ClsEnumeration.EnColumnToRead.RATES : ClsEnumeration.EnColumnToRead.AMOUNT;
		double amountOrRateValue = 0;
		int tabl = 0;
		double val = -1;
		tempNumber = null;
		if (rubrique.getRubrique().getTabl() != null)
		{
			tabl = Integer.valueOf(rubrique.getRubrique().getTabl());
			tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,this.salary.getInfoSalary().getComp_id().getCdos(), tabl, accesKey,
					rubrique.getRubrique().getNutm(), this.salary.getNlot(), this.salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), colToRead, rubrique.getRubrique().getComp_id().getCrub());
		}
		if (tempNumber == null)
		{
			salary.getParameter().setPbWithCalulation(true);
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90056", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat(), accesKey, rubrique.getRubrique().getTabl()));
			return false;
		}
		amountOrRateValue = tempNumber.doubleValue();
		salary.getValeurRubriquePartage().setValeur(amountOrRateValue);
		if ("M".equals(rubrique.getRubrique().getToum()))
		{
			salary.getValeurRubriquePartage().setRates(0);
			salary.getValeurRubriquePartage().setAmount(amountOrRateValue);
		}
		else if ("T".equals(rubrique.getRubrique().getToum()))
		{
			salary.getValeurRubriquePartage().setRates(amountOrRateValue);
			val = salary.getValeurRubriquePartage().getBase() * salary.getValeurRubriquePartage().getRates() / 100;
			salary.getValeurRubriquePartage().setAmount(val);
		}
		else if ("D".equals(rubrique.getRubrique().getToum()))
		{
			salary.getValeurRubriquePartage().setRates(amountOrRateValue);
			System.out.println("ALGO 124 rub = "+rubrique.getRubrique().getComp_id().getCrub()+"  on divise la base par = " + amountOrRateValue);
				if(amountOrRateValue == 0)
					salary.getValeurRubriquePartage().setAmount(0);
				else
				{
					val = salary.getValeurRubriquePartage().getBase() / salary.getValeurRubriquePartage().getRates();
					salary.getValeurRubriquePartage().setAmount(val);
				}
			
			
		}
		else
		{
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90058", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), salary.getInfoSalary().getComp_id().getNmat(),
							rubrique.getRubrique().getComp_id().getCrub()));
			return false;
		}
		return true;
		
		
	}		

	
	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo125(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo125(ClsRubriqueClone rubrique)
	{
		
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo125";
		
//		if (this.salary.getAnciennete() == 0)
//		{
//			salary.getValeurRubriquePartage().setRates(0);
//			salary.getValeurRubriquePartage().setAmount(0);
//			return true;
//		}
		String accesKey = ClsStringUtil.formatNumber(this.salary.getAnciennete(), "00");
		
		accesKey= accesKey.concat(ClsStringUtil.formatNumber(this.salary.getAgeOfAgent()<=60 ?this.salary.getAgeOfAgent():60, "00"));
		
		if('O' == salary.getParameter().getGenfile()) outputtext += "Anciennete||Age du salarié = " + accesKey;

		
		String RateOrAmount = rubrique.getRubrique().getToum();
		
		System.out.println("ALGO 125 rub = "+rubrique.getRubrique().getComp_id().getCrub()+"  AccesKey = " + accesKey);
		
		ClsEnumeration.EnColumnToRead colToRead = ("T".equals(RateOrAmount) || "D".equals(RateOrAmount) )? ClsEnumeration.EnColumnToRead.RATES : ClsEnumeration.EnColumnToRead.AMOUNT;
		double amountOrRateValue = 0;
		int tabl = 0;
		double val = -1;
		tempNumber = null;
		if (rubrique.getRubrique().getTabl() != null)
		{
			tabl = Integer.valueOf(rubrique.getRubrique().getTabl());
			tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,this.salary.getInfoSalary().getComp_id().getCdos(), tabl, accesKey,
					rubrique.getRubrique().getNutm(), this.salary.getNlot(), this.salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), colToRead, rubrique.getRubrique().getComp_id().getCrub());
		}
		if (tempNumber == null)
		{
			salary.getParameter().setPbWithCalulation(true);
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90056", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat(), accesKey, rubrique.getRubrique().getTabl()));
			return false;
		}
		amountOrRateValue = tempNumber.doubleValue();
		salary.getValeurRubriquePartage().setValeur(amountOrRateValue);
		if ("M".equals(rubrique.getRubrique().getToum()))
		{
			salary.getValeurRubriquePartage().setRates(0);
			salary.getValeurRubriquePartage().setAmount(amountOrRateValue);
		}
		else if ("T".equals(rubrique.getRubrique().getToum()))
		{
			salary.getValeurRubriquePartage().setRates(amountOrRateValue);
			val = salary.getValeurRubriquePartage().getBase() * salary.getValeurRubriquePartage().getRates() / 100;
			salary.getValeurRubriquePartage().setAmount(val);
		}
		else if ("D".equals(rubrique.getRubrique().getToum()))
		{
			salary.getValeurRubriquePartage().setRates(amountOrRateValue);
			System.out.println("ALGO 125 rub = "+rubrique.getRubrique().getComp_id().getCrub()+"  on divise la base par = " + amountOrRateValue);
				if(amountOrRateValue == 0)
					salary.getValeurRubriquePartage().setAmount(0);
				else
				{
					val = salary.getValeurRubriquePartage().getBase() / salary.getValeurRubriquePartage().getRates();
					salary.getValeurRubriquePartage().setAmount(val);
				}
						
		}
		else
		{
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90058", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), salary.getInfoSalary().getComp_id().getNmat(),
							rubrique.getRubrique().getComp_id().getCrub()));
			return false;
		}
		return true;
		
		
	}	
	
	
	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo126(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo126(ClsRubriqueClone rubrique)
	{
		
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo126";
			

		String accesKey = salary.getMonthOfPay().substring(4,6);
		
//		accesKey= accesKey.concat(ClsStringUtil.formatNumber(this.salary.getAgeOfAgent(), "00"));
		
		if('O' == salary.getParameter().getGenfile()) outputtext += "Mois de Paie du salarié = " + accesKey;
		System.out.println("ALGO 126 rub = "+rubrique.getRubrique().getComp_id().getCrub()+"  AccesKey = " + accesKey);
		
		String RateOrAmount = rubrique.getRubrique().getToum();
		
		ClsEnumeration.EnColumnToRead colToRead = ("T".equals(RateOrAmount) || "D".equals(RateOrAmount) ) ? ClsEnumeration.EnColumnToRead.RATES : ClsEnumeration.EnColumnToRead.AMOUNT;
		double amountOrRateValue = 0;
		int tabl = 0;
		double val = -1;
		tempNumber = null;
		if (rubrique.getRubrique().getTabl() != null)
		{
			tabl = Integer.valueOf(rubrique.getRubrique().getTabl());
			tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,this.salary.getInfoSalary().getComp_id().getCdos(), tabl, accesKey,
					rubrique.getRubrique().getNutm(), this.salary.getNlot(), this.salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), colToRead, rubrique.getRubrique().getComp_id().getCrub());
		}
		if (tempNumber == null)
		{
			salary.getParameter().setPbWithCalulation(true);
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90056", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat(), accesKey, rubrique.getRubrique().getTabl()));
			return false;
		}
		amountOrRateValue = tempNumber.doubleValue();
		salary.getValeurRubriquePartage().setValeur(amountOrRateValue);
		if ("M".equals(rubrique.getRubrique().getToum()))
		{
			salary.getValeurRubriquePartage().setRates(0);
			salary.getValeurRubriquePartage().setAmount(amountOrRateValue);
		}
		else if ("T".equals(rubrique.getRubrique().getToum()))
		{
			salary.getValeurRubriquePartage().setRates(amountOrRateValue);
			val = salary.getValeurRubriquePartage().getBase() * salary.getValeurRubriquePartage().getRates() / 100;
			salary.getValeurRubriquePartage().setAmount(val);
		}
		else if ("D".equals(rubrique.getRubrique().getToum()))
		{
			salary.getValeurRubriquePartage().setRates(amountOrRateValue);
			System.out.println("ALGO 126 rub = "+rubrique.getRubrique().getComp_id().getCrub()+"  on divise la base par = " + amountOrRateValue);
				if(amountOrRateValue == 0)
					salary.getValeurRubriquePartage().setAmount(0);
				else
				{
					val = salary.getValeurRubriquePartage().getBase() / salary.getValeurRubriquePartage().getRates();
					salary.getValeurRubriquePartage().setAmount(val);
				}
						
		}
		else
		{
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90058", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), salary.getInfoSalary().getComp_id().getNmat(),
							rubrique.getRubrique().getComp_id().getCrub()));
			return false;
		}
		return true;
		
		
	}		
	
	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo127(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo127(ClsRubriqueClone rubrique)
	{
		
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo126";
			
		
		String accesKey = this.salary.infoSalary.getCat();
		
//		accesKey= accesKey.concat(ClsStringUtil.formatNumber(this.salary.getAgeOfAgent(), "00"));
		System.out.println("ALGO 127 rub = "+rubrique.getRubrique().getComp_id().getCrub()+"  AccesKey = " + accesKey);
		
		if('O' == salary.getParameter().getGenfile()) outputtext += "Mois de Paie du salarié = " + accesKey;

		
		String RateOrAmount = rubrique.getRubrique().getToum();
		
		ClsEnumeration.EnColumnToRead colToRead = ("T".equals(RateOrAmount) || "D".equals(RateOrAmount) ) ? ClsEnumeration.EnColumnToRead.RATES : ClsEnumeration.EnColumnToRead.AMOUNT;
		double amountOrRateValue = 0;
		int tabl = 0;
		double val = -1;
		tempNumber = null;
		if (rubrique.getRubrique().getTabl() != null)
		{
			tabl = Integer.valueOf(rubrique.getRubrique().getTabl());
			tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,this.salary.getInfoSalary().getComp_id().getCdos(), tabl, accesKey,
					rubrique.getRubrique().getNutm(), this.salary.getNlot(), this.salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), colToRead, rubrique.getRubrique().getComp_id().getCrub());
		}
		if (tempNumber == null)
		{
			salary.getParameter().setPbWithCalulation(true);
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90056", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat(), accesKey, rubrique.getRubrique().getTabl()));
			return false;
		}
		amountOrRateValue = tempNumber.doubleValue();
		salary.getValeurRubriquePartage().setValeur(amountOrRateValue);
		if ("M".equals(rubrique.getRubrique().getToum()))
		{
			salary.getValeurRubriquePartage().setRates(0);
			salary.getValeurRubriquePartage().setAmount(amountOrRateValue);
		}
		else if ("T".equals(rubrique.getRubrique().getToum()))
		{
			salary.getValeurRubriquePartage().setRates(amountOrRateValue);
			val = salary.getValeurRubriquePartage().getBase() * salary.getValeurRubriquePartage().getRates() / 100;
			salary.getValeurRubriquePartage().setAmount(val);
		}
		else if ("D".equals(rubrique.getRubrique().getToum()))
		{
			salary.getValeurRubriquePartage().setRates(amountOrRateValue);
			System.out.println("ALGO 127 rub = "+rubrique.getRubrique().getComp_id().getCrub()+"  on divise la base par = " + amountOrRateValue);
				if(amountOrRateValue == 0)
					salary.getValeurRubriquePartage().setAmount(0);
				else
				{
					val = salary.getValeurRubriquePartage().getBase() / salary.getValeurRubriquePartage().getRates();
					salary.getValeurRubriquePartage().setAmount(val);
				}
						
		}
		else
		{
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90058", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), salary.getInfoSalary().getComp_id().getNmat(),
							rubrique.getRubrique().getComp_id().getCrub()));
			return false;
		}
		return true;
		
		
	}	

	
	
	public boolean algo128(ClsRubriqueClone rubrique)
	{
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo122";
		
		double taux = 1 ;		
		Date date_entree= this.salary.getInfoSalary().getDtes();
		Date date_radiation = this.salary.getInfoSalary().getDmrr();
		if(date_radiation == null){
			salary.getValeurRubriquePartage().setRates(0);
			salary.getValeurRubriquePartage().setAmount(0);		
			return true;
		}
		ClsDate dt = new ClsDate(date_entree);
		double nbr_jrs_total = Math.floor(dt.getNomberOfDaysBetween(date_entree, date_radiation));

        double nbAns = Math.floor(nbr_jrs_total / 365.25);
        double nbMois = Math.floor((nbr_jrs_total % 365.25) / 30.4375);
        double nbJours = Math.floor(((nbr_jrs_total % 365.25) % 30.4375));

		double result = (nbMois * 30) + nbJours;
		salary.getValeurRubriquePartage().setRates(nbr_jrs_total);
		salary.getValeurRubriquePartage().setAmount(result);
		
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo4(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo129(ClsRubriqueClone rubrique)
	{
		
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>algo4";
		

		
		
//		if (this.salary.getAnciennete() == 0)
//		{
//			salary.getValeurRubriquePartage().setRates(0);
//			salary.getValeurRubriquePartage().setAmount(0);
//			return true;
//		}
		
		
		int nbjr_presence = ClsDate.getNomberOfDaysBetween(salary.getInfoSalary().getDtes(), salary.getParameter().getLastDayOfMonth());
		double anciennete= Math.ceil(nbjr_presence/365.25);
		String formattedMonth = ClsStringUtil.formatNumber(anciennete, "00");
		if('O' == salary.getParameter().getGenfile()) outputtext += "Anciennete du salarié = " + formattedMonth;
		String RateOrAmount = rubrique.getRubrique().getToum();
		ClsEnumeration.EnColumnToRead colToRead = "T".equals(RateOrAmount) ? ClsEnumeration.EnColumnToRead.RATES : ClsEnumeration.EnColumnToRead.AMOUNT;
		double amountOrRateValue = 0;
		int tabl = 0;
		double val = -1;
		tempNumber = null;
		if (rubrique.getRubrique().getTabl() != null)
		{
			tabl = Integer.valueOf(rubrique.getRubrique().getTabl());
			tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,this.salary.getInfoSalary().getComp_id().getCdos(), tabl, formattedMonth,
					rubrique.getRubrique().getNutm(), this.salary.getNlot(), this.salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), colToRead, rubrique.getRubrique().getComp_id().getCrub());
		}
		if (tempNumber == null)
		{
			salary.getParameter().setPbWithCalulation(true);
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90056", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat(), formattedMonth, rubrique.getRubrique().getTabl()));
			return false;
		}
		amountOrRateValue = tempNumber.doubleValue();
		salary.getValeurRubriquePartage().setValeur(amountOrRateValue);
		if ("M".equals(rubrique.getRubrique().getToum()))
		{
			salary.getValeurRubriquePartage().setRates(0);
			salary.getValeurRubriquePartage().setAmount(amountOrRateValue);
		}
		else if ("T".equals(rubrique.getRubrique().getToum()))
		{
			salary.getValeurRubriquePartage().setRates(amountOrRateValue);
			val = salary.getValeurRubriquePartage().getBase() * salary.getValeurRubriquePartage().getRates() / 100;
			salary.getValeurRubriquePartage().setAmount(val);
		}
		else if ("D".equals(rubrique.getRubrique().getToum()))
		{
			salary.getValeurRubriquePartage().setRates(amountOrRateValue);
			val = salary.getValeurRubriquePartage().getBase() * salary.getValeurRubriquePartage().getRates();
			salary.getValeurRubriquePartage().setAmount(val);
			// spécifique edm
//			if (StringUtils.equals(rubrique.getSalary().getParameter().nomClient, ClsEntreprise.EDM))
//			{
//				if(amountOrRateValue == 0)
//					salary.getValeurRubriquePartage().setAmount(0);
//				else
//				{
//					val = salary.getValeurRubriquePartage().getBase() / salary.getValeurRubriquePartage().getRates();
//					salary.getValeurRubriquePartage().setAmount(val);
//				}
//			}
			
		}
		else
		{
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90058", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), salary.getInfoSalary().getComp_id().getNmat(),
							rubrique.getRubrique().getComp_id().getCrub()));
			return false;
		}
		return true;
		
		
	}	
	
	
	//***************** FIN ***  Algo ajoutés par JOSEPH pour les SABC **********************	

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#arrondi2(char, double, double)
	 */
	public double arrondi2(char p_type, double p_arro, double p_mon)
	{
		if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>arrondi2";
		// if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "..........p_arro:" + p_arro);
		if (p_arro == 0)
			return p_mon;
		double int_mon = 0;
		Double w_decarro = p_mon / p_arro;
		double dec_mon = 0;
		if (p_type == 'I')
		{
			int_mon = w_decarro.intValue();
			dec_mon = int_mon;
		}
		else if (p_type == 'S')
		{
			int_mon = w_decarro.intValue() + 1;
			dec_mon = int_mon;
		}
		else
			dec_mon = w_decarro;
		p_mon = dec_mon * p_arro;
		//
		return p_mon;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#calculPlafond(java.lang.String, java.lang.String)
	 */
	public double calculPlafond(String char16, String rubrique)
	{
		// if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>calculPlafond");
		// -- ----- Definitions
		int coeff = -1;
		double w_plaf = 0;
		double w_plaft = 0;
		double w_plafm = 0;
		
		Number w_plaftNumber = null;
		Number w_plafmNumber = null;
		
		String char2 = "";
		if (!ClsObjectUtil.isNull(char16) && char16.length() >= 2)
			char2 = char16.substring(0, 1);
		//
		// -- Fonction
		
		w_plafmNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,salary.getInfoSalary().getComp_id().getCdos(), 96, char2, 1, salary.getNlot(), salary.getNbul(),
				salary.getMoisPaieCourant(), salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.AMOUNT, rubrique);
		w_plaftNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,salary.getInfoSalary().getComp_id().getCdos(), 96, char2, 1, salary.getNlot(), salary.getNbul(),
				salary.getMoisPaieCourant(), salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.RATES, rubrique);
		if (w_plafmNumber != null)
		{
			w_plafm = w_plafmNumber.doubleValue();
			w_plaft = w_plaftNumber == null ? 0 : w_plaftNumber.doubleValue();
			if (!ClsObjectUtil.isNull(char16) && char16.length() >= 4)
			{
				coeff = Integer.valueOf(char16.substring(2, 3));
			}
			if (w_plafm == 0)
				w_plaf = w_plaft * coeff;
			else
				w_plaf = w_plafm * coeff;
		}
		else
		{
			w_plaf = 0;
			salary.getParameter().setError(salary.getParameter().errorMessage("ERR-90081", salary.getParameter().getLangue(), rubrique, char2, salary.getInfoSalary().getComp_id().getNmat()));
			salary.getParameter().setPbWithCalulation(true);
			return 0;
		}
		return w_plaf;
	}


	public String concat43(int key, String rubrique)
	{
		Integer moisDonnee =  this.salary.parameter.moisDonnees.get(rubrique);
		//ClsInfoSalaryClone info =  salary.getInfoSalary();
		ClsInfoSalaryClone info =  getInfoOfMonth(moisDonnee);
		int w_cle = key;
		String char16 = "";
		boolean valuedefault = false;
		if(info == null)
			return char16;


		if (w_cle >= 801 && w_cle <= 899)
			char16 = salary.rechercheZoneLibre(w_cle - 800);
		else
		{
			switch (w_cle)
			{
			case 1:
				char16 = info.getNiv1();
				break;
			case 2:
				char16 = info.getNiv2();
				break;
			case 3:
				char16 = info.getNiv3();
				break;
			case 4:
				char16 = info.getComp_id().getNmat();
				break;
			case 11:
				char16 = info.getSexe();
				break;
			case 19:
				char16 = new ClsDate(info.getDtna(), "yyyy-MM-dd").getFormatedDate('/');
				break;
			case 21:
				char16 = info.getNato();
				break;
			case 22:
				char16 = info.getSitf();
				break;
			case 23:
				char16 = String.valueOf(info.getNbcj());
				break;
			case 24:
				if (info.getNbec() == null)
					info.setNbec(0);
				char16 = ClsStringUtil.formatNumber(info.getNbec(), "00");
				break;
			case 25:
				if (info.getNbfe() == null)
					info.setNbfe(0);
				char16 = ClsStringUtil.formatNumber(info.getNbfe(), "00");
				break;
			case 26:
				if (info.getNbpt() == null)
					info.setNbpt(new BigDecimal(0));
				char16 = ClsStringUtil.formatNumber(info.getNbpt(), "00.00");
				break;
			case 27:
				char16 = info.getModp();
				break;
			case 34:
				char16 = info.getVild();
				break;
			case 39:
				char16 = info.getCat();
				break;
			case 40:
				char16 = info.getEch();
				break;
			case 43:
				char16 = info.getGrad();
				break;
			case 44:
				char16 = info.getFonc();
				break;
			case 45:
				char16 = info.getAfec();
				break;
			case 46:
				char16 = info.getCodf();
				break;
			case 47:
				ClsStringUtil.formatNumber(info.getIndi(), "000");
				break;
			case 48:
				char16 = info.getCtat();
				break;
			case 58:
				char16 = info.getHifo();
				break;
			case 59:
				char16 = info.getZli1();
				break;
			case 60:
				char16 = info.getZli2();
				break;
			case 61:
				char16 = new ClsDate(info.getDtes(), "yyyy-MM-dd").getFormatedDate('/');
				break;
			case 63:
				char16 = new ClsDate(info.getDtit(), "yyyy-MM-dd").getFormatedDate('/');
				break;
			case 64:
				char16 = new ClsDate(info.getDdca(), "yyyy-MM-dd").getFormatedDate('/');
				break;
			case 65:
				char16 = new ClsDate(info.getDepr(), "yyyy-MM-dd").getFormatedDate('/');
				break;
			case 67:
				char16 = info.getTypc();
				break;
			case 70:
				char16 = info.getRegi();
				break;
			case 71:
				char16 = info.getZres();
				break;
			case 72:
				char16 = info.getDmst();
				break;
			default:
				valuedefault = true;
				break;
			}
		}
		if (valuedefault)
		{
			// logger
			salary.getParameter().setError(salary.getParameter().errorMessage("ERR-90082", salary.getParameter().getLangue(), rubrique, key, info.getComp_id().getNmat()));
			salary.getParameter().setPbWithCalulation(true);
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + salary.getParameter().getError();
			return "";
		}
		return char16;
	}

	
	private ClsInfoSalaryClone getInfoOfMonth(Integer moisDonnee)
	{
		String aamm = this.salary.getMonthOfPay();
		String aamm1 = aamm;
		if(moisDonnee != null)
			aamm1 = new ClsDate(new ClsDate(aamm1,"yyyyMM").addMonth(-moisDonnee)).getYearAndMonth();
		if(StringUtils.equalsIgnoreCase(aamm1, aamm)) return salary.infoSalary;
		String nmat = salary.infoSalary.getComp_id().getNmat();
		String cdos = salary.infoSalary.getComp_id().getCdos();
		String cle = aamm1+"-"+nmat;
		if(infos.containsKey(cle)) return infos.get(cle);
//		Rhthagent ag = (Rhthagent)salary.service.get(Rhthagent.class, new RhthagentPK(cdos,nmat,aamm1,9));
//		if(ag != null)
//		{
//			Rhpagent agent =new Rhpagent();
//			RhpagentPK pk =new RhpagentPK();
//			BeanUtils.copyProperties(ag, agent);
//			BeanUtils.copyProperties(ag.getComp_id(), pk);
//			agent.setComp_id(pk);
//			infos.put(cle, new ClsInfoSalaryClone(agent));
//			return infos.get(cle);
//		}
		return null;
	}
	
	private Concateneur _getValFromCle(int cle, String rubrique)
	{
		Integer moisDonnee =  this.salary.parameter.moisDonnees.get(rubrique);
		//ClsInfoSalaryClone info =  salary.getInfoSalary();
		ClsInfoSalaryClone info =  getInfoOfMonth(moisDonnee);
		String char30 = StringUtils.EMPTY;
		boolean valuedefault = false;
		Concateneur result = new Concateneur();
		salary.getParameter().setPbWithCalulation(false);
		if(info == null)
		{
			result.valdefaut = valuedefault;
			result.valeur = char30;
			return result;
		}
		
		//
		if (cle >= 801 && cle <= 899)
		{
			char30 = salary.rechercheZoneLibre(cle - 800);
		}
		else
		{
			switch (cle)
			{
			case 1:
				char30 = this.tempOraSubstring(info.getNiv1(), 1, 3);
				break;
			case 2:
				char30 = this.tempOraSubstring(info.getNiv2(), 1, 3); 
				break;
			case 3:
				char30 = this.tempOraSubstring(info.getNiv3(), 1,8);
				break;
			case 4:
				char30 = this.tempOraSubstring(info.getComp_id().getNmat(), 1,6);
				break;
			case 7:
				char30 = this.tempOraSubstring(info.getClas(), 1,2);
				break;
			case 8:
				char30 = this.tempOraSubstring(info.getEqui(), 1,8); 
				break;
			case 11:
				char30 = this.tempOraSubstring(info.getSexe(), 1,1); 
				break;
			case 20:
				char30 =  this.tempOraSubstring(info.getPnai(), 1,3);
				break;
			case 21:
				char30 =  this.tempOraSubstring(info.getNato(), 1,3);
				break;
			case 22:
				char30 =  this.tempOraSubstring(info.getSitf(), 1,1);
				break;
			case 23:
				char30 = ClsStringUtil.formatNumber(info.getNbcj(),"00");
				break;
			case 142:
				char30 = ClsStringUtil.formatNumber(info.getNbef(),"00");
				break;
			case 24:
				char30 = ClsStringUtil.formatNumber(info.getNbec(),"00");
				break;
			case 25:
				char30 = ClsStringUtil.formatNumber(info.getNbfe(),"00");
				break;
			case 26:
				// spécifique cnss
//				if (StringUtils.equals(salary.getParameter().nomClient, ClsEntreprise.CNSS))
//				{
//					char30 =  String.valueOf(info.getNbpt());
//				}
//				else
					char30 = ClsStringUtil.formatNumber(info.getNbpt(),"00");
					
				break;
			case 27:
				char30 =  this.tempOraSubstring(info.getModp(), 1,1);
				break;
			case 34:
				char30 =  this.tempOraSubstring(info.getVild(), 1,10);
				break;
			case 35:
				char30 =  this.tempOraSubstring(info.getComm(), 1,10);
				break;
			case 36:
				char30 =  this.tempOraSubstring(info.getPbpe(), 1,1);
				break;
			case 39:
				char30 = this.tempOraSubstring(info.getCat(), 1,10); 
				break;
			case 40:
				char30 =  this.tempOraSubstring(info.getEch(), 1,3);
				break;
			case 43:
				char30 =  this.tempOraSubstring(info.getGrad(), 1,3);
				break;
			case 44:
				char30 =  this.tempOraSubstring(info.getFonc(), 1,4);
				break;
			case 45:
				char30 =  this.tempOraSubstring(info.getAfec(), 1,3);
				break;
			case 46:
				char30 =  this.tempOraSubstring(info.getCodf(), 1,3);
				break;
			case 47:
				char30 = ClsStringUtil.formatNumber(info.getIndi(),"000");
				break;
			case 48:
				char30 =  this.tempOraSubstring(info.getCtat(), 1,2);
				break;
			case 50:
				char30 =  this.tempOraSubstring(info.getSynd(), 1,1);
				break;
			case 51:
				char30 =  this.tempOraSubstring(info.getDels(), 1,1);
				break;
			case 52:
				char30 =  this.tempOraSubstring(info.getTits(), 1,1);
				break;
			case 58:
				char30 =  this.tempOraSubstring(info.getHifo(), 1,2);
				break;
			case 59:
				char30 =  this.tempOraSubstring(info.getZli1(), 1,10);
				break;
			case 60:
				char30 =  this.tempOraSubstring(info.getZli2(), 1,10);
				break;
			case 67:
				char30 =  this.tempOraSubstring(info.getTypc(), 1,1);
				break;
			case 70:
				char30 =  this.tempOraSubstring(info.getRegi(), 1,1);
				break;
			case 71:
				char30 =  this.tempOraSubstring(info.getZres(), 1,1);
				break;
			case 72:
				char30 =  this.tempOraSubstring(info.getDmst(), 1,1);
				break;
			case 73:
				char30 = String.valueOf(info.getNpie());
				break;
			case 300:
				char30 = ClsStringUtil.formatNumber(salary.getAgeOfAgent(),"00");
				break;
			case 301:
				char30 = ClsStringUtil.formatNumber(salary.getAnciennete(),"00");
				break;
			case 302:
				char30 = ClsStringUtil.formatNumber(new ClsDate(salary.parameter.getMonthOfPay(),"yyyyMM").getMonth(),"00");
				break;
			case 303:
				char30 = info.getDchf() != null ? new ClsDate(info.getDchf()).getDateS(salary.parameter.appDateFormat):StringUtils.EMPTY;
				break;
			case 304:
				char30 = new ClsDate(salary.parameter.getMonthOfPay(),"yyyyMM").getDateS(salary.parameter.appDateFormat);
				break;
			case 305:
				Date prec = new ClsDate(salary.parameter.getMonthOfPay(),"yyyyMM").addMonth(-1);
				char30 = new ClsDate(prec).getDateS(salary.parameter.appDateFormat);
				break;
			case 306:
				Date prec1 = new ClsDate(salary.parameter.getMonthOfPay(),"yyyyMM").addMonth(-2);
				char30 = new ClsDate(prec1).getDateS(salary.parameter.appDateFormat);
				break;
			//les champs de type date 'DTES','DDCA','DMRR','DTIT','DEPR','DECC','DCHG','DFES','DRTCG','DCHF',DTNA
			case 19:
				char30 = info.getDtna() != null ? new ClsDate(info.getDtna()).getDateS(salary.parameter.appDateFormat):StringUtils.EMPTY;
				break;
			case 61:
				char30 = info.getDtes() != null ? new ClsDate(info.getDtes()).getDateS(salary.parameter.appDateFormat):StringUtils.EMPTY;
				break;
			case 64:
				char30 = info.getDdca() != null ? new ClsDate(info.getDdca()).getDateS(salary.parameter.appDateFormat):StringUtils.EMPTY;
				break;
			case 81:
				char30 = info.getDmrr() != null ? new ClsDate(info.getDmrr()).getDateS(salary.parameter.appDateFormat):StringUtils.EMPTY;
				break;
			case 63:
				char30 = info.getDtit() != null ? new ClsDate(info.getDtit()).getDateS(salary.parameter.appDateFormat):StringUtils.EMPTY;
				break;
			case 65:
				char30 = info.getDepr() != null ? new ClsDate(info.getDepr()).getDateS(salary.parameter.appDateFormat):StringUtils.EMPTY;
				break;
			case 68:
				char30 = info.getDecc() != null ? new ClsDate(info.getDecc()).getDateS(salary.parameter.appDateFormat):StringUtils.EMPTY;
				break;
			case 41:
				char30 = info.getDchg() != null ? new ClsDate(info.getDchg()).getDateS(salary.parameter.appDateFormat):StringUtils.EMPTY;
				break;
			case 62:
				char30 = info.getDfes() != null ? new ClsDate(info.getDfes()).getDateS(salary.parameter.appDateFormat):StringUtils.EMPTY;
				break;
			case 105:
				char30 = info.getDrtcg() != null ? new ClsDate(info.getDrtcg()).getDateS(salary.parameter.appDateFormat):StringUtils.EMPTY;
				break;
			case 107:
				char30 = info.getDdenv() != null ?new ClsDate(info.getDdenv(), salary.parameter.appDateFormat).getDateS():StringUtils.EMPTY;
				break;
			case 108:
				char30 = info.getDrenv() != null ? new ClsDate(info.getDrenv(), salary.parameter.appDateFormat).getDateS():StringUtils.EMPTY;
				break;

			default:
				valuedefault = true;
				break;
			}
		}
		result.valdefaut = valuedefault;
		result.valeur = char30;
		return result;
	}
	
	private String tempOraSubstring(String info, int debut, int fin)
	{
		if(1==1) return info;
		return StringUtil.oraSubstring(info, debut,fin);
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#concatene(int, int, java.lang.String)
	 */
	public String concatene(int cle1, int cle2, String rubrique)
	{
		// if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>concatene");
		// if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "............cle1 :" +cle1);
		// if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "............cle2 :" +cle2);
		Concateneur concat = this._getValFromCle(cle1, rubrique);
		if (concat.valdefaut)
		{
			// logger
			salary.getParameter().setError(
					salary.getParameter()
							.errorMessage("ERR-90082", salary.getParameter().getLangue(), rubrique, ClsStringUtil.formatNumber(cle1, "000"), salary.getInfoSalary().getComp_id().getNmat()));
			salary.getParameter().setPbWithCalulation(true);
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "----Return on error clé 1 = " + concat.valeur;
			return concat.valeur;
		}
		String char10 = StringUtil.oraLTrim(StringUtil.oraRTrim(concat.valeur != null ?concat.valeur:StringUtils.EMPTY, " "), " ");
		// ajout yannick
		concat = this._getValFromCle(cle2, rubrique);
		String char20 = StringUtils.EMPTY;
		if(! concat.valdefaut)
			char20 = StringUtil.oraLTrim(concat.valeur != null ?concat.valeur:StringUtils.EMPTY," ");
		
		 //return char10 + StringUtil.oraSubstring(char20, 1, 10 - char20.length());
		return char10 + char20;	
	}
	
	public String concateneCle1(int cle,String rubrique)
	{
		Concateneur concat = this._getValFromCle(cle, rubrique);
		if (concat.valdefaut)
		{
			// logger
			salary.getParameter().setError(
					salary.getParameter()
							.errorMessage("ERR-90082", salary.getParameter().getLangue(), rubrique, ClsStringUtil.formatNumber(cle, "000"), salary.getInfoSalary().getComp_id().getNmat()));
			salary.getParameter().setPbWithCalulation(true);
			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "----Return on error clé 1 = " + concat.valeur;
			return concat.valeur;
		}
		String char10 = StringUtil.oraLTrim(StringUtil.oraRTrim(concat.valeur != null ?concat.valeur:StringUtils.EMPTY, " "), " ");
		
		
		return char10;	
	}
	
	public String concateneCle2(int cle,String rubrique)
	{
		Concateneur concat = this._getValFromCle(cle, rubrique);
		String char20 = StringUtils.EMPTY;
		if(! concat.valdefaut)
			char20 = StringUtil.oraLTrim(concat.valeur != null ?concat.valeur:StringUtils.EMPTY," ");
		return char20;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#convertToNumber(java.lang.String, com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public double convertToNumber(String char16, ClsRubriqueClone rubrique)
	{
		// if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + ">>convertToNumber");
		// if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + "............char16 :" + char16);
		double w_nombre = 0;
		if (ClsObjectUtil.isNull(char16))
			return 0;
		try
		{
			Character ch = char16.toCharArray()[0];
			if (!Character.isLetter(ch))
			{
				return Double.valueOf(char16);
			}
			Character ch1 = char16.toCharArray()[0];
			Character ch2 = char16.toCharArray()[1];
			if (ch1 == 'R' && Character.isDigit(ch2))
			{
				w_nombre = rubrique.getAmount();
				return w_nombre;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		w_nombre = calculPlafond(char16, rubrique.getRubrique().getComp_id().getCrub());
		return w_nombre;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#convertToNumber37(java.lang.String, double)
	 */
	public double convertToNumber37(String char16, double lvalnum)
	{
		double w_nombre = 0;
		double lplaf = 0;
		int coeff = 0;
		try
		{
			if (!ClsObjectUtil.isNull(char16))
			{
				if (Character.isDigit(char16.toCharArray()[0]))
				{
					try
					{
						w_nombre = Integer.parseInt(char16);
					}
					catch (Exception e)
					{
						salary.getParameter().setError(salary.getParameter().errorMessage("ERR-90079", salary.getParameter().getLangue(), salary.getInfoSalary().getComp_id().getNmat()));
						salary.getParameter().setPbWithCalulation(true);
						return 0;
					}
					return w_nombre;
				}
				else if (Character.isLetter(char16.toCharArray()[0]))
				{
					tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,salary.getInfoSalary().getComp_id().getCdos(), 96, char16.substring(0, 1 + 1), 1, salary.getNlot(),
							salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.AMOUNT, null);
					if(tempNumber  != null)
					{
						lplaf = tempNumber.doubleValue();
						coeff = Integer.valueOf(char16.substring(2, 3 + 1));
						w_nombre = lvalnum * coeff;
					}
					else
					{
						// logger
						salary.getParameter().setError(salary.getParameter().errorMessage("ERR-90080", salary.getParameter().getLangue(), salary.getInfoSalary().getComp_id().getNmat()));
						salary.getParameter().setPbWithCalulation(true);
						return 0;
					}
				}
				else
				{
					// logger
					salary.getParameter().setError(salary.getParameter().errorMessage("ERR-90079", salary.getParameter().getLangue(), salary.getInfoSalary().getComp_id().getNmat()));
					salary.getParameter().setPbWithCalulation(true);
					return 0;
				}
			}
		}
		catch (Exception e)
		{
			// logger
			salary.getParameter().setError(
					"Exception sur convertToNumber37! " + salary.getParameter().errorMessage("ERR-90079", salary.getParameter().getLangue(), salary.getInfoSalary().getComp_id().getNmat()));
			salary.getParameter().setPbWithCalulation(true);
			return 0;
		}
		return w_nombre;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#getOutputtext()
	 */
	public String getOutputtext()
	{
		return outputtext;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#getSalary()
	 */
	public ClsSalaryToProcess getSalary()
	{
		return salary;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#Lecture_Code_nationalite(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public Double Lecture_Code_nationalite(ClsRubriqueClone rubrique)
	{
		double Code_nat = 0;
		tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,salary.getInfoSalary().getComp_id().getCdos(), 4, salary.getInfoSalary().getNato(), 1, salary.getNlot(),
				salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.AMOUNT, rubrique.getRubrique().getComp_id().getCrub());
		if (tempNumber == null)
		{
			// logger
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90086", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat(), rubrique.getRubrique().getTabl(), salary.getInfoSalary().getNato()));
			return new Double(0);
		}
		
		Code_nat = tempNumber.doubleValue();
		
		if (Code_nat > 3 || Code_nat < 0)
		{
			// logger
			salary.getParameter().setError(
					salary.getParameter().errorMessage("ERR-90087", salary.getParameter().getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id().getCrub(),
							salary.getInfoSalary().getComp_id().getNmat(), rubrique.getRubrique().getTabl(), salary.getInfoSalary().getNato()));
			return new Double(0);
		}
		return Code_nat;
	}

	/**
	 * =>rec_nbjcg Lecture du nombre de jours de conge du mois
	 * @return
	 */
	private int rechercheNombreDeJourCongeDuMois(String rubrique)
	{
		// nbjc_lx DECIMAL(5,2);
		// nbjc_ex DECIMAL(5,2);
		// nbj_con DECIMAL(5,2);
		//
		// BEGIN
		//
		// IF PA_CALCUL.Expatrie THEN
		//
		// nbjc_ex := paf_lecfnomT(99,'NBJC-DEF',2,PA_CALCUL.w_aamm,PA_CALCUL.w_nlot,PA_CALCUL.wsd_fcal1.nbul);
		//
		// IF PA_PAIE.NouZ(nbjc_ex) THEN
		// nbjc_ex := 5;
		// END IF;
		//
		// nbj_con := paf_lecfnomT(35,PA_CALCUL.wsal01.cat,5,PA_CALCUL.w_aamm,PA_CALCUL.w_nlot,PA_CALCUL.wsd_fcal1.nbul);
		//
		// IF PA_PAIE.NouZ(nbj_con) THEN
		// nbj_con := nbjc_ex;
		// END IF;
		// ELSE
		// nbjc_lx := paf_lecfnomT(99,'NBJC-DEF',1,PA_CALCUL.w_aamm,PA_CALCUL.w_nlot,PA_CALCUL.wsd_fcal1.nbul);
		//
		// IF PA_PAIE.NouZ(nbjc_lx) THEN
		// nbjc_lx := 2;
		// END IF;
		//
		// nbj_con := paf_lecfnomT(35,PA_CALCUL.wsal01.cat,3,PA_CALCUL.w_aamm,PA_CALCUL.w_nlot,PA_CALCUL.wsd_fcal1.nbul);
		// IF PA_PAIE.NouZ(nbj_con) THEN
		// nbj_con := nbjc_lx;
		// END IF;
		// END IF;
		int nbreJourCongelx = 0;
		int nbreJourCongeEx = 0;
		int nbreJourConge = 0;
		if (salary._isExpatrie())
		{
			tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,salary.getInfoSalary().getComp_id().getCdos(), 99, "NBJC-DEF", 2, salary.getNlot(),
					salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.RATES, rubrique);
			nbreJourCongeEx = tempNumber == null ? 0 : tempNumber.intValue();
			if (nbreJourCongeEx == 0)
				nbreJourCongeEx = 5;
			//
			tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,salary.getInfoSalary().getComp_id().getCdos(), 35, salary.getInfoSalary().getCat(), 5,
					salary.getNlot(), salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.RATES, rubrique);
			nbreJourConge = tempNumber == null ? 0 : tempNumber.intValue();
			if (nbreJourConge == 0)
				nbreJourConge = nbreJourCongeEx;
		}
		else
		{
			tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,salary.getInfoSalary().getComp_id().getCdos(), 99, "NBJC-DEF", 1, salary.getNlot(),
					salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.RATES, rubrique);
			nbreJourCongelx = tempNumber == null ? 0 : tempNumber.intValue();
			if (nbreJourCongelx == 0)
				nbreJourCongelx = 2;
			//
			tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,salary.getInfoSalary().getComp_id().getCdos(), 35, salary.getInfoSalary().getCat(), 3,
					salary.getNlot(), salary.getNbul(), this.salary.getMoisPaieCourant(), this.salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.RATES, rubrique);
			nbreJourConge = tempNumber == null ? 0 : tempNumber.intValue();
			if (nbreJourConge == 0)
				nbreJourConge = nbreJourCongelx;
		}
		//
		return nbreJourConge;
	}

	/**
	 * @param rubrique1
	 * @return
	 */
	private int rechercheNombreDeJourTravailDuMois(ClsRubriqueClone rubrique1)
	{
		ClsRubriqueClone rubrique = salary.findRubriqueClone(salary.parameter.jourTravailleRubrique);
		Double n = new Double(0);
		if ("B".equals(salary.getParameter().getJourTravaillePlage()))
			n = rubrique.getBase();
		else if ("T".equals(salary.getParameter().getJourTravaillePlage()))
			n = rubrique.getRates();
		else if ("M".equals(salary.getParameter().getJourTravaillePlage()))
			n = rubrique.getAmount();
		return n.intValue();
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#setOutputtext(java.lang.String)
	 */
	public void setOutputtext(String outputtext)
	{
		this.outputtext = outputtext;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#setSalary(com.cdi.deltarh.paie.engine.ClsSalaryToProcess)
	 */
	public void setSalary(ClsSalaryToProcess salary)
	{
		this.salary = salary;
	}

	// private void listdesrubriques(List rub){
	// for (Object object : rub) {
	// if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + object);
	// }
	// }
	private Object callMethode(Object obj, String nomMethode,Object valeurParam)
	{
		if(obj == null) return null;
		try
		{
//			Method oMethod = obj.getClass().getMethod(nomMethode, classe);
//			if (oMethod != null) return  oMethod.invoke(obj, valeurParam);
			return MethodUtils.invokeExactMethod(obj, nomMethode, valeurParam);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	private void instanceSNCC()
	{
		try
		{
			sncc = Class.forName ("com.cdi.deltarh.paie.engine.ClsAlgorithmSNCC").newInstance ();
			this.callMethode(sncc, "init", salary);
		}
		catch (Exception e)
		{
			//e.printStackTrace();
		}
	}
	//-------------ALGORITHMES SPECIFIQUES SNCC
	//900 a 932 : execution
	//933 - 965 : M et C
	//966 - 999 : Directeurs
	
	// Exécution - calculPrimeCommissionnement
	public boolean algo900(ClsRubriqueClone rubrique)
	{
		this.callMethode(sncc, "calculPaieMin", rubrique);
		return true;
	}

	// Calcul du nombre de points des diplémes
//	@Override
	public boolean algo901(ClsRubriqueClone rubrique) {
		// TODO Auto-generated method stub
		ClsInfoSalaryClone info = this.salary.infoSalary;
	    String cdos = info.getComp_id().getCdos();
	    String nmat = info.getComp_id().getNmat();
	    String cat = info.getCat();
	    
	    BigDecimal nbrePoints = BigDecimal.ZERO;
	    boolean peutCumulPts = false;
	    
		String requete = "select ag.nmat, dp.valm, nf.vall from rhtdiplomeagent ag, ParamData dp "+ 
						 "left join ParamData nf on (nf.cdos=dp.cdos and nf.ctab=dp.ctab and nf.cacc=dp.cacc and nf.nume=2) "+
				         "where ag.cdos=dp.cdos and ag.cdip=dp.cacc and dp.ctab=16 and dp.nume=1 and ag.identreprise='"+cdos+"' and ag.nmat='"+nmat+"' order by dp.valm desc";
		
		Session session  = salary.service.getSession();
		List<Object[]> objs = session.createSQLQuery(requete).list();
		salary.service.closeSession(session);
		
		if(objs==null || objs.size()==0){
			this.salary.getValeurRubriquePartage().setAmount(nbrePoints.doubleValue());
		    this.salary.getValeurRubriquePartage().setBase(nbrePoints.doubleValue());
		    this.salary.getValeurRubriquePartage().setBasePlafonnee(nbrePoints.doubleValue());
			return true;
		}
		
		ParamData cumulPts = salary.service.findAnyColumnFromNomenclature (cdos, "", "132", cat, "1"); 
		if(cumulPts!=null && cumulPts.getValm()!=null) peutCumulPts = (cumulPts.getValm().intValue()==1)?true:false;
			
		try {
			List<String> famillesParcours = new ArrayList<String>();
			for(Object[] obj:objs){
				BigDecimal pt = (BigDecimal)obj[1];
				String famCourante = (String)obj[2];
				if(pt!=null){
					if(peutCumulPts==false){
						nbrePoints = pt;
						break;
					} else {
						if(StringUtils.isNotEmpty(famCourante)){
						   if(famillesParcours.contains(famCourante)==false) nbrePoints = nbrePoints.add(pt);
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		this.salary.getValeurRubriquePartage().setAmount(nbrePoints.doubleValue());
	    this.salary.getValeurRubriquePartage().setBase(nbrePoints.doubleValue());
	    this.salary.getValeurRubriquePartage().setBasePlafonnee(nbrePoints.doubleValue());
	    
		return true;
	}

//	@Override
	public boolean algo902(ClsRubriqueClone rubrique) {
		// TODO Auto-generated method stub
		 ClsInfoSalaryClone info = this.salary.infoSalary;
		 String cdos = info.getComp_id().getCdos();
		 String nmat = info.getComp_id().getNmat();
		 Date dtemb = info.getDtes();
		 String err = null;
		 String rubBaseConge = null;
		 ParamData rubSB = salary.service.findAnyColumnFromNomenclature (cdos, "", "266", "BASECONGE", "2");
		 if(rubSB!=null && StringUtils.isNotEmpty(rubSB.getVall())) rubBaseConge = rubSB.getVall();
		 else {
			 err = "Algo 77  Sal : " + nmat + ". Libellé 2 Param BASECONGE table 266 mal renseigne. " + "Verifier paramétrage";
		      err = this.salary.getParameter().errorMessage(err, this.salary.getParameter().getLangue(), new Object[0]);
		      this.salary.getParameter().setError(err);
		      return false;
		 }
		 int anneepasse = salary.getParameter().getMyMonthOfPay().getYear()-1;
		 String query = "SELECT sum(mont)  FROM CumulPaie WHERE identreprise='" + cdos + "' and nmat = '" + nmat + "' ";
		 query = query + " and aamm not like '%99' and aamm like '" + anneepasse + "%'  and rubq = '" + rubBaseConge + "' and nbul = " + this.salary.getNbul() + " ";
	      double mont_base_conge = 0.0D;
	        
	        List lst = this.salary.getService().find(query);
	        if ((!lst.isEmpty()) && (lst.get(0) != null)) {
	        	mont_base_conge = ClsObjectUtil.getBigDecimalFromObject(lst.get(0)).doubleValue();
	        }
	        
	        int nbremtrv = 12;
	        if(new ClsDate(dtemb).getYear()>anneepasse) nbremtrv = 0;
	        else if(new ClsDate(dtemb).getYear()==anneepasse) nbremtrv = 12 - new ClsDate(dtemb).getMonth() + 1;
	        
	        if(nbremtrv == 0){
	        	this.salary.getValeurRubriquePartage().setAmount(0);
			    this.salary.getValeurRubriquePartage().setBase(0);
			    this.salary.getValeurRubriquePartage().setBasePlafonnee(0);
	        } else {
	        	BigDecimal result = new BigDecimal(mont_base_conge).divide(new BigDecimal(nbremtrv), 0, RoundingMode.HALF_UP);
		        this.salary.getValeurRubriquePartage().setAmount(result.doubleValue());
		        this.salary.getValeurRubriquePartage().setRates(new BigDecimal(nbremtrv).doubleValue());
			    this.salary.getValeurRubriquePartage().setBase(new BigDecimal(mont_base_conge).doubleValue());
			    this.salary.getValeurRubriquePartage().setBasePlafonnee(new BigDecimal(mont_base_conge).doubleValue());
	        }
		
		    return true;
	}
	
//	@Override
	public boolean algo903(ClsRubriqueClone rubrique) {
		// TODO Auto-generated method stub
		 ClsInfoSalaryClone info = this.salary.infoSalary;
		 String cdos = info.getComp_id().getCdos();
		 String nmat = info.getComp_id().getNmat();
		 Date dtemb = info.getDtes();
		 String err = null;
		 String rubBase = null;
		 ParamData rubBaseBilan = (ParamData)this.salary.getService().findAnyColumnFromNomenclature(cdos, "", "266", "BASEBILAN", "2");

		 if(rubBaseBilan!=null && StringUtils.isNotEmpty(rubBaseBilan.getVall())) rubBase = rubBaseBilan.getVall();
		 else {
			 err = "Algo 903  Sal : " + nmat + ". Libellé 2 Param BASEBILAN table 266 mal renseigne. " + "Verifier paramétrage";
		      err = this.salary.getParameter().errorMessage(err, this.salary.getParameter().getLangue(), new Object[0]);
		      this.salary.getParameter().setError(err);
		      return false;
		 }
		 
		 String listeRub = "'0000'";
		 for(int j=1; j<=20; j++){
				if(j%(ParameterUtil.longueurRubrique+1)==1){
					try {
						listeRub = listeRub + ",'"+ rubBase.substring(j-1, j+ ParameterUtil.longueurRubrique-1)+"'";
					} catch (IndexOutOfBoundsException e) {
						// TODO: handle exception
						//e.printStackTrace();
//						break;
					}
				}
		 }
		 listeRub = "("+listeRub+")";
		 int anneepasse = salary.getParameter().getMyMonthOfPay().getYear()-1;
		 String query = "SELECT sum(mont)  FROM CumulPaie WHERE identreprise='" + cdos + "' and nmat = '" + nmat + "' ";
		 query = query + " and aamm not like '%99' and aamm like '" + anneepasse + "%'  and rubq in " + listeRub + " and nbul = " + this.salary.getNbul() + " ";
	      double mont_base_bilan = 0.0D;
	        
	        List lst = this.salary.getService().find(query);
	        if ((!lst.isEmpty()) && (lst.get(0) != null)) {
	        	mont_base_bilan = ClsObjectUtil.getBigDecimalFromObject(lst.get(0)).doubleValue();
	        }
	        
		        this.salary.getValeurRubriquePartage().setAmount(mont_base_bilan);
			    this.salary.getValeurRubriquePartage().setBase(mont_base_bilan);
			    this.salary.getValeurRubriquePartage().setBasePlafonnee(mont_base_bilan);
		
		    return true;
	}

	public boolean algo54(ClsRubriqueClone rubrique) {
		String cdos = salary.getInfoSalary().getComp_id().getCdos();
		// String crub = rubrique.getRubrique().getComp_id().getCrub();
		Double montant = new Double(0);
		// double taux = 0;
		double base = 0;
		double totalNbreJourSuppl = 0;
		int diviseurNbreJourSuppl = 0;
		//
//		salary.getValeurRubriquePartage().setRates(0);
//		salary.getValeurRubriquePartage().setAmount(0);
//		salary.getValeurRubriquePartage().setValeur(0);
//		if (ClsObjectUtil.isNull(salary.getInfoSalary().getDrtcg()) && ClsObjectUtil.isNull(salary.getInfoSalary().getDtes()))
//		{
//			// logger
//			salary.getParameter().setError(salary.getParameter().errorMessage("ERR-90067", salary.getParameter().getLangue(), salary.getInfoSalary().getComp_id().getNmat()));
//			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + salary.getParameter().getError();
//			return false;
//		}
//		// -- Verification de la coherence de la date de retour conges annuels
//		Date oDate = new ClsDate("01-01-1990", "dd-MM-yyyy").getDate();
//		if (salary.getInfoSalary().getDrtcg()!=null && salary.getInfoSalary().getDrtcg().getTime() < oDate.getTime())
//		{
//			// logger
//			salary.getParameter().setError(
//					salary.getParameter().errorMessage("ERR-90068", salary.getParameter().getLangue(), salary.getInfoSalary().getComp_id().getNmat(),
//							new ClsDate(salary.getInfoSalary().getDrtcg()).getDateS()));
//			if('O' == salary.getParameter().getGenfile()) outputtext += "\n" + salary.getParameter().getError();
//			return false;
//		}
//		Date date_retcg = null;
//		Date date_debcg = null;
//		
//		String requete=!salary.getParameter().isUseRetroactif() ?
//			"select max(ddcg) from HistoCongeSalarie " + " where identreprise='" + cdos + "'" + " and nmat = '" + salary.getInfoSalary().getComp_id().getNmat() + "'"
//			+ " and cmcg in (select a.cacc from ParamData a, ParamData b " + " where b.cacc = a.cacc "
//			+ " and b.ctab = b.ctab" + " and b.identreprise='" + cdos + "'" + " and a.identreprise='" + cdos + "'" + " and a.ctab = 22 "
//			+ " and b.ctab = 22 " + " and a.nume = 1 " + " and b.nume = 3 " + " and a.valm = 1 " + " and b.valm = 0 )" :
//	"select max(ddcg) from HistoCongeSalarie " + " where identreprise='" + cdos + "'" + " and nmat = '" + salary.getInfoSalary().getComp_id().getNmat() + "'"
//			+ " and cmcg in (select a.cacc from Rhthfnom a, Rhthfnom b "
//			+ " where b.cacc = a.cacc " + " and b.ctab = b.ctab" + " and b.identreprise='" + cdos + "'" + " and a.identreprise='" + cdos + "'"
//			+ " and a.ctab = 22 " + " and b.ctab = 22 " + " and a.nume = 1 " + " and b.nume = 3 " + " and a.valm = 1 " + " and b.valm = 0 )";
//	
//		Session session = salary.getService().getSession();
//		Query q = session.createSQLQuery(requete);
//		
//		List l = q.list();
//		salary.getService().closeSession(session);
//		if (!ClsObjectUtil.isListEmty(l))
//		{
//			if (!ClsObjectUtil.isNull(l.get(0)))
//				date_retcg = (Date) l.get(0);
//		}
//		if (date_retcg == null)
//		{
//			date_retcg = ClsObjectUtil.isNull(salary.getInfoSalary().getDrtcg()) ? salary.getInfoSalary().getDtes() : salary.getInfoSalary().getDrtcg();
//		}
//		// -- Determination de la date de debut de conges
//		// -- MM 12/10/2000 si STC date deb = date radiation
//		date_debcg = salary.getParameter().isStc() ? salary.getInfoSalary().getDmrr() : new ClsDate(salary.getPeriodOfPay()+"01", "yyyyMMdd").getLastDayOfMonth();
//		
//		//
//		// -- Calcul du nombre de mois ecoule depuis le dernier conges
//		double nbreDeMoisTr = ClsDate.getMonthsBetween(date_retcg, date_debcg);
//		if(salary.parameter.nomClient.equalsIgnoreCase(ClsEntreprise.COMILOG)) 
//			nbreDeMoisTr = ClsStringUtil.truncateToXDecimal(new ClsAlgorithmComilog(salary).getMonthsBetween(date_retcg, date_debcg),2).doubleValue();
		// -- Récupération du nombre de jrs de delai de route
		tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,cdos, 99, "BASE30", 2, salary.getNlot(), salary.getNbul(), salary.getMoisPaieCourant(), salary.getPeriodOfPay(),
				ClsEnumeration.EnColumnToRead.AMOUNT, rubrique.getRubrique().getComp_id().getCrub());
		double nbreJrAnnuel =  tempNumber.doubleValue();
		double nbreJrSupl =  0;
		
		String formattedMonth = ClsStringUtil.formatNumber(this.salary.getAnciennete(), "00");
		ParamData fnom = (ParamData)salary.getService().findAnyColumnFromNomenclature(cdos, "", "34", formattedMonth, "2");

		if(fnom != null && fnom.getValt()!=null)
		{
			nbreJrSupl = fnom.getValt().doubleValue();
		}
		
		// Contréle du nombre de jour travaillé
		double basecalcul = salary.getValeurRubriquePartage().getBase();
		if(basecalcul!=0 && basecalcul<=11) base=0;
		else base = ((nbreJrAnnuel + nbreJrSupl)/12);//*nbreDeMoisTr;
		salary.getValeurRubriquePartage().setBase(base);
		salary.getValeurRubriquePartage().setBasePlafonnee(base);
		salary.getValeurRubriquePartage().setAmount(base);
		
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo904(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo904(ClsRubriqueClone rubrique)
	{
		String cdos = salary.getInfoSalary().getComp_id().getCdos();
		// String crub = rubrique.getRubrique().getComp_id().getCrub();
		Double montant = new Double(0);
		// double taux = 0;
		double base = 0;
		double totalNbreJourSuppl = 0;
		int diviseurNbreJourSuppl = 0;
		double tau = 0;
		//
		
			Session session = null;
			Query q = null;
			String query= null;
			
		
			ParamData typeBGFI = (ParamData) salary.parameter.service.find("FROM ParamData WHERE identreprise="+cdos+" AND ctab=99 and nume=2 and cacc='PAYSBGFI'");

			if(typeBGFI !=null && StringUtils.isNotEmpty(typeBGFI.getVall()) && "CONGO".equalsIgnoreCase(typeBGFI.getVall())){
				query = "select cast(sum(nvl(nbjc,0)) as int) nb from ElementVariableConge where identreprise='" + this.salary.getInfoSalary().getComp_id().getCdos() + "' and year(ddeb)||lpad(month(ddeb),2,'0') <= '" + this.salary.getMoisPaieCourant() + "' ";
			    query = query + " and nmat = '" + this.salary.getInfoSalary().getComp_id().getNmat() + "' and nbul = " + this.salary.getNbul() + " AND motf = '01' ";
			} else {
				query="select cast(sum(nvl(nbja,0)) as int) nb from ElementVariableConge where identreprise='"+salary.getInfoSalary().getComp_id().getCdos()+"' and to_char(ddeb,'yyyyMM') <= '"+salary.getMoisPaieCourant()+"' ";
				query+=" and nmat = '"+salary.getInfoSalary().getComp_id().getNmat()+"' and nbul = "+salary.getNbul()+" AND motf in (select a.cacc from ParamData a, ParamData b "+
																	 "where b.cacc = a.cacc   and b.ctab = b.ctab  and b.identreprise='"+salary.getInfoSalary().getComp_id().getCdos()+"'  and a.identreprise='"+salary.getInfoSalary().getComp_id().getCdos()+"' "+
																	 "and a.ctab = 22 and b.ctab = 22 and a.nume = 1  and b.nume = 3 and a.valm = 1 and b.valm = 0 ) ";
				
				if (StringUtils.equals(TypeBDUtil.typeBD, TypeBDUtil.IN))
				{
					query="select cast(sum(nvl(nbja,0)) as int) nb from ElementVariableConge where identreprise='"+salary.getInfoSalary().getComp_id().getCdos()+"' and to_char1(ddeb,'YYYYMM') <= '"+salary.getMoisPaieCourant()+"' ";
					query+=" and nmat = '"+salary.getInfoSalary().getComp_id().getNmat()+"' and nbul = "+salary.getNbul()+" AND motf in (select a.cacc from ParamData a, ParamData b "+
																		 "where b.cacc = a.cacc   and b.ctab = b.ctab  and b.identreprise='"+salary.getInfoSalary().getComp_id().getCdos()+"'  and a.identreprise='"+salary.getInfoSalary().getComp_id().getCdos()+"' "+
																		 "and a.ctab = 22 and b.ctab = 22 and a.nume = 1  and b.nume = 3 and a.valm = 1 and b.valm = 0 ) ";
				}
				if (StringUtils.equals(TypeBDUtil.typeBD, TypeBDUtil.MS))
				{
					query="select cast(sum(nvl(nbja,0)) as int) nb from ElementVariableConge where identreprise='"+salary.getInfoSalary().getComp_id().getCdos()+"' and dbo.formaterDateEnChaine(ddeb,'yyyyMM') <= '"+salary.getMoisPaieCourant()+"' ";
					query+=" and nmat = '"+salary.getInfoSalary().getComp_id().getNmat()+"' and nbul = "+salary.getNbul()+" AND motf in (select a.cacc from ParamData a, ParamData b "+
																		 "where b.cacc = a.cacc   and b.ctab = b.ctab  and b.identreprise='"+salary.getInfoSalary().getComp_id().getCdos()+"'  and a.identreprise='"+salary.getInfoSalary().getComp_id().getCdos()+"' "+
																		 "and a.ctab = 22 and b.ctab = 22 and a.nume = 1  and b.nume = 3 and a.valm = 1 and b.valm = 0 ) ";
				}
				if (StringUtils.equals(TypeBDUtil.typeBD, TypeBDUtil.MY))
				{
					query="select cast(sum(nvl(nbja,0)) as int) nb from ElementVariableConge where identreprise='"+salary.getInfoSalary().getComp_id().getCdos()+"' and date_format(ddeb,'%Y%m') <= '"+salary.getMoisPaieCourant()+"' ";
					query+=" and nmat = '"+salary.getInfoSalary().getComp_id().getNmat()+"' and nbul = "+salary.getNbul()+" AND motf in (select a.cacc from ParamData a, ParamData b "+
																		 "where b.cacc = a.cacc   and b.ctab = b.ctab  and b.identreprise='"+salary.getInfoSalary().getComp_id().getCdos()+"'  and a.identreprise='"+salary.getInfoSalary().getComp_id().getCdos()+"' "+
																		 "and a.ctab = 22 and b.ctab = 22 and a.nume = 1  and b.nume = 3 and a.valm = 1 and b.valm = 0 ) ";
				}
			}
//		System.out.println("REQUETE CONGE: "+query);
		session = salary.parameter.service.getSession();
		try {
			q = session.createSQLQuery(query).addScalar("nb");
			List lst = q.list();
			
			if(! lst.isEmpty() && lst.get(0) != null)
				tau = tau + ClsObjectUtil.getDoubleFromObject(lst.get(0));
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			salary.parameter.service.closeSession(session);
		}
		
		salary.getValeurRubriquePartage().setRates(tau);
		base = salary.getValeurRubriquePartage().getBase();
		if(typeBGFI !=null && StringUtils.isNotEmpty(typeBGFI.getVall()) && "CONGO".equalsIgnoreCase(typeBGFI.getVall()))
			base = base*tau;
		else base = base*tau/30;
		salary.getValeurRubriquePartage().setAmount(base);
		
		return true;
	}

	/**
	 * Congés non pris
	 */
	public boolean algo905(ClsRubriqueClone rubrique) {
		String cdos = salary.getInfoSalary().getComp_id().getCdos();
		// String crub = rubrique.getRubrique().getComp_id().getCrub();
		Double montant = new Double(0);
		// double taux = 0;
		double base = 0;
		double totalNbreJourSuppl = 0;
		int diviseurNbreJourSuppl = 0;
		double tau = 0;
		//
		salary.getValeurRubriquePartage().setRates(0);
		salary.getValeurRubriquePartage().setAmount(0);
		salary.getValeurRubriquePartage().setValeur(0);
		//
		if (salary.getValeurRubriquePartage().getBase() == 0)
		{
			// logger
			return true;
		}
		
			tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,salary.getInfoSalary().getComp_id().getCdos(), 99, "FICTIF", 4, salary.getNlot(), salary.getNbul(),
					salary.getMoisPaieCourant(), salary.getPeriodOfPay(), ClsEnumeration.EnColumnToRead.AMOUNT, rubrique.getRubrique().getComp_id().getCrub());
			if(tempNumber != null){
				double diviseur = tempNumber.doubleValue();
				String cleRub = ClsStringUtil.formatNumber(tempNumber, ParameterUtil.formatRubrique);
				String query="select cast(sum(nvl(mont,0)) as int) nb from Rhteltvardet where identreprise='"+salary.getInfoSalary().getComp_id().getCdos()+"' and aamm = '"+salary.getMoisPaieCourant()+"' ";
				query+=" and nmat = '"+salary.getInfoSalary().getComp_id().getNmat()+"' and nbul = "+salary.getNbul()+" and rubq='"+cleRub+"'";
				Session session = salary.parameter.service.getSession();
				try {
					Query q = session.createSQLQuery(query).addScalar("nb");
					List lst = q.list();
					
					if(! lst.isEmpty() && lst.get(0) != null)
						tau = ClsObjectUtil.getDoubleFromObject(lst.get(0));
				} catch (Exception e) {
					// TODO: handle exception
				} finally {
					salary.parameter.service.closeSession(session);
				}
			}
		
		salary.getValeurRubriquePartage().setRates(tau);
		base = salary.getValeurRubriquePartage().getBase();
		base = base*tau;
		salary.getValeurRubriquePartage().setAmount(base);
		
		return true;
	}

	/**
	 * Congés STC
	 */
	public boolean algo906(ClsRubriqueClone rubrique) {
		String cdos = salary.getInfoSalary().getComp_id().getCdos();
		// String crub = rubrique.getRubrique().getComp_id().getCrub();
		Double montant = new Double(0);
		// double taux = 0;
		double base = 0;
		double totalNbreJourSuppl = 0;
		int diviseurNbreJourSuppl = 0;
		double tau = 0;
		//
		salary.getValeurRubriquePartage().setRates(0);
		salary.getValeurRubriquePartage().setAmount(0);
		salary.getValeurRubriquePartage().setValeur(0);
		//
		if (salary.getValeurRubriquePartage().getBase() == 0)
		{
			// logger
			return true;
		}
		
		if(salary.parameter.isStc()){
			tau = (salary.getInfoSalary().getJapec()==null)?0:salary.getInfoSalary().getJapec().doubleValue();
		} else {
			return true;
		}
		
		salary.getValeurRubriquePartage().setRates(tau);
		base = salary.getValeurRubriquePartage().getBase();
		base = base*tau;
		salary.getValeurRubriquePartage().setAmount(base);
		
		return true;
	}

	/**
	 * Nbre de jours acquis exercice en cours -- Exclut le mois en cours
	 */
	public boolean algo907(ClsRubriqueClone rubrique) {
		String cdos = salary.getInfoSalary().getComp_id().getCdos();
		// String crub = rubrique.getRubrique().getComp_id().getCrub();
		Double montant = new Double(0);
		// double taux = 0;
		double base = 0;
		double totalNbreJourSuppl = 0;
		int diviseurNbreJourSuppl = 0;
		//
		salary.getValeurRubriquePartage().setRates(0);
		salary.getValeurRubriquePartage().setAmount(0);
		salary.getValeurRubriquePartage().setValeur(0);
		// -- Calcul du nombre de mois ecoule depuis le début de l'exercice
		Date dtes = salary.getInfoSalary().getDtes();
		Date ddebfinex = new ClsDate(salary.getPeriodOfPay()+"01", "yyyyMMdd").getFirstDayOfYear();
		if(dtes.getTime()>ddebfinex.getTime()) ddebfinex = dtes;
		
		Date dtFinMoisPasse = new ClsDate(new ClsDate(salary.getPeriodOfPay()+"01", "yyyyMMdd").addMonth(-1)).getLastDayOfMonth();
		
		if(ddebfinex.getTime()>dtFinMoisPasse.getTime()){
			salary.getValeurRubriquePartage().setBase(0);
			salary.getValeurRubriquePartage().setBasePlafonnee(0);
			salary.getValeurRubriquePartage().setAmount(0);
			
			return true;
		}
		double nbreDeMoisTr = ClsDate.getMonthsBetween(ddebfinex, dtFinMoisPasse);
		// -- Récupération du nombre de jrs de delai de route
		tempNumber = this.getAmountOrRateFromNomenclature(this.salary.parameter.listOfTableXXMap,cdos, 99, "BASE30", 2, salary.getNlot(), salary.getNbul(), salary.getMoisPaieCourant(), salary.getPeriodOfPay(),
				ClsEnumeration.EnColumnToRead.AMOUNT, rubrique.getRubrique().getComp_id().getCrub());
		double nbreJrAnnuel =  tempNumber.doubleValue();
		double nbreJrSupl =  0;
		
		String formattedMonth = ClsStringUtil.formatNumber(this.salary.getAnciennete(), "00");
		ParamData fnom = (ParamData)salary.getService().find("FROM ParamData WHERE identreprise="+cdos+" AND ctab=34 and nume=2 and cacc='"+formattedMonth+"'");

		if(fnom != null && fnom.getValt()!=null)
		{
			nbreJrSupl = fnom.getValt().doubleValue();
		}
		
		base = ((nbreJrAnnuel + nbreJrSupl)/12)*nbreDeMoisTr;
		salary.getValeurRubriquePartage().setBase(base);
		salary.getValeurRubriquePartage().setBasePlafonnee(base);
		salary.getValeurRubriquePartage().setAmount(base);
		
		return true;
	}

	/**
	 * Nbre de jours congés pris exercice en cours -- Exclut le mois en cours
	 */
	public boolean algo908(ClsRubriqueClone rubrique) {
		String cdos = salary.getInfoSalary().getComp_id().getCdos();
		// String crub = rubrique.getRubrique().getComp_id().getCrub();
		Double montant = new Double(0);
		// double taux = 0;
		double base = 0;
		double totalNbreJourSuppl = 0;
		int diviseurNbreJourSuppl = 0;
		//
		salary.getValeurRubriquePartage().setRates(0);
		salary.getValeurRubriquePartage().setAmount(0);
		salary.getValeurRubriquePartage().setValeur(0);
		
		Date dtes = salary.getInfoSalary().getDtes();
		Date ddebfinex = new ClsDate(salary.getPeriodOfPay()+"01", "yyyyMMdd").getFirstDayOfYear();
		if(dtes.getTime()>ddebfinex.getTime()) ddebfinex = dtes;
		String ddcf = "'" + new ClsDate(ddebfinex).getDateS(salary.parameter.appDateFormat) + "'";
		String requete = "select sum(nvl(nbjc,0)) from HistoCongeSalarie " + " where identreprise='" + cdos + "'" + " and nmat = '" + salary.getInfoSalary().getComp_id().getNmat() + "'"
						+ " and ddcg > " + ddcf + "" + "  and cmcg in (select a.cacc from ParamData a, ParamData b " + " where b.cacc = a.cacc "
						+ " and b.ctab = b.ctab" + " and b.identreprise='" + cdos + "'" + " and a.identreprise='" + cdos + "'" + " and a.ctab = 22 "
						+ " and b.ctab = 22 " + " and a.nume = 1 " + " and b.nume = 3 " + " and a.valm = 1 " + " and b.valm = 0 )";
	
		Session session = salary.getService().getSession();
		Query q = session.createSQLQuery(requete);
		
		List l = q.list();
		salary.getService().closeSession(session);
		BigDecimal nbCOnge = BigDecimal.ZERO;
		if (!ClsObjectUtil.isListEmty(l))
		{
			if (!ClsObjectUtil.isNull(l.get(0)))
				nbCOnge = (BigDecimal) l.get(0);
		}

		if(nbCOnge!=null)
			base = nbCOnge.intValue();
		salary.getValeurRubriquePartage().setBase(base);
		salary.getValeurRubriquePartage().setBasePlafonnee(base);
		salary.getValeurRubriquePartage().setAmount(base);
		
		return true;
	}
	
	/* (non-Javadoc)
	 * Obtention du nombre de jour de congé pris
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo904(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo800(ClsRubriqueClone rubrique)
	{
		String cdos = salary.getInfoSalary().getComp_id().getCdos();
		// String crub = rubrique.getRubrique().getComp_id().getCrub();
		Double montant = new Double(0);
		// double taux = 0;
		double base = 0;
		double totalNbreJourSuppl = 0;
		int diviseurNbreJourSuppl = 0;
		double tau = 0;
		//
		
			Session session = null;
			Query q = null;
			
		
		String query="select cast(sum(nvl(nbjc,0)) as int) nb from ElementVariableConge where identreprise='"+salary.getInfoSalary().getComp_id().getCdos()+"' and to_char(ddeb,'yyyyMM') <= '"+salary.getMoisPaieCourant()+"' ";
		query+=" and nmat = '"+salary.getInfoSalary().getComp_id().getNmat()+"' and nbul = "+salary.getNbul()+" AND motf in (select a.cacc from ParamData a, ParamData b "+
															 "where b.cacc = a.cacc   and b.ctab = b.ctab  and b.identreprise='"+salary.getInfoSalary().getComp_id().getCdos()+"'  and a.identreprise='"+salary.getInfoSalary().getComp_id().getCdos()+"' "+
															 "and a.ctab = 22 and b.ctab = 22 and a.nume = 1  and b.nume = 3 and a.valm = 1 and b.valm = 0 ) ";
		
		if (StringUtils.equals(TypeBDUtil.typeBD, TypeBDUtil.IN))
		{
			query="select cast(sum(nvl(nbjc,0)) as int) nb from ElementVariableConge where identreprise='"+salary.getInfoSalary().getComp_id().getCdos()+"' and to_char1(ddeb,'yyyyMM') <= '"+salary.getMoisPaieCourant()+"' ";
			query+=" and nmat = '"+salary.getInfoSalary().getComp_id().getNmat()+"' and nbul = "+salary.getNbul()+" AND motf in (select a.cacc from ParamData a, ParamData b "+
																 "where b.cacc = a.cacc   and b.ctab = b.ctab  and b.identreprise='"+salary.getInfoSalary().getComp_id().getCdos()+"'  and a.identreprise='"+salary.getInfoSalary().getComp_id().getCdos()+"' "+
																 "and a.ctab = 22 and b.ctab = 22 and a.nume = 1  and b.nume = 3 and a.valm = 1 and b.valm = 0 ) ";
			
		}
		
		if (StringUtils.equals(TypeBDUtil.typeBD, TypeBDUtil.MS))
		{
			query="select cast(sum(nvl(nbjc,0)) as int) nb from ElementVariableConge where identreprise='"+salary.getInfoSalary().getComp_id().getCdos()+"' and dbo.formaterDateEnChaine(ddeb,'yyyyMM') <= '"+salary.getMoisPaieCourant()+"' ";
			query+=" and nmat = '"+salary.getInfoSalary().getComp_id().getNmat()+"' and nbul = "+salary.getNbul()+" AND motf in (select a.cacc from ParamData a, ParamData b "+
																 "where b.cacc = a.cacc   and b.ctab = b.ctab  and b.identreprise='"+salary.getInfoSalary().getComp_id().getCdos()+"'  and a.identreprise='"+salary.getInfoSalary().getComp_id().getCdos()+"' "+
																 "and a.ctab = 22 and b.ctab = 22 and a.nume = 1  and b.nume = 3 and a.valm = 1 and b.valm = 0 ) ";
		}
		if (StringUtils.equals(TypeBDUtil.typeBD, TypeBDUtil.MY))
		{
			query="select cast(sum(nvl(nbjc,0)) as int) nb from ElementVariableConge where identreprise='"+salary.getInfoSalary().getComp_id().getCdos()+"' and date_format(ddeb,'%Y%m') <= '"+salary.getMoisPaieCourant()+"' ";
			query+=" and nmat = '"+salary.getInfoSalary().getComp_id().getNmat()+"' and nbul = "+salary.getNbul()+" AND motf in (select a.cacc from ParamData a, ParamData b "+
																 "where b.cacc = a.cacc   and b.ctab = b.ctab  and b.identreprise='"+salary.getInfoSalary().getComp_id().getCdos()+"'  and a.identreprise='"+salary.getInfoSalary().getComp_id().getCdos()+"' "+
																 "and a.ctab = 22 and b.ctab = 22 and a.nume = 1  and b.nume = 3 and a.valm = 1 and b.valm = 0 ) ";
		}
//		System.out.println("REQUETE: "+query);
		session = salary.parameter.service.getSession();
		try {
			q = session.createSQLQuery(query).addScalar("nb");
			List lst = q.list();
			
			if(! lst.isEmpty() && lst.get(0) != null)
				tau = tau + ClsObjectUtil.getDoubleFromObject(lst.get(0));
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			salary.parameter.service.closeSession(session);
		}
		
		salary.getValeurRubriquePartage().setRates(0);
		salary.getValeurRubriquePartage().setBase(tau);
		salary.getValeurRubriquePartage().setAmount(tau);
		
		return true;
	}
	
	/* (non-Javadoc)
	 * Absences é déduire des jours de congés
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo904(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo801(ClsRubriqueClone rubrique)
	{
		String cdos = salary.getInfoSalary().getComp_id().getCdos();
		// String crub = rubrique.getRubrique().getComp_id().getCrub();
		Double montant = new Double(0);
		// double taux = 0;
		double base = 0;
		double totalNbreJourSuppl = 0;
		int diviseurNbreJourSuppl = 0;
		double tau = 0;
		//
		
			Session session = null;
			Query q = null;
			
		
		String query="select cast(sum(nvl(nbja,0)) as int) nb from ElementVariableConge where identreprise='"+salary.getInfoSalary().getComp_id().getCdos()+"' and to_char(ddeb,'yyyyMM') <= '"+salary.getMoisPaieCourant()+"' ";
		query+=" and nmat = '"+salary.getInfoSalary().getComp_id().getNmat()+"' and nbul = "+salary.getNbul()+" AND motf in (select a.cacc from ParamData a where a.identreprise='"+salary.getInfoSalary().getComp_id().getCdos()+"' and a.ctab = 22 and a.nume = 3  and a.vall = 'O') ";
		if (StringUtils.equals(TypeBDUtil.typeBD, TypeBDUtil.IN))
		{
			query="select cast(sum(nvl(nbja,0)) as int) nb from ElementVariableConge where identreprise='"+salary.getInfoSalary().getComp_id().getCdos()+"' and to_char1(ddeb,'yyyyMM') <= '"+salary.getMoisPaieCourant()+"' ";
			query+=" and nmat = '"+salary.getInfoSalary().getComp_id().getNmat()+"' and nbul = "+salary.getNbul()+" AND motf in (select a.cacc from ParamData a where a.identreprise='"+salary.getInfoSalary().getComp_id().getCdos()+"' and a.ctab = 22 and a.nume = 3  and a.vall = 'O') ";
		}
		
		if (StringUtils.equals(TypeBDUtil.typeBD, TypeBDUtil.MS))
		{
			query="select cast(sum(nvl(nbja,0)) as int) nb from ElementVariableConge where identreprise='"+salary.getInfoSalary().getComp_id().getCdos()+"' and dbo.formaterDateEnChaine(ddeb,'yyyyMM') <= '"+salary.getMoisPaieCourant()+"' ";
			query+=" and nmat = '"+salary.getInfoSalary().getComp_id().getNmat()+"' and nbul = "+salary.getNbul()+" AND motf in (select a.cacc from ParamData a where a.identreprise='"+salary.getInfoSalary().getComp_id().getCdos()+"' and a.ctab = 22 and a.nume = 3  and a.vall = 'O') ";
		}
		if (StringUtils.equals(TypeBDUtil.typeBD, TypeBDUtil.MY))
		{
			query="select cast(sum(nvl(nbja,0)) as int) nb from ElementVariableConge where identreprise='"+salary.getInfoSalary().getComp_id().getCdos()+"' and date_format(ddeb,'%Y%m') <= '"+salary.getMoisPaieCourant()+"' ";
			query+=" and nmat = '"+salary.getInfoSalary().getComp_id().getNmat()+"' and nbul = "+salary.getNbul()+" AND motf in (select a.cacc from ParamData a where a.identreprise='"+salary.getInfoSalary().getComp_id().getCdos()+"' and a.ctab = 22 and a.nume = 3  and a.vall = 'O') ";
		}
//		System.out.println("REQUETE: "+query);
		session = salary.parameter.service.getSession();
		try {
			q = session.createSQLQuery(query).addScalar("nb");
			List lst = q.list();
			
			if(! lst.isEmpty() && lst.get(0) != null)
				tau = tau + ClsObjectUtil.getDoubleFromObject(lst.get(0));
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			salary.parameter.service.closeSession(session);
		}
		
		salary.getValeurRubriquePartage().setRates(0);
		salary.getValeurRubriquePartage().setBase(tau);
		salary.getValeurRubriquePartage().setAmount(tau);
		
		return true;
	}

}
