package com.kinart.paie.business.services.cloture;

import java.util.ArrayList;
import java.util.List;

import com.kinart.paie.business.model.*;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.kinart.paie.business.services.utils.ParameterUtil;
import com.kinart.stock.business.model.Utilisateur;
import org.hibernate.Session;
import org.springframework.dao.DataAccessException;

@SuppressWarnings("unchecked")
public class ClsCentralisationDao //extends Dao
{

	//ClsCIDao cidao;

	public String _getLangueUtilisateur(GeneriqueConnexionService service, String cdos, String cuti) throws DataAccessException
	{
		List result = service.find("From Utilisateur Where identreprise="+cdos+" And email='"+cuti+"'");
		if(result.isEmpty()) return null;
		return ((Utilisateur)result.get(0)).getClang();
	}

	public String _getCompFromRhpdos(GeneriqueConnexionService service, String cdos)
	{
		List result = service.find("From DossierPaie Where idEntreprise="+cdos);
		if(result.isEmpty()) return null;
		return ((DossierPaie)result.get(0)).getComp();
	}

	public ParamData _chercherEnNomenclature(GeneriqueConnexionService service, String cdos, String ctab, String cacc, Integer nume)
	{
		String queryString = "From ParamData where idEntreprise='"+cdos+"' and ctab="+ctab+" and cacc='"+cacc+"' and nume="+nume.intValue();
		List result = service.find(queryString);
		if(result.isEmpty()) return null;
		else return(ParamData)result.get(0);
	}

	public ParamData _chercherEnNomenclature(GeneriqueConnexionService service, String cdos, Integer ctab, String cacc, Integer nume)
	{
		String queryString = "From ParamData where idEntreprise='"+cdos+"' and ctab="+ctab.intValue()+" and cacc='"+cacc+"' and nume="+nume.intValue();
		List result = service.find(queryString);
		if(result.isEmpty()) return null;
		else return(ParamData)result.get(0);
	}

	public String _chercherLibelleJournalFromCp_Jou(GeneriqueConnexionService service, String cdos, String codeJournal)
	{
		String query = "From Journal where idEntreprise='" + cdos + "' and codjou= '" + codeJournal + "'";
		List<Journal> cpjoux = service.find(query);
		if (!cpjoux.isEmpty())
			return cpjoux.get(0).getLibjou();
		return null;
	}

	public String _chercherLibelleEtablissementFromCpEt(GeneriqueConnexionService service, String cdos, String codeEtablissement)
	{
		String query = "From Etablissement where idEntreprise='" + cdos + "' and codets= '" + codeEtablissement + "'";
		List<Etablissement> ets = service.find(query);
		if (!ets.isEmpty())
			return ets.get(0).getLibets();
		return null;
	}

	public CpDevise _getInformationSectionFromCpDe(GeneriqueConnexionService service, String cdos, String codedestination, Integer numaxe)
	{
		String query = "From CpDevise where idEntreprise='" + cdos + "' and coddes= '" + codedestination + "' and numaxe=" + numaxe + " and typdes = 'IMP'";
		List<CpDevise> cpdes = service.find(query);
		if (!cpdes.isEmpty())
			return cpdes.get(0);
		return null;
	}

	public String _chercherLibelleAbbregeFromCpAbr(GeneriqueConnexionService service, String cdos, String libelleAbrege)
	{
		String query = "From CpAbr where idEntreprise='" + cdos + "' and codabr= '" + libelleAbrege + "'";
		List<CpAbr> abrs = service.find(query);
		if (!abrs.isEmpty())
			return abrs.get(0).getLibabr();
		return null;
	}

	public String _getDeviseDossierFromCpdos(GeneriqueConnexionService service, String cdos)
	{
		String query = "From DossierPaie where idEntreprise='" + cdos + "'";
		List<DossierPaie> abrs = service.find(query);
		if (!abrs.isEmpty())
			return abrs.get(0).getDdev();
		return null;
	}
	
	public DossierPaie _getCpdos(GeneriqueConnexionService service, String cdos)
	{
		String query = "From DossierPaie where idEntreprise='" + cdos + "'";
		List<DossierPaie> abrs = service.find(query);
		if (!abrs.isEmpty())
			return abrs.get(0);
		return null;
	}

	public Integer _getNbreBulFromRhtcalcul(GeneriqueConnexionService service, String cdos, String aamm, String nbul, String rubNAP)
	{
		String query = "select count(*) from CalculPaie where idEntreprise ='" + cdos + "'";
		query += " and aamm='" + aamm + "'";
		query += " and nbul=" + nbul;
		query += " and rubq='" + rubNAP + "'";

		return new Integer(service.find(query).get(0).toString());
	}

	public Integer _getNbreBulNonEditeFromRhtcalcul(GeneriqueConnexionService service, String cdos, String aamm, String nbul, String rubNAP)
	{
		String query = "Select count(*) from CalculPaie b where b.idEntreprise ='" + cdos + "'";
		query += " and b.aamm='" + aamm + "'";
		query += " and b.nbul=" + nbul;
		query += " and b.rubq='" + rubNAP + "'";
		query += " and b.trtb ='1'";
//		query += " and not exists (";
//		query += " select 'X' from Rhtcalcul a, Rhprubrique c";
//		query += " where a.cdos = b.cdos";
//		query += " and a.aamm = b.aamm";
//		query += " and a.nbul = b.nbul";
//		query += " and a.rubq !='" + rubNAP + "'";
//		query += " and a.cdos = c.cdos and a.rubq = c.crub";
//		query += " and c.prbul != 0 ";
//		query += " and (c.ednul = 'O' or a.mont != 0))";

		ParameterUtil.println(query);

		return new Integer(service.find(query).get(0).toString());
	}

	public Integer _getNbreRubComptaFromRhprubrique(GeneriqueConnexionService service, String cdos)
	{
		String query = "Select count(*) from ElementSalaire  where idEntreprise ='" + cdos + "' and comp='O'";
		return new Integer(service.find(query).get(0).toString());
	}
	

	public List<Salarie> _getListeSalarieNationaliteInexistante(GeneriqueConnexionService service, String cdos, String aamm, String nbul, String rubNAP)
	{
		String query = "From Salarie a where a.idEntreprise ='" + cdos + "'";
		query += " and exists (";
		query += " select 'X' from CalculPaie b";
		query += " where b.idEntreprise = a.idEntreprise";
		query += " and b.aamm = '" + aamm + "'";
		query += " and b.nbul = '" + nbul + "'";
		query += " and b.rubq ='" + rubNAP + "'";
		query += " and b.nmat = a.nmat";
		query += " and not exists (";
		query += " select 'X' from ParamData c";
		query += " where c.idEntreprise = a.idEntreprise";
		query += " and c.cacc = a.nato";
		query += " and c.ctab = 4";
		query += " and c.nume = 1";
		query += " )";
		query += " )";

		return service.find(query);
	}

	public List<Salarie> _getListeSalarieGradeInexistant(GeneriqueConnexionService service, String cdos, String aamm, String nbul, String rubNAP)
	{
		String query = "From Salarie a where a.idEntreprise ='" + cdos + "'";
		query += " and exists (";
		query += " select 'X' from CalculPaie b";
		query += " where b.idEntreprise = a.idEntreprise";
		query += " and b.aamm = '" + aamm + "'";
		query += " and b.nbul = '" + nbul + "'";
		query += " and b.rubq ='" + rubNAP + "'";
		query += " and b.nmat = a.nmat";
		query += " and not exists (";
		query += " select 'X' from ParamData c";
		query += " where c.idEntreprise = a.idEntreprise";
		query += " and c.cacc = a.grad";
		query += " and c.ctab = 6";
		query += " and c.nume = 1";
		query += " )";
		query += " )";

		return service.find(query);
	}

	public void _viderTableCPINT(GeneriqueConnexionService service, String cdos)
	{
		String query = "Delete from InterfComptable where idEntreprise='" + cdos + "'";
		Session sess = service.getSession();
		sess.createQuery(query).executeUpdate();
		service.closeSession(sess);
	}
	
	public void _viderTableCPINTWithPiece(GeneriqueConnexionService service, String cdos, String numpiece)
	{
		String query = "Delete from InterfComptable where idEntreprise='" + cdos + "' and numpce = '"+numpiece+"'";
		Session sess = service.getSession();
		sess.createQuery(query).executeUpdate();
		service.closeSession(sess);
	}
	
	public void _viderTableCPINT(Session session, String cdos)
	{
		String query = "Delete from InterfComptable where idEntreprise='" + cdos + "'";
		session.createQuery(query).executeUpdate();
	}
	
	public void _viderTableCPINTWithPiece(Session session,String cdos, String numpiece)
	{
		String query = "Delete from InterfComptable where idEntreprise='" + cdos + "' and numpce = '"+numpiece+"'";
		session.createQuery(query).executeUpdate();
	}

	public void _updateLibErrTableCPINT(Session session , String cdos)
	{
		//Session session = this.getDaoSession();
		session.createQuery("Update InterfComptable set liberr = null where idEntreprise='"+cdos+"'").executeUpdate();
		session.createQuery("Update MouvCptPaie set iden = null").executeUpdate();
		//this.releaseDaoSession(session);
	}

	public List<Salarie> _getListeSalarieFromRhpagent(GeneriqueConnexionService service, String cdos, String aamm, Integer nbul)
	{
		//return this.find("From Rhpagent where cdos='" + cdos + "' order by nmat");
		String _strQuery = "From Salarie a where a.identreprise = '" + cdos + "' and exists ( "
				+ "select 1 from CalculPaie b where  b.idEntreprise = a.identreprise and a.nmat = b.nmat and b.aamm='" + aamm + "' " + "and b.nbul="
				+ nbul;
		_strQuery += ") order by nmat";
		
		//and a.nmat>='0011' and nmat<='1003'

		System.out.println("requete liste agent dans tcalcul: " + _strQuery);
		List<Salarie> oRhpagent = new ArrayList<Salarie>();
		try
		{
			oRhpagent = service.find(_strQuery);
		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
		return oRhpagent;
	}

	public List<CalculPaie> _getListeBulletinCalculFromRhtcalcul(GeneriqueConnexionService service, String cdos, String aamm, String nbul, String nmat)
	{
		String query = "From CalculPaie where idEntreprise ='" + cdos + "'";
		query += " and aamm='" + aamm + "'";
		query += " and nbul=" + nbul;
		query += " and nmat='" + nmat + "'";
		query += " order by idEntreprise, aamm, nmat, nbul, rubq";

		return service.find(query);
	}

	public ElementSalaire _getInfoRubriqueFromRhprubrique(GeneriqueConnexionService service, String cdos, String crub)
	{
		String query = "From ElementSalaire where idEntreprise ='" + cdos + "' and crub='" + crub + "'";

		List result = service.find(query);
		if(result.isEmpty()) return null;
		else return (ElementSalaire)result.get(0);
	}

	public String _getCodproFromCp_Cpt(GeneriqueConnexionService service, String cdos, String numcompte)
	{
		String query = "From Compte where idEntreprise = '" + cdos + "' and numcpt = '" + numcompte + "'";
		List<Compte> Cp_Cpts = service.find(query);
		if (!Cp_Cpts.isEmpty())
			return Cp_Cpts.get(0).getCodpro();
		return null;
	}

	public String _getNumcptFromCpAux(GeneriqueConnexionService service, String cdos, String numcompte)
	{
		String query = "From Auxiliaire where idEntreprise = '" + cdos + "' and numtie = '" + numcompte + "'";
		List<Auxiliaire> cpaux = service.find(query);
		if (!cpaux.isEmpty())
			return cpaux.get(0).getNumcpt();
		return null;
	}
	
	public Integer _getNbreCpAuxFromCpAux(GeneriqueConnexionService service, String cdos, String numcompte, String numtiers)
	{
		String query = "Select count(*) From Auxiliaire where idEntreprise = '" + cdos + "' and numcpt = '" + numcompte + "' and numtie = '" + numtiers + "'";
		return new Integer(service.find(query).get(0).toString());
	}

	public Produit _getCpProFromCpPro(GeneriqueConnexionService service, String cdos, String codeProfil)
	{
		String query = "From Produit where idEntreprise = '" + cdos + "' and codpro = '" + codeProfil + "'";
		List<Produit> cppros = service.find(query);
		if (!cppros.isEmpty())
			return cppros.get(0);
		return null;
	}

	public String _getTypcFromCp_Cpt(GeneriqueConnexionService service, String cdos, String numcompte)
	{
		String query = "From Compte where idEntreprise = '" + cdos + "' and numcpt = '" + numcompte + "'";
		List<Compte> Cp_Cpts = service.find(query);
		if (!Cp_Cpts.isEmpty())
			return Cp_Cpts.get(0).getTypcpt();
		return null;
	}

	
	public Compte _getCp_Cpt(GeneriqueConnexionService service, String cdos, String numcompte)
	{
		String query = "From Compte where idEntreprise = '" + cdos + "' and numcpt = '" + numcompte + "'";
		List<Compte> Cp_Cpts = service.find(query);
		if (!Cp_Cpts.isEmpty())
			return Cp_Cpts.get(0);
		return null;
	}

	public String _getNcptFromRhtpretEnt(GeneriqueConnexionService service, String cdos, String nprt)
	{
		String query = "From PretExterneEntete where idEntreprise = '" + cdos + "' and nprt = '" + nprt + "'";
		List<PretExterneEntete> Cp_Cpts = service.find(query);
		if (!Cp_Cpts.isEmpty())
			return Cp_Cpts.get(0).getNcpt();
		return null;
	}

	public String _getLibetsFromCpEts(GeneriqueConnexionService service, String cdos, String codeetablissement)
	{
		String query = "From Etablissement where idEntreprise = '" + cdos + "' and codets = '" + codeetablissement + "'";
		List<Etablissement> cpets = service.find(query);
		if (!cpets.isEmpty())
			return cpets.get(0).getLibets();
		return null;
	}

//	public ClsCIDao getCidao()
//	{
//		return cidao;
//	}
//
//	public void setCidao(ClsCIDao cidao)
//	{
//		this.cidao = cidao;
//	}

}
