/**
 * 
 */
package com.kinart.paie.business.services.cloture;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.kinart.paie.business.model.*;
import com.kinart.paie.business.services.calcul.ClsCodeLibelle;
import com.kinart.paie.business.services.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.springframework.dao.DataAccessException;

/**
 * @author YT
 * 
 */
public class ClsCentralisationBank implements ICentralisation
{
	private Session session;

	private ClsCentralisationService centraservice = null;

	private ClsPafnomType pafnom = new ClsPafnomType();

	private ParamData fnom;

	private ClsGlobalUpdate globalUpdate = null;

	public String v_cdos;

	private String Rbq_NAP;

	private String Rbq_BRUT;

	private String Rbq_ACQZ;

	private char exist;

	private String char4;

	private Integer nbbul;

	private Integer nb_sal;

	private Boolean retour;

	private String Lib_Mois;

	private String mess;

	private String mess2;

	private char cas;

	private String aamm;

	private Integer nbul;

	private String cuti;

	private String clang;

	private Integer nddd;

	private ClsDate Date_Comptable;
	
	private ClsDate Date_Valeur;
	public ClsDate getDate_Valeur()
	{
		return Date_Valeur;
	}

	public void setDate_Valeur(ClsDate date_Valeur)
	{
		Date_Valeur = date_Valeur;
	}

	private String pipe_name;

	private Integer timeout;

	private String Code_Journal;

	private char sens;

	private String Num_compte;

	private String Num_Piece;

	private String Libelle_Ecriture;

	private String Libelle_Abrege;

	private String[] Destination = new String[] { null, null, null, null, null, null, null, null, null, null };

	private String char10;

	private char Cpt_Type;

	private String Code_Etablissement;

	private String Num_Tiers;

	private String Type_Num_Tiers;

	private String Devise_Dossier;

	private String Cod_Etb_Commun;

	private String Dest2_Commune;

	private Boolean Existence_Compte;

	private Boolean Suivi_Analytique;

	private Boolean centra_dos;

	private String numdos_centra;

	private Boolean exist_cpt;

	private Boolean exist_aux;

	private String Cpt_numcpt;

	private Integer ind_deb;

	private Integer ind_cre;

	private Double Total_Debit;

	private Double Total_Credit;

	private Double wecart;

	private Integer Type_Rubq;

	private double Mnt_Ecriture;

	private InterfComptable wint;
	private MouvCptPaie wpampai;

	private String cdos;

	private char comp;

	private HttpServletRequest request;

	private CalculPaie calcul = new CalculPaie();

	private ElementSalaire rubrique = new ElementSalaire();

	public static Integer compteur = 0;

	private boolean blib = false;

	private Integer llib = 2;

	private boolean centra_par_cpt = false;
	private boolean compte_avec_suffixe = false;
	private boolean gen_cpt_agence_dom = false;
	private String libelle_NAP;
	private String libelle_GLOBAL;
	private boolean libelle_Egal_LibelleRubrique = false;
	private String mode_virement;
	private String code_dev;
	private String code_operation;
	private String code_agence;
	private String code_agence_dech_tiers;
	private String code_agence_comparaison;
	private String agence_comptable;
	private String compte_liaison;
	private boolean centralisionSurUneRubrique = false;
	private String rubriqueAComptabiliser;
	private boolean crediteur = true;
	
	private boolean cpt_credit_nap = false;
	
	private Integer tableCodeSite = 274;
	
	private boolean ancienneApprocheCentralisation = true;
	
	private boolean agenceToujoursSite = false;
	
	private static final String COMPTE_PERSONNEL_AGENT = "PERS";
	private static final String COMPTE_BANCAIRE_AGENT = "COMP";
	private static final String COMMENTAIRE_AGENT = "COMM";
	String nomclient;

	/**
	 * @param aamm
	 *            yyMM
	 * @param nbul
	 * @param cuti
	 * @param clang
	 * @param nddd
	 * @param date_Comptable
	 * @param cdos
	 */
	public ClsCentralisationBank(HttpServletRequest request, ClsGlobalUpdate globalUpdate, ClsCentralisationService centraservice, String aamm, Integer nbul,
								 String cuti, String clang, Integer nddd, ClsDate date_Comptable, String cdos)
	{
		this.request = request;
		this.centraservice = centraservice;
		this.aamm = aamm;
		this.nbul = nbul;
		this.cuti = cuti;
		this.clang = clang;
		this.nddd = nddd;
		this.Date_Comptable = date_Comptable;
		this.cdos = cdos;
		this.globalUpdate = globalUpdate;	
	}
	boolean nouvelleCompta = false;

	public List<Salarie> chg_sal() throws Exception
	{

		ParameterUtil.println(">> Entr�e dans chg_sal(0)");
		List list = null;
		try
		{
			list = centraservice.getListeSalarieFromRhpagent(this.cdos, this.aamm, this.nbul);
		}
		catch (DataAccessException ex)
		{
			ex.printStackTrace();
			throw ex;
		}
		if (list != null && list.size() > 0)
		{
			nb_sal = list.size();
		}

		return list;
	}

	// Parametrage des zones pour la destination analytique 2
	// Cre�er la cl� CENTRA-DES : Lib i : Colonne Destination i (niv1,niv2,niv3,site), Montant i :num�ro de la table des donn�es, Taux i :num�ro du libell� �
	// lire

	List<ParamData> paramDestinations = new ArrayList<ParamData>();

	@SuppressWarnings("unchecked")
	public boolean init(char comp) throws Exception
	{
		ParameterUtil.println(">> Entr�e dans init(1)");
		try
		{
			
			compteur = 0;
			
			rubriqueAComptabiliser = ParameterUtil.getSessionObject(request, "rubriqueAComptabiliser");
			String typeContrepartie = ParameterUtil.getSessionObject(request, "typeContrepartie");
			String compteContrepartie = ParameterUtil.getSessionObject(request, "compteContrepartie");
			
			if(StringUtils.isNotBlank(rubriqueAComptabiliser) && StringUtils.isNotBlank(typeContrepartie) )
			{
				centralisionSurUneRubrique = true;
			}
			String crediteur = ParameterUtil.getSessionObject(request, "crediteurOuDebiteur");
			if(StringUtils.isNotBlank(crediteur) && StringUtils.equals("N", crediteur) )
			{
				this.crediteur = false;
			}
			else
				this.crediteur = true;

			// w_acompte = SUBSTR(l_options,31,1);
			// IF PA_PAIE.NouB(w_acompte) THEN
			// centralisionSurUneRubrique = FALSE;
			// ELSIF w_acompte = 'A' THEN
			// centralisionSurUneRubrique = TRUE;
			// ELSE
			// centralisionSurUneRubrique = FALSE;
			// END IF;

			ParamData fnom = globalUpdate.getService().findAnyColumnFromNomenclature(cdos,"", "99","NEWCPTRUB","1");
			//(Rhfnom)globalUpdate.getService().get(Rhfnom.class, new RhfnomPK(cdos,99,"NEWCPTRUB",1));
			nouvelleCompta = (fnom != null && StringUtils.isNotBlank(fnom.getVall()) && StringUtils.equals("O", fnom.getVall()));

			
			ClsDate date;

			Num_Piece = aamm.substring(4, 6) + aamm.substring(0, 4) + nbul.toString().trim();

			date = new ClsDate(aamm, "yyyyMM");
			Lib_Mois = date.getDateS("MMMM");
			Libelle_Ecriture = "Paie:Bul. " + nbul.toString().trim() + " / " + Lib_Mois + " " + date.getDateS("yyyy");

			fnom = centraservice.chercherEnNomenclature(cdos, 99, "RUBNAP", 1);
			if (fnom.getValm() != null)
			{
				Rbq_NAP = StringUtils.leftPad(fnom.getValm().toString(), 4, '0');
			}
			else
			{
				mess = centraservice.chercherMessage(request, "INF-10116", clang);
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}

//			fnom = centraservice.chercherEnNomenclature(cdos, 99, "RUBACQZ", 1);
//			if (fnom.getValm() != null)
//			{
//				Rbq_ACQZ = StringUtils.leftPad(fnom.getValm().toString(), 4, '0');
//			}
//			else
//			{
//				mess = "Creer le parametre RUBACQZ, table 99";
//				globalUpdate._setEvolutionTraitement(request, mess, true);
//				return false;
//			}
			
			//Si centralisation d'une rubrique uniquement, alors le nap est la rubrique � comptabiliser
			if(centralisionSurUneRubrique)
				Rbq_NAP = rubriqueAComptabiliser;
			
			fnom = centraservice.chercherEnNomenclature(cdos, 266, "NOMCLIENT", 2);
			if(StringUtils.isNotBlank(fnom.getVall())) 
				nomclient = fnom.getVall();

			fnom = centraservice.chercherEnNomenclature(cdos, 99, "RUBBRUT", 1);
			if (fnom.getValm() != null)
			{
				Rbq_BRUT = StringUtils.leftPad(fnom.getValm().toString(), 4, '0');
			}
			else
			{
				mess = centraservice.chercherMessage(request, "INF-10113", clang);
				globalUpdate._setEvolutionTraitement(request, "INF-10113", true);
				return false;
			}

			fnom = centraservice.chercherEnNomenclature(cdos, 99, "INT-BANK", 1);
			if (StringUtils.isBlank(fnom.getVall()))
			{
				mess = "Creer le parametre INT-BANK,Lib1, table 99";
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
			else
			{
				if (StringUtil.notIn(fnom.getVall(), "O,o,N,n"))
				{
					mess = "PARAM INT-BANK en table 99 Lib 1 (O/N).";
					globalUpdate._setEvolutionTraitement(request, mess, true);
					return false;
				}
				else centra_par_cpt = StringUtil.in(fnom.getVall(), "O,o");
			}

			fnom = centraservice.chercherEnNomenclature(cdos, 99, "INT-BANK", 2);
			if (fnom.getValm() == null)
			{
				mess = "Creer le parametre INT-BANK,Mnt 2, table 99";
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
			else
			{
				if (StringUtil.notIn(fnom.getValm().intValue() + "", "0,1"))
				{
					mess = "PARAM INT-BANK en table 99 Mnt 2 (0/1).";
					globalUpdate._setEvolutionTraitement(request, mess, true);
					return false;
				}
				else compte_avec_suffixe = fnom.getValm().intValue() == 1;
			}

			fnom = centraservice.chercherEnNomenclature(cdos, 99, "INT-BANK", 3);
			if (fnom.getValm() == null)
			{
				mess = "Creer le parametre INT-BANK,Mnt 3, table 99";
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
			else
			{
				if (StringUtil.notIn(fnom.getValm().intValue() + "", "0,1"))
				{
					mess = "PARAM INT-BANK en table 99 Mnt 3 (0/1).";
					globalUpdate._setEvolutionTraitement(request, mess, true);
					return false;
				}
				else gen_cpt_agence_dom = fnom.getValm().intValue() == 1;
			}

			fnom = centraservice.chercherEnNomenclature(cdos, 99, "PAIE-INTER", 4);
			if (StringUtils.isBlank(fnom.getVall()))
			{
				mess = "Creer le parametre PAIE-INTER,Lib 4, table 99";
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
			else
			{
				libelle_NAP = StringUtil.oraSubstring(fnom.getVall(), 1, 20);
			}

			// -- MM 01-2006 si bulletin supplementaire alors on recupere le libelle du bulletin
			// -- en table 92 des bulletins Sup. si pas de libelle en T92 alors on prend le lib std.
			if (nbul != 9)
			{
				String query = "From ParamData where idEntreprise = '" + cdos + "' and ctab = 92 and cacc ='" + aamm + "' and valm = " + nbul;
				List<ParamData> lst = centraservice.chercherEnNomenclature(query);
				if (!lst.isEmpty() && StringUtils.isNotBlank(lst.get(0).getVall()))
					libelle_NAP = StringUtil.oraSubstring(lst.get(0).getVall(), 1, 20);
			}

			fnom = centraservice.chercherEnNomenclature(cdos, 99, "INT-BANK", 2);
			if (StringUtils.isBlank(fnom.getVall()))
			{
				mess = "Creer le parametre INT-BANK,Lib 2, table 99";
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
			else
			{
				code_dev = StringUtil.oraSubstring(fnom.getVall(), 1, 3);
			}

			fnom = centraservice.chercherEnNomenclature(cdos, 99, "INT-BANK", 3);
			if (StringUtils.isBlank(fnom.getVall()))
			{
				mess = "Creer le parametre INT-BANK,Lib 3, table 99";
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
			else
			{
				code_operation = StringUtil.oraSubstring(fnom.getVall(), 1, 3);
			}
			if(centralisionSurUneRubrique)
			{
				fnom = centraservice.chercherEnNomenclature(cdos, 99, "ACOMPTE", 2);
				if (StringUtils.isBlank(fnom.getVall()))
				{
					mess = "Creer le parametre ACOMPTE,Lib 2, table 99";
					globalUpdate._setEvolutionTraitement(request, mess, true);
					return false;
				}
				else
				{
					code_dev = StringUtil.oraSubstring(fnom.getVall(), 1, 3);
				}

				fnom = centraservice.chercherEnNomenclature(cdos, 99, "ACOMPTE", 3);
				if (StringUtils.isBlank(fnom.getVall()))
				{
					mess = "Creer le parametre ACOMPTE,Lib 3, table 99";
					globalUpdate._setEvolutionTraitement(request, mess, true);
					return false;
				}
				else
				{
					code_operation = StringUtil.oraSubstring(fnom.getVall(), 1, 4);
				}
			}

			fnom = centraservice.chercherEnNomenclature(cdos, 99, "INT-BANK", 1);
			if (fnom.getValm() == null)
			{
				mess = "Creer le parametre INT-BANK,Mnt 1, table 99";
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
			else
			{
				if (fnom.getValm() != 0)
					code_agence = ClsStringUtil.formatNumber(fnom.getValm(), "00000");
			}

			// -- Lecture du code paiement virement en table 54
			mode_virement = StringUtils.EMPTY;
			String query = "From ParamData where idEntreprise = '" + cdos + "' and ctab = 54 and nume = 4 and vall = 'V' ";
			List<ParamData> lst = centraservice.chercherEnNomenclature(query);
			if (!lst.isEmpty())
			{
				for(int i=0;i<lst.size();i++)
				{
					if(i==0) mode_virement = lst.get(i).getCacc();
					else mode_virement +=","+ lst.get(i).getCacc();
				}
				
			}

			fnom = centraservice.chercherEnNomenclature(cdos, 99, "JOURPAIE", 1);
			if (StringUtils.isNotBlank(fnom.getVall()))
			{
				Code_Journal = StringUtils.substring(fnom.getVall(), 0, 5);
			}
			else
			{
				mess = centraservice.chercherMessage(request, "ERR-90090", clang);
				globalUpdate._setEvolutionTraitement(request, "ERR-90090", true);
				return false;
			}

			fnom = centraservice.chercherEnNomenclature(cdos, 99, "JOURPAIE", 2);
			if (StringUtils.isNotBlank(fnom.getVall()))
			{
				Cod_Etb_Commun = StringUtils.substring(fnom.getVall(), 0, 3);
			}

			fnom = centraservice.chercherEnNomenclature(cdos, 99, "JOURPAIE", 3);
			if (StringUtils.isNotBlank(fnom.getVall()))
			{
				Dest2_Commune = StringUtils.substring(fnom.getVall(), 0, 8);
			}

			fnom = centraservice.chercherEnNomenclature(cdos, 99, "JOURPAIE", 4);
			if (StringUtils.isNotBlank(fnom.getVall()))
			{
				Libelle_Abrege = StringUtils.substring(fnom.getVall(), 0, 2);
			}

			// -- Code de la devise dossier
			DossierPaie cpdo = centraservice.getCpdos(this.cdos);
			if (cpdo == null)
			{
				mess = centraservice.chercherMessage(request, "ERR-90099", clang, cdos);
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}

			Devise_Dossier = cpdo.getDdev();

			if (StringUtils.isBlank(Devise_Dossier))
			{
				mess = centraservice.chercherMessage(request, "ERR-90100", clang, cdos);
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}

			if(! centralisionSurUneRubrique)
			{
				nbbul = centraservice.getNbreBulFromRhtcalcul(this.cdos, this.aamm, this.nbul.toString(), Rbq_NAP);
				if (nbbul == 0)
				{
					mess = centraservice.chercherMessage(request, "ERR-90101", clang);
					globalUpdate._setEvolutionTraitement(request, mess, true);
					return false;
				}
	
				nbbul = centraservice.getNbreBulNonEditeFromRhtcalcul(this.cdos, this.aamm, this.nbul.toString(), Rbq_NAP);
				if (nbbul > 0)
				{
					mess = centraservice.chercherMessage(request, "INF-10207", clang);
					globalUpdate._setEvolutionTraitement(request, mess, true);
					return false;
				}
			}

			query = "From ParamData where idEntreprise = '" + cdos + "' and ctab = 99 and cacc='CENTRA-DES' order by nume";
			paramDestinations = centraservice.service.find(query);
			
			//Libell� 6
			fnom = centraservice.chercherEnNomenclature(cdos, 99, "INT-BANK", 6);
			cpt_credit_nap = StringUtils.equals(fnom.getVall(), "O");
			//Montant 6 : permet de savoir si le code agence sera toujours ou non le code site du salari� au lieu du guichet dans rhtvrmt
			agenceToujoursSite= (fnom.getValm() != null) && (fnom.getValm().intValue() == 1);
			
			//Libell� 7 : permet de savoir si l'approche utilis� est la nouvelle ou l'ancienne avec les tables de liaisons
			fnom = centraservice.chercherEnNomenclature(cdos, 99, "INT-BANK", 7);
			ancienneApprocheCentralisation = StringUtils.equals(fnom.getVall(), "O");
			//Montant 7 : nombre de colonnes suppl�mentaires � ajouter au fichier final
			
			//Libell� 8 : permet de stocker l'agence de comptabilisation des charges et des tiers
			fnom = centraservice.chercherEnNomenclature(cdos, 99, "INT-BANK", 8);
			code_agence_dech_tiers = fnom.getVall();
			
			//Libell� 9 : Agence comparaison
			fnom = centraservice.chercherEnNomenclature(cdos, 99, "INT-BANK", 9);
			code_agence_comparaison = fnom.getVall();
			
			//Gestion des intutil�s des rubriques sp�cifiques
			//Dans paie-inter, lib6, on met l'intitul� global des rubriques autre que le NAP, dont le libell� est dans le lib4
			fnom = centraservice.chercherEnNomenclature(cdos, 99, "PAIE-INTER", 6);
			if (StringUtils.isNotBlank(fnom.getVall()))
			{
				libelle_GLOBAL = StringUtil.oraSubstring(fnom.getVall(), 1, 20);
			}
			
			//Dans paie-inter, lib7, on met si oui ou non le libell� du mouvement est le libell� de la rubrique
			fnom = centraservice.chercherEnNomenclature(cdos, 99, "PAIE-INTER", 7);
			if (StringUtils.isNotBlank(fnom.getVall()))
			{
				libelle_Egal_LibelleRubrique = StringUtils.equalsIgnoreCase(fnom.getVall(), "O");
			}
			
			libelleRubMap = new HashMap<String, String>();
			String sql="From ParamData where idEntreprise = '"+cdos+"' and ctab = 400 and nume = 1";
			List<ParamData> libelles = centraservice.chercherEnNomenclature(sql);
			for(ParamData lib : libelles)
				libelleRubMap.put(lib.getCacc(), lib.getVall());
			// suppression des anciennes �critures
			session.createSQLQuery("Delete From Rhtmpai ").executeUpdate();

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			logMessage(ex.getMessage());
			return false;
		}
		return true;
	}

	private void logMessage(Object objMessage)
	{
		if (objMessage != null)
			ParameterUtil.println(objMessage.toString());
	}

	public boolean centra_dossier() throws Exception
	{
		ParameterUtil.println("Entr�e dans centra_dossier");
		String lib, dos = null;

		lib = centraservice.chercherEnNomenclature(this.cdos, 99, "DOS-CENTRA", 1).getVall();
		if (StringUtils.isNotBlank(lib))
			lib = StringUtils.substring(lib, 0, 1);
		else lib = "N";

		dos = centraservice.chercherEnNomenclature(this.cdos, 99, "DOS-CENTRA", 2).getVall();
		if (StringUtils.isNotBlank(dos))
			dos = StringUtils.substring(dos, 0, 2);
		else dos = null;

		if (lib.compareToIgnoreCase("O") == 0 && NouB(dos))
		{
			mess = centraservice.chercherMessage(request, "ERR-30165", clang, "DOS-CENTRA", "99", "2");
			globalUpdate._setEvolutionTraitement(request, mess, true);
			centra_dos = false;
			numdos_centra = null;
			return false;
		}

		centra_dos = (lib.compareToIgnoreCase("O") == 0);
		numdos_centra = dos;
		return true;
	}

	private boolean NouB(Object obj)
	{
		return ClsObjectUtil.isNull(obj);
	}

	// ----------------------------------------------------------------------------
	// -- Chargement des infos de la banque principale (multi_banques ) dans wsal01
	// -- dans le cas une install DELTA-BANK on utilise uniquement la banque principale
	// ----------------------------------------------------------------------------

	private boolean charg_infos_banque(Salarie salarie)
	{
		if (StringUtil.in(salarie.getModp(), mode_virement))
		{
			String query = "From VirementSalarie where idEntreprise = '" + cdos + "' and nmat = '" + salarie.getNmat() + "' and princ='O'";
			List<VirementSalarie> banques = centraservice.service.find(query);
			if (banques.isEmpty())
			{
				mess = " Agent " + salarie.getNmat() + "  : Banque principale inexistante.";
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
			VirementSalarie banque = banques.get(0);
			salarie.setBanq(banque.getBqag());
			salarie.setGuic(banque.getGuic());
			salarie.setComp(banque.getComp());
			salarie.setBqso(banque.getBqso());
			salarie.setCle(banque.getCle());
		}
		return true;
	}

	public boolean lect_rub_cal(Salarie salarie) throws Exception
	{
		ParameterUtil.println("Entr�e dans lect_rub_cal");

		// Appel des m�thodes charg_infos_banque
		if (!this.charg_infos_banque(salarie))
			return false;

		String char_deb, char_cre;
		List<CalculPaie> list = null;

		Total_Credit = new Double(0);
		Total_Debit = new Double(0);
		list = centraservice.getListeBulletinCalculFromRhtcalcul(cdos, aamm, nbul.toString(), salarie.getNmat());

		for (CalculPaie calcul : list)
		{
			this.calcul = calcul;
			if (calcul.getMont().compareTo(new BigDecimal(0)) != 0)
			{
				if (!ven_ecr(salarie))
				{
					ParameterUtil.println("-------------------->Impossible de ventiler les ecriture pr le salarie " + salarie.getNmat());
					return false;
				}
			}
		}
		// System.out.println("Total Cr�dit = "+Total_Credit+"; total d�bit = "+Total_Debit);
		Total_Credit = ClsStringUtil.truncateToXDecimal(Total_Credit, nddd).doubleValue();
		Total_Debit = ClsStringUtil.truncateToXDecimal(Total_Debit, nddd).doubleValue();
		// System.out.println("Cr�dit "+Total_Credit+"; d�bit = "+Total_Debit);
		if (Math.abs(Total_Credit - Total_Debit) > 0.9)
		{
			wecart = Total_Debit - Total_Credit;
			char_deb = format_mt(Total_Debit, nddd, 13, true);
			char_cre = format_mt(Total_Credit, nddd, 13, true);
			mess = centraservice.chercherMessage(request, "ERR-90103", clang, salarie.getNmat(), char_deb, char_cre);
			globalUpdate._setEvolutionTraitement(request, mess + "##" + wecart, true);
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param ValeurToconv
	 *            valeur � convertir
	 * @param ndecimal
	 *            nombre de d�cimals apr�s la virgule
	 * @param long_ret
	 *            nombre de chiffres � retenir ds la cha�ne de caract�res
	 * @param boolAvec_zeros
	 *            indique si oui ou non les z�ros apr�s la virgule sont affich�s
	 * @return
	 */
	private String format_mt(Double ValeurToconv, int ndecimal, int long_ret, boolean boolAvec_zeros)
	{

		String mtf1 = null;
		String c20 = null;
		// BEGIN

		// IF Avec_Zero THEN
		// IF ndc = 3 THEN
		// mtf1 = StringUtil.oraLPad(LTRIM(TO_CHAR(NVL(mt,0), '999G999G999G990D999')),20,' ');
		// ELSIF ndc = 2 THEN
		// mtf1 = StringUtil.oraLPad(LTRIM(TO_CHAR(NVL(mt,0),'9G999G999G999G990D99')),20,' ');
		// ELSIF ndc = 1 THEN
		// mtf1 = StringUtil.oraLPad(LTRIM(TO_CHAR(NVL(mt,0),'99G999G999G999G990D9')),20,' ');
		// ELSE
		// mtf1 = StringUtil.oraLPad(LTRIM(TO_CHAR(NVL(mt,0), '999G999G999G999G990')),20,' ');
		// END IF;
		// ELSE
		// IF NOT NouZ(mt) THEN
		// IF ndc = 3 THEN
		// mtf1 = StringUtil.oraLPad(LTRIM(TO_CHAR(NVL(mt,0), '999G999G999G990D999')),20,' ');
		// ELSIF ndc = 2 THEN
		// mtf1 = StringUtil.oraLPad(LTRIM(TO_CHAR(NVL(mt,0),'9G999G999G999G990D99')),20,' ');
		// ELSIF ndc = 1 THEN
		// mtf1 = StringUtil.oraLPad(LTRIM(TO_CHAR(NVL(mt,0),'99G999G999G999G990D9')),20,' ');
		// ELSE
		// mtf1 = StringUtil.oraLPad(LTRIM(TO_CHAR(NVL(mt,0), '999G999G999G999G990')),20,' ');
		// END IF;
		// ELSE
		// mtf1 = StringUtil.oraLPad(' ',20,' ');
		// END IF;
		// END IF;
		if (!ClsObjectUtil.isNull(ValeurToconv))
		{
			if (boolAvec_zeros)
			{
				mtf1 = ClsStringUtil.double2string(ValeurToconv, 14, 15, ndecimal, ndecimal, "", "");
				if (mtf1.length() < 20)
				{
					char[] caTab = new char[20 - mtf1.length()];
					for (int i = 0; i < caTab.length; i++)
					{
						mtf1 = " " + mtf1;
					}
				}
				else
				{
					if (mtf1.length() > 20)
						mtf1 = mtf1.substring(0, 21);
				}
			}
			else
			{
				mtf1 = "                    ";// 20 espaces...
			}
		}
		else
		{
			mtf1 = "                    ";// 20 espaces...
		}
		// c20 = ' ';
		// c20 = SUBSTR(mtf1,20-long_ret+1);
		// RETURN c20;
		//
		c20 = " ";
		c20 = mtf1.substring(20 - long_ret);
		// END Format_Mt;
		return c20;
	}

	// ---------------------------------------------------------------------
	// -- Ventilation des ecritures comptables
	// ---------------------------------------------------------------------

	public boolean ven_ecr(Salarie salarie) throws Exception
	{
		ParameterUtil.println("Entr�e dans ven_ecr");
		if (StringUtils.equals(calcul.getRubq(), Rbq_NAP))
		{
			String txt = null;
			txt = "test";
		}
		char4 = StringUtils.isBlank(calcul.getRuba()) ? calcul.getRubq() : calcul.getRuba();
		rubrique = centraservice.getInfoRubriqueFromRhprubrique(this.getCdos(), char4);
		if (rubrique == null)
		{
			mess = centraservice.chercherMessage(request, "ERR-90104", clang, salarie.getNmat(), char4);
			globalUpdate._setEvolutionTraitement(request, mess, true);
			return false;
		}

		if (!"O".equalsIgnoreCase(rubrique.getComp()))
		{
			return true;
		}
		
		//on considere la rubq acompte commme le NAP.
		if(centralisionSurUneRubrique)
			rubrique.setPrbul(99);
		//Si centralisation d'une seule rubrique, alors ne traiter que cette rubrique la.
		if(centralisionSurUneRubrique && StringUtil.notEquals(rubrique.getCrub(), rubriqueAComptabiliser))
		{
			return true;
		}

		if (!StringUtils.equalsIgnoreCase(rubrique.getCrub(), Rbq_NAP))
		{
			fnom = centraservice.chercherEnNomenclature(this.getCdos(), 28, rubrique.getTypr(), 1);
			if (fnom.getValm() != null)
				Type_Rubq = fnom.getValm().intValue();
			else
			{
				mess = centraservice.chercherMessage(request, "ERR-90105", clang, calcul.getRubq(), rubrique.getTypr());
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
		}
		else Type_Rubq = 2;

		// --- Recherche agence de comptabilisation
		String colonne = "codesite";
		tableCodeSite = 0;
		Integer numColonne = -1;
		if (!paramDestinations.isEmpty())
		{
			for (ParamData fnom : paramDestinations)
			{
				if (fnom.getNume() == 5)
				{
					if (StringUtils.isNotBlank(fnom.getVall()))
						colonne = fnom.getVall();
					if (fnom.getValm() != null)
						tableCodeSite = fnom.getValm().intValue();
					if (fnom.getValt() != null)
						numColonne = fnom.getValt().intValue();
					break;
				}
			}
		}
		
		if (StringUtils.isBlank(code_agence))
		{
			String valeur = StringUtils.valueOf((char[]) MethodUtils.invokeExactMethod(salarie, "get" + StringUtils.capitalize(colonne.toLowerCase()), null));
			if (tableCodeSite != 0 && numColonne != -1)
			{
				fnom = centraservice.chercherEnNomenclature(cdos, tableCodeSite, valeur, numColonne);
				if (!ClsObjectUtil.isNull(fnom))
					valeur = fnom.getVall();
			}
			if(tableCodeSite == 0) tableCodeSite = 274;

			if (StringUtils.isBlank(valeur))
			{
				mess = "Sal. " + salarie.getNmat() + ". Rubrique " + rubrique.getCrub() + ". Aucun code agence";
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
			else agence_comptable = valeur;
		}
		else agence_comptable = code_agence;

		// Cas des pr�ts; on pourrait ici ajouter la condition rubrique.algo in (17,20)
		if (StringUtils.equalsIgnoreCase("O", rubrique.getCper()))
		{
			if (StringUtils.isBlank(calcul.getNprt()))
			{
				if (StringUtils.isBlank(salarie.getCcpt()))
				{
					mess = centraservice.chercherMessage(request, "ERR-90106", clang, salarie.getNmat(), calcul.getRubq());
					globalUpdate._setEvolutionTraitement(request, mess, true);
					return false;
				}
				Num_compte = salarie.getCcpt();
			}
			else
			{
				Num_compte = centraservice.getNcptFromRhtpretEnt(this.getCdos(), calcul.getNprt());
				if (StringUtils.isBlank(Num_compte))
				{
					if (StringUtils.isBlank(salarie.getCcpt()))
					{
						mess = centraservice.chercherMessage(request, "ERR-90106", clang, salarie.getNmat(), calcul.getRubq());
						globalUpdate._setEvolutionTraitement(request, mess, true);
						return false;
					}
					Num_compte = salarie.getCcpt();
				}
			}
			String Num_Compte_sav = Num_compte;

			// -- Il faut generer le pret sur l'agence du salarie
			// -- generation de 2 ecritures supplementaires pour equilibrer
			// -- le tout

			ParamData fnom;
			if(agenceToujoursSite) salarie.setGuic(salarie.getCodesite());
			if (gen_cpt_agence_dom && StringUtil.notEquals(agence_comptable, salarie.getGuic()))
			{
				// -- Lecture du compte de liaison dans la table 39

				if (StringUtil.in(salarie.getModp(), mode_virement))
				{
					fnom = centraservice.chercherEnNomenclature(cdos, tableCodeSite, salarie.getGuic(), 3);
				}
				else
				{
					fnom = centraservice.chercherEnNomenclature(cdos, tableCodeSite, salarie.getGuic(), 5);
				}
				if (StringUtils.isBlank(fnom.getVall()))
				{
					mess = "Sal. " + salarie.getNmat() + ". Rubrique " + rubrique.getCrub() + ". Manque cl� " + salarie.getGuic()
							+ " en Table "+tableCodeSite;
					globalUpdate._setEvolutionTraitement(request, mess, true);
					return false;
				}
				compte_liaison = fnom.getVall();

				// -- Generation ecriture de credit sur compte de liaison
				// -- (agence de comptabilisation renseignee ci-dessus)
				Num_compte = compte_liaison;
				sens = 'C';
				if (!ch_rub(salarie))
				{
					return false;
				}

				// -- Generation ecriture de debit sur compte de liaison
				// -- (agence de domiciliation)
				Long sav_prbul = rubrique.getPrbul();
				rubrique.setPrbul(1);
				agence_comptable = salarie.getGuic();
				
				if(agenceToujoursSite) agence_comptable = salarie.getCodesite();
				
				Num_compte = compte_liaison;
				sens = 'D';
				if (!ch_rub(salarie))
				{
					return false;
				}
				rubrique.setPrbul(sav_prbul);

				// -- L'autre ecriture sera generee juste apres sur le credit
				// -- du compte de pret sur l'agence de domiciliation du
				// -- salarie
				Num_compte = Num_Compte_sav;
				//	        
			}

			if (Type_Rubq == 1)
			{
				sens = 'D';
				if (!ch_rub(salarie))
				{
					return false;
				}
			}
			else
			{
				if (Type_Rubq == 2)
				{
					sens = 'C';
					if (!ch_rub(salarie))
					{
						return false;
					}
				}
				else
				{
					if (Type_Rubq == 3)
					{
						sens = 'D';
						if (!ch_rub(salarie))
						{
							return false;
						}

						sens = 'C';
						if (!ch_rub(salarie))
						{
							return false;
						}
					}
				}
			}
		}// END IF rubrique.cper = 'O' THEN
		else
		{
			
//			System.out.println("ancienneApprocheCentralisation:"+ancienneApprocheCentralisation);
			if(ancienneApprocheCentralisation==true)
			{
//				System.out.println("Rbq_NAP"+Rbq_NAP);
				if (Rbq_NAP.equalsIgnoreCase(rubrique.getCrub())==false)
				{
					if("BGFIBANKGE".equalsIgnoreCase(nomclient)){
						code_agence_comparaison = salarie.getCodesite();
						if (code_agence_comparaison.trim().equalsIgnoreCase(salarie.getGuic().trim()))
						{
							Num_compte = salarie.getComp();
							sens = 'C';
							//System.out.println("G�n�ration des �critures au cr�dit");
							if (!ch_rub(salarie))
							{
								return false;
							}
						}
						
					} else {
						if (!rech_cpt(rubrique.getCrub(), salarie))
						{
							return false;
						}
					}
				}
				else
				{
					ParameterUtil.println("------------GESTION DU NAP");
					// -- si centra des acomptes : on prend en compte uniquement la banque principal pour le virement
					char w_test_acq = '%';
					if (centralisionSurUneRubrique)
						w_test_acq = 'O';
					else w_test_acq = '%';
					String agence_comptable_sav = agence_comptable;
					String query = "From VirementSalarie where idEntreprise = '" + cdos + "' and nmat = '" + salarie.getNmat()
							+ "' and princ like '" + w_test_acq + "'";
					List<VirementSalarie> banques = centraservice.service.find(query);
					for (VirementSalarie wmbk : banques)
					{
						//System.out.println("Traitement de la banque "+wmbk.getBqag()+"-"+wmbk.getGuic()+"-"+wmbk.getComp());
						salarie.setGuic(wmbk.getGuic());
						if(agenceToujoursSite) salarie.setGuic(salarie.getCodesite());
						salarie.setComp(wmbk.getComp());
						salarie.setCle(wmbk.getCle());
						if (!centralisionSurUneRubrique && StringUtil.in(salarie.getModp(), mode_virement))
							calcul.setMont(NumberUtils.bdnvl(wmbk.getMntdvd(), 0));
	
						agence_comptable = agence_comptable_sav;
						// -- Agent avec compte dans meme agence que pour les charges
//						System.out.println("NOM CLIENT:"+nomclient);
						if("UTB".equalsIgnoreCase(nomclient) || "BGFIBANKGE".equalsIgnoreCase(nomclient)){
//							System.out.println("Agence comparaison:"+code_agence_comparaison);
//							System.out.println("Agence Guichet:"+salarie.getGuic());
							if (code_agence_comparaison.trim().equalsIgnoreCase(salarie.getGuic().trim()))
							{
								//System.out.println("Salari� dans la m�me agence "+agence_comptable);
								if (compte_avec_suffixe)
									Num_compte = salarie.getComp() + "/" + salarie.getCle();
								else
								{
									Num_compte = salarie.getComp();
								}
								sens = 'C';
								//System.out.println("G�n�ration des �critures au cr�dit");
								if (!ch_rub(salarie))
								{
									return false;
								}
		
								// -- MM 28/06/2004 prise en compte des acomptes : on genere la contrepartie au debit
								
								if (centralisionSurUneRubrique)
								{
									// -- Rubrique a comptabiliser sur compte ecran 7
									Type_Rubq = 1;
									if (!rech_cpt(rubrique.getCrub(), salarie))
									{
										return false;
									}
								}
		
							}
							else
							{
								
								//System.out.println("Agent dans une autre agence, "+salarie.getGuic()+", utilisation des comtpes de liaisons");
								// -- Lecture du compte de liaison dans la table 39
		
								if (StringUtil.in(salarie.getModp(), mode_virement))
								{
									fnom = centraservice.chercherEnNomenclature(cdos, tableCodeSite, salarie.getGuic(), 2);
								}
								else
								{
									fnom = centraservice.chercherEnNomenclature(cdos, tableCodeSite, salarie.getGuic(), 4);
								}
								if (StringUtils.isBlank(fnom.getVall()))
								{
									mess = "Sal. " + salarie.getNmat() + ". Rubrique " + rubrique.getCrub() + ". Manque cl� "
											+ salarie.getGuic() + " en Table "+tableCodeSite;
									globalUpdate._setEvolutionTraitement(request, mess, true);
									return false;
								}
								compte_liaison = fnom.getVall();
//								System.out.println("Cpte de liason:"+compte_liaison);
								// -- Generation ecriture de credit sur compte de liaison
								// -- (agence de comptabilisation renseignee ci-dessus)
		
								Num_compte = compte_liaison;
								sens = 'C';
								if (!ch_rub(salarie))
								{
									return false;
								}
								// -- Generation ecriture de debit sur compte de liaison
								// -- (agence de domiciliation)
								agence_comptable = salarie.getGuic();
								Num_compte = compte_liaison;
								sens = 'D';
								if (!ch_rub(salarie))
								{
									return false;
								}
		
								// -- Generation ecriture de credit sur compte du salarie
								// -- (agence de domiciliation)
								agence_comptable = salarie.getGuic();
								if (compte_avec_suffixe)
									Num_compte = salarie.getComp() + "/" + salarie.getCle();
								else Num_compte = salarie.getComp();
		
								sens = 'C';
								if (!ch_rub(salarie))
								{
									return false;
								}
		
								// -- MM 28/06/2004 prise en compte des acomptes : on genere la contrepartie au debit
								if (centralisionSurUneRubrique)
								{
									// -- MM 10/09/2004
									agence_comptable = agence_comptable_sav;
									// -- Rubrique a comptabiliser sur compte ecran 7
									Type_Rubq = 1;
									if (!rech_cpt(rubrique.getCrub(), salarie))
									{
										return false;
									}
								}
							}
							
							
						} else {
							if (StringUtils.equals(agence_comptable, salarie.getGuic()))
							{
								//System.out.println("Salari� dans la m�me agence "+agence_comptable);
								if (compte_avec_suffixe)
									Num_compte = salarie.getComp() + "/" + salarie.getCle();
								else
								{
									Num_compte = salarie.getComp();
								}
								sens = 'C';
								//System.out.println("G�n�ration des �critures au cr�dit");
								if (!ch_rub(salarie))
								{
									return false;
								}
		
								// -- MM 28/06/2004 prise en compte des acomptes : on genere la contrepartie au debit
								
								if (centralisionSurUneRubrique)
								{
									// -- Rubrique a comptabiliser sur compte ecran 7
									Type_Rubq = 1;
									if (!rech_cpt(rubrique.getCrub(), salarie))
									{
										return false;
									}
								}
		
							}
							else
							{
								
								//System.out.println("Agent dans une autre agence, "+salarie.getGuic()+", utilisation des comtpes de liaisons");
								// -- Lecture du compte de liaison dans la table 39
		
								if (StringUtil.in(salarie.getModp(), mode_virement))
								{
									fnom = centraservice.chercherEnNomenclature(cdos, tableCodeSite, salarie.getGuic(), 2);
								}
								else
								{
									fnom = centraservice.chercherEnNomenclature(cdos, tableCodeSite, salarie.getGuic(), 4);
								}
								if (StringUtils.isBlank(fnom.getVall()))
								{
									mess = "Sal. " + salarie.getNmat() + ". Rubrique " + rubrique.getCrub() + ". Manque cl� "
											+ salarie.getGuic() + " en Table "+tableCodeSite;
									globalUpdate._setEvolutionTraitement(request, mess, true);
									return false;
								}
								compte_liaison = fnom.getVall();
								// -- Generation ecriture de credit sur compte de liaison
								// -- (agence de comptabilisation renseignee ci-dessus)
		
								Num_compte = compte_liaison;
								sens = 'C';
								if (!ch_rub(salarie))
								{
									return false;
								}
								// -- Generation ecriture de debit sur compte de liaison
								// -- (agence de domiciliation)
								agence_comptable = salarie.getGuic();
								Num_compte = compte_liaison;
								sens = 'D';
								if (!ch_rub(salarie))
								{
									return false;
								}
		
								// -- Generation ecriture de credit sur compte du salarie
								// -- (agence de domiciliation)
								agence_comptable = salarie.getGuic();
								if (compte_avec_suffixe)
									Num_compte = salarie.getComp() + "/" + salarie.getCle();
								else Num_compte = salarie.getComp();
		
								sens = 'C';
								if (!ch_rub(salarie))
								{
									return false;
								}
		
								// -- MM 28/06/2004 prise en compte des acomptes : on genere la contrepartie au debit
								if (centralisionSurUneRubrique)
								{
									// -- MM 10/09/2004
									agence_comptable = agence_comptable_sav;
									// -- Rubrique a comptabiliser sur compte ecran 7
									Type_Rubq = 1;
									if (!rech_cpt(rubrique.getCrub(), salarie))
									{
										return false;
									}
								}
							}
						}
					}
//					if("BGFIBANKGE".equalsIgnoreCase(nomclient)) Destination[1] = salarie.getGuic();
				}
			}
		}

		return true;
	}// END ven_ecr;

	Map<String, String> libelleRubMap = new HashMap<String, String>();
	
	public boolean ch_rub(Salarie salarie) throws Exception
	{
		ParameterUtil.println("Entr�e dans ch_rub");
		
		wint = new InterfComptable();
		wpampai = new MouvCptPaie();

		Cpt_Type = ' ';

		if (!StringUtils.equals("O", StringUtil.nvl(rubrique.getCper(), "N")))
		{
			if(StringUtil.notEquals(Rbq_NAP, rubrique.getCrub()))
			{
				if (!cpt_cherche_lettre(salarie, mess))
				{
					return false;
				}
			}
		}

		Libelle_Ecriture = StringUtils.substring(rubrique.getLrub(), 0, 22);

		wint.setReflet(null);

		Mnt_Ecriture = new Double(0);

		if (sens == 'D')
		{
			ch_deb();
		}
		else
		{
			if (sens == 'C')
			{
				ch_cre();
			}
			else
			{
				mess = centraservice.chercherMessage(request, "ERR-90124", clang, salarie.getNmat(), calcul.getRubq());
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
		}

		if (!chargerEtablissement(salarie, rubrique.getCrub()))
		{
			return false;
		}

		// ----DEBUT RHTMPAI
//		if(Num_compte.endsWith("9660") && StringUtils.isNotBlank(code_agence_dech_tiers) && StringUtil.notEquals(Rbq_NAP, rubrique.getCrub()))
//			wpampai.setAge(code_agence_dech_tiers); // -- code agence charge et tiers
		if("UTB".equalsIgnoreCase(nomclient))  wpampai.setAge(code_agence_dech_tiers);
		else wpampai.setAge(agence_comptable); // -- code agence
		wpampai.setDev(code_dev); // -- Code devise
		wpampai.setCha(StringUtil.oraLPad(" ", 10, " ")); // -- Zone libre
	
		int tailleCompte = 11;
		ParamData fnom3 = centraservice.chercherEnNomenclature(cdos, 99, "INT-BANK-3", 1);
		if(fnom3 !=null && fnom3.getValm()!=null && fnom3.getValm().intValue()!=0)  tailleCompte = fnom3.getValm().intValue();
		
		if (compte_avec_suffixe)
		{
			ClsCodeLibelle lib = recherche_compte(Num_compte, wpampai.getSuf());
			wpampai.setNcp(StringUtil.oraSubstring(lib.getCode(), 1, tailleCompte));
			wpampai.setSuf(lib.getLibelle());
		}
		else
		{
			
			wpampai.setNcp(StringUtil.oraSubstring(Num_compte, 1, tailleCompte));
			wpampai.setSuf("");
			
			ParamData fnom2 = centraservice.chercherEnNomenclature(cdos, 99, "INT-BANK-2", 2);
			if(fnom2 !=null && StringUtils.isNotEmpty(fnom2.getVall()))  wpampai.setSuf(fnom2.getVall());
			
		}
		wpampai.setOpe(code_operation);
		wpampai.setMvt(StringUtil.oraLPad(" ", 6, " ")); // -- Zone libre
		wpampai.setRgp(StringUtil.oraLPad(" ", 3, " ")); // -- Zone libre
		wpampai.setEve(StringUtil.oraLPad(" ", 6, " ")); // -- Zone libre
		wpampai.setClc(StringUtil.oraLPad(" ", 2, " ")); // -- Zone libre
		wpampai.setUti("AUTO");

		wpampai.setSer(StringUtil.oraLPad(StringUtil.nvl(salarie.getEqui(), " "), 4, " ")); // -- Zone libre
		wpampai.setDco(Date_Comptable.getDate());
		// wpampai.setDva (date_valeur);
		wpampai.setDva(Date_Comptable.getDate());

		wpampai.setMon(ClsStringUtil.truncateToXDecimal(new BigDecimal(Mnt_Ecriture), nddd));
		wpampai.setSen(sens + "");
		
		if(ancienneApprocheCentralisation)
		{
			String lib = "";
			if("BGFIBANKGE".equalsIgnoreCase(nomclient)) lib = rubrique.getLrub() + " " + rech_mois_3x(StringUtil.oraSubstring(aamm, 5, 2)) + " " + StringUtil.oraSubstring(aamm, 1, 4);
			else{
				if (StringUtils.equals(rubrique.getCrub(), Rbq_NAP))
					lib = libelle_NAP + " " + rech_mois_3x(StringUtil.oraSubstring(aamm, 5, 2)).toUpperCase() + " " + StringUtil.oraSubstring(aamm, 1, 4);				
				else
				{ 
					//String desc ="Bul "+ nbul + " " + rech_mois_3x(StringUtil.oraSubstring(aamm, 5, 2)) + ",annee " + StringUtil.oraSubstring(aamm, 1, 4);
					String desc = " Bul "+ nbul + " "+rech_mois_3x(StringUtil.oraSubstring(aamm, 5, 2)) + " " + StringUtil.oraSubstring(aamm, 1, 4);	
					lib = "Paie:" ;
					if(StringUtils.isNotBlank(libelle_GLOBAL)) lib = libelle_GLOBAL;
					if(libelle_Egal_LibelleRubrique) lib = StringUtils.substring(rubrique.getLrub(), 0, 22);
					if(libelleRubMap.containsKey(rubrique.getCrub()) && StringUtils.isNotBlank(libelleRubMap.get(rubrique.getCrub())))
						lib = libelleRubMap.get(rubrique.getCrub());
					//On cherche en table 400, si la rubrique concern� doit avoir un libell� particulier
					
					lib = lib + desc;
					lib = StringUtil.oraSubstring(lib, 1, 30);
				}
			}
			wpampai.setLib(lib);
			//else wpampai.setLib("Paie:Bul " + nbul + " " + rech_mois_3x(StringUtil.oraSubstring(aamm, 5, 2)).toUpperCase() + StringUtil.oraSubstring(aamm, 1, 4));
		}
		else
			wpampai.setLib(StringUtils.substring(rubrique.getLrub(), 0, 22));
		
		wpampai.setExo(" ");
		// wpampai.setPie (cdos+StringUtil.oraSubstring(aamm,5,2)+StringUtil.oraSubstring(aamm,3,2)+nbul);
		wpampai.setPie(Num_Piece);
		if (centra_par_cpt)
			wpampai.setRlet(wpampai.getPie());
		else wpampai.setRlet(salarie.getNmat());

		wpampai.setDes1(StringUtil.oraLPad(" ", 4, " ")); // -- Zone libre
		wpampai.setDes2(StringUtil.oraLPad(" ", 4, " ")); // -- Zone libre
		wpampai.setDes3(StringUtil.oraLPad(" ", 4, " ")); // -- Zone libre
		wpampai.setDes4(StringUtil.oraLPad(" ", 4, " ")); // -- Zone libre
		wpampai.setDes5(StringUtil.oraLPad(" ", 4, " ")); // -- Zone libre
		wpampai.setUtf(StringUtil.oraLPad(" ", 4, " ")); // -- Zone libre
		wpampai.setUta(StringUtil.oraLPad(" ", 4, " ")); // -- Zone libre
		wpampai.setTau(BigDecimal.ZERO); // -- Zone libre
		wpampai.setDin(null); // -- Zone libre
		wpampai.setTpr(StringUtil.oraLPad(" ", 1, " ")); // -- Zone libre
		wpampai.setNpr(Long.valueOf(0)); // -- Zone libre
		wpampai.setNcc(StringUtil.oraLPad(" ", 11, " ")); // -- Zone libre
		wpampai.setSuc(StringUtil.oraLPad(" ", 2, " ")); // -- Zone libre
		wpampai.setEsi(StringUtil.oraLPad(" ", 1, " ")); // -- Zone libre
		wpampai.setImp(StringUtil.oraLPad(" ", 1, " ")); // -- Zone libre
		wpampai.setCta(StringUtil.oraLPad(" ", 1, " ")); // -- Zone libre
		wpampai.setMar(StringUtil.oraLPad(" ", 1, " ")); // -- Zone libre
		wpampai.setDech(null); // -- Zone libre
		wpampai.setAgsa(wpampai.getAge());
		wpampai.setAgem(StringUtil.oraLPad(" ", 5, " ")); // -- Zone libre
		wpampai.setAgde(StringUtil.oraLPad(" ", 5, " ")); // -- Zone libre
		wpampai.setDevc(wpampai.getDev());
		wpampai.setMctv(wpampai.getMon());
		wpampai.setPieo(wpampai.getPie());
		wpampai.setIden(StringUtil.oraLPad(" ", 6, " ")); // -- Zone libre
		wpampai.setNoseq(0); // -- Zone libre

		wint.setIdEntreprise(new Integer(cdos));
		wint.setCodets(Code_Etablissement);
		wint.setCodjou(Code_Journal);
		wint.setNumcpt(wpampai.getNcp());
		wint.setNumtie(wpampai.getSuf());
		wint.setNumpce(wpampai.getPie());
		wint.setDatcpt(wpampai.getDco());
		wint.setDatech(wpampai.getDech());
		wint.setDatpce(wpampai.getDin());
		wint.setDevpce(wpampai.getDev());
		wint.setQuantite(null);
		wint.setSens(sens + "");
		wint.setPceMt(ClsStringUtil.truncateToXDecimal(new BigDecimal(Mnt_Ecriture), nddd));
//		if("BGFIBANKGE".equalsIgnoreCase(nomclient)){
//			if(Rbq_NAP.equalsIgnoreCase(rubrique.getCrub())==true) wint.setCoddes1(salarie.getGuic());
//			else wint.setCoddes1(Destination[1]);
//		} else 
		wint.setCoddes1(Destination[1]);
		wint.setCoddes1(Destination[1]);
		wint.setCoddes2(Destination[2]);
		wint.setCoddes3(wpampai.getOpe());
		wint.setCoddes4(wpampai.getDev());
//		if("BGFIBANKGE".equalsIgnoreCase(nomclient)) wint.setCoddes5(salarie.getGuic());
//		else 
		wint.setCoddes5(wpampai.getAge());
		wint.setCoddes6(salarie.getNmat());
//		wint.setCoddes7(salarie.getSana());
		wint.setCoddes7(rubrique.getCrub());
		wint.setCoddes8(Destination[8]);
		wint.setCoddes9(Destination[9]);
		wint.setLibecr(wpampai.getLib());
		wint.setCodtre(Type_Num_Tiers);// Permet de savoir si le num tiers est le matricule, ou un nivXX
		wint.setCodabr(Libelle_Abrege);
		wint.setCoderr(null);
		wint.setReflet(wpampai.getRlet());
		wint.setCreationDate(Instant.now());
		wint.setLastModifiedDate(Instant.now());
		wint.setCoduti(wpampai.getUti());

		try
		{
			ClsCentralisationBank.compteur += 1;

			wint.setLiberr(String.valueOf(ClsCentralisationBank.compteur));
			session.save(wint);

			wpampai.setIden(String.valueOf(ClsCentralisationBank.compteur));
			session.save(wpampai);

			if (ClsCentralisationBank.compteur % 20 == 0)
			{
				session.flush();
				session.clear();
			}

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			mess = centraservice.chercherMessage(request, "ERR-90125", clang, salarie.getNmat(), calcul.getRubq());
			globalUpdate._setEvolutionTraitement(request, mess, true);
			logMessage(ex.getMessage());
			return false;
		}
		return true;
	}

	private ClsCodeLibelle recherche_compte(String wnumcpt, String suffixe)
	{
		int i;
		boolean fini = false;
		String w_ncp;
		String w_suf;

		w_suf = "  ";

		// -- Recherche du "/" dans le No de compte
		i = 1;
		while (i <= 15 && !fini)
		{
			if (StringUtils.equals(StringUtil.oraSubstring(wnumcpt, i, 1), "/"))
				fini = true;
			else i = i + 1;
		}
		if (fini)
		{
			w_ncp = StringUtil.oraSubstring(wnumcpt, 1, i - 1);
			if (i <= 13)
				w_suf = StringUtil.oraSubstring(wnumcpt, i + 1, i + 2);

		}
		else w_ncp = wnumcpt;

		// -- suffixe OUT
		suffixe = w_suf;
		//
		return new ClsCodeLibelle(w_ncp, suffixe);
	}

	// ---------------------------------------------------------------------
	// -- Fonction retournant le libelle du mois sur 3 caracteres
	// ---------------------------------------------------------------------
	private String rech_mois_3x(String mois)

	{
		String w_mois;

		w_mois = mois;
		if (StringUtils.equals(w_mois, "01"))
			return "Janvier";
		else if (StringUtils.equals(w_mois, "02"))
			return "Fevrier";
		else if (StringUtils.equals(w_mois, "03"))
			return "Mars";
		else if (StringUtils.equals(w_mois, "04"))
			return "Avril";
		else if (StringUtils.equals(w_mois, "05"))
			return "Mai";
		else if (StringUtils.equals(w_mois, "06"))
			return "Juin";
		else if (StringUtils.equals(w_mois, "07"))
			return "Juillet";
		else if (StringUtils.equals(w_mois, "08"))
			return "Aout";
		else if (StringUtils.equals(w_mois, "09"))
			return "Septembre";
		else if (StringUtils.equals(w_mois, "10"))
			return "Octobre";
		else if (StringUtils.equals(w_mois, "11"))
			return "Novembre";
		else if (StringUtils.equals(w_mois, "12"))
			return "Decembre";
		return "ERR";
	}

	public void refreshConnexion()
	{
		String query = "Update Evlang set clang=clang where clang='XX'";
		session.createSQLQuery(query).executeUpdate();
	}
	
	private String getCompteContainingCcptOrSana(String currentNumCompte, Salarie salarie)
	{
		//Le compte peut �tre PERS
		//Le compte peut �tre 2546+PERS6+1452 , le r�sultat etant 2546 concaten� aux 6 premiers caract�res de CCPT concaten� � 1452
		String result = currentNumCompte;
		if(StringUtil.indexOf(currentNumCompte, COMPTE_PERSONNEL_AGENT) != -1)
		{
			result = StringUtils.EMPTY;
			String[] split = StringUtils.splitPreserveAllTokens(currentNumCompte,"+");
			for(String str : split)
			{
				if(StringUtil.indexOf(str, COMPTE_PERSONNEL_AGENT) != -1)
				{
					String lg = "100";
					if(str.length()>COMPTE_PERSONNEL_AGENT.length()) lg = StringUtil.oraSubstring(str, COMPTE_PERSONNEL_AGENT.length()+1, 10);
					if(StringUtils.isBlank(lg)) lg = "100";
					if(NumberUtils.isInteger(lg))
					{
						str = StringUtil.oraSubstring(salarie.getCcpt(), 1, Integer.valueOf(lg));	
					}
					else
						str = salarie.getCcpt();
				}
				result += str;
			}
		}
		else if(StringUtil.indexOf(currentNumCompte, COMMENTAIRE_AGENT) != -1)
		{
			//Le compte peut �tre SANA
			//Le compte peut �tre 2546+SANA6+1452 , le r�sultat etant 2546 concaten� aux 6 premiers caract�res de SANA concaten� � 1452
			
			result = StringUtils.EMPTY;
			String[] split = StringUtils.splitPreserveAllTokens(currentNumCompte,"+");
			for(String str : split)
			{
				if(StringUtil.indexOf(str, COMMENTAIRE_AGENT) != -1)
				{
					String lg = "100";
					if(str.length()>COMMENTAIRE_AGENT.length()) lg = StringUtil.oraSubstring(str, COMMENTAIRE_AGENT.length()+1, 10);
					if(StringUtils.isBlank(lg)) lg = "100";
					if(NumberUtils.isInteger(lg))
					{
						str = StringUtil.oraSubstring(salarie.getComm(), 1, Integer.valueOf(lg));	
					}
					else
						str = salarie.getComm();
				}
				result += str;
			}
		}
		
		return result;
	}
	
	public boolean rech_cpt(String strCrub, Salarie salarie) throws Exception
	{

		ParameterUtil.println("Entr�e dans rech_cpt");

		if (NouB(rubrique.getDe01()) && NouB(rubrique.getCr01()) &&!nouvelleCompta)
		{
			mess = " Sal. " + salarie.getNmat() + " Rubrique " + calcul.getRubq() + "  Aucun no compte de comptabilite (Ecran 7)";
			globalUpdate._setEvolutionTraitement(request, mess, true);
			return false;
		}

		fnom = centraservice.chercherEnNomenclature(cdos, 6, salarie.getGrad(), 1);
		if (fnom.getValm() == null)
		{
			mess = centraservice.chercherMessage(request, "ERR-90109", clang, salarie.getNmat(), salarie.getGrad());
			globalUpdate._setEvolutionTraitement(request, mess, true);
			return false;
		}

		ind_cre = fnom.getValm().intValue() + 1;
		if (ind_cre < 1 || ind_cre > 5)
		{
			mess = centraservice.chercherMessage(request, "ERR-90110", clang, salarie.getNmat(), salarie.getGrad());
			globalUpdate._setEvolutionTraitement(request, mess, true);
			return false;
		}

		boolean booltyper = false;
		booltyper = (Type_Rubq == 1 || Type_Rubq == 3);
		if (booltyper)
		{
			sens = 'D';

			fnom = centraservice.chercherEnNomenclature(cdos, 4, salarie.getNato(), 1);
			if (fnom.getValm() == null)
			{
				mess = centraservice.chercherMessage(request, "ERR-90111", clang, salarie.getNmat(), salarie.getNato());
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}

			ind_deb = ((ind_cre - 1) * 4) + 1 + fnom.getValm().intValue();

			if (ind_deb < 1 || ind_deb > 20)
			{
				mess = centraservice.chercherMessage(request, "ERR-90112", clang, salarie.getNmat(), salarie.getGrad(), salarie.getNato());
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
			if(nouvelleCompta)
				Num_compte = centraservice.getCompteRubriqueIndDeb(salarie,rubrique, ind_deb);
			else
				Num_compte = centraservice.getCompteRubriqueIndDeb(rubrique, ind_deb);
			if (NouB(Num_compte))
			{
				mess = centraservice.chercherMessage(request, "ERR-90113", clang, calcul.getRubq(), ClsStringUtil
						.int2string(ind_deb, 1, 2, "", ""));
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
		
			Num_compte = getCompteContainingCcptOrSana(Num_compte, salarie);
			
			
			boolean comptebancaire = false;
			if (Num_compte.compareTo(COMPTE_BANCAIRE_AGENT) == 0)
			{
				Num_compte = salarie.getComp();
				comptebancaire = true;
			}

			if (NouB(Num_compte))
			{
				mess = centraservice.chercherMessage(request, "ERR-90117", clang, calcul.getRubq(), ClsStringUtil.int2string(ind_cre, 1, 2, "", ""));
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
			
			if (comptebancaire)
			{
				if (!this.gestionCompeBancaireAgent(salarie))
					return false;
			}
			else
			{
				if (!ch_rub(salarie)) { return false; }
			}

		}// if(Type_Rubq == 1 | Type_Rubq == 3){*

		booltyper = (Type_Rubq == 2 || Type_Rubq == 3);
		if (booltyper)
		{
			sens = 'C';
			if(nouvelleCompta)
				Num_compte = centraservice.getCompteRubriqueIndCre(salarie,rubrique, ind_cre);
			else
				Num_compte = centraservice.getCompteRubriqueIndCre(rubrique, ind_cre);

			if (NouB(Num_compte))
			{
				mess = centraservice.chercherMessage(request, "ERR-90117", clang, calcul.getRubq(), ClsStringUtil
						.int2string(ind_cre, 1, 2, "", ""));
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}

			Num_compte = getCompteContainingCcptOrSana(Num_compte, salarie);
			
			boolean comptebancaire = false;
			if (Num_compte.compareTo(COMPTE_BANCAIRE_AGENT) == 0)
			{
				Num_compte = salarie.getComp();
				comptebancaire = true;
			}

			if (NouB(Num_compte))
			{
				mess = centraservice.chercherMessage(request, "ERR-90117", clang, calcul.getRubq(), ClsStringUtil.int2string(ind_cre, 1, 2, "", ""));
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
			
			if (comptebancaire)
			{
				if (!this.gestionCompeBancaireAgent(salarie))
					return false;
			}
			else
			{
				if (!ch_rub(salarie)) { return false; }
			}

		}// if(Type_Rubq == 2 | Type_Rubq == 3)

		return true;
	}
	
	private boolean gestionCompeBancaireAgent(Salarie salarie) throws Exception
	{
		String agence_comptable_sav = agence_comptable;
		
		if (StringUtils.equals(agence_comptable, salarie.getGuic()))
		{
			if (compte_avec_suffixe)
				Num_compte = salarie.getComp() + "/" + salarie.getCle();
			else
			{
				Num_compte = salarie.getComp();
			}
			if (!ch_rub(salarie))
			{
				return false;
			}
		}
		else
		{
			if (StringUtil.in(salarie.getModp(), mode_virement))
			{
				fnom = centraservice.chercherEnNomenclature(cdos, tableCodeSite, salarie.getGuic(), 2);
			}
			else
			{
				fnom = centraservice.chercherEnNomenclature(cdos, tableCodeSite, salarie.getGuic(), 4);
			}
			if (StringUtils.isBlank(fnom.getVall()))
			{
				mess = "Sal. " + salarie.getNmat() + ". Rubrique " + rubrique.getCrub() + ". Manque cl� " + salarie.getGuic() + " en Table "+tableCodeSite;
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
			compte_liaison = fnom.getVall();
			Num_compte = compte_liaison;
			if (!ch_rub(salarie))
			{
				return false;
			}
			
			agence_comptable = salarie.getGuic();
			Num_compte = compte_liaison;
			if(sens == 'D') sens='C'; else sens='D';
			if (!ch_rub(salarie))
			{
				return false;
			}

			if(sens == 'D') sens='C'; else sens='D';
			agence_comptable = salarie.getGuic();
			if (compte_avec_suffixe)
				Num_compte = salarie.getComp() + "/" + salarie.getCle();
			else Num_compte = salarie.getComp();

			if (!ch_rub(salarie))
			{
				return false;
			}
		}
		agence_comptable = agence_comptable_sav;
		return true;
	}

	public boolean _getCompteRubrique(String strCrub, Salarie salarie) throws Exception
	{
		if (NouB(rubrique.getDe01()) && NouB(rubrique.getCr01()) && !nouvelleCompta)
		{
			mess = " Sal. " + salarie.getNmat() + " Rubrique " + calcul.getRubq();
			globalUpdate._setEvolutionTraitement(request, mess, true);
			return false;
		}

		fnom = centraservice.chercherEnNomenclature(cdos, 6, salarie.getGrad(), 1);
		if (fnom.getValm() == null)
		{
			mess = centraservice.chercherMessage(request, "ERR-90109", clang, salarie.getNmat(), salarie.getGrad());
			globalUpdate._setEvolutionTraitement(request, mess, true);
			return false;
		}

		ind_cre = fnom.getValm().intValue() + 1;
		if (ind_cre < 1 || ind_cre > 5)
		{
			mess = centraservice.chercherMessage(request, "ERR-90110", clang, salarie.getNmat(), salarie.getGrad());
			globalUpdate._setEvolutionTraitement(request, mess, true);
			return false;
		}
		boolean booltyper = false;
		booltyper = (Type_Rubq == 1 || Type_Rubq == 3);
		if (booltyper)
		{
			sens = 'D';
			fnom = centraservice.chercherEnNomenclature(cdos, 4, salarie.getNato(), 1);
			if (fnom.getValm() == null)
			{
				mess = centraservice.chercherMessage(request, "ERR-90111", clang, salarie.getNmat(), salarie.getNato());
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}

			ind_deb = ((ind_cre - 1) * 4) + 1 + fnom.getValm().intValue();

			if (ind_deb < 1 || ind_deb > 20)
			{
				mess = centraservice.chercherMessage(request, "ERR-90112", clang, salarie.getNmat(), salarie.getGrad(), salarie.getNato());
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}

			if(nouvelleCompta)
				Num_compte = centraservice.getCompteRubriqueIndDeb(salarie,rubrique, ind_deb);
			else
				Num_compte = centraservice.getCompteRubriqueIndDeb(rubrique, ind_deb);

			if (NouB(Num_compte))
			{
				mess = centraservice.chercherMessage(request, "ERR-90113", clang, calcul.getRubq(), ClsStringUtil
						.int2string(ind_deb, 1, 2, "", ""));
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
			
			Num_compte = getCompteContainingCcptOrSana(Num_compte, salarie);

			if (NouB(Num_compte))
			{
				mess = centraservice.chercherMessage(request, "ERR-90114", clang, salarie.getNmat());
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}

		}

		booltyper = (Type_Rubq == 2 || Type_Rubq == 3);
		if (booltyper)
		{
			sens = 'C';

			if(nouvelleCompta)
				Num_compte = centraservice.getCompteRubriqueIndCre(salarie,rubrique, ind_cre);
			else
				Num_compte = centraservice.getCompteRubriqueIndCre(rubrique, ind_cre);

			if (NouB(Num_compte))
			{
				mess = centraservice.chercherMessage(request, "ERR-90117", clang, calcul.getRubq(), ClsStringUtil
						.int2string(ind_cre, 1, 2, "", ""));
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}

			Num_compte = getCompteContainingCcptOrSana(Num_compte, salarie);
			
			if (NouB(Num_compte))
			{
				mess = centraservice.chercherMessage(request, "ERR-90114", clang, salarie.getNmat());
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}

		}
		return true;
	}

	public void ch_deb()
	{
		ParameterUtil.println("Entr�e dans ch_deb");

		if (rubrique.getCrub().compareTo(Rbq_BRUT) == 0 || Type_Rubq == 3)
		{
			Total_Debit = Total_Debit + calcul.getMont().doubleValue();
			Mnt_Ecriture = Mnt_Ecriture + calcul.getMont().doubleValue();
		}
		else
		{
			if (rubrique.getPrbul() == 3 || rubrique.getPrbul() == 4)
			{
				Total_Debit = Total_Debit - calcul.getMont().doubleValue();
				Mnt_Ecriture = Mnt_Ecriture - calcul.getMont().doubleValue();
			}
			else
			{
				Total_Debit = Total_Debit + calcul.getMont().doubleValue();
				Mnt_Ecriture = Mnt_Ecriture + calcul.getMont().doubleValue();
			}
		}
	}

	public void ch_cre()
	{
		ParameterUtil.println("Entr�e dans ch_cre");
		if (Type_Rubq == 3)
		{
			Total_Credit = Total_Credit + calcul.getMont().doubleValue();
			Mnt_Ecriture = Mnt_Ecriture + calcul.getMont().doubleValue();
		}
		else
		{
			if (rubrique.getPrbul() == 1 || rubrique.getPrbul() == 2)
			{
				Total_Credit = Total_Credit - calcul.getMont().doubleValue();
				Mnt_Ecriture = Mnt_Ecriture - calcul.getMont().doubleValue();
			}
			else
			{
				Total_Credit = Total_Credit + calcul.getMont().doubleValue();
				Mnt_Ecriture = Mnt_Ecriture + calcul.getMont().doubleValue();
			}
		}
	}

	// -------------------------------------------------------------------------------
	// -- Recherche d'une lettre dans le No de compte
	// -------------------------------------------------------------------------------

	public boolean cpt_cherche_lettre(Salarie salarie, String mess)
	{
		// -- Si on insere 'Mn' dans le numero de compte, on concatene les caracteres
		// -- du debut du compte avec les n derniers caracteres du matricule.
		// -- Si on insere 'N1' dans le numero de compte, on concatene les caracteres
		// -- du debut du compte avec niveau 1 du salarie.
		// -- Si on insere 'N2' dans le numero de compte, on concatene les caracteres
		// -- du debut du compte avec niveau 2 du salarie.
		// -- Si on insere 'N3' dans le numero de compte, on concatene les caracteres
		// -- du debut du compte avec niveau 3 du salarie.
		// -- Si on insere 'Vn' dans le numero de compte, on concatene les caracteres
		// -- du debut du compte avec les n derniers caracteres du code ville declaration indique en l3. n=Max.6.
		// -- Si on insere 'S1' dans le numero de compte, on genere le niveau 3 du salari� dans la zone destination 1
		// -- Si on insere 'S2' dans le numero de compte, on genere le section analytique du salari� dans la zone destination 1
		// -- Exemple: Salarie 100253, niv1='010', niv2='015', niv3='4852', vildec='0010',Sana='10020'
		// -- Compte='4200M4' donnera '42000253'
		// -- Compte='6500N1' donnera '65000010'
		// -- Compte='6500N2' donnera '65000015'
		// -- Compte='6500N3' donnera '650004582'
		// -- Compte='4200V4' donnera '42000010'
		// -- Compte='6500S1' donnera compte='6500' Destination 1='4852'
		// -- Compte='6500S2' donnera compte='6500' Destination 1='10020'
		// -------------------------------------------------------------------------------
		ParameterUtil.println("Entr�e dans cpt_cherche_lettre");
		Num_Tiers = StringUtils.EMPTY;
		Type_Num_Tiers = StringUtils.EMPTY;
		int pos, nb_rec = 0;
		String lib = null;

		// -- Concatenation du Matricule et du niveau 1
		pos = (Num_compte.indexOf("MN1") != -1) ? Num_compte.indexOf("MN1") : -1;
		if (pos != -1)
		{
			Num_compte = StringUtils.substring(Num_compte, 0, pos);
			Num_Tiers = StringUtils.right(salarie.getNmat(), 4);
			Type_Num_Tiers = "MN";
			if (blib)
			{
				fnom = centraservice.chercherEnNomenclature(cdos, 1, salarie.getNiv1(), llib);
				if (fnom != null && StringUtils.isNotBlank(fnom.getVall()))
					Num_Tiers += StringUtils.right(fnom.getVall(), 4);
				else
				{
					mess = centraservice.chercherMessage(request, "Cr�er LIB" + llib + " sur cl� " + salarie.getNiv1() + " en T1", clang);
					globalUpdate._setEvolutionTraitement(request, mess, true);
					return false;
				}

			}
			else Num_Tiers += StringUtils.right(salarie.getNiv1(), 4);

			// compte ressemblant � XXXX+MN1
			pos = (Num_compte.indexOf("+") != -1) ? Num_compte.indexOf("+") : -1;
			if (pos != -1)
			{
				Num_compte = StringUtils.substring(StringUtils.substring(Num_compte, 0, pos) + Num_Tiers, 0, 11);
				Num_Tiers = StringUtils.EMPTY;
			}
			return true;
		}

		// -- Concatenation du Matricule et du niveau 2
		pos = (Num_compte.indexOf("MN2") != -1) ? Num_compte.indexOf("MN2") : -1;
		if (pos != -1)
		{
			Num_compte = StringUtils.substring(Num_compte, 0, pos);
			Num_Tiers = StringUtils.right(salarie.getNmat(), 4);
			Type_Num_Tiers = "MN";
			if (blib)
			{
				fnom = centraservice.chercherEnNomenclature(cdos, 2, salarie.getNiv2(), llib);
				if (fnom != null && StringUtils.isNotBlank(fnom.getVall()))
					Num_Tiers += StringUtils.right(fnom.getVall(), 4);
				else
				{
					mess = centraservice.chercherMessage(request, "Cr�er LIB" + llib + " sur cl� " + salarie.getNiv2() + " en T2", clang);
					globalUpdate._setEvolutionTraitement(request, mess, true);
					return false;
				}

			}
			else Num_Tiers += StringUtils.right(salarie.getNiv2(), 4);

			// compte ressemblant � XXXX+MN2
			pos = (Num_compte.indexOf("+") != -1) ? Num_compte.indexOf("+") : -1;
			if (pos != -1)
			{
				Num_compte = StringUtils.substring(StringUtils.substring(Num_compte, 0, pos) + Num_Tiers, 0, 11);
				Num_Tiers = StringUtils.EMPTY;
			}
			return true;
		}

		// -- Concatenation du Matricule et du niveau 3
		pos = (Num_compte.indexOf("MN3") != -1) ? Num_compte.indexOf("MN3") : -1;
		if (pos != -1)
		{
			Num_compte = StringUtils.substring(Num_compte, 0, pos);
			Num_Tiers = StringUtils.right(salarie.getNmat(), 4);
			Type_Num_Tiers = "MN";
			if (blib)
			{
				fnom = centraservice.chercherEnNomenclature(cdos, 3, salarie.getNiv3(), llib);
				if (fnom != null && StringUtils.isNotBlank(fnom.getVall()))
					Num_Tiers += StringUtils.right(fnom.getVall(), 4);
				else
				{
					mess = centraservice.chercherMessage(request, "Cr�er LIB" + llib + " sur cl� " + salarie.getNiv3() + " en T3", clang);
					globalUpdate._setEvolutionTraitement(request, mess, true);
					return false;
				}

			}
			else Num_Tiers += StringUtils.right(salarie.getNiv3(), 4);

			// compte ressemblant � XXXX+MN3
			pos = (Num_compte.indexOf("+") != -1) ? Num_compte.indexOf("+") : -1;
			if (pos != -1)
			{
				Num_compte = StringUtils.substring(StringUtils.substring(Num_compte, 0, pos) + Num_Tiers, 0, 11);
				Num_Tiers = StringUtils.EMPTY;
			}
			return true;
		}

		// Contatenation du matricule uniquement
		pos = StringUtil.indexOf(Num_compte, 'M');
		if (pos != -1)
		{
			Num_compte = StringUtil.oraSubstring(Num_compte, 1, pos);
			Num_Tiers = StringUtil.oraSubstring(salarie.getNmat(), 1, 6);
			Type_Num_Tiers = "M";
			// compte ressemblant � XXXX+M
			pos = (Num_compte.indexOf("+") != -1) ? Num_compte.indexOf("+") : -1;
			if (pos != -1)
			{
				Num_compte = StringUtils.substring(StringUtils.substring(Num_compte, 0, pos) + Num_Tiers, 0, 11);
				Num_Tiers = StringUtils.EMPTY;
				Type_Num_Tiers = "";
			}
			return true;
		}

		// -- Concatenation du niveau 1
		pos = (Num_compte.indexOf("N1") != -1) ? Num_compte.indexOf("N1") : -1;
		if (pos != -1)
		{
			Num_compte = StringUtils.substring(Num_compte, 0, pos);
			Type_Num_Tiers = "N1";
			if (blib)
			{
				fnom = centraservice.chercherEnNomenclature(cdos, 1, salarie.getNiv1(), llib);
				if (fnom != null && StringUtils.isNotBlank(fnom.getVall()))
					Num_Tiers = StringUtils.substring(fnom.getVall(), 0, 8);
				else
				{
					mess = centraservice.chercherMessage(request, "Cr�er LIB" + llib + " sur cl� " + salarie.getNiv1() + " en T1", clang);
					globalUpdate._setEvolutionTraitement(request, mess, true);
					return false;
				}

			}
			else Num_Tiers = StringUtils.substring(salarie.getNiv1(), 0, 8);

			// compte ressemblant � XXXX+N1
			pos = (Num_compte.indexOf("+") != -1) ? Num_compte.indexOf("+") : -1;
			if (pos != -1)
			{
				Num_compte = StringUtils.substring(StringUtils.substring(Num_compte, 0, pos) + Num_Tiers, 0, 11);
				Num_Tiers = StringUtils.EMPTY;
				Type_Num_Tiers = "";
			}
			return true;
		}
		// -- Concatenation du niveau 2
		pos = (Num_compte.indexOf("N2") != -1) ? Num_compte.indexOf("N2") : -1;
		if (pos != -1)
		{
			// sp�cifique cnss
			Num_compte = StringUtils.substring(Num_compte, 0, pos);
			Type_Num_Tiers = "N2";
			if (blib)
			{
				fnom = centraservice.chercherEnNomenclature(cdos, 2, salarie.getNiv2(), llib);
				if (fnom != null && StringUtils.isNotBlank(fnom.getVall()))
					Num_Tiers = StringUtils.substring(fnom.getVall(), 0, 8);
				else
				{
					mess = centraservice.chercherMessage(request, "Cr�er LIB" + llib + " sur cl� " + salarie.getNiv2() + " en T2", clang);
					globalUpdate._setEvolutionTraitement(request, mess, true);
					return false;
				}

			}
			else Num_Tiers = StringUtils.substring(salarie.getNiv2(), 0, 8);

			// compte ressemblant � XXXX+N2
			pos = (Num_compte.indexOf("+") != -1) ? Num_compte.indexOf("+") : -1;
			if (pos != -1)
			{
				Num_compte = StringUtils.substring(StringUtils.substring(Num_compte, 0, pos) + Num_Tiers, 0, 11);
				Num_Tiers = StringUtils.EMPTY;
				Type_Num_Tiers = "";
			}
			return true;
		}
		// -- Concatenation du niveau 3
		pos = (Num_compte.indexOf("N3") != -1) ? Num_compte.indexOf("N3") : -1;
		if (pos != -1)
		{
			Num_compte = StringUtils.substring(Num_compte, 0, pos);
			Type_Num_Tiers = "N3";
			if (blib)
			{
				fnom = centraservice.chercherEnNomenclature(cdos, 3, salarie.getNiv3(), llib);
				if (fnom != null && StringUtils.isNotBlank(fnom.getVall()))
					Num_Tiers = StringUtils.substring(fnom.getVall(), 0, 8);
				else
				{
					mess = centraservice.chercherMessage(request, "Cr�er LIB" + llib + " sur cl� " + salarie.getNiv3() + " en T3", clang);
					globalUpdate._setEvolutionTraitement(request, mess, true);
					return false;
				}

			}
			else Num_Tiers = StringUtils.substring(salarie.getNiv3(), 0, 8);

			// compte ressemblant � XXXX+N3
			pos = (Num_compte.indexOf("+") != -1) ? Num_compte.indexOf("+") : -1;
			if (pos != -1)
			{
				Num_compte = StringUtils.substring(StringUtils.substring(Num_compte, 0, pos) + Num_Tiers, 0, 11);
				Num_Tiers = StringUtils.EMPTY;
				Type_Num_Tiers = "";
			}
			return true;
		}
		
		//-- Concatenation au code agence
		pos = (Num_compte.indexOf("AGE") != -1) ? Num_compte.indexOf("AGE") : -1;
		if (pos != -1)
		{
			Num_compte = StringUtils.substring(Num_compte, 0, pos);
			Type_Num_Tiers = "AGE";
			
			Num_Tiers = StringUtils.substring(agence_comptable, 0, 8);
			
			// compte ressemblant � XXXX+AGE
			pos = (Num_compte.indexOf("+") != -1) ? Num_compte.indexOf("+") : -1;
			if (pos != -1)
			{
				Num_compte = StringUtils.substring(StringUtils.substring(Num_compte, 0, pos) + Num_Tiers, 0, 11);
				Num_Tiers = StringUtils.EMPTY;
				Type_Num_Tiers = "";
			}
			return true;
		}
		
		// -- Concatenation de la classe
		pos = (Num_compte.indexOf("C") != -1) ? Num_compte.indexOf("C") : -1;
		if (pos != -1)
		{
			Num_compte = StringUtils.substring(Num_compte, 0, pos);
			Type_Num_Tiers = "C";
			if (blib)
			{
				fnom = centraservice.chercherEnNomenclature(cdos, 8, salarie.getClas(), llib);
				if (fnom != null && StringUtils.isNotBlank(fnom.getVall()))
					Num_Tiers = StringUtils.substring(fnom.getVall(), 0, 8);
				else
				{
					mess = centraservice.chercherMessage(request, "Cr�er LIB" + llib + " sur cl� " + salarie.getClas() + " en T8", clang);
					globalUpdate._setEvolutionTraitement(request, mess, true);
					return false;
				}
			}
			else Num_Tiers = StringUtils.substring(salarie.getClas(), 0, 8);

			// compte ressemblant � XXXX+C
			pos = (Num_compte.indexOf("+") != -1) ? Num_compte.indexOf("+") : -1;
			if (pos != -1)
			{
				Num_compte = StringUtils.substring(StringUtils.substring(Num_compte, 0, pos) + Num_Tiers, 0, 11);
				Num_Tiers = StringUtils.EMPTY;
				Type_Num_Tiers = "";
			}
			return true;
		}

		// -- Concatenation d'une constante stock�e dans le compte de la rubrique
		pos = (Num_compte.indexOf("CT") != -1) ? Num_compte.indexOf("CT") : -1;
		if (pos != -1)
		{
			Num_Tiers = StringUtils.substring(Num_compte, pos + 1, 8);
			Num_compte = StringUtils.substring(Num_compte, 0, pos);
			Type_Num_Tiers = "CT";
			// compte ressemblant � XXXX+CT
			pos = (Num_compte.indexOf("+") != -1) ? Num_compte.indexOf("+") : -1;
			if (pos != -1)
			{
				Num_compte = StringUtils.substring(StringUtils.substring(Num_compte, 0, pos) + Num_Tiers, 0, 11);
				Num_Tiers = StringUtils.EMPTY;
				Type_Num_Tiers = "";
			}
			return true;
		}

		// -- Concatenation de la ville de declaration
		pos = (Num_compte.indexOf("V") != -1) ? Num_compte.indexOf("V") : -1;
		if (pos != -1)
		{
			try
			{
				nb_rec = Integer.parseInt(StringUtils.substring(Num_compte, pos + 1));
			}
			catch (NumberFormatException ex)
			{
				nb_rec = 6;
			}
			fnom = centraservice.chercherEnNomenclature(cdos, 5, salarie.getVild(), 3);
			lib = fnom.getVall();
			if (StringUtils.isBlank(lib))
			{
				mess = centraservice.chercherMessage(request, "ERR-90137", clang);
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}

			Num_compte = StringUtils.substring(Num_compte, 0, pos);
			Num_Tiers = StringUtils.substring(lib, 0, nb_rec);
			Type_Num_Tiers = "V";
			// compte ressemblant � XXXX+V
			pos = (Num_compte.indexOf("+") != -1) ? Num_compte.indexOf("+") : -1;
			if (pos != -1)
			{
				Num_compte = StringUtils.substring(StringUtils.substring(Num_compte, 0, pos) + Num_Tiers, 0, 11);
				Num_Tiers = StringUtils.EMPTY;
				Type_Num_Tiers = "";
			}
			return true;
		}
		// -- generation parti analytique a partir du compte
		pos = (Num_compte.indexOf("S") != -1) ? Num_compte.indexOf("S") : -1;
		if (pos != -1)
		{
			try
			{
				nb_rec = Integer.parseInt(StringUtils.substring(Num_compte, pos + 1));
			}
			catch (NumberFormatException ex)
			{
				nb_rec = 1;
			}
			Num_compte = StringUtils.substring(Num_compte, 0, pos);
			Type_Num_Tiers = "S";
			if (nb_rec == 1)
			{
				Destination[1] = salarie.getNiv3();
				Num_Tiers = salarie.getNiv3();
			}
			else
			{
				if (nb_rec == 2)
				{
					Destination[1] = StringUtils.substring(salarie.getSana(), 0, 8);
				}
				else
				{
					Destination[1] = "";
				}
				Num_Tiers = Destination[1];
			}
			// compte ressemblant � XXXX+S
			pos = (Num_compte.indexOf("+") != -1) ? Num_compte.indexOf("+") : -1;
			if (pos != -1)
			{
				Num_compte = StringUtils.substring(StringUtils.substring(Num_compte, 0, pos) + Num_Tiers, 0, 11);
				Num_Tiers = StringUtils.EMPTY;
				Type_Num_Tiers = "";
			}
			return true;
		}

		// -- MM 01-2006
		// -- Zones libres
		pos = Num_compte.indexOf("@");
		if (pos != -1)
		{
			// w_pos2 = NVL(INSTR(SUBSTR(Num_Compte,w_pos+1,length(Num_Compte)-w_pos),'@'),0) + w_pos;
			int pos2 = StringUtil.oraSubstring(Num_compte, pos + 1, Num_compte.length() - pos).indexOf("@") + pos;
			if (pos2 != -1)
			{
				Integer zli1 = 0;
				try
				{
					zli1 = Integer.valueOf(StringUtil.oraSubstring(Num_compte, pos + 1, pos2 - (pos + 1)));
				}
				catch (NumberFormatException e)
				{
					mess = "Agent " + salarie.getNmat() + " Compte " + Num_compte + " format incorrect.";
					globalUpdate._setEvolutionTraitement(request, mess, true);
					return false;
				}

				Integer zli2 = 0;
				try
				{
					zli2 = Integer.valueOf(StringUtil.oraSubstring(Num_compte, pos2 + 1, Num_compte.length() - pos2));
				}
				catch (NumberFormatException e)
				{
					mess = "Agent " + salarie.getNmat() + " Compte " + Num_compte + " format incorrect.";
					globalUpdate._setEvolutionTraitement(request, mess, true);
					return false;
				}

				agence_comptable = StringUtil.oraSubstring(centraservice.chercherZoneLibreAgent(cdos, salarie.getNmat(), zli1), 1, 5);
				Num_compte = StringUtil.oraSubstring(centraservice.chercherZoneLibreAgent(cdos, salarie.getNmat(), zli2), 1, 15);

				return true;
			}
			else
			{
				mess = "Agent " + salarie.getNmat() + " Compte " + Num_compte + " format incorrect.";
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#chargerEtablissement(com.cdi.deltarh.entites.Rhpagent, java.lang.String)
	 */
	public boolean chargerEtablissement(Salarie salarie, String crub) throws Exception
	{
		try
		{
			ParameterUtil.println("Entr�e dans chargerEtablissement");

			if (ClsObjectUtil.isNull(Cod_Etb_Commun))
			{
				fnom = centraservice.chercherEnNomenclature(cdos, 1, salarie.getNiv1(), 2);
				if (!ClsObjectUtil.isNull(fnom))
					Code_Etablissement = fnom.getVall();
				if (ClsObjectUtil.isNull(Code_Etablissement))
					Code_Etablissement = salarie.getNiv1();
			}
			else
			{
				Code_Etablissement = Cod_Etb_Commun;
			}

			String colonne = StringUtils.EMPTY;
			Integer table = 0;
			Integer numColonne = -1;

			Integer index = 0;
			String valeur = StringUtils.EMPTY;
			if (!paramDestinations.isEmpty())
			{
				for (ParamData fnom : paramDestinations)
				{
					valeur = StringUtils.EMPTY;
					index = fnom.getNume();
					// cas particulier : dest1 = sana

					if (StringUtils.isNotBlank(fnom.getVall()))
						colonne = fnom.getVall();
					if (index == 1 && StringUtils.isBlank(colonne))
						colonne = "sana";
					if (fnom.getValm() != null)
						table = fnom.getValm().intValue();
					if (fnom.getValt() != null)
						numColonne = fnom.getValt().intValue();
					if (StringUtils.isNotBlank(colonne))
					{
						valeur = StringUtils.valueOf((char[]) MethodUtils.invokeExactMethod(salarie, "get" + StringUtils.capitalize(colonne.toLowerCase()), null));
						if (table != 0 && numColonne != -1)
						{
							fnom = centraservice.chercherEnNomenclature(cdos, table, valeur, numColonne);
							if (!ClsObjectUtil.isNull(fnom))
								valeur = fnom.getVall();
						}
					}
					Destination[index] = valeur;
				}
			}

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			throw ex;
		}
		return true;
	}

	public String RechercheValeur(Salarie salarie, String crub, String cle1)
	{

		int numerotable = 0;
		String numeroCompte = Num_compte;// "";
		String libelletable = "";
		int numerolibelle = 0;
		String key = "";

		boolean flag = false;
		String s = "";
		if (!ClsObjectUtil.isNull(cle1) && cle1.toCharArray()[0] == '@')
		{
			flag = true;
			s = StringUtils.substring(cle1, 2, 4);
			if (!ClsObjectUtil.isNull(s))
			{
				numerotable = Integer.parseInt(s);
			}
			s = StringUtils.substring(cle1, 5, 7);
			if (!ClsObjectUtil.isNull(s))
			{
				numerolibelle = Integer.parseInt(s);
			}
			key = StringUtils.substring(cle1, 8, 11);
		}
		else key = cle1;
		String res = "";
		int ikey = Integer.parseInt(key);

		if (ikey >= 801 && ikey <= 899)
		{
			res = centraservice.chercherZoneLibreAgent(cdos, salarie.getNmat(), Integer.parseInt(StringUtils.substring(key, 1, 3)));
			res = res.substring(0, 10);
		}
		else
		{
			switch (ikey)
			{
			case 1:
				res = salarie.getNiv1();
				break;
			case 2:
				res = salarie.getNiv2();
				break;
			case 3:
				res = salarie.getNiv3();
				break;

			case 4:
				res = salarie.getNmat();
				break;
			case 8:
				res = salarie.getEqui();
				break;
			case 11:
				res = salarie.getSexe();
				break;
			case 21:
				res = salarie.getNato();
				break;
			case 22:
				res = salarie.getSitf();
				break;
			case 23:
				res = ClsStringUtil.formatNumber(salarie.getNbcj(), "0000");
				break;
			case 24:
				res = ClsStringUtil.formatNumber(salarie.getNbec(), "0000");
				break;
			case 25:
				res = ClsStringUtil.formatNumber(salarie.getNbfe(), "0000");
				break;
			case 27:
				res = salarie.getModp();
				break;
			case 33:
				res = StringUtils.substring(salarie.getCcpt(), 0, 10);
				break;
			case 39:
				res = salarie.getCat();
				break;
			case 40:
				res = salarie.getEch();
				break;
			case 43:
				res = salarie.getGrad();
				break;
			case 44:
				res = salarie.getFonc();
				break;
			case 45:
				res = salarie.getAfec();
				break;
			case 47:
				res = ClsStringUtil.formatNumber(salarie.getIndi(), "000000");
				break;
			case 58:
				res = salarie.getHifo();
				break;
			case 59:
				res = salarie.getZli1();
				break;
			case 60:
				res = salarie.getZli2();
				break;
			case 67:
				res = salarie.getTypc();
				break;
			case 70:
				res = salarie.getRegi();
				break;
			case 71:
				res = salarie.getZres();
				break;
			case 72:
				res = salarie.getDmst();
				break;
			case 73:
				res = ClsStringUtil.formatNumber(salarie.getNpie(), "0000");
				break;
			case 184:
				res = StringUtils.substring(salarie.getSana(), 0, 10);
				break;
			case 185:
				res = salarie.getEqui();
				break;
			case 770:
				res = StringUtil.oraLPad(crub, 4, "O");
				break;
			case 771:
				res = StringUtils.substring(numeroCompte, 0, 10);
				break;

			default:
				res = " ";
				break;
			}
		}

		if (flag)
		{
			boolean val = true;
			// -- on fait une lecture sur la Table xx en libelle xx avec cle = char10
			if (ikey == 771 && val)
			{
				// -- la recherche du No compte se fait sur le lib 1 car cacc ne fait que 10 char max.
				String queryString = "From ParamData" + " where idEntreprise = '" + cdos + "'" + " and ctab = " + numerotable + " and nume = "
						+ numerolibelle + " and cacc in ( select cacc From ParamData" + " where idEntreprise = '" + cdos + "'"
						+ " and ctab = " + numerotable + " and nume = 1" + " and vall = '" + numeroCompte + "')";
				List<ParamData> l = centraservice.chercherEnNomenclature(queryString);

				if (l.size() > 0)
				{
					libelletable = l.get(0).getVall();
					res = StringUtils.substring(libelletable, 0, 10);
				}
			}
			else
			{
				fnom = centraservice.chercherEnNomenclature(cdos, numerotable, res.trim(), numerolibelle);
				if (fnom != null)
				{
					libelletable = fnom.getVall();
					res = StringUtils.substring(libelletable, 0, 10);
				}
			}
		}
		//
		// RETURN char10;
		return res;
	}

	public ClsCentralisationService getCentraservice()
	{
		return centraservice;
	}

	public void setCentraservice(ClsCentralisationService centraservice)
	{
		this.centraservice = centraservice;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getPafnom()
	 */
	public ClsPafnomType getPafnom()
	{
		return pafnom;
	}

	public void setPafnom(ClsPafnomType pafnom)
	{
		this.pafnom = pafnom;
	}

	public String getRbq_NAP()
	{
		return Rbq_NAP;
	}

	public void setRbq_NAP(String rbq_NAP)
	{
		Rbq_NAP = rbq_NAP;
	}

	public String getRbq_BRUT()
	{
		return Rbq_BRUT;
	}

	public void setRbq_BRUT(String rbq_BRUT)
	{
		Rbq_BRUT = rbq_BRUT;
	}

	public char getExist()
	{
		return exist;
	}

	public void setExist(char exist)
	{
		this.exist = exist;
	}

	public String getChar4()
	{
		return char4;
	}

	public void setChar4(String char4)
	{
		this.char4 = char4;
	}

	public Integer getNbbul()
	{
		return nbbul;
	}

	public void setNbbul(Integer nbbul)
	{
		this.nbbul = nbbul;
	}

	public Integer getNb_sal()
	{
		return nb_sal;
	}

	public void setNb_sal(Integer nb_sal)
	{
		this.nb_sal = nb_sal;
	}

	public Boolean getRetour()
	{
		return retour;
	}

	public void setRetour(Boolean retour)
	{
		this.retour = retour;
	}

	public String getLib_Mois()
	{
		return Lib_Mois;
	}

	public void setLib_Mois(String lib_Mois)
	{
		Lib_Mois = lib_Mois;
	}

	public String getMess()
	{
		return mess;
	}

	public void setMess(String mess)
	{
		this.mess = mess;
	}

	public String getMess2()
	{
		return mess2;
	}

	public void setMess2(String mess2)
	{
		this.mess2 = mess2;
	}

	public char getCas()
	{
		return cas;
	}

	public void setCas(char cas)
	{
		this.cas = cas;
	}

	public String getAamm()
	{
		return aamm;
	}

	public void setAamm(String aamm)
	{
		this.aamm = aamm;
	}

	public Integer getNbul()
	{
		return nbul;
	}

	public void setNbul(Integer nbul)
	{
		this.nbul = nbul;
	}

	public String getCuti()
	{
		return cuti;
	}

	public void setCuti(String cuti)
	{
		this.cuti = cuti;
	}

	public String getClang()
	{
		return clang;
	}

	public void setClang(String clang)
	{
		this.clang = clang;
	}

	public Integer getNddd()
	{
		return nddd;
	}

	public void setNddd(Integer nddd)
	{
		this.nddd = nddd;
	}

	public ClsDate getDate_Comptable()
	{
		return Date_Comptable;
	}

	public void setDate_Comptable(ClsDate date_Comptable)
	{
		Date_Comptable = date_Comptable;
	}

	public String getPipe_name()
	{
		return pipe_name;
	}

	public void setPipe_name(String pipe_name)
	{
		this.pipe_name = pipe_name;
	}

	public Integer getTimeout()
	{
		return timeout;
	}

	public void setTimeout(Integer timeout)
	{
		this.timeout = timeout;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getCode_Journal()
	 */
	public String getCode_Journal()
	{
		return Code_Journal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setCode_Journal(java.lang.String)
	 */
	public void setCode_Journal(String code_Journal)
	{
		Code_Journal = code_Journal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getSens()
	 */
	public char getSens()
	{
		return sens;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setSens(char)
	 */
	public void setSens(char sens)
	{
		this.sens = sens;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getNum_compte()
	 */
	public String getNum_compte()
	{
		return Num_compte;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setNum_compte(java.lang.String)
	 */
	public void setNum_compte(String num_compte)
	{
		Num_compte = num_compte;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getNum_Piece()
	 */
	public String getNum_Piece()
	{
		return Num_Piece;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setNum_Piece(java.lang.String)
	 */
	public void setNum_Piece(String num_Piece)
	{
		Num_Piece = num_Piece;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getLibelle_Ecriture()
	 */
	public String getLibelle_Ecriture()
	{
		return Libelle_Ecriture;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setLibelle_Ecriture(java.lang.String)
	 */
	public void setLibelle_Ecriture(String libelle_Ecriture)
	{
		Libelle_Ecriture = libelle_Ecriture;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getLibelle_Abrege()
	 */
	public String getLibelle_Abrege()
	{
		return Libelle_Abrege;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setLibelle_Abrege(java.lang.String)
	 */
	public void setLibelle_Abrege(String libelle_Abrege)
	{
		Libelle_Abrege = libelle_Abrege;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getChar10()
	 */
	public String getChar10()
	{
		return char10;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setChar10(java.lang.String)
	 */
	public void setChar10(String char10)
	{
		this.char10 = char10;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getCpt_Type()
	 */
	public char getCpt_Type()
	{
		return Cpt_Type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setCpt_Type(char)
	 */
	public void setCpt_Type(char cpt_Type)
	{
		Cpt_Type = cpt_Type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getCode_Etablissement()
	 */
	public String getCode_Etablissement()
	{
		return Code_Etablissement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setCode_Etablissement(java.lang.String)
	 */
	public void setCode_Etablissement(String code_Etablissement)
	{
		Code_Etablissement = code_Etablissement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getNum_Tiers()
	 */
	public String getNum_Tiers()
	{
		return Num_Tiers;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setNum_Tiers(java.lang.String)
	 */
	public void setNum_Tiers(String num_Tiers)
	{
		Num_Tiers = num_Tiers;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getDevise_Dossier()
	 */
	public String getDevise_Dossier()
	{
		return Devise_Dossier;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setDevise_Dossier(java.lang.String)
	 */
	public void setDevise_Dossier(String devise_Dossier)
	{
		Devise_Dossier = devise_Dossier;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getCod_Etb_Commun()
	 */
	public String getCod_Etb_Commun()
	{
		return Cod_Etb_Commun;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setCod_Etb_Commun(java.lang.String)
	 */
	public void setCod_Etb_Commun(String cod_Etb_Commun)
	{
		Cod_Etb_Commun = cod_Etb_Commun;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getDest2_Commune()
	 */
	public String getDest2_Commune()
	{
		return Dest2_Commune;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setDest2_Commune(java.lang.String)
	 */
	public void setDest2_Commune(String dest2_Commune)
	{
		Dest2_Commune = dest2_Commune;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getExistence_Compte()
	 */
	public Boolean getExistence_Compte()
	{
		return Existence_Compte;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setExistence_Compte(java.lang.Boolean)
	 */
	public void setExistence_Compte(Boolean existence_Compte)
	{
		Existence_Compte = existence_Compte;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getSuivi_Analytique()
	 */
	public Boolean getSuivi_Analytique()
	{
		return Suivi_Analytique;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setSuivi_Analytique(java.lang.Boolean)
	 */
	public void setSuivi_Analytique(Boolean suivi_Analytique)
	{
		Suivi_Analytique = suivi_Analytique;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getCentra_dos()
	 */
	public Boolean getCentra_dos()
	{
		return centra_dos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setCentra_dos(java.lang.Boolean)
	 */
	public void setCentra_dos(Boolean centra_dos)
	{
		this.centra_dos = centra_dos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getNumdos_centra()
	 */
	public String getNumdos_centra()
	{
		return numdos_centra;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setNumdos_centra(java.lang.String)
	 */
	public void setNumdos_centra(String numdos_centra)
	{
		this.numdos_centra = numdos_centra;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getExist_cpt()
	 */
	public Boolean getExist_cpt()
	{
		return exist_cpt;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setExist_cpt(java.lang.Boolean)
	 */
	public void setExist_cpt(Boolean exist_cpt)
	{
		this.exist_cpt = exist_cpt;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getExist_aux()
	 */
	public Boolean getExist_aux()
	{
		return exist_aux;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setExist_aux(java.lang.Boolean)
	 */
	public void setExist_aux(Boolean exist_aux)
	{
		this.exist_aux = exist_aux;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getCpt_numcpt()
	 */
	public String getCpt_numcpt()
	{
		return Cpt_numcpt;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setCpt_numcpt(java.lang.String)
	 */
	public void setCpt_numcpt(String cpt_numcpt)
	{
		Cpt_numcpt = cpt_numcpt;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getInd_deb()
	 */
	public Integer getInd_deb()
	{
		return ind_deb;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setInd_deb(java.lang.Integer)
	 */
	public void setInd_deb(Integer ind_deb)
	{
		this.ind_deb = ind_deb;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getInd_cre()
	 */
	public Integer getInd_cre()
	{
		return ind_cre;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setInd_cre(java.lang.Integer)
	 */
	public void setInd_cre(Integer ind_cre)
	{
		this.ind_cre = ind_cre;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getTotal_Debit()
	 */
	public Double getTotal_Debit()
	{
		return Total_Debit;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setTotal_Debit(java.lang.Double)
	 */
	public void setTotal_Debit(Double total_Debit)
	{
		Total_Debit = total_Debit;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getTotal_Credit()
	 */
	public Double getTotal_Credit()
	{
		return Total_Credit;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setTotal_Credit(java.lang.Double)
	 */
	public void setTotal_Credit(Double total_Credit)
	{
		Total_Credit = total_Credit;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getWecart()
	 */
	public Double getWecart()
	{
		return wecart;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setWecart(java.lang.Double)
	 */
	public void setWecart(Double wecart)
	{
		this.wecart = wecart;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getType_Rubq()
	 */
	public Integer getType_Rubq()
	{
		return Type_Rubq;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setType_Rubq(java.lang.Integer)
	 */
	public void setType_Rubq(Integer type_Rubq)
	{
		Type_Rubq = type_Rubq;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getMnt_Ecriture()
	 */
	public double getMnt_Ecriture()
	{
		return Mnt_Ecriture;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setMnt_Ecriture(double)
	 */
	public void setMnt_Ecriture(double mnt_Ecriture)
	{
		Mnt_Ecriture = mnt_Ecriture;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getWint()
	 */
	public InterfComptable getWint()
	{
		return wint;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setWint(com.cdi.deltarh.entites.CpInt)
	 */
	public void setWint(InterfComptable wint)
	{
		this.wint = wint;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getCdos()
	 */
	public String getCdos()
	{
		return cdos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setCdos(java.lang.String)
	 */
	public void setCdos(String cdos)
	{
		this.cdos = cdos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getComp()
	 */
	public char getComp()
	{
		return comp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setComp(char)
	 */
	public void setComp(char comp)
	{
		this.comp = comp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getRequest()
	 */
	public HttpServletRequest getRequest()
	{
		return request;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setRequest(javax.servlet.http.HttpServletRequest)
	 */
	public void setRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getCalcul()
	 */
	public CalculPaie getCalcul()
	{
		return calcul;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setCalcul(com.cdi.deltarh.entites.Rhtcalcul)
	 */
	public void setCalcul(CalculPaie calcul)
	{
		this.calcul = calcul;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getRubrique()
	 */
	public ElementSalaire getRubrique()
	{
		return rubrique;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setRubrique(com.cdi.deltarh.entites.Rhprubrique)
	 */
	public void setRubrique(ElementSalaire rubrique)
	{
		this.rubrique = rubrique;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getFnom()
	 */
	public ParamData getFnom()
	{
		return fnom;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setFnom(com.cdi.deltarh.entites.Rhfnom)
	 */
	public void setFnom(ParamData fnom)
	{
		this.fnom = fnom;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getSession()
	 */
	public Session getSession()
	{
		return session;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setSession(org.hibernate.Session)
	 */
	public void setSession(Session session)
	{
		this.session = session;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getDestination()
	 */
	public String[] getDestination()
	{
		return Destination;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setDestination(java.lang.String[])
	 */
	public void setDestination(String[] destination)
	{
		Destination = destination;
	}

	public String getV_cdos()
	{
		return v_cdos;
	}

	public void setV_cdos(String v_cdos)
	{
		this.v_cdos = v_cdos;
	}

	public void Compte_OK(String Num_Cpt)
	{
		// TODO Auto-generated method stub

	}

	public boolean Destination_OK(String Destination, Integer Num_Axe)
	{
		// TODO Auto-generated method stub
		return false;
	}

	public String getCle_tab_gl()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Boolean getDeltacom()
	{
		// TODO Auto-generated method stub
		return false;
	}

	public String[] getDestination_gl()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Boolean getInterface_gl()
	{
		// TODO Auto-generated method stub
		return false;
	}

	public Integer getNum_tab_gl()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setCle_tab_gl(String cle_tab_gl)
	{
		// TODO Auto-generated method stub

	}

	public void setDeltacom(Boolean deltacom)
	{
		// TODO Auto-generated method stub

	}

	public void setDestination_gl(String[] destination_gl)
	{
		// TODO Auto-generated method stub

	}

	public void setInterface_gl(Boolean interface_gl)
	{
		// TODO Auto-generated method stub

	}

	public void setNum_tab_gl(Integer num_tab_gl)
	{
		// TODO Auto-generated method stub

	}

	public Boolean[] getDestination_Existe()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setDestination_Existe(Boolean[] destination_Existe)
	{
		// TODO Auto-generated method stub

	}

	public boolean isCrediteur()
	{
		return crediteur;
	}

	public void setCrediteur(boolean crediteur)
	{
		this.crediteur = crediteur;
	}

	public ClsGlobalUpdate getGlobalUpdate()
	{
		return globalUpdate;
	}

	public void setGlobalUpdate(ClsGlobalUpdate globalUpdate)
	{
		this.globalUpdate = globalUpdate;
	}

}
