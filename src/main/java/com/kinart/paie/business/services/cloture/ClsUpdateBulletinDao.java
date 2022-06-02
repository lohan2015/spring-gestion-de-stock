package com.kinart.paie.business.services.cloture;

import java.util.Date;
import java.util.List;

import com.kinart.paie.business.model.*;
import com.kinart.paie.business.services.utils.ClsDate;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.kinart.paie.business.services.utils.ParameterUtil;
import com.kinart.stock.business.model.Utilisateur;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.dao.DataAccessException;

@SuppressWarnings("unchecked")
public class ClsUpdateBulletinDao //extends Dao
{

	//ClsCIDao cidao;
	
	public DossierPaie _getRhpdos(GeneriqueConnexionService service, String dossier)
	{
		List result = service.find("From DossierPaie Where idEntreprise="+dossier);
		if(result.isEmpty()) return null;
		return (DossierPaie)result.get(0);
	}
	
	public String _getLangueUtilisateur(GeneriqueConnexionService service, String cdos, String cuti) throws DataAccessException
	{
		List result = service.find("From Utilisateur Where identreprise="+cdos+" And email='"+cuti+"'");
		if(result.isEmpty()) return null;
		return ((Utilisateur)result.get(0)).getClang();
	}

	public List<ParamData> _getDonneesNomenclature91_99(GeneriqueConnexionService service, String dossier, String cacc_to_add1, String cacc_to_add2)
	{
		String queryString = "From ParamData where idEntreprise = '" + dossier + "'  and ctab in (91, 99) and cacc in('RUBNAP', 'RUBNBJTR', 'CALCPART', 'CGRELIQ', 'NBJPNP', 'EXPAT', 'FICTIF', "
				+ "'NBJC-DEF', 'BASE-CONGE', 'RUBBRUT', 'PROR-JCG', 'PRJ-BARCG', 'ANNIV-CG', 'HC', 'HP', 'MC', 'MP', 'IN','CG-MONT','"+cacc_to_add1+"','"+cacc_to_add2+"')";
		return service.find(queryString);
	}

	public ParamData _chercherEnNomenclature(GeneriqueConnexionService service, String cdos, String ctab, String cacc, Integer nume)
	{
		String queryString = "From ParamData where idEntreprise='"+cdos+"' and ctab="+ctab+" and cacc='"+cacc+"' and nume="+nume.intValue();
		List result = service.find(queryString);
		if(result.isEmpty()) return null;
		return (ParamData)result.get(0);
	}

	public ParamData _chercherEnNomenclature(GeneriqueConnexionService service, String cdos, Integer ctab, String cacc, Integer nume)
	{
		String queryString = "From ParamData where idEntreprise='"+cdos+"' and ctab="+ctab.intValue()+" and cacc='"+cacc+"' and nume="+nume.intValue();
		List result = service.find(queryString);
		if(result.isEmpty()) return null;
		return (ParamData)result.get(0);
	}

	public Integer _getNbreBulFromRhtcalcul(GeneriqueConnexionService service, String cdos, String aamm, Integer nbul, String rubNAP)
	{
		String query = "select count(*) from CalculPaie where idEntreprise ='" + cdos + "'";
		query += " and aamm='" + aamm + "'";
		query += " and nbul=" + nbul;
		query += " and rubq='" + rubNAP + "'";

		return new Integer(service.find(query).get(0).toString());
	}

	public Integer _getNbreBulNonEditeFromRhtcalcul(GeneriqueConnexionService service, String cdos, String aamm, Integer nbul, String rubNAP)
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

		//ParameterUtil.println(query);

		return new Integer(service.find(query).get(0).toString());
	}
	
	public List<Object[]> _chargerT_EV_MC(GeneriqueConnexionService service, String cdos)
	{
		String complexQuery =
		  " select max(case nume when 1 then valm else 0 end)," + " max(case nume when 2 then valm else 0 end),"
		+ " max(case nume when 3 then valm else 0 end)," + " max(case nume when 4 then valm else 0 end),"
		+ " max(case nume when 5 then valm else 0 end)," + " max(case nume when 6 then valm else 0 end),"
		+ " max(case nume when 7 then valm else 0 end)," + " max(case nume when 8 then valm else 0 end),"
		+ " max(case nume when 9 then valm else 0 end)," + " max(case nume when 10 then valm else 0 end),"
		+ " max(case nume when 11 then valm else 0 end)," + " max(case nume when 12 then valm else 0 end),"
		+ " max(case nume when 13 then valm else 0 end)," + " max(case nume when 14 then valm else 0 end),"
		+ " max(case nume when 15 then valm else 0 end)," + " max(case nume when 16 then valm else 0 end)"
		+ " from ParamData where idEntreprise = '"
		+ cdos + "' and ctab = 99 and cacc = 'EV-MC' and nume in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16)";
		return service.find(complexQuery);
	}
	
	public List<Object[]> _chargerT_EV_MS(GeneriqueConnexionService service, String cdos)
	{
		String complexQuery =
		" select max(case nume when 1 then valm else 0 end)," + " max(case nume when 2 then valm else 0 end),"
		+ " max(case nume when 3 then valm else 0 end)," + " max(case nume when 4 then valm else 0 end),"
		+ " max(case nume when 5 then valm else 0 end)," + " max(case nume when 6 then valm else 0 end),"
		+ " max(case nume when 7 then valm else 0 end)," + " max(case nume when 8 then valm else 0 end),"
		+ " max(case nume when 9 then valm else 0 end)," + " max(case nume when 10 then valm else 0 end),"
		+ " max(case nume when 11 then valm else 0 end)," + " max(case nume when 12 then valm else 0 end),"
		+ " max(case nume when 13 then valm else 0 end)," + " max(case nume when 14 then valm else 0 end),"
		+ " max(case nume when 15 then valm else 0 end)," + " max(case nume when 16 then valm else 0 end)"
		+ " from ParamData where idEntreprise = '"
		+ cdos + "' and ctab = 99 and cacc = 'EV-MS' and nume in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16)";
		return service.find(complexQuery);
	}
	
	public List<Object[]> _getEnfantsJoursSup(GeneriqueConnexionService service, String cdos)
	{
		String complexQuery="select max(case nume when 1 then valm else 0 end), max(case nume when 2 then valm else 0 end), "
		+ " max(case nume when 3 then valm else 0 end), max(case nume when 4 then valm else 0 end) from ParamData" + " where idEntreprise = '" + cdos + "'" + " and ctab = 99"
		+ " and cacc = 'ENFANT'" + " and nume in(1, 2, 3, 4)";
		return service.find(complexQuery);
	}
	
	public Integer _countNbColonnesT91(GeneriqueConnexionService service, String cdos)
	{
		String queryString = "select count(*) from ParamColumn" + " where idEntreprise = '" + cdos + "'" + " and ctab = 91 and nume = 1";
		return new Integer(service.find(queryString).get(0).toString());
	}
	
	public List<Object[]> _getPeriodesT91(GeneriqueConnexionService service, String cdos, String cacc)
	{
		String queryString = "select a.cacc, a.vall, b.vall from ParamData a, ParamData b " + " where a.idEntreprise = '" + cdos + "'"
		+ " and a.ctab = 91" + " and (a.cacc >= '" + cacc + "' "
		+ " and a.cacc not in('MC','HC','MP','HP','IN'))" + " and a.nume = 2" + " and b.idEntreprise = a.idEntreprise"
		+ " and b.ctab = a.ctab" + " and b.cacc = a.cacc" + " and b.nume = 3" + " order by a.cacc";
		return service.find(queryString);
	}
	
	public List<ElementSalaire> _getRubriquesRegularisation(GeneriqueConnexionService service, String cdos)
	{
		String queryString ="From ElementSalaire where idEntreprise = '" + cdos + "' and rreg = 'O' and rman = 'N' order by idEntreprise, crub";
		return service.find(queryString);
	}
	
	public List<Salarie> _getListeSalarieFromRhpagent(GeneriqueConnexionService service, String cdos, String dateforamt)
	{
		//On ne prend que les salari�s actif sur l'ann�e
		Date dt = this._getRhpdos(service, cdos).getDdmp();
		String past12Years = new ClsDate(new ClsDate(dt).addYear(-1)).getDateS(dateforamt);
		String query="From Salarie where identreprise='" + cdos + "' ";
		query+=" and (dmrr is null or (dmrr is not null and dmrr>='"+past12Years+"'))";
		query+=" order by nmat";
		return service.find(query);
	}
	
	public List<CalculPaie> _getListeBulletinCalculFromRhtcalcul(GeneriqueConnexionService service, String cdos, String aamm, Integer nbul, String nmat)
	{
		String query = "From CalculPaie  where idEntreprise ='" + cdos + "'";
		query += " and aamm='" + aamm + "'";
		query += " and nbul=" + nbul;
		query += " and nmat='" + nmat + "'";
		query += " order by idEntreprise, aamm, nmat, nbul, rubq";

		return service.find(query);
	}

	public List<CalculPaie> _getListeBulletinCalculFromRhtcalcul(Session oSession,String cdos, String aamm, Integer nbul, String nmat)
	{
		String query = "Select a.* From CalculPaie a where a.idEntreprise ='" + cdos + "'";
		query += " and a.aamm='" + aamm + "'";
		query += " and a.nbul=" + nbul;
		query += " and a.nmat='" + nmat + "'";
		query += " order by idEntreprise, aamm, nmat, nbul, rubq";
		
		Query q = oSession.createSQLQuery(query).addEntity("a", CalculPaie.class);
		ParameterUtil.println("Debut Chargement elements de calcul du mois  query = "+query);
		List<CalculPaie> lst = q.list();
		ParameterUtil.println("Fin Chargement elements de calcul du mois  query = "+query);

		return lst;
	}

	public ElementSalaire _getInfoRubriqueFromRhprubrique(GeneriqueConnexionService service, String cdos, String crub)
	{
		String queryString = "From ElementSalaire where idEntreprise='"+cdos+"' and crub='"+crub+"'";
		List result = service.find(queryString);
		if(result.isEmpty()) return null;
		return (ElementSalaire)result.get(0);
	}
	
	public Object[] _getRhfnomMotifConges(GeneriqueConnexionService service, String cdos, String cacc)
	{
		String queryString = "select max(case nume when 1 then valm else 0 end), " + " max(case nume when 3 then valm else 0 end) from ParamData" + " where idEntreprise = '" + cdos + "'" + " and ctab = 22" + " and cacc = '" + cacc + "'" + " and nume in(1, 3)";
		return (Object[]) service.find(queryString).get(0);
	}
	
	public List<Object[]> _chargerEVMoisCourant(GeneriqueConnexionService service, String cdos, String aamm, String nmat, Integer nbul)
	{	
		String queryString = "select a, b from ElementVariableConge a, ElementVariableEnteteMois b where b.idEntreprise = '" + cdos + "'" + " and b.aamm = '" + aamm + "'" + " and b.nmat = '" + nmat + "'" +
		" and b.nbul = " + nbul + " and a.cdos = b.cdos and a.aamm = b.aamm" + " and a.nmat = b.nmat" + " and a.nbul = b.nbul";
		
		return (List<Object[]>) service.find(queryString);
	}
	
	public List<Object[]> _chargerEVMoisCourant(Session oSession,String cdos, String aamm, String nmat, Integer nbul)
	{	
		String query = "select a.*, b.* from ElementVariableConge a, ElementVariableEnteteMois b where b.idEntreprise = '" + cdos + "'" + " and b.aamm = '" + aamm + "'" + " and b.nmat = '" + nmat + "'" +
		" and b.nbul = " + nbul + " and a.idEntreprise = b.idEntreprise and a.aamm = b.aamm" + " and a.nmat = b.nmat" + " and a.nbul = b.nbul";
		Query q = oSession.createSQLQuery(query).addEntity("a", ElementVariableConge.class).addEntity("b", ElementVariableEnteteMois.class);
		ParameterUtil.println("Debut Chargement evconge du mois  query = "+query);
		List<Object[]>lst = q.list();
		ParameterUtil.println("Fin Chargement evconge du mois  query = "+query);
		return lst;
	}
	
	public List<CumulPaie> _getCumulsToDelete(GeneriqueConnexionService service, String cdos, String aamm, Integer nbul,String nmat)
	{
		String queryString = "From CumulPaie cumul where exists (Select 'X' from CalculPaie calcul" + " where calcul.idEntreprise = cumul.idEntreprise" + " and calcul.aamm = cumul.aamm" + " and calcul.nmat = cumul.nmat" + " and calcul.nbul = cumul.nbul )"
				+ " and  cumul.idEntreprise = '" + cdos + "'" + " and cumul.aamm = '" + aamm + "'" + " and cumul.nmat = '" + nmat + "'" + " and cumul.nbul = " + nbul;
		ParameterUtil.println("Begin Executing "+queryString);
		List<CumulPaie> liste = service.find(queryString);
		ParameterUtil.println("End Executing "+queryString);
		return  liste;
	}

	
	public List<CumulPaie> _getCumulsToDelete(Session oSession,String cdos, String aamm, Integer nbul,String nmat)
	{
		String query =" Select cumul.* From CumulPaie cumul where exists (Select 'X' from CalculPaie calcul" + " where calcul.idEntreprise = cumul.idEntreprise" + " and calcul.aamm = cumul.aamm" + " and calcul.nmat = cumul.nmat" + " and calcul.nbul = cumul.nbul )"
		+ " and  cumul.idEntreprise = '" + cdos + "'" + " and cumul.aamm = '" + aamm + "'" + " and cumul.nmat = '" + nmat + "'" + " and cumul.nbul = " + nbul;
		Query q = oSession.createSQLQuery(query).addEntity("cumul", CumulPaie.class);
		ParameterUtil.println("Debut Chargement cumuls  query = "+query);
		List<CumulPaie> liste = q.list();
		ParameterUtil.println("Fin Chargement cumuls  query = "+query);
		return liste;
	}
	

	
	public List<CumulPaie> _getCumulsToDelete99(GeneriqueConnexionService service, String cdos, String aamm,String aamm99, Integer nbul,String nmat)
	{
		String queryString = "From CumulPaie cumul where exists (Select 'X' from CalculPaie calcul" + " where calcul.aamm = '" + aamm + "'" + " and calcul.nbul = " + nbul+ " and calcul.idEntreprise = cumul.idEntreprise" + " and calcul.nmat = cumul.nmat )"
				+ " and cumul.idEntreprise = '" + cdos + "'" + " and cumul.aamm = '" + aamm99 + "'" + " and cumul.nmat = '" + nmat + "'" + " and cumul.nbul = " + 9;
		return service.find(queryString);
	}
	
	public List<CumulPaie> _getCumulsToDelete99(Session oSession,String cdos, String aamm,String aamm99, Integer nbul,String nmat)
	{
		String query =" Select cumul.* From CumulPaie cumul where exists (Select 'X' from CalculPaie calcul" + " where calcul.aamm = '" + aamm + "'" + " and calcul.nbul = " + nbul+ " and calcul.idEntreprise = cumul.idEntreprise" + " and calcul.nmat = cumul.nmat )"
				+ " and cumul.idEntreprise = '" + cdos + "'" + " and cumul.aamm = '" + aamm99 + "'" + " and cumul.nmat = '" + nmat + "'" + " and cumul.nbul = " + 9;
		Query q = oSession.createSQLQuery(query).addEntity("cumul", CumulPaie.class);
		ParameterUtil.println("Debut Chargement cumuls 99  query = "+query);
		List<CumulPaie> liste = q.list();
		ParameterUtil.println("Fin Chargement cumuls 99  query = "+query);
		return liste;
	}
	
	public void _deleteCumulsWithoutCommit(Session session,String cdos, String aamm, Integer nbul,String nmat)
	{
		String queryString = "From CumulPaie cumul where exists (Select 'X' from CalculPaie calcul" + " where calcul.idEntreprise = cumul.idEntreprise" + " and calcul.aamm = cumul.aamm" + " and calcul.nmat = cumul.nmat" + " and calcul.nbul = cumul.nbul )"
				+ " and  cumul.idEntreprise = '" + cdos + "'" + " and cumul.aamm = '" + aamm + "'" + " and cumul.nmat = '" + nmat + "'" + " and cumul.nbul = " + nbul;
		session.createQuery("Delete " + queryString).executeUpdate();
	}
	
	public void _deleteCumuls99WithoutCommit(Session session, String cdos, String aamm,String aamm99, Integer nbul,String nmat)
	{
		String queryString = "From CumulPaie cumul where exists (Select 'X' from CalculPaie calcul" + " where calcul.aamm = '" + aamm + "'" + " and calcul.nbul = " + nbul+ " and calcul.idEntreprise = cumul.idEntreprise" + " and calcul.nmat = cumul.nmat )"
				+ " and cumul.idEntreprise = '" + cdos + "'"+ " and cumul.nmat = '" + nmat + "'"  + " and cumul.aamm = '" + aamm99 + "'" + " and cumul.nbul = " + 9;
		session.createQuery("Delete " + queryString).executeUpdate();
	}
	
	public void _supp_fictif(Session session, String cdos, String nmat, Integer nbul)
	{
		session.createQuery("delete from CongeFictif" + " where idEntreprise = '" + cdos+ "'" + " and nmat = '" + nmat + "'" + " and nbul = " + nbul).executeUpdate();
	}
	
	public Integer _countNbJourReliquat(GeneriqueConnexionService service, String cdos,String dateDebut,String dateFin)
	{
		return new Integer(service.find("select count(*) from CalendrierPaie" + " where idEntreprise = '" + cdos + "'" + " and jour between '" + dateDebut+ "' and '" + dateFin +"'").get(0).toString());
	}
	
	public Integer _updatePrets17(Session session, String cdos, String nmat) throws Exception
	{
		String query = "update PretExterneEntete" + " set pact = 'N'" + " where idEntreprise = '" + cdos + "'" + " and nmat = '" + nmat + "'";
		return session.createQuery(query).executeUpdate();
	}
	
	public Object[] _getRhfnomCalcPartFiscal(GeneriqueConnexionService service, String cdos, String cacc)
	{
		String queryString = "select max(case nume when 3 then valm else 0 end)" + ", max(case nume when 1 then valt else 0 end)" + ", max(case nume when 2 then valt else 0 end)" + " from ParamData" + " where idEntreprise = '" + cdos + "'" + " and ctab = 35" + " and cacc = '"
		+ cacc+ "'" + " and nume in (1, 2, 3)";
		return (Object[]) service.find(queryString).get(0);
	}
	
	public Integer _getNbEnfantsSalarie(GeneriqueConnexionService service, String cdos, String nmat, String dtna)
	{
		String query="select count(*) from EnfantSalarie" + " where idEntreprise = '" + cdos + "'" + " and nmat = '" + nmat + "'" + " and achg = 'O'" + " and dtna > '" + dtna + "'";
		return new Integer(service.find(query).get(0).toString());
	}
	
	public double _getNbJrsDecoration(GeneriqueConnexionService service, String cdos, String nmat)
	{
		String query="select sum(valt) from ParamData where idEntreprise = '" + cdos + "'" + " and ctab = 18 and nume = 1 and cacc in( select distinct cdis from DistinctionSalarie" + " where idEntreprise = '" + cdos + "'" + " and nmat = '"
			+ nmat + "')";
		Object obj = service.find(query).get(0);
		if(obj == null)
			return 0;
		return new Double(obj.toString());
	}
	
	public List _getDtnaEnfantsSalarie(GeneriqueConnexionService service, String cdos,String nmat)
	{
		String query="select dtna from EnfantSalarie" + " where idEntreprise = '" + cdos + "'" + " and nmat = '" + nmat + "'";
		return service.find(query);
	}
	
	public Object[] _getRhfnomNbParts(GeneriqueConnexionService service, String cdos, String cacc)
	{
		String query="select max(case nume when 1 then valt else 0 end)" + ", max(case nume when 2 then valt else 0 end)" + ", max(case nume when 3 then valt else 0 end)" + ", max(case nume when 4 then valt else 0 end)" + ", max(case nume when 5 then valt else 0 end)"
						+ ", max(case nume when 6 then valt else 0 end)" + ", max(case nume when 7 then valt else 0 end)" + ", max(case nume when 8 then valt else 0 end)" + " from ParamData" + " where idEntreprise = '" + cdos + "'" + " and ctab = 55" + " and cacc = '"
						+ cacc + "'" + " and nume in (1, 2, 3, 4, 5, 6, 7, 8)";
		return (Object[]) service.find(query).get(0);
	}
	
	public Integer _computeNbjrAbsences(GeneriqueConnexionService service, String cdos, String jourdebut,String jourfin)
	{
		return new Integer(service.find("select count(*) from CalendrierPaie" + " where idEntreprise = '" + cdos + "'" + " and jour >= '" + jourdebut + "'" + " and jour <= '" + jourfin + "'").get(0).toString());
	}
	
	public Integer _computeNbjrConges(GeneriqueConnexionService service, String cdos, String jourdebut,String jourfin)
	{
		return new Integer(service.find("select count(*) from CalendrierPaie" + " where idEntreprise = '" + cdos + "'" + " and jour >= '" + jourdebut + "'" + " and jour <= '" + jourfin + "' and ouvr = 'O'" + " and fer = 'N'").get(0).toString());
	}
	
	public void _updateDossier(Session oSession, String cdos, String aamm, Integer nbul)
	{
		oSession.createQuery("delete from CalculPaie" + " where idEntreprise = '" + cdos + "'" + " and aamm = '" + aamm + "'" + " and nbul = " + nbul).executeUpdate();

		oSession.createQuery("delete from ElementVariableEnteteMois" + " where idEntreprise = '" + cdos + "'" + " and aamm = '" + aamm + "'" + " and nbul = " + nbul).executeUpdate();

		oSession.createQuery("delete from ElementVariableDetailMois" + " where idEntreprise = '" + cdos + "'" + " and aamm = '" + aamm + "'" + " and nbul = " + nbul).executeUpdate();

		oSession.createQuery("delete from ElementVariableConge" + " where idEntreprise = '" + cdos + "'" + " and aamm = '" + aamm + "'" + " and nbul = " + nbul).executeUpdate();

		oSession.createQuery("delete from LogMessage" + " where idEntreprise = '" + cdos + "'").executeUpdate();

		//oSession.createQuery("delete from Rhtblq" + " where idEntreprise = '" + cdos + "'").executeUpdate();
	}
	
	public List<PretExterneEntete> _getPretsToTransfert(GeneriqueConnexionService service, String cdos, String nmat, String perb1)
	{
		String queryString = "From PretExterneEntete" + " where idEntreprise = '" + cdos + "'" + " and nmat = '" + nmat + "'" + " and dpec <= '" + perb1 + "'" + " and resr > 0" + " and (tint = 0 or tint is null)" + " and pact = 'O'";
		ParameterUtil.println("Debut Chargement query = "+queryString);
		List<PretExterneEntete> liste = service.find(queryString);
		ParameterUtil.println("Fin Chargement query = "+queryString);
		return liste;
	}
	
	public List<PretExterneEntete> _getPretsToTransfert(Session oSession,String cdos, String nmat, Date perb1)
	{
		String query =" Select a.* From PretExterneEntete a where a.idEntreprise = '"+cdos+"' and a.nmat = '"+nmat+"' and a.dpec <= :per1 and resr > 0" + " and (tint = 0 or tint is null)" + " and pact = 'O'";
		Query q = oSession.createSQLQuery(query).addEntity("a", PretExterneEntete.class);
		q.setParameter("per1", perb1, StandardBasicTypes.DATE);
		ParameterUtil.println("Debut Chargement prets � transferer  query = "+query);
		List<PretExterneEntete> liste = q.list();
		ParameterUtil.println("Fin Chargement prets � transferer query = "+query);
		return liste;
	}
	
	public Object _getMaxPerbFromPret(GeneriqueConnexionService service, String cdos, String nprt)
	{
		List liste = service.find("select max(perb) from PretExterneDetail" + " where idEntreprise = '" + cdos + "'" + " and nprt = '" + nprt + "'");
		if(liste.size()>0)
			return liste.get(0);
		return null;
	}
	
	public boolean _transferEVMoisSuivant(Session oSession,String cdos,String aamm,Integer nbul,String mois_ms, Integer nbul_ms,String nmat, String date_debut_ms, String date_fin_ms, String cuti)
	{
		try
		{
			String query="Insert Into ElementVariableEnteteMois (idEntreprise,aamm,nmat, nbul ,ddpa, dfpa,bcmo)"+
						 "Select "+ 
						 "eltvardet.idEntreprise as idEntreprise,'"+mois_ms+"' as aamm,eltvardet.nmat as nmat,'"+nbul_ms+"' as nbul,'"+date_debut_ms+"' as ddpa,'"+date_fin_ms+"' as dfpa,'N' as bcmo "+
						 "From "+ 
						 "ElementVariableDetailMois eltvardet,ElementVariableEnteteMois eltvarent "+
						 "Where	eltvarent.idEntreprise = '"+cdos+"' "+
						 "	   	And eltvarent.nmat = '"+nmat+"' "+
						 "	   	And eltvarent.aamm = '"+aamm+"' "+
						 "	   	And eltvarent.nbul = '"+nbul+"' "+
						 "	   	And eltvardet.idEntreprise = eltvarent.idEntreprise "+
						 "	   	And eltvardet.aamm = eltvarent.aamm "+
						 "	   	And eltvardet.nmat = eltvarent.nmat "+
						 "	   	And eltvardet.nbul = eltvarent.nbul "+
						 "	   	And "+ 
						 "	   	(eltvardet.cdos,eltvardet.nmat,eltvardet.aamm,eltvardet.nbul) not in "+
						 "	   	  ( "+
						 "	   		Select cdos, nmat, aamm, nbul "+
						 "	   	 	From ElementVariableEnteteMois "+
						 "	   	 	where idEntreprise = '"+cdos+"' and nmat = '"+nmat+"' "+ 
						 "	   	 	and aamm = '"+mois_ms+"' and nbul = '"+nbul_ms+"'"+
						 "	   	  ) ";
			System.out.println(query);
			  int nbRhteltvarent = oSession.createSQLQuery(query).executeUpdate();
			  
			  query="Insert Into ElementVariableDetailMois (idEntreprise,aamm,nmat, nbul ,rubq, argu, nprt,ruba,mont, cuti) "+
					"Select "+ 
					"eltvardet.idEntreprise,'"+mois_ms+"' as aamm,eltvardet.nmat,'"+nbul_ms+"' as nbul,eltvardet.rubq, "+
					"eltvardet.argu,eltvardet.nprt,eltvardet.ruba,eltvardet.mont,coalesce(eltvardet.cuti,'"+cuti+"') as cuti "+
					"From "+ 
					"ElementVariableDetailMois eltvardet,ElementVariableEnteteMois eltvarent "+
					"Where	eltvarent.idEntreprise = '"+cdos+"'"+
					"	   	And eltvarent.nmat = '"+nmat+"' "+
					"	   	And eltvarent.aamm = '"+aamm+"' "+
					"	   	And eltvarent.nbul = '"+nbul+"' "+
					"	   	And eltvardet.idEntreprise = eltvarent.idEntreprise "+
					"	   	And eltvardet.aamm = eltvarent.aamm "+
					"	   	And eltvardet.nmat = eltvarent.nmat "+
					"	   	And eltvardet.nbul = eltvarent.nbul ";
			  System.out.println(query);
			  int nbRhteltvardet = oSession.createSQLQuery(query).executeUpdate();
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public List<Object[]> _chargerEVEnteteEtDetail(GeneriqueConnexionService service, String cdos, String aamm, String nmat, Integer nbul)
	{	
		String queryString = "select a,b From ElementVariableDetailMois a, ElementVariableEnteteMois b"
		+ " where b.idEntreprise = '" + cdos + "'" + " and b.aamm = '" +aamm + "'" + " and b.nmat = '" + nmat + "'" + " and b.nbul = " + nbul+ " and a.idEntreprise = b.idEntreprise" + " and a.aamm = b.aamm"
		+ " and a.nmat = b.nmat" + " and a.nbul = b.nbul";
		
		return (List<Object[]>) service.find(queryString);
	}
	
	public List<Object[]> _chargerEVEnteteEtDetail(Session oSession,String cdos, String aamm, String nmat, Integer nbul)
	{	
		String queryString = "select a.*,b.* From ElementVariableDetailMois a, ElementVariableEnteteMois b"
		+ " where b.idEntreprise = '" + cdos + "'" + " and b.aamm = '" +aamm + "'" + " and b.nmat = '" + nmat + "'" + " and b.nbul = " + nbul+ " and a.idEntreprise = b.idEntreprise" + " and a.aamm = b.aamm"
		+ " and a.nmat = b.nmat" + " and a.nbul = b.nbul";
		
		Query q = oSession.createSQLQuery(queryString).addEntity("a", ElementVariableDetailMois.class).addEntity("b", ElementVariableEnteteMois.class);
		List<Object[]> liste = q.list();
		
		return liste;
	}
	
	public List<HistoCongeSalarie> _chargerMaxRhtCongeAgent(Session oSession,String cdos, String nmat, String motf)
	{	
		String queryString = "select a.* From HistoCongeSalarie a  where a.idEntreprise = '" + cdos + "' and a.nmat = '" + nmat + "'" + " and a.cmcg = '" + motf+ "'";
		queryString+=" and a.ddcg=(select max(b.ddcg) from HistoCongeSalarie b where a.idEntreprise=b.idEntreprise and a.nmat=b.nmat and a.cmcg=b.cmcg) ";
		
		Query q = oSession.createSQLQuery(queryString).addEntity("a", HistoCongeSalarie.class);
		List<HistoCongeSalarie> liste = q.list();
		return liste;
	}
	
	public boolean _deleteEVMois(Session oSession,String cdos, String aamm, Integer nbul, String nmat)
	{
		try
		{
			String deleteQuery = "delete from ElementVariableDetailMois" + " where idEntreprise = '" + cdos + "'" + " and nmat = '" + nmat + "'" + " and aamm = '" + aamm + "'" + " and nbul = " + nbul;
			//System.out.println("---------------->Delete query = "+deleteQuery);
			oSession.
			createQuery(deleteQuery).
			executeUpdate();

			deleteQuery = "delete from ElementVariableConge" + " where idEntreprise = '" + cdos + "'" + " and nmat = '" + nmat + "'" + " and aamm = '" + aamm + "'" + " and nbul = " + nbul;
			//System.out.println("---------------->Delete query = "+deleteQuery);
			oSession.
			createQuery(deleteQuery).
			executeUpdate();

			deleteQuery = "delete from ElementVariableEnteteMois" + " where idEntreprise = '" + cdos + "'" + " and nmat = '" + nmat + "'" + " and aamm = '" + aamm + "'" + " and nbul = " + nbul;
			//System.out.println("---------------->Delete query = "+deleteQuery);
			oSession.
			createQuery(deleteQuery).
			executeUpdate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean _deleteEFMois(Session oSession,String cdos,String datefin,String nmat) throws Exception
	{
		try
		{
			oSession.createQuery("delete from ElementFixeSalaire" + " where idEntreprise = '" + cdos + "'" + " and nmat = '" + nmat + "'" + " and dfin is not null" + " and dfin <= '" + datefin + "'").executeUpdate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
			//return false;
		}
		return true;
	}
//
//	public ClsCIDao getCidao()
//	{
//		return cidao;
//	}
//
//	public void setCidao(ClsCIDao cidao)
//	{
//		this.cidao = cidao;
//	}

//	public GeneriqueConnexionService getService() {
//		return service;
//	}
//
//	public void setService(GeneriqueConnexionService service) {
//		this.service = service;
//	}
}
