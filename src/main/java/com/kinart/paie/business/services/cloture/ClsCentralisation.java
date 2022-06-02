/**
 * 
 */
package com.kinart.paie.business.services.cloture;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.kinart.paie.business.model.*;
import com.kinart.paie.business.services.utils.*;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.springframework.dao.DataAccessException;

/**
 * @author p.nouthio
 * 
 */
public class ClsCentralisation implements ICentralisation
{
	Map<String, InterfComptable> mapCPINTCNSS = new HashMap<String, InterfComptable>();
	
	Map<String, CpAnaCNSS> mapCPANACNSS = new HashMap<String, CpAnaCNSS>();
	// Objet d'acc�s aux donn�es
	
	private Session session;

	private ClsCentralisationService centraservice = null;

	private ClsPafnomType pafnom = new ClsPafnomType();

	private ParamData fnom;
	
	private ClsGlobalUpdate globalUpdate = null;

	// CREATE OR REPLACE PACKAGE DELTA.pa_centra IS

	//
	// PCKI CONSTANT VARCHAR2(20) := 'PA_CENTRA'; -- Nom du package
	// PCKV CONSTANT VARCHAR2(10) := '4.1.200505'; -- Version du package
	//
	public static String PCKI = "PA_CENTRA";

	public static String PCKV = "4.1.200505";

	//
	// Rbq_NAP VARCHAR2(4);
	// Rbq_BRUT VARCHAR2(4);
	// exist CHAR(1);
	// char4 VARCHAR2(4);
	// nbbul INTEGER;
	// nb_sal INTEGER;
	// retour BOOLEAN;
	// Lib_Mois VARCHAR2(20);
	//
	// mess VARCHAR2(100);
	// mess2 VARCHAR2(100);
	// cas varchar2(1);
	// aamm VARCHAR2(6);
	// nbul pacalc.nbul%TYPE;
	// cuti evuti.cuti%TYPE;
	// clang VARCHAR2(5);
	// nddd cp_dos.nddd%TYPE;
	// Date_Comptable cp_int.datcpt%TYPE;
	//
	// pipe_name VARCHAR2(30);
	// timeout INTEGER:=3600;
	// status INTEGER;
	//
	
	public String v_cdos;
	
	private String Rbq_NAP;

	private String Rbq_BRUT;

	private char exist;

	private String char4;

	private int nbbul;

	private int nb_sal;

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

	private int timeout;

	// -- Declarations pour centralisation
	// deltacom BOOLEAN;
	// Code_Journal cp_int.codjou%TYPE;
	// sens cp_int.sens%TYPE;
	// -- Num_Compte cp_int.numcpt%TYPE;
	// Num_Compte PARUBQ.DE01%TYPE;
	//
	private Boolean deltacom;

	private String Code_Journal;

	private char sens;

	private String Num_compte;
	
	//sp�cifique cnss
	private String xNum_compte;

	// Num_Piece cp_int.numpce%TYPE;
	// Libelle_Ecriture cp_int.libecr%TYPE;
	// Libelle_Abrege cp_int.codabr%TYPE;
	// Destination1 cp_int.coddes1%TYPE;
	// Destination2 cp_int.coddes2%TYPE;
	// Destination3 cp_int.coddes3%TYPE;
	// Destination4 cp_int.coddes4%TYPE;
	// Destination5 cp_int.coddes5%TYPE;
	// Destination6 cp_int.coddes6%TYPE;
	// Destination7 cp_int.coddes7%TYPE;
	// Destination8 cp_int.coddes8%TYPE;
	// Destination9 cp_int.coddes9%TYPE;
	//

	private String Num_Piece;

	private String Libelle_Ecriture;

	private String Libelle_Abrege;

	private String[] Destination = new String[]{null,null,null,null,null,null,null,null,null,null};
	/*
	private String Destination1;

	private String Destination2;

	private String Destination3;

	private String Destination4;

	private String Destination5;

	private String Destination6;

	private String Destination7;

	private String Destination8;

	private String Destination9;*/

	// --- debut ajout interface gl
	//
	// Destination1_gl pafnom.vall%TYPE;
	// Destination2_gl pafnom.vall%TYPE;
	// Destination3_gl pafnom.vall%TYPE;
	// Destination4_gl pafnom.vall%TYPE;
	// Destination5_gl pafnom.vall%TYPE;
	// Destination6_gl pafnom.vall%TYPE;
	// Destination7_gl pafnom.vall%TYPE;
	// Destination8_gl pafnom.vall%TYPE;
	// Destination9_gl pafnom.vall%TYPE;
	// interface_gl BOOLEAN;
	// num_tab_gl pafnom.valm%TYPE;
	// cle_tab_gl pafnom.cacc%TYPE;
	// char10 VARCHAR2(10);
	private String[] Destination_gl = new String[]{null,null,null,null,null,null,null,null,null,null};
	/*
	private String Destination1_gl;

	private String Destination2_gl;

	private String Destination3_gl;

	private String Destination4_gl;

	private String Destination5_gl;

	private String Destination6_gl;

	private String Destination7_gl;

	private String Destination8_gl;

	private String Destination9_gl;*/

	private Boolean interface_gl;

	private int num_tab_gl;

	private String cle_tab_gl;

	private String char10;

	// ----- fin ajout interface gl
	// Cpt_Type cp_cpt.typcpt%TYPE;
	//
	//
	// Code_Etablissement cp_int.codets%TYPE;
	// Num_Tiers cp_int.numtie%type;
	// Devise_Dossier cp_int.devpce%type;
	// Cod_Etb_Commun cp_int.codets%TYPE;
	// Dest2_Commune cp_int.coddes2%TYPE;
	// Destination1_Existe BOOLEAN;
	// Destination2_Existe BOOLEAN;
	// Destination3_Existe BOOLEAN;
	// Destination4_Existe BOOLEAN;
	// Destination5_Existe BOOLEAN;
	// Destination6_Existe BOOLEAN;
	// Destination7_Existe BOOLEAN;
	// Destination8_Existe BOOLEAN;
	// Destination9_Existe BOOLEAN;
	//
	private char Cpt_Type;

	private String Code_Etablissement;

	private String Num_Tiers;

	private String Devise_Dossier;

	private String Cod_Etb_Commun;

	private String Dest2_Commune;

	private Boolean[] Destination_Existe = new Boolean[]{false,false,false,false,false,false,false,false,false,false};

	/*
	private Boolean Destination1_Existe;
	
	private Boolean Destination2_Existe;

	private Boolean Destination3_Existe;

	private Boolean Destination4_Existe;

	private Boolean Destination5_Existe;

	private Boolean Destination6_Existe;

	private Boolean Destination7_Existe;

	private Boolean Destination8_Existe;

	private Boolean Destination9_Existe;*/

	// Existence_Compte BOOLEAN;
	// Suivi_Analytique BOOLEAN;
	//
	// centra_dos BOOLEAN;
	private Boolean Existence_Compte;

	private Boolean Suivi_Analytique;

	private Boolean centra_dos;

	// numdos_centra PADOS.CDOS%TYPE;
	//
	// exist_cpt BOOLEAN;
	// exist_aux BOOLEAN;
	// Cpt_numcpt cp_cpt.numcpt%TYPE;
	//
	private String numdos_centra;

	private Boolean exist_cpt;

	private Boolean exist_aux;

	private String Cpt_numcpt;

	//
	// ind_deb NUMBER(2);
	// ind_cre NUMBER(2);
	// Total_Debit cp_int.pce_mt%TYPE;
	// Total_Credit cp_int.pce_mt%TYPE;
	// wecart cp_int.pce_mt%TYPE;
	//
	private int ind_deb;

	private int ind_cre;

	private BigDecimal Total_Debit;

	private BigDecimal Total_Credit;

	private BigDecimal wecart;

	// Type_Rubq pafnom.valm%TYPE;
	// Mnt_Ecriture cp_int.pce_mt%TYPE;
	//
	private int Type_Rubq;

	private BigDecimal Mnt_Ecriture;

	// -- Variables de table
	// wint cp_int%ROWTYPE;
	// cdos pados.cdos%TYPE;
	// comp pados.comp%TYPE;
	// salarie pasa01%ROWTYPE;
	// calcul pacalc%ROWTYPE;
	// rubrique parubq%ROWTYPE;
	//
	private InterfComptable wint;
	
	private CpAnaCNSS cpana;

	private String cdos;

	private char comp;

	private HttpServletRequest request;

	private CalculPaie calcul = new CalculPaie();

	private ElementSalaire rubrique = new ElementSalaire();
	
	private SimpleDateFormat dateFormat;
	
	public static int compteur = 0;
//	w_perso                 varchar2(1);
//	-- LM 13/01/2009 Specif CNSS 
//	w_tiers                 varchar2(1);
//	-- LM 13/01/2009 Fin specif CNSS 
	//sp�cifique cnss
	String perso;
	String tiers;

//	----- debut ajout specif cnss 
//	xNum_Compte		parubq.de01%TYPE;
	String xNum_Compte;
//	w_cpt			cp_int.numcpt%TYPE;
	String cpt;
//	Num_Piece_Sal		cp_int.numpce%TYPE;
	String Num_Piece_Sal;
//	w_codets		cp_int.codets%TYPE;
	String codets;
//	w_codges		cp_int.coddes2%TYPE;
	String codges;
//	w_analyt		cp_int.coddes3%TYPE;
	String analyt;
//	w_sana			cp_int.coddes6%TYPE;
	String sana;
//	w_codpie		VARCHAR2(2);
	String codpie;
//	w_count 		NUMBER;
	Integer count;
//	tmp_codges		cp_int.coddes2%TYPE;
	String tmp_codges;
//
//	----- fin ajout specif cnss 
//	-- LM 13/01/2009 Fin specif CNSS 
	/**

	 */
	public ClsCentralisation()
	{
		
	}
	public ClsCentralisation(HttpServletRequest request, ClsGlobalUpdate globalUpdate, ClsCentralisationService centraservice, String aamm, Integer nbul, String cuti, String clang, Integer nddd, ClsDate date_Comptable, String cdos)
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

	//
	// PROCEDURE lcentra (l_options IN VARCHAR2, l_pipe_name IN VARCHAR2);
	// PROCEDURE ch_deb;
	// PROCEDURE ch_cre;
	// PROCEDURE Compte_OK( Num_Cpt IN cp_cpt.numcpt%TYPE,
	// Compte_Exist OUT BOOLEAN,Suivi_anal OUT BOOLEAN);
	// FUNCTION init RETURN BOOLEAN;
	// FUNCTION centra RETURN BOOLEAN;
	// FUNCTION lect_rub_cal RETURN BOOLEAN;
	// FUNCTION ven_ecr RETURN BOOLEAN;
	// FUNCTION rech_cpt(crub IN VARCHAR2) RETURN BOOLEAN;
	// FUNCTION cpt_cherche_lettre RETURN BOOLEAN;
	// FUNCTION ch_rub RETURN BOOLEAN;
	// FUNCTION Destination_OK(Destination IN cp_des.coddes%TYPE , Num_Axe IN cp_des.numaxe%TYPE)
	// RETURN BOOLEAN;
	// FUNCTION charg_etssec RETURN BOOLEAN;
	// FUNCTION chg_sal RETURN BOOLEAN;
	//
	// FUNCTION concpro(a_cle1 IN VARCHAR2) RETURN VARCHAR2;
	//
	// FUNCTION centra_dossier (o_centra OUT BOOLEAN,o_dos OUT VARCHAR2) RETURN BOOLEAN;
	//
	//
	// END pa_centra;
	// /
	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#chg_sal()
	 */
	public List<Salarie> chg_sal() throws Exception
	{
		// ---------------------------------------------------------------------------------
		// -- chargement des salaries a traiter
		// ---------------------------------------------------------------------------------
		// FUNCTION chg_sal RETURN BOOLEAN IS
		//
		// BEGIN
		// nb_sal := 0;
		//
		// SELECT COUNT(*) INTO nb_sal FROM pasa01 WHERE cdos = cdos;
		//
		// RETURN TRUE;
		//
		// END chg_sal;
		ParameterUtil.println(">> Entr�e dans chg_sal(0)");
		List list = null;
		String request = null;
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

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#init(char)
	 */
	@SuppressWarnings("unchecked")
	public boolean init(char comp) throws Exception
	{
		ParameterUtil.println(">> Entr�e dans init(1)");
		// Variable utilis�e pour r�cup�rer les r�sultats des select Hibernate
		List list = null;
		String query = null;// affiche les requ�tes HQL.
		try
		{
			this.dateFormat = new SimpleDateFormat(ParameterUtil.getSessionObject(request, ParameterUtil.SESSION_FORMAT_DATE));

			ParamData fnom = globalUpdate.getService().findAnyColumnFromNomenclature(cdos,"", "99","NEWCPTRUB","1");
			//Rhfnom fnom = (Rhfnom)globalUpdate.getService().get(Rhfnom.class, new RhfnomPK(cdos,99,"NEWCPTRUB",1));
			nouvelleCompta = (fnom != null && StringUtils.isNotBlank(fnom.getVall()) && StringUtils.equals("O", fnom.getVall()));
			
			// FUNCTION init RETURN BOOLEAN IS
			//
			// CURSOR curs_edibul IS SELECT nmat FROM pacalc
			// WHERE cdos = cdos
			// AND aamm = aamm
			// AND nbul = nbul
			// AND rubq = Rbq_NAP
			// AND trtb = '1';
			//
			// date DATE;
			// edi_mat pasa01.nmat%TYPE;
			// Dest_etat cp_des.etat%TYPE;
			// Dest_debsai cp_des.debsai%TYPE;
			// Dest_finsai cp_des.finsai%TYPE;

			ClsDate date;
			String edi_mat = null;
			char Dest_etat;
			ClsDate Dest_debsai;
			ClsDate Dest_finsai;
			//
			// ---- MM 28/09/2000 on ne controle pas les bulletins vierge = toute rub = zero
			// mat_exist pasa01.nmat%TYPE;
			// CURSOR curs_bull_exist IS
			// SELECT a.nmat
			// FROM pacalc a, parubq b
			// WHERE a.cdos = cdos
			// AND a.aamm = aamm
			// AND a.nmat = edi_mat
			// AND a.nbul = nbul
			// AND a.rubq != Rbq_NAP
			// AND b.cdos = a.cdos
			// AND b.crub=a.rubq
			// AND b.prbul !=0
			// AND (b.ednul = 'O' OR NVL(a.mont,0) != 0);
			// -- Fin MM 28/09/2000
			//
			//
			// BEGIN
			//
			// -- Centralisation : no de piece , libelles etendu
			// Num_Piece := substr(aamm,5,2)||substr(aamm,1,4)
			// || ltrim(to_char(nbul,0));
			//
			// date := TO_DATE(aamm,'YYYYMM');
			// Lib_Mois := TO_CHAR(date,'MONTH');
			//
			// Libelle_Ecriture := 'Paie:Bul. ' || ltrim(to_char(nbul,'0'))
			// || ' de ' || Lib_Mois || ' ' || TO_CHAR(date,'YYYY');

			Num_Piece = aamm.substring(4, 6) + aamm.substring(0, 4) + nbul.toString().trim();
			// sp�cifique cnss
			if (StringUtils.equals(globalUpdate.nomClient, ClsEntreprise.CNSS))
			{
//				 Num_Piece := substr(w_aamm,5,2)||substr(w_aamm,3,2)
//	                || ltrim(to_char(w_nbul,0));
				Num_Piece = aamm.substring(4, 6) + StringUtil.oraSubstring(aamm, 3, 2) + ClsStringUtil.formatNumber(Integer.parseInt(nbul.toString().trim()),"0");
			}
			date = new ClsDate(aamm, "yyyyMM");
			Lib_Mois = date.getDateS("MMMM");
			if (StringUtils.equals(globalUpdate.nomClient, ClsEntreprise.EDM))
				Lib_Mois = date.getDateS("MMMM").toUpperCase();
			Libelle_Ecriture = "Paie:Bul. " + nbul.toString().trim() + " de " + Lib_Mois + " " + date.getDateS("yyyy");
			//
			// -- Lecture de la rubrique du net a payer table 99
			// BEGIN
			// SELECT valm INTO pafnom.mnt1 FROM pafnom
			// WHERE cdos = cdos
			// AND ctab = 99
			// AND cacc = 'RUBNAP'
			// AND nume = 1;
			// EXCEPTION
			// WHEN NO_DATA_FOUND THEN NULL;
			// END;
			//
			// IF SQL%NOTFOUND THEN
			// mess := PA_PAIE.centraservice.chercherMessage(request,'INF-10116',clang);
			// RETURN FALSE;
			// ELSE
			// Rbq_NAP := ltrim(to_char(pafnom.mnt1,'0999'));
			// END IF;
			//
			fnom = centraservice.chercherEnNomenclature(cdos, 99, "RUBNAP", 1);
			if(fnom.getValm() != null)
			{
				Rbq_NAP = StringUtils.leftPad(fnom.getValm().toString(), 4,'0');
			}
			else
			{
				mess = centraservice.chercherMessage(request,"INF-10116", clang);
				globalUpdate._setEvolutionTraitement(request, "INF-10116", true);
				return false;
			}
			
			// -- Lecture de la rubrique du brut table 99
			// BEGIN
			// SELECT valm INTO pafnom.mnt1 FROM pafnom
			// WHERE cdos = cdos
			// AND ctab = 99
			// AND cacc = 'RUBBRUT'
			// AND nume = 1;
			// EXCEPTION
			// WHEN NO_DATA_FOUND THEN null;
			// END;
			//
			// IF SQL%NOTFOUND THEN
			// mess := PA_PAIE.centraservice.chercherMessage(request,'INF-10113',clang);
			// RETURN FALSE;
			// ELSE
			// Rbq_BRUT := ltrim(to_char(pafnom.mnt1,'0999'));
			// END IF;
			fnom = centraservice.chercherEnNomenclature(cdos, 99, "RUBBRUT", 1);
			if(fnom.getValm() != null)
			{
				Rbq_BRUT = StringUtils.leftPad(fnom.getValm().toString(), 4,'0');
			}
			else
			{
				mess = centraservice.chercherMessage(request,"INF-10113", clang);
				globalUpdate._setEvolutionTraitement(request, "INF-10113", true);
				return false;
			}
			
			//
			// -- Utilise-t-on la compta standard DELTA ?
			// IF comp = 'O' THEN
			// deltacom := TRUE;
			// ELSIF comp = 'N' THEN
			// deltacom := FALSE;
			// ELSE
			// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90089',clang);
			// RETURN FALSE ;
			// END IF;
			//
			if (comp == 'O')
			{
				deltacom = true;
			}
			else
			{
				if (comp == 'N')
				{
					deltacom = false;
				}
				else
				{
					mess = centraservice.chercherMessage(request,"ERR-90089", clang);
					globalUpdate._setEvolutionTraitement(request, "ERR-90089", true);
					ParameterUtil.println(mess);
					return false;
				}
			}
			// IF deltacom THEN
			// -- fait-on la centralisation de tous les dossiers sur un dossier
			// IF NOT centra_dossier (centra_dos,numdos_centra) THEN
			// RETURN FALSE;
			// END IF;
			// IF NOT centra_dos THEN
			// -- la lecture de tous les params et controle de la compta delta se font sur la var dossier numdos_centra
			// numdos_centra := cdos;
			// ELSE
			// -- Centralisation : no de piece = MM YYYY DOS.Origine
			// Num_Piece := substr(aamm,5,2)||substr(aamm,1,4)|| cdos;
			// END IF;
			// END IF;
			//
			// sp�cifique cnss
			//le bloc n'est pas ex�cut� pour la cnss
			if (StringUtil.notEquals(globalUpdate.nomClient, ClsEntreprise.CNSS))
			{
				if (deltacom)
				{
					if (!centra_dossier())
					{
						return false;
					}
					if (!centra_dos)
					{
						numdos_centra = cdos;
					}
					else
					{
						Num_Piece = aamm.substring(4, 6) + aamm.substring(0, 4) + cdos;
					}
				}
			}
			
			if (StringUtil.notEquals(globalUpdate.nomClient, ClsEntreprise.CNSS))
			{
				// -- Lecture du code journal de paie (JOURPAIE, table 99)
				// BEGIN
				// SELECT vall INTO pafnom.lib1 FROM pafnom
				// WHERE cdos = cdos
				// AND ctab = 99
				// AND cacc = 'JOURPAIE'
				// AND nume = 1;
				// EXCEPTION
				// WHEN NO_DATA_FOUND THEN null;
				// END;
				//
				fnom = centraservice.chercherEnNomenclature(cdos, 99, "JOURPAIE", 1);
				if(StringUtils.isNotBlank(fnom.getVall()))
				{
					Code_Journal = StringUtils.substring(fnom.getVall(), 0, 5);
				}
				else
				{
					mess = centraservice.chercherMessage(request,"ERR-90090", clang);
					globalUpdate._setEvolutionTraitement(request, "ERR-90090", true);
					return false;
				}
				
				// -- Lecture du code etablissement commun
				// SELECT vall INTO pafnom.lib2 FROM pafnom
				// WHERE cdos = cdos
				// AND ctab = 99
				// AND cacc = 'JOURPAIE'
				// AND nume = 2;
				// Cod_Etb_Commun := SUBSTR(pafnom.lib2,1,3);
				//
				fnom = centraservice.chercherEnNomenclature(cdos, 99, "JOURPAIE", 2);
				if(StringUtils.isNotBlank(fnom.getVall()))
				{
					Cod_Etb_Commun = StringUtils.substring(fnom.getVall(), 0, 3);
				}
				
				
				// -- Lecture du code axe 2 commun
				// SELECT vall INTO pafnom.lib3 FROM pafnom
				// WHERE cdos = cdos
				// AND ctab = 99
				// AND cacc = 'JOURPAIE'
				// AND nume = 3;
				// Dest2_Commune := SUBSTR(pafnom.lib3,1,8);
				//
				fnom = centraservice.chercherEnNomenclature(cdos, 99, "JOURPAIE", 3);
				if(StringUtils.isNotBlank(fnom.getVall()))
				{
					Dest2_Commune = StringUtils.substring(fnom.getVall(), 0, 8);
				}
				
				// -- Lecture du code libelle abrege
				// SELECT vall INTO pafnom.lib4 FROM pafnom
				// WHERE cdos = cdos
				// AND ctab = 99
				// AND cacc = 'JOURPAIE'
				// AND nume = 4;
				// Libelle_Abrege := SUBSTR(pafnom.lib4,1,2);
				//
				fnom = centraservice.chercherEnNomenclature(cdos, 99, "JOURPAIE", 4);
				if(StringUtils.isNotBlank(fnom.getVall()))
				{
					Libelle_Abrege = StringUtils.substring(fnom.getVall(), 0, 2);
				}
				
				// sp�cifique cnss
				if (StringUtils.equals(globalUpdate.nomClient, ClsEntreprise.CNSS))
				{
	//				Libelle_Abrege := SUBSTR(wfnom.lib3,1,2);
					Libelle_Abrege = StringUtil.oraSubstring(Dest2_Commune, 1, 2);
				}
				
				// sp�cifique cnss
				if (StringUtils.equals(globalUpdate.nomClient, ClsEntreprise.EDM))
				{
	//				Libelle_Abrege := SUBSTR(wfnom.lib3,1,2);
					Libelle_Abrege = StringUtil.oraSubstring(Dest2_Commune, 1, 2);
				}
			}
			
			// -- MM 05/12/1999
			// -- Chargement des parametres utilises pour interface DELTA - GL
			// IF NOT deltacom THEN
			// -- controle si interface gl
			// -- chargement du no de la table de parametrage
			// BEGIN
			// SELECT vall,valm INTO pafnom.lib1,pafnom.mnt1 FROM pafnom
			// WHERE cdos = cdos
			// AND ctab = 99
			// AND cacc = 'INTER-GL'
			// AND nume = 1;
			// EXCEPTION
			// WHEN NO_DATA_FOUND THEN null;
			// END;
			//
			if (!deltacom)
			{
				fnom = centraservice.chercherEnNomenclature(cdos, 99, "INTER-GL", 1);
				
				// IF SQL%NOTFOUND OR pa_paie.NouZ(pafnom.mnt1) THEN
				// interface_gl := FALSE;
				// ELSE
				// interface_gl := TRUE;
				// num_tab_gl := pafnom.mnt1;
				// cle_tab_gl := substr(pafnom.lib1,1,10);
				// END IF;
				//
				if(fnom == null || (fnom != null && StringUtils.isBlank(fnom.getCacc())) || fnom.getValm() == null)
				{
					interface_gl = false;
				}
				else
				{
					interface_gl = true;
					num_tab_gl = fnom.getValm().intValue();
					cle_tab_gl = StringUtils.substring(fnom.getVall(), 0, 10);
				}
				// IF interface_gl THEN
				// --- on charge les differentes zones infos salaries
				// --- pour le parametrage de la cle comptable GL
				// BEGIN
				// SELECT vall INTO pafnom.lib1 FROM pafnom
				// WHERE cdos = cdos
				// AND ctab = num_tab_gl
				// AND cacc = cle_tab_gl
				// AND nume = 1;
				// EXCEPTION
				// WHEN NO_DATA_FOUND THEN null;
				// END;
				//
				if (interface_gl)
				{
					for(int i=1; i<10; i++)
					{
						fnom = centraservice.chercherEnNomenclature(cdos, num_tab_gl, cle_tab_gl, i);
						if(StringUtils.isNotBlank(fnom.getVall()))
							Destination_gl[i] = fnom.getVall();
						// sp�cifique cnss
						if (StringUtils.equals(globalUpdate.nomClient, ClsEntreprise.CNSS))
						{
//							 IF SQL%NOTFOUND OR pa_paie.NouB(wfnom.lib1) THEN
//					            w_mess := 'Attention la table GL '||TO_CHAR(num_tab_gl)||' est vide (nomenclatures).';
//					            RETURN FALSE;
//					         END IF;  
							if(i == 1 && (fnom == null || StringUtils.isBlank(fnom.getVall())))
							{
								mess = "Attention la table GL '||TO_CHAR(num_tab_gl)||' est vide (nomenclatures).";
								globalUpdate._setEvolutionTraitement(request, mess, true);
								return false;
							}
						}
					}
					
					// Destination1_gl := RTRIM(LTRIM(pafnom.lib1));
					// pafnom.lib2 := null;
					// BEGIN
					// SELECT vall INTO pafnom.lib2 FROM pafnom
					// WHERE cdos = cdos
					// AND ctab = num_tab_gl
					// AND cacc = cle_tab_gl
					// AND nume = 2;
					// EXCEPTION
					// WHEN NO_DATA_FOUND THEN null;
					// END;
					//
					// Destination2_gl := RTRIM(LTRIM(pafnom.lib2));
					

					//
					// pafnom.lib3 := null;
					// BEGIN
					// SELECT vall INTO pafnom.lib3 FROM pafnom
					// WHERE cdos = cdos
					// AND ctab = num_tab_gl
					// AND cacc = cle_tab_gl
					// AND nume = 3;
					// EXCEPTION
					// WHEN NO_DATA_FOUND THEN null;
					// END;
					//
					// Destination3_gl := RTRIM(LTRIM(pafnom.lib3));
					//
					
					// pafnom.lib4 := null;
					// BEGIN
					// SELECT vall INTO pafnom.lib4 FROM pafnom
					// WHERE cdos = cdos
					// AND ctab = num_tab_gl
					// AND cacc = cle_tab_gl
					// AND nume = 4;
					// EXCEPTION
					// WHEN NO_DATA_FOUND THEN null;
					// END;
					//
					// Destination4_gl := RTRIM(LTRIM(pafnom.lib4));
					//
					
					// pafnom.lib5 := null;
					// BEGIN
					// SELECT vall INTO pafnom.lib5 FROM pafnom
					// WHERE cdos = cdos
					// AND ctab = num_tab_gl
					// AND cacc = cle_tab_gl
					// AND nume = 5;
					// EXCEPTION
					// WHEN NO_DATA_FOUND THEN null;
					// END;
					//
					// Destination5_gl := RTRIM(LTRIM(pafnom.lib5));
					//
					
					// pafnom.lib5 := null;
					// BEGIN
					// SELECT vall INTO pafnom.lib5 FROM pafnom
					// WHERE cdos = cdos
					// AND ctab = num_tab_gl
					// AND cacc = cle_tab_gl
					// AND nume = 6;
					// EXCEPTION
					// WHEN NO_DATA_FOUND THEN null;
					// END;
					//
					// Destination6_gl := RTRIM(LTRIM(pafnom.lib5));
					//
					
					// pafnom.lib5 := null;
					// BEGIN
					// SELECT vall INTO pafnom.lib5 FROM pafnom
					// WHERE cdos = cdos
					// AND ctab = num_tab_gl
					// AND cacc = cle_tab_gl
					// AND nume = 7;
					// EXCEPTION
					// WHEN NO_DATA_FOUND THEN null;
					// END;
					//
					// Destination7_gl := RTRIM(LTRIM(pafnom.lib5));
					//
					
					// pafnom.lib5 := null;
					// BEGIN
					// SELECT vall INTO pafnom.lib5 FROM pafnom
					// WHERE cdos = cdos
					// AND ctab = num_tab_gl
					// AND cacc = cle_tab_gl
					// AND nume = 8;
					// EXCEPTION
					// WHEN NO_DATA_FOUND THEN null;
					// END;
					//
					// Destination8_gl := RTRIM(LTRIM(pafnom.lib5));
					//
					
					// pafnom.lib5 := null;
					// BEGIN
					// SELECT vall INTO pafnom.lib5 FROM pafnom
					// WHERE cdos = cdos
					// AND ctab = num_tab_gl
					// AND cacc = cle_tab_gl
					// AND nume = 9;
					// EXCEPTION
					// WHEN NO_DATA_FOUND THEN null;
					// END;
					//
					// Destination9_gl := RTRIM(LTRIM(pafnom.lib5));
					//
					
					// IF pa_paie.NouB(Destination1_gl) AND
					// pa_paie.NouB(Destination2_gl) AND
					// pa_paie.NouB(Destination3_gl) AND
					// pa_paie.NouB(Destination4_gl) AND
					// pa_paie.NouB(Destination5_gl) AND
					// pa_paie.NouB(Destination6_gl) AND
					// pa_paie.NouB(Destination7_gl) AND
					// pa_paie.NouB(Destination8_gl) AND
					// pa_paie.NouB(Destination9_gl)
					// THEN
					// mess := 'Attention la table GL '||TO_CHAR(num_tab_gl)||' est vide (nomenclatures).';
					// RETURN FALSE;
					// END IF;
					//
					
					// sp�cifique cnss
					//bloc a ex�cuter ssi le client n'est pas cnss
					if (StringUtil.notEquals(globalUpdate.nomClient, ClsEntreprise.CNSS))
					{
						if (NouB(Destination_gl[1]) && NouB(Destination_gl[2]) && NouB(Destination_gl[3]) && NouB(Destination_gl[4]) && NouB(Destination_gl[5]) && NouB(Destination_gl[6]) && NouB(Destination_gl[7]) && NouB(Destination_gl[8]) && NouB(Destination_gl[9]))
						{
							mess = "Attention la table GL " + num_tab_gl + " est vide (nomenclatures).";
							globalUpdate._setEvolutionTraitement(request, mess, true);
							return false;
						}
					}
				}// END IF interface_gl
			}// END IF !deltacom
			//
			//
			// IF deltacom THEN
			// -- Controle existence du code journal
			// BEGIN
			// SELECT 'X' INTO exist FROM cp_jou
			// WHERE cdos = numdos_centra
			// AND codjou = Code_Journal;
			// EXCEPTION
			// WHEN NO_DATA_FOUND THEN null;
			// END;
			//
			// IF SQL%NOTFOUND THEN
			// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90091',clang,Code_Journal);
			// RETURN FALSE;
			// END IF;
			//
			if (deltacom)
			{
				String tmpLibJournal = StringUtils.EMPTY;
				// sp�cifique cnss
				//on n'a pas de numdos_centra, on utilise directement cdos
				if (StringUtils.equals(globalUpdate.nomClient, ClsEntreprise.CNSS))
					numdos_centra = cdos;
				
				if(StringUtils.isBlank(centraservice.chercherLibelleJournalFromCp_Jou(numdos_centra, Code_Journal)))
				{
					mess = centraservice.chercherMessage(request,"ERR-90091", clang, Code_Journal);
					globalUpdate._setEvolutionTraitement(request, mess, true);
					return false;
				}
				
				// -- Controle existence du code etablissement standard
				// IF NOT pa_paie.NouB(Cod_Etb_Commun) THEN
				// BEGIN
				// SELECT 'X' INTO exist FROM cp_ets
				// WHERE cdos = numdos_centra
				// AND codets = Cod_Etb_Commun;
				// EXCEPTION
				// WHEN NO_DATA_FOUND THEN null;
				// END;
				// IF SQL%NOTFOUND THEN
				// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90092',clang,Cod_Etb_Commun);
				// RETURN FALSE;
				// END IF;
				// END IF;
				//
				if (!NouB(Cod_Etb_Commun))
				{
					if(StringUtils.isBlank(centraservice.chercherLibelleEtablissementFromCpEt(numdos_centra, Cod_Etb_Commun)))
					{
						mess = centraservice.chercherMessage(request,"ERR-90092", clang, Cod_Etb_Commun);
						globalUpdate._setEvolutionTraitement(request, mess, true);
						return false;
					}
				}
				// -- Controle existence du code section axe 2 standard
				// -- ajout controle sur cnsa
				// IF NOT pa_paie.NouB(Dest2_Commune) THEN
				// BEGIN
				// SELECT etat, debsai, finsai
				// INTO Dest_Etat, Dest_debsai, Dest_finsai
				// FROM cp_des
				// WHERE cdos = numdos_centra
				// AND coddes = Dest2_Commune
				// AND numaxe = '2'
				// AND typdes = 'IMP';
				// EXCEPTION
				// WHEN NO_DATA_FOUND THEN null;
				// END;
				// IF SQL%NOTFOUND THEN
				// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90093',clang,Dest2_Commune);
				// RETURN FALSE;
				// ELSE
				// IF Dest_Etat = 'I'
				// THEN
				// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90094',clang,Dest2_Commune);
				// RETURN FALSE;
				// END IF;
				// IF Date_Comptable > Dest_finsai
				// THEN
				// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90095',clang,Dest2_Commune);
				// RETURN FALSE;
				// END IF;
				// IF Date_Comptable < Dest_debsai
				// THEN
				// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90096',clang,Dest2_Commune);
				// RETURN FALSE;
				// END IF;
				// END IF;
				// END IF
				if (!NouB(Dest2_Commune))
				{
					CpDevise cpde = centraservice.getInformationSectionFromCpDe(numdos_centra, Dest2_Commune, 2);
					if(cpde != null)
					{
						Dest_etat = CharUtils.toChar(cpde.getEtat());
						Dest_debsai = new ClsDate(cpde.getDebsai());
						Dest_finsai = new ClsDate(cpde.getFinsai());
						if (Dest_etat == 'I')
						{
							mess = centraservice.chercherMessage(request,"ERR-90094", clang, Dest2_Commune);
							globalUpdate._setEvolutionTraitement(request, mess, true);
							return false;
						}
						if (Date_Comptable.getDate().compareTo(Dest_debsai.getDate()) < 0)
						{
							mess = centraservice.chercherMessage(request,"ERR-90096", clang, Dest2_Commune);
							globalUpdate._setEvolutionTraitement(request, mess, true);
							return false;
						}
						if (Date_Comptable.getDate().compareTo(Dest_finsai.getDate()) > 0)
						{
							mess = centraservice.chercherMessage(request,"ERR-90095", clang, Dest2_Commune);
							globalUpdate._setEvolutionTraitement(request, mess, true);
							return false;
						}
					}
					else
					{
						mess = centraservice.chercherMessage(request,"ERR-90093", clang, Dest2_Commune);
						globalUpdate._setEvolutionTraitement(request, mess, true);
						return false;
					}

				}// END IF !NouB(Dest2_Commune);
				//
				// -- Controle existence du code libelle abrege
				// IF pa_paie.NouB(Libelle_Abrege)
				// THEN
				// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90097',clang);
				// RETURN FALSE;
				// ELSE
				// BEGIN
				// SELECT 'X'
				// INTO exist
				// FROM cp_abr
				// WHERE cdos = numdos_centra
				// AND codabr = Libelle_Abrege;
				// EXCEPTION
				// WHEN NO_DATA_FOUND THEN
				// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90098',clang,Libelle_Abrege);
				// RETURN FALSE;
				// END;
				// END IF;
				if (NouB(Libelle_Abrege))
				{
					mess = centraservice.chercherMessage(request,"ERR-90097", clang);
					globalUpdate._setEvolutionTraitement(request, mess, true);
					return false;
				}
				else
				{
					if(StringUtils.isBlank(centraservice.chercherLibelleAbbregeFromCpAbr(numdos_centra, Libelle_Abrege)))
					{
						mess = centraservice.chercherMessage(request,"ERR-90098", clang, Libelle_Abrege);
						globalUpdate._setEvolutionTraitement(request, mess, true);
						return false;
					}
				}
				//
			}// END IF deltacom;
			//
			// -- Code de la devise dossier
			// BEGIN
			// SELECT ddev
			// INTO Devise_Dossier
			// FROM cpdos
			// WHERE cdos = cdos;
			// EXCEPTION
			// WHEN NO_DATA_FOUND THEN
			// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90099',clang,cdos);
			// RETURN FALSE;
			// END;
			DossierPaie cpdo = centraservice.getCpdos(this.cdos);
			if(cpdo == null)
			{
				mess = centraservice.chercherMessage(request,"ERR-90099", clang, cdos);
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
			
			Devise_Dossier = cpdo.getDdev();
			
			if(StringUtils.isBlank(Devise_Dossier))
			{
				mess = centraservice.chercherMessage(request,"ERR-90100", clang, cdos);
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
			
			// IF PA_PAIE.NouB( Devise_Dossier ) THEN
			// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90100',clang,cdos);
			// RETURN FALSE;
			// END IF;
			//
			
			// -- Verification si des bulletins existent pour ce dossier
			// SELECT count(*) INTO nbbul FROM pacalc
			// WHERE cdos = cdos
			// AND aamm = aamm
			// AND nbul = nbul
			// AND rubq = Rbq_NAP;
			//
			// IF PA_PAIE.NouZ(nbbul) THEN
			// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90101',clang);
			// RETURN FALSE;
			// END IF;
			//
			nbbul = centraservice.getNbreBulFromRhtcalcul(this.cdos, this.aamm, this.nbul.toString(), Rbq_NAP);
			if(nbbul == 0)
			{
				mess = centraservice.chercherMessage(request,"ERR-90101", clang);
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
			
			// -- Verification si tous les bulletins ont ete edites
			// nbbul := 0;
			// OPEN curs_edibul;
			// LOOP
			// FETCH curs_edibul INTO edi_mat;
			// EXIT WHEN curs_edibul%NOTFOUND;
			//
			// mat_exist := NULL;
			// OPEN curs_bull_exist;
			// FETCH curs_bull_exist INTO mat_exist;
			// CLOSE curs_bull_exist;
			// IF mat_exist IS NOT NULL THEN
			// nbbul := nbbul + 1;
			// END IF;
			//
			// END LOOP;
			// IF nbbul > 0 THEN
			// mess := TO_CHAR( nbbul, '0009') || PA_PAIE.centraservice.chercherMessage(request,'ERR-90102',clang,edi_mat);
			// RETURN FALSE;
			// END IF;
			//
			nbbul = centraservice.getNbreBulNonEditeFromRhtcalcul(this.cdos, this.aamm, this.nbul.toString(), Rbq_NAP);
			if (nbbul > 0)
			{
				mess = centraservice.chercherMessage(request,"INF-10207", clang);
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
			
			
			String query1="From ParamData where idEntreprise = '"+cdos+"' and ctab = 99 and cacc='CENTRA-DES' order by nume";
			paramDestinations = centraservice.service.find(query1);
			
			// RETURN TRUE;
			//
			// END init;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			logMessage(ex.getMessage());
			return false;
		}
		return true;
	}
	List<ParamData> paramDestinations = new ArrayList<ParamData>();
	
	private void logMessage(Object objMessage)
	{
		if (objMessage != null)
			ParameterUtil.println(objMessage.toString());
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#centra_dossier()
	 */
	public boolean centra_dossier() throws Exception
	{
		ParameterUtil.println("Entr�e dans centra_dossier");
		boolean boolResultat = false;
		String query = null;
		// FUNCTION centra_dossier (o_centra OUT BOOLEAN,o_dos OUT VARCHAR2) RETURN BOOLEAN
		// IS
		//
		// lib PAFNOM.VALL%TYPE;
		// dos PAFNOM.VALL%TYPE;
		String lib, dos = null;

		//
		// BEGIN
		//
		// BEGIN
		// SELECT SUBSTR(vall,1,1) INTO lib
		// FROM pafnom
		// WHERE cdos = cdos
		// AND ctab = 99
		// AND cacc = 'DOS-CENTRA'
		// AND nume = 1;
		// EXCEPTION
		// WHEN NO_DATA_FOUND THEN
		// lib := 'N';
		// END;
		//
		lib = centraservice.chercherEnNomenclature(this.cdos, 99, "DOS-CENTRA", 1).getVall();
		if(StringUtils.isNotBlank(lib))
			lib = StringUtils.substring(lib, 0, 1);
		else
			lib="N";
		
		// BEGIN
		// SELECT SUBSTR(vall,1,2) INTO dos
		// FROM pafnom
		// WHERE cdos = cdos
		// AND ctab = 99
		// AND cacc = 'DOS-CENTRA'
		// AND nume = 2;
		// EXCEPTION
		// WHEN NO_DATA_FOUND THEN
		// dos := NULL;
		// END;
		dos = centraservice.chercherEnNomenclature(this.cdos, 99, "DOS-CENTRA", 2).getVall();
		if(StringUtils.isNotBlank(dos))
			dos = StringUtils.substring(dos, 0, 2);
		else
			dos= null;
		
		//
		// IF UPPER(lib) = 'O' AND PA_PAIE.NouB(dos) THEN
		// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-30165',clang,'DOS-CENTRA','99','2');
		// o_centra := FALSE;
		// o_dos := NULL;
		// RETURN FALSE;
		// END IF;
		//
		if (lib.compareToIgnoreCase("O") == 0 && NouB(dos))
		{
			mess = centraservice.chercherMessage(request,"ERR-30165", clang, "DOS-CENTRA", "99", "2");
			globalUpdate._setEvolutionTraitement(request, mess, true);
			centra_dos = false;
			numdos_centra = null;
			return false;
		}
		// IF UPPER(lib) = 'O' THEN
		// o_centra := TRUE;
		// ELSE
		// o_centra := FALSE;
		// END IF;
		// o_dos := dos;
		//
		// RETURN TRUE;
		//
		centra_dos = (lib.compareToIgnoreCase("O") == 0);
		numdos_centra = dos;
		return true;
		// END centra_dossier;
	}

	private boolean NouB(Object obj)
	{
		return ClsObjectUtil.isNull(obj);
	}

	private boolean NouZ(Object obj)
	{
		return ClsObjectUtil.isNull(obj);
	}

	

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#lect_rub_cal(com.cdi.deltarh.entites.Rhpagent)
	 */
	public boolean lect_rub_cal(Salarie salarie) throws Exception
	{
		ParameterUtil.println("Entr�e dans lect_rub_cal");
		String char_deb, char_cre, query;
		List<CalculPaie> list = null;

		Total_Credit = BigDecimal.ZERO;
		Total_Debit = BigDecimal.ZERO;
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
		Total_Credit = ClsStringUtil.truncateTo3Decimal(Total_Credit);
		Total_Debit = ClsStringUtil.truncateTo3Decimal(Total_Debit);
		BigDecimal diff = new BigDecimal("0.001");
		fnom = centraservice.chercherEnNomenclature(this.getCdos(), 99, "ECART-CLOT", 1);
		if (fnom!=null && fnom.getValt() != null)
			diff = fnom.getValt();
		wecart = Total_Credit.subtract(Total_Debit);
		if (wecart.compareTo(diff)> 0)
		{
			wecart = Total_Debit.subtract(Total_Credit);
			char_deb = format_mt(Total_Debit.doubleValue(), nddd, 13, true);
			char_cre = format_mt(Total_Credit.doubleValue(), nddd, 13, true);
			mess = centraservice.chercherMessage(request,"ERR-90103", clang, salarie.getNmat(), char_deb, char_cre);
			globalUpdate._setEvolutionTraitement(request, mess, true);
			return false;
		}
		return true;
		// FUNCTION lect_rub_cal RETURN BOOLEAN IS
		//
		// rowid ROWID;
		// ind_rev SMALLINT;
		// char_deb VARCHAR(15);
		// char_cre VARCHAR2(15);
		//
		// CURSOR curs_cal IS SELECT pacalc.* FROM pacalc
		// WHERE cdos = cdos
		// AND aamm = aamm
		// AND nmat = salarie.nmat
		// AND nbul = nbul
		// ORDER BY cdos, aamm, nmat, nbul, rubq;
		// BEGIN
		// -- Total d�bit, Total cr�dit
		// Total_Debit := 0;
		// Total_Credit := 0;
		//
		// OPEN curs_cal;
		// LOOP
		// FETCH curs_cal INTO calcul;
		// EXIT WHEN curs_cal%NOTFOUND;
		//
		// IF calcul.mont != 0 THEN
		// IF NOT ven_ecr THEN
		// CLOSE curs_cal;
		// RETURN FALSE;
		// END IF;
		// END IF;
		// END LOOP;
		//
		// CLOSE curs_cal;
		//
		// -- Message d'erreur si ecart debit/credit
		// IF Total_Debit != Total_Credit THEN
		// wecart := Total_Debit - Total_Credit;
		// char_deb := pa_paie.format_mt(Total_Debit,nddd,13,TRUE);
		// char_cre := pa_paie.format_mt(Total_Credit,nddd,13,TRUE);
		// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90103',clang,salarie.nmat,char_deb,char_cre);
		// RETURN FALSE;
		// END IF;
		//
		// RETURN TRUE;
		//
		// END lect_rub_cal;
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
		// FUNCTION Format_Mt (mt IN NUMBER, ndc IN NUMBER,
		// long_ret IN NUMBER, Avec_Zero IN BOOLEAN) RETURN VARCHAR2
		// IS
		//
		// mtf1 CHAR(20);
		// c20 VARCHAR2(20);
		//
		String mtf1 = null;
		String c20 = null;
		// BEGIN

		// IF Avec_Zero THEN
		// IF ndc = 3 THEN
		// mtf1 := LPAD(LTRIM(TO_CHAR(NVL(mt,0), '999G999G999G990D999')),20,' ');
		// ELSIF ndc = 2 THEN
		// mtf1 := LPAD(LTRIM(TO_CHAR(NVL(mt,0),'9G999G999G999G990D99')),20,' ');
		// ELSIF ndc = 1 THEN
		// mtf1 := LPAD(LTRIM(TO_CHAR(NVL(mt,0),'99G999G999G999G990D9')),20,' ');
		// ELSE
		// mtf1 := LPAD(LTRIM(TO_CHAR(NVL(mt,0), '999G999G999G999G990')),20,' ');
		// END IF;
		// ELSE
		// IF NOT NouZ(mt) THEN
		// IF ndc = 3 THEN
		// mtf1 := LPAD(LTRIM(TO_CHAR(NVL(mt,0), '999G999G999G990D999')),20,' ');
		// ELSIF ndc = 2 THEN
		// mtf1 := LPAD(LTRIM(TO_CHAR(NVL(mt,0),'9G999G999G999G990D99')),20,' ');
		// ELSIF ndc = 1 THEN
		// mtf1 := LPAD(LTRIM(TO_CHAR(NVL(mt,0),'99G999G999G999G990D9')),20,' ');
		// ELSE
		// mtf1 := LPAD(LTRIM(TO_CHAR(NVL(mt,0), '999G999G999G999G990')),20,' ');
		// END IF;
		// ELSE
		// mtf1 := LPAD(' ',20,' ');
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
		// c20 := ' ';
		// c20 := SUBSTR(mtf1,20-long_ret+1);
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
	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#ven_ecr(com.cdi.deltarh.entites.Rhpagent)
	 */
	public boolean ven_ecr(Salarie salarie) throws Exception
	{
		ParameterUtil.println("Entr�e dans ven_ecr");
		// sp�cifique cnss
		if (StringUtils.equals(globalUpdate.nomClient, ClsEntreprise.CNSS))
		{
//			  --LM 27/10/00 Initialisation
//			  w_perso:='N';
//			-- LM 13/01/2009 Specif CNSS
//			  w_tiers := 'N';
//			-- LM 13/01/2009 Specif CNSS
			perso = "N";
			tiers = "N";
		}
		char4 = StringUtils.isBlank(calcul.getRuba()) ? calcul.getRubq() : calcul.getRuba();
		rubrique = centraservice.getInfoRubriqueFromRhprubrique(this.getCdos(), char4);
		if (rubrique == null)
		{
			mess = centraservice.chercherMessage(request,"ERR-90104", clang, salarie.getNmat(), char4);
			globalUpdate._setEvolutionTraitement(request, mess, true);
			return false;
		}

		if (!"O".equalsIgnoreCase(rubrique.getComp()))
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
				mess = centraservice.chercherMessage(request,"ERR-90105", clang, calcul.getRubq(), rubrique.getTypr());
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
		}
		else
			Type_Rubq = 2;
		
		if(StringUtils.equalsIgnoreCase("O", rubrique.getCper()))
		{
			if(StringUtils.isBlank(calcul.getNprt()))
			{
				if (StringUtils.isBlank(salarie.getCcpt()))
				{
					mess = centraservice.chercherMessage(request,"ERR-90106", clang, salarie.getNmat(), calcul.getRubq());
					globalUpdate._setEvolutionTraitement(request, mess, true);
					return false;
				}
				Num_compte = salarie.getCcpt();
			}
			else
			{
				Num_compte = centraservice.getNcptFromRhtpretEnt(this.getCdos(), calcul.getNprt());
				if(StringUtils.isBlank(Num_compte))
				{
					if(StringUtils.isBlank(salarie.getCcpt()))
					{
						mess = centraservice.chercherMessage(request,"ERR-90106", clang, salarie.getNmat(), calcul.getRubq());
						globalUpdate._setEvolutionTraitement(request, mess, true);
						return false;
					}
					Num_compte = salarie.getCcpt();
				}
			}

			// FUNCTION ven_ecr RETURN BOOLEAN IS
			//
			// BEGIN
			//
			// -- On charge la rubrique lue OU la rubrique associee
			// IF NOT PA_PAIE.NouB(calcul.ruba) THEN
			// 	char4 := calcul.ruba;
			// ELSE
			// 	char4 := calcul.rubq;
			// END IF;
			//
			// BEGIN
			// 	SELECT * INTO rubrique FROM parubq
			// 	WHERE cdos = cdos
			// 	AND crub = char4;
			// 	EXCEPTION
			// 	WHEN NO_DATA_FOUND THEN null;
			// END;
			//
			// IF SQL%NOTFOUND THEN
			// 	mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90104',clang,salarie.nmat,calcul.ruba);
			// 	RETURN FALSE;
			// END IF;
			//
			// IF rubrique.comp != 'O' THEN
			// 	RETURN TRUE;
			// END IF;
			//
			//
			// IF rubrique.crub != Rbq_NAP THEN
			// 	-- Lecture type rubrique (Gain, Retenue ou Part patronnale)
			// 	BEGIN
			// 		SELECT valm INTO Type_Rubq FROM pafnom
			// 		WHERE cdos = cdos
			// 		AND ctab = 28
			// 		AND cacc = rubrique.typr
			// 		AND nume = 1;
			// 		EXCEPTION
			// 		WHEN NO_DATA_FOUND THEN null;
			// 	END;
			//
			// 	IF SQL%NOTFOUND THEN
			// 		mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90105',clang,calcul.rubq,rubrique.typr);
			// 		RETURN FALSE;
			// 	END IF;
			// ELSE
			// 	Type_Rubq := 2;
			// END IF;
			//
			// IF rubrique.cper = 'O' THEN
			// -- Rubrique a comptabiliser sur compte personnel
			// IF PA_PAIE.NouB(calcul.nprt) THEN
			// IF PA_PAIE.NouB(salarie.ccpt) THEN
			// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90106',clang,salarie.nmat,calcul.rubq);
			// RETURN FALSE;
			// END IF;
			// Num_Compte := salarie.ccpt;
			// ELSE
			// Num_Compte := ' ';
			// BEGIN
			// SELECT ncpt INTO Num_Compte FROM paprent
			// WHERE cdos = cdos
			// AND nprt = TO_NUMBER(calcul.nprt);
			// EXCEPTION
			// WHEN NO_DATA_FOUND THEN null;
			// END;
			//
			// IF PA_PAIE.NouB(Num_Compte) THEN
			// IF PA_PAIE.NouB(salarie.ccpt) THEN
			// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90106',clang,salarie.nmat,calcul.rubq);
			// RETURN FALSE;
			// END IF;
			// Num_Compte := salarie.ccpt;
			// END IF;
			// END IF;
			//
			if (deltacom)
			{
				Compte_OK(Num_compte);
				if (!Existence_Compte)
				{
					mess = centraservice.chercherMessage(request,"ERR-90107", clang) + salarie.getNmat();
					globalUpdate._setEvolutionTraitement(request, mess, true);
					return false;
				}
				for(int i=1; i<10; i++)
				{
					// sp�cifique cnss
					//on s'arr�te � l'axe 2
					if (StringUtils.equals(globalUpdate.nomClient, ClsEntreprise.CNSS))
					{
						if(i == 3)
							break;
					}
					if (Suivi_Analytique && !Destination_Existe[i])
					{
						mess = centraservice.chercherMessage(request,"ERR-90108", clang, String.valueOf(i), Destination[i], salarie.getNmat(), calcul.getRubq());
						globalUpdate._setEvolutionTraitement(request, mess, true);
						return false;
					}
				}
			}
			
			// sp�cifique cnss
			if (StringUtils.equals(globalUpdate.nomClient, ClsEntreprise.CNSS))
			{
//				 Num_Tiers := Num_Compte;
//			     Num_Compte := ' ';
				Num_Tiers = Num_compte;
				Num_compte =" ";
//	
//			     IF NOT rech_cpt(wrubq.crub) THEN
//			        RETURN FALSE;
//			     END IF;
				if(! rech_cpt(rubrique.getCrub(), salarie))
					return false;
			}
			// -- Ctle existence du compte et de la section en compta
			// IF deltacom
			// 	THEN
			// 		Compte_OK(Num_Compte, Existence_Compte, Suivi_Analytique);
			// 		IF NOT Existence_Compte THEN
			// 			mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90107',clang)|| salarie.nmat;
			// 			RETURN FALSE;
			//		 END IF;
			//
			// 		IF Suivi_Analytique AND NOT Destination1_Existe
			// 			THEN
			// 				mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90108',clang,'1',Destination1,salarie.nmat,calcul.rubq);
			// 				RETURN FALSE;
			// 			END IF;
			//
			// 		IF Suivi_Analytique AND NOT Destination2_Existe THEN
			// 			mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90108',clang,'2',Destination2,salarie.nmat,calcul.rubq);
			// 			RETURN FALSE;
			// 		END IF;
			//
			// 		IF Suivi_Analytique AND NOT Destination3_Existe THEN
			// 			mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90108',clang,'3',Destination3,salarie.nmat,calcul.rubq);
			// 			RETURN FALSE;
			// 		END IF;
			//
			// 		IF Suivi_Analytique AND NOT Destination4_Existe THEN
			// 			mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90108',clang,'4',Destination4,salarie.nmat,calcul.rubq);
			// 			RETURN FALSE;
			// 		END IF;
			//
			// 		IF Suivi_Analytique AND NOT Destination5_Existe THEN
			// 			mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90108',clang,'5',Destination5,salarie.nmat,calcul.rubq);
			// 			RETURN FALSE;
			// 		END IF;
			//
			// 		IF Suivi_Analytique AND NOT Destination6_Existe THEN
			// 			mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90108',clang,'6',Destination6,salarie.nmat,calcul.rubq);
			// 			RETURN FALSE;
			// 		END IF;
			//
			// 		IF Suivi_Analytique AND NOT Destination7_Existe THEN
			// 			mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90108',clang,'7',Destination7,salarie.nmat,calcul.rubq);
			// 			RETURN FALSE;
			// 		END IF;
			//
			// 		IF Suivi_Analytique AND NOT Destination8_Existe THEN
			// 			mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90108',clang,'8',Destination8,salarie.nmat,calcul.rubq);
			// 			RETURN FALSE;
			// 		END IF;
			//
			// 		IF Suivi_Analytique AND NOT Destination9_Existe THEN
			// 			mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90108',clang,'9',Destination9,salarie.nmat,calcul.rubq);
			// 			RETURN FALSE;
			// 		END IF;
			//
			// END IF;
			//
			
			// sp�cifique cnss
			if (StringUtil.notEquals(globalUpdate.nomClient, ClsEntreprise.CNSS))
			{
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
			}
			// IF Type_Rubq = 1 THEN
			// sens := 'D';
			// IF NOT ch_rub THEN
			// RETURN FALSE;
			// END IF;
			// ELSIF Type_Rubq = 2 THEN
			// sens := 'C';
			// IF NOT ch_rub THEN
			// RETURN FALSE;
			// END IF;
			// ELSIF Type_Rubq = 3 THEN
			// sens := 'D';
			// IF NOT ch_rub THEN
			// RETURN FALSE;
			// END IF;
			// sens := 'C';
			// IF NOT ch_rub THEN
			// RETURN FALSE;
			// END IF;
			// END IF;
		}// END IF rubrique.cper = 'O' THEN
		else
		{
			// sp�cifique cnss
			if (StringUtils.equals(globalUpdate.nomClient, ClsEntreprise.CNSS))
			{
//				Num_Tiers := ' ';
				Num_Tiers = " ";
			}
			if (!rech_cpt(rubrique.getCrub(), salarie))
			{
				return false;
			}
		}

		// ELSE
		// -- Rubrique a comptabiliser sur compte ecran 7
		// IF NOT rech_cpt(rubrique.crub) THEN
		// RETURN FALSE;
		// END IF;
		// END IF;
		//
		// RETURN TRUE;
		//
		return true;
	}// END ven_ecr;

	

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#Compte_OK(java.lang.String)
	 */
	public void Compte_OK(String Num_Cpt)
	{
		ParameterUtil.println("Entr�e dans Compte_OK");
		String Profil = "";
		char Gest_anal;
		// PROCEDURE Compte_OK(Num_Cpt IN cp_cpt.numcpt%TYPE,
		// Compte_Exist OUT BOOLEAN,
		// Suivi_anal OUT BOOLEAN)
		// IS
		//
		// Gest_anal cp_pro.gesana%TYPE;
		// Profil cp_cpt.codpro%TYPE;
		//

		// BEGIN
		// -- Test existence compte
		// -- MM 22/04/2005 le compte saisie dans les rubriques est soit dans Compte ou cp_aux
		//
		// BEGIN
		// sp�cifique cnss
		if (StringUtils.equals(globalUpdate.nomClient, ClsEntreprise.CNSS))
		{
			numdos_centra = cdos;
		}
		Profil = centraservice.getCodproFromCp_Cpt(numdos_centra, Num_Cpt);
		if(StringUtils.isBlank(Profil))
			exist_cpt = false;
		else
			exist_cpt = true;
		

		// SELECT codpro
		// INTO Profil
		// FROM cp_cpt
		// WHERE cdos = numdos_centra
		// AND numcpt = Num_cpt;
		// EXCEPTION
		// WHEN NO_DATA_FOUND THEN null;
		// END;
		// IF SQL%NOTFOUND THEN
		// exist_cpt := FALSE;
		// ELSE
		// exist_cpt := TRUE;
		// END IF;
		exist_aux = false;
		
		// sp�cifique cnss
		if (StringUtil.notEquals(globalUpdate.nomClient, ClsEntreprise.CNSS))
		{
			if (!exist_cpt)
			{
				Cpt_numcpt = centraservice.getNumcptFromCpAux(numdos_centra, Num_Cpt);
				if(StringUtils.isBlank(Cpt_numcpt))
					exist_aux = true;
				else
					exist_aux = false;
				
				if (exist_aux)
				{
					Profil = centraservice.getCodproFromCp_Cpt(numdos_centra, Cpt_numcpt);
					if(StringUtils.isBlank(Profil))
						exist_cpt = false;
					else
						exist_cpt = true;
	
					if (exist_aux && exist_cpt)
					{
						Num_Tiers = Num_compte;
						Num_compte = Cpt_numcpt;
					}
				}
			}
		}

		// exist_aux := FALSE;
		//
		// IF NOT exist_cpt THEN
		//
		// BEGIN
		// SELECT numcpt
		// INTO cpt_numcpt
		// FROM cp_aux
		// WHERE cdos = numdos_centra
		// AND numtie = Num_cpt;
		// EXCEPTION
		// WHEN NO_DATA_FOUND THEN null;
		// END;
		// IF SQL%NOTFOUND THEN
		// exist_aux := FALSE;
		// ELSE
		// exist_aux := TRUE;
		// END IF;
		//
		// IF exist_aux THEN
		// BEGIN
		// SELECT codpro
		// INTO Profil
		// FROM cp_cpt
		// WHERE cdos = numdos_centra
		// AND numcpt = cpt_numcpt;
		// EXCEPTION
		// WHEN NO_DATA_FOUND THEN null;
		// END;
		// IF SQL%NOTFOUND THEN
		// exist_cpt := FALSE;
		// ELSE
		// exist_cpt := TRUE;
		// END IF;
		//
		// -- MM 12/05/2005
		// IF exist_cpt AND exist_aux THEN
		// -- si compte aux. alors on charge le compte collectif dans Num_compte et le compte aux dans Num_Tiers.
		// Num_Tiers := Num_Compte;
		// Num_Compte := Cpt_numcpt;
		// END IF;
		//
		// END IF;
		//
		// END IF;
		//
		if (!exist_cpt)
		{
			Existence_Compte = false;
			Suivi_Analytique = false;
		}
		else
		{
			//CpPro cppro = centraservice.getCpProFromCpPro(this.cdos, Profil);
			Produit cppro = centraservice.getCpProFromCpPro(this.numdos_centra, Profil);
			
			if(cppro !=null)
			{
				Gest_anal =CharUtils.toChar(cppro.getGesana());
				if (Gest_anal != 'N')
				{
					Existence_Compte = true;
					Suivi_Analytique = true;
				}
				else
				{
					Existence_Compte = true;
					Suivi_Analytique = false;
				}
			}
			else
			{
				Existence_Compte = false;
				Suivi_Analytique = false;
			}
		}
		// IF NOT exist_cpt THEN
		// Compte_Exist := FALSE;
		// Suivi_anal := FALSE;
		// ELSE
		// -- Lecture profil, pour suivi analytique
		// BEGIN
		// SELECT gesana
		// INTO Gest_anal
		// FROM cp_pro
		// WHERE cdos = numdos_centra
		// AND codpro = Profil;
		// EXCEPTION
		// WHEN NO_DATA_FOUND THEN null;
		// END;
		// IF SQL%NOTFOUND THEN
		// Compte_Exist := FALSE;
		// Suivi_anal := FALSE;
		// ELSE
		// IF Gest_anal != 'N' THEN
		// Compte_Exist := TRUE;
		// Suivi_anal := TRUE;
		// ELSE
		// Compte_Exist := TRUE;
		// Suivi_anal := FALSE;
		// END IF;
		// END IF;
		// END IF;
		//
		// END Compte_OK;
	}
	
	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#ch_rubBDT(com.cdi.deltarh.entites.Rhpagent)
	 */
	public boolean ch_rubBDT(Salarie salarie) throws Exception
	{
		ParameterUtil.println("Entr�e dans ch_rub");
		wint = new InterfComptable();
		List list = null;
		String Cpt_Profil = null, Cpt_Nature = null, query;
		char Gest_Anal = ' ', Gest_Lettrage = ' ';
		int nb_aux = 0;

		//Cpt_Type = ' ';

		if(! StringUtils.equals("O",StringUtil.nvl(rubrique.getCper(), "N")))
		{
			if (!cpt_cherche_lettre(salarie, mess))
			{
				return false;
			}
		}

		
		
		Libelle_Ecriture = StringUtils.substring(rubrique.getLrub(),0,22) +" "+ StringUtils.substring(aamm,4,6) +"/"+StringUtils.substring(aamm,0,4);
		
		
		if (deltacom)
		{

			exist_aux = false;
			exist_cpt = true;
			Compte cpcpt = centraservice.getCp_Cpt(numdos_centra, Num_compte);
			if(cpcpt != null)
			{
				Cpt_Profil = cpcpt.getCodpro();
				Cpt_Nature = cpcpt.getCodnat();
				Cpt_Type = CharUtils.toChar(cpcpt.getTypcpt());
			}
			else
			{
				exist_cpt = false;
			}
			
			if (!exist_cpt)
			{
				exist_aux = true;
				Cpt_numcpt = centraservice.getNumcptFromCpAux(numdos_centra, Num_compte);
				if(StringUtils.isBlank(Cpt_numcpt))
					exist_aux = false;
				
				exist_cpt = true;

				cpcpt = centraservice.getCp_Cpt(numdos_centra, Cpt_numcpt);
				if(cpcpt != null)
				{
					Cpt_Profil = cpcpt.getCodpro();
					Cpt_Nature = cpcpt.getCodnat();
					Cpt_Type = CharUtils.toChar(cpcpt.getTypcpt());
				}
				else
				{
					exist_cpt = false;
				}
				
				if (!exist_cpt && !exist_aux)
				{
					mess = centraservice.chercherMessage(request,"ERR-90121", clang, salarie.getNmat(), calcul.getRubq(), Num_compte);
					globalUpdate._setEvolutionTraitement(request, mess, true);
					return false;
				}
				Num_Tiers = Num_compte;
				Num_compte = Cpt_numcpt;
			}
			Produit cppro = centraservice.getCpProFromCpPro(numdos_centra, Cpt_Profil);
			if(cppro != null)
			{
				Gest_Anal = CharUtils.toChar(cppro.getGesana());
				Gest_Lettrage = CharUtils.toChar(cppro.getGeslet());
			}
			else
			{
				mess = centraservice.chercherMessage(request,"ERR-90122", clang, calcul.getRubq(), Num_compte, Cpt_Profil);
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
			
			if (!exist_aux)
			{
				if (Cpt_Type == 'C')
				{
					nb_aux = centraservice.getNbreCpAuxFromCpAux(numdos_centra, Num_compte, Num_Tiers);
					
					if (nb_aux == 0)
					{
						mess = centraservice.chercherMessage(request,"ERR-90123", clang, salarie.getNmat(), calcul.getRubq(), Num_Tiers);
						globalUpdate._setEvolutionTraitement(request, mess, true);
						return false;
					}
				}
			}

			if (Gest_Lettrage == 'O')
				wint.setReflet(StringUtils.substring(Lib_Mois, 0, 8));
			else
				wint.setReflet(null);
		}
		else
		{
			Gest_Anal = 'N';
			wint.setReflet(null);
		}
		Mnt_Ecriture = BigDecimal.ZERO;

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
				mess = centraservice.chercherMessage(request,"ERR-90124", clang, salarie.getNmat(), calcul.getRubq());
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
		}

		if (!chargerEtablissement(salarie, rubrique.getCrub()))
		{
			return false;
		}
		if (deltacom)
		{
			wint.setIdEntreprise(new Integer(numdos_centra));
		}
		else
		{
			wint.setIdEntreprise(new Integer(cdos));
		}
		wint.setCodets(Code_Etablissement);
		wint.setCodjou(Code_Journal);

		// *****************************************************************
		
		if (!deltacom)
			wint.setNumcpt(StringUtils.substring(Num_compte, 0, 8));
		else
			wint.setNumcpt(Num_compte);
		
		if (Cpt_Type == 'C')
		{
			wint.setNumtie(Num_Tiers);
		}
		else
		{
			wint.setNumtie(null);
		}
		

		wint.setNumpce(Num_Piece);
		wint.setDatcpt(Date_Comptable.getDate());
		wint.setDatech(Date_Comptable.getDate());
		wint.setDatpce(Date_Comptable.getDate());
		wint.setDevpce(Devise_Dossier);
		wint.setQuantite(null);
		wint.setSens(sens + "");
		//wint.setPceMt(new BigDecimal(Mnt_Ecriture));
		wint.setPceMt(ClsStringUtil.truncateToXDecimal(Mnt_Ecriture,nddd));
		wint.setCoddes1(Destination[1]);
		wint.setCoddes2(Destination[2]);
		wint.setCoddes3(null);
		wint.setCoddes4(null);
		wint.setCoddes5(null);
		wint.setCoddes6(null);
		wint.setCoddes7(null);
		wint.setCoddes8(null);
		wint.setCoddes9(null);
		wint.setLibecr(Libelle_Ecriture);
		wint.setCodtre(null);
		wint.setCodabr(Libelle_Abrege);
		wint.setCoderr(null);
		wint.setCreationDate(Instant.now());
		wint.setLastModifiedDate(Instant.now());
		wint.setCoduti(cuti);

		if (Gest_Anal == 'N' && deltacom)
		{
			wint.setCoddes1(null);
			wint.setCoddes2(null);
			wint.setCoddes3(null);
			wint.setCoddes4(null);
			wint.setCoddes5(null);
			wint.setCoddes6(null);
			wint.setCoddes7(null);
			wint.setCoddes8(null);
			wint.setCoddes9(null);
		}

		if (!deltacom && interface_gl)
		{
			wint.setCoddes1(Destination[1]);
			wint.setCoddes2(Destination[2]);
			wint.setCoddes3(Destination[3]);
			wint.setCoddes4(Destination[4]);
			wint.setCoddes5(Destination[5]);
			wint.setCoddes6(Destination[6]);
			wint.setCoddes7(Destination[7]);
			wint.setCoddes8(Destination[8]);
			wint.setCoddes9(Destination[9]);
		}
		
		try
		{
			/* on ajoute un compteur pour differencier les lignes de cpint*/
			ClsCentralisation.compteur +=1;
			wint.setLiberr(String.valueOf(ClsCentralisation.compteur));
			session.save(wint);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			mess = centraservice.chercherMessage(request,"ERR-90125", clang, salarie.getNmat(), calcul.getRubq());
			globalUpdate._setEvolutionTraitement(request, mess, true);
			logMessage(ex.getMessage());
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#ch_rub(com.cdi.deltarh.entites.Rhpagent)
	 */
	public boolean ch_rub(Salarie salarie) throws Exception
	{
		ParameterUtil.println("Entr�e dans ch_rub");
		
		if (StringUtils.equals(globalUpdate.nomClient, ClsEntreprise.CNSS))
		{
			return ch_rubCNSS(salarie);
		}
		wint = new InterfComptable();
		List list = null;
		String Cpt_Profil = null, Cpt_Nature = null, query;
		char Gest_Anal = ' ', Gest_Lettrage = ' ';
		int nb_aux = 0;
		// FUNCTION ch_rub RETURN BOOLEAN IS
		//
		// Cpt_Profil cp_cpt.codpro%TYPE;
		// Cpt_Nature cp_cpt.codnat%TYPE;
		// Gest_Anal cp_pro.gesana%TYPE;
		// Gest_Lettrage cp_pro.geslet%TYPE;
		// nb_aux NUMBER;
		//
		// BEGIN
		//
		Cpt_Type = ' ';
		// Cpt_Type := '';
		//
		
		if(! StringUtils.equals("O",StringUtil.nvl(rubrique.getCper(), "N")))
		//if (rubrique.getCper().compareToIgnoreCase("N") != 0)
		{
			if (!cpt_cherche_lettre(salarie, mess))
			{
				return false;
			}
		}

		// -- Recherche si matricule ou niveau a integrer dans le numero de compte
		// IF NVL(rubrique.cper,'N') != 'O' THEN
		// IF NOT cpt_cherche_lettre THEN
		// RETURN FALSE;
		// END IF;
		// END IF;
		//
		// -- MM 23/09/2000 Ajout libelle ecriture = Lib rubrique + aamm (specif comilog)
		// Libelle_Ecriture := substr(rubrique.lrub,1,22) ||' '|| substr(aamm,5,2)||'/'||substr(aamm,1,4);
		//
		// *
		// IF deltacom THEN
		// -- Ajout code lettrage
		// -- Lecture du compte
		// exist_aux := FALSE;
		// exist_cpt := TRUE;
		// BEGIN
		// SELECT codpro, codnat, typcpt
		// INTO Cpt_Profil, Cpt_Nature, Cpt_Type
		// FROM cp_cpt
		// WHERE cdos = numdos_centra
		// AND numcpt = Num_Compte;
		// EXCEPTION
		// WHEN NO_DATA_FOUND THEN
		// exist_cpt := FALSE;
		// END;
		//
		// --- MM 22/04/2005 si pas dans Compte on regarde si compte auxilliaire
		// IF NOT exist_cpt THEN
		//
		// exist_aux := TRUE;
		// BEGIN
		// SELECT numcpt
		// INTO Cpt_numcpt
		// FROM cp_aux
		// WHERE cdos = numdos_centra
		// AND numtie = Num_Compte;
		// EXCEPTION
		// WHEN NO_DATA_FOUND THEN
		// exist_aux := FALSE;
		// END;
		//
		//		         
		// END IF;
		
		
		
		//sp�cifique EDM
		if (StringUtils.equals(globalUpdate.nomClient, ClsEntreprise.EDM))
		{
			
		}
		else
		{
			Libelle_Ecriture = StringUtils.substring(rubrique.getLrub(),0,22) +" "+ StringUtils.substring(aamm,4,6) +"/"+StringUtils.substring(aamm,0,4);
		}
		
		if (deltacom)
		{

			exist_aux = false;
			exist_cpt = true;
			Compte cpcpt = centraservice.getCp_Cpt(numdos_centra, Num_compte);
			if(cpcpt != null)
			{
				Cpt_Profil = cpcpt.getCodpro();
				Cpt_Nature = cpcpt.getCodnat();
				Cpt_Type = CharUtils.toChar(cpcpt.getTypcpt());
			}
			else
			{
				exist_cpt = false;
			}
			
			if (!exist_cpt)
			{
				exist_aux = true;
				Cpt_numcpt = centraservice.getNumcptFromCpAux(numdos_centra, Num_compte);
				if(StringUtils.isBlank(Cpt_numcpt))
					exist_aux = false;
				
				exist_cpt = true;

				// exist_cpt := TRUE;
				// BEGIN
				// SELECT codpro, codnat, typcpt
				// INTO Cpt_Profil, Cpt_Nature, Cpt_Type
				// FROM cp_cpt
				// WHERE cdos = numdos_centra
				// AND numcpt = Cpt_numcpt;
				// EXCEPTION
				// WHEN NO_DATA_FOUND THEN
				// exist_cpt := FALSE;
				// END;
				//
				// IF NOT exist_cpt AND NOT exist_aux THEN
				// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90121',clang,salarie.nmat,calcul.rubq,Num_Compte);
				// RETURN FALSE;
				// END IF;
				// -- MM 12/05/2005
				// -- si compte aux. alors on charge le compte collectif dans Num_compte et le compte aux dans Num_Tiers.
				// Num_Tiers := Num_Compte;
				// Num_Compte := Cpt_numcpt;
				cpcpt = centraservice.getCp_Cpt(numdos_centra, Cpt_numcpt);
				if(cpcpt != null)
				{
					Cpt_Profil = cpcpt.getCodpro();
					Cpt_Nature = cpcpt.getCodnat();
					Cpt_Type = CharUtils.toChar(cpcpt.getTypcpt());
				}
				else
				{
					exist_cpt = false;
				}
				
				if (!exist_cpt && !exist_aux)
				{
					mess = centraservice.chercherMessage(request,"ERR-90121", clang, salarie.getNmat(), calcul.getRubq(), Num_compte);
					globalUpdate._setEvolutionTraitement(request, mess, true);
					return false;
				}
				Num_Tiers = Num_compte;
				Num_compte = Cpt_numcpt;
			}// END IF NOT exist_cpt

			// -- Lecture du profil
			// BEGIN
			// SELECT gesana, geslet
			// INTO Gest_Anal, Gest_lettrage
			// FROM cp_pro
			// WHERE cdos = numdos_centra
			// AND codpro = Cpt_Profil;
			// EXCEPTION
			// WHEN NO_DATA_FOUND THEN
			// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90122',clang,calcul.rubq,Num_Compte,Cpt_Profil);
			// RETURN FALSE;
			// END;
			//
			// IF NOT exist_aux THEN
			// -- LK 2/8/98
			// -- Test existence numero de tiers(compte personnel)
			// IF Cpt_Type = 'C'
			// THEN
			// SELECT count(*)
			// INTO nb_aux
			// FROM cp_aux
			// WHERE cdos = numdos_centra
			// AND numcpt = Num_Compte
			// AND numtie = Num_Tiers;
			// IF nb_aux = 0 THEN
			// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90123',clang,salarie.nmat,calcul.rubq,Num_Tiers);
			// RETURN FALSE;
			// END IF;
			// END IF;
			// END IF;
			//
			Produit cppro = centraservice.getCpProFromCpPro(numdos_centra, Cpt_Profil);
			if(cppro != null)
			{
				Gest_Anal = CharUtils.toChar(cppro.getGesana());
				Gest_Lettrage = CharUtils.toChar(cppro.getGeslet());
			}
			else
			{
				mess = centraservice.chercherMessage(request,"ERR-90122", clang, calcul.getRubq(), Num_compte, Cpt_Profil);
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
			
			if (!exist_aux)
			{
				if (Cpt_Type == 'C')
				{
					nb_aux = centraservice.getNbreCpAuxFromCpAux(numdos_centra, Num_compte, Num_Tiers);
					
					if (nb_aux == 0)
					{
						mess = centraservice.chercherMessage(request,"ERR-90123", clang, salarie.getNmat(), calcul.getRubq(), Num_Tiers);
						globalUpdate._setEvolutionTraitement(request, mess, true);
						return false;
					}
				}
			}
			// -- Si on gere le lettrage, la reference est le mois de paie
			// IF Gest_lettrage = 'O' THEN
			// IF LENGTH(Lib_Mois) > 8 THEN
			// wint.reflet := SUBSTR(Lib_Mois,1,8);
			// ELSE
			// wint.reflet := Lib_Mois;
			// END IF;
			// ELSE
			// wint.reflet := NULL;
			// END IF;

			//
			if (Gest_Lettrage == 'O')
				wint.setReflet(StringUtils.substring(Lib_Mois, 0, 8));
			else
				wint.setReflet(null);
		}
		else
		{// IF deltacom
			// ELSE
			// Gest_Anal := 'N';
			// wint.reflet := NULL;
			// END IF;
			Gest_Anal = 'N';
			wint.setReflet(null);
		}// END If deltacom
		// Mnt_Ecriture := 0;
		Mnt_Ecriture = BigDecimal.ZERO;
		// IF sens = 'D' THEN
		// -- Chargement du montant au debit
		// ch_deb;
		// ELSIF sens = 'C' THEN
		// -- Chargement du montant au credit
		// ch_cre;
		// ELSE
		// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90124',clang,salarie.nmat,calcul.rubq);
		// RETURN FALSE;
		// END IF;
		//
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
				mess = centraservice.chercherMessage(request,"ERR-90124", clang, salarie.getNmat(), calcul.getRubq());
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
		}
		// -- MM 12/2003 deplacement du chargement car on peut charge le code rubrique dans un segment
		// -- ou une cle a partir du numcpt dans une table des nomenclatures.
		// -- Chargement section et etablissement (pour le salarie traite)
		// IF NOT charg_etssec THEN
		// RETURN FALSE;
		// END IF;
		// -- Fin modif MM.
		//
		// IF deltacom THEN
		// wint.cdos := numdos_centra; -- Code dossier
		// ELSE
		// wint.cdos := cdos; -- Code dossier
		// END IF;
		//
		if (!chargerEtablissement(salarie, rubrique.getCrub()))
		{
			return false;
		}
		if (deltacom)
		{
			wint.setIdEntreprise(new Integer(numdos_centra));
		}
		else
		{
			wint.setIdEntreprise(new Integer(cdos));
		}
		// wint.codets := Code_Etablissement; -- Code etablissement
		// wint.codjou := Code_Journal; -- Code Journal
		wint.setCodets(Code_Etablissement);
		wint.setCodjou(Code_Journal);

		//
		// IF deltacom THEN
		// wint.numcpt := SUBSTR(Num_Compte,1,8); -- Numero de compte
		// ELSE
		// wint.numcpt := Num_Compte; -- Numero de compte
		// END IF;
		//

		// *****************************************************************
		int lgNumcpt = 20;
		fnom = centraservice.chercherEnNomenclature(cdos, 99, "LG-NUMCPT", 1);
		if(fnom != null && fnom.getValm() != null && fnom.getValm().intValue()!=0)
			lgNumcpt = fnom.getValm().intValue()>0 ? fnom.getValm().intValue() : 8;
			
		if (!deltacom)
			wint.setNumcpt(StringUtils.substring(Num_compte, 0, lgNumcpt));
		else
			wint.setNumcpt(Num_compte);

		// ******************************************************************
		/*
		 * if(deltacom) wint.setNumcpt(Num_compte.length()>8?Num_compte.substring(0,8):Num_compte); else wint.setNumcpt(Num_compte);
		 */
		// IF Cpt_Type = 'C'
		// THEN
		// wint.numtie := Num_tiers; -- Numero de tiers
		// ELSE
		// wint.numtie := null;
		// END IF;
		ParameterUtil.println("---------------->Cpt_Type = " + Cpt_Type);
		if (Cpt_Type == 'C')
		{
			wint.setNumtie(Num_Tiers);
		}
		else
		{
			wint.setNumtie(null);
		}
		// wint.numpce := Num_Piece; -- Numero de piece
		// wint.datcpt := Date_Comptable; -- Date Comptable
		// wint.datpce := Date_Comptable; -- Date de la piece
		// wint.datech := Date_Comptable; -- Date d'echeance
		// wint.devpce := Devise_Dossier; -- Devise de la piece
		// wint.quantite := null; -- Quantite
		// wint.sens := sens; -- Sens de l'ecriture
		// wint.pce_mt := Mnt_Ecriture; -- Montant de la piece
		// wint.coddes1 := Destination1; -- Axe analytique 1
		// wint.coddes2 := Destination2; -- Axe analytique 2
		// wint.coddes3 := NULL; -- Axe analytique 3
		// wint.coddes4 := NULL; -- Axe analytique 4
		// wint.coddes5 := NULL; -- Axe analytique 5
		// wint.coddes6 := NULL; -- Axe analytique 6
		// wint.coddes7 := NULL; -- Axe analytique 7
		// wint.coddes8 := NULL; -- Axe analytique 8
		// wint.coddes9 := NULL; -- Axe analytique 9
		// wint.libecr := Libelle_Ecriture; -- Libelle de l'ecriture
		// wint.codtre := null; -- Code tresorerie
		// wint.codabr := Libelle_Abrege; -- Code libelle abrege
		// wint.coderr := null; -- Code erreur
		// wint.liberr := null; -- Libelle erreur
		// wint.datcre := sysdate; -- Date de creation
		// wint.datmod := sysdate; -- Date de modification
		// wint.coduti := cuti; -- Code utilisateur
		//
		wint.setNumpce(Num_Piece);
		wint.setDatcpt(Date_Comptable.getDate());
		wint.setDatech(Date_Comptable.getDate());
		wint.setDatpce(Date_Comptable.getDate());
		wint.setDevpce(Devise_Dossier);
		wint.setQuantite(null);
		wint.setSens(sens + "");
		//wint.setPceMt(new BigDecimal(Mnt_Ecriture));
		wint.setPceMt(ClsStringUtil.truncateToXDecimal(Mnt_Ecriture,nddd));
		wint.setCoddes1(Destination[1]);
		wint.setCoddes2(Destination[2]);
		wint.setCoddes3(null);
		wint.setCoddes4(null);
		wint.setCoddes5(null);
		wint.setCoddes6(null);
		wint.setCoddes7(null);
//		wint.setCoddes8(rubrique.getCrub());
//		wint.setCoddes9(StringUtils.valueOf(Type_Rubq));
		wint.setCoddes8(null);
		wint.setCoddes9(null);
		if(!deltacom)
		{
			wint.setCoddes3(Destination[3]);
			wint.setCoddes4(Destination[4]);
			wint.setCoddes5(Destination[5]);
			wint.setCoddes6(Destination[6]);
			wint.setCoddes7(Destination[7]);
			wint.setCoddes8(salarie.getNmat());
			wint.setCoddes9(rubrique.getCrub());
		}
		wint.setLibecr(Libelle_Ecriture);
		wint.setCodtre(null);
		wint.setCodabr(Libelle_Abrege);
		wint.setCoderr(null);
		wint.setCreationDate(Instant.now());
		wint.setLastModifiedDate(Instant.now());
//		wint.setDatcre(new ClsDate(new Date(),"dd-MM-yyyy hh:mm:ss.000").getDate());
//		wint.setDatmod(new ClsDate(new Date(),"dd-MM-yyyy hh:mm:ss.000").getDate());
		wint.setCoduti(cuti);
		// IF Gest_Anal = 'N' AND deltacom THEN
		// wint.coddes1 := NULL;
		// wint.coddes2 := NULL;
		// wint.coddes3 := NULL;
		// wint.coddes4 := NULL;
		// wint.coddes5 := NULL;
		// wint.coddes6 := NULL;
		// wint.coddes7 := NULL;
		// wint.coddes8 := NULL;
		// wint.coddes9 := NULL;
		// END IF;
		//
		if (Gest_Anal == 'N' && deltacom)
		{
			wint.setCoddes1(null);
			wint.setCoddes2(null);
			wint.setCoddes3(null);
			wint.setCoddes4(null);
			wint.setCoddes5(null);
			wint.setCoddes6(null);
			wint.setCoddes7(null);
			wint.setCoddes8(null);
			wint.setCoddes9(null);
		}
		// IF NOT deltacom AND interface_gl THEN
		// -- Si interface GL on charge tous les segments
		// wint.coddes1 := Destination1;
		// wint.coddes2 := Destination2;
		// wint.coddes3 := Destination3;
		// wint.coddes4 := Destination4;
		// wint.coddes5 := Destination5;
		// wint.coddes6 := Destination6;
		// wint.coddes7 := Destination7;
		// wint.coddes8 := Destination8;
		// wint.coddes9 := Destination9;
		// END IF;
		//
		if (!deltacom && interface_gl)
		{
			wint.setCoddes1(Destination[1]);
			wint.setCoddes2(Destination[2]);
			wint.setCoddes3(Destination[3]);
			wint.setCoddes4(Destination[4]);
			wint.setCoddes5(Destination[5]);
			wint.setCoddes6(Destination[6]);
			wint.setCoddes7(Destination[7]);
			wint.setCoddes8(Destination[8]);
			wint.setCoddes9(Destination[9]);
		}
		// -- Insertion dans cp_int
		// BEGIN
		// INSERT INTO cp_int VALUES(wint.CDOS,
		// wint.CODETS,
		// wint.CODJOU,
		// wint.NUMCPT,
		// wint.NUMTIE,
		// wint.NUMPCE,
		// wint.DATCPT,
		// wint.DATPCE,
		// wint.DATECH,
		// wint.DEVPCE,
		// wint.QUANTITE,
		// wint.SENS,
		// wint.PCE_MT,
		// wint.REFLET,
		// wint.CODDES1,
		// wint.CODDES2,
		// wint.CODDES3,
		// wint.CODDES4,
		// wint.CODDES5,
		// wint.CODDES6,
		// wint.CODDES7,
		// wint.CODDES8,
		// wint.CODDES9,
		// wint.LIBECR,
		// wint.CODTRE,
		// wint.CODABR,
		// wint.CODERR,
		// wint.LIBERR,
		// wint.DATCRE,
		// wint.DATMOD,
		// wint.CODUTI);
		// EXCEPTION
		// WHEN OTHERS THEN
		// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90125',clang,salarie.nmat,calcul.rubq);
		// RETURN FALSE;
		// END;
		//
		// RETURN TRUE;
		//
		// END ch_rub;
		try
		{
			ParameterUtil.println("---------------------------->Saving cpint " + wint.toString());
			
			/* on ajoute un compteur pour differencier les lignes de cpint*/
			ClsCentralisation.compteur +=1;
			wint.setLiberr(String.valueOf(ClsCentralisation.compteur));
			//centraservice.save(wint);
			session.save(wint);
			if(ClsCentralisation.compteur % 20 == 0)
			{
				session.flush();
				session.clear();
			}
			
			
			query="Insert into InterfComptable VALUES('"+
			 wint.getIdEntreprise()+"','"+
			 wint.getCodets()+"','"+
			 wint.getCodjou()+"','"+
			 wint.getNumcpt()+"','"+
			 wint.getNumtie()+"','"+
			 wint.getNumpce()+"','"+
			 this.dateFormat.format(wint.getDatcpt())+"','"+
			 this.dateFormat.format(wint.getDatpce())+"','"+
			 this.dateFormat.format(wint.getDatech())+"','"+
			 wint.getDevpce()+"',"+
			 wint.getQuantite()+",'"+
			 wint.getSens()+"',"+
			 wint.getPceMt()+",'"+
			 wint.getReflet()+"','"+
			 wint.getCoddes1()+"','"+
			 wint.getCoddes2()+"','"+
			 wint.getCoddes3()+"','"+
			 wint.getCoddes4()+"','"+
			 wint.getCoddes5()+"','"+
			 wint.getCoddes6()+"','"+
			 wint.getCoddes7()+"','"+
			 wint.getCoddes8()+"','"+
			 wint.getCoddes9()+"','"+
			 wint.getLibecr()+"','"+
			 wint.getCodtre()+"','"+
			 wint.getCodabr()+"','"+
			 wint.getCoderr()+"','"+
			 wint.getLiberr()+"','"+
			 this.dateFormat.format(Date.from(wint.getCreationDate()))+"','"+
			 this.dateFormat.format(Date.from(wint.getLastModifiedDate()))+"','"+
			 wint.getCoduti()+"')";
			ParameterUtil.println("==================>Ligne courante de cpint ="+ClsCentralisation.compteur);
//			ParameterUtil.println(query);
//			session.createSQLQuery(query).executeUpdate();
			
			//session.flush();
			//session.clear();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			mess = centraservice.chercherMessage(request,"ERR-90125", clang, salarie.getNmat(), calcul.getRubq());
			globalUpdate._setEvolutionTraitement(request, mess, true);
			logMessage(ex.getMessage());
			return false;
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#ch_rubCNSS(com.cdi.deltarh.entites.Rhpagent)
	 */
	public boolean ch_rubCNSS(Salarie salarie) throws Exception
	{
		String cpintKey;
		String cpanaKey;
		ParameterUtil.println("Entr�e dans ch_rub");
		wint = new InterfComptable();
		List list = null;
		String Cpt_Profil = null, Cpt_Nature = null, query;
		char Gest_Anal = ' ', Gest_Lettrage = ' ';
		int nb_aux = 0;
		
		//sp�cifique cnss
		char sens_def;
		 BigDecimal Mnt_Ecriture_def;
		
//		 Cpt_Type := '';
//		   w_sens_def := '';
//
//		-- LM Modif 05/02/2009 Code gestion uniquement si compte classe 6 ou 7
//		   IF SUBSTR(Num_Compte,1,1) = '6' OR SUBSTR(Num_Compte,1,1) = '7' THEN
//		      tmp_codges := w_codges;
//		   ELSE
//		      tmp_codges := 'N';
//		   END IF;
//		-- LM Fin Modif 05/02/2009

		Cpt_Type = ' ';
		sens_def= ' ';
		
		if(StringUtil.in(StringUtil.oraSubstring(Num_compte, 1, 1),"6,7"))
			 tmp_codges = codges;
			else
				tmp_codges = "N";
		
//		 -- Recherche si matricule ou niveau a integrer dans le numero de compte
//		   IF NVL(wrubq.cper,'N') != 'O' THEN
//		      IF NOT cpt_cherche_lettre THEN
//		         RETURN FALSE;
//		      END IF;
//		   END IF;
		
		if(! StringUtils.equals("O",StringUtil.nvl(rubrique.getCper(), "N")))
		{
			if (!cpt_cherche_lettre(salarie, mess))
			{
				return false;
			}
		}

//		 	Mnt_Ecriture := 0;
//		   Mnt_Ecriture_def := 0;
//		    
//		   -- MM 11/10/2000 modif du libelle de l'ecriture
		Mnt_Ecriture = BigDecimal.ZERO;
		Mnt_Ecriture_def = BigDecimal.ZERO;
		
//		-- LM 27/10/2000 modif libelle ecriture, si pret on met n� du pret dans libelle
//		   IF NVL(wcalc.nprt,' ') > ' ' THEN
//		   -- LM 09/11/2000 modif libelle ecriture, si pret et compte perso,
//		   --                      on met n� du pret dans libelle
//		      IF NVL(wrubq.cper,'N') = 'O' THEN
//		         Libelle_Ecriture := SUBSTR(wrubq.lrub,1,20)||' '||wcalc.nprt;
//		      ELSE
//		         Libelle_Ecriture := wrubq.lrub;
//		      END IF;
//		   ELSE   
//		      Libelle_Ecriture := wrubq.lrub;
//		   END IF;
		if(StringUtils.isNotBlank(calcul.getNprt()))
		{
			if( StringUtils.equals("O",StringUtil.nvl(rubrique.getCper(), "N")))
				Libelle_Ecriture = StringUtil.oraSubstring(rubrique.getLrub(), 1, 20) + " " + calcul.getNprt();
			else
				Libelle_Ecriture = rubrique.getLrub();
		}
		else
			Libelle_Ecriture = rubrique.getLrub();
		
//		-- Lecture T.080 pour savoir si cpt soumis a analytique
//		   BEGIN
//		     SELECT vall
//		       INTO wfnom.lib1
//		       FROM pafnom
//		      WHERE cdos = wpdos_cdos
//		        AND ctab = 80
//		        AND cacc = Num_Compte
//		        AND nume = 1;
//		   EXCEPTION
//		     WHEN NO_DATA_FOUND THEN null;
//		   END;
//		   IF SQL%NOTFOUND THEN
//		      w_analyt := 'N';
//		   ELSE 
//		      w_analyt := 'O';
//		   END IF;
		fnom = centraservice.chercherEnNomenclature(cdos, 80, Num_compte, 1);
//		analyt = (fnom != null && StringUtils.isNotBlank(fnom.getVall())) ? "O" : "N";
		analyt = (fnom != null && fnom != null) ? "O" : "N";
		
//		 IF Num_Tiers is NULL THEN
//	     Num_Tiers := ' ';
//	   END IF;
//	     
//	   -- LM 02/03/09 Deplacement du chargement des variables
//	   IF w_sens = 'D' THEN
//	      -- Chargement du montant au debit
//	      ch_deb;
//	   ELSIF w_sens = 'C' THEN
//	      -- Chargement du montant au credit
//	      ch_cre;
//	   ELSE
//	      w_mess :=    ' Sal. ' || wsal01.nmat || ' Rub. ' || wcalc.rubq
//	                            || ' Erreur sens de l''ecriture comptable';
//	      RETURN FALSE;
//	   END IF;
//	   -- LM 02/03/09 Fin Deplacement du chargement des variables
		if(StringUtils.isBlank(Num_Tiers))
			Num_Tiers = " ";
		
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
				mess = centraservice.chercherMessage(request,"ERR-90124", clang, salarie.getNmat(), calcul.getRubq());
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
		}
//		IF Mnt_Ecriture < 0 THEN
//	      IF w_sens = 'D' THEN
//	         w_sens_def := 'C';
//	      ELSIF w_sens = 'C' THEN
//	         w_sens_def := 'D';
//	      ELSE
//	         w_mess :=    ' Sal. ' || wsal01.nmat || ' Rub. ' || wcalc.rubq
//	                               || ' Erreur sens de l''ecriture comptable';
//	         RETURN FALSE;
//	      END IF;
//	      Mnt_Ecriture_def := ABS(Mnt_Ecriture);
//	   ELSE
//	      w_sens_def := w_sens;
//	      Mnt_Ecriture_def := Mnt_Ecriture;
//	   END IF;
		if(Mnt_Ecriture.signum() < 0)
		{
			if(sens=='D')
				sens_def = 'C';
			else if (sens=='C')
				sens_def='D';
			else
			{
				mess = centraservice.chercherMessage(request,"ERR-90124", clang, salarie.getNmat(), calcul.getRubq());
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
			Mnt_Ecriture_def = Mnt_Ecriture.abs();
		}
		else
		{
			sens_def = sens;
			Mnt_Ecriture_def = Mnt_Ecriture;
		}

//		--- Lecture sur cp_int si existe on cumule sinon on cree 
//		   -- LM 03/11/00 Contr�le sur Num_compte au lieu de w_cpt
//		   BEGIN 
//		      SELECT count(*) INTO w_count 
//		        FROM cp_int
//		       WHERE cdos = wpdos_cdos
//		         AND codets = w_codets
//		         AND numcpt = Num_Compte
//		         AND numtie = Num_Tiers
//		         AND numpce = Num_Piece_Sal
//		         AND sens = w_sens_def
//		         AND coddes1 = wrubq.crub
//		         AND coddes2 = tmp_codges;
//		   EXCEPTION 
//		      WHEN NO_DATA_FOUND THEN null;
//		   END;
			
//		 	query="SELECT count(*) FROM cp_int WHERE idEntreprise = '"+cdos+"'";
//		 	query+=" AND codets = '"+codets+"'";
//			query+=" AND numcpt = '"+ Num_compte+"'";
//			query+=" AND numtie = '"+Num_Tiers+"'";
//			query+=" AND numpce =  '"+Num_Piece_Sal+"'";
//			query+=" AND sens =  '"+sens_def+"'";
//			query+=" AND coddes1 =  '"+rubrique.getCrub()+"'";
//			query+=" AND coddes2 =  '"+tmp_codges+"'";
//			List lst = session.createSQLQuery(query).list();
//			Integer nbr = Integer.parseInt(lst.get(0).toString());
		 cpintKey = cdos+"-"+codets+"-"+Num_compte+"-"+Num_Tiers+"-"+Num_Piece_Sal+"-"+sens_def+"-"+rubrique.getCrub()+"-"+tmp_codges;
		 if(mapCPINTCNSS.containsKey(cpintKey))
		 {	
			
//			IF w_count > 0 THEN 
//			if( nbr > 0)
//			{
//		      
//			   -- LM 02/03/09 Deplacement du chargement des variables
//			   --   IF w_sens = 'D' THEN
//			         -- Chargement du montant au debit
//			   --      ch_deb;
//			   --   ELSIF w_sens = 'C' THEN
//			         -- Chargement du montant au credit
//			   --      ch_cre;
//			   --   ELSE
//			   --      w_mess :=    ' Sal. ' || wsal01.nmat || ' Rub. ' || wcalc.rubq
//			   --                            || ' Erreur sens de l''ecriture comptable';
//			   --      RETURN FALSE;
//			   --   END IF;
//			   -- LM 02/03/09 Fin Deplacement du chargement des variables
//			      
//			      -- LM 03/11/00 Contr�le sur Num_compte au lieu de w_cpt
//			      UPDATE cp_int SET pce_mt = NVL(pce_mt,0) + NVL(Mnt_Ecriture_def,0)
//			       WHERE cdos = wpdos_cdos
//			         AND codets = w_codets
//			         AND numcpt = Num_Compte
//			         AND numtie = Num_Tiers
//			         AND numpce = Num_Piece_Sal
//			         AND sens = w_sens_def
//			         AND coddes1 = wrubq.crub
//			         AND coddes2 = tmp_codges;
				    
				
//				 	query="UPDATE cp_int SET pce_mt = NVL(pce_mt,0) + NVL(:montantt,0)";
//					query+=" WHERE cdos ='"+cdos+"'";
//					query+=" AND codets = '"+codets+"'";
//					query+=" AND numcpt = '"+Num_compte+"'";
//					query+=" AND numtie = '"+Num_Tiers+"'";
//					query+=" AND numpce = '"+Num_Piece_Sal+"'";
//					query+=" AND sens = '"+sens_def+"'";
//					query+=" AND coddes1 = '"+rubrique.getCrub()+"'";
//					query+=" AND coddes2 = '"+tmp_codges+"'";
//					Integer nbrResult = session.createSQLQuery(query)
//						.setParameter("montantt", new BigDecimal(Mnt_Ecriture_def), Hibernate.BIG_DECIMAL)
//						.executeUpdate();
			 InterfComptable wintt = mapCPINTCNSS.get(cpintKey);
			 wintt.setPceMt(wintt.getPceMt().add(Mnt_Ecriture_def));
//			        
//			   ELSE 
			}
			else
			{
//				-- LM 02/03/09 Deplacement du chargement des variables
//				   --   IF w_sens = 'D' THEN
//				         -- Chargement du montant au debit
//				   --      ch_deb;
//				   --   ELSIF w_sens = 'C' THEN
//				         -- Chargement du montant au credit
//				   --      ch_cre;
//				   --   ELSE
//				   --      w_mess :=    ' Sal. ' || wsal01.nmat || ' Rub. ' || wcalc.rubq
//				   --                            || ' Erreur sens de l''ecriture comptable';
//				   --      RETURN FALSE;
//				   --   END IF;
//				   -- LM 02/03/09 Fin Deplacement du chargement des variables
//
//				      IF NVL(wrubq.cper,'N') = 'O' THEN
//				         wint.coddes4  := 'O';
//				      ELSE          
//				      -- LM 27/10/00 Test si param�tre PERSO sur compte
//				         IF NVL(w_perso,'N') = 'O' THEN
//				            wint.coddes4  := 'O';
//				         ELSE   
//				-- LM 13/01/2009 Specif CNSS
//				            IF NVL(w_tiers,'N') = 'O' THEN
//				               wint.coddes4  := 'O';
//				            ELSE   
//				               wint.coddes4  := 'N';
//				            END IF;
//				-- LM 13/01/2009 Fin specif CNSS
//				         END IF;
//				      END IF;
				if(StringUtils.equals("O",StringUtil.nvl(rubrique.getCper(), "N")))
					wint.setCoddes4("O");
				else
				{
					if(StringUtils.equals("O",StringUtil.nvl(perso, "N")))
						wint.setCoddes4("O");
					else
					{
						if(StringUtils.equals("O",StringUtil.nvl(tiers, "N")))
							wint.setCoddes4("O");
						else
							wint.setCoddes4("N");
					}
				}
				
				  wint.setIdEntreprise(new Integer(cdos));//			-- Code dossier
			      wint.setCodets(codets);//   := w_codets;			-- Code etablissement
			      wint.setCodjou("XXXXX");//;				-- Code Journal
			      wint.setNumcpt(Num_compte);//			-- Numero de compte
			      wint.setNumtie(Num_Tiers);//			-- Num�ro compte personnel
			      wint.setNumpce(Num_Piece_Sal);//			-- Numero de piece
			      wint.setDatcpt(Date_Comptable.getDate());//			-- Date Comptable
			      wint.setDatpce(Date_Comptable.getDate());//			-- Date de la piece
			      wint.setDatech(Date_Comptable.getDate());//			-- Date d'echeance
			      wint.setDevpce(Devise_Dossier);//			-- Devise de la piece
			      wint.setQuantite(null);//				-- Quantite
			      wint.setSens(StringUtil.valueOf(new char[]{sens_def}));//			-- Sens de l'ecriture
			      wint.setPceMt(Mnt_Ecriture_def);//		-- Montant de la piece
			      wint.setCoddes1(rubrique.getCrub());//			-- Numero de rubrique	
			      wint.setCoddes2(tmp_codges);//			-- Code Gestion	
			      wint.setCoddes3(analyt);//			-- Analytique ? (O/N)	sqds

			      wint.setCoddes5(null);
			      wint.setCoddes6(null);				
			      wint.setCoddes7(null);				
			      wint.setCoddes8(null);				
			      wint.setCoddes9(null);				
			      wint.setLibecr(Libelle_Ecriture);//		-- Libelle de l'ecriture
			      wint.setCodtre(null);//				-- Code tresorerie
			      wint.setCodabr( Libelle_Abrege);//			-- Code libelle abrege
			      wint.setCoderr(null);//				-- Code erreur
			      wint.setLiberr(null);//				-- Libelle erreur
			      wint.setCreationDate(Instant.now());	//			-- Date de creation
			      wint.setLastModifiedDate(Instant.now());//				-- Date de modification
			      wint.setCoduti(cuti);//				-- Code utilisateur
			      
			  	try
				{
					ParameterUtil.println("---------------------------->Saving cpint " + wint.toString());
					/* on ajoute un compteur pour differencier les lignes de cpint*/
					ClsCentralisation.compteur +=1;
					wint.setLiberr(String.valueOf(ClsCentralisation.compteur));
					//session.save(wint);
					this.refreshConnexion();
					
					mapCPINTCNSS.put(cpintKey, wint);
					
					if(ClsCentralisation.compteur % 20 == 0)
					{
						session.flush();
						session.clear();
					}
					
				
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
					mess = centraservice.chercherMessage(request,"ERR-90125", clang, salarie.getNmat(), calcul.getRubq());
					globalUpdate._setEvolutionTraitement(request, mess, true);
					logMessage(ex.getMessage());
					return false;
				}

			}
		
//			 IF w_analyt = 'O' THEN
			if(StringUtils.equals(analyt, "O"))
			{
//
//		      -- LM 03/11/00 Contr�le sur Num_compte au lieu de w_cpt
//		      BEGIN 
//			  SELECT count(*) INTO w_count 
//		          FROM cp_analyt
//		         WHERE cdos = wpdos_cdos
//		           AND pcea = Num_Piece_Sal
//		           AND cg = Num_Compte
//			   AND gesa = tmp_codges
//		           AND ruba = wrubq.crub
//		           AND cana = w_sana
//		           AND sena = w_sens_def;
//		      EXCEPTION 
//		        WHEN NO_DATA_FOUND THEN null;
//		      END;
			
				
//			query = "SELECT count(*) FROM cp_analyt WHERE idEntreprise = '" + cdos + "'";
//			query += " AND pcea =  '" + Num_Piece_Sal + "'";
//			query += " AND cg = '" + Num_compte + "'";
//			query += " AND gesa =  '" + tmp_codges + "'";
//			query += " AND ruba =  '" + rubrique.getCrub() + "' ";
//			query += " AND cana =  '" + sana + "'";
//			query += " AND sena =  '" + sens_def + "'";
//			 lst = session.createSQLQuery(query).list();
			cpanaKey = cdos+"-"+Num_Piece_Sal+"-"+Num_compte+"-"+tmp_codges+"-"+rubrique.getCrub()+"-"+sana+"-"+sens_def;
			
			if(mapCPANACNSS.containsKey(cpanaKey))
			{
//
//		      IF w_count > 0 THEN 
//				if(Integer.parseInt(lst.get(0).toString()) > 0)
//				{
//
//		      -- LM 03/11/00 Contr�le sur Num_compte au lieu de w_cpt
//		         UPDATE cp_analyt SET mona = NVL(mona,0) + NVL(Mnt_Ecriture_def,0)
//		          WHERE cdos = wpdos_cdos
//		            AND pcea = Num_Piece_Sal
//		            AND cg = Num_Compte
//		   	    AND gesa = tmp_codges
//		            AND ruba = wrubq.crub
//		            AND cana = w_sana
//		            AND sena = w_sens_def;
				query = "UPDATE cp_analyt SET mona = NVL(mona,0) + NVL(:montant,0) ";
				query += " WHERE idEntreprise = '" + cdos + "'";
				query += " AND pcea =  '" + Num_Piece_Sal + "'";
				query += " AND cg =  '" + Num_compte + "'";
				query += " AND gesa =  '" + tmp_codges + "'";
				query += " AND ruba =  '" + rubrique.getCrub() + "'";
				query += " AND cana =  '" + sana + "'";
				query += " AND sena =  '" + sens_def + "'";
//				session.createSQLQuery(query)
//				.setParameter("montant", new BigDecimal(Mnt_Ecriture_def), Hibernate.BIG_DECIMAL)
//				.executeUpdate();
				CpAnaCNSS wintt = mapCPANACNSS.get(cpanaKey);
				 wintt.setMona(wintt.getMona().add(Mnt_Ecriture_def));
//				
//		         
					}
				else
//		      ELSE 
				{
//
//		         -- Insertion dans cp_analyt
//		         -- LM 03/11/00 Contr�le sur Num_compte au lieu de w_cpt
//		        BEGIN
//		           INSERT INTO cp_analyt VALUES(wpdos_cdos,
//		                                        Num_Piece_Sal,
//		                                        Num_Compte,
//		                                        tmp_codges,
//		                                        w_sana,
//		                                        wrubq.crub,
//		                                        w_sens_def,
//		                                        Mnt_Ecriture_def);
//					query="INSERT INTO cp_analyt VALUES('"+cdos+"','"+Num_Piece_Sal+"','"+  Num_compte+"','"+tmp_codges+"','"+ sana+"','"+rubrique.getCrub()+"','"+ sens_def+"',"+Mnt_Ecriture_def+")";
//					session.createSQLQuery(query).executeUpdate();
					cpana = new CpAnaCNSS();
					cpana.setCdos(cdos);
					cpana.setPcea(Num_Piece_Sal);
					cpana.setCg(Num_compte);
					cpana.setGesa(tmp_codges);
					cpana.setCana(sana);
					cpana.setRuba(rubrique.getCrub());
					cpana.setSena(String.valueOf(sens_def));
					cpana.setMona(Mnt_Ecriture_def);
					mapCPANACNSS.put(cpanaKey, cpana);
//		         EXCEPTION
//		           WHEN OTHERS THEN
//		              w_mess :=    ' Sal. ' || wsal01.nmat || ' Rub. ' || wcalc.rubq
//		                                    || '. Erreur insertion dans interface.';
//		              RETURN FALSE;
//		         END;
//
//
//		      END IF;
				}
//
//
//		   END IF;
			}//if analyt = O
	
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#refreshConnexion()
	 */
	public void refreshConnexion()
	{
		String query="Update Evlang set clang=clang where clang='XX'";
		session.createSQLQuery(query).executeUpdate();
	}
	
	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#saveAllCNSS()
	 */
	public void saveAllCNSS()
	{
		Iterator<InterfComptable> iter = mapCPINTCNSS.values().iterator();
		Integer i = 0;
		while(iter.hasNext())
		{
			i++;
			session.save(iter.next());
			if(i % 20 == 0)
			{
				session.flush();
				session.clear();
			}
		}
		
		Iterator<CpAnaCNSS> iter2 = mapCPANACNSS.values().iterator();
		i = 0;
		while(iter2.hasNext())
		{
			i++;
			session.createSQLQuery(iter2.next().insertQuery()).executeUpdate();
			if(i % 20 == 0)
			{
				session.flush();
				session.clear();
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#rech_cpt(java.lang.String, com.cdi.deltarh.entites.Rhpagent)
	 */
	public boolean rech_cpt(String strCrub, Salarie salarie) throws Exception
	{
		// FUNCTION rech_cpt(crub IN VARCHAR2) RETURN BOOLEAN IS
		//
		// i SMALLINT;
		// j SMALLINT;
		//
		ParameterUtil.println("Entr�e dans rech_cpt");
		int i, j;
		List list = null;
		String query = null;
		// BEGIN
		// -- LH 160998 on test plutot le compte concerne
		// --IF PA_PAIE.NouB(rubrique.de01) AND PA_PAIE.NouB(rubrique.cr01) THEN
		// -- mess := ' Sal. ' || salarie.nmat || ' Rubrique ' || calcul.rubq
		// -- || ' Aucun no compte de comptabilite (Ecran 7)';
		// -- RETURN FALSE;
		// --END IF;
		//
		if(StringUtil.notEquals(ClsEntreprise.SHELL_GABON, this.globalUpdate.nomClient) && StringUtil.notEquals(ClsEntreprise.COMILOG, this.globalUpdate.nomClient))
			if (NouB(rubrique.getDe01()) && NouB(rubrique.getCr01()) && !nouvelleCompta)
			{
				mess = " Sal. " + salarie.getNmat() + " Rubrique " + calcul.getRubq()+"  Aucun no compte de comptabilite (Ecran 7)";
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
		// -- Recherche grade du salarie
		// BEGIN
		// SELECT NVL(valm, 0)
		// INTO pafnom.mnt1
		// FROM pafnom
		// WHERE cdos = cdos
		// AND ctab = 6
		// AND cacc = salarie.grad
		// AND nume = 1;
		// EXCEPTION
		// WHEN NO_DATA_FOUND THEN null;
		// END;
		// $
		fnom = centraservice.chercherEnNomenclature(cdos, 6, salarie.getGrad(), 1);
		if(fnom.getValm() == null)
		{
			mess = centraservice.chercherMessage(request,"ERR-90109", clang, salarie.getNmat(), salarie.getGrad());
			globalUpdate._setEvolutionTraitement(request, mess, true);
			return false;
		}
		
		// IF SQL%NOTFOUND THEN
		// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90109',clang,salarie.nmat,salarie.grad);
		// RETURN FALSE;
		// END IF;
		//
		// ind_cre := pafnom.mnt1 + 1;
		//
		// IF ind_cre < 1 OR ind_cre > 5
		// THEN
		// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90110',clang,salarie.nmat,salarie.grad);
		// RETURN FALSE;
		// END IF;
		//
		ind_cre = fnom.getValm().intValue() + 1;
		if (ind_cre < 1 || ind_cre > 5)
		{
			mess = centraservice.chercherMessage(request,"ERR-90110", clang, salarie.getNmat(), salarie.getGrad());
			globalUpdate._setEvolutionTraitement(request, mess, true);
			return false;
		}
		// IF Type_Rubq = 1 OR Type_Rubq = 3
		// THEN
		// -- Gain ou Part Pat. -> Debit
		// sens := 'D';
		//
		// -- MM 02/10/2000 Ajout du nume = 1
		// BEGIN
		// SELECT NVL(valm, 0)
		// INTO pafnom.mnt1
		// FROM pafnom
		// WHERE cdos = cdos
		// AND ctab = 4
		// AND cacc = salarie.nato
		// AND nume = 1;
		// EXCEPTION
		// WHEN NO_DATA_FOUND THEN null;
		// END;
		//
		// IF SQL%NOTFOUND THEN
		// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90111',clang,salarie.nmat,salarie.nato);
		// RETURN FALSE;
		// END IF;
		//
		boolean booltyper = false;
		booltyper = (Type_Rubq == 1 || Type_Rubq == 3);
		if(StringUtils.equals(ClsEntreprise.SHELL_GABON, this.globalUpdate.nomClient))
			booltyper = (Type_Rubq == 1 || Type_Rubq == 3 || Type_Rubq == 2);
		if(booltyper)
		{
			sens = 'D';
			if(StringUtils.equals(ClsEntreprise.SHELL_GABON, this.globalUpdate.nomClient))
	   		 {
	   			 if(Type_Rubq == 1 || Type_Rubq == 3)
	   				 sens = 'D';
	   			 if(Type_Rubq == 2)
	   				 sens = 'C';
	   		 }
			fnom = centraservice.chercherEnNomenclature(cdos, 4, salarie.getNato(), 1);
			if(fnom.getValm() == null)
			{
				mess = centraservice.chercherMessage(request,"ERR-90111", clang, salarie.getNmat(), salarie.getNato());
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
		

			// ind_deb := ((ind_cre-1) * 4) + 1 + pafnom.mnt1;
			//
			// IF ind_deb < 1 OR ind_deb > 20
			// THEN
			// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90112',clang,salarie.nmat,salarie.grad,salarie.nato);
			// RETURN FALSE;
			// END IF;
			//
			ind_deb = ((ind_cre - 1) * 4) + 1 + fnom.getValm().intValue();
			
			if(ind_deb < 1 || ind_deb > 20)
			{
				mess = centraservice.chercherMessage(request,"ERR-90112", clang, salarie.getNmat(), salarie.getGrad(), salarie.getNato());
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
			// IF ind_deb = 1 THEN
			// Num_Compte := rubrique.de01;
			// ELSIF ind_deb = 2 THEN
			// Num_Compte := rubrique.de02;
			// ELSIF ind_deb = 3 THEN
			// Num_Compte := rubrique.de03;
			// ELSIF ind_deb = 4 THEN
			// Num_Compte := rubrique.de04;
			// ELSIF ind_deb = 5 THEN
			// Num_Compte := rubrique.de05;
			// ELSIF ind_deb = 6 THEN
			// Num_Compte := rubrique.de06;
			// ELSIF ind_deb = 7 THEN
			// Num_Compte := rubrique.de07;
			// ELSIF ind_deb = 8 THEN
			// Num_Compte := rubrique.de08;
			// ELSIF ind_deb = 9 THEN
			// Num_Compte := rubrique.de09;
			// ELSIF ind_deb = 10 THEN
			// Num_Compte := rubrique.de10;
			// ELSIF ind_deb = 11 THEN
			// Num_Compte := rubrique.de11;
			// ELSIF ind_deb = 12 THEN
			// Num_Compte := rubrique.de12;
			// ELSIF ind_deb = 13 THEN
			// Num_Compte := rubrique.de13;
			// ELSIF ind_deb = 14 THEN
			// Num_Compte := rubrique.de14;
			// ELSIF ind_deb = 15 THEN
			// Num_Compte := rubrique.de15;
			// ELSIF ind_deb = 16 THEN
			// Num_Compte := rubrique.de16;
			// ELSIF ind_deb = 17 THEN
			// Num_Compte := rubrique.de17;
			// ELSIF ind_deb = 18 THEN
			// Num_Compte := rubrique.de18;
			// ELSIF ind_deb = 19 THEN
			// Num_Compte := rubrique.de19;
			// ELSIF ind_deb = 20 THEN
			// Num_Compte := rubrique.de20;
			// END IF;
			//
			
			if(nouvelleCompta)
				Num_compte = centraservice.getCompteRubriqueIndDeb(salarie,rubrique, ind_deb);
			else
				Num_compte = centraservice.getCompteRubriqueIndDeb(rubrique, ind_deb);
			// sp�cifique cnss
			if (StringUtils.equals(globalUpdate.nomClient, ClsEntreprise.CNSS))
			{
//				Num_Compte := substr(Num_compte,1,8);
				xNum_compte = Num_compte;
				Num_compte = StringUtil.oraSubstring(Num_compte, 1, 8);
			}
			
			// IF PA_PAIE.NouB(Num_Compte)
			// THEN
			// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90113',clang,calcul.rubq,TO_CHAR( ind_deb, '09' ));
			// RETURN FALSE;
			// END IF;
			//
			// IF Num_Compte = 'PERS' THEN
			// Num_Compte := salarie.ccpt;
			// END IF;
			//
			if (NouB(Num_compte))
			{
				mess =salarie.getNmat()+". "+ centraservice.chercherMessage(request,"ERR-90113", clang, calcul.getRubq(), ClsStringUtil.int2string(ind_deb, 1, 2, "", ""));
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
			if (Num_compte.compareTo("PERS") == 0)
			{
				Num_compte = salarie.getCcpt();
			}
			// sp�cifique cnss
			if (StringUtils.equals(globalUpdate.nomClient, ClsEntreprise.CNSS))
			{
				if (Num_compte.compareTo("PERSO") == 0)
				{
					Num_compte = StringUtil.oraSubstring(salarie.getCcpt(), 1, 8);
					perso = "O";
				}
			}
			// IF PA_PAIE.NouB(Num_Compte)
			// THEN
			// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90114',clang,salarie.nmat);
			// RETURN FALSE;
			// END IF;
			//
			if (NouB(Num_compte))
			{
				mess = centraservice.chercherMessage(request,"ERR-90114", clang, salarie.getNmat());
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
			// IF NOT deltacom AND interface_gl THEN
			// -- Si interface GL on reinitialise tous les segments
			// Destination1 := NULL;
			// Destination2 := NULL;
			// Destination3 := NULL;
			// Destination4 := NULL;
			// Destination5 := NULL;
			// Destination6 := NULL;
			// Destination7 := NULL;
			// Destination8 := NULL;
			// Destination9 := NULL;
			// END IF;
			//
			// sp�cifique cnss
			if (StringUtil.notEquals(globalUpdate.nomClient, ClsEntreprise.CNSS))
			{
				if (!deltacom && interface_gl)
				{
					for(i=1; i<10; i++)
						Destination[i] = null;
				}
			}
			// -- Ctle existence du compte credit dans DELTACOM
			// IF deltacom
			// THEN
			//
			// --- MM 02-2005
			// --- on fait le remplacement des lettres avant le controle existense du cpte en compta.
			// IF NVL(rubrique.cper,'N') != 'O' THEN
			// IF NOT cpt_cherche_lettre THEN
			// RETURN FALSE;
			// END IF;
			// END IF;
			//
			if (deltacom)
			{
				// sp�cifique cnss
				if (StringUtil.notEquals(globalUpdate.nomClient, ClsEntreprise.CNSS))
				{
					if (rubrique.getCper().compareTo("O") != 0)
					{
						if (!cpt_cherche_lettre(salarie, mess))
						{
							return false;
						}
					}
				}
				// Compte_OK(Num_Compte, Existence_Compte, Suivi_Analytique);
				// IF NOT Existence_Compte
				// THEN
				// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90115',clang,RTRIM(Num_Compte));
				// RETURN FALSE;
				// END IF;
				// IF Suivi_Analytique AND NOT Destination1_Existe
				// THEN
				// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90116',clang,'1',Destination1,salarie.nmat,calcul.rubq);
				// RETURN FALSE;
				// END IF;
				Compte_OK(Num_compte);
				if (!Existence_Compte)
				{
					mess = centraservice.chercherMessage(request,"ERR-90115", clang, Num_compte.trim());
					globalUpdate._setEvolutionTraitement(request, mess, true);
					return false;
				}
				
				for(j=1; j<10; j++)
				{
					// sp�cifique cnss
					//on s'arr�te � l'axe 2
					if (StringUtils.equals(globalUpdate.nomClient, ClsEntreprise.CNSS))
					{
						if(j == 3)
							break;
					}
					if (Suivi_Analytique && !Destination_Existe[j])
					{
						mess = centraservice.chercherMessage(request,"ERR-90116", clang, String.valueOf(j), Destination[j], salarie.getNmat(), calcul.getRubq());
						globalUpdate._setEvolutionTraitement(request, mess, true);
						return false;
					}
				}
				
				// IF Suivi_Analytique AND NOT Destination2_Existe
				// THEN
				// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90116',clang,'2',Destination2,salarie.nmat,calcul.rubq);
				// RETURN FALSE;
				// END IF;
				
				// IF Suivi_Analytique AND NOT Destination3_Existe
				// THEN
				// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90116',clang,'3',Destination3,salarie.nmat,calcul.rubq);
				// RETURN FALSE;
				// END IF;
				
				// IF Suivi_Analytique AND NOT Destination4_Existe
				// THEN
				// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90116',clang,'4',Destination4,salarie.nmat,calcul.rubq);
				// RETURN FALSE;
				// END IF;
				
				// IF Suivi_Analytique AND NOT Destination5_Existe
				// THEN
				// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90116',clang,'5',Destination5,salarie.nmat,calcul.rubq);
				// RETURN FALSE;
				// END IF;
				
				// IF Suivi_Analytique AND NOT Destination6_Existe
				// THEN
				// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90116',clang,'6',Destination6,salarie.nmat,calcul.rubq);
				// RETURN FALSE;
				// END IF;
				
				// IF Suivi_Analytique AND NOT Destination7_Existe
				// THEN
				// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90116',clang,'7',Destination7,salarie.nmat,calcul.rubq);
				// RETURN FALSE;
				// END IF;
				
				// IF Suivi_Analytique AND NOT Destination8_Existe
				// THEN
				// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90116',clang,'8',Destination8,salarie.nmat,calcul.rubq);
				// RETURN FALSE;
				// END IF;
				
				// IF Suivi_Analytique AND NOT Destination9_Existe
				// THEN
				// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90116',clang,'9',Destination9,salarie.nmat,calcul.rubq);
				// RETURN FALSE;
				// END IF;
				
				//
				// END IF;
			}// If deltacom

			//
			// -- Insertion dans la table d'interface(cp_int)
			// IF NOT ch_rub THEN
			// RETURN FALSE;
			// END IF;
			//
			if (!ch_rub(salarie))
			{
				return false;
			}
			// END IF;
			//
		}// if(Type_Rubq == 1 | Type_Rubq == 3){*

		// IF Type_Rubq = 2 OR Type_Rubq = 3 THEN
		// -- Ret ou Part Pat. -> Credit
		// sens := 'C';
		// IF ind_cre = 1 THEN
		// Num_Compte := rubrique.cr01;
		// ELSIF ind_cre = 2 THEN
		// Num_Compte := rubrique.cr02;
		// ELSIF ind_cre = 3 THEN
		// Num_Compte := rubrique.cr03;
		// ELSIF ind_cre = 4 THEN
		// Num_Compte := rubrique.cr04;
		// ELSIF ind_cre = 5 THEN
		// Num_Compte := rubrique.cr05;
		// END IF;
		//
		booltyper = (Type_Rubq ==2 || Type_Rubq == 3);
		if(StringUtils.equals(ClsEntreprise.SHELL_GABON, this.globalUpdate.nomClient))
			booltyper = (Type_Rubq == 3);
		if(booltyper)
		{
			sens = 'C';

			if(nouvelleCompta)
				Num_compte = centraservice.getCompteRubriqueIndCre(salarie,rubrique, ind_cre);
			else
				Num_compte = centraservice.getCompteRubriqueIndCre(rubrique, ind_cre);
			// sp�cifique cnss
			if (StringUtils.equals(globalUpdate.nomClient, ClsEntreprise.CNSS))
			{
//				Num_Compte := substr(Num_compte,1,8);
				xNum_compte = Num_compte;
				Num_compte = StringUtil.oraSubstring(Num_compte, 1, 8);
			}
			
			// IF PA_PAIE.NouB(Num_Compte) THEN
			// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90117',clang,calcul.rubq,TO_CHAR(ind_cre, '09'));
			// RETURN FALSE;
			// END IF;
			//
			if (NouB(Num_compte))
			{
				mess = centraservice.chercherMessage(request,"ERR-90117", clang, calcul.getRubq(), ClsStringUtil.int2string(ind_cre, 1, 2, "", ""));
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
			// IF Num_Compte = 'PERS' THEN
			// Num_Compte := salarie.ccpt;
			// END IF;
			//
			// IF PA_PAIE.NouB(Num_Compte)
			// THEN
			// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90114',clang,salarie.nmat);
			// RETURN FALSE;
			// END IF;
			//
			if (Num_compte!=null && Num_compte.compareTo("PERS") == 0)
			{
				Num_compte = salarie.getCcpt();
			}
			// sp�cifique cnss
			if (StringUtils.equals(globalUpdate.nomClient, ClsEntreprise.CNSS))
			{
				if (Num_compte.compareTo("PERSO") == 0)
				{
					Num_compte = StringUtil.oraSubstring(salarie.getCcpt(), 1, 8);
					perso = "O";
				}
			}
			if (NouB(Num_compte))
			{
				mess = centraservice.chercherMessage(request,"ERR-90114", clang, salarie.getNmat());
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
			// IF NOT deltacom AND interface_gl THEN
			// -- Si interface GL on reinitialise tous les segments
			// Destination1 := NULL;
			// Destination2 := NULL;
			// Destination3 := NULL;
			// Destination4 := NULL;
			// Destination5 := NULL;
			// Destination6 := NULL;
			// Destination7 := NULL;
			// Destination8 := NULL;
			// Destination9 := NULL;
			// END IF;
			//
			// sp�cifique cnss
			if (StringUtil.notEquals(globalUpdate.nomClient, ClsEntreprise.CNSS))
			{
				if (!deltacom && interface_gl)
				{	
					for(i=1; i<10; i++)
						Destination[i] = null;
				}
			}
			// -- Ctle existence du compte credit dans DELTACOM
			// IF deltacom
			// THEN
			//
			// --- MM 02-2005
			// --- on fait le remplacement des lettres avant le controle existense du cpte en compta.
			// IF NVL(rubrique.cper,'N') != 'O' THEN
			// IF NOT cpt_cherche_lettre THEN
			// RETURN FALSE;
			// END IF;
			// END IF;
			//
			if (deltacom)
			{
				// sp�cifique cnss
				if (StringUtil.notEquals(globalUpdate.nomClient, ClsEntreprise.CNSS))
				{
					if (rubrique.getCper().compareTo("O") != 0)
					{
						if (!cpt_cherche_lettre(salarie, mess))
						{
							return false;
						}
					}
				}
				//
				// Compte_OK(Num_Compte, Existence_Compte, Suivi_Analytique);
				// IF NOT Existence_Compte THEN
				// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90118',clang,RTRIM(Num_Compte),salarie.nmat,calcul.rubq);
				// RETURN FALSE;
				// END IF;
				// IF Suivi_Analytique AND NOT Destination1_Existe
				// THEN
				// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90119',clang,Destination1,salarie.nmat,calcul.rubq);
				// RETURN FALSE;
				// END IF;
				Compte_OK(Num_compte);
				if (!Existence_Compte)
				{
					mess = centraservice.chercherMessage(request,"ERR-90118", clang, Num_compte.trim(), salarie.getNmat(), calcul.getRubq());
					globalUpdate._setEvolutionTraitement(request, mess, true);
					return false;
				}
				
				for(j=1; j<10; j++)
				{
					// sp�cifique cnss
					if (StringUtils.equals(globalUpdate.nomClient, ClsEntreprise.CNSS))
					{
						if(j == 3)
							break;
					}
					if (Suivi_Analytique && !Destination_Existe[j])
					{
						mess = centraservice.chercherMessage(request,"ERR-90116", clang, String.valueOf(j), Destination[j], salarie.getNmat(), calcul.getRubq());
						globalUpdate._setEvolutionTraitement(request, mess, true);
						return false;
					}
				}
				
				
				// IF Suivi_Analytique AND NOT Destination2_Existe
				// THEN
				// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90120',clang,Destination2,salarie.nmat,calcul.rubq);
				// RETURN FALSE;
				// END IF;
				
				// IF Suivi_Analytique AND NOT Destination3_Existe
				// THEN
				// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90120',clang,Destination3,salarie.nmat,calcul.rubq);
				// RETURN FALSE;
				// END IF;
				
				// IF Suivi_Analytique AND NOT Destination4_Existe
				// THEN
				// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90120',clang,Destination4,salarie.nmat,calcul.rubq);
				// RETURN FALSE;
				// END IF;
				

				// IF Suivi_Analytique AND NOT Destination5_Existe
				// THEN
				// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90120',clang,Destination5,salarie.nmat,calcul.rubq);
				// RETURN FALSE;
				// END IF;
				
				// IF Suivi_Analytique AND NOT Destination6_Existe
				// THEN
				// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90120',clang,Destination6,salarie.nmat,calcul.rubq);
				// RETURN FALSE;
				// END IF;
				
				// IF Suivi_Analytique AND NOT Destination7_Existe
				// THEN
				// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90120',clang,Destination7,salarie.nmat,calcul.rubq);
				// RETURN FALSE;
				// END IF;
				
				// IF Suivi_Analytique AND NOT Destination8_Existe
				// THEN
				// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90120',clang,Destination8,salarie.nmat,calcul.rubq);
				// RETURN FALSE;
				// END IF;
				
				// IF Suivi_Analytique AND NOT Destination9_Existe
				// THEN
				// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90120',clang,Destination9,salarie.nmat,calcul.rubq);
				// RETURN FALSE;
				// END IF;
				
				//
				// END IF;
			}// If deltacom
			//
			// END IF;
			//
			// -- Insertion dans la table d'interface(cp_int)
			// IF NOT ch_rub THEN
			// RETURN FALSE;
			// END IF;
			if (!ch_rub(salarie))
			{
				return false;
			}
			//
			// END IF;
			//
		}// if(Type_Rubq == 2 | Type_Rubq == 3)
		// RETURN TRUE;
		//
		// END rech_cpt;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#_getCompteRubrique(java.lang.String, com.cdi.deltarh.entites.Rhpagent)
	 */
	public boolean _getCompteRubrique(String strCrub, Salarie salarie) throws Exception
	{
		int i, j;
		List list = null;
		String query = null;
		if(StringUtil.notEquals(ClsEntreprise.SHELL_GABON, this.globalUpdate.nomClient))
		if (NouB(rubrique.getDe01()) && NouB(rubrique.getCr01()) && !nouvelleCompta)
		{
			mess = " Sal. " + salarie.getNmat() + " Rubrique " + calcul.getRubq();
			globalUpdate._setEvolutionTraitement(request, mess, true);
			return false;
		}
		
		fnom = centraservice.chercherEnNomenclature(cdos, 6, salarie.getGrad(), 1);
		if(fnom.getValm() == null)
		{
			mess = centraservice.chercherMessage(request,"ERR-90109", clang, salarie.getNmat(), salarie.getGrad());
			globalUpdate._setEvolutionTraitement(request, mess, true);
			return false;
		}
		
		
		ind_cre = fnom.getValm().intValue() + 1;
		if (ind_cre < 1 || ind_cre > 5)
		{
			mess = centraservice.chercherMessage(request,"ERR-90110", clang, salarie.getNmat(), salarie.getGrad());
			globalUpdate._setEvolutionTraitement(request, mess, true);
			return false;
		}
		boolean booltyper = false;
		booltyper = (Type_Rubq == 1 || Type_Rubq == 3);
		if(StringUtils.equals(ClsEntreprise.SHELL_GABON, this.globalUpdate.nomClient))
			booltyper = (Type_Rubq == 1 || Type_Rubq == 3 || Type_Rubq == 2);
		if(booltyper)
		{
//		if (Type_Rubq == 1 || Type_Rubq == 3)
//		{
			sens = 'D';
			if(StringUtils.equals(ClsEntreprise.SHELL_GABON, this.globalUpdate.nomClient))
	   		 {
	   			 if(Type_Rubq == 1 || Type_Rubq == 3)
	   				 sens = 'D';
	   			 if(Type_Rubq == 2)
	   				 sens = 'C';
	   		 }
			fnom = centraservice.chercherEnNomenclature(cdos, 4, salarie.getNato(), 1);
			if(fnom.getValm() == null)
			{
				mess = centraservice.chercherMessage(request,"ERR-90111", clang, salarie.getNmat(), salarie.getNato());
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
		
			ind_deb = ((ind_cre - 1) * 4) + 1 + fnom.getValm().intValue();
			
			if(ind_deb < 1 || ind_deb > 20)
			{
				mess = centraservice.chercherMessage(request,"ERR-90112", clang, salarie.getNmat(), salarie.getGrad(), salarie.getNato());
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
			
			if(nouvelleCompta)
				Num_compte = centraservice.getCompteRubriqueIndDeb(salarie,rubrique, ind_deb);
			else
				Num_compte = centraservice.getCompteRubriqueIndDeb(rubrique, ind_deb);
			
			if (NouB(Num_compte))
			{
				mess = centraservice.chercherMessage(request,"ERR-90113", clang, calcul.getRubq(), ClsStringUtil.int2string(ind_deb, 1, 2, "", ""));
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
			if (Num_compte.compareTo("PERS") == 0)
			{
				Num_compte = salarie.getCcpt();
			}
			
			if (NouB(Num_compte))
			{
				mess = centraservice.chercherMessage(request,"ERR-90114", clang, salarie.getNmat());
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
			
			if (!deltacom && interface_gl)
			{
				for(i=1; i<10; i++)
					Destination[i] = null;
			}
			
			if (deltacom)
			{
				if (rubrique.getCper().compareTo("O") != 0)
				{
					if (!cpt_cherche_lettre(salarie, mess))
					{
						return false;
					}
				}
				
				Compte_OK(Num_compte);
				if (!Existence_Compte)
				{
					mess = centraservice.chercherMessage(request,"ERR-90115", clang, Num_compte.trim());
					globalUpdate._setEvolutionTraitement(request, mess, true);
					return false;
				}
				
				for(j=1; j<10; j++)
				{
					if (Suivi_Analytique && !Destination_Existe[j])
					{
						mess = centraservice.chercherMessage(request,"ERR-90116", clang, String.valueOf(j), Destination[j], salarie.getNmat(), calcul.getRubq());
						globalUpdate._setEvolutionTraitement(request, mess, true);
						return false;
					}
				}
			}

		}
		
		booltyper = (Type_Rubq ==2 || Type_Rubq == 3);
		if(StringUtils.equals(ClsEntreprise.SHELL_GABON, this.globalUpdate.nomClient))
			booltyper = (Type_Rubq == 3);
		if(booltyper)
		{
//		if (Type_Rubq == 2 || Type_Rubq == 3)
//		{
			sens = 'C';

			if(nouvelleCompta)
				Num_compte = centraservice.getCompteRubriqueIndCre(salarie,rubrique, ind_cre);
			else
				Num_compte = centraservice.getCompteRubriqueIndCre(rubrique, ind_cre);
			
			if (NouB(Num_compte))
			{
				mess = centraservice.chercherMessage(request,"ERR-90117", clang, calcul.getRubq(), ClsStringUtil.int2string(ind_cre, 1, 2, "", ""));
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
			
			if (Num_compte.compareTo("PERS") == 0)
			{
				Num_compte = salarie.getCcpt();
			}
			if (NouB(Num_compte))
			{
				mess = centraservice.chercherMessage(request,"ERR-90114", clang, salarie.getNmat());
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
			
			if (!deltacom && interface_gl)
			{	
				for(i=1; i<10; i++)
					Destination[i] = null;
			}
			
			if (deltacom)
			{
				if (rubrique.getCper().compareTo("O") != 0)
				{
					if (!cpt_cherche_lettre(salarie, mess))
					{
						return false;
					}
				}
				
				Compte_OK(Num_compte);
				if (!Existence_Compte)
				{
					mess = centraservice.chercherMessage(request,"ERR-90118", clang, Num_compte.trim(), salarie.getNmat(), calcul.getRubq());
					globalUpdate._setEvolutionTraitement(request, mess, true);
					return false;
				}
				
				for(j=1; j<10; j++)
				{
					if (Suivi_Analytique && !Destination_Existe[j])
					{
						mess = centraservice.chercherMessage(request,"ERR-90116", clang, String.valueOf(j), Destination[j], salarie.getNmat(), calcul.getRubq());
						globalUpdate._setEvolutionTraitement(request, mess, true);
						return false;
					}
				}				
			}		
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#ch_deb()
	 */
	public void ch_deb()
	{
		ParameterUtil.println("Entr�e dans ch_deb");
		// PROCEDURE ch_deb IS
		//
		// BEGIN
		// IF rubrique.crub = Rbq_BRUT THEN
		// Total_Debit := Total_Debit + calcul.mont;
		// Mnt_Ecriture := Mnt_Ecriture + calcul.mont;
		// ELSE
		// IF rubrique.prbul = 3 OR rubrique.prbul = 4 THEN
		// Total_Debit := Total_Debit - calcul.mont;
		// Mnt_Ecriture := Mnt_Ecriture - calcul.mont;
		// ELSE
		// Total_Debit := Total_Debit + calcul.mont;
		// Mnt_Ecriture := Mnt_Ecriture + calcul.mont;
		// END IF;
		// END IF;
		//
		// END ch_deb;
		if (rubrique.getCrub().compareTo(Rbq_BRUT) == 0 || Type_Rubq == 3)
		{
			Total_Debit = Total_Debit.add(calcul.getMont());
			Mnt_Ecriture = Mnt_Ecriture.add(calcul.getMont());
		}
		else
		{
			if (rubrique.getPrbul() == 3 || rubrique.getPrbul() == 4)
			{
				Total_Debit = Total_Debit.subtract(calcul.getMont());
				Mnt_Ecriture = Mnt_Ecriture.subtract(calcul.getMont());
			}
			else
			{
				Total_Debit = Total_Debit.add(calcul.getMont());
				Mnt_Ecriture = Mnt_Ecriture.add(calcul.getMont());
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#ch_cre()
	 */
	public void ch_cre()
	{
		ParameterUtil.println("Entr�e dans ch_cre");

		// PROCEDURE ch_cre IS
		//
		// BEGIN
		// IF rubrique.prbul = 1 OR rubrique.prbul = 2 THEN
		// Total_Credit := Total_Credit - calcul.mont;
		// Mnt_Ecriture := Mnt_Ecriture - calcul.mont;
		// ELSE
		// Total_Credit := Total_Credit + calcul.mont;
		// Mnt_Ecriture := Mnt_Ecriture + calcul.mont;
		// END IF;
		//
		// END ch_cre;
		if (Type_Rubq == 3)
		{
			Total_Credit = Total_Credit.add(calcul.getMont());
			Mnt_Ecriture = Mnt_Ecriture.add(calcul.getMont());
		}
		else
		{
			if (rubrique.getPrbul() == 1 || rubrique.getPrbul() == 2)
			{
				Total_Credit = Total_Credit.subtract(calcul.getMont());
				Mnt_Ecriture = Mnt_Ecriture.subtract(calcul.getMont());
			}
			else
			{
				Total_Credit = Total_Credit.add(calcul.getMont());
				Mnt_Ecriture = Mnt_Ecriture.add(calcul.getMont());
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#Destination_OK(java.lang.String, java.lang.Integer)
	 */
	public boolean Destination_OK(String Destination, Integer Num_Axe)
	{
		ParameterUtil.println("Entr�e dans Destination_OK");
		// FUNCTION Destination_OK(Destination IN cp_des.coddes%TYPE ,
		// Num_Axe IN cp_des.numaxe%TYPE)
		// RETURN BOOLEAN IS
		//
		// Bidon VARCHAR2(1);
		//
		// BEGIN
		// BEGIN
		// SELECT 'X'
		// INTO Bidon
		// FROM cp_des
		// WHERE cdos = numdos_centra
		// AND coddes = Destination
		// AND numaxe = Num_Axe
		// AND typdes = 'IMP';
		// -- AND cnsa = 'R';
		// EXCEPTION
		// WHEN NO_DATA_FOUND THEN
		// RETURN FALSE;
		// END;
		//
		// RETURN TRUE;
		//
		// END Destination_OK;
		if (StringUtils.equals(globalUpdate.nomClient, ClsEntreprise.CNSS))
		{
			numdos_centra = cdos;
		}
		CpDevise cpde = centraservice.getInformationSectionFromCpDe(numdos_centra, Destination, Num_Axe);
		if(cpde == null)
			return false;
		
		return true;
	}

	// -------------------------------------------------------------------------------
	// -- Recherche d'une lettre dans le No de compte
	// -------------------------------------------------------------------------------
	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#cpt_cherche_lettre(com.cdi.deltarh.entites.Rhpagent, java.lang.String)
	 */
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
		// FUNCTION cpt_cherche_lettre RETURN BOOLEAN IS
		//
		// pos NUMBER(2); -- Position de la lettre
		// nb_rec NUMBER(1); -- Nombre de caracteres a prendre pour le matricule
		// -- a partir de la fin
		// lib PAFNOM.VALL%TYPE;
		//
		ParameterUtil.println("Entr�e dans cpt_cherche_lettre");
		int pos, nb_rec = 0;
		String lib = null, query = null;
		List list = null;
		// BEGIN
		//
		// -- Concatenation du matricule
		// pos := NVL(INSTR(Num_Compte,'M'),0);
		// IF pos != 0 THEN
		// BEGIN
		// nb_rec := TO_NUMBER(SUBSTR(Num_Compte,pos+1));
		// EXCEPTION
		// WHEN INVALID_NUMBER THEN
		// nb_rec := 6;
		// END;
		ParameterUtil.println("Num_compte = " + Num_compte);
		pos = StringUtils.indexOf(Num_compte, 'M');
		if (pos != -1)
		{
			int nmatLength = salarie.getNmat().length();
			try
			{
				nb_rec = Integer.parseInt(StringUtils.substring(Num_compte, pos+1, pos+2));
			}
			catch (NumberFormatException ex)
			{
				// nb_rec = 6;
				nb_rec = nmatLength;
			}
			
			//Si apr�s le MX on a un chaine, alors, l'ajouter au tiers
			String add = StringUtils.substring(Num_compte, pos+2);
			
			// -- MM 21/01/2000
			// -- la zone numcpt dans cp_int = 8 char donc probleme de taille
			// -- si compte avec prefixe + matricule
			// -- la partie matricule est insere dans num_tiers.
			//
			// Num_Compte := SUBSTR(Num_Compte,1,pos-1);
			// Num_tiers := SUBSTR(salarie.nmat,6-nb_rec+1,nb_rec);
			// cpt_type := 'C';
			//
			Num_compte = StringUtil.oraSubstring(Num_compte,1, pos);
			// Num_Tiers = salarie.getNmat().substring(6-nb_rec,6);
			//Num_Tiers = StringUtils.substring(salarie.getNmat(),salarie.getNmat().length() - nb_rec, salarie.getNmat().length()*2 - nb_rec -1);
			Num_Tiers = StringUtil.oraSubstring(salarie.getNmat(), nmatLength-nb_rec +1, nb_rec);
			ParameterUtil.println("Num_Tiers = salarie.getNmat().substring(salarie.getNmat().length()-nb_rec,salarie.getNmat().length()*2 - nb_rec -1)"
					+ StringUtils.substring(salarie.getNmat(),salarie.getNmat().length() - nb_rec, salarie.getNmat().length()*2 - nb_rec -1));
			if(StringUtils.isNotBlank(add))
				Num_Tiers = Num_Tiers+add;
			Cpt_Type = 'C';
			return true;
			// RETURN TRUE;
			// END IF;
		}
		// ****************CECI EST UN SPECIFIQUE AJOUTE PAR YANNICK -> JULES***********
//		else
//		{
//			Cpt_Type = 'C';
//			Num_Tiers = salarie.getCcpt();
//		}

		//
		// -- Concatenation du niveau 1
		pos = (Num_compte!=null && Num_compte.indexOf("N1") != -1) ? Num_compte.indexOf("N1") : -1;
		// pos := NVL(INSTR(Num_Compte,'N1'),0);
		// IF pos != 0 THEN
		// Num_Compte := SUBSTR( SUBSTR(Num_Compte,1,pos-1) || salarie.niv1 , 1, 8);
		// RETURN TRUE;
		// END IF;
		//
		if (pos != -1)
		{
			// sp�cifique cnss
			if (StringUtils.equals(globalUpdate.nomClient, ClsEntreprise.CNSS))
				Num_compte = StringUtils.substring(Num_compte,0, pos) +  salarie.getNiv1();
			else if (StringUtils.equals(globalUpdate.nomClient, ClsEntreprise.SODECOTON)){
				Num_compte = StringUtils.substring(Num_compte,0, pos);
				Num_Tiers = StringUtil.oraSubstring(salarie.getNiv3(), 1, 8);
				Cpt_Type = 'C';
			} else Num_compte = StringUtils.substring(StringUtils.substring(Num_compte,0, pos) +  salarie.getNiv1(),0, 8);
			return true;
		}
		// -- Concatenation du niveau 2
		// pos := NVL(INSTR(Num_Compte,'N2'),0);
		// IF pos != 0 THEN
		// Num_Compte := SUBSTR( SUBSTR(Num_Compte,1,pos-1) || salarie.niv2 , 1 , 8);
		// RETURN TRUE;
		// END IF;
		//
		pos = (Num_compte!=null && Num_compte.indexOf("N2") != -1) ? Num_compte.indexOf("N2") : -1;
		if (pos != -1)
		{
			// sp�cifique cnss
			if (StringUtils.equals(globalUpdate.nomClient, ClsEntreprise.CNSS))
				Num_compte = StringUtils.substring(Num_compte,0, pos) +  salarie.getNiv2();
			else if (StringUtils.equals(globalUpdate.nomClient, ClsEntreprise.SODECOTON)){
				Num_compte = StringUtils.substring(Num_compte,0, pos);
				Num_Tiers = StringUtil.oraSubstring(salarie.getNiv3(), 1, 8);
				Cpt_Type = 'C';
			} else Num_compte = StringUtils.substring(StringUtils.substring(Num_compte,0, pos) + salarie.getNiv2(),0, 8);
			return true;
		}
		// -- Concatenation du niveau 3
		// pos := NVL(INSTR(Num_Compte,'N3'),0);
		// IF pos != 0 THEN
		// Num_Compte := SUBSTR( SUBSTR(Num_Compte,1,pos-1) || salarie.niv3, 1, 8);
		// RETURN TRUE;
		// END IF;
		//
		pos = (Num_compte!=null && Num_compte.indexOf("N3") != -1) ? Num_compte.indexOf("N3") : -1;
		if (pos != -1)
		{
			// sp�cifique cnss
			if (StringUtils.equals(globalUpdate.nomClient, ClsEntreprise.CNSS))
				Num_compte = StringUtils.substring(Num_compte,0, pos) +  salarie.getNiv3();
			else if (StringUtils.equals(globalUpdate.nomClient, ClsEntreprise.SODECOTON)){
				Num_compte = StringUtils.substring(Num_compte,0, pos);
				Num_Tiers = StringUtil.oraSubstring(salarie.getNiv3(), 1, 8);
				Cpt_Type = 'C';
			} else Num_compte = StringUtils.substring(StringUtils.substring(Num_compte,0, pos) + salarie.getNiv3(),0, 8);
			return true;
		}
		// sp�cifique cnss
		if (StringUtil.notEquals(globalUpdate.nomClient, ClsEntreprise.CNSS))
		{
			// -- Concatenation de la ville de declaration
			// pos := NVL(INSTR(Num_Compte,'V'),0);
			// IF pos != 0 THEN
			// BEGIN
			// nb_rec := TO_NUMBER(SUBSTR(Num_Compte,pos+1));
			// EXCEPTION
			// WHEN INVALID_NUMBER THEN
			// nb_rec := 6;
			// END;
			//
			// BEGIN
			// SELECT SUBSTR(vall,1,6) INTO lib
			// FROM pafnom
			// WHERE cdos = cdos
			// AND ctab = 5
			// AND cacc = salarie.vild
			// AND nume = 3;
			// EXCEPTION
			// WHEN NO_DATA_FOUND THEN
			// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90137',clang);
			// RETURN FALSE;
			// END;
			//
			// Num_Compte := SUBSTR( SUBSTR(Num_Compte,1,pos-1) || SUBSTR(lib,1,nb_rec) , 1, 8);
			//
			// RETURN TRUE;
			// END IF;
			//
			pos = (Num_compte!=null && Num_compte.indexOf("V") != -1) ? Num_compte.indexOf("V") : -1;
			if (pos != -1)
			{
				try
				{
					nb_rec = Integer.parseInt(StringUtils.substring(Num_compte,pos + 1));
				}
				catch (NumberFormatException ex)
				{
					nb_rec = 6;
				}
				fnom = centraservice.chercherEnNomenclature(cdos, 5, salarie.getVild(), 3);
				lib = fnom.getVall();
				if(StringUtils.isBlank(lib))
				{
					mess = centraservice.chercherMessage(request,"ERR-90137", clang);
					globalUpdate._setEvolutionTraitement(request, mess, true);
					return false;
				}
				
				Num_compte = StringUtils.substring(StringUtils.substring(Num_compte,0, pos) + StringUtils.substring(lib,0, nb_rec),0, 8);
				return true;
			}
		}
		// -- generation parti analytique a partir du compte
		// pos := NVL(INSTR(Num_Compte,'S'),0);
		// IF pos != 0 THEN
		// BEGIN
		// nb_rec := TO_NUMBER(SUBSTR(Num_Compte,pos+1));
		// EXCEPTION
		// WHEN INVALID_NUMBER THEN
		// nb_rec := 1;
		// END;
		//
		// Num_Compte := SUBSTR(Num_Compte,1,pos-1);
		// IF nb_rec = 1 THEN
		// Destination1 := salarie.niv3;
		// ELSIF nb_rec = 2 THEN
		// Destination1 := SUBSTR(salarie.sana,1,8);
		// ELSE
		// Destination1 := '';
		// END IF;
		//
		// RETURN TRUE;
		// END IF;
		//
		// sp�cifique cnss
		if (StringUtils.equals(globalUpdate.nomClient, ClsEntreprise.CNSS))
		{
//		-- LM 13/01/2009 Specif CNSS
//		   -- Prise en compte du '/'
//		   w_pos := NVL(INSTR(xNum_Compte,'/'),0);
//		   IF w_pos != 0 THEN
//		      -- Partie avant le '/' dans Num_compte 
//		      -- Partie apr�s le '/' dans Num_tiers.
//
//		      Num_Tiers := SUBSTR(xNum_Compte,w_pos+1,6);
//		      Num_Compte := SUBSTR(xNum_Compte,1,w_pos-1);
//		      w_tiers := 'O';
//		      
//		      RETURN TRUE;
//		   END IF;
			pos = (xNum_compte.indexOf("/") != -1) ? xNum_compte.indexOf("/") : -1;
			if (pos != -1)
			{
				pos += 1;
				Num_Tiers= StringUtil.oraSubstring(xNum_compte, pos + 1, 6);
				//Num_compte = StringUtil.oraSubstring(xNum_compte, 1, pos);
				Num_compte = StringUtil.oraSubstring(xNum_compte, 1, pos-1);
				tiers = "O";
				return true;
			}
		}
		if (StringUtil.notEquals(globalUpdate.nomClient, ClsEntreprise.CNSS))
		{
			pos = (Num_compte!=null && Num_compte.indexOf("S") != -1) ? Num_compte.indexOf("S") : -1;
			if (pos != -1)
			{
				try
				{
					nb_rec = Integer.parseInt(StringUtils.substring(Num_compte,pos + 1));
				}
				catch (NumberFormatException ex)
				{
					nb_rec = 1;
				}
				Num_compte = StringUtils.substring(Num_compte,0, pos);
				if (nb_rec == 1)
				{
					Destination[1] = salarie.getNiv3();
				}
				else
				{
					if (nb_rec == 2)
					{
						Destination[1] = StringUtils.substring(salarie.getSana(),0, 8);
					}
					else
					{
						Destination[1] = "";
					}
				}
				return true;
			}
		}
		//
		// RETURN TRUE;
		//
		// END;
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#chargerEtablissement(com.cdi.deltarh.entites.Rhpagent, java.lang.String)
	 */
	public boolean chargerEtablissement(Salarie salarie, String crub) throws Exception
	{
		try
		{
			ParameterUtil.println("Entr�e dans chargerEtablissement");
			
			// sp�cifique cnss
			if (StringUtils.equals(globalUpdate.nomClient, ClsEntreprise.CNSS))
			{
				return this.chargerEtablissementCNSS(salarie, crub);
			}
			// BEGIN
			// IF pa_paie.NouB(Cod_Etb_Commun) THEN
			Object obj = null;
			String error = "";
			// BEGIN
			// SELECT vall
			// INTO Code_Etablissement
			// FROM pafnom
			// WHERE cdos = cdos
			// AND ctab = 01
			// AND cacc = salarie.niv1
			// AND nume = 2;
			// EXCEPTION
			// WHEN NO_DATA_FOUND THEN null;
			// END;
			//
			// IF pa_paie.NouB(Code_Etablissement) THEN
			// Code_Etablissement := salarie.niv1;
			// END IF;
			// ELSE
			// Code_Etablissement := Cod_Etb_Commun;
			// END IF;
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
			//
			// IF deltacom THEN
			// -- Controle existence etablissement
			// BEGIN
			// SELECT 'X' INTO exist FROM cp_ets
			// WHERE cdos = numdos_centra
			// AND codets = Code_Etablissement;
			// EXCEPTION
			// WHEN NO_DATA_FOUND THEN null;
			// END;
			//
			// IF SQL%NOTFOUND THEN
			// mess := PA_PAIE.centraservice.chercherMessage(request,'ERR-90092',clang,Code_Etablissement);
			// RETURN FALSE;
			// END IF;
			// END IF;
			//
			// -- Lecture en premier lieu de la zone section analytique du salarie
			// IF Not pa_paie.NouB(salarie.sana) THEN
			// Destination1 := salarie.sana;
			// ELSE
			// Destination1 := NULL;
			// -- Sinon Lecture Lib2 de table 3
			// BEGIN
			// SELECT vall INTO Destination1 FROM pafnom
			// WHERE cdos = cdos
			// AND ctab = 03
			// AND cacc = salarie.niv3
			// AND nume = 2;
			// EXCEPTION
			// WHEN NO_DATA_FOUND THEN
			// Destination1 := NULL;
			// END;
			// -- Sinon niveau 3
			// IF pa_paie.NouB(Destination1) THEN
			// Destination1 := salarie.niv3;
			// END IF;
			// END IF;
			//
			// END IF;
			if (deltacom)
			{
				// -- Controle existence etablissement
				if(StringUtils.isBlank(centraservice.getLibetsFromCpEts(cdos, Code_Etablissement)))
				{
					mess = centraservice.chercherMessage(request,"ERR-90092", clang, Code_Etablissement);
					globalUpdate._setEvolutionTraitement(request, mess, true);
					return false;
				}
			}

			if (!ClsObjectUtil.isNull(salarie.getSana()))
			{
				Destination[1] = salarie.getSana();
			}
			else
			{
//				fnom = centraservice.chercherEnNomenclature(cdos, 3, salarie.getNiv3(), 2);
//				if (!ClsObjectUtil.isNull(fnom))
//					Destination[1] = fnom.getVall();
//				if (ClsObjectUtil.isNull(Destination[1]))
//					Destination[1] = salarie.getNiv3();
				
				
				String colonne = "niv3";
				Integer table = 3;
				Integer numColonne = 2;
				if(!paramDestinations.isEmpty())
				{
					for(ParamData fnom : paramDestinations)
					{
						if(fnom.getNume() == 1)
						{
							if(StringUtils.isNotBlank(fnom.getVall())) colonne = fnom.getVall();
							if(fnom.getValm() != null) table = fnom.getValm().intValue();
							if(fnom.getValt() != null) numColonne = fnom.getValt().intValue();
							break;
						}
					}
				}
				String valNiv = StringUtils.valueOf((char[]) MethodUtils.invokeExactMethod(salarie, "get"+StringUtils.capitalize(colonne.toLowerCase()), null));
				fnom = centraservice.chercherEnNomenclature(cdos, table, valNiv , numColonne);
				if (!ClsObjectUtil.isNull(fnom))
					Destination[1] = fnom.getVall();
				if (ClsObjectUtil.isNull(Destination[1]))
					Destination[1] = valNiv;
				
			}

			ParameterUtil.println("Valeur intiale de destination1" + Destination[1]);
			// }
			//
			// IF deltacom THEN
			// IF Destination_OK(Destination1 , 1) THEN
			// Destination1_Existe := TRUE;
			// ELSE
			// Destination1_Existe := FALSE;
			// END IF;
			// END IF;
			if (deltacom)
			{
				if (Destination_OK(Destination[1], 1))
					Destination_Existe[1] = true;
				else
					Destination_Existe[1] = false;
			}
			//
			// -- Si pas de section analytique 2 commune on prend lib3 de table 3
			// IF pa_paie.NouB(Dest2_Commune) THEN
			// BEGIN
			// SELECT vall INTO Destination2 FROM pafnom
			// WHERE cdos = cdos
			// AND ctab = 03
			// AND cacc = salarie.niv3
			// AND nume = 3;
			// EXCEPTION
			// WHEN NO_DATA_FOUND THEN null;
			// END;
			// ELSE
			// Destination2 := Dest2_Commune;
			// END IF;
			if (ClsObjectUtil.isNull(Dest2_Commune))
			{
//				fnom = centraservice.chercherEnNomenclature(cdos, 3, salarie.getNiv3(), 3);
//				if (!ClsObjectUtil.isNull(fnom))
//					Destination[2] = fnom.getVall();
//				if (ClsObjectUtil.isNull(Destination[2]))
//					Destination[2] = salarie.getNiv3();
				
				String colonne = "niv3";
				Integer table = 3;
				Integer numColonne = 3;
				if(!paramDestinations.isEmpty())
				{
					for(ParamData fnom : paramDestinations)
					{
						if(fnom.getNume() == 2)
						{
							if(StringUtils.isNotBlank(fnom.getVall())) colonne = fnom.getVall();
							if(fnom.getValm() != null) table = fnom.getValm().intValue();
							if(fnom.getValt() != null) numColonne = fnom.getValt().intValue();
							break;
						}
					}
				}
				String valNiv = StringUtils.valueOf((char[]) MethodUtils.invokeExactMethod(salarie, "get"+StringUtils.capitalize(colonne.toLowerCase()), null));
				fnom = centraservice.chercherEnNomenclature(cdos, table, valNiv , numColonne);
				if (!ClsObjectUtil.isNull(fnom))
					Destination[2] = fnom.getVall();
				if (ClsObjectUtil.isNull(Destination[2]))
					Destination[2] = valNiv;
			}
			else
			{
				Destination[2] = Dest2_Commune;
			}
			
			for (int i = 3; i < 10; i++)
			{
				String colonne = null;
				Integer table = 3;
				Integer numColonne = 3;
				if(!paramDestinations.isEmpty())
				{
					for(ParamData fnom : paramDestinations)
					{
						if(fnom.getNume() == i)
						{
							if(StringUtils.isNotBlank(fnom.getVall())) colonne = fnom.getVall();
							if(fnom.getValm() != null) table = fnom.getValm().intValue();
							if(fnom.getValt() != null) numColonne = fnom.getValt().intValue();
							break;
						}
					}
				}
				if(StringUtils.isNotBlank(colonne) && table != null && numColonne != null)
				{
					String valNiv = StringUtil.valueOf((char[]) MethodUtils.invokeExactMethod(salarie, "get"+StringUtils.capitalize(colonne.toLowerCase()), null));
					fnom = centraservice.chercherEnNomenclature(cdos, table, valNiv , numColonne);
					if (!ClsObjectUtil.isNull(fnom))
						Destination[i] = fnom.getVall();
					if (ClsObjectUtil.isNull(Destination[i]))
						Destination[i] = valNiv;
				}
			}
			
			
			//
			// IF deltacom THEN
			if (deltacom)
			{
				// IF NOT pa_paie.NouB(Destination2) THEN
				// IF Destination_OK(Destination2, 2) THEN
				// Destination2_Existe := TRUE;
				// ELSE
				// Destination2_Existe := FALSE;
				// END IF;
				// ELSE
				// Destination2_Existe := TRUE;
				// END IF;
				if (!ClsObjectUtil.isNull(Destination[2]))
				{
					if (Destination_OK(Destination[2], 1))
						Destination_Existe[2] = true;
					else
					{
						Destination_Existe[2] = false;
					}
				}
				else
				{
					Destination_Existe[2] = true;
				}
				//
				// --- MM 01-2005
				// --- Ajout des destination 3 a 9.
				// BEGIN
				// SELECT vall INTO Destination3 FROM pafnom
				// WHERE cdos = cdos
				// AND ctab = 03
				// AND cacc = salarie.niv3
				// AND nume = 4;
				// EXCEPTION
				// WHEN NO_DATA_FOUND THEN
				// Destination3 := null;
				// END;
				for (int i = 3; i < 10; i++)
				{
					fnom = centraservice.chercherEnNomenclature(cdos, 3, salarie.getNiv3(), i+1);
					if (!ClsObjectUtil.isNull(fnom))
						Destination[i] = fnom.getVall();
				}
				
				// BEGIN
				// SELECT vall INTO Destination4 FROM pafnom
				// WHERE cdos = cdos
				// AND ctab = 03
				// AND cacc = salarie.niv3
				// AND nume = 5;
				// EXCEPTION
				// WHEN NO_DATA_FOUND THEN
				// Destination4 := null;
				// END;
				
				// BEGIN
				// SELECT vall INTO Destination5 FROM pafnom
				// WHERE cdos = cdos
				// AND ctab = 03
				// AND cacc = salarie.niv3
				// AND nume = 6;
				// EXCEPTION
				// WHEN NO_DATA_FOUND THEN
				// Destination5 := null;
				// END;
				
				//
				// BEGIN
				// SELECT vall INTO Destination6 FROM pafnom
				// WHERE cdos = cdos
				// AND ctab = 03
				// AND cacc = salarie.niv3
				// AND nume = 7;
				// EXCEPTION
				// WHEN NO_DATA_FOUND THEN
				// Destination6 := null;
				// END;
				
				//
				// BEGIN
				// SELECT vall INTO Destination7 FROM pafnom
				// WHERE cdos = cdos
				// AND ctab = 03
				// AND cacc = salarie.niv3
				// AND nume = 8;
				// EXCEPTION
				// WHEN NO_DATA_FOUND THEN
				// Destination7 := null;
				// END;
				
				//
				// BEGIN
				// SELECT vall INTO Destination8 FROM pafnom
				// WHERE cdos = cdos
				// AND ctab = 03
				// AND cacc = salarie.niv3
				// AND nume = 9;
				// EXCEPTION
				// WHEN NO_DATA_FOUND THEN
				// Destination8 := null;
				// END;
				
				//
				// BEGIN
				// SELECT vall INTO Destination9 FROM pafnom
				// WHERE cdos = cdos
				// AND ctab = 03
				// AND cacc = salarie.niv3
				// AND nume = 10;
				// EXCEPTION
				// WHEN NO_DATA_FOUND THEN
				// Destination9 := null;
				// END;
				
				//
				// IF NOT pa_paie.NouB(Destination3) THEN
				// IF Destination_OK(Destination3, 3) THEN
				// Destination3_Existe := TRUE;
				// ELSE
				// Destination3_Existe := FALSE;
				// END IF;
				// ELSE
				// Destination3_Existe := TRUE;
				// END IF;
				for (int i = 3; i < 10; i++)
				{
					if (!ClsObjectUtil.isNull(Destination[i]))
					{
						if (Destination_OK(Destination[i], i))
							Destination_Existe[i] = true;
						else
						{
							Destination_Existe[i] = false;
						}
					}
					else
					{
						Destination_Existe[i] = true;
					}
				}
				
				
				//
				// IF NOT pa_paie.NouB(Destination4) THEN
				// IF Destination_OK(Destination4, 4) THEN
				// Destination4_Existe := TRUE;
				// ELSE
				// Destination4_Existe := FALSE;
				// END IF;
				// ELSE
				// Destination4_Existe := TRUE;
				// END IF;
				
				//
				// IF NOT pa_paie.NouB(Destination5) THEN
				// IF Destination_OK(Destination5, 5) THEN
				// Destination5_Existe := TRUE;
				// ELSE
				// Destination5_Existe := FALSE;
				// END IF;
				// ELSE
				// Destination5_Existe := TRUE;
				// END IF;
				
				//
				// IF NOT pa_paie.NouB(Destination6) THEN
				// IF Destination_OK(Destination6, 6) THEN
				// Destination6_Existe := TRUE;
				// ELSE
				// Destination6_Existe := FALSE;
				// END IF;
				// ELSE
				// Destination6_Existe := TRUE;
				// END IF;
				
				//
				// IF NOT pa_paie.NouB(Destination7) THEN
				// IF Destination_OK(Destination7, 7) THEN
				// Destination7_Existe := TRUE;
				// ELSE
				// Destination7_Existe := FALSE;
				// END IF;
				// ELSE
				// Destination7_Existe := TRUE;
				// END IF;
				
				//
				// IF NOT pa_paie.NouB(Destination8) THEN
				// IF Destination_OK(Destination8, 8) THEN
				// Destination8_Existe := TRUE;
				// ELSE
				// Destination8_Existe := FALSE;
				// END IF;
				// ELSE
				// Destination8_Existe := TRUE;
				// END IF;
				
				//
				// IF NOT pa_paie.NouB(Destination9) THEN
				// IF Destination_OK(Destination9, 9) THEN
				// Destination9_Existe := TRUE;
				// ELSE
				// Destination9_Existe := FALSE;
				// END IF;
				// ELSE
				// Destination9_Existe := TRUE;
				// END IF;
				
				// END IF;
			}
			//
			//
			// IF NOT deltacom AND interface_gl THEN
			if (!deltacom && interface_gl)
			{
				ParameterUtil.println("Valeur de deltacom=" + deltacom + " et valeur de interface_gl=" + interface_gl + " recalculer destination1");
				//
				// IF NOT pa_paie.NouB(Destination1_gl)THEN
				// Destination1 := concpro(Destination1_gl);
				// END IF;
				for (int i = 1; i < 10; i++)
				{
					if (!ClsObjectUtil.isNull(Destination_gl[i]))
					{
						ParameterUtil.println("---------modification de destination "+i);
						Destination[i] = RechercheValeur(salarie, crub, Destination_gl[i]);
					}
				}
				
				
				//
				// IF NOT pa_paie.NouB(Destination2_gl)THEN
				// Destination2 := concpro(Destination2_gl);
				// END IF;
				
				//
				// IF NOT pa_paie.NouB(Destination3_gl)THEN
				// Destination3 := concpro(Destination3_gl);
				// END IF;
				
				//
				// IF NOT pa_paie.NouB(Destination4_gl)THEN
				// Destination4 := concpro(Destination4_gl);
				// END IF;
				
				//
				// IF NOT pa_paie.NouB(Destination5_gl)THEN
				// Destination5 := concpro(Destination5_gl);
				// END IF;
				
				//
				// IF NOT pa_paie.NouB(Destination6_gl)THEN
				// Destination6 := concpro(Destination6_gl);
				// END IF;
				
				//
				// IF NOT pa_paie.NouB(Destination7_gl)THEN
				// Destination7 := concpro(Destination7_gl);
				// END IF;
				
				//
				// IF NOT pa_paie.NouB(Destination8_gl)THEN
				// Destination8 := concpro(Destination8_gl);
				// END IF;
				
				//
				// IF NOT pa_paie.NouB(Destination9_gl)THEN
				// Destination9 := concpro(Destination9_gl);
				// END IF;
				
				// END IF;
			}
			//
			// RETURN TRUE;
			//
			// END charg_etssec;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			throw ex;
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#chargerEtablissementCNSS(com.cdi.deltarh.entites.Rhpagent, java.lang.String)
	 */
	public boolean chargerEtablissementCNSS(Salarie salarie, String crub) throws Exception
	{
		try
		{
			ParameterUtil.println("Entr�e dans chargerEtablissement");
//			-- Lecture du code etablissement du salarie
//		    BEGIN
//		       SELECT vall
//		         INTO w_codets
//		         FROM pafnom
//		        WHERE cdos = wpdos_cdos
//		          AND ctab = 01
//		          AND cacc = wsal01.niv1
//		          AND nume = 4;
//		    EXCEPTION
//		       WHEN NO_DATA_FOUND THEN null;
//		    END;
//		    IF SQL%NOTFOUND THEN
//		       w_mess := 'Code Etablis. : ' || wsal01.niv1 ||
//		                 ' inexistant en T01. Salarie : '|| wsal01.nmat;
//		       RETURN FALSE;
//		    END IF;
			
			fnom = centraservice.chercherEnNomenclature(cdos, 1, salarie.getNiv1(), 4);
			if(fnom == null || (fnom != null && StringUtils.isBlank(fnom.getVall())))
			{
				mess ="Code Etablis. : " + salarie.getNiv1() +" inexistant en T01. Salarie : "+salarie.getNmat();
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
			codets = fnom.getVall();
			
//			-- Lecture du code pi�ce du salarie
//			BEGIN
//		       SELECT substr(vall, 1, 2)
//		         INTO w_codpie
//		         FROM pafnom
//		        WHERE cdos = wpdos_cdos
//		          AND ctab = 01
//		          AND cacc = wsal01.niv1
//		          AND nume = 5;
//		    EXCEPTION
//		       WHEN NO_DATA_FOUND THEN null;
//		    END;
//		    IF SQL%NOTFOUND THEN
//		       w_mess := 'Code pi�ce : ' || wsal01.niv1 ||
//		                 ' inexistant en T01. Salarie : '|| wsal01.nmat;
//		       RETURN FALSE;
//		    ELSE
//		       Num_Piece_Sal := Num_Piece || w_codpie || '1';
//		    END IF;
			fnom = centraservice.chercherEnNomenclature(cdos, 1, salarie.getNiv1(), 5);
			if(fnom == null || (fnom != null && StringUtils.isBlank(fnom.getVall())))
			{
				mess ="Code pi�ce. : " + salarie.getNiv1() +" inexistant en T01. Salarie : "+salarie.getNmat();
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
			codpie = StringUtil.oraSubstring(fnom.getVall(),1,2);
			Num_Piece_Sal = Num_Piece + codpie+"1";
			
//			 -- Lecture du code gestion du salarie
//			   BEGIN
//			      SELECT vall INTO w_codges FROM pafnom
//			       WHERE cdos = wpdos_cdos
//			         AND ctab = 03
//			         AND cacc = wsal01.niv3
//			         AND nume = 4;
//			   EXCEPTION
//			      WHEN NO_DATA_FOUND THEN NULL;
//			   END;
//			   IF SQL%NOTFOUND THEN
//			      w_mess := 'Code Gestion : ' || wsal01.niv3 ||
//			                ' inexistant en T03. Salarie : '|| wsal01.nmat;
//			      RETURN FALSE;
//			   END IF;
			fnom = centraservice.chercherEnNomenclature(cdos, 3, salarie.getNiv3(),4);
			if(fnom == null || (fnom != null && StringUtils.isBlank(fnom.getVall())))
			{
				mess ="Code Gestion. : " + salarie.getNiv3() +" inexistant en T01. Salarie : "+salarie.getNmat();
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
			codges = fnom.getVall();
			
//			 -- Lecture du code imputation analytique du salarie
//			   BEGIN
//			      SELECT vall INTO w_sana FROM pafnom
//			       WHERE cdos = wpdos_cdos
//			         AND ctab = 03
//			         AND cacc = wsal01.niv3
//			         AND nume = 5;
//			   EXCEPTION
//			      WHEN NO_DATA_FOUND THEN
//			         w_sana := NULL;
//			   END;
//			   IF SQL%NOTFOUND THEN
//			      w_mess := 'Imput. Analyt. : ' || wsal01.niv3 ||
//			                ' inexistant en T03. Salarie : '|| wsal01.nmat;
//			      RETURN FALSE;
//			   END IF;
			fnom = centraservice.chercherEnNomenclature(cdos, 3, salarie.getNiv3(),5);
			if(fnom == null || (fnom != null && StringUtils.isBlank(fnom.getVall())))
			{
				mess ="Imput. Analyt : " + salarie.getNiv3() +" inexistant en T01. Salarie : "+salarie.getNmat();
				globalUpdate._setEvolutionTraitement(request, mess, true);
				return false;
			}
			sana = fnom.getVall();
	
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			throw ex;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#RechercheValeur(com.cdi.deltarh.entites.Rhpagent, java.lang.String, java.lang.String)
	 */
	public String RechercheValeur(Salarie salarie, String crub, String cle1)
	{
		// -- Declarations
		// cle NUMBER(5); -- Indice (tab 30) de la donnee a ajouter
		// num_tab PAFNOM.CTAB%TYPE;
		// num_lib PAFNOM.NUME%TYPE;
		// libelle_tab PAFNOM.VALL%TYPE;
		// flag_tab BOOLEAN;
		//
		// CURSOR C_COMPTES IS
		// SELECT vall
		// FROM pafnom
		// WHERE cdos = cdos
		// AND ctab = num_tab
		// AND cacc in (select distinct cacc from pafnom where cdos = cdos and ctab = num_tab
		// and vall = Num_Compte and nume = 1)
		// AND nume = num_lib;
		int numerotable = 0;
		String numeroCompte = Num_compte;// "";
		String libelletable = "";
		int numerolibelle = 0;
		String key = "";
		
		//
		// BEGIN
		//
		// flag_tab := FALSE;
		// IF substr(a_cle1,1,1) = '@' THEN
		// flag_tab := TRUE;
		// num_tab := substr(a_cle1,3,2);
		// num_lib := substr(a_cle1,6,2);
		// cle := substr(a_cle1,9,3);
		// ELSE
		// cle := a_cle1;
		// END IF;
		boolean flag = false;
		String s = "";
		if (!ClsObjectUtil.isNull(cle1) && cle1.toCharArray()[0] == '@')
		{
			flag = true;
			s =StringUtils.substring(cle1,2, 4);
			if (!ClsObjectUtil.isNull(s))
			{
				numerotable = Integer.parseInt(s);
			}
			s = StringUtils.substring(cle1,5, 7);
			if (!ClsObjectUtil.isNull(s))
			{
				numerolibelle = Integer.parseInt(s);
			}
			key = StringUtils.substring(cle1,8, 11);
		}
		else
			key = cle1;
		String res = "";
		int ikey = Integer.parseInt(key);
		//
		// IF cle = 1 THEN
		// char10 := salarie.niv1;
		// ELSIF cle = 2 THEN
		// char10 := salarie.niv2;
		// ELSIF cle = 3 THEN
		// char10 := salarie.niv3;
		// ELSIF cle = 4 THEN
		// char10 := salarie.nmat;
		// ELSIF cle = 8 THEN
		// char10 := salarie.equi;
		// ELSIF cle = 11 THEN
		// char10 := salarie.sexe;
		// ELSIF cle = 21 THEN
		// char10 := salarie.nato;
		// ELSIF cle = 22 THEN
		// char10 := salarie.sitf;
		// ELSIF cle = 23 THEN
		// char10 := LTRIM(TO_CHAR(salarie.nbcj,'9999'));
		// ELSIF cle = 24 THEN
		// char10 := LTRIM(TO_CHAR(salarie.nbec,'9999'));
		// ELSIF cle = 25 THEN
		// char10 := LTRIM(TO_CHAR(salarie.nbfe,'9999'));
		// ELSIF cle = 27 THEN
		// char10 := salarie.modp;
		// ELSIF cle = 33 THEN
		// char10 := substr(salarie.ccpt,1,10);
		// ELSIF cle = 39 THEN
		// char10 := salarie.cat;
		// ELSIF cle = 40 THEN
		// char10 := salarie.ech;
		// ELSIF cle = 43 THEN
		// char10 := salarie.grad;
		// ELSIF cle = 44 THEN
		// char10 := salarie.fonc;
		// ELSIF cle = 45 THEN
		// char10 := salarie.afec;
		// ELSIF cle = 47 THEN
		// char10 := LTRIM(TO_CHAR(salarie.indi,'999999'));
		// ELSIF cle = 58 THEN
		// char10 := salarie.hifo;
		// ELSIF cle = 59 THEN
		// char10 := salarie.zli1;
		// ELSIF cle = 60 THEN
		// char10 := salarie.zli2;
		// ELSIF cle = 67 THEN
		// char10 := salarie.typc;
		// ELSIF cle = 70 THEN
		// char10 := salarie.regi;
		// ELSIF cle = 71 THEN
		// char10 := salarie.zres;
		// ELSIF cle = 72 THEN
		// char10 := salarie.dmst;
		// ELSIF cle = 73 THEN
		// char10 := LTRIM(TO_CHAR(salarie.npie,'9999'));
		// ELSIF cle = 184 THEN
		// char10 := substr(salarie.sana,1,10);
		// ELSIF cle = 185 THEN
		// char10 := salarie.equi;
		// -- LH 20031009
		// ELSIF cle = 770 THEN
		// char10 := lpad(rubrique.crub, 4, 0);
		// -- MM 12/2003
		// ELSIF cle = 771 THEN
		// char10 := substr(Num_Compte, 1, 10);
		// ELSIF cle >= 801 AND cle <= 899 THEN
		// char10 := SUBSTR(RecZli(salarie.nmat,substr(To_char(cle),2,2)),1,10);
		// ELSE
		// char10 := ' ';
		// END IF;
		if (ikey >= 801 && ikey <= 899)
		{
			res = centraservice.chercherZoneLibreAgent(cdos, salarie.getNmat(), Integer.parseInt(StringUtils.substring(key,1, 3)));
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
				// sp�cifique cnss
				if (StringUtil.notEquals(globalUpdate.nomClient, ClsEntreprise.CNSS))
				{
					res = salarie.getNmat();
				}
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
					res =StringUtils.substring(salarie.getCcpt(),0, 10);
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
				res = StringUtils.substring(salarie.getSana(),0, 10);
				break;
			case 185:
				res = salarie.getEqui();
				break;
			case 770:
				// sp�cifique cnss
				if (StringUtil.notEquals(globalUpdate.nomClient, ClsEntreprise.CNSS))
				{
					res = StringUtil.oraLPad(crub, 4, "O");
				}
				break;
			case 771:
				// sp�cifique cnss
				if (StringUtil.notEquals(globalUpdate.nomClient, ClsEntreprise.CNSS))
				{
					res = StringUtils.substring(numeroCompte,0, 10);
				}
				break;

			default:
				res = " ";
				break;
			}
		}
		//
		// IF flag_tab THEN
		// -- on fait une lecture sur la Table xx en libelle xx avec cle = char10
		// IF cle = 771 THEN
		// -- la recherche du No compte se fait sur le lib 1 car cacc ne fait que 10 char max.
		// libelle_tab := null;
		// OPEN C_COMPTES;
		// FETCH C_COMPTES INTO libelle_tab;
		// CLOSE C_COMPTES;
		// char10 := LTRIM(RTRIM(substr(libelle_tab,1,10)));
		// ELSE
		// BEGIN
		// SELECT vall INTO libelle_tab
		// FROM pafnom
		// WHERE cdos = cdos
		// AND ctab = num_tab
		// AND cacc = char10
		// AND nume = num_lib;
		// EXCEPTION
		// WHEN NO_DATA_FOUND THEN
		// libelle_tab := null;
		// END;
		// char10 := LTRIM(RTRIM(substr(libelle_tab,1,10)));
		// END IF;
		//
		// END IF;
		if (flag)
		{
			boolean val = true;
			// sp�cifique cnss
			if (StringUtils.equals(globalUpdate.nomClient, ClsEntreprise.CNSS) || StringUtils.equals(globalUpdate.nomClient, ClsEntreprise.SOCIETE_OILYBIA_CMR))
			{
				val = false;
			}
			// -- on fait une lecture sur la Table xx en libelle xx avec cle = char10
			if (ikey == 771 && val)
			{
				// -- la recherche du No compte se fait sur le lib 1 car cacc ne fait que 10 char max.
				String queryString = "From ParamData" + " where idEntreprise = '" + cdos + "'" + " and ctab = " + numerotable + " and nume = " + numerolibelle + " and cacc in ( select cacc from ParamData" + " where idEntreprise = '" + cdos + "'"
				+ " and ctab = " + numerotable + " and nume = 1" + " and vall = '" + numeroCompte + "')";
				List<ParamData> l = centraservice.chercherEnNomenclature(queryString);
				
				if ( l.size() > 0)
				{
					libelletable = l.get(0).getVall();
					res = StringUtils.substring(libelletable,0, 10);
				}
			}
			else
			{
				if (StringUtils.isNotBlank(res))
				{
					fnom = centraservice.chercherEnNomenclature(cdos, numerotable, res.trim(), numerolibelle);
					if (fnom != null)
					{
						libelletable = fnom.getVall();
						res = StringUtils.substring(libelletable, 0, 10);
					}
				}
			}
		}
		//
		// RETURN char10;
		return res;
	}

	
	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getCentraservice()
	 */
	public ClsCentralisationService getCentraservice()
	{
		return centraservice;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setCentraservice(com.cdi.deltarh.paie.centralisation.ClsCentralisationService)
	 */
	public void setCentraservice(ClsCentralisationService centraservice)
	{
		this.centraservice = centraservice;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getPafnom()
	 */
	public ClsPafnomType getPafnom()
	{
		return pafnom;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setPafnom(com.cdi.deltarh.paie.centralisation.ClsPafnomType)
	 */
	public void setPafnom(ClsPafnomType pafnom)
	{
		this.pafnom = pafnom;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getRbq_NAP()
	 */
	public String getRbq_NAP()
	{
		return Rbq_NAP;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setRbq_NAP(java.lang.String)
	 */
	public void setRbq_NAP(String rbq_NAP)
	{
		Rbq_NAP = rbq_NAP;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getRbq_BRUT()
	 */
	public String getRbq_BRUT()
	{
		return Rbq_BRUT;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setRbq_BRUT(java.lang.String)
	 */
	public void setRbq_BRUT(String rbq_BRUT)
	{
		Rbq_BRUT = rbq_BRUT;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getExist()
	 */
	public char getExist()
	{
		return exist;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setExist(char)
	 */
	public void setExist(char exist)
	{
		this.exist = exist;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getChar4()
	 */
	public String getChar4()
	{
		return char4;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setChar4(java.lang.String)
	 */
	public void setChar4(String char4)
	{
		this.char4 = char4;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getNbbul()
	 */
	public Integer getNbbul()
	{
		return nbbul;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setNbbul(java.lang.Integer)
	 */
	public void setNbbul(Integer nbbul)
	{
		this.nbbul = nbbul;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getNb_sal()
	 */
	public Integer getNb_sal()
	{
		return nb_sal;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setNb_sal(java.lang.Integer)
	 */
	public void setNb_sal(Integer nb_sal)
	{
		this.nb_sal = nb_sal;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getRetour()
	 */
	public Boolean getRetour()
	{
		return retour;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setRetour(java.lang.Boolean)
	 */
	public void setRetour(Boolean retour)
	{
		this.retour = retour;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getLib_Mois()
	 */
	public String getLib_Mois()
	{
		return Lib_Mois;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setLib_Mois(java.lang.String)
	 */
	public void setLib_Mois(String lib_Mois)
	{
		Lib_Mois = lib_Mois;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getMess()
	 */
	public String getMess()
	{
		return mess;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setMess(java.lang.String)
	 */
	public void setMess(String mess)
	{
		this.mess = mess;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getMess2()
	 */
	public String getMess2()
	{
		return mess2;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setMess2(java.lang.String)
	 */
	public void setMess2(String mess2)
	{
		this.mess2 = mess2;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getCas()
	 */
	public char getCas()
	{
		return cas;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setCas(char)
	 */
	public void setCas(char cas)
	{
		this.cas = cas;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getAamm()
	 */
	public String getAamm()
	{
		return aamm;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setAamm(java.lang.String)
	 */
	public void setAamm(String aamm)
	{
		this.aamm = aamm;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getNbul()
	 */
	public Integer getNbul()
	{
		return nbul;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setNbul(java.lang.Integer)
	 */
	public void setNbul(Integer nbul)
	{
		this.nbul = nbul;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getCuti()
	 */
	public String getCuti()
	{
		return cuti;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setCuti(java.lang.String)
	 */
	public void setCuti(String cuti)
	{
		this.cuti = cuti;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getClang()
	 */
	public String getClang()
	{
		return clang;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setClang(java.lang.String)
	 */
	public void setClang(String clang)
	{
		this.clang = clang;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getNddd()
	 */
	public Integer getNddd()
	{
		return nddd;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setNddd(java.lang.Integer)
	 */
	public void setNddd(Integer nddd)
	{
		this.nddd = nddd;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getDate_Comptable()
	 */
	public ClsDate getDate_Comptable()
	{
		return Date_Comptable;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setDate_Comptable(com.cdi.util.ClsDate)
	 */
	public void setDate_Comptable(ClsDate date_Comptable)
	{
		Date_Comptable = date_Comptable;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getPipe_name()
	 */
	public String getPipe_name()
	{
		return pipe_name;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setPipe_name(java.lang.String)
	 */
	public void setPipe_name(String pipe_name)
	{
		this.pipe_name = pipe_name;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getTimeout()
	 */
	public Integer getTimeout()
	{
		return timeout;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setTimeout(java.lang.Integer)
	 */
	public void setTimeout(Integer timeout)
	{
		this.timeout = timeout;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getDeltacom()
	 */
	public Boolean getDeltacom()
	{
		return deltacom;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setDeltacom(java.lang.Boolean)
	 */
	public void setDeltacom(Boolean deltacom)
	{
		this.deltacom = deltacom;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getCode_Journal()
	 */
	public String getCode_Journal()
	{
		return Code_Journal;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setCode_Journal(java.lang.String)
	 */
	public void setCode_Journal(String code_Journal)
	{
		Code_Journal = code_Journal;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getSens()
	 */
	public char getSens()
	{
		return sens;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setSens(char)
	 */
	public void setSens(char sens)
	{
		this.sens = sens;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getNum_compte()
	 */
	public String getNum_compte()
	{
		return Num_compte;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setNum_compte(java.lang.String)
	 */
	public void setNum_compte(String num_compte)
	{
		Num_compte = num_compte;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getNum_Piece()
	 */
	public String getNum_Piece()
	{
		return Num_Piece;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setNum_Piece(java.lang.String)
	 */
	public void setNum_Piece(String num_Piece)
	{
		Num_Piece = num_Piece;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getLibelle_Ecriture()
	 */
	public String getLibelle_Ecriture()
	{
		return Libelle_Ecriture;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setLibelle_Ecriture(java.lang.String)
	 */
	public void setLibelle_Ecriture(String libelle_Ecriture)
	{
		Libelle_Ecriture = libelle_Ecriture;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getLibelle_Abrege()
	 */
	public String getLibelle_Abrege()
	{
		return Libelle_Abrege;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setLibelle_Abrege(java.lang.String)
	 */
	public void setLibelle_Abrege(String libelle_Abrege)
	{
		Libelle_Abrege = libelle_Abrege;
	}

	
	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getInterface_gl()
	 */
	public Boolean getInterface_gl()
	{
		return interface_gl;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setInterface_gl(java.lang.Boolean)
	 */
	public void setInterface_gl(Boolean interface_gl)
	{
		this.interface_gl = interface_gl;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getNum_tab_gl()
	 */
	public Integer getNum_tab_gl()
	{
		return num_tab_gl;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setNum_tab_gl(java.lang.Integer)
	 */
	public void setNum_tab_gl(Integer num_tab_gl)
	{
		this.num_tab_gl = num_tab_gl;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getCle_tab_gl()
	 */
	public String getCle_tab_gl()
	{
		return cle_tab_gl;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setCle_tab_gl(java.lang.String)
	 */
	public void setCle_tab_gl(String cle_tab_gl)
	{
		this.cle_tab_gl = cle_tab_gl;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getChar10()
	 */
	public String getChar10()
	{
		return char10;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setChar10(java.lang.String)
	 */
	public void setChar10(String char10)
	{
		this.char10 = char10;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getCpt_Type()
	 */
	public char getCpt_Type()
	{
		return Cpt_Type;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setCpt_Type(char)
	 */
	public void setCpt_Type(char cpt_Type)
	{
		Cpt_Type = cpt_Type;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getCode_Etablissement()
	 */
	public String getCode_Etablissement()
	{
		return Code_Etablissement;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setCode_Etablissement(java.lang.String)
	 */
	public void setCode_Etablissement(String code_Etablissement)
	{
		Code_Etablissement = code_Etablissement;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getNum_Tiers()
	 */
	public String getNum_Tiers()
	{
		return Num_Tiers;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setNum_Tiers(java.lang.String)
	 */
	public void setNum_Tiers(String num_Tiers)
	{
		Num_Tiers = num_Tiers;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getDevise_Dossier()
	 */
	public String getDevise_Dossier()
	{
		return Devise_Dossier;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setDevise_Dossier(java.lang.String)
	 */
	public void setDevise_Dossier(String devise_Dossier)
	{
		Devise_Dossier = devise_Dossier;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getCod_Etb_Commun()
	 */
	public String getCod_Etb_Commun()
	{
		return Cod_Etb_Commun;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setCod_Etb_Commun(java.lang.String)
	 */
	public void setCod_Etb_Commun(String cod_Etb_Commun)
	{
		Cod_Etb_Commun = cod_Etb_Commun;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getDest2_Commune()
	 */
	public String getDest2_Commune()
	{
		return Dest2_Commune;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setDest2_Commune(java.lang.String)
	 */
	public void setDest2_Commune(String dest2_Commune)
	{
		Dest2_Commune = dest2_Commune;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getExistence_Compte()
	 */
	public Boolean getExistence_Compte()
	{
		return Existence_Compte;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setExistence_Compte(java.lang.Boolean)
	 */
	public void setExistence_Compte(Boolean existence_Compte)
	{
		Existence_Compte = existence_Compte;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getSuivi_Analytique()
	 */
	public Boolean getSuivi_Analytique()
	{
		return Suivi_Analytique;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setSuivi_Analytique(java.lang.Boolean)
	 */
	public void setSuivi_Analytique(Boolean suivi_Analytique)
	{
		Suivi_Analytique = suivi_Analytique;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getCentra_dos()
	 */
	public Boolean getCentra_dos()
	{
		return centra_dos;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setCentra_dos(java.lang.Boolean)
	 */
	public void setCentra_dos(Boolean centra_dos)
	{
		this.centra_dos = centra_dos;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getNumdos_centra()
	 */
	public String getNumdos_centra()
	{
		return numdos_centra;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setNumdos_centra(java.lang.String)
	 */
	public void setNumdos_centra(String numdos_centra)
	{
		this.numdos_centra = numdos_centra;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getExist_cpt()
	 */
	public Boolean getExist_cpt()
	{
		return exist_cpt;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setExist_cpt(java.lang.Boolean)
	 */
	public void setExist_cpt(Boolean exist_cpt)
	{
		this.exist_cpt = exist_cpt;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getExist_aux()
	 */
	public Boolean getExist_aux()
	{
		return exist_aux;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setExist_aux(java.lang.Boolean)
	 */
	public void setExist_aux(Boolean exist_aux)
	{
		this.exist_aux = exist_aux;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getCpt_numcpt()
	 */
	public String getCpt_numcpt()
	{
		return Cpt_numcpt;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setCpt_numcpt(java.lang.String)
	 */
	public void setCpt_numcpt(String cpt_numcpt)
	{
		Cpt_numcpt = cpt_numcpt;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getInd_deb()
	 */
	public Integer getInd_deb()
	{
		return ind_deb;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setInd_deb(java.lang.Integer)
	 */
	public void setInd_deb(Integer ind_deb)
	{
		this.ind_deb = ind_deb;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getInd_cre()
	 */
	public Integer getInd_cre()
	{
		return ind_cre;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setInd_cre(java.lang.Integer)
	 */
	public void setInd_cre(Integer ind_cre)
	{
		this.ind_cre = ind_cre;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getTotal_Debit()
	 */
	public BigDecimal getTotal_Debit()
	{
		return Total_Debit;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setTotal_Debit(java.lang.Double)
	 */
	public void setTotal_Debit(BigDecimal total_Debit)
	{
		Total_Debit = total_Debit;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getTotal_Credit()
	 */
	public BigDecimal getTotal_Credit()
	{
		return Total_Credit;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setTotal_Credit(java.lang.Double)
	 */
	public void setTotal_Credit(BigDecimal total_Credit)
	{
		Total_Credit = total_Credit;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getWecart()
	 */
	public BigDecimal getWecart()
	{
		return wecart;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setWecart(java.lang.Double)
	 */
	public void setWecart(BigDecimal wecart)
	{
		this.wecart = wecart;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getType_Rubq()
	 */
	public Integer getType_Rubq()
	{
		return Type_Rubq;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setType_Rubq(java.lang.Integer)
	 */
	public void setType_Rubq(Integer type_Rubq)
	{
		Type_Rubq = type_Rubq;
	}

	
	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setMnt_Ecriture(double)
	 */
	public void setMnt_Ecriture(double mnt_Ecriture)
	{
		Mnt_Ecriture = new BigDecimal(mnt_Ecriture+"");
	}
	
	public double getMnt_Ecriture()
	{
		return Mnt_Ecriture.doubleValue();
	}

	

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getWint()
	 */
	public InterfComptable getWint()
	{
		return wint;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setWint(com.cdi.deltarh.entites.CpInt)
	 */
	public void setWint(InterfComptable wint)
	{
		this.wint = wint;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getCdos()
	 */
	public String getCdos()
	{
		return cdos;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setCdos(java.lang.String)
	 */
	public void setCdos(String cdos)
	{
		this.cdos = cdos;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getComp()
	 */
	public char getComp()
	{
		return comp;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setComp(char)
	 */
	public void setComp(char comp)
	{
		this.comp = comp;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getRequest()
	 */
	public HttpServletRequest getRequest()
	{
		return request;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setRequest(javax.servlet.http.HttpServletRequest)
	 */
	public void setRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getCalcul()
	 */
	public CalculPaie getCalcul()
	{
		return calcul;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setCalcul(com.cdi.deltarh.entites.Rhtcalcul)
	 */
	public void setCalcul(CalculPaie calcul)
	{
		this.calcul = calcul;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getRubrique()
	 */
	public ElementSalaire getRubrique()
	{
		return rubrique;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setRubrique(com.cdi.deltarh.entites.Rhprubrique)
	 */
	public void setRubrique(ElementSalaire rubrique)
	{
		this.rubrique = rubrique;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getFnom()
	 */
	public ParamData getFnom()
	{
		return fnom;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setFnom(com.cdi.deltarh.entites.Rhfnom)
	 */
	public void setFnom(ParamData fnom)
	{
		this.fnom = fnom;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getSession()
	 */
	public Session getSession()
	{
		return session;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setSession(org.hibernate.Session)
	 */
	public void setSession(Session session)
	{
		this.session = session;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getDestination()
	 */
	public String[] getDestination()
	{
		return Destination;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setDestination(java.lang.String[])
	 */
	public void setDestination(String[] destination)
	{
		Destination = destination;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getDestination_gl()
	 */
	public String[] getDestination_gl()
	{
		return Destination_gl;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setDestination_gl(java.lang.String[])
	 */
	public void setDestination_gl(String[] destination_gl)
	{
		Destination_gl = destination_gl;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#getDestination_Existe()
	 */
	public Boolean[] getDestination_Existe()
	{
		return Destination_Existe;
	}

	/* (non-Javadoc)
	 * @see com.cdi.deltarh.paie.centralisation.ICentralisation#setDestination_Existe(java.lang.Boolean[])
	 */
	public void setDestination_Existe(Boolean[] destination_Existe)
	{
		Destination_Existe = destination_Existe;
	}

	public String getV_cdos()
	{
		return v_cdos;
	}

	public void setV_cdos(String v_cdos)
	{
		this.v_cdos = v_cdos;
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
