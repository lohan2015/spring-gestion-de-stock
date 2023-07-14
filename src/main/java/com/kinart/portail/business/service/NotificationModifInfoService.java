package com.kinart.portail.business.service;

import com.kinart.api.portail.dto.DemandeAbsenceCongeDto;

public interface NotificationAbsenceCongeService {

    /**
     *
     * @param dto
     * @throws Exception
     */
    public void sendAbsenceCongeNotification(DemandeAbsenceCongeDto dto, String recipient) throws Exception;

    /**
     *
     * @param dto
     * @param validator
     * @throws Exception
     */
    public void sendAbsCongeNotificationSender(DemandeAbsenceCongeDto dto, String validator) throws Exception;

    /**
     *
     * @param dto
     * @throws Exception
     */
    public void sendAbsCongeNotificationRejet(DemandeAbsenceCongeDto dto) throws Exception;

    /**
     *
     * @param dto
     * @param recipient
     * @throws Exception
     */
    public void sendAnnulationAbsCgeNotification(DemandeAbsenceCongeDto dto, String recipient) throws Exception;

}
