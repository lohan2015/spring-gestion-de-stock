package com.kinart.paie.business.services.cloture;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.kinart.paie.business.model.InterfComptable;
import com.kinart.paie.business.model.ParamData;
import com.kinart.paie.business.model.Produit;
import com.kinart.paie.business.services.utils.*;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

@SuppressWarnings("unused")
public class ClsCentralisationTreater
{

	private ClsOutPrecentralisation centra = new ClsOutPrecentralisation();

	private ClsCentralisationService service;

	private Session session;

	private HttpServletRequest _request;

	private String p_cdos;

	private String p_aamm;

	private String p_nbul;

	private String p_cuti;

	private String p_nddd;

	private String p_datc;

	private ParamData w_nome = null;
	
	private static Integer compteur = 0;
	
	private String generationFichiers = null;
	
	private String dossierGenerationFichiers = null;

	public ClsCentralisationTreater(GeneriqueConnexionService tmpService, ClsCentralisationService service, HttpServletRequest _request, String p_cdos, String p_aamm, String p_nbul, String p_cuti, String p_nddd, String p_datc)
	{
		this.service = service;
		this._request = _request;
		this.p_cdos = p_cdos;
		this.p_aamm = p_aamm;
		this.p_nbul = p_nbul;
		this.p_cuti = p_cuti;
		this.p_nddd = p_nddd;
		this.p_datc = p_datc;
		String strCLang = ParameterUtil.getSessionObject(_request, ParameterUtil.SESSION_LANGUE);
		generationFichiers = "O";//ClsConfigurationParameters.getConfigParameterValue(tmpService, p_cdos, strCLang, ClsConfigurationParameters.GENERATION_FICHIERS_CONTROLE_LORS_CALCUL_PAIE);
		ParamData path = tmpService.findAnyColumnFromNomenclature(p_cdos, strCLang, "266", "GENCALCFLD", "2");
		if(path!=null)
		 dossierGenerationFichiers = path.getVall();//ClsConfigurationParameters.getConfigParameterValue(tmpService, p_cdos, strCLang, ClsConfigurationParameters.DOSSIER_GENERATION_FICHIERS_CONTROLE_LORS_CALCUL_PAIE);
	}

	public boolean lcentra()
	{

		try
		{
			session = service.getSession();
			Transaction tx = session.beginTransaction();

			service._setTitreEvolutionTraitement(_request, ClsTreater._getResultat("CENTRALISATION EN COURS...", "INF-01115", false).getLibelle());

			if (!initialisationVariable())
			{
				centra.printOutPrecentralisation();
				centra.message = ClsTreater._getResultat("Impossible de continuer le traitement. L'initialisation s'est mal passé"
						, "INF-80-RH-188", true);
				service.insertIntoMemoryLog(centra, true);
				return false;
			}
			ParameterUtil.println("--------------After initialisaze");
			if (!verificationParametrage())
			{
				centra.printOutPrecentralisation();
				centra.message = ClsTreater._getResultat("impossible de continuer le traitement. La vérification s'est mal passé"
						, "INF-80-RH-189", true);
				service.insertIntoMemoryLog(centra, true);
				return false;
			}
			ParameterUtil.println("--------------After verification");
			if (!centralisationTraitement())
			{
				centra.printOutPrecentralisation();
				if (tx != null)
					tx.rollback();
			}
			else
			{
				ParameterUtil.println("--------------After centralisation");
				centra.printOutPrecentralisation();
				
				try
				{
					service.updateLibErrTableCPINT(session, centra.v_dos);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				tx.commit();
				centra.message = ClsTreater._getResultat("Centralisation effectuée avec succés"
						, "INF-80-RH-190", false);
				service._setTitreEvolutionTraitement(_request,"");
				service.insertIntoMemoryLog(centra, false);
			}
			ParameterUtil.println("--------------After all functions");
			return true;
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
			return false;
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
			return false;
		}
		finally
		{
			
			if(session !=null)
				service.closeConnexion(session);
		}
		

	}

	private boolean initialisationVariable()

	{
		centra = new ClsOutPrecentralisation();
		centra.request = _request;

		centra.request.getSession().setAttribute(ClsSessionObjectName.SESSION_O_PRECENTRALISATION_LISTE, new ArrayList<ClsLog>());

		centra.dateFormat = new SimpleDateFormat(ParameterUtil.getSessionObject(centra.request, ParameterUtil.SESSION_FORMAT_DATE));
		centra.cdos = p_cdos;
		centra.aamm = p_aamm;
		centra.nbul = p_nbul;
		centra.cuti = p_cuti;
		centra.generationFichiers = generationFichiers.charAt(0);
		centra.dossierGenerationFichiers = dossierGenerationFichiers;
		centra.nddd = StringUtils.isBlank(p_nddd) ? 0 : Integer.valueOf(p_nddd);
		try
		{
			centra.date_comptable = centra.dateFormat.parse(p_datc);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		centra.clang = this.service.getLangue(centra);
		centra.comp = service.getCompFromRhpdos(centra.cdos);
		centra.deltacom = "O".equalsIgnoreCase(centra.comp);
		String[] tab_centra_dossier = service.centra_dossier(centra.cdos);
		centra.centra_dos = "O".equalsIgnoreCase(tab_centra_dossier[0]);
		centra.numdos_centra = tab_centra_dossier[1];
		centra.Num_Piece = service.genereNumpieceSimpe(centra.aamm, centra.nbul);
		centra.Libelle_Ecriture = service.genereLibelleEcriture(centra.aamm, centra.nbul);
		centra.Rbq_NAP = service.chercherRubriqueNAP(centra.cdos);
		centra.Rbq_BRUT = service.chercherRubriqueBrut(centra.cdos);
		centra.Code_Journal = service.chercherCodeJournal(centra.cdos);
		centra.Code_Etb_Commun = service.chercherCodeEtablissementCommun(centra.cdos);
		centra.Dest2_Commune = service.chercherDest2Commune(centra.cdos);
		centra.Libelle_Abrege = service.chercherLibelleAbrege(centra.cdos);
		w_nome = service.chercherInterfaceGL(centra.cdos);
		centra.interface_gl = !StringUtils.isBlank(w_nome.getVall());
		centra.num_tab_gl = w_nome.getValm() != null ? w_nome.getValm().intValue() : null;
		centra.cle_tab_gl = StringUtils.substring(w_nome.getVall(), 0, 10);

		if (centra.interface_gl)
		{
			for (int i = 0; i < 9; i++)
				centra.destination_gl[i] = StringUtils.trim(service.chercherEnNomenclature(centra.cdos, centra.num_tab_gl, centra.cle_tab_gl, i + 1).getVall());
			if (ArrayUtils.isEmpty(centra.destination_gl))
			{
				centra.message = ClsTreater._getResultat("Table de paramétrage GL %1 est vide", "INF-80-RH-191", true," "+centra.num_tab_gl );
				service.insertIntoMemoryLog(centra, true);
				//return false;
			}
		}

		centra.Devise_Dossier = service.getDeviseDossierFromCpdos(centra.cdos);

		centra.NbreBul = service.getNbreBulFromRhtcalcul(centra.cdos, centra.aamm, centra.nbul, centra.Rbq_NAP);

		centra.NbreBulNonEdite = service.getNbreBulNonEditeFromRhtcalcul(centra.cdos, centra.aamm, centra.nbul, centra.Rbq_NAP);

		centra.NbreRubCompta = service.getNbreRubComptaFromRhprubrique(centra.cdos);

		centra.ListeSalarieNationaliteInexistante = service.getListeSalarieNationaliteInexistante(centra.cdos, centra.aamm, centra.nbul, centra.Rbq_NAP);
		centra.ListeSalarieGradeInexistant = service.getListeSalarieGradeInexistant(centra.cdos, centra.aamm, centra.nbul, centra.Rbq_NAP);
		
		
		if (centra.deltacom && centra.centra_dos)
			centra.v_dos = centra.numdos_centra;
		else
			centra.v_dos = centra.cdos;

		return true;
	}

	private boolean verificationParametrage()
	{
		boolean result = true;

		if (StringUtils.isBlank(centra.Rbq_BRUT))
		{
			centra.message = ClsTreater._getResultat("ERREUR : Créer le paramétre RUBBRUT en table 99", "INF-10113", true);
			service.insertIntoMemoryLog(centra, true);

			centra.message = ClsTreater._getResultat("ERREUR :  Montant 1 clé RUBBRUT en table 99 doit contenir le No de la rubrique Salaire Brut", "INF-10114", true);
			service.insertIntoMemoryLog(centra, true);
			result = false;
		}

		if (StringUtils.isBlank(centra.Rbq_NAP))
		{
			centra.message = ClsTreater._getResultat("ERREUR : Créer le paramétre RUBNAP en table 99", "INF-10116", true);
			service.insertIntoMemoryLog(centra, true);

			centra.message = ClsTreater._getResultat("ERREUR : Montant 1 clé RUBNAP en table 99 doit contenir le No de la rubrique du Net é Payer", "INF-10117", true);
			service.insertIntoMemoryLog(centra, true);
			result = false;
		}

		if (centra.interface_gl)
		{
			if (ArrayUtils.isEmpty(centra.destination_gl))
			{
				centra.message = ClsTreater._getResultat("Table de paramétrage GL %1 est vide"
						, "INF-80-RH-191", true,""+centra.num_tab_gl);
				service.insertIntoMemoryLog(centra, true);
				result = false;
			}
		}

		if (centra.NbreBul == 0)
		{
			centra.message = ClsTreater._getResultat("Aucun bulletin a mettre a jour.", "ERR-90101", true);
			service.insertIntoMemoryLog(centra, true);
			result = false;
		}

		if (centra.NbreBulNonEdite > 0)
		{
			centra.message = ClsTreater._getResultat("Aucun bulletin a mettre a jour.", "ERR-90101", true);
			service.insertIntoMemoryLog(centra, true);
			result = false;
		}

		if (centra.NbreRubCompta == 0)
		{
			centra.message = ClsTreater._getResultat("REMARQUE : Aucune rubrique détectée pour la comptabilisation.", "INF-10125", true);
			service.insertIntoMemoryLog(centra, true);

			centra.message = ClsTreater._getResultat("Il n'y aura pas de vérification de l'équilibre comptable des bulletins.", "INF-10126", true);
			service.insertIntoMemoryLog(centra, true);
			result = false;
		}

		if (centra.ListeSalarieNationaliteInexistante.size() != 0)
		{
			centra.message = ClsTreater._getResultat("Une nationalité est affectée au matricule, alors que cette nationalité n'existe plus en table 04", "INF-10132", true);
			service.insertIntoMemoryLog(centra, true);

			String chaineNmat = "";
			for (int i = 0; i < centra.ListeSalarieNationaliteInexistante.size(); i++)
			{
				chaineNmat += centra.ListeSalarieNationaliteInexistante.get(i).getNmat();
			}
			centra.message = ClsTreater._getResultat(chaineNmat, null, true);
			service.insertIntoMemoryLog(centra, true);
			result = false;
		}

		if (centra.ListeSalarieGradeInexistant.size() != 0)
		{
			centra.message = ClsTreater._getResultat("Un titre est affecté au matricule, alors que ce titre n'existe plus en table 06", "INF-10129", true);
			service.insertIntoMemoryLog(centra, true);

			String chaineNmat = "";
			for (int i = 0; i < centra.ListeSalarieGradeInexistant.size(); i++)
			{
				chaineNmat += centra.ListeSalarieGradeInexistant.get(i).getNmat();
			}
			centra.message = ClsTreater._getResultat(chaineNmat, null, true);
			service.insertIntoMemoryLog(centra, true);
			result = false;
		}

		if (centra.deltacom)
		{

			if (StringUtils.isBlank(service.chercherLibelleJournalFromCp_Jou(centra.cdos, centra.Code_Journal)))
			{
				centra.message = ClsTreater._getResultat("Journal %1 inexistant en comptabilite.", "ERR-90091", true, centra.Code_Journal);
				service.insertIntoMemoryLog(centra, true);
				return false;
			}
			if (!StringUtils.isBlank(centra.Code_Etb_Commun) && StringUtils.isBlank(service.chercherLibelleEtablissementFromCpEt(centra.cdos, centra.Code_Etb_Commun)))
			{
				centra.message = ClsTreater._getResultat("Etablissement standard %1 inexistant en comptabilite.", "ERR-90092", true, centra.Code_Etb_Commun);
				service.insertIntoMemoryLog(centra, true);
				result = false;
			}

			centra = service.verifieValiditeSection(centra, centra.cdos, centra.Dest2_Commune, 2, centra.date_comptable);
			if (!centra.verifok)
				result = false;

			if (StringUtils.isBlank(service.chercherLibelleAbbregeFromCpAbr(centra.cdos, centra.Libelle_Abrege)))
			{
				centra.message = ClsTreater._getResultat("Code libelle abrege %1 inexistant en comptabilité.", "ERR-90098", true, centra.Libelle_Abrege);
				service.insertIntoMemoryLog(centra, true);
				result = false;
			}

			if (StringUtils.isBlank(centra.Devise_Dossier))
			{
				centra.message = ClsTreater._getResultat("Dossier %1, devise é renseigner dans le dossier comptable..", "ERR-90100", true, centra.cdos);
				service.insertIntoMemoryLog(centra, true);
				result = false;
			}

		}


		service.viderTableCPINT(centra.v_dos);

		return result;
	}

	private boolean centralisationTraitement()
	{
		centra.listeSalaries = service.getListeSalarieFromRhpagent(centra.cdos, centra.aamm, Integer.valueOf(centra.nbul));

		boolean result = true;

		String message = null;

		Integer size = centra.listeSalaries.size();

		for (int i = 0; i < size; i++)
		{
			centra.salarie = new ClsSalarieCentralisation(centra.listeSalaries.get(i));

			message = ClsTreater._getResultat("Salarié courant ", "INF-80-RH-182", false).getLibelle() 
				+ centra.salarie.getNmat() + " : " + centra.salarie.getNom() + " " + centra.salarie.getPren();
			service._setEvolutionTraitement(centra.request, message, false, i + 1, size);

			if (!lect_rub_cal(centra.cdos))
			{
				service._setEvolutionTraitement(centra.request, centra.message.getLibelle(), true, i + 1, size);
				centra.message = ClsTreater._getResultat("ERREUR PENDANT LA CENTRALISATION...", "INF-01116", true);
				service._setTitreEvolutionTraitement(centra.request, centra.message.getLibelle());
				//afficher uniquement le salarie en probléme
				centra.listeSalariesCentralisation.add(centra.salarie);
				result = false;
				break;
			}
			
			//centra.listeSalariesCentralisation.add(centra.salarie);
		}
		return result;
	}

	private boolean lect_rub_cal(String cdos)
	{
		centra.salarie.listeCalculs = service.getListeBulletinCalculFromRhtcalcul(centra.cdos, centra.aamm, centra.nbul, centra.salarie.getNmat());

		centra.salarie.Total_Debit = new BigDecimal(0);
		centra.salarie.Total_Credit = new BigDecimal(0);

		for (int i = 0; i < centra.salarie.listeCalculs.size(); i++)
		{
			centra.salarie.calcul = new ClsCalculCentralisation(centra.salarie.listeCalculs.get(i));

			if (centra.salarie.calcul.getMont().compareTo(new BigDecimal(0)) != 0)
			{
				if (!ven_ecr())
				{
					centra.salarie.listeCalculsCentralisation.add(centra.salarie.calcul);
					
					return false;
				}
				
				centra.salarie.listeCalculsCentralisation.add(centra.salarie.calcul);
			}
		}

		if (centra.salarie.Total_Debit.compareTo(centra.salarie.Total_Credit) != 0)
		{
			centra.message = ClsTreater._getResultat(
					ClsTreater._getResultat("Salarié ", "INF-01440", false).getLibelle()
					+centra.salarie.getNmat()
					+" , "+ClsTreater._getResultat(" Total Débit = ", "INF-80-RH-186", false).getLibelle()
					+centra.salarie.Total_Debit
					+" Et "+ClsTreater._getResultat(" Total crédit = ", "INF-80-RH-187", false).getLibelle()
					+centra.salarie.Total_Credit, "", true, centra.cdos);
			centra.salarie.Ecart = centra.salarie.Total_Debit.subtract(centra.salarie.Total_Credit);
			ParameterUtil.println(centra.salarie.getNmat()+ " , centra.salarie.Ecart = "+centra.salarie.Ecart);
			return false;
		}

		return true;
	}

	private boolean ven_ecr()
	{
		try
		{
			String crub = StringUtils.isBlank(centra.salarie.calcul.getRuba()) == true ? centra.salarie.calcul.getRubq() : centra.salarie.calcul.getRuba();
			centra.salarie.calcul.rubrique = service.getInfoRubriqueFromRhprubrique(centra.cdos, crub);
			if (!StringUtils.equals("O", centra.salarie.calcul.rubrique.getComp()))
				return true;
			Integer MonTypeRub = null;
			ParamData nome = null;
			ParameterUtil.println("--------->rubrique courante = "+centra.salarie.calcul.rubrique+" de type "+centra.salarie.calcul.rubrique.getTypr());
			if (!StringUtils.equals(centra.Rbq_NAP, centra.salarie.calcul.rubrique.getCrub()))
			{
				nome = service.chercherEnNomenclature(centra.Code_Etb_Commun, 28, centra.salarie.calcul.rubrique.getTypr(), 1);
				if(nome != null && nome.getVall() != null)
					MonTypeRub = nome.getValm().intValue();
				else
					ParameterUtil.println("--------->type rubrique nulle, code etablissement commun = "+centra.Code_Etb_Commun);
					
			}
			else
				MonTypeRub = 2;

			centra.salarie.calcul.Type_Rub = MonTypeRub;
			if (StringUtils.equals("O", centra.salarie.calcul.rubrique.getCper()))
			{
				if (!rech_cpt_per())
					return false;
			}
			else
			{
				if (!rech_cpt())
					return false;
			}

			if (!StringUtils.equalsIgnoreCase("O", centra.salarie.calcul.rubrique.getCper()))
				if (!cpt_cherche_lettre())
					return false;

			if (!charg_etssec())
				return false;

			for (int i = 0; i < 2; i++)
			{
				if (StringUtils.equalsIgnoreCase("D", centra.salarie.calcul.sens[i].sens))
					ch_deb();

				if (StringUtils.equalsIgnoreCase("C", centra.salarie.calcul.sens[i].sens))
					ch_cre();
			}
			if (centra.deltacom)
			{
				if (!verifieExistenceCompte())
					return false;

				if (!verifieExistenceSection())
					return false;
			}
			

			if (!ch_rub())
				return false;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			centra.message = ClsTreater._getResultat(ClsTreater._getStackTrace(e), null, true);
			service.insertIntoMemoryLog(centra, true);
			return false;
		}
		
		return true;
	}

	private boolean verifieExistenceCompte()
	{
		try
		{
			ClsCalculCentralisation.Sens[] LeCompte = centra.salarie.calcul.sens;
			String MonCompte = null;
			String MonTiers = null;
			String MonCpt_Type = null;
			Produit cppro = null;
			for (int i = 0; i < LeCompte.length; i++)
			{
				String MonProfil = service.getCodproFromCp_Cpt(centra.cdos, LeCompte[i].Num_Compte);
				if (!StringUtils.isBlank(MonProfil))
				{
					String MonNumcpt = service.getNumcptFromCpAux(centra.cdos, LeCompte[i].Num_Compte);
					if (StringUtils.isBlank(MonNumcpt))
					{
						centra.message = ClsTreater._getResultat("Compte personnel inexistant en compta; salarie :", "ERR-90107", true);
						service.insertIntoMemoryLog(centra, true);
						return false;
					}
					else
					{
						MonCompte = MonNumcpt;
						MonTiers = LeCompte[i].Num_Compte;
						centra.salarie.calcul.sens[i].Cpt_Type = "D";
					}
				}
				else
				{
					MonCompte = LeCompte[i].Num_Compte;
					MonTiers = null;
					cppro = service.getCpProFromCpPro(centra.cdos, MonProfil);
					MonCpt_Type = service.getTypcFromCp_Cpt(centra.cdos, MonCompte);
				}

				String MonGesana = cppro.getGesana();
				String MonGeslet = cppro.getGeslet();
				centra.salarie.calcul.sens[i].suivi_anal = (StringUtils.equalsIgnoreCase("O", MonGesana));
				centra.salarie.calcul.sens[i].suivi_let = (StringUtils.equalsIgnoreCase("O", MonGeslet));
				centra.salarie.calcul.sens[i].Num_Compte = MonCompte;
				centra.salarie.calcul.sens[i].Num_Tiers = MonTiers;
				centra.salarie.calcul.sens[i].Cpt_Type = MonCpt_Type;
				if (StringUtils.equalsIgnoreCase("C", MonCpt_Type) && StringUtils.isBlank(MonTiers))
				{
					centra.message = ClsTreater._getResultat("Sal. %1  Rub. %2  Compte %3  inexistant en comptabilite.", "ERR-90121", true, centra.salarie.getNmat(), centra.salarie.calcul.getRubq(), MonCompte);
					service.insertIntoMemoryLog(centra, true);
					return false;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			centra.message = ClsTreater._getResultat(ClsTreater._getStackTrace(e), null, true);
			service.insertIntoMemoryLog(centra, true);
			return false;
		}

		return true;
	}

	private boolean ch_rub()
	{
		try
		{
			for (int i = 0; i < 2; i++)
			{
				if(StringUtils.isNotBlank(centra.salarie.calcul.sens[i].sens))
				{
					compteur ++;
					centra.Libelle_Ecriture = StringUtils.substring(centra.salarie.calcul.rubrique.getLrub(),0,22)+" "+ StringUtils.substring(centra.aamm,4,6)+"/"+StringUtils.substring(centra.aamm,0,4);
					centra.salarie.calcul.wint = new InterfComptable();
					centra.salarie.calcul.wint.setIdEntreprise(new Integer(centra.v_dos));
					centra.salarie.calcul.wint.setCodets(centra.Code_Etablissement);
					centra.salarie.calcul.wint.setCodjou(centra.Code_Journal);
					centra.salarie.calcul.wint.setNumcpt(centra.salarie.calcul.sens[i].Num_Compte);
					centra.salarie.calcul.wint.setNumtie(centra.salarie.calcul.sens[i].Num_Tiers);
					centra.salarie.calcul.wint.setNumpce(centra.Num_Piece);
					centra.salarie.calcul.wint.setDatcpt(centra.date_comptable);
					centra.salarie.calcul.wint.setDatpce(centra.date_comptable);
					centra.salarie.calcul.wint.setDatech(centra.date_comptable);
					centra.salarie.calcul.wint.setDevpce(centra.Devise_Dossier);
					centra.salarie.calcul.wint.setQuantite(null);
					centra.salarie.calcul.wint.setSens(centra.salarie.calcul.sens[i].sens);
					centra.salarie.calcul.wint.setPceMt(centra.salarie.calcul.getMont());
					centra.salarie.calcul.wint.setCoddes1(centra.salarie.calcul.sens[i].Destination[0]);
					centra.salarie.calcul.wint.setCoddes2(centra.salarie.calcul.sens[i].Destination[1]);
					centra.salarie.calcul.wint.setCoddes3(centra.salarie.calcul.sens[i].Destination[2]);
					centra.salarie.calcul.wint.setCoddes4(centra.salarie.calcul.sens[i].Destination[3]);
					centra.salarie.calcul.wint.setCoddes5(centra.salarie.calcul.sens[i].Destination[4]);
					centra.salarie.calcul.wint.setCoddes6(centra.salarie.calcul.sens[i].Destination[5]);
					centra.salarie.calcul.wint.setCoddes7(centra.salarie.calcul.sens[i].Destination[6]);
					centra.salarie.calcul.wint.setCoddes8(centra.salarie.calcul.sens[i].Destination[7]);
					centra.salarie.calcul.wint.setCoddes9(centra.salarie.calcul.sens[i].Destination[8]);
					centra.salarie.calcul.wint.setLibecr(centra.Libelle_Ecriture);
					centra.salarie.calcul.wint.setCodtre(null);
					centra.salarie.calcul.wint.setCodabr(centra.Libelle_Abrege);
					centra.salarie.calcul.wint.setCoderr(null);
					//centra.salarie.calcul.wint.setLiberr(null);
					centra.salarie.calcul.wint.setLiberr(String.valueOf(compteur));
					centra.salarie.calcul.wint.setCreationDate(Instant.now());
					centra.salarie.calcul.wint.setLastModifiedDate(Instant.now());
					centra.salarie.calcul.wint.setCoduti(centra.cuti);
					
					service.insereDansCPINT(session, centra.salarie.calcul.wint);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			centra.message = ClsTreater._getResultat(ClsTreater._getStackTrace(e), null, true);
			service.insertIntoMemoryLog(centra, true);
			return false;
		}
		return true;
	}

	private boolean ch_cre()
	{
		String signe="";
		if (centra.salarie.calcul.rubrique.getPrbul() == 1 || centra.salarie.calcul.rubrique.getPrbul() == 2)
		{
			centra.salarie.Total_Credit = centra.salarie.Total_Credit.subtract(centra.salarie.calcul.getMont());
			signe="-";
		}
		else
		{
			centra.salarie.Total_Credit = centra.salarie.Total_Credit.add(centra.salarie.calcul.getMont());
			signe="+";
		}
		
		

		ClsRubriqueAComptabiliser rubriq = new ClsRubriqueAComptabiliser();
		rubriq.setMontant(centra.salarie.calcul.getMont());
		rubriq.setPrbul(centra.salarie.calcul.rubrique.getPrbul());
		rubriq.setRubrique(centra.salarie.calcul.getRubq());
		rubriq.setSens("C");
		rubriq.setSigne(signe);
		
		centra.salarie.rubriqueacomptabiliser.add(rubriq);

		return true;
	}

	private boolean ch_deb()
	{
		String signe="";
		if (StringUtils.equalsIgnoreCase(centra.salarie.calcul.rubrique.getCrub(), centra.Rbq_BRUT))
		{
			centra.salarie.Total_Debit = centra.salarie.Total_Debit.add(centra.salarie.calcul.getMont());
			signe ="+";
		}
		else
		{
			if (centra.salarie.calcul.rubrique.getPrbul() == 3 || centra.salarie.calcul.rubrique.getPrbul() == 4)
			{
				centra.salarie.Total_Debit = centra.salarie.Total_Debit.subtract(centra.salarie.calcul.getMont());
				signe = "-";
			}
			else
			{
				centra.salarie.Total_Debit = centra.salarie.Total_Debit.add(centra.salarie.calcul.getMont());
				signe = "+";
			}
		}
		
		
		ClsRubriqueAComptabiliser rubriq = new ClsRubriqueAComptabiliser();
		rubriq.setMontant(centra.salarie.calcul.getMont());
		rubriq.setPrbul(centra.salarie.calcul.rubrique.getPrbul());
		rubriq.setRubrique(centra.salarie.calcul.getRubq());
		rubriq.setSens("D");
		rubriq.setSigne(signe);
		
		centra.salarie.rubriqueacomptabiliser.add(rubriq);
		return true;

	}
	private boolean cpt_cherche_lettre()
	{
		try
		{
			ClsCalculCentralisation.Sens[] LeCompte = centra.salarie.calcul.sens;
			int MonNbRec;
			String MonCpt_Type = null;
			String MaDestination = null;
			for (int i = 0; i < LeCompte.length; i++)
			{
				String MonNumCompte = LeCompte[i].Num_Compte;
				int position = StringUtils.indexOf(MonNumCompte, 'M');
				if (position != -1)
				{
					MonNbRec = NumberUtils.toInt(StringUtils.substring(StringUtils.substringAfter(MonNumCompte, "M"), 0, 1));
					centra.salarie.calcul.sens[i].Num_Compte = StringUtils.substringBefore(MonNumCompte, "M");
					centra.salarie.calcul.sens[i].Num_Tiers = StringUtils.right(MonNumCompte, MonNbRec);
					MonCpt_Type = "C";
					return true;
				}

				position = StringUtils.indexOf(MonNumCompte, "N1");
				if (position != -1)
				{
					centra.salarie.calcul.sens[i].Num_Compte = StringUtils.substring(StringUtils.substringBefore(MonNumCompte, "N1") + centra.salarie.getNiv1(),0,8);
					return true;
				}

				position = StringUtils.indexOf(MonNumCompte, "N2");
				if (position != -1)
				{
					centra.salarie.calcul.sens[i].Num_Compte = StringUtils.substring(StringUtils.substringBefore(MonNumCompte, "N2") + centra.salarie.getNiv2(),0,8);
					return true;
				}

				position = StringUtils.indexOf(MonNumCompte, "N3");
				if (position != -1)
				{
					centra.salarie.calcul.sens[i].Num_Compte = StringUtils.substring(StringUtils.substringBefore(MonNumCompte, "N3") + centra.salarie.getNiv3(),0,8);
					return true;
				}

				position = StringUtils.indexOf(MonNumCompte, "V");
				if (position != -1)
				{
					MonNbRec = NumberUtils.toInt(StringUtils.substring(MonNumCompte, position, position + 1));
					centra.salarie.calcul.sens[i].Num_Compte = StringUtils.substring(StringUtils.substringBefore(MonNumCompte, "V") + service.chercherEnNomenclature(centra.cdos, 5, centra.salarie.getVild(), 3).getVall(),0,8);
					return true;
				}

				position = StringUtils.indexOf(MonNumCompte, "S");
				if (position != -1)
				{
					MonNbRec = NumberUtils.toInt(StringUtils.substring(StringUtils.substringAfter(MonNumCompte, "S"), 0, 1));
					centra.salarie.calcul.sens[i].Num_Compte = StringUtils.substring(StringUtils.substringBefore(MonNumCompte, "S"),0,8);
					if (MonNbRec == 1)
						MaDestination = centra.salarie.getNiv3();
					if (MonNbRec == 2)
						MaDestination = centra.salarie.getSana();
					centra.salarie.calcul.sens[i].Destination[0] = MaDestination;
					return true;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			centra.message = ClsTreater._getResultat(ClsTreater._getStackTrace(e), null, true);
			service.insertIntoMemoryLog(centra, true);
			return false;
		}

		return true;
	}

	private boolean cpt_cherche_lettreOld()
	{
		try
		{
			ClsCalculCentralisation.Sens[] LeCompte = centra.salarie.calcul.sens;
			int MonNbRec;
			String MonCpt_Type = null;
			String MaDestination = null;
			for (int i = 0; i < LeCompte.length; i++)
			{
				String MonNumCompte = LeCompte[i].Num_Compte;
				int position = StringUtils.indexOf(MonNumCompte, 'M');
				if (position != -1)
				{
					MonNbRec = NumberUtils.toInt(StringUtils.substring(MonNumCompte, position, position +1));
					centra.salarie.calcul.sens[i].Num_Compte = StringUtils.substring(MonNumCompte, 0, position - 1);
					centra.salarie.calcul.sens[i].Num_Tiers = StringUtils.substring(centra.salarie.getNmat(), 6 - MonNbRec, 6);
					MonCpt_Type = "C";
					return true;
				}

				position = StringUtils.indexOf(MonNumCompte, "N1");
				if (position != -1)
				{
					centra.salarie.calcul.sens[i].Num_Compte = StringUtils.substring(StringUtils.substring(MonNumCompte, 0, position - 1) + centra.salarie.getNiv1(),0,8);
					return true;
				}

				position = StringUtils.indexOf(MonNumCompte, "N2");
				if (position != -1)
				{
					centra.salarie.calcul.sens[i].Num_Compte = StringUtils.substring(StringUtils.substring(MonNumCompte, 0, position - 1) + centra.salarie.getNiv2(),0,8);
					return true;
				}

				position = StringUtils.indexOf(MonNumCompte, "N3");
				if (position != -1)
				{
					centra.salarie.calcul.sens[i].Num_Compte = StringUtils.substring(StringUtils.substring(MonNumCompte, 0, position - 1) + centra.salarie.getNiv3(),0,8);
					return true;
				}

				position = StringUtils.indexOf(MonNumCompte, "V");
				if (position != -1)
				{
					MonNbRec = NumberUtils.toInt(StringUtils.substring(MonNumCompte, position, position + 1));
					centra.salarie.calcul.sens[i].Num_Compte = StringUtils.substring(StringUtils.substring(MonNumCompte, 0, position - 1) + service.chercherEnNomenclature(centra.cdos, 5, centra.salarie.getVild(), 3).getVall(),0,8);
					return true;
				}

				position = StringUtils.indexOf(MonNumCompte, "S");
				if (position != -1)
				{
					MonNbRec = NumberUtils.toInt(StringUtils.substring(MonNumCompte, position, position + 1));
					centra.salarie.calcul.sens[i].Num_Compte = StringUtils.substring(StringUtils.substring(MonNumCompte, 0, position - 1),0,8);
					if (MonNbRec == 1)
						MaDestination = centra.salarie.getNiv3();
					if (MonNbRec == 2)
						MaDestination = centra.salarie.getSana();
					centra.salarie.calcul.sens[i].Destination[0] = MaDestination;
					return true;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			centra.message = ClsTreater._getResultat(ClsTreater._getStackTrace(e), null, true);
			service.insertIntoMemoryLog(centra, true);
			return false;
		}

		return true;
	}

	private boolean rech_cpt_per()
	{
		String MonCompte = null;
		if (!StringUtils.isBlank(centra.salarie.calcul.getNprt()))
			MonCompte = service.getNcptFromRhtpretEnt(centra.cdos, centra.salarie.calcul.getNprt());

		MonCompte = StringUtils.isBlank(MonCompte) ? centra.salarie.getCcpt() : MonCompte;
		if (StringUtils.isBlank(MonCompte))
		{
			centra.message = ClsTreater._getResultat("Sal. %1  Rubrique %2  Compte personnel salarie inexistant.", "ERR-90106", true, centra.salarie.getNmat(), centra.salarie.calcul.getRubq());
			service.insertIntoMemoryLog(centra, true);
			return false;
		}
		else
		{
			// if (centra.salarie.calcul.Type_Rub == 1 || centra.salarie.calcul.Type_Rub == 3)
			// {
			// centra.salarie.calcul.sens[Max_Indice].sens = "D";
			// centra.salarie.calcul.sens[Max_Indice].Num_Compte = MonCompte;
			// }
			// if (centra.salarie.calcul.Type_Rub == 2 || centra.salarie.calcul.Type_Rub == 3)
			// {
			// centra.salarie.calcul.sens[Max_Indice].sens = "C";
			// centra.salarie.calcul.sens[Max_Indice].Num_Compte = MonCompte;
			// }
			if (centra.salarie.calcul.Type_Rub == 1 || centra.salarie.calcul.Type_Rub == 3)
			{
				centra.salarie.calcul.sens[0].sens = "D";
				centra.salarie.calcul.sens[0].Num_Compte = MonCompte;
			}
			if (centra.salarie.calcul.Type_Rub == 2 || centra.salarie.calcul.Type_Rub == 3)
			{
				centra.salarie.calcul.sens[1].sens = "C";
				centra.salarie.calcul.sens[1].Num_Compte = MonCompte;
			}
		}
		return true;
	}

	private boolean rech_cpt()
	{
		Long MonIndCre = service.chercherEnNomenclature(centra.cdos, 6, centra.salarie.getGrad(), 1).getValm();
		if (MonIndCre == null)
		{
			centra.message = ClsTreater._getResultat("Salarie %1  Grade %2  inexistant en table 6.", "ERR-90109", true, centra.salarie.getNmat(), centra.salarie.getGrad());
			service.insertIntoMemoryLog(centra, true);
			return false;
		}
		MonIndCre += 1;

		if (MonIndCre < 1 || MonIndCre > 5)
		{
			centra.message = ClsTreater._getResultat("Salarie %1  Grade %2  Montant 1 mal parametre en table 6.", "ERR-90110", true, centra.salarie.getNmat(), centra.salarie.getGrad());
			service.insertIntoMemoryLog(centra, true);
			return false;
		}

		if (centra.salarie.calcul.Type_Rub == 1 || centra.salarie.calcul.Type_Rub == 3)
		{
			// centra.salarie.calcul.sens[Max_Indice].sens = "D";
			centra.salarie.calcul.sens[0].sens = "D";
			Long MonIndDeb = service.chercherEnNomenclature(centra.cdos, 4, centra.salarie.getNato(), 1).getValm();
			if (MonIndDeb == null)
			{
				centra.message = ClsTreater._getResultat("Salarie %1 Nationalite %2   inexistante en table 4", "ERR-90111", true, centra.salarie.getNmat(), centra.salarie.getNato());
				service.insertIntoMemoryLog(centra, true);
				return false;
			}
			MonIndDeb += ((MonIndCre - 1) * 4) + 1;
			if (MonIndDeb < 1 || MonIndDeb > 20)
			{
				centra.message = ClsTreater._getResultat("Salarie %1 Grade %2 Nationalite %3 : Combinaison impossible.", "ERR-90112", true, centra.salarie.getNmat(), centra.salarie.getGrad(), centra.salarie.getNato());
				service.insertIntoMemoryLog(centra, true);
				return false;
			}

			String MonNum_Compte = getCompteRubriqueIndDeb(MonIndDeb);
			if (StringUtils.isBlank(MonNum_Compte))
			{
				centra.message = ClsTreater._getResultat(" Rbq %1 Compte au debit %2  é renseigner.", "ERR-90113", true, centra.salarie.calcul.getRubq(),  MonIndDeb.toString());
				service.insertIntoMemoryLog(centra, true);
				return false;
			}

			if (StringUtils.equalsIgnoreCase("PERS", MonNum_Compte))
			{
				MonNum_Compte = centra.salarie.getCcpt();
			}
			if (StringUtils.isBlank(MonNum_Compte))
			{
				centra.message = ClsTreater._getResultat(" Agent %1  Compte personnel é renseigner.", "ERR-90114", true, centra.salarie.getNmat());
				service.insertIntoMemoryLog(centra, true);
				return false;
			}

			// centra.salarie.calcul.sens[Max_Indice].Num_Compte = MonNum_Compte;
			centra.salarie.calcul.sens[0].Num_Compte = MonNum_Compte;
		}

		if (centra.salarie.calcul.Type_Rub == 2 || centra.salarie.calcul.Type_Rub == 3)
		{
			// centra.salarie.calcul.sens[Max_Indice].sens = "C";
			centra.salarie.calcul.sens[1].sens = "C";
			
			String MonNum_Compte = getCompteRubriqueIndCre(MonIndCre);
			if (StringUtils.isBlank(MonNum_Compte))
			{
				centra.message = ClsTreater._getResultat("Rbq %1 . Compte de crédit %2  é renseigner.", "ERR-90117", true, centra.salarie.calcul.getRubq(),  MonIndCre.toString());
				service.insertIntoMemoryLog(centra, true);
				return false;
			}

			if (StringUtils.equalsIgnoreCase("PERS", MonNum_Compte))
			{
				MonNum_Compte = centra.salarie.getCcpt();
			}
			if (StringUtils.isBlank(MonNum_Compte))
			{
				centra.message = ClsTreater._getResultat(" Agent %1  Compte personnel é renseigner.", "ERR-90114", true, centra.salarie.getNmat());
				service.insertIntoMemoryLog(centra, true);
				return false;
			}

			// centra.salarie.calcul.sens[Max_Indice].Num_Compte = MonNum_Compte;
			centra.salarie.calcul.sens[1].Num_Compte = MonNum_Compte;
		}
		
		if (!cpt_cherche_lettre())
		{
			centra.message = ClsTreater._getResultat(" Agent %1  Compte personnel é renseigner.", "ERR-90114", true, centra.salarie.getNmat());
			service.insertIntoMemoryLog(centra, true);
			return false;
		}

		return true;
	}
	
	private boolean rech_cpt_patrice()
	{
		Long MonIndCre = service.chercherEnNomenclature(centra.cdos, 6, centra.salarie.getGrad(), 1).getValm();
		if (MonIndCre == null)
		{
			centra.message = ClsTreater._getResultat("Salarie %1  Grade %2  inexistant en table 6.", "ERR-90109", true, centra.salarie.getNmat(), centra.salarie.getGrad());
			service.insertIntoMemoryLog(centra, true);
			return false;
		}
		MonIndCre += 1;

		if (MonIndCre < 1 || MonIndCre > 5)
		{
			centra.message = ClsTreater._getResultat("Salarie %1  Grade %2  Montant 1 mal parametre en table 6.", "ERR-90110", true, centra.salarie.getNmat(), centra.salarie.getGrad());
			service.insertIntoMemoryLog(centra, true);
			return false;
		}

		if (centra.salarie.calcul.Type_Rub == 1 || centra.salarie.calcul.Type_Rub == 3)
		{
			// centra.salarie.calcul.sens[Max_Indice].sens = "D";
			centra.salarie.calcul.sens[0].sens = "D";
			Long MonIndDeb = service.chercherEnNomenclature(centra.cdos, 4, centra.salarie.getNato(), 1).getValm();
			if (MonIndDeb == null)
			{
				centra.message = ClsTreater._getResultat("Salarie %1 Nationalite %2   inexistante en table 4", "ERR-90111", true, centra.salarie.getNmat(), centra.salarie.getNato());
				service.insertIntoMemoryLog(centra, true);
				return false;
			}
			MonIndDeb += ((MonIndCre - 1) * 4) + 1;
			if (MonIndDeb < 1 || MonIndDeb > 20)
			{
				centra.message = ClsTreater._getResultat("Salarie %1 Grade %2 Nationalite %3 : Combinaison impossible.", "ERR-90112", true, centra.salarie.getNmat(), centra.salarie.getGrad(), centra.salarie.getNato());
				service.insertIntoMemoryLog(centra, true);
				return false;
			}

			String MonNum_Compte = getCompteRubriqueIndDeb(MonIndDeb);
			if (StringUtils.isBlank(MonNum_Compte))
			{
				centra.message = ClsTreater._getResultat(" Rbq %1 Compte au debit %2  é renseigner.", "ERR-90113", true, centra.salarie.calcul.getRubq(),  MonIndDeb.toString());
				service.insertIntoMemoryLog(centra, true);
				return false;
			}

			if (StringUtils.equalsIgnoreCase("PERS", MonNum_Compte))
			{
				MonNum_Compte = centra.salarie.getCcpt();
			}
			if (StringUtils.isBlank(MonNum_Compte))
			{
				centra.message = ClsTreater._getResultat(" Agent %1  Compte personnel é renseigner.", "ERR-90114", true, centra.salarie.getNmat());
				service.insertIntoMemoryLog(centra, true);
				return false;
			}

			// centra.salarie.calcul.sens[Max_Indice].Num_Compte = MonNum_Compte;
			centra.salarie.calcul.sens[0].Num_Compte = MonNum_Compte;
			if (!cpt_cherche_lettre())
			{
				centra.message = ClsTreater._getResultat(" Agent %1  Compte personnel é renseigner.", "ERR-90114", true, centra.salarie.getNmat());
				service.insertIntoMemoryLog(centra, true);
				return false;
			}
		}

		if (centra.salarie.calcul.Type_Rub == 2 || centra.salarie.calcul.Type_Rub == 3)
		{
			// centra.salarie.calcul.sens[Max_Indice].sens = "C";
			centra.salarie.calcul.sens[1].sens = "C";
		}

		return true;
	}

	private String getCompteRubriqueIndDeb(Long MonIndDeb)
	{
		/*
		 * Ideal de le faire ainsi, par réflexion
		try
		{
			String methodName="getDe"+ClsStringUtil.formatNumber(MonIndDeb, "00");
			Method method = centra.salarie.calcul.rubrique.getClass().getMethod(methodName, null);
			return String.valueOf(method.invoke(centra.salarie.calcul.rubrique, null));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		*/
		if (MonIndDeb == 1)
			return centra.salarie.calcul.rubrique.getDe01();
		if (MonIndDeb == 2)
			return centra.salarie.calcul.rubrique.getDe02();
		if (MonIndDeb == 3)
			return centra.salarie.calcul.rubrique.getDe03();
		if (MonIndDeb == 4)
			return centra.salarie.calcul.rubrique.getDe04();
		if (MonIndDeb == 5)
			return centra.salarie.calcul.rubrique.getDe05();
		if (MonIndDeb == 6)
			return centra.salarie.calcul.rubrique.getDe06();
		if (MonIndDeb == 7)
			return centra.salarie.calcul.rubrique.getDe07();
		if (MonIndDeb == 8)
			return centra.salarie.calcul.rubrique.getDe08();
		if (MonIndDeb == 9)
			return centra.salarie.calcul.rubrique.getDe09();
		if (MonIndDeb == 10)
			return centra.salarie.calcul.rubrique.getDe10();
		if (MonIndDeb == 11)
			return centra.salarie.calcul.rubrique.getDe11();
		if (MonIndDeb == 12)
			return centra.salarie.calcul.rubrique.getDe12();
		if (MonIndDeb == 13)
			return centra.salarie.calcul.rubrique.getDe13();
		if (MonIndDeb == 14)
			return centra.salarie.calcul.rubrique.getDe14();
		if (MonIndDeb == 15)
			return centra.salarie.calcul.rubrique.getDe15();
		if (MonIndDeb == 16)
			return centra.salarie.calcul.rubrique.getDe16();
		if (MonIndDeb == 17)
			return centra.salarie.calcul.rubrique.getDe17();
		if (MonIndDeb == 18)
			return centra.salarie.calcul.rubrique.getDe18();
		if (MonIndDeb == 19)
			return centra.salarie.calcul.rubrique.getDe19();
		if (MonIndDeb == 20)
			return centra.salarie.calcul.rubrique.getDe20();
		return null;
	}
	
	private String getCompteRubriqueIndCre(Long MonIndCre)
	{
		/*
		 * Ideal de le faire ainsi, par réflexion
		try
		{
			String methodName="getCr"+ClsStringUtil.formatNumber(MonIndCre, "00");
			Method method = centra.salarie.calcul.rubrique.getClass().getMethod(methodName, null);
			return String.valueOf(method.invoke(centra.salarie.calcul.rubrique, null));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		*/
		if (MonIndCre == 1)
			return centra.salarie.calcul.rubrique.getCr01();
		if (MonIndCre == 2)
			return centra.salarie.calcul.rubrique.getCr02();
		if (MonIndCre == 3)
			return centra.salarie.calcul.rubrique.getCr03();
		if (MonIndCre == 4)
			return centra.salarie.calcul.rubrique.getCr04();
		if (MonIndCre == 5)
			return centra.salarie.calcul.rubrique.getCr05();
		
		return null;
	}

	private boolean verifieExistenceSection()
	{
		ClsCalculCentralisation.Sens[] LeCompte = centra.salarie.calcul.sens;
		String MaSection = null;
		for (int i = 0; i < LeCompte.length; i++)
		{
			if (centra.salarie.calcul.sens[i].suivi_anal)
			{
				for (int j = 0; j < 9; j++)
				{
					MaSection = centra.salarie.calcul.sens[i].Destination[j];
					if (!StringUtils.isBlank(MaSection))
					{
						if (!sectionExiste(centra.numdos_centra, MaSection, j))
						{
							centra.message = ClsTreater._getResultat("Destination, axe %1: %2 inconnue en comptabilite. Sal :%3.Rub:%4", "ERR-90108", true, String.valueOf(j + 1), MaSection, centra.salarie.getNmat(), centra.salarie.calcul.rubrique.getCrub());
							service.insertIntoMemoryLog(centra, true);
							return false;
						}
					}
				}
			}
		}

		return true;
	}

	private boolean charg_etssec()
	{
		String[] MaSection = new String[] { null, null, null, null, null, null, null, null, null };
		if (StringUtils.isBlank(centra.Code_Etb_Commun))
		{
			String MonEtab = service.chercherEnNomenclature(centra.cdos, 1, centra.salarie.getNiv1(), 2).getVall();
			if (StringUtils.isBlank(MonEtab))
				centra.Code_Etablissement = centra.salarie.getNiv1();
		}
		else
			centra.Code_Etablissement = centra.Code_Etb_Commun;

		if (centra.deltacom)
		{
			if (StringUtils.isBlank(service.getLibetsFromCpEts(centra.numdos_centra, centra.Code_Etablissement)))
			{
				centra.message = ClsTreater._getResultat("Etablissement standard %1 inexistant en comptabilite.", "ERR-90092", true, centra.Code_Etablissement);
				service.insertIntoMemoryLog(centra, true);
				return false;
			}

			if (!StringUtils.isBlank(centra.salarie.getSana()))
				MaSection[0] = centra.salarie.getSana();
			else
			{
				MaSection[0] = service.chercherEnNomenclature(centra.cdos, 3, centra.salarie.getNiv1(), 2).getVall();
				if (StringUtils.isBlank(MaSection[0]))
					MaSection[0] = centra.salarie.getNiv3();
			}

			if (!StringUtils.isBlank(centra.Dest2_Commune))
				MaSection[1] = service.chercherEnNomenclature(centra.cdos, 3, centra.salarie.getNiv1(), 3).getVall();
			else
				MaSection[1] = centra.Dest2_Commune;

			for (int i = 2; i < 9; i++)
			{
				MaSection[i] = service.chercherEnNomenclature(centra.cdos, 3, centra.salarie.getNiv1(), i + 1).getVall();
			}
		}

		if (!centra.deltacom && centra.interface_gl)
		{
			for (int i = 0; i < 9; i++)
			{
				MaSection[i] = concpro(centra.destination_gl[i]);
			}
		}
		for (int i = 0; i < 2; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				centra.salarie.calcul.sens[i].Destination[j] = MaSection[j];
			}
		}

		return true;
	}

	private boolean sectionExiste(String cdos, String codedestination, Integer numaxe)
	{
		return !StringUtils.isBlank(service.getInformationSectionFromCpDe(cdos, codedestination, numaxe).getLibdes());
	}

	private String concpro(String destination_gl)
	{
		boolean MaRef = false;
		String num_tab = null;
		String MonCACC = null;
		String num_lib = null;
		String cle = null;
		String MaSection = null;
		if (StringUtils.equalsIgnoreCase(StringUtils.substring(destination_gl, 0, 1), "@"))
		{
			MaRef = true;
			num_tab = StringUtils.substring(destination_gl, 2, 2);
			num_lib = StringUtils.substring(destination_gl, 5, 2);
			cle = StringUtils.substring(destination_gl, 8, 3);
		}
		else
		{
			MaRef = false;
			cle = destination_gl;
		}

		MonCACC = getInfoSal(Integer.valueOf(cle));
		if (MaRef)
			MaSection = StringUtils.substring(service.chercherEnNomenclature(centra.cdos, num_tab, MonCACC, NumberUtils.toInt(num_lib)).getVall().toString(), 0);
		else
			MaSection = MonCACC;

		return MaSection;

	}

	private String getInfoSal(Integer cle)
	{
		if (cle == 1)
			return centra.salarie.getNiv1();
		if (cle == 2)
			return centra.salarie.getNiv2();
		if (cle == 3)
			return centra.salarie.getNiv3();
		if (cle == 4)
			return centra.salarie.getNmat();
		if (cle == 8)
			return centra.salarie.getEqui();
		if (cle == 11)
			return centra.salarie.getSexe();
		if (cle == 21)
			return centra.salarie.getNato();
		if (cle == 22)
			return centra.salarie.getSitf();
		if (cle == 23)
			return ClsStringUtil.formatNumber(centra.salarie.getNbcj(), "9999");
		if (cle == 24)
			return ClsStringUtil.formatNumber(centra.salarie.getNbec(), "9999");
		if (cle == 25)
			return ClsStringUtil.formatNumber(centra.salarie.getNbfe(), "9999");
		if (cle == 27)
			return centra.salarie.getModp();
		if (cle == 33)
			return centra.salarie.getCcpt();
		if (cle == 39)
			return centra.salarie.getCat();
		if (cle == 40)
			return centra.salarie.getEch();
		if (cle == 43)
			return centra.salarie.getGrad();
		if (cle == 44)
			return centra.salarie.getFonc();
		if (cle == 45)
			return centra.salarie.getAfec();
		if (cle == 47)
			return centra.salarie.getIndi() == null ? null : centra.salarie.getIndi().toString();
		if (cle == 58)
			return centra.salarie.getHifo();
		if (cle == 59)
			return centra.salarie.getZli2();
		if (cle == 60)
			return centra.salarie.getZli1();
		if (cle == 67)
			return centra.salarie.getTypc();
		if (cle == 70)
			return centra.salarie.getRegi();
		if (cle == 71)
			return centra.salarie.getZres();
		if (cle == 72)
			return centra.salarie.getDmst();
		if (cle == 73)
			return centra.salarie.getNpie() == null ? null : centra.salarie.getNpie().toString();
		if (cle == 184)
			return centra.salarie.getSana();
		if (cle == 185)
			return centra.salarie.getEqui();
		if (cle == 770)
			return centra.salarie.getModp();
		if (cle >= 801 && cle <= 899)
			return recZli(centra.cdos, centra.salarie.getNmat(), StringUtils.substring(cle.toString(), 1, 2));
		return " ";
	}

	private String recZli(String cdos, String nmat, String numZl)
	{
		return service.chercherZoneLibreAgent(cdos, nmat, Integer.valueOf(numZl));
	}

	public ClsCentralisationService getService()
	{
		return service;
	}

	public void setService(ClsCentralisationService service)
	{
		this.service = service;
	}

}
