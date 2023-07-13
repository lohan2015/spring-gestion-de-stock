package com.kinart.portail.business.service.impl;

import com.kinart.portail.business.service.NotificationService;
import com.kinart.portail.business.utils.EnumNotificationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    @Override
    public boolean sendNotification(EnumNotificationType notificationType, String sender, String recipient, String modele) throws Exception {
        return false;
    }
}
