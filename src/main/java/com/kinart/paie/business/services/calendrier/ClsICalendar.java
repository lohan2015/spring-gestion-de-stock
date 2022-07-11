package com.kinart.paie.business.services.calendrier;

public  interface ClsICalendar {

	public String _STR_AND   = "AND";
	
	public String _STR_WHERE = " WHERE";
	
	public String _STR_DAY_LABEL = "90";
	
	public String _STR_DAY_FERIE = "232";
	
	public String _INT_MOND = "1";
	
	public String _INT_TUES = "2";
	
	public String _INT_WED = "3";
	
	public String _INT_THUR = "4";
	
	public String _INT_FRID = "5";
	
	public String _INT_SAT = "6";
	
	public String _INT_SUND = "7";
	
	public void __getYear();
	
	public void __getDayOfWeek();
	
	public boolean __save();
	
	public boolean __update();
}
