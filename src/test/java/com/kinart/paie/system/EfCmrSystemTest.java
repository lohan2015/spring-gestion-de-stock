package com.kinart.paie.system;

import com.kinart.api.gestiondepaie.dto.ParameterOfDipeDto;
import com.kinart.stock.business.utils.Constants;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class EfCmrSystemTest {

//    @Test
//    void testCreateReadDelete() throws Exception {
//        RestTemplate restTemplate = new RestTemplate();
//
//        ParameterOfDipeDto dto = new ParameterOfDipeDto();
//        dto.setAamm("201811");
//        dto.setAnnee("2018");
//        dto.setCdos("45");
//        dto.setCuti("test@gmail.com");
//        dto.setNbul(9);
//        dto.setTypeDipe(0);
//
//        String host = "http://localhost:8081/";
//        String urlPost = host + Constants.APP_ROOT + "/efcmr/dipemagnetique";
//        ResponseEntity<ParameterOfDipeDto> entrepriseDtoResponseEntity = restTemplate.postForEntity(urlPost, dto, ParameterOfDipeDto.class);
//
//        Assertions.assertThat(entrepriseDtoResponseEntity.getBody().cheminDipeMensuel).isEqualTo("file:///D:/Programmation%20orientee%20objet/Technologies/Angular/projets/gestiondestock/generated-dipes/45-201811-C04.txt");
//    }


}
