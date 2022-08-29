package com.kinart.evaluation.business.service.impl;

import com.kinart.api.evaluation.dto.EvalcritnoteDto;
import com.kinart.api.evaluation.dto.ParamGenerateEvaluationDto;
import com.kinart.evaluation.business.model.*;
import com.kinart.evaluation.business.service.GenererEvaluationService;
import com.kinart.paie.business.model.Salarie;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class GenererEvaluationServiceImpl implements GenererEvaluationService {

    private GeneriqueConnexionService service;

    @Autowired
    public GenererEvaluationServiceImpl(GeneriqueConnexionService service){
        this.service = service;
    }

    /**
     * Génération des fiche d'évaluation
     *
     * @param dto
     * @return
     */
    @Override
    public List<String> genererFicheEvaluation(ParamGenerateEvaluationDto dto) {
        List<String> messages = new ArrayList<String>();

        // Liste des modèles concernés
        List<Evalmodelchamp> lstModeles = service.find("from Evalmodelchamp where identreprise='" + dto.getIdentreprise() + "' and codechamp='" + dto.getCodeChamp()+ "'");

        // Liste des agents concernés
        List<Salarie> lstAgent = service.find("From Salarie WHERE identreprise='" + dto.getIdentreprise() + "' and nmat in (select nmat from Evalchampagent where identreprise='" + dto.getIdentreprise() + "' and codechamp='" + dto.getCodeChamp() + "')");

        if(lstModeles != null && lstModeles.size() > 0){
            String strCodeModele = null;
            Integer cmpModele = 0;
            String strCodeEval = StringUtils.EMPTY;
            for (Evalmodelchamp rhpmodelchamp : lstModeles)
            {
                if(lstAgent == null || lstAgent.size() == 0){
                    continue;
                }

                cmpModele++;
                strCodeModele = rhpmodelchamp.getCodemodel();

                List<Evalcarrmodele> modeles = service.find("From Evalcarrmodele where identreprise='" + dto.getIdentreprise() + "' and codemodele='" + rhpmodelchamp.getCodemodel()+ "'");
                if(modeles!=null && modeles.size()>0){
                    Evalcarrmodele modele = modeles.get(0);
                    for (Salarie salarie : lstAgent){
                        // Suppression des fiches existantes
                        suprressionEvaluationExistante(dto.getIdentreprise(), salarie.getNmat(), strCodeModele);
                        strCodeEval = getRangEvalAgent(dto.getIdentreprise(), salarie.getNmat(),dto.getDtFin()) + cmpModele;
                        // Commentaires

                        // **************************Objectifs*****************************************
                        // Objectifs affectés au salarié
                        genererObjectifForSalarie(dto.getIdentreprise(), salarie.getNmat(), strCodeEval);
                        // Objectifs liés au poste
                        int ordre = 1;
                        List<EvalcritnoteDto> lstObjectifs = genererObjectifForFonction(dto.getIdentreprise(), salarie.getNmat(), strCodeEval);
                        for(EvalcritnoteDto vo : lstObjectifs){
                            Evalobjectif objectif = new Evalobjectif();
                            objectif.setIdEntreprise(dto.getIdentreprise());
                            objectif.setCodeeval(strCodeEval);
                            objectif.setNmat(salarie.getNmat());
                            objectif.setOrdre(new Integer(ordre));
                            objectif.setNature("MIS");
                            objectif.setDesc(vo.getLibcritere());
                            objectif.setPoids(BigDecimal.ZERO);
                            objectif.setCodeobj(vo.getCodecrit());
                            objectif.setNote(BigDecimal.ZERO);
                            objectif.setNote(BigDecimal.ZERO);
                            objectif.setNotep(BigDecimal.ZERO);
                            objectif.setRt1(BigDecimal.ZERO);
                            objectif.setRt2(BigDecimal.ZERO);
                            service.save(objectif);
                            ordre = ordre + 1;
                        }
                        //******************************************************************************

                        // **********************************Compétences****************************************
                        // Compétences techniques
                        List<EvalcritnoteDto> lstApprec = getListeCritereStandard(dto.getIdentreprise(), salarie.getNmat(), strCodeEval, salarie.getClas(), 785);
                        for(EvalcritnoteDto vo : lstApprec){
                            if(vo.getNotechf()==null) vo.setNotechf(BigDecimal.ZERO);
                            service.save(EvalcritnoteDto.toEntity(vo));
                        }

                        // Compétences comportementales
                        lstApprec = getListeCritereStandard(dto.getIdentreprise(), salarie.getNmat(), strCodeEval, salarie.getClas(), 786);
                        for(EvalcritnoteDto vo : lstApprec){
                            if(vo.getNotechf()==null) vo.setNotechf(BigDecimal.ZERO);
                            service.save(EvalcritnoteDto.toEntity(vo));
                        }

                        // Compétences managériales
                        lstApprec = getListeCritereStandard(dto.getIdentreprise(), salarie.getNmat(), strCodeEval, salarie.getClas(), 787);
                        for(EvalcritnoteDto vo : lstApprec){
                            if(vo.getNotechf()==null) vo.setNotechf(BigDecimal.ZERO);
                            service.save(EvalcritnoteDto.toEntity(vo));
                        }

                        // Motivation
                        lstApprec = getListeCritereStandard(dto.getIdentreprise(), salarie.getNmat(), strCodeEval, salarie.getClas(), 788);
                        for(EvalcritnoteDto vo : lstApprec){
                            if(vo.getNotechf()==null) vo.setNotechf(BigDecimal.ZERO);
                            service.save(EvalcritnoteDto.toEntity(vo));
                        }
                        //****************************************************************************************

                    }
                }
            }
        }

        return messages;
    }

    @SuppressWarnings("unchecked")
    private String getRangEvalAgent(int cdos, String nmat, Date dtfin) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(dtfin);
        int intYearVal = c1.get(Calendar.YEAR);

        String strCode = "";

        String strQuery = "from Evaluation " + "	where identreprise = '"
                + cdos + "'" + " and nmat = '" + nmat + "' "
                + " order by codeeval desc";
        List<Evaluation> maListe = service.find(strQuery);
        if (maListe.size() > 0) {
            String strCodeEvalBD = maListe.get(0).getCodeeval();
            int intYearValDB = Integer.parseInt(strCodeEvalBD.substring(0, 4));
            int intRang = Integer.parseInt(strCodeEvalBD.substring(4,6));
            if (intYearValDB < intYearVal) {
                strCode = String.valueOf(intYearVal) + "01";
                return strCode;
            } else {
                intRang = intRang + 1;
                if (intRang < 10) {
                    strCode = String.valueOf(intYearValDB) + "0"
                            + String.valueOf(intRang);
                    return strCode;
                } else {
                    strCode = String.valueOf(intYearValDB)
                            + String.valueOf(intRang);
                    return strCode;
                }
            }
        } else {
            strCode = String.valueOf(intYearVal) + "01";
            return strCode;
        }
    }

    private List<EvalcritnoteDto> getListeCritereStandard(int cdos, String nmat, String codeEvaluation, String calsseSalarie, int ctab){
        List<EvalcritnoteDto> result = new ArrayList<EvalcritnoteDto>();
        String sql = "select crit.*, nc.vall libcrit, nc.cacc codecritnom, nc.valt coeff "+
                "from ParamData nc "+
                "left join Evalcritnote crit on (nc.identreprise=crit.identreprise and nc.cacc=crit.codecrit and crit.nmat='"+nmat+"' and crit.codeeval='"+codeEvaluation+"') "+
                "where nc.ctab="+ctab+" and nc.nume=1 and nc.identreprise='"+cdos+"' and nc.valm="+new Integer(calsseSalarie).intValue();

        Session session  = service.getSession();
        Query query = session.createSQLQuery(sql).addEntity("crit", Evalcritnote.class).addScalar("libcrit",StandardBasicTypes.STRING).addScalar("codecritnom",StandardBasicTypes.STRING)
                .addScalar("coeff", StandardBasicTypes.BIG_DECIMAL);

        List<Object[]> objs = query.list();
        if(objs == null || objs.size() == 0) return result;

        try {
            for(Object[] obj:objs){
                EvalcritnoteDto vo = new EvalcritnoteDto();
                String libCritere = (obj[1]!=null)?(String)obj[1]:null;
                String codeCritere = (obj[2]!=null)?(String)obj[2]:null;
                BigDecimal coeffCrit = (obj[3]!=null)?(BigDecimal)obj[3]:BigDecimal.ONE;
                vo.setIdEntreprise(cdos);
                vo.setCodecrit(codeCritere);
                vo.setCodeeval(codeEvaluation);
                vo.setNmat(nmat);
                vo.setLibcritere(libCritere);
                if(obj[0]!=null){
                    Evalcritnote ent = (Evalcritnote)obj[0];
                    if(ent!=null){
                        vo.setNotechf(ent.getNotechf());
                        if(coeffCrit!=null) vo.setPoids(coeffCrit);
                        else vo.setPoids(BigDecimal.ONE);
                    } else {
                        vo.setNotechf(BigDecimal.ZERO);
                        if(coeffCrit!=null) vo.setPoids(coeffCrit);
                        else vo.setPoids(BigDecimal.ONE);
                    }
                } else {
                    vo.setNotechf(BigDecimal.ZERO);
                    if(coeffCrit!=null) vo.setPoids(coeffCrit);
                    else vo.setPoids(BigDecimal.ONE);
                }

                result.add(vo);
            }

        } catch (Exception e) {
            // TODO: handle exception
            return new ArrayList<EvalcritnoteDto>();
        } finally {
            service.closeSession(session);
        }

        return result;
    }

    private String getEvaluationExistante(int cdos, String matricule, String codemodele)
    {
        Session session = service.getSession();
        String queryString = "Select cdos, codeeval from Evaluation where identreprise = '" + cdos + "' and nmat='"+matricule+"' and codemodel='" + codemodele + "'";
        Query query = session.createSQLQuery(queryString);
        List<Object[]> listPied = query.list();
        service.closeSession(session);
        Object[] obj = null;
        if (listPied != null && !listPied.isEmpty())
        {
            obj = listPied.get(0);

            return (obj[1] != null ? (String) obj[1] : "");
        }
        return StringUtils.EMPTY;
    }

    private void suprressionEvaluationExistante(int cdos, String matricule, String codemodele)
    {
        String codeeval = getEvaluationExistante(cdos, matricule, codemodele);
        if(StringUtils.isEmpty(codeeval)) return;

        Session session = service.getSession();
        Transaction tx = null;
        try
        {
            tx = session.beginTransaction();
            try
            {
                Evaluation eval = null;//(Evaluation)service.get(Rhtevaluation.class, new RhtevaluationPK(matricule, codeeval, codemodele));
                List listEval = service.find("from Evaluation " +
                        " where identreprise = '" + cdos + "'" +
                        " and nmat = '" + matricule + "'" +
                        " and codeeval = '" + codeeval + "'"+
                        " and codemodel = '" + codemodele + "'");
                if(listEval!=null && listEval.size()>0) eval = (Evaluation)listEval.get(0);

                List listOfPhase = service.find("from Evalhaseeval " +
                        " where identreprise = '" + cdos + "'" +
                        " and nmat = '" + matricule + "'" +
                        " and codeeval = '" + codeeval + "'");
                List listOfEtape = null;
                List listOfCritere = null;
                List listOfCommentaire= null;
                for (Object phase : listOfPhase) {
                    listOfEtape = service.find("from Evaletapeeval " +
                            " where identreprise = '" + cdos + "'" +
                            " and codephase = '" + ((Evalphaseeval)phase).getCodephase() + "'" +
                            " and nmat = '" + matricule + "'"+
                            " and codeeval = '" + codeeval + "'");
                    for (Object etape : listOfEtape) {
                        listOfCritere = service.find("from Evalcriteval " +
                                " where identreprise = '" + cdos + "'" +
                                " and codephase = '" + ((Evalphaseeval)phase).getCodephase() + "'" +
                                " and nmat = '" + matricule + "'"+
                                " and codeeval = '" + codeeval + "'");
                        for (Object critere : listOfCritere) {
                            //suppression du crit�re
                            session.delete((Evalcriteval)critere);
                        }
                        //suppression de la phase
                        session.delete((Evaletapeeval)etape);
                    }
                    listOfCommentaire = service.find("from Rhtevalcommentaire1 " +
                            " where identreprise = '" + cdos + "'" +
                            " and codephase = '" + ((Evalphaseeval)phase).getCodephase() + "'" +
                            " and nmat = '" + matricule + "'"+
                            " and codeeval = '" + codeeval + "'");
                    for (Object comm : listOfCommentaire) {
                        session.delete(comm);
                    }
                    //suppression de la phase
                    session.delete((Evalphaseeval)phase);
                }
                //suppression de l'évaluation
                if(eval!=null)
                    session.delete(eval);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            String query = null; //"Delete From Rhtevalobjectif where identreprise='" + cdos + "' and nmat='" + vo.getNmat() + "' and codeeval = '" + vo.getCodeeval() + "'";
//					session.createSQLQuery(query).executeUpdate();
            session.createSQLQuery("Delete From Evalobjectif where identreprise='" + cdos + "' and nmat='" + matricule + "' and codeeval = '" + codeeval + "'").executeUpdate();

//					query = "Delete From Rhtevalcritnote where identreprise='" + cdos + "' and nmat='" + vo.getNmat() + "' and codeeval = '" + vo.getCodeeval() + "'";
//					session.createSQLQuery(query).executeUpdate();
            session.createSQLQuery("Delete From Evalcritnote where identreprise='" + cdos + "' and nmat='" + matricule + "' and codeeval = '" + codeeval + "'").executeUpdate();

            session.createSQLQuery("Delete From Evalanal where identreprise='" + cdos + "' and nmat='" + matricule + "' and codeeval = '" + codeeval + "'").executeUpdate();

//			//					query = "Delete From Rhtevaluation where identreprise='" + cdos + "' and nmat='" + vo.getNmat() + "' and codeeval = '" + vo.getCodeeval() + "'";
//					session.createSQLQuery(query).executeUpdate();

//					service.delete(session, "From Rhtevaluation where identreprise='" + cdos + "' and nmat='" + vo.getNmat() + "' and codeeval = '" + vo.getCodeeval() + "'");
            List listOfEvaluation = session.createQuery("From Evaluation where identreprise='" + cdos + "' and nmat='" + matricule + "' and codeeval = '" + codeeval + "'").list();
            for (Object evaluat : listOfEvaluation) {
                //suppression du critère
                session.delete((Evaluation)evaluat);
            }

            tx.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            if(tx != null)
                tx.rollback();
        }
        finally
        {
            service.closeSession(session);
        }
    }

    private void genererObjectifForSalarie(int cdos, String matricule, String strCodeEval){
        String strQuery = "select a.vall libobj, a.cacc codeobj, d1.vall indicat, d2.vall cible, a.valm coefp, d3.vall natobj "+
                "from Orgposteinfo g " +
                "inner join (select nmat, max(codeinfo4) codeinfo4 from Orgposteinfo where identreprise='"+cdos+"' and typeinfo='TASK' group by nmat)h on g.codeinfo4=h.codeinfo4 and g.nmat=h.nmat  "+
                ",ParamData a "+
                "left join ParamData d1 on (d1.identreprise=a.identreprise and d1.ctab=a.ctab and d1.cacc=a.cacc and d1.nume=3) "+
                "left join ParamData d2 on (d2.identreprise=a.identreprise and d2.ctab=a.ctab and d2.cacc=a.cacc and d2.nume=4) "+
                "left join ParamData d3 on (d3.identreprise=a.identreprise and d3.ctab=a.ctab and d3.cacc=a.cacc and d3.nume=2) "+
                "where g.identreprise=a.identreprise and g.typeinfo='TASK' and g.codeinfo1=a.cacc and a.ctab=119 and a.nume=1 and a.identreprise='"+cdos+"' and g.nmat='"+matricule+"' order by a.cacc asc";

        Session session = service.getSession();
        Query query = session.createSQLQuery(strQuery);

        List<Object[]> objs = query.list();
        service.closeSession(session);
        int ordre = 1;
        if(objs != null && objs.size() > 0){
            for(Object[] obj:objs){
                String libelleobjectif = (obj[0]!=null)?(String)obj[0]:null;
                String codeobjectif = (obj[1]!=null)?(String)obj[1]:null;
                String indicateur = (obj[2]!=null)?(String)obj[2]:null;
                String cible = (obj[3]!=null)?(String)obj[3]:null;
                BigDecimal cp = (obj[4]!=null)?(BigDecimal)obj[4]:null;
                String natureobj = (obj[5]!=null)?(String)obj[5]:null;

                Evalobjectif objectif = new Evalobjectif();
                objectif.setIdEntreprise(cdos);
                objectif.setCodeeval(strCodeEval);
                objectif.setNmat(matricule);
                objectif.setOrdre(new Integer(ordre));
                objectif.setNature("OBJ");
                objectif.setDesc(libelleobjectif);
                objectif.setPoids(cp);
                objectif.setCodeobj(codeobjectif);
                objectif.setMoy(cible);
                objectif.setNote(BigDecimal.ZERO);
                objectif.setNotep(BigDecimal.ZERO);
                objectif.setRt1(BigDecimal.ZERO);
                objectif.setRt2(BigDecimal.ZERO);
                service.save(objectif);
                ordre = ordre + 1;
            }
        }
    }

    private List<EvalcritnoteDto> genererObjectifForFonction(int cdos, String matricule, String strCodeEval){
        List<EvalcritnoteDto> result = new ArrayList<EvalcritnoteDto>();

        // Lecture des compétences liées à la fonction
        String sql = "select f.cacc, f.valm, c1.vall comp1, c2.vall comp2, c3.vall comp3, c4.vall comp4 "+
                "from Salarie a, ParamData f "+
                "left join rhfnom c1 on c1.identreprise=f.identreprise and c1.ctab=f.ctab and c1.cacc=f.cacc and c1.nume=7 "+
                "left join rhfnom c2 on c2.identreprise=f.identreprise and c2.ctab=f.ctab and c2.cacc=f.cacc and c2.nume=8 "+
                "left join rhfnom c3 on c3.identreprise=f.identreprise and c3.ctab=f.ctab and c3.cacc=f.cacc and c3.nume=9 "+
                "left join rhfnom c4 on c4.identreprise=f.identreprise and c4.ctab=f.ctab and c4.cacc=f.cacc and c4.nume=10 "+
                "where a.identreprise=f.identreprise and a.fonc=f.cacc and f.ctab=743 and f.nume=1 and a.nmat='"+matricule+"'";

        Session session  = service.getSession();
        Query query = session.createSQLQuery(sql);

        List<Object[]> objs = query.list();
        service.closeSession(session);
        if(objs == null || objs.size() == 0){
            return result;
        } else {
            try {
                for(Object[] obj:objs){
                    BigDecimal addCodeFonc = BigDecimal.ONE;
                    String competence1 = (obj[2]!=null)?(String)obj[2]:null;
                    String competence2 = (obj[3]!=null)?(String)obj[3]:null;
                    String competence3 = (obj[4]!=null)?(String)obj[4]:null;
                    String competence4 = (obj[5]!=null)?(String)obj[5]:null;
//					if(StringUtils.isNotEmpty(competence1)){
                    EvalcritnoteDto vo = new EvalcritnoteDto();
                    String codefonc = (obj[0]!=null)?(String)obj[0]:null;
                    BigDecimal manager = (obj[1]!=null)?(BigDecimal)obj[1]:BigDecimal.ZERO;
                    vo.setIdEntreprise(cdos);
                    vo.setCodecrit(codefonc+""+addCodeFonc.intValue());
                    vo.setCodeeval(strCodeEval);
                    vo.setNmat(matricule);
                    vo.setLibcritere(competence1);
                    vo.setPoids(manager);
                    result.add(vo);
//					}

                    addCodeFonc = addCodeFonc.add(BigDecimal.ONE);
//					if(StringUtils.isNotEmpty(competence2)){
                    EvalcritnoteDto vo2 = new EvalcritnoteDto();
                    vo2.setIdEntreprise(cdos);
                    vo2.setCodecrit(codefonc+""+addCodeFonc.intValue());
                    vo2.setCodeeval(strCodeEval);
                    vo2.setNmat(matricule);
                    vo2.setLibcritere(competence2);
                    vo2.setPoids(manager);
                    result.add(vo2);
//					}

                    addCodeFonc = addCodeFonc.add(BigDecimal.ONE);
//					if(StringUtils.isNotEmpty(competence3)){
                    EvalcritnoteDto vo3 = new EvalcritnoteDto();
                    vo3.setIdEntreprise(cdos);
                    vo3.setCodecrit(codefonc+""+addCodeFonc.intValue());
                    vo3.setCodeeval(strCodeEval);
                    vo3.setNmat(matricule);
                    vo3.setLibcritere(competence3);
                    vo3.setPoids(manager);
                    result.add(vo3);
//					}

                    addCodeFonc = addCodeFonc.add(BigDecimal.ONE);
//					if(StringUtils.isNotEmpty(competence4)){
                    EvalcritnoteDto vo4 = new EvalcritnoteDto();
                    vo4.setIdEntreprise(cdos);
                    vo4.setCodecrit(codefonc+""+addCodeFonc.intValue());
                    vo4.setCodeeval(strCodeEval);
                    vo4.setNmat(matricule);
                    vo4.setLibcritere(competence4);
                    vo4.setPoids(manager);
                    result.add(vo4);
//					}
                }

            } catch (Exception e) {
                // TODO: handle exception
                return new ArrayList<EvalcritnoteDto>();
            } finally {
                service.closeSession(session);
            }
        }
        return result;
    }
}
