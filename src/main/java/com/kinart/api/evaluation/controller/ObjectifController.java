package com.kinart.api.evaluation.controller;

import com.kinart.api.evaluation.controller.api.ObjectifApi;
import com.kinart.api.evaluation.dto.ObjectifDto;
import com.kinart.organisation.business.model.Orgposteinfo;
import com.kinart.paie.business.model.ParamData;
import com.kinart.paie.business.model.Salarie;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class ObjectifController implements ObjectifApi {

    private GeneriqueConnexionService service;

    @Autowired
    public ObjectifController(GeneriqueConnexionService service) {
        this.service = service;
    }

    @Override
    public List<String> saveObjectifs(List<ObjectifDto> dtos) {
        List<String> result = new ArrayList<String>();

        String cpteurProcedure = null;
        BigDecimal nbMaxObjectif = new BigDecimal(6);
        int identreprise = dtos.get(0).getIdentreprise();

        ParamData cptPrc = service.findAnyColumnFromNomenclature(identreprise+"", null, "99", "CPTROBJF", "2");
        if(cptPrc!=null) cpteurProcedure = cptPrc.getVall();
        cptPrc = service.findAnyColumnFromNomenclature(identreprise+"", null, "99", "NBMAXOBJ", "2");
        if(cptPrc!=null) nbMaxObjectif = new BigDecimal(cptPrc.getValm());

        for(ObjectifDto vo : dtos){
            /* Ouverture de la session */
//			Session session = service.getSession();
            Date dt = new Date();
            /* Déclaration de l'objet transaction */
//			Transaction transaction = session.beginTransaction();

            try {
                String codeProc = null;
//                if(vo.getMode().equals(MODE_MODIFICATION)){
//                    codeProc = vo.getCodeProc();
////					session.createSQLQuery("DELETE FROM ParamData WHERE ctab=119 AND cacc='"+vo.getCodeProc()+"' AND cdos='"+getCdos()+"'").executeUpdate();
//                    service.deleteFromTable("DELETE FROM ParamData WHERE ctab=119 AND cacc='"+vo.getCodeProc()+"' AND cdos='"+getCdos()+"'");
//                } else
                    codeProc = getNumProcedure(identreprise, cpteurProcedure);

                // Enregistrement la procedure
                ParamData newProc = new ParamData();//new ParamDataPK(getCdos(), new Integer(119), codeProc, new Integer(1)), getUser(), new Date());
                newProc.setCtab(119);
                newProc.setCacc(codeProc);
                newProc.setNume(1);
                newProc.setVall(vo.getLibProc());
                newProc.setValm(new Long(vo.getNumProc()));
                newProc.setValt(new BigDecimal(vo.getNumOperation()));
                newProc.setDuti(vo.getUser());
                service.save(newProc);
//				if (StringUtils.equalsIgnoreCase(ClsParameter.getSessionObject(request, "TRACE"), ClsParameter.OUI))
//					ClsTrace.traceCreation(session, dt, ClsParameter.getSessionObject(request, ClsParameter.SESSION_LOGIN), "GPEC07EC", newProc);

                ParamData cycle = new ParamData();//new ParamData(new ParamDataPK(getCdos(), new Integer(119), codeProc, new Integer(2)), getUser(), new Date());
                cycle.setVall(vo.getCodeCycle());
                cycle.setCtab(119);
                cycle.setCacc(codeProc);
                cycle.setNume(2);
                cycle.setDuti(vo.getUser());
                service.save(cycle);

                ParamData indicateur = new ParamData();//new ParamData(new ParamDataPK(getCdos(), new Integer(119), codeProc, new Integer(3)), getUser(), new Date());
                indicateur.setVall(vo.getLibOperation());
                cycle.setCtab(119);
                indicateur.setCacc(codeProc);
                indicateur.setNume(3);
                indicateur.setDuti(vo.getUser());
                service.save(indicateur);

                ParamData cible = new ParamData();//new ParamData(new ParamDataPK(getCdos(), new Integer(119), codeProc, new Integer(4)), getUser(), new Date());
                cible.setVall(vo.getLibSsProc());
                cible.setCtab(119);
                cible.setCacc(codeProc);
                cible.setNume(4);
                cible.setDuti(vo.getUser());
                service.save(cible);

                ParamData activite = new ParamData();//new ParamData(new ParamDataPK(getCdos(), new Integer(119), codeProc, new Integer(5)), getUser(), new Date());
                activite.setVall(vo.getTache9());
                activite.setCtab(119);
                activite.setCacc(codeProc);
                activite.setNume(5);
                activite.setDuti(vo.getUser());
                service.save(activite);
//				if (StringUtils.equalsIgnoreCase(ClsParameter.getSessionObject(request, "TRACE"), ClsParameter.OUI))
//					ClsTrace.traceCreation(session, dt, ClsParameter.getSessionObject(request, ClsParameter.SESSION_LOGIN), "GPEC07EC", activite);

//				transaction.commit();
//				service.closeConnexion(session);

                //String autorisationagent = ClsAutorisationsUtilisateur._getChaineAutorisations(getRequest(), "", false);
                if(StringUtils.isNotEmpty(vo.getTache1())){
                    String query = "FROM Salarie WHERE identreprise='"+identreprise+"' AND (cals='O' OR mrrx != 'RA')";
                    String listeMatricule = "'1=1'";
                    if("SAL".equalsIgnoreCase(vo.getType1())){
//							query += " AND comp_id.nmat='"+vo.getValcible()+"'";
                        listeMatricule = listeMatricule + ",'"+vo.getTache1()+"'";
                    }
                    else if("AGE".equalsIgnoreCase(vo.getType1())) query += " AND codesite='"+vo.getTache1()+"'";
                    else if("DIR".equalsIgnoreCase(vo.getType1())) query += " AND niv1='"+vo.getTache1()+"'";
                    else if("DEP".equalsIgnoreCase(vo.getType1())) query += " AND niv2='"+vo.getTache1()+"'";
                    else if("SERV".equalsIgnoreCase(vo.getType1())) query += " AND niv3='"+vo.getTache1()+"'";
                    else if("FCT".equalsIgnoreCase(vo.getType1())) query += " AND fonc='"+vo.getTache1()+"'";
                    else if("CLS".equalsIgnoreCase(vo.getType1())) query += " AND clas='"+vo.getTache1()+"'";
                    else if("STAT".equalsIgnoreCase(vo.getType1())) query += " AND grad='"+vo.getTache1()+"'";
                    else if("SEX".equalsIgnoreCase(vo.getType1())) query += " AND sexe='"+vo.getTache1()+"'";
                    else if("CAT".equalsIgnoreCase(vo.getType1())) query += " AND cat='"+vo.getTache1()+"'";
                    else if("ECH".equalsIgnoreCase(vo.getType1())) query += " AND ech='"+vo.getTache1()+"'";
                    else if("BRP".equalsIgnoreCase(vo.getType1())) query += " AND equi='"+vo.getTache1()+"'";
                    else if("ZCT".equalsIgnoreCase(vo.getType1())) query += " AND afec='"+vo.getTache1()+"'";
                    else if("TPC".equalsIgnoreCase(vo.getType1())) query += " AND typc='"+vo.getTache1()+"'";
                    else if("STA".equalsIgnoreCase(vo.getType1())) query += " AND regi='"+vo.getTache1()+"'";
                    else if("POST".equalsIgnoreCase(vo.getType1())) query += " AND codeposte='"+vo.getTache1()+"'";
                    else if("EMPT".equalsIgnoreCase(vo.getType1())) query += " AND codeposte IN (SELECT codeposte FROM Orgposte WHERE fonc='"+vo.getTache1()+"' AND identreprise='"+identreprise+"')";

                    if(!listeMatricule.equalsIgnoreCase("'1=1'"))  query += " AND nmat in (" +listeMatricule+")";
                    //query += " AND "+autorisationagent;
                    List<Salarie> lstSalarie = (List<Salarie>)service.find(query);

                    if(lstSalarie!=null && lstSalarie.size()>0){
                        for(Salarie agent : lstSalarie){
                            String requete = "select info.identreprise "+
                                    "from Orgposteinfo info, ParamData typ "+
                                    "Where info.identreprise=typ.identreprise and info.codeinfo1=typ.cacc and typ.ctab=119 and typ.nume=2 and typ.vall='"+vo.getCodeCycle()+"' "+
                                    "and info.nmat='"+agent.getNmat()+"' and info.typeinfo='TASK' and info.codeinfo3='"+vo.getNumOperation()+"' and info.identreprise='"+identreprise+"'";
                            Session session = service.getSession();
                            List listeObjectifs = session.createSQLQuery(requete).list();
                            service.closeSession(session);
                            if(listeObjectifs!=null && listeObjectifs.size()>=nbMaxObjectif.intValue()){
                                result.add("Le matricule "+agent.getNmat()+" a déjà "+nbMaxObjectif.intValue()+" objectifs");
                            } else {
                                Orgposteinfo info = new Orgposteinfo();//new RhtevalagentinfoPK(getCdos(), agent.getComp_id().getNmat(), "TASK", codeProc));
                                info.setIdEntreprise(identreprise);
                                info.setCodeposte(agent.getNmat());
                                info.setCodeinfo1(codeProc);
                                info.setTypeinfo("TASK");
                                info.setCodeinfo3(vo.getNumOperation()+"");
                                info.setValminfo1(new BigDecimal(vo.getNumSsProc()));
                                service.save(info);
                            }
                        }
                    }
                }

                if(StringUtils.isNotEmpty(vo.getTache3())){
                    String query = "FROM Salarie WHERE identreprise='"+identreprise+"' AND (cals='O' OR mrrx != 'RA')";
                    String listeMatricule = "'1=1'";
                    if("SAL".equalsIgnoreCase(vo.getType1())){
//							query += " AND comp_id.nmat='"+vo.getValcible()+"'";
                        listeMatricule = listeMatricule + ",'"+vo.getTache3()+"'";
                    }
                    else if("AGE".equalsIgnoreCase(vo.getType2())) query += " AND codesite='"+vo.getTache3()+"'";
                    else if("DIR".equalsIgnoreCase(vo.getType2())) query += " AND niv1='"+vo.getTache3()+"'";
                    else if("DEP".equalsIgnoreCase(vo.getType2())) query += " AND niv2='"+vo.getTache3()+"'";
                    else if("SERV".equalsIgnoreCase(vo.getType2())) query += " AND niv3='"+vo.getTache3()+"'";
                    else if("FCT".equalsIgnoreCase(vo.getType2())) query += " AND fonc='"+vo.getTache3()+"'";
                    else if("CLS".equalsIgnoreCase(vo.getType2())) query += " AND clas='"+vo.getTache3()+"'";
                    else if("STAT".equalsIgnoreCase(vo.getType2())) query += " AND grad='"+vo.getTache3()+"'";
                    else if("SEX".equalsIgnoreCase(vo.getType2())) query += " AND sexe='"+vo.getTache3()+"'";
                    else if("CAT".equalsIgnoreCase(vo.getType2())) query += " AND cat='"+vo.getTache3()+"'";
                    else if("ECH".equalsIgnoreCase(vo.getType2())) query += " AND ech='"+vo.getTache3()+"'";
                    else if("BRP".equalsIgnoreCase(vo.getType2())) query += " AND equi='"+vo.getTache3()+"'";
                    else if("ZCT".equalsIgnoreCase(vo.getType2())) query += " AND afec='"+vo.getTache3()+"'";
                    else if("TPC".equalsIgnoreCase(vo.getType2())) query += " AND typc='"+vo.getTache3()+"'";
                    else if("STA".equalsIgnoreCase(vo.getType2())) query += " AND regi='"+vo.getTache3()+"'";
                    else if("POST".equalsIgnoreCase(vo.getType2())) query += " AND codeposte='"+vo.getTache3()+"'";
                    else if("EMPT".equalsIgnoreCase(vo.getType1())) query += " AND codeposte IN (SELECT codeposte FROM Orgposte WHERE fonc='"+vo.getTache1()+"' AND identreprise='"+identreprise+"')";

                    if(!listeMatricule.equalsIgnoreCase("'1=1'"))  query += " AND nmat in (" +listeMatricule+")";
                    //query += " AND "+autorisationagent;
                    List<Salarie> lstSalarie = (List<Salarie>)service.find(query);

                    if(lstSalarie!=null && lstSalarie.size()>0){
                        for(Salarie agent : lstSalarie){
                            String requete = "select info.identreprise "+
                                    "from Orgposteinfo info, ParamData typ "+
                                    "Where info.identreprise=typ.identreprise and info.codeinfo1=typ.cacc and typ.ctab=119 and typ.nume=2 and typ.vall='"+vo.getCodeCycle()+"' "+
                                    "and info.nmat='"+agent.getNmat()+"' and info.typeinfo='TASK' and info.codeinfo3='"+vo.getNumOperation()+"' and info.identreprise='"+identreprise+"'";
                            Session session = service.getSession();
                            List listeObjectifs = session.createSQLQuery(requete).list();
                            service.closeSession(session);
                            if(listeObjectifs!=null && listeObjectifs.size()>=nbMaxObjectif.intValue()){
                                result.add("Le matricule "+agent.getNmat()+" a déjà "+nbMaxObjectif.intValue()+" objectifs");
                            } else {
                                Orgposteinfo info = new Orgposteinfo();//new RhtevalagentinfoPK(getCdos(), agent.getComp_id().getNmat(), "TASK", codeProc));
                                info.setIdEntreprise(identreprise);
                                info.setCodeposte(agent.getNmat());
                                info.setCodeinfo1(codeProc);
                                info.setTypeinfo("TASK");
                                info.setCodeinfo3(vo.getNumOperation()+"");
                                info.setValminfo1(new BigDecimal(vo.getNumSsProc()));
                                service.save(info);
                            }
                        }
                    }
                }

                if(StringUtils.isNotEmpty(vo.getTache5())){
                    String query = "FROM Salarie WHERE identreprise='"+identreprise+"' AND (cals='O' OR mrrx != 'RA')";
                    String listeMatricule = "'1=1'";
                    if("SAL".equalsIgnoreCase(vo.getType1())){
//							query += " AND comp_id.nmat='"+vo.getValcible()+"'";
                        listeMatricule = listeMatricule + ",'"+vo.getTache5()+"'";
                    }
                    else if("AGE".equalsIgnoreCase(vo.getType3())) query += " AND codesite='"+vo.getTache5()+"'";
                    else if("DIR".equalsIgnoreCase(vo.getType3())) query += " AND niv1='"+vo.getTache5()+"'";
                    else if("DEP".equalsIgnoreCase(vo.getType3())) query += " AND niv2='"+vo.getTache5()+"'";
                    else if("SERV".equalsIgnoreCase(vo.getType3())) query += " AND niv3='"+vo.getTache5()+"'";
                    else if("FCT".equalsIgnoreCase(vo.getType3())) query += " AND fonc='"+vo.getTache5()+"'";
                    else if("CLS".equalsIgnoreCase(vo.getType3())) query += " AND clas='"+vo.getTache5()+"'";
                    else if("STAT".equalsIgnoreCase(vo.getType3())) query += " AND grad='"+vo.getTache5()+"'";
                    else if("SEX".equalsIgnoreCase(vo.getType3())) query += " AND sexe='"+vo.getTache5()+"'";
                    else if("CAT".equalsIgnoreCase(vo.getType3())) query += " AND cat='"+vo.getTache5()+"'";
                    else if("ECH".equalsIgnoreCase(vo.getType3())) query += " AND ech='"+vo.getTache5()+"'";
                    else if("BRP".equalsIgnoreCase(vo.getType3())) query += " AND equi='"+vo.getTache5()+"'";
                    else if("ZCT".equalsIgnoreCase(vo.getType3())) query += " AND afec='"+vo.getTache5()+"'";
                    else if("TPC".equalsIgnoreCase(vo.getType3())) query += " AND typc='"+vo.getTache5()+"'";
                    else if("STA".equalsIgnoreCase(vo.getType3())) query += " AND regi='"+vo.getTache5()+"'";
                    else if("POST".equalsIgnoreCase(vo.getType3())) query += " AND codeposte='"+vo.getTache3()+"'";
                    else if("EMPT".equalsIgnoreCase(vo.getType1())) query += " AND codeposte IN (SELECT codeposte FROM Orgposte WHERE fonc='"+vo.getTache1()+"' AND identreprise='"+identreprise+"')";

                    if(!listeMatricule.equalsIgnoreCase("'1=1'"))  query += " AND nmat in (" +listeMatricule+")";
                    //query += " AND "+autorisationagent;
                    List<Salarie> lstSalarie = (List<Salarie>)service.find(query);
                    if(lstSalarie!=null && lstSalarie.size()>0){
                        for(Salarie agent : lstSalarie){
                            String requete = "select info.identreprise "+
                                    "from Orgposteinfo info, ParamData typ "+
                                    "Where info.identreprise=typ.identreprise and info.codeinfo1=typ.cacc and typ.ctab=119 and typ.nume=2 and typ.vall='"+vo.getCodeCycle()+"' "+
                                    "and info.nmat='"+agent.getNmat()+"' and info.typeinfo='TASK' and info.codeinfo3='"+vo.getNumOperation()+"' and info.identreprise='"+identreprise+"'";
                            Session session = service.getSession();
                            List listeObjectifs = session.createSQLQuery(requete).list();
                            service.closeSession(session);
                            if(listeObjectifs!=null && listeObjectifs.size()>=nbMaxObjectif.intValue()){
                                result.add("Le matricule "+agent.getNmat()+" a déjà "+nbMaxObjectif.intValue()+" objectifs");
                            } else {
                                Orgposteinfo info = new Orgposteinfo();//new RhtevalagentinfoPK(getCdos(), agent.getComp_id().getNmat(), "TASK", codeProc));
                                info.setIdEntreprise(identreprise);
                                info.setCodeposte(agent.getNmat());
                                info.setCodeinfo1(codeProc);
                                info.setTypeinfo("TASK");
                                info.setCodeinfo3(vo.getNumOperation()+"");
                                info.setValminfo1(new BigDecimal(vo.getNumSsProc()));
                                service.save(info);
                            }
                        }
                    }
                }

                //vo.setCodeProc(codeProc);

            } catch (Exception e) {
                // TODO: handle exception
//				transaction.rollback();
//				service.closeConnexion(session);
                e.printStackTrace();
                result.add(e.getMessage());
            } finally {

            }
        }

        return result;
    }

    @Override
    public ResponseEntity<List<ObjectifDto>> findAll(Integer identreprise) {
        List<ObjectifDto> result = new ArrayList<ObjectifDto>();
        String sql = "select p.*, pc.vall typob, c.vall indicateur, d.vall cible, t.vall libtyp, s.vall codeact, r.vall libact  from ParamData p "+
                "left join ParamData pc on (p.identreprise=pc.identreprise and p.cacc=pc.cacc and pc.ctab=p.ctab and pc.nume=2) "+
                "left join ParamData t on (pc.identreprise=t.identreprise and t.cacc=pc.vall and t.ctab=118 and t.nume=1) "+
                "left join ParamData c on (p.identreprise=c.identreprise and p.cacc=c.cacc and c.ctab=p.ctab and c.nume=3) "+
                "left join ParamData d on (p.identreprise=d.identreprise and p.cacc=d.cacc and d.ctab=p.ctab and d.nume=4) "+
                "left join ParamData s on (p.identreprise=s.identreprise and p.cacc=s.cacc and p.ctab=s.ctab and s.nume=5) "+
                "left join ParamData r on (s.identreprise=r.identreprise and r.cacc=s.vall and r.ctab=744 and r.nume=1) "+
                "where p.ctab=119 and p.nume=1 and p.identreprise='"+identreprise+"'";

        sql += " ORDER BY p.cacc ASC";

        Session session  = service.getSession();
        Query query = session.createSQLQuery(sql).addEntity("p", ParamData.class).addScalar("typob",StandardBasicTypes.STRING).addScalar("indicateur",StandardBasicTypes.STRING)
                .addScalar("cible", StandardBasicTypes.STRING).addScalar("libtyp",StandardBasicTypes.STRING)
                .addScalar("codeact",StandardBasicTypes.STRING).addScalar("libact",StandardBasicTypes.STRING);

        List<Object[]> objs = query.list();
//						System.out.println("TAILLE : "+objs.size());
        if(objs == null || objs.size() == 0) return ResponseEntity.ok(result);

        try {
            for(Object[] obj:objs){
                ObjectifDto proc = new ObjectifDto();
                ParamData vo=(ParamData)obj[0];
                String typeobj = (obj[1]!=null)?(String)obj[1]:null;
                String indicat = (obj[2]!=null)?(String)obj[2]:null;
                String cibl = (obj[3]!=null)?(String)obj[3]:null;
                String valtypob = (obj[4]!=null)?(String)obj[4]:null;
                proc.setCodeCycle(typeobj);
                proc.setLibCycle(valtypob);
                proc.setLibOperation(indicat);
                proc.setLibSsProc(cibl);
                proc.setCodeProc(vo.getCacc());
                proc.setLibProc(vo.getVall());
                proc.setTache9((obj[5]!=null)?(String)obj[5]:null);
                proc.setTache10((obj[6]!=null)?(String)obj[6]:null);
                if(vo.getValm()!=null)
                    proc.setNumProc(vo.getValm().intValue());
                if(vo.getValt()!=null)
                    proc.setNumOperation(vo.getValt().intValue());
                result.add(proc);
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.fillInStackTrace();
            return ResponseEntity.ok(result);
        } finally {
            service.closeSession(session);
        }

        return ResponseEntity.ok(result);
    }

    private String getNumProcedure(int cdos, String codeCompteur) throws Exception
    {

        String prochainIncrement = service.getProchainIncrement(codeCompteur);
        String souche = service.soucheCompteur(cdos, codeCompteur);
        String numc = souche + prochainIncrement;
        // Contrôle de l'unicité de la clé dans les tables salarie (Candidats internes) et Rhpcandidat (Candidats externes/internes).
        ParamData cptPrc = service.findAnyColumnFromNomenclature(cdos+"", null, "99", numc, "1");
        if (cptPrc != null)
        {
            //Si le numéro existe déjà, on peut incrémenter le compteur pour prendre la prochaine valeur:
            service.incrementerCompteur(codeCompteur);
            numc = getNumProcedure(cdos, codeCompteur);
        }
        return numc;
    }
}
