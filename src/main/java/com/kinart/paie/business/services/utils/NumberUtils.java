package com.kinart.paie.business.services.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;


public class NumberUtils extends org.apache.commons.lang3.math.NumberUtils
{
	public static String toString(Number number)
	{
		if(number == null) return "";
		if(number == Long.valueOf(0)) return "0";
		//si pas de partie decimale
		if(number.longValue()-number.doubleValue()==0) return number.longValue()+"";
		return number.toString();
	}
	
	public static boolean NouZ(Number number)
	{
		if(number == null) return true;
		if(number == Long.valueOf(0)) return true;
		return false;
	}
	
	public static boolean notIn(Number number, Number... values)
	{
		for (int i = 0; i < values.length; i++)
		{
			if(StringUtils.equals(number.toString(),values[i].toString()))
				return false;
		}
		return true;
	}
	
	public static boolean in(Number number, Number... values)
	{
		return ! notIn(number, values);
	}
	
	public static Number nvl(Number number, Number remplacant)
	{
		if(number == null) 
			return remplacant;
		return number;
	}
	
	public static BigDecimal bdnvl(Number number, Number remplacant)
	{
		if(number == null) 
			return new BigDecimal(remplacant.doubleValue());
		return new BigDecimal(number.doubleValue());
	}
	
	public static Number nvl(Object number, Number remplacant)
	{
		if(number == null) 
			return remplacant;
		return (Number)number;
	}
	
	public static Number substractFrom(Number number, Number numberToSubstract)
	{
		return (Number)(number.doubleValue() - numberToSubstract.doubleValue());
	}
	
	public static Number substract(Number numberToSubstract,Number number)
	{
		return (Number)(number.doubleValue() - numberToSubstract.doubleValue());
	}
	
	public static BigDecimal convert(Number number)
	{
		return number == null ? null: new BigDecimal(number.doubleValue());
	}
	
	public static boolean isInteger(String val)
	{
		if (val.matches("^\\s*\\d+\\s*$"))
			return true;
		else
			return false;
	}
	
	/**
	 * Cette m�thode donne l'arrondi d'un nombre <b>val</b> avec
	 * <b>scale</b> chiffres apr�s la virgule
	 * @param val nombre � arrondir(type {@link BigDecimal})
	 * @param scale nombre de chiffres apr�s la virgule
	 * @return l'arrondi (type {@link BigDecimal}) 
	 * HALF-UP(si reste &gt= 0.5, alors par exc�s, sinon par d�faut)
	 */
	public static final BigDecimal round(BigDecimal val,int scale){
		BigDecimal ret=null;
		if(val!=null){
			ret=val.setScale(scale,RoundingMode.HALF_UP);
		}
		return ret;
	}
	/**
	 * Cette m�thode donne l'arrondi d'un nombre <b>val</b> avec
	 * <b>scale</b> chiffres apr�s la virgule
	 * @param val nombre � arrondir
	 * @param scale nombre de chiffres apr�s la virgule
	 * @return l'arrondi (type {@link BigDecimal})
	 * HALF-UP(si reste &gt= 0.5, alors par exc�s, sinon par d�faut)
	 */
	public static final BigDecimal round(double val,int scale){
		BigDecimal ret=null;
		if(val!=Double.NaN){
			ret=new BigDecimal(val).setScale(scale,RoundingMode.HALF_UP);
		}
		return ret;
	}
	
	public static final BigDecimal round(float val,int scale){
		BigDecimal ret=null;
		if(val!=Float.NaN){
			ret=new BigDecimal(val).setScale(scale,RoundingMode.HALF_UP);
		}
		return ret;
	}
	/**
	 * Cette m�thode donne l'arrondi d'un nombre <b>val</b> avec
	 * <b>scale</b> chiffres apr�s la virgule
	 * @param val nombre � arrondir(type {@link BigDecimal})
	 * @param scale nombre de chiffres apr�s la virgule
	 * @return l'arrondi (type {@link BigDecimal}) 
	 * HALF-UP(si reste &gt= 0.5, alors par exc�s, sinon par d�faut)
	 */
	public static final BigDecimal truncate(BigDecimal val,int scale){
		BigDecimal ret=null;
		if(val!=null){
			ret=val.setScale(scale,RoundingMode.DOWN);
		}
		return ret;
	}
	/**
	 * Cette m�thode donne l'arrondi d'un nombre <b>val</b> avec
	 * <b>scale</b> chiffres apr�s la virgule
	 * @param val nombre � arrondir
	 * @param scale nombre de chiffres apr�s la virgule
	 * @return l'arrondi (type {@link BigDecimal})
	 * HALF-UP(si reste &gt= 0.5, alors par exc�s, sinon par d�faut)
	 */
	public static final BigDecimal truncate(double val,int scale){
		BigDecimal ret=null;
		if(val!=Double.NaN){
			ret=new BigDecimal(val).setScale(scale,RoundingMode.DOWN);
		}
		return ret;
	}
	public static final double _truncate(double val,int scale)
	{
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(scale);            
		nf.setGroupingUsed(false);

		//return Double.valueOf(nf.format(val));
		try
		{
			return nf.parse(nf.format(val)).doubleValue();
		}
		catch (ParseException e)
		{		
			e.printStackTrace();
			return 0;
		}
	}
//	public static final double truncate(Number val,int scale)
//	{
//		NumberFormat nf = NumberFormat.getInstance();
//		nf.setMaximumFractionDigits(scale);            
//		nf.setGroupingUsed(false);
//
//		return Double.valueOf(nf.format(val));
//	}
	public static final BigDecimal zeroIfNull(BigDecimal arg){
		return arg==null?new BigDecimal(0):arg;
	}

	public String formatNddd(Number nombre, int nddd)
	{
		return NumberUtils.format(nombre, nddd);
	}
	public static final String format(Number nombre, int nddd)
	{
		return format(nombre, nddd, '.', ' ');
	}

	
	public static final String format(Number nombre, int nddd,char sepdec,char sepmille)
	{
		DecimalFormat df=new DecimalFormat();
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator(sepdec);
		dfs.setGroupingSeparator(sepmille);
		df.setDecimalFormatSymbols(dfs);
		df.setGroupingUsed(true);
		df.setMinimumFractionDigits(nddd);
		df.setMaximumFractionDigits(nddd);
		if(nombre == null) return StringUtils.EMPTY;
		return df.format(nombre);
	}
	
	public static final String formaterMontant(Number nombre, int nddd)
	{
		return formaterMontant(nombre, nddd, '.', ' ');
	}
	
	public static final String formaterMontant(Number nombre, int nddd, char csd, char csm)
	{
		if(nombre == null || (nombre != null && nombre.doubleValue() == 0)) return "";
		return NumberUtils.format(nombre, nddd,csd,csm);
	}
	
	/**
	 * Cette m�thode formate un nombre suivant le format pass� n param�tre
	 * @param n nombre � formatter
	 * @param format format � appliquer
	 * @return le nombre formatt�(risque d'exception si le format n'est pas valide)
	 */
	public static final String formatNumber(Number n,String format){
		String ret = "";
		DecimalFormat df = new DecimalFormat(format);
		ret = df.format(n);
		return ret;
	}
	
	public static final double toDouble(Number n)
	{
		if(n == null) return 0;
		return n.doubleValue();
	}
	
	public static final double toDouble(Object o)
	{
		return toDouble(ClsObjectUtil.getBigDecimalFromObject(o));
	}
	
	public static final double toDouble(String n)
	{
		if(n == null) return 0;
		return Double.valueOf(n.trim());
	}
	
	public static final int toInt(Number n)
	{
		if(n == null) return 0;
		return n.intValue();
	}
	
	public static final BigDecimal toBigDecimal(Number n)
	{
		if(n == null) return BigDecimal.ZERO;
		return new BigDecimal(n.doubleValue());
	}
	
}
