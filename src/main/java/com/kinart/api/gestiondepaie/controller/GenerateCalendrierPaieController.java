package com.kinart.api.gestiondepaie.controller;

import com.kinart.api.gestiondepaie.controller.api.GenerateCalendierPaieApi;
import com.kinart.api.gestiondepaie.dto.ParamCalendarDto;
import com.kinart.paie.business.services.calendrier.ClsCalendar;
import com.kinart.paie.business.services.utils.ClsDateRhpcalendrier;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class GenerateCalendrierPaieController implements GenerateCalendierPaieApi {

    private GeneriqueConnexionService service;

    @Autowired
    public GenerateCalendrierPaieController(GeneriqueConnexionService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<Object> generateCalendar(ParamCalendarDto dto) throws Exception {
        ClsCalendar vo = ClsDateRhpcalendrier.fromParameter(dto);
        vo.setService(service);
        //vo.initCalendar();
        String _strMessage = "";

        if (vo.getCurrentyear() == null || vo.getCurrentyear().trim().length() == 0)
            _strMessage += "<ul>- Saisissez l'année!</ul>";

        if (vo.getFirstdaynumber() == null || vo.getFirstdaynumber().trim().length() == 0)
            _strMessage += "<ul>- Saisissez le premier jour de l'année!</ul>";

        try
        {
            int _intNbre = Integer.parseInt(vo.getFirstdaynumber());
        }
        catch (Exception e)
        {
            // TODO: handle exception
            _strMessage += "<ul>- Le premier jour de l'année doit être un nombre entier!</ul>";
        }

        if (vo.getWeeknumber() == null || vo.getWeeknumber().trim().length() == 0)
            _strMessage += "<ul>- Saisissez le nombre de semaine!</ul>";

        try
        {
            int _intNbre = Integer.parseInt(vo.getWeeknumber());
        }
        catch (Exception e)
        {
            // TODO: handle exception
            _strMessage += "<ul>- Le nombre de semaines doit être un nombre entier!</ul>";
        }

        if (vo.getFirstweek() == null || vo.getFirstweek().trim().length() == 0)
            _strMessage += "<ul>- Saisissez le premier jour de semaine!</ul>";

        boolean existCalendar = vo._existCalendar();
        if (existCalendar)
            _strMessage += "<ul>- Calendrier deja généré!</ul>";

        if (StringUtils.isNotBlank(_strMessage))
        {
            return new ResponseEntity(_strMessage, HttpStatus.BAD_REQUEST);
        }
        vo.convertFromBooleanToString();
        boolean _isSucceded = vo.__save();
        if(_isSucceded==true)
            return new ResponseEntity(dto, HttpStatus.CREATED);
        else return new ResponseEntity("Echec de la sauvegarde", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Object> modifyCalendar(ParamCalendarDto dto) throws Exception {
        ClsCalendar vo = ClsDateRhpcalendrier.fromParameter(dto);
        vo.setService(service);
        vo.initCalendar();
        vo.convertFromBooleanToString();
        boolean _isSucceded = vo.__update();

        if(_isSucceded==true)
            return new ResponseEntity(dto, HttpStatus.CREATED);
        else return new ResponseEntity("Echec de la sauvegarde", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<ParamCalendarDto> getDateToModify(ParamCalendarDto dto) throws Exception {
        Date dt = dto.getDateToModify();
        if (dt == null)
        {
            String _strMessage = "<ul>- Saisissez la date à modifier!</ul>";
            return new ResponseEntity(_strMessage, HttpStatus.BAD_REQUEST);
        }
        String _strYear = null;
        try
        {
            _strYear = new SimpleDateFormat(dto.getDateFormat()).format(dt);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity("Format de sate incorrect", HttpStatus.BAD_REQUEST);
        }

        ClsCalendar vo = ClsDateRhpcalendrier.fromParameter(dto);
        vo.setService(service);
        vo.setDateToModify(dt);
        vo.initCalendarToModify(_strYear, dto.getDateFormat());

        vo.convertFromStringToBoolean();

        return new ResponseEntity(ClsDateRhpcalendrier.getParameter(vo), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ParamCalendarDto> initCalendar(ParamCalendarDto dto) throws Exception {
        ClsCalendar vo = ClsDateRhpcalendrier.fromParameter(dto);
        vo.setService(service);
        vo.initCalendar();

        return new ResponseEntity(ClsDateRhpcalendrier.getParameter(vo), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ParamCalendarDto> deleteYear(ParamCalendarDto dto) throws Exception {
        ClsCalendar vo = ClsDateRhpcalendrier.fromParameter(dto);
        ClsCalendar.__delete(service, vo.getCompanykey(), vo.getLangue(), vo.getCurrentyeartodelete());

        return new ResponseEntity(ClsDateRhpcalendrier.getParameter(vo), HttpStatus.CREATED);
    }
}
