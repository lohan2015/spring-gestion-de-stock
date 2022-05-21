package com.kinart.paie.business.repository;

import com.kinart.paie.business.model.CalculPaie;
import com.kinart.paie.business.model.CongeFictif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CongeFictifRepository extends JpaRepository<CongeFictif, Integer> {

    @Query("select a from CongeFictif a where nmat = :nmat and aamm = :aamm and nbul = :nbul")
    List<CongeFictif> findByMatriculeAndPeriod(@Param("nmat") String nmat, @Param("aamm") String aamm, @Param("nbul") Integer nbul);
}
