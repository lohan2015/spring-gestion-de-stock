package com.kinart.stock.business.services.strategy;

import com.kinart.api.gestiondestock.dto.FournisseurDto;
import com.kinart.stock.business.exception.ErrorCodes;
import com.kinart.stock.business.exception.InvalidOperationException;
import com.kinart.stock.business.services.FlickrService;
import com.kinart.stock.business.services.FournisseurService;
import com.flickr4java.flickr.FlickrException;
import java.io.InputStream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service("fournisseurStrategy")
@Slf4j
public class SaveFournisseurPhoto implements Strategy<FournisseurDto> {

  private FlickrService flickrService;
  private FournisseurService fournisseurService;

  @Autowired
  public SaveFournisseurPhoto(FlickrService flickrService, FournisseurService fournisseurService) {
    this.flickrService = flickrService;
    this.fournisseurService = fournisseurService;
  }

  @Override
  public FournisseurDto savePhoto(Integer id, InputStream photo, String titre) throws FlickrException {
    FournisseurDto fournisseur = fournisseurService.findById(id);
    String urlPhoto = flickrService.savePhoto(photo, titre);
    if (!StringUtils.hasLength(urlPhoto)) {
      throw new InvalidOperationException("Erreur lors de l'enregistrement de photo du fournisseur", ErrorCodes.UPDATE_PHOTO_EXCEPTION);
    }
    fournisseur.setPhoto(urlPhoto);
    return fournisseurService.save(fournisseur);
  }
}
