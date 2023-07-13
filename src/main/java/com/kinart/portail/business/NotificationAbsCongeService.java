package com.kinart.portail.business;

import com.kinart.api.portail.dto.NotifabscongeDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@AllArgsConstructor
@Service
public class NotificationAbsCongeService {

    private RestTemplate restTemplate;

    public void sendNotification() throws Exception {
       System.out.println("-------SET VALUES---------------");
        NotifabscongeDto dto = new NotifabscongeDto();
        dto.setDate_created(new Date());
        dto.setSender("lohan2015@yahoo.com");
        dto.setRecipient("cyrille.mbassi@yaho.com");
        dto.setMessage("Monsieur, merci de valider la demande d'absence de Cyrille MBASSI");

        String urlPost = "http://localhost:8055/items/Notifabsconge";
        String urlPost2 = "http://localhost:8055/items/notifpret";
        System.out.println("Sender:---"+dto.getSender());
        //ResponseEntity<Notifabsconge> notifAbsConge = restTemplate.postForEntity(urlPost, dto, Notifabsconge.class);
        ResponseEntity<NotifabscongeDto> notifPret = restTemplate.postForEntity(urlPost2, dto, NotifabscongeDto.class);
        //System.out.println("Id code="+notifAbsConge.getBody().getId());



    }
}
