package com.kinart.paie.business.services.impl;

import com.kinart.api.gestiondepaie.dto.LogMessageDto;
import com.kinart.paie.business.repository.LogMessageRepository;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.ErrorCodes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LogMessageServiceImpl implements com.kinart.paie.business.services.LogMessageService {

    private LogMessageRepository logMessageRepository;

    @Autowired
    public LogMessageServiceImpl(LogMessageRepository logMessageRepository) {
        this.logMessageRepository = logMessageRepository;
    }

    @Override
    public LogMessageDto save(LogMessageDto dto) {
        return LogMessageDto.fromEntity(
                logMessageRepository.save(
                        LogMessageDto.toEntity(dto)
                )
        );
    }

    @Override
    public LogMessageDto findById(Integer id) {
        if (id == null) {
            log.error("Dossier ID is null");
            return null;
        }

        return logMessageRepository.findById(id).map(LogMessageDto::fromEntity).orElseThrow(() ->
                new EntityNotFoundException(
                        "Aucun message avec l'ID = " + id + " n'a été trouvé dans la BDD",
                        ErrorCodes.ARTICLE_NOT_FOUND)
        );
    }

    @Override
    public List<LogMessageDto> findAll() {
        return logMessageRepository.findAll().stream()
                .map(LogMessageDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Dossier ID is null");
            return;
        }
        logMessageRepository.deleteById(id);
    }

    @Override
    public List<LogMessageDto> findByUser(String user) {
        return logMessageRepository.findByUser(user).stream()
                .map(LogMessageDto::fromEntity)
                .collect(Collectors.toList());
    }
}
