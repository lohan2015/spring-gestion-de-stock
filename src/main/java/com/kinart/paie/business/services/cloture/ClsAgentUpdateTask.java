package com.kinart.paie.business.services.cloture;

import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;

import com.kinart.paie.business.model.Salarie;
import org.hibernate.Session;


public class ClsAgentUpdateTask extends ClsAgentUpdateBulletin implements
		Callable<Boolean> {
	
	/*public ClsAgentUpdateTask(HttpServletRequest request,String user, ClsUpdateBulletin updateBulletin, ClsService service,Session oSession, ClsParameterOfPay parameter, String dateformat, Salarie  agent){
		super(request,user, updateBulletin, service,oSession,parameter, dateformat, agent);
	}
	*/
	public ClsAgentUpdateTask(ClsUpdateBulletin updateBulletin, Session oSession, Salarie agent){
		super(updateBulletin, oSession, agent);
	}
	public Boolean call() throws Exception {
		// TODO Auto-generated method stub
		return this.updateBulletin();
	}

}
