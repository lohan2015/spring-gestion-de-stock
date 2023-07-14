package com.kinart.portail.business.service;

import com.kinart.api.portail.dto.DemandePretDto;

public interface NotificationPretService {

    /**
     *
     * @param dto
     * @throws Exception
     */
    public void sendPretNotification(DemandePretDto dto, String recipient) throws Exception;

    /**
     *
     * @param dto
     * @param validator
     * @throws Exception
     */
    public void sendPretNotificationSender(DemandePretDto dto, String validator) throws Exception;

    /**
     *
     * @param dto
     * @throws Exception
     */
    public void sendPretNotificationRejet(DemandePretDto dto) throws Exception;

    /**
     *
     * @param dto
     * @param recipient
     * @throws Exception
     */
    public void sendAnnulationPretNotification(DemandePretDto dto, String recipient) throws Exception;

}
