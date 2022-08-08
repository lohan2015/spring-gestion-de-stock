package com.kinart.organisation.business.services;

import com.kinart.paie.business.services.utils.ParameterUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;

public class ClsOrgPrinter
{
	
	private String strTitreOrganigramme;
	
	private String strLegendeOrganigramme;
	
	private String strDebutOrganigramme;
	
	private String strOrganigramme;
	
	private HttpServletRequest request;
	
	private static final String CHEMIN_FICHIERS_ORGANIGRAMMES="temp_org";
	
	private static final String EXTENTION_FICHIERS_ORGANIGRAMMES=".html";
	
	private static final String EXTENTION_FICHIERS_ORGANIGRAMMES_PDF=".pdf";
	
	private static final String CHEMIN_FONTES="fontes";

	
	
	public ClsOrgPrinter ( HttpServletRequest request ,String strTitreOrganigramme , String strLegendeOrganigramme ,
			String strDebutOrganigramme , String strOrganigramme )
	{

		super();
		this.request = request;
		this.strTitreOrganigramme = strTitreOrganigramme;
		this.strLegendeOrganigramme = strLegendeOrganigramme;
		this.strDebutOrganigramme = strDebutOrganigramme;
		this.strOrganigramme = strOrganigramme;
	}

	/**
	 * G�n�re le fichier html qui comporte l'organigramme
	 */
	public String _createFlowChartFile()
	{
		
		String strUser= ParameterUtil.getSessionObject(request, ParameterUtil.SESSION_LOGIN);
		
		strUser="Organigramme_"+strUser;
		
		String strDossierAbsolue =request.getRealPath(CHEMIN_FICHIERS_ORGANIGRAMMES);
		
		String strNomFichierAbsolue =request.getRealPath(CHEMIN_FICHIERS_ORGANIGRAMMES+"/"+strUser+EXTENTION_FICHIERS_ORGANIGRAMMES);
		
		String strNomFichierPDF =request.getRealPath(CHEMIN_FICHIERS_ORGANIGRAMMES+"/"+strUser+EXTENTION_FICHIERS_ORGANIGRAMMES_PDF);
		
		File destination = new File(strNomFichierAbsolue);
		
		//si le fichier existe, on le supprime
		if(destination.exists())  destination.delete();
		
		//si le fichier n'existe pas, on cree le repertoire
		if(!destination.exists())
		{
			File folderFile = new File(strDossierAbsolue);
			
			boolean mkdir = folderFile.mkdir();
			
			if(mkdir==false) folderFile.mkdirs();
			
		}
		
		//on ecrit l'organigramme dans le dit fichier
		try
		{
			PrintWriter writer = new PrintWriter( new FileWriter(strNomFichierAbsolue),true );
			
			
			if(!ParameterUtil._isStringNull(strOrganigramme))
			{
				strOrganigramme=strOrganigramme.replaceAll("photos", "../photos");
				
				strOrganigramme=strOrganigramme.replaceAll("images", "../images");
				
				strOrganigramme=strOrganigramme.replaceAll("V123V900F514Y", "'");
				
			}
			
			writer.println(_getDocumentContent());
			
			writer.close();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		ParameterUtil.println("Nom du fichier html = "+strNomFichierAbsolue);
		
		ParameterUtil.println("Nom du fichier pdf � g�n�rer = "+strNomFichierPDF);
		
		String strUrl="file:///"+strNomFichierAbsolue;
		
		String strCheminFontes = request.getRealPath(CHEMIN_FONTES);
		
		ParameterUtil.println("Chemin des fontes = "+ strCheminFontes);
		
		ClsHtmlToPdfConverter _o_Html_Converter = new ClsHtmlToPdfConverter();
		
		try
		{
			_o_Html_Converter._generatePDF(strUrl, strNomFichierPDF,strCheminFontes);
			
		} 
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return strNomFichierPDF;
		
	}
	
	private String _getDocumentContent(){
		
		String head="";
		
		String body="";
		
		String foot="";
		
		
		head="<html><head><title>"+strTitreOrganigramme+"</title>";
		head+="<style type=\"text/css\">/*<![CDATA[*/ @import url(\"../styles/style.css\");   /*]]>*/  </style>";
		head+="</head>";
		
		body="<body>"+
		"<div id=\"title\">"+
		"<table width='100%' cellpadding='0' cellspacing='0' border='0' class='moduleTitle'>"+
			"<tr>"+
				"<td valign='top'>"+
					"<h2>"+"<img src=\"../images/application/organig.gif\">"+strTitreOrganigramme+"<br>"+	"</h2>"+
				"</td>"+
			"</tr>"+
		"</table>"+
		"</div>" +
		"<br>";
		
		body+=
		"<table align=\"left\" width=\"100%\">"+
			
		"<tr>"+
				"<td>&nbsp;</td>"+
			"</tr>"+
			"<tr>" +
				"<td>"+
					 "<table>"+
						"<tr>" +
							"<td>"+strLegendeOrganigramme+"</td>" +
						"</tr>"+
					"</table>"+
				"</td>" +
			"</tr>"+
			"<tr>"+
				"<td>&nbsp;</td>"+
			"</tr>"+
			"<tr>" +
			    "<td>"+
					 "<table width=\"100%\">"+
						"<tr>" +
						    "<td>"+strDebutOrganigramme+	"</td>" +
						"</tr>"+
					"</table>"+
		        "</td>" +
		   "</tr>"+
		   "<tr>"+
		   	 "<td>&nbsp;</td>"+
		   "</tr>"+
		   "<tr align=\"left\">" +
		       "<td>"+
		       		"<div id=\"organigrammediv\">"+
		       			"<table align=\"left\" width=\"100%\">"+
		       				"<tr>"+
		       					"<td  align=\"left\" width=\"100%\">"+strOrganigramme+"</td>"+
		       				"</tr>"+
		       			"</table>"+
		       		"</div>"+
		       	"</td>" +
		  "</tr>"+

	"</table>";
		
		
		foot="</body>"+
		"</html>";
		
		
		return head+body+foot;
	}
	
}
