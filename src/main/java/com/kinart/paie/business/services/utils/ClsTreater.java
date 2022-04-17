/**
 * 
 */

package com.kinart.paie.business.services.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author c.mbassi
 * 
 */
public class ClsTreater
{

	private static final Logger log = LogManager.getLogger(ClsTreater.class);
	public static String RESULTAT = "_o_result";

	/**
	 * Concatener deux messages pour en faire un seul
	 * 
	 * @return
	 */

	public static ClsResultat _concat(ClsResultat _o_Result_Start, ClsResultat _o_Result_End, String _strSeparator)
	{
		ClsResultat _o_Result = new ClsResultat();

		if (_o_Result_Start == null)
			return _o_Result_End;

		if (_o_Result_End == null)
			return _o_Result_Start;

		if (_strSeparator == null)
			_strSeparator = ParameterUtil.CHAINE_VIDE;

		if (StringUtils.isBlank(_o_Result_Start.getLibelle()))
			_o_Result_Start.setLibelle(ParameterUtil.CHAINE_VIDE);

		if (StringUtils.isBlank(_o_Result_End.getLibelle()))
			_o_Result_End.setLibelle(ParameterUtil.CHAINE_VIDE);

		if (StringUtils.isBlank(_o_Result_Start.getCode()))
			_o_Result_Start.setCode(_o_Result_Start.getLibelle());

		if (StringUtils.isBlank(_o_Result_End.getCode()))
			_o_Result_End.setCode(_o_Result_End.getLibelle());

		_o_Result.setCode(_o_Result_Start.getCode().concat(ParameterUtil.CHAINE_ESPACE).concat(_o_Result_End.getCode()));

		_o_Result.setLibelle(_o_Result_Start.getLibelle().concat(_strSeparator).concat(_o_Result_End.getLibelle()));

		if (_o_Result_End.isErrormessage())
		{
			_o_Result.setTable(_o_Result_End.getTable());
			_o_Result.setTr(_o_Result_End.getTr());
			_o_Result.setTd(_o_Result_End.getTd());
			_o_Result.setIsErrormessage(true);
		}

		return _o_Result;
	}


	/**
	 * Renvoi un objet resultat � partir du code
	 * 
	 * @param _strDescription
	 * @param _strErrorCode
	 * @param bIsError
	 * @return
	 */

	@SuppressWarnings("unchecked")
	public static ClsResultat _getResultat(String _strDescription, String _strErrorCode, boolean bIsError)
	{
		String libelle = _strDescription;

		String strTableStyle;

		String strTrStyle;

		String strTdStyle;
		
		libelle = _getParametizedMessageValue(_strDescription);

		if (bIsError == true)
		{
			strTableStyle = "errortable";
			strTdStyle = "errortd";
			strTrStyle = "errortr";
		}
		else
		{
			strTableStyle = "succestable";
			strTdStyle = "succestd";
			strTrStyle = "succestr";
		}

		return new ClsResultat(bIsError, _strErrorCode, libelle, _strDescription, strTableStyle, strTrStyle, strTdStyle);
	}

	/**
	 * Cas d'un message param�tr�, permet d'obtenir l'objet resultat si le message contient des param�tres
	 * 
	 * @param _strDescription
	 * @param _strErrorCode
	 * @param bIsError
	 * @param arrayParmetersValues
	 * @return
	 */

	@SuppressWarnings("unchecked")
	public static ClsResultat _getResultat(String _strDescription, String _strErrorCode, boolean bIsError, String... arrayParmetersValues)
	{
		String libelle = _strDescription;

		String strTableStyle;
		String strTrStyle;
		String strTdStyle;
		
		libelle = _getParametizedMessageValue(_strDescription, arrayParmetersValues);

		if (bIsError == true)
		{
			strTableStyle = "errortable";
			strTdStyle = "errortd";
			strTrStyle = "errortr";
		}
		else
		{
			strTableStyle = "succestable";
			strTdStyle = "succestd";
			strTrStyle = "succestr";
		}

		return new ClsResultat(bIsError, _strErrorCode, libelle, _strDescription, strTableStyle, strTrStyle, strTdStyle);
	}

	//	@SuppressWarnings("unchecked")
	//	public static ClsResultat _getResultat(HttpServletRequest request, String _strDescription, String _strErrorCode, boolean bIsError)
	//	{
	//		
	//		Map<EvmsgPK, String> libelles = new HashMap<EvmsgPK, String>();
	//
	//		Object oMap = null;
	//		if(request!=null)
	//			oMap=request.getSession().getAttribute(ClsParameter.SESSION_LIBELLES);
	//
	//		String clang = ClsParameter.getSessionObject(request, ClsParameter.SESSION_LANGUE);
	//
	//		String libelle = "";
	//
	//		String strTableStyle;
	//
	//		String strTrStyle;
	//
	//		String strTdStyle;
	//
	//		if (oMap != null && _strErrorCode != null)
	//		{
	//
	//			libelles = (Map<EvmsgPK, String>) oMap;
	//
	//			EvmsgPK pk = new EvmsgPK(_strErrorCode, clang);
	//
	//			if (libelles.get(pk) != null)
	//				libelle = _getParametizedMessageValue(libelles.get(pk));
	//			else
	//				libelle = _getParametizedMessageValue(_strDescription);
	//		}
	//		else
	//			libelle = _getParametizedMessageValue(_strDescription);
	//
	//		if (bIsError == true)
	//		{
	//			strTableStyle = "errortable";
	//			strTdStyle = "errortd";
	//			strTrStyle = "errortr";
	//		}
	//		else
	//		{
	//			strTableStyle = "succestable";
	//			strTdStyle = "succestd";
	//			strTrStyle = "succestr";
	//		}
	//
	//		return new ClsResultat(bIsError, _strErrorCode, libelle, _strDescription, strTableStyle, strTrStyle, strTdStyle);
	//	}
	//
	//	/**
	//	 * Cas d'un message param�tr�, permet d'obtenir l'objet resultat si le message contient des param�tres
	//	 * 
	//	 * @param request
	//	 * @param _strDescription
	//	 * @param _strErrorCode
	//	 * @param bIsError
	//	 * @param arrayParmetersValues
	//	 * @return
	//	 */
	//	
	//
	//	@SuppressWarnings("unchecked")
	//	public static ClsResultat _getResultat(HttpServletRequest request, String _strDescription, String _strErrorCode, boolean bIsError,
	//			String... arrayParmetersValues)
	//	{
	//
	//		Map<EvmsgPK, String> libelles = new HashMap<EvmsgPK, String>();
	//		Object oMap = null;
	//		if(request!=null)
	//			oMap=request.getSession().getAttribute(ClsParameter.SESSION_LIBELLES);
	//		String clang = ClsParameter.getSessionObject(request, ClsParameter.SESSION_LANGUE);
	//		String libelle = "";
	//
	//		String strTableStyle;
	//		String strTrStyle;
	//		String strTdStyle;
	//
	//		if (oMap != null && _strErrorCode != null)
	//		{
	//			libelles = (Map<EvmsgPK, String>) oMap;
	//			EvmsgPK pk = new EvmsgPK(_strErrorCode, clang);
	//
	//			if (libelles.get(pk) != null)
	//				libelle = _getParametizedMessageValue(libelles.get(pk), arrayParmetersValues);
	//			else
	//				libelle = _getParametizedMessageValue(_strDescription, arrayParmetersValues);
	//		}
	//		else
	//			libelle = _getParametizedMessageValue(_strDescription, arrayParmetersValues);
	//
	//		if (bIsError == true)
	//		{
	//			strTableStyle = "errortable";
	//			strTdStyle = "errortd";
	//			strTrStyle = "errortr";
	//		}
	//		else
	//		{
	//			strTableStyle = "succestable";
	//			strTdStyle = "succestd";
	//			strTrStyle = "succestr";
	//		}
	//
	//		return new ClsResultat(bIsError, _strErrorCode, libelle, _strDescription, strTableStyle, strTrStyle, strTdStyle);
	//	}
	/**
	 * Formater le message issu de la console pour l'afficher � l'utilisateur
	 * 
	 * @return
	 */
	public static String _getStackTrace(Exception _exception_)
	{

		String _strErrorMessage = "";

		if (StringUtils.isBlank(_strErrorMessage) && _exception_.getCause() != null)
		{
			Throwable o_th = _exception_.getCause();
			if (o_th != null)
				_strErrorMessage += o_th.toString();
		}
		//		
		if (StringUtils.isBlank(_strErrorMessage) && _exception_.getMessage() != null)
			_strErrorMessage += _exception_.getMessage();

		if (StringUtils.isBlank(_strErrorMessage) && _exception_.getLocalizedMessage() != null)
			_strErrorMessage += _exception_.getLocalizedMessage();

		//		if (ClsParameter._isStringNull(_strErrorMessage) && _exception_.fillInStackTrace()!= null)
		//			_strErrorMessage = _exception_.fillInStackTrace().toString();

		if (StringUtils.isBlank(_strErrorMessage))
		{
			_strErrorMessage = _exception_.toString();
		}

		if (_exception_.getStackTrace() != null)
		{
			for (StackTraceElement str : _exception_.getStackTrace())
				if (str != null)
					_strErrorMessage += "\r\n ->" + str.toString();
		}

		//		System.out.println("Message d'erreur dans stack trace de clstreater = " + _strErrorMessage);
		log.info("Message d'erreur dans stack trace de clstreater = " + _strErrorMessage);

		if (StringUtils.isBlank(_strErrorMessage))
		{
			_strErrorMessage = _exception_.toString();
		}
		/*
		 * System.out.println("Message d'erreur getCause= "+_exception_.getMessage()); System.out.println("Message d'erreur getMessage=
		 * "+_exception_.getMessage()); System.out.println("Message d'erreur getLocalizedMessage= "+_exception_.getLocalizedMessage());
		 * System.out.println("Message d'erreur fillInStackTrace().toString()= "+_exception_.fillInStackTrace().toString());
		 */

		/*
		 * String[] tokens = StringTokenizerUtils.tokenizeToArray(_strErrorMessage, ":"); int size = tokens.length; if ( size == 0 ) return null; else return
		 * tokens[size - 1];
		 */
		return StringUtils.substring(_strErrorMessage, 0, 999);
	}

	public static String _getParametizedMessageValue(String _strMessageWithParameters, String... arrayParmetersValues)
	{

		if (arrayParmetersValues == null || _strMessageWithParameters == null)
			return _strMessageWithParameters;

		int j = 0;

		for (int i = 0; i < arrayParmetersValues.length; ++i)
		{

			j += 1;

			_strMessageWithParameters = _strMessageWithParameters.replaceFirst("%" + j, arrayParmetersValues[i]);

		}

		for (int i = 0; i < arrayParmetersValues.length; ++i)
		{

			_strMessageWithParameters = _strMessageWithParameters.replaceFirst("%", arrayParmetersValues[i]);

		}

		return _strMessageWithParameters;
	}

	/**
	 * Apres un traitement, utiliser cette methode pour fixer la valeur du message de retour
	 */

	public static void _setEndResult(ClsResultat _o_Result)
	{

		//request.getSession().setAttribute(RESULTAT, _o_Result);

	}

	/**
	 * Apres un traitement, utiliser cette methode pour r�cup�rer la valeur du message de retour a envoyer sur le formulaire
	 */
	public static ClsResultat _getEndResult()
	{

		//ClsResultat result = (ClsResultat) request.getSession().getAttribute(RESULTAT);

		//request.getSession().removeAttribute(RESULTAT);

		return null;//result;
	}

	public static String _getMessage(String codeMessage)
	{
		ClsResultat oResult = ClsTreater._getResultat("Message " + codeMessage + " inexistant en base", codeMessage, false);
		return oResult.getLibelle();
	}

	public static String _getMessage(String _strDescription, String _strErrorCode)
	{
		ClsResultat oResult = ClsTreater._getResultat(_strDescription, _strErrorCode, false);
		return oResult.getLibelle();
	}

}
