package com.kinart.portail.business.service;

import com.kinart.api.portail.dto.DemandeHabilitationDto;

public interface NotificationHabilitationService {

   /**
     *
     * @param dto
     * @throws Exception
     */
    public void sendHabilitationNotification(DemandeHabilitationDto dto, String recipient) throws Exception;

    /**
     *
     * @param dto
     * @param validator
     * @throws Exception
     */
    public void sendHabilitationNotificationSender(DemandeHabilitationDto dto, String validator) throws Exception;

    /**
     *
     * @param dto
     * @throws Exception
     */
    public void sendHabilitationNotificationRejet(DemandeHabilitationDto dto) throws Exception;

    /**
     *
     * @param dto
     * @param recipient
     * @throws Exception
     */
    public void sendAnnulationHabilitationNotification(DemandeHabilitationDto dto, String recipient) throws Exception;

}
