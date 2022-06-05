package com.kinart.api.mail.service;

import com.kinart.api.mail.EmailDetails;

public interface EmailService {
    // Method
    // To send a simple email
    String sendSimpleMail(EmailDetails details);

    // Method
    // To send an email with attachment
    String sendMailWithAttachment(EmailDetails details);

    // Method
    // To send an email with attachment password
    String sendMailWithAttachmentPassword(EmailDetails details);
}
