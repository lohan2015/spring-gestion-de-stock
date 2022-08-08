package com.kinart.organisation.business.vo;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.kinart.organisation.business.model.Organigramme;
import com.kinart.paie.business.model.DossierPaie;
import com.kinart.paie.business.services.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.BeanUtils;

public class AjoutFilsOrganigrammeVO extends Organigramme
{
	private static final long serialVersionUID = 1L;

	boolean selected = false;

	//Mode mode;

	@SuppressWarnings("unused")
	private boolean hasbeenmodify = false;

	private Integer numero;

	private boolean prestataire = false;

	private boolean casefictive = false;

	public AjoutFilsOrganigrammeVO()
	{
		super();
		//this.setMode(Mode.CREATION);
	}

	public static AjoutFilsOrganigrammeVO getBusinessObject(GeneriqueConnexionService service, String codeDossier, String codeOrganigramme)
	{
		AjoutFilsOrganigrammeVO vo = new AjoutFilsOrganigrammeVO();

		Organigramme emploi = null;//(Organigramme) service.get(Organigramme.class, new OrganigrammePK(codeDossier, codeOrganigramme));

		List<Organigramme> list = service.find("from Organigramme where identreprise='"+codeDossier+"' And codeorganigramme='"+codeOrganigramme+"'");
		if (list != null && list.size() > 0)
		{
			emploi = list.get(0);
		}

		if (emploi != null)
		{
			BeanUtils.copyProperties(emploi, vo);

			vo.setPrestataire(StringUtils.equals("O", emploi.getBprestataire()));

			vo.setCasefictive(StringUtils.equals("O", emploi.getBcasefictive()));

			//vo.setMode(Mode.MODIFICATION);
		}
		return vo;
	}

	public Organigramme getEntityObject()
	{
		Organigramme org = new Organigramme();

		BeanUtils.copyProperties(this, org);

		return org;
	}

	public ClsResultat save(HttpServletRequest request, GeneriqueConnexionService service)
	{
		return this.saveAData(request, service, true, false);
	}

	public ClsResultat update(HttpServletRequest request, GeneriqueConnexionService service)
	{
		return this.saveAData(request, service, false, false);
	}

	public ClsResultat delete(HttpServletRequest request, GeneriqueConnexionService service)
	{
		return this.saveAData(request, service, false, true);
	}

	public ClsResultat exist(HttpServletRequest request, GeneriqueConnexionService service)
	{
		Organigramme emploi = null;//(Organigramme) service.get(Organigramme.class, new OrganigrammePK(pk.getCdos(), pk.getCodeorganigramme()));
		List<Organigramme> list = service.find("from Organigramme where identreprise='"+this.getIdEntreprise()+"' And codeorganigramme='"+this.getCodeorganigramme()+"'");
		if (list != null && list.size() > 0)
		{
			emploi = list.get(0);
		}
		if (emploi != null)
			return ClsTreater._getResultat("La ligne existe deja", "INF-80432", true);
		else
			return null;
	}

	private ClsResultat saveAData(HttpServletRequest request, GeneriqueConnexionService service, boolean save, boolean delete)
	{
		ClsResultat resultat = null;
		Session sess = service.getSession();
		Transaction tx = null;

		Organigramme emploi = this.getEntityObject();

		try
		{
			tx = sess.beginTransaction();
			if (delete)
			{
				sess.delete(emploi);
			}
			else
			{
				if (StringUtils.equals("O", emploi.getBcasefictive()))
					emploi.setCodeniveau("0");
				if (save)
				{
					sess.save(emploi);
				}
				else
					sess.update(emploi);
			}
			tx.commit();
			if (delete)
				resultat = ClsTreater._getResultat("Suppression éffectuée avec succés", "INF-80180", false);
			else
			{
				if (save)
					resultat = ClsTreater._getResultat("Sauvegarde éffectuée avec succés", "INF-80178", false);
				else
					resultat = ClsTreater._getResultat("Mise é jour éffectuée avec succés", "INF-80179", false);
			}
		}
		catch (Exception e)
		{
			if (tx != null)
				tx.rollback();

			e.printStackTrace();

			resultat = ClsTreater._getResultat(ClsTreater._getStackTrace(e), null, true);
		}
		finally
		{
			service.closeSession(sess);
		}
		return resultat;
	}

	public ClsResultat _checkValues(HttpServletRequest request)
	{

		ClsResultat _o_Result_Start = ClsTreater._getResultat("Saisir la valeur du", "INF-80097", true);

		ClsResultat _o_Result_End = null;

		String sep = ParameterUtil.CHAINE_ALLER_A_LA_LIGNE + ParameterUtil.CHAINE_TIRET;

		if (StringUtils.isBlank(this.getCodeorganigramme()))
			_o_Result_End = ClsTreater._concat(_o_Result_End, ClsTreater._getResultat("Code", "INF-00034", true), sep);

		if (StringUtils.isBlank(this.getLibelle()))
			_o_Result_End = ClsTreater._concat(_o_Result_End, ClsTreater._getResultat("Libellé", "INF-00626", true), sep);

		if (_o_Result_End == null)
			return null;
		else
			return ClsTreater._concat(_o_Result_Start, _o_Result_End, sep);
	}

	public ClsResultat _checkExistance(HttpServletRequest request, GeneriqueConnexionService service)
	{
		ClsResultat resultat = null;
		Organigramme business = null;//(Organigramme) service.get(Organigramme.class, new OrganigrammePK(pk.getCdos(), pk.getCodeorganigramme()));
		List<Organigramme> list = service.find("from Organigramme where identreprise='"+this.getIdEntreprise()+"' And codeorganigramme='"+this.getCodeorganigramme()+"'");
		if (list != null && list.size() > 0)
		{
			business = list.get(0);
		}
		if (business != null)
			resultat = ClsTreater._getResultat("La donnée % existe déja", "ERR-80020", true, this.getCodeorganigramme());
		return resultat;
	}
	
	/**
	 * Retourne la liste des numero non encore utilisé
	 * @param service
	 * @return
	 */
	public void numerosVides(GeneriqueConnexionService service, int nbr)
	{
		List<String> posibilites = new ArrayList<String>();
		List<String> posibilitesend = new ArrayList<String>();
		for(int i=1; i< 100; i++)
			posibilites.add(ClsStringUtil.formatNumber(i, "00"));
		
		 numerovide = StringUtils.EMPTY;
		String query=" From Organigramme where codepere= '"+this.getCodepere()+"' and identreprise = '"+this.getIdEntreprise()+"'";
		List<Organigramme> fils = service.find(query);
		
		String codeorg;
		for (Organigramme org : fils)
		{
			codeorg = StringUtils.substring( org.getCodeorganigramme(), this.getCodepere().length());
			posibilitesend.add(codeorg);	
		}
		
		int j = 0;
		for(int i=1; i< 100; i++)
		{
			codeorg = posibilites.get(i - 1);
			if(posibilitesend.contains(codeorg))
				continue;
			 j++;
			if(StringUtils.isBlank(numerovide))
				numerovide = String.valueOf(Integer.parseInt(codeorg));
			else
				numerovide +=","+ String.valueOf(Integer.parseInt(codeorg));
			
			if(j % nbr == 0)
				break;
		}
	}
	
	private String numerovide;
	
	
	public String getNumerovide()
	{
		return numerovide;
	}

	public void setNumerovide(String numerovide)
	{
		this.numerovide = numerovide;
	}

	public boolean isSelected()
	{
		return selected;
	}

	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}

	public void setHasbeenmodify(boolean hasbeenmodify)
	{
		this.hasbeenmodify = hasbeenmodify;
	}

	public boolean isPrestataire()
	{
		return prestataire;
	}

	public void setPrestataire(boolean prestataire)
	{
		this.prestataire = prestataire;
		if (this.prestataire)
			this.setBprestataire("O");
		else
			this.setBprestataire("N");
	}

	public boolean isCasefictive()
	{
		return casefictive;
	}

	public void setCasefictive(boolean casefictive)
	{
		this.casefictive = casefictive;
		if (this.casefictive)
			this.setBcasefictive("O");
		else
			this.setBcasefictive("N");
	}

	public Integer getNumero()
	{
		return numero;
	}

	public void setNumero(Integer numero)
	{
		this.numero = numero;
		if(this.numero != null)
		{
			String code = this.numero.toString();
			if(code.length() == 1)
				code = "0"+code;
			this.setCodeorganigramme(this.getCodepere()+code);
		}
	}
	
	

}
