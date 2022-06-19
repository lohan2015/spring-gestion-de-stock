package com.kinart.paie.business.utils;

import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.kinart.paie.business.model.*;
import com.kinart.paie.business.services.calcul.ClsParameterOfPay;
import com.kinart.paie.business.services.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.BeanUtils;

public class ClsLanDipesMagnetiques
{
	//private static final Logger LOG=Logger.getLogger(currentClassName());
	HttpServletRequest request;

	GeneriqueConnexionService service;

	ClsParameterOfDipe param;

	public Integer nbrSalaries = 0;

	////progressionVO //progression;

	String separateur="|";

	public ClsLanDipesMagnetiques()
	{

	}

	public ClsLanDipesMagnetiques(GeneriqueConnexionService service, ClsParameterOfDipe param)
	{
		this.service = service;
		this.param = param;
	}

	public void lancerGeneration()////progressionVO //progression
	{
		ClsResultat result = null;
		////progression = //progression;

		if (this.nbrSalaries == 0)
		{
			this.traitement = " Aucun Salari� � traiter ";
			//this.engine.incrementerNombreSalariesTraite(0);
			////progression.setEvolutiontraitement("Aucun Salari� � traiter",true);
			return;
		}
		switch (param.typeDipe)
		{
		case ClsTypeDipesMagnetiques.MENSUEL:
			////progression.setTraitementcourant("GENERATION DES DIPES MENSUELS");
			result = this.genererDipeMensuel();
			break;

		case ClsTypeDipesMagnetiques.DEBUT_EXERCICE:
			////progression.setTraitementcourant("GENERATION DES DIPES DE DEBUT D'EXERCICE");
			result = this.genererDipeDebutExercice();
			break;

		case ClsTypeDipesMagnetiques.FIN_EXERCICE:
			////progression.setTraitementcourant("GENERATION DES DIPES DE FIN D'EXERCICE");
			result = this.genererDipeFinExercice();
			break;

		case ClsTypeDipesMagnetiques.TEMPORAIRE:
			////progression.setTraitementcourant("GENERATION DES DIPES TEMPORAIRES");
			result = this.genererDipeTemporaire();
			break;

		case ClsTypeDipesMagnetiques.EMBAUCHE:
			////progression.setTraitementcourant("GENERATION DES DIPES EMBAUCHE");
			result = this.genererDipeEmbauche();
			break;
		case ClsTypeDipesMagnetiques.CD10:
			////progression.setTraitementcourant("GENERATION ETAT CD10");
			result = this.genererDipeCD10();
			break;

		default:
			break;
		}

		if (result != null)
		{
			this.traitement = result.getLibelle();
			////progression.setEvolutiontraitement(this.traitement, true);
			//this.engine.incrementerNombreSalariesTraite(engine.nbrSalariesTraite);
		}
		else
		{
			//progression.setEvolutiontraitement("G�n�ration termin�e avec succ�s");
		}
	}

	private String traitement = null;

	private ClsResultat genererDipeMensuel()
	{
		String codenreg;
		Integer ndip;
		String cndip;
		Integer nfeuil;
		String nemp;
		String cnemp;
		Integer rcnps;
		String nass;
		String cnass;
		Integer nbj;
		BigDecimal salbrut;
		BigDecimal eltexcep;
		BigDecimal saltax;
		BigDecimal salcottot;
		BigDecimal salcotplaf;
		// BigDecimal rettp;
		// BigDecimal retsp;
		BigDecimal rettc;
		BigDecimal retirpp;
		Integer nlign;

		Session session = service.getSession();

		phase = "G�n�ration du fichier " + param.cheminDipeMensuel;


		param.codenreg = "C04";
		codenreg = param.codenreg;


		String c_sal = "SELECT   a.*, dipe.* ";

		if(fnomNbrj != null && StringUtils.equals(fnomNbrj.getVall(), "T"))
		{
			c_sal +=", (Select coalesce(sum(coalesce(b.taux,0)),0) From CumulPaie b where b.idEntreprise = a.idEntreprise and b.nmat = a.nmat and b.aamm = '"+param.aamm+"' and b.rubq in ("+StringUtil.nvl(colxxPos[0], "''")+") ) as mntPos0 ";
			c_sal +=", (Select coalesce(sum(coalesce(c.taux,0)),0) From CumulPaie c where c.idEntreprise = a.idEntreprise and c.nmat = a.nmat and c.aamm = '"+param.aamm+"' and c.rubq in ("+StringUtil.nvl(colxxNeg[0], "''")+") ) as mntNeg0 ";
		}
		else
		{
			c_sal +=", (Select coalesce(sum(coalesce(b.mont,0)),0) From CumulPaie b where b.idEntreprise = a.idEntreprise and b.nmat = a.nmat and b.aamm = '"+param.aamm+"' and b.rubq in ("+StringUtil.nvl(colxxPos[0], "''")+") ) as mntPos0 ";
			c_sal +=", (Select coalesce(sum(coalesce(c.mont,0)),0) From CumulPaie c where c.idEntreprise = a.idEntreprise and c.nmat = a.nmat and c.aamm = '"+param.aamm+"' and c.rubq in ("+StringUtil.nvl(colxxNeg[0], "''")+") ) as mntNeg0 ";
		}
		for (int k = 1; k < 16; k++)
		{
			c_sal +=", (Select coalesce(sum(coalesce(b.mont,0)),0) From CumulPaie b where b.idEntreprise = a.idEntreprise and b.nmat = a.nmat and b.aamm = '"+param.aamm+"' and b.rubq in ("+StringUtil.nvl(colxxPos[k], "''")+") ) as mntPos" + k + " ";
			c_sal +=", (Select coalesce(sum(coalesce(c.mont,0)),0) From CumulPaie c where c.idEntreprise = a.idEntreprise and c.nmat = a.nmat and c.aamm = '"+param.aamm+"' and c.rubq in ("+StringUtil.nvl(colxxNeg[k], "''")+") ) as mntNeg" + k + " ";
		}

		c_sal += "FROM Salarie a  JOIN Dipe dipe ON (dipe.idEntreprise = a.idEntreprise  AND dipe.nmat = a.nmat AND (dipe.annee=:aamm or (dipe.annee!=:aamm and dipe.annee = :annee)))";
		c_sal += " WHERE a.idEntreprise = :cdos ";
//		c_sal += " AND dipe.idEntreprise = a.idEntreprise ";
//		c_sal += " AND dipe.nmat = a.nmat ";
//		c_sal += " AND dipe.annee = :annee ";

//		c_sal += " AND (a.mrrx is null or a.mrrx <> 'RA' )";
		//c_sal += "AND LTRIM (a.vild) IS NOT NULL ";
		if (StringUtils.isNotBlank(param.ville1))
			c_sal += " AND a.vild >= :ville1 ";
		if (StringUtils.isNotBlank(param.ville2))
			c_sal += " AND a.vild <= :ville2 ";
//		c_sal += " AND a.nmat IN (SELECT distinct nmat  FROM CumulPaie WHERE idEntreprise = a.idEntreprise AND aamm = :aamm) ";
//		c_sal += "AND (a.mrrx != 'RA' or a.mrrx is null or (a.dmrr >= :dfex and a.dmrr is not null)) ";
//		c_sal += "AND ((a.mrrx = 'RA' and a.dmrr is not null and a.dmrr >= :dfex) or a.mrrx is null) ";
		//c_sal += "AND ((coalesce(a.mrrx,'XX') != 'RA' or to_char(coalesce(a.dmrr,:daamm),'yyyyMM')>=:aamm)) ";
		c_sal += " ORDER BY a.vild, a.nmat ";

		try
		{
			Query q = session.createSQLQuery(c_sal)
			.addEntity("a", Salarie.class)
			.addEntity("dipe", Dipe.class)
			.addScalar("mntPos0", StandardBasicTypes.BIG_DECIMAL)
			.addScalar("mntNeg0", StandardBasicTypes.BIG_DECIMAL)
			.addScalar("mntPos1", StandardBasicTypes.BIG_DECIMAL)
			.addScalar("mntNeg1", StandardBasicTypes.BIG_DECIMAL)
			.addScalar("mntPos2", StandardBasicTypes.BIG_DECIMAL)
			.addScalar("mntNeg2", StandardBasicTypes.BIG_DECIMAL)
			.addScalar("mntPos3", StandardBasicTypes.BIG_DECIMAL)
			.addScalar("mntNeg3", StandardBasicTypes.BIG_DECIMAL)
			.addScalar("mntPos4", StandardBasicTypes.BIG_DECIMAL)
			.addScalar("mntNeg4", StandardBasicTypes.BIG_DECIMAL)
			.addScalar("mntPos5", StandardBasicTypes.BIG_DECIMAL)
			.addScalar("mntNeg5", StandardBasicTypes.BIG_DECIMAL)
			.addScalar("mntPos6", StandardBasicTypes.BIG_DECIMAL)
			.addScalar("mntNeg6", StandardBasicTypes.BIG_DECIMAL)
			.addScalar("mntPos7", StandardBasicTypes.BIG_DECIMAL)
			.addScalar("mntNeg7", StandardBasicTypes.BIG_DECIMAL)
			.addScalar("mntPos8", StandardBasicTypes.BIG_DECIMAL)
			.addScalar("mntNeg8", StandardBasicTypes.BIG_DECIMAL)
			.addScalar("mntPos9", StandardBasicTypes.BIG_DECIMAL)
			.addScalar("mntNeg9", StandardBasicTypes.BIG_DECIMAL)
			.addScalar("mntPos10", StandardBasicTypes.BIG_DECIMAL)
			.addScalar("mntNeg10", StandardBasicTypes.BIG_DECIMAL)
			.addScalar("mntPos11", StandardBasicTypes.BIG_DECIMAL)
			.addScalar("mntNeg11", StandardBasicTypes.BIG_DECIMAL)
			.addScalar("mntPos12", StandardBasicTypes.BIG_DECIMAL)
			.addScalar("mntNeg12", StandardBasicTypes.BIG_DECIMAL)
			.addScalar("mntPos13", StandardBasicTypes.BIG_DECIMAL)
			.addScalar("mntNeg13", StandardBasicTypes.BIG_DECIMAL)
			.addScalar("mntPos14", StandardBasicTypes.BIG_DECIMAL)
			.addScalar("mntNeg14", StandardBasicTypes.BIG_DECIMAL)
			.addScalar("mntPos15", StandardBasicTypes.BIG_DECIMAL)
			.addScalar("mntNeg15", StandardBasicTypes.BIG_DECIMAL);

			q.setParameter("cdos", param.cdos);
			q.setParameter("aamm", param.aamm);
			//q.setParameter("daamm", param.date);
			if (StringUtils.isNotBlank(param.ville1))
				q.setParameter("ville1", param.ville1);
			if (StringUtils.isNotBlank(param.ville2))
				q.setParameter("ville2", param.ville2);
//			q.setParameter("dfex", new ClsDate(param.aamm, "yyyyMM").getFirstDayOfMonth(), StandardBasicTypes.DATE);
			q.setParameter("annee", param.annee);
			//LOG.info(param.dfex);

			this.traitement = "Chargement des salari�s � traiter, veuillez patienter";
			//progression.setEvolutiontraitement(this.traitement,false);
			List<Object[]> lst = q.list();

			if (lst.size() == 0)
			{
				this.traitement = " Aucun Salari� � traiter ";
				//progression.setEvolutiontraitement(this.traitement,true);
				return null;
			}
			Integer nbrLigne = lst.size();
			//progression.debutAffichageProgressBar(nbrLigne);
			//if (nbrLigne > 0)
				//progression.mettreAJourProgressBar(0);

			// Suppression du fichier
			param.deleteFileToGenerate();

			Salarie a;
			Dipe b;
			Salarie ancien_a;
			String nmat = null;
			String ancien_nmat = null;
			ClsVarDipes var = null;
			String texteLigne = StringUtils.EMPTY;
			String texteLigne2 = StringUtils.EMPTY;

			try
			{
				PrintWriter bufferedWriter = new PrintWriter(new FileWriter(param.cheminDipeMensuel));
				PrintWriter bufferedWriter2 = new PrintWriter(new FileWriter(param.cheminDipeMensuel2));

				texteLigne2="Enregistrement"+separateur+"Num Dipe"+separateur+"Cle Num Dipe"+separateur+"Num Contribuable"+separateur+"Num Feuillet"+separateur+"Num Employeur"+separateur+" Cle Num Empl"
				+separateur+"Regime CNPS"+separateur+"Annee Dipe"+separateur+"Num assure"+separateur+"Cle num assure"+separateur+"Nbr Jours"+separateur+"Sal Brut"
				+separateur+"Sal Except"+separateur+"Sal Taxable"+separateur+"Sal Cotisable Cnps"+separateur+"Sal Cotisable plaf"+separateur+"Retenue irpp"+separateur+"Retenue taxe communale"
				+separateur+"Num Ligne"+separateur+"Matricule Interne"+separateur+"Filler";
				bufferedWriter2.println(texteLigne2);
				bufferedWriter.flush();
				ParameterUtil.println("<--------->"+"Fin �criture de l'ent�te du fichier s�par�");
				int j=0;
				for (int i = 0; i < lst.size(); i++)
				{
					j=i;
					a = (Salarie) lst.get(i)[0];
					b = (Dipe) lst.get(i)[1];
					nmat = a.getNmat();


					this.traitement = "Matricule Courant "+nmat+" - "+a.getNom()+" "+StringUtil.nvl(a.getPren(),"");
					System.out.println(this.traitement);
					//progression.setEvolutiontraitement(this.traitement);
					if (i > 0)
					{
						ancien_a = (Salarie) lst.get(i - 1)[0];
						ancien_nmat = ancien_a.getNmat();
					}
//					if (StringUtil.notEquals(ancien_nmat, nmat))
//					{
						//progression.mettreAJourProgressBar(++j);
						//engine.incrementerNombreSalariesTraite();
//					}

					if (StringUtils.isBlank(a.getVild()))
						return ClsTreater._getResultat("Ville non d�clar�e pour le salari� " + nmat + " " + a.getNom() + " " + StringUtil.nvl(a.getPren(), " "), null, true);

					System.out.println("Lire ville declaration.............................");
					this.infoVille(a.getVild());
					//var = this.calculSalaire(session,a);
					System.out.println("Calcul salaire.............................");
					var = this.calculSalaire(lst.get(i), a.getCods());

					//ParameterUtil.println("Nmat "+nmat+" Variables : "+var.localToStrin());
					if(b==null){
						b = new Dipe();
						b.setIdEntreprise(a.getIdentreprise());
					}
					ndip = b.getNume();
					cndip = b.getLett();param.numCompte=StringUtils.isBlank(param.numCompte)?StringUtil.getEmptyString(10):param.numCompte;
					nfeuil = param.feuille;
					// --voir le changement de matricule
					// --enlever les tiret
					// --N� sur 11 position sans les tirets
					System.out.println("Lecture montant.............................");
					nemp = StringUtil.oraLPad(StringUtils.replace(StringUtil.nvl(StringUtils.replace(StringUtil.nvl(param.numCompte, " "), "-", ""), " "), " ", ""), 11, "0");
					nemp = StringUtil.oraSubstring(nemp, 1, 10);

					// --Le dernier caractere represente la cl�
					//cnemp = StringUtil.oraSubstring(param.numCompte, -1, 1);
					cnemp = StringUtil.lastCharacter(param.numCompte);
					rcnps = param.regCNPS.intValue();

					if (StringUtils.isNotBlank(a.getNoss()))
					{
						nass = StringUtils.replace(StringUtil.nvl(a.getNoss(), " "), "-", "");// -- retirer le tiret
						nass = StringUtils.replace(StringUtil.nvl(nass, " "), " ", "");// -- retirer les espaces
						nass = StringUtil.oraLPad(nass, 11, "0");
						nass = StringUtil.oraSubstring(nass, 1, 10);// -- prendre les 10 premiers
						nass = StringUtil.oraLPad(nass, 10, "0"); // -- completer les anciens matricules avec des z�ros
						// --Le dernier caractere represente la cl�
						//cnass = StringUtil.oraSubstring(StringUtil.nvl(a.getNoss(), " "), -1, 1);
						cnass = StringUtil.lastCharacter(StringUtil.nvl(a.getNoss(), " "));
					}
					else
					{
						nass = "          ";// -- 10 espace
						cnass = " ";
					}

					System.out.println("Nb de jours="+var.nbj);
					//if (var.nbj.compareTo(new BigDecimal(99)) <= 0)
						nbj = var.nbj.compareTo(new BigDecimal(param.maxNbrJours)) <= 0?var.nbj.intValue():param.maxNbrJours;
//					else
//						return ClsTreater._getResultat("Nb de jour > 99 ! pour le salari� " + nmat + " " + a.getNom() + " " + StringUtil.nvl(a.getPren(),""), null, true);
					//LOG.debug("JOS-DEBUG:======> MATRICULE EN COURS   = "+a.getNmat());
					//LOG.debug("JOS-DEBUG:======> NMAT = DIPE-nbj av = "+nbj);
					salbrut = var.salBrut;
					//LOG.debug("JOS-DEBUG:======> NMAT = DIPE-SALBRUT av = "+salbrut.doubleValue());
					salbrut = salbrut.setScale(0,BigDecimal.ROUND_HALF_UP);
					//LOG.debug("JOS-DEBUG:======> NMAT = DIPE-SALBRUT apr = "+salbrut.doubleValue());
					eltexcep = var.elExcep;
					//LOG.debug("JOS-DEBUG:======> NMAT = DIPE-eltexcep av = "+eltexcep.doubleValue());
					eltexcep =eltexcep.setScale(0,BigDecimal.ROUND_HALF_UP);
					//LOG.debug("JOS-DEBUG:======> NMAT = DIPE-eltexcep apr = "+eltexcep.doubleValue());
					saltax = var.salTax;
					//LOG.debug("JOS-DEBUG:======> NMAT = DIPE-saltax av = "+saltax.doubleValue());
					saltax =saltax.setScale(0,BigDecimal.ROUND_HALF_UP);
					//LOG.debug("JOS-DEBUG:======> NMAT = DIPE-saltax apr = "+saltax.doubleValue());
					salcottot = var.totCnp;
					//LOG.debug("JOS-DEBUG:======> NMAT = DIPE-salcottot av = "+salcottot.doubleValue());
					salcottot = salcottot.setScale(0,BigDecimal.ROUND_HALF_UP);
					//LOG.debug("JOS-DEBUG:======> NMAT = DIPE-salcottot apr = "+salcottot.doubleValue());
					salcotplaf = var.plafCnp;
					//LOG.debug("JOS-DEBUG:======> NMAT = DIPE-salcotplaf av = "+salcotplaf.doubleValue());
					salcotplaf =salcotplaf.setScale(0,BigDecimal.ROUND_HALF_UP);
					//LOG.debug("JOS-DEBUG:======> NMAT = DIPE-salcotplaf apr = "+salcotplaf.doubleValue());

					// rettp = var.retTax;
					// --retsp =var.ret_surt;
					// --retenue surtaxe prog devient l'irpp
					retirpp = var.retSurt;
					retirpp =retirpp.setScale(0,BigDecimal.ROUND_HALF_UP);
					// retsp = var.retSurt;
					rettc = var.retTxco;
					rettc = rettc.setScale(0,BigDecimal.ROUND_HALF_UP);
					nlign = b.getLign();

//					String texte = "Mat "+nmat+" :codenreg - "+ codenreg+" ndip- "+ndip+" cndip- "+cndip+" ncontribuable- "+param.ncontribuable+" nfeuil- "+nfeuil+" nemp- "+nemp+" cnemp- "+cnemp+" rcnps- "+rcnps+" adexe_c- "+param.adexe_c;
//					texte+=" nass- "+nass+" cnass- "+cnass+" nbj- "+nbj+" salbrut- "+salbrut+" eltexcep- "+eltexcep+" saltax- "+saltax+" salcottot- "+salcottot+" salcotplaf- "+salcotplaf+" retirpp- "+retirpp+" rettc- "+rettc+" nlign- "+nlign+" nmat- "+nmat;
//					ParameterUtil.println(texte);
					texteLigne = StringUtil.oraLPad(codenreg, 3) + StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(ndip)), 5, " ") + StringUtil.nvl(cndip, " ")
							+ StringUtil.oraLPad(StringUtil.oraLTrim(param.ncontribuable), 14) + StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(nfeuil)), 2, "0")
							+ StringUtil.oraLPad(StringUtil.oraLTrim(nemp), 10) + StringUtil.oraLPad(cnemp, 1) + StringUtil.nvl(StringUtil.oraLTrim(StringUtil.oraToChar(rcnps)), " ") + /*param.adexe_c*/param.annee
							+ StringUtil.oraLPad(nass, 10) + StringUtil.oraLPad(cnass, 1) + StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(nbj)), 2, "0")
							+ StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(salbrut)), 10, "0") + StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(eltexcep)), 10, "0")
							+ StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(saltax)), 10, "0") + StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(salcottot)), 10, "0")
							+ StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(salcotplaf)), 10, "0") + StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(retirpp)), 8, " ")
							+ StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(rettc)), 6, "0") + StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(nlign)), 2, "0")
							+ StringUtil.oraLPad(nmat, 14, " ") + StringUtil.oraLPad(" ", 1) + (StringUtils.isNotBlank(a.getNoss())?StringUtils.EMPTY:StringUtil.oraLPad(StringUtil.oraSubstring(StringUtils.upperCase(StringUtil.unAccent(StringUtil.join2(a.getNom()," ",a.getPren()))), 1, 59), 59));

					texteLigne2 = StringUtil.oraLPad(codenreg, 3) +separateur+ StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(ndip)), 5, " ") +separateur+ StringUtil.nvl(cndip, " ")
					+separateur+ StringUtil.oraLPad(StringUtil.oraLTrim(param.ncontribuable), 14) +separateur+ StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(nfeuil)), 2, "0")
					+separateur+ StringUtil.oraLPad(StringUtil.oraLTrim(nemp), 10) +separateur+ StringUtil.oraLPad(cnemp, 1) +separateur+ StringUtil.nvl(StringUtil.oraLTrim(StringUtil.oraToChar(rcnps)), " ") +separateur+ /*param.adexe_c*/param.annee
					+separateur+ StringUtil.oraLPad(nass, 10) +separateur+ StringUtil.oraLPad(cnass, 1) +separateur+ StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(nbj)), 2, "0")
					+separateur+ StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(salbrut)), 10, "0") +separateur+ StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(eltexcep)), 10, "0")
					+separateur+ StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(saltax)), 10, "0") +separateur+ StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(salcottot)), 10, "0")
					+separateur+ StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(salcotplaf)), 10, "0") +separateur+ StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(retirpp)), 8, " ")
					+separateur+ StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(rettc)), 6, "0") +separateur+ StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(nlign)), 2, "0")
					+separateur+ StringUtil.oraLPad(nmat, 14, " ") +separateur+ StringUtil.oraLPad(" ", 1) + (StringUtils.isNotBlank(a.getNoss())?StringUtils.EMPTY:StringUtil.join2(separateur,StringUtil.oraLPad(StringUtil.oraSubstring(StringUtils.upperCase(StringUtil.unAccent(StringUtil.join2(a.getNom()," ",a.getPren()))), 1, 59), 59)));


					System.out.println("Texte ligne="+texteLigne);
					bufferedWriter.println(texteLigne);
					ParameterUtil.println("<--------->"+"Fin �criture Ligne "+i);
					bufferedWriter.flush();
					ParameterUtil.println("<--------->"+"�criture Ligne "+i+" = "+texteLigne2);
					bufferedWriter2.println(texteLigne2);
					ParameterUtil.println("<--------->"+"Fin �criture Ligne "+i+" du fichier s�par�");
					bufferedWriter.flush();
				}
				bufferedWriter.close();
				bufferedWriter2.close();

			}
			catch (IOException e)
			{
				//LOG.error(e.getLocalizedMessage(),e);
				return ClsTreater._getResultat(ClsTreater._getStackTrace(e), null, true);
			}
		}
		catch (Exception e)
		{
			//LOG.error(e.getLocalizedMessage(),e);
			return ClsTreater._getResultat(ClsTreater._getStackTrace(e), null, true);
		}
		finally
		{
			service.closeSession(session);
		}
		return null;
	}

	private ClsResultat genererDipeDebutExercice()
	{
		String nato;

		String codenreg;
		Integer ndip;
		String cndip;
		String nemp;
		String cnemp;
		Integer rcnps;
		String nass;
		String cnass;
		Integer nlign;
		String sexe;
		String datnais;
		String nom;
		String pren;
		String sitf;
		Integer nbec;
		Integer nbpt;
		String typpers;
		String cat;
		//Integer typact;
		String dtes_c;

		phase = "G�n�ration du fichier " + param.cheminDipeDebutExercice;
		//engine.incrementerNombreSalariesTraite(0);

		param.codenreg = "C05";
		codenreg = param.codenreg;

		String c_sal = "SELECT   a.*, b.* ";
		c_sal += "FROM Salarie a  JOIN Dipe b ON (b.idEntreprise = a.idEntreprise AND b.nmat = a.nmat AND (b.annee=:aamm or (b.annee!=:aamm and b.annee = :annee)))";
		c_sal += "WHERE a.idEntreprise = :cdos ";
//		c_sal += "AND b.idEntreprise = a.idEntreprise ";
//		c_sal += "AND b.nmat = a.nmat ";
//		c_sal += "AND b.annee = :annee ";

		c_sal += "AND LTRIM (a.vild) IS NOT NULL ";
		if (StringUtils.isNotBlank(param.ville1))
			c_sal += "AND a.vild >= :ville1 ";
		if (StringUtils.isNotBlank(param.ville2))
			c_sal += "AND a.vild <= :ville2 ";
		c_sal += "AND exists (SELECT 'x' ";
		c_sal += "FROM CumulPaie c ";
		c_sal += "WHERE c.idEntreprise = a.idEntreprise AND c.aamm = :aamm and c.nmat = a.nmat) ";
		c_sal += "AND (a.mrrx != 'RA' or a.mrrx is null or (a.dmrr > :dfex and a.dmrr is not null)) ";
		c_sal += "ORDER BY a.vild, a.nmat ";

		Session session = service.getSession();
		try
		{

			Query q = session.createSQLQuery(c_sal).addEntity("a", Salarie.class).addEntity("b", Dipe.class);

			q.setParameter("cdos", param.cdos);
			q.setParameter("aamm", param.aamm);
			if (StringUtils.isNotBlank(param.ville1))
				q.setParameter("ville1", param.ville1);
			if (StringUtils.isNotBlank(param.ville2))
				q.setParameter("ville2", param.ville2);
			q.setParameter("dfex", param.dfex, StandardBasicTypes.DATE);
			q.setParameter("annee", param.annee);

			this.traitement = "Chargement des salari�s � traiter, veuillez patienter";
			//progression.setEvolutiontraitement(this.traitement,false);
			//this.engine.incrementerNombreSalariesTraite(0);
			List<Object[]> lst = q.list();

			if (lst.size() == 0)
			{
				this.traitement = " Aucun Salari� � traiter ";
				//progression.setEvolutiontraitement(traitement,true);
				//this.engine.incrementerNombreSalariesTraite(0);
				return null;
			}
			Integer nbrLigne = lst.size();
			//progression.debutAffichageProgressBar(nbrLigne);
			if (nbrLigne > 0)
				//progression.mettreAJourProgressBar(0);

			// Suppression du fichier
			param.deleteFileToGenerate();

			Salarie a;
			Dipe b;
			Salarie ancien_a;
			String nmat = null;
			String ancien_nmat = null;
			String texteLigne = StringUtils.EMPTY;
			String texteLigne2 = StringUtils.EMPTY;

			try
			{
				PrintWriter bufferedWriter = new PrintWriter(new FileWriter(param.cheminDipeDebutExercice));
				PrintWriter bufferedWriter2 = new PrintWriter(new FileWriter(param.cheminDipeDebutExercice2));

				texteLigne2="Enregistrement"+separateur+"Num Dipe"+separateur+"Cle Num Dipe"+separateur+"Num Feuillet"+separateur+"Num Contribuable"+separateur+"Num Employeur"+separateur+" Cle Num Empl"
				+separateur+"Regime CNPS"+separateur+"Annee Debut Exo" +separateur+" Convention Collective"+separateur+"Type Activite ind"+separateur+"Sexe"+separateur+"Nationalite"
				+separateur+"Date Naissance"+separateur+"Num assure"+separateur+"Cle num assure"+separateur+"Nom assure"+separateur+"Prenom assure"+separateur+"situation familiale"
				+separateur+"Nombre Enfants"+separateur+"Nombre de parts"+separateur+"Mois embauche"+separateur+"Type de personnel"+separateur+"Num ligne"+separateur+"Matricule Interne"+separateur+"Filler";
				bufferedWriter2.println(texteLigne2);
				bufferedWriter.flush();
				int j=0;
				for (int i = 0; i < lst.size(); i++)
				{
					j=i;
					a = (Salarie) lst.get(i)[0];
					b = (Dipe) lst.get(i)[1];
					nmat = a.getNmat();

					this.traitement = "Matricule Courant "+nmat+" - "+a.getNom()+" "+StringUtil.nvl(a.getPren(),"");
					//progression.setEvolutiontraitement(traitement);
					if (i > 0)
					{
						ancien_a = (Salarie) lst.get(i - 1)[0];
						ancien_nmat = ancien_a.getNmat();
					}
					if (StringUtil.notEquals(ancien_nmat, nmat))
						//engine.incrementerNombreSalariesTraite();
						//progression.mettreAJourProgressBar(++j);

					if (StringUtils.isBlank(a.getVild()))
						return ClsTreater._getResultat( "Ville non d�clar�e pour le salari� " + nmat + " " + a.getNom() + " " + StringUtil.nvl(a.getPren(), " "), null, true);

					this.infoVille(a.getVild());
					// var = this.calculSalaire(a);
					ndip = b.getNume();
					cndip = b.getLett();
					// nfeuil = 13;
					// --voir le changement de matricule
					// --enlever les tiret
					// --N� sur 11 position sans les tirets
					nemp = StringUtil.oraLPad(StringUtils.replace(StringUtil.nvl(param.numCompte, " "), "-", ""), 11, "0");
					nemp = StringUtil.oraSubstring(nemp, 1, 10);
					rcnps = param.regCNPS.intValue();
					// --Le dernier caractere represente la cl�
					cnemp = StringUtil.lastCharacter(param.numCompte);


					if (StringUtils.isNotBlank(a.getNoss()))
					{
						nass = StringUtils.replace(StringUtil.nvl(a.getNoss(), " "), "-", "");// -- retirer le tiret
						nass = StringUtils.replace(StringUtil.nvl(nass, " "), " ", "");// -- retirer les espaces
						nass = StringUtil.oraLPad(nass, 11, "0");
						nass = StringUtil.oraSubstring(nass, 1, 10);// -- prendre les 10 premiers
						nass = StringUtil.oraLPad(nass, 10, "0"); // -- completer les anciens matricules avec des z�ros
						// --Le dernier caractere represente la cl�
						cnass = StringUtil.lastCharacter(StringUtil.nvl(a.getNoss(), " "));
					}
					else
					{
						nass = "          ";// -- 10 espace
						cnass = " ";
					}

					cat = StringUtil.oraSubstring(a.getCat(), 1, 2);

					//typact = new Integer(StringUtil.nvl(StringUtil.oraSubstring(service.findAnyColumnFromNomenclature(param.cdos, param.clang, "7", a.getFonc(), "3").getVall(), 1, 3),"0"));

					sexe = a.getSexe();

					nato = StringUtil.nvl(service.findAnyColumnFromNomenclature(param.cdos, param.clang, "4", a.getNato(), "2").getVall(), " ");
					nato = StringUtil.oraLPad(nato, 3);

					datnais = new ClsDate(a.getDtna()).getDateS("MMyyyy");
					nom = StringUtil.oraRPad(a.getNom(), 20);
					nom=StringUtil.oraSubstring(nom, 1, 20);
					pren = StringUtil.oraRPad(StringUtils.isBlank(a.getPren())?StringUtil.getEmptyString(15):a.getPren(), 15);pren=StringUtils.substring(pren, 0, 15);

					sitf = a.getSitf();
					nbec = a.getNbec() != null ? a.getNbec().intValue() : 0;
					nbpt = a.getNbpt() != null ? a.getNbpt().intValue() : 0;
					if(param.inclureEnfantsEtNombreParts)
						sitf = sitf + StringUtil.oraLTrim(ClsStringUtil.formatNumber(nbec, "00")) 	+ StringUtil.oraLTrim(ClsStringUtil.formatNumber(nbpt, "000"));
					dtes_c = new ClsDate(a.getDtes()).getDateS("ddMMyyyy");

					typpers = "X";

					nlign = b.getLign();

					texteLigne =

					StringUtil.oraLPad(codenreg, 3) + StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(ndip)), 5, " ") + StringUtil.nvl(cndip, " ") + "13"
							+ StringUtil.oraLPad(StringUtil.oraLTrim(param.ncontribuable), 14) + StringUtil.oraLPad(StringUtil.oraLTrim(nemp), 10) + StringUtil.oraLPad(cnemp, 1)
							+ StringUtil.nvl(StringUtil.oraLTrim(StringUtil.oraToChar(rcnps)), " ") + param.adexe_c + StringUtil.oraLPad(cat, 2) + StringUtil.nvl(ClsStringUtil.formatNumber(param.typact,"000"), "000") + sexe + nato
							+ datnais + StringUtil.oraLPad(nass, 10) + StringUtil.oraLPad(cnass, 1) + nom + pren + sitf + StringUtil.oraLPad(dtes_c, 4) + typpers + StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(nlign)), 2, "0")
							+ StringUtil.oraLPad(nmat, 14, " ") + StringUtil.oraLPad(" ", 8);

					texteLigne2 =

						StringUtil.oraLPad(codenreg, 3) +separateur+ StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(ndip)), 5, " ") +separateur+ StringUtil.nvl(cndip, " ") +separateur+ "13"
								+separateur+ StringUtil.oraLPad(StringUtil.oraLTrim(param.ncontribuable), 14) +separateur+ StringUtil.oraLPad(StringUtil.oraLTrim(nemp), 10) +separateur+ StringUtil.oraLPad(cnemp, 1)
								+separateur+ StringUtil.nvl(StringUtil.oraLTrim(StringUtil.oraToChar(rcnps)), " ") +separateur+ param.adexe_c +separateur+ StringUtil.oraLPad(cat, 2) +separateur+ StringUtil.nvl(ClsStringUtil.formatNumber(param.typact,"000"), "000") +separateur+ sexe +separateur+ nato
								+separateur+ datnais +separateur+ StringUtil.oraLPad(nass, 10) +separateur+ StringUtil.oraLPad(cnass, 1) +separateur+ nom +separateur+ pren +separateur+ sitf +separateur+ StringUtil.oraLPad(dtes_c, 4) +separateur+ typpers +separateur+ StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(nlign)), 2, "0")
								+separateur+ StringUtil.oraLPad(nmat, 14, " ") +separateur+ StringUtil.oraLPad(" ", 8);

					bufferedWriter.println(texteLigne);
					bufferedWriter.flush();

					bufferedWriter2.println(texteLigne2);
					bufferedWriter.flush();
				}
				bufferedWriter.close();
				bufferedWriter2.close();

			}
			catch (IOException e)
			{
				//LOG.error(e.getLocalizedMessage(),e);
				return ClsTreater._getResultat( ClsTreater._getStackTrace(e), null, true);
			}
		}
		catch (Exception e)
		{
			//LOG.error(e.getLocalizedMessage(),e);
			return ClsTreater._getResultat( ClsTreater._getStackTrace(e), null, true);
		}
		finally
		{
			service.closeSession(session);
		}
		return null;
	}

	private ClsResultat genererDipeFinExercice()
	{
		String codenreg;
		Integer ndip;
		String cndip;
		String nemp;
		String cnemp;
		Integer rcnps;
		String nass;
		String cnass;
		Integer nlign;
		String rdrsaltax;
		String rdrsalcot;
		String rdrirpp;
		String rdrtc;
		String avlo;
		String nbmavlo;
		String avno;
		String nbmavno;
		String avel;
		String nbmavel;
		String avdo;
		String nbmavdo;
		String dtes_c;
		String dtss_c;
		String rubavlo;
		String rubavno;
		String rubavel;
		String rubavdo;
		String avn;

		String liste_rub_tax;
		String liste_rub_cot;
		// String liste_rub_tp;
		String liste_rub_sp;
		String liste_rub_tc;

		phase = "G�n�ration du fichier " + param.cheminDipeFinExercice;
		//engine.incrementerNombreSalariesTraite(0);

		param.codenreg = "C03";
		codenreg = param.codenreg;

		String c_sal = "SELECT   a.*, b.* ";
		c_sal += "FROM Salarie a JOIN  Dipe b ON (b.idEntreprise = a.idEntreprise AND b.nmat = a.nmat AND (b.annee=:aamm or (b.annee!=:aamm and b.annee = :annee)))";
		c_sal += "WHERE a.idEntreprise = :cdos ";
//		c_sal += "AND b.idEntreprise = a.idEntreprise ";
//		c_sal += "AND b.nmat = a.nmat ";
//		c_sal += "AND b.annee = :annee ";
		c_sal += "AND LTRIM (a.vild) IS NOT NULL ";
		if (StringUtils.isNotBlank(param.ville1))
			c_sal += "AND a.vild >= :ville1 ";
		if (StringUtils.isNotBlank(param.ville2))
			c_sal += "AND a.vild <= :ville2 ";
		c_sal += "AND exists (SELECT 'x' ";
		c_sal += "FROM CumulPaie c ";
		c_sal += "WHERE c.idEntreprise = a.idEntreprise AND c.aamm = :aamm and c.nmat = a.nmat) ";
		c_sal += "AND (a.mrrx != 'RA' or a.mrrx is null or (a.dmrr > :dfex and a.dmrr is not null)) ";
		c_sal += "ORDER BY a.vild, a.nmat ";

		Session session = service.getSession();

		try
		{
			Query  q = session.createSQLQuery(c_sal).addEntity("a", Salarie.class).addEntity("b", Dipe.class);

			q.setParameter("cdos", param.cdos);
			q.setParameter("aamm", param.aamm);
			if (StringUtils.isNotBlank(param.ville1))
				q.setParameter("ville1", param.ville1);
			if (StringUtils.isNotBlank(param.ville2))
				q.setParameter("ville2", param.ville2);
			q.setParameter("dfex", param.dfex, StandardBasicTypes.DATE);
			q.setParameter("annee", param.annee);

			this.traitement = "Chargement des salari�s � traiter, veuillez patienter";
			//progression.setEvolutiontraitement(this.traitement,false);
			//this.engine.incrementerNombreSalariesTraite(0);
			List<Object[]> lst = q.list();

			if (lst.size() == 0)
			{
				this.traitement = " Aucun Salari� � traiter ";
				//this.engine.incrementerNombreSalariesTraite(0);
				//progression.setEvolutiontraitement(this.traitement, true);
				return null;
			}

			Integer nbrLigne = lst.size();
			//progression.debutAffichageProgressBar(nbrLigne);
			if (nbrLigne > 0)
				//progression.mettreAJourProgressBar(0);

			// Suppression du fichier
			param.deleteFileToGenerate();

			Salarie a;
			Dipe b;
			Salarie ancien_a;
			String nmat = null;
			String ancien_nmat = null;
			// ClsVarDipes var = null;
			String texteLigne = StringUtils.EMPTY;
			String texteLigne2 = StringUtils.EMPTY;

			liste_rub_tax = service.findAnyColumnFromNomenclature(param.cdos, param.clang, "99", "RUBRED", "1").getVall();
			liste_rub_cot = service.findAnyColumnFromNomenclature(param.cdos, param.clang, "99", "RUBRED", "2").getVall();
			// liste_rub_tp = service.findAnyColumnFromNomenclature(param.cdos, param.clang, "99", "RUBRED", "3").getVall();
			liste_rub_sp = service.findAnyColumnFromNomenclature(param.cdos, param.clang, "99", "RUBRED", "4").getVall();
			liste_rub_tc = service.findAnyColumnFromNomenclature(param.cdos, param.clang, "99", "RUBRED", "5").getVall();
			param.adexe_c = StringUtil.oraLTrim(new ClsDate(param.ddex).getDateS("yy"));

			try
			{
				PrintWriter bufferedWriter = new PrintWriter(new FileWriter(param.cheminDipeFinExercice));
				PrintWriter bufferedWriter2 = new PrintWriter(new FileWriter(param.cheminDipeFinExercice2));
				int j=0;
				for (int i = 0; i < lst.size(); i++)
				{
					j=i;
					a = (Salarie) lst.get(i)[0];
					b = (Dipe) lst.get(i)[1];
					nmat = a.getNmat();
					this.traitement = "Matricule Courant "+nmat+" - "+a.getNom()+" "+StringUtil.nvl(a.getPren(),"");
					//progression.setEvolutiontraitement(this.traitement);
					if (i > 0)
					{
						ancien_a = (Salarie) lst.get(i - 1)[0];
						ancien_nmat = ancien_a.getNmat();
					}
					if (StringUtil.notEquals(ancien_nmat, nmat))
						//engine.incrementerNombreSalariesTraite();
						//progression.mettreAJourProgressBar(++j);

					if (StringUtils.isBlank(a.getVild()))
						return ClsTreater._getResultat( "Ville non d�clar�e pour le salari� " + nmat + " " + a.getNom() + " " + StringUtil.nvl(a.getPren(), " "), null, true);

					this.infoVille(a.getVild());
					// var = this.calculSalaire(a);
					ndip = b.getNume();
					cndip = b.getLett();
					// nfeuil = param.feuille;
					// --voir le changement de matricule
					// --enlever les tiret
					// --N� sur 11 position sans les tirets
					nemp = StringUtil.oraLPad(StringUtils.replace(StringUtil.nvl(param.numCompte, " "), "-", ""), 11, "0");
					nemp = StringUtil.oraSubstring(nemp, 1, 10);
					rcnps = param.regCNPS.intValue();
					// --Le dernier caractere represente la cl�
					cnemp = StringUtil.lastCharacter(param.numCompte);

					if (StringUtils.isNotBlank(a.getNoss()))
					{
						nass = StringUtils.replace(StringUtil.nvl(a.getNoss(), " "), "-", "");// -- retirer le tiret
						nass = StringUtils.replace(StringUtil.nvl(nass, " "), " ", "");// -- retirer les espaces
						nass = StringUtil.oraLPad(nass, 11, "0");
						nass = StringUtil.oraSubstring(nass, 1, 10);// -- prendre les 10 premiers
						nass = StringUtil.oraLPad(nass, 10, "0"); // -- completer les anciens matricules avec des z�ros
						// --Le dernier caractere represente la cl�
						cnass = StringUtil.lastCharacter(StringUtil.nvl(a.getNoss(), " "));
					}
					else
					{
						nass = "          ";// -- 10 espace
						cnass = " ";
					}

					rdrsaltax = StringUtil.nvl(StringUtil.oraLTrim(ClsStringUtil.formatNumber(fmntCumul(nmat, liste_rub_tax, param.aamm_deb, param.aamm_fin), "0999999")), "0000000");
					rdrsalcot = StringUtil.nvl(StringUtil.oraLTrim(ClsStringUtil.formatNumber(fmntCumul(nmat, liste_rub_cot, param.aamm_deb, param.aamm_fin), "0999999")), "0000000");
					// rdrtp =
					// StringUtil.nvl(StringUtil.oraLTrim(ClsStringUtil.formatNumber(fmntCumul(nmat,liste_rub_tp,param.aamm_deb,param.aamm_fin),"0999999")),"0000000");
					rdrirpp = StringUtil.nvl(StringUtil.oraLTrim(ClsStringUtil.formatNumber(fmntCumul(nmat, liste_rub_sp, param.aamm_deb, param.aamm_fin), "0999999")), "0000000");
					rdrtc = StringUtil.nvl(StringUtil.oraLTrim(ClsStringUtil.formatNumber(fmntCumul(nmat, liste_rub_tc, param.aamm_deb, param.aamm_fin), "0999999")), "0000000");

					rubavlo = service.findAnyColumnFromNomenclature(param.cdos, param.clang, "36", "LO", "2").getVall();
//					String query = " Select ltrim(to_char(count(*),'00')) ";
					String query = " Select count(*) ";
					query += " from CumulPaie";
					query += " where idEntreprise = :cdos ";
					query += "and rubq = :rubavlo ";
					query += "and nmat = :nmat ";
					query += "and aamm between :aamm_deb and :aamm_fin and aamm not like '%99' ";
					q = session.createSQLQuery(query);
					q.setParameter("cdos", param.cdos);
					q.setParameter("nmat", nmat);
					q.setParameter("rubavlo", rubavlo);
					q.setParameter("aamm_deb", param.aamm_deb);
					q.setParameter("aamm_fin", param.aamm_fin);

					List<Object> lste = q.list();
					Object obj=lste.get(0);
					if(obj instanceof Number){
						nbmavlo=ClsStringUtil.formatNumber((Number)obj, "00");
					}else{
						nbmavlo="0";
					}
//					nbmavlo = lste.get(0).toString();
					if (nbmavlo.equals("0"))
						avlo = " ";
					else
						avlo = "X";

					rubavno = service.findAnyColumnFromNomenclature(param.cdos, param.clang, "36", "NO", "2").getVall();
//					query = " Select ltrim(to_char(count(*),'00')) ";
					query = " Select count(*) ";
					query += " from CumulPaie";
					query += " where idEntreprise = :cdos ";
					query += "and rubq = :rubavno ";
					query += "and nmat = :nmat ";
					query += "and aamm between :aamm_deb and :aamm_fin and aamm not like '%99' ";
					q = session.createSQLQuery(query);
					q.setParameter("cdos", param.cdos);
					q.setParameter("nmat", nmat);
					q.setParameter("rubavno", rubavno);
					q.setParameter("aamm_deb", param.aamm_deb);
					q.setParameter("aamm_fin", param.aamm_fin);

					lste = q.list();
					obj=lste.get(0);
					if(obj instanceof Number){
						nbmavno=ClsStringUtil.formatNumber((Number)obj, "00");
					}else{
						nbmavno="0";
					}
//					nbmavno = lste.get(0).toString();
					if (nbmavno.equals("0"))
						avno = " ";
					else
						avno = "X";

					rubavel = service.findAnyColumnFromNomenclature(param.cdos, param.clang, "36", "EL", "2").getVall();
//					query = " Select ltrim(to_char(count(*),'00')) ";
					query = " Select count(*) ";
					query += " from CumulPaie";
					query += " where idEntreprise = :cdos ";
					query += "and rubq = :rubavel ";
					query += "and nmat = :nmat ";
					query += "and aamm between :aamm_deb and :aamm_fin and aamm not like '%99' ";
					q = session.createSQLQuery(query);
					q.setParameter("cdos", param.cdos);
					q.setParameter("nmat", nmat);
					q.setParameter("rubavel", rubavel);
					q.setParameter("aamm_deb", param.aamm_deb);
					q.setParameter("aamm_fin", param.aamm_fin);

					lste = q.list();
					obj=lste.get(0);
					if(obj instanceof Number){
						nbmavel=ClsStringUtil.formatNumber((Number)obj, "00");
					}else{
						nbmavel="0";
					}
//					nbmavel = lste.get(0).toString();
					if (nbmavel.equals("0"))
						avel = " ";
					else
						avel = "X";

					rubavdo = service.findAnyColumnFromNomenclature(param.cdos, param.clang, "36", "EL", "2").getVall();
//					query = " Select ltrim(to_char(count(*),'00')) ";
					query = " Select count(*) ";
					query += " from CumulPaie";
					query += " where idEntreprise = :cdos ";
					query += "and rubq = :rubavdo ";
					query += "and nmat = :nmat ";
					query += "and aamm between :aamm_deb and :aamm_fin and aamm not like '%99' ";
					q = session.createSQLQuery(query);
					q.setParameter("cdos", param.cdos);
					q.setParameter("nmat", nmat);
					q.setParameter("rubavdo", rubavdo);
					q.setParameter("aamm_deb", param.aamm_deb);
					q.setParameter("aamm_fin", param.aamm_fin);

					lste = q.list();
					obj=lste.get(0);
					if(obj instanceof Number){
						nbmavdo=ClsStringUtil.formatNumber((Number)obj, "00");
					}else{
						nbmavdo="0";
					}
//					nbmavdo = lste.get(0).toString();
					if (nbmavdo.equals("0"))
						avdo = " ";
					else
						avdo = "X";

					//avn = avlo+ nbmavlo + avno + nbmavno + avel + nbmavel + avdo + nbmavdo;
					avn = (StringUtils.isNotBlank(avlo) ||StringUtils.isNotBlank(avno) ||StringUtils.isNotBlank(avel) ||StringUtils.isNotBlank(avdo))  ?"O":"N";

					dtes_c = new ClsDate(a.getDtes()).getDateS("ddMM");
					if (StringUtils.equals(a.getMrrx(), "RA"))
						dtss_c = new ClsDate(a.getDmrr()).getDateS("ddMM");
					else
						dtss_c = "    ";

					// nom = StringUtil.oraRPad(a.getNom(), 20);
					// pren = StringUtil.oraRPad(a.getPren(), 15);

					// sitf = a.getSitf();
					// nbec = a.getNbec() != null ? a.getNbec().intValue() : 0;
					// nbpt = a.getNbpt() != null ? a.getNbpt().intValue() : 0;

					// typpers = "X";

					nlign = b.getLign();

					texteLigne = StringUtil.oraLPad(codenreg, 3) + StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(ndip)), 5, " ") + StringUtil.nvl(cndip, " ") + "13"
							+ StringUtil.oraLPad(StringUtil.oraLTrim(param.ncontribuable), 14) + StringUtil.oraLPad(StringUtil.oraLTrim(nemp), 10) + StringUtil.oraLPad(cnemp, 1)
							+ StringUtil.nvl(StringUtil.oraLTrim(StringUtil.oraToChar(rcnps)), " ") + /*param.afexe_c*/param.annee + StringUtil.oraLPad(StringUtil.oraLTrim(rdrsaltax), 7, "0")
							+ StringUtil.oraLPad(StringUtil.oraLTrim(rdrsalcot), 7, "0") + StringUtil.oraLPad(StringUtil.oraLTrim(rdrirpp), 7, "0") + StringUtil.oraLPad(StringUtil.oraLTrim(rdrtc), 7, "0") + avn + StringUtil.oraLPad(dtes_c, 4) + StringUtil.oraLPad(dtss_c, 4)
							+ StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(nlign)), 2, "0") + StringUtil.oraLPad(nass, 10) + StringUtil.oraLPad(cnass, 1)
							+ StringUtil.oraLPad(StringUtil.oraLPad(nmat, 7, "0"), 14, "0") + StringUtil.oraLPad(" ", 19);

					texteLigne2 = StringUtil.oraLPad(codenreg, 3) +separateur+ StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(ndip)), 5, " ") +separateur+ StringUtil.nvl(cndip, " ") +separateur+ "13"
					+separateur+ StringUtil.oraLPad(StringUtil.oraLTrim(param.ncontribuable), 14) +separateur+ StringUtil.oraLPad(StringUtil.oraLTrim(nemp), 10) +separateur+ StringUtil.oraLPad(cnemp, 1)
					+separateur+ StringUtil.nvl(StringUtil.oraLTrim(StringUtil.oraToChar(rcnps)), " ") +separateur+ /*param.afexe_c*/param.annee +separateur+ StringUtil.oraLPad(StringUtil.oraLTrim(rdrsaltax), 7, "0")
					+separateur+ StringUtil.oraLPad(StringUtil.oraLTrim(rdrsalcot), 7, "0") +separateur+ StringUtil.oraLPad(StringUtil.oraLTrim(rdrirpp), 7, "0") +separateur+ StringUtil.oraLPad(StringUtil.oraLTrim(rdrtc), 7, "0") +separateur+ avn +separateur+ StringUtil.oraLPad(dtes_c, 4) +separateur+ StringUtil.oraLPad(dtss_c, 4)
					+separateur+ StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(nlign)), 2, "0") +separateur+ StringUtil.oraLPad(nass, 10) +separateur+ StringUtil.oraLPad(cnass, 1)
					+separateur+ StringUtil.oraLPad(StringUtil.oraLPad(nmat, 7, "0"), 14, "0") +separateur+ StringUtil.oraLPad(" ", 19);

					bufferedWriter.println(texteLigne);
					bufferedWriter.flush();

					bufferedWriter2.println(texteLigne2);
					bufferedWriter.flush();
				}
				bufferedWriter.close();
				bufferedWriter2.close();

			}
			catch (IOException e)
			{
				//LOG.error(e.getLocalizedMessage(),e);
				return ClsTreater._getResultat( ClsTreater._getStackTrace(e), null, true);
			}
		}
		catch (Exception e)
		{
			//LOG.error(e.getLocalizedMessage(),e);
			return ClsTreater._getResultat( ClsTreater._getStackTrace(e), null, true);
		}
		finally
		{
			service.closeSession(session);
		}
		return null;
	}

	private ClsResultat genererDipeTemporaire()
	{
		String codenreg;
		Integer ndip;
		String cndip;
		Integer nfeuil;
		String nemp;
		String cnemp;
		Integer rcnps;
		String nass;
		String cnass;
		Integer nbj;
		BigDecimal salbrut;

		phase = "G�n�ration du fichier " + param.cheminDipeTemporaire;
		//engine.incrementerNombreSalariesTraite(0);

		param.codenreg = "C07";
		codenreg = param.codenreg;

		param.adexe_c = new ClsDate(param.ddex).getYear() + "";

		String c_sal_temp = " SELECT   a.*, b.*  ";
		c_sal_temp += "FROM Salarie a  JOIN Dipe b ON (b.idEntreprise = a.idEntreprise AND b.nmat = a.nmat AND (b.annee=:aamm or (b.annee!=:aamm and b.annee = :annee)))";
		c_sal_temp += "WHERE a.idEntreprise = :cdos ";
//		c_sal_temp += "AND b.idEntreprise = a.idEntreprise ";
//		c_sal_temp += "AND b.nmat = a.nmat ";
//		c_sal_temp += "AND b.annee = :annee ";
		c_sal_temp += "AND LTRIM (a.vild) IS NOT NULL ";
		if (StringUtils.isNotBlank(param.ville1))
			c_sal_temp += "AND a.vild >= :ville1 ";
		if (StringUtils.isNotBlank(param.ville2))
			c_sal_temp += "AND a.vild <= :ville2 ";
		c_sal_temp += "AND exists (SELECT 'x' ";
		c_sal_temp += "FROM CumulPaie c ";
		c_sal_temp += "WHERE c.idEntreprise = a.idEntreprise AND c.aamm = :aamm and c.nmat = a.nmat) ";
		c_sal_temp += " AND a.GRAD in (select cacc from ParamData where cdos=:cdos and ctab='6' and nume=1 and valm=3) ";
		c_sal_temp += "AND (a.mrrx != 'RA' or a.mrrx is null or (a.dmrr > :dfex and a.dmrr is not null)) ";
		c_sal_temp += "ORDER BY a.vild, a.nmat ";

		Session session = service.getSession();

		try
		{
			Query q = session.createSQLQuery(c_sal_temp).addEntity("a", Salarie.class).addEntity("b", Dipe.class);

			q.setParameter("cdos", param.cdos);
			q.setParameter("aamm", param.aamm);
			if (StringUtils.isNotBlank(param.ville1))
				q.setParameter("ville1", param.ville1);
			if (StringUtils.isNotBlank(param.ville2))
				q.setParameter("ville2", param.ville2);
			q.setParameter("dfex", param.dfex, StandardBasicTypes.DATE);
			q.setParameter("annee", param.aamm);

			this.traitement = "Chargement des salari�s � traiter, veuillez patienter";
			//progression.setEvolutiontraitement(this.traitement,false);
			//this.engine.incrementerNombreSalariesTraite(0);
			List<Object[]> lst = q.list();

			if (lst.size() == 0)
			{
				this.traitement = " Aucun Salari� � traiter ";
				//this.engine.incrementerNombreSalariesTraite(0);
				//progression.setEvolutiontraitement(traitement, true);
				return null;
			}

			Integer nbrLigne = lst.size();
			//progression.debutAffichageProgressBar(nbrLigne);
			if (nbrLigne > 0)
				//progression.mettreAJourProgressBar(0);

			// Suppression du fichier
			param.deleteFileToGenerate();

			Salarie a;
			Dipe b;
			Salarie ancien_a;
			String nmat = null;
			String ancien_nmat = null;
			ClsVarDipes var = null;
			String texteLigne = StringUtils.EMPTY;
			String texteLigne2 = StringUtils.EMPTY;

			try
			{
				PrintWriter bufferedWriter = new PrintWriter(new FileWriter(param.cheminDipeTemporaire));
				PrintWriter bufferedWriter2 = new PrintWriter(new FileWriter(param.cheminDipeTemporaire2));
				int j=0;
				for (int i = 0; i < lst.size(); i++)
				{
					j=i;
					a = (Salarie) lst.get(i)[0];
					b = (Dipe) lst.get(i)[1];
					nmat = a.getNmat();
					this.traitement = "Matricule Courant "+nmat+" - "+a.getNom()+" "+StringUtil.nvl(a.getPren(),"");
					//progression.setEvolutiontraitement(traitement);
					if (i > 0)
					{
						ancien_a = (Salarie) lst.get(i - 1)[0];
						ancien_nmat = ancien_a.getNmat();
					}
					if (StringUtil.notEquals(ancien_nmat, nmat))
						//engine.incrementerNombreSalariesTraite();
						//progression.mettreAJourProgressBar(++j);

					if (StringUtils.isBlank(a.getVild()))
						return ClsTreater._getResultat( "Ville non d�clar�e pour le salari� " + nmat + " " + a.getNom() + " " + StringUtil.nvl(a.getPren(), " "), null, true);

					this.infoVille(a.getVild());
					var = this.calculSalaire(session, a);
					ndip = b.getNume();
					cndip = b.getLett();
					nfeuil = param.feuille;
					// --voir le changement de matricule
					// --enlever les tiret
					// --N� sur 11 position sans les tirets
					nemp = StringUtil.oraLPad(StringUtils.replace(StringUtil.nvl(StringUtils.replace(StringUtil.nvl(param.numCompte, " "), "-", ""), " "), " ", ""), 11, "0");
					nemp = StringUtil.oraSubstring(nemp, 1, 10);

					// --Le dernier caractere represente la cl�
					cnemp = StringUtil.lastCharacter(param.numCompte);
					rcnps = param.regCNPS.intValue();

					if (StringUtils.isNotBlank(a.getNoss()))
					{
						nass = StringUtils.replace(StringUtil.nvl(a.getNoss(), " "), "-", "");// -- retirer le tiret
						nass = StringUtils.replace(StringUtil.nvl(nass, " "), " ", "");// -- retirer les espaces
						nass = StringUtil.oraLPad(nass, 11, "0");
						nass = StringUtil.oraSubstring(nass, 1, 10);// -- prendre les 10 premiers
						nass = StringUtil.oraLPad(nass, 10, "0"); // -- completer les anciens matricules avec des z�ros
						// --Le dernier caractere represente la cl�
						cnass = StringUtil.lastCharacter(StringUtil.nvl(a.getNoss(), " "));
					}
					else
					{
						nass = "          ";// -- 10 espace
						cnass = " ";
					}

					if (var.nbj.compareTo(new BigDecimal(99)) <= 0)
						nbj = var.nbj.intValue();
					else
						return ClsTreater._getResultat( "Nb de jour > 99 !", null, true);

					salbrut = var.salBrut;
					salbrut = salbrut.setScale(0);
					// eltexcep = var.elExcep;
					// saltax = var.salTax;
					// salcottot = var.totCnp;
					// salcotplaf = var.plafCnp;
					// rettp = var.retTax;
					// --retsp =var.ret_surt;
					// --retenue surtaxe prog devient l'irpp
					// retirpp = var.retSurt;
					// retsp = var.retSurt;
					// rettc = var.retTxco;
					// nlign = b.getLign();

					texteLigne =

					StringUtil.oraLPad(codenreg, 3) + StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(ndip)), 5, " ") + StringUtil.nvl(cndip, " ")
							+ StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(nfeuil)), 2, "0") + StringUtil.oraLPad(StringUtil.oraLTrim(nemp), 10) + StringUtil.oraRPad(cnemp, 1)
							+ StringUtil.nvl(StringUtil.oraLTrim(StringUtil.oraToChar(rcnps)), " ") + new ClsDate(param.aamm, "yyyyMM").getDateS("yy") + "150300" + "01"
							+ new ClsDate(param.aamm, "yyyyMM").getDateS("MMyy") + "C" + StringUtil.oraRPad(nass, 10) + StringUtil.oraRPad(cnass, 1) + new ClsDate(param.aamm, "yyyyMM").getDateS("ddMM") + "S"
							+ StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(salbrut)), 7, "0") + StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(nbj)), 2, "0")
							+ StringUtil.oraLPad(" ", 72);

					texteLigne2 =
						StringUtil.oraLPad(codenreg, 3) +separateur+ StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(ndip)), 5, " ") +separateur+ StringUtil.nvl(cndip, " ")
								+separateur+ StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(nfeuil)), 2, "0") +separateur+ StringUtil.oraLPad(StringUtil.oraLTrim(nemp), 10) +separateur+ StringUtil.oraRPad(cnemp, 1)
								+separateur+ StringUtil.nvl(StringUtil.oraLTrim(StringUtil.oraToChar(rcnps)), " ") +separateur+ new ClsDate(param.aamm, "yyyyMM").getDateS("yy") +separateur+ "150300" +separateur+ "01"
								+separateur+ new ClsDate(param.aamm, "yyyyMM").getDateS("MMyy") +separateur+ "C" +separateur+ StringUtil.oraRPad(nass, 10) +separateur+ StringUtil.oraRPad(cnass, 1) +separateur+ new ClsDate(param.aamm, "yyyyMM").getDateS("ddMM") +separateur+ "S"
								+separateur+ StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(salbrut)), 7, "0") +separateur+ StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(nbj)), 2, "0")
								+separateur+ StringUtil.oraLPad(" ", 72);

					bufferedWriter.println(texteLigne);
					bufferedWriter.flush();

					bufferedWriter2.println(texteLigne2);
					bufferedWriter.flush();
				}
				bufferedWriter.close();
				bufferedWriter2.close();
			}
			catch (IOException e)
			{
				//LOG.error(e.getLocalizedMessage(),e);
				return ClsTreater._getResultat( ClsTreater._getStackTrace(e), null, true);
			}
		}
		catch (Exception e)
		{
			//LOG.error(e.getLocalizedMessage(),e);
			return ClsTreater._getResultat( ClsTreater._getStackTrace(e), null, true);
		}
		finally
		{
			service.closeSession(session);
		}
		return null;
	}
	private ClsResultat genererDipeCD10()
	{
		String nato;
		String codenreg;
		Integer ndip;
		String cndip;
		String nemp;
		String cnemp;
		Integer rcnps;
		String nass;
		String cnass;
		Integer nlign;
		String sexe;
		String datnais;
		String nom;
		String pren;
		String sitf;
		Integer nbec;
		Integer nbpt;
		String typpers;
		String cat;
		//Integer typact;
		String dtes_c;
		String dtes;
		String session_id = null;
		ClsJournalPaieGlobal jp = new ClsJournalPaieGlobal();
		jp.setService(service);
		try {
			phase = "Calcul des formules ";
			this.traitement = "Calcul des formules, veuillez patienter";
			//progression.setEvolutiontraitement(this.traitement,false);

			jp.initJournalPaie(param.cdos, param.cuti, ClsObjectUtil.getIntegerFromObject(831), null, null, null, null, null, null, null, null, null, ClsDate.getDateS(param.getDdex(), "yyyyMM"),
					 ClsDate.getDateS(param.getDfex(), "yyyyMM"), param.nbul, 0, "1", "1", "1",null);

			session_id = jp.genererJournalPaieCumule(param.cdos, param.cuti,  ClsObjectUtil.getIntegerFromObject(831), null, null, null, null, null, null, null, null, null, ClsDate.getDateS(param.getDdex(), "yyyyMM"),
					 ClsDate.getDateS(param.getDfex(), "yyyyMM"), param.nbul, 0, "1", "1", "1");

		} catch (Exception e1) {
			//LOG.error(e1.getLocalizedMessage(),e1);
		}

		phase = "G�n�ration du fichier " + param.cheminDipeCD10;
		//engine.incrementerNombreSalariesTraite(0);
		//dos.nred,dos.bp,a.vild,dos.numcontrib,a.nom,a.pren,a.sitf,a.nbef,dos.ddex,dos.ffex,dip.nume,dip.lg,a.noss,cum*

		param.codenreg = "CD10";
		codenreg = param.codenreg;

		String c_sal_emb = " SELECT   a.*, b.*,c.*,d.*,lig1.*,lig2.*  ";
		c_sal_emb += " FROM Salarie a  JOIN Dipe b ON (b.idEntreprise = a.idEntreprise AND b.nmat = a.nmat AND (b.annee=:aamm or (b.annee!=:aamm and b.annee = :annee)))";
		c_sal_emb += " , DossierPaie c, DossierPaie d, ";
		c_sal_emb += " JournalPaie lig1,  ";
		c_sal_emb += " JournalPaie lig2 ";
		c_sal_emb += "  WHERE a.idEntreprise = :cdos ";
//		c_sal_emb += " AND b.idEntreprise = a.idEntreprise ";
//		c_sal_emb += " AND b.nmat = a.nmat ";
//		c_sal_emb += "AND b.annee = :annee ";

		c_sal_emb += " AND lig1.idEntreprise = a.idEntreprise ";
		c_sal_emb += " AND lig1.nmat = a.nmat ";
		c_sal_emb += "AND lig1.SESSIONID = :sessionid ";
		c_sal_emb += "AND lig1.nume = 1";

		c_sal_emb += " AND lig2.idEntreprise = a.idEntreprise ";
		c_sal_emb += " AND lig2.nmat = a.nmat ";
		c_sal_emb += "AND lig2.SESSIONID = :sessionid2 ";
		c_sal_emb += "AND lig2.nume = 2";



		c_sal_emb += " AND c.idEntreprise = a.idEntreprise ";
		c_sal_emb += " AND d.idEntreprise = a.idEntreprise ";
		c_sal_emb += " AND LTRIM (a.vild) IS NOT NULL ";
		if (StringUtils.isNotBlank(param.ville1))
			c_sal_emb += "AND a.vild >= :ville1 ";
		if (StringUtils.isNotBlank(param.ville2))
			c_sal_emb += "AND a.vild <= :ville2 ";
		c_sal_emb += "AND exists(SELECT 'x' ";
		c_sal_emb += "FROM CumulPaie c ";
		c_sal_emb += "WHERE c.idEntreprise = a.idEntreprise ";
		c_sal_emb += "AND " + TypeBDUtil.souschaine("c.aamm", 1, 4) + "  = :aaaa and c.nmat = a.nmat)  ";
		c_sal_emb += " ORDER BY a.vild, a.nmat ";

		Session session = service.getSession();

		try
		{

			Query q = session.createSQLQuery(c_sal_emb).addEntity("a", Salarie.class).addEntity("b", Dipe.class).addEntity("c", DossierPaie.class).addEntity("d", DossierPaie.class).
			addEntity("lig1", JournalPaie.class).addEntity("lig2", JournalPaie.class);

			q.setParameter("cdos", param.cdos);
			q.setParameter("aaaa", param.annee);
			if (StringUtils.isNotBlank(param.ville1))
				q.setParameter("ville1", param.ville1);
			if (StringUtils.isNotBlank(param.ville2))
				q.setParameter("ville2", param.ville2);
			q.setParameter("annee", param.aamm);
			q.setParameter("aamm", param.aamm);
			q.setParameter("sessionid", session_id);
			q.setParameter("sessionid2", session_id);

			this.traitement = "Chargement des salari�s � traiter, veuillez patienter";
			//progression.setEvolutiontraitement(this.traitement,false);
			//this.engine.incrementerNombreSalariesTraite(0);
			List<Object[]> lst = q.list();
			List<Object[]> lst2=new ArrayList<Object[]>();
			for (Object[]item : lst)
			{
				Salarie a = (Salarie) item[0];
				lst2.add(item);
//				if(ClsDate.getDateS(a.getDtes(), "yyyyMM").equalsIgnoreCase(param.aamm)){
//					lst2.add(item);
//				}
			}
			lst.clear();
			lst.addAll(lst2);

			if (lst.size() == 0)
			{
				this.traitement = " Aucun Salari� � traiter ";
				//this.engine.incrementerNombreSalariesTraite(0);
				//progression.setEvolutiontraitement(traitement, true);
				return null;
			}

			Integer nbrLigne = lst.size();
			//progression.debutAffichageProgressBar(nbrLigne);
			//if (nbrLigne > 0)
				//progression.mettreAJourProgressBar(0);

			ClsGenericPrintObject data = null;
			List<ClsGenericPrintObject> file = new ArrayList<ClsGenericPrintObject>();
			 String[][] infos = null;
			 Integer[][] position = null;
			DossierPaie c = null;DossierPaie d = null;JournalPaie lig1 = null,lig2 = null;
			 Salarie ancien_a,a;Dipe b;String ancien_nmat = "",nmat;

			try
			{

				int j=0;
				for (int i = 0; i < lst.size(); i++)
				{
					j = i;
					//progression.mettreAJourProgressBar(++j);
					a = (Salarie) lst.get(i)[0];
					b = (Dipe) lst.get(i)[1];
					c = (DossierPaie) lst.get(i)[2];
					d = (DossierPaie) lst.get(i)[3];
					lig1 = (JournalPaie) lst.get(i)[4];
					lig2 = (JournalPaie) lst.get(i)[5];
					nmat = a.getNmat();
					this.traitement = "Matricule Courant "+nmat+" - "+a.getNom()+" "+StringUtil.nvl(a.getPren(),"");
					//progression.setEvolutiontraitement(traitement);
					if (i > 0)
					{
						ancien_a = (Salarie) lst.get(i - 1)[0];
						ancien_nmat = ancien_a.getNmat();
					}
					if (StringUtil.notEquals(ancien_nmat, nmat))
						//engine.incrementerNombreSalariesTraite();
						//progression.mettreAJourProgressBar(++j);

					if (StringUtils.isBlank(a.getVild()))
						return ClsTreater._getResultat( "Ville non d�clar�e pour le salari� " + nmat + " " + a.getNom() + " " + StringUtil.nvl(a.getPren(), " "), null, true);

					this.infoVille(a.getVild());
					// var = this.calculSalaire(a);
					ndip = b.getNume();
					cndip = b.getLett();
					// nfeuil = param.feuille;

					nemp = StringUtil.oraLPad(StringUtils.replace(StringUtil.nvl(param.numCompte, " "), "-", ""), 11, "0");
					nemp = StringUtil.oraSubstring(nemp, 1, 10);
					rcnps = param.regCNPS.intValue();
					// --Le dernier caractere represente la cl�
					cnemp = StringUtil.lastCharacter(param.numCompte);

					if (StringUtils.isNotBlank(a.getNoss()))
					{
						nass = StringUtils.replace(StringUtil.nvl(a.getNoss(), " "), "-", "");// -- retirer le tiret
						nass = StringUtils.replace(StringUtil.nvl(nass, " "), " ", "");// -- retirer les espaces
						nass = StringUtil.oraLPad(nass, 11, "0");
						nass = StringUtil.oraSubstring(nass, 1, 10);// -- prendre les 10 premiers
						nass = StringUtil.oraLPad(nass, 10, "0"); // -- completer les anciens matricules avec des z�ros
						// --Le dernier caractere represente la cl�
						cnass = StringUtil.lastCharacter(StringUtil.nvl(a.getNoss(), " "));
					}
					else
					{
						nass = "          ";// -- 10 espace
						cnass = " ";
					}

					cat = StringUtil.oraSubstring(a.getCat(), 1, 2);

					//typact = new Integer(StringUtil.nvl(StringUtil.oraSubstring(service.findAnyColumnFromNomenclature(param.cdos, param.clang, "7", a.getFonc(), "3").getVall(), 1, 3),"0"));

					sexe = a.getSexe();

					nato = StringUtil.nvl(service.findAnyColumnFromNomenclature(param.cdos, param.clang, "4", a.getNato(), "2").getVall(), " ");
					nato = StringUtil.oraLPad(nato, 3);

					datnais = new ClsDate(a.getDtna()).getDateS("ddMMyyyy");
					nom = StringUtil.oraRPad(a.getNom(), 20);
					nom=StringUtil.oraSubstring(nom, 1, 20);
					pren = StringUtil.oraRPad(StringUtils.isBlank(a.getPren())?StringUtil.getEmptyString(15):a.getPren(), 15);
					pren=StringUtils.substring(pren, 0, 15);

					sitf = a.getSitf();
					nbec = a.getNbec() != null ? a.getNbec().intValue() : 0;
					nbpt = a.getNbpt() != null ? a.getNbpt().intValue() : 0;
					//sitf = sitf + StringUtil.oraLTrim(ClsStringUtil.formatNumber(nbec, "00")) 	+ StringUtil.oraLTrim(ClsStringUtil.formatNumber(nbpt, "000"));
					dtes = new ClsDate(a.getDtes()).getDateS("ddMMyyyy");
					dtes_c = new ClsDate(a.getDtes()).getDateS("yyyy");

					typpers = "X";
					//R�cup�rer les lignes du tableau



					nlign = b.getLign();

					data = new ClsGenericPrintObject(50,20);
					infos = (String[][])data.getInfos();
					position =  (Integer[][])data.getPosition();

					infos[0][0] = ClsDate.getDateS(c.getDdex(), "yyyy");position[0][0] = 126;
					infos[0][1] = ClsDate.getDateS(c.getDdex(), "yyyy");position[0][1] = 152;
					infos[1][0] = c.getNred();position[1][0] = 8;
					infos[1][1] = c.getDad1();position[1][1] = 36;
					infos[1][2] = c.getDad2();position[1][2] = 48;
					infos[1][3] = d.getDnii();position[1][3] = 112;
					infos[1][4] = ndip+"";position[1][4] = 147;
					infos[1][5] = nlign+"";position[1][5] = 155;
					infos[2][0] = "";position[2][0] = 146;
					infos[3][0] = nom;position[3][0] = 0;
					infos[3][1] = pren;position[3][1] = 20;
					infos[3][2] = sitf;position[3][2] = 36;
					infos[3][3] = nbec>1?nbec + " ENFANTS":" ENFANT";position[3][3] = 55;
					infos[3][4] = ClsDate.getDateS(c.getDdex(), "MM/yyyy");position[3][4] = 69;
					infos[3][5] = ClsDate.getDateS(c.getDfex(), "MM/yyyy");position[3][5] = 82;
					infos[3][6] = ndip+"";position[3][6] = 105;
					infos[3][7] = nlign+"";position[3][7] = 112;
					infos[3][8] = nom;position[3][8] = 140;
					infos[4][0] = pren;position[4][0] = 148;
					infos[5][0] = a.getNoss();position[5][0] = 146;
					infos[6][0] = "";position[6][0] = 0;
					infos[7][0] = "";position[7][0] = 0;
					infos[8][0] = "";position[8][0] = 0;


					infos[9][0] = lig1.getCol1();position[9][0] = 42;
					infos[9][1] = lig1.getCol2();position[9][1] = 59;
					infos[9][2] = lig1.getCol3();position[9][2] = 76;
					infos[9][3] = lig1.getCol4();position[9][3] = 92;
					infos[9][4] = lig1.getCol5();position[9][4] = 108;
					infos[9][5] = lig1.getCol6();position[9][5] = 124;
					infos[9][6] = c.getNred();position[9][6] = 140;


					infos[10][0] = "";position[10][0] = 100;
					infos[11][0] = d.getDnii();position[11][0] = 130;

					infos[12][0] = lig2.getCol1();position[12][0] = 42;
					infos[12][1] = lig2.getCol2();position[12][1] = 59;
					infos[12][2] = lig2.getCol3();position[12][2] = 76;
					infos[12][3] = lig2.getCol4();position[12][3] = 92;
					infos[12][4] = lig2.getCol5();position[12][4] = 108;
					infos[12][5] = lig2.getCol6();position[12][5] = 124;

					infos[13][0] = "";position[13][0] = 100;
					infos[14][0] = "";position[14][0] = 100;
					infos[15][0] = "";position[15][0] = 100;
					infos[16][0] = "";position[16][0] = 100;


					infos[17][0] = a.getNiv1();position[17][0] = 6;
					infos[17][1] = a.getNiv2();position[17][1] = 13;
					infos[17][2] = a.getNmat();position[17][2] = 21;
					infos[17][3] = a.getNiv3();position[17][3] = 36;
					infos[17][4] = a.getNiv1();position[17][4] = 138;
					infos[17][5] = a.getNiv2();position[17][5] = 145;
					infos[17][6] = a.getNmat();position[17][6] = 150;
					infos[17][7] = a.getNiv3();position[17][7] = 159;

					data.setInfos(infos);
					data.setPosition(position);
					file.add(data);


//					texteLigne =
//
//					StringUtil.oraLPad(codenreg, 3) + StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(ndip)), 5, " ") + StringUtil.nvl(cndip, " ")
//							+ StringUtil.oraLPad(StringUtil.oraLTrim("13"), 2, "0") + StringUtil.oraLPad(StringUtil.oraLTrim(nemp), 10) + StringUtil.oraLPad(cnemp, 1)
//							+ StringUtil.nvl(StringUtil.oraLTrim(StringUtil.oraToChar(rcnps)), " ") + dtes_c + StringUtil.oraLPad(cat, 2) + StringUtil.nvl(ClsStringUtil.formatNumber(param.typact,"000"), "000") + sexe + nato
//							+ datnais + StringUtil.oraLPad(nass, 10) + StringUtil.oraLPad(cnass, 1) + nom + pren + sitf + StringUtil.oraLPad(StringUtil.oraSubstring(dtes, 3, 2), 2) + typpers
//							+ StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(nlign)), 2, "0") + StringUtil.oraLPad(nmat, 14, " ") + StringUtil.oraLPad(" ", 29);
				}
				EditionEngine engine = new EditionEngine();
				engine.setPrintPath(param.cheminDipeCD10);
				engine.generateFile(file);

			}
			catch (Exception e)
			{
				//LOG.error(e.getLocalizedMessage(),e);
				return ClsTreater._getResultat( ClsTreater._getStackTrace(e), null, true);
			}
		}
		catch (Exception e)
		{
			//LOG.error(e.getLocalizedMessage(),e);
			return ClsTreater._getResultat( ClsTreater._getStackTrace(e), null, true);
		}
		finally
		{
			service.closeSession(session);
		}
		return null;
	}

	private ClsResultat genererDipeEmbauche()
	{
		String nato;
		String codenreg;
		Integer ndip;
		String cndip;
		String nemp;
		String cnemp;
		Integer rcnps;
		String nass;
		String cnass;
		Integer nlign;
		String sexe;
		String datnais;
		String nom;
		String pren;
		String sitf;
		Integer nbec;
		Integer nbpt;
		String typpers;
		String cat;
		//Integer typact;
		String dtes_c;
		String dtes;

		phase = "G�n�ration du fichier " + param.cheminDipeEmbauche;
		//engine.incrementerNombreSalariesTraite(0);

		param.codenreg = "C15";
		codenreg = param.codenreg;

		String c_sal_emb = " SELECT   a.*, b.* ";
		c_sal_emb += " FROM Salarie a  JOIN Dipe b ON (b.idEntreprise = a.idEntreprise AND b.nmat = a.nmat AND (b.annee=:aamm or (b.annee!=:aamm and b.annee = :annee)))";
		c_sal_emb += "  WHERE a.idEntreprise = :cdos ";
//		c_sal_emb += " AND b.idEntreprise = a.idEntreprise ";
//		c_sal_emb += " AND b.nmat = a.nmat ";
//		c_sal_emb += "AND b.annee = :annee ";
		c_sal_emb += " AND LTRIM (a.vild) IS NOT NULL ";
		if (StringUtils.isNotBlank(param.ville1))
			c_sal_emb += "AND a.vild >= :ville1 ";
		if (StringUtils.isNotBlank(param.ville2))
			c_sal_emb += "AND a.vild <= :ville2 ";
		c_sal_emb += "AND exists (SELECT 'x' ";
		c_sal_emb += "FROM CumulPaie c ";
		c_sal_emb += "WHERE c.idEntreprise = a.idEntreprise AND c.aamm = :aamm and c.nmat = a.nmat) ";
//		c_sal_emb += " AND to_char(a.dtes,'yyyymm') = :aamm ";
		c_sal_emb += " AND (a.mrrx != 'RA' or a.mrrx is null or (a.dmrr > :dfex and a.dmrr is not null)) ";
		c_sal_emb += " ORDER BY a.vild, a.nmat ";

		Session session = service.getSession();

		try
		{

			Query q = session.createSQLQuery(c_sal_emb).addEntity("a", Salarie.class).addEntity("b", Dipe.class);

			q.setParameter("cdos", param.cdos);
			q.setParameter("aamm", param.aamm);
			if (StringUtils.isNotBlank(param.ville1))
				q.setParameter("ville1", param.ville1);
			if (StringUtils.isNotBlank(param.ville2))
				q.setParameter("ville2", param.ville2);
			q.setParameter("dfex", param.dfex, StandardBasicTypes.DATE);
			q.setParameter("annee", param.annee);

			this.traitement = "Chargement des salari�s � traiter, veuillez patienter";
			//progression.setEvolutiontraitement(this.traitement,false);
			//this.engine.incrementerNombreSalariesTraite(0);
			List<Object[]> lst = q.list();
			List<Object[]> lst2=new ArrayList<Object[]>();
			for (Object[]item : lst)
			{
				Salarie a = (Salarie) item[0];
				if(ClsDate.getDateS(a.getDtes(), "yyyyMM").equalsIgnoreCase(param.aamm)){
					lst2.add(item);
				}
			}
			lst.clear();
			lst.addAll(lst2);

			if (lst.size() == 0)
			{
				this.traitement = " Aucun Salari� � traiter ";
				//this.engine.incrementerNombreSalariesTraite(0);
				//progression.setEvolutiontraitement(traitement, true);
				return null;
			}

			Integer nbrLigne = lst.size();
			//progression.debutAffichageProgressBar(nbrLigne);
			if (nbrLigne > 0)
				//progression.mettreAJourProgressBar(0);

			// Suppression du fichier
			param.deleteFileToGenerate();

			Salarie a;
			Dipe b;
			Salarie ancien_a;
			String nmat = null;
			String ancien_nmat = null;
			// ClsVarDipes var = null;
			String texteLigne = StringUtils.EMPTY;
			String texteLigne2 = StringUtils.EMPTY;

			try
			{
				PrintWriter bufferedWriter = new PrintWriter(new FileWriter(param.cheminDipeEmbauche));
				PrintWriter bufferedWriter2 = new PrintWriter(new FileWriter(param.cheminDipeEmbauche2));
				int j=0;
				for (int i = 0; i < lst.size(); i++)
				{
					j=i;
					a = (Salarie) lst.get(i)[0];
					b = (Dipe) lst.get(i)[1];
					nmat = a.getNmat();
					this.traitement = "Matricule Courant "+nmat+" - "+a.getNom()+" "+StringUtil.nvl(a.getPren(),"");
					//progression.setEvolutiontraitement(traitement);
					if (i > 0)
					{
						ancien_a = (Salarie) lst.get(i - 1)[0];
						ancien_nmat = ancien_a.getNmat();
					}
					if (StringUtil.notEquals(ancien_nmat, nmat))
						//engine.incrementerNombreSalariesTraite();
						//progression.mettreAJourProgressBar(++j);

					if (StringUtils.isBlank(a.getVild()))
						return ClsTreater._getResultat( "Ville non d�clar�e pour le salari� " + nmat + " " + a.getNom() + " " + StringUtil.nvl(a.getPren(), " "), null, true);

					this.infoVille(a.getVild());
					// var = this.calculSalaire(a);
					ndip = b.getNume();
					cndip = b.getLett();
					// nfeuil = param.feuille;

					nemp = StringUtil.oraLPad(StringUtils.replace(StringUtil.nvl(param.numCompte, " "), "-", ""), 11, "0");
					nemp = StringUtil.oraSubstring(nemp, 1, 10);
					rcnps = param.regCNPS.intValue();
					// --Le dernier caractere represente la cl�
					cnemp = StringUtil.lastCharacter(param.numCompte);

					if (StringUtils.isNotBlank(a.getNoss()))
					{
						nass = StringUtils.replace(StringUtil.nvl(a.getNoss(), " "), "-", "");// -- retirer le tiret
						nass = StringUtils.replace(StringUtil.nvl(nass, " "), " ", "");// -- retirer les espaces
						nass = StringUtil.oraLPad(nass, 11, "0");
						nass = StringUtil.oraSubstring(nass, 1, 10);// -- prendre les 10 premiers
						nass = StringUtil.oraLPad(nass, 10, "0"); // -- completer les anciens matricules avec des z�ros
						// --Le dernier caractere represente la cl�
						cnass = StringUtil.lastCharacter(StringUtil.nvl(a.getNoss(), " "));
					}
					else
					{
						nass = "          ";// -- 10 espace
						cnass = " ";
					}

					cat = StringUtil.oraSubstring(a.getCat(), 1, 2);

					//typact = new Integer(StringUtil.nvl(StringUtil.oraSubstring(service.findAnyColumnFromNomenclature(param.cdos, param.clang, "7", a.getFonc(), "3").getVall(), 1, 3),"0"));

					sexe = a.getSexe();

					nato = StringUtil.nvl(service.findAnyColumnFromNomenclature(param.cdos, param.clang, "4", a.getNato(), "2").getVall(), " ");
					nato = StringUtil.oraLPad(nato, 3);

					datnais = new ClsDate(a.getDtna()).getDateS("ddMMyyyy");
					nom = StringUtil.oraRPad(a.getNom(), 20);
					nom=StringUtil.oraSubstring(nom, 1, 20);
					pren = StringUtil.oraRPad(StringUtils.isBlank(a.getPren())?StringUtil.getEmptyString(15):a.getPren(), 15);
					pren=StringUtils.substring(pren, 0, 15);

					sitf = a.getSitf();
					nbec = a.getNbec() != null ? a.getNbec().intValue() : 0;
					nbpt = a.getNbpt() != null ? a.getNbpt().intValue() : 0;
					//sitf = sitf + StringUtil.oraLTrim(ClsStringUtil.formatNumber(nbec, "00")) 	+ StringUtil.oraLTrim(ClsStringUtil.formatNumber(nbpt, "000"));
					dtes = new ClsDate(a.getDtes()).getDateS("ddMMyyyy");
					dtes_c = new ClsDate(a.getDtes()).getDateS("yyyy");

					typpers = "X";

					nlign = b.getLign();

					texteLigne =

					StringUtil.oraLPad(codenreg, 3) + StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(ndip)), 5, " ") + StringUtil.nvl(cndip, " ")
							+ StringUtil.oraLPad(StringUtil.oraLTrim("13"), 2, "0") + StringUtil.oraLPad(StringUtil.oraLTrim(nemp), 10) + StringUtil.oraLPad(cnemp, 1)
							+ StringUtil.nvl(StringUtil.oraLTrim(StringUtil.oraToChar(rcnps)), " ") + dtes_c + StringUtil.oraLPad(cat, 2) + StringUtil.nvl(ClsStringUtil.formatNumber(param.typact,"000"), "000") + sexe + nato
							+ datnais + StringUtil.oraLPad(nass, 10) + StringUtil.oraLPad(cnass, 1) + nom + pren + sitf + StringUtil.oraLPad(StringUtil.oraSubstring(dtes, 3, 2), 2) + typpers
							+ StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(nlign)), 2, "0") + StringUtil.oraLPad(nmat, 14, " ") + StringUtil.oraLPad(" ", 29);

					texteLigne2 =

						StringUtil.oraLPad(codenreg, 3) +separateur+ StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(ndip)), 5, " ") +separateur+ StringUtil.nvl(cndip, " ")
								+separateur+ StringUtil.oraLPad(StringUtil.oraLTrim("13"), 2, "0") +separateur+ StringUtil.oraLPad(StringUtil.oraLTrim(nemp), 10) +separateur+ StringUtil.oraLPad(cnemp, 1)
								+separateur+ StringUtil.nvl(StringUtil.oraLTrim(StringUtil.oraToChar(rcnps)), " ") +separateur+ dtes_c +separateur+ StringUtil.oraLPad(cat, 2) +separateur+ StringUtil.nvl(ClsStringUtil.formatNumber(param.typact,"000"), "000") +separateur+ sexe +separateur+ nato
								+separateur+ datnais +separateur+ StringUtil.oraLPad(nass, 10) +separateur+ StringUtil.oraLPad(cnass, 1) +separateur+ nom +separateur+ pren +separateur+ sitf +separateur+ StringUtil.oraLPad(StringUtil.oraSubstring(dtes, 3, 2), 2) +separateur+ typpers
								+separateur+ StringUtil.oraLPad(StringUtil.oraLTrim(StringUtil.oraToChar(nlign)), 2, "0") +separateur+ StringUtil.oraLPad(nmat, 14, " ") +separateur+ StringUtil.oraLPad(" ", 29);

					bufferedWriter.println(texteLigne);
					bufferedWriter.flush();

					bufferedWriter2.println(texteLigne2);
					bufferedWriter.flush();
				}
				bufferedWriter.close();
				bufferedWriter2.close();

			}
			catch (IOException e)
			{
				//LOG.error(e.getLocalizedMessage(),e);
				return ClsTreater._getResultat( ClsTreater._getStackTrace(e), null, true);
			}
		}
		catch (Exception e)
		{
			//LOG.error(e.getLocalizedMessage(),e);
			return ClsTreater._getResultat( ClsTreater._getStackTrace(e), null, true);
		}
		finally
		{
			service.closeSession(session);
		}
		return null;
	}

	private ClsVarDipes calculSalaire2(Salarie agent)
	{
		ClsVarDipes var = new ClsVarDipes();
		String rubq;
		String c_Rhtcumul_taux = " SELECT SUM (coalesce (taux, 0)) ";
		c_Rhtcumul_taux += " FROM CumulPaie";
		c_Rhtcumul_taux += "WHERE idEntreprise = :cdos ";
		c_Rhtcumul_taux += "AND nmat = :nmat ";
		//c_Rhtcumul_taux += "AND nbul > 0 ";
		c_Rhtcumul_taux += "AND aamm =  :aamm ";
		c_Rhtcumul_taux += "AND rubq = :rubq ";

		String c_CumulPaie= " SELECT SUM (coalesce (mont, 0)) ";
		c_CumulPaie+= " FROM CumulPaie";
		c_CumulPaie+= "WHERE idEntreprise = :cdos ";
		c_CumulPaie+= "AND nmat = :nmat ";
		//c_CumulPaie+= "AND nbul > 0 ";
		c_CumulPaie+= "AND aamm = :aamm ";
		c_CumulPaie+= "AND rubq = :rubq ";

		BigDecimal mont = new BigDecimal(0);
		BigDecimal result = new BigDecimal(0);
		BigDecimal txh = new BigDecimal(0);

		Session session = service.getSession();
		try
		{
			Query q = null;
			List lst = null;
			for (int i = 1; i <= 90; i++)
			{
				if (i % 5 == 1)
				{
					rubq = StringUtil.oraSubstring(colxx[0], i, 4);

					if (StringUtils.isBlank(rubq))
						break;

					if(fnomNbrj != null && StringUtils.equals(fnomNbrj.getVall(), "T"))
						q = session.createSQLQuery(c_Rhtcumul_taux);
					else
						q = session.createSQLQuery(c_CumulPaie);

					q.setParameter("cdos", param.cdos);
					q.setParameter("nmat", agent.getNmat());
					q.setParameter("aamm", param.aamm);
					q.setParameter("rubq", rubq);

					lst = q.list();
					if (lst.get(0) != null)
						mont = new BigDecimal(lst.get(0).toString());
					else
						mont = new BigDecimal(0);

					if (StringUtil.oraSubstring(colxx[0], i + 4, 1).equals("+"))
						result = result.add(mont);
					else if (StringUtil.oraSubstring(colxx[0], i + 4, 1).equals("-"))
						result = result.subtract(mont);
					i = i + 5;
				}
			}
			// -- Traitement des salari�s dont la base de calcul est horaire
			if (StringUtil.oraSubstring(agent.getCods(), 1, 1).equals("H"))
			{
				// --lecture du taux horaie en table 99
				//ParamData fnom = (ParamData) service.get(ParamData.class, new ParamDataPK(param.cdos, 99, "HORAIRE", 1));
				ParamData fnom = service.findAnyColumnFromNomenclature(param.cdos, param.clang, "99", "HORAIRE", "1");
				if (fnom != null && fnom.getValt() != null)
					txh = fnom.getValt();
				if (txh.compareTo(new BigDecimal(0)) != 0)
					var.nbj = new BigDecimal(Math.ceil(result.divide(txh, MathContext.DECIMAL32).doubleValue()));
			}
			else
				var.nbj = result;

			for (int k = 1; k < 16; k++)
			{

				result = new BigDecimal(0);
				mont = new BigDecimal(0);
				for (int i = 1; i <= 90; i++)
				{
					if (i % 5 == 1)
					{
						rubq = StringUtil.oraSubstring(colxx[k], i, 4);
//						if(StringUtils.equals("00006310", agent.getNmat()))
//							ParameterUtil.println(" COL"+ClsStringUtil.formatNumber(k, "00")+", Rubrique = "+rubq);


						if (StringUtils.isBlank(rubq))
							break;

						q = session.createSQLQuery(c_CumulPaie);
						q.setParameter("cdos", param.cdos);
						q.setParameter("nmat", agent.getNmat());
						q.setParameter("aamm", param.aamm);
						q.setParameter("rubq", rubq);

						lst = q.list();
						if (lst.get(0) != null)
							mont = new BigDecimal(lst.get(0).toString());
						else
							mont = new BigDecimal(0);

						if (StringUtil.oraSubstring(colxx[k], i + 4, 1).equals("+"))
							result = result.add(mont);
						else if (StringUtil.oraSubstring(colxx[k], i + 4, 1).equals("-"))
							result = result.subtract(mont);
						i = i + 5;
					}
				}
				if (k == 1)
					var.salBrut = result;
				if (k == 2)
					var.elExcep = result;
				if (k == 3)
					var.salTax = result;
				if (k == 4)
					var.totCnp = result;
				// if(k == 5) var.salBrut= result;
				if (k == 6)
					var.retTax = result;
				if (k == 7)
					var.retSurt = result;
				if (k == 8)
					var.centAdd = result;
				if (k == 9)
					var.retTxco = result;
				// if(k == 10) var.salBrut= result;
				// if(k == 11) var.salBrut= result;
				// if(k == 12) var.salBrut= result;
				if (k == 13)
					var.cacSurtProg = result;
				if (k == 14)
					var.cacTaxProp = result;
				// if(k == 15) var.salBrut= result;
			}

			if (param.PLAFOND_CNPS.compareTo(var.totCnp) < 0)
				var.plafCnp = param.PLAFOND_CNPS;
			else
				var.plafCnp = var.totCnp;

			var.pensViel = var.plafCnp.multiply(param.TAUX_PENS_VIEIL).divide(new BigDecimal(100));
			var.prestFam = var.plafCnp.multiply(param.TAUX_PREST_FAM).divide(new BigDecimal(100));
			var.accTrav = var.totCnp.multiply(param.TAUX_ACC_TRAV).divide(new BigDecimal(100));
		}
		catch (Exception e)
		{
			//LOG.error(e.getLocalizedMessage(),e);
			this.traitement = ClsTreater._getStackTrace(e);
		}
		finally
		{
			service.closeSession(session);
		}

		return var;
	}


	private void splitColxx()
	{
		//on parcours colxx pour spliter en colxxPos et colxxNeg
		String rubq;
		String signe;
		int i = 1;
		for (int k = 0; k < 16; k++)
		{
			i = 1;
			while (i <= 90)
			{
//				if (i % 5 == 1)
//				{
				System.out.println("Valuer i="+i);
				System.out.println("Valuer data="+colxx[k]);
					rubq = StringUtil.oraSubstring(colxx[k], i, 4);
					if (StringUtils.isBlank(rubq))
						break;
					signe = StringUtil.oraSubstring(colxx[k], i + 4, 1);
					if ("+".equals(signe))
						colxxPos[k] = StringUtils.isBlank(colxxPos[k]) ? "'"+rubq+"'" : colxxPos[k] + ",'"+rubq+"'";
					else if ("-".equals(signe))
						colxxNeg[k] = StringUtils.isBlank(colxxNeg[k]) ? "'"+rubq+"'" : colxxNeg[k] + ",'"+rubq+"'";
					i = i + 5;
//				}
			}
		}
	}

	private ClsVarDipes calculSalaire(Object[] lst, String cods)
	{

		ClsVarDipes var = new ClsVarDipes();

		BigDecimal montp = new BigDecimal(0);
		BigDecimal montn = new BigDecimal(0);
		BigDecimal result = new BigDecimal(0);
		BigDecimal txh = new BigDecimal("173.33");

		try
		{

			int index = 0;
			for (int k = 0; k < 16; k++)
			{
				index = 2*k+2;
				montp = new BigDecimal((lst[index]).toString());
				montn = new BigDecimal((lst[index+1]).toString());

				result = montp.subtract(montn);

				if(k == 0)
				{
					// -- Traitement des salari�s dont la base de calcul est horaire
					if (StringUtil.oraSubstring(cods, 1, 1).equals("H"))
					{
						// --lecture du taux horaie en table 99
						if (fnomTxhDiv != null && fnomTxhDiv.getValt() != null)
							txh = fnomTxhDiv.getValt();
						if (txh.compareTo(new BigDecimal(0)) != 0)
						{
							var.nbj = new BigDecimal(Math.ceil(result.divide(txh, MathContext.DECIMAL32).doubleValue()));
					        if(var.nbj.compareTo(fnomNbrj.getValt()) > 0)var.nbj = fnomNbrj.getValt();
						}
					}
					else
						var.nbj = result;
				}

				if (k == 1)
					var.salBrut = result;
				if (k == 2)
					var.elExcep = result;
				if (k == 3)
					var.salTax = result;
				if (k == 4)
					var.totCnp = result;
				// if(k == 5) var.salBrut= result;
				if (k == 6)
					var.retTax = result;
				if (k == 7)
					var.retSurt = result;
				if (k == 8)
					var.centAdd = result;
				if (k == 9)
					var.retTxco = result;
				// if(k == 10) var.salBrut= result;
				// if(k == 11) var.salBrut= result;
				// if(k == 12) var.salBrut= result;
				if (k == 13)
					var.cacSurtProg = result;
				if (k == 14)
					var.cacTaxProp = result;
				// if(k == 15) var.salBrut= result;
			}

			if (param.PLAFOND_CNPS.compareTo(var.totCnp) < 0)
				var.plafCnp = param.PLAFOND_CNPS;
			else
				var.plafCnp = var.totCnp;

			var.pensViel = var.plafCnp.multiply(param.TAUX_PENS_VIEIL).divide(new BigDecimal(100));
			var.prestFam = var.plafCnp.multiply(param.TAUX_PREST_FAM).divide(new BigDecimal(100));
			var.accTrav = var.totCnp.multiply(param.TAUX_ACC_TRAV).divide(new BigDecimal(100));
		}
		catch (Exception e)
		{
			//LOG.error(e.getLocalizedMessage(),e);
			this.traitement = ClsTreater._getStackTrace(e);
		}


		return var;
	}

	private ClsVarDipes calculSalaire(Session session,Salarie agent)
	{
		String nmat = agent.getNmat();

		ClsVarDipes var = new ClsVarDipes();

		BigDecimal montp = new BigDecimal(0);
		BigDecimal montn = new BigDecimal(0);
		BigDecimal result = new BigDecimal(0);
		BigDecimal txh = new BigDecimal(0);

		try
		{
			Query q = null;
			List<Object[]> lst = null;

			String strQuery = " Select a.idEntreprise ";
			if(fnomNbrj != null && StringUtils.equals(fnomNbrj.getVall(), "T"))
			{
				strQuery +=", (Select coalesce(sum(coalesce(b.taux,0)),0) From CumulPaieb where b.idEntreprise = a.idEntreprise and b.nmat = '"+nmat+"' and b.aamm = '"+param.aamm+"' and b.rubq in ("+StringUtil.nvl(colxxPos[0], "''")+") ) as mntPos0 ";
				strQuery +=", (Select coalesce(sum(coalesce(c.taux,0)),0) From CumulPaie c where c.idEntreprise = a.idEntreprise and c.nmat = '"+nmat+"' and c.aamm = '"+param.aamm+"' and c.rubq in ("+StringUtil.nvl(colxxNeg[0], "''")+") ) as mntNeg0 ";
			}
			else
			{
				strQuery +=", (Select coalesce(sum(coalesce(b.mont,0)),0) From CumulPaieb where b.idEntreprise = a.idEntreprise and b.nmat = '"+nmat+"' and b.aamm = '"+param.aamm+"' and b.rubq in ("+StringUtil.nvl(colxxPos[0], "''")+") ) as mntPos0 ";
				strQuery +=", (Select coalesce(sum(coalesce(c.mont,0)),0) From CumulPaie c where c.idEntreprise = a.idEntreprise and c.nmat = '"+nmat+"' and c.aamm = '"+param.aamm+"' and c.rubq in ("+StringUtil.nvl(colxxNeg[0], "''")+") ) as mntNeg0 ";
			}
			for (int k = 1; k < 16; k++)
			{
				strQuery +=", (Select coalesce(sum(coalesce(b.mont,0)),0) From CumulPaieb where b.idEntreprise = a.idEntreprise and b.nmat = '"+nmat+"' and b.aamm = '"+param.aamm+"' and b.rubq in ("+StringUtil.nvl(colxxPos[k], "''")+") ) as mntPos" + k + " ";
				strQuery +=", (Select coalesce(sum(coalesce(c.mont,0)),0) From CumulPaie c where c.idEntreprise = a.idEntreprise and c.nmat = '"+nmat+"' and c.aamm = '"+param.aamm+"' and c.rubq in ("+StringUtil.nvl(colxxNeg[k], "''")+") ) as mntNeg" + k + " ";
			}

			strQuery +=" From DossierPaies a where a.idEntreprise = '"+ param.cdos+"' ";

			q = session.createSQLQuery(strQuery);

			lst = q.list();
			int index = 0;
			for (int k = 0; k < 16; k++)
			{
				index = 2*k+1;
				montp = new BigDecimal((lst.get(0)[index]).toString());
				montn = new BigDecimal((lst.get(0)[index+1]).toString());

				result = montp.subtract(montn);

				if(k == 0)
				{
					// -- Traitement des salari�s dont la base de calcul est horaire
					if (StringUtil.oraSubstring(agent.getCods(), 1, 1).equals("H"))
					{
						// --lecture du taux horaie en table 99
						if (fnomTxh != null && fnomTxh.getValt() != null)
							txh = fnomTxh.getValt();
						if (txh.compareTo(new BigDecimal(0)) != 0)
							var.nbj = new BigDecimal(Math.ceil(result.divide(txh, MathContext.DECIMAL32).doubleValue()));
					}
					else
						var.nbj = result;
				}

				if (k == 1)
					var.salBrut = result;
				if (k == 2)
					var.elExcep = result;
				if (k == 3)
					var.salTax = result;
				if (k == 4)
					var.totCnp = result;
				// if(k == 5) var.salBrut= result;
				if (k == 6)
					var.retTax = result;
				if (k == 7)
					var.retSurt = result;
				if (k == 8)
					var.centAdd = result;
				if (k == 9)
					var.retTxco = result;
				// if(k == 10) var.salBrut= result;
				// if(k == 11) var.salBrut= result;
				// if(k == 12) var.salBrut= result;
				if (k == 13)
					var.cacSurtProg = result;
				if (k == 14)
					var.cacTaxProp = result;
				// if(k == 15) var.salBrut= result;
			}

			if (param.PLAFOND_CNPS.compareTo(var.totCnp) < 0)
				var.plafCnp = param.PLAFOND_CNPS;
			else
				var.plafCnp = var.totCnp;

			var.pensViel = var.plafCnp.multiply(param.TAUX_PENS_VIEIL).divide(new BigDecimal(100));
			var.prestFam = var.plafCnp.multiply(param.TAUX_PREST_FAM).divide(new BigDecimal(100));
			var.accTrav = var.totCnp.multiply(param.TAUX_ACC_TRAV).divide(new BigDecimal(100));
		}
		catch (Exception e)
		{
			//LOG.error(e.getLocalizedMessage(),e);
			this.traitement = ClsTreater._getStackTrace(e);
		}


		return var;
	}


	private ClsVarDipes calculSalaireOld(Session session,Salarie agent)
	{
		ClsVarDipes var = new ClsVarDipes();

		String strTaux = " Select a.idEntreprise, ";
		strTaux +=" (Select coalesce(sum(coalesce(b.taux,0)),0) From CumulPaieb where b.idEntreprise = a.idEntreprise and b.nmat = :nmat and b.aamm = :aamm and b.rubq in (:rubqpos) ) as mntPos ,";
		strTaux +=" (Select coalesce(sum(coalesce(c.taux,0)),0) From CumulPaie c where c.idEntreprise = a.idEntreprise and c.nmat = :nmat and c.aamm = :aamm and c.rubq in (:rubqneg) ) as mntNeg ";
		strTaux +=" From DossierPaies a where a.idEntreprise = :cdos ";

		String strMont = " Select a.idEntreprise, ";
		strMont +=" (Select coalesce(sum(coalesce(b.mont,0)),0) From CumulPaieb where b.idEntreprise = a.idEntreprise and b.nmat = :nmat and b.aamm = :aamm and b.rubq in (:rubqpos) ) as mntPos ,";
		strMont +=" (Select coalesce(sum(coalesce(c.mont,0)),0) From CumulPaie c where c.idEntreprise = a.idEntreprise and c.nmat = :nmat and c.aamm = :aamm and c.rubq in (:rubqneg) ) as mntNeg ";
		strMont +=" From DossierPaies a where a.idEntreprise = :cdos ";



		BigDecimal montp = new BigDecimal(0);
		BigDecimal montn = new BigDecimal(0);
		BigDecimal result = new BigDecimal(0);
		BigDecimal txh = new BigDecimal(0);

		try
		{
			Query q = null;
			List<Object[]> lst = null;

			String strTaux1 = " Select a.idEntreprise, ";
			strTaux1 +=" (Select coalesce(sum(coalesce(b.taux,0)),0) From CumulPaieb where b.idEntreprise = a.idEntreprise and b.nmat = '"+agent.getNmat()+"' and b.aamm = '"+param.aamm+"' and b.rubq in ("+StringUtil.nvl(colxxPos[0], "''")+") ) as mntPos ,";
			strTaux1 +=" (Select coalesce(sum(coalesce(c.taux,0)),0) From CumulPaie c where c.idEntreprise = a.idEntreprise and c.nmat = '"+agent.getNmat()+"' and c.aamm = '"+param.aamm+"' and c.rubq in ("+StringUtil.nvl(colxxNeg[0], "''")+") ) as mntNeg ";
			strTaux1 +=" From DossierPaies a where a.idEntreprise = '"+ param.cdos+"' ";

			String strMont1 = " Select a.idEntreprise, ";
			strMont1 +=" (Select coalesce(sum(coalesce(b.mont,0)),0) From CumulPaieb where b.idEntreprise = a.idEntreprise and b.nmat = '"+agent.getNmat()+"' and b.aamm = '"+param.aamm+"' and b.rubq in ("+StringUtil.nvl(colxxPos[0], "''")+") ) as mntPos ,";
			strMont1 +=" (Select coalesce(sum(coalesce(c.mont,0)),0) From CumulPaie c where c.idEntreprise = a.idEntreprise and c.nmat = '"+agent.getNmat()+"' and c.aamm = '"+param.aamm+"' and c.rubq in ("+StringUtil.nvl(colxxNeg[0], "''")+") ) as mntNeg ";
			strMont1 +=" From DossierPaies a where a.idEntreprise = '"+ param.cdos+"' ";

			if(fnomNbrj != null && StringUtils.equals(fnomNbrj.getVall(), "T"))
				q = session.createSQLQuery(strTaux1);
			else
				q = session.createSQLQuery(strMont1);

//			q.setParameter("cdos", param.cdos);
//			q.setParameter("nmat", agent.getNmat());
//			q.setParameter("aamm", param.aamm);
//			q.setParameter("rubqpos", StringUtil.nvl(colxxPos[0], "''"));
//			q.setParameter("rubqneg", StringUtil.nvl(colxxNeg[0], "''"));
			lst = q.list();
			montp = new BigDecimal((lst.get(0)[1]).toString());
			montn = new BigDecimal((lst.get(0)[2]).toString());

			result = montp.subtract(montn);

			// -- Traitement des salari�s dont la base de calcul est horaire
			if (StringUtil.oraSubstring(agent.getCods(), 1, 1).equals("H"))
			{
				// --lecture du taux horaie en table 99
				//ParamData fnom = (ParamData) service.get(ParamData.class, new ParamDataPK(param.cdos, 99, "HORAIRE", 1));
				ParamData fnom = service.findAnyColumnFromNomenclature(param.cdos, param.clang, "99", "HORAIRE", "1");
				if (fnom != null && fnom.getValt() != null)
					txh = fnom.getValt();
				if (txh.compareTo(new BigDecimal(0)) != 0)
					var.nbj = new BigDecimal(Math.ceil(result.divide(txh, MathContext.DECIMAL32).doubleValue()));
			}
			else
				var.nbj = result;

			for (int k = 1; k < 16; k++)
			{
				strMont1 = " Select a.idEntreprise, ";
				strMont1 +=" (Select coalesce(sum(coalesce(b.mont,0)),0) From CumulPaieb where b.idEntreprise = a.idEntreprise and b.nmat = '"+agent.getNmat()+"' and b.aamm = '"+param.aamm+"' and b.rubq in ("+StringUtil.nvl(colxxPos[k], "''")+") ) as mntPos ,";
				strMont1 +=" (Select coalesce(sum(coalesce(c.mont,0)),0) From CumulPaie c where c.idEntreprise = a.idEntreprise and c.nmat = '"+agent.getNmat()+"' and c.aamm = '"+param.aamm+"' and c.rubq in ("+StringUtil.nvl(colxxNeg[k], "''")+") ) as mntNeg ";
				strMont1 +=" From DossierPaies a where a.idEntreprise = '"+ param.cdos+"' ";

				q = session.createSQLQuery(strMont1);

//				q.setParameter("cdos", param.cdos);
//				q.setParameter("nmat", agent.getNmat());
//				q.setParameter("aamm", param.aamm);
//				q.setParameter("rubqpos", StringUtil.nvl(colxxPos[k], "''"));
//				q.setParameter("rubqneg", StringUtil.nvl(colxxNeg[k], "''"));
				lst = q.list();
				montp = new BigDecimal((lst.get(0)[1]).toString());
				montn = new BigDecimal((lst.get(0)[2]).toString());

				result = montp.subtract(montn);

				if (k == 1)
					var.salBrut = result;
				if (k == 2)
					var.elExcep = result;
				if (k == 3)
					var.salTax = result;
				if (k == 4)
					var.totCnp = result;
				// if(k == 5) var.salBrut= result;
				if (k == 6)
					var.retTax = result;
				if (k == 7)
					var.retSurt = result;
				if (k == 8)
					var.centAdd = result;
				if (k == 9)
					var.retTxco = result;
				// if(k == 10) var.salBrut= result;
				// if(k == 11) var.salBrut= result;
				// if(k == 12) var.salBrut= result;
				if (k == 13)
					var.cacSurtProg = result;
				if (k == 14)
					var.cacTaxProp = result;
				// if(k == 15) var.salBrut= result;
			}

			if (param.PLAFOND_CNPS.compareTo(var.totCnp) < 0)
				var.plafCnp = param.PLAFOND_CNPS;
			else
				var.plafCnp = var.totCnp;

			var.pensViel = var.plafCnp.multiply(param.TAUX_PENS_VIEIL).divide(new BigDecimal(100));
			var.prestFam = var.plafCnp.multiply(param.TAUX_PREST_FAM).divide(new BigDecimal(100));
			var.accTrav = var.totCnp.multiply(param.TAUX_ACC_TRAV).divide(new BigDecimal(100));
		}
		catch (Exception e)
		{
			//LOG.error(e.getLocalizedMessage(),e);
			this.traitement = ClsTreater._getStackTrace(e);
		}


		return var;
	}

	Map<String, ClsParameterOfDipe> infosVilles = new HashMap<String, ClsParameterOfDipe>();

	private void infoVille(String vville)
	{

		Session session = service.getSession();
		int i = 0;
		String query = "select b.ctyp as ctyp, (case b.ctyp when 'L' then a.vall else null end) vall, (case b.ctyp when 'M' then a.valm else null end) valm ";
		query += "from ParamData a, ParamColumn b ";
		query += "where a.idEntreprise = :cdos ";
		query += "and a.ctab = 5 ";
		query += "and a.cacc = :ville ";
		query += "and a.nume = :nume ";
		query += "and b.idEntreprise = a.idEntreprise ";
		query += "and b.ctab = a.ctab ";
		query += "and b.nume = a.nume";

		try
		{
			if (infosVilles.containsKey(vville))
			{
				ClsParameterOfDipe paramClone = infosVilles.get(vville);
				param.ville = paramClone.ville;
				param.regCNPS = paramClone.regCNPS;
				param.quartier = paramClone.quartier;
				param.bp = paramClone.bp;
				param.telephone = paramClone.telephone;
				param.numCompte = paramClone.numCompte;
				return;
			}

			String ville = StringUtils.EMPTY, vall = StringUtils.EMPTY;
			BigDecimal valm = new BigDecimal(0);

			i = 1;
			Query q = session.createSQLQuery(query);
			q.setParameter("cdos", param.cdos);
			q.setParameter("ville", vville);
			q.setParameter("nume", i);
			List<Object[]> lst = q.list();
			String typ = StringUtils.EMPTY;

			if (!lst.isEmpty())
			{
				for(int ii=0; ii<lst.size(); ii++)
				{
					typ = (lst.get(ii)[0]).toString();
					if (lst.get(ii) != null)
					{
						if (lst.get(ii)[1] != null && StringUtils.equals(typ, "L"))
							ville = (lst.get(ii)[1]).toString();

						if (lst.get(ii)[2] != null && StringUtils.equals(typ, "M"))
							valm = new BigDecimal((lst.get(ii)[2]).toString());
					}
				}
			}

			param.ville = ville;

			param.regCNPS = valm;

			i = 2;
			q = session.createSQLQuery(query);
			q.setParameter("cdos", param.cdos);
			q.setParameter("ville", vville);
			q.setParameter("nume", i);
			lst = q.list();
			if (!lst.isEmpty())
			{
				for(int ii=0; ii<lst.size(); ii++)
				{
					typ = (lst.get(ii)[0]).toString();
					if (lst.get(ii) != null)
					{
						if (lst.get(ii)[1] != null && StringUtils.equals(typ, "L"))
							vall = (lst.get(ii)[1]).toString();

						if (lst.get(ii)[2] != null && StringUtils.equals(typ, "M"))
							valm = new BigDecimal((lst.get(ii)[2]).toString());
					}
				}
			}
			param.quartier = vall;

			i = 3;
			q = session.createSQLQuery(query);
			q.setParameter("cdos", param.cdos);
			q.setParameter("ville", vville);
			q.setParameter("nume", i);
			lst = q.list();
			if (!lst.isEmpty())
			{
				for(int ii=0; ii<lst.size(); ii++)
				{
					typ = (lst.get(ii)[0]).toString();
					if (lst.get(ii) != null)
					{
						if (lst.get(ii)[1] != null && StringUtils.equals(typ, "L"))
							vall = (lst.get(ii)[1]).toString();

						if (lst.get(ii)[2] != null && StringUtils.equals(typ, "M"))
							valm = new BigDecimal((lst.get(ii)[2]).toString());
					}
				}

			}
			param.bp = vall;

			i = 4;
			q = session.createSQLQuery(query);
			q.setParameter("cdos", param.cdos);
			q.setParameter("ville", vville);
			q.setParameter("nume", i);
			lst = q.list();
			if (!lst.isEmpty())
			{
				for(int ii=0; ii<lst.size(); ii++)
				{
					typ = (lst.get(ii)[0]).toString();
					if (lst.get(ii) != null)
					{
						if (lst.get(ii)[1] != null && StringUtils.equals(typ, "L"))
							vall = (lst.get(ii)[1]).toString();

						if (lst.get(ii)[2] != null && StringUtils.equals(typ, "M"))
							valm = new BigDecimal((lst.get(ii)[2]).toString());
					}
				}
			}
			param.telephone = vall;

			i = 5;
			q = session.createSQLQuery(query);
			q.setParameter("cdos", param.cdos);
			q.setParameter("ville", vville);
			q.setParameter("nume", i);
			lst = q.list();
			if (!lst.isEmpty())
			{
				for(int ii=0; ii<lst.size(); ii++)
				{
					typ = (lst.get(ii)[0]).toString();
					if (lst.get(ii) != null)
					{
						if (lst.get(ii)[1] != null && StringUtils.equals(typ, "L"))
							vall = (lst.get(ii)[1]).toString();

						if (lst.get(ii)[2] != null && StringUtils.equals(typ, "M"))
							valm = new BigDecimal((lst.get(ii)[2]).toString());
					}
				}
			}
			param.numCompte = vall;

			ClsParameterOfDipe param1 = new ClsParameterOfDipe();
			BeanUtils.copyProperties(param, param1);

			infosVilles.put(vville, param1);
		}
		catch (Exception e)
		{
			//LOG.error(e.getLocalizedMessage(),e);
			this.traitement = ClsTreater._getStackTrace(e);
		}
		finally
		{
			service.closeSession(session);
		}
	}

	String[] colxx = new String[] { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null };

	String[] colxxPos = new String[] { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null };
	String[] colxxNeg = new String[] { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null };

	private void prepareParam()
	{
		Integer ctab = 957;
		//ParamData fnom = (ParamData) service.get(ParamData.class, new ParamDataPK(param.cdos, 99, "DIPE", 3));
		ParamData fnom = service.findAnyColumnFromNomenclature(param.cdos, param.clang, "99", "DIPE", "3");
		if (fnom != null && fnom.getValm() != null && fnom.getValm() != 0)
			ctab = fnom.getValm().intValue();

		String query = " SELECT cacc,vall,nume ";
		query += "FROM ParamData ";
		query += "WHERE idEntreprise = :cdos ";
		query += "AND ctab = "+ctab+" and nume > 1 ";
		query += "ORDER BY cacc,nume ";
		for (int i = 0; i < 16; i++)
			colxx[i] = StringUtils.EMPTY;

		Session session = service.getSession();

		try
		{
			Query q = session.createSQLQuery(query);
			q.setParameter("cdos", param.cdos);
			List<Object[]> lst = q.list();
			String cacc, vall;
			// Integer nume;
			for (int i = 0; i < lst.size(); i++)
			{
				vall = StringUtils.EMPTY;
				cacc = lst.get(i)[0].toString();
				if (lst.get(i)[1] != null)
					vall = lst.get(i)[1].toString();
				// nume = new Integer(lst.get(i)[2].toString());

				for (int j = 0; j < 16; j++)
					if (cacc.equals("COL" + ClsStringUtil.formatNumber(j, "00")))
						colxx[j] = colxx[j] + vall;
			}

			splitColxx();

//			for (int j = 0; j < 16; j++)
//					ParameterUtil.println("Colonne COL"+ClsStringUtil.formatNumber(j, "00")+" = "+colxx[j]);

			// -- Recherche plafond CNPS

			ElementSalaireBareme bareme = null;//(ElementSalaireBareme) service.get(ElementSalaireBareme.class, new ElementSalaireBaremePK(param.cdos, StringUtil.oraSubstring(colxx[10], 1, 4), 1));
			List<ElementSalaireBareme> baremes = this.getService().find("FROM ElementSalaireBareme WHERE idEntreprise="+param.cdos+" AND crub='"+StringUtil.oraSubstring(colxx[10], 1, 4)+"' AND nume=1");
			if(!baremes.isEmpty()) bareme = baremes.get(0);
			if (bareme != null)
				param.PLAFOND_CNPS = new BigDecimal(bareme.getVal2());

			// -- Recherche Taux Pension Vieilesse (CNPS)
			ElementSalaireBareme bareme1 = null;//(ElementSalaireBareme) service.get(ElementSalaireBareme.class, new ElementSalaireBaremePK(param.cdos, StringUtil.oraSubstring(colxx[10], 1, 4), 1));
			baremes = this.getService().find("FROM ElementSalaireBareme WHERE idEntreprise="+param.cdos+" AND crub='"+StringUtil.oraSubstring(colxx[10], 1, 4)+"' AND nume=1");
			if(!baremes.isEmpty()) bareme1 = baremes.get(0);
			ElementSalaireBareme bareme2 = null;//(ElementSalaireBareme) service.get(ElementSalaireBareme.class, new ElementSalaireBaremePK(param.cdos, StringUtil.oraSubstring(colxx[10], 6, 4), 1));
			baremes = this.getService().find("FROM ElementSalaireBareme WHERE idEntreprise="+param.cdos+" AND crub='"+StringUtil.oraSubstring(colxx[10], 6, 4)+"' AND nume=1");
			if(!baremes.isEmpty()) bareme2 = baremes.get(0);
			if (bareme1 != null)
				param.TAUX_PENS_VIEIL = bareme1.getTaux();
			if (bareme2 != null)
				param.TAUX_PENS_VIEIL = param.TAUX_PENS_VIEIL.add(bareme2.getTaux());

			// -- Recherche Taux prestations familiales
			baremes = this.getService().find("FROM ElementSalaireBareme WHERE idEntreprise="+param.cdos+" AND crub='"+StringUtil.oraSubstring(colxx[15], 1, 4)+"' AND nume=1");
			if(!baremes.isEmpty()) bareme = baremes.get(0);
			//bareme = (ElementSalaireBareme) service.get(ElementSalaireBareme.class, new ElementSalaireBaremePK(param.cdos, StringUtil.oraSubstring(colxx[15], 1, 4), 1));
			if (bareme != null)
				param.TAUX_PREST_FAM = bareme.getTaux();

			// -- Recherche Taux Accident Travail
			baremes = this.getService().find("FROM ElementSalaireBareme WHERE idEntreprise="+param.cdos+" AND crub='"+StringUtil.oraSubstring(colxx[15], 1, 4)+"' AND nume=1");
			if(!baremes.isEmpty()) bareme = baremes.get(0);
			//bareme = (ElementSalaireBareme) service.get(ElementSalaireBareme.class, new ElementSalaireBaremePK(param.cdos, StringUtil.oraSubstring(colxx[15], 1, 4), 1));
			if (bareme != null)
				param.TAUX_ACC_TRAV = bareme.getTaux();
		}
		catch (Exception e)
		{
			//LOG.error(e.getLocalizedMessage(),e);
			this.traitement = ClsTreater._getStackTrace(e);
		}
		finally
		{
			service.closeSession(session);
		}
	}

	private String phase = StringUtils.EMPTY;

	public void init()
	{
		int div = 1;

		phase = "Lecture des param�tre...";

		// --recuperation du numero de contribuable
		DossierPaie dos = null;//(DossierPaie) service.get(DossierPaie.class, param.cdos);
		List<DossierPaie> dossiers = this.getService().find("FROM DossierPaie WHERE idEntreprise="+param.cdos);
		if(!dossiers.isEmpty()) dos = dossiers.get(0);
		param.dnii = dos.getDnii();
		param.dnii = StringUtil.nvl(StringUtil.oraLPad(param.dnii, 14, "0"), "00000000000000");
		param.ncontribuable = param.dnii;

		param.feuille = new ClsDate(param.aamm, "yyyyMM").getMonth();

		prepareParam();

		param.date = new ClsDate(param.aamm, "yyyyMM").getDate();
		param.date_c = new ClsDate(param.date).getDateS("yyyyMMdd");

		//ParamData fnm = (ParamData)service.get(ParamData.class, new ParamDataPK(param.cdos,99,"TYPEACT",1));
		ParamData fnm = service.findAnyColumnFromNomenclature(param.cdos, param.clang, "99", "TYPEACT", "1");
		if(fnm != null && fnm.getValm() != null)
			param.typact = fnm.getValm().intValue();

		//fnm = (ParamData)service.get(ParamData.class, new ParamDataPK(param.cdos,99,"PLAFNBRJRS",1));
		fnm = service.findAnyColumnFromNomenclature(param.cdos, param.clang, "99", "PLAFNBRJRS", "1");
		if(fnm != null && fnm.getValm() != null)
			param.maxNbrJours = fnm.getValm().intValue();

		//fnm = (ParamData)service.get(ParamData.class, new ParamDataPK(param.cdos,99,"PLAFNBRJRS",1));
		fnm = service.findAnyColumnFromNomenclature(param.cdos, param.clang, "99", "PLAFNBRJRS", "1");
		if(fnm != null && StringUtils.isNotBlank(fnm.getVall()))
			param.inclureEnfantsEtNombreParts = StringUtils.equals(fnm.getVall(),"O");

		Session session = service.getSession();

		try
		{
			Query q = null;
			if (param.typeDipe == ClsTypeDipesMagnetiques.MENSUEL)
			{
//				String c_sal_count = "SELECT  count(distinct a.nmat) ";
//				c_sal_count += "FROM Salarie a, Dipe b ";
//				c_sal_count += "WHERE a.idEntreprise = :cdos ";
//				c_sal_count += "AND b.idEntreprise = a.idEntreprise ";
//				c_sal_count += "AND b.nmat = a.nmat ";
//				c_sal_count += "AND b.annee = :annee ";
//				c_sal_count += "AND LTRIM (a.vild) IS NOT NULL ";
//				if (StringUtils.isNotBlank(param.ville1))
//					c_sal_count += "AND a.vild >= :ville1 ";
//				if (StringUtils.isNotBlank(param.ville2))
//					c_sal_count += "AND a.vild <= :ville2 ";
//				c_sal_count += "AND a.nmat IN (SELECT distinct nmat ";
//				c_sal_count += "FROM CumulPaie";
//				c_sal_count += "WHERE idEntreprise = a.idEntreprise AND aamm = :aamm) ";
////				c_sal_count += "AND (a.mrrx != 'RA' or a.mrrx is null or (a.dmrr > :dfex and a.dmrr is not null)) ";
//				c_sal_count += "GROUP BY a.vild, a.nmat ";
//				c_sal_count += "ORDER BY a.vild, a.nmat ";

				// Pour ART
				String c_sal_count = "SELECT  count(distinct a.nmat) ";
				c_sal_count += "FROM Salarie a LEFT JOIN Dipe b ON (b.idEntreprise = a.idEntreprise AND b.nmat = a.nmat AND (b.annee=:aamm or (b.annee!=:aamm and b.annee = :annee)) )";
				c_sal_count += "WHERE a.idEntreprise = :cdos ";
//				c_sal_count += "AND b.idEntreprise = a.idEntreprise ";
//				c_sal_count += "AND b.nmat = a.nmat ";
//				c_sal_count += "AND b.annee = :annee ";
				c_sal_count += "AND LTRIM (a.vild) IS NOT NULL ";
				if (StringUtils.isNotBlank(param.ville1))
					c_sal_count += "AND a.vild >= :ville1 ";
				if (StringUtils.isNotBlank(param.ville2))
					c_sal_count += "AND a.vild <= :ville2 ";
				c_sal_count += "AND exists (SELECT 'x' ";
				c_sal_count += "FROM CumulPaie c ";
				c_sal_count += "WHERE c.idEntreprise = a.idEntreprise AND c.aamm = :aamm and c.nmat = a.nmat) ";
//				c_sal_count += "AND (a.mrrx != 'RA' or a.mrrx is null or (a.dmrr > :dfex and a.dmrr is not null)) ";
				c_sal_count += "AND ((coalesce(a.mrrx,'XX') != 'RA' or to_char(coalesce(a.dmrr,:daamm),'yyyyMM')>=:aamm)) ";
				/*c_sal_count += "GROUP BY a.vild, a.nmat ";
				c_sal_count += "ORDER BY a.vild, a.nmat ";*/

				q = session.createSQLQuery(c_sal_count);

				q.setParameter("cdos", param.cdos);
				q.setParameter("aamm", param.aamm);
				q.setParameter("daamm", param.date);
				if (StringUtils.isNotBlank(param.ville1))
					q.setParameter("ville1", param.ville1);
				if (StringUtils.isNotBlank(param.ville2))
					q.setParameter("ville2", param.ville2);
//				q.setParameter("dfex", param.dfex, StandardBasicTypes.DATE);
				q.setParameter("annee", param.annee);
				List<Object> l=q.list();
				if(ClsObjectUtil.isListEmty(l)){
					nbrSalaries=0;
				}else{
					nbrSalaries = new Integer(l.get(0).toString());
				}
			}
			if (param.typeDipe == ClsTypeDipesMagnetiques.DEBUT_EXERCICE)
			{
//				String c_sal_count = "SELECT  count(distinct a.nmat) ";
//				c_sal_count += "FROM Salarie a, Dipe b ";
//				c_sal_count += "WHERE a.idEntreprise = :cdos ";
//				c_sal_count += "AND b.idEntreprise = a.idEntreprise ";
//				c_sal_count += "AND b.nmat = a.nmat ";
//				c_sal_count += "AND b.annee = :annee ";
//				c_sal_count += "AND LTRIM (a.vild) IS NOT NULL ";
//				if (StringUtils.isNotBlank(param.ville1))
//					c_sal_count += "AND a.vild >= :ville1 ";
//				if (StringUtils.isNotBlank(param.ville2))
//					c_sal_count += "AND a.vild <= :ville2 ";
//				c_sal_count += "AND a.nmat IN (SELECT distinct nmat ";
//				c_sal_count += "FROM CumulPaie";
//				c_sal_count += "WHERE idEntreprise = a.idEntreprise AND aamm = :aamm) ";
//				c_sal_count += "AND (a.mrrx != 'RA' or a.mrrx is null or (a.dmrr > :dfex and a.dmrr is not null)) ";
//				c_sal_count += "GROUP BY a.vild, a.nmat ";
//				c_sal_count += "ORDER BY a.vild, a.nmat ";

				// Pour ART
				String c_sal_count = "SELECT  count(distinct a.nmat) ";
				c_sal_count += "FROM Salarie a LEFT JOIN Dipe b ON ( b.idEntreprise = a.idEntreprise AND b.nmat = a.nmat AND (b.annee=:aamm or (b.annee!=:aamm and b.annee = :annee)) )";
				c_sal_count += "WHERE a.idEntreprise = :cdos ";
//				c_sal_count += "AND b.idEntreprise = a.idEntreprise ";
//				c_sal_count += "AND b.nmat = a.nmat ";
//				c_sal_count += "AND b.annee = :annee ";
				c_sal_count += "AND LTRIM (a.vild) IS NOT NULL ";
				if (StringUtils.isNotBlank(param.ville1))
					c_sal_count += "AND a.vild >= :ville1 ";
				if (StringUtils.isNotBlank(param.ville2))
					c_sal_count += "AND a.vild <= :ville2 ";
				c_sal_count += "AND exists (SELECT 'x' ";
				c_sal_count += "FROM CumulPaie c ";
				c_sal_count += "WHERE c.idEntreprise = a.idEntreprise AND c.aamm = :aamm and c.nmat = a.nmat) ";
				c_sal_count += "AND (a.mrrx != 'RA' or a.mrrx is null or (a.dmrr > :dfex and a.dmrr is not null)) ";
				/*c_sal_count += "GROUP BY a.vild, a.nmat ";
				c_sal_count += "ORDER BY a.vild, a.nmat ";*/

				q = session.createSQLQuery(c_sal_count);

				q.setParameter("cdos", param.cdos);
				q.setParameter("aamm", param.aamm);
				if (StringUtils.isNotBlank(param.ville1))
					q.setParameter("ville1", param.ville1);
				if (StringUtils.isNotBlank(param.ville2))
					q.setParameter("ville2", param.ville2);
				q.setParameter("dfex", param.dfex, StandardBasicTypes.DATE);
				q.setParameter("annee", param.annee);

				List<Object> l=q.list();
				if(ClsObjectUtil.isListEmty(l)){
					nbrSalaries=0;
				}else{
					nbrSalaries = new Integer(l.get(0).toString());
				}

			}
			if (param.typeDipe == ClsTypeDipesMagnetiques.FIN_EXERCICE)
			{
//				String c_sal_count = "SELECT  count(distinct a.nmat) ";
//				c_sal_count += "FROM Salarie a, Dipe b ";
//				c_sal_count += "WHERE a.idEntreprise = :cdos ";
//				c_sal_count += "AND b.idEntreprise = a.idEntreprise ";
//				c_sal_count += "AND b.nmat = a.nmat ";
//				c_sal_count += "AND b.annee = :annee ";
//				c_sal_count += "AND LTRIM (a.vild) IS NOT NULL ";
//				if (StringUtils.isNotBlank(param.ville1))
//					c_sal_count += "AND a.vild >= :ville1 ";
//				if (StringUtils.isNotBlank(param.ville2))
//					c_sal_count += "AND a.vild <= :ville2 ";
//				c_sal_count += "AND a.nmat IN (SELECT distinct nmat ";
//				c_sal_count += "FROM CumulPaie";
//				c_sal_count += "WHERE idEntreprise = a.idEntreprise AND aamm = :aamm) ";
//				c_sal_count += "AND (a.mrrx != 'RA' or a.mrrx is null or (a.dmrr > :dfex and a.dmrr is not null)) ";
//				c_sal_count += "GROUP BY a.vild, a.nmat ";
//				c_sal_count += "ORDER BY a.vild, a.nmat ";

				// Pour ART
				String c_sal_count = "SELECT  count(distinct a.nmat) ";
				c_sal_count += "FROM Salarie a LEFT JOIN Dipe b ON (b.idEntreprise = a.idEntreprise  AND b.nmat = a.nmat AND (b.annee=:aamm or (b.annee!=:aamm and b.annee = :annee)) )";
				c_sal_count += "WHERE a.idEntreprise = :cdos ";
//				c_sal_count += "AND b.idEntreprise = a.idEntreprise ";
//				c_sal_count += "AND b.nmat = a.nmat ";
//				c_sal_count += "AND b.annee = :annee ";
				c_sal_count += "AND LTRIM (a.vild) IS NOT NULL ";
				if (StringUtils.isNotBlank(param.ville1))
					c_sal_count += "AND a.vild >= :ville1 ";
				if (StringUtils.isNotBlank(param.ville2))
					c_sal_count += "AND a.vild <= :ville2 ";
				c_sal_count += "AND exists (SELECT 'x' ";
				c_sal_count += "FROM CumulPaie c ";
				c_sal_count += "WHERE c.idEntreprise = a.idEntreprise AND c.aamm = :aamm and c.nmat = a.nmat) ";
				c_sal_count += "AND (a.mrrx != 'RA' or a.mrrx is null or (a.dmrr > :dfex and a.dmrr is not null)) ";
				/*c_sal_count += "GROUP BY a.vild, a.nmat ";
				c_sal_count += "ORDER BY a.vild, a.nmat ";*/

				q = session.createSQLQuery(c_sal_count);

				q.setParameter("cdos", param.cdos);
				q.setParameter("aamm", param.aamm);
				if (StringUtils.isNotBlank(param.ville1))
					q.setParameter("ville1", param.ville1);
				if (StringUtils.isNotBlank(param.ville2))
					q.setParameter("ville2", param.ville2);
				q.setParameter("dfex", param.dfex, StandardBasicTypes.DATE);
				q.setParameter("annee", param.annee);

				List<Object> l=q.list();
				if(ClsObjectUtil.isListEmty(l)){
					nbrSalaries=0;
				}else{
					nbrSalaries = new Integer(l.get(0).toString());
				}
			}
			if (param.typeDipe == ClsTypeDipesMagnetiques.TEMPORAIRE)
			{
//				String c_sal_count = "SELECT  count(distinct a.nmat) ";
//				c_sal_count += "FROM Salarie a, Dipe b ";
//				c_sal_count += "WHERE a.idEntreprise = :cdos ";
//				c_sal_count += "AND b.idEntreprise = a.idEntreprise ";
//				c_sal_count += "AND b.nmat = a.nmat ";
//				c_sal_count += "AND b.annee = :annee ";
//				c_sal_count += "AND LTRIM (a.vild) IS NOT NULL ";
//				if (StringUtils.isNotBlank(param.ville1))
//					c_sal_count += "AND a.vild >= :ville1 ";
//				if (StringUtils.isNotBlank(param.ville2))
//					c_sal_count += "AND a.vild <= :ville2 ";
//				c_sal_count += "AND a.nmat IN (SELECT distinct nmat ";
//				c_sal_count += "FROM CumulPaie";
//				c_sal_count += "WHERE idEntreprise = a.idEntreprise AND aamm = :aamm) ";
//				c_sal_count += " AND a.GRAD in (select cacc from ParamData where cdos=:cdos and ctab='6' and nume=1 and valm=3) ";
//				c_sal_count += "AND (a.mrrx != 'RA' or a.mrrx is null or (a.dmrr > :dfex and a.dmrr is not null)) ";
//				c_sal_count += "GROUP BY a.vild, a.nmat ";
//				c_sal_count += "ORDER BY a.vild, a.nmat ";

				// Pour ART
				String c_sal_count = "SELECT  count(distinct a.nmat) ";
				c_sal_count += "FROM Salarie a LEFT JOIN Dipe b ON (b.idEntreprise = a.idEntreprise AND b.nmat = a.nmat AND (b.annee=:aamm or (b.annee!=:aamm and b.annee = :annee)))";
				c_sal_count += "WHERE a.idEntreprise = :cdos ";
//				c_sal_count += "AND b.idEntreprise = a.idEntreprise ";
//				c_sal_count += "AND b.nmat = a.nmat ";
//				c_sal_count += "AND b.annee = :annee ";
				c_sal_count += "AND LTRIM (a.vild) IS NOT NULL ";
				if (StringUtils.isNotBlank(param.ville1))
					c_sal_count += "AND a.vild >= :ville1 ";
				if (StringUtils.isNotBlank(param.ville2))
					c_sal_count += "AND a.vild <= :ville2 ";
				c_sal_count += "AND exists (SELECT 'x' ";
				c_sal_count += "FROM CumulPaie c ";
				c_sal_count += "WHERE c.idEntreprise = a.idEntreprise AND c.aamm = :aamm and c.nmat = a.nmat) ";
				c_sal_count += " AND a.GRAD in (select cacc from ParamData where cdos=:cdos and ctab='6' and nume=1 and valm=3) ";
				c_sal_count += "AND (a.mrrx != 'RA' or a.mrrx is null or (a.dmrr > :dfex and a.dmrr is not null)) ";
				/*c_sal_count += "GROUP BY a.vild, a.nmat ";
				c_sal_count += "ORDER BY a.vild, a.nmat ";*/

				q = session.createSQLQuery(c_sal_count);

				q.setParameter("cdos", param.cdos);
				q.setParameter("aamm", param.aamm);
				if (StringUtils.isNotBlank(param.ville1))
					q.setParameter("ville1", param.ville1);
				if (StringUtils.isNotBlank(param.ville2))
					q.setParameter("ville2", param.ville2);
				q.setParameter("dfex", param.dfex, StandardBasicTypes.DATE);
				q.setParameter("annee", param.annee);

				List<Object> l=q.list();
				if(ClsObjectUtil.isListEmty(l)){
					nbrSalaries=0;
				}else{
					nbrSalaries = new Integer(l.get(0).toString());
				}
			}
			if (param.typeDipe == ClsTypeDipesMagnetiques.EMBAUCHE)
			{
//				String c_sal_count = "SELECT  "/*"count("*/+"distinct a.nmat,a.dtes"+/*")"*/" ";
//				c_sal_count += " FROM Salarie a, Dipe b ";
//				c_sal_count += "  WHERE a.idEntreprise = :cdos ";
//				c_sal_count += " AND b.idEntreprise = a.idEntreprise ";
//				c_sal_count += " AND b.nmat = a.nmat ";
//				c_sal_count += "AND b.annee = :annee ";
//				c_sal_count += " AND LTRIM (a.vild) IS NOT NULL ";
//				if (StringUtils.isNotBlank(param.ville1))
//					c_sal_count += "AND a.vild >= :ville1 ";
//				if (StringUtils.isNotBlank(param.ville2))
//					c_sal_count += "AND a.vild <= :ville2 ";
//				c_sal_count += " AND a.nmat IN (SELECT distinct nmat ";
//				c_sal_count += " FROM CumulPaie";
//				c_sal_count += " WHERE idEntreprise = a.idEntreprise AND aamm = :aamm) ";
////				c_sal_count += " AND to_char(a.dtes,'yyyymm') = :aamm ";
//				c_sal_count += " AND (a.mrrx != 'RA' or a.mrrx is null or (a.dmrr > :dfex and a.dmrr is not null)) ";
//				c_sal_count += "GROUP BY a.vild, a.nmat, a.dtes ";
//				c_sal_count += " ORDER BY a.nmat ";

				// Pour ART
				String c_sal_count = "SELECT  "/*"count("*/+"distinct a.nmat,a.dtes"+/*")"*/" ";
				c_sal_count += " FROM Salarie a LEFT JOIN Dipe b ON ( b.idEntreprise = a.idEntreprise AND b.nmat = a.nmat AND (b.annee=:aamm or (b.annee!=:aamm and b.annee = :annee)))";
				c_sal_count += "  WHERE a.idEntreprise = :cdos ";
//				c_sal_count += " AND b.idEntreprise = a.idEntreprise ";
//				c_sal_count += " AND b.nmat = a.nmat ";
//				c_sal_count += "AND b.annee = :annee ";
				c_sal_count += " AND LTRIM (a.vild) IS NOT NULL ";
				if (StringUtils.isNotBlank(param.ville1))
					c_sal_count += "AND a.vild >= :ville1 ";
				if (StringUtils.isNotBlank(param.ville2))
					c_sal_count += "AND a.vild <= :ville2 ";
				c_sal_count += "AND exists (SELECT 'x' ";
				c_sal_count += "FROM CumulPaie c ";
				c_sal_count += "WHERE c.idEntreprise = a.idEntreprise AND c.aamm = :aamm and c.nmat = a.nmat) ";
//				c_sal_count += " AND to_char(a.dtes,'yyyymm') = :aamm ";
				c_sal_count += " AND (a.mrrx != 'RA' or a.mrrx is null or (a.dmrr > :dfex and a.dmrr is not null)) ";
				c_sal_count += "GROUP BY a.vild, a.nmat, a.dtes ";
				c_sal_count += " ORDER BY a.nmat ";

				q = session.createSQLQuery(c_sal_count);

				q.setParameter("cdos", param.cdos);
				q.setParameter("aamm", param.aamm);
				if (StringUtils.isNotBlank(param.ville1))
					q.setParameter("ville1", param.ville1);
				if (StringUtils.isNotBlank(param.ville2))
					q.setParameter("ville2", param.ville2);
				q.setParameter("dfex", param.dfex, StandardBasicTypes.DATE);
				q.setParameter("annee", param.annee);
				List<Object[]> ls=(List<Object[]>)q.list();
				List<Object[]> ls2=new ArrayList<Object[]>();
				for(Object[] item:ls){
					if(item[1] instanceof Date){
						Date dtes=(Date)item[1];
						if(ClsDate.getDateS(dtes, "yyyyMM").equalsIgnoreCase(param.aamm)){
							ls2.add(item);
						}
					}
				}
				nbrSalaries = /*new Integer(q.list().get(0).toString())*/ls2.size();
				ls2.clear();
			}
			if (param.typeDipe == ClsTypeDipesMagnetiques.CD10)
			{
				String c_sal_count = "SELECT  count(distinct a.nmat) ";
				c_sal_count += "FROM Salarie a, Dipe b ";
				c_sal_count += "WHERE a.idEntreprise = :cdos ";
				c_sal_count += "AND b.idEntreprise = a.idEntreprise ";
				c_sal_count += "AND b.nmat = a.nmat ";
				c_sal_count += "AND (b.annee=:aamm or (b.annee!=:aamm and b.annee = :annee)) ";
				c_sal_count += "AND LTRIM (a.vild) IS NOT NULL ";
				if (StringUtils.isNotBlank(param.ville1))
					c_sal_count += "AND a.vild >= :ville1 ";
				if (StringUtils.isNotBlank(param.ville2))
					c_sal_count += "AND a.vild <= :ville2 ";
				c_sal_count += "AND exists(SELECT 'x' ";
				c_sal_count += "FROM CumulPaie c ";
				c_sal_count += "WHERE c.idEntreprise = a.idEntreprise ";
				c_sal_count += "AND " + TypeBDUtil.souschaine("c.aamm", 1, 4) + "  = :aaaa and c.nmat = a.nmat)  ";
				/*c_sal_count += "GROUP BY a.vild, a.nmat ";
				c_sal_count += "ORDER BY a.vild, a.nmat ";*/

				q = session.createSQLQuery(c_sal_count);

				q.setParameter("cdos", param.cdos);
				q.setParameter("aaaa", param.annee);
				q.setParameter("aamm", param.aamm);
				if (StringUtils.isNotBlank(param.ville1))
					q.setParameter("ville1", param.ville1);
				if (StringUtils.isNotBlank(param.ville2))
					q.setParameter("ville2", param.ville2);
				q.setParameter("annee", param.annee);

				List<Object> l=q.list();
				if(ClsObjectUtil.isListEmty(l)){
					nbrSalaries=0;
				}else{
					nbrSalaries = new Integer(l.get(0).toString());
				}

			}
			if (nbrSalaries == 0)
				this.traitement = " Aucun salari� � traiter";

			div = 0;

			if (param.typeDipe == ClsTypeDipesMagnetiques.MENSUEL)
				div += 1;

			if (param.typeDipe == ClsTypeDipesMagnetiques.DEBUT_EXERCICE)
				div += 1;
			if (param.typeDipe == ClsTypeDipesMagnetiques.FIN_EXERCICE)
				div += 1;
			if (param.typeDipe == ClsTypeDipesMagnetiques.TEMPORAIRE)
				div += 1;
			if (param.typeDipe == ClsTypeDipesMagnetiques.EMBAUCHE)
				div += 1;
			if (param.typeDipe == ClsTypeDipesMagnetiques.CD10)
				div += 1;

			param.adexe_c = new ClsDate(param.ddex).getYear() + "";
			param.afexe_c = new ClsDate(param.dfex).getYear() + "";

			param.aamm_deb = new ClsDate(param.ddex).getYearAndMonth();
			param.aamm_fin = new ClsDate(param.dfex).getYearAndMonth();

			//fnomNbrj = (ParamData) service.get(ParamData.class, new ParamDataPK(param.cdos, 99, "DIPEMAGNBJ", 1));
			fnomNbrj = service.findAnyColumnFromNomenclature(param.cdos, param.clang, "99", "DIPEMAGNBJ", "1");
			//fnomTxh= (ParamData) service.get(ParamData.class, new ParamDataPK(param.cdos, 99, "HORAIRE", 1));
			fnomTxh = service.findAnyColumnFromNomenclature(param.cdos, param.clang, "99", "HORAIRE", "1");
			//fnomTxhDiv= (ParamData) service.get(ParamData.class, new ParamDataPK(param.cdos, 99, "DIPDIVTXH", 1));
			fnomTxhDiv = service.findAnyColumnFromNomenclature(param.cdos, param.clang, "99", "DIPDIVTXH", "1");

		}
		catch (Exception e)
		{
			//LOG.error(e.getLocalizedMessage(),e);
			this.traitement = ClsTreater._getStackTrace(e);
		}
		finally
		{
			service.closeSession(session);
		}
	}

	ParamData fnomNbrj;
	ParamData fnomTxh;
	ParamData fnomTxhDiv;

	private Integer fmntCumul(String nmat, String liste_rub, String am_deb, String am_fin)
	{
		Integer sum = 0;

//		String query = " select sum(decode(a.rubq, ";
//		query += TypeBDUtil.souschaine(":liste_rub", 1, 4)+/*"substr(:liste_rub,1,4)"*/",decode("+TypeBDUtil.souschaine(":liste_rub", 5, 1)+/*"substr(:liste_rub,5,1)"*/",'+',a.mont,'-',-a.mont,0), ";
//		query += TypeBDUtil.souschaine(":liste_rub", 6, 4)+/*"substr(:liste_rub,6,4)"*/",decode("+TypeBDUtil.souschaine(":liste_rub", 10, 1)+/*"substr(:liste_rub,10,1)"*/",'+',a.mont,'-',-a.mont,0), ";
//		query += TypeBDUtil.souschaine(":liste_rub", 11, 4)+/*"substr(:liste_rub,11,4)"*/",decode("+TypeBDUtil.souschaine(":liste_rub", 15, 1)+/*"substr(:liste_rub,15,1)"*/",'+',a.mont,'-',-a.mont,0), ";
//		query += TypeBDUtil.souschaine(":liste_rub", 16, 4)+/*"substr(:liste_rub,16,4)"*/",decode("+TypeBDUtil.souschaine(":liste_rub", 20, 1)+/*"substr(:liste_rub,20,1)"*/",'+',a.mont,'-',-a.mont,0), ";
//		query += TypeBDUtil.souschaine(":liste_rub", 21, 4)+/*"substr(:liste_rub,21,4)"*/",decode("+TypeBDUtil.souschaine(":liste_rub", 25, 1)+/*"substr(:liste_rub,25,1)"*/",'+',a.mont,'-',-a.mont,0), ";
//		query += TypeBDUtil.souschaine(":liste_rub", 26, 4)+/*"substr(:liste_rub,26,4)"*/",decode("+TypeBDUtil.souschaine(":liste_rub", 30, 1)+/*"substr(:liste_rub,30,1)"*/",'+',a.mont,'-',-a.mont,0) ";
//		query += ") ";
//		query += ") ";
//		query += " from CumulPaiea ";
//		query += "where idEntreprise = :cdos ";
//		query += "and nmat = :nmat ";
//		query += "and aamm between :am_deb and :am_fin ";
		String query = " select sum((case a.rubq ";
		query += " when "+TypeBDUtil.souschaine(":liste_rub", 1, 4)+/*"substr(:liste_rub,1,4)"*/" then (case "+TypeBDUtil.souschaine(":liste_rub", 5, 1)+/*"substr(:liste_rub,5,1)"*/" when '+' then a.mont when '-' then -a.mont else 0 end) ";
		query += " when "+TypeBDUtil.souschaine(":liste_rub", 6, 4)+/*"substr(:liste_rub,6,4)"*/" then (case "+TypeBDUtil.souschaine(":liste_rub", 10, 1)+/*"substr(:liste_rub,10,1)"*/" when '+' then a.mont when '-' then -a.mont else 0 end) ";
		query += " when "+TypeBDUtil.souschaine(":liste_rub", 11, 4)+/*"substr(:liste_rub,11,4)"*/" then (case "+TypeBDUtil.souschaine(":liste_rub", 15, 1)+/*"substr(:liste_rub,15,1)"*/" when '+' then a.mont when '-' then -a.mont else 0 end) ";
		query += " when "+TypeBDUtil.souschaine(":liste_rub", 16, 4)+/*"substr(:liste_rub,16,4)"*/" then (case "+TypeBDUtil.souschaine(":liste_rub", 20, 1)+/*"substr(:liste_rub,20,1)"*/" when '+' then a.mont when '-' then -a.mont else 0 end) ";
		query += " when "+TypeBDUtil.souschaine(":liste_rub", 21, 4)+/*"substr(:liste_rub,21,4)"*/" then (case "+TypeBDUtil.souschaine(":liste_rub", 25, 1)+/*"substr(:liste_rub,25,1)"*/" when '+' then a.mont when '-' then -a.mont else 0 end) ";
		query += " when "+TypeBDUtil.souschaine(":liste_rub", 26, 4)+/*"substr(:liste_rub,26,4)"*/" then (case "+TypeBDUtil.souschaine(":liste_rub", 30, 1)+/*"substr(:liste_rub,30,1)"*/" when '+' then a.mont when '-' then -a.mont else 0 end) ";
		query += " else 0 end) ";
		query += ") ";
		query += " from CumulPaiea ";
		query += "where idEntreprise = :cdos ";
		query += "and nmat = :nmat ";
		query += "and aamm between :am_deb and :am_fin ";
		Session session = service.getSession();
		try
		{
			Query q = session.createSQLQuery(query);
			q.setParameter("liste_rub", liste_rub);
			q.setParameter("cdos", param.cdos);
			q.setParameter("nmat", nmat);
			q.setParameter("am_deb", am_deb);
			q.setParameter("am_fin", am_fin);
			List<Object> lst = q.list();
			if (lst.get(0) != null)
				sum = new Integer(lst.get(0).toString());
		}
		catch (Exception e)
		{
			//LOG.error(e.getLocalizedMessage(),e);
			this.traitement = ClsTreater._getStackTrace(e);
		}
		finally
		{
			service.closeSession(session);
		}

		return sum;
	}

	public GeneriqueConnexionService getService()
	{
		return service;
	}

	public void setService(GeneriqueConnexionService service)
	{
		this.service = service;
	}

	public ClsParameterOfDipe getParam()
	{
		return param;
	}

	public void setParam(ClsParameterOfDipe param)
	{
		this.param = param;
	}



	public String getPhase()
	{
		return phase;
	}

	public void setPhase(String phase)
	{
		this.phase = phase;
	}

	public HttpServletRequest getRequest()
	{
		return request;
	}

	public void setRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	public String getTraitement()
	{
		return traitement;
	}

	public void setTraitement(String traitement)
	{
		this.traitement = traitement;
	}

}
