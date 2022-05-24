package com.kinart.api.gestiondepaie.controller;

import com.kinart.api.gestiondepaie.controller.api.HistoConnexionApi;
import com.kinart.api.gestiondepaie.dto.HistoConnexionDto;
import com.kinart.api.gestiondepaie.dto.LogMessageDto;
import com.kinart.api.gestiondepaie.dto.RechercheTraceDto;
import com.kinart.paie.business.services.HistoConnexionService;
import com.kinart.paie.business.services.LogMessageService;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.InvalidEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class HistoConnexionController implements HistoConnexionApi {

    private HistoConnexionService histoConnexionService;

    @Autowired
    public HistoConnexionController(HistoConnexionService histoConnexionService) {
        this.histoConnexionService = histoConnexionService;
    }

    @Override
    public ResponseEntity<HistoConnexionDto> save(HistoConnexionDto dto) {
        try {
            dto.setDatc(new Date());
            histoConnexionService.save(dto);
        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<HistoConnexionDto> findById(Integer id) {
        HistoConnexionDto histoConnexionDto = histoConnexionService.findById(id);
        if(histoConnexionDto!=null) return ResponseEntity.ok(histoConnexionDto);
        return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<HistoConnexionDto>> findByUser(String user) {
        List<HistoConnexionDto> histoConnexionDtos = histoConnexionService.findByUser(user);
        if(histoConnexionDtos!=null) return ResponseEntity.ok(histoConnexionDtos);
        return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<HistoConnexionDto>> findAll() {
        List<HistoConnexionDto> histoConnexionDtos = histoConnexionService.findAll();
        if(histoConnexionDtos!=null) {
            return ResponseEntity.ok(histoConnexionDtos);
        } else {
            throw new EntityNotFoundException("Pas d'historique trouvés");
        }
    }

    @Override
    public void delete(Integer id) {
        histoConnexionService.delete(id);
    }

    @Override
    public ResponseEntity<List<HistoConnexionDto>> findByUserAndOperation(RechercheTraceDto dto) {
        List<HistoConnexionDto> histoConnexionDtos = histoConnexionService.findByUserAndOperation(dto.user, dto.typeOperation);
        if(histoConnexionDtos!=null) {
            return ResponseEntity.ok(histoConnexionDtos);
        } else {
            throw new EntityNotFoundException("Pas d'historique trouvés");
        }
    }

    @Override
    public ResponseEntity<List<HistoConnexionDto>> findByUserAndNoOperation(RechercheTraceDto dto) {
        List<HistoConnexionDto> histoConnexionDtos = histoConnexionService.findByUserAndNoOperation(dto.user, dto.typeOperation);
        if(histoConnexionDtos!=null) {
            return ResponseEntity.ok(histoConnexionDtos);
        } else {
            throw new EntityNotFoundException("Pas d'historique trouvés");
        }
    }

    @Override
    public ResponseEntity<List<HistoConnexionDto>> findByOperation(RechercheTraceDto dto) {
        List<HistoConnexionDto> histoConnexionDtos = histoConnexionService.findByOperation(dto.typeOperation);
        if(histoConnexionDtos!=null) {
            return ResponseEntity.ok(histoConnexionDtos);
        } else {
            throw new EntityNotFoundException("Pas d'historique trouvés");
        }
    }

    @Override
    public ResponseEntity<List<HistoConnexionDto>> findByNoOperation(RechercheTraceDto dto) {
        List<HistoConnexionDto> histoConnexionDtos = histoConnexionService.findByNoOperation(dto.typeOperation);
        if(histoConnexionDtos!=null) {
            return ResponseEntity.ok(histoConnexionDtos);
        } else {
            throw new EntityNotFoundException("Pas d'historique trouvés");
        }
    }
}
