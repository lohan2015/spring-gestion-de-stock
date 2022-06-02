package com.kinart.paie.business.services.cloture;

import java.io.File;
import java.io.PrintStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

import com.kinart.paie.business.model.*;
import com.kinart.paie.business.services.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ClsOGL
  implements IOGL
{
  private boolean pb = false;
  private String errmess1 = "";
  private String errmess2 = "";
  private String dossier = "";
  private String langue = "";
  private String session = "";
  private String cuti = "";
  private GeneriqueConnexionService service = null;
  private Integer nddd = Integer.valueOf(0);
  private ClsGlobalUpdate globalUpdate = null;
  String nomClient;
  
  public synchronized String getErrmess2()
  {
    return this.errmess2;
  }
  
  public synchronized void setErrmess2(String errmess2)
  {
    this.errmess2 = errmess2;
  }
  
  public synchronized String getErrmess1()
  {
    return this.errmess1;
  }
  
  public synchronized void setErrmess1(String errmess1)
  {
    this.errmess1 = errmess1;
  }
  
  private ClsOGL() {}
  
  public ClsOGL(ClsGlobalUpdate globalUpdate, GeneriqueConnexionService service, String dossier, String langue, String session, String cuti)
  {
    this.dossier = dossier;
    this.langue = langue;
    this.session = session;
    this.service = service;
    this.globalUpdate = globalUpdate;
  }
  
  public static Integer _getCurrentDBMaxRhttext(GeneriqueConnexionService service, String session)
  {
    return Integer.valueOf(0);
  }
  
  public void generateOGL(HttpServletRequest request)
  {
    ParamData client = this.service.findAnyColumnFromNomenclature(this.dossier, this.langue, "266", "NOMCLIENT", "2");
    //ClsConfigurationParameters.getConfigParameterValue(this.service, this.dossier, this.langue, "NOMCLIENT");
    this.nomClient = client.getVall();
    ParamData fnom = this.service.findAnyColumnFromNomenclature(this.dossier, this.langue, "99", "PAR-CENTRA", "1");
    //(Rhfnom)this.service.get(Rhfnom.class, new RhfnomPK(this.dossier, Integer.valueOf(99), "PAR-CENTRA", Integer.valueOf(1)));
    if ((fnom != null) && (StringUtils.equalsIgnoreCase("DELTA-BANK", fnom.getVall())))
    {
      ClsOGLBank ogl = new ClsOGLBank();
      this.errmess1 = ogl.generateOGL(request, this.globalUpdate, this.service, this.dossier, this.session, this.cuti);
      return;
    }
    String rubriqueAComptabiliser = ParameterUtil.getSessionObject(request, "rubriqueAComptabiliser");
    String typeContrepartie = ParameterUtil.getSessionObject(request, "typeContrepartie");
    String compteContrepartie = ParameterUtil.getSessionObject(request, "compteContrepartie");
    if ((StringUtils.isNotBlank(rubriqueAComptabiliser)) && (StringUtils.isNotBlank(typeContrepartie)))
    {
      if (StringUtil.notEquals("F", typeContrepartie)) {
        //generateOGL_Rubrique_CompteBancaire(request, rubriqueAComptabiliser);
      } else {
        //generateOGL_Rubrique_CompteFixe(request, rubriqueAComptabiliser, compteContrepartie);
      }
      return;
    }
    ParamData nome = this.service.findAnyColumnFromNomenclature(this.dossier, this.langue, "99", "PAR-CENTRA", "3");
    //Rhfnom nome = (Rhfnom)this.service.get(Rhfnom.class, new RhfnomPK(this.dossier, Integer.valueOf(99), "PAR-CENTRA", Integer.valueOf(3)));
    if ((nome != null) && (StringUtils.equalsIgnoreCase("ISBA", nome.getVall())))
    {
      //generateOGL_ISBA(request);
      return;
    }
    if ((nome != null) && (StringUtils.equalsIgnoreCase("LAWSONM3", nome.getVall())))
    {
      //generateOGLLawson(request);
      return;
    }
    if ((nome != null) && (StringUtils.equalsIgnoreCase("SAGE500", nome.getVall())))
    {
      //generateOGLSAGE500(request);
      return;
    }
    if ((nome != null) && (StringUtils.equalsIgnoreCase("X3", nome.getVall())))
    {
      //generateOGLX3(request);
      return;
    }
    if (StringUtils.equals(this.nomClient, "SBOA"))
    {
      //generateOGLSBOA(request);
      generateFPint(request);
      return;
    }
    if (StringUtils.equals(this.nomClient, "ACTIVA"))
    {
      //generateOGLAGRESO_ACTIVA(request);
      return;
    }
    if (StringUtils.equals(this.nomClient, "CNSS"))
    {
      //generateOGLCNSS(request);
      return;
    }
    if (StringUtils.equals(this.nomClient, "EDM"))
    {
      //generateOGLEDM(request);
      return;
    }
    if (StringUtils.equals(this.nomClient, "SDV_NIGER"))
    {
      //generateOGLSDVNIGER(request);
      return;
    }
    if (StringUtils.equals(this.nomClient, "TASIAST"))
    {
      //generateOGLTASIAST(request);
      return;
    }
    if (StringUtils.equals(this.nomClient, "OILYBIA"))
    {
      generateOGLOILYBIACMR(request);
      return;
    }
    if (StringUtils.equals(this.nomClient, "COMILOG"))
    {
      //generateOGLComilog(request);
      return;
    }
    List lint = this.service.findFromCpInt("From InterfComptable where idEntreprise='" + this.dossier + "'");
    
    List l = this.service.find("from DossierPaie where idEntreprise = '" + this.dossier + "'");
    DossierPaie rhpdo = null;
    if ((l != null) && (l.size() > 0))
    {
      rhpdo = (DossierPaie)l.get(0);
    }
    else
    {
      this.errmess1 = ("Erreur en lecture du dossier No " + this.dossier);
      writeLog(this.errmess1);
      this.pb = true;
      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
      return;
    }
    ParamData nomenc = null;
    String separateur = "";
    nomenc = this.service.findAnyColumnFromNomenclature(this.dossier, "", "99", "PAIE-INTER", "5");
    //Object o = this.service.get(Rhfnom.class, new RhfnomPK(this.dossier, Integer.valueOf(99), "PAIE-INTER", Integer.valueOf(5)));
    if (nomenc != null)
    {
      //nomenc = (Rhfnom)o;
      if (!ClsObjectUtil.isNull(nomenc.getVall())) {
        separateur = nomenc.getVall().trim();
      }
    }
    else
    {
      this.errmess1 = "Erreur : Creez PAIE-INTER, Lib5 en Tb99.";
      writeLog(this.errmess1);
      this.pb = true;
      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
      return;
    }
    if (ClsObjectUtil.isNull(separateur))
    {
      this.errmess1 = "Renseignez Lib5 de PAIE-INTER en Tb99.";
      writeLog(this.errmess1);
      this.pb = true;
      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
      return;
    }
    int numero = _getCurrentDBMaxRhttext(this.service, this.session).intValue();
    
    String requete = "DELETE FROM Rhttext WHERE idEntreprise = '" + this.dossier + "' and sess='" + this.session + "'";
    Session oSession = null;
    Transaction tx = null;
    try
    {
      oSession = this.service.getSession();
      tx = oSession.beginTransaction();
      oSession.createQuery(requete).executeUpdate();
      
      InterfComptable cpint = null;
      String message = "";
      TraitementTexte text = null;
      int j = 0;
      for (int i = 0; i < lint.size(); i++)
      {
        j = i + 1;
        cpint = (InterfComptable)lint.get(i);
        
        message = "NEW" + separateur;
        if (!ClsObjectUtil.isNull(cpint.getDatcpt())) {
          message = message + new ClsDate(cpint.getDatcpt()).getDateS() + separateur;
        }
        if (!ClsObjectUtil.isNull(cpint.getCreationDate())) {
          message = message + new ClsDate(cpint.getCreationDate()).getDateS() + separateur;
        }
        message = message + cpint.getIdEntreprise() + separateur;
        message = message + cpint.getCoddes1() + separateur;
        message = message + cpint.getNumcpt() + separateur;
        message = message + cpint.getCoddes2() + separateur;
        if ("D".equals(cpint.getSens())) {
          message = message + cpint.getPceMt() + separateur;
        }
        if ("C".equals(cpint.getSens())) {
          message = message + cpint.getPceMt() + separateur;
        }
        message = message + cpint.getLibecr() + separateur;
        
        numero++;
        text = new TraitementTexte();
        text.setSess(this.session); //Comp_id(new RhttextPK(this.session, Integer.valueOf(numero)));
        text.setNlig(Integer.valueOf(numero));
        text.setIdEntreprise(new Integer(this.dossier));
        text.setTexte(message);
        
        oSession.save(text);
        if (i % 20 == 0)
        {
          oSession.flush();
          oSession.clear();
        }
      }
      //generateFPint(request, oSession);
      tx.commit();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      if (tx != null) {
        tx.rollback();
      }
      this.errmess1 = ClsTreater._getStackTrace(e);
      writeLog(this.errmess1);
      this.pb = true;
      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
      return;
    }
    finally
    {
      this.service.closeSession(oSession);
    }
  }
  
  public void generateFPint(HttpServletRequest request)
  {
    Session oSession = null;
    Transaction tx = null;
    try
    {
      oSession = this.service.getSession();
      tx = oSession.beginTransaction();
      
      generateFPint(request, oSession);
      
      tx.commit();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      if (tx != null) {
        tx.rollback();
      }
      this.errmess1 = ClsTreater._getStackTrace(e);
      writeLog(this.errmess1);
      this.pb = true;
      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
      return;
    }
    finally
    {
      this.service.closeSession(oSession);
    }
  }
  
  public void generateFPint(HttpServletRequest request, Session oSession)
  {
//    String dateFormat = ParameterUtil.getSessionObject(request, "dateformat");
//    Rhfnom fnom = (Rhfnom)this.service.get(Rhfnom.class, new RhfnomPK(this.dossier, Integer.valueOf(99), "PAR-CENTRA", Integer.valueOf(1)));
//    if ((fnom != null) && (StringUtils.equalsIgnoreCase("CASTEL", fnom.getVall())))
//    {
//      String numpce = new ClsDate(this.globalUpdate.getPeriode(), "yyyyMM").getDateS("MMyyyy") + this.globalUpdate.getNumerobulletin();
//
////      String deleteQuery = "delete from fpint where nste='" + this.dossier + "' and npce = '" + numpce + "'";
//      String deleteQuery = "delete from fpint where npce = '" + numpce + "'";
//      oSession.createSQLQuery(deleteQuery).executeUpdate();
//
//      String dt = new ClsDate(this.globalUpdate.getDateComptable()).getDateS("yyyyMMdd");
//      String currentDt = new ClsDate(new Date()).getDateS("yyyyMMdd");
//      String query = " insert into FPINT(nste,cjou,dtcp,ncpt,refl,seca,cpta,npce,dtsa,liba,libe,cptc,sens,mtdd,cour,mtdc,cdev,cuti,dtec,dtvl,mpnr,stat,cenr,dfac)";
//
////      query = query + " select '" + this.dossier + "',codjou,'" + dt + "',numcpt,' ',coddes1,' ',numpce,'" + dt;
//      query = query + " select cdos, codjou,'" + dt + "',numcpt,' ',coddes1,' ',numpce,'" + dt;
//      query = query + "',codabr,libecr,' ',sens,sum(pce_mt),'1',sum(pce_mt),devpce, 'OLI','0','" + dt + "',' ',' ','G','0'  from cp_int";
////      query = query + " where cdos='" + this.dossier + "' and datcpt='" + new ClsDate(this.globalUpdate.getDateComptable()).getDateS("yyyy-MM-dd") + "' ";
//      query = query + " where datcpt='" + new ClsDate(this.globalUpdate.getDateComptable()).getDateS("yyyy-MM-dd") + "' ";
//
//      query = query + " group by cdos, codjou,numcpt,coddes1,numpce,codabr,libecr,sens,devpce";
//      if (StringUtils.equals(this.nomClient, "SBOA"))
//      {
//        query = " insert into FPINT(nste,cjou,dtcp,ncpt,refl,seca,cpta,npce,dtsa,liba,libe,cptc,sens,mtdd,cour,mtdc,cdev,cuti,dtec,dtvl,mpnr,stat,cenr,dfac) ";
//        query = query + " select " + ClsTypeBD.souschaine("coddes1", 1, 3) + ",codjou,'" + dt + "',numcpt,numpce," + ClsTypeBD.souschaine("coddes2", 1, 4) + ",numcpt,numpce,'" + dt + "',codabr,libecr,' ',sens,sum(pce_mt) ";
//        query = query + " ,'1',sum(pce_mt),devpce, 'OLI','0','" + dt + "',' ',' ','G','0'  from cp_int ";
//        query = query + " where cdos='" + this.dossier + "' and datcpt='" + new ClsDate(this.globalUpdate.getDateComptable()).getDateS("yyyy-MM-dd") + "' ";
//        query = query + " group by " + ClsTypeBD.souschaine("coddes1", 1, 3) + ",codjou,numcpt,numpce," + ClsTypeBD.souschaine("coddes2", 1, 4) + ",numpce,codabr,libecr,sens,devpce ";
//      }
//      String col = "";
//      fnom = (Rhfnom)this.service.get(Rhfnom.class, new RhfnomPK(this.dossier, Integer.valueOf(99), "FPINT", Integer.valueOf(1)));
//      if (fnom != null) {
//        col = fnom.getVall();
//      } else {
//        col = "vild";
//      }
//      String query1 = "";
//      fnom = (Rhfnom)this.service.get(Rhfnom.class, new RhfnomPK(this.dossier, Integer.valueOf(99), "PAR-CENTRA", Integer.valueOf(3)));
//      if ((fnom != null) && (StringUtils.equalsIgnoreCase("SABC", fnom.getVall())))
//      {
//        numpce = this.globalUpdate.getPeriode();
////        deleteQuery = "delete from fpint where nste in (select codesite from Rhpagent where idEntreprise = '" + this.dossier + "') and npce = '" + numpce + "'";
//        deleteQuery = "delete from fpint where npce = '" + numpce + "'";
//        oSession.createSQLQuery(deleteQuery).executeUpdate();
//
//        query = " INSERT INTO FPINT(NSTE,CJOU,DTCP,NCPT,REFL,NSEC,CPTA,NPCE,DTSA,LIBA,LIBE,CPTC,SENS,MTDD,COUR,MTDC,CDEV,CUTI,DTEC,DTVL,MPNR,STAT,CENR,DFAC) ";
//        query = query + " SELECT a.coddes2,a.codjou,'" + dt + "', case c.typr when 'PR' then a.coddes8 else a.numcpt end as ncpt ,b." + col + ",' ',a.numcpt,'" + numpce + "','" + currentDt + "','0',a.libecr,' ',a.sens,sum(a.pce_mt) ";
//        query = query + " ,'1',sum(a.pce_mt),a.devpce, 'PAIE','0','0',' ',' ','G','0'  FROM CP_INT a, Rhpagent b, Rhprubrique c ";
//        query = query + " where a.cdos=b.cdos and a.cdos=c.cdos and a.coddes8=b.nmat and a.coddes9=c.crub ";
//        query = query + " and a.numcpt not like '6%' and";
////        query = query + " a.cdos='" + this.dossier + "' and a.datcpt='" + new ClsDate(this.globalUpdate.getDateComptable()).getDateS("yyyy-MM-dd") + "' ";
//        query = query + " a.datcpt='" + new ClsDate(this.globalUpdate.getDateComptable()).getDateS("yyyy-MM-dd") + "' ";
//        query = query + " group by a.coddes2,a.codjou,b." + col + ",case c.typr when 'PR' then a.coddes8 else a.numcpt end,a.numcpt,a.libecr,a.sens,a.devpce ";
//
//        query1 = " INSERT INTO FPINT(NSTE,CJOU,DTCP,NCPT,REFL,NSEC,CPTA,NPCE,DTSA,LIBA,LIBE,CPTC,SENS,MTDD,COUR,MTDC,CDEV,CUTI,DTEC,DTVL,MPNR,STAT,CENR,DFAC) ";
//        query1 = query1 + " SELECT a.coddes2,a.codjou,'" + dt + "', case c.typr when 'PR' then a.coddes8 else a.numcpt end as ncpt ,b." + col + ",a.coddes1,a.numcpt,'" + numpce + "','" + currentDt + "','0',a.libecr,' ',a.sens,sum(a.pce_mt) ";
//        query1 = query1 + " ,'1',sum(a.pce_mt),a.devpce, 'PAIE','0','0',' ',' ','G','0'  FROM CP_INT a, Rhpagent b, Rhprubrique c ";
//        query1 = query1 + " where a.cdos=b.cdos and a.cdos=c.cdos and a.coddes8=b.nmat and a.coddes9=c.crub ";
//        query1 = query1 + " and a.numcpt like '6%' and";
////        query1 = query1 + " a.cdos='" + this.dossier + "' and a.datcpt='" + new ClsDate(this.globalUpdate.getDateComptable()).getDateS("yyyy-MM-dd") + "' ";
//        query1 = query1 + " a.datcpt='" + new ClsDate(this.globalUpdate.getDateComptable()).getDateS("yyyy-MM-dd") + "' ";
//        query1 = query1 + " group by a.coddes2,a.codjou,b." + col + ",case c.typr when 'PR' then a.coddes8 else a.numcpt end,a.numcpt,a.coddes1,a.libecr,a.sens,a.devpce ";
//      }
//      if ((fnom != null) && (StringUtils.equalsIgnoreCase("GLOBAL", fnom.getVall())))
//      {
//        numpce = this.globalUpdate.getPeriode();
//
//        deleteQuery = "delete from fpint where nste='" + this.dossier + "' and npce = '" + numpce + "'";
//        oSession.createSQLQuery(deleteQuery).executeUpdate();
//
//        String nmat1 = ClsTypeBD.sqlPad("a.coddes8", 6, '0', true);
//
//        String nmat = "(case b.ccpt when '' then " + nmat1 + " else b.ccpt end)";
//
//        query = " INSERT INTO FPINT(NSTE,CJOU,DTCP,NCPT,REFL,SECA,CPTA,NPCE,DTSA,LIBA,LIBE,CPTC,SENS,MTDD,COUR,MTDC,CDEV,CUTI,DTEC,DTVL,MPNR,STAT,CENR,DFAC) ";
//        query = query + " SELECT '" + this.dossier + "',a.codjou,'" + dt + "', case c.typr when 'GM' then " + nmat + " when 'RM' then " + nmat + " else a.numcpt end as ncpt ,' ',' ',a.numcpt,'" + numpce + "','" + currentDt + "','0',a.libecr,' ',a.sens,sum(a.pce_mt) ";
//        query = query + " ,'1',sum(a.pce_mt),a.devpce, 'PAIE','0','0',' ',' ','G','0'  FROM CP_INT a, Rhpagent b, Rhprubrique c ";
//        query = query + " where a.cdos=b.cdos and a.cdos=c.cdos and a.coddes8=b.nmat and a.coddes9=c.crub ";
//        query = query + " and a.numcpt not like '6%' and";
//        query = query + " a.cdos='" + this.dossier + "' and a.datcpt='" + new ClsDate(this.globalUpdate.getDateComptable()).getDateS(dateFormat) + "' ";
//        query = query + " group by a.codjou,case c.typr when 'GM' then " + nmat + " when 'RM' then " + nmat + " else a.numcpt end,a.numcpt,a.libecr,a.sens,a.devpce ";
//
//        query1 = " INSERT INTO FPINT(NSTE,CJOU,DTCP,NCPT,REFL,SECA,CPTA,NPCE,DTSA,LIBA,LIBE,CPTC,SENS,MTDD,COUR,MTDC,CDEV,CUTI,DTEC,DTVL,MPNR,STAT,CENR,DFAC) ";
//        query1 = query1 + " SELECT '" + this.dossier + "',a.codjou,'" + dt + "', a.numcpt ,' ',a.coddes1,a.numcpt,'" + numpce + "','" + currentDt + "','0',a.libecr,' ',a.sens,sum(a.pce_mt) ";
//        query1 = query1 + " ,'1',sum(a.pce_mt),a.devpce, 'PAIE','0','0',' ',' ','G','0'  FROM CP_INT a, Rhpagent b, Rhprubrique c ";
//        query1 = query1 + " where a.cdos=b.cdos and a.cdos=c.cdos and a.coddes8=b.nmat and a.coddes9=c.crub ";
//        query1 = query1 + " and a.numcpt like '6%' and";
//        query1 = query1 + " a.cdos='" + this.dossier + "' and a.datcpt='" + new ClsDate(this.globalUpdate.getDateComptable()).getDateS(dateFormat) + "' ";
//        query1 = query1 + " group by a.codjou,a.coddes1,a.numcpt,a.coddes1,a.libecr,a.sens,a.devpce ";
//      }
//      if (StringUtils.equals(this.nomClient, "BBLOME"))
//      {
//        numpce = this.globalUpdate.getPeriode();
//
//        deleteQuery = "delete from fpint where nste='" + this.dossier + "'";
//        oSession.createSQLQuery(deleteQuery).executeUpdate();
//
//        String nmat1 = ClsTypeBD.sqlPad("a.coddes8", 6, '0', true);
//
//        String nmat = "(case coalesce(b.ccpt,'') when '' then " + nmat1 + " else b.ccpt end)";
//
//        query = " INSERT INTO FPINT(NSTE,CJOU,DTCP,NCPT,REFL,SECA,CPTA,NPCE,DTSA,LIBA,LIBE,CPTC,SENS,MTDD,COUR,MTDC,CDEV,CUTI,DTEC,DTVL,MPNR,STAT,CENR,DFAC) ";
//        query = query + " SELECT '" + this.dossier + "',a.codjou,'" + dt + "', case c.typr when 'GM' then " + nmat + " when 'RM' then " + nmat + " else a.numcpt end as ncpt ,' ',' ',a.numcpt,'" + numpce + "','" + currentDt + "','0',a.libecr,' ',a.sens,sum(a.pce_mt) ";
//        query = query + " ,'1',sum(a.pce_mt),a.devpce, 'PAIE','0','0',' ',' ','G','0'  FROM CP_INT a, Rhpagent b, Rhprubrique c ";
//        query = query + " where a.cdos=b.cdos and a.cdos=c.cdos and a.coddes8=b.nmat and a.coddes9=c.crub ";
//        query = query + " and a.numcpt not like '6%' and";
//        query = query + " a.cdos='" + this.dossier + "' and a.datcpt='" + new ClsDate(this.globalUpdate.getDateComptable()).getDateS(dateFormat) + "' ";
//        query = query + " group by a.codjou,case c.typr when 'GM' then " + nmat + " when 'RM' then " + nmat + " else a.numcpt end,a.numcpt,a.libecr,a.sens,a.devpce ";
//
//        query1 = " INSERT INTO FPINT(NSTE,CJOU,DTCP,NCPT,REFL,SECA,CPTA,NPCE,DTSA,LIBA,LIBE,CPTC,SENS,MTDD,COUR,MTDC,CDEV,CUTI,DTEC,DTVL,MPNR,STAT,CENR,DFAC) ";
//        query1 = query1 + " SELECT '" + this.dossier + "',a.codjou,'" + dt + "', a.numcpt ,' ',a.coddes1,a.numcpt,'" + numpce + "','" + currentDt + "','0',a.libecr,b.zli2,a.sens,sum(a.pce_mt) ";
//        query1 = query1 + " ,'1',sum(a.pce_mt),a.devpce, 'PAIE','0','0',' ',' ','G','0'  FROM CP_INT a, Rhpagent b, Rhprubrique c ";
//        query1 = query1 + " where a.cdos=b.cdos and a.cdos=c.cdos and a.coddes8=b.nmat and a.coddes9=c.crub ";
//        query1 = query1 + " and a.numcpt like '6%' and";
//        query1 = query1 + " a.cdos='" + this.dossier + "' and a.datcpt='" + new ClsDate(this.globalUpdate.getDateComptable()).getDateS(dateFormat) + "' ";
//        query1 = query1 + " group by a.codjou,a.coddes1,a.numcpt,a.coddes1,a.libecr,b.zli2,a.sens,a.devpce ";
//      }
//      if (StringUtils.isNotBlank(query1)) {
//        oSession.createSQLQuery(query1).executeUpdate();
////        System.out.println("QUERY 1 : "+query1);
//      }
//      oSession.createSQLQuery(query).executeUpdate();
////      System.out.println("QUERY : "+query);
//    }
  }
  
//  public void generateOGLCNSS(HttpServletRequest request)
//  {
//    String separateur = "";
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//    String ppce = StringUtils.getEmptyString(4);
//
//    String pdpc = StringUtils.getEmptyString(18);
//
//    String pnps = StringUtils.getEmptyString(8);
//
//    String pnpj = StringUtils.getEmptyString(8);
//
//    String plib = StringUtils.getEmptyString(30);
//
//    String ptau = StringUtils.getEmptyString(17);
//
//    String pdsp = StringUtils.getEmptyString(18);
//
//    String psta = StringUtils.getEmptyString(1);
//
//    String peta = StringUtils.getEmptyString(4);
//
//    String pexe = StringUtils.getEmptyString(4);
//
//    String puti = StringUtils.getEmptyString(15);
//
//    String pjou = StringUtils.getEmptyString(10);
//
//    String pdev = StringUtils.getEmptyString(3);
//
//    String pptr = StringUtils.getEmptyString(5);
//
//    String pver = StringUtils.getEmptyString(1);
//
//    String ppgm = StringUtils.getEmptyString(30);
//
//    String pmdp = StringUtils.getEmptyString(20);
//
//    String pmcp = StringUtils.getEmptyString(20);
//
//    String pnli = StringUtils.getEmptyString(5);
//
//    String pptc = StringUtils.getEmptyString(5);
//
//    String putc = StringUtils.getEmptyString(15);
//
//    String pdcp = StringUtils.getEmptyString(18);
//
//    String pmdd = StringUtils.getEmptyString(20);
//
//    String pmcd = StringUtils.getEmptyString(20);
//
//
//    String mmvt = StringUtils.getEmptyString(4);
//
//    String mnum = StringUtils.getEmptyString(4);
//
//    String mmon = StringUtils.getEmptyString(20);
//
//    String msen = StringUtils.getEmptyString(1);
//
//    String mdvm = StringUtils.getEmptyString(18);
//
//    String mdem = StringUtils.getEmptyString(18);
//
//    String mrpj = StringUtils.getEmptyString(20);
//
//    String meta = StringUtils.getEmptyString(4);
//
//    String mexe = StringUtils.getEmptyString(4);
//
//    String mnps = StringUtils.getEmptyString(8);
//
//    String mtpj = StringUtils.getEmptyString(4);
//
//    String mtie = StringUtils.getEmptyString(15);
//
//    String mcom = StringUtils.getEmptyString(15);
//
//    String mges = StringUtils.getEmptyString(8);
//
//    String mdsm = StringUtils.getEmptyString(18);
//
//    String mlib = StringUtils.getEmptyString(30);
//
//    String mtge = StringUtils.getEmptyString(1);
//
//    String mtle = StringUtils.getEmptyString(1);
//
//    String mmle = StringUtils.getEmptyString(20);
//
//    String mrmv = StringUtils.getEmptyString(1);
//
//    String mdsp = StringUtils.getEmptyString(18);
//
//    String mjou = StringUtils.getEmptyString(10);
//
//    String mnpj = StringUtils.getEmptyString(8);
//
//    String mmdd = StringUtils.getEmptyString(20);
//
//    String mmcd = StringUtils.getEmptyString(20);
//
//
//    String aana = StringUtils.getEmptyString(4);
//
//    String anum = StringUtils.getEmptyString(4);
//
//    String amon = StringUtils.getEmptyString(20);
//
//    String aimp = StringUtils.getEmptyString(8);
//
//
//    String esp1 = StringUtils.getEmptyString(1);
//
//    String esp2 = StringUtils.getEmptyString(2);
//
//    String esp3 = StringUtils.getEmptyString(3);
//
//    String esp5 = StringUtils.getEmptyString(5);
//
//    String esp31 = StringUtils.getEmptyString(31);
//
//
//
//    CpInt cpint = null;
//
//
//    String codets = null;
//
//
//
//
//
//    String numpce = null;
//
//
//
//
//
//
//
//
//
//    Date datcpt = null;
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//    String curs_01 = "";
//    curs_01 = " SELECT codets, numcpt, numtie, numpce, datcpt, sens, coddes1, coddes2, coddes3, coddes4, libecr, sum(pce_mt) as pce_mt ";
//    curs_01 = curs_01 + " From Cp_int where idEntreprise = '" + this.dossier + "'";
//    curs_01 = curs_01 + " group by codets, numcpt, numtie, numpce, datcpt, sens, coddes1, coddes2, coddes3, coddes4, libecr";
//    curs_01 = curs_01 + " order by datcpt, numpce, codets, numcpt, numtie, coddes2, coddes1, sens";
//
//
//
//
//
//
//
//
//
//
//
//    String curs_02 = "";
//    curs_02 = " SELECT pcea,cg,gesa,cana,ruba,sena,mona From cp_analyt where idEntreprise = '" + this.dossier + "' ";
//    curs_02 = curs_02 + " and pcea = :numpce";
//    curs_02 = curs_02 + " and cg = :numcpt";
//    curs_02 = curs_02 + " and gesa = :codges";
//    curs_02 = curs_02 + " and ruba = :coddes1";
//
//
//
//
//
//
//
//
//
//
//
//
//    List l = this.service.find("from Rhpdo where idEntreprise = '" + this.dossier + "'");
//    Rhpdo rhpdo = null;
//    if ((l != null) && (l.size() > 0))
//    {
//      rhpdo = (Rhpdo)l.get(0);
//    }
//    else
//    {
//      this.errmess1 = ("Erreur en lecture du dossier No " + this.dossier);
//      writeLog(this.errmess1);
//      this.pb = true;
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
//      return;
//    }
//    Rhfnom nome = null;
//    Object o = this.service.get(Rhfnom.class, new RhfnomPK(this.dossier, Integer.valueOf(99), "INT-GI", Integer.valueOf(1)));
//    if (o == null)
//    {
//      this.errmess1 = "Creez INT-GI en table 99 (nomenclatures)";
//      writeLog(this.errmess1);
//      this.pb = true;
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
//      return;
//    }
//    nome = (Rhfnom)o;
//    if (StringUtils.nvl(nome.getVall(), " ").compareTo(" ") > 0) {
//      pdev = StringUtils.oraRPad(StringUtil.oraSubstring(nome.getVall(), 1, 3), 3, ' ');
//    } else {
//      pdev = "CFA";
//    }
//    Integer taux;
//    if (nome.getValt() == null) {
//      taux = Integer.valueOf(1);
//    } else {
//      taux = Integer.valueOf(nome.getValt().intValue());
//    }
//    o = this.service.get(Rhfnom.class, new RhfnomPK(this.dossier, Integer.valueOf(99), "INT-GI", Integer.valueOf(2)));
//    if (o == null)
//    {
//      this.errmess1 = "Creez INT-GI en table 99 (nomenclatures)";
//      writeLog(this.errmess1);
//      this.pb = true;
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
//      return;
//    }
//    nome = (Rhfnom)o;
//    if (StringUtils.nvl(nome.getVall(), " ").compareTo(" ") > 0)
//    {
//      pjou = StringUtils.oraRPad(StringUtil.oraSubstring(nome.getVall(), 1, 10), 10, ' ');
//      mnpj = StringUtils.oraRPad(StringUtil.oraSubstring(nome.getVall(), 1, 8), 8, ' ');
//      mjou = StringUtils.oraRPad(StringUtil.oraSubstring(nome.getVall(), 1, 10), 10, ' ');
//    }
//    else
//    {
//      pjou = "65000     ";
//      mnpj = "65000   ";
//      mjou = "65000     ";
//    }
//    ppce = "PCE ";
//    psta = "N";
//    puti = "CNSS           ";
//    pptr = "99999";
//    pver = "N";
//    ppgm = "GRH                           ";
//    pptc = "99999";
//    putc = "CNSS           ";
//
//    mmvt = "MVT ";
//    mdvm = "                  ";
//    mdem = "                  ";
//    mtpj = "PME ";
//    mtge = "M";
//    mtle = "N";
//    mmle = "00000000000000000000";
//    mrmv = "N";
//
//    aana = "ANA ";
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//    o = this.service.get(Rhfnom.class, new RhfnomPK(this.dossier, Integer.valueOf(99), "PAIE-INTER", Integer.valueOf(5)));
//    if (o == null)
//    {
//      this.errmess1 = "Erreur : Creez PAIE-INTER, Lib5 en Tb99.";
//      writeLog(this.errmess1);
//      this.pb = true;
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
//      return;
//    }
//    nome = (Rhfnom)o;
//    if (StringUtils.isBlank(nome.getVall())) {
//      separateur = " ";
//    } else {
//      separateur = StringUtil.oraSubstring(nome.getVall(), 1, 1);
//    }
//    Integer nblig = Integer.valueOf(0);
//    Integer nummvt = Integer.valueOf(0);
//    Integer numligpce = Integer.valueOf(0);
//    BigDecimal totdeb = new BigDecimal(0);
//    BigDecimal totcre = new BigDecimal(0);
//    BigDecimal totdebdev = new BigDecimal(0);
//    BigDecimal totcredev = new BigDecimal(0);
//    String anc_numpce = " ";
//    String anc_codets = " ";
//
//
//
//
//
//
//    Session _o_Session = this.service.getSession();
//    Transaction tx = null;
//    Connection oConnexion = null;
//    Statement oStatement = null;
//    ResultSet oResultSet = null;
//    CpInt _o_Data = null;
//    List<Object[]> list = null;
//    String pcea = null;String cg = null;String gesa = null;String cana = null;String ruba = null;String sena = null;
//    Integer mona = Integer.valueOf(0);
//    try
//    {
//      tx = _o_Session.beginTransaction();
//      oConnexion = _o_Session.connection();
//      oStatement = oConnexion.createStatement();
//      oResultSet = oStatement.executeQuery(curs_01);
//      Query q = null;
//      Rhttext texte = null;
//
//      Integer i = Integer.valueOf(0);
//      while (oResultSet.next())
//      {
//        BigDecimal pce_mt_dev;
//        BigDecimal pce_mt;
//        String sens;
//        String libecr;
//        String coddes4;
//        String coddes3;
//        String codges;
//        String coddes1;
//        String numtie;
//        String numcpt;
//        String ligne;
//        try
//        {
//          if (i.intValue() % 20 == 0)
//          {
//            _o_Session.flush();
//            _o_Session.clear();
//          }
//          codets = oResultSet.getString("codets");
//          numcpt = oResultSet.getString("numcpt");
//          numtie = oResultSet.getString("numtie");
//          numpce = oResultSet.getString("numpce");
//          datcpt = oResultSet.getDate("datcpt");
//          sens = oResultSet.getString("sens");
//          coddes1 = oResultSet.getString("coddes1");
//          codges = oResultSet.getString("coddes2");
//          coddes3 = oResultSet.getString("coddes3");
//          coddes4 = oResultSet.getString("coddes4");
//          libecr = oResultSet.getString("libecr");
//          pce_mt = oResultSet.getBigDecimal("pce_mt");
//
//          nblig = Integer.valueOf(nblig.intValue() + 1);
//          if ((StringUtils.notEquals(numpce, anc_numpce)) || (StringUtils.notEquals(codets, anc_codets)))
//          {
//            if ((StringUtils.notEquals(anc_numpce, " ")) || (StringUtils.notEquals(anc_codets, " ")))
//            {
//              pdpc = ClsDate.getDateS(datcpt, "dd/MM/yyyy") + "12:00:00";
//
//              pnps = StringUtils.oraRPad(StringUtils.oraLTrim(anc_numpce, " "), 8, ' ');
//
//              pnpj = pnps;
//
//
//              plib = StringUtils.oraRPad("PAIE DU MOIS DE " + ClsDate.getDateS(datcpt, "MM/yyyy"), 30, ' ');
//
//
//              ptau = StringUtil.oraSubstring(ClsStringUtil.oraLPad(StringUtils.oraLTrim(StringUtils.oraToChar(taux), " "), 17, "0"), 1, 17);
//
//              pdsp = pdpc;
//
//              peta = StringUtils.oraRPad(StringUtils.oraLTrim(anc_codets, " "), 4, ' ');
//
//              pexe = ClsDate.getDateS(datcpt, "yyyy");
//
//
//              pmdp = StringUtil.oraSubstring(ClsStringUtil.oraLPad(StringUtils.oraLTrim(StringUtils.oraToChar(totdeb), " "), 20, "0"), 1, 20);
//
//
//              pmcp = StringUtil.oraSubstring(ClsStringUtil.oraLPad(StringUtils.oraLTrim(StringUtils.oraToChar(totcre), " "), 20, "0"), 1, 20);
//
//
//              pnli = StringUtil.oraSubstring(ClsStringUtil.oraLPad(StringUtils.oraLTrim(StringUtils.oraToChar(nummvt), " "), 5, "0"), 1, 5);
//
//              pdcp = pdpc;
//
//
//              pmdd = StringUtil.oraSubstring(ClsStringUtil.oraLPad(StringUtils.oraLTrim(StringUtils.oraToChar(totdebdev), " "), 20, "0"), 1, 20);
//
//
//              pmcd = StringUtil.oraSubstring(ClsStringUtil.oraLPad(StringUtils.oraLTrim(StringUtils.oraToChar(totcredev), " "), 20, "0"), 1, 20);
//
//
//
//
//
//
//              ligne = ppce + pdpc + esp1 + pnps + esp2 + pnpj + esp1 + plib + esp1 + ptau + esp1 + pdsp + esp2 + psta + esp1 + peta + esp1 + pexe +
//                esp1 + puti + esp1 + pjou + esp1 + pdev + esp1 + pptr + esp1 + pver + esp1 + ppgm + esp1 + pmdp + esp1 + pmcp + esp1 +
//                pnli + esp1 + pptc + esp1 + putc + esp1 + pdcp + esp5 + pmdd + esp1 + pmcd;
//
//
//
//
//
//              q = _o_Session.createSQLQuery("Update Rhttext set texte = :texte where sess = :sess and cdos = :cdos and nlig = :nlig");
//              q.setParameter("texte", ligne, Hibernate.STRING);
//              q.setParameter("sess", this.session, Hibernate.STRING);
//              q.setParameter("cdos", this.dossier, Hibernate.STRING);
//              q.setParameter("nlig", numligpce, Hibernate.INTEGER);
//              q.executeUpdate();
//
//
//              totdeb = new BigDecimal(0);
//
//              totcre = new BigDecimal(0);
//
//              totdebdev = new BigDecimal(0);
//
//              totcredev = new BigDecimal(0);
//
//              nummvt = Integer.valueOf(0);
//            }
//            anc_numpce = numpce;
//
//            anc_codets = codets;
//
//            numligpce = nblig;
//
//
//
//            pdpc = ClsDate.getDateS(datcpt, "dd/MM/yyyy") + "12:00:00";
//
//            pnps = StringUtils.oraRPad(StringUtils.oraLTrim(numpce, " "), 8, ' ');
//
//            pnpj = pnps;
//
//
//            plib = StringUtils.oraRPad("PAIE DU MOIS DE " + ClsDate.getDateS(datcpt, "MM/yyyy"), 30, ' ');
//
//
//            ptau = StringUtil.oraSubstring(ClsStringUtil.oraLPad(StringUtils.oraLTrim(StringUtils.oraToChar(taux), " "), 17, "0"), 1, 17);
//
//            pdsp = pdpc;
//
//            peta = StringUtils.oraRPad(StringUtils.oraLTrim(codets, " "), 4, ' ');
//
//            pexe = ClsDate.getDateS(datcpt, "yyyy");
//
//            pmdp = "00000000000000000000";
//
//            pmcp = "00000000000000000000";
//
//            pnli = "00000";
//
//            pdcp = pdpc;
//
//            pmdd = "00000000000000000000";
//
//            pmcd = "00000000000000000000";
//
//
//
//
//
//
//            ligne = ppce + pdpc + esp1 + pnps + esp2 + pnpj + esp1 + plib + esp1 + ptau + esp1 + pdsp + esp2 + psta + esp1 + peta + esp1 + pexe +
//              esp1 + puti + esp1 + pjou + esp1 + pdev + esp1 + pptr + esp1 + pver + esp1 + ppgm + esp1 + pmdp + esp1 + pmcp + esp1 + pnli +
//              esp1 + pptc + esp1 + putc + esp1 + pdcp + esp5 + pmdd + esp1 + pmcd;
//
//
//
//
//
//
//
//
//
//
//
//            texte = new Rhttext();
//            texte.setComp_id(new RhttextPK(this.session, nblig));
//            texte.setCdos(this.dossier);
//            texte.setTexte(ligne);
//            _o_Session.save(texte);
//            i = Integer.valueOf(i.intValue() + 1);
//
//
//            nblig = Integer.valueOf(nblig.intValue() + 1);
//          }
//          nummvt = Integer.valueOf(nummvt.intValue() + 1);
//
//
//
//
//
//          mnum = StringUtils.oraRPad(StringUtils.oraLTrim(StringUtils.oraToChar(nummvt), " "), 4, ' ');
//
//
//
//
//
//          mmon = StringUtil.oraSubstring(ClsStringUtil.oraLPad(StringUtils.oraLTrim(StringUtils.oraToChar(pce_mt), " "), 20, "0"), 1, 20);
//
//          msen = sens;
//
//
//          mrpj = StringUtils.oraRPad("PAIE DU " + ClsDate.getDateS(datcpt, "dd/MM/yyyy"), 20, ' ');
//
//          meta = StringUtils.oraRPad(StringUtils.oraLTrim(codets, " "), 4, ' ');
//
//          mexe = ClsDate.getDateS(datcpt, "yyyy");
//
//          mnps = numpce;
//          if (StringUtils.equals(coddes4, "N")) {
//            mtie = "               ";
//          } else {
//            mtie = StringUtils.oraRPad(StringUtils.oraLTrim(numtie, " "), 15, ' ');
//          }
//          mcom = StringUtils.oraRPad(StringUtils.oraLTrim(numcpt, " "), 15, ' ');
//          if (StringUtils.equals(codges, "N")) {
//            mges = "        ";
//          } else {
//            mges = StringUtils.oraRPad(StringUtils.oraLTrim(codges, " "), 8, ' ');
//          }
//          mdsm = ClsDate.getDateS(datcpt, "dd/MM/yyyy") + "12:00:00";
//
//          mlib = StringUtils.oraRPad(StringUtils.oraLTrim(libecr, " "), 30, ' ');
//
//          mdsp = mdsm;
//
//          pce_mt_dev = pce_mt.multiply(new BigDecimal(taux.intValue()));
//          if (StringUtils.equals(sens, "D"))
//          {
//            mmdd = StringUtil.oraSubstring(ClsStringUtil.oraLPad(StringUtils.oraLTrim(StringUtils.oraToChar(pce_mt_dev), " "), 20, "0"), 1, 20);
//
//            mmcd = "00000000000000000000";
//
//            totdeb = totdeb.add(pce_mt);
//
//            totdebdev = totdebdev.add(pce_mt_dev);
//          }
//          else
//          {
//            mmdd = "00000000000000000000";
//
//
//
//
//
//            mmcd = StringUtil.oraSubstring(ClsStringUtil.oraLPad(StringUtils.oraLTrim(StringUtils.oraToChar(pce_mt_dev), " "), 20, "0"), 1, 20);
//
//            totcre = totcre.add(pce_mt);
//
//            totcredev = totcredev.add(pce_mt_dev);
//          }
//          ligne = mmvt + mnum + mmon + esp2 + msen + esp1 + mdvm + esp2 + mdem + esp2 + mrpj + esp1 + meta + esp1 + mexe + esp1 + mnps + esp1 + mtpj +
//            esp1 + mtie + esp1 + mcom + esp1 + mges + esp1 + mdsm + esp2 + mlib + esp31 + mtge + esp1 + mtle + esp1 + mmle + esp1 + mrmv +
//            esp1 + mdsp + esp2 + mjou + esp3 + mnpj + esp1 + mmdd + mmcd;
//
//
//
//
//
//
//
//
//
//
//
//
//          texte = new Rhttext();
//          texte.setComp_id(new RhttextPK(this.session, nblig));
//          texte.setCdos(this.dossier);
//          texte.setTexte(ligne);
//          _o_Session.save(texte);
//          i = Integer.valueOf(i.intValue() + 1);
//          if (StringUtils.equals(coddes3, "O"))
//          {
//            Integer numana = Integer.valueOf(0);
//
//            curs_02 = " SELECT pcea,cg,gesa,cana,ruba,sena,mona From cp_analyt where idEntreprise = '" + this.dossier + "' ";
//            curs_02 = curs_02 + " and pcea = '" + numpce + "'";
//            curs_02 = curs_02 + " and cg = '" + numcpt + "'";
//            curs_02 = curs_02 + " and gesa = '" + codges + "'";
//            curs_02 = curs_02 + " and ruba = '" + coddes1 + "'";
//            curs_02 = curs_02 + " and sena = '" + sens + "'";
//            curs_02 = curs_02 + " order by cdos,pcea,cg,gesa,cana,ruba,sena,mona ";
//
//
//            list = _o_Session.createSQLQuery(curs_02).list();
//            for (Object[] obj : list)
//            {
//              if (obj[0] != null) {
//                pcea = obj[0].toString();
//              }
//              if (obj[1] != null) {
//                cg = obj[1].toString();
//              }
//              if (obj[2] != null) {
//                gesa = obj[2].toString();
//              }
//              if (obj[3] != null) {
//                cana = obj[3].toString();
//              }
//              if (obj[4] != null) {
//                ruba = obj[4].toString();
//              }
//              if (obj[5] != null) {
//                sena = obj[5].toString();
//              }
//              if (obj[6] != null) {
//                mona = Integer.valueOf(obj[6].toString());
//              }
//              nblig = Integer.valueOf(nblig.intValue() + 1);
//
//              numana = Integer.valueOf(numana.intValue() + 1);
//
//
//
//
//
//
//              anum = StringUtils.oraRPad(StringUtils.oraLTrim(StringUtils.oraToChar(numana), " "), 4, ' ');
//
//
//
//
//
//              amon = StringUtil.oraSubstring(ClsStringUtil.oraLPad(StringUtils.oraLTrim(StringUtils.oraToChar(mona), " "), 20, "0"), 1, 20);
//
//              aimp = StringUtils.oraRPad(StringUtils.oraLTrim(cana, " "), 8, ' ');
//
//
//              ligne = aana + anum + amon + esp2 + aimp;
//
//
//
//
//
//
//
//
//
//
//
//
//              texte = new Rhttext();
//              texte.setComp_id(new RhttextPK(this.session, nblig));
//              texte.setCdos(this.dossier);
//              texte.setTexte(ligne);
//              _o_Session.save(texte);
//              i = Integer.valueOf(i.intValue() + 1);
//            }
//          }
//        }
//        catch (Exception e)
//        {
//          e.printStackTrace();
//        }
//        finally
//        {
//          _o_Data = null;
//        }
//      }
//      if (numligpce.intValue() > 0)
//      {
//        pdpc = ClsDate.getDateS(datcpt, "dd/MM/yyyy") + "12:00:00";
//
//        pnps = StringUtils.oraRPad(StringUtils.oraLTrim(numpce, " "), 8, ' ');
//
//        pnpj = pnps;
//
//
//        plib = StringUtils.oraRPad("PAIE DU MOIS DE " + ClsDate.getDateS(datcpt, "MM/yyyy"), 30, ' ');
//
//
//        ptau = StringUtil.oraSubstring(ClsStringUtil.oraLPad(StringUtils.oraLTrim(StringUtils.oraToChar(taux), " "), 17, "0"), 1, 17);
//
//        pdsp = pdpc;
//
//        peta = StringUtils.oraRPad(StringUtils.oraLTrim(codets, " "), 4, ' ');
//
//        pexe = ClsDate.getDateS(datcpt, "yyyy");
//
//
//
//
//
//        pmdp = StringUtil.oraSubstring(ClsStringUtil.oraLPad(StringUtils.oraLTrim(StringUtils.oraToChar(totdeb), " "), 20, "0"), 1, 20);
//
//
//
//
//
//        pmcp = StringUtil.oraSubstring(ClsStringUtil.oraLPad(StringUtils.oraLTrim(StringUtils.oraToChar(totcre), " "), 20, "0"), 1, 20);
//
//
//
//
//
//        pnli = StringUtil.oraSubstring(ClsStringUtil.oraLPad(StringUtils.oraLTrim(StringUtils.oraToChar(nummvt), " "), 5, "0"), 1, 5);
//
//        pdcp = pdpc;
//
//
//
//
//
//        pmdd = StringUtil.oraSubstring(ClsStringUtil.oraLPad(StringUtils.oraLTrim(StringUtils.oraToChar(totdebdev), " "), 20, "0"), 1, 20);
//
//
//
//
//
//        pmcd = StringUtil.oraSubstring(ClsStringUtil.oraLPad(StringUtils.oraLTrim(StringUtils.oraToChar(totcredev), " "), 20, "0"), 1, 20);
//
//
//
//
//
//
//
//
//        String ligne = ppce + pdpc + esp1 + pnps + esp2 + pnpj + esp1 + plib + esp1 + ptau + esp1 + pdsp + esp2 + psta + esp1 + peta + esp1 + pexe + esp1 +
//          puti + esp1 + pjou + esp1 + pdev + esp1 + pptr + esp1 + pver + esp1 + ppgm + esp1 + pmdp + esp1 + pmcp + esp1 + pnli + esp1 + pptc +
//          esp1 + putc + esp1 + pdcp + esp5 + pmdd + esp1 + pmcd;
//
//
//
//
//
//
//
//        q = _o_Session.createSQLQuery("Update Rhttext set texte = :texte where sess = :sess and cdos = :cdos and nlig = :nlig");
//        q.setParameter("texte", ligne, Hibernate.STRING);
//        q.setParameter("sess", this.session, Hibernate.STRING);
//        q.setParameter("cdos", this.dossier, Hibernate.STRING);
//        q.setParameter("nlig", numligpce, Hibernate.INTEGER);
//        q.executeUpdate();
//      }
//      tx.commit();
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//      if (tx != null) {
//        tx.rollback();
//      }
//      this.errmess1 = ClsTreater._getStackTrace(e);
//      writeLog(this.errmess1);
//      this.pb = true;
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
//      return;
//    }
//    finally
//    {
//      try
//      {
//        oResultSet.close();
//        oResultSet = null;
//        oStatement.close();
//        oStatement = null;
//        oConnexion.close();
//        oConnexion = null;
//        this.service.closeConnexion(_o_Session);
//      }
//      catch (Exception e)
//      {
//        e.printStackTrace();
//      }
//    }
//  }
//
//  public void generateOGLEDM(HttpServletRequest request)
//  {
//    String query = "SELECT ";
//    query = query + "'NEW'||'|'\t\t\t\t\t\t";
//    query = query + "||'23'||'|'\t\t\t\t\t\t";
//    query = query + "||'Paie'||'|'\t\t\t\t";
//    query = query + "||'Paie'||'|'\t\t\t\t\t";
//    query = query + "||datcpt||'|'\t\t\t\t\t";
//    query = query + "||'XOF'||'|'\t\t\t\t\t";
//    query = query + "||datcre||'|'\t\t\t\t\t";
//    query = query + "||'1165'||'|'\t\t\t\t\t";
//    query = query + "||'A'||'|'\t\t\t\t\t\t";
//    query = query + "||CDOS||'|'\t\t\t\t\t\t\t\t\t\t\t";
//
//    query = query + "||decode(substr(numcpt,1,1),2,'00',4,'00',coddes1)||'|'\t\t\t";
//
//    query = query + "||NUMCPT||'|'\t\t\t\t\t\t\t\t\t\t";
//
//    query = query + "||decode(substr(numcpt,1,1),2,'00000',4,'00000',coddes2)||'|'\t\t";
//
//    query = query + "||decode(substr(numcpt,1,1),2,'000',4,'000',coddes3)||'|'\t\t\t";
//
//    query = query + "||decode(substr(numcpt,1,1),4,'T'||coddes2,NVL(CODDES4,'000000'))||'|'\t";
//
//    query = query;
//    query = query + "||decode(substr(numcpt,1,1),2,'000',4,'000',DECODE(coddes2,'25000','SCP','000'))||'|'\t\t";
//
//    query = query + "||NVL(CODDES6,'000000')||'|'\t\t\t\t\t\t\t\t";
//
//    query = query + "||NVL(CODDES7,'000000')||'|'\t\t\t\t\t\t\t\t";
//
//    query = query + "||DECODE(SENS,'D',TO_CHAR(PCE_MT),null)||'|'\t";
//
//    query = query + "||DECODE(SENS,'C',TO_CHAR(PCE_MT),null)||'|'\t";
//
//    query = query + "||DECODE(SENS,'D',TO_CHAR(PCE_MT),null)||'|'\t";
//
//    query = query + "||DECODE(SENS,'C',TO_CHAR(PCE_MT),null)||'|'\t";
//
//    query = query + "||'DELTA PAIE'||'|'\t\t\t\t";
//
//    query = query + "||CODJOU||'|'\t\t\t\t\t";
//
//    query = query + "||'NO'||'|'\t\t\t\t\t\t";
//    query = query + "||libecr||'|'\tas texte\t\t\t\t";
//
//
//    query = query + "FROM cp_int ";
//    query = query + "Where idEntreprise = '" + this.dossier + "'";
//
//    String numpce = new ClsDate(this.globalUpdate.getPeriode(), "yyyyMM").getDateS("MMyyyy") + this.globalUpdate.getNumerobulletin();
//    query = query + " and numpce = '" + numpce + "'";
//
//    Session _o_Session = this.service.getSession();
//    Transaction tx = null;
//    Connection oConnexion = null;
//    Statement oStatement = null;
//    ResultSet oResultSet = null;
//    try
//    {
//      tx = _o_Session.beginTransaction();
//      oConnexion = _o_Session.connection();
//      oStatement = oConnexion.createStatement();
//      oResultSet = oStatement.executeQuery(query);
//      Rhttext texte = null;
//
//      Integer i = Integer.valueOf(0);
//
//      Integer nblig = Integer.valueOf(0);
//      while (oResultSet.next())
//      {
//        nblig = Integer.valueOf(nblig.intValue() + 1);
//        try
//        {
//          if (i.intValue() % 20 == 0)
//          {
//            _o_Session.flush();
//            _o_Session.clear();
//          }
//          texte = new Rhttext();
//          texte.setComp_id(new RhttextPK(this.session, nblig));
//          texte.setCdos(this.dossier);
//          texte.setTexte(oResultSet.getString("texte"));
//          _o_Session.save(texte);
//          i = Integer.valueOf(i.intValue() + 1);
//        }
//        catch (Exception e)
//        {
//          e.printStackTrace();
//        }
//      }
//      tx.commit();
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//      if (tx != null) {
//        tx.rollback();
//      }
//      this.errmess1 = ClsTreater._getStackTrace(e);
//      writeLog(this.errmess1);
//      this.pb = true;
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
//      return;
//    }
//    finally
//    {
//      try
//      {
//        oResultSet.close();
//        oResultSet = null;
//        oStatement.close();
//        oStatement = null;
//        oConnexion.close();
//        oConnexion = null;
//        this.service.closeConnexion(_o_Session);
//      }
//      catch (Exception e)
//      {
//        e.printStackTrace();
//      }
//    }
//  }
//
//  public void generateOGLSDVNIGER(HttpServletRequest request)
//  {
//    Rhfnom nomenc = null;
//    String separateur = "";
//    Object o = this.service.get(Rhfnom.class, new RhfnomPK(this.dossier, Integer.valueOf(99), "PAIE-INTER", Integer.valueOf(5)));
//    if (o != null)
//    {
//      nomenc = (Rhfnom)o;
//      if (!ClsObjectUtil.isNull(nomenc.getVall())) {
//        separateur = nomenc.getVall().trim();
//      }
//    }
//    else
//    {
//      this.errmess1 = "Erreur : Creez PAIE-INTER, Lib5 en Tb99.";
//      writeLog(this.errmess1);
//      this.pb = true;
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
//      return;
//    }
//    if (ClsObjectUtil.isNull(separateur))
//    {
//      this.errmess1 = "Renseignez Lib5 de PAIE-INTER en Tb99.";
//      writeLog(this.errmess1);
//      this.pb = true;
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
//      return;
//    }
//    int numero = _getCurrentDBMaxRhttext(this.service, this.session).intValue();
//
//    String numpiece = new ClsDate(this.globalUpdate.getPeriode(), "yyyyMM").getDateS("MMyyyy") + this.globalUpdate.getNumerobulletin();
//
//
//
//
//
//
//    String query = " select codjou,codets,sens,numcpt,numtie,datpce,libecr, SUM(pce_mt) as pce_mt from cp_int where cdos='" + this.dossier + "' and numpce='" +
//      numpiece + "'";
//    query = query + " and datalength(ltrim(rtrim(numcpt)))=4 ";
//    query = query + " GROUP BY codjou,codets,sens,numcpt,numtie,datpce,libecr ";
//    query = query + " union all ";
//    query = query + " SELECT codjou,codets,sens,numcpt,numtie,datpce,case substring(numcpt,1,1) when 6 then coddes1 else '' end as libecr, SUM(pce_mt) as pce_mt FROM cp_int WHERE  cdos='" +
//      this.dossier + "' and numpce='" + numpiece + "'";
//    query = query + " and datalength(ltrim(rtrim(numcpt)))>4 ";
//    query = query + " GROUP BY codjou,codets,sens,numcpt,numtie,datpce,case substring(numcpt,1,1) when 6 then coddes1 else '' end ";
//    query = query + " ORDER BY NUMCPT ";
//
//    String requete = "DELETE FROM Rhttext WHERE idEntreprise = '" + this.dossier + "' and sess='" + this.session + "'";
//    Session oSession = null;
//    Transaction tx = null;
//    try
//    {
//      oSession = this.service.getSession();
//      tx = oSession.beginTransaction();
//      oSession.createQuery(requete).executeUpdate();
//
//      List<Object[]> lint = oSession.createSQLQuery(query).list();
//
//      String message = "";
//      Rhttext text = null;
//      int j = 0;
//
//      String numjou = new ClsDate(this.globalUpdate.getPeriode(), "yyyyMM").getDateS("MM/yyyy");
//      for (int i = 0; i < lint.size(); i++)
//      {
//        j = i + 1;
//        String str90 = StringUtils.valueOf(((Object[])lint.get(i))[0]);
//        String str830 = StringUtils.valueOf(((Object[])lint.get(i))[1]);
//        String sens = StringUtils.valueOf(((Object[])lint.get(i))[2]);
//        String numcpt = StringUtils.valueOf(((Object[])lint.get(i))[3]);
//        String numtie = StringUtils.valueOf(((Object[])lint.get(i))[4]);
//        Date datpce = (Date)((Object[])lint.get(i))[5];
//        String libecr = StringUtils.valueOf(((Object[])lint.get(i))[6]);
//        BigDecimal pce_mt = new BigDecimal(StringUtils.valueOf(((Object[])lint.get(i))[7]));
//
//        numpiece = str830 + new ClsDate(this.globalUpdate.getPeriode(), "yyyyMM").getDateS("MMyy");
//
//        message = str90 + separateur;
//        message = message + str830 + separateur;
//        message = message + numpiece + separateur;
//        message = message + numjou + separateur;
//        message = message + (numcpt.length() == 4 ? "KSDV000" + numcpt : numcpt) + separateur;
//        message = message + new ClsDate(datpce).getDateS() + separateur;
//        message = message + sens + separateur;
//        message = message + libecr + separateur;
//        message = message + pce_mt.setScale(0) + separateur;
//
//        numero++;
//        text = new Rhttext();
//        text.setComp_id(new RhttextPK(this.session, Integer.valueOf(numero)));
//        text.setCdos(this.dossier);
//        text.setTexte(message);
//
//        oSession.save(text);
//        if (i % 20 == 0)
//        {
//          oSession.flush();
//          oSession.clear();
//        }
//      }
//      tx.commit();
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//      if (tx != null) {
//        tx.rollback();
//      }
//      this.errmess1 = ClsTreater._getStackTrace(e);
//      writeLog(this.errmess1);
//      this.pb = true;
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
//      return;
//    }
//    finally
//    {
//      this.service.closeConnexion(oSession);
//    }
//  }
//
//  public void generateOGLTASIAST(HttpServletRequest request)
//  {
//    Rhfnom nomenc = null;
//    String separateur = "";
//
//    Rhfnom fnom = (Rhfnom)this.service.get(Rhfnom.class, new RhfnomPK(this.dossier, Integer.valueOf(99), "PAR-CENTRA", Integer.valueOf(1)));
//    if ((fnom != null) && (StringUtils.equalsIgnoreCase("JDE", fnom.getVall())))
//    {
//      generateOGLJDETASIAST(request);
//      return;
//    }
//    Object o = this.service.get(Rhfnom.class, new RhfnomPK(this.dossier, Integer.valueOf(99), "PAIE-INTER", Integer.valueOf(5)));
//    if (o != null)
//    {
//      nomenc = (Rhfnom)o;
//      if (!ClsObjectUtil.isNull(nomenc.getVall())) {
//        separateur = nomenc.getVall().trim();
//      }
//    }
//    else
//    {
//      this.errmess1 = "Erreur : Creez PAIE-INTER, Lib5 en Tb99.";
//      writeLog(this.errmess1);
//      this.pb = true;
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
//      return;
//    }
//    if (ClsObjectUtil.isNull(separateur))
//    {
//      this.errmess1 = "Renseignez Lib5 de PAIE-INTER en Tb99.";
//      writeLog(this.errmess1);
//      this.pb = true;
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
//      return;
//    }
//    int numero = _getCurrentDBMaxRhttext(this.service, this.session).intValue();
//
//    String numpiece = new ClsDate(this.globalUpdate.getPeriode(), "yyyyMM").getDateS("MMyyyy") + this.globalUpdate.getNumerobulletin();
//    ClsDate date = new ClsDate(this.globalUpdate.getPeriode(), "yyyyMM");
//    String libelleMois = date.getDateS("MMMM").toUpperCase();
//    String reflet = "SAL " + libelleMois;
//    String description = "";
//
//    o = this.service.get(Rhfnom.class, new RhfnomPK(this.dossier, Integer.valueOf(99), "PAIE-INTER", Integer.valueOf(8)));
//    boolean multiplyNeg = (o != null) && (((Rhfnom)o).getValm().longValue() == 1L);
//    int mult = multiplyNeg ? -1 : 1;
//
//    String query = "(select numcpt,null as numtie,coddes3,reflet,devpce, " + mult + "*sum(pce_mt) as mont from cp_int ";
//    query = query + " where cdos='" + this.dossier + "'  and NUMPCE='" + numpiece + "' and numcpt like '4%'  group by numcpt,coddes3,null,reflet,devpce) ";
//    query = query + " union all ";
//    query = query + " ( select numcpt,numtie,coddes3,reflet,devpce, sum(pce_mt) as mont from cp_int ";
//    query = query + " where cdos='" + this.dossier + "' and NUMPCE='" + numpiece + "' and numcpt like '6%' group by numcpt,numtie,coddes3,reflet,devpce) ";
//    query = query + " order by numcpt";
//
//    String requete = "DELETE FROM Rhttext WHERE idEntreprise = '" + this.dossier + "' and sess='" + this.session + "'";
//    Session oSession = null;
//    Transaction tx = null;
//    try
//    {
//      oSession = this.service.getSession();
//      tx = oSession.beginTransaction();
//      oSession.createQuery(requete).executeUpdate();
//
//      List<Object[]> lint = oSession.createSQLQuery(query).list();
//
//      String message = "";
//      Rhttext text = null;
//
//
//
//
//
//      ClsNomenclature nome = null;
//      for (int i = 0; i < lint.size(); i++)
//      {
//        String numcpt = StringUtils.valueOf(((Object[])lint.get(i))[0]);
//        String numtie = StringUtils.valueOf(((Object[])lint.get(i))[1]);
//        String typr = StringUtils.valueOf(((Object[])lint.get(i))[2]);
//
//        nome = this.service.findFromNomenclature(this.dossier, "0001", "59", numcpt);
//        if (StringUtils.isBlank(nome.getLibelle())) {
//          nome = this.service.findFromNomenclature(this.dossier, "0001", "59", numcpt + typr);
//        }
//        if (StringUtils.isBlank(nome.getLibelle()))
//        {
//          this.errmess1 = ("Renseignez le libell� du compte " + numcpt + " en Table 59.");
//          writeLog(this.errmess1);
//          this.pb = true;
//          this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true); return;
//        }
//        description = StringUtils.trim(nome.getLibelle()) + " " + libelleMois;
//        String devise = StringUtils.valueOf(((Object[])lint.get(i))[4]);
//        BigDecimal pce_mt = new BigDecimal(StringUtils.valueOf(((Object[])lint.get(i))[5]));
//
//        message = numcpt + separateur;
//        message = message + StringUtils.nvl(numtie, "") + separateur;
//        message = message + reflet + separateur;
//        message = message + description + separateur;
//        message = message + pce_mt + separateur;
//        message = message + devise + separateur;
//
//        numero++;
//        text = new Rhttext();
//        text.setComp_id(new RhttextPK(this.session, Integer.valueOf(numero)));
//        text.setCdos(this.dossier);
//        text.setTexte(message);
//
//        oSession.save(text);
//        if (i % 20 == 0)
//        {
//          oSession.flush();
//          oSession.clear();
//        }
//      }
//      tx.commit();
//    }
//    catch (Exception e)
//    {
//      for (;;)
//      {
//        e.printStackTrace();
//        if (tx != null) {
//          tx.rollback();
//        }
//        this.errmess1 = ClsTreater._getStackTrace(e);
//        writeLog(this.errmess1);
//        this.pb = true;
//        this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
//      }
//    }
//    finally
//    {
//      this.service.closeConnexion(oSession);
//    }
//    this.service.closeConnexion(oSession);
//  }
//
//  public void generateOGLJDETASIAST(HttpServletRequest request)
//  {
//    String symboleMatricule = "E";
//    String symboleBU = "BU";
//    Rhfnom nomenc = null;
//    String separateur = "";
//    Object o = this.service.get(Rhfnom.class, new RhfnomPK(this.dossier, Integer.valueOf(99), "PAIE-INTER", Integer.valueOf(5)));
//    if (o != null)
//    {
//      nomenc = (Rhfnom)o;
//      if (!ClsObjectUtil.isNull(nomenc.getVall())) {
//        separateur = nomenc.getVall().trim();
//      }
//    }
//    else
//    {
//      this.errmess1 = "Erreur : Creez PAIE-INTER, Lib5 en Tb99.";
//      writeLog(this.errmess1);
//      this.pb = true;
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
//      return;
//    }
//    if (ClsObjectUtil.isNull(separateur))
//    {
//      this.errmess1 = "Renseignez Lib5 de PAIE-INTER en Tb99.";
//      writeLog(this.errmess1);
//      this.pb = true;
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
//      return;
//    }
//    int numero = _getCurrentDBMaxRhttext(this.service, this.session).intValue();
//
//    String numpiece = new ClsDate(this.globalUpdate.getPeriode(), "yyyyMM").getDateS("MMyyyy") + this.globalUpdate.getNumerobulletin();
//
//    String query = "Select numcpt,coddes1, coddes4, sens, sum(pce_mt) from cp_int ";
//    query = query + " where cdos='" + this.dossier + "'  and NUMPCE='" + numpiece + "' ";
//    query = query + " and upper(numcpt) like '%;" + symboleMatricule + ";%' and upper(numcpt) like '%" + symboleBU + ";%'";
//    query = query + " group by numcpt,coddes1, coddes4, sens";
//
//    query = query + " union all ";
//
//    query = query + "Select numcpt,coddes1, '1' as coddes4, sens, sum(pce_mt) from cp_int ";
//    query = query + " where cdos='" + this.dossier + "'  and NUMPCE='" + numpiece + "' ";
//    query = query + " and upper(numcpt) not like '%;" + symboleMatricule + ";%' and upper(numcpt) like '%" + symboleBU + ";%'";
//    query = query + " group by numcpt,coddes1,sens";
//
//    query = query + " union all ";
//
//    query = query + "Select numcpt,'1'  as coddes1,  coddes4, sens, sum(pce_mt) from cp_int ";
//    query = query + " where cdos='" + this.dossier + "'  and NUMPCE='" + numpiece + "' ";
//    query = query + " and upper(numcpt) like '%;" + symboleMatricule + ";%' and upper(numcpt) not like '%" + symboleBU + ";%'";
//    query = query + " group by numcpt,coddes4,sens";
//
//
//    query = query + " union all ";
//
//    query = query + "Select numcpt,'1' as coddes1, '1' as coddes4, sens, sum(pce_mt) from cp_int ";
//    query = query + " where cdos='" + this.dossier + "'  and NUMPCE='" + numpiece + "' ";
//    query = query + " and upper(numcpt) not like '%;" + symboleMatricule + ";%' and upper(numcpt) not like '%" + symboleBU + ";%'";
//    query = query + " group by numcpt,sens";
//
//
//
//
//    query = query + " order by numcpt";
//
//
//
//    String requete = "DELETE FROM Rhttext WHERE idEntreprise = '" + this.dossier + "' and sess='" + this.session + "'";
//    Session oSession = null;
//    Transaction tx = null;
//    try
//    {
//      oSession = this.service.getSession();
//      tx = oSession.beginTransaction();
//      oSession.createQuery(requete).executeUpdate();
//
//      List<Object[]> lint = oSession.createSQLQuery(query).list();
//
//      String message = "";
//      Rhttext text = null;
//
//
//
//
//
//
//      int id = 0;
//
//
//
//
//
//
//
//
//
//      boolean positif = true;
//
//      int k = 0;
//      for (int i = 0; i < lint.size(); i++)
//      {
//        k = 0;
//        String numcpt = StringUtils.valueOf(((Object[])lint.get(i))[(k++)]);
//        String coddes1 = StringUtils.valueOf(((Object[])lint.get(i))[(k++)]);
//        String coddes4 = StringUtils.valueOf(((Object[])lint.get(i))[(k++)]);
//        String sens = StringUtils.valueOf(((Object[])lint.get(i))[(k++)]);
//        BigDecimal pce_mt = new BigDecimal(StringUtils.valueOf(((Object[])lint.get(i))[(k++)]));
//        positif = true;
//        if (pce_mt.signum() < 0) {
//          positif = false;
//        }
//        String pce_mt_str = pce_mt.abs().toString();
//        String[] split = StringUtils.splitPreserveAllTokens(numcpt, ";");
//        id = 0;
//        String bu = "";
//        String signe = "";
//        String account = "";
//        String subsidiary = "";
//        String subledger = "";
//        String type = "";
//        if (split.length > id) {
//          bu = split[(id++)];
//        }
//        if (split.length > id) {
//          account = split[(id++)];
//        }
//        if (split.length > id) {
//          subsidiary = split[(id++)];
//        }
//        if (split.length > id) {
//          subledger = split[(id++)];
//        }
//        if (split.length > id) {
//          type = split[(id++)];
//        }
//        if (StringUtils.equalsIgnoreCase(symboleMatricule, subledger)) {
//          subledger = coddes4;
//        }
//        if (StringUtils.equalsIgnoreCase(symboleBU, bu)) {
//          bu = coddes1;
//        }
//        if (!positif) {
//          sens = StringUtils.equalsIgnoreCase("C", sens) ? "D" : "C";
//        }
//        signe = StringUtils.equalsIgnoreCase("C", sens) ? "-" : "+";
//
//
//
//
//
//
//
//
//
//
//
//
//
//        message = StringUtil.oraSubstring(oraLPad(StringUtils.nvl(bu, " "), 12, " "), 1, 12) + separateur;
//        message = message + account + separateur;
//        message = message + StringUtil.oraSubstring(oraLPad(subsidiary, 8, " "), 1, 8) + separateur;
//        message = message + StringUtil.oraSubstring(oraLPad(subledger, 8, " "), 1, 8) + separateur;
//        message = message + StringUtil.oraSubstring(oraLPad(type, 1, " "), 1, 1) + separateur;
//        message = message + StringUtil.oraSubstring(oraLPad(pce_mt_str, 15, "0"), 1, 15) + separateur;
//        message = message + signe;
//
//        numero++;
//        text = new Rhttext();
//        text.setComp_id(new RhttextPK(this.session, Integer.valueOf(numero)));
//        text.setCdos(this.dossier);
//        text.setTexte(message);
//
//        oSession.save(text);
//        if (i % 20 == 0)
//        {
//          oSession.flush();
//          oSession.clear();
//        }
//      }
//      tx.commit();
//    }
//    catch (HibernateException e)
//    {
//      e.printStackTrace();
//      if (tx != null) {
//        tx.rollback();
//      }
//      this.errmess1 = ClsTreater._getStackTrace(e);
//      writeLog(this.errmess1);
//      this.pb = true;
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
//      return;
//    }
//    finally
//    {
//      this.service.closeConnexion(oSession);
//    }
//  }
//
//  public void generateOGLCastel(HttpServletRequest request) {}
//
//  public void generateOGLSBOA(HttpServletRequest request)
//  {
//    String numpce = new ClsDate(this.globalUpdate.getPeriode(), "yyyyMM").getDateS("MMyyyy") + this.globalUpdate.getNumerobulletin();
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//    String query = "SELECT ";
//    query = query + " CODDES1,CODJOU,datcpt,NUMCPT,REFLET,CODDES2,NUMTIE,NUMPCE,datpce,codabr,libecr,sens,SUM(PCE_MT),devpce,coduti";
//    query = query + " FROM cp_int ";
//    query = query + " Where idEntreprise = '" + this.dossier + "'";
//    query = query + " and numpce = '" + numpce + "'";
//    query = query + " group by ";
//    query = query + " CODDES1,codjou,datcpt,numcpt,reflet,coddes2,NUMTIE,numpce,datpce,codabr,libecr,sens, ";
//    query = query + " devpce,coduti ";
//    try
//    {
//      String str = "P+A+E";
//      String inStr = "";
//      Rhfnom fnom = (Rhfnom)this.service.get(Rhfnom.class, new RhfnomPK(this.dossier, Integer.valueOf(99), "INT-SBOA", Integer.valueOf(1)));
//      if ((fnom != null) && (StringUtils.isNotBlank(fnom.getVall()))) {
//        str = fnom.getVall();
//      }
//      String[] split = StringUtils.split(str, "+");
//      for (String str2 : split) {
//        inStr = inStr + ",'" + str2 + "'";
//      }
//      String tmpQuery = "UPDATE cp_int SET numcpt = numcpt||numtie WHERE numcpt in (" + inStr + ")";
//      this.service.updateFromTable(tmpQuery);
//
//      tmpQuery = "UPDATE cp_int SET numtie = numcpt ";
//      this.service.updateFromTable(tmpQuery);
//    }
//    catch (Exception e1)
//    {
//      e1.printStackTrace();
//      this.errmess1 = ClsTreater._getStackTrace(e1);
//      writeLog(this.errmess1);
//      this.pb = true;
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
//      return;
//    }
//    String requete = "DELETE FROM Rhttext WHERE idEntreprise = '" + this.dossier + "' and sess='" + this.session + "'";
//    Session oSession = null;
//    Transaction tx = null;
//    try
//    {
//      oSession = this.service.getSession();
//      tx = oSession.beginTransaction();
//      oSession.createQuery(requete).executeUpdate();
//
//      List<Object[]> lint = oSession.createSQLQuery(query).list();
//
//      String message = "";
//      Rhttext text = null;
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//      int index = 0;
//      int numero = 0;
//      for (int i = 0; i < lint.size(); i++)
//      {
//        index = 0;
//        String coddes1 = StringUtils.valueOf(((Object[])lint.get(i))[(index++)]);
//        String codjou = StringUtils.valueOf(((Object[])lint.get(i))[(index++)]);
//        Date datcpt = (Date)((Object[])lint.get(i))[(index++)];
//        String numcpt = StringUtils.valueOf(((Object[])lint.get(i))[(index++)]);
//        String reflet = StringUtils.valueOf(((Object[])lint.get(i))[(index++)]);
//        String coddes2 = StringUtils.valueOf(((Object[])lint.get(i))[(index++)]);
//        String numtie = StringUtils.valueOf(((Object[])lint.get(i))[(index++)]);
//        String numpcee = StringUtils.valueOf(((Object[])lint.get(i))[(index++)]);
//        Date datpce = (Date)((Object[])lint.get(i))[(index++)];
//        String codabr = StringUtils.valueOf(((Object[])lint.get(i))[(index++)]);
//        String libecr = StringUtils.valueOf(((Object[])lint.get(i))[(index++)]);
//        String sens = StringUtils.valueOf(((Object[])lint.get(i))[(index++)]);
//        BigDecimal pce_mt = new BigDecimal(StringUtils.valueOf(((Object[])lint.get(i))[(index++)]));
//        pce_mt = pce_mt.setScale(3, RoundingMode.CEILING);
//        String devpce = StringUtils.valueOf(((Object[])lint.get(i))[(index++)]);
//        String coduti = StringUtils.valueOf(((Object[])lint.get(i))[(index++)]);
//
//
//        message = StringUtils.oraRPad(StringUtils.nvl(coddes1, " "), 3);
//        message = message + StringUtils.oraRPad(StringUtils.nvl(codjou, " "), 3);
//        if (datcpt != null) {
//          message = message + ClsStringUtil.oraLPad(new ClsDate(datcpt).getDateS("yyyyMMdd"), 10);
//        } else {
//          message = message + ClsStringUtil.oraLPad(" ", 10);
//        }
//        message = message + StringUtils.oraRPad(StringUtils.nvl(numcpt, " "), 8);
//        message = message + StringUtils.oraRPad(StringUtils.nvl(reflet, " "), 8);
//        message = message + StringUtils.oraRPad(StringUtils.nvl(coddes2, " "), 8);
//        message = message + StringUtils.oraRPad(StringUtils.nvl(numtie, " "), 8);
//        message = message + ClsStringUtil.oraLPad(StringUtils.nvl(numpcee, " "), 10);
//        if (datpce != null) {
//          message = message + ClsStringUtil.oraLPad(new ClsDate(datpce).getDateS("yyyyMMdd"), 10);
//        } else {
//          message = message + ClsStringUtil.oraLPad(" ", 10);
//        }
//        message = message + StringUtils.oraRPad(StringUtils.nvl(codabr, " "), 6);
//        message = message + StringUtils.oraRPad(StringUtils.nvl(libecr, " "), 30);
//        message = message + StringUtils.oraRPad(StringUtils.nvl(" ", " "), 8);
//        message = message + StringUtils.oraRPad(StringUtils.nvl(sens, " "), 1);
//        message = message + ClsStringUtil.oraLPad(StringUtils.replace(new StringBuilder().append(pce_mt).toString(), ".", ","), 21);
//        message = message + ClsStringUtil.oraLPad(StringUtils.replace(new StringBuilder().append(new BigDecimal(1).setScale(7, RoundingMode.CEILING)).toString(), ".", ","), 15);
//        message = message + ClsStringUtil.oraLPad(StringUtils.replace(new StringBuilder().append(pce_mt).toString(), ".", ","), 21);
//        message = message + StringUtils.oraRPad(StringUtils.nvl(devpce, " "), 3);
//        message = message + StringUtil.oraSubstring(StringUtils.oraRPad(StringUtils.nvl(coduti, " "), 4), 1, 4);
//        message = message + ClsStringUtil.oraLPad(StringUtils.nvl("0", " "), 10);
//        if (datcpt != null) {
//          message = message + ClsStringUtil.oraLPad(new ClsDate(datcpt).getDateS("yyyyMMdd"), 10);
//        } else {
//          message = message + ClsStringUtil.oraLPad(" ", 10);
//        }
//        message = message + StringUtils.oraRPad(StringUtils.nvl(" ", " "), 2);
//        message = message + StringUtils.oraRPad(StringUtils.nvl(" ", " "), 2);
//        message = message + StringUtils.oraRPad(StringUtils.nvl("G", " "), 1);
//        message = message + ClsStringUtil.oraLPad(StringUtils.nvl("0", " "), 10);
//
//
//
//        numero++;
//        text = new Rhttext();
//        text.setComp_id(new RhttextPK(this.session, Integer.valueOf(numero)));
//        text.setCdos(this.dossier);
//        text.setTexte(message);
//
//        oSession.save(text);
//        if (i % 20 == 0)
//        {
//          oSession.flush();
//          oSession.clear();
//        }
//      }
//      tx.commit();
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//      if (tx != null) {
//        tx.rollback();
//      }
//      this.errmess1 = ClsTreater._getStackTrace(e);
//      writeLog(this.errmess1);
//      this.pb = true;
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
//      return;
//    }
//    finally
//    {
//      this.service.closeConnexion(oSession);
//    }
//  }
//
//  public void generateOGLAGRESO_ACTIVA(HttpServletRequest request)
//  {
//    Rhfnom nomenc = null;
//    String separateur = "";
//    Object o = this.service.get(Rhfnom.class, new RhfnomPK(this.dossier, Integer.valueOf(99), "PAIE-INTER", Integer.valueOf(5)));
//    if (o != null)
//    {
//      nomenc = (Rhfnom)o;
//      if (!ClsObjectUtil.isNull(nomenc.getVall())) {
//        separateur = nomenc.getVall().trim();
//      }
//    }
//    else
//    {
//      this.errmess1 = "Erreur : Creez PAIE-INTER, Lib5 en Tb99.";
//      writeLog(this.errmess1);
//      this.pb = true;
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
//      return;
//    }
//    if (ClsObjectUtil.isNull(separateur))
//    {
//      this.errmess1 = "Renseignez Lib5 de PAIE-INTER en Tb99.";
//      writeLog(this.errmess1);
//      this.pb = true;
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
//      return;
//    }
//    int numero = _getCurrentDBMaxRhttext(this.service, this.session).intValue();
//
//    String numpiece = new ClsDate(this.globalUpdate.getPeriode(), "yyyyMM").getDateS("MMyyyy") + this.globalUpdate.getNumerobulletin();
//
//
//
//
//
//
//    String query = " select 'BI' as col1,'BI' as col2,cpint.codjou,'GL' as col3,cpint.cdos," +
//      ClsTypeBD.contatener(new String[] { "coalesce(cpint.numcpt,'')", "coalesce(cpint.numtie,'')" }) +
//      " as compte,cpint.coddes1,'XAF' as devise,cpint.sens,sum(cpint.pce_mt) pce_mt1," +
//      "\t\tsum(cpint.pce_mt) pce_mt2,cpint.libecr,cpint.datcpt, cpint.datcpt,cpint.libecr,cpint.datech" + "\t\tfrom Cp_int cpint";
//
//
//
//
//    query = query + " Where cpint.idEntreprise = '" + this.dossier + "'";
//    query = query + " and cpint.numpce = '" + numpiece + "'";
//    query = query + " and cpint.numcpt like '6%'";
//    query = query + " Group by cpint.codjou,cpint.cdos," + ClsTypeBD.contatener(new String[] { "coalesce(cpint.numcpt,'')", "coalesce(cpint.numtie,'')" }) + ",cpint.coddes1,cpint.sens, ";
//    query = query + " cpint.libecr,cpint.datcpt, cpint.datcpt,cpint.datech ";
//
//
//    query = query + " union all select 'BI' as col1,'BI' as col2,cpint.codjou,'GL' as col3,cpint.cdos," +
//      ClsTypeBD.contatener(new String[] { "coalesce(cpint.numcpt,'')", "coalesce(cpint.numtie,'')" }) +
//      " as compte,'' as coddes1,'XAF' as devise,cpint.sens,sum(cpint.pce_mt) pce_mt1," +
//      "\t\tsum(cpint.pce_mt) pce_mt2,cpint.libecr,cpint.datcpt, cpint.datcpt,cpint.libecr,cpint.datech" + "\t\tfrom Cp_int cpint";
//
//
//
//
//    query = query + " Where cpint.idEntreprise = '" + this.dossier + "'";
//    query = query + " and cpint.numpce = '" + numpiece + "'";
//    query = query + " and cpint.numcpt not like '6%'";
//    query = query + " Group by cpint.codjou,cpint.cdos," + ClsTypeBD.contatener(new String[] { "coalesce(cpint.numcpt,'')", "coalesce(cpint.numtie,'')" }) + ",cpint.sens, ";
//    query = query + " cpint.libecr,cpint.datcpt, cpint.datcpt,cpint.datech ";
//
//
//    String requete = "DELETE FROM Rhttext WHERE idEntreprise = '" + this.dossier + "' and sess='" + this.session + "'";
//    Session oSession = null;
//    Transaction tx = null;
//    try
//    {
//      oSession = this.service.getSession();
//      tx = oSession.beginTransaction();
//      oSession.createQuery(requete).executeUpdate();
//
//      List<Object[]> lint = oSession.createSQLQuery(query).list();
//
//      String message = "";
//      Rhttext text = null;
//      String axe1 = " ";
//      String axe2 = " ";
//      String axe3 = " ";
//      String axe4 = " ";
//      String axe5 = " ";
//      String axe6 = " ";
//      String axe7 = " ";
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//      NumberFormat currencyFormater = NumberFormat.getCurrencyInstance(Locale.FRANCE);
//      currencyFormater.setGroupingUsed(false);
//      for (int i = 0; i < lint.size(); i++)
//      {
//        String batch_id = StringUtils.valueOf(((Object[])lint.get(i))[0]);
//        String interfac = StringUtils.valueOf(((Object[])lint.get(i))[1]);
//        String voucher_type = StringUtils.valueOf(((Object[])lint.get(i))[2]);
//        String trans_type = StringUtils.valueOf(((Object[])lint.get(i))[3]);
//        String client = StringUtils.valueOf(((Object[])lint.get(i))[4]);
//        client = (client != null) && (client.contains("01")) ? "A1" : "A2";
//        String compte = StringUtils.valueOf(((Object[])lint.get(i))[5]);
//        axe1 = StringUtils.nvl(StringUtils.valueOf(((Object[])lint.get(i))[6]), " ");
//        axe4 = StringUtils.isBlank(axe1) ? " " : "NA";
//        axe5 = StringUtils.isBlank(axe1) ? " " : "FRGEN";
//        String currency = StringUtils.valueOf(((Object[])lint.get(i))[7]);
//        String sens = StringUtils.valueOf(((Object[])lint.get(i))[8]);
//        String dc_flag = (sens != null) && (sens.contains("C")) ? "-1" : "1";
//        BigDecimal cur_amount = new BigDecimal(StringUtils.valueOf(((Object[])lint.get(i))[9])).multiply(new BigDecimal(100)).multiply(new BigDecimal(dc_flag));
//
//        BigDecimal montant = new BigDecimal(StringUtils.valueOf(((Object[])lint.get(i))[10])).multiply(new BigDecimal(100)).multiply(new BigDecimal(dc_flag));
//        cur_amount = cur_amount.setScale(0);
//        montant = montant.setScale(0);
//        String description = StringUtils.valueOf(((Object[])lint.get(i))[11]);
//        String trans_date = ClsDate.getDateS((Date)((Object[])lint.get(i))[12], "yyyyMMdd");
//        String voucher_date = ClsDate.getDateS((Date)((Object[])lint.get(i))[13], "yyyyMMdd");
//        String ext_inv_ref = StringUtils.valueOf(((Object[])lint.get(i))[14]);
//        String due_date = ClsDate.getDateS((Date)((Object[])lint.get(i))[15], "yyyyMMdd");
//
//
//        message = formatString(batch_id, 25, " ");
//        message = message + formatString(interfac, 25, " ");
//        message = message + formatString(voucher_type, 25, " ");
//        message = message + formatString(trans_type, 2, " ");
//        message = message + formatString(client, 25, " ");
//        message = message + formatString(compte, 25, " ");
//        message = message + formatString(axe1, 25, " ");
//        message = message + formatString(axe2, 25, " ");
//        message = message + formatString(axe3, 25, " ");
//        message = message + formatString(axe4, 25, " ");
//        message = message + formatString(axe5, 25, " ");
//        message = message + formatString(axe6, 25, " ");
//        message = message + formatString(axe7, 25, " ");
//        message = message + formatString(" ", 25, " ");
//        message = message + formatString(" ", 25, " ");
//        message = message + formatString(currency, 25, " ");
//        message = message + formatString2(dc_flag, 2, " ");
//
//
//
//
//
//
//        message = message + formatString2(new StringBuilder().append(cur_amount).toString(), 20, " ");
//        message = message + formatString2(new StringBuilder().append(montant).toString(), 20, " ");
//        message = message + formatString(" ", 11, " ");
//        message = message + formatString(" ", 20, " ");
//        message = message + formatString(" ", 20, " ");
//        message = message + formatString(" ", 20, " ");
//        message = message + formatString(description, 255, " ");
//        message = message + formatString(trans_date, 8, " ");
//        message = message + formatString(voucher_date, 8, " ");
//        message = message + formatString(" ", 15, " ");
//        message = message + formatString(" ", 6, " ");
//        message = message + formatString(" ", 1, " ");
//        message = message + formatString(ext_inv_ref, 100, " ");
//        message = message + formatString(" ", 255, " ");
//        message = message + formatString(due_date, 8, " ");
//        message = message + formatString(" ", 8, " ");
//        message = message + formatString(" ", 20, " ");
//        message = message + formatString(" ", 25, " ");
//        message = message + formatString(" ", 15, " ");
//        message = message + formatString(" ", 27, " ");
//        message = message + formatString(" ", 2, " ");
//        message = message + formatString(" ", 1, " ");
//        message = message + formatString(" ", 1, " ");
//        message = message + formatString(" ", 25, " ");
//        message = message + formatString(" ", 1, " ");
//        message = message + formatString(" ", 15, " ");
//        message = message + formatString(" ", 9, " ");
//        message = message + formatString(" ", 25, " ");
//        message = message + formatString(" ", 25, " ");
//        message = message + formatString(" ", 25, " ");
//        message = message + formatString(" ", 255, " ");
//        message = message + formatString(" ", 160, " ");
//        message = message + formatString(" ", 40, " ");
//        message = message + formatString(" ", 40, " ");
//        message = message + formatString(" ", 35, " ");
//        message = message + formatString(" ", 2, " ");
//        message = message + formatString(" ", 25, " ");
//        message = message + formatString(" ", 15, " ");
//        message = message + formatString(" ", 2, " ");
//        message = message + formatString(" ", 25, " ");
//        message = message + formatString(" ", 20, " ");
//        message = message + formatString(" ", 20, " ");
//        message = message + formatString(" ", 4, " ");
//        message = message + formatString(" ", 3, " ");
//        message = message + formatString(" ", 2, " ");
//        message = message + formatString(" ", 13, " ");
//        message = message + formatString(" ", 11, " ");
//        message = message + formatString(" ", 15, " ");
//        message = message + formatString(" ", 2, " ");
//
//
//        numero++;
//        text = new Rhttext();
//        text.setComp_id(new RhttextPK(this.session, Integer.valueOf(numero)));
//        text.setCdos(this.dossier);
//        text.setTexte(message);
//
//        oSession.save(text);
//        if (i % 20 == 0)
//        {
//          oSession.flush();
//          oSession.clear();
//        }
//      }
//      tx.commit();
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//      if (tx != null) {
//        tx.rollback();
//      }
//      this.errmess1 = ClsTreater._getStackTrace(e);
//      writeLog(this.errmess1);
//      this.pb = true;
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
//      return;
//    }
//    finally
//    {
//      this.service.closeConnexion(oSession);
//    }
//  }
  
  public String formatString(String chaine, int longeurfinal, String charToAdd)
  {
    if ((chaine != null) && (chaine.length() > longeurfinal)) {
      return new String(StringUtil.oraSubstring(chaine, 1, longeurfinal));
    }
    if (chaine != null) {
      return oraRPad(chaine, longeurfinal, charToAdd);
    }
    return null;
  }
  
  public String formatString2(String chaine, int longeurfinal, String charToAdd)
  {
    if ((chaine != null) && (chaine.length() > longeurfinal)) {
      return new String(StringUtil.oraSubstring(chaine, 1, longeurfinal));
    }
    if (chaine != null) {
      return oraLPad(chaine, longeurfinal, charToAdd);
    }
    return null;
  }
  
  public String oraRPad(String chaine, int totalLength, String charToAdd)
  {
    String add = "";
    if (chaine.length() < totalLength) {
      for (int i = 0; i < totalLength - chaine.length(); i++) {
        add = add + charToAdd;
      }
    }
    return chaine + add;
  }
  
  public String oraLPad(String chaine, int totalLength, String charToAdd)
  {
    String add = "";
    if (chaine.length() < totalLength) {
      for (int i = 0; i < totalLength - chaine.length(); i++) {
        add = add + charToAdd;
      }
    }
    return add + chaine;
  }
  

	public void generateOGLOILYBIACMR(HttpServletRequest request)
	{
		int numero = ClsOGL._getCurrentDBMaxRhttext(service, session);

		String numpiece = new ClsDate(this.globalUpdate.getPeriode(), "yyyyMM").getDateS("MMyyyy") + this.globalUpdate.getNumerobulletin();

		String queryNumpce=" and numpce = '"+numpiece+"'";
		
		Session oSession = null;
		Transaction tx = null;
		try
		{
			oSession = service.getSession();
			tx = oSession.beginTransaction();
			
			ParamData nomenc = service.findAnyColumnFromNomenclature(dossier, "","99", "PAIE-INTER", "5");
			String separateur = "";
			//Object o = service.get(Rhfnom.class, new RhfnomPK(dossier, 99, "PAIE-INTER", 5));
			if (nomenc != null)
			{
				//nomenc = (Rhfnom) o;
				if (!ClsObjectUtil.isNull(nomenc.getVall())) separateur = nomenc.getVall().trim();
			}
			else
			{
				errmess1 = "Erreur : Creez PAIE-INTER, Lib5 en Tb99.";
				writeLog(errmess1);
				pb = true;
				globalUpdate._setEvolutionTraitement(request, errmess1, true);
				return;
			}
			separateur = ",";
			if (ClsObjectUtil.isNull(separateur))
			{
				errmess1 = "Renseignez Lib5 de PAIE-INTER en Tb99.";
				writeLog(errmess1);
				pb = true;
				globalUpdate._setEvolutionTraitement(request, errmess1, true);
				return;
			}

			// -- MM 03-2005 ajout logiciel compta EXXON : 1 ACCPAC (par defaut)
			// 2 JD
			String exxon_compta = "1";
           nomenc = service.findAnyColumnFromNomenclature(dossier, "","99", "FILE-GL", "1");
          //o = service.get(Rhfnom.class, new RhfnomPK(dossier, 99, "FILE-GL", 1));
			if (nomenc != null)
			{
				//nomenc = (Rhfnom) o;
				if (StringUtils.isNotBlank(nomenc.getVall())) exxon_compta = nomenc.getVall().trim();
			}
			if (StringUtils.isBlank(exxon_compta)) exxon_compta = "1";
			else if (StringUtil.notIn(StringUtil.oraSubstring(exxon_compta, 1, 1), "1,2"))
			{
				errmess1 = "Lib1 FILE-GL en Tb99.Valeurs possible 1:ACCPAC 2:JD";
				writeLog(errmess1);
				pb = true;
				globalUpdate._setEvolutionTraitement(request, errmess1, true);
				return;
			}
			else exxon_compta = StringUtil.oraSubstring(exxon_compta, 1, 1);
			if (StringUtils.equals("1", exxon_compta))// ----- Compta ACCPAC
			{
				// -- Lecture du type compta 1 - DOS ou 2 - Window
				String type_compta = StringUtils.EMPTY;
              nomenc = service.findAnyColumnFromNomenclature(dossier, "","99", "ACCPAC", "1");
              //o = service.get(Rhfnom.class, new RhfnomPK(dossier, 99, "ACCPAC", 1));
				if (nomenc != null)
				{
					//nomenc = (Rhfnom) o;
					if (StringUtils.isNotBlank(nomenc.getVall())) type_compta = nomenc.getVall();
				}
				else
				{
					errmess1 = "Erreur : Creez ACCPAC, Lib1 en Tb99.";
					writeLog(errmess1);
					pb = true;
					globalUpdate._setEvolutionTraitement(request, errmess1, true);
					return;
				}
				if (StringUtils.isBlank(type_compta))
				{
					errmess1 = "Renseignez Lib1 de ACCPAC en Tb99.";
					writeLog(errmess1);
					pb = true;
					globalUpdate._setEvolutionTraitement(request, errmess1, true);
					return;
				}
				else type_compta = StringUtil.oraSubstring(type_compta, 1, 1);

				// -- Lecture du code devise interface
				String devise_interface = StringUtils.EMPTY;
              nomenc = service.findAnyColumnFromNomenclature(dossier, "","99", "ACCPAC", "2");
              //o = service.get(Rhfnom.class, new RhfnomPK(dossier, 99, "ACCPAC", 2));
				if (nomenc != null)
				{
					//nomenc = (Rhfnom) o;
					if (StringUtils.isNotBlank(nomenc.getVall())) devise_interface = nomenc.getVall();
				}
				else
				{
					errmess1 = "Erreur : Creez ACCPAC, Lib2 en Tb99.";
					writeLog(errmess1);
					pb = true;
					globalUpdate._setEvolutionTraitement(request, errmess1, true);
					return;
				}
				if (StringUtils.isBlank(devise_interface))
				{
					errmess1 = "Renseignez Lib2 de ACCPAC en Tb99.";
					writeLog(errmess1);
					pb = true;
					globalUpdate._setEvolutionTraitement(request, errmess1, true);
					return;
				}
				else devise_interface = StringUtil.oraSubstring(devise_interface, 1, 3);

				// -- Lecture du code determination cpte charge ou produit
				String code_compte = StringUtils.EMPTY;
              nomenc = service.findAnyColumnFromNomenclature(dossier, "","99", "ACCPAC", "3");
              //o = service.get(Rhfnom.class, new RhfnomPK(dossier, 99, "ACCPAC", 3));
				if (nomenc != null)
				{
					//nomenc = (Rhfnom) o;
					if (StringUtils.isNotBlank(nomenc.getVall())) code_compte = nomenc.getVall();
				}
				else
				{
					errmess1 = "Erreur : Creez ACCPAC, Lib3 en Tb99.";
					writeLog(errmess1);
					pb = true;
					globalUpdate._setEvolutionTraitement(request, errmess1, true);
					return;
				}
				if (StringUtils.isBlank(code_compte))
				{
					errmess1 = "Renseignez Lib3 de ACCPAC en Tb99.";
					writeLog(errmess1);
					pb = true;
					globalUpdate._setEvolutionTraitement(request, errmess1, true);
					return;
				}

				// -- Lecture du nombre de zero compte bilan pour le cas SYSCOA
				// + compte bilan '000000' + CODE ICA
				Integer nombrezero = 6;
              nomenc = service.findAnyColumnFromNomenclature(dossier, "","99", "ACCPAC", "1");
              //o = service.get(Rhfnom.class, new RhfnomPK(dossier, 99, "ACCPAC", 1));
				if (nomenc != null)
				{
					//nomenc = (Rhfnom) o;
					if (nomenc.getValm() != null) nombrezero = nomenc.getValm().intValue();
				}
				else
				{
					errmess1 = "Erreur : Creez ACCPAC, Mnt1 en Tb99.";
					writeLog(errmess1);
					pb = true;
					globalUpdate._setEvolutionTraitement(request, errmess1, true);
					return;
				}
				if (nombrezero == null)
				{
					errmess1 = "Renseignez Mnt1 de ACCPAC en Tb99.";
					writeLog(errmess1);
					pb = true;
					globalUpdate._setEvolutionTraitement(request, errmess1, true);
					return;
				}

				// -- Lecture si utilisation interface AR
				boolean interface_ar = false;
				String w_vall = StringUtils.EMPTY;
              nomenc = service.findAnyColumnFromNomenclature(dossier, "","99", "ACCPAC", "4");
              //o = service.get(Rhfnom.class, new RhfnomPK(dossier, 99, "ACCPAC", 4));
				if (nomenc != null)
				{
					//nomenc = (Rhfnom) o;
					if (StringUtils.isNotBlank(nomenc.getVall())) w_vall = nomenc.getVall();
				}
				else
				{
					errmess1 = "Erreur : Creez ACCPAC, Lib4 en Tb99.";
					writeLog(errmess1);
					pb = true;
					globalUpdate._setEvolutionTraitement(request, errmess1, true);
					return;
				}
//				if (StringUtils.isBlank(w_vall))
//				{
//					errmess1 = "Renseignez Lib4 de ACCPAC en Tb99.";
//					writeLog(errmess1);
//					pb = true;
//					globalUpdate._setEvolutionTraitement(request, errmess1, true);
//					return;
//				}
				if (StringUtils.equals("1", w_vall)) interface_ar = true;
				else interface_ar = false;
				

				String requete = "DELETE FROM TraitementTexte WHERE idEntreprise = '" + dossier + "' and sess='" + session + "'";
				oSession.createSQLQuery(requete).executeUpdate();
				
				
				if (StringUtils.equals("1", type_compta))
				{
					// -- Suppression du Cost Center pour les comptes de bilan
					String query = "update InterfComptable set coddes1 = '000000' where NUMCPT < '" + code_compte + "%'"+queryNumpce;
					oSession.createSQLQuery(query).executeUpdate();
					// -- Formatage du Cost Center pour les comptes de charge
					query = "update InterfComptable set coddes1 = CODDES1+replicate('0',6-datalength(coddes1)) where NUMCPT >= '" + code_compte + "%'"+queryNumpce;
					oSession.createSQLQuery(query).executeUpdate();

					numero = 1;
					// -- MM ajout entete
					// ------------ Generation des lignes dans patext
					String w_datcpt = new ClsDate(this.globalUpdate.getDateComptable()).getDateS("yyMMdd");

					String ligne = "'H'" + separateur + " '" + w_datcpt + "'" + separateur + "      0";

                  TraitementTexte text = new TraitementTexte();
					text.setSess(session);
                    text.setNlig(numero);
					text.setIdEntreprise(new Integer(dossier));
					text.setTexte(ligne);
					oSession.save(text);

					// -------------- Fin ajout entete

					String w_libelle = "PAIE " + ClsStringUtil.oraRPad(new ClsDate(w_datcpt, "yyMMdd").getDateS("MMMM"), 10, " ") + " "
							+ StringUtil.oraSubstring(w_datcpt, 1, 4);

					
					oSession.createQuery(requete).executeUpdate();
					// ------------ Generation des lignes dans patext
					String cp_int_v_nar  = " SELECT NUMCPT, CASE NUMTIE WHEN NULL THEN CODDES1 ELSE NUMTIE END CODDES1, DATCPT, SENS, COALESCE(SUM(PCE_MT), 0) PCE_MT ";
					cp_int_v_nar+= "FROM InterfComptable Where idEntreprise = '"+dossier+"' "+queryNumpce+" GROUP BY NUMCPT, CASE NUMTIE WHEN NULL THEN CODDES1 ELSE NUMTIE END CODDES1, DATCPT, SENS ";
					
					String cp_int_v_ar  = " SELECT NUMCPT, CASE NUMTIE WHEN NULL THEN CODDES1 ELSE '000000' END CODDES1, DATCPT, SENS, COALESCE(SUM(PCE_MT), 0) PCE_MT ";
					cp_int_v_ar+= "FROM InterfComptable Where idEntreprise = '"+dossier+"' "+queryNumpce+" GROUP BY NUMCPT, CASE NUMTIE WHEN NULL THEN CODDES1 ELSE '000000' END CODDES1, DATCPT, SENS ";
					
					query = "Select numcpt,numtie,coddes1,coddes2,coddes3,sens,pce_mt From InterfComptable WHERE idEntreprise='" + dossier + "' and numpce='" + numpiece + "'";
					
					if (interface_ar) query = cp_int_v_ar;
					else query = cp_int_v_nar;
					
					
					
					SQLQuery q = oSession.createSQLQuery(query);
					List<Object[]> lint = q.list();
					String message = "";
					text = null;
					String sens;
					String numcpt;
					String coddes1;
					Date datcpt;
					BigDecimal pce_mt;
					String strPcemt;

					for (int i = 0; i < lint.size(); i++)
					{
						numcpt = StringUtils.valueOf((char[]) lint.get(i)[0]);
						coddes1 = StringUtil.nvl(StringUtils.valueOf((char[]) lint.get(i)[1]), StringUtils.EMPTY);
						datcpt = (Date)lint.get(i)[2];
						sens = StringUtils.valueOf((char[]) lint.get(i)[3]);
						pce_mt = new BigDecimal(StringUtils.valueOf((char[]) lint.get(i)[4]));
						
						if (StringUtils.equals("C", sens)) pce_mt = pce_mt.multiply(new BigDecimal(-1));
						strPcemt = ClsStringUtil.oraLPad(pce_mt.setScale(0) + "", 15, " ");

						message = "\"D\"" + separateur + " \"" //
								+ StringUtil.oraSubstring(numcpt, 1, 6) + "\"" + separateur // No compte
								+ " \"" // Filler
								+ StringUtil.oraSubstring(coddes1, 1, 6) + "\"" + separateur // section
								+ " \"GLJV\"" + separateur // Filler
								+ " \" PAIE " + new ClsDate(datcpt, "yyMMdd").getDateS("MM/yy") + "  \"" + separateur // Ref
								+ " \"" // Filler
								+ new ClsDate(datcpt, "yyMMdd").getDateS("yyMMdd") + "\"" + separateur + " \"" // ACCOUNTING_DATE
								+ ClsStringUtil.oraRPad(w_libelle, 30, " ") // libelle
								// ecriture
								+ "\"" + separateur // Filler
								+ strPcemt + separateur // ACCOUNTED_DR
								+ " \"" + devise_interface + "\""; // Filler

						numero += 1;
						text = new TraitementTexte();
                      text.setSess(session);
                      text.setNlig(numero);
                      text.setIdEntreprise(new Integer(dossier));
						text.setTexte(message);
						//
						oSession.save(text);
						if (i % 20 == 0)
						{
							oSession.flush();
							oSession.clear();
						}

					}
				}
				else
				{
					// ELSE
					//
					numero = 1;
					// -- MM ajout entete
					// ------------ Generation des lignes dans patext
					String w_datcpt2 = new ClsDate(this.globalUpdate.getDateComptable()).getDateS("yyyyMMdd");

					// -- JLM Ajout Nom des colonnes import�exs
					// -- 1 pour ent�te
					// -- 1 pour d�tail

					String w_libelle = "PAIE " + ClsStringUtil.oraRPad(new ClsDate(w_datcpt2, "yyyyMMdd").getDateS("MMMM").toUpperCase(), 10, " ") + " "
							+ StringUtil.oraSubstring(w_datcpt2, 1, 4);
					//
					//
					// -- Champs dans enregistrement ent�te
					String ligne = "\"RECTYPE\"" + separateur + "\"BATCHID\"" + separateur + "\"BTCHENTRY\"" + separateur + "\"SRCELEDGER\"" + separateur
							+ "\"SRCETYPE\"" + separateur + "\"FSCSYR\"" + separateur + "\"FSCSPERD\"" + separateur + "\"JRNLDESC\"" + separateur + "\"DATEENTRY\"";

					TraitementTexte text = new TraitementTexte();
                    text.setSess(session);
                    text.setNlig(numero);
                    text.setIdEntreprise(new Integer(dossier));
					text.setTexte(ligne);
					oSession.save(text);

					numero = 2;
					// -- Champs dans enregistrement lignes
					ligne = "\"RECTYPE\"" + separateur + "\"BATCHNBR\"" + separateur + "\"JOURNALID\"" + separateur + "\"TRANSNBR\"" + separateur + "\"ACCTID\""
							+ separateur + "\"TRANSAMT\"" + separateur + "\"SCURNAMT\"" + separateur + "\"TRANSDESC\"" + separateur + "\"TRANSREF\"" + separateur
							+ "\"TRANSDATE\"" + separateur + "\"SRCELDGR\"" + separateur + "\"SRCETYPE\"";

					text = new TraitementTexte();
                    text.setSess(session);
                    text.setNlig(numero);
                    text.setIdEntreprise(new Integer(dossier));
					text.setTexte(ligne);
					oSession.save(text);

					numero = 3;
					ligne = "1" + separateur + ClsStringUtil.oraRPad(" ", 6, " ") + separateur + "00001" + separateur + "GL" + separateur + "JE" + separateur
							+ StringUtil.oraSubstring(w_datcpt2, 1, 4) + separateur + StringUtil.oraSubstring(w_datcpt2, 5, 2) + separateur
							+ ClsStringUtil.oraRPad(w_libelle, 30, " ") + separateur + StringUtil.oraSubstring(w_datcpt2, 1, 4)
							+ StringUtil.oraSubstring(w_datcpt2, 5, 2) + StringUtil.oraSubstring(w_datcpt2, 7, 2);

					text = new TraitementTexte();
                    text.setSess(session);
                    text.setNlig(numero);
                    text.setIdEntreprise(new Integer(dossier));
					text.setTexte(ligne);
					oSession.save(text);

					// -------------- Fin ajout entete
					//
					// ------------ Generation des lignes dans patext
					// --- type compte = SYSCOA + COST CENTER + CODE ICA
					
					String cp_int_v1=" SELECT NUMCPT, CODDES1,CODDES3, DATCPT, SENS, COALESCE(SUM(PCE_MT), 0) PCE_MT ";
					cp_int_v1+=" FROM InterfComptable where idEntreprise= '"+dossier+"' "+queryNumpce+"  and COALESCE(SUBSTRING(NUMCPT,7,2),' ') = 'CC' ";
					cp_int_v1+=" GROUP BY NUMCPT, CODDES1, CODDES3, DATCPT, SENS ";
					
					String query = cp_int_v1;
					SQLQuery q = oSession.createSQLQuery(query);
					List<Object[]> lint = q.list();
					String message = "";
					text = null;
					String sens;
					String numcpt;
					String coddes1;
					String coddes3;
					BigDecimal pce_mt;
					String strPcemt;

					for (int i = 0; i < lint.size(); i++)
					{
						pce_mt = new BigDecimal(StringUtils.valueOf((char[]) lint.get(i)[5]));
						sens = StringUtils.valueOf((char[]) lint.get(i)[4]);
						numcpt = StringUtils.valueOf((char[]) lint.get(i)[0]);
						coddes1 = StringUtil.nvl(StringUtils.valueOf((char[]) lint.get(i)[1]),StringUtils.EMPTY);
						coddes3 = StringUtil.nvl(StringUtils.valueOf((char[]) lint.get(i)[2]),StringUtils.EMPTY);
						if (StringUtils.equals("C", sens)) pce_mt = pce_mt.multiply(new BigDecimal(-1));
						strPcemt = ClsStringUtil.oraLPad(pce_mt.setScale(0) + "", 10, " ");

						message = "2"
								+ separateur
								+ ClsStringUtil.oraRPad(" ", 6, " ")
								+ separateur
								+ "00001"
								+ separateur
								+ ClsStringUtil.oraRPad(StringUtil.oraSubstring(w_datcpt2, 1, 6), 10, " ")
								+ separateur //
								+ ClsStringUtil.oraRPad(StringUtil.nvl(StringUtil.oraSubstring(numcpt, 1, 6) + StringUtil.oraSubstring(coddes1, 1, 6)
										+ StringUtil.oraSubstring(coddes3, 1, 4)," "), 45, " ")
								+ separateur // No compte
								+ strPcemt
								+ separateur // ACCOUNTED_DR
								+ strPcemt
								+ separateur // ACCOUNTED_DR
								+ ClsStringUtil.oraRPad(w_libelle, 30, " ")
								+ separateur // libelle ecriture
								+ ClsStringUtil.oraRPad("PAYROLL GL ENTRY", 22, " ")
								+ separateur // libelle ecriture
								+ StringUtil.oraSubstring(w_datcpt2, 1, 4) + StringUtil.oraSubstring(w_datcpt2, 5, 2)
								+ StringUtil.oraSubstring(w_datcpt2, 7, 2) + separateur + "GL" + separateur // Filler
								+ "JE"; // Filler

						numero += 1;
						text = new TraitementTexte();
                        text.setSess(session);
                        text.setNlig(numero);
                        text.setIdEntreprise(new Integer(dossier));
						text.setTexte(message);
						//
						oSession.save(text);
						if (i % 20 == 0)
						{
							oSession.flush();
							oSession.clear();
						}

					}
					//
					// ------------ Generation des lignes dans patext
					// --- type compte = SYSCOA + compte personnel + CODE ICA
					
					String cp_int_v2=" SELECT NUMCPT, NUMTIE,CODDES2,CODDES3, DATCPT, SENS, COALESCE(SUM(PCE_MT), 0) PCE_MT ";
					cp_int_v2+=" FROM InterfComptable where cdos= '"+dossier+"' "+queryNumpce+"  and NUMTIE IS NOT NULL ";
					cp_int_v2+=" GROUP BY NUMCPT, NUMTIE,CODDES2, CODDES3, DATCPT, SENS ";
					
					query = cp_int_v2;
					q = oSession.createSQLQuery(query);
					lint = q.list();
					message = "";
					text = null;

					String coddes2;
					for (int i = 0; i < lint.size(); i++)
					{
						pce_mt = new BigDecimal(StringUtils.valueOf((char[]) lint.get(i)[6]));
						sens = StringUtils.valueOf((char[]) lint.get(i)[5]);
						numcpt = StringUtils.valueOf((char[]) lint.get(i)[0]);
						coddes2 = StringUtil.nvl(StringUtils.valueOf((char[]) lint.get(i)[2]), StringUtils.EMPTY);
						coddes3 = StringUtil.nvl(StringUtils.valueOf((char[]) lint.get(i)[3]), StringUtils.EMPTY);
						if (StringUtils.equals("C", sens)) pce_mt = pce_mt.multiply(new BigDecimal(-1));
						strPcemt = ClsStringUtil.oraLPad(pce_mt.setScale(0) + "", 10, " ");

						message = "2"
								+ separateur
								+ ClsStringUtil.oraRPad(" ", 6, " ")
								+ separateur
								+ "00001"
								+ separateur
								+ ClsStringUtil.oraRPad(StringUtil.oraSubstring(w_datcpt2, 1, 6), 10, " ")
								+ separateur //
								+ ClsStringUtil.oraRPad(StringUtil.oraSubstring(numcpt, 1, 6) + StringUtil.oraSubstring(coddes2, 1, 6)
										+ StringUtil.oraSubstring(coddes3, 1, 4), 45, " ")
								+ separateur // No compte
								+ strPcemt
								+ separateur // ACCOUNTED_DR
								+ strPcemt
								+ separateur // ACCOUNTED_DR
								+ ClsStringUtil.oraRPad(w_libelle, 30, " ")
								+ separateur // libelle ecriture
								+ ClsStringUtil.oraRPad("PAYROLL GL ENTRY", 22, " ")
								+ separateur // libelle ecriture
								+ StringUtil.oraSubstring(w_datcpt2, 1, 4) + StringUtil.oraSubstring(w_datcpt2, 5, 2)
								+ StringUtil.oraSubstring(w_datcpt2, 7, 2) + separateur + "GL" + separateur // Filler
								+ "JE"; // Filler

						numero += 1;
                        text = new TraitementTexte();
                        text.setSess(session);
                        text.setNlig(numero);
                        text.setIdEntreprise(new Integer(dossier));
						text.setTexte(message);
						//
						oSession.save(text);
						if (i % 20 == 0)
						{
							oSession.flush();
							oSession.clear();
						}

					}

					//
					// ------------ Generation des lignes dans patext
					// --- type compte = SYSCOA + compte bilan '000000' + CODE
					// ICA
					// IF Curs_04_03%ISOPEN THEN CLOSE Curs_04_03; END IF;
					
					String cp_int_v3=" SELECT NUMCPT, CODDES3, DATCPT, SENS, COALESCE(SUM(PCE_MT), 0) PCE_MT ";
					cp_int_v3+=" FROM InterfComptable where idEntreprise= '"+dossier+"' "+queryNumpce+"  AND COALESCE(SUBSTRING(NUMCPT,7,2),' ') != 'CC'  and NUMTIE IS NULL";
					cp_int_v3+=" GROUP BY NUMCPT, CODDES3, DATCPT, SENS ";
					
					query = cp_int_v3;
					q = oSession.createSQLQuery(query);
					lint = q.list();
					message = "";
					text = null;
					for (int i = 0; i < lint.size(); i++)
					{
						pce_mt = new BigDecimal(StringUtils.valueOf((char[]) lint.get(i)[4]));
						sens = StringUtils.valueOf((char[]) lint.get(i)[3]);
						numcpt = StringUtils.valueOf((char[]) lint.get(i)[0]);
						coddes3 = StringUtil.nvl(StringUtils.valueOf((char[]) lint.get(i)[1]), StringUtils.EMPTY);
						if (StringUtils.equals("C", sens)) pce_mt = pce_mt.multiply(new BigDecimal(-1));
						strPcemt = ClsStringUtil.oraLPad(pce_mt.setScale(0) + "", 10, " ");

						message = "2"
								+ separateur
								+ ClsStringUtil.oraRPad(" ", 6, " ")
								+ separateur
								+ "00001"
								+ separateur
								+ ClsStringUtil.oraRPad(StringUtil.oraSubstring(w_datcpt2, 1, 6), 10, " ")
								+ separateur //
								+ ClsStringUtil.oraRPad(StringUtil.oraSubstring(numcpt, 1, 6) + ClsStringUtil.oraLPad("0",nombrezero,"0")
										+ StringUtil.oraSubstring(coddes3, 1, 4), 45, " ")
								+ separateur // No compte
								+ strPcemt
								+ separateur // ACCOUNTED_DR
								+ strPcemt
								+ separateur // ACCOUNTED_DR
								+ ClsStringUtil.oraRPad(w_libelle, 30, " ")
								+ separateur // libelle ecriture
								+ ClsStringUtil.oraRPad("PAYROLL GL ENTRY", 22, " ")
								+ separateur // libelle ecriture
								+ StringUtil.oraSubstring(w_datcpt2, 1, 4) + StringUtil.oraSubstring(w_datcpt2, 5, 2)
								+ StringUtil.oraSubstring(w_datcpt2, 7, 2) + separateur + "GL" + separateur // Filler
								+ "JE"; // Filler

						numero += 1;
                        text = new TraitementTexte();
                        text.setSess(session);
                        text.setNlig(numero);
                        text.setIdEntreprise(new Integer(dossier));
						text.setTexte(message);
						//
						oSession.save(text);
						if (i % 20 == 0)
						{
							oSession.flush();
							oSession.clear();
						}

					}
					// ---- Fin interfaces ACCPAC
				}
			}
			else
			{
				String cp_int_v_s=" SELECT NUMCPT,NUMTIE,CODDES1, DATCPT, SENS, COALESCE(SUM(PCE_MT), 0) PCE_MT";
				cp_int_v_s+=" FROM cp_int where cdos= '"+dossier+"' "+queryNumpce+" ";
				cp_int_v_s+=" GROUP BY NUMCPT,NUMTIE,CODDES1, DATCPT, SENS ";
				
				
				String query = cp_int_v_s;
				Query q = oSession.createSQLQuery(query);
				List<Object[]> lint = q.list();
				String message = "";
				TraitementTexte text = null;

				String sens;
				String numcpt;
				String numtie;
				String coddes1;
				BigDecimal pce_mt;
				String strPcemt;
				Date datcpt;

				for (int i = 0; i < lint.size(); i++)
				{
					pce_mt = new BigDecimal(StringUtils.valueOf((char[]) lint.get(i)[5]));
					datcpt = (Date) lint.get(i)[3];
					sens = StringUtils.valueOf((char[]) lint.get(i)[4]);
					numcpt = StringUtils.valueOf((char[]) lint.get(i)[0]);
					numtie = StringUtil.nvl(StringUtils.valueOf((char[]) lint.get(i)[1]),StringUtils.EMPTY);
					coddes1 = StringUtil.nvl(StringUtils.valueOf((char[]) lint.get(i)[2]), StringUtils.EMPTY);
					if (StringUtils.equals("C", sens)) pce_mt = pce_mt.multiply(new BigDecimal(-1));
					strPcemt = ClsStringUtil.oraLPad(pce_mt.setScale(0) + "", 10, " ");
					if (StringUtils.isBlank(coddes1)) coddes1 = StringUtil.nvl(coddes1, "01454");
					else coddes1 = ClsStringUtil.oraLPad(StringUtil.oraSubstring(coddes1, 1, 5), 5, " ");

					if (StringUtils.isBlank(numtie)) numtie = ClsStringUtil.oraLPad(" ", 8, " ") + separateur + " ";
					else numtie = ClsStringUtil.oraLPad(numtie, 8, "0") + separateur + "A";

					message = "JP" + separateur + ClsStringUtil.oraLPad("1", 8, "0") + separateur + "01454" + separateur + new ClsDate(datcpt).getDateS("ddMMyy")
							+ separateur + strPcemt + separateur + coddes1 + "-" + ClsStringUtil.oraLPad(StringUtil.oraSubstring(numcpt, 1, 4), 4, " ") + "-"
							+ ClsStringUtil.oraLPad(StringUtil.oraSubstring(numcpt, 5, 4), 4, " ") + separateur + numtie + separateur
							+ ClsStringUtil.oraLPad(" ", 30, " ");

					numero += 1;
                    text = new TraitementTexte();
                    text.setSess(session);
                    text.setNlig(numero);
                    text.setIdEntreprise(new Integer(dossier));
					text.setTexte(message);
					//
					oSession.save(text);
					if (i % 20 == 0)
					{
						oSession.flush();
						oSession.clear();
					}

				}

			}

			tx.commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			if (tx != null) tx.rollback();
			errmess1 = ClsTreater._getStackTrace(e);
			writeLog(errmess1);
			pb = true;
			globalUpdate._setEvolutionTraitement(request, errmess1, true);
			return;
		}
		finally
		{
			service.closeSession(oSession);
		}
	}
	

//  public void generateOGLBankInterfaceToFile(HttpServletRequest request)
//  {
//    Rhfnom nomenc = null;
//    String separateur = "";
//    Object o = this.service.get(Rhfnom.class, new RhfnomPK(this.dossier, Integer.valueOf(99), "PAIE-INTER", Integer.valueOf(5)));
//    if (o != null)
//    {
//      nomenc = (Rhfnom)o;
//      if (!ClsObjectUtil.isNull(nomenc.getVall())) {
//        separateur = nomenc.getVall().trim();
//      }
//    }
//    else
//    {
//      this.errmess1 = "Erreur : Creez PAIE-INTER, Lib5 en Tb99.";
//      writeLog(this.errmess1);
//      this.pb = true;
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
//      return;
//    }
//    if (ClsObjectUtil.isNull(separateur))
//    {
//      this.errmess1 = "Renseignez Lib5 de PAIE-INTER en Tb99.";
//      writeLog(this.errmess1);
//      this.pb = true;
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
//      return;
//    }
//    int numero = _getCurrentDBMaxRhttext(this.service, this.session).intValue();
//
//
//    String numpiece = new ClsDate(this.globalUpdate.getPeriode(), "yyyyMM").getDateS("MMyyyy") + this.globalUpdate.getNumerobulletin();
//
//
//
//
//
//    Rhfnom obj = (Rhfnom)this.service.get(Rhfnom.class, new RhfnomPK(this.dossier, Integer.valueOf(99), "INT-BANK", Integer.valueOf(1)));
//    Rhfnom obj2 = (Rhfnom)this.service.get(Rhfnom.class, new RhfnomPK(this.dossier, Integer.valueOf(99), "INT-BANK", Integer.valueOf(2)));
//    Rhfnom obj3 = (Rhfnom)this.service.get(Rhfnom.class, new RhfnomPK(this.dossier, Integer.valueOf(99), "INT-BANK", Integer.valueOf(3)));
//    Rhfnom obj4 = (Rhfnom)this.service.get(Rhfnom.class, new RhfnomPK(this.dossier, Integer.valueOf(99), "PAIE-INTER", Integer.valueOf(4)));
//    Rhfnom rubq = (Rhfnom)this.service.get(Rhfnom.class, new RhfnomPK(this.dossier, Integer.valueOf(99), "RUBNAP", Integer.valueOf(1)));
//
//
//    String query = " SELECT a.CDOS, a.CODETS, a.CODJOU, a.NUMCPT, a.NUMTIE, a.NUMPCE, a.DATCPT, a.DATPCE, a.DATECH,a.DEVPCE, a.QUANTITE, a.SENS, a.PCE_MT, a.REFLET, a.CODDES1,  a.CODDES2, a.CODDES3, a.CODDES4, a.CODDES5, a.CODDES6, a.CODDES7, a.CODDES8, a.CODDES9, a.LIBECR,  a.CODTRE, a.CODABR, a.CODERR, a.LIBERR, a.DATCRE, a.DATMOD, a.CODUTI,ag.equi FROM cp_int a   left join rhpagent ag on (ag.cdos=a.cdos and ag.nmat=a.coddes6) where a.cdos=" +
//
//
//
//
//
//      this.dossier;
//
//    query = query + "  order by a.cdos, codjou, codets, numpce, datcpt, devpce, numcpt, numtie,  sens, reflet, coddes1, coddes2, coddes3, coddes4, coddes5, coddes6, coddes7, coddes8, coddes9";
//
//
//
//    String querygrp = " SELECT a.CDOS, a.CODETS, a.CODJOU, a.NUMCPT, a.NUMTIE, a.NUMPCE, a.DATCPT, a.DATPCE, a.DATECH,a.DEVPCE, a.QUANTITE, a.SENS, SUM(a.PCE_MT), a.REFLET, a.CODDES1,  a.CODDES2, a.CODDES3, a.CODDES4, a.CODDES5, a.CODDES6, a.CODDES7, a.CODDES8, a.CODDES9, a.LIBECR,  a.CODTRE, a.CODABR, a.CODERR, a.LIBERR, a.DATCRE, a.DATMOD, a.CODUTI,ag.equi FROM cp_int a   left join rhpagent ag on (ag.cdos=a.cdos and ag.nmat=a.coddes6) where a.cdos=" +
//
//
//
//
//
//      this.dossier;
//
//    querygrp = querygrp + " group by a.CDOS, a.CODETS, a.CODJOU, a.NUMCPT, a.NUMTIE, a.NUMPCE,   a.DATCPT, a.DATPCE, a.DATECH,a.DEVPCE,a.SENS, a.QUANTITE,  a.REFLET, a.CODDES1,   a.CODDES2, a.CODDES3, a.CODDES4, a.CODDES5, a.CODDES6, a.CODDES7, a.CODDES8, a.CODDES9, a.LIBECR,   a.CODTRE, a.CODABR, a.CODERR, a.LIBERR, a.DATCRE, a.DATMOD, a.CODUTI,ag.equi ";
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//    String requete = "DELETE FROM Rhttext WHERE idEntreprise = '" + this.dossier + "' and sess='" + this.session + "'";
//    Session oSession = null;
//    Transaction tx = null;
//    try
//    {
//      oSession = this.service.getSession();
//      tx = oSession.beginTransaction();
//      oSession.createQuery(requete).executeUpdate();
//
//      String currentquery = "";
//      if (obj.getVall().equalsIgnoreCase("O")) {
//        currentquery = querygrp;
//      } else {
//        currentquery = query;
//      }
//      List<Object[]> lint = oSession.createSQLQuery(currentquery).list();
//
//      String message = "";
//      Rhttext text = null;
//      int j = 0;
//
//
//
//
//
//
//
//
//
//
//
//      int w_nddd = 0;
//      List l = this.service.find("select nddd from Cpdo where idEntreprise = '" + this.dossier + "'");
//      if ((l != null) && (l.size() > 0))
//      {
//        if (l.get(0) != null) {
//          w_nddd = new Integer(l.get(0).toString()).intValue();
//        }
//      }
//      else {
//        w_nddd = ((Rhfnom)o).getValm().intValue();
//      }
//      String libelle = "";
//      if (StringUtils.isNotBlank(obj4.getVall()))
//      {
//        libelle = obj4.getVall() + " " + getDateM("yyyyMM", "MMMM", this.globalUpdate.getPeriode()) + " " + getDateM("yyyyMM", "yyyy", this.globalUpdate.getPeriode());
//      }
//      else
//      {
//        Rhfnom obj5 = (Rhfnom)this.service.get(Rhfnom.class, new RhfnomPK(this.dossier, Integer.valueOf(92), this.globalUpdate.getPeriode(), Integer.valueOf(1)));
//        if ((obj5 != null) && (obj5.getVall() != null)) {
//          libelle = obj5.getVall() + " " + getDateM("yyyyMM", "MMMM", this.globalUpdate.getPeriode()) + " " + getDateM("yyyyMM", "yyyy", this.globalUpdate.getPeriode());
//        } else {
//          libelle = "Paie:Bul:" + this.globalUpdate.getNumerobulletin() + "/" + getDateM("yyyyMM", "MMMM", this.globalUpdate.getPeriode()) + " " + getDateM("yyyyMM", "yyyy", this.globalUpdate.getPeriode());
//        }
//      }
//      for (int i = 0; i < lint.size(); i++)
//      {
//        Object[] rhtmpai = (Object[])lint.get(i);
//        String suffixe = "";
//        if (((String)rhtmpai[3]).contains("/"))
//        {
//          String[] tabvalue1 = ((String)rhtmpai[3]).split("/");
//          suffixe = tabvalue1[1];
//        }
//        String dco = ClsDate.getDateS((Date)rhtmpai[6], "dd/MM/yyyy");
//
//        String w_ligne = ClsStringUtil.formatNumber(obj.getValm(), w_nddd, 19, false, ',') + "|" + obj2.getVall() + "|" + "|" +
//          (String)rhtmpai[3] + "|" + suffixe + "|" + obj.getVall() + "|" + "|" + "|" +
//          "AUTO" + "|" + "|" + "|" + dco + "|" + (String)rhtmpai[31] + "|" + "|" +
//          ClsStringUtil.formatNumber((Number)rhtmpai[12], w_nddd, 19, false, ',') + "|" + (String)rhtmpai[11] + "|" + libelle + "|" + "|" +
//          numpiece + "|" + obj.getVall() + "|" + "|" + "|" + "|" + "|" + "|" +
//          "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" +
//          "|" + "|" + "|" + ClsStringUtil.formatNumber(obj.getValm(), w_nddd, 19, false, ',') + "|" +
//          "|" + "|" + obj2.getVall() + "|" + ClsStringUtil.formatNumber((Number)rhtmpai[12], w_nddd, 19, false, ',') + "|" +
//          numpiece + "|" + "|" + "|";
//
//        j = i + 1;
//
//        message = w_ligne;
//
//        numero++;
//        text = new Rhttext();
//        text.setComp_id(new RhttextPK(this.session, Integer.valueOf(numero)));
//        text.setCdos(this.dossier);
//        text.setTexte(message);
//
//        oSession.save(text);
//
//        String guichet = "";
//        String compte = "";
//        String cle = "";
//        List<Object[]> lstvrmtagent = this.service.find("select guic,comp,cle from Rhtvrmtagent where idEntreprise = '" + this.dossier + "'" + " and nmat= '" + (String)rhtmpai[19] + "'");
//        if ((lstvrmtagent != null) && (lstvrmtagent.size() > 0)) {
//          if (lstvrmtagent.get(0) != null)
//          {
//            Object[] rhtvrmt = (Object[])lstvrmtagent.get(0);
//            guichet = (String)rhtvrmt[0];
//            compte = (String)rhtvrmt[1];
//            cle = (String)rhtvrmt[2];
//          }
//        }
//        if (((String)rhtmpai[22]).equalsIgnoreCase(String.valueOf(rubq.getValm())))
//        {
//          if (String.valueOf(obj.getValm()).equalsIgnoreCase(guichet))
//          {
//            w_ligne =
//
//
//
//
//
//
//
//              ClsStringUtil.formatNumber(obj.getValm(), w_nddd, 19, false, ',') + "|" + obj2.getVall() + "|" + "|" + (String)rhtmpai[3] + "|" + suffixe + "|" + obj.getVall() + "|" + "|" + "|" + "AUTO" + "|" + "|" + "|" + dco + "|" + (String)rhtmpai[31] + "|" + "|" + ClsStringUtil.formatNumber((Number)rhtmpai[12], w_nddd, 19, false, ',') + "|" + "D" + "|" + libelle + "|" + "|" + numpiece + "|" + obj.getVall() + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + ClsStringUtil.formatNumber(obj.getValm(), w_nddd, 19, false, ',') + "|" + "|" + "|" + obj2.getVall() + "|" + ClsStringUtil.formatNumber((Number)rhtmpai[12], w_nddd, 19, false, ',') + "|" + numpiece + "|" + "|" + "|";
//
//            j = i + 1;
//
//            message = w_ligne;
//
//            numero++;
//            text = new Rhttext();
//            text.setComp_id(new RhttextPK(this.session, Integer.valueOf(numero)));
//            text.setCdos(this.dossier);
//            text.setTexte(message);
//
//            oSession.save(text);
//
//
//
//
//            w_ligne = ClsStringUtil.formatNumber(obj.getValm(), w_nddd, 19, false, ',') + "|" + obj2.getVall() + "|" + "|" +
//              compte + "|" + suffixe + "|" + obj.getVall() + "|" + "|" + "|" +
//              "AUTO" + "|" + "|" + "|" + dco + "|" + (String)rhtmpai[31] + "|" + "|" +
//              ClsStringUtil.formatNumber((Number)rhtmpai[12], w_nddd, 19, false, ',') + "|" + "C" + "|" + libelle + "|" + "|" +
//              numpiece + "|" + obj.getVall() + "|" + "|" + "|" + "|" + "|" + "|" +
//              "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" +
//              "|" + "|" + "|" + ClsStringUtil.formatNumber(obj.getValm(), w_nddd, 19, false, ',') + "|" +
//              "|" + "|" + obj2.getVall() + "|" + ClsStringUtil.formatNumber((Number)rhtmpai[12], w_nddd, 19, false, ',') + "|" +
//              numpiece + "|" + "|" + "|";
//
//            j = i + 1;
//
//            message = w_ligne;
//
//            numero++;
//            text = new Rhttext();
//            text.setComp_id(new RhttextPK(this.session, Integer.valueOf(numero)));
//            text.setCdos(this.dossier);
//            text.setTexte(message);
//
//            oSession.save(text);
//          }
//          else
//          {
//            Rhfnom netP = (Rhfnom)this.service.get(Rhfnom.class, new RhfnomPK(this.dossier, Integer.valueOf(39), guichet, Integer.valueOf(2)));
//
//
//            w_ligne = ClsStringUtil.formatNumber(obj.getValm(), w_nddd, 19, false, ',') + "|" + obj2.getVall() + "|" + "|" +
//              (String)rhtmpai[3] + "|" + suffixe + "|" + obj.getVall() + "|" + "|" + "|" +
//              "AUTO" + "|" + "|" + "|" + dco + "|" + (String)rhtmpai[31] + "|" + "|" +
//              ClsStringUtil.formatNumber((Number)rhtmpai[12], w_nddd, 19, false, ',') + "|" + "D" + "|" + libelle + "|" + "|" +
//              numpiece + "|" + obj.getVall() + "|" + "|" + "|" + "|" + "|" + "|" +
//              "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" +
//              "|" + "|" + "|" + ClsStringUtil.formatNumber(obj.getValm(), w_nddd, 19, false, ',') + "|" +
//              "|" + "|" + obj2.getVall() + "|" + ClsStringUtil.formatNumber((Number)rhtmpai[12], w_nddd, 19, false, ',') + "|" +
//              numpiece + "|" + "|" + "|";
//
//            j = i + 1;
//
//            message = w_ligne;
//
//            numero++;
//            text = new Rhttext();
//            text.setComp_id(new RhttextPK(this.session, Integer.valueOf(numero)));
//            text.setCdos(this.dossier);
//            text.setTexte(message);
//
//            oSession.save(text);
//
//
//
//
//            w_ligne = ClsStringUtil.formatNumber(obj.getValm(), w_nddd, 19, false, ',') + "|" + obj2.getVall() + "|" + "|" +
//              netP.getVall() + "|" + suffixe + "|" + obj.getVall() + "|" + "|" + "|" +
//              "AUTO" + "|" + "|" + "|" + dco + "|" + (String)rhtmpai[31] + "|" + "|" +
//              ClsStringUtil.formatNumber((Number)rhtmpai[12], w_nddd, 19, false, ',') + "|" + "C" + "|" + libelle + "|" + "|" +
//              numpiece + "|" + obj.getVall() + "|" + "|" + "|" + "|" + "|" + "|" +
//              "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" +
//              "|" + "|" + "|" + ClsStringUtil.formatNumber(obj.getValm(), w_nddd, 19, false, ',') + "|" +
//              "|" + "|" + obj2.getVall() + "|" + ClsStringUtil.formatNumber((Number)rhtmpai[12], w_nddd, 19, false, ',') + "|" +
//              numpiece + "|" + "|" + "|";
//
//            j = i + 1;
//
//            message = w_ligne;
//
//            numero++;
//            text = new Rhttext();
//            text.setComp_id(new RhttextPK(this.session, Integer.valueOf(numero)));
//            text.setCdos(this.dossier);
//            text.setTexte(message);
//
//            oSession.save(text);
//
//
//
//
//
//
//            w_ligne = ClsStringUtil.formatNumber(obj.getValm(), w_nddd, 19, false, ',') + "|" + obj2.getVall() + "|" + "|" +
//              netP.getVall() + "|" + suffixe + "|" + obj.getVall() + "|" + "|" + "|" +
//              "AUTO" + "|" + "|" + "|" + dco + "|" + (String)rhtmpai[31] + "|" + "|" +
//              ClsStringUtil.formatNumber((Number)rhtmpai[12], w_nddd, 19, false, ',') + "|" + "D" + "|" + libelle + "|" + "|" +
//              numpiece + "|" + obj.getVall() + "|" + "|" + "|" + "|" + "|" + "|" +
//              "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" +
//              "|" + "|" + "|" + ClsStringUtil.formatNumber(obj.getValm(), w_nddd, 19, false, ',') + "|" +
//              "|" + "|" + obj2.getVall() + "|" + ClsStringUtil.formatNumber((Number)rhtmpai[12], w_nddd, 19, false, ',') + "|" +
//              numpiece + "|" + "|" + "|";
//
//            j = i + 1;
//
//            message = w_ligne;
//
//            numero++;
//            text = new Rhttext();
//            text.setComp_id(new RhttextPK(this.session, Integer.valueOf(numero)));
//            text.setCdos(this.dossier);
//            text.setTexte(message);
//
//            oSession.save(text);
//
//
//
//
//            w_ligne = ClsStringUtil.formatNumber(obj.getValm(), w_nddd, 19, false, ',') + "|" + obj2.getVall() + "|" + "|" +
//              compte + "|" + suffixe + "|" + obj.getVall() + "|" + "|" + "|" +
//              "AUTO" + "|" + "|" + "|" + dco + "|" + (String)rhtmpai[31] + "|" + "|" +
//              ClsStringUtil.formatNumber((Number)rhtmpai[12], w_nddd, 19, false, ',') + "|" + "C" + "|" + libelle + "|" + "|" +
//              numpiece + "|" + obj.getVall() + "|" + "|" + "|" + "|" + "|" + "|" +
//              "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" +
//              "|" + "|" + "|" + ClsStringUtil.formatNumber(obj.getValm(), w_nddd, 19, false, ',') + "|" +
//              "|" + "|" + obj2.getVall() + "|" + ClsStringUtil.formatNumber((Number)rhtmpai[12], w_nddd, 19, false, ',') + "|" +
//              numpiece + "|" + "|" + "|";
//
//            j = i + 1;
//
//            message = w_ligne;
//
//            numero++;
//            text = new Rhttext();
//            text.setComp_id(new RhttextPK(this.session, Integer.valueOf(numero)));
//            text.setCdos(this.dossier);
//            text.setTexte(message);
//
//            oSession.save(text);
//          }
//        }
//        else
//        {
//          List lstrubq = this.service.find("From Rhprubrique where idEntreprise = '" + this.dossier + "'" + " and crub='" + (String)rhtmpai[22] + "'" + " and algo in (17,20)");
//          if ((lstrubq != null) && (lstrubq.size() > 0)) {
//            if (String.valueOf(obj.getValm()).equalsIgnoreCase(guichet))
//            {
//              w_ligne =
//
//
//
//
//
//
//
//                ClsStringUtil.formatNumber(obj.getValm(), w_nddd, 19, false, ',') + "|" + obj2.getVall() + "|" + "|" + (String)rhtmpai[3] + "|" + suffixe + "|" + obj.getVall() + "|" + "|" + "|" + "AUTO" + "|" + "|" + "|" + dco + "|" + (String)rhtmpai[31] + "|" + "|" + ClsStringUtil.formatNumber((Number)rhtmpai[12], w_nddd, 19, false, ',') + "|" + "C" + "|" + libelle + "|" + "|" + numpiece + "|" + obj.getVall() + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + ClsStringUtil.formatNumber(obj.getValm(), w_nddd, 19, false, ',') + "|" + "|" + "|" + obj2.getVall() + "|" + ClsStringUtil.formatNumber((Number)rhtmpai[12], w_nddd, 19, false, ',') + "|" + numpiece + "|" + "|" + "|";
//
//              j = i + 1;
//
//              message = w_ligne;
//
//              numero++;
//              text = new Rhttext();
//              text.setComp_id(new RhttextPK(this.session, Integer.valueOf(numero)));
//              text.setCdos(this.dossier);
//              text.setTexte(message);
//
//              oSession.save(text);
//
//
//
//
//              w_ligne = ClsStringUtil.formatNumber(obj.getValm(), w_nddd, 19, false, ',') + "|" + obj2.getVall() + "|" + "|" +
//                compte + "|" + suffixe + "|" + obj.getVall() + "|" + "|" + "|" +
//                "AUTO" + "|" + "|" + "|" + dco + "|" + (String)rhtmpai[31] + "|" + "|" +
//                ClsStringUtil.formatNumber((Number)rhtmpai[12], w_nddd, 19, false, ',') + "|" + "D" + "|" + libelle + "|" + "|" +
//                numpiece + "|" + obj.getVall() + "|" + "|" + "|" + "|" + "|" + "|" +
//                "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" +
//                "|" + "|" + "|" + ClsStringUtil.formatNumber(obj.getValm(), w_nddd, 19, false, ',') + "|" +
//                "|" + "|" + obj2.getVall() + "|" + ClsStringUtil.formatNumber((Number)rhtmpai[12], w_nddd, 19, false, ',') + "|" +
//                numpiece + "|" + "|" + "|";
//
//              j = i + 1;
//
//              message = w_ligne;
//
//              numero++;
//              text = new Rhttext();
//              text.setComp_id(new RhttextPK(this.session, Integer.valueOf(numero)));
//              text.setCdos(this.dossier);
//              text.setTexte(message);
//
//              oSession.save(text);
//            }
//            else
//            {
//              Rhfnom pret = (Rhfnom)this.service.get(Rhfnom.class, new RhfnomPK(this.dossier, Integer.valueOf(39), guichet, Integer.valueOf(3)));
//
//              w_ligne = ClsStringUtil.formatNumber(obj.getValm(), w_nddd, 19, false, ',') + "|" + obj2.getVall() + "|" + "|" +
//                (String)rhtmpai[3] + "|" + suffixe + "|" + obj.getVall() + "|" + "|" + "|" +
//                "AUTO" + "|" + "|" + "|" + dco + "|" + (String)rhtmpai[31] + "|" + "|" +
//                ClsStringUtil.formatNumber((Number)rhtmpai[12], w_nddd, 19, false, ',') + "|" + "C" + "|" + libelle + "|" + "|" +
//                numpiece + "|" + obj.getVall() + "|" + "|" + "|" + "|" + "|" + "|" +
//                "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" +
//                "|" + "|" + "|" + ClsStringUtil.formatNumber(obj.getValm(), w_nddd, 19, false, ',') + "|" +
//                "|" + "|" + obj2.getVall() + "|" + ClsStringUtil.formatNumber((Number)rhtmpai[12], w_nddd, 19, false, ',') + "|" +
//                numpiece + "|" + "|" + "|";
//
//              j = i + 1;
//
//              message = w_ligne;
//
//              numero++;
//              text = new Rhttext();
//              text.setComp_id(new RhttextPK(this.session, Integer.valueOf(numero)));
//              text.setCdos(this.dossier);
//              text.setTexte(message);
//
//              oSession.save(text);
//
//
//
//
//              w_ligne = ClsStringUtil.formatNumber(obj.getValm(), w_nddd, 19, false, ',') + "|" + obj2.getVall() + "|" + "|" +
//                pret.getVall() + "|" + suffixe + "|" + obj.getVall() + "|" + "|" + "|" +
//                "AUTO" + "|" + "|" + "|" + dco + "|" + (String)rhtmpai[31] + "|" + "|" +
//                ClsStringUtil.formatNumber((Number)rhtmpai[12], w_nddd, 19, false, ',') + "|" + "D" + "|" + libelle + "|" + "|" +
//                numpiece + "|" + obj.getVall() + "|" + "|" + "|" + "|" + "|" + "|" +
//                "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" +
//                "|" + "|" + "|" + ClsStringUtil.formatNumber(obj.getValm(), w_nddd, 19, false, ',') + "|" +
//                "|" + "|" + obj2.getVall() + "|" + ClsStringUtil.formatNumber((Number)rhtmpai[12], w_nddd, 19, false, ',') + "|" +
//                numpiece + "|" + "|" + "|";
//
//              j = i + 1;
//
//              message = w_ligne;
//
//              numero++;
//              text = new Rhttext();
//              text.setComp_id(new RhttextPK(this.session, Integer.valueOf(numero)));
//              text.setCdos(this.dossier);
//              text.setTexte(message);
//
//              oSession.save(text);
//
//
//
//
//
//
//              w_ligne = ClsStringUtil.formatNumber(obj.getValm(), w_nddd, 19, false, ',') + "|" + obj2.getVall() + "|" + "|" +
//                pret.getVall() + "|" + suffixe + "|" + obj.getVall() + "|" + "|" + "|" +
//                "AUTO" + "|" + "|" + "|" + dco + "|" + (String)rhtmpai[31] + "|" + "|" +
//                ClsStringUtil.formatNumber((Number)rhtmpai[12], w_nddd, 19, false, ',') + "|" + "C" + "|" + libelle + "|" + "|" +
//                numpiece + "|" + obj.getVall() + "|" + "|" + "|" + "|" + "|" + "|" +
//                "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" +
//                "|" + "|" + "|" + ClsStringUtil.formatNumber(obj.getValm(), w_nddd, 19, false, ',') + "|" +
//                "|" + "|" + obj2.getVall() + "|" + ClsStringUtil.formatNumber((Number)rhtmpai[12], w_nddd, 19, false, ',') + "|" +
//                numpiece + "|" + "|" + "|";
//
//              j = i + 1;
//
//              message = w_ligne;
//
//              numero++;
//              text = new Rhttext();
//              text.setComp_id(new RhttextPK(this.session, Integer.valueOf(numero)));
//              text.setCdos(this.dossier);
//              text.setTexte(message);
//
//              oSession.save(text);
//
//
//
//
//              w_ligne = ClsStringUtil.formatNumber(obj.getValm(), w_nddd, 19, false, ',') + "|" + obj2.getVall() + "|" + "|" +
//                compte + "|" + suffixe + "|" + obj.getVall() + "|" + "|" + "|" +
//                "AUTO" + "|" + "|" + "|" + dco + "|" + (String)rhtmpai[31] + "|" + "|" +
//                ClsStringUtil.formatNumber((Number)rhtmpai[12], w_nddd, 19, false, ',') + "|" + "D" + "|" + libelle + "|" + "|" +
//                numpiece + "|" + obj.getVall() + "|" + "|" + "|" + "|" + "|" + "|" +
//                "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" +
//                "|" + "|" + "|" + ClsStringUtil.formatNumber(obj.getValm(), w_nddd, 19, false, ',') + "|" +
//                "|" + "|" + obj2.getVall() + "|" + ClsStringUtil.formatNumber((Number)rhtmpai[12], w_nddd, 19, false, ',') + "|" +
//                numpiece + "|" + "|" + "|";
//
//              j = i + 1;
//
//              message = w_ligne;
//
//              numero++;
//              text = new Rhttext();
//              text.setComp_id(new RhttextPK(this.session, Integer.valueOf(numero)));
//              text.setCdos(this.dossier);
//              text.setTexte(message);
//
//              oSession.save(text);
//            }
//          }
//        }
//        if (i % 20 == 0)
//        {
//          oSession.flush();
//          oSession.clear();
//        }
//      }
//      tx.commit();
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//      if (tx != null) {
//        tx.rollback();
//      }
//      this.errmess1 = ClsTreater._getStackTrace(e);
//      writeLog(this.errmess1);
//      this.pb = true;
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
//      return;
//    }
//    finally
//    {
//      this.service.closeConnexion(oSession);
//    }
//  }
  
  public String getDateM(String strFormat1, String strFormat2, String date)
  {
    DateFormat oFormat1 = new SimpleDateFormat(strFormat1);
    DateFormat oFormat2 = new SimpleDateFormat(strFormat2);
    Date dd1 = null;
    
    Calendar calendrier = Calendar.getInstance();
    try
    {
      dd1 = oFormat1.parse(date);
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }
    return oFormat2.format(dd1);
  }
//
//  public void generateOGL_ISBA(HttpServletRequest request)
//  {
//    Rhfnom nomenc = null;
//    String separateur = "";
//    Object o = this.service.get(Rhfnom.class, new RhfnomPK(this.dossier, Integer.valueOf(99), "PAIE-INTER", Integer.valueOf(5)));
//    if (o != null)
//    {
//      nomenc = (Rhfnom)o;
//      if (!ClsObjectUtil.isNull(nomenc.getVall())) {
//        separateur = nomenc.getVall().trim();
//      }
//    }
//    else
//    {
//      this.errmess1 = "Erreur : Creez PAIE-INTER, Lib5 en Tb99.";
//      writeLog(this.errmess1);
//      this.pb = true;
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
//      return;
//    }
//    if (ClsObjectUtil.isNull(separateur))
//    {
//      this.errmess1 = "Renseignez Lib5 de PAIE-INTER en Tb99.";
//      writeLog(this.errmess1);
//      this.pb = true;
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
//      return;
//    }
//    int nddd = 0;
//    List l = this.service.find("select nddd from Cpdo where idEntreprise = '" + this.dossier + "'");
//    if ((l != null) && (l.size() > 0))
//    {
//      if (l.get(0) != null) {
//        nddd = new Integer(l.get(0).toString()).intValue();
//      }
//    }
//    else
//    {
//      this.errmess1 = this.globalUpdate.getParameter().errorMessage("ERR-30060", this.langue, new Object[0]);
//      writeLog(this.errmess1);
//      this.pb = true;
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
//      return;
//    }
//    int numero = _getCurrentDBMaxRhttext(this.service, this.session).intValue();
//
//    String numpiece = new ClsDate(this.globalUpdate.getPeriode(), "yyyyMM").getDateS("MMyyyy") + this.globalUpdate.getNumerobulletin();
//
//
//    String query = " select codjou,datcpt,numcpt,reflet,coddes1,numpce,libecr,sens,pce_mt,devpce,coduti,b.afec, b.ccpt,c.bqag,c.guic,c.comp,c.cle ";
//    query = query + " from cp_int a ";
//    query = query + " left join rhpagent b on a.cdos=b.cdos and a.coddes8=b.nmat ";
//    query = query + " left join rhtvrmtagent c on a.cdos=c.cdos and a.coddes8=c.nmat ";
//    query = query + " where a.cdos='" + this.dossier + "' and numpce='" + numpiece + "' ";
//    query = query + " order by numcpt ";
//
//
//
//    String requete = "DELETE FROM Rhttext WHERE idEntreprise = '" + this.dossier + "' and sess='" + this.session + "'";
//    Session oSession = null;
//    Transaction tx = null;
//    try
//    {
//      oSession = this.service.getSession();
//      tx = oSession.beginTransaction();
//      oSession.createQuery(requete).executeUpdate();
//
//      List<Object[]> lint = oSession.createSQLQuery(query).list();
//
//      String message = "";
//      Rhttext text = null;
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//      Integer index = Integer.valueOf(0);
//      for (int i = 0; i < lint.size(); i++)
//      {
//        index = Integer.valueOf(0);
//        String nste = this.dossier; Integer
//          tmp634_632 = index;index = Integer.valueOf(tmp634_632.intValue() + 1);String cjou = StringUtils.nvl(StringUtils.valueOf(((Object[])lint.get(i))[tmp634_632.intValue()]), ""); Integer
//          tmp673_671 = index;index = Integer.valueOf(tmp673_671.intValue() + 1);Date dtcp = (Date)((Object[])lint.get(i))[tmp673_671.intValue()]; Integer
//          tmp707_705 = index;index = Integer.valueOf(tmp707_705.intValue() + 1);String ncpt = StringUtils.nvl(StringUtils.valueOf(((Object[])lint.get(i))[tmp707_705.intValue()]), ""); Integer
//          tmp746_744 = index;index = Integer.valueOf(tmp746_744.intValue() + 1);String refl = StringUtils.nvl(StringUtils.valueOf(((Object[])lint.get(i))[tmp746_744.intValue()]), ""); Integer
//          tmp785_783 = index;index = Integer.valueOf(tmp785_783.intValue() + 1);String nsec = StringUtils.nvl(StringUtils.valueOf(((Object[])lint.get(i))[tmp785_783.intValue()]), ""); Integer
//          tmp824_822 = index;index = Integer.valueOf(tmp824_822.intValue() + 1);String ncpe = StringUtils.nvl(StringUtils.valueOf(((Object[])lint.get(i))[tmp824_822.intValue()]), "");
//        Date dtsa = new Date();
//        String liba = ""; Integer
//          tmp876_874 = index;index = Integer.valueOf(tmp876_874.intValue() + 1);String libe = StringUtils.nvl(StringUtils.valueOf(((Object[])lint.get(i))[tmp876_874.intValue()]), "");
//        String cptc = ""; Integer
//          tmp919_917 = index;index = Integer.valueOf(tmp919_917.intValue() + 1);String sens = StringUtils.nvl(StringUtils.valueOf(((Object[])lint.get(i))[tmp919_917.intValue()]), ""); Integer
//          tmp962_960 = index;index = Integer.valueOf(tmp962_960.intValue() + 1);BigDecimal mtdd = new BigDecimal(StringUtils.nvl(StringUtils.valueOf(((Object[])lint.get(i))[tmp962_960.intValue()]), "0")).setScale(nddd);
//        String cour = "1";
//        BigDecimal mtdc = mtdd; Integer
//          tmp1019_1017 = index;index = Integer.valueOf(tmp1019_1017.intValue() + 1);String cdev = StringUtils.nvl(StringUtils.valueOf(((Object[])lint.get(i))[tmp1019_1017.intValue()]), ""); Integer
//          tmp1058_1056 = index;index = Integer.valueOf(tmp1058_1056.intValue() + 1);String cuti = StringUtils.nvl(StringUtils.valueOf(((Object[])lint.get(i))[tmp1058_1056.intValue()]), "");
//        String dtec = "";
//        Date dtvl = new Date();
//        String mpnr = "";
//        String stat = this.globalUpdate.getNumerobulletin()+"";
//        String cenr = "";
//        String dfac = ""; Integer
//
//          tmp1144_1142 = index;index = Integer.valueOf(tmp1144_1142.intValue() + 1);String zcat = StringUtils.nvl(StringUtils.valueOf(((Object[])lint.get(i))[tmp1144_1142.intValue()]), ""); Integer
//          tmp1183_1181 = index;index = Integer.valueOf(tmp1183_1181.intValue() + 1);String ccpt = StringUtils.nvl(StringUtils.valueOf(((Object[])lint.get(i))[tmp1183_1181.intValue()]), ""); Integer
//          tmp1222_1220 = index;index = Integer.valueOf(tmp1222_1220.intValue() + 1);String bqag = StringUtils.nvl(StringUtils.valueOf(((Object[])lint.get(i))[tmp1222_1220.intValue()]), ""); Integer
//          tmp1261_1259 = index;index = Integer.valueOf(tmp1261_1259.intValue() + 1);String guic = StringUtils.nvl(StringUtils.valueOf(((Object[])lint.get(i))[tmp1261_1259.intValue()]), ""); Integer
//          tmp1300_1298 = index;index = Integer.valueOf(tmp1300_1298.intValue() + 1);String comp = StringUtils.nvl(StringUtils.valueOf(((Object[])lint.get(i))[tmp1300_1298.intValue()]), ""); Integer
//          tmp1339_1337 = index;index = Integer.valueOf(tmp1339_1337.intValue() + 1);String cle = StringUtils.nvl(StringUtils.valueOf(((Object[])lint.get(i))[tmp1339_1337.intValue()]), "");
//        if (StringUtils.equals("AG", StringUtils.left(ncpt, 2))) {
//          ncpt = zcat + StringUtil.oraSubstring(ncpt, 3, ncpt.length() - 2);
//        } else if (StringUtils.equals("4200M", StringUtils.left(ncpt, 5))) {
//          ncpt = ccpt;
//        } else if (StringUtils.equals("RC", StringUtils.left(ncpt, 2))) {
//          ncpt = bqag + guic + comp + cle;
//        }
//        String cpta = StringUtil.oraSubstring(ncpt, 9, 8);
//        ncpt = StringUtil.oraSubstring(ncpt, 1, 8);
//
//        message = nste + separateur;
//        message = message + cjou + separateur;
//        message = message + new ClsDate(dtcp).getDateS("yyyyMMdd") + separateur;
//        message = message + ncpt + separateur;
//        message = message + refl + separateur;
//        message = message + nsec + separateur;
//        message = message + cpta + separateur;
//        message = message + ncpe + separateur;
//        message = message + new ClsDate(dtsa).getDateS("yyyyMMdd") + separateur;
//        message = message + liba + separateur;
//        message = message + libe + separateur;
//        message = message + cptc + separateur;
//        message = message + sens + separateur;
//        message = message + mtdd + separateur;
//        message = message + cour + separateur;
//        message = message + mtdc + separateur;
//        message = message + cdev + separateur;
//        message = message + cuti + separateur;
//        message = message + dtec + separateur;
//        message = message + new ClsDate(dtvl).getDateS("yyyyMMdd") + separateur;
//        message = message + mpnr + separateur;
//        message = message + stat + separateur;
//        message = message + cenr + separateur;
//        message = message + dfac + separateur;
//
//        numero++;
//        text = new Rhttext();
//        text.setComp_id(new RhttextPK(this.session, Integer.valueOf(numero)));
//        text.setCdos(this.dossier);
//        text.setTexte(message);
//
//        oSession.save(text);
//        if (i % 20 == 0)
//        {
//          oSession.flush();
//          oSession.clear();
//        }
//      }
//      tx.commit();
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//      if (tx != null) {
//        tx.rollback();
//      }
//      this.errmess1 = ClsTreater._getStackTrace(e);
//      writeLog(this.errmess1);
//      this.pb = true;
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
//      return;
//    }
//    finally
//    {
//      this.service.closeConnexion(oSession);
//    }
//  }
//
//  public void generateOGL_Rubrique_CompteBancaire(HttpServletRequest request, String rubriqueAComptabilise)
//  {
//    Rhfnom nomenc = null;
//    String separateur = "";
//    Object o = this.service.get(Rhfnom.class, new RhfnomPK(this.dossier, Integer.valueOf(99), "PAIE-INTER", Integer.valueOf(5)));
//    if (o != null)
//    {
//      nomenc = (Rhfnom)o;
//      if (!ClsObjectUtil.isNull(nomenc.getVall())) {
//        separateur = nomenc.getVall().trim();
//      }
//    }
//    else
//    {
//      this.errmess1 = "Erreur : Creez PAIE-INTER, Lib5 en Tb99.";
//      writeLog(this.errmess1);
//      this.pb = true;
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
//      return;
//    }
//    if (ClsObjectUtil.isNull(separateur))
//    {
//      this.errmess1 = "Renseignez Lib5 de PAIE-INTER en Tb99.";
//      writeLog(this.errmess1);
//      this.pb = true;
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
//      return;
//    }
//    String rubQuinz = rubriqueAComptabilise;
//    if (ClsObjectUtil.isNull(separateur))
//    {
//      this.errmess1 = "Renseignez Lib5 de PAIE-INTER en Tb99.";
//      writeLog(this.errmess1);
//      this.pb = true;
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
//      return;
//    }
//    int nddd = 0;
//    List l = this.service.find("select nddd from Cpdo where idEntreprise = '" + this.dossier + "'");
//    if ((l != null) && (l.size() > 0))
//    {
//      if (l.get(0) != null) {
//        nddd = new Integer(l.get(0).toString()).intValue();
//      }
//    }
//    else
//    {
//      this.errmess1 = this.globalUpdate.getParameter().errorMessage("ERR-30060", this.langue, new Object[0]);
//      writeLog(this.errmess1);
//      this.pb = true;
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
//      return;
//    }
//    int numero = _getCurrentDBMaxRhttext(this.service, this.session).intValue();
//
//    String numpiece = new ClsDate(this.globalUpdate.getPeriode(), "yyyyMM").getDateS("MMyyyy") + this.globalUpdate.getNumerobulletin();
//
//
//    String query = " select codjou,datcpt,numcpt,reflet,coddes1,numpce,libecr,sens,pce_mt,devpce,coduti,b.afec, b.ccpt,c.bqag,c.guic,c.comp,c.cle ";
//    query = query + " from cp_int a ";
//    query = query + " left join rhpagent b on a.cdos=b.cdos and a.coddes8=b.nmat ";
//    query = query + " left join rhtvrmtagent c on a.cdos=c.cdos and a.coddes8=c.nmat ";
//    query = query + " where a.cdos='" + this.dossier + "' and numpce='" + numpiece + "' ";
//    query = query + " and a.coddes9 = '" + rubQuinz + "' ";
//    query = query + " order by numcpt ";
//
//
//
//    String requete = "DELETE FROM Rhttext WHERE idEntreprise = '" + this.dossier + "' and sess='" + this.session + "'";
//    Session oSession = null;
//    Transaction tx = null;
//    try
//    {
//      oSession = this.service.getSession();
//      tx = oSession.beginTransaction();
//      oSession.createQuery(requete).executeUpdate();
//
//      List<Object[]> lint = oSession.createSQLQuery(query).list();
//
//      String message = "";
//      Rhttext text = null;
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//      Integer index = Integer.valueOf(0);
//      for (int i = 0; i < lint.size(); i++)
//      {
//        index = Integer.valueOf(0);
//        String nste = this.dossier; Integer
//          tmp716_714 = index;index = Integer.valueOf(tmp716_714.intValue() + 1);String cjou = StringUtils.nvl(StringUtils.valueOf(((Object[])lint.get(i))[tmp716_714.intValue()]), ""); Integer
//          tmp755_753 = index;index = Integer.valueOf(tmp755_753.intValue() + 1);Date dtcp = (Date)((Object[])lint.get(i))[tmp755_753.intValue()]; Integer
//          tmp789_787 = index;index = Integer.valueOf(tmp789_787.intValue() + 1);String ncpt = StringUtils.nvl(StringUtils.valueOf(((Object[])lint.get(i))[tmp789_787.intValue()]), ""); Integer
//          tmp828_826 = index;index = Integer.valueOf(tmp828_826.intValue() + 1);String refl = StringUtils.nvl(StringUtils.valueOf(((Object[])lint.get(i))[tmp828_826.intValue()]), ""); Integer
//          tmp867_865 = index;index = Integer.valueOf(tmp867_865.intValue() + 1);String nsec = StringUtils.nvl(StringUtils.valueOf(((Object[])lint.get(i))[tmp867_865.intValue()]), ""); Integer
//          tmp906_904 = index;index = Integer.valueOf(tmp906_904.intValue() + 1);String ncpe = StringUtils.nvl(StringUtils.valueOf(((Object[])lint.get(i))[tmp906_904.intValue()]), "");
//        Date dtsa = new Date();
//        String liba = ""; Integer
//          tmp958_956 = index;index = Integer.valueOf(tmp958_956.intValue() + 1);String libe = StringUtils.nvl(StringUtils.valueOf(((Object[])lint.get(i))[tmp958_956.intValue()]), "");
//        String cptc = ""; Integer
//          tmp1001_999 = index;index = Integer.valueOf(tmp1001_999.intValue() + 1);String sens = StringUtils.nvl(StringUtils.valueOf(((Object[])lint.get(i))[tmp1001_999.intValue()]), ""); Integer
//          tmp1044_1042 = index;index = Integer.valueOf(tmp1044_1042.intValue() + 1);BigDecimal mtdd = new BigDecimal(StringUtils.nvl(StringUtils.valueOf(((Object[])lint.get(i))[tmp1044_1042.intValue()]), "0")).setScale(nddd);
//        String cour = "1";
//        BigDecimal mtdc = mtdd; Integer
//          tmp1101_1099 = index;index = Integer.valueOf(tmp1101_1099.intValue() + 1);String cdev = StringUtils.nvl(StringUtils.valueOf(((Object[])lint.get(i))[tmp1101_1099.intValue()]), ""); Integer
//          tmp1140_1138 = index;index = Integer.valueOf(tmp1140_1138.intValue() + 1);String cuti = StringUtils.nvl(StringUtils.valueOf(((Object[])lint.get(i))[tmp1140_1138.intValue()]), "");
//        String dtec = "";
//        Date dtvl = new Date();
//        String mpnr = "";
//        String stat = this.globalUpdate.getNumerobulletin()+"";
//        String cenr = "";
//        String dfac = ""; Integer
//
//          tmp1226_1224 = index;index = Integer.valueOf(tmp1226_1224.intValue() + 1);String zcat = StringUtils.nvl(StringUtils.valueOf(((Object[])lint.get(i))[tmp1226_1224.intValue()]), ""); Integer
//          tmp1265_1263 = index;index = Integer.valueOf(tmp1265_1263.intValue() + 1);String ccpt = StringUtils.nvl(StringUtils.valueOf(((Object[])lint.get(i))[tmp1265_1263.intValue()]), ""); Integer
//          tmp1304_1302 = index;index = Integer.valueOf(tmp1304_1302.intValue() + 1);String bqag = StringUtils.nvl(StringUtils.valueOf(((Object[])lint.get(i))[tmp1304_1302.intValue()]), ""); Integer
//          tmp1343_1341 = index;index = Integer.valueOf(tmp1343_1341.intValue() + 1);String guic = StringUtils.nvl(StringUtils.valueOf(((Object[])lint.get(i))[tmp1343_1341.intValue()]), ""); Integer
//          tmp1382_1380 = index;index = Integer.valueOf(tmp1382_1380.intValue() + 1);String comp = StringUtils.nvl(StringUtils.valueOf(((Object[])lint.get(i))[tmp1382_1380.intValue()]), ""); Integer
//          tmp1421_1419 = index;index = Integer.valueOf(tmp1421_1419.intValue() + 1);String cle = StringUtils.nvl(StringUtils.valueOf(((Object[])lint.get(i))[tmp1421_1419.intValue()]), "");
//        if (StringUtils.equals("AG", StringUtils.left(ncpt, 2))) {
//          ncpt = zcat + StringUtil.oraSubstring(ncpt, 3, ncpt.length() - 2);
//        } else if (StringUtils.equals("4200M", StringUtils.left(ncpt, 5))) {
//          ncpt = ccpt;
//        } else if (StringUtils.equals("RC", StringUtils.left(ncpt, 2))) {
//          ncpt = bqag + guic + comp + cle;
//        }
//        String cpta = StringUtil.oraSubstring(ncpt, 9, 8);
//        ncpt = StringUtil.oraSubstring(ncpt, 1, 8);
//
//
//        message = nste + separateur;
//        message = message + cjou + separateur;
//        message = message + new ClsDate(dtcp).getDateS("yyyyMMdd") + separateur;
//        message = message + ncpt + separateur;
//        message = message + refl + separateur;
//        message = message + nsec + separateur;
//        message = message + cpta + separateur;
//        message = message + ncpe + separateur;
//        message = message + dtsa + separateur;
//        message = message + liba + separateur;
//        message = message + libe + separateur;
//        message = message + cptc + separateur;
//        message = message + "D" + separateur;
//        message = message + mtdd + separateur;
//        message = message + cour + separateur;
//        message = message + mtdc + separateur;
//        message = message + cdev + separateur;
//        message = message + cuti + separateur;
//        message = message + dtec + separateur;
//        message = message + new ClsDate(dtvl).getDateS("yyyyMMdd") + separateur;
//        message = message + mpnr + separateur;
//        message = message + stat + separateur;
//        message = message + cenr + separateur;
//        message = message + dfac + separateur;
//
//        numero++;
//        text = new Rhttext();
//        text.setComp_id(new RhttextPK(this.session, Integer.valueOf(numero)));
//        text.setCdos(this.dossier);
//        text.setTexte(message);
//
//        oSession.save(text);
//
//
//
//
//        ncpt = bqag + guic + comp + cle;
//        cpta = StringUtil.oraSubstring(ncpt, 9, 8);
//        ncpt = StringUtil.oraSubstring(ncpt, 1, 8);
//
//        message = nste + separateur;
//        message = message + cjou + separateur;
//        message = message + new ClsDate(dtcp).getDateS("yyyyMMdd") + separateur;
//        message = message + ncpt + separateur;
//        message = message + refl + separateur;
//        message = message + nsec + separateur;
//        message = message + cpta + separateur;
//        message = message + ncpe + separateur;
//        message = message + dtsa + separateur;
//        message = message + liba + separateur;
//        message = message + libe + separateur;
//        message = message + cptc + separateur;
//        message = message + "C" + separateur;
//        message = message + mtdd + separateur;
//        message = message + cour + separateur;
//        message = message + mtdc + separateur;
//        message = message + cdev + separateur;
//        message = message + cuti + separateur;
//        message = message + dtec + separateur;
//        message = message + new ClsDate(dtvl).getDateS("yyyyMMdd") + separateur;
//        message = message + mpnr + separateur;
//        message = message + stat + separateur;
//        message = message + cenr + separateur;
//        message = message + dfac + separateur;
//
//        numero++;
//        text = new Rhttext();
//        text.setComp_id(new RhttextPK(this.session, Integer.valueOf(numero)));
//        text.setCdos(this.dossier);
//        text.setTexte(message);
//
//        oSession.save(text);
//        if (i % 20 == 0)
//        {
//          oSession.flush();
//          oSession.clear();
//        }
//      }
//      tx.commit();
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//      if (tx != null) {
//        tx.rollback();
//      }
//      this.errmess1 = ClsTreater._getStackTrace(e);
//      writeLog(this.errmess1);
//      this.pb = true;
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
//      return;
//    }
//    finally
//    {
//      this.service.closeConnexion(oSession);
//    }
//  }
//
//
//	public void generateOGL_Rubrique_CompteFixe(HttpServletRequest request, String rubriqueAComptabilise, String compteContrepartie)
//	{
//		Rhfnom nomenc = null;
//		String separateur = "";
//		Object o = service.get(Rhfnom.class, new RhfnomPK(dossier, 99, "PAIE-INTER", 5));
//		if (o != null)
//		{
//			nomenc = (Rhfnom) o;
//			if (!ClsObjectUtil.isNull(nomenc.getVall()))
//				separateur = nomenc.getVall().trim();
//		}
//		else
//		{
//			errmess1 = "Erreur : Creez PAIE-INTER, Lib5 en Tb99.";
//			writeLog(errmess1);
//
//			globalUpdate._setEvolutionTraitement(request, errmess1, true);
//			return;
//		}
//
//		if (ClsObjectUtil.isNull(separateur))
//		{
//			errmess1 = "Renseignez Lib5 de PAIE-INTER en Tb99.";
//			writeLog(errmess1);
//
//			globalUpdate._setEvolutionTraitement(request, errmess1, true);
//			return;
//		}
//
//
//
//		if (ClsObjectUtil.isNull(separateur))
//		{
//			errmess1 = "Renseignez Lib5 de PAIE-INTER en Tb99.";
//			writeLog(errmess1);
//
//			globalUpdate._setEvolutionTraitement(request, errmess1, true);
//			return;
//		}
//
//
//		int numero = 0;
//
//		String numpiece = new ClsDate(this.globalUpdate.getPeriode(), "yyyyMM").getDateS("MMyyyy") + this.globalUpdate.getNumerobulletin();
//
//		String query = "Select sum(pce_mt) as pce_mt,sens,numcpt,numtie,datpce,devpce,coddes4,b.vall as vall1 LIBECR  From Cp_int a, Rhfnom b WHERE a.cdos='" + dossier + "' and numpce='" + numpiece + "'";
//		query += " and a.cdos=b.cdos and b.ctab=1 and b.nume=3 and b.cacc=a.coddes4 ";
//		query+=" and a.coddes8 = '"+rubriqueAComptabilise+"' ";
//		query += " Group by sens,numcpt,numtie,datpce,devpce,coddes4,b.vall LIBECR ";
//		query += " Order by coddes4,numcpt,numtie";
//
//		String requete = "DELETE FROM Rhttext WHERE idEntreprise = '" + dossier + "' and sess='" + session + "'";
//		Session oSession = null;
//		Transaction tx = null;
//		try
//		{
//			oSession = service.getSession();
//			tx = oSession.beginTransaction();
//			oSession.createQuery(requete).executeUpdate();
//
//			// Chargement du param�trage Table 99, cl� GESCOM
//			List<Rhfnom> gescoms = service.find("From Rhfnom where comp_id.cdos='" + dossier + "' and comp_id.ctab=99 and comp_id.cacc='GESCOM' order by comp_id.nume");
//			if (gescoms.isEmpty())
//			{
//				errmess1 = "Creez GESCOM en Tb99.";
//				writeLog(errmess1);
//				globalUpdate._setEvolutionTraitement(request, errmess1, true);
//				return;
//			}
//
//			Long uneEcritureParCompteEtTiers = Long.valueOf(0);
//			// Chargement du param�trage Table 99, cl� PAR-CENTRA
//			Rhfnom fn = (Rhfnom) service.get(Rhfnom.class, new RhfnomPK(dossier, 99, "PAR-CENTRA", 2));
//			if (fn != null && fn.getValm() != null)
//				uneEcritureParCompteEtTiers = fn.getValm();
//
//			String cart = StringUtils.EMPTY;
//			Long csoc = null;
//			Long orig = null;
//
//			String facture = StringUtils.EMPTY;
//			String ndossier = StringUtils.EMPTY;
//			String datefacture = StringUtils.EMPTY;
//			Integer lsouscompte = 6;
//			Integer lmontant = 9;
//			String charCompletionSCompte = "0";
//			String charCompletionMontant = "0";
//
//			for (Rhfnom fnom : gescoms)
//			{
//				if (fnom.getComp_id().getNume() == 2)
//					cart = fnom.getVall();
//
//				if (fnom.getComp_id().getNume() == 3)
//				{
//					csoc = fnom.getValm();
//					facture = this.nvlEmpty(fnom.getVall(), StringUtils.EMPTY);
//				}
//
//				if (fnom.getComp_id().getNume() == 4)
//				{
//					orig = fnom.getValm();
//					ndossier = this.nvlEmpty(fnom.getVall(), StringUtils.EMPTY);
//				}
//
//				if (fnom.getComp_id().getNume() == 5)
//				{
//					if (fnom.getValm() != null)
//						lsouscompte = fnom.getValm().intValue();
//					datefacture = this.nvlEmpty(fnom.getVall(), StringUtils.EMPTY);
//				}
//
//				if (fnom.getComp_id().getNume() == 6)
//				{
//					if (fnom.getValm() != null)
//						lmontant = fnom.getValm().intValue();
//				}
//
//				if (fnom.getComp_id().getNume() == 7)
//				{
//					if (fnom.getValm() != null)
//						charCompletionSCompte = StringUtils.valueOf(fnom.getValm().intValue());
//				}
//
//				if (fnom.getComp_id().getNume() == 8)
//				{
//					if (fnom.getValm() != null)
//						charCompletionMontant = StringUtils.valueOf(fnom.getValm().intValue());
//				}
//			}
//
//			if (StringUtils.isBlank(cart))
//			{
//				errmess1 = "Renseignez Lib2 de GESCOM en Tb99.";
//				writeLog(errmess1);
//				globalUpdate._setEvolutionTraitement(request, errmess1, true);
//				return;
//			}
//
//			if (csoc == null)
//			{
//				errmess1 = "Renseignez Mnt3 de GESCOM en Tb99.";
//				writeLog(errmess1);
//				globalUpdate._setEvolutionTraitement(request, errmess1, true);
//				return;
//			}
//
//			if (orig == null)
//			{
//				errmess1 = "Renseignez Mnt4 de GESCOM en Tb99.";
//				writeLog(errmess1);
//				globalUpdate._setEvolutionTraitement(request, errmess1, true);
//				return;
//			}
//
////			if (uneEcritureParCompteEtTiers == 1)
////				query = query.replaceAll("LIBECR", StringUtils.EMPTY);
////			else
//				query = query.replaceAll("LIBECR", ",libecr");
//
//			SQLQuery q = oSession.createSQLQuery(query);
//			List<Object[]> lint = q.list();
//
//			String message = "";
//			Rhttext text = null;
//			String sens;
//			String numcpt;
//			String numtie;
//			BigDecimal pce_mt;
//			String strPceMt;
//			String libecr = null;
//			Date datpce = null;
//			String devise = null;
//			String niv1;
//			String vallniv1;
//
//			String chainereplicationMontant = StringUtils.EMPTY;
//			Map<String, List<String>> etablissements = new HashMap<String, List<String>>();
//			Map<String, BigDecimal> etablissementsMnt = new HashMap<String, BigDecimal>();
//			Map<String, String> libelles = new HashMap<String, String>();
//
//			for (int i = 0; i < lint.size(); i++)
//			{
//				pce_mt = new BigDecimal(StringUtils.valueOf(lint.get(i)[0]));
//				sens = StringUtils.valueOf(lint.get(i)[1]);
//				numcpt = StringUtils.valueOf(lint.get(i)[2]);
//				numtie = StringUtils.valueOf(lint.get(i)[3]);
//				datpce = (Date) lint.get(i)[4];
//				devise = StringUtils.valueOf(lint.get(i)[5]);
//				niv1 = StringUtils.valueOf(lint.get(i)[6]);
//				vallniv1 = StringUtils.valueOf(lint.get(i)[7]);
////				if (uneEcritureParCompteEtTiers == 1)
////				{
//					// libecr = StringUtils.EMPTY;
//					libecr = StringUtils.nvl(StringUtils.valueOf(lint.get(i)[8]), StringUtils.EMPTY)+" " + vallniv1 + " " + ClsStringUtil.formatNumber(new ClsDate(datpce).getMonth(), "00");
//					libecr += "/" + new ClsDate(datpce).getDateS("yy") + " PAIE" + niv1;
////				}
////				else
////					libecr = StringUtils.nvl(StringUtils.valueOf(lint.get(i)[8]), StringUtils.EMPTY);
//
//				numtie = StringUtils.nvl(numtie, StringUtils.EMPTY);
//				if(numcpt.startsWith("420") && StringUtils.isNotBlank(numtie))
//					numtie = ClsStringUtil.oraLPad(numtie, 6,"0");
//				if (StringUtils.notEquals("2", charCompletionSCompte))
//				{
//					if (StringUtils.isBlank(numtie))
//					{
//						for (int ii = 0; ii < lsouscompte; ii++)
//							numtie += StringUtils.equals("0", charCompletionSCompte) ? "0" : " ";
//					}
//					else
//						numtie = ClsStringUtil.oraLPad(numtie, lsouscompte, StringUtils.equals("0", charCompletionSCompte) ? "0" : " ");
//				}
//
//				pce_mt = pce_mt.setScale(nddd);
//				strPceMt = pce_mt + "";
//
//				if (StringUtils.notEquals("2", charCompletionMontant))
//				{
//					chainereplicationMontant = StringUtils.equals("0", charCompletionMontant) ? "0" : " ";
//					if (StringUtils.equals("0", charCompletionMontant) && lmontant - strPceMt.length() > 0)
//						strPceMt = ClsStringUtil.formatNumber(pce_mt, this.replicate(chainereplicationMontant, lmontant));
//					else
//						strPceMt = this.replicate(chainereplicationMontant, lmontant - strPceMt.length()) + strPceMt;
//
//				}
//
//				strPceMt = StringUtils.replace(strPceMt, ".", ",");
//
//				message = new ClsDate(datpce).getDateS("dd/MM/yyyy") + separateur; // --DATE
//				message += csoc + separateur; // --ORIGINE GESCOM, MNT3
//				message += this.globalUpdate.getPeriode() + this.globalUpdate.getNumerobulletin() + separateur; // --NUMPIE NUMERO DE PIECE AAAAMMN
//				message += numcpt + separateur; // --NUMCPT NUMERO DE COMPTE
//				message += facture + separateur; // --NFACT NUMERO DE FACTURE VIDE
//				message += ndossier + separateur; // --DOSS DOSSIER
//				message += numtie + separateur;
//				message += cart + separateur; // --CART
//				message += (StringUtils.equals("C", sens) ? strPceMt : "" ) + separateur; // --SENS 1=DEBIT,2=CREDIT
//				message += (StringUtils.equals("C", sens) ? "" : "-" + strPceMt) + separateur; // --SENS 1=DEBIT,2=CREDIT
//				message += devise + separateur;
//				message += libecr + separateur;
//
//				if(!etablissements.containsKey(niv1))
//					etablissements.put(niv1, new ArrayList<String>());
//
//				etablissements.get(niv1).add(message);
//
//				if(!etablissementsMnt.containsKey(niv1))
//					etablissementsMnt.put(niv1, BigDecimal.ZERO);
//
//				etablissementsMnt.put(niv1,etablissementsMnt.get(niv1).add(pce_mt));
//				libelles.put(niv1, libecr);
//
//				numero += 1;
//				text = new Rhttext();
//				text.setComp_id(new RhttextPK(session, numero));
//				text.setCdos(dossier);
//				text.setTexte(message);
//				//
//				oSession.save(text);
//				if (i % 20 == 0)
//				{
//					oSession.flush();
//					oSession.clear();
//				}
//
//			}
//
//
//			Iterator<String> iter = etablissements.keySet().iterator();
//			List<String> texte = new ArrayList<String>();
//			String txt = StringUtils.EMPTY;
//
//			List<ClsTemplate> listeFichiers = new ArrayList<ClsTemplate>();
//			String nomFichierEtablissement;
//			while(iter.hasNext())
//			{
//				niv1 = iter.next();
//
//
//				//Ajout de la contrepartie : 422200
//				numcpt = compteContrepartie;
//				numtie = StringUtils.EMPTY;
//				message = new ClsDate(datpce).getDateS("dd/MM/yyyy") + separateur; // --DATE
//				message += csoc + separateur; // --ORIGINE GESCOM, MNT3
//				message += this.globalUpdate.getPeriode() + this.globalUpdate.getNumerobulletin() + separateur; // --NUMPIE NUMERO DE PIECE AAAAMMN
//				message += numcpt + separateur; // --NUMCPT NUMERO DE COMPTE
//				message += facture + separateur; // --NFACT NUMERO DE FACTURE VIDE
//				message += ndossier + separateur; // --DOSS DOSSIER
//				message += numtie + separateur;
//				message += cart + separateur; // --CART
//				message +=  "" + separateur; // --SENS 1=DEBIT,2=CREDIT
//				message +=  "-" + etablissementsMnt.get(niv1) + separateur; // --SENS 1=DEBIT,2=CREDIT
//				message += devise + separateur;
//				message += libelles.get(niv1) + separateur;
//
//				txt = message;
//				texte = etablissements.get(niv1);
//				for(String str : texte)
//				{
//					if(StringUtils.isBlank(txt))
//						txt = str;
//					else
//						txt+="\n"+str;
//				}
//
//
//				if(StringUtils.isNotBlank(txt))
//				{
//					nomFichierEtablissement = StringUtils.replace(globalUpdate.getNomasciifile(),"XX",niv1);
//					StringUtils.printOutObject(txt, nomFichierEtablissement, true);
//					listeFichiers.add(new ClsTemplate(nomFichierEtablissement,new File(nomFichierEtablissement).getName()));
//				}
//			}
//			//G�n�ration d'un zip pour l'ensemble des fichiers
//			try
//			{
//				String nomFichierZip =globalUpdate.getNomasciifile().substring(0, globalUpdate.getNomasciifile().length()-4) + ".zip";
//				ClsZipFileService clsZipFileService = new ClsZipFile(nomFichierZip);
//				clsZipFileService.__processFiles(listeFichiers);
//				globalUpdate.setNomasciifile(nomFichierZip);
//			}
//			catch (Exception e)
//			{
//				e.printStackTrace();
//			}
//
//			tx.commit();
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//			if (tx != null)
//				tx.rollback();
//			errmess1 = ClsTreater._getStackTrace(e);
//			writeLog(errmess1);
//
//			globalUpdate._setEvolutionTraitement(request, errmess1, true);
//			return;
//		}
//		finally
//		{
//			service.closeConnexion(oSession);
//		}
//	}
//
//
//  public void generateOGLLawson(HttpServletRequest request)
//  {
//    Rhfnom nomenc = null;
//    String separateur = "";
//    Object o = this.service.get(Rhfnom.class, new RhfnomPK(this.dossier, Integer.valueOf(99), "PAIE-INTER", Integer.valueOf(5)));
//    if (o != null)
//    {
//      nomenc = (Rhfnom)o;
//      if (!ClsObjectUtil.isNull(nomenc.getVall())) {
//        separateur = nomenc.getVall().trim();
//      }
//    }
//    else
//    {
//      this.errmess1 = "Erreur : Creez PAIE-INTER, Lib5 en Tb99.";
//      writeLog(this.errmess1);
//
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
//      return;
//    }
//    if (ClsObjectUtil.isNull(separateur))
//    {
//      this.errmess1 = "Renseignez Lib5 de PAIE-INTER en Tb99.";
//      writeLog(this.errmess1);
//
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
//      return;
//    }
//    separateur = "";
//
//    int numero = 0;
//
//    String numpiece = new ClsDate(this.globalUpdate.getPeriode(), "yyyyMM").getDateS("MMyyyy") + this.globalUpdate.getNumerobulletin();
//
//    String query = "";
//
//    query = " select cper,numcpt,coddes8 as nmat,coddes2,datcpt,codjou,codets,sens,c.lrub as libecr,'' as tiers,sum(pce_mt)";
//    query = query + " from cp_int a ";
//    query = query + " join rhpagent b on a.cdos=b.cdos and a.coddes8=b.nmat ";
//    query = query + " join  rhprubrique c on a.cdos=c.cdos and a.coddes9=c.crub ";
//    query = query + " where a.cdos='" + this.dossier + "' and numpce='" + numpiece + "' ";
//    query = query + " and c.cper='O' and c.algo not in (13,17,20) ";
//    query = query + " group by cper,numcpt,coddes8,coddes2,datcpt,codjou,codets,sens,lrub ";
//
//    query = query + " union all ";
//
//    query = query + " select cper,numcpt,coddes8 as nmat,coddes2,datcpt,codjou,codets,sens,c.lrub as libecr,'' as tiers,pce_mt";
//    query = query + " from cp_int a ";
//    query = query + " join rhpagent b on a.cdos=b.cdos and a.coddes8=b.nmat ";
//    query = query + " join  rhprubrique c on a.cdos=c.cdos and a.coddes9=c.crub ";
//    query = query + " where a.cdos='" + this.dossier + "' and numpce='" + numpiece + "' ";
//    query = query + " and c.cper='O' and c.algo in (13,17,20)  ";
//
//    query = query + " union all ";
//
//    query = query + " select cper,numcpt,'' as nmat,coddes2,datcpt,codjou,codets,sens,c.lrub as libecr,d.vall as tiers,sum(pce_mt) ";
//    query = query + " from cp_int a ";
//    query = query + " join rhpagent b on a.cdos=b.cdos and a.coddes8=b.nmat ";
//    query = query + " join  rhprubrique c on a.cdos=c.cdos and a.coddes9=c.crub ";
//    query = query + " join rhfnom d on a.cdos=d.cdos and d.ctab=2 and d.nume=5 and d.cacc=b.niv2 ";
//    query = query + " where a.cdos='" + this.dossier + "' and numpce='" + numpiece + "' and numcpt like '6%' ";
//    query = query + " and c.cper='N' ";
//    query = query + " group by cper,numcpt,coddes2,datcpt,codjou,codets,sens,lrub,d.vall ";
//
//    query = query + " union all ";
//
//    query = query + " select cper,numcpt,'' as nmat,'' as coddes2,datcpt,codjou,codets,sens,c.lrub as libecr,'' as tiers,sum(pce_mt) ";
//    query = query + " from cp_int a ";
//    query = query + " join  rhprubrique c on a.cdos=c.cdos and a.coddes9=c.crub ";
//    query = query + " where a.cdos='" + this.dossier + "' and numpce='" + numpiece + "' and numcpt not like '6%' ";
//    query = query + " and c.cper='N' ";
//    query = query + " group by cper,numcpt,datcpt,codjou,codets,sens,lrub ";
//
//
//
//
//
//    String requete = "DELETE FROM Rhttext WHERE idEntreprise = '" + this.dossier + "' and sess='" + this.session + "'";
//    Session oSession = null;
//    Transaction tx = null;
//    try
//    {
//      oSession = this.service.getSession();
//      tx = oSession.beginTransaction();
//      oSession.createQuery(requete).executeUpdate();
//
//      List<Object[]> lint = oSession.createSQLQuery(query).list();
//
//      String message = "";
//      Rhttext text = null;
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//      Integer index = Integer.valueOf(0);
//
//
//      Integer nfac = Integer.valueOf(0);
//      for (int i = 0; i < lint.size(); i++)
//      {
//        index = Integer.valueOf(0);
//        Object[] obj = (Object[])lint.get(i);
//
//        String cper = "";
//        String numcpt = "";
//        String nmat = "";
//        String codesoc = "";
//        Date datcpt = new Date();
//        String codjou = "";
//        String codets = "";
//        String sens = "";
//        String libecr = "";
//        String tiers = "";
//        BigDecimal pce_mt = BigDecimal.ZERO;
//        if (obj[index.intValue()] != null)
//        {
//          Integer tmp1166_1164 = index;index = Integer.valueOf(tmp1166_1164.intValue() + 1);cper = StringUtils.nvl(obj[tmp1166_1164.intValue()].toString(), "N");
//        }
//        else
//        {
//          index = Integer.valueOf(index.intValue() + 1);
//        }
//        if (obj[index.intValue()] != null)
//        {
//          Integer tmp1222_1220 = index;index = Integer.valueOf(tmp1222_1220.intValue() + 1);numcpt = obj[tmp1222_1220.intValue()].toString();
//        }
//        else
//        {
//          index = Integer.valueOf(index.intValue() + 1);
//        }
//        if (obj[index.intValue()] != null)
//        {
//          Integer tmp1272_1270 = index;index = Integer.valueOf(tmp1272_1270.intValue() + 1);nmat = obj[tmp1272_1270.intValue()].toString();
//        }
//        else
//        {
//          index = Integer.valueOf(index.intValue() + 1);
//        }
//        if (obj[index.intValue()] != null)
//        {
//          Integer tmp1322_1320 = index;index = Integer.valueOf(tmp1322_1320.intValue() + 1);codesoc = obj[tmp1322_1320.intValue()].toString();
//        }
//        else
//        {
//          index = Integer.valueOf(index.intValue() + 1);
//        }
//        if (obj[index.intValue()] != null)
//        {
//          Integer tmp1372_1370 = index;index = Integer.valueOf(tmp1372_1370.intValue() + 1);datcpt = (Date)obj[tmp1372_1370.intValue()];
//        }
//        else
//        {
//          index = Integer.valueOf(index.intValue() + 1);
//        }
//        if (obj[index.intValue()] != null)
//        {
//          Integer tmp1422_1420 = index;index = Integer.valueOf(tmp1422_1420.intValue() + 1);codjou = obj[tmp1422_1420.intValue()].toString();
//        }
//        else
//        {
//          index = Integer.valueOf(index.intValue() + 1);
//        }
//        if (obj[index.intValue()] != null)
//        {
//          Integer tmp1472_1470 = index;index = Integer.valueOf(tmp1472_1470.intValue() + 1);codets = obj[tmp1472_1470.intValue()].toString();
//        }
//        else
//        {
//          index = Integer.valueOf(index.intValue() + 1);
//        }
//        if (obj[index.intValue()] != null)
//        {
//          Integer tmp1522_1520 = index;index = Integer.valueOf(tmp1522_1520.intValue() + 1);sens = obj[tmp1522_1520.intValue()].toString();
//        }
//        else
//        {
//          index = Integer.valueOf(index.intValue() + 1);
//        }
//        if (obj[index.intValue()] != null)
//        {
//          Integer tmp1572_1570 = index;index = Integer.valueOf(tmp1572_1570.intValue() + 1);libecr = obj[tmp1572_1570.intValue()].toString();
//        }
//        else
//        {
//          index = Integer.valueOf(index.intValue() + 1);
//        }
//        if (obj[index.intValue()] != null)
//        {
//          Integer tmp1622_1620 = index;index = Integer.valueOf(tmp1622_1620.intValue() + 1);tiers = obj[tmp1622_1620.intValue()].toString();
//        }
//        else
//        {
//          index = Integer.valueOf(index.intValue() + 1);
//        }
//        Integer tmp1665_1663 = index;index = Integer.valueOf(tmp1665_1663.intValue() + 1);pce_mt = new BigDecimal(StringUtils.valueOf(obj[tmp1665_1663.intValue()]));
//
//        String aa = StringUtil.oraSubstring(new ClsDate(datcpt).getYear()+"", 3, 2);
//        String mm = ClsStringUtil.formatNumber(Integer.valueOf(new ClsDate(datcpt).getMonth()), "00");
//
//        String id = StringUtils.equals(cper, "O") ? "I1" : "I2";
//
//        String rnno = this.globalUpdate.getPeriode() + "001";
//
//        String grnr = ClsStringUtil.oraLPad(codjou, 3) + aa + mm + "1";
//
//        String divi = ClsStringUtil.oraLPad(codets, 3);
//        String suno;
//        if (StringUtils.equals(id, "I2")) {
//          suno = StringUtils.getEmptyString(10);
//        } else {
//          suno = ClsStringUtil.oraLPad(codesoc, 3) + ClsStringUtil.oraLPad(nmat, 6) + StringUtils.getEmptyString(1);
//        }
//        String sino;
//        if (StringUtils.equals(id, "I2")) {
//          sino = StringUtils.getEmptyString(24);
//        } else {
//          sino = this.globalUpdate.getPeriode() + StringUtils.getEmptyString(1) + ClsStringUtil.formatNumber(nfac = Integer.valueOf(nfac.intValue() + 1), "00000") + StringUtils.getEmptyString(1) + StringUtils.oraRPad("PAIE", 11);
//        }
//        if (pce_mt.compareTo(BigDecimal.ZERO) < 0)
//        {
//          if (StringUtils.equals(sens, "C")) {
//            sens = "D";
//          } else {
//            sens = "C";
//          }
//          pce_mt = pce_mt.multiply(new BigDecimal(-1));
//        }
//        String ivam = pce_mt.setScale(this.nddd.intValue())+"";
//        if (StringUtils.equals(sens, "C")) {
//          ivam = "-" + ivam;
//        }
//        ivam = ClsStringUtil.oraLPad(ivam, 17);
//
//        String acdt = new ClsDate(datcpt).getDateS("yyyyMMdd");
//
//        String ait1 = StringUtils.oraRPad(numcpt, 8);
//
//        String ait2 = StringUtils.getEmptyString(8);
//        if ((StringUtils.equals(id, "I2")) && (StringUtils.equals(StringUtils.left(numcpt, 1), "6"))) {
//          ait2 = StringUtils.oraRPad(StringUtils.nvl(tiers, " "), 8);
//        }
//        String aitx = StringUtils.getEmptyString(8);
//
//        String vtxt = StringUtil.oraSubstring(StringUtils.oraRPad(libecr, 40), 1, 40);
//
//        message = id + separateur + rnno + separateur + grnr + separateur + divi + separateur + suno + separateur + sino + separateur + ivam + separateur + acdt + separateur + ait1 + separateur + ait2 + separateur + aitx + separateur + aitx + separateur + aitx + separateur + aitx + separateur + aitx + separateur + vtxt;
//
//        numero++;
//        text = new Rhttext();
//        text.setComp_id(new RhttextPK(this.session, Integer.valueOf(numero)));
//        text.setCdos(this.dossier);
//        text.setTexte(message);
//
//        oSession.save(text);
//        if (i % 20 == 0)
//        {
//          oSession.flush();
//          oSession.clear();
//        }
//      }
//      tx.commit();
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//      if (tx != null) {
//        tx.rollback();
//      }
//      this.errmess1 = ClsTreater._getStackTrace(e);
//      writeLog(this.errmess1);
//      this.pb = true;
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
//      return;
//    }
//    finally
//    {
//      this.service.closeConnexion(oSession);
//    }
//  }
//
//  public void generateOGLSAGE500(HttpServletRequest request)
//  {
//    String separateur = " ";
//
//
//    int numero = _getCurrentDBMaxRhttext(this.service, this.session).intValue();
//
//    String numpiece = new ClsDate(this.globalUpdate.getPeriode(), "yyyyMM").getDateS("MMyyyy") + this.globalUpdate.getNumerobulletin();
//
//    String query = " select codjou,sens,numcpt,datcpt,datpce, coalesce(b.lrub,a.libecr) as libecr,'' as coddes1, SUM(pce_mt) as pce_mt from cp_int a left join rhprubrique b on  a.coddes9=b.crub and a.cdos=b.cdos ";
//    query = query + "where a.cdos='" + this.dossier + "' and numpce='" + numpiece + "'";
//    query = query + " and numcpt not like '6%' ";
//    query = query + " GROUP BY codjou,sens,numcpt,datcpt,datpce,coalesce(b.lrub,a.libecr) ";
//    query = query + " union all ";
//    query = query + " select codjou,sens,numcpt,datcpt,datpce, coalesce(b.lrub,a.libecr) as libecr,coddes1, SUM(pce_mt) as pce_mt from cp_int a left join rhprubrique b on  a.coddes9=b.crub and a.cdos=b.cdos ";
//    query = query + "where a.cdos='" + this.dossier + "' and numpce='" + numpiece + "'";
//    query = query + " and numcpt like '6%' ";
//    query = query + " GROUP BY codjou,sens,numcpt,datcpt,datpce,coalesce(b.lrub,a.libecr),coddes1 ";
//    query = query + " ORDER BY NUMCPT,coddes1,libecr desc ";
//
//    String requete = "DELETE FROM Rhttext WHERE idEntreprise = '" + this.dossier + "' and sess='" + this.session + "'";
//    Session oSession = null;
//    Transaction tx = null;
//    try
//    {
//      oSession = this.service.getSession();
//      tx = oSession.beginTransaction();
//      oSession.createQuery(requete).executeUpdate();
//
//      List<Object[]> lint = oSession.createSQLQuery(query).list();
//
//      String message = "";
//      Rhttext text = null;
//
//
//
//
//
//
//
//
//
//
//
//      String nvl = " ";
//
//      int index = 0;
//      for (int i = 0; i < lint.size(); i++)
//      {
//        index = 0;
//
//        String codjou = StringUtils.nvl((String)((Object[])lint.get(i))[(index++)], nvl);
//        String sens = StringUtils.nvl((String)((Object[])lint.get(i))[(index++)], nvl);
//        String numcpt = StringUtils.nvl((String)((Object[])lint.get(i))[(index++)], nvl);
//        Date datcpt = (Date)((Object[])lint.get(i))[(index++)];
//        Date datpce = (Date)((Object[])lint.get(i))[(index++)];
//        String libecr = StringUtils.nvl((String)((Object[])lint.get(i))[(index++)], nvl);
//        String coddes1 = StringUtils.nvl((String)((Object[])lint.get(i))[(index++)], nvl);
//        BigDecimal pce_mt = new BigDecimal(StringUtils.valueOf(((Object[])lint.get(i))[(index++)]));
//
//        pce_mt = pce_mt.setScale(3);
//        BigDecimal pce_mtpos = pce_mt.abs();
//
//        message = StringUtil.oraSubstring(StringUtils.oraRPad(codjou, 4), 1, 4) + separateur;
//        message = message + StringUtil.oraSubstring(StringUtils.oraRPad(numcpt, 7), 1, 7) + separateur;
//        message = message + StringUtil.oraSubstring(StringUtils.oraRPad(numpiece, 7), 1, 7) + separateur;
//        message = message + ClsDate.getDateS(datcpt, "yyyyMMdd");
//        message = message + ClsDate.getDateS(datpce, "yyyyMMdd");
//        message = message + StringUtil.oraSubstring(StringUtils.oraRPad(sens, 1), 1, 1);
//        if (pce_mt.signum() > 0) {
//          message = message + StringUtil.oraSubstring(ClsStringUtil.oraLPad(new StringBuilder().append(pce_mt).toString(), 16, "0"), 1, 16);
//        } else {
//          message = message + "-" + StringUtil.oraSubstring(ClsStringUtil.oraLPad(new StringBuilder().append(pce_mt.abs()).toString(), 16, "0"), 1, 16);
//        }
//        message = message + StringUtil.oraSubstring(StringUtils.oraRPad(coddes1, 7), 1, 7) + separateur;
//        message = message + StringUtil.oraSubstring(StringUtils.oraRPad(libecr, 30), 1, 30);
//
//        numero++;
//        text = new Rhttext();
//        text.setComp_id(new RhttextPK(this.session, Integer.valueOf(numero)));
//        text.setCdos(this.dossier);
//        text.setTexte(message);
//
//        oSession.save(text);
//        if (i % 20 == 0)
//        {
//          oSession.flush();
//          oSession.clear();
//        }
//      }
//      tx.commit();
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//      if (tx != null) {
//        tx.rollback();
//      }
//      this.errmess1 = ClsTreater._getStackTrace(e);
//      writeLog(this.errmess1);
//      this.pb = true;
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
//      return;
//    }
//    finally
//    {
//      this.service.closeConnexion(oSession);
//    }
//  }
//
//  public void generateOGL_PERENCO_RDC(HttpServletRequest request)
//  {
//    Rhfnom nome = (Rhfnom)this.service.get(Rhfnom.class, new RhfnomPK(this.dossier, Integer.valueOf(99), "JOURPAIE", Integer.valueOf(1)));
//    System.out.println("==DEBUG-PERENCO==> Entr�e dans generateOGL_PERENCO_RDC");
//
//    String separateur = "";
//    String codejour = "SAL";
//    if (nome != null) {
//      codejour = nome.getVall();
//    }
//    int numero = _getCurrentDBMaxRhttext(this.service, this.session).intValue();
//
//    String numpiece = new ClsDate(this.globalUpdate.getPeriode(), "yyyyMM").getDateS("MMyyyy") + this.globalUpdate.getNumerobulletin();
//
//    String query = " select numcpt,coddes1,datcpt,devpce,sens, datcre, SUM(pce_mt) as montant from cp_int ";
//    query = query + " where cdos='" + this.dossier + "' and numpce='" + numpiece + "'" + " and codjou ='" + codejour + "' ";
//    query = query + " GROUP BY numcpt,coddes1,datcpt,devpce,sens, datcre ";
//
//
//    String requete = "DELETE FROM Rhttext WHERE idEntreprise = '" + this.dossier + "' and sess='" + this.session + "'";
//    Session oSession = null;
//    Transaction tx = null;
//    try
//    {
//      oSession = this.service.getSession();
//      tx = oSession.beginTransaction();
//      oSession.createQuery(requete).executeUpdate();
//
//      System.out.println("==DEBUG-PERENCO==> la requete finale est: " + query);
//
//      List<Object[]> lint = oSession.createSQLQuery(query).list();
//
//      String message = "";
//      Rhttext text = null;
//
//
//
//
//
//
//
//
//
//      String nvl = " ";
//
//      int index = 0;
//      System.out.println("==DEBUG-PERENCO==> le nombre de ligne=" + lint.size());
//      for (int i = 0; i < lint.size(); i++)
//      {
//        index = 0;
//
//        String numcpt = StringUtils.nvl((String)((Object[])lint.get(i))[(index++)], nvl);
//        String coddes1 = StringUtils.nvl((String)((Object[])lint.get(i))[(index++)], nvl);
//        Date datcpt = (Date)((Object[])lint.get(i))[(index++)];
//        String devpce = StringUtils.nvl((String)((Object[])lint.get(i))[(index++)], nvl);
//        String sens = StringUtils.nvl((String)((Object[])lint.get(i))[(index++)], nvl);
//        Date datcre = (Date)((Object[])lint.get(i))[(index++)];
//        BigDecimal montant = new BigDecimal(StringUtils.valueOf(((Object[])lint.get(i))[(index++)]));
//
//        message = StringUtil.oraSubstring(StringUtils.oraRPad(" ", 32), 1, 32) + separateur;
//        message = message + StringUtil.oraSubstring(StringUtils.oraRPad(numcpt, 16), 1, 16) + separateur;
//        message = message + "ZRN" + separateur;
//        message = message + " " + separateur;
//        message = message + StringUtil.oraSubstring(StringUtils.oraRPad(new StringBuilder("SAL").append(ClsDate.getDateS(datcpt, "MMyy")).toString(), 10), 1, 10) + separateur;
//        message = message + StringUtil.oraSubstring(StringUtils.oraRPad(sens, 1), 1, 1) + separateur;
//        message = message + StringUtil.oraSubstring(StringUtils.oraRPad(coddes1, 15), 1, 15) + separateur;
//        message = message + StringUtil.oraSubstring(StringUtils.oraRPad(" ", 22), 1, 22) + separateur;
//        message = message + StringUtil.oraSubstring(StringUtils.oraRPad(new StringBuilder("SALAIRES ").append(ClsDate.getDateS(datcpt, "MM/yyyy")).toString(), 60), 1, 60) + separateur;
//        message = message + StringUtil.oraSubstring(ClsStringUtil.oraLPad(new StringBuilder().append(montant).toString(), 15, "0"), 1, 15);
//
//        numero++;
//        text = new Rhttext();
//        text.setComp_id(new RhttextPK(this.session, Integer.valueOf(numero)));
//        text.setCdos(this.dossier);
//        text.setTexte(message);
//
//        oSession.save(text);
//        if (i % 20 == 0)
//        {
//          oSession.flush();
//          oSession.clear();
//        }
//      }
//      tx.commit();
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//      if (tx != null) {
//        tx.rollback();
//      }
//      this.errmess1 = ClsTreater._getStackTrace(e);
//      writeLog(this.errmess1);
//      this.pb = true;
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
//      return;
//    }
//    finally
//    {
//      this.service.closeConnexion(oSession);
//    }
//  }
//
//  public void generateOGLX3(HttpServletRequest request)
//  {
//    String separateur = "";
//
//    Object o = this.service.get(Rhfnom.class, new RhfnomPK(this.dossier, Integer.valueOf(99), "PAIE-INTER", Integer.valueOf(5)));
//    if (o != null)
//    {
//      Rhfnom nomenc = (Rhfnom)o;
//      if (!ClsObjectUtil.isNull(nomenc.getVall())) {
//        separateur = nomenc.getVall().trim();
//      }
//    }
//    else
//    {
//      this.errmess1 = "Erreur : Creez PAIE-INTER, Lib5 en Tb99.";
//      writeLog(this.errmess1);
//
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true); return;
//    }
//    Rhfnom nomenc;
//    if (ClsObjectUtil.isNull(separateur))
//    {
//      this.errmess1 = "Renseignez Lib5 de PAIE-INTER en Tb99.";
//      writeLog(this.errmess1);
//
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
//      return;
//    }
//    int numero = _getCurrentDBMaxRhttext(this.service, this.session).intValue();
//
//    String numpiece = new ClsDate(this.globalUpdate.getPeriode(), "yyyyMM").getDateS("MMyyyy") + this.globalUpdate.getNumerobulletin();
//
//    String query = " select a.libecr as nom,coddes8,codjou,sens,numcpt,datcpt, '' as coddes1, '' as coddes2,'' as coddes3,'' as coddes4,c.vall as codeappel, SUM(pce_mt) as pce_mt from cp_int a left join rhprubrique b on  a.coddes9=b.crub and a.cdos=b.cdos ";
//    query = query + " left join Rhfnom c on a.cdos=c.cdos and c.cacc = a.numcpt and c.ctab=402 and c.nume=2  ";
//    query = query + "where a.cdos='" + this.dossier + "' and numpce='" + numpiece + "'";
//    query = query + " and numcpt not like '6%' ";
//    query = query + " GROUP BY  a.libecr,coddes8,codjou,sens,numcpt,datcpt,c.vall";
//    query = query + " union all ";
//    query = query + " select d.vall as nom,'' as coddes8, codjou,sens,numcpt,datcpt, coddes1, coddes2,coddes3,coddes4,c.vall as codeappel, SUM(pce_mt) as pce_mt from cp_int a left join rhprubrique b on  a.coddes9=b.crub and a.cdos=b.cdos ";
//    query = query + " left join Rhfnom c on a.cdos=c.cdos and c.cacc = a.numcpt and c.ctab=402 and c.nume=2 left join Rhfnom d on a.cdos=d.cdos and d.cacc = a.numcpt and d.ctab=402 and d.nume=1 ";
//
//    query = query + "where a.cdos='" + this.dossier + "' and numpce='" + numpiece + "'";
//    query = query + " and numcpt like '6%' ";
//    query = query + " GROUP BY d.vall,coddes8, codjou,sens,numcpt,datcpt,coddes1,coddes2,coddes3,coddes4,c.vall ";
//    query = query + " ORDER BY NUMCPT,coddes1 desc ";
//
//    String requete = "DELETE FROM Rhttext WHERE idEntreprise = '" + this.dossier + "' and sess='" + this.session + "'";
//    Session oSession = null;
//    Transaction tx = null;
//    try
//    {
//      oSession = this.service.getSession();
//      tx = oSession.beginTransaction();
//      oSession.createQuery(requete).executeUpdate();
//
//      List<Object[]> lint = oSession.createSQLQuery(query).list();
//
//      String message = "";
//      Rhttext text = null;
//
//      ClsDate date = new ClsDate(this.globalUpdate.getPeriode(), "yyyyMM");
//      String libelleMois = StringUtils.capitalize(date.getDateS("MMMM"));
//
//
//      String TypePiece = "ODP";
//      String Site = "000";
//      String Journal = "JOD05";
//
//      String ReferencePiece = numpiece;
//      String LibelleDeLaPiece = "OD PAIE " + libelleMois + " " + numpiece;
//      String Devise = "XAF";
//      String DocumentOrigine = "Extraction " + libelleMois + " " + date.getYear();
//      String DateDuDocument = new ClsDate(new Date()).getDateS("ddMMyy");
//
//      String TypedeReferentiel = "1";
//
//
//
//      String LibelleDeLaligneEcriture = "D�pense pour M. ";
//
//
//      String NumeroOrdreDeAnalytique = "1";
//      String CodeAxe1 = "AX1";
//
//      String CodeAxe2 = "AX2";
//      String CentreAnalyse = "";
//      String CodeAxe3 = "AX3";
//      String LignesDeService = "";
//      String CodeAxe4 = "AX4";
//      String Projet = "";
//      String QuantiteImputeeEnAnalytique = "";
//
//
//
//
//      String nvl = " ";
//
//
//
//
//
//
//      int index = 0;
//      int nlig = 0;
//      for (int i = 0; i < lint.size(); i++)
//      {
//        index = 0;
//
//
//        LibelleDeLaligneEcriture = StringUtils.nvl((String)((Object[])lint.get(i))[(index++)], nvl);
//        String Tiers = StringUtils.nvl((String)((Object[])lint.get(i))[(index++)], nvl);
//        Journal = StringUtils.nvl((String)((Object[])lint.get(i))[(index++)], nvl);
//        String SensEcriture = StringUtils.nvl((String)((Object[])lint.get(i))[(index++)], nvl).equals("C") ? "-1" : "1";
//        String Compte = StringUtils.nvl((String)((Object[])lint.get(i))[(index++)], nvl);
//        String DateComptable = new ClsDate((Date)((Object[])lint.get(i))[(index++)]).getDateS("ddMMyy");
//        String coddes1 = StringUtils.nvl((String)((Object[])lint.get(i))[(index++)], nvl);
//        String coddes2 = StringUtils.nvl((String)((Object[])lint.get(i))[(index++)], nvl);
//        String coddes3 = StringUtils.nvl((String)((Object[])lint.get(i))[(index++)], nvl);
//        String coddes4 = StringUtils.nvl((String)((Object[])lint.get(i))[(index++)], nvl);
//        String Collectif = StringUtils.nvl((String)((Object[])lint.get(i))[(index++)], nvl);
//
//        BigDecimal pce_mt = new BigDecimal(StringUtils.valueOf(((Object[])lint.get(i))[(index++)]));
//        pce_mt = pce_mt.setScale(2);
//
//        String MontantEnDevise = pce_mt+"";
//        String MontantImputationAnalytiqueEnDevise = MontantEnDevise;
//        String NLigneDetail = (++nlig)+"";
//
//
//        message = TypePiece + separateur;
//        message = message + Site + separateur;
//        message = message + Journal + separateur;
//        message = message + DateComptable;
//        message = message + ReferencePiece + separateur;
//        message = message + LibelleDeLaPiece + separateur;
//        message = message + Devise + separateur;
//        message = message + DocumentOrigine + separateur;
//        message = message + DateDuDocument + separateur;
//        message = message + NLigneDetail + separateur;
//        message = message + TypedeReferentiel + separateur;
//
//        message = message + Collectif + separateur;
//        message = message + Compte + separateur;
//        message = message + Tiers + separateur;
//        message = message + LibelleDeLaligneEcriture + separateur;
//        message = message + SensEcriture + separateur;
//        message = message + MontantEnDevise + separateur;
//        message = message + NumeroOrdreDeAnalytique + separateur;
//        message = message + CodeAxe1 + separateur;
//
//        String CentreDeResponsabilite = coddes1;
//
//        message = message + CentreDeResponsabilite + separateur;
//        message = message + CodeAxe2 + separateur;
//
//
//        CentreAnalyse = coddes2;
//
//        message = message + CentreAnalyse + separateur;
//        message = message + CodeAxe3 + separateur;
//        LignesDeService = coddes3;
//
//
//        message = message + LignesDeService + separateur;
//        message = message + CodeAxe4 + separateur;
//
//        Projet = coddes4;
//
//
//        message = message + Projet + separateur;
//        message = message + QuantiteImputeeEnAnalytique + separateur;
//        message = message + MontantImputationAnalytiqueEnDevise + separateur;
//        if (pce_mt.signum() > 0) {
//          message = message + StringUtil.oraSubstring(ClsStringUtil.oraLPad(new StringBuilder().append(pce_mt).toString(), 16, "0"), 1, 16);
//        } else {
//          message = message + "-" + StringUtil.oraSubstring(ClsStringUtil.oraLPad(new StringBuilder().append(pce_mt.abs()).toString(), 16, "0"), 1, 16);
//        }
//        numero++;
//        text = new Rhttext();
//        text.setComp_id(new RhttextPK(this.session, Integer.valueOf(numero)));
//        text.setCdos(this.dossier);
//        text.setTexte(message);
//
//        oSession.save(text);
//        if (i % 20 == 0)
//        {
//          oSession.flush();
//          oSession.clear();
//        }
//      }
//      tx.commit();
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//      if (tx != null) {
//        tx.rollback();
//      }
//      this.errmess1 = ClsTreater._getStackTrace(e);
//      writeLog(this.errmess1);
//      this.pb = true;
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
//      return;
//    }
//    finally
//    {
//      this.service.closeConnexion(oSession);
//    }
//  }
//
//  public void generateOGLX3CNSS(HttpServletRequest request)
//  {
//    String separateur = "";
//
//
//    Cpdo cpdo = (Cpdo)this.service.get(Cpdo.class, this.dossier);
//    Integer nddd = Integer.valueOf(2);
//    nddd = cpdo == null ? nddd : cpdo.getNddd();
//    if (nddd == null) {
//      nddd = Integer.valueOf(2);
//    }
//    Object o = this.service.get(Rhfnom.class, new RhfnomPK(this.dossier, Integer.valueOf(99), "PAIE-INTER", Integer.valueOf(5)));
//    if (o != null)
//    {
//      Rhfnom nomenc = (Rhfnom)o;
//      if (!ClsObjectUtil.isNull(nomenc.getVall())) {
//        separateur = nomenc.getVall().trim();
//      }
//    }
//    else
//    {
//      this.errmess1 = "Erreur : Creez PAIE-INTER, Lib5 en Tb99.";
//      writeLog(this.errmess1);
//
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true); return;
//    }
//    Rhfnom nomenc;
//    o = this.service.get(Rhfnom.class, new RhfnomPK(this.dossier, Integer.valueOf(99), "INT-GI", Integer.valueOf(2)));
//    if (o == null)
//    {
//      this.errmess1 = "Creez INT-GI en table 99 (nomenclatures)";
//      writeLog(this.errmess1);
//      this.pb = true;
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
//      return;
//    }
//    Rhfnom nome = (Rhfnom)o;
//    String mjou;
//    String pjou;
//    if (StringUtils.nvl(nome.getVall(), " ").compareTo(" ") > 0)
//    {
//      pjou = StringUtils.oraRPad(StringUtil.oraSubstring(nome.getVall(), 1, 10), 10, ' ');
//      String mnpj = StringUtils.oraRPad(StringUtil.oraSubstring(nome.getVall(), 1, 8), 8, ' ');
//      mjou = StringUtils.oraRPad(StringUtil.oraSubstring(nome.getVall(), 1, 10), 10, ' ');
//    }
//    else
//    {
//      pjou = "65000     ";
//      String mnpj = "65000   ";
//      mjou = "65000     ";
//    }
//    if (ClsObjectUtil.isNull(separateur))
//    {
//      this.errmess1 = "Renseignez Lib5 de PAIE-INTER en Tb99.";
//      writeLog(this.errmess1);
//
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
//      return;
//    }
//    int numero = _getCurrentDBMaxRhttext(this.service, this.session).intValue();
//
//    String numpiece = new ClsDate(this.globalUpdate.getPeriode(), "yyyyMM").getDateS("MMyyyy") + this.globalUpdate.getNumerobulletin();
//
//    String query = " select a.libecr as nom,numtie,codjou,sens,numcpt,datcpt, '' as coddes1, '' as coddes2,coalesce(a.coddes3,'N') as coddes3,'' as coddes4, '' as coddes5, c.vall as codeappel, SUM(pce_mt) as pce_mt, a.codets, a.numpce from cp_int a left join rhprubrique b on  a.coddes9=b.crub and a.cdos=b.cdos ";
//    query = query + " left join Rhfnom c on a.cdos=c.cdos and c.cacc = a.numcpt and c.ctab=402 and c.nume=2  ";
//    query = query + "where a.cdos='" + this.dossier + "'";
//    query = query + " and coalesce(a.coddes3,'N') <> 'O' ";
//    query = query + " GROUP BY  a.libecr,numtie,coalesce(a.coddes3,'N'), codjou,sens,numcpt,datcpt,c.vall,a.codets,a.numpce";
//    query = query + " union all ";
//    query = query + " select a.libecr,numtie, codjou,sens,numcpt,datcpt, coddes1, coddes2,coddes3,coddes4,coddes5,c.vall as codeappel, SUM(pce_mt) as pce_mt, a.codets, a.numpce from cp_int a left join rhprubrique b on  a.coddes9=b.crub and a.cdos=b.cdos ";
//    query = query + " left join Rhfnom c on a.cdos=c.cdos and c.cacc = a.numcpt and c.ctab=402 and c.nume=2 ";
//
//    query = query + "where a.cdos='" + this.dossier + "'";
//    query = query + " and a.coddes3 = 'O' ";
//    query = query + " GROUP BY a.libecr,numtie, codjou,sens,numcpt,datcpt,coddes1,coddes2,coddes3,coddes4,coddes5,c.vall,a.codets,a.numpce ";
//    query = query + " ORDER BY numpce,NUMCPT,coddes1 desc ";
//
//    String requete = "DELETE FROM Rhttext WHERE idEntreprise = '" + this.dossier + "' and sess='" + this.session + "'";
//    Session oSession = null;
//    Transaction tx = null;
//    try
//    {
//      oSession = this.service.getSession();
//      tx = oSession.beginTransaction();
//      oSession.createQuery(requete).executeUpdate();
//
//      List<Object[]> lint = oSession.createSQLQuery(query).list();
//
//      String message = "";
//      Rhttext text = null;
//
//      ClsDate date = new ClsDate(this.globalUpdate.getPeriode(), "yyyyMM");
//      String libelleMois = StringUtils.capitalize(date.getDateS("MMMM"));
//
//
//      String indicateur = "G";
//      String TypePiece = "ODDIV";
//
//      String Journal = "JOD05";
//
//      String ReferencePiece = numpiece;
//      String LibelleDeLaPiece = "OD PAIE " + libelleMois + " " + numpiece;
//
//
//
//
//
//
//
//
//      String LibelleDeLaligneEcriture = "D�pense pour M. ";
//
//
//
//      String CodeAxe1 = "AX1";
//
//      String CodeAxe2 = "AX2";
//
//
//
//
//
//
//
//
//
//
//      String nvl = " ";
//
//
//
//
//
//
//      int index = 0;
//      int nlig = 0;
//      String curs_02 = "";
//      curs_02 = " SELECT pcea,cg,gesa,cana,ruba,sena,mona From cp_analyt where cdos = :cdos ";
//      curs_02 = curs_02 + " and pcea = :numpce";
//      curs_02 = curs_02 + " and cg = :numcpt";
//      curs_02 = curs_02 + " and gesa = :codges";
//      curs_02 = curs_02 + " and ruba = :coddes1";
//
//      String crlf = System.getProperty("line.separator", "\r\n");
//
//      String numpcecurrent = "";
//      for (int i = 0; i < lint.size(); i++)
//      {
//        index = 0;
//
//
//        LibelleDeLaligneEcriture = StringUtils.nvl((String)((Object[])lint.get(i))[(index++)], nvl);
//        String Tiers = StringUtils.nvl((String)((Object[])lint.get(i))[(index++)], nvl);
//
//        Journal = pjou;index++;
//        String SensEcriture = StringUtils.nvl((String)((Object[])lint.get(i))[(index++)], nvl).equals("C") ? "-1" : "1";
//        String Compte = StringUtils.nvl((String)((Object[])lint.get(i))[(index++)], nvl);
//        String DateComptable = new ClsDate((Date)((Object[])lint.get(i))[(index++)]).getDateS("ddMMyyyy");
//        String coddes1 = StringUtils.nvl((String)((Object[])lint.get(i))[(index++)], nvl);
//        String coddes2 = StringUtils.nvl((String)((Object[])lint.get(i))[(index++)], nvl);
//        String coddes3 = StringUtils.nvl((String)((Object[])lint.get(i))[(index++)], nvl);
//        String coddes4 = StringUtils.nvl((String)((Object[])lint.get(i))[(index++)], nvl);
//        String Collectif = StringUtils.nvl((String)((Object[])lint.get(i))[(index++)], nvl);
//        index++;
//
//        BigDecimal pce_mt = new BigDecimal(StringUtils.valueOf(((Object[])lint.get(i))[(index++)]));
//        pce_mt = pce_mt.setScale(nddd.intValue());
//        String codets = StringUtils.nvl((String)((Object[])lint.get(i))[(index++)], nvl);
//        String numpce = StringUtils.nvl((String)((Object[])lint.get(i))[(index++)], nvl);
//
//        message = "";
//        if ((i == 0) || (!numpce.equalsIgnoreCase(numpcecurrent)))
//        {
//          numpcecurrent = numpce;
//          ReferencePiece = numpce;
//          String entete = indicateur + separateur + TypePiece + separateur + ReferencePiece + separateur;
//          entete = entete + codets + separateur + Journal + separateur + DateComptable + separateur + LibelleDeLaPiece + separateur + ReferencePiece + separateur + "STDCO";
//
//          message = entete + crlf;
//        }
//        String MontantEnDevise = pce_mt+"";
//        String MontantImputationAnalytiqueEnDevise = MontantEnDevise;
//        String NLigneDetail = (++nlig)+"";
//
//        message = message + "D" + separateur;
//        message = message + NLigneDetail + separateur;
//        message = message + "1" + separateur;
//        message = message + Compte + separateur;
//        message = message + StringUtils.trim(Tiers) + separateur;
//        message = message + LibelleDeLaligneEcriture + separateur;
//        message = message + SensEcriture + separateur;
//        message = message + MontantEnDevise + separateur;
//        message = message + nvl.trim() + separateur;
//        if (StringUtils.equalsIgnoreCase(coddes3, "O"))
//        {
//          message = message + crlf;
//          message = message + "D" + separateur;
//          message = message + NLigneDetail + separateur;
//          message = message + "2" + separateur;
//          message = message + Compte + separateur;
//          message = message + Tiers + separateur;
//          message = message + LibelleDeLaligneEcriture + separateur;
//          message = message + SensEcriture + separateur;
//          message = message + MontantEnDevise + separateur;
//          message = message + nvl.trim() + separateur;
//          curs_02 = " SELECT pcea,cg,gesa,cana,ruba,sena,mona From cp_analyt where idEntreprise = '" + this.dossier + "' ";
//          curs_02 = curs_02 + " and pcea = '" + numpce + "'";
//          curs_02 = curs_02 + " and cg = '" + Compte + "'";
//          curs_02 = curs_02 + " and gesa = '" + coddes2 + "'";
//          curs_02 = curs_02 + " and ruba = '" + coddes1 + "'";
//          curs_02 = curs_02 + " and sena = '" + (StringUtils.equalsIgnoreCase(SensEcriture, "-1") ? "C" : "D") + "'";
//          curs_02 = curs_02 + " order by cdos,pcea,cg,gesa,cana,ruba,sena,mona ";
//          try
//          {
//            String pcea = "";
//            String cg = "";
//            String gesa = "";
//            String cana = "";
//            String ruba = "";
//
//            int mona = 0;
//            String anum = "";
//            int numana = 0;
//            String amon = "";String aimp = "";
//            List<Object[]> list = oSession.createSQLQuery(curs_02).list();
//            if ((list != null) && (!list.isEmpty())) {
//              for (Object[] obj : list)
//              {
//                pcea = "";
//                cg = "";
//                gesa = "";
//                cana = "";
//                ruba = "";
//                if (obj[0] != null) {
//                  pcea = obj[0].toString();
//                }
//                if (obj[1] != null) {
//                  cg = obj[1].toString();
//                }
//                if (obj[2] != null) {
//                  gesa = obj[2].toString();
//                }
//                if (obj[3] != null) {
//                  cana = obj[3].toString();
//                }
//                if (obj[4] != null) {
//                  ruba = obj[4].toString();
//                }
//                if (obj[6] != null) {
//                  mona = obj[6] == null ? 0 : Integer.valueOf(obj[6].toString()).intValue();
//                }
//                numana++;
//                anum = StringUtils.oraRPad(StringUtils.oraLTrim(StringUtils.oraToChar(Integer.valueOf(numana)), " "), 4, ' ');
//                amon = StringUtil.oraSubstring(ClsStringUtil.oraLPad(StringUtils.oraLTrim(StringUtils.oraToChar(Integer.valueOf(mona)), " "), 20, "0"), 1, 20);
//                aimp = StringUtils.oraRPad(StringUtils.oraLTrim(cana, " "), 8, ' ');
//                message = message + crlf;
//                message = message + "A" + separateur;
//                message = message + anum + separateur;
//                message = message + gesa + separateur;
//                message = message + aimp + separateur;
//                message = message + separateur;
//                message = message + separateur;
//                message = message + separateur;
//                message = message + separateur;
//                message = message + separateur;
//                message = message + separateur;
//                message = message + separateur;
//                message = message + separateur;
//                message = message + amon;
//              }
//            }
//          }
//          catch (Exception ex)
//          {
//            writeLog(ex.getLocalizedMessage());
//          }
//        }
//        numero++;
//        text = new Rhttext();
//        text.setComp_id(new RhttextPK(this.session, Integer.valueOf(numero)));
//        text.setCdos(this.dossier);
//        text.setTexte(message);
//
//        oSession.save(text);
//        if (i % 20 == 0)
//        {
//          oSession.flush();
//          oSession.clear();
//        }
//      }
//      tx.commit();
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//      if (tx != null) {
//        tx.rollback();
//      }
//      this.errmess1 = ClsTreater._getStackTrace(e);
//      writeLog(this.errmess1);
//      this.pb = true;
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
//      return;
//    }
//    finally
//    {
//      this.service.closeConnexion(oSession);
//    }
//  }
//
//  public void generateOGLComilog(HttpServletRequest request)
//  {
//    String query = " SELECT  ";
//    query = query + " 'NEW'||'|'  ";
//    query = query + " ||'1'||'|'                        ";
//    query = query + " ||'DELTA PAIE'||'|'                ";
//    query = query + " ||'55-DELTA PAIE'||'|'                    ";
//    query = query + " ||TO_CHAR(datcpt,'DD/MM/YYYY')||'|'        ";
//    query = query + " ||DEVPCE||'|'                    ";
//    query = query + " ||TO_CHAR(datcre,'DD/MM/YYYY')||'|'        ";
//    query = query + " ||'99999'||'|'                    ";
//    query = query + " ||'A'||'|'                        ";
//    query = query + " ||TO_CHAR(datpce,'DD/MM/YYYY')||'|'        ";
//    query = query + " ||'SPOT'||'|'                    ";
//    query = query + " ||'01'||'|'                        ";
//    query = query + " ||NUMCPT||'|'                    ";
//    query = query + " ||DECODE(NUMTIE,null,'000000','T0'||SUBSTR(NUMTIE,1,4))||'|'            ";
//    query = query + " ||DECODE(SUBSTR(NUMCPT,1,1),'1','00','2','00','3','00','4','00','5','00',DECODE(SUBSTR(CODDES1,1,3),'321','95','92'))||'|'        ";
//    query = query + " ||DECODE(SUBSTR(NUMCPT,1,1),'1','0000','2','0000','3','0000','4','0000','5','0000', CODDES1)||'|'                    ";
//    query = query + " ||'0'||'|'                        ";
//    query = query + " ||'0'||'|'                        ";
//    query = query + " ||NULL||'|'                        ";
//    query = query + " ||NULL||'|'                        ";
//    query = query + " ||NULL||'|'                        ";
//    query = query + " ||NULL||'|'                        ";
//    query = query + " ||DECODE(SENS,'D',TO_CHAR(PCE_MT),null)||'|'    ";
//    query = query + " ||DECODE(SENS,'C',TO_CHAR(PCE_MT),null)||'|'    ";
//    query = query + " ||DECODE(SENS,'D',TO_CHAR(PCE_MT),null)||'|'    ";
//    query = query + " ||DECODE(SENS,'C',TO_CHAR(PCE_MT),null)||'|'    ";
//    query = query + " ||'DELTA PAIE'||'|'                ";
//    query = query + " ||CODJOU||'|'                    ";
//    query = query + " ||'NO'||'|'                        ";
//    query = query + " ||libecr||'|' as texte             ";
//    query = query + " FROM cp_int ";
//    query = query + " Where idEntreprise = '" + this.dossier + "'";
//
//    String numpce = new ClsDate(this.globalUpdate.getPeriode(), "yyyyMM").getDateS("MMyyyy") + this.globalUpdate.getNumerobulletin();
//    query = query + " and numpce = '" + numpce + "'";
//
//    Session _o_Session = this.service.getSession();
//    Transaction tx = null;
//    Connection oConnexion = null;
//    Statement oStatement = null;
//    ResultSet oResultSet = null;
//    try
//    {
//      tx = _o_Session.beginTransaction();
//      oConnexion = _o_Session.connection();
//      oStatement = oConnexion.createStatement();
//      oResultSet = oStatement.executeQuery(query);
//      Rhttext texte = null;
//
//      Integer i = Integer.valueOf(0);
//
//      Integer nblig = Integer.valueOf(0);
//      while (oResultSet.next())
//      {
//        nblig = Integer.valueOf(nblig.intValue() + 1);
//        try
//        {
//          if (i.intValue() % 20 == 0)
//          {
//            _o_Session.flush();
//            _o_Session.clear();
//          }
//          texte = new Rhttext();
//          texte.setComp_id(new RhttextPK(this.session, nblig));
//          texte.setCdos(this.dossier);
//          texte.setTexte(oResultSet.getString("texte"));
//          _o_Session.save(texte);
//          i = Integer.valueOf(i.intValue() + 1);
//        }
//        catch (Exception e)
//        {
//          e.printStackTrace();
//        }
//      }
//      tx.commit();
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//      if (tx != null) {
//        tx.rollback();
//      }
//      this.errmess1 = ClsTreater._getStackTrace(e);
//      writeLog(this.errmess1);
//      this.pb = true;
//      this.globalUpdate._setEvolutionTraitement(request, this.errmess1, true);
//      return;
//    }
//    finally
//    {
//      try
//      {
//        oResultSet.close();
//        oResultSet = null;
//        oStatement.close();
//        oStatement = null;
//        oConnexion.close();
//        oConnexion = null;
//        this.service.closeConnexion(_o_Session);
//      }
//      catch (Exception e)
//      {
//        e.printStackTrace();
//      }
//    }
//  }
  
  private Object callMethode(Object obj, String nomMethode, Class classe, Object valeurParam)
  {
    if (obj == null) {
      return null;
    }
    try
    {
      Method oMethod = obj.getClass().getMethod(nomMethode, new Class[] { classe });
      if (oMethod != null) {
        return oMethod.invoke(obj, new Object[] { valeurParam });
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return null;
  }
  
  public String replicate(String car, int size)
  {
    String result = "";
    for (int i = 0; i < size; i++) {
      result = result + car;
    }
    return result;
  }
  
  public String nvlEmpty(String chaine, String remplacant)
  {
    if (StringUtils.isEmpty(chaine)) {
      return remplacant;
    }
    return chaine;
  }
  
  public Integer getNddd()
  {
    return this.nddd;
  }
  
  public void setNddd(Integer nddd)
  {
    this.nddd = nddd;
  }
  
  void writeLog(String error)
  {
    LogMessage logger = new LogMessage();
    logger.setIdEntreprise(new Integer(this.dossier));
    logger.setCuti(this.cuti);
    logger.setDatc(new Date());
    logger.setLigne(error);
  }
  
  public class Fpint
    implements Serializable
  {
    private String nste;
    private String cjou;
    private Integer dtcp;
    private String ncpt;
    private String refl;
    private String seca;
    private String cpta;
    private Integer npce;
    private Integer dtsa;
    private String liba;
    private String libe;
    private String cptc;
    private String sens;
    private BigDecimal mtdd;
    private BigDecimal cour;
    private BigDecimal mtdc;
    private String cdev;
    private String cuti;
    private Integer dtec;
    private Integer dtvl;
    private String mpnr;
    private String stat;
    private String cenr;
    private Integer dfac;
    
    public Fpint(String nste, String cjou, Integer dtcp, String ncpt, String refl, String seca, String cpta, Integer npce, Integer dtsa, String liba, String libe, String cptc, String sens, BigDecimal mtdd, BigDecimal cour, BigDecimal mtdc, String cdev, String cuti, Integer dtec, Integer dtvl, String mpnr, String stat, String cenr, Integer dfac)
    {
      this.nste = nste;
      this.cjou = cjou;
      this.dtcp = dtcp;
      this.ncpt = ncpt;
      this.refl = refl;
      this.seca = seca;
      this.cpta = cpta;
      this.npce = npce;
      this.dtsa = dtsa;
      this.liba = liba;
      this.libe = libe;
      this.cptc = cptc;
      this.sens = sens;
      this.mtdd = mtdd;
      this.cour = cour;
      this.mtdc = mtdc;
      this.cdev = cdev;
      this.cuti = cuti;
      this.dtec = dtec;
      this.dtvl = dtvl;
      this.mpnr = mpnr;
      this.stat = stat;
      this.cenr = cenr;
      this.dfac = dfac;
    }
    
    public Fpint() {}
    
    public String getNste()
    {
      return this.nste;
    }
    
    public void setNste(String nste)
    {
      this.nste = nste;
    }
    
    public String getCjou()
    {
      return this.cjou;
    }
    
    public void setCjou(String cjou)
    {
      this.cjou = cjou;
    }
    
    public Integer getDtcp()
    {
      return this.dtcp;
    }
    
    public void setDtcp(Integer dtcp)
    {
      this.dtcp = dtcp;
    }
    
    public String getNcpt()
    {
      return this.ncpt;
    }
    
    public void setNcpt(String ncpt)
    {
      this.ncpt = ncpt;
    }
    
    public String getRefl()
    {
      return this.refl;
    }
    
    public void setRefl(String refl)
    {
      this.refl = refl;
    }
    
    public String getSeca()
    {
      return this.seca;
    }
    
    public void setSeca(String seca)
    {
      this.seca = seca;
    }
    
    public String getCpta()
    {
      return this.cpta;
    }
    
    public void setCpta(String cpta)
    {
      this.cpta = cpta;
    }
    
    public Integer getNpce()
    {
      return this.npce;
    }
    
    public void setNpce(Integer npce)
    {
      this.npce = npce;
    }
    
    public Integer getDtsa()
    {
      return this.dtsa;
    }
    
    public void setDtsa(Integer dtsa)
    {
      this.dtsa = dtsa;
    }
    
    public String getLiba()
    {
      return this.liba;
    }
    
    public void setLiba(String liba)
    {
      this.liba = liba;
    }
    
    public String getLibe()
    {
      return this.libe;
    }
    
    public void setLibe(String libe)
    {
      this.libe = libe;
    }
    
    public String getCptc()
    {
      return this.cptc;
    }
    
    public void setCptc(String cptc)
    {
      this.cptc = cptc;
    }
    
    public String getSens()
    {
      return this.sens;
    }
    
    public void setSens(String sens)
    {
      this.sens = sens;
    }
    
    public BigDecimal getMtdd()
    {
      return this.mtdd;
    }
    
    public void setMtdd(BigDecimal mtdd)
    {
      this.mtdd = mtdd;
    }
    
    public BigDecimal getCour()
    {
      return this.cour;
    }
    
    public void setCour(BigDecimal cour)
    {
      this.cour = cour;
    }
    
    public BigDecimal getMtdc()
    {
      return this.mtdc;
    }
    
    public void setMtdc(BigDecimal mtdc)
    {
      this.mtdc = mtdc;
    }
    
    public String getCdev()
    {
      return this.cdev;
    }
    
    public void setCdev(String cdev)
    {
      this.cdev = cdev;
    }
    
    public String getCuti()
    {
      return this.cuti;
    }
    
    public void setCuti(String cuti)
    {
      this.cuti = cuti;
    }
    
    public Integer getDtec()
    {
      return this.dtec;
    }
    
    public void setDtec(Integer dtec)
    {
      this.dtec = dtec;
    }
    
    public Integer getDtvl()
    {
      return this.dtvl;
    }
    
    public void setDtvl(Integer dtvl)
    {
      this.dtvl = dtvl;
    }
    
    public String getMpnr()
    {
      return this.mpnr;
    }
    
    public void setMpnr(String mpnr)
    {
      this.mpnr = mpnr;
    }
    
    public String getStat()
    {
      return this.stat;
    }
    
    public void setStat(String stat)
    {
      this.stat = stat;
    }
    
    public String getCenr()
    {
      return this.cenr;
    }
    
    public void setCenr(String cenr)
    {
      this.cenr = cenr;
    }
    
    public Integer getDfac()
    {
      return this.dfac;
    }
    
    public void setDfac(Integer dfac)
    {
      this.dfac = dfac;
    }
    
    public String toString()
    {
      return 
      























        new ToStringBuilder(this).append("nste", getNste()).append("cjou", getCjou()).append("dtcp", getDtcp()).append("ncpt", getNcpt()).append("refl", getRefl()).append("seca", getSeca()).append("cpta", getCpta()).append("npce", getNpce()).append("dtsa", getDtsa()).append("liba", getLiba()).append("libe", getLibe()).append("cptc", getCptc()).append("sens", getSens()).append("mtdd", getMtdd()).append("cour", getCour()).append("mtdc", getMtdc()).append("cdev", getCdev()).append("cuti", getCuti()).append("dtec", getDtec()).append("dtvl", getDtvl()).append("mpnr", getMpnr()).append("stat", getStat()).append("cenr", getCenr()).append("dfac", getDfac()).toString();
    }
  }
}
