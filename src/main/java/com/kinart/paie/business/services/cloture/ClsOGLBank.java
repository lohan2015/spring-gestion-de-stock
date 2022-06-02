package com.kinart.paie.business.services.cloture;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.kinart.paie.business.model.LogMessage;
import com.kinart.paie.business.model.MouvCptPaie;
import com.kinart.paie.business.model.ParamData;
import com.kinart.paie.business.model.TraitementTexte;
import com.kinart.paie.business.services.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ClsOGLBank
{
	private Integer index = 0;
	private MouvCptPaie getRhtmpai(Object[] obj)
	{
		MouvCptPaie mpai = new MouvCptPaie();
		index = 0;
		mpai.setAge((String) obj[index++]);
		mpai.setDev((String) obj[index++]);
		mpai.setCha(null);
		mpai.setNcp((String) obj[index++]);
		mpai.setSuf((String) obj[index++]);
		mpai.setOpe((String) obj[index++]);
		mpai.setMvt(null);
		mpai.setRgp(null);
		mpai.setUti((String) obj[index++]);
		mpai.setEve(null);
		mpai.setClc(null);
		if (obj[index] != null && StringUtils.isNotBlank(obj[index].toString()))
			mpai.setDco((Date) obj[index++]);
		else
			index++;
		mpai.setSer(null);
		if (obj[index] != null && StringUtils.isNotBlank(obj[index].toString()))
			mpai.setDva((Date) obj[index++]);
		else
			index++;
		if (obj[index] != null && StringUtils.isNotBlank(obj[index].toString()))
			mpai.setMon(new BigDecimal(obj[index++].toString()));
		else
			index++;
		mpai.setSen((String) obj[index++]);
		mpai.setLib((String) obj[index++]);
		mpai.setExo(null);
		int curentindex = index++;
		try {
			mpai.setPie((String) obj[curentindex]);
		} catch (Exception e) {
			// TODO: handle exception
			mpai.setPie(obj[curentindex].toString());
		}
		mpai.setRlet((String) obj[index++]);
		mpai.setDes1(null);
		mpai.setDes2(null);
		mpai.setDes3(null);
		mpai.setDes4(null);
		mpai.setDes5(null);
		mpai.setUtf(null);
		mpai.setUta(null);
		mpai.setTau(null);
		mpai.setDin(null);
		mpai.setTpr(null);
		mpai.setNpr(null);
		mpai.setNcc(null);
		mpai.setSuc(null);
		mpai.setEsi(null);
		mpai.setImp(null);
		mpai.setCta(null);
		mpai.setMar(null);
		mpai.setDech(null);
		mpai.setAgsa((String) obj[index++]);
		mpai.setAgem(null);
		mpai.setAgde(null);
		curentindex = index++;
		try {
			mpai.setDevc((String) obj[curentindex]);
		} catch (Exception e) {
			// TODO: handle exception
			mpai.setDevc(obj[curentindex].toString());
		}
		
		if (obj[index] != null && StringUtils.isNotBlank(obj[index].toString()))
			mpai.setMctv(new BigDecimal(obj[index++].toString()));
		else
			index++;
		curentindex = index++;
		try {
			mpai.setPieo((String) obj[curentindex]);
		} catch (Exception e) {
			// TODO: handle exception
			mpai.setPieo(obj[curentindex].toString());
		}
		
		mpai.setIden(null);
		mpai.setNoseq(null);
		
		//si le montant est n�gatif, alors on inverse le sens
		if(mpai.getMon().signum()<0)
		{
			mpai.setMon(BigDecimal.ZERO.subtract(mpai.getMon()));
			mpai.setMctv(BigDecimal.ZERO.subtract(mpai.getMctv()));
			if(StringUtils.equals("C", mpai.getSen()))
				mpai.setSen("D");
			else
				mpai.setSen("C");
		}
		

		return mpai;
	}

	Map<String, Integer> nbrDecimales = new HashMap<String, Integer>();

	public int getNbrDecimale(HttpServletRequest request, ClsGlobalUpdate globalUpdate, GeneriqueConnexionService service, String dossier, String devise)
	{
		String key = devise;
		if (nbrDecimales.containsKey(devise))
			return nbrDecimales.get(key);
		int w_nddd = -1;
		error = StringUtils.EMPTY;
		ParamData o = service.findAnyColumnFromNomenclature(dossier, "","27", devise, "1");
		//(Rhfnom) service.get(Rhfnom.class, new RhfnomPK(dossier, 27, devise, 1));
		if (o == null)
		{
			error = globalUpdate.getParameter().errorMessage("ERR-90042", "0001", devise);
			globalUpdate._setEvolutionTraitement(request, error, true);
		}
		else
		{
			w_nddd = o.getValm().intValue();
		}

		nbrDecimales.put(key, w_nddd);

		return w_nddd;
	}

	String error;
	
	String nomClient;
	public String generateOGL(HttpServletRequest request, ClsGlobalUpdate globalUpdate, GeneriqueConnexionService service, String dossier, String session, String cuti)
	{
		// Determination du nom du client pour la gestion des sp�cifiques
//		nomClient = ClsConfigurationParameters.getConfigParameterValue(service, dossier, "0001", ClsConfigurationParameters.NOM_CLIENT);
//		if (StringUtils.equals(nomClient, ClsEntreprise.BANQUE_POSTALE_CONGO))
//			return this.generateOGLBPC(request, globalUpdate, service, dossier, session, cuti);
//
//		if (StringUtils.equals(nomClient, ClsEntreprise.BANQUE_COMMERCIALE_INTERNATIONALE))
//			return this.generateOGLBCI(request, globalUpdate, service, dossier, session, cuti);
		
		return this.generateOGLStandard(request, globalUpdate, service, dossier, session, cuti);
	}

	Date Date_Valeur = null;
	public String generateOGLStandard(HttpServletRequest request, ClsGlobalUpdate globalUpdate, GeneriqueConnexionService service, String dossier, String session, String cuti)
	{

		String datevaleurcomptable = ParameterUtil.getSessionObject(request, "datevaleurcomptable");
		if(StringUtils.isNotBlank(datevaleurcomptable))
		{
			String dateformat = ParameterUtil.getSessionObject(request, ParameterUtil.SESSION_FORMAT_DATE);
			Date_Valeur = new ClsDate(datevaleurcomptable, dateformat).getDate();
		}

		String separateur = "|";

		int numero = 0;

		int nbrColonnesSupp = 12;
		ParamData nome = service.findAnyColumnFromNomenclature(dossier, "0001", "99", "INT-BANK", "7");
				//(Rhfnom)service.get(Rhfnom.class, new RhfnomPK(dossier,99,"INT-BANK",7));
		if(nome != null && nome.getValm() != null)
			nbrColonnesSupp = nome.getValm().intValue();
		//ce nombre est le nombre de colonnes qu'on retrouve apr�s la colonne noseq dans la table bkmvti, auquel on rajoute 1
		//S'il y a pas de colonnes suppl�mentaires, on rajoute quand m�me 1 colonne

		boolean centra_par_cpt = StringUtil.in(service.findAnyColumnFromNomenclature(dossier, "0001", "99", "INT-BANK", "1").getVall(), "O,o");

		String numpce = new ClsDate(globalUpdate.getPeriode(), "yyyyMM").getDateS("MMyyyy") + globalUpdate.getNumerobulletin();

		// String query = " select AGE, DEV, CHA, NCP, SUF, OPE, MVT, RGP, UTI, EVE, CLC, DCO, SER, DVA, MON, SEN, LIB, EXO, "
		// + "PIE, RLET, DES1, DES2, DES3, DES4, DES5, UTF, UTA, TAU, DIN, TPR, NPR, NCC, SUC, ESI, IMP, CTA, "
		// + "MAR, DECH, AGSA, AGEM, AGDE, DEVC, MCTV, PIEO, NULL AS IDEN, NOSEQ from Rhtmpai a ";


//		String query = " select "
//			+ " CODDES1 AS AGE, DEVPCE AS DEV, NUMCPT AS NCP, NUMTIE AS SUF, CODDES3 AS OPE,  CODUTI AS UTI,"
//			+ " DATCPT AS DCO,  DATCPT AS DVA,PCE_MT AS MON, SENS AS SEN, LIBECR AS LIB, NUMPCE AS PIE, REFLET AS RLET, "
//			+" CODDES5 AS AGSA, DEVPCE AS DEVC, PCE_MT AS MCTV, NUMPCE AS PIEO, CODDES7 AS REFANA "
//			+ " from Cp_int a where cdos = '" + dossier + "' and numpce = '" + numpce+"'";
//		query += " Order by CODDES5, DEVPCE, NUMCPT, NUMTIE, CODDES3,  CODUTI, DATCPT, SENS, LIBECR, NUMPCE, REFLET, DEVPCE, CODDES7, CODDES5";
//
//		if (centra_par_cpt)
//		{
//			query = " select "
//					+ "  CODDES1 AS AGE, DEVPCE AS DEV, NUMCPT AS NCP, NUMTIE AS SUF, CODDES3 AS OPE,  CODUTI AS UTI,"
//					+ " DATCPT AS DCO,  DATCPT AS DVA,SUM(PCE_MT) AS MON, SENS AS SEN, LIBECR AS LIB, NUMPCE AS PIE, REFLET AS RLET, "
//					+ " CODDES5 AS AGSA, DEVPCE AS DEVC, SUM(PCE_MT) AS MCTV, NUMPCE AS PIEO, CODDES7 AS REFANA" + " from Cp_int a where cdos = '" + dossier + "' and numpce = '"
//					+ numpce + "'";
//			query += " Group by CODDES1, DEVPCE, NUMCPT, NUMTIE, CODDES3,  CODUTI, DATCPT, SENS, LIBECR, NUMPCE, REFLET, DEVPCE, CODDES7, CODDES5";
//			query += " Order by CODDES1, DEVPCE, NUMCPT, NUMTIE, CODDES3,  CODUTI, DATCPT, SENS, LIBECR, NUMPCE, REFLET, DEVPCE, CODDES7, CODDES5";
//		}

		String query = " select "
			+ " CODDES5 AS AGE, DEVPCE AS DEV, NUMCPT AS NCP, NUMTIE AS SUF, CODDES3 AS OPE,  CODUTI AS UTI,"
			+ " DATCPT AS DCO,  DATCPT AS DVA,PCE_MT AS MON, SENS AS SEN, LIBECR AS LIB, NUMPCE AS PIE, REFLET AS RLET, "
			+" CODDES5 AS AGSA, DEVPCE AS DEVC, PCE_MT AS MCTV, NUMPCE AS PIEO, CODDES7 AS REFANA "
			+ " from InterfComptable a where idEntreprise = '" + dossier + "' and numpce = '" + numpce+"'";
		query += " Order by CODDES5, DEVPCE, NUMCPT, NUMTIE, CODDES3,  CODUTI, DATCPT, SENS, LIBECR, NUMPCE, REFLET, DEVPCE, CODDES7";

		if (centra_par_cpt)
		{
			query = " select "
					+ "  CODDES5 AS AGE, DEVPCE AS DEV, NUMCPT AS NCP, NUMTIE AS SUF, CODDES3 AS OPE,  CODUTI AS UTI,"
					+ " DATCPT AS DCO,  DATCPT AS DVA,SUM(PCE_MT) AS MON, SENS AS SEN, LIBECR AS LIB, NUMPCE AS PIE, REFLET AS RLET, "
					+ " CODDES5 AS AGSA, DEVPCE AS DEVC, SUM(PCE_MT) AS MCTV, NUMPCE AS PIEO, CODDES7 AS REFANA" + " from InterfComptable a where idEntreprise = '" + dossier + "' and numpce = '"
					+ numpce + "'";
			query += " Group by CODDES5, DEVPCE, NUMCPT, NUMTIE, CODDES3,  CODUTI, DATCPT, SENS, LIBECR, NUMPCE, REFLET, DEVPCE, CODDES7";
			query += " Order by CODDES5, DEVPCE, NUMCPT, NUMTIE, CODDES3,  CODUTI, DATCPT, SENS, LIBECR, NUMPCE, REFLET, DEVPCE, CODDES7";
		}

//		System.out.println("REQUETE VIREMENT: "+query);

		String requete = "DELETE FROM TraitementTexte WHERE idEntreprise = '" + dossier + "' and sess='" + session + "'";
		Session oSession = null;
		Transaction tx = null;

		try
		{
			oSession = service.getSession();
			tx = oSession.beginTransaction();
			oSession.createQuery(requete).executeUpdate();

			Query q = oSession.createSQLQuery(query);

			//System.out.println("Ex�cution de la requete "+query);

			List<Object[]> lint = q.list();

			//System.out.println("Taille du r�sultat "+lint.size());

			String message = "";
			TraitementTexte text = null;

			MouvCptPaie lg;

			int w_nddd = 0;
			List l = service.find("select nddd from DossierPaie where idEntreprise = '" + dossier + "'");
			if (l != null && l.size() > 0)
			{
				if (l.get(0) != null)
					w_nddd = new Integer(l.get(0).toString());
			}


			String format = "dd/MM/yyyy";
			nome = service.findAnyColumnFromNomenclature(dossier, "0001", "99", "PAIE-INTER", "8");
			//nome = (Rhfnom)service.get(Rhfnom.class, new RhfnomPK(dossier,99,"PAIE-INTER",8));
			if(nome != null && StringUtils.isNotBlank(nome.getVall()))
				format = nome.getVall();

			String refana;
			Object[] ligne = null;

//			AGE	CHAR(5 BYTE)
//			DEV	CHAR(3 BYTE)
//			CHA	CHAR(10 BYTE)
//			NCP	CHAR(11 BYTE)
//			SUF	CHAR(2 BYTE)
//			OPE	CHAR(3 BYTE)
//			MVTI	CHAR(6 BYTE)
//			RGP	CHAR(3 BYTE)
//			UTI	CHAR(10 BYTE)
//			EVE	CHAR(6 BYTE)
//			CLC	CHAR(2 BYTE)
//			DCO	DATE
//			SER	CHAR(4 BYTE)
//			DVA	DATE
//			MON	NUMBER(19,4)
//			SEN	CHAR(1 BYTE)
//			LIB	CHAR(30 BYTE)
//			EXO	CHAR(1 BYTE)
//			PIE	CHAR(11 BYTE)
//			RLET	CHAR(8 BYTE)
//			DES1	CHAR(4 BYTE)
//			DES2	CHAR(4 BYTE)
//			DES3	CHAR(4 BYTE)
//			DES4	CHAR(4 BYTE)
//			DES5	CHAR(4 BYTE)
//			UTF	CHAR(10 BYTE)
//			UTA	CHAR(10 BYTE)
//			TAU	NUMBER(15,7)
//			DIN	DATE
//			TPR	CHAR(1 BYTE)
//			NPR	NUMBER(12,0)
//			NCC	CHAR(11 BYTE)
//			SUC	CHAR(2 BYTE)
//			ESI	CHAR(1 BYTE)
//			IMP	CHAR(1 BYTE)
//			CTA	CHAR(1 BYTE)
//			MAR	CHAR(1 BYTE)
//			DECH	DATE
//			AGSA	CHAR(5 BYTE)
//			AGEM	CHAR(5 BYTE)
//			AGDE	CHAR(5 BYTE)
//			DEVC	CHAR(3 BYTE)
//			MCTV	NUMBER(19,4)
//			PIEO	CHAR(11 BYTE)
//			IDEN	CHAR(6 BYTE)
//			NOSEQ	NUMBER(10,0)
//			LANG	CHAR(3 BYTE)
//			LIBNLS	CHAR(30 BYTE)
//			MODU	VARCHAR2(3 BYTE)
//			REFDOS	VARCHAR2(50 BYTE)
//			REFANA	CHAR(25 BYTE)
//			LABEL	VARCHAR2(25 BYTE)
//			NAT	VARCHAR2(6 BYTE)
//			ETA	VARCHAR2(2 BYTE)
//			SCHEMA	VARCHAR2(10 BYTE)
//			CETICPT	VARCHAR2(10 BYTE)
//			DESTANA	VARCHAR2(30 BYTE)
//			FUSION	CHAR(1 BYTE)
//
//
			for (int i = 0; i < lint.size(); i++)
			{
				ligne = lint.get(i);
				lg = this.getRhtmpai(ligne);
				refana = null;
				if(ligne[index] != null && StringUtils.isNotBlank(ligne[index].toString()))
					refana = ligne[index++].toString();
				else
					index++;

//				w_nddd = getNbrDecimale(request, globalUpdate, service, dossier, lg.getDev());
//				if (w_nddd == -1)
//					return error;

				message = StringUtil.nvl(lg.getAge(), StringUtils.EMPTY) + separateur;
				message += StringUtil.nvl(lg.getDev(), StringUtils.EMPTY) + separateur;
				message += StringUtil.nvl(lg.getCha(), StringUtils.EMPTY) + separateur;
				message += StringUtil.nvl(lg.getNcp(), StringUtils.EMPTY) + separateur;
				message += (lg.getSuf() != null ? lg.getSuf() : StringUtils.EMPTY) + separateur;
				message += StringUtil.nvl(lg.getOpe(), StringUtils.EMPTY) + separateur;
				message += StringUtil.nvl(lg.getMvt(), StringUtils.EMPTY) + separateur;
				message += StringUtil.nvl(lg.getRgp(), StringUtils.EMPTY) + separateur;
				message += StringUtil.nvl(lg.getUti(), StringUtils.EMPTY) + separateur;
				message += StringUtil.nvl(lg.getEve(), StringUtils.EMPTY) + separateur;
				message += StringUtil.nvl(lg.getClc(), StringUtils.EMPTY) + separateur;
				message += (lg.getDco() != null ? new ClsDate(lg.getDco()).getDateS(format) : StringUtils.EMPTY) + separateur;
				message += StringUtil.nvl(lg.getSer(), StringUtils.EMPTY) + separateur;
				//message += (lg.getDva() != null ? new ClsDate(lg.getDva()).getDateS(format) : StringUtils.EMPTY) + separateur;
				if(Date_Valeur != null)
					message += (new ClsDate(Date_Valeur).getDateS(format) ) + separateur;
				else
					message += (lg.getDva() != null ? new ClsDate(lg.getDva()).getDateS(format) : StringUtils.EMPTY) + separateur;

				message += ClsStringUtil.formatNumber(NumberUtils.bdnvl(lg.getMon(), 0), w_nddd, 19, false, '.') + separateur;
				message += StringUtil.nvl(lg.getSen(), StringUtils.EMPTY) + separateur;
				message += StringUtil.nvl(StringUtil.unAccent(lg.getLib()), StringUtils.EMPTY) + separateur;
				message += StringUtil.nvl(lg.getExo(), StringUtils.EMPTY) + separateur;
				message += StringUtil.nvl(lg.getPie(), StringUtils.EMPTY) + separateur;
				message += StringUtil.nvl(lg.getRlet(), StringUtils.EMPTY) + separateur;
				message += StringUtil.nvl(lg.getDes1(), StringUtils.EMPTY) + separateur;
				message += StringUtil.nvl(lg.getDes2(), StringUtils.EMPTY) + separateur;
				message += StringUtil.nvl(lg.getDes3(), StringUtils.EMPTY) + separateur;
				message += StringUtil.nvl(lg.getDes4(), StringUtils.EMPTY) + separateur;
				message += StringUtil.nvl(lg.getDes5(), StringUtils.EMPTY) + separateur;
				message += StringUtil.nvl(lg.getUtf(), StringUtils.EMPTY) + separateur;
				message += StringUtil.nvl(lg.getUta(), StringUtils.EMPTY) + separateur;
				message += StringUtil.nvl(lg.getTau() == null ? StringUtils.EMPTY : lg.getTau().toString(), StringUtils.EMPTY) + separateur;
				message += (lg.getDin() != null ? new ClsDate(lg.getDin()).getDateS(format) : StringUtils.EMPTY) + separateur;
				message += StringUtil.nvl(lg.getTpr(), StringUtils.EMPTY) + separateur;
				message += StringUtil.nvl(lg.getNpr() != null ? lg.getNpr().toString() : StringUtils.EMPTY, StringUtils.EMPTY) + separateur;
				message += StringUtil.nvl(lg.getNcc(), StringUtils.EMPTY) + separateur;
				message += StringUtil.nvl(lg.getSuc(), StringUtils.EMPTY) + separateur;
				message += StringUtil.nvl(lg.getEsi(), StringUtils.EMPTY) + separateur;
				message += StringUtil.nvl(lg.getImp(), StringUtils.EMPTY) + separateur;
				message += StringUtil.nvl(lg.getCta(), StringUtils.EMPTY) + separateur;
				message += StringUtil.nvl(lg.getMar(), StringUtils.EMPTY) + separateur;
				message += (lg.getDech() != null ? new ClsDate(lg.getDech()).getDateS(format) : StringUtils.EMPTY) + separateur;
				message += StringUtil.nvl(lg.getAgsa(), StringUtils.EMPTY) + separateur;
				message += StringUtil.nvl(lg.getAgem(), StringUtils.EMPTY) + separateur;
				message += StringUtil.nvl(lg.getAgde(), StringUtils.EMPTY) + separateur;
				message += StringUtil.nvl(lg.getDevc(), StringUtils.EMPTY) + separateur;
				message += ClsStringUtil.formatNumber(NumberUtils.bdnvl(lg.getMctv(), 0), w_nddd, 19, false, '.') + separateur;
				message += StringUtil.nvl(lg.getPieo(), StringUtils.EMPTY) + separateur;
				message += StringUtil.nvl(lg.getIden(), StringUtils.EMPTY) + separateur;
				message += StringUtil.nvl(lg.getNoseq() != null ? lg.getNoseq().toString() : StringUtils.EMPTY, StringUtils.EMPTY);
				//Les colonnes suppl�mentaires commencent ici
				//Il faut qu'on param�tre le nombre de colonnes suppl�mentaires � ajouter


				if(nbrColonnesSupp>=1) message += separateur + StringUtils.EMPTY;
				if(nbrColonnesSupp>=2) message += separateur + StringUtils.EMPTY;
				if(nbrColonnesSupp>=3) message += separateur + StringUtils.EMPTY;
				if(nbrColonnesSupp>=4) message += separateur + StringUtils.EMPTY;

				if(nbrColonnesSupp>=5) message += separateur + StringUtil.nvl(refana, StringUtils.EMPTY);

				if(nbrColonnesSupp>=6) message += separateur + StringUtils.EMPTY;
				if(nbrColonnesSupp>=7) message += separateur + StringUtils.EMPTY;
				if(nbrColonnesSupp>=8) message += separateur + StringUtils.EMPTY;
				if(nbrColonnesSupp>=9) message += separateur + StringUtils.EMPTY;
				if(nbrColonnesSupp>=10) message += separateur + StringUtils.EMPTY;
				if(nbrColonnesSupp>=11) message += separateur + StringUtils.EMPTY;
				if(nbrColonnesSupp>=12) message += separateur + StringUtils.EMPTY;
				for(int n=1; n<= nbrColonnesSupp - 12; n++)
					message += separateur + StringUtils.EMPTY;

				numero += 1;
				text = new TraitementTexte();
				text.setSess(session);
				text.setNlig(numero);
				text.setIdEntreprise(new Integer(dossier));
				text.setTexte(message);
				//System.out.println("Ligne "+numero+" = "+message);
				//
				oSession.save(text);
				if (i % 20 == 0)
				{
					oSession.flush();
					oSession.clear();
				}
			}

			tx.commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			if (tx != null)
				tx.rollback();
			String errmess1 = ClsTreater._getStackTrace(e);
			writeLog(errmess1, dossier, cuti);
			globalUpdate._setEvolutionTraitement(request, errmess1, true);
			return errmess1;
		}
		finally
		{
			service.closeSession(oSession);
		}
		return null;
	}


//	public String generateOGLBCI(HttpServletRequest request, ClsGlobalUpdate globalUpdate, ClsService service, String dossier, String session, String cuti)
//	{
//		String separateur = "|";
//
//		int numero = 0;
//
//		int nbrColonnesSupp = 12;
//		Rhfnom nome = (Rhfnom)service.get(Rhfnom.class, new RhfnomPK(dossier,99,"INT-BANK",7));
//		if(nome != null && nome.getValm() != null)
//			nbrColonnesSupp = nome.getValm().intValue();
//
//		boolean centra_par_cpt = StringUtils.in(service.findAnyColumnFromNomenclature(dossier, "0001", "99", "INT-BANK", "1").getLibelle(), "O,o");
//
//		String numpce = new ClsDate(globalUpdate.getPeriode(), "yyyyMM").getDateS("MMyyyy") + globalUpdate.getNumerobulletin();
//
//		// String query = " select AGE, DEV, CHA, NCP, SUF, OPE, MVT, RGP, UTI, EVE, CLC, DCO, SER, DVA, MON, SEN, LIB, EXO, "
//		// + "PIE, RLET, DES1, DES2, DES3, DES4, DES5, UTF, UTA, TAU, DIN, TPR, NPR, NCC, SUC, ESI, IMP, CTA, "
//		// + "MAR, DECH, AGSA, AGEM, AGDE, DEVC, MCTV, PIEO, NULL AS IDEN, NOSEQ from Rhtmpai a ";
//
//
//		String query = " select "
//			+ " CODDES5 AS AGE, DEVPCE AS DEV, NUMCPT AS NCP, NUMTIE AS SUF, CODDES3 AS OPE,  CODUTI AS UTI,"
//			+ " DATCPT AS DCO,  DATCPT AS DVA,PCE_MT AS MON, SENS AS SEN, LIBECR AS LIB, NUMPCE AS PIE, REFLET AS RLET, "
//			+" CODDES5 AS AGSA, DEVPCE AS DEVC, PCE_MT AS MCTV, NUMPCE AS PIEO, CODDES7 AS REFANA "
//			+ " from Cp_int a where cdos = '" + dossier + "' and numpce = '" + numpce+"'";
//		query += " Order by CODDES5, DEVPCE, NUMCPT, NUMTIE, CODDES3,  CODUTI, DATCPT, SENS, LIBECR, NUMPCE, REFLET, DEVPCE, CODDES7";
//
//		if (centra_par_cpt)
//		{
//			query = " select "
//					+ "  CODDES5 AS AGE, DEVPCE AS DEV, NUMCPT AS NCP, NUMTIE AS SUF, CODDES3 AS OPE,  CODUTI AS UTI,"
//					+ " DATCPT AS DCO,  DATCPT AS DVA,SUM(PCE_MT) AS MON, SENS AS SEN, LIBECR AS LIB, NUMPCE AS PIE, REFLET AS RLET, "
//					+ " CODDES5 AS AGSA, DEVPCE AS DEVC, SUM(PCE_MT) AS MCTV, NUMPCE AS PIEO, CODDES7 AS REFANA" + " from Cp_int a where cdos = '" + dossier + "' and numpce = '"
//					+ numpce + "'";
//			query += " Group by CODDES5, DEVPCE, NUMCPT, NUMTIE, CODDES3,  CODUTI, DATCPT, SENS, LIBECR, NUMPCE, REFLET, DEVPCE, CODDES7";
//			query += " Order by CODDES5, DEVPCE, NUMCPT, NUMTIE, CODDES3,  CODUTI, DATCPT, SENS, LIBECR, NUMPCE, REFLET, DEVPCE, CODDES7";
//		}
//
//		String requete = "DELETE FROM Rhttext WHERE cdos = '" + dossier + "' and sess='" + session + "'";
//		Session oSession = null;
//		Transaction tx = null;
//		try
//		{
//			oSession = service.getSession();
//			tx = oSession.beginTransaction();
//			oSession.createQuery(requete).executeUpdate();
//
//			Query q = oSession.createSQLQuery(query);
//
//			//System.out.println("Ex�cution de la requete "+query);
//
//			List<Object[]> lint = q.list();
//
//			//System.out.println("Taille du r�sultat "+lint.size());
//
//			String message = "";
//			Rhttext text = null;
//
//			Rhtmpai lg;
//
//			int w_nddd = 0;
//			List l = service.find("select nddd from Cpdo where cdos = '" + dossier + "'");
//			if (l != null && l.size() > 0)
//			{
//				if (l.get(0) != null)
//					w_nddd = new Integer(l.get(0).toString());
//			}
//
//
//			String format = "dd/MM/yyyy";
//
//			String refana;
//			Object[] ligne = null;
//
////			AGE	CHAR(5 BYTE)
////			DEV	CHAR(3 BYTE)
////			CHA	CHAR(10 BYTE)
////			NCP	CHAR(11 BYTE)
////			SUF	CHAR(2 BYTE)
////			OPE	CHAR(3 BYTE)
////			MVTI	CHAR(6 BYTE)
////			RGP	CHAR(3 BYTE)
////			UTI	CHAR(10 BYTE)
////			EVE	CHAR(6 BYTE)
////			CLC	CHAR(2 BYTE)
////			DCO	DATE
////			SER	CHAR(4 BYTE)
////			DVA	DATE
////			MON	NUMBER(19,4)
////			SEN	CHAR(1 BYTE)
////			LIB	CHAR(30 BYTE)
////			EXO	CHAR(1 BYTE)
////			PIE	CHAR(11 BYTE)
////			RLET	CHAR(8 BYTE)
////			DES1	CHAR(4 BYTE)
////			DES2	CHAR(4 BYTE)
////			DES3	CHAR(4 BYTE)
////			DES4	CHAR(4 BYTE)
////			DES5	CHAR(4 BYTE)
////			UTF	CHAR(10 BYTE)
////			UTA	CHAR(10 BYTE)
////			TAU	NUMBER(15,7)
////			DIN	DATE
////			TPR	CHAR(1 BYTE)
////			NPR	NUMBER(12,0)
////			NCC	CHAR(11 BYTE)
////			SUC	CHAR(2 BYTE)
////			ESI	CHAR(1 BYTE)
////			IMP	CHAR(1 BYTE)
////			CTA	CHAR(1 BYTE)
////			MAR	CHAR(1 BYTE)
////			DECH	DATE
////			AGSA	CHAR(5 BYTE)
////			AGEM	CHAR(5 BYTE)
////			AGDE	CHAR(5 BYTE)
////			DEVC	CHAR(3 BYTE)
////			MCTV	NUMBER(19,4)
////			PIEO	CHAR(11 BYTE)
////			IDEN	CHAR(6 BYTE)
////			NOSEQ	NUMBER(10,0)
////			LANG	CHAR(3 BYTE)
////			LIBNLS	CHAR(30 BYTE)
////			MODU	VARCHAR2(3 BYTE)
////			REFDOS	VARCHAR2(50 BYTE)
////			REFANA	CHAR(25 BYTE)
////			LABEL	VARCHAR2(25 BYTE)
////			NAT	VARCHAR2(6 BYTE)
////			ETA	VARCHAR2(2 BYTE)
////			SCHEMA	VARCHAR2(10 BYTE)
////			CETICPT	VARCHAR2(10 BYTE)
////			DESTANA	VARCHAR2(30 BYTE)
////			FUSION	CHAR(1 BYTE)
////
////
//			for (int i = 0; i < lint.size(); i++)
//			{
//				ligne = lint.get(i);
//				lg = this.getRhtmpai(ligne);
//				refana = null;
//				if(ligne[index] != null && StringUtils.isNotBlank(ligne[index].toString()))
//					refana = ligne[index++].toString();
//				else
//					index++;
//
////				w_nddd = getNbrDecimale(request, globalUpdate, service, dossier, lg.getDev());
////				if (w_nddd == -1)
////					return error;
//
//				message = StringUtil.nvl(lg.getAge(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getDev(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl("", StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getNcp(), StringUtils.EMPTY) + separateur;
//				message += " " + separateur;
//				message += StringUtil.nvl(lg.getOpe(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getMvt(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getRgp(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getUti(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getEve(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getClc(), StringUtils.EMPTY) + separateur;
//				message += (lg.getDco() != null ? new ClsDate(lg.getDco()).getDateS(format) : StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getSer(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl("", StringUtils.EMPTY) + separateur;
//				message += ClsStringUtil.formatNumber(NumberUtils.bdnvl(lg.getMon(), 0), 1, 19, false, '.') + separateur;
//				message += StringUtil.nvl(lg.getSen(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getLib(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getExo(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getPie(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getRlet(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getDes1(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getDes2(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getDes3(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getDes4(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getDes5(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getUtf(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getUta(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getTau() == null ? StringUtils.EMPTY : lg.getTau().toString(), StringUtils.EMPTY) + separateur;
//				message += (lg.getDin() != null ? new ClsDate(lg.getDin()).getDateS(format) : StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getTpr(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getNpr() != null ? lg.getNpr().toString() : StringUtils.EMPTY, StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getNcc(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getSuc(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getEsi(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getImp(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getCta(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getMar(), StringUtils.EMPTY) + separateur;
//				message += (lg.getDech() != null ? new ClsDate(lg.getDech()).getDateS(format) : StringUtils.EMPTY) + separateur;
//				message += " " + separateur;
//				message += " " + separateur;
//				message += " " + separateur;
//				message += " " + separateur;
//				message += "0" + separateur;
//				message += " " + separateur;
//				message += " " + separateur;
//				message += StringUtil.nvl("0", StringUtils.EMPTY);
//				//Les colonnes suppl�mentaires commencent ici
//				//Il faut qu'on param�tre le nombre de colonnes suppl�mentaires � ajouter
//
//
//				if(nbrColonnesSupp>=1) message += separateur + StringUtils.EMPTY;
//				if(nbrColonnesSupp>=2) message += separateur + StringUtils.EMPTY;
//				if(nbrColonnesSupp>=3) message += separateur + StringUtils.EMPTY;
//				if(nbrColonnesSupp>=4) message += separateur + StringUtils.EMPTY;
//
//				if(nbrColonnesSupp>=5) message += separateur + StringUtil.nvl(refana, StringUtils.EMPTY);
//
//				if(nbrColonnesSupp>=6) message += separateur + StringUtils.EMPTY;
//				if(nbrColonnesSupp>=7) message += separateur + StringUtils.EMPTY;
//				if(nbrColonnesSupp>=8) message += separateur + StringUtils.EMPTY;
//				if(nbrColonnesSupp>=9) message += separateur + StringUtils.EMPTY;
//				if(nbrColonnesSupp>=10) message += separateur + StringUtils.EMPTY;
//				if(nbrColonnesSupp>=11) message += separateur + StringUtils.EMPTY;
//				if(nbrColonnesSupp>=12) message += separateur + StringUtils.EMPTY;
//				for(int n=1; n<= nbrColonnesSupp - 12; n++)
//					message += separateur + StringUtils.EMPTY;
//
//				numero += 1;
//				text = new Rhttext();
//				text.setComp_id(new RhttextPK(session, numero));
//				text.setCdos(dossier);
//				text.setTexte(message);
//				//System.out.println("Ligne "+numero+" = "+message);
//				//
//				oSession.save(text);
//				if (i % 20 == 0)
//				{
//					oSession.flush();
//					oSession.clear();
//				}
//			}
//
//			tx.commit();
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//			if (tx != null)
//				tx.rollback();
//			String errmess1 = ClsTreater._getStackTrace(e);
//			writeLog(errmess1, dossier, cuti);
//			globalUpdate._setEvolutionTraitement(request, errmess1, true);
//			return errmess1;
//		}
//		finally
//		{
//			service.closeConnexion(oSession);
//		}
//		return null;
//	}
//
//	public String generateOGLBIAC(HttpServletRequest request, ClsGlobalUpdate globalUpdate, ClsService service, String dossier, String session, String cuti)
//	{
//		String separateur = "|";
//
//		int numero = 0;
//
//		boolean centra_par_cpt = StringUtils.in(service.findAnyColumnFromNomenclature(dossier, "0001", "99", "INT-BANK", "1").getLibelle(), "O,o");
//
//		String numpce = new ClsDate(globalUpdate.getPeriode(), "yyyyMM").getDateS("MMyyyy") + globalUpdate.getNumerobulletin();
//
//		// String query = " select AGE, DEV, CHA, NCP, SUF, OPE, MVT, RGP, UTI, EVE, CLC, DCO, SER, DVA, MON, SEN, LIB, EXO, "
//		// + "PIE, RLET, DES1, DES2, DES3, DES4, DES5, UTF, UTA, TAU, DIN, TPR, NPR, NCC, SUC, ESI, IMP, CTA, "
//		// + "MAR, DECH, AGSA, AGEM, AGDE, DEVC, MCTV, PIEO, NULL AS IDEN, NOSEQ from Rhtmpai a ";
//
//
//		String query = " select "
//			+ " CODDES5 AS AGE, DEVPCE AS DEV, NUMCPT AS NCP, NUMTIE AS SUF, CODDES3 AS OPE,  CODUTI AS UTI,"
//			+ " DATCPT AS DCO,  DATCPT AS DVA,PCE_MT AS MON, SENS AS SEN, LIBECR AS LIB, NUMPCE AS PIE, REFLET AS RLET, "
//			+" CODDES5 AS AGSA, DEVPCE AS DEVC, PCE_MT AS MCTV, NUMPCE AS PIEO, CODDES7 AS REFANA "
//			+ " from Cp_int a where cdos = '" + dossier + "' and numpce = '" + numpce+"'";
//
//		if (centra_par_cpt)
//		{
//			query = " select "
//					+ "  CODDES5 AS AGE, DEVPCE AS DEV, NUMCPT AS NCP, NUMTIE AS SUF, CODDES3 AS OPE,  CODUTI AS UTI,"
//					+ " DATCPT AS DCO,  DATCPT AS DVA,SUM(PCE_MT) AS MON, SENS AS SEN, LIBECR AS LIB, NUMPCE AS PIE, REFLET AS RLET, "
//					+ " CODDES5 AS AGSA, DEVPCE AS DEVC, SUM(PCE_MT) AS MCTV, NUMPCE AS PIEO, CODDES7 AS REFANA" + " from Cp_int a where cdos = '" + dossier + "' and numpce = '"
//					+ numpce + "'";
//			query += " Group by CODDES5, DEVPCE, NUMCPT, NUMTIE, CODDES3,  CODUTI, DATCPT, SENS, LIBECR, NUMPCE, REFLET, DEVPCE, CODDES7";
//		}
//
//		String requete = "DELETE FROM Rhttext WHERE cdos = '" + dossier + "' and sess='" + session + "'";
//		Session oSession = null;
//		Transaction tx = null;
//		try
//		{
//			oSession = service.getSession();
//			tx = oSession.beginTransaction();
//			oSession.createQuery(requete).executeUpdate();
//
//			Query q = oSession.createSQLQuery(query);
//
//			//System.out.println("Ex�cution de la requete "+query);
//
//			List<Object[]> lint = q.list();
//
//			//System.out.println("Taille du r�sultat "+lint.size());
//
//			String message = "";
//			Rhttext text = null;
//
//			Rhtmpai lg;
//
//			int w_nddd = 0;
//			List l = service.find("select nddd from Cpdo where cdos = '" + dossier + "'");
//			if (l != null && l.size() > 0)
//			{
//				if (l.get(0) != null)
//					w_nddd = new Integer(l.get(0).toString());
//			}
//
//
//			String format = "dd/MM/yyyy";
//
//			String refana;
//			Object[] ligne = null;
//
////			AGE	CHAR(5 BYTE)
////			DEV	CHAR(3 BYTE)
////			CHA	CHAR(10 BYTE)
////			NCP	CHAR(11 BYTE)
////			SUF	CHAR(2 BYTE)
////			OPE	CHAR(3 BYTE)
////			MVTI	CHAR(6 BYTE)
////			RGP	CHAR(3 BYTE)
////			UTI	CHAR(10 BYTE)
////			EVE	CHAR(6 BYTE)
////			CLC	CHAR(2 BYTE)
////			DCO	DATE
////			SER	CHAR(4 BYTE)
////			DVA	DATE
////			MON	NUMBER(19,4)
////			SEN	CHAR(1 BYTE)
////			LIB	CHAR(30 BYTE)
////			EXO	CHAR(1 BYTE)
////			PIE	CHAR(11 BYTE)
////			RLET	CHAR(8 BYTE)
////			DES1	CHAR(4 BYTE)
////			DES2	CHAR(4 BYTE)
////			DES3	CHAR(4 BYTE)
////			DES4	CHAR(4 BYTE)
////			DES5	CHAR(4 BYTE)
////			UTF	CHAR(10 BYTE)
////			UTA	CHAR(10 BYTE)
////			TAU	NUMBER(15,7)
////			DIN	DATE
////			TPR	CHAR(1 BYTE)
////			NPR	NUMBER(12,0)
////			NCC	CHAR(11 BYTE)
////			SUC	CHAR(2 BYTE)
////			ESI	CHAR(1 BYTE)
////			IMP	CHAR(1 BYTE)
////			CTA	CHAR(1 BYTE)
////			MAR	CHAR(1 BYTE)
////			DECH	DATE
////			AGSA	CHAR(5 BYTE)
////			AGEM	CHAR(5 BYTE)
////			AGDE	CHAR(5 BYTE)
////			DEVC	CHAR(3 BYTE)
////			MCTV	NUMBER(19,4)
////			PIEO	CHAR(11 BYTE)
////			IDEN	CHAR(6 BYTE)
////			NOSEQ	NUMBER(10,0)
////			LANG	CHAR(3 BYTE)
////			LIBNLS	CHAR(30 BYTE)
////			MODU	VARCHAR2(3 BYTE)
////			REFDOS	VARCHAR2(50 BYTE)
////			REFANA	CHAR(25 BYTE)
////			LABEL	VARCHAR2(25 BYTE)
////			NAT	VARCHAR2(6 BYTE)
////			ETA	VARCHAR2(2 BYTE)
////			SCHEMA	VARCHAR2(10 BYTE)
////			CETICPT	VARCHAR2(10 BYTE)
////			DESTANA	VARCHAR2(30 BYTE)
////			FUSION	CHAR(1 BYTE)
////
////
//			for (int i = 0; i < lint.size(); i++)
//			{
//				ligne = lint.get(i);
//				lg = this.getRhtmpai(ligne);
//				refana = null;
//				if(ligne[index] != null && StringUtils.isNotBlank(ligne[index].toString()))
//					refana = ligne[index++].toString();
//				else
//					index++;
//
////				w_nddd = getNbrDecimale(request, globalUpdate, service, dossier, lg.getDev());
////				if (w_nddd == -1)
////					return error;
//
//				message = StringUtil.nvl(lg.getAge(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getDev(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getCha(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getNcp(), StringUtils.EMPTY) + separateur;
//				/*TFN 24/10/2012 :suffixe toujours � null*/
//				//message += (lg.getSuf() != null ? lg.getSuf() : StringUtils.EMPTY) + separateur;
//				message += "  "  + separateur;
//
//				message += StringUtil.nvl(lg.getOpe(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getMvt(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getRgp(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getUti(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getEve(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getClc(), StringUtils.EMPTY) + separateur;
//				message += (lg.getDco() != null ? new ClsDate(lg.getDco()).getDateS(format) : StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getSer(), StringUtils.EMPTY) + separateur;
//				message += (lg.getDva() != null ? new ClsDate(lg.getDva()).getDateS(format) : StringUtils.EMPTY) + separateur;
//				message += ClsStringUtil.formatNumber(NumberUtils.bdnvl(lg.getMon(), 0), w_nddd, 19, false, '.') + separateur;
//				message += StringUtil.nvl(lg.getSen(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getLib(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getExo(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getPie(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getRlet(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getDes1(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getDes2(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getDes3(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getDes4(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getDes5(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getUtf(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getUta(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getTau() == null ? StringUtils.EMPTY : lg.getTau().toString(), StringUtils.EMPTY) + separateur;
//				message += (lg.getDin() != null ? new ClsDate(lg.getDin()).getDateS(format) : StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getTpr(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getNpr() != null ? lg.getNpr().toString() : StringUtils.EMPTY, StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getNcc(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getSuc(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getEsi(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getImp(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getCta(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getMar(), StringUtils.EMPTY) + separateur;
//				message += (lg.getDech() != null ? new ClsDate(lg.getDech()).getDateS(format) : StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getAgsa(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getAgem(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getAgde(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getDevc(), StringUtils.EMPTY) + separateur;
//				message += ClsStringUtil.formatNumber(NumberUtils.bdnvl(lg.getMctv(), 0), w_nddd, 19, false, '.') + separateur;
//				message += StringUtil.nvl(lg.getPieo(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getIden(), StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl(lg.getNoseq() != null ? lg.getNoseq().toString() : StringUtils.EMPTY, StringUtils.EMPTY) + separateur;
//
//				/*TFN 24/10/2012 - Centralisation BIAC dans DeltaBank 4js (V7.5), ces champs sont en trop
//				 *
//				 * message += StringUtils.EMPTY + separateur;
//				message += StringUtils.EMPTY + separateur;
//				message += StringUtils.EMPTY + separateur;
//				message += StringUtils.EMPTY + separateur;
//
//				message += StringUtil.nvl(refana, StringUtils.EMPTY) + separateur;
//
//				message += StringUtils.EMPTY + separateur;
//				message += StringUtils.EMPTY + separateur;
//				message += StringUtils.EMPTY + separateur;
//				message += StringUtils.EMPTY + separateur;
//				message += StringUtils.EMPTY + separateur;
//				message += StringUtils.EMPTY + separateur;
//				message += StringUtils.EMPTY + separateur;
//*/
//				numero += 1;
//				text = new Rhttext();
//				text.setComp_id(new RhttextPK(session, numero));
//				text.setCdos(dossier);
//				text.setTexte(message);
//				//System.out.println("Ligne "+numero+" = "+message);
//				//
//				oSession.save(text);
//				if (i % 20 == 0)
//				{
//					oSession.flush();
//					oSession.clear();
//				}
//			}
//
//			tx.commit();
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//			if (tx != null)
//				tx.rollback();
//			String errmess1 = ClsTreater._getStackTrace(e);
//			writeLog(errmess1, dossier, cuti);
//			globalUpdate._setEvolutionTraitement(request, errmess1, true);
//			return errmess1;
//		}
//		finally
//		{
//			service.closeConnexion(oSession);
//		}
//		return null;
//	}
//
//	public String generateOGLBPC(HttpServletRequest request, ClsGlobalUpdate globalUpdate, ClsService service, String dossier, String session, String cuti)
//	{
//		String separateur = "";
//
//		int numero = 0;
//
//		int nbrColonnesSupp = 12;
//		Rhfnom nome = (Rhfnom)service.get(Rhfnom.class, new RhfnomPK(dossier,99,"INT-BANK",7));
//		if(nome != null && nome.getValm() != null)
//			nbrColonnesSupp = nome.getValm().intValue();
//
//		boolean centra_par_cpt = StringUtils.in(service.findAnyColumnFromNomenclature(dossier, "0001", "99", "INT-BANK", "1").getLibelle(), "O,o");
//
//		String numpce = new ClsDate(globalUpdate.getPeriode(), "yyyyMM").getDateS("MMyyyy") + globalUpdate.getNumerobulletin();
//
//		// String query = " select AGE, DEV, CHA, NCP, SUF, OPE, MVT, RGP, UTI, EVE, CLC, DCO, SER, DVA, MON, SEN, LIB, EXO, "
//		// + "PIE, RLET, DES1, DES2, DES3, DES4, DES5, UTF, UTA, TAU, DIN, TPR, NPR, NCC, SUC, ESI, IMP, CTA, "
//		// + "MAR, DECH, AGSA, AGEM, AGDE, DEVC, MCTV, PIEO, NULL AS IDEN, NOSEQ from Rhtmpai a ";
//
//
//		String query = " select "
//			+ " CODDES5 AS AGE, DEVPCE AS DEV, NUMCPT AS NCP, NUMTIE AS SUF, CODDES3 AS OPE,  CODUTI AS UTI,"
//			+ " DATCPT AS DCO,  DATCPT AS DVA,PCE_MT AS MON, SENS AS SEN, LIBECR AS LIB, NUMPCE AS PIE, REFLET AS RLET, "
//			+" CODDES5 AS AGSA, DEVPCE AS DEVC, PCE_MT AS MCTV, NUMPCE AS PIEO, CODDES7 AS REFANA "
//			+ " from Cp_int a where cdos = '" + dossier + "' and numpce = '" + numpce+"'";
//
//		if (centra_par_cpt)
//		{
//			query = " select "
//					+ "  CODDES5 AS AGE, DEVPCE AS DEV, NUMCPT AS NCP, NUMTIE AS SUF, CODDES3 AS OPE,  CODUTI AS UTI,"
//					+ " DATCPT AS DCO,  DATCPT AS DVA,SUM(PCE_MT) AS MON, SENS AS SEN, LIBECR AS LIB, NUMPCE AS PIE, REFLET AS RLET, "
//					+ " CODDES5 AS AGSA, DEVPCE AS DEVC, SUM(PCE_MT) AS MCTV, NUMPCE AS PIEO, CODDES7 AS REFANA" + " from Cp_int a where cdos = '" + dossier + "' and numpce = '"
//					+ numpce + "'";
//			query += " Group by CODDES5, DEVPCE, NUMCPT, NUMTIE, CODDES3,  CODUTI, DATCPT, SENS, LIBECR, NUMPCE, REFLET, DEVPCE, CODDES7";
//		}
//
//		String requete = "DELETE FROM Rhttext WHERE cdos = '" + dossier + "' and sess='" + session + "'";
//		Session oSession = null;
//		Transaction tx = null;
//		try
//		{
//			oSession = service.getSession();
//			tx = oSession.beginTransaction();
//			oSession.createQuery(requete).executeUpdate();
//
//			Query q = oSession.createSQLQuery(query);
//
//			//System.out.println("Ex�cution de la requete "+query);
//
//			List<Object[]> lint = q.list();
//
//			//System.out.println("Taille du r�sultat "+lint.size());
//
//			String message = "";
//			Rhttext text = null;
//
//			Rhtmpai lg;
//
//			int w_nddd = 0;
//			List l = service.find("select nddd from Cpdo where cdos = '" + dossier + "'");
//			if (l != null && l.size() > 0)
//			{
//				if (l.get(0) != null)
//					w_nddd = new Integer(l.get(0).toString());
//			}
//
//
//			String format = "yyyyMMdd";
//
//			String refana;
//			Object[] ligne = null;
//
////			COMPTE 	1	11	INTEGER EXTERNAL  	Num�ro du compte (11 caract�res)
////			DATOPER	12	19	DATE "YYYYMMDD"	Date op�ration
////			DATVAL 	20	27	DATE "YYYYMMDD" 	Date de valeur (laisser vide si identique � la date d'op�ration
////			MNTDEV 	28	41	DECIMAL	Montant du mouvement en valeur absolue
////			SIGN   	42	42	CHAR	Signe du montant
////			LIBELLE	43	74	CHAR	Libell� associ� au mouvement
////			CODOPSC	75	79	'PAYE '	Valeur fixe
////			EXPL   	80	81	'AU'	Valeur fixe
////			NOOPER 	93	99	'       '	Laisser vide
////			NATOP  	100	104	'    '	Laisser vide
////			AGENCE	105	109	CHAR	Code agence du compte
//
//			BigDecimal mnt;
//			String sens;
//
//			for (int i = 0; i < lint.size(); i++)
//			{
//				ligne = lint.get(i);
//				lg = this.getRhtmpai(ligne);
//				refana = null;
//				if(ligne[index] != null && StringUtils.isNotBlank(ligne[index].toString()))
//					refana = ligne[index++].toString();
//				else
//					index++;
//
//				message = StringUtils.oraSubstring(StringUtils.oraLPad(StringUtil.nvl(lg.getNcp(), " "),11," "),1,11) + separateur;
//				message += (lg.getDco() != null ? new ClsDate(lg.getDco()).getDateS(format) : StringUtils.EMPTY) + separateur;
//				message += (lg.getDva() != null ? new ClsDate(lg.getDva()).getDateS(format) : StringUtils.EMPTY) + separateur;
//				mnt = NumberUtils.bdnvl(lg.getMon(), 0);
//				sens = lg.getSen();
//				if(mnt.signum()<0)
//				{
//					mnt = mnt.multiply(new BigDecimal(-1), MathContext.DECIMAL32);
//					sens = StringUtils.equals("D", sens) ? "C" : "D";
//				}
//				message += StringUtils.oraSubstring(StringUtils.oraLPad(StringUtil.nvl(ClsStringUtil.formatNumber(mnt, w_nddd, 14, true, '.'), "0"),14,"0"),1,14) + separateur;
//				message += StringUtil.nvl(StringUtils.equals("C", sens) ? "+" : "-", StringUtils.EMPTY) + separateur;
//				message += StringUtils.oraSubstring(StringUtils.oraRPad(StringUtil.nvl(lg.getLib(), " "),32," "),1,32) + separateur;
//				message += StringUtil.nvl("PAYE ", StringUtils.EMPTY) + separateur;
//				message += StringUtil.nvl("AU", StringUtils.EMPTY) + separateur;
//				message += "           "+ separateur;
//				message += "       "+ separateur;
//				message += "     "+ separateur;
//				message += StringUtils.oraSubstring(StringUtils.oraLPad(StringUtil.nvl(lg.getAge(), " "),5," "),1,5);
//
//
//
//				numero += 1;
//				text = new Rhttext();
//				text.setComp_id(new RhttextPK(session, numero));
//				text.setCdos(dossier);
//				text.setTexte(message);
//				//System.out.println("Ligne "+numero+" = "+message);
//				//
//				oSession.save(text);
//				if (i % 20 == 0)
//				{
//					oSession.flush();
//					oSession.clear();
//				}
//			}
//
//			tx.commit();
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//			if (tx != null)
//				tx.rollback();
//			String errmess1 = ClsTreater._getStackTrace(e);
//			writeLog(errmess1, dossier, cuti);
//			globalUpdate._setEvolutionTraitement(request, errmess1, true);
//			return errmess1;
//		}
//		finally
//		{
//			service.closeConnexion(oSession);
//		}
//		return null;
//	}

	void writeLog(String error, String dossier, String cuti)
	{
		LogMessage logger = new LogMessage();
		logger.setIdEntreprise(new Integer(dossier));
		logger.setCuti(cuti);
		logger.setDatc(new Date());
		logger.setLigne(error);
	}

}
