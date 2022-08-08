package com.kinart.organisation.business.vo;

import com.kinart.organisation.business.model.Organigramme;
import com.kinart.organisation.business.services.ClsCellule;
import com.kinart.organisation.business.services.ClsCelluleThread;
import com.kinart.organisation.business.services.EnumFlowChartView;
import com.kinart.organisation.business.services.EnumTypeOrganigramme;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.kinart.paie.business.services.utils.ParameterUtil;

import java.io.File;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public class ClsTemplate {

	public static String STR_PERE = "Pere";

	public static String STR_FILS_1 = "Fils1";

	public static String STR_FILS_2 = "Fils2";

	public static String STR_FILS_3 = "Fils3";

	public static String STR_FILS_4 = "Fils4";

	public static String STR_FILS_5 = "Fils5";

	public static String STR_FILS_6 = "Fils6";

	public static String STR_FILS_7 = "Fils7";

	public static String STR_FILS_8 = "Fils8";

	public static String STR_FILS_9 = "Fils9";

	public static String STR_FILS_10 = "Fils_10";

	public static String STR_FILS_11 = "Fils_11";

	public static String STR_FILS_12 = "Fils_12";

	public static String STR_FILS_13 = "Fils_13";

	public static String STR_FILS_14 = "Fils_14";

	public static String STR_FILS_15 = "Fils_15";

	public static String STR_FILS_16 = "Fils_16";

	public static String STR_FILS_17 = "Fils_17";

	public static String STR_FILS_18 = "Fils_18";

	public static String STR_FILS_19 = "Fils_19";

	public static String STR_FILS_20 = "Fils_20";

	public static String STR_PHOTO_PATH = "photos/";

	public static String STR_SPECIAL_CHAR = "V123V900F514Y";
	
	public static String STR_SPECIFIC_CHAR = "DELTA";
	
	public static String TIRET_ENTETE_CELLULE="<td style=\"border-left: blue 1px solid; \">";

	public static String TIRET_ENTETE_PREMIERE_CELLULE="<td>";
	
	
	public static String STR_FLOWCHART_LEG_HEAD = "<table style='border-bottom-width: 2px; border-right-width: 2px; border-left-width: 2px; border-top-width: 2px' width='100%'  bgcolor='#D9FFFF'><tr><td nowrap='nowrap'><span style='color: #6e6e6e; font-family: Verdana, Arial, Helvetica, sans-serif; font-weight:bold'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;LEGENDE----------------------------------------------------------------------------------------------------------------------------------------------------------</span></td></tr></table>";
	
	public static String STR_FLOWCHART_LEG_HEAD_START = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<table border= \"1\" width=\"100%\"  cellspacing=\"0\"  cellpadding=\"0\"><tr>";
	
	public static String STR_FLOWCHART_LEG_HEAD_CLOSE = "</tr></table>";
	
	public static String STR_FICTIVE_CELL = "<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">"+
										    "<tr>"+
										    "<td width=\"100%\" align=\"center\" valign=\"top\" >&nbsp;</td>"+
										    "<td width=\"100%\" height=\"135\" align=\"center\" valign=\"top\" style=\"border-left: blue 1px solid; \">&nbsp;</td>"+
										    "</tr>"+
										    "</table>";
	
	public static String STR_FICTIVE_CELL_LIST = "<table border=\"0\" cellspacing=\"0\" align=\"center\">"+
	  "<tr>"+
	    "<td width=\"135\" >&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	        "<td width=\"135\" align=\"center\" valign=\"top\" style=\"border-top: blue 1px solid; \">&nbsp;</td>"+
	  "</tr>"+
	"</table>";

	public static String STR_TEMPLATE_1 = "<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
			+ "<tr>"
			+ "<td width=\"50%\" align=\"center\" valign=\"top\" >&nbsp;</td>"
			+ "<td width=\"50%\" align=\"center\" valign=\"top\" style=\"border-left: blue 1px solid; \">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td colspan=\"2\" width=\"100%\" align=\"center\" valign=\"top\">Pere</td>"
			+ "</tr>" + "</table>";

	public static String STR_TEMPLATE_LIST_1 = "<table cellspacing=\"0\"  border=\"0\" cellpadding=\"0\">"
			+ "<tr>"
			+ "<td align=\"center\" valign=\"top\">Pere</td>"
			+ "</tr>" + "</table>";

	// public static String STR_TEMPLATE_2 = "<table width=\"12%\" border=\"0\"
	// cellspacing=\"0\">"+
	// "<tr align=\"center\">"+
	// "<td colspan=\"2\" valign=\"top\">Pere</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td width=\"48%\">&nbsp;</td>"+
	// "<td width=\"52%\" style=\"border-left: blue 1px solid;\">&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td width=\"48%\">&nbsp;</td>"+
	// "<td width=\"52%\" style=\"border-left: blue 1px solid;\">&nbsp;</td>"+
	// "</tr>"+
	// "<tr align=\"center\">"+
	// "<td colspan=\"2\" valign=\"top\">Fils1</td>"+
	// "</tr>"+
	// "</table>";
	public static String STR_TEMPLATE_2 = "<table align=\"center\" cellspacing=\"0\" border=\"0\" cellpadding=\"0\">"+
	  "<tr>"+
	  "<td style=\"border-bottom: blue 1px solid;\">&nbsp;</td>"+
	    "<td colspan=\"2\" style=\"border-bottom: blue 1px solid;\"><table align=\"center\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	    "<td style=\"border-left: blue 1px solid; \">&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td colspan=\"2\">Pere</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	    "<td style=\"border-left: blue 1px solid; \">&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	"</table></td>"+
		"<td style=\"border-bottom: blue 1px solid;\">&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	  "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"center\">Fils1</td>"+
		"<td >&nbsp;</td>"+
	  "</tr>"+
	"</table>";

	public static String STR_TEMPLATE_LIST_2 = "<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
			+ "<tr>"
			+ "<td colspan=\"3\" align=\"left\" valign=\"bottom\">Pere</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td rowspan=\"2\" style=\"border-right: blue 1px solid\">&nbsp;</td>"
			+ "<td colspan=\"2\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			//+ "<td >&nbsp;</td>"
			+ "<td  style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td  rowspan=\"2\" align=\"left\"valign=\"center\">Fils1</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			//+ "<td style=\"border-top: blue 1px solid\">&nbsp;</td>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "</table>";

	// public static String STR_TEMPLATE_3 = "<table width=\"25%\"
	// height=\"25%\" border=\"0\" cellspacing=\"0\">"+
	// "<tr>"+
	// "<td>&nbsp;</td>"+
	// "<td width=\"16%\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Pere</td>"+
	// "<td width=\"13%\">&nbsp;</td>"+
	// "<td width=\"15%\">&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td>&nbsp;</td>"+
	// "<td style=\"border-bottom: blue 1px solid;\">&nbsp;</td>"+
	// "<td style=\"border-bottom: blue 1px solid;\">&nbsp;</td>"+
	// "<td style=\"border-left: blue 1px solid; border-bottom: blue 1px
	// solid;\">&nbsp;</td>"+
	// "<td style=\"border-bottom: blue 1px solid;\">&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td width=\"16%\" style=\"border-right: blue 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"4\" bordercolor=\"#CCCCCC\" style=\"border-top: blue 1px
	// solid;\"><span class=\"Style1\"></span></td>"+
	// "<td width=\"15%\" style=\"border-left: blue 1px solid;\">&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils1</td>"+
	// "<td colspan=\"2\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils2</td>"+
	// "</tr>"+
	// "</table>";
	public static String STR_TEMPLATE_3 = "<table border=\"0\" cellspacing=\"0\" align=\"center\" cellpadding=\"0\">"+
	  "<tr>"+
	  "<td colspan=\"3\" style=\"border-bottom: blue 1px solid;\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" style=\"border-bottom: blue 1px solid;\"><table align=\"center\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	    "<td style=\"border-left: blue 1px solid; \">&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td colspan=\"2\">Pere</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	    "<td style=\"border-left: blue 1px solid; \">&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	"</table></td>"+
	    "<td colspan=\"3\" style=\"border-bottom: blue 1px solid;\">&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	  "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils1</td>"+
	    "<td colspan=\"2\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils2</td>"+
		"<td >&nbsp;</td>"+
	  "</tr>"+
	"</table>";

	public static String STR_TEMPLATE_LIST_3 = "<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
			+ "<tr>"
			+ "<td colspan=\"3\" align=\"left\" valign=\"bottom\">Pere</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td rowspan=\"5\" style=\"border-right: blue 1px solid\">&nbsp;</td>"
			+ "<td colspan=\"2\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			//+ "<td >&nbsp;</td>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\"valign=\"center\">Fils1</td>"
			+ "</tr>"
			+ "<tr>"
			//+ "<td style=\"border-top: blue 1px solid\">&nbsp;</td>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td colspan=\"2\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\"valign=\"center\">Fils2</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td colspan=\"2\" >&nbsp;</td>"
			+ "</tr>"
			+ "</table>";

	// public static String STR_TEMPLATE_4 ="<table width=\"26%\" height=\"24%\"
	// border=\"0\" cellspacing=\"0\" style=\"border-top: #6e6e6e 1px
	// solid;\">"+
	// "<tr>"+
	// "<td>&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Pere</td>"+
	// "<td>&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td colspan=\"4\">&nbsp;</td>"+
	// "<td colspan=\"4\" style=\"border-left: blue 1px solid;\">&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td>&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td style=\"border-left: blue 1px solid;\">&nbsp;</td>"+
	// "</tr>"+
	// "<tr align=\"center\" valign=\"middle\">"+
	// "<td colspan=\"2\" valign=\"top\">Fils1</td>"+
	// "<td>&nbsp;</td>"+
	// "<td colspan=\"2\" valign=\"top\">Fils2</td>"+
	// "<td>&nbsp;</td>"+
	// "<td colspan=\"2\" valign=\"top\">Fils3</td>"+
	// "</tr>"+
	// "</table>";
	public static String STR_TEMPLATE_4 = "<table border=\"0\" cellspacing=\"0\" align=\"center\" cellpadding=\"0\">"+
	  "<tr>"+
	    "<td colspan=\"4\" style=\"border-bottom: blue 1px solid;\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" style=\"border-bottom: blue 1px solid;\"><table align=\"center\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	    "<td style=\"border-left: blue 1px solid; \">&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td colspan=\"2\">Pere</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	    "<td style=\"border-left: blue 1px solid; \">&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	"</table></td>"+
	    "<td colspan=\"4\" style=\"border-bottom: blue 1px solid;\">&nbsp;</td>"+
	  "</tr>"+
	  "<tr align=\"center\" valign=\"top\">"+
	      "<td>&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils1</td>"+
	    "<td>&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils2</td>"+
	    "<td>&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils3</td>"+
		    "<td width=\"8\">&nbsp;</td>"+
	  "</tr>"+
	"</table>";

	public static String STR_TEMPLATE_LIST_4 = "<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
			+ "<tr>"
			+ "<td colspan=\"3\" align=\"left\" valign=\"bottom\">Pere</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td rowspan=\"8\" style=\"border-right: blue 1px solid\">&nbsp;</td>"
			+ "<td colspan=\"2\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			//+ "<td >&nbsp;</td>"
			+ "<td  style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\"valign=\"center\">Fils1</td>"
			+ "</tr>"
			+ "<tr>"
			//+ "<td style=\"border-top: blue 1px solid\">&nbsp;</td>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td colspan=\"2\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\"valign=\"center\">Fils2</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\"valign=\"center\">Fils3</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td >&nbsp;</td>"
			+ "</tr>" + "</table>";

	// public static String STR_TEMPLATE_5 ="<table width=\"42%\" height=\"41%\"
	// border=\"0\" cellspacing=\"0\" style=\"border-top: #6e6e6e 1px
	// solid;\">"+
	// "<tr>"+
	// "<td>&nbsp;</td>"+
	// "<td width=\"10%\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td width=\"8%\">&nbsp;</td>"+
	// "<td width=\"7%\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Pere</td>"+
	// "<td width=\"8%\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td width=\"6%\">&nbsp;</td>"+
	// "<td width=\"7%\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td colspan=\"6\">&nbsp;</td>"+
	// "<td colspan=\"6\" style=\"border-left: blue 1px solid;\">&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td>&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"2\" style=\"border-left: blue 1px solid;\">&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils1</td>"+
	// "<td align=\"center\" valign=\"middle\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils2</td>"+
	// "<td width=\"9%\" align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils3</td>"+
	// "<td align=\"center\" valign=\"middle\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils4</td>"+
	// "<td>&nbsp;</td>"+
	// "</tr>"+
	// "</table>";
	public static String STR_TEMPLATE_5 = "<table border=\"0\" cellspacing=\"0\" align=\"center\" cellpadding=\"0\">"+
	  "<tr>"+
	    "<td colspan=\"6\" style=\"border-bottom: blue 1px solid;\">&nbsp;</td>"+
		"<td colspan=\"2\" align=\"center\" valign=\"top\" style=\"border-bottom: blue 1px solid;\"><table align=\"center\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	    "<td style=\"border-left: blue 1px solid; \">&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td colspan=\"2\">Pere</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	    "<td style=\"border-left: blue 1px solid; \">&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	"</table></td>"+
	    "<td colspan=\"6\" style=\"border-bottom: blue 1px solid;\">&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	  "<td>&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils1</td>"+
	    "<td  align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils2</td>"+
	    "<td  align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils3</td>"+
	    "<td  align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils4</td>"+
	    "<td >&nbsp;</td>"+
		"<td>&nbsp;</td>"+
	  "</tr>"+
	"</table>";

	public static String STR_TEMPLATE_LIST_5 = "<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
			+ "<tr>"
			+ "<td colspan=\"3\" align=\"left\" valign=\"bottom\">Pere</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td rowspan=\"11\" style=\"border-right: blue 1px solid\">&nbsp;</td>"
			+ "<td colspan=\"2\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\"valign=\"center\">Fils1</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-top: blue 1px solid\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td colspan=\"2\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\"valign=\"center\">Fils2</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\"valign=\"center\">Fils3</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\"valign=\"center\">Fils4</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td >&nbsp;</td>"
			+ "</tr>" + "</table>";

	// public static String STR_TEMPLATE_6 = "<table width=\"31%\"
	// height=\"41%\" border=\"0\" cellspacing=\"0\" style=\"border-top: #6e6e6e
	// 1px solid;\">"+
	// "<tr>"+
	// "<td>&nbsp;</td>"+
	// "<td width=\"9%\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td width=\"7%\">&nbsp;</td>"+
	// "<td width=\"5%\">&nbsp;</td>"+
	// "<td align=\"center\" valign=\"middle\" >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Pere</td>"+
	// "<td>&nbsp;</td>"+
	// "<td width=\"4%\">&nbsp;</td>"+
	// "<td width=\"6%\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td width=\"7%\">&nbsp;</td>"+
	// "<td width=\"19%\">&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td colspan=\"7\">&nbsp;</td>"+
	// "<td colspan=\"7\" style=\"border-left: blue 1px solid;\">&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td width=\"8%\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td style=\"border-left: blue 1px solid;\">&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils1</td>"+
	// "<td width=\"8%\" align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils2</td>"+
	// "<td width=\"5%\" align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils3</td>"+
	// "<td width=\"2%\" align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils4</td>"+
	// "<td width=\"2%\" align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils5</td>"+
	// "<td width=\"5%\">&nbsp;</td>"+
	// "</tr>"+
	// "</table>";

	public static String STR_TEMPLATE_6 = "<table border=\"0\" cellspacing=\"0\" align=\"center\" cellpadding=\"0\">"+
	  "<tr>"+
	    "<td colspan=\"7\" style=\"border-bottom: blue 1px solid;\">&nbsp;</td>"+
		"<td colspan=\"2\" align=\"center\" valign=\"top\" style=\"border-bottom: blue 1px solid;\"><table align=\"center\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	    "<td style=\"border-left: blue 1px solid; \">&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td colspan=\"2\">Pere</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	    "<td style=\"border-left: blue 1px solid; \">&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	"</table></td>"+
	    "<td colspan=\"6\" style=\"border-bottom: blue 1px solid;\">&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	  "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils1</td>"+
	    "<td align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td align=\"center\" valign=\"top\">Fils2 </td>"+
	    "<td  align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils3</td>"+
	    "<td align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td align=\"center\" valign=\"top\" >Fils4</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\"  align=\"center\" valign=\"top\">Fils5</td>"+
		"<td >&nbsp;</td>"+
	  "</tr>"+
	"</table>";

	public static String STR_TEMPLATE_LIST_6 = "<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
			+ "<tr>"
			+ "<td colspan=\"3\" align=\"left\">Pere</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td rowspan=\"14\" style=\"border-right: blue 1px solid\">&nbsp;</td>"
			+ "<td colspan=\"2\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\"valign=\"center\">Fils1</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-top: blue 1px solid\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td colspan=\"2\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\"valign=\"center\">Fils2</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\"valign=\"center\">Fils3</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\"valign=\"center\">Fils4</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\"valign=\"center\">Fils5</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "</table>";

	// public static String STR_TEMPLATE_7 = "<table width=\"46%\"
	// height=\"41%\" border=\"0\" cellspacing=\"0\" style=\"border-top: #6e6e6e
	// 1px solid;\">"+
	// "<tr>"+
	// "<td>&nbsp;</td>"+
	// "<td width=\"10%\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td width=\"8%\">&nbsp;</td>"+
	// "<td width=\"5%\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"middle\" ></td>"+
	// "<td colspan=\"2\" align=\"center\"
	// valign=\"top\"><strong>Pere</strong></td>"+
	// "<td width=\"6%\">&nbsp;</td>"+
	// "<td width=\"7%\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td width=\"9%\">&nbsp;</td>"+
	// "<td width=\"9%\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td width=\"9%\">&nbsp;</td>"+
	// "<td width=\"9%\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td colspan=\"8\">&nbsp;</td>"+
	// "<td colspan=\"10\" style=\"border-left: blue 1px solid;\">&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td width=\"9%\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"2\" style=\"border-left: blue 1px solid;\">&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils1 </td>"+
	// "<td width=\"9%\" align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils2 </td>"+
	// "<td width=\"9%\" align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils3 </td>"+
	// "<td width=\"11%\" align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils4 </td>"+
	// "<td width=\"9%\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils5</td>"+
	// "<td width=\"9%\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils6</td>"+
	// "<td width=\"9%\">&nbsp;</td>"+
	// "</tr>"+
	// "</table>";
	public static String STR_TEMPLATE_7 = "<table  border=\"0\" cellspacing=\"0\" align=\"center\" cellpadding=\"0\">"+
	  "<tr>"+
	    "<td colspan=\"9\" style=\"border-bottom: blue 1px solid;\">&nbsp;</td>"+
		"<td colspan=\"2\" align=\"center\" valign=\"top\" style=\"border-bottom: blue 1px solid;\"><table align=\"center\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	    "<td style=\"border-left: blue 1px solid; \">&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td colspan=\"2\">Pere</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	    "<td style=\"border-left: blue 1px solid; \">&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	"</table></td>"+
	    "<td colspan=\"9\" style=\"border-bottom: blue 1px solid;\">&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	  "<td>&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils1</td>"+
	    "<td align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils2</td>"+
	    "<td  align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils3</td>"+
	    "<td  align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils4</td>"+
	    "<td>&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils5</td>"+
	    "<td>&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils6</td>"+
	    "<td >&nbsp;</td>"+
		"<td>&nbsp;</td>"+
	  "</tr>"+
	"</table>";

	public static String STR_TEMPLATE_LIST_7 = "<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
			+ "<tr>"
			+ "<td colspan=\"3\" align=\"left\">Pere</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td rowspan=\"17\" style=\"border-right: blue 1px solid\">&nbsp;</td>"
			+ "<td colspan=\"2\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\"valign=\"center\">Fils1</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-top: blue 1px solid\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td colspan=\"2\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\"valign=\"center\">Fils2</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\"valign=\"center\">Fils3</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\"valign=\"center\">Fils4</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\"valign=\"center\">Fils5</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\"valign=\"center\">Fils6</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td >&nbsp;</td>"
			+ "</tr>" + "</table>";

	// public static String STR_TEMPLATE_8 = "<table width=\"46%\"
	// height=\"41%\" border=\"0\" cellspacing=\"0\" style=\"border-top: #6e6e6e
	// 1px solid;\">"+
	// "<tr>"+
	// "<td>&nbsp;</td>"+
	// "<td width=\"6%\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td width=\"3%\">&nbsp;</td>"+
	// "<td width=\"6%\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"middle\" ></td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\"></td>"+
	// "<td colspan=\"2\" align=\"center\"
	// valign=\"top\"><strong>Pere</strong></td>"+
	// "<td>&nbsp;</td>"+
	// "<td width=\"4%\">&nbsp;</td>"+
	// "<td width=\"6%\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td width=\"4%\">&nbsp;</td>"+
	// "<td width=\"6%\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td width=\"3%\">&nbsp;</td>"+
	// "<td width=\"6%\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td colspan=\"10\">&nbsp;</td>"+
	// "<td colspan=\"11\" style=\"border-left: blue 1px solid;\">&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td width=\"4%\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"2\" style=\"border-left: blue 1px solid;\">&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils1 </td>"+
	// "<td width=\"4%\" align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils2 </td>"+
	// "<td width=\"4%\" align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils3 </td>"+
	// "<td width=\"3%\" align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils4 </td>"+
	// "<td width=\"4%\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils5</td>"+
	// "<td width=\"4%\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils6</td>"+
	// "<td width=\"3%\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils7</td>"+
	// "<td width=\"17%\">&nbsp;</td>"+
	// "</tr>"+
	// "</table>";
	public static String STR_TEMPLATE_8 = "<table border=\"0\" cellspacing=\"0\" align=\"center\" cellpadding=\"0\">"+
	 "<tr>"+
	    "<td colspan=\"10\" style=\"border-bottom: blue 1px solid;\">&nbsp;</td>"+
		"<td colspan=\"2\" align=\"center\" valign=\"top\" style=\"border-bottom: blue 1px solid;\"><table align=\"center\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	    "<td style=\"border-left: blue 1px solid; \">&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td colspan=\"2\">Pere</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	    "<td style=\"border-left: blue 1px solid; \">&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	"</table></td>"+
	    "<td colspan=\"11\" style=\"border-bottom: blue 1px solid;\">&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	  "<td>&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils1</td>"+
	    "<td  align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils2</td>"+
	    "<td  align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils3</td>"+
	    "<td  align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils4</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils5</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils6</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils7</td>"+
	    "<td >&nbsp;</td>"+
		"<td>&nbsp;</td>"+
	  "</tr>"+
	"</table>";

	public static String STR_TEMPLATE_LIST_8 = "<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
			+ "<tr>"
			+ "<td colspan=\"3\" align=\"left\">Pere</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td rowspan=\"20\" style=\"border-right: blue 1px solid\">&nbsp;</td>"
			+ "<td colspan=\"2\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils1</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-top: blue 1px solid\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td colspan=\"2\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils2</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils3</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils4</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils5</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils6</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils7</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td >&nbsp;</td>"
			+ "</tr>" + "</table>";

	// public static String STR_TEMPLATE_9 = "<table width=\"35%\"
	// height=\"41%\" border=\"0\" cellspacing=\"0\" style=\"border-top: #6e6e6e
	// 1px solid;\">"+
	// "<tr>"+
	// "<td>&nbsp;</td>"+
	// "<td width=\"6%\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td width=\"3%\">&nbsp;</td>"+
	// "<td width=\"6%\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"middle\" ></td>"+
	// "<td colspan=\"2\"></td>"+
	// "<td colspan=\"4\" align=\"center\" valign=\"top\">Pere</td>"+
	// "<td width=\"6%\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td width=\"4%\">&nbsp;</td>"+
	// "<td width=\"6%\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td width=\"3%\">&nbsp;</td>"+
	// "<td width=\"6%\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td width=\"17%\">&nbsp;</td>"+
	// "<td width=\"17%\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td colspan=\"11\">&nbsp;</td>"+
	// "<td colspan=\"9\" style=\"border-left: blue 1px solid;\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td width=\"4%\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\"\">&nbsp;</td>"+
	// "<td colspan=\"2\" style=\"border-left: blue 1px solid;\">&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils1</td>"+
	// "<td width=\"4%\" align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils2</td>"+
	// "<td width=\"4%\" align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils3</td>"+
	// "<td width=\"3%\" align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils4</td>"+
	// "<td width=\"4%\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils5</td>"+
	// "<td width=\"4%\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils6</td>"+
	// "<td width=\"3%\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils7</td>"+
	// "<td width=\"17%\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils8</td>"+
	// "<td width=\"17%\">&nbsp;</td>"+
	// "</tr>"+
	// "</table>";
	public static String STR_TEMPLATE_9 = "<table align=\"center\"  border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
	  "<tr>"+
	  "<td colspan=\"11\" style=\"border-bottom: blue 1px solid;\">&nbsp;</td>"+
	  "<td colspan=\"4\" align=\"center\" valign=\"top\" style=\"border-bottom: blue 1px solid;\"><table align=\"center\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	    "<td style=\"border-left: blue 1px solid; \">&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td colspan=\"2\">Pere</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	    "<td style=\"border-left: blue 1px solid; \">&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	"</table></td>"+
	    "<td colspan=\"13\" style=\"border-bottom: blue 1px solid;\">&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	  "<td>&nbsp;</td>"+
	  "<td>&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils1</td>"+
	    "<td  align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils2</td>"+
	    "<td  align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils3</td>"+
	    "<td  align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils4</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils5</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils6</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils7</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils8</td>"+
	    "<td >&nbsp;</td>"+
		"<td>&nbsp;</td>"+
		"<td>&nbsp;</td>"+
	  "</tr>"+
	"</table>";

	public static String STR_TEMPLATE_LIST_9 = "<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
			+ "<tr>"
			+ "<td colspan=\"3\" align=\"left\">Pere</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td rowspan=\"23\" style=\"border-right: blue 1px solid\">&nbsp;</td>"
			+ "<td colspan=\"2\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils1</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-top: blue 1px solid\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td colspan=\"2\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils2</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils3</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils4</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils5</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils6</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils7</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils8</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td >&nbsp;</td>"
			+ "</tr>" + "</table>";

	// public static String STR_TEMPLATE_10 = "<table width=\"37%\"
	// height=\"41%\" border=\"0\" cellspacing=\"0\" style=\"border-top: #6e6e6e
	// 1px solid;\">"+
	// "<tr>"+
	// "<td>&nbsp;</td>"+
	// "<td width=\"7%\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td width=\"9%\">&nbsp;</td>"+
	// "<td width=\"7%\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"middle\" ></td>"+
	// "<td colspan=\"2\"></td>"+
	// "<td colspan=\"3\" align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Pere</td>"+
	// "<td>&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td width=\"7%\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td width=\"7%\">&nbsp;</td>"+
	// "<td width=\"7%\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td width=\"7%\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td width=\"2%\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td colspan=\"13\">&nbsp;</td>"+
	// "<td colspan=\"14\" style=\"border-left: blue 1px solid;\">&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td width=\"9%\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\"\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td style=\"border-left: blue 1px solid;\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils1</td>"+
	// "<td align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils2</td>"+
	// "<td align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils3</td>"+
	// "<td align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils4</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils5</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils6</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils7</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils8</td>"+
	// "<td width=\"5%\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils9</td>"+
	// "<td width=\"3%\">&nbsp;</td>"+
	// "</tr>"+
	// "</table>";

	public static String STR_TEMPLATE_10 = "<table border=\"0\" cellspacing=\"0\" align=\"center\" cellpadding=\"0\">"+
	  "<tr>"+
	    "<td colspan=\"12\" style=\"border-bottom: blue 1px solid;\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" style=\"border-bottom: blue 1px solid;\"><table align=\"center\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	    "<td style=\"border-left: blue 1px solid; \">&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td colspan=\"2\">Pere</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	    "<td style=\"border-left: blue 1px solid; \">&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	"</table></td>"+
	    "<td colspan=\"11\" style=\"border-bottom: blue 1px solid;\">&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	    "<td align=\"center\" valign=\"top\" >Fils1</td>"+
	    "<td  align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils2</td>"+
	    "<td  align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils3</td>"+
	    "<td  align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils4</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils5</td>"+
	    "<td >&nbsp;</td>"+
	    "<td align=\"center\" valign=\"top\" >Fils6</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils7</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils8</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils9</td>"+
	    "<td >&nbsp;</td>"+
	  "</tr>"+
	"</table>";

	public static String STR_TEMPLATE_LIST_10 = "<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
			+ "<tr>"
			+ "<td colspan=\"3\" align=\"left\">Pere</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td rowspan=\"26\" style=\"border-right: blue 1px solid\">&nbsp;</td>"
			+ "<td colspan=\"2\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils1</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-top: blue 1px solid\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td colspan=\"2\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils2</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils3</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils4</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils5</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils6</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils7</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils8</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils9</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td >&nbsp;</td>"
			+ "</tr>" + "</table>";

	// public static String STR_TEMPLATE_11 = "<table width=\"48%\"
	// height=\"41%\" border=\"0\" cellspacing=\"0\" style=\"border-top: #6e6e6e
	// 1px solid;\">"+
	// "<tr>"+
	// "<td>&nbsp;</td>"+
	// "<td width=\"9%\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td width=\"5%\">&nbsp;</td>"+
	// "<td width=\"8%\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"middle\" ></td>"+
	// "<td colspan=\"2\"></td>"+
	// "<td colspan=\"3\" align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" ></td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Pere</td>"+
	// "<td width=\"6%\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td width=\"5%\">&nbsp;</td>"+
	// "<td width=\"7%\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td width=\"8%\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td width=\"8%\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td colspan=\"15\">&nbsp;</td>"+
	// "<td colspan=\"11\" style=\"border-left: blue 1px solid;\">&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\"\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid;border-right: blue 1px solid;\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils1</td>"+
	// "<td align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils2</td>"+
	// "<td align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils3</td>"+
	// "<td align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils4</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils5</td>"+
	// "<td width=\"7%\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils6</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils7</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils8</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils9</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_10</td>"+
	// "<td >&nbsp;</td>"+
	// "</tr>"+
	// "</table>";

	public static String STR_TEMPLATE_11 = "<table border=\"0\" cellspacing=\"0\" align=\"center\" cellpadding=\"0\">"+
	  "<tr>"+
	    "<td colspan=\"15\" style=\"border-bottom: blue 1px solid;\">&nbsp;</td>"+
		"<td colspan=\"2\" align=\"center\" valign=\"top\" style=\"border-bottom: blue 1px solid;\"><table align=\"center\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	    "<td style=\"border-left: blue 1px solid; \">&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td colspan=\"2\">Pere</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	    "<td style=\"border-left: blue 1px solid; \">&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	"</table></td>"+
	    "<td colspan=\"15\" style=\"border-bottom: blue 1px solid;\">&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	  "<td>&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils1</td>"+
	    "<td  align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils2</td>"+
	    "<td  align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils3</td>"+
	    "<td  align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils4</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils5</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils6</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils7</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils8</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils9</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_10</td>"+
	    "<td >&nbsp;</td>"+
		"<td>&nbsp;</td>"+
	  "</tr>"+
	"</table>";

	public static String STR_TEMPLATE_LIST_11 = "<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
			+ "<tr>"
			+ "<td colspan=\"3\" align=\"left\">Pere</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td rowspan=\"29\" style=\"border-right: blue 1px solid\">&nbsp;</td>"
			+ "<td colspan=\"2\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils1</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-top: blue 1px solid\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td colspan=\"2\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils2</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils3</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils4</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils5</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils6</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils7</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils8</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils9</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_10</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td >&nbsp;</td>"
			+ "</tr>" + "</table>";

	// public static String STR_TEMPLATE_12 = "<table width=\"48%\"
	// height=\"41%\" border=\"0\" cellspacing=\"0\" style=\"border-top: #6e6e6e
	// 1px solid;\">"+
	// "<tr>"+
	// "<td>&nbsp;</td>"+
	// "<td width=\"8%\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td width=\"2%\">&nbsp;</td>"+
	// "<td width=\"8%\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"middle\" ></td>"+
	// "<td colspan=\"2\"></td>"+
	// "<td colspan=\"3\" align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" ></td>"+
	// "<td></td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Pere</td>"+
	// "<td>&nbsp;</td>"+
	// "<td width=\"5%\">&nbsp;</td>"+
	// "<td width=\"7%\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td width=\"8%\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td width=\"8%\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td width=\"2%\">&nbsp;</td>"+
	// "<td width=\"5%\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td width=\"3%\">&nbsp;</td>"+
	// "<td width=\"5%\">&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td colspan=\"16\">&nbsp;</td>"+
	// "<td colspan=\"10\" style=\"border-left: blue 1px solid;\">&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\"\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\"style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid;border-right: blue 1px solid;\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils1</td>"+
	// "<td align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils2</td>"+
	// "<td align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils3</td>"+
	// "<td align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils4 </td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils5</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils6</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils7</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils8</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils9</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_10</td>"+
	// "<td width=\"3%\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_11</td>"+
	// "</tr>"+
	// "</table>";

	public static String STR_TEMPLATE_12 = "<table align=\"center\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
	   "<tr>"+
	    "<td colspan=\"16\" style=\"border-bottom: blue 1px solid;\">&nbsp;</td>"+
		"<td colspan=\"2\" style=\"border-bottom: blue 1px solid;\"><table align=\"center\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	    "<td style=\"border-left: blue 1px solid; \">&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td colspan=\"2\">Pere</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	    "<td style=\"border-left: blue 1px solid; \">&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	"</table></td>"+
	    "<td colspan=\"16\" style=\"border-bottom: blue 1px solid;\">&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	  "<td>&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils1</td>"+
	    "<td  align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils2</td>"+
	    "<td  align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils3</td>"+
	    "<td  align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils4</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils5</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils6</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils7</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils8</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils9</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_10</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_11</td>"+
		"<td>&nbsp;</td>"+
	  "</tr>"+
	"</table>";

	public static String STR_TEMPLATE_LIST_12 = "<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
			+ "<tr>"
			+ "<td colspan=\"3\" align=\"left\">Pere</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td rowspan=\"32\" style=\"border-right: blue 1px solid\">&nbsp;</td>"
			+ "<td colspan=\"2\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils1</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-top: blue 1px solid\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td colspan=\"2\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils2</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils3</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils4</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils5</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils6</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils7</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils8</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils9</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_10</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_11</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td >&nbsp;</td>"
			+ "</tr>" + "</table>";

	// public static String STR_TEMPLATE_13 = "<table width=\"48%\"
	// height=\"41%\" border=\"0\" cellspacing=\"0\" style=\"border-top: #6e6e6e
	// 1px solid;\">"+
	// "<tr>"+
	// "<td>&nbsp;</td>"+
	// "<td width=\"7%\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td width=\"7%\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"middle\" ></td>"+
	// "<td colspan=\"2\"></td>"+
	// "<td colspan=\"3\" align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" ></td>"+
	// "<td></td>"+
	// "<td colspan=\"2\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Pere</td>"+
	// "<td >&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td width=\"7%\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td width=\"7%\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td width=\"8%\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td width=\"8%\">&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td width=\"6%\">&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<tr>"+
	// "<tr>"+
	// "<td colspan=\"18\">&nbsp;</td>"+
	// "<td colspan=\"11\" style=\"border-left: blue 1px solid;\">&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<tr>"+
	// "<tr>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\"\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\"style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid;border-right: blue 1px solid;\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<tr>"+
	// "<tr>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils1</td>"+
	// "<td align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils2</td>"+
	// "<td align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils3</td>"+
	// "<td align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils4</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils5</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils6</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils7</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils8</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils9</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_10</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_11</td>"+
	// "<td>&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_12</td>"+
	// "<tr>"+
	// "</table>";

	public static String STR_TEMPLATE_13 = "<table align=\"center\"  border=\"0\" cellspacing=\"0\"> cellpadding=\"0\""+
	  "<tr>"+
	    "<td colspan=\"18\" style=\"border-bottom: blue 1px solid;\">&nbsp;</td>"+
		"<td colspan=\"2\" align=\"center\" valign=\"top\" style=\"border-bottom: blue 1px solid;\"><table align=\"center\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	    "<td style=\"border-left: blue 1px solid; \">&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td colspan=\"2\">Pere</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	    "<td style=\"border-left: blue 1px solid; \">&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	"</table></td>"+
	    "<td colspan=\"17\" style=\"border-bottom: blue 1px solid;\">&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	  "<td>&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils1</td>"+
	    "<td  align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils2</td>"+
	    "<td  align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils3</td>"+
	    "<td  align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils4</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils5</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils6</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils7</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils8</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils9</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_10</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_11</td>"+
		    "<td>&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_12</td>"+
		"<td>&nbsp;</td>"+
	  "</tr>"+
	"</table>";

	public static String STR_TEMPLATE_LIST_13 = "<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
			+ "<tr>"
			+ "<td colspan=\"3\" align=\"left\">Pere</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td rowspan=\"35\" style=\"border-right: blue 1px solid\">&nbsp;</td>"
			+ "<td colspan=\"2\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils1</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-top: blue 1px solid\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td colspan=\"2\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils2</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils3</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils4</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils5</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils6</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils7</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils8</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils9</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_10</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_11</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_12</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td >&nbsp;</td>"
			+ "</tr>" + "</table>";

	// public static String STR_TEMPLATE_14 = "<table width=\"48%\"
	// height=\"41%\" border=\"0\" cellspacing=\"0\" style=\"border-top: #6e6e6e
	// 1px solid;\">"+
	// "<tr>"+
	// "<td>&nbsp;</td>"+
	// "<td width=\"7%\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td width=\"7%\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"middle\" ></td>"+
	// "<td colspan=\"2\"></td>"+
	// "<td colspan=\"3\" align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" ></td>"+
	// "<td></td>"+
	// "<td colspan=\"2\">&nbsp;</td>"+
	// "<td></td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Pere</td>"+
	// "<td >&nbsp;</td>"+
	// "<td width=\"7%\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td width=\"7%\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td width=\"8%\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td width=\"8%\">&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td width=\"8%\">&nbsp;</td>"+
	// "<td width=\"8%\">&nbsp;</td>"+
	// "<td width=\"8%\">&nbsp;</td>"+
	// "<td width=\"8%\">&nbsp;</td>"+
	// "<td width=\"8%\">&nbsp;</td>"+
	// "<td width=\"8%\">&nbsp;</td>"+
	// "<td width=\"8%\">&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td colspan=\"19\">&nbsp;</td>"+
	// "<td colspan=\"13\" style=\"border-left: blue 1px solid;\">&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\"\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\"style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"5\" style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid;border-right: blue 1px solid;\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils1</td>"+
	// "<td align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils2</td>"+
	// "<td align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils3</td>"+
	// "<td align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils4 </td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils5</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils6</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils7</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils8</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils9</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_10</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_11</td>"+
	// "<td>&nbsp;</td>"+
	// "<td colspan=\"4\" align=\"center\" valign=\"top\">Fils_12</td>"+
	// "<td>&nbsp;</td>"+
	// "<td colspan=\"4\" align=\"center\" valign=\"top\">Fils_13</td>"+
	// "</tr>"+
	// "</table>";

	public static String STR_TEMPLATE_14 = "<table align=\"center\"  border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
	  "<tr>"+
	    "<td colspan=\"19\" style=\"border-bottom: blue 1px solid;\">&nbsp;</td>"+
		"<td colspan=\"2\" align=\"center\" valign=\"top\" style=\"border-bottom: blue 1px solid;\"><table align=\"center\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	    "<td style=\"border-left: blue 1px solid; \">&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td colspan=\"2\">Pere</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	    "<td style=\"border-left: blue 1px solid; \">&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	"</table></td>"+
	    "<td colspan=\"22\" style=\"border-bottom: blue 1px solid;\">&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	  "<td>&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils1</td>"+
	    "<td  align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils2</td>"+
	    "<td  align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils3</td>"+
	    "<td  align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils4</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils5</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils6</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils7</td>"+
	    "<td >&nbsp;</td>"+
	    "<td align=\"center\" valign=\"top\" >Fils8</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils9</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_10</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_11</td>"+
	    "<td>&nbsp;</td>"+
	    "<td colspan=\"4\" align=\"center\" valign=\"top\" >Fils_12</td>"+
		    "<td>&nbsp;</td>"+
	            "<td colspan=\"4\" align=\"center\" valign=\"top\" >Fils_13</td>"+
				"<td>&nbsp;</td>"+
	  "</tr>"+
	"</table>";

	public static String STR_TEMPLATE_LIST_14 = "<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
			+ "<tr>"
			+ "<td colspan=\"3\" align=\"left\">Pere</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td rowspan=\"38\" style=\"border-right: blue 1px solid\">&nbsp;</td>"
			+ "<td colspan=\"2\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils1</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-top: blue 1px solid\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td colspan=\"2\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils2</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils3</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils4</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils5</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils6</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils7</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils8</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils9</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_10</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_11</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_12</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_13</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td >&nbsp;</td>"
			+ "</tr>" + "</table>";

	// public static String STR_TEMPLATE_15 = "<table width=\"48%\"
	// height=\"41%\" border=\"0\" cellspacing=\"0\" style=\"border-top: #6e6e6e
	// 1px solid;\">"+
	// "<tr>"+
	// "<td width=\"1%\">&nbsp;</td>"+
	// "<td width=\"5%\">&nbsp;</td>"+
	// "<td width=\"1%\">&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"5%\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"middle\" ></td>"+
	// "<td colspan=\"2\"></td>"+
	// "<td colspan=\"3\" align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" ></td>"+
	// "<td width=\"1%\"></td>"+
	// "<td colspan=\"2\">&nbsp;</td>"+
	// "<td width=\"1%\"></td>"+
	// "<td colspan=\"2\"></td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Pere</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"5%\">&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"5%\">&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"5%\">&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"5%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"-3%\">&nbsp;</td>"+
	// "<td width=\"5%\" >&nbsp;</td>"+
	// "<td width=\"4%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"4%\" >&nbsp;</td>"+
	// "<td width=\"3%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td colspan=\"21\">&nbsp;</td>"+
	// "<td colspan=\"11\" style=\"border-left: blue 1px solid;\">&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\"\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\"style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td style=\"border-left: blue 1px solid\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils1</td>"+
	// "<td align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils2</td>"+
	// "<td width=\"1%\" align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils3</td>"+
	// "<td width=\"1%\" align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils4</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils5</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils6</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils7</td>"+
	// "<td width=\"1%\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils8</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils9</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_10</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_11</td>"+
	// "<td>&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_12</td>"+
	// "<td>&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_13</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_14</td>"+
	// "<td >&nbsp;</td>"+
	// "</tr>"+
	// "</table>";

	public static String STR_TEMPLATE_15 = "<table align=\"center\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
	  "<tr>"+
	    "<td colspan=\"21\" style=\"border-bottom: blue 1px solid;\">&nbsp;</td>"+
		"<td colspan=\"2\" align=\"center\" valign=\"top\" style=\"border-bottom: blue 1px solid;\"><table align=\"center\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	    "<td style=\"border-left: blue 1px solid; \">&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td colspan=\"2\">Pere</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	    "<td style=\"border-left: blue 1px solid; \">&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	"</table></td>"+
	    "<td colspan=\"20\"  style=\"border-bottom: blue 1px solid;\">&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	  "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils1</td>"+
	    "<td  align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils2</td>"+
	    "<td   align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils3</td>"+
	    "<td   align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils4</td>"+
	    "<td  >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils5</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils6</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils7</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils8</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils9</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_10</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_11</td>"+
	    "<td>&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_12</td>"+
	    "<td>&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_13</td>"+
		    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_14</td>"+
	        "<td >&nbsp;</td>"+
	  "</tr>"+
	"</table>";

	public static String STR_TEMPLATE_LIST_15 = "<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
			+ "<tr>"
			+ "<td colspan=\"3\" align=\"left\">Pere</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td rowspan=\"41\" style=\"border-right: blue 1px solid\">&nbsp;</td>"
			+ "<td colspan=\"2\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils1</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-top: blue 1px solid\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td colspan=\"2\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils2</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils3</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils4</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils5</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils6</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils7</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils8</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils9</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_10</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_11</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_12</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_13</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_14</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td >&nbsp;</td>"
			+ "</tr>" + "</table>";

	// public static String STR_TEMPLATE_16 = "<table width=\"48%\"
	// height=\"41%\" border=\"0\" cellspacing=\"0\" style=\"border-top: #6e6e6e
	// 1px solid;\">"+
	// "<tr>"+
	// "<td width=\"1%\">&nbsp;</td>"+
	// "<td width=\"5%\">&nbsp;</td>"+
	// "<td width=\"1%\">&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"5%\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"middle\" ></td>"+
	// "<td colspan=\"2\"></td>"+
	// "<td colspan=\"3\" align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" ></td>"+
	// "<td width=\"1%\"></td>"+
	// "<td colspan=\"2\">&nbsp;</td>"+
	// "<td width=\"1%\"></td>"+
	// "<td colspan=\"2\"></td>"+
	// "<td></td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Pere</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"5%\">&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"5%\">&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"5%\">&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"5%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"-3%\">&nbsp;</td>"+
	// "<td width=\"5%\" >&nbsp;</td>"+
	// "<td width=\"4%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"4%\" >&nbsp;</td>"+
	// "<td width=\"3%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td colspan=\"22\">&nbsp;</td>"+
	// "<td colspan=\"12\" style=\"border-left: blue 1px solid;\">&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\"\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\"style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid;border-right: blue 1px solid\">&nbsp;</td>"+
	// "<td>&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils1</td>"+
	// "<td align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils2</td>"+
	// "<td width=\"1%\" align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils3</td>"+
	// "<td width=\"1%\" align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils4 </td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils5</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils6</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils7</td>"+
	// "<td width=\"1%\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils8</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils9</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_10</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_11</td>"+
	// "<td>&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_12</td>"+
	// "<td>&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_13</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_14</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_15</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "</tr>"+
	// "</table>";

	public static String STR_TEMPLATE_16 = "<table align=\"center\"  border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
	  "<tr>"+
	    "<td colspan=\"22\"  style=\"border-bottom: blue 1px solid;\">&nbsp;</td>"+
		"<td colspan=\"2\" align=\"center\" valign=\"top\"  style=\"border-bottom: blue 1px solid;\"><table align=\"center\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	    "<td style=\"border-left: blue 1px solid; \">&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td colspan=\"2\">Pere</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	    "<td style=\"border-left: blue 1px solid; \">&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	"</table></td>"+
	    "<td colspan=\"22\"  style=\"border-bottom: blue 1px solid;\">&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	  "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils1</td>"+
	    "<td  align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils2</td>"+
	    "<td   align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils3</td>"+
	    "<td   align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils4</td>"+
	    "<td  >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils5</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils6</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils7</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils8</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils9</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_10</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_11</td>"+
	    "<td>&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_12</td>"+
	    "<td>&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_13</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_14</td>"+
	    "<td >&nbsp;</td>"+
		    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_15</td>"+
	    "<td >&nbsp;</td>"+
	  "</tr>"+
	"</table>";

	public static String STR_TEMPLATE_LIST_16 = "<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
			+ "<tr>"
			+ "<td colspan=\"3\" align=\"left\">Pere</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td rowspan=\"44\" style=\"border-right: blue 1px solid\">&nbsp;</td>"
			+ "<td colspan=\"2\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils1</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-top: blue 1px solid\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td colspan=\"2\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils2</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils3</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils4</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils5</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils6</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils7</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils8</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils9</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_10</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_11</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_12</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_13</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_14</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_15</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td >&nbsp;</td>"
			+ "</tr>" + "</table>";

	// public static String STR_TEMPLATE_17 = "<table width=\"48%\"
	// height=\"41%\" border=\"0\" cellspacing=\"0\" style=\"border-top: #6e6e6e
	// 1px solid;\">"+
	// "<tr>"+
	// "<td width=\"1%\">&nbsp;</td>"+
	// "<td width=\"5%\">&nbsp;</td>"+
	// "<td width=\"1%\">&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"5%\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"middle\" ></td>"+
	// "<td colspan=\"2\"></td>"+
	// "<td colspan=\"3\" align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" ></td>"+
	// "<td width=\"1%\"></td>"+
	// "<td colspan=\"2\">&nbsp;</td>"+
	// "<td width=\"1%\"></td>"+
	// "<td colspan=\"2\"></td>"+
	// "<td></td>"+
	// "<td colspan=\"3\" align=\"center\" valign=\"top\">Pere</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"5%\">&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"5%\">&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"5%\">&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"5%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"-3%\">&nbsp;</td>"+
	// "<td width=\"5%\" >&nbsp;</td>"+
	// "<td width=\"4%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"4%\" >&nbsp;</td>"+
	// "<td width=\"3%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td colspan=\"23\">&nbsp;</td>"+
	// "<td colspan=\"12\" style=\"border-left: blue 1px solid;\">&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\"\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\"style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid;border-right: blue 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-top: blue 1px solid;border-right: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils1 </td>"+
	// "<td align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils2 </td>"+
	// "<td width=\"1%\" align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils3 </td>"+
	// "<td width=\"1%\" align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils4 </td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils5</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils6</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils7</td>"+
	// "<td width=\"1%\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils8</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils9</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_10</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_11</td>"+
	// "<td>&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_12</td>"+
	// "<td>&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_13</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_14</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_15</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_16</td>"+
	// "<td >&nbsp;</td>"+
	// "</tr>"+
	// "</table>";

	public static String STR_TEMPLATE_17 = "<table align=\"center\"  border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
	   "<tr>"+
	    "<td colspan=\"23\" style=\"border-bottom: blue 1px solid;\">&nbsp;</td>"+
		"<td colspan=\"2\" align=\"center\" valign=\"top\" style=\"border-bottom: blue 1px solid;\"><table align=\"center\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	    "<td style=\"border-left: blue 1px solid; \">&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td colspan=\"2\">Pere</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	    "<td style=\"border-left: blue 1px solid; \">&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	"</table></td>"+
	    "<td colspan=\"23\"  style=\"border-bottom: blue 1px solid;\">&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	  "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils1</td>"+
	    "<td  align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils2</td>"+
	    "<td   align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils3</td>"+
	    "<td   align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils4</td>"+
	    "<td  >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils5</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils6</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils7</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils8</td>"+
	    "<td  >&nbsp;</td>"+
	    "<td align=\"center\" valign=\"top\" >Fils9</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_10</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_11</td>"+
	    "<td>&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_12</td>"+
	    "<td>&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_13</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_14</td>"+
	    "<td >&nbsp;</td>"+
		    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_15</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_16</td>"+
	    "<td >&nbsp;</td>"+
	  "</tr>"+
	"</table>";

	public static String STR_TEMPLATE_LIST_17 = "<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
			+ "<tr>"
			+ "<td colspan=\"3\" align=\"left\">Pere</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td rowspan=\"47\" style=\"border-right: blue 1px solid\">&nbsp; </td>"
			+ "<td colspan=\"2\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils1</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-top: blue 1px solid\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td colspan=\"2\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils2</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils3</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils4</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils5</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils6</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils7</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils8</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils9</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_10</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_11</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_12</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_13</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_14</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_15</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_16</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td >&nbsp;</td>"
			+ "</tr>" + "</table>";

	// public static String STR_TEMPLATE_18 = "<table width=\"48%\"
	// height=\"41%\" border=\"0\" cellspacing=\"0\" style=\"border-top: #6e6e6e
	// 1px solid;\">"+
	// "<tr>"+
	// "<td width=\"1%\">&nbsp;</td>"+
	// "<td width=\"5%\">&nbsp;</td>"+
	// "<td width=\"1%\">&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"5%\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"middle\" ></td>"+
	// "<td colspan=\"2\"></td>"+
	// "<td colspan=\"3\" align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" ></td>"+
	// "<td width=\"1%\"></td>"+
	// "<td colspan=\"2\">&nbsp;</td>"+
	// "<td width=\"1%\"></td>"+
	// "<td colspan=\"2\"></td>"+
	// "<td></td>"+
	// "<td colspan=\"3\" align=\"center\" valign=\"top\"></td>"+
	// "<td colspan=\"3\" align=\"center\" valign=\"top\">Pere</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"5%\">&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"5%\">&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"5%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"-3%\">&nbsp;</td>"+
	// "<td width=\"5%\" >&nbsp;</td>"+
	// "<td width=\"4%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"4%\" >&nbsp;</td>"+
	// "<td width=\"3%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td colspan=\"25\">&nbsp;</td>"+
	// "<td colspan=\"12\" style=\"border-left: blue 1px solid;\">&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\"\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\"style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid;border-right: blue 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-top: blue 1px solid;border-right: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-top: blue 1px solid;border-right: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils1</td>"+
	// "<td align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils2</td>"+
	// "<td width=\"1%\" align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils3</td>"+
	// "<td width=\"1%\" align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils4</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils5</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils6</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils7</td>"+
	// "<td width=\"1%\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils8</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils9</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_10</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_11</td>"+
	// "<td>&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_12</td>"+
	// "<td>&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_13</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_14</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_15</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_16</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_17</td>"+
	// "<td >&nbsp;</td>"+
	// "</tr>"+
	// "</table>";

	public static String STR_TEMPLATE_18 = "<table align=\"center\"  border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
	  "<tr>"+
	    "<td colspan=\"25\" style=\"border-bottom: blue 1px solid;\">&nbsp;</td>"+
		"<td colspan=\"2\" align=\"center\" valign=\"top\" style=\"border-bottom: blue 1px solid;\"><table align=\"center\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	    "<td style=\"border-left: blue 1px solid; \">&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td colspan=\"2\">Pere</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	    "<td style=\"border-left: blue 1px solid; \">&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	"</table></td>"+
	    "<td colspan=\"25\" style=\"border-bottom: blue 1px solid;\">&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	  "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils1</td>"+
	    "<td  align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils2</td>"+
	    "<td   align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils3</td>"+
	    "<td   align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils4</td>"+
	    "<td  >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils5</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils6</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils7</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils8</td>"+
	    "<td  >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils9</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_10</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_11</td>"+
	    "<td>&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_12</td>"+
	    "<td>&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_13</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_14</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_15</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_16</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_17</td>"+
	    "<td >&nbsp;</td>"+
	  "</tr>"+
	"</table>";

	public static String STR_TEMPLATE_LIST_18 = "<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
			+ "<tr>"
			+ "<td colspan=\"3\" align=\"left\">Pere</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td rowspan=\"50\" style=\"border-right: blue 1px solid\">&nbsp;</td>"
			+ "<td colspan=\"2\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils1</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-top: blue 1px solid\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td colspan=\"2\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils2</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils3</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils4</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils5</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils6</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils7</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils8</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils9</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_10</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_11</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_12</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_13</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_14</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_15</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_16</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_17</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td >&nbsp;</td>"
			+ "</tr>" + "</table>";

	// public static String STR_TEMPLATE_19 = "<table width=\"48%\"
	// height=\"41%\" border=\"0\" cellspacing=\"0\" style=\"border-top: #6e6e6e
	// 1px solid;\">"+
	// "<tr>"+
	// "<td width=\"1%\">&nbsp;</td>"+
	// "<td width=\"5%\">&nbsp;</td>"+
	// "<td width=\"1%\">&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"5%\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"middle\" ></td>"+
	// "<td colspan=\"2\"></td>"+
	// "<td colspan=\"3\" align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" ></td>"+
	// "<td width=\"1%\"></td>"+
	// "<td colspan=\"2\">&nbsp;</td>"+
	// "<td width=\"1%\"></td>"+
	// "<td colspan=\"2\"></td>"+
	// "<td></td>"+
	// "<td colspan=\"3\" align=\"center\" valign=\"top\"></td>"+
	// "<td colspan=\"3\" align=\"center\" valign=\"top\">Pere</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"5%\">&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"5%\">&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"5%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"-3%\">&nbsp;</td>"+
	// "<td width=\"5%\" >&nbsp;</td>"+
	// "<td width=\"4%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"4%\" >&nbsp;</td>"+
	// "<td width=\"3%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td colspan=\"26\">&nbsp;</td>"+
	// "<td colspan=\"12\" style=\"border-left: blue 1px solid;\">&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\"\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\"style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid;border-right: blue 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-top: blue 1px solid;border-right: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-top: blue 1px solid;border-right: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-top: blue 1px solid;border-right: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils1 </td>"+
	// "<td align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils2 </td>"+
	// "<td width=\"1%\" align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils3 </td>"+
	// "<td width=\"1%\" align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils4 </td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils5</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils6</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils7</td>"+
	// "<td width=\"1%\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils8</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils9</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_10</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_11</td>"+
	// "<td>&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_12</td>"+
	// "<td>&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_13</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_14</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_15</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_16</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_17</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_18</td>"+
	// "<td >&nbsp;</td>"+
	// "</tr>"+
	// "</table>";

	public static String STR_TEMPLATE_19 = "<table align=\"center\"  border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
	  "<tr>"+
	    "<td colspan=\"26\" style=\"border-bottom: blue 1px solid;\">&nbsp;</td>"+
		"<td colspan=\"2\" align=\"center\" valign=\"top\" style=\"border-bottom: blue 1px solid;\"><table align=\"center\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	    "<td style=\"border-left: blue 1px solid; \">&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td colspan=\"2\">Pere</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	    "<td style=\"border-left: blue 1px solid; \">&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	"</table></td>"+
	    "<td colspan=\"26\" style=\"border-bottom: blue 1px solid;\">&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	  "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils1</td>"+
	    "<td  align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils2</td>"+
	    "<td   align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils3</td>"+
	    "<td   align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils4</td>"+
	    "<td  >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils5</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils6</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils7</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils8</td>"+
	    "<td  >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils9</td>"+
	    "<td >&nbsp;</td>"+
	    "<td align=\"center\" valign=\"top\" >Fils_10</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_11</td>"+
	    "<td>&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_12</td>"+
	    "<td>&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_13</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_14</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_15</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_16</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_17</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_18</td>"+
	    "<td >&nbsp;</td>"+
	  "</tr>"+
	"</table>";

	public static String STR_TEMPLATE_LIST_19 = "<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
			+ "<tr>"
			+ "<td colspan=\"3\" align=\"left\">Pere</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td rowspan=\"53\" style=\"border-right: blue 1px solid\">&nbsp;</td>"
			+ "<td colspan=\"2\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils1</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-top: blue 1px solid\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td colspan=\"2\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils2</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils3</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils4</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils5</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils6</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils7</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils8</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils9</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_10</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_11</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_12</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_13</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_14</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_15</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_16</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_17</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_18</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td >&nbsp;</td>"
			+ "</tr>" + "</table>";

	// public static String STR_TEMPLATE_20 = "<table width=\"48%\"
	// height=\"41%\" border=\"0\" cellspacing=\"0\" style=\"border-top: #6e6e6e
	// 1px solid;\">"+
	// "<tr>"+
	// "<td width=\"1%\">&nbsp;</td>"+
	// "<td width=\"5%\">&nbsp;</td>"+
	// "<td width=\"1%\">&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"5%\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"middle\" ></td>"+
	// "<td colspan=\"2\"></td>"+
	// "<td colspan=\"3\" align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" ></td>"+
	// "<td width=\"1%\"></td>"+
	// "<td colspan=\"2\">&nbsp;</td>"+
	// "<td width=\"1%\"></td>"+
	// "<td colspan=\"2\"></td>"+
	// "<td></td>"+
	// "<td colspan=\"3\" align=\"center\" valign=\"top\"></td>"+
	// "<td colspan=\"3\" align=\"center\" valign=\"top\"></td>"+
	// "<td colspan=\"3\" align=\"center\" valign=\"top\">Pere</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"5%\">&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"5%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"-3%\">&nbsp;</td>"+
	// "<td width=\"5%\" >&nbsp;</td>"+
	// "<td width=\"4%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"4%\" >&nbsp;</td>"+
	// "<td width=\"3%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td colspan=\"28\">&nbsp;</td>"+
	// "<td colspan=\"10\" style=\"border-left: blue 1px solid;\">&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\"\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\"style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid;border-right: blue 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-top: blue 1px solid;border-right: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-top: blue 1px solid;border-right: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-top: blue 1px solid;border-right: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-top: blue 1px solid;border-right: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils1 </td>"+
	// "<td align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils2 </td>"+
	// "<td width=\"1%\" align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils3 </td>"+
	// "<td width=\"1%\" align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils4 </td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils5</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils6</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils7</td>"+
	// "<td width=\"1%\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils8</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils9</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_10</td>"+
	// "<td width=\"5%\" >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_11</td>"+
	// "<td>&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_12</td>"+
	// "<td>&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_13</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_14</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_15</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_16</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_17</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_18</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_19</td>"+
	// "</tr>"+
	// "</table>";

	public static String STR_TEMPLATE_20 = "<table align=\"center\"  border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
	  "<tr>"+
	    "<td colspan=\"28\" style=\"border-bottom: blue 1px solid;\">&nbsp;</td>"+
		"<td colspan=\"2\" align=\"center\" valign=\"top\" style=\"border-bottom: blue 1px solid;\"><table align=\"center\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	    "<td style=\"border-left: blue 1px solid; \">&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td colspan=\"2\">Pere</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	    "<td style=\"border-left: blue 1px solid; \">&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	"</table></td>"+
	    "<td colspan=\"28\" style=\"border-bottom: blue 1px solid;\">&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	  "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils1</td>"+
	    "<td  align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils2</td>"+
	    "<td   align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils3</td>"+
	    "<td   align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils4</td>"+
	    "<td  >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils5</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils6</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils7</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils8</td>"+
	    "<td  >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils9</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_10</td>"+
	    "<td  >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_11</td>"+
	    "<td>&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_12</td>"+
	    "<td>&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_13</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_14</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_15</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_16</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_17</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_18</td>"+
	    "<td >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_19</td>"+
		"<td  >&nbsp;</td>"+
	  "</tr>"+
	"</table>";

	public static String STR_TEMPLATE_LIST_20 = "<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
			+ "<tr>"
			+ "<td colspan=\"3\" align=\"left\">Pere</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td rowspan=\"56\" style=\"border-right: blue 1px solid\">&nbsp;</td>"
			+ "<td colspan=\"2\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils1</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-top: blue 1px solid\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td colspan=\"2\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils2</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils3</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils4</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils5</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils6</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils7</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils8</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils9</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_10</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_11</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_12</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_13</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_14</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_15</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_16</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_17</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_18</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_19</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td >&nbsp;</td>"
			+ "</tr>" + "</table>";

	// public static String STR_TEMPLATE_21 = "<table width=\"48%\"
	// height=\"41%\" border=\"0\" cellspacing=\"0\" style=\"border-top: #6e6e6e
	// 1px solid;\">"+
	// "<tr>"+
	// "<td width=\"1%\">&nbsp;</td>"+
	// "<td width=\"5%\">&nbsp;</td>"+
	// "<td width=\"1%\">&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"5%\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"middle\" ></td>"+
	// "<td colspan=\"2\"></td>"+
	// "<td colspan=\"3\" align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" ></td>"+
	// "<td width=\"1%\"></td>"+
	// "<td colspan=\"2\">&nbsp;</td>"+
	// "<td width=\"1%\"></td>"+
	// "<td colspan=\"2\"></td>"+
	// "<td></td>"+
	// "<td colspan=\"3\" align=\"center\" valign=\"top\"></td>"+
	// "<td colspan=\"3\" align=\"center\" valign=\"top\"></td>"+
	// "<td align=\"center\" valign=\"top\"></td>"+
	// "<td colspan=\"3\" align=\"center\" valign=\"top\">Pere</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"5%\">&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"5%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"-3%\">&nbsp;</td>"+
	// "<td width=\"5%\" >&nbsp;</td>"+
	// "<td width=\"4%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"4%\" >&nbsp;</td>"+
	// "<td width=\"3%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td colspan=\"29\">&nbsp;</td>"+
	// "<td colspan=\"9\" style=\"border-left: blue 1px solid;\">&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\"\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid; border-top: blue
	// 1px solid;\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\"style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-left: blue 1px solid;border-top: blue
	// 1px solid;border-right: blue 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-top: blue 1px solid;border-right: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-top: blue 1px solid;border-right: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-top: blue 1px solid;border-right: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-top: blue 1px solid;border-right: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td colspan=\"3\" style=\"border-top: blue 1px solid;border-right: blue
	// 1px solid\">&nbsp;</td>"+
	// "<td >&nbsp;</td>"+
	// "</tr>"+
	// "<tr>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils1 </td>"+
	// "<td align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils2 </td>"+
	// "<td width=\"1%\" align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils3 </td>"+
	// "<td width=\"1%\" align=\"center\" valign=\"top\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils4 </td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils5</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils6</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils7</td>"+
	// "<td width=\"1%\">&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils8</td>"+
	// "<td width=\"1%\" >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils9</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_10</td>"+
	// "<td width=\"5%\" >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_11</td>"+
	// "<td>&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_12</td>"+
	// "<td>&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_13</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_14</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_15</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_16</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_17</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_18</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_19</td>"+
	// "<td >&nbsp;</td>"+
	// "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_20</td>"+
	// "</tr>"+
	// "</table>";

	public static String STR_TEMPLATE_21 = "<table align=\"center\"  border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
	  "<tr>"+
	    "<td colspan=\"29\" style=\"border-bottom: blue 1px solid;\">&nbsp;</td>"+
		"<td width=\"21\" colspan=\"2\" align=\"center\" valign=\"top\" style=\"border-bottom: blue 1px solid;\"><table align=\"center\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	    "<td style=\"border-left: blue 1px solid; \">&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td colspan=\"2\">Pere</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	    "<td>&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	    "<td style=\"border-left: blue 1px solid; \">&nbsp;</td>"+
	    "<td>&nbsp;</td>"+
	  "</tr>"+
	"</table></td>"+
	    "<td colspan=\"29\" style=\"border-bottom: blue 1px solid;\">&nbsp;</td>"+
	  "</tr>"+
	  "<tr>"+
	  "<td width=\"8\" >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils1</td>"+
	    "<td width=\"8\"  align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils2</td>"+
	    "<td width=\"8\"   align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils3</td>"+
	    "<td width=\"8\"   align=\"center\" valign=\"top\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils4</td>"+
	    "<td width=\"8\"  >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils5</td>"+
	    "<td width=\"8\" >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils6</td>"+
	    "<td width=\"8\" >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils7</td>"+
	    "<td width=\"8\" >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils8</td>"+
	    "<td width=\"8\"  >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils9</td>"+
	    "<td width=\"8\" >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_10</td>"+
	    "<td width=\"21\"  >&nbsp;</td>"+
	    "<td width=\"10\" align=\"center\" valign=\"top\" >Fils_11</td>"+
	    "<td width=\"8\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_12</td>"+
	    "<td width=\"8\">&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_13</td>"+
	    "<td width=\"8\" >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_14</td>"+
	    "<td width=\"8\" >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_15</td>"+
	    "<td width=\"8\" >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_16</td>"+
	    "<td width=\"8\" >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_17</td>"+
	    "<td width=\"8\" >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\">Fils_18</td>"+
	    "<td width=\"8\" >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_19</td>"+
		    "<td width=\"8\" >&nbsp;</td>"+
	    "<td colspan=\"2\" align=\"center\" valign=\"top\" >Fils_20</td>"+
		"<td width=\"35\" >&nbsp;</td>"+
	  "</tr>"+
	"</table>";

	public static String STR_TEMPLATE_LIST_21 = "<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
			+ "<tr>"
			+ "<td colspan=\"3\" align=\"left\">Pere</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td rowspan=\"59\" style=\"border-right: blue 1px solid\">&nbsp;</td>"
			+ "<td colspan=\"2\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils1</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-top: blue 1px solid\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td colspan=\"2\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils2</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils3</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils4</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils5</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils6</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"center\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils7</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils8</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils9</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_10</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_11</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_12</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_13</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_14</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_15</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_16</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_17</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_18</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_19</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td align=\"left\" valign=\"top\">&nbsp;</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style=\"border-bottom: blue 1px solid\">&nbsp;</td>"
			+ "<td rowspan=\"2\" align=\"left\" valign=\"center\">Fils_20</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td >&nbsp;</td>"
			+ "<td >&nbsp;</td>"
			+ "</tr>" + "</table>";

	// Replace exp1 by exp2 in strExpression.
	public static String replaceString(String exp1, String exp2,
			String strExpression) {
		
		try {
//			ParameterUtil.println("------------------- REPLACE :" + exp1 + " BY " + exp2);
//			 compilation de la regex avec le motif : "th�"
			Pattern p = Pattern.compile(exp1, Pattern.CASE_INSENSITIVE);
		// cr�ation du moteur associ� � la regex sur la cha�ne "J�aime le th�."
			Matcher m = p.matcher(strExpression);
		// remplacement de toutes les occurrences de "th�" par "chocolat"
			strExpression = m.replaceAll(exp2);
//			ParameterUtil.println("---------------- RESULT : " + strExpression);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
			
//		return strExpression.replaceAll(exp1, exp2);
		return strExpression;
	}

	// Get template matching current cell.
	public static String getTemplate(ClsCellule oCellule) {
		String strTemplate = "";
		try {
			if (oCellule.getOListefils().size() >= 0) {
				switch (oCellule.getOListefils().size()) {
				case 0:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_1;
					else
						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_1;

					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, oCellule
									.getCodeorganigramme()
									+ oCellule.getLibelle(), strTemplate);
					break;

				case 1:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_2;
					else
						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_2;

					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, oCellule
									.getCodeorganigramme()
									+ oCellule.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_1, oCellule.getOListefils()
									.get(0).getCodeorganigramme()
									+ oCellule.getOListefils().get(0)
											.getLibelle(), strTemplate);
					// ParameterUtil.println(strTemplate);
					break;

				case 2:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_3;
					else
						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_3;

					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, oCellule
									.getCodeorganigramme()
									+ oCellule.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_1, oCellule.getOListefils()
									.get(0).getCodeorganigramme()
									+ oCellule.getOListefils().get(0)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_2, oCellule.getOListefils()
									.get(1).getCodeorganigramme()
									+ oCellule.getOListefils().get(1)
											.getLibelle(), strTemplate);
					break;

				case 3:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_4;
					else
						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_4;

					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, oCellule
									.getCodeorganigramme()
									+ oCellule.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_1, oCellule.getOListefils()
									.get(0).getCodeorganigramme()
									+ oCellule.getOListefils().get(0)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_2, oCellule.getOListefils()
									.get(1).getCodeorganigramme()
									+ oCellule.getOListefils().get(1)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_3, oCellule.getOListefils()
									.get(2).getCodeorganigramme()
									+ oCellule.getOListefils().get(2)
											.getLibelle(), strTemplate);
					break;

				case 4:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_5;
					else
						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_5;

					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, oCellule
									.getCodeorganigramme()
									+ oCellule.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_1, oCellule.getOListefils()
									.get(0).getCodeorganigramme()
									+ oCellule.getOListefils().get(0)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_2, oCellule.getOListefils()
									.get(1).getCodeorganigramme()
									+ oCellule.getOListefils().get(1)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_3, oCellule.getOListefils()
									.get(2).getCodeorganigramme()
									+ oCellule.getOListefils().get(2)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_4, oCellule.getOListefils()
									.get(3).getCodeorganigramme()
									+ oCellule.getOListefils().get(3)
											.getLibelle(), strTemplate);
					break;

				case 5:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_6;
					else
						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_6;

					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, oCellule
									.getCodeorganigramme()
									+ oCellule.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_1, oCellule.getOListefils()
									.get(0).getCodeorganigramme()
									+ oCellule.getOListefils().get(0)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_2, oCellule.getOListefils()
									.get(1).getCodeorganigramme()
									+ oCellule.getOListefils().get(1)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_3, oCellule.getOListefils()
									.get(2).getCodeorganigramme()
									+ oCellule.getOListefils().get(2)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_4, oCellule.getOListefils()
									.get(3).getCodeorganigramme()
									+ oCellule.getOListefils().get(3)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_5, oCellule.getOListefils()
									.get(4).getCodeorganigramme()
									+ oCellule.getOListefils().get(4)
											.getLibelle(), strTemplate);
					break;

				case 6:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_7;
					else
						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_7;

					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, oCellule
									.getCodeorganigramme()
									+ oCellule.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_1, oCellule.getOListefils()
									.get(0).getCodeorganigramme()
									+ oCellule.getOListefils().get(0)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_2, oCellule.getOListefils()
									.get(1).getCodeorganigramme()
									+ oCellule.getOListefils().get(1)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_3, oCellule.getOListefils()
									.get(2).getCodeorganigramme()
									+ oCellule.getOListefils().get(2)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_4, oCellule.getOListefils()
									.get(3).getCodeorganigramme()
									+ oCellule.getOListefils().get(3)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_5, oCellule.getOListefils()
									.get(4).getCodeorganigramme()
									+ oCellule.getOListefils().get(4)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_6, oCellule.getOListefils()
									.get(5).getCodeorganigramme()
									+ oCellule.getOListefils().get(5)
											.getLibelle(), strTemplate);
					break;

				case 7:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_8;
					else
						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_8;

					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, oCellule
									.getCodeorganigramme()
									+ oCellule.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_1, oCellule.getOListefils()
									.get(0).getCodeorganigramme()
									+ oCellule.getOListefils().get(0)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_2, oCellule.getOListefils()
									.get(1).getCodeorganigramme()
									+ oCellule.getOListefils().get(1)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_3, oCellule.getOListefils()
									.get(2).getCodeorganigramme()
									+ oCellule.getOListefils().get(2)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_4, oCellule.getOListefils()
									.get(3).getCodeorganigramme()
									+ oCellule.getOListefils().get(3)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_5, oCellule.getOListefils()
									.get(4).getCodeorganigramme()
									+ oCellule.getOListefils().get(4)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_6, oCellule.getOListefils()
									.get(5).getCodeorganigramme()
									+ oCellule.getOListefils().get(5)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_7, oCellule.getOListefils()
									.get(6).getCodeorganigramme()
									+ oCellule.getOListefils().get(6)
											.getLibelle(), strTemplate);
					break;

				case 8:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_9;
					else
						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_9;

					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, oCellule
									.getCodeorganigramme()
									+ oCellule.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_1, oCellule.getOListefils()
									.get(0).getCodeorganigramme()
									+ oCellule.getOListefils().get(0)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_2, oCellule.getOListefils()
									.get(1).getCodeorganigramme()
									+ oCellule.getOListefils().get(1)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_3, oCellule.getOListefils()
									.get(2).getCodeorganigramme()
									+ oCellule.getOListefils().get(2)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_4, oCellule.getOListefils()
									.get(3).getCodeorganigramme()
									+ oCellule.getOListefils().get(3)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_5, oCellule.getOListefils()
									.get(4).getCodeorganigramme()
									+ oCellule.getOListefils().get(4)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_6, oCellule.getOListefils()
									.get(5).getCodeorganigramme()
									+ oCellule.getOListefils().get(5)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_7, oCellule.getOListefils()
									.get(6).getCodeorganigramme()
									+ oCellule.getOListefils().get(6)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_8, oCellule.getOListefils()
									.get(7).getCodeorganigramme()
									+ oCellule.getOListefils().get(7)
											.getLibelle(), strTemplate);
					break;

				case 9:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_10;
					else
						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_10;

					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, oCellule
									.getCodeorganigramme()
									+ oCellule.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_1, oCellule.getOListefils()
									.get(0).getCodeorganigramme()
									+ oCellule.getOListefils().get(0)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_2, oCellule.getOListefils()
									.get(1).getCodeorganigramme()
									+ oCellule.getOListefils().get(1)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_3, oCellule.getOListefils()
									.get(2).getCodeorganigramme()
									+ oCellule.getOListefils().get(2)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_4, oCellule.getOListefils()
									.get(3).getCodeorganigramme()
									+ oCellule.getOListefils().get(3)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_5, oCellule.getOListefils()
									.get(4).getCodeorganigramme()
									+ oCellule.getOListefils().get(4)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_6, oCellule.getOListefils()
									.get(5).getCodeorganigramme()
									+ oCellule.getOListefils().get(5)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_7, oCellule.getOListefils()
									.get(6).getCodeorganigramme()
									+ oCellule.getOListefils().get(6)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_8, oCellule.getOListefils()
									.get(7).getCodeorganigramme()
									+ oCellule.getOListefils().get(7)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_9, oCellule.getOListefils()
									.get(8).getCodeorganigramme()
									+ oCellule.getOListefils().get(8)
											.getLibelle(), strTemplate);
					break;

				case 10:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_11;
					else
						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_11;

					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, oCellule
									.getCodeorganigramme()
									+ oCellule.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_1, oCellule.getOListefils()
									.get(0).getCodeorganigramme()
									+ oCellule.getOListefils().get(0)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_2, oCellule.getOListefils()
									.get(1).getCodeorganigramme()
									+ oCellule.getOListefils().get(1)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_3, oCellule.getOListefils()
									.get(2).getCodeorganigramme()
									+ oCellule.getOListefils().get(2)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_4, oCellule.getOListefils()
									.get(3).getCodeorganigramme()
									+ oCellule.getOListefils().get(3)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_5, oCellule.getOListefils()
									.get(4).getCodeorganigramme()
									+ oCellule.getOListefils().get(4)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_6, oCellule.getOListefils()
									.get(5).getCodeorganigramme()
									+ oCellule.getOListefils().get(5)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_7, oCellule.getOListefils()
									.get(6).getCodeorganigramme()
									+ oCellule.getOListefils().get(6)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_8, oCellule.getOListefils()
									.get(7).getCodeorganigramme()
									+ oCellule.getOListefils().get(7)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_9, oCellule.getOListefils()
									.get(8).getCodeorganigramme()
									+ oCellule.getOListefils().get(8)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_10, oCellule.getOListefils()
									.get(9).getCodeorganigramme()
									+ oCellule.getOListefils().get(9)
											.getLibelle(), strTemplate);
					break;

				case 11:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_12;
					else
						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_12;

					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, oCellule
									.getCodeorganigramme()
									+ oCellule.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_1, oCellule.getOListefils()
									.get(0).getCodeorganigramme()
									+ oCellule.getOListefils().get(0)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_2, oCellule.getOListefils()
									.get(1).getCodeorganigramme()
									+ oCellule.getOListefils().get(1)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_3, oCellule.getOListefils()
									.get(2).getCodeorganigramme()
									+ oCellule.getOListefils().get(2)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_4, oCellule.getOListefils()
									.get(3).getCodeorganigramme()
									+ oCellule.getOListefils().get(3)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_5, oCellule.getOListefils()
									.get(4).getCodeorganigramme()
									+ oCellule.getOListefils().get(4)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_6, oCellule.getOListefils()
									.get(5).getCodeorganigramme()
									+ oCellule.getOListefils().get(5)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_7, oCellule.getOListefils()
									.get(6).getCodeorganigramme()
									+ oCellule.getOListefils().get(6)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_8, oCellule.getOListefils()
									.get(7).getCodeorganigramme()
									+ oCellule.getOListefils().get(7)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_9, oCellule.getOListefils()
									.get(8).getCodeorganigramme()
									+ oCellule.getOListefils().get(8)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_10, oCellule.getOListefils()
									.get(9).getCodeorganigramme()
									+ oCellule.getOListefils().get(9)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_11, oCellule.getOListefils()
									.get(10).getCodeorganigramme()
									+ oCellule.getOListefils().get(10)
											.getLibelle(), strTemplate);
					break;

				case 12:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_13;
					else
						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_13;

					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, oCellule
									.getCodeorganigramme()
									+ oCellule.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_1, oCellule.getOListefils()
									.get(0).getCodeorganigramme()
									+ oCellule.getOListefils().get(0)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_2, oCellule.getOListefils()
									.get(1).getCodeorganigramme()
									+ oCellule.getOListefils().get(1)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_3, oCellule.getOListefils()
									.get(2).getCodeorganigramme()
									+ oCellule.getOListefils().get(2)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_4, oCellule.getOListefils()
									.get(3).getCodeorganigramme()
									+ oCellule.getOListefils().get(3)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_5, oCellule.getOListefils()
									.get(4).getCodeorganigramme()
									+ oCellule.getOListefils().get(4)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_6, oCellule.getOListefils()
									.get(5).getCodeorganigramme()
									+ oCellule.getOListefils().get(5)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_7, oCellule.getOListefils()
									.get(6).getCodeorganigramme()
									+ oCellule.getOListefils().get(6)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_8, oCellule.getOListefils()
									.get(7).getCodeorganigramme()
									+ oCellule.getOListefils().get(7)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_9, oCellule.getOListefils()
									.get(8).getCodeorganigramme()
									+ oCellule.getOListefils().get(8)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_10, oCellule.getOListefils()
									.get(9).getCodeorganigramme()
									+ oCellule.getOListefils().get(9)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_11, oCellule.getOListefils()
									.get(10).getCodeorganigramme()
									+ oCellule.getOListefils().get(10)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_12, oCellule.getOListefils()
									.get(11).getCodeorganigramme()
									+ oCellule.getOListefils().get(11)
											.getLibelle(), strTemplate);
					break;

				case 13:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_14;
					else
						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_14;

					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, oCellule
									.getCodeorganigramme()
									+ oCellule.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_1, oCellule.getOListefils()
									.get(0).getCodeorganigramme()
									+ oCellule.getOListefils().get(0)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_2, oCellule.getOListefils()
									.get(1).getCodeorganigramme()
									+ oCellule.getOListefils().get(1)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_3, oCellule.getOListefils()
									.get(2).getCodeorganigramme()
									+ oCellule.getOListefils().get(2)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_4, oCellule.getOListefils()
									.get(3).getCodeorganigramme()
									+ oCellule.getOListefils().get(3)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_5, oCellule.getOListefils()
									.get(4).getCodeorganigramme()
									+ oCellule.getOListefils().get(4)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_6, oCellule.getOListefils()
									.get(5).getCodeorganigramme()
									+ oCellule.getOListefils().get(5)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_7, oCellule.getOListefils()
									.get(6).getCodeorganigramme()
									+ oCellule.getOListefils().get(6)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_8, oCellule.getOListefils()
									.get(7).getCodeorganigramme()
									+ oCellule.getOListefils().get(7)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_9, oCellule.getOListefils()
									.get(8).getCodeorganigramme()
									+ oCellule.getOListefils().get(8)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_10, oCellule.getOListefils()
									.get(9).getCodeorganigramme()
									+ oCellule.getOListefils().get(9)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_11, oCellule.getOListefils()
									.get(10).getCodeorganigramme()
									+ oCellule.getOListefils().get(10)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_12, oCellule.getOListefils()
									.get(11).getCodeorganigramme()
									+ oCellule.getOListefils().get(11)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_13, oCellule.getOListefils()
									.get(12).getCodeorganigramme()
									+ oCellule.getOListefils().get(12)
											.getLibelle(), strTemplate);
					break;

				case 14:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_15;
					else
						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_15;

					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, oCellule
									.getCodeorganigramme()
									+ oCellule.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_1, oCellule.getOListefils()
									.get(0).getCodeorganigramme()
									+ oCellule.getOListefils().get(0)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_2, oCellule.getOListefils()
									.get(1).getCodeorganigramme()
									+ oCellule.getOListefils().get(1)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_3, oCellule.getOListefils()
									.get(2).getCodeorganigramme()
									+ oCellule.getOListefils().get(2)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_4, oCellule.getOListefils()
									.get(3).getCodeorganigramme()
									+ oCellule.getOListefils().get(3)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_5, oCellule.getOListefils()
									.get(4).getCodeorganigramme()
									+ oCellule.getOListefils().get(4)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_6, oCellule.getOListefils()
									.get(5).getCodeorganigramme()
									+ oCellule.getOListefils().get(5)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_7, oCellule.getOListefils()
									.get(6).getCodeorganigramme()
									+ oCellule.getOListefils().get(6)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_8, oCellule.getOListefils()
									.get(7).getCodeorganigramme()
									+ oCellule.getOListefils().get(7)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_9, oCellule.getOListefils()
									.get(8).getCodeorganigramme()
									+ oCellule.getOListefils().get(8)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_10, oCellule.getOListefils()
									.get(9).getCodeorganigramme()
									+ oCellule.getOListefils().get(9)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_11, oCellule.getOListefils()
									.get(10).getCodeorganigramme()
									+ oCellule.getOListefils().get(10)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_12, oCellule.getOListefils()
									.get(11).getCodeorganigramme()
									+ oCellule.getOListefils().get(11)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_13, oCellule.getOListefils()
									.get(12).getCodeorganigramme()
									+ oCellule.getOListefils().get(12)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_14, oCellule.getOListefils()
									.get(13).getCodeorganigramme()
									+ oCellule.getOListefils().get(13)
											.getLibelle(), strTemplate);
					break;

				case 15:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_16;
					else
						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_16;

					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, oCellule
									.getCodeorganigramme()
									+ oCellule.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_1, oCellule.getOListefils()
									.get(0).getCodeorganigramme()
									+ oCellule.getOListefils().get(0)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_2, oCellule.getOListefils()
									.get(1).getCodeorganigramme()
									+ oCellule.getOListefils().get(1)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_3, oCellule.getOListefils()
									.get(2).getCodeorganigramme()
									+ oCellule.getOListefils().get(2)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_4, oCellule.getOListefils()
									.get(3).getCodeorganigramme()
									+ oCellule.getOListefils().get(3)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_5, oCellule.getOListefils()
									.get(4).getCodeorganigramme()
									+ oCellule.getOListefils().get(4)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_6, oCellule.getOListefils()
									.get(5).getCodeorganigramme()
									+ oCellule.getOListefils().get(5)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_7, oCellule.getOListefils()
									.get(6).getCodeorganigramme()
									+ oCellule.getOListefils().get(6)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_8, oCellule.getOListefils()
									.get(7).getCodeorganigramme()
									+ oCellule.getOListefils().get(7)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_9, oCellule.getOListefils()
									.get(8).getCodeorganigramme()
									+ oCellule.getOListefils().get(8)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_10, oCellule.getOListefils()
									.get(9).getCodeorganigramme()
									+ oCellule.getOListefils().get(9)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_11, oCellule.getOListefils()
									.get(10).getCodeorganigramme()
									+ oCellule.getOListefils().get(10)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_12, oCellule.getOListefils()
									.get(11).getCodeorganigramme()
									+ oCellule.getOListefils().get(11)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_13, oCellule.getOListefils()
									.get(12).getCodeorganigramme()
									+ oCellule.getOListefils().get(12)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_14, oCellule.getOListefils()
									.get(13).getCodeorganigramme()
									+ oCellule.getOListefils().get(13)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_15, oCellule.getOListefils()
									.get(14).getCodeorganigramme()
									+ oCellule.getOListefils().get(14)
											.getLibelle(), strTemplate);
					break;

				case 16:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_17;
					else
						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_17;

					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, oCellule
									.getCodeorganigramme()
									+ oCellule.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_1, oCellule.getOListefils()
									.get(0).getCodeorganigramme()
									+ oCellule.getOListefils().get(0)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_2, oCellule.getOListefils()
									.get(1).getCodeorganigramme()
									+ oCellule.getOListefils().get(1)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_3, oCellule.getOListefils()
									.get(2).getCodeorganigramme()
									+ oCellule.getOListefils().get(2)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_4, oCellule.getOListefils()
									.get(3).getCodeorganigramme()
									+ oCellule.getOListefils().get(3)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_5, oCellule.getOListefils()
									.get(4).getCodeorganigramme()
									+ oCellule.getOListefils().get(4)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_6, oCellule.getOListefils()
									.get(5).getCodeorganigramme()
									+ oCellule.getOListefils().get(5)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_7, oCellule.getOListefils()
									.get(6).getCodeorganigramme()
									+ oCellule.getOListefils().get(6)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_8, oCellule.getOListefils()
									.get(7).getCodeorganigramme()
									+ oCellule.getOListefils().get(7)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_9, oCellule.getOListefils()
									.get(8).getCodeorganigramme()
									+ oCellule.getOListefils().get(8)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_10, oCellule.getOListefils()
									.get(9).getCodeorganigramme()
									+ oCellule.getOListefils().get(9)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_11, oCellule.getOListefils()
									.get(10).getCodeorganigramme()
									+ oCellule.getOListefils().get(10)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_12, oCellule.getOListefils()
									.get(11).getCodeorganigramme()
									+ oCellule.getOListefils().get(11)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_13, oCellule.getOListefils()
									.get(12).getCodeorganigramme()
									+ oCellule.getOListefils().get(12)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_14, oCellule.getOListefils()
									.get(13).getCodeorganigramme()
									+ oCellule.getOListefils().get(13)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_15, oCellule.getOListefils()
									.get(14).getCodeorganigramme()
									+ oCellule.getOListefils().get(14)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_16, oCellule.getOListefils()
									.get(15).getCodeorganigramme()
									+ oCellule.getOListefils().get(15)
											.getLibelle(), strTemplate);
					break;

				case 17:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_18;
					else
						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_18;

					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, oCellule
									.getCodeorganigramme()
									+ oCellule.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_1, oCellule.getOListefils()
									.get(0).getCodeorganigramme()
									+ oCellule.getOListefils().get(0)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_2, oCellule.getOListefils()
									.get(1).getCodeorganigramme()
									+ oCellule.getOListefils().get(1)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_3, oCellule.getOListefils()
									.get(2).getCodeorganigramme()
									+ oCellule.getOListefils().get(2)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_4, oCellule.getOListefils()
									.get(3).getCodeorganigramme()
									+ oCellule.getOListefils().get(3)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_5, oCellule.getOListefils()
									.get(4).getCodeorganigramme()
									+ oCellule.getOListefils().get(4)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_6, oCellule.getOListefils()
									.get(5).getCodeorganigramme()
									+ oCellule.getOListefils().get(5)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_7, oCellule.getOListefils()
									.get(6).getCodeorganigramme()
									+ oCellule.getOListefils().get(6)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_8, oCellule.getOListefils()
									.get(7).getCodeorganigramme()
									+ oCellule.getOListefils().get(7)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_9, oCellule.getOListefils()
									.get(8).getCodeorganigramme()
									+ oCellule.getOListefils().get(8)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_10, oCellule.getOListefils()
									.get(9).getCodeorganigramme()
									+ oCellule.getOListefils().get(9)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_11, oCellule.getOListefils()
									.get(10).getCodeorganigramme()
									+ oCellule.getOListefils().get(10)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_12, oCellule.getOListefils()
									.get(11).getCodeorganigramme()
									+ oCellule.getOListefils().get(11)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_13, oCellule.getOListefils()
									.get(12).getCodeorganigramme()
									+ oCellule.getOListefils().get(12)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_14, oCellule.getOListefils()
									.get(13).getCodeorganigramme()
									+ oCellule.getOListefils().get(13)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_15, oCellule.getOListefils()
									.get(14).getCodeorganigramme()
									+ oCellule.getOListefils().get(14)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_16, oCellule.getOListefils()
									.get(15).getCodeorganigramme()
									+ oCellule.getOListefils().get(15)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_17, oCellule.getOListefils()
									.get(16).getCodeorganigramme()
									+ oCellule.getOListefils().get(16)
											.getLibelle(), strTemplate);
					break;

				case 18:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_19;
					else
						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_19;

					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, oCellule
									.getCodeorganigramme()
									+ oCellule.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_1, oCellule.getOListefils()
									.get(0).getCodeorganigramme()
									+ oCellule.getOListefils().get(0)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_2, oCellule.getOListefils()
									.get(1).getCodeorganigramme()
									+ oCellule.getOListefils().get(1)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_3, oCellule.getOListefils()
									.get(2).getCodeorganigramme()
									+ oCellule.getOListefils().get(2)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_4, oCellule.getOListefils()
									.get(3).getCodeorganigramme()
									+ oCellule.getOListefils().get(3)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_5, oCellule.getOListefils()
									.get(4).getCodeorganigramme()
									+ oCellule.getOListefils().get(4)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_6, oCellule.getOListefils()
									.get(5).getCodeorganigramme()
									+ oCellule.getOListefils().get(5)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_7, oCellule.getOListefils()
									.get(6).getCodeorganigramme()
									+ oCellule.getOListefils().get(6)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_8, oCellule.getOListefils()
									.get(7).getCodeorganigramme()
									+ oCellule.getOListefils().get(7)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_9, oCellule.getOListefils()
									.get(8).getCodeorganigramme()
									+ oCellule.getOListefils().get(8)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_10, oCellule.getOListefils()
									.get(9).getCodeorganigramme()
									+ oCellule.getOListefils().get(9)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_11, oCellule.getOListefils()
									.get(10).getCodeorganigramme()
									+ oCellule.getOListefils().get(10)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_12, oCellule.getOListefils()
									.get(11).getCodeorganigramme()
									+ oCellule.getOListefils().get(11)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_13, oCellule.getOListefils()
									.get(12).getCodeorganigramme()
									+ oCellule.getOListefils().get(12)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_14, oCellule.getOListefils()
									.get(13).getCodeorganigramme()
									+ oCellule.getOListefils().get(13)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_15, oCellule.getOListefils()
									.get(14).getCodeorganigramme()
									+ oCellule.getOListefils().get(14)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_16, oCellule.getOListefils()
									.get(15).getCodeorganigramme()
									+ oCellule.getOListefils().get(15)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_17, oCellule.getOListefils()
									.get(16).getCodeorganigramme()
									+ oCellule.getOListefils().get(16)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_18, oCellule.getOListefils()
									.get(17).getCodeorganigramme()
									+ oCellule.getOListefils().get(17)
											.getLibelle(), strTemplate);
					break;

				case 19:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_20;
					else
						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_20;

					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, oCellule
									.getCodeorganigramme()
									+ oCellule.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_1, oCellule.getOListefils()
									.get(0).getCodeorganigramme()
									+ oCellule.getOListefils().get(0)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_2, oCellule.getOListefils()
									.get(1).getCodeorganigramme()
									+ oCellule.getOListefils().get(1)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_3, oCellule.getOListefils()
									.get(2).getCodeorganigramme()
									+ oCellule.getOListefils().get(2)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_4, oCellule.getOListefils()
									.get(3).getCodeorganigramme()
									+ oCellule.getOListefils().get(3)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_5, oCellule.getOListefils()
									.get(4).getCodeorganigramme()
									+ oCellule.getOListefils().get(4)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_6, oCellule.getOListefils()
									.get(5).getCodeorganigramme()
									+ oCellule.getOListefils().get(5)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_7, oCellule.getOListefils()
									.get(6).getCodeorganigramme()
									+ oCellule.getOListefils().get(6)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_8, oCellule.getOListefils()
									.get(7).getCodeorganigramme()
									+ oCellule.getOListefils().get(7)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_9, oCellule.getOListefils()
									.get(8).getCodeorganigramme()
									+ oCellule.getOListefils().get(8)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_10, oCellule.getOListefils()
									.get(9).getCodeorganigramme()
									+ oCellule.getOListefils().get(9)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_11, oCellule.getOListefils()
									.get(10).getCodeorganigramme()
									+ oCellule.getOListefils().get(10)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_12, oCellule.getOListefils()
									.get(11).getCodeorganigramme()
									+ oCellule.getOListefils().get(11)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_13, oCellule.getOListefils()
									.get(12).getCodeorganigramme()
									+ oCellule.getOListefils().get(12)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_14, oCellule.getOListefils()
									.get(13).getCodeorganigramme()
									+ oCellule.getOListefils().get(13)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_15, oCellule.getOListefils()
									.get(14).getCodeorganigramme()
									+ oCellule.getOListefils().get(14)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_16, oCellule.getOListefils()
									.get(15).getCodeorganigramme()
									+ oCellule.getOListefils().get(15)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_17, oCellule.getOListefils()
									.get(16).getCodeorganigramme()
									+ oCellule.getOListefils().get(16)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_18, oCellule.getOListefils()
									.get(17).getCodeorganigramme()
									+ oCellule.getOListefils().get(17)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_19, oCellule.getOListefils()
									.get(18).getCodeorganigramme()
									+ oCellule.getOListefils().get(18)
											.getLibelle(), strTemplate);
					break;

				case 20:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_21;
					else
						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_21;

					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, oCellule
									.getCodeorganigramme()
									+ oCellule.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_1, oCellule.getOListefils()
									.get(0).getCodeorganigramme()
									+ oCellule.getOListefils().get(0)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_2, oCellule.getOListefils()
									.get(1).getCodeorganigramme()
									+ oCellule.getOListefils().get(1)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_3, oCellule.getOListefils()
									.get(2).getCodeorganigramme()
									+ oCellule.getOListefils().get(2)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_4, oCellule.getOListefils()
									.get(3).getCodeorganigramme()
									+ oCellule.getOListefils().get(3)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_5, oCellule.getOListefils()
									.get(4).getCodeorganigramme()
									+ oCellule.getOListefils().get(4)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_6, oCellule.getOListefils()
									.get(5).getCodeorganigramme()
									+ oCellule.getOListefils().get(5)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_7, oCellule.getOListefils()
									.get(6).getCodeorganigramme()
									+ oCellule.getOListefils().get(6)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_8, oCellule.getOListefils()
									.get(7).getCodeorganigramme()
									+ oCellule.getOListefils().get(7)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_9, oCellule.getOListefils()
									.get(8).getCodeorganigramme()
									+ oCellule.getOListefils().get(8)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_10, oCellule.getOListefils()
									.get(9).getCodeorganigramme()
									+ oCellule.getOListefils().get(9)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_11, oCellule.getOListefils()
									.get(10).getCodeorganigramme()
									+ oCellule.getOListefils().get(10)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_12, oCellule.getOListefils()
									.get(11).getCodeorganigramme()
									+ oCellule.getOListefils().get(11)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_13, oCellule.getOListefils()
									.get(12).getCodeorganigramme()
									+ oCellule.getOListefils().get(12)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_14, oCellule.getOListefils()
									.get(13).getCodeorganigramme()
									+ oCellule.getOListefils().get(13)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_15, oCellule.getOListefils()
									.get(14).getCodeorganigramme()
									+ oCellule.getOListefils().get(14)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_16, oCellule.getOListefils()
									.get(15).getCodeorganigramme()
									+ oCellule.getOListefils().get(15)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_17, oCellule.getOListefils()
									.get(16).getCodeorganigramme()
									+ oCellule.getOListefils().get(16)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_18, oCellule.getOListefils()
									.get(17).getCodeorganigramme()
									+ oCellule.getOListefils().get(17)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_19, oCellule.getOListefils()
									.get(18).getCodeorganigramme()
									+ oCellule.getOListefils().get(18)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_20, oCellule.getOListefils()
									.get(19).getCodeorganigramme()
									+ oCellule.getOListefils().get(19)
											.getLibelle(), strTemplate);
					break;

				default:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_21;
					else
						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_21;

					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, oCellule
									.getCodeorganigramme()
									+ oCellule.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_1, oCellule.getOListefils()
									.get(0).getCodeorganigramme()
									+ oCellule.getOListefils().get(0)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_2, oCellule.getOListefils()
									.get(1).getCodeorganigramme()
									+ oCellule.getOListefils().get(1)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_3, oCellule.getOListefils()
									.get(2).getCodeorganigramme()
									+ oCellule.getOListefils().get(2)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_4, oCellule.getOListefils()
									.get(3).getCodeorganigramme()
									+ oCellule.getOListefils().get(3)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_5, oCellule.getOListefils()
									.get(4).getCodeorganigramme()
									+ oCellule.getOListefils().get(4)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_6, oCellule.getOListefils()
									.get(5).getCodeorganigramme()
									+ oCellule.getOListefils().get(5)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_7, oCellule.getOListefils()
									.get(6).getCodeorganigramme()
									+ oCellule.getOListefils().get(6)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_8, oCellule.getOListefils()
									.get(7).getCodeorganigramme()
									+ oCellule.getOListefils().get(7)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_9, oCellule.getOListefils()
									.get(8).getCodeorganigramme()
									+ oCellule.getOListefils().get(8)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_10, oCellule.getOListefils()
									.get(9).getCodeorganigramme()
									+ oCellule.getOListefils().get(9)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_11, oCellule.getOListefils()
									.get(10).getCodeorganigramme()
									+ oCellule.getOListefils().get(10)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_12, oCellule.getOListefils()
									.get(11).getCodeorganigramme()
									+ oCellule.getOListefils().get(11)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_13, oCellule.getOListefils()
									.get(12).getCodeorganigramme()
									+ oCellule.getOListefils().get(12)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_14, oCellule.getOListefils()
									.get(13).getCodeorganigramme()
									+ oCellule.getOListefils().get(13)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_15, oCellule.getOListefils()
									.get(14).getCodeorganigramme()
									+ oCellule.getOListefils().get(14)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_16, oCellule.getOListefils()
									.get(15).getCodeorganigramme()
									+ oCellule.getOListefils().get(15)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_17, oCellule.getOListefils()
									.get(16).getCodeorganigramme()
									+ oCellule.getOListefils().get(16)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_18, oCellule.getOListefils()
									.get(17).getCodeorganigramme()
									+ oCellule.getOListefils().get(17)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_19, oCellule.getOListefils()
									.get(18).getCodeorganigramme()
									+ oCellule.getOListefils().get(18)
											.getLibelle(), strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_20, oCellule.getOListefils()
									.get(19).getCodeorganigramme()
									+ oCellule.getOListefils().get(19)
											.getLibelle(), strTemplate);
					break;
				}
			}
			return strTemplate;
		} catch (Exception e) {
			return strTemplate;
		}
	}

//	 Get template matching current cell.
//	public static String getTemplateThread(ClsCelluleThread oCellule) {
//		String strTemplate = "";
//		try {
//			if (oCellule.getOListefils2().size() >= 0) {
//				switch (oCellule.getOListefils2().size()) {
//				case 0:
//					strTemplate = ClsTemplate.STR_TEMPLATE_1;
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_PERE, oCellule
//									.getCodeorganigramme()
//									+ oCellule.getLibelle(), strTemplate);
//					break;
//
//				case 1:
//					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
//						strTemplate = ClsTemplate.STR_TEMPLATE_2;
//					else
//						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_2;
//
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_PERE, oCellule
//									.getCodeorganigramme()
//									+ oCellule.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_1, oCellule.getOListefils2()
//									.get(0).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(0)
//											.getLibelle(), strTemplate);
//					// ParameterUtil.println(strTemplate);
//					break;
//
//				case 2:
//					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
//						strTemplate = ClsTemplate.STR_TEMPLATE_3;
//					else
//						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_3;
//
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_PERE, oCellule
//									.getCodeorganigramme()
//									+ oCellule.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_1, oCellule.getOListefils2()
//									.get(0).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(0)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_2, oCellule.getOListefils2()
//									.get(1).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(1)
//											.getLibelle(), strTemplate);
//					break;
//
//				case 3:
//					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
//						strTemplate = ClsTemplate.STR_TEMPLATE_4;
//					else
//						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_4;
//
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_PERE, oCellule
//									.getCodeorganigramme()
//									+ oCellule.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_1, oCellule.getOListefils2()
//									.get(0).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(0)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_2, oCellule.getOListefils2()
//									.get(1).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(1)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_3, oCellule.getOListefils2()
//									.get(2).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(2)
//											.getLibelle(), strTemplate);
//					break;
//
//				case 4:
//					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
//						strTemplate = ClsTemplate.STR_TEMPLATE_5;
//					else
//						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_5;
//
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_PERE, oCellule
//									.getCodeorganigramme()
//									+ oCellule.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_1, oCellule.getOListefils2()
//									.get(0).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(0)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_2, oCellule.getOListefils2()
//									.get(1).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(1)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_3, oCellule.getOListefils2()
//									.get(2).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(2)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_4, oCellule.getOListefils2()
//									.get(3).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(3)
//											.getLibelle(), strTemplate);
//					break;
//
//				case 5:
//					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
//						strTemplate = ClsTemplate.STR_TEMPLATE_6;
//					else
//						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_6;
//
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_PERE, oCellule
//									.getCodeorganigramme()
//									+ oCellule.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_1, oCellule.getOListefils2()
//									.get(0).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(0)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_2, oCellule.getOListefils2()
//									.get(1).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(1)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_3, oCellule.getOListefils2()
//									.get(2).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(2)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_4, oCellule.getOListefils2()
//									.get(3).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(3)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_5, oCellule.getOListefils2()
//									.get(4).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(4)
//											.getLibelle(), strTemplate);
//					break;
//
//				case 6:
//					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
//						strTemplate = ClsTemplate.STR_TEMPLATE_7;
//					else
//						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_7;
//
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_PERE, oCellule
//									.getCodeorganigramme()
//									+ oCellule.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_1, oCellule.getOListefils2()
//									.get(0).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(0)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_2, oCellule.getOListefils2()
//									.get(1).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(1)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_3, oCellule.getOListefils2()
//									.get(2).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(2)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_4, oCellule.getOListefils2()
//									.get(3).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(3)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_5, oCellule.getOListefils2()
//									.get(4).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(4)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_6, oCellule.getOListefils2()
//									.get(5).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(5)
//											.getLibelle(), strTemplate);
//					break;
//
//				case 7:
//					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
//						strTemplate = ClsTemplate.STR_TEMPLATE_8;
//					else
//						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_8;
//
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_PERE, oCellule
//									.getCodeorganigramme()
//									+ oCellule.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_1, oCellule.getOListefils2()
//									.get(0).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(0)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_2, oCellule.getOListefils2()
//									.get(1).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(1)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_3, oCellule.getOListefils2()
//									.get(2).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(2)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_4, oCellule.getOListefils2()
//									.get(3).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(3)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_5, oCellule.getOListefils2()
//									.get(4).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(4)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_6, oCellule.getOListefils2()
//									.get(5).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(5)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_7, oCellule.getOListefils2()
//									.get(6).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(6)
//											.getLibelle(), strTemplate);
//					break;
//
//				case 8:
//					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
//						strTemplate = ClsTemplate.STR_TEMPLATE_9;
//					else
//						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_9;
//
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_PERE, oCellule
//									.getCodeorganigramme()
//									+ oCellule.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_1, oCellule.getOListefils2()
//									.get(0).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(0)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_2, oCellule.getOListefils2()
//									.get(1).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(1)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_3, oCellule.getOListefils2()
//									.get(2).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(2)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_4, oCellule.getOListefils2()
//									.get(3).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(3)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_5, oCellule.getOListefils2()
//									.get(4).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(4)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_6, oCellule.getOListefils2()
//									.get(5).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(5)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_7, oCellule.getOListefils2()
//									.get(6).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(6)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_8, oCellule.getOListefils2()
//									.get(7).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(7)
//											.getLibelle(), strTemplate);
//					break;
//
//				case 9:
//					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
//						strTemplate = ClsTemplate.STR_TEMPLATE_10;
//					else
//						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_10;
//
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_PERE, oCellule
//									.getCodeorganigramme()
//									+ oCellule.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_1, oCellule.getOListefils2()
//									.get(0).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(0)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_2, oCellule.getOListefils2()
//									.get(1).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(1)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_3, oCellule.getOListefils2()
//									.get(2).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(2)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_4, oCellule.getOListefils2()
//									.get(3).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(3)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_5, oCellule.getOListefils2()
//									.get(4).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(4)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_6, oCellule.getOListefils2()
//									.get(5).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(5)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_7, oCellule.getOListefils2()
//									.get(6).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(6)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_8, oCellule.getOListefils2()
//									.get(7).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(7)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_9, oCellule.getOListefils2()
//									.get(8).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(8)
//											.getLibelle(), strTemplate);
//					break;
//
//				case 10:
//					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
//						strTemplate = ClsTemplate.STR_TEMPLATE_11;
//					else
//						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_11;
//
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_PERE, oCellule
//									.getCodeorganigramme()
//									+ oCellule.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_1, oCellule.getOListefils2()
//									.get(0).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(0)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_2, oCellule.getOListefils2()
//									.get(1).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(1)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_3, oCellule.getOListefils2()
//									.get(2).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(2)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_4, oCellule.getOListefils2()
//									.get(3).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(3)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_5, oCellule.getOListefils2()
//									.get(4).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(4)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_6, oCellule.getOListefils2()
//									.get(5).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(5)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_7, oCellule.getOListefils2()
//									.get(6).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(6)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_8, oCellule.getOListefils2()
//									.get(7).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(7)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_9, oCellule.getOListefils2()
//									.get(8).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(8)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_10, oCellule.getOListefils2()
//									.get(9).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(9)
//											.getLibelle(), strTemplate);
//					break;
//
//				case 11:
//					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
//						strTemplate = ClsTemplate.STR_TEMPLATE_12;
//					else
//						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_12;
//
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_PERE, oCellule
//									.getCodeorganigramme()
//									+ oCellule.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_1, oCellule.getOListefils2()
//									.get(0).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(0)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_2, oCellule.getOListefils2()
//									.get(1).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(1)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_3, oCellule.getOListefils2()
//									.get(2).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(2)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_4, oCellule.getOListefils2()
//									.get(3).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(3)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_5, oCellule.getOListefils2()
//									.get(4).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(4)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_6, oCellule.getOListefils2()
//									.get(5).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(5)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_7, oCellule.getOListefils2()
//									.get(6).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(6)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_8, oCellule.getOListefils2()
//									.get(7).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(7)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_9, oCellule.getOListefils2()
//									.get(8).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(8)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_10, oCellule.getOListefils2()
//									.get(9).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(9)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_11, oCellule.getOListefils2()
//									.get(10).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(10)
//											.getLibelle(), strTemplate);
//					break;
//
//				case 12:
//					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
//						strTemplate = ClsTemplate.STR_TEMPLATE_13;
//					else
//						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_13;
//
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_PERE, oCellule
//									.getCodeorganigramme()
//									+ oCellule.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_1, oCellule.getOListefils2()
//									.get(0).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(0)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_2, oCellule.getOListefils2()
//									.get(1).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(1)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_3, oCellule.getOListefils2()
//									.get(2).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(2)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_4, oCellule.getOListefils2()
//									.get(3).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(3)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_5, oCellule.getOListefils2()
//									.get(4).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(4)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_6, oCellule.getOListefils2()
//									.get(5).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(5)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_7, oCellule.getOListefils2()
//									.get(6).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(6)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_8, oCellule.getOListefils2()
//									.get(7).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(7)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_9, oCellule.getOListefils2()
//									.get(8).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(8)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_10, oCellule.getOListefils2()
//									.get(9).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(9)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_11, oCellule.getOListefils2()
//									.get(10).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(10)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_12, oCellule.getOListefils2()
//									.get(11).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(11)
//											.getLibelle(), strTemplate);
//					break;
//
//				case 13:
//					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
//						strTemplate = ClsTemplate.STR_TEMPLATE_14;
//					else
//						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_14;
//
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_PERE, oCellule
//									.getCodeorganigramme()
//									+ oCellule.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_1, oCellule.getOListefils2()
//									.get(0).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(0)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_2, oCellule.getOListefils2()
//									.get(1).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(1)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_3, oCellule.getOListefils2()
//									.get(2).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(2)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_4, oCellule.getOListefils2()
//									.get(3).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(3)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_5, oCellule.getOListefils2()
//									.get(4).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(4)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_6, oCellule.getOListefils2()
//									.get(5).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(5)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_7, oCellule.getOListefils2()
//									.get(6).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(6)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_8, oCellule.getOListefils2()
//									.get(7).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(7)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_9, oCellule.getOListefils2()
//									.get(8).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(8)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_10, oCellule.getOListefils2()
//									.get(9).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(9)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_11, oCellule.getOListefils2()
//									.get(10).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(10)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_12, oCellule.getOListefils2()
//									.get(11).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(11)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_13, oCellule.getOListefils2()
//									.get(12).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(12)
//											.getLibelle(), strTemplate);
//					break;
//
//				case 14:
//					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
//						strTemplate = ClsTemplate.STR_TEMPLATE_15;
//					else
//						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_15;
//
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_PERE, oCellule
//									.getCodeorganigramme()
//									+ oCellule.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_1, oCellule.getOListefils2()
//									.get(0).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(0)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_2, oCellule.getOListefils2()
//									.get(1).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(1)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_3, oCellule.getOListefils2()
//									.get(2).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(2)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_4, oCellule.getOListefils2()
//									.get(3).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(3)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_5, oCellule.getOListefils2()
//									.get(4).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(4)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_6, oCellule.getOListefils2()
//									.get(5).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(5)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_7, oCellule.getOListefils2()
//									.get(6).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(6)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_8, oCellule.getOListefils2()
//									.get(7).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(7)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_9, oCellule.getOListefils2()
//									.get(8).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(8)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_10, oCellule.getOListefils2()
//									.get(9).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(9)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_11, oCellule.getOListefils2()
//									.get(10).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(10)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_12, oCellule.getOListefils2()
//									.get(11).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(11)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_13, oCellule.getOListefils2()
//									.get(12).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(12)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_14, oCellule.getOListefils2()
//									.get(13).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(13)
//											.getLibelle(), strTemplate);
//					break;
//
//				case 15:
//					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
//						strTemplate = ClsTemplate.STR_TEMPLATE_16;
//					else
//						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_16;
//
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_PERE, oCellule
//									.getCodeorganigramme()
//									+ oCellule.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_1, oCellule.getOListefils2()
//									.get(0).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(0)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_2, oCellule.getOListefils2()
//									.get(1).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(1)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_3, oCellule.getOListefils2()
//									.get(2).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(2)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_4, oCellule.getOListefils2()
//									.get(3).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(3)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_5, oCellule.getOListefils2()
//									.get(4).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(4)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_6, oCellule.getOListefils2()
//									.get(5).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(5)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_7, oCellule.getOListefils2()
//									.get(6).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(6)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_8, oCellule.getOListefils2()
//									.get(7).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(7)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_9, oCellule.getOListefils2()
//									.get(8).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(8)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_10, oCellule.getOListefils2()
//									.get(9).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(9)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_11, oCellule.getOListefils2()
//									.get(10).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(10)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_12, oCellule.getOListefils2()
//									.get(11).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(11)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_13, oCellule.getOListefils2()
//									.get(12).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(12)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_14, oCellule.getOListefils2()
//									.get(13).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(13)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_15, oCellule.getOListefils2()
//									.get(14).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(14)
//											.getLibelle(), strTemplate);
//					break;
//
//				case 16:
//					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
//						strTemplate = ClsTemplate.STR_TEMPLATE_17;
//					else
//						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_17;
//
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_PERE, oCellule
//									.getCodeorganigramme()
//									+ oCellule.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_1, oCellule.getOListefils2()
//									.get(0).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(0)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_2, oCellule.getOListefils2()
//									.get(1).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(1)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_3, oCellule.getOListefils2()
//									.get(2).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(2)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_4, oCellule.getOListefils2()
//									.get(3).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(3)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_5, oCellule.getOListefils2()
//									.get(4).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(4)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_6, oCellule.getOListefils2()
//									.get(5).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(5)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_7, oCellule.getOListefils2()
//									.get(6).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(6)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_8, oCellule.getOListefils2()
//									.get(7).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(7)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_9, oCellule.getOListefils2()
//									.get(8).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(8)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_10, oCellule.getOListefils2()
//									.get(9).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(9)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_11, oCellule.getOListefils2()
//									.get(10).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(10)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_12, oCellule.getOListefils2()
//									.get(11).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(11)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_13, oCellule.getOListefils2()
//									.get(12).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(12)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_14, oCellule.getOListefils2()
//									.get(13).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(13)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_15, oCellule.getOListefils2()
//									.get(14).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(14)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_16, oCellule.getOListefils2()
//									.get(15).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(15)
//											.getLibelle(), strTemplate);
//					break;
//
//				case 17:
//					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
//						strTemplate = ClsTemplate.STR_TEMPLATE_18;
//					else
//						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_18;
//
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_PERE, oCellule
//									.getCodeorganigramme()
//									+ oCellule.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_1, oCellule.getOListefils2()
//									.get(0).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(0)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_2, oCellule.getOListefils2()
//									.get(1).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(1)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_3, oCellule.getOListefils2()
//									.get(2).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(2)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_4, oCellule.getOListefils2()
//									.get(3).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(3)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_5, oCellule.getOListefils2()
//									.get(4).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(4)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_6, oCellule.getOListefils2()
//									.get(5).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(5)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_7, oCellule.getOListefils2()
//									.get(6).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(6)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_8, oCellule.getOListefils2()
//									.get(7).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(7)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_9, oCellule.getOListefils2()
//									.get(8).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(8)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_10, oCellule.getOListefils2()
//									.get(9).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(9)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_11, oCellule.getOListefils2()
//									.get(10).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(10)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_12, oCellule.getOListefils2()
//									.get(11).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(11)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_13, oCellule.getOListefils2()
//									.get(12).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(12)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_14, oCellule.getOListefils2()
//									.get(13).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(13)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_15, oCellule.getOListefils2()
//									.get(14).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(14)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_16, oCellule.getOListefils2()
//									.get(15).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(15)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_17, oCellule.getOListefils2()
//									.get(16).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(16)
//											.getLibelle(), strTemplate);
//					break;
//
//				case 18:
//					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
//						strTemplate = ClsTemplate.STR_TEMPLATE_19;
//					else
//						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_19;
//
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_PERE, oCellule
//									.getCodeorganigramme()
//									+ oCellule.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_1, oCellule.getOListefils2()
//									.get(0).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(0)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_2, oCellule.getOListefils2()
//									.get(1).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(1)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_3, oCellule.getOListefils2()
//									.get(2).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(2)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_4, oCellule.getOListefils2()
//									.get(3).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(3)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_5, oCellule.getOListefils2()
//									.get(4).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(4)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_6, oCellule.getOListefils2()
//									.get(5).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(5)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_7, oCellule.getOListefils2()
//									.get(6).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(6)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_8, oCellule.getOListefils2()
//									.get(7).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(7)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_9, oCellule.getOListefils2()
//									.get(8).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(8)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_10, oCellule.getOListefils2()
//									.get(9).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(9)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_11, oCellule.getOListefils2()
//									.get(10).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(10)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_12, oCellule.getOListefils2()
//									.get(11).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(11)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_13, oCellule.getOListefils2()
//									.get(12).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(12)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_14, oCellule.getOListefils2()
//									.get(13).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(13)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_15, oCellule.getOListefils2()
//									.get(14).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(14)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_16, oCellule.getOListefils2()
//									.get(15).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(15)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_17, oCellule.getOListefils2()
//									.get(16).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(16)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_18, oCellule.getOListefils2()
//									.get(17).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(17)
//											.getLibelle(), strTemplate);
//					break;
//
//				case 19:
//					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
//						strTemplate = ClsTemplate.STR_TEMPLATE_20;
//					else
//						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_20;
//
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_PERE, oCellule
//									.getCodeorganigramme()
//									+ oCellule.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_1, oCellule.getOListefils2()
//									.get(0).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(0)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_2, oCellule.getOListefils2()
//									.get(1).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(1)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_3, oCellule.getOListefils2()
//									.get(2).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(2)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_4, oCellule.getOListefils2()
//									.get(3).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(3)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_5, oCellule.getOListefils2()
//									.get(4).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(4)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_6, oCellule.getOListefils2()
//									.get(5).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(5)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_7, oCellule.getOListefils2()
//									.get(6).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(6)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_8, oCellule.getOListefils2()
//									.get(7).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(7)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_9, oCellule.getOListefils2()
//									.get(8).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(8)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_10, oCellule.getOListefils2()
//									.get(9).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(9)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_11, oCellule.getOListefils2()
//									.get(10).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(10)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_12, oCellule.getOListefils2()
//									.get(11).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(11)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_13, oCellule.getOListefils2()
//									.get(12).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(12)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_14, oCellule.getOListefils2()
//									.get(13).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(13)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_15, oCellule.getOListefils2()
//									.get(14).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(14)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_16, oCellule.getOListefils2()
//									.get(15).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(15)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_17, oCellule.getOListefils2()
//									.get(16).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(16)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_18, oCellule.getOListefils2()
//									.get(17).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(17)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_19, oCellule.getOListefils2()
//									.get(18).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(18)
//											.getLibelle(), strTemplate);
//					break;
//
//				case 20:
//					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
//						strTemplate = ClsTemplate.STR_TEMPLATE_21;
//					else
//						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_21;
//
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_PERE, oCellule
//									.getCodeorganigramme()
//									+ oCellule.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_1, oCellule.getOListefils2()
//									.get(0).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(0)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_2, oCellule.getOListefils2()
//									.get(1).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(1)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_3, oCellule.getOListefils2()
//									.get(2).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(2)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_4, oCellule.getOListefils2()
//									.get(3).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(3)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_5, oCellule.getOListefils2()
//									.get(4).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(4)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_6, oCellule.getOListefils2()
//									.get(5).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(5)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_7, oCellule.getOListefils2()
//									.get(6).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(6)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_8, oCellule.getOListefils2()
//									.get(7).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(7)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_9, oCellule.getOListefils2()
//									.get(8).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(8)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_10, oCellule.getOListefils2()
//									.get(9).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(9)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_11, oCellule.getOListefils2()
//									.get(10).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(10)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_12, oCellule.getOListefils2()
//									.get(11).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(11)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_13, oCellule.getOListefils2()
//									.get(12).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(12)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_14, oCellule.getOListefils2()
//									.get(13).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(13)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_15, oCellule.getOListefils2()
//									.get(14).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(14)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_16, oCellule.getOListefils2()
//									.get(15).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(15)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_17, oCellule.getOListefils2()
//									.get(16).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(16)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_18, oCellule.getOListefils2()
//									.get(17).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(17)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_19, oCellule.getOListefils2()
//									.get(18).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(18)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_20, oCellule.getOListefils2()
//									.get(19).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(19)
//											.getLibelle(), strTemplate);
//					break;
//
//				default:
//					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
//						strTemplate = ClsTemplate.STR_TEMPLATE_21;
//					else
//						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_21;
//
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_PERE, oCellule
//									.getCodeorganigramme()
//									+ oCellule.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_1, oCellule.getOListefils2()
//									.get(0).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(0)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_2, oCellule.getOListefils2()
//									.get(1).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(1)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_3, oCellule.getOListefils2()
//									.get(2).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(2)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_4, oCellule.getOListefils2()
//									.get(3).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(3)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_5, oCellule.getOListefils2()
//									.get(4).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(4)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_6, oCellule.getOListefils2()
//									.get(5).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(5)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_7, oCellule.getOListefils2()
//									.get(6).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(6)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_8, oCellule.getOListefils2()
//									.get(7).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(7)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_9, oCellule.getOListefils2()
//									.get(8).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(8)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_10, oCellule.getOListefils2()
//									.get(9).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(9)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_11, oCellule.getOListefils2()
//									.get(10).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(10)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_12, oCellule.getOListefils2()
//									.get(11).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(11)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_13, oCellule.getOListefils2()
//									.get(12).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(12)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_14, oCellule.getOListefils2()
//									.get(13).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(13)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_15, oCellule.getOListefils2()
//									.get(14).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(14)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_16, oCellule.getOListefils2()
//									.get(15).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(15)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_17, oCellule.getOListefils2()
//									.get(16).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(16)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_18, oCellule.getOListefils2()
//									.get(17).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(17)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_19, oCellule.getOListefils2()
//									.get(18).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(18)
//											.getLibelle(), strTemplate);
//					strTemplate = ClsTemplate.replaceString(
//							ClsTemplate.STR_FILS_20, oCellule.getOListefils2()
//									.get(19).getCodeorganigramme()
//									+ oCellule.getOListefils2().get(19)
//											.getLibelle(), strTemplate);
//					break;
//				}
//			}
//			return strTemplate;
//		} catch (Exception e) {
//			return strTemplate;
//		}
//	}
	
	// Get template matching current cell.
	public static String getTemplateThread(ClsCelluleThread oCellule) {
		String strTemplate = "";
		try {
			if (oCellule.getOListefils2().size() >= 0) {
				switch (oCellule.getOListefils2().size()) {
				case 0:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_1;
					else
					   strTemplate = ClsTemplate.STR_TEMPLATE_LIST_1;
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, STR_SPECIFIC_CHAR + oCellule
									.getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					break;

				case 1:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_2;
					else
						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_2;

					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, STR_SPECIFIC_CHAR + oCellule
									.getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_1, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(0).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					// ParameterUtil.println(strTemplate);
					break;

				case 2:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_3;
					else
						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_3;

					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, STR_SPECIFIC_CHAR + oCellule
									.getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_1, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(0).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_2, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(1).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					break;

				case 3:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_4;
					else
						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_4;

					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, STR_SPECIFIC_CHAR + oCellule
									.getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_1, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(0).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_2, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(1).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_3, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(2).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					break;

				case 4:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_5;
					else
						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_5;

					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, STR_SPECIFIC_CHAR + oCellule
									.getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_1, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(0).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_2, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(1).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_3, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(2).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_4, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(3).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					break;

				case 5:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_6;
					else
						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_6;

					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, STR_SPECIFIC_CHAR + oCellule
									.getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_1, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(0).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_2, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(1).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_3, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(2).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_4, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(3).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_5, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(4).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					break;

				case 6:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_7;
					else
						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_7;

					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, STR_SPECIFIC_CHAR + oCellule
									.getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_1, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(0).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_2, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(1).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_3, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(2).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_4, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(3).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_5, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(4).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_6, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(5).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					break;

				case 7:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_8;
					else
						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_8;

					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, STR_SPECIFIC_CHAR + oCellule
									.getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_1, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(0).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_2, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(1).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_3, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(2).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_4, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(3).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_5, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(4).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_6, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(5).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_7, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(6).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					break;

				case 8:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_9;
					else
						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_9;

					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, STR_SPECIFIC_CHAR + oCellule
									.getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_1, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(0).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_2, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(1).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_3, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(2).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_4, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(3).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_5, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(4).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_6, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(5).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_7, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(6).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_8, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(7).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					break;

				case 9:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_10;
					else
						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_10;

					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, STR_SPECIFIC_CHAR + oCellule
									.getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_1, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(0).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_2, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(1).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_3, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(2).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_4, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(3).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_5, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(4).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_6, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(5).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_7, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(6).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_8, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(7).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_9, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(8).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					break;

				case 10:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_11;
					else
						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_11;

					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, STR_SPECIFIC_CHAR + oCellule
									.getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_1, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(0).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_2, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(1).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_3, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(2).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_4, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(3).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_5, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(4).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_6, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(5).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_7, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(6).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_8, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(7).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_9, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(8).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_10, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(9).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					break;

				case 11:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_12;
					else
						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_12;

					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, STR_SPECIFIC_CHAR + oCellule
									.getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_1, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(0).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_2, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(1).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_3, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(2).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_4, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(3).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_5, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(4).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_6, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(5).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_7, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(6).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_8, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(7).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_9, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(8).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_10, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(9).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_11, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(10).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					break;

				case 12:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_13;
					else
						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_13;

					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, STR_SPECIFIC_CHAR + oCellule
									.getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_1, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(0).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_2, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(1).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_3, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(2).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_4, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(3).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_5, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(4).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_6, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(5).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_7, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(6).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_8, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(7).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_9, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(8).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_10, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(9).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_11, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(10).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_12, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(11).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					break;

				case 13:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_14;
					else
						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_14;

					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, STR_SPECIFIC_CHAR + oCellule
									.getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_1, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(0).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_2, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(1).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_3, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(2).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_4, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(3).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_5, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(4).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_6, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(5).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_7, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(6).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_8, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(7).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_9, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(8).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_10, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(9).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_11, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(10).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_12, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(11).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_13, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(12).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					break;

				case 14:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_15;
					else
						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_15;

					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, STR_SPECIFIC_CHAR + oCellule
									.getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_1, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(0).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_2, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(1).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_3, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(2).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_4, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(3).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_5, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(4).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_6, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(5).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_7, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(6).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_8, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(7).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_9, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(8).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_10, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(9).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_11, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(10).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_12, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(11).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_13, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(12).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_14, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(13).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					break;

				case 15:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_16;
					else
						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_16;

					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, STR_SPECIFIC_CHAR + oCellule
									.getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_1, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(0).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_2, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(1).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_3, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(2).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_4, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(3).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_5, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(4).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_6, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(5).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_7, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(6).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_8, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(7).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_9, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(8).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_10, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(9).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_11, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(10).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_12, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(11).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_13, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(12).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_14, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(13).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_15, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(14).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					break;

				case 16:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_17;
					else
						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_17;

					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, STR_SPECIFIC_CHAR + oCellule
									.getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_1, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(0).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_2, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(1).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_3, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(2).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_4, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(3).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_5, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(4).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_6, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(5).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_7, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(6).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_8, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(7).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_9, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(8).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_10, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(9).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_11, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(10).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_12, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(11).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_13, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(12).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_14, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(13).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_15, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(14).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_16, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(15).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					break;

				case 17:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_18;
					else
						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_18;

					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, STR_SPECIFIC_CHAR + oCellule
									.getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_1, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(0).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_2, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(1).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_3, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(2).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_4, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(3).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_5, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(4).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_6, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(5).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_7, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(6).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_8, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(7).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_9, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(8).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_10, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(9).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_11, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(10).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_12, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(11).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_13, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(12).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_14, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(13).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_15, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(14).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_16, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(15).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_17, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(16).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					break;

				case 18:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_19;
					else
						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_19;

					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, STR_SPECIFIC_CHAR + oCellule
									.getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_1, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(0).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_2, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(1).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_3, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(2).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_4, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(3).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_5, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(4).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_6, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(5).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_7, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(6).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_8, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(7).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_9, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(8).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_10, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(9).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_11, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(10).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_12, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(11).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_13, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(12).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_14, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(13).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_15, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(14).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_16, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(15).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_17, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(16).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_18, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(17).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					break;

				case 19:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_20;
					else
						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_20;

					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, STR_SPECIFIC_CHAR + oCellule
									.getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_1, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(0).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_2, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(1).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_3, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(2).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_4, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(3).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_5, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(4).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_6, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(5).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_7, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(6).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_8, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(7).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_9, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(8).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_10, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(9).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_11, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(10).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_12, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(11).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_13, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(12).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_14, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(13).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_15, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(14).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_16, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(15).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_17, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(16).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_18, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(17).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_19, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(18).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					break;

				case 20:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_21;
					else
						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_21;

					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, STR_SPECIFIC_CHAR + oCellule
									.getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_1, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(0).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_2, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(1).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_3, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(2).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_4, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(3).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_5, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(4).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_6, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(5).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_7, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(6).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_8, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(7).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_9, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(8).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_10, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(9).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_11, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(10).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_12, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(11).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_13, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(12).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_14, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(13).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_15, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(14).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_16, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(15).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_17, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(16).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_18, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(17).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_19, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(18).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_20, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(19).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					break;

				default:
					if (oCellule.getEnumOrganigrammeType() == EnumTypeOrganigramme.RAKE)
						strTemplate = ClsTemplate.STR_TEMPLATE_21;
					else
						strTemplate = ClsTemplate.STR_TEMPLATE_LIST_21;

					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_PERE, STR_SPECIFIC_CHAR + oCellule
									.getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_1, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(0).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_2, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(1).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_3, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(2).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_4, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(3).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_5, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(4).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_6, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(5).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_7, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(6).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_8, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(7).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_9, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(8).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_10, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(9).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_11, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(10).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_12, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(11).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_13, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(12).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_14, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(13).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_15, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(14).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_16, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(15).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_17, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(16).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_18, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(17).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_19, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(18).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					strTemplate = ClsTemplate.replaceString(
							ClsTemplate.STR_FILS_20, STR_SPECIFIC_CHAR + oCellule.getOListefils2()
									.get(19).getCodeorganigramme()
									+ STR_SPECIFIC_CHAR, strTemplate);
					break;
				}
			}
			return strTemplate;
		} catch (Exception e) {
			return strTemplate;
		}
	}

	public static String getCellList(String strCategorie,
			String strCategoriePoste, String strLibelleOrganigramme,
			String strEffectif, String strNom, String strMatricule, String color, String strBorderSize, String strCaseFictive) {
		Integer intEffectif = 0;

		if(strCaseFictive != null && strCaseFictive.equalsIgnoreCase("O"))
			return "";//STR_FICTIVE_CELL_LIST;
		
		if (strEffectif != null && strEffectif.trim().length() != 0)
		{
			try {
				intEffectif = Integer.parseInt(strEffectif.trim()) + 1;
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		else
			intEffectif += 1;
		
		//ParameterUtil.println("..........................."+"Nbr de cellules fils ds getCellList = "+intEffectif);
		
		if (strNom != null && strNom.trim().length() != 0)
			strNom = strNom.toUpperCase();
		else
			strNom = "";

		if (strLibelleOrganigramme != null
				&& strLibelleOrganigramme.trim().length() != 0)
			strLibelleOrganigramme = strLibelleOrganigramme.toUpperCase();

		if (strCategorie == null)
			strCategorie = "";

		if (strMatricule == null)
			strMatricule = "";

		if (color == null || color.trim().length() == 0)
			color = "blue";

		if (strCategorie == null)
			strCategorie = "--";
		if (strCategoriePoste == null)
			strCategoriePoste = "--";
		
		if(strBorderSize == null || strBorderSize.trim().length() == 0)
			strBorderSize = "2";

		return "<table cellspacing=\"0\" border=\"0\" cellpadding=\"0\"><tr><td>" + "<table border=\"0\" style=\"border: "
				+ color + " " + strBorderSize + "px solid;\" cellspacing=\"0\" cellpadding=\"0\">" + "<tr>"
				+ "<td colspan=\"3\" align=\"center\">" + strCategoriePoste
				+ " " + strLibelleOrganigramme + " " + "["
				+ intEffectif.toString() + "]" + "</td>" + "</tr>"
				+ "</table></td>" + "<td align=\"center\" >" + "  " + strNom
				+ " " + strMatricule + " [" + strCategorie
				+ "]</td></tr></table>";

		// return "<table border=\"0\" style=\"border: " + color + " 2px
		// solid;\" cellspacing=\"0\">"+
		// "<tr>" +
		// "<td colspan=\"3\" align=\"center\">" + strCategorie + " " +
		// strLibelleOrganigramme + " " + "[" + intEffectif.toString() + "]" +
		// "</td>" +
		// "<td align=\"center\" >" + " " + strNom + " " + strMatricule +
		// "</td>" +
		// "</tr>" +
		// "</table>";
	}

	// Get cell of flow chart.
	public static String getCell(HttpServletRequest request,
								 String codeorganigramme, String libelle, String codeposte,
								 String matriculeagent, String nomagent, String codeniveau,
								 Integer nbreFils, String color, EnumFlowChartView _enumView, String strBorderSize, String strPrest, String strCaseFictive) {
		String strCell = "";

		if(strCaseFictive != null && strCaseFictive.equalsIgnoreCase("O"))
			return STR_FICTIVE_CELL;
		
		if (color == null || color.trim().length() == 0)
			color = "blue";

		if (_enumView == EnumFlowChartView.WITHOUT_ACTION)
			return ClsTemplate._getWithoutActionTemplate(request,
					matriculeagent, libelle, color, strBorderSize);
		else if (_enumView == EnumFlowChartView.FONCTION || strPrest.equalsIgnoreCase("O"))
			return ClsTemplate._getFunctionTemplate(libelle, color, strBorderSize);

		String codelibelle = STR_SPECIAL_CHAR + codeorganigramme
				+ STR_SPECIAL_CHAR;// + "," + STR_SPECIAL_CHAR + libelle +
									// STR_SPECIAL_CHAR;
		// String codeexterne = STR_SPECIAL_CHAR + "" + STR_SPECIAL_CHAR + "," +
		// STR_SPECIAL_CHAR + codeorganigramme + STR_SPECIAL_CHAR;
		String codeexterne = STR_SPECIAL_CHAR + codeorganigramme
				+ STR_SPECIAL_CHAR;
		// String codelibelleniveau = STR_SPECIAL_CHAR + codeorganigramme +
		// STR_SPECIAL_CHAR + "," + STR_SPECIAL_CHAR + libelle +
		// STR_SPECIAL_CHAR + "," + STR_SPECIAL_CHAR + codeniveau +
		// STR_SPECIAL_CHAR;
		String codelibelleniveau = STR_SPECIAL_CHAR + codeorganigramme
				+ STR_SPECIAL_CHAR + "," + STR_SPECIAL_CHAR + codeniveau
				+ STR_SPECIAL_CHAR;
		// String codelibellenbrefils = STR_SPECIAL_CHAR + codeorganigramme +
		// STR_SPECIAL_CHAR + "," + STR_SPECIAL_CHAR + libelle +
		// STR_SPECIAL_CHAR + "," + STR_SPECIAL_CHAR + nbreFils.toString() +
		// STR_SPECIAL_CHAR;
		String codelibellenbrefils = STR_SPECIAL_CHAR + codeorganigramme
				+ STR_SPECIAL_CHAR + "," + STR_SPECIAL_CHAR
				+ nbreFils.toString() + STR_SPECIAL_CHAR;
		String orgposte = STR_SPECIAL_CHAR + codeorganigramme
				+ STR_SPECIAL_CHAR + "," + STR_SPECIAL_CHAR + codeposte
				+ STR_SPECIAL_CHAR;

		// if(color == null || color.trim().length() == 0)
		// color = "blue";

		// libelle = ClsTemplate._getCompleteCharacter(25 - libelle.length(),
		// libelle);

		libelle = __getFlowChartName(libelle);
		
		
		
		// libelle = "<strong>" + libelle + _strCompleteChars + "</span>" +
		// "</strong>";
		// }
		// else
		// libelle = "<strong>" + libelle.substring(0, 15) + "</strong>";

		try {
			if (matriculeagent == null || matriculeagent.trim().length() == 0)
				strCell = "<table width=\"14\" height=\"48\" style=\"border: "
						+ color
						+ " " + strBorderSize + "px solid;\" cellspacing=\"0\">"
						+ "<tr>"
						+ "<td ><table width=\"10\" height=\"10\" align=\"center\">"
						+ "<tr>"
						+ "<td  align=\"center\"><img src=\"images/application/photoorg.gif\" border=\"0\" align=\"middle\"></td>"
						+ "</tr>"
						+ "</table></td>"
						+ "<td  rowspan=\"2\"><table width=\"30\" height=\"28\" border=\"0\" align=\"left\" cellspacing=\"5\" bordercolor=\"#000000\">"
						+ "<tr>"
						+ "<td align=\"center\" valign=\"middle\"><a href=\"#\"><img src=\"images/application/effectif.gif\"  width=\"18\" height=\"18\" border=\"0\" title=\"saisir Effectif\" onclick=\"javascript:saisireffectif("
						+ codelibelle
						+ ")\"></a></td>"
						+ "</tr>"
						+ "<tr>"
						+ "<td align=\"center\" valign=\"middle\"><a href=\"#\"><img src=\"images/application/prestataires.gif\" width=\"18\" height=\"18\" border=\"0\" title=\"Saisir Prestataire\" onclick=\"javascript:editRhpexterne2("
						+ codelibelle + ")\"></a></td>" + "</tr>" + "<tr>";// +
			else {
				String strPath = STR_PHOTO_PATH + matriculeagent + ".png";
				// ParameterUtil.println(request.getRealPath(strPath));
				File file = new File(request.getRealPath(strPath));

				if (file.exists())
					strPath = "photos/" + matriculeagent + ".png";
				else
					strPath = "photos/" + "photoorg.gif";

				strCell = "<table width=\"14\" height=\"48\" style=\"border: "
						+ color
						+ " " + strBorderSize + "px solid;\" cellspacing=\"0\" bordercolor=\"#999999\">"
						+ "<tr>"
						+ "<td ><table width=\"10\" height=\"10\" align=\"center\">"
						+ "<tr>"
						+ "<td  align=\"center\"><a href=\"#\"><img src=\""
						+ strPath
						+ "\" border=\"0\" align=\"middle\" onclick=\"javascript:appercuagent("
						+ STR_SPECIAL_CHAR
						+ matriculeagent
						+ STR_SPECIAL_CHAR
						+ ")\" title=\""
						+ nomagent
						+ "\"></a></td>"
						+ "</tr>"
						+ "</table></td>"
						+ "<td  rowspan=\"2\"><table width=\"30\" height=\"28\" border=\"0\" align=\"left\" cellspacing=\"5\" bordercolor=\"#000000\">"
						+ "<tr>"
						+ "<td align=\"center\" valign=\"middle\"><a href=\"#\"><img src=\"images/application/effectif.gif\" width=\"18\" height=\"18\" border=\"0\" title=\"saisir Effectif\" onclick=\"javascript:saisireffectif("
						+ codelibelle
						+ ")\"></a></td>"
						+ "</tr>"
						+ "<tr>"
						+ "<td align=\"center\" valign=\"middle\"><a href=\"#\"><img src=\"images/application/prestataires.gif\" width=\"18\" height=\"18\" border=\"0\" title=\"Saisir Prestataire\" onclick=\"javascript:editRhpexterne2("
						+ codelibelle + ")\"></a></td>" + "</tr>" + "<tr>";
			}

			if (codeposte == null || codeposte.trim().length() == 0)
				strCell += "<td align=\"center\" valign=\"middle\"><a href=\"#\"><img src=\"images/application/poste1.gif\" width=\"18\" height=\"18\" border=\"0\" title=\"Affecter Poste\" onclick=\"javascript:affecterposte("
						+ codelibelle
						+ ")\"></a></td>"
						+ "</tr>"
						+ "</table></td>"
						+ "</tr>"
						+ "<tr>"
						+
						// "<td align=\"center\" valign=\"middle\"><span
						// class=\"Style1\">"+ libelle + "</span></td>" +
						"<td colspan=\"2\" align=\"center\" valign=\"middle\"><span style=\"color: #6e6e6e; font-family: Verdana, Arial, Helvetica, sans-serif; font-weight:bold\">"
						+ libelle
						+ "</span></td>"
						+ "</tr>"
						+ "<tr>"
						+ "<td colspan=\"2\"><table width=\"96%\" height=\"20%\" border=\"0\" cellspacing=\"0\" bordercolor=\"#000000\">"
						+ "<tr>"
						+ "<td align=\"center\" valign=\"middle\"><a href=\"javascript:ajouterfils("
						+ codelibelle
						+ ")\"><img src=\"images/application/fils1.gif\" border=\"0\" title=\"Ajouter Fils\"></a></td>"
						+ "<td align=\"center\" valign=\"middle\"><a href=\"#\"><img src=\"images/application/modifier.gif\" border=\"0\" title=\"Modifier Organigramme\" onclick=\"javascript:modifierorganigramme("
						+ codelibelleniveau
						+ ")\"></a></td>"
						+ "<td align=\"center\" valign=\"middle\"><a href=\"#\"><img src=\"images/application/supprimer.gif\" border=\"0\" title=\"Supprimer Organigramme\" onclick=\"javascript:supprimerorganigramme("
						+ codelibellenbrefils
						+ ")\"></a></td>"
						+ "<td align=\"center\" valign=\"middle\"><img src=\"images/application/poste.gif\" border=\"0\" title=\"Consulter Poste\"></td>"
						+ "</tr>" + "</table></td>" + "</tr>" + "</table>";
			else
				strCell += "<td align=\"center\" valign=\"middle\"><a href=\"#\"><img src=\"images/application/poste1.gif\" width=\"18\" height=\"18\" border=\"0\" title=\"D�saffecter Poste\" onclick=\"javascript:desaffecterposte("
						+ orgposte
						+ ")\"></a></td>"
						+ "</tr>"
						+ "</table></td>"
						+ "</tr>"
						+ "<tr>"
						+
						// "<td align=\"center\" valign=\"middle\"><span
						// class=\"Style1\">"+ libelle + "</span></td>" +
						"<td colspan=\"2\" align=\"center\" valign=\"middle\"><span style=\"color: #6e6e6e; font-family: Verdana, Arial, Helvetica, sans-serif; font-weight:bold\">"
						+ libelle
						+ "</span></td>"
						+ "</tr>"
						+ "<tr>"
						+ "<td colspan=\"2\"><table width=\"96%\" height=\"20%\" border=\"0\" cellspacing=\"0\" bordercolor=\"#000000\">"
						+ "<tr>"
						+ "<td align=\"center\" valign=\"middle\"><a href=\"javascript:ajouterfils("
						+ codelibelle
						+ ")\"><img src=\"images/application/fils1.gif\" border=\"0\" title=\"Ajouter Fils\" ></a></td>"
						+ "<td align=\"center\" valign=\"middle\"><a href=\"#\"><img src=\"images/application/modifier.gif\" border=\"0\" title=\"Modifier Organigramme\" onclick=\"javascript:modifierorganigramme("
						+ codelibelleniveau
						+ ")\"></a></td>"
						+ "<td align=\"center\" valign=\"middle\"><a href=\"#\"><img src=\"images/application/supprimer.gif\" border=\"0\" title=\"Supprimer Organigramme\" onclick=\"javascript:supprimerorganigramme("
						+ codelibellenbrefils
						+ ")\"></a></td>"
						+ "<td align=\"center\" valign=\"middle\"><a href=\"#\"><img src=\"images/application/poste.gif\" border=\"0\" title=\"Consulter Poste\" onclick=\"javascript:consulterposte("
						+ STR_SPECIAL_CHAR
						+ codeposte
						+ STR_SPECIAL_CHAR
						+ ")\"></a></td>"
						+ "</tr>"
						+ "</table></td>"
						+ "</tr>"
						+ "</table>";

			return strCell;
		} catch (Exception e) {
			return strCell;
		}
	}
	
	private static String __getFlowChartName(String libelle)
	{
		try {
			if (libelle.trim().length() < 16) {
				
				int intRest = 15 - libelle.trim().length();
				if(intRest > 0)
				{
					libelle = libelle.trim();
					String _strCompleteChars = "<span style=\"color:#d2d2d2\">";
					for(int i = 1; i <= intRest; i++)
					{
						_strCompleteChars += "1";
					}
					libelle = libelle + _strCompleteChars + "</span>";
				}
			} else
				libelle = libelle.substring(0, 15);
			
			return libelle;
			
		} catch (Exception e) {
			// TODO: handle exception
			return libelle;
		}
	}

	/**
	 * Get function template.
	 * 
	 * @param strLibelle
	 * @return
	 */
	private static String _getFunctionTemplate(String strLibelle, String color, String strBorderSize) {
		String strCell = "";

		try {
			
			strLibelle = __getFlowChartName(strLibelle);
			
			strCell = "<table width=\"14\" height=\"48\" style=\"border:"
					+ color
					+ " " + strBorderSize + "2px solid;\" cellspacing=\"0\">"
					+ "<tr>"
					+ "<td align=\"center\" valign=\"middle\"><span style=\"color: #6e6e6e; font-family: Verdana, Arial, Helvetica, sans-serif; font-weight:bold\">"
					+ strLibelle + "</span></td>" + "</tr>" + "</table>";
		} catch (Exception e) {
			// TODO: handle exception
		}

		return strCell;
	}

	/**
	 * Get function template.
	 * 
	 * @param strLibelle
	 * @return
	 */
	private static String _getWithoutActionTemplate(HttpServletRequest request,
			String matriculeagent, String strLibelle, String color, String strBorderSize) {
		String strCell = "";

		try {
			
			strLibelle = __getFlowChartName(strLibelle);
			
			if (matriculeagent == null || matriculeagent.trim().length() == 0)
				strCell = "<table width=\"14\" height=\"48\" style=\"border:"
						+ color
						+ " " + strBorderSize + "px solid;\" cellspacing=\"0\">"
						+ "<tr>"
						+ "<td ><table width=\"10\" height=\"10\" align=\"center\">"
						+ "<tr>"
						+ "<td  align=\"center\"><img src=\"images/application/photoorg.gif\" border=\"0\" align=\"middle\"></td>"
						+ "</tr>"
						+ "</table></td>"
						+ "</tr>"
						+ "<tr>"
						+ "<td align=\"center\" valign=\"middle\"><span style=\"color: #6e6e6e; font-family: Verdana, Arial, Helvetica, sans-serif; font-weight:bold\">"
						+ strLibelle + "</span></td>" + "</tr>" + "</table>";
			else {
				String strPath = STR_PHOTO_PATH + matriculeagent + ".png";
				// ParameterUtil.println(request.getRealPath(strPath));
				File file = new File(request.getRealPath(strPath));

				if (file.exists())
					strPath = "photos/" + matriculeagent + ".png";
				else
					strPath = "photos/" + "photoorg.gif";

				strCell = "<table width=\"14\" height=\"48\" style=\"border:"
						+ color
						+ " " + strBorderSize + "px solid;\" cellspacing=\"0\">"
						+ "<tr>"
						+ "<td ><table width=\"10\" height=\"10\" align=\"center\">"
						+ "<tr>"
						+ "<td  align=\"center\"><img src=\""
						+ strPath
						+ "\" border=\"0\" align=\"middle\"></td>"
						+ "</tr>"
						+ "</table></td>"
						+ "</tr>"
						+ "<tr>"
						+ "<td align=\"center\" valign=\"middle\"><span style=\"color: #6e6e6e; font-family: Verdana, Arial, Helvetica, sans-serif; font-weight:bold\">"
						+ strLibelle + "</span></td>" + "</tr>" + "</table>";
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return strCell;
	}

	private static String _getCompleteCharacter(int _intStringRemainderLengh,
			String _strStringValue) {

		String _strBeforeString = "<span style=\"color:#D5F5F9\"> ";
		String _strAfterString = "<span style=\"color:#D5F5F9\"> ";

		try {

			if (_intStringRemainderLengh <= 0)
				return _strStringValue;

			for (int index = 0; index < _intStringRemainderLengh; index++) {
				if (index % 2 == 0)
					_strBeforeString += "&nbsp;";
				else
					_strAfterString += "&nbsp;";
			}

		} catch (Exception e) {
			// TODO: handle exception

		}

		_strBeforeString += "</span>";
		_strAfterString += "</span>";

		return _strBeforeString + _strStringValue + _strAfterString;

	}

	private static String _getCompleteCharacter2(int _intStringRemainderLengh,
			String _strStringValue) {

		try {

			if (_intStringRemainderLengh <= 0)
				return _strStringValue;

			for (int index = 0; index < _intStringRemainderLengh; index++) {
				_strStringValue += "&nbsp;";
			}

		} catch (Exception e) {
			// TODO: handle exception

		}

		return _strStringValue;

	}
	/**
	 * Get flowchart element legend.
	 * @param strColor
	 * @param strLevel
	 * @return
	 */
	public static String _getFlowChartLegElement(String strColor, String strLevel)
	{
		
		if(strColor == null || strColor.trim().length() == 0)
			return "";
		
		if(strLevel == null || strLevel.trim().length() == 0)
			return "";
			
		String strLegElement = "<td>"+
							   "<table border=\"0\" cellspacing=\"0\">"+
							   "<tr>"+
							   "<td>"+
							   "<table border=\"1\" cellspacing=\"0\" bordercolor=\"#000000\">"+
							   "<tr>"+
							   "<td width=\"10\" bordercolor=\"" + strColor + "\" bgcolor=\"" + strColor + "\">&nbsp;</td>"+
							   "</tr>"+
							   "</table>"+
							   "</td>"+
							   "<td nowrap=\"nowrap\" style='color: #6e6e6e; font-family: Verdana; font-weight:bold; font-size: 9px;'>" + strLevel + "</td>"+
							   "</tr>"+
							   "</table>"+
							   "</td>";
//							   "<td>&nbsp;"+
//							   "</td>";
		return strLegElement;
	}
	/**
	 * Get the wright soon code.
	 * @return
	 */
	public static boolean _getControlAndSaveCell(GeneriqueConnexionService oService, Organigramme oOrganigramme)
	{
		 String strFatherCode;
		 String strSoonCode;
		 int intSoonLevel;
		 
		try {
			
			strFatherCode = oOrganigramme.getCodepere();
			strSoonCode = oOrganigramme.getCodeorganigramme();
			intSoonLevel = Integer.parseInt(oOrganigramme.getCodeniveau());
//			if(strSoonCode == null || Integer.parseInt(strSoonCode) == (2*intSoonLevel))
//				return strSoonCode;
			
			int intSoonCodeSize = strSoonCode.length();
		
			if(intSoonCodeSize < (2*intSoonLevel))
			{
				int intDiff = (2*intSoonLevel) - intSoonCodeSize;
//				for(int i = 0; i < intDiff; i++)
//				{
//					strFatherCode += "9";
//				}
//				
//				strSoonCode = strFatherCode + strSoonCode.substring(oOrganigramme.getCodepere().length(), strSoonCode.length());
//				oOrganigramme.setCodeorganigramme(strSoonCode);
				
				String strNextCelCode = strFatherCode;
				String strCellFaherCode = strNextCelCode;
				Organigramme oFlowChart = null;
				ParameterUtil.println(">>>>>>>>>>>>>>>Nbr iterations * 2 = "+intDiff);
				for(int j = 1; j <= (intDiff/2); j++)
				{
					try {
						
						strCellFaherCode = strNextCelCode;
						strNextCelCode += "99";
						Date dt = new Date();
						dt.setHours(0);
						dt.setMinutes(0);
						dt.setSeconds(0);
						ParameterUtil.println(strNextCelCode.length()/2);
						ParameterUtil.println(strNextCelCode);
						oFlowChart = new Organigramme(oOrganigramme.getIdEntreprise(), strNextCelCode,
								                         "CASE FICTIVE " + strNextCelCode, String.valueOf(strNextCelCode.length()/2), dt, 
								                         "", "", "", strCellFaherCode, "", "N", "O",oOrganigramme.getCodesite(),oOrganigramme.getNivfonction(),oOrganigramme.getLibellecourt());
						oService.save(oFlowChart);
						
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						continue;
					}
				}
				
				oOrganigramme.setBcasefictive("N");
				oOrganigramme.setCodeorganigramme(strNextCelCode + strSoonCode.substring(oOrganigramme.getCodepere().length(), strSoonCode.length()));
				oOrganigramme.setCodepere(strNextCelCode);
			}
			else if(intSoonCodeSize > (2*intSoonLevel))
				return false;
			
			
			ParameterUtil.println(">>>>>>>>>>>>>>>oOrganigramme.getAccepteexterne()"+oOrganigramme.getAccepteexterne());
			ParameterUtil.println(">>>>>>>>>>>>>>>oOrganigramme.getBcasefictive()"+oOrganigramme.getBcasefictive());
			ParameterUtil.println(">>>>>>>>>>>>>>>oOrganigramme.getBprestataire()"+oOrganigramme.getBprestataire());
			ParameterUtil.println(">>>>>>>>>>>>>>>oOrganigramme.getCodematricule()"+oOrganigramme.getCodematricule());
			ParameterUtil.println(">>>>>>>>>>>>>>>oOrganigramme.getCodeniveau()"+oOrganigramme.getCodeniveau());
			ParameterUtil.println(">>>>>>>>>>>>>>>oOrganigramme.getCodepere()"+oOrganigramme.getCodepere());
			ParameterUtil.println(">>>>>>>>>>>>>>>oOrganigramme.getCodeposte()"+oOrganigramme.getCodeposte());
			ParameterUtil.println(">>>>>>>>>>>>>>>oOrganigramme.getCodesite()"+oOrganigramme.getCodesite());
			ParameterUtil.println(">>>>>>>>>>>>>>>oOrganigramme.getEstvalide()"+oOrganigramme.getEstvalide());
			ParameterUtil.println(">>>>>>>>>>>>>>>oOrganigramme.getLibellecourt()"+oOrganigramme.getLibellecourt());
			ParameterUtil.println(">>>>>>>>>>>>>>>oOrganigramme.getLibelle()"+oOrganigramme.getLibelle());
			ParameterUtil.println(">>>>>>>>>>>>>>>oOrganigramme.getNivfonction()"+oOrganigramme.getNivfonction());
			ParameterUtil.println(">>>>>>>>>>>>>>>oOrganigramme.getCodeorganigramme()"+oOrganigramme.getCodeorganigramme());
			ParameterUtil.println(">>>>>>>>>>>>>>>oOrganigramme.getCdos()"+oOrganigramme.getIdEntreprise());
			
			
			oService.save(oOrganigramme);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

}
