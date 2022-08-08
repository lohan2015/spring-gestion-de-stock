/**
 * 
 */
package com.kinart.paie.business.services.utils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author c.mbassi
 * 
 */
public class ParameterUtil
{
	public static final String CHEMIN_GENERATION_FILES = "pages//genfiles//";
	private static boolean showPhoto = false;

	public static boolean refreshWholePage = false;
	public static String typeRefresh = "R";
	public static String formatRubrique = "0000";
	public static Integer longueurRubrique = 4;

	public static final String SESSION_LOGIN = "login";
	public static final String SESSION_DDMP = "ddmp";
	public static final String SESSION_FORMAT_DATE = "dateformat";
	public static final String SESSION_LANGUE = "langue";
	public static final String SESSION_DOSSIER = "dossier";

	public static String RUBRIQUE_BRUTE="RUBBRUT";
	public static String RUBRIQUE_NAP="RUBNAP";

	public static final int YEAR_ON_2_CHARACTER = 0;

	public static final String YEAR_ON_2_CHARACTER_FORMAT = "yy";

	public static final int YEAR_ON_4_CHARACTER = 1;

	public static final String YEAR_ON_4_CHARACTER_FORMAT = "yyyy";

	public static final int MONTH_AS_LETTER = 2;

	public static final int MONTH_AS_NUMBER = 3;

	public static final int DAY_AS_NUMBER = 4;

	public static final String MONT_FORMAT = "MM";

	public final static String CHAINE_VIDE = " ";

	public final static String CHAINE_ESPACE = " ";

	public final static String SEPARATEUR_TIRET = "-";

	public final static String PARANTHESE_OUVRANTE = "(";

	public final static String PARANTHESE_FERMENTE = ")";

	public final static String CHAINE_ALLER_A_LA_LIGNE = "<br>";

	public final static String CHAINE_ALLER_A_LA_LIGNE_DOUBLE = "<br><br>";

	public final static String CHAINE_ALLER_A_LA_LIGNE_JAVASCRIPT = "\n\n";

	public final static String CHAINE_TIRET = "--> ";

	public final static String CHAINE_UNDERSCORE = "_";

	public final static String SEPARATOR = "§";

	public final static String CHAINE_DEUX_POINTS = " : ";

	/** *******************ORGANIGRAMME AND NIVEAU******************************** */
	public static String STR_CODEORGANIGRAMME = "codeorganigramme";

	public static String STR_CODENIVEAU = "codeniveau";

	public static String STR_CODEPERE = "codepere";

	public static String STR_CODEPOSTE = "codeposte";

	public static String STR_CODEMATRICULE = "codematricule";

	public static String STR_LIBELLE = "libelle";

	public static String STR_ACCEPTEEXTERNE = "accepteexterne";

	public static String STR_CODECOULEUR = "codecouleur";

	public static String STR_CODE_MAT = "nmat";

	public static String STR_CODE_CAT_POSTE = "pcat";

	public static String STR_CODE_CAT_AGENT = "cat";

	public static String STR_NOM_AGENT = "nom";

	public static String STR_PRENOM_AGENT = "pren";

	public static String STR_PRISE_EN_COMPTE_COULEUR = "priseencomptecouleur";

	public static String STR_INIT_CODEPERE = "00";

	public static String STR_NB_FILS_TOTAL = "nbFilsTotal";

	/** ************************************************************************* */

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

	/**
	 * Convert money to String.
	 * @param _decAmount
	 * @param strSeparateurDeMillier
	 * @param strSeparateurDeDecimal
	 * @param strDevise
	 * @param intNombreDeDecimales
	 * @param boolPartieDecimalAdmise
	 * @return
	 */
	public static String __formatMoney(BigDecimal _decAmount, String strSeparateurDeMillier, String strSeparateurDeDecimal, String strDevise, int intNombreDeDecimales, boolean boolPartieDecimalAdmise, boolean boolDeviseAGaucheOuADroite)
	{
		String _strResult = "";
		String _strResultTemp = "";
		String _strEntireParty = "";
		String _strDecParty = "0.0";
		String _strRealDecParty = "";
		String _strStartRejectedDecParty = "";
		String strNbreToAdd = "0";

		try {

			_strResultTemp = String.valueOf(_decAmount);

			if(_strResultTemp == null || _strResultTemp.trim().length() == 0 || _strResultTemp.equalsIgnoreCase("null"))
				return "";

			_strResultTemp = _strResultTemp.trim();
			_strResultTemp = _strResultTemp.replace(" ", "");

			String[] arrResult = _strResultTemp.split("\\.");

			if(arrResult == null)
				return _strResult;

			_strEntireParty = arrResult[0];

			if(boolPartieDecimalAdmise)
			{
				if(arrResult.length > 1)
					_strDecParty = arrResult[1];

				if(_strDecParty != null && _strDecParty.trim().length() > 0)
				{
					if(_strDecParty.length() > intNombreDeDecimales)
					{
						_strRealDecParty = _strDecParty.substring(0, intNombreDeDecimales);
						_strStartRejectedDecParty = _strDecParty.substring(intNombreDeDecimales, intNombreDeDecimales + 1);
						if(Integer.parseInt(_strStartRejectedDecParty) > 5)
						{
							strNbreToAdd = "0.";
							for(int i = 0; i < intNombreDeDecimales - 1; i++)
							{
								strNbreToAdd += "0";
							}

							strNbreToAdd += "1";
						}
					}
					else
						_strRealDecParty = _strDecParty;

					_strRealDecParty = "0." + _strRealDecParty;

					try {
						String[] arrDecParties = _strRealDecParty.split("\\.");
						if(arrDecParties != null && arrDecParties.length >= 2)
							_strRealDecParty = arrDecParties[0] + "." + arrDecParties[1];
					} catch (Exception e) {
						e.printStackTrace();
					}

					_strDecParty = String.valueOf(new BigDecimal(_strRealDecParty).add(new BigDecimal(strNbreToAdd)));
				}
			}

			int _intEntirePartyLengh = _strEntireParty.length();

			if(_intEntirePartyLengh > 3)
			{
				String strEntPart1 = "";
				String strEntPart2 = "";
				String strEntPart3 = "";
				String strEntPart4 = "";
				String strEntPart5 = "";

				strEntPart1 = _strEntireParty.substring(_strEntireParty.length() - 3, _strEntireParty.length());
				strEntPart2 = _strEntireParty.substring(0, _strEntireParty.length() - 3);
				if(strEntPart2.length() <= 3)
					_strEntireParty = strEntPart2 + strSeparateurDeMillier + strEntPart1;
				else
				{
					strEntPart3 = strEntPart2.substring(0, strEntPart2.length() - 3);
					strEntPart2 = strEntPart2.substring(strEntPart2.length() - 3, strEntPart2.length());

					if(strEntPart3.length() <= 3)
						_strEntireParty = strEntPart3 + strSeparateurDeMillier + strEntPart2 + strSeparateurDeMillier + strEntPart1;
					else
					{
						strEntPart4 = strEntPart3.substring(0, strEntPart3.length() - 3);
						strEntPart3 = strEntPart3.substring(strEntPart3.length() - 3, strEntPart3.length());

						if(strEntPart4.length() <= 3)
							_strEntireParty = strEntPart4 + strSeparateurDeMillier + strEntPart3 + strSeparateurDeMillier + strEntPart2 + strSeparateurDeMillier + strEntPart1;
						else
						{
							strEntPart5 = strEntPart4.substring(0, strEntPart4.length() - 3);
							strEntPart4 = strEntPart4.substring(strEntPart4.length() - 3, strEntPart4.length());

							_strEntireParty = strEntPart5 + strSeparateurDeMillier + strEntPart4 + strSeparateurDeMillier + strEntPart3 + strSeparateurDeMillier + strEntPart2 + strSeparateurDeMillier + strEntPart1;
						}
					}
				}
			}

			if(!boolDeviseAGaucheOuADroite){
				if(boolPartieDecimalAdmise){
					String decPartyToTroncate = _strDecParty.split("\\.")[1];
					if(decPartyToTroncate != null && decPartyToTroncate.length() > 2)
						decPartyToTroncate = decPartyToTroncate.substring(0, 2);
					_strResult = _strEntireParty + strSeparateurDeDecimal + decPartyToTroncate + strDevise;
				}
				else
					_strResult = _strEntireParty + strDevise;
			}
			else{
				if(boolPartieDecimalAdmise){
					String decPartyToTroncate = _strDecParty.split("\\.")[1];
					if(decPartyToTroncate != null && decPartyToTroncate.length() > 2)
						decPartyToTroncate = decPartyToTroncate.substring(0, 2);
					_strResult = strDevise + _strEntireParty + strSeparateurDeDecimal + decPartyToTroncate;
				}
				else
					_strResult = strDevise + _strEntireParty;
			}


		} catch (Exception e) {
			e.printStackTrace();
		}

		return _strResult;
	}

	/**
	 * Get the next due date.
	 * @param dtDate
	 * @return
	 */
	public static Date _getNextDueDate(Date dtDate, List<Integer> oMoisNonRembs)
	{
		try {

			while(true)
			{
				dtDate = _getEndOfMonth(_getAddDay(_getEndOfMonth(dtDate), 1));
				if(_isMoishNonRemb(dtDate, oMoisNonRembs))
					continue;
				else
					break;
			}

			return dtDate;

		} catch (Exception e) {
			return dtDate;
		}
	}

	/**
	 * Chech if there is pay month.
	 * @param dtDate
	 * @param oMoisNonRembs
	 * @return
	 */
	private static boolean _isMoishNonRemb(Date dtDate, List<Integer> oMoisNonRembs)
	{
		try {

			if(oMoisNonRembs == null || oMoisNonRembs.size() == 0)
				return false;

			for(Integer intMonth : oMoisNonRembs)
			{
				if(intMonth.intValue() == (dtDate.getMonth() + 1))
					return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Get the end of the month.
	 * @param dtDate
	 * @return
	 */
	public static Date _getAddDay(Date dtDate, int day)
	{
		try {

			Calendar c1 = Calendar.getInstance();
			c1.setTime(dtDate);
			c1.add(Calendar.DATE, day);
			return c1.getTime();

		} catch (Exception e) {
			return dtDate;
		}
	}

	/**
	 * Get the end of the month.
	 * @param dtDate
	 * @return
	 */
	public static Date _getEndOfMonth(Date dtDate)
	{
		try {

			Calendar c1 = Calendar.getInstance();
			c1.setTime(dtDate);
			c1.add(Calendar.MONTH, 1);
			c1.add(Calendar.DATE, - c1.get(Calendar.DAY_OF_MONTH));
			return c1.getTime();

		} catch (Exception e) {
			return dtDate;
		}
	}

	/**
	 * Get the next due date.
	 * @param dtDate
	 * @return
	 */
	public static Date _getNextDueFirstDate(Date dtDate, List<Integer> oMoisNonRembs)
	{
		try {

			while(true)
			{
				if(_isMoishNonRemb(dtDate, oMoisNonRembs))
					dtDate = _getAddMonth(dtDate, 1);
				else
					break;
			}

			return dtDate;

		} catch (Exception e) {
			e.printStackTrace();
			return dtDate;
		}
	}

	/**
	 * Get the end of the month.
	 * @param dtDate
	 * @return
	 */
	public static Date _getAddMonth(Date dtDate, int month)
	{
		try {

			Calendar c1 = Calendar.getInstance();
			c1.setTime(dtDate);
			c1.add(Calendar.MONTH, month);
			return c1.getTime();

		} catch (Exception e) {
			return dtDate;
		}
	}

	// avec cette fonction, on peut decider du moment ou on affiche ou non les messages dans la console
	public static void println(Object obj)
	{
		System.out.println(obj);
		try
		{
//			String print = ClsParameter.getSessionObject(request, ClsParameter.SESSION_SHOW_CONSOLE);
//			if (StringUtils.equals("O", print))
//				System.out.println(obj);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getSessionObject(HttpServletRequest request, String strObjectName)
	{
		try
		{
			if(request==null)
				return "";
			Object object = request.getSession().getAttribute(strObjectName);
			if (object == null)
				return "";
			else
				return object.toString();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public static String _completerLongueurRubrique(String strCodeRubrique)
	{
		BigDecimal bd = new BigDecimal(strCodeRubrique);
		bd.setScale(0);
		strCodeRubrique = String.valueOf(bd.intValue());
		if(1==1) return StringUtil.oraLPad(strCodeRubrique, longueurRubrique,"0");
		if (strCodeRubrique.length() == 1)
			return "000" + strCodeRubrique;

		if (strCodeRubrique.length() == 2)
			return "00" + strCodeRubrique;

		if (strCodeRubrique.length() == 3)
			return "0" + strCodeRubrique;

		if (strCodeRubrique.length() == 4)
			return strCodeRubrique;

		return "";
	}

	public static boolean _isStringNull(String _strString)
	{
		if (_strString == null)
			return true;
		if (_strString != null && _strString.trim().length() == 0)
			return true;
		return false;
	}

	/**
	 * Invoke garbage collection.
	 *
	 */
	public static void __invokeGarbageCollection()
	{
		try {
//    		System.out.println("----------------Free memory size before-------" + ClsTools.__getFreeMemory());
			Runtime _o_Hrm_Rtme = Runtime.getRuntime();
			_o_Hrm_Rtme.gc();
//    		System.out.println("----------------Free memory size after-------" + ClsTools.__getFreeMemory());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static String getPhotoPath(HttpServletRequest request, GeneriqueConnexionService service, String matricule, String dossier, String langue)
	{
		String relPath = "photos/";

		// path = ClsConfigurationParameters.getConfigParameterValue(service, dossier, langue, ClsConfigurationParameters.CHEMIN_STOCKAGE_PHOTOS_SALARIE);

		File file = new File(relPath);

		// if(file.isAbsolute())
		// return ClsAgent.getPhotoFromAbsolutePath(path, matricule);

		String absPath = request.getSession().getServletContext().getRealPath("/"+relPath);
		if (!absPath.endsWith(File.separator))
			absPath += File.separator;

		if (!relPath.endsWith(File.separator))
			relPath += File.separator;

		return getPath(absPath, relPath, matricule);
	}

	public static String getPhotoPath(String matricule, HttpServletRequest request)
	{
		GeneriqueConnexionService service = (GeneriqueConnexionService) request.getSession().getAttribute("oservice");
		String strCodeDossier = ParameterUtil.getSessionObject(request, ParameterUtil.SESSION_DOSSIER);
		String strCodeLangue = ParameterUtil.getSessionObject(request, ParameterUtil.SESSION_LANGUE);

		return getPhotoPath(request, service, matricule, strCodeDossier, strCodeLangue);
	}

	// public static String getPhotoFromAbsolutePath(String absolutePath, String matricule)
	// {
	// if (!absolutePath.endsWith("\\"))
	// absolutePath += "\\";
	// return ClsAgent.getPath(absolutePath, matricule);
	// }

	public static String getPath(String path, String relPath, String matricule)
	{
		if (!showPhoto)
			return relPath + "photoorg.gif";

		if (1 == 1)
		{
			File file = new File(path);
			final String debut = matricule;
			File[] tf = file.listFiles(new FilenameFilter()
			{

				public boolean accept(File r, String n)
				{
					return n.startsWith(debut + ".");
				}
			});
			if (tf==null || tf.length == 0)
				return relPath + "photoorg.gif";

			int indexOfFile = tf[0].getAbsolutePath().lastIndexOf(File.separator);
			String fichierFinal = tf[0].getAbsolutePath().substring(indexOfFile + 1);
			return "photos/" + fichierFinal;

		}

		String strPath = path + matricule + ".png";
		String strRelPath = relPath + matricule + ".png";
		File file = new File(strPath);
		if (!file.exists())
		{
			strPath = path + matricule + ".gif";
			strRelPath = relPath + matricule + ".gif";
			file = new File(strPath);
			if (!file.exists())
			{
				strPath = path + matricule + ".jpeg";
				strRelPath = relPath + matricule + ".jpeg";
				file = new File(strPath);
				if (!file.exists())
				{
					strPath = path + matricule + ".jpg";
					strRelPath = relPath + matricule + ".jpg";
					file = new File(strPath);
					if (!file.exists())
					{
						strPath = path + matricule + ".JPG";
						strRelPath = relPath + matricule + ".JPG";
						file = new File(strPath);
						if (!file.exists())
							strRelPath = relPath + "photoorg.gif";
					}
				}
			}
		}
		try
		{
			System.out.println(file.getCanonicalPath());
			System.out.println(file.getCanonicalFile());
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strRelPath;
	}

	/**
	 * Retourne le chemin réelle du dossier contenant l'application.<br>
	 *
	 * @param request l'{@link HttpServletRequest} courant
	 * @return le chemin d'accès à l'application
	 */
	public static String getApplicationContextRealPath(HttpServletRequest request){
//    	return request.getRealPath(CommonFunctions.CHEMIN_GENERATION_FILES);
		return request.getSession().getServletContext().getRealPath("/"+ParameterUtil.CHEMIN_GENERATION_FILES);
	}

}
