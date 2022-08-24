package com.kinart.organisation.business.repository;

import com.kinart.organisation.business.model.Orgniveau;
import com.kinart.organisation.business.model.Orgposte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NiveauRepository extends JpaRepository<Orgniveau, Integer> {

    @Query("select a from Orgniveau a where priseencomptecouleur='O' and identreprise = :codedossier")
    List<Orgniveau> findLevelForLegende(@Param("codedossier") String codedossier);
}
