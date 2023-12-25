package com.kinart.portail.business.repository;

import com.kinart.portail.business.model.EnqBilanCompAtt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author c.mbassi
 * 06.07.2023
 */
public interface EnqBilanCompAttRepository extends JpaRepository<EnqBilanCompAtt, Integer> {

   List<EnqBilanCompAtt> findByNmatAndAnnee(@Param("nmat") String nmat, @Param("annee") int annee);

   @Modifying
   void deleteByNmatAndAnnee(@Param("nmat") String nmat, @Param("annee") int annee);
}
