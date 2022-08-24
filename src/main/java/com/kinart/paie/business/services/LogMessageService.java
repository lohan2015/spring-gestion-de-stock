package com.kinart.paie.business.services;

import com.kinart.api.gestiondepaie.dto.LogMessageDto;

import java.util.List;

public interface LogMessageService {

    LogMessageDto save(LogMessageDto dto);

    LogMessageDto findById(Integer id);

    List<LogMessageDto> findAll();

    void delete(Integer id);

    List<LogMessageDto>  findByUser(String user);
}
