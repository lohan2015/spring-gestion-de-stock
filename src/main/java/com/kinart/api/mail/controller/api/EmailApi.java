package com.kinart.api.mail.controller.api;

import com.kinart.api.mail.EmailDetails;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.kinart.stock.business.utils.Constants.APP_ROOT_PAIE;

@Api("envoimail")
public interface EmailApi {

    // Sending a simple Email
    @PostMapping(value = APP_ROOT_PAIE + "/mail/sendMail", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Envoi un mail simple", notes = "Cette methode permet d'envoyer un mail simple")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Mail envoyé avec succès"),
            @ApiResponse(code = 400, message = "Paramètres incorrects")
    })
    public String sendMail(@RequestBody EmailDetails details);

    // Sending a Email with password
    @PostMapping(value = APP_ROOT_PAIE + "/mail/sendMailWithAttachment", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Envoi un mail avec fichier attaché", notes = "Cette methode permet d'envoyer un mail avec fichier attaché")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Mail envoyé avec succès"),
            @ApiResponse(code = 400, message = "Paramètres incorrects")
    })
    public String sendMailWithAttachment(@RequestBody EmailDetails details);

    // Sending a Email with password
    @PostMapping(value = APP_ROOT_PAIE + "/mail/sendMailWithAttachmentPassword", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Envoi un mail avec fichier attaché", notes = "Cette methode permet d'envoyer un mail avec fichier attaché")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Mail envoyé avec succès"),
            @ApiResponse(code = 400, message = "Paramètres incorrects")
    })
    public String sendMailWithAttachmentPassword(@RequestBody EmailDetails details);
}
