package com.kinart.portail.business.repository;

import com.kinart.portail.business.model.EnqCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author c.mbassi
 * 06.07.2023
 */
public interface EnqConditionRepository extends JpaRepository<EnqCondition, Integer> {

   List<EnqCondition> findByNmatAndAnnee(@Param("nmat") String nmat, @Param("annee") int annee);

   @Modifying
   void deleteByNmatAndAnnee(@Param("nmat") String nmat, @Param("annee") int annee);
}
