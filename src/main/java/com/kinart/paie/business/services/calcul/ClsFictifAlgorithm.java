package com.kinart.paie.business.services.calcul;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.kinart.paie.business.model.*;
import com.kinart.paie.business.services.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.dao.DataAccessException;

/**
 * Cette classe donne une implémentation de tous les algorithmes utilisés pour le calcul de la base de clacul, de la base plafonnée, du taux et du montant des
 * différentes rubriques. Au paramétrage, il est précisé pour chaque rubrique l'algorithme qui sera utilisé.
 * <p>
 * Elle est la transcription Java du package Oracle PA_ALGO de la version Oracle de la paie.
 * </p>
 * 
 * @author 
 * @version 1.0
 */
public class ClsFictifAlgorithm implements IFictifAlgorithm
{
	ClsFictifSalaryToProcess fictivesalary = null;

	private String outputtext = "";

	private Number tempNumber = null;
	
	//ClsFictifAlgorithmComilog comilog;

	/**
	 * Constructeur par défaut
	 */
	private ClsFictifAlgorithm()
	{

	}

	/**
	 * A la construction de l'objet, il faut indiquer le salarié auquel correspond cet objet pour pouvoir utiliser les ressources qu'il regroupe déjé comme les
	 * paramétres de calcul et le service d'accés é la base de données.
	 * 
	 * @param fictivesalary
	 *            le salarié dont on calcule actuellement la paie.
	 */
	public ClsFictifAlgorithm(ClsFictifSalaryToProcess fictivesalary)
	{
		this.fictivesalary = fictivesalary;
		//comilog = new ClsFictifAlgorithmComilog(fictivesalary);
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo1(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public boolean algo1(ClsFictifRubriqueClone rubrique)
	{
		
		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\n" + ">>algo1 "+" Rubrique "+rubrique.getRubrique().getComp_id().getCrub();
		fictivesalary.getValeurRubriquePartage().setRates(0);
		fictivesalary.getValeurRubriquePartage().setAmount(fictivesalary.getValeurRubriquePartage().getBase());
		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\n" + "montant : " + fictivesalary.getValeurRubriquePartage().getAmount();
		
		return true;
	}
	
	public boolean algo9(ClsFictifRubriqueClone rubrique)
	{
		String error = null;
		fictivesalary.getValeurRubriquePartage().setBase(0);
		fictivesalary.getValeurRubriquePartage().setBasePlafonnee(0);
		fictivesalary.getValeurRubriquePartage().setAmount(0);
		fictivesalary.getValeurRubriquePartage().setRates(0);
		
		String cleAcces = rubrique.getRubrique().getComp_id().getCrub();
		String tabl = rubrique.getRubrique().getTabl();
		String cdos = fictivesalary.param.dossier;
		String nmat = fictivesalary.getInfoSalary().getComp_id().getNmat();
		long algo = rubrique.getRubrique().getAlgo();
		String queryParam = "From ParamData where cdos='"+cdos+"' and ctab = "+tabl+"  and cacc = '"+cleAcces+"' order by nume ";
		List<ParamData> lst = fictivesalary.service.find(queryParam);
		if(lst.isEmpty())
		{
			error = "ALGO" + algo + ", Sal " + nmat + ", Rbq " + cleAcces + ": Parametre " + cleAcces + ", Mq en T " + tabl;
			fictivesalary.param.setError(error);
			fictivesalary.param.setPbWithCalulation(true);
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
			fictivesalary.param.setError(error);
			fictivesalary.param.setPbWithCalulation(true);
			return false;
		}
		
		if(StringUtils.isBlank(fonction))
		{
			error = "ALGO" + algo + ", Sal " + nmat + ", Rbq " + cleAcces + ": Parametre " + cleAcces + ",Lib 2 Mq en T " + tabl;
			fictivesalary.param.setError(error);
			fictivesalary.param.setPbWithCalulation(true);
			return false;
		}
		
		String queryEnd="Select "+fonction+" From "+table+" where identreprise = '"+cdos+"' and nmat = '"+ nmat +"'";
		String colonne;
		String signe;
		String valeur;
		String[] decoup = null;
		String[] cle;
		for(int i=0; i<=conditions.length; i++)
		{
			//Traitement des conditions
			if(StringUtils.isBlank(conditions[i]))
				continue;
			decoup = StringUtils.split(conditions[i], "#");
			if(decoup.length != 3)
			{
				error = "ALGO" + algo + ", Sal " + nmat + ", Rbq " + cleAcces + ": Parametre " + cleAcces + ",Lib "+i+3+" Mal formé en T " + tabl;
				fictivesalary.param.setError(error);
				fictivesalary.param.setPbWithCalulation(true);
				return false;
			}
			colonne = decoup[0] ;
			signe = decoup[1];
			valeur = decoup[2];
			
			if(StringUtils.contains(colonne, "AGE"))
			{
				cle = StringUtils.split(colonne,".");
				if(cle.length != 2)
				{
					error = "ALGO" + algo + ", Sal " + nmat + ", Rbq " + cleAcces + ": Parametre " + cleAcces + ",Lib "+i+3+" Mal formé en T " + tabl;
					fictivesalary.param.setError(error);
					fictivesalary.param.setPbWithCalulation(true);
					return false;
				}
				colonne = cle[1];
				long anneecourante = fictivesalary.param.getMyMonthOfPay().getYear();
				long annee = anneecourante - Integer.valueOf(decoup[2]) - 1;
				long mois = fictivesalary.param.getMyMonthOfPay().getMonth();
				mois++;
				if (mois > 12)
				{
					mois = 1;
					annee++;
				}
				String dtna = ClsStringUtil.formatNumber(annee, "0000") + ClsStringUtil.formatNumber(mois, "00");
				Date dtna1 = new ClsDate(dtna, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getFirstDayOfMonth();			
				valeur =new ClsDate(dtna1).getDateS(fictivesalary.param.appDateFormat) ;		
			}
			queryEnd+=" and "+colonne+" "+signe+" '" + valeur + "'";
		}
		List nbreenfant = null;
		try
		{
			nbreenfant = fictivesalary.getService().find(queryEnd);
		}
		catch (Exception e)
		{
			error = "ALGO" + algo + ", Sal " + nmat + ", Rbq " + cleAcces + ": Parametre " + cleAcces + ", Impossible de fusionner les conditions en T " + tabl;
			fictivesalary.param.setError(error);
			fictivesalary.param.setPbWithCalulation(true);
			return false;
		}
		fictivesalary.getValeurRubriquePartage().setBase((Integer) nbreenfant.get(0));
		fictivesalary.getValeurRubriquePartage().setBasePlafonnee(fictivesalary.getValeurRubriquePartage().getBase());
		fictivesalary.getValeurRubriquePartage().setAmount((Integer) nbreenfant.get(0));
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo10(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public boolean algo10(ClsFictifRubriqueClone rubrique)
	{
		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\n" + ">>algo10"+" Rubrique "+rubrique.getRubrique().getComp_id().getCrub();
		// String codeRubrique = rubrique.getRubrique().getComp_id().getCrub();
		// double amountValue = -1;
		double rateValue = 0;
		int tabl = 0;
		double val = -1;
		String cleAccess = "";
		tempNumber = null;
		if (this.fictivesalary.getInfoSalary().getNiv3().compareTo(this.fictivesalary.param.getPrecNiv3()) != 0)
		{

			tabl = 3;
			cleAccess = this.fictivesalary.getInfoSalary().getNiv3();
			tempNumber = this.fictivesalary.utilNomenclatureFictif.getAmountOrRateFromNomenclature(this.fictivesalary.getInfoSalary().getComp_id().getCdos(), tabl, cleAccess, 1,
					this.fictivesalary.param.getNlot(), this.fictivesalary.param.getNumeroBulletin(), this.fictivesalary.param.getMonthOfPay(), ClsEnumeration.EnColumnToRead.RATES);

			if (tempNumber == null)
			{
				// logger
				fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90056", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id()
						.getCrub(), fictivesalary.getInfoSalary().getComp_id().getNmat(), fictivesalary.getInfoSalary().getNiv3(), cleAccess, rubrique.getRubrique().getTabl()));
				return false;
			}
			rateValue = tempNumber.doubleValue();
			this.fictivesalary.getWorkTimeFictif().setProrataNbreHeures(rateValue);
			this.fictivesalary.param.setPrecNiv3(this.fictivesalary.getInfoSalary().getNiv3());
		}
		else
			this.fictivesalary.getWorkTimeFictif().setProrataNbreHeures(0);
		//
		this.fictivesalary.getValeurRubriquePartage().setRates(rateValue);
		if ("T".equals(rubrique.getRubrique().getToum()) || "D".equals(rubrique.getRubrique().getToum()))
		{
			if (fictivesalary.getValeurRubriquePartage().getRates() != 0)
			{
				val = fictivesalary.getValeurRubriquePartage().getBase() / fictivesalary.getValeurRubriquePartage().getRates();
				fictivesalary.getValeurRubriquePartage().setAmount(val);
			}
		}
		else
		{
			val = fictivesalary.getValeurRubriquePartage().getBase() * fictivesalary.getValeurRubriquePartage().getRates();
			fictivesalary.getValeurRubriquePartage().setAmount(val);
		}
		return true;
	}

	public boolean algo100(ClsFictifRubriqueClone rubrique)
	{
		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\n" + ">>algo100"+" Rubrique "+rubrique.getRubrique().getComp_id().getCrub();
		fictivesalary.getValeurRubriquePartage().setRates(0);
		int agePeriode = ClsDate.getYearsBetween(fictivesalary.getInfoSalary().getDtna(), fictivesalary.param.getMyMonthOfPay().getDate());
		fictivesalary.getValeurRubriquePartage().setAmount(agePeriode);
		fictivesalary.getValeurRubriquePartage().setBase(agePeriode);
		fictivesalary.getValeurRubriquePartage().setBasePlafonnee(agePeriode);
		return true;
	}

	public boolean algo101(ClsFictifRubriqueClone rubrique)
	{
		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\n" + ">>algo101"+" Rubrique "+rubrique.getRubrique().getComp_id().getCrub();
		fictivesalary.getValeurRubriquePartage().setRates(0);
		int anciennete = ClsDate.getYearsBetween(fictivesalary.getInfoSalary().getDdca(), fictivesalary.param.getMyMonthOfPay().getDate());
		fictivesalary.getValeurRubriquePartage().setAmount(anciennete);
		fictivesalary.getValeurRubriquePartage().setBase(anciennete);
		fictivesalary.getValeurRubriquePartage().setBasePlafonnee(anciennete);
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo102(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public boolean algo102(ClsFictifRubriqueClone rubrique)
	{
		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\n" + ">>algo102"+" Rubrique "+rubrique.getRubrique().getComp_id().getCrub();
		String cdos = fictivesalary.getInfoSalary().getComp_id().getCdos();
		Double montant = new Double(0);
		String a_cacc = "";
		//
		fictivesalary.getValeurRubriquePartage().setRates(0);
		fictivesalary.getValeurRubriquePartage().setAmount(0);
		BigDecimal bd = new BigDecimal(fictivesalary.getValeurRubriquePartage().getBase());
		a_cacc = String.valueOf(bd.longValue());
		//
		// -- Lecture de la valeur dans les nomenclatures
		ClsEnumeration.EnColumnToRead col = "M".equals(rubrique.getRubrique().getToum()) ? ClsEnumeration.EnColumnToRead.AMOUNT : ClsEnumeration.EnColumnToRead.RATES;
		tempNumber = null;
		if (rubrique.getRubrique().getTabl() != null)
		{
			tempNumber = fictivesalary.utilNomenclatureFictif.getAmountOrRateFromNomenclature(cdos, Integer.valueOf(rubrique.getRubrique().getTabl()), a_cacc, rubrique.getRubrique().getNutm(),
					fictivesalary.param.getNlot(), fictivesalary.param.getNumeroBulletin(), this.fictivesalary.param.getMonthOfPay(), col);
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + "----------------------------->Montant de la table " + rubrique.getRubrique().getTabl() + " = " + montant;
		}

		if (tempNumber == null)
		{
			// logger
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90056", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(),
					rubrique.getRubrique().getComp_id().getCrub(), fictivesalary.getInfoSalary().getComp_id().getNmat(), a_cacc, rubrique.getRubrique().getTabl()));
			fictivesalary.param.setPbWithCalulation(true);
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + fictivesalary.param.getError();
			return false;
		}

		montant = tempNumber.doubleValue();
		fictivesalary.getValeurRubriquePartage().setValeur(montant);
		fictivesalary.getValeurRubriquePartage().setRates(0);
		fictivesalary.getValeurRubriquePartage().setAmount(montant);
		return true;
	}
	
	/* 
	 * Calcul du nombre de jours d'absences suivant un motif paramétré en table (tabl) sur clé le code de la rubrique, en libellé 1
	 */
	public boolean algo104(ClsFictifRubriqueClone rubrique)
	{
		if('O' == fictivesalary.getParam().getGenfile()) outputtext += "\n" + ">>algo5";

		String codeRubrique = rubrique.getRubrique().getComp_id().getCrub();
		String cdos = fictivesalary.getInfoSalary().getComp_id().getCdos();
		String nmat = this.fictivesalary.getInfoSalary().getComp_id().getNmat();
		HibernateConnexionService service = fictivesalary.getService();
		if(StringUtils.isBlank(rubrique.getRubrique().getTabl()))
		{
			// logger
			fictivesalary.getParam().setError(
					fictivesalary.getParam().errorMessage("Rub. "+codeRubrique+" ,Algo."+rubrique.getRubrique().getAlgo()+", Mat. "+nmat+" Table Nom renseigné",fictivesalary.getParam().getLangue()));
			return false;
		}
		Integer table =  Integer.valueOf(rubrique.getRubrique().getTabl());
		ParamData fnom = (ParamData)service.find("FROM ParamData WHERE identreprise="+Integer.valueOf(cdos)+" AND ctab="+table+" AND cacc='"+codeRubrique+"' AND nume=1");
		if(fnom == null)
		{
			fictivesalary.getParam().setError(
					fictivesalary.getParam().errorMessage("Rub. "+codeRubrique+" ,Algo."+rubrique.getRubrique().getAlgo()+", Mat. "+nmat+" Clé "+codeRubrique+" Inexistante en table "+table,fictivesalary.getParam().getLangue()));
			return false;
		}
		
		if(StringUtils.isBlank(fnom.getVall()))
		{
			fictivesalary.getParam().setError(
					fictivesalary.getParam().errorMessage("Rub. "+codeRubrique+" ,Algo."+rubrique.getRubrique().getAlgo()+", Mat. "+nmat+" Clé "+codeRubrique+", Libellé 1 Non renseigné en table "+table,fictivesalary.getParam().getLangue()));
			return false;
		}
		String motif = fnom.getVall().trim();
		String colonne="A";
		 fnom = (ParamData)service.find("FROM ParamData WHERE identreprise="+Integer.valueOf(cdos)+" AND ctab="+table+" AND cacc='"+codeRubrique+"' AND nume=2");
         if(StringUtils.isNotBlank(fnom.getVall()))
			colonne = fnom.getVall().trim().toUpperCase();
		
		if(StringUtil.notIn(colonne, "A,C"))
		{
			fictivesalary.getParam().setError(
					fictivesalary.getParam().errorMessage("Rub. "+codeRubrique+" ,Algo."+rubrique.getRubrique().getAlgo()+", Mat. "+nmat+" Clé "+codeRubrique+", Libellé 2 doit étre A(Absence) ou C(congé) en table "+table,fictivesalary.getParam().getLangue()));
			return false;
		}
		colonne = StringUtils.equals("C", colonne) ?"nbjc":"nbja";
		String ddeb = new ClsDate(fictivesalary.getParam().getFirstDayOfMonth()).getDateS(fictivesalary.param.appDateFormat);
		String dfin = new ClsDate(fictivesalary.getParam().getLastDayOfMonth()).getDateS(fictivesalary.param.appDateFormat);
		
		//System.out.println("*********************Rubrique "+codeRubrique+",Mois "+fictivesalary.param.getMonthOfPay()+" ddeb = "+ddeb+" et dfin = "+dfin);
		String query="select sum("+colonne+") from ElementVariableConge where identreprise = '" + cdos + "' and nmat = '" + nmat+ "' ";
		//query+=" and aamm = '" +  fictivesalary.param.getMonthOfPay() + "'" ;
		query+=" and nbul = " + this.fictivesalary.param.getNumeroBulletin()+" and ddeb >= '"+ddeb+"' and dfin <= '"+dfin+"' and motf = '"+motif+"'";
		List l = service.find( query);
		double amountValue = 0;
		if (!ClsObjectUtil.isListEmty(l) && l.get(0) != null)
			amountValue = (Double)(((BigDecimal) l.get(0)).doubleValue());
		
		fictivesalary.getValeurRubriquePartage().setRates(0);
		fictivesalary.getValeurRubriquePartage().setAmount(amountValue);
		fictivesalary.getValeurRubriquePartage().setBase(amountValue);
		fictivesalary.getValeurRubriquePartage().setBasePlafonnee(amountValue);
	
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo11(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public boolean algo11(ClsFictifRubriqueClone rubrique)
	{
		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\n" + ">>algo11"+" Rubrique "+rubrique.getRubrique().getComp_id().getCrub();
		fictivesalary.param.setPbWithCalulation(false);
		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\n" + "---Directly into algo, fictivesalary.param.isPbWithCalulation()=" + fictivesalary.param.isPbWithCalulation();

		// w_dec NUMBER;
		double w_dec = 0;
		double val = 0;
		fictivesalary.getValeurRubriquePartage().setAmount(0);
		fictivesalary.getValeurRubriquePartage().setRates(0);
		double inter = fictivesalary.getValeurRubriquePartage().getBase();
		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\n" + "En entrée, Base = " + inter;
		fictivesalary.getValeurRubriquePartage().setInter(inter);
//		if(StringUtils.equals(fictivesalary.param.nomClient, ClsEntreprise.COMILOG))
//		{
//
//		}
//		else
//		{
			if (inter <= 0)
			{
				if ('O' == fictivesalary.param.getGenfile())
					outputtext += "\n" + "------------------>Inter <= 0, return true";
				return true;
			}
		//}
		String cdos = fictivesalary.getInfoSalary().getComp_id().getCdos();
		String crub = rubrique.getRubrique().getComp_id().getCrub();
		// if('O' == fictivesalary.param.getGenfile()) outputtext += "\n" + "Rubrique courante = "+crub);
		//
		// -- Declaration d' un curseur sur la table des baremes
		// CURSOR curs_barem6 IS
		// SELECT cdos , crub , nume , val1, val2, NVL(taux, 0), NVL(mont, 0)
		// FROM parubbarem
		// where identreprise = PA_CALCUL.wpdos.cdos
		// AND crub = PA_CALCUL.t_rub.crub
		// ORDER BY cdos , crub , nume;
		// String queryString = "from ElementSalaireBareme "
		// + " where identreprise = '" + cdos + "'"
		// + " and crub = '" + crub + "'"
		// + " order by cdos , crub , nume";
		//
		// CURSOR curs_barem62 IS
		// SELECT cdos , crub , nume , val1, val2, NVL(taux, 0), NVL(mont, 0)
		// FROM pahrubbarem
		// where identreprise = PA_CALCUL.wpdos.cdos
		// AND crub = PA_CALCUL.t_rub.crub
		// AND aamm = PA_CALCUL.w_aamm
		// AND nbul = PA_CALCUL.wsd_fcal1.nbul
		// ORDER BY cdos , crub , nume;
		// String queryStringRetro = "from Rhthrubbarem "
		// + " where identreprise = '" + cdos + "'"
		// + " and crub = '" + crub + "'"
		// + " and aamm = '" + this.fictivesalary.param.getMonthOfPay() + "'"
		// + " and nbul = " + this.fictivesalary.param.getNumeroBulletin()
		// + " order by cdos , crub , nume";
		// ------------------------------------------------------------------------------
		// -- Abattement
		// -------------------------------------------------------------------------------
		if (!ClsObjectUtil.isNull(rubrique.getRubrique().getPcab()))
		{
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + "---Before computing, fictivesalary.param.isPbWithCalulation()=" + fictivesalary.param.isPbWithCalulation();
			val = fictivesalary.getValeurRubriquePartage().getBase() * rubrique.getRubrique().getPcab().doubleValue() / 100;
			fictivesalary.getValeurRubriquePartage().setAbattement(val);
			w_dec = rubrique.convertToNumber(rubrique.getRubrique().getAbmx());
			if (fictivesalary.param.isPbWithCalulation())
			{
				if ('O' == fictivesalary.param.getGenfile())
					outputtext += "\n" + "------------------>pb with calculation , converting abmx to number, return false; value is " + rubrique.getRubrique().getAbmx() + " and value must be " + val;
				return false;
			}
			if (w_dec > 0 && fictivesalary.getValeurRubriquePartage().getAbattement() > w_dec)
				fictivesalary.getValeurRubriquePartage().setAbattement(w_dec);
			val = fictivesalary.getValeurRubriquePartage().getBase() - fictivesalary.getValeurRubriquePartage().getAbattement();
			fictivesalary.getValeurRubriquePartage().setInter(val);
		}
		else
		{
			w_dec = rubrique.convertToNumber(rubrique.getRubrique().getAbat());
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + "Convertion du montant de l'abattement => " + w_dec;
			if (fictivesalary.param.isPbWithCalulation())
			{
				if ('O' == fictivesalary.param.getGenfile())
					outputtext += "\n" + "------------------>pb with calculation , converting abat to number, return false";
				return false;
			}
			if (w_dec > 0)
			{
				val = fictivesalary.getValeurRubriquePartage().getInter() - w_dec;
				fictivesalary.getValeurRubriquePartage().setInter(val);
			}
		}
		inter = fictivesalary.getValeurRubriquePartage().getInter();
		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\n" + "Final inter = " + inter;
		if (inter < 0)
		{
			fictivesalary.getValeurRubriquePartage().setAmount(0);
			fictivesalary.getValeurRubriquePartage().setBase(0);
			fictivesalary.getValeurRubriquePartage().setRates(0);
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + "------------------>inter <0 , return true";
			return true;
		}
		// -------------------------------------------------------------------------------
		// -- Division par nombre de parts
		// -------------------------------------------------------------------------------
		if ("O".equals(rubrique.getRubrique().getDnbp()))
		{
			if (!ClsObjectUtil.isNull(fictivesalary.getInfoSalary().getNbpt()))
			{
				val = fictivesalary.getValeurRubriquePartage().getInter() / fictivesalary.getInfoSalary().getNbpt().doubleValue();
				fictivesalary.getValeurRubriquePartage().setInter(val);
			}
			else
			{
				// logger
				fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90059", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(), fictivesalary.getInfoSalary()
						.getComp_id().getNmat(), rubrique.getRubrique().getComp_id().getCrub()));
				if ('O' == fictivesalary.param.getGenfile())
					outputtext += "\n" + "------------------>fictivesalary nbpt  is null , return false";
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
			if (fictivesalary.param.isUseRetroactif()){
//				int nbul = fictivesalary.param.getNumeroBulletin();
//				RhthrubbaremPK pk = null;// new RhthrubbaremPK(cdos, rubrique.getRubrique().getComp_id().getCrub(), 1, fictivesalary.param.getMonthOfPay(),
//											// nbul);
//
//				String _strQueryForFirstElement = "From Rhthrubbarem where cdos='" + cdos + "' and crub='" + rubrique.getRubrique().getComp_id().getCrub() + "' and aamm ='"
//						+ fictivesalary.param.getMonthOfPay() + "' and nbul=" + nbul + " order by nume";

//				List<Rhthrubbarem> _l_Result = fictivesalary.getService().find(_strQueryForFirstElement);
//				if (_l_Result.size() > 0)
//					obj = _l_Result.get(0);
//
//				// obj = fictivesalary.getService().get(Rhthrubbarem.class, pk);
//				if (obj != null)
//				{
//					oRhthrubbarem = (Rhthrubbarem) obj;
//					valeur2 = oRhthrubbarem.getVal2();
//					// ajout yannick
//					// this.fictivesalary.getValeurRubriquePartage().setRates(oRhthrubbarem.getTaux().doubleValue());
//				}
			}
			else
			{

				//ElementSalaireBaremePK pk1 = null;

				String _strQueryForFirstElement = "From ElementSalaireBareme where cdos='" + cdos + "' and crub='" + rubrique.getRubrique().getComp_id().getCrub()
						+ "' order by nume";

				List<ElementSalaireBareme> _l_Result = fictivesalary.getService().find(_strQueryForFirstElement);
				if (_l_Result.size() > 0)
					obj = _l_Result.get(0);

				//pk1 = _l_Result.get(0).getComp_id();
				// new ElementSalaireBaremePK(cdos, rubrique.getRubrique().getComp_id().getCrub(), 1);
				// obj = fictivesalary.getService().get(ElementSalaireBareme.class, pk1);
				if (obj != null)
				{
					oElementSalaireBareme = (ElementSalaireBareme) obj;
					//valeur2 = oElementSalaireBareme.getVal2();
					valeur2 = rubrique.getValeurBareme(oElementSalaireBareme, false);
					// ajout yannick
					// this.fictivesalary.getValeurRubriquePartage().setRates(oElementSalaireBareme.getTaux().doubleValue());
				}
			}
			if (obj == null)
			{
				// logger
				fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90060", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(), fictivesalary.getInfoSalary()
						.getComp_id().getNmat(), rubrique.getRubrique().getComp_id().getCrub()));
				if ('O' == fictivesalary.param.getGenfile())
					outputtext += "\n" + "------------------>pas de bareme alors que plafonné, return false";
				return false;
			}
			val = rubrique.convertToNumber(valeur2);
			if (fictivesalary.param.isPbWithCalulation())
			{
				if ('O' == fictivesalary.param.getGenfile())
					outputtext += "\n" + "------------------>pb with calculation , converting valeur2 to number, return false";
				return false;
			}
			fictivesalary.getValeurRubriquePartage().setPlafond(val);
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + "Taux = " + fictivesalary.getValeurRubriquePartage().getRates();
			if (fictivesalary.getValeurRubriquePartage().getInter() > fictivesalary.getValeurRubriquePartage().getPlafond())
			{
				fictivesalary.getValeurRubriquePartage().setBasePlafonnee(val);
				fictivesalary.getValeurRubriquePartage().setInter(val);
			}
		}
		// -------------------------------------------------------------------------------
		// -- APPLICATION D'UN BAREME ( tranche cumulees ou non )
		// -------------------------------------------------------------------------------
		fictivesalary.getValeurRubriquePartage().setAmount(0);
		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\n" + "On Fixe le montant é O, montant = " + fictivesalary.getValeurRubriquePartage().getAmount();
		int nbreBaremeRubrique = 0;
		double montant = 0;
		double taux = 0;
		String valeur1 = "";
		double dblValeur1 = 0;
		double dblValeur2 = 0;
		// construit la liste des barémes
		// List listOfBarem = fictivesalary.param.isUseRetroactif() ? fictivesalary.getService().find(queryStringRetro) :
		// fictivesalary.getService().find(queryString);
		//
		String keyOfBaremeList = fictivesalary.param.isUseRetroactif() ? crub + this.fictivesalary.param.getMonthOfPay() + this.fictivesalary.param.getNumeroBulletin() : crub;
		List listOfBarem = (List) fictivesalary.param.getListOfRubriquebaremeMap().get(keyOfBaremeList);

		if (listOfBarem != null)
		{
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + "Lecture du baréme avec valeur de comparaison =" + fictivesalary.getValeurRubriquePartage().getInter();
			for (Object barem : listOfBarem)
			{
				if (fictivesalary.param.isUseRetroactif())
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
				// if (nbreBaremeRubrique > 50)
				// {
				// nbreBaremeRubrique = 50;
				// break;
				// }
				//
				dblValeur1 = rubrique.convertToNumber(valeur1);
				if ('O' == fictivesalary.param.getGenfile())
					outputtext += "\n" + "Borne inf converti = " + dblValeur1;
				if (fictivesalary.param.isPbWithCalulation())
				{
					if ('O' == fictivesalary.param.getGenfile())
						outputtext += "\n" + "------------------>pb with calculation , converting valeur1,who is bareme val1, to number, return false";
					return false;
				}
				dblValeur2 = rubrique.convertToNumber(valeur2);
				if ('O' == fictivesalary.param.getGenfile())
					outputtext += "\n" + "Borne sup converti = " + dblValeur2;
				if (fictivesalary.param.isPbWithCalulation())
				{
					if ('O' == fictivesalary.param.getGenfile())
						outputtext += "\n" + "------------------>pb with calculation , converting valeur2,who is bareme val1, to number, return false";
					return false;
				}
				val = 0;
				if (fictivesalary.getValeurRubriquePartage().getInter() >= dblValeur1)
				{
					if ('O' == fictivesalary.param.getGenfile())
						outputtext += "\n" + "Intervalle trouvé = " + dblValeur1 + " é " + dblValeur2;
					val = fictivesalary.getValeurRubriquePartage().getInter() <= dblValeur2 ? fictivesalary.getValeurRubriquePartage().getInter() - dblValeur1 : dblValeur2 - dblValeur1;
					fictivesalary.getValeurRubriquePartage().setBasetaux(val);

					if ("N".equals(rubrique.getRubrique().getTrcu()))
						fictivesalary.getValeurRubriquePartage().setBasetaux(fictivesalary.getValeurRubriquePartage().getInter());
					val = 0;
					if ("M".equals(rubrique.getRubrique().getTxmt()))
					{
						fictivesalary.getValeurRubriquePartage().setValeur(montant);
						fictivesalary.getValeurRubriquePartage().setRates(0);
					}
					if ("T".equals(rubrique.getRubrique().getTxmt()))
					{
						fictivesalary.getValeurRubriquePartage().setRates(taux);
						val = fictivesalary.getValeurRubriquePartage().getBasetaux() * taux / 100;
						fictivesalary.getValeurRubriquePartage().setValeur(val);
					}
					if ("D".equals(rubrique.getRubrique().getTxmt()))
					{
						fictivesalary.getValeurRubriquePartage().setRates(taux);
						if (fictivesalary.getValeurRubriquePartage().getRates() != 0)
						{
							val = fictivesalary.getValeurRubriquePartage().getBasetaux() / fictivesalary.getValeurRubriquePartage().getRates();
							fictivesalary.getValeurRubriquePartage().setValeur(val);
						}
					}
					if ("O".equals(rubrique.getRubrique().getTrcu()))
					{
						val = fictivesalary.getValeurRubriquePartage().getAmount() + fictivesalary.getValeurRubriquePartage().getValeur();
						fictivesalary.getValeurRubriquePartage().setAmount(val);
					}
					else
					{
						val = fictivesalary.getValeurRubriquePartage().getValeur();
						fictivesalary.getValeurRubriquePartage().setAmount(val);
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
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90061", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(), fictivesalary.getInfoSalary().getComp_id()
					.getNmat(), rubrique.getRubrique().getComp_id().getCrub()));
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + "------------------>nbreBaremeRubrique is equal to 0, return false";
			return false;
		}
		// -------------------------------------------------------------------------------
		// -- Multiplication par nombre de part
		// -- le calcul se fait sur une part mais la cotisation sur toutes
		// -------------------------------------------------------------------------------
		if ("O".equals(rubrique.getRubrique().getDnbp()) && fictivesalary.getInfoSalary().getNbpt() != null)
		{
			val = fictivesalary.getValeurRubriquePartage().getAmount() * fictivesalary.getInfoSalary().getNbpt().doubleValue();
			fictivesalary.getValeurRubriquePartage().setAmount(val);
		}
		// -------------------------------------------------------------------------------
		// -- Perception mini
		// -------------------------------------------------------------------------------
		if (!ClsObjectUtil.isNull(rubrique.getRubrique().getPmin()))
		{
			val = rubrique.convertToNumber(rubrique.getRubrique().getPmin());
			if (fictivesalary.param.isPbWithCalulation())
			{
				if ('O' == fictivesalary.param.getGenfile())
					outputtext += "\n" + "------------------>pb with calculation , converting rubrique.pmin to number, return false";
				return false;
			}
			fictivesalary.getValeurRubriquePartage().setValeur(val);
			if (fictivesalary.getValeurRubriquePartage().getAmount() < fictivesalary.getValeurRubriquePartage().getValeur() && fictivesalary.getValeurRubriquePartage().getValeur() != 0)
			{
				val = fictivesalary.getValeurRubriquePartage().getValeur();
				fictivesalary.getValeurRubriquePartage().setAmount(val);
			}
		}
		// -------------------------------------------------------------------------------
		// -- Perception maxi
		// -------------------------------------------------------------------------------
		if (!ClsObjectUtil.isNull(rubrique.getRubrique().getPmax()))
		{
			val = rubrique.convertToNumber(rubrique.getRubrique().getPmax());
			if (fictivesalary.param.isPbWithCalulation())
			{
				if ('O' == fictivesalary.param.getGenfile())
					outputtext += "\n" + "------------------>pb with calculation rubrique.pmax to number, return false";
				return false;
			}
			fictivesalary.getValeurRubriquePartage().setValeur(val);
			if (fictivesalary.getValeurRubriquePartage().getAmount() > fictivesalary.getValeurRubriquePartage().getValeur() && fictivesalary.getValeurRubriquePartage().getValeur() != 0)
			{
				val = fictivesalary.getValeurRubriquePartage().getValeur();
				fictivesalary.getValeurRubriquePartage().setAmount(val);
			}
		}

		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\n" + "------------------>no pb, return true, end algo";
		
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo12(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public boolean algo12(ClsFictifRubriqueClone rubrique)
	{
		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\n" + ">>algo12"+" Rubrique "+rubrique.getRubrique().getComp_id().getCrub();
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
			tempNumber = this.fictivesalary.utilNomenclatureFictif.getAmountOrRateFromNomenclature(this.fictivesalary.getInfoSalary().getComp_id().getCdos(), tabl, cleAccess, rubrique.getRubrique()
					.getNutm(), this.fictivesalary.param.getNlot(), this.fictivesalary.param.getNumeroBulletin(), this.fictivesalary.param.getMonthOfPay(), colToRead);
		}
		if (tempNumber == null)
		{
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90056", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(),
					rubrique.getRubrique().getComp_id().getCrub(), fictivesalary.getInfoSalary().getComp_id().getNmat(), cleAccess, rubrique.getRubrique().getTabl()));
			fictivesalary.param.setPbWithCalulation(true);
			return false;
		}
		amountOrRateValue = tempNumber.doubleValue();
		fictivesalary.getValeurRubriquePartage().setValeur(amountOrRateValue);
		fictivesalary.getValeurRubriquePartage().setBase(this.fictivesalary.getInfoSalary().getIndi());
		fictivesalary.getValeurRubriquePartage().setBasePlafonnee(this.fictivesalary.getInfoSalary().getIndi());
		fictivesalary.getValeurRubriquePartage().setRates(amountOrRateValue);
		val = fictivesalary.getValeurRubriquePartage().getBase() * fictivesalary.getValeurRubriquePartage().getRates();
		fictivesalary.getValeurRubriquePartage().setAmount(val);
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo13(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public double pret_fic(String crub, String nprt)
	{
		
	   double p_dejr = 0;
	   String cdos = fictivesalary.getInfoSalary().getComp_id().getCdos();
	   String nmat =  fictivesalary.getInfoSalary().getComp_id().getNmat();
	   String w_aamm = fictivesalary.param.getMonthOfPay();
	   String query="SELECT SUM (mont) FROM Rhtfic where identreprise = '"+cdos+"'  AND aamm < '"+w_aamm+"' AND nmat = '"+nmat+"' ";
	   query+=" AND nbul != 0  AND rubq = '"+crub+"' AND nprt = '"+nprt+"' ";
	   List l = fictivesalary.getService().find(query);
	   if(!l.isEmpty() && l.get(0) != null)
		   p_dejr = new BigDecimal(l.get(0).toString()).doubleValue();

	   return p_dejr;
	}
	
	
	public boolean algo13(ClsFictifRubriqueClone rubrique)
	{
		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\n" + ">>algo13" + " Rubrique " + rubrique.getRubrique().getComp_id().getCrub();
		double w_mtrest = 0;
		double montant = 0;
		double taux = 0;
		double base = 0;
		double val = 0;
		double l_dejr = 0;
		//
		// BEGIN
		// -- Initialisations
		fictivesalary.getValeurRubriquePartage().setAmount(0);
		fictivesalary.getValeurRubriquePartage().setBase(0);
		fictivesalary.getValeurRubriquePartage().setRates(0);
		//
		// -- si traitement retroactif alors on prend les donnees dans les cumuls.
		String cdos = fictivesalary.getInfoSalary().getComp_id().getCdos();
		String crub = rubrique.getRubrique().getComp_id().getCrub();
		String nmat = fictivesalary.getInfoSalary().getComp_id().getNmat();

		int nbreDeprets = 0;
		if (rubrique.getListOfLoanNumber() != null)
		{
			nbreDeprets = rubrique.getListOfLoanNumber().size();
			if (nbreDeprets == 0)
				return true;
		}
		else return true;
		// on prend le premier prét de la liste des préts de ce salarié
		Object obj = fictivesalary.getService().find("FROM PretInterne WHERE identreprise="+Integer.valueOf(cdos)
														+" AND nmat='"+nmat+"' AND lg="+((Integer) rubrique.getListOfLoanNumber().get(0)).intValue());
		if (obj == null)
			return true;

		PretInterne pret = (PretInterne) obj;

		// -- Recherche dans pafic
		l_dejr = l_dejr + pret_fic(crub, pret.getIdEntreprise().toString());
		if (pret.getMtremb() == null)
			pret.setMtremb(BigDecimal.ZERO);
		pret.setMtremb(pret.getMtremb().add(new BigDecimal(l_dejr)));

		if (fictivesalary.param.isUseRetroactif())
		{
			montant = fictivesalary.utilNomenclatureFictif.getCumul(fictivesalary, fictivesalary.param.getMonthOfPay(), crub, fictivesalary.param.getNumeroBulletin(),
					ClsEnumeration.EnTypeOfColumn.AMOUNT, fictivesalary.param.getMonthOfPay());
			taux = fictivesalary.utilNomenclatureFictif.getCumul(fictivesalary, fictivesalary.param.getMonthOfPay(), crub, fictivesalary.param.getNumeroBulletin(),
					ClsEnumeration.EnTypeOfColumn.RATES, fictivesalary.param.getMonthOfPay());
			base = fictivesalary.utilNomenclatureFictif.getCumul(fictivesalary, fictivesalary.param.getMonthOfPay(), crub, fictivesalary.param.getNumeroBulletin(),
					ClsEnumeration.EnTypeOfColumn.BASE, fictivesalary.param.getMonthOfPay());
			//
			fictivesalary.getValeurRubriquePartage().setBase(base);
			fictivesalary.getValeurRubriquePartage().setBasePlafonnee(base);
			fictivesalary.getValeurRubriquePartage().setAmount(montant);
			fictivesalary.getValeurRubriquePartage().setRates(taux);
			return true;
		}
		//

		if (!ClsObjectUtil.isNull(pret.getMtpr()))
			w_mtrest = pret.getMtpr().doubleValue() - pret.getMtremb().doubleValue();
		else w_mtrest = pret.getMtmens().doubleValue();

		if (ClsObjectUtil.isNull(pret.getMtpr()) || (!ClsObjectUtil.isNull(pret.getMtpr()) && w_mtrest > 0))
		{
			//
			// --------------------------------------------------------------------
			// -- Calcul de la base ( base = montant emprunte )
			// --------------------------------------------------------------------
			if (fictivesalary.param.isStc() && fictivesalary.param.getEnBasePretsSTC() == ClsEnumeration.EnLoan.STC)
			{
				if (!ClsObjectUtil.isNull(pret.getMtpr()))
					fictivesalary.getValeurRubriquePartage().setAmount(w_mtrest);
				else fictivesalary.getValeurRubriquePartage().setAmount(0);
			}
			else
			{
				if (ClsObjectUtil.isNull(pret.getMtpr()))
				{
					// -- Base = deja rembourse, mois inclus
					val = pret.getMtremb().doubleValue() + pret.getMtmens().doubleValue();
					fictivesalary.getValeurRubriquePartage().setBase(val);
					fictivesalary.getValeurRubriquePartage().setBasePlafonnee(val);
				}
				else
				{
					if (fictivesalary.getEntreprise() == ClsEnumeration.EnEnterprise.SHELL_GABON)
					{
						if (ClsObjectUtil.isNull(pret.getMtmens()))
						{
							if (pret.getNbmens().intValue() != 0)
							{
								val = w_mtrest - (pret.getMtpr().doubleValue() / pret.getNbmens().intValue());
								fictivesalary.getValeurRubriquePartage().setBase(val);
							}
						}
						else
						{
							val = w_mtrest - pret.getMtmens().doubleValue();
							fictivesalary.getValeurRubriquePartage().setBase(val);
						}
						fictivesalary.getValeurRubriquePartage().setBasePlafonnee(fictivesalary.getValeurRubriquePartage().getBase());
					}
					else
					{
						// -- Base = Montant du pret
						fictivesalary.getValeurRubriquePartage().setBase(pret.getMtpr().doubleValue());
						fictivesalary.getValeurRubriquePartage().setBasePlafonnee(pret.getMtpr().doubleValue());
					}
				}
			}
			// --------------------------------------------------------------------
			// -- Calcul du montant ( montant = mensualite prevue ou calculee )
			// --------------------------------------------------------------------
			// -- LH 210198
			if (fictivesalary.param.isStc() && fictivesalary.param.getEnBasePretsSTC() == ClsEnumeration.EnLoan.STC)
			{
				if (!ClsObjectUtil.isNull(pret.getMtpr()))
					fictivesalary.getValeurRubriquePartage().setAmount(w_mtrest);
				else fictivesalary.getValeurRubriquePartage().setAmount(pret.getMtmens().doubleValue());
			}
			else
			{
				if (!ClsObjectUtil.isNull(pret.getMtmens()))
				{
					fictivesalary.getValeurRubriquePartage().setAmount(pret.getMtmens().doubleValue());
				}
				else
				{
					if (pret.getNbmens().intValue() != 0)
					{
						val = pret.getMtpr().doubleValue() / pret.getNbmens().doubleValue();
						fictivesalary.getValeurRubriquePartage().setAmount(Math.ceil(val));
					}
				}
				if (!ClsObjectUtil.isNull(pret.getMtpr()))
				{
					if (fictivesalary.getValeurRubriquePartage().getAmount() > w_mtrest)
						fictivesalary.getValeurRubriquePartage().setAmount(w_mtrest);
				}
			}
			// ---------------------------------------------------------------------
			// -- Calcul du taux
			// -- ( taux = nombre de mois equivalent au montant deja
			// -- rembourse pour la mensualite precedemment calculee )
			// ---------------------------------------------------------------------
			if (fictivesalary.param.isStc() && fictivesalary.param.getEnBasePretsSTC() == ClsEnumeration.EnLoan.STC)
				fictivesalary.getValeurRubriquePartage().setRates(0);
			else
			{
				// -- w_tau = Nb d'echeances deja payees
				if (pret.getNbmens()==null)
				{
					if (fictivesalary.getValeurRubriquePartage().getAmount() != 0)
					{
						val = (pret.getMtremb().doubleValue() + fictivesalary.getValeurRubriquePartage().getAmount()) / fictivesalary.getValeurRubriquePartage().getAmount();
						fictivesalary.getValeurRubriquePartage().setRates(val);
					}
				}
				else
				{
					if (pret.getNbmens().intValue() != 0)
					{
						val = (pret.getMtremb().doubleValue() + fictivesalary.getValeurRubriquePartage().getAmount())
								/ (pret.getMtpr().doubleValue() / pret.getNbmens().doubleValue());
						fictivesalary.getValeurRubriquePartage().setRates(val);
					}
				}
				if (fictivesalary.getEntreprise() == ClsEnumeration.EnEnterprise.SHELL_GABON)
				{
					// -- w_tau = Nb d'echances restantes
					if (!ClsObjectUtil.isNull(pret.getMtpr()))
					{
						if (ClsObjectUtil.isNull(pret.getNbmens()))
						{
							if (!ClsObjectUtil.isNull(pret.getMtmens()))
							{
								val = (pret.getMtpr().doubleValue() / pret.getMtmens().doubleValue()) - fictivesalary.getValeurRubriquePartage().getRates();
								fictivesalary.getValeurRubriquePartage().setRates(val);
							}
						}
						else
						{
							val = pret.getNbmens().doubleValue() - fictivesalary.getValeurRubriquePartage().getRates();
							fictivesalary.getValeurRubriquePartage().setRates(val);
						}
					}
				}
				if (fictivesalary.getValeurRubriquePartage().getRates() > 999)
					fictivesalary.getValeurRubriquePartage().setRates(999);
			}
		}
		fictivesalary.getEvparam().setNprt(ClsStringUtil.formatNumber(Integer.valueOf(pret.getLg()), "000000"));
		fictivesalary.getEvparam().setArgu("No ".concat(ClsStringUtil.formatNumber(Integer.valueOf(pret.getLg()), "000000")));
		// enlever ce prét de la liste des préts
		rubrique.getListOfLoanNumber().remove(pret.getLg());
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo14(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public boolean algo14(ClsFictifRubriqueClone rubrique)
	{
		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\n" + ">>algo14"+" Rubrique "+rubrique.getRubrique().getComp_id().getCrub();
		// double w_mtrest = 0;
		// double montant = 0;
		// double base = 0;
		double amountOrRate = 0;
		double val = 0;
		double w_basap = fictivesalary.getValeurRubriquePartage().getBase();
		double wcalc1 = 0;
		double wcalc2 = 0;
		int e = 0;
		int multAppoint = 0;
		double baseAppoint = 0;
		if (fictivesalary.getValeurRubriquePartage().getBase() < 0)
			return true;
		// -- Lecture table 54 si l'appoint est calcule pour ce mode de paiement
		String cdos = fictivesalary.getInfoSalary().getComp_id().getCdos();
		String libelle = fictivesalary.utilNomenclatureFictif.getLabelFromNomenclature(cdos, 54, fictivesalary.getInfoSalary().getModp(), 3, fictivesalary.param.getNlot(), fictivesalary.param
				.getNumeroBulletin(), fictivesalary.param.getMonthOfPay());
		// if(! ClsObjectUtil.isNull(libelle)){
		if (StringUtils.isBlank(libelle))
		{
			// logger
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90056", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(),
					rubrique.getRubrique().getComp_id().getCrub(), fictivesalary.getInfoSalary().getComp_id().getNmat(), fictivesalary.param.getMonthOfPay(), rubrique.getRubrique().getTabl()));
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
			// taux = this.fictivesalary.utilNomenclatureFictif.getAmountOrRateFromNomenclature(this.fictivesalary.getInfoSalary().getComp_id().getCdos(), tabl,
			// cleAccess, 1,
			// this.fictivesalary.param.getNlot(),
			// this.fictivesalary.param.getNumeroBulletin(), this.fictivesalary.param.getMonthOfPay(), ClsEnumeration.EnColumnToRead.RATES);
			tempNumber = this.fictivesalary.utilNomenclatureFictif.getAmountOrRateFromNomenclature(this.fictivesalary.getInfoSalary().getComp_id().getCdos(), tabl, cleAccess, 1,
					this.fictivesalary.param.getNlot(), this.fictivesalary.param.getNumeroBulletin(), this.fictivesalary.param.getMonthOfPay(), colToRead);
		}
		if (tempNumber == null)
		{
			// logger
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90056", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(),
					rubrique.getRubrique().getComp_id().getCrub(), fictivesalary.getInfoSalary().getComp_id().getNmat(), cleAccess, rubrique.getRubrique().getTabl()));
			return false;
		}
		amountOrRate = tempNumber.doubleValue();
		multAppoint = 1;
		if (!ClsObjectUtil.isNull(fictivesalary.param.getDossierInfo()))
		{

			if (fictivesalary.param.getDossierInfo().getNddd() == null)
				fictivesalary.param.getDossierInfo().setNddd(0);

			if (fictivesalary.param.getDossierInfo().getNddd() == 0)
				multAppoint = 1;
			else if (fictivesalary.param.getDossierInfo().getNddd() == 1)
				multAppoint = 10;
			else if (fictivesalary.param.getDossierInfo().getNddd() == 2)
				multAppoint = 100;
			else if (fictivesalary.param.getDossierInfo().getNddd() == 3)
				multAppoint = 1000;
			else
				multAppoint = 1;
		}
		//val = fictivesalary.getValeurRubriquePartage().getBase() * multAppoint;
		//fictivesalary.getValeurRubriquePartage().setBasePlafonnee(val);
		w_basap = fictivesalary.getValeurRubriquePartage().getBase() * multAppoint;
		baseAppoint = amountOrRate * multAppoint;
		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\n" + "--------------------------txt=" + amountOrRate + " et base appoint=" + baseAppoint + " pr mult=" + multAppoint;
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
				if ('O' == fictivesalary.param.getGenfile())
					outputtext += "\n" + "sbasap10 est = 0, return true";
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
					if ('O' == fictivesalary.param.getGenfile())
						outputtext += "\n" + "--------------->>>wcalc2 = " + wcalc2 + " et sbasap10=" + sbasap10 + " => montant = " + (wcalc2 - sbasap10);
					fictivesalary.getValeurRubriquePartage().setAmount(wcalc2 - sbasap10);
					break;
				}
				e = e + 1;
			}
			if (multAppoint != 0)
				fictivesalary.getValeurRubriquePartage().setAmount(fictivesalary.getValeurRubriquePartage().getAmount() / multAppoint);

			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + "baseAppoint < 100, return true";
			return true;
		}
		//
		if (baseAppoint < 1000)
		{
			if (sbasap100 == 0)
			{
				if ('O' == fictivesalary.param.getGenfile())
					outputtext += "\n" + "sbasap100 est = 0, return true";
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
					fictivesalary.getValeurRubriquePartage().setAmount(wcalc2 - sbasap100);
					break;
				}
				e = e + 1;
			}
			if (multAppoint != 0)
				fictivesalary.getValeurRubriquePartage().setAmount(fictivesalary.getValeurRubriquePartage().getAmount() / multAppoint);

			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + "baseAppoint < 1000, return true";
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
					fictivesalary.getValeurRubriquePartage().setAmount(wcalc2 - sbasap1000);
					break;
				}
				e = e + 1;
			}
			if (multAppoint != 0)
				fictivesalary.getValeurRubriquePartage().setAmount(fictivesalary.getValeurRubriquePartage().getAmount() / multAppoint);
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
					fictivesalary.getValeurRubriquePartage().setAmount(wcalc2 - sbasap10000);
					break;
				}
				e = e + 1;
			}
			if (multAppoint != 0)
				fictivesalary.getValeurRubriquePartage().setAmount(fictivesalary.getValeurRubriquePartage().getAmount() / multAppoint);
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
					fictivesalary.getValeurRubriquePartage().setAmount(wcalc2 - sbasap100000);
					break;
				}
				e = e + 1;
			}
			if (multAppoint != 0)
				fictivesalary.getValeurRubriquePartage().setAmount(fictivesalary.getValeurRubriquePartage().getAmount() / multAppoint);
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
					fictivesalary.getValeurRubriquePartage().setAmount(wcalc2 - sbasap1000000);
					break;
				}
				e = e + 1;
			}
			if (multAppoint != 0)
				fictivesalary.getValeurRubriquePartage().setAmount(fictivesalary.getValeurRubriquePartage().getAmount() / multAppoint);
			return true;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo15(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public boolean algo15(ClsFictifRubriqueClone rubrique)
	{
		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\n" + ">>algo15"+" Rubrique "+rubrique.getRubrique().getComp_id().getCrub();
		fictivesalary.getValeurRubriquePartage().setRates(0);
		if (fictivesalary.getValeurRubriquePartage().getBase() < fictivesalary.param.getBulletinNegative())
			fictivesalary.getValeurRubriquePartage().setAmount(fictivesalary.param.getBulletinNegative() - fictivesalary.getValeurRubriquePartage().getBase());
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo16(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public boolean algo16(ClsFictifRubriqueClone rubrique)
	{
		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\n" + ">>algo16"+" Rubrique "+rubrique.getRubrique().getComp_id().getCrub();
		String cdos = fictivesalary.getInfoSalary().getComp_id().getCdos();
		String crub = rubrique.getRubrique().getComp_id().getCrub();

		// String queryString = "from ElementSalaireBareme "
		// + " where identreprise = '" + cdos + "'"
		// + " and crub = '" + crub + "'"
		// + " order by cdos , crub , nume";
		// String queryStringRetro = "from Rhthrubbarem "
		// + " where identreprise = '" + cdos + "'"
		// + " and crub = '" + crub + "'"
		// + " and aamm = '" + fictivesalary.param.getMonthOfPay() + "'"
		// + " and nbul = " + fictivesalary.param.getNumeroBulletin()
		// + " order by cdos , crub , nume";
		//
		fictivesalary.getValeurRubriquePartage().setAmount(0);
		fictivesalary.getValeurRubriquePartage().setRates(0);
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
		// List listOfBarem = fictivesalary.param.isUseRetroactif() ? fictivesalary.getService().find(queryStringRetro) :
		// fictivesalary.getService().find(queryString);
		String keyOfBaremeList = fictivesalary.param.isUseRetroactif() ? crub + this.fictivesalary.param.getMonthOfPay() + this.fictivesalary.param.getNumeroBulletin() : crub;
		List listOfBarem = (List) fictivesalary.param.getListOfRubriquebaremeMap().get(keyOfBaremeList);
		if (listOfBarem != null)
			for (Object barem : listOfBarem)
			{
				if (fictivesalary.param.isUseRetroactif())
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
				if (fictivesalary.param.isPbWithCalulation())
				{
					return false;
				}
				dblValeur2 = rubrique.convertToNumber(valeur2);
				if (fictivesalary.param.isPbWithCalulation())
				{
					return false;
				}
				if (fictivesalary.getValeurRubriquePartage().getBase() >= dblValeur1 && fictivesalary.getValeurRubriquePartage().getBase() <= dblValeur2)
				{
					ok = true;
					fictivesalary.getValeurRubriquePartage().setAmount(montant);
					break;
				}
			}
		if (nbreBaremeRubrique == 0)
		{
			// logger
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90061", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(), fictivesalary.getInfoSalary().getComp_id()
					.getNmat(), rubrique.getRubrique().getComp_id().getCrub()));
			return false;
		}
		if (ok == false)
			fictivesalary.getValeurRubriquePartage().setAmount(0);
		// -- Multiplication par nombre de femmes
		if (ok == true && "M".equals(fictivesalary.getInfoSalary().getSitf()))
			fictivesalary.getValeurRubriquePartage().setAmount(fictivesalary.getValeurRubriquePartage().getAmount() * (fictivesalary.getInfoSalary().getNbfe() + 1));
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo17(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public boolean algo17(ClsFictifRubriqueClone rubrique)
	{
		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\n" + ">>algo17"+" Rubrique "+rubrique.getRubrique().getComp_id().getCrub();
		String cdos = fictivesalary.getInfoSalary().getComp_id().getCdos();
		String crub = rubrique.getRubrique().getComp_id().getCrub();
		PretExterneEntete wpaprent = null;
		double w_mens = 0;
		double montant = 0;
		double taux = 0;
		double base = 0;
		fictivesalary.getValeurRubriquePartage().setAmount(0);
		fictivesalary.getValeurRubriquePartage().setBase(0);
		fictivesalary.getValeurRubriquePartage().setRates(0);
		//
		// -- si traitement retroactif alors on prend les donnees dans les cumuls.
		if (fictivesalary.param.isUseRetroactif())
		{
			base = fictivesalary.utilNomenclatureFictif.getCumul(fictivesalary, fictivesalary.param.getMonthOfPay(), crub, fictivesalary.param.getNumeroBulletin(), ClsEnumeration.EnTypeOfColumn.BASE,
					fictivesalary.param.getMonthOfPay());
			montant = fictivesalary.utilNomenclatureFictif.getCumul(fictivesalary, fictivesalary.param.getMonthOfPay(), crub, fictivesalary.param.getNumeroBulletin(),
					ClsEnumeration.EnTypeOfColumn.AMOUNT, fictivesalary.param.getMonthOfPay());
			taux = fictivesalary.utilNomenclatureFictif.getCumul(fictivesalary, fictivesalary.param.getMonthOfPay(), crub, fictivesalary.param.getNumeroBulletin(),
					ClsEnumeration.EnTypeOfColumn.RATES, fictivesalary.param.getMonthOfPay());
			//
			fictivesalary.getValeurRubriquePartage().setAmount(montant);
			fictivesalary.getValeurRubriquePartage().setBase(base);
			fictivesalary.getValeurRubriquePartage().setRates(taux);
			fictivesalary.getValeurRubriquePartage().setBasePlafonnee(base);
			return true;
		}
		if (rubrique.getListOfLoanNumber() == null || rubrique.getListOfLoanNumber().size() == 0)
			return true;

		List listOfRhtprentagent = fictivesalary.getService().find(
				"from PretExterneEntete" + " where identreprise = '" + cdos + "'" + " and nprt = " + rubrique.getListOfLoanNumber().get(0) + " and nmat = '"
						+ fictivesalary.getInfoSalary().getComp_id().getNmat() + "'");
		if (listOfRhtprentagent == null || listOfRhtprentagent.size() == 0)
			return true;
		else
			wpaprent = (PretExterneEntete) listOfRhtprentagent.get(0);
		//
		// --LH 2101998
		if (fictivesalary.param.isStc() && fictivesalary.param.getEnBasePretsSTC() == ClsEnumeration.EnLoan.STC)
			// -- Dans le cas d'une radiation, le pret est solde
			w_mens = wpaprent.getResr().doubleValue();
		else
		{
			// -- Lecture de la mensualite
			List listOfPretExterneDetail = fictivesalary.getService().find("from PretExterneDetail" + " where identreprise = '" + cdos + "'" + " and nprt = " + wpaprent.getNprt());
			for (Object prlig : listOfPretExterneDetail)
			{
				if (fictivesalary.param.getMonthOfPay().equals(new ClsDate(((PretExterneDetail) prlig).getPerb()).getYearAndMonth()))
				{
					w_mens = ((PretExterneDetail) prlig).getEchr().doubleValue();
					break;
				}
			}
		}
		//
		if ("MP".equals(fictivesalary.param.getBasePrets()))
			fictivesalary.getValeurRubriquePartage().setBase(wpaprent.getMntp().doubleValue());
		else if ("DRI".equals(fictivesalary.param.getBasePrets()))
		{
			fictivesalary.getValeurRubriquePartage().setBase(wpaprent.getMntp().doubleValue() - wpaprent.getResr().doubleValue() + w_mens);
		}
		else if ("DRN".equals(fictivesalary.param.getBasePrets()))
		{
			fictivesalary.getValeurRubriquePartage().setBase(wpaprent.getMntp().doubleValue() - wpaprent.getResr().doubleValue());
		}
		else if ("RRI".equals(fictivesalary.param.getBasePrets()))
		{
			fictivesalary.getValeurRubriquePartage().setBase(wpaprent.getResr().doubleValue() - w_mens);
		}
		else if ("RRN".equals(fictivesalary.param.getBasePrets()))
		{
			fictivesalary.getValeurRubriquePartage().setBase(wpaprent.getResr().doubleValue());
		}
		else
			fictivesalary.getValeurRubriquePartage().setBase(wpaprent.getMntp().doubleValue());
		fictivesalary.getValeurRubriquePartage().setBasePlafonnee(fictivesalary.getValeurRubriquePartage().getBase());
		fictivesalary.getEvparam().setArgu("No ".concat(ClsStringUtil.formatNumber(Integer.valueOf(wpaprent.getNprt()), "000000")));
		fictivesalary.getValeurRubriquePartage().setAmount(w_mens);
		fictivesalary.getValeurRubriquePartage().setRates(0);
		// if(wpaprent.getNbec()==null)
		if (wpaprent.getNbec() != null && wpaprent.getNbec() != 0)
		{
			if ("NM".equals(fictivesalary.param.getBasePretsTaux()))
			{
				fictivesalary.getValeurRubriquePartage().setRates(wpaprent.getNbec());
			}
			else if ("DRI".equals(fictivesalary.param.getBasePretsTaux()))
			{
				fictivesalary.getValeurRubriquePartage().setRates(wpaprent.getNbec() - wpaprent.getNber() + 1);
			}
			else if ("DRN".equals(fictivesalary.param.getBasePretsTaux()))
			{
				fictivesalary.getValeurRubriquePartage().setRates(wpaprent.getNbec() - wpaprent.getNber());
			}
			else if ("RRI".equals(fictivesalary.param.getBasePretsTaux()))
			{
				fictivesalary.getValeurRubriquePartage().setRates(wpaprent.getNber() - 1);
			}
			else if ("RRN".equals(fictivesalary.param.getBasePretsTaux()))
			{
				fictivesalary.getValeurRubriquePartage().setRates(wpaprent.getNber());
			}
			else
				fictivesalary.getValeurRubriquePartage().setRates(wpaprent.getNbec());
		}
		fictivesalary.getEvparam().setNprt(ClsStringUtil.formatNumber(Integer.valueOf(wpaprent.getNprt()), "000000"));

		// enlever un numéro de prét dans la liste
		rubrique.getListOfLoanNumber().remove(0);
		//
		//Gestion de la devise
		String devise = wpaprent.getCodedevise();
		if(StringUtils.isNotBlank(devise))
		{
			if(!fictivesalary.param.dossierCcy.equalsIgnoreCase(devise))
			{
				tempNumber = this.fictivesalary.utilNomenclatureFictif.getAmountOrRateFromNomenclature(this.fictivesalary.getInfoSalary().getComp_id().getCdos(), 27, devise, 1,
						this.fictivesalary.param.getNlot(), this.fictivesalary.param.getNumeroBulletin(), this.fictivesalary.param.getMonthOfPay(), ClsEnumeration.EnColumnToRead.RATES);
				
				if(tempNumber != null && tempNumber.intValue() !=0 && tempNumber.intValue() !=1)
				{
					double mnt = fictivesalary.getValeurRubriquePartage().getAmount();
					double basc = fictivesalary.getValeurRubriquePartage().getBase();
					mnt = mnt * tempNumber.doubleValue();
					basc = basc * tempNumber.doubleValue();
					fictivesalary.getValeurRubriquePartage().setAmount(mnt);
					fictivesalary.getValeurRubriquePartage().setBase(basc);
					fictivesalary.getValeurRubriquePartage().setBasePlafonnee(basc);
				}
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo18(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public boolean algo18(ClsFictifRubriqueClone rubrique)
	{
		String cdos = fictivesalary.getInfoSalary().getComp_id().getCdos();
		String crub = rubrique.getRubrique().getComp_id().getCrub();
		double base = 0;
		double val = 0;
		int tabl = 0;
		String cleAccess = "";
		//
		fictivesalary.getValeurRubriquePartage().setAmount(0);
		// fictivesalary.getValeurRubriquePartage().setBase(0);
		fictivesalary.getValeurRubriquePartage().setRates(0);
		fictivesalary.getValeurRubriquePartage().setValeur(0);
		//
		tempNumber = null;
		if (rubrique.getRubrique().getTabl() != null)
		{
			cleAccess = rubrique.getRubrique().getComp_id().getCrub();
			tabl = Integer.valueOf(rubrique.getRubrique().getTabl());
			tempNumber = this.fictivesalary.utilNomenclatureFictif.getAmountOrRateFromNomenclature(this.fictivesalary.getInfoSalary().getComp_id().getCdos(), tabl, cleAccess, 1,
					this.fictivesalary.param.getNlot(), this.fictivesalary.param.getNumeroBulletin(), this.fictivesalary.param.getMonthOfPay(), ClsEnumeration.EnColumnToRead.AMOUNT);
			// fictivesalary.getValeurRubriquePartage().setBase(base);
		}
		if (tempNumber == null)
		{
			// -- appeler le logger
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90056", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(),
					rubrique.getRubrique().getComp_id().getCrub(), fictivesalary.getInfoSalary().getComp_id().getNmat(), rubrique.getRubrique().getComp_id().getCrub(), cleAccess, rubrique
							.getRubrique().getTabl()));
			return false;
		}
		base = tempNumber.doubleValue();
		fictivesalary.getValeurRubriquePartage().setBase(base);
		fictivesalary.getValeurRubriquePartage().setBasePlafonnee(base);
		Object obj = null;
		//
		if (rubrique.getElementVariableNombre() != 0 && rubrique.getElementVariableValeur() != 0)
		{
			fictivesalary.getValeurRubriquePartage().setValeur(rubrique.getElementVariableValeur());
		}
		// if('O' == fictivesalary.param.getGenfile()) outputtext += "\n" + "..isUseRetroactif:" + fictivesalary.param.isUseRetroactif());
		else
		{
			if (fictivesalary.param.isUseRetroactif())
			{
//				obj = fictivesalary.getService().get(Rhthelfix.class,
//						new RhthelfixPK(cdos, fictivesalary.getInfoSalary().getComp_id().getNmat(), crub, fictivesalary.param.getMonthOfPay(), fictivesalary.param.getNumeroBulletin()));
			}
			else
			{
				obj = fictivesalary.getService().find("FROM ElementFixeSalaire WHERE identreprise="+cdos+" AND codp='"+crub+"' AND nmat='"+fictivesalary.getInfoSalary().getComp_id().getNmat()+"'");
			}
			if (obj != null)
			{
//				val = obj instanceof Rhthelfix ? ((Rhthelfix) obj).getMonp().doubleValue() : ((ElementFixeSalaire) obj).getMonp().doubleValue();
//				fictivesalary.getValeurRubriquePartage().setValeur(val);
			}
		}
		// fixer le taux avec la valeur
		fictivesalary.getValeurRubriquePartage().setRates(fictivesalary.getValeurRubriquePartage().getValeur());
		if ("T".equals(rubrique.getRubrique().getToum()))
		{
			fictivesalary.getValeurRubriquePartage().setAmount(fictivesalary.getValeurRubriquePartage().getBase() * fictivesalary.getValeurRubriquePartage().getRates() / 100);
		}
		else if ("D".equals(rubrique.getRubrique().getToum()))
		{
			if (fictivesalary.getValeurRubriquePartage().getRates() != 0)
				fictivesalary.getValeurRubriquePartage().setAmount(fictivesalary.getValeurRubriquePartage().getBase() / fictivesalary.getValeurRubriquePartage().getRates());
		}
		else if ("M".equals(rubrique.getRubrique().getToum()))
		{
			fictivesalary.getValeurRubriquePartage().setAmount(fictivesalary.getValeurRubriquePartage().getBase() * fictivesalary.getValeurRubriquePartage().getValeur());
		}
		else
		{
			// logger
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90058", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(), fictivesalary.getInfoSalary().getComp_id()
					.getNmat(), rubrique.getRubrique().getComp_id().getCrub()));
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo2(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public boolean algo2(ClsFictifRubriqueClone rubrique)
	{
		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\n" + ">>algo2"+" Rubrique "+rubrique.getRubrique().getComp_id().getCrub();

		String accessKey = this.fictivesalary.getInfoSalary().getAfec() + this.fictivesalary.getInfoSalary().getCat() + this.fictivesalary.getInfoSalary().getEch();
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
			tempNumber = this.fictivesalary.utilNomenclatureFictif.getAmountOrRateFromNomenclature(this.fictivesalary.getInfoSalary().getComp_id().getCdos(), tabl, accessKey, rubrique.getRubrique()
					.getNutm(), this.fictivesalary.param.getNlot(), this.fictivesalary.param.getNumeroBulletin(), this.fictivesalary.param.getMonthOfPay(), colToRead);
		}
		if (tempNumber == null)
		{
			fictivesalary.param.setPbWithCalulation(true);
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90056", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(),
					rubrique.getRubrique().getComp_id().getCrub(), fictivesalary.getInfoSalary().getComp_id().getNmat(), accessKey, rubrique.getRubrique().getTabl()));
			return false;
		}
		amountOrRateValue = tempNumber.doubleValue();
		fictivesalary.getValeurRubriquePartage().setValeur(amountOrRateValue);
		if ("M".equals(rubrique.getRubrique().getToum()))
		{
			fictivesalary.getValeurRubriquePartage().setRates(0);
			fictivesalary.getValeurRubriquePartage().setAmount(amountOrRateValue);
			fictivesalary.getValeurRubriquePartage().setBase(amountOrRateValue);
			fictivesalary.getValeurRubriquePartage().setBasePlafonnee(amountOrRateValue);
		}
		else if ("T".equals(rubrique.getRubrique().getToum()))
		{
			if (fictivesalary.getValeurRubriquePartage().getBase() != 0)
			{
				fictivesalary.getValeurRubriquePartage().setRates(amountOrRateValue);
				val = fictivesalary.getValeurRubriquePartage().getBase() * fictivesalary.getValeurRubriquePartage().getRates() / 100;
				fictivesalary.getValeurRubriquePartage().setAmount(val);
			}
			else
			{
				fictivesalary.getValeurRubriquePartage().setRates(0);
				fictivesalary.getValeurRubriquePartage().setAmount(amountOrRateValue);
				fictivesalary.getValeurRubriquePartage().setBase(amountOrRateValue);
				fictivesalary.getValeurRubriquePartage().setBasePlafonnee(amountOrRateValue);
			}
		}
		else
		{
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90057", fictivesalary.param.getLangue(), fictivesalary.getInfoSalary().getComp_id().getNmat(), rubrique.getRubrique()
					.getComp_id().getCrub()));
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo20(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public boolean algo20(ClsFictifRubriqueClone rubrique)
	{
		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\n" + ">>algo20"+" Rubrique "+rubrique.getRubrique().getComp_id().getCrub();
		String cdos = fictivesalary.getInfoSalary().getComp_id().getCdos();
		String crub = rubrique.getRubrique().getComp_id().getCrub();
		PretExterneEntete wpaprent = null;
		PretExterneDetail wpaprlig = null;
		double montant = 0;
		double taux = 0;
		double base = 0;
		fictivesalary.getValeurRubriquePartage().setAmount(0);
		fictivesalary.getValeurRubriquePartage().setBase(0);
		fictivesalary.getValeurRubriquePartage().setRates(0);
		// si traitement retroactif alors on prend les donnees dans les cumuls.
		if (fictivesalary.param.isUseRetroactif())
		{
			base = fictivesalary.utilNomenclatureFictif.getCumul(fictivesalary, fictivesalary.param.getMonthOfPay(), crub, fictivesalary.param.getNumeroBulletin(), ClsEnumeration.EnTypeOfColumn.BASE,
					fictivesalary.param.getMonthOfPay());
			montant = fictivesalary.utilNomenclatureFictif.getCumul(fictivesalary, fictivesalary.param.getMonthOfPay(), crub, fictivesalary.param.getNumeroBulletin(),
					ClsEnumeration.EnTypeOfColumn.AMOUNT, fictivesalary.param.getMonthOfPay());
			taux = fictivesalary.utilNomenclatureFictif.getCumul(fictivesalary, fictivesalary.param.getMonthOfPay(), crub, fictivesalary.param.getNumeroBulletin(),
					ClsEnumeration.EnTypeOfColumn.RATES, fictivesalary.param.getMonthOfPay());
			//
			fictivesalary.getValeurRubriquePartage().setBase(base);
			fictivesalary.getValeurRubriquePartage().setBasePlafonnee(base);
			fictivesalary.getValeurRubriquePartage().setAmount(montant);
			fictivesalary.getValeurRubriquePartage().setRates(taux);
			//
			return true;
		}
		if (ClsObjectUtil.isListEmty(rubrique.getListOfLoanNumber()))
			return true;
		List listOfRhtprentagent = fictivesalary.getService().find(
				"from PretExterneEntete" + " where identreprise = '" + cdos + "'" + " and nprt = " + rubrique.getListOfLoanNumber().get(0) + " and nmat = '"
						+ fictivesalary.getInfoSalary().getComp_id().getNmat() + "'");
		if (ClsObjectUtil.isListEmty(listOfRhtprentagent))
			return true;
		wpaprent = (PretExterneEntete) listOfRhtprentagent.get(0);
		//
		List listOfPretExterneDetail = null;
		// -- LH 210198
		if (fictivesalary.param.isStc() && fictivesalary.param.getEnBasePretsSTC() == ClsEnumeration.EnLoan.STC)
		{
			// -- Dans le cas d'une radiation, le pret est solde
			// BEGIN
			// SELECT SUM(echo), SUM(echr), SUM(inte), SUM(taxe)
			// INTO wpaprlig.echo, wpaprlig.echr, wpaprlig.inte, wpaprlig.taxe
			// FROM paprlig
			// where identreprise = PA_CALCUL.wpdos.cdos
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
			listOfPretExterneDetail = fictivesalary.getService().find(
					"select echo, echr, inte, taxe, perb from PretExterneDetail" + " where identreprise = '" + cdos + "'" + " and nprt = " + wpaprent.getNprt());
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
					if (new ClsDate(perb, this.fictivesalary.param.getAppDateFormat()).getYearAndMonth().compareTo(fictivesalary.param.getMonthOfPay()) >= 0)
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
			// where identreprise = PA_CALCUL.wpdos.cdos
			// AND nprt = wpaprent.nprt
			// AND TO_CHAR(perb,'YYYYMM') = w_aamm;
			// EXCEPTION
			// WHEN NO_DATA_FOUND THEN
			// wpaprlig.echo := 0;
			// wpaprlig.echr := 0;
			// wpaprlig.inte := 0;
			// wpaprlig.taxe := 0;
			// END;
			listOfPretExterneDetail = fictivesalary.getService().find(
					"from PretExterneDetail" + " where identreprise = '" + wpaprent.getIdEntreprise() + "'" + " and nprt = " + wpaprent.getNprt());
			PretExterneDetail row = null;
			Date perb = null;
			for (Object prlig : listOfPretExterneDetail)
			{
				row = (PretExterneDetail) prlig;
				perb = row.getPerb();
				if (new ClsDate(perb, this.fictivesalary.param.getAppDateFormat()).getYearAndMonth().equals(fictivesalary.param.getMonthOfPay()))
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
		tempNumber = this.fictivesalary.utilNomenclatureFictif.getAmountOrRateFromNomenclature(this.fictivesalary.getInfoSalary().getComp_id().getCdos(), tabl, cleAccess, 1, this.fictivesalary.param
				.getNlot(), this.fictivesalary.param.getNumeroBulletin(), this.fictivesalary.param.getMonthOfPay(), ClsEnumeration.EnColumnToRead.AMOUNT);
		tablGenCrub[0] = tempNumber == null ? 0 : tempNumber.doubleValue();
		// Active la génération de la rubrique saisie en montant 1 (1 = Oui, 0 = Non)
		tempNumber = this.fictivesalary.utilNomenclatureFictif.getAmountOrRateFromNomenclature(this.fictivesalary.getInfoSalary().getComp_id().getCdos(), tabl, cleAccess, 1, this.fictivesalary.param
				.getNlot(), this.fictivesalary.param.getNumeroBulletin(), this.fictivesalary.param.getMonthOfPay(), ClsEnumeration.EnColumnToRead.RATES);
		tablGenTaux[0] = tempNumber == null ? 0 : tempNumber.doubleValue();
		// Rubrique sur laquelle on injecte le montant des intéréts
		tempNumber = this.fictivesalary.utilNomenclatureFictif.getAmountOrRateFromNomenclature(this.fictivesalary.getInfoSalary().getComp_id().getCdos(), tabl, cleAccess, 2, this.fictivesalary.param
				.getNlot(), this.fictivesalary.param.getNumeroBulletin(), this.fictivesalary.param.getMonthOfPay(), ClsEnumeration.EnColumnToRead.AMOUNT);
		tablGenCrub[1] = tempNumber == null ? 0 : tempNumber.doubleValue();
		// Active la génération de la rubrique saisie en montant 2 (1 = Oui, 0 = Non)
		tempNumber = this.fictivesalary.utilNomenclatureFictif.getAmountOrRateFromNomenclature(this.fictivesalary.getInfoSalary().getComp_id().getCdos(), tabl, cleAccess, 2, this.fictivesalary.param
				.getNlot(), this.fictivesalary.param.getNumeroBulletin(), this.fictivesalary.param.getMonthOfPay(), ClsEnumeration.EnColumnToRead.RATES);
		tablGenTaux[1] = tempNumber == null ? 0 : tempNumber.doubleValue();
		// Rubrique sur laquelle on injecte le montant des taxes
		tempNumber = this.fictivesalary.utilNomenclatureFictif.getAmountOrRateFromNomenclature(this.fictivesalary.getInfoSalary().getComp_id().getCdos(), tabl, cleAccess, 3, this.fictivesalary.param
				.getNlot(), this.fictivesalary.param.getNumeroBulletin(), this.fictivesalary.param.getMonthOfPay(), ClsEnumeration.EnColumnToRead.AMOUNT);
		tablGenCrub[2] = tempNumber == null ? 0 : tempNumber.doubleValue();
		// Active la génération de la rubrique saisie en montant 3 (1 = Oui, 0 = Non)
		tempNumber = this.fictivesalary.utilNomenclatureFictif.getAmountOrRateFromNomenclature(this.fictivesalary.getInfoSalary().getComp_id().getCdos(), tabl, cleAccess, 3, this.fictivesalary.param
				.getNlot(), this.fictivesalary.param.getNumeroBulletin(), this.fictivesalary.param.getMonthOfPay(), ClsEnumeration.EnColumnToRead.RATES);
		tablGenTaux[2] = tempNumber == null ? 0 : tempNumber.doubleValue();
		fictivesalary.getEvparam().setNprt("");
		fictivesalary.getEvparam().setArgu("No ".concat(ClsStringUtil.formatNumber(Integer.valueOf(wpaprent.getNprt()), "000000")));
		//
		for (int i = 0; i < 3; i++)
		{
			if (tablGenTaux[i] != 0)
			{
				if (i == 0)
				{
					// -- Insertion de la rubrique amortissement
					fictivesalary.getValeurRubriquePartage().setAmount(wpaprlig.getEcho().doubleValue());
					fictivesalary.getValeurRubriquePartage().setBase(wpaprlig.getEcho().doubleValue());
					fictivesalary.getValeurRubriquePartage().setRates(0);
				}
				else if (i == 1)
				{
					// -- Insertion de la rubrique interet
					fictivesalary.getValeurRubriquePartage().setAmount(wpaprlig.getInte().doubleValue());
					fictivesalary.getValeurRubriquePartage().setBase(wpaprent.getResr().doubleValue());
					fictivesalary.getValeurRubriquePartage().setRates(wpaprent.getTint().doubleValue());
				}
				else if (i == 2)
				{
					// -- Insertion de la rubrique taxe
					fictivesalary.getValeurRubriquePartage().setAmount(wpaprlig.getTaxe().doubleValue());
					fictivesalary.getValeurRubriquePartage().setBase(wpaprlig.getInte().doubleValue());
					fictivesalary.getValeurRubriquePartage().setRates(wpaprent.getTtax().doubleValue());
				}
				//
				fictivesalary.getValeurRubriquePartage().setBasePlafonnee(fictivesalary.getValeurRubriquePartage().getBase());
				if (tablGenCrub[i] != 0)
				{
					ClsFictifRubriqueClone rubTemp = fictivesalary.findRubriqueCloneFictif(ClsStringUtil.formatNumber(tablGenCrub[i], ParameterUtil.formatRubrique));
					rubTemp.insertRubriqueAlgo();
				}
			}
		}
		//
		if ("MP".equals(fictivesalary.param.getBasePrets()))
		{
			fictivesalary.getValeurRubriquePartage().setBase(wpaprent.getMntp().doubleValue());
		}
		else if ("DRI".equals(fictivesalary.param.getBasePrets()))
		{
			fictivesalary.getValeurRubriquePartage().setBase(wpaprent.getMntp().doubleValue() - wpaprent.getResr().doubleValue() + wpaprlig.getEchr().doubleValue());
		}
		else if ("DRN".equals(fictivesalary.param.getBasePrets()))
		{
			fictivesalary.getValeurRubriquePartage().setBase(wpaprent.getMntp().doubleValue() - wpaprent.getResr().doubleValue());
		}
		else if ("RRI".equals(fictivesalary.param.getBasePrets()))
		{
			fictivesalary.getValeurRubriquePartage().setBase(wpaprent.getResr().doubleValue() - wpaprlig.getEchr().doubleValue());
		}
		else if ("RRN".equals(fictivesalary.param.getBasePrets()))
		{
			fictivesalary.getValeurRubriquePartage().setBase(wpaprent.getResr().doubleValue());
		}
		else
			fictivesalary.getValeurRubriquePartage().setBase(wpaprent.getMntp().doubleValue());
		//
		fictivesalary.getValeurRubriquePartage().setBasePlafonnee(fictivesalary.getValeurRubriquePartage().getBase());
		fictivesalary.getEvparam().setArgu("No ".concat(ClsStringUtil.formatNumber(Integer.valueOf(wpaprent.getNprt()), "000000")));
		fictivesalary.getValeurRubriquePartage().setAmount(wpaprlig.getEchr().doubleValue());
		//
		fictivesalary.getValeurRubriquePartage().setRates(0);
		//
		if (wpaprent.getNbec() != null && wpaprent.getNbec().doubleValue() != 0)
		{
			if ("NM".equals(fictivesalary.param.getBasePretsTaux()))
			{
				fictivesalary.getValeurRubriquePartage().setRates(wpaprent.getNbec().doubleValue());
			}
			else if ("DRI".equals(fictivesalary.param.getBasePretsTaux()))
			{
				fictivesalary.getValeurRubriquePartage().setRates(wpaprent.getNbec().doubleValue() - wpaprent.getNber().doubleValue() + 1);
			}
			else if ("DRN".equals(fictivesalary.param.getBasePretsTaux()))
			{
				fictivesalary.getValeurRubriquePartage().setRates(wpaprent.getNbec().doubleValue() - wpaprent.getNber().doubleValue());
			}
			else if ("RRI".equals(fictivesalary.param.getBasePretsTaux()))
			{
				fictivesalary.getValeurRubriquePartage().setRates(wpaprent.getNber().doubleValue() - 1);
			}
			else if ("RRN".equals(fictivesalary.param.getBasePretsTaux()))
			{
				fictivesalary.getValeurRubriquePartage().setRates(wpaprent.getNber().doubleValue());
			}
			else
				fictivesalary.getValeurRubriquePartage().setRates(wpaprent.getNbec().doubleValue());
		}
		//
		// fictivesalary.getEvparam().setNprt("No ".concat(wpaprent.getComp_id().getNprt().toString()));
		fictivesalary.getEvparam().setNprt(wpaprent.getNprt().toString());
		// enlever le prét traité
		rubrique.getListOfLoanNumber().remove(0);
		//
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo21(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public boolean algo21(ClsFictifRubriqueClone rubrique)
	{
		// String cdos = fictivesalary.getInfoSalary().getComp_id().getCdos();
		// String crub = rubrique.getRubrique().getComp_id().getCrub();
		double montant = 0;
		// double taux = 0;
		double base = 0;
		// divbascg NUMBER(15,0);
		double divbascg = 0;
		
//	   IF PA_CALFIC.wchg THEN
//	      w_bas := w_bas + cal_con;
//	   END IF;
//	   w_bas := w_bas + fic_con;
		base = fictivesalary.getValeurRubriquePartage().getBase();
		fictivesalary.getValeurRubriquePartage().setBase(base + this.fic_con());
		//
		if (fictivesalary.param.isStc())
		{
			if (fictivesalary.getInfoSalary().getDapa() == null)
				fictivesalary.getInfoSalary().setDapa(new BigDecimal(0));
			//
			if (fictivesalary.getInfoSalary().getDapec() == null)
				fictivesalary.getInfoSalary().setDapec(new BigDecimal(0));
			//
			if (fictivesalary.getInfoSalary().getDded() == null)
				fictivesalary.getInfoSalary().setDded(new BigDecimal(0));
			//
			base = fictivesalary.getValeurRubriquePartage().getBase();
			fictivesalary.getValeurRubriquePartage().setBase(base + fictivesalary.getInfoSalary().getDapa().doubleValue() + fictivesalary.getInfoSalary().getDapec().doubleValue());
			fictivesalary.getValeurRubriquePartage().setBasePlafonnee(base + fictivesalary.getInfoSalary().getDapa().doubleValue() + fictivesalary.getInfoSalary().getDapec().doubleValue());
			//
			// -- Lecture du diviseur de la base conges
			tempNumber = null;
			if (rubrique.getRubrique().getTabl() != null)
			{
				tempNumber = fictivesalary.utilNomenclatureFictif.getAmountOrRateFromNomenclature(this.fictivesalary.getInfoSalary().getComp_id().getCdos(), Integer.valueOf(rubrique.getRubrique()
						.getTabl()), fictivesalary.getInfoSalary().getCat(), 1, this.fictivesalary.param.getNlot(), this.fictivesalary.param.getNumeroBulletin(), this.fictivesalary.param
						.getMonthOfPay(), ClsEnumeration.EnColumnToRead.AMOUNT);
			}
			if (tempNumber == null || tempNumber.intValue() == 0)
			{
				// logger
				fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90062", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id()
						.getCrub(), fictivesalary.getInfoSalary().getComp_id().getNmat(), rubrique.getRubrique().getTabl(), fictivesalary.getInfoSalary().getCat()));
				return false;
			}

			divbascg = tempNumber.doubleValue();
			fictivesalary.getValeurRubriquePartage().setRates(fictivesalary.getInfoSalary().getJapa().doubleValue() + fictivesalary.getInfoSalary().getJapec().doubleValue());
			base = fictivesalary.getValeurRubriquePartage().getBase();
			fictivesalary.getValeurRubriquePartage().setAmount((base / divbascg) - fictivesalary.getInfoSalary().getDded().doubleValue());
			return true;
		}
		// -- ----- Pas de calcul si pas de fictif et WMDP != du premier mois de conges

		if ("N".equals(fictivesalary.param.getFictiveCalculus()) && (!fictivesalary.param.getMonthOfPay().equals(fictivesalary.getInfoSalary().getPmcf())))
		{
			fictivesalary.getValeurRubriquePartage().setAmount(0);
			fictivesalary.getValeurRubriquePartage().setBase(0);
			fictivesalary.getValeurRubriquePartage().setBasePlafonnee(0);
			fictivesalary.getValeurRubriquePartage().setRates(0);

			return true;
		}
		// -- Pas de calcul si pas de conges
		if (fictivesalary.getWorkTimeFictif().getNbreJourConges() == 0 && fictivesalary.getWorkTimeFictif().getNbreJourCongesAnnuelMoisSuiv() == 0)
		{
			fictivesalary.getValeurRubriquePartage().setAmount(0);
			fictivesalary.getValeurRubriquePartage().setBase(0);
			fictivesalary.getValeurRubriquePartage().setBasePlafonnee(0);
			fictivesalary.getValeurRubriquePartage().setRates(0);
			//
			return true;
		}

		// -- Pas de calcul si montant du conges deja calcule
		/*
		 * if(fictivesalary.getWorkTimeFictif().getMontantCongePonctuel() != 0){
		 * fictivesalary.getValeurRubriquePartage().setAmount(fictivesalary.getWorkTimeFictif().getMontantCongePonctuel()); return true; }
		 */
		if (fictivesalary.getInfoSalary().getDapa() == null)
			fictivesalary.getInfoSalary().setDapa(new BigDecimal(0));
		if (fictivesalary.getInfoSalary().getDapec() == null)
			fictivesalary.getInfoSalary().setDapec(new BigDecimal(0));
		if (fictivesalary.getInfoSalary().getDded() == null)
			fictivesalary.getInfoSalary().setDded(new BigDecimal(0));
		if (fictivesalary.getWorkTimeFictif().isDebutDeMois())
			fictivesalary.getValeurRubriquePartage().setBase(0);
		//
		base = fictivesalary.getValeurRubriquePartage().getBase();
		fictivesalary.getValeurRubriquePartage().setBase(base + fictivesalary.getInfoSalary().getDapa().doubleValue() + fictivesalary.getInfoSalary().getDapec().doubleValue());
		base = fictivesalary.getValeurRubriquePartage().getBase();
		fictivesalary.getValeurRubriquePartage().setBasePlafonnee(base);
		// -- Lecture du diviseur de la base conges
		tempNumber = null;
		if (rubrique.getRubrique().getTabl() != null)
		{
			tempNumber = fictivesalary.utilNomenclatureFictif.getAmountOrRateFromNomenclature(fictivesalary.getInfoSalary().getComp_id().getCdos(), Integer.valueOf(rubrique.getRubrique().getTabl()),
					fictivesalary.getInfoSalary().getCat(), 1, fictivesalary.param.getNlot(), fictivesalary.param.getNumeroBulletin(), fictivesalary.param.getMoisPaieCourant(),
					ClsEnumeration.EnColumnToRead.AMOUNT);
		}
		if (tempNumber == null || tempNumber.intValue() == 0)
		{
			// logger
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90062", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(),
					rubrique.getRubrique().getComp_id().getCrub(), fictivesalary.getInfoSalary().getComp_id().getNmat(), rubrique.getRubrique().getTabl(), fictivesalary.getInfoSalary().getCat()));
			return false;
		}
		divbascg = tempNumber.doubleValue();
		if ("N".equals(fictivesalary.param.getFictiveCalculus()))
		{
			if (fictivesalary.param.getMonthOfPay().equals(fictivesalary.getInfoSalary().getPmcf()))
				fictivesalary.getWorkTimeFictif().setNbreJourConges(fictivesalary.getInfoSalary().getNbjcf().intValue());
			else
				fictivesalary.getWorkTimeFictif().setNbreJourConges(0);
		}
		fictivesalary.getValeurRubriquePartage().setRates(fictivesalary.getWorkTimeFictif().getNbreJourConges() + fictivesalary.getWorkTimeFictif().getNbreJourCongesNonPris());
		base = fictivesalary.getValeurRubriquePartage().getBase();
		base = (base / divbascg) - fictivesalary.getInfoSalary().getDded().doubleValue() - fictivesalary.getWorkTimeFictif().getMontantCongePonctuel();
		fictivesalary.getValeurRubriquePartage().setAmount(base);
		//
		if (fictivesalary.getInfoSalary().getNbjaf() != null && fictivesalary.getInfoSalary().getNbjaf().doubleValue() != 0 && "O".equals(fictivesalary.param.getFictiveCalculus()))
		{
			montant = fictivesalary.getValeurRubriquePartage().getAmount();
			montant = montant * (fictivesalary.getValeurRubriquePartage().getRates() / fictivesalary.getInfoSalary().getNbjaf().doubleValue());
			fictivesalary.getValeurRubriquePartage().setAmount(montant);
		}

		// if (fictivesalary.getInfoSalary().getNbjcf() != null && fictivesalary.getInfoSalary().getNbjcf().doubleValue() != 0 &&
		// "O".equals(fictivesalary.param.getFictiveCalculus()))
		// {
		// montant = fictivesalary.getValeurRubriquePartage().getAmount();
		// montant = montant * (fictivesalary.getValeurRubriquePartage().getRates() / fictivesalary.getInfoSalary().getNbjcf().doubleValue());
		// fictivesalary.getValeurRubriquePartage().setAmount(montant);
		// }
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo22(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public boolean algo22(ClsFictifRubriqueClone rubrique)
	{
		if (fictivesalary.getWorkTimeFictif().getNbreJourCongesPonctuels() == 0)
			fictivesalary.getValeurRubriquePartage().setBase(0);
		else
		{
			fictivesalary.getValeurRubriquePartage().setRates(fictivesalary.getWorkTimeFictif().getNbreJourAbsencePourCongesPonctuels());
			if (fictivesalary.getWorkTimeFictif().getMontantCongePonctuel() != 0)
				fictivesalary.getValeurRubriquePartage().setAmount(fictivesalary.getWorkTimeFictif().getMontantCongePonctuel());
			else
			{
				fictivesalary.getValeurRubriquePartage().setAmount(fictivesalary.getValeurRubriquePartage().getBase() * fictivesalary.getValeurRubriquePartage().getRates() / 30);
				fictivesalary.getWorkTimeFictif().setMontantCongePonctuel(fictivesalary.getValeurRubriquePartage().getAmount());
			}
		}
		// END IF;
		//
		// -- MM 11/09/2000
		// -- w_tau := PA_CALCUL.wnbjcp;
		fictivesalary.getValeurRubriquePartage().setRates(fictivesalary.getWorkTimeFictif().getNbreJourAbsencePourCongesPonctuels());
		//
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo23(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public boolean algo23(ClsFictifRubriqueClone rubrique)
	{
		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\n" + ">>algo23"+" Rubrique "+rubrique.getRubrique().getComp_id().getCrub();
		String cdos = fictivesalary.getInfoSalary().getComp_id().getCdos();
		String crub = rubrique.getRubrique().getComp_id().getCrub();
		double montant = 0;
		double taux = 0;
		double base = 0;

		double A23_inter1 = 0;
		//
		// CURSOR curs_barem8 IS
		// SELECT cdos , crub , val1 , val2 , NVL(taux, 0) , NVL(mont, 0)
		// FROM parubbarem
		// where identreprise = PA_CALCUL.wpdos.cdos
		// AND crub = PA_CALCUL.t_rub.crub
		// ORDER BY cdos, crub, nume;
		// RhprubriquePK t_rubPK = this.rubriquePKFromEngine;
		// String queryString = " from ElementSalaireBareme "
		// + " where identreprise = '" + cdos + "'"
		// + " and crub = '" + crub + "'"
		// + " order by cdos , crub , nume";
		//
		// CURSOR curs_barem82 IS
		// SELECT cdos , crub , val1 , val2 , NVL(taux, 0) , NVL(mont, 0)
		// FROM pahrubbarem
		// where identreprise = PA_CALCUL.wpdos.cdos
		// AND crub = PA_CALCUL.t_rub.crub
		// AND aamm = PA_CALCUL.w_aamm
		// AND nbul = PA_CALCUL.wsd_fcal1.nbul
		// ORDER BY cdos, crub, nume;
		// String queryStringRetro = " from Rhthrubbarem "
		// + " where identreprise = '" + cdos + "'"
		// + " and crub = '" + crub + "'"
		// + " and aamm = '" + fictivesalary.param.getMonthOfPay() + "'"
		// + " and nbul = " + fictivesalary.param.getNumeroBulletin()
		// + " order by cdos , crub , nume";
		//
		fictivesalary.getValeurRubriquePartage().setAmount(0);
		fictivesalary.getValeurRubriquePartage().setInter(fictivesalary.getValeurRubriquePartage().getBase());
		fictivesalary.getValeurRubriquePartage().setRates(0);
		//
		// -- Base <= 0 : Pas de calcul
		if (fictivesalary.getValeurRubriquePartage().getInter() <= 0)
			return true;
		// -- Abattement en pourcentage
		if (!ClsObjectUtil.isNull(rubrique.getRubrique().getPcab()))
		{
			base = fictivesalary.getValeurRubriquePartage().getBase();
			base = base * rubrique.getRubrique().getPcab().doubleValue() / 100;
			fictivesalary.getValeurRubriquePartage().setAbattement(base);
			fictivesalary.getValeurRubriquePartage().setInter(fictivesalary.getValeurRubriquePartage().getBase() - fictivesalary.getValeurRubriquePartage().getAbattement());
		}
		//
		// -- Division par le nombre de parts
		// -- LH 130899
		if ("O".equals(rubrique.getRubrique().getDnbp()))
		{
			if (ClsObjectUtil.isNull(fictivesalary.getInfoSalary().getNbpt()))
			{
				// logger
				fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90063", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id()
						.getCrub(), fictivesalary.getInfoSalary().getComp_id().getNmat()));
				fictivesalary.param.setPbWithCalulation(true);
				return false;
			}
			base = fictivesalary.getValeurRubriquePartage().getInter();
			base = base / fictivesalary.getInfoSalary().getNbpt().doubleValue();
			fictivesalary.getValeurRubriquePartage().setInter(base);
		}
		//
		// -- Abattement en montant
		base = rubrique.convertToNumber(rubrique.getRubrique().getAbat());
		if (fictivesalary.param.isPbWithCalulation())
		{
			return false;
		}
		fictivesalary.getValeurRubriquePartage().setPlafond(base);
		base = fictivesalary.getValeurRubriquePartage().getInter() - fictivesalary.getValeurRubriquePartage().getPlafond();
		fictivesalary.getValeurRubriquePartage().setInter(base);
		//
		// -- Base <= 0 : Pas de calcul
		if (fictivesalary.getValeurRubriquePartage().getInter() <= 0)
		{
			fictivesalary.getValeurRubriquePartage().setBase(0);
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
		// List listOfBarem = fictivesalary.param.isUseRetroactif() ? fictivesalary.getService().find(queryStringRetro) :
		// fictivesalary.getService().find(queryString);
		String keyOfBaremeList = fictivesalary.param.isUseRetroactif() ? crub + this.fictivesalary.param.getMonthOfPay() + this.fictivesalary.param.getNumeroBulletin() : crub;
		List listOfBarem = (List) fictivesalary.param.getListOfRubriquebaremeMap().get(keyOfBaremeList);
		if (listOfBarem != null)
			for (Object barem : listOfBarem)
			{
				if (fictivesalary.param.isUseRetroactif())
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
				if (fictivesalary.param.isPbWithCalulation())
				{
					return false;
				}
				dblValeur2 = rubrique.convertToNumber(valeur2);
				if (fictivesalary.param.isPbWithCalulation())
				{
					return false;
				}
				if (fictivesalary.getValeurRubriquePartage().getBase() >= dblValeur1 && fictivesalary.getValeurRubriquePartage().getBase() <= dblValeur2)
				{
					ok = true;
					fictivesalary.getValeurRubriquePartage().setAmount(montant);
					break;
				}
				if (fictivesalary.getValeurRubriquePartage().getInter() >= dblValeur1 && fictivesalary.getValeurRubriquePartage().getInter() <= dblValeur2)
				{
					ok = true;
					break;
				}
			}// foreach
		if (nbreBaremeRubrique == 0)
		{
			// logger
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90061", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(),
					rubrique.getRubrique().getComp_id().getCrub(), fictivesalary.getInfoSalary().getComp_id().getNmat()));
			return false;
		}
		double inter1 = 0;
		if (ok == true)
		{
			fictivesalary.getValeurRubriquePartage().setRates(taux);
			// w_inter1 := w_bas * w_tau / 100;
			inter1 = fictivesalary.getValeurRubriquePartage().getBase() * fictivesalary.getValeurRubriquePartage().getRates() / 100;
			//
			// -- LH 130899
			A23_inter1 = inter1;
			if ("O".equals(rubrique.getRubrique().getDnbp()))
				fictivesalary.getValeurRubriquePartage().setInter(montant * fictivesalary.getInfoSalary().getNbpt().doubleValue());
			else
				fictivesalary.getValeurRubriquePartage().setInter(montant);
			// -- LH 130899
			if (fictivesalary.param.getDossierInfo().getNddd() == 0)
				fictivesalary.getValeurRubriquePartage().setAmount(inter1 - fictivesalary.getValeurRubriquePartage().getInter());
			else
				fictivesalary.getValeurRubriquePartage().setAmount(A23_inter1 - fictivesalary.getValeurRubriquePartage().getInter());
		}
		else
		{
			// logger
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90064", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(),
					rubrique.getRubrique().getComp_id().getCrub(), fictivesalary.getInfoSalary().getComp_id().getNmat()));
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo24(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public boolean algo24(ClsFictifRubriqueClone rubrique)
	{
		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\n" + ">>algo24"+" Rubrique "+rubrique.getRubrique().getComp_id().getCrub();
		String cdos = fictivesalary.getInfoSalary().getComp_id().getCdos();
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
		// + " where identreprise = '" + cdos + "'"
		// + " and crub = '" + crub + "'"
		// + " order by cdos , crub , nume";
		// //
		// String queryStringRetro = " from Rhthrubbarem "
		// + " where identreprise = '" + cdos + "'"
		// + " and crub = '" + crub + "'"
		// + " and aamm = '" + fictivesalary.param.getMonthOfPay() + "'"
		// + " and nbul = " + fictivesalary.param.getNumeroBulletin()
		// + " order by cdos , crub , nume";
		//
		fictivesalary.getValeurRubriquePartage().setAmount(0);
		fictivesalary.getValeurRubriquePartage().setRates(0);
		//
		// -- Prorata sur periode
		// -- Recherche du nombre de mois travailles
		int periodRegu = 0;
		int nbrePeriod = 0;
		if (fictivesalary.param.getNbrePeriodeRegularisationEv() <= 0)
		{
			ClsDate oClsDate = new ClsDate(fictivesalary.param.getDossierInfo().getDdex(), this.fictivesalary.param.getAppDateFormat());
			c_cum99 = String.valueOf(oClsDate.getYear()) + "99";
			//
			// SELECT count(*) INTO per_regu FROM pacumu
			// where identreprise = PA_CALCUL.wpdos.cdos
			// AND nmat = PA_CALCUL.wsal01.nmat
			// AND aamm >= w_prem
			// AND aamm < w_aamm
			// AND aamm != c_cum99
			// AND rubq = PA_CALCUL.w_rubbrut
			// AND nbul = 9;
			//

			List l = fictivesalary.getService().find(
					"select count(*) from Rhtcumul" + " where identreprise = '" + cdos + "'" + " and nmat = '" + fictivesalary.getInfoSalary().getComp_id().getNmat() + "'"
							+ " and aamm >= '" + oClsDate.getYearAndMonth() + "'" + " and aamm < '" + oClsDate.getYearAndMonth() + "'" + " and aamm != '" + c_cum99 + "'"
							+ " and rubq = '" + fictivesalary.param.getBrutRubrique() + "'" + " and nbul = 9");
			if (!ClsObjectUtil.isListEmty(l))
			{
				periodRegu = ((Long) l.get(0)).intValue();
				fictivesalary.param.setNbrePeriodeRegularisationEv(periodRegu);
			}
			nbrePeriod = periodRegu + 1;
		}
		else
			nbrePeriod = fictivesalary.param.getNbrePeriodeRegularisationEv();
		//
		if (nbrePeriod != 0)
		{
			base = (base / nbrePeriod) * 12;
			fictivesalary.getValeurRubriquePartage().setBase(base);
		}
		fictivesalary.getValeurRubriquePartage().setInter(fictivesalary.getValeurRubriquePartage().getBase());
		if (fictivesalary.getValeurRubriquePartage().getInter() <= 0)
		{
			fictivesalary.getValeurRubriquePartage().setBase(0);
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
		if ((fictivesalary.getValeurRubriquePartage().getInter() - inter2) < inter3)
		{
			fictivesalary.getValeurRubriquePartage().setBase(0);
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
		// List listOfBarem = fictivesalary.param.isUseRetroactif() ? fictivesalary.getService().find(queryStringRetro) :
		// fictivesalary.getService().find(queryString);
		String keyOfBaremeList = fictivesalary.param.isUseRetroactif() ? crub + this.fictivesalary.param.getMonthOfPay() + this.fictivesalary.param.getNumeroBulletin() : crub;
		List listOfBarem = (List) fictivesalary.param.getListOfRubriquebaremeMap().get(keyOfBaremeList);
		if (listOfBarem != null)
			for (Object barem : listOfBarem)
			{
				if (fictivesalary.param.isUseRetroactif())
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
				if (fictivesalary.param.isPbWithCalulation())
				{
					return false;
				}
				dblValeur2 = rubrique.convertToNumber(valeur2);
				if (fictivesalary.param.isPbWithCalulation())
				{
					return false;
				}
				if (fictivesalary.getValeurRubriquePartage().getBase() >= dblValeur1 && fictivesalary.getValeurRubriquePartage().getBase() <= dblValeur2)
				{
					ok = true;
					fictivesalary.getValeurRubriquePartage().setAmount(montant);
					break;
				}
				if (fictivesalary.getValeurRubriquePartage().getInter() >= dblValeur1 && fictivesalary.getValeurRubriquePartage().getInter() <= dblValeur2)
				{
					ok = true;
					break;
				}
			}// foreach
		if (nbreBaremeRubrique == 0)
		{
			// logger
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90061", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(),
					rubrique.getRubrique().getComp_id().getCrub(), fictivesalary.getInfoSalary().getComp_id().getNmat()));
			return false;
		}
		if (ok == true)
		{
			fictivesalary.getValeurRubriquePartage().setRates(taux);
			// fixer le taux
			inter1 = fictivesalary.getValeurRubriquePartage().getBase() * fictivesalary.getValeurRubriquePartage().getRates() / 100;
			//
			if ("O".equals(rubrique.getRubrique().getDnbp()))
				fictivesalary.getValeurRubriquePartage().setInter(montant * fictivesalary.getInfoSalary().getNbpt().doubleValue());
			else
				fictivesalary.getValeurRubriquePartage().setInter(montant);
			// fixer le montant
			fictivesalary.getValeurRubriquePartage().setAmount(inter1 - fictivesalary.getValeurRubriquePartage().getInter());
		}
		//
		fictivesalary.getValeurRubriquePartage().setAmount((fictivesalary.getValeurRubriquePartage().getAmount() / 12) - fictivesalary.getValeurRubriquePartage().getInter());

		fictivesalary.getValeurRubriquePartage().setInter(0);
		ind_f = 0;
		if (!ClsObjectUtil.isNull(rubrique.getRubrique().getAbat()))
			ind_f = Integer.valueOf(rubrique.getRubrique().getAbat());
		if (ind_f > 0 && ind_f <= 9999)
			fictivesalary.getValeurRubriquePartage().setInter(rubrique.getAmount());
		if (fictivesalary.getValeurRubriquePartage().getAmount() > 0)
			fictivesalary.getValeurRubriquePartage().setAmount(fictivesalary.getValeurRubriquePartage().getAmount() - fictivesalary.getValeurRubriquePartage().getInter());
		else
			fictivesalary.getValeurRubriquePartage().setAmount(0);
		//
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo25(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public boolean algo25(ClsFictifRubriqueClone rubrique)
	{
		String cdos = fictivesalary.getInfoSalary().getComp_id().getCdos();
		String crub = rubrique.getRubrique().getComp_id().getCrub();
		double montant = 0;
		double taux = 0;
		double base = 0;
		String cleAccess = "";
		int tabl = 0;
		String libelle = "";
		// double w4 = 0;
		//
		fictivesalary.getValeurRubriquePartage().setRates(0);
		fictivesalary.getValeurRubriquePartage().setAmount(0);
		fictivesalary.getValeurRubriquePartage().setValeur(0);
		if (rubrique.getRubrique().getTabl() != null)
		{
			cleAccess = crub;
			tabl = Integer.valueOf(rubrique.getRubrique().getTabl());
			libelle = fictivesalary.utilNomenclatureFictif.getLabelFromNomenclature(cdos, tabl, cleAccess, 3, fictivesalary.param.getNlot(), fictivesalary.param.getNumeroBulletin(),
					this.fictivesalary.param.getMonthOfPay());
		}
		if (ClsObjectUtil.isNull(libelle))
		{
			// logger
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90056", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(),
					rubrique.getRubrique().getComp_id().getCrub(), fictivesalary.getInfoSalary().getComp_id().getNmat(), cleAccess, rubrique.getRubrique().getTabl()));
			return false;
		}
		tempNumber = null;
		if (rubrique.getRubrique().getTabl() != null)
		{
			tempNumber = fictivesalary.utilNomenclatureFictif.getAmountOrRateFromNomenclature(fictivesalary.getInfoSalary().getComp_id().getCdos(), tabl, cleAccess, 1, fictivesalary.param.getNlot(),
					fictivesalary.param.getNumeroBulletin(), fictivesalary.param.getMoisPaieCourant(), ClsEnumeration.EnColumnToRead.AMOUNT);
		}
		if (tempNumber == null)
		{
			// logger
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90056", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(),
					rubrique.getRubrique().getComp_id().getCrub(), fictivesalary.getInfoSalary().getComp_id().getNmat(), cleAccess, rubrique.getRubrique().getTabl()));
			return false;
		}

		if (tempNumber.intValue() == 0)
		{
			// logger
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90065", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(),
					rubrique.getRubrique().getComp_id().getCrub(), fictivesalary.getInfoSalary().getComp_id().getNmat(), cleAccess, rubrique.getRubrique().getTabl()));
			return false;
		}
		montant = tempNumber.doubleValue();
		char char1 = libelle.toCharArray()[0];
		if (char1 != 'B' && char1 != 'T' && char1 != 'M')
		{
			// logger
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90066", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(),
					rubrique.getRubrique().getComp_id().getCrub(), fictivesalary.getInfoSalary().getComp_id().getNmat(), crub, rubrique.getRubrique().getTabl()));
			return false;
		}
		// prendre les valeurs de taux, montant et base associées é la rubrique correspondant au libelle
		// ClsFictifRubriqueClone rubriqueTmp = fictivesalary.findRubriqueCloneFictif(String.valueOf(libelle));
		String newCrub = ClsStringUtil.formatNumber(montant, ParameterUtil.formatRubrique);
		ClsFictifRubriqueClone rubriqueTmp = fictivesalary.findRubriqueCloneFictif(newCrub);

		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\n" + "rubrique trouvée = " + rubriqueTmp;
		if (char1 == 'B')
			fictivesalary.getValeurRubriquePartage().setRates(rubriqueTmp.getBasePlafonnee());
		else if (char1 == 'T')
			fictivesalary.getValeurRubriquePartage().setRates(rubriqueTmp.getRates());
		else if (char1 == 'M')
			fictivesalary.getValeurRubriquePartage().setRates(rubriqueTmp.getAmount());
		//
		if (fictivesalary.getValeurRubriquePartage().getRates() != 0)
		{
			if ("T".equals(rubrique.getRubrique().getToum()))
			{
				// w_mon := w_bas * w_tau / 100;
				taux = fictivesalary.getValeurRubriquePartage().getRates();
				base = fictivesalary.getValeurRubriquePartage().getBase();
				fictivesalary.getValeurRubriquePartage().setAmount(taux * base / 100);
			}
			else if ("M".equals(rubrique.getRubrique().getToum()))
			{
				// w_mon := w_bas * w_tau;
				taux = fictivesalary.getValeurRubriquePartage().getRates();
				base = fictivesalary.getValeurRubriquePartage().getBase();
				fictivesalary.getValeurRubriquePartage().setAmount(taux * base);
			}
			else if ("D".equals(rubrique.getRubrique().getToum()))
			{
				// w_mon := w_bas / w_tau;
				taux = fictivesalary.getValeurRubriquePartage().getRates();
				base = fictivesalary.getValeurRubriquePartage().getBase();
				if (taux != 0)
					fictivesalary.getValeurRubriquePartage().setAmount(base / taux);
			}
			else
			{
				// logger
				fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90058", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(), fictivesalary.getInfoSalary()
						.getComp_id().getNmat(), rubrique.getRubrique().getComp_id().getCrub()));
				if ('O' == fictivesalary.param.getGenfile())
					outputtext += "\n" + fictivesalary.param.getError();
				return false;
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo26(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public boolean algo26(ClsFictifRubriqueClone rubrique)
	{
		// String cdos = fictivesalary.getInfoSalary().getComp_id().getCdos();
		// String crub = rubrique.getRubrique().getComp_id().getCrub();
		double montant = 0;
		// double taux = 0;
		// double base = 0;
		// String cleAccess = "";
		// int tabl = 0;
		// String libelle = "";
		double div_nbjs = 0;
		//
		fictivesalary.getValeurRubriquePartage().setRates(0);
		fictivesalary.getValeurRubriquePartage().setAmount(0);
		fictivesalary.getValeurRubriquePartage().setValeur(0);
		//
		// -- Pas de calcul si pas de base ( Base := Montant du conges normal )
		// -- Pas de calcul si pas de jours supp
		if (fictivesalary.getValeurRubriquePartage().getBase() == 0
				|| ((ClsObjectUtil.isNull(fictivesalary.getInfoSalary().getNbjsa())) && (ClsObjectUtil.isNull(fictivesalary.getInfoSalary().getNbjse()))))
			return true;
		//
		// -- Stc - Calcul du montant
		if (fictivesalary.param.isStc())
		{
			fictivesalary.getValeurRubriquePartage().setRates(fictivesalary.getInfoSalary().getNbjse().doubleValue() + fictivesalary.getInfoSalary().getNbjsa().doubleValue());
			div_nbjs = (fictivesalary.getInfoSalary().getJapa().doubleValue() + fictivesalary.getInfoSalary().getJapec().doubleValue()) - fictivesalary.getValeurRubriquePartage().getRates()
					- fictivesalary.getInfoSalary().getJded().doubleValue();
			// w_mon := w_bas * w_tau / div_nbjs;
			if (div_nbjs != 0)
			{
				montant = fictivesalary.getValeurRubriquePartage().getBase() * fictivesalary.getValeurRubriquePartage().getRates() / div_nbjs;
				fictivesalary.getValeurRubriquePartage().setAmount(montant);
			}
		}
		// ELSE
		else
		{
			fictivesalary.getValeurRubriquePartage().setRates(fictivesalary.getInfoSalary().getNbjse().doubleValue() + fictivesalary.getInfoSalary().getNbjsa().doubleValue());
			// div_nbjs := PA_CALCUL.wsal01.nbjcf;
			div_nbjs = fictivesalary.getInfoSalary().getNbjcf().doubleValue();
			// w_mon := w_bas * w_tau / div_nbjs;
			if (div_nbjs != 0)
			{
				montant = fictivesalary.getValeurRubriquePartage().getBase() * fictivesalary.getValeurRubriquePartage().getRates() / div_nbjs;
				fictivesalary.getValeurRubriquePartage().setAmount(montant);
			}
		}
		//
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo27(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public boolean algo27(ClsFictifRubriqueClone rubrique)
	{
		String cdos = fictivesalary.getInfoSalary().getComp_id().getCdos();
		// String crub = rubrique.getRubrique().getComp_id().getCrub();
		Double montant = new Double(0);
		// double taux = 0;
		double base = 0;
		double totalNbreJourSuppl = 0;
		int diviseurNbreJourSuppl = 0;
		//
		fictivesalary.getValeurRubriquePartage().setRates(0);
		fictivesalary.getValeurRubriquePartage().setAmount(0);
		fictivesalary.getValeurRubriquePartage().setValeur(0);
		//
		if (fictivesalary.getValeurRubriquePartage().getBase() == 0)
		{
			// logger
			return true;
		}
		if (ClsObjectUtil.isNull(fictivesalary.getInfoSalary().getDrtcg()) && ClsObjectUtil.isNull(fictivesalary.getInfoSalary().getDtes()))
		{
			// logger
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90067", fictivesalary.param.getLangue(), fictivesalary.getInfoSalary().getComp_id().getNmat()));
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + fictivesalary.param.getError();
			return false;
		}
		// -- Verification de la coherence de la date de retour conges annuels
		Date oDate = new ClsDate("01-01-1990", "dd-MM-yyyy").getDate();
		if ((fictivesalary.getInfoSalary().getDrtcg()!=null) && (fictivesalary.getInfoSalary().getDrtcg().getTime() < oDate.getTime()))
		{
			// logger
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90068", fictivesalary.param.getLangue(), fictivesalary.getInfoSalary().getComp_id().getNmat(), new ClsDate(fictivesalary
					.getInfoSalary().getDrtcg()).getDateS()));
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + fictivesalary.param.getError();
			return false;
		}
		Date date_retcg = null;
		Date date_debcg = null;
		
		String ddcf = null;
		if (fictivesalary.getInfoSalary().getDdcf() != null)
			ddcf = "'" + new ClsDate(fictivesalary.getInfoSalary().getDdcf()).getDateS(fictivesalary.param.appDateFormat) + "'";
		
		String requete = !fictivesalary.param.isUseRetroactif() ? 
				"select max(ddcg) from Rhtcongeagent " + " where identreprise = '" + cdos + "'" + " and nmat = '" + fictivesalary.getInfoSalary().getComp_id().getNmat() + "'"
						+ " and ddcg < " + ddcf+ "" + " and cmcg in (select a.cacc from ParamData a, ParamData b "
						+ " where b.cacc = a.cacc " + " and b.ctab = b.ctab" + " and b.cdos = '" + cdos + "'" + " and a.cdos = '" + cdos + "'"
						+ " and a.ctab = 22 " + " and b.ctab = 22 " + " and a.nume = 1 " + " and b.nume = 3 " + " and a.valm = 1 " + " and b.valm = 0 )"
				: 
						"select max(ddcg) from Rhtcongeagent " + " where identreprise = '" + cdos + "'" + " and nmat = '" + fictivesalary.getInfoSalary().getComp_id().getNmat() + "'"
								+ " and ddcg < " + ddcf  + "" + " and cmcg in (select a.cacc from Rhthfnom a, Rhthfnom b "
								+ " where b.cacc = a.cacc " + " and b.ctab = b.ctab" + " and b.cdos = '" + cdos + "'" + " and a.cdos = '" + cdos + "'"
								+ " and a.ctab = 22 " + " and b.ctab = 22 " + " and a.nume = 1 " + " and b.nume = 3 " + " and a.valm = 1 " + " and b.valm = 0 )";
		Session session = fictivesalary.getService().getSession();
		Query q = session.createSQLQuery(requete);
		
		List l = q.list();
		fictivesalary.getService().closeConnexion(session);
				
		if (!ClsObjectUtil.isListEmty(l))
		{
			if (!ClsObjectUtil.isNull(l.get(0)))
				date_retcg = (Date) l.get(0);
		}
		if (date_retcg == null)
		{
			date_retcg = ClsObjectUtil.isNull(fictivesalary.getInfoSalary().getDrtcg()) ? fictivesalary.getInfoSalary().getDtes() : fictivesalary.getInfoSalary().getDrtcg();
		}
		// -- Determination de la date de debut de conges
		// -- MM 12/10/2000 si STC date deb = date radiation
		date_debcg = fictivesalary.param.isStc() ? fictivesalary.getInfoSalary().getDmrr() : fictivesalary.getInfoSalary().getDdcf();
		date_debcg = ClsObjectUtil.isNull(date_debcg) ? date_retcg : new ClsDate(date_debcg).addDay(-1);
		//
		// -- Calcul du nombre de mois ecoule depuis le dernier conges
		if (date_debcg.getTime() < date_retcg.getTime())
		{
			// logger
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90071", fictivesalary.param.getLangue(), fictivesalary.getInfoSalary().getComp_id().getNmat()));
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + fictivesalary.param.getError();
			return false;
		}
		double nbreDeMoisTr = ClsDate.getMonthsBetween(date_debcg, date_retcg);
//		if(fictivesalary.param.nomClient.equalsIgnoreCase(ClsEntreprise.COMILOG)) 
//			nbreDeMoisTr = ClsStringUtil.truncateToXDecimal(new ClsFictifAlgorithmComilog(fictivesalary).getMonthsBetween(date_debcg, date_retcg),2).doubleValue();
		// -- Récupération du nombre de jrs de delai de route
		tempNumber = fictivesalary.utilNomenclatureFictif.getAmountOrRateFromNomenclature(cdos, 99, "DELAI-RTE", 1, fictivesalary.param.getNlot(), fictivesalary.param.getNumeroBulletin(),
				fictivesalary.param.getMoisPaieCourant(), ClsEnumeration.EnColumnToRead.AMOUNT);
		String rubriqueDelaiDeRoute = ClsStringUtil.formatNumber(tempNumber == null ? 0 : tempNumber.doubleValue(), ParameterUtil.formatRubrique);
		int nbreJoursDelai = 0;
		tempNumber = fictivesalary.utilNomenclatureFictif.getAmountOrRateOfFixElement(cdos, fictivesalary.getInfoSalary().getComp_id().getNmat(), fictivesalary.param.getMonthOfPay(),
				rubriqueDelaiDeRoute, fictivesalary.param.getNumeroBulletin());
		nbreJoursDelai = tempNumber == null ? 0 : tempNumber.intValue();
		// -- Stc - Calcul du montant
		if (fictivesalary.param.isStc())
		{
			// -- w_tau := PA_CALCUL.wsal01.nbjse + PA_CALCUL.wsal01.nbjsa;
			totalNbreJourSuppl = fictivesalary.calculNombreDJourSuppl();
			// IF PA_CALCUL.wsal01.sexe = 'F' THEN
			if ("F".equals(fictivesalary.getInfoSalary().getSexe()))
				totalNbreJourSuppl -= fictivesalary.getNbreJourPourEnfant() + fictivesalary.getInfoSalary().getNbjse().intValue();
			else
				totalNbreJourSuppl = totalNbreJourSuppl - fictivesalary.getNbreJourPourEnfant();
			// -- Prorata sur nb mois travaillés donne nb jrs supp ouvrables
			totalNbreJourSuppl = totalNbreJourSuppl * nbreDeMoisTr / 12;
			// -- Calcul du nb jrs supp en calendaire
			totalNbreJourSuppl = totalNbreJourSuppl * 7 / 6;
			if (totalNbreJourSuppl - Math.floor(totalNbreJourSuppl) > 0)
				fictivesalary.getValeurRubriquePartage().setRates(Math.floor(totalNbreJourSuppl) + 1);
			else
				fictivesalary.getValeurRubriquePartage().setRates(Math.floor(totalNbreJourSuppl));
			// -- MM 25/09/2002 les jrs deduct (ponctuels) . a la COMILOG sont inclus dans les jrs de droits.
			if(fictivesalary.getInfoSalary().getJapa() == null) fictivesalary.getInfoSalary().setJapa(BigDecimal.ZERO);
			if(fictivesalary.getInfoSalary().getJapec() == null) fictivesalary.getInfoSalary().setJapec(BigDecimal.ZERO);
			diviseurNbreJourSuppl = fictivesalary.getInfoSalary().getJapa().intValue() + fictivesalary.getInfoSalary().getJapec().intValue();
			fictivesalary.getValeurRubriquePartage().setBasePlafonnee(fictivesalary.getValeurRubriquePartage().getBase());
			base = fictivesalary.getValeurRubriquePartage().getBase() * fictivesalary.getValeurRubriquePartage().getRates() / diviseurNbreJourSuppl;
			fictivesalary.getValeurRubriquePartage().setAmount(base);
			// -- Fin modif MM.
		}
		else
		{
			// -- w_tau := PA_CALCUL.wsal01.nbjse + PA_CALCUL.wsal01.nbjsa;
			totalNbreJourSuppl = fictivesalary.calculNombreDJourSuppl();
			if ("F".equals(fictivesalary.getInfoSalary().getSexe()))
				totalNbreJourSuppl = totalNbreJourSuppl - fictivesalary.getNbreJourPourEnfant() + fictivesalary.getInfoSalary().getNbjse().intValue();
			else
				totalNbreJourSuppl = totalNbreJourSuppl - fictivesalary.getNbreJourPourEnfant();
			// END IF;
			// -- Prorata sur nb mois travaillés donne nb jrs supp ouvables
			totalNbreJourSuppl = totalNbreJourSuppl * nbreDeMoisTr / 12;
			//
			// -- Calcul du nb jrs supp en calendaire
			totalNbreJourSuppl = totalNbreJourSuppl * 7 / 6;
			if (totalNbreJourSuppl - Math.floor(totalNbreJourSuppl) > 0)
				fictivesalary.getValeurRubriquePartage().setRates(Math.floor(totalNbreJourSuppl) + 1);
			else
				fictivesalary.getValeurRubriquePartage().setRates(Math.floor(totalNbreJourSuppl));
			//
			//
			// -- MM 25/09/2002 les jrs deduct (ponctuels) . a la COMILOG sont inclus dans les jrs de droits.
			// -- div_nbjs := PA_CALCUL.wsal01.nbjaf - w_tau - nbj_delai;
			if(fictivesalary.getInfoSalary().getJded() == null) fictivesalary.getInfoSalary().setJded(BigDecimal.ZERO);
			diviseurNbreJourSuppl = new Double((fictivesalary.getInfoSalary().getNbjaf().intValue() + fictivesalary.getInfoSalary().getJded().intValue())
					- fictivesalary.getValeurRubriquePartage().getRates() - nbreJoursDelai).intValue();
			// -- w_mon := w_bas * w_tau / div_nbjs;
			fictivesalary.getValeurRubriquePartage().setBasePlafonnee(fictivesalary.getValeurRubriquePartage().getBase());
			montant = fictivesalary.getValeurRubriquePartage().getBase() * fictivesalary.getValeurRubriquePartage().getRates() / diviseurNbreJourSuppl;
			fictivesalary.getValeurRubriquePartage().setAmount(montant);
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo28(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public boolean algo28(ClsFictifRubriqueClone rubrique)
	{
		String cdos = fictivesalary.getInfoSalary().getComp_id().getCdos();
		String crub = rubrique.getRubrique().getComp_id().getCrub();
		Double montant = new Double(0);
		// double taux = 0;
		// double base = 0;
		double valeur = 0;
		int status = 0;
		//
		fictivesalary.getValeurRubriquePartage().setRates(0);
		fictivesalary.getValeurRubriquePartage().setAmount(0);
		fictivesalary.getValeurRubriquePartage().setValeur(0);
		//
		tempNumber = null;
		if (!ClsObjectUtil.isListEmty(rubrique.getListOfElementVariable()))
		{
			Object[] obj = (Object[]) rubrique.getListOfElementVariable().get(rubrique.getNumElementVarCourant());
			if (obj != null && obj[0] != null)
				tempNumber = (new BigDecimal( obj[0].toString())).doubleValue();
			else
			{
				tempNumber = fictivesalary.utilNomenclatureFictif.getAmountOrRateOfFixElement(cdos, fictivesalary.getInfoSalary().getComp_id().getNmat(), fictivesalary.param.getMonthOfPay(), crub,
						fictivesalary.param.getNumeroBulletin());
				if (tempNumber == null)
					status = 1;
			}
			// tempNumber = rubrique.getAmount();
		}
		else
		{
			tempNumber = fictivesalary.utilNomenclatureFictif.getAmountOrRateOfFixElement(cdos, fictivesalary.getInfoSalary().getComp_id().getNmat(), fictivesalary.param.getMonthOfPay(), crub,
					fictivesalary.param.getNumeroBulletin());
			if (tempNumber == null)
				status = 1;
		}
		valeur = tempNumber == null ? 0 : tempNumber.doubleValue();
		fictivesalary.getValeurRubriquePartage().setValeur(valeur);
		//
		if (status == 0)
		{
			if ("T".equals(rubrique.getRubrique().getToum()))
			{
				fictivesalary.getValeurRubriquePartage().setRates(valeur);
				montant = fictivesalary.getValeurRubriquePartage().getBase() * fictivesalary.getValeurRubriquePartage().getRates() / 100;
				fictivesalary.getValeurRubriquePartage().setAmount(montant);
			}
			else if ("M".equals(rubrique.getRubrique().getToum()))
			{
				fictivesalary.getValeurRubriquePartage().setRates(valeur);
				montant = fictivesalary.getValeurRubriquePartage().getBase() * fictivesalary.getValeurRubriquePartage().getRates();
				fictivesalary.getValeurRubriquePartage().setAmount(montant);
			}
			else if ("D".equals(rubrique.getRubrique().getToum()))
			{
				if (valeur != 0)
				{
					fictivesalary.getValeurRubriquePartage().setRates(valeur);
					montant = fictivesalary.getValeurRubriquePartage().getBase() / fictivesalary.getValeurRubriquePartage().getRates();
					fictivesalary.getValeurRubriquePartage().setAmount(montant);
				}
				else
					fictivesalary.getValeurRubriquePartage().setAmount(0);
			}
			else
			{
				// logger
				fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90058", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(), fictivesalary.getInfoSalary()
						.getComp_id().getNmat(), rubrique.getRubrique().getComp_id().getCrub()));
				if ('O' == fictivesalary.param.getGenfile())
					outputtext += "\n" + fictivesalary.param.getError();
				return false;
			}
		}
		//
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo3(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public boolean algo3(ClsFictifRubriqueClone rubrique)
	{
		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\n" + ">>algo3"+" Rubrique "+rubrique.getRubrique().getComp_id().getCrub();

		int MonthOfDtes = 0, monthOfPay = 0;
		double val = 0;
		double amountOrRateValue = 0;
		fictivesalary.getValeurRubriquePartage().setRates(0);
		fictivesalary.getValeurRubriquePartage().setAmount(0);
		//
		if (this.fictivesalary.getAnciennete() == 0)
			return true;
		MonthOfDtes = new ClsDate(this.fictivesalary.getInfoSalary().getDtes()).getMonth();
		monthOfPay = new ClsDate(this.fictivesalary.param.getMonthOfPay(), ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getMonth();
		// ce n'est pas le mois anniversaire d'entrée dans la société pour cet employé
		if (MonthOfDtes != monthOfPay)
			return true;
		String formattedMonth = ClsStringUtil.formatNumber(this.fictivesalary.getAnciennete(), "00");
		//
		String RateOrAmount = rubrique.getRubrique().getToum();
		ClsEnumeration.EnColumnToRead colToRead = "T".equals(RateOrAmount) ? ClsEnumeration.EnColumnToRead.RATES : ClsEnumeration.EnColumnToRead.AMOUNT;
		int tabl = 0;
		tempNumber = null;
		if (rubrique.getRubrique().getTabl() != null)
		{
			tabl = Integer.valueOf(rubrique.getRubrique().getTabl());
			tempNumber = this.fictivesalary.utilNomenclatureFictif.getAmountOrRateFromNomenclature(this.fictivesalary.getInfoSalary().getComp_id().getCdos(), tabl, formattedMonth, rubrique
					.getRubrique().getNutm(), this.fictivesalary.param.getNlot(), this.fictivesalary.param.getNumeroBulletin(), this.fictivesalary.param.getMonthOfPay(), colToRead);
		}
		if (tempNumber == null)
		{
			fictivesalary.param.setPbWithCalulation(true);
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90056", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(),
					rubrique.getRubrique().getComp_id().getCrub(), fictivesalary.getInfoSalary().getComp_id().getNmat(), formattedMonth, rubrique.getRubrique().getTabl()));
			return false;
		}
		amountOrRateValue = tempNumber.doubleValue();
		fictivesalary.getValeurRubriquePartage().setValeur(amountOrRateValue);
		if ("M".equals(rubrique.getRubrique().getToum()))
		{
			fictivesalary.getValeurRubriquePartage().setRates(0);
			fictivesalary.getValeurRubriquePartage().setAmount(amountOrRateValue);
		}
		else if ("T".equals(rubrique.getRubrique().getToum()))
		{
			fictivesalary.getValeurRubriquePartage().setRates(amountOrRateValue);
			val = fictivesalary.getValeurRubriquePartage().getBase() * fictivesalary.getValeurRubriquePartage().getRates() / 100;
			fictivesalary.getValeurRubriquePartage().setAmount(val);
		}
		else if ("D".equals(rubrique.getRubrique().getToum()))
		{
			fictivesalary.getValeurRubriquePartage().setRates(amountOrRateValue);
			val = fictivesalary.getValeurRubriquePartage().getBase() * fictivesalary.getValeurRubriquePartage().getRates();
			fictivesalary.getValeurRubriquePartage().setAmount(val);
		}
		else
		{
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90058", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(), fictivesalary.getInfoSalary().getComp_id()
					.getNmat(), rubrique.getRubrique().getComp_id().getCrub()));
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo30(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public boolean algo30(ClsFictifRubriqueClone rubrique)
	{
		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\nin algo=> 30"+" Rubrique "+rubrique.getRubrique().getComp_id().getCrub();

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
		// where identreprise = PA_CALCUL.wpdos.cdos
		// AND nmat = PA_CALCUL.wsal01.nmat
		// ORDER BY cdos, nmat, nlig;
		// numero de ligne pour la banque principale
		Integer nligPrincipal = -1;

		// String queryString = "select nlig, bqag, pourc, mont, dvd, txchg, mntdb, mntdvd, aamm, princ, guic, comp from VirementSalarie " + " where identreprise =
		// '"
		// + fictivesalary.getInfoSalary().getComp_id().getCdos() + "'" + " and nmat = '" + fictivesalary.getInfoSalary().getComp_id().getNmat() + "'" + " order
		// by cdos,
		// nmat, nlig";
		String queryString = "From VirementSalarie " + " where identreprise = '" + fictivesalary.getInfoSalary().getComp_id().getCdos() + "'" + " and nmat = '"
				+ fictivesalary.getInfoSalary().getComp_id().getNmat() + "'" + " order by cdos, nmat, nlig";
		//
		// BEGIN
		//
		// -- Initialisations
		// w_tau := 0;
		// w_mon := 0;
		// Ref_TB10_Pal := 0;
		fictivesalary.getValeurRubriquePartage().setRates(0);
		fictivesalary.getValeurRubriquePartage().setAmount(0);
		int ref_tb10_pal = 0;
		//
		// -- Controle si agent paye par virement
		// IF paf_TypePaiement(PA_CALCUL.wsal01.nmat) != 'V'
		// THEN
		// RETURN TRUE;
		// END IF;
		//
		String typa = fictivesalary.getTypePaiement();
		if (!"V".equals(typa))
		{
			return true;
		}
		
		
//		if(StringUtils.equals(fictivesalary.param.getNomClient(), ClsEntreprise.COMILOG))
//		{
//			if(!fictivesalary.param.fictiveCalculusB)
//				return true;
//		}
		// -- La base contient le net a payer
		// IF PA_PAIE.NouZ( w_bas )
		// THEN
		double montantarepartir = 0;
		double montantrestant = 0;
		double montantbanquedvd = 0;
		double montantbanquedb = 0;
		double arrondidossier = 0;
		double arrondibanque = 0;
		if (fictivesalary.getValeurRubriquePartage().getBase() == 0)
		{
			// -- * Modification AB le 03/09/1999 * --
			// -- * Ajout de la mise é jour de PAVRMT quand base = 0 *--
			// BEGIN
			// UPDATE pavrmt
			// SET mntdb = 0,
			// mntdvd = 0
			// where identreprise = PA_CALCUL.wpdos.cdos
			// AND nmat = PA_CALCUL.wsal01.nmat
			// AND aamm = w_aamm;
			// EXCEPTION
			// WHEN OTHERS THEN
			// PA_CALCUL.err_msg := PA_PAIE.erreurp('ERR-90072',PA_CALCUL.w_clang,PA_CALCUL.t_rub.algo,PA_CALCUL.wsal01.nmat);
			// RETURN FALSE;
			// END;
			String updateString = "update VirementSalarie" + " set mntdb = 0, mntdvd = 0" + " where identreprise = '" + fictivesalary.getInfoSalary().getComp_id().getCdos() + "'" + " and nmat = '"
					+ fictivesalary.getInfoSalary().getComp_id().getNmat() + "'";
			// fictivesalary.getService().bulkUpdate(updateString);
			try
			{
				if ('O' == fictivesalary.param.getGenfile())
					outputtext += "\n" + "...................Updating vrmtagent";
				fictivesalary.getService().updateFromTable(updateString);
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				fictivesalary.param.setError(ClsTreater._getStackTrace(e));
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
			montantarepartir = fictivesalary.getValeurRubriquePartage().getBase();
			// Mnt_restant := w_bas;
			montantrestant = fictivesalary.getValeurRubriquePartage().getBase();
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

		// String queryString1 = "From VirementSalarie" ;
		// List l1 = fictivesalary.getService().find(queryString1);
		// for (int i = 0; i < l1.size(); i++) {
		// if('O' == fictivesalary.param.getGenfile()) outputtext += "\n" + "-------------------->"+l1.get(i));
		// }

		List l = fictivesalary.getService().find(queryString);
		VirementSalarie virement = null;
		ClsInfoOfBank bankRef = null;
		int idxTB10 = 0;
		int refTB10 = 0;
		Object[] array = null;
		// LOOP
		for (Object obj : l)
		{
			// virement = (VirementSalarie)obj;
			// nlig, bqag, pourc, mont, dvd, txchg, mntdb, mntdvd, aamm, princ
			// virement = new VirementSalarie();
			virement = (VirementSalarie) obj;
			// array = (Object[]) obj;
			// virement.setComp_id(new VirementSalariePK());
			// virement.getComp_id().setCdos(fictivesalary.getInfoSalary().getComp_id().getCdos());
			// virement.getComp_id().setNmat(fictivesalary.getInfoSalary().getComp_id().getNmat());
			// virement.getComp_id().setNlig((Integer) array[0]);
			// virement.setBqag(array[1].toString());
			// virement.setPourc((Integer) array[2]);
			// virement.setMont((BigDecimal) array[3]);
			// virement.setDvd(array[4].toString());
			// virement.setTxchg((BigDecimal) array[5]);
			// virement.setMntdb((BigDecimal) array[6]);
			// virement.setMntdvd((BigDecimal) array[7]);
			// virement.setAamm(array[8] != null ? array[8].toString() : null);
			// virement.setPrinc(array[9].toString());
			// virement.setGuic(array[10].toString());
			// virement.setComp(array[11].toString());
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
			for (ClsInfoOfBank bank : fictivesalary.param.getListOfBank())
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
				String error = fictivesalary.param.errorMessage("ERR-90073", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(), fictivesalary.getInfoSalary().getComp_id().getNmat(),
						rubrique.getRubrique().getComp_id().getCrub(), virement.getBqag());
				// fictivesalary.param.insererLogMessage(error);
				fictivesalary.param.setError(error);
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
				nligPrincipal = virement.getNlig();
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
				String error = fictivesalary.param.errorMessage("ERR-90074", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(), fictivesalary.getInfoSalary().getComp_id().getNmat(),
						rubrique.getRubrique().getComp_id().getCrub(), virement.getBqag());
				// fictivesalary.param.insererLogMessage(error);
				fictivesalary.param.setError(error);
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
			if (bankRef.isForeignBank())
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
			// if (fictivesalary.param.getDossierNbreDecimale() != 0)
			arrondidossier = fictivesalary.param.getDossierNbreDecimale() == 0 ? 1 : 1 / (10 * fictivesalary.param.getDossierNbreDecimale());

			// IF PA_CALCUL.TB10_Nb_Dec( Ref_TB10 ) = 0
			// THEN
			// Arrondi_Banque := 1;
			// ELSE
			// Arrondi_Banque := 1 / ( 10 * PA_CALCUL.TB10_Nb_Dec( Ref_TB10 ) );
			// END IF;
			// if (bankRef.getNbreDecimal() != 0)
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
				fictivesalary.getValeurRubriquePartage().setAmount(fictivesalary.getValeurRubriquePartage().getAmount() + montantbanquedvd);
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
			virement.setAamm(fictivesalary.param.getMonthOfPay());
			//
			try
			{
				if ('O' == fictivesalary.param.getGenfile())
					outputtext += "\n" + "Vrmt to update = " + virement.toString();
				fictivesalary.getService().update(virement);
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
				String error = fictivesalary.param.errorMessage("ERR-90076", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(), fictivesalary.getInfoSalary().getComp_id().getNmat(),
						rubrique.getRubrique().getComp_id().getCrub());
				// fictivesalary.param.insererLogMessage(error);
				fictivesalary.param.setError(error);
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

			if (bankRef.isForeignBank())
			{
				if (bankRef.getExchangeRate() != 0)
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
			// PA_CALCUL.err_msg :=
			// PA_PAIE.erreurp('ERR-90077',PA_CALCUL.w_clang,PA_CALCUL.t_rub.algo,PA_CALCUL.wsal01.nmat,PA_CALCUL.t_rub.crub,PA_CALCUL.TB10_Banque( Ref_TB10_Pal
			// ));
			// w_retour := Ins_palog( PA_CALCUL.err_msg );
			// RETURN FALSE;
			// END;
			// ajout yannick
			virement = (VirementSalarie) fictivesalary.getService().find("FROM VirementSalarie WHERE identreprise="+fictivesalary.getInfoSalary().getComp_id().getCdos()+" AND nmat='"+fictivesalary.getInfoSalary().getComp_id().getNmat()
																		+"' AND nlig="+nligPrincipal.intValue());
			// ////////fin ajout
			virement.setTxchg(new BigDecimal(bankRef.getExchangeRate()));
			double a = virement.getMntdb() == null ? 0 : virement.getMntdb().doubleValue();
			virement.setMntdb(ClsStringUtil.truncateTo3Decimal(new BigDecimal(a + montantbanquedb)));
			a = virement.getMntdvd() == null ? 0 : virement.getMntdvd().doubleValue();
			virement.setMntdvd(ClsStringUtil.truncateTo3Decimal(new BigDecimal(a + montantbanquedvd)));
			virement.setAamm(fictivesalary.param.getMonthOfPay());
			//
			try
			{
				fictivesalary.getService().update(virement);
			}
			catch (DataAccessException e1)
			{
				String error = fictivesalary.param.errorMessage("ERR-90077", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(), fictivesalary.getInfoSalary().getComp_id().getNmat(),
						rubrique.getRubrique().getComp_id().getCrub());
				// fictivesalary.param.insererLogMessage(error);
				fictivesalary.param.setError(error);
				return false;
			}

			/** ****par yannick ************** */
			// String updateString = "update VirementSalarie set txchg = " + virement.getTxchg() + ",mntdb = mntdb + " + virement.getMntdb() + ",mntdvd = mntdvd +
			// " + virement.getMntdvd() + ",aamm ="
			// + fictivesalary.param.getMonthOfPay();
			// updateString += " where identreprise = '" + fictivesalary.getInfoSalary().getComp_id().getCdos() + "'" + " and nmat = '" +
			// fictivesalary.getInfoSalary().getComp_id().getNmat() + "' and nlig=" + nligPrincipal;
			// try
			// {
			// fictivesalary.getService().updateFromTable(updateString);
			// }
			// catch (SQLException e)
			// {
			// e.printStackTrace();
			// }
		}
		// END IF;
		//
		// RETURN TRUE;
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo31(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public boolean algo31(ClsFictifRubriqueClone rubrique)
	{
//		if(StringUtils.equals(fictivesalary.param.getNomClient(), ClsEntreprise.COMILOG))
//			return comilog.algo31(rubrique);
		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\n" + ">>algo31"+" Rubrique "+rubrique.getRubrique().getComp_id().getCrub();
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
		String libelle2 = fictivesalary.utilNomenclatureFictif.getLabelFromNomenclature(fictivesalary.getInfoSalary().getComp_id().getCdos(), 99, "ALGO-31", 2, fictivesalary.param.getNlot(),
				fictivesalary.param.getNumeroBulletin(), this.fictivesalary.param.getMonthOfPay());
		if (ClsObjectUtil.isNull(libelle2))
		{
			type_nbj = "A";
		}
		else
		{
			type_nbj = StringUtils.substring(libelle2, 0, 1);
		}
		//
		// diviseur := paf_lecfnomM(99,'ALGO-31',1,PA_CALCUL.w_aamm,PA_CALCUL.w_nlot,PA_CALCUL.wsd_fcal1.nbul);
		// IF PA_PAIE.NouZ(diviseur) THEN
		// diviseur := 30;
		// END IF;
		tempNumber = fictivesalary.utilNomenclatureFictif.getAmountOrRateFromNomenclature(fictivesalary.getInfoSalary().getComp_id().getCdos(), 99, "ALGO-31", 1, fictivesalary.param.getNlot(),
				fictivesalary.param.getNumeroBulletin(), fictivesalary.param.getMoisPaieCourant(), ClsEnumeration.EnColumnToRead.AMOUNT);
		double diviseur = (tempNumber == null || tempNumber.intValue() == 0) ? 30 : tempNumber.doubleValue();

		//
		// IF type_nbj = 'C' THEN
		// PA_CALCUL.wnbjsmm := PA_CALCUL.wnbjc;
		// ELSE
		// PA_CALCUL.wnbjsmm := PA_CALCUL.wnbjca;
		// END IF;
		if ("C".equals(type_nbj))
		{
			fictivesalary.getWorkTimeFictif().setNbreJourCongesSalaireMoyMois(fictivesalary.getWorkTimeFictif().getNbreJourConges());
		}
		else
		{
			fictivesalary.getWorkTimeFictif().setNbreJourCongesSalaireMoyMois(fictivesalary.getWorkTimeFictif().getNbreJoursAbsencePourCongeAnnuel());
		}
		//
		if (fictivesalary.getInfoSalary().getJapa() == null)
			fictivesalary.getInfoSalary().setJapa(new BigDecimal(0));
		if (fictivesalary.getInfoSalary().getJapec() == null)
			fictivesalary.getInfoSalary().setJapec(new BigDecimal(0));
		//
		// -- LH 210198
		if (fictivesalary.param.isStc() && !fictivesalary.getEntreprise().equals(ClsEnumeration.EnEnterprise.SHELL_GABON))
		{
			if (fictivesalary.getInfoSalary().getJapa().doubleValue() == 0 && fictivesalary.getInfoSalary().getJapec().doubleValue() == 0)
				nbreJourscstc = fictivesalary.getWorkTimeFictif().getNbreJourCongesSalaireMoyMois();
			else
			{
				if (fictivesalary.getWorkTimeFictif().getNbreJourCongesSalaireMoyMois() > fictivesalary.getInfoSalary().getJapa().doubleValue())
				{
					fictivesalary.getInfoSalary().setJapec(
							new BigDecimal(fictivesalary.getInfoSalary().getJapec().doubleValue()
									- (fictivesalary.getWorkTimeFictif().getNbreJourCongesSalaireMoyMois() - fictivesalary.getInfoSalary().getJapa().doubleValue())));
					fictivesalary.getInfoSalary().setJapa(new BigDecimal(0));
				}
				else
				{
					fictivesalary.getInfoSalary().setJapa(new BigDecimal(fictivesalary.getInfoSalary().getJapa().doubleValue() - fictivesalary.getWorkTimeFictif().getNbreJourCongesSalaireMoyMois()));
				}
				nbreJourscstc = nbreJourscstc
						+ new Double(fictivesalary.getInfoSalary().getJapa().doubleValue() + fictivesalary.getInfoSalary().getJapec().doubleValue()
								+ fictivesalary.getWorkTimeFictif().getNbreJourCongesSalaireMoyMois()).intValue();
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
			int nbjcg = rechercheNombreDeJourCongeDuMois();
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
				fictivesalary.getValeurRubriquePartage().setRates(0);
				fictivesalary.getValeurRubriquePartage().setAmount(0);
				fictivesalary.getValeurRubriquePartage().setBase(0);
				//
			}
			else
			{
				fictivesalary.getValeurRubriquePartage().setRates(nbreJourscstc);
				montant = fictivesalary.getValeurRubriquePartage().getBase() * nbreJourscstc / diviseur;
				fictivesalary.getValeurRubriquePartage().setAmount(montant);
			}
		}
		else
		{
			if (fictivesalary.getWorkTimeFictif().getNbreJourCongesSalaireMoyMois() == 0 && fictivesalary.getWorkTimeFictif().getNbreJourPayeSupplPayeNonPris() == 0
					&& fictivesalary.getWorkTimeFictif().getNbreJourCongesSalaireMoyMoisSuiv() == 0)
			{
				//
				fictivesalary.getValeurRubriquePartage().setRates(0);
				fictivesalary.getValeurRubriquePartage().setAmount(0);
				fictivesalary.getValeurRubriquePartage().setBase(0);
				//
				return true;
			}
			if ("N".equals(fictivesalary.param.getFictiveCalculus()))
			{
				if (fictivesalary.param.getMonthOfPay().equals(fictivesalary.getInfoSalary().getPmcf()))
				{
					if ("C".equalsIgnoreCase(type_nbj))
						fictivesalary.getValeurRubriquePartage().setRates(fictivesalary.getInfoSalary().getNbjcf().doubleValue());
					else
						fictivesalary.getValeurRubriquePartage().setRates(fictivesalary.getInfoSalary().getNbjaf().doubleValue());
				}
				else
				{
					//
					fictivesalary.getValeurRubriquePartage().setRates(0);
					fictivesalary.getValeurRubriquePartage().setAmount(0);
					//
					return true;
				}
			}
			else
				fictivesalary.getValeurRubriquePartage().setRates(fictivesalary.getWorkTimeFictif().getNbreJourCongesSalaireMoyMois());
			//
			fictivesalary.getValeurRubriquePartage().setRates(fictivesalary.getValeurRubriquePartage().getRates() + fictivesalary.getWorkTimeFictif().getNbreJourPayeSupplPayeNonPris());
			fictivesalary.getValeurRubriquePartage().setAmount(fictivesalary.getValeurRubriquePartage().getBase() * fictivesalary.getValeurRubriquePartage().getRates() / diviseur);
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo37(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public boolean algo37(ClsFictifRubriqueClone rubrique)
	{
		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\n" + ">>algo37";
		String cdos = fictivesalary.getInfoSalary().getComp_id().getCdos();
		String crub = rubrique.getRubrique().getComp_id().getCrub();
		Double montant = new Double(0);
		double taux = 0;
		double plaf1 = 0;
		double plaf2 = 0;
		double reste = 0;
		double inter = 0;
		//
		// String queryString = " from ElementSalaireBareme "
		// + " where identreprise = '" + cdos + "'"
		// + " and crub = '" + crub + "'"
		// + " order by cdos , crub , nume";
		// //
		// String queryStringRetro = " from Rhthrubbarem "
		// + " where identreprise = '" + cdos + "'"
		// + " and crub = '" + crub + "'"
		// + " and aamm = '" + fictivesalary.param.getMonthOfPay() + "'"
		// + " and nbul = " + fictivesalary.param.getNumeroBulletin()
		// + " order by cdos , crub , nume";
		//
		rubrique.calculateCumulOfBase();
		//
		fictivesalary.getValeurRubriquePartage().setRates(0);
		fictivesalary.getValeurRubriquePartage().setAmount(0);
		double tauxCotisation = 0;
		//
		for (int i = 0; i < 12; i++)
		{
			if (rubrique.getListOfRegularisationFr().get(i).getPlafond() == 0)
				// EXIT;
				break;
			fictivesalary.getValeurRubriquePartage().setInter(rubrique.getListOfRegularisationFr().get(i).getBase());
			if ("O".equals(rubrique.getRubrique().getRreg()) && "N".equals(rubrique.getRubrique().getRman()))
				tauxCotisation = rubrique.getListOfRegularisationFr().get(i).getTaux();
			// -- Recherche du montant ET du taux a appliquer
			String valeur1 = "";
			String valeur2 = "";
			//Rhthrubbarem oRhthrubbarem = null;
			ElementSalaireBareme oElementSalaireBareme = null;
			// List listOfBarem = fictivesalary.param.isUseRetroactif() ? fictivesalary.getService().find(queryStringRetro) :
			// fictivesalary.getService().find(queryString);
			String keyOfBaremeList = fictivesalary.param.isUseRetroactif() ? crub + this.fictivesalary.param.getMonthOfPay() + this.fictivesalary.param.getNumeroBulletin() : crub;
			List listOfBarem = (List) fictivesalary.param.getListOfRubriquebaremeMap().get(keyOfBaremeList);
			if (listOfBarem != null)
				for (Object barem : listOfBarem)
				{
					if (fictivesalary.param.isUseRetroactif())
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
					if (fictivesalary.param.isPbWithCalulation())
					{
						return false;
					}
					fictivesalary.getValeurRubriquePartage().setPlafond(montant);
					//
					if (fictivesalary.getValeurRubriquePartage().getInter() <= fictivesalary.getValeurRubriquePartage().getPlafond())
						break;
					plaf1 = fictivesalary.getValeurRubriquePartage().getPlafond();
					plaf2 = convertToNumber37(valeur1, rubrique.getListOfRegularisationFr().get(i).getPlafond());
					if (fictivesalary.param.isPbWithCalulation())
					{
						return false;
					}
					reste = plaf1 - plaf2;
					if ("O".equals(rubrique.getRubrique().getRreg()) && "N".equals(rubrique.getRubrique().getRman()))
						inter = reste * tauxCotisation / 100;
					else
					{
						inter = reste * taux / 100;
						fictivesalary.getValeurRubriquePartage().setBasePlafonnee(reste);
					}
					//
					montant = fictivesalary.getValeurRubriquePartage().getAmount() + inter;
					fictivesalary.getValeurRubriquePartage().setAmount(montant);
					//
					if ("N".equals(rubrique.getRubrique().getRreg()) && "O".equals(rubrique.getRubrique().getRman()))
						fictivesalary.getValeurRubriquePartage().setRates(taux);
					else
						fictivesalary.getValeurRubriquePartage().setRates(tauxCotisation);
				}// foreach
			//
			montant = convertToNumber37(valeur1, rubrique.getListOfRegularisationFr().get(i).getPlafond());
			if (fictivesalary.param.isPbWithCalulation())
			{
				return false;
			}
			fictivesalary.getValeurRubriquePartage().setPlafond(montant);
			//
			if (fictivesalary.getValeurRubriquePartage().getPlafond() > fictivesalary.getValeurRubriquePartage().getInter())
			{
				if ("N".equals(rubrique.getRubrique().getRreg()) && "O".equals(rubrique.getRubrique().getRman()))
				{
					reste = 0;
					fictivesalary.getValeurRubriquePartage().setRates(taux);
				}
				else
				{
					reste = 0;
					fictivesalary.getValeurRubriquePartage().setRates(tauxCotisation);
				}
			}
			else
			{
				reste = fictivesalary.getValeurRubriquePartage().getInter() - fictivesalary.getValeurRubriquePartage().getPlafond();
				if ((taux != 0) && "O".equals(rubrique.getRubrique().getRreg()) && "N".equals(rubrique.getRubrique().getRman()))
					tauxCotisation = 0;
				if ("O".equals(rubrique.getRubrique().getRreg()) && "N".equals(rubrique.getRubrique().getRman()))
					inter = tauxCotisation * reste / 100;
				else
				{
					inter = taux * reste / 100;
					if (taux <= 0)
						fictivesalary.getValeurRubriquePartage().setBasePlafonnee(reste);
				}
				if (inter > 0)
				{
					montant = fictivesalary.getValeurRubriquePartage().getAmount() + inter;
					fictivesalary.getValeurRubriquePartage().setAmount(inter);
				}
				if ((taux != 0) && ("N".equals(rubrique.getRubrique().getRreg()) || "O".equals(rubrique.getRubrique().getRman())))
					fictivesalary.getValeurRubriquePartage().setRates(taux);
			}
		}
		//
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo4(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public boolean algo4(ClsFictifRubriqueClone rubrique)
	{

		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\n" + ">>algo4"+" Rubrique "+rubrique.getRubrique().getComp_id().getCrub();

//		if (this.fictivesalary.getAnciennete() == 0)
//		{
//			fictivesalary.getValeurRubriquePartage().setRates(0);
//			fictivesalary.getValeurRubriquePartage().setAmount(0);
//			return true;
//		}
		String formattedMonth = ClsStringUtil.formatNumber(this.fictivesalary.getAnciennete(), "00");
		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "Anciennete du salarié = " + formattedMonth;
		String RateOrAmount = rubrique.getRubrique().getToum();
		ClsEnumeration.EnColumnToRead colToRead = "T".equals(RateOrAmount) ? ClsEnumeration.EnColumnToRead.RATES : ClsEnumeration.EnColumnToRead.AMOUNT;
		double amountOrRateValue = 0;
		int tabl = 0;
		double val = -1;
		tempNumber = null;
		if (rubrique.getRubrique().getTabl() != null)
		{
			tabl = Integer.valueOf(rubrique.getRubrique().getTabl());
			tempNumber = this.fictivesalary.utilNomenclatureFictif.getAmountOrRateFromNomenclature(this.fictivesalary.getInfoSalary().getComp_id().getCdos(), tabl, formattedMonth, rubrique
					.getRubrique().getNutm(), this.fictivesalary.param.getNlot(), this.fictivesalary.param.getNumeroBulletin(), this.fictivesalary.param.getMonthOfPay(), colToRead);
		}
		if (tempNumber == null)
		{
			fictivesalary.param.setPbWithCalulation(true);
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90056", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(),
					rubrique.getRubrique().getComp_id().getCrub(), fictivesalary.getInfoSalary().getComp_id().getNmat(), formattedMonth, rubrique.getRubrique().getTabl()));
			return false;
		}
		amountOrRateValue = tempNumber.doubleValue();
		fictivesalary.getValeurRubriquePartage().setValeur(amountOrRateValue);
		if ("M".equals(rubrique.getRubrique().getToum()))
		{
			fictivesalary.getValeurRubriquePartage().setRates(0);
			fictivesalary.getValeurRubriquePartage().setAmount(amountOrRateValue);
		}
		else if ("T".equals(rubrique.getRubrique().getToum()))
		{
			fictivesalary.getValeurRubriquePartage().setRates(amountOrRateValue);
			val = fictivesalary.getValeurRubriquePartage().getBase() * fictivesalary.getValeurRubriquePartage().getRates() / 100;
			fictivesalary.getValeurRubriquePartage().setAmount(val);
		}
		else if ("D".equals(rubrique.getRubrique().getToum()))
		{
			fictivesalary.getValeurRubriquePartage().setRates(amountOrRateValue);
			val = fictivesalary.getValeurRubriquePartage().getBase() * fictivesalary.getValeurRubriquePartage().getRates();
			fictivesalary.getValeurRubriquePartage().setAmount(val);
		}
		else
		{
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90058", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(), fictivesalary.getInfoSalary().getComp_id()
					.getNmat(), rubrique.getRubrique().getComp_id().getCrub()));
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo40(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public boolean algo40(ClsFictifRubriqueClone rubrique)
	{
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo41(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public boolean algo41(ClsFictifRubriqueClone rubrique)
	{
		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\n" + ">>algo41"+" Rubrique "+rubrique.getRubrique().getComp_id().getCrub();
		String cdos = fictivesalary.getInfoSalary().getComp_id().getCdos();
		Double montant = new Double(0);
		String a_cacc = "";
		//
		fictivesalary.getValeurRubriquePartage().setRates(0);
		fictivesalary.getValeurRubriquePartage().setAmount(0);
		//
		// -- Concatenation des deux cles
		int cle1 = ClsObjectUtil.isNull(rubrique.getRubrique().getCle1()) ? 0 : Integer.valueOf(rubrique.getRubrique().getCle1());
		int cle2 = ClsObjectUtil.isNull(rubrique.getRubrique().getCle2()) ? 0 : Integer.valueOf(rubrique.getRubrique().getCle2());
		a_cacc = concatene(cle1, cle2, rubrique.getRubrique().getComp_id().getCrub());
		a_cacc = StringUtils.isBlank(a_cacc)? "VIDE" : a_cacc;
		if (fictivesalary.param.isPbWithCalulation())
		{
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + "...Probléme de calcul!";
			return false;
		}
		//
		// -- Lecture de la valeur dans les nomenclatures
		ClsEnumeration.EnColumnToRead col = "M".equals(rubrique.getRubrique().getToum()) ? ClsEnumeration.EnColumnToRead.AMOUNT : ClsEnumeration.EnColumnToRead.RATES;
		tempNumber = null;
		if (rubrique.getRubrique().getTabl() != null)
		{
			tempNumber = fictivesalary.utilNomenclatureFictif.getAmountOrRateFromNomenclature(cdos, Integer.valueOf(rubrique.getRubrique().getTabl()), a_cacc, rubrique.getRubrique().getNutm(),
					fictivesalary.param.getNlot(), fictivesalary.param.getNumeroBulletin(), this.fictivesalary.param.getMonthOfPay(), col);
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + "----------------------------->Montant de la table " + rubrique.getRubrique().getTabl() + " = " + montant;
		}

		if (tempNumber == null)
		{
			// logger
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90056", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(),
					rubrique.getRubrique().getComp_id().getCrub(), fictivesalary.getInfoSalary().getComp_id().getNmat(), a_cacc, rubrique.getRubrique().getTabl()));
			fictivesalary.param.setPbWithCalulation(true);
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + fictivesalary.param.getError();
			return false;
		}

		montant = tempNumber.doubleValue();
		fictivesalary.getValeurRubriquePartage().setValeur(montant);
		// -- Calcul du montant
		if ("T".equals(rubrique.getRubrique().getToum()))
		{
			if (fictivesalary.getValeurRubriquePartage().getBase() == 0)
			{
				fictivesalary.getValeurRubriquePartage().setBase(fictivesalary.getValeurRubriquePartage().getValeur());
				fictivesalary.getValeurRubriquePartage().setBasePlafonnee(fictivesalary.getValeurRubriquePartage().getValeur());
				fictivesalary.getValeurRubriquePartage().setAmount(fictivesalary.getValeurRubriquePartage().getValeur());
			}
			else
			{
				fictivesalary.getValeurRubriquePartage().setRates(fictivesalary.getValeurRubriquePartage().getValeur());
				montant = fictivesalary.getValeurRubriquePartage().getBase() * fictivesalary.getValeurRubriquePartage().getRates() / 100;
				fictivesalary.getValeurRubriquePartage().setAmount(montant);
			}
		}
		else if ("D".equals(rubrique.getRubrique().getToum()))
		{
			fictivesalary.getValeurRubriquePartage().setRates(fictivesalary.getValeurRubriquePartage().getValeur());
			if (fictivesalary.getValeurRubriquePartage().getRates() != 0)
			{
				montant = fictivesalary.getValeurRubriquePartage().getBase() / fictivesalary.getValeurRubriquePartage().getRates();
				fictivesalary.getValeurRubriquePartage().setAmount(montant);
			}
			else
				fictivesalary.getValeurRubriquePartage().setAmount(0);
		}
		else if ("M".equals(rubrique.getRubrique().getToum()))
		{
			fictivesalary.getValeurRubriquePartage().setBase(fictivesalary.getValeurRubriquePartage().getValeur());
			fictivesalary.getValeurRubriquePartage().setBasePlafonnee(fictivesalary.getValeurRubriquePartage().getValeur());
			fictivesalary.getValeurRubriquePartage().setAmount(fictivesalary.getValeurRubriquePartage().getValeur());
			fictivesalary.getValeurRubriquePartage().setRates(0);
		}
		else if ("R".equals(rubrique.getRubrique().getToum()))
		{
			fictivesalary.getValeurRubriquePartage().setBase(fictivesalary.getValeurRubriquePartage().getValeur());
			fictivesalary.getValeurRubriquePartage().setBasePlafonnee(fictivesalary.getValeurRubriquePartage().getValeur());
			fictivesalary.getValeurRubriquePartage().setAmount(fictivesalary.getValeurRubriquePartage().getValeur());
			fictivesalary.getValeurRubriquePartage().setRates(0);
		}
		else
		{
			// logger
			String error = fictivesalary.param.errorMessage("ERR-90058", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(), fictivesalary.getInfoSalary().getComp_id().getNmat(),
					rubrique.getRubrique().getComp_id().getCrub());
			fictivesalary.param.setError(error);
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + fictivesalary.param.getError();
			return false;
		}
		return true;
	}
	
	public boolean algo42(ClsFictifRubriqueClone rubrique)
	{
		return true;//comilog.algo42(rubrique);
	}

	
	public boolean algo46(ClsFictifRubriqueClone rubrique)
	{
		return true;//comilog.algo46(rubrique);
	}
	
	public boolean algo47(ClsFictifRubriqueClone rubrique)
	{
		return true;//comilog.algo47(rubrique);
	}
	
	public boolean algo44(ClsFictifRubriqueClone rubrique)
	{
		
		return true;
	}
	
	public boolean algo45(ClsFictifRubriqueClone rubrique)
	{
		if (this.fictivesalary.infoSalary.getJapa() == null)
			fictivesalary.infoSalary.setJapa(BigDecimal.ZERO);
		if (this.fictivesalary.infoSalary.getJapec() == null)
			fictivesalary.infoSalary.setJapec(BigDecimal.ZERO);

		double w_bas = this.getFictivesalary().getValeurRubriquePartage().getBase();
		double w_basp = this.getFictivesalary().getValeurRubriquePartage().getBase();
		double w_mon = (w_bas / 30) * (this.fictivesalary.infoSalary.getJapec().doubleValue() + this.fictivesalary.infoSalary.getJapa().doubleValue());
		fictivesalary.getValeurRubriquePartage().setBasePlafonnee(w_basp);
		fictivesalary.getValeurRubriquePartage().setAmount(w_mon);

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo43(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public boolean algo43(ClsFictifRubriqueClone rubrique)
	{
		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\n" + ">>algo43"+" Rubrique "+rubrique.getRubrique().getComp_id().getCrub();
		String cdos = fictivesalary.getInfoSalary().getComp_id().getCdos();
		String crub = rubrique.getRubrique().getComp_id().getCrub();
		Double montant = new Double(0);
		double taux = 0;
		//
		// String queryString = " from ElementSalaireBareme "
		// + " where identreprise = '" + cdos + "'"
		// + " and crub = '" + crub + "'"
		// + " order by cdos , crub , nume";
		// //
		// String queryStringRetro = " from Rhthrubbarem "
		// + " where identreprise = '" + cdos + "'"
		// + " and crub = '" + crub + "'"
		// + " and aamm = '" + fictivesalary.param.getMonthOfPay() + "'"
		// + " and nbul = " + fictivesalary.param.getNumeroBulletin()
		// + " order by cdos , crub , nume";
		//
		fictivesalary.getValeurRubriquePartage().setRates(0);
		fictivesalary.getValeurRubriquePartage().setAmount(0);
		fictivesalary.param.setPbWithCalulation(false);
		// -- Lecture de la donnee designee par la cle1
		String valeur = concat43(Integer.valueOf(rubrique.getRubrique().getCle1()), crub);
		valeur = StringUtils.trim(valeur);
		valeur = StringUtils.replace(valeur, ",", ".");
		if(StringUtils.isBlank(valeur)) valeur = "0";
		// w_inter := a_valeur;
		// int inter = Integer.valueOf(valeur);
		fictivesalary.getValeurRubriquePartage().setInter(new BigDecimal(valeur).doubleValue());
		if (fictivesalary.param.isPbWithCalulation())
		{
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90058", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(), fictivesalary.getInfoSalary().getComp_id()
					.getNmat(), rubrique.getRubrique().getComp_id().getCrub()));
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + fictivesalary.param.getError();
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
		// List listOfBarem = fictivesalary.param.isUseRetroactif() ? fictivesalary.getService().find(queryStringRetro) :
		// fictivesalary.getService().find(queryString);
		String keyOfBaremeList = fictivesalary.param.isUseRetroactif() ? crub + this.fictivesalary.param.getMonthOfPay() + this.fictivesalary.param.getNumeroBulletin() : crub;
		List listOfBarem = (List) fictivesalary.param.getListOfRubriquebaremeMap().get(keyOfBaremeList);
		if (listOfBarem != null)
			for (Object barem : listOfBarem)
			{
				if (fictivesalary.param.isUseRetroactif())
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
				if (!ClsObjectUtil.isNull(valeur1) && !ClsObjectUtil.isNull(valeur2))
				{
					dblValeur1 = rubrique.convertToNumber(valeur1);
					dblValeur2 = rubrique.convertToNumber(valeur2);
					if (fictivesalary.getValeurRubriquePartage().getInter() >= dblValeur1 && fictivesalary.getValeurRubriquePartage().getInter() <= dblValeur2)
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
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90061", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(),
					rubrique.getRubrique().getComp_id().getCrub(), fictivesalary.getInfoSalary().getComp_id().getNmat()));
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + fictivesalary.param.getError();
			return false;
		}
		// -- Calcul de base, taux et montant
		if (!ok)
		{
			fictivesalary.getValeurRubriquePartage().setRates(0);
			fictivesalary.getValeurRubriquePartage().setAmount(0);
		}
		else
		{
			if ("T".equals(rubrique.getRubrique().getTxmt()))
			{
				fictivesalary.getValeurRubriquePartage().setRates(taux);
				double montant1 = fictivesalary.getValeurRubriquePartage().getBase() * fictivesalary.getValeurRubriquePartage().getRates() / 100;
				fictivesalary.getValeurRubriquePartage().setAmount(montant1);
			}
			else if ("M".equals(rubrique.getRubrique().getTxmt()))
			{
				fictivesalary.getValeurRubriquePartage().setBase(montant);
				fictivesalary.getValeurRubriquePartage().setBasePlafonnee(montant);
				fictivesalary.getValeurRubriquePartage().setAmount(montant);
			}
			else if ("D".equals(rubrique.getRubrique().getTxmt()))
			{
				fictivesalary.getValeurRubriquePartage().setRates(taux);
				if (taux != 0)
					fictivesalary.getValeurRubriquePartage().setAmount(fictivesalary.getValeurRubriquePartage().getBase() / taux);
				else
					fictivesalary.getValeurRubriquePartage().setAmount(0);
			}
			else
			{
				//
				fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90058", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(), fictivesalary.getInfoSalary()
						.getComp_id().getNmat(), rubrique.getRubrique().getComp_id().getCrub()));
				if ('O' == fictivesalary.param.getGenfile())
					outputtext += "\n" + fictivesalary.param.getError();
				return false;
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo48(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public boolean algo48(ClsFictifRubriqueClone rubrique)
	{
		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\n" + ">>algo48";
		String cdos = fictivesalary.getInfoSalary().getComp_id().getCdos();
		// String crub = rubrique.getRubrique().getComp_id().getCrub();
		Double montant = new Double(0);
		// double taux = 0;
		double divBaseConge = 0;
		double divBaseSex = 0;
		double nbreJourSuppl = 0;
		// -- Pas de calcul si pas de fictif et WMDP != du premier mois de conges
		if ("N".equals(fictivesalary.param.getFictiveCalculus()) && !fictivesalary.param.getMonthOfPay().equals(fictivesalary.getInfoSalary().getPmcf()))
		{
			fictivesalary.getValeurRubriquePartage().setBase(0);
			fictivesalary.getValeurRubriquePartage().setBasePlafonnee(0);
			fictivesalary.getValeurRubriquePartage().setRates(0);
			fictivesalary.getValeurRubriquePartage().setAmount(0);
			return true;
		}
		// -- Pas de calcul si pas de conges
		if (fictivesalary.getWorkTimeFictif().getNbreJourConges() == 0 && fictivesalary.getWorkTimeFictif().getNbreJourCongesAnnuelMoisSuiv() == 0)
		{
			fictivesalary.getValeurRubriquePartage().setBase(0);
			fictivesalary.getValeurRubriquePartage().setBasePlafonnee(0);
			fictivesalary.getValeurRubriquePartage().setRates(0);
			fictivesalary.getValeurRubriquePartage().setAmount(0);
			return true;
		}
		// -- Pas de calcul si montant du conges deja calcule
		if (fictivesalary.getWorkTimeFictif().getMontantCongePonctuel() != 0)
		{
			fictivesalary.getValeurRubriquePartage().setAmount(fictivesalary.getWorkTimeFictif().getMontantCongePonctuel());
			return true;
		}
		// IF PA_CALCUL.wsal01.dapa IS NULL THEN
		// PA_CALCUL.wsal01.dapa := 0;
		if (fictivesalary.getInfoSalary().getDapa() == null || fictivesalary.getInfoSalary().getDapa().doubleValue() == 0)
			fictivesalary.getInfoSalary().setDapa(new BigDecimal(0));
		// END IF;
		// IF PA_CALCUL.wsal01.dapec IS NULL THEN
		// PA_CALCUL.wsal01.dapec := 0;
		// PA_CALCUL.wsal01.dapa := 0;
		if (fictivesalary.getInfoSalary().getDapec() == null || fictivesalary.getInfoSalary().getDapec().doubleValue() == 0)
			fictivesalary.getInfoSalary().setDapec(new BigDecimal(0));
		// END IF;
		// IF PA_CALCUL.wsal01.dded IS NULL THEN
		// PA_CALCUL.wsal01.dded := 0;
		// PA_CALCUL.wsal01.dapa := 0;
		if (fictivesalary.getInfoSalary().getDded() == null || fictivesalary.getInfoSalary().getDded().doubleValue() == 0)
			fictivesalary.getInfoSalary().setDded(new BigDecimal(0));
		// END IF;
		//
		if (fictivesalary.getWorkTimeFictif().isDebutDeMois())
			fictivesalary.getValeurRubriquePartage().setBase(0);
		//
		montant = fictivesalary.getValeurRubriquePartage().getBase() + fictivesalary.getInfoSalary().getDapa().doubleValue() + fictivesalary.getInfoSalary().getDapec().doubleValue();
		fictivesalary.getValeurRubriquePartage().setBase(montant);
		fictivesalary.getValeurRubriquePartage().setBasePlafonnee(montant);
		//
		// -- Lecture du diviseur de la base conges
		// IF PA_CALCUL.retroactif THEN
		List l = null;
		if (fictivesalary.param.isUseRetroactif())
		{
			// BEGIN
			// SELECT SUM(DECODE(nume,1,valm)),
			// SUM(DECODE(nume,2,valm))
			// INTO divbascg, divbasex
			// FROM pahfnom
			// where identreprise = PA_CALCUL.wpdos.cdos
			// AND ctab = PA_CALCUL.t_rub.tabl
			// AND cacc = PA_CALCUL.wsal01.cat
			// AND nume IN (1,2)
			// AND aamm = PA_CALCUL.w_aamm
			// AND nbul = PA_CALCUL.wsd_fcal1.nbul;
			// EXCEPTION
			// WHEN NO_DATA_FOUND THEN null;
			// END;
			l = fictivesalary.getService().find(
					"select sum(valm) from Rhthfnom" + " where identreprise = '" + cdos + "'" + " and ctab = " + rubrique.getRubrique().getTabl() + " and cacc = '"
							+ fictivesalary.getInfoSalary().getCat() + "'" + " and aamm = '" + fictivesalary.param.getMonthOfPay() + "'" + " and nbul = "
							+ fictivesalary.param.getNumeroBulletin() + " and nume = 1");
			if (!ClsObjectUtil.isListEmty(l))
				divBaseConge = (Double) l.get(0);
			//
			l = fictivesalary.getService().find(
					"select sum(valm) from Rhthfnom" + " where identreprise = '" + cdos + "'" + " and ctab = '" + rubrique.getRubrique().getTabl() + "'" + " and cacc = '"
							+ fictivesalary.getInfoSalary().getCat() + "'" + " and aamm = '" + fictivesalary.param.getMonthOfPay() + "'" + " and nbul = "
							+ fictivesalary.param.getNumeroBulletin() + " and nume = 2");
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
			// where identreprise = PA_CALCUL.wpdos.cdos
			// AND ctab = PA_CALCUL.t_rub.tabl
			// AND cacc = PA_CALCUL.wsal01.cat
			// AND nume IN (1,2);
			// EXCEPTION
			// WHEN NO_DATA_FOUND THEN null;
			// END;
			l = fictivesalary.getService().find(
					"select sum(valm) from ParamData" + " where identreprise = '" + cdos + "'" + " and ctab = '" + rubrique.getRubrique().getTabl() + "'" + " and cacc = '"
							+ fictivesalary.getInfoSalary().getCat() + "'" + " and nume = 1");
			if (!ClsObjectUtil.isListEmty(l))
				divBaseConge = (Double) l.get(0);
			//
			l = fictivesalary.getService().find(
					"select sum(valm) from ParamData" + " where identreprise = '" + cdos + "'" + " and ctab = '" + rubrique.getRubrique().getTabl() + "'" + " and cacc = '"
							+ fictivesalary.getInfoSalary().getCat() + "'" + " and nume = 2");
			if (!ClsObjectUtil.isListEmty(l))
				divBaseSex = (Double) l.get(0);
		}
		if (divBaseConge <= 0)
		{
			//
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90062", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(),
					rubrique.getRubrique().getComp_id().getCrub(), fictivesalary.getInfoSalary().getComp_id().getNmat(), rubrique.getRubrique().getTabl(), fictivesalary.getInfoSalary().getCat()));
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + fictivesalary.param.getError();
			return false;
		}
		if (!ClsObjectUtil.isNull(fictivesalary.param.getExpatrieValeur()))
		{
			if (fictivesalary.param.getExpatrieTypeContrat() == 1)
			{
				if (fictivesalary.param.getExpatrieValeur().equals(fictivesalary.getInfoSalary().getRegi()))
					divBaseConge = divBaseSex;
			}
			else if (fictivesalary.param.getExpatrieTypeContrat() == 2)
			{
				if (fictivesalary.param.getExpatrieValeur().equals(fictivesalary.getInfoSalary().getTypc()))
					divBaseConge = divBaseSex;
			}
			else if (fictivesalary.param.getExpatrieTypeContrat() == 3)
			{
				if (fictivesalary.param.getExpatrieValeur().equals(fictivesalary.getInfoSalary().getClas()))
					divBaseConge = divBaseSex;
			}
		}
		if ("N".equals(fictivesalary.param.getFictiveCalculus()))
		{
			if (fictivesalary.param.getMonthOfPay().equals(fictivesalary.getInfoSalary().getPmcf()))
				fictivesalary.getWorkTimeFictif().setNbreJourConges(fictivesalary.getInfoSalary().getNbjcf().intValue());
			else
				fictivesalary.getWorkTimeFictif().setNbreJourConges(0);
		}
		//
		fictivesalary.getValeurRubriquePartage().setRates(fictivesalary.getWorkTimeFictif().getNbreJourConges() + fictivesalary.getWorkTimeFictif().getNbreJourCongesNonPris());
		montant = (fictivesalary.getValeurRubriquePartage().getBase() / divBaseConge) - fictivesalary.getInfoSalary().getDded().doubleValue();
		fictivesalary.getValeurRubriquePartage().setAmount(montant);
		//
		if (fictivesalary.getInfoSalary().getNbjaf() != null && fictivesalary.getInfoSalary().getNbjaf().intValue() != 0 && fictivesalary.getInfoSalary().getNbjaf().doubleValue() != 0
				&& "O".equals(fictivesalary.param.getFictiveCalculus()))
		{
			montant = fictivesalary.getValeurRubriquePartage().getAmount() * (fictivesalary.getValeurRubriquePartage().getRates() / fictivesalary.getInfoSalary().getNbjaf().intValue());
			fictivesalary.getValeurRubriquePartage().setAmount(montant);
		}
		//
		nbreJourSuppl = fictivesalary.calculNombreDJourSuppl();

		if ((fictivesalary.getValeurRubriquePartage().getRates() - nbreJourSuppl) != 0)
		{
			montant = fictivesalary.getValeurRubriquePartage().getAmount()
					* (fictivesalary.getValeurRubriquePartage().getRates() / (fictivesalary.getValeurRubriquePartage().getRates() - nbreJourSuppl));
			fictivesalary.getValeurRubriquePartage().setAmount(montant);
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo5(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public boolean algo5(ClsFictifRubriqueClone rubrique)
	{
		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\n" + ">>algo5"+" Rubrique "+rubrique.getRubrique().getComp_id().getCrub();

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
					amountValueNumber = this.fictivesalary.utilNomenclatureFictif.getAmountOrRateFromT99(fictivesalary.param.getListOfTable99Map(), this.fictivesalary.getInfoSalary().getComp_id()
							.getCdos(), tabl, codeRubrique, 1, this.fictivesalary.param.getNlot(), this.fictivesalary.param.getMonthOfPay(), ClsEnumeration.EnColumnToRead.AMOUNT);
				}
				else
				{
					amountValueNumber = this.fictivesalary.utilNomenclatureFictif.getAmountOrRateFromNomenclature(this.fictivesalary.getInfoSalary().getComp_id().getCdos(), tabl, codeRubrique, 1,
							this.fictivesalary.param.getNlot(), this.fictivesalary.param.getNumeroBulletin(), this.fictivesalary.param.getMonthOfPay(), ClsEnumeration.EnColumnToRead.AMOUNT);
				}
			}
		}
		else if (rubrique.getRubrique().getTabl() != null)
		{
			tabl = Integer.valueOf(rubrique.getRubrique().getTabl());
			if (tabl == 51 || tabl == 52 || tabl == 99)
			{
				rateValueNumber = this.fictivesalary.utilNomenclatureFictif.getAmountOrRateFromT99(fictivesalary.param.getListOfTable99Map(),
						this.fictivesalary.getInfoSalary().getComp_id().getCdos(), tabl, codeRubrique, 1, this.fictivesalary.param.getNlot(), this.fictivesalary.param.getMonthOfPay(),
						ClsEnumeration.EnColumnToRead.RATES);
			}
			else
			{
				rateValueNumber = this.fictivesalary.utilNomenclatureFictif.getAmountOrRateFromNomenclature(this.fictivesalary.getInfoSalary().getComp_id().getCdos(), tabl, codeRubrique, 1,
						this.fictivesalary.param.getNlot(), this.fictivesalary.param.getNumeroBulletin(), this.fictivesalary.param.getMonthOfPay(), ClsEnumeration.EnColumnToRead.RATES);
			}
		}
		if ((amountValueNumber == null && "M".equals(rubrique.getRubrique().getToum())) || (rateValueNumber == null && (!"M".equals(rubrique.getRubrique().getToum()))))
		{
			// logger
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90056", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(),
					rubrique.getRubrique().getComp_id().getCrub(), fictivesalary.getInfoSalary().getComp_id().getNmat(), codeRubrique, rubrique.getRubrique().getTabl()));
			return false;
		}
		amountValue = amountValueNumber == null ? 0 : amountValueNumber.doubleValue();
		rateValue = rateValueNumber == null ? 0 : rateValueNumber.doubleValue();

		if ("T".equals(rubrique.getRubrique().getToum()))
		{
			fictivesalary.getValeurRubriquePartage().setRates(rateValue);
			val = fictivesalary.getValeurRubriquePartage().getBase() * fictivesalary.getValeurRubriquePartage().getRates() / 100;
			fictivesalary.getValeurRubriquePartage().setAmount(val);
		}
		else if ("D".equals(rubrique.getRubrique().getToum()))
		{
			fictivesalary.getValeurRubriquePartage().setRates(rateValue);
			if (rateValue != 0)
			{
				val = fictivesalary.getValeurRubriquePartage().getBase() / fictivesalary.getValeurRubriquePartage().getRates();
				fictivesalary.getValeurRubriquePartage().setAmount(val);
			}
		}
		else if ("M".equals(rubrique.getRubrique().getToum()))
		{
			fictivesalary.getValeurRubriquePartage().setRates(0);
			fictivesalary.getValeurRubriquePartage().setAmount(amountValue);
			fictivesalary.getValeurRubriquePartage().setBase(amountValue);
			fictivesalary.getValeurRubriquePartage().setBasePlafonnee(amountValue);
		}
		else if ("R".equals(rubrique.getRubrique().getToum()))
		{
			fictivesalary.getValeurRubriquePartage().setRates(0);
			fictivesalary.getValeurRubriquePartage().setAmount(rateValue);
			fictivesalary.getValeurRubriquePartage().setBase(rateValue);
			fictivesalary.getValeurRubriquePartage().setBasePlafonnee(rateValue);
		}
		else
		{
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90058", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(), fictivesalary.getInfoSalary().getComp_id()
					.getNmat(), rubrique.getRubrique().getComp_id().getCrub()));
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo50(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public boolean algo50(ClsFictifRubriqueClone rubrique)
	{
//		if(StringUtils.equals(fictivesalary.param.getNomClient(), ClsEntreprise.COMILOG))
//			return comilog.algo50(rubrique);
		
		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\n" + ">>algo50";
		Double montant = new Double(0);
		double divBaseConge = 0;
		double coefBaseConge = 0;
		
//		   IF PA_CALFIC.wchg THEN
//	      w_bas := w_bas + cal_con;
//	   END IF;
//	   w_bas := w_bas + fic_con;
		double base = fictivesalary.getValeurRubriquePartage().getBase();
		fictivesalary.getValeurRubriquePartage().setBase(base + this.fic_con());
		//
		if (fictivesalary.param.isStc())
		{
			if (ClsObjectUtil.isNull(fictivesalary.getInfoSalary().getDapa()))
				fictivesalary.getInfoSalary().setDapa(new BigDecimal(0));
			if (ClsObjectUtil.isNull(fictivesalary.getInfoSalary().getDapec()))
				fictivesalary.getInfoSalary().setDapec(new BigDecimal(0));
			if (ClsObjectUtil.isNull(fictivesalary.getInfoSalary().getDded()))
				fictivesalary.getInfoSalary().setDded(new BigDecimal(0));
			montant = fictivesalary.getValeurRubriquePartage().getBase() + fictivesalary.getInfoSalary().getDapa().doubleValue() + fictivesalary.getInfoSalary().getDapec().doubleValue();
			fictivesalary.getValeurRubriquePartage().setBase(montant);
			fictivesalary.getValeurRubriquePartage().setBasePlafonnee(montant);
			// -- Lecture du diviseur de la base conges
			tempNumber = null;
			if (rubrique.getRubrique().getTabl() != null)
			{
				tempNumber = fictivesalary.utilNomenclatureFictif.getAmountOrRateFromNomenclature(fictivesalary.getInfoSalary().getComp_id().getCdos(), Integer.valueOf(rubrique.getRubrique()
						.getTabl()), fictivesalary.getInfoSalary().getCat(), 1, fictivesalary.param.getNlot(), fictivesalary.param.getNumeroBulletin(), this.fictivesalary.param.getMonthOfPay(),
						ClsEnumeration.EnColumnToRead.AMOUNT);
			}
			if (tempNumber == null || tempNumber.intValue() == 0)
			{
				//
				fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90062", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id()
						.getCrub(), fictivesalary.getInfoSalary().getComp_id().getNmat(), rubrique.getRubrique().getTabl(), fictivesalary.getInfoSalary().getCat()));
				if ('O' == fictivesalary.param.getGenfile())
					outputtext += "\n" + fictivesalary.param.getError();
				return false;
			}

			montant = tempNumber.doubleValue();
			//
			divBaseConge = montant;
			// -- Lecture du coefficient de la base conges
			tempNumber = null;
			if (rubrique.getRubrique().getTabl() != null)
			{
				tempNumber = fictivesalary.utilNomenclatureFictif.getAmountOrRateFromNomenclature(fictivesalary.getInfoSalary().getComp_id().getCdos(), Integer.valueOf(rubrique.getRubrique()
						.getTabl()), fictivesalary.getInfoSalary().getCat(), 4, fictivesalary.param.getNlot(), fictivesalary.param.getNumeroBulletin(), this.fictivesalary.param.getMonthOfPay(),
						ClsEnumeration.EnColumnToRead.RATES);
			}
			if (tempNumber == null || tempNumber.intValue() == 0)
			{
				//
				fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90078", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(), rubrique.getRubrique().getComp_id()
						.getCrub(), fictivesalary.getInfoSalary().getComp_id().getNmat(), rubrique.getRubrique().getTabl(), fictivesalary.getInfoSalary().getCat()));
				if ('O' == fictivesalary.param.getGenfile())
					outputtext += "\n" + fictivesalary.param.getError();
				return false;
			}
			montant = tempNumber.doubleValue();
			//
			coefBaseConge = montant;
			//
			fictivesalary.getValeurRubriquePartage().setRates(fictivesalary.getInfoSalary().getJapa().add(fictivesalary.getInfoSalary().getJapec()).doubleValue());
			montant = ((fictivesalary.getValeurRubriquePartage().getBase() / divBaseConge) * coefBaseConge) - fictivesalary.getInfoSalary().getDded().doubleValue();
			fictivesalary.getValeurRubriquePartage().setAmount(montant);
			//
			return true;
		}
		// -- ----- Pas de calcul si pas de fictif et WMDP != du premier mois de conges
		if (("N".equals(fictivesalary.param.getFictiveCalculus()) && !fictivesalary.param.getMonthOfPay().equals(fictivesalary.getInfoSalary().getPmcf()))
				|| (fictivesalary.getWorkTimeFictif().getNbreJourConges() == 0 && fictivesalary.getWorkTimeFictif().getNbreJourCongesAnnuelMoisSuiv() == 0))
		{
			//
			fictivesalary.getValeurRubriquePartage().setBase(0);
			fictivesalary.getValeurRubriquePartage().setBasePlafonnee(0);
			fictivesalary.getValeurRubriquePartage().setRates(0);
			fictivesalary.getValeurRubriquePartage().setAmount(0);
			//
			return true;
		}
		//
		if (ClsObjectUtil.isNull(fictivesalary.getInfoSalary().getDapa()))
			fictivesalary.getInfoSalary().setDapa(new BigDecimal(0));
		if (ClsObjectUtil.isNull(fictivesalary.getInfoSalary().getDapec()))
			fictivesalary.getInfoSalary().setDapec(new BigDecimal(0));
		if (ClsObjectUtil.isNull(fictivesalary.getInfoSalary().getDded()))
			fictivesalary.getInfoSalary().setDded(new BigDecimal(0));
		//
		if (fictivesalary.getWorkTimeFictif().isDebutDeMois())
			fictivesalary.getValeurRubriquePartage().setBase(0);
		//
		montant = fictivesalary.getValeurRubriquePartage().getBase() + fictivesalary.getInfoSalary().getDapa().add(fictivesalary.getInfoSalary().getDapec()).doubleValue();
		fictivesalary.getValeurRubriquePartage().setBase(montant);
		fictivesalary.getValeurRubriquePartage().setBasePlafonnee(montant);
		//
		// -- Lecture du diviseur de la base conges
		tempNumber = null;
		if (rubrique.getRubrique().getTabl() != null)
		{
			tempNumber = fictivesalary.utilNomenclatureFictif.getAmountOrRateFromNomenclature(fictivesalary.getInfoSalary().getComp_id().getCdos(), Integer.valueOf(rubrique.getRubrique().getTabl()),
					fictivesalary.getInfoSalary().getCat(), 1, fictivesalary.param.getNlot(), fictivesalary.param.getNumeroBulletin(), this.fictivesalary.param.getMonthOfPay(),
					ClsEnumeration.EnColumnToRead.AMOUNT);
		}
		if (tempNumber == null || tempNumber.intValue() == 0)
		{
			//
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90062", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(),
					rubrique.getRubrique().getComp_id().getCrub(), fictivesalary.getInfoSalary().getComp_id().getNmat(), rubrique.getRubrique().getTabl(), fictivesalary.getInfoSalary().getCat()));
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + fictivesalary.param.getError();
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
			tempNumber = fictivesalary.utilNomenclatureFictif.getAmountOrRateFromNomenclature(fictivesalary.getInfoSalary().getComp_id().getCdos(), Integer.valueOf(rubrique.getRubrique().getTabl()),
					fictivesalary.getInfoSalary().getCat(), 4, fictivesalary.param.getNlot(), fictivesalary.param.getNumeroBulletin(), this.fictivesalary.param.getMonthOfPay(),
					ClsEnumeration.EnColumnToRead.RATES);
		}
		if (tempNumber == null || tempNumber.intValue() == 0)
		{
			// logger
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90078", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(),
					rubrique.getRubrique().getComp_id().getCrub(), fictivesalary.getInfoSalary().getComp_id().getNmat(), rubrique.getRubrique().getTabl(), fictivesalary.getInfoSalary().getCat()));
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + fictivesalary.param.getError();
			return false;
		}
		montant = tempNumber.doubleValue();
		//
		coefBaseConge = montant;
		//
		if ("N".equals(fictivesalary.param.getFictiveCalculus()))
		{
			if (fictivesalary.param.getMonthOfPay().equals(fictivesalary.getInfoSalary().getPmcf()))
				fictivesalary.getWorkTimeFictif().setNbreJoursAbsencePourCongeAnnuel(fictivesalary.getInfoSalary().getNbjaf().intValue());
			else
				fictivesalary.getWorkTimeFictif().setNbreJoursAbsencePourCongeAnnuel(0);
		}
		//
		fictivesalary.getValeurRubriquePartage().setRates(
				fictivesalary.getWorkTimeFictif().getNbreJoursAbsencePourCongeAnnuel() + fictivesalary.getWorkTimeFictif().getNbreJourCongesAnnuelPayeNonPris());
		montant = (fictivesalary.getValeurRubriquePartage().getBase() / divBaseConge) * coefBaseConge - fictivesalary.getInfoSalary().getDded().doubleValue()
				- fictivesalary.getWorkTimeFictif().getMontantCongePonctuel();
		fictivesalary.getValeurRubriquePartage().setAmount(montant);
		//
		if ("O".equals(fictivesalary.param.getFictiveCalculus()) && !ClsObjectUtil.isNull(fictivesalary.getInfoSalary().getNbjaf()))
		{
			montant = fictivesalary.getValeurRubriquePartage().getAmount() * fictivesalary.getValeurRubriquePartage().getRates() / fictivesalary.getInfoSalary().getNbjaf().intValue();
			fictivesalary.getValeurRubriquePartage().setAmount(montant);
		}
		//
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo6(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public boolean algo6(ClsFictifRubriqueClone rubrique)
	{
		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\n" + ">>algo6"+" Rubrique "+rubrique.getRubrique().getComp_id().getCrub();
		fictivesalary.getValeurRubriquePartage().setAmount(0);
		fictivesalary.getValeurRubriquePartage().setRates(0);
		Object[] obj = null;
		if (rubrique.getListOfElementVariable() != null && rubrique.getListOfElementVariable().size() > 0)
		{
			obj = (Object[]) rubrique.getListOfElementVariable().get(rubrique.getNumElementVarCourant());
			if (obj != null)
			{
				// if('O' == fictivesalary.param.getGenfile()) outputtext += "\n" + ">>obj = " + ((BigDecimal)obj[0])doubleValue());
				if (fictivesalary.param.isUseRetroactif())
					rubrique.setElementVariableTransit((new BigDecimal( obj[0].toString())).doubleValue());
				else
					rubrique.setElementVariableTransit((new BigDecimal( obj[0].toString())).doubleValue());
			}
		}
		double val = 0;
		// if('O' == fictivesalary.param.getGenfile()) outputtext += "\n" + ">>toum = " + rubrique.getRubrique().getToum());
		if ("T".equals(rubrique.getRubrique().getToum()))
		{
			fictivesalary.getValeurRubriquePartage().setRates(rubrique.getElementVariableTransit());
			val = fictivesalary.getValeurRubriquePartage().getBase() * fictivesalary.getValeurRubriquePartage().getRates() / 100;
			fictivesalary.getValeurRubriquePartage().setAmount(val);
		}
		else if ("M".equals(rubrique.getRubrique().getToum()))
		{
			fictivesalary.getValeurRubriquePartage().setRates(rubrique.getElementVariableTransit());
			val = fictivesalary.getValeurRubriquePartage().getBase() * fictivesalary.getValeurRubriquePartage().getRates();
			fictivesalary.getValeurRubriquePartage().setAmount(val);
		}
		else if ("D".equals(rubrique.getRubrique().getToum()))
		{
			if (rubrique.getElementVariableTransit() != 0)
			{
				fictivesalary.getValeurRubriquePartage().setRates(rubrique.getElementVariableTransit());
				val = fictivesalary.getValeurRubriquePartage().getBase() / fictivesalary.getValeurRubriquePartage().getRates();
				fictivesalary.getValeurRubriquePartage().setAmount(val);
			}
			else
				fictivesalary.getValeurRubriquePartage().setAmount(0);
		}
		else
		{
			// logger
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90058", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(), fictivesalary.getInfoSalary().getComp_id()
					.getNmat(), rubrique.getRubrique().getComp_id().getCrub()));
			return false;
		}
		//
		// if('O' == fictivesalary.param.getGenfile()) outputtext += "\n" + "..amount :" + fictivesalary.getValeurRubriquePartage().getAmount());
		return true;
	}
	
	public boolean algo8(ClsFictifRubriqueClone rubrique)
	{
		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\n" + ">>algo41"+" Rubrique "+rubrique.getRubrique().getComp_id().getCrub();
		String cdos = fictivesalary.getInfoSalary().getComp_id().getCdos();
		Double montant = new Double(0);
		String a_cacc = "";
		//
		fictivesalary.getValeurRubriquePartage().setRates(0);
		fictivesalary.getValeurRubriquePartage().setAmount(0);
		//
		// -- Concatenation des deux cles
		int cle1 = ClsObjectUtil.isNull(rubrique.getRubrique().getCle1()) ? 0 : Integer.valueOf(rubrique.getRubrique().getCle1());
		int cle2 = ClsObjectUtil.isNull(rubrique.getRubrique().getCle2()) ? 0 : Integer.valueOf(rubrique.getRubrique().getCle2());
		a_cacc = concatene(cle1, cle2, rubrique.getRubrique().getComp_id().getCrub());
		
		if (fictivesalary.param.isPbWithCalulation())
		{
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + "...Probléme de calcul!";
			return false;
		}

		if(StringUtils.isBlank(a_cacc)) return true;
		montant = rubrique.convertToNumber(a_cacc);
		
		fictivesalary.getValeurRubriquePartage().setValeur(montant);
		// -- Calcul du montant
		if ("T".equals(rubrique.getRubrique().getToum()))
		{
			if (fictivesalary.getValeurRubriquePartage().getBase() == 0)
			{
				fictivesalary.getValeurRubriquePartage().setBase(fictivesalary.getValeurRubriquePartage().getValeur());
				fictivesalary.getValeurRubriquePartage().setBasePlafonnee(fictivesalary.getValeurRubriquePartage().getValeur());
				fictivesalary.getValeurRubriquePartage().setAmount(fictivesalary.getValeurRubriquePartage().getValeur());
			}
			else
			{
				fictivesalary.getValeurRubriquePartage().setRates(fictivesalary.getValeurRubriquePartage().getValeur());
				montant = fictivesalary.getValeurRubriquePartage().getBase() * fictivesalary.getValeurRubriquePartage().getRates() / 100;
				fictivesalary.getValeurRubriquePartage().setAmount(montant);
			}
		}
		else if ("D".equals(rubrique.getRubrique().getToum()))
		{
			fictivesalary.getValeurRubriquePartage().setRates(fictivesalary.getValeurRubriquePartage().getValeur());
			if (fictivesalary.getValeurRubriquePartage().getRates() != 0)
			{
				montant = fictivesalary.getValeurRubriquePartage().getBase() / fictivesalary.getValeurRubriquePartage().getRates();
				fictivesalary.getValeurRubriquePartage().setAmount(montant);
			}
			else
				fictivesalary.getValeurRubriquePartage().setAmount(0);
		}
		else if ("M".equals(rubrique.getRubrique().getToum()))
		{
			fictivesalary.getValeurRubriquePartage().setBase(fictivesalary.getValeurRubriquePartage().getValeur());
			fictivesalary.getValeurRubriquePartage().setBasePlafonnee(fictivesalary.getValeurRubriquePartage().getValeur());
			fictivesalary.getValeurRubriquePartage().setAmount(fictivesalary.getValeurRubriquePartage().getValeur());
			fictivesalary.getValeurRubriquePartage().setRates(0);
		}
		else if ("R".equals(rubrique.getRubrique().getToum()))
		{
			fictivesalary.getValeurRubriquePartage().setBase(fictivesalary.getValeurRubriquePartage().getValeur());
			fictivesalary.getValeurRubriquePartage().setBasePlafonnee(fictivesalary.getValeurRubriquePartage().getValeur());
			fictivesalary.getValeurRubriquePartage().setAmount(fictivesalary.getValeurRubriquePartage().getValeur());
			fictivesalary.getValeurRubriquePartage().setRates(0);
		}
		else
		{
			// logger
			String error = fictivesalary.param.errorMessage("ERR-90058", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(), fictivesalary.getInfoSalary().getComp_id().getNmat(),
					rubrique.getRubrique().getComp_id().getCrub());
			fictivesalary.param.setError(error);
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + fictivesalary.param.getError();
			return false;
		}
		return true;
	}

	public boolean algo60(ClsFictifRubriqueClone rubrique)
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
	
	public boolean algo61(ClsFictifRubriqueClone rubrique)
	{
		ClsParubqClone rub = rubrique.getRubrique();
		String crub = rub.getComp_id().getCrub();
		ClsInfoSalaryClone info = this.fictivesalary.infoSalary;

		ClsValeurRubriquePartage pa_algo = fictivesalary.getValeurRubriquePartage();
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
				err = fictivesalary.getParam().errorMessage(err, fictivesalary.getParam().getLangue());
				fictivesalary.getParam().setError(err);
				return false;
			}
			int i = new ClsDate(fictivesalary.getInfoSalary().getDdca()).getMonth();
			int j = fictivesalary.getMyMonthOfPay().getMonth();
			j = j - i;
			if (j < 0)
				j = j + 12;
			pa_algo.setBase(NumberUtils.toDouble(new ClsDate(info.getDdca(), "YYYYMMDD")));
			pa_algo.setAmount(fictivesalary.getAnciennete());
			pa_algo.setRates(j);
			if (wfnom10.mnt[2] == 1 && new ClsDate(info.getDdca()).getMonth() != new ClsDate(fictivesalary.param.periodOfPay, "yyyyMM").getMonth())
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
				err = fictivesalary.getParam().errorMessage(err, fictivesalary.getParam().getLangue());
				fictivesalary.getParam().setError(err);
				return false;
			}
			if (StringUtil.notIn(wfnom10.lib[2], "T,S,A"))
			{
				err = "R" + rub.getComp_id().getCrub() + "*, " + "S" + info.getComp_id().getNmat() + "*, " + "A" + rub.getAlgo() + "*. " + "Tb " + rub.getTabl() + ", " + "Cle *" + Cle_acces + "*: " + "Libelle 2 incorrect.";
				err = fictivesalary.getParam().errorMessage(err, fictivesalary.getParam().getLangue());
				fictivesalary.getParam().setError(err);
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
			pa_algo.setBase(fictivesalary.param.nbreJourMoisPourProrata);
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
				err = fictivesalary.getParam().errorMessage(err, fictivesalary.getParam().getLangue());
				fictivesalary.param.setError(err);
				return false;
			}
			//-- Calcul periodes debut et fin
			w_retour = this.Deb_Fin_Periode(wfnom10.lib[2]);
			A61_I1 = NumberUtils.toDouble(StringUtil.oraSubstring(A61_Per1, 5, 2));
			A61_I2 = new ClsDate(fictivesalary.param.periodOfPay, "yyyyMM").getMonth();
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
			pa_algo.setAmount(fictivesalary.getAgeOfAgent());
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
				err = fictivesalary.getParam().errorMessage(err, fictivesalary.getParam().getLangue());
				fictivesalary.param.setError(err);
				return false;
			}
			A61_Rubq = ClsStringUtil.formatNumber(wfnom10.mnt[3], ParameterUtil.formatRubrique);
			if (fictivesalary.findRubriqueCloneFictif(A61_Rubq) == null)
			{
				err = "R" + rub.getComp_id().getCrub() + "*, " + "S" + info.getComp_id().getNmat() + "*, " + "A" + rub.getAlgo() + "*, " + "Montant 3 incorrect.";
				err = fictivesalary.getParam().errorMessage(err, fictivesalary.getParam().getLangue());
				fictivesalary.param.setError(err);
				return false;
			}

			if (StringUtil.notIn(wfnom10.lib[2], "T,S,A"))
			{
				err = "R" + rub.getComp_id().getCrub() + "*, " + "S" + info.getComp_id().getNmat() + "*, " + "A" + rub.getAlgo() + "*, " + "Libelle 2 incorrect.";
				err = fictivesalary.getParam().errorMessage(err, fictivesalary.getParam().getLangue());
				fictivesalary.param.setError(err);
				return false;
			}
			//-- Lecture des dates adhesion et radiation
			String sql = "From CaisseMutuelleSalarie where identreprise = '" + rub.getComp_id().getCdos() + "'";
			sql += " and nmat = '" + info.getComp_id().getNmat() + "'";
			sql += " (and rscm = '" + A61_Rubq + "' or rpcm ='" + A61_Rubq + "')";
			List<CaisseMutuelleSalarie> lst = fictivesalary.service.find(sql);
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
			if (new ClsDate(fictivesalary.param.periodOfPay, "yyyyMM").getMonth() == new ClsDate(info.getDdca()).getMonth())
				pa_algo.setAmount(pa_algo.getBase());
		}
		else
		{

			//			   ---------------------------------------------------------------------
			//			      -- !! Cette information n"est pas traitee !!
			//			      ---------------------------------------------------------------------
			err = "S" + info.getComp_id().getNmat() + "*, " + "R" + rub.getComp_id().getCrub() + "*, " + "A" + rub.getAlgo() + "*, " + "Tb " + rub.getTabl() + ", " +
			// --"Cle *" + LTRIM(RTRIM(Cle_acces))   + "*: " +
					"Cle *" + Cle_acces + "*: " + "Info No " + ClsStringUtil.formatNumber(A61_Info, "00") + "* non traitee.";
			err = fictivesalary.getParam().errorMessage(err, fictivesalary.getParam().getLangue());
			fictivesalary.param.setError(err);
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
		per_deb = new ClsDate(fictivesalary.param.dtDdex).getDateS("yyyyMM");
		per_fin = per_deb;
		if (StringUtils.equalsIgnoreCase(periode, "T"))
		{
			nb_mois = 3;
			if (fictivesalary.param.rangMoisDePaieExercice <= 3)
			{
				per_fin = ClsGeneralUtil.addPer(Integer.valueOf(per_fin), 0, 2) + "";
				ind_mois = fictivesalary.param.rangMoisDePaieExercice;
			}
			else if (fictivesalary.param.rangMoisDePaieExercice <= 6)
			{
				per_deb = ClsGeneralUtil.addPer(Integer.valueOf(per_deb), 0, 3) + "";
				per_fin = ClsGeneralUtil.addPer(Integer.valueOf(per_fin), 0, 5) + "";
				ind_mois = fictivesalary.param.rangMoisDePaieExercice - 3;
			}
			else if (fictivesalary.param.rangMoisDePaieExercice <= 9)
			{
				per_deb = ClsGeneralUtil.addPer(Integer.valueOf(per_deb), 0, 6) + "";
				per_fin = ClsGeneralUtil.addPer(Integer.valueOf(per_fin), 0, 8) + "";
				ind_mois = fictivesalary.param.rangMoisDePaieExercice - 6;
			}
			else if (fictivesalary.param.rangMoisDePaieExercice <= 12)
			{
				per_deb = ClsGeneralUtil.addPer(Integer.valueOf(per_deb), 0, 9) + "";
				per_fin = ClsGeneralUtil.addPer(Integer.valueOf(per_fin), 0, 11) + "";
				ind_mois = fictivesalary.param.rangMoisDePaieExercice - 9;
			}
		}
		else if (StringUtils.equalsIgnoreCase(periode, "S"))
		{
			nb_mois = 6;
			if (fictivesalary.param.rangMoisDePaieExercice <= 6)
			{
				per_fin = ClsGeneralUtil.addPer(Integer.valueOf(per_fin), 0, 5) + "";
				ind_mois = fictivesalary.param.rangMoisDePaieExercice;
			}
			else
			{
				per_deb = ClsGeneralUtil.addPer(Integer.valueOf(per_deb), 0, 6) + "";
				per_fin = ClsGeneralUtil.addPer(Integer.valueOf(per_fin), 0, 11) + "";
				ind_mois = fictivesalary.param.rangMoisDePaieExercice - 6;
			}
		}
		else
		{
			nb_mois = 12;
			ind_mois = fictivesalary.param.rangMoisDePaieExercice;
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
		ParamData nome = (ParamData) this.fictivesalary.service.find("FROM ParamData WHERE identreprise="+rubq.getComp_id().getCdos()+" AND ctab="+num_table+" AND cacc='"+cle10+"' AND nume=1");

		if (nome == null)
		{
			err = "A" + rubq.getAlgo() + ", S" + this.fictivesalary.infoSalary.getComp_id().getNmat() + ", R" + rubq.getComp_id().getCrub() + ": Param: *" + cle10 + "* Mq en T" + rubq.getTabl();
			err = fictivesalary.getParam().errorMessage(err, fictivesalary.getParam().getLangue());
			fictivesalary.getParam().setError(err);
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

		sql += " From ParamData where identreprise = '" + rubq.getComp_id().getCdos() + "' and ctab = " + num_table + " and cacc = '" + cle10 + "' and nume in (1,2,3,4,5,6,7,8,9,10)";

		List listOfMaxsum = fictivesalary.getService().find(sql);
		if (listOfMaxsum.isEmpty())
		{
			err = "A" + rubq.getAlgo() + ", S" + this.fictivesalary.infoSalary.getComp_id().getNmat() + ", R" + rubq.getComp_id().getCrub() + ": Param: *" + cle10 + "* Mq en T" + rubq.getTabl();
			err = fictivesalary.getParam().errorMessage(err, fictivesalary.getParam().getLangue());
			fictivesalary.getParam().setError(err);
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
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo62(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public boolean algo62(ClsFictifRubriqueClone rubrique)
	{
		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\n" + ">>algo62";

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
		fictivesalary.getValeurRubriquePartage().setAmount(0);
		fictivesalary.getValeurRubriquePartage().setRates(0);
		fictivesalary.getValeurRubriquePartage().setBase(0);
		// PA_ALGO.Pb_Calcul := FALSE;
		fictivesalary.param.setPbWithCalulation(false);
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
		// where identreprise = PA_CALCUL.wpdos.cdos
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
		Object objNomenc = fictivesalary.getService().find("FROM ParamData WHERE identreprise="+cdos+" AND ctab="+tabl+" AND cacc='"+accesKey+" AND nume=1");

		if (objNomenc == null)
		{
			// logger
			error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + fictivesalary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + crub + ": Clé " + accesKey + ", Tb "
					+ rubrique.getRubrique().getTabl() + ": Pb lecture.";
			fictivesalary.param.setError(error);
			fictivesalary.param.setPbWithCalulation(true);
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
		// where identreprise = PA_CALCUL.wpdos.cdos
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
		complexQuery += " from ParamData" + " where identreprise = '" + cdos + "'" + " and ctab = " + tabl + " and cacc = '" + accesKey + "'" + " and nume in (2, 3, 4, 5, 6)";
		List listOfMaxsum = fictivesalary.getService().find(complexQuery);
		if (listOfMaxsum == null)
		{
			// logger
			error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + fictivesalary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + crub + ": Clé " + accesKey + ", Tb "
					+ rubrique.getRubrique().getTabl() + ": Pb lecture.";
			fictivesalary.param.setError(error);
			fictivesalary.param.setPbWithCalulation(true);
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
			// if('O' == fictivesalary.param.getGenfile()) outputtext += "\n" + "..................................ERROR on SEX!");
			error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + fictivesalary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + crub + ": Clé " + accesKey + ", Tb "
					+ rubrique.getRubrique().getTabl() + ": Lib2 incorrect.";
			fictivesalary.param.setError(error);
			fictivesalary.param.setPbWithCalulation(true);
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
		// where identreprise = PA_CALCUL.wpdos.cdos
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
				objNomenc = fictivesalary.getService().find("FROM ParamData WHERE identreprise="+cdos+" AND ctab=74 AND cacc='"+lib3+" AND nume=1");

				if (objNomenc == null)
				{
					if ('O' == fictivesalary.param.getGenfile())
						outputtext += "\n" + "..................................ERROR on lib3!";
					// logger
					error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + fictivesalary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + crub + ": Clé " + accesKey + ", Tb "
							+ rubrique.getRubrique().getTabl() + ": Lib3 inconnu en T74.";
					fictivesalary.param.setError(error);
					fictivesalary.param.setPbWithCalulation(true);
					return false;
				}
				pnai = lib3.length() > 1 ? lib3.substring(0, 2) : lib3;
			}
			else
			{
				// logger
				error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + fictivesalary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + crub + ": Clé " + accesKey + ", Tb "
						+ rubrique.getRubrique().getTabl() + ": Pb lect. sur T74.";
				fictivesalary.param.setPbWithCalulation(true);
				fictivesalary.param.setError(error);
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
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + "..................................ERROR on  lib4!";
			// logger
			error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + fictivesalary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + crub + ": Clé " + accesKey + ", Tb "
					+ rubrique.getRubrique().getTabl() + ": Lib4 incorrect.";
			fictivesalary.param.setError(error);
			fictivesalary.param.setPbWithCalulation(true);
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
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + "..................................ERROR on  lib5!";
			// logger
			error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + fictivesalary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + crub + ": Clé " + accesKey + ", Tb "
					+ rubrique.getRubrique().getTabl() + ": Lib5 incorrect.";
			fictivesalary.param.setError(error);
			fictivesalary.param.setPbWithCalulation(true);
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
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + "..................................ERROR on  mnt2!";
			// logger
			error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + fictivesalary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + crub + ": Clé " + accesKey + ", Tb "
					+ rubrique.getRubrique().getTabl() + ": mnt3 incorrect.";
			fictivesalary.param.setError(error);
			fictivesalary.param.setPbWithCalulation(true);
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
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + "..................................ERROR on  mnt4!";
			// logger
			error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + fictivesalary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + crub + ": Clé " + accesKey + ", Tb "
					+ rubrique.getRubrique().getTabl() + ": mnt4 incorrect.";
			fictivesalary.param.setError(error);
			fictivesalary.param.setPbWithCalulation(true);
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
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + "..................................ERROR on  age2 < age1!";
			// logger
			error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + fictivesalary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + crub + ": Clé " + accesKey + ", Tb "
					+ rubrique.getRubrique().getTabl() + ": mnt3, mnt4 decroissant.";
			fictivesalary.param.setError(error);
			fictivesalary.param.setPbWithCalulation(true);
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
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + "..................................ERROR on  mnt5!";
			// logger
			error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + fictivesalary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + crub + ": Clé " + accesKey + ", Tb "
					+ rubrique.getRubrique().getTabl() + ": mnt5 incorrect.";
			fictivesalary.param.setError(error);
			fictivesalary.param.setPbWithCalulation(true);
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
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + "..................................ERROR on  mnt6!";
			// logger
			error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + fictivesalary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + crub + ": Clé " + accesKey + ", Tb "
					+ rubrique.getRubrique().getTabl() + ": mnt6 incorrect.";
			fictivesalary.param.setError(error);
			fictivesalary.param.setPbWithCalulation(true);
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
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + "..................................ERROR on  age2 < age1!";
			// logger
			error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + fictivesalary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + crub + ": Clé " + accesKey + ", Tb "
					+ rubrique.getRubrique().getTabl() + ": mnt5, mnt6 decroissant.";
			fictivesalary.param.setError(error);
			fictivesalary.param.setPbWithCalulation(true);
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
		long anneecourante = fictivesalary.param.getMyMonthOfPay().getYear();
		long annee = anneecourante - age2 - 1;
		long mois = fictivesalary.param.getMyMonthOfPay().getMonth();
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
			dtna2 = fictivesalary.param.getMyMonthOfPay().getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
		}
		else
		{
			annee = anneecourante - age1 - 1;
			mois = fictivesalary.param.getMyMonthOfPay().getMonth();
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
			// where identreprise = PA_CALCUL.wpdos.cdos
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
			complexQuery = "select count(*) from Rhtenfantagent" + " where identreprise = '" + cdos + "'" + " and nmat = '" + fictivesalary.getInfoSalary().getComp_id().getNmat() + "'" + " and nucj between "
					+ nucj1 + " and " + nucj2 + " and sexe like '" + sexe + "'" + " and pnai like '" + pnai + "'" + " and scol like '" + scol + "'" + " and achg like '" + achg + "'";
			nbreenfant = fictivesalary.getService().find(complexQuery);
			if (nbreenfant == null)
			{
				if ('O' == fictivesalary.param.getGenfile())
					outputtext += "\n" + "..................................ERROR on  enfants : date naissance!";
				// logger
				error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + fictivesalary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + crub + ": Clé " + accesKey + ", Tb "
						+ rubrique.getRubrique().getTabl() + ": Pb lect. paenfan. N1";
				fictivesalary.param.setError(error);
				fictivesalary.param.setPbWithCalulation(true);
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
			// where identreprise = PA_CALCUL.wpdos.cdos
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
			complexQuery = "select count(*) from Rhtenfantagent" + " where identreprise = '" + cdos + "'" + " and nmat = '" + fictivesalary.getInfoSalary().getComp_id().getNmat() + "'" + " and dtna is null";
			nbreenfant = fictivesalary.getService().find(complexQuery);
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
				if ('O' == fictivesalary.param.getGenfile())
					outputtext += "\n" + "..................................ERROR on  enfants : date naissance!";
				// logger
				error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + fictivesalary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + crub + ": Clé " + accesKey + ", Tb "
						+ rubrique.getRubrique().getTabl() + "enfants : date naissance";
				fictivesalary.param.setError(error);
				fictivesalary.param.setPbWithCalulation(true);
				return false;
			}
			//
			// BEGIN
			// SELECT NVL(count(*), 0)
			// INTO Nb_enfants
			// FROM paenfan
			// where identreprise = PA_CALCUL.wpdos.cdos
			// AND nmat = PA_CALCUL.wsal01.nmat
			// AND nucj BETWEEN A62_nucj1 AND A62_nucj2
			// AND sexe LIKE A62_sexe
			// AND TO_CHAR(dtna, 'YYYYMM') BETWEEN A62_dtna1 AND A62_dtna2
			// AND pnai LIKE A62_pnai
			// AND scol LIKE A62_scol
			// AND achg LIKE A62_achg;
			Date dtna11 = new ClsDate(dtna1, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getFirstDayOfMonth();
			Date dtna22 = new ClsDate(dtna2, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getLastDayOfMonth();

			complexQuery = "select count(*) from Rhtenfantagent" + " where identreprise = '" + cdos + "'" + " and nmat = '" + fictivesalary.getInfoSalary().getComp_id().getNmat() + "'" + " and nucj between "
					+ nucj1 + " and " + nucj2 + " and sexe like '" + sexe + "'" + " and dtna between '" + new ClsDate(dtna11).getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM) + "' and '"
					+ new ClsDate(dtna22).getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM) + "'" + " and pnai like '" + pnai + "'" + " and scol like '" + scol + "'" + " and achg like '"
					+ achg + "'";
			nbreenfant = fictivesalary.getService().find(complexQuery);
			if (nbreenfant == null)
			{
				if ('O' == fictivesalary.param.getGenfile())
					outputtext += "\n" + "..................................ERROR on  enfants : date naissance!";
				// logger
				error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + fictivesalary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + crub + ": Clé " + accesKey + ", Tb "
						+ rubrique.getRubrique().getTabl() + ": Pb lect. paenfan. N3";
				fictivesalary.param.setError(error);
				fictivesalary.param.setPbWithCalulation(true);
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
		fictivesalary.getValeurRubriquePartage().setBase((Integer) nbreenfant.get(0));
		fictivesalary.getValeurRubriquePartage().setBasePlafonnee(fictivesalary.getValeurRubriquePartage().getBase());
		fictivesalary.getValeurRubriquePartage().setAmount((Integer) nbreenfant.get(0));
		//
		return true;
	}

	public boolean algo63(ClsFictifRubriqueClone rubrique)
	{
		// TODO Auto-generated method stub
		return false;
	}

	public boolean algo64(ClsFictifRubriqueClone rubrique)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo65(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public boolean algo65(ClsFictifRubriqueClone rubrique)
	{
		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\n" + ">>algo65";
		
//		if(StringUtils.equals(fictivesalary.param.getNomClient(), ClsEntreprise.COMILOG))
//			return comilog.algo65(rubrique);

		// w_tau := 0;
		// w_mon := 0;
		// w_valeur := 0;
		fictivesalary.getValeurRubriquePartage().setAmount(0);
		fictivesalary.getValeurRubriquePartage().setRates(0);
		fictivesalary.getValeurRubriquePartage().setBase(0);
		//
		// char4 := PA_CALCUL.t_rub.crub;
		String char4 = rubrique.getRubrique().getComp_id().getCrub();
		int tabl = Integer.valueOf(rubrique.getRubrique().getTabl());
		String cdos = fictivesalary.getInfoSalary().getComp_id().getCdos();
		// wfnom.lib3 := paf_lecfnomL(TO_NUMBER(PA_CALCUL.t_rub.tabl),char4,3,PA_CALCUL.w_aamm,PA_CALCUL.w_nlot,PA_CALCUL.wsd_fcal1.nbul);
		// IF wfnom.lib3 IS NULL THEN
		// PA_CALCUL.err_msg :=
		// PA_PAIE.erreurp('ERR-90056',PA_CALCUL.w_clang,PA_CALCUL.t_rub.algo,PA_CALCUL.t_rub.crub,PA_CALCUL.wsal01.nmat,char4,PA_CALCUL.t_rub.tabl);
		// RETURN FALSE;
		// END IF;
		String libelle3 = fictivesalary.utilNomenclatureFictif.getLabelFromNomenclature(cdos, tabl, char4, 3, fictivesalary.param.getNlot(), fictivesalary.param.getNumeroBulletin(),
				this.fictivesalary.param.getMonthOfPay());
		if (ClsObjectUtil.isNull(libelle3))
		{
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + "..................................ERROR on lib3!";
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90056", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(),
					rubrique.getRubrique().getComp_id().getCrub(), fictivesalary.getInfoSalary().getComp_id().getNmat(), char4, rubrique.getRubrique().getTabl()));
			// logger
			return false;
		}
		//
		// wfnom.lib4 := paf_lecfnomL(TO_NUMBER(PA_CALCUL.t_rub.tabl),char4,4,PA_CALCUL.w_aamm,PA_CALCUL.w_nlot,PA_CALCUL.wsd_fcal1.nbul);
		// IF wfnom.lib4 IS NULL THEN
		// PA_CALCUL.err_msg :=
		// PA_PAIE.erreurp('ERR-90056',PA_CALCUL.w_clang,PA_CALCUL.t_rub.algo,PA_CALCUL.t_rub.crub,PA_CALCUL.wsal01.nmat,char4,PA_CALCUL.t_rub.tabl);
		// RETURN FALSE;
		// END IF;
		String libelle4 = fictivesalary.utilNomenclatureFictif.getLabelFromNomenclature(cdos, tabl, char4, 4, fictivesalary.param.getNlot(), fictivesalary.param.getNumeroBulletin(),
				this.fictivesalary.param.getMonthOfPay());
		if (ClsObjectUtil.isNull(libelle4))
		{
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + "..................................ERROR on lib4!";
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90056", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(),
					rubrique.getRubrique().getComp_id().getCrub(), fictivesalary.getInfoSalary().getComp_id().getNmat(), char4, rubrique.getRubrique().getTabl()));
			// logger
			return false;
		}
		//
		// wfnom.lib5 := paf_lecfnomL(TO_NUMBER(PA_CALCUL.t_rub.tabl),char4,5,PA_CALCUL.w_aamm,PA_CALCUL.w_nlot,PA_CALCUL.wsd_fcal1.nbul);
		// IF wfnom.lib5 IS NULL THEN
		// PA_CALCUL.err_msg :=
		// PA_PAIE.erreurp('ERR-90056',PA_CALCUL.w_clang,PA_CALCUL.t_rub.algo,PA_CALCUL.t_rub.crub,PA_CALCUL.wsal01.nmat,char4,PA_CALCUL.t_rub.tabl);
		// RETURN FALSE;
		// END IF;
		String libelle5 = fictivesalary.utilNomenclatureFictif.getLabelFromNomenclature(cdos, tabl, char4, 5, fictivesalary.param.getNlot(), fictivesalary.param.getNumeroBulletin(),
				this.fictivesalary.param.getMonthOfPay());
		if (ClsObjectUtil.isNull(libelle5))
		{
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90056", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(),
					rubrique.getRubrique().getComp_id().getCrub(), fictivesalary.getInfoSalary().getComp_id().getNmat(), char4, rubrique.getRubrique().getTabl()));
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + "..................................ERROR on lib4!";
			// logger
			return false;
		}
		//
		// w4 := paf_lecfnomM(TO_NUMBER(PA_CALCUL.t_rub.tabl),char4,1,PA_CALCUL.w_aamm,PA_CALCUL.w_nlot,PA_CALCUL.wsd_fcal1.nbul);
		// IF w4 IS NULL THEN
		// PA_CALCUL.err_msg :=
		// PA_PAIE.erreurp('ERR-90056',PA_CALCUL.w_clang,PA_CALCUL.t_rub.algo,PA_CALCUL.t_rub.crub,PA_CALCUL.wsal01.nmat,char4,PA_CALCUL.t_rub.tabl);
		// RETURN FALSE;
		// END IF;
		//
		// IF PA_PAIE.NouZ(w4) THEN
		// PA_CALCUL.err_msg :=
		// PA_PAIE.erreurp('ERR-90065',PA_CALCUL.w_clang,PA_CALCUL.t_rub.algo,PA_CALCUL.t_rub.crub,PA_CALCUL.wsal01.nmat,char4,PA_CALCUL.t_rub.tabl);
		// RETURN FALSE;
		// END IF;
		tempNumber = fictivesalary.utilNomenclatureFictif.getAmountOrRateFromNomenclature(cdos, tabl, char4, 1, fictivesalary.param.getNlot(), fictivesalary.param.getNumeroBulletin(),
				fictivesalary.param.getMoisPaieCourant(), ClsEnumeration.EnColumnToRead.AMOUNT);
		if (tempNumber == null)
		{
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + "..................................ERROR on montant4!";
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90056", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(),
					rubrique.getRubrique().getComp_id().getCrub(), fictivesalary.getInfoSalary().getComp_id().getNmat(), char4, rubrique.getRubrique().getTabl()));
			// logger
			return false;
		}

		if (tempNumber.intValue() == 0)
		{
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + "..................................ERROR on montant4!";
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90065", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(),
					rubrique.getRubrique().getComp_id().getCrub(), fictivesalary.getInfoSalary().getComp_id().getNmat(), char4, rubrique.getRubrique().getTabl()));
			// logger
			return false;
		}
		double montant4 = tempNumber.doubleValue();
		//
		// c4 := LTRIM(TO_CHAR(w4,'0999'));
		//
		String c4 = ClsStringUtil.formatNumber(montant4, ParameterUtil.formatRubrique);
		// wmax := paf_lecfnomM(TO_NUMBER(PA_CALCUL.t_rub.tabl),char4,2,PA_CALCUL.w_aamm,PA_CALCUL.w_nlot,PA_CALCUL.wsd_fcal1.nbul);
		tempNumber = fictivesalary.utilNomenclatureFictif.getAmountOrRateFromNomenclature(cdos, tabl, char4, 2, fictivesalary.param.getNlot(), fictivesalary.param.getNumeroBulletin(),
				this.fictivesalary.param.getMonthOfPay(), ClsEnumeration.EnColumnToRead.AMOUNT);
		Double wmax = new Double(0);
		if (tempNumber != null)
			wmax = tempNumber.doubleValue();
		//
		//
		// char1 := SUBSTR(wfnom.lib3,1,1);
		char char1 = libelle3.toCharArray()[0];
		//
		// IF char1 NOT IN ('S','M','C') THEN
		// PA_CALCUL.err_msg :=
		// PA_PAIE.erreurp('ERR-10514',PA_CALCUL.w_clang,PA_CALCUL.t_rub.algo,PA_CALCUL.t_rub.crub,PA_CALCUL.wsal01.nmat,char4,PA_CALCUL.t_rub.tabl);
		// RETURN FALSE;
		// END IF;
		if (!('S' == char1 || 'M' == char1 || 'C' == char1 || 'L' == char1))
		{
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + "..................................ERROR on char1!";
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-10514", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(),
					rubrique.getRubrique().getComp_id().getCrub(), fictivesalary.getInfoSalary().getComp_id().getNmat(), char4, rubrique.getRubrique().getTabl()));
			// logger
			return false;
		}
		//
		// tdate := RTRIM(LTRIM(SUBSTR(wfnom.lib4,1,5)));
		//
		// IF tdate NOT IN ('DTES','DDCA','DMRR','DTIT','DEPR','DECC','DCHG','DFES','DRTCG','DCHF') THEN
		// PA_CALCUL.err_msg :=
		// PA_PAIE.erreurp('ERR-10515',PA_CALCUL.w_clang,PA_CALCUL.t_rub.algo,PA_CALCUL.t_rub.crub,PA_CALCUL.wsal01.nmat,char4,PA_CALCUL.t_rub.tabl);
		// RETURN FALSE;
		// END IF;
		// String tdate = libelle4.substring(0, 4).trim();
		String tdate = libelle4.trim();
		if (!("DTES".equals(tdate) || "DDCA".equals(tdate) || "DMRR".equals(tdate) || "DTIT".equals(tdate) || "DEPR".equals(tdate) || "DECC".equals(tdate) || "DCHG".equals(tdate)
				|| "DFES".equals(tdate) || "DRTCG".equals(tdate) || "DCHF".equals(tdate) || "M-12".equals(tdate)))
		{
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + "..................................ERROR on tdate! = " + tdate + " libelle4 =" + libelle4;
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-10515", fictivesalary.param.getLangue(), fictivesalary.getInfoSalary().getComp_id().getNmat(), rubrique.getRubrique()
					.getComp_id().getCrub(), rubrique.getRubrique().getTabl()));
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
		String tdate2 = StringUtil.oraSubstring(libelle5,1, 4).trim();
		if (!"PMCF".equals(tdate2))
		{
			dateBase = fictivesalary.param.getMyMonthOfPay().getDateS();
		}
		else
		{
			dateBase = (!ClsObjectUtil.isNull(fictivesalary.getInfoSalary().getPmcf())) ? fictivesalary.getInfoSalary().getPmcf() : fictivesalary.param.getMyMonthOfPay().getDateS();
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
		if ("DTES".equals(tdate) && !ClsObjectUtil.isNull(fictivesalary.getInfoSalary().getDtes()))
			dateReference = new ClsDate(fictivesalary.getInfoSalary().getDtes()).getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
		else if ("DDCA".equals(tdate) && !ClsObjectUtil.isNull(fictivesalary.getInfoSalary().getDdca()))
			dateReference = new ClsDate(fictivesalary.getInfoSalary().getDdca()).getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
		else if ("DMRR".equals(tdate) && !ClsObjectUtil.isNull(fictivesalary.getInfoSalary().getDmrr()))
			dateReference = new ClsDate(fictivesalary.getInfoSalary().getDmrr()).getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
		else if ("DTIT".equals(tdate) && !ClsObjectUtil.isNull(fictivesalary.getInfoSalary().getDtit()))
			dateReference = new ClsDate(fictivesalary.getInfoSalary().getDtit()).getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
		else if ("DEPR".equals(tdate) && !ClsObjectUtil.isNull(fictivesalary.getInfoSalary().getDepr()))
			dateReference = new ClsDate(fictivesalary.getInfoSalary().getDepr()).getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
		else if ("DECC".equals(tdate) && !ClsObjectUtil.isNull(fictivesalary.getInfoSalary().getDecc()))
			dateReference = new ClsDate(fictivesalary.getInfoSalary().getDecc()).getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
		else if ("DCHG".equals(tdate) && !ClsObjectUtil.isNull(fictivesalary.getInfoSalary().getDchg()))
			dateReference = new ClsDate(fictivesalary.getInfoSalary().getDchg()).getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
		else if ("DFES".equals(tdate) && !ClsObjectUtil.isNull(fictivesalary.getInfoSalary().getDfes()))
			dateReference = new ClsDate(fictivesalary.getInfoSalary().getDfes()).getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
		else if ("DRTCG".equals(tdate))
		{
			if (!ClsObjectUtil.isNull(fictivesalary.getInfoSalary().getDrtcg()))
				dateReference = new ClsDate(fictivesalary.getInfoSalary().getDrtcg()).getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
			else
				dateReference = new ClsDate(fictivesalary.getInfoSalary().getDtes()).getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
		}
		else if ("DCHF".equals(tdate) && !ClsObjectUtil.isNull(fictivesalary.getInfoSalary().getDchf()))
			dateReference = new ClsDate(fictivesalary.getInfoSalary().getDchf()).getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
		else  if ("M-12".equals(tdate))
			dateReference = new ClsDate(new ClsDate(fictivesalary.param.periodOfPay,"yyyyMM").addMonth(-12)).getYearAndMonth();
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
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + "..................................ERROR on dateReference et dateBase!";
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-10522", fictivesalary.param.getLangue(), fictivesalary.getInfoSalary().getComp_id().getNmat(), rubrique.getRubrique()
					.getComp_id().getCrub(), rubrique.getRubrique().getTabl()));
			// logger
			return false;
		}
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
		// where identreprise = PA_CALCUL.wpdos.cdos
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
		// where identreprise = PA_CALCUL.wpdos.cdos
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
			C_last_cum += " where identreprise =  '" + cdos + "' ";
			C_last_cum += " AND nmat =  '" + fictivesalary.getInfoSalary().getComp_id().getNmat() + "' ";
			C_last_cum += " AND aamm >= '" + periodeDebut + "' ";
			C_last_cum += " AND aamm <  '" + dateBase + "' ";
			C_last_cum += " AND aamm not like '%99' ";
			C_last_cum += " AND rubq =  '" + c4 + "' ";
			C_last_cum += " AND nbul != 0 ";
			C_last_cum += " order by aamm desc ";

			C_last_cum = fictivesalary.getParam().isUseRetroactif() ? C_last_cum.replace("table", "Rhtprcumu") : C_last_cum.replace("table", "Rhtcumul");
			if ('O' == fictivesalary.getParam().getGenfile())
				outputtext += "\n" + "................query: " + C_last_cum;
			List l = fictivesalary.getService().find(C_last_cum);
			if (l != null && l.size() > 0)
			{
				Object[] valeur = (Object[]) l.get(0);
				last_periode = valeur[1] == null ? "" : valeur[1].toString();
				sum_montant = valeur[0] == null ? 0 : valeur[0] instanceof BigDecimal ? ((BigDecimal) valeur[0]).doubleValue() : (Double) valeur[0];
			}

			fictivesalary.getValeurRubriquePartage().setBase(sum_montant);
			fictivesalary.getValeurRubriquePartage().setAmount(sum_montant);
			fictivesalary.getValeurRubriquePartage().setRates(0);
		}
		else
		{
			String queryString = "select sum(mont), count(*) from table" + " where identreprise = '" + cdos + "'" + " and nmat = '" + fictivesalary.getInfoSalary().getComp_id().getNmat() + "'" + " and aamm >= '" + periodeDebut + "' and aamm < '" + dateBase + "'" + " and substring(aamm, 5, 2) != '99'" + " and rubq = '" + c4 + "'" + " and nbul != 0";
			queryString = fictivesalary.param.isUseRetroactif() ? queryString.replace("table", "Rhtprcumu") : queryString.replace("table", "Rhtcumul");
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + "................query: " + queryString;
			List l = fictivesalary.getService().find(queryString);
			if (l != null && l.size() > 0)
			{
				Object[] valeur = (Object[]) l.get(0);
				double sum_montant = valeur[0] == null ? 0 : valeur[0] instanceof BigDecimal ? ((BigDecimal) valeur[0]).doubleValue() : (Double) valeur[0];
				int count_montant = valeur[1] == null ? 0 : (Integer) valeur[1];
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
				if (char1 == 'S')
				{
					fictivesalary.getValeurRubriquePartage().setBase(sum_montant);
					fictivesalary.getValeurRubriquePartage().setAmount(sum_montant);
					fictivesalary.getValeurRubriquePartage().setRates(0);
				}
				else if (char1 == 'M')
				{
					fictivesalary.getValeurRubriquePartage().setBase(sum_montant);
					fictivesalary.getValeurRubriquePartage().setAmount(sum_montant / count_montant);
					fictivesalary.getValeurRubriquePartage().setRates(0);
				}
				else if (char1 == 'C')
				{
					fictivesalary.getValeurRubriquePartage().setBase(count_montant);
					fictivesalary.getValeurRubriquePartage().setAmount(count_montant);
					fictivesalary.getValeurRubriquePartage().setRates(0);
				}
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo66(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public boolean algo66(ClsFictifRubriqueClone rubrique)
	{
		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\n" + ">>algo66";
		// w_valeur := NULL;
		// w_tau := 0;
		// w_mon := 0;
		fictivesalary.getValeurRubriquePartage().setValeur(0);
		fictivesalary.getValeurRubriquePartage().setAmount(0);
		fictivesalary.getValeurRubriquePartage().setRates(0);
		//
		// -- cle
		// w_valeur := recvalnum(TO_NUMBER(PA_CALCUL.t_rub.cle1));
		// IF Pb_Calcul THEN
		// RETURN FALSE;
		// END IF;
		double valeur = rubrique.recupValeurNumeriqueGSAL(rubrique.getRubrique().getCle1());
		if (fictivesalary.param.isPbWithCalulation())
		{
			return false;
		}
		//
		// IF w_valeur IS NULL THEN
		// w_valeur := 0;
		// END IF;
		if (valeur <= 0)
			valeur = 0;
		fictivesalary.getValeurRubriquePartage().setValeur(valeur);
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
			if (fictivesalary.getValeurRubriquePartage().getBase() == 0)
			{
				fictivesalary.getValeurRubriquePartage().setBase(valeur);
				fictivesalary.getValeurRubriquePartage().setBasePlafonnee(valeur);
				fictivesalary.getValeurRubriquePartage().setAmount(valeur);
			}
			else
			{
				fictivesalary.getValeurRubriquePartage().setRates(valeur);
				fictivesalary.getValeurRubriquePartage().setAmount(fictivesalary.getValeurRubriquePartage().getBase() * valeur / 100);
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
			fictivesalary.getValeurRubriquePartage().setRates(valeur);
			if (fictivesalary.getValeurRubriquePartage().getRates() != 0)
			{
				fictivesalary.getValeurRubriquePartage().setAmount(fictivesalary.getValeurRubriquePartage().getBase() / valeur);
			}
			else
			{
				fictivesalary.getValeurRubriquePartage().setAmount(0);
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
			if (fictivesalary.getValeurRubriquePartage().getBase() == 0)
			{
				fictivesalary.getValeurRubriquePartage().setBase(valeur);
				fictivesalary.getValeurRubriquePartage().setBasePlafonnee(valeur);
				fictivesalary.getValeurRubriquePartage().setAmount(valeur);
				fictivesalary.getValeurRubriquePartage().setRates(0);
			}
			else
			{// --- multiplication
				fictivesalary.getValeurRubriquePartage().setRates(valeur);
				fictivesalary.getValeurRubriquePartage().setBasePlafonnee(fictivesalary.getValeurRubriquePartage().getBase());
				fictivesalary.getValeurRubriquePartage().setAmount(fictivesalary.getValeurRubriquePartage().getBase() * fictivesalary.getValeurRubriquePartage().getRates());
			}
		}
		// ELSE
		// PA_CALCUL.err_msg := PA_PAIE.erreurp('ERR-90058',PA_CALCUL.w_clang,PA_CALCUL.t_rub.algo,PA_CALCUL.wsal01.nmat,PA_CALCUL.t_rub.crub);
		// RETURN FALSE;
		// END IF;
		else
		{
			// logger
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90058", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(), fictivesalary.getInfoSalary().getComp_id()
					.getNmat(), rubrique.getRubrique().getComp_id().getCrub()));
			return false;
		}
		return true;
	}

	public boolean algo67(ClsFictifRubriqueClone rubrique)
	{
		// TODO Auto-generated method stub
		return false;
	}

	public boolean algo68(ClsFictifRubriqueClone rubrique)
	{
		
//		if (StringUtils.equals(fictivesalary.getParam().nomClient, ClsEntreprise.SOBRAGA))
//		{
//			fictivesalary.getValeurRubriquePartage().setRates(NumberUtils.bdnvl(fictivesalary.infoSalary.getNbjsa(),0).doubleValue());
//			fictivesalary.getValeurRubriquePartage().setAmount(fictivesalary.getValeurRubriquePartage().getBase() * fictivesalary.getValeurRubriquePartage().getRates() / 24);
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
		if(fictivesalary.workTimeFictif.getNbreJourConges() == 0 && fictivesalary.workTimeFictif.getNbreJourCongesNonPris() == 0)
		{
			fictivesalary.getValeurRubriquePartage().setBase(0);
			fictivesalary.getValeurRubriquePartage().setBasePlafonnee(0);
			fictivesalary.getValeurRubriquePartage().setRates(0);
			fictivesalary.getValeurRubriquePartage().setAmount(0);
			return true;
		}
//
//		-- Si Solde de Tout Comptes
//		   IF PA_CALCUL.stc THEN
		if(fictivesalary.getParam().isStc())
		{
//		      IF PA_CALCUL.wsal01.japa IS NULL THEN
//		         PA_CALCUL.wsal01.japa := 0;
//		      END IF;
			if(fictivesalary.getInfoSalary().getJapa() == null)
				fictivesalary.getInfoSalary().setJapa(new BigDecimal(0));
//
//		      IF PA_CALCUL.wsal01.japec IS NULL THEN
//		         PA_CALCUL.wsal01.japec := 0;
//		      END IF;
			if(fictivesalary.getInfoSalary().getJapec() == null)
				fictivesalary.getInfoSalary().setJapec(new BigDecimal(0));
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
			if(fictivesalary.getInfoSalary().getJapa().compareTo(new BigDecimal(0)) == 0 && fictivesalary.getInfoSalary().getJapec().compareTo(new BigDecimal(0)) == 0)
			{
				nbj_cstc = fictivesalary.workTimeFictif.getNbreJourConges();
				if(nbj_cstc == 0)
				{
					fictivesalary.getValeurRubriquePartage().setBase(0);
					fictivesalary.getValeurRubriquePartage().setBasePlafonnee(0);
					fictivesalary.getValeurRubriquePartage().setRates(0);
					fictivesalary.getValeurRubriquePartage().setAmount(0);
					return true;
				}
				if(fictivesalary.workTimeFictif.getMontantCongePonctuel() != 0)
				{
					fictivesalary.getValeurRubriquePartage().setAmount(fictivesalary.workTimeFictif.getMontantCongePonctuel());
					return true;
				}
				fictivesalary.getValeurRubriquePartage().setRates(nbj_cstc);
				fictivesalary.getValeurRubriquePartage().setAmount(fictivesalary.getValeurRubriquePartage().getBase() * nbj_cstc / 24);
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
			if(fictivesalary.workTimeFictif.getNbreJourConges() > fictivesalary.getInfoSalary().getJapa().doubleValue())
			{
				nbj_reste = fictivesalary.workTimeFictif.getNbreJourConges() - fictivesalary.getInfoSalary().getJapa().doubleValue();
				fictivesalary.getInfoSalary().setJapa(new BigDecimal(0));
				fictivesalary.getInfoSalary().setJapec(fictivesalary.getInfoSalary().getJapec().subtract(new BigDecimal(nbj_reste)));
			}
			else
				fictivesalary.getInfoSalary().setJapa(fictivesalary.getInfoSalary().getJapa().subtract(new BigDecimal( fictivesalary.workTimeFictif.getNbreJourConges())));
//
//		      nbj_cstc := nbj_cstc + PA_CALCUL.wsal01.japa + PA_CALCUL.wsal01.japec;
//		      nbj_cstc := nbj_cstc + PA_CALCUL.wnbjc;
//		--      nbj_cstc := nbj_cstc + PA_CALCUL.wnbjca;
			nbj_cstc += fictivesalary.getInfoSalary().getJapa().doubleValue() + fictivesalary.getInfoSalary().getJapec().doubleValue();
			nbj_cstc += fictivesalary.workTimeFictif.getNbreJourConges();
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
				fictivesalary.getValeurRubriquePartage().setBase(0);
				fictivesalary.getValeurRubriquePartage().setBasePlafonnee(0);
				fictivesalary.getValeurRubriquePartage().setRates(0);
				fictivesalary.getValeurRubriquePartage().setAmount(0);
				return true;
			}
//
//		      IF PA_CALCUL.wmntcp != 0 THEN
//		         PA_ALGO.w_mon := PA_CALCUL.wmntcp;
//		         RETURN TRUE;
//		      END IF;
			if(fictivesalary.workTimeFictif.getNbreJourConges() != 0)
			{
				fictivesalary.getValeurRubriquePartage().setAmount(fictivesalary.workTimeFictif.getMontantCongePonctuel());
				return true;
			}
//
//		      PA_ALGO.w_tau := nbj_cstc;
//		      PA_ALGO.w_mon := (PA_ALGO.w_bas * PA_ALGO.w_tau) / 24;
//		--      PA_ALGO.w_mon := (PA_ALGO.w_bas * PA_ALGO.w_tau) / 30;
//		      RETURN TRUE;
//		   END IF;
			fictivesalary.getValeurRubriquePartage().setRates(nbj_cstc);
			fictivesalary.getValeurRubriquePartage().setAmount(fictivesalary.getValeurRubriquePartage().getBase() * nbj_cstc / 24);
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
		if(StringUtils.equals(fictivesalary.getParam().fictiveCalculus,"N") && !fictivesalary.getInfoSalary().getPmcf().equalsIgnoreCase(fictivesalary.getParam().getMonthOfPay()))
		{
			fictivesalary.getValeurRubriquePartage().setBase(0);
			fictivesalary.getValeurRubriquePartage().setBasePlafonnee(0);
			fictivesalary.getValeurRubriquePartage().setRates(0);
			fictivesalary.getValeurRubriquePartage().setAmount(0);
			return true;
		}
//
//		   -- Pas de calcul si pas de conges
//		   IF PA_CALCUL.wnbjc = 0 THEN
//		      PA_ALGO.w_bas  := 0;
//		      PA_ALGO.w_basp := 0;
//		      PA_ALGO.w_tau  := 0;
//		      PA_ALGO.w_mon  := 0;
//		      RETURN TRUE;
//		   END IF;
		if(fictivesalary.workTimeFictif.getNbreJourConges() == 0)
		{
			fictivesalary.getValeurRubriquePartage().setBase(0);
			fictivesalary.getValeurRubriquePartage().setBasePlafonnee(0);
			fictivesalary.getValeurRubriquePartage().setRates(0);
			fictivesalary.getValeurRubriquePartage().setAmount(0);
			return true;
		}
//
//		   -- Pas de calcul si montant du conges deja calcule
//		   IF PA_CALCUL.wmntcp != 0 THEN
//		      PA_ALGO.w_mon := PA_CALCUL.wmntcp;
//		      RETURN TRUE;
//		   END IF;
		if(fictivesalary.workTimeFictif.getMontantCongePonctuel() != 0)
		{
			fictivesalary.getValeurRubriquePartage().setAmount(fictivesalary.workTimeFictif.getMontantCongePonctuel());
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
		fictivesalary.getValeurRubriquePartage().setRates(fictivesalary.workTimeFictif.getNbreJourConges() + fictivesalary.workTimeFictif.getNbreJourCongesNonPris());
		fictivesalary.getValeurRubriquePartage().setAmount(fictivesalary.getValeurRubriquePartage().getBase() * fictivesalary.getValeurRubriquePartage().getRates() / 24);
		if(fictivesalary.getInfoSalary().getMtcf() == null) fictivesalary.getInfoSalary().setMtcf(new BigDecimal(0));
		fictivesalary.getValeurRubriquePartage().setAmount(fictivesalary.getValeurRubriquePartage().getAmount() - fictivesalary.getInfoSalary().getMtcf().doubleValue());
		
		if(fictivesalary.getInfoSalary().getNbjcf() == null) fictivesalary.getInfoSalary().setNbjcf(new BigDecimal(0));
		fictivesalary.setTot_cgs((fictivesalary.getValeurRubriquePartage().getBase() * fictivesalary.getInfoSalary().getNbjcf().doubleValue()) / 24);
		
//
//		   RETURN TRUE;
		// TODO Auto-generated method stub
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo78(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 * , NON ACTIF EN CALCUL FICTIF CNSS
	 */
	public boolean algo69(ClsFictifRubriqueClone rubrique)
	{
//		if (StringUtils.equals(fictivesalary.getParam().nomClient, ClsEntreprise.SOBRAGA))
//		{
//			fictivesalary.getValeurRubriquePartage().setRates(NumberUtils.bdnvl(fictivesalary.infoSalary.getNbjse(),0).doubleValue());
//			fictivesalary.getValeurRubriquePartage().setAmount(fictivesalary.getValeurRubriquePartage().getBase() * fictivesalary.getValeurRubriquePartage().getRates() / 24);
//			return true;
//		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo78(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public boolean algo78(ClsFictifRubriqueClone rubrique)
	{
//		if(StringUtils.equals(fictivesalary.param.getNomClient(), ClsEntreprise.COMILOG))
//			return comilog.algo78(rubrique);
		
		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\n" + ">>algo78";
		String cdos = fictivesalary.getInfoSalary().getComp_id().getCdos();
		String crub = rubrique.getRubrique().getComp_id().getCrub();
		Double montantbareme = new Double(0);
		double tauxbareme = 0;
		fictivesalary.getValeurRubriquePartage().setInter(0);
		int i = 0, j = 0;
		String typeCalcul = fictivesalary.utilNomenclatureFictif.getLabelFromNomenclature(cdos, 99, "ALGO-78", 2, fictivesalary.param.getNlot(), fictivesalary.param.getNumeroBulletin(),
				fictivesalary.param.getMonthOfPay());
		if (ClsObjectUtil.isNull(typeCalcul) || (!"1".equals(typeCalcul) && !"2".equals(typeCalcul)))
			typeCalcul = "2";
		//
		if ("2".equals(typeCalcul))
		{
			// -- MM 12/2003 Prise en compte de ddca
			// -- Conversion de l'anciennete en nombre de jours
			if (fictivesalary.getAnciennete() != 0)
				fictivesalary.getValeurRubriquePartage().setInter(360 * fictivesalary.getAnciennete());
			i = new ClsDate(fictivesalary.getInfoSalary().getDdca()).getMonth();
			j = fictivesalary.param.getMyMonthOfPay().getMonth();
			j = j - i;
			if (j < 0)
				j = j + 12;
			i = new ClsDate(fictivesalary.getInfoSalary().getDdca()).getDay();
			if (i == 1)
				i = 0;
			else if (i == 31)
				i = 30;
			fictivesalary.getValeurRubriquePartage().setInter(fictivesalary.getValeurRubriquePartage().getInter() + (30 * (j + 1)) - i);
		}
		else
		{
			ClsDate ddtes = new ClsDate(fictivesalary.getInfoSalary().getDtes());
			j = Double.valueOf(fictivesalary.utilNomenclatureFictif.getMonthsBetween(new ClsDate(fictivesalary.param.getMonthOfPay(), "yyyyMM").getLastDayOfMonth(), fictivesalary.getInfoSalary().getDtes())).intValue();
			i = new ClsDate(fictivesalary.getInfoSalary().getDtes()).getDay();
			// recueillir le dernier jour du mois ddtes
			int i1 = new ClsDate(ddtes.getLastDayOfMonth()).getDay();
			if (i == i1)
				i = 30;
			fictivesalary.getValeurRubriquePartage().setInter((30 * (j + 1)) - i);
		}
		//
		// String queryString = " from ElementSalaireBareme "
		// + " where identreprise = '" + cdos + "'"
		// + " and crub = '" + crub + "'"
		// + " order by cdos , crub , nume";
		// //
		// String queryStringRetro = " from Rhthrubbarem "
		// + " where identreprise = '" + cdos + "'"
		// + " and crub = '" + crub + "'"
		// + " and aamm = '" + fictivesalary.param.getMonthOfPay() + "'"
		// + " and nbul = " + fictivesalary.param.getNumeroBulletin()
		// + " order by cdos , crub , nume";
		//
		// fictivesalary.getValeurRubriquePartage().setRates(0);
		// fictivesalary.getValeurRubriquePartage().setAmount(0);
		// -- Lecture de la donnee designee par la cle1
		String valeur1 = "";
		String valeur2 = "";
		int nbreBaremeRubrique = 0;
		double dblValeur1 = 0;
		double dblValeur2 = 0;
		//Rhthrubbarem oRhthrubbarem = null;
		ElementSalaireBareme oElementSalaireBareme = null;
		// List listOfBarem = fictivesalary.param.isUseRetroactif() ? fictivesalary.getService().find(queryStringRetro) :
		// fictivesalary.getService().find(queryString);
		String keyOfBaremeList = fictivesalary.param.isUseRetroactif() ? crub + this.fictivesalary.param.getMonthOfPay() + this.fictivesalary.param.getNumeroBulletin() : crub;
		List listOfBarem = (List) fictivesalary.param.getListOfRubriquebaremeMap().get(keyOfBaremeList);
		if (listOfBarem != null)
			for (Object barem : listOfBarem)
			{
				if (fictivesalary.param.isUseRetroactif())
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
					if (fictivesalary.getValeurRubriquePartage().getInter() <= dblValeur2)
						break;
					//
					fictivesalary.getValeurRubriquePartage().setValeur((dblValeur2 - dblValeur1) * tauxbareme);
					fictivesalary.getValeurRubriquePartage().setAmount(fictivesalary.getValeurRubriquePartage().getAmount() + fictivesalary.getValeurRubriquePartage().getValeur());
				}
			}
		//
		if (nbreBaremeRubrique == 0)
		{
			// logger
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90061", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(),
					rubrique.getRubrique().getComp_id().getCrub(), fictivesalary.getInfoSalary().getComp_id().getNmat()));
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + fictivesalary.param.getError();
			return false;
		}
		montantbareme = fictivesalary.getValeurRubriquePartage().getAmount() + tauxbareme * (fictivesalary.getValeurRubriquePartage().getInter() - dblValeur1);
		fictivesalary.getValeurRubriquePartage().setAmount(montantbareme);
		// -- Calcul du montant
		fictivesalary.getValeurRubriquePartage().setRates(fictivesalary.getValeurRubriquePartage().getAmount() / 360);
		montantbareme = fictivesalary.getValeurRubriquePartage().getBase() * fictivesalary.getValeurRubriquePartage().getRates() / 100;
		fictivesalary.getValeurRubriquePartage().setAmount(montantbareme);
		//
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo82(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public boolean algo82(ClsFictifRubriqueClone rubrique)
	{
		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\n" + ">>algo82";
		int n = 0;
		int nt = 0;
		double valeur = 0;
		if (fictivesalary.getWorkTimeFictif().getNbreJourCongesPonctuels() == 0)
			fictivesalary.getValeurRubriquePartage().setBase(0);
		else
		{
			fictivesalary.getValeurRubriquePartage().setRates(fictivesalary.getWorkTimeFictif().getNbreJourAbsencePourCongesPonctuels());// Jours calendaires
			if (fictivesalary.getWorkTimeFictif().getMontantCongePonctuel() != 0)
				fictivesalary.getValeurRubriquePartage().setAmount(fictivesalary.getWorkTimeFictif().getMontantCongePonctuel());
			else
			{
//				   IF PA_CALFIC.wchg THEN
//			      w_bas := w_bas + cal_con;
//			   END IF;
//			   w_bas := w_bas + fic_con;
				double base = fictivesalary.getValeurRubriquePartage().getBase();
				fictivesalary.getValeurRubriquePartage().setBase(base + this.fic_con());
				
				valeur = fictivesalary.getValeurRubriquePartage().getBase() + fictivesalary.getInfoSalary().getDapa().doubleValue() + fictivesalary.getInfoSalary().getDapec().doubleValue();
				fictivesalary.getValeurRubriquePartage().setBase(valeur);
				fictivesalary.getValeurRubriquePartage().setBasePlafonnee(valeur);
				nt = fictivesalary.getInfoSalary().getNbjtr().intValue();
				n = rechercheNombreDeJourTravailDuMois(rubrique);
				if (n <= 0)
					n = 0;
				if (nt <= 0)
					nt = 0;
				if (n == 0 && nt == 0)
					fictivesalary.getValeurRubriquePartage().setAmount(0);
				else
				{
					fictivesalary.getValeurRubriquePartage().setAmount(
							(fictivesalary.getValeurRubriquePartage().getBase() / (n + nt)) * fictivesalary.getWorkTimeFictif().getNbreJourAbsencePourCongesPonctuels());
					fictivesalary.getWorkTimeFictif().setMontantCongePonctuel(fictivesalary.getValeurRubriquePartage().getAmount());
				}
			}
		}
		//
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo83(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public boolean algo83(ClsFictifRubriqueClone rubrique)
	{
		double n = 0;
		double nt = 0;
		double montant = 0;
		// ------------------- Calcul pour un STC ----------------------
		if (fictivesalary.param.isStc() && (fictivesalary.getWorkTimeFictif().getNbreJourConges() == 0 && fictivesalary.getWorkTimeFictif().getNbreJourCongesNonPris() == 0))
		{
			n = rechercheNombreDeJourTravailDuMois(rubrique);
			//
			// -- RAZ base des conges si conges de debut de mois
			if (fictivesalary.getWorkTimeFictif().isDebutDeMois())
			{
				fictivesalary.getValeurRubriquePartage().setBase(0);
				n = 0;
			}
			//
			fictivesalary.getValeurRubriquePartage().setInter(
					fictivesalary.getInfoSalary().getDapa().doubleValue() + fictivesalary.getInfoSalary().getDapec().doubleValue() + fictivesalary.getValeurRubriquePartage().getBase());
			nt = 0;
			if (!ClsObjectUtil.isNull(fictivesalary.getInfoSalary().getNbjtr()))
				nt = fictivesalary.getInfoSalary().getNbjtr().doubleValue();
			if (nt == 0 && n == 0)
			{
				fictivesalary.getValeurRubriquePartage().setAmount(0);
				return true;
			}
			fictivesalary.getValeurRubriquePartage().setBase(fictivesalary.getValeurRubriquePartage().getInter() / (nt + n));
			fictivesalary.getValeurRubriquePartage().setRates(fictivesalary.getInfoSalary().getJapa().doubleValue() + fictivesalary.getInfoSalary().getJapec().doubleValue());
			montant = fictivesalary.getValeurRubriquePartage().getBase() * fictivesalary.getValeurRubriquePartage().getRates() - fictivesalary.getInfoSalary().getDded().doubleValue();
			montant = montant - fictivesalary.getWorkTimeFictif().getMontantCongePonctuel();
			fictivesalary.getValeurRubriquePartage().setAmount(montant);
			fictivesalary.getValeurRubriquePartage().setBasePlafonnee(montant);
			// fictivesalary.getValeurRubriquePartage().setBase(montant);
			//
			return true;
		}
		// --------------- Calcul pour un conges annuel ------------------
		if (fictivesalary.getWorkTimeFictif().getNbreJourConges() == 0 && fictivesalary.getWorkTimeFictif().getNbreJourCongesNonPris() == 0
				&& fictivesalary.getWorkTimeFictif().getNbreJourCongesAnnuelMoisAnte() == 0 && fictivesalary.getWorkTimeFictif().getNbreJourCongesAnnuelMoisSuiv() == 0)
		{
			// -- Pas de conges ce mois ci
			fictivesalary.getValeurRubriquePartage().setAmount(0);
			fictivesalary.getValeurRubriquePartage().setRates(0);
			fictivesalary.getValeurRubriquePartage().setBase(0);
		}
		else
		{
			// -- Nb jrs trav du mois
			n = rechercheNombreDeJourTravailDuMois(rubrique);
			//
			// -- RAZ base des conges si conges de debut de mois
			if (fictivesalary.getWorkTimeFictif().isDebutDeMois())
			{
				fictivesalary.getValeurRubriquePartage().setBase(0);
				n = 0;
			}
//			   IF PA_CALFIC.wchg THEN
//		      w_bas := w_bas + cal_con;
//		   END IF;
//		   w_bas := w_bas + fic_con;
			double base = fictivesalary.getValeurRubriquePartage().getBase();
			fictivesalary.getValeurRubriquePartage().setBase(base + this.fic_con());
			
			montant = fictivesalary.getInfoSalary().getDapa().doubleValue() + fictivesalary.getInfoSalary().getDapec().doubleValue() + fictivesalary.getValeurRubriquePartage().getBase();
			fictivesalary.getValeurRubriquePartage().setInter(montant);
			//
			// -- Ajout du nb de jours payes non pris (PA_CALCUL.wnbjcapnp)
			// -- pour prise en compte dans le calcul du conge
			if (!"O".equals(fictivesalary.param.getFictiveCalculus()))
			{
				// -- Pas de traitement fictif, on paie tout le premier mois
				if (fictivesalary.param.getMonthOfPay().equals(fictivesalary.getInfoSalary().getPmcf()))
				{
					montant = fictivesalary.getWorkTimeFictif().getNbreJourCongesAnnuelPayeNonPris() + fictivesalary.getInfoSalary().getNbjaf().doubleValue();
					fictivesalary.getValeurRubriquePartage().setRates(montant);
				}
				else
				{
					fictivesalary.getValeurRubriquePartage().setAmount(0);
					// fictivesalary.getValeurRubriquePartage().setBasePlafonnee(0);
					fictivesalary.getValeurRubriquePartage().setRates(0);
					fictivesalary.getValeurRubriquePartage().setBase(0);
					//
					return true;
				}
			}
			else
			{
				montant = fictivesalary.getWorkTimeFictif().getNbreJourCongesAnnuelPayeNonPris() + fictivesalary.getWorkTimeFictif().getNbreJoursAbsencePourCongeAnnuel()
						+ fictivesalary.getWorkTimeFictif().getNbreJourAbsenceCongesAnnuelMoisAnte();// +
				// fictivesalary.getWorkTimeFictif().getNbreJourCongesSalaireMoyMoisSuiv();
				fictivesalary.getValeurRubriquePartage().setRates(montant);
			}
			//
			// -- Nb jrs trav du mois depuis dernier conges
			if (!ClsObjectUtil.isNull(fictivesalary.getInfoSalary().getNbjtr()))
				nt = fictivesalary.getInfoSalary().getNbjtr().doubleValue();
			if (nt == 0 && n == 0)
			{
				fictivesalary.getValeurRubriquePartage().setAmount(0);
				return true;
			}
			//
			montant = fictivesalary.getValeurRubriquePartage().getInter() / (nt + n);
			fictivesalary.getValeurRubriquePartage().setBase(montant);
			montant = fictivesalary.getValeurRubriquePartage().getBase() * fictivesalary.getValeurRubriquePartage().getRates();
			montant = montant - fictivesalary.getInfoSalary().getDded().doubleValue() - fictivesalary.getWorkTimeFictif().getMontantCongePonctuel();
			fictivesalary.getValeurRubriquePartage().setAmount(montant);
			//
			fictivesalary.getValeurRubriquePartage().setBasePlafonnee(fictivesalary.getValeurRubriquePartage().getBase());
		}
		//
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo100(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo84(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public boolean algo84(ClsFictifRubriqueClone rubrique)
	{
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo101(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo85(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public boolean algo85(ClsFictifRubriqueClone rubrique)
	{
		String cdos = fictivesalary.getInfoSalary().getComp_id().getCdos();
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
		fictivesalary.getValeurRubriquePartage().setAmount(0);
		fictivesalary.getValeurRubriquePartage().setRates(0);
		//
		Base_annuelle = 0;
		Deja_abattement = false;
		// -- Recuperation du plafond annuel
		Plafond_annuel = convertToNumber(rubrique.getRubrique().getPmax(), rubrique);
		if (fictivesalary.param.isPbWithCalulation())
		{
			return false;
		}
		//
		// -- Recuperation de l'abattement annuel
		Abattement_annuel = convertToNumber(rubrique.getRubrique().getAbmx(), rubrique);
		if (fictivesalary.param.isPbWithCalulation())
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
		Indice_mois_de_paie = fictivesalary.param.getMyMonthOfPay().getMonth();
		//
		// -- Si il s'agit d'un solde de tous comptes pour un non gabonais,
		// -- le calcul est fait sur 12 mois
		if (fictivesalary.param.isStc())
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
		ClsDate oClsDate = new ClsDate(fictivesalary.getInfoSalary().getDtes());
		if (oClsDate.getYear() == fictivesalary.param.getMyMonthOfPay().getYear())
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
		fictivesalary.getValeurRubriquePartage().setBase(Base_mensuelle);
		fictivesalary.getValeurRubriquePartage().setBasePlafonnee(Base_mensuelle);
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
		if (ClsObjectUtil.isNull(fictivesalary.getInfoSalary().getNbpt()))
		{
			// logger
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90083", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(), fictivesalary.getInfoSalary().getComp_id()
					.getNmat(), rubrique.getRubrique().getComp_id().getCrub()));
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + fictivesalary.param.getError();
			return false;
		}
		else
		{
			if (!ClsObjectUtil.isNull(fictivesalary.getInfoSalary().getNbpt()))
			{
				Base_mensuelle = Base_mensuelle / fictivesalary.getInfoSalary().getNbpt().doubleValue();
				Base_mensuelle = arrondi2('I', 100, Base_mensuelle);
			}
		}
		// END IF;
		//
//		String queryString = " from ElementSalaireBareme " + " where identreprise = '" + cdos + "'" + " and crub = '" + crub + "'" + " and val1 <= '" + Base_mensuelle + "'" + " and val2 >= '"
//				+ Base_mensuelle + "'" + " order by cdos , crub , nume";
//		String queryStringRetro = " Rhthrubbarem " + " where identreprise = '" + cdos + "'" + " and crub = '" + crub + "'" + " and aamm = '" + fictivesalary.param.getMonthOfPay()
//				+ "'" + " and nbul = " + fictivesalary.param.getNumeroBulletin() + " and val1 <= '" + Base_mensuelle + "'" + " and val2 >= '" + Base_mensuelle + "'"
//				+ " order by cdos , crub , nume";
		
		String queryString = " from ElementSalaireBareme " + " where identreprise = '" + cdos + "'" + " and crub = '" + crub + "' order by cdos , crub , nume";
		String queryStringRetro = " from Rhthrubbarem " + " where identreprise = '" + cdos + "'" + " and crub = '" + crub + "'" + " and aamm = '" + fictivesalary.param.getMonthOfPay() + "'"
		+ " and nbul = " + fictivesalary.param.getNumeroBulletin() + " order by cdos , crub , nume";
		//
		//
		l = fictivesalary.param.isUseRetroactif() ? fictivesalary.getService().find(queryStringRetro) : fictivesalary.getService().find(queryString);

		//Rhthrubbarem oRhthrubbarem = null;
		ElementSalaireBareme oElementSalaireBareme = null;
		double valeur1=0, valeur2 = 0;
		boolean trouve = false;
		if (!ClsObjectUtil.isListEmty(l))
		{
			for (Object barem : l)
			{
				if (fictivesalary.getParam().isUseRetroactif())
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
			
//			if (fictivesalary.param.isUseRetroactif())
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
			String error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + fictivesalary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + crub + ". Tranche "
					+ fictivesalary.getValeurRubriquePartage().getInter() + " introuvable.";
			fictivesalary.param.setError(error);
		}
		//
		// -- Calcul de l'impot pour une part
		Impot_une_part = (Base_mensuelle * taux / 100) - montant;
		//
		// -- Calcul de l'impot total pour un mois
		Impot_un_mois = Impot_une_part * fictivesalary.getInfoSalary().getNbpt().doubleValue();
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
			Periode_debut = String.valueOf(fictivesalary.param.getMyMonthOfPay().getYear()) + "01";
			if (fictivesalary.param.isUseRetroactif())
			{
				l = fictivesalary.getService().find(
						"select sum(mont) from Rhtprcumu" + " where identreprise = '" + cdos + "'" + " and nmat = '" + fictivesalary.getInfoSalary().getComp_id().getNmat() + "'"
								+ " and aamm >= '" + Periode_debut + "'" + " and aamm < '" + fictivesalary.param.getMonthOfPay() + "'" + " and rubq = '"
								+ rubrique.getRubrique().getRcon() + "'" + " and nbul != 0");
				if (!ClsObjectUtil.isListEmty(l))
					Impot_deja_paye = (Double) l.get(0);
			}
			else
			{
				l = fictivesalary.getService().find(
						"select sum(mont) from Rhtcumul" + " where identreprise = '" + cdos + "'" + " and nmat = '" + fictivesalary.getInfoSalary().getComp_id().getNmat() + "'"
								+ " and aamm >= '" + Periode_debut + "'" + " and aamm < '" + fictivesalary.param.getMonthOfPay() + "'" + " and rubq = '"
								+ rubrique.getRubrique().getRcon() + "'" + " and nbul != 0");
				if (!ClsObjectUtil.isListEmty(l))
					Impot_deja_paye = (Double) l.get(0);
			}
		}
		Cotisation_du_mois = Impot_Annuel - Impot_deja_paye;
		//
		// -- Affectation des valeurs dans pacalc
		fictivesalary.getValeurRubriquePartage().setRates(0);
		fictivesalary.getValeurRubriquePartage().setAmount(Cotisation_du_mois);
		//
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo86(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public boolean algo86(ClsFictifRubriqueClone rubrique)
	{
		// String cdos = fictivesalary.getInfoSalary().getComp_id().getCdos();
		// String crub = rubrique.getRubrique().getComp_id().getCrub();
		Double montant = new Double(0);
		int nombre_de_mois = 0;
		Date Date_Debut = null;
		// Date_Fin DATE;
		Date Date_Fin = null;
		//
		//
		fictivesalary.getValeurRubriquePartage().setAmount(0);
		fictivesalary.getValeurRubriquePartage().setRates(0);
		//
		// -- Si aucun jour de conges, il est inutile de continuer
		fictivesalary.getValeurRubriquePartage().setRates(fictivesalary.getWorkTimeFictif().getNbreJourCongesSalaireMoyMois() + fictivesalary.getWorkTimeFictif().getNbreJourPayeSupplPayeNonPris());
		if (fictivesalary.getValeurRubriquePartage().getRates() == 0)
		{
			fictivesalary.getValeurRubriquePartage().setBase(0);
			fictivesalary.getValeurRubriquePartage().setRates(0);
			fictivesalary.getValeurRubriquePartage().setAmount(0);
			//
			return true;
		}
		// -------------------- CALCUL DE LA BASE ------------------------------------------
		// -- La base de calcul est egale a la base du mois plus les droits aux conges memorises
		fictivesalary.getValeurRubriquePartage().setBase(
				fictivesalary.getValeurRubriquePartage().getBase() + fictivesalary.getInfoSalary().getDapa().doubleValue() + fictivesalary.getInfoSalary().getDapec().doubleValue());
		
		fictivesalary.getValeurRubriquePartage().setBase( fictivesalary.getValeurRubriquePartage().getBase() +this.fic_con());
		// -- Si la base est nulle, il est inutile de continuer
		if (fictivesalary.getValeurRubriquePartage().getBase() == 0)
		{
			fictivesalary.getValeurRubriquePartage().setBase(0);
			fictivesalary.getValeurRubriquePartage().setRates(0);
			fictivesalary.getValeurRubriquePartage().setAmount(0);
			//
			return true;
		}
		//
		String error = "";
		// -- Verification si date de retour conges annuels renseignee
		if (ClsObjectUtil.isNull(fictivesalary.getInfoSalary().getDrtcg()))
		{
			// logger
			error = "Sal " + fictivesalary.getInfoSalary().getComp_id().getNmat() + ". Dernier retour conges non renseignee.  Verifier fiche salarie.";
			fictivesalary.param.setError(error);
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + fictivesalary.param.getError();
			return false;
		}
		if (ClsObjectUtil.isNull(fictivesalary.getInfoSalary().getDtes()))
		{
			// logger
			error = "Sal " + fictivesalary.getInfoSalary().getComp_id().getNmat() + ". Dernier entree societe non renseignee.  Verifier fiche salarie.";
			fictivesalary.param.setError(error);
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + fictivesalary.param.getError();
			return false;
		}
		// -- Verification de la coherence de la date de retour conges annuels
		if (fictivesalary.getInfoSalary().getDrtcg().getTime() < new ClsDate("01/01/1990", "dd/MM/yyyy").getDate().getTime())
		{
			// logger
			error = "Sal " + fictivesalary.getInfoSalary().getComp_id().getNmat() + ". Dernier retour conges=." + fictivesalary.getInfoSalary().getDrtcg() + "  Verifier fiche salarie.";
			fictivesalary.param.setError(error);
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + fictivesalary.param.getError();
			return false;
		}
		//
		// -- Calcul du nombre de mois ecoule depuis le dernier conges
		//
		// -- Determination de la date de debut
		Date_Debut = ClsObjectUtil.isNull(fictivesalary.getInfoSalary().getDrtcg()) ? ClsDate.getFirstDayOfMonth(fictivesalary.getInfoSalary().getDtes()) : ClsDate.getFirstDayOfMonth(fictivesalary
				.getInfoSalary().getDrtcg());
		//
		// -- Determination de la date de fin
		Date_Fin = ClsObjectUtil.isNull(fictivesalary.getInfoSalary().getPmcf()) ? ClsDate.getLastDayOfMonth(new ClsDate(fictivesalary.param.getMonthOfPay() + "01", "yyyyMMdd").getDate()) : ClsDate
				.getLastDayOfMonth(new ClsDate(fictivesalary.getInfoSalary().getPmcf() + "01", "yyyyMMdd").getDate());
		// -- Calcul du nombre de mois ecoule depuis le dernier conges
		nombre_de_mois = ClsDate.getMonthsBetween(Date_Fin, Date_Debut) + 1;
		// -- Calcul de la moyenne, affectation a la base
		if (nombre_de_mois > 0)
		{
			fictivesalary.getValeurRubriquePartage().setBase(fictivesalary.getValeurRubriquePartage().getBase() / nombre_de_mois);
			montant = arrondi2('I', 1, fictivesalary.getValeurRubriquePartage().getBase());
			fictivesalary.getValeurRubriquePartage().setBase(montant);
		}
		else
		{
			// logger
			error = "Sal: " + fictivesalary.getInfoSalary().getComp_id().getNmat() + ". Algo 86 : Pb calcul nombre de mois (NULL ou zero)";
			fictivesalary.param.setError(error);
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + fictivesalary.param.getError();
			return false;
		}
		// --------------------- Application d'un algo31 ------------------------------------------
		//
		if (fictivesalary.getWorkTimeFictif().getNbreJourCongesSalaireMoyMois() == 0)
			fictivesalary.getWorkTimeFictif().setNbreJourCongesSalaireMoyMois(fictivesalary.getWorkTimeFictif().getNbreJoursAbsencePourCongeAnnuel());
		//
		if (ClsObjectUtil.isNull(fictivesalary.getInfoSalary().getJapa()))
			fictivesalary.getInfoSalary().setJapa(new BigDecimal(0));
		if (ClsObjectUtil.isNull(fictivesalary.getInfoSalary().getJapec()))
			fictivesalary.getInfoSalary().setJapec(new BigDecimal(0));
		//
		if (fictivesalary.getWorkTimeFictif().getNbreJourCongesSalaireMoyMois() == 0 && fictivesalary.getWorkTimeFictif().getNbreJourPayeSupplPayeNonPris() == 0)
		{
			fictivesalary.getValeurRubriquePartage().setBase(0);
			fictivesalary.getValeurRubriquePartage().setRates(0);
			fictivesalary.getValeurRubriquePartage().setAmount(0);
			//
			return true;
		}
		//
		double i = fictivesalary.getWorkTimeFictif().getNbreJourCongesSalaireMoyMois() + fictivesalary.getWorkTimeFictif().getNbreJourPayeSupplPayeNonPris();
		fictivesalary.getValeurRubriquePartage().setRates(i);
		montant = fictivesalary.getValeurRubriquePartage().getBase() * fictivesalary.getValeurRubriquePartage().getRates() / 30;
		fictivesalary.getValeurRubriquePartage().setAmount(montant);
		fictivesalary.getValeurRubriquePartage().setBasePlafonnee(fictivesalary.getValeurRubriquePartage().getBase());
		//
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#algo87(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public boolean algo87(ClsFictifRubriqueClone rubrique)
	{
		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\n" + ">>algo87";
		String cdos = fictivesalary.getInfoSalary().getComp_id().getCdos();
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
		// + " where identreprise = '" + cdos + "'"
		// + " and crub = '" + crub + "'"
		// + " order by cdos , crub , nume";
		// String queryStringRetro = " from Rhthrubbarem "
		// + " where identreprise = '" + cdos + "'"
		// + " and crub = '" + crub + "'"
		// + " and aamm = '" + fictivesalary.param.getMonthOfPay() + "'"
		// + " and nbul = " + fictivesalary.param.getNumeroBulletin()
		// + " order by cdos , crub , nume";
		//
		fictivesalary.getValeurRubriquePartage().setRates(0);
		fictivesalary.getValeurRubriquePartage().setAmount(0);
		// -- Chargement de la base annuelle avec la base calculee
		Base_annuelle = fictivesalary.getValeurRubriquePartage().getBase();
		//
		// -- Calcul de l'indice du mois
		Indice_mois_de_paie = fictivesalary.param.getMyMonthOfPay().getMonth();
		//
		// -- Si il s'agit d'un solde de tous comptes pour un non gabonais,
		// -- le calcul est fait sur 12 mois
		if (fictivesalary.param.isStc())
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
		ClsDate oDtes = new ClsDate(fictivesalary.getInfoSalary().getDtes());
		if (oDtes.getYear() == fictivesalary.param.getMyMonthOfPay().getYear())
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
		// List listOfBarem = fictivesalary.param.isUseRetroactif() ? fictivesalary.getService().find(queryStringRetro) :
		// fictivesalary.getService().find(queryString);
		// @emmanuel: add to not call multiple times the same query
		String keyOfBaremeList = fictivesalary.param.isUseRetroactif() ? crub + this.fictivesalary.param.getMonthOfPay() + this.fictivesalary.param.getNumeroBulletin() : crub;
		List listOfBarem = (List) fictivesalary.param.getListOfRubriquebaremeMap().get(keyOfBaremeList);
		if (listOfBarem != null)
			for (Object barem : listOfBarem)
			{
				if (fictivesalary.param.isUseRetroactif())
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
				Borne_inferieure = convertToNumber(valeur1, rubrique);
				Borne_superieure = convertToNumber(valeur2, rubrique);
				//
				// if (!ClsObjectUtil.isNull(valeur1) && !ClsObjectUtil.isNull(valeur2))
				// {
				// dblValeur1 = rubrique.convertToNumber(valeur1);
				// dblValeur2 = rubrique.convertToNumber(valeur2);
				// if (fictivesalary.getValeurRubriquePartage().getInter() <= dblValeur2)
				// break;
				// //
				// fictivesalary.getValeurRubriquePartage().setValeur((dblValeur2 - dblValeur1) * taux);
				// fictivesalary.getValeurRubriquePartage().setAmount(fictivesalary.getValeurRubriquePartage().getAmount() +
				// fictivesalary.getValeurRubriquePartage().getValeur());
				// }
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
			error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + fictivesalary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + crub + ". Il manque le bareme, ecran 6";
			fictivesalary.param.setError(error);
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + fictivesalary.param.getError();
			return false;
		}
		else if (Nombre_Lignes == 50)
		{
			error = "ALGO" + rubrique.getRubrique().getAlgo() + ", Sal " + fictivesalary.getInfoSalary().getComp_id().getNmat() + ", Rbq " + crub + ". Il manque le bareme, ecran 6";
			fictivesalary.param.setError(error);
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + fictivesalary.param.getError();
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
			Periode_debut = String.valueOf(fictivesalary.param.getMyMonthOfPay().getYear()).concat("01");
			List l = null;
			if (fictivesalary.param.isUseRetroactif())
			{
				l = fictivesalary.getService().find(
						"select sum(mont) from Rhtprcumu" + " where identreprise = '" + cdos + "'" + " and nmat = '" + fictivesalary.getInfoSalary().getComp_id().getNmat() + "'"
								+ " and aamm >= '" + Periode_debut + "'" + " and aamm < '" + fictivesalary.param.getMonthOfPay() + "'" + " and rubq = '"
								+ rubrique.getRubrique().getRcon() + "'" + " and nbul != 0");
			}
			else
			{
				l = fictivesalary.getService().find(
						"select sum(mont) from Rhtcumul" + " where identreprise = '" + cdos + "'" + " and nmat = '" + fictivesalary.getInfoSalary().getComp_id().getNmat() + "'"
								+ " and aamm >= '" + Periode_debut + "'" + " and aamm < '" + fictivesalary.param.getMonthOfPay() + "'" + " and rubq = '"
								+ rubrique.getRubrique().getRcon() + "'" + " and nbul != 0");
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
			fictivesalary.getValeurRubriquePartage().setBase(Base_annuelle / Indice_mois_de_paie);
			montant = arrondi2('I', 1, Base_annuelle / Indice_mois_de_paie);
			fictivesalary.getValeurRubriquePartage().setBase(montant);
		}
		fictivesalary.getValeurRubriquePartage().setBasePlafonnee(Base_mensuelle);
		fictivesalary.getValeurRubriquePartage().setRates(0);
		fictivesalary.getValeurRubriquePartage().setAmount(Cotisation_du_mois);
		//
		return true;
	}
	
	
	public boolean algo89(ClsFictifRubriqueClone rubrique)
	{
		
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#arrondi2(char, double, double)
	 */
	public double arrondi2(char p_type, double p_arro, double p_mon)
	{
		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\n" + ">>arrondi2";
		// if('O' == fictivesalary.param.getGenfile()) outputtext += "\n" + "..........p_arro:" + p_arro);
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
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#calculPlafond(java.lang.String, java.lang.String)
	 */
	public double calculPlafond(String char16, String rubrique)
	{
		// if('O' == fictivesalary.param.getGenfile()) outputtext += "\n" + ">>calculPlafond");
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
		w_plafmNumber = fictivesalary.utilNomenclatureFictif.getAmountOrRateFromNomenclature(fictivesalary.getInfoSalary().getComp_id().getCdos(), 96, char2, 1, fictivesalary.param.getNlot(),
				fictivesalary.param.getNumeroBulletin(), fictivesalary.param.getMoisPaieCourant(), ClsEnumeration.EnColumnToRead.AMOUNT);
		w_plaftNumber = fictivesalary.utilNomenclatureFictif.getAmountOrRateFromNomenclature(fictivesalary.getInfoSalary().getComp_id().getCdos(), 96, char2, 1, fictivesalary.param.getNlot(),
				fictivesalary.param.getNumeroBulletin(), fictivesalary.param.getMoisPaieCourant(), ClsEnumeration.EnColumnToRead.RATES);
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
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90081", fictivesalary.param.getLangue(), rubrique, char2, fictivesalary.getInfoSalary().getComp_id().getNmat()));
			fictivesalary.param.setPbWithCalulation(true);
			return 0;
		}
		return w_plaf;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#concat43(int, java.lang.String)
	 */
	public String concat43(int key, String rubrique)
	{
		int w_cle = key;
		String char16 = "";
		boolean valuedefault = false;
		//
		if (w_cle >= 801 && w_cle <= 899)
			// char16 = fictivesalary.rechercheZoneLibre(w_cle);
			char16 = fictivesalary.rechercheZoneLibre(w_cle - 800);
		else
		{
			switch (w_cle)
			{
			case 1:
				char16 = fictivesalary.getInfoSalary().getNiv1();
				break;
			case 2:
				char16 = fictivesalary.getInfoSalary().getNiv2();
				break;
			case 3:
				char16 = fictivesalary.getInfoSalary().getNiv3();
				break;
			case 4:
				char16 = fictivesalary.getInfoSalary().getComp_id().getNmat();
				break;
			case 11:
				char16 = fictivesalary.getInfoSalary().getSexe();
				break;
			case 19:
				char16 = new ClsDate(fictivesalary.getInfoSalary().getDtna(), "yyyy-MM-dd").getFormatedDate('/');
				break;
			case 21:
				char16 = fictivesalary.getInfoSalary().getNato();
				break;
			case 22:
				char16 = fictivesalary.getInfoSalary().getSitf();
				break;
			case 23:
				char16 = String.valueOf(fictivesalary.getInfoSalary().getNbcj());
				break;
			case 24:
				if (fictivesalary.getInfoSalary().getNbec() == null)
					fictivesalary.getInfoSalary().setNbec(0);
				char16 = ClsStringUtil.formatNumber(fictivesalary.getInfoSalary().getNbec(), "000000000000000000");
				break;
			case 25:
				if (fictivesalary.getInfoSalary().getNbfe() == null)
					fictivesalary.getInfoSalary().setNbfe(0);
				char16 = ClsStringUtil.formatNumber(fictivesalary.getInfoSalary().getNbfe(), "000000000000000000");
				break;
			case 26:
				if (fictivesalary.getInfoSalary().getNbpt() == null)
					fictivesalary.getInfoSalary().setNbpt(new BigDecimal(0));
				char16 = ClsStringUtil.formatNumber(fictivesalary.getInfoSalary().getNbpt(), "000000000000000000");
				break;
			case 27:
				char16 = fictivesalary.getInfoSalary().getModp();
				break;
			case 34:
				char16 = fictivesalary.getInfoSalary().getVild();
				break;
			case 39:
				char16 = fictivesalary.getInfoSalary().getCat();
				break;
			case 40:
				char16 = fictivesalary.getInfoSalary().getEch();
				break;
			case 43:
				char16 = fictivesalary.getInfoSalary().getGrad();
				break;
			case 44:
				char16 = fictivesalary.getInfoSalary().getFonc();
				break;
			case 45:
				char16 = fictivesalary.getInfoSalary().getAfec();
				break;
			case 46:
				char16 = fictivesalary.getInfoSalary().getCodf();
				break;
			case 47:
				ClsStringUtil.formatNumber(fictivesalary.getInfoSalary().getIndi(), "000000000000000000");
				break;
			case 48:
				char16 = fictivesalary.getInfoSalary().getCtat();
				break;
			case 58:
				char16 = fictivesalary.getInfoSalary().getHifo();
				break;
			case 59:
				char16 = fictivesalary.getInfoSalary().getZli1();
				break;
			case 60:
				char16 = fictivesalary.getInfoSalary().getZli2();
				break;
			case 61:
				char16 = new ClsDate(fictivesalary.getInfoSalary().getDtes(), "yyyy-MM-dd").getFormatedDate('/');
				break;
			case 63:
				char16 = new ClsDate(fictivesalary.getInfoSalary().getDtit(), "yyyy-MM-dd").getFormatedDate('/');
				break;
			case 64:
				char16 = new ClsDate(fictivesalary.getInfoSalary().getDdca(), "yyyy-MM-dd").getFormatedDate('/');
				break;
			case 65:
				char16 = new ClsDate(fictivesalary.getInfoSalary().getDepr(), "yyyy-MM-dd").getFormatedDate('/');
				break;
			case 67:
				char16 = fictivesalary.getInfoSalary().getTypc();
				break;
			case 70:
				char16 = fictivesalary.getInfoSalary().getRegi();
				break;
			case 71:
				char16 = fictivesalary.getInfoSalary().getZres();
				break;
			case 72:
				char16 = fictivesalary.getInfoSalary().getDmst();
				break;
			default:
				valuedefault = true;
				break;
			}
		}
		if (valuedefault)
		{
			// logger
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90082", fictivesalary.param.getLangue(), rubrique, key, fictivesalary.getInfoSalary().getComp_id().getNmat()));
			fictivesalary.param.setPbWithCalulation(true);
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + fictivesalary.param.getError();
			return "";
		}
		return char16;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#concatene(int, int, java.lang.String)
	 */
//	public class Concateneur
//	{
//		public boolean valdefaut = false;
//		public String valeur = StringUtils.EMPTY;
//	}
	
	private Concateneur _getValFromCle(int cle, String rubrique)
	{
		String char30 = StringUtils.EMPTY;
		boolean valuedefault = false;
		Concateneur result = new Concateneur();
		fictivesalary.param.setPbWithCalulation(false);
		
		ClsInfoSalaryClone info =  fictivesalary.getInfoSalary();
		//
		if (cle >= 801 && cle <= 899)
		{
			char30 = fictivesalary.rechercheZoneLibre(cle - 800);
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
				char30 = ClsStringUtil.formatNumber(fictivesalary.getInfoSalary().getNbcj(),"00");
				break;
			case 142:
				char30 = ClsStringUtil.formatNumber(fictivesalary.getInfoSalary().getNbef(),"00");
				break;
			case 24:
				char30 = ClsStringUtil.formatNumber(fictivesalary.getInfoSalary().getNbec(),"00");
				break;
			case 25:
				char30 = ClsStringUtil.formatNumber(fictivesalary.getInfoSalary().getNbfe(),"00");
				break;
			case 26:
				// spécifique cnss
//				if (StringUtils.equals(fictivesalary.param.nomClient, ClsEntreprise.CNSS))
//				{
//					char30 =  String.valueOf(fictivesalary.getInfoSalary().getNbpt());
//				}
//				else
					char30 = ClsStringUtil.formatNumber(fictivesalary.getInfoSalary().getNbpt(),"00");
					
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
				char30 = this.tempOraSubstring(info.getCat(), 1,3); 
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
				char30 = ClsStringUtil.formatNumber(fictivesalary.getInfoSalary().getIndi(),"000");
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
				char30 = String.valueOf(fictivesalary.getInfoSalary().getNpie());
				break;
			case 300:
				char30 = ClsStringUtil.formatNumber(fictivesalary.getAgeOfAgent(),"00");
				break;
			case 301:
				char30 = ClsStringUtil.formatNumber(fictivesalary.getAnciennete(),"00");
				break;
			case 302:
				char30 = ClsStringUtil.formatNumber(new ClsDate(fictivesalary.param.getMonthOfPay(),"yyyyMM").getMonth(),"00");
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
	
	public String concatene(int cle1, int cle2, String rubrique)
	{
		// if('O' == salary.getParam().getGenfile()) outputtext += "\n" + ">>concatene");
		// if('O' == salary.getParam().getGenfile()) outputtext += "\n" + "............cle1 :" +cle1);
		// if('O' == salary.getParam().getGenfile()) outputtext += "\n" + "............cle2 :" +cle2);
		Concateneur concat = this._getValFromCle(cle1, rubrique);
		if (concat.valdefaut)
		{
			// logger
			fictivesalary.param.setError(
					fictivesalary.param
							.errorMessage("ERR-90082", fictivesalary.param.getLangue(), rubrique, ClsStringUtil.formatNumber(cle1, "000"), fictivesalary.getInfoSalary().getComp_id().getNmat()));
			fictivesalary.param.setPbWithCalulation(true);
			if('O' == fictivesalary.param.getGenfile()) outputtext += "\n" + "----Return on error clé 1 = " + concat.valeur;
			return concat.valeur;
		}
		String char10 = StringUtil.oraLTrim(StringUtil.oraRTrim(concat.valeur != null ?concat.valeur:StringUtils.EMPTY, " "), " ");
		// ajout yannick
		concat = this._getValFromCle(cle2, rubrique);
		String char20 = StringUtils.EMPTY;
		if(! concat.valdefaut)
			char20 = StringUtil.oraLTrim(concat.valeur != null ?concat.valeur:StringUtils.EMPTY," ");
		
		 return char10 + StringUtil.oraSubstring(char20, 1, 10 - char20.length());	
	}
	
	public String concatene1(int cle1, int cle2, String rubrique)
	{
		// if('O' == fictivesalary.param.getGenfile()) outputtext += "\n" + ">>concatene");
		// if('O' == fictivesalary.param.getGenfile()) outputtext += "\n" + "............cle1 :" +cle1);
		// if('O' == fictivesalary.param.getGenfile()) outputtext += "\n" + "............cle2 :" +cle2);
		String char30 = "";
		boolean valuedefault = false;
		fictivesalary.param.setPbWithCalulation(false);
		//
		if (cle1 >= 801 && cle1 <= 899)
		{
			char30 = fictivesalary.rechercheZoneLibre(cle1 -800);
		}
		else
		{
			switch (cle1)
			{
			case 1:
				char30 = fictivesalary.getInfoSalary().getNiv1().length() > 2 ? fictivesalary.getInfoSalary().getNiv1().substring(0, 2 + 1) : fictivesalary.getInfoSalary().getNiv1();
				break;
			case 2:
				char30 = fictivesalary.getInfoSalary().getNiv2().length() > 2 ? fictivesalary.getInfoSalary().getNiv2().substring(0, 2 + 1) : fictivesalary.getInfoSalary().getNiv2();
				break;
			case 3:
				char30 = fictivesalary.getInfoSalary().getNiv3().length() > 7 ? fictivesalary.getInfoSalary().getNiv3().substring(0, 7 + 1) : fictivesalary.getInfoSalary().getNiv3();
				break;
			case 8:
				char30 = fictivesalary.getInfoSalary().getEqui().length() > 7 ? fictivesalary.getInfoSalary().getEqui().substring(0, 7 + 1) : fictivesalary.getInfoSalary().getEqui();
				break;
			case 11:
				char30 = fictivesalary.getInfoSalary().getSexe().length() > 0 ? fictivesalary.getInfoSalary().getSexe().substring(0, 0 + 1) : fictivesalary.getInfoSalary().getSexe();
				break;
			case 21:
				char30 = fictivesalary.getInfoSalary().getNato().length() > 2 ? fictivesalary.getInfoSalary().getNato().substring(0, 2 + 1) : fictivesalary.getInfoSalary().getNato();
				break;
			case 22:
				char30 = fictivesalary.getInfoSalary().getSitf().length() > 0 ? fictivesalary.getInfoSalary().getSitf().substring(0, 0 + 1) : fictivesalary.getInfoSalary().getSitf();
				break;
			case 23:
				char30 = String.valueOf(fictivesalary.getInfoSalary().getNbcj());
				break;
			case 24:
				char30 = String.valueOf(fictivesalary.getInfoSalary().getNbec());
				break;
			case 25:
				char30 = String.valueOf(fictivesalary.getInfoSalary().getNbfe());
				break;
			case 27:
				char30 = fictivesalary.getInfoSalary().getModp().length() > 0 ? fictivesalary.getInfoSalary().getModp().substring(0, 0 + 1) : fictivesalary.getInfoSalary().getModp();
				break;
			case 34:
				char30 = fictivesalary.getInfoSalary().getVild().length() > 9 ? fictivesalary.getInfoSalary().getVild().substring(0, 9 + 1) : fictivesalary.getInfoSalary().getVild();
				break;
			case 39:
				char30 = fictivesalary.getInfoSalary().getCat().length() > 2 ? fictivesalary.getInfoSalary().getCat().substring(0, 2 + 1) : fictivesalary.getInfoSalary().getCat();
				break;
			case 40:
				char30 = fictivesalary.getInfoSalary().getEch().length() > 2 ? fictivesalary.getInfoSalary().getEch().substring(0, 2 + 1) : fictivesalary.getInfoSalary().getEch();
				break;
			case 43:
				char30 = fictivesalary.getInfoSalary().getGrad().length() > 3 ? fictivesalary.getInfoSalary().getGrad().substring(0, 3 + 1) : fictivesalary.getInfoSalary().getGrad();
				break;
			case 44:
				char30 = fictivesalary.getInfoSalary().getFonc().length() > 3 ? fictivesalary.getInfoSalary().getFonc().substring(0, 3 + 1) : fictivesalary.getInfoSalary().getFonc();
				break;
			case 45:
				char30 = fictivesalary.getInfoSalary().getAfec().length() > 2 ? fictivesalary.getInfoSalary().getAfec().substring(0, 2 + 1) : fictivesalary.getInfoSalary().getAfec();
				break;
			case 47:
				char30 = String.valueOf(fictivesalary.getInfoSalary().getIndi());
				break;
			case 48:
				char30 = fictivesalary.getInfoSalary().getCtat().length() > 1 ? fictivesalary.getInfoSalary().getCtat().substring(0, 1 + 1) : fictivesalary.getInfoSalary().getCtat();
				break;
			case 58:
				char30 = fictivesalary.getInfoSalary().getHifo().length() > 1 ? fictivesalary.getInfoSalary().getHifo().substring(0, 1 + 1) : fictivesalary.getInfoSalary().getHifo();
				break;
			case 59:
				char30 = fictivesalary.getInfoSalary().getZli1().length() > 9 ? fictivesalary.getInfoSalary().getZli1().substring(0, 9 + 1) : fictivesalary.getInfoSalary().getZli1();
				break;
			case 60:
				char30 = fictivesalary.getInfoSalary().getZli2().length() > 9 ? fictivesalary.getInfoSalary().getZli2().substring(0, 9 + 1) : fictivesalary.getInfoSalary().getZli2();
				break;
			case 67:
				char30 = fictivesalary.getInfoSalary().getTypc().length() > 0 ? fictivesalary.getInfoSalary().getTypc().substring(0, 0 + 1) : fictivesalary.getInfoSalary().getTypc();
				break;
			case 70:
				char30 = fictivesalary.getInfoSalary().getRegi().length() > 0 ? fictivesalary.getInfoSalary().getRegi().substring(0, 0 + 1) : fictivesalary.getInfoSalary().getRegi();
				break;
			case 71:
				char30 = fictivesalary.getInfoSalary().getZres().length() > 0 ? fictivesalary.getInfoSalary().getZres().substring(0, 0 + 1) : fictivesalary.getInfoSalary().getZres();
				break;
			case 72:
				char30 = fictivesalary.getInfoSalary().getDmst().length() > 0 ? fictivesalary.getInfoSalary().getDmst().substring(0, 0 + 1) : fictivesalary.getInfoSalary().getDmst();
				break;
			case 73:
				char30 = String.valueOf(fictivesalary.getInfoSalary().getNpie());
				break;
			default:
				valuedefault = true;
				break;
			}
		}
		if (valuedefault)
		{
			// logger
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90082", fictivesalary.param.getLangue(), rubrique, ClsStringUtil.formatNumber(cle1, "000"), fictivesalary
					.getInfoSalary().getComp_id().getNmat()));
			fictivesalary.param.setPbWithCalulation(true);
			if ('O' == fictivesalary.param.getGenfile())
				outputtext += "\n" + "----Return on error clé 1 = " + char30;
			return char30;
		}
		String char10 = char30;
		// ajout yannick
		char30 = "";
		valuedefault = false;
		//
		if (cle2 >= 801 && cle2 <= 899)
			char30 = fictivesalary.rechercheZoneLibre(cle2 - 800);
		else
		{
			if (cle2 >= 0)
			{
				switch (cle2)
				{
				case 1:
					char30 = fictivesalary.getInfoSalary().getNiv1().length() > 2 ? fictivesalary.getInfoSalary().getNiv1().substring(0, 2 + 1) : fictivesalary.getInfoSalary().getNiv1();
					break;
				case 2:
					char30 = fictivesalary.getInfoSalary().getNiv2().length() > 2 ? fictivesalary.getInfoSalary().getNiv2().substring(0, 2 + 1) : fictivesalary.getInfoSalary().getNiv2();
					break;
				case 3:
					char30 = fictivesalary.getInfoSalary().getNiv3().length() > 7 ? fictivesalary.getInfoSalary().getNiv3().substring(0, 7 + 1) : fictivesalary.getInfoSalary().getNiv3();
					break;
				case 8:
					char30 = fictivesalary.getInfoSalary().getEqui().length() > 7 ? fictivesalary.getInfoSalary().getEqui().substring(0, 7 + 1) : fictivesalary.getInfoSalary().getEqui();
					break;
				case 11:
					char30 = fictivesalary.getInfoSalary().getSexe().length() > 0 ? fictivesalary.getInfoSalary().getSexe().substring(0, 0 + 1) : fictivesalary.getInfoSalary().getSexe();
					break;
				case 21:
					char30 = fictivesalary.getInfoSalary().getNato().length() > 2 ? fictivesalary.getInfoSalary().getNato().substring(0, 2 + 1) : fictivesalary.getInfoSalary().getNato();
					break;
				case 22:
					char30 = fictivesalary.getInfoSalary().getSitf().length() > 0 ? fictivesalary.getInfoSalary().getSitf().substring(0, 0 + 1) : fictivesalary.getInfoSalary().getSitf();
					break;
				case 23:
					char30 = String.valueOf(fictivesalary.getInfoSalary().getNbcj());
					break;
				case 24:
					char30 = String.valueOf(fictivesalary.getInfoSalary().getNbec());
					break;
				case 25:
					char30 = String.valueOf(fictivesalary.getInfoSalary().getNbfe());
					break;
				case 27:
					char30 = fictivesalary.getInfoSalary().getModp().length() > 0 ? fictivesalary.getInfoSalary().getModp().substring(0, 0 + 1) : fictivesalary.getInfoSalary().getModp();
					break;
				case 34:
					char30 = fictivesalary.getInfoSalary().getVild().length() > 9 ? fictivesalary.getInfoSalary().getVild().substring(0, 9 + 1) : fictivesalary.getInfoSalary().getVild();
					break;
				case 39:
					char30 = fictivesalary.getInfoSalary().getCat().length() > 2 ? fictivesalary.getInfoSalary().getCat().substring(0, 2 + 1) : fictivesalary.getInfoSalary().getCat();
					break;
				case 40:
					char30 = fictivesalary.getInfoSalary().getEch().length() > 2 ? fictivesalary.getInfoSalary().getEch().substring(0, 2 + 1) : fictivesalary.getInfoSalary().getEch();
					break;
				case 43:
					char30 = fictivesalary.getInfoSalary().getGrad().length() > 3 ? fictivesalary.getInfoSalary().getGrad().substring(0, 3 + 1) : fictivesalary.getInfoSalary().getGrad();
					break;
				case 44:
					char30 = fictivesalary.getInfoSalary().getFonc().length() > 3 ? fictivesalary.getInfoSalary().getFonc().substring(0, 3 + 1) : fictivesalary.getInfoSalary().getFonc();
					break;
				case 45:
					char30 = fictivesalary.getInfoSalary().getAfec().length() > 2 ? fictivesalary.getInfoSalary().getAfec().substring(0, 2 + 1) : fictivesalary.getInfoSalary().getAfec();
					break;
				case 47:
					char30 = String.valueOf(fictivesalary.getInfoSalary().getIndi());
					break;
				case 48:
					char30 = fictivesalary.getInfoSalary().getCtat().length() > 1 ? fictivesalary.getInfoSalary().getCtat().substring(0, 1 + 1) : fictivesalary.getInfoSalary().getCtat();
					break;
				case 58:
					char30 = fictivesalary.getInfoSalary().getHifo().length() > 1 ? fictivesalary.getInfoSalary().getHifo().substring(0, 1 + 1) : fictivesalary.getInfoSalary().getHifo();
					break;
				case 59:
					char30 = fictivesalary.getInfoSalary().getZli1().length() > 9 ? fictivesalary.getInfoSalary().getZli1().substring(0, 9 + 1) : fictivesalary.getInfoSalary().getZli1();
					break;
				case 60:
					char30 = fictivesalary.getInfoSalary().getZli2().length() > 9 ? fictivesalary.getInfoSalary().getZli2().substring(0, 9 + 1) : fictivesalary.getInfoSalary().getZli2();
					break;
				case 67:
					char30 = fictivesalary.getInfoSalary().getTypc().length() > 0 ? fictivesalary.getInfoSalary().getTypc().substring(0, 0 + 1) : fictivesalary.getInfoSalary().getTypc();
					break;
				case 70:
					char30 = fictivesalary.getInfoSalary().getRegi().length() > 0 ? fictivesalary.getInfoSalary().getRegi().substring(0, 0 + 1) : fictivesalary.getInfoSalary().getRegi();
					break;
				case 71:
					char30 = fictivesalary.getInfoSalary().getZres().length() > 0 ? fictivesalary.getInfoSalary().getZres().substring(0, 0 + 1) : fictivesalary.getInfoSalary().getZres();
					break;
				case 72:
					char30 = fictivesalary.getInfoSalary().getDmst().length() > 0 ? fictivesalary.getInfoSalary().getDmst().substring(0, 0 + 1) : fictivesalary.getInfoSalary().getDmst();
					break;
				case 73:
					char30 = String.valueOf(fictivesalary.getInfoSalary().getNpie());
					break;
				default:
					valuedefault = true;
					break;
				}
			}
			// mise en commentaire par yannick
			// if(valuedefault)
			// char30 = char30 + ' ';
		}
		// int i = char10.length();
		// char10 = char10 + char30.substring(0, 10-i);
		//
		if ('O' == fictivesalary.param.getGenfile())
			outputtext += "\n" + "----End return = " + char10.trim() + char30.trim();

		return char10.trim() + StringUtils.substring(char30, 0, 10 - char10.trim().length());

		// return char10.trim() + char30.trim();
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#convertToNumber(java.lang.String, com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public double convertToNumber(String char16, ClsFictifRubriqueClone rubrique)
	{
		// if('O' == fictivesalary.param.getGenfile()) outputtext += "\n" + ">>convertToNumber");
		// if('O' == fictivesalary.param.getGenfile()) outputtext += "\n" + "............char16 :" + char16);
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
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#convertToNumber37(java.lang.String, double)
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
						fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90079", fictivesalary.param.getLangue(), fictivesalary.getInfoSalary().getComp_id().getNmat()));
						fictivesalary.param.setPbWithCalulation(true);
						return 0;
					}
					return w_nombre;
				}
				else if (Character.isLetter(char16.toCharArray()[0]))
				{
					tempNumber = fictivesalary.utilNomenclatureFictif.getAmountOrRateFromNomenclature(fictivesalary.getInfoSalary().getComp_id().getCdos(), 96, char16.substring(0, 1 + 1), 1,
							fictivesalary.param.getNlot(), fictivesalary.param.getNumeroBulletin(), this.fictivesalary.param.getMonthOfPay(), ClsEnumeration.EnColumnToRead.AMOUNT);
					if (tempNumber != null)
					{
						lplaf = tempNumber.doubleValue();
						coeff = Integer.valueOf(char16.substring(2, 3 + 1));
						w_nombre = lvalnum * coeff;
					}
					else
					{
						// logger
						fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90080", fictivesalary.param.getLangue(), fictivesalary.getInfoSalary().getComp_id().getNmat()));
						fictivesalary.param.setPbWithCalulation(true);
						return 0;
					}
				}
				else
				{
					// logger
					fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90079", fictivesalary.param.getLangue(), fictivesalary.getInfoSalary().getComp_id().getNmat()));
					fictivesalary.param.setPbWithCalulation(true);
					return 0;
				}
			}
		}
		catch (Exception e)
		{
			// logger
			fictivesalary.param.setError("Exception sur convertToNumber37! "
					+ fictivesalary.param.errorMessage("ERR-90079", fictivesalary.param.getLangue(), fictivesalary.getInfoSalary().getComp_id().getNmat()));
			fictivesalary.param.setPbWithCalulation(true);
			return 0;
		}
		return w_nombre;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#getFictivesalary()
	 */
	public ClsFictifSalaryToProcess getFictivesalary()
	{
		return fictivesalary;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#getOutputtext()
	 */
	public String getOutputtext()
	{
		return outputtext;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#Lecture_Code_nationalite(com.cdi.deltarh.paie.engine.ClsFictifRubriqueClone)
	 */
	public Double Lecture_Code_nationalite(ClsFictifRubriqueClone rubrique)
	{
		double Code_nat = 0;
		tempNumber = fictivesalary.utilNomenclatureFictif.getAmountOrRateFromNomenclature(fictivesalary.getInfoSalary().getComp_id().getCdos(), 4, fictivesalary.getInfoSalary().getNato(), 1,
				fictivesalary.param.getNlot(), fictivesalary.param.getNumeroBulletin(), this.fictivesalary.param.getMonthOfPay(), ClsEnumeration.EnColumnToRead.AMOUNT);
		if (tempNumber == null)
		{
			// logger
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90086", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(),
					rubrique.getRubrique().getComp_id().getCrub(), fictivesalary.getInfoSalary().getComp_id().getNmat(), rubrique.getRubrique().getTabl(), fictivesalary.getInfoSalary().getNato()));
			return new Double(0);
		}

		Code_nat = tempNumber.doubleValue();

		if (Code_nat > 3 || Code_nat < 0)
		{
			// logger
			fictivesalary.param.setError(fictivesalary.param.errorMessage("ERR-90087", fictivesalary.param.getLangue(), rubrique.getRubrique().getAlgo(),
					rubrique.getRubrique().getComp_id().getCrub(), fictivesalary.getInfoSalary().getComp_id().getNmat(), rubrique.getRubrique().getTabl(), fictivesalary.getInfoSalary().getNato()));
			return new Double(0);
		}
		return Code_nat;
	}

	/**
	 * =>rec_nbjcg Lecture du nombre de jours de conge du mois
	 * 
	 * @return
	 */
	private int rechercheNombreDeJourCongeDuMois()
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
		if (fictivesalary._isExpatrie())
		{
			tempNumber = fictivesalary.utilNomenclatureFictif.getAmountOrRateFromNomenclature(fictivesalary.getInfoSalary().getComp_id().getCdos(), 99, "NBJC-DEF", 2, fictivesalary.param.getNlot(),
					fictivesalary.param.getNumeroBulletin(), this.fictivesalary.param.getMonthOfPay(), ClsEnumeration.EnColumnToRead.RATES);
			nbreJourCongeEx = tempNumber == null ? 0 : tempNumber.intValue();
			if (nbreJourCongeEx == 0)
				nbreJourCongeEx = 5;
			//
			tempNumber = fictivesalary.utilNomenclatureFictif.getAmountOrRateFromNomenclature(fictivesalary.getInfoSalary().getComp_id().getCdos(), 35, fictivesalary.getInfoSalary().getCat(), 5,
					fictivesalary.param.getNlot(), fictivesalary.param.getNumeroBulletin(), this.fictivesalary.param.getMonthOfPay(), ClsEnumeration.EnColumnToRead.RATES);
			nbreJourConge = tempNumber == null ? 0 : tempNumber.intValue();
			if (nbreJourConge == 0)
				nbreJourConge = nbreJourCongeEx;
		}
		else
		{
			tempNumber = fictivesalary.utilNomenclatureFictif.getAmountOrRateFromNomenclature(fictivesalary.getInfoSalary().getComp_id().getCdos(), 99, "NBJC-DEF", 1, fictivesalary.param.getNlot(),
					fictivesalary.param.getNumeroBulletin(), this.fictivesalary.param.getMonthOfPay(), ClsEnumeration.EnColumnToRead.RATES);
			nbreJourCongelx = tempNumber == null ? 0 : tempNumber.intValue();
			if (nbreJourCongelx == 0)
				nbreJourCongelx = 2;
			//
			tempNumber = fictivesalary.utilNomenclatureFictif.getAmountOrRateFromNomenclature(fictivesalary.getInfoSalary().getComp_id().getCdos(), 35, fictivesalary.getInfoSalary().getCat(), 3,
					fictivesalary.param.getNlot(), fictivesalary.param.getNumeroBulletin(), this.fictivesalary.param.getMonthOfPay(), ClsEnumeration.EnColumnToRead.RATES);
			nbreJourConge = tempNumber == null ? 0 : tempNumber.intValue();
			if (nbreJourConge == 0)
				nbreJourConge = nbreJourCongelx;
		}
		//
		return nbreJourConge;
	}

	/**
	 * @param rubrique
	 * @return
	 */
	private int rechercheNombreDeJourTravailDuMois(ClsFictifRubriqueClone rubrique)
	{
		Double n = new Double(0);
		if ("B".equals(fictivesalary.param.getJourTravaillePlage()))
			n = rubrique.getBase();
		else if ("T".equals(fictivesalary.param.getJourTravaillePlage()))
			n = rubrique.getRates();
		else if ("M".equals(fictivesalary.param.getJourTravaillePlage()))
			n = rubrique.getAmount();
		return n.intValue();
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#setFictivesalary(com.cdi.deltarh.paie.engine.ClsFictifSalaryToProcess)
	 */
	public void setFictivesalary(ClsFictifSalaryToProcess fictivesalary)
	{
		this.fictivesalary = fictivesalary;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IFictifAlgorithm#setOutputtext(java.lang.String)
	 */
	public void setOutputtext(String outputtext)
	{
		this.outputtext = outputtext;
	}

	// private void listdesrubriques(List rub){
	// for (Object object : rub) {
	// if('O' == fictivesalary.param.getGenfile()) outputtext += "\n" + object);
	// }
	// }
	
//	---------------------------------------------------------------------------------
//	-- Droits au conges fichier calcul
//	---------------------------------------------------------------------------------
	public double cal_con() 
	{
		String cdos = fictivesalary.getInfoSalary().getComp_id().getCdos();
		String nmat = fictivesalary.getInfoSalary().getComp_id().getNmat();
		String rubq = fictivesalary.param.getBaseCongeRubrique();
		String aamm = new ClsDate(fictivesalary.param.myMoisPaieCourant.addMonth(-1)).getDateS("yyyyMM");
		HibernateConnexionService service = fictivesalary.getService();
		double l_mon   = 0;

	   String query=" SELECT SUM(coalesce(mont,0)) FROM Rhtcalcul  where identreprise = '"+cdos+"'";
	   query+=" AND aamm = '"+aamm+"' AND nmat = '"+nmat+"' AND rubq = '"+rubq+"'";
	   List lst = service.find(query);
	   if(!lst.isEmpty() && lst.get(0)!= null)
		   l_mon = new BigDecimal(lst.get(0).toString()).doubleValue();

	 return l_mon;

	}
	
//	---------------------------------------------------------------------------------
//	-- Droits au conges fichier fictif
//	---------------------------------------------------------------------------------
	public double fic_con()
	{

		String cdos = fictivesalary.getInfoSalary().getComp_id().getCdos();
		String nmat = fictivesalary.getInfoSalary().getComp_id().getNmat();
		String rubq = fictivesalary.param.getBaseCongeRubrique();
		String aamm = fictivesalary.param.getMonthOfPay();
		HibernateConnexionService service = fictivesalary.getService();
		double l_mon   = 0;

	   String query=" SELECT SUM(coalesce(mont,0)) FROM Rhtfic  where identreprise = '"+cdos+"'";
	   query+=" AND aamm < '"+aamm+"' and aamm not like '%99' AND nmat = '"+nmat+"' AND rubq = '"+rubq+"'";
	   List lst = service.find(query);
	   if(!lst.isEmpty() && lst.get(0)!= null)
		   l_mon = new BigDecimal(lst.get(0).toString()).doubleValue();
	   if(!lst.isEmpty() && lst.get(0)!= null)
		   l_mon = new BigDecimal(lst.get(0).toString()).doubleValue();

	   return l_mon;
	 }

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.engine.IAlgorithm#algo904(com.cdi.deltarh.paie.engine.ClsRubriqueClone)
	 */
	public boolean algo904(ClsFictifRubriqueClone rubrique)
	{
//		System.out.println("CALCUL CONGE FICT------------"+this.fictivesalary.param.getMonthOfPay());
		String cdos = fictivesalary.getInfoSalary().getComp_id().getCdos();
		// String crub = rubrique.getRubrique().getComp_id().getCrub();
		Double montant = new Double(0);
		// double taux = 0;
		double base = 0;
		double totalNbreJourSuppl = 0;
		int diviseurNbreJourSuppl = 0;
		//
		
		double tau = 0;
		String query=null;
		ParamData typeBGFI = null;//(ParamData) fictivesalary.param.service.get(ParamData.class, new ParamDataPK(cdos, 99, "PAYSBGFI", 2));
		if(typeBGFI !=null && StringUtils.isNotEmpty(typeBGFI.getVall()) && "CONGO".equalsIgnoreCase(typeBGFI.getVall())){
			query = "select cast(sum(nvl(nbjc,0)) as int) nb from ElementVariableConge where identreprise = '" + this.fictivesalary.getInfoSalary().getComp_id().getCdos() + "' and year(ddeb)||lpad(month(ddeb),2,'0') = '" + this.fictivesalary.param.getMonthOfPay() + "' ";
		    query = query + " and nmat = '" + this.fictivesalary.getInfoSalary().getComp_id().getNmat() + "' and nbul = " + this.fictivesalary.param.getNumeroBulletin() + " AND motf = '01' ";
		} else {
			query="select cast(sum(nvl(nbja,0)) as int) nb from ElementVariableConge where identreprise = '"+fictivesalary.getInfoSalary().getComp_id().getCdos()+"' and to_char(ddeb, 'yyyyMM') = '"+this.fictivesalary.param.getMonthOfPay()+"' ";
			query+=" and nmat = '"+fictivesalary.getInfoSalary().getComp_id().getNmat()+"' and nbul = "+fictivesalary.param.getNumeroBulletin()+" AND motf in (select a.cacc from ParamData a, ParamData b "+
																 "where b.cacc = a.cacc   and b.ctab = b.ctab  and b.cdos = '"+fictivesalary.getInfoSalary().getComp_id().getCdos()+"'  and a.cdos = '"+fictivesalary.getInfoSalary().getComp_id().getCdos()+"' "+
																 "and a.ctab = 22 and b.ctab = 22 and a.nume = 1  and b.nume = 3 and a.valm = 1 and b.valm = 0 ) ";
			if (StringUtils.equals(TypeBDUtil.typeBD, TypeBDUtil.IN))
			{
				query="select cast(sum(nvl(nbja,0)) as int) nb from ElementVariableConge where identreprise = '"+fictivesalary.getInfoSalary().getComp_id().getCdos()+"' and to_char1(ddeb,'YYYYMM') = '"+this.fictivesalary.param.getMoisPaieCourant()+"' ";
				query+=" and nmat = '"+fictivesalary.getInfoSalary().getComp_id().getNmat()+"' and nbul = "+fictivesalary.param.getNumeroBulletin()+" AND motf in (select a.cacc from ParamData a, ParamData b "+
																	 "where b.cacc = a.cacc   and b.ctab = b.ctab  and b.cdos = '"+fictivesalary.getInfoSalary().getComp_id().getCdos()+"'  and a.cdos = '"+fictivesalary.getInfoSalary().getComp_id().getCdos()+"' "+
																	 "and a.ctab = 22 and b.ctab = 22 and a.nume = 1  and b.nume = 3 and a.valm = 1 and b.valm = 0 ) ";
			}
			if (StringUtils.equals(TypeBDUtil.typeBD, TypeBDUtil.MS))
			{
				query="select cast(sum(nvl(nbja,0)) as int) nb from ElementVariableConge where identreprise = '"+fictivesalary.getInfoSalary().getComp_id().getCdos()+"' and dbo.formaterDateEnChaine(ddeb,'yyyyMM') = '"+this.fictivesalary.param.getMoisPaieCourant()+"' ";
				query+=" and nmat = '"+fictivesalary.getInfoSalary().getComp_id().getNmat()+"' and nbul = "+fictivesalary.param.getNumeroBulletin()+" AND motf in (select a.cacc from ParamData a, ParamData b "+
																	 "where b.cacc = a.cacc   and b.ctab = b.ctab  and b.cdos = '"+fictivesalary.getInfoSalary().getComp_id().getCdos()+"'  and a.cdos = '"+fictivesalary.getInfoSalary().getComp_id().getCdos()+"' "+
																	 "and a.ctab = 22 and b.ctab = 22 and a.nume = 1  and b.nume = 3 and a.valm = 1 and b.valm = 0 ) ";
			}
			if (StringUtils.equals(TypeBDUtil.typeBD, TypeBDUtil.MY))
			{
				query="select cast(sum(nvl(nbja,0)) as int) nb from ElementVariableConge where identreprise = '"+fictivesalary.getInfoSalary().getComp_id().getCdos()+"' and date_format(ddeb,'%Y%m') = '"+this.fictivesalary.param.getMoisPaieCourant()+"' ";
				query+=" and nmat = '"+fictivesalary.getInfoSalary().getComp_id().getNmat()+"' and nbul = "+fictivesalary.param.getNumeroBulletin()+" AND motf in (select a.cacc from ParamData a, ParamData b "+
																	 "where b.cacc = a.cacc   and b.ctab = b.ctab  and b.cdos = '"+fictivesalary.getInfoSalary().getComp_id().getCdos()+"'  and a.cdos = '"+fictivesalary.getInfoSalary().getComp_id().getCdos()+"' "+
																	 "and a.ctab = 22 and b.ctab = 22 and a.nume = 1  and b.nume = 3 and a.valm = 1 and b.valm = 0 ) ";
			}
		}
//		System.out.println("REQUETE PAIE: "+query);
		Session session = fictivesalary.param.service.getSession();
		try {
			Query q = session.createSQLQuery(query).addScalar("nb");
			List lst = q.list();
			
			if(! lst.isEmpty() && lst.get(0) != null)
				tau = ClsObjectUtil.getDoubleFromObject(lst.get(0));
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			fictivesalary.param.service.closeConnexion(session);
		}
		
		fictivesalary.getValeurRubriquePartage().setRates(tau);
		base = fictivesalary.getValeurRubriquePartage().getBase();
		if(typeBGFI !=null && StringUtils.isNotEmpty(typeBGFI.getVall()) && "CONGO".equalsIgnoreCase(typeBGFI.getVall()))
			base = base*tau;
		else base = base*tau/30;
		fictivesalary.getValeurRubriquePartage().setAmount(base);
		
		return true;
	}
}
