package com.kinart.paie.business.repository;

import com.kinart.paie.business.model.ElementVariableEnteteMois;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ElementVariableEnteteMoisRepository extends JpaRepository<ElementVariableEnteteMois, Integer> {

    @Query("select a from ElementVariableEnteteMois a where aamm=:aamm and nmat=:nmat and nbul=:nbul")
    ElementVariableEnteteMois findEntEVByMatricule(@Param("aamm") String aamm, @Param("nmat") String nmat, @Param("nbul") Integer nbul);
}