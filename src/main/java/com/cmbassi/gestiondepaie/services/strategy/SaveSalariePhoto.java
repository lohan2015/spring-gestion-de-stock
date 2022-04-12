package com.cmbassi.gestiondepaie.services.strategy;

import com.cmbassi.gestiondepaie.dto.SalarieDto;
import com.cmbassi.gestiondepaie.services.SalarieService;
import com.cmbassi.gestiondestock.exception.ErrorCodes;
import com.cmbassi.gestiondestock.exception.InvalidOperationException;
import com.cmbassi.gestiondestock.services.FlickrService;
import com.cmbassi.gestiondestock.services.strategy.Strategy;
import com.flickr4java.flickr.FlickrException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;

@Service("salarieStrategy")
@Slf4j
public class SaveSalariePhoto implements Strategy<SalarieDto> {

  private FlickrService flickrService;
  private SalarieService salarieService;

  @Autowired
  public SaveSalariePhoto(FlickrService flickrService, SalarieService salarieService) {
    this.flickrService = flickrService;
    this.salarieService = salarieService;
  }

  @Override
  public SalarieDto savePhoto(Integer id, InputStream photo, String titre) throws FlickrException {
    SalarieDto dto = salarieService.findById(id);
    String urlPhoto = flickrService.savePhoto(photo, titre);
    if (!StringUtils.hasLength(urlPhoto)) {
      throw new InvalidOperationException("Erreur lors de l'enregistrement de photo du salarié", ErrorCodes.UPDATE_PHOTO_EXCEPTION);
    }
    dto.setPhoto(urlPhoto);
    return salarieService.save(dto);
  }
}
