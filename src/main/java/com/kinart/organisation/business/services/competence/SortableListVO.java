package com.kinart.organisation.business.services.competence;

import com.kinart.organisation.business.vo.ClsTemplate;
import com.kinart.utils.ClsGenericComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortableListVO<T>
{

	List<T> liste;

	List<T> listeASupprimer;

	public final static String PAGING = "paging";

	public final static String SCROLLING = "scrolling";

	public final static String NONE = "none";

	int pageRows = 5;

	int nbrePageAvancementRapide = 2;

	int nbrePageAfficherAAccesDirect = 5;

	int[] listOfNbrePageRows;

	List<ClsTemplate> listeNombreLignesParPage = new ArrayList<ClsTemplate>();

	int selectedPageRows = 0;

	String selectedDataScrollMode = "paging";

	String sortColumnName;

	boolean ascending = true;

	// we only want to resort if the order or column has changed.
	protected String oldSort;

	protected boolean oldAscending;

	CommonFunctions.Mode mode;

	private Class classe;

	public SortableListVO(Class classe, String defaultSortColunm)
	{
		this.classe = classe;
		this.sortColumnName = defaultSortColunm;
	}

	public SortableListVO(Class classe, String defaultSortColunm, List<T> liste, CommonFunctions.Mode mode)
	{
		this.classe = classe;
		this.sortColumnName = defaultSortColunm;
		this.liste = liste;
		this.mode = mode;

		sortColumnName = defaultSortColunm;
		ascending = true;
		oldSort = sortColumnName;
		oldAscending = !ascending;
	}

	@SuppressWarnings("unchecked")
	protected void sort()
	{

		Collections.sort(liste, new ClsGenericComparator(classe, sortColumnName, ascending ? "ASC" : "DESC"));

	}

	public int[] getListOfNbrePageRows()
	{
		return new int[] { 5, 10, 15, 20, 30, 40, 50, 80, 100, 200 };
	}

	public void setListOfNbrePageRows(int[] listOfNbrePageRows)
	{
		this.listOfNbrePageRows = listOfNbrePageRows;
	}

	public CommonFunctions.Mode getMode()
	{
		return mode;
	}

	public void setMode(CommonFunctions.Mode mode)
	{
		this.mode = mode;
	}

	public Class getClasse()
	{
		return classe;
	}

	public void setClasse(Class classe)
	{
		this.classe = classe;
	}

	public int getNbrePageAvancementRapide()
	{
		return nbrePageAvancementRapide;
	}

	public void setNbrePageAvancementRapide(int nbrePageAvancementRapide)
	{
		this.nbrePageAvancementRapide = nbrePageAvancementRapide;
	}

	public int getNbrePageAfficherAAccesDirect()
	{
		return nbrePageAfficherAAccesDirect;
	}

	public void setNbrePageAfficherAAccesDirect(int nbrePageAfficherAAccesDirect)
	{
		this.nbrePageAfficherAAccesDirect = nbrePageAfficherAAccesDirect;
	}

	public String getPAGING()
	{
		return PAGING;
	}

	public String getSCROLLING()
	{
		return SCROLLING;
	}

	public String getNONE()
	{
		return NONE;
	}

	public String getSelectedDataScrollMode()
	{
		return selectedDataScrollMode;
	}

	public void setSelectedDataScrollMode(String selectedDataScrollMode)
	{
		this.selectedDataScrollMode = selectedDataScrollMode;
	}

	public int getPageRows()
	{
		return pageRows;
	}

	public void setPageRows(int pageRows)
	{
		this.pageRows = pageRows;
	}

	public int getSelectedPageRows()
	{
		return selectedPageRows;
	}

	public void setSelectedPageRows(int selectedPageRows)
	{
		this.selectedPageRows = selectedPageRows;
	}

	public List<T> getListe()
	{
		// this.sort();

		if (!oldSort.equals(sortColumnName) || oldAscending != ascending)
		{
			sort();
			oldSort = sortColumnName;
			oldAscending = ascending;
		}

		return liste;
	}

	public void setListe(List<T> liste)
	{
		this.liste = liste;
	}

	public String getSortColumnName()
	{
		return sortColumnName;
	}

	public void setSortColumnName(String sortColumnName)
	{
		oldSort = this.sortColumnName;
		this.sortColumnName = sortColumnName;
	}

	public boolean isAscending()
	{
		return ascending;
	}

	public void setAscending(boolean ascending)
	{
		oldAscending = this.ascending;
		this.ascending = ascending;
	}

	public List<T> getListeASupprimer()
	{
		return listeASupprimer;
	}

	public void setListeASupprimer(List<T> listeVOSupprimes)
	{
		this.listeASupprimer = listeVOSupprimes;
	}

	public List<ClsTemplate> getListeNombreLignesParPage()
	{
		return listeNombreLignesParPage;
	}

	public void setListeNombreLignesParPage(List<ClsTemplate> listeNombreLignesParPage)
	{
		this.listeNombreLignesParPage = listeNombreLignesParPage;
	}

	public String getOldSort()
	{
		return oldSort;
	}

	public void setOldSort(String oldSort)
	{
		this.oldSort = oldSort;
	}

	public boolean isOldAscending()
	{
		return oldAscending;
	}

	public void setOldAscending(boolean oldAscending)
	{
		this.oldAscending = oldAscending;
	}

}
