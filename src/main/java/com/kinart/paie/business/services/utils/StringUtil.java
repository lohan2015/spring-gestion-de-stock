/**
 * 
 */
package com.kinart.paie.business.services.utils;

import org.springframework.util.StringUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * @author c.mbassi
 *
 */
public class StringUtil extends org.apache.commons.lang3.StringUtils {

	/**
	 * Teste si la chaine ne fait partie de chainevaleurs chainevaleurs possède le format suivant : "Chaine1,Chaine2,Chaine3,..."
	 *
	 * @param chaine
	 * @param chainevaleurs
	 * @return
	 */
	public static boolean notIn(String chaine, String chainevaleurs)
	{
		if (isBlank(chainevaleurs))
			return true;

		StringTokenizer tokenizer = new StringTokenizer(chainevaleurs, ",");
		while (tokenizer.hasMoreTokens())
		{
			if (equals(chaine, tokenizer.nextToken()))
				return false;
		}
		return true;
	}

	public static String substring(String chaine, int indiceDebut, int indiceFin){
		String resultat="";
		if(chaine!=null){
			if(indiceFin> chaine.length()) resultat=chaine;
			else resultat=chaine.substring(indiceDebut-1, indiceFin);
		}
		return resultat;
	}

	public static String compactString(String chaine, int longueur)
	{
		if (chaine != null && chaine.length() > longueur)
			return new String(oraSubstring(chaine, 1, longueur) + "...");
		else if (chaine != null)
			return new String(chaine);
		else return null;
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

	public static String oraLTrim(String arg)
	{
		return oraLTrim(arg, " ");
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

	public static String oraSubstring(String chaine, int debut, int longueur)
	{
		return substring(chaine, debut - 1, longueur + debut - 1);
	}

//	public static String lastCharacter(String chaine)
//	{
//		if (StringUtils.isEmpty(chaine))
//			return chaine;
//		return substring(chaine, chaine.length() - 1);
//	}

	public static boolean isGreaterOrEqual(String chaine1, String chaine2)
	{
		return isGreaterThan(chaine1, chaine2) || StringUtil.equalsIgnoreCase(chaine1, chaine2);

	}

	public static boolean isLesserOrEqual(String chaine1, String chaine2)
	{
		return isGreaterThan(chaine2, chaine1) || StringUtil.equalsIgnoreCase(chaine2, chaine1);

	}

	public static boolean isLesserThan(String chaine1, String chaine2){
		if(StringUtil.equalsIgnoreCase(chaine2, chaine1)){
			return false;
		} else {
			return isGreaterThan(chaine2, chaine1);
		}
	}

	public static boolean isGreaterThan(String chaine1, String chaine2)
	{
		if (org.apache.commons.lang3.StringUtils.equals(chaine1, chaine2))
			return false;
		if (StringUtils.isEmpty(chaine1) && org.apache.commons.lang3.StringUtils.isNotEmpty(chaine2))
			return false;
		if (StringUtils.isEmpty(chaine2) && org.apache.commons.lang3.StringUtils.isNotEmpty(chaine1))
			return false;
		if (chaine1.compareTo(chaine2) < 0)
			return false;
		else return true;

	}


	public static boolean isNotGreaterThan(String chaine1, String chaine2)
	{
		return !StringUtil.isGreaterThan(chaine1, chaine2);
	}

	/**
	 * Teste si la chaine fait partie de chainevaleurs chainevaleurs possède le format suivant : "Chaine1,Chaine2,Chaine3,..."
	 *
	 * @param chaine
	 * @param chainevaleurs
	 * @return
	 */
	public static boolean in(String chaine, String chainevaleurs)
	{
		return !notIn(chaine, chainevaleurs);
	}

	public static boolean notEquals(String chaine, String chainevaleurs)
	{
		return !chaine.equalsIgnoreCase(chainevaleurs);
	}

	public static void printOutObject(String texte, String absoluteFilePath)
	{
		printOutObject(texte, absoluteFilePath, true);
	}

	public static void printOutObject(String texte, String absoluteFilePath, boolean deleteExistingFile)
	{
		String nomfichier = absoluteFilePath;

		File foutput = new File(nomfichier);
		Writer output = null;

		try
		{
			if (foutput.exists())
				if (deleteExistingFile)
					foutput.delete();

			if (!foutput.exists())
			{
				_createFileFolder(nomfichier, "\\");
				foutput.createNewFile();
			}

			output = new BufferedWriter(new FileWriter(foutput, true));
		}
		catch (IOException ioex)
		{
			// ioex.printStackTrace();
		}

		try
		{
			if (deleteExistingFile)
				output.write(texte);
			else
			{
				//output.append(texte);
				PrintWriter pw = new PrintWriter(new FileWriter(foutput, true));

				pw.println(texte);

				pw.close();

			}

			output.close();
		}
		catch (Exception ioex)
		{
			// ioex.printStackTrace();
		}
	}

	public static void _createFileFolder(String strAbsoluteFilePath, String strFileSeparator)
	{
		String folder = null;
		try
		{
			File foutput = new File(strAbsoluteFilePath);
			int indexOfFile = strAbsoluteFilePath.lastIndexOf(strFileSeparator);
			if(indexOfFile != -1)
			{
				folder = strAbsoluteFilePath.substring(0, indexOfFile);
				foutput = new File(folder);
				if(! foutput.exists())
				{
					foutput.mkdirs();
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	public static String toString(Object object)
	{
		Field[] fields = object.getClass().getDeclaredFields();

		Field[] fields2 = object.getClass().getFields();
		if (fields.length == 0)
			fields = fields2;

		return toString(fields, object, "get") + "\n" + toString(fields, object, "is");
	}

	private static String toString(Field[] fields, Object object, String methodNameStart)
	{
		String methodeName = null;
		Method method = null;
		String result = "";
		Object returnValue = null;
		Class returnType = null;
		for (Field field : fields)
		{
			try
			{
				methodeName = methodNameStart + StringUtils.capitalize(field.getName());
				method = object.getClass().getMethod(methodeName, null);

				if (method == null)
				{
					methodeName = "is" + StringUtils.capitalize(field.getName());
					method = object.getClass().getMethod(methodeName, null);
				}

				if (method != null)
				{
					returnValue = method.invoke(object, null);
					if (returnValue != null)
					{
						if (returnValue instanceof Date)
						{
							result += "\n->" + field.getName() + " = " + new SimpleDateFormat("dd-MM-yyyy").format(returnValue);
						}
						else if ((returnValue instanceof List) || (returnValue instanceof ArrayList))
						{
							result += "\n->Liste " + field.getName() + " = " + toStringList(returnValue);
						}
						else if (returnValue instanceof Object[])
						{
							result += "\n->Tableau " + field.getName() + " = " + toStringArray(returnValue);
						}
						else
						{
							/*
							 * returnType = method.getReturnType(); if( returnType.isPrimitive() ||
							 * ClsEnumTypes.BIGDECIMAL.equalsIgnoreCase(returnType.getSimpleName()) ||
							 * ClsEnumTypes.NUMBER.equalsIgnoreCase(returnType.getSimpleName()) ||
							 * ClsEnumTypes.DATE.equalsIgnoreCase(returnType.getSimpleName()) ||
							 * ClsEnumTypes.DOUBLE.equalsIgnoreCase(returnType.getSimpleName()) ||
							 * ClsEnumTypes.FLOAT.equalsIgnoreCase(returnType.getSimpleName()) || ClsEnumTypes.INT.equalsIgnoreCase(returnType.getSimpleName()) ||
							 * ClsEnumTypes.STRING.equalsIgnoreCase(returnType.getSimpleName())) result += "\n->"+field.getName()+" = "+ returnValue; else
							 * result += "\n->Objet "+field.getName()+" = \n\t\t"+toString(returnValue);
							 */

							result += "\n->" + field.getName() + " = " + returnValue;

						}
					}
					else result += "\n->" + field.getName() + " = " + returnValue;
				}
			}
			catch (SecurityException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IllegalArgumentException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (NoSuchMethodException e)
			{
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (InvocationTargetException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		// if(isBlank(result))
		// result = toStringMethode(object);
		return result;
	}

	private static String toStringMethode(Object object)
	{
		Method[] methodes = object.getClass().getDeclaredMethods();
		if (methodes.length == 0)
			methodes = object.getClass().getMethods();
		String result = "";
		for (Method methode : methodes)
		{
			try
			{
				if (methode.getName().startsWith("get"))
					result += "\n->" + methode.getName() + "=" + methode.invoke(object, null);
			}
			catch (SecurityException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IllegalArgumentException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (InvocationTargetException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return result;
	}

	private static String toStringList(Object object)
	{
		String result = "\n----------------------------------------------------------------\n";
		List<Object> liste = (List<Object>) object;
		for (Object object3 : liste)
		{
			if (object3 != null)
			{
				if (!(object3 instanceof String) && !(object3 instanceof Integer) && !(object3 instanceof Date) && !(object3 instanceof Number))
					result += "\t\t" + toString(object3);
				else result += "\t\t" + object3;
			}
			else
			{
				result += "\t\t" + object3;
			}
			result += "\n";
		}
		result += "\n----------------------------------------------------------------";
		return result;
	}

	private static String toStringArray(Object object)
	{
		String result = "\n----------------------------------------------------------------\n";
		Object[] tableau = (Object[]) object;

		int index = 0;
		for (Object object3 : tableau)
		{
			if (object3 != null)
			{
				if (!(object3 instanceof String) && !(object3 instanceof Integer) && !(object3 instanceof Date) && !(object3 instanceof Number))
					result += "\t\t" + toString(object3);
				else result += "\t\t" + object3;
			}
			else
			{
				result += "\t\t" + object3;
			}
			result += "\n";

		}
		result += "\n----------------------------------------------------------------";
		return result;
	}

	/**
	 * Teste si la premiere chaine est nulle, auquel cas renvoyer la seconde
	 *
	 * @param chaine
	 * @param remplacant
	 * @return
	 */
	public static String nvl(String chaine, String remplacant)
	{
		if (isBlank(chaine))
			return remplacant;
		return chaine;
	}

	/**
	 * Teste si l'objet 'chaine' est null, auquel cas renvoyer la chaine remplacant
	 *
	 * @param chaine
	 * @param remplacant
	 * @return
	 */
	public static String nvl(Object chaine, String remplacant)
	{
		if (chaine == null)
			return remplacant;
		if (isBlank((String) chaine))
			return remplacant;
		return (String) chaine;
	}

	/**
	 * retrait des accents
	 * @param s
	 * @return
	 */
	public static final String unAccent(String s)
	{
		//
		// JDK1.5
		//   use sun.text.Normalizer.normalize(s, Normalizer.DECOMP, 0);
		//
		//	      String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
		if (isBlank(s))
			return EMPTY;
		//String temp = Normalizer.decompose(s, false, 0).replaceAll("\\p{IsM}+", "");
			      Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
			      return pattern.matcher(s).replaceAll("");
		//return temp;
	}

	public static String lastCharacter(String chaine)
	{
		if (StringUtils.isEmpty(chaine))
			return chaine;
		return StringUtil.substring(chaine, chaine.length() - 1);
	}
}
