package com.cmbassi.gestiondestock.repository;

import com.cmbassi.gestiondestock.model.Article;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends JpaRepository<Article, Integer> {

  Optional<Article> findArticleByCodeArticle(String codeArticle);


  List<Article> findAllByCategoryId(Integer idCategory);

  /*@Query("select a from Article a where codearticle = :code and desgnation = :designation")
  List<Article> findByCustomQuery(@Param("code") String code, @Param("desgnation") String desgnation);

  @Query(value = "select * from Article a where codearticle = :code", nativeQuery = true)
  List<Article> findByCustomNativeQuery(@Param("code") String code);

  List<Article> findArticleByCodeArticleAndDesignation(String codeArticle, String designation);

  List<Article> findArticleByCodeArticleIgnoreCaseAnAndDesignationIgnoreCase(String codeArticle, String designation);
  */
}
