package com.kinart.portail.business.service;

import com.kinart.api.portail.dto.DemandeAttestationDto;

public interface NotificationAttestationService {

   /**
     *
     * @param dto
     * @throws Exception
     */
    public void sendAttestationNotification(DemandeAttestationDto dto, String recipient) throws Exception;

    /**
     *
     * @param dto
     * @param validator
     * @throws Exception
     */
    public void sendAttestationNotificationSender(DemandeAttestationDto dto, String validator) throws Exception;

    /**
     *
     * @param dto
     * @throws Exception
     */
    public void sendAttestationNotificationRejet(DemandeAttestationDto dto) throws Exception;

    /**
     *
     * @param dto
     * @param recipient
     * @throws Exception
     */
    public void sendAnnulationAttestationNotification(DemandeAttestationDto dto, String recipient) throws Exception;

}
