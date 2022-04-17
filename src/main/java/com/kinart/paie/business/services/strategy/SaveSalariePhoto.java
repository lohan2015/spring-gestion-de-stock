package com.kinart.paie.business.services.strategy;

import com.kinart.api.gestiondepaie.dto.SalarieDto;
import com.kinart.paie.business.services.SalarieService;
import com.kinart.stock.business.exception.ErrorCodes;
import com.kinart.stock.business.exception.InvalidOperationException;
import com.kinart.stock.business.services.FlickrService;
import com.kinart.stock.business.services.strategy.Strategy;
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
