package com.kinart.paie.business.services.cloture;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.kinart.paie.business.model.ElementSalaire;
import com.kinart.paie.business.model.Message;
import com.kinart.paie.business.model.ParamData;
import com.kinart.paie.business.model.TraitementTexte;
import com.kinart.paie.business.services.calcul.ClsParameterOfPay;
import com.kinart.paie.business.services.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.context.ApplicationContext;

public class ClsGlobalUpdateThread
{
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

	private ClsUpdateBulletinThread update = null;

	//
	private String interbank = "";

	private String centraparcompte = "";

	private String genfile = "";

	private String nomasciifile = "";

	private String separateur = "";

	private String genhistory = "";

	//
	private String error = "";

	// ////////////////PAR YANNICK POUR AFFICHAGE DU SUIVI EVOLUTION TRAITEMENT
	private HttpServletRequest request;
	
	private static ClsResultat initializeError = null;
	
	public synchronized String getUser_sm()
	{
		return user_sm;
	}

	public synchronized void setUser_sm(String user_sm)
	{
		this.user_sm = user_sm;
	}

	//
	public ClsGlobalUpdateThread(HttpServletRequest request, GeneriqueConnexionService service, ClsCentralisationService centraservice, ClsParameterOfPay parameter, String dossier, String periode, String langue, String userid, int numerobulletin, Date dateComptable, int w_nddd, String session, ClsUpdateBulletinThread update)
	{
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
	}

	/** ********DEBUT DES FONCTIONS DE CENTRALISATION YANNICK************************************ */
	//pour pouvoir concatener tous les messages d'erreurs issus de l'initialisation avant de les afficher
	public static void _setInitializeError(HttpServletRequest request, String message, boolean isError)
	{
		ClsResultat oResult = ClsTreater._getResultat(message, message, isError);
		initializeError = ClsTreater._concat(initializeError, oResult,  ParameterUtil.CHAINE_ALLER_A_LA_LIGNE);
	}
	
	public static ClsResultat _printInitializeError(HttpServletRequest request)
	{
		request.getSession().setAttribute(ClsSessionObjectName.SESSION_O_CENTRALISATION_MESSAGE_SUIVI, initializeError);
		return initializeError;
	}

	
	public static void _setEvolutionTraitement(HttpServletRequest request, String message, boolean isError)
	{
		ClsResultat oResult = ClsTreater._getResultat(message, message, isError);
		request.getSession().setAttribute(ClsSessionObjectName.SESSION_O_CENTRALISATION_MESSAGE_SUIVI, oResult);
		//ParameterUtil.println("-------------Message = "+message);
	}

	public static void _setEvolutionTraitement(HttpServletRequest request, String message, boolean isError, int nbrCourant, int nbrTotal)
	{
		ClsResultat oResult = ClsTreater._getResultat(message, message, isError);
		request.getSession().setAttribute(ClsSessionObjectName.SESSION_O_CENTRALISATION_MESSAGE_SUIVI, oResult);

		request.getSession().setAttribute(ClsSessionObjectName.SESSION_O_CENTRALISATION_SALARIES_TRAITES_SUIVI, new ClsNomenclature(String.valueOf(nbrCourant), String.valueOf(nbrTotal)));

	}

	public static void _setTitreEvolutionTraitement(HttpServletRequest request, String message)
	{
		ClsGlobalUpdateThread._setTitreEvolutionTraitement(request, message, false);
	}
	
	public static void _setTitreEvolutionTraitement(HttpServletRequest request, String message, boolean isErrorMessage)
	{
		ClsResultat oResult = ClsTreater._getResultat(message, message, isErrorMessage);
		request.getSession().setAttribute(ClsSessionObjectName.SESSION_O_CENTRALISATION_TITRE_SUIVI, oResult);

	}

	public ClsResultat _getEvolutionTraitement()
	{
		Object obj = request.getSession().getAttribute(ClsSessionObjectName.SESSION_O_CENTRALISATION_MESSAGE_SUIVI);
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

	public boolean lancerCentralisation()
	{
		boolean res = false;

		ClsSalaryProcessCentralisation oSalaryCentraToProcess = new ClsSalaryProcessCentralisation(request, service,null, centraservice, null, periode, numerobulletin, user_sm, langue, w_nddd, new ClsDate(dateComptable), dossier);

		// oSalaryCentraToProcess.setService(service);

		try
		{
			String message = infoMessage("INF-01115");
			ClsGlobalUpdateThread._setTitreEvolutionTraitement(request, message);
			
			request.getSession().setAttribute(ClsSessionObjectName.SESSION_O_CENTRALISATION_NATURE_TRAITEMENT, "GMC");

			oSalaryCentraToProcess.lcentra();
			
			ClsResultat oResult = this._getEvolutionTraitement();
			if (oResult != null && oResult.isErrormessage())
				return false;

			if ("O".equals(genfile))
			{
				res = true;
				if ("O".equals(interbank))
				{
					res = genrateBank(session);
				}
				else
				{
					res = genrateFile(session);
				}
				ParameterUtil.println("...res :" + res);
			}
			ClsGlobalUpdateThread._setTitreEvolutionTraitement(request, "");
			ClsGlobalUpdateThread._setEvolutionTraitement(request, "INF-10346", false);
			return res;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/** ********FIN DES FONCTIONS DE CENTRALISATION************************************ */
	
	
	public boolean lancerMiseAJour()
	{
		boolean res = true;
		String message;
		ClsResultat oResult;
		
		Session oSession = null;
		Transaction tx =null;

		try
		{
			oSession = service.getSession();
			tx = oSession.beginTransaction();

			if ("O".equals(genhistory))
			{
				request.getSession().setAttribute(ClsSessionObjectName.SESSION_O_CENTRALISATION_NATURE_TRAITEMENT, null);
				message = infoMessage("INF-01117");
				ClsGlobalUpdateThread._setTitreEvolutionTraitement(request, message);
				ParameterUtil.println("...genhistory");
				res = new ClsDataHistory().processHistory(request, service,null, dossier, periode, numerobulletin, user_sm);
			}
			if (res)
			{
				message = infoMessage("INF-01119");
//				ClsTraiterSalaireThread.Res_cal = message + infoMessage("INF-00995");
				ClsGlobalUpdateThread._setTitreEvolutionTraitement(request, message);

				boolean canExec = update.initVariables();
				if(!canExec)
					return false;
				
				canExec = update.verificationParametrage();
				if(!canExec)
					return false;
				
				if (canExec)
				{
					request.getSession().setAttribute(ClsSessionObjectName.SESSION_O_CENTRALISATION_NATURE_TRAITEMENT, "MAJ");
					update.execute(oSession);// il y une attente � ce niveau
				}
				
				request.getSession().setAttribute(ClsSessionObjectName.SESSION_O_CENTRALISATION_NATURE_TRAITEMENT, null);
				//
				res = update.getFinalResult();

				oResult = this._getEvolutionTraitement();
				if (oResult != null && oResult.isErrormessage())
					res = false;
				else
					res = true;
				
				if(! res) return false;

				ParameterUtil.println("...update res :" + res);
				if (res)
				{
					if (!update.updateDossierDePaie(oSession))
					{
						error = parameter.errorMessage("ERR-90130", langue);
						ParameterUtil.println(error);
						ClsGlobalUpdateThread._setEvolutionTraitement(request, error, true);
						return false;
					}
					
					try
					{
						//Execution des m�thodes stock�s en nomenclatures
						execExternalFunctions(oSession);
					}
					catch (Exception e)
					{
						error = ClsTreater._getStackTrace(e);
						ParameterUtil.println(error);
						ClsGlobalUpdateThread._setEvolutionTraitement(request, error, true);
						return false;
					}
					
					//
//					ClsTraiterSalaireThread.Res_cal = "";
//					ClsTraiterSalaireThread.encours = infoMessage("INF-00440") + "...";
					ClsGlobalUpdateThread._setTitreEvolutionTraitement(request, "");
					
					///--------------commentaire temporaire pour tests, => pas de maj au niveau de la bd
					tx.commit();
				}
				else
				{
					if (tx != null && tx.isActive())
						tx.rollback();
					message = infoMessage("INF-00014");
//					ClsTraiterSalaireThread.Res_cal = message;
					ClsGlobalUpdateThread._setEvolutionTraitement(request, message, true);
					// ajout yannick
					error = update.error;
				}
			}
			else
			{
				error = update.error;
			}
			//
			if (res)
			{

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{	
			service.closeSession(oSession);
		}

		//
		return true;
	}
	public boolean execute()
	{
		boolean res = true;
		String message;

		try
		{
			message = infoMessage("INF-01115");
//			ClsTraiterSalaireThread.Res_cal = infoMessage("INF-01115") + infoMessage("INF-00995");
			ClsGlobalUpdateThread._setTitreEvolutionTraitement(request, message);

			res = lancerCentralisation();
			

			ParameterUtil.println("---------------.....FIN TRAITEMENT CENTRALISATION .......-----------------");

			ClsResultat oResult = this._getEvolutionTraitement();
			if (oResult != null && ! oResult.isErrormessage())
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
		if(!res)
		{
			message = infoMessage("INF-00972");
			ClsGlobalUpdateThread._setTitreEvolutionTraitement(request, message, true);
		}
		
		return res;
	}
	
	public boolean execExternalFunctions(Session oSession) throws Exception
	{
		if(! execExternalFunction)
			return true;
		
		String queryForFunctions = "From ParamData where idEntreprise='"+this.getDossier()+"' and ctab="+94+" and nume < 3 order by cacc,nume";

		List<ParamData> fonctions = this.getService().find(queryForFunctions);
		String idBean = null;
		String nomMethode = null;

		Map<String, Cls3Libelle> map = this.getFunctionsMap(fonctions);
		Object obj = null;
		Method oMethod = null;
		Object returnValue = null;
		Class returnType = null;
		for (Cls3Libelle libelle : map.values())
		{
			idBean = libelle.getLibelle1();
			nomMethode = libelle.getLibelle2();

			if (StringUtils.isNotBlank(idBean) && StringUtils.isNotBlank(nomMethode))
			{
				
				obj = getApplicationContext().getBean(idBean);
				if (obj != null)
				{
					oMethod = obj.getClass().getMethod(nomMethode,Session.class, String.class, String.class);
					if (oMethod != null)
					{
						returnType = oMethod.getReturnType();
						String periodecourante = this.getPeriode();
						Date moisuivant = new ClsDate(periodecourante, "yyyyMM").addMonth(1);
						returnValue = oMethod.invoke(obj,oSession, this.getDossier(), new ClsDate(moisuivant).getDateS("yyyyMM"));
						if ("Boolean".equalsIgnoreCase(returnType.getSimpleName()) || "boolean".equalsIgnoreCase(returnType.getSimpleName()))
						{
							if ("false".equalsIgnoreCase(returnValue.toString()))
								throw new Exception("Erreur lors de l'appel de la fonction externe "+nomMethode);
						}
					}
				}
			}
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
//		ClsTraiterSalaireThread.Res_cal = infoMessage("INF-01122");
		if ("O".equals(genfile))
		{
			if ("O".equals(interbank))
			{
				res = genrateBank(session);
			}
			else
			{
				res = genrateFile(session);
			}
		}
		else
		{
//			ClsTraiterSalaireThread.Res_cal = "Impossible avec fichier ASCII non activ�.";
			ClsGlobalUpdateThread._setEvolutionTraitement(request, "Impossible avec fichier ASCII non activ�.", true);
		}
		if (!res)
		{
//			ClsTraiterSalaireThread.Res_cal = "Impossible avec fichier ASCII non activ�.";
		}
		else
		{
			ClsResultat result = ClsTreater._getResultat("G�n�ration termin�e avec succ�s.", "ERR-30025", false);
			ClsGlobalUpdateThread._setTitreEvolutionTraitement(request, result.getLibelle(), false);
			
			ClsGlobalUpdateThread._setEvolutionTraitement(request, "", false);
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
		List l = service.find("from ParamData " + " where idEntreprise='" + dossier + "'" + " and ctab in (91, 99)" + " and cacc in('INT-BANK', 'JOURPAIE', 'PAIE-INTER', 'USER-SM', 'GEN-HIST')");
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
					nomasciifile = nomasciifile.replace("&A", periode.substring(0, 4));
					nomasciifile = nomasciifile.replace("&M", periode.substring(4, 6));
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

	private boolean genrateFile(String session)
	{
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
		
		

		ClsGlobalUpdateThread._setTitreEvolutionTraitement(request, "INF-10100");

		File foutput = new File(nomasciifile);
		
		if (!foutput.exists())
		{
			GeneriqueConnexionService._createFileFolder(nomasciifile, "\\");
			try
			{
				foutput.createNewFile();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Writer output = null;
		try
		{
			output = new BufferedWriter(new FileWriter(foutput));
		}
		catch (IOException ioex)
		{
			ioex.printStackTrace();
			error = ClsTreater._getStackTrace(ioex);
			ClsGlobalUpdateThread._setEvolutionTraitement(request, error, true);
			return false;
		}
		// -- Lecture du nom de fichier et chemin
		// w_nom_fic := NVL(:b_label.nom_ascii, ' ');
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
			ClsGlobalUpdateThread._setEvolutionTraitement(request, error, true);
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
		List l = service.find("select nddd from Cpdo where idEntreprise='" + dossier + "'");
		if (l != null && l.size() > 0)
		{
			if (l.get(0) != null)
				nddd = new Integer(l.get(0).toString());
		}
		else
		{
			error = parameter.errorMessage("ERR-30060", langue);
			ClsGlobalUpdateThread._setEvolutionTraitement(request, error, true);
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
		//
		// SET_APPLICATION_PROPERTY(CURSOR_STYLE,'BUSY');
		//
		// -- ========== Formatage des ecritures ===============================
		// -- Les ecritures de cp_int sont format�es puis ins�r�es dans patext
		// :param.w_en_cours := '';
		// :param.w_res_cal := fmsg('INF-01121');
		// SYNCHRONIZE;
		// w_Session := DBMS_SESSION.UNIQUE_SESSION_ID;
		// Pb_Forme := FALSE;
		// Err_Msg1 := ' ';
		// Err_Msg2 := ' ';
		// PA_OGL.Gen_OGL(w_Session, :b_label.w_cdos, Pb_Forme, Err_Msg1, Err_Msg2);
		// IF NOT NouB(Err_Msg1)
		// THEN
		// erreurp('ERR-30044',Err_Msg1);
		// --w_status := fgen_alert_autre('ALERT_ERREUR','ERR-00001', Err_Msg1 );
		// IF NOT NouB(Err_Msg2)
		// THEN
		// erreurp('ERR-30044',Err_Msg2);
		// --w_status := fgen_alert_autre('ALERT_ERREUR','ERR-00001', Err_Msg2 );
		// END IF;
		// RETURN;
		// END IF;
		ClsGlobalUpdate global = new ClsGlobalUpdate(request, service, centraservice, parameter, dossier, periode, langue, user_sm, 9, null, 2, "123", null);
		ClsOGL ogl = new ClsOGL(global, service, dossier,langue, session, user_sm);
		ogl.generateOGL(request);
		if (!ClsObjectUtil.isNull(ogl.getErrmess1()))
		{
			error = parameter.errorMessage("ERR-30044", langue, ogl.getErrmess1());
			if (!ClsObjectUtil.isNull(ogl.getErrmess2()))
			{
				error = parameter.errorMessage("ERR-30044", langue, ogl.getErrmess2());
			}
			ClsGlobalUpdateThread._setEvolutionTraitement(request, error, true);
			return false;
		}
		//
		// -- ========== Generation du fichier ASCII ===============================
		// -- Ouverture du fichier
		// BEGIN
		// out_file := TEXT_IO.FOPEN(w_nom_fic, 'w');
		// EXCEPTION
		// WHEN OTHERS THEN
		// erreur('ERR-30061');
		// /*w_status := fgen_alert_autre('ALERT_ERREUR','ERR-00001',
		// 'V�rifiez le chemin du fichier ASCII (PAIE-INTER, Lib2 & Lib3).' );*/
		// RETURN;
		// END;
		//
		// -- Generation du fichier
		// :param.w_en_cours := '';
		// :param.w_res_cal := fmsg('INF-01122');
		// SYNCHRONIZE;
		//
		// Compteur := 0;
		// IF C_texte%ISOPEN THEN CLOSE C_texte; END IF;
		// OPEN C_texte;
		// LOOP
		// FETCH C_texte INTO W_Texte;
		// EXIT WHEN C_texte%NOTFOUND;
		//
		// IF NOT NouB(W_Texte)
		// THEN
		// Compteur := Compteur + 1;
		// TEXT_IO.PUT_LINE(out_file,W_Texte);
		// END IF;
		// END LOOP;
		l = service.find("select texte from Rhttext where sess = '" + session + "'");
		try
		{
			for (Object obj : l)
			{
				output.write(obj.toString());
				output.write("\n");
			}
			//
			output.close();
			//
			// -- Fermeture du fichier ASCII
			// BEGIN
			// TEXT_IO.FCLOSE(out_file);
			// EXCEPTION
			// WHEN OTHERS THEN NULL;
			// END;
			//
			// -- Suppression du texte
			// BEGIN
			// DELETE FROM patext WHERE sess = w_Session;
			// EXCEPTION
			// WHEN OTHERS THEN NULL;
			// END;

			Session cnx = service.getSession();
			cnx.createQuery("delete from TraitementTexte" + " where sess = '" + session + "'").executeUpdate();
			service.closeSession(cnx);

			ClsGlobalUpdateThread._setEvolutionTraitement(request, "INF-10346", false);
		}
		catch (Exception ioex)
		{
			ioex.printStackTrace();
		}
		//
		// -- Le curseur reprend sa forme par defaut
		// SET_APPLICATION_PROPERTY(CURSOR_STYLE,'DEFAULT');
		//
		// Compteur := NVL(Compteur, 0);
		// :param.w_res_cal := fmsg('INF-00440')||' : '
		// || LTRIM(RTRIM(TO_CHAR(Compteur, '999990')))
		// || fmsg('INF-01125');
		// SYNCHRONIZE;
		//
		// EXCEPTION
		// WHEN OTHERS THEN
		// IF c_texte%ISOPEN THEN CLOSE c_texte; END IF;
		// TEXT_IO.FCLOSE(out_file);
		//
		// SET_APPLICATION_PROPERTY(CURSOR_STYLE,'DEFAULT');
		//
		// BEGIN
		// DELETE FROM patext WHERE sess = w_Session;
		// EXCEPTION
		// WHEN OTHERS THEN NULL;
		// END;
		//
		// IF NOT NOUZ(SQLCODE) THEN
		// :param.w_erreur := 'Erreur : '||substr(SQLERRM,1,90);
		// ELSE
		// :param.w_erreur := 'Erreur no : '||to_char(error_code)||' / '||error_text||' !';
		// END IF;
		// SYNCHRONIZE;
		// raise;
		// END;
		//
		return true;
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
		String complexQuery = "select age, dev, ncp, suf, ope, uti, dco, dva, sen, lib, pie, rlet, agsa, devc, pieo" + ", sum(case mon when null then 0 else mon end)" + ", sum(case mctv when null then 0 else mctv end) " + " from Rhtmpai"
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
			ClsGlobalUpdateThread._setEvolutionTraitement(request, error, true);
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
				GeneriqueConnexionService._createFileFolder(w_nom_fic, "\\");
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
					ParamData nome = service.findAnyColumnFromNomenclature(dossier, "","27", rhtmpai[1].toString(), "1");
					//o = service.get(Rhfnom.class, new RhfnomPK(dossier, 27, rhtmpai[1].toString(), 1));
					if (nome == null)
					{
						error = parameter.errorMessage("ERR-90042", langue, rhtmpai[1]);
						ClsGlobalUpdateThread._setEvolutionTraitement(request, error, true);
						output.close();
						return false;
					}
					else
					{
						w_nddd = nome.getValm().intValue();
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
					w_ligne = rhtmpai[0] + "|" + rhtmpai[1] + "|" + "" + "|" + rhtmpai[2] + "|" + rhtmpai[3] + "|" + rhtmpai[4] + "|" + "" + "|" + "" + "|" + rhtmpai[5] + "|" + "" + "|" + "" + "|" + w_dco + "|" + "" + "|" + "" + "|"
							+ ClsStringUtil.formatNumber((Number) rhtmpai[15], w_nddd, 19, false, ',') + "|" + rhtmpai[8] + "|" + rhtmpai[9] + "|" + "" + "|" + rhtmpai[10] + "|" + rhtmpai[11] + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|"
							+ "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + rhtmpai[12] + "|" + "" + "|" + "" + "|" + rhtmpai[13] + "|" + ClsStringUtil.formatNumber((Number) rhtmpai[16], w_nddd, 19, false, ',') + "|" + rhtmpai[14] + "|" + ""
							+ "|" + "" + "|";
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
					ParamData nome = service.findAnyColumnFromNomenclature(dossier, "","27", rhtmpai[1].toString(), "1");
					//o = service.get(Rhfnom.class, new RhfnomPK(dossier, 27, rhtmpai[1].toString(), 1));
					if (nome == null)
					{
						error = parameter.errorMessage("ERR-90042", langue, rhtmpai[1]);
						output.close();
						return false;
					}
					else
					{
						w_nddd = nome.getValm().intValue();
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
					w_ligne = rhtmpai[0] + "|" + rhtmpai[1] + "|" + "" + "|" + rhtmpai[2] + "|" + rhtmpai[3] + "|" + rhtmpai[4] + "|" + "" + "|" + "" + "|" + rhtmpai[5] + "|" + "" + "|" + "" + "|" + w_dco + "|" + "" + "|" + w_dva + "|"
							+ ClsStringUtil.formatNumber((Number) rhtmpai[15], w_nddd, 19, false, ',') + "|" + rhtmpai[8] + "|" + rhtmpai[9] + "|" + "" + "|" + rhtmpai[10] + "|" + rhtmpai[11] + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|"
							+ "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + rhtmpai[12] + "|" + "" + "|" + "" + "|" + rhtmpai[13] + "|" + ClsStringUtil.formatNumber((Number) rhtmpai[16], w_nddd, 19, false, ',') + "|" + rhtmpai[10] + "|" + ""
							+ "|" + "" + "|";
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
		List<Message> result = service.find("FROM Message cdmes='"+errorCode+"' And clang='"+langue+"'");
		Message message = null;//(Evmsg) service.get(Evmsg.class, new EvmsgPK(errorCode, langue));
		if(result!=null && !result.isEmpty()) message = result.get(0);
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

	public ClsUpdateBulletinThread getUpdate()
	{
		return update;
	}

	public void setUpdate(ClsUpdateBulletinThread update)
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
