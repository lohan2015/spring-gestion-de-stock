/**
 * 
 */
package com.kinart.paie.business.services.cloture;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.kinart.paie.business.model.LogMessage;
import com.kinart.paie.business.model.ParamData;
import com.kinart.paie.business.model.Salarie;
import com.kinart.paie.business.services.utils.*;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ClsSalaryProcessCentralisation implements Runnable
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run()
	{
		try
		{
			ParameterUtil.println("Traitement du salari� : " + salarie.getNom());
			lcentra();// Lancement de la centralisation sur un salari�.

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			ParameterUtil.println(ex.getMessage());
		}
		ParameterUtil.println("Fin du traitement du salari� : " + salarie.getNom());
	}

	private Salarie salarie = new Salarie();

	private GeneriqueConnexionService service;

	private ClsCentralisationService centraservice = null;

	private ICentralisation oCentralisation;

	private String mess2, mess;

	private char cas;

	private HttpServletRequest request;

	private String dateFormat = "dd-MM-yyyy";

	public UIClotureMensuelle ui;

	private ClsGlobalUpdate globalUpdate = null;

	private Object callMethode(Object obj, String nomMethode, Class classe, Object valeurParam)
	{
		if (obj == null)
			return null;
		try
		{
			Method oMethod = obj.getClass().getMethod(nomMethode, classe);
			if (oMethod != null)
				return oMethod.invoke(obj, valeurParam);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param salarie
	 * @param aamm
	 * @param nbul
	 * @param cuti
	 * @param clang
	 * @param nddd
	 * @param date_Comptable
	 */
	public ClsSalaryProcessCentralisation(HttpServletRequest request, GeneriqueConnexionService service, ClsGlobalUpdate globalUpdate, ClsCentralisationService centraservice, Salarie salarie, String aamm, Integer nbul, String cuti, String clang, Integer nddd, ClsDate date_Comptable, String cdos)
	{
		super();
		this.request = request;
		this.salarie = salarie;
		oCentralisation = new ClsCentralisation(request, globalUpdate, centraservice, aamm, nbul, cuti, clang, nddd, date_Comptable, cdos);

		ParamData fnom1 = service.findAnyColumnFromNomenclature(cdos, "", "99", "CLASSECENT", "2");
		//Rhfnom fnom1 = (Rhfnom) service.get(Rhfnom.class, new RhfnomPK(cdos, 99, "CLASSECENT", 2));
		if (fnom1 != null && StringUtils.isNotBlank(fnom1.getVall()))
		{
			try
			{
				oCentralisation = (ICentralisation) Class.forName("com.kinart.paie.business.services.cloture." + fnom1.getVall()).newInstance();

				String datevaleurcomptable = ParameterUtil.getSessionObject(request, "datevaleurcomptable");
				Date dateValeur = date_Comptable.getDate();
				if (StringUtils.isNotBlank(datevaleurcomptable))
				{
					String dateformat = ParameterUtil.getSessionObject(request, ParameterUtil.SESSION_FORMAT_DATE);
					dateValeur = new ClsDate(datevaleurcomptable, dateformat).getDate();
				}
				callMethode(oCentralisation, "setRequest", HttpServletRequest.class, request);
				callMethode(oCentralisation, "setGlobalUpdate", ClsGlobalUpdate.class, globalUpdate);
				callMethode(oCentralisation, "setCentraservice", ClsCentralisationService.class, centraservice);
				callMethode(oCentralisation, "setAamm", String.class, aamm);
				callMethode(oCentralisation, "setNbul", Integer.class, nbul);
				callMethode(oCentralisation, "setCuti", String.class, cuti);
				callMethode(oCentralisation, "setClang", String.class, clang);
				callMethode(oCentralisation, "setNddd", Integer.class, nddd);
				callMethode(oCentralisation, "setDate_Comptable", ClsDate.class, date_Comptable);
				callMethode(oCentralisation, "setDate_Valeur", ClsDate.class, new ClsDate(dateValeur));
				callMethode(oCentralisation, "setCdos", String.class, cdos);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}
		else
		{
			if (StringUtils.equals(globalUpdate.nomClient, ClsEntreprise.TASIAST_MAURITANIE))
			{
				//oCentralisation = new ClsCentralisationTasiast(request, globalUpdate, centraservice, aamm, nbul, cuti, clang, nddd, date_Comptable, cdos);
			}
			if (StringUtils.equals(globalUpdate.nomClient, ClsEntreprise.BGFIGE) || StringUtils.equals(globalUpdate.nomClient, ClsEntreprise.SONIBANK))
			{
//				Rhfnom typeBGFI = (Rhfnom) service.get(Rhfnom.class, new RhfnomPK(cdos, 99, "PAYSBGFI", 2));
//				if(typeBGFI !=null ){
//					if(StringUtils.isNotEmpty(typeBGFI.getVall()) && "CONGO".equalsIgnoreCase(typeBGFI.getVall()))
//							oCentralisation = new ClsCentralisationBGFICongo(request, globalUpdate, centraservice, aamm, nbul, cuti, clang, nddd, date_Comptable, cdos);
//					else if(StringUtils.isNotEmpty(typeBGFI.getVall()) && "GUINEE".equalsIgnoreCase(typeBGFI.getVall()))
//							oCentralisation = new ClsCentralisationBGFIGuineeEquatoriale(request, globalUpdate, centraservice, aamm, nbul, cuti, clang, nddd, date_Comptable, cdos);
//					else oCentralisation = new ClsCentralisationBGFIGE(request, globalUpdate, centraservice, aamm, nbul, cuti, clang, nddd, date_Comptable, cdos);
//				} else oCentralisation = new ClsCentralisationBGFIGE(request, globalUpdate, centraservice, aamm, nbul, cuti, clang, nddd, date_Comptable, cdos);
			}
			ParamData fnom = service.findAnyColumnFromNomenclature(cdos, "", "99", "PAR-CENTRA", "1");
			//Rhfnom fnom = (Rhfnom) service.get(Rhfnom.class, new RhfnomPK(cdos, 99, "PAR-CENTRA", 1));
			if (fnom != null && StringUtils.equalsIgnoreCase("SDV", fnom.getVall()))
			{
				//oCentralisation = new ClsCentralisationSDV(request, globalUpdate, centraservice, aamm, nbul, cuti, clang, nddd, date_Comptable, cdos);
			}
			if (fnom != null && StringUtils.equalsIgnoreCase("DELTA-BANK", fnom.getVall()))
			{
				oCentralisation = new ClsCentralisationBank(request, globalUpdate, centraservice, aamm, nbul, cuti, clang, nddd, date_Comptable, cdos);
			}
		}

		this.service = service;
		this.centraservice = centraservice;
		mess = "";
		mess2 = "";
		dateFormat = ParameterUtil.getSessionObject(request, ParameterUtil.SESSION_FORMAT_DATE);
		this.globalUpdate = globalUpdate;
		this.globalUpdate.setService(service);
		this.globalUpdate.getParameter().setService(service);

	}

	public void lcentra() throws Exception
	{
		List list;
		String query = null;
		Session session = null;
		Transaction tx = null;
		boolean continuer = true;
		// Connection conn = null;
		// Statement statement = null;
		try
		{
			// -- Lancement de la centralisation
			// ---------------------------------------------------------------------------------
			// PROCEDURE lcentra (l_options IN VARCHAR2, l_pipe_name IN VARCHAR2) IS
			//
			// BEGIN
			// --BEGIN
			// -- ALTER ROLLBACK SEGMENT R05 ONLINE;
			// --EXCEPTION
			// -- WHEN OTHERS THEN NULL;
			// --END;
			// BEGIN
			// SET TRANSACTION USE ROLLBACK SEGMENT R05;
			// EXCEPTION
			// WHEN OTHERS THEN NULL;
			// END;
			//

			// N�cessaire pour les transactions.
			if (session != null)
				service.closeSession(session);

			session = service.getSession();

			try
			{
				tx = null;
				tx = session.beginTransaction();
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
				tx = session.getTransaction();
				tx.begin();
			}

			oCentralisation.setSession(session);
			// conn = session.connection();
			// statement = conn.createStatement();

			// -- Recuperation des donnees du lanceur.
			// wpdos_cdos := SUBSTR(l_options,1,2);
			// aamm := SUBSTR(l_options,3,6);
			// nbul := TO_NUMBER(SUBSTR(l_options,9,1));
			// cuti := SUBSTR(l_options,10,4);
			// nddd := TO_NUMBER(SUBSTR(l_options,14,1));
			// Date_Comptable := TO_DATE(SUBSTR(l_options,15,8),'YYYYMMDD');
			//
			// sp�cifique cnss
			if (StringUtils.equals(globalUpdate.nomClient, ClsEntreprise.CNSS) || StringUtils.equals(globalUpdate.nomClient, ClsEntreprise.COMILOG))
			{
				//				-- suppression CP_INT
				//				   -- MM 02/02/2000
				//				   DELETE FROM cp_int WHERE cdos = wpdos_cdos;
				//				   DELETE FROM cp_analyt WHERE cdos = wpdos_cdos;
				session.createSQLQuery("Delete From Cp_int where cdos='" + oCentralisation.getCdos() + "'").executeUpdate();
				session.createSQLQuery("Delete From Cp_analyt where cdos='" + oCentralisation.getCdos() + "'").executeUpdate();
			}

			// -- DBMS_OUTPUT.ENABLE(50000);
			//
			//
			// clang := PA_PAIE.rec_clang(wpdos_cdos,cuti);
			//
			// BEGIN
			// SELECT cdos, comp
			// INTO wpdos_cdos, wpdos_comp
			// FROM pados
			// WHERE cdos = wpdos_cdos;
			// EXCEPTION
			// WHEN NO_DATA_FOUND THEN null;
			// END;
			//

			oCentralisation.setComp(CharUtils.toChar(centraservice.getCompFromRhpdos(oCentralisation.getCdos())));
			//ParameterUtil.println("---oCentralisation.getComp() = "+oCentralisation.getComp());

			// IF init THEN
			//
			// IF deltacom AND centra_dos THEN
			// --- si centra sur un seul dossier on ne supprime pas toute les ecritures - uniquement la piece du dos courant.
			// DELETE FROM cp_int WHERE cdos = numdos_centra AND numpce = Num_Piece;
			// ELSE
			// -- suppression CP_INT
			// -- MM 02/02/2000
			// DELETE FROM cp_int WHERE cdos = wpdos_cdos;
			// END IF;
			//
			// IF NOT centra THEN
			// ROLLBACK;
			// cas := 'E';
			// ELSE
			// COMMIT;
			// cas := 'F';
			// mess := PA_PAIE.erreurp('INF-10346',clang);
			// END IF;
			// ELSE
			// cas := 'E';
			// END IF;
			// DBMS_PIPE.PACK_MESSAGE(cas);
			// DBMS_PIPE.PACK_MESSAGE(mess);
			// status := DBMS_PIPE.SEND_MESSAGE(pipe_name);
			//
			if (oCentralisation.init(oCentralisation.getComp()))
			{
				// sp�cifique cnss
				//le bloc n'est pas ex�cut� pour la cnss
				if (StringUtil.notEquals(globalUpdate.nomClient, ClsEntreprise.CNSS))
				{
					if (oCentralisation.getDeltacom() && oCentralisation.getCentra_dos())
					{
						centraservice.viderTableCPINTWithPiece(session, oCentralisation.getNumdos_centra(), oCentralisation.getNum_Piece());
						oCentralisation.setV_cdos(oCentralisation.getNumdos_centra());
					}
					else
					{
						//centraservice.viderTableCPINT(session,oCentralisation.getCdos());
						centraservice.viderTableCPINTWithPiece(session, oCentralisation.getCdos(), oCentralisation.getNum_Piece());
						oCentralisation.setV_cdos(oCentralisation.getCdos());
					}
				}

				if (!centra())
				{
					if (tx != null && tx.isActive())
						tx.rollback();
					cas = 'E';
					continuer = false;
				}
				else
				{
					try
					{
						// sp�cifique cnss
						if (StringUtils.equals(globalUpdate.nomClient, ClsEntreprise.CNSS))
						{
							//((ClsCentralisationCNSS)oCentralisation).saveAll();
							((ClsCentralisation) oCentralisation).saveAllCNSS();
						}

						centraservice.updateLibErrTableCPINT(session, oCentralisation.getV_cdos());
					}
					catch (Exception e)
					{
						e.printStackTrace();
						if (tx != null && tx.isActive())
							tx.rollback();
						continuer = false;
					}
					if (continuer)
						tx.commit();
					cas = 'F';
					mess = erreurp("INF-10346", oCentralisation.getClang());

				}
			} // END IF init
			else
			{
				ParameterUtil.println("Cas ou le init ne marche pas");
				cas = 'E';
				//
//				if (ui != null)
//					if (ui.progressbar != null)
//						ui.progressbar.refresh();
			}
			logMessage(cas);
			logMessage(mess);

			// END lcentra;

		}
		catch (Exception ex)
		{
			// EXCEPTION
			// WHEN OTHERS THEN
			// ROLLBACK;
			//
			// mess2 := '*** PACENTRA Erreur ' || TO_CHAR(SQLCODE);
			// INSERT INTO palog VALUES (wpdos_cdos, 'CENT', sysdate, mess2 );
			// mess2 := '*** Text ' || SUBSTR(SQLERRM, 1, 90);
			// INSERT INTO palog VALUES (wpdos_cdos, 'CENT', sysdate, mess2 );
			// COMMIT;
			//
			// DBMS_PIPE.PACK_MESSAGE('E');
			// DBMS_PIPE.PACK_MESSAGE('Erreur : ' || SUBSTR(SQLERRM, 1, 90) );
			// status := DBMS_PIPE.SEND_MESSAGE(pipe_name);
			//
			ParameterUtil.println("Exeption dans lcentra() :" + ex.getCause());
			ex.printStackTrace();
			if (tx != null)
			{
				//if (tx.wasCommitted())
					tx.rollback();
			}
			mess2 = "*** PACENTRA Erreur " + ex.getMessage();
			// tx = session.beginTransaction();
			query = "INSERT INTO LogMessage VALUES ('" + oCentralisation.getCdos() + "', 'CENT', '" + ClsDate.getDateS(new Date(System.currentTimeMillis()), this.dateFormat) + "', '" + mess2 + "' )";
			//query = "INSERT INTO Rhtlog VALUES ('" + oCentralisation.getCdos() + "', 'CENT', '" + ClsDate.getDateS(new Date(System.currentTimeMillis()), "yyyyMMdd") + "', '" + mess2 + "' )";
			try
			{
				service.insertIntoTable(query);

			}
			catch (Exception ex1)
			{
				ex1.printStackTrace();
				throw ex1;
			}
			// tx.commit();
			logMessage(mess2);
			throw ex;
		}
		finally
		{
			try
			{
				// if(statement!=null)statement.close();
				// if(conn!=null && !conn.isClosed())conn.close();
				// if(session!=null && session.isOpen())session.close();
				tx = null;
				service.closeSession(session);
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				ParameterUtil.println("Exeption dans lcentra() clause finally:" + ex.getCause());
				throw ex;
			}
		}
		ParameterUtil.println("Sortie de lcentra()");
	}

	public boolean centra() throws Exception
	{
		try
		{
			ParameterUtil.println("Entr�e dans centra()");
			// FUNCTION centra RETURN BOOLEAN IS
			//
			// sal_rst VARCHAR2(4);
			// nbSalaries SMALLINT;
			//
			// CURSOR curs_sal IS
			// SELECT pasa01.* FROM pasa01
			// WHERE cdos = wpdos_cdos
			// ORDER BY cdos,nmat;
			//
			// BEGIN
			// -- ----- Calcul du nbre de salaries a traiter
			// salaries := chg_sal;
			// nbSalaries := nb_sal;
			//
			// WHILE true LOOP
			// LOCK TABLE pasa01 IN EXCLUSIVE MODE;
			// IF SQLCODE = 0 THEN
			// LOCK TABLE pados IN EXCLUSIVE MODE;
			// IF SQLCODE = 0 THEN
			// LOCK TABLE paevar IN EXCLUSIVE MODE;
			// IF SQLCODE = 0 THEN
			// LOCK TABLE paevcg IN EXCLUSIVE MODE;
			// IF SQLCODE = 0 THEN
			// LOCK TABLE paprent IN EXCLUSIVE MODE;
			// IF SQLCODE = 0 THEN
			// LOCK TABLE paprlig IN EXCLUSIVE MODE;
			// IF SQLCODE = 0 THEN
			// LOCK TABLE pacalc IN EXCLUSIVE MODE;
			// IF SQLCODE = 0 THEN
			// LOCK TABLE pacumu IN EXCLUSIVE MODE;
			// IF SQLCODE = 0 THEN
			// LOCK TABLE paconge IN EXCLUSIVE MODE;
			// IF SQLCODE = 0 THEN
			// LOCK TABLE pafic IN EXCLUSIVE MODE;
			// IF SQLCODE = 0 THEN
			// EXIT;
			// END IF;
			// END IF;
			// END IF;
			// END IF;
			// END IF;
			// END IF;
			// END IF;
			// END IF;
			// END IF;
			// END IF;
			// EXIT;
			// END LOOP;
			//
			// OPEN curs_sal;
			// LOOP
			// FETCH curs_sal INTO salarie;
			// EXIT WHEN curs_sal%NOTFOUND;
			//
			// DBMS_PIPE.PACK_MESSAGE('I');
			// mess := salarie.nmat || ltrim(to_char(nbSalaries,'0999'));
			// DBMS_PIPE.PACK_MESSAGE(mess);
			// status := DBMS_PIPE.SEND_MESSAGE(pipe_name);
			//
			// nbSalaries := nbSalaries - 1;
			// Num_Tiers := SUBSTR(salarie.ccpt,1,8);
			//
			// -- MM 11/2003 a faire au niveau des rubriques
			// -- Chargement section et etablissement (pour le salarie traite)
			// --IF NOT charg_etssec THEN
			// -- CLOSE curs_sal;
			// -- RETURN FALSE;
			// --END IF;
			//
			// -- Traitement des elements de la table de calcul
			// IF NOT lect_rub_cal THEN
			// CLOSE curs_sal;
			// RETURN FALSE;
			// END IF;
			//
			// END LOOP;
			// CLOSE curs_sal;
			//
			// RETURN TRUE;
			//
			// END centra;
			String sal_rst;
			int nbSalaries;

			List<Salarie> salaries = centraservice.getListeSalarieFromRhpagent(oCentralisation.getCdos(), oCentralisation.getAamm(), oCentralisation.getNbul());
			nbSalaries = salaries.size();
			String cpcpt = null;

//			if (ui != null)
//				ui.debutAffichageProgressBar(nbSalaries);

			for (int i = 1; i <= salaries.size(); i++)
			{
//				if (StringUtils.equals("O", ParameterUtil.getSessionObject(request, ClsSessionObjectName.SESSION_STOP_ALL_THREADS)))
//				{
//					break;
//				}
				salarie = salaries.get(i - 1);
				ParameterUtil.println("------------------Centra---Salarie=" + salarie.getNmat() + " - " + salarie.getNom() + " " + salarie.getPren());

				// // CURSOR curs_sal IS
				// // SELECT pasa01.* FROM pasa01
				// // WHERE cdos = wpdos_cdos
				// // ORDER BY cdos,nmat;
				// try{
				// request = "from Rhpagent where comp_id.cdos='" + oCentralisation.getWpdos_cdos() + "' group by comp_id.cdos,comp_id.nmat";
				// list = service.find(request);
				// }
				// catch(DataAccessException ex){
				// logMessage(ex.getMessage());
				// }
				cpcpt = salarie.getCcpt();
//				if (!ParameterUtil._isStringNull(cpcpt)) continue;
//					oCentralisation.setNum_Tiers(StringUtils.substring(cpcpt, 0, 8));
//				else oCentralisation.setNum_Tiers("");
				// sp�cifique cnss
				if (StringUtils.equals(globalUpdate.nomClient, ClsEntreprise.CNSS))
				{
					//					Num_Tiers := ' ';
					oCentralisation.setNum_Tiers(" ");
					//					 -- Chargement section et etablissement (pour le salarie traite)
					//				       IF NOT charg_etssec THEN
					//				          CLOSE curs_sal;
					//				          RETURN FALSE;
					//				       END IF;
					if (!oCentralisation.chargerEtablissement(salarie, ""))
						return false;

				}
				// mess = salarie.getComp_id().getNmat() + ClsStringUtil.int2string(nbSalaries, 3, 4, i+"/", " Salari�s").trim();
				mess = ClsTreater._getResultat("Salari� courant ", "INF-80-RH-182", false).getLibelle()
					+ salarie.getNmat() + " : " + salarie.getNom() + " " + salarie.getPren();
				globalUpdate._setEvolutionTraitement(request, mess, false, i, nbSalaries);

//				if (ui != null)
//					ui.mettreAJourProgressBar(i);

				logMessage(mess);
				if (!oCentralisation.lect_rub_cal(salarie))
				{
					ParameterUtil.println("-------------------->Pas d'elements � traiter pour le salari� " + salarie.getNmat());
					return false;
				}
			}
			globalUpdate._setEvolutionTraitement(request, "INF-01112", false);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			throw ex;
		}
		return true;

		// if(list!=null&&list.size()>0){
		// for (Object object : list) {
		// salarie = (Rhpagent)object;
		// Num_Tiers = salarie.getCcpt().substring(0,8);
		// mess = salarie.getComp_id().getNmat() + ClsStringUtil.int2string(nbSalaries, 3, 4, "", "").trim();
		// logMessage(mess);
		// nbSalaries = nbSalaries - 1;
		// if(!lect_rub_cal()){
		// return false;
		// }
		// }
		// }
	}

	private String erreurp(String codErreur, String langue, Object... param)
	{
		//		ClsParameterOfPay mPayParamOfPay = new ClsParameterOfPay();
		this.globalUpdate.getParameter().setService(service);
		return this.globalUpdate.getParameter().errorMessage(codErreur, langue, param);
		//		return mPayParamOfPay.errorMessage(codErreur, langue, param);
	}

	private void logMessage(Object objMessage)
	{
		ParameterUtil.println("Log message = " + objMessage.toString());
	}

	public Salarie getSalarie()
	{
		return salarie;
	}

	public void setSalarie(Salarie salarie)
	{
		this.salarie = salarie;
	}

	/**
	 * @return the service
	 */
	public GeneriqueConnexionService getService()
	{
		return service;
	}

	/**
	 * @param service
	 *            the service to set
	 */
	public void setService(GeneriqueConnexionService service)
	{
		this.service = service;
	}

	public ClsCentralisationService getCentraservice()
	{
		return centraservice;
	}

	public void setCentraservice(ClsCentralisationService centraservice)
	{
		this.centraservice = centraservice;
	}

	public ICentralisation getOCentralisation()
	{
		return oCentralisation;
	}

	public void setOCentralisation(ICentralisation centralisation)
	{
		oCentralisation = centralisation;
	}

	public String getMess2()
	{
		return mess2;
	}

	public void setMess2(String mess2)
	{
		this.mess2 = mess2;
	}

	public String getMess()
	{
		return mess;
	}

	public void setMess(String mess)
	{
		this.mess = mess;
	}

	public char getCas()
	{
		return cas;
	}

	public void setCas(char cas)
	{
		this.cas = cas;
	}

}
