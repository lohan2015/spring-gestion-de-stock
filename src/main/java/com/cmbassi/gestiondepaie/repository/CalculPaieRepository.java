package com.cmbassi.gestiondepaie.repository;

import com.cmbassi.gestiondepaie.model.CalculPaie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CalculPaieRepository extends JpaRepository<CalculPaie, Integer> {

    @Query("select a from CalculPaie a where nmat = :nmat and aamm = :aamm and nbul = :nbul")
    List<CalculPaie> findByMatriculeAndPeriod(@Param("nmat") String nmat, @Param("aamm") String aamm, @Param("nbul") Integer nbul);
}