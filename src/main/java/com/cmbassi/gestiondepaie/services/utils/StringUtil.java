/**
 * 
 */
package com.cmbassi.gestiondepaie.services.utils;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @author c.mbassi
 *
 */
public class StringUtil {
	
	
	public static String substring(String chaine, int indiceDebut, int indiceFin){
		String resultat="";
		if(chaine!=null){
			if(indiceFin> chaine.length()) resultat=chaine;
			else resultat=chaine.substring(indiceDebut-1, indiceFin);
		}
		return resultat;
	}
	
	public static String capitalize(String s) {
	    if (s.length() == 0) return s;
	    return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	}
	
	public static String capitalizefirstletter(String s) {
	    if (s.length() == 0) return s;
	    return s.substring(0, 1).toUpperCase() + s.substring(1);
	}

	public static String oraLTrim(String arg, String charToRemove){
		char[] char2RemoveList = charToRemove.toCharArray();
		char[] charTmpList;
		boolean continu = false;
		boolean found = false;
		int subLen = 1;
		int i=0;
		String strSubLen = "";
		//
		continu = true;
		while(continu){			
			strSubLen = arg.substring(0, subLen);
			//est-ce que tous ses char de la sous-chaine <strSubLen> sont dans <charToRemove>
			charTmpList = strSubLen.toCharArray();
			for(i=0; i < charTmpList.length; i++)
			{
				found = false;
				for(int j=0; j < char2RemoveList.length; j++)
				{
					// ce char n'est pas dans <charToRemove>
					// alors la longueur � enlever est (subLen -1)
					if(charTmpList[i] == char2RemoveList[j])
					{
						found = true;
						break;
					}
				}
				if(found == false)
					break;
			}
			//au cas o� le dernier char est different
			continu = found;
			if(subLen == arg.length())
			{
				continu = false;
			}
			subLen++;
		}
		//
		return arg.substring(i);
	}
	

	
	public static String oraRTrim(String arg, String charToRemove)
	{


		char[] argTab = arg.toCharArray();
		int index = 0;
		for (int i = argTab.length -1; i >= 0; i--)
		{
			if(charToRemove.indexOf(String.valueOf(argTab[i])) == -1)
			{
				index = i;
				break;
			}
		}
		
		return arg.substring(0,index+1);
		
	}
	
	public static BigDecimal truncateTo3Decimal(Double bd)
	{
		NumberFormat formatter = new DecimalFormat("#.###");
		String nbr = formatter.format(bd);
		return new BigDecimal(nbr.replaceAll(",", "."));
	}
	
	public static BigDecimal truncateTo3Decimal(BigDecimal bd)
	{
		NumberFormat formatter = new DecimalFormat("#.###");
		String nbr = formatter.format(bd);
		return new BigDecimal(nbr.replaceAll(",", "."));
	}
	
	public static BigDecimal truncateToXDecimal(Number bd, Integer nbrDecimales)
	{
		String nbr1 = "";
		for(int i=0; i<nbrDecimales; i++)
			nbr1+="#";
		nbr1 = "#."+nbr1;
		NumberFormat formatter = new DecimalFormat(nbr1);
		String nbr = formatter.format(bd);
		return new BigDecimal(nbr.replaceAll(",", "."));
	}
	
	public static String formatNumber(Number i, String fmt){
		if(i == null) i = 0;
//		 The 0 symbol shows a digit or 0 if no digit present
	    NumberFormat formatter = new DecimalFormat(fmt);
	    return formatter.format(i);
	}

	public static String reviewSubstring(String in, int minIdx)throws StringIndexOutOfBoundsException{
		if(minIdx >= 0 )
			return in.substring(minIdx, minIdx);
		else{		
			int len = in.length();
			int iMinIdx = Math.abs(minIdx);
			if(iMinIdx > len)
				throw new StringIndexOutOfBoundsException("Cannot get the length specified.");
			int idx = len - iMinIdx;
			return in.substring(idx, len-1);
		}
	}
	/**
	 * Renvoie une cha�ne de caract�res format�e repr�sentant un entier
	 * @param nToconvert entier � formater
	 * @param minIntegerDigits nombre minimum d
	 * @param maxIntegerDigits
	 * @param strPrefix
	 * @param strSuffix
	 * @return
	 */
	public static String int2string(Integer nToconvert, int minIntegerDigits, int maxIntegerDigits, String strPrefix, String strSuffix){
		NumberFormat nFormat = NumberFormat.getIntegerInstance();
		nFormat.setMinimumIntegerDigits(minIntegerDigits);
		nFormat.setMaximumIntegerDigits(maxIntegerDigits);
		nFormat.setGroupingUsed(false);
		return strPrefix + nFormat.format(nToconvert.doubleValue())+ strSuffix;
	}
	public static String double2string(Double doubleToconvert,  int minIntegerDigits, int maxIntegerDigits,int minFractionDigits,int maxFractionDigits, String strPrefix, String strSuffix){
		NumberFormat nFormat = NumberFormat.getInstance();
		nFormat.setMinimumFractionDigits(minFractionDigits);
		nFormat.setMaximumFractionDigits(maxFractionDigits);
		nFormat.setGroupingUsed(false);
		return strPrefix + nFormat.format(doubleToconvert)+ strSuffix;
	}

	
//	 R�pondre 	R�pondre 	R�pondre � tous 	R�pondre � tous 	Transf�rer 	Transf�rer 		D�placer 	Copier 	Supprimer 		�l�ment pr�c�dent 	�l�ment suivant 		Fermer 			 Aide 	 
//	 De:  	 Patrice CHEDJOU 	 Date:  	 mer. 13/02/2008 16:51
//	 �:  	 Emmanuel Tite ETOUNDI ABOA
//	 Cc:  	 
//	 Objet :  	 PR_FORMAT
//	 Pi�ces jointes :  	
//	Afficher sous forme de page Web
//
//	FUNCTION PR_FORMAT (mt IN cp_int.pce_mt%TYPE,     
//	      ndc IN NUMBER,
//	                    long_ret IN NUMBER,
//	      Avec_Zero IN BOOLEAN)
//	RETURN VARCHAR2
	public static String formatNumber(Number montant, int ndc, int long_ret, boolean Avec_Zero, char separateur){
//	IS
//	  mtf1     CHAR(20);
//	  c20      VARCHAR2(20);
//		String c20 = "";
		String mtf1 = "";
//
//	BEGIN
//	  IF Avec_Zero THEN
//	     IF ndc = 3 THEN
//	        mtf1 := LPAD(LTRIM(TO_CHAR(NVL(mt,0), '999999999990D999')),20,' ');
//	     ELSIF ndc = 2 THEN
//	        mtf1 := LPAD(LTRIM(TO_CHAR(NVL(mt,0),'9999999999990D99')),20,' ');
//	     ELSIF ndc = 1 THEN
//	        mtf1 := LPAD(LTRIM(TO_CHAR(NVL(mt,0),'99999999999990D9')),20,' ');
//	     ELSE
//	        mtf1 := LPAD(LTRIM(TO_CHAR(NVL(mt,0), '999999999999990')),20,' ');
//	     END IF;
//	  ELSE
//	     IF NOT NouZ(mt) THEN
//	       IF ndc = 3 THEN
//	          mtf1 := LPAD(LTRIM(TO_CHAR(NVL(mt,0), '999999999990D999')),20,' ');
//	       ELSIF ndc = 2 THEN
//	          --mtf1 := LPAD(LTRIM(TO_CHAR(NVL(mt,0),'9999999999990D99',:GLOBAL.NLS_NUMERIC)),20,' ');
//	          mtf1 := LPAD( LTRIM( TO_CHAR(NVL(mt,0),'9999999999990D99')),20,' ');
//	       ELSIF ndc = 1 THEN
//	         mtf1 := LPAD(LTRIM(TO_CHAR(NVL(mt,0),'99999999999990D9')),20,' ');
//	       ELSE
//	         mtf1 := LPAD(LTRIM(TO_CHAR(NVL(mt,0), '999999999999990')),20,' ');
//	       END IF;
//	     ELSE
//	       mtf1 := LPAD(' ',20,' ');
//	     END IF;
//	  END IF;
		if(Avec_Zero){
			if(ndc == 3)
				mtf1 = formatNumber(montant, "#"+ separateur + "###");
			else if(ndc == 2)
				mtf1 = formatNumber(montant, "#"+ separateur + "##");
			else if(ndc == 1)
				mtf1 = formatNumber(montant, "#"+ separateur + "#");
			else
				mtf1 = formatNumber(montant, "#");
		}
		else{
			if(montant.doubleValue() > 0){
				if(ndc == 3)
					mtf1 = formatNumber(montant, "#"+ separateur + "###");
				else if(ndc == 2)
					mtf1 = formatNumber(montant, "#"+ separateur + "##");
				else if(ndc == 1)
					mtf1 = formatNumber(montant, "#"+ separateur + "#");
				else
					mtf1 = formatNumber(montant, "#");
			}
				
		}
//
//	  c20  := ' ';
//	  c20 := SUBSTR(mtf1,20-long_ret+1);
//
//	  RETURN c20;
		return mtf1;
//	END;
//	 
	}
	
	/**
	 * Formate le number en entier en completant de zero au
	 * d�but de sorte que sa longueur atteigne taille.
	 * @param number
	 * @param taille
	 * @return
	 */
	public static String _getPartieEntiere(double number, int taille)
	{
		 BigDecimal bd = new BigDecimal(Math.floor(number));
		    bd.setScale(0);
		    String str = String.valueOf(bd);
		    int leng = str.length();
		    
		    if(leng<taille)
		    {
		    	int diff = 18 - leng;
		    	
		    	for(int i=0; i< diff; i++)
		    		str="0"+str;
		    }
		 return str;
	}
	
	public static String _getPartieEntiere(Number number)
	{
		 BigDecimal bd = new BigDecimal(Math.floor(number.doubleValue()));
		    bd.setScale(0);
		    String str = String.valueOf(bd);
		 return str;
	}
	
	
	
	private static String constituerPattern(int long_ret)
	{
		String pattern="#.";
		String temp="";
		for (int i = 0; i < long_ret; i++)
		{
			temp+="0";
		}
		pattern+=temp;
		return pattern;
	}
	
	private static String stringToAdd(int ndc, boolean Avec_Zero)
	{
		String temp="";
		for (int i = 0; i < ndc; i++)
		{
			temp+="0";
		}
		if(!Avec_Zero) temp = temp.replace("0", " ");
		return temp;
	}

	public static String oraLPad(String chaine, int totalLength, String charToAdd)
	{
		if (StringUtils.isEmpty(chaine))
			return chaine;
		String add = "";
//		if (ClsParameter._isStringNull(chaine))
//			chaine = "";
		if (chaine.length() < totalLength)
			for (int i = 0; i < totalLength - chaine.length(); i++)
				add += charToAdd;
		return add + chaine;
	}

	public static String oraLPad(String chaine, int totalLength, char charToAdd)
	{
		if (StringUtils.isEmpty(chaine))
			return chaine;
		String add = "";
//		if (ClsParameter._isStringNull(chaine))
//			chaine = "";
		if (chaine.length() < totalLength)
			for (int i = 0; i < totalLength - chaine.length(); i++)
				add += charToAdd;
		return add + chaine;
	}

	public static String oraLPad(String chaine, int totalLength)
	{
		return StringUtil.oraLPad(chaine, totalLength, " ");
	}

}
