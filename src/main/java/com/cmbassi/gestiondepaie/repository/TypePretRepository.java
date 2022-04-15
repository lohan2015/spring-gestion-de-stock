package com.cmbassi.gestiondepaie.repository;


import com.cmbassi.gestiondepaie.model.TypePret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TypePretRepository extends JpaRepository<TypePret, Integer> {

    @Query("select a from TypePret a where crub = :crub")
    TypePret findByCodeRubrique(@Param("crub") String crub);

    @Query("select a from TypePret a where codenatpret = :codenatpret")
    TypePret findByCodeNature(@Param("codenatpret") String codenatpret);
}