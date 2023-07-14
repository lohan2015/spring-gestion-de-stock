package com.kinart.portail.business.service;

import com.kinart.api.portail.dto.DemandeModifInfoDto;

public interface NotificationModifInfoService {

    /**
     *
     * @param dto
     * @throws Exception
     */
    public void sendModifInfoNotification(DemandeModifInfoDto dto, String recipient) throws Exception;

    /**
     *
     * @param dto
     * @param validator
     * @throws Exception
     */
    public void sendModifInfoNotificationSender(DemandeModifInfoDto dto, String validator) throws Exception;

    /**
     *
     * @param dto
     * @throws Exception
     */
    public void sendModifInfoNotificationRejet(DemandeModifInfoDto dto) throws Exception;

    /**
     *
     * @param dto
     * @param recipient
     * @throws Exception
     */
    public void sendAnnulationModifInfoNotification(DemandeModifInfoDto dto, String recipient) throws Exception;

}
