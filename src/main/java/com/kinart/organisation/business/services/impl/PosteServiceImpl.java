package com.kinart.organisation.business.services.impl;

import com.kinart.api.organisation.dto.PosteDto;
import com.kinart.api.organisation.dto.PosteinfoDto;
import com.kinart.organisation.business.model.Orgposte;
import com.kinart.organisation.business.repository.PosteRepository;
import com.kinart.organisation.business.repository.PosteinfoRepository;
import com.kinart.organisation.business.services.PosteService;
import com.kinart.organisation.business.validator.PosteValidator;
import com.kinart.paie.business.services.cloture.ClsNomenclature;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.kinart.paie.business.services.utils.StringUtil;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.ErrorCodes;
import com.kinart.stock.business.exception.InvalidEntityException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PosteServiceImpl implements PosteService {

    private PosteRepository repository;
    private PosteinfoRepository infoRepository;
    private GeneriqueConnexionService service;

    @Autowired
    public PosteServiceImpl(PosteRepository repository, GeneriqueConnexionService service, PosteinfoRepository infoRepository){
        this.service = service;
        this.repository = repository;
        this.infoRepository = infoRepository;
    }

    @Override
    public PosteDto save(PosteDto dto) {
        List<String> errors = PosteValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Poste non valide {}", dto);
            throw new InvalidEntityException("Le poste n'est pas valide", ErrorCodes.ARTICLE_NOT_VALID, errors);
        }

        //Sauvegarde des informations des compétences
        infoRepository.deleteInfoByCodeposte(dto.getCodeposte());
        for(PosteinfoDto info : dto.getSavoirs()){
            infoRepository.save(PosteinfoDto.toEntity(info));
        }
        for(PosteinfoDto info : dto.getSavoirsEtres()){
            infoRepository.save(PosteinfoDto.toEntity(info));
        }
        for(PosteinfoDto info : dto.getSavoirsFaires()){
            infoRepository.save(PosteinfoDto.toEntity(info));
        }

        //Sauvegarde des informations principales
        return PosteDto.fromEntity(
                repository.save(
                        PosteDto.toEntity(dto)
                )
        );
    }

    @Override
    public PosteDto findById(Integer id) {
        // Chargement du poste
        PosteDto poste = repository.findById(id).map(PosteDto::fromEntity).orElseThrow(() ->
                        new EntityNotFoundException(
                                "Aucun poste avec l'ID = " + id + " n'a été trouvé dans la BDD",
                                ErrorCodes.ARTICLE_NOT_FOUND));

        //Chargement de l'emploi type
        List<Orgposte> emploiTypeListe = repository.findEmploiTypeByCOde(poste.getFonc());
        if(emploiTypeListe!=null && emploiTypeListe.size()>0)
            poste.setLibelleemploitype(emploiTypeListe.get(0).getLibelle());

        //Chargement des libellé
        initLibelles(poste);

        //Chargement de la hiérarchie
        loadHierarchie(poste);

        return poste;
    }

    @Override
    public List<PosteDto> findAllPoste(String codeDossier){
        String queryString = "Select a.*, b.vall as libellefiliere, c.vall as libellespecialite, e.libelle as libelleorganigramme, e.codeorganigramme as organigramme2, i.libelle as libelleemploitype";
        queryString+=" From Orgposte a ";
        queryString += " Left join ParamData b on a.identreprise=b.identreprise and a.cdfi=b.cacc and b.nume = :nume1 and b.ctab = :ctab1";
        queryString += " Left join ParamData c on a.identreprise=c.identreprise and a.cdsp=c.cacc and c.nume = :nume1 and c.ctab = :ctab2";
        queryString += " Left join ParamData d on a.identreprise=d.identreprise and a.cat=d.cacc and d.nume = :nume1 and d.ctab = :ctab3";
        queryString += " Left join Organigramme e on a.identreprise=e.identreprise and a.codeposte=e.codeposte";
        queryString += " Left join ParamData f on a.identreprise=f.identreprise and a.niv1=f.cacc and f.nume = :nume1 and f.ctab = :ctab4";
        queryString += " Left join ParamData g on a.identreprise=g.identreprise and a.niv2=g.cacc and g.nume = :nume1 and g.ctab = :ctab5";
        queryString += " Left join ParamData h on a.identreprise=h.identreprise and a.niv3=h.cacc and h.nume = :nume1 and h.ctab = :ctab6";
        queryString += " Left join Orgposte i on a.identreprise=i.identreprise and a.fonc=i.codeposte";
        queryString += " Left join ParamData j on a.identreprise=j.identreprise and a.codesite=j.cacc and j.nume = :nume1 and j.ctab = :ctab7";
        queryString += " Where a.identreprise = '"+codeDossier+"' and a.fonc is not null ";
        queryString += " order by a.codeposte";

        List<PosteDto> liste = new ArrayList<PosteDto>();

        try {
            Session session = service.getSession();
            Query query  = session.createSQLQuery(queryString)
                    .addEntity("a", Orgposte.class)
                    .addScalar("libellefiliere", StandardBasicTypes.STRING).addScalar("libellespecialite", StandardBasicTypes.STRING)
                    .addScalar("libelleorganigramme", StandardBasicTypes.STRING)
                    .addScalar("organigramme2", StandardBasicTypes.STRING).addScalar("libelleemploitype", StandardBasicTypes.STRING);

            query.setParameter("nume1", 1, StandardBasicTypes.INTEGER);
            query.setParameter("ctab1", Integer.parseInt(ClsNomenclature.FILIERE), StandardBasicTypes.INTEGER);
            query.setParameter("ctab2", Integer.parseInt(ClsNomenclature.SPECIALITE), StandardBasicTypes.INTEGER);
            query.setParameter("ctab3", Integer.parseInt(ClsNomenclature.CATEGORIE), StandardBasicTypes.INTEGER);
            query.setParameter("ctab4", Integer.parseInt(ClsNomenclature.NIVEAU1), StandardBasicTypes.INTEGER);
            query.setParameter("ctab5", Integer.parseInt(ClsNomenclature.NIVEAU2), StandardBasicTypes.INTEGER);
            query.setParameter("ctab6", Integer.parseInt(ClsNomenclature.NIVEAU3), StandardBasicTypes.INTEGER);
            query.setParameter("ctab7", Integer.parseInt(ClsNomenclature.SITE_TRAVAIL), StandardBasicTypes.INTEGER);


            List<Object[]> lst = query.getResultList();
            service.closeSession(session);

            for (Object[] o : lst)
            {
                Orgposte evDB = (Orgposte)o[0];
                PosteDto evDto = PosteDto.fromEntity(evDB);
                if(o[1]!=null) evDto.setLibellecdfi(o[1].toString());
                if(o[2]!=null) evDto.setLibellecdsp(o[2].toString());
                if(o[3]!=null) evDto.setLibelleorganigramme(o[3].toString());
                if(o[4]!=null) evDto.setCodeorganigramme(o[4].toString());
                if(o[5]!=null) evDto.setLibelleemploitype(o[5].toString());

                liste.add(evDto);
            }

        } catch (Exception e){
            throw e;
        }
        return liste;
    }

    @Override
    public List<PosteDto> findAllMetier(String codeDossier) {
        String queryString = "Select a.*, b.vall as libellefiliere, c.vall as libellespecialite, e.libelle as libelleorganigramme, e.codeorganigramme as organigramme2, i.libelle as libelleemploitype";
        queryString+=" From Orgposte a ";
        queryString += " Left join ParamData b on a.identreprise=b.identreprise and a.cdfi=b.cacc and b.nume = :nume1 and b.ctab = :ctab1";
        queryString += " Left join ParamData c on a.identreprise=c.identreprise and a.cdsp=c.cacc and c.nume = :nume1 and c.ctab = :ctab2";
        queryString += " Left join ParamData d on a.identreprise=d.identreprise and a.cat=d.cacc and d.nume = :nume1 and d.ctab = :ctab3";
        queryString += " Left join Organigramme e on a.identreprise=e.identreprise and a.codeposte=e.codeposte";
        queryString += " Left join ParamData f on a.identreprise=f.identreprise and a.niv1=f.cacc and f.nume = :nume1 and f.ctab = :ctab4";
        queryString += " Left join ParamData g on a.identreprise=g.identreprise and a.niv2=g.cacc and g.nume = :nume1 and g.ctab = :ctab5";
        queryString += " Left join ParamData h on a.identreprise=h.identreprise and a.niv3=h.cacc and h.nume = :nume1 and h.ctab = :ctab6";
        queryString += " Left join Orgposte i on a.identreprise=i.identreprise and a.fonc=i.codeposte";
        queryString += " Left join ParamData j on a.identreprise=j.identreprise and a.codesite=j.cacc and j.nume = :nume1 and j.ctab = :ctab7";
        queryString += " Where a.identreprise = '"+codeDossier+"' and a.fonc is null ";
        queryString += " order by a.codeposte";

        List<PosteDto> liste = new ArrayList<PosteDto>();

        try {
            Session session = service.getSession();
            Query query  = session.createSQLQuery(queryString)
                    .addEntity("a", Orgposte.class)
                    .addScalar("libellefiliere", StandardBasicTypes.STRING).addScalar("libellespecialite", StandardBasicTypes.STRING)
                    .addScalar("libelleorganigramme", StandardBasicTypes.STRING)
                    .addScalar("organigramme2", StandardBasicTypes.STRING).addScalar("libelleemploitype", StandardBasicTypes.STRING);

            query.setParameter("nume1", 1, StandardBasicTypes.INTEGER);
            query.setParameter("ctab1", Integer.parseInt(ClsNomenclature.FILIERE), StandardBasicTypes.INTEGER);
            query.setParameter("ctab2", Integer.parseInt(ClsNomenclature.SPECIALITE), StandardBasicTypes.INTEGER);
            query.setParameter("ctab3", Integer.parseInt(ClsNomenclature.CATEGORIE), StandardBasicTypes.INTEGER);
            query.setParameter("ctab4", Integer.parseInt(ClsNomenclature.NIVEAU1), StandardBasicTypes.INTEGER);
            query.setParameter("ctab5", Integer.parseInt(ClsNomenclature.NIVEAU2), StandardBasicTypes.INTEGER);
            query.setParameter("ctab6", Integer.parseInt(ClsNomenclature.NIVEAU3), StandardBasicTypes.INTEGER);
            query.setParameter("ctab7", Integer.parseInt(ClsNomenclature.SITE_TRAVAIL), StandardBasicTypes.INTEGER);

            List<Object[]> lst = query.getResultList();
            service.closeSession(session);

            for (Object[] o : lst)
            {
                Orgposte evDB = (Orgposte)o[0];
                PosteDto evDto = PosteDto.fromEntity(evDB);
                if(o[1]!=null) evDto.setLibellecdfi(o[1].toString());
                if(o[2]!=null) evDto.setLibellecdsp(o[2].toString());
                if(o[3]!=null) evDto.setLibelleorganigramme(o[3].toString());
                if(o[4]!=null) evDto.setCodeorganigramme(o[4].toString());
                if(o[5]!=null) evDto.setLibelleemploitype(o[5].toString());

                liste.add(evDto);
            }

        } catch (Exception e){
            throw e;
        }
        return liste;
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Salarie ID is null");
            return;
        }
        repository.deleteById(id);
    }

    private void initLibelles(PosteDto pt)
    {
        String query = "Select filiere.vall as filiere,spclt.vall as spclt,tpform.vall as tpform,nvform.vall as nvform,categorie.vall as categorie,echelon.vall as echelon,classe.vall as classe ";
        query += ",hora.vall as hora, dispo.vall as dispo, mobil.vall as mobil, nuis.vall as nuis, sexe.vall as sexe ";
        query += ",site.vall as site, niv1.vall as niv1, niv2.vall as niv2,niv3.vall as niv3, org.libelle as liborg, emptyp.libelle as libet, org.codeorganigramme as codeorganigramme ";
        query += "From DossierPaie dossier ";
        query += "Left join ParamData filiere on (dossier.identreprise=filiere.identreprise and filiere.nume=1 and filiere.cacc='" + pt.getCdfi() + "' and filiere.ctab=" + ClsNomenclature.FILIERE + ") ";
        query += "Left join ParamData spclt on (dossier.identreprise=spclt.identreprise and spclt.nume=1 and spclt.cacc='" + pt.getCdsp() + "' and spclt.ctab=" + ClsNomenclature.SPECIALITE + ") ";
        query += "Left join ParamData tpform on (dossier.identreprise=tpform.identreprise and tpform.nume=1 and tpform.cacc='" + pt.getTyfo() + "' and tpform.ctab=" + ClsNomenclature.TYPE_FORMATION + ") ";
        query += "Left join ParamData nvform on (dossier.identreprise=nvform.identreprise and nvform.nume=1 and nvform.cacc='" + pt.getNifo() + "' and nvform.ctab=" + ClsNomenclature.NIVEAU_FORMATION + ") ";
        query += "Left join ParamData categorie on (dossier.identreprise=categorie.identreprise and categorie.nume=1 and categorie.cacc='" + pt.getCat() + "' and categorie.ctab=" + ClsNomenclature.CATEGORIE + ") ";
        query += "Left join ParamData echelon on (dossier.identreprise=echelon.identreprise and echelon.nume=1 and echelon.cacc='" + pt.getEch() + "' and echelon.ctab=" + ClsNomenclature.ECHELON + ") ";
        query += "Left join ParamData classe on (dossier.identreprise=classe.identreprise and classe.nume=1 and classe.cacc='" + pt.getClas() + "' and classe.ctab=" + ClsNomenclature.CLASSE + ") ";
        query += "Left join ParamData hora on (dossier.identreprise=hora.identreprise and hora.nume=1 and hora.cacc='" + pt.getHora() + "' and hora.ctab=" + ClsNomenclature.HORAIRE + ") ";
        query += "Left join ParamData dispo on (dossier.identreprise=dispo.identreprise and dispo.nume=1 and dispo.cacc='" + pt.getDispo() + "' and dispo.ctab=" + ClsNomenclature.DISPONIBILITE_POSTE + ") ";
        query += "Left join ParamData mobil on (dossier.identreprise=mobil.identreprise and mobil.nume=1 and mobil.cacc='" + pt.getMobil() + "' and mobil.ctab=" + ClsNomenclature.MOBILITE + ") ";
        query += "Left join ParamData nuis on (dossier.identreprise=nuis.identreprise and nuis.nume=1 and nuis.cacc='" + pt.getNuis() + "' and nuis.ctab=" + ClsNomenclature.NUISANCE + ") ";
        query += "Left join ParamData sexe on (dossier.identreprise=sexe.identreprise and sexe.nume=1 and sexe.cacc='" + pt.getSexe() + "' and sexe.ctab=" + ClsNomenclature.SEXES_POSTE + ") ";
        query += "Left join ParamData site on (dossier.identreprise=site.identreprise and site.nume=1 and site.cacc='" + pt.getCodesite() + "' and site.ctab=" + ClsNomenclature.SITE_TRAVAIL + ") ";
        query += "Left join ParamData niv1 on (dossier.identreprise=niv1.identreprise and niv1.nume=1 and niv1.cacc='" + pt.getNiv1() + "' and niv1.ctab=" + ClsNomenclature.NIVEAU1 + ") ";
        query += "Left join ParamData niv2 on (dossier.identreprise=niv2.identreprise and niv2.nume=1 and niv2.cacc='" + pt.getNiv2() + "' and niv2.ctab=" + ClsNomenclature.NIVEAU2 + ") ";
        query += "Left join ParamData niv3 on (dossier.identreprise=niv3.identreprise and niv3.nume=1 and niv3.cacc='" + pt.getNiv3() + "' and niv3.ctab=" + ClsNomenclature.NIVEAU3 + ") ";
        query += "Left join Rhtorganigramme org on (dossier.identreprise=org.identreprise and org.codeposte='" + pt.getCodeposte() + "' ) ";
        query += "Left join Rhtposte emptyp on (dossier.identreprise=emptyp.identreprise and emptyp.fonc is null and emptyp.codeposte='" + StringUtil.nvl(pt.getFonc(),"null") + "' ) ";
        query += "Where dossier.identreprise='" + pt.getIdEntreprise() + "' ";

        List<Object[]> liste = null;

        Session session = null;

        Query q = null;
        try
        {
            session = service.getSession();

            q = session.createSQLQuery(query);

            liste = q.list();

            for (Object[] objects : liste)
            {
                if (objects[0] != null)
                    pt.setLibellecdfi((String) objects[0]);
                if (objects[1] != null)
                    pt.setLibellecdsp((String) objects[1]);
                if (objects[2] != null)
                    pt.setLibelletyfo((String) objects[2]);
                if (objects[3] != null)
                    pt.setLibellenifo((String) objects[3]);
                if (objects[4] != null)
                    pt.setLibellecat((String) objects[4]);
                if (objects[5] != null)
                    pt.setLibelleech((String) objects[5]);
                if (objects[6] != null)
                    pt.setLibelleclas((String) objects[6]);
                if (objects[7] != null)
                    pt.setLibellehora((String) objects[7]);
                if (objects[8] != null)
                    pt.setLibelledispo((String) objects[8]);
                if (objects[9] != null)
                    pt.setLibellemobil((String) objects[9]);
                if (objects[10] != null)
                    pt.setLibellenuis((String) objects[10]);
                if (objects[11] != null)
                    pt.setLibellesexe((String) objects[11]);
                if (objects[12] != null)
                    pt.setLibellesite((String) objects[12]);
                if (objects[13] != null)
                    pt.setLibelleniv1((String) objects[13]);
                if (objects[14] != null)
                    pt.setLibelleniv2((String) objects[14]);
                if (objects[15] != null)
                    pt.setLibelleniv3((String) objects[15]);
                if (objects[16] != null)
                    pt.setLibelleorganigramme((String) objects[16]);
                if (objects[17] != null)
                    pt.setLibelleemploitype((String) objects[17]);
                if (objects[18] != null)
                    pt.setCodeorganigramme((String) objects[18]);
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
    }

    private void loadHierarchie(PosteDto pt)
    {
        String query = "select org1.libelle lib1, org2.libelle lib2, org3.libelle lib3 from rhtorganigramme org1 ";
        query += "left join Organigramme org2 on (org1.identreprise=org2.identreprise and org1.codepere=org2.codeorganigramme) ";
        query += "left join Organigramme org3 on (org2.identreprise=org3.identreprise and org2.codepere=org3.codeorganigramme)";
        query += "where org1.identreprise='"+pt.getIdEntreprise()+"' and org1.codeposte='"+pt.getCodeposte()+"'";

        try
        {
            Session session = service.getSession();
            List liste = session.createSQLQuery(query).list();
            service.closeSession(session);
            query = null;
            if(liste==null || liste.size()==0) return;
            for (int j=0; j<liste.size(); j++) {
                Object[] objects = (Object[])((Object)liste.get(j));
                if (objects[0] != null) pt.setPeren((String)objects[0]);
                if (objects[1] != null) pt.setPerenplus1((String)objects[1]);
                if (objects[2] != null) pt.setPerenplus2((String)objects[2]);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
//			service.closeConnexion(session);
        }
    }
}
