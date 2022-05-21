package com.kinart.paie.business.services.utils;

public class TypeBDUtil
{

	// SQL = norme SQL2
	// ASE = Sybase Adaptive Server Enterprise (15)
	// ASA = Sybase Adaptive Server Anywhere
	// MS = Microsoft SQL Server (2000)
	// OR = Oracle (10g)
	// DB = DB2-UDB (8.2)
	// AS = DB2-400 (V5R3)
	// MY = MySQL (4.1) (merci � Maximilian)
	// PG = PostgreSql (8.0)
	// IN = Informix

	public static String SQL = "SQL";

	public static String ASE = "ASE";

	public static String ASA = "ASA";

	public static String MS = "MS";

	public static String OR = "OR";

	public static String DB = "DB";

	public static String AS = "AS";

	public static String MY = "MY";

	public static String PG = "PG";

	public static String IN = "IN";

	public static String typeBD = MY;

	/**
	 * Fonctions standard de sql
	 */

	/**
	 * Concatenation de deux Chaines
	 */
//	public static String contatener(String... colonnes)
//	{
//		String result = StringUtils.EMPTY;
//		if (StringUtils.equals(typeBD, ClsTypeBD.OR) || StringUtils.equals(typeBD, ClsTypeBD.DB) || StringUtils.equals(typeBD, ClsTypeBD.AS) || StringUtils.equals(typeBD, ClsTypeBD.IN)
//				|| StringUtils.equals(typeBD, ClsTypeBD.ASA))
//		{
//			for (int i = 0; i < colonnes.length; i++)
//				if (StringUtils.isEmpty(result))
//					result = colonnes[i];
//				else
//					result += "||" + colonnes[i];
//			return result;
//		}
//
//		if (StringUtils.equals(typeBD, ClsTypeBD.MY) || StringUtils.equals(typeBD, ClsTypeBD.PG))
//		{
//			for (int i = 0; i < colonnes.length; i++)
//				if (StringUtils.isEmpty(result))
//					result = colonnes[i];
//				else
//					result += "," + colonnes[i];
//			return "concat(" + result + ")";
//		}
//
//		if (StringUtils.equals(typeBD, ClsTypeBD.MS) || StringUtils.equals(typeBD, ClsTypeBD.ASE))
//		{
//			for (int i = 0; i < colonnes.length; i++)
//				if (StringUtils.isEmpty(result))
//					result = colonnes[i];
//				else
//					result += "+" + colonnes[i];
//			return result;
//		}
//
//		for (int i = 0; i < colonnes.length; i++)
//			if (StringUtils.isEmpty(result))
//				result = colonnes[i];
//			else
//				result += "||" + colonnes[i];
//		return result;
//	}
//
//	/**
//	 * Fonctions standard de sql
//	 */
//
//	/**
//	 * coalesce
//	 */
//	public static String coalesce(String col1, String col2)
//	{
//		if (StringUtils.equals(typeBD, ClsTypeBD.IN))
//			return "nvl("+col1+","+col2+")";
//
//		return "coalesce("+col1+","+col2+")";
//	}
//
//	/**
//	 * Longueur d'une chaine
//	 */
//	public static String longueur(String chaine)
//	{
//		String result = StringUtils.EMPTY;
//		if (StringUtils.equals(typeBD, ClsTypeBD.OR) || StringUtils.equals(typeBD, ClsTypeBD.DB) || StringUtils.equals(typeBD, ClsTypeBD.AS) || StringUtils.equals(typeBD, ClsTypeBD.IN)
//				|| StringUtils.equals(typeBD, ClsTypeBD.ASA) || StringUtils.equals(typeBD, ClsTypeBD.MY) || StringUtils.equals(typeBD, ClsTypeBD.PG))
//		{
//			return "length(" + chaine + ")";
//		}
//
//		if (StringUtils.equals(typeBD, ClsTypeBD.MS) || StringUtils.equals(typeBD, ClsTypeBD.ASE))
//		{
//			return "datalength(" + chaine + ")";
//			// ou bien return "len("+chaine+")";
//		}
//
//		return StringUtils.isNotEmpty(chaine) ? String.valueOf(chaine.length()) : "0";
//	}
//
//	/**
//	 * Extraction d'une sous chaine
//	 */
//	public static String souschaine(String chaine, int depart, int longueur)
//	{
//		String result = StringUtils.EMPTY;
//		if (StringUtils.equals(typeBD, ClsTypeBD.OR) || StringUtils.equals(typeBD, ClsTypeBD.DB) || StringUtils.equals(typeBD, ClsTypeBD.AS) || StringUtils.equals(typeBD, ClsTypeBD.MY) || StringUtils.equals(typeBD, ClsTypeBD.IN))
//		{
//			return "substr(" + chaine + ","+depart+","+longueur+")";
//		}
//
//		if (StringUtils.equals(typeBD, ClsTypeBD.MS) || StringUtils.equals(typeBD, ClsTypeBD.ASE))
//		{
//			return "substring(" + chaine + ","+depart+","+longueur+")";
//		}
//
//		return chaine;
//	}
//
//	/**
//	 * Extraction d'une sous chaine
//	 */
//	public static String souschaine(String chaine, String depart, String longueur)
//	{
//		String result = StringUtils.EMPTY;
//		if (StringUtils.equals(typeBD, ClsTypeBD.OR) || StringUtils.equals(typeBD, ClsTypeBD.DB) || StringUtils.equals(typeBD, ClsTypeBD.AS) || StringUtils.equals(typeBD, ClsTypeBD.MY) || StringUtils.equals(typeBD, ClsTypeBD.IN))
//		{
//			return "substr(" + chaine + ","+depart+","+longueur+")";
//		}
//
//		if (StringUtils.equals(typeBD, ClsTypeBD.MS) || StringUtils.equals(typeBD, ClsTypeBD.ASE))
//		{
//			return "substring(" + chaine + ","+depart+","+longueur+")";
//		}
//
//		return chaine;
//	}
//
//	public static String convertirEnNumeric(String colonne)
//	{
//		String result = StringUtils.EMPTY;
//		if (StringUtils.equals(typeBD, ClsTypeBD.OR) || StringUtils.equals(typeBD, ClsTypeBD.DB) || StringUtils.equals(typeBD, ClsTypeBD.AS) || StringUtils.equals(typeBD, ClsTypeBD.MY) || StringUtils.equals(typeBD, ClsTypeBD.IN))
//		{
//			return "cast(" + colonne + " as Decimal)";
//		}
//
//		if (StringUtils.equals(typeBD, ClsTypeBD.MS) || StringUtils.equals(typeBD, ClsTypeBD.ASE))
//		{
//			return "cast(" + colonne + " as Numeric)";
//		}
//
//		return colonne;
//	}
//	public static String convertirEnChaineDeCaractere(String colonne)
//	{
//		if (StringUtils.equals(typeBD, ClsTypeBD.DB))
//		{
//			return "'" + colonne + "'";
//		}
//
//		return colonne ;
//	}
//
//	public static String castEnChaine(String colonne)
//	{
//		if (StringUtils.equals(typeBD, ClsTypeBD.DB) || StringUtils.equals(typeBD, ClsTypeBD.MS) || StringUtils.equals(typeBD, ClsTypeBD.IN))
//			return "cast(" + colonne + " as varchar(100))";
//
//		if (StringUtils.equals(typeBD, ClsTypeBD.OR))
//			return "cast(" + colonne + " as varchar2(100))";
//
//		if (StringUtils.equals(typeBD, ClsTypeBD.MY))
//			return "concat("+colonne+",'')";
//
//
//		return colonne ;
//	}
//
//	public static String convertirEnChaineDeCaractere1(String colonne)
//	{
//		if (StringUtils.equals(typeBD, ClsTypeBD.DB))
//		{
//			return "'" + colonne + "'";
//		}
//
//		return "'" + colonne + "'";
//	}
//	public static String convertirEnChaineDeCaractere2(String colonne)
//	{
//		if (StringUtils.equals(typeBD, ClsTypeBD.DB))
//		{
//			return "char(" + colonne + ")";
//		}
//
//		return colonne;
//	}
//
//	public static String to_char(String colonne, String format)
//	{
//		if (StringUtils.equals(typeBD, ClsTypeBD.OR) || StringUtils.equals(typeBD, ClsTypeBD.MY))
//		{
//			return "to_char(" + colonne + ",'"+format+"')";
//		}
//
//		if (StringUtils.equals(typeBD, ClsTypeBD.DB) || StringUtils.equals(typeBD, ClsTypeBD.AS))
//		{
//			return "varchar_format(" + colonne + ",'"+format+"')";
//		}
//
//		if (StringUtils.equals(typeBD, ClsTypeBD.MS) || StringUtils.equals(typeBD, ClsTypeBD.ASE))
//		{
//			//return "cast(" + colonne + " as Numeric)";
//		}
//		return colonne;
//	}
//
//	public static String currentdate()
//	{
//		if (StringUtils.equals(typeBD, ClsTypeBD.OR))
//		{
//			return "sysdate";
//		}
//
//		if (StringUtils.equals(typeBD, ClsTypeBD.MY))
//		{
//			return "current_date";
//		}
//
//		if (StringUtils.equals(typeBD, ClsTypeBD.DB) || StringUtils.equals(typeBD, ClsTypeBD.AS))
//		{
//			return "current date";
//		}
//
//		if (StringUtils.equals(typeBD, ClsTypeBD.MS) || StringUtils.equals(typeBD, ClsTypeBD.ASE))
//		{
//			//return "cast(" + colonne + " as Numeric)";
//		}
//		return "";
//	}
//
//	public static String to_date(String colonne, String format)
//	{
//		if (StringUtils.equals(typeBD, ClsTypeBD.OR) || StringUtils.equals(typeBD, ClsTypeBD.DB) || StringUtils.equals(typeBD, ClsTypeBD.AS) || StringUtils.equals(typeBD, ClsTypeBD.MY))
//		{
//			return "to_date(" + colonne + ",'"+format+"')";
//		}
//
//		if (StringUtils.equals(typeBD, ClsTypeBD.MS) || StringUtils.equals(typeBD, ClsTypeBD.ASE))
//		{
//			//return "cast(" + colonne + " as Numeric)";
//		}
//		return colonne;
//	}
//
//	public static String sqlPad(String toPad,int length,char padChar, boolean left){
//		String ret=EMPTY,arg=isBlank(toPad)?EMPTY:toPad,mPad=EMPTY;
//		for(int i=1;i<=length;i++){
//			mPad=mPad+padChar;
//		}
//		arg=contatener("''",arg);
//		String slen=length+"-"+longueur(arg),
//		substr=souschaine("'"+mPad+"'", 1, Integer.MIN_VALUE).replaceAll(""+Integer.MIN_VALUE, slen);
//		ret=left?contatener(substr,arg):contatener(arg,substr);
////		ret="concat(substr( '0000', 1, 4-length(''||c.valm)), ''||c.valm )";
//		return ret;
//	}
//
//	public static String trunc(String colonne,String format)
//	{
//		if (StringUtils.equals(typeBD, ClsTypeBD.MS))
//		{
//
//			return "dbo.formaterDateEnChaine("+colonne+","+format+")";
//		}
//
//		if (StringUtils.equals(typeBD, ClsTypeBD.OR) || StringUtils.equals(typeBD, ClsTypeBD.DB) || StringUtils.equals(typeBD, ClsTypeBD.AS) || StringUtils.equals(typeBD, ClsTypeBD.MY))
//		{
//			return "trunc(" +colonne+")";
//		}
//		return colonne;
//	}
//	public static void main(String[] args){
//		System.out.println(sqlPad("2", 4, '+', true));
//	}
}
