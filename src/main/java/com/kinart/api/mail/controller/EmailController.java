package com.kinart.api.mail.controller;

import com.kinart.api.mail.EmailDetails;
import com.kinart.api.mail.controller.api.EmailApi;
import com.kinart.api.mail.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController implements EmailApi {

    @Autowired
    private EmailService emailService;

    @Override
    public String sendMail(EmailDetails details) {
        return emailService.sendSimpleMail(details);
    }

    @Override
    public String sendMailWithAttachment(EmailDetails details) {
        return emailService.sendMailWithAttachment(details);
    }

    @Override
    public String sendMailWithAttachmentPassword(EmailDetails details) {
        return emailService.sendMailWithAttachmentPassword(details);
    }
}
