package com.kinart.paie.business.services.utils;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ClsDate {
	
	private String DATE_FORMAT = "dd/MM/yyyy";
	private String _strFormat = DATE_FORMAT;
	private DateFormat _format = null;
	//
	Calendar c1 = Calendar.getInstance(); 
	
	public ClsDate(){
		
	}
	
	public ClsDate(Date date, String strFormat){
		_format = new SimpleDateFormat(strFormat);
//		c1.setTimeZone(TimeZone.getTimeZone("Etc/GMT+1"));
		if(date != null)
			c1.setTime(date);
	}
	
	public ClsDate(Date date){
		_format = new SimpleDateFormat(_strFormat);
		try
		{
			c1.setTime(date);
		}
		catch (RuntimeException e)
		{

		}
	}
	
	public ClsDate(Object oDate){
		_format = new SimpleDateFormat(_strFormat);
		try{
			c1.setTime((Date)oDate);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public ClsDate(String strDate){
		_format = new SimpleDateFormat(_strFormat);
		try{
			Date dt = _format.parse(strDate);
			c1.setTime(dt);
		}
		catch(ParseException e){
			e.printStackTrace();
		}
	}
	
	public ClsDate(String strDate, String strFormat){
		_strFormat = strFormat;
		_format = new SimpleDateFormat(strFormat);
//		System.out.println("Chaine date = "+strDate);
//		
//		System.out.println("Format de la date= "+strFormat);
		try{
			Date dt = _format.parse(strDate);
			c1.setTime(dt);
		}
		catch(ParseException e){
			System.out.println("Erreur Chaine date = "+strDate);
			
			System.out.println("Erreur Format de la date= "+strFormat);
			e.printStackTrace();
			
		}
	}
	
	public ClsDate(Object o, String strFormat){
		_format = new SimpleDateFormat(_strFormat);
		if(o instanceof Date){
			c1.setTime((Date)o);
		}
		else if(!(o instanceof Date)){
			_strFormat = strFormat;
			_format = new SimpleDateFormat(_strFormat);
			try{
				Date dt = _format.parse(o.toString());
				c1.setTime(dt);
			}
			catch(ParseException e){
				System.out.println("Erreur Chaine date = "+ o.toString());
				
				System.out.println("Erreur Format de la date= "+ strFormat);
				e.printStackTrace();
				
			}
		}
	}
	
	/**
	 * Conversion d'une date en tenant compte du format.<br>
	 *   Si le <param>format</param> est <code>null</code> alors le format par d�faut sera consid�r�
	 * 
	 * @param datString la date sous forme de chaine de caract�re
	 * @param format le format
	 * @return la date
	 * @throws ParseException si la date en chaine de caract�re est mal form�e
	 */
	public Date convertDate(String datString, String format) throws ParseException {
		if(StringUtils.isEmpty(format))
		   _format = new SimpleDateFormat(_strFormat);
		else
			_format = new SimpleDateFormat(format);
		
		Date dt = _format.parse(datString);
		c1.setTime(dt);
		return c1.getTime();
	}
	
	/**
	 * Compare deux dates pour savoir si elles sont egales.<br>
	 *   Si le <param>format</param> est <code>null</code> alors le format par d�faut sera consid�r�
	 * 
	 * @param date1
	 * @param date2
	 * @return true si (date1 = null et date2 = null) ou si (date1.compareTo(date2) == 0)
	 */
	public static boolean equals(Date date1, Date date2){
		if((date1 == null) && (date2 == null)){
			return true;
		} else if((date1 == null) || (date2 == null)){
			return false;
		} else if(date1.compareTo(date2) == 0){
			return true;
		}else{
			return false;
		}
	}
	
	public int getMonth(){
		return c1.get(Calendar.MONTH) + 1;
	}
	
	public static int getMonth(Date d){
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		return c.get(Calendar.MONTH);
	}
	
	public static int getYear(Date d){
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		return c.get(Calendar.YEAR);
	}
	
	public int getDay(){
		return c1.get(Calendar.DAY_OF_MONTH);
	}
	
	public static int getDay(Date d){
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		return c.get(Calendar.DAY_OF_MONTH);
	}
	
	public int getYear(){
		return c1.get(Calendar.YEAR);
	}
	
	public Date addSecond(int nSecs){
//		Calendar c1 = (Calendar)this.c1.clone();
		c1.add(Calendar.SECOND, nSecs);
		return c1.getTime();
	}
	
	public Date addMin(int nMins){
//		Calendar c1 = (Calendar)this.c1.clone();
		c1.add(Calendar.MINUTE, nMins);
		return c1.getTime();
	}
	
	public Date addHour(int nHours){
//		Calendar c1 = (Calendar)this.c1.clone();
		c1.add(Calendar.HOUR, nHours);
		return c1.getTime();
	}
	
	public Date addDay(int nDays){
//		Calendar c1 = (Calendar)this.c1.clone();
		c1.add(Calendar.DATE, nDays);
		return c1.getTime();
	}
	
	public static Date addDay(Date d, int nDays){
		Calendar c1 = Calendar.getInstance();
		c1.setTime(d);
		c1.add(Calendar.DATE, nDays);
		return c1.getTime();
	}
	
	public Date addMonth(int nMonths){
//		Calendar c1 = (Calendar)this.c1.clone();
		int nMonthsw = (nMonths <= 0)? nMonths : nMonths--;
		c1.add(Calendar.MONTH, nMonthsw);
		return c1.getTime();
	}
	
	public static Date addMonth(Date d,int nMonths){
		int nMonthsw = (nMonths <= 0)? nMonths : nMonths--;
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.MONTH, nMonthsw);
		return c.getTime();
	}
	
	public Date addYear(int nYears){
//		Calendar c1 = (Calendar)this.c1.clone();
		c1.add(Calendar.YEAR, nYears);
		return c1.getTime();
	}
//
	/**
	 * @return the _strDate
	 */
	public String getDateS(){
		return _format.format(c1.getTime());
	}
	
	public String getDateS(String strFormat){

		SimpleDateFormat oFormat = new SimpleDateFormat(strFormat);
		return oFormat.format(c1.getTime());
	}
	
	public static String getDateS(Date d, String strFormat){
		SimpleDateFormat oFormat = new SimpleDateFormat(strFormat);
		return oFormat.format(d);
	}

	/**
	 * @return the w_ddcd
	 */
	public Date getDate(){
		return c1.getTime();
	}
	
	/**
	 * @return the w_ddcd
	 */
	public static Date substractDate(Date d_end, Date d_start) throws Exception{
		Date w_ddcd = null;
		long l_end = d_end.getTime();
		long l_start = d_start.getTime();
		try{
			if(l_end < l_start)
				throw new Exception("La date de fin doit �tre sup�rieure � la date de d�but.");
			w_ddcd = new Date(l_end - l_start);
		}
		catch(ParseException e)
		{
			e.printStackTrace();
		}
		return w_ddcd;
	}		
	
	/**
	 * @return the w_ddcd
	 */
	public static long getNbreMillisecondsBetweenDates(Date d_end, Date d_start){
		long l_end = d_end.getTime();
		long l_start = d_start.getTime();
		return l_end - l_start;
	}
	
	/**
	 * @return the w_ddcd
	 */
	public static Date addDate(Date d_first, Date d_snd){
		Date w_ddcd = null;
		long l_end = d_first.getTime();
		long l_start = d_snd.getTime();
		//
		w_ddcd = new Date(l_end + l_start);
		//
		return w_ddcd;
	}	
	
	public String getYearAndMonth(){
		return String.valueOf(this.c1.get(Calendar.YEAR)) + StringUtil.formatNumber(this.c1.get(Calendar.MONTH) + 1, "00");
	}
	
	public String getYearAndMonth(char c){
		return String.valueOf(this.c1.get(Calendar.YEAR)) + c + StringUtil.formatNumber(this.c1.get(Calendar.MONTH) + 1, "00");
	}
	
	public Date getYearAndMonthDate(){
		Date oDate = null;
		try{
			oDate = new SimpleDateFormat("yyyyMM").parse(String.valueOf(c1.get(Calendar.YEAR)) + String.valueOf( 1 + c1.get(Calendar.MONTH) + 1));
		}
		catch(ParseException e)
		{
			e.printStackTrace();
		}
		return oDate;
	}
	
	public Date getLastDayOfMonth(){
		
		Calendar c1 = Calendar.getInstance();
		c1 = (Calendar)this.c1.clone();
		int a = c1.getActualMaximum(Calendar.DAY_OF_MONTH);
		c1.set(Calendar.DAY_OF_MONTH, a);
		
		return c1.getTime();
	}
	
	public Date getLastDayOfYear(){
		
		Calendar c1 = Calendar.getInstance();
		c1 = (Calendar)this.c1.clone();
		int a = c1.getActualMaximum(Calendar.DAY_OF_YEAR);
		c1.set(Calendar.DAY_OF_YEAR, a);
		
		return c1.getTime();
	}
	
	public static Date getLastDayOfMonth(Date d1){
			
		Calendar c1 = Calendar.getInstance();
		c1.setTime(d1);
		int a = c1.getActualMaximum(Calendar.DAY_OF_MONTH);
		c1.set(Calendar.DAY_OF_MONTH, a);
		return c1.getTime();
	}
	
	public int getMaxDayOfMonth(){
		return c1.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	public static int getMaxDayOfYear(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.getActualMaximum(Calendar.DAY_OF_YEAR);
	}
	
	public static Date getFirstDayOfMonth(Date d1){
		Calendar c1 = Calendar.getInstance();
		c1.setTime(d1);
		c1.set(Calendar.DAY_OF_MONTH, 1);
		
		return c1.getTime();
	}
	
	public Date getFirstDayOfMonth(){
		//Calendar c1 = (Calendar)this.c1.clone();
		c1.set(Calendar.DAY_OF_MONTH, 1);
		
		return c1.getTime();
	}
	
	public Date getFirstDayOfYear(){
		//Calendar c1 = (Calendar)this.c1.clone();
		c1.set(Calendar.DAY_OF_YEAR, 1);
		
		return c1.getTime();
	}
	
	/**
	 * @return the number of day between the two dates
	 */
	public static Long getNumberOfDay(Date d_end, Date d_start){
//		try {
			Calendar cal_start = Calendar.getInstance();
			Calendar cal_end = Calendar.getInstance();
			cal_start.setTime(d_start);
			cal_end.setTime(d_end);
			cal_start.set(Calendar.HOUR_OF_DAY,0);
			cal_start.set(Calendar.MINUTE,0);
			cal_start.set(Calendar.SECOND,0);
			cal_start.set(Calendar.MILLISECOND,0);
			cal_end.set(Calendar.HOUR_OF_DAY,0);
			cal_end.set(Calendar.MINUTE,0);
			cal_end.set(Calendar.SECOND,0);
			cal_end.set(Calendar.MILLISECOND,0);

			return Math.abs(((cal_end.getTime().getTime() - cal_start.getTime().getTime()) / 
			(24*60*60*1000)));
//		} catch (RuntimeException e) {
//			e.printStackTrace();
//		}
//		return 0;
	}
	/**
	 * @return the number of day between the two dates
	 */
	public static Long getRelativeNumberOfDay(Date d_end, Date d_start){
//		try {
			Calendar cal_start = Calendar.getInstance();
			Calendar cal_end = Calendar.getInstance();
			cal_start.setTime(d_start);
			cal_end.setTime(d_end);
			cal_start.set(Calendar.HOUR_OF_DAY,0);
			cal_start.set(Calendar.MINUTE,0);
			cal_start.set(Calendar.SECOND,0);
			cal_start.set(Calendar.MILLISECOND,0);
			cal_end.set(Calendar.HOUR_OF_DAY,0);
			cal_end.set(Calendar.MINUTE,0);
			cal_end.set(Calendar.SECOND,0);
			cal_end.set(Calendar.MILLISECOND,0);

			return ((cal_end.getTime().getTime() - cal_start.getTime().getTime()) / 
			(24*60*60*1000));
//		} catch (RuntimeException e) {
//			e.printStackTrace();
//		}
//		return 0;
	}
	/**
	 * @return the number of hours between the two dates
	 */
	public static Long getNumberOfHours(Date d_end, Date d_start)
	{
		Calendar cal_start = Calendar.getInstance();
		Calendar cal_end = Calendar.getInstance();
		cal_start.setTime(d_start);
		cal_end.setTime(d_end);
		cal_start.set(Calendar.HOUR_OF_DAY, 0);
		cal_start.set(Calendar.MINUTE, 0);
		cal_start.set(Calendar.SECOND, 0);
		cal_start.set(Calendar.MILLISECOND, 0);
		cal_end.set(Calendar.HOUR_OF_DAY, 0);
		cal_end.set(Calendar.MINUTE, 0);
		cal_end.set(Calendar.SECOND, 0);
		cal_end.set(Calendar.MILLISECOND, 0);

		return Math.abs(((cal_end.getTime().getTime() - cal_start.getTime().getTime()) / (60 * 60 * 1000)));

	}
	
	public static int getMaxDayOfMonth(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	public static Date getDateWithFormatDDMMYYYY(Date date, String format){
		return new ClsDate(new ClsDate(date).getDateS(format), format).getDate();
	}
	
	public static boolean haveTheSameMonth(Date d1, Date d2){
		Calendar c1 = Calendar.getInstance();
		c1.setTime(d1);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(d2);
		
		return (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH));
	}
	
	public static int getNomberOfDaysBetween(Date from, Date to){
		if(from == null || to == null)
			return -1;
		if(from.after(to))
			return -1;
		
		int i = 0;
		
		ClsDate oFrom1 = new ClsDate(from);
		Date tmp = from; 
		while(! tmp.after(to)){
			tmp = oFrom1.addDay(1);
			i++;
		}
		//
		return i;
	}
	public static double getMonthsBetweenIncludingDays(Date from, Date to){
		if(from == null || to == null)
			return 0;
		if(from.compareTo(to)>0)
		{
			Date temp = to;
			to = from;
			from = temp;
		}
		
		ClsDate oFrom = new ClsDate(from);
		ClsDate oTo = new ClsDate(to);
		double a1 = oFrom.getYear();
		double a2 = oTo.getYear();
		double nmois = 0,
			na = 0,
			nj = 0,
			nmois1 = oFrom.getMonth(),
			nmois2 = oTo.getMonth(),
			njour1 = oFrom.getDay(),
			njour2 = oTo.getDay();
		na = a2 - a1;
		nmois = nmois2 - nmois1;
		nj = njour2 - njour1;
		if(nj<0)
		{
			nj = nj + 30;
			nmois = nmois - 1;
		}
		if(nmois < 0)
		{
			nmois = nmois + 12;
			na = na -1;
		}
		
		double nmoisAnnee = na*12;
		double nmoisMois = nmois;
		double nmoisJours = nj / Double.valueOf(30);
		nmois = StringUtil.truncateTo3Decimal(new BigDecimal(nmoisAnnee + nmoisMois + nmoisJours)).doubleValue();
		
		return nmois;
	}
	
	public static int getMonthsBetween(Date from, Date to){
		if(from == null || to == null)
			return 0;
		if(from.compareTo(to)>0)
		{
			Date temp = to;
			to = from;
			from = temp;
		}
		ClsDate oFrom = new ClsDate(from);
		ClsDate oTo = new ClsDate(to);
		int a1 = oFrom.getYear();
		int a2 = oTo.getYear();
		int nmois = 0,
			na = 0,
			nmois1 = oFrom.getMonth(),
			nmois2 = oTo.getMonth();
		na = a2 - a1;
		
		nmois = (nmois2 < nmois1)? (nmois2 - nmois1 + 12) + (na - 1)*12 + 1: (nmois2 - nmois1) + na*12 + 1;
		//
		return Math.abs(nmois);
	}
	
	public static int getYearsBetween(Date from, Date to){
		int nmois = ClsDate.getMonthsBetween(from, to);
		Integer nannee = new Integer(nmois/12);
		return nannee.intValue();
	}
	
	public static int getMonthsBetween(String from, String to){
		if(from == "" || to == "")
			return -1;
		return ClsDate.getMonthsBetween(new ClsDate(from).getDate(), new ClsDate(to).getDate());
	}
	
	public String getFormatedDate(char c){
		return String.valueOf(this.c1.get(Calendar.YEAR)) + c + StringUtil.formatNumber(this.c1.get(Calendar.MONTH), "00")
				 + c + StringUtil.formatNumber(this.c1.get(Calendar.DAY_OF_MONTH), "00");
	}
	
	public String getFormatedDate(){
		return getFormatedDate('-');
	}
	
	public int getYearAndMonthInt(){
		//return this.c1.get(Calendar.YEAR) * 100 + Calendar.MONTH;
		return this.c1.get(Calendar.YEAR) * 100 + this.c1.get(Calendar.MONTH) + 1;
	}
	
	public void setDate(Date d){
		c1.setTime(d);
	}
	
	public static String getFormatedDate(String format, Date date){
		SimpleDateFormat dateformat = new SimpleDateFormat(format);
		return dateformat.format(date);
	}
	
	public int getDayOfTheWeek(){
		return this.c1.get(Calendar.DAY_OF_WEEK);
	}
	
	public int getWeekOfYear(){
		return this.c1.get(Calendar.WEEK_OF_YEAR);
	}
	
	public static int getDayOfTheWeek(Date d){
		Calendar c1 = Calendar.getInstance();
		c1.setTime(d);
		return c1.get(Calendar.DAY_OF_WEEK);
	}
	
	public static int getWeekOfMonth(Date d){
		Calendar c1 = Calendar.getInstance();
		c1.setTime(d);
		return c1.get(Calendar.WEEK_OF_MONTH);
	}
	
	public Date convertToDate(String strDate) throws ParseException{
		_format = new SimpleDateFormat(_strFormat);
		try{
			Date dt = _format.parse(strDate);
			c1.setTime(dt);
			return dt;
		}
		catch(ParseException e){
			throw e;
		}
	}
	
	public ClsDate clone()
	{
		ClsDate dt = null;
		if(this != null) 
			dt = new ClsDate(this.getDate());
		return dt;
	}
	
	public String localToString()
	{
		return "Annee = "+this.getYear()+" , Mois = "+this.getMonth()+" , Jour =  "+this.getDay()+" : periode = "+this.getYearAndMonth();
	}
	public static Date getdate(int day, int month, int year){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, day);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);
		return cal.getTime();
	}
	public static int getDureeMoisEntre(Date debut,Date fin){
		//if(dt==null) return null;
		Calendar endDate=Calendar.getInstance();
		endDate.setTime(fin);
		Calendar startDate=Calendar.getInstance();
		startDate.setTime(debut);
		//today.
		int todayMonths=endDate.get(Calendar.MONTH);
		int dtMonths=startDate.get(Calendar.MONTH);
		int todayYears=endDate.get(Calendar.YEAR);
		int dtYears=startDate.get(Calendar.YEAR);
		return (todayYears-dtYears)*12+todayMonths-dtMonths;
	}
	
	public static boolean sameDay(Date toTest,Date ref){
		boolean res = true;
		String dayFormat = "dd/MM/yyyy";
		if(toTest == null || ref == null){
			res = false;
		}else{
			res = getDateWithFormatDDMMYYYY(toTest,dayFormat).equals(getDateWithFormatDDMMYYYY(ref, dayFormat));
		}
		return res;
	}
	
	public static boolean between(Date toTest,Date from,Date to){
		return between(toTest, from, to, false);
	}
	
	public static boolean betweenNoTime(Date toTest,Date from,Date to){
		return between(toTest, from, to, true);
	}
	
	public static boolean between(Date toTest,Date from,Date to, boolean ignoreTime){
		String dayFormat = "dd/MM/yyyy";
		if(toTest==null)
			return false;
		if(from==null&&to==null){
			return false;
		}
		Date toTest2 = getDateWithFormatDDMMYYYY(toTest, dayFormat);
		Date from2 = from==null?null:getDateWithFormatDDMMYYYY(from, dayFormat);
		Date to2 = to==null?null:getDateWithFormatDDMMYYYY(to, dayFormat);
		toTest2 = ignoreTime?toTest2:toTest;
		from2 = ignoreTime?from2:from;
		to2 = ignoreTime?to2:to;
		if(to2==null){
			return toTest2.compareTo(from2)>=0;
		}else if(from2==null){
			return toTest2.compareTo(to2)<=0;
		}else{
			return toTest2.compareTo(from2)>=0&&toTest2.compareTo(to2)<=0;
		}
	}
	
	public static boolean validDateString(String dates,String format){
		boolean ret = true;
		try{
			DateFormat df = new SimpleDateFormat(format);
			df.parse(dates);
		}catch(Exception ex){
			ret = false;
		}
		return ret;
	}
	
	/**
	 * Allows to know whether the date <B>toComp is</B> before or equal to the date <b>ref</b>.
	 * It returns true if both of the dates are <b>null</b> and false if only one of them is <b>null</b>.
	 * @param toComp the date to compare
	 * @param ref the reference date
	 * @return true/false
	 */
	public static boolean beforeOrEqual(Date toComp,Date ref){
		if(toComp==null&&ref==null)
			return true;
		if(toComp==null||ref==null)
			return false;
		return toComp.compareTo(ref)<=0;
	}
	
	public static boolean before(Date toComp,Date ref){
		if(toComp==null&&ref==null)
			return false;
		if(toComp==null||ref==null)
			return false;
		return toComp.compareTo(ref)<0;
	}
	
	/**
	 * Allows to know whether the date <B>toComp is</B> after or equal to the date <b>ref</b>.
	 * It returns true if both of the dates are <b>null</b> and false if only one of them is <b>null</b>.
	 * @param toComp the date to compare
	 * @param ref the reference date
	 * @return true/false
	 */
	public static boolean afterOrEqual(Date toComp,Date ref){
		if(toComp==null&&ref==null)
			return true;
		if(toComp==null||ref==null)
			return false;
		return toComp.compareTo(ref)>=0;
	}
	
	public static boolean after(Date toComp,Date ref){
		if(toComp==null&&ref==null)
			return false;
		if(toComp==null||ref==null)
			return false;
		return toComp.compareTo(ref)>0;
	}
	
	public static String to_char(Date date, String format)
	{
		return new ClsDate(date).getDateS(format);
	}
	
	public static Date last_day(Date date)
	{
		return new ClsDate(date).getLastDayOfMonth();
	}
}