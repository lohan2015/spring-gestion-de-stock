package com.kinart.api.gestiondepaie.controller.api;

import com.kinart.api.gestiondepaie.dto.CalculPaieDto;
import com.kinart.api.gestiondepaie.dto.ParamCalendarDto;
import com.kinart.api.gestiondepaie.dto.ParamEFCMRDto;
import com.kinart.api.gestiondepaie.dto.ParameterOfDipeDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;

import static com.kinart.stock.business.utils.Constants.APP_ROOT;

@Api("generatecalendar")
public interface GenerateCalendierPaieApi {

    @PostMapping(value=APP_ROOT + "/generatecalendar/generate", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Générer le calendrier de paie", notes = "Cette methode permet de générer le calendrier de paie")
    ResponseEntity<Object> generateCalendar(@RequestBody ParamCalendarDto dto) throws Exception;

    @PostMapping(value=APP_ROOT + "/generatecalendar/modify", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Générer le calendrier de paie", notes = "Cette methode permet de générer le calendrier de paie")
    ResponseEntity<Object> modifyCalendar(@RequestBody ParamCalendarDto dto) throws Exception;

    @PostMapping(value=APP_ROOT + "/generatecalendar/search", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Générer le calendrier de paie", notes = "Cette methode permet de générer le calendrier de paie")
    ResponseEntity<ParamCalendarDto> getDateToModify(@RequestBody ParamCalendarDto dto) throws Exception;

    @PostMapping(value=APP_ROOT + "/generatecalendar/init", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Inituialisation calendrier de paie", notes = "Cette methode permet de générer le calendrier de paie", response = ParamCalendarDto.class)
    ResponseEntity<ParamCalendarDto> initCalendar(@RequestBody ParamCalendarDto dto) throws Exception;

    @PostMapping(value=APP_ROOT + "/generatecalendar/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Suppression une année du calendrier de paie", notes = "Cette methode permet de supprimer le calendrier de paie", response = ParamCalendarDto.class)
    ResponseEntity<ParamCalendarDto> deleteYear(@RequestBody ParamCalendarDto dto) throws Exception;
}
