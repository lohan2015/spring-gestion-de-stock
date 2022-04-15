/**
 * 
 */
package com.cmbassi.gestiondepaie.services.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @author dba
 * 
 */
public class ClsParameter
{
	public static boolean refreshWholePage = false;
	public static String typeRefresh = "R";
	public static String formatRubrique = "0000";
	public static Integer longueurRubrique = 4;
	public static Integer getMaxRubrique()
	{
		String n="";
		for(int i=0;i<longueurRubrique;i++) n+="9";
		return Integer.valueOf(n);
	}
	public static String getMaxRubriqueStr()
	{
		String n="";
		for(int i=0;i<longueurRubrique;i++) n+="9";
		return n;
	}

}
