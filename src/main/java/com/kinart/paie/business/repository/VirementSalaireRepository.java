package com.kinart.paie.business.repository;

import com.kinart.paie.business.model.VirementSalarie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VirementSalaireRepository extends JpaRepository<VirementSalarie, Integer> {

    @Query("select a from VirementSalarie a where nmat = :matricule")
    VirementSalarie findBySalarie(@Param("matricule") String matricule);
}
