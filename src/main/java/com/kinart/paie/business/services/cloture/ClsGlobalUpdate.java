package com.kinart.paie.business.services.cloture;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.kinart.paie.business.model.*;
import com.kinart.paie.business.services.calcul.ClsParameterOfPay;
import com.kinart.paie.business.services.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.context.ApplicationContext;

public class ClsGlobalUpdate
{
	public UIClotureMensuelle ui;

	private ApplicationContext applicationContext;

	private boolean execExternalFunction = false;

	private String dossier = "";

	private String periode = "";

	private String langue = "";

	private int numerobulletin = 9;

	private GeneriqueConnexionService service = null;

	private ClsCentralisationService centraservice = null;

	private ClsParameterOfPay parameter = null;

	private String user_sm = "";

	private int w_nddd = 0;

	private Date dateComptable = null;

	private String session = "";

	private ClsUpdateBulletin update = null;

	//
	private String interbank = "";

	private String centraparcompte = "";

	private String genfile = "";

	private String nomasciifile = "";

	private String separateur = "";

	private String genhistory = "";

	//
	private String error = "";
	
	public String nomClient = "TOUT";

	// ////////////////PAR YANNICK POUR AFFICHAGE DU SUIVI EVOLUTION TRAITEMENT
	private HttpServletRequest request;

	private static ClsResultat initializeError = null;
	
	public boolean refreshConnextion = false;

	public synchronized String getUser_sm()
	{
		return user_sm;
	}

	public synchronized void setUser_sm(String user_sm)
	{
		this.user_sm = user_sm;
	}

	//
	public ClsGlobalUpdate(HttpServletRequest request, GeneriqueConnexionService service, ClsCentralisationService centraservice, ClsParameterOfPay parameter, String dossier,
			String periode, String langue, String userid, int numerobulletin, Date dateComptable, int w_nddd, String session, ClsUpdateBulletin update)
	{
		ParameterUtil.println("...userid:" + userid);
		this.request = request;
		this.dossier = dossier;
		this.periode = periode;
		this.langue = langue;
		this.numerobulletin = numerobulletin;
		this.service = service;
		this.centraservice = centraservice;
		this.centraservice.setRubriquesMap(new HashMap<String, ElementSalaire>());
		this.centraservice.setRhfnomsMap(new HashMap<String, ParamData>());
		this.parameter = parameter;
		this.dateComptable = dateComptable;
		this.w_nddd = w_nddd;
		this.session = session;
		this.setUser_sm(userid);
		//
		this.update = update;
		this.update.request = request;
		this.update.numerobulletin = numerobulletin;
		this.update.dossier = dossier;
		this.update.langue = langue;
		this.update.parameter = parameter;
		this.update.periode = periode;
		this.update.service = service;
		this.update.user = userid;
		this.update.bulletin = new ClsBulletin(service);
		initializeError = null;
		nomClient =  "TEST";//ClsConfigurationParameters.getConfigParameterValue(service, dossier, langue, ClsConfigurationParameters.NOM_CLIENT);
		refreshConnextion =  true;//StringUtils.equals("O", ClsConfigurationParameters.getConfigParameterValue(service, dossier, langue, ClsConfigurationParameters.REFRESHCONNECTION));
		this.update.setGenfile(parameter.getGenfile());
		this.update.setGenfilefolder(parameter.getGenfilefolder());
	}

	/** ********DEBUT DES FONCTIONS DE CENTRALISATION YANNICK************************************ */
	private void evolutionSimulateur()
	{
		Object nome =  request.getSession().getAttribute("SIMULATEURPAIE");
		if(nome != null)
		{
			try
			{
				MethodUtils.invokeMethod(nome, "bareProgressionCloture", null);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	// pour pouvoir concatener tous les messages d'erreurs issus de l'initialisation avant de les afficher
	public static void _setInitializeError(HttpServletRequest request,ClsGlobalUpdate gupdate, String message, boolean isError)
	{
		ClsResultat oResult = ClsTreater._getResultat(message, message, isError);
		initializeError = ClsTreater._concat(initializeError, oResult, ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
		if (isError)
			gupdate.logError(message);
	}
	
	public void _setInitializeError(HttpServletRequest request, String message, boolean isError)
	{
		ClsResultat oResult = ClsTreater._getResultat(message, message, isError);
		initializeError = ClsTreater._concat(initializeError, oResult, ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
		if (isError)
			this.logError(message);
	}

	public static ClsResultat _printInitializeError(HttpServletRequest request)
	{
		if(request != null)
			request.getSession().setAttribute(ClsSessionObjectName.SESSION_O_CENTRALISATION_MESSAGE_SUIVI, initializeError);
		return initializeError;
	}

	public void _setEvolutionTraitement(HttpServletRequest request, String message, boolean isError)
	{
		if (isError)
			this.error = message;
		ClsResultat oResult = ClsTreater._getResultat(message, message, isError);
		if (StringUtils.isBlank(oResult.getLibelle()) || StringUtils.isBlank(oResult.getDescription()))
			ParameterUtil.println("--------->Setting Message suivit =" + message);
		if(request != null) request.getSession().setAttribute(ClsSessionObjectName.SESSION_O_CENTRALISATION_MESSAGE_SUIVI, oResult);
		if (ui != null)
		{
			if (isError)
			{
//				ui.setShowannimationimage(false);
//				ui.setDisplayBlockingPopup(false);
			}
//			ui.setEvolutiontraitement(oResult.getLibelle(), isError);
		}

		if (StringUtils.equals("O", ParameterUtil.getSessionObject(request, ClsSessionObjectName.SESSION_STOP_ALL_THREADS)))
		{
			if (update.threadPool != null)
				update.threadPool.shutdown();
		}
		if (isError)
			this.logError(message);

		evolutionSimulateur();
	}

	public void _setEvolutionTraitement(HttpServletRequest request, String message, boolean isError, int nbrCourant, int nbrTotal)
	{
		if (isError)
			this.error = message;
		ClsResultat oResult = ClsTreater._getResultat(message, message, isError);
		if (StringUtils.isBlank(oResult.getLibelle()) || StringUtils.isBlank(oResult.getDescription()))
			ParameterUtil.println("--------->Setting Message suivit =" + message);
		if(request != null) 
		{
		request.getSession().setAttribute(ClsSessionObjectName.SESSION_O_CENTRALISATION_MESSAGE_SUIVI, oResult);
//		request.getSession().setAttribute(ClsSessionObjectName.SESSION_O_CENTRALISATION_SALARIES_TRAITES_SUIVI,
//				new ClsNomenclature(String.valueOf(nbrCourant), String.valueOf(nbrTotal)));
		}
		if (ui != null)
		{
//			if (isError)
//			{
//				ui.setShowannimationimage(false);
//				ui.setDisplayBlockingPopup(false);
//			}
//			ui.setEvolutiontraitement(oResult.getLibelle(), isError);
		}

		if (StringUtils.equals("O", ParameterUtil.getSessionObject(request, ClsSessionObjectName.SESSION_STOP_ALL_THREADS)))
		{
			if (update.threadPool != null)
				update.threadPool.shutdown();
		}
		evolutionSimulateur();
	}

	public void _setTitreEvolutionTraitement(HttpServletRequest request, String message)
	{
		this._setTitreEvolutionTraitement(request, message, false);
	}

	public void _setTitreEvolutionTraitement(HttpServletRequest request, String message, boolean isErrorMessage)
	{
		ClsResultat oResult = ClsTreater._getResultat(message, message, isErrorMessage);
		if(request != null) request.getSession().setAttribute(ClsSessionObjectName.SESSION_O_CENTRALISATION_TITRE_SUIVI, oResult);
		if (ui != null)
		{
//			if (isErrorMessage)
//			{
//				ui.setShowannimationimage(false);
//				ui.setDisplayBlockingPopup(false);
//			}
//			ui.setTraitementcourant(oResult.getLibelle());
		}
		evolutionSimulateur();
	}

	public ClsResultat _getEvolutionTraitement()
	{
		Object obj = null;
		if(request != null) obj = request.getSession().getAttribute(ClsSessionObjectName.SESSION_O_CENTRALISATION_MESSAGE_SUIVI);
		else
		{
			obj = ClsTreater._getResultat(" ", null, false);
		}
		if (obj != null)
		{
			ClsResultat result = (ClsResultat) obj;
			ParameterUtil.println("Dans la session, resultat =" + result.getLibelle());
			return result;
		}
		else
		{
			ParameterUtil.println("Dans la session, le result est null");
			return null;
		}
	}
	
	public boolean lancerCentralisation(String rubriqueAComptabiliser, String typeContrepartie, String compteContrepartie, String crediteurOuDebiteur)
	{
		//On les met dans la session
		if(request != null) 
		{
		request.getSession().setAttribute("rubriqueAComptabiliser", rubriqueAComptabiliser);
		request.getSession().setAttribute("typeContrepartie", typeContrepartie);
		request.getSession().setAttribute("compteContrepartie", compteContrepartie);
		request.getSession().setAttribute("crediteurOuDebiteur", crediteurOuDebiteur);
		}
		return this.lancerCentralisation();
	}

	public boolean lancerCentralisation()
	{
		boolean res = true;

		ClsSalaryProcessCentralisation oSalaryCentraToProcess = new ClsSalaryProcessCentralisation(request, service, this, centraservice, null, periode,
				numerobulletin, user_sm, langue, w_nddd, new ClsDate(dateComptable), dossier);
		oSalaryCentraToProcess.ui = this.ui;

		// oSalaryCentraToProcess.setService(service);

		try
		{
			String message = infoMessage("INF-01115");
			this._setTitreEvolutionTraitement(request, message);

			if(request != null) request.getSession().setAttribute(ClsSessionObjectName.SESSION_O_CENTRALISATION_NATURE_TRAITEMENT, "GMC");

			oSalaryCentraToProcess.setService(service);
			oSalaryCentraToProcess.lcentra();

			if (StringUtils.equals("O", ParameterUtil.getSessionObject(request, ClsSessionObjectName.SESSION_STOP_ALL_THREADS)))
			{
				return false;
			}

			ClsResultat oResult = this._getEvolutionTraitement();
			if (oResult != null && oResult.isErrormessage())
				return false;

			if ("O".equals(genfile))
			{
				res = true;
				//La banque est vu comme un client pour qui il faut g�n�rer le fichier
				//Donc on peut mettre ce bloc en commentaire;Yannick @145/04/2011
//				if ("O".equals(interbank))
//				{
//					res = genrateBank(session);
//				}
//				else
//				{
					res = genrateFile(session);
//					if(res)
//						if(ui!=null) ui.downloadFile();
//				}
				ParameterUtil.println("...res :" + res);
			}
			if(res)
			{
				if(request != null) request.getSession().removeAttribute(ClsSessionObjectName.SESSION_O_CENTRALISATION_SALARIES_TRAITES_SUIVI);
	
				this._setEvolutionTraitement(request, "", false, 0, 0);
	
				this._setTitreEvolutionTraitement(request, "");
				this._setEvolutionTraitement(request, "INF-10346", false);
				if(request != null) 
				{
				request.getSession().removeAttribute("rubriqueAComptabiliser");
				request.getSession().removeAttribute("typeContrepartie");
				request.getSession().removeAttribute("compteContrepartie");
				request.getSession().removeAttribute("crediteurOuDebiteur");
				request.getSession().removeAttribute("datevaleurcomptable");
				}
				
			}
			return res;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		finally
		{
			oSalaryCentraToProcess = (ClsSalaryProcessCentralisation) ObjectUtils.setNull(oSalaryCentraToProcess);
		}
	}

	/** ********FIN DES FONCTIONS DE CENTRALISATION************************************ */

	public void logError(String error)
	{	
		try 
		{
			LogMessage log = new LogMessage();
			log.setIdEntreprise(new Integer(this.dossier));
			log.setCuti(ParameterUtil.getSessionObject(request, ParameterUtil.SESSION_LOGIN));
			log.setDatc(new Date());
			log.setLigne(error);
			//
			this.service.save(log);
			System.out.println("********Erreur = "+error);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}		
	}
	
	class AfficheurFlux implements Runnable {

	    private final InputStream inputStream;

	    AfficheurFlux(InputStream inputStream) {
	        this.inputStream = inputStream;
	    }

	    private BufferedReader getBufferedReader(InputStream is) {
	        return new BufferedReader(new InputStreamReader(is));
	    }

	    public void run() {
	        BufferedReader br = getBufferedReader(inputStream);
	        String ligne = "";
	        try {
	            while ((ligne = br.readLine()) != null) {
	                System.out.println(ligne);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}

	
	private boolean sauvegardeBD()
	{
		
		String message = infoMessage(ClsTreater._getResultat("Sauvegarde de la base de donn�es", "INF-80-RH-183", false).getLibelle());
		this._setTitreEvolutionTraitement(request, message);
		
		//Si le serveur de bd et celui d'appli sont sur la mm machine, ou si � partir du serveur d'appli on peut cr�er un lien vers la bd 
		//(pour oracle, en installant par exemple le client oracle sur le serveur d'appli et en configurant le tnsnames.ora)
		String cmdScriptSauvegarde="";	
		List<ParamData> list = service.find("From ParamData where idEntreprise = '"+dossier+"' and ctab = 266 and cacc='BACKUPFILE' and nume = 2");
		if(!list.isEmpty())
		{
			ParamData fnom = list.get(0);
			cmdScriptSauvegarde = StringUtils.trim(fnom.getVall());
				
			if(StringUtils.isBlank(cmdScriptSauvegarde))
				return true;
			int result = 0;
			try
			{
	//			Runtime.getRuntime().exec("cmd /c start Aide.html"); // Sous Windows
	//			Runtime.getRuntime().exec("start Aide.html"); // Sous Mac OS X
	//			Runtime.getRuntime().exec("xdg-open Aide.html"); // Sous Linux
				cmdScriptSauvegarde = cmdScriptSauvegarde.replace("&A2", periode.substring(2, 4));
				cmdScriptSauvegarde = cmdScriptSauvegarde.replace("&A4", periode.substring(0, 4));
				cmdScriptSauvegarde = cmdScriptSauvegarde.replace("&A", periode.substring(0, 4));
				cmdScriptSauvegarde = cmdScriptSauvegarde.replace("&M", periode.substring(4, 6));
				cmdScriptSauvegarde = cmdScriptSauvegarde.replace("&B", this.getNumerobulletin()+"");
				cmdScriptSauvegarde = cmdScriptSauvegarde.replace("&D", dossier);
				Process p= Runtime.getRuntime().exec(cmdScriptSauvegarde);
				//attente de fin du processus
				AfficheurFlux fluxSortie = new AfficheurFlux(p.getInputStream());
	            AfficheurFlux fluxErreur = new AfficheurFlux(p.getErrorStream());

	            new Thread(fluxSortie).start();
	            new Thread(fluxErreur).start();

	            result = p.waitFor();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				update.error  = ClsTreater._getStackTrace(e);
				this.logError(update.error);
				return false;
			}
			if(result != 0)
			{
				update.error = ClsTreater._getResultat(
								"Erreur lors de la sauvegarde de la base de donn�es; v�rifier",
								"INF-80-RH-184", false).getLibelle();
				this.logError(update.error);
				return false;
			}
		}
		else
		{
		
			//Si le serveur de bd est different de celui d'appli et que la sauvegarde se fait via requete sql : alors ex�cuter les requetes se sauvegardes
			//T99, lib 1 : description, lib2=param�tres de la commande de sauvegarde,du genre D:\DELTA\BACKUP,PARAM2,PARAM3 lib3,4,5: requete de sauvegarde 
			//Le nom de la sauvegarde de la base DELTARH du dossier 01 du 21/01/2012 sera 21-01-2012DELTARH01.bak (pour sqlserver)
			//Exemple de requete de sauvegarde : BACKUP DATABASE DELTARH TO DISK=N'%1&A&M&B&D-DELTARH.bak
			String dt = new ClsDate(new Date()).getDateS("dd-MM-yyyy");
	//		String cheminSauvegarde = "";
	//		if(cheminSauvegarde.endsWith(File.pathSeparator)) cheminSauvegarde+=File.pathSeparator;
			List<ParamData> requetes = service.find("From ParamData where idEntreprise = '"+dossier+"' and ctab=99 and cacc='BACKUPFILE' order by nume");
			String params = StringUtils.EMPTY;
			String query;
			Session session = service.getSession();
			SQLQuery q = null;
			for(ParamData nome : requetes)
			{
				try
				{
					if(nome.getNume() == 2) params = nome.getVall();
					if(nome.getNume() > 2)
					{
						query = nome.getVall();
						if(StringUtils.isNotBlank(query))
						{
							query = query.replace("&A2", periode.substring(2, 4));
							query = query.replace("&A4", periode.substring(0, 4));
							query = query.replace("&A", periode.substring(0, 4));
							query = query.replace("&M", periode.substring(4, 6));
							query = query.replace("&B", this.getNumerobulletin()+"");
							query = query.replace("&D", dossier);
							String[] arrayParams = params.split(",");
							for(int i=0;i<arrayParams.length;i++)
							{
								query = query.replace("%"+(i+1), arrayParams[i]);
							}
							q = session.createSQLQuery(query);
							q.executeUpdate();
						}
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
					update.error  = ClsTreater._getStackTrace(e);
					this.logError(update.error);
					return false;
				}
			}
			service.closeSession(session);
		}
		
		return true;
	}
	
	public boolean generationHistorique()
	{
		boolean res = true;
		String message;

		if ("O".equals(genhistory))
		{
			if(request != null) request.getSession().setAttribute(ClsSessionObjectName.SESSION_O_CENTRALISATION_NATURE_TRAITEMENT, null);
			message = infoMessage("INF-01117");
			this._setTitreEvolutionTraitement(request, message);
			ParameterUtil.println("...genhistory");
			try
			{
				res = new ClsDataHistory().processHistory(request, service, this, dossier, periode, numerobulletin, user_sm);
				System.out.println("---------Fin de l'historisation");
			}
			catch (Exception e)
			{
				res = false;
				e.printStackTrace();
				this._setEvolutionTraitement(update.request, ClsTreater._getStackTrace(e), true);
				update.error  = ClsTreater._getStackTrace(e);
				this.logError(update.error);
				return false;
			}

		}
		return res;
	}
	public boolean lancerMiseAJour()
	{
		return this.lancerMiseAJour(true);
	}

	
	public boolean lancerMiseAJour(boolean avecHistorique)
	{
		if(!sauvegardeBD())
			return false;
		
		if (StringUtils.equals("O", ParameterUtil.getSessionObject(request, ClsSessionObjectName.SESSION_STOP_ALL_THREADS)))
		{
			return false;
		}

		boolean res = true;
		String message;
		ClsResultat oResult;

		Session oSession = null;
		Transaction tx = null;

		if(avecHistorique)
		{
			if(!generationHistorique())
				return false;
		}
		
		try
		{
			oSession = service.getSession();
			tx = oSession.beginTransaction();

			if (res)
			{
				message = infoMessage("INF-01119");
				// ClsTraiterSalaireThread.Res_cal = message + infoMessage("INF-00995");
				this._setTitreEvolutionTraitement(request, message);
				
				update.globalUpdate = this;

				boolean canExec = update.initVariables();
				System.out.println("--After init variables");
				if (!canExec)
					return false;

				canExec = update.verificationParametrage();
				System.out.println("--After verifications variables");
				if (!canExec)
				{
					oResult = ClsGlobalUpdate._printInitializeError(request);
					this._setEvolutionTraitement(request, oResult.getLibelle(), true);
					this.logError(oResult.getLibelle());
					if (tx != null && tx.isActive())
						tx.rollback();
					return false;
				}

				if (canExec)
				{
					update.ui = this.ui;
					if(request != null) request.getSession().setAttribute(ClsSessionObjectName.SESSION_O_CENTRALISATION_NATURE_TRAITEMENT, "MAJ");
					System.out.println("--Lancement de la maj");
					this.logError("D�but de la MAJ a "+new Date());
					refreshConnexion(oSession);
					update.execute(oSession);// il y une attente � ce niveau
				}

				if(request != null) request.getSession().setAttribute(ClsSessionObjectName.SESSION_O_CENTRALISATION_NATURE_TRAITEMENT, null);
				//
				res = update.getFinalResult();

				oResult = this._getEvolutionTraitement();
				if (oResult != null && oResult.isErrormessage())
					res = false;
				else
					res = true;

				if (!res)
				{
					this._setEvolutionTraitement(request, oResult.getLibelle(), true);
					if (tx != null && tx.isActive())
						tx.rollback();
					return false;
				}
				if (StringUtils.isNotBlank(this.error))
				{
					ParameterUtil.println("Message d'erreur = " + this.error);
					res = false;
					this.logError(this.error);
					if (tx != null && tx.isActive())
						tx.rollback();
					return false;
				}

				ParameterUtil.println("...update res :" + res);
				if (res)
				{
					refreshConnexion(oSession);
					
					if (!update.updateDossierDePaie(oSession))
					{
						error = parameter.errorMessage("ERR-90130", langue);
						ParameterUtil.println(error);
						this._setEvolutionTraitement(request, error, true);
						this.logError(error);
						if (tx != null && tx.isActive())
							tx.rollback();
						return false;
					}

					try
					{
						// Execution des m�thodes stock�s en nomenclatures
						execExternalFunctions(oSession);
					}
					catch (Exception e)
					{
						error = ClsTreater._getStackTrace(e);
						ParameterUtil.println(error);
						this._setEvolutionTraitement(request, error, true);
						this.logError(error);
						if (tx != null && tx.isActive())
							tx.rollback();
						return false;
					}

					//
					// ClsTraiterSalaireThread.Res_cal = "";
					// ClsTraiterSalaireThread.encours = infoMessage("INF-00440") + "...";
					this._setTitreEvolutionTraitement(request, "");
					System.out.println("------Avant appel de commit");
					// /--------------commentaire temporaire pour tests, => pas de maj au niveau de la bd
					//On va conditionner le commit
					String simulaion = "N";//ClsConfigurationParameters.getConfigParameterValue(service, dossier, langue, ClsConfigurationParameters.SIMULATION_CLOTURE);
					if(StringUtils.equals("N", simulaion))
						tx.commit();
					
					//mise � jour du ddmp de la session
					List l1 = service.find("from DossierPaie where idEntreprise = '" + dossier + "'");
					DossierPaie rhpdo = null;
					if ( l1 != null && l1.size() > 0 )
					{
						rhpdo = ( DossierPaie ) l1.get(0);
						if ( rhpdo.getDdmp() != null )
						{
							if(request != null) request.getSession().setAttribute(ParameterUtil.SESSION_DDMP, new ClsDate(rhpdo.getDdmp()).addMonth(1));
						}
					}
					else
					{
						if(request != null) request.getSession().setAttribute(ParameterUtil.SESSION_DDMP, new ClsDate(new Date()).addMonth(1));
					}
				}
				else
				{
					if (tx != null && tx.isActive())
						tx.rollback();
					// message = infoMessage("INF-00014");
					// this._setEvolutionTraitement(request, message, true);
					// ajout yannick
					error = update.error;
					return false;
				}
			}
			else
			{
				error = update.error;
				return false;
			}
			//
			if (res)
			{

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			this._setEvolutionTraitement(update.request, ClsTreater._getStackTrace(e), true);
			if (tx != null)
				tx.rollback();
			this.logError(ClsTreater._getStackTrace(e));
			return false;
		}
		finally
		{
			service.closeSession(oSession);
		}

		//
		return true;
	}
	
	public void refreshConnexion(Session session)
	{
		if(!refreshConnextion) return;
		String query="Update Langue set clang=clang where clang='XX'";
//		String query = "Select 'X' From Cpdos where idEntreprise = '"+update.dossier+"'";
		session.createSQLQuery(query).executeUpdate();
	}

	public boolean execute()
	{
		boolean res = true;
		String message;

		try
		{
			message = infoMessage("INF-01115");
			// ClsTraiterSalaireThread.Res_cal = infoMessage("INF-01115") + infoMessage("INF-00995");
			this._setTitreEvolutionTraitement(request, message);

			res = lancerCentralisation();

			ParameterUtil.println("---------------.....FIN TRAITEMENT CENTRALISATION .......-----------------");

			ClsResultat oResult = this._getEvolutionTraitement();
			if (oResult != null && !oResult.isErrormessage())
			{
				if (res)
				{
					res = lancerMiseAJour();
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		//
		if (!res)
		{
			message =update.error;// infoMessage("INF-00972");
			this._setTitreEvolutionTraitement(request, message, true);
		}

		return res;
	}

	public boolean execExternalFunctions(Session oSession) throws Exception
	{
		if (!execExternalFunction)
			return true;
		
		String ctabFunctions = "94";//ClsConfigurationParameters.getConfigParameterValue(service, this.getDossier(), getLangue(), ClsConfigurationParameters.TABLE_METHODES_APRES_CLOTURE);

		String queryForFunctions = "From ParamData where idEntreprise = '" + this.getDossier() + "' and ctab=" + ctabFunctions
				+ " and nume < 3 order by cacc,nume";

		List<ParamData> fonctions = this.getService().find(queryForFunctions);
		String idBean = null;
		String nomMethode = null;

		Map<String, Cls3Libelle> map = this.getFunctionsMap(fonctions);
		Object obj = null;
		Method oMethod = null;
		Object returnValue = null;
		Class returnType = null;
		try
		{
			for (Cls3Libelle libelle : map.values())
			{
				idBean = libelle.getLibelle1();
				nomMethode = libelle.getLibelle2();

				try
				{
					if (StringUtils.isNotBlank(idBean) && StringUtils.isNotBlank(nomMethode))
					{

						obj = getApplicationContext().getBean(idBean);
						if (obj != null)
						{
							oMethod = obj.getClass().getMethod(nomMethode, Session.class, String.class, String.class);
							if (oMethod != null)
							{
								returnType = oMethod.getReturnType();
								String periodecourante = this.getPeriode();
								Date moisuivant = new ClsDate(periodecourante, "yyyyMM").addMonth(1);
								//returnValue = oMethod.invoke(obj, oSession, this.getDossier(), new ClsDate(moisuivant).getDateS("yyyyMM"));
								returnValue = oMethod.invoke(obj, oSession, this.getDossier(), periodecourante);
								if ("Boolean".equalsIgnoreCase(returnType.getSimpleName()) || "boolean".equalsIgnoreCase(returnType.getSimpleName()))
								{
									if ("false".equalsIgnoreCase(returnValue.toString()))
									{
										System.out.println("Erreur lors de l'appel de la fonction externe " + nomMethode);
										String str = ClsTreater._getResultat("Erreur lors de l'appel de la fonction externe ", "INF-80-RH-185", false).getLibelle();
										throw new Exception(str+" "+ nomMethode);
									}
								}
							}
						}
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
					throw e;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}

		return true;
	}

	public Map<String, Cls3Libelle> getFunctionsMap(List<ParamData> fnoms)
	{
		Map<String, Cls3Libelle> map = new HashMap<String, Cls3Libelle>();
		for (ParamData fnom : fnoms)
		{
			if (!map.containsKey(fnom.getCacc()))
				map.put(fnom.getCacc(), new Cls3Libelle());
			if (1 == fnom.getNume())
				map.get(fnom.getCacc()).setLibelle1(fnom.getVall());

			if (2 == fnom.getNume())
				map.get(fnom.getCacc()).setLibelle2(fnom.getVall());
		}
		return map;
	}

	public class Cls3Libelle
	{
		public String libelle1;

		public String libelle2;

		public String getLibelle1()
		{
			return libelle1;
		}

		public void setLibelle1(String libelle1)
		{
			this.libelle1 = libelle1;
		}

		public String getLibelle2()
		{
			return libelle2;
		}

		public void setLibelle2(String libelle2)
		{
			this.libelle2 = libelle2;
		}
	}

	public void genrateMouvement()
	{
		boolean res = false;
		// ClsTraiterSalaireThread.Res_cal = infoMessage("INF-01122");
		if ("O".equals(genfile))
		{
//			if ("O".equals(interbank))
//			{
//				res = genrateBank(session);
//			}
//			else
//			{
				res = genrateFile(session);
//			}
		}
		else
		{
			// ClsTraiterSalaireThread.Res_cal = "Impossible avec fichier ASCII non activ�.";
			this._setEvolutionTraitement(request, 
					ClsTreater._getResultat("Impossible avec fichier ASCII non activ�.", "INF-10101", false).getLibelle()
					, true);
		}
		if (!res)
		{
			// ClsTraiterSalaireThread.Res_cal = "Impossible avec fichier ASCII non activ�.";
		}
		else
		{
			ClsResultat result = ClsTreater._getResultat("G�n�ration du fichier ASCII ...", "INF-01113", false);
			this._setTitreEvolutionTraitement(request, result.getLibelle(), false);

			this._setEvolutionTraitement(request, ClsTreater._getResultat("Traitement termine...", "INF-00009", false).getLibelle(), false);
			//if(ui!=null) ui.downloadFile();
		}
	}

	private Map<String, ParamData> listOfTable99Map = null;

	/**
	 * chargement des valeurs T99 pour ne pas aller chercher dans la base lors de l'initialisation des param�tres de calcul
	 * 
	 */
	private void buildTableT51T52T99Map()
	{

		listOfTable99Map = new HashMap<String, ParamData>();
		List l = service.find("from ParamData " + " where idEntreprise = '" + dossier + "'" + " and ctab in (91, 99)"
				+ " and cacc in('INT-BANK', 'JOURPAIE', 'PAIE-INTER', 'USER-SM', 'GEN-HIST')");
		//
		ParamData o1 = null;
		String key = "";
		for (Object object : l)
		{
			// t99 = new Rhfnom();
			o1 = (ParamData) object;
			/*
			 * t99[0] = o1.getNume(); t99[1] = o1.getVall(); t99[2] = o1.getValm(); t99[3] = o1.getValt();
			 */
			key = o1.getCtab() + o1.getCacc() + o1.getNume();
			//
			listOfTable99Map.put(key.trim(), o1);
		}
	}

	//
	public void init()
	{
		buildTableT51T52T99Map();
		// INT-BANK
		String key = "99INT-BANK1";
		ParamData fnom = listOfTable99Map.get(key);
		if (fnom != null)
		{
			interbank = "O";
		}
		if ("O".equals(interbank))
		{
			if (fnom.getVall() != null)
				centraparcompte = StringUtils.substring(fnom.getVall(), 0, 1).toUpperCase();
			else
				centraparcompte = "N";
		}
		// JOURPAIE
		key = "99JOURPAIE4";
		fnom = listOfTable99Map.get(key);
		if (fnom != null)
		{
			genfile = StringUtils.substring(fnom.getVall(), 0, 1).toUpperCase();
		}
		else
		{
			genfile = "N";
		}
		if ("O".equals(genfile))
		{
			if (fnom.getVall() != null)
			{
				key = "99PAIE-INTER2";
				fnom = listOfTable99Map.get(key);
				if (fnom != null)
					nomasciifile = fnom.getVall();
				//
				key = "99PAIE-INTER3";
				fnom = listOfTable99Map.get(key);
				if (fnom != null)
					if (!StringUtils.isBlank(fnom.getVall()))
						nomasciifile += fnom.getVall();
				//
				if (!StringUtils.isBlank(nomasciifile))
				{
					nomasciifile = nomasciifile.replace("&A2", periode.substring(2, 4));
					nomasciifile = nomasciifile.replace("&A4", periode.substring(0, 4));
					nomasciifile = nomasciifile.replace("&A", periode.substring(0, 4));
					nomasciifile = nomasciifile.replace("&M", periode.substring(4, 6));
					nomasciifile = nomasciifile.replace("&B", this.getNumerobulletin()+"");
					nomasciifile = nomasciifile.replace("&D", dossier);
				}
				if (StringUtils.isBlank(nomasciifile)){
					ParamData nFic = service.findAnyColumnFromNomenclature(this.dossier, getLangue(), "266", "PAIEGENFILEPATH", "2");
					//		ClsConfigurationParameters.getConfigParameterValue(service, this.dossier, getLangue(), ClsConfigurationParameters.PAIE_GENFILEPATH);
					if(nFic!=null) nomasciifile = nFic.getVall();
				}
			}
			//
			key = "99PAIE-INTER5";
			fnom = listOfTable99Map.get(key);
			if (fnom == null || fnom.getVall() == null)
			{
				error = parameter.errorMessage("ERR-30059", langue);
				ParameterUtil.println(error);
			}
			else
			{
				separateur = fnom.getVall().toString();
			}
		}
		//
		key = "99USER-SM2";
		fnom = listOfTable99Map.get(key);
		if (fnom == null || fnom.getVall() == null)
		{
			user_sm = "DELTA";
		}
		else
		{
			user_sm = fnom.getVall();
		}
		//
		user_sm = this.update.user;
		//
		key = "99GEN-HIST2";
		fnom = listOfTable99Map.get(key);
		if (fnom == null || fnom.getVall() == null)
		{
			genhistory = "N";
		}
		else
		{
			genhistory = StringUtils.substring(fnom.getVall(), 0, 1).toUpperCase();
		}
		//
		return;
	}

	public void printInitParameters()
	{
		ParameterUtil.println("interbank = " + interbank);
		ParameterUtil.println(" genfile= " + genfile);
		ParameterUtil.println(" nomasciifile= " + nomasciifile);
		ParameterUtil.println(" separateur= " + separateur);
		ParameterUtil.println(" user_sm= " + user_sm);
		ParameterUtil.println(" genhistory= " + genhistory);
	}

	private Object callMethode(Object obj, String nomMethode, Class classe, Object valeurParam) throws Exception
	{
		if(obj == null) return null;
		try
		{
			Method oMethod = obj.getClass().getMethod(nomMethode, classe);
			if (oMethod != null) return  oMethod.invoke(obj, valeurParam);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return null;
	}
	
	
	private boolean genrateFile(String session)
	{	
		//De mani�re g�n�rique, si en table 99, cl� CLASSEOGL, lib2 on met le nom d'une classe, alors il faut plut�t invoquer cette classe
		//qui doit se trouver dans le package com.cdi.deltarh.paie.engine
		ParamData fnom1 = service.findAnyColumnFromNomenclature(dossier,"","99","CLASSEOGL","2");
		//Rhfnom fnom1 = (Rhfnom)service.get(Rhfnom.class, new RhfnomPK(dossier,99,"CLASSEOGL",2));
		if(fnom1 != null && StringUtils.isNotBlank(fnom1.getVall()))
		{
			try
			{
				Object obj = Class.forName("com.kinart.paie.business.services.cloture."+fnom1.getVall()).newInstance();
				
				callMethode(obj, "setDossier", String.class, dossier);
				callMethode(obj, "setLangue", String.class, langue);
				callMethode(obj, "setSession", String.class, session);
				callMethode(obj, "setService", GeneriqueConnexionService.class, service);
				callMethode(obj, "setRequest", HttpServletRequest.class, request);
				callMethode(obj, "setGlobalUpdate", ClsGlobalUpdate.class, this);

				Boolean result = (Boolean)callMethode(obj, "generateFile", String.class, "test");
				
				return result;
			}
			catch (Exception e)
			{				
				e.printStackTrace();
				error = ClsTreater._getStackTrace(e);
				this._setEvolutionTraitement(request, error, true);
				return false;			
			}
		}
		
		Object obj1 =  request.getSession().getAttribute("EXXON_CLOTURE");
		if(obj1 != null)
		{
			String strObj = (String)obj1;
			if(StringUtils.equalsIgnoreCase("O", strObj)) return this.genrateFileExxon(session);
		}
		
		// GEN_FICASCII
		// PROCEDURE PR_GEN_FICASCII IS
		// out_file TEXT_IO.FILE_TYPE;
		//
		// w_cdos pasa01.cdos%TYPE;
		// w_nmat pasa01.nmat%TYPE;
		// w_nompren VARCHAR2(61);
		// w_Session VARCHAR2(30);
		//
		// w_nddd cpdos.nddd%TYPE;
		//
		// w_Texte patext.texte%TYPE;
		// Compteur patext.nlig%TYPE;
		// wint cp_int%ROWTYPE;
		// Separateur VARCHAR2(1);
		//
		// w_vall2 PAFNOM.VALL%TYPE;
		// w_vall3 PAFNOM.VALL%TYPE;
		// w_status NUMBER;
		// Pb_Forme BOOLEAN;
		// Err_Msg1 VARCHAR(70);
		// Err_Msg2 VARCHAR(70);
		//
		// w_nom_fic VARCHAR2(60);
		// w_nom_fichier VARCHAR2(100);
		//
		// CURSOR c_texte IS
		// SELECT texte
		// FROM patext
		// WHERE sess = w_Session
		// ORDER BY nlig;
		//
		// BEGIN
		//	 

		this._setTitreEvolutionTraitement(request, "INF-10100");
		
		// -- Lecture du nom de fichier et chemin
		// w_nom_fic := NVL(:b_label.nom_ascii, ' ');
		
		
		File foutput = new File(nomasciifile);

		if (!foutput.exists())
		{
			GeneriqueConnexionService._createFileFolder(nomasciifile, File.separator);
			try
			{
				foutput.createNewFile();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				error = ClsTreater._getStackTrace(e);
				this._setEvolutionTraitement(request, error, true);
				return false;
			}
	
		}
		try
		{
			Process proc0 = Runtime.getRuntime().exec("chmod 777 "+nomasciifile);
		}
		catch (Exception e)
		{
			//e.printStackTrace();
			//error = ClsTreater._getStackTrace(e);
			//this._setEvolutionTraitement(request, error, true);
		}

		BufferedWriter output = null;
		try
		{
			output = new BufferedWriter(new FileWriter(foutput));
		}
		catch (Exception ioex)
		{
			ioex.printStackTrace();
			error = ClsTreater._getStackTrace(ioex);
			this._setEvolutionTraitement(request, error, true);
			return false;
		}
		//
		// -- Lecture du caractere separateur
		// Separateur := PR_RECUP_LIBELLE(99,'PAIE-INTER',5);
		// IF NouB(Separateur)
		// THEN
		// erreur('ERR-30059');
		// /* w_status := fgen_alert_autre('ALERT_ERREUR','ERR-00001',
		// 'Libelle 5 PARAM T99 PAIE-INTER inexistant' );*/
		// RETURN;
		// ELSE
		// Separateur := SUBSTR( Separateur, 1, 1);
		// END IF;
		if (ClsObjectUtil.isNull(separateur))
		{
			error = parameter.errorMessage("ERR-30059", langue);
			this._setEvolutionTraitement(request, error, true);
		}
		//
		// -- Lecture deci devise
		// BEGIN
		// SELECT nddd
		// INTO w_nddd
		// FROM cpdos
		// WHERE cdos = :b_label.w_cdos;
		// EXCEPTION
		// WHEN OTHERS THEN
		// erreur('ERR-30060');
		// -- w_status := fgen_alert_autre('ALERT_ERREUR','ERR-00001','Pb lecture du dossier.' );
		// IF c_texte%ISOPEN THEN CLOSE c_texte; END IF;
		// TEXT_IO.FCLOSE(out_file);
		// :param.w_res_cal := fmsg('INF-01120');
		// SYNCHRONIZE;
		// RETURN;
		// END;
		int nddd = 0;
		List l = service.find("select nddd from DossierPaie where idEntreprise = '" + dossier + "'");
		if (l != null && l.size() > 0)
		{
			if (l.get(0) != null)
				nddd = new Integer(l.get(0).toString());
		}
		else
		{
			error = parameter.errorMessage("ERR-30060", langue);
			this._setEvolutionTraitement(request, error, true);
			try
			{
				output.close();
			}
			catch (IOException ioex)
			{
				ioex.printStackTrace();
			}
			return false;
		}
		// -- ========== Formatage des ecritures ===============================
		// -- Les ecritures de cp_int sont format�es puis ins�r�es dans patext
		IOGL ogl = new ClsOGL(this, service, dossier, langue,session, user_sm);
		ParamData fnom = service.findAnyColumnFromNomenclature(dossier, "","99","PAR-CENTRA","1");
		//		(Rhfnom)service.get(Rhfnom.class, new RhfnomPK(dossier,99,"PAR-CENTRA",1));
		//if(fnom != null && StringUtils.equalsIgnoreCase("SDV", fnom.getVall()))
			//ogl = new ClsOGLSDV(this,service,dossier,langue,session,user_sm);
		
		ogl.setNddd(nddd);
		ogl.generateOGL(request);
		
		//Pour SUN, pas besoin de g�n�rer le fichier r�cap
		fnom = fnom = service.findAnyColumnFromNomenclature(dossier, "","99","PAR-CENTRA","5");
		//		Rhfnom)service.get(Rhfnom.class, new RhfnomPK(dossier,99,"PAR-CENTRA",5));
		if(fnom != null && StringUtils.equals("SUN", fnom.getVall()))
		{
			
			try
			{
				output.close();
			}
			catch (IOException ioex)
			{
				ioex.printStackTrace();
				return false;
			}
			return true;		
		}
		
		
		if (!ClsObjectUtil.isNull(ogl.getErrmess1()))
		{
			error = parameter.errorMessage("ERR-30044", langue, ogl.getErrmess1());
			if (!ClsObjectUtil.isNull(ogl.getErrmess2()))
			{
				error = parameter.errorMessage("ERR-30044", langue, ogl.getErrmess2());
			}
			this._setEvolutionTraitement(request, error, true);
			try
			{
				output.close();
			}
			catch (IOException ioex)
			{
				ioex.printStackTrace();
			}
			return false;
		}
		
		l = service.find("select texte from TraitementTexte where sess = '" + session + "' order by nlig");
		try
		{
			boolean caractereRetourChariotEnFinDeLigne = false;
			String key = "99PAIE-INTER9";
			fnom = listOfTable99Map.get(key);
			if (fnom != null && StringUtils.isNotBlank(fnom.getVall()))
				caractereRetourChariotEnFinDeLigne = StringUtils.equals("O",fnom.getVall().trim());
			for (Object obj : l)
			{
				output.write(caractereRetourChariotEnFinDeLigne ? StringUtil.nvl(obj, StringUtils.EMPTY)+"\r" : StringUtil.nvl(obj, StringUtils.EMPTY) );
				output.newLine();
				
			}
			//
			output.close();
			Session cnx = service.getSession();
			cnx.createQuery("delete from Rhttext" + " where sess = '" + session + "'").executeUpdate();
			service.closeSession(cnx);

			this._setEvolutionTraitement(request, "INF-10346", false);
		}
		catch (Exception ioex)
		{
			ioex.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	private boolean genrateFileExxon(String session)
	{
		this._setTitreEvolutionTraitement(request, "INF-10100");
		
		
		try
		{
			Object obj = Class.forName("com.kinart.paie.business.services.cloture.ClsOGLExxon").newInstance();
			
			callMethode(obj, "setDossier", String.class, dossier);
			callMethode(obj, "setLangue", String.class, langue);
			callMethode(obj, "setSession", String.class, session);
			callMethode(obj, "setService", GeneriqueConnexionService.class, service);
			callMethode(obj, "setRequest", HttpServletRequest.class, request);
			callMethode(obj, "setGlobalUpdate", ClsGlobalUpdate.class, this);

			boolean result = (Boolean) callMethode(obj, "generateFile", String.class, "test");
			
			return result;
		}
		catch (Exception e)
		{
			error = ClsTreater._getStackTrace(e);
			this._setEvolutionTraitement(request, error, true);
			return false;
		}
	}


	private boolean genrateBank(String session)
	{
		// GEN_BANK
		// PROCEDURE PR_GEN_BANK IS
		// out_file TEXT_IO.FILE_TYPE;
		//
		// w_cdos pasa01.cdos%TYPE;
		// w_nmat pasa01.nmat%TYPE;
		// w_nompren VARCHAR2(61);
		//
		// w_nddd NUMBER(2);
		//
		// w_ligne VARCHAR2(700);
		//	   
		//
		// w_vall2 PAFNOM.VALL%TYPE;
		// w_vall3 PAFNOM.VALL%TYPE;
		// w_type_base PAFNOM.VALL%TYPE;
		//	   
		// w_status NUMBER;
		//	   
		// w_dco VARCHAR2(30);
		// w_dva VARCHAR2(30);
		//	   
		// w_nom_fic VARCHAR2(60);
		// w_nom_fichier VARCHAR2(100);
		//	   
		// wmpai PAMPAI%ROWTYPE;
		// CURSOR c_pampai IS
		// SELECT *
		// FROM pampai;
		//
		//
		// CURSOR c_pampai_2 IS
		// SELECT age,dev,ncp,suf,ope,uti,dco,dva,sen,lib,pie,rlet,agsa,devc,pieo,sum(NVL(mon,0)) mon,sum(NVL(mctv,0)) mctv
		// FROM pampai
		// GROUP BY age,dev,ncp,suf,ope,uti,dco,dva,sen,lib,pie,rlet,agsa,devc,pieo;
		//	   
		String complexQuery = "select age, dev, ncp, suf, ope, uti, dco, dva, sen, lib, pie, rlet, agsa, devc, pieo"
				+ ", sum(case mon when null then 0 else mon end)" + ", sum(case mctv when null then 0 else mctv end) " + " from Rhtmpai"
				+ " group by age,dev,ncp,suf,ope,uti,dco,dva,sen,lib,pie,rlet,agsa,devc,pieo";
		// wmpai2 C_PAMPAI_2%ROWTYPE;
		//	   
		//
		// BEGIN
		// -- MM modif interface bank pb base Oracle format Date + suffixe = ' '.
		// w_type_base := PR_RECUP_LIBELLE(99,'INT-BANK',5);
		// INT-BANK
		// IF NouB(w_type_base) THEN
		// w_type_base := 'INF';
		// END IF;
		// -- MM modif.
		String key = "99INT-BANK5";
		ParamData obj = listOfTable99Map.get(key);
		String w_type_base = "";
		if (obj != null && obj.getVall() != null)
		{
			w_type_base = obj.getVall().toString();
		}
		if (ClsObjectUtil.isNull(w_type_base))
			w_type_base = "INF";
		//	   
		// -- Lecture du nom de fichier et chemin
		// w_vall2 := PR_RECUP_LIBELLE(99,'PAIE-INTER',2);
		// w_vall3 := PR_RECUP_LIBELLE(99,'PAIE-INTER',3);
		//	   
		// IF NouB(w_vall2) AND NouB(w_vall3) THEN
		// erreurp('ERR-30165','PAIE-INTER','99','2' );
		// RETURN;
		// END IF;
		//
		// w_nom_fic := NVL(w_vall2,'')||NVL(w_vall3,'');
		//
		String w_nom_fic = nomasciifile;
		if (ClsObjectUtil.isNull(w_nom_fic))
		{
			error = parameter.errorMessage("ERR-30165", langue, "99", "2");
			this._setEvolutionTraitement(request, error, true);
			return false;
		}
		//	 
		// SET_APPLICATION_PROPERTY(CURSOR_STYLE,'BUSY');
		//
		//
		// /*
		// w_nom_fichier := RTRIM(WIN_API_ENVIRONMENT.Get_Temp_Directory(FALSE))
		// || '\' || w_nom_fic;
		// */
		//
		try
		{
			// out_file := TEXT_IO.FOPEN(w_nom_fic, 'w');File foutput = new File(nomasciifile);
			Writer output = null;
			File foutput = new File(w_nom_fic);
			if (!foutput.exists())
			{
				GeneriqueConnexionService._createFileFolder(w_nom_fic, File.separator);
				foutput.createNewFile();
			}
			output = new BufferedWriter(new FileWriter(foutput));
			//
			// -- G�n�ration du fichier
			// :param.w_en_cours := '';
			// :param.w_res_cal := 'GENERATION DU FICHIER ASCII';
			// SYNCHRONIZE;
			Object o = null;
			List l = service.find(complexQuery);
			Object[] rhtmpai = null;
			String w_dco = "";
			String w_dva = "";
			String w_ligne = "";
			for (Object object : l)
			{
				rhtmpai = (Object[]) object;
				// IF :b_label.centra_par_cpt = 'N' THEN
				if ("N".equals(centraparcompte))
				{
					//
					// OPEN c_pampai;
					// LOOP
					// FETCH c_pampai INTO wmpai;
					// EXIT WHEN c_pampai%NOTFOUND;
					//
					// -- Lecture deci devise
					// BEGIN
					// SELECT valm INTO w_nddd
					// FROM pafnom
					// WHERE cdos = :b_label.w_cdos
					// AND ctab = 27
					// AND cacc = wmpai.dev
					// AND nume = 1;
					// EXCEPTION
					// WHEN NO_DATA_FOUND THEN
					// erreurp('ERR-90042',wmpai.dev);
					// IF c_pampai%ISOPEN THEN
					// CLOSE c_pampai;
					// END IF;
					// TEXT_IO.FCLOSE(out_file);
					// :param.w_res_cal := fmsg('INF-01120');
					// SYNCHRONIZE;
					// RETURN;
					// END;
					// uery = "select age0, dev1, ncp2, suf3, ope4, uti5, dco6, dva7, sen8, lib9, pie10, rlet11, agsa12, devc13, pieo14" +
					// ", sum(case mont when null then 0 else mont end)15" +
					// ", sum(case mctv when null then 0 else mctv end)16 " +
					// " from Rhtmpai" +
					// " group by
					int w_nddd = 0;
					o = service.findAnyColumnFromNomenclature(this.dossier, "", "27", rhtmpai[1].toString(), "1");
					//o = service.get(Rhfnom.class, new RhfnomPK(dossier, 27, rhtmpai[1].toString(), 1));
					if (o == null)
					{
						error = parameter.errorMessage("ERR-90042", langue, rhtmpai[1]);
						this._setEvolutionTraitement(request, error, true);
						output.close();
						return false;
					}
					else
					{
						w_nddd = ((ParamData) o).getValm().intValue();
					}
					//
					// IF w_type_base = 'ORA' THEN
					// w_dco := TO_CHAR(wmpai.dco,'YYYY-MM-DD HH:MM:SS');
					// IF NouB(wmpai.suf) THEN
					// wmpai.suf := ' ';
					// END IF;
					// ELSE
					// w_dco := TO_CHAR(wmpai.dco,'DD/MM/YYYY');
					// END IF;
					if ("ORA".equals(w_type_base) && rhtmpai[6] != null)
					{
						w_dco = new ClsDate((Date) rhtmpai[6], "yyyy-MM-dd hh:mm:ss").getDateS();
						if (rhtmpai[3] != null)
							rhtmpai[3] = "";
					}
					else
					{
						w_dco = new ClsDate((Date) rhtmpai[6], "yyyy/MM/dd").getDateS();
					}
					//	      
					// w_ligne := wmpai.age||'|'||
					// wmpai.dev||'|'||
					// ''||'|'||
					// wmpai.ncp||'|'||
					// wmpai.suf||'|'||
					// wmpai.ope||'|'||
					// ''||'|'||
					// ''||'|'||
					// wmpai.uti||'|'||
					// ''||'|'||
					// ''||'|'||
					// w_dco||'|'||
					// ''||'|'||
					// ''||'|'||
					// LTRIM(PR_FORMAT(wmpai.mon,w_nddd,19,FALSE))||'|'||
					// wmpai.sen||'|'||
					// wmpai.lib||'|'||
					// ''||'|'||
					// wmpai.pie||'|'||
					// wmpai.rlet||'|'||
					// ''||'|'||
					// ''||'|'||
					// ''||'|'||
					// ''||'|'||
					// ''||'|'||
					// ''||'|'||
					// ''||'|'||
					// ''||'|'||
					// ''||'|'||
					// ''||'|'||
					// ''||'|'||
					// ''||'|'||
					// ''||'|'||
					// ''||'|'||
					// ''||'|'||
					// ''||'|'||
					// ''||'|'||
					// ''||'|'||
					// wmpai.agsa||'|'||
					// ''||'|'||
					// ''||'|'||
					// wmpai.devc||'|'||
					// LTRIM(PR_FORMAT(wmpai.mctv,w_nddd,19,FALSE))||'|'||
					// wmpai.pieo||'|'||
					// ''||'|'||
					// ''||'|';

					//	      
					w_ligne = rhtmpai[0] + "|" + rhtmpai[1] + "|" + "" + "|" + rhtmpai[2] + "|" + rhtmpai[3] + "|" + rhtmpai[4] + "|" + "" + "|" + "" + "|"
							+ rhtmpai[5] + "|" + "" + "|" + "" + "|" + w_dco + "|" + "" + "|" + "" + "|"
							+ ClsStringUtil.formatNumber((Number) rhtmpai[15], w_nddd, 19, false, ',') + "|" + rhtmpai[8] + "|" + rhtmpai[9] + "|" + "" + "|"
							+ rhtmpai[10] + "|" + rhtmpai[11] + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|"
							+ "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + rhtmpai[12] + "|"
							+ "" + "|" + "" + "|" + rhtmpai[13] + "|" + ClsStringUtil.formatNumber((Number) rhtmpai[16], w_nddd, 19, false, ',') + "|"
							+ rhtmpai[14] + "|" + "" + "|" + "" + "|";
					//
					//
					// TEXT_IO.PUT_LINE(out_file,w_ligne);
					output.write(w_ligne);
					// END LOOP;
					// CLOSE c_pampai;
					//
				}
				// ELSE
				else
				{
					//
					// OPEN c_pampai_2;
					// LOOP
					// FETCH c_pampai_2 INTO wmpai2;
					// EXIT WHEN c_pampai_2%NOTFOUND;
					//
					// -- Lecture deci devise
					// BEGIN
					// SELECT valm INTO w_nddd
					// FROM pafnom
					// WHERE cdos = :b_label.w_cdos
					// AND ctab = 27
					// AND cacc = wmpai2.dev
					// AND nume = 1;
					// EXCEPTION
					// WHEN NO_DATA_FOUND THEN
					// erreurp('ERR-90042',wmpai2.dev);
					// IF c_pampai_2%ISOPEN THEN
					// CLOSE c_pampai_2;
					// END IF;
					// TEXT_IO.FCLOSE(out_file);
					// :param.w_res_cal := fmsg('INF-01120');
					// SYNCHRONIZE;
					// RETURN;
					// END;
					//
					int w_nddd = 0;

					o = service.findAnyColumnFromNomenclature(dossier, "","27", rhtmpai[1].toString(), "1");
					//		get(Rhfnom.class, new RhfnomPK(dossier, 27, rhtmpai[1].toString(), 1));
					if (o == null)
					{
						error = parameter.errorMessage("ERR-90042", langue, rhtmpai[1]);
						output.close();
						return false;
					}
					else
					{
						w_nddd = ((ParamData) o).getValm().intValue();
					}
					// IF w_type_base = 'ORA' THEN
					// w_dco := TO_CHAR(wmpai2.dco,'YYYY-MM-DD HH:MM:SS');
					// w_dva := TO_CHAR(wmpai2.dva,'YYYY-MM-DD HH:MM:SS');
					// IF NouB(wmpai2.suf) THEN
					// wmpai2.suf := ' ';
					// END IF;
					// ELSE
					// w_dco := TO_CHAR(wmpai2.dco,'DD/MM/YYYY');
					// w_dva := TO_CHAR(wmpai2.dva,'DD/MM/YYYY');
					// END IF;
					//	      
					if ("ORA".equals(w_type_base) && rhtmpai[6] != null)
					{
						w_dco = new ClsDate((Date) rhtmpai[6], "yyyy-MM-dd hh:mm:ss").getDateS();
						w_dva = new ClsDate((Date) rhtmpai[7], "yyyy-MM-dd hh:mm:ss").getDateS();
						if (rhtmpai[3] != null)
							rhtmpai[3] = "";
					}
					else
					{
						w_dco = new ClsDate((Date) rhtmpai[6], "yyyy/MM/dd").getDateS();
						w_dva = new ClsDate((Date) rhtmpai[7], "yyyy/MM/dd").getDateS();
					}
					// w_ligne := wmpai2.age||'|'||
					// wmpai2.dev||'|'||
					// ''||'|'||
					// wmpai2.ncp||'|'||
					// wmpai2.suf||'|'||
					// wmpai2.ope||'|'||
					// ''||'|'||
					// ''||'|'||
					// wmpai2.uti||'|'||
					// ''||'|'||
					// ''||'|'||
					// w_dco||'|'||
					// ''||'|'||
					// w_dva||'|'||
					// LTRIM(PR_FORMAT(wmpai2.mon,w_nddd,19,FALSE))||'|'||
					// wmpai2.sen||'|'||
					// wmpai2.lib||'|'||
					// ''||'|'||
					// wmpai2.pie||'|'||
					// wmpai2.rlet||'|'||
					// ''||'|'||
					// ''||'|'||
					// ''||'|'||
					// ''||'|'||
					// ''||'|'||
					// ''||'|'||
					// ''||'|'||
					// ''||'|'||
					// ''||'|'||
					// ''||'|'||
					// ''||'|'||
					// ''||'|'||
					// ''||'|'||
					// ''||'|'||
					// ''||'|'||
					// ''||'|'||
					// ''||'|'||
					// ''||'|'||
					// wmpai2.agsa||'|'||
					// ''||'|'||
					// ''||'|'||
					// wmpai2.devc||'|'||
					// LTRIM(PR_FORMAT(wmpai2.mctv,w_nddd,19,FALSE))||'|'||
					// wmpai2.pieo||'|'||
					// ''||'|'||
					// ''||'|';

					// uery = "select age0, dev1, ncp2, suf3, ope4, uti5, dco6, dva7, sen8, lib9, pie10, rlet11, agsa12, devc13, pieo14" +
					// ", sum(case mont when null then 0 else mont end)15" +
					// ", sum(case mctv when null then 0 else mctv end)16 " +
					// " from Rhtmpai" +
					// " group by
					w_ligne = rhtmpai[0] + "|" + rhtmpai[1] + "|" + "" + "|" + rhtmpai[2] + "|" + rhtmpai[3] + "|" + rhtmpai[4] + "|" + "" + "|" + "" + "|"
							+ rhtmpai[5] + "|" + "" + "|" + "" + "|" + w_dco + "|" + "" + "|" + w_dva + "|"
							+ ClsStringUtil.formatNumber((Number) rhtmpai[15], w_nddd, 19, false, ',') + "|" + rhtmpai[8] + "|" + rhtmpai[9] + "|" + "" + "|"
							+ rhtmpai[10] + "|" + rhtmpai[11] + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|"
							+ "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + rhtmpai[12] + "|"
							+ "" + "|" + "" + "|" + rhtmpai[13] + "|" + ClsStringUtil.formatNumber((Number) rhtmpai[16], w_nddd, 19, false, ',') + "|"
							+ rhtmpai[10] + "|" + "" + "|" + "" + "|";
					//
					output.write(w_ligne);
					//
					// TEXT_IO.PUT_LINE(out_file,w_ligne);
					// END LOOP;
					// CLOSE c_pampai_2;
					//
					//	   	
					// END IF;
				}

			}
			//	   
			//
			output.close();
			//
			//
			// TEXT_IO.FCLOSE(out_file);
			// SET_APPLICATION_PROPERTY(CURSOR_STYLE,'DEFAULT');
		}
		catch (IOException ioex)
		{
			ioex.printStackTrace();
			return false;
		}
		//
		// :param.w_res_cal := fmsg('INF-10346');
		// SYNCHRONIZE;
		//
		// EXCEPTION
		// WHEN OTHERS THEN
		// IF c_pampai%ISOPEN THEN
		// CLOSE c_pampai;
		// END IF;
		// TEXT_IO.FCLOSE(out_file);
		//
		// IF NOT NOUZ(SQLCODE) THEN
		// :param.w_erreur := 'Erreur : '||substr(SQLERRM,1,90);
		// ELSE
		// :param.w_erreur := 'Erreur no : '||to_char(error_code)||' / '||error_text||' !';
		// END IF;
		// SYNCHRONIZE;
		// raise;
		return true;
		// END;
	}

	private String infoMessage(String errorCode)
	{
		List result = service.find("From Message Where cdmes='"+errorCode+"' And clang='"+langue+"'");
		Message message = null;//(Evmsg) service.get(Evmsg.class, new EvmsgPK(errorCode, langue));
		if(result!=null && result.size()>0) message = (Message)result.get(0);
		String libelleMessage = "";
		if (message != null)
		{
			// n := instr(w_lib_mess,'%1');
			libelleMessage = message.getLbmes();
		}
		//
		return libelleMessage;
	}

	public String getDossier()
	{
		return dossier;
	}

	public void setDossier(String dossier)
	{
		this.dossier = dossier;
	}

	public String getPeriode()
	{
		return periode;
	}

	public void setPeriode(String periode)
	{
		this.periode = periode;
	}

	public String getLangue()
	{
		return langue;
	}

	public void setLangue(String langue)
	{
		this.langue = langue;
	}

	public int getNumerobulletin()
	{
		return numerobulletin;
	}

	public void setNumerobulletin(int numerobulletin)
	{
		this.numerobulletin = numerobulletin;
	}

	public GeneriqueConnexionService getService()
	{
		return service;
	}

	public void setService(GeneriqueConnexionService service)
	{
		this.service = service;
	}

	public ClsParameterOfPay getParameter()
	{
		return parameter;
	}

	public void setParameter(ClsParameterOfPay parameter)
	{
		this.parameter = parameter;
	}

	public int getW_nddd()
	{
		return w_nddd;
	}

	public void setW_nddd(int w_nddd)
	{
		this.w_nddd = w_nddd;
	}

	public Date getDateComptable()
	{
		return dateComptable;
	}

	public void setDateComptable(Date dateComptable)
	{
		this.dateComptable = dateComptable;
	}

	public String getSession()
	{
		return session;
	}

	public void setSession(String session)
	{
		this.session = session;
	}

	public ClsUpdateBulletin getUpdate()
	{
		return update;
	}

	public void setUpdate(ClsUpdateBulletin update)
	{
		this.update = update;
	}

	public String getInterbank()
	{
		return interbank;
	}

	public void setInterbank(String interbank)
	{
		this.interbank = interbank;
	}

	public String getCentraparcompte()
	{
		return centraparcompte;
	}

	public void setCentraparcompte(String centraparcompte)
	{
		this.centraparcompte = centraparcompte;
	}

	public String getGenfile()
	{
		return genfile;
	}

	public void setGenfile(String genfile)
	{
		this.genfile = genfile;
	}

	public String getNomasciifile()
	{
		return nomasciifile;
	}

	public void setNomasciifile(String nomasciifile)
	{
		this.nomasciifile = nomasciifile;
	}

	public String getSeparateur()
	{
		return separateur;
	}

	public void setSeparateur(String separateur)
	{
		this.separateur = separateur;
	}

	public String getGenhistory()
	{
		return genhistory;
	}

	public void setGenhistory(String genhistory)
	{
		this.genhistory = genhistory;
	}

	public String getError()
	{
		return error;
	}

	public void setError(String error)
	{
		this.error = error;
	}

	public Map<String, ParamData> getListOfTable99Map()
	{
		return listOfTable99Map;
	}

	public void setListOfTable99Map(Map<String, ParamData> listOfTable99Map)
	{
		this.listOfTable99Map = listOfTable99Map;
	}

	public ApplicationContext getApplicationContext()
	{
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext)
	{
		this.applicationContext = applicationContext;
	}

	public void setExecExternalFunction(boolean execExternalFunction)
	{
		this.execExternalFunction = execExternalFunction;
	}

}
