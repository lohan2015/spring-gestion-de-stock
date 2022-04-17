package com.kinart.paie.business.services.calcul;

public interface IAlgorithm
{

	/**
	 * Cette algorithme force la base � �tre �gale au montant.
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle se fait le calcul.
	 * @return true si tout s'est bien pass� et false dans le cas contraire, au cas o� on a une exception par exemple.
	 */
	public abstract boolean algo1(ClsRubriqueClone rubrique);
	
	
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
	public abstract boolean algo9(ClsRubriqueClone rubrique);

	/**
	 * Division ou Multiplication de la base par le nombre d'heures du service
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algo s'applique
	 * @return true ou false
	 */
	public abstract boolean algo10(ClsRubriqueClone rubrique);

	/**
	 * @param rubrique
	 * @return age du salari� � la p�riode de paie
	 */

	public abstract boolean algo100(ClsRubriqueClone rubrique);

	/**
	 * @param rubrique
	 * @return nombre d'ann�es d'anciennet� du salari� � la p�riode de paie
	 */

	public abstract boolean algo101(ClsRubriqueClone rubrique);

	/**
	 * RECHERCHE D'UNE VALEUR EN TABLE := f( CLES ECRAN 2 ) sur cl� base de calcul : idem � l'algo 41 sauf que la cl� est la base de la rubrique, et au final on
	 * fixe le montant � la valeur lu, le taux � zero
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algo s'applique
	 * @return true ou false
	 */
	public abstract boolean algo102(ClsRubriqueClone rubrique);
	
	/**
	 * Calcul du nombre de jours supplementaires
	 * En fonction de l'anciennet�, le nombre de jours meres
	 */
	public abstract boolean algo103(ClsRubriqueClone rubrique);
	
	/* 
	 * Calcul du nombre de jours d'absences suivant un motif param�tr� en table (tabl) sur cl� le code de la rubrique, en libell� 1
	 */
	public abstract boolean algo104(ClsRubriqueClone rubrique);
	/* 
	 * Calcul du nombre de jours du � un employ� lors de sa radiation, cas tasiast
	 */
	public abstract boolean algo105(ClsRubriqueClone rubrique);
	/**
	 * 
	 *Calcul du nombre de jours entre le dernier cong� annuel et le d�but du cong� de ce mois
	 */
	public abstract boolean algo106(ClsRubriqueClone rubrique);
	/**
	 * 
	 *Calcul du nombre de jours dus ann�es par ann�e
	 *On cr�e en T99 la cl� JOURCGPRIS dont le montant 1 contient la rubrique du total des jours de cong�s pris sur le mois
	 *On cr�e en T99 la cl� JOURDUS-MP contenant dans les montants 1 � 7 les jours dus du mois pr�c�dents
	 *On cr�e en T99 la cl� JOURDUS-MC contenant dans les montants 1 � 7 les jours dus du mois en cours
	 *On cr�e en T99 la cl� JOURDUS-MS contenant dans les montants 1 � 7 les jours dus du mois en cours qui seront g�n�r�s le mois suivant
	 *Les jours dus du mois pr�c�dents sont d�j� existants, il est question d'�valeur ceux du mois en cours
	 *en d�duisant les jours pris du mois et en basculant lorsqu'on passe d'un exo � un autre
	 */
	public abstract boolean algo107(ClsRubriqueClone rubrique);

	/**
	 * ANNIVERSAIRE DU SALAIRE
	 * D�termine, � la date d'anniversaire du salari�, si le nombre d'ann�e entre DTES ou DDCA
	 * est proportionnel � xAns
	 * On param�tre en table de l'algo sur cl� la rubrique : Lib1 -> la colonne date utilis�e (DTES,DDCA, DTNA,....)
	 * Mont1 -> Le nombre d'ann�es recherch� pour la proportion
	 * Ce algo permet d'activer le calcul des rubriques qui se calculent apr�s X ans pour un salari�.
	 */
	public abstract boolean algo110(ClsRubriqueClone rubrique);

	/**
	 * Calcul selon un bareme (memorise en rubrique)
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algo s'applique
	 * @return true ou false
	 */
	public abstract boolean algo11(ClsRubriqueClone rubrique);
	/**
	 * Dans la base de la rubrique on met le montant obtenue apr�s application du bar�me
	 * cet algo renvoit le brut qui a permit d'obtenir le montant dans la base de calcul
	 */
	public abstract boolean algo111(ClsRubriqueClone rubrique);
	/**
	 * Calcul selon un bareme (memorise en rubrique)
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algo s'applique
	 * @return true ou false
	 */
	public abstract boolean algo7(ClsRubriqueClone rubrique);
	/**
	 *RECHERCHE D'UNE VALEUR := f T30 ( CLES ECRAN 2 )
	 * On concat�ne cle1 et cle2 pous avoirs une valeur num�rique qu'on stocke dans la rubrique.
	 * si c'est une date, on renvoit:
	 * dans la base : le jour
	 * dans le taux : le mois
	 * dans le montant : l'ann�e
	 * @param rubrique
	 *            la rubrique sur laquelle se fait le calcul.
	 * @return true ou false
	 */
	public abstract boolean algo8(ClsRubriqueClone rubrique);
	/**
	 * Calcul du salaire indiciaire
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algo s'applique
	 * @return true ou false
	 */
	public abstract boolean algo12(ClsRubriqueClone rubrique);

	/**
	 * Calcul remboursement d'un pret L'algo 13 concerne la gestion des prets qui est comprise dans la gestion des salaries si traitement retroactif : on
	 * recupere directement le montant de l'echeance du pret dans les cumuls.
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algo s'applique
	 * @return true ou false
	 */
	public abstract boolean algo13(ClsRubriqueClone rubrique);

	/**
	 * Calcul de l'appoint ( La table 54 -modes de paiement- contient un flag indiquant si l'appoint doit etre calcule )
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algo s'applique
	 * @return true ou false
	 */
	public abstract boolean algo14(ClsRubriqueClone rubrique);

	/**
	 * Regularisation d'un bulletin inferieur au minimum requis (tabl99, BUL_NEG)
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algo s'applique
	 * @return true ou false
	 */
	public abstract boolean algo15(ClsRubriqueClone rubrique);

	/**
	 * Calcul selon un bareme fonction de la situation familiale (Type T R I M F Senegal)
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algo s'applique
	 * @return true ou false
	 */
	public abstract boolean algo16(ClsRubriqueClone rubrique);

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
	public abstract boolean algo17(ClsRubriqueClone rubrique);

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
	public abstract boolean algo18(ClsRubriqueClone rubrique);
	
	
	/**
	 * Cong�s suppl�mentaires gabon nbjsa*base/24 + nbjse*base/24
	 */
	public abstract boolean algo19(ClsRubriqueClone rubrique);

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
	public abstract boolean algo2(ClsRubriqueClone rubrique);

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
	public abstract boolean algo20(ClsRubriqueClone rubrique);

	/**
	 * CONGES PAYES ANNUELS
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algo s'applique
	 * @return true ou false
	 */
	public abstract boolean algo21(ClsRubriqueClone rubrique);

	/**
	 * Cet algorithme permet de calculer les cong�s ponctuels selon ces deux m�thodes de calcul
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algo s'applique
	 * @return true ou false
	 */
	public abstract boolean algo22(ClsRubriqueClone rubrique);

	/**
	 * Calcul IGR cote d ivoire
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algo s'applique
	 * @return true ou false
	 */
	public abstract boolean algo23(ClsRubriqueClone rubrique);

	/**
	 * Calcul IGR MAROC --> IDEM algo23 sauf deductions nb part
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algo s'applique
	 * @return true ou false
	 */
	public abstract boolean algo24(ClsRubriqueClone rubrique);

	/**
	 * La base est (multiplie,divisee ou proratee ) par la ( base, taux ou montant ) d'une rubrique deja calculee
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algo s'applique
	 * @return true ou false
	 */
	public abstract boolean algo25(ClsRubriqueClone rubrique);

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
	public abstract boolean algo26(ClsRubriqueClone rubrique);

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
	public abstract boolean algo27(ClsRubriqueClone rubrique);

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
	public abstract boolean algo28(ClsRubriqueClone rubrique);
	
	
	public abstract boolean algo29(ClsRubriqueClone rubrique);

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
	public abstract boolean algo3(ClsRubriqueClone rubrique);

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
	public abstract boolean algo30(ClsRubriqueClone rubrique);

	/**
	 * CALCUL DES CONGES := f( SALAIRE MOYEN MENSUEL )
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algo s'applique
	 * @return true ou false
	 */
	public abstract boolean algo31(ClsRubriqueClone rubrique);
	
	//Idem � l'algo 9, sauf qu'on peut faire des requetes comme souhait�, et le resultat de la requete doit �tre un nombre
	public abstract boolean algo32(ClsRubriqueClone rubrique);
	
	/**
	 ---------------------------------------------------------------------------------
	-- Calcul d'une prime fonction anciennete au mois suivant le mois d'entr�e
	-- dans la soci�t�
	---------------------------------------------------------------------------------
	-- L'algo n'est declenche que pour le mois suivant le mois anniversaire de l'entree du salarie
	-- On recupere le montant ou le taux correspondant a son anciennete (cle d'acces)
	-- Ensuite, selon parubq.TOUM, on applique un montant, taux ou diviseur.
	---------------------------------------------------------------------------------
	 */
	public abstract boolean algo34(ClsRubriqueClone rubrique);

	/**
	 * Calcul cotisations caisses francaises
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algo s'applique
	 * @return true ou false
	 */
	public abstract boolean algo37(ClsRubriqueClone rubrique);

	//Calcul abattement IRG ALGERIE F�vrier 2008
	public abstract boolean algo38(ClsRubriqueClone rubrique);

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
	public abstract boolean algo4(ClsRubriqueClone rubrique);

	/**
	 * CHAMP D'APPLICATION PARAMETRE
	 * 
	 * @param rubrique
	 * @return true ou false
	 */
	public abstract boolean algo40(ClsRubriqueClone rubrique);

	/**
	 * RECHERCHE D'UNE VALEUR EN TABLE := f( CLES ECRAN 2 )
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algo s'applique
	 * @return true ou false
	 */
	public abstract boolean algo41(ClsRubriqueClone rubrique);
	
	public abstract boolean algo42(ClsRubriqueClone rubrique);
	public abstract boolean algo46(ClsRubriqueClone rubrique);
	public abstract boolean algo47(ClsRubriqueClone rubrique);

	/**
	 * RECHERCHE D'UNE VALEUR CODEE EN TABLE 30 APPLICATION D'UN BAREME
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algorithme est appel�
	 * @return true ou false
	 */
	public abstract boolean algo43(ClsRubriqueClone rubrique);
	
	/**
	 * 	Algo calcul des provisions pour conges ( GABON ) Agent Local
	 *	Base = BASE CONGE TOTAL
	 *	MONT = BASE / DIV * COEFF
	 * @param rubrique
	 *            la rubrique sur laquelle l'algorithme est appel�
	 * @return true ou false
	 */
	public abstract boolean algo44(ClsRubriqueClone rubrique);
	/**
	 * 	Algo calcul des provisions pour conges ( GABON ) Expat
	 *	Base = SALAIRE MOYEN
	 *	MONT = BASE / 30 * DROITS
	 * @param rubrique
	 *            la rubrique sur laquelle l'algorithme est appel�
	 * @return true ou false
	 */
	public abstract boolean algo45(ClsRubriqueClone rubrique);

	/**
	 * CONGES PAYES ANNUELS SPNP
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algorithme est appel�
	 * @return true ou false
	 */
	public abstract boolean algo48(ClsRubriqueClone rubrique);

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
	public abstract boolean algo5(ClsRubriqueClone rubrique);

	/**
	 * CONGES PAYES ANNUELS (Avec diviseur base conge et coefficient )
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algorithme est appel�
	 * @return true ou false
	 */
	public abstract boolean algo50(ClsRubriqueClone rubrique);
	
	/**
	 * PRIME ENFANT A CHG
	 * @param rubrique
	 * @return
	 */
	public abstract boolean algo52(ClsRubriqueClone rubrique);
	
	/**
	 * ---------------------------------------------------------------------------------
-- Calcul d une rubrique qui cumul le montant d une autre rubrique             --
-- du mois de retour de conge jqa le mois de paie courant                      --
---------------------------------------------------------------------------------
	 */
	public abstract boolean algo59(ClsRubriqueClone rubrique);

	/**
	 * Multiplication / Division d'une base par un element variable
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algo s'applique
	 * @return true ou false
	 */
	public abstract boolean algo6(ClsRubriqueClone rubrique);

	public abstract boolean algo60(ClsRubriqueClone rubrique);

	/*-----------------------------------------------------------------------
	-- Extraction de valeurs systemes, ou de variables interm�diaires
	--    du calcul. Les possibilites sont fixees .
	-----------------------------------------------------------------------
	-- o Lecture du parametrage en table parubq.tabl (utilisez 62).
	--   La cle d'acces est composee sous la forme Axx-yyyy-z
	--      avec xx=No d'algo, yyyy=No de la rbq utilisant cet algo, z=indice
	-- o Controle du parametrage
	-- o Recherche de la valeur
	------------------------------------------------------------------------
	*/
	public abstract boolean algo61(ClsRubriqueClone rubrique);

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
	public abstract boolean algo62(ClsRubriqueClone rubrique);
	/**
	 * 
	 -----------------------------------------------------------------------
	-- Creation de rubriques a partir d'autres deja calculees
	-----------------------------------------------------------------------
	-- Chaque colonne (base, taux et montant) est alimentee a partir de la
	--    valeur d'une autre rubrique (base, taux ou montant).
	-- Le lien est fait a partir d'une table dont la cle d'acces est le numero
	--    la rubrique utilisant cet algorithme.
	-- Dans cette table :
	--    Montant 1 = No de la rubrique pour alimenter la base
	--    Libelle 1 = B/T/M = colonne a lire dans cette rubrique
	--    Montant 2, libelle 2 = alimentation du taux
	--    Montant 3, libelle 3 = alimentation du montant
	-----------------------------------------------------------------------
	 */

	public abstract boolean algo63(ClsRubriqueClone rubrique);

	public abstract boolean algo64(ClsRubriqueClone rubrique);

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
	public abstract boolean algo65(ClsRubriqueClone rubrique);

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
	public abstract boolean algo66(ClsRubriqueClone rubrique);

	public abstract boolean algo67(ClsRubriqueClone rubrique);
	
	
	/**
	 * CNSS Calcul des cong�s pay�s imposables
	 * CONGES PAYES ANNUELS CNSS FONCTION SALAIRE MOYEN MENSUEL
	 * @param rubrique
	 * @return
	 * Chez SOBRAGA, c'est base * nbj supp anciennet� (fiche sal) / 24
	 */
	public abstract boolean algo68(ClsRubriqueClone rubrique);
	
	/**
	 * --------------------------------------------------------------
	*--  CALCUL ACOMPTE CNSS                                     --
	*--------------------------------------------------------------
	 * @param rubrique
	 * Chez SOBRAGA, c'est base * nbj supp enfants (fiche sal) / 24
	 * @return
	 */
	public abstract boolean algo69(ClsRubriqueClone rubrique);
	
	/**
	 * 
	 -----------------------------------------------------------------------
	-- Creation de rubriques a partir d'autres deja calculees
	-----------------------------------------------------------------------
	-- Chaque colonne (base, taux et montant) est alimentee a partir de la
	--    valeur d'une autre rubrique (base, taux ou montant).
	-- Le lien est fait a partir d'une table dont la cle d'acces est le numero
	--    la rubrique utilisant cet algorithme.
	-- Dans cette table :
	--    Montant 1 = 0/1/2 (pour B/T/M respectivement)
	--    Libelle 1 = Formule � utiliser pour obtenir le montant (la formule ne li que soit la base, soit le taux, soit le montant
	-- Formule du genre +0500-0700*1020
	-- de chacune des rubriques qui la constitue)
	--    Montant 2, libelle 2 = alimentation du taux
	--    Montant 3, libelle 3 = alimentation du montant
	-----------------------------------------------------------------------
	 */

	public abstract boolean algo70(ClsRubriqueClone rubrique);

	/**
	 * Calcul du montant d'une indemnite en fonction du nombre de jours depuis l'entre dans la societe
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algorithme est appel�
	 * @return true ou false
	 */
	public abstract boolean algo78(ClsRubriqueClone rubrique);

	/**
	 * CONGES PAYES PONCTUELS COTE D'IVOIRE
	 * 
	 * @param rubrique
	 *            la rubrique sur laquelle l'algorithme est appel�
	 * @return true ou false
	 */
	public abstract boolean algo82(ClsRubriqueClone rubrique);

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
	public abstract boolean algo83(ClsRubriqueClone rubrique);

	/**
	 * @param rubrique
	 *            la rubrique sur laquelle l'algorithme est appel�
	 * @return true ou false
	 */
	public abstract boolean algo84(ClsRubriqueClone rubrique);

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
	public abstract boolean algo85(ClsRubriqueClone rubrique);

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
	public abstract boolean algo86(ClsRubriqueClone rubrique);

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
	public abstract boolean algo87(ClsRubriqueClone rubrique);
	
	/**
	 * Calcul provision impot/bonus specif shell
	 * @param rubrique
	 * @return true ou false
	 */
	public abstract boolean algo89(ClsRubriqueClone rubrique);
	
	public abstract boolean algo120(ClsRubriqueClone rubrique);
	public abstract boolean algo121(ClsRubriqueClone rubrique);
	public abstract boolean algo122(ClsRubriqueClone rubrique);
	public abstract boolean algo123(ClsRubriqueClone rubrique);
	public abstract boolean algo124(ClsRubriqueClone rubrique);
	public abstract boolean algo125(ClsRubriqueClone rubrique);
	public abstract boolean algo126(ClsRubriqueClone rubrique);
	public abstract boolean algo127(ClsRubriqueClone rubrique);
	public abstract boolean algo128(ClsRubriqueClone rubrique);
	public abstract boolean algo129(ClsRubriqueClone rubrique);

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

	public abstract double convertToNumber(String char16, ClsRubriqueClone rubrique);

	/**
	 * CONVERSION D'UNE CHAINE EN VALEUR NUMERIQUE Specifique algo. 37
	 * 
	 * @param char16
	 * @param lvalnum
	 *            multiplicateur du coefficient au cas o� char16 est une cl� d'acc�s
	 * @return la valeur obtenue
	 */
	public abstract double convertToNumber37(String char16, double lvalnum);

	public abstract String getOutputtext();

	public abstract ClsSalaryToProcess getSalary();

	public abstract Double Lecture_Code_nationalite(ClsRubriqueClone rubrique);

	public abstract void setOutputtext(String outputtext);

	public abstract void setSalary(ClsSalaryToProcess salary);
	
	//Algo Sp�cifiques SNCC
	public abstract boolean algo900(ClsRubriqueClone rubrique);
	//Algo Sp�cifiques UTB
	public abstract boolean algo901(ClsRubriqueClone rubrique);
	//Algo Sp�cifiques UTB
	public abstract boolean algo902(ClsRubriqueClone rubrique);
	//Algo Sp�cifiques UTB
	public abstract boolean algo903(ClsRubriqueClone rubrique);
	
	//Algo Sp�cifiques BGFI Congo
	public abstract boolean algo54(ClsRubriqueClone rubrique);
	public abstract boolean algo904(ClsRubriqueClone rubrique);
	public abstract boolean algo905(ClsRubriqueClone rubrique);
	public abstract boolean algo906(ClsRubriqueClone rubrique);
	
	public abstract boolean algo907(ClsRubriqueClone rubrique);
	public abstract boolean algo908(ClsRubriqueClone rubrique);
	
	public abstract boolean algo800(ClsRubriqueClone rubrique);
	public abstract boolean algo801(ClsRubriqueClone rubrique);
}