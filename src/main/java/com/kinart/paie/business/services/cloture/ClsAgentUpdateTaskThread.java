package com.kinart.paie.business.services.cloture;

import javax.servlet.http.HttpServletRequest;

import com.kinart.paie.business.model.Salarie;
import org.hibernate.Session;

public class ClsAgentUpdateTaskThread implements Runnable
{
	public ClsGlobalUpdate globalUpdate;
	public int nbTotal = 0;

	public int nbTraite = 0;

	public Salarie agent;

	HttpServletRequest request;
	
	ClsUpdateBulletinThread updateBulletin;
	
	ClsAgentUpdateBulletinThread agentUpdate;
	

	public ClsAgentUpdateTaskThread(HttpServletRequest request, ClsUpdateBulletinThread updateBulletin, Session oSession, Salarie agent, int nbTotal)
	{
		agentUpdate = new ClsAgentUpdateBulletinThread(updateBulletin, oSession, agent);
		this.nbTotal = nbTotal;
		this.agent = agent;
		this.request = request;
		this.updateBulletin = updateBulletin;
	}

	public void run()
	{
		try
		{
			agentUpdate.updateBulletin();
			
			updateBulletin.latch.countDown();
			
			nbTraite++;
			if ((nbTraite == this.nbTotal) || (nbTraite < 0))
			{
				globalUpdate._setEvolutionTraitement(request, "Mise � jour termin�e avec succ�s", false);
				globalUpdate._setTitreEvolutionTraitement(request, "");
			}
			else
			{
				globalUpdate._setEvolutionTraitement(request, "Mise � jour du salari� " + agent.getNmat() + " : " + agent.getNom() + " "
						+ agent.getPren() + " (" + nbTraite + "/" + this.nbTotal + ")", false, nbTraite, this.nbTotal);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}
		finally
		{

		}
	}

}
