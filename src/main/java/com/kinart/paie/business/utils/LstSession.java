package com.kinart.paie.business.utils;

/**
 * @author c.mbassi
 *
 */

// Classe de d�finition du type LstSession

public class LstSession{
	/*
	 *  identificateur de la session
	 */
	String sessionId;
	/*
	 * Crit�re de tri sur la session
	 */
	String sessionSuppMin;
	
	String sessionSuppMax;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getSessionSuppMin() {
		return sessionSuppMin;
	}

	public void setSessionSuppMin(String sessionSuppMin) {
		this.sessionSuppMin = sessionSuppMin;
	}

	public String getSessionSuppMax() {
		return sessionSuppMax;
	}

	public void setSessionSuppMax(String sessionSuppMax) {
		this.sessionSuppMax = sessionSuppMax;
	}
	
}



