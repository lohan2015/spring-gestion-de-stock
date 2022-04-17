package com.kinart.paie.business.services.calcul;

import java.io.Serializable;

public class ClsLanceurCalcul implements Serializable
{
	public int nbrErreur = 0;

	public int nbrTraite = 0;

	public int nbrATraiter = 0;

	// ui � partir duquel on lance le traitement
	public Object ui;

	// dfsd
	public ClsSalariesEngine engine;
//
//	/**
//	 * Renvoit true si le calcul se passe bien, false sinon
//	 *
//	 * @param request
//	 * @param oCalcul
//	 * @return
//	 */
//
//	public boolean calculBulletin(HttpServletRequest request, ClsCalculLancement oCalcul)
//	{
//		this.nbrATraiter = oCalcul.getListeAgents().size();
//		if (this.nbrATraiter == 0)
//			return true;
//
//		ClsService service = (ClsService) ServiceFinder.findBean("service");
//
//		String strDateFormat = ClsParameter.getSessionObject(request, ClsParameter.SESSION_FORMAT_DATE);
//
//		String cdos = ClsParameter.getSessionObject(request, ClsParameter.SESSION_DOSSIER);
//
//		String clang = ClsParameter.getSessionObject(request, ClsParameter.SESSION_LANGUE);
//
//		String user = ClsParameter.getSessionObject(request, ClsParameter.SESSION_LOGIN);
//
//		ClsParameterOfPay parameter = new ClsParameterOfPay();
//
//		ClsFictifParameterOfPay fictiveParameter = new ClsFictifParameterOfPay();
//
//		boolean initfictif = true;
//
//		ClsNomenclatureUtil utilNomenclature = (ClsNomenclatureUtil) ServiceFinder.findBean("ClsNomenclatureUtil");
//
//		ClsDate d = new ClsDate(oCalcul.getPeriodepaie(), "yyyyMM");
//		parameter.setMyMonthOfPay(d);
//		parameter.setService(service);
//		parameter.setUtilNomenclature(utilNomenclature);
//		parameter.request = request;
//		// calcul des salaires
//		parameter.setDossier(cdos);
//		parameter.setClas(oCalcul.getClassesalariemin());
//		parameter.setUseRetroactif(false);
//		parameter.setNumeroBulletin(Integer.valueOf(oCalcul.getNumerobulletin()));
//		parameter.setMonthOfPay(oCalcul.getPeriodepaie());
//		parameter.setMoisPaieCourant(oCalcul.getPeriodepaie());
//		parameter.setPeriodOfPay(oCalcul.getPeriodepaie());
//		parameter.setMyMonthOfPay(d);
//		parameter.setLangue(clang);
//
//		this.nbrErreur = 0;
//
//		if (!ClsObjectUtil.isNull(oCalcul.getModepaiement()))
//		{
//			if ("V".equals(oCalcul.getModepaiement().trim().toUpperCase()))
//				parameter.setModePaiement(ClsEnumeration.EnModePaiement.V);
//			else if ("E".equals(oCalcul.getModepaiement().trim().toUpperCase()))
//				parameter.setModePaiement(ClsEnumeration.EnModePaiement.E);
//			else if ("C".equals(oCalcul.getModepaiement().trim().toUpperCase()))
//				parameter.setModePaiement(ClsEnumeration.EnModePaiement.C);
//			else
//				parameter.setModePaiement(ClsEnumeration.EnModePaiement.UNKNOWN);
//		}
//		else
//			parameter.setModePaiement(ClsEnumeration.EnModePaiement.UNKNOWN);
//		parameter.setDepartMatricule(oCalcul.getMatriculedepart());
//		parameter.setFinMatricule(oCalcul.getMatriculearrive());
//		parameter.setSessionId(1322);
//		parameter.setUti(user);
//		parameter.setDepartNiv1(oCalcul.getNiveau1depart());
//		parameter.setFinNiv1(oCalcul.getNiveau1arrive());
//		parameter.setDepartNiv2(oCalcul.getNiveau2depart());
//		parameter.setFinNiv2(oCalcul.getNiveau2arrive());
//		parameter.setDepartNiv3(oCalcul.getNiveau3depart());
//		parameter.setFinNiv3(oCalcul.getNiveau3arrive());
//
//		parameter.setAppDateFormat(strDateFormat);
//		// initialisation
//
//		String generationFichiers = ClsConfigurationParameters.getConfigParameterValue(service, cdos, clang, ClsConfigurationParameters.GENERATION_FICHIERS_CONTROLE_LORS_CALCUL_PAIE);
//		String dossierGenerationFichiers = ClsConfigurationParameters.getConfigParameterValue(service, cdos, clang, ClsConfigurationParameters.DOSSIER_GENERATION_FICHIERS_CONTROLE_LORS_CALCUL_PAIE);
//		Integer nbrThread = NumberUtils.toInt(ClsConfigurationParameters.getConfigParameterValue(service, cdos, clang, ClsConfigurationParameters.NOMBRE_THREADS_LANCEMENT_CALCUL));
//		String synchro_traiter_salaire = ClsConfigurationParameters.getConfigParameterValue(service, cdos, clang, ClsConfigurationParameters.SYNCHRONISER_LE_CALCUL_D_UN_BULLETIN);
//
//		parameter.setGenfile(generationFichiers.charAt(0));
//		parameter.setGenfilefolder(dossierGenerationFichiers);
//		parameter.request = request;
//		parameter.session = request.getSession();
//		parameter.setLanceur(this);
//
//		if (parameter.init())
//		{
//			if (StringUtils.equals("O", parameter.getFictiveCalculus()))
//			{
//				fictiveParameter = new ClsFictifParameterOfPay();
//				BeanUtils.copyProperties(parameter, fictiveParameter);
//				fictiveParameter.setFictiveMonthOfPay(parameter.getMonthOfPay());
//				fictiveParameter.setMyMoisPaieCourant(parameter.getMyMoisPaieCourant().clone());
//				fictiveParameter.setMyMonthOfPay(parameter.getMyMonthOfPay().clone());
//
//				ClsFictifNomenclatureUtil utilNomenclatureFictif = new ClsFictifNomenclatureUtil(fictiveParameter);
//				fictiveParameter.setUtilNomenclatureFictif(utilNomenclatureFictif);
//				initfictif = fictiveParameter.init();
//				if (!initfictif)
//				{
//					this.nbrErreur = 1;
//
//					ClsTraiterSalaireThread.NBRE_AGENT_A_TRAITE = 0;
//					ClsTraiterSalaireThread.NBRE_AGENT_TRAITE = 0;
//
//					String message = ClsTreater._getResultat(request, "CALCUL DES BULLETINS", "INF-10038", false).getLibelle() + " ...";
//					String message2 = ClsTreater._getResultat(request, "**Erreur**", "INF-00972", false).getLibelle() + " ...";
//					String erreur = message2 + fictiveParameter.getError();
//					parameter.insererLogMessage(erreur);
//					return false;
//				}
//			}
//			if (initfictif)
//			{
//				ClsTraiterSalaireThread.NBRE_AGENT_A_TRAITE = oCalcul.getListeAgents().size();
//				ClsTraiterSalaireThread.NBRE_AGENT_TRAITE = 0;
//				request.getSession().setAttribute(ClsSessionObjectName.SESSION_O_CALCUL_NOMBRE_SALARIES_A_TRAITE, oCalcul.getListeAgents().size());
//				request.getSession().setAttribute(ClsSessionObjectName.SESSION_O_CALCUL_NOMBRE_SALARIES_TRAITE, 0);
//				request.getSession().setAttribute(ClsSessionObjectName.SESSION_O_CALCUL_NOMBRE_ERREURS_LORS_CALCUL, 0);
//
//				engine = new ClsSalariesEngine();
//				engine.httpSession = request.getSession();
//				engine.setThreadmax(nbrThread);
//				engine.setParameterOfSalary(parameter);
//				engine.synchronize_traiter_salaire = StringUtils.equals("O", synchro_traiter_salaire);
//				engine.setFictiveParameterOfPay(fictiveParameter);
//				engine.setService(service);
//				engine.setLanceur(this);
//				engine.execute(oCalcul.getListeAgents());
//			}
//			if (parameter.getGenfile() == 'O')
//				StringUtils.printOutObject(parameter, parameter.getGenfilefolder() + "\\parametresCalcul.txt");
//			return true;
//
//		}
//		else
//		{
//			this.nbrErreur = 1;
//
//			return false;
//		}
//
//	}

	public int getNbrErreur()
	{
		return nbrErreur;
	}

	public void setNbrErreur(int nbrErreur)
	{
		this.nbrErreur = nbrErreur;

//		try
//		{
//			if (ui != null)
//				MethodUtils.invokeMethod(ui, ILanceurCalcul.METHOD_MISE_A_JOUR_ERREUR, this.nbrErreur);
//		}
//		catch (NoSuchMethodException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		catch (IllegalAccessException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		catch (InvocationTargetException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	public int getNbrTraite()
	{
		return nbrTraite;
	}
	
	public boolean finCalcul()
	{
		//System.out.println("Dans simulateur, A Traiter = "+this.nbrATraiter+" et Trait� = "+this.nbrTraite);
		if(this.nbrTraite >= this.nbrATraiter) return true;
		
		return false;
	}

	public void setNbrTraite(int nbrTraite)
	{
		this.nbrTraite = nbrTraite;

//		try
//		{
//			if (ui != null)
//				MethodUtils.invokeMethod(ui, ILanceurCalcul.METHOD_MISE_A_JOUR_PROGRESS_BAR, this.nbrTraite);
//		}
//		catch (NoSuchMethodException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		catch (IllegalAccessException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		catch (InvocationTargetException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	public int getNbrATraiter()
	{
		return nbrATraiter;
	}

	public void setNbrATraiter(int nbrATraiter)
	{
		this.nbrATraiter = nbrATraiter;
	}

	public Object getUi()
	{
		return ui;
	}

	public void setUi(Object ui)
	{
		this.ui = ui;
	}

	public ClsSalariesEngine getEngine()
	{
		return engine;
	}

	public void setEngine(ClsSalariesEngine engine)
	{
		this.engine = engine;
	}

}
