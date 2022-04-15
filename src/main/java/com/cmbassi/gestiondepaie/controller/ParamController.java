package com.cmbassi.gestiondepaie.controller;

import com.cmbassi.gestiondepaie.controller.api.ParamApi;
import com.cmbassi.gestiondepaie.dto.ParamColumnDto;
import com.cmbassi.gestiondepaie.dto.ParamDataDto;
import com.cmbassi.gestiondepaie.dto.ParamTableDto;
import com.cmbassi.gestiondepaie.dto.SalarieDto;
import com.cmbassi.gestiondepaie.services.ParamService;
import com.cmbassi.gestiondestock.exception.EntityNotFoundException;
import com.cmbassi.gestiondestock.exception.InvalidEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ParamController implements ParamApi {

    private ParamService paramService;

    @Autowired
    public ParamController(ParamService paramService) {
        this.paramService = paramService;
    }

    @Override
    public ResponseEntity<ParamTableDto> save(ParamTableDto dto) {
        try {
            paramService.save(dto);
        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ParamColumnDto> save(ParamColumnDto dto) {
        try {
            paramService.save(dto);
        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ParamDataDto> save(ParamDataDto dto) {
        try {
            paramService.save(dto);
        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ParamColumnDto> findColumById(Integer id) {
        ParamColumnDto paramColumnDto = paramService.findColumById(id);
        if(paramColumnDto!=null) return ResponseEntity.ok(paramColumnDto);
        return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<ParamTableDto> findTableById(Integer id) {
        ParamTableDto paramTableDto = paramService.findTableById(id);
        if(paramTableDto!=null) return ResponseEntity.ok(paramTableDto);
        return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<ParamDataDto> findDataById(Integer id) {
        ParamDataDto paramDataDto = paramService.findDataById(id);
        if(paramDataDto!=null) return ResponseEntity.ok(paramDataDto);
        return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<ParamDataDto>> findDataByCodeTable(Integer ctab) {
        List<ParamDataDto> paramsDataDto = paramService.findDataByCodeTable(ctab);
        if(paramsDataDto!=null) {
            return ResponseEntity.ok(paramsDataDto);
        } else {
            throw new EntityNotFoundException("Pas de données trouveés");
        }
    }

    @Override
    public ResponseEntity<List<ParamDataDto>> findDataByCle(Integer ctab, String cacc) {
        List<ParamDataDto> paramsDataDto = paramService.findDataByCle(ctab, cacc);
        if(paramsDataDto!=null) {
            return ResponseEntity.ok(paramsDataDto);
        } else {
            throw new EntityNotFoundException("Pas de données trouveés");
        }
    }

    @Override
    public ResponseEntity<ParamDataDto> findDataByNumeroLigne(Integer ctab, String cacc, Integer nume) {
        ParamDataDto paramsDataDto = paramService.findDataByNumeroLigne(ctab, cacc, nume);
        if(paramsDataDto!=null) {
            return ResponseEntity.ok(paramsDataDto);
        } else {
            throw new EntityNotFoundException("Pas de données trouveés");
        }
    }

    @Override
    public ResponseEntity<List<ParamColumnDto>> findColumnByCodeTable(Integer ctab) {
        List<ParamColumnDto> paramsDataDto = paramService.findColumnByCodeTable(ctab);
        if(paramsDataDto!=null) {
            return ResponseEntity.ok(paramsDataDto);
        } else {
            throw new EntityNotFoundException("Pas de données trouveés");
        }
    }

    @Override
    public ResponseEntity<List<ParamColumnDto>> findColumnAll() {
        List<ParamColumnDto> paramsColumnDto = paramService.findColumnAll();
        if(paramsColumnDto!=null) {
            return ResponseEntity.ok(paramsColumnDto);
        } else {
            throw new EntityNotFoundException("Pas de colonnes trouveés");
        }
    }

    @Override
    public ResponseEntity<List<ParamDataDto>> findDataAll() {
        List<ParamDataDto> paramsDataDto = paramService.findDataAll();
        if(paramsDataDto!=null) {
            return ResponseEntity.ok(paramsDataDto);
        } else {
            throw new EntityNotFoundException("Pas de données trouveés");
        }
    }

    @Override
    public ResponseEntity<List<ParamTableDto>> findTableAll() {
        List<ParamTableDto> paramsTableDto = paramService.findTableAll();
        if(paramsTableDto!=null) {
            return ResponseEntity.ok(paramsTableDto);
        } else {
            throw new EntityNotFoundException("Pas de tables trouveés");
        }
    }

    @Override
    public void deleteColumn(Integer id) {
        paramService.deleteColumn(id);
    }

    @Override
    public void deleteTable(Integer id) {
        paramService.deleteTable(id);
    }

    @Override
    public void deleteData(Integer id) {
        paramService.deleteData(id);
    }
}
