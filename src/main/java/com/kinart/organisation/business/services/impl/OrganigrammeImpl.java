package com.kinart.organisation.business.services.impl;

import com.kinart.api.organisation.dto.OrganigrammeDto;
import com.kinart.organisation.business.model.Organigramme;
import com.kinart.organisation.business.repository.OrganigrammeRepository;
import com.kinart.organisation.business.services.OrganigrammeService;
import com.kinart.api.organisation.dto.RechercheListeOrganigrammeDto;
import com.kinart.paie.business.services.cloture.ClsNomenclature;
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

    @Autowired
    public OrganigrammeImpl(GeneriqueConnexionService service, OrganigrammeRepository repository){
        this.service = service;
        this.repository = repository;
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
        queryString += " Left join Organigramme b on a.cdos=b.cdos and b.codepere = a.codeorganigramme ";
        queryString += " Where a.cdos = :cdos ";

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

}
