package com.kinart.organisation.business.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public interface ClsCelluleService{

	// Plot flow chart.
	public String dessinerOrganigramme(ClsCelluleThread oCellulePere, HttpServletRequest request, HttpSession session, String strBorderSize);

	//	 Plot flow chart.
//	public String dessinerOrganigrammeFils(ClsCelluleThread oCellule, String strOrganigramme, HttpServletRequest request, HttpSession session);
}
