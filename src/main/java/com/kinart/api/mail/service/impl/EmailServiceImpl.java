package com.kinart.api.mail.service.impl;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.kinart.api.mail.EmailDetails;
import com.kinart.api.mail.service.EmailService;
import com.kinart.paie.business.services.utils.StringUtil;
import jakarta.mail.*;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

// Annotation
@Service
// Class
// Implementing EmailService interface
public class EmailServiceImpl implements EmailService {

    @Autowired private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}") private String sender;

    // Method 1
    // To send a simple email
    public String sendSimpleMail(EmailDetails details)
    {
        // Try block to check for exceptions
        try {
            return sendSimpleMailGmail(details);
            //return sendSimpleMailSonibank(details);
            // Creating a simple mail message
            /*SimpleMailMessage mailMessage = new SimpleMailMessage();

            // Setting up necessary details
            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(details.getSubject());

            // Sending the mail
            javaMailSender.send(mailMessage);
            return "Mail Sent Successfully...";*/
        }

        // Catch block to handle the exceptions
        catch (Exception e) {
            return "Error while Sending Mail";
        }
    }

    // Method 2
    // To send an email with attachment
    public String sendMailWithAttachment(EmailDetails details)
    {
        // Creating a mime message
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {
            // Setting multipart as true for attachments to
            // be send
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(details.getRecipient());
            mimeMessageHelper.setText(details.getMsgBody());
            mimeMessageHelper.setSubject(details.getSubject());

            // Adding the attachment
            FileSystemResource file = new FileSystemResource(new File(details.getAttachment()));

            mimeMessageHelper.addAttachment(file.getFilename(), file);

            // Sending the mail
            javaMailSender.send(mimeMessage);
            return "Mail sent Successfully";
        }// Catch block to handle MessagingException
        catch (MessagingException e) {
            // Display message when exception occurred
            return "Error while sending mail!!!";
        }
    }

    @Override
    public String sendMailWithAttachmentPassword(EmailDetails details) {
        // Creating a mime message
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {
            // Setting multipart as true for attachments to
            // be send
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(details.getRecipient());
            mimeMessageHelper.setText(details.getMsgBody());
            mimeMessageHelper.setSubject(details.getSubject());

            PdfReader reader = new PdfReader(details.getAttachment());
            String generatedDir = StringUtils.cleanPath("./generated-reports-scripted/");
            Path generatedPath = Paths.get(generatedDir);
            if (!Files.exists(generatedPath)){
                Files.createDirectories(generatedPath);
            }
            System.out.println("Chemin 1="+Paths.get(generatedDir).toAbsolutePath().toString());
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(Paths.get(generatedDir).toAbsolutePath().toString()+"/"+details.getFilename()));
            stamper.setEncryption(StringUtil.nvl(details.getPassword(),"").getBytes(), "cp123".getBytes(), PdfWriter.ALLOW_PRINTING /*PdfWriter.ALLOW_PRINTING | PdfWriter.ALLOW_COPY*/, PdfWriter.ENCRYPTION_AES_256);
            stamper.close();
            reader.close();

            // Adding the attachment
            FileSystemResource file = new FileSystemResource(new File(generatedDir+details.getFilename()));

            mimeMessageHelper.addAttachment(file.getFilename(), file);

            // Sending the mail
            javaMailSender.send(mimeMessage);
            return "Mail sent Successfully";
        }// Catch block to handle MessagingException
        catch (MessagingException e) {
            // Display message when exception occurred
            return "Error while sending mail!!!";
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Mail sent Successfully";
    }


    public String sendSimpleMailGmail(EmailDetails details)
    {
        Properties props = new Properties();
        props.put("mail.from", "lohanlaurel@gmail.com");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", false);
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.debug", "true");
        props.put("mail.smtp.connectiontimeout", "20000");
        props.put("mail.smtp.timeout", "20000");
        props.put("mail.smtp.writetimeout", "20000");
        props.put("mail.smtp.ssl.enable", true);

        //create the Session object
        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("lohanlaurel@gmail.com", "qszduiavxyicburp");
            }
        };

        //System.out.println("PASSWORD-------------------------------------------qszduiavxyicburp");


        try {
            //System.out.println("Word----------------------1");
            Session session = null;
            session = Session.getInstance(props, authenticator);

            //create a MimeMessage object
            Message message = new jakarta.mail.internet.MimeMessage(session);
            //set From email field
            message.setFrom(new InternetAddress("lohanlaurel@gmail.com"));
            //set To email field
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(details.getRecipient()));
            //set email subject field
            message.setSubject(details.getSubject());
            //System.out.println("Word----------------------2");
            //create the message body part
            BodyPart messageBodyPart = new MimeBodyPart();
            //set the actual message
            messageBodyPart.setText(details.getMsgBody());
            //create an instance of multipart object
            Multipart multipart = new MimeMultipart();
            //System.out.println("Word----------------------3");
            //set the first text message part
            multipart.addBodyPart(messageBodyPart);
            //set the second part, which is the attachment
            /*if(courrier.getAttachedFile() != null && !courrier.getAttachedFile().isEmpty()){
                for (ClsCodeLibelle file : courrier.getAttachedFile()) {
                    messageBodyPart = new MimeBodyPart();
                    DataSource source = new FileDataSource(file.getLibelle());
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName(file.getCode());
                    multipart.addBodyPart(messageBodyPart);
                }
            }*/

            //send the entire message parts
            message.setContent(multipart);
            //send the email message
            Transport.send(message);
            //System.out.println("Email Message Sent Successfully");
        } catch (AddressException e) {
            e.printStackTrace();
            return "Error while Sending Mail";
        } catch (jakarta.mail.MessagingException e) {
            e.printStackTrace();
            return "Error while Sending Mail";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error while Sending Mail";
        }

        return "Mail Sent Successfully...";
    }

    public String sendSimpleMailSonibank(EmailDetails details)
    {
        Properties props = new Properties();
        props.put("mail.from", "amplituderh@sonibank.com");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", false);
        props.put("mail.smtp.starttls.enable", false);
        props.put("mail.smtp.host", "192.168.160.65");
        props.put("mail.smtp.port", "25");
        props.put("mail.debug", "true");
        props.put("mail.smtp.connectiontimeout", "20000");
        props.put("mail.smtp.timeout", "20000");
        props.put("mail.smtp.writetimeout", "20000");
        props.put("mail.smtp.ssl.enable", false);

        //create the Session object
        /*Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("amplituderh@sonibank.com", "sabc");
            }
        };*/

        //System.out.println("PASSWORD-------------------------------------------No Password");

        try {
            Session session = null;
            session = Session.getInstance(props);

            //create a MimeMessage object
            Message message = new jakarta.mail.internet.MimeMessage(session);
            //set From email field
            message.setFrom(new InternetAddress("amplituderh@sonibank.com"));
            //set To email field
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(details.getRecipient()));
            //set email subject field
            message.setSubject(details.getSubject());
            //create the message body part
            BodyPart messageBodyPart = new MimeBodyPart();
            //set the actual message
            messageBodyPart.setText(details.getMsgBody());
            //create an instance of multipart object
            Multipart multipart = new MimeMultipart();
            //set the first text message part
            multipart.addBodyPart(messageBodyPart);
            //set the second part, which is the attachment
            /*if(courrier.getAttachedFile() != null && !courrier.getAttachedFile().isEmpty()){
                for (ClsCodeLibelle file : courrier.getAttachedFile()) {
                    messageBodyPart = new MimeBodyPart();
                    DataSource source = new FileDataSource(file.getLibelle());
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName(file.getCode());
                    multipart.addBodyPart(messageBodyPart);
                }
            }*/

            //send the entire message parts
            message.setContent(multipart);
            //send the email message
            Transport.send(message);
            //System.out.println("Email Message Sent Successfully");
        } catch (AddressException e) {
            e.printStackTrace();
            return "Error while Sending Mail";
        } catch (jakarta.mail.MessagingException e) {
            e.printStackTrace();
            return "Error while Sending Mail";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error while Sending Mail";
        }

        return "Mail Sent Successfully...";
    }

}
