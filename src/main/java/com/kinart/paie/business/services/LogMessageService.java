package com.kinart.paie.business.services;

import com.kinart.api.gestiondepaie.dto.LogMessageDto;
import com.kinart.paie.business.model.LogMessage;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LogMessageService {

    LogMessageDto save(LogMessageDto dto);

    LogMessageDto findById(Integer id);

    List<LogMessageDto> findAll();

    void delete(Integer id);

    List<LogMessageDto>  findByUser(String user);
}
