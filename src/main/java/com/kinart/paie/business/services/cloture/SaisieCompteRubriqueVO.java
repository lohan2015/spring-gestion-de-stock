package com.kinart.paie.business.services.cloture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kinart.paie.business.model.*;
import com.kinart.paie.business.services.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;

public class SaisieCompteRubriqueVO extends ElementSalaireCompte {
	boolean selected = false;

	//Mode mode;

	private String lrub;
	private String lsens;

	private String lcritere1;
	private String lcritere2;
	private String lcritere3;

	private boolean hasbeenmodify = false; // permet de savoir si le vo a subit une modification

	int tabindex = 0;

	public boolean isSelected()
	{
		return selected;
	}

	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}

	public SaisieCompteRubriqueVO copie = null;

	public void creerCopie()
	{
		try
		{
			copie = new SaisieCompteRubriqueVO();
			BeanUtils.copyProperties(this, copie);
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
	}

	public void annulerModification()
	{
		BeanUtils.copyProperties(copie, this);
		copie = null;
	}

	public static SaisieCompteRubriqueVO convertToBusinness(ElementSalaireCompte vo)
	{
		SaisieCompteRubriqueVO ev = new SaisieCompteRubriqueVO();
		BeanUtils.copyProperties(vo, ev);
		return ev;
	}

	public static ElementSalaireCompte convertToEntity(SaisieCompteRubriqueVO vo)
	{
		ElementSalaireCompte ev = new ElementSalaireCompte();
		BeanUtils.copyProperties(vo, ev);
		return ev;
	}

	public SaisieCompteRubriqueVO(String cdos, String crub, Integer nlig, String sens, String critere1, String critere2, String critere3, String compte)
	{
		//super(new ElementSalaireComptePK(cdos, crub, nlig), sens, critere1, critere2, critere3, compte);
		super();
		this.setIdEntreprise(new Integer(cdos));
		this.setCrub(crub);
		this.setSens(sens);
		this.setCritere1(critere1);
		this.setCritere2(critere2);
		this.setCritere3(critere3);
		this.setCompte(compte);
		//this.setMode(Mode.CONSULTATION);
		this.creerCopie();
	}

	public SaisieCompteRubriqueVO()
	{
		super();
	}

	private static String racine = "CPTRUB-CT";

	public static ClsResultat initTables(ClsRechercheCompteRubriqueVO search)
	{

		ClsResultat result = null;
		String query = "From ParamData where idEntreprise = '" + search.cdos
				+ "' and ctab = 99 and nume in (1,2,3) and cacc like 'CPTRUB-CT%' Order by cacc,nume ";
		List<ParamData> lst = search.service.find(query);
		Integer i = 0;
		String cdmes;
		Message msg = null;
		try
		{
			for (ParamData nome : lst)
			{
				i = Integer.valueOf(StringUtil.lastCharacter(nome.getCacc())) - 1;
				if (i > search.nbrCriteres - 1)
					break;
				if (1 == nome.getNume())
				{
					search.colCriteres[i] = StringUtil.nvl(nome.getVall(), StringUtils.EMPTY);
					search.ctabsAgent[i] = NumberUtils.nvl(nome.getValm(), 0).intValue();
				}

				if (2 == nome.getNume())
				{
					search.colsNome[i] = StringUtil.nvl(nome.getVall(), StringUtils.EMPTY);
					search.numeNome[i] = NumberUtils.nvl(nome.getValm(), 0).intValue();
				}

				if (3 == nome.getNume())
				{
					search.ctabs[i] = NumberUtils.nvl(nome.getValm(), 0).intValue();
					cdmes = nome.getVall();
					if(StringUtils.isNotBlank(cdmes))
					{
						List<Message> result2 = search.service.find("From Message Where cdmes='"+cdmes+"' And clang='"+search.clang+"'");
						if(result2!= null && !result2.isEmpty()) msg = result2.get(0);
						//msg = (Evmsg)search.service.get(Evmsg.class, new EvmsgPK(cdmes,search.clang));
						if(msg != null) cdmes = msg.getLbmes();
					}
					search.titres[i] = StringUtil.nvl(cdmes, "Crit�re " + (i + 1));
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			result = ClsTreater._getResultat(ClsTreater._getStackTrace(e), null, true);
			return result;
		}

		// le crit�re 1 ne peut pas �tre vide
		if (StringUtils.isBlank(search.colCriteres[0]))
			result = ClsTreater._getResultat("Creer Param " + racine + "1, T.99, Lib2", null, true);

		return result;
	}

	private static final String remplacement = "X";

	public static List<SaisieCompteRubriqueVO> buildListOfDataToDisplay(ClsRechercheCompteRubriqueVO search)
	{
		List listOfDataToDisplay = new ArrayList<SaisieCompteRubriqueVO>();

		String queryString = getQueryString(search);

		try
		{
			Session session = search.service.getSession();

			Query q = session.createSQLQuery(queryString).addEntity("a", ElementSalaireCompte.class).addScalar("lrub", StandardBasicTypes.STRING).addScalar("lcritere1", StandardBasicTypes.STRING).addScalar("lcritere2",
					StandardBasicTypes.STRING).addScalar("lcritere3", StandardBasicTypes.STRING);
			SaisieCompteRubriqueVO.setQueryParameters(q, search);

			List<Object[]> liste = q.list();

			SaisieCompteRubriqueVO vo = null;

			ElementSalaireCompte poste;

			Integer index = 0;
			for (Object[] o : liste)
			{
				index = 0;
				vo = new SaisieCompteRubriqueVO();
				if (o[index] != null)
				{
					poste = (ElementSalaireCompte) o[index++];
					BeanUtils.copyProperties(poste, vo);
				}

				if (o[index] != null)
					vo.setLrub((String) o[index++]);
				if (o[index] != null)
					vo.setLcritere1((String) o[index++]);
				if (o[index] != null)
					vo.setLcritere2((String) o[index++]);
				if (o[index] != null)
					vo.setLcritere3((String) o[index++]);
				if (StringUtils.equalsIgnoreCase(remplacement, vo.getLcritere1()))
					vo.setLcritere1(StringUtils.EMPTY);
				if (StringUtils.equalsIgnoreCase(remplacement, vo.getLcritere2()))
					vo.setLcritere2(StringUtils.EMPTY);
				if (StringUtils.equalsIgnoreCase(remplacement, vo.getLcritere3()))
					vo.setLcritere3(StringUtils.EMPTY);

				if (StringUtils.equals("C", vo.getSens()))
					vo.setLsens("Cr�dit");
				else vo.setLsens("D�bit");

				//vo.setMode(Mode.CONSULTATION);
				listOfDataToDisplay.add(vo);

			}
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
		}
		catch (BeansException e)
		{
			e.printStackTrace();
		}

		return listOfDataToDisplay;
	}

	private static String getQueryString(ClsRechercheCompteRubriqueVO search)
	{
		String select = " Select a.*, b.lrub as lrub ";
		String from = " From ElementSalaireCompte a ";
		from += "Left Join Rhprubrique b on a.cdos=b.cdos and a.crub = b.crub ";
		int j = 0;
		for (int i = 0; i < search.nbrCriteres; i++)
		{
			j = i + 1;
			if (search.ctabs[i] != 0)
			{
				select += ", b" + j + ".vall as lcritere" + j;
				from += " Left Join Rhfnom b" + j + " on a.cdos=b" + j + ".cdos and a.critere" + j + "=b" + j + ".cacc and b" + j + ".nume = 1 and b" + j + ".ctab = " + search.ctabs[i];
			}
			else
			{
				select += ", '" + remplacement + "' as lcritere" + j;
			}
		}

		String where = " Where a.cdos = :cdos ";

		if (StringUtils.isNotBlank(search.getCrub()))
			where += "and UPPER(a.crub) like :crub ";
		if (StringUtils.isNotBlank(search.getSens()))
			where += "and UPPER(a.sens) like :sens ";
		if (StringUtils.isNotBlank(search.getCritere1()))
			where += "and UPPER(a.critere1) like :critere1 ";
		if (StringUtils.isNotBlank(search.getCritere2()))
			where += "and UPPER(a.critere2) like :critere2 ";
		if (StringUtils.isNotBlank(search.getCritere3()))
			where += "and UPPER(a.critere3) like :critere3 ";

		where += " order by a.crub,a.sens,a.critere1,a.critere2,a.critere3 ";

		return select + from + where;
	}

	private static void setQueryParameters(Query query, ClsRechercheCompteRubriqueVO search)
	{
		query.setParameter("cdos", search.cdos, StandardBasicTypes.STRING);

		if (StringUtils.isNotBlank(search.getCrub()))
			query.setParameter("crub", "%" + search.getCrub().toUpperCase() + "%", StandardBasicTypes.STRING);
		if (StringUtils.isNotBlank(search.getSens()))
			query.setParameter("sens", "%" + search.getSens().toUpperCase() + "%", StandardBasicTypes.STRING);
		if (StringUtils.isNotBlank(search.getCritere1()))
			query.setParameter("critere1", "%" + search.getCritere1().toUpperCase() + "%", StandardBasicTypes.STRING);

		if (StringUtils.isNotBlank(search.getCritere2()))
			query.setParameter("critere2", "%" + search.getCritere2().toUpperCase() + "%", StandardBasicTypes.STRING);

		if (StringUtils.isNotBlank(search.getCritere3()))
			query.setParameter("critere3", "%" + search.getCritere3().toUpperCase() + "%", StandardBasicTypes.STRING);

	}

	public static ClsResultat supprimerDonnee(ClsRechercheCompteRubriqueVO search, List<SaisieCompteRubriqueVO> liste, List<SaisieCompteRubriqueVO> listeAAfficher)
	{
		ClsResultat oResult = null;
		Session session = search.service.getSession();

		Transaction tx = null;
		try
		{
			tx = session.beginTransaction();

			for (SaisieCompteRubriqueVO vo : liste)
			{
				session.delete(SaisieCompteRubriqueVO.convertToEntity(vo));
				listeAAfficher.remove(vo);
			}

			tx.commit();

		}
		catch (Exception e)
		{
			e.printStackTrace();
			oResult = ClsTreater._getResultat(ClsTreater._getStackTrace(e), null, true);
			if (tx != null)
				tx.rollback();
		}
		finally
		{
			search.service.closeSession(session);
		}

		return oResult;
	}

	public static String controllerValeursNulles(ClsRechercheCompteRubriqueVO search, SaisieCompteRubriqueVO vo)
	{
		String _strMessage = "";

		if (StringUtils.isBlank(vo.getCrub()))
			_strMessage += "<br/>" +ClsTreater._getMessage("Saisir la valeur de la rubrique","ERR-70-AF-000");
		else
		{
			List<ElementSalaire> result = search.service.find("From ElementSalaire Where idEntreprise="+search.cdos+" And crub='"+vo.getCrub()+"'");
			ElementSalaire rub = null;//(Rhprubrique) search.service.get(Rhprubrique.class, new RhprubriquePK(search.cdos, vo.getCrub()));
			if(result!=null && !result.isEmpty()) rub=result.get(0);
			if (rub == null)
				_strMessage += "<br/>" +ClsTreater._getParametizedMessageValue(ClsTreater._getMessage("Rubrique %1 inconnue. ","INF-00454"),rub.getCrub());		
			else if (StringUtil.notEquals("O", rub.getComp()))
				_strMessage += "<br/>" +ClsTreater._getMessage("Rubrique non comptabilisee","ERR-10008") +"(" + rub.getCrub() + ")";
			
		}

		if (StringUtils.isBlank(vo.getSens()))
			_strMessage += "<br/>" +ClsTreater._getMessage("Saisir la valeur du sens ","ERR-70-AF-001");
		else
		{
			if (StringUtil.notIn(vo.getSens(), "C,D"))
				_strMessage += "<br/>" +ClsTreater._getMessage("Valeurs possible du sens : C ou D","ERR-70-AF-002") ;
		}

//		if (StringUtils.isBlank(vo.getCritere1()) && StringUtils.isBlank(vo.getCritere2()) && StringUtils.isBlank(vo.getCritere3()))
//		{
//			_strMessage += "<br/>" + "Renseigner au moins un crit�re";
//		}
//		else
//		{
		

			if (!StringUtils.isBlank(vo.getCritere1()))
			{
				if (search.ctabs[0] != null)
				{
					ParamData fnom = search.service.findAnyColumnFromNomenclature(search.cdos, "", search.ctabs[0].intValue()+"", vo.getCritere1(), "1");
					//(Rhfnom) search.service.get(Rhfnom.class, new RhfnomPK(search.cdos, search.ctabs[0], vo.getCritere1(), 1));
					if (fnom == null)
					{
						String libelle = "(" + ClsTreater._getResultat("Table", "INF-01059", true).getLibelle() + " " + search.ctabs[0];
						List<ParamTable> tnoms = search.service.find("From ParamTable Where idEntreprise="+search.cdos+" And ctab="+search.ctabs[0].intValue());
						ParamTable tnom = null;
						if(tnoms!=null && !tnoms.isEmpty()) tnom = tnoms.get(0);
						//(Rhtnom) search.service.get(Rhtnom.class, new RhtnomPK(search.cdos, search.ctabs[0]));
						if (tnom != null)
							libelle += " - " + tnom.getLibe();
						libelle += ")";
						_strMessage += "<br/>" + ClsTreater._getResultat("La donn�e % n'existe pas", "ERR-80135", true, vo.getCritere1()).getLibelle()+libelle;
						//_strMessage += "<br/>" + "La valeur " + vo.getCritere1() + " n'existe pas en table " + search.ctabs[0];
					}
				}
			}

			if (!StringUtils.isBlank(vo.getCritere2()))
			{
			
				if (search.ctabs[1] != null)
				{
					ParamData fnom = search.service.findAnyColumnFromNomenclature(search.cdos, "", search.ctabs[1].intValue()+"", vo.getCritere2(), "1");
					//Rhfnom fnom = (Rhfnom) search.service.get(Rhfnom.class, new RhfnomPK(search.cdos, search.ctabs[1], vo.getCritere2(), 1));
					if (fnom == null)
					{
						String libelle = "(" + ClsTreater._getResultat("Table", "INF-01059", true).getLibelle() + " " + search.ctabs[1];
						List<ParamTable> tnoms = search.service.find("From ParamTable Where idEntreprise="+search.cdos+" And ctab="+search.ctabs[1].intValue());
						ParamTable tnom = null;
						if(tnoms!=null && !tnoms.isEmpty()) tnom = tnoms.get(0);
						//Rhtnom tnom = (Rhtnom) search.service.get(Rhtnom.class, new RhtnomPK(search.cdos, search.ctabs[1]));
						if (tnom != null)
							libelle += " - " + tnom.getLibe();
						libelle += ")";
						_strMessage += "<br/>" + ClsTreater._getResultat("La donn�e % n'existe pas", "ERR-80135", true, vo.getCritere2()).getLibelle()+libelle;
					}
						//_strMessage += "<br/>" + "La valeur " + vo.getCritere2() + " n'existe pas en table " + search.ctabs[1];
				}
			}

			if (!StringUtils.isBlank(vo.getCritere3()))
			{
				if (search.ctabs[2] != null)
				{
					ParamData fnom = search.service.findAnyColumnFromNomenclature(search.cdos, "", search.ctabs[2].intValue()+"", vo.getCritere3(), "1");
					//Rhfnom fnom = (Rhfnom) search.service.get(Rhfnom.class, new RhfnomPK(search.cdos, search.ctabs[2], vo.getCritere3(), 1));
					if (fnom == null)
					{
						String libelle = "(" + ClsTreater._getResultat("Table", "INF-01059", true).getLibelle() + " " + search.ctabs[2];
						List<ParamTable> tnoms = search.service.find("From ParamTable Where idEntreprise="+search.cdos+" And ctab="+search.ctabs[2].intValue());
						ParamTable tnom = null;
						if(tnoms!=null && !tnoms.isEmpty()) tnom = tnoms.get(0);
						//Rhtnom tnom = (Rhtnom) search.service.get(Rhtnom.class, new RhtnomPK(search.cdos, search.ctabs[2]));
						if (tnom != null)
							libelle += " - " + tnom.getLibe();
						libelle += ")";
						_strMessage += "<br/>" + ClsTreater._getResultat("La donn�e % n'existe pas", "ERR-80135", true, vo.getCritere3()).getLibelle()+libelle;
					}
						//_strMessage += "<br/>" + "La valeur " + vo.getCritere3() + " n'existe pas en table " + search.ctabs[2];
				}
			}
//		}

		if (StringUtils.isBlank(vo.getCompte()))
			_strMessage += "<br/>" + ClsTreater._getMessage("Saisir la valeur du compte ","ERR-70-AF-003");
		
		//Contr�le de la cl�
		if(StringUtils.isNotBlank(vo.getCrub()) && StringUtils.isNotBlank(vo.getSens()) )
		{
			String query = "From ElementSalaireCompte where idEntreprise = '"+search.cdos+"' and crub = '"+vo.getCrub() +"' and sens = '"+vo.getSens()+"' ";
			query+= "and coalesce(critere1,'') = '"+ StringUtil.nvl(vo.getCritere1(), StringUtils.EMPTY)+"' ";
			query+= "and coalesce(critere2,'') = '"+StringUtil.nvl(vo.getCritere2(), StringUtils.EMPTY)+"' ";
			query+= "and coalesce(critere3,'') = '"+StringUtil.nvl(vo.getCritere3(), StringUtils.EMPTY)+"' ";
			
			List lst = search.service.find(query);
			if(!lst.isEmpty())
			{
				String values=vo.getCrub()+" - "+vo.getSens()+" - "+vo.getCritere1()+" - "+vo.getCritere2()+" - "+vo.getCritere3();
				_strMessage += "<br/>" +ClsTreater._getParametizedMessageValue(ClsTreater._getMessage("Il existe d�j� une ligne avec les informations %","ERR-70-AF-004"),values);
			}
		}

		return _strMessage;
	}

	private static Map<String, List<SaisieCompteRubriqueVO>> chargerRubriquesSaisie(ClsRechercheCompteRubriqueVO search, List<SaisieCompteRubriqueVO> liste)
	{
		Map<String, List<SaisieCompteRubriqueVO>> map = new HashMap<String, List<SaisieCompteRubriqueVO>>();

		Map<String, String> rubq = new HashMap<String, String>();

		String query = "From ElementSalaireCompte where cdos = :cdos and crub = :crub order by nume ";
		String crub;
		List<ElementSalaireCompte> lst = new ArrayList<ElementSalaireCompte>();
		for (SaisieCompteRubriqueVO vo : liste)
		{
			crub = vo.getCrub();
			if (!map.containsKey(crub))
				map.put(crub, new ArrayList<SaisieCompteRubriqueVO>());
			if (!rubq.containsKey(crub))
			{
				query = "From ElementSalaireCompte where idEntreprise = '" + search.cdos + "' and crub = '" + vo.getCrub() + "' order by nume ";
				lst = search.service.find(query);
				for (ElementSalaireCompte db : lst)
					map.get(crub).add(SaisieCompteRubriqueVO.convertToBusinness(db));

				rubq.put(crub, crub);
			}

			map.get(crub).add(vo);
		}

		return map;
	}

	public static ClsResultat enregistrerDonnee2(ClsRechercheCompteRubriqueVO search, List<SaisieCompteRubriqueVO> liste)
	{

		Map<String, List<SaisieCompteRubriqueVO>> map = SaisieCompteRubriqueVO.chargerRubriquesSaisie(search, liste);
		ClsResultat oResult = null;
		Session session = search.service.getSession();
		Transaction tx = null;
		Query q = null;
		List<SaisieCompteRubriqueVO> listef;
		try
		{
			tx = session.beginTransaction();

			Integer nume = 0;
			for (String crub : map.keySet())
			{
				listef = map.get(crub);
				q = session.createSQLQuery("Delete from ElementSalaireCompte where idEntreprise = '" + search.cdos + "' and crub = '" + crub + "' ");
				q.executeUpdate();

				nume = 0;
				for (SaisieCompteRubriqueVO vo : listef)
				{
					vo.setNume(++nume);

					session.save(SaisieCompteRubriqueVO.convertToEntity(vo));

					//vo.setMode(Mode.CONSULTATION);
				}
			}

			tx.commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			oResult = ClsTreater._getResultat(ClsTreater._getStackTrace(e), null, true);
			if (tx != null)
				tx.rollback();
		}
		finally
		{
			search.service.closeSession(session);
		}

		return oResult;
	}
	
	public static Integer getCurrentDBMaxNume(GeneriqueConnexionService service, String cdos, String crub)
	{
		String queryString = "select max(nume) From ElementSalaireCompte  where idEntreprise=" + "'" + cdos + "'" + " and crub=" + "'" + crub + "'";

		List result = service.findByQuery(queryString);

		if (result.size() > 0)
			return Integer.valueOf(result.get(0) == null ? "0" : result.get(0).toString());
		else return 0;

	}
	
	
	
	public static ClsResultat enregistrerDonnee(ClsRechercheCompteRubriqueVO search, List<SaisieCompteRubriqueVO> liste)
	{
		Map<String, Integer> mapCurrentNume = new HashMap<String, Integer>();
		ClsResultat oResult = null;
		Session session = search.service.getSession();
		Transaction tx = null;
		String crub;
		try
		{
			tx = session.beginTransaction();

			Integer nume = 0;
			
			nume = 0;
			for (SaisieCompteRubriqueVO vo : liste)
			{
				crub = vo.getCrub();
				
				//if(Mode.MODIFICATION.equals(vo.getMode()))
					session.update(SaisieCompteRubriqueVO.convertToEntity(vo));
//				else
//				{
//					if( ! mapCurrentNume.containsKey(crub))
//						mapCurrentNume.put(crub, getCurrentDBMaxNume(search.service, search.cdos, crub));
//
//					nume = mapCurrentNume.get(crub);
//					vo.setNume(++nume);
//
//					session.save(SaisieCompteRubriqueVO.convertToEntity(vo));
//				}

				//vo.setMode(Mode.CONSULTATION);
			}

			tx.commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			oResult = ClsTreater._getResultat(ClsTreater._getStackTrace(e), null, true);
			if (tx != null)
				tx.rollback();
		}
		finally
		{
			search.service.closeSession(session);
		}

		return oResult;
	}

	public void localToString()
	{
		// System.out.println(" Nmat = " + this.getNmat() + " Rub = " + this.getRubq() + " Mont = " + this.getMont() + " Argu =" + this.getArgu());
	}

//	public Mode getMode()
//	{
//		return mode;
//	}
//
//	public void setMode(Mode mode)
//	{
//		this.mode = mode;
//	}

	public boolean hasbeenmodify()
	{
		return true; //Mode.MODIFICATION.equals(this.getMode()) || Mode.CREATION.equals(this.getMode());
	}

	public void setHasbeenmodify(boolean hasbeenmodify)
	{
		this.hasbeenmodify = hasbeenmodify;
	}

	public String getLrub()
	{
		return lrub;
	}

	public void setLrub(String lrub)
	{
		this.lrub = lrub;
	}

	public int getTabindex()
	{
		return tabindex;
	}

	public void setTabindex(int tabindex)
	{
		this.tabindex = tabindex;
	}

	public String getLcritere1()
	{
		return lcritere1;
	}

	public void setLcritere1(String lcritere1)
	{
		this.lcritere1 = lcritere1;
	}

	public String getLcritere2()
	{
		return lcritere2;
	}

	public void setLcritere2(String lcritere2)
	{
		this.lcritere2 = lcritere2;
	}

	public String getLcritere3()
	{
		return lcritere3;
	}

	public void setLcritere3(String lcritere3)
	{
		this.lcritere3 = lcritere3;
	}

	public String getLsens()
	{
		return lsens;
	}

	public void setLsens(String lsens)
	{
		this.lsens = lsens;
	}

}
