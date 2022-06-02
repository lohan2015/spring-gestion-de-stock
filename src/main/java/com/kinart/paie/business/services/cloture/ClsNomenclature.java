package com.kinart.paie.business.services.cloture;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ClsNomenclature implements Serializable,Comparable{
	//ctabPointage, ctabTypeheure, ctabPeriode, ctabShift, ctabTypejour
	public static final int GPAC_TABLE_DES_ACCONIERS = 1;//ok
	
	public static final int GPAC_TABLE_DES_TYPES_PREVISIONS = 755;//ok
	
	public static final String GPAC_TABLE_ACCONIERS = "1";//ok
	
	public static final int GPAC_PROFIL_FONCTION_METIER = 75;//ok
	public static final int GPAC_NBRE_MAX_LIGNE_DIPE = 15; 
	public static final int GPAC_TABLE_DES_OPERATIONS = 715;//ok
	public static final int GPAC_TABLE_DES_UNITES = 708;//ok
	public static final int GPAC_TABLE_DES_CADENCES = 725; 
	public static final int GPAC_TABLE_DES_NATUREMARCHANDISE = 719;//ok
	public static final int GPAC_TABLE_DES_POINTAGES = 714;//ok
	public static final int GPAC_TABLE_DES_PRIMES_OPERATION = 727;//ok
	public static final int GPAC_TABLE_DES_POINTAGES_OPERATION = 716;//ok
	public static final int GPAC_TABLE_DES_TYPES_DECOMMANDE = 740;//ok
	public static final int GPAC_TABLE_DES_POINTAGES_DECOMMANDE = 739;//ok
	public static final int GPAC_TABLE_DES_PRIMES_DECOMMANDE = 741;//ok
	public static final int GPAC_TABLE_DES_TYPESHEURE = 713; 
	public static final int GPAC_TABLE_DES_PERIODES = 712;//ok
	public static final int GPAC_TABLE_DES_TYPESJOUR = 720; 
	public static final int GPAC_TABLE_DES_TYPES_BORDEREAUX = 730; 
	public static final int GPAC_TABLE_DES_RESPONSABLE_ACCONAGE= 721; 
	public static final int GPAC_TABLE_DES_TAUX_HORAIRES = 722; 
	public static final int GPAC_TABLE_DES_SHIFTS = 707;//ok
	public static final int GPAC_TABLE_DES_HEURES_SHIFTS = 713;//ok
	public static final int GPAC_TABLE_DES_PRIMES = 709;//ok
	public static final int GPAC_TABLE_DES_CHANTIERS = 711;//ok
	public static final int GPAC_TABLE_DES_ESCALES = 718;//ok
	public static final int GPAC_TABLE_DES_ETATS_BORDEREAUX = 731;//ok
	public static final int GPAC_TABLE_DES_LIEUX = 717;//ok
	public static final int GPAC_TABLE_DES_SECTIONS = 724;//ok
	public static final int GPAC_TABLE_DES_SITES = 723;//ok
	public static final int GPAC_TABLE_DES_FONCTIONS_DELEGUES = 710;//ok 
	public static final int GPAC_TABLE_DES_FONCTIONS = 7;//ok
	public static final int GPAC_TABLE_DES_CAISSES = 799;//ok
	public static final int GPAC_TABLE_DES_EQUIPES = 726;//ok
	public static final int GPAC_TABLE_DES_NAVIRES = 734;//ok
	public static final int GPAC_TABLE_DES_PRIMES_TRAVAILAFINIR = 729;//ok
	public static final int GPAC_TABLE_DES_PROFILS_UTILISATEUR = 704;//ok
	public static final int GPAC_TABLE_DES_MOTIFS_PERMISSIONS_ABSENCES = 732;
	public static final int GPAC_TABLE_DES_GRATIFICATIONS_HISTORIQUES = 735;
	public static final int GPAC_TABLE_DES_TAUX_INDEMNISATIONS_MALADIE = 736;
	public static final int GPAC_TABLE_DES_POSTES_DELEGUES = 737;
	public static final int GPAC_TABLE_DES_PRIMES_DELEGUES = 738;
	public static final String GPAC_TAB_NOM_PROV_RETRAITE = "800"; 
	public static final String TABLE_NOM_OP_CAISSE = "842";
	public static final String GPAC_TAB_NOM_BULLETINS = "804";
	public static final String GPAC_TAB_NOM_PRINTETATS = "805";
	public static final int GPAC_TABLE_DES_TYPES_ARRET_MALADIE_POUR_ACCIDENT = 742;
	public static final int GPAC_TABLE_DES_POINTAGES_GARANTIE_PRESENCE = 743;
	public static final int GPAC_TABLE_DES_PRIMES_DIRECTES_OPERATIONS = 744;
	public static final int GPAC_TABLE_DES_TYPES_HEURES_DE_POINTAGE = 745;	
	public static final String GPAC_TAB_NOM_EDIT_CONGE = "801"; 
	public static final int GPAC_TABLE_LISTE_DES_DECADES = 746;
	public static final String GPAC_TAB_NOM_EDIT_RETRAITES = "802";
	public static final String GPAC_TAB_NOM_EDIT_DECES = "803";
	public static final String GPAC_TAB_NOM_ETATS_FISCAUX = "806";
	public static final String GPAC_TAB_NOM_STATISTIQUES = "807";
	public static final String GPAC_TAB_NOM_STATISTIQUES_MONTANT = "815";
	public static final String GPAC_TAB_ETAT_IMPRIMABLE_SUR_FORMULAIRE = "809";
	public static final String GPAC_TAB_PRODUIRE_STATISTIQUES_DOCKERS = "810";
	public static final String GPAC_TAB_PRODUIRE_STATISTIQUES_GARDIENS = "816";
	public static final String GPAC_TAB_PRODUIRE_STATISTIQUES_AUTRES = "825";
	public static final String GPAC_TAB_EDITIONS_PRETS = "820";
	public static final String GPAC_TAB_PRODUIRE_STATISTIQUES_ACCONIER = "811";
	public static final String GPAC_TAB_EDITIONS_STABILISATION = "817";
	public static final String GPAC_TAB_NOM_AVANCEMENTS = "812";
	public static final String GPAC_TAB_NOM_PECULE_RETRAITE = "813";
	public static final String GPAC_TAB_NOM_ETATS_PAIEMENT = "814";
	public static final String GPAC_TAB_NOM_BILLETAGE_QUINZAINE = "822";
	public static final String GPAC_TAB_NOM_ETATS_NON_STANDARDS = "821";
	public static final String GPAC_TAB_NOM_BORDEREAUX = "824";
	public static final String GPAC_TAB_EDITION_LIVRE_PAIE = "808";
	public static final int GPAC_TYPE_RETRAITE = 706;
	public static final int GPAC_PARAMETRAGE_PERIODE_TRAITEMENT = 747;
	public static final int GPAC_PARAMETRAGE_CADENCE_OP = 749;
	public static final int GPAC_OPERATIONS_GARDIEN = 751;
	public static final int GPAC_PRIMES_DELEGUES = 752;
	public static final int TABLE_HORAIRES_BORDX_GARDIENS = 756;
	public static final int TABLE_TERMINAUX_FIXES = 757;
	public static final int GPAC_TABLE_AFFECTATION_IMM_DIFF = 758;
	public static final int GPAC_TABLE_PERIODE_BORDX_GARDIENS = 759;
//	public static final int GPAC_TABLE_BRDX_JOUR_PREV = 760;
	public static final String GPAC_STATS_BIOMET = "829";
	public static final String GPAC_EDITION_DIPES_MAGNETIQUES = "830";
	
	public static final String CLE_ETAT_PRET = "ETATPRET";
	public static final String GPAC_ETAT_PRET = "792";
	public static final int ETABLISSEMENTS_PRETEURS=793;
	
	public static final int TABLE_ECHELON=79;
	public static final int TABLE_CATEGORIE=76;
	public static final int TABLE_ALERTES=997;
	public static final String LISTE_TABLES_CHARGES = "1000";
	
	public static final int TABLE_DES_SCHEMAS = 918;
//	====================GESTION DES SALARIES========================
	public static final String INFORMATION_SALARIE="30";//ok
	public static final String TYPE_MOUVEMENT="204";//ok
	public static final String MOTIF_FIN_CONTRAT="23";//ok
	public static final String RUBRIQUE_FIN_CONTRAT="47";//ok
	public static final String SITE_TRAVAIL="274";//ok
	public static final String NIVEAU_FONCTION="278";//ok
	public static final String MOTIF_MOUVEMENT="202";//ok
	public static final String MOTIF_TRANSFERT="203";//ok
	public static final String LIEU_TRAVAIL="253";//"31";
	public static final String ZONE="31";
	public static final String ZONE_RESIDENCE="24";
	public static final String CATEGORIE_ECHELON="32";
	public static final String CATEGORIE="132";
	public static final String ECHELON="133";
	public static final String TABLE_EDITION_SALARIE="301";//ok
	public static final String TABLE_EDITION_EVOLUTION_SALAIRE="300";//ok
	public static final String TABLE_FORMAT_EDITION="366";//ok
	public static final String TABLE_EDITION_PLAN_FORMATION= "375";//ok
	public static final String TABLE_EDITION_SAISIE_RAPPORT_FORMATION= "377";//ok
	public static final String TABLE_EDITION_EXXON_ALTAS_EFT_AWPS= "378";//ok
	public static final String GPAC_TABLE_EDITION_LIVRE_DE_PAIE= "808";//ok
	public static final String TABLE_EDITION_STATISTIQUES_FORMATION="383";//ok
	public static final String TABLE_EDITION_CODIFICATIONNOTE="7501";//ok	
	
	
	public static final String TABLE_EDITION_SIMUL_FORMATION= "376";//ok
	public static final String TABLE_EDITION_DECL_SALAIRES= "384";
	

	
	public static final String TABLE_EDITION_COMPTE_RENDU_POSTE="374";//ok
	public static final String TABLE_EDITION_CANDIDAT_ADEQUAT="373";//ok
	public static final String TABLE_EDITION_FICHE_CANDIDAT="395";//ok
	
	public static final String TABLE_EDITION_APPRECIATION_COMPETENCES = "392";//ok
	
	public static final String 	TABLE_EDITION_BESOIN_RECRUTEMENT ="372";//ok
	
	public static final String TABLE_ETAT_IMPRIMABLE_SUR_FORMULAIRE = "337";
	
	public static final String 	STATUT_CAMPAGNE_RECRUTEMENT ="284";
	public static final String 	STATUT_RECRUTEMENT =STATUT_CAMPAGNE_RECRUTEMENT;

	
	
	public static final String TABLE_EDITION_DEPART_SALARIE="302";//ok
	public static final String TABLE_EDITION_POSTE_EMPLOITYPE="303";//ok
	public static final String MOTIF_ARRET_PAIE="21";//ok
	public static final String ETABLISSEMENT="1";
	public static final String NIVEAU1=ETABLISSEMENT;
	public static final String DEPARTEMENT="2";
	public static final String NIVEAU2=DEPARTEMENT;
	public static final String SECTION="3";
	public static final String NIVEAU3=SECTION;
	public static final String EQUIPE="20";
	public static final String CODE_SALAIRE="200";
	public static final String NATIONALITE="4";
	public static final String FONCTION="7"; 
	public static final String FONCTIONS_DELEGUES="794";
	public static final String TITRE="6"; 
	public static final String CONTRAT="9"; 
	public static final String MOTIF_CAT ="26";
	public static final String INDICE ="33";
	public static final String PAYS= "74";
	public static final String SITUATION_FAMILIALE="55";
	public static final String SEXE="205";//ok
	public static final String CIVILITE="207";
	public static final String CLASSE="8";
	public static final String DIPLOME="16";
	public static final String LANGUE="72";
	public static final String NIVEAUX_LANGUE="73";
	public static final String STATUT="13";
	public static final String ACCIDENT_TRAVAIL="53";
	public static final String AVANTAGE_EN_NATURE="36";
	public static final String DOMESTICITE="25";
	public static final String DEVISE_PAIEMENT="27";
	public static final String VILLE_DECLARATION="5";
	public static final String ANCIENNETE="34";
	public static final String FILIATION="70";
	public static final String STAGE="15";
	//Op�ration �pargne
	public static final String TABLES_EPARGNE_RETRAITE="790";
	public static final String BANQUE="10";
	public static final String MODE_PAIEMENT="54";
	public static final String COURS="14";
	public static final String DISTINCTION="18";
	public static final String NOTATION="19";
	public static final String SANCTION="17";
	
	public static final String MOTIF_DEPART="202";
	public static final String HIERARCHIE_FONCTION_PUBLIQUE="214";
	public static final String POSITION_CIVILITE="215";	
	public static final String NATURE_DOCUMENT="71";
	public static final String TYPE_DOCUMENT=NATURE_DOCUMENT;
	public static final String FAMILLE_DOCUMENT=FILIATION;//Filiation qui poss�de le document
	public static final String VIVANT_DECEDE="210";
	public static final String OUI_NON="210";
	public static final String CLE_PARAMETRAGE_ALGORITHMES="30";
	public static final String ETAT_PRET="213";
	public static final String DECISION_STAGE="212";
	public static final String DECISION_COURS="212";
	
	public static final String FILIERE="106";//ok
	public static final String SPECIALITE="107";//ok
	public static final String SEXES_POSTE="206";//ok
	public static final String TYPE_FORMATION="77";
	public static final String NIVEAU_FORMATION="78";
	public static final String HORAIRE="110";//ok
	public static final String DISPONIBILITE="111";//ok
	public static final String MOBILITE="112";//ok
	public static final String NUISANCE="113";//ok
	public static final String COULEUR="211";//ok
	public static final String AUTRES_PREREQUIS_FORMATION="134";//ok
	public static final String NBRE_ANNEE_EXPERIENCE="135";//ok
	public static final String BESOIN_MATERIEL="715";//ok
	

//LDAP
	public static final String ATTR_NOMENCLATURE="308";//ok
	public static final String CLASS_NOMENCLATURE="307";
//	====================GESTION DES RUBRIQUES========================
	public static final String TYPE_RUBRIQUE="28";
	public static final String SAISIE_AUTORISEE_RUBRIQUE="219";
	public static final String RUBRIQUE_FICTIC="220";
	public static final String CALCUL_DERNIER_BULLETIN_FICTIC="221";
	public static final String BASE_CALCUL="222";
	public static final String CONSERVATION_LIGNE_BULLETIN_CONGE="223";
	public static final String PRESENTATION_BULLETIN="43";
	public static final String PERIODE_CALCUL_BULLETION="224";
	public static final String FREQUENCE_CALCUL_BULLETIN="225";
	public static final String ALGORITHME_CALCUL="11";
	public static final String ARRONDI_RUBRIQUE="63";
	public static final String PLAFOND="96";
	public static final String SEXE_COMPLET=SEXES_POSTE;
	public static final String TAUX_MONTANT_DIVISEUR="229";
	
	public static final String TABLE_EDITION_RUBRIQUE="304";
	
	public static final String TABLE_EDITION_CUM_SAL="326";   
	public static final String TABLE_EDITION_ETA_PARAM_CUM="327";   
	public static final String TABLE_EDITION_DDAS="329";
	public static final String TABLE_EDITION_FIC_NIV_CUM="328";
	public static final String TABLE_EDITION_FIC_MOIS_CUM="339";	
	public static final String TABLE_EDITION_JR_PAIE="330";
	public static final String TABLE_EDITION_JR_PAIE_CUM="337";
	public static final String TABLE_EDITION_RUB_CUM="334";		
	public static final String TABLE_EDITION_RUB_PER_CUM="335";	
	public static final String TABLE_EDITION_TAB_CUM="336";
	
	
// ===================GESTION DES PRETS=====================================
	public static final String TABLE_EDITION_FICH_PRET="340";   
	public static final String TABLE_EDITION_RECAP_PAR_SAL="341";   
	public static final String TABLE_EDITION_RECAP_PAR_RUB="342";	
	public static final String TABLE_EDITION_SIT_AVCE="343";	
	public static final String TABLE_EDITION_SIT_ENG="344";		
	public static final String TABLE_EDITION_SIT_AVCE_PAR_MOIS="345";
	
//=====================remuneration/Pr�paration=============================
	public static final String REMUN_PREPA_EDT_ELT_VAR="355";   
	public static final String REMUN_PREPA_EDT_RUB_SAISIE="356";   
	public static final String REMUN_PREPA_EDT_BILL_EV="357";
	public static final String REMUN_PREPA_EDT_ACCPT_EV="358";   
	public static final String REMUN_PREPA_EDT_SANS_EV="359";   
	public static final String REMUN_PREPA_EDT_VIR_ACCPT_QZNE="360";
	public static final String REMUN_PREPA_EDT_VIR_ACCPT_CONGE="361";
	
//=====================remuneration/calcul/edition==========================
	public static final String REMUN_CAL_EDT_BUL_CONTROLE="367";
	public static final String REMUN_CAL_EDT_BUL_FICTIF="368";   
	public static final String REMUN_CAL_EDT_BUL ="362";   
	public static final String REMUN_CAL_EDT_CTRL_BUL ="363";   
	public static final String REMUN_CAL_EDT_SAL_SANS_BUL ="364";
	public static final String REMUN_CAL_EDT_SOLDE_TT_CMPTE ="365";
	
//====================Menu Administration fonctionnelle/Nomenclature========
	public static final String EDIT_NOM_STRUCT="351";
	public static final String EDIT_NOM_DONNEE="352";
	
// ====================REMUNERATION/FICHIER======================
	public static final String REMUN_FICH_EDT_ELT_FIXE="346";   
	public static final String REMUN_FICH_EDT_PRT_INTERN="347";   
	public static final String REMUN_FICH_EDT_ABSCE_CONGE="348";	
	public static final String REMUN_FICH_EDT_ETA_EV_DBLE="349";
	
//	==================GESTION DES CANDIDATS ET DU RECRUTEMENT===============
	public static final String CABINET_RECRUTEMENT="233";
	public static final String TYPE_RECRUTEMENT="234";
	public static final String STATUT_CANDIDATURE="238";
	public static final String CHAMP_ADEQUATION_POSTE="237";
	public static final String COMPTAGE="239";
	public static final String FORMATION="115";
	public static final String CARRIERE_LECTURE="279";
	public static final String CARRIERE_AUTRE_ACTION="280";
	public static final String CARRIERE_DISPONIBILITE_POSTE=DISPONIBILITE;
	public static final String LECTURE=CARRIERE_LECTURE;
	public static final String AUTRE_ACTION_COMPETENCE=CARRIERE_AUTRE_ACTION;
	public static final String DISPONIBILITE_POSTE=DISPONIBILITE;
	public static final String PREFERENCE_POSTE="281"; 
	
//	==================EDITIONS RECRUTEMENT===============
	public static final String TABLE_EDITION_EVALUATION_RECRUTEMENT = "382";//ok
	
	public static final String TYPE_CANDIDAT="230";
	public static final String SECTEUR_ACTIVITE="106";//Fili�re?
	public static final String THEME     = "101";
	public static final String DOMAINE_COMPETENCE     = "102";
	public static final String NIVEAU_THEME="209";
	public static final String TACHE="119";
	public static final String AVANTAGE_HORS_PAIE="235";
	public static final String SAVOIR_ETRE="218";
	public static String OBJECTIF_INTERMEDIAIRE = "227";
	public static String STANDARD_PERFORMANCE = "228";
	public static String DOMAINE_CLE = "226";
	public static String MISSION= "291";
	public static String TYPE_FONCTION= "231";
	
	// ============================ REPRISE APRES RADIATION ====================
	public static String TABLE_REPRISE_APRES_RADIATION="825";
	
//	==================GESTION FICHES EVALUATION===============
	public static String SECTION_FICHE_EVAL= "240";
	public static String TYPE_CRITERE_EVAL= "241";
	public static String CRITERE_EVAL_DESC= "246";
	
//	==================GESTION DES EVALUATIONS===============
	public static String EVAL_PHASE= "257";
	public static String EVAL_TYPE_CRIT= "255";
	public static String EVAL_STATE_CRIT= "258";
	public static String EVAL_RESULT= "256";
	public static String EVAL_APPR_BUDGET= "259";
	public static String EVAL_APPR_QUAL= "260";
	public static String EVAL_APPR_SATIS= "261";
	public static String EVAL_APPR_DIST= "262";
	public static String PRESTATAIRES ="201";
	public static final String TABLE_EDITION_ECART_COMPETENCES_POSTES = "390";
	public static final String TABLE_EDITION_BESOIN_FORMATION = "391";
	public static final String TABLE_EDITION_TAUX_EVALUATIONS = "392";
	
//	===========================GESTION DES FORMATIONS======================
	public static final String CODE_EMPLOI_SALARIE="242";
	public static final String TYPE_FORMATION_LIEU="243";
	public static final String ACTION_FORMATION=TYPE_FORMATION_LIEU;
	public static final String PROJET="244";
	public static final String THEME_FORMATION="101";
	public static final String DOMAINE_FORMATION="102";
	public static final String PRIORITE_FORMATION="263";
	public static final String 	TAB_NOM_EVAL ="380";
	public static final String 	TAB_NOM_ENTRETIEN_EVAL ="387";
	public static final String 	TAB_NOM_MODEL_EVAL ="388";
	//public static final String DOMAINE_FORMATION="245";
	public static final String ORGANISME_FORMATEUR="248";
	public static final String COUT_FORMATION="249";
	public static final String NATURE_COUT_FORMATION="250";
	public static final String RAPPORT_FORMATION="251";
	public static final String ORGANISME_PAYEUR="252";
	public static final String MOTIF_ABSENCE="22";
	public static final String TYPE_BESOIN_FORMATION="265";
	public static final String NATURE_FORMATION="286";
	public static final String STATUT_FORMATION="287";
	public static final String COLONNE_MONTANT_A_FACTURER_ABSENCE="2";//9 normalement;
	public static final String STATUT_INSCRIPTION="288";
	public static final String STATUT_SESSION="289";
	public static final String MODE_AFFICHAGE_PLAN_FORMATION="290";
	public static final String ASSOCIATION_RAPPORT_MODELE_EVALUATION="293";
	public static final String INTEGRATION_PREDEFINIES_FICHE_SALARIE="297";
	//===========================GESTION WORKFLOW===========================
	public static final String TYPE_INSTANCE_WF="294";
	public static final String STATUT_INSTANCE_WF="295";
	//	===========================GESTION DES BESOINS DE FORMATIONS==============
	public static final String OBJECTIFS_FORMATIONS="247";
	public static final String STATUTS_WORKFLOW="254";
	
	
//	===========================GESTION DE L'EVALUATION==============
	public static final String COMMENTAIRE_EVALUATION="254";
	public static final String TABLE_EDITION_MODELE_EVALUATION ="7502";
	public static final String TABLE_EDITION_EDITIONS_COMPETENCE = "381";
		
//	===========================GESTION DE LA PAIE================================
	public static final String TABLE_EDITION_POST_PAIE="306";
	public static final String TABLE_EDITION_RECAP_NAP_PAR_SERVICE="329";
	public static final String TABLE_EDITION_RECAP_PAR_SERVICE_CUM="338";
	
	public static final String TABLE_EDITION_BILLETAGE="311";
	public static final String TABLE_EDITION_CHEQUE_EMETTRE="312";
	public static final String TABLE_EDITION_VIREMENT="325";
	public static final String TABLE_EDITION_REFERENCES_PAIEMENT="313";
	public static final String TABLE_EDITION_PAR_RUB="314";
	public static final String TABLE_EDITION_ET_GEN_PAR="315";
	public static final String TABLE_EDITION_JOUR_PAIE="316";
	public static final String TABLE_EDITION_TAB_CHAR="317";
	public static final String TABLE_EDITION_BAL_PAIE="318";
	public static final String TABLE_EDITION_JOUR_RUB="319";
	public static final String TABLE_EDITION_EVOL_SAL="320";
	public static final String TABLE_EDITION_RECP_HS="321";
	public static final String TABLE_EDITION_TAB_SUIV_AV="322";
	public static final String TABLE_EDITION_LIV_PAIE="323";
	public static final String TABLE_EDITION_RECP_EL_VAR="324";
	public static final String TABLE_PERIODE_MASSE_SALAIRE="465";
	public static final String TABLE_GROUPE_MASSE_SALAIRE="466";
	public static final String TABLE_EDITION_MASSE_SALAIRE="467";
	public static final String TABLE_EDITION_CONTROLE_RUBRIQUES="911";
	
	
	public static final String TABLE_EDITION_AVANCES_COMILOG="1024";
	//===================== GESTION DE LA CHARGEMENT NOMENCLATURE ==============
	public static final String COLONNES_TABLE_NOMEN="1025";
	//===========================GESTION DE LA SECURITE SALARIE==============
	public static final String COLONNES_TABLE_AGENT="264";
	public static final String ONGLETS_FICHE_SALARIE="275";
	//===========================PARAMETRAGES PORTAIL==============
	public static final String TABLE_PARAMETRAGE_PORTAIL="699";
	public static final String TABLE_CONDITIONS_PORTLETS="698";
	public static final String TABLE_MAPPING_COLONNES_MOTS_CLES="697";
	public static final String TABLE_PARAMETRAGE_PECULE="696";
	public static final String TABLE_PARAMETRAGE_PRIMES="695";
	public static final String TABLE_CHAMPS_MODIFIABLE_SALARIE="602";
	public static final String TABLE_TABLES_MODIFIABLE_SALARIE="607";
	public static final String TABLE_EDITION_HISTORIQUE_DEMANDE="690";
	//===========================GESTION DEMANDE CONGES ET ABSENCES==============
	public static final String TABLE_TYPES_ABSENCE="610";
	public static final String TABLE_CONDITIONS_ABSENCE="611";
	
	//===========================GESTION DEMANDE ASSISTANCES SCOLAIRES==============
	public static final String TABLE_ETABLISSEMENTS_SCOLAIRE="841";
	public static final String TABLE_PAYS="74";
	public static final String TABLE_NIVEAU_ETUDE="809";
	
	//===========================EVOLUTION SALARIE==============
	public static final String COLONNES_TABLE_AGENT_SALAIRE="369";
	
	public static final String GROUPE_RUBRIQUE_EVOL_SALAIRE="370";
	//les types de profil pour la gestion de la scurit� salari�e
	public static final String TYPE_PROFIL_SECURITE_SALARIE="269";
//	===========================FORMAT DE LA DATE==============
	public static final String FORMTAT_DATE="80";
	
//	===========================PARAMETRES DE CONFIG==============
	public static final String CONFIGURATION_PARAMETERS="266";
	
	
//	===========================PARAMETRAGE DE LDAP==============
	public static final String LDAP_CLASSES="307";
	public static final String LDAP_CLASSES_ATTRIBUTES="308";
	public static final String LDAP_CLASSES_ATTRIBUTES_ASS="309";
	
//	===========================PARAMETRAGE DE LDAP==============
	public static final String TYPES_DE_DONNEES="267";
//	===========================PARAMETRAGE DE WFW==============
	public static final String NOMS_DE_COLONNES="268";
//	===========================GROUPE DES POIDS pOSTE==============	
	public static final String GROUPE_POIDS_POSTE="270";
	public static final String SSGROUPE_POIDS_POSTE="271";
	
	public static final String SAVOIR_FAIRE_FAIRE="272";
	
	public static final String TYPE_DONNEES_ZONES_LIBRES="273";
	
	public static final String TYPE_DONNEES=TYPE_DONNEES_ZONES_LIBRES;
	
	public static final String TABLES_PARAMETRAGES_SYSTEME="99";
	public static final String TABLES_PARAMETRAGES_ETATS="599";
	
	public static final String TABLE_MODE_CALCUL_PAIE="276";
	public static final String TABLE_ETAT_PRET="277";
	
	public static final String SAVOIR= "797";
	public static final String RELATIONS_INTERNES= "798";
	public static final String RELATIONS_EXTERNES= "799";
//	===========================GROUPE DES POIDS pOSTE==============	
	public static final String TABLE_CARRIERE_QUESTIONS = "282";//ok
	public static final String TABLE_CARRIERE_QUESTIONS_NOTES = "283";//ok
	public static final String TABLE_EVALUATION_TITRE_COMMENTAIRES = "285";//ok
	public static final String TABLE_DECISIONS_EVALUATION = "292";

	public static final String TABLE_EDITIONS_SPECIFIQUES_PAIE_SNCC = "906";
	public static final String TABLE_FORMULES_EDITIONS_SPECIFIQUES_PAIE_SNCC = "907";
	


//  ===========================EtatsFiscaux===================
	public static final String TABLE_ETAT_FISCAUX_TCHAD = "385";
	public static final String TABLE_ETAT_FISCAUX_DECLARATIONS_ANNUELLES_TCHAD = "329";
	public static final String TABLE_PARAMETRAGES_EF_TCHAD1="589";
	public static final String TABLE_PARAMETRAGES_EF_TCHAD2="590";
	public static final String TABLE_PARAMETRAGES_EF_TCHAD3="591";
	//public static final String TABLE_CNPS_REC = "386";
	public static final String TABLE_ETAT_FISCAL_ALGERIE_DAS = "393";
	public static final String TABLE_ETAT_FISCAL_MAURITANIE_DIVERS = "451";
	public static final String TABLE_ETAT_FISCAL_ALGERIE_DSSM = "394";
	public static final String TABLE_ETAT_FISCAL_CI_CNPS_MENS = "396";
	public static final String TABLE_ETAT_FISCAL_CI_DM_IMPOTS_SAL = "397";
	public static final String TABLE_ETATS_FISCAUX_CI_FIN_EXC = "398";
	public static final String TABLE_ETATS_FISCAUX_CI_DISA = "399";
	public static final String TABLE_ETAT_FISCAL_FIN_ANNEE_GABON = "95";
	public static final String TABLE_ETAT_FISCAL_GABON_MENS31 = "401";
	public static final String TABLE_ETAT_FISCAL_GABON_CNSS_TRIM = "402";
	public static final String TABLE_ETATS_FISCAUX_GABON_FINEXC_INDIV_RECAP = "403";
	public static final String TABLE_ETAT_FISCAL_GABON_FINEXC_MODELE_36 = "404";
	public static final String TABLE_PARAMETRAGES_EF_GABON="595";
	public static final String TABLE_ETAT_FISCAL_SENEGAL_CSS_MENS = "405";
	public static final String TABLE_ETAT_FISCAL_SENEGAL_IPRES_MENS = "406";
	public static final String TABLE_ETAT_FISCAL_SENEGAL_CSS_AN = "407";
	public static final String TABLE_ETAT_FISCAL_SENEGAL_IPRES_AN = "408";
	public static final String TABLE_ETAT_FISCAL_SENEGAL_FIN_EXC = "409";
	public static final String TABLE_ETAT_FISCAL_SENEGAL_FIN_EXC_INDIV = "410";
	public static final String TABLE_ETATS_FISCAUX_GHANA = "411";
	public static final String TABLE_ETAT_FISCAL_BENIN_IPTS = "412";
	public static final String TABLE_ETAT_FISCAL_BENIN_PE_CI_SS = "413";
	public static final String TABLE_ETAT_FISCAL_BENIN_LN_PERS = "414";
	public static final String TABLE_ETAT_FISCAL_NIGER_CNSS_TRIM = "415";
	public static final String TABLE_ETAT_FISCAL_NIGER_ANNUEL = "416";
	public static final String TABLE_ETAT_FISCAL_CONGO_MENS_FIN_EXC = "417";
	public static final String TABLE_ETAT_FISCAL_CONGO_RECAP_CNSS_AN = "418";
	public static final String TABLE_ETAT_FISCAL_BURKINA_CSS_MENS = "419";
	public static final String TABLE_ETAT_FISCAL_BURKINA_TRIM_NOM = "420";
	public static final String TABLE_ETAT_FISCAL_BURKINA_DAREC_IMSAL = "421";
	public static final String TABLE_ETAT_FISCAL_MAURITANIE_LNOM_PERS = "422";
	public static final String TABLE_ETAT_FISCAL_MAURITANIE_DMENS_ITS = "423";
	public static final String TABLE_ETAT_VARIABLES_MOIS = "4233";
	public static final String TABLE_ETAT_FISCAL_MAURITANIE_PARAMETRAGE_RUBRIQUE = "589";
	public static final String TABLE_ETAT_FISCAL_MALI_DAS= "424";
	public static final String TABLE_ETAT_FISCAL_MALI_INPS_MENS= "425";
	public static final String TABLE_ETAT_FISCAL_MALI_ITS_MENS= "426";
	public static final String TABLE_PARAMETRAGES_EF_MALI="589";
	public static final String TABLE_ETAT_FISCAL_TOGO_CNSS_MENS= "427";
	public static final String TABLE_ETAT_FISCAL_TOGO_LNOM_PERS= "428";
	public static final String TABLE_ETAT_FISCAL_TOGO_TABREC= "429";
	public static final String TABLE_ETAT_FISCAL_TOGO_BUL= "430";
	public static final String TABLE_ETAT_FISCAL_TCHAD_BORD_INDIV = "431";
	public static final String TABLE_ETAT_FISCAL_TCHAD_DNOM_SMENS= "432";
	public static final String TABLE_ETATS_FISCAUX_TCHAD_AUTRES = "433";
	public static final String TABLE_ETATS_FISCAUX_BREC_AVNAT_IND_NIMP = "434";
	public static final String TABLE_ETAT_FISCAL_MAROC_IGR_AN = "434";
	public static final String TABLE_ETAT_FISCAL_MAROC_COT_MG = "435";
	public static final String TABLE_ETATS_FISCAUX_GUINEE_EQ = "436";
	public static final String TABLE_ETATS_FISCAUX_EGYPTE = "437";
	public static final String TABLE_ETATS_FISCAUX_EGYPTE_INSURANCE = "437";
	public static final String TABLE_ETATS_FISCAUX_EGYPTE_TAX = "440";
	public static final String TABLE_ETATS_FISCAUX_EGYPTE_PAY_SLIP = "441";
	public static final String TABLE_ETATS_FISCAUX_KENYA_NREP = "438";
	public static final String TABLE_ETATS_FISCAUX_KENYA_PREP = "439";
	public static final String TABLE_ETATS_FISCAUX_CAMEROUN = "460";
	public static final String TABLE_ETATS_FISCAUX_CONGO = "461";
	public static final String TABLE_FICHE_INDIV_CONGO = "462";
	public static final String TABLE_PARAMETRAGES_ETATS_FISCAUX_CONGO = "598";
	public static final String TABLE_PARAMETRAGES_ETATS_FISCAUX_COTE_IVOIRE = "463";
	public static final String TABLE_PARAMETRAGES_ETATS_FISCAUX_COTE_IVOIRE_BIS = "464";
	public static final String TABLE_PARAMETRAGES_ETATS_FISCAUX_COTE_IVOIRE_FIN_EXERCICE = "465";
	public static final String TABLE_ETAT_FISCAL_GUINEE_BISSAU = "470";
	public static final String TABLE_EDIT_FISC_SEN = "5015";
	
	public static final String TABLE_EDIT_VIR_ART = "5027";
	
	public static final String TABLE_ETATS_FISCAUX_BURKINA = "5018";
	
	public static final String TABLE_PARAMETRAGES_ETATS_ADMIN_PERS_SUPP = "1050";
	public static final String TABLE_PARAMETRAGES_BILAN_SOCIAL = "1051";
	public static final String TABLE_PARAMETRAGES_CLASSE_SOCIO_PROFESSIONNELLES = "1005";
	public static final String TABLE_PARAMETRAGES_PYRAMIDE_AGE = "1006";
	public static final String TABLE_PARAMETRAGES_PYRAMIDE_ANCEIENNETE = "1007";
	public static final String TABLE_EDITION_BAL_PAIE_NEW="1008";
	public static final String COLONNES_TABLE_CANDIDAT="1264";
	public static final String COLONNES_TABLE_SALARIE_LANCEUR="1265";
	
	public static final String TABLE_EDITION_BICEC="1100";
	
	// ===========================Editions optimisation Deltapaie SABC ============
	 public static final String CTAB_RETRAI_SABC = "5020";
	 public static final String CTAB_CODESITE = "274";
	 public static final String CTAB_NATION_SABC = "5021";
	 public static final String CTAB_CLASSE_SABC = "5022";
	 public static final String CTAB_PRET_SABC = "5023";
	 	
	//=========================Editions du bilan social ===========//
	public static final String TABLE_EDITIONS_PYRAMIDES = "450";
	public static final String TABLE_EDITIONS_BILAN_FORMATION = "451";
	public static final String TABLE_EDITIONS_EFFECTIFS = "452";
	
	//=================== ETATS ANGOLA =================//
	public static final String TABLE_EDITIONS_HEURESSUP_CONGES_AVANCEPRET = "453";
	public static final String TABLE_DEVISES = "454";
	
//  ===========================Gestion des p�riodes de paie========
	public static final String PERIODE_DE_PAIE = "91";
	//##############################Gestion standard des cong�s#################################
	public static final int CONGE_STANDARD = 753;
	public static final String CLE_CONGE_STANDARD="CONGESTD";
	public static final int TYPE_CONGE = 754;
//  ===========================Gestion des p�riodes de paie========
	public static final String TABLE_OPERATIONS_CAISSE="796";
	
//	Param�tres g�n�raux(EVT/L/F)
	public static final int TABLE_EV_LIEN_LANGUE_ISO = 10001;
	public static final int TABLE_CONF_GEN_LDAP_KDC = 10002;
	public static final int TABLE_CONF_LDAP_USER_OBJECTCLASS = 10003;
//	Param�tres synchronisation CSV-DeltaRH (SGBC)
	public static final String TABLE_CONFIG_TABLES_SYNCHRONISATION = "901";
	public static final String TABLE_CONFIG_COLONNES_SYNCHRONISATION = "902";
	
	//-------POINTAGE--NOMENCLATURES
	public static final int TABLE_CODES_POINTAGES = 20020;
	public static final int TABLE_CODES_PRIMES = 20021;
	public static final int TABLE_PARAMETRES_VALIDATION_POINTAGE = 20022;
	public static final int TABLE_CODES_POINTAGE_A_ELIMINER = 20023;
	public static final int TABLE_CODES_POINTAGE_A_TOTALISER = 20024;
	public static final int TABLE_CODES_COTATION_INFORMATIQUE = 41;// 20025;
	public static final int TABLE_POSTES_COTATION_INFORMATIQUE = 20026;
	public static final int TABLE_POSTES_PRIME_DETECTIVE = 20027;
	public static final int TABLE_TYPES_PRIMES = 20028;
	public static final int TABLE_PARAMETRES_VALIDATION_POINTAGE_PAR_CLASSE = 20029;
	public static final int TABLE_PARAMETRAGE_PRIME_POINTAGE_PAR_CLASSE = 20030;
	public static final int TABLE_EDITION_POINTAGE = 20031;
	public static final int TABLE_PARAMETRAGE_CONITUER_MOIS_MODIF = 20032;
	public static final int TABLE_CODES_POINTAGES_SPECIFIQUES = 20033;
	
	//--COMMISSIONNEMENT
	public static final int TABLE_CODES_DTABGRA = 20034;
	public static final int TABLE_PARAM_COMMISSIONNEMENT_EXE = 20035;
	
	public static final int TABLE_BAREME_CLASSE = 20036;
	public static final int TABLE_PARAM_COMMISSIONNEMENT_MC = 20037;
	public static final int TABLE_PARAM_COMMISSIONNEMENT_D = 20041;
	public static final int TABLE_FTABLE_MC_D = 20038;
	public static final String CTAB_EDITIONS_COMMISSIONNEMENT = "20040";
	
	//--pension
	public static final int TABLE_LANCEUR_FICHE_PENSION = 20042;
	
	//--solde
	public static final int TABLE_LANCEUR_FICHE_SOLDE = 20043;
	
	//--CONGE
	public static final int TABLE_LANCEUR_LETTRE_CONGE = 20039;
	
	//ANNALE ET PROMO AVANCEMENT
	public static final String CTAB_EDITIONS_ANNALE_PROMOTION = "20041";
	public static final String CACC_EDITIONS_ANNALE_PROMOTION_DIRECTEUR = "ANNALDIR";
	public static final String CACC_EDITIONS_ANNALE_PROMOTION_MC = "ANNALMC";
	public static final String CACC_EDITIONS_ANNALE_PROMOTION_EXE = "ANNALEXE";
	public static final String CACC_EDITIONS_LISTING_MC_DIRECTEUR = "LISTINGMD";
	public static final String CACC_EDITIONS_LISTING_EXE = "LISTINGEXE";
	
	
	//------POINTAGE--ZONES LIBRES
	public final static int ZONE_LIBRE_REDGRA = 34;
	public final static int ZONE_LIBRE_REDCNG = 35;
	public final static int ZONE_LIBRE_REDANC = 36;
	public final static int ZONE_LIBRE_NOMBREJOUC = 37;
	public final static int ZONE_LIBRE_JRCIRC = 38;
	public final static int ZONE_LIBRE_JOURAB = 39;
	public final static int ZONE_LIBRE_JOURMP = 40;
	
	public final static int ZONE_LIBRE_AVISDEPCONGES = 10;
	
	public final static int ZONE_LIBRE_RECPOI = 41;
	public final static int ZONE_LIBRE_REDPRI = 42;
	public final static int ZONE_LIBRE_DATSALA = 43;
	
	public final static int ZONE_LIBRE_DATECOMMIS = 3;
	public final static int ZONE_LIBRE_GRADECOMMIS = 5;
	public final static int ZONE_LIBRE_FINCOMMISS = 4;
	public final static int ZONE_LIBRE_DATEINTERI = 6;
	public final static int ZONE_LIBRE_GRADEINTER = 8;
	public final static int ZONE_LIBRE_FININTERIM = 7;
	
	public static final String CODE_PRIME_TYPE_DETECTIVE = "DTV";
	public static final String CODE_PRIME_TYPE_KILOMETRE = "KLM";
	public static final String CODE_PRIME_TYPE_DE_DECOUCHER = "DCH";
	public static final String CODE_PRIME_TYPE_INCONFORT = "INC";
	public static final String CODE_PRIME_TYPE_LABORATOIRE = "LAB";
	public static final String CODE_PRIME_TYPE_INFORMATIQUE = "INF";
	public static final String CODE_PRIME_TYPE_TECHNICITE = "TCH";
	public static final String CODE_PRIME_TYPE_HUM_TECH = "HTCH";
	public static final String CODE_PRIME_TYPE_HUM_HEURES_E_G_H = "EGH";
	public static final String CODE_PRIME_TYPE_HUM_HEURES_NUIT = "HNUIT";
	public static final String CODE_PRIME_TYPE_HUM_HEURES_SUP = "HSUP";
	public static final String CODE_PRIME_TYPE_HUM_IND_TRANS = "INTR";
	
	
	public static final String CODE_PARAMES_VALIDATION_POINTAGE = "MDPEC";
	public static final String CODE_POINTAGE_GENERE = "1";
	public static final String CODE_POINTAGE_SAISI = "2";
	public static final String CODE_POINTAGE_VALIDE = "3";
	public static final String CODE_POINTAGE_REGULARISE = "6";
	
	public static final int TABLE_INFOS_STATIQUES = 2001;//ok
	public static final int TABLE_INFOS_GROUPES = 2002;//ok
	
	public static final String TABLE_EDITION_PREEMBAUCHES = "2005";//ok
	
	public static final String TABLE_ETAT_SIM="7511";//ok
	public static final String TABLE_TYPHYPO_SIM="7512";//ok
	
	public static final String CODE_POINTAGE_ABSENCE = "A";
	public static final String CODE_POINTAGE_ACCIDENT_DE_TRAVAIL = "B";
	public static final String CODE_POINTAGE_CONGE_NON_PAYE = "C";
	public static final String CODE_POINTAGE_DETENU = "D";
	public static final String CODE_POINTAGE_PRESTATION_INCOMPLETE_AUTORISEE = "E";
	public static final String CODE_POINTAGE_FERIE = "F";
	public static final String CODE_POINTAGE_JOURS_INACHEVE = "G";
	public static final String CODE_POINTAGE_CHOME = "H";
	public static final String CODE_POINTAGE_INTERUPTION_DE_SERVICE = "I";
	public static final String CODE_POINTAGE_MISE_A_PIED = "K";
	public static final String CODE_POINTAGE_CONGE_LOINTAINT_CONGOLAIS = "L";
	public static final String CODE_POINTAGE_MALADIE_CONGOLAIS = "M";
	public static final String CODE_POINTAGE_CONGE_DE_CIRCONSTANCE = "N";
	public static final String CODE_POINTAGE_PRESENT = "P";
	public static final String CODE_POINTAGE_REPOS = "R";
	public static final String CODE_POINTAGE_EN_MUTATION = "T";
	public static final String CODE_POINTAGE_CONGE_GROSSESSE_MATERNITE = "V";
	public static final String CODE_POINTAGE_GREVE = "Z";
	public static final String CODE_POINTAGE_FICTIF = "X";
	
	public static final int NUME_SAL_MAX=53;
    public static final int NUME_SAL_CONF=54;
    public final static String PARAM_GRILLE = "GESAVA_TGR";
    
  //table utilis�e pour l'interface M3
	public static final int M3ETA=10800;
	public static final int M3CORETA=10801;
	public static final int M3CORCPT=10802;
	public static final int M3CORRESP=10803;
	public static final String PARAM_CHEMIN_INTERFACE="CHEMINM3";
	
	//tables editions sp�cifiques Exxon
	public static final String TABLE_EDITION_COMPTE_BANCAIRES_EN_DOUBLE = "1500";
	public static final String TABLE_EDITION_VIREMENT_EN_DOUBLE = "1501";
	
	/**
	 * Paie, cloture
	 */
	public static final String PAIE_CLOTURE="331";
	public static final String PAIE_LOG="332";
	public static final String PAIE_MVT_COMPTABLE="333";

	// Gestion des mutation HEVECAAM
	public static final String TAB_MUTATION="1045";

	public static final String TYPECONTRAT = "9";
	
	private String code;
	private String libelle;
	private String montant;
	private String date;
	private String taux;
	public static String AFFICHAGE_CODE_LIBELLE="code-libelle";
	public static String AFFICHAGE_LIBELLE="libelle";
	
	public static String RUBRIQUE_CONGE="RUB_CONGE";
	public static String RUBRIQUE_RETRAITE="RUB_RETRAI";
	
	/*Editions multiluinges*/
	public static final String DEFAULT_XML_DATA_FILES_REP = "xmlDataFiles/";
	public static final int TABLE_ETIQUETTES_EDITIONS = 10004;
	
	private String codelibelle;
	public ClsNomenclature(String code, String libelle){
		this.setCode(code);
		this.setLibelle(libelle);
		setCodelibelle(code+" - "+libelle);
	}
	
	public ClsNomenclature(String code, String libelle,String montant,String taux,String date){
		this.setCode(code);
		this.setLibelle(libelle);
		setMontant(montant);
		setTaux(taux);
		setDate(date);
		setCodelibelle(code+" - "+libelle);
	}
	
	public ClsNomenclature(String code, String libelle,Long montant,BigDecimal taux,Date date){
		this.setCode(code);
		this.setLibelle(libelle);
		setMontant(montant != null?montant.toString():null);
		setTaux(taux != null?taux.toString():null);
		setDate(date != null ? date.toString() : null);
		setCodelibelle(code+" - "+libelle);
	}
	
	/**
	 * Constructeur par d�faut.<br>
	 */
	public ClsNomenclature(){
		
	}
	
	public String getCode() {
		return code;
	}
	public String getLibelle() {
		return libelle;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getMontant() {
		return montant;
	}
	public void setMontant(String montant) {
		this.montant = montant;
	}
	public String getTaux() {
		return taux;
	}
	public void setTaux(String taux) {
		this.taux = taux;
	}	
	
	 public String getCodelibelle() {
		return codelibelle;
	}

	public void setCodelibelle(String codelibelle) {
		this.codelibelle = codelibelle;
	}

	public int compareTo(Object other) { 
	      String otherModel = ((ClsNomenclature) other).getCode(); 
	      String currentModel = ((ClsNomenclature) this).getCode(); 
	      if(currentModel.toLowerCase().compareTo(otherModel.toLowerCase())<0) return -1;
	      else if(currentModel.compareTo(otherModel)==0) return 0;
	      else return 1; 
	   }
	
}
