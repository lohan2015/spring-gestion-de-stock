package com.kinart.organisation.business.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.kinart.organisation.business.model.Organigramme;


/**
 * Classe correspondant � une entit� Cellule d'organigramme
 * @author c.mbassi
 * @version 1.0
 */
public class ClsOrgCellule extends Organigramme {
	
	private int numero;
	
	private int nbrFils;
	
	private String nomagent;
	
	private String taillebordure;
	
	private String couleur;
	
	private String matricule;
	
	private String template;
	
	private String categorieposte;
	
	private String echelonposte;
	
	private int effectif;
	
	private int effectifprestataire;
	
	private String categorieagent;
	
	private String echelonagent;
	
	private boolean isallcellsfather;
	
	List<ClsOrgCellule> fils = new ArrayList<ClsOrgCellule>();
	
	private ClsOrgCellule cellulepere = null;
	
	
	private String tableauprestataires;
	
	
	/**
	 * Constructeur de la classe
	 * @param codepere : le code du p�re de la cellule
	 * @param libelle : l'intitul� de la cellule
	 * @param codeniveau : le code du niveau de la cellule
	 * @param datecree : la date de cr�ation de la cellule
	 * @param estvalide : validit� de la cellule
	 * @param accepteexterne : si la cellule accepte des externes
	 * @param codeposte : le code du poste affect� � la cellule
	 * @param codematricule : le matricule de l'agent occupant le poste affect� � la cellule
	 * @param bprestataire : d�termine si la cellule est un prestataire ou non
	 * @param bcasefictive : d�termine si la cellule est une case fictive ou non
	 * @param codesite : le code du site dans lequel se trouve la cellule
	 * @param nivfonction : le niveau de fonction auquel est ratach� la c�llule, existe en table 278 des nomenclatrues
	 * @param libellecourt : l'abbreviation de l'intitul� de la c�llule
	 */

	public ClsOrgCellule ( Integer idEntreprise, String codeorganigramme, String codepere , String libelle , String codeniveau , Date datecree , String estvalide ,
			String accepteexterne , String codeposte , String codematricule , String bprestataire , String bcasefictive , String codesite ,
			String nivfonction , String libellecourt) {
		
		
		super(idEntreprise, codeorganigramme, libelle, codeniveau, datecree, estvalide, accepteexterne, codeposte, codepere, codematricule, bprestataire, bcasefictive,
				codesite, nivfonction, libellecourt);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructeur par d�faut
	 */

	public ClsOrgCellule ( ) {

		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructeur simplifi�
	 * @param codeniveau : le code du niveau de la cellule
	 * @param codesite : le code du site dans lequel se trouve la cellule
	 */
	public ClsOrgCellule ( Integer idEntreprise, String codeorganigramme, String codeniveau , String codesite ) {
		this.setIdEntreprise(idEntreprise);
		this.setCodeorganigramme(codeorganigramme);
		this.setCodeniveau(codeniveau);
		this.setCodesite(codesite);
		// TODO Auto-generated constructor stub
	}

	/**
	 *  Converti un objet de type Rhtorganigramme en objet de type ClsOrgCellule
	 * @param _o_Organigramme : objet de type Rhtorganigramme
	 * @param numero : le num�ro de la cellule
	 * @param fils la liste des fils de la cellule
	 * @return : l'objet _o_Organigramme converti en object de type ClsOrgCellule
	 */
	public static ClsOrgCellule _convertToCellule(Organigramme _o_Organigramme, int numero, List<ClsOrgCellule> fils){
		
		ClsOrgCellule cell = new ClsOrgCellule(_o_Organigramme.getIdEntreprise(), _o_Organigramme.getCodeorganigramme(),_o_Organigramme.getCodepere(),_o_Organigramme.getLibelle(),_o_Organigramme.getCodeniveau(),
				_o_Organigramme.getDatecree(),_o_Organigramme.getEstvalide(),_o_Organigramme.getAccepteexterne(),_o_Organigramme.getCodeposte(),
				_o_Organigramme.getCodematricule(),_o_Organigramme.getBprestataire(),_o_Organigramme.getBcasefictive(),_o_Organigramme.getCodesite(),
				_o_Organigramme.getNivfonction(),_o_Organigramme.getLibellecourt());
		
		cell.setNumero(numero);
		
		cell.setNbrFils(fils.size());
		
		//cell.setFils(fils);
		
		return cell;
	}
	
	/**
	 * Converti un objet de type Rhtorganigramme en objet de type ClsOrgCellule
	 * @param _o_Organigramme: objet de type Rhtorganigramme � convertir
	 * @return l'objet _o_Organigramme converti en object de type ClsOrgCellule
	 */
	
	public static ClsOrgCellule _convertToCellule(Organigramme _o_Organigramme){
		
		ClsOrgCellule cell = new ClsOrgCellule(_o_Organigramme.getIdEntreprise(), _o_Organigramme.getCodeorganigramme(),_o_Organigramme.getCodepere(),_o_Organigramme.getLibelle(),_o_Organigramme.getCodeniveau(),
				_o_Organigramme.getDatecree(),_o_Organigramme.getEstvalide(),_o_Organigramme.getAccepteexterne(),_o_Organigramme.getCodeposte(),
				_o_Organigramme.getCodematricule(),_o_Organigramme.getBprestataire(),_o_Organigramme.getBcasefictive(),_o_Organigramme.getCodesite(),
				_o_Organigramme.getNivfonction(),_o_Organigramme.getLibellecourt());
	
		return cell;
	}
	
	/**
	 * 
	 * @return true si la cellule courante poss�de des fils, false sinon
	 */
	public boolean _possedeFils(){
		if(this.getFils().size()>0) return true;
		else return false;
	}

	
	public int getNumero ( ) {
	
		return numero;
	}

	
	public void setNumero ( int numero ) {
	
		this.numero = numero;
	}

	
	public int getNbrFils ( ) {
	
		//return nbrFils;
		if(this.fils != null)
			return this.fils.size();
		else
			return 0;
	}

	
	public void setNbrFils ( int nbrFils ) {
	
		this.nbrFils = nbrFils;
	}

	
	public List<ClsOrgCellule> getFils ( ) {
	
		return fils;
	}

	
	public void setFils ( List<ClsOrgCellule> fils ) {
	
		this.fils = fils;
	}


	
	public String getNomagent ( ) {
	
		return nomagent;
	}


	
	public void setNomagent ( String nomagent ) {
	
		this.nomagent = nomagent;
	}

	
	public String getTaillebordure ( ) {
	
		return taillebordure;
	}


	
	public void setTaillebordure ( String taillebordure ) {
	
		this.taillebordure = taillebordure;
	}


	
	public String getCouleur ( ) {
	
		return couleur;
	}


	
	public void setCouleur ( String couleur ) {
	
		this.couleur = couleur;
	}


	
	public String getMatricule ( ) {
	
		return matricule;
	}


	
	public void setMatricule ( String matricule ) {
	
		this.matricule = matricule;
	}


	
	public String getTemplate ( )
	{
	
		return template;
	}


	
	public void setTemplate ( String template )
	{
	
		this.template = template;
	}


	
	public String getCategorieposte ( )
	{
	
		return categorieposte;
	}


	
	public void setCategorieposte ( String categorieposte )
	{
	
		this.categorieposte = categorieposte;
	}


	
	public int getEffectif ( )
	{
	
		return effectif;
	}


	
	public void setEffectif ( int effectif )
	{
	
		this.effectif = effectif;
	}


	
	public String getCategorieagent ( )
	{
	
		return categorieagent;
	}


	
	public void setCategorieagent ( String categorieagent )
	{
	
		this.categorieagent = categorieagent;
	}


	
	public boolean isIsallcellsfather ( )
	{
	
		return isallcellsfather;
	}


	
	public void setIsallcellsfather ( boolean isallcellsfather )
	{
	
		this.isallcellsfather = isallcellsfather;
	}

	public int getEffectifprestataire()
	{
		return effectifprestataire;
	}

	public void setEffectifprestataire(int effectifprestataire)
	{
		this.effectifprestataire = effectifprestataire;
	}

	public String getTableauprestataires()
	{
		return tableauprestataires;
	}

	public void setTableauprestataires(String tableauprestataires)
	{
		this.tableauprestataires = tableauprestataires;
	}

	public ClsOrgCellule getCellulepere()
	{
		return cellulepere;
	}

	public void setCellulepere(ClsOrgCellule cellulepere)
	{
		this.cellulepere = cellulepere;
	}

	public String getEchelonposte()
	{
		return echelonposte;
	}

	public void setEchelonposte(String echelonposte)
	{
		this.echelonposte = echelonposte;
	}

	public String getEchelonagent()
	{
		return echelonagent;
	}

	public void setEchelonagent(String echelonagent)
	{
		this.echelonagent = echelonagent;
	}
	
	

}
