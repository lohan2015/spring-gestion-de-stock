package com.kinart.paie.business.services.calcul;

import java.io.File;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.kinart.paie.business.model.*;
import com.kinart.paie.business.services.CalculPaieService;
import com.kinart.paie.business.services.CongeFictifService;
import com.kinart.paie.business.services.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.BeanUtils;

/**
 * Cette classe permet d'encapsuler toutes les données propres é un agent qui participent au calcul de son salaire. Elle contient une méthode qui permet de
 * calculer toutes les rubriques. Elle a été conéue pour que ses intances soient tangibles les unes des autres. Ainsi, son espace de nommage est complétement
 * isolée.
 * 
 * @author e.etoundi
 * 
 */
public class ClsFictifSalaryToProcess
{

	Session session = null;

	// Le numero de la ligne dans CalculPaie
	protected int derniereLigne = 0;

	// le champ contenant toutes les informations du salarié
	protected ClsInfoSalaryClone infoSalary = null;

	protected GeneriqueConnexionService service = null;

	protected ClsDate myMonthOfPay = null;

	protected Number tempNumber = null;

	public File outputFile = null;

	protected Map<String, List<Object>> listLignePretMap = null;

	protected Map<String, List<Object>> listNumeroPretMap = null;

	protected Map<String, List<Object[]>> listOfEltvar = null;

	protected Map<String, List<Object>> listOfEltfix = null;

	protected Map<String, Object[]> cumul1 = null;

	protected Map<String, Object[]> cumul2 = null;

	protected Map<String, Object[]> cumul3 = null;

	protected Map<String, Object[]> cumul4 = null;

	protected int anciennete = 0;

	protected boolean expatrie = false;

	protected boolean table91LabelNotEmpty = false;
	
	public List<ElementVariableConge> listeConges = null;

	/**
	 * c'est le numéro de bulletion renvoyé lors de la destruction des info salarié dans le fichier salariés
	 */
	protected int numeroBulletinVerified = 0;

	// la nature de l'entreprise
	protected ClsEnumeration.EnEnterprise entreprise = ClsEnumeration.EnEnterprise.UNKNOWN;

	protected int ageOfAgent = 0;

	// POUR LE CALCUL DES PERIODES
	protected ClsFictifPeriodUtil oFictifPeriod = null;

	// PARAMETRES CUMUL
	protected ClsParametreCumul paramCumul = null;

	// VALEURS RUBRIQUES PARTAGEES
	protected ClsValeurRubriquePartage valeurRubriquePartage = null;

	// Embauche
	protected boolean embauche = false;

	// Map de => concPro
	protected Map<String, String> mapOfConcPro = null;

	// salaire é écarter du net
	protected double salaireAecarterDuNet = 0;

	// salaire brut
	protected double salaireBrut = 0;

	// salaire net
	protected double salaireNet = 0;

	// salaire net é calculer
	protected double salaireNetATrouver = 0;

	// contient les paramétres du dernier EV
	protected ClsParamElementVariable evparam = null;

	// Cpt_valajus
	protected int compteurValeurAjus = 0;

	// Max_Nb_valajus
	protected int maxNbreValeurAjus = 22;

	// nombre d'itération => nbiter
	protected int nbiter = 0;

	boolean sensPositif = true;

	// continuer le traitement du salaire
	protected boolean continuer = false;

	// salaire é l'itération paire
	double salaireIterPaire = 0;

	// salaire é l'itération impaire
	double salaireIterImpaire = 0;

	//
	protected double nbreJourPourEnfant = 0;

	public String outputtext = "";

	private double tot_cgs = 0;

	protected ClsFictifNomenclatureUtil utilNomenclatureFictif = null;

	protected ClsFictifParameterOfPay param = null;

	protected ClsFictifSalaryWorkTime workTimeFictif = null;

	protected List<ClsFictifRubriqueClone> ListOfRubriqueOfMotifFinContratFictif = null;// --

	double total_nap = 0;

	boolean l_traitmois;

	boolean dern_mois_de_conge;

	// boolean Continuer_Calcul_Net;//param.calcul
	private CongeFictifService congeFictifService;
	private CalculPaieService calculPaieService;

	public ClsFictifSalaryToProcess()
	{

	}

	/**
	 * 
	 * @param parameter
	 * @param infoSalary
	 */
	public ClsFictifSalaryToProcess(ClsFictifParameterOfPay parameter, ClsInfoSalaryClone infoSalary, ClsFictifSalaryWorkTime workTimeFictif, CalculPaieService calculPaieService, CongeFictifService congeFictifService)
	{
		this.infoSalary = infoSalary;
		this.param = parameter;
		this.utilNomenclatureFictif = this.param.getUtilNomenclatureFictif(); // new ClsFictifNomenclatureUtil(this.param);
		this.congeFictifService = congeFictifService;
		this.calculPaieService = calculPaieService;
		// this.param.setUtilNomenclatureFictif(utilNomenclatureFictif);
		this.oFictifPeriod = new ClsFictifPeriodUtil(0, 0);
		this.workTimeFictif = workTimeFictif;
		this.param.setUseRetroactif(false);
	}

	public void traiterSalaireFictif()
	{
		boolean continuer = true;

		this.deleteFictifFictif(false, false, false);

		this.total_nap = 0;

		if ("O".equals(this.infoSalary.getCals()))
		{
			boolean finArretPaie = false;
			String queryArretPaie = "from SuspensionPaie" + " where identreprise = '" + param.getDossier() + "'" + " and nmat = '" + this.infoSalary.getComp_id().getNmat() + "'"
					+ " order by identreprise, nmat, ddar";

			List listOfArriere = this.getService().find(queryArretPaie);
			Date ddar = null;
			Date dfar = null;
			for (Object obj1 : listOfArriere)
			{
				ddar = ((SuspensionPaie) obj1).getDdar();
				dfar = ((SuspensionPaie) obj1).getDfar();

				if (ddar.compareTo(param.getFirstDayOfMonth()) <= 0 && dfar.compareTo(param.getLastDayOfMonth()) >= 0)
				{
					finArretPaie = true;
					break;
				}
			}
			if (finArretPaie)
			{
				continuer = false;
				param.error = this.getInfoSalary().getComp_id().getNmat() + " " + param.errorMessage("INF-10345", param.getLangue());
				param.insererLogMessage(param.error);
			}
			else
			{
				this.setEmbauche(false);
				if (this.infoSalary.getDtes() != null && param.getFirstDayOfMonth().compareTo(this.infoSalary.getDtes()) <= 0 && param.getLastDayOfMonth().compareTo(this.infoSalary.getDtes()) >= 0
						&& param.getNumeroBulletin() == 9)
				{
					this.setEmbauche(true);
				}

				param.setStc(false);
				if ("MU".equals(this.infoSalary.getMrrx()) || "RA".equals(this.infoSalary.getMrrx()))
				{
					if (this.infoSalary.getDmrr() != null)
					{
						if (param.getFirstDayOfMonth().compareTo(this.infoSalary.getDmrr()) <= 0 && param.getLastDayOfMonth().compareTo(this.infoSalary.getDmrr()) >= 0 && param.getNumeroBulletin() == 9)
						{
							param.setStc(true);
						}
						else
						{
							if (param.getFirstDayOfMonth().compareTo(this.infoSalary.getDmrr()) > 0)
								continuer = false;
						}
					}
				}
			}// finarretpaie

			if (continuer)
			{
				this.traitementSalaireFictif(param.getAppDateFormat());

				if (!param.isPbWithCalulation())
				{
					this.deletePseudoEVFictif();
				}
				else
				{
					param.setPbWithCalulation(false);
					param.insererLogMessage(param.getError());
				}
			}

		}// calc = 0
		
		char cas = ' ';
		if(StringUtils.isNotBlank(param.fictiveCalculusType))
			cas = param.fictiveCalculusType.charAt(0);
		this.genererBulletinFictif(cas);

	}
	
	public List<CongeFictif> populateFictiveList(int size)
	{
		List<CongeFictif> liste = new ArrayList<CongeFictif>();
		for(int i=0; i <size; i++)
			liste.add(new CongeFictif());
		return liste;
	}
	
	private void printListe(List<CongeFictif> liste)
	{
		int i = 0;
		for (CongeFictif CongeFictif : liste)
		{
			ParameterUtil.println(i +" - "+CongeFictif.toString());
			i++;
			
		}
	}
	
//	-------------------------------------------------------------------------------
//	--  Gen_Cal :       SOUS - PROGRAMMES
//	-------------------------------------------------------------------------------
//	--            Cette procedure permet de generer la fusion des bulletins
//	--            calcules dans pafic pour chaque mois de conges.
//	--            Le resultat de cette fusion est charge dans pafic ou dans
//	--            pacalc selon la methode de fictif utilisee.
//	--            Le type de methode utilise est stocke dans le paramaetre
//	--            'FICTIF' de la table 99 en libelle 2 :
//	--                     A : le resultat est genere dans pafic et correspond
//	--                         au bulletin fictif que l'on editait auparavant
//	--                         en faisant la somme des bulletins de controles.
//	--                     B : le resultat est genere dans pacal et correspond
//	--                         au bulletin qui sera centralise lors de la mise a
//	--                         jour.
//	-------------------------------------------------------------------------------
//	-- r_cdos : Code du dossier traite
//	-- r_cas  : Cas du fictif (A/B)
//	-- r_nmat : Numero du matricule traite
//	-- r_pmcf : Premier mois du conges fictif. Mois d'EV ou sera generee l'avance sur
//	--                                         conges (r_cas='A')
//	--                                         Mois ou sera genere le bulletin total
//	--                                         (r_cas='B')
//	-------------------------------------------------------------------------------
	private void genererBulletinFictif(char cas)
	{
		Session session = service.getSession();
		Transaction tx = null;
		
		try
		{
			tx = session.beginTransaction();
			
			genererBulletinFictif(session,cas);
			
			tx.commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			service.closeSession(session);
		}

	}
	private void genererBulletinFictif(Session session, char cas)
	{
		String cdos = this.param.dossier;
		String nmat = this.infoSalary.getComp_id().getNmat();
		String pmcf = this.infoSalary.getPmcf();
		//pa_fictif.gen_cal
//		PROCEDURE Gen_Cal(r_cdos IN VARCHAR2,  r_cas  IN VARCHAR2,
//                r_nmat IN VARCHAR2,  r_pmcf IN VARCHAR2) IS
//
// i           NUMBER(5);
		Integer i;
// tot_lig     NUMBER(5);
		Integer tot_lig;
// cpt_lig     NUMBER(5);
		Integer cpt_lig;
// cpt_res     NUMBER(5);
		Integer cpt_res;
//
// w_crub      pacalc.rubq%TYPE;
		String crub;
// w_argu      pacalc.argu%TYPE;
		String argu;
// w_nprt      pacalc.nprt%TYPE;
		String nprt;
//
// w_mont      pacalc.mont%TYPE;
		double mont = 0;;
//
// w_cbulf     parubq.cbulf%TYPE;
		String cbulf = StringUtils.EMPTY;
// w_fbas      parubq.fbas%TYPE;
		String fbas = StringUtils.EMPTY;
// w_tbas      parubq.tbas%TYPE;
		String tbas = StringUtils.EMPTY;
//
// w_exist     VARCHAR2(1);
		String exist;
// w_nb        NUMBER(5);
		Integer nb = 0;
//
// Exclure_mois_1 	PAFNOM.VALM%TYPE;
		//double exclure_mois_1 = this.param.exclure_mois_1;
//
//BEGIN
// -- MM 15/04/2004 Ajout param exclure de l'avance le 1er mois de fictif.
// Exclure_mois_1 := NVL(PA_CALCUL.rech_txmt(99,'FICTIF','M',3),0);
// -- Fin modif MM.

		String query = null;
		
		CalculPaie calcul = null;
		CongeFictif fictif = null;

//
// -- Fictif B, on supprime le bulletin normal existant
// IF r_cas = 'B' THEN
		if(cas == 'B')
		{
//    DELETE FROM pacalc
//     WHERE cdos = r_cdos
//       AND nmat = r_nmat
//       AND aamm = r_pmcf;
			query="Delete From CalculPaie where identreprise = '"+cdos+"' and nmat = '"+nmat+"' and aamm = '"+pmcf+"'";
			session.createSQLQuery(query).executeUpdate();
		}
// ELSE
		else
		{
// -- Fictif A, on supprime le bulletin fictif existant
//    DELETE FROM pafic
//     WHERE cdos = r_cdos
//       AND nmat = r_nmat
//       AND aamm = SUBSTR(r_pmcf,1,4)||'99';
			query="Delete From CongeFictif where identreprise ='"+cdos+"' and nmat = '"+nmat+"' and aamm = '"+ StringUtil.oraSubstring(pmcf, 1,4) + "99"+"'";
			session.createSQLQuery(query).executeUpdate();
// END IF;
		}
//
// i := 0;
		i = 0;
		List<CongeFictif> lignes = service.find("From CongeFictif where identreprise ='"+cdos+"' and nmat = '"+nmat+"' order by cdos, nmat, rubq, nprt, argu, aamm, nbul, nlig");
		List<CongeFictif> tabfic = this.populateFictiveList(lignes.size() + 1);
		List<CongeFictif> tabres = this.populateFictiveList(lignes.size() + 1);
		List<CongeFictif> tabficclone = this.populateFictiveList(lignes.size() + 1);
// FOR fic_curs IN (SELECT * FROM pafic
//                   WHERE cdos = r_cdos
//                     AND nmat = r_nmat
//                   ORDER BY cdos,nmat,rubq,nprt,argu,aamm,nbul,nlig) LOOP
		
		CongeFictif clone = null;
		for (CongeFictif fic : lignes)
		{
//    i := i + 1;
			i++;
//    IF tab_fic(i).nprt IS NULL
//    THEN
//       tab_fic(i).nprt := 0;
//    END IF;
			if(StringUtils.isBlank(fic.getNprt()))
				fic.setNprt("0");
//    IF tab_fic(i).argu IS NULL
//    THEN
//       tab_fic(i).argu := ' ';
//    END IF;
			if(StringUtils.isBlank(fic.getArgu()))
				fic.setArgu(" ");
			//
			clone = new CongeFictif();
			BeanUtils.copyProperties(fic, clone);
//    tab_fic(i) := fic_curs;
			tabfic.set(i, fic);
			tabficclone.set(i, clone);
// END LOOP;
		}
// tot_lig := i;
		tot_lig = i;
//
// cpt_res := 0;
		cpt_res = 0;
// cpt_lig := 1;
		cpt_lig = 1;
		
		ClsParubqClone rub = null;
// WHILE cpt_lig <= tot_lig LOOP
		while(cpt_lig <= tot_lig)
		{
//    w_crub := tab_fic(cpt_lig).rubq;
			crub = tabfic.get(cpt_lig).getRubq();
			ParameterUtil.println("--->Traitement de la rubrique "+crub);
//    w_argu := tab_fic(cpt_lig).argu;
			argu = tabfic.get(cpt_lig).getArgu();
//    w_nprt := tab_fic(cpt_lig).nprt;
			nprt = tabfic.get(cpt_lig).getNprt();
//    BEGIN
			rub = this.findRubriqueCloneFictif(crub).getRubrique();
//       SELECT cbulf, fbas, tbas
//         INTO w_cbulf, w_fbas, w_tbas
//         FROM parubq
//        WHERE cdos = r_cdos
//          AND crub = tab_fic(cpt_lig).rubq;
			if(rub != null)
			{
				cbulf = rub.getCbulf();
				fbas = rub.getFbas();
				tbas = rub.getTbas();
			}
			else
				cbulf = "T";
			ParameterUtil.println("Cbulf = "+cbulf);
			ParameterUtil.println("Fbas = "+fbas);
			ParameterUtil.println("Tbas = "+tbas);
//    EXCEPTION
//       WHEN NO_DATA_FOUND THEN
//          w_cbulf := 'T';
//    END;
		
//    IF w_cbulf = 'T' THEN
			if(StringUtils.equals("T", cbulf))
			{
//       -- Prendre toutes les lignes
//       WHILE cpt_lig <= tot_lig AND tab_fic(cpt_lig).rubq = w_crub LOOP
//          cpt_res := cpt_res + 1;
//          tab_res(cpt_res) := tab_fic(cpt_lig);
//          cpt_lig := cpt_lig + 1;
//       END LOOP;
				while((cpt_lig <= tot_lig) && StringUtils.equals(crub, tabfic.get(cpt_lig).getRubq()))
				{
					cpt_res ++;
					tabres.set(cpt_res, tabficclone.get(cpt_lig));
					cpt_lig ++;
				}
			}
			else if(StringUtils.equals("P", cbulf))
			{
//    ELSIF w_cbulf = 'P' THEN
//       -- Ne prendre que la 1ere ligne
//       cpt_res := cpt_res + 1;
				cpt_res ++;
//       tab_res(cpt_res) := tab_fic(cpt_lig);
				tabres.set(cpt_res, tabficclone.get(cpt_lig));
//       WHILE cpt_lig <= tot_lig AND tab_fic(cpt_lig).rubq = w_crub
//                                AND tab_fic(cpt_lig).nprt = w_nprt LOOP
//          cpt_lig := cpt_lig + 1;
//       END LOOP;
				while((cpt_lig <= tot_lig) && StringUtils.equals(crub, tabfic.get(cpt_lig).getRubq()) && StringUtils.equals(nprt, tabfic.get(cpt_lig).getNprt()))
					cpt_lig++;
			}
			else if(StringUtils.equals("D", cbulf))
			{
//    ELSIF w_cbulf = 'D' THEN
//       i := 0;
				i = 0;
//       cpt_res := cpt_res + 1;
				cpt_res ++;
//       tab_res(cpt_res) := tab_fic(cpt_lig);
				tabres.set(cpt_res, tabficclone.get(cpt_lig));
//
//       tab_res(cpt_res).basc := 0;
				tabres.get(cpt_res).setBasc(new BigDecimal(0));
//       tab_res(cpt_res).basp := 0;
				tabres.get(cpt_res).setBasp(new BigDecimal(0));
//       tab_res(cpt_res).taux := 0;
				tabres.get(cpt_res).setTaux(new BigDecimal(0));
//       tab_res(cpt_res).mont := 0;
				tabres.get(cpt_res).setMont(new BigDecimal(0));
				
//
//       WHILE cpt_lig <= tot_lig AND tab_fic(cpt_lig).rubq = w_crub
//                                AND tab_fic(cpt_lig).nprt = w_nprt
//                                AND tab_fic(cpt_lig).argu = w_argu LOOP
			 
				ParameterUtil.println("Rub 1 = "+crub);
				ParameterUtil.println("Rub 2 = "+tabfic.get(cpt_lig).getRubq());
				
				ParameterUtil.println("Argu 1 = "+argu);
				ParameterUtil.println("Argu 2 = "+tabfic.get(cpt_lig).getArgu());
				
				ParameterUtil.println("Nprt 1 = "+nprt);
				ParameterUtil.println("Nprt 2 = "+tabfic.get(cpt_lig).getNprt());
				while((cpt_lig <= tot_lig) && StringUtils.equals(crub, tabfic.get(cpt_lig).getRubq()) && StringUtils.equals(nprt, tabfic.get(cpt_lig).getNprt()) && StringUtils.equals(argu, tabfic.get(cpt_lig).getArgu()))
				{
//          i := i + 1;
					i++;
//          IF w_fbas = 'S' OR (w_fbas = 'P' AND i = 1) THEN
//             tab_res(cpt_res).basc := tab_res(cpt_res).basc + tab_fic(cpt_lig).basc;
//             tab_res(cpt_res).basp := tab_res(cpt_res).basp + tab_fic(cpt_lig).basp;
//          END IF;
					if(StringUtils.equals(fbas, "S") || (StringUtils.equals(fbas, "P") && i == 1))
					{
						tabres.get(cpt_res).setBasc(tabres.get(cpt_res).getBasc().add(tabfic.get(cpt_lig).getBasc()));
						tabres.get(cpt_res).setBasp(tabres.get(cpt_res).getBasp().add(tabfic.get(cpt_lig).getBasp()));
						ParameterUtil.println("Base C é ajouter = "+tabfic.get(cpt_lig).getBasc()+" é la base "+tabres.get(cpt_res).getBasc());
						ParameterUtil.println("Base P é ajouter = "+tabfic.get(cpt_lig).getBasp()+" é la base "+tabres.get(cpt_res).getBasp());
					}
//          IF w_tbas = 'S' OR (w_tbas = 'P' AND i = 1) THEN
//             tab_res(cpt_res).taux := tab_res(cpt_res).taux + tab_fic(cpt_lig).taux;
//          END IF;
					if(StringUtils.equals(tbas, "S") || (StringUtils.equals(tbas, "P") && i == 1))
					{
						tabres.get(cpt_res).setTaux(tabres.get(cpt_res).getTaux().add(tabfic.get(cpt_lig).getTaux()));
						ParameterUtil.println("Taux é ajouter = "+tabfic.get(cpt_lig).getBasc()+" au taux "+tabres.get(cpt_res).getTaux());
					}
//          tab_res(cpt_res).mont := tab_res(cpt_res).mont + tab_fic(cpt_lig).mont;
					tabres.get(cpt_res).setMont(tabres.get(cpt_res).getMont().add(tabfic.get(cpt_lig).getMont()));
					ParameterUtil.println("Montant é ajouter = "+tabfic.get(cpt_lig).getMont()+" au montant "+tabres.get(cpt_res).getMont());
//          cpt_lig := cpt_lig + 1;
					cpt_lig++;
//       END LOOP;
				}
			}
			else
			{
//    ELSE
//       cpt_lig := cpt_lig + 1;
				cpt_lig++;
//    END IF;
			}
// END LOOP;
		}
//
		
		
		List l = null;
// IF r_cas = 'B' THEN
		if(cas == 'B')
		{
//    --  Fictif B : On genere le bulletin global dans pacalc
//    FOR i IN 1..cpt_res LOOP
//       INSERT INTO pacalc
//       VALUES (tab_res(i).cdos,
//               tab_res(i).nmat,
//               r_pmcf,
//               tab_res(i).nbul,
//               i,
//               tab_res(i).rubq,
//               tab_res(i).basc,
//               tab_res(i).basp,
//               tab_res(i).taux,
//               tab_res(i).mont,
//               tab_res(i).nprt,
//               tab_res(i).ruba,
//               tab_res(i).argu,
//               tab_res(i).clas,
//               tab_res(i).trtb
//              );
//    END LOOP;
			for(int ii=1; ii<=cpt_res; ii++)
			{
				calcul = new CalculPaie();
				calcul.setIdEntreprise(Integer.valueOf(cdos));
				calcul.setNmat(nmat);
				calcul.setAamm(pmcf);
				calcul.setNbul(tabres.get(ii).getNbul());
				calcul.setNlig(ii);
				calcul.setRubq(tabres.get(ii).getRubq());
				calcul.setBasc(tabres.get(ii).getBasc());
				calcul.setBasp(tabres.get(ii).getBasp());
				calcul.setTaux(tabres.get(ii).getTaux());
				calcul.setMont(tabres.get(ii).getMont());
				calcul.setNprt(tabres.get(ii).getNprt());
				calcul.setRuba(tabres.get(ii).getRuba());
				calcul.setArgu(tabres.get(ii).getArgu());
				calcul.setClas(tabres.get(ii).getClas());
				calcul.setTrtb(tabres.get(ii).getTrtb());
				
				session.save(calcul);
			}
		}
// ELSE
		else
		{
//
//	-- MM 15/04/2004 Modif exclusion 1er mois fictif de l'avance
//	IF PA_PAIE.NouZ(Exclure_mois_1) THEN
			if(this.param.exclure_mois_1 == 0)
			{
//       -- Generation de l'avance conges (Fictif A)
//       SELECT SUM(mont) INTO w_mont FROM pafic
//        WHERE cdos = r_cdos
//          AND nmat = r_nmat
//          AND rubq = PA_CALFIC.rub_nap;
				l = service.find("Select sum(mont) from CongeFictif where identreprise ='"+cdos+"' and nmat = '"+nmat+"' and rubq = '"+param.getNapRubrique()+"'");
				if(! l.isEmpty() && l.get(0) != null)
					mont = Double.valueOf(l.get(0).toString()).doubleValue();
			}
//	ELSE
			else
			{
//       -- Generation de l'avance conges (Fictif A)
//       SELECT SUM(mont) INTO w_mont FROM pafic
//        WHERE cdos = r_cdos
//          AND nmat = r_nmat
//		AND aamm != r_pmcf
//          AND rubq = PA_CALFIC.rub_nap;
				l = service.find("Select sum(mont) from CongeFictif where identreprise ='"+cdos+"' and nmat = '"+nmat+"' and aamm != '"+pmcf+"' and rubq = '"+param.getNapRubrique()+"'");
				if(! l.isEmpty() && l.get(0) != null)
					mont = Double.valueOf(l.get(0).toString()).doubleValue();
			}	
//	END IF;
		}
		elementsEVAvancesConges = new Object[4];
		elementsEVAvancesConges[0] = new BigDecimal(mont);
		elementsEVAvancesConges[1] = "AUTO";
		elementsEVAvancesConges[2] = "";
		elementsEVAvancesConges[3] = "";
//
//    -- Test si l'avance conges existe deja
//    SELECT count(*) INTO w_nb FROM paevar
//     WHERE cdos = r_cdos
//       AND nmat = r_nmat
//       AND aamm = r_pmcf
//       AND nbul = 9
//       AND rubq = PA_CALFIC.w_avance_conge;
		
		l = service.find("Select count(*) from ElementVariableDetailMois where identreprise ='"+cdos+"' and nmat = '"+nmat+"' and aamm = '"+pmcf+"' and nbul = 9 and rubq = '"+param.getFictiveRubrique()+"'");
		if(! l.isEmpty() && l.get(0) != null)
			nb = Integer.valueOf(l.get(0).toString());
//
//    IF NOT PA_PAIE.NouZ(w_nb) THEN
		if(nb != 0)
		{
//       -- l'avance conges existe deja : UPDATE
//       UPDATE paevar SET mont = w_mont
//        WHERE cdos = r_cdos
//          AND nmat = r_nmat
//          AND aamm = r_pmcf
//          AND nbul = 9
//          AND rubq = PA_CALFIC.w_avance_conge;
			query="Update ElementVariableDetailMois set mont = "+mont+" where identreprise ='"+cdos+"' and nmat = '"+nmat+"' and aamm = '"+pmcf+"' and nbul = 9 and rubq = '"+param.getFictiveRubrique();
			session.createSQLQuery(query).executeUpdate();
		}
//    ELSE
		else
		{
//       -- l'avance conges n'existe pas : INSERT
//       INSERT INTO paevar
//       VALUES (r_cdos,r_pmcf,r_nmat,9,
//               PA_CALFIC.w_avance_conge,null,null,null,w_mont,'AUTO');
			ElementVariableDetailMois det = new ElementVariableDetailMois();
			int maxLig = this.getMaxNumLigneEltVar(9) + 1;
			det.setId(maxLig);
			det.setIdEntreprise(Integer.valueOf(cdos));
			det.setNbul(9);
			det.setNmat(nmat);
			det.setAamm(pmcf);
			det.setMont(new BigDecimal(mont));
			det.setRubq(param.getFictiveRubrique());
			det.setArgu("AUTO");
			det.setCuti("AUTO");
			session.save(det);
			
			
//
//       BEGIN
//          SELECT 'X' INTO w_exist FROM paev
//           WHERE cdos = r_cdos
//             AND nmat = r_nmat
//             AND aamm = r_pmcf
//             AND nbul = 9;
			nb = 0;
			l = service.find("Select count(*) from ElementVariableEnteteMois where identreprise ='"+cdos+"' and nmat = '"+nmat+"' and aamm = '"+pmcf+"' and nbul = 9");
			if(! l.isEmpty() && l.get(0) != null)
				nb = Integer.valueOf(l.get(0).toString());
//       EXCEPTION
//          WHEN NO_DATA_FOUND THEN null;
//       END;
//       IF SQL%NOTFOUND THEN
//          INSERT INTO paev
//          VALUES (r_cdos,r_pmcf,r_nmat,9,
//                  PA_CALFIC.memo_ddpa,PA_CALFIC.memo_dfpa,'N');
			if(nb == 0)
			{
				ElementVariableEnteteMois ent = new ElementVariableEnteteMois();
				ent.setIdEntreprise(Integer.valueOf(cdos));
				ent.setAamm(pmcf);
				ent.setNbul(9);
				ent.setNmat(nmat);
				ent.setBcmo("N");
				ent.setDdpa(param.getFictiveFirstDayOfMonth());
				ent.setDfpa(param.getFictiveLastDayOfMonth());
				
				session.save(ent);
			}
//       END IF;
//    END IF;
		}
//
//    -- Generation du bulletin fictif dans pafic
//    FOR i IN 1..cpt_res LOOP
		for( int ii =1; ii<= cpt_res; ii++)
		{
//       INSERT INTO pafic
//       VALUES (tab_res(i).cdos,
//               tab_res(i).nmat,
//               SUBSTR(r_pmcf,1,4) || '99',
//               9,
//               i,
//               tab_res(i).rubq,
//               tab_res(i).basc,
//               tab_res(i).basp,
//               tab_res(i).taux,
//               tab_res(i).mont,
//               tab_res(i).nprt,
//               tab_res(i).ruba,
//               tab_res(i).argu,
//               tab_res(i).clas,
//               tab_res(i).trtb
//              );
			fictif = new CongeFictif();
			fictif.setIdEntreprise(Integer.valueOf(cdos));
			fictif.setNmat(nmat);
			fictif.setAamm(StringUtil.oraSubstring(pmcf,1,4) + "99");
			fictif.setNbul(9);
			fictif.setNlig(ii);
			fictif.setRubq(tabres.get(ii).getRubq());
			fictif.setBasc(tabres.get(ii).getBasc());
			fictif.setBasp(tabres.get(ii).getBasp());
			fictif.setTaux(tabres.get(ii).getTaux());
			fictif.setMont(tabres.get(ii).getMont());
			fictif.setNprt(tabres.get(ii).getNprt());
			fictif.setRuba(tabres.get(ii).getRuba());
			fictif.setArgu(tabres.get(ii).getArgu());
			fictif.setClas(tabres.get(ii).getClas());
			fictif.setTrtb(tabres.get(ii).getTrtb());
			
			session.save(fictif);
//    END LOOP;
		}
// END IF;
		}
	public Object[] elementsEVAvancesConges = null; //0 = mont, 1=argu, 2 = nprt, 3 =ruba

	/**
	 * => trait_sal
	 * <p>
	 * Traitement proprement dit de la paie d'un salarié
	 * </p>
	 */
	@SuppressWarnings("unchecked")
	private void traitementSalaireFictif(String strDateFormat)
	{
		// -- Suppression de l'avance conge si conge deja valide de la base de donnée et de la liste des ev é traiter pour le mois pour le salarié
		this.deleteAvanceConge();
		
		this.getListOfEltvar().remove(this.param.getFictiveRubrique());
		
//		if (StringUtils.equals(this.param.nomClient, ClsEntreprise.COMILOG))
//			this.param.chargerPeriodeDePaie(this.infoSalary.getCods());
//		else
		this.param.chargerPeriodeDePaieFictif();

		l_traitmois = true;

		while (l_traitmois)
		{
			// ----------------------------------------------------------------------------
			// -- PAIE AU NET : Initialisations
			// ----------------------------------------------------------------------------
			this.param.bouclage = "O";
			
			this.oFictifPeriod.setAammExercice(this.param.myMonthOfPay.getYearAndMonthInt());

			// -------------------------------------------------------------------------------
			// -- Calcul du rang du mois de paie dans l' exercice
			// -------------------------------------------------------------------------------
			if (!ClsObjectUtil.isNull(this.param.dtDdex))
			{
				this.param.rangMoisDePaieExercice = this.param.myMonthOfPay.getMonth() - new ClsDate(this.param.dtDdex).getMonth() + 1;
				if (this.param.rangMoisDePaieExercice <= 0)
					this.param.rangMoisDePaieExercice += 12;
			}

			dern_mois_de_conge = false;
			try
			{
				anciennete = this.calculateAnciennete(this.param.appDateFormat);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				this.param.setPbWithCalulation(true);
				return;
			}
			if (param.isPbWithCalulation())
				return;

			if (StringUtil.in(infoSalary.getMrrx(), "MU,RA") && (infoSalary.getDmrr() != null)
					&& ClsDate.getDateS(infoSalary.getDmrr(), ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).equals(this.param.getMonthOfPay()) && (this.param.numeroBulletin == 9))
			{
				this.param.stc = true;
				if (!this.chargerRubriqueMotifFinContrat(infoSalary.getMtfr()))
				{
					this.param.setPbWithCalulation(true);
					this.param.error = this.param.errorMessage("Sal: " + infoSalary.getComp_id().getNmat() + ". Motif de radiation (" + infoSalary.getMtfr() + ") inexistant en table 23", this.param.langue);
					return;
				}
			}

			if (StringUtils.equals("O", infoSalary.getPnet()))
			{
				this.chargementRubriqueSaisiesEnNetAjus();
			}// StringUtils.equals("O", infoSalary.getPnet())


			if ("O".equals(this.param.getBase30Rubrique()))
			{
				if (this.param.getBase30NombreJour() > 0)
				{
					this.workTimeFictif.setProrataNbreJourTravaillees(this.param.getBase30NombreJour());
				}
				else
					this.workTimeFictif.setProrataNbreJourTravaillees(30);
			}
			else
				this.workTimeFictif.setProrataNbreJourTravaillees(param.myMonthOfPay.getMaxDayOfMonth());

			
			// nouvel ajout, par yannick, je propose de reinitialiser worktime é chaque itération
			this.workTimeFictif.resetValues();
			
			this.calculNbrePeriodeRegulation();

			this.calculateNbreJourSuppl();

			this.calculNbreJourPaidNonPris();

			this.calculCongeAbsence();

			if (this.param.isPbWithCalulation())
				return;

			this.workTimeFictif.setNbreJourPresence(this.workTimeFictif.getProrataNbreJourTravaillees() - this.workTimeFictif.getNbreJourAbsence() - this.workTimeFictif.getNbreJoursAbsencePourCongeAnnuel());
			// Nbj_Presence_du_Mois = this.workTimeFictif.getNbreJourPresence()
			// Nbj_Conges_Cal_du_Mois = this.workTimeFictif.getNbreJoursAbsencePourCongeAnnuel()

			this.workTimeFictif.prorataNbreHeuresJoursTravailles(this);

//			if (this.param.genfile == 'O')
//				StringUtils.printOutObject(this.workTimeFictif, "C:\\deltarh\\calculpaie\\fictifworktime" + this.param.getMonthOfPay() + ".txt");

			boolean arret = false;

			while (true)
			{
				if (StringUtils.equals("O", this.infoSalary.getPnet()))
				{
					this.nbiter = 0;
					this.getParam().setNumeroAjustementActuel(this.getParam().getNumeroAjustementActuel() + 1);
					this.compteurValeurAjus = this.maxNbreValeurAjus;
					double nbreJourProrata = this.workTimeFictif.getProrataNbreJourTravaillees() - this.workTimeFictif.getNbreJourAbsence();
					if (this.getEntreprise().equals(ClsEnumeration.EnEnterprise.SHELL_GABON))
						nbreJourProrata += this.workTimeFictif.getNbreJourCongesNonPris();

					double a = 0;
					if (!ClsObjectUtil.isNull(this.getInfoSalary().getSnet()))
						a = this.getInfoSalary().getSnet().doubleValue();

					if (this.workTimeFictif.getProrataNbreJourTravaillees() != 0)
						this.salaireNetATrouver = Math.ceil(a * nbreJourProrata / this.workTimeFictif.getProrataNbreJourTravaillees());

					salaireIterPaire = 0;
					salaireIterImpaire = 0;
					double salaireAjouterAuNet = 0;
					double tauxAjustement = 0;
					if (this.param.isRubriqueNetExist())
					{
						String queryAjouterAuNet = "select sum(mont) from ElementSalaireNet" + " where ajnu <= " + this.param.getNumeroAjustementActuel() + " and session_id = '" + this.param.getsession_id() + "'";
						List l = service.find(queryAjouterAuNet);
						if (l != null && l.size() > 0 && l.get(0) != null)
						{
							salaireAjouterAuNet = ((BigDecimal) l.get(0)).doubleValue();
						}

						this.salaireNetATrouver += salaireAjouterAuNet;
					}
					if ((!ClsObjectUtil.isNull(this.getInfoSalary().getSnet())) && (this.salaireNetATrouver != this.getInfoSalary().getSnet().doubleValue()))
					{
						if (this.getInfoSalary().getSnet().doubleValue() != 0)
							tauxAjustement = this.salaireNetATrouver / this.getInfoSalary().getSnet().doubleValue();

						List l = service.find("from ElementSalaireajus" + " where ajnu is not null " + " and ajnu = " + this.param.getNumeroAjustementActuel() + " and session_id = '" + this.param.getsession_id() + "'"
								+ " and mont is not null ");
						//
						try
						{
							ElementSalaireAjus ajust = null;
							for (Object obj : l)
							{
								ajust = (ElementSalaireAjus) obj;
								service.updateFromTable("update ElementSalaireAjus set mont = mont *" + tauxAjustement + " where ajnu = " + ajust.getAjnu() + " and crub = '" + ajust.getCrub() + "'" + " and sessionid = "
										+ ajust.getSessionId());
							}
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}
				while (true)
				{

					this.salaireAecarterDuNet = 0;
					this.salaireNet = 0;
					this.salaireBrut = 0;

					if (StringUtils.equals("O", this.infoSalary.getPnet()))
					{
						this.deleteFictifFictif(true, true, false);
						this.derniereLigne = 0;
					}

					processAllRubrique(0, strDateFormat);

					if (this.param.isPbWithCalulation())
						return;

					if (this.param.isStc() && ! this.param.isRubriqueStc())
					{
						this.param.setPbWithCalulation(true);
						this.param.error = this.param.errorMessage("Sal: " + infoSalary.getComp_id().getNmat() + ". Motif de radiation (" + infoSalary.getMtfr() + ") Pb sur RAZ tableau.", this.param.langue);
					}
					if (param.isPbWithCalulation())
						return;

					if (!StringUtils.equals("O", this.infoSalary.getPnet()))
						break;

					this.param.setCalcul("N");

					this.comparerAvecNet();

					if ("N".equals(this.param.getCalcul()))
						break;
				}

				if (!StringUtils.equals("O", this.infoSalary.getPnet()))
					break;

				if (!param.isPbWithCalulation())
					processAllRubrique(1, strDateFormat);

				arret = true;

				int i = 0;
				if (this.param.isRubriqueNetExist())
				{
					String queryAjouterAuNet = "select count(*) from ElementSalaireNet where ajnu > " + this.param.getNumeroAjustementActuel() + " and session_id = '" + this.param.getsession_id() + "'";
					List l = service.find(queryAjouterAuNet);
					if (l != null && l.size() > 0)
					{
						i = ((Long) l.get(0)).intValue();
						if (i != 0)
							arret = false;
					}
				}

				if (arret)
					break;

			} // while (true)

			this.param.setMyMonthOfPay(new ClsDate(this.param.getMyMonthOfPay().addMonth(1)));
			this.param.setMonthOfPay(this.param.getMyMonthOfPay().getYearAndMonth());
			ClsDate oClsDate = new ClsDate(this.param.monthOfPay, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
			if (oClsDate.getFirstDayOfMonth().compareTo(this.param.dtDfex) > 0)
			{
				this.param.dtDdex = new ClsDate(this.param.dtDdex).addMonth(12);
				this.param.dtDfex = new ClsDate(this.param.dtDfex).addMonth(12);
			}

			ClsDate myExcercise = new ClsDate(this.param.dtDdex);
			this.param.debutExerciceAnnee = myExcercise.getYear();
			this.param.debutExerciceMois = myExcercise.getMonth();
			this.param.setDossierDateDebutExercice(new ClsDate(this.param.dtDdex).getDateS(this.param.appDateFormat));
			this.param.setDossierDateFinExercice(new ClsDate(this.param.dtDfex).getDateS(this.param.appDateFormat));
			this.param.debutExerciceaamm = this.param.debutExerciceAnnee * 100 + this.param.debutExerciceMois;
			this.param.debutExerciceRangMoisPaie = this.param.getMyMonthOfPay().getMonth() - this.param.debutExerciceMois + 1;


//			if (StringUtils.equals(this.param.nomClient, ClsEntreprise.COMILOG))
//				this.param.chargerPeriodeDePaie(this.infoSalary.getCods());
//			else
				this.param.chargerPeriodeDePaieFictif();

			if ((this.infoSalary.getDfcf().compareTo(this.param.lastDayOfMonth) >= 0)
					|| (ClsDate.getDateS(this.infoSalary.getDfcf(), ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).equals(ClsDate.getDateS(this.param.lastDayOfMonth, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM)))
					|| (this.infoSalary.getDfcf().compareTo(this.param.firstDayOfMonth) >= 0 && this.infoSalary.getDfcf().compareTo(this.param.lastDayOfMonth) <= 0))
			{
				l_traitmois = true;
				//this.param.reBuildListOfRubrique(this);
				//
				this.buildElementVarMap();

				this.buildElementFixeMap();

				this.buildLignePretMap();

				this.buildNumeroPretMap();
				
				//this.buildSpecifiqueCumul99Map();
			}
			else
				l_traitmois = false;
			
//			if (param.genfile == 'O')
//			{
//				String texte = StringUtils.toString(this) + "\n" + StringUtils.toString(this.workTimeFictif) + "\n---------------LISTE PARAMETRAGE POUR LE SALARIE"
//						+ StringUtils.toString(this.getParam());
//
//				StringUtils.printOutObject(texte, param.getGenfilefolder() + "\\" + this.getInfoSalary().getComp_id().getNmat()+"-"+param.getMonthOfPay() + ".txt");
//			}

		}// while (l_traitmois)
	}

	/**
	 * => chg_rubstc
	 * <p>
	 * Construire la liste des rubriques motif fin de contrat
	 * </p>
	 * Lecture de la table 023 'Motifs de fin de contrat'. Les libelles 2,3 et 4 contiennent les rbq a declencher pour le motif (XXXX+YYYY+ZZZZ+...) Les rbq
	 * sont chargees dans un tableau puis activees pour un calcul de STC
	 */

	public boolean chargerRubriqueMotifFinContrat(String motf)
	{

		String libelle2 = utilNomenclatureFictif.getLabelFromNomenclature(infoSalary.getComp_id().getCdos(), 23, motf, 2, param.nlot, param.numeroBulletin, param.monthOfPay);

		if (libelle2 == null || libelle2 == "")
			return false;

		String libelle3 = utilNomenclatureFictif.getLabelFromNomenclature(infoSalary.getComp_id().getCdos(), 23, motf, 3, param.nlot, param.numeroBulletin, param.monthOfPay);

		String libelle4 = utilNomenclatureFictif.getLabelFromNomenclature(infoSalary.getComp_id().getCdos(), 23, motf, 4, param.nlot, param.numeroBulletin, param.monthOfPay);

		String arrayRubrique = libelle2;
		if (StringUtils.isNotBlank(libelle3))
			arrayRubrique += libelle3;
		if (StringUtils.isNotBlank(libelle4))
			arrayRubrique += libelle4;
		List<String> liste = this._loadListOfRubriqueOfMotifFinContrat(arrayRubrique);

		ClsFictifRubriqueClone rubriqueClone = null;
		ElementSalaire myRubrique = null;
//		if (ListOfRubriqueOfMotifFinContratFictif == null)
			ListOfRubriqueOfMotifFinContratFictif = new ArrayList<ClsFictifRubriqueClone>();
		for (String string : liste)
		{
			myRubrique = (ElementSalaire) service.find("FROM ElementSalaire WHERE identreprise="+infoSalary.getComp_id().getCdos()
															 +" AND crub='"+string+"'");
			ClsParubqClone myRubriqueClone = null;
			if (myRubrique != null)
			{
				myRubriqueClone = new ClsParubqClone(myRubrique);
				rubriqueClone = new ClsFictifRubriqueClone(this);
				rubriqueClone.setRubrique(myRubriqueClone);
				ListOfRubriqueOfMotifFinContratFictif.add(rubriqueClone);
			}
		}
		return true;
	}

	private List<String> _loadListOfRubriqueOfMotifFinContrat(String arrayRubrique)
	{
		List<String> liste = new ArrayList<String>();
		if (StringUtils.isNotBlank(arrayRubrique))
		{
			int i = 0;
			while (i < arrayRubrique.length())
			{
				if (StringUtils.isNotBlank(StringUtils.substring(arrayRubrique, i, i + ParameterUtil.longueurRubrique)))
				{
					liste.add(StringUtils.substring(arrayRubrique, i, i + ParameterUtil.longueurRubrique));
					i = i + ParameterUtil.longueurRubrique +1;
				}
			}
		}
		return liste;
	}

	/**
	 * =>paf_TypePaiement Retrouve le type de paiement é utiliser
	 * 
	 * @return le type de paiement
	 */
	public String getTypePaiement()
	{
		String typa = utilNomenclatureFictif.getLabelFromNomenclature(infoSalary.getComp_id().getCdos(), 54, infoSalary.getModp(), 4, param.nlot, param.numeroBulletin, param.monthOfPay);
		//
		return typa;
	}

	/**
	 * TEST SI SALARIE EXPATRIE
	 * <p>
	 * typ_rec = Zone de pasa01 a tester : 1=Regime, 2=Type de contrat, 3=Classe salarie
	 * </p>
	 * <p>
	 * val_exp = Valeur de la zone pour un expatrie
	 * </p>
	 * 
	 * @return true si c'est un expatrié ou false dans le cas contraire
	 */
	public boolean _isExpatrie()
	{
		int typeContratExpatrie = param.getExpatrieTypeContrat();
		String valeurExpatrie = param.getExpatrieValeur();
		// Expatrie := FALSE;
		// IF typ_rec = 1 AND wsal01.regi = val_exp THEN Expatrie := TRUE; END
		// IF;
		if (typeContratExpatrie == 1 && valeurExpatrie.equals(this.infoSalary.getRegi()))
			return true;
		// IF typ_rec = 2 AND wsal01.typc = val_exp THEN Expatrie := TRUE; END
		// IF;
		if (typeContratExpatrie == 2 && valeurExpatrie.equals(this.infoSalary.getTypc()))
			return true;
		// IF typ_rec = 3 AND wsal01.clas = val_exp THEN Expatrie := TRUE; END
		// IF;
		if (typeContratExpatrie == 3 && valeurExpatrie.equals(this.infoSalary.getClas()))
			return true;
		return false;
	}

	/**
	 * calculate anciennete of salary
	 */
	public int calculateAnciennete(String strDateFormat) throws Exception
	{
		if (this.infoSalary.getDdca() == null)
			return 0;
		ClsDate myDdca = new ClsDate(this.infoSalary.getDdca());
		int nbreYear = 0;

		if (entreprise.equals(ClsEnumeration.EnEnterprise.SGMB) && myDdca.getDay() != 1)
			this.infoSalary.setDdca(myDdca.clone().addMonth(1));

		int nyear = param.getMyMonthOfPay().getYear();

		if ((nyear - myDdca.getYear()) > this.getParam().AGE_MAX_OF_SALARY)
		{
			param.setError(param.errorMessage("ERR-90045", param.getLangue(), infoSalary.getComp_id().getNmat()));

			setOutputtext("\n" + param.getError());
			getParam().setPbWithCalulation(true);
		}

		nbreYear = calculateAnciennetePre(strDateFormat);

		if (nbreYear < 0)
		{
			param.setError(param.errorMessage("ERR-90046", param.getLangue(), infoSalary.getComp_id().getNmat()));

			setOutputtext("\n" + param.getError());
			getParam().setPbWithCalulation(true);
		}

		if (nbreYear > param.AGE_MAX_OF_SALARY)
		{
			param.setError(param.errorMessage("ERR-90045", param.getLangue(), infoSalary.getComp_id().getNmat()));

			setOutputtext("\n" + param.getError());
			getParam().setPbWithCalulation(true);
		}

		if (nbreYear > param.getAncienneteMaxi())
			this.setAnciennete(param.getAncienneteMaxi());
		else
			this.setAnciennete(nbreYear);
		return nbreYear;
	}

	public int calculateAnciennetePre(String strDateFormat)
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
		ClsDate myMonthOfPay = new ClsDate(param.getMonthOfPay(), ClsFictifParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
		Date monthOfPayDate = myMonthOfPay.getDate();
		String queryString = "select ddar, dfar from SuspensionPaie" + " where identreprise ='" + this.param.getDossier() + "'" + " and mtar in (" + " select cacc from ParamData" + " where identreprise ='"
				+ this.param.getDossier() + "'" + " and ctab = 21" + " and nume = 1 and valm =1)" + " and nmat ='" + this.infoSalary.getComp_id().getNmat() + "'" + " and ddar < '"
				+ param.myMonthOfPay.getDateS(strDateFormat) + "'" + " order by ddar";

		List listOfArriere = service.find(queryString);
		//
		long l_nDayDeduted = 0;
		Object[] rowOfArr = null;
		long millisecondperday = 86400000;
		for (Object object : listOfArriere)
		{
			rowOfArr = (Object[]) object;

			setOutputtext("\n" + ">>Date = " + String.valueOf(rowOfArr[0]) + " " + this.getParam().getAppDateFormat());
			dateDebutArriereOfPay = (Date) rowOfArr[0];
			dateFinArriereOfPay = (Date) rowOfArr[1];

			if (dateFinArriereOfPay.compareTo(monthOfPayDate) >= 0)
			{
				l_nDayDeduted = (monthOfPayDate.getTime() - dateDebutArriereOfPay.getTime()) / millisecondperday;
			}
			else
			{
				l_nDayDeduted = (dateFinArriereOfPay.getTime() - dateDebutArriereOfPay.getTime()) / millisecondperday;
			}
			nDayDeduted += l_nDayDeduted;
		}
		nMonthDeducted = nDayDeduted / 30;

		ClsDate myDdca = new ClsDate(this.infoSalary.getDdca());
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

	/**
	 * Calcul de l'age de l'agent
	 * 
	 * @return l'age
	 */
	public int calculateAgentAge()
	{
		// -- CALCUL Age de l'agent
		Date dtLastDayOfMonthPay = param.getMyMonthOfPay().getLastDayOfMonth();
		int ecart = ClsDate.getMonthsBetween(infoSalary.getDtna(), dtLastDayOfMonthPay);
		int nombreYears = ecart / 12;
		//
		// -- MM Modif DELTACI-010302-94 Controle de coherence sur la date
		// naissance
		// IF FLOOR( ( dtLastDayOfMonthPay - wsal01.dtna ) / 365 ) > 100 THEN
		// pb_calcul := TRUE;
		// err_msg := PA_PAIE.erreurp('ERR-90047',w_clang,wsal01.nmat);
		// RETURN FALSE;
		// END IF;
		//
		// nbr_an := FLOOR( ( dtLastDayOfMonthPay - wsal01.dtna ) / 365 );
		// IF nbr_an > 100 THEN
		// pb_calcul := TRUE;
		// err_msg := PA_PAIE.erreurp('ERR-90047',w_clang,wsal01.nmat);
		// RETURN FALSE;
		// END IF;
		if (nombreYears > 100)
		{
			param.setError(param.errorMessage("ERR-90047", param.getLangue(), infoSalary.getComp_id().getNmat()));

			setOutputtext("\n" + param.getError());
			getParam().setPbWithCalulation(true);
			return -1;
		}
		// IF nbr_an < 0 THEN
		// pb_calcul := TRUE;
		// err_msg := PA_PAIE.erreurp('ERR-90048',w_clang,wsal01.nmat);
		// RETURN FALSE;
		// END IF;
		if (nombreYears < 0)
		{
			param.setError(param.errorMessage("ERR-90048", param.getLangue(), infoSalary.getComp_id().getNmat()));

			setOutputtext("\n" + param.getError());
			getParam().setPbWithCalulation(true);
			return -1;
		}
		// Age_Agent := nbr_an;
		this.ageOfAgent = nombreYears;
		return this.ageOfAgent;
	}

	/**
	 * update the amount of rubriques under adjustment => charg_ajus
	 */
	private void chargementMontantDesRubriquesAdjustment()
	{
		double montant = 0;

		List listOfCount = null;

		int count = 0;

		String queryString = "from ElementSalaireajus" + " where session_id =" + param.getsession_id();

		try
		{
			service.updateFromTable("update ElementSalaireajus set mont = 0 where session_id = " + param.getsession_id());

			List listOfRubajus = service.find(queryString);

			String strQueryUpdate = "update ElementSalaireajus a " + " set mont = (select c.monp from ElementFixeSalaire c" + " where  c.identreprise ='" + param.getDossier() + "' and c.nmat = '" + infoSalary.getComp_id().getNmat()
					+ "'" + " and c.codp = a.crub " + " )" + " where exists(" + " select b.codp from ElementFixeSalaire b" + " where b.identreprise ='" + param.getDossier() + "'" + " and b.nmat = '"
					+ infoSalary.getComp_id().getNmat() + "'" + " and b.codp = a.crub " + " and ( b.ddeb is null or (b.ddeb is not null and TO_CHAR(b.ddeb,'yyyyMM') <= '" + param.getMyMonthOfPay().getYearAndMonth()
					+ "'))" + " and ( b.dfin is null or (b.dfin is not null and TO_CHAR(b.dfin,'yyyyMM') >= '" + param.getMyMonthOfPay().getYearAndMonth() + "'))" + " )" + " and session_id = " + param.getsession_id();
			
			if (StringUtils.equals(TypeBDUtil.typeBD, TypeBDUtil.MS))
			{
				strQueryUpdate = "update ElementSalaireajus a " + " set mont = (select c.monp from ElementFixeSalaire c" + " where  c.identreprise ='" + param.getDossier() + "' and c.nmat = '" + infoSalary.getComp_id().getNmat()
				+ "'" + " and c.codp = a.crub " + " )" + " where exists(" + " select b.codp from ElementFixeSalaire b" + " where b.identreprise ='" + param.getDossier() + "'" + " and b.nmat = '"
				+ infoSalary.getComp_id().getNmat() + "'" + " and b.codp = a.crub " + " and ( b.ddeb is null or (b.ddeb is not null and do.formaterDateEnChaine(b.ddeb,'yyyyMM') <= '" + param.getMyMonthOfPay().getYearAndMonth()
				+ "'))" + " and ( b.dfin is null or (b.dfin is not null and do.formaterDateEnChaine(b.dfin,'yyyyMM') >= '" + param.getMyMonthOfPay().getYearAndMonth() + "'))" + " )" + " and session_id = " + param.getsession_id();
			}
			this.setOutputtext("\n Premiére Requete de mise é jour de la table ElementSalaireajus " + strQueryUpdate);
			session = service.getSession();

			session.createSQLQuery(strQueryUpdate).executeUpdate();

			service.closeSession(session);

			listOfCount = service.find("select count(*) from ElementVariableDetailMois" + " where identreprise ='" + param.getDossier() + "'" + " and nmat = '" + infoSalary.getComp_id().getNmat() + "'" + " and aamm = '"
					+ param.getMyMonthOfPay().getYearAndMonth() + "'" + " and nbul = " + param.getNumeroBulletin());
			count = 0;
			if (listOfCount != null && listOfCount.size() > 0)
			{
				count = ((Long) listOfCount.get(0)).intValue();
			}

			if (count > 0)
			{

				strQueryUpdate = "update ElementSalaireajus a " + "set mont = (select sum( mont ) from ElementVariableDetailMois" + " where identreprise ='" + param.getDossier() + "' and nmat = '" + infoSalary.getComp_id().getNmat() + "'"
						+ " and rubq = a.crub " + " and aamm = '" + param.getMyMonthOfPay().getYearAndMonth() + "'" + " and nbul = " + param.getNumeroBulletin() + " )" + " where exists("
						+ " select rubq from ElementVariableDetailMois" + " where identreprise ='" + param.getDossier() + "'" + " and nmat = '" + infoSalary.getComp_id().getNmat() + "'" + " and rubq = a.crub " + " and aamm = '"
						+ param.getMyMonthOfPay().getYearAndMonth() + "'" + " and nbul = " + param.getNumeroBulletin() + " )" + " and session_id = " + param.getsession_id();

				this.setOutputtext("\n Deuxiéme Requete de mise é jour de la table ElementSalaireajus " + strQueryUpdate);

				session = service.getSession();

				session.createSQLQuery(strQueryUpdate).executeUpdate();

				service.closeSession(session);
			}

			String strQuery = "update ElementSalaireajus set mont = 0" + " where session_id = " + param.getsession_id();
			session = service.getSession();

			session.createSQLQuery(strQuery).executeUpdate();

			service.closeSession(session);

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * => PA_ALGO.calc_nbjsup
	 * <p>
	 * Calcul du nombre de jours supplementaires
	 * 
	 * @return le nombre de jours supplementaires
	 */
	public double calculNombreDJourSuppl()
	{
		String cdos = getInfoSalary().getComp_id().getCdos();
		int montant = 0;
		double taux1 = 0;
		double taux2 = 0;
		// nbr_an NUMBER(5);
		// int nbr_an = 0;
		// // an_s NUMBER(5);
		// int an_s = 0;
		// // mois_s NUMBER(5);
		// int mois_s = 0;
		// // nb_enf NUMBER(5);
		// int nb_enf = 0;
		// // nbj_par_enf NUMBER(5);
		// double nbj_par_enf = 0;
		// // l_tranche NUMBER(5,2);
		// double l_tranche = 0;
		// //
		// // nbj_sup NUMBER(5,2);
		// double nbj_sup = 0;
		//
		// BEGIN
		//
		// nbj_sup := 0;
		// nbj_deco := 0;
		//
		// -- Calcul des jours supp selon la categorie et
		// -- acquis tous les MNT3 annees
		montant = new Double((tempNumber = getUtilNomenclatureFictif().getAmountOrRateFromNomenclature(cdos, 35, getInfoSalary().getCat(), 3, param.getNlot(), param.getNumeroBulletin(), param.getMonthOfPay(),
				ClsEnumeration.EnColumnToRead.AMOUNT)) == null ? 0 : tempNumber.intValue()).intValue();

		taux1 = new Double((tempNumber = getUtilNomenclatureFictif().getAmountOrRateFromNomenclature(cdos, 35, getInfoSalary().getCat(), 1, param.getNlot(), param.getNumeroBulletin(), param.getMonthOfPay(),
				ClsEnumeration.EnColumnToRead.RATES)) == null ? 0 : tempNumber.intValue()).doubleValue();
		taux2 = new Double((tempNumber = getUtilNomenclatureFictif().getAmountOrRateFromNomenclature(cdos, 35, getInfoSalary().getCat(), 2, param.getNlot(), param.getNumeroBulletin(), param.getMonthOfPay(),
				ClsEnumeration.EnColumnToRead.RATES)) == null ? 0 : tempNumber.intValue()).doubleValue();
		//
		if (montant <= 0)
			montant = 1;
		//
		// l_tranche := (PA_CALCUL.ancien / wfnom.mnt3);
		double tranche = getAnciennete() / montant;
		// nbj_sup := l_tranche * wfnom.tau1;
		double nbreJourSuppl = tranche * taux1;
		// nbj_par_enf := wfnom.tau2;
		double nbreJourParEnfant = taux2;
		// -- Calcul du nombre de jours supp en fonction de l'anciennete
		String ancienS = ClsStringUtil.formatNumber(getAnciennete(), "00");
		int nbreEnfant = 0;
		//
		// wfnom.tau2 :=
		// paf_lecfnomT(34,char2,2,PA_CALCUL.w_aamm,PA_CALCUL.w_nlot,PA_CALCUL.wsd_fcal1.nbul);
		taux2 = new Double((tempNumber = getUtilNomenclatureFictif().getAmountOrRateFromNomenclature(cdos, 34, ancienS, 2, param.getNlot(), param.getNumeroBulletin(), param.getMonthOfPay(),
				ClsEnumeration.EnColumnToRead.RATES)) == null ? 0 : tempNumber.intValue()).doubleValue();
		//
		// nbj_sup := nbj_sup + wfnom.tau2;
		nbreJourSuppl = nbreJourSuppl + taux2;
		//
		// -- Calcul du nombre de jrs supp en fonction du nombre d'enfants
		// -- a charge, uniquement pour les femmes
		// IF PA_CALCUL.wsal01.sexe = 'F' THEN
		if ("F".equals(getInfoSalary().getSexe()))
		{
			// IF PA_CALCUL.comptage_enfant THEN
			// /*
			// SELECT COUNT(*) INTO nb_enf FROM paenfan
			// WHERE cdos = PA_CALCUL.wpdos.cdos
			// AND nmat = PA_CALCUL.wsal01.nmat
			// AND dtna > SYSDATE - ( age_max_enfant UNITS YEAR );
			// */
			// null;
			// ELSE
			// nb_enf := PA_CALCUL.wsal01.nbec;
			if (!getParam().isEnfantComptage())
				nbreEnfant = getInfoSalary().getNbec();
			// END IF;
			// nbj_sup := nbj_sup + (nbj_par_enf * nb_enf);
			nbreJourSuppl += nbreJourParEnfant * nbreEnfant;
		}
		// END IF;
		//
		// nbj_enf := NVL(nbj_par_enf,0) * NVL(nb_enf,0);
		nbreJourPourEnfant = nbreJourParEnfant * nbreEnfant;
		//
		// -- Calcul du nombre de jrs supp en fonction des decorations
		// nbj_deco := 0;
		double nbreJourDeco = 0;
		// SELECT SUM(valt) INTO nbj_deco from pahfnom
		// WHERE cdos = PA_CALCUL.wpdos.cdos
		// AND ctab = 18
		// AND nume = 1
		// AND cacc IN (SELECT UNIQUE cdis FROM padistin
		// WHERE cdos = PA_CALCUL.wpdos.cdos
		// AND nmat = PA_CALCUL.wsal01.nmat);
		// SELECT SUM(valt) INTO nbj_deco from pafnom
		// WHERE cdos = PA_CALCUL.wpdos.cdos
		// AND ctab = 18
		// AND nume = 1
		// AND cacc IN (SELECT UNIQUE cdis FROM padistin
		// WHERE cdos = PA_CALCUL.wpdos.cdos
		// AND nmat = PA_CALCUL.wsal01.nmat);
		List l = getParam().isUseRetroactif() ? getService().find(
				"select sum(valt) from Rhthfnom" + " where identreprise ='" + cdos + "'" + " and ctab = 18 " + " and nume = 1 " + " and cacc in (select distinct cdis from Rhtdistinctionagent "
						+ " where identreprise ='" + cdos + "'" + " and nmat = '" + getInfoSalary().getComp_id().getNmat() + "')") : getService().find(
				"select sum(valt) from ParamData" + " where identreprise ='" + cdos + "'" + " and ctab = 18 " + " and nume = 1 " + " and cacc in (select distinct cdis from Rhtdistinctionagent "
						+ " where identreprise ='" + cdos + "'" + " and nmat = '" + getInfoSalary().getComp_id().getNmat() + "')");
		if (!ClsObjectUtil.isListEmty(l) && l.get(0) != null)
			nbreJourDeco = ((Long) l.get(0)).doubleValue();
		nbreJourSuppl += nbreJourDeco;
		return nbreJourSuppl;
	}

	/**
	 * => charg_net update the amount of rubriques under adjustment
	 */
	private void chargementMontantDesRubriquesNettes()
	{

		String queryString = "from ElementSalaireNet" + " where session_id =" + param.getsession_id();
		double montant = 0;
		int count = 0;
		List listOfCount = null;
		try
		{
			service.updateFromTable("update ElementSalaireNet set mont = 0" + " where session_id = " + param.getsession_id());

			String queryStringforupdate = "update ElementSalaireNet a " + " set mont = (select c.monp from ElementFixeSalaire c" + " where c.identreprise ='" + param.getDossier() + "'" + " and c.nmat = '"
					+ infoSalary.getComp_id().getNmat() + "'" + " and c.codp = a.crub " + " )" + " where exists(" + " select b.nmat from ElementFixeSalaire b" + " where b.identreprise ='" + param.getDossier() + "'"
					+ " and b.nmat = '" + infoSalary.getComp_id().getNmat() + "'" + " and b.codp = a.crub " + " and ( b.ddeb is null or (b.ddeb is not null and b.ddeb <= '"
					+ new ClsDate(param.getMyMonthOfPay().getLastDayOfMonth()).getDateS(param.appDateFormat) + "'))" + " and ( b.dfin is null or (b.dfin is not null and b.dfin >= '"
					+ new ClsDate(param.getMyMonthOfPay().getFirstDayOfMonth()).getDateS(param.appDateFormat) + "'))" + " )" + " and session_id = " + param.getsession_id();

			this.setOutputtext("\n Requete Initiale de mise é jour de la table ElementSalaireNet " + queryStringforupdate);
			session = service.getSession();
			session.createSQLQuery(queryStringforupdate).executeUpdate();
			service.closeSession(session);

			listOfCount = service.find("select count(*) from ElementVariableDetailMois" + " where identreprise ='" + param.getDossier() + "'" + " and nmat = '" + infoSalary.getComp_id().getNmat() + "'" + " and aamm = '"
					+ param.getMyMonthOfPay().getYearAndMonth() + "'" + " and nbul = " + param.getNumeroBulletin());
			count = 0;
			if (listOfCount != null && listOfCount.size() > 0)
			{
				count = ((Long) listOfCount.get(0)).intValue();
			}

			if (count > 0)
			{

				String queryStringforupdate2 = "update ElementSalaireNet a " + "set mont = (select sum( mont ) from ElementVariableDetailMois" + " where identreprise ='" + param.getDossier() + "'" + " and nmat = '"
						+ infoSalary.getComp_id().getNmat() + "'" + " and rubq = a.crub " + " and aamm = '" + param.getMyMonthOfPay().getYearAndMonth() + "'" + " and nbul = " + param.getNumeroBulletin() + " )"
						+ " where exists(" + " select * from ElementVariableDetailMois" + " where identreprise ='" + param.getDossier() + "'" + " and nmat = '" + infoSalary.getComp_id().getNmat() + "'" + " and rubq = a.crub "
						+ " and aamm = '" + param.getMyMonthOfPay().getYearAndMonth() + "'" + " and nbul = " + param.getNumeroBulletin() + " )" + " and session_id = " + param.getsession_id();

				this.setOutputtext("\n Deuxieme Requete de mise é jour de la table ElementSalaireNet " + queryStringforupdate2);
				session = service.getSession();
				session.createSQLQuery(queryStringforupdate).executeUpdate();
				service.closeSession(session);
			}

			String strQuery = "update ElementSalaireNet set mont = 0" + " where session_id = " + param.getsession_id();
			session = service.getSession();

			session.createSQLQuery(strQuery).executeUpdate();

			service.closeSession(session);

		}
		catch (SQLException e)
		{

			e.printStackTrace();
		}
	}

	/**
	 * => corps() process all the rubriques of that salary
	 * 
	 * @param w_dec1
	 */
	public void processAllRubrique(int w_dec1, String dateformat)
	{
		// if('O' == this.getParam().getGenfile()) setOutputtext("\n"+">> processAllRubrique");
		// if('O' == this.getParam().getGenfile()) setOutputtext("\n"+">> AJUSTEMENT :" + w_dec1);
		//
		if (this.getParam().isUseRetroactif())
			processAllRubrique(param.listOrdonneRubrique, w_dec1, dateformat);
		else
			processAllRubrique(param.listOrdonneRubrique, w_dec1, dateformat);
	}

	/**
	 * => corps() process all the rubriques of that salary numeroAjustement => w_numcal
	 * 
	 * @param listOfAllRubrique
	 * @param w_dec1
	 */
	boolean applyTaux = false;

	private void processAllRubrique(List<String> listOrdonneRubrique, int w_dec1, String dateformat)
	{
		boolean go = false;

		String query = "select count(*) from CongeFictif";
		int nbrFictif = 0;
		List l = service.find(query);
		if (l != null && l.size() > 0)
			nbrFictif = ((Long) l.get(0)).intValue();

		if (w_dec1 == 0)
		{
			param.reBuildListOfRubrique(this,ParameterUtil.formatRubrique);
			this.derniereLigne = 0;
			if (StringUtils.equals("O", this.infoSalary.getPnet()) && nbrFictif > 0)
			{
				this.deleteFictifFictif(true, true, false);
			}
		}
		else
		{
			// Indice_Debut := TO_NUMBER( w_rubnet ) + 1;
			// FOR i IN Indice_Debut..9999 LOOP
			// t9999_basc(i):=0;
			// t9999_basp(i):=0;
			// t9999_taux(i):=0;
			// t9999_mont(i):=0;
			// END LOOP;
			param.reBuildListOfRubrique(this,this.param.paieAuNetRubrique);
			
			this.deleteFictifFictif(true, true, true);
		}

		String codeRubrique = "";
		String rubriqueNet = this.getParam().getPaieAuNetRubrique();
		String rubriqueAffectationEcart = this.getParam().getPaieAuNetAffectationEcartRubrique();
		ClsFictifRubriqueClone rubriqueClone = null;
		for (String crub : listOrdonneRubrique)
		{
			this.setOutputtext("\n########################### Calcul de la rubrique" + crub+" ########################");
			rubriqueClone = param.getListOfAllFictiveRubriqueMap().get(crub);
			codeRubrique = rubriqueClone.getRubrique().getComp_id().getCrub();

			go = true;

			if ((w_dec1 == 1) && (rubriqueClone.getRubrique().getComp_id().getCrub().compareTo(this.getParam().getPaieAuNetRubrique()) <= 0))
				go = false;

			if (go)
			{

				if ("O".equals(this.infoSalary.getPnet()) && rubriqueClone.getRubrique().getComp_id().getCrub().equals(this.getParam().getPaieAuNetAffectationEcartRubrique())
						&& param.getPaieAuNetAffectationEcartRubrique().compareToIgnoreCase(rubriqueNet) > 0)
					rubriqueClone.setRates(0);
				else
				{
					rubriqueClone.setRates(0);
					rubriqueClone.setAmount(0);
					rubriqueClone.setBase(0);
					rubriqueClone.setBasePlafonnee(0);
				}

				if ("N".equals(rubriqueClone.getRubrique().getCalc()))
				{
					go = false;

					if (this.getParam().isStc() && this.getParam().isRubriqueStc())
					{
						for (ClsFictifRubriqueClone clone : ListOfRubriqueOfMotifFinContratFictif)
						{
							if (codeRubrique.equals(clone.getRubrique().getComp_id().getCrub()))
							{
								go = true;
								break;
							}
						}
					}
				}

				if (go)
				{
					if (StringUtils.equals("N", rubriqueClone.getRubrique().getApcf()))
						go = false;

					if (StringUtils.equals("E", rubriqueClone.getRubrique().getApcf()))
						go = false;

					if (StringUtils.equals("P", rubriqueClone.getRubrique().getApcf()))
					{
						if (StringUtils.isNotBlank(this.infoSalary.getPmcf()) && (!StringUtils.equals(this.param.monthOfPay, this.infoSalary.getPmcf())))
							go = false;

					}
					if (this.dern_mois_de_conge)
					{
						if (StringUtils.equals("N", rubriqueClone.getRubrique().getCabf()))
							go = false;
					}
					else
					{
//						if (StringUtils.in(rubriqueClone.getRubrique().getCabf(), "U,V"))
						if ("V".equalsIgnoreCase(rubriqueClone.getRubrique().getCabf()))
							go = false;
					}

					if (this.dern_mois_de_conge && StringUtils.equals("J", rubriqueClone.getRubrique().getCabf()) && (this.workTimeFictif.getNbj_cabf() < this.param.getFictiveNombreDeJour()))
						go = false;
				}
			}
			if (go)
			{
				if (rubriqueClone.getRubrique().getAlgo() == 13 || rubriqueClone.getRubrique().getAlgo() == 17 || rubriqueClone.getRubrique().getAlgo() == 20)
					rubriqueClone.loadListOfLoanNumber();

				this.getValeurRubriquePartage().setAmount(0);
				this.getValeurRubriquePartage().setMonhs(0);
				this.getValeurRubriquePartage().setBase(0);
				this.getValeurRubriquePartage().setBasePlafonnee(0);
				this.getValeurRubriquePartage().setRates(0);

				if (!rubriqueClone.champApplicationSalarieToRubrique(this.getParam().getNumeroAjustementActuel()))
				{
					if (rubriqueClone.getRubrique().getEpbul() != null && rubriqueClone.getRubrique().getEpbul() != 0)
						this.insererUneRubriqueClone(rubriqueClone, "1");
					go = false;
				}
			}

			if (go)
			{
				double base = 0;

				Object[] obj = null;

				if ("O".equals(this.infoSalary.getPnet()))
				{
					l = service.find("select mont from ElementSalaireajus where session_id = '" + this.getParam().getsession_id() + "' and crub ='" + rubriqueClone.getRubrique().getComp_id().getCrub() + "'");

					if (l != null && l.size() > 0)
					{
						base = ((BigDecimal) (l.get(0) != null ? l.get(0) : 0)).doubleValue();
						this.getValeurRubriquePartage().setBase(base);
						//
						rubriqueClone.setElementVariableApply(true);
						rubriqueClone.setElementVariableValeur(this.getValeurRubriquePartage().getBase());
					}
					else
					{
						rubriqueClone.calculateBase();
					}
				}
				else
				{
					rubriqueClone.calculateBase();
				}
				
				rubriqueClone.setNumElementVarCourant(0);
				Object[] elementVariableSuiv = null;
				int i = 0;
				int nbre_elvar = 1;
				if (rubriqueClone.getListOfElementVariable() != null || rubriqueClone.getListOfLoanNumber() != null)
				{
					nbre_elvar = rubriqueClone.getListOfElementVariable() != null ? rubriqueClone.getListOfElementVariable().size() : 0;
					if (nbre_elvar == 0)
						nbre_elvar = rubriqueClone.getListOfLoanNumber() != null ? rubriqueClone.getListOfLoanNumber().size() : 0;
					if (nbre_elvar == 0)
						nbre_elvar = 1;
				}
				else
				{
					rubriqueClone.setListOfElementVariable(new ArrayList());
				}

				while (i < nbre_elvar)
				{
					i++;
					this.getValeurRubriquePartage().setBasePlafonnee(this.getValeurRubriquePartage().getBase());

					if ("O".equals(rubriqueClone.getRubrique().getRreg()) && "N".equals(rubriqueClone.getRubrique().getRman()))
					{

						if (rubriqueClone.getRubrique().getAlgo() == 37)
						{
							rubriqueClone.regularisationFrancaise();
						}
						else
						{
							Double nbrePeriodeRegularisationEv = (tempNumber = this.getUtilNomenclatureFictif().getSumOfAmountOfVariableElement(this.getParam().getDossier(), infoSalary.getComp_id().getNmat(),
									param.getMonthOfPay(), this.getParam().getRegularisationRubrique(), param.getNumeroBulletin())) == null ? 0 : tempNumber.doubleValue();
							
							rubriqueClone.calculateRubriqueOfRegularisation1(nbrePeriodeRegularisationEv.intValue());
							param.setNbrePeriodeRegularisationEv(nbrePeriodeRegularisationEv.intValue());
						}
					}
					else
					{
						boolean isalgo161828 = (rubriqueClone.getRubrique().getAlgo() == 1 || rubriqueClone.getRubrique().getAlgo() == 6 || rubriqueClone.getRubrique().getAlgo() == 18 || rubriqueClone.getRubrique()
								.getAlgo() == 28);
						if (("O".equals(this.infoSalary.getPnet()) && "O".equals(rubriqueClone.getRubrique().getAjus())) || isalgo161828)
						{
							if (rubriqueClone.isElementVariableApply() || rubriqueClone.getRubrique().getAlgo() == 1)
							{
								if (!rubriqueClone.applyAlgorithm())
								{
									if (param.isPbWithCalulation())
									{
									}
								}
								
								if(this.dern_mois_de_conge && ("J".equals(rubriqueClone.getRubrique().getCabf()) || "U".equals(rubriqueClone.getRubrique().getCabf()))){
									if("J".equals(rubriqueClone.getRubrique().getCabf()))
										rubriqueClone.calculateBase();
//									rubriqueClone.setBase(this.getValeurRubriquePartage().getBase());
//									System.out.println("RUBRIQUE:"+rubriqueClone.getRubrique().getComp_id().getCrub());
//									System.out.println("BASE:"+rubriqueClone.getBase());
									double nbjrmois = param.getBase30NombreJour();
									if("O".equals(rubriqueClone.getRubrique().getPror()) && this.getValeurRubriquePartage().getBase()!=0 && !"U".equals(rubriqueClone.getRubrique().getCabf())){
										double nbjrabs = this.workTimeFictif.getNbj_cabf();
										double taux = nbjrmois-nbjrabs;
//										rubriqueClone.setRates(taux);
										this.getValeurRubriquePartage().setRates(taux);
//										rubriqueClone.setBasePlafonnee(this.getValeurRubriquePartage().getBase());
										this.getValeurRubriquePartage().setBasePlafonnee(this.getValeurRubriquePartage().getBase());
										if(nbjrmois!=0){
//											rubriqueClone.setAmount((rubriqueClone.getBase()*taux)/nbjrmois);
											rubriqueClone.setElementVariableValeur((this.getValeurRubriquePartage().getBase()*taux)/nbjrmois);
											this.getValeurRubriquePartage().setAmount((this.getValeurRubriquePartage().getBase()*taux)/nbjrmois);
										}
//										System.out.println("TAUX:"+taux);
//										System.out.println("MONTANT:"+rubriqueClone.getAmount());
//										this.insererUneRubriqueClone(rubriqueClone, "1"); 
									} else {
										if(this.getValeurRubriquePartage().getBase()!=0){
//											rubriqueClone.setRates(0);
//											rubriqueClone.setBasePlafonnee(this.getValeurRubriquePartage().getBase());
//											rubriqueClone.setAmount(rubriqueClone.getBase());
											rubriqueClone.setElementVariableValeur(this.getValeurRubriquePartage().getBase());
											this.getValeurRubriquePartage().setRates(nbjrmois);
											this.getValeurRubriquePartage().setAmount(this.getValeurRubriquePartage().getBase());
//											System.out.println("MONTANT:"+rubriqueClone.getAmount());
//											this.insererUneRubriqueClone(rubriqueClone, "1"); 
										}
									}
									rubriqueClone.setElementVariableApply(true);
//									rubriqueClone.setBase(0);
//									rubriqueClone.setRates(0);
//									rubriqueClone.setBasePlafonnee(0);
//									rubriqueClone.setAmount(0);
									
//									this.getValeurRubriquePartage().setBase(0);
//									this.getValeurRubriquePartage().setRates(0);
//									this.getValeurRubriquePartage().setBasePlafonnee(0);
//									this.getValeurRubriquePartage().setAmount(0);
//									continue;
								}
							}
						}
						else
						{
							if (rubriqueClone.isElementVariableApply())
								this.getValeurRubriquePartage().setAmount(rubriqueClone.getElementVariableValeur());
							else
							{
								if (!rubriqueClone.applyAlgorithm())
								{
									if (param.isPbWithCalulation())
									{

									}
								}
							}
						}
					}
					applyTaux = false;

					if (rubriqueClone.getRubrique().getAlgo() != 17 && rubriqueClone.getRubrique().getAlgo() != 20 && rubriqueClone.getRubrique().getAlgo() != 1)
						rubriqueClone.calculatePourcentage();

					if ((!applyTaux) && (!rubriqueClone.isRubriqueEV()))
					{
						if ("O".equals(rubriqueClone.getRubrique().getPror()) && this.getValeurRubriquePartage().getAmount() != 0 && rubriqueClone.getRubrique().getAlgo() != 17
								&& rubriqueClone.getRubrique().getAlgo() != 20 && !"J".equals(rubriqueClone.getRubrique().getCabf()) && !"U".equals(rubriqueClone.getRubrique().getCabf()))
						{
							if (this.getParam().isProrataStandard())
							{
								rubriqueClone.calculateProrataStandard(dateformat);
							}
							else
							{
								rubriqueClone.calculateProrataSpecifiqueRCI();
							}
						}
						else
						{
							if (this.dern_mois_de_conge && "P".equals(rubriqueClone.getRubrique().getCabf()) && this.getValeurRubriquePartage().getAmount() != 0 && rubriqueClone.getRubrique().getAlgo() != 1)
							{
								if (this.getParam().isFictiveCalculusA())
									rubriqueClone.calculateProrataFictif();
								else
								{
									if (rubriqueClone.getRubrique().getAlgo() != 17 && rubriqueClone.getRubrique().getAlgo() != 20)
										rubriqueClone.calculateProrataFictif();
								}
							}
						}
						if (!ClsObjectUtil.isNull(rubriqueClone.getRubrique().getResl()))
						{
							if (rubriqueClone.getRubrique().getAlgo() != 13 && rubriqueClone.getRubrique().getAlgo() != 17 && rubriqueClone.getRubrique().getAlgo() != 20)
							{
								rubriqueClone.compareResult();
							}
						}
					}
					else
						applyTaux = false;

					if ("O".equals(this.infoSalary.getPnet()))
					{
						if ("O".equals(rubriqueClone.getRubrique().getEcar()))
						{
							if (rubriqueClone.getRubrique().getPrbul() == 3 || rubriqueClone.getRubrique().getPrbul() == 4)
							{
								this.salaireAecarterDuNet += this.getValeurRubriquePartage().getAmount();
							}
							else
							{
								this.salaireAecarterDuNet -= this.getValeurRubriquePartage().getAmount();
							}
						}

						if (rubriqueClone.getRubrique().getComp_id().getCrub().equals(this.getParam().getBrutRubrique()))
							this.salaireBrut = this.getValeurRubriquePartage().getAmount();

						if (rubriqueClone.getRubrique().getComp_id().getCrub().equals(this.getParam().getPaieAuNetRubrique()))
							this.salaireNet = this.getValeurRubriquePartage().getAmount();
					}
					boolean insertion = false;
					if (this.getValeurRubriquePartage().getAmount() != 0)
					{
						insertion = true;
					}

					if (!insertion && "O".equals(rubriqueClone.getRubrique().getEdnul()))
					{
						insertion = true;
					}

					if (!insertion && rubriqueClone.getRubrique().getEpbul() != null && (0 != rubriqueClone.getRubrique().getEpbul()))
					{
						insertion = true;
					}

					if (!insertion)
					{
						if (this.param.getListOfRubriqueToCalculate() != null)
							insertion = this.param.getListOfRubriqueToCalculate().contains(rubriqueClone.getRubrique().getComp_id().getCrub());
					}

					if (insertion)
					{
						this.insererUneRubriqueClone(rubriqueClone, "1");
					}
					else
					{
						evparam.setArgu("");
						evparam.setNprt("");
						evparam.setRuba("");
					}

					rubriqueClone.setNumElementVarCourant(1 + rubriqueClone.getNumElementVarCourant());
					if (rubriqueClone.getNumElementVarCourant() < rubriqueClone.getListOfElementVariable().size())
					{
						elementVariableSuiv = (Object[]) rubriqueClone.getListOfElementVariable().get(rubriqueClone.getNumElementVarCourant());
						//
						if (!(rubriqueClone.getRubrique().getAlgo() == 6 || rubriqueClone.getRubrique().getAlgo() == 18 || rubriqueClone.getRubrique().getAlgo() == 28))
						{
							if (!ClsObjectUtil.isNull(elementVariableSuiv[0]))
							{
								this.getValeurRubriquePartage().setBase(new BigDecimal(elementVariableSuiv[0].toString()).doubleValue());
							}
						}
						if (!ClsObjectUtil.isNull(elementVariableSuiv[0]))
							rubriqueClone.setElementVariableValeur(new BigDecimal(elementVariableSuiv[0].toString()).doubleValue());

						rubriqueClone.setElementVariableApply(true);
						evparam.setApplyElementVariable(true);
						if (!ClsObjectUtil.isNull(elementVariableSuiv[0]))
							evparam.setMont(new BigDecimal(elementVariableSuiv[0].toString()).doubleValue());
						if (!ClsObjectUtil.isNull(elementVariableSuiv[1]))
							evparam.setArgu((String) elementVariableSuiv[1]);
						if (!ClsObjectUtil.isNull(elementVariableSuiv[2]))
							evparam.setNprt((String) elementVariableSuiv[2]);
						if (!ClsObjectUtil.isNull(elementVariableSuiv[3]))
							evparam.setRuba((String) elementVariableSuiv[3]);
					}
					else
					{
						if (rubriqueClone.getListOfLoanNumber() != null && !rubriqueClone.getListOfLoanNumber().isEmpty())
						{
							rubriqueClone.setNumElementVarCourant(1);
							rubriqueClone.setElementVariableApply(false);
						}

					}
				}

				if ("O".equals(this.infoSalary.getPnet()) && (rubriqueClone.getRubrique().getComp_id().getCrub().equals(this.getParam().getPaieAuNetRubrique())))
				{
					break;
				}

			}
			this.setOutputtext("\n########################### Fin Calcul de la rubrique" + crub+" ########################");
		}
	}

	/**
	 * Calculate the total days of holidays/absence
	 */
	public void calculateNbreMoisSuppl()
	{
		this.workTimeFictif.calculateNbreMoisSuppl(this);
	}

	/**
	 * Calcul du nombre de periodes de régulation
	 */
	public void calculNbrePeriodeRegulation()
	{
		this.workTimeFictif.calculNbrePeriodeRegulation(this);
	}

	/**
	 * calculer le nombre de jours payés non pris
	 */
	public void calculNbreJourPaidNonPris()
	{
		this.workTimeFictif.calculNbreJourPaidNonPris(this);
	}

	/**
	 * Calcul des conges et absences
	 */
	public void calculCongeAbsence()
	{
		try
		{
			this.workTimeFictif.calculateNbreJourCongeEtAbsence(this);
			ParameterUtil.println("Mois "+param.getMonthOfPay()+" NBJA = "+workTimeFictif.getNbreJourAbsence());
			ParameterUtil.println("Mois "+param.getMonthOfPay()+" NBJC = "+workTimeFictif.getNbreJourConges());
			ParameterUtil.println("Dernier mois de conge du salarie = "+dern_mois_de_conge);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * calculer le nombre de jours supplémentaires
	 */
	public void calculateNbreJourSuppl()
	{
		this.workTimeFictif.calculateNbreJourSuppl(this);
	}

	@SuppressWarnings("unchecked")
	private int getMaxNumLigneEltVar(int nbul)
	{
		String queryString = "select max(nlig) From ElementVariableDetailMois  where cdos=" + "'" + param.getDossier() + "'" + " and  nmat=" + "'" + infoSalary.getComp_id().getNmat() + "' and nbul ="
				+ nbul;
		List result = service.findByQuery(queryString);

		if (result.size() > 0)
			return Integer.valueOf(result.get(0) == null ? "0" : result.get(0).toString());
		else
			return 0;
	}

	/**
	 * generer les rubriques
	 * 
	 * @param pseudoRubrique
	 * @param rubriqueConge
	 * @param argu
	 * @param nprt
	 * @param ruba
	 * @param montant
	 */
	public void generatePseudoEv(String pseudoRubrique, String rubriqueConge, String argu, String nprt, String ruba, double montant)
	{
		
		if(StringUtils.equals(ParameterUtil.formatRubrique, pseudoRubrique) || StringUtils.equals(ParameterUtil.formatRubrique, rubriqueConge))
			return;
		String queryString = "from ElementVariableDetailMois" + " where identreprise ='" + param.getDossier() + "'" + " and nmat ='" + infoSalary.getComp_id().getNmat() + "'" + " and aamm ='" + param.getFictiveMonthOfPay() + "'"
				+ " and nbul =" + param.getNumeroBulletin() + " and rubq ='" + pseudoRubrique + "'" + " and argu ='PSEUDO-EV'";

		List listOfElementVariableDetailMois = null;

		try
		{
			listOfElementVariableDetailMois = service.find(queryString);
			if (listOfElementVariableDetailMois.isEmpty())
			{
				int nlig = this.getMaxNumLigneEltVar(param.getNumeroBulletin()) + 1;
				ElementVariableDetailMois ev = new ElementVariableDetailMois();
				ev.setIdEntreprise(Integer.valueOf(param.getDossier()));
				ev.setNmat(infoSalary.getComp_id().getNmat());
				ev.setNbul(param.getNumeroBulletin());
				ev.setNlig(nlig);
				ev.setRubq(rubriqueConge);
				ev.setArgu(argu);
				ev.setNprt(nprt);
				ev.setRuba(ruba);
				ev.setMont(new BigDecimal(montant));
				ev.setAamm(param.getMonthOfPay());
				ev.setCuti(param.uti);
				//
				//service.save(ev);
				
				//Ajout dans la liste des EV deja chargés du salarié
				Object elvar[] = null;
					// map.put(crub, value)
					elvar = new Object[4];
					elvar[0] = ev.getMont();
					elvar[1] = ev.getArgu();
					elvar[2] = ev.getNprt();
					elvar[3] = ev.getRuba();
					if (!this.listOfEltvar.containsKey(ev.getRubq()))
						this.listOfEltvar.put(ev.getRubq(), new ArrayList<Object[]>());
					this.listOfEltvar.get(ev.getRubq()).add(elvar);
			}
			else
			{
				if (listOfElementVariableDetailMois.size() > 0)
				{
					ElementVariableDetailMois o = new ElementVariableDetailMois();
					o = (ElementVariableDetailMois) listOfElementVariableDetailMois.get(0);
					o.setMont(new BigDecimal(o.getMont().doubleValue() + montant));
					//service.update(o);
					
					//MAJ du montant dans la liste des EV deja chargés du salarié
					Object elvar[] = null;
					// map.put(crub, value)
					elvar = new Object[4];
					elvar[0] = montant;
					elvar[1] = o.getArgu();
					elvar[2] = o.getNprt();
					elvar[3] = o.getRuba();
					if (!this.listOfEltvar.containsKey(o.getRubq()))
						this.listOfEltvar.put(o.getRubq(), new ArrayList<Object[]>());
					this.listOfEltvar.get(o.getRubq()).add(elvar);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * vérifie si le salaire de ce salarié est bloqué ou pas
	 * 
	 * @return true ou false
	 */
	public boolean bulletinBloque()
	{
		// if('O' == this.getParam().getGenfile()) setOutputtext("\n"+"nmat : " +
		// this.getInfoSalary().getComp_id().getNmat());
//		Rhtblq oRhtblq = (Rhtblq) service.get(Rhtblq.class, new RhtblqPK(this.getParam().getDossier(), this.getInfoSalary().getComp_id().getNmat(), 1));
//		if (oRhtblq != null)
//			return true;
		return true;
	}

	/**
	 * bloque ou débloque le bulletin d'un agent. si on veut bloquer et que l'agent existe déjé dans la table de blocage on met le code de blocage é 1. s'il
	 * n'est pas encore dans la table, on le crée.
	 */
	public void bulletinBloqueDebloquer(boolean blocked)
	{
		//Rhtblq oRhtblq = null;
		if (blocked)
		{// on veut bloquer
//			oRhtblq = new Rhtblq();
//			oRhtblq.setComp_id(new RhtblqPK(this.getParam().getDossier(), this.getInfoSalary().getComp_id().getNmat(), 1));
//			service.saveOrUpdate(oRhtblq);
//
//			oRhtblq = (Rhtblq) service.get(Rhtblq.class, new RhtblqPK(this.getParam().getDossier(), this.getInfoSalary().getComp_id().getNmat(), 0));
//			if (oRhtblq != null)
//			{
//				service.delete(oRhtblq);
//			}
		}
		else
		{// on débloque un agent qui est bloqué
//			oRhtblq = (Rhtblq) service.get(Rhtblq.class, new RhtblqPK(this.getParam().getDossier(), this.getInfoSalary().getComp_id().getNmat(), 1));
//			if (oRhtblq != null)
//			{
//				service.delete(oRhtblq);
//
//				oRhtblq = new Rhtblq();
//				oRhtblq.setComp_id(new RhtblqPK(this.getParam().getDossier(), this.getInfoSalary().getComp_id().getNmat(), 0));
//				service.saveOrUpdate(oRhtblq);
//			}
		}
	}

	public void bulletinBloqueDebloquerOld(boolean blocked)
	{
		// if('O' == this.getParam().getGenfile()) setOutputtext("\n"+">>bulletinBloqueDebloquer");
		// if('O' == this.getParam().getGenfile()) setOutputtext("\n"+"nmat : " +
		// this.getInfoSalary().getComp_id().getNmat());
		//Rhtblq oRhtblq = null;
		if (blocked)
		{// on veut bloquer
//			oRhtblq = (Rhtblq) service.get(Rhtblq.class, new RhtblqPK(this.getParam().getDossier(), this.getInfoSalary().getComp_id().getNmat(), 0));
//			if (oRhtblq != null)
//			{
//				oRhtblq.getComp_id().setCode(1);
//				service.update(oRhtblq);
//			}
//			else
//			{
//				oRhtblq = new Rhtblq();
//				oRhtblq.setComp_id(new RhtblqPK(this.getParam().getDossier(), this.getInfoSalary().getComp_id().getNmat(), 1));
//				service.save(oRhtblq);
//			}
		}
		else
		{// on débloque un agent qui est bloqué
//			oRhtblq = (Rhtblq) service.get(Rhtblq.class, new RhtblqPK(this.getParam().getDossier(), this.getInfoSalary().getComp_id().getNmat(), 1));
//			if (oRhtblq != null)
//			{
//				// if('O' == this.getParam().getGenfile()) setOutputtext("\n"+"...oRhtblq != null...............");
//				oRhtblq.getComp_id().setCode(0);
//				service.update(oRhtblq);
//				// service.delete(oRhtblq);
//			}
		}
	}

	/**
	 * recherche la zone libre en fonction du code
	 * 
	 * @param codezone
	 * @return la zone libre ou une chaine vide si rien n'est trouvé
	 */
	public String rechercheZoneLibre(int codezone)
	{
		String zone_libre = "";
//		Rhtzonelibre oZonelibre = (Rhtzonelibre) service.get(Rhtzonelibre.class, new RhtzonelibrePK(param.getDossier(), infoSalary.getComp_id().getNmat(), codezone));
//		if (oZonelibre != null)
//		{
//			Rhpzonelibre oPZonelibre = (Rhpzonelibre) service.get(Rhpzonelibre.class, new RhpzonelibrePK(param.getDossier(), codezone));
//			if (oPZonelibre != null)
//			{
//				if ("L".equalsIgnoreCase(oPZonelibre.getTypezl()))
//					zone_libre = oZonelibre.getVallzl();
//
//				if ("M".equalsIgnoreCase(oPZonelibre.getTypezl()))
//					zone_libre = oZonelibre.getValmzl() == null ? "0" : String.valueOf(oZonelibre.getValmzl());
//
//				if ("T".equalsIgnoreCase(oPZonelibre.getTypezl()))
//					zone_libre = oZonelibre.getValtzl() == null ? "0" : String.valueOf(oZonelibre.getValtzl());
//
//				if ("D".equalsIgnoreCase(oPZonelibre.getTypezl()))
//					zone_libre = oZonelibre.getValdzl() == null ? "0" : String.valueOf(new SimpleDateFormat(this.getParam().appDateFormat).format(oZonelibre.getValdzl()));
//			}
//		}
		return zone_libre;
	}

	public void deletePseudoEVFictif()
	{
		try
		{
			session = service.getSession();

			session.createQuery(
					"delete from ElementVariableDetailMois" + " where identreprise ='" + param.getDossier() + "'" + " and nmat = '" + infoSalary.getComp_id().getNmat() + "'" + " and aamm >= '" + param.getFictiveMonthOfPay() + "'"
							+ " and argu = 'PSEUDO-EV'" + " and nbul = " + param.getNumeroBulletin()).executeUpdate();
//			Iterator<String> iter = this.listOfEltvar.keySet().iterator();
//			while(iter.hasNext())
//			{
//				String key = iter.next();
//				Object elvar[] = null;
//				// map.put(crub, value)
//				elvar = new Object[4];
//				elvar[0] = ev.getMont();
//				elvar[1] = ev.getArgu();
//				elvar[2] = ev.getNprt();
//				elvar[3] = ev.getRuba();
//				if (!this.listOfEltvar.containsKey(ev.getRubq()))
//					this.listOfEltvar.put(ev.getRubq(), new ArrayList<Object[]>());
//				this.listOfEltvar.get(ev.getRubq()).add(elvar);
//				List<Object[]> lst = this.listOfEltvar.get(key);
//				for(Object[] obj : lst)
//					if()
//			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}
		finally
		{
			if (session.isOpen() || session.isConnected())
			{
				service.closeSession(session);
			}
		}
		// END IF;
	}

	public void deletePseudoEV()
	{
		try
		{
			session = service.getSession();

			// IF retroactif THEN
			// DELETE FROM pahevar
			// WHERE cdos = wpdos.cdos
			// AND nmat = wsal01.nmat
			// AND aamm = w_aamm
			// AND param.getNumeroBulletin() = wsd_fcal1.param.getNumeroBulletin()
			// AND argu = 'PSEUDO-EV';
			// ELSE
			// DELETE FROM paevar
			// WHERE cdos = wpdos.cdos
			// AND nmat = wsal01.nmat
			// AND aamm = w_aamm
			// AND param.getNumeroBulletin() = wsd_fcal1.param.getNumeroBulletin()
			// AND argu = 'PSEUDO-EV';
			// END IF;

			if (param.isUseRetroactif())
			{

				session.createQuery(
						"delete from Rhthevar where identreprise ='" + param.getDossier() + "'" + " and nmat = '" + infoSalary.getComp_id().getNmat() + "'" + " and aamm = '" + param.getMyMonthOfPay().getYearAndMonth() + "'"
								+ " and argu = 'PSEUDO-EV'" + " and nbul = " + param.getNumeroBulletin()).executeUpdate();
			}
			else
			{

				session.createQuery(
						"delete from ElementVariableDetailMois" + " where identreprise ='" + param.getDossier() + "'" + " and nmat = '" + infoSalary.getComp_id().getNmat() + "'" + " and aamm = '" + param.getMyMonthOfPay().getYearAndMonth()
								+ "'" + " and argu = 'PSEUDO-EV'" + " and nbul = " + param.getNumeroBulletin()).executeUpdate();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}
		finally
		{
			if (session.isOpen() || session.isConnected())
			{
				service.closeSession(session);
			}
		}
		// END IF;
	}

	public void deleteAvanceConge()
	{
		try
		{
			String query = "DELETE FROM ElementVariableDetailMois WHERE identreprise ='" + param.getDossier() + "' AND aamm = '" + param.getFictiveMonthOfPay()+ "' AND nmat = '" + infoSalary.getComp_id().getNmat() + "' AND rubq = '"
					+ param.getFictiveRubrique() + "'";
//			System.out.println(query);
			session = service.getSession();
			session.createQuery(query).executeUpdate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}
		finally
		{
			if (session.isOpen() || session.isConnected())
			{
				service.closeSession(session);
			}
		}
	}

	public void deleteFictifFictif(boolean addNbul, boolean addMonthOfPay, boolean addRubriqueNet)
	{
		try
		{
			session = service.getSession();

			String query = "delete from CongeFictif" + " where identreprise ='" + param.getDossier() + "'" + " and nmat = '" + infoSalary.getComp_id().getNmat() + "'";

			if (addNbul)
				query += " and nbul =" + param.getNumeroBulletin();

			if (addMonthOfPay)
				query += " and aamm ='" + param.getMonthOfPay() + "'";

			if (addRubriqueNet)
				query += " and rubq = >'" + param.getPaieAuNetRubrique() + "'";

			session.createQuery(query).executeUpdate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}
		finally
		{
			if (session.isOpen() || session.isConnected())
			{
				service.closeSession(session);
			}
		}
		// END IF;
	}
	
	public void deleteSQLQuery(String query)
	{
		try
		{
			session = service.getSession();

			session.createSQLQuery(query).executeUpdate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}
		finally
		{
			if (session.isOpen() || session.isConnected())
			{
				service.closeSession(session);
			}
		}
		// END IF;
	}
	
	public void deleteQueryf(String query)
	{
		try
		{
			session.createQuery(query).executeUpdate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}
		finally
		{
			if (session.isOpen() || session.isConnected())
			{
				service.closeSession(session);
			}
		}
		// END IF;
	}

	/**
	 * cherche une rubrique dans la table des rubriques en fonction de son code une fois la rubrique retrouvée, on va utiliser les valeurs des taux, montant et
	 * de base qui seront utilisées pour le calcule de la base du mois de paie =>
	 * 
	 * @param crub
	 * @return le wrapper de la rubrique, et null si rien n'est trouvé
	 */
	public ClsFictifRubriqueClone findRubriqueCloneFictif(String crub)
	{
		if (crub == null || crub.trim().equals(""))
			return null;
		ClsFictifRubriqueClone rubClone = null;
		// if(this.getParam().isUseRetroactif()){
		// rubClone = ListOfAllRubriqueRetroMap.get(crub);
		// }
		// else{
		// rubClone = ListOfAllRubriqueMap.get(crub);
		// }
		rubClone = param.getListOfAllFictiveRubriqueMap().get(crub);
		return rubClone;
	}

	public void initMapOfConcPro()
	{
		int i = 0;
		mapOfConcPro.put("1", infoSalary.getNiv1());
		mapOfConcPro.put("2", infoSalary.getNiv2());
		mapOfConcPro.put("3", infoSalary.getNiv3());
		mapOfConcPro.put("8", infoSalary.getEqui());
		mapOfConcPro.put("11", infoSalary.getSexe());
		mapOfConcPro.put("21", infoSalary.getNato());
		mapOfConcPro.put("22", infoSalary.getSitf());
		if (infoSalary.getNbcj() != 0)
			i = infoSalary.getNbcj();
		mapOfConcPro.put("23", ClsStringUtil.formatNumber(i, "0000"));
		i = 0;
		if (infoSalary.getNbec() != 0)
			i = infoSalary.getNbec();
		mapOfConcPro.put("24", ClsStringUtil.formatNumber(i, "0000"));
		i = 0;
		if (infoSalary.getNbfe() != 0)
			i = infoSalary.getNbfe();
		mapOfConcPro.put("25", ClsStringUtil.formatNumber(infoSalary.getNbfe(), "0000"));
		i = 0;
		mapOfConcPro.put("27", infoSalary.getModp());
		mapOfConcPro.put("39", infoSalary.getCat());
		mapOfConcPro.put("40", infoSalary.getEch());
		mapOfConcPro.put("43", infoSalary.getGrad());
		mapOfConcPro.put("44", infoSalary.getFonc());
		mapOfConcPro.put("45", infoSalary.getAfec());
		if (infoSalary.getIndi() != 0)
			i = infoSalary.getIndi();
		mapOfConcPro.put("47", ClsStringUtil.formatNumber(infoSalary.getIndi(), "000000"));
		i = 0;
		mapOfConcPro.put("58", infoSalary.getHifo());
		mapOfConcPro.put("59", infoSalary.getZli1());
		mapOfConcPro.put("60", infoSalary.getZli2());
		mapOfConcPro.put("67", infoSalary.getTypc());
		mapOfConcPro.put("70", infoSalary.getRegi());
		mapOfConcPro.put("71", infoSalary.getZres());
		mapOfConcPro.put("72", infoSalary.getDmst());
		mapOfConcPro.put("73", infoSalary.getDmst());
		if (infoSalary.getNpie() != 0)
			i = infoSalary.getNpie();
		mapOfConcPro.put("73", ClsStringUtil.formatNumber(infoSalary.getNpie(), "0000"));
		i = 0;
	}

	public boolean majTableVirements()
	{
		String updateString = "update VirementSalaire" + " set mntdb = 0, mntdvd = 0" + " where identreprise ='" + infoSalary.getComp_id().getCdos() + "'" + " and nmat = '" + infoSalary.getComp_id().getNmat() + "'";
		try
		{
			this.getService().updateFromTable(updateString);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			this.getParam().setError(ClsTreater._getStackTrace(e));
			return false;
		}
		return true;
	}

	/**
	 * => concPro(String cle)
	 * 
	 * @param cle
	 * @return xxx
	 */
	public String concPro(String cle)
	{
		// if('O' == this.getParam().getGenfile()) setOutputtext("\n"+">>concPro");
		if (ClsObjectUtil.isNull(cle))
			return "";
		String codezone = cle.substring(1, 2);
		String char10 = "";

		// BEGIN
		// w_cle := a_cle1;
		// IF w_cle = 1 THEN
		// PA_CALCUL.char10 := PA_CALCUL.wsal01.niv1;
		if ("1".equals(cle))
			char10 = infoSalary.getNiv1();
		// ELSIF w_cle = 2 THEN
		// PA_CALCUL.char10 := PA_CALCUL.wsal01.niv2;
		else if ("2".equals(cle))
			char10 = infoSalary.getNiv2();
		// ELSIF w_cle = 3 THEN
		// PA_CALCUL.char10 := PA_CALCUL.wsal01.niv3;
		else if ("3".equals(cle))
			char10 = infoSalary.getNiv3();
		// ELSIF w_cle = 8 THEN
		// PA_CALCUL.char10 := PA_CALCUL.wsal01.equi;
		else if ("8".equals(cle))
			char10 = infoSalary.getEqui();
		// ELSIF w_cle = 11 THEN
		// PA_CALCUL.char10 := PA_CALCUL.wsal01.sexe;
		else if ("11".equals(cle))
			char10 = infoSalary.getSexe();
		// ELSIF w_cle = 21 THEN
		// PA_CALCUL.char10 := PA_CALCUL.wsal01.nato;
		else if ("21".equals(cle))
			char10 = infoSalary.getNato();
		// ELSIF w_cle = 22 THEN
		// PA_CALCUL.char10 := PA_CALCUL.wsal01.sitf;
		else if ("22".equals(cle))
			char10 = infoSalary.getSitf();
		// ELSIF w_cle = 23 THEN
		// PA_CALCUL.char10 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.nbcj,'9999'));
		else if ("23".equals(cle))
			char10 = ClsStringUtil.formatNumber(infoSalary.getNbcj(), "#");
		// ELSIF w_cle = 24 THEN
		// PA_CALCUL.char10 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.nbec,'9999'));
		else if ("24".equals(cle))
			char10 = ClsStringUtil.formatNumber(infoSalary.getNbec(), "#");
		// ELSIF w_cle = 25 THEN
		// PA_CALCUL.char10 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.nbfe,'9999'));
		else if ("25".equals(cle))
			char10 = ClsStringUtil.formatNumber(infoSalary.getNbfe(), "#");
		// ELSIF w_cle = 27 THEN
		// PA_CALCUL.char10 := PA_CALCUL.wsal01.modp;
		else if ("27".equals(cle))
			char10 = infoSalary.getModp();
		// ELSIF w_cle = 39 THEN
		// PA_CALCUL.char10 := PA_CALCUL.wsal01.cat;
		else if ("39".equals(cle))
			char10 = infoSalary.getCat();
		// ELSIF w_cle = 40 THEN
		// PA_CALCUL.char10 := PA_CALCUL.wsal01.ech;
		else if ("40".equals(cle))
			char10 = infoSalary.getEch();
		// ELSIF w_cle = 43 THEN
		// PA_CALCUL.char10 := PA_CALCUL.wsal01.grad;
		else if ("43".equals(cle))
			char10 = infoSalary.getGrad();
		// ELSIF w_cle = 44 THEN
		// PA_CALCUL.char10 := PA_CALCUL.wsal01.fonc;
		else if ("44".equals(cle))
			char10 = infoSalary.getFonc();
		// ELSIF w_cle = 45 THEN
		// PA_CALCUL.char10 := PA_CALCUL.wsal01.afec;
		else if ("45".equals(cle))
			char10 = infoSalary.getAfec();
		// ELSIF w_cle = 47 THEN
		// PA_CALCUL.char10 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.indi,'999999'));
		else if ("47".equals(cle))
			char10 = ClsStringUtil.formatNumber(infoSalary.getIndi(), "#");
		// ELSIF w_cle = 58 THEN
		// PA_CALCUL.char10 := PA_CALCUL.wsal01.hifo;
		else if ("58".equals(cle))
			char10 = infoSalary.getHifo();
		// ELSIF w_cle = 59 THEN
		// PA_CALCUL.char10 := PA_CALCUL.wsal01.zli1;
		else if ("59".equals(cle))
			char10 = infoSalary.getZli1();
		// ELSIF w_cle = 60 THEN
		// PA_CALCUL.char10 := PA_CALCUL.wsal01.zli2;
		else if ("60".equals(cle))
			char10 = infoSalary.getZli2();
		// ELSIF w_cle = 67 THEN
		// PA_CALCUL.char10 := PA_CALCUL.wsal01.typc;
		else if ("67".equals(cle))
			char10 = infoSalary.getTypc();
		// ELSIF w_cle = 70 THEN
		// PA_CALCUL.char10 := PA_CALCUL.wsal01.regi;
		else if ("70".equals(cle))
			char10 = infoSalary.getRegi();
		// ELSIF w_cle = 71 THEN
		// PA_CALCUL.char10 := PA_CALCUL.wsal01.zres;
		else if ("71".equals(cle))
			char10 = infoSalary.getZres();
		// ELSIF w_cle = 72 THEN
		// PA_CALCUL.char10 := PA_CALCUL.wsal01.dmst;
		else if ("72".equals(cle))
			char10 = infoSalary.getDmst();
		// ELSIF w_cle = 73 THEN
		// PA_CALCUL.char10 := LTRIM(TO_CHAR(PA_CALCUL.wsal01.npie,'9999'));
		else if ("73".equals(cle))
			char10 = ClsStringUtil.formatNumber(infoSalary.getNpie(), "#");
		if (cle.compareTo("801") >= 0 && cle.compareTo("899") <= 0)
			char10 = rechercheZoneLibre(Integer.valueOf(codezone));
		// else
		// char10 = this.mapOfConcPro.get(cle);
		//
		return char10;
	}

	/**
	 * permet d'insérer une rubrique dans pacalc
	 * 
	 * @param rubriqueClone
	 * @param trtb
	 */
	private void insererUneRubriqueClone(ClsFictifRubriqueClone rubriqueClone, String trtb)
	{
		rubriqueClone.insertIntoSalariesFile(this.getInfoSalary().getClas(), evparam.getNprt(), evparam.getRuba(), trtb);
		evparam.setArgu("");
		evparam.setNprt("");
		evparam.setRuba("");
	}

	/**
	 * PAIE AU NET : chargement montant rubrique saisie en net et soumises é l'ajustement
	 */
	public void chargementRubriqueSaisiesEnNetAjus()
	{
		if ("O".equals(infoSalary.getPnet()))
		{
			this.getParam().setCalcul("O");
			this.nbiter = 0;
			this.getParam().setNumeroAjustementActuel(0);

			if (this.getParam().isRubriqueAdjustExist())
				chargementMontantDesRubriquesAdjustment();

			if (this.getParam().isRubriqueNetExist())
				chargementMontantDesRubriquesNettes();
		}
	}

	/**
	 * => comp_net Comparaison du salaire net calcule avec le salaire avec le net théorique La variable w_calcul est mise a 'N' avant de lancer cette fonction
	 */
	public void comparerAvecNet()
	{
		nbiter++;

		this.salaireNet += this.salaireAecarterDuNet;

		this.getParam().setBouclage("N");

		if (this.salaireNet == this.salaireIterImpaire || this.salaireNet == this.salaireIterPaire)
			this.getParam().setBouclage("O");

		this.getParam().setEcartSalaireNet(this.salaireNetATrouver - this.salaireNet);

		if (this.getParam().getEcartSalaireNet() == 0)
			return;

		this.param.setEcartSalaireNet(Math.abs(this.param.getEcartSalaireNet()));

		if (nbiter > this.getParam().getPaieAuNetMaxIteration())
			return;

		if (this.getParam().getEcartSalaireNet() <= this.getParam().getPaieAuNetEcartMaximum())
		{

			this.getParam().setEcartSalaireNet(this.salaireNetATrouver - this.salaireNet);

			ClsFictifRubriqueClone rubriqueAffectationEcart = null;

			try
			{
				if (!ParameterUtil.formatRubrique.equals(param.getPaieAuNetAffectationEcartRubrique()))
				{
					rubriqueAffectationEcart = this.findRubriqueCloneFictif(param.getPaieAuNetAffectationEcartRubrique());
					if (param.getPaieAuNetAffectationEcartRubrique().compareToIgnoreCase(param.getPaieAuNetRubrique()) < 0)
					{
						rubriqueAffectationEcart.setAmount(rubriqueAffectationEcart.getAmount() - this.getParam().getEcartSalaireNet());
						rubriqueAffectationEcart.setBase(rubriqueAffectationEcart.getAmount());
						rubriqueAffectationEcart.setBasePlafonnee(rubriqueAffectationEcart.getAmount());

						List l = null;
						String queryString1 = "select nmat from CongeFictif " + " where identreprise ='" + param.getDossier() + "'" + " and aamm = '" + param.getMonthOfPay() + "'" + " and nmat = '"
								+ infoSalary.getComp_id().getNmat() + "'" + " and nbul = " + param.getNumeroBulletin() + " and rubq = '" + param.getPaieAuNetAffectationEcartRubrique() + "'";
						//
						l = service.find(queryString1);
						if (l != null && l.size() > 0)
						{
							String queryStringUpdate = "update CongeFictif set mont = " + rubriqueAffectationEcart.getAmount() + " where identreprise ='" + param.getDossier() + "'" + " and aamm = '"
									+ param.getMonthOfPay() + "'" + " and nmat = '" + infoSalary.getComp_id().getNmat() + "'" + " and nbul = " + param.getNumeroBulletin() + " and rubq = '"
									+ param.getPaieAuNetAffectationEcartRubrique() + "'";

							session = service.getSession();
							session.createQuery(queryStringUpdate).executeUpdate();
							service.closeSession(session);
						}
						else
						{
							this.getParam().setEcartSalaireNet(-1 * this.getParam().getEcartSalaireNet());
							rubriqueAffectationEcart.setAmount(this.getParam().getEcartSalaireNet());
							rubriqueAffectationEcart.setBase(this.getParam().getEcartSalaireNet());
							rubriqueAffectationEcart.setBasePlafonnee(this.getParam().getEcartSalaireNet());

							this.incrementerDerniereLigne();
							CongeFictif oCongeFictif = new CongeFictif();
							oCongeFictif.setAamm(param.getMonthOfPay());
							oCongeFictif.setIdEntreprise(Integer.valueOf(this.getParam().getDossier()));
							oCongeFictif.setNbul(param.getNumeroBulletin());
							oCongeFictif.setNlig(this.derniereLigne);
							oCongeFictif.setNmat(this.getInfoSalary().getComp_id().getNmat());
							oCongeFictif.setRubq(this.getParam().getPaieAuNetAffectationEcartRubrique());
							oCongeFictif.setArgu(evparam.getArgu());
							oCongeFictif.setBasc(new BigDecimal(this.getParam().getEcartSalaireNet()));
							oCongeFictif.setBasp(new BigDecimal(this.getParam().getEcartSalaireNet()));
							oCongeFictif.setMont(new BigDecimal(this.getParam().getEcartSalaireNet()));
							oCongeFictif.setNprt(evparam.getNprt());
							oCongeFictif.setRuba(evparam.getRuba());
							oCongeFictif.setTaux(new BigDecimal(0));
							oCongeFictif.setTrtb("1");
							//
							service.save(oCongeFictif);
						}
					}
					else
					{

						rubriqueAffectationEcart.setAmount(this.getParam().getEcartSalaireNet());
						rubriqueAffectationEcart.setBase(this.getParam().getEcartSalaireNet());
						rubriqueAffectationEcart.setBasePlafonnee(this.getParam().getEcartSalaireNet());

						this.incrementerDerniereLigne();
						CongeFictif oCongeFictif = new CongeFictif();

						oCongeFictif.setAamm(param.getMonthOfPay());
						oCongeFictif.setIdEntreprise(Integer.valueOf(this.getParam().getDossier()));
						oCongeFictif.setNbul(param.getNumeroBulletin());
						oCongeFictif.setNlig(this.derniereLigne);
						oCongeFictif.setNmat(this.getInfoSalary().getComp_id().getNmat());
						oCongeFictif.setRubq(this.getParam().getPaieAuNetAffectationEcartRubrique());
						oCongeFictif.setArgu(evparam.getArgu());
						oCongeFictif.setBasc(new BigDecimal(this.getParam().getEcartSalaireNet()));
						oCongeFictif.setBasp(new BigDecimal(this.getParam().getEcartSalaireNet()));
						oCongeFictif.setMont(new BigDecimal(this.getParam().getEcartSalaireNet()));
						oCongeFictif.setNprt(evparam.getNprt());
						oCongeFictif.setRuba(evparam.getRuba());
						oCongeFictif.setTaux(new BigDecimal(0));
						oCongeFictif.setTrtb("1");
						//
						service.save(oCongeFictif);
					}

					ClsFictifRubriqueClone rubriqueNet = this.findRubriqueCloneFictif(param.getPaieAuNetRubrique());
					rubriqueNet.setAmount(this.salaireNetATrouver - this.salaireAecarterDuNet);
					rubriqueNet.setBase(this.salaireNetATrouver - this.salaireAecarterDuNet);
					rubriqueNet.setBasePlafonnee(this.salaireNetATrouver - this.salaireAecarterDuNet);

					String queryStringUpdate = "update CongeFictif " + " set mont = " + rubriqueNet.getAmount() + " ,basc = " + rubriqueNet.getBase() + " ,basp = " + rubriqueNet.getBasePlafonnee() + " where identreprise ='"
							+ param.getDossier() + "'" + " and aamm = '" + param.getMonthOfPay() + "'" + " and nmat = '" + infoSalary.getComp_id().getNmat() + "'" + " and nbul = "
							+ param.getNumeroBulletin() + " and rubq = '" + param.getPaieAuNetRubrique() + "'";
					//

					this.setOutputtext("\n" + "Requete de Mise é jour de la rubrique du net " + param.getPaieAuNetRubrique() + " " + queryStringUpdate);
					session = service.getSession();
					session.createQuery(queryStringUpdate).executeUpdate();
					service.closeSession(session);
				}
			}
			catch (Exception e)
			{
				// logger
				e.printStackTrace();
			}
		}

		this.getParam().setCalcul("O");

		if (nbiter % 2 == 1)
			this.salaireIterImpaire = this.salaireNet;
		else
			this.salaireIterPaire = this.salaireNet;

		if (this.salaireNetATrouver > 0)
			this.getParam().setPourcentageEcartNet((this.salaireNetATrouver - this.salaireNet) / this.salaireNetATrouver);

		this.getParam().setEcartSalaireNet(this.salaireNetATrouver - this.salaireNet);

		List l = service.find("select sum(mont) from ElementSalaireajus" + " where ajnu = " + this.getParam().getNumeroAjustementActuel() + " and session_id = '" + this.getParam().getsession_id() + "'");
		double totalAjnu = 0;
		if (l != null && l.size() > 0 && !ClsObjectUtil.isNull(l.get(0)))
			totalAjnu = ((BigDecimal) l.get(0)).doubleValue();

		if (nbiter == 1)
			sensPositif = (this.getParam().getEcartSalaireNet() > 0) ? true : false;

		double majo = 0;
		if (sensPositif)
		{
			if (this.getParam().getEcartSalaireNet() > 0)
				majo = this.getParam().getTableOfAdjustmentValues()[this.compteurValeurAjus];
			else
			{
				majo = -1 * this.getParam().getTableOfAdjustmentValues()[this.compteurValeurAjus];
				this.compteurValeurAjus = ("O".equals(this.getParam().getBouclage())) ? this.compteurValeurAjus - 2 : this.compteurValeurAjus - 1;
				if (this.compteurValeurAjus <= 0)
					this.compteurValeurAjus = 1;
				if (this.compteurValeurAjus > this.maxNbreValeurAjus)
					this.compteurValeurAjus = this.maxNbreValeurAjus;
				ParameterUtil.println("Temp majo 2 for " + this.compteurValeurAjus + " = " + this.getParam().getTableOfAdjustmentValues()[this.compteurValeurAjus]);
				majo = majo + this.getParam().getTableOfAdjustmentValues()[this.compteurValeurAjus];
			}
		}
		else
		{
			if (this.getParam().getEcartSalaireNet() < 0)
				majo = -1 * this.getParam().getTableOfAdjustmentValues()[this.compteurValeurAjus];
			else
			{
				majo = this.getParam().getTableOfAdjustmentValues()[this.compteurValeurAjus];
				this.compteurValeurAjus = ("O".equals(this.getParam().getBouclage())) ? this.compteurValeurAjus - 2 : this.compteurValeurAjus - 1;
				if (this.compteurValeurAjus <= 0)
					this.compteurValeurAjus = 1;
				if (this.compteurValeurAjus > this.maxNbreValeurAjus)
					this.compteurValeurAjus = this.maxNbreValeurAjus;
				majo = majo - this.getParam().getTableOfAdjustmentValues()[this.compteurValeurAjus];
			}
		}

		double majoReste = majo;

		String queryString = "from ElementSalaireajus" + " where session_id = " + param.getsession_id() + " and ajnu is not null" + " and ajnu = " + this.getParam().getNumeroAjustementActuel();

		l = service.find(queryString);
		double pourcentageMajo = 0;
		double monMajo = 0;
		ElementSalaireAjus ruba = null;
		for (Object obj : l)
		{
			ruba = (ElementSalaireAjus) obj;
			if (totalAjnu != 0)
			{
				pourcentageMajo = Math.abs(ruba.getMont().doubleValue() / totalAjnu);
				monMajo = Math.round(majo * pourcentageMajo);
			}
			else
			{
				monMajo = majo;
			}
			try
			{
				service.updateFromTable("update ElementSalaireajus set mont = mont + " + monMajo + " where crub =" + ruba.getCrub() + " and ajnu = " + ruba.getAjnu() + " and session_id = " + ruba.getSessionId());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			majoReste -= monMajo;
			if (majoReste <= 0)
				break;
		}
	}

	public void buildNumeroPretMap()
	{
		listNumeroPretMap = new HashMap<String, List<Object>>();
		//
		// CURSOR curs_pretx IS SELECT nprt FROM paprent
		// WHERE paprent.cdos = wpdos.cdos
		// AND paprent.nmat = wsal01.nmat
		// AND paprent.crub = t_rub.crub
		// -- AND paprent.per1 <= w_aamm
		// AND paprent.pact = 'O'
		// AND paprent.etpr = 'D'
		// AND paprent.resr != 0;
		String complexQuery = "select crub, nprt from PretExterneEntete" + " where identreprise ='" + this.getParam().getDossier() + "'" + " and nmat = '" + this.getInfoSalary().getComp_id().getNmat() + "'" +
		// " and per1 <= '" +
				// salary.getMyMonthOfPay().getDateS(ParameterUtil.SESSION_FORMAT_DATE)
				// + "'" +
				" and pact = 'O'" + " and etpr = 'D'" + " and resr != 0 order by crub";
		List l = this.getService().find(complexQuery);
		//
		for (Object object : l)
		{
			// map.put(crub, value)
			if (!listNumeroPretMap.containsKey((String) ((Object[]) object)[0]))
				listNumeroPretMap.put((String) ((Object[]) object)[0], new ArrayList<Object>());
			listNumeroPretMap.get((String) ((Object[]) object)[0]).add(((Object[]) object)[1]);
		}
	}

	/**
	 * construit un map qui contient les numéros de préts d'un agent
	 */
	Map<String, Object[]> listSpecifiqueCumul99Map1 = null;

	Map<String, Object> listSpecifiqueCumul99Map2 = null;

	Map<String, Object> listSpecifiqueCumul99Map3 = null;

	Map<String, Object[]> listSpecifiqueCumul99Map4 = null;

	Map<String, Object> listSpecifiqueCumul99Map5 = null;

	public void buildSpecifiqueCumul99Map()
	{
		listSpecifiqueCumul99Map1 = new HashMap<String, Object[]>();
		listSpecifiqueCumul99Map2 = new HashMap<String, Object>();
		listSpecifiqueCumul99Map3 = new HashMap<String, Object>();

		listSpecifiqueCumul99Map4 = new HashMap<String, Object[]>();
		listSpecifiqueCumul99Map5 = new HashMap<String, Object>();

		int fin_periode = 0;
		int yearAndMonthCumul = this.getOFictifPeriod().getAammExercice();
		int cum99 = yearAndMonthCumul / 100;
		cum99 = cum99 * 100 + 99;
		if (param.getMyMonthOfPay().getYearAndMonthInt() < this.getOFictifPeriod().getFinPeriode())
			fin_periode = param.getMyMonthOfPay().getYearAndMonthInt();
		else
		{
			fin_periode = this.getOFictifPeriod().getFinPeriode();
		}
		//
		// IF retroactif THEN
		// -- on exclu la periode de traitement (w_aamm)
		// IF aamm_cum < w_aamm AND fin_periode = w_aamm THEN
		// fin_periode := fin_periode - 1;
		// END IF;
		// END IF;
		if (this.getParam().isUseRetroactif())
		{
			if ((yearAndMonthCumul < param.getMyMonthOfPay().getYearAndMonthInt()) && (fin_periode == param.getMyMonthOfPay().getYearAndMonthInt()))
				fin_periode--;
		}
		//
		// SELECT SUM(basc), SUM(mont) INTO cum_basc, cum_coti
		// FROM prcumu
		// WHERE cdos = wpdos.cdos
		// AND nmat = wsal01.nmat
		// AND aamm >= c_aamm_cum
		// AND aamm <= c_fin_periode
		// AND aamm != c_cum99
		// AND rubq = t_rub.rcon
		// AND param.getNumeroBulletin() != 0;
		String yearAndMonthCumulS = ClsStringUtil.formatNumber(yearAndMonthCumul, "000000");
		String finPeriodeS = ClsStringUtil.formatNumber(fin_periode, "000000");
		String cum99S = ClsStringUtil.formatNumber(cum99, "000000");
		String queryString = "select rubq, sum(basc), sum(mont) from CumulPaie" + " where identreprise ='" + this.getParam().getDossier() + "'" + " and nmat = '" + this.getInfoSalary().getComp_id().getNmat() + "'"
				+ " and aamm >= '" + yearAndMonthCumulS + "'" + " and aamm <= '" + finPeriodeS + "'" + " and aamm != '" + cum99S + "'" +
				// " and rubq = '" + rubrique.getRcon() + "'" +
				" and nbul != 0" + " group by  rubq";

		setOutputtext("\n" + "..Query for map 1: " + queryString);
		String queryStringRetro = "select rubq, sum(basc), sum(mont) from Rhtprcumu" + " where identreprise ='" + this.getParam().getDossier() + "'" + " and nmat = '" + this.getInfoSalary().getComp_id().getNmat() + "'"
				+ " and aamm >= '" + yearAndMonthCumulS + "'" + " and aamm <= '" + finPeriodeS + "'" + " and aamm != '" + cum99S + "'" +
				// " and rubq = '" + rubrique.getRcon() + "'" +
				" and nbul != 0" + " group by  rubq";
		List l = (this.getParam().isUseRetroactif()) ? this.getService().find(queryStringRetro) : this.getService().find(queryString);
		//
		Object[] array = null;
		//
		for (Object object : l)
		{
			// map.put(crub, value)
			array = new Object[2];
			array[0] = ((Object[]) object)[1];
			array[1] = ((Object[]) object)[2];
			listSpecifiqueCumul99Map1.put((String) ((Object[]) object)[0], array);
		}
		//

		queryString = "select rubq, sum(mont) from CumulPaie" + " where identreprise ='" + this.getParam().getDossier() + "'" + " and nmat = '" + this.getInfoSalary().getComp_id().getNmat() + "'" + " and aamm >= '"
				+ yearAndMonthCumulS + "'" + " and aamm <= '" + finPeriodeS + "'" + " and aamm != '" + cum99S + "'" +
				// " and rubq = '" + this.getComp_id().getCrub() + "'" +
				" and nbul != 0" + " group by  rubq";

		setOutputtext("\n" + "..Query for map 2: " + queryString);
		queryStringRetro = "select rubq, sum(mont) from Rhtprcumu" + " where identreprise ='" + this.getParam().getDossier() + "'" + " and nmat = '" + this.getInfoSalary().getComp_id().getNmat() + "'"
				+ " and aamm >= '" + yearAndMonthCumulS + "'" + " and aamm <= '" + finPeriodeS + "'" + " and aamm != '" + cum99S + "'" +
				// " and rubq = '" + rubrique.getComp_id().getCrub() +
				// "'" +
				" and nbul != 0" + " group by  rubq";
		l = (this.getParam().isUseRetroactif()) ? this.getService().find(queryStringRetro) : this.getService().find(queryString);
		//
		for (Object object : l)
		{
			// map.put(crub, value)
			listSpecifiqueCumul99Map2.put((String) ((Object[]) object)[0], ((Object[]) object)[1]);
		}
		//
		queryString = "select rubq, count(*) from CumulPaie" + " where identreprise ='" + this.getParam().getDossier() + "'" + " and nmat = '" + this.getInfoSalary().getComp_id().getNmat() + "'" + " and aamm >= '"
				+ yearAndMonthCumulS + "'" + " and aamm < '" + finPeriodeS + "'" + " and aamm != '" + cum99S + "'" +
				// " and rubq = '" +
				// this.getParam().getBrutRubrique() + "'" +
				" and nbul = 9" + " group by  rubq";

		setOutputtext("\n" + "..Query for map 3: " + queryString);
		queryStringRetro = "select rubq, count(*) from Rhtprcumu" + " where identreprise ='" + this.getParam().getDossier() + "'" + " and nmat = '" + this.getInfoSalary().getComp_id().getNmat() + "'" + " and aamm >= '"
				+ yearAndMonthCumulS + "'" + " and aamm < '" + finPeriodeS + "'" + " and aamm != '" + cum99S + "'" +
				// " and rubq = '" +
				// this.getParam().getBrutRubrique() + "'" +
				" and nbul = 9" + " group by  rubq";
		l = (this.getParam().isUseRetroactif()) ? this.getService().find(queryStringRetro) : this.getService().find(queryString);
		//
		for (Object object : l)
		{
			// map.put(crub, value)
			listSpecifiqueCumul99Map3.put((String) ((Object[]) object)[0], ((Object[]) object)[1]);
		}

		queryString = "select rubq, sum(basc), sum(mont) from CumulPaie" + " where identreprise ='" + this.getParam().getDossier() + "'" + " and nmat = '" + this.getInfoSalary().getComp_id().getNmat() + "'"
				+ " and aamm >= '" + yearAndMonthCumulS + "'" + " and aamm <= '" + param.getMonthOfPay() + "'" + " and aamm != '" + cum99S + "'" +
				// " and rubq = '" + rubrique.getRcon() + "'" +
				" and nbul != 0" + " group by  rubq";

		setOutputtext("\n" + "..Query for map 4: " + queryString);
		l = this.getService().find(queryString);
		//
		array = null;
		//
		for (Object object : l)
		{
			// map.put(crub, value)
			array = new Object[2];
			array[0] = ((Object[]) object)[1];
			array[1] = ((Object[]) object)[2];
			listSpecifiqueCumul99Map4.put((String) ((Object[]) object)[0], array);
		}
		//

		queryString = "select rubq, sum(mont) from CumulPaie" + " where identreprise ='" + this.getParam().getDossier() + "'" + " and nmat = '" + this.getInfoSalary().getComp_id().getNmat() + "'" + " and aamm >= '"
				+ yearAndMonthCumulS + "'" + " and aamm <= '" + param.getMonthOfPay()  + "'" + " and aamm != '" + cum99S + "'" +
				// " and rubq = '" + this.getComp_id().getCrub() + "'" +
				" and nbul != 0" + " group by  rubq";

		l = this.getService().find(queryString);
		//
		for (Object object : l)
		{
			// map.put(crub, value)
			listSpecifiqueCumul99Map5.put((String) ((Object[]) object)[0], ((Object[]) object)[1]);
		}
		//
	}

	public void incrementerDerniereLigne()
	{
		this.derniereLigne++;
	}

	public double getTotal_nap()
	{
		return total_nap;
	}

	public void setTotal_nap(double total_nap)
	{
		this.total_nap = total_nap;
	}

	public ClsFictifParameterOfPay getParam()
	{
		return param;
	}

	public void setParam(ClsFictifParameterOfPay param)
	{
		this.param = param;
	}

	public ClsFictifNomenclatureUtil getUtilNomenclatureFictif()
	{
		return utilNomenclatureFictif;
	}

	public void setUtilNomenclatureFictif(ClsFictifNomenclatureUtil utilNomenclatureFictif)
	{
		this.utilNomenclatureFictif = utilNomenclatureFictif;
	}

	public ClsFictifSalaryWorkTime getWorkTimeFictif()
	{
		return workTimeFictif;
	}

	public void setWorkTimeFictif(ClsFictifSalaryWorkTime workTimeFictif)
	{
		this.workTimeFictif = workTimeFictif;
	}

	public int getDerniereLigne()
	{
		return derniereLigne;
	}

	public void setDerniereLigne(int derniereLigne)
	{
		this.derniereLigne = derniereLigne;
	}

	public synchronized Map<String, Object> getListSpecifiqueCumul99Map3()
	{
		return listSpecifiqueCumul99Map3;
	}

	public synchronized void setListSpecifiqueCumul99Map3(Map<String, Object> listSpecifiqueCumul99Map3)
	{
		this.listSpecifiqueCumul99Map3 = listSpecifiqueCumul99Map3;
	}

	public synchronized Map<String, Object[]> getListSpecifiqueCumul99Map1()
	{
		return listSpecifiqueCumul99Map1;
	}

	public synchronized void setListSpecifiqueCumul99Map1(Map<String, Object[]> listSpecifiqueCumul99Map1)
	{
		this.listSpecifiqueCumul99Map1 = listSpecifiqueCumul99Map1;
	}

	public synchronized Map<String, Object> getListSpecifiqueCumul99Map2()
	{
		return listSpecifiqueCumul99Map2;
	}

	public synchronized void setListSpecifiqueCumul99Map2(Map<String, Object> listSpecifiqueCumul99Map2)
	{
		this.listSpecifiqueCumul99Map2 = listSpecifiqueCumul99Map2;
	}

	public String getOutputtext()
	{
		return outputtext;
	}

	public void setOutputtext(String outputtext)
	{
		if ('O' == this.param.getGenfile())
		{
			if ("O".equals(this.getInfoSalary().getPnet()))
			{
				// try
				// {
				// if (outputFile == null)
				// outputFile = new File(this.parameter.getGenfilefolder() + "\\" + this.getInfoSalary().getComp_id().getNmat() + ".txt");
				//
				// List<String> lst = FileUtils.readLines(outputFile);
				//
				// lst.add(outputtext.replace("\n", ""));
				//
				// FileUtils.writeLines(outputFile, lst);
				// }
				// catch (IOException e)
				// {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				// ParameterUtil.println(outputtext);
			}
			else
			{
				this.outputtext += outputtext;
			}
		}
	}

	public synchronized Map<String, List<Object>> getListLignePretMap()
	{
		return listLignePretMap;
	}

	public synchronized void setListLignePretMap(Map<String, List<Object>> listLignePretMap)
	{
		this.listLignePretMap = listLignePretMap;
	}

	public synchronized Map<String, List<Object>> getListNumeroPretMap()
	{
		return listNumeroPretMap;
	}

	public synchronized void setListNumeroPretMap(Map<String, List<Object>> listNumeroPretMap)
	{
		this.listNumeroPretMap = listNumeroPretMap;
	}

	public synchronized Map<String, Object[]> getCumul1()
	{
		return cumul1;
	}

	public synchronized void setCumul1(Map<String, Object[]> cumul1)
	{
		this.cumul1 = cumul1;
	}

	public synchronized Map<String, Object[]> getCumul2()
	{
		return cumul2;
	}

	public synchronized void setCumul2(Map<String, Object[]> cumul2)
	{
		this.cumul2 = cumul2;
	}

	public synchronized Map<String, Object[]> getCumul3()
	{
		return cumul3;
	}

	public synchronized void setCumul3(Map<String, Object[]> cumul3)
	{
		this.cumul3 = cumul3;
	}

	public synchronized Map<String, Object[]> getCumul4()
	{
		return cumul4;
	}

	public synchronized void setCumul4(Map<String, Object[]> cumul4)
	{
		this.cumul4 = cumul4;
	}

	public synchronized Map<String, List<Object>> getListOfEltfix()
	{
		return listOfEltfix;
	}

	public synchronized void setListOfEltfix(Map<String, List<Object>> listOfEltfix)
	{
		this.listOfEltfix = listOfEltfix;
	}

	public synchronized Map<String, List<Object[]>> getListOfEltvar()
	{
		return listOfEltvar;
	}

	public synchronized void setListOfEltvar(Map<String, List<Object[]>> listOfEltvar)
	{
		this.listOfEltvar = listOfEltvar;
	}

	public boolean isApplyTaux()
	{
		return applyTaux;
	}

	public void setApplyTaux(boolean applyTaux)
	{
		this.applyTaux = applyTaux;
	}

	public int getAgeOfAgent()
	{
		return ageOfAgent;
	}

	public void setAgeOfAgent(int ageOfAgent)
	{
		this.ageOfAgent = ageOfAgent;
	}

	public int getCompteurValeurAjus()
	{
		return compteurValeurAjus;
	}

	public void setCompteurValeurAjus(int compteurValeurAjus)
	{
		this.compteurValeurAjus = compteurValeurAjus;
	}

	public Map<String, String> getMapOfConcPro()
	{
		return mapOfConcPro;
	}

	public void setMapOfConcPro(Map<String, String> mapOfConcPro)
	{
		this.mapOfConcPro = mapOfConcPro;
	}

	public int getMaxNbreValeurAjus()
	{
		return maxNbreValeurAjus;
	}

	public void setMaxNbreValeurAjus(int maxNbreValeurAjus)
	{
		this.maxNbreValeurAjus = maxNbreValeurAjus;
	}

	public int getNbiter()
	{
		return nbiter;
	}

	public void setNbiter(int nbiter)
	{
		this.nbiter = nbiter;
	}

	public int getNumeroBulletinVerified()
	{
		return numeroBulletinVerified;
	}

	public void setNumeroBulletinVerified(int numeroBulletinVerified)
	{
		this.numeroBulletinVerified = numeroBulletinVerified;
	}

	public double getNbreJourPourEnfant()
	{
		return nbreJourPourEnfant;
	}

	public void setNbreJourPourEnfant(double nbreJourPourEnfant)
	{
		this.nbreJourPourEnfant = nbreJourPourEnfant;
	}

	public ClsParamElementVariable getEvparam()
	{
		return evparam;
	}

	public void setEvparam(ClsParamElementVariable evparam)
	{
		this.evparam = evparam;
	}

	public boolean isContinuer()
	{
		return continuer;
	}

	public void setContinuer(boolean continuer)
	{
		this.continuer = continuer;
	}

	public ClsEnumeration.EnEnterprise getEntreprise()
	{
		return entreprise;
	}

	public void setEntreprise(ClsEnumeration.EnEnterprise entreprise)
	{
		this.entreprise = entreprise;
	}

	public void setExpatrie(boolean expatrie)
	{
		this.expatrie = expatrie;
	}

	public double getSalaireIterPaire()
	{
		return salaireIterPaire;
	}

	public void setSalaireIterPaire(double salaireIterPaire)
	{
		this.salaireIterPaire = salaireIterPaire;
	}

	public double getSalaireIterImpaire()
	{
		return salaireIterImpaire;
	}

	public void setSalaireIterImpaire(double salaireIterImpaire)
	{
		this.salaireIterImpaire = salaireIterImpaire;
	}

	public boolean isExpatrie()
	{
		return expatrie;
	}

	public double getSalaireNetATrouver()
	{
		return salaireNetATrouver;
	}

	public void setSalaireNetATrouver(double salaireNetACalculer)
	{
		this.salaireNetATrouver = salaireNetACalculer;
	}

	public boolean isTable91LabelNotEmpty()
	{
		return table91LabelNotEmpty;
	}

	public void setTable91LabelNotEmpty(boolean table91LabelNotEmpty)
	{
		this.table91LabelNotEmpty = table91LabelNotEmpty;
	}

	public double getSalaireBrut()
	{
		return salaireBrut;
	}

	public void setSalaireBrut(double salaireBrut)
	{
		this.salaireBrut = salaireBrut;
	}

	public double getSalaireNet()
	{
		return salaireNet;
	}

	public void setSalaireNet(double salaireNet)
	{
		this.salaireNet = salaireNet;
	}

	public double getSalaireAecarterDuNet()
	{
		return salaireAecarterDuNet;
	}

	public void setSalaireAecarterDuNet(double salaireAecarterDuNet)
	{
		this.salaireAecarterDuNet = salaireAecarterDuNet;
	}

	public boolean isEmbauche()
	{
		return embauche;
	}

	public void setEmbauche(boolean embauche)
	{
		this.embauche = embauche;
	}

	//
	

	public ClsParametreCumul getParamCumul()
	{
		return paramCumul;
	}

	public void setParamCumul(ClsParametreCumul paramCumul)
	{
		this.paramCumul = paramCumul;
	}

	public ClsValeurRubriquePartage getValeurRubriquePartage()
	{
		return valeurRubriquePartage;
	}

	public void setValeurRubriquePartage(ClsValeurRubriquePartage valeurRubriquePartage)
	{
		this.valeurRubriquePartage = valeurRubriquePartage;
	}

	public GeneriqueConnexionService getService()
	{
		return service;
	}

	public void setService(GeneriqueConnexionService service)
	{
		this.service = service;
	}

	public ClsDate getMyMonthOfPay()
	{
		return myMonthOfPay;
	}

	public void setMyMonthOfPay(ClsDate myMonthOfPay)
	{
		this.myMonthOfPay = myMonthOfPay;
	}

	public int getAnciennete()
	{
		return anciennete;
	}

	public void setAnciennete(int anciennete)
	{
		this.anciennete = anciennete;
	}

	//
	public ClsInfoSalaryClone getInfoSalary()
	{
		return infoSalary;
	}

	public void setInfoSalary(ClsInfoSalaryClone infoSalary)
	{
		this.infoSalary = infoSalary;
	}

	public List<ClsFictifRubriqueClone> getListOfRubriqueOfMotifFinContratFictif()
	{
		return ListOfRubriqueOfMotifFinContratFictif;
	}

	public void setListOfRubriqueOfMotifFinContratFictif(List<ClsFictifRubriqueClone> listOfRubriqueOfMotifFinContratFictif)
	{
		ListOfRubriqueOfMotifFinContratFictif = listOfRubriqueOfMotifFinContratFictif;
	}

	public double getTot_cgs()
	{
		return tot_cgs;
	}

	public void setTot_cgs(double tot_cgs)
	{
		this.tot_cgs = tot_cgs;
	}

	public boolean isL_traitmois()
	{
		return l_traitmois;
	}

	public void setL_traitmois(boolean l_traitmois)
	{
		this.l_traitmois = l_traitmois;
	}

	public boolean isDern_mois_de_conge()
	{
		return dern_mois_de_conge;
	}

	public void setDern_mois_de_conge(boolean dern_mois_de_conge)
	{
		this.dern_mois_de_conge = dern_mois_de_conge;
	}

	public Map<String, Object[]> getListSpecifiqueCumul99Map4()
	{
		return listSpecifiqueCumul99Map4;
	}

	public void setListSpecifiqueCumul99Map4(Map<String, Object[]> listSpecifiqueCumul99Map4)
	{
		this.listSpecifiqueCumul99Map4 = listSpecifiqueCumul99Map4;
	}

	public Map<String, Object> getListSpecifiqueCumul99Map5()
	{
		return listSpecifiqueCumul99Map5;
	}

	public void setListSpecifiqueCumul99Map5(Map<String, Object> listSpecifiqueCumul99Map5)
	{
		this.listSpecifiqueCumul99Map5 = listSpecifiqueCumul99Map5;
	}

	public ClsFictifPeriodUtil getOFictifPeriod()
	{
		return oFictifPeriod;
	}

	public void setOFictifPeriod(ClsFictifPeriodUtil fictifPeriod)
	{
		oFictifPeriod = fictifPeriod;
	}
	
	/**
	 * construit un map qui contient les éléments variables d'un agent
	 */
	public void buildElementVarMap()
	{
		Map<String, List<Object[]>> map = new HashMap<String, List<Object[]>>();
		List listOfElementVariable = new ArrayList();
		// CURSOR curs_evar IS
		// SELECT mont, argu, nprt, ruba FROM paevar
		// WHERE cdos = PA_CALCUL.wpdos.cdos
		// AND nmat = PA_CALCUL.wsal01.nmat
		// AND aamm = PA_CALCUL.w_aamm
		// AND nbul = PA_CALCUL.wsd_fcal1.nbul
		// AND rubq = PA_CALCUL.t_rub.crub;
		//
		String queryString = "select rubq, mont, argu, nprt, ruba from ElementVariableDetailMois" + " where identreprise ='" + this.getParam().getDossier() + "'" + " and nmat = '"
				+ this.getInfoSalary().getComp_id().getNmat() + "'" + " and aamm = '" + this.getParam().getMonthOfPay() + "'" + " and nbul = " + this.getParam().getNumeroBulletin();
		//

			listOfElementVariable = this.getService().find(queryString);
		//
		Object elvar[] = null;
		for (Object object : listOfElementVariable)
		{
			// map.put(crub, value)
			elvar = new Object[4];
			elvar[0] = ((Object[]) object)[1];
			elvar[1] = ((Object[]) object)[2];
			elvar[2] = ((Object[]) object)[3];
			elvar[3] = ((Object[]) object)[4];
			if (!map.containsKey((String) ((Object[]) object)[0]))
				map.put((String) ((Object[]) object)[0], new ArrayList<Object[]>());
			map.get((String) ((Object[]) object)[0]).add(elvar);
		}
		//
		listOfEltvar = map;
	}
	
	public void buildElementFixeMap()
	{
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();
		// BEGIN
		// SELECT monp INTO PA_CALCUL.w_transit FROM paelfix
		// WHERE cdos = PA_CALCUL.wpdos.cdos
		// AND nmat = PA_CALCUL.wsal01.nmat
		// AND codp = PA_CALCUL.t_rub.crub
		// AND (ddeb IS NULL OR
		// (ddeb IS NOT NULL AND ddeb <= TO_DATE(PA_CALCUL.w_aamm,'YYYYMM')))
		// AND (dfin IS NULL OR
		// (dfin IS NOT NULL AND dfin >= TO_DATE(PA_CALCUL.w_aamm,'YYYYMM')));
		// EXCEPTION
		// WHEN OTHERS THEN null;
		// END;
		String complexQuery = "select codp, monp from ElementFixeSalaire" + " where identreprise ='" + this.getParam().getDossier() + "'" + " and nmat = '"
				+ this.getInfoSalary().getComp_id().getNmat() + "'" + " and (ddeb is null or ((ddeb is not null) and (ddeb <= '"
				+ new ClsDate(param.getMyMonthOfPay().getFirstDayOfMonth()).getDateS(param.appDateFormat) + "')))" + " and (dfin is null or ((dfin is not null) and (dfin >= '"
				+ new ClsDate(param.getMyMonthOfPay().getFirstDayOfMonth()).getDateS(param.appDateFormat) + "')))";
		// if('O' == this.getParameter().getGenfile()) setOutputtext("\n"+"Query: " + complexQuery);
//		System.out.println("Query: " + complexQuery);
		List l = this.getService().find(complexQuery);
		//
		//
		for (Object object : l)
		{
			// map.put(crub, value)
			if (!map.containsKey((String) ((Object[]) object)[0]))
				map.put((String) ((Object[]) object)[0], new ArrayList<Object>());
			map.get((String) ((Object[]) object)[0]).add(((Object[]) object)[1]);
		}
		//
		listOfEltfix = map;
//		System.out.println("Taille elt fix:"+listOfEltfix.size());
	}
	
	public void buildLignePretMap()
	{
		listLignePretMap = new HashMap<String, List<Object>>();
		// CURSOR curs_preti IS SELECT lg FROM paprets
		// WHERE cdos = wpdos.cdos
		// AND nmat = wsal01.nmat
		// AND crub = t_rub.crub
		// AND premrb <= w_aamm
		// AND etatpr = 'D'
		// AND ( NVL(mtpr,0) != NVL(mtremb,0)
		// OR ( NVL(mtpr,0) = 0 AND
		// NVL(mtmens,0) != 0 )
		// )
		// ORDER BY lg;
		String complexQuery = "select crub, lg from Rhtpretsagent" + " where identreprise ='" + this.getParam().getDossier() + "'" + " and nmat = '" + this.getInfoSalary().getComp_id().getNmat()
				+ "'" + " and premrb <= '" + param.getMonthOfPay() + "'" + " and etatpr = 'D'"
				+ " and (((case when mtpr is null then 0 else mtpr end) != (case when mtremb is null then 0 else mtremb end))"
				+ " or (((case when mtpr is null then 0 else mtpr end) = 0) and ((case when mtmens is null then 0 else mtmens end) != 0)))" + " order by lg";
		List l = this.getService().find(complexQuery);
		//
		//
		for (Object object : l)
		{
			// map.put(crub, value)
			if (!listLignePretMap.containsKey((String) ((Object[]) object)[0]))
				listLignePretMap.put((String) ((Object[]) object)[0], new ArrayList<Object>());
			listLignePretMap.get((String) ((Object[]) object)[0]).add(((Object[]) object)[1]);
		}
	}

	public CongeFictifService getCongeFictifService() {
		return congeFictifService;
	}

	public void setCongeFictifService(CongeFictifService congeFictifService) {
		this.congeFictifService = congeFictifService;
	}

	public CalculPaieService getCalculPaieService() {
		return calculPaieService;
	}

	public void setCalculPaieService(CalculPaieService calculPaieService) {
		this.calculPaieService = calculPaieService;
	}
}
