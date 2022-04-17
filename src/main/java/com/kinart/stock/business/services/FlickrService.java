package com.kinart.stock.business.services;

import java.io.InputStream;

public interface FlickrService {

  String savePhoto(InputStream photo, String title);

}
