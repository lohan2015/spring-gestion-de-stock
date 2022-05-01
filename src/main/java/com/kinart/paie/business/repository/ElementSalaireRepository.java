package com.kinart.paie.business.repository;

import com.kinart.paie.business.model.ElementSalaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ElementSalaireRepository extends JpaRepository<ElementSalaire, Integer> {

    @Query("select a from ElementSalaire a where upper(crub) = upper(:crub)")
    List<ElementSalaire> findByCodeRubrique(@Param("crub") String crub);

    @Query("select a from ElementSalaire a where crub = :crub")
    ElementSalaire findByCodeRubriqueExactly(@Param("crub") String crub);

    @Query("select a from ElementSalaire a where upper(lrub) like upper(:lrub)")
    List<ElementSalaire> findByLibelleRubrique(@Param("lrub") String lrub);

    // Base de calcul
    @Transactional
    @Modifying
    @Query("delete from ElementSalaireBase where crub = :crub")
    void deleteBaseCalculByCodeElement(@Param("crub") String crub);

    @Transactional
    @Modifying
    @Query("delete from ElementSalaireBase where idelementsalaire = :idelementsalaire")
    void deleteBaseCalculByIdElement(@Param("idelementsalaire") Integer idelementsalaire);

    // Barème
    @Transactional
    @Modifying
    @Query("delete from ElementSalaireBareme where crub = :crub")
    void deleteBaremeByCodeElement(@Param("crub") String crub);

    @Transactional
    @Modifying
    @Query("delete from ElementSalaireBareme where idelementsalaire = :idelementsalaire")
    void deleteBaremeByIdElement(@Param("idelementsalaire") Integer idelementsalaire);
}