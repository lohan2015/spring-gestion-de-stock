package com.kinart.api.portail.controller;

import com.kinart.portail.business.NotificationAbsCongeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.kinart.stock.business.utils.Constants.APP_ROOT_PORTAIL;

@Api("notification")
@RestController
public class NotificationController {

    @Autowired
    private NotificationAbsCongeService service;

    @PostMapping(value = APP_ROOT_PORTAIL + "/notification/absconge", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Envoi notification absence congé", notes = "Cette methode permet d'envoyer des notifications absence congé")
    void sendNotification(){
        try {
            service.sendNotification();
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
