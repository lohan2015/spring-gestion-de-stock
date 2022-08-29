package com.kinart.evaluation.business.service.impl;

import com.kinart.api.evaluation.dto.EvalcritnoteDto;
import com.kinart.api.evaluation.dto.ParamGenerateEvaluationDto;
import com.kinart.evaluation.business.model.*;
import com.kinart.evaluation.business.service.GenererEvaluationService;
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
        List<String> lstAgent = service.find("select nmat from Evalchampagent where identreprise='" + dto.getIdentreprise() + "' and codechamp='" + dto.getCodeChamp() + "'");

        if(lstModeles != null && lstModeles.size() > 0){
            String strCodeModele = null;
            Integer cmpModele = 0;
            for (Evalmodelchamp rhpmodelchamp : lstModeles)
            {
                if(lstAgent == null || lstAgent.size() == 0){
                    continue;
                }

                List<Evalcarrmodele> modeles = service.find("from Evalcarrmodele where identreprise='" + dto.getIdentreprise() + "' and codemodele='" + rhpmodelchamp.getCodemodel()+ "'");
                if(modeles!=null && modeles.size()>0){
                    Evalcarrmodele modele = modeles.get(0);
                    for (String nmat : lstAgent){

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
    
    public void suprressionEvaluationExistante(int cdos, String matricule, String codemodele)
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

            String query = null; //"Delete From Rhtevalobjectif where identreprise='" + cdos + "' and nmat='" + vo.getComp_id().getNmat() + "' and codeeval = '" + vo.getComp_id().getCodeeval() + "'";
//					session.createSQLQuery(query).executeUpdate();
            session.createSQLQuery("Delete From Evalobjectif where identreprise='" + cdos + "' and nmat='" + matricule + "' and codeeval = '" + codeeval + "'").executeUpdate();

//					query = "Delete From Rhtevalcritnote where identreprise='" + cdos + "' and nmat='" + vo.getComp_id().getNmat() + "' and codeeval = '" + vo.getComp_id().getCodeeval() + "'";
//					session.createSQLQuery(query).executeUpdate();
            session.createSQLQuery("Delete From Evalcritnote where identreprise='" + cdos + "' and nmat='" + matricule + "' and codeeval = '" + codeeval + "'").executeUpdate();

            session.createSQLQuery("Delete From Evalanal where identreprise='" + cdos + "' and nmat='" + matricule + "' and codeeval = '" + codeeval + "'").executeUpdate();

//			//					query = "Delete From Rhtevaluation where identreprise='" + cdos + "' and nmat='" + vo.getComp_id().getNmat() + "' and codeeval = '" + vo.getComp_id().getCodeeval() + "'";
//					session.createSQLQuery(query).executeUpdate();

//					service.delete(session, "From Rhtevaluation where identreprise='" + cdos + "' and nmat='" + vo.getComp_id().getNmat() + "' and codeeval = '" + vo.getComp_id().getCodeeval() + "'");
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
}
