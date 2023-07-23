package com.kinart.portail.business.service.impl;

import com.kinart.api.gestiondepaie.dto.ParamDataDto;
import com.kinart.api.mail.EmailDetails;
import com.kinart.api.mail.service.EmailService;
import com.kinart.api.portail.dto.DemandeAttestationDto;
import com.kinart.paie.business.repository.ParamDataRepository;
import com.kinart.portail.business.model.NotifAttestation;
import com.kinart.portail.business.repository.NotifAttestationRepository;
import com.kinart.portail.business.service.NotificationAttestationService;
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
public class NotificationAttestationServiceImpl implements NotificationAttestationService {

    private final NotifAttestationRepository attestationRepository;
    private final EmailService emailService;
    private final ParamDataRepository paramDataRepository;

    @Override
    @Transactional
    public void sendAttestationNotification(DemandeAttestationDto dto, String recipient) throws Exception {
        // Sauvegarde modification
        String modele = "Bonjour,"+
                "Merci de traiter la demande d'attestation de $SENDER."+
                "Cdlt,"+
                "RH SONIBANK";

        ParamDataDto fnom = paramDataRepository.findByNumeroLigne(Integer.valueOf(99), "MOD_ATTEST", Integer.valueOf(1))
                .map(ParamDataDto::fromEntity)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Aucune donnée avec l'ID = "+"MOD_ATTEST"+" n' ete trouve dans la table 99",
                                ErrorCodes.ARTICLE_NOT_FOUND)
                );

        if(fnom == null)
            throw new EntityNotFoundException("Aucune donnée avec l'ID = "+"MOD_ATTEST"+" n'a pas été trouvée dans la table 99", ErrorCodes.ARTICLE_NOT_FOUND);
        else modele = fnom.getVall();

        modele = modele.replaceAll("\\$SENDER", dto.getUserDemAttest().getPrenom()+" "+dto.getUserDemAttest().getNom());
        modele = modele.replaceAll("\\$NUMERO", String.valueOf(dto.getId()));

        NotifAttestation notifAttestation = new NotifAttestation();
        notifAttestation.setSender(dto.getUserDemAttest().getEmail());
        notifAttestation.setRecipient(recipient);
        notifAttestation.setMessage(modele);
        notifAttestation.setCreationDate(Instant.now());
        notifAttestation.setLastModifiedDate(Instant.now());
        attestationRepository.save(notifAttestation);

        // Envoi notification
        EmailDetails paramMail = new EmailDetails();
        paramMail.setMsgBody(notifAttestation.getMessage());
        paramMail.setRecipient(recipient);
        paramMail.setSubject("Notification demande attestation");
        try {
            emailService.sendSimpleMail(paramMail);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void sendAttestationNotificationSender(DemandeAttestationDto dto, String validator) throws Exception {
        // Sauvegarde modification
        String modele = "Bonjour,"+
                        "Votre demande d'attestation est soumise a la validation de $VALIDATOR."+
                        "Cdlt,"+
                        "RH SONIBANK";

        ParamDataDto fnom = paramDataRepository.findByNumeroLigne(Integer.valueOf(99), "ACK_ATTEST", Integer.valueOf(1))
                .map(ParamDataDto::fromEntity)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Aucune donnée avec l'ID = "+"ACK_ATTEST"+" n' ete trouve dans la table 99",
                                ErrorCodes.ARTICLE_NOT_FOUND)
                );

        if(fnom == null)
            throw new EntityNotFoundException("Aucune donnée avec l'ID = "+"ACK_ATTEST"+" n'a pas été trouvée dans la table 99", ErrorCodes.ARTICLE_NOT_FOUND);
        else modele = fnom.getVall();

        modele = modele.replaceAll("\\$VALIDATOR", validator);
        modele = modele.replaceAll("\\$NUMERO", String.valueOf(dto.getId()));

        NotifAttestation notifAttestation = new NotifAttestation();
        notifAttestation.setSender(dto.getUserDemAttest().getEmail());
        notifAttestation.setRecipient(dto.getUserDemAttest().getEmail());
        notifAttestation.setMessage(modele);
        notifAttestation.setCreationDate(Instant.now());
        notifAttestation.setLastModifiedDate(Instant.now());
        attestationRepository.save(notifAttestation);

        // Envoi notification
        EmailDetails paramMail = new EmailDetails();
        paramMail.setMsgBody(notifAttestation.getMessage());
        paramMail.setRecipient(dto.getUserDemAttest().getEmail());
        paramMail.setSubject("Notification demande d'attestation");
        try {
            emailService.sendSimpleMail(paramMail);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void sendAttestationNotificationRejet(DemandeAttestationDto dto) throws Exception {
        // Sauvegarde modification
        String modele = "Bonjour,"+
                        "Votre demande d'attestation du $DATE a été rejetée."+
                        "Cdlt,"+
                        "RH SONIBANK";

        ParamDataDto fnom = paramDataRepository.findByNumeroLigne(Integer.valueOf(99), "REJ_ATTEST", Integer.valueOf(1))
                .map(ParamDataDto::fromEntity)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Aucune donnée avec l'ID = "+"REJ_ATTEST"+" n' ete trouve dans la table 99",
                                ErrorCodes.ARTICLE_NOT_FOUND)
                );

        if(fnom == null)
            throw new EntityNotFoundException("Aucune donnée avec l'ID = "+"REJ_ATTEST"+" n'a pas été trouvée dans la table 99", ErrorCodes.ARTICLE_NOT_FOUND);
        else modele = fnom.getVall();

        modele = modele.replaceAll("\\$DATE", new SimpleDateFormat("dd-MM-yyyy").format(Date.from(dto.getCreationDate())));
        modele = modele.replaceAll("\\$NUMERO", String.valueOf(dto.getId()));

        NotifAttestation notifAttestation = new NotifAttestation();
        notifAttestation.setSender(dto.getUserDemAttest().getEmail());
        notifAttestation.setRecipient(dto.getUserDemAttest().getEmail());
        notifAttestation.setMessage(modele);
        notifAttestation.setCreationDate(Instant.now());
        notifAttestation.setLastModifiedDate(Instant.now());
        attestationRepository.save(notifAttestation);

        // Envoi notification
        EmailDetails paramMail = new EmailDetails();
        paramMail.setMsgBody(notifAttestation.getMessage());
        paramMail.setRecipient(dto.getUserDemAttest().getEmail());
        paramMail.setSubject("Notification demande d'habilitation");
        try {
            emailService.sendSimpleMail(paramMail);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void sendAnnulationAttestationNotification(DemandeAttestationDto dto, String recipient) throws Exception {
        // Sauvegarde modification
        String modele = "Bonjour,"+
                        "La demande d'attestation de $SENDER a été annulée par ce dernier."+
                        "Cdlt,"+
                        "RH SONIBANK";

        ParamDataDto fnom = paramDataRepository.findByNumeroLigne(Integer.valueOf(99), "ANN_ATTEST", Integer.valueOf(1))
                .map(ParamDataDto::fromEntity)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Aucune donnée avec l'ID = "+"ANN_ATTEST"+" n' ete trouve dans la table 99",
                                ErrorCodes.ARTICLE_NOT_FOUND)
                );

        if(fnom == null)
            throw new EntityNotFoundException("Aucune donnée avec l'ID = "+"ANN_ATTEST"+" n'a pas été trouvée dans la table 99", ErrorCodes.ARTICLE_NOT_FOUND);
        else modele = fnom.getVall();

        modele = modele.replaceAll("\\$SENDER", dto.getUserDemAttest().getPrenom()+" "+dto.getUserDemAttest().getNom());
        modele = modele.replaceAll("\\$NUMERO", String.valueOf(dto.getId()));

        NotifAttestation notifAttestation = new NotifAttestation();
        notifAttestation.setSender(dto.getUserDemAttest().getEmail());
        notifAttestation.setRecipient(recipient);
        notifAttestation.setMessage(modele);
        notifAttestation.setCreationDate(Instant.now());
        notifAttestation.setLastModifiedDate(Instant.now());
        attestationRepository.save(notifAttestation);

        // Envoi notification
        EmailDetails paramMail = new EmailDetails();
        paramMail.setMsgBody(notifAttestation.getMessage());
        paramMail.setRecipient(recipient);
        paramMail.setSubject("Notification annulation d'attestation'");
        try {
            emailService.sendSimpleMail(paramMail);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
