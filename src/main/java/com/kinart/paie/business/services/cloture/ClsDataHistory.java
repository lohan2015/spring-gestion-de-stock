package com.kinart.paie.business.services.cloture;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.kinart.paie.business.services.utils.ClsTreater;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.context.ApplicationContext;

public class ClsDataHistory
{
	public ClsGlobalUpdate globalUpdate;
	//Session oSession = null;

	//Transaction tx = null;

	public boolean processHistory(HttpServletRequest request, GeneriqueConnexionService service, ClsGlobalUpdate globalUpdate, String cdos, String periode, int bulletin, String cuti)
	throws Exception
	{
		this.globalUpdate = globalUpdate;
		Session oSession = null;
		Transaction tx =null;
		
		oSession = service.getSession();
		tx = oSession.beginTransaction();
		
		boolean res = false;
		try
		{
//			oSession = service.getSession();
//			tx = oSession.beginTransaction();
			//		CREATE OR REPLACE PACKAGE BODY PA_HIST
			//		AS
			//		-------------------------------------------------
			//		--  gen_hist :    SOUS - PROGRAMMES
			//		-------------------------------------------------
			//		PROCEDURE gen_hist  ( P_dos		 IN VARCHAR2,
			//		                      P_cuti		 IN VARCHAR2,
			//					    P_aamm         IN VARCHAR2,
			//		                      P_nbul         IN VARCHAR2,
			//		                      l_pipe_name    IN VARCHAR2)
			//		 IS
			//
			//
			//		  w_cas            VARCHAR2(10);
			//		  w_mess           VARCHAR2(100);
			//		  w_pipe_name      VARCHAR2(30);
			//		  w_status 		NUMBER;
			//
			//		  w_cdos 	  VARCHAR2(2);
			//		  w_aamm      VARCHAR2(6);
			//		  w_nbul      NUMBER(1);
			//
			//		  wpatnom     patnom%ROWTYPE;
			//
			//		  wpalnom     palnom%ROWTYPE;
			//
			//		  wpafnom     pafnom%ROWTYPE;
			//
			//		  wrub	  parubq%ROWTYPE;
			//
			//		  wrbqbas     parbqbas%ROWTYPE;
			//
			//		  wbarem      parubbarem%ROWTYPE;
			//
			//		  wsal01      pasa01%ROWTYPE;
			//
			//		  wainfo      painfo%ROWTYPE;
			//
			//		  waelfix     paelfix%ROWTYPE;
			//
			//		  wcalc	  pacumu%ROWTYPE;
			//
			//		  waev	  paev%ROWTYPE;
			//
			//		  waevar	  paevar%ROWTYPE;
			//
			//		  waevcg	  paevcg%ROWTYPE;
			//
			//
			//		  CURSOR C_PATNOM IS
			//		      SELECT * FROM patnom
			//		       where cdos = w_cdos
			//		       order by cdos;
			//
			//		  CURSOR C_PALNOM IS
			//		      SELECT * FROM palnom
			//		       where cdos = w_cdos
			//		       order by cdos;
			//
			//		  CURSOR C_PAFNOM IS
			//		      SELECT * FROM pafnom
			//		       where cdos = w_cdos
			//		       order by cdos;
			//
			//		  CURSOR C_PARUBQ IS
			//		      SELECT * FROM parubq
			//		       where cdos = w_cdos
			//		       order by cdos;
			//
			//		  CURSOR C_PARBQBAS IS
			//		      SELECT * FROM parbqbas
			//		       where cdos = w_cdos
			//		       order by cdos;
			//
			//		  CURSOR C_PARUBBAREM IS
			//		      SELECT * FROM parubbarem
			//		       where cdos = w_cdos
			//		       order by cdos;
			//
			//		  CURSOR C_PAELFIX IS
			//		      SELECT * FROM paelfix
			//		       where cdos = w_cdos
			//		       order by cdos;
			//
			//		  CURSOR C_PASA01 IS
			//		      SELECT * FROM pasa01
			//		       where cdos = w_cdos
			//		       order by cdos;
			//
			//		  CURSOR C_PAINFO IS
			//		      SELECT * FROM painfo
			//		       where cdos = w_cdos
			//		         and nomtab = 'PASA01'
			//		         and calcr = 'O'
			//		       order by cdos;
			//String queryInfoTable = "from Rhtinfo where comp_id.cdos = '" + cdos + "' and comp_id.nomtab in('PASA01','RHPAGENT') and calcr = 'O' order by comp_id.cdos";
			//
			//
			//		  CURSOR C_PACALC IS
			//		      SELECT CDOS,NMAT,AAMM,RUBQ,NBUL,SUM(NVL(BASC,0)),SUM(NVL(BASP,0)),SUM(NVL(TAUX,0)),SUM(NVL(MONT,0)) FROM pacalc
			//		       WHERE cdos = w_cdos
			//		         AND aamm = w_aamm
			//		         AND nbul = w_nbul
			//		        GROUP BY CDOS,NMAT,AAMM,RUBQ,NBUL;
			//
			//		  CURSOR C_PAEV IS
			//			SELECT * FROM paev
			//		       WHERE cdos = w_cdos
			//		         AND aamm = w_aamm
			//		         AND nbul = w_nbul;
			//
			//		  CURSOR C_PAEVAR IS
			//			SELECT * FROM paevar
			//		       WHERE cdos = w_cdos
			//		         AND aamm = w_aamm
			//		         AND nbul = w_nbul;
			//
			//		  CURSOR C_PAEVCG IS
			//			SELECT * FROM paevcg
			//		       WHERE cdos = w_cdos
			//		         AND aamm = w_aamm
			//		         AND nbul = w_nbul;
			//
			//		--------------------------------------------------
			//		--   CORPS gen_hist
			//		--------------------------------------------------
			//		BEGIN
			//
			//		-- initialisation des parametres (param pipe pour job)
			//		   w_pipe_name := l_pipe_name;
			//		   w_aamm := P_aamm;
			//		   w_nbul := P_nbul;
			//		   w_cdos := P_dos;
			//
			//
			//		   -- Nomenclatures
			//		   w_cas := 'H1';
			//		   w_mess := 'Historiques des nomenclatures...';
			//		   DBMS_PIPE.PACK_MESSAGE(w_cas);
			//		   DBMS_PIPE.PACK_MESSAGE(w_mess);
			//		   w_status := DBMS_PIPE.SEND_MESSAGE(w_pipe_name);
			String cas = "H";
			String message = "Historiques des nomenclatures...";
			globalUpdate._setEvolutionTraitement(request, message, false);
			//
			//		   OPEN C_PATNOM;
			//		   LOOP
			//		      FETCH C_PATNOM INTO wpatnom;
			//		      EXIT WHEN C_PATNOM%NOTFOUND;
			//
			//		      INSERT INTO pahtnom
			//		             VALUES(wpatnom.cdos,wpatnom.ctab,wpatnom.libe,wpatnom.nccl,wpatnom.typc,wpatnom.duti,wpatnom.ddmo,w_aamm,w_nbul);
			//
			//		   END LOOP;
			//		   CLOSE C_PATNOM;

			String deleteQuery = null;
//			String deleteQuery = "Delete From Rhthtnom where comp_id.cdos='"+cdos+"' and comp_id.aamm='"+periode+"' and comp_id.nbul="+bulletin;
//			oSession.createQuery(deleteQuery).executeUpdate();
//			String deleteQuery = "Delete From Rhthtnom where cdos='"+cdos+"' and aamm='"+periode+"' and nbul="+bulletin;
//			oSession.createSQLQuery(deleteQuery).executeUpdate();
			
			List l = null;
//			l = service.find("from Rhtnom where comp_id.cdos = '" + cdos + "'");
//			Rhtnom tnomenc = null;
//			Rhthtnom htnomenc = null;
			int i = 0;
//			for (Object object : l)
//			{
//				i++;
//				tnomenc = (Rhtnom) object;
//				htnomenc = new Rhthtnom();
//				htnomenc.setComp_id(new RhthtnomPK(tnomenc.getComp_id().getCdos(), tnomenc.getComp_id().getCtab(), periode, bulletin));
//				htnomenc.setDdmo(new Date());
//				htnomenc.setDuti(cuti);
//				htnomenc.setLibe(tnomenc.getLibe());
//				htnomenc.setNccl(tnomenc.getNccl());
//				htnomenc.setTypc(tnomenc.getTypc());
//				//
//				oSession.save(htnomenc);
//				if (i % 20 == 0)
//				{//20, m�me taille que la taille du pacquet JDBC
//					//flush un pacquet d'insertion et lib�re la m�moire
//					oSession.flush();
//					oSession.clear();
//				}
//			}
			String queryInsert = null;
//			String queryInsert = "insert into Rhthtnom(cdos, ctab, libe, nccl, typc, duti, ddmo, aamm, nbul)" +
//						"select cdos, ctab, libe, nccl, typc, duti, ddmo, '" + periode + "', " + bulletin + " from Rhtnom" +
//						" where cdos = '" + cdos + "'";
//			oSession.createSQLQuery(queryInsert).executeUpdate();
			//		String queryInsert = "insert into Rhthtnom(cdos, ctab, libe, nccl, typc, duti, ddmo, aamm, nbul)" +
			//			"select cdos, ctab, libe, nccl, typc, duti, ddmo, '" + periode + "', " + bulletin + " from Rhtnom" +
			//			" where cdos = '" + cdos + "'";
			//		int createdEntities = service.bulkUpdate(queryInsert);
			//		com.cdi.deltarh.service.ClsParameter.println("...Nommbre d'�l�ments ins�r�s dans Rhthtnom: " + createdEntities);
			//
			//		   OPEN C_PALNOM;
			//		   LOOP
			//		      FETCH C_PALNOM INTO wpalnom;
			//		      EXIT WHEN C_PALNOM%NOTFOUND;
			//
			//		      INSERT INTO pahlnom
			//		             VALUES(wpalnom.cdos,wpalnom.ctab,wpalnom.ctyp,wpalnom.nume,wpalnom.libe,wpalnom.duti,wpalnom.ddmo,w_aamm,w_nbul);
			//
			//		   END LOOP;
			//		   CLOSE C_PALNOM;
			//l = service.find("from Rhlnom where comp_id.cdos = '" + cdos + "'");

//			deleteQuery = "Delete From Rhthlnom where comp_id.cdos='"+cdos+"' and comp_id.aamm='"+periode+"' and comp_id.nbul="+bulletin;
//			oSession.createQuery(deleteQuery).executeUpdate();
//			deleteQuery = "Delete From Rhthlnom where cdos='"+cdos+"' and aamm='"+periode+"' and nbul="+bulletin;
//			oSession.createSQLQuery(deleteQuery).executeUpdate();
			
//			l = service.findFromRhlnom("Select * From Rhlnom where cdos='" + cdos + "'");
//			Rhlnom lnomenc = null;
//			Rhthlnom hlnomenc = null;
//			i = 0;
//			for (Object object : l)
//			{
//				i++;
//				lnomenc = (Rhlnom) object;
//				hlnomenc = new Rhthlnom();
//				hlnomenc.setComp_id(new RhthlnomPK(lnomenc.getComp_id().getCdos(), lnomenc.getComp_id().getCtab(), lnomenc.getComp_id().getCtyp(), lnomenc.getComp_id().getNume(), periode, bulletin));
//				hlnomenc.setDdmo(new ClsDate(new Date()).getDate());
//				hlnomenc.setDuti(cuti);
//				hlnomenc.setLibe(lnomenc.getLibe());
//				//
//				oSession.save(hlnomenc);
//				if (i % 20 == 0)
//				{//20, m�me taille que la taille du pacquet JDBC
//					//flush un pacquet d'insertion et lib�re la m�moire
//					oSession.flush();
//					oSession.clear();
//				}
//			}

			
			//
//					queryInsert = "insert into Rhthlnom(cdos, ctab, ctyp, nume, libe, duti, ddmo, aamm, nbul)" +
//						"select cdos, ctab, ctyp, nume, libe, duti, ddmo, '" + periode + "', " + bulletin + " from Rhlnom" +
//						" where cdos = '" + cdos + "'";
//					oSession.createSQLQuery(queryInsert).executeUpdate();
			//		com.cdi.deltarh.service.ClsParameter.println("...Nommbre d'�l�ments ins�r�s dans Rhthlnom: " + createdEntities);
			//
			//		   OPEN C_PAFNOM;
			//		   LOOP
			//		      FETCH C_PAFNOM INTO wpafnom;
			//		      EXIT WHEN C_PAFNOM%NOTFOUND;
			//
			//		      INSERT INTO pahfnom
			//		             VALUES(wpafnom.cdos,wpafnom.ctab,wpafnom.cacc,wpafnom.nume,wpafnom.vall,wpafnom.valm,wpafnom.valt,wpafnom.duti,wpafnom.ddmo,w_aamm,w_nbul);
			//
			//		   END LOOP;
			//		   CLOSE C_PAFNOM;

//			deleteQuery = "Delete From Rhthfnom where comp_id.cdos='"+cdos+"' and comp_id.aamm='"+periode+"' and comp_id.nbul="+bulletin;
//			oSession.createQuery(deleteQuery).executeUpdate();
			deleteQuery = "Delete From HistoParamData where idEntreprise='"+cdos+"' and aamm='"+periode+"' and nbul="+bulletin;
			oSession.createSQLQuery(deleteQuery).executeUpdate();
			
//			l = service.find("from Rhfnom where comp_id.cdos = '" + cdos + "'");
//			Rhfnom nomenc = null;
//			Rhthfnom hnomenc = null;
//			i = 0;
//			for (Object object : l)
//			{
//				i++;
//				nomenc = (Rhfnom) object;
//				hnomenc = new Rhthfnom();
//				hnomenc.setComp_id(new RhthfnomPK(nomenc.getComp_id().getCdos(), nomenc.getComp_id().getCtab(), nomenc.getComp_id().getCacc(), nomenc.getComp_id().getNume(), periode, bulletin));
//				hnomenc.setDdmo(new Date());
//				//			com.cdi.deltarh.service.ClsParameter.println("...cuti:"+ cuti);
//				hnomenc.setDuti(cuti);
//				hnomenc.setVall(nomenc.getVall());
//				hnomenc.setValm(nomenc.getValm());
//				hnomenc.setValt(nomenc.getValt());
//				hnomenc.setVald(nomenc.getVald());
//				//
//				oSession.save(hnomenc);
//				
//				if (i % 20 == 0)
//				{//20, m�me taille que la taille du pacquet JDBC
//					//flush un pacquet d'insertion et lib�re la m�moire
//					oSession.flush();
//					oSession.clear();
//				}
//			}

			
			//
					queryInsert = "insert into HistoParamData(id, idEntreprise, ctab, cacc, nume, vall, valm, valt, duti, creation_date, last_modified_date, aamm, nbul)" +
						"select id, idEntreprise, ctab, cacc, nume, vall, valm, valt, duti, creation_date, last_modified_date, '" + periode + "', " + bulletin + " from ParamData" +
						" where idEntreprise = '" + cdos + "'";
					oSession.createSQLQuery(queryInsert).executeUpdate();
			//		com.cdi.deltarh.service.ClsParameter.println("...Nommbre d'�l�ments ins�r�s dans Rhthfnom: " + createdEntities);
			//
			//
			//		   --- rubriques
			//		   w_cas := 'H2';
			//		   w_mess := 'Historiques des rubriques...';
			cas = "H2";
			message = "Historiques des éléments de salaire...";
			globalUpdate._setEvolutionTraitement(request, message, false);
			//		   DBMS_PIPE.PACK_MESSAGE(w_cas);
			//		   DBMS_PIPE.PACK_MESSAGE(w_mess);
			//		   w_status := DBMS_PIPE.SEND_MESSAGE(w_pipe_name);
			//
			//		   OPEN C_PARUBQ;
			//		   LOOP
			//		       FETCH C_PARUBQ INTO wrub;
			//		       EXIT WHEN C_PARUBQ%NOTFOUND;
			//
			//		       INSERT INTO pahrubq
			//		              VALUES(wrub.CDOS,wrub.CRUB,wrub.LRUB,wrub.CALC,wrub.PROR,wrub.PPCG,wrub.PRAC,wrub.PRHR,wrub.PRTB,wrub.PRCL,wrub.PRTM,wrub.PRNO,wrub.PPAS,wrub.MOI1,wrub.MOI2,wrub.MOI3,wrub.MOI4,
			//		                     wrub.BUL1,wrub.BUL2,wrub.BUL3,wrub.BUL4,wrub.APCF,wrub.CABF,wrub.PRBUL,wrub.CBULF,wrub.EDNUL,wrub.EDCUM,wrub.EDBBU,wrub.EPBUL,wrub.AJUS,wrub.AJNU,wrub.SNET,wrub.ECAR,
			//			  	         wrub.TYPR,wrub.ESAT,wrub.RREG,wrub.RMAN,wrub.PERC,wrub.FREQ,wrub.ADDF,wrub.RCON,wrub.EDDF,wrub.BASC,wrub.TRTC,wrub.TRVE,wrub.EXO,wrub.VAL1,wrub.VAL2,wrub.VAL3,wrub.MOPA,wrub.LBTM,
			//		                     wrub.OPFI,wrub.ALGO,wrub.CLE1,wrub.CLE2,wrub.TABL,wrub.TOUM,wrub.NUTM,wrub.ARRO,wrub.RESL,wrub.SUP1,wrub.SUPS,wrub.SUP2,wrub.INF1,wrub.INFS,wrub.INF2,wrub.EGU1,wrub.EGUS,wrub.EGU2,
			//		                     wrub.CS1,wrub.CS2,wrub.CS3,wrub.SEXE,wrub.AGE1,wrub.AGE2,wrub.SIT1,wrub.SIT2,wrub.SIT3,wrub.SIT4,wrub.NBE1,wrub.NBE2,wrub.NAT1,wrub.NAT2,wrub.ZCA1,wrub.ZCA2,wrub.ZCA3,wrub.ZCA4,wrub.CAT1,
			//		                     wrub.CAT2,wrub.TYC1,wrub.TYC2,wrub.TYC3,wrub.TYC4,wrub.TYC5,wrub.TYC6,wrub.TYC7,wrub.TYC8,wrub.GRA1,wrub.GRA2,wrub.GRA3,wrub.GRA4,wrub.GRA5,wrub.GRA6,wrub.GRA7,wrub.GRA8,wrub.AVN,wrub.NIV11,
			//		                     wrub.NIV12,wrub.NIV13,wrub.NIV14,wrub.NIV21,wrub.NIV22,wrub.NIV23,wrub.NIV24,wrub.NIV31,wrub.NIV32,wrub.NIV33,wrub.NIV34,wrub.SYND,wrub.REG1,wrub.REG2,wrub.REG3,wrub.REG4,wrub.REG5,
			//		                     wrub.REG6,wrub.REG7,wrub.REG8,wrub.CLAS1,wrub.CLAS2,wrub.CLAS3,wrub.CLAS4,wrub.CFON,wrub.HIF1,wrub.HIF2,wrub.HIF3,wrub.HIF4,wrub.FON1,wrub.FON2,wrub.FON3,wrub.FON4,wrub.FON5,wrub.FON6,wrub.FON7,
			//		                     wrub.FON8,wrub.ZL11,wrub.ZL12,wrub.ZL21,wrub.ZL22,wrub.CAIS,wrub.DNBP,wrub.TXMT,wrub.TRCU,wrub.BASP,wrub.ABAT,wrub.ABMX,wrub.PMIN,wrub.PMAX,wrub.PCAB,wrub.PDAP,wrub.DTCR,wrub.DTDM,wrub.COMP,wrub.CPER,
			//		                     wrub.DE01,wrub.DE02,wrub.DE03,wrub.DE04,wrub.DE05,wrub.DE06,wrub.DE07,wrub.DE08,wrub.DE09,wrub.DE10,wrub.DE11,wrub.DE12,wrub.DE13,wrub.DE14,wrub.DE15,wrub.DE16,wrub.DE17,wrub.DE18,wrub.DE19,wrub.DE20,
			//		                     wrub.CR01,wrub.CR02,wrub.CR03,wrub.CR04,wrub.CR05,wrub.FBAS,wrub.TBAS,wrub.NOTE,w_aamm,w_nbul);
			//
			//		   END LOOP;
			//		   CLOSE C_PARUBQ;
			
//			deleteQuery = "Delete From Rhthrubq where comp_id.cdos='"+cdos+"' and comp_id.aamm='"+periode+"' and comp_id.nbul="+bulletin;
//			oSession.createQuery(deleteQuery).executeUpdate();
			deleteQuery = "Delete From HistoElementSalaire where idEntreprise='"+cdos+"' and aamm='"+periode+"' and nbul="+bulletin;
			oSession.createSQLQuery(deleteQuery).executeUpdate();
			
//			l = service.find("from Rhprubrique where comp_id.cdos = '" + cdos + "'");
//			Rhprubrique rubrique = null;
//			Rhthrubq hrubrique = null;
//			i = 0;
//			for (Object object2 : l)
//			{
//				i++;
//				rubrique = (Rhprubrique) object2;
//				hrubrique = new Rhthrubq();
//				//hrubrique = (Rhthrubq) ObjUpdate(hrubrique, rubrique);
//				BeanUtils.copyProperties(rubrique, hrubrique, new String[]{"comp_id"});
//				hrubrique.setComp_id(new RhthrubqPK(cdos, rubrique.getComp_id().getCrub(), periode, bulletin));
//				//			hrubrique.getComp_id().setAamm(periode);
//				//			hrubrique.getComp_id().setCdos(cdos);
//				//			hrubrique.getComp_id().setCrub(rubrique.getComp_id().getCrub());
//				//			hrubrique.getComp_id().setNbul(bulletin);
//				//
//				//			ClsObjectUtil.displayClassProperties(hrubrique.getClass(), hrubrique);
//				oSession.save(hrubrique);
//				if (i % 20 == 0)
//				{//20, m�me taille que la taille du pacquet JDBC
//					//flush un pacquet d'insertion et lib�re la m�moire
//					oSession.flush();
//					oSession.clear();
//				}
//			}

			
			//
					queryInsert = "insert into HistoElementSalaire(id, creation_date, last_modified_date, abat, abmx, addf, age1, age2, ajnu, ajus, algo, apcf, arro, avn, basc, basp, bul1, bul2, " +
							"bul3, bul4, cabf, cais, calc, cat1, cat2, cbulf, cfon, clas1, clas2, clas3, clas4, cle1, cle2, comp, cper, cr01, cr02, cr03, cr04, " +
							"cr05, crub, cs1, cs2, cs3, de01, de02, de03, de04, de05, de06, de07, de08, de09, de10, de11, de12, de13, de14, de15, de16, de17, de18, " +
							"de19, de20, dnbp, ecar, edbbu, edcum, eddf, ednul, egu1, egu2, egus, epbul, esat, exo, fbas, fon1, fon2, fon3, fon4, fon5, fon6, fon7, " +
							"fon8, formule, freq, gra1, gra2, gra3, gra4, gra5, gra6, gra7, gra8, hif1, hif2, hif3, hif4, identreprise, inf1, inf2, infs, lbtm, lrub, " +
							"moi1, moi2, moi3, moi4, mopa, nat1, nat2, nbe1, nbe2, niv11, niv12, niv13, niv14, niv21, niv22, niv23, niv24, niv31, niv32, niv33, niv34,  " +
							"note, nutm, opfi, pcab, pdap, perc, pmax, pmin, ppas, ppcg, prac, prbul, prcl, prhr, prno, pror, prtb, prtm, rcon, reg1, reg2, reg3, reg4, " +
							"reg5, reg6, reg7, reg8, resl, rman, rreg, sexe, sit1, sit2, sit3, sit4, snet, sup1, sup2, sups, synd, tabl, tbas, toum, trcu, trtc, trve, " +
							"txmt, tyc1, tyc2, tyc3, tyc4, tyc5, tyc6, tyc7, tyc8, typr, val1, val2, val3, zca1, zca2, zca3, zca4, zl11, zl12, zl21, zl22, aamm, nbul)" +
						"select id, creation_date, last_modified_date, abat, abmx, addf, age1, age2, ajnu, ajus, algo, apcf, arro, avn, basc, basp, bul1, bul2, " +
							"bul3, bul4, cabf, cais, calc, cat1, cat2, cbulf, cfon, clas1, clas2, clas3, clas4, cle1, cle2, comp, cper, cr01, cr02, cr03, cr04, " +
							"cr05, crub, cs1, cs2, cs3, de01, de02, de03, de04, de05, de06, de07, de08, de09, de10, de11, de12, de13, de14, de15, de16, de17, de18, " +
							"de19, de20, dnbp, ecar, edbbu, edcum, eddf, ednul, egu1, egu2, egus, epbul, esat, exo, fbas, fon1, fon2, fon3, fon4, fon5, fon6, fon7, " +
							"fon8, formule, freq, gra1, gra2, gra3, gra4, gra5, gra6, gra7, gra8, hif1, hif2, hif3, hif4, identreprise, inf1, inf2, infs, lbtm, lrub, " +
							"moi1, moi2, moi3, moi4, mopa, nat1, nat2, nbe1, nbe2, niv11, niv12, niv13, niv14, niv21, niv22, niv23, niv24, niv31, niv32, niv33, niv34,  " +
							"note, nutm, opfi, pcab, pdap, perc, pmax, pmin, ppas, ppcg, prac, prbul, prcl, prhr, prno, pror, prtb, prtm, rcon, reg1, reg2, reg3, reg4, " +
							"reg5, reg6, reg7, reg8, resl, rman, rreg, sexe, sit1, sit2, sit3, sit4, snet, sup1, sup2, sups, synd, tabl, tbas, toum, trcu, trtc, trve, " +
							"txmt, tyc1, tyc2, tyc3, tyc4, tyc5, tyc6, tyc7, tyc8, typr, val1, val2, val3, zca1, zca2, zca3, zca4, zl11, zl12, zl21, zl22, '" + periode + "', " + bulletin + " from ElementSalaire" +
						" where idEntreprise = '" + cdos + "'";
					oSession.createSQLQuery(queryInsert).executeUpdate();
			//		com.cdi.deltarh.service.ClsParameter.println("...Nommbre d'�l�ments ins�r�s dans Rhthrubq: " + createdEntities);
			//
			//		   OPEN C_PARBQBAS;
			//		   LOOP
			//		       FETCH C_PARBQBAS INTO wrbqbas;
			//		       EXIT WHEN C_PARBQBAS%NOTFOUND;
			//
			//		       INSERT INTO pahrbqbas
			//		              VALUES(wrbqbas.cdos,wrbqbas.crub,wrbqbas.nume,wrbqbas.sign,wrbqbas.rubk,w_aamm,w_nbul);
			//
			//		   END LOOP;
			//		   CLOSE C_PARBQBAS;
			
//			deleteQuery = "Delete From Rhthrbqba where comp_id.cdos='"+cdos+"' and comp_id.aamm='"+periode+"' and comp_id.nbul="+bulletin;
//			oSession.createQuery(deleteQuery).executeUpdate();
			deleteQuery = "Delete From HistoElementSalaireBase where idEntreprise='"+cdos+"' and aamm='"+periode+"' and nbul="+bulletin;
			oSession.createSQLQuery(deleteQuery).executeUpdate();
			
//			l = service.find("from Rhprubriquebase where comp_id.cdos = '" + cdos + "'");
//			Rhprubriquebase rubriquebase = null;
//			Rhthrbqba hrubriquebase = null;
//			i = 0;
//			for (Object object2 : l)
//			{
//				i++;
//				rubriquebase = (Rhprubriquebase) object2;
//				hrubriquebase = new Rhthrbqba();
//				hrubriquebase.setComp_id(new RhthrbqbaPK(rubriquebase.getComp_id().getCdos(), rubriquebase.getComp_id().getCrub(), rubriquebase.getComp_id().getNume(), periode, bulletin));
//				hrubriquebase.setRubk(rubriquebase.getRubk());
//				hrubriquebase.setSign(rubriquebase.getSign());
//				//
//				oSession.save(hrubriquebase);
//				if (i % 20 == 0)
//				{//20, m�me taille que la taille du pacquet JDBC
//					//flush un pacquet d'insertion et lib�re la m�moire
//					oSession.flush();
//					oSession.clear();
//				}
//			}

			queryInsert = "insert into HistoElementSalaireBase(id, creation_date, last_modified_date, idEntreprise, CRUB, NUME, SIGN, RUBK,AAMM, NBUL)" +
				"select id, creation_date, last_modified_date, idEntreprise, CRUB, NUME, SIGN, RUBK, '" + periode + "', " + bulletin + " from ElementSalaireBase" +
				" where idEntreprise = '" + cdos + "'";
			oSession.createSQLQuery(queryInsert).executeUpdate();
			//
			//		   OPEN C_PARUBBAREM;
			//		   LOOP
			//		      FETCH C_PARUBBAREM INTO wbarem;
			//		      EXIT WHEN C_PARUBBAREM%NOTFOUND;
			//
			//		      INSERT INTO pahrubbarem
			//		             VALUES(wbarem.cdos,wbarem.crub,wbarem.nume,wbarem.val1,wbarem.val2,wbarem.taux,wbarem.mont,w_aamm,w_nbul);
			//
			//		   END LOOP;
			//		   CLOSE C_PARUBBAREM;

//			deleteQuery = "Delete From Rhthrubbarem where comp_id.cdos='"+cdos+"' and comp_id.aamm='"+periode+"' and comp_id.nbul="+bulletin;
//			oSession.createQuery(deleteQuery).executeUpdate();
			deleteQuery = "Delete From HistoElementSalaireBareme where idEntreprise='"+cdos+"' and aamm='"+periode+"' and nbul="+bulletin;
			oSession.createSQLQuery(deleteQuery).executeUpdate();
			
//			l = service.find("from Rhprubriquebareme where comp_id.cdos = '" + cdos + "'");
//			Rhprubriquebareme rubriquebareme = null;
//			Rhthrubbarem hrubriquebareme = null;
//			i = 0;
//			for (Object object2 : l)
//			{
//				i++;
//				rubriquebareme = (Rhprubriquebareme) object2;
//				hrubriquebareme = new Rhthrubbarem();
//				hrubriquebareme.setComp_id(new RhthrubbaremPK(rubriquebareme.getComp_id().getCdos(), rubriquebareme.getComp_id().getCrub(), rubriquebareme.getComp_id().getNume(), periode, bulletin));
//				hrubriquebareme.setVal1(rubriquebareme.getVal1());
//				hrubriquebareme.setVal2(rubriquebareme.getVal2());
//				hrubriquebareme.setMont(rubriquebareme.getMont());
//				hrubriquebareme.setTaux(rubriquebareme.getTaux());
//				//
//				oSession.save(hrubriquebareme);
//				if (i % 20 == 0)
//				{//20, m�me taille que la taille du pacquet JDBC
//					//flush un pacquet d'insertion et lib�re la m�moire
//					oSession.flush();
//					oSession.clear();
//				}
//			}

			queryInsert = "insert into HistoElementSalaireBareme(id, creation_date, last_modified_date, idEntreprise, CRUB, NUME, VAL1, VAL2, TAUX, MONT,AAMM, NBUL)" +
				"select id, creation_date, last_modified_date, idEntreprise, CRUB, NUME, VAL1, VAL2, TAUX, MONT, '" + periode + "', " + bulletin + " from ElementSalaireBareme" +
				" where idEntreprise = '" + cdos + "'";
			oSession.createSQLQuery(queryInsert).executeUpdate();
			//
			//		   --- elements fixes
			//		   w_cas := 'H3';
			//		   w_mess := 'Historiques des �l�ments fixes...';
			cas = "H3";
			message = "Historiques des �l�ments fixes...";
			globalUpdate._setEvolutionTraitement(request, message, false);
			//		   DBMS_PIPE.PACK_MESSAGE(w_cas);
			//		   DBMS_PIPE.PACK_MESSAGE(w_mess);
			//		   w_status := DBMS_PIPE.SEND_MESSAGE(w_pipe_name);
			//
			//		   OPEN C_PAELFIX;
			//		   LOOP
			//		      FETCH C_PAELFIX INTO waelfix;
			//		      EXIT WHEN C_PAELFIX%NOTFOUND;
			//
			//		      INSERT INTO pahelfix
			//		             VALUES(waelfix.cdos,waelfix.nmat,waelfix.codp,waelfix.monp,w_aamm,w_nbul);
			//		   END LOOP;
			//		   CLOSE C_PAELFIX;

//			deleteQuery = "Delete From Rhthelfix where comp_id.cdos='"+cdos+"' and comp_id.aamm='"+periode+"' and comp_id.nbul="+bulletin;
//			oSession.createQuery(deleteQuery).executeUpdate();
			deleteQuery = "Delete From HistoElementFixeSalaire where idEntreprise='"+cdos+"' and aamm='"+periode+"' and nbul="+bulletin;
			oSession.createSQLQuery(deleteQuery).executeUpdate();
			
//			l = service.find("from Rhteltfixagent where comp_id.cdos = '" + cdos + "'");
//			Rhteltfixagent elementfixe = null;
//			Rhthelfix helementfixe = null;
//			i = 0;
//			for (Object object2 : l)
//			{
//				i++;
//				elementfixe = (Rhteltfixagent) object2;
//				helementfixe = new Rhthelfix();
//				helementfixe.setComp_id(new RhthelfixPK(elementfixe.getComp_id().getCdos(), elementfixe.getComp_id().getNmat(), elementfixe.getComp_id().getCodp(), periode, bulletin));
//				helementfixe.setMonp(elementfixe.getMonp());
//				//
//				oSession.save(helementfixe);
//				if (i % 20 == 0)
//				{//20, m�me taille que la taille du pacquet JDBC
//					//flush un pacquet d'insertion et lib�re la m�moire
//					oSession.flush();
//					oSession.clear();
//				}
//			}

			queryInsert = "insert into HistoElementFixeSalaire(id, creation_date, last_modified_date, idEntreprise, NMAT, CODP, MONP,AAMM, NBUL)" +
				"select id, creation_date, last_modified_date, idEntreprise, NMAT, CODP, MONP, '" + periode + "', " + bulletin + " from ElementFixeSalaire" +
				" where idEntreprise = '" + cdos + "'";
			oSession.createSQLQuery(queryInsert).executeUpdate();
			//
			//
			//		   ------ elements variables
			//		   w_cas := 'H4';
			//		   w_mess := 'Historiques des �l�ments variables...';
			cas = "H3";
			message = "Historiques des �l�ments variables...";
			globalUpdate._setEvolutionTraitement(request, message, false);
			//		   DBMS_PIPE.PACK_MESSAGE(w_cas);
			//		   DBMS_PIPE.PACK_MESSAGE(w_mess);
			//		   w_status := DBMS_PIPE.SEND_MESSAGE(w_pipe_name);
			//		   OPEN C_PAEV;
			//		   LOOP
			//		      FETCH C_PAEV INTO waev;
			//		      EXIT WHEN C_PAEV%NOTFOUND;
			//
			//		      INSERT INTO pahev (CDOS,AAMM,NMAT,NBUL,DDPA,DFPA,BCMO)
			//		             VALUES(waev.cdos,w_aamm,waev.nmat,w_nbul,waev.ddpa,waev.dfpa,waev.bcmo);
			//		   END LOOP;
			//		   CLOSE C_PAEV;

//			deleteQuery = "Delete From Rhthev where comp_id.cdos='"+cdos+"' and comp_id.aamm='"+periode+"' and comp_id.nbul="+bulletin;
//			oSession.createQuery(deleteQuery).executeUpdate();
			deleteQuery = "Delete From HistoElementVariableEnteteMois where idEntreprise='"+cdos+"' and aamm='"+periode+"' and nbul="+bulletin;
			oSession.createSQLQuery(deleteQuery).executeUpdate();
			
			String insertQuery = "Insert Into HistoElementVariableEnteteMois(id, creation_date, last_modified_date,idEntreprise,aamm,nmat,nbul,ddpa,dfpa,bcmo) "+
								"Select id, creation_date, last_modified_date,idEntreprise,aamm,nmat,nbul,ddpa,dfpa,bcmo from ElementVariableEnteteMois where idEntreprise = '" + cdos + "' and aamm = '" + periode + "' and nbul = " + bulletin;
			oSession.createSQLQuery(insertQuery).executeUpdate();
			
//			l = service.find("from Rhteltvarent where comp_id.cdos = '" + cdos + "' and comp_id.aamm = '" + periode + "' and comp_id.nbul = " + bulletin);
//			Rhteltvarent elementvarent = null;
//			Rhthev helementvarent = null;
//			i = 0;
//			for (Object object2 : l)
//			{
//				i++;
//				elementvarent = (Rhteltvarent) object2;
//				helementvarent = new Rhthev();
//				helementvarent.setComp_id(new RhthevPK(elementvarent.getComp_id().getCdos(), elementvarent.getComp_id().getAamm(), elementvarent.getComp_id().getNmat(), bulletin));
//				helementvarent.setBcmo(elementvarent.getBcmo());
//				helementvarent.setDdpa(elementvarent.getDdpa());
//				helementvarent.setDfpa(elementvarent.getDfpa());
//				//
//				oSession.save(helementvarent);
//				if (i % 20 == 0)
//				{//20, m�me taille que la taille du pacquet JDBC
//					//flush un pacquet d'insertion et lib�re la m�moire
//					oSession.flush();
//					oSession.clear();
//				}
//			}

			
			//
			//		   OPEN C_PAEVAR;
			//		   LOOP
			//		      FETCH C_PAEVAR INTO waevar;
			//		      EXIT WHEN C_PAEVAR%NOTFOUND;
			//
			//		      INSERT INTO pahevar (CDOS,AAMM,NMAT,NBUL,RUBQ,ARGU,NPRT,RUBA,MONT,CUTI)
			//		                    VALUES(waevar.cdos,w_aamm,waevar.nmat,w_nbul,waevar.rubq,waevar.argu,waevar.nprt,waevar.ruba,waevar.mont,waevar.cuti);
			//		   END LOOP;
			//		   CLOSE C_PAEVAR;

//			deleteQuery = "Delete From Rhthevar where cdos='"+cdos+"' and aamm='"+periode+"' and nbul="+bulletin;
//			oSession.createQuery(deleteQuery).executeUpdate();
			deleteQuery = "Delete From HistoElementVariableDetailMois where idEntreprise='"+cdos+"' and aamm='"+periode+"' and nbul="+bulletin;
			oSession.createSQLQuery(deleteQuery).executeUpdate();
			
			insertQuery = "Insert Into HistoElementVariableDetailMois(id, creation_date, last_modified_date, idEntreprise,aamm,nmat,nbul,rubq,argu,nprt,ruba,mont,cuti) Select id, creation_date, last_modified_date, idEntreprise,aamm,nmat,nbul,rubq,argu,nprt,ruba,mont,cuti from ElementVariableDetailMois where idEntreprise = '" + cdos + "' and aamm = '" + periode + "' and nbul = " + bulletin;
			oSession.createSQLQuery(insertQuery).executeUpdate();
			
//			l = service.find("from Rhteltvardet where comp_id.cdos = '" + cdos + "' and aamm = '" + periode + "' and comp_id.nbul = " + bulletin);
//			Rhteltvardet elementvardet = null;
//			Rhthevar helementvardet = null;
//			i = 0;
//			String helementvardetInsertQuery="INSERT INTO Rhthevar (CDOS,AAMM,NMAT,NBUL,RUBQ,ARGU,NPRT,RUBA,MONT,CUTI) VALUES(";
//			helementvardetInsertQuery+=":cdos, :aamm, :nmat, :nbul, :rubq, :argu, :nprt, :ruba, :mont, :cuti)";
//			Query q = oSession.createSQLQuery(helementvardetInsertQuery);
//			for (Object object2 : l)
//			{
//				i++;
//				elementvardet = (Rhteltvardet) object2;
//				
//				q.setParameter("cdos", elementvardet.getComp_id().getCdos(), Hibernate.STRING);
//				q.setParameter("aamm", periode, Hibernate.STRING);
//				q.setParameter("nmat", elementvardet.getComp_id().getNmat(), Hibernate.STRING);
//				q.setParameter("nbul", elementvardet.getComp_id().getNbul(), Hibernate.INTEGER);
//				q.setParameter("rubq", elementvardet.getRubq(), Hibernate.STRING);
//				q.setParameter("argu", elementvardet.getArgu(), Hibernate.STRING);
//				q.setParameter("nprt", elementvardet.getNprt(), Hibernate.STRING);
//				q.setParameter("ruba", elementvardet.getRuba(), Hibernate.STRING);
//				q.setParameter("mont", elementvardet.getMont(), Hibernate.BIG_DECIMAL);
//				q.setParameter("cuti", elementvardet.getCuti(), Hibernate.STRING);
//				q.executeUpdate();
//				if (i % 20 == 0)
//				{//20, m�me taille que la taille du pacquet JDBC
//					//flush un pacquet d'insertion et lib�re la m�moire
//					oSession.flush();
//					oSession.clear();
//				}
//				/*
//				helementvardet = new Rhthevar();
//				helementvardet.setAamm(periode);
//				helementvardet.setArgu(elementvardet.getArgu());
//				helementvardet.setCdos(elementvardet.getComp_id().getCdos());
//				helementvardet.setNbul(elementvardet.getComp_id().getNbul());
//				helementvardet.setNmat(elementvardet.getComp_id().getNmat());
//				helementvardet.setCuti(elementvardet.getCuti());
//				helementvardet.setMont(elementvardet.getMont());
//				helementvardet.setNprt(elementvardet.getNprt());
//				helementvardet.setRuba(elementvardet.getRuba());
//				helementvardet.setRubq(elementvardet.getRubq());
//				//
//				oSession.save(helementvardet);
//				if (i % 20 == 0)
//				{//20, m�me taille que la taille du pacquet JDBC
//					//flush un pacquet d'insertion et lib�re la m�moire
//					//oSession.flush();
//					//oSession.clear();
//				}
//				*/
//			}

			
			//
			//		   OPEN C_PAEVCG;
			//		   LOOP
			//		      FETCH C_PAEVCG INTO waevcg;
			//		      EXIT WHEN C_PAEVCG%NOTFOUND;
			//
			//		      INSERT INTO pahevcg (CDOS,AAMM,NMAT,NBUL,DDEB,DFIN,NBJC,NBJA,MOTF,MONT,CUTI)
			//		                    VALUES(waevcg.cdos,w_aamm,waevcg.nmat,w_nbul,waevcg.ddeb,waevcg.dfin,waevcg.nbjc,waevcg.nbja,waevcg.motf,waevcg.mont,waevcg.cuti);
			//		   END LOOP;
			//		   CLOSE C_PAEVCG;

//			deleteQuery = "Delete From Rhthevcg where comp_id.cdos='"+cdos+"' and comp_id.aamm='"+periode+"' and comp_id.nbul="+bulletin;
//			oSession.createQuery(deleteQuery).executeUpdate();
			deleteQuery = "Delete From HistoElementVariableConge where idEntreprise='"+cdos+"' and aamm='"+periode+"' and nbul="+bulletin;
			oSession.createSQLQuery(deleteQuery).executeUpdate();
			
			insertQuery = "Insert Into HistoElementVariableConge(id, creation_date, last_modified_date, idEntreprise,aamm,nmat,nbul,ddeb, dfin, nbjc, nbja, motf, mont, cuti) Select id, creation_date, last_modified_date, idEntreprise,aamm,nmat,nbul,ddeb, dfin, nbjc, nbja, motf, mont, cuti from ElementVariableConge where idEntreprise = '" + cdos + "' and aamm = '" + periode + "' and nbul = " + bulletin;
			oSession.createSQLQuery(insertQuery).executeUpdate();
			
//			l = service.find("from Rhteltvarconge where comp_id.cdos = '" + cdos + "' and comp_id.aamm = '" + periode + "' and comp_id.nbul = " + bulletin);
//			Rhteltvarconge elementvarconge = null;
//			Rhthevcg helementvarconge = null;
//			i = 0;
//			for (Object object2 : l)
//			{
//				i++;
//				elementvarconge = (Rhteltvarconge) object2;
//				helementvarconge = new Rhthevcg();
//				helementvarconge.setComp_id(new RhthevcgPK(elementvarconge.getComp_id().getCdos(), elementvarconge.getComp_id().getAamm(), elementvarconge.getComp_id().getNmat(), elementvarconge.getComp_id().getNbul(), elementvarconge.getComp_id().getDdeb()));
//				helementvarconge.setCuti(elementvarconge.getCuti());
//				helementvarconge.setDfin(elementvarconge.getDfin());
//				helementvarconge.setMont(elementvarconge.getMont());
//				helementvarconge.setMotf(elementvarconge.getMotf());
//				helementvarconge.setNbja(elementvarconge.getNbja());
//				helementvarconge.setNbjc(elementvarconge.getNbjc());
//				//
//				//ClsParameter.println("---------->Saving evcg");
//				oSession.save(helementvarconge);
//				if (i % 20 == 0)
//				{//20, m�me taille que la taille du pacquet JDBC
//					//flush un pacquet d'insertion et lib�re la m�moire
//					oSession.flush();
//					oSession.clear();
//				}
//			}

			
			//
			//		   ------ salaries
			//		   w_cas := 'H5';
			//		   w_mess := 'Historiques des salaries...';
			cas = "H5";
			message = "Historiques des salaries...";
			
			/**
			 * On va g�rer deux tables d'historiques (RHTHAGENT et RHTSAL)
			 * RHTHAGENT a la m�me structure ques les autres tables d'historique
			 */
			
//			String histoSal = ClsConfigurationParameters.getConfigParameterValue(service, cdos, null, ClsConfigurationParameters.HISTORISATION_RHPAGENT);
//			if(StringUtils.equals("O", histoSal))
//			{
				deleteQuery = "Delete From HistoSalarie where identreprise='"+cdos+"' and aamm='"+periode+"' and nbul="+bulletin;
				oSession.createSQLQuery(deleteQuery).executeUpdate();
				
				insertQuery = "Insert Into HistoSalarie(id, creation_date, last_modified_date, adr1, adr2, adr3, adr4, afec, avn1, avn2, avn3, avn4, avn5, avn6, avn7, banq, bpos, bqso, cals, cat, ccpt, clas," +
						"cle, codeposte, codesite, codf, cods, comm, comp, cont, ctat, dapa, dapec, dchf, dchg, ddca, ddcf, dded, ddenv, decc, dels, depr, devp, dfcf, dfes, dmrr," +
						"dmst, drenv, drtcg, dtes, dtit, dtna, ech, equi, fonc, grad, guic, hifo, identreprise, indi, japa, japec, jded, jrla, jrlec, lemb, lieu, lnai, mchg, modp," +
						"mrrx, mtcf, mtfr, nato, nbcj, nbec, nbef, nbfe, nbjaf, nbjcf, nbjsa, nbjse, nbjsm, nbjtr, nbpt, nifo, niv1, niv2, niv3, nmat, nmjf, nom, noss, note, npie," +
						"ntel, pbpe, pmcf, pnai, pnet, pren, regi, sana, sexe, sitf, snet, stor, synd, tinp, tits, tyfo1, tyfo2, typc, vild, zli1, zli10, zli2, zli3, zli4, zli5," +
						"zli6, zli7, zli8, zli9, zres, photo, aamm, nbul) "+
						"Select id, creation_date, last_modified_date, adr1, adr2, adr3, adr4, afec, avn1, avn2, avn3, avn4, avn5, avn6, avn7, banq, bpos, bqso, cals, cat, ccpt, clas," +
						"cle, codeposte, codesite, codf, cods, comm, comp, cont, ctat, dapa, dapec, dchf, dchg, ddca, ddcf, dded, ddenv, decc, dels, depr, devp, dfcf, dfes, dmrr," +
						"dmst, drenv, drtcg, dtes, dtit, dtna, ech, equi, fonc, grad, guic, hifo, identreprise, indi, japa, japec, jded, jrla, jrlec, lemb, lieu, lnai, mchg, modp," +
						"mrrx, mtcf, mtfr, nato, nbcj, nbec, nbef, nbfe, nbjaf, nbjcf, nbjsa, nbjse, nbjsm, nbjtr, nbpt, nifo, niv1, niv2, niv3, nmat, nmjf, nom, noss, note, npie," +
						"ntel, pbpe, pmcf, pnai, pnet, pren, regi, sana, sexe, sitf, snet, stor, synd, tinp, tits, tyfo1, tyfo2, typc, vild, zli1, zli10, zli2, zli3, zli4, zli5," +
						"zli6, zli7, zli8, zli9, zres, photo,'"+periode+"',"+bulletin+" From Salarie a where identreprise ='"+cdos+"'";
				oSession.createSQLQuery(insertQuery).executeUpdate();
				
//				l = service.find("from Rhpagent where comp_id.cdos = '" + cdos + "'");
//				Rhpagent agent = null;
//				Rhthagent hagent = null;
//				i = 0;
//				for (Object object2 : l)
//				{
//					i++;
//					agent = (Rhpagent) object2;
//					hagent = new Rhthagent();
//					BeanUtils.copyProperties(agent, hagent, new String[]{"comp_id"});
//					hagent.setComp_id(new RhthagentPK(cdos, agent.getComp_id().getNmat(), periode, bulletin));
//					oSession.save(hagent);
//					if (i % 20 == 0)
//					{
//						oSession.flush();
//						oSession.clear();
//					}
//				}
			//}
			
			
//			String histoZL = ClsConfigurationParameters.getConfigParameterValue(service, cdos, null, ClsConfigurationParameters.HISTORISATION_RHTZONELIBRE);
//			if(StringUtils.equals("O", histoZL))
//			{
////				deleteQuery = "Delete From Rhthzonelibre where comp_id.cdos='"+cdos+"' and comp_id.aamm='"+periode+"' and comp_id.nbul="+bulletin;
////				oSession.createQuery(deleteQuery).executeUpdate();
//				deleteQuery = "Delete From Rhthzonelibre where cdos='"+cdos+"' and aamm='"+periode+"' and nbul="+bulletin;
//				oSession.createSQLQuery(deleteQuery).executeUpdate();
//				String strQ = "insert into Rhthzonelibre(cdos,nmat,numerozl,vallzl,valmzl,valtzl,valdzl,aamm,nbul) ";
//				strQ += "select cdos,nmat,numerozl,vallzl,valmzl,valtzl,valdzl,'"+periode+"',"+bulletin+" From Rhtzonelibre where cdos='"+cdos+"'";
//				oSession.createSQLQuery(strQ).executeUpdate();
////				oSession.flush();
////				oSession.clear();
//			}
			
//			String tableRhthagentexist =  ClsConfigurationParameters.getConfigParameterValue(service, cdos, globalUpdate.getLangue(), ClsConfigurationParameters.RHTHAGENT_EXIST);
//			if(StringUtils.equals("O", tableRhthagentexist))
//			{
//				deleteQuery = "Delete From Rhthagent where cdos='"+cdos+"' and aamm='"+periode+"' and nbul="+bulletin;
//				oSession.createSQLQuery(deleteQuery).executeUpdate();
//				
//				l = service.find("from Rhpagent where cdos = '" + cdos + "'");
//				Rhteltvarconge elementvarconge = null;
//				Rhthevcg helementvarconge = null;
//				i = 0;
//				for (Object object2 : l)
//				{
//					i++;
//					elementvarconge = (Rhteltvarconge) object2;
//					helementvarconge = new Rhthevcg();
//					helementvarconge.setComp_id(new RhthevcgPK(elementvarconge.getComp_id().getCdos(), elementvarconge.getComp_id().getAamm(), elementvarconge.getComp_id().getNmat(), elementvarconge.getComp_id().getNbul(), elementvarconge.getComp_id().getDdeb()));
//					helementvarconge.setCuti(elementvarconge.getCuti());
//					helementvarconge.setDfin(elementvarconge.getDfin());
//					helementvarconge.setMont(elementvarconge.getMont());
//					helementvarconge.setMotf(elementvarconge.getMotf());
//					helementvarconge.setNbja(elementvarconge.getNbja());
//					helementvarconge.setNbjc(elementvarconge.getNbjc());
//					//
//					oSession.save(helementvarconge);
//					if (i % 20 == 0)
//					{
//						oSession.flush();
//						oSession.clear();
//					}
//				}
//			}
			globalUpdate._setEvolutionTraitement(request, message, false);
			//		   DBMS_PIPE.PACK_MESSAGE(w_cas);
			//		   DBMS_PIPE.PACK_MESSAGE(w_mess);
			//		   w_status := DBMS_PIPE.SEND_MESSAGE(w_pipe_name);
			//		   OPEN C_PASA01;


			message = "G�n�ration des cumuls ...";
			globalUpdate._setEvolutionTraitement(request, message, false);
			//
			//
			//		   ----- cumul retro
			//		   OPEN C_PACALC;
			//		   LOOP
			//		      FETCH C_PACALC INTO wcalc;
			//		      EXIT WHEN C_PACALC%NOTFOUND;
			//
			//		      INSERT INTO prcumu (CDOS,NMAT,AAMM,RUBQ,NBUL,BASC,BASP,TAUX,MONT)
			//		                   VALUES(wcalc.cdos,wcalc.nmat,w_aamm,wcalc.rubq,w_nbul,wcalc.basc,wcalc.basp,wcalc.taux,wcalc.mont);
			//		   END LOOP;
			//		   CLOSE C_PACALC;
			//
			//
			//		   		  CURSOR C_PACALC IS
			//		   		      SELECT CDOS,NMAT,AAMM,RUBQ,NBUL,SUM(NVL(BASC,0)),SUM(NVL(BASP,0)),SUM(NVL(TAUX,0)),SUM(NVL(MONT,0)) FROM pacalc
			//		   		       WHERE cdos = w_cdos
			//		   		         AND aamm = w_aamm
			//		   		         AND nbul = w_nbul
			//		   		        GROUP BY CDOS,NMAT,AAMM,RUBQ,NBUL;

//			deleteQuery = "Delete From Rhtprcumu where comp_id.cdos='"+cdos+"' and comp_id.aamm='"+periode+"' and comp_id.nbul="+bulletin;
//			oSession.createQuery(deleteQuery).executeUpdate();
//			deleteQuery = "Delete From Rhtprcumu where cdos='"+cdos+"' and aamm='"+periode+"' and nbul="+bulletin;
//			oSession.createSQLQuery(deleteQuery).executeUpdate();
//
//			insertQuery = "Insert Into Rhtprcumu select cdos, nmat, aamm, rubq, nbul, sum(nvl(basc,0)), sum(nvl(basp,0)), sum(nvl(taux,0)) ";
//			insertQuery+= ", sum(nvl(mont,0))  from Rhtcalcul where cdos = '" + cdos + "' and aamm = '" + periode + "' and nbul = " + bulletin + " group by cdos, nmat, aamm, rubq, nbul ";
//			oSession.createSQLQuery(insertQuery).executeUpdate();
//			oSession.flush();
//			oSession.clear();
			
//			String histoCal = ClsConfigurationParameters.getConfigParameterValue(service, cdos, null, ClsConfigurationParameters.HISTORISATION_RHTCALCUL);
//			if(StringUtils.equals("O", histoCal))
//			{
				message = "G�n�ration des cumuls ...";
				globalUpdate._setEvolutionTraitement(request, message, false);
//				deleteQuery = "Delete From Rhthcalcul where comp_id.cdos='"+cdos+"' and comp_id.aamm='"+periode+"' and comp_id.nbul="+bulletin;
//				oSession.createQuery(deleteQuery).executeUpdate();
				deleteQuery = "Delete From HistoCalculPaie where idEntreprise='"+cdos+"' and aamm='"+periode+"' and nbul="+bulletin;
				oSession.createSQLQuery(deleteQuery).executeUpdate();
				
				insertQuery = "Insert Into HistoCalculPaie(id, creation_date, last_modified_date, aamm, argu, basc, basp, clas, identreprise, mont, nbul, nmat, nprt, ruba, rubq, taux, trtb, nlig) "+
						"select id, creation_date, last_modified_date, aamm, argu, basc, basp, clas, identreprise, mont, nbul, nmat, nprt, ruba, rubq, taux, trtb, nlig from CalculPaie where idEntreprise = '" + cdos + "' and aamm = '" + periode + "' and nbul = " + bulletin;
				oSession.createSQLQuery(insertQuery).executeUpdate();
//				oSession.flush();
//				oSession.clear();
			//}
			
//			String queryCalcul = "select comp_id.cdos, comp_id.nmat, comp_id.aamm, comp_id.rubq, comp_id.nbul, sum(case basc when null then 0 else basc end), sum(case basp when null then 0 else basp end), sum(case taux when null then 0 else taux end)"
//					+ ", sum(case mont when null then 0 else mont end)  from Rhtcalcul where cdos = '" + cdos + "' and aamm = '" + periode + "' and nbul = " + bulletin + " group by comp_id.cdos, comp_id.nmat, comp_id.aamm, comp_id.rubq, comp_id.nbul";
			
//			String queryCalcul = "select comp_id.cdos, comp_id.nmat, comp_id.aamm, comp_id.rubq, comp_id.nbul, sum(nvl(basc,0)), sum(nvl(basp,0)), sum(nvl(taux,0))"
//				+ ", sum(nvl(mont,0))  from Rhtcalcul where cdos = '" + cdos + "' and aamm = '" + periode + "' and nbul = " + bulletin + " group by comp_id.cdos, comp_id.nmat, comp_id.aamm, comp_id.rubq, comp_id.nbul";
//			l = service.find(queryCalcul);
//			Object[] calcul = null;
//			Rhtprcumu cumul = null;
//			i = 0;
//			for (Object object : l)
//			{
//				i++;
//				calcul = (Object[]) object;
//				cumul = new Rhtprcumu();
//				cumul.setComp_id(new RhtprcumuPK(cdos, (String) calcul[1], (String) calcul[2], (String) calcul[3], calcul[4] != null ? ((Integer) calcul[4]).intValue() : null));
//				cumul.setBasc(new BigDecimal(calcul[5].toString()));
//				cumul.setBasp(new BigDecimal(calcul[6].toString()));
//				cumul.setTaux(new BigDecimal(calcul[7].toString()));
//				cumul.setMont(new BigDecimal(calcul[8].toString()));
//				//
//				//ClsParameter.println("---------->Saving cumul");
//				oSession.save(cumul);
//				if (i % 20 == 0)
//				{//20, m�me taille que la taille du pacquet JDBC
//					//flush un pacquet d'insertion et lib�re la m�moire
//					oSession.flush();
//					oSession.clear();
//				}
//			}

			
			//
			//		   COMMIT;
			//
			//		   w_cas := 'F';
			//		   w_mess := '';
			//		   DBMS_PIPE.PACK_MESSAGE(w_cas);
			//		   DBMS_PIPE.PACK_MESSAGE(w_mess);
			//		   w_status := DBMS_PIPE.SEND_MESSAGE(w_pipe_name);
			//
			//		EXCEPTION
			//		   WHEN OTHERS THEN
			//
			//		      ROLLBACK;
			//		      w_cas := 'E';
			//		      DBMS_PIPE.PACK_MESSAGE(w_cas);
			//		      -- A decommenter pour afficher l'erreur si lancement par script
			//		      --DBMS_OUTPUT.put_line('w_mess = *'|| w_mess || '*');
			//		      w_mess := 'Erreur ' || SUBSTR(SQLERRM, 1, 90);
			//		      DBMS_PIPE.PACK_MESSAGE(w_mess);
			//		      w_status := DBMS_PIPE.SEND_MESSAGE(w_pipe_name);
			//
			//		END gen_hist;
			//		--------------------------------------------------------
			//		END PA_HIST;
			//		--------------------------------------------------------
			//		/
			
			message = "Historiques externes";
			globalUpdate._setEvolutionTraitement(request, message, false);
			//this.historisationExternes(service, oSession, cdos, periode, bulletin);
			
			tx.commit();
//			System.out.println("#############Fin historique des cumuls");
			res = true;
			//tx.commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			globalUpdate._setEvolutionTraitement(request, ClsTreater._getStackTrace(e), true);
			//if(tx!=null) tx.rollback();
			res = false;
			throw e;
		}
		finally
		{
			try
			{
				service.closeSession(oSession);
				/*
				 * if(oSession.isOpen() || oSession.isConnected()){ if(! oSession.connection().isClosed()) oSession.connection().close(); //oSession.flush(); //oSession.clear(); oSession.close(); }
				 */
				//			if(oSession!=null) //oSession.flush();
				//			if(oSession!=null) //oSession.clear();
				//			if(oSession!=null) service.closeConnexion(oSession);
			}
			catch (Exception e)
			{
				// TODO: handle exception
				e.printStackTrace();
				res = false;
				throw e;
			}
		}
		//

		return res;
	}

	/**
	 * update one object with values from another when they have the same structure
	 * 
	 * @param updatee
	 *            the object to be updated
	 * @param param
	 *            the object that has values
	 * @return the updated object
	 * @deprecated : the better method is BeanUtils.copyProperties(source, target) from spring;
	 */

	public Object ObjUpdate(Object updatee, Object param) throws Exception
	{
		//		com.cdi.deltarh.service.ClsParameter.println(">>clone");
		try
		{
			Method[] tabMethodes = updatee.getClass().getMethods();
			Object obj = null;
			for (Method methodSet : tabMethodes)
			{
				if (methodSet.getName().startsWith("set") && (!methodSet.getName().equals("setObject")))
				{
					for (Method methodGet : param.getClass().getMethods())
					{
						if (methodGet.getName().startsWith("get") && (!methodGet.getName().equals("getObject")) && (!methodGet.getName().equals("getComp_id")) && methodGet.getName().substring(1).equals(methodSet.getName().substring(1)))
						{
							obj = methodGet.invoke(param, (Object[]) null);
							methodSet.invoke(updatee, (Object[]) new Object[] { obj });
							break;
						}
						//pour les m�thodes qui commencent par is
						else if (methodGet.getName().startsWith("is") && methodGet.getName().substring(2).equals(methodSet.getName().substring(3)))
						{
							obj = methodGet.invoke(param, (Object[]) null);
							methodSet.invoke(updatee, (Object[]) new Object[] { obj });
							break;
						}
					}
				}
			}
		}
		catch (IllegalAccessException illEx)
		{
			illEx.printStackTrace();
			throw illEx;
		}
		catch (InvocationTargetException invEx)
		{
			invEx.printStackTrace();
			throw invEx;
		}
		return updatee;
	}

}
