package com.kinart.api.gestiondestock.controller;

import com.kinart.api.gestiondestock.dto.LigneCommandeClientDto;
import com.kinart.api.gestiondestock.dto.LigneCommandeFournisseurDto;
import com.kinart.api.gestiondestock.dto.LigneVenteDto;
import com.kinart.api.gestiondestock.controller.api.ArticleApi;
import com.kinart.api.gestiondestock.dto.ArticleDto;
import com.kinart.stock.business.services.ArticleService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.kinart.stock.business.exception.InvalidEntityException;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticleController implements ArticleApi {

  private ArticleService articleService;

  @Autowired
  public ArticleController(
      ArticleService articleService
  ) {
    this.articleService = articleService;
  }

  @Override
  public ResponseEntity<ArticleDto> save(ArticleDto dto) {
    try {
      articleService.save(dto);
    } catch (InvalidEntityException e){
      return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity(dto, HttpStatus.CREATED);
  }

  @Override
  public ArticleDto findById(Integer id) {
    return articleService.findById(id);
  }

  @Override
  public ArticleDto findByCodeArticle(String codeArticle) {
    return articleService.findByCodeArticle(codeArticle);
  }

  @Override
  public List<ArticleDto> findAll() {
    return articleService.findAll();
  }

  @Override
  public List<LigneVenteDto> findHistoriqueVentes(Integer idArticle) {
    return articleService.findHistoriqueVentes(idArticle);
  }

  @Override
  public List<LigneCommandeClientDto> findHistoriaueCommandeClient(Integer idArticle) {
    return articleService.findHistoriaueCommandeClient(idArticle);
  }

  @Override
  public List<LigneCommandeFournisseurDto> findHistoriqueCommandeFournisseur(Integer idArticle) {
    return articleService.findHistoriqueCommandeFournisseur(idArticle);
  }

  @Override
  public List<ArticleDto> findAllArticleByIdCategory(Integer idCategory) {
    return articleService.findAllArticleByIdCategory(idCategory);
  }

  @Override
  public void delete(Integer id) {
    articleService.delete(id);
  }
}
