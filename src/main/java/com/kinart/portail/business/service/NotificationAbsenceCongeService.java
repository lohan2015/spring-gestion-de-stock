package com.kinart.portail.business.service;

import com.kinart.api.portail.dto.DemandeAbsenceCongeDto;
import com.kinart.api.portail.dto.DemandeHabilitationDto;

public interface NotificationService {

    /**
     *
     * @param dto
     * @throws Exception
     */
    public void sendAbsenceCongeNotification(DemandeAbsenceCongeDto dto, String recipient) throws Exception;

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
