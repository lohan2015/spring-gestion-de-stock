/**
 * 
 */
package com.kinart.paie.business.services.cloture;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;

import com.kinart.paie.business.model.LogMessage;
import com.kinart.paie.business.model.Salarie;
import com.kinart.paie.business.services.calcul.ClsParameterOfPay;
import com.kinart.paie.business.services.utils.ClsDate;
import com.kinart.paie.business.services.utils.ClsStringUtil;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.kinart.paie.business.services.utils.ParameterUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.dao.DataAccessException;

/**
 * @author c.mbassi
 *
 */
public class ClsSalaryTalkerCentralisation implements Callable<String>{
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public String call() throws Exception{
		try{
			ParameterUtil.println("D�but du traitement du salari� : " + wsal01.getNom());
			 FutureTalker.ifInterruptedStop();
			 lcentra();//Lancement de la centralisation sur un salari�.
			 FutureTalker.ifInterruptedStop();
		}
		catch(Exception ex){
			ParameterUtil.println(ex.getMessage());
		}
		return "Fin du traitement du salari� : " + wsal01.getNom();
	}
	private Salarie wsal01 = new Salarie();
	private HttpServletRequest request;
	private GeneriqueConnexionService service;
	private ICentralisation oCentralisation;
	private String w_mess2, w_mess;
	private char w_cas;
	/**
	 * @param wsal01
	 * @param w_aamm
	 * @param w_nbul
	 * @param w_cuti
	 * @param w_clang
	 * @param w_nddd
	 * @param date_Comptable
	 * @param wpdos_cdos
	 * @param service
	 */
	public ClsSalaryTalkerCentralisation(HttpServletRequest request, GeneriqueConnexionService service, ClsGlobalUpdate globalUpdate, ClsCentralisationService centraservice, Salarie wsal01, String w_aamm,
										 Integer w_nbul, String w_cuti, String w_clang, Integer w_nddd,
										 ClsDate date_Comptable, String wpdos_cdos) {
		super();
		this.wsal01 = wsal01;
		this.request = request;
		oCentralisation =  new ClsCentralisation(request,globalUpdate,centraservice, w_aamm,w_nbul,w_cuti,w_clang,w_nddd,date_Comptable,wpdos_cdos);;
		this.service = service;
		w_mess = "";
		w_mess2 = "";
	}
	public void lcentra()throws Exception{
		ParameterUtil.println("Entr�e dans lcentra()");
		List list ;
		String request = null;
		Session session = null;
		Transaction tx = null;
// 		Connection conn = null;
// 		Statement statement = null;
		try{
//				-- Lancement de la centralisation
//			---------------------------------------------------------------------------------
//			PROCEDURE lcentra (l_options IN VARCHAR2, l_pipe_name IN VARCHAR2) IS
//
//			BEGIN
//			   --BEGIN
//			     -- ALTER ROLLBACK SEGMENT R05 ONLINE;
//			   --EXCEPTION
//			   --   WHEN OTHERS THEN NULL;
//			   --END;
//			   BEGIN
//			      SET TRANSACTION USE ROLLBACK SEGMENT R05;
//			   EXCEPTION
//			      WHEN OTHERS THEN NULL;
//			   END;
//
			
			//N�cessaire pour les transactions.
	 		session = service.getSession();
	 		tx = session.beginTransaction();
	 		
	 		oCentralisation.setSession(session);
//	 		conn = session.connection();
//	 		statement = conn.createStatement();
	 		
//			   -- Recuperation des donnees du lanceur.
//			   wpdos_cdos  := SUBSTR(l_options,1,2);
//			   w_aamm      := SUBSTR(l_options,3,6);
//			   w_nbul      := TO_NUMBER(SUBSTR(l_options,9,1));
//			   w_cuti      := SUBSTR(l_options,10,4);
//			   w_nddd      := TO_NUMBER(SUBSTR(l_options,14,1));
//			   Date_Comptable := TO_DATE(SUBSTR(l_options,15,8),'YYYYMMDD');
//

		
	 		
//			   -- DBMS_OUTPUT.ENABLE(50000);
//
//
//			   w_clang := PA_PAIE.rec_clang(wpdos_cdos,w_cuti);
//
//			   BEGIN
//			      SELECT cdos, comp
//			        INTO wpdos_cdos, wpdos_comp
//			        FROM pados
//			       WHERE cdos = wpdos_cdos;
//			   EXCEPTION
//			      WHEN NO_DATA_FOUND THEN null;
//			   END;
//
			list = null;
			try{
				request = "select cdos, comp from Rhpdo where cdos = '" + oCentralisation.getCdos()+ "'";
				list = service.find(request);
			}
			catch(DataAccessException ex){
				throw ex;
			}
			if(list!=null && list.size()>0){
				oCentralisation.setCdos(((Object[])list.get(0))[0].toString());
				oCentralisation.setComp(((Object[])list.get(0))[1].toString().charAt(0));
			}else{
			}
//			   IF init THEN
//
//			      IF deltacom AND centra_dos THEN
//			         --- si centra sur un seul dossier on ne supprime pas toute les ecritures - uniquement la piece du dos courant.
//			         DELETE FROM cp_int WHERE cdos = numdos_centra AND numpce = Num_Piece;
//			      ELSE
//			         -- suppression CP_INT
//			         -- MM 02/02/2000
//			         DELETE FROM cp_int WHERE cdos = wpdos_cdos;
//			      END IF;
//
//			      IF NOT centra THEN
//			         ROLLBACK;
//			         w_cas := 'E';
//			      ELSE
//			         COMMIT;
//			         w_cas := 'F';
//			         w_mess := PA_PAIE.erreurp('INF-10346',w_clang);
//			      END IF;
//			   ELSE
//			      w_cas := 'E';
//			   END IF;
//			   DBMS_PIPE.PACK_MESSAGE(w_cas);
//			   DBMS_PIPE.PACK_MESSAGE(w_mess);
//			   w_status := DBMS_PIPE.SEND_MESSAGE(w_pipe_name);
//
			if(oCentralisation.init(oCentralisation.getComp())){
				if(oCentralisation.getDeltacom() && oCentralisation.getCentra_dos()){
					request = "DELETE FROM CpInt WHERE cdos = '"+oCentralisation.getNumdos_centra()+"' AND numpce = '"+oCentralisation.getNum_Piece()+"'";
//			 		statement.executeUpdate(request);
					session.createQuery(request).executeUpdate();
				}
				else{
					request = "DELETE FROM CpInt WHERE cdos = '" + oCentralisation.getCdos() + "'";
//					statement.executeUpdate(request);
					session.createQuery(request).executeUpdate();
				}
				//
//				statement.close();
//				if(! conn.isClosed())
//					conn.close();
			if(! centra()){
				tx.rollback();
				w_cas = 'E';
			}
			else{
				tx.commit();
				w_cas = 'F';
				w_mess = erreurp("INF-10346",oCentralisation.getClang());
			}
		}//END IF init
		else{
			w_cas = 'E';
		}
			logMessage(w_cas);
			logMessage(w_mess);

//			END lcentra;
			
		}
		catch(Exception ex){
//			EXCEPTION
//			   WHEN OTHERS THEN
//			      ROLLBACK;
//
//			      w_mess2 := '*** PACENTRA Erreur ' || TO_CHAR(SQLCODE);
//			      INSERT INTO palog VALUES (wpdos_cdos, 'CENT', sysdate, w_mess2 );
//			      w_mess2 := '*** Text ' || SUBSTR(SQLERRM, 1, 90);
//			      INSERT INTO palog VALUES (wpdos_cdos, 'CENT', sysdate, w_mess2 );
//			      COMMIT;
//
//			      DBMS_PIPE.PACK_MESSAGE('E');
//			      DBMS_PIPE.PACK_MESSAGE('Erreur : ' || SUBSTR(SQLERRM, 1, 90) );
//			      w_status := DBMS_PIPE.SEND_MESSAGE(w_pipe_name);
//
			ParameterUtil.println("Exeption dans lcentra() :" + ex.getCause().getMessage());
			if(tx!=null)tx.rollback();
			w_mess2 = "*** PACENTRA Erreur " + ex.getMessage();
			tx = session.beginTransaction();
			request = "INSERT INTO LogMessage VALUES ('" + oCentralisation.getCdos() + "', 'CENT', '"+ClsDate.getDateS(new Date(System.currentTimeMillis()), "yyyyMMdd")+"', '"+w_mess2+"' )";
			try{
//			statement.executeUpdate(request);
			}catch(Exception ex1){

				throw ex1;
			}
			tx.commit();
			logMessage(w_mess2);
			throw ex;
		}
		finally{
			try{
//			if(statement!=null)statement.close();
//	 		if(conn!=null && !conn.isClosed())conn.close();
//	 		if(session!=null && session.isOpen())session.close();
			}
			catch(Exception ex){
				ParameterUtil.println("Exeption dans lcentra() clause finally:" + ex.getCause().getMessage());
				throw ex;
			}
		}
		ParameterUtil.println("Sortie de lcentra()");
	}
	public boolean centra() throws Exception{
		try{
			ParameterUtil.println("Entr�e dans centra()");
//		FUNCTION centra RETURN BOOLEAN IS
//
//		sal_rst    VARCHAR2(4);
//		wsal_rst   SMALLINT;
//
//		CURSOR curs_sal IS
//		     SELECT pasa01.* FROM pasa01
//		      WHERE cdos = wpdos_cdos
//		      ORDER BY cdos,nmat;
//
//		BEGIN
//		-- ----- Calcul du nbre de salaries a traiter
//		  w_retour := chg_sal;
//		  wsal_rst := nb_sal;
//
//		  WHILE true LOOP
//		    LOCK TABLE pasa01 IN EXCLUSIVE MODE;
//		    IF SQLCODE = 0 THEN
//		       LOCK TABLE pados IN EXCLUSIVE MODE;
//		       IF SQLCODE = 0 THEN
//		          LOCK TABLE paevar IN EXCLUSIVE MODE;
//		          IF SQLCODE = 0 THEN
//		             LOCK TABLE paevcg IN EXCLUSIVE MODE;
//		             IF SQLCODE = 0 THEN
//		                LOCK TABLE paprent IN EXCLUSIVE MODE;
//		                IF SQLCODE = 0 THEN
//		                   LOCK TABLE paprlig IN EXCLUSIVE MODE;
//		                   IF SQLCODE = 0 THEN
//		                      LOCK TABLE pacalc IN EXCLUSIVE MODE;
//		                      IF SQLCODE = 0 THEN
//		                         LOCK TABLE pacumu IN EXCLUSIVE MODE;
//		                         IF SQLCODE = 0 THEN
//		                            LOCK TABLE paconge IN EXCLUSIVE MODE;
//		                            IF SQLCODE = 0 THEN
//		                               LOCK TABLE pafic IN EXCLUSIVE MODE;
//		                               IF SQLCODE = 0 THEN
//		                                  EXIT;
//		                               END IF;
//		                            END IF;
//		                         END IF;
//		                      END IF;
//		                   END IF;
//		                END IF;
//		             END IF;
//		          END IF;
//		       END IF;
//		    END IF;
//		    EXIT;
//		  END LOOP;
//
//		  OPEN curs_sal;
//		  LOOP
//		    FETCH curs_sal INTO wsal01;
//		    EXIT WHEN curs_sal%NOTFOUND;
//
//		       DBMS_PIPE.PACK_MESSAGE('I');
//		       w_mess := wsal01.nmat || ltrim(to_char(wsal_rst,'0999'));
//		       DBMS_PIPE.PACK_MESSAGE(w_mess);
//		       w_status := DBMS_PIPE.SEND_MESSAGE(w_pipe_name);
//
//		       wsal_rst := wsal_rst - 1;
//		       Num_Tiers := SUBSTR(wsal01.ccpt,1,8);
//
//			 -- MM 11/2003 a faire au niveau des rubriques
//		       -- Chargement section et etablissement (pour le salarie traite)
//		       --IF NOT charg_etssec THEN
//		       --   CLOSE curs_sal;
//		       --   RETURN FALSE;
//		       --END IF;
//
//		       -- Traitement des elements de la table de calcul
//		       IF NOT lect_rub_cal THEN
//		          CLOSE curs_sal;
//		          RETURN FALSE;
//		       END IF;
//
//		    END LOOP;
//		    CLOSE curs_sal;
//
//		    RETURN TRUE;
//
//		END centra;
		String sal_rst;
		int wsal_rst;
		
		List<Salarie> w_retour = oCentralisation.chg_sal();
		wsal_rst = oCentralisation.getNb_sal();
		
////		CURSOR curs_sal IS
////	     SELECT pasa01.* FROM pasa01
////	      WHERE cdos = wpdos_cdos
////	      ORDER BY cdos,nmat;
//		try{
//			request = "from Rhpagent where comp_id.cdos='" + oCentralisation.getWpdos_cdos() + "' group by comp_id.cdos,comp_id.nmat";
//			list = service.find(request);
//		}
//		catch(DataAccessException ex){
//				logMessage(ex.getMessage());
//		}
		oCentralisation.setNum_Tiers(wsal01.getCcpt().substring(0,8));
		w_mess = wsal01.getNmat() + ClsStringUtil.int2string(wsal_rst, 3, 4, "", "").trim();
		logMessage(w_mess);
		if(!oCentralisation.lect_rub_cal(wsal01)){
			return false;
		}
		}
		catch(Exception ex){
			throw ex;
		}
		return true;
		
			//		if(list!=null&&list.size()>0){
			//			for (Object object : list) {
			//				wsal01 = (Rhpagent)object;				
			//			    Num_Tiers = wsal01.getCcpt().substring(0,8);
			//			    w_mess = wsal01.getComp_id().getNmat() + ClsStringUtil.int2string(wsal_rst, 3, 4, "", "").trim();
			//			    logMessage(w_mess);
			//			    wsal_rst = wsal_rst - 1;
			//			    if(!lect_rub_cal()){
			//			    	return false;
			//			    }
			//			}
			//		}
	}
	private String erreurp(String codErreur, String langue, Object ... param ){
		ClsParameterOfPay mPayParamOfPay = new ClsParameterOfPay();
		mPayParamOfPay.setService(service);
		return mPayParamOfPay.errorMessage(codErreur, langue, param);
	}
	private void logMessage(Object objMessage){
		ParameterUtil.println(objMessage.toString());
	}
	/**
	 * @return the wsal01
	 */
	public Salarie getWsal01() {
		return wsal01;
	}
	/**
	 * @param wsal01 the wsal01 to set
	 */
	public void setWsal01(Salarie wsal01) {
		this.wsal01 = wsal01;
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
	public ICentralisation getOCentralisation()
	{
		return oCentralisation;
	}
	public void setOCentralisation(ICentralisation centralisation)
	{
		oCentralisation = centralisation;
	}
	
}
