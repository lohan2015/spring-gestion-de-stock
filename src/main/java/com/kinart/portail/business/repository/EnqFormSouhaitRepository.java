package com.kinart.portail.business.repository;

import com.kinart.portail.business.model.EnqFormSouhait;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author c.mbassi
 * 06.07.2023
 */
public interface EnqFormSouhaitRepository extends JpaRepository<EnqFormSouhait, Integer> {

   List<EnqFormSouhait> findByNmatAndAnnee(@Param("nmat") String nmat, @Param("annee") int annee);

   @Modifying
   void deleteByNmatAndAnnee(@Param("nmat") String nmat, @Param("annee") int annee);
}
