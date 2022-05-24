package com.kinart.api.gestiondepaie.controller;

import com.kinart.api.gestiondepaie.controller.api.LogMessageApi;
import com.kinart.api.gestiondepaie.dto.LogMessageDto;
import com.kinart.paie.business.services.LogMessageService;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.InvalidEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LogMessageController implements LogMessageApi {

    private LogMessageService logMessageService;

    @Autowired
    public LogMessageController(LogMessageService logMessageService) {
        this.logMessageService = logMessageService;
    }

    @Override
    public ResponseEntity<LogMessageDto> save(LogMessageDto dto) {
        try {
            logMessageService.save(dto);
        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<LogMessageDto> findById(Integer id) {
        LogMessageDto logMessageDto = logMessageService.findById(id);
        if(logMessageDto!=null) return ResponseEntity.ok(logMessageDto);
        return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<LogMessageDto>> findByUser(String user) {
        List<LogMessageDto> logMessageDto = logMessageService.findByUser(user);
        if(logMessageDto!=null) return ResponseEntity.ok(logMessageDto);
        return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<LogMessageDto>> findAll() {
        List<LogMessageDto> logMessageDtos = logMessageService.findAll();
        if(logMessageDtos!=null) {
            return ResponseEntity.ok(logMessageDtos);
        } else {
            throw new EntityNotFoundException("Pas de logs trouvés");
        }
    }

    @Override
    public void delete(Integer id) {
        logMessageService.delete(id);
    }
}
