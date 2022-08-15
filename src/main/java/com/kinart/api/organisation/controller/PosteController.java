package com.kinart.api.organisation.controller;

import com.kinart.api.organisation.controller.api.PosteApi;
import com.kinart.api.organisation.dto.*;
import com.kinart.organisation.business.services.PosteService;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.InvalidEntityException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class PosteController implements PosteApi {

    private PosteService service;
    private GeneriqueConnexionService connexionService;

    @Autowired
    public PosteController(PosteService service, GeneriqueConnexionService connexionService) {
        this.connexionService = connexionService;
        this.service = service;
    }

    @Override
    public ResponseEntity<PosteDto> save(PosteDto dto) {
        try {
            service.save(dto);
        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<PosteDto> findById(Integer id) {
        PosteDto dto = service.findById(id);
        if(dto!=null) return ResponseEntity.ok(dto);
        return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<PosteDto>> findAllPoste(String codeDossier) {
        List<PosteDto> posteDtos = service.findAllPoste(codeDossier);
        if(posteDtos!=null) {
            return ResponseEntity.ok(posteDtos);
        } else {
            throw new EntityNotFoundException("Pas de postes trouvés");
        }
    }

    @Override
    public ResponseEntity<List<PosteDto>> findAllMetier(String codeDossier) {
        List<PosteDto> posteDtos = service.findAllMetier(codeDossier);
        if(posteDtos!=null) {
            return ResponseEntity.ok(posteDtos);
        } else {
            throw new EntityNotFoundException("Pas de métiers trouvés");
        }
    }

    @Override
    public void delete(Integer id) {
        service.delete(id);
    }

    @Override
    public ResponseEntity<List<ElementDto>> findMetierDataPopup(String codeDossier) {
        String queryString = "Select codeposte, libelle";
        queryString+=" From Orgposte ";
        queryString += " Where identreprise = '"+codeDossier+"' and fonc is null ";
        queryString += " order by a.codeposte";

        List<ElementDto> liste = new ArrayList<ElementDto>();

        try {
            Session session = connexionService.getSession();
            Query query  = session.createSQLQuery(queryString);

            List<Object[]> lst = query.getResultList();
            connexionService.closeSession(session);

            for (Object[] o : lst)
            {
                ElementDto evDto = new ElementDto();
                if(o[0]!=null) evDto.setCode(o[0].toString());
                if(o[1]!=null) evDto.setLibelle(o[1].toString());

                liste.add(evDto);
            }

        } catch (Exception e){
            throw e;
        }

        return ResponseEntity.ok(liste);
    }

    @Override
    public ResponseEntity<List<ElementDto>> findPosteDataPopup(String codeDossier) {
        String queryString = "Select codeposte, libelle";
        queryString+=" From Orgposte ";
        queryString += " Where identreprise = '"+codeDossier+"' and fonc is not null ";
        queryString += " order by a.codeposte";

        List<ElementDto> liste = new ArrayList<ElementDto>();

        try {
            Session session = connexionService.getSession();
            Query query  = session.createSQLQuery(queryString);

            List<Object[]> lst = query.getResultList();
            connexionService.closeSession(session);

            for (Object[] o : lst)
            {
                ElementDto evDto = new ElementDto();
                if(o[0]!=null) evDto.setCode(o[0].toString());
                if(o[1]!=null) evDto.setLibelle(o[1].toString());

                liste.add(evDto);
            }

        } catch (Exception e){
            throw e;
        }

        return ResponseEntity.ok(liste);
    }

    @Override
    public ResponseEntity<List<OccupationPosteDto>> historiqueOccupation(RecherchePosteDto dto) {
        String queryString = "select ag.nom, ag.pren, hp.dchg dtdeb, hp.dtfin dtfin, ag.nmat from Salarie ag, HistoFonction hp";
        queryString += " Where ag.identreprise=hp.identreprise and ag.nmat=hp.nmat and ag.identreprise = '"+dto.getCodeDossier()+"' and hp.fonc='"+dto.getCodeposte()+"'";
        queryString += " order by hp.dchg";

        List<OccupationPosteDto> liste = new ArrayList<OccupationPosteDto>();

        try {
            Session session = connexionService.getSession();
            Query query  = session.createSQLQuery(queryString);

            List<Object[]> lst = query.getResultList();
            connexionService.closeSession(session);

            for (Object[] o : lst)
            {
                OccupationPosteDto evDto = new OccupationPosteDto();
                if(o[0]!=null) evDto.setNom(o[0].toString());
                if(o[1]!=null) evDto.setPrenom(o[1].toString());
                if(o[2]!=null) evDto.setDebut((Date) o[2]);
                if(o[3]!=null) evDto.setFin((Date) o[3]);
                if(o[4]!=null) evDto.setMatricule(o[4].toString());

                liste.add(evDto);
            }

        } catch (Exception e){
            throw e;
        }

        return ResponseEntity.ok(liste);
    }

    @Override
    public ResponseEntity<List<PosteinfoDto>> loadProcedures(RecherchePosteDto dto) {
        String queryString = _getGenericQueryString("proced", "PROC", "7528", dto.getCodeDossier().intValue()+"", dto.getCodeposte());

        List<PosteinfoDto> liste = new ArrayList<PosteinfoDto>();

        try {
            Session session = connexionService.getSession();
            Query query  = session.createSQLQuery(queryString);

            List<Object[]> lst = query.getResultList();
            connexionService.closeSession(session);

            for (Object[] o : lst)
            {
                PosteinfoDto evDto = new PosteinfoDto();
                if(o[0]!=null) evDto.setCodeinfo1(o[0].toString());
                if(o[1]!=null) evDto.setTypeinfo(o[1].toString());
                if(o[2]!=null) evDto.setCodeinfo2(o[2].toString());
                if(o[3]!=null) evDto.setCodeinfo3(o[3].toString());
                if(o[4]!=null) evDto.setValminfo1(new BigDecimal(o[4].toString()));
                if(o[5]!=null) evDto.setCoeff(o[4].toString());
                if(o[6]!=null) evDto.setLibelleinfo1(o[5].toString());

                liste.add(evDto);
            }

        } catch (Exception e){
            throw e;
        }

        return ResponseEntity.ok(liste);
    }

    @Override
    public ResponseEntity<List<PosteinfoDto>> loadPoids(RecherchePosteDto dto) {
        String queryString = _getGenericQueryString("pds", "POIDS", "7530", dto.getCodeDossier().intValue()+"", dto.getCodeposte());

        List<PosteinfoDto> liste = new ArrayList<PosteinfoDto>();

        try {
            Session session = connexionService.getSession();
            Query query  = session.createSQLQuery(queryString);

            List<Object[]> lst = query.getResultList();
            connexionService.closeSession(session);

            for (Object[] o : lst)
            {
                PosteinfoDto evDto = new PosteinfoDto();
                if(o[0]!=null) evDto.setCodeinfo1(o[0].toString());
                if(o[1]!=null) evDto.setTypeinfo(o[1].toString());
                if(o[2]!=null) evDto.setCodeinfo2(o[2].toString());
                if(o[3]!=null) evDto.setCodeinfo3(o[3].toString());
                if(o[4]!=null) evDto.setValminfo1(new BigDecimal(o[4].toString()));
                if(o[5]!=null) evDto.setCoeff(o[4].toString());
                if(o[6]!=null) evDto.setLibelleinfo1(o[5].toString());

                liste.add(evDto);
            }

        } catch (Exception e){
            throw e;
        }

        return ResponseEntity.ok(liste);
    }

    private String _getGenericQueryString(String libelleInfo, String typeInfo, String table, String cdos, String codeposte)
    {

        String queryString = _getBeginQuery() + "," + libelleInfo + ".vall as libelle" + libelleInfo + " from Orgposteinfo competenceinfo " + "left join ParamData " + libelleInfo + " on (" + libelleInfo
                + ".identreprise=competenceinfo.identreprise and " + libelleInfo + ".cacc=competenceinfo.codeinfo1 and " + libelleInfo + ".nume=1 and " + libelleInfo + ".ctab='" + table + "') "
                + _getEndQuery(typeInfo, cdos, codeposte);

        return queryString;
    }

    private String _getBeginQuery()
    {
        String queryString = "select competenceinfo.codeinfo1 as codeinfo1,competenceinfo.typeinfo as typeinfo,competenceinfo.codeinfo2 as codeinfo2, "
                + "competenceinfo.codeinfo3 as codeinfo3, competenceinfo.valminfo1 as valminfo1, competenceinfo.coeff as coef ";

        return queryString;
    }

    private String _getEndQuery(String typeInfo, String cdos, String codeposte)
    {
        String queryString = " where	competenceinfo.identreprise='" + cdos + "'" + " AND  competenceinfo.codeposte='" + codeposte + "'" + " AND competenceinfo.typeinfo = '" + typeInfo + "'";
        return queryString;
    }
}
