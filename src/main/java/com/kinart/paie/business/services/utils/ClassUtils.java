/**
 * 
 */
package com.kinart.paie.business.services.utils;
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static java.util.Collections.*;

import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Pattern;
import java.io.OutputStream;
import java.io.InputStream;


//import org.springframework.beans.BeanUtils;







/**
* @author c.mbassi
*
*/
public final class ClassUtils {
	
	private static final Map<String, Object> LOCK_MAP=synchronizedMap(new Hashtable<String, Object>());
	
	private static final Logger log = LogManager.getLogger(ClassUtils.class);
	
	/**
	   * Renvoie true si @attribut est un attribut de la classe @nomComplet
	   * @param nomComplet
	   * @param atribut
	   * @return
	   */
	public static boolean estAttribut(Object nomComplet, String atribut) {
		  if(nomComplet == null){
			  return true;
		  }
		  try{
			  for(Field f : nomComplet.getClass().getDeclaredFields()){
				  
					if(f.getType().getName().startsWith("com.cdi")){
						
						return estAttribut(f.getType().newInstance(), atribut);
					}
					
					if(f.getName().equalsIgnoreCase(atribut)){
						return true;
					}
				}
		  }catch(InstantiationException e){
			  e.printStackTrace();
		  }catch(IllegalAccessException e){
			  e.printStackTrace();
		  }
			
			return false;
	}
	/**
	 * Cette m�thode renvoie un objet qui peut �tre utilis� comme verrou de
	 * synchronisation sur un dossier.
	 * @param cdos le dossier concern�
	 * @return le dossier
	 */
	public static final synchronized Object getLock(String cdos){
		Object lock=LOCK_MAP.get(cdos);
		if(lock==null){
			lock=new Object();
			LOCK_MAP.put(cdos, lock);
		}
		return lock;
	}
	/**
	 * Cette m�thode retourne le nom de la classe qui l'invoque.<br/>
	 * <strong><b>attention!</b></strong>En cas de probl�me, elle retourne le nom de la classe {@link Object}
	 * @return le nom de la classe appelante.
	 */
	public static final String currentClassName(){
		String ret = "",cun=ClassUtils.class.getName();
		int depth=0;
		try {
			StackTraceElement[] st=Thread.currentThread().getStackTrace();
			for(StackTraceElement ste:st){
				if(ste.getClassName().equalsIgnoreCase(cun))
					depth++;
				else
					break;
			}
			ret = depth<st.length-1?st[1+depth].getClassName():Object.class.getName();
		} catch (Exception e) {
//			e.printStackTrace();
			log.error(e.getLocalizedMessage(),e);
			ret=Object.class.getName();
		}
		return ret;
	}
	/**
	 * Cette m�thode permet de modifier les attributs d'un objet Java.<br/>
	 * <b>ATTENTION!</b>Elle ne fonctionnera que si les conventions de nommage de Java
	 * ont �t� respect�es.
	 * @param target l'objet cible(dont on veut modifier l'attribut)
	 * @param property l'attribut � modifier
	 * @param value la valeur que l'on veut affecter
	 * @return <b>true</b> si tout se passe bien et <b>false</b> sinon!
	 */
	@SuppressWarnings("unchecked")
	public static final boolean set(Object target,String property,Object value){
		boolean test=true;
		Class pclass=exactType(target, property);
		if(target==null||pclass==null||property==null||"".equals(property.trim()))
			return false;
		Class tclass=target.getClass();
		StringBuilder msb=new StringBuilder(property.replaceAll("\\s", ""));
		msb.setCharAt(0, Character.toUpperCase(msb.charAt(0)));
		try {
			Method setter=tclass.getMethod("set"+msb.toString(), pclass);
			setter.invoke(target, value);
		} catch (SecurityException e) {
//			e.printStackTrace();
			log.error(e.getLocalizedMessage(),e);
			test=false;
		} catch (NoSuchMethodException e) {
//			e.printStackTrace();
			log.error(e.getLocalizedMessage(),e);
			test=false;
		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
			log.error(e.getLocalizedMessage(),e);
			test=false;
		} catch (IllegalAccessException e) {
//			e.printStackTrace();
			log.error(e.getLocalizedMessage(),e);
			test=false;
		} catch (InvocationTargetException e) {
//			e.printStackTrace();
			log.error(e.getLocalizedMessage(),e);
			test=false;
		}
		return test;
	}
	/**
	 * Cette m�thode permet de modifier les attributs d'un objet Java.<br/>
	 * <b>ATTENTION!</b>Elle ne fonctionnera que si les conventions de nommage de Java
	 * ont �t� respect�es.
	 * @param target l'objet cible(dont on veut modifier l'attribut)
	 * @param property l'attribut � modifier
	 * @param pclass le type exact de cet attribut
	 * @param value la valeur que l'on veut affecter
	 * @return <b>true</b> si tout se passe bien et <b>false</b> sinon!
	 */
	@SuppressWarnings("unchecked")
	public static final boolean set(Object target,String property,Class pclass,Object value){
		boolean test=true;
		if(target==null||pclass==null||property==null||"".equals(property.trim()))
			return false;
		Class tclass=target.getClass();
		StringBuilder msb=new StringBuilder(property.replaceAll("\\s", ""));
		msb.setCharAt(0, Character.toUpperCase(msb.charAt(0)));
		try {
			Method setter=tclass.getMethod("set"+msb.toString(), pclass);
			setter.invoke(target, value);
		} catch (SecurityException e) {
//			e.printStackTrace();
			log.error(e.getLocalizedMessage(),e);
			test=false;
		} catch (NoSuchMethodException e) {
//			e.printStackTrace();
			log.error(e.getLocalizedMessage(),e);
			test=false;
		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
			log.error(e.getLocalizedMessage(),e);
			test=false;
		} catch (IllegalAccessException e) {
//			e.printStackTrace();
			log.error(e.getLocalizedMessage(),e);
			test=false;
		} catch (InvocationTargetException e) {
//			e.printStackTrace();
			log.error(e.getLocalizedMessage(),e);
			test=false;
		}
		return test;
	}
	/**
	 * Cette m�thode permet d'obtenir les attributs d'un objet Java.
	 * ATTENTION!Elle ne fonctionnera que si les conventions de nommage de Java
	 * ont �t� respect�es.
	 * @param target l'objet cible(contenant l'attribut)
	 * @param property l'attribut dont on veut la valeur
	 * @param pclass le type exact de cet attribut
	 * @return la valeur de l'attribut<br/>
	 * <b>En cas de probl�me, cette m�thode renvoie null!!!</b>
	 */
	@SuppressWarnings("unchecked")
	public static final Object get(Object target,String property,Class pclass){
		Object ret=null;
		if(target==null||pclass==null||property==null||"".equals(property.trim()))
			return null;
		Class tclass=target.getClass();
		StringBuilder msb=new StringBuilder(property.replaceAll("\\s", ""));
		msb.setCharAt(0, Character.toUpperCase(msb.charAt(0)));
		String prefix=(pclass.equals(boolean.class)||pclass.equals(Boolean.class))?"is":"get";
		try {
			Method getter=tclass.getMethod(prefix+msb.toString(), (Class[])null);
			ret=getter.invoke(target,(Object[]) null);
		} catch (SecurityException e) {
//			e.printStackTrace();
			log.error(e.getLocalizedMessage(),e);
		} catch (NoSuchMethodException e) {
//			e.printStackTrace();
			log.error(e.getLocalizedMessage(),e);
		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
			log.error(e.getLocalizedMessage(),e);
		} catch (IllegalAccessException e) {
//			e.printStackTrace();
			log.error(e.getLocalizedMessage(),e);
		} catch (InvocationTargetException e) {
//			e.printStackTrace();
			log.error(e.getLocalizedMessage(),e);
		}
		return ret;
	}
	/**
	 * Cette m�thode permet d'obtenir les attributs d'un objet Java.
	 * ATTENTION!Elle ne fonctionnera que si les conventions de nommage de Java
	 * ont �t� respect�es.
	 * @param target l'objet cible(contenant l'attribut)
	 * @param property l'attribut dont on veut la valeur
	 * @return la valeur de l'attribut<br/>
	 * <b>En cas de probl�me, cette m�thode renvoie null!!!</b>
	 */
	@SuppressWarnings("unchecked")
	public static final Object get(Object target,String property){
		Object ret=null;
		if(target==null||property==null||"".equals(property.trim()))
			return null;
		Class tclass=target.getClass();
		//boolean error=false;
		StringBuilder msb=new StringBuilder(property.replaceAll("\\s", ""));
		msb.setCharAt(0, Character.toUpperCase(msb.charAt(0)));
		Class pclass=exactType(target, property);
		if(pclass==null)
			return null;
		String prefix=(pclass.equals(boolean.class)||pclass.equals(Boolean.class))?"is":"get";
		try {
			Method getter=tclass.getMethod(prefix+msb.toString(), (Class[])null);
			ret=getter.invoke(target,(Object[]) null);
		} catch (SecurityException e) {
			//e.printStackTrace();
			//error=true;
			log.error(e.getLocalizedMessage(),e);
		} catch (NoSuchMethodException e) {
			//e.printStackTrace();
			//error=true;
			log.error(e.getLocalizedMessage(),e);
		} catch (IllegalArgumentException e) {
			//e.printStackTrace();
			//error=true;
			log.error(e.getLocalizedMessage(),e);
		} catch (IllegalAccessException e) {
			//e.printStackTrace();
			//error=true;
			log.error(e.getLocalizedMessage(),e);
		} catch (InvocationTargetException e) {
			//e.printStackTrace();
			//error=true;
			log.error(e.getLocalizedMessage(),e);
		}
		/*if(error){
			prefix="is";
			try {
				Method getter=tclass.getMethod(prefix+msb.toString(), (Class[])null);
				ret=getter.invoke(target,(Object[]) null);
			} catch (SecurityException e) {
				e.printStackTrace();
				//error=true;
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
				//error=true;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				//error=true;
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				//error=true;
			} catch (InvocationTargetException e) {
				e.printStackTrace();
				//error=true;
			}
		}*/
		return ret;
	}
	/**
	 * Cette m�thode permet d'obtenir la valeur d'une expression point�e Java(par exemple <b>comp_id.cdos</b>).
	 * ATTENTION!Elle ne fonctionnera que si les conventions de nommage de Java
	 * ont �t� respect�es.Les crochets ne sont pas g�r�s.
	 * @param target l'objet cible(contenant l'attribut)
	 * @param expr l'expression dont on veut la valeur
	 * @return la valeur de l'attribut<br/>
	 * <b>En cas de probl�me, cette m�thode renvoie null!!!</b>
	 */
	@SuppressWarnings("unchecked")
	public static final Object evaluate(Object target,String expr){
		Object ret=null;
		if(target==null||expr==null||"".equals(expr.trim())||!Pattern.matches("([\\S&&[^\\.]])+(\\.([\\S&&[^\\.]])+)*", expr.trim()))
			return null;
		String[] tab=expr.split("\\.");
		Object temp=target;
		for(int i=0;i<tab.length;i++){
			temp=get(temp, tab[i]);
			if(temp==null){
				break;
			}
		}
		ret=temp;
		return ret;
	}
	/**
	 * Cette m�thode permet de modifier la valeur d'une expression point�e Java(par exemple <b>comp_id.cdos</b>).
	 * ATTENTION!Elle ne fonctionnera que si les conventions de nommage de Java
	 * ont �t� respect�es.Les crochets ne sont pas g�r�s.
	 * @param target l'objet cible(contenant l'attribut)
	 * @param expr l'expression dont on veut la valeur
	 * @param value la valeur que l'on veut affecter
	 * @return <b>true</b> si tout se passe bien et <b>false</b> sinon!
	 */
	@SuppressWarnings("unchecked")
	public static final boolean modify(Object target,String expr,Object value){
		boolean test=true;
		if(target==null||expr==null||"".equals(expr.trim())||!Pattern.matches("([\\S&&[^\\.]])+(\\.([\\S&&[^\\.]])+)*", expr.trim()))
			return false;
		String[] tab=expr.split("\\.");
		Object temp=target;
		for(int i=0;i<tab.length-1;i++){
			temp=get(temp, tab[i]);
			if(temp==null){
				break;
			}
		}
		test=set(temp, tab[tab.length-1], value);
		return test;
	}
	/**
	 * Cette m�thode permet d'obtenir le type de l'attribut d'un objet Java.
	 * ATTENTION!Elle ne fonctionnera que si les conventions de nommage de Java
	 * ont �t� respect�es.
	 * @param target l'objet cible(contenant l'attribut)
	 * @param property l'attribut dont on veut la valeur
	 * @param pclass le type exact de cet attribut
	 * @return la valeur de l'attribut<br/>
	 * <b>En cas de probl�me, cette m�thode renvoie null!!!</b>
	 */
	@SuppressWarnings("unchecked")
	public static final Class exactType(Object target,String property){
		Class ret=null;
		if(target==null||property==null||"".equals(property.trim()))
			return null;
		Class tclass=target.getClass();
		boolean error=false;
		StringBuilder msb=new StringBuilder(property.replaceAll("\\s", ""));
		msb.setCharAt(0, Character.toUpperCase(msb.charAt(0)));
		String prefix="get";
		try {
			Method getter=tclass.getMethod(prefix+msb.toString(), (Class[])null);
			ret=getter.getReturnType();
		} catch (SecurityException e) {
			 //e.printStackTrace();
			error=true;
		} catch (NoSuchMethodException e) {
			//e.printStackTrace();
			error=true;
		} catch (IllegalArgumentException e) {
			//e.printStackTrace();
			error=true;
		} 
		if(error){
			prefix="is";
			try {
				Method getter=tclass.getMethod(prefix+msb.toString(), (Class[])null);
				ret=getter.getReturnType();
			} catch (SecurityException e) {
//				e.printStackTrace();
				log.error(e.getLocalizedMessage(),e);
				//error=true;
			} catch (NoSuchMethodException e) {
//				e.printStackTrace();
				log.error(e.getLocalizedMessage(),e);
				//error=true;
			} catch (IllegalArgumentException e) {
//				e.printStackTrace();
				log.error(e.getLocalizedMessage(),e);
				//error=true;
			} 
		}
		return ret;
	}
	/**
	 * Cette m�thode permet de convertir une cha�ne de caract�res en l'un des types {@link Integer},<br/>
	 * {@link BigDecimal},{@link Date} et {@link String}
	 * @param strval la cha�ne � convertir
	 * @param pclass le type d'arriv�e(valeurs autoris�es : null,{@link Integer},
	 * {@link BigDecimal},{@link Date} et {@link String})
	 * @param date_format le format de la date(utile si le type d'arriv�e est {@link Date})
	 * @return l'objet converti ou <b>null</b> si la classe d'arriv�e sp�cifi�e est null!
	 * @throws Exception si l'objet n'est pas convertible ou si le type d'arriv�e n'est pas g�r�
	 */
	@SuppressWarnings("unchecked")
	public static final Object convert(String strval,Class pclass,String date_format) throws Exception{
		Object ret=null;
		if(pclass==null)
			return ret;
		if(pclass.equals(String.class)){
			return strval;
		}
		else if(pclass.equals(Integer.class)){
			ret=new Integer(strval);
		}
		else if(pclass.equals(BigDecimal.class)){
			ret=new BigDecimal(strval);
		}else if(pclass.equals(Date.class)){
			DateFormat df=new SimpleDateFormat(date_format);
			if(strval==null||StringUtils.isEmpty(strval)||StringUtils.isBlank(strval))
				return null;
			return df.parse(strval);
		}else
			throw new UnsupportedOperationException("The java type "+pclass.getName()+" is not supported!");
		return ret;
	}

	/**
   * Returns a copy of the object, or null if the object cannot
   * be serialized.
   * Warning! If <b>orig</b> is <b>null</b>,an exception will be thrown!
   * @param orig the object to copy or source object
   * @return a copy of the object
   */
  public static Object deepCopy(Object orig) {
  	if(orig==null){
  		throw new RuntimeException("The source object must not be null!");
  	}
  	Object obj = null;
  	if(orig instanceof Serializable){
	        try {
	            // Write the object out to a byte array
	            FastByteArrayOutputStream fbos =
	                    new FastByteArrayOutputStream();
	            ObjectOutputStream out = new ObjectOutputStream(fbos);
	            out.writeObject(orig);
	            out.flush();
	            out.close();
	
	            // Retrieve an input stream from the byte array and read
	            // a copy of the object back in.
	            ObjectInputStream in =
	                new ObjectInputStream(fbos.getInputStream());
	            obj = in.readObject();
	        }
	        catch(IOException e) {
//	            e.printStackTrace();
	        	log.error(e.getLocalizedMessage(),e);
	        }
	        catch(ClassNotFoundException cnfe) {
//	            cnfe.printStackTrace();
	        	log.error(cnfe.getLocalizedMessage(),cnfe);
	        }
	        finally{
	        	try{
	        		obj=deepCopyThroughXML(orig);
	        	}
	        	catch(Exception e){
//	        		e.printStackTrace();
	        		log.error(e.getLocalizedMessage(),e);
	        		obj=null;
	        	}
	        }
  	}else{
  		try{
      		obj=deepCopyThroughXML(orig);
      	}
      	catch(Exception e){
//      		e.printStackTrace();
      		log.error(e.getLocalizedMessage(),e);
      		obj=null;
      	}
  	}
      return obj;
  }
  /**
   * Returns a copy of the object
   * Warning! If <b>orig</b> is <b>null</b>,an exception will be thrown!
   * @param orig the object to copy or source object
   * @return a copy of the object
   */
  private static Object deepCopyThroughXML(Object orig) {
  	if(orig==null){
  		throw new RuntimeException("The source object must not be null!");
  	}
  	Object obj = null;
  	try {
          Object ser = Class.forName("com.thoughtworks.xstream.XStream").newInstance();
          String sorig = (String)MethodUtils.invokeExactMethod(ser, "toXML", new Object[]{orig},new Class[]{Object.class});
          obj=MethodUtils.invokeExactMethod(ser, "fromXML", new Object[]{sorig},new Class[]{String.class});
      } catch (ClassNotFoundException ex) {
//          ex.printStackTrace();
    	  log.error(ex.getLocalizedMessage(),ex);
      }catch (InstantiationException ex) {
//          ex.printStackTrace();
    	  log.error(ex.getLocalizedMessage(),ex);
      }catch (IllegalAccessException ex) {
//          ex.printStackTrace();
    	  log.error(ex.getLocalizedMessage(),ex);
      }catch (NoSuchMethodException ex) {
//          ex.printStackTrace();
    	  log.error(ex.getLocalizedMessage(),ex);
      }catch (InvocationTargetException ex) {
//          ex.printStackTrace();
    	  log.error(ex.getLocalizedMessage(),ex);
      }
      return obj;
  }
	public static final String minusList(String formula){
		String res="";
		if(!StringUtils.isBlank(formula)){
			String temp=formula.replaceAll("\\+", "|P").replaceAll("-", "|M");
			if(!temp.startsWith("|")){
				temp="P"+temp;
			}
			//System.out.println(temp);
			String[] tab= temp.split("\\|");
			for(String item:tab){
				//System.out.println(item);
				if(item.startsWith("M")){
					res+=(item.substring(1)+"','");
				}
			}
			int index=res.lastIndexOf("','");
			if(index!=-1){
				res=res.substring(0, index);
			}
		}
		res="'"+res+"'";
		return res;
	}
	public static final String plusList(String formula){
		String res="";
		if(!StringUtils.isBlank(formula)){
			String temp=formula.replaceAll("\\+", "|P").replaceAll("-", "|M");
			if(!temp.startsWith("|")){
				temp="P"+temp;
			}
			//System.out.println(temp);
			String[] tab= temp.split("\\|");
			for(String item:tab){
				//System.out.println(item);
				if(item.startsWith("P")){
					res+=(item.substring(1)+"','");
				}
			}
			int index=res.lastIndexOf("','");
			if(index!=-1){
				res=res.substring(0, index);
			}
		}
		res="'"+res+"'";
		return res;
	}
	public static final String minusListPost(String formula){
		String res="";
		int len=0;
		if(!StringUtils.isBlank(formula)){
			String temp=formula.replaceAll("\\+", "P|").replaceAll("-", "M|");
			if(!temp.endsWith("|")){
				temp=temp+"P";
			}
			//System.out.println(temp);
			String[] tab= temp.split("\\|");
			for(String item:tab){
				//System.out.println(item);
				len=item.length();
				if(item.endsWith("M")){
					res+=(item.substring(0,len-1)+"','");
				}
			}
			int index=res.lastIndexOf("','");
			if(index!=-1){
				res=res.substring(0, index);
			}
		}
		res="'"+res+"'";
		return res;
	}
	public static final String plusListPost(String formula){
		String res="";
		int len=0;
		if(!StringUtils.isBlank(formula)){
			String temp=formula.replaceAll("\\+", "P|").replaceAll("-", "M|");
			if(!temp.endsWith("|")){
				temp=temp+"P";
			}
			//System.out.println(temp);
			String[] tab= temp.split("\\|");
			for(String item:tab){
				//System.out.println(item);
				len=item.length();
				if(item.endsWith("P")){
					res+=(item.substring(0,len-1)+"','");
				}
			}
			int index=res.lastIndexOf("','");
			if(index!=-1){
				res=res.substring(0, index);
			}
		}
		res="'"+res+"'";
		return res;
	}
	/**
	 * @param args
	 * @throws Exception 
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception{
		/*RhpagentPK pk=new RhpagentPK();
		Rhpagent a=new Rhpagent(new RhpagentPK());
		ClonePK pk2=new ClonePK();
		boolean test=true;
		test=set(pk, "cdos", "001");
		System.out.println("pk1.cdos="+get(pk, "cdos"));
		System.out.println("pk1.cdos.bytes="+evaluate(pk, "cdos.bytes.class.array"));
		System.out.println("pk2.cdos="+get(pk2, "cdos")+" before copy");
		BeanUtils.copyProperties(pk, pk2);
		System.out.println("pk2.cdos="+get(pk2, "cdos")+" after copy");
		test=set(pk2, "bField", true);
		System.out.println("pk2.bfield="+get(pk2, "bField"));
		System.out.println(test?"successful reflection":"failure");
		Object ret=convert("12/02/2007",Class.forName("java.util.Date"),"dd/MM/yyyy");
		System.out.println("converted="+ret+"\t"+ret.getClass().getName());
		test=modify(a, "comp_id.cdos", "002");
		System.out.println(test?"successful reflection":"failure");
		System.out.println("a.comp_id.cdos="+evaluate(a, "comp_id.cdos"));*/
	}

}
//class ClonePK{
//	private String cdos;
//	private String nmat;
//	private boolean bField;
//	/**
//	 * @return the cdos
//	 */
//	public String getCdos() {
//		return cdos;
//	}
//	/**
//	 * @param cdos the cdos to set
//	 */
//	public void setCdos(String cdos) {
//		this.cdos = cdos;
//	}
//	/**
//	 * @return the nmat
//	 */
//	public String getNmat() {
//		return nmat;
//	}
//	/**
//	 * @param nmat the nmat to set
//	 */
//	public void setNmat(String nmat) {
//		this.nmat = nmat;
//	}
//	/**
//	 * @return the bfield
//	 */
//	public boolean isBField() {
//		return bField;
//	}
//	/**
//	 * @param bfield the bfield to set
//	 */
//	public void setBField(boolean bfield) {
//		this.bField = bfield;
//	}
//
//}

/**
* ByteArrayOutputStream implementation that doesn't synchronize methods
* and doesn't copy the data on toByteArray().
*/
final class FastByteArrayOutputStream extends OutputStream {
  /**
   * Buffer and size
   */
  protected byte[] buf = null;
  protected int size = 0;

  /**
   * Constructs a stream with buffer capacity size 5K
   */
  public FastByteArrayOutputStream() {
      this(5 * 1024);
  }

  /**
   * Constructs a stream with the given initial size
   */
  public FastByteArrayOutputStream(int initSize) {
      this.size = 0;
      this.buf = new byte[initSize];
  }

  /**
   * Ensures that we have a large enough buffer for the given size.
   */
  private void verifyBufferSize(int sz) {
      if (sz > buf.length) {
          byte[] old = buf;
          buf = new byte[Math.max(sz, 2 * buf.length )];
          System.arraycopy(old, 0, buf, 0, old.length);
          old = null;
      }
  }

  public int getSize() {
      return size;
  }

  /**
   * Returns the byte array containing the written data. Note that this
   * array will almost always be larger than the amount of data actually
   * written.
   */
  public byte[] getByteArray() {
      return buf;
  }

  public final void write(byte b[]) {
      verifyBufferSize(size + b.length);
      System.arraycopy(b, 0, buf, size, b.length);
      size += b.length;
  }

  public final void write(byte b[], int off, int len) {
      verifyBufferSize(size + len);
      System.arraycopy(b, off, buf, size, len);
      size += len;
  }

  public final void write(int b) {
      verifyBufferSize(size + 1);
      buf[size++] = (byte) b;
  }

  public void reset() {
      size = 0;
  }

  /**
   * Returns a ByteArrayInputStream for reading back the written data
   */
  public InputStream getInputStream() {
      return new FastByteArrayInputStream(buf, size);
  }

}

/**
* ByteArrayInputStream implementation that does not synchronize methods.
*/
final class FastByteArrayInputStream extends InputStream {
  /**
   * Our byte buffer
   */
  protected byte[] buf = null;

  /**
   * Number of bytes that we can read from the buffer
   */
  protected int count = 0;

  /**
   * Number of bytes that have been read from the buffer
   */
  protected int pos = 0;

  public FastByteArrayInputStream(byte[] buf, int count) {
      this.buf = buf;
      this.count = count;
  }

  public final int available() {
      return count - pos;
  }

  public final int read() {
      return (pos < count) ? (buf[pos++] & 0xff) : -1;
  }

  public final int read(byte[] b, int off, int len) {
      if (pos >= count)
          return -1;

      if ((pos + len) > count)
          len = (count - pos);

      System.arraycopy(buf, pos, b, off, len);
      pos += len;
      return len;
  }

  public final long skip(long n) {
      if ((pos + n) > count)
          n = count - pos;
      if (n < 0)
          return 0;
      pos += n;
      return n;
  }
  

}
