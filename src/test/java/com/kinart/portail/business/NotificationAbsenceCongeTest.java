package com.kinart.portail.business;

import com.kinart.api.portail.dto.NotifabscongeDto;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

public class NotificationAbsenceCongeTest {


    @Test
    void testCreateReadDelete() throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        NotifabscongeDto dto = new NotifabscongeDto();
        dto.setDate_created(new Date());
        dto.setSender("lohan2015@yahoo.com");
        dto.setRecipient("cyrille.mbassi@yaho.com");
        dto.setMessage("Monsieur, merci de valider la demande d'absence de Cyrille MBASSI");

        String urlPost = "http://localhost:8055/items/notifabsconge";
        ResponseEntity<NotifabscongeDto> entrepriseDtoResponseEntity = restTemplate.postForEntity(urlPost, dto, NotifabscongeDto.class);

        Assertions.assertThat(entrepriseDtoResponseEntity.getBody().getId()).isEqualTo(1);
    }

}
