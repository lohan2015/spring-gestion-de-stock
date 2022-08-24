package com.kinart.organisation.business.services;

import com.kinart.paie.business.services.utils.ParameterUtil;

/**
 * Contient les feuilles de style des traits de l'organigramme
 * @author c.mbassi
 * @version 1.0
 */
public class ClsOrgCellsStyle {
	private String ligneHaute;
	
	private String ligneGaucheHaute;
	
	private String ligneDroiteHaute;
	
	private String ligneGauche;
	
	private String ligneDroite;
	
	private String tableLigneDroiteFictive;
	
	private String tdLigneDroiteFictive;
	
	private String ligneHauteOld="class=\"ligneHaute\"";
	
	private String ligneGaucheHauteOld="class=\"ligneGaucheHaute\"";
	
	private String ligneDroiteHauteOld="class=\"ligneDroiteHaute\"" ;
	
	private String ligneGaucheOld="class=\"ligneGauche\"";
	
	private String ligneDroiteOld="class=\"ligneDroite\"";
	
	private String tableLigneDroiteFictiveOld="class=\"tableLigneDroiteFictive\"";
	
	private String tdLigneDroiteFictiveOld="class=\"tdLigneDroiteFictive\"";
	
	private String couleur;
	
	private String contenucelluleOld="class=\"contenucellule\"";
	
	private String cellulefinctiveOld="class=\"cellulefictive\"";
	
	private String contenucellule;
	
	private String cellulefictive;
	
	private String couleurContenu;
	
	private int taille;
	
	private int taille2;
	
	/**
	 *  Constructeur de la classe
	 * @param couleurContenu couleur du contenu des cellules
	 * @param couleurTrait  couleur des traits de l'organigramme
	 * @param taille taille des traits de l'organigramme
	 */
	public ClsOrgCellsStyle (String couleurContenu, String couleurTrait, int taille ) {

		super();
		
		this.couleurContenu = couleurContenu;
		
		if(ParameterUtil._isStringNull(this.couleurContenu)) this.couleurContenu = "#CCECFF";
		
		this.couleur = couleurTrait;
		
		this.taille = taille;
		
		if(this.taille<=2) this.taille = 2;
		
		this.taille2 = this.taille - 1;
		//this.taille2 = this.taille - 0;
		
		if(this.taille2==0) this.taille2=1;
		
	}
	
	/**
	 * 
	 * @return le style correspondant au contenu des cellules
	 */
	public String getContenucellule ( ) {
		
		//+"BORDER-TOP: blue 1px solid; BORDER-BOTTOM: blue 1px solid; border-left: blue 1px solid; border-right: blue 1px solid; HEIGHT: 100px";
		
		contenucellule = "BACKGROUND-COLOR: "+couleurContenu+"; TEXT-ALIGN: Center; VERTICAL-ALIGN: Text-Top; FONT-FAMILY: Verdana; FONT-SIZE: 10px;"; 
		
		contenucellule="style=\""+contenucellule+"\"";
		
		return contenucellule;
	}
	
	/**
	 * 
	 * @return le style correspondant au contenu des cellules fictives
	 */
	public String getCellulefictive ( ) {
		
//		cellulefictive = "BACKGROUND-COLOR: #CCECFF; TEXT-ALIGN: Center; VERTICAL-ALIGN: Text-Top; FONT-FAMILY: Verdana; FONT-SIZE: 10px;"; 
//		
//		cellulefictive="style=\""+cellulefictive+"\"";
		cellulefictive="";
		
		return cellulefictive;
	}

	/**
	 * 
	 * @return le style correspondant au trait horizontal haut
	 */
	public String getLigneHaute ( ) {
		
		ligneHaute ="BORDER-TOP:    "+couleur+" "+taille+"px solid";
		
		ligneHaute="style=\""+ligneHaute+"\"";
		
		return ligneHaute;
	}
	
	/**
	 * 
	 * @return le style correspondant au trait horizontal haut +  le trait vertical � gauche (angle droit � gauche)
	 */
	public String getLigneGaucheHaute ( ) {
		
		ligneGaucheHaute="BORDER-TOP:    "+couleur+" "+taille+"px solid; border-left: "+couleur+" "+taille2+"px solid";
		
		ligneGaucheHaute="style=\""+ligneGaucheHaute+"\"";
	
		return ligneGaucheHaute;
	}
	
	/**
	 * 
	 * @return le style correspondant au trait horizontal haut + le trait vertical � droite (angle droit � droite)
	 */
	public String getLigneDroiteHaute ( ) {
		
		ligneDroiteHaute="border-right:    "+couleur+" "+taille2+"px solid; BORDER-TOP: "+couleur+" "+taille+"px solid";
		
		ligneDroiteHaute="style=\""+ligneDroiteHaute+"\"";
	
		return ligneDroiteHaute;
	}
	
	/**
	 * 
	 * @return  le style correspondant au trait vertical � gauche
	 */
	public String getLigneGauche ( ) {
		
		ligneGauche="border-left:    "+couleur+" "+taille2+"px solid";
		
		ligneGauche="style=\""+ligneGauche+"\"";
	
		return ligneGauche;
	}
	
	/**
	 * 
	 * @return  le style correspondant au trait vertical � gauche
	 */
	public String getTableLigneDroiteFictive ( ) {
		
//		tableLigneDroiteFictive="border-right:    "+couleur+" 3px solid; color: #E9E9E9;";
//		
//		tableLigneDroiteFictive="style=\""+tableLigneDroiteFictive+"\"";
//	
//		return tableLigneDroiteFictive;
		
		tableLigneDroiteFictive="BORDER:    "+couleur+" 1px solid; color: #E9E9E9;";
		
		tableLigneDroiteFictive="style=\""+tableLigneDroiteFictive+"\"";
	
		return tableLigneDroiteFictive;
	}
	
	/**
	 * 
	 * @return  le style correspondant au trait vertical � gauche
	 */
	public String getTdLigneDroiteFictive ( ) {
		
		tdLigneDroiteFictive="border-right:    "+couleur+" 1px solid;";
		
		tdLigneDroiteFictive="style=\""+tdLigneDroiteFictive+"\"";
	
		return tdLigneDroiteFictive;
	}
	
	/**
	 * 
	 * @return le style correspondant au trait vertical � droite
	 */
	public String getLigneDroite ( ) {
		
		ligneDroite="border-right:    "+couleur+" "+taille2+"px solid";
		
		ligneDroite="style=\""+ligneDroite+"\"";
	
		return ligneDroite;
	}

	
	public String getLigneHauteOld ( ) {
	
		return ligneHauteOld;
	}

	
	public void setLigneHauteOld ( String ligneHauteOld ) {
	
		this.ligneHauteOld = ligneHauteOld;
	}

	
	public String getLigneGaucheHauteOld ( ) {
	
		return ligneGaucheHauteOld;
	}

	
	public void setLigneGaucheHauteOld ( String ligneGaucheHauteOld ) {
	
		this.ligneGaucheHauteOld = ligneGaucheHauteOld;
	}

	
	public String getLigneDroiteHauteOld ( ) {
	
		return ligneDroiteHauteOld;
	}

	
	public void setLigneDroiteHauteOld ( String ligneDroiteHauteOld ) {
	
		this.ligneDroiteHauteOld = ligneDroiteHauteOld;
	}

	
	public String getLigneGaucheOld ( ) {
	
		return ligneGaucheOld;
	}

	
	public void setLigneGaucheOld ( String ligneGaucheOld ) {
	
		this.ligneGaucheOld = ligneGaucheOld;
	}

	
	public String getLigneDroiteOld ( ) {
	
		return ligneDroiteOld;
	}

	
	public void setLigneDroiteOld ( String ligneDroiteOld ) {
	
		this.ligneDroiteOld = ligneDroiteOld;
	}

	
	public String getContenucelluleOld ( ) {
	
		return contenucelluleOld;
	}

	public String getTableLigneDroiteFictiveOld()
	{
		return tableLigneDroiteFictiveOld;
	}

	public void setTableLigneDroiteFictiveOld(String tableLigneDroiteFictiveOld)
	{
		this.tableLigneDroiteFictiveOld = tableLigneDroiteFictiveOld;
	}

	

	public String getTdLigneDroiteFictiveOld()
	{
		return tdLigneDroiteFictiveOld;
	}

	public void setTdLigneDroiteFictiveOld(String tdLigneDroiteFictiveOld)
	{
		this.tdLigneDroiteFictiveOld = tdLigneDroiteFictiveOld;
	}

	public void setTableLigneDroiteFictive(String tableLigneDroiteFictive)
	{
		this.tableLigneDroiteFictive = tableLigneDroiteFictive;
	}

	public void setTdLigneDroiteFictive(String tdLigneDroiteFictive)
	{
		this.tdLigneDroiteFictive = tdLigneDroiteFictive;
	}

	public String getCellulefinctiveOld()
	{
		return cellulefinctiveOld;
	}

	public void setCellulefinctiveOld(String cellulefinctiveOld)
	{
		this.cellulefinctiveOld = cellulefinctiveOld;
	}
	
	
	
}
