package com.kinart.stock.business.services.impl;

import com.kinart.api.gestiondestock.dto.ArticleDto;
import com.kinart.api.gestiondestock.dto.LigneCommandeClientDto;
import com.kinart.api.gestiondestock.dto.LigneCommandeFournisseurDto;
import com.kinart.api.gestiondestock.dto.LigneVenteDto;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.ErrorCodes;
import com.kinart.stock.business.exception.InvalidEntityException;
import com.kinart.stock.business.exception.InvalidOperationException;
import com.kinart.stock.business.model.LigneCommandeClient;
import com.kinart.stock.business.model.LigneCommandeFournisseur;
import com.kinart.stock.business.model.LigneVente;
import com.kinart.stock.business.repository.ArticleRepository;
import com.kinart.stock.business.repository.LigneCommandeClientRepository;
import com.kinart.stock.business.repository.LigneCommandeFournisseurRepository;
import com.kinart.stock.business.repository.LigneVenteRepository;
import com.kinart.stock.business.services.ArticleService;
import com.kinart.stock.business.validator.ArticleValidator;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

  private ArticleRepository articleRepository;
  private LigneVenteRepository venteRepository;
  private LigneCommandeFournisseurRepository commandeFournisseurRepository;
  private LigneCommandeClientRepository commandeClientRepository;

  @Autowired
  public ArticleServiceImpl(
      ArticleRepository articleRepository,
      LigneVenteRepository venteRepository, LigneCommandeFournisseurRepository commandeFournisseurRepository,
      LigneCommandeClientRepository commandeClientRepository) {
    this.articleRepository = articleRepository;
    this.venteRepository = venteRepository;
    this.commandeFournisseurRepository = commandeFournisseurRepository;
    this.commandeClientRepository = commandeClientRepository;
  }

  @Override
  public ArticleDto save(ArticleDto dto) {
    List<String> errors = ArticleValidator.validate(dto);
    if (!errors.isEmpty()) {
      log.error("Article is not valid {}", dto);
      throw new InvalidEntityException("L'article n'est pas valide", ErrorCodes.ARTICLE_NOT_VALID, errors);
    }

    return ArticleDto.fromEntity(
        articleRepository.save(
            ArticleDto.toEntity(dto)
        )
    );
  }

  @Override
  public ArticleDto findById(Integer id) {
    if (id == null) {
      log.error("Article ID is null");
      return null;
    }

    return articleRepository.findById(id).map(ArticleDto::fromEntity).orElseThrow(() ->
        new EntityNotFoundException(
            "Aucun article avec l'ID = " + id + " n' ete trouve dans la BDD",
            ErrorCodes.ARTICLE_NOT_FOUND)
    );
  }

  @Override
  public ArticleDto findByCodeArticle(String codeArticle) {
    if (!StringUtils.hasLength(codeArticle)) {
      log.error("Article CODE is null");
      return null;
    }

    return articleRepository.findArticleByCodeArticle(codeArticle)
        .map(ArticleDto::fromEntity)
        .orElseThrow(() ->
            new EntityNotFoundException(
                "Aucun article avec le CODE = " + codeArticle + " n' ete trouve dans la BDD",
                ErrorCodes.ARTICLE_NOT_FOUND)
        );
  }

  @Override
  public List<ArticleDto> findAll() {
    return articleRepository.findAll().stream()
        .map(ArticleDto::fromEntity)
        .collect(Collectors.toList());
  }

  @Override
  public List<LigneVenteDto> findHistoriqueVentes(Integer idArticle) {
    return venteRepository.findAllByArticleId(idArticle).stream()
        .map(LigneVenteDto::fromEntity)
        .collect(Collectors.toList());
  }

  @Override
  public List<LigneCommandeClientDto> findHistoriaueCommandeClient(Integer idArticle) {
    return commandeClientRepository.findAllByArticleId(idArticle).stream()
        .map(LigneCommandeClientDto::fromEntity)
        .collect(Collectors.toList());
  }

  @Override
  public List<LigneCommandeFournisseurDto> findHistoriqueCommandeFournisseur(Integer idArticle) {
    return commandeFournisseurRepository.findAllByArticleId(idArticle).stream()
        .map(LigneCommandeFournisseurDto::fromEntity)
        .collect(Collectors.toList());
  }

  @Override
  public List<ArticleDto> findAllArticleByIdCategory(Integer idCategory) {
    return articleRepository.findAllByCategoryId(idCategory).stream()
        .map(ArticleDto::fromEntity)
        .collect(Collectors.toList());
  }

  @Override
  public void delete(Integer id) {
    if (id == null) {
      log.error("Article ID is null");
      return;
    }
    List<LigneCommandeClient> ligneCommandeClients = commandeClientRepository.findAllByArticleId(id);
    if (!ligneCommandeClients.isEmpty()) {
      throw new InvalidOperationException("Impossible de supprimer un article deja utilise dans des commandes client", ErrorCodes.ARTICLE_ALREADY_IN_USE);
    }
    List<LigneCommandeFournisseur> ligneCommandeFournisseurs = commandeFournisseurRepository.findAllByArticleId(id);
    if (!ligneCommandeFournisseurs.isEmpty()) {
      throw new InvalidOperationException("Impossible de supprimer un article deja utilise dans des commandes fournisseur",
          ErrorCodes.ARTICLE_ALREADY_IN_USE);
    }
    List<LigneVente> ligneVentes = venteRepository.findAllByArticleId(id);
    if (!ligneVentes.isEmpty()) {
      throw new InvalidOperationException("Impossible de supprimer un article deja utilise dans des ventes",
          ErrorCodes.ARTICLE_ALREADY_IN_USE);
    }
    articleRepository.deleteById(id);
  }
}
