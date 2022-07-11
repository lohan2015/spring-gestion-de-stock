package com.kinart.paie.business.services.precentralisation;

import com.kinart.paie.business.services.utils.GeneriqueConnexionService;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;

public class UIPrecentralisation //extends IceFaceUIGenericTable<ClsRhtLogEtendu>
{
	private GeneriqueConnexionService service;

	private boolean isRowCheck = false;// determine s'il existe ou non une ligne s�lectionn�e

	boolean renderSupprimer = false;// determine s'il faut ou non afficher le bouton supprimer du popup

	boolean renderEnregistrer = false;// determine s'il faut ou non afficher le bouton enregistrer du popup

	int indexlignecourante = -1;// index de la ligne courante

	boolean selectAllColumn = false; // S�lection ou pas des colonnes

	String imgColumn = null;//IMAGES_DECOCHER;// Chemin de l'Image de s�lection

	boolean displayMessage = true;// conditionne l'affichage ou non des messages retours

	private boolean expandZoneRecherche = true; // Etendre la zone de recherche ou pas

	private static final String codeEvenementColumnName = "nmat";// Colonne de tri par d�faut pour le vo

	ClsRechercheVo search;// objet permettant de faire les recherche

	public static final int PRECENTRALISATION = 5;

	public static final int EFFACER = 6;

	public static final String DATETIMEFORMATAFFICHAGE = "dd-MM-yyyy HH:mm:ss";

	public static final String DATETIMEFORMATTRAITEMENT = "dd-MM-yyyy HH:mm:ss";

	ClsInfoPrecentPaie oPrecentralisation;

	ClsAxPrecentralisation axPrecentralisation;
	
	HttpServletRequest request;

	// Le constructeur de l'ui
	public UIPrecentralisation()
	{
		//super(codeEvenementColumnName);
		this.init();

	}

	private void init()
	{
		search = new ClsRechercheVo();

		initProgressBar();

	}

	private void initProgressBar()
	{

	}

	// -------------------------GESTION DE LA ZONE DE RECHERCHE------------------------
	public void onClickBoutonRechercherListener()
	{
		initProgressBar();

		oPrecentralisation = new ClsInfoPrecentPaie();
		oPrecentralisation.setAamm(search.getPeriode());

		oPrecentralisation.setVerif_edit_bul(search.isEdbul() ? "O" : "N");

		oPrecentralisation.setRub_a_comptabilise(search.isComp() ? "O" : "N");

		oPrecentralisation.setContinu_si_erreur(search.isContinuersierreur() ? "O" : "N");

		oPrecentralisation.setOnlyerrormessage(search.isUniquementerreur() ? "O" : "N");

		oPrecentralisation.setNmat(search.getNmat());
		oPrecentralisation.setValeurnmat(search.getValnmat());

		oPrecentralisation.setNbul(search.getNbul());

	}

	public void confirmationPrecentralisation()
	{
		axPrecentralisation = new ClsAxPrecentralisation();// (ClsAxPrecentralisation) ServiceFinder.findBean("axPrecentralisationService");
		//axPrecentralisation.setService(service);
		axPrecentralisation.ui = this;
		axPrecentralisation.checkcostcenter = search.isCostcenter();
		axPrecentralisation.checkemail = search.isEmail();
//		if (axPrecentralisation.initTraitement(this.getRequest(), oPrecentralisation.getAamm(), oPrecentralisation.getNbul(), oPrecentralisation.getNmat(),
//				oPrecentralisation.getVerif_edit_bul(), oPrecentralisation.getRub_a_comptabilise(), oPrecentralisation.getContinu_si_erreur(),
//				oPrecentralisation.getOnlyerrormessage()))
//		{
//			int nbr = axPrecentralisation.oInitialisationPrecentralisation.getListeOfSalary().size();
//			if(nbr != 0)
//			{
//				ThreadPoolExecutor longRunningTaskThreadPool = new ThreadPoolExecutor(5, 15, 30, TimeUnit.SECONDS, new LinkedBlockingQueue(nbr));
//
//				longRunningTaskThreadPool.execute(new LongOperationRunner(this.getRequest()));
//			}
//			else
//			{
//				String message = ClsTreater._getResultat(getRequest(), "Confirmation", "INF-81509", false).getLibelle();
//				String message2 = ClsTreater._getResultat(getRequest(), "Aucunes donn�es retourn�es par  votre s�lection.", "ERR-30055", false).getLibelle() + " !";
//				CommonFunctions.showPopupMessage(this.erreurPopup, message, message2, PopupInfoVO.IMAGE_INFORMATION, PopupInfoVO.STYLE_INFORMATION);
//				return;
//			}
//
//		}
//		else
//		{
//
//		}

	}

	protected class LongOperationRunner implements Runnable
	{
		HttpServletRequest request;
		public LongOperationRunner(HttpServletRequest request)
		{
			this.request = request;
		}
		
		public void run()
		{

			try
			{
				boolean result = axPrecentralisation.traitementPrecentralisation(oPrecentralisation.getCdos(), request, oPrecentralisation.getAamm(), oPrecentralisation.getNbul(),
						oPrecentralisation.getNmat(), oPrecentralisation.getVerif_edit_bul(), oPrecentralisation.getRub_a_comptabilise(), oPrecentralisation
								.getContinu_si_erreur(), oPrecentralisation.getOnlyerrormessage());
				//listeDonneesToPrint = listOfDataToDisplay;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public void onClickBoutonEnregistrerListener()
	{
//		if (progressbar.isShowduration())
//		{
//			displayConfirm = true;
//			messageTitle = ClsTreater._getResultat(getRequest(), "Confirmation", "INF-81509", false).getLibelle();
//			messageRetour = ClsTreater._getResultat(getRequest(), "Proceder au traitement ?", "INF-80193", false).getLibelle();
//			action = ENREGISTREMENT_LOT;
//		}
	}

	public void confirmationEnrgistrementLog()
	{
		if (axPrecentralisation != null)
		{
//			ClsResultat oResult = axPrecentralisation.enregisterLog(getRequest());
//			String message = ClsTreater._getResultat(getRequest(), "Enregistrer", "INF-80092", false).getLibelle() + " ...";
//			CommonFunctions.showPopupMessage(this.erreurPopup, message, oResult.getLibelle(), PopupInfoVO.IMAGE_INFORMATION, PopupInfoVO.STYLE_INFORMATION);
		}
	}

	public void onClickBoutonEffacerListener()
	{
//		if (progressbar.isShowduration())
//		{
//			displayConfirm = true;
//			messageTitle = ClsTreater._getResultat(getRequest(), "Confirmation", "INF-81509", false).getLibelle();
//			messageRetour = ClsTreater._getResultat(getRequest(), "Proceder au traitement ?", "INF-80193", false).getLibelle();
//			action = EFFACER;
//		}
	}
	
	public void downloadFile(String nomFichierAscii)
	{
		try
		{

		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
		}
	}
	
	String fileToDownload;
	
	

	public String getFileToDownload()
	{
		return fileToDownload;
	}

	public void setFileToDownload(String fileToDownload)
	{
		this.fileToDownload = fileToDownload;
	}

	public void confirmationEffacerLog()
	{
		if (axPrecentralisation != null)
		{

		}
	}

	public void onClickBoutonAnnulerListener()
	{

	}

	public void confirmationAnnulation()
	{
		if (axPrecentralisation != null)
		{
//			String nbrRstant = axPrecentralisation.stopAllThread(getRequest());
//			String message = ClsTreater._getResultat(getRequest(), "Annulation", "INF-10068", false).getLibelle() + " ...";
//
//			String message2 = ClsTreater._getResultat(getRequest(), "Effectif � traiter :", "INF-10121", false).getLibelle();
//
//			String message3 = ClsTreater._getResultat(getRequest(), "Salari�s", "INF-00085", false).getLibelle();
//
//			CommonFunctions.showPopupMessage(this.erreurPopup, message, message2+" "+ nbrRstant+" "+message3, PopupInfoVO.IMAGE_INFORMATION, PopupInfoVO.STYLE_INFORMATION);
		}
	}

	public void insererUneErreur(ClsRhtLogEtendu ligne)
	{
//		listOfDataToDisplay.add(ligne);
//		try
//		{
//			persistentFacesState.render();
//		}
//		catch (RenderingException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	public void debutAffichageProgressBar(int nbrSalaries)
	{
//		Date startDate = new Date();
//		this.progressbar.setDebuttraitement(new ClsDate(startDate).getDateS(DATETIMEFORMATAFFICHAGE));
//		this.progressbar.setDtdebuttraitement(startDate);
//		this.progressbar.nbrSalaries = nbrSalaries;
//		progressbar.setPogressStarted(true);
	}

	public void mettreAJourProgressBar(int nbrSalariesTraite)
	{
//		if (progressbar.nbrSalaries != 0)
//		{
//			progressbar.setShowduration(true);
//			progressbar.setNbrSalariesTraite(nbrSalariesTraite);
//			progressbar.setPercentComplete((new BigDecimal((float) progressbar.getNbrSalariesTraite() * 100 / (float) progressbar.nbrSalaries)).intValue());
//		}

	}

	// -------------------------GESTION DE LA SAUVEGARDE PAR LOT------------------------
	public void demandeEnregistrementParLotEvent()
	{
		indexlignecourante = -1;// pour gerer le cas ou on modifier plusieurs lignes avant de cliquer sur enregistrer
		//demandeEnregistrementGeneriqueEvent(event);
	}

	public void demandeEnregistrementGeneriqueEvent()
	{
		//messageRetour = "";
		renderSupprimer = false;
		renderEnregistrer = true;
	}

	public void confirmationEnregistrementEvent()
	{


	}

	// -------------------------GESTION DU TRI------------------------

	public void setSearch(ClsRechercheVo search)
	{
		this.search = search;
	}

	public ClsRechercheVo getSearch()
	{
		return search;
	}

	public boolean isSelectAllColumn()
	{
		return selectAllColumn;
	}

	public void setSelectAllColumn(boolean selectAllColumn)
	{
		this.selectAllColumn = selectAllColumn;
	}

	public String getImgColumn()
	{
		return imgColumn;
	}

	public void setImgColumn(String imgColumn)
	{
		this.imgColumn = imgColumn;
	}

	public boolean isExpandZoneRecherche()
	{
		return expandZoneRecherche;
	}

	public void setExpandZoneRecherche(boolean expandZoneRecherche)
	{
		this.expandZoneRecherche = expandZoneRecherche;
	}

	public boolean getRenderSupprimer()
	{
		return renderSupprimer;
	}

	public void setRenderSupprimer(boolean renderSupprimer)
	{
		this.renderSupprimer = renderSupprimer;
	}

	public boolean getRenderEnregistrer()
	{
		return renderEnregistrer;
	}

	public void setRenderEnregistrer(boolean renderEnregistrer)
	{
		this.renderEnregistrer = renderEnregistrer;
	}

	public boolean isRowCheck()
	{
		return isRowCheck;
	}

	public void setRowCheck(boolean isRowCheck)
	{
		this.isRowCheck = isRowCheck;
	}

	public int getIndexlignecourante()
	{
		return indexlignecourante;
	}

	public void setIndexlignecourante(int indexlignecourante)
	{
		this.indexlignecourante = indexlignecourante;
	}

	public boolean isDisplayMessage()
	{
		return displayMessage;
	}

	public void setDisplayMessage(boolean displayMessage)
	{
		this.displayMessage = displayMessage;
	}


	public class ClsRechercheVo
	{
		private String periode;

		private String nbul = "9";

		private String nmat;

		private String valnmat;

		private boolean edbul = false;

		private boolean comp = false;

		private boolean continuersierreur = true;

		private boolean uniquementerreur = true;
		
		private boolean costcenter = false;
		private boolean email = false;

		public String getValnmat()
		{
			return valnmat;
		}

		public void setValnmat(String valnmat)
		{
			this.valnmat = valnmat;
		}

		public String getPeriode()
		{
			return periode;
		}

		public void setPeriode(String periode)
		{
			this.periode = periode;
		}

		public String getNbul()
		{
			return nbul;
		}

		public void setNbul(String nbul)
		{
			this.nbul = nbul;
		}

		public String getNmat()
		{
			return nmat;
		}

		public void setNmat(String nmat)
		{
			this.nmat = nmat;
		}

		public boolean isEdbul()
		{
			return edbul;
		}

		public void setEdbul(boolean edbul)
		{
			this.edbul = edbul;
		}

		public boolean isComp()
		{
			return comp;
		}

		public void setComp(boolean comp)
		{
			this.comp = comp;
		}

		public boolean isContinuersierreur()
		{
			return continuersierreur;
		}

		public void setContinuersierreur(boolean continuersierreur)
		{
			this.continuersierreur = continuersierreur;
		}

		public boolean isUniquementerreur()
		{
			return uniquementerreur;
		}

		public void setUniquementerreur(boolean uniquementerreur)
		{
			this.uniquementerreur = uniquementerreur;
		}

		public boolean isCostcenter()
		{
			return costcenter;
		}

		public void setCostcenter(boolean costcenter)
		{
			this.costcenter = costcenter;
		}

		public boolean isEmail()
		{
			return email;
		}

		public void setEmail(boolean email)
		{
			this.email = email;
		}

	}

}
