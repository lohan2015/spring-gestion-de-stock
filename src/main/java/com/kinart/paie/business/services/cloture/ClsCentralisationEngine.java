/**
 * 
 */
package com.kinart.paie.business.services.cloture;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;

import com.kinart.paie.business.model.Salarie;
import com.kinart.paie.business.services.utils.ClsDate;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import org.springframework.dao.DataAccessException;


/**
 * @author p.nouthio
 *
 */
public class ClsCentralisationEngine {
	private int threadmax = 5;
	ExecutorService poolSrev = null;
	private GeneriqueConnexionService service;
	private ClsCentralisationService centraservice;
	private HttpServletRequest request;
	public void lancerCentralisationSalaries(String w_aamm, int w_nbul, String w_cuti, String w_clang, int w_nddd, ClsDate dateComptable, String cdos)throws Exception{
////		CURSOR curs_sal IS
////	     SELECT pasa01.* FROM pasa01
////	      WHERE cdos = wpdos_cdos
////	      ORDER BY cdos,nmat;
//		try{
//			request = "from Rhpagent where comp_id.cdos='" + mClsCentralisation.getWpdos_cdos() + "' group by comp_id.cdos,comp_id.nmat";
//			list = service.find(request);
//		}
//		catch(DataAccessException ex){
//				logMessage(ex.getMessage());
//		}
		
//		CURSOR curs_sal IS
//	     SELECT pasa01.* FROM pasa01
//	      WHERE cdos = wpdos_cdos
//	      ORDER BY cdos,nmat;
		List<Salarie> list = null;
		try{
			list = (List<Salarie>)service.find("from Salarie where idEntreprise='" + cdos + "' order by idEntreprise,nmat");
		}
		catch(DataAccessException ex){
				throw ex;
		}
		if(list!=null && list.size()>0){
			for (Iterator<Salarie> iterator = list.iterator(); iterator.hasNext();) {
				try{
					if(poolSrev == null){
						poolSrev = Executors.newFixedThreadPool(threadmax);
					}
					Salarie rhpagent = (Salarie) iterator.next();
					ClsSalaryProcessCentralisation oSalaryCentraToProcess = new ClsSalaryProcessCentralisation(request,service,null,centraservice, rhpagent,w_aamm,w_nbul,w_cuti,w_clang,w_nddd,dateComptable,cdos);
					oSalaryCentraToProcess.setService(service); //TODO � r�cup�rer du contr�leur Spring (phase de production)
					poolSrev.execute(oSalaryCentraToProcess);
				}
				catch(Exception e){
					e.printStackTrace();
					//poolSrev.shutdown(); sinon le traitement gloabal est arr�t�!
				}
			}
		}
	}
	/**
	 * @param threadmax
	 * @param service
	 */
	public ClsCentralisationEngine(HttpServletRequest request,GeneriqueConnexionService service,ClsCentralisationService centraservice, int threadmax) {
		super();
		this.request = request;
		this.service = service;
		this.centraservice = centraservice;
		this.threadmax = threadmax;
	}
	/**
	 * @return the threadmax
	 */
	public int getThreadmax() {
		return threadmax;
	}
	/**
	 * @param threadmax the threadmax to set
	 */
	public void setThreadmax(int threadmax) {
		this.threadmax = threadmax;
	}
	/**
	 * @return the service
	 */
	public GeneriqueConnexionService getService() {
		return service;
	}
	/**
	 * @param service the service to set
	 */
	public void setService(GeneriqueConnexionService service) {
		this.service = service;
	}
}
