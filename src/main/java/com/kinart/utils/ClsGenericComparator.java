package com.kinart.utils;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;

import com.kinart.paie.business.services.calendrier.ClsEnumTypes;
import org.springframework.util.StringUtils;


	public class ClsGenericComparator implements Comparator{
		
		private String compareColumn;
		private String compareOrder;
		boolean isKeyColumn;
		private String comparekeyColumn;
//		private Rhpagent object;
//		private Object object;
		private Class c;
//		private Class pkc;
		
		
		
		

//		public synchronized Class getPkc() {
//			return pkc;
//		}
//
//		public synchronized void setPkc(Class pkc) {
//			this.pkc = pkc;
//		}

		public synchronized Class getC() {
			return c;
		}

		public synchronized void setC(Class c) {
			this.c = c;
		}

//		public Object getObject() {
//			return object;
//		}
//
//		public void setObject(Object object) {
//			this.object = object;
//		}

		public String getComparekeyColumn() {
			return comparekeyColumn;
		}

		public void setComparekeyColumn(String comparekeyColumn) {
			this.comparekeyColumn = comparekeyColumn;
		}

		public boolean isKeyColumn() {
			return isKeyColumn;
		}

		public void setKeyColumn(boolean isKeyColumn) {
			this.isKeyColumn = isKeyColumn;
		}

		public String getCompareColumn() {
			return compareColumn;
		}

		public void setCompareColumn(String compareColumn) {
			this.compareColumn = compareColumn;
		}

		public String getCompareOrder() {
			return compareOrder;
		}

		public void setCompareOrder(String compareOrder) {
			this.compareOrder = compareOrder;
		}
		public ClsGenericComparator(){
			
		}

//		public ClsRhtnomComparator(Object object,String compareColumn,String compareOrder, boolean isKeyColumn,Object keyObject){
//			this.setObject(object);
//			this.setCompareColumn("get"+StringUtils.capitalize(compareColumn.toLowerCase()));
//			this.setCompareOrder(compareOrder);
//			this.setKeyColumn(isKeyColumn);
//			this.setKeyObject(keyObject);
//			if(this.isKeyColumn()){
//				this.setObject(keyObject);
//			}
//		}
		
		public ClsGenericComparator(Class c, String compareColumn, String compareOrder){
			this.setCompareColumn("get" + StringUtils.capitalize(compareColumn));
			this.setCompareOrder(compareOrder);
			
//			this.setObject(o);
			this.setC(c);
//			this.setPkc(pkc);
			//
			if(_isKeyColumn(compareColumn)){
				this.setKeyColumn(true);
				this.setCompareColumn("getComp_id");
				//System.out.println("..........................."+"Get key column = "+_getKeyColumn(compareColumn));
				this.setComparekeyColumn("get" + StringUtils.capitalize(_getKeyColumn(compareColumn)));
			}
			else	this.setKeyColumn(false);
		}
		
		public ClsGenericComparator(Class c, String compareColumn){
			this.setCompareColumn("get" + StringUtils.capitalize(compareColumn));
			this.setCompareOrder("ASC");
			
//			this.setObject(o);
			this.setC(c);
//			this.setPkc(pkc);
			//
			if(_isKeyColumn(compareColumn)){
				this.setKeyColumn(true);
				this.setCompareColumn("getComp_id");
				//System.out.println("..........................."+"Get key column = "+_getKeyColumn(compareColumn));
				this.setComparekeyColumn("get" + StringUtils.capitalize(_getKeyColumn(compareColumn)));
			}
			else	this.setKeyColumn(false);
		}
		
		private boolean _isKeyColumn(String strColumnName){
			
			if(strColumnName.toLowerCase().indexOf("comp_id")!=-1) return true;
			return false;
		}
		
		private String _getKeyColumn(String strColumnName){
			strColumnName = strColumnName.trim();
			//int indexDuPoint = strColumnName.indexOf(".");
			//System.out.println("..........................."+"Position du point = "+indexDuPoint);
			return strColumnName.substring(8, strColumnName.length());
			//return strColumnName.replace("comp_id.", "");
		}
		
		
		public int compare(Object o1, Object o2) {
//			Rhpagent oo1 = (Rhpagent)o1;
//			Rhpagent oo2 = (Rhpagent)o2;
			if(this.isKeyColumn()) return  compareKey( o1,  o2);
			if(! this.isKeyColumn()) return  compareNormal( o1,  o2);
			return 0;
		}
		
		public int compareNormal(Object o1, Object o2) {
			try {
				Method method = this.getC().getMethod(this.getCompareColumn(), null);
				//System.out.println("..........................."+"Method name = "+method.getName());
				String returnType = method.getReturnType().getSimpleName();
				//System.out.println("..........................."+"return tye = "+returnType);

				if(ClsEnumTypes.BIGDECIMAL.equalsIgnoreCase(returnType)) return this.bigDecimalComparaison(method, o1, o2);
				if(ClsEnumTypes.NUMBER.equalsIgnoreCase(returnType)) return this.bigDecimalComparaison(method, o1, o2);
				if(ClsEnumTypes.LONG.equalsIgnoreCase(returnType)) return this.longComparaison(method, o1, o2);
				if(ClsEnumTypes.DATE.equalsIgnoreCase(returnType))	 return this.dateComparaison(method, o1, o2);
				if(ClsEnumTypes.DOUBLE.equalsIgnoreCase(returnType))	 return this.doubleComparaison(method, o1, o2);
				if(ClsEnumTypes.FLOAT.equalsIgnoreCase(returnType))	 return this.floatComparaison(method, o1, o2);
				if(ClsEnumTypes.INTEGER.equalsIgnoreCase(returnType))	 return this.integerComparaison(method, o1, o2);
				if(ClsEnumTypes.INT.equalsIgnoreCase(returnType))	 return this.integerComparaison(method, o1, o2);
				if(ClsEnumTypes.STRING.equalsIgnoreCase(returnType))	 return this.stringComparaison(method, o1, o2);
				
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
			return 0;
		}
		
		public int compareKey(Object o1, Object o2) {
			try {
				if(o1 == null && o2 != null)
					return 1;
				else if(o1 != null && o2 == null)
						return -1;
				else if(o1 == null && o2 == null)
					return 0;
				Method method = this.getC().getMethod(this.getCompareColumn(), null);
//				System.out.println("..........................."+"Method name of comp_id = " + method.getName());
				Class returnTypeClass = method.getReturnType();
//				System.out.println("..........................."+"return tye of comp_id= "+returnTypeClass.toString());
				
				Object opk1 = method.invoke(o1); 
				Object opk2 = method.invoke(o2);  
				
				method = returnTypeClass.getMethod(this.getComparekeyColumn(), null);
//				System.out.println("..........................."+"Method name in pk = "+method.getName());
				String returnType = method.getReturnType().getSimpleName();
//				System.out.println("..........................."+"return tye in pk = "+returnType);

					if(ClsEnumTypes.BIGDECIMAL.equalsIgnoreCase(returnType)) return this.bigDecimalKeyComparaison(method, opk1, opk2);
					if(ClsEnumTypes.NUMBER.equalsIgnoreCase(returnType)) return this.bigDecimalComparaison(method, opk1, opk2);
					if(ClsEnumTypes.LONG.equalsIgnoreCase(returnType)) return this.longComparaison(method, opk1, opk2);
					if(ClsEnumTypes.DATE.equalsIgnoreCase(returnType))	 return this.dateKeyComparaison(method, opk1, opk2);
					if(ClsEnumTypes.DOUBLE.equalsIgnoreCase(returnType))	 return this.doubleKeyComparaison(method, opk1, opk2);
					if(ClsEnumTypes.FLOAT.equalsIgnoreCase(returnType))	 return this.floatKeyComparaison(method, opk1, opk2);
					if(ClsEnumTypes.INTEGER.equalsIgnoreCase(returnType))	 return this.integerKeyComparaison(method, opk1, opk2);
					if(ClsEnumTypes.INT.equalsIgnoreCase(returnType))	 return this.integerKeyComparaison(method, opk1, opk2);
					if(ClsEnumTypes.STRING.equalsIgnoreCase(returnType))	 return this.stringKeyComparaison(method, opk1, opk2);
				
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
			return 0;
		}
		
		
		private int bigDecimalComparaison(Method method, Object o1, Object o2) throws Exception{ 
			BigDecimal other=(BigDecimal)method.invoke(o2); 
			BigDecimal current =(BigDecimal) method.invoke(o1); 
			
			
			if("ASC".equalsIgnoreCase(this.getCompareOrder())){
					if(other==null){
						if(current==null) return 0;
						if(current!=null) return 1;
					}
					
					if(current==null){
						if(other==null) return 0;
						if(other!=null) return -1;
					}
				
				 if(current.compareTo(other)<0) return -1;
			      else if(current.compareTo(other)==0) return 0;
			      else return 1; 
				}else{
					
					if(other==null){
						if(current==null) return 0;
						if(current!=null) return -1;
					}
					
					if(current==null){
						if(other==null) return 0;
						if(other!=null) return 1;
					}
					 if(current.compareTo(other)<0) return 1;
				      else if(current.compareTo(other)==0) return 0;
				      else return -1; 
				}
		   } 
		
		private int longComparaison(Method method, Object o1, Object o2) throws Exception{ 
			Long other=(Long)method.invoke(o2); 
			Long current =(Long) method.invoke(o1); 
			
			
			if("ASC".equalsIgnoreCase(this.getCompareOrder())){
					if(other==null){
						if(current==null) return 0;
						if(current!=null) return 1;
					}
					
					if(current==null){
						if(other==null) return 0;
						if(other!=null) return -1;
					}
				
				 if(current.compareTo(other)<0) return -1;
			      else if(current.compareTo(other)==0) return 0;
			      else return 1; 
				}else{
					
					if(other==null){
						if(current==null) return 0;
						if(current!=null) return -1;
					}
					
					if(current==null){
						if(other==null) return 0;
						if(other!=null) return 1;
					}
					 if(current.compareTo(other)<0) return 1;
				      else if(current.compareTo(other)==0) return 0;
				      else return -1; 
				}
		   } 
		
		

		
		private int integerComparaison(Method method, Object o1, Object o2) throws Exception{ 
			Integer other=(Integer)method.invoke(o2); 
			Integer current =(Integer) method.invoke(o1); 
			if("ASC".equalsIgnoreCase(this.getCompareOrder())){
				if(other==null){
					if(current==null) return 0;
					if(current!=null) return 1;
				}
				
				if(current==null){
					if(other==null) return 0;
					if(other!=null) return -1;
				}	
			 if(current.compareTo(other)<0) return -1;
		      else if(current.compareTo(other)==0) return 0;
		      else return 1; 
			}else{
				if(other==null){
					if(current==null) return 0;
					if(current!=null) return -1;
				}
				
				if(current==null){
					if(other==null) return 0;
					if(other!=null) return 1;
				}
				 if(current.compareTo(other)<0) return 1;
			      else if(current.compareTo(other)==0) return 0;
			      else return -1; 
			}
		   } 
		
		protected int stringComparaison(Method method, Object o1, Object o2) throws Exception{ 
			String other=(String)method.invoke(o2); 
			String current =(String) method.invoke(o1); 
			if("ASC".equalsIgnoreCase(this.getCompareOrder())){
				if(other==null){
					if(current==null) return 0;
					if(current!=null) return 1;
				}
				
				if(current==null){
					if(other==null) return 0;
					if(other!=null) return -1;
				}
			 if(current.compareToIgnoreCase(other)<0) return -1;
		      else if(current.compareToIgnoreCase(other)==0) return 0;
		      else return 1; 
			}else{
				if(other==null){
					if(current==null) return 0;
					if(current!=null) return -1;
				}
				
				if(current==null){
					if(other==null) return 0;
					if(other!=null) return 1;
				}
				 if(current.compareToIgnoreCase(other)<0) return 1;
			      else if(current.compareToIgnoreCase(other)==0) return 0;
			      else return -1; 
			}
		   } 
		
		private int dateComparaison(Method method, Object o1, Object o2) throws Exception{ 
			Date other=(Date)method.invoke(o2); 
			Date current =(Date) method.invoke(o1); 
			if("ASC".equalsIgnoreCase(this.getCompareOrder())){
				if(other==null){
					if(current==null) return 0;
					if(current!=null) return 1;
				}
				
				if(current==null){
					if(other==null) return 0;
					if(other!=null) return -1;
				}
			 if(current.compareTo(other)<0) return -1;
		      else if(current.compareTo(other)==0) return 0;
		      else return 1; 
			}else{
				if(other==null){
					if(current==null) return 0;
					if(current!=null) return -1;
				}
				
				if(current==null){
					if(other==null) return 0;
					if(other!=null) return 1;
				}
				 if(current.compareTo(other)<0) return 1;
			      else if(current.compareTo(other)==0) return 0;
			      else return -1; 
			}
		   } 
		
		private int doubleComparaison(Method method, Object o1, Object o2) throws Exception{ 
			Double other=(Double)method.invoke(o2); 
			Double current =(Double) method.invoke(o1); 
			if("ASC".equalsIgnoreCase(this.getCompareOrder())){
				if(other==null){
					if(current==null) return 0;
					if(current!=null) return 1;
				}
				
				if(current==null){
					if(other==null) return 0;
					if(other!=null) return -1;
				}
			 if(current.compareTo(other)<0) return -1;
		      else if(current.compareTo(other)==0) return 0;
		      else return 1; 
			}else{
				if(other==null){
					if(current==null) return 0;
					if(current!=null) return -1;
				}
				
				if(current==null){
					if(other==null) return 0;
					if(other!=null) return 1;
				}
				 if(current.compareTo(other)<0) return 1;
			      else if(current.compareTo(other)==0) return 0;
			      else return -1; 
			}
		   } 
		
		private int floatComparaison(Method method, Object o1, Object o2) throws Exception{ 
			Float other=(Float)method.invoke(o2); 
			Float current =(Float) method.invoke(o1); 
			if("ASC".equalsIgnoreCase(this.getCompareOrder())){
				if(other==null){
					if(current==null) return 0;
					if(current!=null) return 1;
				}
				
				if(current==null){
					if(other==null) return 0;
					if(other!=null) return -1;
				}
			 if(current.compareTo(other)<0) return -1;
		      else if(current.compareTo(other)==0) return 0;
		      else return 1; 
			}else{
				if(other==null){
					if(current==null) return 0;
					if(current!=null) return -1;
				}
				
				if(current==null){
					if(other==null) return 0;
					if(other!=null) return 1;
				}
				 if(current.compareTo(other)<0) return 1;
			      else if(current.compareTo(other)==0) return 0;
			      else return -1; 
			}
		   } 
		
		
		
		private int bigDecimalKeyComparaison(Method method, Object o1, Object o2) throws Exception{
			
			if("DESC".equalsIgnoreCase(this.getCompareOrder())){
				if(o1 == null && o2 != null)
					return 1;
				else if(o1 != null && o2 == null)
						return -1;
				else if(o1 == null && o2 == null)
					return 0;
			}else{
				if(o1 == null && o2 != null)
					return -1;
				else if(o1 != null && o2 == null)
						return 1;
				else if(o1 == null && o2 == null)
					return 0;
			}
//			Object opk1 = method.invoke(o1); 
//			Object opk2 = method.invoke(o2);  
//			Method methodpk = this.getPkc().getMethod(this.getComparekeyColumn(), null); 
			BigDecimal other=(BigDecimal)method.invoke(o1); 
			BigDecimal current =(BigDecimal) method.invoke(o2); 
			if("DESC".equalsIgnoreCase(this.getCompareOrder())){
				if(other==null){
					if(current==null) return 0;
					if(current!=null) return 1;
				}
				
				if(current==null){
					if(other==null) return 0;
					if(other!=null) return -1;
				}
			 if(current.compareTo(other)<0) return -1;
		      else if(current.compareTo(other)==0) return 0;
		      else return 1; 
			}else{
				if(other==null){
					if(current==null) return 0;
					if(current!=null) return -1;
				}
				
				if(current==null){
					if(other==null) return 0;
					if(other!=null) return 1;
				}
				 if(current.compareTo(other)<0) return 1;
			      else if(current.compareTo(other)==0) return 0;
			      else return -1; 
			}
		   } 
		
		private int integerKeyComparaison(Method method, Object o1, Object o2) throws Exception{ 
			
			if("DESC".equalsIgnoreCase(this.getCompareOrder())){
				if(o1 == null && o2 != null)
					return 1;
				else if(o1 != null && o2 == null)
						return -1;
				else if(o1 == null && o2 == null)
					return 0;
			}else{
				if(o1 == null && o2 != null)
					return -1;
				else if(o1 != null && o2 == null)
						return 1;
				else if(o1 == null && o2 == null)
					return 0;
			}
//			Object opk1 = method.invoke(o1); 
//			Object opk2 = method.invoke(o2);  
//			Method methodpk = this.getPkc().getMethod(this.getComparekeyColumn(), null); 
			Integer other=(Integer)method.invoke(o1); 
			Integer current =(Integer) method.invoke(o2); 
			if("DESC".equalsIgnoreCase(this.getCompareOrder())){
				if(other==null){
					if(current==null) return 0;
					if(current!=null) return 1;
				}
				
				if(current==null){
					if(other==null) return 0;
					if(other!=null) return -1;
				}
			 if(current.compareTo(other)<0) return -1;
		      else if(current.compareTo(other)==0) return 0;
		      else return 1; 
			}else{
				if(other==null){
					if(current==null) return 0;
					if(current!=null) return -1;
				}
				
				if(current==null){
					if(other==null) return 0;
					if(other!=null) return 1;
				}
				 if(current.compareTo(other)<0) return 1;
			      else if(current.compareTo(other)==0) return 0;
			      else return -1; 
			}
		   } 
		
		private int stringKeyComparaison(Method method, Object o1, Object o2) throws Exception{
			
			if("DESC".equalsIgnoreCase(this.getCompareOrder())){
				if(o1 == null && o2 != null)
					return 1;
				else if(o1 != null && o2 == null)
						return -1;
				else if(o1 == null && o2 == null)
					return 0;
			}else{
				if(o1 == null && o2 != null)
					return -1;
				else if(o1 != null && o2 == null)
						return 1;
				else if(o1 == null && o2 == null)
					return 0;
			} 
			
			String other=(String)method.invoke(o1); 
			String current =(String) method.invoke(o2); 
			if("DESC".equalsIgnoreCase(this.getCompareOrder())){
				if(other==null){
					if(current==null) return 0;
					if(current!=null) return 1;
				}
				
				if(current==null){
					if(other==null) return 0;
					if(other!=null) return -1;
				}
			 if(current.compareToIgnoreCase(other)<0) return -1;
		      else if(current.compareToIgnoreCase(other)==0) return 0;
		      else return 1; 
			}else{
				if(other==null){
					if(current==null) return 0;
					if(current!=null) return -1;
				}
				
				if(current==null){
					if(other==null) return 0;
					if(other!=null) return 1;
				}
				 if(current.compareToIgnoreCase(other)<0) return 1;
			      else if(current.compareToIgnoreCase(other)==0) return 0;
			      else return -1; 
			}
		   } 
		
		private int dateKeyComparaison(Method method, Object o1, Object o2) throws Exception{
//			Object opk1 = method.invoke(o1); 
//			Object opk2 = method.invoke(o2);  
//			Method methodpk = this.getPkc().getMethod(this.getComparekeyColumn(), null); 
			
			if("DESC".equalsIgnoreCase(this.getCompareOrder())){
				if(o1 == null && o2 != null)
					return 1;
				else if(o1 != null && o2 == null)
						return -1;
				else if(o1 == null && o2 == null)
					return 0;
			}else{
				if(o1 == null && o2 != null)
					return -1;
				else if(o1 != null && o2 == null)
						return 1;
				else if(o1 == null && o2 == null)
					return 0;
			}
			Date other=(Date)method.invoke(o1); 
			Date current =(Date) method.invoke(o2); 
			if("DESC".equalsIgnoreCase(this.getCompareOrder())){
				if(other==null){
					if(current==null) return 0;
					if(current!=null) return 1;
				}
				
				if(current==null){
					if(other==null) return 0;
					if(other!=null) return -1;
				}
			 if(current.compareTo(other)<0) return -1;
		      else if(current.compareTo(other)==0) return 0;
		      else return 1; 
			}else{
				if(other==null){
					if(current==null) return 0;
					if(current!=null) return -1;
				}
				
				if(current==null){
					if(other==null) return 0;
					if(other!=null) return 1;
				}
				 if(current.compareTo(other)<0) return 1;
			      else if(current.compareTo(other)==0) return 0;
			      else return -1; 
			}
		   } 
		
		private int doubleKeyComparaison(Method method, Object o1, Object o2) throws Exception{ 
//			Method methodpk = this.getPkc().getMethod("getComp_id", null); 
//			Object opk1 = methodpk.invoke(o1); 
//			Object opk2 = methodpk.invoke(o2); 
			
			if("DESC".equalsIgnoreCase(this.getCompareOrder())){
				if(o1 == null && o2 != null)
					return 1;
				else if(o1 != null && o2 == null)
						return -1;
				else if(o1 == null && o2 == null)
					return 0;
			}else{
				if(o1 == null && o2 != null)
					return -1;
				else if(o1 != null && o2 == null)
						return 1;
				else if(o1 == null && o2 == null)
					return 0;
			}
			Double other=(Double)method.invoke(o1); 
			Double current =(Double) method.invoke(o2); 
			if("DESC".equalsIgnoreCase(this.getCompareOrder())){
				if(other==null){
					if(current==null) return 0;
					if(current!=null) return 1;
				}
				
				if(current==null){
					if(other==null) return 0;
					if(other!=null) return -1;
				}
			 if(current.compareTo(other)<0) return -1;
		      else if(current.compareTo(other)==0) return 0;
		      else return 1; 
			}else{
				if(other==null){
					if(current==null) return 0;
					if(current!=null) return -1;
				}
				
				if(current==null){
					if(other==null) return 0;
					if(other!=null) return 1;
				}
				 if(current.compareTo(other)<0) return 1;
			      else if(current.compareTo(other)==0) return 0;
			      else return -1; 
			}
		   }
		
		private int floatKeyComparaison(Method method, Object o1, Object o2) throws Exception{ 
//			Method methodpk = this.getPkc().getMethod("getComp_id", null); 
//			Object opk1 = methodpk.invoke(o1); 
//			Object opk2 = methodpk.invoke(o2); 
			
			if("DESC".equalsIgnoreCase(this.getCompareOrder())){
				if(o1 == null && o2 != null)
					return 1;
				else if(o1 != null && o2 == null)
						return -1;
				else if(o1 == null && o2 == null)
					return 0;
			}else{
				if(o1 == null && o2 != null)
					return -1;
				else if(o1 != null && o2 == null)
						return 1;
				else if(o1 == null && o2 == null)
					return 0;
			}
			Float other=(Float)method.invoke(o1); 
			Float current =(Float) method.invoke(o2); 
			if("DESC".equalsIgnoreCase(this.getCompareOrder())){
				if(other==null){
					if(current==null) return 0;
					if(current!=null) return 1;
				}
				
				if(current==null){
					if(other==null) return 0;
					if(other!=null) return -1;
				}
			 if(current.compareTo(other)<0) return -1;
		      else if(current.compareTo(other)==0) return 0;
		      else return 1; 
			}else{
				if(other==null){
					if(current==null) return 0;
					if(current!=null) return -1;
				}
				
				if(current==null){
					if(other==null) return 0;
					if(other!=null) return 1;
				}
				 if(current.compareTo(other)<0) return 1;
			      else if(current.compareTo(other)==0) return 0;
			      else return -1; 
			}
		}
	}
