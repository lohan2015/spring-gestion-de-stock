package com.kinart.paie.business.services.precentralisation;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.kinart.paie.business.model.*;
import com.kinart.paie.business.services.cloture.ClsNomenclature;
import com.kinart.paie.business.services.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.springframework.dao.DataAccessException;

public class ClsAxPrecentralisation
{

	GeneriqueConnexionService service;

	String message;

	String dt;

	String toprint;

	String iserrormessage;

	String isendblock;

	String isblockingmessage;

	public ClsInitPrecentralisationPaie oInitialisationPrecentralisation;

	public UIPrecentralisation ui;

	private static int DEFAULT_NUMBER_OF_SALARY_TO_PROCEED = 0;

	public String getDt()
	{
		return dt;
	}

	public void setDt(String dt)
	{
		this.dt = dt;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public String getToprint()
	{
		return toprint;
	}

	public void setToprint(String toprint)
	{
		this.toprint = toprint;
	}

	public String getIserrormessage()
	{
		return iserrormessage;
	}

	public void setIserrormessage(String iserrormessage)
	{
		this.iserrormessage = iserrormessage;
	}

	public String getIsendblock()
	{
		return isendblock;
	}

	public void setIsendblock(String isendblock)
	{
		this.isendblock = isendblock;
	}

	public String getIsblockingmessage()
	{
		return isblockingmessage;
	}

	public void setIsblockingmessage(String isblockingmessage)
	{
		this.isblockingmessage = isblockingmessage;
	}

	public ClsAxPrecentralisation getCurrentObjectLog(String strIndex, HttpServletRequest request)
	{

		int index = Integer.valueOf(strIndex);
		ClsAxPrecentralisation o = new ClsAxPrecentralisation();
		o.message = "end";
		o.dt = "17-01-2008";
		o.setToprint("0");
		SimpleDateFormat formatDate = new SimpleDateFormat("hh'h'mm'min'ss");
		Object oList = request.getSession().getAttribute(ClsSessionObjectName.SESSION_O_PRECENTRALISATION_LISTE);
		if (oList != null)
		{
			List<ClsRhtLogEtendu> listeLogEtendu = (List<ClsRhtLogEtendu>) oList;

			if (listeLogEtendu != null)
				if (listeLogEtendu.size() > index)
				{
					o.setMessage(listeLogEtendu.get(index).getMessageLigne());
					o.setDt(formatDate.format(listeLogEtendu.get(index).getRhtlog().getDatc()));
					o.setToprint("1");
					if (listeLogEtendu.get(index).isErrormessage() == true)
					{
						o.setIserrormessage("O");
						if (listeLogEtendu.get(index).isIsblockingmessage() == true)
							o.setIsblockingmessage("O");
						else o.setIsblockingmessage("N");
					}
					else
					{
						o.setIserrormessage("N");
						o.setIsblockingmessage("N");
						if (listeLogEtendu.get(index).isIsendblock())
							o.setIsendblock("O");
						else o.setIsendblock("N");
					}
				}
		}
		return o;
	}

	public String[] getCurrentObjectAsArray(HttpServletRequest request)
	{
		ParameterUtil.println("Dans getcurr array");
		String[] o = new String[] { "", "" };
		o[0] = "end";
		o[1] = "17-01-2008";
		return o;
	}

	public String traitementPrecentralisation0(HttpServletRequest request, String aamm, String nbul, String nmat, String verif_edit_bul, String rub_a_comptabilise, String continu_si_erreur)
	{
		ParameterUtil.println("traitement pr�centraliation");

		return "Fin Traitement";
	}

	public ClsResultat enregisterLog(HttpServletRequest request)
	{
		ClsResultat resultat = null;

		//		Session sess = null;
		//		
		//		Transaction tx = null;

		List<ClsRhtLogEtendu> listeLogEtendu = (List<ClsRhtLogEtendu>) request.getSession().getAttribute(ClsSessionObjectName.SESSION_O_PRECENTRALISATION_LISTE);
		if (listeLogEtendu != null)
			if (listeLogEtendu.size() > 0)
			{
				try
				{
					//				sess = service.getSession();
					//				tx = sess.beginTransaction();

					for (int i = 0; i < listeLogEtendu.size(); i++)
					{
						//					sess.save(listeLogEtendu.get(i).getRhtlog());
						if (listeLogEtendu.get(i).isErrormessage())
							service.save(listeLogEtendu.get(i).getRhtlog());
					}
					//				tx.commit();
					resultat = ClsTreater._getResultat("Sauvegarde �ffectu�e avec succ�s", "INF-80178", false);
				}
				catch (DataAccessException e)
				{
					e.printStackTrace();
					//				if (tx != null)	tx.rollback();

					e.printStackTrace();

					resultat = ClsTreater._getResultat(ClsTreater._getStackTrace(e), null, true);
				}
				//			finally
				//			{
				//				service.closeConnexion(sess);
				//			}
			}

		return resultat;
	}

	public void updateRhtCalculTrTbRUBNAP(String strCalculWherePart, String strAgentWherePart, HttpServletRequest request)
	{
		String cdos = ParameterUtil.getSessionObject(request, ParameterUtil.SESSION_DOSSIER);

		String clang = ParameterUtil.getSessionObject(request, ParameterUtil.SESSION_LANGUE);

		ParamData oNome = null;//service.findFromNomenclature(cdos, clang, ClsNomenclature.TABLES_PARAMETRAGES_SYSTEME, ClsParameters.RUBRIQUE_NAP);

		String queryString = "From ParamData where idEntreprise='"+cdos+"' and ctab=99 and cacc='"+ParameterUtil.RUBRIQUE_NAP+"' and nume=1";
		List result = service.find(queryString);
		if(result.isEmpty()) oNome = null;
		else oNome = (ParamData)result.get(0);

		String crub = ParameterUtil._completerLongueurRubrique(oNome.getValm()+"");

		if (ParameterUtil._isStringNull(strCalculWherePart))
			strCalculWherePart = "1=1";

		if (ParameterUtil._isStringNull(strAgentWherePart))
			strAgentWherePart = "1=1";

		queryString = "Update CalculPaie cal set cal.trtb='2' where cal.idEntreprise='" + cdos + "' and rubq='" + crub + "' and " + strCalculWherePart;
		queryString += " and exists (Select 1 From Salarie agent where agent.idEntreprise=cal.idEntreprise and agent.nmat=cal.nmat and " + strAgentWherePart + ")";

		ParameterUtil.println("Update rhtcalcul query = " + queryString);
		try
		{
			this.getService().updateFromTable(queryString);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public boolean initTraitement(HttpServletRequest request, String aamm, String nbul, String nmat, String verif_edit_bul, String rub_a_comptabilise, String continu_si_erreur, String uniquement_message_erreur)
	{
		try
		{
			String cdos = ParameterUtil.getSessionObject(request, ParameterUtil.SESSION_DOSSIER);

			String user = ParameterUtil.getSessionObject(request, ParameterUtil.SESSION_LOGIN);

			String clang = ParameterUtil.getSessionObject(request, ParameterUtil.SESSION_LANGUE);

			oInitialisationPrecentralisation = new ClsInitPrecentralisationPaie(cdos, aamm, nbul, nmat, verif_edit_bul, rub_a_comptabilise, continu_si_erreur, uniquement_message_erreur);

			oInitialisationPrecentralisation.setClang(clang);

			oInitialisationPrecentralisation.setService(service);

			oInitialisationPrecentralisation.setUser(user);

			oInitialisationPrecentralisation.setRequest(request);

			oInitialisationPrecentralisation.setUi(ui);

			oInitialisationPrecentralisation.init();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean checkcostcenter = false;
	public boolean checkemail = false;

	public boolean traitementPrecentralisation(String cdos, HttpServletRequest request, String aamm, String nbul, String nmat, String verif_edit_bul, String rub_a_comptabilise, String continu_si_erreur, String uniquement_message_erreur)
	{

		/******************************BLOC DE DECLARATION DES VARIABLES GLOBALES*************************************/
		int N; //-- nombre total de salari�s � traiter
		int nb_sal; //NUMBER(10); -- nombre de salari�s trait�s
		int nb_err = 0; //NUMBER(6); -- nombre d'erreurs rencontr�es
		boolean w_edit; // indique si les bulletins non-�dit�s sont test�s
		boolean w_noed; // indique s'il existe des bulletins non-�dit�s
		boolean ut_int = false; // indique une interruption utilisateur
		String rub_brut; // code rubrique salaire brut

		// user defined exception
		Exception EX_INT_UT; // exception interruption utilisateur
		Exception EX_ERR_PRECI; // exception erreur pr�centralisation

		/**********************************SUITE DES VARIABLES********************************************************/
		int ii;//INTEGER; -- indice de boucle for

		boolean ya_c_pacalc;//BOOLEAN; --indique si des rubriques ont �t� trait�es pour le matricule
		boolean deltacom;//BOOLEAN; -- indique si la compta delta est install�e
		int N1;//NUMBER(10); -- nombre de lignes de RG_CRUB
		int N1P;//NUMBER(10); -- index de la derni�re rubriques acc�d�e
		int N2;//NUMBER(10); -- nombre de lignes de RG_TYPR
		int i_crub;//INTEGER; -- index de la rubrique dans le record group
		int i_typr;//INTEGER; -- index du type dans le RG

		int ind_deb;//NUMBER(2); -- indice compte au d�bit
		int ind_cre;//NUMBER(2); -- indice compte au cr�dit
		int ind_typr = 0;//NUMBER(2); -- code num�rique du type de rubrique

		String rub_nap;//VARCHAR2(4); -- code rubrique net � payer

		BigDecimal num_pret;//NUMBER(10); -- num�ro du pr�t

		boolean ex_cpt;//BOOLEAN; -- indique si le compte existe
		String cpt_deb;//parubq.de01%TYPE; -- compte � cr�diter
		String cpt_cre;//parubq.cr01%TYPE; -- compte � d�biter
		String cpt_sens;//VARCHAR2(1); -- sens du mouvement sur le compte

		BigDecimal somme_debit = new BigDecimal(0);//DECIMAL(15,3); -- somme des d�bits
		BigDecimal somme_credit = new BigDecimal(0);//DECIMAL(15,3); -- somme des cr�dits

		boolean s_ana;//BOOLEAN; -- indique si la sana existe

		Salarie oAgent = null;
		ClsNomenclature oNome = null;
		Cpcpt oCpcpt = null;
		Cpsec oCpsec = null;

		List<CalculPaie> oCalculListe;
		CalculPaie oCalcul = null;
		ElementSalaire oRubrique = null;
		int tempInt;
		/*************************************************************************************************************/
		//String cdos = ParameterUtil.getSessionObject(request, ParameterUtil.SESSION_DOSSIER);

		String user = ParameterUtil.getSessionObject(request, ParameterUtil.SESSION_LOGIN);

		String clang = ParameterUtil.getSessionObject(request, ParameterUtil.SESSION_LANGUE);

		List<ClsRhtLogEtendu> listeLogEtendu = new ArrayList<ClsRhtLogEtendu>();

		request.getSession().setAttribute(ClsSessionObjectName.SESSION_O_PRECENTRALISATION_LISTE, listeLogEtendu);

		initTraitement(request, aamm, nbul, nmat, verif_edit_bul, rub_a_comptabilise, continu_si_erreur, uniquement_message_erreur);

		ClsResultat oResult = null;

		// Determination du nom du client pour la gestion des sp�cifiques
		String nomClient = "CDI";//ClsConfigurationParameters.getConfigParameterValue(service, cdos, clang, ClsConfigurationParameters.NOM_CLIENT);

		/******************************DEBUT DE TOUS LES TRAITEMENTS*************************************/
		Date deb = new Date();
		String strdeb = ClsTreater._getResultat("Starting process on", "INF-10224", true).getLibelle()+" "+(new ClsDate(deb).getDateS("dd/MM/yyyy HH:mm:ss"));
		oResult = ClsTreater._getResultat(strdeb, null, true);
		if (!oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult))
			return false;
		
		//Affichage du message Initialisation....
		//oResult = ClsTreater._getResultat("Initialisation ...", "INF-10112", false);
		oResult = ClsTreater._getResultat("Initialisation ...", "INF-10112", true);
		if (!oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult))
			return false;

		/******************************CONTROLE EXISTENCE RUBRIQUE BRUT*********************************/
		ClsCheckNomenclature oCheckNomeRubBrut = oInitialisationPrecentralisation._verifyRubriqueBrutExistance();
		//cas ou la cl� n'existe pas en nomenclature
		if (!oCheckNomeRubBrut.isKeyexist())
		{
			oResult = ClsTreater._getResultat("Cr�er le param�tre RUBBRUT en table 99", "INF-10113", true);
			oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult, true);
			return false;
		}
		else
		{
			//cas ou la cl� existe, mais le montant n'est pas renseign�
			if (!oCheckNomeRubBrut.isColumnexist())
			{
				oResult = ClsTreater._getResultat("ERREUR : Le montant 1 de la cle RUBBRUT en table 99 doit contenir le num�ro de la rubrique du salaire brut.", "INF-10114", true);
				oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult, true);
				return false;
			}
			else
			{
				rub_brut = oCheckNomeRubBrut.getColumnvalue();
				oResult = ClsTreater._getResultat("Rubrique SALAIRE BRUT :", "INF-10115", false);
				oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oCheckNomeRubBrut.getColumnvalue(), null, false), null);
				if (!oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult))
					return false;
			}
		}
		/******************************FIN CONTROLE EXISTENCE RUBRIQUE BRUT*********************************/

		/******************************CONTROLE EXISTENCE RUBRIQUE NET A PAYER*********************************/
		ClsCheckNomenclature oCheckNomeNAP = oInitialisationPrecentralisation._verifyRubriqueNetAPayerExistance();
		//cas ou la cl� n'existe pas en nomenclature
		if (!oCheckNomeNAP.isKeyexist())
		{
			oResult = ClsTreater._getResultat("Cr�er le param�tre RUBNAP en table 99", "INF-10116", true);
			oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult, true);
			return false;
		}
		else
		{
			//cas ou la cl� existe, mais le montant n'est pas renseign�
			if (!oCheckNomeNAP.isColumnexist())
			{
				oResult = ClsTreater._getResultat("ERREUR : Le montant 1 de la cl� RUBNAP en table 99 doit contenir le num�ro de la rubrique du Net � Payer.", "INF-10117", true);
				oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult, true);
				return false;
			}
			else
			{
				rub_nap = oCheckNomeNAP.getColumnvalue();
				oResult = ClsTreater._getResultat("Rubrique NET A PAYER : ", "INF-10118", false);
				oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oCheckNomeNAP.getColumnvalue(), null, false), null);
				if (!oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult))
					return false;
			}
		}
		/******************************FIN CONTROLE EXISTENCE RUBRIQUE NET A PAYER*********************************/

		/******************************CONTROLE UTILISATION DE LA COMPTA DELTA*********************************/
		if ("O".equalsIgnoreCase(oInitialisationPrecentralisation.getRub_a_comptabilise()))
		{
			deltacom = true;
			oResult = ClsTreater._getResultat("Comptabilit� DELTA d�tect�e.", "INF-10119", false);
			if (!oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult))
				return false;
		}
		else
		{
			deltacom = false;
//			oResult = ClsTreater._getResultat("Comptabilit� DELTA non install�e.", "INF-10120", true);
//			if (!oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult))
//				return false;
		}
		/******************************FIN CONTROLE UTILISATION DE LA COMPTA DELTA*********************************/

		/******************************RECUPERATION DU NOMBRE TOTAL DE SALARIES A TRAITER****************************/
		List<Salarie> oAgentListe = oInitialisationPrecentralisation.getListeOfSalary();
		N = oAgentListe.size();
		oResult = ClsTreater._getResultat("Effectif � traiter : ", "INF-10121", true);
		oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(String.valueOf(oAgentListe.size()), null, true), null);
		if (!oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult))
			return false;
		/******************************FIN RECUPERATION DU NOMBRE TOTAL DE SALARIES A TRAITER*********************************/

		/******************************RECUPERATION DU NOMBRE TOTAL DE RUBRIQUES A TRAITER****************************/
		List<ElementSalaire> oRubriqueListe = oInitialisationPrecentralisation.getAllRubriqueListe();
		N1 = oRubriqueListe.size();
		oResult = ClsTreater._getResultat("Nombre total de rubriques : ", "INF-10122", false);
		oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(String.valueOf(oRubriqueListe.size()), null, false), null);
		if (!oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult))
			return false;
		if (oRubriqueListe.size() == 0)
		{
			oResult = ClsTreater._getResultat("Il n''existe aucune rubrique dans le plan de paie.", "INF-10123", true);
			oResult = ClsTreater._concat(ClsTreater._getResultat("ERREUR : ", "INF-10094", false), oResult, null);
			oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult, true);
			nb_err = nb_err + 1;
			oInitialisationPrecentralisation.incrementerErreurs();
			return false;
		}
		/******************************FIN RECUPERATION DU NOMBRE TOTAL DE RUBRIQUES A TRAITER*********************************/

		/******************************RECUPERATION DU NOMBRE TOTAL DE RUBRIQUES A COMPTABILISER****************************/
		List<ElementSalaire> oRubriqueAComptabiliserListe = oInitialisationPrecentralisation.getRubriqueAComptabiliserListe();
		N1 = oRubriqueAComptabiliserListe.size();
		oResult = ClsTreater._getResultat("Rubriques � comptabiliser :  ", "INF-10124", false);
		oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(String.valueOf(oRubriqueAComptabiliserListe.size()), null, false), null);
		if (!oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult))
			return false;
		if (oRubriqueAComptabiliserListe.size() == 0)
		{
			oResult = ClsTreater._getResultat("REMARQUE : Aucune rubrique d�tect�e pour la comptabilisation. ", "INF-10125", true);
			oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("Il n''y aura pas de v�rification de l''�quilibre comptable des bulletins.", "INF-10126", true), null);
			if (!oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult))
				return false;
		}
		/******************************FIN RECUPERATION DU NOMBRE TOTAL DE RUBRIQUES A TRAITER*********************************/

		/***************************************************************************************/
		/*                          TRAITEMENTS SUR LES BULLETINS                              */
		/***************************************************************************************/

		oResult = ClsTreater._getResultat("Traitement ...", "INF-10127", true);
		if (!oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult))
			return false;

		nb_sal = 0;

		/*** RESEIGNEMENT DES RECORDS GROUPS SUR LES COMPTES AFFECTES AUX RUBRIQUES ET SUR LES TYPES DE RUBRIQUES ***/
		N1 = oInitialisationPrecentralisation.getAllRubriqueListe().size();
		N2 = oInitialisationPrecentralisation.getDonneesNomenclature(28).size();

		int otherNbSal = 0;

		int zoneLibreEmail = 0;
		ParamData nome = null;//(Rhfnom) service.get(Rhfnom.class, new RhfnomPK(cdos, 99, "EMAIL", 1));
		String queryString = "From ParamData where idEntreprise='"+cdos+"' and ctab=99 and cacc='EMAIL' and nume=1";
		List result = service.find(queryString);
		if(result.isEmpty()) nome = null;
		else nome = (ParamData)result.get(0);

		if (nome != null && nome.getValm() != null && nome.getValm() != 0)
			zoneLibreEmail = nome.getValm().intValue();

		//--pour le cost center
		String vall1 = "";
		int valm1 = 0;
		String vall2 = "";
		String w_cleCostCenter = "";
		//nome = (Rhfnom) service.get(Rhfnom.class, new RhfnomPK(cdos, 99, "INTER-GL", 1));
		queryString = "From ParamData where idEntreprise='"+cdos+"' and ctab=99 and cacc='INTER-GL' and nume=1";
		result = service.find(queryString);
		if(result.isEmpty()) nome = null;
		else nome = (ParamData)result.get(0);

		if (nome != null)
		{
			vall1 = nome.getVall();
			valm1 = nome.getValm() != null ? nome.getValm().intValue() : 0;

			//--R�cup�ration de la zone de cost center
			if (valm1 == 0 || StringUtils.isNotBlank(vall1))
			{
				//nome = (Rhfnom) service.get(Rhfnom.class, new RhfnomPK(cdos, valm1, vall1, 2));
				queryString = "From ParamData where idEntreprise='"+cdos+"' and ctab="+valm1+" and cacc='"+vall1+"' and nume=2";
				result = service.find(queryString);
				if(result.isEmpty()) nome = null;
				else nome = (ParamData)result.get(0);
				if (nome != null)
				{
					vall2 = nome.getVall();
					if (StringUtil.equalsIgnoreCase(StringUtil.oraSubstring(vall2, 1, 1), "@"))
					{
						w_cleCostCenter = StringUtil.oraSubstring(vall2, 9, 3);
					}
					else w_cleCostCenter = vall2;
				}
			}
		}

		if (ui != null)
			ui.debutAffichageProgressBar(oAgentListe.size());

		setAffichageProgression(otherNbSal, N, request);

		for (int i = 0; i < oAgentListe.size(); i++)
		{
			otherNbSal += 1;
			setAffichageProgression(otherNbSal, N, request);

			oAgent = oAgentListe.get(i);
			oInitialisationPrecentralisation.writeInTextLog("--------------------------------SALARIE " + oAgent.getNmat() + " " + oAgent.getNom() + " " + oAgent.getPren() + "-------------");
			//			  --pr_rpo('Matricule ' || wsal01.nmat || ' ...',0,0,nb_sal,N);
			//			    pr_rpo(fmsg('INF-00060')||' '|| wsal01.nmat || ' ...',0,0,nb_sal,N);
			oResult = ClsTreater._getResultat("Matricule ", "INF-00060", false);
			oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oAgent.getNmat(), null, false), " ");
			oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("...,", null, false), " ");
			if (!oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult))
				return false;

			ind_deb = 0;
			ind_cre = 0;
			//-- grad
			oNome = oInitialisationPrecentralisation.findFromNomenclature(oAgent.getGrad(), oInitialisationPrecentralisation.getDonneesNomenclature(6));
			if (ParameterUtil._isStringNull(oNome.getCode()))
			{
				oResult = ClsTreater._getResultat("Matricule ", "INF-00060", true);
				oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oAgent.getNmat(), null, true), " ");
				oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(" / Table 06 : Titre inexistant.", "INF-10128", true), " ");
				oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("Un titre est affect� au matricule, alors que ce titre n''existe plus en table 06", "INF-10129", true), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
				oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("=> Changer le titre du matricule ou re-cr�er le titre en table 06.", "INF-10130", true), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
				oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult, true);
				return false;
			}

			ind_cre = Integer.valueOf(ParameterUtil._isStringNull(oNome.getMontant()) == true ? "0" : oNome.getMontant()) + 1;

			// -- nationalit�
			oNome = oInitialisationPrecentralisation.findFromNomenclature(oAgent.getNato(), oInitialisationPrecentralisation.getDonneesNomenclature(4));
			if (ParameterUtil._isStringNull(oNome.getCode()))
			{
				oResult = ClsTreater._getResultat("Matricule ", "INF-00060", true);
				oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oAgent.getNmat(), null, true), " ");
				oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(" / Table 04 : Nationalit� inexistante.", "INF-10131", true), " ");
				oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("Une nationalit� est affect�e au matricule, alors que cette nationalit� n''existe plus en table 04", "INF-10132", true), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
				oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("=> Changer la nationalit� du matricule ou re-cr�er la nationalit� en table 04.", "INF-10133", true), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
				oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult, true);
				return false;
			}

			ind_deb = ((ind_cre - 1) * 4) + 1 + Integer.valueOf(ParameterUtil._isStringNull(oNome.getMontant()) == true ? "0" : oNome.getMontant());

			//-- nombre de jours travaill�s
			if (oAgent.getNbjtr() != null)
			{
				if (oAgent.getNbjtr().intValue() > 995)
				{
					oResult = ClsTreater._getResultat("ERREUR :", "INF-10094", true);
					oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("Matricule ", "INF-00060", true), " ");
					oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oAgent.getNmat(), null, true), " ");
					oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(" / Nombre de jours travaill�s = ", "INF-10134", true), " ");
					oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oAgent.getNbjtr().toString(), null, true), " ");
					oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("Le nombre de jours travaill�s risque de d�passer 999 � la prochaine cl�ture.", "INF-10135", true), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
					oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("=> Modifier ce nombre dans la fiche salari� �cran historique cong�s.", "INF-10136", true), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
					if (!oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult))
						return false;
					nb_err += 1;
					oInitialisationPrecentralisation.incrementerErreurs();
					if ("N".equalsIgnoreCase(oInitialisationPrecentralisation.getContinu_si_erreur()))
						return false;
				}

			}
			//-- date de naissance
			//On param�tre en table 99, cl� AGE, montant 1 l'age d'alerte que l'agent ne devrait pas d�passer
			int age = 70;
			oNome = oInitialisationPrecentralisation.findFromNomenclature("AGEMAXSAL", oInitialisationPrecentralisation.getDonneesNomenclature(99));
			if (!ParameterUtil._isStringNull(oNome.getCode()) && !ParameterUtil._isStringNull(oNome.getMontant()))
				age = Integer.valueOf(oNome.getMontant());
			if (ClsDate.getMonthsBetween(oAgent.getDtna(), new Date()) > age*12 /*840*/)
			{
				oResult = ClsTreater._getResultat("ATTENTION :", "INF-10137", true);
				oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("Matricule ", "INF-00060", true), " ");
				oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oAgent.getNmat(), null, true), " ");
				oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(" / Age calcul� > 70 ans", "INF-10138", true), " ");
				oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("L''�ge du salari� est sup�rieur � 70 ans.", "INF-10139", true), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
				oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("=> V�rifier la date de naissance dans la fiche salari�.", "INF-10140", true), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
				if (!oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult))
					return false;
				if ("N".equalsIgnoreCase(oInitialisationPrecentralisation.getContinu_si_erreur()))
					return false;
			}

			//-- date de d�part calcul de l'anciennet�
			//		    if(ClsDate.getMonthsBetween(oAgent.getDdca(), new Date()) > 600)
			//		    {
			//		    	oResult = ClsTreater._getResultat("ERREUR :", "INF-10094", true);
			//	    		oResult = ClsTreater._concat(oResult,ClsTreater._getResultat("Matricule ", "INF-00060", true), " ");
			//				oResult = ClsTreater._concat(oResult,ClsTreater._getResultat(oAgent.getNmat(), null, true), " ");
			//				oResult = ClsTreater._concat(oResult,ClsTreater._getResultat(" / Anciennet� calcul�e > 50 ans.", "INF-10141", true), " ");
			//				oResult = ClsTreater._concat(oResult,ClsTreater._getResultat("L''anciennet� du salari� est sup�rieure � 50 ans.", "INF-10142", true), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
			//				oResult = ClsTreater._concat(oResult,ClsTreater._getResultat("=> V�rifier la date de d�part de calcul de l''anciennet� dans la fiche salari�.", "INF-10143", true), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
			//				if(! oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult)) return false;
			//				nb_err += 1;	
			//		    	oInitialisationPrecentralisation.incrementerErreurs();
			//				if("N".equalsIgnoreCase(oInitialisationPrecentralisation.getContinu_si_erreur())) return false;
			//		    }

			//-- date d'entr�e dans la soci�t�
			//		    if(ClsDate.getMonthsBetween(oAgent.getDtes(), new Date()) > 600)
			//		    {
			//		    	oResult = ClsTreater._getResultat("ERREUR :", "INF-10094", true);
			//	    		oResult = ClsTreater._concat(oResult,ClsTreater._getResultat("Matricule ", "INF-00060", true), " ");
			//				oResult = ClsTreater._concat(oResult,ClsTreater._getResultat(oAgent.getNmat(), null, true), " ");
			//				oResult = ClsTreater._concat(oResult,ClsTreater._getResultat(" / Entr�e dans la soci�t� > 50 ans.", "INF-10144", true), " ");
			//				oResult = ClsTreater._concat(oResult,ClsTreater._getResultat("La date d''entr�e dans la soci�t� remonte � plus de 50 ans.", "INF-10145", true), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
			//				oResult = ClsTreater._concat(oResult,ClsTreater._getResultat("=> V�rifier la date d''entr�e dans la soci�t� dans la fiche salari�.", "INF-10146", true), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
			//				if(! oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult)) return false;
			//				nb_err += 1;	
			//		    	oInitialisationPrecentralisation.incrementerErreurs();
			//				if("N".equalsIgnoreCase(oInitialisationPrecentralisation.getContinu_si_erreur())) return false;
			//		    }

			//Controle de l'email
			if (checkemail)
			{
				//--R�cup�ration adresse email pour effectuer certains contr�le.
				if (zoneLibreEmail != 0)
				{
					String w_adresseEmail = "";
					boolean email = true;
					FreeZone zl = null;//(Rhtzonelibre) service.get(Rhtzonelibre.class, new RhtzonelibrePK(cdos, oAgent.getNmat(), zoneLibreEmail));
					queryString = "From FreeZone where idEntreprise='"+cdos+"' and nmat='"+oAgent.getNmat()+"' and numerozl="+zoneLibreEmail;
					result = service.find(queryString);
					if(result.isEmpty()) zl = null;
					else zl = (FreeZone)result.get(0);

					if (zl != null)
						w_adresseEmail = zl.getVallzl();
					if (StringUtils.isBlank(w_adresseEmail))
						email = false;
					else
					{
						if (StringUtils.countMatches(w_adresseEmail, " ") > 0) //--Controle que l'adresse ne comporte pas d'espace
							email = false;
						if (StringUtils.countMatches(w_adresseEmail, "@") == 0) //--Controle que l'adresse comporte bien le "@"
							email = false;
					}
					//--V�rification adresses emails
					if (!email)
					{
						oResult = ClsTreater._getResultat("ATTENTION :", "INF-10137", true);
						oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("Matricule ", "INF-00060", true), " ");
						oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oAgent.getNmat(), null, true), " ");
						oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("Adresse email non valide", "INF-01619", true), " ");
						if (!oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult))
							return false;
						if ("N".equalsIgnoreCase(oInitialisationPrecentralisation.getContinu_si_erreur()))
							return false;
					}
				}
			}
			//
			//
			//			--TFN 10/12/2012 - Verification pr�sence du cost center
			if (checkcostcenter)
			{
				if (StringUtils.isNotBlank(w_cleCostCenter))
				{
					if (StringUtil.in(oInitialisationPrecentralisation.fn_costCenter(w_cleCostCenter, oAgent), oInitialisationPrecentralisation.fn_getZero(1) + "," + oInitialisationPrecentralisation.fn_getZero(2) + "," + oInitialisationPrecentralisation.fn_getZero(3) + "," + oInitialisationPrecentralisation.fn_getZero(4) + "," + oInitialisationPrecentralisation.fn_getZero(5) + ","
							+ oInitialisationPrecentralisation.fn_getZero(6) + "," + oInitialisationPrecentralisation.fn_getZero(7) + "," + oInitialisationPrecentralisation.fn_getZero(8) + "," + oInitialisationPrecentralisation.fn_getZero(9) + "," + oInitialisationPrecentralisation.fn_getZero(10)))
					{
						oResult = ClsTreater._getResultat("ATTENTION :", "INF-10137", true);
						oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("Matricule ", "INF-00060", true), " ");
						oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oAgent.getNmat(), null, true), " ");
						oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("Centres de co�ts invalide", "INF-01647", true), " ");
						if (!oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult))
							return false;
						if ("N".equalsIgnoreCase(oInitialisationPrecentralisation.getContinu_si_erreur()))
							return false;
					}
				}
			}

			/***************************************************************************************/
			/*          initialisation du traitement des rubriques du matricule                    */
			/***************************************************************************************/
			somme_credit = new BigDecimal(0);
			somme_debit = new BigDecimal(0);
			w_edit = true;
			ya_c_pacalc = false;
			N1P = 1;

			//		    --------------------------------------------
			//		    -- curseur sur rubriques pacalc du matricule
			//		    --------------------------------------------
			oCalculListe = oInitialisationPrecentralisation.getListeOfRhtcalcul(oAgent.getNmat());
			for (int j = 0; j < oCalculListe.size(); j++)
			{
				oCalcul = oCalculListe.get(j);
				if (oCalcul.getNmat().equals(oAgent.getNmat()))
				{
					ya_c_pacalc = true;

					oResult = ClsTreater._getResultat("Rubrique", "INF-00082", false);
					oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oCalcul.getRubq(), null, false), " ");
					if (!oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult))
						return false;

					//					-- Chargement de la rubrique de base si la rubrique calculee
					//				      -- est un rappel ou un trop percu
					if (!ParameterUtil._isStringNull(oCalcul.getRuba()))
					{
						oCalcul.setRubq(oCalcul.getRuba());
						oResult = ClsTreater._getResultat(" rubrique associ�e ", "INF-10148", false);
						oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oCalcul.getRuba(), null, false), " ");
						if (!oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult))
							return false;
					}

					//					 -- test existence de la rubrique
					oRubrique = oInitialisationPrecentralisation.existRubrique(oCalcul.getRubq());

					if (oRubrique == null)
					{
						if (!ParameterUtil._isStringNull(oCalcul.getRuba()))
						{
							oResult = ClsTreater._getResultat("Rubrique ", "INF-00082", true);
							oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oCalcul.getRubq(), null, true), " ");
							oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(" inexistante.", "INF-10149", true), " ");
							oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("Le plan de rubriques a �t� modifi� depuis le calcul", "INF-10152", true), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
							oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("=> V�rifier le plan de rubriques ou recalculer le bulletin.", "INF-10153", true), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
							oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult, true);
							return false;
						}
						else
						{
							oResult = ClsTreater._getResultat("Rubrique ", "INF-00082", true);
							oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oCalcul.getRubq(), null, true), " ");
							oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(" / Rubrique associ�e ", "INF-10148", true), " ");
							oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oCalcul.getRuba(), null, true), " ");
							oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(" inexistante.", "INF-10149", true), " ");
							oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("V�rifier la rubrique associ�e � l''�l�ment variable saisi sur la rubrique ", "INF-10150", true), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
							oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oCalcul.getRubq(), null, true), " ");
							oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("=> Modifier la rubrique associ�e ou saisir un �l�ment variable sur une rubrique sans rubrique associ�e, puis recalculer le bulletin.", "INF-10151", true), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
							oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult, true);
							return false;
						}
					}
					tempInt = oInitialisationPrecentralisation.indexOfRubrique(oRubrique);
					if (tempInt != -1)
					{
						i_crub = tempInt;
						N1P = tempInt;
					}

					//					 -- test existence du type rubrique
					if ("O".equalsIgnoreCase(oRubrique.getComp()))
					{
						oResult = ClsTreater._getResultat("Montant ", "INF-10154", false);
						oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(String.valueOf(oCalcul.getMont()), null, false), " ");
						oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("Type Rubrique", "INF-00210", false), " / ");
						oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oRubrique.getTypr(), null, false), " ");
						oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("Perception", "INF-10155", false), " / ");
						oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oRubrique.getCper(), null, false), " ");
						if (!oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult))
							return false;

						if (!oRubrique.getCrub().equals(oCheckNomeNAP.getColumnvalue()))
						{
							oNome = oInitialisationPrecentralisation.findFromNomenclature(oRubrique.getTypr(), oInitialisationPrecentralisation.getDonneesNomenclature(28));
							i_typr = oInitialisationPrecentralisation.indexOfNomenclature(oRubrique.getTypr(), 28);

							if (ParameterUtil._isStringNull(oNome.getCode()) || ParameterUtil._isStringNull(oNome.getMontant()) )
							{
								oResult = ClsTreater._getResultat("Rubrique ", "INF-00082", true);
								oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oCalcul.getRubq(), null, true), " ");
								oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("Table 28 : Type de rubrique inexistant.", "INF-10156", true), " / ");
								oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("Une rubrique existe avec un type non-r�pertori� dans la table 28", "INF-10157", true), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
								oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("=> Changer la type de la rubrique ou re-cr�er ce type en table 28.", "INF-10158", true), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
								oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult, true);
								return false;
							}
							
							
							ind_typr = Integer.parseInt(oNome.getMontant());
						} else ind_typr = 2;
					}

					//					-- test existence du pret
					if (!ParameterUtil._isStringNull(oCalcul.getNprt()))
					{
						//on transforme la chaine nprt en nombre nprt, si nprt=09887 alors nprt=9887
						oCalcul.setNprt(Integer.valueOf(oCalcul.getNprt().trim()).toString());

						oResult = ClsTreater._getResultat("Algorithme", "INF-01070", false);
						oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(String.valueOf(oRubrique.getAlgo()), null, false), " ");
						oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("Num�ro de pr�t", "INF-10159", false), " / ");
						oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oCalcul.getNprt(), null, false), " ");
						if (!oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult))
							return false;

						if (Integer.valueOf(String.valueOf(oRubrique.getAlgo())) == 13)
						{
							oNome = oInitialisationPrecentralisation.findFromPretAgent(oCalcul.getNprt(), oInitialisationPrecentralisation.getListePretsAgent(oCalcul.getNmat(), oCalcul.getNprt()));
							if (ParameterUtil._isStringNull(oNome.getCode()))
							{

								oResult = ClsTreater._getResultat("ERREUR :", "INF-10094", true);
								oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("Rubrique ", "INF-00082", true), null);
								oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oCalcul.getRubq(), null, true), " ");
								oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(" Pr�t ", "INF-00821", true), " / ");
								oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oCalcul.getNprt(), null, true), " ");
								oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(" inexistant en pr�ts internes.", "INF-10160", true), " ");
								oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("Le bulletin est calcul� avec le pr�t ", "INF-10161", true), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
								oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oCalcul.getNprt(), null, true), " ");
								oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(" inexistant en pr�ts internes du matricule", "INF-10162", true), " ");
								oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("=> Re-cr�er ce pr�t dans la fiche salari� du matricule.", "INF-10163", true), oAgent.getNmat() + ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
								if (!oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult))
									return false;
								nb_err = nb_err + 1;
								oInitialisationPrecentralisation.incrementerErreurs();
								if ("N".equalsIgnoreCase(oInitialisationPrecentralisation.getContinu_si_erreur()))
									return false;
							}
						}
						else
						{
							if (Integer.valueOf(String.valueOf(oRubrique.getAlgo())) == 17 || Integer.valueOf(String.valueOf(oRubrique.getAlgo())) == 20)
							{
								oNome = oInitialisationPrecentralisation.findFromPret(oCalcul.getNprt(), oInitialisationPrecentralisation.getListePretEnt());
								if (ParameterUtil._isStringNull(oNome.getCode()))
								{
									oResult = ClsTreater._getResultat("ERREUR :", "INF-10094", true);
									oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("Rubrique ", "INF-00082", true), null);
									oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oCalcul.getRubq(), null, true), " ");
									oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(" Pr�t ", "INF-00821", true), " / ");
									oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oCalcul.getNprt(), null, true), " ");
									oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(" inexistant en pr�ts externes.", "INF-10164", true), " ");
									oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("Le bulletin est calcul� avec le pr�t ", "INF-10161", true), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
									oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oCalcul.getNprt(), null, true), " ");
									oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(" inexistant en pr�ts externes", "INF-10164", true), " ");
									oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("=> Re-cr�er ce pr�t pour ce matricule dans la gestion des pr�ts externes.", "INF-10165", true), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
									if (!oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult))
										return false;
									nb_err = nb_err + 1;
									oInitialisationPrecentralisation.incrementerErreurs();
									if ("N".equalsIgnoreCase(oInitialisationPrecentralisation.getContinu_si_erreur()))
										return false;
								}
							}
							else
							{
								oResult = ClsTreater._getResultat("Rubrique ", "INF-00082", true);
								oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oCalcul.getRubq(), null, true), " ");
								oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("Algo ", "INF-01070", true), " / ");
								oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(String.valueOf(oRubrique.getAlgo()), null, true), " ");
								oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(" Pr�t ", "INF-00821", true), " / ");
								oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oCalcul.getNprt(), null, true), " ");
								oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("L''algorithme de traitement de pr�t est inconnu. Seuls les algorithmes 13 (pr�ts internes) et 17 & 20 (pr�ts externes) sont identifi�s.", "INF-10166", true), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
								oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("=> Utiliser un algorithme connu pour la rubrique ou r�pertorier le nouvel algorithme.", "INF-10167", true), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
								oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult, true);
								return false;
							}
						}
					}

					if ("O".equalsIgnoreCase(oRubrique.getComp()))
					{
						oInitialisationPrecentralisation.writeInTextLog("Rubrique = " + oCalcul.getRubq() + " " + oRubrique.getLrub());
						if ("O".equalsIgnoreCase(oRubrique.getCper()))
						{
							if (ParameterUtil._isStringNull(oCalcul.getNprt()))
							{
								Session session = service.getSession();
								List l = null;
								try {
									l = session.createSQLQuery("Select ncpt From PretExterneEntete where idEntreprise='" + cdos + "' and nmat='" + oAgent.getNmat() + "' and crub='" + oCalcul.getRubq() + "' order by nprt desc").list();
								} catch (Exception e) {
									// TODO: handle exception
								} finally {
									service.closeSession(session);
								}
								
								if (!ClsObjectUtil.isListEmty(l))
								{
									if (ClsObjectUtil.isNull(l.get(0))){
										oResult = ClsTreater._getResultat("Rubrique ", "INF-00082", true);
										oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oCalcul.getRubq(), null, true), " ");
										oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(" Compte pr�t salari� inexistant.", "INF-0", true), " / ");
										oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("La comptabilisation de la rubrique fait appel � un compte li� au matricule", "INF-10169", true), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
										oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("=> Renseigner ce num�ro de compte sur le pr�t du salari� de matricule "+oAgent.getNmat(), "INF-0", true), oAgent.getNmat() + ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
										oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult, true);
										return false;
									}
								} else if (ParameterUtil._isStringNull(oAgent.getCcpt()))
								{
									oResult = ClsTreater._getResultat("Rubrique ", "INF-00082", true);
									oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oCalcul.getRubq(), null, true), " ");
									oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(" Compte personnel salari� inexistant.", "INF-10168", true), " / ");
									oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("La comptabilisation de la rubrique fait appel � un compte li� au matricule", "INF-10169", true), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
									oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("=> Renseigner ce num�ro de compte dans la fiche salari� du matricule.", "INF-10170", true), oAgent.getNmat() + ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
									oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult, true);
									return false;
								}
							}
							else
							{
								oNome = oInitialisationPrecentralisation.findCompteFromPret(oCalcul.getNprt(), oInitialisationPrecentralisation.getListePretEnt());
								if (ParameterUtil._isStringNull(oNome.getCode()))
								{
									if (ParameterUtil._isStringNull(oAgent.getCcpt()))
									{
										oResult = ClsTreater._getResultat("Rubrique ", "INF-00082", true);
										oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oCalcul.getRubq(), null, true), " ");
										oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(" Compte personnel salari� inexistant.", "INF-10168", true), " / ");
										oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("La comptabilisation de la rubrique fait appel � un compte li� au matricule", "INF-10169", true), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
										oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("=> Renseigner ce num�ro de compte dans la fiche salari� du matricule.", "INF-10170", true), oAgent.getNmat() + ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
										oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult, true);
										return false;

									}
								}
							}

							if ("O".equalsIgnoreCase(oInitialisationPrecentralisation.getRub_a_comptabilise()))
							{
								oCpcpt = oInitialisationPrecentralisation.existCompteComptabilite(oAgent.getCcpt());
								if (oCpcpt == null)
								{
									ex_cpt = false;
									s_ana = false;
								}
								else
								{
									if (!"0".equals(oCpcpt.getCana()))
									{
										ex_cpt = true;
										s_ana = true;
									}
									else
									{
										ex_cpt = true;
										s_ana = false;
									}
								}

								if (!ex_cpt)
								{
									oResult = ClsTreater._getResultat("Rubrique ", "INF-00082", true);
									oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oCalcul.getRubq(), null, true), " ");
									oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("Compte personnel salari� inexistant en comptabilit� Delta.", "INF-10171", true), " / ");
									oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("La comptabilisation de la rubrique fait appel � un compte li� au matricule", "INF-10169", true), oAgent.getNmat() + ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
									oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("=> V�rifier ce num�ro de compte dans la fiche salari� du matricule ou cr�er le compte en comptabilit� Delta.", "INF-10172", true), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
									oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult, true);
									return false;
								}
							}

							oNome = oInitialisationPrecentralisation.findFromNomenclature(oRubrique.getTypr(), oInitialisationPrecentralisation.getDonneesNomenclature(28));
							if (!ParameterUtil._isStringNull(oNome.getMontant()))
							{
								ind_typr = Integer.parseInt(oNome.getMontant());
								if (Integer.parseInt(oNome.getMontant()) == 1)
								{
									somme_debit = somme_debit.add(oCalcul.getMont());
									oResult = ClsTreater._getResultat("", "INF-10175", false);
									if (!oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult))
										return false;
								}
								if (Integer.parseInt(oNome.getMontant()) == 2)
								{
									somme_credit = somme_credit.add(oCalcul.getMont());
									oResult = ClsTreater._getResultat("", "INF-10176", false);
									if (!oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult))
										return false;
								}
							} else if (oRubrique.getCrub().equalsIgnoreCase(oCheckNomeNAP.getColumnvalue())){
								ind_typr = 2;
								somme_credit = somme_credit.add(oCalcul.getMont());
								oResult = ClsTreater._getResultat("", "INF-10176", false);
								if (!oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult))
									return false;
							}
						} //if("O".equalsIgnoreCase(oRubrique.getCper()))
						else
						{
							if (ParameterUtil._isStringNull(oRubrique.getTypr()))
							{
								if (!oRubrique.getCrub().equalsIgnoreCase(oCheckNomeNAP.getColumnvalue()))
								{
									oResult = ClsTreater._getResultat("Rubrique ", "INF-00082", true);
									oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oCalcul.getRubq(), null, true), " ");
									oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(" Type de la rubrique non-renseign�.", "INF-10177", true), " / ");
									oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("Une rubrique � comptabiliser doit n�cessairement avoir son type de rubrique renseign�.", "INF-10178", true), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
									oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("=> Renseigner le type de rubrique dans la fiche rubrique ou v�rifier si la rubrique est � comptabiliser.", "INF-10179", true), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
									oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult, true);
									return false;
								}
								else
								{
									//									 -- Cas du net a payer
									//						              -- Pas de type rubrique - Presentation bulletin := 99
									//						              -- A comptabiliser au credit
									oResult = ClsTreater._getResultat("", "INF-10180", false);
									if (!oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult))
										return false;
									ind_typr = 2;
								}
							}

							oResult = ClsTreater._getResultat("Type Rubrique", "INF-10181", false);
							oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oRubrique.getTypr(), null, false), " ");
							oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(String.valueOf(ind_typr), null, false), " <-> ");
							if (!oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult))
								return false;
							boolean booltyper = false;
							booltyper = (ind_typr == 1 || ind_typr == 3);
							if (StringUtils.equals(ClsEntreprise.SHELL_GABON, nomClient))
								booltyper = (ind_typr == 1 || ind_typr == 3 || ind_typr == 2);
							if (booltyper)
							{
								cpt_deb = oInitialisationPrecentralisation.rcpt(oAgent, oRubrique, "D", ind_deb);
								if (ParameterUtil._isStringNull(cpt_deb))
								{

									oResult = ClsTreater._getResultat("Rubrique ", "INF-00082", true);
									oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oCalcul.getRubq(), null, true), " ");
									oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("  Comptes � d�biter incorrects.", "INF-10182", true), " / ");
									oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("Les comptes � d�biter indiqu�s pour la rubrique ", "INF-10183", true), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
									oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oCalcul.getRubq(), null, true), " ");
									oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("  sont mal renseign�s.", "INF-10184", true), " ");
									oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("=> Renseigner correctement ces comptes dans la fiche rubrique.", "INF-10185", true), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
									oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult, true);
									return false;
								}

								if ("PERS".equalsIgnoreCase(cpt_deb))
								{
									if (ParameterUtil._isStringNull(oAgent.getCcpt()))
									{
										oResult = ClsTreater._getResultat("Rubrique ", "INF-00082", true);
										oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oCalcul.getRubq(), null, true), " ");
										oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("Compte personnel salari� incorrect.", "INF-10186", true), " / ");
										oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("La rubrique ", "INF-10187", true), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
										oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oCalcul.getRubq(), null, true), " ");
										oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(" est � comptabiliser sur compte personnel, et ce dernier est mal renseign�.", "INF-10188", true), " ");
										oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("=> Renseigner correctement le compte personnel dans la fiche salari� du matricule.", "INF-10189", true), oAgent.getNmat() + ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
										oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult, true);
										return false;
									}
									else cpt_deb = oAgent.getCcpt();
								}

								oResult = ClsTreater._getResultat("", "INF-10190", false);
								oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(cpt_deb, null, false), " ");
								if (!oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult))
									return false;

								if ("O".equals(oInitialisationPrecentralisation.getRub_a_comptabilise()))
								{
									oCpcpt = oInitialisationPrecentralisation.existCompteComptabilite(cpt_deb);
									if (oCpcpt == null)
									{
										ex_cpt = false;
										s_ana = false;
									}
									else
									{
										if (!"0".equals(oCpcpt.getCana()))
										{
											ex_cpt = true;
											s_ana = true;
										}
										else
										{
											ex_cpt = true;
											s_ana = false;
										}
									}
									if (!ex_cpt)
									{

										oResult = ClsTreater._getResultat("Rubrique ", "INF-00082", true);
										oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oCalcul.getRubq(), null, true), " ");
										oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("Comptes � d�biter inexistant en comptabilit� Delta.", "INF-10191", true), " / ");
										oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("Les comptes � d�biter indiqu�s pour la rubrique ", "INF-10183", true), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
										oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oCalcul.getRubq(), null, true), " ");
										oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(" n''existent pas dans la comptabilit� Delta.", "INF-10192", true), " ");
										oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("=> V�rifier les comptes au d�bit de la fiche rubrique ou renseigner correctement ces comptes dans la comptabilit� Delta.", "INF-10193", true), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
										oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult, true);
										return false;
									}
									oCpsec = oInitialisationPrecentralisation.existSectionAnalytique(oAgent.getNiv3());
									if (s_ana && (oCpsec == null))
									{
										oResult = ClsTreater._getResultat("Rubrique ", "INF-00082", true);
										oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oCalcul.getRubq(), null, true), " ");
										oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("Section inexistante en comptabilite Delta.", "INF-10173", true), " / ");
										oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("=> V�rifier la section dans la paie ou cr�er la section en comptabilit� Delta.", "INF-10174", true), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
										oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult, true);
										return false;
									}
								} //"O".equals(oInitialisationPrecentralisation.getRub_a_comptabilise()

								oInitialisationPrecentralisation.writeInTextLog("Debit = " + oCalcul.getMont());
								cpt_sens = "D";
								if (StringUtils.equals(ClsEntreprise.SHELL_GABON, nomClient))
								{
									if (ind_typr == 1 || ind_typr == 3)
										cpt_sens = "D";
									if (ind_typr == 2)
										cpt_sens = "C";
								}
								oNome = oInitialisationPrecentralisation.ch_rub(oRubrique, oCheckNomeRubBrut, oCalcul.getMont(), cpt_sens, somme_debit, somme_credit);
								somme_debit = new BigDecimal(oNome.getCode());
								somme_credit = new BigDecimal(oNome.getLibelle());
							} //if(ind_typr == 1 || ind_typr == 3)
							booltyper = (ind_typr == 2 || ind_typr == 3);
							if (StringUtils.equals(ClsEntreprise.SHELL_GABON, nomClient))
								booltyper = (ind_typr == 3);
							if (booltyper)
							{
								cpt_cre = oInitialisationPrecentralisation.rcpt(oAgent, oRubrique, "C", ind_cre);
								if (ParameterUtil._isStringNull(cpt_cre))
								{
									oResult = ClsTreater._getResultat("Rubrique ", "INF-00082", true);
									oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oCalcul.getRubq(), null, true), " ");
									oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("Comptes � cr�diter incorrects.", "INF-10194", true), " / ");
									oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("Les comptes � cr�diter indiqu�s pour la rubrique ", "INF-10195", true), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
									oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oCalcul.getRubq(), null, true), " ");
									oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(" sont mal renseign�s.", "INF-10184", true), " ");
									oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("=> Renseigner correctement ces comptes dans la fiche rubrique.", "INF-10185", true), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
									oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult, true);
									return false;
								}
								if ("PERS".equalsIgnoreCase(cpt_cre))
								{
									if (ParameterUtil._isStringNull(oAgent.getCcpt()))
									{
										oResult = ClsTreater._getResultat("Rubrique ", "INF-00082", true);
										oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oCalcul.getRubq(), null, true), " ");
										oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("Compte personnel salari� incorrect.", "INF-10186", true), " / ");
										oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("La rubrique ", "INF-10187", true), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
										oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oCalcul.getRubq(), null, true), " ");
										oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(" est � comptabiliser sur compte personnel, et ce dernier est mal renseign�.", "INF-10188", true), " ");
										oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("=> Renseigner correctement le compte personnel dans la fiche salari� du matricule.", "INF-10189", true), oAgent.getNmat() + ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
										oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult, true);
										return false;
									}
									else cpt_cre = oAgent.getCcpt();
								}

								oResult = ClsTreater._getResultat("", "INF-10196", false);
								oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(cpt_cre, null, false), " ");
								if (!oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult))
									return false;

								if ("O".equals(oInitialisationPrecentralisation.getRub_a_comptabilise()))
								{
									oCpcpt = oInitialisationPrecentralisation.existCompteComptabilite(cpt_cre);
									if (oCpcpt == null)
									{
										ex_cpt = false;
										s_ana = false;
									}
									else
									{
										if (!"0".equals(oCpcpt.getCana()))
										{
											ex_cpt = true;
											s_ana = true;
										}
										else
										{
											ex_cpt = true;
											s_ana = false;
										}
									}
									if (!ex_cpt)
									{
										oResult = ClsTreater._getResultat("Rubrique ", "INF-00082", true);
										oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oCalcul.getRubq(), null, true), " ");
										oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("Comptes � c�diter inexistant en comptabilit� Delta.", "INF-10197", true), " / ");
										oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("Les comptes � cr�diter indiqu�s pour la rubrique ", "INF-10195", true), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
										oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oCalcul.getRubq(), null, true), " ");
										oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(" n''existent pas dans la comptabilit� Delta.", "INF-10192", true), " ");
										oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("=> V�rifier les comptes au cr�dit de la fiche rubrique ou renseigner correctement ces comptes dans la comptabilit� Delta.", "INF-10198", true), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
										oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult, true);
										return false;
									}
									oCpsec = oInitialisationPrecentralisation.existSectionAnalytique(oAgent.getNiv3());
									if (s_ana && (oCpsec == null))
									{
										oResult = ClsTreater._getResultat("Rubrique ", "INF-00082", true);
										oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oCalcul.getRubq(), null, true), " ");
										oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("Section inexistante en comptabilite Delta.", "INF-10173", true), " / ");
										oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("=> V�rifier la section dans la paie ou cr�er la section en comptabilit� Delta.", "INF-10174", true), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
										oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult, true);
										return false;
									}
								} //"O".equals(oInitialisationPrecentralisation.getRub_a_comptabilise()
								oInitialisationPrecentralisation.writeInTextLog("Credit = " + oCalcul.getMont());
								cpt_sens = "C";
								oNome = oInitialisationPrecentralisation.ch_rub(oRubrique, oCheckNomeRubBrut, oCalcul.getMont(), cpt_sens, somme_debit, somme_credit);
								somme_debit = new BigDecimal(oNome.getCode());
								somme_credit = new BigDecimal(oNome.getLibelle());
							}
						} //else if("O".equalsIgnoreCase(oRubrique.getCper()))

						oResult = ClsTreater._getResultat("D= ", null, false);
						oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(somme_debit.toString(), null, false), " ");
						oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("C=", null, false), ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
						oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(somme_credit.toString(), null, false), " ");
						if (!oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult))
							return false;
					} //if("O".equalsIgnoreCase(oRubrique.getComp()))

					if (("1".equals(oCalcul.getTrtb())) && (oCalcul.getRubq().equals(oCheckNomeNAP.getColumnvalue())) && ("O".equalsIgnoreCase(oInitialisationPrecentralisation.getVerif_edit_bul())))
						w_edit = false;
					//			    oResult = ClsTreater._getResultat("Fermeture de PACALC", "INF-10199", false);
					//				if(! oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult)) return false;	

				} //if calcul = calcul agent

			} //for calcul
			//			 ---------------------------------------------
			//			    -- v�rification post-traitement des rubriques 
			//			    ---------------------------------------------
			if (ya_c_pacalc)
			{
				nb_sal += 1;
				ParameterUtil.println("Salari� " + oAgent.getNmat() + "D�bit =" + somme_debit + " et Cr�dit = " + somme_credit);
				if ((somme_debit.intValue() != 0) || (somme_credit.intValue() != 0))
				{
					if (somme_debit.compareTo(somme_credit) != 0)
					{
						String diff = "0.001";
						oNome = oInitialisationPrecentralisation.findFromNomenclature("ECART-CLOT", oInitialisationPrecentralisation.getDonneesNomenclature(99));
						if (StringUtils.isNotBlank(oNome.getTaux()))
							diff = oNome.getTaux();
						if ((somme_debit.subtract(somme_credit)).abs().compareTo(new BigDecimal(diff)) > 0)
						{
							oResult = ClsTreater._getResultat("", "INF-10200", true);
							oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oAgent.getNmat(), null, true), " ");
							oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("", "INF-10201", true), " ");
							oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(somme_debit.toString(), null, true), " ");
							oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("", "INF-10202", true), " ");
							oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(somme_credit.toString(), null, true), " ");
							if (!oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult))
								return false;
							nb_err = nb_err + 1;
							oInitialisationPrecentralisation.incrementerErreurs();
							if ("N".equalsIgnoreCase(oInitialisationPrecentralisation.getContinu_si_erreur()))
								return false;
						}
					}

					if ((!w_edit) && ("O".equalsIgnoreCase(oInitialisationPrecentralisation.getVerif_edit_bul())))
					{
						oResult = ClsTreater._getResultat("", "INF-10203", true);
						oResult = ClsTreater._concat(oResult, ClsTreater._getResultat(oAgent.getNmat(), null, true), " ");
						oResult = ClsTreater._concat(oResult, ClsTreater._getResultat("", "INF-10204", true), " ");
						if (!oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult))
							return false;
						nb_err = nb_err + 1;
						oInitialisationPrecentralisation.incrementerErreurs();
						w_noed = true;
						if ("N".equalsIgnoreCase(oInitialisationPrecentralisation.getContinu_si_erreur()))
							return false;
					}
				}
			}

			oResult = ClsTreater._getResultat("", "INF-10205", false);
			if (!oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult, true))
				return false;

		} //for agent ...
		Date fin  = new Date();
		String strfin = ClsTreater._getResultat("Traitement termin�", "INF-00440", true).getLibelle()+" "+new ClsDate(fin).getDateS("dd/MM/yyyy HH:mm:ss");
		oResult = ClsTreater._getResultat(strfin, null, true);
		oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult);

		String dureetraitement;
		try
		{
			Date duree = ClsDate.substractDate(fin, deb);
			dureetraitement = new ClsDate(new ClsDate(duree).addHour(-1)).getDateS("HH:mm:ss");
			
			String dur = ClsTreater._getResultat("Dur�e du traitement :", "INF-10223", true).getLibelle()+" "+dureetraitement;
			oResult = ClsTreater._getResultat(dur, null, true);
			oInitialisationPrecentralisation._memorySaveListeOfLogs(listeLogEtendu, oResult);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

		
		//Permettre de t�l�charger le fichier des messages de pr�centralisation
		//FPAPRECI_aaaammdd_hhmiss.TXT
		String texte = "";
		List<ClsRhtLogEtendu> liste = (List<ClsRhtLogEtendu>) request.getSession().getAttribute(ClsSessionObjectName.SESSION_O_PRECENTRALISATION_LISTE);
		if (liste != null)
		{
			if (liste.size() > 0)
			{
				LogMessage log;
				for (int i = 0; i < liste.size(); i++)
				{
					log = liste.get(i).getRhtlog();

					if (StringUtil.isBlank(texte))
						texte = log.getLigne();
					else texte += "\n" + log.getLigne();
				}

			}
		}
		if (StringUtil.isNotBlank(texte))
		{
			//String cheminLog = 
			String file = "C:\\FPAPRECI_" + new ClsDate(new Date()).getDateS("yyyyMMddHHmmss")+".txt";
			//StringUtils.printOutObject(texte, file, true);
			//ui.downloadFile(file);
//			String fileToDownload = IntegrationAutomatique.genererFichierLog(request, service, cdos, clang, texte);
//			ui.setFileToDownload(fileToDownload);
//			ui.refreshInterface();
			
		}

		return true;
	}

	public void setAffichageProgression(int nbTraite, int nbtotal, HttpServletRequest request)
	{
		if (ui != null)
			ui.mettreAJourProgressBar(nbTraite);

		ClsTemplate oTemplate = new ClsTemplate(String.valueOf(nbtotal), String.valueOf(nbTraite));

		request.getSession().setAttribute(ClsSessionObjectName.SESSION_O_PRECENTRALISATION_TRAITEMENT_SALARIE, oTemplate);

		//if(nbTraite == nbtotal) request.getSession().removeAttribute(ClsSessionObjectName.SESSION_O_PRECENTRALISATION_LISTE);

		//		ParameterUtil.println("---------------En cours de traitement : "+nbTraite+" sur "+nbtotal);
		//		if(nbTraite <= nbtotal) 
		//			request.getSession().setAttribute(ClsSessionObjectName.SESSION_O_PRECENTRALISATION_TRAITEMENT_SALARIE, nbTraite+" Sur "+nbtotal);
		//		else
		//		{
		//			request.getSession().setAttribute(ClsSessionObjectName.SESSION_O_PRECENTRALISATION_TRAITEMENT_SALARIE, "TRAITEMENT TERMINEE");
		//			request.getSession().removeAttribute(ClsSessionObjectName.SESSION_O_PRECENTRALISATION_LISTE);
		//		}
	}

	public String getAffichageProgression(HttpServletRequest request)
	{
		if (request.getSession().getAttribute(ClsSessionObjectName.SESSION_O_PRECENTRALISATION_TRAITEMENT_SALARIE) != null)
			return request.getSession().getAttribute(ClsSessionObjectName.SESSION_O_PRECENTRALISATION_TRAITEMENT_SALARIE).toString();
		else return "";
	}

	public int getNbreOfAgentProcessed(HttpServletRequest request)
	{
		Object obj = request.getSession().getAttribute(ClsSessionObjectName.SESSION_O_PRECENTRALISATION_TRAITEMENT_SALARIE);
		if (obj == null)
			return 0;
		ClsTemplate oTemplate = (ClsTemplate) obj;
		return Integer.parseInt(oTemplate.getLibelle());
	}

	public int getSizeOfAgent(HttpServletRequest request)
	{
		Object obj = request.getSession().getAttribute(ClsSessionObjectName.SESSION_O_PRECENTRALISATION_TRAITEMENT_SALARIE);
		if (obj == null)
			return DEFAULT_NUMBER_OF_SALARY_TO_PROCEED;
		ClsTemplate oTemplate = (ClsTemplate) obj;
		return Integer.parseInt(oTemplate.getCode());
	}

	public String getBlockingMessage(HttpServletRequest request)
	{
		Object obj = request.getSession().getAttribute(ClsSessionObjectName.SESSION_O_PRECENTRALISATION_BLOCKING_MESSAGE);
		if (obj == null)
			return "";
		ClsResultat oResult = (ClsResultat) obj;
		return oResult.getLibelle();
	}

	public String stopAllThread(HttpServletRequest request)
	{
		Object obj = request.getSession().getAttribute(ClsSessionObjectName.SESSION_O_PRECENTRALISATION_TRAITEMENT_SALARIE);

		request.getSession().removeAttribute(ClsSessionObjectName.SESSION_O_PRECENTRALISATION_LISTE);
		request.getSession().removeAttribute(ClsSessionObjectName.SESSION_O_PRECENTRALISATION_TRAITEMENT_SALARIE);
		request.getSession().removeAttribute(ClsSessionObjectName.SESSION_O_PRECENTRALISATION_BLOCKING_MESSAGE);

		if (obj == null)
			return "0";
		ClsTemplate oTemplate = (ClsTemplate) obj;
		int nbTotal = Integer.parseInt(oTemplate.getCode());
		int nbTraite = Integer.parseInt(oTemplate.getLibelle());
		return String.valueOf(nbTotal - nbTraite);
	}

	public GeneriqueConnexionService getService()
	{
		return service;
	}

	public void setService(GeneriqueConnexionService service)
	{
		this.service = service;
	}

	public ClsInitPrecentralisationPaie getOInitialisationPrecentralisation()
	{
		return oInitialisationPrecentralisation;
	}

	public void setOInitialisationPrecentralisation(ClsInitPrecentralisationPaie initialisationPrecentralisation)
	{
		oInitialisationPrecentralisation = initialisationPrecentralisation;
	}

}
