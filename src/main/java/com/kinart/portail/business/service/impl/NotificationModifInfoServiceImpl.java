package com.kinart.portail.business.service.impl;

import com.kinart.api.gestiondepaie.dto.ParamDataDto;
import com.kinart.api.mail.EmailDetails;
import com.kinart.api.mail.service.EmailService;
import com.kinart.api.portail.dto.DemandeModifInfoDto;
import com.kinart.paie.business.repository.ParamDataRepository;
import com.kinart.portail.business.model.NotifModifInfo;
import com.kinart.portail.business.repository.NotifModifInfoRepository;
import com.kinart.portail.business.service.NotificationModifInfoService;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.ErrorCodes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationModifInfoServiceImpl implements NotificationModifInfoService {

    private final NotifModifInfoRepository modifInfoRepository;
    private final EmailService emailService;
    private final ParamDataRepository paramDataRepository;

    @Override
    @Transactional
    public void sendModifInfoNotification(DemandeModifInfoDto dto, String recipient) throws Exception {
        // Sauvegarde modification
        String modele = "Bonjour,"+
                        "Merci de traiter la demande de modification de l'information de sa fiche salarié de $SENDER."+
                        "Cdlt,"+
                        "RH SONIBANK";

        ParamDataDto fnom = paramDataRepository.findByNumeroLigne(Integer.valueOf(99), "MOD_MODINF", Integer.valueOf(1))
                .map(ParamDataDto::fromEntity)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Aucune donnée avec l'ID = "+"MOD_MODINF"+" n' ete trouve dans la table 99",
                                ErrorCodes.ARTICLE_NOT_FOUND)
                );

        if(fnom == null)
            throw new EntityNotFoundException("Aucune donnée avec l'ID = "+"MOD_MODINF"+" n'a pas été trouvée dans la table 99", ErrorCodes.ARTICLE_NOT_FOUND);
        else modele = fnom.getVall();

        modele = modele.replaceAll("\\$SENDER", dto.getUserDemModInfo().getPrenom()+" "+dto.getUserDemModInfo().getNom());
        modele = modele.replaceAll("\\$NUMERO", String.valueOf(dto.getId()));

        NotifModifInfo modifInfo = new NotifModifInfo();
        modifInfo.setSender(dto.getUserDemModInfo().getEmail());
        modifInfo.setRecipient(recipient);
        modifInfo.setMessage(modele);
        modifInfo.setCreationDate(Instant.now());
        modifInfo.setLastModifiedDate(Instant.now());
        modifInfoRepository.save(modifInfo);

        // Envoi notification
        EmailDetails paramMail = new EmailDetails();
        paramMail.setMsgBody(modifInfo.getMessage());
        paramMail.setRecipient(recipient);
        paramMail.setSubject("Notification de modification de la fiche salarié");
        try {
            emailService.sendSimpleMail(paramMail);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void sendModifInfoNotificationSender(DemandeModifInfoDto dto, String validator) throws Exception {
        // Sauvegarde modification
        String modele = "Bonjour,"+
                        "Votre demande de modification de l'information de la fiche salarié est soumise a la validation de $VALIDATOR."+
                        "Cdlt,"+
                        "RH SONIBANK";

        ParamDataDto fnom = paramDataRepository.findByNumeroLigne(Integer.valueOf(99), "ACK_MODINF", Integer.valueOf(1))
                .map(ParamDataDto::fromEntity)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Aucune donnée avec l'ID = "+"ACK_MODINF"+" n' ete trouve dans la table 99",
                                ErrorCodes.ARTICLE_NOT_FOUND)
                );

        if(fnom == null)
            throw new EntityNotFoundException("Aucune donnée avec l'ID = "+"ACK_MODINF"+" n'a pas été trouvée dans la table 99", ErrorCodes.ARTICLE_NOT_FOUND);
        else modele = fnom.getVall();

        modele = modele.replaceAll("\\$VALIDATOR", validator);
        modele = modele.replaceAll("\\$NUMERO", String.valueOf(dto.getId()));

        NotifModifInfo modifInfo = new NotifModifInfo();
        modifInfo.setSender(dto.getUserDemModInfo().getEmail());
        modifInfo.setRecipient(dto.getUserDemModInfo().getEmail());
        modifInfo.setMessage(modele);
        modifInfo.setCreationDate(Instant.now());
        modifInfo.setLastModifiedDate(Instant.now());
        modifInfoRepository.save(modifInfo);

        // Envoi notification
        EmailDetails paramMail = new EmailDetails();
        paramMail.setMsgBody(modifInfo.getMessage());
        paramMail.setRecipient(dto.getUserDemModInfo().getEmail());
        paramMail.setSubject("Notification demande absence / congé");
        try {
            emailService.sendSimpleMail(paramMail);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void sendModifInfoNotificationRejet(DemandeModifInfoDto dto) throws Exception {
        // Sauvegarde modification
        String modele = "Bonjour,"+
                        "Votre demande de modification de l'information du $DATE de la fiche salarié a été rejetée."+
                        "Cdlt,"+
                        "RH SONIBANK";

        ParamDataDto fnom = paramDataRepository.findByNumeroLigne(Integer.valueOf(99), "REJ_MODINF", Integer.valueOf(1))
                .map(ParamDataDto::fromEntity)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Aucune donnée avec l'ID = "+"REJ_MODINF"+" n' ete trouve dans la table 99",
                                ErrorCodes.ARTICLE_NOT_FOUND)
                );

        if(fnom == null)
            throw new EntityNotFoundException("Aucune donnée avec l'ID = "+"REJ_MODINF"+" n'a pas été trouvée dans la table 99", ErrorCodes.ARTICLE_NOT_FOUND);
        else modele = fnom.getVall();

        modele = modele.replaceAll("\\$DATE", new SimpleDateFormat("dd-MM-yyyy").format(Date.from(dto.getCreationDate())));
        modele = modele.replaceAll("\\$NUMERO", String.valueOf(dto.getId()));

        NotifModifInfo modifInfo = new NotifModifInfo();
        modifInfo.setSender(dto.getUserDemModInfo().getEmail());
        modifInfo.setRecipient(dto.getUserDemModInfo().getEmail());
        modifInfo.setMessage(modele);
        modifInfo.setCreationDate(Instant.now());
        modifInfo.setLastModifiedDate(Instant.now());
        modifInfoRepository.save(modifInfo);

        // Envoi notification
        EmailDetails paramMail = new EmailDetails();
        paramMail.setMsgBody(modifInfo.getMessage());
        paramMail.setRecipient(dto.getUserDemModInfo().getEmail());
        paramMail.setSubject("Notification demande d'information de la fiche salarié");
        try {
            emailService.sendSimpleMail(paramMail);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void sendAnnulationModifInfoNotification(DemandeModifInfoDto dto, String recipient) throws Exception {
        // Sauvegarde modification
        String modele = "Bonjour,"+
                        "La demande de modification d'information de $SENDER a été annulée par ce dernier."+
                        "Cdlt,"+
                        "RH SONIBANK";

        ParamDataDto fnom = paramDataRepository.findByNumeroLigne(Integer.valueOf(99), "ANN_MODINF", Integer.valueOf(1))
                .map(ParamDataDto::fromEntity)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Aucune donnée avec l'ID = "+"ANN_MODINF"+" n' ete trouve dans la table 99",
                                ErrorCodes.ARTICLE_NOT_FOUND)
                );

        if(fnom == null)
            throw new EntityNotFoundException("Aucune donnée avec l'ID = "+"ANN_MODINF"+" n'a pas été trouvée dans la table 99", ErrorCodes.ARTICLE_NOT_FOUND);
        else modele = fnom.getVall();

        modele = modele.replaceAll("\\$SENDER", dto.getUserDemModInfo().getPrenom()+" "+dto.getUserDemModInfo().getNom());
        modele = modele.replaceAll("\\$NUMERO", String.valueOf(dto.getId()));

        NotifModifInfo modifInfo = new NotifModifInfo();
        modifInfo.setSender(dto.getUserDemModInfo().getEmail());
        modifInfo.setRecipient(recipient);
        modifInfo.setMessage(modele);
        modifInfo.setCreationDate(Instant.now());
        modifInfo.setLastModifiedDate(Instant.now());
        modifInfoRepository.save(modifInfo);

        // Envoi notification
        EmailDetails paramMail = new EmailDetails();
        paramMail.setMsgBody(modifInfo.getMessage());
        paramMail.setRecipient(recipient);
        paramMail.setSubject("Notification annulation demande modification information");
        try {
            emailService.sendSimpleMail(paramMail);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
