package com.kinart.paie.business.utils;

import java.math.BigDecimal;
import java.util.List;

import com.kinart.paie.business.model.ParamData;
import com.kinart.paie.business.model.Salarie;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import org.hibernate.Session;

public interface IJournalPaie
{

	public abstract LstSession initLstSessionID(String cdos, String cuti);

	public abstract void purgerTablesTemp(String CLE);

	public abstract void initData();

	public abstract List<ParamData> getRowRhfnom(String cdos, Integer ctab);

	public abstract void lectureCle(String cdos, Integer ctab);

	public abstract void insereRhtedjpai(String idSession, String cdos, String NIV1, String NIV2, String NIV3, String NMAT, Integer NUME, String[] COLS, Session session, boolean montants);

	public abstract void genereEntete(String cdos);

	public abstract void getListeSalarie(String cdos, String Tri_1, String Tri_2, String Tri_3, String niv1Min, String niv1Max, String niv2Min, String niv2Max, String niv3Min, String niv3Max,
			String nmatMin, String nmatMax, String clas,String queryExt);

	public abstract BigDecimal lectureMontantRub(String RUBQ, String cdos, String NMAT, String periode1, String PER2, Integer cas, Integer nbul, String colonnealire);

	public abstract void genereRegTotalGeneral(String idsession);

	public abstract void genereRegNiv1(String idsession);

	public abstract void genereRegNiv2(String idsession);

	public abstract void genereRegNiv3(String idsession);

	public abstract void genereRegroupement(String Tri_1, String Tri_2, String Tri_3, String session);

	public abstract void ecrireSalarie(String cdos, String NMAT, String niv1, String niv2, String niv3, String periode1, String PER2, Integer cas, Integer nbul);

	/** ******************POUR LE TRAITEMENT THREADE*************************************** */
	public abstract void initJournalPaie(String cdos, String cuti, Integer ctab, String clas, String niv1Min, String niv1Max, String niv2Min, String niv2Max, String niv3Min, String niv3Max,
			String nmatMin, String nmatMax, String periode1, String PER2, Integer nbul, Integer cas, String tri1, String tri2, String tri3,String queryExt);

	public abstract void journaliserUnAgent(Salarie salarie);

	public abstract String cloturerJournalPaie();

	/** *****************FIN TRAITEMENT THREADE******************************************* */

	public abstract String genererJournalPaieCumule(String cdos, String cuti, Integer ctab, String clas, String niv1Min, String niv1Max, String niv2Min, String niv2Max, String niv3Min,
			String niv3Max, String nmatMin, String nmatMax, String periode1, String PER2, Integer nbul, Integer cas, String tri1, String tri2, String tri3);

	// Ecriture de la classe principale pour la g�n�ration du journal de paie
	public abstract String genererJournalPaie(String cdos, String cuti, Integer ctab, String clas, String niv1Min, String niv1Max, String niv2Min, String niv2Max, String niv3Min, String niv3Max,
			String nmatMin, String nmatMax, String periode1, Integer nbul, Integer cas, String tri1, String tri2, String tri3,String queryExt);

	public abstract LstSession getListIdSession();

	public abstract void setListIdSession(LstSession listIdSession);

	public abstract GeneriqueConnexionService getService();

	public abstract void setService(GeneriqueConnexionService service);

	public abstract List<ParamData> getTabParametrage();

	public abstract void setTabParametrage(List<ParamData> tabParametrage);

	public abstract ClsJournalPaieGlobal.FormuleLibelle[][] getTabForm();

	public abstract void setTabForm(ClsJournalPaieGlobal.FormuleLibelle[][] tabForm);

	public abstract List<Salarie> getTabsalaries();

	public abstract void setTabsalaries(List<Salarie> tabsalaries);

	public abstract List<BigDecimal> getTotal();

	public abstract void setTotal(List<BigDecimal> total);

	public abstract String getCdos();

	public abstract void setCdos(String cdos);

	public abstract String getCuti();

	public abstract void setCuti(String cuti);

	public abstract Integer getCtab();

	public abstract void setCtab(Integer ctab);

	public abstract String getClas();

	public abstract void setClas(String clas);

	public abstract String getNiv1min();

	public abstract void setNiv1min(String niv1min);

	public abstract String getNiv1max();

	public abstract void setNiv1max(String niv1max);

	public abstract String getNiv2min();

	public abstract void setNiv2min(String niv2min);

	public abstract String getNiv2max();

	public abstract void setNiv2max(String niv2max);

	public abstract String getNiv3min();

	public abstract void setNiv3min(String niv3min);

	public abstract String getNiv3max();

	public abstract void setNiv3max(String niv3max);

	public abstract String getNmatmin();

	public abstract void setNmatmin(String nmatmin);

	public abstract String getNmatmax();

	public abstract void setNmatmax(String nmatmax);

	public abstract String getPer1();

	public abstract void setPer1(String per1);

	public abstract String getPer2();

	public abstract void setPer2(String per2);

	public abstract Integer getNbul();

	public abstract void setNbul(Integer nbul);

	public abstract Integer getCas();

	public abstract void setCas(Integer cas);

	public abstract String getTri1();

	public abstract void setTri1(String tri1);

	public abstract String getTri2();

	public abstract void setTri2(String tri2);

	public abstract String getTri3();

	public abstract void setTri3(String tri3);

}
