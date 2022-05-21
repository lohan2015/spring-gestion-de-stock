package com.kinart.paie.business.services.utils;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ClsObjectUtil {

	
	public static Double getDoubleFromObject(Object o){
		Double res = null;
		if(o == null)
			return new Double(0);
		if(o instanceof BigDecimal){
			res = ((BigDecimal) o).doubleValue() ;
		}else if (o instanceof Double){
			res = (Double)o;
		}else if (o instanceof Float){
			res = ((Float)o).doubleValue();
		}else if (o instanceof Long){
			res = ((Long)o).doubleValue();
		}else if (o instanceof Integer){
			res = ((Integer)o).doubleValue();
		}else if (o instanceof Short){
			res = ((Short)o).doubleValue();
		}else if (o instanceof String){
			res = Double.parseDouble(o.toString());
		}else{
			res=(Double)o;//risky
		}
//		if(o instanceof String)
//			return Double.parseDouble(o.toString());
//		return o instanceof BigDecimal ? ((BigDecimal)o).doubleValue() : (Double)o;
		return res;
	}
	
	public static Long getLongFromObject(Object o){
		Long res = null;
		if(o == null)
			return new Long(0);
		if(o instanceof BigDecimal){
			res = ((BigDecimal) o).longValue() ;
		}else if (o instanceof Double){
			res = ((Double)o).longValue();
		}else if (o instanceof Float){
			res = ((Float)o).longValue();
		}else if (o instanceof Long){
			res = (Long)o;
		}else if (o instanceof Integer){
			res = ((Integer)o).longValue();
		}else if (o instanceof Short){
			res = ((Short)o).longValue();
		}else if (o instanceof String){
			res = new Long((String)o);
		}else{
			res=(Long)o;//risky
		}
		return res;
//		return o instanceof BigDecimal ? ((BigDecimal)o).longValue() : (Long)o;
	}
	
	public static BigDecimal getBigDecimalFromObject(Object o){
		BigDecimal res = null; 
		if(o == null)
			return new BigDecimal(0);
		if(o instanceof BigDecimal){
			res = (BigDecimal) o ;
		}else if (o instanceof Double){
			res = BigDecimal.valueOf((Double)o);
		}else if (o instanceof Float){
			res = BigDecimal.valueOf((Float)o);
		}else if (o instanceof Long){
			res = BigDecimal.valueOf((Long)o);
		}else if (o instanceof Integer){
			res = BigDecimal.valueOf((Integer)o);
		}else if (o instanceof Short){
			res = BigDecimal.valueOf((Short)o);
		}else if (o instanceof String){
			res = new BigDecimal((String)o);
		}else{
			res=(BigDecimal)o;//risky
		}
		return res;
	}
	
	public static Integer getIntegerFromObject(Object o){
		if(o == null)
			return new Integer(0);
		int i = 0;
		if(o instanceof BigDecimal)
			i = ((BigDecimal)o).intValue();
		else if(o instanceof Double)
			i = ((Double)o).intValue();
		else if(o instanceof Float)
			i = ((Float)o).intValue();
		else if(o instanceof Long)
			i = ((Long)o).intValue();
		else if(o instanceof Short)
			i = ((Short)o).intValue();
		else if(o instanceof String){
			if(StringUtils.isEmpty(o.toString()))
				return 0;
			i = Integer.parseInt(o.toString());
		}
		else
			i = (Integer)o;
		
		return i;
	}
	
	/**
	 * compare le premier argument aux autres
	 * @param value la valeur � comparer aux autres
	 * @param values le tableau qui contient les valeurs auxquelles on fait la comparaison
	 * @return
	 */
	public static boolean isAppliedToStrings(String value, String ...values){
		boolean applied = false;
		if(value == null && value == ""){
			for (int i = 0; i < values.length; i++) {
		        applied = (values[i] == null && values[i] == "");
		        if(! applied)
		        	return (! applied);
		    }
		}
		else if(value != ""){
			for (int i = 0; i < values.length; i++) {
		        applied = values[i].equals(value);
		        if(applied)
		        	return (applied);
		    }
		}		
		//
		return applied;
	}
	
	/**
	 * compare le premier argument aux autres
	 * @param value la valeur � comparer aux autres
	 * @param values le tableau qui contient les valeurs auxquelles on fait la comparaison
	 * @return
	 */
	public static boolean isAppliedToInteger(int value, int ...values){
		boolean applied = false;
		if(value < 0){
			for (int i = 0; i < values.length; i++) {
		        applied = (values[i] < 0);
		        if(! applied)
		        	return (! applied);
		    }
		}
		else{
			for (int i = 0; i < values.length; i++) {
		        applied = (values[i ]== value);
		        if(applied)
		        	return (applied);
		    }
		}		
		//
		return applied;
	}
	
	/**
	 * compare le premier argument aux autres
	 * @param value la valeur � comparer aux autres
	 * @param values le tableau qui contient les valeurs auxquelles on fait la comparaison
	 * @return
	 */
	public static boolean isAppliedToObject(Object value, Object ...values){
		boolean applied = true;
		if(isNull(value)) return applied;
		//si toutes les valeurs sont nulles alors true
		for (int i = 0; i < values.length; i++) {
			//com.cdi.deltarh.service.ClsParameter.println("Valeur à rechercher chez le salarié = "+values[i]);
			applied = applied && (isNull(values[i]));
		}
		//si la valeur à comparer n'est pas nulle alors retourner true
		if(! applied){
			if(isNull(value))
				return false;
		}
		else
			return true;
		//
		applied = false;
		for (int i = 0; i < values.length; i++) {
			if(values[i] == null){
//					applied = true;
				continue;
			}
			applied = value.toString().equals(values[i].toString());
			if(applied)
				return (applied);
		}
//		}
		//
		return applied;
	}
	
	/**
	 * Verify that this argument is null
	 * @param obj
	 * @return
	 */
	public static boolean isNull(Object obj){
		boolean flag = false;
		//
		if(obj instanceof BigDecimal){
			if(obj == null || ((BigDecimal)obj).doubleValue() == 0)
				flag = true;
		}
		else if(obj instanceof Integer){
			if(obj == null || ((Integer)obj) == 0)
				flag = true;
		}
		else if(obj instanceof Long){
			if(obj == null || ((Long)obj) == 0)
				flag = true;
		}
		else if(obj instanceof Double){
			if(obj == null || ((Double)obj) == 0)
				flag = true;
		}
		else if(obj instanceof Float){
			if(obj == null || ((Float)obj) == 0)
				flag = true;
		}
		else if(obj instanceof String){
			if(obj == null || "".equalsIgnoreCase(((String)obj).trim()))
			    flag = true;//StringUtils.isEmpty((String)obj);
		}
		else if(obj instanceof Date){
			if(obj == null)
				flag = true;
		}
		return flag;
	}
	
	public static boolean NouZ(Number number)
	{
		if(number == null) return true;
		if(number == Long.valueOf(0)) return true;
		return false;
	}
	
	/**
	 * v�rifie si la liste des pr�ts est vide
	 * @param rubrique
	 * @return true   si la liste est vide
	 */
	public static boolean isListEmty(List l){
		return (l == null || l.size() == 0);
	}
	
	public static Object callMethode(Object obj, String nomMethode, Class classe, Object valeurParam) throws Exception
	{
		if(obj == null) return null;
		try
		{
			Method oMethod = obj.getClass().getMethod(nomMethode, classe);
			if (oMethod != null) return  oMethod.invoke(obj, valeurParam);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return null;
	}
	
	public static Object callGetMethode(Object obj, String nomMethode) throws Exception
	{
		if(obj == null) return null;
		try
		{
			Method oMethod = obj.getClass().getMethod(nomMethode);
			if (oMethod != null) return  oMethod.invoke(obj);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return null;
	}
	
}
