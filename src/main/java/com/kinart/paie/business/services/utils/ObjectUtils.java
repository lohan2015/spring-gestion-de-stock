package com.kinart.paie.business.services.utils;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.reflect.Field;
import java.math.BigDecimal;


public class ObjectUtils extends org.apache.commons.lang3.ObjectUtils
{
	public static Object setNull(Object object)
	{
		ReferenceQueue<Object> queue = new ReferenceQueue<Object>();
		
		PhantomReference<Object> ref = new PhantomReference<Object>(object, queue);
		
		try
		{
			
			
			Field[] fields = object.getClass().getDeclaredFields();
			
			for (Field field : fields)
			{
				//if( ! field.getClass().isInstance(Session.class.newInstance()))
				field = null;
			}
			
			Field[] fields2 = object.getClass().getFields();
			
			for (Field field : fields2)
			{
				field = null;
			}
			object = null;
			
			if(queue.poll() != null)
				queue.poll().clear();
			
			ref.clear();
			
			return object;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			
			return null;
		}
	}
	
	public static boolean isNull(Object obj){
		return (obj == null) ? true : false;
	}
	
	public static boolean isNotNull(Object obj){
		return !isNull(obj);
	}

   public static BigDecimal ZEROIFNULL(BigDecimal b){
	return b==null?BigDecimal.ZERO:b;	   
   }
}
