package com.kinart.paie.business.services.cloture;

import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.kinart.paie.business.services.utils.ParameterUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class ClsRechercheCompteRubriqueVO
{
	private String crub;

	private String critere1;

	private String critere2;

	private String critere3;

	private String lcritere1;

	private String lcritere2;

	private String lcritere3;

	private String compte;

	private String sens;

	public HttpServletRequest request;

	public GeneriqueConnexionService service;

	public String cdos;
	
	public String clang;

	public Integer nbrCriteres = 3;
	
	public String sep = "-";

	public Integer[] ctabs = new Integer[] { 0, 0, 0 };
	
	public Integer[] ctabsAgent = new Integer[] { 0, 0, 0 };

	public String[] colCriteres = new String[] { StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY };

	public String[] titres = new String[] { StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY };
	
	public String[] colsNome = new String[] { StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY };
	public Integer[] numeNome = new Integer[] { 0, 0, 0 };

	public ClsRechercheCompteRubriqueVO(HttpServletRequest request, GeneriqueConnexionService service, String cdos)
	{
		super();
		this.request = request;
		this.service = service;
		this.cdos = cdos;
		
		this.clang = ParameterUtil.getSessionObject(request, ParameterUtil.SESSION_LANGUE);
	}

	public ClsRechercheCompteRubriqueVO(HttpServletRequest request, String crub, String sens, String critere1, String critere2, String critere3, String compte)
	{
		super();
		this.crub = crub;
		this.sens = sens;
		this.critere1 = critere1;
		this.critere2 = critere2;
		this.critere3 = critere3;
		this.compte = compte;
		this.request = request;
		this.clang = ParameterUtil.getSessionObject(request, ParameterUtil.SESSION_LANGUE);
	}

	public String getCrub()
	{
		return crub;
	}

	public void setCrub(String crub)
	{
		this.crub = crub;
	}

	public String getCritere1()
	{
		return critere1;
	}

	public void setCritere1(String critere1)
	{
		this.critere1 = critere1;
	}

	public String getCritere2()
	{
		return critere2;
	}

	public void setCritere2(String critere2)
	{
		this.critere2 = critere2;
	}

	public String getCritere3()
	{
		return critere3;
	}

	public void setCritere3(String critere3)
	{
		this.critere3 = critere3;
	}

	public String getCompte()
	{
		return compte;
	}

	public void setCompte(String compte)
	{
		this.compte = compte;
	}

	public String getSens()
	{
		return sens;
	}

	public void setSens(String sens)
	{
		this.sens = sens;
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

	public Integer[] getCtabs()
	{
		return ctabs;
	}

	public void setCtabs(Integer[] ctabs)
	{
		this.ctabs = ctabs;
	}

	public String[] getColCriteres()
	{
		return colCriteres;
	}

	public void setColCriteres(String[] colCriteres)
	{
		this.colCriteres = colCriteres;
	}

	public String[] getTitres()
	{
		return titres;
	}

	public void setTitres(String[] titres)
	{
		this.titres = titres;
	}

	public Integer[] getCtabsAgent()
	{
		return ctabsAgent;
	}

	public void setCtabsAgent(Integer[] ctabsAgent)
	{
		this.ctabsAgent = ctabsAgent;
	}

	public String[] getColsNome()
	{
		return colsNome;
	}

	public void setColsNome(String[] colsNome)
	{
		this.colsNome = colsNome;
	}

	public Integer[] getNumeNome()
	{
		return numeNome;
	}

	public void setNumeNome(Integer[] numeNome)
	{
		this.numeNome = numeNome;
	}
	
}
