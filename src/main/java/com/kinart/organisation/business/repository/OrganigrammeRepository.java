package com.kinart.organisation.business.repository;

import com.kinart.organisation.business.model.Organigramme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrganigrammeRepository extends JpaRepository<Organigramme, Integer> {

    @Query("select a from Organigramme a where codeorganigramme = :codeorganigramme")
    List<Organigramme> findCelluleByCode(@Param("codeorganigramme") String codeorganigramme);

    @Query("select a from Organigramme a where codepere = :codepere")
    List<Organigramme> findCellulePereByCode(@Param("codepere") String codepere);
}
