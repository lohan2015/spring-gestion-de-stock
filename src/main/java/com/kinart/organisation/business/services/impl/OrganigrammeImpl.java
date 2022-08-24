package com.kinart.organisation.business.services.impl;

import com.kinart.api.organisation.dto.OperationOrganigrammeDto;
import com.kinart.api.organisation.dto.OrganigrammeDto;
import com.kinart.organisation.business.model.Organigramme;
import com.kinart.organisation.business.model.Orgniveau;
import com.kinart.organisation.business.repository.NiveauRepository;
import com.kinart.organisation.business.repository.OrganigrammeRepository;
import com.kinart.organisation.business.services.OrganigrammeService;
import com.kinart.api.organisation.dto.RechercheListeOrganigrammeDto;
import com.kinart.organisation.business.vo.ClsTemplate;
import com.kinart.paie.business.model.ParamData;
import com.kinart.paie.business.model.Salarie;
import com.kinart.paie.business.repository.SalarieRepository;
import com.kinart.paie.business.services.cloture.ClsNomenclature;
import com.kinart.paie.business.services.utils.ClsStringUtil;
import com.kinart.paie.business.services.utils.ClsTreater;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.kinart.paie.business.services.utils.TypeBDUtil;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.ErrorCodes;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OrganigrammeImpl implements OrganigrammeService {

    private OrganigrammeRepository repository;
    private GeneriqueConnexionService service;
    private NiveauRepository niveauRepository;
    private SalarieRepository salarieRepository;

    @Autowired
    public OrganigrammeImpl(GeneriqueConnexionService service, SalarieRepository salarieRepository, OrganigrammeRepository repository, NiveauRepository niveauRepository){
        this.service = service;
        this.repository = repository;
        this.niveauRepository = niveauRepository;
        this.salarieRepository = salarieRepository;
    }

    @Override
    public OrganigrammeDto save(OrganigrammeDto dto) {
        return OrganigrammeDto.fromEntity(
                repository.save(
                        OrganigrammeDto.toEntity(dto)
                )
        );
    }

    @Override
    public OrganigrammeDto findById(Integer id) {
        return repository.findById(id).map(OrganigrammeDto::fromEntity).orElseThrow(() ->
                new EntityNotFoundException(
                        "Aucune cellule avec l'ID = " + id + " n'a été trouvée dans la BDD",
                        ErrorCodes.ARTICLE_NOT_FOUND));
    }

    @Override
    public List<OrganigrammeDto> findAll(RechercheListeOrganigrammeDto search) {
        List<OrganigrammeDto> listOfDataToDisplay = new ArrayList<OrganigrammeDto>();

        String queryString = getQueryString(search);
        try
        {
            Session session = service.getSession();

            Query q = session.createSQLQuery(queryString).addEntity("a", Organigramme.class).addScalar("libelleniveau", StandardBasicTypes.STRING).addScalar("libellesite", StandardBasicTypes.STRING).addScalar("nmat", StandardBasicTypes.STRING).addScalar("nomagent",
                    StandardBasicTypes.STRING).addScalar("prenomagent", StandardBasicTypes.STRING);

            this.setQueryParameters(q, search);

            List<Object[]> liste = q.list();

            service.closeSession(session);

            OrganigrammeDto vo = null;

            Organigramme org;

            Map<String, Integer> map = null;
            Map<String, Integer> maptotal = null;
//            if (showChildNumber)
//            {
                map = this.buildMapOfChildNumber(service, search);
                maptotal = this.buildMapOfTotalChildNumber(service, search);
            //}
            for (Object[] o : liste)
            {
                vo = new OrganigrammeDto();
                vo.setNomagent(StringUtils.EMPTY);
                if (o[0] != null)
                {
                    org = (Organigramme) o[0];
                    BeanUtils.copyProperties(org, vo);
                }

                if (o[1] != null)
                    vo.setLibelleniveau((String) o[1]);

                if (o[2] != null)
                    vo.setLibellesite((String) o[2]);

                if (o[3] != null)
                    vo.setNmat((String) o[3]);

                if (o[4] != null)
                    vo.setNomagent((String) o[4]);

                if (o[5] != null)
                    vo.setNomagent(vo.getNomagent() + " " + (String) o[5]);

//                if (showChildNumber)
//                {
                    vo.setNombrefils(map.get(vo.getCodeorganigramme()));
                    vo.setNombrefilstotal(maptotal.get(vo.getCodeorganigramme()));
                //}

                vo.setLibelleorganigramme(vo.getLibelle());

                //vo.setMode(Mode.CONSULTATION);
                listOfDataToDisplay.add(vo);
            }

            service.closeSession(session);
        }
        catch (HibernateException e)
        {
            e.printStackTrace();
        }
        catch (BeansException e)
        {
            e.printStackTrace();
        }
        return listOfDataToDisplay;
    }

    @Override
    public void delete(String codeOrganigramme) {
        Session sess = service.getSession();
        Transaction tx = null;

        try
        {
            tx = sess.beginTransaction();

                String strSQLPere="Delete From Organigramme  where codeorganigramme = " + "'" + codeOrganigramme + "'";
                String strSQLFils="Delete From Organigramme  where codepere = " + "'" + codeOrganigramme +"'";
                String strSQLPrestataires="Delete From Organigrammeassocie  where codeorganigramme like '" + codeOrganigramme + "%'";
                sess.createSQLQuery(strSQLPere).executeUpdate();
                sess.createSQLQuery(strSQLFils).executeUpdate();
                sess.createSQLQuery(strSQLPrestataires).executeUpdate();

            tx.commit();
        }
        catch (Exception e)
        {
            if (tx != null)
                tx.rollback();

            e.printStackTrace();

        }
        finally
        {
            service.closeSession(sess);
        }
    }

    @Override
    public boolean isCelluleExist(String codeOrganigramme) {
        List<Organigramme> listeCellules = repository.findCelluleByCode(codeOrganigramme);
        if(listeCellules!=null && listeCellules.size()>=1)return true;
        return false;
    }

    @Override
    public String getPossibilites(String codePere) {
        ParamData data = null;
        int nbr = 0;
        List<ParamData> list = service.find("from ParamData " + " where ctab=266 and nume=2 and cacc='FCNOTUSED'");
        if (list != null && list.size() > 0)
        {
            data = list.get(0);
            nbr = Integer.parseInt(data.getVall());
        }
        List<String> posibilites = new ArrayList<String>();
        List<String> posibilitesend = new ArrayList<String>();
        for(int i=1; i< 100; i++)
            posibilites.add(ClsStringUtil.formatNumber(i, "00"));

        String numerovide = StringUtils.EMPTY;
        List<Organigramme> listeCellules = repository.findCellulePereByCode(codePere);
        //if(listeCellules!=null && listeCellules.size()>=0){
            String codeorg;
            for (Organigramme org : listeCellules)
            {
                codeorg = StringUtils.substring( org.getCodeorganigramme(), codePere.length());
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
        //}
        return numerovide;
    }

    private String getQueryString(RechercheListeOrganigrammeDto search)
    {

        String queryString = "Select a.*, b.libelle as libelleniveau, c.vall as libellesite, d.nmat as nmat, d.nom as nomagent, d.pren as prenomagent From Organigramme a ";
        queryString += " Left join Orgniveau b on a.identreprise=b.identreprise and a.codeniveau=b.codeniveau";
        queryString += " Left join ParamData c on a.identreprise=c.identreprise and a.codesite=c.cacc and c.nume =:nume1 and c.ctab = :ctab1";
        queryString += " Left join Salarie d on a.identreprise=d.identreprise and a.codeposte=d.codeposte";
        queryString += " Where a.identreprise = :cdos ";

        queryString = getWherePart(search, queryString, false);

        return queryString;
    }

    private String getWherePart(RechercheListeOrganigrammeDto search, String queryString, boolean addGroupBy)
    {
        if (StringUtils.isNotBlank(search.code))
            queryString += " and UPPER(a.codeorganigramme) like :code ";

        if (StringUtils.isNotBlank(search.libelle))
            queryString += " and UPPER(a.libelle) like :libelle ";

        if (StringUtils.isNotBlank(search.niveau))
            queryString += " and UPPER(a.codeniveau)  = :niveau ";

        if (StringUtils.isNotBlank(search.site))
            queryString += " and UPPER(a.codesite)  = :site ";

        if (StringUtils.isNotBlank(search.fictive))
            queryString += " and UPPER(a.bcasefictive)  = :fictive ";

        if (StringUtils.isNotBlank(search.prestataire))
            queryString += " and UPPER(a.bprestataire)  = :prestataire ";

        if (addGroupBy)
            queryString += " group by a.codeorganigramme";

        queryString += " order by a.codeorganigramme";

        return queryString;
    }

    private void setQueryParameters(Query query, RechercheListeOrganigrammeDto search)
    {
        query.setParameter("nume1", 1, StandardBasicTypes.INTEGER);

        query.setParameter("ctab1", ClsNomenclature.SITE_TRAVAIL, StandardBasicTypes.STRING);

        setWhereParQueryParameters(query, search);
    }

    private void setWhereParQueryParameters(Query query, RechercheListeOrganigrammeDto search)
    {
        query.setParameter("cdos", search.cdos, StandardBasicTypes.STRING);

//		if (StringUtils.isNotBlank(search.code))
//			query.setParameter("code", "%" + search.code.toUpperCase() + "%", StandardBasicTypes.STRING);
        if (StringUtils.isNotBlank(search.code))
            query.setParameter("code", search.code.toUpperCase() + "%", StandardBasicTypes.STRING);

        if (StringUtils.isNotBlank(search.libelle))
            query.setParameter("libelle", "%" + search.libelle.toUpperCase() + "%", StandardBasicTypes.STRING);

        if (StringUtils.isNotBlank(search.niveau))
            query.setParameter("niveau", search.niveau.toUpperCase(), StandardBasicTypes.STRING);

        if (StringUtils.isNotBlank(search.site))
            query.setParameter("site", search.site.toUpperCase(), StandardBasicTypes.STRING);

        if (StringUtils.isNotBlank(search.fictive))
            query.setParameter("fictive", search.fictive.toUpperCase(), StandardBasicTypes.STRING);

        if (StringUtils.isNotBlank(search.prestataire))
            query.setParameter("prestataire", search.prestataire.toUpperCase(), StandardBasicTypes.STRING);
    }

    private String getCountChildQueryString(RechercheListeOrganigrammeDto search)
    {

        String queryString = "Select count(b.codeorganigramme) as nombrefils, a.codeorganigramme as codeorg From Organigramme a ";
        queryString += " Left join Organigramme b on a.identreprise=b.identreprise and b.codepere = a.codeorganigramme ";
        queryString += " Where a.identreprise = :cdos ";

        queryString = getWherePart(search, queryString, true);

        return queryString;
    }

    private String getCountTotalChildQueryString(RechercheListeOrganigrammeDto search)
    {

        String queryString = "Select count(b.codeorganigramme) as nombrefils, a.codeorganigramme as codeorg From Organigramme a ";
        queryString += " Left join Organigramme b on a.identreprise=b.identreprise and b.codepere like "+ TypeBDUtil.contatener("a.codeorganigramme","'%'");
        queryString += " Where a.identreprise = :cdos ";


        queryString = getWherePart(search, queryString, true);

        return queryString;
    }


    @SuppressWarnings("unchecked")
    private Map<String, Integer> buildMapOfChildNumber(GeneriqueConnexionService service, RechercheListeOrganigrammeDto search)
    {
        Map<String, Integer> map = new HashMap<String, Integer>();

        String queryString = getCountChildQueryString(search);

        Session session = null;

        try
        {
            session = service.getSession();

            Query q = session.createSQLQuery(queryString).addScalar("nombrefils", StandardBasicTypes.INTEGER).addScalar("codeorg", StandardBasicTypes.STRING);

            setWhereParQueryParameters(q, search);

            List<Object[]> liste = q.list();
            for (Object[] obj : liste)
            {
                map.put((String) obj[1], Integer.parseInt((obj[0].toString())));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            service.closeSession(session);
        }
        return map;
    }

    private Map<String, Integer> buildMapOfTotalChildNumber(GeneriqueConnexionService service, RechercheListeOrganigrammeDto search)
    {
        Map<String, Integer> map = new HashMap<String, Integer>();

        String queryString = getCountTotalChildQueryString(search);

        Session session = null;

        try
        {
            session = service.getSession();

            Query q = session.createSQLQuery(queryString).addScalar("nombrefils", StandardBasicTypes.INTEGER).addScalar("codeorg", StandardBasicTypes.STRING);

            setWhereParQueryParameters(q, search);

            List<Object[]> liste = q.list();
            service.closeSession(session);

            for (Object[] obj : liste)
            {
                map.put((String) obj[1], Integer.parseInt((obj[0].toString())));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            service.closeSession(session);
        }
        return map;
    }

    public String getLegende(String codeDOssier){
        String strLegend = null;
        try
        {
            List<Orgniveau> oLevelCollection = niveauRepository.findLevelForLegende(codeDOssier); // service.find("FROM Orgniveau WHERE priseencomptecouleur = 'O' AND identreprise = " + "'" + codeDOssier + "'");
            if (oLevelCollection != null && oLevelCollection.size() > 0)
            {

                String strLegende = ClsTreater._getResultat("Legende", "INF-80161", false).getLibelle();

                strLegend = "<table style='border-bottom-width: 2px; border-right-width: 2px; border-left-width: 2px; border-top-width: 2px' width='100%'  bgcolor='#D9FFFF'>";
                strLegend += "<tr><td nowrap='nowrap'><span style='color: #6e6e6e; font-family: Verdana; font-weight:bold; font-size: 9px;'>"
                        + strLegende
                        + "----------------------------------------------------------------------------------------------------------------------------------------------------------</span></td></tr></table>";

                strLegend += ClsTemplate.STR_FLOWCHART_LEG_HEAD_START;

                for (Orgniveau oLevel : oLevelCollection)
                {
                    strLegend += ClsTemplate._getFlowChartLegElement(oLevel.getCodecouleur(), oLevel.getLibelle());
                }

                strLegend += ClsTemplate._getFlowChartLegElement("#0033FF", "Par défaut");

                strLegend += ClsTemplate.STR_FLOWCHART_LEG_HEAD_CLOSE;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return strLegend;
    }

    public List<String> controleAffectationPoste(OperationOrganigrammeDto dto){
        List<String> result = new ArrayList<String>();
        if(StringUtils.isNotEmpty(dto.getCodeposte())){
            List<Organigramme> org = repository.findCelluleByCodePoste(dto.getCodeposte());
            if (org != null && org.size()>=1)
            {
                Organigramme entity = org.get(0);
                if(StringUtils.isNotEmpty(entity.getCodeorganigramme())){
                    if(!dto.getCodeorganigramme().equalsIgnoreCase(entity.getCodeorganigramme()))
                        result.add("Ce poste est déja affecté à la cellule "+entity.getCodeorganigramme()+"-"+entity.getLibelle());
                    else result.add("Affectation déjà effectuée");
                }
            }
        }
        if(StringUtils.isNotEmpty(dto.getCodeorganigramme())){
            List<Organigramme> org = repository.findCelluleByCode(dto.getCodeorganigramme());
            if (org != null && org.size()>=1)
            {
                Organigramme entity = org.get(0);
                if(StringUtils.isNotEmpty(entity.getCodeposte())){
                    if(!dto.getCodeposte().equalsIgnoreCase(entity.getCodeposte()))
                        result.add("Cet organigramme est déja affecté au poste "+entity.getCodeposte());
                    else result.add("Affectation déjà effectuée");
                }
            }
        }
        if(result.size()==0) result.add("Aucune affectation");
        return result;
    }

    public List<String> affectationPosteOrganigramme(OperationOrganigrammeDto dto){
        List<String> result = controleAffectationPoste(dto);
//        if (result != null && result.size()>=1) {
//            return result;
//        }

        result = new ArrayList<String>();

                Session sess = this.service.getSession();
        Transaction tx = null;
        try
        {
            tx = sess.beginTransaction();
            List<Organigramme> org = repository.findCelluleByCode(dto.getCodeorganigramme());
            if (org != null)
            {
                Organigramme entity = org.get(0);
                entity.setCodeposte(dto.getCodeposte());
                sess.update(entity);

                String strSQL = "Update Organigramme set codeposte = null where identreprise='" + dto.getIdentreprise() + "' and codeorganigramme != '" + dto.getCodeorganigramme() + "'  and codeposte = '" + dto.getCodeposte() + "'";
                sess.createSQLQuery(strSQL).executeUpdate();
            }
            tx.commit();

            result.add("Mise à jour éffectuée avec succès");
        }
        catch (Exception ex)
        {
            if (tx != null)
                tx.rollback();
            ex.printStackTrace();
        }
        finally
        {
            this.service.closeSession(sess);
        }

        return result;
    }

    public List<String> controleAffectationSalarie(OperationOrganigrammeDto dto){
        List<String> result = new ArrayList<String>();
        if(StringUtils.isNotEmpty(dto.getCodeposte())){
            Salarie sal = salarieRepository.findByCodeposteExactly(dto.getCodeposte());
            if (sal != null)
            {
                if(StringUtils.isNotEmpty(sal.getNmat())){
                    if(!dto.getMatricule().equalsIgnoreCase(sal.getNmat()))
                        result.add("Ce poste est déja affecté au salarié "+sal.getNmat()+"-"+sal.getNom());
                    else result.add("Affectation déjà effectuée");
                }
            }
        }
        if(StringUtils.isNotEmpty(dto.getMatricule())){
            Salarie sal = salarieRepository.findByMatriculeExactly(dto.getMatricule());
            if (sal != null)
            {
                if(StringUtils.isNotEmpty(sal.getCodeposte())){
                    if(!dto.getCodeposte().equalsIgnoreCase(sal.getCodeposte()))
                        result.add("Cet agent est déja affecté au poste "+sal.getCodeposte());
                    else result.add("Affectation déjà effectuée");
                }
            }
        }
        if(result.size()==0) result.add("Aucune affectation");
        return result;
    }

    public List<String> affectationSalariePoste(OperationOrganigrammeDto dto){
        List<String> result = controleAffectationSalarie(dto);
//        if (result != null && result.size()>=1) {
//            return result;
//        }

        result = new ArrayList<String>();

        Session sess = this.service.getSession();
        Transaction tx = null;
        try
        {
            tx = sess.beginTransaction();
            Salarie sal = salarieRepository.findByCodeposteExactly(dto.getCodeposte());
            if (sal != null)
            {
                if(StringUtils.isEmpty(dto.getMatricule()))
                    sal.setCodeposte("");
                else sal.setCodeposte(dto.getCodeposte());
                sess.update(sal);

                String strSQL = "Update Salarie set codeposte = null where identreprise='" + dto.getIdentreprise() + "' and nmat != '" + dto.getMatricule() + "'  and codeposte = '" + dto.getCodeposte() + "'";
                sess.createSQLQuery(strSQL).executeUpdate();
            }
            tx.commit();

            result.add("Mise à jour éffectuée avec succès");
        }
        catch (Exception ex)
        {
            if (tx != null)
                tx.rollback();
            ex.printStackTrace();
        }
        finally
        {
            this.service.closeSession(sess);
        }

        return result;
    }

    public List<String> rattacherCellules(OperationOrganigrammeDto dto){
        List<String> result = new ArrayList<String>();
        String org1 = dto.getCodeorganigramme();
        String org2 = dto.getCodeorganigramme2();

        String cdos = dto.getIdentreprise().intValue()+"";

        Session session = this.service.getSession();
        Transaction tx = null;

        String newCode = null;
        try
        {
            tx = session.beginTransaction();

            int maxFils = _getMaxFils(cdos, org2) + 1;
            if (maxFils < 10)
                newCode = org2 + "0" + maxFils;
            else {
                newCode = org2 + maxFils;
            }
            updateOneCell(cdos, org1, newCode, true, org2, session);

            tx.commit();

            result.add("Mise à jour éffectuée avec succès");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            if (tx != null)
                tx.rollback();
        }
        finally
        {
            this.service.closeSession(session);
        }

        return null;
    }

    private int _getMaxFils(String cdos, String codeorganigramme)
    {
        String query = "Select max(codeorganigramme) from Organigramme where identreprise='" + cdos + "' and codepere='" + codeorganigramme + "'";
        List lst = this.service.find(query);
        if ((!lst.isEmpty()) && (lst.get(0) != null))
        {
            String maxOrg = lst.get(0).toString();
            maxOrg = StringUtils.substring(maxOrg, codeorganigramme.length(), maxOrg.length());
            return Integer.parseInt(maxOrg);
        }

        return 0;
    }

    private List<Organigramme> updateOneCell(String cdos, String oldCodeOrganigramme, String newCodeOrganigramme, boolean updatePereFirsCell, String firstCellCodepere, Session session)
            throws Exception
    {
        try
        {
            String updateString = "Update Organigramme set codeorganigramme = '" + newCodeOrganigramme + "'";
            if (updatePereFirsCell) {
                updateString = updateString + " , codepere='" + firstCellCodepere + "'";
            }
            updateString = updateString + " where identreprise='" + cdos + "' and codeorganigramme ='" + oldCodeOrganigramme + "'";

            session.createSQLQuery(updateString).executeUpdate();

//      List<Rhtorganigramme> filss = session.createQuery("Select a From Rhtorganigramme a where comp_id.cdos='" + cdos + "' and codepere like '" + oldCodeOrganigramme + "%'").list();
            List<Organigramme> filsss = new ArrayList();

            String sql = "select codepere, codeorganigramme "+
                    "from Organigramme "+
                    "where identreprise='" + cdos + "' and codepere like '" + oldCodeOrganigramme + "%'";

            Query query = session.createSQLQuery(sql);
            List<Object[]> objs = query.list();
            if(objs == null || objs.size() == 0) return filsss;

            String codeOrgFils = null;

            String subCodeOrgFils = null;

            String codePereFils = null;

            String subCodePereFils = null;
            int i = 0;

//      for (Rhtorganigramme fils:filss)
            for (Object[] obj:objs)
            {
                i++;

                codePereFils = (obj[0]!=null)?(String)obj[0]:null;
                codeOrgFils = (obj[1]!=null)?(String)obj[1]:null;
//		System.out.println("TRaitement :"+codePereFils);
//		Rhtorganigramme fils = (Rhtorganigramme)service.get(Rhtorganigramme.class, new RhtorganigrammePK(cdos, codeOrgFils));
//		if(fils==null) continue;

//        codeOrgFils = fils.getComp_id().getCodeorganigramme();

                subCodeOrgFils = codeOrgFils.substring(oldCodeOrganigramme.length(), codeOrgFils.length());

//        codePereFils = fils.getCodepere();

                subCodePereFils = codePereFils.substring(oldCodeOrganigramme.length(), codePereFils.length());

                updateString = "Update Organigramme set codeorganigramme = '" + newCodeOrganigramme + subCodeOrgFils + "', codepere = '" + newCodeOrganigramme + subCodePereFils + "' where identreprise='" + cdos +
                        "' and codeorganigramme ='" + codeOrgFils + "'";

                session.createSQLQuery(updateString).executeUpdate();

                if (i % 20 == 0)
                {
                    session.flush();
                    session.clear();
                }

//        fils.getComp_id().setCodeorganigramme(newCodeOrganigramme + subCodeOrgFils);
//
//        fils.setCodepere(newCodeOrganigramme + subCodePereFils);
//
//        filsss.add(fils);
            }
            return filsss;
        }
        catch (HibernateException e)
        {
            e.printStackTrace();
            throw e;
        }
        catch (DataAccessException e)
        {
            e.printStackTrace();
            throw e;
        }
    }
}
