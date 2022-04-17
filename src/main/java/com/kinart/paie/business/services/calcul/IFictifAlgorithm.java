package com.kinart.paie.business.services.calcul;

public interface IFictifAlgorithm
{

	/**
	 * Cette algorithme force la base � �tre �gale au montant.
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle se fait le calcul.
	 * @return true si tout s'est bien pass� et false dans le cas contraire, au cas o� on a une exception par exemple.
	 */
	public abstract boolean algo1(ClsFictifRubriqueClone rubrique);
	
	/**
	 * <p>
	 * D�termination d'une valeur num�rique repondant � certains criteres
	 * </p>
	 * <p>
	 * o Lecture du parametrage en table parubq.tabl .
	 * </p>
	 * <p>
	 * La cle d'acces est le code de la rubrique
	 * </p>
	 * <p>
	 * o Controle du parametrage
	 * </p>
	 * <p>
	 * o L1 : Table physique dans laquelle la recherche va se faire
	 * </p>
	 * <p>
	 * Parametres
	 * </p>
	 * <p>
	 * L2 = Fonction sql utilis�e pour la recherche :  count(*), min(colonne), max(..)
	 * </p>
	 * <p>
	 * LX = Condition X-2
	 * </p>
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle se fait le calcul.
	 * @return true ou false
	 */
	public abstract boolean algo9(ClsFictifRubriqueClone rubrique);

	/**
	 * Division ou Multiplication de la base par le nombre d'heures du service
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algo s'applique
	 * @return true ou false
	 */
	public abstract boolean algo10(ClsFictifRubriqueClone rubrique);

	/**
	 * @param rubrique
	 * @return age du salari� � la p�riode de paie
	 */

	public abstract boolean algo100(ClsFictifRubriqueClone rubrique);

	/**
	 * @param rubrique
	 * @return nombre d'ann�es d'anciennet� du salari� � la p�riode de paie
	 */

	public abstract boolean algo101(ClsFictifRubriqueClone rubrique);

	/**
	 * RECHERCHE D'UNE VALEUR EN TABLE := f( CLES ECRAN 2 ) sur cl� base de calcul : idem � l'algo 41 sauf que la cl� est la base de la rubrique, et au final on
	 * fixe le montant � la valeur lu, le taux � zero
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algo s'applique
	 * @return true ou false
	 */
	public abstract boolean algo102(ClsFictifRubriqueClone rubrique);
	
	/* 
	 * Calcul du nombre de jours d'absences suivant un motif param�tr� en table (tabl) sur cl� le code de la rubrique, en libell� 1
	 */
	public abstract boolean algo104(ClsFictifRubriqueClone rubrique);

	/**
	 * Calcul selon un bareme (memorise en rubrique)
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algo s'applique
	 * @return true ou false
	 */
	public abstract boolean algo11(ClsFictifRubriqueClone rubrique);

	/**
	 * Calcul du salaire indiciaire
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algo s'applique
	 * @return true ou false
	 */
	public abstract boolean algo12(ClsFictifRubriqueClone rubrique);

	/**
	 * Calcul remboursement d'un pret L'algo 13 concerne la gestion des prets qui est comprise dans la gestion des salaries si traitement retroactif : on
	 * recupere directement le montant de l'echeance du pret dans les cumuls.
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algo s'applique
	 * @return true ou false
	 */
	public abstract boolean algo13(ClsFictifRubriqueClone rubrique);

	/**
	 * Calcul de l'appoint ( La table 54 -modes de paiement- contient un flag indiquant si l'appoint doit etre calcule )
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algo s'applique
	 * @return true ou false
	 */
	public abstract boolean algo14(ClsFictifRubriqueClone rubrique);

	/**
	 * Regularisation d'un bulletin inferieur au minimum requis (tabl99, BUL_NEG)
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algo s'applique
	 * @return true ou false
	 */
	public abstract boolean algo15(ClsFictifRubriqueClone rubrique);

	/**
	 * Calcul selon un bareme fonction de la situation familiale (Type T R I M F Senegal)
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algo s'applique
	 * @return true ou false
	 */
	public abstract boolean algo16(ClsFictifRubriqueClone rubrique);

	/**
	 * <p>
	 * Calcul remboursement d'un pret
	 * </p>
	 * L'algo 17 concerne la gestion des prets externes si traitement retroactif alors on prend les donnees dans les cumuls.
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algo s'applique
	 * @return true ou false
	 */
	public abstract boolean algo17(ClsFictifRubriqueClone rubrique);

	/**
	 * Base := montant en table avec cle := No rubrique
	 * <p>
	 * Taux := E.V. ou E.F.
	 * </p>
	 * <p>
	 * Montant := Base * Taux ou Base * Taux / 100
	 * </p>
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algo s'applique
	 * @return true ou false
	 */
	public abstract boolean algo18(ClsFictifRubriqueClone rubrique);

	/**
	 * Recherche montant ou taux fonction de zone categorielle / categorie / echelon
	 * <p>
	 * Ce commentaire est celui de la source PA_ALGO
	 * </p>
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle se fait le calcul.
	 * @return true si tout s'est bien pass� et false dans le cas contraire, au cas o� la valeur TOUM de la rubrique est nulle ou que la valeur du montant lu
	 *         est nulle.
	 */
	public abstract boolean algo2(ClsFictifRubriqueClone rubrique);

	/**
	 * Calcul remboursement d'un pret
	 * <p>
	 * L'algo 20 concerne la gestion des prets externes avec INTERETS
	 * </p>
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algo s'applique
	 * @return true ou false
	 */
	public abstract boolean algo20(ClsFictifRubriqueClone rubrique);

	/**
	 * CONGES PAYES ANNUELS
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algo s'applique
	 * @return true ou false
	 */
	public abstract boolean algo21(ClsFictifRubriqueClone rubrique);

	/**
	 * Cet algorithme permet de calculer les cong�s ponctuels selon ces deux m�thodes de calcul
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algo s'applique
	 * @return true ou false
	 */
	public abstract boolean algo22(ClsFictifRubriqueClone rubrique);

	/**
	 * Calcul IGR cote d ivoire
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algo s'applique
	 * @return true ou false
	 */
	public abstract boolean algo23(ClsFictifRubriqueClone rubrique);

	/**
	 * Calcul IGR MAROC --> IDEM algo23 sauf deductions nb part
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algo s'applique
	 * @return true ou false
	 */
	public abstract boolean algo24(ClsFictifRubriqueClone rubrique);

	/**
	 * La base est (multiplie,divisee ou proratee ) par la ( base, taux ou montant ) d'une rubrique deja calculee
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algo s'applique
	 * @return true ou false
	 */
	public abstract boolean algo25(ClsFictifRubriqueClone rubrique);

	/**
	 * CALCUL DU MONTANT DES CONGES POUR JOURS SUPPLEMENTAIRES (COMILOG)
	 * <p>
	 * Base := 1,25 * Base-conges / 12
	 * </p>
	 * <p>
	 * Montant := (Base / (nbre jrs du conges princ.)) * nbre jrs suppl. calend.
	 * </p>
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algo s'applique
	 * @return true ou false
	 */
	public abstract boolean algo26(ClsFictifRubriqueClone rubrique);

	/**
	 * CALCUL DU MONTANT DES CONGES POUR JOURS SUPPLEMENTAIRES(HEVECAM)
	 * <p>
	 * Base := (Base-conges) / 16
	 * </p>
	 * <p>
	 * Montant := Base / (nbre jrs du conges) * nbre jrs suppl.
	 * </p>
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algo s'applique
	 * @return true ou false
	 */
	public abstract boolean algo27(ClsFictifRubriqueClone rubrique);

	/**
	 * MULTIPLICATION DE LA BASE PAR UN E.V. OU UN E.F.
	 * <p>
	 * DIVISION DE LA BASE PAR UN E.V. OU UN E.F.
	 * </p>
	 * <p>
	 * APPLICATION D'UN E.V. OU D'UN E.F. (sous forme de taux) SUR LA BASE
	 * </p>
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algo s'applique
	 * @return true ou false
	 */
	public abstract boolean algo28(ClsFictifRubriqueClone rubrique);

	/**
	 * Calcul d'une prime fonction anciennete a la DATE D'ENTREE DS LA SOCIETE. L'algo n'est declenche que pour le mois anniversaire de l'entree du salarie On
	 * recupere le montant ou le taux correspondant a son anciennete (cle d'acces) Ensuite, selon parubq.TOUM, on applique un montant, taux ou diviseur.
	 * <p>
	 * Ce commentaire est celui de la source PA_ALGO
	 * </p>
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle se fait le calcul.
	 * @return false si le montant ou le taux correspondant a son anciennete (cle d'acces) est z�ro.
	 */
	public abstract boolean algo3(ClsFictifRubriqueClone rubrique);

	/**
	 * CALCUL DU MONTANT A VIRER SUR L'ETRANGER
	 * <p>
	 * L'algo 30 permet de trouver en montant le total de la retenue a effectuer pour les virements etrangers, cad le total a crediter sur les banques non
	 * locales.
	 * </p>
	 * <p>
	 * Base = Net a payer (m�me base que pour RUBNAP)
	 * </p>
	 * <p>
	 * Montant = Total des montants pour les banques dont la devise est differente de la devise dossier.
	 * </p>
	 * <p>
	 * Ce commentaire est celui de la source PA_ALGO
	 * </p>
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle se fait le calcul.
	 * @return true ou false
	 */
	public abstract boolean algo30(ClsFictifRubriqueClone rubrique);

	/**
	 * CALCUL DES CONGES := f( SALAIRE MOYEN MENSUEL )
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algo s'applique
	 * @return true ou false
	 */
	public abstract boolean algo31(ClsFictifRubriqueClone rubrique);

	/**
	 * Calcul cotisations caisses francaises
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algo s'applique
	 * @return true ou false
	 */
	public abstract boolean algo37(ClsFictifRubriqueClone rubrique);

	/**
	 * Anciennete taux sur base ou montant fonction de l'anciennete
	 * <p>
	 * Ce commentaire est celui de la source PA_ALGO
	 * </p>
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle se fait le calcul.
	 * @return true ou false
	 */
	public abstract boolean algo4(ClsFictifRubriqueClone rubrique);

	/**
	 * CHAMP D'APPLICATION PARAMETRE
	 * 
	 * @param rubrique
	 * @return true ou false
	 */
	public abstract boolean algo40(ClsFictifRubriqueClone rubrique);

	/**
	 * RECHERCHE D'UNE VALEUR EN TABLE := f( CLES ECRAN 2 )
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algo s'applique
	 * @return true ou false
	 */
	public abstract boolean algo41(ClsFictifRubriqueClone rubrique);
	
	public abstract boolean algo42(ClsFictifRubriqueClone rubrique);
	public abstract boolean algo46(ClsFictifRubriqueClone rubrique);
	public abstract boolean algo47(ClsFictifRubriqueClone rubrique);
	
	public abstract boolean algo44(ClsFictifRubriqueClone rubrique);
	public abstract boolean algo45(ClsFictifRubriqueClone rubrique);

	/**
	 * RECHERCHE D'UNE VALEUR CODEE EN TABLE 30 APPLICATION D'UN BAREME
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algorithme est appel�
	 * @return true ou false
	 */
	public abstract boolean algo43(ClsFictifRubriqueClone rubrique);

	/**
	 * CONGES PAYES ANNUELS SPNP
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algorithme est appel�
	 * @return true ou false
	 */
	public abstract boolean algo48(ClsFictifRubriqueClone rubrique);

	/**
	 * Application d' un taux ou d' un diviseur sur une base ou chargement d' un montant.
	 * <p>
	 * Ce commentaire est celui de la source PA_ALGO
	 * </p>
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle se fait le calcul.
	 * @return true ou false
	 */
	public abstract boolean algo5(ClsFictifRubriqueClone rubrique);

	/**
	 * CONGES PAYES ANNUELS (Avec diviseur base conge et coefficient )
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algorithme est appel�
	 * @return true ou false
	 */
	public abstract boolean algo50(ClsFictifRubriqueClone rubrique);

	/**
	 * Multiplication / Division d'une base par un element variable
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algo s'applique
	 * @return true ou false
	 */
	public abstract boolean algo6(ClsFictifRubriqueClone rubrique);

	public abstract boolean algo60(ClsFictifRubriqueClone rubrique);

	public abstract boolean algo61(ClsFictifRubriqueClone rubrique);

	/**
	 * <p>
	 * Calcul du nombre d'enfants dans paenfan repondant � certains criteres
	 * </p>
	 * <p>
	 * o Lecture du parametrage en table parubq.tabl (utilisez 62).
	 * </p>
	 * <p>
	 * La cle d'acces est composee sous la forme Axx-yyyy-z avec xx=No d'algo, yyyy=No de la rbq utilisant cet algo, z=indice
	 * </p>
	 * <p>
	 * o Controle du parametrage
	 * </p>
	 * <p>
	 * o Calcul par count(*)
	 * </p>
	 * <p>
	 * Parametres
	 * </p>
	 * <p>
	 * L2 = Sexe = /F/M/ /
	 * </p>
	 * <p>
	 * L3 = Pays Naissance (Existe en T74)
	 * </p>
	 * <p>
	 * L4 = Scolarise = /O/N/ /
	 * </p>
	 * <p>
	 * L5 = A charge = /O/N/ /
	 * </p>
	 * <p>
	 * M3 = Age Mini
	 * </p>
	 * <p>
	 * M4 = Age Maxi
	 * </p>
	 * <p>
	 * M5 = Num Conjoint mini
	 * </p>
	 * <p>
	 * M6 = Num Conjoint maxi
	 * </p>
	 * <p>
	 * Ce commentaire est celui de la source PA_ALGO
	 * </p>
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle se fait le calcul.
	 * @return true ou false
	 */
	public abstract boolean algo62(ClsFictifRubriqueClone rubrique);

	public abstract boolean algo63(ClsFictifRubriqueClone rubrique);

	public abstract boolean algo64(ClsFictifRubriqueClone rubrique);

	/**
	 * Calcul somme,moyenne,count rubrique en cumul de aamm (ou pmcf)
	 * <p>
	 * jqa une date en parametre
	 * </p>
	 * <p>
	 * Commentaire de PA_ALGO
	 * </p>
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle se fait le calcul.
	 * @return true ou false
	 */
	public abstract boolean algo65(ClsFictifRubriqueClone rubrique);

	/**
	 * RECHERCHE D'UNE VALEUR NUMERIQUE := f T30 ( CLES ECRAN 2 )
	 * <p>
	 * Commentaire de PA_ALGO
	 * </p>
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle se fait le calcul.
	 * @return true ou false
	 */
	public abstract boolean algo66(ClsFictifRubriqueClone rubrique);

	public abstract boolean algo67(ClsFictifRubriqueClone rubrique);

	public abstract boolean algo68(ClsFictifRubriqueClone rubrique);

	public abstract boolean algo69(ClsFictifRubriqueClone rubrique);

	/**
	 * Calcul du montant d'une indemnite en fonction du nombre de jours depuis l'entre dans la societe
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algorithme est appel�
	 * @return true ou false
	 */
	public abstract boolean algo78(ClsFictifRubriqueClone rubrique);

	/**
	 * CONGES PAYES PONCTUELS COTE D'IVOIRE
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algorithme est appel�
	 * @return true ou false
	 */
	public abstract boolean algo82(ClsFictifRubriqueClone rubrique);

	/**
	 * CONGES PAYES ANNUELS COTE D'IVOIRE Montant_du_conges = ( Base_Conges / Nb_jrs_travailles ) * Nb_jrs_conges avec
	 * <p>
	 * Base_conges = Montant des droits (pasa01) + Base conges du mois
	 * </p>
	 * <p>
	 * Nb_jrs_travailles = Nb jrs trav depuis dernier conges + Nbj Trav du mois
	 * <p> !! Attention !! Si un agent prend un conges inclus dans le mois, la base conges du mois est fausse car elle tient compte de la base correspondant aux
	 * jours travailles apres le conges.
	 * </p>
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algorithme est appel�
	 * @return true ou false
	 */
	public abstract boolean algo83(ClsFictifRubriqueClone rubrique);

	/**
	 * @param rubrique
	 *            la rubrique sur laquelle l'algorithme est appel�
	 * @return true ou false
	 */
	public abstract boolean algo84(ClsFictifRubriqueClone rubrique);

	/**
	 * IRPP SPECIF SHELL
	 * <p>
	 * o La base est calculee a partir de la formule stockee dans la rubrique
	 * <p>
	 * Base = Sommme(SBI - TC) sur l'exercice en cours, mois de paie compris
	 * </p>
	 * <p>
	 * o Si la base est superieure au plafond annuel (55 000 000, stocke dans le montant indique au niveau de la perception maximum ) alors on deduit
	 * l'abattement annuel (10 000 000, stocke dans le montant indique au niveau de l'abattement maximum ).
	 * </p>
	 * <p>
	 * o La base annuelle est divisee par le mois de paie pour obtenir la base mensuelle.
	 * </p>
	 * <p>
	 * o Si il n'y a pas eu d'abattement annuel, alors on procede a un abattement sur la base mensuelle (20%).
	 * </p>
	 * <p>
	 * o La base mensuelle est divisee par le nombre de parts fiscales, puis arrondie aux 100 F.CFA inferieurs.
	 * </p>
	 * <p>
	 * o La tranche du bareme correspondant a la base mensuelle indique un taux et un montant, on peut calculer le montant pour une part : Impot_une_part = (
	 * base_mensuelle * taux / 100 ) - Montant
	 * </p>
	 * <p>
	 * o Le montant pour une part est multiplie par le nombre de parts fiscales pour donner le montant de l'impot mensuel, arrondi aux 10F inferieurs.
	 * </p>
	 * <p>
	 * o Le montant d'impot mensuel est multiplie par l'indice du mois de paie pour obtenir un montant annuel.
	 * </p>
	 * <p>
	 * o Le montant de la cotisation du mois est egal au montant annuel duquel l'exercice.
	 * </p>
	 * <p>
	 * Rmq : o Dans le cas d'un solde de tous compte le calcul est fait sur 12 mois si le salarie n'est pas gabonais. Indice_mois_de_paie = 12.
	 * </p>
	 * <p>
	 * o Si le salarie est entre en cours d'annee on ne calcule l'impot que sur la periode concernee quand il s'agit d'un gabonais.
	 * </p>
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algorithme est appel�
	 * @return true ou false
	 */
	public abstract boolean algo85(ClsFictifRubriqueClone rubrique);

	/**
	 * COMPENSATION CONGES SPECIF SHELL
	 * <p>
	 * Le principe est de calculer la moyenne des montants percus au titre de
	 * <p>
	 * certaines rubriques depuis le dernier conges. Cette compensation n'est
	 * <p>
	 * versee que lors du premier mois de conges (pmcf=mdp).
	 * <p>
	 * o Calcul du cumul de la base de calcul depuis le dernier conges, ou depuis
	 * <p>
	 * la date d'entree si il s'agit d'un premier conges.
	 * <p>
	 * On utilise la base conges (dapa+dapec) et la base conge du mois (w_bas)
	 * <p>
	 * o Calcul du nombre de mois ecoules depuis cette date
	 * <p>
	 * o Calcul de la moyenne : cumul/nombre de mois
	 * <p>
	 * Cette moyenne constitue la base de calcul
	 * <p>
	 * o Calcul du taux : nombre de jours de conges du mois
	 * <p>
	 * o Calcul du montant : (w_bas / 30 ) * nb jours conges
	 * <p>
	 * Rmq : Le calcul a ete copie sur l'algo 31
	 * 
	 * @param rubrique
	 * @return true ou false
	 */
	public abstract boolean algo86(ClsFictifRubriqueClone rubrique);

	/**
	 * TCS SPECIF SHELL
	 * <p>
	 * o La base est calculee a partir de la formule stockee dans la rubrique
	 * <p>
	 * Base = Sommme SBI sur l'exercice en cours, mois de paie compris
	 * <p>
	 * o La base annuelle est divisee par le mois de paie pour obtenir la base mensuelle.
	 * <p>
	 * o Le calcul est fait sur le bareme stocke en memoire
	 * <p>
	 * o Le montant d'impot mensuel est multiplie par l'indice du mois de paie pour obtenir un montant annuel.
	 * <p>
	 * o Le montant de la cotisation du mois est egal au montant annuel duquel
	 * <p>
	 * on deduit le montant deja paye au titre de la TCS depuis le debut de l'exercice.
	 * 
	 * @param rubrique
	 * @return true ou false
	 */
	public abstract boolean algo87(ClsFictifRubriqueClone rubrique);
	
	/**
	 * Calcul provision impot/bonus specif shell
	 * @param rubrique
	 * @return true ou false
	 */
	public abstract boolean algo89(ClsFictifRubriqueClone rubrique);

	/**
	 * calcule l'arrondi
	 * 
	 * @param p_type
	 *            le type d'arrondi
	 * @param p_arro
	 *            nombre de decimaux
	 * @param p_mon
	 *            la valeur � arrondir
	 * @return la valeur obtenue
	 */
	public abstract double arrondi2(char p_type, double p_arro, double p_mon);

	/**
	 * calcul du plafond
	 * 
	 * @param char16
	 * @return le montant du plafond
	 */
	public abstract double calculPlafond(String char16, String rubrique);

	/**
	 * FORMATAGE D'UNE DONNEE SALARIE POUR L'ALGO 43 := f( indice table 30 )
	 * 
	 * @param key
	 * @return la chaine r�sultante
	 */
	public abstract String concat43(int key, String rubrique);

	/**
	 * CONCATENATION DE DEUX VALEURS := f( indice table 30 )
	 * 
	 * @param cle1
	 * @param cle2
	 * @param rubrique
	 *            la rubrique sur laquelle l'algorithme est appel�
	 * @return la chaine r�sultante
	 */
	public abstract String concatene(int cle1, int cle2, String rubrique);
	public abstract double convertToNumber(String char16, ClsFictifRubriqueClone rubrique);

	/**
	 * CONVERSION D'UNE CHAINE EN VALEUR NUMERIQUE Specifique algo. 37
	 * 
	 * @param char16
	 * @param lvalnum
	 *            multiplicateur du coefficient au cas o� char16 est une cl� d'acc�s
	 * @return la valeur obtenue
	 */
	public abstract double convertToNumber37(String char16, double lvalnum);
	public abstract ClsFictifSalaryToProcess getFictivesalary();
	public abstract String getOutputtext();
	public abstract Double Lecture_Code_nationalite(ClsFictifRubriqueClone rubrique);
	public abstract void setFictivesalary(ClsFictifSalaryToProcess fictivesalary);
	public abstract void setOutputtext(String outputtext);
	public abstract boolean algo904(ClsFictifRubriqueClone rubrique);
}