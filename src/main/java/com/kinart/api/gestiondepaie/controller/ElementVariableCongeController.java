package com.kinart.api.gestiondepaie.controller;

import com.kinart.api.gestiondepaie.controller.api.ElementVariableCongeApi;
import com.kinart.api.gestiondepaie.dto.ElementVariableCongeDto;
import com.kinart.api.gestiondepaie.dto.RechercheDto;
import com.kinart.paie.business.repository.ParamDataRepository;
import com.kinart.paie.business.services.ElementVariableCongeService;
import com.kinart.paie.business.services.utils.ClsDate;
import com.kinart.paie.business.services.utils.ClsDateRhpcalendrier;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.InvalidEntityException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class ElementVariableCongeController implements ElementVariableCongeApi {

    private ElementVariableCongeService elementVariableCongeService;
    private GeneriqueConnexionService service;
    private ParamDataRepository paramDataRepository;

    @Autowired
    public ElementVariableCongeController(ParamDataRepository paramDataRepository, ElementVariableCongeService elementVariableCongeService, GeneriqueConnexionService service) {
        this.elementVariableCongeService = elementVariableCongeService;
        this.service = service;
        this.paramDataRepository = paramDataRepository;
    }

    @Override
    public ResponseEntity<ElementVariableCongeDto> save(ElementVariableCongeDto dto) {
        try {
            elementVariableCongeService.save(dto, dto.getDateFormat(), dto.getTypeBD());
        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ElementVariableCongeDto> findById(Integer id) {
        ElementVariableCongeDto elementVariableCongeDto = elementVariableCongeService.findById(id);
        if(elementVariableCongeDto!=null) return ResponseEntity.ok(elementVariableCongeDto);
        return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<ElementVariableCongeDto>> findAll() {
        List<ElementVariableCongeDto> elementVariableCongeDto = elementVariableCongeService.findAll();
        if(elementVariableCongeDto!=null) {
            return ResponseEntity.ok(elementVariableCongeDto);
        } else {
            throw new EntityNotFoundException("Pas de EVs congé trouvés");
        }
    }

    @Override
    public ResponseEntity<List<ElementVariableCongeDto>> findEVCongeByFilter(RechercheDto dto) {
        List<ElementVariableCongeDto> elementVariableCongeDto = elementVariableCongeService.findEVCongeByFilter(dto.nmatMin, dto.codeMotifCg);
        if(elementVariableCongeDto!=null) {
            return ResponseEntity.ok(elementVariableCongeDto);
        } else {
            throw new EntityNotFoundException("Pas de EV congé trouvés");
        }
    }

    @Override
    public void delete(Integer id) {
        elementVariableCongeService.delete(id);
    }

    @Override
    public ResponseEntity<ElementVariableCongeDto> setEndDate(ElementVariableCongeDto dto) {
        BigDecimal nbJrAcquis = getNbJourCongeAcquis(dto);

        Date dtperiode = new ClsDate(dto.getAamm()+"01", "yyyyMMdd").getDate();
        long diff = dto.getDdeb().getTime() - dtperiode.getTime();
        int diffDays = (int) (diff / (24 * 60 * 60 * 1000));

        BigDecimal nbJrCgParMois = new BigDecimal(2.2);
        nbJrAcquis = nbJrAcquis.add(nbJrCgParMois.multiply(new BigDecimal(diffDays)).divide(new BigDecimal(30),2, RoundingMode.HALF_UP));
        ClsDateRhpcalendrier rhp = new ClsDateRhpcalendrier(service, paramDataRepository, dto.getIdEntreprise()+"", "dd/MM/yyyy");
        Date dfin = rhp.getDateFinConges(dto.getDdeb(), nbJrAcquis.intValue(), dto.getTypeBD());
        int nbJrAbs = rhp.getNombreDeJoursAbsences(dto.getDdeb(), dfin, dto.getTypeBD());

        dto.setDfin(dfin);
        dto.setEnddate(new ClsDate(dfin, dto.getDateFormat()).getDateS());
        dto.setNbjc(nbJrAcquis);
        dto.setNbja(new BigDecimal(nbJrAbs));

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ElementVariableCongeDto> calculJourAcquis(ElementVariableCongeDto dto) {
        BigDecimal nbJrAcquis = getNbJourCongeAcquis(dto);

        Date dtperiode = new ClsDate(dto.getAamm()+"01", "yyyyMMdd").getDate();
        long diff = dto.getDdeb().getTime() - dtperiode.getTime();
        int diffDays = (int) (diff / (24 * 60 * 60 * 1000));

        BigDecimal nbJrCgParMois = new BigDecimal(2.2);
        nbJrAcquis = nbJrAcquis.add(nbJrCgParMois.multiply(new BigDecimal(diffDays)).divide(new BigDecimal(30),2, RoundingMode.HALF_UP));
        ClsDateRhpcalendrier rhp = new ClsDateRhpcalendrier(service, paramDataRepository, dto.getIdEntreprise()+"", "dd/MM/yyyy");
        Date dfin = rhp.getDateFinConges(dto.getDdeb(), nbJrAcquis.intValue(), dto.getTypeBD());
        int nbJrAbs = rhp.getNombreDeJoursAbsences(dto.getDdeb(), dfin, dto.getTypeBD());

        dto.setDfin(dfin);
        dto.setEnddate(new ClsDate(dfin, dto.getDateFormat()).getDateS());
        dto.setNbjc(nbJrAcquis);
        dto.setNbja(new BigDecimal(nbJrAbs));

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    private BigDecimal getNbJourCongeAcquis(ElementVariableCongeDto dto){
        BigDecimal nbJr = BigDecimal.ZERO;

        String requete = "select clas, coalesce(japa,0)+coalesce(japec,0)+coalesce(nbjsa,0)+coalesce(nbjse,0)+coalesce(nbjsm,0) from Salarie where idEntreprise = :cdos and nmat = :nmat";
        Session session = service.getSession();
        SQLQuery sQLQuery = session.createSQLQuery(requete);
        sQLQuery.setParameter("cdos", dto.getIdEntreprise().intValue());
        sQLQuery.setParameter("nmat", dto.getNmat());
        List<Object[]> result = null;
        try {
            result = sQLQuery.list();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        finally
        {
            service.closeSession(session);
        }
        String clas = "";
        if(result!= null && ! result.isEmpty())
        {
            for(Object[] obj:result){
                if(obj[0]!=null) clas = ((String)obj[0]);
                nbJr = (obj[1]!=null)?(BigDecimal)obj[1]:BigDecimal.ZERO;
            }
        }

        return nbJr;
    }
}
