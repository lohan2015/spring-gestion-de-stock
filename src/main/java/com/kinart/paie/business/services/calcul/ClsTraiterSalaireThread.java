package com.kinart.paie.business.services.calcul;

import com.kinart.paie.business.model.Salarie;

/**
 * Classe permet de lancer le calcul de la paie d'un agent dans un thread s�par�. Elle sera utilis�e dans un pool de threads.
 * 
 * @author c.mbassi
 * 
 */
public class ClsTraiterSalaireThread implements Runnable
{
	public static int NBRE_AGENT_A_TRAITE = 0;

	public static int NBRE_AGENT_TRAITE = 0;

	public static int NBRE_ERREURS_RENCONTREES = 0;

	public static String Res_cal = "";

	public static String encours = "";

	ClsSalariesEngine engine = null;

	Salarie salary = null;
	
	Integer sessionId;

	// String strDateFormat="yyyy-MM-dd";
	/**
	 * full constructor
	 * 
	 * @param engine
	 *            le moteur de calcul
	 * @param salary
	 *            le salari� concern�
	 */
	public ClsTraiterSalaireThread(ClsSalariesEngine engine, Salarie salary, Integer i)
	{
		this.engine = engine;
		this.salary = salary;
		this.sessionId = i;
		// this.strDateFormat = strDateFormat;
	}

	/**
	 * traite le salaire d'un agent dans un thread
	 */
	public void run()
	{
		try
		{
			engine.traiterSalaire(salary, sessionId);
			engine.incrementerNombreSalariesTraite();
		}
		catch (Exception e)
		{
			engine.incrementerNombreSalariesTraite();
			e.printStackTrace();
			return;
		}
	}
	

	public static String toXml()
	{
		StringBuffer xml = new StringBuffer();
		xml.append("<?xml version=\"1.0\"?>\n");
		xml.append("<params>\n");

		xml.append("<total>");
		xml.append(ClsTraiterSalaireThread.NBRE_AGENT_A_TRAITE);
		// xml.append("100");
		xml.append("</total>\n");

		xml.append("<traite>");
		xml.append(ClsTraiterSalaireThread.NBRE_AGENT_TRAITE);
		xml.append("</traite>\n");

		xml.append("</params>\n");
		return xml.toString();
	}
}
