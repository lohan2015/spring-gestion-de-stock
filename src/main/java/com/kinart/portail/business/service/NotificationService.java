package com.kinart.portail.business.service;

import com.kinart.portail.business.utils.EnumNotificationType;

public interface NotificationService {

    boolean sendNotification(EnumNotificationType notificationType, String sender, String recipient, String modele) throws Exception;
}
