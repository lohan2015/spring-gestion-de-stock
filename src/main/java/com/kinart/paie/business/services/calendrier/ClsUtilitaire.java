package com.kinart.paie.business.services.calendrier;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.kinart.paie.business.model.ParamData;
import com.kinart.paie.business.model.Salarie;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.kinart.paie.business.services.utils.ParameterUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.util.StringUtils;

public class ClsUtilitaire {
	
	private Object currentObject;
	
	private String commandName;
	
	private HttpServletRequest request;
	
	public ClsUtilitaire(Object currentObject,String commandName,HttpServletRequest request){
		setCurrentObject(currentObject);
		setCommandName(commandName);
		setRequest(request);
	}
	
	public List<Method> getObjectSetters(){
		Method[] methods= this.getCurrentObject().getClass().getMethods();
		//Method[] setters = new Method[]{}
		//System.out.println(">>>>>>>>>>>>>>>Classe = "+this.getCurrentObject().getClass().toString());
		//System.out.println(">>>>>>>>>>>>>>>Tailles des methoes = "+methods.length);
		List<Method> setters = new ArrayList<Method>();
		int j=0;
		for(int i=0;i<methods.length;i++){
			String methodName = methods[i].getName();
			//System.out.println(">>>>>>>>>>>>>>> Nom de la m�thode = "+methodName);
			if("set".equalsIgnoreCase(methodName.substring(0, 3)))
				setters.add(methods[i]);
		}	
		return setters;
	}
	
	public List<Method> getSetters(){
		Object obj=new Object();
		Method[] methods= Salarie.class.getMethods();
		//Method[] setters = new Method[]{};
		List<Method> setters = new ArrayList<Method>();
		int j=0;
		for(int i=0;i<methods.length;i++){
			String methodName = methods[i].getName();
			if("set".equalsIgnoreCase(methodName.substring(0, 3)))
				setters.add(methods[i]);
		}	
		return setters;
	}
	
	public static void main(String[] argv) throws Exception{
		Salarie agent = new Salarie();
		agent.setIdentreprise(1);
		agent.setNmat("000001");
		HttpServletRequest request=null;
		ClsUtilitaire util = new ClsUtilitaire(agent,"rhpagent",request);
		//util.testGlobal();
		for(int i=0;i<util.getObjectSetters().size();i++){
			/*System.out.println(util.getSetters().get(i).getName());
			System.out.println(util.getSetters().get(i).getModifiers());
			System.out.println(util.getSetters().get(i).getParameterTypes()[0].getSimpleName());*/
			
			Method method =util.getSetters().get(i);
		if("setNbjaf".equalsIgnoreCase(method.getName())){
		
		util.invokeASetter(method, "0.40");
		System.out.println("Agent name = "+agent.getNbjaf());
		String aName="rhpagent.cat";
		String split=aName.split("\\.")[1];
		split =StringUtils.capitalize(split);
		
		System.out.println("Split = "+split);
		System.out.println("Split = "+aName.split("\\.")[0]);
		System.out.println("nom du parametre dans le request : "+util.getRequestParameterNameFromSetterName( method));
		}
		}
	}
	public String getRequestParameterNameFromSetterName(Method setterMethod){
		
		String setterName = setterMethod.getName();
		int setterNameLength = setterName.length();
		String arg = setterName.substring(3, setterNameLength);
		arg = arg.toLowerCase();
		if(this.getCommandName()==null) return arg;
		else return this.getCommandName()+"."+arg;
	}
	
	public void invokeASetter(Method method, String arg) throws Exception{
		String dateformat =  ParameterUtil.getSessionObject(this.getRequest ( ), ParameterUtil.SESSION_FORMAT_DATE);
		DateFormat dateFormat=new SimpleDateFormat(dateformat);
		String argType = method.getParameterTypes()[0].getSimpleName();
		//System.out.println("Methode � invoker = "+method.getName());
		//System.out.println("Type de l'argument du setter = "+argType);
		//System.out.println("En train d'invoker--------------------->"+method.toString());
		if("String".equalsIgnoreCase(argType))	method.invoke(this.getCurrentObject(), new Object[]{arg});
		else{
			if(arg!=null && arg.length()>0){
			//System.out.println("En train d'invoker");	
			if(ClsEnumTypes.BIGDECIMAL.equalsIgnoreCase(argType))	method.invoke(this.getCurrentObject(), new Object[]{new BigDecimal(arg)});
			if(ClsEnumTypes.DATE.equalsIgnoreCase(argType))	method.invoke(this.getCurrentObject(), new Object[]{dateFormat.parse(arg)});
			if(ClsEnumTypes.DOUBLE.equalsIgnoreCase(argType))	method.invoke(this.getCurrentObject(), new Object[]{Double.valueOf(arg)});
			if(ClsEnumTypes.INTEGER.equalsIgnoreCase(argType))	method.invoke(this.getCurrentObject(), new Object[]{Integer.valueOf(arg)});
			}
		}
	}
	public void traitementCle(Method methode)throws Exception{
		if(methode.getName().length()==10)
		if("comp_id".equalsIgnoreCase(methode.getName().substring(3, 10))){
			//traitement de la cl�
			
		}
	}

	public void invokeAllObjectSetters() throws Exception{
		List<Method> methodes = this.getObjectSetters();
		for(int i=0;i<methodes.size();i++){
			String parameter = this.getRequestParameterNameFromSetterName(methodes.get(i));
			//System.out.println("#######################################################Parameter = "+parameter);
			String parameterValue = this.getRequest().getParameter(parameter); 
			//System.out.println("#######################################################Parameter value = "+parameterValue);
			if(parameterValue!=null){
			if( methodes.get(i).getName().length()==10){
				if("comp_id".equalsIgnoreCase(methodes.get(i).getName().substring(3, 10))){
					
					//System.out.print("Cas des comp_id ");
					//System.out.println("Methode = "+methodes.get(i).getName());
				}
				else this.invokeASetter(methodes.get(i),parameterValue);
			}
			else
			  this.invokeASetter(methodes.get(i),parameterValue);
		}
	  }
	}
	
	
	
	
	public static String getSessionObject(HttpServletRequest request,String strObjectName){
		Object object = request.getSession().getAttribute(strObjectName);
		if(object==null) return "";
		else return object.toString();
	}
	
	public static String getDate(GeneriqueConnexionService service, String strCodeDossier, String codeLangue, String annee, String mois, String jour) {
		String date="01-01-1900";
		String separateur="-";
		String orderAnnee="1";
		String orderMois="2";
		String orderJour="3";
		
		date=jour+separateur+mois+separateur+annee;
		
		ParamData oNomenclature = service.findAnyColumnFromNomenclature(strCodeDossier, codeLangue,"266","SEPARATOR", "2");
		if(!oNomenclature.getVall().equalsIgnoreCase("")) {
		separateur=oNomenclature.getVall();
		
		oNomenclature = service.findAnyColumnFromNomenclature(strCodeDossier, codeLangue,"266","YEAR_POS", "2");
		orderAnnee=oNomenclature.getVall();
		
		oNomenclature = service.findAnyColumnFromNomenclature(strCodeDossier, codeLangue,"266","MONTH_POS", "2");
		orderMois=oNomenclature.getVall();
		
		oNomenclature = service.findAnyColumnFromNomenclature(strCodeDossier, codeLangue,"266","DAY_POS", "2");
		orderJour=oNomenclature.getVall();
		
		date=annee+separateur+mois+separateur+jour;
		
		if("1".equalsIgnoreCase(orderAnnee)) {
			if("2".equalsIgnoreCase(orderMois))  date=annee+separateur+mois+separateur+jour;
			if("3".equalsIgnoreCase(orderMois))  date=annee+separateur+jour+separateur+mois;
		}
		
		if("2".equalsIgnoreCase(orderAnnee)) {
			if("1".equalsIgnoreCase(orderMois))  date=mois+separateur+annee+separateur+jour;
			if("3".equalsIgnoreCase(orderMois))  date=jour+separateur+annee+separateur+mois;
		}
		
		if("3".equalsIgnoreCase(orderAnnee)) {
			if("1".equalsIgnoreCase(orderMois))  date=mois+separateur+jour+separateur+annee;
			if("2".equalsIgnoreCase(orderMois))  date=jour+separateur+mois+separateur+annee;
		}
		
		
		}
		//System.out.println("Date apres formatage = "+date);
		return date;
	}
	
	
	public static List getSalarieList(GeneriqueConnexionService service, String queryString){
		List resultat = new ArrayList();
		Session session = service.getSession();
		Query q = session.createQuery(queryString);
		q.setFirstResult(3);
		q.setMaxResults(5);
		resultat = q.list();
		service.closeSession(session);
		return resultat;
	}
	
	/**
	 * @param d
	 * @return la valeur arrondi.<br>
	 */
	public static BigDecimal getRound(Number d){
		//application des arrondis
		long lo = Math.round(d.doubleValue());
		return lo < d.doubleValue() ? new BigDecimal(lo + 1) : new BigDecimal(lo);
	}
	
	public Object getCurrentObject() {
		return currentObject;
	}

	public void setCurrentObject(Object currentObject) {
		this.currentObject = currentObject;
	}

	public String getCommandName() {
		return commandName;
	}

	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

}
