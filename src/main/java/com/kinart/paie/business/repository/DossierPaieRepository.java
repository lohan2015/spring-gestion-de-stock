package com.kinart.paie.business.repository;

import com.kinart.paie.business.model.DossierPaie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DossierPaieRepository extends JpaRepository<DossierPaie, Integer> {

    @Query("select a from DossierPaie a where identreprise = :identreprise")
    DossierPaie findByIdEntreprise(@Param("identreprise") Integer identreprise);
}