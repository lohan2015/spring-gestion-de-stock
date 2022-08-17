package com.kinart.api.organisation.controller;

import com.kinart.api.organisation.controller.api.PosteinfoApi;
import com.kinart.api.organisation.dto.ElementDto;
import com.kinart.api.organisation.dto.PosteinfoDto;
import com.kinart.api.organisation.dto.RechercheCompetenceDto;
import com.kinart.organisation.business.services.PosteinfoService;
import com.kinart.organisation.business.services.competence.ClsTypeCompetence;
import com.kinart.organisation.business.services.competence.CompetencePosteVO;
import com.kinart.organisation.business.services.competence.CompetencesPosteVO;
import com.kinart.organisation.business.services.competence.ComputeCompetencePosteVO;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.InvalidEntityException;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PosteinfoController implements PosteinfoApi {

    private PosteinfoService service;
    private GeneriqueConnexionService generiqueConnexionService;

    @Autowired
    public PosteinfoController(PosteinfoService service, GeneriqueConnexionService generiqueConnexionService) {
        this.service = service;
        this.generiqueConnexionService = generiqueConnexionService;
    }

    @Override
    public ResponseEntity<PosteinfoDto> save(PosteinfoDto dto) {
        try {
            service.save(dto);
        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<PosteinfoDto> findById(Integer id) {
        PosteinfoDto dto = service.findById(id);
        if(dto!=null) return ResponseEntity.ok(dto);
        return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<PosteinfoDto>> findAll(String codeDossier) {
        List<PosteinfoDto> niveauDtos = service.findAll();
        if(niveauDtos!=null) {
            return ResponseEntity.ok(niveauDtos);
        } else {
            throw new EntityNotFoundException("Pas de compétences trouvées");
        }
    }

    @Override
    public ResponseEntity<List<PosteinfoDto>> findSkilByType(RechercheCompetenceDto dto, HttpServletRequest request) {
        if(StringUtils.isNotEmpty(dto.getTypeinfo())){
            if(ClsTypeCompetence.SAVOIR.equalsIgnoreCase(dto.getTypeinfo())){

                CompetencesPosteVO header = new CompetencesPosteVO(request, generiqueConnexionService, dto.getCodeposte(), dto.getCodedossier(), null);
                ComputeCompetencePosteVO compute = new ComputeCompetencePosteVO(header);
                return ResponseEntity.ok(PosteinfoDto.fromCompetence(compute.computeSavoir()));

            } else if(ClsTypeCompetence.SAVOIR_ETRE.equalsIgnoreCase(dto.getTypeinfo())){

                CompetencesPosteVO header = new CompetencesPosteVO(request, generiqueConnexionService, dto.getCodeposte(), dto.getCodedossier(), null);
                ComputeCompetencePosteVO compute = new ComputeCompetencePosteVO(header);
                return ResponseEntity.ok(PosteinfoDto.fromCompetence(compute.computeSavoirEtre()));

            } else if(ClsTypeCompetence.SAVOIR_FAIRE.equalsIgnoreCase(dto.getTypeinfo())){

                CompetencesPosteVO header = new CompetencesPosteVO(request, generiqueConnexionService, dto.getCodeposte(), dto.getCodedossier(), null);
                ComputeCompetencePosteVO compute = new ComputeCompetencePosteVO(header);
                return ResponseEntity.ok(PosteinfoDto.fromCompetence(compute.computeSavoirFaire()));

            } else if(ClsTypeCompetence.FORMATION.equalsIgnoreCase(dto.getTypeinfo())){

                CompetencesPosteVO header = new CompetencesPosteVO(request, generiqueConnexionService, dto.getCodeposte(), dto.getCodedossier(), null);
                ComputeCompetencePosteVO compute = new ComputeCompetencePosteVO(header);
                return ResponseEntity.ok(PosteinfoDto.fromCompetence(compute.computeFormation()));

            } else if(ClsTypeCompetence.FONCTION.equalsIgnoreCase(dto.getTypeinfo())){

                CompetencesPosteVO header = new CompetencesPosteVO(request, generiqueConnexionService, dto.getCodeposte(), dto.getCodedossier(), null);
                ComputeCompetencePosteVO compute = new ComputeCompetencePosteVO(header);
                return ResponseEntity.ok(PosteinfoDto.fromCompetence(compute.computeFonction()));

            } else if(ClsTypeCompetence.DIPLOME.equalsIgnoreCase(dto.getTypeinfo())){

                CompetencesPosteVO header = new CompetencesPosteVO(request, generiqueConnexionService, dto.getCodeposte(), dto.getCodedossier(), null);
                ComputeCompetencePosteVO compute = new ComputeCompetencePosteVO(header);
                return ResponseEntity.ok(PosteinfoDto.fromCompetence(compute.computeDiplome()));

            } else if(ClsTypeCompetence.LANGUE.equalsIgnoreCase(dto.getTypeinfo())){

                CompetencesPosteVO header = new CompetencesPosteVO(request, generiqueConnexionService, dto.getCodeposte(), dto.getCodedossier(), null);
                ComputeCompetencePosteVO compute = new ComputeCompetencePosteVO(header);
                return ResponseEntity.ok(PosteinfoDto.fromCompetence(compute.computeLanguage()));

            }
        }

        return null;
    }

    @Override
    public void delete(Integer id) {
        service.delete(id);
    }

    @Override
    public Integer findNextSkillId(String codeDossier) {
        String queryString = "Select max(id)+1 ";
        queryString+=" From Orgposteinfo ";
        queryString += " Where identreprise = '"+codeDossier+"'";

        try {
            Session session = generiqueConnexionService.getSession();
            Query query  = session.createSQLQuery(queryString);

            List list = query.list();
            generiqueConnexionService.closeSession(session);

           if(list==null && list.size()>=1) return new Integer(((BigDecimal)list.get(0)).intValue());

        } catch (Exception e){
            throw e;
        }

        return new Integer(1);
    }
}
