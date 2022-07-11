package com.kinart.paie.business.services.precentralisation;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.kinart.paie.business.model.*;
import com.kinart.paie.business.services.cloture.ClsNomenclature;
import com.kinart.paie.business.services.cloture.RechercheCompteRubriqueVO;
import com.kinart.paie.business.services.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;

public class ClsInitPrecentralisationPaie
{

	private String aamm;

	private String nbul;

	private String nmat;

	private String verif_edit_bul;

	private String rub_a_comptabilise;

	private String continu_si_erreur;

	private String uniquement_message_erreur;

	private String cdos;

	private String activersecuritesalarie;

	private String sortColumn = "datec";

	private String sortOrder = "ASC";

	private GeneriqueConnexionService service;

	private String user;

	private String clang;

	private int nombreerreurs;

	private HttpServletRequest request;

	private UIPrecentralisation ui;

	public String getSortColumn()
	{
		return sortColumn;
	}

	public void setSortColumn(String sortColumn)
	{
		this.sortColumn = sortColumn;
	}

	public String getSortOrder()
	{
		return sortOrder;
	}

	public void setSortOrder(String sortOrder)
	{
		this.sortOrder = sortOrder;
	}

	public ClsInitPrecentralisationPaie()
	{
	}

	public String chercherZoneLibreAgent(String cdos, String nmat, Integer numZl)
	{
		FreeZone zone = null;//(FreeZone) this.service.get(Rhtzonelibre.class, new RhtzonelibrePK(cdos, nmat, numZl));
		String queryString = "From FreeZone where idEntreprise='"+cdos+"' and nmat='"+nmat+"' and numerozl="+numZl.intValue();
		List result = service.find(queryString);
		if(result.isEmpty()) zone = null;
		else zone = (FreeZone)result.get(0);

		FreeZoneParam pzone = null;//(Rhpzonelibre) this.service.get(Rhpzonelibre.class, new RhpzonelibrePK(cdos, numZl));
		queryString = "From FreeZoneParam where idEntreprise='"+cdos+"' and numerozl="+numZl.intValue();
		result = service.find(queryString);
		if(result.isEmpty()) zone = null;
		else zone = (FreeZone)result.get(0);
		if (zone != null && pzone != null)
		{
			if (StringUtils.equalsIgnoreCase("L", pzone.getTypezl()))
				return zone.getVallzl();
			if (StringUtils.equalsIgnoreCase("M", pzone.getTypezl()))
				return zone.getValmzl() == null ? null : zone.getValmzl().toString();
			if (StringUtils.equalsIgnoreCase("T", pzone.getTypezl()))
				return zone.getValtzl() == null ? null : zone.getValtzl().toString();
			if (StringUtils.equalsIgnoreCase("D", pzone.getTypezl()))
				return zone.getValdzl() == null ? null : new SimpleDateFormat("dd-MM-yyyy").format(zone.getValdzl());
		}
		return null;
	}
	
	public String fn_getZero(int nbr)
	{
		String str="";
		for(int i=1;i<=nbr;i++)
			str +="0";
		return str;
	}

	public String fn_costCenter(String w_cle, Salarie salarie)
	{
		String res = "";
		//--R�cuperation du num�ro de la table 

		int ikey = Integer.parseInt(w_cle);

		if (ikey >= 801 && ikey <= 899)
		{
			res = this.chercherZoneLibreAgent(cdos, salarie.getNmat(), Integer.parseInt(StringUtils.substring(w_cle, 1, 3)));
			res = res.substring(0, 10);
		}
		else
		{
			switch (ikey)
			{
			case 1:
				res = salarie.getNiv1();
				break;
			case 2:
				res = salarie.getNiv2();
				break;
			case 3:
				res = salarie.getNiv3();
				break;

			case 4:
				res = salarie.getNmat();
				break;
			case 8:
				res = salarie.getEqui();
				break;
			case 11:
				res = salarie.getSexe();
				break;
			case 21:
				res = salarie.getNato();
				break;
			case 22:
				res = salarie.getSitf();
				break;
			case 23:
				res = ClsStringUtil.formatNumber(salarie.getNbcj(), "0000");
				break;
			case 24:
				res = ClsStringUtil.formatNumber(salarie.getNbec(), "0000");
				break;
			case 25:
				res = ClsStringUtil.formatNumber(salarie.getNbfe(), "0000");
				break;
			case 27:
				res = salarie.getModp();
				break;
			case 33:
				res = StringUtil.substring(salarie.getCcpt(), 0, 10);
				break;
			case 39:
				res = salarie.getCat();
				break;
			case 40:
				res = salarie.getEch();
				break;
			case 43:
				res = salarie.getGrad();
				break;
			case 44:
				res = salarie.getFonc();
				break;
			case 45:
				res = salarie.getAfec();
				break;
			case 47:
				res = ClsStringUtil.formatNumber(salarie.getIndi(), "000000");
				break;
			case 58:
				res = salarie.getHifo();
				break;
			case 59:
				res = salarie.getZli1();
				break;
			case 60:
				res = salarie.getZli2();
				break;
			case 67:
				res = salarie.getTypc();
				break;
			case 70:
				res = salarie.getRegi();
				break;
			case 71:
				res = salarie.getZres();
				break;
			case 72:
				res = salarie.getDmst();
				break;
			case 73:
				res = ClsStringUtil.formatNumber(salarie.getNpie(), "0000");
				break;
			case 184:
				res = StringUtil.substring(salarie.getSana(), 0, 10);
				break;
			case 185:
				res = salarie.getEqui();
				break;
			default:
				res = " ";
				break;
			}
		}

		return res;
	}

	private ClsRhtLogEtendu _createNewRhtLogEtendu(ClsResultat oResult, boolean isBlockingErrorOrIsEndBlock)
	{

		return new ClsRhtLogEtendu(new LogMessage(new Integer(this.getCdos()), this.getUser(), new Date(), oResult.getLibelle()), oResult.getLibelle(), oResult.getIserrormessage(), isBlockingErrorOrIsEndBlock);
	}

	public boolean _memorySaveListeOfLogs(List<ClsRhtLogEtendu> listeLogEtendu, ClsResultat oResult)
	{
		//if (request.getSession().getAttribute(ClsSessionObjectName.SESSION_O_PRECENTRALISATION_LISTE) == null) { return false; }
		if ("O".equalsIgnoreCase(this.getUniquement_message_erreur()))
		{
			if (oResult != null && oResult.isErrormessage())
			{
				ClsRhtLogEtendu o = this._createNewRhtLogEtendu(oResult, false);
				listeLogEtendu.add(o);
				request.getSession().setAttribute(ClsSessionObjectName.SESSION_O_PRECENTRALISATION_LISTE, listeLogEtendu);
				if (ui != null)
				{
					ui.insererUneErreur(o);
				}
			}
		}
		else
		{
			ClsRhtLogEtendu o = this._createNewRhtLogEtendu(oResult, false);
			listeLogEtendu.add(o);
			//request.getSession().setAttribute(ClsSessionObjectName.SESSION_O_PRECENTRALISATION_LISTE, listeLogEtendu);
			if (ui != null)
			{
				ui.insererUneErreur(o);
			}
		}

		return true;
	}

	public boolean _memorySaveListeOfLogs(List<ClsRhtLogEtendu> listeLogEtendu, ClsResultat oResult, boolean isBlockingErrorOrIsEndBlock)
	{
//		if (request.getSession().getAttribute(ClsSessionObjectName.SESSION_O_PRECENTRALISATION_LISTE) == null)
//			return false;
//		if (isBlockingErrorOrIsEndBlock && oResult != null && oResult.isErrormessage())
//			request.getSession().setAttribute(ClsSessionObjectName.SESSION_O_PRECENTRALISATION_BLOCKING_MESSAGE, oResult);
		if ("O".equalsIgnoreCase(this.getUniquement_message_erreur()))
		{
			if (oResult != null && oResult.isErrormessage())
			{
				ClsRhtLogEtendu o = this._createNewRhtLogEtendu(oResult, isBlockingErrorOrIsEndBlock);
				listeLogEtendu.add(o);
				request.getSession().setAttribute(ClsSessionObjectName.SESSION_O_PRECENTRALISATION_LISTE, listeLogEtendu);
				if (ui != null)
				{
					ui.insererUneErreur(o);
				}
			}
		}
		else
		{
			ClsRhtLogEtendu o = this._createNewRhtLogEtendu(oResult, isBlockingErrorOrIsEndBlock);
			listeLogEtendu.add(o);
			request.getSession().setAttribute(ClsSessionObjectName.SESSION_O_PRECENTRALISATION_LISTE, listeLogEtendu);
			if (ui != null)
			{
				ui.insererUneErreur(o);
			}
		}

		return true;
	}

	String generationFichiers = "N";
	String dossierGenerationFichiers = StringUtil.EMPTY;

	boolean nouvelleCompta = false;
	RechercheCompteRubriqueVO nouvelleComptabilisation;

	// cdos , codeutilisateur , datedebut , datefin ,message, strSortColumn ,
	// strSortOrder
	public ClsInitPrecentralisationPaie(String cdos, String aamm, String nbul, String nmat, String verif_edit_bul, String rub_a_comptabilise, String continu_si_erreur, String uniquement_message_erreur)
	{
		setAamm(aamm);
		setNbul(nbul);
		setNmat(nmat);
		setVerif_edit_bul(verif_edit_bul);
		setRub_a_comptabilise(rub_a_comptabilise);
		setContinu_si_erreur(continu_si_erreur);
		setCdos(cdos);
		setNombreerreurs(0);
		setUniquement_message_erreur(uniquement_message_erreur);

	}

	public void init()
	{
		ParamData fnom = null;//(Rhfnom) service.get(Rhfnom.class, new RhfnomPK(cdos, 99, "NEWCPTRUB", 1));
		String queryString = "From ParamData where idEntreprise='"+cdos+"' and ctab=99 and cacc='NEWCPTRUB' and nume=1";
		List result = service.find(queryString);
		if(result.isEmpty()) fnom = null;
		else fnom = (ParamData)result.get(0);

		nouvelleCompta = (fnom != null && StringUtils.isNotBlank(fnom.getVall()) && StringUtils.equals("O", fnom.getVall()));
		if (nouvelleCompta)
		{
			nouvelleComptabilisation = new RechercheCompteRubriqueVO();
			nouvelleComptabilisation.init(request, service, cdos);
		}
	}

	public void writeInTextLog(String message)
	{
		if (StringUtils.equals("O", generationFichiers))
			StringUtil.printOutObject(message, dossierGenerationFichiers + "\\precentralisation.txt", false);
	}

	private List<ElementSalaire> allRubriqueListe;

	private List<ElementSalaire> fetchAllRubriqueListe()
	{

		String _strQuery = "From ElementSalaire where idEntreprise=" + "'" + this.getCdos() + "'";

		ParameterUtil.println("requete liste all rubriques: " + _strQuery);

		List<ElementSalaire> oListeRhprubrique = new ArrayList<ElementSalaire>();
		try
		{
			oListeRhprubrique = service.find(_strQuery);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return oListeRhprubrique;
	}

	private List<ElementSalaire> rubriqueAComptabiliserListe;

	private List<ElementSalaire> fetchRubriqueAComptabiliserListe()
	{

		/*String _strQuery = "From Rhprubrique where cdos=" + "'" + this.getCdos() + "' and comp='O'";

		List<Rhprubrique> oListeRhprubrique = new ArrayList<Rhprubrique>();
		try
		{
			oListeRhprubrique = service.find(_strQuery);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return oListeRhprubrique;
		*/

		List<ElementSalaire> oListeRhprubrique = new ArrayList<ElementSalaire>();

		List<ElementSalaire> oListeAllRubrique = this.getAllRubriqueListe();
		for (int i = 0; i < oListeAllRubrique.size(); i++)
		{
			if ("O".equalsIgnoreCase(oListeAllRubrique.get(i).getComp()))
				oListeRhprubrique.add(oListeAllRubrique.get(i));
		}

		return oListeRhprubrique;

	}

	public void incrementerErreurs()
	{
		this.setNombreerreurs(this.getNombreerreurs() + 1);
	}

	private List<CalculPaie> listeOfRhtcalcul;

	private List<CalculPaie> getRhtcalculOfPeriod(String nmat)
	{
		String _strQuery = "From CalculPaie a where a.idEntreprise=" + "'" + this.getCdos() + "'" + " and aamm = " + "'" + this.getAamm() + "' and a.nbul=" + this.getNbul() + " and nmat='" + nmat + "'";

		if ("O".equalsIgnoreCase(this.getRub_a_comptabilise()))
			_strQuery += " and exists ( select 1 from ElementSalaire b where  b.idEntreprise = a.idEntreprise and a.rubq = b.crub  and b.comp ='" + this.getRub_a_comptabilise() + "') ";
		_strQuery += "order by a.idEntreprise, a.aamm, a.nmat, a.nbul, a.rubq, a.nlig";
		List<CalculPaie> oListeRhtcalcul = new ArrayList<CalculPaie>();
		try
		{
			oListeRhtcalcul = service.find(_strQuery);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return oListeRhtcalcul;
	}

	public ClsNomenclature findFromNomenclature(String cacc, List<ParamData> liste)
	{
		ClsNomenclature nomenclature = null;
		String code = "";
		String libelle = "";
		String montant = "";
		String taux = "";
		String date = "";

		for (ParamData rhfnom : liste)
		{
			code = "";
			libelle = "";
			montant = "";
			taux = "";
			date = "";
			if (rhfnom.getCacc().equalsIgnoreCase(cacc))
			{
				code = cacc;
				libelle = rhfnom.getVall();
				if (rhfnom.getValm() != null)
					montant = String.valueOf(rhfnom.getValm());
				if (rhfnom.getValt() != null)
					taux = String.valueOf(rhfnom.getValt());
				if (rhfnom.getVald() != null)
					date = String.valueOf(rhfnom.getVald());
				break;
			}
		}

		nomenclature = new ClsNomenclature(code, libelle, montant, taux, date);

		return nomenclature;
	}

	public int indexOfNomenclature(String cacc, int ctab)
	{
		List<ParamData> oListe = this.getDonneesNomenclature(ctab);

		for (int i = 0; i < oListe.size(); i++)
		{
			if (oListe.get(i).getCacc().equalsIgnoreCase(cacc))
				return i;
		}

		return -1;
	}

	public ClsNomenclature findFromPret(String cacc, List<PretExterneEntete> liste)
	{
		ClsNomenclature nomenclature = null;
		String code = "";
		String libelle = "";
		String montant = "";
		String taux = "";
		String date = "";
		try
		{
			cacc = String.valueOf(Integer.parseInt(cacc));
		}
		catch (NumberFormatException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (PretExterneEntete ParamData : liste)
		{
			if (ParamData.getNprt().equalsIgnoreCase(cacc))
			{
				code = cacc;
				break;
			}
		}

		nomenclature = new ClsNomenclature(code, libelle, montant, taux, date);

		return nomenclature;
	}

	public ClsNomenclature findCompteFromPret(String cacc, List<PretExterneEntete> liste)
	{
		ClsNomenclature nomenclature = null;
		String code = "";
		String libelle = "";
		String montant = "";
		String taux = "";
		String date = "";

		for (PretExterneEntete rhfnom : liste)
		{
			if (rhfnom.getNprt().equalsIgnoreCase(cacc))
			{
				code = rhfnom.getNcpt();
				break;
			}
		}

		nomenclature = new ClsNomenclature(code, libelle, montant, taux, date);

		return nomenclature;
	}

	public ClsNomenclature findFromPretAgent(String cacc, List<PretInterne> liste)
	{
		ClsNomenclature nomenclature = null;
		String code = "";
		String libelle = "";
		String montant = "";
		String taux = "";
		String date = "";

		for (PretInterne rhfnom : liste)
		{
			if (rhfnom.getLg().equals(Integer.valueOf(cacc)))
			{
				code = cacc;
				break;
			}
		}

		nomenclature = new ClsNomenclature(code, libelle, montant, taux, date);

		return nomenclature;
	}

	//	public List<Rhfnom> getListeTable28()
	//	{
	//		String _strQuery = "From ParamData where cdos=" + "'" + this.getCdos() + "'";
	//		List<Rhfnom> oListeParamData = new ArrayList<Rhfnom>();
	//
	//		try
	//		{
	//			oListeParamData = this.service.find(_strQuery);
	//		}
	//		catch (Exception e)
	//		{
	//			// TODO: handle exception
	//			e.printStackTrace();
	//		}
	//		return oListeRhfnom;
	//	}

	private Map<Integer, List<ParamData>> mapListeDonneesNomenclature = new HashMap<Integer, List<ParamData>>();

	private List<ParamData> getListeDonneesNomenclature(int ctab)
	{
		String _strQuery = "From ParamData where idEntreprise=" + "'" + this.getCdos() + "'" + " and nume=1 and ctab=" + ctab;
		List<ParamData> oListeParamData = new ArrayList<ParamData>();

		try
		{
			oListeParamData = this.service.find(_strQuery);
		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
		return oListeParamData;
	}

	private List<Salarie> listeOfSalary;

	private List<Salarie> getSalaryOfPeriod()
	{
		String _strQuery = "From Salarie a where a.identreprise = '" + this.getCdos() + "' and exists ( " + "select 1 from CalculPaie b where  b.idEntreprise = a.identreprise and a.nmat = b.nmat and b.aamm='" + this.getAamm() + "' " + "and b.nbul=" + this.getNbul();
		if (!StringUtils.isEmpty(this.getNmat()))
			_strQuery += " and UPPER(b.nmat) like UPPER('" + this.getNmat() + "' )";
		_strQuery += ") order by nmat";

		ParameterUtil.println("requete liste agent dans tcalcul: " + _strQuery);
		List<Salarie> oRhpagent = new ArrayList<Salarie>();
		try
		{
			oRhpagent = this.service.find(_strQuery);
		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
		return oRhpagent;
	}

	private List<PretInterne> listePretsAgent;

	private List<PretInterne> getListeprentagent(String nmat, String nlig)
	{
		// List<ClsInfoCumul> _o_Cumuls = new ArrayList<ClsInfoCumul>();
		String _strQuery = "From PretInterne where idEntreprise = '" + this.getCdos() + "' and nmat = '" + nmat + "' and lg = '" + nlig + "' ";
		ParameterUtil.println("requete liste agent dans PretInterne: " + _strQuery);
		List<PretInterne> oRhtpretsagent = new ArrayList<PretInterne>();
		try
		{
			oRhtpretsagent = this.service.find(_strQuery);
		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
		return oRhtpretsagent;
	}

	private List<PretExterneEntete> listePretEnt;

	private List<PretExterneEntete> getListeprentent()
	{
		// List<ClsInfoCumul> _o_Cumuls = new ArrayList<ClsInfoCumul>();
		String _strQuery = "From PretExterneEntete where idEntreprise = '" + this.getCdos() + "'";
		ParameterUtil.println("requete liste agent dans PretInterne: " + _strQuery);
		List<PretExterneEntete> oRhtpretent = new ArrayList<PretExterneEntete>();
		try
		{
			oRhtpretent = this.service.find(_strQuery);
		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
		return oRhtpretent;
	}

	public List<Cpsec> listeSectionAnalytique;

	private List<Cpsec> loadListeSectionAnalytique()
	{
		String _strQuery = "From Cpsec where cdos = '" + this.getCdos() + "' and naxa='1' and cnsa='R'";
		List<Cpsec> oCpsec = new ArrayList<Cpsec>();
		try
		{
			oCpsec = this.service.find(_strQuery);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return oCpsec;
	}

	public List<Cpcpt> listeCompteComptabilite;

	private List<Cpcpt> loadListeCompteComptabilite()
	{
		String _strQuery = "From Cpcpt where cdos = '" + this.getCdos() + "'";
		ParameterUtil.println("requete liste agent dans Rhtpretent: " + _strQuery);
		List<Cpcpt> oCpcpt = new ArrayList<Cpcpt>();
		try
		{
			oCpcpt = this.service.find(_strQuery);
		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
		return oCpcpt;
	}

	//V�rification de l'existance de la rubrique salaire brut param�tr�e en table 99 avec cl� RUBBRUT et montant 1 = crub
	public ClsCheckNomenclature _verifyRubriqueBrutExistance()
	{
		return _verifyRubriqueExistance(ParameterUtil.RUBRIQUE_BRUTE);
	}

	//V�rification de l'existance de la rubrique net � payer param�tr�e en table 99 avec cl� RUBNAP et montant 1 = crub
	public ClsCheckNomenclature _verifyRubriqueNetAPayerExistance()
	{
		return _verifyRubriqueExistance(ParameterUtil.RUBRIQUE_NAP);
	}

	//V�rification de l'existance des salari�s dans Rhtcalcul
	public boolean _verifyRhtcalculSalariesExistance(String nmat)
	{
		boolean result = false;

		String strQuery = "Select count(*) From CalculPaie where aamm='" + this.getAamm() + "' and idEntreprise='" + this.getCdos() + "'";

		try
		{
			List oList = service.find(strQuery);
			int intValue = (new Integer(oList.get(0).toString())).intValue();
			if (intValue > 0)
				result = true;
		}
		catch (DataAccessException e)
		{
			e.printStackTrace();
		}

		return result;
	}

	//V�rification de l'existance des rubriques dans Rhprubrique
	public boolean _verifyRhprubriquesExistance()
	{
		boolean result = false;

		String strQuery = "Select count(*) From ElementSalaire where idEntreprise='" + this.getCdos() + "'";

		try
		{
			List oList = service.find(strQuery);
			int intValue = (new Integer(oList.get(0).toString())).intValue();
			if (intValue > 0)
				result = true;
		}
		catch (DataAccessException e)
		{
			e.printStackTrace();
		}

		return result;
	}

	//V�rification de l'existance des rubriques � comptabiliser dans Rhprubrique
	public boolean _verifyRhprubriquesAComptabiliserExistance()
	{
		boolean result = false;

		String strQuery = "Select count(*) From ElementSalaire where idEntreprise='" + this.getCdos() + "' and comp='O'";

		try
		{
			List oList = service.find(strQuery);
			int intValue = (new Integer(oList.get(0).toString())).intValue();
			if (intValue > 0)
				result = true;
		}
		catch (DataAccessException e)
		{
			e.printStackTrace();
		}

		return result;
	}

	//	public String getResultVerification()
	//	{
	//		String result = _verifyRubriqueBrutExistance();
	//		
	//		result += _verifyRubriqueNetAPayerExistance();
	//		
	//		result += _verifyRhtcalculSalariesExistance();
	//		
	//		result += _verifyRhprubriquesExistance();
	//		
	//		result += _verifyRhprubriquesAComptabiliserExistance();
	//		
	//		return result;
	//	}

	public ClsCheckNomenclature _verifyRubriqueExistance(String strCle)
	{

		//ClsNomenclature oNome = service.findFromNomenclature(this.getCdos(), this.getClang(), ClsNomenclature.TABLES_PARAMETRAGES_SYSTEME, strCle);

		ParamData oNome =null;
		String queryString = "From ParamData where idEntreprise='"+this.getCdos()+"' and ctab=99 and cacc='"+strCle+"' and nume=1";
		List result = service.find(queryString);
		if(result.isEmpty()) oNome = null;
		else oNome = (ParamData)result.get(0);

		if (StringUtils.isEmpty(oNome.getCacc()))
			return new ClsCheckNomenclature(false, false, null);
		if (oNome.getValm()==null)
			return new ClsCheckNomenclature(true, false, null);

		String crub = ParameterUtil._completerLongueurRubrique(oNome.getValm().intValue()+"");
		return new ClsCheckNomenclature(true, true, crub);
		//		try
		//		{
		//			Rhprubrique rubrique = (Rhprubrique)service.get(Rhprubrique.class, new RhprubriquePK(this.getCdos(),crub));
		//			if(rubrique != null)
		//				result = new ClsCheckNomenclature(true, true,crub);
		//		}
		//		catch (DataAccessException e)
		//		{
		//			e.printStackTrace();
		//		}

	}

	public Map<Integer, List<ParamData>> getMapListeDonneesNomenclature()
	{
		return mapListeDonneesNomenclature;
	}

	public void setMapListeDonneesNomenclature(Map<Integer, List<ParamData>> mapListeDonneesNomenclature)
	{
		this.mapListeDonneesNomenclature = mapListeDonneesNomenclature;
	}

	public List<ParamData> getDonneesNomenclature(int ctab)
	{
		List<ParamData> oListe = this.getMapListeDonneesNomenclature().get(ctab);

		if (oListe != null)
			return oListe;

		oListe = this.getListeDonneesNomenclature(Integer.valueOf(ctab));

		this.getMapListeDonneesNomenclature().put(ctab, oListe);

		return oListe;
	}

	public List<Salarie> getListeOfSalary()
	{
		if (listeOfSalary != null)
			return listeOfSalary;
		listeOfSalary = this.getSalaryOfPeriod();
		return listeOfSalary;
	}

	public List<PretInterne> getListePretsAgent(String nmat, String nlig)
	{
		//if(listePretsAgent!=null) return listePretsAgent;
		listePretsAgent = this.getListeprentagent(nmat, nlig);
		return listePretsAgent;
	}

	public List<PretExterneEntete> getListePretEnt()
	{
		if (listePretEnt != null)
			return listePretEnt;
		listePretEnt = this.getListeprentent();
		return listePretEnt;
	}

	public List<CalculPaie> getListeOfRhtcalcul(String nmat)
	{
		//if(listeOfRhtcalcul != null) return listeOfRhtcalcul;
		listeOfRhtcalcul = this.getRhtcalculOfPeriod(nmat);
		return listeOfRhtcalcul;
	}

	public List<ElementSalaire> getAllRubriqueListe()
	{
		if (allRubriqueListe != null)
			return allRubriqueListe;
		allRubriqueListe = this.fetchAllRubriqueListe();
		return allRubriqueListe;
	}

	public ElementSalaire existRubrique(String crub)
	{
		for (int i = 0; i < this.getAllRubriqueListe().size(); i++)
		{
			if (this.getAllRubriqueListe().get(i).getCrub().equals(crub))
				return getAllRubriqueListe().get(i);
		}
		return null;
	}

	public int indexOfRubrique(ElementSalaire oRubrique)
	{
		for (int i = 0; i < this.getAllRubriqueListe().size(); i++)
		{
			if (this.getAllRubriqueListe().get(i).getCrub().equals(oRubrique.getCrub()))
				return i;
		}
		return -1;
	}

	public Cpcpt existCompteComptabilite(String cpcpt)
	{
		for (int i = 0; i < this.getListeCompteComptabilite().size(); i++)
		{
			if (this.getListeCompteComptabilite().get(i).getNcpt().equals(cpcpt))
				return getListeCompteComptabilite().get(i);
		}
		return null;
	}

	public Cpsec existSectionAnalytique(String cpsec)
	{
		for (int i = 0; i < this.getListeSectionAnalytique().size(); i++)
		{
			if (this.getListeSectionAnalytique().get(i).getNsec().equals(cpsec))
				return getListeSectionAnalytique().get(i);
		}
		return null;
	}

	public List<ElementSalaire> getRubriqueAComptabiliserListe()
	{
		if (rubriqueAComptabiliserListe != null)
			return rubriqueAComptabiliserListe;
		rubriqueAComptabiliserListe = this.fetchRubriqueAComptabiliserListe();
		return rubriqueAComptabiliserListe;
	}

	public List<Cpcpt> getListeCompteComptabilite()
	{
		if (listeCompteComptabilite != null)
			return listeCompteComptabilite;
		listeCompteComptabilite = this.loadListeCompteComptabilite();
		return listeCompteComptabilite;
	}

	public List<Cpsec> getListeSectionAnalytique()
	{
		if (listeSectionAnalytique != null)
			return listeSectionAnalytique;
		listeSectionAnalytique = this.loadListeSectionAnalytique();
		return listeSectionAnalytique;
	}

	//	---------------------------------------------------------------------------------
	//	-- Comptabilisation au debit ou au credit
	//	-----------------------------------------
	public ClsNomenclature ch_rub(ElementSalaire oRubrique, ClsCheckNomenclature oCheckRubBrut, BigDecimal zmont, String zsens, BigDecimal zdeb, BigDecimal zcre)
	{
		if (StringUtils.equals("2110", oRubrique.getCrub()))
		{
			ParameterUtil.println("Cas de la rubrique 2110");
		}
		int prbul = Integer.parseInt(String.valueOf(oRubrique.getPrbul()));
		int ind_typr = 0;
		ClsNomenclature oNome = findFromNomenclature(oRubrique.getTypr(), getDonneesNomenclature(28));
		if (!StringUtils.isEmpty(oNome.getMontant()))
			ind_typr = Integer.parseInt(oNome.getMontant());

		if ("D".equalsIgnoreCase(zsens))
		{
			//cas d'une comptabilisation au d�bit
			if (oRubrique.getCrub().equals(oCheckRubBrut.getColumnvalue()) || ind_typr == 3)
			{
				zdeb = zdeb.add(zmont);
				ParameterUtil.println(oRubrique.getCrub() + "  -  " + oRubrique.getLrub() + " ....D....Signe=+...." + zmont);
			}
			else
			{
				if (prbul == 3 || prbul == 4)
				{
					zdeb = zdeb.subtract(zmont);
					ParameterUtil.println(oRubrique.getCrub() + "  -  " + oRubrique.getLrub() + " ....D....Signe=-...." + zmont);
				}
				else
				{
					zdeb = zdeb.add(zmont);
					ParameterUtil.println(oRubrique.getCrub() + "  -  " + oRubrique.getLrub() + " ....D....Signe=+...." + zmont);
				}

			}

		}
		else
		{
			//cas d'une comptabilisation au cr�dit
			if (ind_typr == 3)
			{
				zcre = zcre.add(zmont);
				ParameterUtil.println(oRubrique.getCrub() + "  -  " + oRubrique.getLrub() + " ....C....Signe=+...." + zmont);
			}
			else
			{
				if (prbul == 1 || prbul == 2)
				{
					zcre = zcre.subtract(zmont);
					ParameterUtil.println(oRubrique.getCrub() + "  -  " + oRubrique.getLrub() + " ....C....Signe=-...." + zmont);
				}
				else
				{
					zcre = zcre.add(zmont);
					ParameterUtil.println(oRubrique.getCrub() + "  -  " + oRubrique.getLrub() + " ....C....Signe=+...." + zmont);
				}
			}
		}
		ParameterUtil.println(oRubrique.getCrub() + "  -  " + oRubrique.getLrub() + " ....Mtdeb =" + zdeb + " et Mtcre =" + zcre + " En chaine " + zdeb.toString() + " et " + zcre.toString());
		return new ClsNomenclature(zdeb.toString(), zcre.toString());
	}

	//	---------------------------------------------------------------------------------
	//	-- Recherche du compte
	//	----------------------
	public String rcpt(Salarie agent, ElementSalaire oRubrique, String r_type, int r_num)
	{
		if (nouvelleCompta) { return nouvelleComptabilisation.getCompte(oRubrique.getCrub(), r_type, agent); }
		if ("D".equalsIgnoreCase(r_type))
		{
			if (r_num == 1)
				return oRubrique.getDe01();
			if (r_num == 2)
				return oRubrique.getDe02();
			if (r_num == 3)
				return oRubrique.getDe03();
			if (r_num == 4)
				return oRubrique.getDe04();
			if (r_num == 5)
				return oRubrique.getDe05();
			if (r_num == 6)
				return oRubrique.getDe06();
			if (r_num == 7)
				return oRubrique.getDe07();
			if (r_num == 8)
				return oRubrique.getDe08();
			if (r_num == 9)
				return oRubrique.getDe09();
			if (r_num == 10)
				return oRubrique.getDe10();
			if (r_num == 11)
				return oRubrique.getDe11();
			if (r_num == 12)
				return oRubrique.getDe12();
			if (r_num == 13)
				return oRubrique.getDe13();
			if (r_num == 14)
				return oRubrique.getDe14();
			if (r_num == 15)
				return oRubrique.getDe15();
			if (r_num == 16)
				return oRubrique.getDe16();
			if (r_num == 17)
				return oRubrique.getDe17();
			if (r_num == 18)
				return oRubrique.getDe18();
			if (r_num == 19)
				return oRubrique.getDe19();
			if (r_num == 20)
				return oRubrique.getDe20();

		}
		else
		{
			if (r_num == 1)
				return oRubrique.getCr01();
			if (r_num == 2)
				return oRubrique.getCr02();
			if (r_num == 3)
				return oRubrique.getCr03();
			if (r_num == 4)
				return oRubrique.getCr04();
			if (r_num == 5)
				return oRubrique.getCr05();
		}

		return null;

	}

	public String getCdos()
	{
		return cdos;
	}

	public void setCdos(String cdos)
	{
		this.cdos = cdos;
	}

	public GeneriqueConnexionService getService()
	{
		return service;
	}

	public void setService(GeneriqueConnexionService service)
	{
		this.service = service;
		generationFichiers = "O";//ClsConfigurationParameters.getConfigParameterValue(service, cdos, clang, ClsConfigurationParameters.GENERATION_FICHIERS_CONTROLE_LORS_CALCUL_PAIE);
		dossierGenerationFichiers = "O";//ClsConfigurationParameters.getConfigParameterValue(service, cdos, clang, ClsConfigurationParameters.DOSSIER_GENERATION_FICHIERS_CONTROLE_LORS_CALCUL_PAIE);
	}

	public String getUser()
	{
		return user;
	}

	public void setUser(String user)
	{
		this.user = user;
	}

	public String getClang()
	{
		return clang;
	}

	public void setClang(String clang)
	{
		this.clang = clang;
	}

	public String getActiversecuritesalarie()
	{
		return activersecuritesalarie;
	}

	public void setActiversecuritesalarie(String activersecuritesalarie)
	{
		this.activersecuritesalarie = activersecuritesalarie;
	}

	public String getContinu_si_erreur()
	{
		return continu_si_erreur;
	}

	public void setContinu_si_erreur(String continu_si_erreur)
	{
		this.continu_si_erreur = continu_si_erreur;
	}

	public String getNbul()
	{
		return nbul;
	}

	public void setNbul(String nbul)
	{
		this.nbul = nbul;
	}

	public String getRub_a_comptabilise()
	{
		return rub_a_comptabilise;
	}

	public void setRub_a_comptabilise(String rub_a_comptabilise)
	{
		this.rub_a_comptabilise = rub_a_comptabilise;
	}

	public String getVerif_edit_bul()
	{
		return verif_edit_bul;
	}

	public void setVerif_edit_bul(String verif_edit_bul)
	{
		this.verif_edit_bul = verif_edit_bul;
	}

	public String getAamm()
	{
		return aamm;
	}

	public void setAamm(String aamm)
	{
		this.aamm = aamm;
	}

	public String getNmat()
	{
		return nmat;
	}

	public void setNmat(String nmat)
	{
		this.nmat = nmat;
	}

	public HttpServletRequest getRequest()
	{
		return request;
	}

	public void setRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	public int getNombreerreurs()
	{
		return nombreerreurs;
	}

	public void setNombreerreurs(int nombreerreurs)
	{
		this.nombreerreurs = nombreerreurs;
	}

	public String getUniquement_message_erreur()
	{
		return uniquement_message_erreur;
	}

	public void setUniquement_message_erreur(String uniquement_message_erreur)
	{
		this.uniquement_message_erreur = uniquement_message_erreur;
	}

	public UIPrecentralisation getUi()
	{
		return ui;
	}

	public void setUi(UIPrecentralisation ui)
	{
		this.ui = ui;
	}

}
