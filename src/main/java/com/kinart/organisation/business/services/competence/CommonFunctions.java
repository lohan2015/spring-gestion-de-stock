package com.kinart.organisation.business.services.competence;
import com.kinart.paie.business.services.cloture.ClsNomenclature;
import com.kinart.paie.business.services.utils.ClsDate;
import com.kinart.paie.business.services.utils.ParameterUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public class CommonFunctions {
	/** Chemin de g�n�ration des fichiers */
	public static final String ETAT_CONNECTE = "Connect�";
	public static final String ETAT_DECONNECTE = "D�connect�";
	public static final String CACC_ET_TER_FIX="ET_TER_FIX";
	public static final String CACC_PARAMGAR="PARAMGAR";
	public static final int NUME_ET_TER_FIX=2;
	public static final String CACC_BIO_CAISSE="BIO_CAISSE";
	public static final int NUME_BIO_CAISSE=2;
	public static final String CACC_JOUR_CAISSE="JOUR_CAISS";
	public static final int NUME_HMIN_CAISSE=2;
	public static final int NUME_HMAX_CAISSE=3;
	public static final String CACC_SERV_BIO="WEBSERVBIO";
	public static final int NUME_SERV_BIO=2;
	public static final String CACC_BIO_BORD="BIO_BORD";
	public static final int NUME_BIO_BORD=2;
	public static final String SESSION_PARAM="session";
	public enum ETAT_TER_FIX{
		ENREGISTREMENT{
			public String toString(){
				return "E";
			}
		},
		CONTROLE{
			public String toString(){
				return "C";
			}
		}
	}
	public enum USAGE_TER_FIX{
		ENREGISTRE{
			public String toString(){
				return "E";
			}
		},
		AFFECTATION{
			public String toString(){
				return "A";
			}
		},
		ENROLEMENT{
			public String toString(){
				return "R";
			}
		}
	}
	public enum ETAT_PREV{
		NONVALIDEE{
			public String toString(){
				return "N";
			}
		},
		VALIDEE{
			public String toString(){
				return "V";
			}
		},
		REJETEE{
			public String toString(){
				return "R";
			}
		}
	}
	public static final String CACC_MINPAIE = "MINPERPAIE";
	public static final int NUME_PPAIE=2;
	public static final int NUME_ANNEE=4;
	public static final String CACC_TICKET_APP = "TICKET_APP";
	public static final int NUME_TEXT_APP=2;
	public static final String CACC_TICKET_PAY = "TICKET_PAY";
	public static final int NUME_TEXT_AV_OP=2;
	public static final int NUME_TEXT_AP_OP=3;
	public static final String CACC_TICKET_END = "TICKET_END";
	public static final int NUME_TEXT_END_AV_OP=2;
	public static final int NUME_TEXT_END_AP_OP=3;
	public static final String ETAT_ACTIF = "O";
	public static final String ETAT_NON_ACTIF = "N";
	public static final String TYPE_OPERATION_TOUTE = "ANY";
	public static final String TYPE_PREVISION_QUALIFICATION = "01";
	public static final String TYPE_PREVISION_OPERATION = "02";
	public static final String LIB_OPERATION_TOUTE = "TOUTE LES OPERATIONS";
	public static final String CHEMIN_DU_LOG = "c:\\gpac\\log.txt";
	public static final String CHEMIN_DU_LOG_FATCURE = "c:\\gpac\\logFacturationMensuelle.txt";
	public static final String CHEMIN_GENERATION_FILES = "pages//genfiles//";
	public static final int LONGUEURMATRICULE=6;
	public static final int SUCCES_CONNEXION=1;
	public static final int CODE_PAR_DEFAUT=45;
	public static final int INDEX_PAR_DEFAUT=0;
    // File sizes used to generate formatted label
    public static final long MEGABYTE_LENGTH_BYTES = 1048000l;
    public static final long KILOBYTE_LENGTH_BYTES = 1024l;
    public static final int BORDEREAUX_NBRE_SHIFT_POUR_PANIER = 10;
    //Variation ann�e budg�taire
    public static final String VARANNEEBUDGETAIRE = "BUDCIVIL";
    //Allocation CNPS
    public static final String ALLOCCNPS = "ALLOCCNPS";
    
    public static final String ARGU_ALLOCCNPS="SPRHALLOCF";
    //acompte quinzaine
    public static final String QUINZAINE="QUINZAINE";
    //gestion des dipes
    public static final String CLE_GENERATION_DIPE="DIPE";
    //gestion des d�c�s
    public static final String CLE_GESTION_DECES="DECES";
    public static final String ARGU_DECES="SPRHDECES";
    
    public static final String CLE_DATE_DERNIERE_CLOTURE = "CLOTURE";
    public static final String CLE_POINTAGE_ACCIDENT_TRAVAIL = "PTACCITRAV";
    //
    public static final String ACCONIERCONVENTIONNEL = "GPDOCKER";
    public static final String RADIE = "RA";
    public static final String GESTIONDECES = "GESTIONDECES";
    public static final String CODEDESPROFILSUTILISATEUR = "OPERATEUR1";
    public static final String BORDEREAUX = "BORDEREAUX";
    public static final String CODEPRDMOI = "CODEPRDMOI";
    public static final String DOCKER = "DOCKER";
    public static final String GARDIEN = "GARDIEN";
    //gratifications
    public static final String ENFANT = "ENFANT1";
    public static final String GRATIFICATION = "GRATIFICAT";
    public static final String LIBELLE_HISTORIQUE_GRATIFICATION="GRATIFICATION ANNEE ";
    public static final int TAILLE_RUBRIQUE=4;
    public static final int NBUL_GRATIFICATION=9;
    public static final String PROCESSUS_GRATIFICATION_ACTIF="0";
    public static final String PROCESSUS_GRATIFICATION_NON_ACTIF="1";
    public static final String PROCESSUS_GENERATION_MNTREEL_PAIE_NON_ACTIF="O";
    public static final String GRATIFICATION_ETAPE_ARBRENOEL="1";
    public static final String GRATIFICATION_ETAPE_INITIALE="0";
    public static final String GRATIFICATION_ETAPE_TERMINE="4";
    //
    public static final String CODETYPEBORDEREAUDECOMMANDE = "DECOMMANDE";
    public static final String CODETYPEBORDEREAUJOURNALIER = "JOURNALIER";
    public static final String CODETYPEBORDEREAUREGULARISATION = "REGUL";
    public static final String CODETYPEBORDEREAUREGULARISATIONMENSUELLE = "ME-REGUL";
    public static final String CODETYPEBORDEREAUACCIDENT = "ACCIDENT";
    public static final String CODETYPEBORDEREAUMENSUALISE = "MENSUALISE"; 
    public static final String CODETYPEBORDEREAUDELEGUE = "DELEGUE"; 
    public static final String CODETYPEBORDEREAUGARDIEN = "GARDIEN"; 
    public static final String CODETYPEDETAILGARDIEN = "GARDIEN"; 
    public static final String CODETYPEDETAILMENSUALISE = "MENSUALISE"; 
    public static final String CODETYPEDETAILDELEGUE = "DELEGUE"; 
    public static final String CODETYPEDETAILBORDEREAUJOURNALIER = "JOURNALIER"; 
    public static final String CODERELEVE = "RELEVE"; 
    
    public static final int PORT_TERMINAUX = 4370; 
    
    public static final String MESS_OPERATION_SUCCESS = "Op�ration termin�e avec succ�s...";
    public static final String MESS_OPERATION_FAILED = "Op�ration �chou�...";
    public static final String MESS_TEXT_FERMER_RETURN_BUTTON = "Fermer";
    public static final String MESS_TEXT_ANNULER_RETURN_BUTTON = "Annuler";
    public static final String TRAVAFINIR = "TRAVAFINIR";
    public static final String DECOMMANDE = "DECOMMANDE";
    public static final String NORMAL = "NORMAL";
    // virement bancaires
    public static final String RUBNET = "RUBNAP";  
    public static final String LIBINDVIREMENT="Virement Salaire ";
    public static final String ECHECRUBRIQUESALAIRENET="9500";
    public static final String MODEVIREMENTBANCAIRE="V";  
    public static final String BULLETIN_CONGE_EDITE="2"; 
    public static final int NBULVIREMENT=9;
    public static final int NBUL=9;
     // pour g�n�rer les fichiers des virements bancaires
    public static final String DOSSIERZIPVIREMENT="pages/genfiles/virement/";   
   
    
    // �pargne
    public static final String CLE_EPARGNE="EPARGNE";
    public static final int NBUL_EPARGNE=9;
    public static final String EPARGNE_RETRAIT="RETRAIT";
    public static final String EPARGNE_DEPOT="DEPOT";
    public static final String EPARGNE_MODE_MANUEL="MAN";
    
    //regroupement des soci�t�s   
    public static final String REGROUPEMENT_LECTURE_ECRITURE="W";
    public static final String REGROUPEMENT_LECTURE="R";
    public static final String REGROUPEMENT_TITLE="Regroupement des soci�t�s ";
    public static final String ETATLIGNE_NOUVEAU="A";
    
    // Stabilisation / D�stabilisation
    public static final String ETATLIGNE_SUPPRIME="S";
    public static final String ETATLIGNE_MODIFIE="M";
    public static final String CLE_STAB_DEST="DESTABILIS";
    
    public static final String STAB_OPERATION="STAB";
    public static final String DESTAB_OPERATION="DEST";
    
    public static final String STAB_DEST_DEMANDE="DEMANDE"; //encore non re�ue par le GPAC
    public static final String STAB_DEST_RECU="RECU"; //d�ja re�ue par le GPAC
    public static final String STAB_DEST_REFUS="REFUS";// rejet�e par le docker (donc le GPAC)
    public static final String STAB_DEST_ACCEPT="ACCEPT";// accept�e par le docker (donc le GPAC)   
    public static final String STAB_DEST_TRAITE="TRAITE"; // d�ja trait�e par le moteur � la cl�ture
    
    
    public static final String STAB_OK="STAB_OK";
    public static final String DEST_OK="DEST_OK";
    public static final String REFUS_STAB="REFUS_STAB";
    public static final String REFUS_DEST="REFUS_DEST";
    //
    public static final String CODETYPEJOURFERIE = "FER";
    
    // AVANCEMENT AUTOMATIQUE
    public static final String AVANCEMENT="AVANCEMENT";
    
    
    // GESTION DES PRETS
    public static final String DEVISE="FCFA";
    public static final  String PRET_NON_DEBLOQUE="02";
    public static final String PRET_DEBLOQUE="03";
    public static final String PRET_SUSPENDU="04";
    public static final String PRET_SOLDE="05";
    public static final String PRET_SIMULE="01";
    public static final String PRET_OPERATION="PRETS";
  
    //
    public static String TYPEPOINTAGEPRIME="PRIME";
    public static String TYPEPOINTAGEPOINTAGE="POINTAGE";   
    public static final String FORMAT_DATE = "dd/MM/yyyy";
    public static final String FORMAT_HEURE = "dd/MM/yyyy HH:mm";
    public static final String CODE_OPERATION_ACCIDENT = "ACTR";
    //GESTION DES DECES
    public static final String CLE_MOTIFDECES_MALADIE="01";
    public static final String CODET99ETATBRDX = "ETATBRDX";
    public static final String CODET99MENSUALISE = "MENSUALISE"; 
    public static final String CODET99DELEGUE = "DELEGUE"; 
    
    //GESTION DES ETATS IMPRIMABLES A PARTIR DES FORMULAIRES DES UC
    public static final String CODET99ETATBONPRISEENCHARGE = "GPACACBP01";
    public static final String CODET99ETATDECLARATIIONACCIDENT = "GPACACDA01";
    public static final String CODET99ETATREGLEMENTPRIMEMEDAILLE = "GPACCAPM01";
    public static final String CODET99ETATBORDEREAUMENSUEL = "GPACPTBM01";
    public static final String CODET99ETATFICHESALARIE = "GPACASFS01";
    public static final String CODET99ETATBORDEREAUJOURNALIER = "GPACPTBJ01";
    public static final String CODET337ETATBULLETINCONGESTANDARD = "RHPACGEB01";
    public static final String CODET99MASQUEBORDEREAUJOURNALIER = "GPACPTMJ01";
    public static final String CODET99MASQUEPREREMPLIBORDEREAUJOURNALIER = "GPACPTPJ01";
    public static final String CODEEDITIONFACTURE = "GPACFDEF01";
    public static final String CODECUMULFACTUREMENSUEL = "GPACFMEF01";
    public static final String CODEDETAILSFACTUREMENSUEL = "GPACFMEF02";
    public static final String CODET99JOURFERIE = "JOURFERIE";
    
    //GESTION DES TYPES DE VALIDATION
    
    public static final String CODETCLOTUREFINMOISAVECVERROUILLAGE = "F";
    public static final String CODETCLOTUREQUINZAINE = "Q";
    public static final String CODETCLOTURESIMULATION = "S";
   
    
   // GARANTIE PRESENCE
    public static final String CODE_OPERATION_GARANTIE_PRESENCE="GARPRE";
    public static final String CODE_MENSUALISE="9";
    public static final String CODE_GARDIEN="G";
    //
    public static final String CODE_OPERATION_ARRET_MALADIE="AMV";
    public static final String PREFIXE_BRD_AM = "AM-";
    public static final String PREFIXE_BRD_PA = "PA-";
    public static final String PREFIXE_BRD_PEX = "PEX-";
    public static final String CMCG_ABSENCES="06";
    public static final String CMCG_AM="02";
    public static final String TABLE_POINTAGE_AM= ClsNomenclature.TABLES_PARAMETRAGES_SYSTEME;
    public static final String TABLE_POINTAGE_PA=ClsNomenclature.TABLES_PARAMETRAGES_SYSTEME;
    public static final String CACC_POINTAGEAM = "HAM";
    public static final String CACC_POINTAGEPA= "HPA";
    public static final String CACC_POINTAGEEX= "PEX";
    public static final String CACC_MOTIFPEX= "MOTIFPEX";
    public static final String OTHER="Autres";//motif autres... pour les pointages exceptionnels des d�l�gu�s
    public static final String CACC_NBJRMAX_PA="NBJRABSMAX";
    public static final String AM_VALIDE = "VALIDE";
    //public static final String ARRETMALAD = "ARRETMALAD";
    public static final String CODE_OPERATION_PERM_ABSENCE="PERABSENCE";
    public static final String FRM_R_H_REPORT_VIEWER_HTML = "frmRHReportViewer.html";
    public static final String REGROUPEMENT_ECRITURE = "W";
    //public static final int NUME_RUBQ_MAJCONGECLO=1;
    public static final int NUME_MAJCONGECLO=2;
    public static final String CACC_MAJCONGECLO="MAJCONGCLO";
    public static final int NUME_MOTIFAM=2;
    public static final String CACC_MOTIFAM="MOTIFAM";
    public static final int NBR_LIGNES_MAX_TAB_DE_BORD=45;
    public static final String OUI = "OUI";
    public static final String NON = "NON";
    public static final String RED_COLOR = "red";
    public static final String GREEN_COLOR = "green";
    public static final int CTAB_NIV1=1;
    public static final int CTAB_NIV2=2;
    public static final int CTAB_NIV3=3;
    public static final String CTAB_EDITIONS="1002";
    
// ===========================Gestion des sessions d'avancement================
	 public static final String CTAB_AVAN_SALARIES = "1001";
	 
// ===========================Administration personnel �ditions================
	 public static final String CTAB_ADPERS_AVANC = "5003";
	 public static final String CTAB_PARAM_REQ = "5012";
	 public static final String CTAB_PROV_CONG = "5014";
	 public static final String CTAB_ADPERS_SNCC = "5017";
	 
// ===========================Editions fiscaux S�n�gal   ======================
	 public static final String CTAB_EDIT_FISC_SEN = "5015";
	 public static final String CTAB_RUB_EDIT_FISC_SEN = "5016";
	 
// ===========================Groupe de fonctions==============================
	 public static final String CTAB_GROUP_FONC = "5004";
    
// ===========================Groupe de fonctions==============================
	 public static final String CTAB_SOU_GROUP_FONC = "5005";
	 
// ===========================Classes socio-professionnelles===================
	 public static final String CTAB_CLAS_SOC_PRO = "5006"; 
// ===========================Table de param�trage ED TAUX REMUNARATION =======
	 public static final String CTAB_ED_TAUX_REM = "5007"; 
// =========== Table de param�trage ED Renforcement de la r�mun�ration fixe ===
	 public static final String CTAB_ED_REM_FIX = "5008"; 
// ======= Table de param�trage ED Renforcement de la r�mun�ration variable ===
	 public static final String CTAB_ED_REM_VAR = "5009";
// =================== Table de param�trage ED ABSENTEISME ====================
	 public static final String CTAB_ED_ABSENTEISME = "5010";
// ============== Table de param�trage nombre jour de travail par mois ========
	 public static final String CTAB_NB_JRW_MOIS = "5011";
// ===========================Etats sp�cifiques perenco  ======================
	 public static final String CTAB_SPEC_PERENCO = "5013";
	 
    /**
     * cl�s dans la table des param�tes syst�mes pr retouver les rubriques de mise
     * � jour des cong�s apr�s la cl�ture
     * @author wlpa
     *
     */
    /*public enum MAJCONGECLO{
    	JAPA,
    	DAPA,
    	JAPEC,
    	DAPEC,
    	JDED,
    	DDED,
    	JRLA,
    	JRLEC
    }*/
    public enum AfterValueChangedItem{
    	OPERATION,
    	DECOMMANDE,
    	VALIDATIONDECOMMANDE,
    	UNDEFINED
    }
    /*public enum MOTIFS_AM{
    	ACCIDENT,
    	MALADIE,
    	REPOS
    }*/
    public enum SANTE_VT_ITEM{
    	O,
    	N
    }
    public enum DEI_VT_ITEM{
    	O,
    	N
    }
    
    public enum TABLEACLOTURER{
    	SPRHTRPAMVTEVENEMENT,
    	SPRHTRPGARPREEVENEMENT,
    	SPRHTRPBORDEREAUJOURNALIER,
    	SPRHTRPMENSUELBORDEREAU
    }
    
    public enum TYPEDECLOTURE{
    	DECADE,
    	QUINZAINE,
    	FINMOIS,
    	FINMOISAVECVERROUILLAGE,
    	SIMULATION,
    	INCONNU
    }
    
    public enum CANUSETHREAD{
    	YES,
    	NO
    }
    /**
     * sera utilis� comme flag lors de la g�n�ration du pointage
     * et de la validation dudit pointage
     * @author Administrateur
     *
     */
    public enum OPERATIONEFFECTUEENGENERATION{
    	GENERATIONPOINTAGEEFFECTUEE,
    	INCONNU
    }
    
	public enum CommonErrorAndWarningOnBordereau{
		ANALYSERETOURCONNEXION,
		POURCENTAGEUNITEOEUVREINCORRECT,
		PERIODEPAIE,
		DOCKERDELEGUEABSENT,
		PRIMETRANSPORTNONAFFECTEEAVECDETAIL,
		PRIMETRANSPORTNONAFFECTEE,
		PRIMEPANIERNONAFFECTEE,
		PRIMEPANIERNONAFFECTEEAVECDETAIL,
		DOCKERNONAFFECTECHEZACCONIER,
		DOCKERDEJAPOINTECHEZACCONIER,
		DOCKERDEJAPOINTECHEZUNAUTREACCONIER,
		DOCKERBLACKLISTECHEZACCONIER,
		DOCKERMENSUALISECHEZACCONIER,
		DOCKERNONMENSUALISECHEZACCONIER,
		DOCKERNONDELEGUE,
		DOCKERESTDELEGUE,
		DOCKERESTGARDIEN,
		DOCKERNONDELEGUEALADATE,
		DOCKERENCONGE,
		DOCKERENCONGEGARDIEN,
		DOCKERENARRETMALADIE,
		DOCKERENARRETTRAVAIL,
		DOCKERESTDECEDE,
		DOCKERESTINEXISTANTAUGPAC,
		DOCKERESTSAISIENDOUBLE,
		DOCKERSHIFTNUIT,
		DOCKERCATEGORIEDIFFERENTE,
		FONCTIONNONOCCUPEE,
		DOCKERFONCTIONDIFFERENTE,
		MATRICULENONRENSEIGNE,
		DOCKERENACCIDENTTRAVAIL,
		DOCKERENPERMISSIONABSENCE,
		DOCKERSTABILISECHEZUNACCONIER,
		DOCKERAUNEPERMISSIONDABSENCE,
		DATEGARANTIEPRESENCE,
		DOCKERPOINTEENGARANTIEPRESENCE,
		DATEPRISEEFFET,
		DOCKERRETRAITE,
		CALENDRIERNONGENERE,
		DOCKERRADIER,
		DOCKERSUSPENDU,
		DOCKERSANCTIONNE,
		DOCKERNONEMBAUCHE,
		DOCKERNONACTIF,
		DOCKERESTENSOMMEILAUGPAC,
		UNEDATEINCORRECTE,
		PERIODEPAIESUPPR,
		MAUVAISEREGUL,
		NOMBREGARDIENPLUSDE8,
		SANCTIONDOCKER,
		UNDEFINED
		
	}
	public enum ListOfReasonForUnsaved{
		DATEBLACKLISTAGE,
		DROITSINSUFFISANTS,
		DONNEEINEXISTANTE,
		LETTREDIPEUNSET,
		DOCKERDEJAPOINTECHEZUNAUTREACCONIER,
		DATEPRISEEFFET,
		DATEGARANTIEPRESENCE,
		DATEPERMISSIONABSENCE,
		NOMBREJOURSABSENCEMALADIE,
		DOCKERESTDEJADELEGUE,
		DOCKERNESTPASDELEGUE,
		DOCKERDERENPERMISSIONABSENCE,
		UNDEFINED,
		PERIODEPAIE,
		TYPECONTRATINVALID
		
	}
	public enum ListeDesTypesAgents{
		VALIDEUR,
		CONTROLEUR,
		AGENTDESAISIE,
		PREVISIONNISTE,
		FACTURIER,
		UNDEFINED
		
	}
	
	public enum BordereauOpenMode{
		CONSULTATION,
		PREVISION,
		SAISIE,
		CONTROLE,
		VALIDATION,
		CREATION,
		LITIGE,
		DECOMMANDE,
		UNDEFINED
	}	
	
	public enum BordereauAction{
		ENREGISTRE,
		PREREMPLI,
		SAISIE,
		CONTROLE,
		VALIDE,
		DEVALIDE,
		ANNULE,
		SUPPRESSION,
		UNDEFINED
	}	
	public enum TypeDeBordereau{
		MENSUALISE,
		ACCIDENT,
		JOURNALIER,
		REGULARISATION,
		DECOMMANDE,
		GARDIEN,
		GARANTPRES,
		CMTID,
		PERABSENCE,
		DELEGUE,
		EXCEPTIONNEL{public String toString(){return "EXCEPT";}},
		UNDEFINED
	}
	
	public enum WarningOrError{
		WARNING,
		ERROR,
		UNDEFINED
	}
	
	public enum DATABASEENGINE{
		ORACLE,
		MSSQL,
		MYSQL
	}
	
	public enum ETATBORDEREAUX{
		GARDIEN,
		AUTRE
	}
	
	public enum ListDesTraitement{
		STATCOUTS,
		BORDEREAU,
		ACCTRAVAIL,
		BLACKLISTAGE,
		FACTURE,
		STABILISATION,
		STATACTIVITE,
		LITIGE,
		CLOTUREACC
		
	}
	public enum Etat{
		SIMULE,
		DEFINITIF
	}	
	//public enum EtatBorereauAlloc{
		//VALIDE,
		//NON VALIDE
	//}
	public enum Mode{
		CREATION,
		MODIFICATION,
		CONSULTATION,
		DUPLICATION,
		CREATION_SEARCH,
		UNKNOWN
	}
	public enum OperationOfButtonContinuer{
		CHANGEMENTOPERATION,
		SUPPRESSIONDOCKER,
		SUPPRESSIONBORDEREAU,
		SUPPRESSIONUNGARDIENDUBORDEREAU,
		CHARGEMENTEQUIPE,
		CHARGEMENTPOINTAGEMENSUALISE,
		CLOTUREACCONIERFINMOIS,
		CLOTUREACCONIERQUINZAINE,
		CLOTUREGPAC,
		RETOURALARECHERCHE,
		UNKNOWN
	}
	
	public enum Mois{
		JANVIER,
		FEVRIER,
		MARS,
		AVRIL,
		MAI,
		JUIN,
		JUILLET,
		AOUT,
		SEPTEMBRE,
		OCTOBRE,
		NOVEMBRE,
		DECEMBRE
	}
	
	public enum TypeAvancementAutomatique{
		ECHELON,
		CATEGORIE,
		MIXTE
	}
	
	public enum TypePointage{
		POINTAGE,
		PRIME
	}
	
	public enum TYPEARRONDI{
		CENTAINEINF,
		CENTAINESUP,
		MILLIERINF,
		MILLIERSUP,
		AUFRANCINF,
		AUFRANCSUP
	}
			
	/**B�atrice
	 * Converti un objet rubq de type long en String et complete sa longueur � 
	 * longmax par des z�ro � partir de la premiere position 
	 * 
	 * @return  la chaine complet�e.
	 */
	public static String completerCodeRubrique(Long rubq){
		String rub=rubq.toString();
		int leng=rub.length();
		int longmax=4;
		for(int i=longmax-leng;i>0;i--){
			rub="0"+rub;
		}			
		
		return rub;
	}
	/**B�atrice
	 * @param date est une chaine de caract�re qui repr�sente une date au format dd/MM/yyyy
	 * @return le mois et l'ann�e en lettre
	 * exemple: juin 2008
	 */
	public static String convertDate(String date){
		
		String mois;
		ClsDate d=new ClsDate(date, "dd/MM/yyyy");
		Integer aa=d.getYear();
		String annee=aa.toString();
		int mm=d.getMonth();
		switch(mm){
		case 1:{mois="janvier"; return mois+" "+annee;}
		case 2:{mois="f�vrier";return mois+" "+annee;}
		case 3:{mois="mars";return mois+" "+annee;}
		case 4:{mois="avril";return mois+" "+annee;}
		case 5:{mois="mai";return mois+" "+annee;}
		case 6:{mois="juin";return mois+" "+annee;}
		case 7:{mois="juillet";return mois+" "+annee;}
		case 8:{mois="ao�t";return mois+" "+annee;}
		case 9:{mois="septembre";return mois+" "+annee;}
		case 10:{mois="octobre";return mois+" "+annee;}
		case 11:{mois="novembre";return mois+" "+annee;}
		case 12:{mois="decembre";return mois+" "+annee;}
		
		}
			
		return null;
}
	
	
	
	
	/**
	 * Retourne le nom de colonne associ� � l'�num�ration <code> ListDesTraitement<code>
	 * @param en la valeur de l'�num dont on veut le nom de la colonne
	 * @return le nom de la colonne
	 */
	public static String getNomColonneTraitement(ListDesTraitement en){
		String nomcolonnedanstablederegroupement="";
		if(en == ListDesTraitement.STATCOUTS)
			nomcolonnedanstablederegroupement="STATCOUTS";
		else if(en == ListDesTraitement.STATACTIVITE)
			nomcolonnedanstablederegroupement="STATACTIVITE";
		else if(en == ListDesTraitement.BORDEREAU)
				nomcolonnedanstablederegroupement="BORDEREAU";
		else if(en == ListDesTraitement.ACCTRAVAIL)
			nomcolonnedanstablederegroupement="ACCTRAVAIL";
		else if(en == ListDesTraitement.BLACKLISTAGE)
			nomcolonnedanstablederegroupement="BLACKLISTAGE";
		else if(en == ListDesTraitement.FACTURE)
			nomcolonnedanstablederegroupement="FACTURE";
		else if(en == ListDesTraitement.STABILISATION)
			nomcolonnedanstablederegroupement="STABILISATION";
		else if(en == ListDesTraitement.LITIGE)
			nomcolonnedanstablederegroupement="LITIGE";
		else if(en == ListDesTraitement.CLOTUREACC)
			nomcolonnedanstablederegroupement="CLOTUREACC";
		//
		return nomcolonnedanstablederegroupement;
	}
	/**
	 * Retourne le num�ro de colonne associ� � l'�num�ration <code> ListDesTraitement<code>
	 * @param en la valeur de l'�num dont on veut le num�ro de la colonne
	 * @return le num�ro de la colonne
	 */
	public static int  getIntFromEnum(ListDesTraitement en){
		int numerocolonnedanstableregroupement = 0;
		if(en == ListDesTraitement.STATCOUTS)
			numerocolonnedanstableregroupement = 9;
		if(en == ListDesTraitement.STATACTIVITE)
			numerocolonnedanstableregroupement = 7;
		if(en == ListDesTraitement.LITIGE)
			numerocolonnedanstableregroupement = 10;
		else if(en == ListDesTraitement.BORDEREAU)
			numerocolonnedanstableregroupement = 4;
		else if(en == ListDesTraitement.ACCTRAVAIL)
			numerocolonnedanstableregroupement = 5;
		else if(en == ListDesTraitement.BLACKLISTAGE)
			numerocolonnedanstableregroupement = 3;
		else if(en == ListDesTraitement.FACTURE)
			numerocolonnedanstableregroupement = 6;
		else if(en == ListDesTraitement.STABILISATION)
			numerocolonnedanstableregroupement = 8;
		else if(en == ListDesTraitement.CLOTUREACC)
			numerocolonnedanstableregroupement = 11;
		return numerocolonnedanstableregroupement;
	}
	   
    public static int getNombreHeurePassed(String heuredebut, String heurefin){
    	String regex = "[0-2][0-9]:[0-5][0-9]";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(heuredebut);
		Matcher m2 = p.matcher(heurefin);
		if((m != null && ! m.matches()) || (m2 != null && ! m2.matches())){
			return -1;
		}
		String[] vHeuredebut = heuredebut.split(":");
		String[] vHeurefin = heurefin.split(":");
		int i11 = Integer.valueOf(vHeuredebut[0]);
		int i12 = Integer.valueOf(vHeuredebut[1]);
		int i21 = Integer.valueOf(vHeurefin[0]);
		int i22 = Integer.valueOf(vHeurefin[1]);
		//
		if(i11 > 23 || i21 > 23){
			return -1;
		}
		//
		int diff1 = 0;
		int diff2 = 0;
		//
		if(i21 > i11){
			if(i22 >= i12){
				diff1 = i22 - i12;
				diff2 = i21 - i11;
			}
			else{
				diff1 = i22 + 60 - i12;
				diff2 = i21 - (i11 + 1);
			}
		}
		else{
			i21 += 24;
			if(i22 >= i12){
				diff1 = i22 - i12;
				diff2 = i21 - i11;
			}
			else{
				diff1 = i22 + 60 - i12;
				diff2 = i21 - (i11 + 1);
			}
		}
		//
		return diff2;
    }
	
    /**
	 * Retourne l'url d'ex�cution de l'application.<br>
	 * 
	 * @param request l'{@link HttpServletRequest} courant
	 * @return l'url de l'application
	 */
    public static String getApplicationContextUrlPath(HttpServletRequest request){
    	return request.getScheme()+"://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
    }
    public static String getApplicationServletUrlPath(HttpServletRequest request){
    	return request.getScheme()+"://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/" + request.getServletPath();
    }
    /**
	 * Retourne le chemin r�elle du dossier contenant l'application.<br>
	 * 
	 * @param request l'{@link HttpServletRequest} courant
	 * @return le chemin d'acc�s � l'application
	 */
    public static String getApplicationContextRealPath(HttpServletRequest request){
//    	return request.getRealPath(CommonFunctions.CHEMIN_GENERATION_FILES);
    	return request.getSession().getServletContext().getRealPath("/"+CommonFunctions.CHEMIN_GENERATION_FILES);
    }

	
	public static String sqlInstringFromListOfString(List<String> l){
		if(l == null || l.size() == 0)
			return "()";
		String sqlin = "(";
		int i = 0;
		for (String s : l) {
			sqlin += "'" + s + "'";
			if(i < l.size() - 1)
				sqlin += ",";
			i++;
		}
		sqlin += ")";
		//
		return sqlin;
	}

	public static String sqlInstringFromListOfString(String[] l){
		if(l == null || l.length == 0)
			return "()";
		String sqlin = "(";
		int i = 0;
		for (String s : l) {
			sqlin += "'" + s + "'";
			if(i < l.length - 1)
				sqlin += ",";
			i++;
		}
		sqlin += ")";
		//
		return sqlin;
	}
	
	public static String[] getCatAndEchelon(String catech){
		if(StringUtils.isEmpty(catech)) 
			return new String[]{"",""};
		int n = catech.length();
		String[] s = new String[2];
		s[0] = catech.substring(0, 2);
		s[1] = catech.substring(2, n);
		return s;
	}
	
	public static String formatString(String valeur, int nb){
		while(valeur.length() < nb){
			valeur = "0".concat(valeur);
		}
		return valeur;
	}
	
	/**
	 * @param rubrique
	 * @return la rubrique compl�t�e
	 */
	public static String completeRubrique(String rubrique){
		int reste = 4-rubrique.length();
		if(reste > 0 && reste < 4){
			for(int i = 1; i <= reste; i++){
				rubrique = "0" + rubrique;
			}
		}
		
		return rubrique;
	}
	public  static String[] findAndReplaceTreatmentInRequete(String requete,String cdos,Date ddmp){
		int index1=0;
		int index2=0;
		
		String chaine="";
		String stringToReplace="";
		int nbVarToReplace=0;
		int decrement=0;
		String decre="";
		if(ddmp==null){
			nbVarToReplace++;
		}
		else{
			index1=requete.indexOf("P_AAMM<");
			while(index1!=-1){		
				 chaine=requete.substring(index1);
				index2=chaine.toLowerCase().indexOf(">");
				if(index2==-1){
					nbVarToReplace++;
				}
				else {
					decre=chaine.substring("P_AAMM<".length(), index2);
					decrement=new Integer(decre);
					ClsDate d=new ClsDate(ddmp);
					Date debut=d.addMonth(-decrement);
					
					String aamm=(new ClsDate(debut)).getDateS("yyyyMM");
					stringToReplace=chaine.substring(0, index2+1);
					requete=requete.replace(stringToReplace, "'"+aamm+"'");
				}
				index1=requete.indexOf("P_AAMM<");
			}
		}
		String []s={requete,(new Integer(nbVarToReplace)).toString()};

			return s;
	}
	/**
	 * @param requete: requete sql qui contient des variables( ou alors param�tres)
	 * @param cdos
	 * @param nmat
	 * @param aamm : Dernier mois de paie clotur� (au format yyyyMM)
	 * @param montantPret: Montant du pr�t sollicit�
	 * @param nbrEcheance : Nombre d'�ch�ance du pr�t
	 * @param Date1eEche : date de la premi�re �ch�ance du pr�t
	 * @return
	 */
	public  static String findAndReplaceParameterInRequete(String requete, String cdos, String nmat, String aamm, double montantPret, int nbrEcheance,String Date1eEche,
			String rubcumul, int ageScolMax){
		
		requete=requete.replace("P_MNTP",String.valueOf(montantPret));
		requete=requete.replace("P_NBEC",String.valueOf(nbrEcheance));
		requete=requete.replace("P_CDOS", "'"+cdos+"'");	
		requete=requete.replace("P_AAMM", "'"+aamm+"'");
		requete=requete.replace("P_NMAT", "'"+nmat+"'");
		requete=requete.replace("P_DATE1ECH", "'"+Date1eEche+"'");
		requete=requete.replace("P_TODAY","'"+ClsDate.getDateS(new Date(), "dd/MM/yyyy")+"'");
		requete=requete.replace("P_RUBCUMUL", "'"+rubcumul+"'");
		requete=requete.replace("P_AGESCOLMAX", String.valueOf(ageScolMax));
				
		return requete;
	}
	
	public static String getModeFromIceMode(Mode icemode)
	{
//		if(Mode.CONSULTATION.equals(icemode))
//			return ParameterUtil.MODE_CONSULTATION;
//
//		if(Mode.CREATION.equals(icemode))
//			return ParameterUtil.MODE_AJOUT;
//
//		if(Mode.DUPLICATION.equals(icemode))
//			return ParameterUtil.MODE_DUPLICATION;
//
//		if(Mode.MODIFICATION.equals(icemode))
//			return ParameterUtil.MODE_MODIFICATION;
		
		return null;
	}
	
	public static Mode getIcemodeFromMode(String mode)
	{
//		if(ParameterUtil.MODE_CONSULTATION.equals(mode))
//			return Mode.CONSULTATION;
//
//		if(ParameterUtil.MODE_AJOUT.equals(mode))
//			return Mode.CREATION;
//
//		if(ParameterUtil.MODE_DUPLICATION.equals(mode))
//			return Mode.DUPLICATION;
//
//		if(ParameterUtil.MODE_MODIFICATION.equals(mode))
//			return Mode.MODIFICATION;
		
		return null;
	}
}
