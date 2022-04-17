package com.kinart.paie.business.services.calcul;

import com.kinart.paie.business.model.Salarie;
import com.kinart.paie.business.model.SuspensionPaie;
import com.kinart.paie.business.services.utils.HibernateConnexionService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

/**
 * Le but de cette classe est de lancer le calcul fictif de la paie sur chaque salari�.
 * 
 * @author y.talla
 */
public class ClsFictifSalariesEngine
{
	private String error = "";
	
	private int threadmax = 5;

	ExecutorService poolSrev = null;

	private HibernateConnexionService service = null;
	private ClsFictifParameterOfPay parameterOfSalary = null;

	private List<ClsFictifSalaryToProcess> listOfSalaryToProcess = null;

	private List listOfParubq = null;

	//public UICalculFictifBulletin ui;

	public Integer nbrSalaries = 0;

	public Integer nbrSalariesTraite = 0;

	public synchronized ExecutorService getPoolSrev()
	{
		return poolSrev;
	}

	public int getThreadmax()
	{
		return threadmax;
	}

	public void setThreadmax(int threadmax)
	{
		this.threadmax = threadmax;
	}

	public List getListOfParubq()
	{
		return listOfParubq;
	}

	public void setListOfParubq(List listOfParubq)
	{
		this.listOfParubq = listOfParubq;
	}

	public HibernateConnexionService getService()
	{
		return service;
	}

	public void setService(HibernateConnexionService service)
	{
		this.service = service;
	}

	public List<ClsFictifSalaryToProcess> getListOfSalaryToProcess()
	{
		return listOfSalaryToProcess;
	}

	public void setListOfSalaryToProcess(List<ClsFictifSalaryToProcess> listOfSalaryToProcess)
	{
		this.listOfSalaryToProcess = listOfSalaryToProcess;
	}

	public ClsFictifParameterOfPay getParameterOfSalary()
	{
		return parameterOfSalary;
	}

	public void setParameterOfSalary(ClsFictifParameterOfPay parameterOfSalary)
	{
		this.parameterOfSalary = parameterOfSalary;
	}

	public void initialise()
	{

	}

	public ClsFictifSalariesEngine()
	{

	}

	/**
	 * construit la liste des rubriques
	 */
	public List buildListOfAllRubrique()
	{
		List listOfParubq = service.find("from Rhprubrique" + " where cdos = '" + parameterOfSalary.getDossier() + "'"
				+ " and crub between '0001' and '9999'" + " order by cdos, crub");
		return listOfParubq;
	}

	/**
	 * construit la liste des rubriques retro
	 */
	public List buildListOfAllRubriqueRetro()
	{
		List listOfParubq = service.find("from Rhthrubq" + " where cdos = '" + parameterOfSalary.getDossier() + "'" + " and aamm = '"
				+ parameterOfSalary.getMonthOfPay() + "'" + " and nbul = " + parameterOfSalary.getNumeroBulletin()
				+ " order by cdos, crub");
		return listOfParubq;
	}

	/**
	 * construit une liste contenant les salari�s clon�s
	 * 
	 * @param listOfSalaries
	 * @return liste des agents � traiter
	 */
	public List<ClsInfoSalaryClone> getListOfInfoSalaryClone(List listOfSalaries)
	{
		//
		List<ClsInfoSalaryClone> l = new ArrayList<ClsInfoSalaryClone>();
		for (Object obj : listOfSalaries)
		{
			l.add(new ClsInfoSalaryClone((Salarie) obj));
		}
		//
		return l;
	}

	/**
	 * Lance le calcul de la paie sur chaque salari� contenu dans la liste re�ue en param�tre. Cette liste est compos�e des �l�ments de type Salarie.
	 * 
	 * @param listOfSalaries
	 *            liste des agents � utiliser pour le calcul des salaires
	 */
	public void execute(List listOfSalaries)
	{
		listOfParubq = buildListOfAllRubrique();

		parameterOfSalary.setThreadmax(threadmax);

		nbrSalaries = listOfSalaries.size();
//		if(ui != null)
//			ui.mettreAJourProgressBar(nbrSalariesTraite);
		
		for (Object obj : listOfSalaries)
		{
			try
			{
				if (poolSrev == null)
				{
					poolSrev = Executors.newFixedThreadPool(threadmax);
				}
				//poolSrev.execute(new ClsTraiterSalaireThread(this, (Salarie) obj));
			}
			catch (RejectedExecutionException e)
			{
				e.printStackTrace();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * calcule le bulletin de paie d'un agent
	 * 
	 * @param agent
	 *            l'agent concern�
	 */
	
	public void traiterSalaire(Salarie agent)
	{
		boolean continuer = true;
		
		ClsInfoSalaryClone infoSalary = new ClsInfoSalaryClone(agent);

		ClsFictifParameterOfPay param = parameterOfSalary.clone();

		ClsFictifSalaryToProcess salaryToProcess = null;// new ClsFictifSalaryToProcess(param, infoSalary, param.getService());

		salaryToProcess.deleteFictifFictif(false, false,false);
		
		salaryToProcess.setTotal_nap(0);
		
		if ("O".equals(agent.getCals()))
		{
			boolean finArretPaie = false;
			String queryArretPaie = "from SuspensionPaie" + " where identreprise = '" + param.getDossier() + "'" + " and nmat = '" + agent.getNmat() + "'"
					+ " order by identreprise, nmat, ddar";

			List listOfArriere = salaryToProcess.getService().find(queryArretPaie);
			Date ddar = null;
			Date dfar = null;
			for (Object obj1 : listOfArriere)
			{
				ddar = ((SuspensionPaie) obj1).getDdar();
				dfar = ((SuspensionPaie) obj1).getDfar();
				
				if (ddar.compareTo(param.getFirstDayOfMonth()) <= 0 && dfar.compareTo(param.getLastDayOfMonth()) >= 0)
				{
					finArretPaie = true;
					break;
				}
			}
			if (finArretPaie)
			{
				continuer = false;
				error = salaryToProcess.getInfoSalary().getComp_id().getNmat()+ " "+param.errorMessage("INF-10345",param.getLangue());
				param.insererLogMessage(error);
			}
			else
			{
				salaryToProcess.setEmbauche(false);
				if (agent.getDtes() != null && param.getFirstDayOfMonth().compareTo(agent.getDtes()) <= 0
						&& param.getLastDayOfMonth().compareTo(agent.getDtes()) >= 0 && param.getNumeroBulletin() == 9)
				{
					salaryToProcess.setEmbauche(true);
				}
				
				param.setStc(false);
				if ("MU".equals(agent.getMrrx()) || "RA".equals(agent.getMrrx()))
				{
					if (agent.getDmrr() != null)
					{
						if (param.getFirstDayOfMonth().compareTo(agent.getDmrr()) <= 0
								&& param.getLastDayOfMonth().compareTo(agent.getDmrr()) >= 0 && param.getNumeroBulletin() == 9)
						{
							param.setStc(true);
						}
						else
						{
							if (param.getFirstDayOfMonth().compareTo(agent.getDmrr()) > 0)
								continuer = false;
						}
					}
				}
			}//finarretpaie
			
			if (continuer)
			{
				salaryToProcess.traiterSalaireFictif();
				
				if (!param.isPbWithCalulation())
				{
					salaryToProcess.deletePseudoEVFictif();
				}
				else
				{
					param.setPbWithCalulation(false);
					param.insererLogMessage(param.getError());
				}
			}
			
		}//calc = 0
		
	}
	
	public void incrementerNombreSalariesTraite()
	{
		ClsTraiterSalaireThread.NBRE_AGENT_TRAITE++;
		nbrSalariesTraite++;
//		httpSession.setAttribute(ClsSessionObjectName.SESSION_O_CALCUL_NOMBRE_SALARIES_TRAITE, nbrSalariesTraite);
//		if(ui != null)
//			ui.mettreAJourProgressBar(nbrSalariesTraite);

	}

}
