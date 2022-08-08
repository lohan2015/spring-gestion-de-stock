package com.kinart.organisation.business.services;



public class ClsOrgListCellsStyle {
	
	//public static String STR_STYLE_FILS_AVEC_FILS = "style=\"border-top: blue 1px solid\"";
	
	//public static String STR_STYLE_FILS_SANS_FILS = "style=\"border-bottom: blue 1px solid\"";
	
	/*public  String STR_STYLE_FILS_AVEC_FILS = "border-top: blue 1px solid;";
	
	public  String STR_STYLE_FILS_SANS_FILS = "border-bottom: blue 1px solid;";
	
	public  String STR_STYLE_BORDURE_DERNIER_FILS = "";
	
	public  String STR_STYLE_BORDURE_NON_DERNIER_FILS = "border-left: blue 1px solid;";
	
	*/
	private String strLineBorder;
	
	private String strLineColor;
	
	private String strPadding;
	
		
	
	public ClsOrgListCellsStyle ( String strLineBorder , String strLineColor, String strPadding )
	{

		super();
		this.strLineBorder = strLineBorder;
		this.strLineColor = strLineColor;
		this.strPadding = strPadding;
	}




	public  String _getStyleFilsAvecFils()
	{
		return  "border-top: "+strLineColor+" "+strLineBorder+"px solid;";
	}
	
	public  String _getStyleFilsSansFils()
	{
		return  "border-bottom: "+strLineColor+" "+strLineBorder+"px solid;";
	}
	
	public  String _getStyleBordureNonDernierFils()
	{
		return  "border-left: "+strLineColor+" "+strLineBorder+"px solid;";
	}
	
	public  String _getStyleLigneGauche()
	{
		return  "border-left: "+strLineColor+" "+strLineBorder+"px solid;";
	}
	
	public  String _getStyleBordureDernierFils()
	{
		return  "";
	}
	
	public  String _getPadding()
	{
		return  "padding:  "+strPadding+"px;";
	}
	
	

}
