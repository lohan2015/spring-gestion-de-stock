package com.kinart.paie.business.services.cloture;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.kinart.paie.business.model.*;
import com.kinart.paie.business.services.utils.ClsDate;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import org.hibernate.Session;

public interface ICentralisation
{
	/**
	 * Chargement des salari�s � traiter. 
	 * Cette fonction calcule en outre le nombre de salari�s � traiter.
	 */
	public abstract List<Salarie> chg_sal() throws Exception;

	/**
	 * Initialisation
	 */
	public abstract boolean init(char comp) throws Exception;

	public abstract boolean centra_dossier() throws Exception;

	public abstract boolean lect_rub_cal(Salarie salarie) throws Exception;

	public abstract boolean ven_ecr(Salarie salarie) throws Exception;

	/**
	 *  Controle de l'existence du compte de comptabilite 
	 *  et test si soumis a analytique (lecture dans la table des profils) 

	 */
	public abstract void Compte_OK(String Num_Cpt);

	

	/**
	 * Insertion dans la table cp_int
	 * @return
	 */
	public abstract boolean ch_rub(Salarie salarie) throws Exception;

	public abstract void refreshConnexion();

	/**
	 * Rech. du (ou des) compte(s) et de son (ou des) sens -- pour une rub. et un sal.
	 * @param strCrub  rub.
	 * @return
	 */
	public abstract boolean rech_cpt(String strCrub, Salarie salarie) throws Exception;

	public abstract boolean _getCompteRubrique(String strCrub, Salarie salarie) throws Exception;

	/**
	 * Ecriture debit - Comptabilite generale
	 * 
	 */
	public abstract void ch_deb();

	/**
	 *Ecriture credit - Comptabilite generale
	 * 
	 */
	public abstract void ch_cre();

	/**
	 * Controle de l'existence de la section salarie (Section analytique de compta.) 
	 * 
	 * @param Destination
	 * @param Num_Axe
	 * @return
	 */
	public abstract boolean Destination_OK(String Destination, Integer Num_Axe);

	/**
	 * Recherche d'une lettre dans le No de compte
	 */
	public abstract boolean cpt_cherche_lettre(Salarie salarie, String mess);

	/**
	 * charg_etssec Chargement etablissement , section et section axe 2@@@ interface GL - chargement des segments GL
	 */
	public abstract boolean chargerEtablissement(Salarie salarie, String crub) throws Exception;
	/**
	 * =>concpro RECHERCHE D'UNE VALEUR = f( indice table 30 ) OU SI @ recherche d'une valeur en Txx Lxx cle = f( indice table 30 ) structure a_cle1 si @ =
	 * @TxxLxx@NNN ou NNN = indice table 30
	 * @return valeur recherc�e
	 */
	public abstract String RechercheValeur(Salarie salarie, String crub, String cle1);

	public abstract ClsCentralisationService getCentraservice();

	public abstract void setCentraservice(ClsCentralisationService centraservice);

	public abstract ClsPafnomType getPafnom();

	public abstract void setPafnom(ClsPafnomType pafnom);

	public abstract String getRbq_NAP();

	public abstract void setRbq_NAP(String rbq_NAP);

	public abstract String getRbq_BRUT();

	public abstract void setRbq_BRUT(String rbq_BRUT);

	public abstract char getExist();

	public abstract void setExist(char exist);

	public abstract String getChar4();

	public abstract void setChar4(String char4);

	public abstract Integer getNbbul();

	public abstract void setNbbul(Integer nbbul);

	public abstract Integer getNb_sal();

	public abstract void setNb_sal(Integer nb_sal);

	public abstract Boolean getRetour();

	public abstract void setRetour(Boolean retour);

	public abstract String getLib_Mois();

	public abstract void setLib_Mois(String lib_Mois);

	public abstract String getMess();

	public abstract void setMess(String mess);

	public abstract String getMess2();

	public abstract void setMess2(String mess2);

	public abstract char getCas();

	public abstract void setCas(char cas);

	public abstract String getAamm();

	public abstract void setAamm(String aamm);

	public abstract Integer getNbul();

	public abstract void setNbul(Integer nbul);

	public abstract String getCuti();

	public abstract void setCuti(String cuti);

	public abstract String getClang();

	public abstract void setClang(String clang);

	public abstract Integer getNddd();

	public abstract void setNddd(Integer nddd);

	public abstract ClsDate getDate_Comptable();

	public abstract void setDate_Comptable(ClsDate date_Comptable);

	public abstract ClsDate getDate_Valeur();

	public abstract void setDate_Valeur(ClsDate date_Valeur);
	
	public abstract String getPipe_name();

	public abstract void setPipe_name(String pipe_name);

	public abstract Integer getTimeout();

	public abstract void setTimeout(Integer timeout);

	public abstract Boolean getDeltacom();

	public abstract void setDeltacom(Boolean deltacom);

	public abstract String getCode_Journal();

	public abstract void setCode_Journal(String code_Journal);

	public abstract char getSens();

	public abstract void setSens(char sens);

	public abstract String getNum_compte();

	public abstract void setNum_compte(String num_compte);

	public abstract String getNum_Piece();

	public abstract void setNum_Piece(String num_Piece);

	public abstract String getLibelle_Ecriture();

	public abstract void setLibelle_Ecriture(String libelle_Ecriture);

	public abstract String getLibelle_Abrege();

	public abstract void setLibelle_Abrege(String libelle_Abrege);

	public abstract Boolean getInterface_gl();

	public abstract void setInterface_gl(Boolean interface_gl);

	public abstract Integer getNum_tab_gl();

	public abstract void setNum_tab_gl(Integer num_tab_gl);

	public abstract String getCle_tab_gl();

	public abstract void setCle_tab_gl(String cle_tab_gl);

	public abstract String getChar10();

	public abstract void setChar10(String char10);

	public abstract char getCpt_Type();

	public abstract void setCpt_Type(char cpt_Type);

	public abstract String getCode_Etablissement();

	public abstract void setCode_Etablissement(String code_Etablissement);

	public abstract String getNum_Tiers();

	public abstract void setNum_Tiers(String num_Tiers);

	public abstract String getDevise_Dossier();

	public abstract void setDevise_Dossier(String devise_Dossier);

	public abstract String getCod_Etb_Commun();

	public abstract void setCod_Etb_Commun(String cod_Etb_Commun);

	public abstract String getDest2_Commune();

	public abstract void setDest2_Commune(String dest2_Commune);

	public abstract Boolean getExistence_Compte();

	public abstract void setExistence_Compte(Boolean existence_Compte);

	public abstract Boolean getSuivi_Analytique();

	public abstract void setSuivi_Analytique(Boolean suivi_Analytique);

	public abstract Boolean getCentra_dos();

	public abstract void setCentra_dos(Boolean centra_dos);

	public abstract String getNumdos_centra();

	public abstract void setNumdos_centra(String numdos_centra);

	public abstract Boolean getExist_cpt();

	public abstract void setExist_cpt(Boolean exist_cpt);

	public abstract Boolean getExist_aux();

	public abstract void setExist_aux(Boolean exist_aux);

	public abstract String getCpt_numcpt();

	public abstract void setCpt_numcpt(String cpt_numcpt);

	public abstract Integer getInd_deb();

	public abstract void setInd_deb(Integer ind_deb);

	public abstract Integer getInd_cre();

	public abstract void setInd_cre(Integer ind_cre);

//	public abstract Double getTotal_Debit();
//
//	public abstract void setTotal_Debit(Double total_Debit);
//
//	public abstract Double getTotal_Credit();
//
//	public abstract void setTotal_Credit(Double total_Credit);
//
//	public abstract Double getWecart();
//
//	public abstract void setWecart(Double wecart);

	public abstract Integer getType_Rubq();

	public abstract void setType_Rubq(Integer type_Rubq);

	public abstract double getMnt_Ecriture();

	public abstract void setMnt_Ecriture(double mnt_Ecriture);

	public abstract InterfComptable getWint();

	public abstract void setWint(InterfComptable wint);

	public abstract String getCdos();

	public abstract void setCdos(String cdos);

	public abstract char getComp();

	public abstract void setComp(char comp);

	public abstract HttpServletRequest getRequest();

	public abstract void setRequest(HttpServletRequest request);
	
	public abstract ClsGlobalUpdate getGlobalUpdate();
	
	public abstract void setGlobalUpdate(ClsGlobalUpdate globalupdate);

	public abstract CalculPaie getCalcul();

	public abstract void setCalcul(CalculPaie calcul);

	public abstract ElementSalaire getRubrique();

	public abstract void setRubrique(ElementSalaire rubrique);

	public abstract ParamData getFnom();

	public abstract void setFnom(ParamData fnom);

	public abstract Session getSession();

	public abstract void setSession(Session session);

	public abstract String[] getDestination();

	public abstract void setDestination(String[] destination);

	public abstract String[] getDestination_gl();

	public abstract void setDestination_gl(String[] destination_gl);

	public abstract Boolean[] getDestination_Existe();

	public abstract void setDestination_Existe(Boolean[] destination_Existe);
	
	public  abstract String getV_cdos();
	

	public  abstract void  setV_cdos(String v_cdos);
	

}
