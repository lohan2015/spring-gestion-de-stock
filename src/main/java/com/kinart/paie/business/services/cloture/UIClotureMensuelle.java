package com.kinart.paie.business.services.cloture;


import javax.servlet.http.HttpServletRequest;

import com.kinart.paie.business.services.calcul.ClsParameterOfPay;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import org.springframework.context.ApplicationContext;

public class UIClotureMensuelle //extends IceFaceUIGenericTable<Calcul>
{
	public ClsUpdateBulletin update = null;

	public ClsParameterOfPay parameter = null;

	public ClsCentralisationService centraservice;

	private ClsUpdateBulletinService updateservice;

	protected ClsInfoCloture search;

	public GeneriqueConnexionService service;

	ApplicationContext applicationContext;

//	private Mode MODE_CREATION = Mode.CREATION;
//
//	private Mode MODE_MODIFICATION = Mode.MODIFICATION;
//
//	private Mode MODE_CONSULTATION = Mode.CONSULTATION;

	boolean displayMessage = true;// conditionne l'affichage ou non des messages retours

	private boolean expandZoneRecherche = true; // Etendre la zone de recherche ou pas

	private static final String codeEvenementColumnName = "nmat";// Colonne de tri par d�faut pour le vo

	//UIData datatable;// Datatable represantant la grille des donn�es � afficher

	//public ProgressBarVO progressbar;

	public static final int LANCEMENT = 5;

	public static final int FICHIERASCII = 6;

	public static final String DATETIMEFORMATAFFICHAGE = "dd-MM-yyyy HH:mm:ss";

	public static final String DATETIMEFORMATTRAITEMENT = "dd-MM-yyyy HH:mm:ss";

	private String traitementcourant;

	private String evolutiontraitement;

	private boolean evolutiontraitementerror = false;

	public HttpServletRequest request = null;//getRequest();

	private boolean showannimationimage = false;

	private String debuttraitement;

	private String fintraitement;

	public ClsGlobalUpdate globalUpdate;

	// Le constructeur de l'ui
	public UIClotureMensuelle()
	{
		//super(codeEvenementColumnName);
		//this.init();
		//this.setLocale(Locale.FRANCE);

		//this.useraccess = IceFaceUIGenericTable._setAccesParameter(request);
	}

	String filepath = null;

	public String traitementlance;

	private String acces = "";
	
	public String getDebuttraitement()
	{
		return debuttraitement;
	}

	public void setDebuttraitement(String debuttraitement)
	{
		this.debuttraitement = debuttraitement;
	}

	public String getFintraitement()
	{
		return fintraitement;
	}

	public void setFintraitement(String fintraitement)
	{
		this.fintraitement = fintraitement;
	}

	public boolean isEvolutiontraitementerror()
	{
		return evolutiontraitementerror;
	}

	public void setEvolutiontraitementerror(boolean evolutiontraitementerror)
	{
		this.evolutiontraitementerror = evolutiontraitementerror;
	}

	public String getTraitementlance()
	{
		return traitementlance;
	}

	public void setTraitementlance(String traitementlance)
	{
		this.traitementlance = traitementlance;
	}

	public String getAcces()
	{
		return acces;
	}

	public void setAcces(String acces)
	{
		this.acces = acces;
	}

}
